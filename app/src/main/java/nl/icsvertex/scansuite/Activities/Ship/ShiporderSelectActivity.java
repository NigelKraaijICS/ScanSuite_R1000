package nl.icsvertex.scansuite.Activities.Ship;

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
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Basics.BarcodeLayouts.cBarcodeLayout;
import SSU_WHS.Basics.Settings.cSetting;
import SSU_WHS.Basics.ShippingAgentServiceShippingUnits.cShippingAgentServiceShippingUnit;
import SSU_WHS.Basics.ShippingAgentServices.cShippingAgentService;
import SSU_WHS.Basics.ShippingAgents.cShippingAgent;
import SSU_WHS.Basics.Users.cUser;
import SSU_WHS.Basics.Workplaces.cWorkplace;
import SSU_WHS.General.Comments.cComment;
import SSU_WHS.General.Licenses.cLicense;
import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.General.cPublicDefinitions;
import SSU_WHS.Picken.PickorderBarcodes.cPickorderBarcode;
import SSU_WHS.Picken.PickorderLines.cPickorderLine;
import SSU_WHS.Picken.Pickorders.cPickorder;
import SSU_WHS.Picken.Pickorders.cPickorderAdapter;
import SSU_WHS.Picken.Shipment.cShipment;
import nl.icsvertex.scansuite.Activities.General.MenuActivity;
import nl.icsvertex.scansuite.Fragments.Dialogs.CommentFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.FilterOrderLinesFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.NoOrdersFragment;
import nl.icsvertex.scansuite.R;


public class ShiporderSelectActivity extends AppCompatActivity implements iICSDefaultActivity, SwipeRefreshLayout.OnRefreshListener {

    //Region Public Properties


    //End Region Public Properties

    //Region Private Properties

    // Region Views

    //Region Private Properties

    // Region Views

    private  SearchView recyclerSearchView;
    private  ImageView toolbarImage;
    private  TextView toolbarTitle;
    private  TextView toolbarSubTitle;
    private  RecyclerView recyclerViewShiporders;

    private  ConstraintLayout constraintFilterOrders;
    private  SwipeRefreshLayout swipeRefreshLayout;
    private  BottomSheetBehavior bottomSheetBehavior;
    private  ImageView imageViewFilter;

    cPickorderAdapter pickorderAdapter;
    cPickorderAdapter getPickorderAdapter(){
        if (this.pickorderAdapter == null) {
            this.pickorderAdapter = new cPickorderAdapter();
        }

        return  this.pickorderAdapter;
    }

    // End Region Views

    //End Region Private Properties


    //Region Default Methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shiporder_select);
        this.mActivityInitialize();
    }

    @Override
    protected void onPause() {
        super.onPause();
        cBarcodeScan.pUnregisterBarcodeReceiver();
    }

    @Override
    public void onResume() {
        super.onResume();
        cBarcodeScan.pRegisterBarcodeReceiver();
        cUserInterface.pEnableScanner();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
        this.mTryToLeaveActivity();
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

        this.mSetToolbar(getResources().getString(R.string.screentitle_shiporderselect));

        this.mFieldsInitialize();

        this.mSetListeners();

        this.mInitScreen();
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
        this.recyclerViewShiporders = findViewById(R.id.recyclerViewShiporders);
        this.recyclerSearchView = findViewById(R.id.recyclerSearchView);
        this.imageViewFilter = findViewById(R.id.imageViewFilter);
        this.constraintFilterOrders = findViewById(R.id.constraintFilterOrders);
        this.swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
    }

    @Override
    public void mSetToolbar(String pvScreenTitleStr) {
        this.toolbarImage.setImageResource(R.drawable.ic_menu_pick);
        this.toolbarTitle.setText(pvScreenTitleStr);
        this.toolbarTitle.setSelected(true);
        this.toolbarSubTitle.setText(cUser.currentUser.currentBranch.getBranchNameStr());

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
        this.pFillOrders();

    }

    @Override
    public void mSetListeners() {
        this.mSetSearchListener();
        this.mSetFilterListener();
        this.mSetSwipeRefreshListener();
    }

    @Override
    public void mInitScreen() {
        cBarcodeScan.pRegisterBarcodeReceiver();
    }

    //End Region iICSDefaultActivity defaults

    //Region Public Methods

    public  void pFillOrders() {

        // Show that we are getting data
        cUserInterface.pShowGettingData();

        new Thread(new Runnable() {
            public void run() {
                mHandleFillOrders();
            }
        }).start();


    }

    public  void pShiporderSelected(cPickorder pvPickorder) {


        if (!mCheckOrderIsLockableBln(pvPickorder)) {
            cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.lockorder_order_assigned_to_another_user), R.raw.badsound);
            cUserInterface.pCheckAndCloseOpenDialogs();
            return;
        }

        // Show that we are getting data
        cUserInterface.pShowGettingData();

        //Set the current pickorder
        cPickorder.currentPickOrder = pvPickorder;

        new Thread(new Runnable() {
            public void run() {
                mHandleShipOrderSelected();
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

        // If there is a prefix, check if its a salesorder, then remove prefix en set filter
        if (cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(), cBarcodeLayout.barcodeLayoutEnu.SALESORDER)) {
            //has prefix, is salesorderStr
            this.recyclerSearchView.setQuery(cRegex.pStripRegexPrefixStr(pvBarcodeScan.getBarcodeOriginalStr()), true);
            this.recyclerSearchView.callOnClick();
            return;
        }

        //If there is a prefix but it's not a salesorder tgen do nope
        cUserInterface.pDoNope(recyclerSearchView, true, true);
    }

    //End Region Public Methods

    //Region Private Method

    private  void mHandleFillOrders() {

        //First get all sortorders
        if (!cPickorder.pGetPackAndShipOrdersViaWebserviceBln(true, "")) {
            cUserInterface.pDoExplodingScreen(cAppExtension.context.getString(R.string.error_get_pickorders_failed), "", true, true);
            return;
        }


        if (cPickorder.allPickordersObl == null || cPickorder.allPickordersObl.size() == 0) {
            this.mShowNoOrdersIcon(true);
            return;
        }

        cAppExtension.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //Fill and show recycler
                mSetSortorderRecycler(cPickorder.allPickordersObl);
                mShowNoOrdersIcon(false);
            }
        });

    }

    private  void mHandleShipOrderSelected() {

        cResult hulpResult;


        //Try to lock the pickorder

        if (!this.mTryToLockOrderBln()) {
            this.pFillOrders();
            return;
        }

        //Delete the detail, so we can get them from the webservice
        if (!cPickorder.currentPickOrder.pDeleteDetailsBln()) {
            this.mStepFailed(cAppExtension.context.getString(R.string.error_couldnt_delete_details));
            return;

        }

        hulpResult = this.mGetOrderDetailsRst();
        if (!hulpResult.resultBln) {
            this.mStepFailed(hulpResult.messagesStr());
            return;
        }

        cAppExtension.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // If everything went well, then start Lines Activity
                mShowShipLinesActivity();
            }
        });

    }

    private void mSetSearchListener() {
        //make whole view clickable
        this.recyclerSearchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerSearchView.setIconified(false);
            }
        });

        //query entered
        this.recyclerSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String pvQueryTextStr) {
                mApplyFilter();
                getPickorderAdapter().pSetFilter((pvQueryTextStr));
                return true;
            }
        });
    }

    private void mSetFilterListener() {
        this.imageViewFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN || bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    mShowHideBottomSheet(true);
                } else {
                    mShowHideBottomSheet(false);
                }
            }
        });
    }

    private void mSetSwipeRefreshListener() {
        this.swipeRefreshLayout.setOnRefreshListener(this);
        this.swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent), getResources().getColor(R.color.colorActive), getResources().getColor(R.color.colorPrimary));
    }

    private void mInitBottomSheet() {

        this.bottomSheetBehavior = BottomSheetBehavior.from(this.constraintFilterOrders);
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

    private void mShowHideBottomSheet(Boolean pvShowBln) {

        if (pvShowBln) {
            this.bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            return;
        }

        this.bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

    }

    private void mApplyFilter() {

        List<cPickorder> filteredPicksObl;

        this.mShowThatFiltersInUse(cSharedPreferences.userFilterBln());

        filteredPicksObl = cPickorder.pGetPicksFromDatabasObl();
        if (filteredPicksObl.size() == 0) {
            return;
        }

        this.mSetSortorderRecycler(filteredPicksObl);
    }

    private void mShowThatFiltersInUse(Boolean pvFiltersInUseBln) {
        if (pvFiltersInUseBln) {
            this.imageViewFilter.setImageDrawable(ContextCompat.getDrawable(cAppExtension.context, R.drawable.ic_filter_filled_black_24dp));
        } else {
            this. imageViewFilter.setImageDrawable(ContextCompat.getDrawable(cAppExtension.context, R.drawable.ic_filter_black_24dp));
        }
    }

    private void mFillBottomSheet() {
        cAppExtension.fragmentManager.beginTransaction().replace(R.id.constraintFilterOrders, new FilterOrderLinesFragment()).commit();
    }

    private void mResetCurrents() {

        //Reset all current objects
        cPickorder.currentPickOrder = null;
        cPickorderLine.currentPickOrderLine = null;
        cPickorderBarcode.currentPickorderBarcode = null;
        cWorkplace.currentWorkplace = null;
        cShipment.allShipmentsObl = null;
    }

    private  void mShowNoOrdersIcon(final Boolean pvShowBln) {

        cAppExtension.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                cUserInterface.pHideGettingData();

                if (pvShowBln) {

                    recyclerViewShiporders.setVisibility(View.INVISIBLE);
                    FragmentTransaction fragmentTransaction = cAppExtension.fragmentManager.beginTransaction();
                    NoOrdersFragment fragment = new NoOrdersFragment();
                    fragmentTransaction.replace(R.id.container, fragment);
                    fragmentTransaction.commit();
                    return;
                }

                recyclerViewShiporders.setVisibility(View.VISIBLE);

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

    private  void mSetSortorderRecycler(List<cPickorder> pvPickorderObl) {

        this.swipeRefreshLayout.setRefreshing(false);

        if (pvPickorderObl == null || pvPickorderObl.size() == 0) {
            return;
        }

        for (Fragment fragment : cAppExtension.fragmentManager.getFragments()) {
            if (fragment instanceof NoOrdersFragment) {
                cAppExtension.fragmentManager.beginTransaction().remove(fragment).commit();
            }
        }

        this.recyclerViewShiporders.setHasFixedSize(false);
        this.recyclerViewShiporders.setAdapter(this.getPickorderAdapter());
        this.recyclerViewShiporders.setLayoutManager(new LinearLayoutManager(cAppExtension.context));

        this.getPickorderAdapter().pFillData(pvPickorderObl);
    }

    private  boolean mCheckOrderIsLockableBln(cPickorder pvPickorder) {

        //If there is no assigned user, then always oke
        if (pvPickorder.getAssignedUserIdStr().isEmpty()) {
            return true;
        }

        //If you are allowed to unlock busy order, then no problem
        if (cSetting.GENERIC_UNLOCK_BUSY_ORDERS_ALLOWED()) {
            return true;
        }

        return cUser.currentUser.getUsernameStr().equalsIgnoreCase(pvPickorder.getAssignedUserIdStr());


    }

    private  boolean mTryToLockOrderBln() {

        cResult hulpResult;
        hulpResult = cPickorder.currentPickOrder.pLockViaWebserviceRst(cWarehouseorder.StepCodeEnu.Pick_PackAndShip, cWarehouseorder.WorkflowPickStepEnu.PickPackAndShip);

        //Everything was fine, so we are done
        if (hulpResult.resultBln) {
            return true;
        }

        //Something went wrong, but no further actions are needed, so ony show reason of failure
        if (hulpResult.activityActionEnu == cWarehouseorder.ActivityActionEnu.Unknown) {
            mStepFailed(hulpResult.messagesStr());
            return false;
        }

        //Something went wrong, the order has been deleted, so show comments and refresh
        if ( hulpResult.activityActionEnu == cWarehouseorder.ActivityActionEnu.Delete ||
             hulpResult.activityActionEnu == cWarehouseorder.ActivityActionEnu.NoStart) {


            //If we got any comments, show them
            if (cPickorder.currentPickOrder.pFeedbackCommentObl() != null && cPickorder.currentPickOrder.pFeedbackCommentObl().size() > 0) {
                //Process comments from webresult
                this.mShowCommentsFragment(cPickorder.currentPickOrder.pFeedbackCommentObl(), hulpResult.messagesStr());
            }

            return false;
        }


        return true;

    }

    private  void mStepFailed(String pvErrorMessageStr) {
        cUserInterface.pDoExplodingScreen(pvErrorMessageStr, cPickorder.currentPickOrder.getOrderNumberStr(), true, true);
        cPickorder.currentPickOrder.pLockReleaseViaWebserviceBln(cWarehouseorder.StepCodeEnu.Pick_PackAndShip, cWarehouseorder.WorkflowPickStepEnu.PickPackAndShip);
        cUserInterface.pCheckAndCloseOpenDialogs();
        cPickorder.currentPickOrder = null;
    }

    private  void mShowCommentsFragment(List<cComment> pvDataObl, String pvTitleStr) {

        cUserInterface.pCheckAndCloseOpenDialogs();

        Bundle bundle = new Bundle();
        bundle.putString(cPublicDefinitions.KEY_COMMENTHEADER, pvTitleStr);

        CommentFragment commentFragment = new CommentFragment(pvDataObl);
        commentFragment.setArguments(bundle);

        commentFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.COMMENTFRAGMENT_TAG);
        cUserInterface.pPlaySound(R.raw.message, 0);
    }

    private  cResult mGetOrderDetailsRst() {

        cResult result;

        result = new cResult();
        result.resultBln = true;

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

    private  void mShowShipLinesActivity() {

        cUserInterface.pCheckAndCloseOpenDialogs();

        final ViewGroup container = cAppExtension.activity.findViewById(R.id.container);

        Intent intent = new Intent(cAppExtension.context, ShiporderLinesActivity.class);
        View clickedOrder = container.findViewWithTag(cPickorder.currentPickOrder.getOrderNumberStr());
        ActivityOptionsCompat activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(cAppExtension.activity, new Pair<>(clickedOrder, cPublicDefinitions.VIEW_CHOSEN_ORDER));
        ActivityCompat.startActivity(cAppExtension.context, intent, activityOptions.toBundle());
    }

    private void mTryToLeaveActivity() {

        this.mReleaseLicense();

        Intent intent = new Intent(cAppExtension.context, MenuActivity.class);
        cAppExtension.activity.startActivity(intent);
    }

    private void mReleaseLicense() {

        if (!cLicense.pReleaseLicenseViaWebserviceBln()) {
            cUserInterface.pShowSnackbarMessage(recyclerViewShiporders, cAppExtension.activity.getString(R.string.message_license_release_error), null, false);
        }

        cLicense.currentLicenseEnu = cLicense.LicenseEnu.Unknown;

    }
}

//End Region Private Methods
