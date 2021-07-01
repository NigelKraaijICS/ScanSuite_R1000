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
import com.google.firebase.crashlytics.FirebaseCrashlytics;

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
import SSU_WHS.Return.ReturnOrder.cReturnorderAdapter;
import nl.icsvertex.scansuite.Activities.General.MenuActivity;
import nl.icsvertex.scansuite.Fragments.Dialogs.CommentFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.FilterOrderLinesFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.NoOrdersFragment;
import nl.icsvertex.scansuite.R;


public class ReturnorderSelectActivity extends AppCompatActivity implements iICSDefaultActivity, SwipeRefreshLayout.OnRefreshListener {

    //Region Public Properties

    //End Region Public Properties

    //Region Private Properties

    // Region Views

    private RecyclerView recyclerViewReturnorders;

    private ImageView toolbarImage;
    private TextView toolbarTitle;
    private TextView toolbarSubTitle;
    private TextView toolbarSubTitle2;

    private SearchView recyclerSearchView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Activity currentActivity;

    private ImageView imageViewFilter;
    private ImageView imageViewNewOrder;
    private ConstraintLayout constraintFilterOrders;
    private BottomSheetBehavior bottomSheetBehavior;

    private cReturnorderAdapter returnorderAdapter;
    private cReturnorderAdapter getReturnorderAdapter(){
        if (this.returnorderAdapter == null) {
            this.returnorderAdapter = new cReturnorderAdapter();
        }

        return  this.returnorderAdapter;
    }

    public static boolean startedViaMenuBln;

    // End Region Views

    //End Region Private Properties

    //Region Default Methods

    @Override
    protected void onCreate(Bundle pvSavedInstanceState) {
        super.onCreate(pvSavedInstanceState);
        setContentView(R.layout.activity_returnorderselect);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        cBarcodeScan.pUnregisterBarcodeReceiver(this.getClass().getSimpleName());

    }

    @Override
    protected void onResume() {
        super.onResume();
        this.mActivityInitialize();
        cBarcodeScan.pRegisterBarcodeReceiver(this.getClass().getSimpleName());
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

        this.mSetToolbar(getResources().getString(R.string.screentitle_return));

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
        currentActivity = this;
    }

    @Override
    public void mFindViews() {
        this.toolbarImage = findViewById(R.id.toolbarImage);
        this.toolbarTitle = findViewById(R.id.toolbarTitle);
        this.toolbarSubTitle = findViewById(R.id.toolbarSubtext);
        this.toolbarSubTitle2 = findViewById(R.id.toolbarSubtext2);
        this.recyclerViewReturnorders = findViewById(R.id.recyclerViewReturnorders);
        this.recyclerSearchView = findViewById(R.id.recyclerSearchView);
        this.imageViewFilter = findViewById(R.id.imageViewFilter);
        this.swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        this.imageViewNewOrder = findViewById(R.id.imageViewNewReturn);
        this.constraintFilterOrders = findViewById(R.id.constraintFilterReturnOrders);
    }

    @Override
    public void mSetToolbar(String pvScreenTitleStr) {

        if (cUser.currentUser.currentAuthorisation.getCustomAuthorisation() != null) {
            this.toolbarImage.setImageBitmap(cUser.currentUser.currentAuthorisation.customImageBmp());
            this.toolbarTitle.setText(cUser.currentUser.currentAuthorisation.getCustomAuthorisation().getDescriptionStr());
        }
        else {
            this.toolbarImage.setImageResource(R.drawable.ic_menu_return);
            this.toolbarTitle.setText(pvScreenTitleStr);
        }

        this.toolbarSubTitle2.setText(cUser.currentUser.currentBranch.getBranchNameStr());
        ViewCompat.setTransitionName(this.toolbarImage, cPublicDefinitions.VIEW_NAME_HEADER_IMAGE);
        ViewCompat.setTransitionName(this.toolbarTitle, cPublicDefinitions.VIEW_NAME_HEADER_TEXT);
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
        this.mSetToolbarTitleListeners();
        this.mSetNewOrderListener();
        this.mSetSwipeRefreshListener();
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
        this.recyclerSearchView.setQuery(pvBarcodeScan.getBarcodeOriginalStr(), true);
        this.recyclerSearchView.callOnClick();
    }

    public  void pReturnorderSelected(final cReturnorder pvReturnorder) {

        // Show that we are getting data
        cUserInterface.pShowGettingData();

        cReturnorder.currentReturnOrder = pvReturnorder;
        FirebaseCrashlytics.getInstance().setCustomKey("Ordernumber", cReturnorder.currentReturnOrder.getOrderNumberStr());

        new Thread(this::mHandleReturnOrderSelected).start();

    }

    //End Region Public Methods

    // Region Private Methods

   private void mHandleReturnOrderSelected(){

       cResult hulpResult;

       if (!mCheckOrderIsLockableBln(cReturnorder.currentReturnOrder)) {

           cAppExtension.activity.runOnUiThread(() -> {
               cReturnorder.currentReturnOrder = null;
               cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.lockorder_order_assigned_to_another_user), R.raw.badsound);
               cUserInterface.pCheckAndCloseOpenDialogs();
               cUserInterface.pHideGettingData();
               return;
           });
       }

       //Try to lock the returnorder
       if (!this.mTryToLockOrderBln()) {
           this.pFillOrders();
           return;
       }

       //Delete the detail, so we can get them from the webservice
       if (!cReturnorder.currentReturnOrder.pDeleteDetailsBln()) {
           this.mStepFailed(cAppExtension.context.getString(R.string.error_couldnt_delete_details));
           return;
       }

       if(!cUser.currentUser.currentBranch.pGetReasonBln(true)){
           this.mStepFailed(cAppExtension.context.getString(R.string.error_getting_return_reasons));
           return;
       }

       hulpResult = cReturnorder.currentReturnOrder.pGetOrderDetailsRst();
       if (!hulpResult.resultBln) {
           this.mStepFailed(hulpResult.messagesStr());
           return;
       }

       // If everything went well, then start Documents Activity
       this.mShowReturnorderDocumentsActivity();

   }

    private boolean mTryToLockOrderBln(){

        cResult hulpResult;
        hulpResult = cReturnorder.currentReturnOrder.pLockViaWebserviceRst(cWarehouseorder.StepCodeEnu.Retour, cWarehouseorder.WorkflowReturnStepEnu.Return);

        //Everything was fine, so we are done
        if (hulpResult.resultBln) {
            return true;
        }

        //Something went wrong, but no further actions are needed, so ony show reason of failure
        if (hulpResult.activityActionEnu == cWarehouseorder.ActivityActionEnu.Unknown ) {
            this.mStepFailed(hulpResult.messagesStr());
            return  false;
        }

        //Something went wrong, the order has been deleted, so show comments and refresh
        if ( hulpResult.activityActionEnu == cWarehouseorder.ActivityActionEnu.Delete ||
                hulpResult.activityActionEnu == cWarehouseorder.ActivityActionEnu.NoStart ) {


            //If we got any comments, show them
            if (cReturnorder.currentReturnOrder.pFeedbackCommentObl() != null && cReturnorder.currentReturnOrder.pFeedbackCommentObl().size() > 0 ) {
                //Process comments from webresult
                this.mShowCommentsFragment(cReturnorder.currentReturnOrder.pFeedbackCommentObl(), hulpResult.messagesStr());
            }

            return  false;
        }


        return true;

    }

    private void mHandleFillOrders(){

        if (!cReturnorder.pGetReturnOrdersViaWebserviceBln(true, "")) {
            cUserInterface.pDoExplodingScreen(cAppExtension.context.getString(R.string.error_get_returnorders_failed), "", true, true );
            return;
        }

        if (cReturnorder.allReturnordersObl == null || cReturnorder.allReturnordersObl.size() == 0) {
            this.mShowNoOrdersIcon(true);
            return;
        }

        cAppExtension.activity.runOnUiThread(() -> {

            cReturnorder.allReturnordersObl  = cReturnorder.pGetReturnOrdersWithFilterFromDatabasObl();
            if (cReturnorder.allReturnordersObl.size() == 0) {
                mShowNoOrdersIcon( true);
                cUserInterface.pHideGettingData();
                return;
            }


            //Fill and show recycler
            mSetReturnorderRecycler(cReturnorder.allReturnordersObl);
            mShowNoOrdersIcon(false);
            if (cSharedPreferences.userReturnFilterBln()) {
                mApplyFilter();
            }

            cUserInterface.pHideGettingData();
        });
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
        cAppExtension.fragmentManager.beginTransaction().replace(R.id.constraintFilterReturnOrders, new FilterOrderLinesFragment()).commit();

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

    private  void mApplyFilter() {

        this.mShowThatFiltersInUse(cSharedPreferences.userReturnFilterBln());

        List<cReturnorder> returnorderObl = cReturnorder.pGetReturnOrdersWithFilterFromDatabasObl();

        this.mSetReturnorderRecycler(returnorderObl);

        mShowNoOrdersIcon(returnorderObl.size() == 0);

    }

    private  void mShowThatFiltersInUse(Boolean pvFiltersInUseBln) {
        if (pvFiltersInUseBln) {
            this.imageViewFilter.setImageDrawable(ContextCompat.getDrawable(cAppExtension.context, R.drawable.ic_filter_filled_black_24dp));
        }
        else {
            this.imageViewFilter.setImageDrawable(ContextCompat.getDrawable(cAppExtension.context, R.drawable.ic_filter_black_24dp));
        }
    }

    private void mSetFilterListener() {
        this.imageViewFilter.setOnClickListener(view -> {
            mShowHideBottomSheet(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN || bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED);
        });
    }

    private void mSetToolbarTitleListeners() {

        this.toolbarTitle.setOnClickListener(view -> mScrollToTop());

        this.toolbarTitle.setOnLongClickListener(view -> {
            mScrollToBottom();
            return true;
        });
    }

    // End Filter

    // Recycler View
    private void mScrollToTop() {
        this.recyclerViewReturnorders.smoothScrollToPosition(0);
    }

    private void mScrollToBottom() {
            if (this.getReturnorderAdapter().getItemCount() > 0) {
                this.recyclerViewReturnorders.smoothScrollToPosition(this.getReturnorderAdapter().getItemCount() - 1);
            }

    }

    private  void mSetReturnorderRecycler(List<cReturnorder> pvReturnorderObl) {

        this.swipeRefreshLayout.setRefreshing(false);

        if (pvReturnorderObl == null) {
            return;
        }

        this.imageViewFilter.setVisibility(View.VISIBLE);

        for (Fragment fragment: cAppExtension.fragmentManager.getFragments()) {
            if (fragment instanceof NoOrdersFragment) {
                cAppExtension.fragmentManager.beginTransaction().remove(fragment).commit();
            }
        }

        this.recyclerViewReturnorders.setHasFixedSize(false);
        this.recyclerViewReturnorders.setAdapter(this.getReturnorderAdapter());
        this.recyclerViewReturnorders.setLayoutManager(new LinearLayoutManager(cAppExtension.context));

        this.getReturnorderAdapter().pFillData(pvReturnorderObl);
    }

    private void mSetRecyclerOnScrollListener() {
        this.recyclerViewReturnorders.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                getReturnorderAdapter().pSetFilter(pvQueryTextStr);
                return true;
            }
        });


    }

    // End Recycler View

    private void mSetNewOrderListener() {
        this.imageViewNewOrder.setOnClickListener(pvView -> mShowCreateReturnorderActivity());
    }

    private void mSetSwipeRefreshListener() {
        this.swipeRefreshLayout.setOnRefreshListener(this);
        this.swipeRefreshLayout.setSize(SwipeRefreshLayout.LARGE);
        this.swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent), getResources().getColor(R.color.colorActive), getResources().getColor(R.color.colorPrimary));
    }


    // No orders icon
    private  void mShowNoOrdersIcon(final Boolean pvShowBln){

        cAppExtension.activity.runOnUiThread(() -> {

            cUserInterface.pHideGettingData();
            swipeRefreshLayout.setRefreshing(false);
            mSetToolBarTitleWithCounters();

            if (pvShowBln) {

                recyclerViewReturnorders.setVisibility(View.INVISIBLE);

                FragmentTransaction fragmentTransaction = cAppExtension.fragmentManager.beginTransaction();
                NoOrdersFragment fragment = new NoOrdersFragment();
                fragmentTransaction.replace(R.id.returnorderContainer, fragment);
                fragmentTransaction.commit();
                mAutoOpenCreateActivity();
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
        });
    }

    private  boolean mCheckOrderIsLockableBln(cReturnorder pvReturnorder){

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

    private void mResetCurrents(){

        //Reset all current objects
        cReturnorder.currentReturnOrder = null;
    }

    private void mSetNewOrderButton() {

        if (cSetting.RETOUR_NEW_WORKFLOWS().contains(cWarehouseorder.WorkflowEnu.RVS.toString().toUpperCase()) || cSetting.RETOUR_NEW_WORKFLOWS().contains(cWarehouseorder.WorkflowEnu.RVR.toString().toUpperCase())) {
            this.imageViewNewOrder.setVisibility(View.VISIBLE);
        }
        else {
            this.imageViewNewOrder.setVisibility(View.INVISIBLE);
        }
    }

    private  void mStepFailed(final String pvErrorMessageStr){

        cAppExtension.activity.runOnUiThread(() -> {
            cUserInterface.pHideGettingData();
            cUserInterface.pDoExplodingScreen(pvErrorMessageStr, cReturnorder.currentReturnOrder.getOrderNumberStr(), true, true );
            cReturnorder.currentReturnOrder.pLockReleaseViaWebserviceBln(cWarehouseorder.StepCodeEnu.Retour, cWarehouseorder.WorkflowReturnStepEnu.Return);
            cUserInterface.pCheckAndCloseOpenDialogs();
            cReturnorder.currentReturnOrder = null;
        });

    }

    private void mShowCreateReturnorderActivity() {

        cUserInterface.pCheckAndCloseOpenDialogs();

        Intent intent = new Intent(cAppExtension.context, CreateReturnActivity.class);

        ActivityOptionsCompat activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(cAppExtension.activity);
        ActivityCompat.startActivity(cAppExtension.context,intent, activityOptions.toBundle());

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

    private void mShowReturnorderDocumentsActivity() {

        cUserInterface.pCheckAndCloseOpenDialogs();

        cAppExtension.activity = this.currentActivity;

        final ViewGroup container = cAppExtension.activity.findViewById(R.id.returnorderSelectcontainer);
        final View clickedOrder = container.findViewWithTag(cReturnorder.currentReturnOrder.getOrderNumberStr());

        final Intent intent = new Intent(cAppExtension.context, ReturnorderDocumentsActivity.class);
        cAppExtension.activity.runOnUiThread(() -> {
            if (clickedOrder != null) {

                ActivityOptionsCompat activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(cAppExtension.activity, new Pair<>(clickedOrder, cPublicDefinitions.VIEW_CHOSEN_ORDER));
                ActivityCompat.startActivity(cAppExtension.context,intent, activityOptions.toBundle());
            } else {
                startActivity(intent);
                finish();
            }
        });
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
        startActivity(intent);
        finish();

    }

    private void mSetToolBarTitleWithCounters(){


        if (cReturnorder.allReturnordersObl== null ) {
            this.toolbarSubTitle.setText("0 " + cAppExtension.activity.getString(R.string.orders));
            return;
        }

        if (!cSharedPreferences.userReturnFilterBln()) {
            this.toolbarSubTitle.setText(cReturnorder.getNumberOfOrdersStr() + " " + cAppExtension.activity.getString(R.string.orders));
        } else {
            this.toolbarSubTitle.setText(cReturnorder.getNumberOfFilteredOrdersStr() + " " + cAppExtension.activity.getString(R.string.orders));
        }
    }

    private  void  mAutoOpenCreateActivity(){

        // We returned in this form, so don't start create activity
        if (!ReturnorderSelectActivity.startedViaMenuBln) {
            return;
        }

        // We can't create, so don't start create activity
        if (cSetting.RETOUR_NEW_WORKFLOWS().isEmpty()) {
            return;
        }

        mShowCreateReturnorderActivity();

    }

    // End No orders icon

    // End Region View Method

}

