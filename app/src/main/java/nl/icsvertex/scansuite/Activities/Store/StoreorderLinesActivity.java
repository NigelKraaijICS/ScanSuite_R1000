package nl.icsvertex.scansuite.Activities.Store;

import android.app.AlertDialog;
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
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.List;

import ICS.Interfaces.iICSDefaultActivity;
import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cResult;
import ICS.Utils.cText;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Basics.BranchBin.cBranchBin;
import SSU_WHS.Basics.Settings.cSetting;
import SSU_WHS.Basics.Users.cUser;
import SSU_WHS.Basics.Workplaces.cWorkplace;
import SSU_WHS.General.Comments.cComment;
import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.General.cPublicDefinitions;
import SSU_WHS.Picken.Pickorders.cPickorder;
import SSU_WHS.Picken.Storement.cStorement;
import nl.icsvertex.scansuite.Activities.Ship.ShiporderLinesActivity;
import nl.icsvertex.scansuite.Activities.Sort.SortorderLinesActivity;
import nl.icsvertex.scansuite.Fragments.Dialogs.CommentFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.SetBinFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.StepDoneFragment;
import nl.icsvertex.scansuite.PagerAdapters.StoreorderLinesPagerAdapter;
import nl.icsvertex.scansuite.R;


//import android.app.Fragment;

public class StoreorderLinesActivity extends AppCompatActivity implements iICSDefaultActivity {

    //Region Public Properties
    public static Fragment currentLineFragment;
    //End Region Public Properties

    //Region Private Properties
    //Region Views

    private TextView textViewChosenOrder;
    private TextView storeOrdersText;
    private TabLayout storeorderLinesTabLayout;
    private ViewPager storeorderLinesViewPager;
    private ImageView imageButtonComments;

    private Toolbar toolbar;
    private  ImageView toolbarImage;
    private  TextView toolbarTitle;

    //End Region Views
    //End Region Private Properties

    //Region Default Methods

    @Override
    protected void onCreate(Bundle pvSavedInstanceState) {
        super.onCreate(pvSavedInstanceState);
        setContentView(R.layout.activity_storeorderlines);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.mActivityInitialize();
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
    }

    //End Region Default Methods

    //Region iICSDefaultActivity defaults

    @Override
    public void mActivityInitialize() {

        this.mSetAppExtensions();

        this.mFindViews();

        this.mSetToolbar(getResources().getString(R.string.screentitle_storeorderlines));

        this.mFieldsInitialize();

        this.mSetListeners();

        this.mInitScreen();

        cBarcodeScan.pRegisterBarcodeReceiver(this.getClass().getSimpleName());

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

        this.toolbar = findViewById(R.id.toolbar);
        this.toolbarImage = findViewById(R.id.toolbarImage);
        this.toolbarTitle = findViewById(R.id.toolbarTitle);

        this.storeOrdersText = findViewById(R.id.storeOrdersText);
        this.storeorderLinesTabLayout = findViewById(R.id.storeorderLinesTabLayout);
        this.storeorderLinesViewPager = findViewById(R.id.storeorderLinesViewPager);
        this.textViewChosenOrder = findViewById(R.id.textViewChosenOrder);
        this.imageButtonComments = findViewById(R.id.imageButtonComments);
    }

    @Override
    public void mSetToolbar(String pvScreenTitleStr) {

        this.toolbarImage.setImageResource(R.drawable.ic_menu_intake_om);
        this.toolbarTitle.setText(pvScreenTitleStr);
        this.toolbarTitle.setSelected(true);

        ViewCompat.setTransitionName(this.textViewChosenOrder, cPublicDefinitions.VIEW_CHOSEN_ORDER);
        setSupportActionBar(this.toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void mFieldsInitialize() {

        this.textViewChosenOrder.setText(cPickorder.currentPickOrder.getOrderNumberStr());

        if (this.storeorderLinesTabLayout.getTabCount() == 0) {
            this.storeorderLinesTabLayout.addTab(storeorderLinesTabLayout.newTab().setText(R.string.tab_sortorderline_tostore));
            this.storeorderLinesTabLayout.addTab(storeorderLinesTabLayout.newTab().setText(R.string.tab_sortorderline_stored));
            this.storeorderLinesTabLayout.addTab(storeorderLinesTabLayout.newTab().setText(R.string.tab_pickorderline_total));
        }

        StoreorderLinesPagerAdapter storeorderLinesPagerAdapter = new StoreorderLinesPagerAdapter(this.storeorderLinesTabLayout.getTabCount());
        this.storeorderLinesViewPager.setAdapter(storeorderLinesPagerAdapter);
        this.storeorderLinesViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(this.storeorderLinesTabLayout));
        this.storeorderLinesTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab pvTab) {
                storeorderLinesViewPager.setCurrentItem(pvTab.getPosition());
                mSetTabselected(pvTab);
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
        this.mSetShowCommentListener();
    }

    @Override
    public void mInitScreen() {
        //Show comments first
        this.mShowComments();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem pvMenuItem) {
        if (pvMenuItem.getItemId() == android.R.id.home) {
            this.mTryToLeaveActivity();
            return true;
        }

        return super.onOptionsItemSelected(pvMenuItem);
    }

    @Override
    public void onBackPressed() {
        this.mTryToLeaveActivity();
    }

    //End Region iICSDefaultActivity defaults

    //Region Public Methods

    public  void pStorementSelected(cStorement pvStorement) {
        cStorement.currentStorement = pvStorement;

    }

    public  void pStorementStored(String pvBinCodeStr) {


        cBranchBin  branchBin = cUser.currentUser.currentBranch.pGetBinByCode(pvBinCodeStr);
        if (branchBin == null) {
            cUserInterface.pDoExplodingScreen(cAppExtension.activity.getString(R.string.message_bin_unknown),"", true, true);
            return;
        }

        if (!branchBin.isUseForStorageBln()) {
            cUserInterface.pDoExplodingScreen(cAppExtension.activity.getString(R.string.message_bin_cant_be_used_for_storage),"", true, true);
            return;
        }

        cStorement.currentStorement.binCodeStr = pvBinCodeStr;

        cResult result = cStorement.currentStorement.pStorementDoneRst();
        if (!result.resultBln) {
            cUserInterface.pDoExplodingScreen(result.messagesStr(),"",true,true);
            return;
        }

        this.mFieldsInitialize();

    }

    public  void pChangeTabCounterText(String pvTextStr){
        this.storeOrdersText.setText(pvTextStr);
    }

    public  void pCloseStoreAndDecideNextStep(){
        // Show that we are getting data
        cUserInterface.pShowGettingData();

        new Thread(this::mHandleStoreFaseHandledAndDecideNextStep).start();
    }

    public void pHandleScan(cBarcodeScan pvBarcodeScan,Boolean pvStorementSelectedBln) {

        cUserInterface.pCheckAndCloseOpenDialogs();

        //BIN button has been pressed, so we already have a current line
        if (pvStorementSelectedBln) {
            this.mShowSetBinFragment();
            return;
        }

        cStorement.currentStorement = cStorement.pGetStorementWithScannedBarcode(pvBarcodeScan);
        if (cStorement.currentStorement== null) {
            cUserInterface.pDoExplodingScreen(cAppExtension.context.getString(R.string.error_unknow_storement), "", true, true);
            return;
        }

        this.mShowSetBinFragment();

    }

    public void pStoringDone() {

        this.mShowOrderDoneFragment();


    }

    //End Region Public Methods

    //Region Private Methods

    //Region Fragments and Activities
    private void mShowOrderDoneFragment() {

        cUserInterface.pPlaySound(R.raw.goodsound, null);

        final StepDoneFragment stepDoneFragment = new StepDoneFragment(cAppExtension.activity.getString(R.string.message_store_done), cAppExtension.activity.getString(R.string.message_close_store_fase),false);
        stepDoneFragment.setCancelable(false);
        stepDoneFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.ORDERDONE_TAG);
    }

    private void mShowComments(){

        if (cPickorder.currentPickOrder.pSortCommentObl() == null || cPickorder.currentPickOrder.pSortCommentObl().size() == 0) {
            this.imageButtonComments.setVisibility(View.INVISIBLE);
            return;
        }

        this.imageButtonComments.setVisibility(View.VISIBLE);

        //We already showed the comments
        if (cComment.commentsShownBln) {
            return;
        }

        this.mShowCommentsFragment(cPickorder.currentPickOrder.pSortCommentObl(),"");
        cComment.commentsShownBln = true;
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

    private void mTryToLeaveActivity(){

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(cAppExtension.context);
        alertDialog.setTitle(R.string.message_sure_leave_screen_title);
        alertDialog.setMessage(getString(R.string.message_sure_leave_screen_text));
        alertDialog.setCancelable(true);

        //If we don't want to leave then we are done
        alertDialog.setNeutralButton(R.string.cancel, (dialogInterface, i) -> {

        });


        //If we want to leave, check if we still need to send progress
        alertDialog.setPositiveButton(R.string.message_sure_leave_screen_positive, (pvDialogInterface, i) -> {
                cPickorder.currentPickOrder.pLockReleaseViaWebserviceBln(cWarehouseorder.StepCodeEnu.Pick_Storage, cWarehouseorder.WorkflowPickStepEnu.PickStorage);
                mStartOrderSelectActivity();

        });

        alertDialog.show();

    }

    private void mSetTabselected(TabLayout.Tab pvTab) {

        switch (pvTab.getPosition()) {
            case 0:
                this.pChangeTabCounterText(cPickorder.currentPickOrder.pGetNotHandledStorementsObl().size() + "/" + cPickorder.currentPickOrder.storementObl().size());
                break;
            case 1:
                this.pChangeTabCounterText(cPickorder.currentPickOrder.pGetHandledStorementsObl().size() + "/" + cPickorder.currentPickOrder.storementObl().size());
                break;
            case 2:
                this.pChangeTabCounterText(cText.pIntToStringStr(cPickorder.currentPickOrder.storementObl().size()));
                break;
            default:

        }
    }

    private  void mHandleStoreFaseHandledAndDecideNextStep(){

        if (!this.mStoreFaseHandledBln()) {
            return;
        }

        if (cPickorder.currentPickOrder.isSortableBln()) {
            this.mSortNextStep();
            return;
        }

        //If Pack or Ship is not required, then we are dibe
        if (cPickorder.currentPickOrder.isPackAndShipNeededBln()) {
            this.mPackAndShipNextStap();
            return;
        }

        this.mStartOrderSelectActivity();

    }

    private  void mStartOrderSelectActivity() {
        Intent intent = new Intent(cAppExtension.context, StoreorderSelectActivity.class);
        startActivity(intent);
        finish();
    }

    //End Region Fragments and Activities

    private void mSetShowCommentListener() {
        this.imageButtonComments.setOnClickListener(view -> mShowCommentsFragment(cPickorder.currentPickOrder.pStoreCommentObl(),""));
    }

    private  boolean mStoreFaseHandledBln(){

        cResult hulpResult;
        hulpResult = new cResult();
        hulpResult.resultBln = false;

        hulpResult = cPickorder.currentPickOrder.pStoreFaseHandledViaWebserviceRst();

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

    private void mShowSetBinFragment(){

        SetBinFragment setBinFragment = new SetBinFragment();
        setBinFragment.setCancelable(true);
        setBinFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.SETBIN_TAG);

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

    private  void mAskSort() {

        cAppExtension.activity.runOnUiThread(() -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(cAppExtension.context);

            builder.setMessage(cAppExtension.context.getString(R.string.question_open_sort));
            builder.setTitle(cAppExtension.context.getString(R.string.question_open_sort_title));
            builder.setPositiveButton(R.string.open, (dialog, id) -> mStartSortActivity());
            builder.setNegativeButton(R.string.no, (dialogInterface, i) -> mStartOrderSelectActivity());
            builder.setIcon(R.drawable.ic_menu_sort);
            builder.show();
        });
    }

    private void mPackAndShipNextStap(){

        //If activity bin is not required, then don't show the fragment
        if ( cPickorder.currentPickOrder.pQuantityHandledDbl() == 0 ) {
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

    private  void mAskShip() {

        cAppExtension.activity.runOnUiThread(() -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(cAppExtension.context);

            builder.setMessage(cAppExtension.context.getString(R.string.question_open_ship));
            builder.setTitle(cAppExtension.context.getString(R.string.question_open_ship_title));
            builder.setPositiveButton(R.string.open, (dialog, id) -> mStartShipActivity());
            builder.setNegativeButton(R.string.no, (dialogInterface, i) -> mStartOrderSelectActivity());
            builder.setIcon(R.drawable.ic_menu_ship);
            builder.show();
        });
    }

    private  void mStartSortActivity() {

        cUserInterface.pShowGettingData();

        new Thread(this::mHandleStartSortActivity).start();

    }

    private  void mHandleStartSortActivity(){

        //Clear workplaceStr, so you have to select it in the next step
        cWorkplace.currentWorkplace = null;

        //Try to lock the pickorder
        if (!cPickorder.currentPickOrder.pLockViaWebserviceRst(cWarehouseorder.StepCodeEnu.Pick_Storage, cWarehouseorder.WorkflowPickStepEnu.PickSorting).resultBln) {
            this.mStepFailed(cAppExtension.context.getString(R.string.error_couldnt_lock_order),cWarehouseorder.StepCodeEnu.Pick_Picking);
            return;
        }

        //Get sort linesInt
        if (!cPickorder.currentPickOrder.pGetLinesViaWebserviceBln(true,cWarehouseorder.PickOrderTypeEnu.SORT)) {
            this.mStepFailed(cAppExtension.context.getString(R.string.error_getting_sort_lines_failed),cWarehouseorder.StepCodeEnu.Pick_Picking);
            return;
        }

        if (!cPickorder.currentPickOrder.pGetPropertyLineDataViaWebserviceBln()) {
            this.mStepFailed(cAppExtension.context.getString(R.string.error_get_property_line_data_failed),cWarehouseorder.StepCodeEnu.Pick_Picking);
            return;
        }

        cAppExtension.activity.runOnUiThread(() -> {
            //Show Sort Activity
            Intent intent = new Intent(cAppExtension.context, SortorderLinesActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private  void mStartShipActivity() {

        cUserInterface.pShowGettingData();

        new Thread(this::mHandleStartShipActivity).start();

    }

    private  void mHandleStartShipActivity(){

        cResult hulpResult;

        if (!this.mTryToLockShipOrderBln()) {
            return;
        }

        hulpResult = cPickorder.currentPickOrder.pGetShipmentDetailsRst();
        if (!hulpResult.resultBln) {
            this.mStepFailed(hulpResult.messagesStr(),cWarehouseorder.StepCodeEnu.Pick_PackAndShip);
            return;
        }

        //Show ShipLines
        Intent intent = new Intent(cAppExtension.context, ShiporderLinesActivity.class);
        startActivity(intent);
        finish();

    }

    private  void mStepFailed(String pvErrorMessageStr, cWarehouseorder.StepCodeEnu pvStepCodeEnu){
        cUserInterface.pDoExplodingScreen(pvErrorMessageStr, cPickorder.currentPickOrder.getOrderNumberStr(), true, true );
        cPickorder.currentPickOrder.pLockReleaseViaWebserviceBln(pvStepCodeEnu, cWarehouseorder.WorkflowPickStepEnu.PickPackAndShip);
        cUserInterface.pCheckAndCloseOpenDialogs();
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
            this.mStepFailed(hulpResult.messagesStr(),cWarehouseorder.StepCodeEnu.Pick_PackAndShip);
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


    //End Region Private Methods


}
