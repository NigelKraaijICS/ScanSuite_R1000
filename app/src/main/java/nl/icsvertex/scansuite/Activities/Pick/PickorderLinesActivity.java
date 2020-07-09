package nl.icsvertex.scansuite.Activities.Pick;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

import com.google.android.material.tabs.TabLayout;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import ICS.Interfaces.iICSDefaultActivity;
import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cConnection;
import ICS.Utils.cNoSwipeViewPager;
import ICS.Utils.cRegex;
import ICS.Utils.cResult;
import ICS.Utils.cText;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Basics.BarcodeLayouts.cBarcodeLayout;
import SSU_WHS.Basics.Settings.cSetting;
import SSU_WHS.Basics.ShippingAgentServiceShippingUnits.cShippingAgentServiceShippingUnit;
import SSU_WHS.Basics.ShippingAgentServices.cShippingAgentService;
import SSU_WHS.Basics.ShippingAgents.cShippingAgent;
import SSU_WHS.Basics.Workplaces.cWorkplace;
import SSU_WHS.General.Comments.cComment;
import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.General.cPublicDefinitions;
import SSU_WHS.Picken.PickorderBarcodes.cPickorderBarcode;
import SSU_WHS.Picken.PickorderLines.cPickorderLine;
import SSU_WHS.Picken.Pickorders.cPickorder;
import SSU_WHS.Picken.Shipment.cShipment;
import nl.icsvertex.scansuite.Activities.Ship.ShiporderLinesActivity;
import nl.icsvertex.scansuite.Activities.Sort.SortorderLinesActivity;
import nl.icsvertex.scansuite.Fragments.Dialogs.AcceptRejectFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.CommentFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.CurrentLocationFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.OrderDoneFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.SendingFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.WorkplaceFragment;
import nl.icsvertex.scansuite.Fragments.Pick.PickorderLinesToPickFragment;
import nl.icsvertex.scansuite.PagerAdapters.PickorderLinesPagerAdapter;
import nl.icsvertex.scansuite.R;

public class PickorderLinesActivity extends AppCompatActivity implements iICSDefaultActivity {

    //Region Public Properties
    public static Fragment currentLineFragment;
    //End Region Public Properties

    //Region Private Properties

    //Region Views
    private TextView textViewChosenOrder;
    private TextView quantityPickordersText;
    private TabLayout pickorderLinesTabLayout;
    private cNoSwipeViewPager pickorderLinesViewPager;
    private ImageView imageButtonComments;
    private ImageView toolbarImage;
    private TextView toolbarTitle;

    //End Region Views

    //End Region Private Properties

    //Region Default Methods

    @Override
    protected void onCreate(Bundle pvSavedInstanceState) {
        super.onCreate(pvSavedInstanceState);
        setContentView(R.layout.activity_pickorderlines);
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
        cConnection.pRegisterWifiChangedReceiver();
        cUserInterface.pEnableScanner();
    }

    @Override
    protected void onPause() {
        try {
            cBarcodeScan.pUnregisterBarcodeReceiver();
            cConnection.pUnregisterWifiChangedReceiver();
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


    //End Region Default Methods

    //Region iICSDefaultActivity defaults

    @Override
    public void mActivityInitialize() {

        this.mSetAppExtensions();

        this.mFindViews();

        this.mSetToolbar(getResources().getString(R.string.screentitle_pickorderlines));

        this.mFieldsInitialize();

        this.mInitScreen();

        cBarcodeScan.pRegisterBarcodeReceiver();
    }

    @Override
    public void mSetAppExtensions() {
        cAppExtension.context = this;
        cAppExtension.fragmentActivity  = this;
        cAppExtension.activity = this;
        cAppExtension.fragmentManager  = getSupportFragmentManager();
    }

    @Override
    public void mFindViews() {

        this.toolbarImage = findViewById(R.id.toolbarImage);
        this.toolbarTitle = findViewById(R.id.toolbarTitle);
        this.quantityPickordersText = findViewById(R.id.quantityPickordersText);
        this.pickorderLinesTabLayout = findViewById(R.id.pickorderLinesTabLayout);
        this.pickorderLinesViewPager = findViewById(R.id.pickorderLinesViewpager);
        this.textViewChosenOrder = findViewById(R.id.textViewChosenOrder);
        this.imageButtonComments = findViewById(R.id.imageButtonComments);
    }

    @Override
    public void mSetToolbar(String pvScreenTitleStr) {
        this.toolbarImage.setImageResource(R.drawable.ic_menu_pick);
        this.toolbarTitle.setText(pvScreenTitleStr);
        this.toolbarTitle.setSelected(true);

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

        this.textViewChosenOrder.setText(cPickorder.currentPickOrder.getOrderNumberStr());
        this.pickorderLinesTabLayout.addTab(this.pickorderLinesTabLayout.newTab().setText(R.string.tab_pickorderline_topick));
        this.pickorderLinesTabLayout.addTab(this.pickorderLinesTabLayout.newTab().setText(R.string.tab_pickorderline_picked));
        this.pickorderLinesTabLayout.addTab(this.pickorderLinesTabLayout.newTab().setText(R.string.tab_pickorderline_total));

        PickorderLinesPagerAdapter pickorderLinesPagerAdapter = new PickorderLinesPagerAdapter(this.pickorderLinesTabLayout.getTabCount());
        this.pickorderLinesViewPager.setAdapter(pickorderLinesPagerAdapter);
        this.pickorderLinesViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(this.pickorderLinesTabLayout));
        this.pickorderLinesTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab pvTab) {
                pickorderLinesViewPager.setCurrentItem(pvTab.getPosition());
                mChangeSelectedTab(pvTab);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab pvTab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab pvTab) {

            }
        });

    }

    @Override
    public void mSetListeners() {
        this.mSetShowCommentListener();
    }

    @Override
    public void mInitScreen() {

        this.mShowComments();

        //Call this here, because this is called everytime the activiy gets shown
        this.pCheckAllDone();

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

    public  void pChangeTabCounterText(String pvTextStr){
        this.quantityPickordersText.setText(pvTextStr);
    }

    public  void pPicklineSelected(cPickorderLine pvPickorderLine) {
        cPickorderLine.currentPickOrderLine = pvPickorderLine;

        if (PickorderLinesActivity.currentLineFragment instanceof  PickorderLinesToPickFragment) {
            PickorderLinesToPickFragment pickorderLinesToPickFragment = (PickorderLinesToPickFragment)PickorderLinesActivity.currentLineFragment;
            pickorderLinesToPickFragment.pSetChosenBinCode();
        }

    }

    public  void pPicklineToResetSelected(cPickorderLine pvPickorderLine) {
        cPickorderLine.currentPickOrderLine = pvPickorderLine;
    }

    public  void pSetCurrentLocation(String pvCurrentLocationStr) {

        if (!cPickorder.currentPickOrder.pUpdateCurrentLocationBln(pvCurrentLocationStr)) {
            cUserInterface.pDoExplodingScreen(cAppExtension.context.getString(R.string.error_currentlocation_could_not_update), "", true, false);
            return;
        }

        //Check if we are done
        if (cPickorder.currentPickOrder.pGetLinesNotHandledFromDatabasObl().size() > 0 ) {
            this.mStartOrderSelectActivity();
            return;
        }

        //We are done
        this.pPickingDone("");

    }

    public  void pHandleScan(cBarcodeScan pvBarcodeScan, Boolean pvBinSelectedBln) {

        cUserInterface.pCheckAndCloseOpenDialogs();
        cResult hulpResult;

        //BIN button has been pressed, so we already have a current line
        if (pvBinSelectedBln) {

            //Clear current barcodeStr
            cPickorderBarcode.currentPickorderBarcode = null;

            hulpResult = cPickorderLine.currentPickOrderLine.pLineBusyRst();
            if (!hulpResult.resultBln) {
                mStepFailed(hulpResult.messagesStr(),cWarehouseorder.StepCodeEnu.Pick_Picking,cWarehouseorder.WorkflowPickStepEnu.PickPicking);
                cPickorderLine.currentPickOrderLine = null;
                return;
            }

            //Set last selected index
            cPickorder.currentPickOrder.lastSelectedIndexInt = cPickorder.currentPickOrder.pGetIndexOfNotHandledLineInt(cPickorderLine.currentPickOrderLine);

            //we have a line to handle, so start Pick activity
            this.mStartPickActivity();
            return;
        }

        //If we scan a branch reset current branch
        if (cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(),cBarcodeLayout.barcodeLayoutEnu.LOCATION)) {
            cPickorder.currentPickOrder.scannedBranch  = null;
        }

        //If we still need a destination scan, make sure we scan this first
        if (cPickorder.currentPickOrder.scannedBranch  == null && cPickorder.currentPickOrder.isPFBln() ) {
            cResult hulpRst = this.mCheckDestionationRst(pvBarcodeScan);
            if (! hulpRst.resultBln) {
                cUserInterface.pDoExplodingScreen(hulpRst.messagesStr(),"", true, true);
            }

            //If we scanned, refresh to pick fragment and leave this void
            if (PickorderLinesActivity.currentLineFragment instanceof PickorderLinesToPickFragment ) {
                PickorderLinesToPickFragment pickorderLinesToPickFragment = (PickorderLinesToPickFragment)PickorderLinesActivity.currentLineFragment;
                pickorderLinesToPickFragment.pBranchScanned();
                return;
            }
        }

        //Check if we have scanned a BIN and check if there are not handled linesInt for this BIN
        if (cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(),cBarcodeLayout.barcodeLayoutEnu.BIN)) {

            String barcodewithoutPrefix = cRegex.pStripRegexPrefixStr(pvBarcodeScan.getBarcodeOriginalStr());

            //Clear current barcodeStr
            cPickorderBarcode.currentPickorderBarcode = null;

            cPickorderLine.currentPickOrderLine = cPickorder.currentPickOrder.pGetNetxLineToHandleForBin(barcodewithoutPrefix);
            if (cPickorderLine.currentPickOrderLine == null) {
                this.mDoUnknownScan(cAppExtension.context.getString(R.string.nothing_more_todo_for_bin), barcodewithoutPrefix);
                return;
            }

            hulpResult = cPickorderLine.currentPickOrderLine.pLineBusyRst();
            if (!hulpResult.resultBln) {
                this.mStepFailed(hulpResult.messagesStr(),cWarehouseorder.StepCodeEnu.Pick_Picking,cWarehouseorder.WorkflowPickStepEnu.PickPackAndShip);
                cPickorderLine.currentPickOrderLine = null;
                return;
            }

            //Set last selected index
            cPickorder.currentPickOrder.lastSelectedIndexInt = cPickorder.currentPickOrder.pGetIndexOfNotHandledLineInt(cPickorderLine.currentPickOrderLine);

            //we have a line to handle, so start Pick activity
            this.mStartPickActivity();
            return;
        }

        //Check if we have scanned an ARTICLE and check if there are not handled linesInt for this ARTICLE
        if (cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(),cBarcodeLayout.barcodeLayoutEnu.ARTICLE)) {

            if (!cSetting.PICK_BIN_IS_ITEM()) {
                //unknown scan
                this.mDoUnknownScan(cAppExtension.context.getString(R.string.error_article_scan_not_allowed), pvBarcodeScan.getBarcodeOriginalStr());
                return;
            }

            String barcodewithoutPrefix = cRegex.pStripRegexPrefixStr(pvBarcodeScan.getBarcodeOriginalStr());

            // Article always had BIN, so ARTICLE is EQUAL to BIN
            cPickorderLine.currentPickOrderLine = cPickorder.currentPickOrder.pGetLineNotHandledByBarcode(barcodewithoutPrefix);
            if (cPickorderLine.currentPickOrderLine == null) {
                this.mDoUnknownScan(cAppExtension.context.getString(R.string.nothing_more_todo_for_article), barcodewithoutPrefix);
                return;
            }

            cPickorderBarcode.currentPickorderBarcode = cPickorderBarcode.pGetPickbarcodeViaBarcode(pvBarcodeScan);
            if (cPickorderBarcode.currentPickorderBarcode == null) {
                cPickorderLine.currentPickOrderLine = null;
                this.mDoUnknownScan(cAppExtension.context.getString(R.string.nothing_more_todo_for_article), barcodewithoutPrefix);
                return;
            }

            hulpResult = cPickorderLine.currentPickOrderLine.pLineBusyRst();
            if (!hulpResult.resultBln) {
                this.mStepFailed(hulpResult.messagesStr(),cWarehouseorder.StepCodeEnu.Pick_Picking,cWarehouseorder.WorkflowPickStepEnu.PickPackAndShip);
                cPickorderLine.currentPickOrderLine = null;
                return;
            }

            //Set last selected index
            cPickorder.currentPickOrder.lastSelectedIndexInt = cPickorder.currentPickOrder.pGetIndexOfNotHandledLineInt(cPickorderLine.currentPickOrderLine);

            //we have a line to handle, so start Pick activity
            this.mStartPickActivity();
            return;
        }

        //unknown scan
        this.mDoUnknownScan(cAppExtension.context.getString(R.string.error_unknown_barcode), pvBarcodeScan.getBarcodeOriginalStr());

    }

    public  void pPickingDone(String pvCurrentLocationStr) {

        //If we received a current location, then update it via the webservice and locally
        //If we didn't receive a location, then we picked 0 items, so it's oke
        if (!pvCurrentLocationStr.isEmpty()){
            if (!cPickorder.currentPickOrder.pUpdateCurrentLocationBln(pvCurrentLocationStr)) {
                cUserInterface.pDoExplodingScreen(cAppExtension.context.getString(R.string.error_currentlocation_could_not_update), "", true, false);
                return;
            }
        }

        if (cPickorder.currentPickOrder.pQuantityHandledDbl() > 0 ) {
            //Pick Transfer
            if (cPickorder.currentPickOrder.isTransferBln()) {
                if (!cPickorder.currentPickOrder.PackAndShipNeededBln() && !cPickorder.currentPickOrder.isSortableBln()&&
                        cPickorder.currentPickOrder.isPickTransferAskWorkplaceBln() && cWorkplace.currentWorkplace == null ) {
                    this.mShowWorkplaceFragment();
                    return;
                }
            }

            else {

                //Pick Sales

                //Check if we need to select a workplaceStr
                if (!cPickorder.currentPickOrder.PackAndShipNeededBln() && !cPickorder.currentPickOrder.isSortableBln()&&
                        cPickorder.currentPickOrder.isPickSalesAskWorkplaceBln() && cWorkplace.currentWorkplace == null ) {
                    this.mShowWorkplaceFragment();
                    return;
                }

            }
        }

        this.pClosePickAndDecideNextStep();

    }

    public  void pClosePickAndDecideNextStep(){

        // Show that we are getting data
        cUserInterface.pShowGettingData();

        new Thread(new Runnable() {
            public void run() {
                mHandleClosePickAndDecideNextStep();
            }
        }).start();

    }

    public  void pLeaveActivity(){

        cPickorder.currentPickOrder.pLockReleaseViaWebserviceBln(cWarehouseorder.StepCodeEnu.Pick_Picking,cWarehouseorder.WorkflowPickStepEnu.PickPicking);

        //If activity bin is not required, then don't show the fragment
        if (!cPickorder.currentPickOrder.isPickActivityBinRequiredBln() ||
            cPickorder.currentPickOrder.pQuantityHandledDbl() == 0 ||
            !cPickorder.currentPickOrder.getCurrentLocationStr().isEmpty()) {
            this.mStartOrderSelectActivity();
            return;
        }

        this.mShowCurrentLocationFragment();

    }

    public void pAskAbort() {

        cUserInterface.pCheckAndCloseOpenDialogs();

        AlertDialog.Builder builder = new AlertDialog.Builder(cAppExtension.context);
        builder.setTitle(R.string.message_abort_header);
        builder.setMessage(cAppExtension.activity.getString(R.string.message_abort_text));
        builder.setPositiveButton(R.string.button_abort, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                cUserInterface.pCheckAndCloseOpenDialogs();
                mAbortOrder();
            }
        });

        builder.setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                cUserInterface.pCheckAndCloseOpenDialogs();
            }
        });

        builder.show();
    }

    private void mAbortOrder() {

        cUserInterface.pShowGettingData();

        if (!cPickorder.currentPickOrder.pAbortBln()) {
            cUserInterface.pDoExplodingScreen(cAppExtension.context.getString(R.string.error_couldnt_abort_order), cPickorderLine.currentPickOrderLine.getLineNoInt().toString(), true, true );
            return;
        }

        cUserInterface.pHideGettingData();

        //Check if we are done
        this.pCheckAllDone();

    }

    //End Region Public Methods

    //Region Private Methods

    private  boolean mTryToCloseOrderBln(){

        cResult hulpResult;
        hulpResult = new cResult();
        hulpResult.resultBln = false;

        hulpResult = cPickorder.currentPickOrder.pPickHandledViaWebserviceRst();

        //Everything was fine, so we are done
        if (hulpResult.resultBln) {
            return true;
        }

        //Something went wrong, but no further actions are needed, so ony show reason of failure
        if ( hulpResult.activityActionEnu == cWarehouseorder.ActivityActionEnu.Unknown ) {
            cUserInterface.pDoExplodingScreen(hulpResult.messagesStr(),"",true,true);
            return  false;
        }

        //Something went wrong, the order has been deleted, so show comments and refresh
        if (hulpResult.activityActionEnu == cWarehouseorder.ActivityActionEnu.Hold ) {

            //If we got any comments, show them
            if (cPickorder.currentPickOrder.pFeedbackCommentObl() != null && cPickorder.currentPickOrder.pFeedbackCommentObl().size() > 0 ) {
                //Process comments from webresult
                this.mShowCommentsFragment(cPickorder.currentPickOrder.pFeedbackCommentObl(), hulpResult.messagesStr());
            }

            return  false;
        }

        return true;

    }

    private  void mHandleClosePickAndDecideNextStep(){

        if (!this.mTryToCloseOrderBln()) {
            return;
        }

        if (cPickorder.currentPickOrder.isSortableBln()) {
            this.mSortNextStep();
        }
        else {
            this.mPackAndShipNextStap();
        }

    }

    public void pCheckAllDone() {

        // If not everything is done, then leave
        if (!this.mAllLinesDoneBln()) {
            return;
        }

        //All lines are done

        if (!cConnection.isInternetConnectedBln()) {
            cConnection.pShowTryAgainDialog();
            return;
        }

        // If not everything is sent, then leave
        if (!this.mCheckAndSentLinesBln()) {
            return;
        }


        if (cPickorder.currentPickOrder.pQuantityHandledDbl() == 0) {
            // Show order done fragment
            this.mShowOrderDoneFragment(false);
            return;
        }

        // If there is no next step, then we are done
        if (!cPickorder.currentPickOrder.PackAndShipNeededBln() && !cPickorder.currentPickOrder.isBPBln() && !cPickorder.currentPickOrder.isBCBln()) {
            if (!cPickorder.currentPickOrder.isPickActivityBinRequiredBln() ||
                    !cPickorder.currentPickOrder.getCurrentLocationStr().isEmpty()) {
                // Show order done fragment
                this.mShowOrderDoneFragment(false);
                return;
            }

            // Show order done fragment
            this.mShowOrderDoneFragment(true);
        }

        //Show Current Location fragment
        if (cPickorder.currentPickOrder.isPickActivityBinRequiredBln() &&
            cPickorder.currentPickOrder.getCurrentLocationStr().isEmpty()) {
            // Show order done fragment
            this.mShowCurrentLocationFragment();
            return;
        }

        //We are done
        this.pPickingDone("");

    }

    private void mChangeSelectedTab(TabLayout.Tab pvTab) {

        switch (pvTab.getPosition()) {
            case 0:
                this.pChangeTabCounterText(cText.pDoubleToStringStr(cPickorder.currentPickOrder.pQuantityNotHandledDbl()) + "/" + cText.pDoubleToStringStr(cPickorder.currentPickOrder.pQuantityTotalDbl()));
                break;
            case 1:
                this.pChangeTabCounterText(cText.pDoubleToStringStr(cPickorder.currentPickOrder.pQuantityHandledDbl()) + "/" + cText.pDoubleToStringStr(cPickorder.currentPickOrder.pQuantityTotalDbl()));
                break;
            case 2:
                this.pChangeTabCounterText(cText.pDoubleToStringStr(cPickorder.currentPickOrder.pQuantityTotalDbl()));
                break;
            default:

        }
    }

    private void mSetShowCommentListener() {
        this.imageButtonComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mShowCommentsFragment(cPickorder.currentPickOrder.pFeedbackAndPickCommentObl(),"");
            }
        });
    }

    private   void mDoUnknownScan(String pvErrorMessageStr, String pvScannedBarcodeStr) {
        cUserInterface.pDoExplodingScreen(pvErrorMessageStr, pvScannedBarcodeStr, true, true);
    }

    private  void mShowCommentsFragment(List<cComment> pvDataObl, String pvTitleStr) {

        cUserInterface.pCheckAndCloseOpenDialogs();

        Bundle bundle = new Bundle();
        bundle.putString(cPublicDefinitions.KEY_COMMENTHEADER, pvTitleStr);

        CommentFragment commentFragment = new CommentFragment(pvDataObl);
        commentFragment.setArguments(bundle);

        commentFragment.show(cAppExtension.fragmentManager , cPublicDefinitions.COMMENTFRAGMENT_TAG);
        cUserInterface.pPlaySound(R.raw.message, 0);
    }

    private  void mShowCurrentLocationFragment() {

        cUserInterface.pCheckAndCloseOpenDialogs();
        cUserInterface.pPlaySound(R.raw.goodsound, null);

        final CurrentLocationFragment currentLocationFragment = new CurrentLocationFragment();
        currentLocationFragment.setCancelable(true);
        currentLocationFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.CURRENTLOCATION_TAG);
    }

    private  void mShowWorkplaceFragment() {

        WorkplaceFragment workplaceFragment = new WorkplaceFragment();
        workplaceFragment.setCancelable(false);
        workplaceFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.WORKPLACEFRAGMENT_TAG);
    }

    private  void mShowOrderDoneFragment(Boolean pvShowCurrentLocationBln) {

        cUserInterface.pPlaySound(R.raw.goodsound, null);

        final OrderDoneFragment orderDoneFragment = new OrderDoneFragment(pvShowCurrentLocationBln);
        orderDoneFragment.setCancelable(false);
        orderDoneFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.ORDERDONE_TAG);
    }

    private  Boolean mCheckAndSentLinesBln() {

        final List<cPickorderLine> linesToSendObl = cPickorder.currentPickOrder.pGetLinesToSendFromDatabasObl();

        // If there is nothing to send, then we are done
        if (linesToSendObl.size() == 0 ) {
            return  true;
        }

        this.mShowSending();


        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Callable<Boolean> callableBln = new Callable<Boolean>() {
            @Override
            public Boolean call() {

                // Try to send each line, if one failes then stop
                for (cPickorderLine pickorderLine : linesToSendObl) {

                    //Set the current line
                    cPickorderLine.currentPickOrderLine = pickorderLine;

                    //Try to send the line
                   cPickorderLine.currentPickOrderLine .pHandledBln();

                }
                return  true;
            }
        };


        try {
            Future<Boolean> callableResultBln = executorService.submit(callableBln);
            Boolean hulpBln = callableResultBln.get();

            if (!hulpBln) {
                    mShowNotSent();
                 return false;
            }
            this.mShowSent();
            return  true;
        }
        catch (InterruptedException | ExecutionException ignored) {
        }
      return  false;


    }

    private  Boolean mAllLinesDoneBln() {
        return cPickorder.currentPickOrder.pGetLinesNotHandledFromDatabasObl().size() <= 0;
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

    private void mShowNotSent() {
        Fragment fragment = cAppExtension.fragmentManager.findFragmentByTag(cPublicDefinitions.SENDING_TAG);
        if (fragment != null) {
            if (fragment instanceof SendingFragment) {
                ((SendingFragment) fragment).pShowCrashAnimation("");
            }
        }
    }

    private  void mShowSent() {
        Fragment fragment = cAppExtension.fragmentManager.findFragmentByTag(cPublicDefinitions.SENDING_TAG);
        if (fragment != null) {
            if (fragment instanceof SendingFragment) {
                ((SendingFragment) fragment).pShowFlyAwayAnimation();
            }
        }
    }

    private  void mStepFailed(String pvErrorMessageStr, cWarehouseorder.StepCodeEnu pvStepCodeEnu,int pvWorkflowPickStepInt ){
        cUserInterface.pDoExplodingScreen(pvErrorMessageStr, cPickorder.currentPickOrder.getOrderNumberStr(), true, true );
        cPickorder.currentPickOrder.pLockReleaseViaWebserviceBln(pvStepCodeEnu,pvWorkflowPickStepInt);
        cUserInterface.pCheckAndCloseOpenDialogs();
    }

    private  void mAskSort() {

        cAppExtension.activity.runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(cAppExtension.context);

                builder.setMessage(cAppExtension.context.getString(R.string.question_open_sort));
                builder.setTitle(cAppExtension.context.getString(R.string.question_open_sort_title));
                builder.setPositiveButton(R.string.open, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        mStartSortActivity();
                    }
                });
                builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mStartOrderSelectActivity();
                    }
                });
                builder.setIcon(R.drawable.ic_menu_sort);
                builder.show();
            }
        });
    }

    private  void mAskShip() {

        cAppExtension.activity.runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(cAppExtension.context);

                builder.setMessage(cAppExtension.context.getString(R.string.question_open_ship));
                builder.setTitle(cAppExtension.context.getString(R.string.question_open_ship_title));
                builder.setPositiveButton(R.string.open, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        mStartShipActivity();
                    }
                });
                builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mStartOrderSelectActivity();
                    }
                });
                builder.setIcon(R.drawable.ic_menu_ship);
                builder.show();
            }
        });



    }

    private  void mSortNextStep(){

        //If activity bin is not required, then don't show the fragment
        if ( cPickorder.currentPickOrder.pQuantityHandledDbl() == 0 ) {
            this.mStartOrderSelectActivity();
            return;
        }

        // If setting is not defined, then ask user
        if (cSetting.PICK_PACK_AND_SHIP_AUTO_START() == null) {
            this.mAskSort();
            return;
        }

        // If settings is false, then go back to order select
        if (!cSetting.PICK_SORT_AUTO_START()) {
            this.mStartOrderSelectActivity();
            return;
        }

        // If settings is true, then go  to sort
        if (cSetting.PICK_SORT_AUTO_START()) {
            this.mStartSortActivity();
        }

    }

    private void mPackAndShipNextStap(){

        //If activity bin is not required, then don't show the fragment
        if ( cPickorder.currentPickOrder.pQuantityHandledDbl() == 0 ) {
            this.mStartOrderSelectActivity();
            return;
        }

        //If Pack or Ship is not required, then we are dibe
        if (!cPickorder.currentPickOrder.PackAndShipNeededBln()) {
            this.mStartOrderSelectActivity();
            return;
        }

        // If setting is not defined, then ask user
        if (cSetting.PICK_PACK_AND_SHIP_AUTO_START() == null) {
            this.mAskShip();
            return;
        }

        // If settings is false, then go back to order select
            if (!cSetting.PICK_PACK_AND_SHIP_AUTO_START()) {
                this.mStartOrderSelectActivity();
                return;
            }

        // If settings is true, then go  to ship
            if (cSetting.PICK_PACK_AND_SHIP_AUTO_START()) {
                this.mStartShipActivity();
            }

    }

    private void mTryToLeaveActivity(){

        cUserInterface.pCheckAndCloseOpenDialogs();

        final AcceptRejectFragment acceptRejectFragment = new AcceptRejectFragment(cAppExtension.activity.getString(R.string.message_sure_leave_screen_title),
                cAppExtension.activity.getString(R.string.message_sure_leave_screen_text),cAppExtension.activity.getString(R.string.message_cancel),cAppExtension.activity.getString(R.string.message_leave), false);
        acceptRejectFragment.setCancelable(true);
        cAppExtension.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // show my popup
                acceptRejectFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.ACCEPTREJECTFRAGMENT_TAG);
            }
        });

    }

    private  void mStartOrderSelectActivity() {

        cAppExtension.activity.runOnUiThread(new Runnable() {
            public void run() {
                Intent intent = new Intent(cAppExtension.context, PickorderSelectActivity.class);
                cAppExtension.activity.startActivity(intent);
            }
        });

    }

    private  void mStartSortActivity() {

        cUserInterface.pShowGettingData();

        new Thread(new Runnable() {
            public void run() {
                mHandleStartSortActivity();
            }
        }).start();

    }

    private  void mHandleStartSortActivity(){

        //Clear workplaceStr, so you have to select it in the next step
        cWorkplace.currentWorkplace = null;

        //Try to lock the pickorder
        if (!cPickorder.currentPickOrder.pLockViaWebserviceRst(cWarehouseorder.StepCodeEnu.Pick_Picking, cWarehouseorder.WorkflowPickStepEnu.PickSorting).resultBln) {
            this.mStepFailed(cAppExtension.context.getString(R.string.error_couldnt_lock_order),cWarehouseorder.StepCodeEnu.Pick_Picking,cWarehouseorder.WorkflowPickStepEnu.PickPackAndShip);
            return;
        }

        //Get sort linesInt
        if (!cPickorder.currentPickOrder.pGetLinesViaWebserviceBln(true,cWarehouseorder.PickOrderTypeEnu.SORT)) {
            this.mStepFailed(cAppExtension.context.getString(R.string.error_getting_sort_lines_failed),cWarehouseorder.StepCodeEnu.Pick_Picking,cWarehouseorder.WorkflowPickStepEnu.PickPackAndShip);
            return;
        }


        cAppExtension.activity.runOnUiThread(new Runnable() {
            public void run() {
                //Show Sort Activity
                Intent intent = new Intent(cAppExtension.context, SortorderLinesActivity.class);
                cAppExtension.activity.startActivity(intent);
            }
        });
    }

    private  void mStartShipActivity() {

        cUserInterface.pShowGettingData();

        new Thread(new Runnable() {
            public void run() {
                mHandleStartShipActivity();
            }
        }).start();

    }

    private  void mHandleStartShipActivity(){

        cResult hulpResult;

        if (!this.mTryToLockShipOrderBln()) {
            return;
        }


        hulpResult = this.mGetShipOrderDetailsRst();
        if (!hulpResult.resultBln) {
            this.mStepFailed(hulpResult.messagesStr(),cWarehouseorder.StepCodeEnu.Pick_PackAndShip, cWarehouseorder.WorkflowPickStepEnu.PickPackAndShip);
            return;
        }

        //Show ShipLines
        Intent intent = new Intent(cAppExtension.context, ShiporderLinesActivity.class);
        cAppExtension.activity.startActivity(intent);

    }

    private  void mStartPickActivity(){
        //we have a line to handle, so start Pick activity
        Intent intent = new Intent(cAppExtension.context, PickorderPickActivity.class);
        cAppExtension.activity.startActivity(intent);
    }

    private  void mShowComments(){

        if (cPickorder.currentPickOrder.pFeedbackAndPickCommentObl() == null || cPickorder.currentPickOrder.pFeedbackAndPickCommentObl().size() == 0) {
            this.imageButtonComments.setVisibility(View.INVISIBLE);
            return;
        }

        this.imageButtonComments.setVisibility(View.VISIBLE);

        //We already showed the comments
        if (cComment.commentsShownBln) {
            return;
        }

        this.mShowCommentsFragment(cPickorder.currentPickOrder.pFeedbackAndPickCommentObl(),"");
        cComment.commentsShownBln = true;
    }

    private  boolean mTryToLockShipOrderBln(){

        cResult hulpResult;
        hulpResult = cPickorder.currentPickOrder.pLockViaWebserviceRst(cWarehouseorder.StepCodeEnu.Pick_PackAndShip, cWarehouseorder.WorkflowPickStepEnu.PickPackAndShip);

        //Everything was fine, so we are done
        if (hulpResult.resultBln) {
            return true;
        }

        //Something went wrong, but no further actions are needed, so ony show reason of failure
        if (hulpResult.activityActionEnu == cWarehouseorder.ActivityActionEnu.Unknown ) {
            this.mStepFailed(hulpResult.messagesStr(),cWarehouseorder.StepCodeEnu.Pick_PackAndShip, cWarehouseorder.WorkflowPickStepEnu.PickPackAndShip);
            return  false;
        }

        //Something went wrong, the order has been deleted, so show comments and refresh
        if ( hulpResult.activityActionEnu == cWarehouseorder.ActivityActionEnu.Delete ||
              hulpResult.activityActionEnu == cWarehouseorder.ActivityActionEnu.NoStart ) {


            //If we got any comments, show them
            if (cPickorder.currentPickOrder.pFeedbackCommentObl() != null && cPickorder.currentPickOrder.pFeedbackCommentObl().size() > 0 ) {
                //Process comments from webresult
                this.mShowCommentsFragment(cPickorder.currentPickOrder.pFeedbackCommentObl(), hulpResult.messagesStr());
            }

            return  false;
        }


        return true;

    }

    private  cResult mGetShipOrderDetailsRst() {

        cResult result;

        result = new cResult();
        result.resultBln = true;

        //Clear workplaceStr, so you have to select it in the next step
        cWorkplace.currentWorkplace = null;

        //Check all ShippingAgents
        if (cShippingAgent.allShippingAgentsObl == null || cShippingAgent.allShippingAgentsObl.size() == 0) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_no_shippingagents_available));
            return result;
        }

        //Check all ShippingAgents
        if (cShippingAgentService.allShippingAgentServicesObl == null || cShippingAgentService.allShippingAgentServicesObl.size() == 0) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_no_shippingagent_services_available));
            return result;
        }

        //Check all ShippingAgent Shipping Units
        if (cShippingAgentServiceShippingUnit.allShippingAgentServiceShippingUnitsObl == null || cShippingAgentServiceShippingUnit.allShippingAgentServiceShippingUnitsObl.size() == 0) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_no_shippingagent_services_units_available));
            return result;
        }

        //Clear all shipments
        cShipment.allShipmentsObl = null;

        // Get all linesInt, if zero than there is something wrong
        if (!cPickorder.currentPickOrder.pGetPackAndShipLinesViaWebserviceBln(true)) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_getting_pack_and_ship_lines_failed));
            return result;
        }

        // Get all packages
        if (!cPickorder.currentPickOrder.pGetShippingPackagedViaWebserviceBln(true)) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_getting_packages_failed));
            return result;
        }

        // Get all adresses, if system settings Pick Shipping Sales == false then don't ask web service
        if (!cPickorder.currentPickOrder.pGetAdressesViaWebserviceBln(true)) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_adresses_failed));
            return result;
        }

        // Get all comments
        if (!cPickorder.currentPickOrder.pGetCommentsViaWebserviceBln(true)) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_comments_failed));
            return result;
        }

        //If this is single article, then get barcodes
        if (cPickorder.currentPickOrder.isSingleArticleOrdersBln()) {
            if (!cPickorder.currentPickOrder.pGetBarcodesViaWebserviceBln(true)) {
                result.resultBln = false;
                result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_barcodes_failed));
                return result;
            }
        }

        return result;
    }

    private  cResult mCheckDestionationRst(cBarcodeScan pvBarcodeScan) {

        cResult resultRst = new cResult();

        //If we don't need a branch, we are done
        if (!cPickorder.currentPickOrder.isPFBln()) {
            resultRst.resultBln = true;
            return  resultRst;
        }

        if (cPickorder.currentPickOrder.destionationBranch() != null) {
            cPickorder.currentPickOrder.scannedBranch = cPickorder.currentPickOrder.destionationBranch();
            resultRst.resultBln = true;
            return  resultRst;
        }

        //Check if scan matches a branch in open lines
        cPickorder.currentPickOrder.scannedBranch = cPickorder.currentPickOrder.pGetBranchForOpenLines(pvBarcodeScan.getBarcodeOriginalStr());
        if (cPickorder.currentPickOrder.scannedBranch  != null) {
            resultRst.resultBln = true;
            return  resultRst;
        }

        //If we don't have a match, check if we have a location scan
        if (!cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(),cBarcodeLayout.barcodeLayoutEnu.LOCATION)) {
            cPickorder.currentPickOrder.scannedBranch  = null;
            resultRst.resultBln = false;
            resultRst.pAddErrorMessage(cAppExtension.activity.getString(R.string.message_scan_is_not_location));
            return  resultRst;
        }

        //We have a location scan, now strip the prefix and check if plain value matches a branch in open lines
        String barcodewithoutPrefix = cRegex.pStripRegexPrefixStr(pvBarcodeScan.getBarcodeOriginalStr());
        cPickorder.currentPickOrder.scannedBranch  = cPickorder.currentPickOrder.pGetBranchForOpenLines(barcodewithoutPrefix);
        if (cPickorder.currentPickOrder.scannedBranch  != null) {
            resultRst.resultBln = true;
            return  resultRst;
        }

        resultRst.resultBln = false;
        resultRst.pAddErrorMessage(cAppExtension.activity.getString(R.string.message_location_incorrect));
        return  resultRst;

    }

    //End Region Private Methods

}
