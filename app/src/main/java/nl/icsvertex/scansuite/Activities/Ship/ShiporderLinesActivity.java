package nl.icsvertex.scansuite.Activities.Ship;

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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.List;

import ICS.Interfaces.iICSDefaultActivity;
import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cResult;
import ICS.Utils.cText;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Basics.BarcodeLayouts.cBarcodeLayout;
import SSU_WHS.Basics.Workplaces.cWorkplace;
import SSU_WHS.General.Comments.cComment;
import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.General.cPublicDefinitions;
import SSU_WHS.Picken.Pickorders.cPickorder;
import SSU_WHS.Picken.Shipment.cShipment;
import nl.icsvertex.scansuite.Fragments.Dialogs.CommentFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.OrderDoneFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.WorkplaceFragment;
import nl.icsvertex.scansuite.Fragments.Ship.ShiporderLinesToShipFragment;
import nl.icsvertex.scansuite.PagerAdapters.ShiporderLinesPagerAdapter;
import nl.icsvertex.scansuite.R;

//import android.app.Fragment;

public class ShiporderLinesActivity extends AppCompatActivity implements iICSDefaultActivity {

    //Region Public Properties

    //End Region Public Properties

    //Region Private Properties

    //Region Views

    private  TextView textViewChosenOrder;
    private  TextView quantityShipordersText;
    private  ConstraintLayout container;
    private  TabLayout shiporderLinesTabLayout;
    private  ViewPager shiporderLinesViewPager;
    private  ImageView imageButtonComments;

    private  ImageView toolbarImage;
    private  TextView toolbarTitle;

    public static Fragment currentLineFragment;

    //End Region Views

    //End Region Private Properties


    //Region Default Methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shiporderlines);
        this.mActivityInitialize();

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
        super.onPause();
        cBarcodeScan.pUnregisterBarcodeReceiver();
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
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

    //End Region Default Methods


    //Region iICSDefaultActivity defaults

    @Override
    public void mActivityInitialize() {

        this.mSetAppExtensions();
        this.mFindViews();
        this.mSetToolbar(getResources().getString(R.string.screentitle_shiporderlines));
        this.mFieldsInitialize();
        this.mSetListeners();
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
        this.container = findViewById(R.id.container);
        this.toolbarImage = findViewById(R.id.toolbarImage);
        this.toolbarTitle = findViewById(R.id.toolbarTitle);
        this.quantityShipordersText = findViewById(R.id.quantityShipordersText);
        this.shiporderLinesTabLayout = findViewById(R.id.shiporderLinesTabLayout);
        this.shiporderLinesViewPager = findViewById(R.id.shiporderLinesViewpager);
        this.textViewChosenOrder = findViewById(R.id.textViewChosenOrder);
        this.imageButtonComments = findViewById(R.id.imageButtonComments);
    }

     @Override
    public void mSetToolbar(String pvScreenTitle) {
         this.toolbarImage.setImageResource(R.drawable.ic_menu_ship);
         this.toolbarTitle.setText(pvScreenTitle);
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
        this.shiporderLinesTabLayout.addTab(shiporderLinesTabLayout.newTab().setText(R.string.tab_shiporderline_toship));
        this.shiporderLinesTabLayout.addTab(shiporderLinesTabLayout.newTab().setText(R.string.tab_shiporderline_shipped));
        this.shiporderLinesTabLayout.addTab(shiporderLinesTabLayout.newTab().setText(R.string.tab_shiporderline_total));

        ShiporderLinesPagerAdapter shiporderLinesPagerAdapter = new ShiporderLinesPagerAdapter(this.shiporderLinesTabLayout.getTabCount());
        this.shiporderLinesViewPager.setAdapter(shiporderLinesPagerAdapter);
        this.shiporderLinesViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(shiporderLinesTabLayout));
        this.shiporderLinesTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab pvTab) {
                shiporderLinesViewPager.setCurrentItem(pvTab.getPosition());
                mChangeSelectedTab(pvTab);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void mSetListeners() {

    }

    @Override
    public void mInitScreen() {

        this.mShowComments();

        //Check if we need to register a workplaceStr
        if (cWorkplace.currentWorkplace == null ) {


            if (cWorkplace.allWorkplacesObl.size() > 1) {
                //Show the workplaceStr fragment
                this.mShowWorkplaceFragment();
                return;
            }
            else {
                //Pop-up is not needed, we just select the only workplace there is
                cWorkplace.currentWorkplace = cWorkplace.allWorkplacesObl.get(0);
                this.pWorkplaceSelected();
                return;
            }

        }

        //Call this here, because this is called everytime the activiy gets shown
        this.mCheckAllDone();
    }

    //End Region iICSDefaultActivity defaults


    //Region Public Methods

    public  void pChangeTabCounterText(String pvTextStr){
        this.quantityShipordersText.setText(pvTextStr);

    }

    public void pHandleScan(cBarcodeScan pvBarcodeScan, Boolean pvSourceNoSelectedBln) {

        cUserInterface.pCheckAndCloseOpenDialogs();
        cResult hulpRst;

        //SourceNo button has been pressed, so we already have a current line
        if (pvSourceNoSelectedBln) {

            hulpRst = mCheckShipmentRst();
            if (!hulpRst.resultBln ){
                cUserInterface.pDoExplodingScreen(hulpRst.messagesStr(),"",true,true);
                return;
            }

            //we have a SourceDocument to handle, so start Pick activity
            this.mStartShipActivity();
            return;
        }

        //Check if we have scanned an ARTICLE and check if there are not handled linesInt for this ARTICLE
        if (cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(),cBarcodeLayout.barcodeLayoutEnu.ARTICLE)) {

            //Check if article scan is allowed
            if (!cPickorder.currentPickOrder.isSingleArticleOrdersBln()) {
                this.mStepFailed(cAppExtension.context.getString(R.string.message_article_not_allowed));
                return;
            }

            //Get shipment with scanned barcodeStr
            cShipment.currentShipment = cShipment.pGetShipmentWithScannedArticleBarcode(pvBarcodeScan);

            // We did not find a match, so stop
            if (cShipment.currentShipment == null) {
                this.mStepFailed(cAppExtension.context.getString(R.string.message_unknown_barcode));
                return;
            }

            //Check if we have everything we need to ship the order before showing next activity
            if (cShipment.currentShipment.shippingAgent() == null ||
                    cShipment.currentShipment.shippingAgentService() == null ||
                    cShipment.currentShipment.shippingAgentService().shippingUnitsObl() == null ||
                    cShipment.currentShipment.shippingAgentService().shippingUnitsObl().size() == 0 ) {
                cUserInterface.pDoExplodingScreen(cAppExtension.activity.getString(R.string.message_shipping_basics_invalid),"",true,true);
                return;
            }

            if (cShipment.currentShipment.isHandledBln()) {
                cUserInterface.pDoExplodingScreen(cAppExtension.activity.getString(R.string.message_shipment_already_handled),"",true,true);
                return;
            }

            //We found a match in open shipments
            this.mStartShipActivity();
            return;

        }

        //Get shipment by SourceNo or pickcartbox
        cShipment.currentShipment = cShipment.pGetShipmentWithScannedBarcode(pvBarcodeScan);

        // We did not find a match, so stop
        if (cShipment.currentShipment == null) {
            this.mStepFailed(cAppExtension.context.getString(R.string.message_unknown_barcode));
            return;
        }

        hulpRst = mCheckShipmentRst();
        if (!hulpRst.resultBln ){
            cUserInterface.pDoExplodingScreen(hulpRst.messagesStr(),"",true,true);
            return;
        }

        if (cShipment.currentShipment.isHandledBln()) {
            cUserInterface.pDoExplodingScreen(cAppExtension.activity.getString(R.string.message_shipment_already_handled),"",true,true);
            return;
        }

        //We found a match in open shipments
        this.mStartShipActivity();
    }

    public  void pShipmentSelected(cShipment pvShipment) {
        cShipment.currentShipment = pvShipment;

        if (cAppExtension.dialogFragment instanceof ShiporderLinesToShipFragment) {
            ShiporderLinesToShipFragment shiporderLinesToShipFragment = (ShiporderLinesToShipFragment)cAppExtension.dialogFragment;
            shiporderLinesToShipFragment.pSetChosenShipment();
        }
    }

    public  void pShippingDone() {

        //Try to close
        if (!this.mTryToCloseOrderBln()){
            return;
        }

        //Go back to order select activity
        this.mStartOrderSelectActivity();

    }

    public void pShowOrderDoneFragment() {

        cUserInterface.pPlaySound(R.raw.goodsound, null);

        final OrderDoneFragment orderDoneFragment = new OrderDoneFragment(false);
        orderDoneFragment.setCancelable(false);
        orderDoneFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.ORDERDONE_TAG);
    }

    public  void pWorkplaceSelected(){


        if (!cPickorder.currentPickOrder.pUpdateWorkplaceViaWebserviceBln(cWorkplace.currentWorkplace.getWorkplaceStr())) {
            cUserInterface.pDoExplodingScreen(cAppExtension.activity.getString(R.string.message_workplace_not_updated),"",true,true);
            return;
        }

        //Register barcodeStr receiver, because the workplaceStr fragment has been shown
        cBarcodeScan.pRegisterBarcodeReceiver();

        cUserInterface.pShowSnackbarMessage(this.container,cAppExtension.activity.getString(R.string.message_workplace_selected) + ' ' + cWorkplace.currentWorkplace.getWorkplaceStr() ,R.raw.headsupsound,false);
    }


    //End Region Public Methods

    private void mCheckAllDone() {

        // If not everything is done, then leave
        if (!this.mCheckAllDoneBln()) {
            return;
        }

        // Show order done fragment
        this.pShowOrderDoneFragment();

    }

    private boolean mCheckAllDoneBln() {

        return cPickorder.currentPickOrder.pGetNotHandledShipmentsObl().size() <= 0;
    }

    private void mChangeSelectedTab(TabLayout.Tab pvTab) {

        switch (pvTab.getPosition()) {
            case 0:
                this.pChangeTabCounterText(cText.pIntToStringStr(cPickorder.currentPickOrder.pGetNotHandledShipmentsObl().size()) + "/" + cText.pIntToStringStr(cPickorder.currentPickOrder.shipmentObl().size()));
                break;
            case 1:
                this.pChangeTabCounterText(cText.pIntToStringStr(cPickorder.currentPickOrder.pGetHandledShipmentsObl().size()) + "/" + cText.pIntToStringStr(cPickorder.currentPickOrder.shipmentObl().size()));
                break;
            case 2:
                this.pChangeTabCounterText(cText.pIntToStringStr(cPickorder.currentPickOrder.shipmentObl().size()));
                break;
            default:

        }
    }

    private void mShowCommentsFragment(List<cComment> pvDataObl, String pvTitleStr) {

        cUserInterface.pCheckAndCloseOpenDialogs();

        Bundle bundle = new Bundle();
        bundle.putString(cPublicDefinitions.KEY_COMMENTHEADER, pvTitleStr);

        CommentFragment commentFragment = new CommentFragment(pvDataObl);
        commentFragment.setArguments(bundle);

        commentFragment.show(cAppExtension.fragmentManager , cPublicDefinitions.COMMENTFRAGMENT_TAG);
        cUserInterface.pPlaySound(R.raw.message, 0);
    }

    private  void mShowComments(){

        if (cPickorder.currentPickOrder.pShipCommentObl() == null || cPickorder.currentPickOrder.pShipCommentObl().size() == 0) {
            this.imageButtonComments.setVisibility(View.INVISIBLE);
            return;
        }

        this.imageButtonComments.setVisibility(View.VISIBLE);

        //We already showed the comments
        if (cComment.commentsShownBln) {
            return;
        }

        this.mShowCommentsFragment(cPickorder.currentPickOrder.pPickCommentObl(),"");
        cComment.commentsShownBln = true;
    }

    private  void mStepFailed(String pvErrorMessageStr){
        cUserInterface.pDoExplodingScreen(pvErrorMessageStr, cPickorder.currentPickOrder.getOrderNumberStr(), true, true );
        cPickorder.currentPickOrder.pLockReleaseViaWebserviceBln(cWarehouseorder.StepCodeEnu.Pick_Picking,cWarehouseorder.WorkflowPickStepEnu.PickPicking);
        cUserInterface.pCheckAndCloseOpenDialogs();
    }

    private  void mShowWorkplaceFragment() {
        WorkplaceFragment workplaceFragment = new WorkplaceFragment();
        workplaceFragment.setCancelable(false);
        workplaceFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.WORKPLACEFRAGMENT_TAG);
    }

    private void mTryToLeaveActivity(){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(cAppExtension.context);
        alertDialog.setTitle(R.string.message_sure_leave_screen_title);
        alertDialog.setMessage(getString(R.string.message_sure_leave_screen_text));
        alertDialog.setPositiveButton(R.string.message_sure_leave_screen_positive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface pvDialogInterface, int i) {

                cPickorder.currentPickOrder.pLockReleaseViaWebserviceBln(cWarehouseorder.StepCodeEnu.Pick_Picking,cWarehouseorder.WorkflowPickStepEnu.PickPicking);

                //If activity bin is not required, then don't show the fragment
                if (!cPickorder.currentPickOrder.isPickActivityBinRequiredBln() || cPickorder.currentPickOrder.pQuantityHandledDbl() == 0 || !cPickorder.currentPickOrder.getCurrentLocationStr().isEmpty()) {
                    mStartOrderSelectActivity();
                }

            }
        });


        alertDialog.setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //do nothing (close the dialog)
            }
        });

        alertDialog.setCancelable(true);
        alertDialog.show();
    }

    private  boolean mTryToCloseOrderBln(){

        cResult hulpResult;
        hulpResult = new cResult();
        hulpResult.resultBln = false;

        hulpResult = cPickorder.currentPickOrder.pShipHandledViaWebserviceRst();

        //Everything was fine, so we are done
        if (hulpResult.resultBln) {
            return true;
        }

        //Something went wrong, but no further actions are needed, so ony show reason of failure
        if (hulpResult.activityActionEnu == cWarehouseorder.ActivityActionEnu.Unknown ) {
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

    private  void mStartOrderSelectActivity() {
        Intent intent = new Intent(cAppExtension.context, ShiporderSelectActivity.class);
        cAppExtension.activity.startActivity(intent);
    }

    private void mStartShipActivity(){
        //we have a SourceDocument to handle, so start Ship activity
        Intent intent = new Intent(cAppExtension.context, ShiporderShipActivity.class);
        cAppExtension.activity.startActivity(intent);
    }

    private cResult mCheckShipmentRst(){

        cResult result = new cResult();
        result.resultBln = true;

        if (cShipment.currentShipment == null) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.activity.getString(R.string.message_current_shipment_unknown));
            return  result;
        }

        if (cShipment.currentShipment.shippingAgent() == null) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.activity.getString(R.string.message_shipping_agent_unkown_or_empty));
            return  result;
        }

        if (cShipment.currentShipment.shippingAgentService() == null) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.activity.getString(R.string.message_shipping_agentservice_unkown_or_empty));
            return  result;
        }

        if (cShipment.currentShipment.shippingAgentService().shippingUnitsObl() == null || cShipment.currentShipment.shippingAgentService().shippingUnitsObl().size() == 0 ) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.activity.getString(R.string.message_shipping_agentservice_shippingingunits_unkown_or_empty));
            return  result;
        }

        return  result;


    }

}
