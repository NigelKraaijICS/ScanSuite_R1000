package nl.icsvertex.scansuite.Activities.Inventory;

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
import ICS.Utils.cResult;
import ICS.Utils.cSharedPreferences;
import ICS.Utils.cText;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Basics.Settings.cSetting;
import SSU_WHS.Basics.Users.cUser;
import SSU_WHS.General.Comments.cComment;
import SSU_WHS.General.Licenses.cLicense;
import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.General.cPublicDefinitions;
import SSU_WHS.Inventory.InventoryOrders.cInventoryorder;
import nl.icsvertex.scansuite.Activities.General.MenuActivity;
import nl.icsvertex.scansuite.Fragments.Dialogs.FilterOrderLinesFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.NoOrdersFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.CommentFragment;
import nl.icsvertex.scansuite.Fragments.Inventory.CreateInventoryFragment;
import nl.icsvertex.scansuite.R;


public class InventoryorderSelectActivity extends AppCompatActivity implements iICSDefaultActivity, SwipeRefreshLayout.OnRefreshListener {

    //Region Public Properties

    public static final String VIEW_NAME_HEADER_IMAGE = "detail:header:imageStr";
    public static final String VIEW_NAME_HEADER_TEXT = "detail:header:text";

    //End Region Public Properties

    //Region Private Properties

    // Region Views
    private static RecyclerView recyclerViewInventoryorders;
    private ImageView toolbarImage;
    private TextView toolbarTitle;
    private static TextView toolbarSubTitle;
    private static SearchView recyclerSearchView;
    private static SwipeRefreshLayout swipeRefreshLayout;

    private static ImageView imageViewFilter;
    private ImageView imageViewNewOrder;
    private ConstraintLayout constraintFilterOrders;
    private BottomSheetBehavior bottomSheetBehavior;

    private static Boolean orderDetailsCompleteBln;
    // End Region Views

    //End Region Private Properties

    //Region Default Methods

    @Override
    protected void onCreate(Bundle pvSavedInstanceState) {
        super.onCreate(pvSavedInstanceState);
        setContentView(R.layout.activity_inventoryorderselect);
        this.mActivityInitialize();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        cBarcodeScan.pUnregisterBarcodeReceiver();

    }

    @Override
    protected void onResume() {
        super.onResume();
        cBarcodeScan.pRegisterBarcodeReceiver();
        cUserInterface.pEnableScanner();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.mLeaveActivity();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        this.mLeaveActivity();
    }

    @Override
    public void onRefresh() {
        InventoryorderSelectActivity.pFillOrders();
    }

    //End Region Default Methods

    //Region iICSDefaultActivity defaults

    @Override
    public void mActivityInitialize() {

        this.mSetAppExtensions();

        this.mFindViews();

        this.mSetToolbar(getResources().getString(R.string.screentitle_inventoryorderselect));

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
        this.toolbarImage = findViewById(R.id.toolbarImage);
        this.toolbarTitle = findViewById(R.id.toolbarTitle);
        this.toolbarSubTitle = findViewById(R.id.toolbarSubtext);
        this.recyclerViewInventoryorders = findViewById(R.id.recyclerViewInventoryorders);
        this.recyclerSearchView = findViewById(R.id.recyclerSearchView);
        this.imageViewFilter = findViewById(R.id.imageViewFilter);
        this.swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        this.imageViewNewOrder = findViewById(R.id.imageViewBinDone);
        this.constraintFilterOrders = findViewById(R.id.constraintFilterOrders);
    }

    @Override
    public void mSetToolbar(String pvScreenTitle) {
        this.toolbarImage.setImageResource(R.drawable.ic_menu_inventory);
        this.toolbarTitle.setText(pvScreenTitle);
        toolbarTitle.setSelected(true);
        toolbarSubTitle.setSelected(true);
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
        InventoryorderSelectActivity.pFillOrders();
    }

    @Override
    public void mSetListeners() {
        this.mSetRecyclerOnScrollListener();
        this.mSetSearchListener();
        this.mSetFilterListener();
        this.mSetToolbarTitleListeners();
        this.mSetNewOrderListener();
        this.mSetSwipeRefreshListener();
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

        InventoryorderSelectActivity.recyclerSearchView.setQuery(pvBarcodeScan.getBarcodeOriginalStr(), true);
        InventoryorderSelectActivity.recyclerSearchView.callOnClick();
        return;

    }

    public static void pInventoryorderSelected(cInventoryorder pvInventoryorder) {

        cResult hulpResult;

        if (mCheckOrderIsLockableBln(pvInventoryorder) == false) {
            cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.lockorder_order_assigned_to_another_user), R.raw.badsound);
            cUserInterface.pCheckAndCloseOpenDialogs();
            return;
        }

        // Show that we are getting data
        cUserInterface.pShowGettingData();

        //Set the current inventoryorder
        cInventoryorder.currentInventoryOrder = pvInventoryorder;

        //Try to lock the inventoryorder
        if (InventoryorderSelectActivity.mTryToLockOrderBln() == false) {
            InventoryorderSelectActivity.pFillOrders();
            return;
        }

        //Delete the detail, so we can get them from the webservice
       if (cInventoryorder.currentInventoryOrder.pDeleteDetailsBln() == false) {
           mStepFailed(cAppExtension.context.getString(R.string.error_couldnt_delete_details));
           return;
       }

        hulpResult = InventoryorderSelectActivity.mGetOrderDetailsRst();
        if (hulpResult.resultBln == false ) {
            InventoryorderSelectActivity.mStepFailed(hulpResult.messagesStr());
            return;
        }

        //If everything went well, then start Bins Activity
        InventoryorderSelectActivity.mShowInventoryorderBinsActivity();
        return;

    }

    public static void pCreateOrder(final String pvDocumentStr){


        // Show that we are getting data
        cUserInterface.pShowGettingData();

        new Thread(new Runnable() {
            public void run() {
                mHandleCreateOrder(pvDocumentStr);
            }
        }).start();

    }

    //End Region Public Methods

    // Region Private Methods

    private static void mHandleCreateOrder(String pvDocumentstr){

        cResult hulpResult;

        //Try to create the order
        if (InventoryorderSelectActivity.mTryToCreateOrderBln(pvDocumentstr) == false) {
            InventoryorderSelectActivity.orderDetailsCompleteBln = false;
            InventoryorderSelectActivity.pFillOrders();
            return;
        }

        //Try to lock the order
        if (InventoryorderSelectActivity.mTryToLockOrderBln() == false) {
            InventoryorderSelectActivity.orderDetailsCompleteBln = false;
            InventoryorderSelectActivity.pFillOrders();
            return;
        }

        //Delete the detail, so we can get them from the webservice
        if (cInventoryorder.currentInventoryOrder.pDeleteDetailsBln() == false) {
            mStepFailed(cAppExtension.context.getString(R.string.error_couldnt_delete_details));
            InventoryorderSelectActivity.orderDetailsCompleteBln = false;
            InventoryorderSelectActivity.pFillOrders();
            return;
        }

        hulpResult = InventoryorderSelectActivity.mGetOrderDetailsRst();
        if (hulpResult.resultBln == false ) {
            InventoryorderSelectActivity.mStepFailed(hulpResult.messagesStr());
            InventoryorderSelectActivity.orderDetailsCompleteBln = false;
            InventoryorderSelectActivity.pFillOrders();
            return;
        }

        cAppExtension.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // If everything went well, then start Lines Activity
                InventoryorderSelectActivity.mShowInventoryorderBinsActivity();
            }
        });

    }

    private static boolean mTryToCreateOrderBln(String pvDocumentstr){

        Boolean resultBln = false;

        resultBln =  cInventoryorder.pCreateInventoryOrderViaWebserviceBln(pvDocumentstr);
        if (!resultBln) {
            mStepFailed(cAppExtension.activity.getString(R.string.message_couldnt_create_order));
            return  false;
        }

        return resultBln;

    }

    private static boolean mTryToLockOrderBln(){

        cResult hulpResult;
        hulpResult = cInventoryorder.currentInventoryOrder.pLockViaWebserviceRst(cWarehouseorder.StepCodeEnu.Inventory, cWarehouseorder.WorkflowInventoryStepEnu.Inventory);

        //Everything was fine, so we are done
        if (hulpResult.resultBln == true) {
            return true;
        }

        //Something went wrong, but no further actions are needed, so ony show reason of failure
        if (hulpResult.resultBln == false  && hulpResult.activityActionEnu == cWarehouseorder.ActivityActionEnu.Unknown ) {
            mStepFailed(hulpResult.messagesStr());
            return  false;
        }

        //Something went wrong, the order has been deleted, so show comments and refresh
        if (hulpResult.resultBln == false  && hulpResult.activityActionEnu == cWarehouseorder.ActivityActionEnu.Delete ||
                hulpResult.resultBln == false  && hulpResult.activityActionEnu == cWarehouseorder.ActivityActionEnu.NoStart ) {


            //If we got any comments, show them
            if (cInventoryorder.currentInventoryOrder.pFeedbackCommentObl() != null && cInventoryorder.currentInventoryOrder.pFeedbackCommentObl().size() > 0 ) {
                //Process comments from webresult
                InventoryorderSelectActivity.mShowCommentsFragment(cInventoryorder.currentInventoryOrder.pFeedbackCommentObl(), hulpResult.messagesStr());
            }

            return  false;
        }


        return true;

    }

    private static void mHandleFillOrders(){

        if (cInventoryorder.pGetInventoryOrdersViaWebserviceBln(true,"") == false) {
            cUserInterface.pDoExplodingScreen(cAppExtension.context.getString(R.string.error_get_inventoryorders_failed), "", true, true );
            return;
        }

        if (cInventoryorder.allInventoryordersObl == null || cInventoryorder.allInventoryordersObl.size() == 0) {
            InventoryorderSelectActivity.mShowNoOrdersIcon(true);
            return;
        }

        cAppExtension.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //Fill and show recycler
                InventoryorderSelectActivity.mSetInventoryorderRecycler(cInventoryorder.allInventoryordersObl);
                InventoryorderSelectActivity.mShowNoOrdersIcon(false);
                if (cSharedPreferences.userFilterBln()) {
                    mApplyFilter();
                }

                cUserInterface.pHideGettingData();
            }
        });
    }

    private static cResult mGetOrderDetailsRst(){

        cResult result;

        result = new cResult();
        result.resultBln = true;

        //Get all bins for current order, if webservice error then stop
        if (cInventoryorder.currentInventoryOrder.pGetBinsViaWebserviceBln(true) == false) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_picklines_failed));
            return result;
        }

        //Get all linesInt for current order, if size = 0 or webservice error then stop
        if (cInventoryorder.currentInventoryOrder.pGetLinesViaWebserviceBln(true) == false) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_inventorylines_failed));
            return result;
        }

        // Get all comments
        if (cInventoryorder.currentInventoryOrder.pGetCommentsViaWebserviceBln(true) == false) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_comments_failed));
            return result;
        }
        //Get all barcodes
        if (!cInventoryorder.currentInventoryOrder.pGetBarcodesViaWebserviceBln(true)) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_barcodes_failed));
            return result;
        }
        //Get all inventorylinebarcodes
        if (!cInventoryorder.currentInventoryOrder.pGetLineBarcodesViaWebserviceBln(true)) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_line_barcodes_failed));
            return result;
        }

        // Get all article images, only if neccesary
        if (!cInventoryorder.currentInventoryOrder.pGetArticleImagesViaWebserviceBln(true)) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_article_images_failed));
            return result;
        }

        return  result;
    }

    // End Region Private Methods

    // Region View Methods

    //Bottom Sheet

    private void mInitBottomSheet() {

        this.bottomSheetBehavior = BottomSheetBehavior.from(constraintFilterOrders);
        this.bottomSheetBehavior.setHideable(true);
        this.bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        this.bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
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

        if (pvShowBln == true) {
            this.bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            return;
        }

        this.bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

    }

    //End Bottom Sheet

    //Filter

    private  static void mApplyFilter() {

        InventoryorderSelectActivity.mShowThatFiltersInUse(cSharedPreferences.userFilterBln());

        List<cInventoryorder> filteredPicksObl = cInventoryorder.pGetInventoriesWithFilterFromDatabasObl();

        InventoryorderSelectActivity.mSetInventoryorderRecycler(filteredPicksObl);

        if (filteredPicksObl.size() == 0) {
            mShowNoOrdersIcon(true);
        } else {
            mShowNoOrdersIcon(false);
        }

    }

    private static void mShowThatFiltersInUse(Boolean pvFiltersInUseBln) {
        if (pvFiltersInUseBln) {
            imageViewFilter.setImageDrawable(ContextCompat.getDrawable(cAppExtension.context, R.drawable.ic_filter_filled_black_24dp));
        }
        else {
            imageViewFilter.setImageDrawable(ContextCompat.getDrawable(cAppExtension.context, R.drawable.ic_filter_black_24dp));
        }
    }

    private void mSetFilterListener() {
        this.imageViewFilter.setOnClickListener(new View.OnClickListener() {
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
        return;
    }

    private void mSetToolbarTitleListeners() {
        this.toolbarTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mScrollToTop();
            }
        });
        this.toolbarTitle.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mScrollToBottom();
                return true;
            }
        });
    }

    // End Filter

    // Recycler View
    private void mScrollToTop() {
        recyclerViewInventoryorders.smoothScrollToPosition(0);
    }

    private void mScrollToBottom() {
        if (cInventoryorder.getInventoryorderAdapter() != null) {
            if (cInventoryorder.getInventoryorderAdapter().getItemCount() > 0) {
                recyclerViewInventoryorders.smoothScrollToPosition(cInventoryorder.getInventoryorderAdapter().getItemCount() - 1);
            }
        }
    }

    private static void mSetInventoryorderRecycler(List<cInventoryorder> pvInventoryorderObl) {

        swipeRefreshLayout.setRefreshing(false);

        if (pvInventoryorderObl == null) {
            return;
        }

        InventoryorderSelectActivity.imageViewFilter.setVisibility(View.VISIBLE);

        for (Fragment fragment: cAppExtension.fragmentManager.getFragments()) {
            if (fragment instanceof NoOrdersFragment) {
                cAppExtension.fragmentManager.beginTransaction().remove(fragment).commit();
            }
        }

        InventoryorderSelectActivity.recyclerViewInventoryorders.setHasFixedSize(false);
        InventoryorderSelectActivity.recyclerViewInventoryorders.setAdapter(cInventoryorder.getInventoryorderAdapter());
        InventoryorderSelectActivity.recyclerViewInventoryorders.setLayoutManager(new LinearLayoutManager(cAppExtension.context));

        cInventoryorder.getInventoryorderAdapter().pFillData(pvInventoryorderObl);
        return;
    }

    private void mSetRecyclerOnScrollListener() {
        recyclerViewInventoryorders.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                        InventoryorderSelectActivity.recyclerSearchView.setVisibility(View.VISIBLE);
                        InventoryorderSelectActivity.recyclerSearchView.setAlpha(0.0f);

                        // Start the animation
                        InventoryorderSelectActivity.recyclerSearchView.animate()
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
                cInventoryorder.getInventoryorderAdapter().pSetFilter(pvQueryTextStr);
                return true;
            }
        });

        return;

    }

    // End Recycler View

    private void mSetNewOrderListener() {
       this.imageViewNewOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View pvView) {
                mShowCreateInventoryFragment(true);
            }
        });
    }

    private void mSetSwipeRefreshListener() {
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setSize(SwipeRefreshLayout.LARGE);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent), getResources().getColor(R.color.colorActive), getResources().getColor(R.color.colorPrimary));
    }


    // No orders icon
    private static void mShowNoOrdersIcon(final Boolean pvShowBln){

        cAppExtension.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                cUserInterface.pHideGettingData();
                InventoryorderSelectActivity.swipeRefreshLayout.setRefreshing(false);
                InventoryorderSelectActivity.mSetToolBarTitleWithCounters();

                if (pvShowBln) {

                    recyclerViewInventoryorders.setVisibility(View.INVISIBLE);

                    FragmentTransaction fragmentTransaction = cAppExtension.fragmentManager.beginTransaction();
                    NoOrdersFragment fragment = new NoOrdersFragment();
                    fragmentTransaction.replace(R.id.inventoryorderContainer, fragment);
                    fragmentTransaction.commit();
                    return;
                }

                recyclerViewInventoryorders.setVisibility(View.VISIBLE);

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

    private static boolean mCheckOrderIsLockableBln(cInventoryorder pvInventoryorder){

        //If there is no assigned user, then always oke
        if (pvInventoryorder.getAssignedUserIdStr().isEmpty()) {
            return true;
        }

        //If you are allowed to unlock busy order, then no problem
        if (cSetting.GENERIC_UNLOCK_BUSY_ORDERS_ALLOWED() == true) {
            return true;
        }

        if (cUser.currentUser.getNameStr().equalsIgnoreCase(pvInventoryorder.getAssignedUserIdStr())) {
            return  true;
        }

        return  false;

    }

    private  void mResetCurrents(){

        //Reset all current objects
        cInventoryorder.currentInventoryOrder = null;

    }

    private void mSetNewOrderButton() {

        if (cSetting.INV_NEW_WORKFLOWS().toUpperCase().contains(cWarehouseorder.WorkflowEnu.IVS.toString().toUpperCase())) {
            imageViewNewOrder.setVisibility(View.VISIBLE);
        }
        else {
            imageViewNewOrder.setVisibility(View.INVISIBLE);
        }
    }

    private static void mStepFailed(String pvErrorMessageStr){
        cUserInterface.pDoExplodingScreen(pvErrorMessageStr, cInventoryorder.currentInventoryOrder.getOrderNumberStr(), true, true );
        cInventoryorder.currentInventoryOrder.pLockReleaseViaWebserviceBln(cWarehouseorder.StepCodeEnu.Inventory, cWarehouseorder.WorkflowPickStepEnu.PickPicking);
        cUserInterface.pCheckAndCloseOpenDialogs();
        cInventoryorder.currentInventoryOrder = null;
        return ;
    }

    private void mShowCreateInventoryFragment(Boolean pvShowDocumentInputBln) {
        final CreateInventoryFragment createInventoryFragment = new CreateInventoryFragment(pvShowDocumentInputBln);
        createInventoryFragment.setCancelable(false);
        createInventoryFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.ORDERDONE_TAG);
    }

    private static void mShowCommentsFragment(List<cComment> pvDataObl, String pvTitleStr) {

        cUserInterface.pCheckAndCloseOpenDialogs();

        Bundle bundle = new Bundle();
        bundle.putString(cPublicDefinitions.KEY_COMMENTHEADER, pvTitleStr);

        CommentFragment commentFragment = new CommentFragment(pvDataObl);
        commentFragment.setArguments(bundle);

        commentFragment.show(cAppExtension.fragmentManager , cPublicDefinitions.COMMENTFRAGMENT_TAG);
        cUserInterface.pPlaySound(R.raw.message, 0);
        return;
    }

    private static void mShowInventoryorderBinsActivity() {

        cUserInterface.pCheckAndCloseOpenDialogs();

        final ViewGroup container = cAppExtension.activity.findViewById(R.id.container);

        Intent intent = new Intent(cAppExtension.context, InventoryorderBinsActivity.class);
        View clickedOrder = container.findViewWithTag(cInventoryorder.currentInventoryOrder.getOrderNumberStr());
        if (clickedOrder != null) {
            ActivityOptionsCompat activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(cAppExtension.activity, new Pair<>(clickedOrder, InventoryorderBinsActivity.VIEW_CHOSEN_ORDER));
            ActivityCompat.startActivity(cAppExtension.context,intent, activityOptions.toBundle());
            return;
        } else {
            cAppExtension.activity.startActivity(intent);
            cAppExtension.activity.finish();
        }
    }

    private void mReleaseLicense() {

        if (! cLicense.pReleaseLicenseViaWebserviceBln()) {
            cUserInterface.pShowSnackbarMessage(recyclerViewInventoryorders, cAppExtension.activity.getString(R.string.message_license_release_error),null, false);
        }

        cLicense.currentLicenseEnu = cLicense.LicenseEnu.Unknown;

    }

    private void mLeaveActivity(){
        this.mReleaseLicense();


        Intent intent = new Intent(cAppExtension.context, MenuActivity.class);
        cAppExtension.activity.startActivity(intent);
        cAppExtension.activity.finish();

    }

    private static void mSetToolBarTitleWithCounters(){


        if (cInventoryorder.allInventoryordersObl == null ) {
            InventoryorderSelectActivity.toolbarSubTitle.setText("(0)");
            return;
        }
        String subtitleStr;
        if (!cSharedPreferences.userFilterBln()) {
            subtitleStr = (cText.pIntToStringStr(cInventoryorder.allInventoryordersObl.size()) + " " + cAppExtension.activity.getString(R.string.orders)   );
        } else {
            subtitleStr = cText.pIntToStringStr(cInventoryorder.pGetInventoriesWithFilterFromDatabasObl().size())  + "/" + cText.pIntToStringStr(cInventoryorder.allInventoryordersObl.size()) + " " + cAppExtension.activity.getString(R.string.orders) + " " + cAppExtension.activity.getString(R.string.shown);
        }
        InventoryorderSelectActivity.toolbarSubTitle.setText(subtitleStr);
    }

    // End No orders icon

    // End Region View Method

}

