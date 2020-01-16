package nl.icsvertex.scansuite.Activities.Intake;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ICS.Interfaces.iICSDefaultActivity;
import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cResult;
import ICS.Utils.cText;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Basics.BarcodeLayouts.cBarcodeLayout;
import SSU_WHS.Basics.Settings.cSetting;
import SSU_WHS.General.Comments.cComment;
import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.General.cPublicDefinitions;
import SSU_WHS.Intake.IntakeorderBarcodes.cIntakeorderBarcode;
import SSU_WHS.Intake.IntakeorderMATLineSummary.cIntakeorderMATSummaryLine;
import SSU_WHS.Intake.Intakeorders.cIntakeorder;
import nl.icsvertex.scansuite.Fragments.Dialogs.AcceptRejectFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.CommentFragment;
import nl.icsvertex.scansuite.R;

public class IntakeorderLinesActivity extends AppCompatActivity implements iICSDefaultActivity {

    //Region Public Properties
    public static String VIEW_CHOSEN_ORDER = "detail:header:text";
    static final String ACCEPTREJECTFRAGMENT_TAG = "ACCEPTREJECTFRAGMENT_TAG";
    public static Fragment currentLineFragment;
    //End Region Public Properties

    //Region Private Properties

    //Region Views

    private static TextView textViewChosenOrder;
    private static ImageView imageButtonComments;

    private static ImageView toolbarImage;
    private static TextView toolbarTitle;
    private static TextView toolbarSubTitle;

    private static ImageView imageViewStart;
    private static SearchView recyclerSearchView;
    private static ImageView closeButton;
    private static RecyclerView recyclerViewLines;

    private static Switch switchDeviations;
    public static Boolean showDefectsBln = false;

    private static ImageView imageButtonCloseOrder;



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

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cBarcodeScan.pRegisterBarcodeReceiver();
        cUserInterface.pEnableScanner();
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

        IntakeorderLinesActivity.pShowData(cIntakeorderMATSummaryLine.allIntakeorderMATSummaryLinesObl);

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

        IntakeorderLinesActivity.toolbarImage = findViewById(R.id.toolbarImage);
        IntakeorderLinesActivity.toolbarTitle = findViewById(R.id.toolbarTitle);
        IntakeorderLinesActivity.toolbarSubTitle = findViewById(R.id.toolbarSubtext);
        IntakeorderLinesActivity.textViewChosenOrder = findViewById(R.id.textViewChosenOrder);
        IntakeorderLinesActivity.imageButtonComments = findViewById(R.id.imageButtonComments);

        IntakeorderLinesActivity.recyclerSearchView = findViewById(R.id.recyclerSearchView);

        IntakeorderLinesActivity.closeButton =  IntakeorderLinesActivity.recyclerSearchView.findViewById(R.id.search_close_btn);

        IntakeorderLinesActivity.recyclerViewLines = findViewById(R.id.recyclerViewLines);

        IntakeorderLinesActivity.imageViewStart = findViewById(R.id.imageViewStart);

        IntakeorderLinesActivity.switchDeviations = findViewById(R.id.switchDeviations);

        IntakeorderLinesActivity.imageButtonCloseOrder = findViewById(R.id.imageButtonCloseOrder);

    }

    @Override
    public void mSetToolbar(String pvScreenTitleStr) {
        IntakeorderLinesActivity.toolbarImage.setImageResource(R.drawable.ic_menu_intake);
        IntakeorderLinesActivity.toolbarTitle.setText(pvScreenTitleStr);
        IntakeorderLinesActivity.toolbarTitle.setSelected(true);
        IntakeorderLinesActivity.toolbarSubTitle.setSelected(true);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void mFieldsInitialize() {

        ViewCompat.setTransitionName(IntakeorderLinesActivity.textViewChosenOrder, IntakeorderLinesActivity.VIEW_CHOSEN_ORDER);
        IntakeorderLinesActivity.textViewChosenOrder.setText(cIntakeorder.currentIntakeOrder.getOrderNumberStr());

        if (!IntakeorderLinesActivity.showDefectsBln) {
            cIntakeorderMATSummaryLine.getSummaryLinesAdapter().pFillData(cIntakeorderMATSummaryLine.allIntakeorderMATSummaryLinesObl);
        } else {
            cIntakeorderMATSummaryLine.getSummaryLinesAdapter().pShowDeviations();
        }

        IntakeorderLinesActivity.imageButtonCloseOrder.setVisibility(View.VISIBLE);

    }

    @Override
    public void mSetListeners() {
        this.mSetRecyclerOnScrollListener();
        this.mSetSearchListener();
        this.mSetSearchCloseListener();
        this.mSetShowCommentListener();
        this.mSetStartLineListener();
        this.mSetDeviationsListener();
        this.mSetSendOrderListener();
    }

    @Override
    public void mInitScreen() {
        IntakeorderLinesActivity.mShowComments();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem pvMenuItem) {

        if (pvMenuItem.getItemId() == android.R.id.home) {
            IntakeorderLinesActivity.pLeaveActivity();
            return true;
        }

        return super.onOptionsItemSelected(pvMenuItem);
    }

    @Override
    public void onBackPressed() {
        IntakeorderLinesActivity.pLeaveActivity();
    }

    //End Region iICSDefaultActivity defaults

    //Region Public Methods

    public static void pIntakelineSelected(cIntakeorderMATSummaryLine pvIntakeorderMATSummaryLine) {
        cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine = pvIntakeorderMATSummaryLine;
    }

    public static void pHandleScan(cBarcodeScan pvBarcodeScan,
                                   Boolean pvLineSelectedBln) {

        cUserInterface.pCheckAndCloseOpenDialogs();
        cResult hulpResult;

        //BIN button has been pressed, so we already have a current line
        if (pvLineSelectedBln) {

            //Clear current barcode
            cIntakeorderBarcode.currentIntakeOrderBarcode = null;

            hulpResult = cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.pSummaryLineBusyRst();
            if (!hulpResult.resultBln) {
                mStepFailed(hulpResult.messagesStr());
                cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine = null;
                return;
            }

           IntakeorderLinesActivity.mStartStoreActivity();
            return;
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
                IntakeorderLinesActivity.mFillRecycler(cIntakeorderMATSummaryLine.allIntakeorderMATSummaryLinesObl);
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
    }

    public static void pSetToolBarTitleWithCounters(String pvTextStr){

        IntakeorderLinesActivity.toolbarSubTitle.setText(pvTextStr);

        //Close open dialogs, so keyboard will also close
        cUserInterface.pHideKeyboard();

        //Click to make even more sure that keyboard gets hidden
        IntakeorderLinesActivity.toolbarSubTitle.performClick();

    }

    public static  void pStartLine(){
        IntakeorderLinesActivity.imageViewStart.performClick();
    }

    public static void pShowData(List<cIntakeorderMATSummaryLine> pvDataObl) {
        IntakeorderLinesActivity.mFillRecycler(pvDataObl);
    }

    public static void pPasswordSuccess() {
        cBarcodeScan.pRegisterBarcodeReceiver();
        IntakeorderLinesActivity.mShowCloseOrderDialog(cAppExtension.activity.getString(R.string.message_leave), cAppExtension.activity.getString(R.string.message_close_order));
    }

    public static void pPasswordCancelled() {
        cBarcodeScan.pRegisterBarcodeReceiver();
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
        if (cIntakeorderBarcode.currentIntakeOrderBarcode.linesObl().size() == 0) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.activity.getString(R.string.message_no_lines_for_this_barcode));
            return result;
        }

        //Set the scanned barcode, so we can raise quantity in next activity
        cIntakeorder.currentIntakeOrder.intakeorderBarcodeScanned = cIntakeorderBarcode.currentIntakeOrderBarcode;


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
        if (hulpResult.activityActionEnu == cWarehouseorder.ActivityActionEnu.Unknown) {
            cUserInterface.pDoExplodingScreen(hulpResult.messagesStr(), "", true, true);
            return false;
        }

        //Something went wrong, the order has been deleted, so show comments and refresh
        if (hulpResult.activityActionEnu == cWarehouseorder.ActivityActionEnu.Hold) {

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

        IntakeorderLinesActivity.mStartOrderSelectActivity();

    }

    private void mSetShowCommentListener() {
        IntakeorderLinesActivity.imageButtonComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mShowCommentsFragment(cIntakeorder.currentIntakeOrder.pCommentObl(), "");
            }
        });
    }

    private void mSetDeviationsListener() {
        IntakeorderLinesActivity.switchDeviations.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean show) {

                if (switchDeviations.isChecked()) {
                    IntakeorderLinesActivity.showDefectsBln = true;
                    cIntakeorderMATSummaryLine.getSummaryLinesAdapter().pShowDeviations();
                }
                else {
                    IntakeorderLinesActivity.showDefectsBln = false;
                    cIntakeorderMATSummaryLine.getSummaryLinesAdapter().pFillData(cIntakeorderMATSummaryLine.allIntakeorderMATSummaryLinesObl);
                }
            }
        });
}

    private void mSetStartLineListener() {
        IntakeorderLinesActivity.imageViewStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntakeorderLinesActivity.pHandleScan(null,true);
            }
        });
    }

    private void mSetSendOrderListener() {

        IntakeorderLinesActivity.imageButtonCloseOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //do we need an administrator for this?
                if (!cSetting.RECEIVE_STORE_DEVIATIONS_PASSWORD().isEmpty() && cIntakeorderMATSummaryLine.totalItemsDifference() > 0 ) {
                    cUserInterface.pShowpasswordDialog(getString(R.string.supervisor_password_header), getString(R.string.supervisor_deviations_password_text), false);
                    return;
                }

                IntakeorderLinesActivity.mShowCloseOrderDialog(cAppExtension.activity.getString(R.string.message_leave), cAppExtension.activity.getString(R.string.message_close_order));
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

    private static void mStepFailed(String pvErrorMessageStr) {
        cUserInterface.pDoExplodingScreen(pvErrorMessageStr, cIntakeorder.currentIntakeOrder.getOrderNumberStr(), true, true);
        cIntakeorder.currentIntakeOrder.pLockReleaseViaWebserviceBln();
        cUserInterface.pCheckAndCloseOpenDialogs();
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

    private static void mShowCloseOrderDialog(String pvRejectStr,String pvAcceptStr) {

        cUserInterface.pCheckAndCloseOpenDialogs();
        String messageStr = "";
        if (cIntakeorderMATSummaryLine.totalItemsDifference() == 0 ) {
            messageStr = (cAppExtension.activity.getString(R.string.message_exactly_what_you_needed));
        }

        if (cIntakeorderMATSummaryLine.totalItems() > cIntakeorderMATSummaryLine.totalItemsHandled()) {
            messageStr =   cText.pDoubleToStringStr(cIntakeorderMATSummaryLine.totalItemsDifference()) + " LESS items";
        }

        if (cIntakeorderMATSummaryLine.totalItems() < cIntakeorderMATSummaryLine.totalItemsHandled()) {
            messageStr =   cText.pDoubleToStringStr(cIntakeorderMATSummaryLine.totalItemsDifference()) + " EXTRA items";
        }

        final AcceptRejectFragment acceptRejectFragment = new AcceptRejectFragment(cAppExtension.activity.getString(R.string.message_close_order),
                cAppExtension.activity.getString(R.string.message_close_storeorder_text,
                        cText.pDoubleToStringStr(cIntakeorderMATSummaryLine.totalItemsHandled()),
                        cText.pDoubleToStringStr(cIntakeorderMATSummaryLine.totalItems()), messageStr),
                pvRejectStr,
                pvAcceptStr ,
                false);

        acceptRejectFragment.setCancelable(true);
        cAppExtension.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // show my popup
                acceptRejectFragment.show(cAppExtension.fragmentManager, ACCEPTREJECTFRAGMENT_TAG);
            }
        });

    }


    private static void mStartStoreActivity(){


        //we have a LINE to handle, so start Store activity
        Intent intent = new Intent(cAppExtension.context, IntakeOrderIntakeActivity.class);
        cAppExtension.activity.startActivity(intent);

    }

    private static void mFillRecycler(List<cIntakeorderMATSummaryLine> pvDataObl) {

        if (pvDataObl.size() == 0) {
            IntakeorderLinesActivity.imageViewStart.setVisibility(View.INVISIBLE);
            return;
        }

        IntakeorderLinesActivity.imageViewStart.setVisibility(View.VISIBLE);

        //Show the recycler view
        cIntakeorderMATSummaryLine.getSummaryLinesAdapter().pFillData(pvDataObl);
        IntakeorderLinesActivity.recyclerViewLines.setHasFixedSize(false);
        IntakeorderLinesActivity.recyclerViewLines.setAdapter( cIntakeorderMATSummaryLine.getSummaryLinesAdapter());
        IntakeorderLinesActivity.recyclerViewLines.setLayoutManager(new LinearLayoutManager(cAppExtension.context));
        IntakeorderLinesActivity.recyclerViewLines.setVisibility(View.VISIBLE);
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
        IntakeorderLinesActivity.recyclerSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String pvString) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String pvQueryTextStr) {

                // If this object is filled, we set the filter with a scan
                if (cIntakeorderBarcode.currentIntakeOrderBarcode != null){
                    cIntakeorderMATSummaryLine.getSummaryLinesAdapter().pSetFilter(pvQueryTextStr, true);
                }

                //Someone typed the filter
                else {
                    cIntakeorderMATSummaryLine.getSummaryLinesAdapter().pSetFilter(pvQueryTextStr, false);
                }
                return true;
            }

        });

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



