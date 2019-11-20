package nl.icsvertex.scansuite.Activities.Intake;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import ICS.Interfaces.iICSDefaultActivity;
import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cResult;
import ICS.Utils.cText;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Basics.BarcodeLayouts.cBarcodeLayout;

import SSU_WHS.General.Comments.cComment;
import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.General.cPublicDefinitions;
import SSU_WHS.Intake.IntakeorderBarcodes.cIntakeorderBarcode;
import SSU_WHS.Intake.IntakeorderMATLines.cIntakeorderMATLine;
import SSU_WHS.Intake.Intakeorders.cIntakeorder;
import nl.icsvertex.scansuite.Fragments.Dialogs.AcceptRejectFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.CommentFragment;

import nl.icsvertex.scansuite.Fragments.Dialogs.OrderDoneFragment;

import nl.icsvertex.scansuite.Fragments.Dialogs.SendingFragment;

import nl.icsvertex.scansuite.R;

public class IntakeorderLinesActivity extends AppCompatActivity implements iICSDefaultActivity {

    //Region Public Properties
    public static String VIEW_CHOSEN_ORDER = "detail:header:text";
    static final String ACCEPTREJECTFRAGMENT_TAG = "ACCEPTREJECTFRAGMENT_TAG";
    private static String SENDING_TAG = "SENDING_TAG";
    public static Fragment currentLineFragment;
    //End Region Public Properties

    //Region Private Properties

    //Region Views

    private TextView textViewChosenOrder;
    static private ImageView imageButtonComments;

    private ImageView toolbarImage;
    private TextView toolbarTitle;
    private static TextView toolbarSubTitle;

    private static  TextView quickhelpText;
    private static ImageView quickhelpIcon;
    private ConstraintLayout quickhelpContainer;
    private static ImageView imageViewStart;

    private static SearchView recyclerSearchView;
    private static ImageView closeButton;
    private static RecyclerView recyclerViewLines;

    //End Region Views

    //End Region Private Properties

    //Region Default Methods

    @Override
    protected void onCreate(Bundle pvSavedInstanceState) {
        super.onCreate(pvSavedInstanceState);
        setContentView(R.layout.activity_intakeorderlines);
        this.mActivityInitialize();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        //Set listeners here, so click listeners only work after activity is shown
        this.mSetListeners();

        //Click so we can show some instructions
        this.quickhelpContainer.performClick();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        cBarcodeScan.pRegisterBarcodeReceiver();
        super.onResume();
    }

    @Override
    protected void onPause() {
        try {
            cBarcodeScan.pUnregisterBarcodeReceiver();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onPause();
    }

    //End Region Default Methods

    //Region iICSDefaultActivity defaults

    @Override
    public void mActivityInitialize() {

        this.mSetAppExtensions();

        this.mFindViews();

        this.mSetToolbar(getResources().getString(R.string.screentitle_intakeorderlines));

        this.mFieldsInitialize();

        this.mInitScreen();

        cBarcodeScan.pRegisterBarcodeReceiver();
    }

    @Override
    public void mSetAppExtensions() {
        cAppExtension.context = this;
        cAppExtension.fragmentActivity = this;
        cAppExtension.activity = this;
        cAppExtension.fragmentManager = getSupportFragmentManager();
    }

    @Override
    public void mFindViews() {

        this.toolbarImage = findViewById(R.id.toolbarImage);
        this.toolbarTitle = findViewById(R.id.toolbarTitle);
        this.toolbarSubTitle = findViewById(R.id.toolbarSubtext);
        this.textViewChosenOrder = findViewById(R.id.textViewChosenOrder);
        this.imageButtonComments = findViewById(R.id.imageButtonComments);

        this.recyclerSearchView = findViewById(R.id.recyclerSearchView);

        this.closeButton =  this.recyclerSearchView.findViewById(R.id.search_close_btn);

        this.recyclerViewLines = findViewById(R.id.recyclerViewLines);

        this.imageViewStart = findViewById(R.id.imageViewStart);

        this.quickhelpText = findViewById(R.id.quickhelpText);
        this.quickhelpContainer = findViewById(R.id.actionsContainer);
        this.quickhelpIcon = findViewById(R.id.quickhelpIcon);


    }

    @Override
    public void mSetToolbar(String pvScreenTitleStr) {
        this.toolbarImage.setImageResource(R.drawable.ic_menu_intake);
        this.toolbarTitle.setText(pvScreenTitleStr);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void mFieldsInitialize() {

        ViewCompat.setTransitionName(this.textViewChosenOrder, this.VIEW_CHOSEN_ORDER);
        this.textViewChosenOrder.setText(cIntakeorder.currentIntakeOrder.getOrderNumberStr());
        this.quickhelpText.setText(cAppExtension.activity.getString(R.string.scan_article));

        this.mGetData();

    }

    @Override
    public void mSetListeners() {
        this.mSetRecyclerOnScrollListener();
        this.mSetSearchListener();
        this.mSetSearchCloseListener();
        this.mSetShowCommentListener();
        this.mSetQuickHelpListener();
    }

    @Override
    public void mInitScreen() {

        this.mShowComments();

        //Call this here, because this is called everytime the activiy gets shown
        this.mCheckAllDone();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem pvMenuItem) {

        if (pvMenuItem.getItemId() == android.R.id.home) {
            mTryToLeaveActivity();
            return true;
        }

        return super.onOptionsItemSelected(pvMenuItem);
    }

    @Override
    public void onBackPressed() {
        mTryToLeaveActivity();
    }

    //End Region iICSDefaultActivity defaults

    //Region Public Methods

    public static void pIntakelineSelected(cIntakeorderMATLine pvIntakeorderMATLine) {
        cIntakeorderMATLine.currentIntakeorderMATLine = pvIntakeorderMATLine;
    }

    public static void pIntakelineToResetSelected(cIntakeorderMATLine pvIntakeorderMATLine) {
        cIntakeorderMATLine.currentIntakeorderMATLine = pvIntakeorderMATLine;
    }

    public static void pHandleScan(cBarcodeScan pvBarcodeScan, Boolean pvLineSelectedBln) {

        cUserInterface.pCheckAndCloseOpenDialogs();
        cResult hulpResult;
        String instructionStr = "";

        //BIN button has been pressed, so we already have a current line
        if (pvLineSelectedBln) {

            //Clear current barcode
            cIntakeorderBarcode.currentIntakeOrderBarcode = null;

            hulpResult = cIntakeorderMATLine.currentIntakeorderMATLine.pLineBusyRst();
            if (!hulpResult.resultBln) {
                mStepFailed(hulpResult.messagesStr());
                cIntakeorderMATLine.currentIntakeorderMATLine = null;
                return;
            }

           IntakeorderLinesActivity.mStartStoreActivity();

        }

        //Check if we have scanned a BIN and check if there are not handled linesInt for this BIN
        if (cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(), cBarcodeLayout.barcodeLayoutEnu.BIN)) {
            //unknown scan
            mDoUnknownScan(cAppExtension.context.getString(R.string.message_bin_not_allowed), pvBarcodeScan.getBarcodeOriginalStr());
            return;
        }

        //Check if we have scanned an ARTICLE and check if there are not handled linesInt for this ARTICLE
        if (cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(), cBarcodeLayout.barcodeLayoutEnu.ARTICLE)) {

            //Handle the ARTICLE scan
            hulpResult = IntakeorderLinesActivity.mHandleArticleScan(pvBarcodeScan);

            //Something went wrong, so show message and stop
            if (!hulpResult.resultBln) {
                mDoUnknownScan(hulpResult.messagesStr(), pvBarcodeScan.getBarcodeOriginalStr());
                IntakeorderLinesActivity.mFillRecycler(cIntakeorder.currentIntakeOrder.pGetLinesFromDatabasObl());
                return;
            }

            //Hide the fucking keyboard
            cUserInterface.pHideKeyboard();

            //Filter has been set in mHandleArticleScan so we are done
            return;

        }

        //unknown scan
        mDoUnknownScan(cAppExtension.context.getString(R.string.error_unknown_barcode), pvBarcodeScan.getBarcodeOriginalStr());

    }

    public static void pDone() {

        // Show that we are getting data
        cUserInterface.pShowGettingData();

        new Thread(new Runnable() {
            public void run() {
                mHandleClose();
            }
        }).start();

    }

    public static void pLeaveActivity() {

        cIntakeorder.currentIntakeOrder.pLockReleaseViaWebserviceBln();
        mStartOrderSelectActivity();
        return;

    }

    public static void pSetToolBarTitleWithCounters(String pvTextStr){
        IntakeorderLinesActivity.toolbarSubTitle.setText(pvTextStr);

        //Close open dialogs, so keyboard will also close
        cUserInterface.pHideKeyboard();

        //Click to make even more sure that keyboard gets hidden
        IntakeorderLinesActivity.toolbarSubTitle.performClick();

    }


    //End Region Public Methods

    //Region Private Methods

    private static cResult mHandleArticleScan(cBarcodeScan pvBarcodeScan) {

        cResult result = new cResult();
        result.resultBln = true;

        String searchStr;

        //Check if this is a barcode we already know
        cIntakeorderBarcode.currentIntakeOrderBarcode = cIntakeorder.currentIntakeOrder.pGetOrderBarcode(pvBarcodeScan);
        if (cIntakeorderBarcode.currentIntakeOrderBarcode == null) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.activity.getString(R.string.message_unknown_barcode));
            return result;
        }

        //Get all lines for this barcode
        if ( cIntakeorderBarcode.currentIntakeOrderBarcode.linesObl().size() == 0) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.activity.getString(R.string.message_no_lines_for_this_barcode));
            return result;
        }

        searchStr = cIntakeorderBarcode.currentIntakeOrderBarcode.getItemNoStr();

        if (!cIntakeorderBarcode.currentIntakeOrderBarcode.getVariantCodeStr().isEmpty()) {
            searchStr += ' ' + cIntakeorderBarcode.currentIntakeOrderBarcode.getVariantCodeStr();
        }

        IntakeorderLinesActivity.recyclerSearchView.setQuery(searchStr, true);
        IntakeorderLinesActivity.recyclerSearchView.callOnClick();

        //Article is known and also not handled, so everything is fine
        return result;
    }

    private static boolean mTryToCloseOrderBln() {

        cResult hulpResult;
        hulpResult = new cResult();
        hulpResult.resultBln = false;

        hulpResult = cIntakeorder.currentIntakeOrder.pHandledViaWebserviceRst();

        //Everything was fine, so we are done
        if (hulpResult.resultBln) {
            return true;
        }

        //Something went wrong, but no further actions are needed, so ony show reason of failure
        if (!hulpResult.resultBln && hulpResult.activityActionEnu == cWarehouseorder.ActivityActionEnu.Unknown) {
            cUserInterface.pDoExplodingScreen(hulpResult.messagesStr(), "", true, true);
            return false;
        }

        //Something went wrong, the order has been deleted, so show comments and refresh
        if (!hulpResult.resultBln && hulpResult.activityActionEnu == cWarehouseorder.ActivityActionEnu.Hold) {

            //If we got any comments, show them
            if (cIntakeorder.currentIntakeOrder.pFeedbackCommentObl() != null && cIntakeorder.currentIntakeOrder.pFeedbackCommentObl().size() > 0) {
                //Process comments from webresult
                IntakeorderLinesActivity.mShowCommentsFragment(cIntakeorder.currentIntakeOrder.pFeedbackCommentObl(), hulpResult.messagesStr());
            }

            return false;
        }

        return true;

    }

    private static void mHandleClose() {

        if (!IntakeorderLinesActivity.mTryToCloseOrderBln()) {
            return;
        }

        mStartOrderSelectActivity();

    }

    private void mCheckAllDone() {

        // If not everything is done, then leave
        if (!this.mAllLinesDoneBln()) {
            return;
        }

        // If not everything is sent, then leave
        if (!this.mCheckAndSentLinesBln(true)) {
            return;
        }

        IntakeorderLinesActivity.mShowOrderDoneFragment();
    }

    private void mSetShowCommentListener() {
        this.imageButtonComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mShowCommentsFragment(cIntakeorder.currentIntakeOrder.pCommentObl(), "");
            }
        });
    }

    private static void mDoUnknownScan(String pvErrorMessageStr, String pvScannedBarcodeStr) {
        cUserInterface.pDoExplodingScreen(pvErrorMessageStr, pvScannedBarcodeStr, true, true);
    }

    private static void mShowCommentsFragment(List<cComment> pvDataObl, String pvTitleStr) {

        cUserInterface.pCheckAndCloseOpenDialogs();

        Bundle bundle = new Bundle();
        bundle.putString(cPublicDefinitions.KEY_COMMENTHEADER, pvTitleStr);

        CommentFragment commentFragment = new CommentFragment(pvDataObl);
        commentFragment.setArguments(bundle);

        commentFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.COMMENTFRAGMENT_TAG);
        cUserInterface.pPlaySound(R.raw.message, 0);
    }

    private static void mShowOrderDoneFragment() {

        cUserInterface.pPlaySound(R.raw.goodsound, null);

        final OrderDoneFragment orderDoneFragment = new OrderDoneFragment(false);
        orderDoneFragment.setCancelable(false);
        orderDoneFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.ORDERDONE_TAG);
    }

    private Boolean mCheckAndSentLinesBln(Boolean pvShowAnimationBln) {

        final List<cIntakeorderMATLine> linesToSendObl = cIntakeorder.currentIntakeOrder.pGetLinesToSendFromDatabasObl();

        // If there is nothing to send, then we are done
        if (linesToSendObl.size() == 0) {
            return true;
        }


        if (pvShowAnimationBln) {
            mShowSending();
        }

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Callable<Boolean> callableBln = new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {

                // Try to send each line, if one failes then stop
                for (cIntakeorderMATLine intakeorderMATLine : linesToSendObl) {

                    //Set the current line
                    cIntakeorderMATLine.currentIntakeorderMATLine = intakeorderMATLine;

                    //Try to send the line
                    return cIntakeorderMATLine.currentIntakeorderMATLine.pHandledBln();

                }
                return false;
            }
        };


        try {
            Future<Boolean> callableResultBln = executorService.submit(callableBln);
            Boolean hulpBln = callableResultBln.get();

            if (!hulpBln) {

                if (pvShowAnimationBln) {
                    mShowNotSent();
                }

                return false;
            }

            if (pvShowAnimationBln) {
                this.mShowSent();
            }

            return true;

        } catch (InterruptedException | ExecutionException e) {

        }

        return false;


    }

    private Boolean mAllLinesDoneBln() {
        return false;
    }

    private static void mShowSending() {
        final SendingFragment sendingFragment = new SendingFragment();
        sendingFragment.setCancelable(true);
        cAppExtension.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // show my popup
                sendingFragment.show(cAppExtension.fragmentManager, SENDING_TAG);
            }
        });
    }

    private static void mShowNotSent() {
        Fragment fragment = cAppExtension.fragmentManager.findFragmentByTag(SENDING_TAG);
        if (fragment != null) {
            if (fragment instanceof SendingFragment) {
                ((SendingFragment) fragment).pShowCrashAnimation("");
            }
        }
    }

    private static void mShowSent() {
        Fragment fragment = cAppExtension.fragmentManager.findFragmentByTag(SENDING_TAG);
        if (fragment != null) {
            if (fragment instanceof SendingFragment) {
                ((SendingFragment) fragment).pShowFlyAwayAnimation();
            }
        }
    }

    private static void mStepFailed(String pvErrorMessageStr) {
        cUserInterface.pDoExplodingScreen(pvErrorMessageStr, cIntakeorder.currentIntakeOrder.getOrderNumberStr(), true, true);
        cIntakeorder.currentIntakeOrder.pLockReleaseViaWebserviceBln();
        cUserInterface.pCheckAndCloseOpenDialogs();
    }

    private void mTryToLeaveActivity() {

        cUserInterface.pCheckAndCloseOpenDialogs();

        final AcceptRejectFragment acceptRejectFragment = new AcceptRejectFragment(cAppExtension.activity.getString(R.string.message_sure_leave_screen_title),
                cAppExtension.activity.getString(R.string.message_sure_leave_screen_text), cAppExtension.activity.getString(R.string.message_cancel), cAppExtension.activity.getString(R.string.message_leave));
        acceptRejectFragment.setCancelable(true);
        cAppExtension.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // show my popup
                acceptRejectFragment.show(cAppExtension.fragmentManager, ACCEPTREJECTFRAGMENT_TAG);
            }
        });

    }

    private static void mStartOrderSelectActivity() {

        cAppExtension.activity.runOnUiThread(new Runnable() {
            public void run() {
                Intent intent = new Intent(cAppExtension.context, IntakeorderSelectActivity.class);
                cAppExtension.activity.startActivity(intent);
            }
        });

    }

    private static void mShowComments() {

        if (cIntakeorder.currentIntakeOrder.pCommentObl() == null || cIntakeorder.currentIntakeOrder.pCommentObl().size() == 0) {
            IntakeorderLinesActivity.imageButtonComments.setVisibility(View.INVISIBLE);
            return;
        }

        IntakeorderLinesActivity.imageButtonComments.setVisibility(View.VISIBLE);

        //We already showed the comments
        if (cComment.commentsShownBln) {
            return;
        }

        IntakeorderLinesActivity.mShowCommentsFragment(cIntakeorder.currentIntakeOrder.pCommentObl(), "");
        cComment.commentsShownBln = true;
    }

    private static void mStartStoreActivity(){


        //we have a LINE to handle, so start Store activity
        Intent intent = new Intent(cAppExtension.context, IntakeOrderIntakeActivity.class);
        cAppExtension.activity.startActivity(intent);

    }

    private void mSetQuickHelpListener() {
        this.quickhelpContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cUserInterface.pDoRotate(quickhelpIcon, 0);
                if (quickhelpText.getVisibility() == View.VISIBLE) {
                    quickhelpText.setVisibility(View.GONE);
                }
                else {
                    quickhelpText.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void mGetData() {
        this.mFillRecycler(cIntakeorder.currentIntakeOrder.pGetLinesFromDatabasObl());
    }

    private static void mFillRecycler(List<cIntakeorderMATLine> pvDataObl) {

        if (pvDataObl.size() == 0) {
            IntakeorderLinesActivity.imageViewStart.setVisibility(View.INVISIBLE);
            return;
        }

        IntakeorderLinesActivity.imageViewStart.setVisibility(View.VISIBLE);

        //Show the recycler view
        cIntakeorderMATLine.getIntakeorderMATLineAdapter().pFillData(pvDataObl);
        IntakeorderLinesActivity.recyclerViewLines.setHasFixedSize(false);
        IntakeorderLinesActivity.recyclerViewLines.setAdapter(cIntakeorderMATLine.getIntakeorderMATLineAdapter());
        IntakeorderLinesActivity.recyclerViewLines.setLayoutManager(new LinearLayoutManager(cAppExtension.context));
        IntakeorderLinesActivity.recyclerViewLines.setVisibility(View.VISIBLE);

        IntakeorderLinesActivity.pSetToolBarTitleWithCounters("(" + cText.pIntToStringStr(pvDataObl.size())  + "/" + cText.pIntToStringStr(cIntakeorder.currentIntakeOrder.linesObl().size()) + ") " + cAppExtension.activity.getString(R.string.lines) + " " + cAppExtension.activity.getString(R.string.shown) );

    }

    private void mSetRecyclerOnScrollListener() {

        IntakeorderLinesActivity.recyclerViewLines.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView pvRecyclerView, int pvNewStateInt) {
                super.onScrollStateChanged(pvRecyclerView, pvNewStateInt);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutmanager = (LinearLayoutManager)recyclerView.getLayoutManager();
                if (dy < 0) {

                    int itemPosition = 0;
                    if (layoutmanager != null) {
                        itemPosition = layoutmanager.findFirstCompletelyVisibleItemPosition();
                    }

                    if(itemPosition==0){
                        // Prepare the View for the animation
                        IntakeorderLinesActivity.recyclerSearchView.setVisibility(View.VISIBLE);
                        IntakeorderLinesActivity.recyclerSearchView.setAlpha(0.0f);

                        // Start the animation
                        IntakeorderLinesActivity.recyclerSearchView.animate()
                                .translationY(0)
                                .alpha(1.0f)
                                .setListener(null);

                    }

                } else {

                    int itemPosition = 0;
                    if (layoutmanager != null) {
                        itemPosition = layoutmanager.findFirstCompletelyVisibleItemPosition();
                    }

                    if(itemPosition>1){// your *second item your recyclerview
                        // Start the animation
                        IntakeorderLinesActivity.recyclerSearchView.setVisibility(View.GONE);
                    }

                }
            }
        });
    }

    private void mSetSearchListener() {
        //make whole view clickable
        IntakeorderLinesActivity.recyclerSearchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View pvView) {
                IntakeorderLinesActivity.recyclerSearchView.setIconified(false);
            }
        });

        //query entered
        this.recyclerSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String pvString) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String pvQueryTextStr) {
                cIntakeorderMATLine.getIntakeorderMATLineAdapter().pSetFilter(pvQueryTextStr);
                return true;
            }

        });

        return;

    }

    private void mSetSearchCloseListener() {
        //make whole view clickable
        IntakeorderLinesActivity.closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View pvView) {
                EditText et = findViewById(R.id.search_src_text);

                //Clear the text from EditText view
                et.setText("");

                //Clear query
                recyclerSearchView.setQuery("", false);

            }
        });

        //query entered


    }



    //End Region Private Methods

}
