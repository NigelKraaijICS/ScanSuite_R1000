package nl.icsvertex.scansuite.Activities.Intake;


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
import SSU_WHS.Intake.Intakeorders.cIntakeorder;
import nl.icsvertex.scansuite.Activities.General.MenuActivity;
import nl.icsvertex.scansuite.Fragments.Dialogs.FilterOrderLinesFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.NoOrdersFragment;
import nl.icsvertex.scansuite.R;

public class IntakeorderSelectActivity extends AppCompatActivity implements iICSDefaultActivity, SwipeRefreshLayout.OnRefreshListener {

    //Region Public Properties

    public static final String VIEW_NAME_HEADER_IMAGE = "detail:header:imageStr";
    public static final String VIEW_NAME_HEADER_TEXT = "detail:header:text";

    //End Region Public Properties

    //Region Private Properties

    // Region Views
    private static RecyclerView recyclerViewIntakeorders;
    private static ImageView toolbarImage;
    private static TextView toolbarTitle;
    private static TextView toolbarSubTitle;
    private static SearchView recyclerSearchView;

    private static ImageView imageViewFilter;
    private  static ConstraintLayout constraintFilterOrders;
    private static SwipeRefreshLayout swipeRefreshLayout;

    private static BottomSheetBehavior bottomSheetBehavior;

    // End Region Views

    //End Region Private Properties

    //Region Default Methods

    @Override
    protected void onCreate(Bundle pvSavedInstanceState) {
        super.onCreate(pvSavedInstanceState);
        setContentView(R.layout.activity_intakeorderselect);
        this.mActivityInitialize();
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
        cBarcodeScan.pRegisterBarcodeReceiver();
        cUserInterface.pEnableScanner();
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
        IntakeorderSelectActivity.pFillOrders();
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
        IntakeorderSelectActivity.toolbarImage = findViewById(R.id.toolbarImage);
        IntakeorderSelectActivity.toolbarTitle = findViewById(R.id.toolbarTitle);
        IntakeorderSelectActivity.toolbarSubTitle = findViewById(R.id.toolbarSubtext);
        IntakeorderSelectActivity.recyclerViewIntakeorders = findViewById(R.id.recyclerViewIntakeorders);
        IntakeorderSelectActivity.recyclerSearchView = findViewById(R.id.recyclerSearchView);
        IntakeorderSelectActivity.imageViewFilter = findViewById(R.id.imageViewFilter);
        IntakeorderSelectActivity.constraintFilterOrders = findViewById(R.id.constraintFilterOrders);
        IntakeorderSelectActivity.swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
    }

    @Override
    public void mSetToolbar(String pvScreenTitle) {
        IntakeorderSelectActivity.toolbarImage.setImageResource(R.drawable.ic_menu_intake);
        IntakeorderSelectActivity.toolbarTitle.setText(pvScreenTitle);
        IntakeorderSelectActivity.toolbarTitle.setSelected(true);
        IntakeorderSelectActivity.toolbarSubTitle.setSelected(true);
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
        IntakeorderSelectActivity.pFillOrders();
    }

    @Override
    public void mSetListeners() {
        this.mSetRecyclerOnScrollListener();
        this.mSetSearchListener();
        this.mSetFilterListener();
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

        //Set filter with scanned barcodeStr if there is no prefix
        if (!cRegex.hasPrefix(pvBarcodeScan.getBarcodeOriginalStr())) {
            //no prefix, fine
            IntakeorderSelectActivity.recyclerSearchView.setQuery(pvBarcodeScan.getBarcodeOriginalStr(), true);
            IntakeorderSelectActivity.recyclerSearchView.callOnClick();
            return;
        }

        // If there is a prefix, check if its a salesorder, then remove prefix en set filter
        if (cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(), cBarcodeLayout.barcodeLayoutEnu.SALESORDER)) {
            //has prefix, is salesorderStr
            IntakeorderSelectActivity.recyclerSearchView.setQuery(cRegex.pStripRegexPrefixStr(pvBarcodeScan.getBarcodeOriginalStr()), true);
            IntakeorderSelectActivity.recyclerSearchView.callOnClick();
            return;
        }

        //If there is a prefix but it's not a salesorder tgen do nope
        cUserInterface.pDoNope(IntakeorderSelectActivity.recyclerSearchView, true, true);

    }

    public static void pIntakeorderSelected(cIntakeorder pvIntakeorder) {

        if (!mCheckOrderIsLockableBln(pvIntakeorder)) {
            cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.lockorder_order_assigned_to_another_user), R.raw.badsound);
            cUserInterface.pCheckAndCloseOpenDialogs();
            return;
        }

        // Show that we are getting data
        cUserInterface.pShowGettingData();

        //Set the current intakeorder
        cIntakeorder.currentIntakeOrder = pvIntakeorder;

        new Thread(new Runnable() {
            public void run() {
                mHandleIntakeorderSelected();
            }
        }).start();

    }

    //End Region Public Methods

    // Region Private Methods

    private static void mHandleFillOrders(){

        //all Intakeorders
        if (!cIntakeorder.pGetIntakeOrdersViaWebserviceBln(true, "")) {
            cUserInterface.pDoExplodingScreen(cAppExtension.context.getString(R.string.error_get_intakeorders_failed), "", true, true );
            return;
        }

        if (cIntakeorder.allIntakeordersObl == null || cIntakeorder.allIntakeordersObl.size() == 0) {
            IntakeorderSelectActivity.mShowNoOrdersIcon(true);
            return;
        }

        cAppExtension.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //Fill and show recycler
                IntakeorderSelectActivity.mSetIntakeorderRecycler(cIntakeorder.allIntakeordersObl);
                IntakeorderSelectActivity.mShowNoOrdersIcon(false);
                if (cSharedPreferences.userFilterBln()) {
                    mApplyFilter();
                }

                cUserInterface.pHideGettingData();
            }
        });
    }

    private static void mHandleIntakeorderSelected(){

        cResult hulpResult;

        //Try to lock the intakeorder
        if (!IntakeorderSelectActivity.mTryToLockOrderBln()) {
            IntakeorderSelectActivity.pFillOrders();
            return;
        }

        //Delete the detail, so we can get them from the webservice
        if (!cIntakeorder.currentIntakeOrder.pDeleteDetailsBln()) {
            mStepFailed(cAppExtension.context.getString(R.string.error_couldnt_delete_details));
            return;
        }

        hulpResult = IntakeorderSelectActivity.mGetOrderDetailsRst();
        if (!hulpResult.resultBln) {
            IntakeorderSelectActivity.mStepFailed(hulpResult.messagesStr());
            return;
        }


        cAppExtension.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // If everything went well, then start Lines Activity
                IntakeorderSelectActivity.mShowIntakeLinesActivity();
            }
        });

    }

    private static cResult mGetOrderDetailsRst(){

        cResult result;

        result = new cResult();
        result.resultBln = true;

        //Get all linesInt for current order, if size = 0 or webservice error then stop
        if (!cIntakeorder.currentIntakeOrder.pGetMATLinesViaWebserviceBln(true)) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_intakelines_failed));
            return result;
        }

        //Get line barcodes for current order
        if (!cIntakeorder.currentIntakeOrder.pGetMATLineBarcodesViaWebserviceBln(true)) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_linebarcodes_failed));
            return result;
        }

//        // Get all article images, only if neccesary
        //todo: do this when there is a setting availaible
//        if (cIntakeorder.currentIntakeOrder.pGetArticleImagesViaWebserviceBln(true) == false) {
//            result.resultBln = false;
//            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_article_images_failed));
//            return result;
//        }

        // Get all barcodes, if size =0 or webservice error then stop
        if (!cIntakeorder.currentIntakeOrder.pGetBarcodesViaWebserviceBln(true)) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_barcodes_failed));
            return result;
        }

        // Get all comments
        if (!cIntakeorder.currentIntakeOrder.pGetCommentsViaWebserviceBln(true)) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_comments_failed));
            return result;
        }

        return  result;
    }

    private static void mStepFailed(String pvErrorMessageStr){
        cUserInterface.pDoExplodingScreen(pvErrorMessageStr, cIntakeorder.currentIntakeOrder.getOrderNumberStr(), true, true );
        cIntakeorder.currentIntakeOrder.pLockReleaseViaWebserviceBln();
        cUserInterface.pCheckAndCloseOpenDialogs();
        cIntakeorder.currentIntakeOrder = null;
    }

    private static void mShowIntakeLinesActivity() {

        cUserInterface.pCheckAndCloseOpenDialogs();

        final ViewGroup container = cAppExtension.activity.findViewById(R.id.container);

        Intent intent = new Intent(cAppExtension.context, IntakeorderLinesActivity.class);
        View clickedOrder = container.findViewWithTag(cIntakeorder.currentIntakeOrder.getOrderNumberStr());
        ActivityOptionsCompat activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(cAppExtension.activity, new Pair<>(clickedOrder, IntakeorderLinesActivity.VIEW_CHOSEN_ORDER));
        ActivityCompat.startActivity(cAppExtension.context,intent, activityOptions.toBundle());
    }

    // End Region Private Methods

    // Region View Methods

    //Bottom Sheet

    private void mInitBottomSheet() {

        IntakeorderSelectActivity.bottomSheetBehavior = BottomSheetBehavior.from(constraintFilterOrders);
        IntakeorderSelectActivity.bottomSheetBehavior.setHideable(true);
        IntakeorderSelectActivity.bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        IntakeorderSelectActivity.bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
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
            IntakeorderSelectActivity.bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            return;
        }

        IntakeorderSelectActivity.bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

    }

    //End Bottom Sheet

    //Filter

    private  static void mApplyFilter() {

        IntakeorderSelectActivity.mShowThatFiltersInUse(cSharedPreferences.userFilterBln());

        List<cIntakeorder> filteredIntakesObl = cIntakeorder.pGetIntakesWithFilterFromDatabasObl();

        IntakeorderSelectActivity.mSetIntakeorderRecycler(filteredIntakesObl);

        if (filteredIntakesObl.size() == 0) {
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
        IntakeorderSelectActivity.imageViewFilter.setOnClickListener(new View.OnClickListener() {
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
        IntakeorderSelectActivity.swipeRefreshLayout.setOnRefreshListener(this);
        IntakeorderSelectActivity.swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent), getResources().getColor(R.color.colorActive), getResources().getColor(R.color.colorPrimary));
    }


    // End Filter

    // Recycler View

    private static void mSetIntakeorderRecycler(List<cIntakeorder> pvIntakeorderObl) {

        IntakeorderSelectActivity.swipeRefreshLayout.setRefreshing(false);

        if (pvIntakeorderObl == null) {
            return;
        }

        IntakeorderSelectActivity.imageViewFilter.setVisibility(View.VISIBLE);

        for (Fragment fragment: cAppExtension.fragmentManager.getFragments()) {
            if (fragment instanceof NoOrdersFragment) {
                cAppExtension.fragmentManager.beginTransaction().remove(fragment).commit();
            }
        }

        IntakeorderSelectActivity.recyclerViewIntakeorders.setHasFixedSize(false);
        IntakeorderSelectActivity.recyclerViewIntakeorders.setAdapter(cIntakeorder.getIntakeorderAdapter());
        IntakeorderSelectActivity.recyclerViewIntakeorders.setLayoutManager(new LinearLayoutManager(cAppExtension.context));

        cIntakeorder.getIntakeorderAdapter().pFillData(pvIntakeorderObl);
    }

    private void mSetRecyclerOnScrollListener() {
        recyclerViewIntakeorders.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                        IntakeorderSelectActivity.recyclerSearchView.setVisibility(View.VISIBLE);
                        IntakeorderSelectActivity.recyclerSearchView.setAlpha(0.0f);

                        // Start the animation
                        IntakeorderSelectActivity.recyclerSearchView.animate()
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
        IntakeorderSelectActivity.recyclerSearchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View pvView) {
                recyclerSearchView.setIconified(false);
            }
        });

        //query entered
        IntakeorderSelectActivity.recyclerSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String pvString) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String pvQueryTextStr) {
                mApplyFilter();
                cIntakeorder.getIntakeorderAdapter().pSetFilter(pvQueryTextStr);
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
                IntakeorderSelectActivity.swipeRefreshLayout.setRefreshing(false);


                IntakeorderSelectActivity.mSetToolBarTitleWithCounters();


                if (pvShowBln) {

                    recyclerViewIntakeorders.setVisibility(View.INVISIBLE);

                    FragmentTransaction fragmentTransaction = cAppExtension.fragmentManager.beginTransaction();
                    NoOrdersFragment fragment = new NoOrdersFragment();
                    fragmentTransaction.replace(R.id.intakeorderContainer, fragment);
                    fragmentTransaction.commit();
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
            }
            });
    }

    private static boolean mCheckOrderIsLockableBln(cIntakeorder pvIntakeorder){

        //If there is no assigned user, then always oke
        if (pvIntakeorder.getAssignedUserIdStr().isEmpty()) {
            return true;
        }

        //If you are allowed to unlock busy order, then no problem
        if (cSetting.GENERIC_UNLOCK_BUSY_ORDERS_ALLOWED()) {
            return true;
        }

        return cUser.currentUser.getNameStr().equalsIgnoreCase(pvIntakeorder.getAssignedUserIdStr());


    }

    private  void mResetCurrents(){

        //Reset all current objects
        cIntakeorder.currentIntakeOrder = null;
    }

    private static boolean mTryToLockOrderBln(){

        cResult hulpResult;
        hulpResult = cIntakeorder.currentIntakeOrder.pLockViaWebserviceRst(cWarehouseorder.StepCodeEnu.Receive_Store, cWarehouseorder.WorkflowReceiveStoreStepEnu.Receive_Store);

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
        //                IntakeorderSelectActivity.mShowCommentsFragment(cIntakeorder.currentIntakeOrder.pFeedbackCommentObl(), hulpResult.messagesStr());
        //            }
        return (hulpResult.activityActionEnu != cWarehouseorder.ActivityActionEnu.Delete) &&
                (hulpResult.activityActionEnu != cWarehouseorder.ActivityActionEnu.NoStart);

    }

    private static void mSetToolBarTitleWithCounters(){


        String subTitleStr;

        if (cIntakeorder.allIntakeordersObl == null ) {
            IntakeorderSelectActivity.toolbarSubTitle.setText("(0)");
            return;
        }

        if (!cSharedPreferences.userFilterBln()) {
            subTitleStr = "(" + cText.pIntToStringStr(cIntakeorder.allIntakeordersObl.size()) + ") " + cAppExtension.activity.getString(R.string.orders);
        } else {
            subTitleStr = "(" + cText.pIntToStringStr(cIntakeorder.pGetIntakesWithFilterFromDatabasObl().size())  + "/" + cText.pIntToStringStr(cIntakeorder.allIntakeordersObl.size()) + ") " + cAppExtension.activity.getString(R.string.orders) + " " + cAppExtension.activity.getString(R.string.shown);
        }

        IntakeorderSelectActivity.toolbarSubTitle.setText(subTitleStr);

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
        cAppExtension.activity.startActivity(intent);
        cAppExtension.activity.finish();

    }

     // End No orders icon

    // End Region View Method

}

