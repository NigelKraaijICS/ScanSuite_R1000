package nl.icsvertex.scansuite.Activities.Move;


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
import SSU_WHS.Basics.Users.cUser;
import SSU_WHS.General.Licenses.cLicense;
import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.Move.MoveOrders.cMoveorder;
import nl.icsvertex.scansuite.Activities.General.MenuActivity;
import nl.icsvertex.scansuite.Activities.Receive.ReceiveLinesActivity;
import nl.icsvertex.scansuite.Fragments.Dialogs.FilterOrderLinesFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.NoOrdersFragment;
import nl.icsvertex.scansuite.R;

public class MoveorderSelectActivity extends AppCompatActivity implements iICSDefaultActivity, SwipeRefreshLayout.OnRefreshListener {

    //Region Public Properties

    public static final String VIEW_NAME_HEADER_IMAGE = "detail:header:imageStr";
    public static final String VIEW_NAME_HEADER_TEXT = "detail:header:text";

    //End Region Public Properties

    //Region Private Properties

    // Region Views
    private static RecyclerView recyclerViewMoveorders;
    private static ImageView toolbarImage;
    private static TextView toolbarTitle;
    private static TextView toolbarSubTitle;
    private static TextView toolbarSubTitle2;
    private static SearchView recyclerSearchView;

    private static ImageView imageViewFilter;
    private static ImageView imageViewNewOrder;

    private  static ConstraintLayout constraintFilterOrders;
    private static SwipeRefreshLayout swipeRefreshLayout;

    private static BottomSheetBehavior bottomSheetBehavior;

    public static boolean startedViaMenuBln;

    // End Region Views

    //End Region Private Properties

    //Region Default Methods

    @Override
    protected void onCreate(Bundle pvSavedInstanceState) {
        super.onCreate(pvSavedInstanceState);
        setContentView(R.layout.activity_move_orderselect);
        cBarcodeScan.pRegisterBarcodeReceiver();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cBarcodeScan.pUnregisterBarcodeReceiver();
    }

    @Override
    protected void onPause() {
        super.onPause();
        cBarcodeScan.pUnregisterBarcodeReceiver();

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
        MoveorderSelectActivity.pFillOrders();
    }


    //End Region Default Methods

    //Region iICSDefaultActivity defaults

    @Override
    public void mActivityInitialize() {

        this.mSetAppExtensions();

        this.mFindViews();

        this.mSetToolbar(getResources().getString(R.string.screentitle_moveorderselect));

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
        MoveorderSelectActivity.toolbarImage = findViewById(R.id.toolbarImage);
        MoveorderSelectActivity.toolbarTitle = findViewById(R.id.toolbarTitle);
        MoveorderSelectActivity.toolbarSubTitle = findViewById(R.id.toolbarSubtext);
        MoveorderSelectActivity.toolbarSubTitle2 = findViewById(R.id.toolbarSubtext2);
        MoveorderSelectActivity.recyclerViewMoveorders = findViewById(R.id.recyclerViewMoveorders);
        MoveorderSelectActivity.recyclerSearchView = findViewById(R.id.recyclerSearchView);
        MoveorderSelectActivity.imageViewFilter = findViewById(R.id.imageViewFilter);
        MoveorderSelectActivity.imageViewNewOrder = findViewById(R.id.imageViewNewOrder);
        MoveorderSelectActivity.constraintFilterOrders = findViewById(R.id.constraintFilterOrders);
        MoveorderSelectActivity.swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
    }

    @Override
    public void mSetToolbar(String pvScreenTitle) {
        MoveorderSelectActivity.toolbarImage.setImageResource(R.drawable.ic_menu_move);
        MoveorderSelectActivity.toolbarTitle.setText(pvScreenTitle);
        MoveorderSelectActivity.toolbarSubTitle2.setText(cUser.currentUser.currentBranch.getBranchNameStr());
        MoveorderSelectActivity.toolbarTitle.setSelected(true);
        MoveorderSelectActivity.toolbarSubTitle.setSelected(true);
        ViewCompat.setTransitionName(toolbarImage, VIEW_NAME_HEADER_IMAGE);
        ViewCompat.setTransitionName(toolbarTitle, VIEW_NAME_HEADER_TEXT);
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
        MoveorderSelectActivity.pFillOrders();
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


    public static void pFillOrders() {

        // Show that we are getting data
        cUserInterface.pShowGettingData();

        new Thread(new Runnable() {
            public void run() {
                mHandleFillOrders();
            }
        }).start();

    }

    public static void pHandleScan(cBarcodeScan pvBarcodeScan) {

        //Set filter with scanned barcodeStr if there is no prefix
        if (!cRegex.pHasPrefix(pvBarcodeScan.getBarcodeOriginalStr())) {
            //no prefix, fine
            MoveorderSelectActivity.recyclerSearchView.setQuery(pvBarcodeScan.getBarcodeOriginalStr(), true);
            MoveorderSelectActivity.recyclerSearchView.callOnClick();
            return;
        }

        // If there is a prefix, check if its a salesorder, then remove prefix en set filter
        if (cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(), cBarcodeLayout.barcodeLayoutEnu.SALESORDER)) {
            //has prefix, is salesorderStr
            MoveorderSelectActivity.recyclerSearchView.setQuery(cRegex.pStripRegexPrefixStr(pvBarcodeScan.getBarcodeOriginalStr()), true);
            MoveorderSelectActivity.recyclerSearchView.callOnClick();
            return;
        }

        //If there is a prefix but it's not a salesorder tgen do nope
        cUserInterface.pDoNope(MoveorderSelectActivity.recyclerSearchView, true, true);

    }

    public static void pMoveorderSelected(cMoveorder pvMoveorder) {

        if (!mCheckOrderIsLockableBln(pvMoveorder)) {
            cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.lockorder_order_assigned_to_another_user), R.raw.badsound);
            cUserInterface.pCheckAndCloseOpenDialogs();
            return;
        }

        // Show that we are getting data
        cUserInterface.pShowGettingData();

        //Set the current moveorder
        cMoveorder.currentMoveOrder = pvMoveorder;

        new Thread(new Runnable() {
            public void run() {
                mHandleMoveorderSelected();
            }
        }).start();

    }

    //End Region Public Methods

    // Region Private Methods

    private static void mHandleFillOrders(){


        //all Moveorders
        if (!cMoveorder.pGetMoveOrdersViaWebserviceBln(true, "", "")) {
            cUserInterface.pDoExplodingScreen(cAppExtension.context.getString(R.string.error_get_moveorders_failed), "", true, true );
            return;
        }

        if (cMoveorder.allMoveordersObl == null || cMoveorder.allMoveordersObl.size() == 0) {
            MoveorderSelectActivity.mShowNoOrdersIcon(true);
            return;
        }

        cAppExtension.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //Fill and show recycler
                MoveorderSelectActivity.mSetMoveorderRecycler(cMoveorder.allMoveordersObl);
                MoveorderSelectActivity.mShowNoOrdersIcon(false);
                if (cSharedPreferences.userFilterBln()) {
                    mApplyFilter();
                }

                cUserInterface.pHideGettingData();
            }
        });
    }

    private static void mHandleMoveorderSelected(){

        cResult hulpResult;

        //Try to lock the moveorder
        if (!MoveorderSelectActivity.mTryToLockOrderBln()) {
            MoveorderSelectActivity.pFillOrders();
            return;
        }

        //Delete the detail, so we can get them from the webservice
        if (!cMoveorder.currentMoveOrder.pDeleteDetailsBln()) {
            mStepFailed(cAppExtension.context.getString(R.string.error_couldnt_delete_details));
            return;
        }

        hulpResult = MoveorderSelectActivity.mGetOrderDetailsRst();
        if (!hulpResult.resultBln) {
            MoveorderSelectActivity.mStepFailed(hulpResult.messagesStr());
            return;
        }

        cAppExtension.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // If everything went well, then start Lines Activity
                MoveorderSelectActivity.mShowMoveLinesActivity();
            }
        });


    }

    private static cResult mGetOrderDetailsRst(){

        cResult result;

        result = new cResult();
        result.resultBln = true;

        //Get all linesInt for current order, if webservice error then stop
        if (!cMoveorder.currentMoveOrder.pGetLinesViaWebserviceBln(true)) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_movelines_failed));
            return result;
        }

        // Get all barcodes, if webservice error then stop
        if (!cMoveorder.currentMoveOrder.pGetBarcodesViaWebserviceBln(true)) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_barcodes_failed));
            return result;
        }

        // Get all line barcodes, if webservice error then stop
        if (!cMoveorder.currentMoveOrder.pGetLineBarcodesViaWebserviceBln(true)) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_line_barcodes_failed));
            return result;
        }

        // Get all comments
        if (!cMoveorder.currentMoveOrder.pGetCommentsViaWebserviceBln(true)) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_comments_failed));
            return result;
        }

        return  result;
    }

    private static void mStepFailed(String pvErrorMessageStr){
        cUserInterface.pDoExplodingScreen(pvErrorMessageStr, cMoveorder.currentMoveOrder.getOrderNumberStr(), true, true );
        cMoveorder.currentMoveOrder.pLockReleaseViaWebserviceBln();
        cUserInterface.pCheckAndCloseOpenDialogs();
        cMoveorder.currentMoveOrder = null;
    }

    private static void mShowMoveLinesActivity() {

        cUserInterface.pCheckAndCloseOpenDialogs();

        final ViewGroup container = cAppExtension.activity.findViewById(R.id.container);

        Intent intent = new Intent(cAppExtension.context, MoveLinesActivity.class);

        if (container != null) {
            View clickedOrder = container.findViewWithTag(cMoveorder.currentMoveOrder.getOrderNumberStr());
            ActivityOptionsCompat activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(cAppExtension.activity, new Pair<>(clickedOrder, MoveLinesActivity.VIEW_CHOSEN_ORDER));
            ActivityCompat.startActivity(cAppExtension.context,intent, activityOptions.toBundle());
            return;
        }

        ActivityCompat.startActivity(cAppExtension.context,intent, null);

    }

    private static void mShowCreateMoveActivity() {

        cUserInterface.pCheckAndCloseOpenDialogs();

        Intent intent = new Intent(cAppExtension.context, CreateMoveActivity.class);
        ActivityCompat.startActivity(cAppExtension.context,intent, null);
    }

    // End Region Private Methods

    // Region View Methods

    //Bottom Sheet

    private void mInitBottomSheet() {

        MoveorderSelectActivity.bottomSheetBehavior = BottomSheetBehavior.from(constraintFilterOrders);
        MoveorderSelectActivity.bottomSheetBehavior.setHideable(true);
        MoveorderSelectActivity.bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        MoveorderSelectActivity.bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
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
            MoveorderSelectActivity.bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            return;
        }

        MoveorderSelectActivity.bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

    }

    //End Bottom Sheet

    //Filter

    private  static void mApplyFilter() {

        MoveorderSelectActivity.mShowThatFiltersInUse(cSharedPreferences.userFilterBln());

        List<cMoveorder> filteredMovesObl = cMoveorder.pGetMovesWithFilterFromDatabasObl();

        MoveorderSelectActivity.mSetMoveorderRecycler(filteredMovesObl);

        if (filteredMovesObl.size() == 0) {
            mShowNoOrdersIcon(true);
        } else {
            mShowNoOrdersIcon(false);
        }

    }

    private static  void mShowThatFiltersInUse(Boolean pvFiltersInUseBln) {
        if (pvFiltersInUseBln) {
            imageViewFilter.setImageDrawable(ContextCompat.getDrawable(cAppExtension.context, R.drawable.ic_filter_filled_black_24dp));
        }
        else {
            imageViewFilter.setImageDrawable(ContextCompat.getDrawable(cAppExtension.context, R.drawable.ic_filter_black_24dp));
        }
    }

    private void mSetFilterListener() {
        MoveorderSelectActivity.imageViewFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN || bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    mShowHideBottomSheet(true);
                }
                else {
                    mShowHideBottomSheet(false);
                }
            }
        });
    }

    private void mSetSwipeRefreshListener() {
        MoveorderSelectActivity.swipeRefreshLayout.setOnRefreshListener(this);
        MoveorderSelectActivity.swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent), getResources().getColor(R.color.colorActive), getResources().getColor(R.color.colorPrimary));
    }


    // End Filter

    // Recycler View

    private static void mSetMoveorderRecycler(List<cMoveorder> pvMoveOrderObl) {

        MoveorderSelectActivity.swipeRefreshLayout.setRefreshing(false);

        if (pvMoveOrderObl == null) {
            return;
        }

        MoveorderSelectActivity.imageViewFilter.setVisibility(View.VISIBLE);

        for (Fragment fragment: cAppExtension.fragmentManager.getFragments()) {
            if (fragment instanceof NoOrdersFragment) {
                cAppExtension.fragmentManager.beginTransaction().remove(fragment).commit();
            }
        }

        MoveorderSelectActivity.recyclerViewMoveorders.setHasFixedSize(false);
        MoveorderSelectActivity.recyclerViewMoveorders.setAdapter(cMoveorder.getMoveorderAdapter());
        MoveorderSelectActivity.recyclerViewMoveorders.setLayoutManager(new LinearLayoutManager(cAppExtension.context));

        cMoveorder.getMoveorderAdapter().pFillData(pvMoveOrderObl);
    }

    private void mSetRecyclerOnScrollListener() {
        recyclerViewMoveorders.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                        MoveorderSelectActivity.recyclerSearchView.setVisibility(View.VISIBLE);
                        MoveorderSelectActivity.recyclerSearchView.setAlpha(0.0f);

                        // Start the animation
                        MoveorderSelectActivity.recyclerSearchView.animate()
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
        MoveorderSelectActivity.recyclerSearchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View pvView) {
                recyclerSearchView.setIconified(false);
            }
        });

        //query entered
        MoveorderSelectActivity.recyclerSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String pvString) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String pvQueryTextStr) {
                mApplyFilter();
                cMoveorder.getMoveorderAdapter().pSetFilter(pvQueryTextStr);
                return true;
            }
        });

    }

    // End Recycler View

    // No orders iconmS

    private static void mShowNoOrdersIcon(final Boolean pvShowBln){

        cAppExtension.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                cUserInterface.pHideGettingData();
                MoveorderSelectActivity.swipeRefreshLayout.setRefreshing(false);


                MoveorderSelectActivity.mSetToolBarTitleWithCounters();


                if (pvShowBln) {

                    recyclerViewMoveorders.setVisibility(View.INVISIBLE);

                    FragmentTransaction fragmentTransaction = cAppExtension.fragmentManager.beginTransaction();
                    NoOrdersFragment fragment = new NoOrdersFragment();
                    fragmentTransaction.replace(R.id.moveorderContainer, fragment);
                    fragmentTransaction.commit();

                    mAutoOpenCreateActivity();
                    return;
                }

                recyclerViewMoveorders.setVisibility(View.VISIBLE);

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

    private static boolean mCheckOrderIsLockableBln(cMoveorder pvMoveorder){

        //If there is no assigned user, then always oke
        if (pvMoveorder.getAssignedUserIdStr().isEmpty()) {
            return true;
        }

        //If you are allowed to unlock busy order, then no problem
        if (cSetting.GENERIC_UNLOCK_BUSY_ORDERS_ALLOWED()) {
            return true;
        }

        return cUser.currentUser.getUsernameStr().equalsIgnoreCase(pvMoveorder.getAssignedUserIdStr());


    }

    private  void mResetCurrents(){

        //Reset all current objects
        cMoveorder.currentMoveOrder = null;
    }

    private static boolean mTryToLockOrderBln(){

        cResult hulpResult = cMoveorder.currentMoveOrder.pLockViaWebserviceRst();

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
        //            if (cMoveOrder.currentMoveOrder.pFeedbackCommentObl() != null && cMoveOrder.currentMoveOrder.pFeedbackCommentObl().size() > 0 ) {
        //                //Process comments from webresult
        //                MoveorderSelectActivity.mShowCommentsFragment(cMoveOrder.currentMoveOrder.pFeedbackCommentObl(), hulpResult.messagesStr());
        //            }
        return (hulpResult.activityActionEnu != cWarehouseorder.ActivityActionEnu.Delete) &&
                (hulpResult.activityActionEnu != cWarehouseorder.ActivityActionEnu.NoStart);

    }

    private static void mSetToolBarTitleWithCounters(){


        String subTitleStr;

        if (cMoveorder.allMoveordersObl == null ) {
            MoveorderSelectActivity.toolbarSubTitle.setText("0 " + cAppExtension.activity.getString(R.string.orders));
            return;
        }

        if (!cSharedPreferences.userFilterBln()) {
            subTitleStr = cAppExtension.context.getResources().getQuantityString(R.plurals.plural_parameter1_orders, cMoveorder.allMoveordersObl.size(),cMoveorder.allMoveordersObl.size());
        } else {
            subTitleStr = "(" + cText.pIntToStringStr(cMoveorder.pGetMovesWithFilterFromDatabasObl().size())  + "/" + cText.pIntToStringStr(cMoveorder.allMoveordersObl.size()) + ") " + cAppExtension.activity.getString(R.string.orders) + " " + cAppExtension.activity.getString(R.string.shown);
        }

        MoveorderSelectActivity.toolbarSubTitle.setText(subTitleStr);

    }

    private void mReleaseLicense() {

        if (! cLicense.pReleaseLicenseViaWebserviceBln()) {
            cUserInterface.pShowSnackbarMessage(recyclerViewMoveorders, cAppExtension.activity.getString(R.string.message_license_release_error),null, false);
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

        if (cSetting.MOVE_NEW_WORKFLOWS().toUpperCase().contains(cWarehouseorder.WorkflowEnu.MV.toString().toUpperCase())) {
            imageViewNewOrder.setVisibility(View.VISIBLE);
        }
        else {
            imageViewNewOrder.setVisibility(View.INVISIBLE);
        }
    }

    private void mSetNewOrderListener() {
        MoveorderSelectActivity.imageViewNewOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View pvView) {
                mShowCreateMoveActivity();
            }
        });
    }

    private static void  mAutoOpenCreateActivity(){

        // We returned in this form, so don't start create activity
        if (!MoveorderSelectActivity.startedViaMenuBln) {
            return;
        }

        // We can't create, so don't start create activity
        if (cSetting.MOVE_NEW_WORKFLOWS().isEmpty()) {
            return;
        }

            if (!cSetting.MOVE_AUTO_CREATE_ORDER_MV()) {
                return;
            }

            mShowCreateMoveActivity();
            return;

    }

    // End No orders icon

    // End Region View Method

}

