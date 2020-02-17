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
    public static final String VIEW_CHOSEN_ORDER = "detail:header:text";
    //End Region Public Properties

    //Region Private Properties

    //Region Views

    private static TextView textViewChosenOrder;
    private static TextView quantityShipordersText;
    private static ConstraintLayout container;
    private static TabLayout shiporderLinesTabLayout;
    private static ViewPager shiporderLinesViewPager;
    private static ImageView imageButtonComments;
    private static ShiporderLinesPagerAdapter shiporderLinesPagerAdapter;

    private static ImageView toolbarImage;
    private static TextView toolbarTitle;

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
        ShiporderLinesActivity.container = findViewById(R.id.container);
        ShiporderLinesActivity.toolbarImage = findViewById(R.id.toolbarImage);
        ShiporderLinesActivity.toolbarTitle = findViewById(R.id.toolbarTitle);
        ShiporderLinesActivity.quantityShipordersText = findViewById(R.id.quantityShipordersText);
        ShiporderLinesActivity.shiporderLinesTabLayout = findViewById(R.id.shiporderLinesTabLayout);
        ShiporderLinesActivity.shiporderLinesViewPager = findViewById(R.id.shiporderLinesViewpager);
        ShiporderLinesActivity.textViewChosenOrder = findViewById(R.id.textViewChosenOrder);
        ShiporderLinesActivity.imageButtonComments = findViewById(R.id.imageButtonComments);
    }

     @Override
    public void mSetToolbar(String pvScreenTitle) {
         ShiporderLinesActivity.toolbarImage.setImageResource(R.drawable.ic_menu_ship);
         ShiporderLinesActivity.toolbarTitle.setText(pvScreenTitle);
         ShiporderLinesActivity.toolbarTitle.setSelected(true);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void mFieldsInitialize() {

        ViewCompat.setTransitionName(ShiporderLinesActivity.textViewChosenOrder, ShiporderLinesActivity.VIEW_CHOSEN_ORDER);

        ShiporderLinesActivity.textViewChosenOrder.setText(cPickorder.currentPickOrder.getOrderNumberStr());
        ShiporderLinesActivity.shiporderLinesTabLayout.addTab(shiporderLinesTabLayout.newTab().setText(R.string.tab_shiporderline_toship));
        ShiporderLinesActivity.shiporderLinesTabLayout.addTab(shiporderLinesTabLayout.newTab().setText(R.string.tab_shiporderline_shipped));
        ShiporderLinesActivity.shiporderLinesTabLayout.addTab(shiporderLinesTabLayout.newTab().setText(R.string.tab_shiporderline_total));

        ShiporderLinesActivity.shiporderLinesPagerAdapter = new ShiporderLinesPagerAdapter(ShiporderLinesActivity.shiporderLinesTabLayout.getTabCount());
        ShiporderLinesActivity.shiporderLinesViewPager.setAdapter(ShiporderLinesActivity.shiporderLinesPagerAdapter);
        ShiporderLinesActivity.shiporderLinesViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(shiporderLinesTabLayout));
        ShiporderLinesActivity.shiporderLinesTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab pvTab) {
                ShiporderLinesActivity.shiporderLinesViewPager.setCurrentItem(pvTab.getPosition());
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

        ShiporderLinesActivity.mShowComments();

        //Check if we need to register a workplaceStr
        if (cWorkplace.currentWorkplace == null ) {


            if (cWorkplace.allWorkplacesObl.size() > 1) {
                //Show the workplaceStr fragment
                ShiporderLinesActivity.mShowWorkplaceFragment();
                return;
            }
            else {
                //Pop-up is not needed, we just select the only workplace there is
                cWorkplace.currentWorkplace = cWorkplace.allWorkplacesObl.get(0);
                ShiporderLinesActivity.pWorkplaceSelected();
                return;
            }


        }

        //Call this here, because this is called everytime the activiy gets shown
        this.mCheckAllDone();



    }

    //End Region iICSDefaultActivity defaults


    //Region Public Methods

    public static void pChangeTabCounterText(String pvTextStr){
        ShiporderLinesActivity.quantityShipordersText.setText(pvTextStr);

    }

    public static void pHandleScan(cBarcodeScan pvBarcodeScan, Boolean pvSourceNoSelectedBln) {

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
            ShiporderLinesActivity.mStartShipActivity();
            return;
        }

        //Check if we have scanned an ARTICLE and check if there are not handled linesInt for this ARTICLE
        if (cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(),cBarcodeLayout.barcodeLayoutEnu.ARTICLE)) {

            //Check if article scan is allowed
            if (!cPickorder.currentPickOrder.isSingleArticleOrdersBln()) {
                mStepFailed(cAppExtension.context.getString(R.string.message_article_not_allowed));
                return;
            }

            //Get shipment with scanned barcodeStr
            cShipment.currentShipment = cShipment.pGetShipmentWithScannedArticleBarcode(pvBarcodeScan);

            // We did not find a match, so stop
            if (cShipment.currentShipment == null) {
                mStepFailed(cAppExtension.context.getString(R.string.message_unknown_barcode));
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
            ShiporderLinesActivity.mStartShipActivity();
            return;

        }

        //Get shipment by SourceNo or pickcartbox
        cShipment.currentShipment = cShipment.pGetShipmentWithScannedBarcode(pvBarcodeScan);

        // We did not find a match, so stop
        if (cShipment.currentShipment == null) {
            mStepFailed(cAppExtension.context.getString(R.string.message_unknown_barcode));
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
        ShiporderLinesActivity.mStartShipActivity();
    }

    public static void pShipmentSelected(cShipment pvShipment) {
        cShipment.currentShipment = pvShipment;
        ShiporderLinesToShipFragment.pSetChosenShipment();
    }

    public static void pShippingDone() {

        //Try to close
        if (!ShiporderLinesActivity.mTryToCloseOrderBln()){
            return;
        }

        //Go back to order select activity
        ShiporderLinesActivity.mStartOrderSelectActivity();

    }

    public static void pShowOrderDoneFragment() {

        cUserInterface.pPlaySound(R.raw.goodsound, null);

        final OrderDoneFragment orderDoneFragment = new OrderDoneFragment(false);
        orderDoneFragment.setCancelable(false);
        orderDoneFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.ORDERDONE_TAG);
    }

    public static void pWorkplaceSelected(){


        if (!cPickorder.currentPickOrder.pUpdateWorkplaceViaWebserviceBln(cWorkplace.currentWorkplace.getWorkplaceStr())) {
            cUserInterface.pDoExplodingScreen(cAppExtension.activity.getString(R.string.message_workplace_not_updated),"",true,true);
            return;
        }

        //Register barcodeStr receiver, because the workplaceStr fragment has been shown
        cBarcodeScan.pRegisterBarcodeReceiver();

        cUserInterface.pShowSnackbarMessage(ShiporderLinesActivity.container,cAppExtension.activity.getString(R.string.message_workplace_selected) + ' ' + cWorkplace.currentWorkplace.getWorkplaceStr() ,R.raw.headsupsound,false);
    }


    //End Region Public Methods

    private void mCheckAllDone() {

        // If not everything is done, then leave
        if (!this.mCheckAllDoneBln()) {
            return;
        }

        // Show order done fragment
        ShiporderLinesActivity.pShowOrderDoneFragment();

    }

    private boolean mCheckAllDoneBln() {

        return cPickorder.currentPickOrder.pGetNotHandledShipmentsObl().size() <= 0;
    }

    private void mChangeSelectedTab(TabLayout.Tab pvTab) {

        switch (pvTab.getPosition()) {
            case 0:
                ShiporderLinesActivity.pChangeTabCounterText(cText.pIntToStringStr(cPickorder.currentPickOrder.pGetNotHandledShipmentsObl().size()) + "/" + cText.pIntToStringStr(cPickorder.currentPickOrder.shipmentObl().size()));
                break;
            case 1:
                ShiporderLinesActivity.pChangeTabCounterText(cText.pIntToStringStr(cPickorder.currentPickOrder.pGetHandledShipmentsObl().size()) + "/" + cText.pIntToStringStr(cPickorder.currentPickOrder.shipmentObl().size()));
                break;
            case 2:
                ShiporderLinesActivity.pChangeTabCounterText(cText.pIntToStringStr(cPickorder.currentPickOrder.shipmentObl().size()));
                break;
            default:

        }
    }

    private static void mShowCommentsFragment(List<cComment> pvDataObl, String pvTitleStr) {

        cUserInterface.pCheckAndCloseOpenDialogs();

        Bundle bundle = new Bundle();
        bundle.putString(cPublicDefinitions.KEY_COMMENTHEADER, pvTitleStr);

        CommentFragment commentFragment = new CommentFragment(pvDataObl);
        commentFragment.setArguments(bundle);

        commentFragment.show(cAppExtension.fragmentManager , cPublicDefinitions.COMMENTFRAGMENT_TAG);
        cUserInterface.pPlaySound(R.raw.message, 0);
    }

    private static void mShowComments(){

        if (cPickorder.currentPickOrder.pShipCommentObl() == null || cPickorder.currentPickOrder.pShipCommentObl().size() == 0) {
            ShiporderLinesActivity.imageButtonComments.setVisibility(View.INVISIBLE);
            return;
        }

        ShiporderLinesActivity.imageButtonComments.setVisibility(View.VISIBLE);

        //We already showed the comments
        if (cComment.commentsShownBln) {
            return;
        }

        ShiporderLinesActivity.mShowCommentsFragment(cPickorder.currentPickOrder.pPickCommentObl(),"");
        cComment.commentsShownBln = true;
    }

    private static void mStepFailed(String pvErrorMessageStr){
        cUserInterface.pDoExplodingScreen(pvErrorMessageStr, cPickorder.currentPickOrder.getOrderNumberStr(), true, true );
        cPickorder.currentPickOrder.pLockReleaseViaWebserviceBln(cWarehouseorder.StepCodeEnu.Pick_Picking,cWarehouseorder.WorkflowPickStepEnu.PickPicking);
        cUserInterface.pCheckAndCloseOpenDialogs();
    }

    private static void mShowWorkplaceFragment() {
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

    private static boolean mTryToCloseOrderBln(){

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
                ShiporderLinesActivity.mShowCommentsFragment(cPickorder.currentPickOrder.pFeedbackCommentObl(), hulpResult.messagesStr());
            }

            return  false;
        }

        return true;

    }

    private static void mStartOrderSelectActivity() {
        Intent intent = new Intent(cAppExtension.context, ShiporderSelectActivity.class);
        cAppExtension.activity.startActivity(intent);
    }

    private static void mStartShipActivity(){
        //we have a SourceDocument to handle, so start Ship activity
        Intent intent = new Intent(cAppExtension.context, ShiporderShipActivity.class);
        cAppExtension.activity.startActivity(intent);
    }

    private static cResult mCheckShipmentRst(){

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
