package nl.icsvertex.scansuite.Activities.PackAndShip;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.List;

import ICS.Interfaces.iICSDefaultActivity;
import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cRegex;
import ICS.Utils.cResult;
import ICS.Utils.cSharedPreferences;
import ICS.Utils.cText;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Basics.Settings.cSetting;
import SSU_WHS.Basics.Users.cUser;
import SSU_WHS.General.Licenses.cLicense;
import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.General.cPublicDefinitions;
import SSU_WHS.PackAndShip.PackAndShipOrders.cPackAndShipOrder;
import SSU_WHS.PackAndShip.PackAndShipOrders.cPackAndShipOrderAdapter;
import nl.icsvertex.scansuite.Activities.General.MenuActivity;
import nl.icsvertex.scansuite.Fragments.Dialogs.FilterOrderLinesFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.NoOrdersFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.WorkflowFragment;
import nl.icsvertex.scansuite.R;

public class PackAndShipSelectActivity extends AppCompatActivity implements iICSDefaultActivity, SwipeRefreshLayout.OnRefreshListener {

    //Region Public Properties

    //End Region Public Properties

    //Region Private Properties

    // Region Views
    private RecyclerView recyclerViewPackAndShipOrders;
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

    public static boolean startedViaMenuBln;

    private cPackAndShipOrderAdapter packAndShipOrderAdapter;
    private cPackAndShipOrderAdapter getPackAndShipOrderAdapter(){
        if (this.packAndShipOrderAdapter == null) {
            this.packAndShipOrderAdapter = new cPackAndShipOrderAdapter();
        }

        return this.packAndShipOrderAdapter;
    }

    public static cWarehouseorder.PackAndShipMainTypeEnu currentMainTypeEnu;

    // End Region Views

    //End Region Private Properties

    //Region Default Methods

    @Override
    protected void onCreate(Bundle pvSavedInstanceState) {
        super.onCreate(pvSavedInstanceState);
        setContentView(R.layout.activity_pack_and_ship_orderselect);
        cBarcodeScan.pRegisterBarcodeReceiver(this.getClass().getSimpleName());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (cAppExtension.activity instanceof PackAndShipSelectActivity) {
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
        mActivityInitialize();
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
        this.mLeaveActivity();
    }

    @Override
    public void onRefresh() {
        this.pFillOrders();
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

        this.mSetToolbar(getResources().getString(R.string.screentitle_packandshiporderselect));

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
        this.recyclerViewPackAndShipOrders = findViewById(R.id.recyclerViewPackAndShipOrders);
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
            this.toolbarImage.setImageResource(R.drawable.ic_menu_ship);
            this.toolbarTitle.setText(pvScreenTitleStr);
        }

        this.toolbarTitle.setSelected(true);
        this.toolbarSubTitle2.setText(cUser.currentUser.currentBranch.getBranchNameStr());
        this.toolbarSubTitle.setSelected(true);
        ViewCompat.setTransitionName(this.toolbarImage, cPublicDefinitions.VIEW_NAME_HEADER_IMAGE);
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

    public void pFillOrders() {

        // Show that we are getting data
        cUserInterface.pShowGettingData();

        new Thread(new Runnable() {
            public void run() {
                mHandleFillOrders();
            }
        }).start();

    }

    public  void pHandleScan(cBarcodeScan pvBarcodeScan) {

        //Set filter with scanned barcodeStr if there is no prefix
        if (!cRegex.pHasPrefix(pvBarcodeScan.getBarcodeOriginalStr())) {
            //no prefix, fine
            this.recyclerSearchView.setQuery(pvBarcodeScan.getBarcodeOriginalStr(), true);
            this.recyclerSearchView.callOnClick();
            return;
        }

        //If there is a prefix but it's not a salesorder tgen do nope
        cUserInterface.pDoNope(this.recyclerSearchView, true, true);

    }

    public void pPackAndShipOrderSelected(cPackAndShipOrder pvPackAndShipOrder) {

        if (!this.mCheckOrderIsLockableBln(pvPackAndShipOrder)) {
            cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.lockorder_order_assigned_to_another_user), R.raw.badsound);
            cUserInterface.pCheckAndCloseOpenDialogs();
            return;
        }

        // Show that we are getting data
        cUserInterface.pShowGettingData();

        //Set the current pack and ship order
        cPackAndShipOrder.currentPackAndShipOrder = pvPackAndShipOrder;

        new Thread(new Runnable() {
            public void run() {
                mHandlePackAndShipOrderSelected();
            }
        }).start();

    }

    public void pNewWorkflowSelected(String pvNewWorkflowsStr) {

        cUserInterface.pCheckAndCloseOpenDialogs();

        cWarehouseorder.PackAndShipMainTypeEnu packAndShipMainTypeEnu = cWarehouseorder.PackAndShipMainTypeEnu.Unknown;

        switch (pvNewWorkflowsStr) {
            case "SINGLE":
                packAndShipMainTypeEnu =  cWarehouseorder.PackAndShipMainTypeEnu.SINGLE;
              break;
            case "MULTI":
                packAndShipMainTypeEnu = cWarehouseorder.PackAndShipMainTypeEnu.MULTI;
                break;

        }

        mShowCreatePackAndShipActivity(packAndShipMainTypeEnu);
    }

    //End Region Public Methods

    // Region Private Methods

    private void mHandleFillOrders(){

        //all Pack and Ship orders
        if (!cPackAndShipOrder.pGetPackAndShipOrdersViaWebserviceBln(true, "")) {
            cUserInterface.pDoExplodingScreen(cAppExtension.context.getString(R.string.error_get_packandshiporders_failed), "", true, true );
            return;
        }

        if (cPackAndShipOrder.allPackAndShipOrdersObl == null || cPackAndShipOrder.allPackAndShipOrdersObl.size() == 0) {
            this.mShowNoOrdersIcon(true);
            return;
        }

        cAppExtension.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //Fill and show recycler

                cPackAndShipOrder.allPackAndShipOrdersObl = cPackAndShipOrder.pGetPackAndShipOrdersWithFilterFromDatabasObl();
                if (cPackAndShipOrder.allPackAndShipOrdersObl.size() == 0) {
                    mShowNoOrdersIcon( true);
                    cUserInterface.pHideGettingData();
                    return;
                }

                mFillRecycler(cPackAndShipOrder.allPackAndShipOrdersObl);
                mShowNoOrdersIcon(false);
                if (cSharedPreferences.userFilterBln()) {
                    mApplyFilter();
                }

                cUserInterface.pHideGettingData();
            }
        });
    }

    private void mHandlePackAndShipOrderSelected(){

        cResult hulpResult;

        //Try to lock the pack and ship order
        if (!this.mTryToLockOrderBln()) {
            this.pFillOrders();
            return;
        }

        //Delete the detail, so we can get them from the webservice
        if (!cPackAndShipOrder.currentPackAndShipOrder.pDeleteDetailsBln()) {
            this.mStepFailed(cAppExtension.context.getString(R.string.error_couldnt_delete_details));
            return;
        }

        hulpResult = cPackAndShipOrder.currentPackAndShipOrder.pGetOrderDetailsRst();


        if (!hulpResult.resultBln) {
            this.mStepFailed(hulpResult.messagesStr());
            return;
        }

        cAppExtension.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // If everything went well, then start Lines Activity
                mShowPackAndShipActivity();
            }
        });


    }

    private  void mStepFailed(String pvErrorMessageStr){
        cUserInterface.pDoExplodingScreen(pvErrorMessageStr, cPackAndShipOrder.currentPackAndShipOrder.getOrderNumberStr(), true, true );
        cPackAndShipOrder.currentPackAndShipOrder.pLockReleaseViaWebserviceBln();
        cUserInterface.pCheckAndCloseOpenDialogs();
        cPackAndShipOrder.currentPackAndShipOrder = null;
    }

    private void mShowPackAndShipActivity() {

        Intent   intent = null;

        if (cPackAndShipOrder.currentPackAndShipOrder.getOrderTypeStr().equalsIgnoreCase(cWarehouseorder.WorkflowEnu.PS1.toString())) {
            intent =  new Intent(cAppExtension.context, PackAndShipSingleActivity.class);
        }

        if (cPackAndShipOrder.currentPackAndShipOrder.getOrderTypeStr().equalsIgnoreCase(cWarehouseorder.WorkflowEnu.PSM.toString())) {
            intent = new Intent(cAppExtension.context, PackAndShipMultiActivity.class);
        }

        cUserInterface.pCheckAndCloseOpenDialogs();
        ActivityCompat.startActivity(cAppExtension.context,intent, null);

    }

    private  void mShowCreatePackAndShipActivity(cWarehouseorder.PackAndShipMainTypeEnu pvPackAndShipMainTypeEnu) {

        Intent intent = null;

        if (pvPackAndShipMainTypeEnu != null) {
            CreatePackAndShipActivity.packAndShipMainTypeEnu = pvPackAndShipMainTypeEnu;
        }

        if (PackAndShipSelectActivity.currentMainTypeEnu !=  cWarehouseorder.PackAndShipMainTypeEnu.Unknown) {
            CreatePackAndShipActivity.packAndShipMainTypeEnu = PackAndShipSelectActivity.currentMainTypeEnu;


            if (CreatePackAndShipActivity.packAndShipMainTypeEnu == cWarehouseorder.PackAndShipMainTypeEnu.SINGLE){
                CreatePackAndShipActivity.packAndShipMainTypeEnu = cWarehouseorder.PackAndShipMainTypeEnu.SINGLE;
                intent = new Intent(cAppExtension.context, PackAndShipSingleActivity.class);
            }

            if (CreatePackAndShipActivity.packAndShipMainTypeEnu == cWarehouseorder.PackAndShipMainTypeEnu.MULTI){
                CreatePackAndShipActivity.packAndShipMainTypeEnu = cWarehouseorder.PackAndShipMainTypeEnu.MULTI;
                intent = new Intent(cAppExtension.context, PackAndShipMultiActivity.class);
            }

        }
        else
        {
            if (cSetting.PACK_AND_SHIP_NEW_WORKFLOWS().size() > 1) {
                this.mShowWorklowFragment();
                return;
            }

            switch (cSetting.PACK_AND_SHIP_NEW_WORKFLOWS().get(0).toUpperCase()) {
                case  "PS1":
                    CreatePackAndShipActivity.packAndShipMainTypeEnu = cWarehouseorder.PackAndShipMainTypeEnu.SINGLE;
                    intent = new Intent(cAppExtension.context, PackAndShipSingleActivity.class);
                    break;

                case  "PSM":
                    CreatePackAndShipActivity.packAndShipMainTypeEnu = cWarehouseorder.PackAndShipMainTypeEnu.MULTI;
                    intent = new Intent(cAppExtension.context, PackAndShipMultiActivity.class);
                    break;

            }
        }

        cUserInterface.pCheckAndCloseOpenDialogs();
        ActivityCompat.startActivity(cAppExtension.context,intent, null);
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

    private void mApplyFilter() {

        this.mShowThatFiltersInUse(cSharedPreferences.userFilterBln());

        List<cPackAndShipOrder> filteredPackAndShipOrdersObl = cPackAndShipOrder.pGetPackAndShipOrdersWithFilterFromDatabasObl();

        this.mFillRecycler(filteredPackAndShipOrdersObl);

        this.mShowNoOrdersIcon(filteredPackAndShipOrdersObl.size() == 0);

    }

    private void mShowThatFiltersInUse(Boolean pvFiltersInUseBln) {
        if (pvFiltersInUseBln) {
            this.imageViewFilter.setImageDrawable(ContextCompat.getDrawable(cAppExtension.context, R.drawable.ic_filter_filled_black_24dp));
        }
        else {
            this.imageViewFilter.setImageDrawable(ContextCompat.getDrawable(cAppExtension.context, R.drawable.ic_filter_black_24dp));
        }
    }

    private void mSetFilterListener() {
        this.imageViewFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mShowHideBottomSheet(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN || bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
    }

    private void mSetSwipeRefreshListener() {
        this.swipeRefreshLayout.setOnRefreshListener(this);
        this.swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent), getResources().getColor(R.color.colorActive), getResources().getColor(R.color.colorPrimary));
    }


    // End Filter

    // Recycler View

    private  void mFillRecycler(List<cPackAndShipOrder> pvDataObl) {

        this.swipeRefreshLayout.setRefreshing(false);

        if (pvDataObl == null) {
            return;
        }

        this.imageViewFilter.setVisibility(View.VISIBLE);

        for (Fragment fragment: cAppExtension.fragmentManager.getFragments()) {
            if (fragment instanceof NoOrdersFragment) {
                cAppExtension.fragmentManager.beginTransaction().remove(fragment).commit();
            }
        }

        this.recyclerViewPackAndShipOrders.setHasFixedSize(false);
        this.recyclerViewPackAndShipOrders.setAdapter(this.getPackAndShipOrderAdapter());
        this.recyclerViewPackAndShipOrders.setLayoutManager(new LinearLayoutManager(cAppExtension.context));

        this.getPackAndShipOrderAdapter().pFillData(pvDataObl);
    }

    private void mSetRecyclerOnScrollListener() {
        recyclerViewPackAndShipOrders.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
        this.recyclerSearchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View pvView) {
                recyclerSearchView.setIconified(false);
            }
        });

        //query entered
        this.recyclerSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String pvString) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String pvQueryTextStr) {
                mApplyFilter();
                getPackAndShipOrderAdapter().pSetFilter(pvQueryTextStr);
                return true;
            }
        });

    }

    // End Recycler View

    // No orders iconmS

    private void mShowNoOrdersIcon(final Boolean pvShowBln){

        cAppExtension.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                cUserInterface.pHideGettingData();
                swipeRefreshLayout.setRefreshing(false);


                mSetToolBarTitleWithCounters();


                if (pvShowBln) {

                    recyclerViewPackAndShipOrders.setVisibility(View.INVISIBLE);

                    FragmentTransaction fragmentTransaction = cAppExtension.fragmentManager.beginTransaction();
                    NoOrdersFragment fragment = new NoOrdersFragment();
                    fragmentTransaction.replace(R.id.packAndShipOrderContainer, fragment);
                    fragmentTransaction.commit();

                    if (cSetting.PACK_AND_SHIP_AUTO_CREATE_ORDER()) {
                        mAutoOpenCreateActivity();
                    }
                    return;
                }

                recyclerViewPackAndShipOrders.setVisibility(View.VISIBLE);

                List<Fragment> fragments = cAppExtension.fragmentManager.getFragments();
                for (Fragment fragment : fragments) {
                    if (fragment instanceof NoOrdersFragment) {
                        FragmentTransaction fragmentTransaction = cAppExtension.fragmentManager.beginTransaction();
                        fragmentTransaction.remove(fragment);
                        fragmentTransaction.commit();
                    }
                }
            }
        });
    }

    private boolean mCheckOrderIsLockableBln(cPackAndShipOrder pvPackAndShipOrder){

        //If there is no assigned user, then always oke
        if (pvPackAndShipOrder.getAssignedUserIdStr().isEmpty()) {
            return true;
        }

        //If you are allowed to unlock busy order, then no problem
        if (cSetting.GENERIC_UNLOCK_BUSY_ORDERS_ALLOWED()) {
            return true;
        }

        return cUser.currentUser.getUsernameStr().equalsIgnoreCase(pvPackAndShipOrder.getAssignedUserIdStr());


    }

    private  void mResetCurrents(){

        //Reset all current objects
        cPackAndShipOrder.currentPackAndShipOrder = null;
    }

    private boolean mTryToLockOrderBln(){

        cResult hulpResult = cPackAndShipOrder.currentPackAndShipOrder.pLockViaWebserviceRst();

        //Everything was fine, so we are done
        if (hulpResult.resultBln) {
            return true;
        }

        //Something went wrong, but no further actions are needed, so ony show reason of failure
        if (hulpResult.activityActionEnu == cWarehouseorder.ActivityActionEnu.Unknown ) {
            mStepFailed(hulpResult.messagesStr());
            return  false;
        }

        return (hulpResult.activityActionEnu != cWarehouseorder.ActivityActionEnu.Delete) &&
                (hulpResult.activityActionEnu != cWarehouseorder.ActivityActionEnu.NoStart);

    }

    private void mSetToolBarTitleWithCounters(){


        String subTitleStr;

        if (cPackAndShipOrder.allPackAndShipOrdersObl == null ) {
            String toolbarStr = "0 " + cAppExtension.activity.getString(R.string.orders);
            this.toolbarSubTitle.setText(toolbarStr);
            return;
        }

        if (!cSharedPreferences.userFilterBln()) {
            subTitleStr = cAppExtension.context.getResources().getQuantityString(R.plurals.plural_parameter1_orders, cPackAndShipOrder.allPackAndShipOrdersObl.size(),cPackAndShipOrder.allPackAndShipOrdersObl.size());
        } else {
            subTitleStr = "(" + cText.pIntToStringStr(cPackAndShipOrder.pGetPackAndShipOrdersWithFilterFromDatabasObl().size())  + "/" + cText.pIntToStringStr(cPackAndShipOrder.allPackAndShipOrdersObl.size()) + ") " + cAppExtension.activity.getString(R.string.orders) + " " + cAppExtension.activity.getString(R.string.shown);
        }

        this.toolbarSubTitle.setText(subTitleStr);

    }

    private void mReleaseLicense() {

        if (! cLicense.pReleaseLicenseViaWebserviceBln()) {
            cUserInterface.pShowSnackbarMessage(recyclerViewPackAndShipOrders, cAppExtension.activity.getString(R.string.message_license_release_error),null, false);
        }

        cLicense.currentLicenseEnu = cLicense.LicenseEnu.Unknown;

    }

    private void mLeaveActivity(){
        this.mReleaseLicense();

        Intent intent = new Intent(cAppExtension.context, MenuActivity.class);
        cAppExtension.activity.startActivity(intent);
        cAppExtension.activity.finish();

    }

    private void mSetNewOrderButton() {

        if (cSetting.PACK_AND_SHIP_NEW_WORKFLOWS().size() >= 1) {
            this.imageViewNewOrder.setVisibility(View.VISIBLE);
        }
        else {
            this.imageViewNewOrder.setVisibility(View.INVISIBLE);
        }
    }

    private void mSetNewOrderListener() {
        this.imageViewNewOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View pvView) {
                mShowCreatePackAndShipActivity(null);
            }
        });
    }

    private void mAutoOpenCreateActivity(){

        // We returned in this form, so don't start create activity
        if (!PackAndShipSelectActivity.startedViaMenuBln) {
            return;
        }

        if (cSetting.PACK_AND_SHIP_NEW_WORKFLOWS().size() == 0) {
            return;
        }

      this.mShowCreatePackAndShipActivity(null);

    }

    private  void mShowWorklowFragment() {

        cUserInterface.pCheckAndCloseOpenDialogs();

        WorkflowFragment workflowFragment = new WorkflowFragment();
        workflowFragment.show(cAppExtension.fragmentManager , cPublicDefinitions.WORKFLOWFRAGMENT_TAG);
    }

    // End No orders icon

    // End Region View Method

}

