package nl.icsvertex.scansuite.Activities.Pick;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
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
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.crashlytics.FirebaseCrashlytics;

import java.util.List;

import ICS.Interfaces.iICSDefaultActivity;
import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.Scanning.cProGlove;
import ICS.Utils.cRegex;
import ICS.Utils.cResult;
import ICS.Utils.cSharedPreferences;
import ICS.Utils.cText;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Basics.BarcodeLayouts.cBarcodeLayout;
import SSU_WHS.Basics.Settings.cSetting;
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
import nl.icsvertex.scansuite.Activities.General.MenuActivity;
import nl.icsvertex.scansuite.Fragments.Dialogs.CommentFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.FilterOrderLinesFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.HugeErrorFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.NoOrdersFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.WorkflowFragment;
import nl.icsvertex.scansuite.R;

import static ICS.Utils.Scanning.cProGlove.PROGLOVE_DISPLAY_TEMPLATE_2FIELD_2HEADER;
import static ICS.Utils.Scanning.cProGlove.PROGLOVE_FEEDBACK_NEGATIVE;
import static ICS.Utils.Scanning.cProGlove.PROGLOVE_FEEDBACK_YELLOW;

public class PickorderSelectActivity extends AppCompatActivity implements iICSDefaultActivity, SwipeRefreshLayout.OnRefreshListener {

    //Region Public Properties

    //End Region Public Properties

    //Region Private Properties

    // Region Views
    private ConstraintLayout combinePicksConstraintLayout;
    private RecyclerView recyclerViewPickorders;
    private ImageView toolbarImage;
    private TextView toolbarTitle;
    private TextView toolbarSubTitle;
    private TextView toolbarSubTitle2;
    private androidx.appcompat.widget.SearchView recyclerSearchView;

    private MenuItem item_combine_picks;
    private MenuItem item_select_single_pick;

    private ImageView imageViewFilter;
    private ImageView imageStartCombinedOrder;
    private ImageView imageViewNewOrder;
    private ConstraintLayout constraintFilterOrders;
    private SwipeRefreshLayout swipeRefreshLayout;

    private DrawerLayout menuActionsDrawer;
    private NavigationView pickMenuNavigation;

    private BottomSheetBehavior bottomSheetBehavior;

    private cPickorderAdapter pickorderAdapter;
    private cPickorderAdapter getPickorderAdapter(){
        if (this.pickorderAdapter == null) {
            this.pickorderAdapter = new cPickorderAdapter();
        }

        return  this.pickorderAdapter;
    }

    public  enum  ModusEnu {
        NORMAL,
        COMBINE
    }

    public static ModusEnu currentModusEnu = ModusEnu.NORMAL;
    public static boolean startedViaMenuBln;


    // End Region Views

    //End Region Private Properties

    //Region Default Methods

    @Override
    protected void onCreate(Bundle pvSavedInstanceState) {
        super.onCreate(pvSavedInstanceState);
        setContentView(R.layout.activity_pickorderselect);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (cAppExtension.activity instanceof  PickorderSelectActivity) {
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

        this.mActivityInitialize();
        cBarcodeScan.pRegisterBarcodeReceiver(this.getClass().getSimpleName());
        cUserInterface.pEnableScanner();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu pvMenu) {
        getMenuInflater().inflate(R.menu.menu_pick,pvMenu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu pvMenu) {

        if (this.item_combine_picks == null && this.item_select_single_pick == null) {
            this.item_combine_picks =  pvMenu.findItem(R.id.item_combine_picks);
            this.item_select_single_pick = pvMenu.findItem(R.id.item_select_single_pick);
        }

        if (cUser.currentUser.canMergePicks()) {

            switch (PickorderSelectActivity.currentModusEnu) {
                case NORMAL:
                    this.item_combine_picks.setVisible(true);
                    this.item_select_single_pick.setVisible(false);
                    break;
                case COMBINE:
                    this.item_combine_picks.setVisible(false);
                    this.item_select_single_pick.setVisible(true);
                    break;
                default:
                    break;
            }

        }

        return super.onPrepareOptionsMenu(pvMenu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem pvMenuItem) {



        switch (pvMenuItem.getItemId()) {

            case android.R.id.home:
                this.mLeaveActivity();
                return  true;

            case R.id.item_combine_picks:
                PickorderSelectActivity.currentModusEnu = ModusEnu.COMBINE;
                mInitCombinePickViews();
                pFillOrders();
                break;

            case R.id.item_select_single_pick:
                PickorderSelectActivity.currentModusEnu = ModusEnu.NORMAL;
                cPickorder.pUnselectAllOrders();
                mInitCombinePickViews();
                pFillOrders();
                break;

            default:
                break;
        }

        // deselect everything
        int size = pickMenuNavigation.getMenu().size();
        for (int i = 0; i < size; i++) {
            pickMenuNavigation.getMenu().getItem(i).setChecked(false);
        }

        // set item as selected to persist highlight
        pvMenuItem.setChecked(true);
        // close drawer when item is tapped
        this.menuActionsDrawer.closeDrawers();
        return true;
    }

    @Override
    public void onBackPressed() {
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

        this.mSetToolbar(getResources().getString(R.string.screentitle_pickorderselect));

        this.mFieldsInitialize();

        this.mSetListeners();

        this.mInitScreen();

        this.mSetProGloveScreen();
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
        this.recyclerViewPickorders = findViewById(R.id.recyclerViewPickorders);
        this.recyclerSearchView = findViewById(R.id.recyclerSearchView);
        this.imageViewFilter = findViewById(R.id.imageViewFilter);
        this.imageStartCombinedOrder = findViewById(R.id.imageStartCombinedOrder);
        this.imageViewNewOrder = findViewById(R.id.imageViewNewOrder);
        this.constraintFilterOrders = findViewById(R.id.constraintFilterOrders);
        this.swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        this.menuActionsDrawer = findViewById(R.id.menuActionsDrawer);
        this.pickMenuNavigation = findViewById(R.id.pickMenuNavigation);
        this.combinePicksConstraintLayout = findViewById(R.id.combinePicksConstraintLayout);
    }

    @Override
    public void mSetToolbar(String pvScreenTitleStr) {


        if (cUser.currentUser.currentAuthorisation.getCustomAuthorisation() != null) {
            this.toolbarImage.setImageBitmap(cUser.currentUser.currentAuthorisation.customImageBmp());
            this.toolbarTitle.setText(cUser.currentUser.currentAuthorisation.getCustomAuthorisation().getDescriptionStr());
        }
        else {
            this.toolbarImage.setImageResource(R.drawable.ic_menu_pick);
            this.toolbarTitle.setText(pvScreenTitleStr);
        }

        this.toolbarSubTitle2.setText(cUser.currentUser.currentBranch.getBranchNameStr());
        this.toolbarTitle.setSelected(true);
        this.toolbarSubTitle.setSelected(true);
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
        this.mSetNewOrderButton();
        this.mInitCombinePickViews();
        this.mResetCurrents();
        this.pFillOrders();
    }

    @Override
    public void mSetListeners() {
        this.mSetRecyclerOnScrollListener();
        this.mSetSearchListener();
        this.mSetFilterListener();
        this.mSetSwipeRefreshListener();
        this.mSetOpenCombinedOrderListener();
        this.mSetNewOrderListener();
    }

    @Override
    public void mInitScreen() {

    }

    //End Region iICSDefaultActivity defaults

    private void mSetProGloveScreen() {

    }

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

    public  void pHandleScan(cBarcodeScan pvBarcodeScan) {

        //Set filter with scanned barcodeStr if there is no prefix
        if (!cRegex.pHasPrefix(pvBarcodeScan.getBarcodeOriginalStr())) {

            if (PickorderSelectActivity.currentModusEnu == ModusEnu.NORMAL) {
                //no prefix, fine
                this.recyclerSearchView.setQuery(pvBarcodeScan.getBarcodeOriginalStr(), true);
                this.recyclerSearchView.callOnClick();
                return;
            }

            if (PickorderSelectActivity.currentModusEnu == ModusEnu.COMBINE) {

                cPickorder pickorder = cPickorder.pGetPickorder(pvBarcodeScan.getBarcodeOriginalStr());
                if (pickorder == null) {
                    this.mStepFailed(cAppExtension.activity.getString(R.string.message_unknown_order));
                    return;
                }

                this.recyclerViewPickorders.findViewHolderForAdapterPosition(cPickorder.pickordersToSelectObl().indexOf(pickorder)).itemView.performClick();
                return;
            }
        }


        // If there is a prefix, check if its a salesorder, then remove prefix en set filter
        if (cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(),cBarcodeLayout.barcodeLayoutEnu.DOCUMENT)) {

            if (PickorderSelectActivity.currentModusEnu == ModusEnu.NORMAL) {
                //has prefix, is salesorderStr
                this.recyclerSearchView.setQuery(cRegex.pStripRegexPrefixStr(pvBarcodeScan.getBarcodeOriginalStr()), true);
                this.recyclerSearchView.callOnClick();
                return;
            }

            if (PickorderSelectActivity.currentModusEnu == ModusEnu.COMBINE) {

                cPickorder pickorder = cPickorder.pGetPickorder(pvBarcodeScan.getBarcodeOriginalStr());
                if (pickorder == null) {
                    this.mStepFailed(cAppExtension.activity.getString(R.string.message_unknown_order));
                    return;
                }

                this.recyclerViewPickorders.findViewHolderForAdapterPosition(cPickorder.pickordersToSelectObl().indexOf(pickorder)).itemView.performClick();
                return;
            }
        }

        //If there is a prefix but it's not a salesorder tgen do nope
        cUserInterface.pDoNope(this.recyclerSearchView, true, true);
    }

    public void pPickorderSelected(cPickorder pvPickorder) {

        if (!mCheckOrderIsLockableBln(pvPickorder)) {
            cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.lockorder_order_assigned_to_another_user), R.raw.badsound);
            cUserInterface.pCheckAndCloseOpenDialogs();
            return;
        }

        // Show that we are getting data
        cUserInterface.pShowGettingData();

        //Set the current pickorder
        cPickorder.currentPickOrder = pvPickorder;
        FirebaseCrashlytics.getInstance().setCustomKey("Ordernumber", cPickorder.currentPickOrder.getOrderNumberStr());

        new Thread(new Runnable() {
            public void run() {
                mHandlePickorderSelected();
            }
        }).start();

    }

    public void pPickorderSelectedForCombi(cPickorder pvPickorder) {
        cPickorder.currentPickOrder = pvPickorder;

        cResult result = cPickorder.currentPickOrder.pUpdateSelectedRst(!cPickorder.currentPickOrder.getIsSelectedBln());
        if (!result.resultBln) {
            this.mStepFailed(result.messagesStr());
            return;
        }

        mInitCombinePickViews();
        if (PickorderSelectActivity.currentModusEnu == ModusEnu.COMBINE) {
            cPickorder.allPickordersObl = cPickorder.pickordersToSelectObl();
        }

        mRefreshRecycler();

    }

    public  void pSetToolBarTitleWithCounters(final int pvCountFilterInt){
        cAppExtension.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (cPickorder.allPickordersObl == null ) {
                    toolbarSubTitle.setText("0"  + " " + cAppExtension.activity.getString(R.string.orders));
                    return;
                }
                String subtitleStr;
                if (!cSharedPreferences.userFilterBln()) {
                    subtitleStr = cAppExtension.context.getResources().getQuantityString(R.plurals.plural_parameter1_orders, cPickorder.totalPicksInt,cPickorder.totalPicksInt);
                } else {
                    subtitleStr = cText.pIntToStringStr(pvCountFilterInt)  + "/" + cText.pIntToStringStr(cPickorder.totalPicksInt) + " " + cAppExtension.activity.getString(R.string.orders) + " " + cAppExtension.activity.getString(R.string.shown);
                }
                toolbarSubTitle.setText(subtitleStr);

                cProGlove myproglove= new cProGlove();
                String proglovedata = "1|" + getString(R.string.proglove_header_pickorders) + "|" + cUser.currentUser.currentBranch.getBranchNameStr() + "|2|" + "" + "|" + subtitleStr;
                myproglove.pSendScreen(cProGlove.PROGLOVE_DISPLAY_TEMPLATE_2FIELD_2HEADER, proglovedata, true, 0, 0);
            }
        });

    }

    public void pNewWorkflowSelected(String pvNewWorkflowsStr) {

        cUserInterface.pCheckAndCloseOpenDialogs();

        cWarehouseorder.PickMainTypeEnu pickMainTypeEnu = cWarehouseorder.PickMainTypeEnu.Unknown;

        switch (pvNewWorkflowsStr) {
            case "PA":
                pickMainTypeEnu =  cWarehouseorder.PickMainTypeEnu.PA;
                break;
            case "PF":
                pickMainTypeEnu = cWarehouseorder.PickMainTypeEnu.PF;
                break;

        }

        this.mShowCreatePickctivity(pickMainTypeEnu);

    }

    //End Region Public Methods

    // Region Private Methods

    private  void mHandleFillOrders(){

        //First get all Pickorders that are new
        if (!cPickorder.pGetPickOrdersViaWebserviceBln(true, false, "")) {
            cUserInterface.pDoExplodingScreen(cAppExtension.context.getString(R.string.error_get_pickorders_failed), "", true, true );
            String proglovedata = "1||" + cAppExtension.context.getString(R.string.error_get_pickorders_failed);
            cProGlove myproglove= new cProGlove();
            myproglove.pSendScreen(cProGlove.PROGLOVE_DISPLAY_TEMPLATE_1FIELD_ERROR, proglovedata, false, 5, PROGLOVE_FEEDBACK_NEGATIVE);
            return;
        }

        //Then get all pickorders that are processing or parked
        if (!cPickorder.pGetPickOrdersViaWebserviceBln(false, true, "")) {
            cUserInterface.pDoExplodingScreen(cAppExtension.context.getString(R.string.error_get_pickorders_failed), "", true, true );
            String proglovedata = "1||" + cAppExtension.context.getString(R.string.error_get_pickorders_failed);
            cProGlove myproglove= new cProGlove();
            myproglove.pSendScreen(cProGlove.PROGLOVE_DISPLAY_TEMPLATE_1FIELD_ERROR, proglovedata, false, 5, PROGLOVE_FEEDBACK_NEGATIVE);
            return;
        }

        if (cPickorder.allPickordersObl == null || cPickorder.allPickordersObl.size() == 0) {
            cPickorder.totalPicksInt = 0;
            pSetToolBarTitleWithCounters(cPickorder.totalPicksInt);
            this.mShowNoOrdersIcon(true);

            String proglovedata = "1||" + getResources().getString(R.string.proglove_no_orders);
            cProGlove myproglove= new cProGlove();
            myproglove.pSendScreen(cProGlove.PROGLOVE_DISPLAY_TEMPLATE_1FIELD_ALERT, proglovedata, true, 0, PROGLOVE_FEEDBACK_YELLOW);

            return;
        }

        cPickorder.totalPicksInt  = cPickorder.allPickordersObl.size();

        cAppExtension.activity.runOnUiThread(() -> {

            if (PickorderSelectActivity.currentModusEnu == ModusEnu.NORMAL) {
                cPickorder.allPickordersObl = cPickorder.pGetPicksWithFilterFromDatabasObl();
            }

            if (PickorderSelectActivity.currentModusEnu == ModusEnu.COMBINE) {
                cPickorder.allPickordersObl = cPickorder.pickordersToSelectObl();
            }

            mRefreshRecycler();

        });
    }

    private void mRefreshRecycler(){

        if (cPickorder.allPickordersObl.size() == 0) {
            mShowNoOrdersIcon( true);
            cUserInterface.pHideGettingData();
            return;
        }

        //Fill and show recycler
        mSetPickorderRecycler(cPickorder.allPickordersObl);
        mShowNoOrdersIcon(false);
        if (cSharedPreferences.userFilterBln()) {
            mApplyFilter();
        }

        cUserInterface.pHideGettingData();

    }

    private  void mHandlePickorderSelected(){

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

        hulpResult =  cPickorder.currentPickOrder.pGetPickDetailsRst();
        if (!hulpResult.resultBln) {
            this.mStepFailed(hulpResult.messagesStr());
            return;
        }

        cAppExtension.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // If everything went well, then start Lines Activity
                mShowPickLinesActivity();
            }
        });

    }

    private void mStepFailed(String pvErrorMessageStr){
        cUserInterface.pDoExplodingScreen(pvErrorMessageStr, cPickorder.currentPickOrder.getOrderNumberStr(), true, true );
        cPickorder.currentPickOrder.pLockReleaseViaWebserviceBln(cWarehouseorder.StepCodeEnu.Pick_Picking, cWarehouseorder.WorkflowPickStepEnu.PickPicking);
        cUserInterface.pCheckAndCloseOpenDialogs();
        cPickorder.currentPickOrder = null;
    }

    private void mShowPickLinesActivity() {

        cUserInterface.pCheckAndCloseOpenDialogs();

        final ViewGroup container = cAppExtension.activity.findViewById(R.id.container);

        Intent intent;

        if (cPickorder.currentPickOrder.isGeneratedOrderBln()) {
            intent = new Intent(cAppExtension.context, PickorderLinesGeneratedActivity.class);
        }
        else{
            intent = new Intent(cAppExtension.context, PickorderLinesActivity.class);
            PickorderLinesActivity.startedViaOrderSelectBln = true;
        }

        View clickedOrder = container.findViewWithTag(cPickorder.currentPickOrder.getOrderNumberStr());

        if (clickedOrder != null) {
            ActivityOptionsCompat activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(cAppExtension.activity, new Pair<>(clickedOrder, cPublicDefinitions.VIEW_CHOSEN_ORDER));
            ActivityCompat.startActivity(cAppExtension.context,intent, activityOptions.toBundle());
            return;
        }

        ActivityCompat.startActivity(cAppExtension.context,intent,null);

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
            public void onStateChanged(@NonNull View bottomSheet, int pvNewStateInt) {
                if (pvNewStateInt == BottomSheetBehavior.STATE_COLLAPSED) {
                    mApplyFilter();
                }
                if (pvNewStateInt == BottomSheetBehavior.STATE_HIDDEN) {
                    mApplyFilter();
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        this.mFillBottomSheet();
    }

    private void mInitCombinePickViews(){

        if (PickorderSelectActivity.currentModusEnu == ModusEnu.NORMAL) {
            this.combinePicksConstraintLayout.setVisibility(View.GONE);
            this.imageStartCombinedOrder.setVisibility(View.GONE);
        }
        else {
            this.combinePicksConstraintLayout.setVisibility(View.VISIBLE);
            this.imageViewNewOrder.setVisibility(View.GONE);

            if (cPickorder.pickorderSelectedObl().size() > 1) {
                this.imageStartCombinedOrder.setVisibility(View.VISIBLE);
            }
            else {
                this.imageStartCombinedOrder.setVisibility(View.GONE);
            }

        }

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

    private void mSetNewOrderButton() {

        if (currentModusEnu == ModusEnu.COMBINE) {
            this.imageViewNewOrder.setVisibility(View.INVISIBLE);
            return;
        }

        if (cSetting.PICK_NEW_WORKFLOWS().size() >= 1) {
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
                mShowCreatePickActivity();
            }
        });
    }

    //End Bottom Sheet

    //Filter

    private  void mApplyFilter() {

        this.mShowThatFiltersInUse(cSharedPreferences.userFilterBln());

        List<cPickorder> filteredPicksObl = cPickorder.pGetPicksWithFilterFromDatabasObl();

        this.mSetPickorderRecycler(filteredPicksObl);

        this.mShowNoOrdersIcon(filteredPicksObl.size() == 0);

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
        this.imageViewFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mShowHideBottomSheet(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN || bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
    }

    private void mSetOpenCombinedOrderListener() {
        this.imageStartCombinedOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pPickorderSelected(cPickorder.currentCombinedPickOrder);

            }
        });
    }

    private void mSetSwipeRefreshListener() {
        this.swipeRefreshLayout.setOnRefreshListener(this);
        this.swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent), getResources().getColor(R.color.colorActive), getResources().getColor(R.color.colorPrimary));
    }

    private void mShowCreatePickActivity() {
        Intent intent = new Intent(cAppExtension.context, CreatePickActivity.class);
        cAppExtension.activity.startActivity(intent);
    }

    // End Filter

    // Recycler View

    private void mSetPickorderRecycler(List<cPickorder> pvPickorderObl) {

        this.swipeRefreshLayout.setRefreshing(false);

        if (pvPickorderObl == null) {
            return;
        }

        this.imageViewFilter.setVisibility(View.VISIBLE);

        for (Fragment fragment: cAppExtension.fragmentManager.getFragments()) {
            if (fragment instanceof NoOrdersFragment) {
                cAppExtension.fragmentManager.beginTransaction().remove(fragment).commit();
            }
        }

        this.recyclerViewPickorders.setHasFixedSize(false);
        this.recyclerViewPickorders.setAdapter(this.getPickorderAdapter());
        this.recyclerViewPickorders.setLayoutManager(new LinearLayoutManager(cAppExtension.context));

        this.getPickorderAdapter().pFillData(pvPickorderObl);
    }

    private void mSetRecyclerOnScrollListener() {
        this.recyclerViewPickorders.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                getPickorderAdapter().pSetFilter(pvQueryTextStr);
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

                if (pvShowBln) {

                    recyclerViewPickorders.setVisibility(View.INVISIBLE);

                    FragmentTransaction fragmentTransaction = cAppExtension.fragmentManager.beginTransaction();
                    NoOrdersFragment fragment = new NoOrdersFragment();
                    fragmentTransaction.replace(R.id.pickorderContainer, fragment);
                    fragmentTransaction.commit();

                    if (cSetting.PICK_AUTO_CREATE_ORDER()) {
                        mAutoOpenCreateActivity();
                    }

                    return;
                }

                recyclerViewPickorders.setVisibility(View.VISIBLE);

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

    private boolean mCheckOrderIsLockableBln(cPickorder pvPickorder){

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

    private  void mResetCurrents(){

        //Reset all current objects
        cPickorder.currentPickOrder = null;
        cPickorderLine.currentPickOrderLine = null;
        cPickorderBarcode.currentPickorderBarcode = null;
        cWorkplace.currentWorkplace = null;

    }

    private  boolean mTryToLockOrderBln(){

        cResult hulpResult;
        hulpResult = cPickorder.currentPickOrder.pLockViaWebserviceRst(cWarehouseorder.StepCodeEnu.Pick_Picking, cWarehouseorder.WorkflowPickStepEnu.PickPicking);

        //Everything was fine, so we are done
        if (hulpResult.resultBln) {
            return true;
        }

        //Something went wrong, but no further actions are needed, so ony show reason of failure
        if ( hulpResult.activityActionEnu == cWarehouseorder.ActivityActionEnu.Unknown ) {
            mStepFailed(hulpResult.messagesStr());
            return  false;
        }

        //Something went wrong, the order has been deleted, so show comments and refresh
        if (hulpResult.activityActionEnu == cWarehouseorder.ActivityActionEnu.Delete ||
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

    private  void mShowCommentsFragment(List<cComment> pvDataObl, String pvTitleStr) {

        cUserInterface.pCheckAndCloseOpenDialogs();

        Bundle bundle = new Bundle();
        bundle.putString(cPublicDefinitions.KEY_COMMENTHEADER, pvTitleStr);

        CommentFragment commentFragment = new CommentFragment(pvDataObl);
        commentFragment.setArguments(bundle);

        commentFragment.show(cAppExtension.fragmentManager , cPublicDefinitions.COMMENTFRAGMENT_TAG);
        cUserInterface.pPlaySound(R.raw.message, 0);
    }

    private void mReleaseLicense() {

        if (! cLicense.pReleaseLicenseViaWebserviceBln()) {
            cUserInterface.pShowSnackbarMessage(recyclerViewPickorders, cAppExtension.activity.getString(R.string.message_license_release_error),null, false);
        }

        cLicense.currentLicenseEnu = cLicense.LicenseEnu.Unknown;

    }

    private void mLeaveActivity(){
        cPickorder.pUnselectAllOrders();
        this.mReleaseLicense();
        Intent intent = new Intent(cAppExtension.context, MenuActivity.class);
        cAppExtension.activity.startActivity(intent);
        finish();

    }

    private void mAutoOpenCreateActivity(){

        // We returned in this form, so don't start create activity
        if (!PickorderSelectActivity.startedViaMenuBln) {
            return;
        }

        if (cSetting.PICK_NEW_WORKFLOWS().size() == 0) {
            return;
        }

        this.mShowCreatePickctivity(null);

    }

    private  void mShowCreatePickctivity(cWarehouseorder.PickMainTypeEnu pvMainTypeEnu) {

        if (pvMainTypeEnu != null) {
            CreatePickActivity.pickMainTypeEnu = pvMainTypeEnu;
        }

            if (cSetting.PICK_NEW_WORKFLOWS().size() > 1) {
                this.mShowWorklowFragment();
                return;
            }

            switch (cSetting.PICK_NEW_WORKFLOWS().get(0).toUpperCase()) {
                case  "PF":
                    CreatePickActivity.pickMainTypeEnu = cWarehouseorder.PickMainTypeEnu.PF;
                    break;
                case "PA":
                    CreatePickActivity.pickMainTypeEnu = cWarehouseorder.PickMainTypeEnu.PA;
                    break;
            }


        cUserInterface.pCheckAndCloseOpenDialogs();
        Intent intent = new Intent(cAppExtension.context, CreatePickActivity.class);
        ActivityCompat.startActivity(cAppExtension.context,intent, null);
    }

    private  void mShowWorklowFragment() {

        cUserInterface.pCheckAndCloseOpenDialogs();

        WorkflowFragment workflowFragment = new WorkflowFragment();
        workflowFragment.show(cAppExtension.fragmentManager , cPublicDefinitions.WORKFLOWFRAGMENT_TAG);
    }

    // End No orders icon

    // End Region View Method

}
