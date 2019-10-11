package nl.icsvertex.scansuite.Activities.ship;

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
import androidx.cardview.widget.CardView;
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
import SSU_WHS.Basics.BarcodeLayouts.cBarcodeLayout;
import SSU_WHS.Basics.Workplaces.cWorkplace;

import SSU_WHS.General.Comments.cComment;
import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.General.cPublicDefinitions;


import SSU_WHS.Picken.Pickorders.cPickorder;

import SSU_WHS.Picken.Shipment.cShipment;
import nl.icsvertex.scansuite.Fragments.dialogs.OrderDoneFragment;
import nl.icsvertex.scansuite.R;

import nl.icsvertex.scansuite.PagerAdapters.ShiporderLinesPagerAdapter;
import ICS.cAppExtension;
import nl.icsvertex.scansuite.Fragments.dialogs.CommentFragment;
import nl.icsvertex.scansuite.Fragments.dialogs.WorkplaceFragment;

//import android.app.Fragment;

public class ShiporderLinesActivity extends AppCompatActivity implements iICSDefaultActivity {

    //Region Public Properties
    public static final String VIEW_CHOSEN_ORDER = "detail:header:text";
     //End Region Public Properties

    //Region Private Properties

    //Region Views

    private TextView textViewChosenOrder;
    private static TextView quantityShipordersText;
    private static ConstraintLayout container;
    private TabLayout shiporderLinesTabLayout;
    private ViewPager shiporderLinesViewPager;
    private CardView chosenOrderContainer;
    private static ImageView imageButtonComments;
    private ShiporderLinesPagerAdapter shiporderLinesPagerAdapter;

    private ImageView toolbarImage;
    private TextView toolbarTitle;
    private ImageView toolbarImageHelp;

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
        this.mSetViewModels();
        this.mSetSettings();
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
        this.toolbarImage = findViewById(R.id.toolbarImage);
        this.toolbarTitle = findViewById(R.id.toolbarTitle);
        this.toolbarImageHelp = findViewById(R.id.toolbarImageHelp);
        this.quantityShipordersText = findViewById(R.id.quantityShipordersText);
        this.shiporderLinesTabLayout = findViewById(R.id.shiporderLinesTabLayout);
        this.shiporderLinesViewPager = findViewById(R.id.shiporderLinesViewpager);
        this.textViewChosenOrder = findViewById(R.id.textViewChosenOrder);
        this.chosenOrderContainer = findViewById(R.id.chosenOrderContainer);
        this.imageButtonComments = findViewById(R.id.imageButtonComments);
    }

    @Override
    public void mSetViewModels() {

    }

    @Override
    public void mSetSettings() {

    }

    @Override
    public void mSetToolbar(String pvScreenTitle) {
        this.toolbarImage.setImageResource(R.drawable.ic_menu_ship);
        this.toolbarTitle.setText(pvScreenTitle);
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

        this.textViewChosenOrder.setText(cPickorder.currentPickOrder.getOrderNumberStr());
        this.shiporderLinesTabLayout.addTab(shiporderLinesTabLayout.newTab().setText(R.string.tab_shiporderline_toship));
        this.shiporderLinesTabLayout.addTab(shiporderLinesTabLayout.newTab().setText(R.string.tab_shiporderline_shipped));
        this.shiporderLinesTabLayout.addTab(shiporderLinesTabLayout.newTab().setText(R.string.tab_shiporderline_total));

        this.shiporderLinesPagerAdapter = new ShiporderLinesPagerAdapter(this.shiporderLinesTabLayout.getTabCount());
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

        //Call this here, because this is called everytime the activiy gets shown
        this.mCheckAllDone();

        //Check if we already have a workplace
        if (cWorkplace.currentWorkplace != null) {
            return;
        }

        //Show the workplace fragment
       this.mShowWorkplaceFragment();

    }

    //End Region iICSDefaultActivity defaults


    //Region Public Methods

    public static void pChangeTabCounterText(String pvTextStr){
        ShiporderLinesActivity.quantityShipordersText.setText(pvTextStr);

    }

    public static void pHandleScan(String pvScannedBarcodeStr, Boolean pvSourceNoSelectedBln) {

        cUserInterface.pCheckAndCloseOpenDialogs();

        //SourceNo button has been pressed, so we already have a current line
        if (pvSourceNoSelectedBln) {

            //we have a SourceDocument to handle, so start Pick activity
            ShiporderLinesActivity.mStartOrderSelectActivity();
            return;
        }

        //Check if we have scanned an ARTICLE and check if there are not handled lines for this ARTICLE
        if (cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvScannedBarcodeStr,cBarcodeLayout.barcodeLayoutEnu.ARTICLE)) {

            //Check if article scan is allowed
            if (!cPickorder.currentPickOrder.isSingleArticleOrdersBln()) {
                mStepFailed(cAppExtension.context.getString(R.string.message_article_not_allowed));
                return;
            }

            //Get shipment with scanned barcode
            cShipment.currentShipment = cShipment.pGetShipmentWithScannedArticleBarcode(pvScannedBarcodeStr);

            // We did not find a match, so stop
            if (cShipment.currentShipment == null) {
                mStepFailed(cAppExtension.context.getString(R.string.message_unknown_barcode));
                return;
            }
        }

        //Get shipment by SourceNo or pickcartbox
        cShipment.currentShipment = cShipment.pGetShipmentWithScannedBarcode(pvScannedBarcodeStr);

        // We did not find a match, so stop
        if (cShipment.currentShipment == null) {
            mStepFailed(cAppExtension.context.getString(R.string.message_unknown_barcode));
            return;
        }

        //We found a match in open shipments
        ShiporderLinesActivity.mStartOrderSelectActivity();
        return;

    }

    public static void pShippingDone() {

        //Try to close
      if (!ShiporderLinesActivity.mTryToCloseOrderBln()){
          return;
      }

      //Go back to order select activity
       ShiporderLinesActivity.mStartOrderSelectActivity();

    }

    //End Region Public Methods

    private void mCheckAllDone() {

        // If not everything is done, then leave
        if (this.mCheckAllDoneBln() == false) {
            return;
        }

        // Show order done fragment
        ShiporderLinesActivity.mShowOrderDoneFragment();


    }

    private boolean mCheckAllDoneBln() {

        if (cPickorder.currentPickOrder.pGetNotHandledShipmentsObl().size() <= 0) {
            return true;
        }
        return  false;
    }

    private void mChangeSelectedTab(TabLayout.Tab pvTab) {

        switch (pvTab.getPosition()) {
            case 0:
                ShiporderLinesActivity.pChangeTabCounterText(cText.intToString(cPickorder.currentPickOrder.pGetNotHandledShipmentsObl().size()) + "/" + cText.intToString(cPickorder.currentPickOrder.shipmentObl().size()));
                break;
            case 1:
                ShiporderLinesActivity.pChangeTabCounterText(cText.intToString(cPickorder.currentPickOrder.pGetHandledShipmentsObl().size()) + "/" + cText.intToString(cPickorder.currentPickOrder.shipmentObl().size()));
                break;
            case 2:
                ShiporderLinesActivity.pChangeTabCounterText(cText.intToString(cPickorder.currentPickOrder.shipmentObl().size()));
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

    private static void mShowOrderDoneFragment() {

        cUserInterface.pPlaySound(R.raw.goodsound, null);

        final OrderDoneFragment orderDoneFragment = new OrderDoneFragment(false);
        orderDoneFragment.setCancelable(false);
        orderDoneFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.ORDERDONE_TAG);
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
                if (!cPickorder.currentPickOrder.pickActivityBinRequiredBln || cPickorder.currentPickOrder.pQuantityHandledDbl() == 0 || !cPickorder.currentPickOrder.getCurrentLocationStr().isEmpty()) {
                    mStartOrderSelectActivity();
                    return;
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
        if (!hulpResult.resultBln && hulpResult.activityActionEnu == cWarehouseorder.ActivityActionEnu.Unknown ) {
            cUserInterface.pDoExplodingScreen(hulpResult.messagesStr(),"",true,true);
            return  false;
        }

        //Something went wrong, the order has been deleted, so show comments and refresh
        if (!hulpResult.resultBln && hulpResult.activityActionEnu == cWarehouseorder.ActivityActionEnu.Hold ) {

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

    //todo: put this back
//    private static void mStartShipActivity(){
//        //we have a SourceDocument to handle, so start Ship activity
//        Intent intent = new Intent(cAppExtension.context, ShiporderShipActivity.class);
//        cAppExtension.activity.startActivity(intent);
//    }

}
