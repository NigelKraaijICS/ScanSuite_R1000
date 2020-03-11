package nl.icsvertex.scansuite.Activities.Receive;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ICS.Interfaces.iICSDefaultActivity;
import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cProductFlavor;
import ICS.Utils.cResult;
import ICS.Utils.cText;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Basics.Article.cArticle;
import SSU_WHS.Basics.BarcodeLayouts.cBarcodeLayout;
import SSU_WHS.Basics.Settings.cSetting;
import SSU_WHS.General.Comments.cComment;
import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.General.cPublicDefinitions;
import SSU_WHS.Intake.IntakeorderBarcodes.cIntakeorderBarcode;
import SSU_WHS.Intake.Intakeorders.cIntakeorder;
import SSU_WHS.Receive.ReceiveLines.cReceiveorderLine;
import SSU_WHS.Receive.ReceiveLines.cReceiveorderLineRecyclerItemTouchHelper;
import SSU_WHS.Receive.ReceiveSummaryLine.cReceiveorderSummaryLine;
import SSU_WHS.Receive.ReceiveSummaryLine.cReceiveorderSummaryLineRecyclerItemTouchHelper;
import SSU_WHS.Receive.ReceiveSummaryLine.cReceiverorderSummaryLineAdapter;
import nl.icsvertex.scansuite.Activities.IntakeAndReceive.IntakeAndReceiveSelectActivity;
import nl.icsvertex.scansuite.BuildConfig;
import nl.icsvertex.scansuite.Fragments.Dialogs.AcceptRejectFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.AddArticleFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.CommentFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.NothingHereFragment;
import nl.icsvertex.scansuite.R;

public class ReceiveLinesActivity extends AppCompatActivity implements iICSDefaultActivity, cReceiveorderSummaryLineRecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    //Region Public Properties
    public static String VIEW_CHOSEN_ORDER = "detail:header:text";
    static final String ACCEPTREJECTFRAGMENT_TAG = "ACCEPTREJECTFRAGMENT_TAG";
    public static boolean busyBln =false;
    public static boolean closeOrderClickedBln = false;
    public  static cBarcodeScan barcodeScanToHandle;
    //End Region Public Properties

    //Region Private Properties

    //Region Views

    private static TextView textViewChosenOrder;
    private static ImageView imageButtonComments;

    private static ImageView toolbarImage;
    private static TextView toolbarTitle;
    private static TextView toolbarSubTitle;

    private static ImageView imageViewStart;
    private static RecyclerView recyclerViewLines;
    private static List<cReceiveorderSummaryLine> linesToShowObl;

    private static Switch switchDeviations;
    public static Boolean showDefectsBln = false;

    private static ImageView imageAddArticle;
    private static ImageView imageButtonCloseOrder;

    public static int positionSwiped;



    //End Region Views

    //End Region Private Properties

    //Region Default Methods

    @Override
    protected void onCreate(Bundle pvSavedInstanceState) {
        super.onCreate(pvSavedInstanceState);
        setContentView(R.layout.activity_receiveorderlines);
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

    @Override
    public void onSwiped(RecyclerView.ViewHolder pvViewHolder, int pvDirectionInt, int pvPositionInt) {

        if (!(pvViewHolder instanceof  cReceiverorderSummaryLineAdapter.ReceiverorderLineViewHolder)) {
            return;
        }

        ReceiveLinesActivity.positionSwiped = pvPositionInt;

        //todo: look at correct objectlist
        cReceiveorderSummaryLine.currentReceiveorderSummaryLine = ReceiveLinesActivity.linesToShowObl.get(pvPositionInt);

        //do we need an adult for this?
        if (!cSetting.RECEIVE_RESET_PASSWORD().isEmpty()) {
            cUserInterface.pShowpasswordDialog(getString(R.string.supervisor_password_header), getString(R.string.supervisor_password_text), false);
            return;
        }

        //Remove the enviroment
        ReceiveLinesActivity.mRemoveAdapterFromFragment();

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

        ReceiveLinesActivity.pShowData(cReceiveorderSummaryLine.allReceiveorderSummaryLinesObl);

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

        ReceiveLinesActivity.toolbarImage = findViewById(R.id.toolbarImage);
        ReceiveLinesActivity.toolbarTitle = findViewById(R.id.toolbarTitle);
        ReceiveLinesActivity.toolbarSubTitle = findViewById(R.id.toolbarSubtext);
        ReceiveLinesActivity.textViewChosenOrder = findViewById(R.id.textViewChosenOrder);
        ReceiveLinesActivity.imageButtonComments = findViewById(R.id.imageButtonComments);

        ReceiveLinesActivity.recyclerViewLines = findViewById(R.id.recyclerViewLines);

        ReceiveLinesActivity.imageViewStart = findViewById(R.id.imageViewStart);

        ReceiveLinesActivity.switchDeviations = findViewById(R.id.switchDeviations);

        ReceiveLinesActivity.imageButtonCloseOrder = findViewById(R.id.imageButtonCloseOrder);

        ReceiveLinesActivity.imageAddArticle = findViewById(R.id.imageAddArticle);

    }

    @Override
    public void mSetToolbar(String pvScreenTitleStr) {
        ReceiveLinesActivity.toolbarImage.setImageResource(R.drawable.ic_menu_intake_eo);
        ReceiveLinesActivity.toolbarTitle.setText(pvScreenTitleStr);
        ReceiveLinesActivity.toolbarTitle.setSelected(true);
        ReceiveLinesActivity.toolbarSubTitle.setSelected(true);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void mFieldsInitialize() {

        ViewCompat.setTransitionName(ReceiveLinesActivity.textViewChosenOrder, ReceiveLinesActivity.VIEW_CHOSEN_ORDER);

        if (BuildConfig.FLAVOR.equalsIgnoreCase(cProductFlavor.FlavorEnu.BMN.toString())) {
            ReceiveLinesActivity.textViewChosenOrder.setText(cIntakeorder.currentIntakeOrder.getDocumentStr());
        }
        else{
            ReceiveLinesActivity.textViewChosenOrder.setText(cIntakeorder.currentIntakeOrder.getOrderNumberStr());
        }

        if (cReceiveorderSummaryLine.allReceiveorderSummaryLinesObl.size() == 0 ) {
            mNoLinesAvailable(true);
            ReceiveLinesActivity.imageButtonCloseOrder.setVisibility(View.INVISIBLE);
            ReceiveLinesActivity.switchDeviations.setVisibility(View.INVISIBLE);
            return;
        }

        mNoLinesAvailable(false);

        if (!ReceiveLinesActivity.showDefectsBln) {
            cReceiveorderSummaryLine.getSummaryLinesAdapter().pFillData(cReceiveorderSummaryLine.allReceiveorderSummaryLinesObl);
        } else {
            cReceiveorderSummaryLine.getSummaryLinesAdapter().pShowDeviations();
        }

        if (cIntakeorder.currentIntakeOrder.getReceiveAmountManualEOBln()) {
            ReceiveLinesActivity.imageAddArticle.setVisibility(View.VISIBLE);
        }
        else
        {
            ReceiveLinesActivity.imageAddArticle.setVisibility(View.GONE);
        }

        ReceiveLinesActivity.imageButtonCloseOrder.setVisibility(View.VISIBLE);

    }

    @Override
    public void mSetListeners() {
        this.mSetShowCommentListener();
        this.mSetStartLineListener();
        this.mSetDeviationsListener();
        this.mSetAddArticleListener();
        this.mSetSendOrderListener();
        this.mSetRecyclerTouchListener();
    }

    @Override
    public void mInitScreen() {
        ReceiveLinesActivity.mShowComments();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem pvMenuItem) {

        if (pvMenuItem.getItemId() == android.R.id.home) {
            ReceiveLinesActivity.pLeaveActivity();
            return true;
        }

        return super.onOptionsItemSelected(pvMenuItem);
    }

    @Override
    public void onBackPressed() {
        ReceiveLinesActivity.pLeaveActivity();
    }

    //End Region iICSDefaultActivity defaults

    //Region Public Methods

    public static void pReceivelineSelected(cReceiveorderSummaryLine pvReceiveorderSummaryLine) {
        cReceiveorderSummaryLine.currentReceiveorderSummaryLine = pvReceiveorderSummaryLine;
    }

    public static void pHandleScan(final cBarcodeScan pvBarcodeScan, final boolean pvLineSelectedBln) {

        if (ReceiveLinesActivity.busyBln) {
            return;
        }

        //Close open Dialogs
        cUserInterface.pCheckAndCloseOpenDialogs();

        // Show that we are getting data
        cUserInterface.pShowGettingData();

        new Thread(new Runnable() {
            public void run() {
                mHandleScan(pvBarcodeScan, pvLineSelectedBln);
            }
        }).start();



    }

    public static void pDone() {

        ReceiveLinesActivity.closeOrderClickedBln = false;

        // Show that we are getting data
        cUserInterface.pShowGettingData();

        new Thread(new Runnable() {
            public void run() {
                mHandleClose();
            }
        }).start();

    }

    public static void pLeaveActivity() {

        ReceiveLinesActivity.closeOrderClickedBln = false;

        cResult hulpRst = new cResult();
        hulpRst.resultBln = true;

        if (cIntakeorder.currentIntakeOrder.isGenerated()) {

            if (cIntakeorder.currentIntakeOrder.summaryReceiveLinesObl() == null || cIntakeorder.currentIntakeOrder.summaryReceiveLinesObl().size() == 0) {
                hulpRst =   cIntakeorder.currentIntakeOrder.pInvalidateViaWebserviceRst();
                if (!hulpRst.resultBln) {
                    ReceiveLinesActivity.mStepFailed(hulpRst.messagesStr(),"");
                    return;
                }

                cUserInterface.pShowSnackbarMessage(recyclerViewLines,cAppExtension.activity.getString(R.string.message_order_invalidated),null,true);

                Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    public void run() {
                        mStartOrderSelectActivity();
                        // Actions to do after 2.5 seconds
                    }
                }, 2500);



            }
        }

        cIntakeorder.currentIntakeOrder.pLockReleaseViaWebserviceBln();
        mStartOrderSelectActivity();
    }

    public static void pSetToolBarTitleWithCounters(final String pvTextStr){

        cAppExtension.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ReceiveLinesActivity.toolbarSubTitle.setText(pvTextStr);

                //Close open dialogs, so keyboard will also close
                cUserInterface.pHideKeyboard();

                //Click to make even more sure that keyboard gets hidden
                ReceiveLinesActivity.toolbarSubTitle.performClick();
            }
        });




    }

    public static  void pStartLine(){

        cAppExtension.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ReceiveLinesActivity.imageViewStart.performClick();
            }});


    }

    public static void pShowData(List<cReceiveorderSummaryLine> pvDataObl) {
        ReceiveLinesActivity.linesToShowObl = pvDataObl;
        ReceiveLinesActivity.mFillRecycler();
    }

    public static void pPasswordSuccess() {
        cBarcodeScan.pRegisterBarcodeReceiver();
        ReceiveLinesActivity.mRemoveAdapterFromFragment();
    }

    public static void pPasswordCancelled() {
        cBarcodeScan.pRegisterBarcodeReceiver();
    }

    public static void pAddUnknownScan(cBarcodeScan pvBarcodeScan){

        cUserInterface.pShowGettingData();

        //Clear the scan that we kept
        ReceiveLinesActivity.barcodeScanToHandle = null;

        //We can add a line, but we don't check with the ERP, so add line and open it
        if (! cSetting.RECEIVE_BARCODE_CHECK()) {
            ReceiveLinesActivity.mAddUnkownArticle(pvBarcodeScan);
            cUserInterface.pHideGettingData();
            return;
        }

        //We can add a line, and we need to check with the ERP, so check, add and open it
        ReceiveLinesActivity.mAddERPArticle(pvBarcodeScan);
        cUserInterface.pHideGettingData();
    }

    //End Region Public Methods

    //Region Private Methods

    private static cResult mHandleArticleScan(cBarcodeScan pvBarcodeScan) {

        cResult result = new cResult();
        result.resultBln = true;

        //Check if this is a barcodeStr we already know
        cIntakeorderBarcode.currentIntakeOrderBarcode = cIntakeorder.currentIntakeOrder.pGetOrderBarcode(pvBarcodeScan);
        if (cIntakeorderBarcode.currentIntakeOrderBarcode == null) {
            ReceiveLinesActivity.mHandleUnknownBarcodeScan(pvBarcodeScan);
            ReceiveLinesActivity.busyBln = false;
            result.resultBln = true;
            return  result;
        }

        //Set the scanned barcodeStr, so we can raise quantity in next activity
        cIntakeorder.currentIntakeOrder.intakeorderBarcodeScanned = cIntakeorderBarcode.currentIntakeOrderBarcode;

        cReceiveorderSummaryLine.currentReceiveorderSummaryLine = cReceiveorderSummaryLine.pGetSummaryLineWithItemNoAndVariantCode(cIntakeorder.currentIntakeOrder.intakeorderBarcodeScanned.getItemNoStr(),cIntakeorder.currentIntakeOrder.intakeorderBarcodeScanned.getVariantCodeStr());
        result = cReceiveorderSummaryLine.currentReceiveorderSummaryLine.pSummaryLineBusyRst();
        if (!result.resultBln) {
            mStepFailed(result.messagesStr(),pvBarcodeScan.getBarcodeOriginalStr());
            cReceiveorderSummaryLine.currentReceiveorderSummaryLine = null;
            ReceiveLinesActivity.busyBln = false;
            return result;
        }

        ReceiveLinesActivity.mStartReceiveActivity();

        //Article is known and also not handled, so everything is fine
        ReceiveLinesActivity.busyBln = false;
        return result;
    }

    private  static void mHandleScan(cBarcodeScan pvBarcodeScan, boolean pvLineSelectedBln){

        // Show that we are getting data and set busy boolean
        ReceiveLinesActivity.busyBln = true;

        cResult hulpResult;

        //BIN button has been pressed, so we already have a current line
        if (pvLineSelectedBln) {

            //Clear current barcodeStr
            cIntakeorderBarcode.currentIntakeOrderBarcode = null;

            hulpResult = cReceiveorderSummaryLine.currentReceiveorderSummaryLine.pSummaryLineBusyRst();
            if (!hulpResult.resultBln) {
                mStepFailed(hulpResult.messagesStr(),pvBarcodeScan.getBarcodeOriginalStr());
                cReceiveorderSummaryLine.currentReceiveorderSummaryLine = null;
                ReceiveLinesActivity.busyBln = false;
                return;
            }

            ReceiveLinesActivity.mStartReceiveActivity();
            return;
        }


        //Check if we have scanned an ARTICLE and check if there are not handled linesInt for this ARTICLE
        if (cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(), cBarcodeLayout.barcodeLayoutEnu.ARTICLE)) {

            //Handle the ARTICLE scan
            hulpResult = ReceiveLinesActivity.mHandleArticleScan(pvBarcodeScan);

            //Something went wrong, so show message and stop
            if (!hulpResult.resultBln) {
                mDoUnknownScan(hulpResult.messagesStr(), pvBarcodeScan.getBarcodeOriginalStr());
                ReceiveLinesActivity.linesToShowObl = cReceiveorderSummaryLine.allReceiveorderSummaryLinesObl;
                ReceiveLinesActivity.mFillRecycler();
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

    private static boolean mTryToCloseOrderBln() {

        cResult hulpResult;
        hulpResult = new cResult();
        hulpResult.resultBln = false;

        hulpResult = cIntakeorder.currentIntakeOrder.pReceiveHandledViaWebserviceRst();

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
                ReceiveLinesActivity.mShowCommentsFragment(cIntakeorder.currentIntakeOrder.pFeedbackCommentObl(), hulpResult.messagesStr());
            }

            return false;
        }

        return true;

    }

    private static void mHandleClose() {

        if (!ReceiveLinesActivity.mTryToCloseOrderBln()) {
            return;
        }

        ReceiveLinesActivity.mStartOrderSelectActivity();

    }

    private void mSetShowCommentListener() {
        ReceiveLinesActivity.imageButtonComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mShowCommentsFragment(cIntakeorder.currentIntakeOrder.pCommentObl(), "");
            }
        });
    }

    private void mSetDeviationsListener() {
        ReceiveLinesActivity.switchDeviations.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean show) {

                if (switchDeviations.isChecked()) {
                    ReceiveLinesActivity.showDefectsBln = true;
                    cReceiveorderSummaryLine.getSummaryLinesAdapter().pShowDeviations();
                }
                else {
                    ReceiveLinesActivity.showDefectsBln = false;
                    cReceiveorderSummaryLine.getSummaryLinesAdapter().pFillData(cReceiveorderSummaryLine.allReceiveorderSummaryLinesObl);
                }
            }
        });
    }

    private void mSetStartLineListener() {
        ReceiveLinesActivity.imageViewStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReceiveLinesActivity.pHandleScan(null,true);
            }
        });
    }

    private void mSetAddArticleListener() {
        ReceiveLinesActivity.imageAddArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mShowAddArticleFragment();
            }
        });
    }

    private void mSetSendOrderListener() {

        ReceiveLinesActivity.imageButtonCloseOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (cIntakeorder.currentIntakeOrder.isGenerated()) {
                    ReceiveLinesActivity.mShowCloseOrderDialog(cAppExtension.activity.getString(R.string.message_leave), cAppExtension.activity.getString(R.string.message_close));
                    return;
                }

                //do we need an administrator for this?
                if (!cSetting.RECEIVE_DEVIATIONS_PASSWORD().isEmpty() && cReceiveorderSummaryLine.totalItemsDifference() > 0 ) {
                    ReceiveLinesActivity.closeOrderClickedBln = true;
                    cUserInterface.pShowpasswordDialog(getString(R.string.supervisor_password_header), getString(R.string.supervisor_deviations_password_text), false);
                    return;
                }

                ReceiveLinesActivity.mShowCloseOrderDialog(cAppExtension.activity.getString(R.string.message_leave), cAppExtension.activity.getString(R.string.message_close));
            }
        });
    }

    private void mSetRecyclerTouchListener(){


        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new cReceiveorderSummaryLineRecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(ReceiveLinesActivity.recyclerViewLines);
    }

    private static void mDoUnknownScan(String pvErrorMessageStr, String pvScannedBarcodeStr) {
        cUserInterface.pDoExplodingScreen(pvErrorMessageStr, pvScannedBarcodeStr, true, true);
        ReceiveLinesActivity.busyBln = true;
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

    private static void mStepFailed(String pvErrorMessageStr, String pvScannedBarcodeStr) {
        cUserInterface.pDoExplodingScreen(pvErrorMessageStr, pvScannedBarcodeStr, true, true);
    }

    private static void mStartOrderSelectActivity() {

        cAppExtension.activity.runOnUiThread(new Runnable() {
            public void run() {
                Intent intent = new Intent(cAppExtension.context, IntakeAndReceiveSelectActivity.class);
                IntakeAndReceiveSelectActivity.startedViaMenuBln = false;
                cAppExtension.activity.startActivity(intent);
            }
        });

    }

    private static void mShowComments() {

        if (cIntakeorder.currentIntakeOrder.pCommentObl() == null || cIntakeorder.currentIntakeOrder.pCommentObl().size() == 0) {
            ReceiveLinesActivity.imageButtonComments.setVisibility(View.INVISIBLE);
            return;
        }

        ReceiveLinesActivity.imageButtonComments.setVisibility(View.VISIBLE);

        //We already showed the comments
        if (cComment.commentsShownBln) {
            return;
        }

        ReceiveLinesActivity.mShowCommentsFragment(cIntakeorder.currentIntakeOrder.pCommentObl(), "");
        cComment.commentsShownBln = true;
    }

    private static void mShowCloseOrderDialog(String pvRejectStr,String pvAcceptStr) {

        cUserInterface.pCheckAndCloseOpenDialogs();
        String messageStr = "";

        ReceiveLinesActivity.closeOrderClickedBln = true;


        if (!cIntakeorder.currentIntakeOrder.isGenerated()) {
            if (cReceiveorderSummaryLine.totalItemsDifference() == 0 ) {
                messageStr = (cAppExtension.activity.getString(R.string.message_exactly_what_you_needed));
            }

            if (cReceiveorderSummaryLine.totalItems() > cReceiveorderSummaryLine.totalItemsHandled()) {
                messageStr =   cText.pDoubleToStringStr(cReceiveorderSummaryLine.totalItemsDifference()) +   " " + cAppExtension.activity.getString(R.string.message_less_items);
            }

            if (cReceiveorderSummaryLine.totalItems() < cReceiveorderSummaryLine.totalItemsHandled()) {
                messageStr =   cText.pDoubleToStringStr(cReceiveorderSummaryLine.totalItemsDifference()) +   " " + cAppExtension.activity.getString(R.string.message_extra_items);
            }

            final AcceptRejectFragment acceptRejectFragment = new AcceptRejectFragment(cAppExtension.activity.getString(R.string.message_close_order),
                    cAppExtension.activity.getString(R.string.message_close_storeorder_text,
                            cText.pDoubleToStringStr(cReceiveorderSummaryLine.totalItemsHandled()),
                            cText.pDoubleToStringStr(cReceiveorderSummaryLine.totalItems()), messageStr),
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

        if (cIntakeorder.currentIntakeOrder.isGenerated()) {


            final AcceptRejectFragment acceptRejectFragment = new AcceptRejectFragment(cAppExtension.activity.getString(R.string.message_close_order),
                    cAppExtension.activity.getString(R.string.message_close_receiveorder_generated_text,
                            cText.pDoubleToStringStr(cReceiveorderSummaryLine.totalItemsHandled())),
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


    }

    private static void mShowAddItemDialog(cBarcodeScan pvBarcodeScan, String pvRejectStr,String pvAcceptStr) {

        cUserInterface.pCheckAndCloseOpenDialogs();

        String pvItemStr = pvBarcodeScan.getBarcodeOriginalStr();

        if (cSetting.RECEIVE_BARCODE_CHECK()) {

            cUserInterface.pShowGettingData();

            cArticle articleScanned =  cArticle.pGetArticleByBarcodeViaWebservice(pvBarcodeScan);
            if (articleScanned != null) {
                pvItemStr = articleScanned.getItemNoStr() + " " + articleScanned.getVariantCodeStr() + " "  + articleScanned.getDescriptionStr();
            }

            cUserInterface.pHideGettingData();

        }


        ReceiveLinesActivity.barcodeScanToHandle = pvBarcodeScan;

            final AcceptRejectFragment acceptRejectFragment = new AcceptRejectFragment(cAppExtension.activity.getString(R.string.message_add_item),
                    cAppExtension.activity.getString(R.string.message_add_item_are_you_sure, pvItemStr) ,
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

    private static void mStartReceiveActivity(){

        ReceiveLinesActivity.busyBln = false;

        //we have a LINE to handle, so start Receive activity
        Intent intent = new Intent(cAppExtension.context, ReceiveOrderReceiveActivity.class);
        cAppExtension.activity.startActivity(intent);

    }

    private static void mFillRecycler() {

        if (ReceiveLinesActivity.linesToShowObl .size() == 0) {
            ReceiveLinesActivity.imageViewStart.setVisibility(View.INVISIBLE);
            mNoLinesAvailable(true);
            return;
        }

        mNoLinesAvailable(false);

        ReceiveLinesActivity.imageViewStart.setVisibility(View.VISIBLE);

        //Show the recycler view
        cReceiveorderSummaryLine.getSummaryLinesAdapter().pFillData(ReceiveLinesActivity.linesToShowObl );
        ReceiveLinesActivity.recyclerViewLines.setHasFixedSize(false);
        ReceiveLinesActivity.recyclerViewLines.setAdapter( cReceiveorderSummaryLine.getSummaryLinesAdapter());
        ReceiveLinesActivity.recyclerViewLines.setLayoutManager(new LinearLayoutManager(cAppExtension.context));
        ReceiveLinesActivity.recyclerViewLines.setVisibility(View.VISIBLE);
    }

    private static void mNoLinesAvailable(final boolean pvShowBln) {


        cAppExtension.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                cUserInterface.pHideGettingData();

                if (pvShowBln) {

                    ReceiveLinesActivity.recyclerViewLines.setVisibility(View.INVISIBLE);

                    FragmentTransaction fragmentTransaction = cAppExtension.fragmentManager.beginTransaction();
                    NothingHereFragment fragment = new NothingHereFragment();
                    fragmentTransaction.replace(R.id.container, fragment);
                    fragmentTransaction.commit();
                    return;
                }

                ReceiveLinesActivity.recyclerViewLines.setVisibility(View.VISIBLE);


                List<Fragment> fragments = cAppExtension.fragmentManager.getFragments();
                for (Fragment fragment : fragments) {
                    if (fragment instanceof NothingHereFragment) {
                        FragmentTransaction fragmentTransaction = cAppExtension.fragmentManager.beginTransaction();
                        fragmentTransaction.remove(fragment);
                        fragmentTransaction.commit();
                    }
                }
            }
        });

    }

    private static void mHandleUnknownBarcodeScan(cBarcodeScan pvBarcodeScan) {

        // Check if we can add a line
        if (!cIntakeorder.currentIntakeOrder.isGenerated() && cIntakeorder.currentIntakeOrder.getReceiveNoExtraItemsBln()) {
            ReceiveLinesActivity.mStepFailed(cAppExtension.activity.getString(R.string.message_add_article_now_allowed),pvBarcodeScan.getBarcodeOriginalStr());
            return;
        }

        // Should we notify the user before adding extra item
        if (cSetting.RECEIVE_INTAKE_EO_CREATE_EXTRA_ITEM_VALIDATION().equalsIgnoreCase("REQUIRED")) {
            ReceiveLinesActivity.mShowAddItemDialog(pvBarcodeScan,cAppExtension.activity.getString(R.string.message_cancel), cAppExtension.activity.getString(R.string.message_add_item));
            return;
        }

        ReceiveLinesActivity.pAddUnknownScan(pvBarcodeScan);


    }

    private static void mAddUnkownArticle(cBarcodeScan pvBarcodeScan){

        cResult hulpResult = new cResult();
        hulpResult.resultBln = false;

        //Add the barcodeStr via the webservice
        if (!cIntakeorder.currentIntakeOrder.pAddUnkownBarcodeAndItemVariantBln(pvBarcodeScan)) {
            cUserInterface.pDoExplodingScreen(cAppExtension.activity.getString(R.string.message_adding_unkown_article_failed),"",true,true);
            ReceiveLinesActivity.busyBln = true;
            return;
        }

        hulpResult = cReceiveorderSummaryLine.currentReceiveorderSummaryLine.pSummaryLineBusyRst();
        if (!hulpResult.resultBln) {
            mStepFailed(hulpResult.messagesStr(),pvBarcodeScan.getBarcodeOriginalStr());
            cReceiveorderSummaryLine.currentReceiveorderSummaryLine = null;
            ReceiveLinesActivity.busyBln = true;
            return;
        }

        //Open the line, so we can edit it
        ReceiveLinesActivity.mStartReceiveActivity();


    }

    private static void mAddERPArticle(cBarcodeScan pvBarcodeScan){

        //Add the barcodeStr via the webservice
        if (!cIntakeorder.currentIntakeOrder.pAddERPBarcodeBln(pvBarcodeScan)) {
            cUserInterface.pDoExplodingScreen(cAppExtension.activity.getString(R.string.message_adding_erp_article_failed, pvBarcodeScan.barcodeOriginalStr),"",true,true);
            ReceiveLinesActivity.busyBln = false;
            return;
        }

        cResult hulpResult = cReceiveorderSummaryLine.currentReceiveorderSummaryLine.pSummaryLineBusyRst();
        if (!hulpResult.resultBln) {
            mStepFailed(hulpResult.messagesStr(),pvBarcodeScan.getBarcodeOriginalStr());
            cReceiveorderSummaryLine.currentReceiveorderSummaryLine = null;
            return;
        }

        //Open the line, so we can edit it
        ReceiveLinesActivity.mStartReceiveActivity();

    }

    private void mShowAddArticleFragment(){
        AddArticleFragment addArticleFragment = new AddArticleFragment();
        addArticleFragment.setCancelable(true);
        addArticleFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.ADDARTICLE_TAG);

    }

    private static void mRemoveAdapterFromFragment(){

        if (cReceiveorderSummaryLine.currentReceiveorderSummaryLine.getQuantityHandledDbl() == 0) {
            cAppExtension.activity.getString(R.string.message_zero_lines_cant_be_reset);
            return;
        }

        //remove the item from recyclerview
        cResult hulpRst = cReceiveorderSummaryLine.currentReceiveorderSummaryLine.pResetRst();
        if (! hulpRst.resultBln) {
            cUserInterface.pDoExplodingScreen(cAppExtension.activity.getString(R.string.message_reset_variant_via_webservice_failed),"",true,true);
            return;
        }

        //Renew data, so only current lines are shown
        ReceiveLinesActivity.linesToShowObl = cReceiveorderSummaryLine.allReceiveorderSummaryLinesObl;
        ReceiveLinesActivity.mFillRecycler();

    }

    //End Region Private Methods

}



