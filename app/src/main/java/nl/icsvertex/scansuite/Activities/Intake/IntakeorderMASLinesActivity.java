package nl.icsvertex.scansuite.Activities.Intake;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
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

import ICS.Interfaces.iICSDefaultActivity;
import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cRegex;
import ICS.Utils.cResult;
import ICS.Utils.cText;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Basics.BarcodeLayouts.cBarcodeLayout;
import SSU_WHS.Basics.Users.cUser;
import SSU_WHS.General.Comments.cComment;
import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.General.cPublicDefinitions;
import SSU_WHS.Intake.IntakeorderBarcodes.cIntakeorderBarcode;
import SSU_WHS.Intake.IntakeorderMATLineSummary.cIntakeorderMATSummaryLine;
import SSU_WHS.Intake.IntakeorderMATLineSummary.cIntakeorderMATSummaryLineAdapter;
import SSU_WHS.Intake.IntakeorderMATLineSummary.cIntakeorderMATSummaryLineRecyclerItemTouchHelper;
import SSU_WHS.Intake.Intakeorders.cIntakeorder;
import SSU_WHS.PackAndShip.PackAndShipOrders.cPackAndShipOrder;
import SSU_WHS.PackAndShip.PackAndShipShipment.cPackAndShipShipment;
import nl.icsvertex.scansuite.Activities.IntakeAndReceive.IntakeAndReceiveSelectActivity;
import nl.icsvertex.scansuite.Fragments.Dialogs.AcceptRejectFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.CommentFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.NothingHereFragment;
import nl.icsvertex.scansuite.R;

public class IntakeorderMASLinesActivity extends AppCompatActivity implements iICSDefaultActivity, cIntakeorderMATSummaryLineRecyclerItemTouchHelper.RecyclerItemTouchHelperListener{

    //Region Public Properties
    //End Region Public Properties

    //Region Private Properties

    //Region Views

    private TextView textViewChosenOrder;
    private ImageView imageButtonComments;

    private ImageView toolbarImage;
    private TextView toolbarTitle;
    private TextView toolbarSubTitle;
    private TextView toolbarSubtext2;

    private RecyclerView recyclerViewLines;

    private ImageView imageButtonCloseOrder;

    private cIntakeorderMATSummaryLineAdapter intakeorderMATSummaryLineAdapter;

    private cIntakeorderMATSummaryLineAdapter getIntakeorderMATSummaryLineAdapter() {
        if (this.intakeorderMATSummaryLineAdapter == null) {
            this.intakeorderMATSummaryLineAdapter = new cIntakeorderMATSummaryLineAdapter();
        }

        return this.intakeorderMATSummaryLineAdapter;
    }

    //End Region Views

    //End Region Private Properties

    //Region Default Methods

    @Override
    protected void onCreate(Bundle pvSavedInstanceState) {
        super.onCreate(pvSavedInstanceState);
        setContentView(R.layout.activity_intakeorder_lines_generated);
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

        if (!(pvViewHolder instanceof  cIntakeorderMATSummaryLineAdapter.IntakeorderMATSummaryLineViewHolder)) {
            return;
        }

        cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine = cIntakeorderMATSummaryLine.sortedMATSummaryLinesGeneratedObl().get(pvPositionInt);

        //Reset the line
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
        this.toolbarSubtext2 = findViewById(R.id.toolbarSubtext2);
        this.textViewChosenOrder = findViewById(R.id.textViewChosenOrder);
        this.imageButtonComments = findViewById(R.id.imageButtonComments);
        this.recyclerViewLines = findViewById(R.id.recyclerViewLines);
        this.imageButtonCloseOrder = findViewById(R.id.imageButtonCloseOrder);
    }

    @Override
    public void mSetToolbar(String pvScreenTitleStr) {
        this.toolbarImage.setImageResource(R.drawable.ic_menu_intake);
        this.toolbarTitle.setText(pvScreenTitleStr);
        this.toolbarSubtext2.setText(cIntakeorder.currentIntakeOrder.getBinCodeStr());
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
        this.imageButtonCloseOrder.setVisibility(View.VISIBLE);
        if (cIntakeorderMATSummaryLine.allIntakeorderMATSummaryLinesObl.size() == 0) {
            mNoLinesAvailable(true);
            return;
        }

        this.mNoLinesAvailable(false);
        this.getIntakeorderMATSummaryLineAdapter().pFillData(cIntakeorderMATSummaryLine.sortedMATSummaryLinesGeneratedObl());

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new cIntakeorderMATSummaryLineRecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(this.recyclerViewLines);

    }

    @Override
    public void mSetListeners() {
        this.mSetShowCommentListener();
        this.mSetSendOrderListener();
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

    public void pIntakelineSelected(cIntakeorderMATSummaryLine pvIntakeorderMATSummaryLine) {
        cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine = pvIntakeorderMATSummaryLine;
    }

    public void pHandleScan(cBarcodeScan pvBarcodeScan,
                            Boolean pvLineSelectedBln) {

        cUserInterface.pCheckAndCloseOpenDialogs();
        cResult hulpResult;

        //BIN button has been pressed, so we already have a current line
        if (pvLineSelectedBln) {

            //Clear current barcodeStr
            cIntakeorderBarcode.currentIntakeOrderBarcode = null;
            hulpResult = cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.pSummaryLineBusyRst();
            if (!hulpResult.resultBln) {
                this.mStepFailed(hulpResult.messagesStr());
                cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine = null;
                return;
            }

            this.mStartStoreActivity();
            return;
        }

        //Check if we have scanned a BIN and check if there are not handled linesInt for this BIN
        if (cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(), cBarcodeLayout.barcodeLayoutEnu.BIN)) {

            //Handle the BIN scan
            hulpResult = this.mHandleBINScan(cRegex.pStripRegexPrefixStr(pvBarcodeScan.getBarcodeOriginalStr()));

            //Something went wrong, so show message and stop
            if (!hulpResult.resultBln) {
                this.mDoUnknownScan(hulpResult.messagesStr(), pvBarcodeScan.getBarcodeOriginalStr());
                this.mFillRecycler(cIntakeorderMATSummaryLine.sortedMATSummaryLinesGeneratedObl());
                return;
            }

            //Hide the keyboard
            cUserInterface.pHideKeyboard();

            //Filter has been set in mHandleBINScan so we are donereturn;
            return;
        }

        //Check if we have scanned an ARTICLE and check if there are not handled linesInt for this ARTICLE
        if (cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(), cBarcodeLayout.barcodeLayoutEnu.ARTICLE)) {
            this.mDoUnknownScan(cAppExtension.activity.getString(R.string.error_article_scan_not_allowed), pvBarcodeScan.getBarcodeOriginalStr());
        }
    }

    public void pDone() {

        // Show that we are getting data
        cUserInterface.pShowGettingData();

        new Thread(new Runnable() {
            public void run() {
                mHandleClose();
            }
        }).start();

    }

    public void pLeaveActivity() {


        if (cIntakeorder.currentIntakeOrder != null) {


            if (cIntakeorder.currentIntakeOrder.summaryMATLinesObl() == null || cIntakeorder.currentIntakeOrder.summaryMATLinesObl().size() == 0) {
                cIntakeorder.currentIntakeOrder.pDeleteViaWebserviceBln();
            }
            else
            {
                cIntakeorder.currentIntakeOrder.pLockReleaseViaWebserviceBln();
            }

        }

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

    public void pShowData(List<cIntakeorderMATSummaryLine> pvDataObl) {
        this.mFillRecycler(pvDataObl);
    }

    //End Region Public Methods

    //Region Private Methods

    private cResult mHandleBINScan(String pvBinCodeStr) {

        cResult result = new cResult();
        result.resultBln = true;

        //Get the current BIN
        cIntakeorder.currentIntakeOrder.currentBin = cUser.currentUser.currentBranch.pGetBinByCode(pvBinCodeStr);
        if (cIntakeorder.currentIntakeOrder.currentBin == null) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.activity.getString(R.string.message_bin_unknown, pvBinCodeStr));
            return result;
        }

        this.mStartStoreActivity();

        return result;
    }

    private boolean mTryToCloseOrderBln() {

        cResult hulpResult;
        hulpResult = new cResult();
        hulpResult.resultBln = false;

        hulpResult = cIntakeorder.currentIntakeOrder.pMATHandledViaWebserviceRst();

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
                this.mShowCommentsFragment(cIntakeorder.currentIntakeOrder.pFeedbackCommentObl(), hulpResult.messagesStr());
            }

            return false;
        }

        return true;

    }

    private void mHandleClose() {

        if (!this.mTryToCloseOrderBln()) {
            return;
        }

        this.mStartOrderSelectActivity();

    }

    private void mSetShowCommentListener() {
        this.imageButtonComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mShowCommentsFragment(cIntakeorder.currentIntakeOrder.pCommentObl(), "");
            }
        });
    }

    private void mSetSendOrderListener() {

        this.imageButtonCloseOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mShowCloseOrderDialog(cAppExtension.activity.getString(R.string.message_leave), cAppExtension.activity.getString(R.string.message_close));
            }
        });
    }

    private void mDoUnknownScan(String pvErrorMessageStr, String pvScannedBarcodeStr) {
        cUserInterface.pDoExplodingScreen(pvErrorMessageStr, pvScannedBarcodeStr, true, true);
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

    private void mStepFailed(String pvErrorMessageStr) {
        cUserInterface.pDoExplodingScreen(pvErrorMessageStr, cIntakeorder.currentIntakeOrder.getOrderNumberStr(), true, true);
        cIntakeorder.currentIntakeOrder.pLockReleaseViaWebserviceBln();
        cUserInterface.pCheckAndCloseOpenDialogs();
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
        if (cIntakeorderMATSummaryLine.totalItemsDifference() == 0) {
            messageStr = (cAppExtension.activity.getString(R.string.message_exactly_what_you_needed));
        }

        if (cIntakeorderMATSummaryLine.totalItems() > cIntakeorderMATSummaryLine.totalItemsHandled()) {
            messageStr = cText.pDoubleToStringStr(cIntakeorderMATSummaryLine.totalItemsDifference()) + " LESS items";
        }

        if (cIntakeorderMATSummaryLine.totalItems() < cIntakeorderMATSummaryLine.totalItemsHandled()) {
            messageStr = cText.pDoubleToStringStr(cIntakeorderMATSummaryLine.totalItemsDifference()) + " EXTRA items";
        }

        AcceptRejectFragment acceptRejectFragment;

        if (!cIntakeorder.currentIntakeOrder.isGenerated()) {
            acceptRejectFragment = new AcceptRejectFragment(cAppExtension.activity.getString(R.string.message_close_order),
                    cAppExtension.activity.getString(R.string.message_close_storeorder_text,
                            cText.pDoubleToStringStr(cIntakeorderMATSummaryLine.totalItemsHandled()),
                            cText.pDoubleToStringStr(cIntakeorderMATSummaryLine.totalItems()), messageStr),
                    pvRejectStr,
                    pvAcceptStr,
                    false);
        } else {
            acceptRejectFragment = new AcceptRejectFragment(cAppExtension.activity.getString(R.string.message_close_order),
                    cAppExtension.activity.getString(R.string.message_close_generated_storeorder_text, cText.pDoubleToStringStr(cIntakeorderMATSummaryLine.totalItemsHandled())),
                    pvRejectStr,
                    pvAcceptStr,
                    false);
        }

        acceptRejectFragment.setCancelable(true);
        final AcceptRejectFragment finalAcceptRejectFragment = acceptRejectFragment;
        cAppExtension.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // show my popup
                finalAcceptRejectFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.ACCEPTREJECTFRAGMENT_TAG);
            }
        });

    }

    private void mStartStoreActivity() {

        Intent intent;
        cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine = null;

        if (cIntakeorder.currentIntakeOrder.isGenerated()) {
            intent = new Intent(cAppExtension.context, IntakeOrderIntakeGeneratedActivity.class);
        }
        else
        {
            intent = new Intent(cAppExtension.context, IntakeOrderIntakeActivity.class);
        }

        cAppExtension.activity.startActivity(intent);

    }

    private void mFillRecycler(List<cIntakeorderMATSummaryLine> pvDataObl) {

        if (pvDataObl.size() == 0) {
            return;
        }

        //Show the recycler view
        this.recyclerViewLines.setHasFixedSize(false);
        this.recyclerViewLines.setAdapter(this.getIntakeorderMATSummaryLineAdapter());
        this.recyclerViewLines.setLayoutManager(new LinearLayoutManager(cAppExtension.context));
        this.recyclerViewLines.setVisibility(View.VISIBLE);
    }

    private void mNoLinesAvailable(final boolean pvShowBln) {


        cAppExtension.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                cUserInterface.pHideGettingData();

                if (pvShowBln) {

                    recyclerViewLines.setVisibility(View.INVISIBLE);
                    imageButtonCloseOrder.setVisibility(View.INVISIBLE);

                    FragmentTransaction fragmentTransaction = cAppExtension.fragmentManager.beginTransaction();
                    NothingHereFragment fragment = new NothingHereFragment();
                    fragmentTransaction.replace(R.id.IntakeorderLinesGeneratedContainer, fragment);
                    fragmentTransaction.commit();
                  pSetToolBarTitleWithCounters(cText.pIntToStringStr(cIntakeorderMATSummaryLine.sortedMATSummaryLinesGeneratedObl().size()) + " "  + cAppExtension.activity.getString(R.string.lines));
                    return;
                }

                recyclerViewLines.setVisibility(View.VISIBLE);
                imageButtonCloseOrder.setVisibility(View.VISIBLE);
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

    private void mRemoveAdapterFromFragment(){

        //remove the item from recyclerview
        boolean resultBln = cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.pResetBln();
        if (! resultBln) {
            cUserInterface.pDoExplodingScreen(cAppExtension.activity.getString(R.string.message_reset_line_via_webservice_failed),"",true,true);
            return;
        }

        cUserInterface.pShowSnackbarMessage(this.recyclerViewLines,cAppExtension.activity.getString(R.string.message_line_reset_succesfull), R.raw.headsupsound, true);
        cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine = null;

        this.mFieldsInitialize();

    }

}


