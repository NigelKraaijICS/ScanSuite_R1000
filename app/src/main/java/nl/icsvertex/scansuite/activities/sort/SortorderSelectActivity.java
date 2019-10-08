package nl.icsvertex.scansuite.activities.sort;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.List;

import ICS.Interfaces.iICSDefaultActivity;
import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cRegex;
import ICS.Utils.cResult;
import ICS.Utils.cUserInterface;
import SSU_WHS.Basics.BarcodeLayouts.cBarcodeLayout;
import SSU_WHS.Basics.Settings.cSetting;
import SSU_WHS.Basics.Users.cUser;
import SSU_WHS.General.Comments.cComment;
import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.General.cPublicDefinitions;
import SSU_WHS.Picken.PickorderBarcodes.cPickorderBarcode;
import SSU_WHS.Picken.PickorderLines.cPickorderLine;
import SSU_WHS.Picken.Pickorders.cPickorder;
import nl.icsvertex.scansuite.R;
import nl.icsvertex.scansuite.activities.general.MenuActivity;
import nl.icsvertex.scansuite.cAppExtension;
import nl.icsvertex.scansuite.fragments.NoOrdersFragment;
import nl.icsvertex.scansuite.fragments.dialogs.CommentFragment;

public class SortorderSelectActivity extends AppCompatActivity implements iICSDefaultActivity {

    //Region Public Properties
    public static final String VIEW_NAME_HEADER_IMAGE = "detail:header:image";
    public static final String VIEW_NAME_HEADER_TEXT = "detail:header:text";

    //End Region Public Properties

    //Region Private Properties

    // Region Views

    private static  ShimmerFrameLayout shimmerViewContainer;
    private static  androidx.appcompat.widget.SearchView recyclerSearchView;
    private  ImageView toolbarImage;
    private TextView toolbarTitle;
    static private RecyclerView recyclerViewSortorders;

    // End Region Views

    //End Region Private Properties

    //Region Default Methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sortorder_select);
        this.mActivityInitialize();
    }

    @Override
    public void onResume() {
        super.onResume();
        cBarcodeScan.pRegisterBarcodeReceiver();
        SortorderSelectActivity.mStartShimmering();
    }

    @Override
    protected void onPause() {
        try {
            cBarcodeScan.pUnregisterBarcodeReceiver();
        } catch (Exception e) {
            e.printStackTrace();
        }
        shimmerViewContainer.stopShimmerAnimation();
        super.onPause();
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

        this.mSetToolbar(getResources().getString(R.string.screentitle_sortorderselect));

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
        this.recyclerViewSortorders = findViewById(R.id.recyclerViewSortorders);
        this.recyclerSearchView = findViewById(R.id.recyclerSearchView);
        this.shimmerViewContainer = findViewById(R.id.shimmerViewContainer);
    }

    @Override
    public void mSetViewModels() {

    }

    @Override
    public void mSetToolbar(String pvScreenTitleStr) {
        this.toolbarImage.setImageResource(R.drawable.ic_menu_sort);
        this.toolbarTitle.setText(pvScreenTitleStr);
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
    public void mSetSettings() {

    }

    @Override
    public void mFieldsInitialize() {

        this.mResetCurrents();
        SortorderSelectActivity.pFillOrders();
    }

    @Override
    public void mSetListeners() {
        mSetSearchListener();
    }

    @Override
    public void mInitScreen() {

        cBarcodeScan.pRegisterBarcodeReceiver();


    }

    //End Region iICSDefaultActivity defaults

    //Region Public Methods
    public static void pFillOrders() {

        //Stop shimmer animation
        SortorderSelectActivity.mStopShimmering();

        //First get all sortorders
        if (!cPickorder.pGetSortOrdersViaWebserviceBln(true,"")) {
            cUserInterface.pDoExplodingScreen(cAppExtension.context.getString(R.string.error_get_pickorders_failed), "", true, true );
            return;
        }


        if (cPickorder.allPickordersObl == null || cPickorder.allPickordersObl.size() == 0) {
            SortorderSelectActivity.mShowNoOrdersIcon(true);
            return;
        }

        //Fill and show recycler
        SortorderSelectActivity.mSetSortorderRecycler(cPickorder.allPickordersObl);

        SortorderSelectActivity.mShowNoOrdersIcon(false);
        return;
    }

    public static void pSortorderSelected(cPickorder pvPickorder) {

        cResult hulpResult;

        if (mCheckOrderIsLockableBln(pvPickorder) == false) {
            cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.lockorder_order_assigned_to_another_user), R.raw.badsound);
            cUserInterface.pCheckAndCloseOpenDialogs();
            return;
        }

        // Show that we are getting data
        cUserInterface.pShowGettingData();

        //Set the current pickorder
        cPickorder.currentPickOrder = pvPickorder;

        //Try to lock the pickorder

        if (SortorderSelectActivity.mTryToLockOrderBln() == false) {
            SortorderSelectActivity.pFillOrders();
            return;
        }

        //Delete the detail, so we can get them from the webservice
        if (cPickorder.currentPickOrder.pDeleteDetailsBln() == false) {
            mStepFailed(cAppExtension.context.getString(R.string.error_couldnt_delete_details));
            return;

        }

        hulpResult = SortorderSelectActivity.mGetOrderDetailsRst();
        if (hulpResult.resultBln == false ) {
            SortorderSelectActivity.mStepFailed(hulpResult.messagesStr());
            return;
        }

        // If everything went well, then start Lines Activity
        SortorderSelectActivity.mShowSortLinesActivity();
        return;

    }

    public static void pHandleScan(String pvBarcodeStr) {

        //Set filter with scanned barcode if there is no prefix
        if (cRegex.hasPrefix(pvBarcodeStr) == false) {
            //no prefix, fine
            SortorderSelectActivity.recyclerSearchView.setQuery(pvBarcodeStr, true);
            SortorderSelectActivity.recyclerSearchView.callOnClick();
            return;
        }

        // If there is a prefix, check if its a salesorder, then remove prefix en set filter
        if (cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeStr,cBarcodeLayout.barcodeLayoutEnu.SALESORDER)) {
            //has prefix, is salesorderStr
            SortorderSelectActivity.recyclerSearchView.setQuery(cRegex.pStripRegexPrefixStr(pvBarcodeStr), true);
            SortorderSelectActivity.recyclerSearchView.callOnClick();
            return;
        }

        //If there is a prefix but it's not a salesorder tgen do nope
        cUserInterface.pDoNope(recyclerSearchView, true, true);
        return;

    }

    //End Region Public Method

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
                cPickorder.getPickorderAdapter().pSetFilter((pvQueryTextStr));
                return true;
            }
        });
    }

    private  void mResetCurrents(){

        //Reset all current objects
        cPickorder.currentPickOrder = null;
        cPickorderLine.currentPickOrderLine = null;
        cPickorderBarcode.currentPickorderBarcode = null;
    }

    private static void mShowNoOrdersIcon(Boolean pvShowBln){

        if (pvShowBln) {

            recyclerViewSortorders.setVisibility(View.INVISIBLE);
            FragmentTransaction fragmentTransaction = cAppExtension.fragmentManager.beginTransaction();
            NoOrdersFragment fragment = new NoOrdersFragment();
            fragmentTransaction.replace(R.id.container, fragment);
            fragmentTransaction.commit();
            return;
        }

        recyclerViewSortorders.setVisibility(View.VISIBLE);

        List<Fragment> fragments = cAppExtension.fragmentManager.getFragments();
        for (Fragment fragment : fragments) {
            if (fragment instanceof NoOrdersFragment) {
                FragmentTransaction fragmentTransaction = cAppExtension.fragmentManager.beginTransaction();
                fragmentTransaction.remove(fragment);
                fragmentTransaction.commit();
            }
        }

        return;
    }

    private static void mSetSortorderRecycler(List<cPickorder> pvPickorderObl) {

        if (pvPickorderObl == null || pvPickorderObl.size() == 0) {
            return;
        }

        for (Fragment fragment: cAppExtension.fragmentManager.getFragments()) {
            if (fragment instanceof NoOrdersFragment) {
                cAppExtension.fragmentManager.beginTransaction().remove(fragment).commit();
            }
        }

        SortorderSelectActivity.recyclerViewSortorders.setHasFixedSize(false);
        SortorderSelectActivity.recyclerViewSortorders.setAdapter(cPickorder.getPickorderAdapter());
        SortorderSelectActivity.recyclerViewSortorders.setLayoutManager(new LinearLayoutManager(cAppExtension.context));

        cPickorder.getPickorderAdapter().pFillData(pvPickorderObl);
        return;
    }

    private static boolean mCheckOrderIsLockableBln(cPickorder pvPickorder){

        //If there is no assigned user, then always oke
        if (pvPickorder.getAssignedUserIdStr().isEmpty()) {
            return true;
        }

        //If you are allowed to unlock busy order, then no problem
        if (cSetting.GENERIC_UNLOCK_BUSY_ORDERS_ALLOWED() == true) {
            return true;
        }

        if (cUser.currentUser.getNameStr().equalsIgnoreCase(pvPickorder.getAssignedUserIdStr())) {
            return  true;
        }

        return  false;


    }

    private static boolean mTryToLockOrderBln(){

        cResult hulpResult;
        hulpResult = cPickorder.currentPickOrder.pLockViaWebserviceRst(cWarehouseorder.StepCodeEnu.Pick_Sorting, cWarehouseorder.WorkflowPickStepEnu.PickSorting);

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
            if (cPickorder.currentPickOrder.pFeedbackCommentObl() != null && cPickorder.currentPickOrder.pFeedbackCommentObl().size() > 0 ) {
                //Process comments from webresult
                SortorderSelectActivity.mShowCommentsFragment(cPickorder.currentPickOrder.pFeedbackCommentObl(), hulpResult.messagesStr());
            }

            return  false;
        }


        return true;

    }

    private static void mStepFailed(String pvErrorMessageStr){
        cUserInterface.pDoExplodingScreen(pvErrorMessageStr, cPickorder.currentPickOrder.getOrderNumberStr(), true, true );
        cPickorder.currentPickOrder.pLockReleaseViaWebserviceBln(cWarehouseorder.StepCodeEnu.Pick_Sorting, cWarehouseorder.WorkflowPickStepEnu.PickPicking);
        cUserInterface.pCheckAndCloseOpenDialogs();
        cPickorder.currentPickOrder = null;
        return ;
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

    private static cResult mGetOrderDetailsRst(){

        cResult result;

        result = new cResult();
        result.resultBln = true;

        //Get all TAKE lines for current order, if size = 0 or webservice error then stop
        if (cPickorder.currentPickOrder.pGetLinesViaWebserviceBln(true,cWarehouseorder.PickOrderTypeEnu.SORT) == false) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_picklines_failed));
            return result;
        }

        // Get all article images, only if neccesary
        if (cPickorder.currentPickOrder.pGetArticleImagesViaWebserviceBln(true) == false) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_article_images_failed));
            return result;
        }

        // Get all barcodes, if size =0 or webservice error then stop
        if (cPickorder.currentPickOrder.pGetBarcodesViaWebserviceBln(true) == false) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_barcodes_failed));
            return result;
        }

        // Get all adresses, if system settings Pick Shipping Sales == false then don't ask web service
        if (cPickorder.currentPickOrder.pGetAdressesViaWebserviceBln(true) == false) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_adresses_failed));
            return result;
        }

        // Get all comments
        if (cPickorder.currentPickOrder.pGetCommentsViaWebserviceBln(true) == false) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_comments_failed));
            return result;
        }

        if (cPickorder.currentPickOrder.pGetSortingDetailsBln() == false) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_sorting_detals_failed));
            return result;
        }

        return  result;
    }

    private static void mShowSortLinesActivity() {

        cUserInterface.pCheckAndCloseOpenDialogs();

        final ViewGroup container = cAppExtension.activity.findViewById(R.id.container);

        Intent intent = new Intent(cAppExtension.context, SortorderLinesActivity.class);
        View clickedOrder = container.findViewWithTag(cPickorder.currentPickOrder.getOrderNumberStr());
        ActivityOptionsCompat activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(cAppExtension.activity, new Pair<>(clickedOrder, SortorderLinesActivity.VIEW_CHOSEN_ORDER));
        ActivityCompat.startActivity(cAppExtension.context,intent, activityOptions.toBundle());
        return;
    }

    private static void mStartShimmering(){
        //Start Shimmer Effect's animation until data is loaded
        SortorderSelectActivity. shimmerViewContainer.startShimmerAnimation();
    }

    private static void mStopShimmering(){
        //Stopping Shimmer Effect's animation after data is loaded
        SortorderSelectActivity.shimmerViewContainer.stopShimmerAnimation();
        SortorderSelectActivity.shimmerViewContainer.setVisibility(View.GONE);
    }

    private void mTryToLeaveActivity(){

        Intent intent = new Intent(cAppExtension.context, MenuActivity.class);
        cAppExtension.activity.startActivity(intent);
    }


}
