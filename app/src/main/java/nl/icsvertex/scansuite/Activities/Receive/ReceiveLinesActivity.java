package nl.icsvertex.scansuite.Activities.Receive;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Objects;

import ICS.Interfaces.iICSDefaultActivity;
import ICS.Utils.Scanning.cBarcodeScan;
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
import SSU_WHS.Receive.ReceiveSummaryLine.cReceiveorderSummaryLine;
import SSU_WHS.Receive.ReceiveSummaryLine.cReceiveorderSummaryLineRecyclerItemTouchHelper;
import SSU_WHS.Receive.ReceiveSummaryLine.cReceiverorderSummaryLineAdapter;
import nl.icsvertex.scansuite.Activities.IntakeAndReceive.IntakeAndReceiveSelectActivity;
import nl.icsvertex.scansuite.Activities.Packaging.PackagingActivity;
import nl.icsvertex.scansuite.BuildConfig;
import nl.icsvertex.scansuite.Fragments.Dialogs.AcceptRejectFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.AddArticleFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.CommentFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.NothingHereFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.SendingFragment;
import nl.icsvertex.scansuite.R;

public class ReceiveLinesActivity extends AppCompatActivity implements iICSDefaultActivity, cReceiveorderSummaryLineRecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    //Region Public Properties

    public static boolean busyBln = false;
    public static boolean closeOrderClickedBln = false;
    public static boolean packagingClickedBln = false;
    public static boolean packagingHandledBln = false;
    public static cBarcodeScan barcodeScanToHandle;
    //End Region Public Properties

    //Region Private Properties

    //Region Views

    private TextView textViewChosenOrder;
    private ImageView imageButtonComments;

    private ImageView toolbarImage;
    private TextView toolbarTitle;
    private TextView toolbarSubTitle;

    private RecyclerView recyclerViewLines;
    private List<cReceiveorderSummaryLine> linesToShowObl;

    private Switch switchDeviations;
    public Boolean showDefectsBln = false;

    private ImageView imageAddArticle;
    private ImageView imageButtonCloseOrder;


    private cReceiverorderSummaryLineAdapter receiverorderSummaryLineAdapter;

    private cReceiverorderSummaryLineAdapter getReceiverorderSummaryLineAdapter() {
        if (this.receiverorderSummaryLineAdapter == null) {
            this.receiverorderSummaryLineAdapter = new cReceiverorderSummaryLineAdapter();
        }

        return this.receiverorderSummaryLineAdapter;
    }

    private cReceiverorderSummaryLineAdapter deviationsReceiverorderSummaryLineAdapter;

    private cReceiverorderSummaryLineAdapter getDeviationsReceiverorderSummaryLineAdapter() {
        if (this.deviationsReceiverorderSummaryLineAdapter == null) {
            this.deviationsReceiverorderSummaryLineAdapter = new cReceiverorderSummaryLineAdapter();
        }

        return this.deviationsReceiverorderSummaryLineAdapter;
    }

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
        cBarcodeScan.pRegisterBarcodeReceiver(this.getClass().getSimpleName());
        cUserInterface.pEnableScanner();
    }

    @Override
    protected void onPause() {
        try {
            cBarcodeScan.pUnregisterBarcodeReceiver(this.getClass().getSimpleName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder pvViewHolder, int pvDirectionInt, int pvPositionInt) {

        if (!(pvViewHolder instanceof cReceiverorderSummaryLineAdapter.ReceiverorderLineViewHolder)) {
            return;
        }

        //todo: look at correct objectlist
        cReceiveorderSummaryLine.currentReceiveorderSummaryLine = this.linesToShowObl.get(pvPositionInt);

        //do we need an adult for this?
        if (!cSetting.RECEIVE_RESET_PASSWORD().isEmpty()) {
            cUserInterface.pShowpasswordDialog(getString(R.string.supervisor_password_header), getString(R.string.supervisor_password_text), false);
            return;
        }

        //Remove the enviroment
        this.mRemoveAdapterFromFragment();

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

        this.pShowData(cReceiveorderSummaryLine.allReceiveorderSummaryLinesObl);

        cBarcodeScan.pRegisterBarcodeReceiver(this.getClass().getSimpleName());
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

        this.recyclerViewLines = findViewById(R.id.recyclerViewLines);

        this.switchDeviations = findViewById(R.id.switchDeviations);

        this.imageButtonCloseOrder = findViewById(R.id.imageButtonCloseOrder);

        this.imageAddArticle = findViewById(R.id.imageAddArticle);

    }

    @Override
    public void mSetToolbar(String pvScreenTitleStr) {
        this.toolbarImage.setImageResource(R.drawable.ic_menu_intake_eo);
        this.toolbarTitle.setText(pvScreenTitleStr);
        this.toolbarTitle.setSelected(true);
        this.toolbarSubTitle.setSelected(true);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void mFieldsInitialize() {

        ViewCompat.setTransitionName(this.textViewChosenOrder, cPublicDefinitions.VIEW_CHOSEN_ORDER);

        this.textViewChosenOrder.setText(cIntakeorder.currentIntakeOrder.getOrderNumberStr());
        if (cReceiveorderSummaryLine.allReceiveorderSummaryLinesObl.size() == 0) {
            mNoLinesAvailable(true);
            this.imageButtonCloseOrder.setVisibility(View.INVISIBLE);
            this.switchDeviations.setVisibility(View.INVISIBLE);
            return;
        }

        mNoLinesAvailable(false);


        if (!this.showDefectsBln) {
            this.getReceiverorderSummaryLineAdapter().pFillData(cReceiveorderSummaryLine.allReceiveorderSummaryLinesObl);
        } else {
            this.getDeviationsReceiverorderSummaryLineAdapter().pShowDeviations();
        }

        if (cIntakeorder.currentIntakeOrder.getReceiveAmountManualEOBln()) {
            this.imageAddArticle.setVisibility(View.VISIBLE);
        } else {
            this.imageAddArticle.setVisibility(View.GONE);
        }

        this.imageButtonCloseOrder.setVisibility(View.VISIBLE);


    }

    @Override
    public void mSetListeners() {
        this.mSetShowCommentListener();
        this.mSetDeviationsListener();
        this.mSetAddArticleListener();
        this.mCloseOrderListener();
        this.mSetRecyclerTouchListener();
    }

    @Override
    public void mInitScreen() {
        this.mShowComments();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem pvMenuItem) {

        if (pvMenuItem.getItemId() == android.R.id.home) {
            this.pLeaveActivity();
            return true;
        }

        return super.onOptionsItemSelected(pvMenuItem);
    }

    @Override
    public void onBackPressed() {
        this.pLeaveActivity();
    }

    //End Region iICSDefaultActivity defaults

    //Region Public Methods

    public void pReceivelineSelected(cReceiveorderSummaryLine pvReceiveorderSummaryLine) {
        cReceiveorderSummaryLine.currentReceiveorderSummaryLine = pvReceiveorderSummaryLine;
    }

    public void pHandleScan(final cBarcodeScan pvBarcodeScan, final boolean pvLineSelectedBln) {

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

    public void pDone() {

        ReceiveLinesActivity.closeOrderClickedBln = false;

        // Show that we are getting data
        cUserInterface.pShowGettingData();

        new Thread(new Runnable() {
            public void run() {
                mHandleClose();
            }
        }).start();

    }

    public void pPackaging() {

        ReceiveLinesActivity.packagingClickedBln = false;

        // Show that we are getting data
        cUserInterface.pShowGettingData();

        new Thread(new Runnable() {
            public void run() {
                mStartPackagingActivity();
            }
        }).start();

    }

    public void pLeaveActivity() {

        ReceiveLinesActivity.closeOrderClickedBln = false;

        cResult hulpRst = new cResult();
        hulpRst.resultBln = true;

        if (cIntakeorder.currentIntakeOrder.isGenerated()) {

            if (cIntakeorder.currentIntakeOrder.summaryReceiveLinesObl() == null || cIntakeorder.currentIntakeOrder.summaryReceiveLinesObl().size() == 0) {
                hulpRst = cIntakeorder.currentIntakeOrder.pInvalidateViaWebserviceRst();
                if (!hulpRst.resultBln) {
                    this.mStepFailed(hulpRst.messagesStr(), "");
                    return;
                }

                cUserInterface.pShowSnackbarMessage(recyclerViewLines, cAppExtension.activity.getString(R.string.message_order_invalidated), null, true);

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
        this.mStartOrderSelectActivity();
    }

    public void pSetToolBarTitleWithCounters(final String pvTextStr) {

        cAppExtension.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                toolbarSubTitle.setText(pvTextStr);

                //Close open dialogs, so keyboard will also close
                cUserInterface.pHideKeyboard();

                //Click to make even more sure that keyboard gets hidden
                toolbarSubTitle.performClick();
            }
        });


    }

    public void pShowData(List<cReceiveorderSummaryLine> pvDataObl) {
        this.linesToShowObl = pvDataObl;
        this.mFillRecycler();
    }

    public void pPasswordSuccess() {
        cBarcodeScan.pRegisterBarcodeReceiver(this.getClass().getSimpleName());
        this.mRemoveAdapterFromFragment();
    }

    public void pPasswordCancelled() {
        cBarcodeScan.pRegisterBarcodeReceiver(this.getClass().getSimpleName());
    }

    public void pAddUnknownScan(cBarcodeScan pvBarcodeScan) {

        cUserInterface.pShowGettingData();

        //Clear the scan that we kept
        ReceiveLinesActivity.barcodeScanToHandle = null;

        //We can add a line, but we don't check with the ERP, so add line and open it
        if (!cSetting.RECEIVE_BARCODE_CHECK()) {
            this.mAddUnkownArticle(pvBarcodeScan);
            cUserInterface.pHideGettingData();
            return;
        }

        //We can add a line, and we need to check with the ERP, so check, add and open it
        this.mAddERPArticle(pvBarcodeScan);
        cUserInterface.pHideGettingData();
    }

    //End Region Public Methods

    //Region Private Methods

    private cResult mHandleArticleScan(cBarcodeScan pvBarcodeScan) {

        cResult result = new cResult();
        result.resultBln = true;

        //Check if this is a barcodeStr we already know
        cIntakeorderBarcode.currentIntakeOrderBarcode = cIntakeorder.currentIntakeOrder.pGetOrderBarcode(pvBarcodeScan);
        if (cIntakeorderBarcode.currentIntakeOrderBarcode == null) {
            this.mHandleUnknownBarcodeScan(pvBarcodeScan);
            ReceiveLinesActivity.busyBln = false;
            result.resultBln = true;
            return result;
        }

        //Set the scanned barcodeStr, so we can raise quantity in next activity
        cIntakeorder.currentIntakeOrder.intakeorderBarcodeScanned = cIntakeorderBarcode.currentIntakeOrderBarcode;

        cReceiveorderSummaryLine.currentReceiveorderSummaryLine = cReceiveorderSummaryLine.pGetSummaryLineWithItemNoAndVariantCode(cIntakeorder.currentIntakeOrder.intakeorderBarcodeScanned.getItemNoStr(), cIntakeorder.currentIntakeOrder.intakeorderBarcodeScanned.getVariantCodeStr());
        result = Objects.requireNonNull(cReceiveorderSummaryLine.currentReceiveorderSummaryLine).pSummaryLineBusyRst();
        if (!result.resultBln) {
            mStepFailed(result.messagesStr(), pvBarcodeScan.getBarcodeOriginalStr());
            cReceiveorderSummaryLine.currentReceiveorderSummaryLine = null;
            ReceiveLinesActivity.busyBln = false;
            return result;
        }

        this.mStartReceiveActivity();

        //Article is known and also not handled, so everything is fine
        ReceiveLinesActivity.busyBln = false;
        return result;
    }

    private void mHandleScan(cBarcodeScan pvBarcodeScan, boolean pvLineSelectedBln) {

        // Show that we are getting data and set busy boolean
        ReceiveLinesActivity.busyBln = true;

        cResult hulpResult;

        //BIN button has been pressed, so we already have a current line
        if (pvLineSelectedBln) {

            //Clear current barcodeStr
            cIntakeorderBarcode.currentIntakeOrderBarcode = null;

            hulpResult = cReceiveorderSummaryLine.currentReceiveorderSummaryLine.pSummaryLineBusyRst();
            if (!hulpResult.resultBln) {
                mStepFailed(hulpResult.messagesStr(), pvBarcodeScan.getBarcodeOriginalStr());
                cReceiveorderSummaryLine.currentReceiveorderSummaryLine = null;
                ReceiveLinesActivity.busyBln = false;
                return;
            }

            this.mStartReceiveActivity();
            return;
        }


        //Check if we have scanned an ARTICLE and check if there are not handled linesInt for this ARTICLE
        if (cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(), cBarcodeLayout.barcodeLayoutEnu.ARTICLE)) {

            //Handle the ARTICLE scan
            hulpResult = this.mHandleArticleScan(pvBarcodeScan);

            //Something went wrong, so show message and stop
            if (!hulpResult.resultBln) {
                mDoUnknownScan(hulpResult.messagesStr(), pvBarcodeScan.getBarcodeOriginalStr());
                this.linesToShowObl = cReceiveorderSummaryLine.allReceiveorderSummaryLinesObl;
                this.mFillRecycler();
                return;
            }

            //Hide the fucking keyboard
            cUserInterface.pHideKeyboard();

            //Filter has been set in mHandleArticleScan so we are done
            return;

        }




        //unknown scan
        this.mDoUnknownScan(cAppExtension.context.getString(R.string.error_unknown_barcode), pvBarcodeScan.getBarcodeOriginalStr());

    }

    private void mTryToCloseOrder() {

        cResult hulpResult;
        hulpResult = new cResult();
        hulpResult.resultBln = false;

        hulpResult = cIntakeorder.currentIntakeOrder.pReceiveHandledViaWebserviceRst();

        //Everything was fine, so we are done
        if (hulpResult.resultBln) {
            this.mShowSend();
            this.mStartOrderSelectActivity();
            return;
        }

        //Something went wrong, but no further actions are needed, so ony show reason of failure
        if (hulpResult.activityActionEnu == cWarehouseorder.ActivityActionEnu.Unknown) {
            this.mShowSendFailed(hulpResult.messagesStr());
            return;
        }

        //Something went wrong, the order has been deleted, so show comments and refresh
        if (hulpResult.activityActionEnu == cWarehouseorder.ActivityActionEnu.Hold) {

            cUserInterface.pCheckAndCloseOpenDialogs();

            //If we got any comments, show them
            if (cIntakeorder.currentIntakeOrder.pFeedbackCommentObl() != null && cIntakeorder.currentIntakeOrder.pFeedbackCommentObl().size() > 0) {
                //Process comments from webresult
                this.mShowCommentsFragment(cIntakeorder.currentIntakeOrder.pFeedbackCommentObl(), hulpResult.messagesStr());
            }

            return;
        }

        this.mShowSend();
        this.mStartOrderSelectActivity();

    }

    private void mHandleClose() {

            if (!ReceiveLinesActivity.packagingHandledBln) {

                //Check if there is there could be packaging
                if (cIntakeorder.currentIntakeOrder.isReceiveIntakeEOPackagingIntakeBln() ||
                    cIntakeorder.currentIntakeOrder.isReceiveIntakeEOPackagingShippedBln() ) {
                    mShowPackagingDialog(cAppExtension.activity.getString(R.string.message_no), cAppExtension.activity.getString(R.string.message_yes));
                    return;
                }
            }
            else
            {
                ReceiveLinesActivity.packagingHandledBln = true;
            }


        mShowSending();
        new Thread(new Runnable() {
            public void run() {
                mTryToCloseOrder();
            }
        }).start();


    }

    private void mSetShowCommentListener() {
        this.imageButtonComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mShowCommentsFragment(cIntakeorder.currentIntakeOrder.pCommentObl(), "");
            }
        });
    }

    private void mSetDeviationsListener() {
        this.switchDeviations.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean show) {
                if (switchDeviations.isChecked()) {
                    showDefectsBln = true;
                    getDeviationsReceiverorderSummaryLineAdapter().pShowDeviations();
                } else {
                    showDefectsBln = false;
                    getReceiverorderSummaryLineAdapter().pFillData(cReceiveorderSummaryLine.allReceiveorderSummaryLinesObl);
                    linesToShowObl = cReceiveorderSummaryLine.allReceiveorderSummaryLinesObl;
                    mFillRecycler();
                }
            }
        });
    }

    private void mSetAddArticleListener() {
        this.imageAddArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mShowAddArticleFragment();
            }
        });
    }

    private void mCloseOrderListener() {

        this.imageButtonCloseOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String acceptStr = cAppExtension.activity.getString(R.string.message_close);
                String rejectStr = cAppExtension.activity.getString(R.string.message_leave);

                if (BuildConfig.FLAVOR.toUpperCase().equalsIgnoreCase("BMN")) {
                    acceptStr = cAppExtension.activity.getString(R.string.message_approve);
                    rejectStr = cAppExtension.activity.getString(R.string.message_cancel);
                }

                if (cIntakeorder.currentIntakeOrder.isGenerated()) {
                    mShowCloseOrderDialog(rejectStr, acceptStr);
                    return;
                }

                //do we need an administrator for this?
                if (!cSetting.RECEIVE_DEVIATIONS_PASSWORD().isEmpty() && cReceiveorderSummaryLine.totalItemsDifference() > 0) {
                    ReceiveLinesActivity.closeOrderClickedBln = true;
                    cUserInterface.pShowpasswordDialog(getString(R.string.supervisor_password_header), getString(R.string.supervisor_deviations_password_text), false);
                    return;
                }
                   mShowCloseOrderDialog(cAppExtension.activity.getString(R.string.message_leave), cAppExtension.activity.getString(R.string.message_close));

            }
        });
    }

    private void mSetRecyclerTouchListener() {


        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new cReceiveorderSummaryLineRecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(this.recyclerViewLines);
    }

    private void mDoUnknownScan(String pvErrorMessageStr, String pvScannedBarcodeStr) {
        cUserInterface.pDoExplodingScreen(pvErrorMessageStr, pvScannedBarcodeStr, true, true);
        ReceiveLinesActivity.busyBln = true;
    }

    private void mShowCommentsFragment(List<cComment> pvDataObl, String pvTitleStr) {

        cUserInterface.pCheckAndCloseOpenDialogs();

        Bundle bundle = new Bundle();
        bundle.putString(cPublicDefinitions.KEY_COMMENTHEADER, pvTitleStr);

        CommentFragment commentFragment = new CommentFragment(pvDataObl);
        commentFragment.setArguments(bundle);

        commentFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.COMMENTFRAGMENT_TAG);
        cUserInterface.pPlaySound(R.raw.message, 0);
    }

    private void mStepFailed(String pvErrorMessageStr, String pvScannedBarcodeStr) {
        cUserInterface.pDoExplodingScreen(pvErrorMessageStr, pvScannedBarcodeStr, true, true);
    }

    private void mStartOrderSelectActivity() {

        cAppExtension.activity.runOnUiThread(new Runnable() {
            public void run() {
                Intent intent = new Intent(cAppExtension.context, IntakeAndReceiveSelectActivity.class);
                IntakeAndReceiveSelectActivity.startedViaMenuBln = false;
                cAppExtension.activity.startActivity(intent);
            }
        });

    }

    private void mStartPackagingActivity() {

        cAppExtension.activity.runOnUiThread(new Runnable() {
            public void run() {
                Intent intent = new Intent(cAppExtension.context, PackagingActivity.class);
                cAppExtension.activity.startActivity(intent);
            }
        });

    }

    private void mShowComments() {

        if (cIntakeorder.currentIntakeOrder.pCommentObl() == null || cIntakeorder.currentIntakeOrder.pCommentObl().size() == 0) {
            this.imageButtonComments.setVisibility(View.INVISIBLE);
            return;
        }

        this.imageButtonComments.setVisibility(View.VISIBLE);

        //We already showed the comments
        if (cComment.commentsShownBln) {
            return;
        }

        this.mShowCommentsFragment(cIntakeorder.currentIntakeOrder.pCommentObl(), "");
        cComment.commentsShownBln = true;
    }

    private void mShowCloseOrderDialog(String pvRejectStr, String pvAcceptStr) {

        cUserInterface.pCheckAndCloseOpenDialogs();
        String messageStr = "";

        ReceiveLinesActivity.closeOrderClickedBln = true;
        ReceiveLinesActivity.packagingClickedBln = false;

        if (!cIntakeorder.currentIntakeOrder.isGenerated()) {
            if (cReceiveorderSummaryLine.totalItemsDifference() == 0) {
                messageStr = (cAppExtension.activity.getString(R.string.message_exactly_what_you_needed));
            }

            if (cReceiveorderSummaryLine.totalItems() > cReceiveorderSummaryLine.totalItemsHandled()) {
                messageStr = cText.pDoubleToStringStr(cReceiveorderSummaryLine.totalItemsDifference()) + " " + cAppExtension.activity.getString(R.string.message_less_items);
            }

            if (cReceiveorderSummaryLine.totalItems() < cReceiveorderSummaryLine.totalItemsHandled()) {
                messageStr = cText.pDoubleToStringStr(cReceiveorderSummaryLine.totalItemsDifference()) + " " + cAppExtension.activity.getString(R.string.message_extra_items);
            }

            final AcceptRejectFragment acceptRejectFragment = new AcceptRejectFragment(cAppExtension.activity.getString(R.string.message_close_order),
                    cAppExtension.activity.getString(R.string.message_close_storeorder_text,
                            cText.pDoubleToStringStr(cReceiveorderSummaryLine.totalItemsHandled()),
                            cText.pDoubleToStringStr(cReceiveorderSummaryLine.totalItems()), messageStr),
                    pvRejectStr,
                    pvAcceptStr,
                    false);

            acceptRejectFragment.setCancelable(true);
            cAppExtension.activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // show my popup
                    acceptRejectFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.ACCEPTREJECTFRAGMENT_TAG);
                }
            });
        }

        if (cIntakeorder.currentIntakeOrder.isGenerated()) {


            final AcceptRejectFragment acceptRejectFragment = new AcceptRejectFragment(cAppExtension.activity.getString(R.string.message_close_order),
                    cAppExtension.activity.getString(R.string.message_close_receiveorder_generated_text,
                            cText.pDoubleToStringStr(cReceiveorderSummaryLine.totalItemsHandled())),
                    pvRejectStr,
                    pvAcceptStr,
                    false);

            acceptRejectFragment.setCancelable(true);
            cAppExtension.activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // show my popup
                    acceptRejectFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.ACCEPTREJECTFRAGMENT_TAG);
                }
            });

        }


    }

    private void mShowPackagingDialog(String pvRejectStr, String pvAcceptStr) {

        cUserInterface.pCheckAndCloseOpenDialogs();

        ReceiveLinesActivity.closeOrderClickedBln = false;
        ReceiveLinesActivity.packagingClickedBln = true;


        final AcceptRejectFragment acceptRejectFragment = new AcceptRejectFragment(cAppExtension.activity.getString(R.string.screentitle_packaging),
                cAppExtension.activity.getString(R.string.message_open_packaging_text),
                pvRejectStr,
                pvAcceptStr,
                false);

        acceptRejectFragment.setCancelable(true);
        cAppExtension.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // show my popup
                acceptRejectFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.ACCEPTREJECTFRAGMENT_TAG);
            }
        });
    }

    private  void mShowAddItemDialog(cBarcodeScan pvBarcodeScan, String pvRejectStr,String pvAcceptStr) {

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
                    acceptRejectFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.ACCEPTREJECTFRAGMENT_TAG);
                }
            });

    }

    private  void mStartReceiveActivity(){

        ReceiveLinesActivity.busyBln = false;

        //we have a LINE to handle, so start Receive activity
        Intent intent = new Intent(cAppExtension.context, ReceiveOrderReceiveActivity.class);
        cAppExtension.activity.startActivity(intent);

    }

    private  void mFillRecycler() {

        if (this.linesToShowObl .size() == 0) {
            mNoLinesAvailable(true);
            return;
        }

        this.mNoLinesAvailable(false);
        this.recyclerViewLines.setHasFixedSize(false);
        this.recyclerViewLines.setLayoutManager(new LinearLayoutManager(cAppExtension.context));
        this.recyclerViewLines.setVisibility(View.VISIBLE);

        if (!this.showDefectsBln) {
            this.getReceiverorderSummaryLineAdapter().pFillData(this.linesToShowObl );
            this.recyclerViewLines.setAdapter(this.getReceiverorderSummaryLineAdapter());
        }

        if (this.showDefectsBln) {
            this.getDeviationsReceiverorderSummaryLineAdapter().pFillData(this.linesToShowObl );
            this.recyclerViewLines.setAdapter(this.getDeviationsReceiverorderSummaryLineAdapter());
        }

        //Show the recycler view


    }

    private  void mNoLinesAvailable(final boolean pvShowBln) {


        cAppExtension.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                cUserInterface.pHideGettingData();

                if (pvShowBln) {

                    recyclerViewLines.setVisibility(View.INVISIBLE);

                    FragmentTransaction fragmentTransaction = cAppExtension.fragmentManager.beginTransaction();
                    NothingHereFragment fragment = new NothingHereFragment();
                    fragmentTransaction.replace(R.id.container, fragment);
                    fragmentTransaction.commit();
                    return;
                }

                recyclerViewLines.setVisibility(View.VISIBLE);


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

    private  void mHandleUnknownBarcodeScan(cBarcodeScan pvBarcodeScan) {

        // Check if we can add a line
        if (!cIntakeorder.currentIntakeOrder.isGenerated() && cIntakeorder.currentIntakeOrder.getReceiveNoExtraItemsBln()) {
            this.mStepFailed(cAppExtension.activity.getString(R.string.message_add_article_now_allowed),pvBarcodeScan.getBarcodeOriginalStr());
            return;
        }

        // Should we notify the user before adding extra item
        if (cSetting.RECEIVE_INTAKE_EO_CREATE_EXTRA_ITEM_VALIDATION().equalsIgnoreCase("REQUIRED")) {
            this.mShowAddItemDialog(pvBarcodeScan,cAppExtension.activity.getString(R.string.message_cancel), cAppExtension.activity.getString(R.string.message_add_item));
            return;
        }

        this.pAddUnknownScan(pvBarcodeScan);


    }

    private void mAddUnkownArticle(cBarcodeScan pvBarcodeScan){

        cResult hulpResult = new cResult();
        hulpResult.resultBln = false;

        boolean hulpBln = true;

        if (!cIntakeorder.currentIntakeOrder.pReceiveAddUnkownBarcodeAndItemVariantBln(pvBarcodeScan)){
            hulpBln = false ;
        }

        //Add the barcodeStr via the webservice
        if (!hulpBln) {
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
        this.mStartReceiveActivity();


    }

    private void mAddERPArticle(cBarcodeScan pvBarcodeScan){

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
        this.mStartReceiveActivity();

    }

    private void mShowAddArticleFragment(){
        AddArticleFragment addArticleFragment = new AddArticleFragment();
        addArticleFragment.setCancelable(true);
        addArticleFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.ADDARTICLE_TAG);

    }

    private void mRemoveAdapterFromFragment(){

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
        this.linesToShowObl = cReceiveorderSummaryLine.allReceiveorderSummaryLinesObl;
        this.mFillRecycler();

    }

    private  void mShowSending() {
        final SendingFragment sendingFragment = new SendingFragment();
        sendingFragment.setCancelable(true);
        cAppExtension.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // show my popup
                sendingFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.SENDING_TAG);
            }
        });
    }

    private  void mShowSend() {
        Fragment fragment = cAppExtension.fragmentManager.findFragmentByTag(cPublicDefinitions.SENDING_TAG);
        if (fragment != null) {
            if (fragment instanceof SendingFragment) {
                ((SendingFragment) fragment).pShowFlyAwayAnimation();
            }
        }
    }

    private  void mShowSendFailed(String pvErrorMessageStr) {
        Fragment fragment = cAppExtension.fragmentManager.findFragmentByTag(cPublicDefinitions.SENDING_TAG);
        if (fragment != null) {
            if (fragment instanceof SendingFragment) {
                ((SendingFragment) fragment).pShowCrashAnimation(pvErrorMessageStr);
            }
        }
    }


    //End Region Private Methods

}



