package nl.icsvertex.scansuite.Activities.Returns;

import android.app.Activity;
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
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Basics.Settings.cSetting;
import SSU_WHS.Basics.Users.cUser;
import SSU_WHS.General.Comments.cComment;
import SSU_WHS.General.Licenses.cLicense;
import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.General.cPublicDefinitions;
import SSU_WHS.Return.ReturnOrder.cReturnorder;
import nl.icsvertex.scansuite.Activities.General.MenuActivity;
import nl.icsvertex.scansuite.Fragments.Dialogs.CommentFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.FilterOrderLinesFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.NoOrdersFragment;
import nl.icsvertex.scansuite.R;


public class ReturnorderSelectActivity extends AppCompatActivity implements iICSDefaultActivity, SwipeRefreshLayout.OnRefreshListener {

    //Region Public Properties

    public static final String VIEW_NAME_HEADER_IMAGE = "detail:header:imageStr";
    public static final String VIEW_NAME_HEADER_TEXT = "detail:header:text";

    //End Region Public Properties

    //Region Private Properties

    // Region Views
    private static RecyclerView recyclerViewReturnorders;
    private static ImageView toolbarImage;
    private static TextView toolbarTitle;
    private static TextView toolbarSubTitle;
    private static TextView toolbarSubTitle2;
    private static SearchView recyclerSearchView;
    private static SwipeRefreshLayout swipeRefreshLayout;
    private static Activity currentActivity;

    private static ImageView imageViewFilter;
    private static ImageView imageViewNewOrder;
    private static ConstraintLayout constraintFilterOrders;
    private static BottomSheetBehavior bottomSheetBehavior;

    // End Region Views

    //End Region Private Properties

    //Region Default Methods

    @Override
    protected void onCreate(Bundle pvSavedInstanceState) {
        super.onCreate(pvSavedInstanceState);
        setContentView(R.layout.activity_returnorderselect);
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
        ReturnorderSelectActivity.pFillOrders();
    }

    //End Region Default Methods

    //Region iICSDefaultActivity defaults

    @Override
    public void mActivityInitialize() {

        this.mSetAppExtensions();

        this.mFindViews();

        this.mSetToolbar(getResources().getString(R.string.screentitle_return));

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
        currentActivity = this;
    }

    @Override
    public void mFindViews() {
        ReturnorderSelectActivity.toolbarImage = findViewById(R.id.toolbarImage);
        ReturnorderSelectActivity.toolbarTitle = findViewById(R.id.toolbarTitle);
        ReturnorderSelectActivity.toolbarSubTitle = findViewById(R.id.toolbarSubtext);
        ReturnorderSelectActivity.toolbarSubTitle2 = findViewById(R.id.toolbarSubtext2);
        ReturnorderSelectActivity.recyclerViewReturnorders = findViewById(R.id.recyclerViewReturnorders);
        ReturnorderSelectActivity.recyclerSearchView = findViewById(R.id.recyclerSearchView);
        ReturnorderSelectActivity.imageViewFilter = findViewById(R.id.imageViewFilter);
        ReturnorderSelectActivity.swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        ReturnorderSelectActivity.imageViewNewOrder = findViewById(R.id.imageViewNewReturn);
        ReturnorderSelectActivity.constraintFilterOrders = findViewById(R.id.constraintFilterReturnOrders);
    }

    @Override
    public void mSetToolbar(String pvScreenTitle) {
        ReturnorderSelectActivity.toolbarImage.setImageResource(R.drawable.ic_menu_inventory);
        ReturnorderSelectActivity.toolbarTitle.setText(pvScreenTitle);
        ReturnorderSelectActivity.toolbarSubTitle2.setText(cUser.currentUser.currentBranch.getBranchNameStr());
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
        ReturnorderSelectActivity.pFillOrders();
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
        ReturnorderSelectActivity.recyclerSearchView.setQuery(pvBarcodeScan.getBarcodeOriginalStr(), true);
        ReturnorderSelectActivity.recyclerSearchView.callOnClick();
    }

    public static void pReturnorderSelected(cReturnorder pvReturnorder) {

        cResult hulpResult;

        if (!mCheckOrderIsLockableBln(pvReturnorder)) {
            cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.lockorder_order_assigned_to_another_user), R.raw.badsound);
            cUserInterface.pCheckAndCloseOpenDialogs();
            return;
        }

        // Show that we are getting data
        cUserInterface.pShowGettingData();

        //Set the current returnorder
        cReturnorder.currentReturnOrder = pvReturnorder;

        //Try to lock the returnorder

        if (!ReturnorderSelectActivity.mTryToLockOrderBln()) {
            ReturnorderSelectActivity.pFillOrders();
            return;
        }

        //Delete the detail, so we can get them from the webservice
        if (!cReturnorder.currentReturnOrder.pDeleteDetailsBln()) {
            mStepFailed(cAppExtension.context.getString(R.string.error_couldnt_delete_details));
            return;
        }

        if(!cUser.currentUser.currentBranch.pGetReasonBln(true)){
            mStepFailed(cAppExtension.context.getString(R.string.error_getting_return_reasons));
            return;
        }

        hulpResult = ReturnorderSelectActivity.mGetOrderDetailsRst();
        if (!hulpResult.resultBln) {
            ReturnorderSelectActivity.mStepFailed(hulpResult.messagesStr());
            return;
        }

        //        // If everything went well, then start Documents Activity
        ReturnorderSelectActivity.mShowReturnorderDocumentsActivity();
    }

    public static void pCreateOrder(final String pvDocumentStr, final Boolean pvMultipleDocumentsBln, final String pvBincodeStr){


        // Show that we are getting data
        cUserInterface.pShowGettingData();

        new Thread(new Runnable() {
            public void run() {
                mHandleCreateOrder(pvDocumentStr, pvMultipleDocumentsBln, pvBincodeStr);
            }
        }).start();

    }

    //End Region Public Methods

    // Region Private Methods

    private static void mHandleCreateOrder(String pvDocumentstr, Boolean pvMultipleDocumentsBln, String pvBincodeStr){

        cResult hulpResult;

        //Try to create the order
        if (!ReturnorderSelectActivity.mTryToCreateOrderBln(pvDocumentstr, pvMultipleDocumentsBln, pvBincodeStr)) {
            mStepFailed(cAppExtension.activity.getString(R.string.message_couldnt_create_order));
            ReturnorderSelectActivity.pFillOrders();
            return;
        }

        //Try to lock the order
        if (!ReturnorderSelectActivity.mTryToLockOrderBln()) {
            ReturnorderSelectActivity.pFillOrders();
            return;
        }

        //Delete the detail, so we can get them from the webservice
        if (!cReturnorder.currentReturnOrder.pDeleteDetailsBln()) {
            mStepFailed(cAppExtension.context.getString(R.string.error_couldnt_delete_details));
            ReturnorderSelectActivity.pFillOrders();
            return;
        }

        hulpResult = ReturnorderSelectActivity.mGetOrderDetailsRst();
        if (!hulpResult.resultBln) {
            ReturnorderSelectActivity.mStepFailed(hulpResult.messagesStr());
            ReturnorderSelectActivity.pFillOrders();
            return;
        }

        cAppExtension.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // If everything went well, then start Lines Activity
                ReturnorderSelectActivity.mShowReturnorderDocumentsActivity();
            }
        });

    }

    private static boolean mTryToCreateOrderBln(String pvDocumentstr, Boolean pvMultipleDocumentsBln, String pvBincodeStr){
        return  cReturnorder.pCreateReturnOrderViaWebserviceBln(pvDocumentstr, pvMultipleDocumentsBln, pvBincodeStr);
    }

    private static boolean mTryToLockOrderBln(){

        cResult hulpResult;
        hulpResult = cReturnorder.currentReturnOrder.pLockViaWebserviceRst(cWarehouseorder.StepCodeEnu.Retour, cWarehouseorder.WorkflowReturnStepEnu.Return);

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
        if ( hulpResult.activityActionEnu == cWarehouseorder.ActivityActionEnu.Delete ||
                hulpResult.activityActionEnu == cWarehouseorder.ActivityActionEnu.NoStart ) {


            //If we got any comments, show them
            if (cReturnorder.currentReturnOrder.pFeedbackCommentObl() != null && cReturnorder.currentReturnOrder.pFeedbackCommentObl().size() > 0 ) {
                //Process comments from webresult
                ReturnorderSelectActivity.mShowCommentsFragment(cReturnorder.currentReturnOrder.pFeedbackCommentObl(), hulpResult.messagesStr());
            }

            return  false;
        }


        return true;

    }

    private static void mHandleFillOrders(){

        if (!cReturnorder.pGetReturnOrdersViaWebserviceBln(true, "")) {
            cUserInterface.pDoExplodingScreen(cAppExtension.context.getString(R.string.error_get_returnorders_failed), "", true, true );
            return;
        }

        if (cReturnorder.allReturnordersObl == null || cReturnorder.allReturnordersObl.size() == 0) {
            ReturnorderSelectActivity.mShowNoOrdersIcon(true);
            return;
        }

        cAppExtension.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //Fill and show recycler
                ReturnorderSelectActivity.mSetReturnorderRecycler(cReturnorder.allReturnordersObl);
                ReturnorderSelectActivity.mShowNoOrdersIcon(false);
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

        //Get all linesInt for current order, if size = 0 or webservice error then stop
        if (!cReturnorder.currentReturnOrder.pGetLinesViaWebserviceBln(true)) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_returnorderlines_failed));
            return result;
        }

        // Get all comments
        if (!cReturnorder.currentReturnOrder.pGetCommentsViaWebserviceBln(true)) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_comments_failed));
            return result;
        }
        //Get all barcodes
        if (!cReturnorder.currentReturnOrder.pGetBarcodesViaWebserviceBln(true)) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_barcodes_failed));
            return result;
        }
        //Get all ReturnHandledlines
        if (!cReturnorder.currentReturnOrder.pGetHandledLinesViaWebserviceBln(true)){
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_returnorderlines_failed));
            return result;
        }

        //Get all Returnlinebarcodes
        if (!cReturnorder.currentReturnOrder.pGetLineBarcodesViaWebserviceBln(true)) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_line_barcodes_failed));
            return result;
        }
        if(!cReturnorder.currentReturnOrder.pDocumentsViaWebserviceBln(true)){
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_returnordersdocuments_failed));
            return result;
        }

        return  result;
    }

    // End Region Private Methods

    // Region View Methods

    //Bottom Sheet

    private void mInitBottomSheet() {

        ReturnorderSelectActivity.bottomSheetBehavior = BottomSheetBehavior.from(constraintFilterOrders);
        ReturnorderSelectActivity.bottomSheetBehavior.setHideable(true);
        ReturnorderSelectActivity.bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        ReturnorderSelectActivity.bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
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
        cAppExtension.fragmentManager.beginTransaction().replace(R.id.constraintFilterReturnOrders, new FilterOrderLinesFragment()).commit();

    }

    private void mShowHideBottomSheet(Boolean pvShowBln) {

        if (pvShowBln) {
            ReturnorderSelectActivity.bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            return;
        }

        ReturnorderSelectActivity.bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

    }

    //End Bottom Sheet

    //Filter

    private  static void mApplyFilter() {

        ReturnorderSelectActivity.mShowThatFiltersInUse(cSharedPreferences.userFilterBln());

        List<cReturnorder> returnorderObl = cReturnorder.pGetReturnOrdersWithFilterFromDatabasObl();

        ReturnorderSelectActivity.mSetReturnorderRecycler(returnorderObl);

        if (returnorderObl.size() == 0) {
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
        ReturnorderSelectActivity.imageViewFilter.setOnClickListener(new View.OnClickListener() {
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

    private void mSetToolbarTitleListeners() {

        ReturnorderSelectActivity.toolbarTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mScrollToTop();
            }
        });

        ReturnorderSelectActivity.toolbarTitle.setOnLongClickListener(new View.OnLongClickListener() {
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
        ReturnorderSelectActivity.recyclerViewReturnorders.smoothScrollToPosition(0);
    }

    private void mScrollToBottom() {
        if (cReturnorder.getReturnorderAdapter() != null) {
            if (cReturnorder.getReturnorderAdapter().getItemCount() > 0) {
                ReturnorderSelectActivity.recyclerViewReturnorders.smoothScrollToPosition(cReturnorder.getReturnorderAdapter().getItemCount() - 1);
            }
        }
    }

    private static void mSetReturnorderRecycler(List<cReturnorder> pvReturnorderObl) {

        swipeRefreshLayout.setRefreshing(false);

        if (pvReturnorderObl == null) {
            return;
        }

        ReturnorderSelectActivity.imageViewFilter.setVisibility(View.VISIBLE);

        for (Fragment fragment: cAppExtension.fragmentManager.getFragments()) {
            if (fragment instanceof NoOrdersFragment) {
                cAppExtension.fragmentManager.beginTransaction().remove(fragment).commit();
            }
        }

        ReturnorderSelectActivity.recyclerViewReturnorders.setHasFixedSize(false);
        ReturnorderSelectActivity.recyclerViewReturnorders.setAdapter(cReturnorder.getReturnorderAdapter());
        ReturnorderSelectActivity.recyclerViewReturnorders.setLayoutManager(new LinearLayoutManager(cAppExtension.context));

        cReturnorder.getReturnorderAdapter().pFillData(pvReturnorderObl);
    }

    private void mSetRecyclerOnScrollListener() {
        ReturnorderSelectActivity.recyclerViewReturnorders.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                        ReturnorderSelectActivity.recyclerSearchView.setVisibility(View.VISIBLE);
                        ReturnorderSelectActivity.recyclerSearchView.setAlpha(0.0f);

                        // Start the animation
                        ReturnorderSelectActivity.recyclerSearchView.animate()
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
        ReturnorderSelectActivity.recyclerSearchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View pvView) {
                ReturnorderSelectActivity.recyclerSearchView.setIconified(false);
            }
        });

        //query entered
        ReturnorderSelectActivity.recyclerSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String pvString) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String pvQueryTextStr) {
                mApplyFilter();
                cReturnorder.getReturnorderAdapter().pSetFilter(pvQueryTextStr);
                return true;
            }
        });


    }

    // End Recycler View

    private void mSetNewOrderListener() {
        ReturnorderSelectActivity.imageViewNewOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View pvView) {
                mShowCreateReturnorderActivity();
            }
        });
    }

    private void mSetSwipeRefreshListener() {
        ReturnorderSelectActivity.swipeRefreshLayout.setOnRefreshListener(this);
        ReturnorderSelectActivity.swipeRefreshLayout.setSize(SwipeRefreshLayout.LARGE);
        ReturnorderSelectActivity.swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent), getResources().getColor(R.color.colorActive), getResources().getColor(R.color.colorPrimary));
    }


    // No orders icon
    private static void mShowNoOrdersIcon(final Boolean pvShowBln){

        cAppExtension.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                cUserInterface.pHideGettingData();
                ReturnorderSelectActivity.swipeRefreshLayout.setRefreshing(false);
                ReturnorderSelectActivity.mSetToolBarTitleWithCounters();

                if (pvShowBln) {

                    recyclerViewReturnorders.setVisibility(View.INVISIBLE);

                    FragmentTransaction fragmentTransaction = cAppExtension.fragmentManager.beginTransaction();
                    NoOrdersFragment fragment = new NoOrdersFragment();
                    fragmentTransaction.replace(R.id.returnorderContainer, fragment);
                    fragmentTransaction.commit();
                    return;
                }

                recyclerViewReturnorders.setVisibility(View.VISIBLE);

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

    private static boolean mCheckOrderIsLockableBln(cReturnorder pvReturnorder){

        //If there is no assigned user, then always oke
        if (pvReturnorder.getAssignedUserIdStr().isEmpty()) {
            return true;
        }

        //If you are allowed to unlock busy order, then no problem
        if (cSetting.GENERIC_UNLOCK_BUSY_ORDERS_ALLOWED()) {
            return true;
        }

        return cUser.currentUser.getUsernameStr().equalsIgnoreCase(pvReturnorder.getAssignedUserIdStr());

    }

    private  void mResetCurrents(){

        //Reset all current objects
        cReturnorder.currentReturnOrder = null;

    }

    private void mSetNewOrderButton() {

        if (cSetting.RETOUR_NEW_WORKFLOWS().toUpperCase().contains(cWarehouseorder.WorkflowEnu.RVS.toString().toUpperCase())) {
            ReturnorderSelectActivity.imageViewNewOrder.setVisibility(View.VISIBLE);
        }
        else {
            ReturnorderSelectActivity.imageViewNewOrder.setVisibility(View.INVISIBLE);
        }
    }

    private static void mStepFailed(String pvErrorMessageStr){
        cUserInterface.pDoExplodingScreen(pvErrorMessageStr, cReturnorder.currentReturnOrder.getOrderNumberStr(), true, true );
        cReturnorder.currentReturnOrder.pLockReleaseViaWebserviceBln(cWarehouseorder.StepCodeEnu.Retour, cWarehouseorder.WorkflowReturnStepEnu.Return);
        cUserInterface.pCheckAndCloseOpenDialogs();
        cReturnorder.currentReturnOrder = null;
    }

    private void mShowCreateReturnorderActivity() {

        cUserInterface.pCheckAndCloseOpenDialogs();

        Intent intent = new Intent(cAppExtension.context, CreateReturnActivity.class);

        ActivityOptionsCompat activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(cAppExtension.activity);
        ActivityCompat.startActivity(cAppExtension.context,intent, activityOptions.toBundle());

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

    private static void mShowReturnorderDocumentsActivity() {

        cUserInterface.pCheckAndCloseOpenDialogs();

        cAppExtension.activity = ReturnorderSelectActivity.currentActivity;

        final ViewGroup container = cAppExtension.activity.findViewById(R.id.returnorderSelectcontainer);
        View clickedOrder = container.findViewWithTag(cReturnorder.currentReturnOrder.getOrderNumberStr());

        Intent intent = new Intent(cAppExtension.context, ReturnorderDocumentsActivity.class);

        if (clickedOrder != null) {
            ActivityOptionsCompat activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(cAppExtension.activity, new Pair<>(clickedOrder, ReturnorderDocumentsActivity.VIEW_CHOSEN_ORDER));
            ActivityCompat.startActivity(cAppExtension.context,intent, activityOptions.toBundle());
        } else {
            cAppExtension.activity.startActivity(intent);
            cAppExtension.activity.finish();
        }
    }

    private void mReleaseLicense() {

        if (! cLicense.pReleaseLicenseViaWebserviceBln()) {
            cUserInterface.pShowSnackbarMessage(recyclerViewReturnorders, cAppExtension.activity.getString(R.string.message_license_release_error),null, false);
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


        if (cReturnorder.allReturnordersObl== null ) {
            ReturnorderSelectActivity.toolbarSubTitle.setText("(0)");
            return;
        }

        if (!cSharedPreferences.userFilterBln()) {
            ReturnorderSelectActivity.toolbarSubTitle.setText(cReturnorder.getNumberOfOrdersStr());
        } else {
            ReturnorderSelectActivity.toolbarSubTitle.setText(cReturnorder.getNumberOfFilteredOrdersStr());
        }
    }

    // End No orders icon

    // End Region View Method

}

