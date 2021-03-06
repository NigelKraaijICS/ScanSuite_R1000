package nl.icsvertex.scansuite.Activities.IntakeAndReceive;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.core.util.Pair;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.crashlytics.FirebaseCrashlytics;

import java.util.ArrayList;
import java.util.List;

import ICS.Interfaces.iICSDefaultActivity;
import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cRegex;
import ICS.Utils.cResult;
import ICS.Utils.cSharedPreferences;
import ICS.Utils.cText;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Basics.BarcodeLayouts.cBarcodeLayout;
import SSU_WHS.Basics.Settings.cSetting;
import SSU_WHS.Basics.StockOwner.cStockOwner;
import SSU_WHS.Basics.Users.cUser;
import SSU_WHS.General.Licenses.cLicense;
import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.General.cPublicDefinitions;
import SSU_WHS.Intake.Intakeorders.cIntakeorder;
import SSU_WHS.Intake.Intakeorders.cIntakeorderAdapter;
import nl.icsvertex.scansuite.Activities.General.MenuActivity;
import nl.icsvertex.scansuite.Activities.Intake.CreateIntakeActivity;
import nl.icsvertex.scansuite.Activities.Intake.IntakeorderMASLinesActivity;
import nl.icsvertex.scansuite.Activities.Intake.IntakeorderMATLinesActivity;
import nl.icsvertex.scansuite.Activities.Receive.CreateReceiveActivity;
import nl.icsvertex.scansuite.Activities.Receive.ReceiveLinesActivity;
import nl.icsvertex.scansuite.Fragments.Dialogs.FilterOrderLinesFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.NoOrdersFragment;
import nl.icsvertex.scansuite.R;

public class IntakeAndReceiveSelectActivity extends AppCompatActivity implements iICSDefaultActivity, SwipeRefreshLayout.OnRefreshListener {

    //Region Public Properties

    //End Region Public Properties

    //Region Private Properties

    // Region Views
    private RecyclerView recyclerViewIntakeorders;
    private ImageView toolbarImage;
    private TextView toolbarTitle;
    private TextView toolbarSubTitle;
    private TextView toolbarSubTitle2;
    private SearchView recyclerSearchView;

    private ImageView imageViewFilter;
    private ImageView imageViewNewOrder;

    private ConstraintLayout constraintFilterOrders;
    private SwipeRefreshLayout swipeRefreshLayout;

    private BottomSheetBehavior bottomSheetBehavior;
    public static cWarehouseorder.ReceiveAndStoreMainTypeEnu currentMainTypeEnu;
    public static boolean startedViaMenuBln;

    cIntakeorderAdapter intakeorderAdapter;
    cIntakeorderAdapter getIntakeorderAdapter(){
        if (this.intakeorderAdapter == null) {
            this.intakeorderAdapter = new cIntakeorderAdapter();
        }
        return  this.intakeorderAdapter;

    }
    // End Region Views

    //End Region Private Properties

    //Region Default Methods

    @Override
    protected void onCreate(Bundle pvSavedInstanceState) {
        super.onCreate(pvSavedInstanceState);
        setContentView(R.layout.activity_intake_and_receive_orderselect);
        cBarcodeScan.pRegisterBarcodeReceiver(this.getClass().getSimpleName());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (cAppExtension.activity instanceof  IntakeAndReceiveSelectActivity){
            cBarcodeScan.pUnregisterBarcodeReceiver(this.getClass().getSimpleName());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        cBarcodeScan.pUnregisterBarcodeReceiver(this.getClass().getSimpleName());
    }

    @Override
    protected void onResume() {
        super.onResume();
        cUserInterface.pEnableScanner();
        this.mActivityInitialize();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            this.mLeaveActivity();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        if (this.bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED){
            this.bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            return;
        }
        this.mLeaveActivity();
    }

    @Override
    public void onRefresh() {
        this.pFillOrders();
    }


    //End Region Default Methods

    //Region iICSDefaultActivity defaults

    @Override
    public void mActivityInitialize() {

        this.mSetAppExtensions();

        this.mFindViews();

        this.mSetToolbar(getResources().getString(R.string.screentitle_intakeorderselect));

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
        this.toolbarImage = findViewById(R.id.toolbarImage);
        this.toolbarTitle = findViewById(R.id.toolbarTitle);
        this.toolbarSubTitle = findViewById(R.id.toolbarSubtext);
        this.toolbarSubTitle2 = findViewById(R.id.toolbarSubtext2);
        this.recyclerViewIntakeorders = findViewById(R.id.recyclerViewMoveorders);
        this.recyclerSearchView = findViewById(R.id.recyclerSearchView);
        this.imageViewFilter = findViewById(R.id.imageViewFilter);
        this.imageViewNewOrder = findViewById(R.id.imageViewNewOrder);
        this.constraintFilterOrders = findViewById(R.id.constraintFilterOrders);
        this.swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
    }

    @Override
    public void mSetToolbar(String pvScreenTitleStr) {

        if (cUser.currentUser.currentAuthorisation.getCustomAuthorisation() != null) {
            this.toolbarImage.setImageBitmap(cUser.currentUser.currentAuthorisation.customImageBmp());
            this.toolbarTitle.setText(cUser.currentUser.currentAuthorisation.getCustomAuthorisation().getDescriptionStr());
        }
        else {
            this.toolbarImage.setImageResource(R.drawable.ic_menu_intake);
            this.toolbarTitle.setText(pvScreenTitleStr);
        }

        this.toolbarSubTitle2.setText(cUser.currentUser.currentBranch.getBranchNameStr());
        this.toolbarTitle.setSelected(true);
        this.toolbarSubTitle.setSelected(true);

        ViewCompat.setTransitionName(toolbarImage, cPublicDefinitions.VIEW_NAME_HEADER_IMAGE);
        ViewCompat.setTransitionName(toolbarTitle, cPublicDefinitions.VIEW_NAME_HEADER_TEXT);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void mFieldsInitialize() {
        this.mInitBottomSheet();
        this.mResetCurrents();
        this.mSetNewOrderButton();
        this.pFillOrders();
    }

    @Override
    public void mSetListeners() {
        this.mSetRecyclerOnScrollListener();
        this.mSetSearchListener();
        this.mSetFilterListener();
        this.mSetSwipeRefreshListener();
        this.mSetNewOrderListener();
    }

    @Override
    public void mInitScreen() {

    }

    //End Region iICSDefaultActivity defaults

    //Region Public Methods


    public  void pFillOrders() {

        // Show that we are getting data
        cUserInterface.pShowGettingData();

        new Thread(this::mHandleFillOrders).start();

    }

    public  void pHandleScan(cBarcodeScan pvBarcodeScan) {

        //Set filter with scanned barcodeStr if there is no prefix
        if (!cRegex.pHasPrefix(pvBarcodeScan.getBarcodeOriginalStr())) {
            //no prefix, fine
            this.recyclerSearchView.setQuery(pvBarcodeScan.getBarcodeOriginalStr(), true);
            this.recyclerSearchView.callOnClick();
            return;
        }

        // If there is a prefix, check if its a salesorder, then remove prefix en set filter
        if (cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(), cBarcodeLayout.barcodeLayoutEnu.SALESORDER)) {
            //has prefix, is salesorderStr
            this.recyclerSearchView.setQuery(cRegex.pStripRegexPrefixStr(pvBarcodeScan.getBarcodeOriginalStr()), true);
            this.recyclerSearchView.callOnClick();
            return;
        }

        //If there is a prefix but it's not a salesorder tgen do nope
        cUserInterface.pDoNope(this.recyclerSearchView, true, true);

    }

    public  void pIntakeorderSelected(cIntakeorder pvIntakeorder) {

        if (pvIntakeorder.getOrderTypeStr().equalsIgnoreCase("EOM") || pvIntakeorder.getOrderTypeStr().equalsIgnoreCase("MAM")) {
            cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.multi_user_assignments_not_supported_in_android), R.raw.badsound);
            cUserInterface.pCheckAndCloseOpenDialogs();
            return;
        }

        if (!mCheckOrderIsLockableBln(pvIntakeorder)) {
            cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.lockorder_order_assigned_to_another_user), R.raw.badsound);
            cUserInterface.pCheckAndCloseOpenDialogs();
            return;
        }

        // Show that we are getting data
        cUserInterface.pShowGettingData();

        //Set the current intakeorder
        cIntakeorder.currentIntakeOrder = pvIntakeorder;
        cUser.currentUser.currentStockOwner = cStockOwner.pGetStockOwnerByCodeStr(cIntakeorder.currentIntakeOrder.getStockownerStr());
        FirebaseCrashlytics.getInstance().setCustomKey("Ordernumber", cIntakeorder.currentIntakeOrder.getOrderNumberStr());

        new Thread(this::mHandleIntakeorderSelected).start();

    }

    //End Region Public Methods

    // Region Private Methods

    private  void mHandleFillOrders(){


        //all Intakeorders
        if (!cIntakeorder.pGetIntakeOrdersViaWebserviceBln(true, "")) {
            cUserInterface.pDoExplodingScreen(cAppExtension.context.getString(R.string.error_get_intakeorders_failed), "", true, true );
            return;
        }

        if (cIntakeorder.allIntakeordersObl == null || cIntakeorder.allIntakeordersObl.size() == 0) {
            this.mShowNoOrdersIcon(true);
            return;
        }

        cAppExtension.activity.runOnUiThread(() -> {

            cIntakeorder.allIntakeordersObl  = cIntakeorder.pGetIntakesWithFilterFromDatabasObl();
            if (cIntakeorder.allIntakeordersObl.size() == 0) {
                mShowNoOrdersIcon( true);
                cUserInterface.pHideGettingData();
                return;
            }

            //Fill and show recycler
            mFillRecycler(cIntakeorder.allIntakeordersObl);
            mShowNoOrdersIcon(false);
            if (cSharedPreferences.userReceiveIntakeFilterBln()) {
                mApplyFilter();
            }

            cUserInterface.pHideGettingData();
        });
    }

    private void mHandleIntakeorderSelected(){

        cResult hulpResult;

        //Try to lock the intakeorder
        if (!this.mTryToLockOrderBln()) {
            this.pFillOrders();
            return;
        }

        //Delete the detail, so we can get them from the webservice
        if (!cIntakeorder.currentIntakeOrder.pDeleteDetailsBln()) {
            this.mStepFailed(cAppExtension.context.getString(R.string.error_couldnt_delete_details));
            return;
        }

        switch (cIntakeorder.currentIntakeOrder.getOrderTypeStr()) {
            case "MAT":
            case "MAS":
                hulpResult =  cIntakeorder.currentIntakeOrder.pGetOrderDetailsRst();
                if (!hulpResult.resultBln) {
                    this.mStepFailed(hulpResult.messagesStr());
                    return;
                }

                // If everything went well, then start Lines Activity
                cAppExtension.activity.runOnUiThread(this::mShowIntakeLinesActivity);

                break;

            case "EOS":
                hulpResult =  cIntakeorder.currentIntakeOrder.pGetOrderDetailsRst();
                if (!hulpResult.resultBln) {
                    this.mStepFailed(hulpResult.messagesStr());
                    return;
                }

                // If everything went well, then start Lines Activity
                cAppExtension.activity.runOnUiThread(this::mShowReceiveLinesActivity);

                break;

        }
    }

    private  void mStepFailed(String pvErrorMessageStr){
        cUserInterface.pDoExplodingScreen(pvErrorMessageStr, cIntakeorder.currentIntakeOrder.getOrderNumberStr(), true, true );
        cIntakeorder.currentIntakeOrder.pLockReleaseViaWebserviceBln();
        cUserInterface.pCheckAndCloseOpenDialogs();
        cIntakeorder.currentIntakeOrder = null;
    }

    private  void mShowIntakeLinesActivity() {

        final Intent intent;

        cUserInterface.pCheckAndCloseOpenDialogs();

        final ViewGroup container = cAppExtension.activity.findViewById(R.id.container);
        View clickedOrder;

        switch (cIntakeorder.currentIntakeOrder.getOrderTypeStr().toUpperCase()) {
            case  "MAT":
                intent = new Intent(cAppExtension.context, IntakeorderMATLinesActivity.class);
                break;

            case "MAS":
                intent = new Intent(cAppExtension.context, IntakeorderMASLinesActivity.class);
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + cIntakeorder.currentIntakeOrder.getOrderTypeStr().toUpperCase());
        }

        clickedOrder = container.findViewWithTag(cIntakeorder.currentIntakeOrder.getOrderNumberStr());

        ActivityOptionsCompat activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(cAppExtension.activity, new Pair<>(clickedOrder, cPublicDefinitions.VIEW_CHOSEN_ORDER));
           ActivityCompat.startActivity(cAppExtension.context,intent, activityOptions.toBundle());
    }

    private  void mShowReceiveLinesActivity() {

        cUserInterface.pCheckAndCloseOpenDialogs();

        ReceiveLinesActivity.closeOrderClickedBln = false;
        ReceiveLinesActivity.packagingClickedBln = false;
        ReceiveLinesActivity.packagingHandledBln = false;


        final ViewGroup container = cAppExtension.activity.findViewById(R.id.container);
        View clickedOrder;

        Intent intent = new Intent(cAppExtension.context, ReceiveLinesActivity.class);

        if (container != null) {
            clickedOrder = container.findViewWithTag(cIntakeorder.currentIntakeOrder.getOrderNumberStr());
            ActivityOptionsCompat activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(cAppExtension.activity, new Pair<>(clickedOrder, cPublicDefinitions.VIEW_CHOSEN_ORDER));
            ActivityCompat.startActivity(cAppExtension.context,intent, activityOptions.toBundle());
            return;
        }

        startActivity(intent);
    }

    private  void mShowCreateReceiveActivity() {

        cUserInterface.pCheckAndCloseOpenDialogs();

        Intent intent = new Intent(cAppExtension.context, CreateReceiveActivity.class);
        startActivity(intent);
    }

    private  void mShowCreateIntakeActivity() {

        cUserInterface.pCheckAndCloseOpenDialogs();

        Intent intent = new Intent(cAppExtension.context, CreateIntakeActivity.class);
        startActivity(intent);
    }

    private  void mShowCreateIntakeOrReceiveActivity() {

        cUserInterface.pCheckAndCloseOpenDialogs();

        Intent intent = new Intent(cAppExtension.context, CreateIntakeOrReceiveActivity.class);
        startActivity(intent);
    }

    // End Region Private Methods

    // Region View Methods

    //Bottom Sheet

    private void mInitBottomSheet() {

        this.bottomSheetBehavior = BottomSheetBehavior.from(constraintFilterOrders);
        this.bottomSheetBehavior.setHideable(true);
        this.bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        this.bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View pvBottomSheet, int pvNewStateInt) {
                if (pvNewStateInt == BottomSheetBehavior.STATE_COLLAPSED) {
                    mApplyFilter();
                }
                if (pvNewStateInt == BottomSheetBehavior.STATE_HIDDEN) {
                    mApplyFilter();
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });

        this.mFillBottomSheet();
    }

    private void mFillBottomSheet() {
        cAppExtension.fragmentManager.beginTransaction().replace(R.id.constraintFilterOrders, new FilterOrderLinesFragment()).commit();
    }

    private void mShowHideBottomSheet(Boolean pvShowBln) {

        if (pvShowBln) {
            this.bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            return;
        }

        this.bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

    }

    //End Bottom Sheet

    //Filter

    private   void mApplyFilter() {

        this.mShowThatFiltersInUse(cSharedPreferences.userReceiveIntakeFilterBln());

        List<cIntakeorder> filteredIntakesObl = cIntakeorder.pGetIntakesWithFilterFromDatabasObl();

        this.mFillRecycler(filteredIntakesObl);

        this.mShowNoOrdersIcon(filteredIntakesObl.size() == 0);

    }

    private   void mShowThatFiltersInUse(Boolean pvFiltersInUseBln) {
        if (pvFiltersInUseBln) {
            this.imageViewFilter.setImageDrawable(ContextCompat.getDrawable(cAppExtension.context, R.drawable.ic_filter_filled_black_24dp));
        }
        else {
            this.imageViewFilter.setImageDrawable(ContextCompat.getDrawable(cAppExtension.context, R.drawable.ic_filter_black_24dp));
        }
    }

    private void mSetFilterListener() {
        this.imageViewFilter.setOnClickListener(view -> mShowHideBottomSheet(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN || bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED));
    }

    private void mSetSwipeRefreshListener() {
        this.swipeRefreshLayout.setOnRefreshListener(this);
        this.swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent), getResources().getColor(R.color.colorActive), getResources().getColor(R.color.colorPrimary));
    }


    // End Filter

    // Recycler View

    private  void mFillRecycler(List<cIntakeorder> pvIntakeorderObl) {

        this.swipeRefreshLayout.setRefreshing(false);

        if (pvIntakeorderObl == null) {
            return;
        }

        this.imageViewFilter.setVisibility(View.VISIBLE);

        for (Fragment fragment: cAppExtension.fragmentManager.getFragments()) {
            if (fragment instanceof NoOrdersFragment) {
                cAppExtension.fragmentManager.beginTransaction().remove(fragment).commit();
            }
        }

        this.recyclerViewIntakeorders.setHasFixedSize(false);
        this.recyclerViewIntakeorders.setAdapter(this.getIntakeorderAdapter());
        this.recyclerViewIntakeorders.setLayoutManager(new LinearLayoutManager(cAppExtension.context));

        this.getIntakeorderAdapter().pFillData(pvIntakeorderObl);
    }

    private void mSetRecyclerOnScrollListener() {
        this.recyclerViewIntakeorders.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                        recyclerSearchView.setVisibility(View.VISIBLE);
                        recyclerSearchView.setAlpha(0.0f);

                        // Start the animation
                        recyclerSearchView.animate()
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
                        recyclerSearchView.setVisibility(View.GONE);
                    }

                }
            }
        });
    }

    private void mSetSearchListener() {
        //make whole view clickable
        this.recyclerSearchView.setOnClickListener(pvView -> recyclerSearchView.setIconified(false));

        //query entered
        this.recyclerSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String pvString) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String pvQueryTextStr) {
                mApplyFilter();
                getIntakeorderAdapter().pSetFilter(pvQueryTextStr);
                return true;
            }
        });

    }

    // End Recycler View

    // No orders iconmS

    private  void mShowNoOrdersIcon(final Boolean pvShowBln){

        cAppExtension.activity.runOnUiThread(() -> {

            cUserInterface.pHideGettingData();
            swipeRefreshLayout.setRefreshing(false);
            mSetToolBarTitleWithCounters();


            if (pvShowBln) {

                recyclerViewIntakeorders.setVisibility(View.INVISIBLE);

                FragmentTransaction fragmentTransaction = cAppExtension.fragmentManager.beginTransaction();
                NoOrdersFragment fragment = new NoOrdersFragment();
                fragmentTransaction.replace(R.id.moveorderContainer, fragment);
                fragmentTransaction.commit();

                mAutoOpenCreateActivity();
                return;
            }

            recyclerViewIntakeorders.setVisibility(View.VISIBLE);

            List<Fragment> fragments = cAppExtension.fragmentManager.getFragments();
            for (Fragment fragment : fragments) {
                if (fragment instanceof NoOrdersFragment) {
                    FragmentTransaction fragmentTransaction = cAppExtension.fragmentManager.beginTransaction();
                    fragmentTransaction.remove(fragment);
                    fragmentTransaction.commit();
                }
            }
        });
    }

    private  boolean mCheckOrderIsLockableBln(cIntakeorder pvIntakeorder){

        //If there is no assigned user, then always oke
        if (pvIntakeorder.getAssignedUserIdStr().isEmpty()) {
            return true;
        }

        //If you are allowed to unlock busy order, then no problem
        if (cSetting.GENERIC_UNLOCK_BUSY_ORDERS_ALLOWED()) {
            return true;
        }

        return cUser.currentUser.getUsernameStr().equalsIgnoreCase(pvIntakeorder.getAssignedUserIdStr());


    }

    private  void mResetCurrents(){

        //Reset all current objects
        cIntakeorder.currentIntakeOrder = null;
    }

    private  boolean mTryToLockOrderBln(){

        cResult hulpResult;
        hulpResult = cIntakeorder.currentIntakeOrder.pLockViaWebserviceRst();

        //Everything was fine, so we are done
        if (hulpResult.resultBln) {
            return true;
        }

        //Something went wrong, but no further actions are needed, so ony show reason of failure
        if (hulpResult.activityActionEnu == cWarehouseorder.ActivityActionEnu.Unknown ) {
            mStepFailed(hulpResult.messagesStr());
            return  false;
        }

        //Something went wrong, the order has been deleted, so show comments and refresh
        //If we got any comments, show them
        //TODO error
        //            if (cIntakeorder.currentIntakeOrder.pFeedbackCommentObl() != null && cIntakeorder.currentIntakeOrder.pFeedbackCommentObl().size() > 0 ) {
        //                //Process comments from webresult
        //                IntakeAndReceiveSelectActivity.mShowCommentsFragment(cIntakeorder.currentIntakeOrder.pFeedbackCommentObl(), hulpResult.messagesStr());
        //            }
        return (hulpResult.activityActionEnu != cWarehouseorder.ActivityActionEnu.Delete) &&
                (hulpResult.activityActionEnu != cWarehouseorder.ActivityActionEnu.NoStart);

    }

    private  void mSetToolBarTitleWithCounters(){


        String subTitleStr;

        if (cIntakeorder.allIntakeordersObl == null ) {
            String subtextStr = "0 " + cAppExtension.activity.getString(R.string.orders);
            this.toolbarSubTitle.setText(subtextStr);
            return;
        }

        if (!cSharedPreferences.userReceiveIntakeFilterBln()) {
            subTitleStr = cAppExtension.context.getResources().getQuantityString(R.plurals.plural_parameter1_orders, cIntakeorder.allIntakeordersObl.size(),cIntakeorder.allIntakeordersObl.size());
        } else {
            subTitleStr = "(" + cText.pIntToStringStr(cIntakeorder.pGetIntakesWithFilterFromDatabasObl().size())  + "/" + cText.pIntToStringStr(cIntakeorder.allIntakeordersObl.size()) + ") " + cAppExtension.activity.getString(R.string.orders) + " " + cAppExtension.activity.getString(R.string.shown);
        }

        this.toolbarSubTitle.setText(subTitleStr);

    }

    private void mReleaseLicense() {

        if (! cLicense.pReleaseLicenseViaWebserviceBln()) {
            cUserInterface.pShowSnackbarMessage(recyclerViewIntakeorders, cAppExtension.activity.getString(R.string.message_license_release_error),null, false);
        }

        cLicense.currentLicenseEnu = cLicense.LicenseEnu.Unknown;

    }

    private void mLeaveActivity(){
        this.mReleaseLicense();
        Intent intent = new Intent(cAppExtension.context, MenuActivity.class);
        startActivity(intent);
        finish();
    }

    private void mSetNewOrderButton() {


        List<String> workflowObl = new ArrayList<>();

        // Only check Store workflows
        if (IntakeAndReceiveSelectActivity.currentMainTypeEnu == cWarehouseorder.ReceiveAndStoreMainTypeEnu.Store) {
            workflowObl = cSetting.RECEIVE_MA_NEW_WORKFLOWS();
        }

        // Only check Receive workflows
        if (IntakeAndReceiveSelectActivity.currentMainTypeEnu == cWarehouseorder.ReceiveAndStoreMainTypeEnu.External) {
            workflowObl = cSetting.RECEIVE_EO_NEW_WORKFLOWS();
        }

        // Only check Receive workflows
        if (IntakeAndReceiveSelectActivity.currentMainTypeEnu == cWarehouseorder.ReceiveAndStoreMainTypeEnu.Unknown) {
            workflowObl = cSetting.RECEIVE_NEW_WORKFLOWS();
        }

        // Show or hide the button
        if (workflowObl.size() > 0) {
            imageViewNewOrder.setVisibility(View.VISIBLE);
        }
        else {
            imageViewNewOrder.setVisibility(View.INVISIBLE);
        }

    }

    private void mSetNewOrderListener() {

        this.imageViewNewOrder.setOnClickListener(pvView -> {

            if (IntakeAndReceiveSelectActivity.currentMainTypeEnu == cWarehouseorder.ReceiveAndStoreMainTypeEnu.External) {
               mShowCreateReceiveActivity();
               return;
           }

           if (IntakeAndReceiveSelectActivity.currentMainTypeEnu == cWarehouseorder.ReceiveAndStoreMainTypeEnu.Store) {
               mShowCreateIntakeActivity();
           }

            if (IntakeAndReceiveSelectActivity.currentMainTypeEnu == cWarehouseorder.ReceiveAndStoreMainTypeEnu.Unknown) {
                mShowCreateIntakeOrReceiveActivity();
            }

        });
    }

    private  void  mAutoOpenCreateActivity(){

        // We returned in this form, so don't start create activity
        if (!IntakeAndReceiveSelectActivity.startedViaMenuBln) {
            return;
        }

        if (IntakeAndReceiveSelectActivity.currentMainTypeEnu == cWarehouseorder.ReceiveAndStoreMainTypeEnu.External) {
            // We can't create, so don't start create activity
            if (cSetting.RECEIVE_EO_NEW_WORKFLOWS().isEmpty()) {
                return;
            }
        }

        if (IntakeAndReceiveSelectActivity.currentMainTypeEnu == cWarehouseorder.ReceiveAndStoreMainTypeEnu.Store) {
            // We can't create, so don't start create activity
            if (cSetting.RECEIVE_MA_NEW_WORKFLOWS().isEmpty()) {
                return;
            }
        }

        if (IntakeAndReceiveSelectActivity.currentMainTypeEnu == cWarehouseorder.ReceiveAndStoreMainTypeEnu.Unknown) {
            // We can't create, so don't start create activity
            if (cSetting.RECEIVE_NEW_WORKFLOWS().isEmpty()) {
                return;
            }
        }



        // We can't create STORE, so don't start create activity
        if (IntakeAndReceiveSelectActivity.currentMainTypeEnu == cWarehouseorder.ReceiveAndStoreMainTypeEnu.Store ) {

          if (!cSetting.RECEIVE_AUTO_CREATE_ORDER_MA()) {
              return;
          }

          this.mShowCreateIntakeActivity();
          return;
        }

        if (IntakeAndReceiveSelectActivity.currentMainTypeEnu == cWarehouseorder.ReceiveAndStoreMainTypeEnu.External ) {

            if (!cSetting.RECEIVE_AUTO_CREATE_ORDER_EO()) {
                return;
            }

            this.mShowCreateReceiveActivity();
        }

        if (IntakeAndReceiveSelectActivity.currentMainTypeEnu == cWarehouseorder.ReceiveAndStoreMainTypeEnu.Unknown ) {

            if (!cSetting.RECEIVE_AUTO_CREATE_ORDER()) {
                return;
            }

            this.mShowCreateIntakeOrReceiveActivity();
        }

    }

    // End No orders icon

    // End Region View Method

}

