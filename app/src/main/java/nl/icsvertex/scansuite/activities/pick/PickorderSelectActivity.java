package nl.icsvertex.scansuite.activities.pick;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.List;

import ICS.Interfaces.iICSDefaultActivity;
import ICS.Weberror.cWeberrorEntity;
import ICS.Weberror.cWeberrorViewModel;
import SSU_WHS.Basics.ArticleImages.cArticleImage;
import SSU_WHS.Basics.ArticleImages.cArticleImageEntity;
import SSU_WHS.Basics.ArticleImages.cArticleImageViewModel;
import SSU_WHS.Basics.BarcodeLayouts.cBarcodeLayout;
import SSU_WHS.Basics.BarcodeLayouts.cBarcodeLayoutEntity;
import SSU_WHS.Basics.BarcodeLayouts.cBarcodeLayoutViewModel;
import SSU_WHS.Picken.PickorderBarcodes.cPickorderBarcodeEntity;
import SSU_WHS.Picken.PickorderBarcodes.cPickorderBarcodeViewModel;
import SSU_WHS.Picken.PickorderLines.cPickorderLineEntity;
import SSU_WHS.Picken.PickorderLines.cPickorderLineViewModel;
import SSU_WHS.Picken.Pickorders.cPickorderAdapter;
import SSU_WHS.Picken.Pickorders.cPickorderEntity;
import SSU_WHS.Picken.Pickorders.cPickorderViewModel;
import SSU_WHS.Picken.SalesOrderPackingTable.cSalesOrderPackingTableViewModel;
import SSU_WHS.Basics.Settings.cSettingsEnums;
import SSU_WHS.Basics.Settings.cSettingsViewModel;
import SSU_WHS.Picken.WarehouseLocations.cWarehouseLocationEntity;
import SSU_WHS.Picken.Warehouseorder.cWarehouseorder;
import SSU_WHS.Picken.Warehouseorder.cWarehouseorderViewModel;
import SSU_WHS.Webservice.cWebservice;
import SSU_WHS.General.cAppExtension;
import SSU_WHS.General.cPublicDefinitions;
import SSU_WHS.Picken.WarehouseLocations.cWarehouseLocationViewModel;
import ICS.Utils.Scanning.cBarcodeScanDefinitions;
import ICS.Utils.cDeviceInfo;
import ICS.Utils.cRegex;
import ICS.Utils.cSharedPreferences;
import ICS.Utils.cText;
import ICS.Utils.cUserInterface;
import nl.icsvertex.scansuite.fragments.FilterOrderLinesFragment;
import nl.icsvertex.scansuite.fragments.dialogs.GettingDataFragment;
import nl.icsvertex.scansuite.fragments.dialogs.HugeErrorFragment;
import nl.icsvertex.scansuite.fragments.NoOrdersFragment;
import nl.icsvertex.scansuite.R;

import static ICS.Utils.cUserInterface.checkAndCloseOpenDialogs;
import static ICS.Weberror.cWeberror.FIREBASE_ACTIVITY;
import static ICS.Weberror.cWeberror.FIREBASE_DEVICE;
import static ICS.Weberror.cWeberror.FIREBASE_ISRESULT;
import static ICS.Weberror.cWeberror.FIREBASE_ISSUCCESS;
import static ICS.Weberror.cWeberror.FIREBASE_ITEMNAME;
import static ICS.Weberror.cWeberror.FIREBASE_METHOD;
import static ICS.Weberror.cWeberror.FIREBASE_PARAMETERS;
import static ICS.Weberror.cWeberror.FIREBASE_TIMESTAMP;
import static ICS.Weberror.cWeberror.FIREBASE_URL;

public class PickorderSelectActivity extends AppCompatActivity implements iICSDefaultActivity {
    public static final String VIEW_NAME_HEADER_IMAGE = "detail:header:image";
    public static final String VIEW_NAME_HEADER_TEXT = "detail:header:text";
    static final String ACTIVITYNAME = "PickorderSelectActivity";

    cWarehouseorderViewModel warehouseorderViewModel;
    cWarehouseLocationViewModel warehouseLocationViewModel;
    cBarcodeLayoutViewModel barcodeLayoutViewModel;
    cPickorderLineViewModel pickorderLineViewModel;
    cPickorderViewModel pickorderViewModel;
    cArticleImageViewModel articleImageViewModel;
    cSettingsViewModel settingsViewModel;
    RecyclerView recyclerViewPickorders;
    cSalesOrderPackingTableViewModel salesOrderPackingTableViewModel;
    cPickorderAdapter pickorderAdapter;
    cPickorderBarcodeViewModel pickorderBarcodeViewModel;
    cWeberrorViewModel weberrorViewModel;

    IntentFilter barcodeIntentFilter;
    private BroadcastReceiver barcodeReceiver;

    String currentUser;
    String currentBranch;
    String mainType;

    ImageView toolbarImage;
    TextView toolbarTitle;
    ImageView toolbarImageHelp;
    android.support.v7.widget.SearchView recyclerSearchView;
    ConstraintLayout pickorderContainer;

    cPickorderEntity chosenPickorderEntity;

    ImageView imageViewFilter;
    ConstraintLayout constraintFilterOrders;
    BottomSheetBehavior bottomSheetBehavior;

    //region settings
    Boolean pickWithPictureBln;
    Boolean pickWithPicturePrefetchBln;
    Boolean genericUnlockBusyOrdersAllowedBln;
    String pickSortAutoStartStr;
    String pickPackAndShipAutoStartStr;
    String genericItemExtraField1Str;
    String genericItemExtraField2Str;
    String genericItemExtraField3Str;
    String genericItemExtraField4Str;
    String genericItemExtraField5Str;
    String genericItemExtraField6Str;
    String genericItemExtraField7Str;
    String genericItemExtraField8Str;
    //endregion settings

    //region checkBooleans
    Boolean hasErrors = false;
    Boolean gotPickLines = false;
    Boolean gotImages = false;
    Boolean gotBarcodes = false;
    //endregion checkBooleans

    Boolean hasNoNotLockedOrders = false;
    Boolean hasNoLockedOrders = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickorderselect);

        hasErrors = false;

        currentUser = cSharedPreferences.getSharedPreferenceString(cPublicDefinitions.PREFERENCE_CURRENT_USER, "");
        currentBranch = cSharedPreferences.getSharedPreferenceString(cPublicDefinitions.PREFERENCE_CURRENT_BRANCH, "");
        mainType = "";

        pickorderViewModel = ViewModelProviders.of(this).get(cPickorderViewModel.class);
        articleImageViewModel = ViewModelProviders.of(this).get(cArticleImageViewModel.class);
        settingsViewModel = ViewModelProviders.of(this).get(cSettingsViewModel.class);
        pickorderBarcodeViewModel = ViewModelProviders.of(this).get(cPickorderBarcodeViewModel.class);
        weberrorViewModel = ViewModelProviders.of(this).get(cWeberrorViewModel.class);

        pickorderAdapter = new cPickorderAdapter(this);
        warehouseLocationViewModel = ViewModelProviders.of(this).get(cWarehouseLocationViewModel.class);
        warehouseorderViewModel = ViewModelProviders.of(this).get(cWarehouseorderViewModel.class);
        barcodeLayoutViewModel = ViewModelProviders.of(this).get(cBarcodeLayoutViewModel.class);
        salesOrderPackingTableViewModel = ViewModelProviders.of(this).get(cSalesOrderPackingTableViewModel.class);

        mActivityInitialize();

        mBarcodeReceiver();

        ViewCompat.setTransitionName(toolbarImage, VIEW_NAME_HEADER_IMAGE);
        ViewCompat.setTransitionName(toolbarTitle, VIEW_NAME_HEADER_TEXT);
    }
    @Override
    protected void onDestroy() {
        try {
            unregisterReceiver(barcodeReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }
    @Override
    protected void onPause() {
        try {
            unregisterReceiver(barcodeReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onPause();
    }
    @Override
    protected void onResume() {
        registerReceiver(barcodeReceiver, barcodeIntentFilter);
        super.onResume();
        cPublicDefinitions.CURRENT_ACTIVITY = ACTIVITYNAME;
    }

    @Override
    public void mActivityInitialize() {
        mSetAppExtensions();

        mFindViews();

        mSetViewModels();

        mSetSettings();

        mSetToolbar(getResources().getString(R.string.screentitle_menu));

        mFieldsInitialize();

        mSetListeners();

        mInitScreen();
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
        pickorderContainer = findViewById(R.id.pickorderContainer);
        toolbarImage = findViewById(R.id.toolbarImage);
        toolbarTitle = findViewById(R.id.toolbarTitle);
        toolbarImageHelp = findViewById(R.id.toolbarImageHelp);
        recyclerViewPickorders = findViewById(R.id.recyclerViewPickorders);
        recyclerSearchView = findViewById(R.id.recyclerSearchView);
        imageViewFilter = findViewById(R.id.imageViewFilter);
        constraintFilterOrders = findViewById(R.id.constraintFilterOrders);
    }

    @Override
    public void mSetViewModels() {

    }

    @Override
    public void mSetSettings() {
        String pickWithPicture = settingsViewModel.getSetting(cSettingsEnums.p_Settings14Enu.PICK_WITH_PICTURE.toString());
        pickWithPictureBln = cText.stringToBoolean(pickWithPicture, false);
        String pickWithPicturePrefetch = settingsViewModel.getSetting(cSettingsEnums.p_Settings14Enu.PICK_WITH_PICTURE_PREFETCH.toString());
        pickWithPicturePrefetchBln = cText.stringToBoolean(pickWithPicturePrefetch, false);
        String genericUnlockBusyOrdersAllowedStr = settingsViewModel.getSetting(cSettingsEnums.p_Settings14Enu.GENERIC_UNLOCK_BUSY_ORDERS_ALLOWED.toString());
        genericUnlockBusyOrdersAllowedBln  =cText.stringToBoolean(genericUnlockBusyOrdersAllowedStr, false);
        pickSortAutoStartStr = settingsViewModel.getSetting(cSettingsEnums.p_Settings14Enu.PICK_SORT_AUTO_START.toString());
        pickPackAndShipAutoStartStr = settingsViewModel.getSetting(cSettingsEnums.p_Settings14Enu.PICK_PACK_AND_SHIP_AUTO_START.toString());
        genericItemExtraField1Str = settingsViewModel.getSetting(cSettingsEnums.p_Settings14Enu.GENERIC_ITEM_EXTRA_FIELD1.toString());
        genericItemExtraField2Str = settingsViewModel.getSetting(cSettingsEnums.p_Settings14Enu.GENERIC_ITEM_EXTRA_FIELD2.toString());
        genericItemExtraField3Str = settingsViewModel.getSetting(cSettingsEnums.p_Settings14Enu.GENERIC_ITEM_EXTRA_FIELD3.toString());
        genericItemExtraField4Str = settingsViewModel.getSetting(cSettingsEnums.p_Settings14Enu.GENERIC_ITEM_EXTRA_FIELD4.toString());
        genericItemExtraField5Str = settingsViewModel.getSetting(cSettingsEnums.p_Settings14Enu.GENERIC_ITEM_EXTRA_FIELD5.toString());
        genericItemExtraField6Str = settingsViewModel.getSetting(cSettingsEnums.p_Settings14Enu.GENERIC_ITEM_EXTRA_FIELD6.toString());
        genericItemExtraField7Str = settingsViewModel.getSetting(cSettingsEnums.p_Settings14Enu.GENERIC_ITEM_EXTRA_FIELD7.toString());
        genericItemExtraField8Str = settingsViewModel.getSetting(cSettingsEnums.p_Settings14Enu.GENERIC_ITEM_EXTRA_FIELD8.toString());
    }

    @Override
    public void mSetToolbar(String pvScreenTitle) {
        toolbarImage.setImageResource(R.drawable.ic_menu_pick);
        toolbarTitle.setText(pvScreenTitle);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void mFieldsInitialize() {
        mInitBottomSheet();
        mGetData();
    }

    @Override
    public void mSetListeners() {
        mSetRecyclerOnScrollListener();
        mSetSearchListener();
        mSetOptionsListener();
        mSetFilterListener();
    }

    @Override
    public void mInitScreen() {

    }






    private void mBarcodeReceiver() {
        barcodeIntentFilter = new IntentFilter();
        for (String str : cBarcodeScanDefinitions.getBarcodeActions()) {
            barcodeIntentFilter.addAction(str);
        }
        for (String str : cBarcodeScanDefinitions.getBarcodeCategories()) {
            barcodeIntentFilter.addCategory(str);
        }

        barcodeReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String barcodeStr = ICS.Utils.Scanning.cBarcodeScan.p_GetBarcode(intent, true);
                if (barcodeStr == null) {
                    barcodeStr = "";
                }
                m_handleScan(barcodeStr);
            }
        };
        //don't forget to unregister on destroy.
        registerReceiver(barcodeReceiver,barcodeIntentFilter);
    }

    private void m_handleScan(String barcode) {
        if (cRegex.hasPrefix(barcode)) {
            List<cBarcodeLayoutEntity> salesOrderLayouts = barcodeLayoutViewModel.getBarcodeLayoutsOfType(cBarcodeLayout.barcodeLayoutEnu.SALESORDER.toString());
            boolean foundBln = false;
            for (cBarcodeLayoutEntity layout : salesOrderLayouts) {
                if (cRegex.p_checkRegexBln(layout.getLayoutValue(), barcode)) {
                    foundBln = true;
                }
            }
            if (foundBln) {
                //has prefix, is salesorder
                recyclerSearchView.setQuery(cRegex.p_stripRegexPrefixStr(barcode), true);
                recyclerSearchView.callOnClick();
            }
            else {
                //has prefix, isn't salesorder
                cUserInterface.doNope(recyclerSearchView, true, true);
            }
        }
        else {
            //no prefix, fine
            recyclerSearchView.setQuery(barcode, true);
            recyclerSearchView.callOnClick();
        }
    }


    public void mGetData() {
        pickorderViewModel.deleteAll();
        hasNoLockedOrders = false;
        hasNoNotLockedOrders = false;
        m_fillOrders(false);
        m_fillOrders(true);
    }
    private void mInitBottomSheet() {

        bottomSheetBehavior = BottomSheetBehavior.from(constraintFilterOrders);
        // set the peek height
        //bottomSheetBehavior.setPeekHeight(R.dimen.bottomsheet_inventory_peek_height);
        // set hideable or not
        bottomSheetBehavior.setHideable(true);
        // set callback for changes
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    m_setFilters();
                    //textViewBottomSheetPeekInv.setText(R.string.label_swipe_up_for_delete_options);
                    //findViewById(R.id.bg).setVisibility(View.GONE);
                }
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    m_setFilters();
                    //textViewBottomSheetPeekInv.setText(R.string.label_swipe_up_for_delete_options);
                }
            }
            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                //findViewById(R.id.bg).setVisibility(View.VISIBLE);
                //findViewById(R.id.bg).setAlpha(slideOffset);
            }
        });
        m_fillBottomSheet();
    }
    public void setApplyFilter() {
        m_hideBottomsheet();
        m_setFilters();
    }
    private void m_setFilters() {
        Boolean useFilters;
        Boolean showProcessedWait;
        Boolean showSingleArticles;
        Boolean showAssignedToMe;
        Boolean showAssignedToOthers;
        Boolean showNotAssigned;
        useFilters = cSharedPreferences.getDefaultSharedPreferenceBoolean(getString(R.string.filter_orderlines_enable_key), false);
        showProcessedWait = cSharedPreferences.getDefaultSharedPreferenceBoolean(getString(R.string.filter_orderlines_processing_key), false);
        showSingleArticles = cSharedPreferences.getDefaultSharedPreferenceBoolean(getString(R.string.filter_orderlines_singlearticles_key), false);
        showAssignedToMe = cSharedPreferences.getDefaultSharedPreferenceBoolean(getString(R.string.filter_orderlines_my_orders_key), false);
        showAssignedToOthers = cSharedPreferences.getDefaultSharedPreferenceBoolean(getString(R.string.filter_orderlines_their_orders_key), false);
        showNotAssigned = cSharedPreferences.getDefaultSharedPreferenceBoolean(getString(R.string.filter_orderlines_nobodys_orders_key), false);

        m_setFiltersInUse(useFilters);
        pickorderViewModel.getFilteredPickorders(currentUser, useFilters,showProcessedWait,showSingleArticles, showAssignedToMe,showAssignedToOthers, showNotAssigned).observe(this, new Observer<List<cPickorderEntity>>() {
            @Override
            public void onChanged(@Nullable List<cPickorderEntity> pickorderEntities) {
                if (pickorderEntities != null) {
                    m_setPickorderRecycler(pickorderEntities);
                }
            }
        });
    }
    private void m_setFiltersInUse(Boolean filtersInUse) {
        if (filtersInUse) {
            imageViewFilter.setImageDrawable(ContextCompat.getDrawable(cAppExtension.context, R.drawable.ic_filter_filled_black_24dp));
        }
        else {
            imageViewFilter.setImageDrawable(ContextCompat.getDrawable(cAppExtension.context, R.drawable.ic_filter_black_24dp));
        }
    }
    private void m_fillBottomSheet() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.constraintFilterOrders, new FilterOrderLinesFragment())
                .commit();
    }

    private void m_hideBottomsheet() {
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }

    private void m_showBottomsheet() {
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    public void m_fillOrders(final Boolean showAll) {

        pickorderViewModel.getPickorders(true, currentUser, currentBranch, showAll, "", mainType).observe(this, new Observer<List<cPickorderEntity>>() {
            @Override
            public void onChanged(@Nullable List<cPickorderEntity> pickorderEntities) {
                if (pickorderEntities != null) {
                    if (pickorderEntities.size() == 0) {
                        if (showAll) {
                            hasNoLockedOrders = true;
                        }
                        else  {
                            hasNoNotLockedOrders = true;
                        }
                    }
                    else {
                        List<cPickorderEntity> pickorderEntityList = pickorderViewModel.getLocalPickorders();
                        m_setPickorderRecycler(pickorderEntityList);
                    }
                    mCheckHasNoOrders();
                }
            }
        });
    }

    private void mSetRecyclerOnScrollListener() {
        recyclerViewPickorders.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
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
                        //cUserInterface.showToastMessage(thisContext, "Show", null);
                        // Prepare the View for the animation
                        recyclerSearchView.setVisibility(View.VISIBLE);
                        recyclerSearchView.setAlpha(0.0f);

                        // Start the animation
                        recyclerSearchView.animate()
                                //.translationY(recyclerSearchView.getHeight())
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
        recyclerSearchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerSearchView.setIconified(false);
            }
        });
        //query entered
        recyclerSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String queryText) {
                pickorderAdapter.setFilter(queryText);
                return true;
            }
        });
    }
    private void m_setNoOrders() {
        imageViewFilter.setVisibility(View.INVISIBLE);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        NoOrdersFragment fragment = new NoOrdersFragment();
        fragmentTransaction.replace(R.id.pickorderContainer, fragment);
        fragmentTransaction.commit();
    }
    private void m_setPickorderRecycler(List<cPickorderEntity> pickorderEntities) {
        imageViewFilter.setVisibility(View.VISIBLE);
        FragmentManager fragmentManager = getSupportFragmentManager();
        for (Fragment fragment:fragmentManager.getFragments()) {
            if (fragment instanceof NoOrdersFragment) {
                fragmentManager.beginTransaction().remove(fragment).commit();
            }
        }

        recyclerViewPickorders.setHasFixedSize(false);
        recyclerViewPickorders.setAdapter(pickorderAdapter);
        recyclerViewPickorders.setLayoutManager(new LinearLayoutManager(this));

        pickorderAdapter.setPickorders(pickorderEntities);
    }
    private void mSetOptionsListener() {

    }
    private void mSetFilterListener() {
        imageViewFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN || bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    m_showBottomsheet();
                }
                else {
                    m_hideBottomsheet();
                }
            }
        });
    }
    public void setChosenPickorder(final cPickorderEntity pv_PickorderEntity) {
        if (!pv_PickorderEntity.getAssignedUserId().trim().isEmpty() && !pv_PickorderEntity.getAssignedUserId().equalsIgnoreCase(currentUser)) {
            final HugeErrorFragment hugeErrorFragment = new HugeErrorFragment();
            Bundle bundle = new Bundle();
            bundle.putString(cPublicDefinitions.HUGEERROR_ERRORMESSAGE, getString(R.string.lockorder_order_assigned_to_another_user));
            hugeErrorFragment.setArguments(bundle);
            hugeErrorFragment.setCancelable(true);
            hugeErrorFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.HUGEERROR_TAG);
            return;
        }
        if (mLockWarehouseOrder(pv_PickorderEntity)) {
            //succesfully locked, proceed
            orderLockedProceed(pv_PickorderEntity);
        }
        else {
            cUserInterface.showToastMessage(getString(R.string.error_couldnt_lock_order), R.raw.badsound);
        }
    }
    private void orderLockedProceed(final cPickorderEntity pickorderEntity) {
        mShowGettingData();
        salesOrderPackingTableViewModel.deleteAll();
        chosenPickorderEntity = pickorderEntity;
        pickorderLineViewModel = ViewModelProviders.of(this).get(cPickorderLineViewModel.class);
        pickorderLineViewModel.getPickorderLines(true,
                                                cPickorderLineEntity.pickOrderTypeEnu.PICK.toString(),
                                                currentBranch,
                                                pickorderEntity.getOrdernumber(),
                                                cWarehouseorder.actionTypes.TAKE.toString(),
                                                genericItemExtraField1Str,
                                                genericItemExtraField2Str,
                                                genericItemExtraField3Str,
                                                genericItemExtraField4Str,
                                                genericItemExtraField5Str,
                                                genericItemExtraField6Str,
                                                genericItemExtraField7Str,
                                                genericItemExtraField8Str).observe(this, new Observer<List<cPickorderLineEntity>>() {
            @Override
            public void onChanged(@Nullable List<cPickorderLineEntity> pickorderLineEntities) {
                if (pickorderLineEntities != null && pickorderLineEntities.size() != 0) {
                    gotPickLines = true;
                    cSharedPreferences.setSharedPreferenceString(cPublicDefinitions.PREFERENCE_CURRENT_ORDER, pickorderEntity.getOrdernumber());

                    List<String> binList = new ArrayList<>();
                    for (cPickorderLineEntity pickorderLineEntity: pickorderLineEntities) {
                        String binStr = pickorderLineEntity.getBincode();
                        if (!binList.contains(binStr)) {
                            binList.add(binStr);
                        }
                    }
                    mGetWarehouseLocations(binList);
                    mCheckAllDone();

                    if (pickWithPictureBln && pickWithPicturePrefetchBln) {
                        //do we need cached pictures?
                        mPrefetchpicturesNew(pickorderLineEntities);
                    }
                    else {
                        gotImages = true;
                        mCheckAllDone();
                    }
                    mGetBarcodes();
                }
                else {
                    mNoOrderlines(pickorderEntity);
                }
            }
        });
    }
    private void mShowGettingData() {
        final GettingDataFragment gettingDataFragment = new GettingDataFragment();
        gettingDataFragment.setCancelable(true);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // show my popup
                gettingDataFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.GETTING_DATA_TAG);
            }
        });
    }
    private void mGetBarcodes() {
        pickorderBarcodeViewModel.getPickorderBarcodes(true, currentBranch, chosenPickorderEntity.getOrdernumber()).observe(this, new Observer<List<cPickorderBarcodeEntity>>() {
            @Override
            public void onChanged(@Nullable List<cPickorderBarcodeEntity> pickorderBarcodeEntities) {
                if (pickorderBarcodeEntities != null) {
                    gotBarcodes = true;
                    mCheckAllDone();
                }
            }
        });
    }

    private void mPrefetchpicturesNew(List<cPickorderLineEntity> pickorderLineEntities) {
        List<Fragment> fragments = cAppExtension.fragmentManager.getFragments();
            for (Fragment fragment : fragments) {
                if (fragment instanceof GettingDataFragment) {
                    ((GettingDataFragment)fragment).mShowMessage("getting pictures");
                }
        }
        List<cArticleImage> articleImages = new ArrayList<>();
        List<String> articleImageKey = new ArrayList<>();
        for (cPickorderLineEntity pickorderLineEntity:pickorderLineEntities) {
            String articleKey = pickorderLineEntity.getItemno() + pickorderLineEntity.getVariantcode();
            cArticleImage articleImage = new cArticleImage();
            articleImage.setItemno(pickorderLineEntity.getItemno());
            articleImage.setVariantcode(pickorderLineEntity.getVariantcode());
            if (!articleImageKey.contains(articleKey)) {
                articleImages.add(articleImage);
                articleImageKey.add(articleKey);
            }
        }
        articleImageViewModel.getArticleImagesMultiple(true,currentUser, "", articleImages).observe(this, new Observer<List<cArticleImageEntity>>() {
            @Override
            public void onChanged(@Nullable List<cArticleImageEntity> articleImageEntities) {
                if (articleImageEntities != null) {
                    gotImages = true;
                    mCheckAllDone();
                }
            }
        });
    }
    private void mGetWarehouseLocations(List<String> binList) {
        warehouseLocationViewModel.getWarehouseLocations(true, currentBranch, binList).observe(this, new Observer<List<cWarehouseLocationEntity>>() {
            @Override
            public void onChanged(@Nullable List<cWarehouseLocationEntity> warehouseLocationEntities) {
            if (warehouseLocationEntities != null) {

            }
            }
        });
    }
    private Boolean mLockWarehouseOrder(cPickorderEntity pickorderEntity) {
        boolean ignoreBusy;
        ignoreBusy = cText.stringToInteger(pickorderEntity.getStatus()) > 10 && currentUser.equalsIgnoreCase(pickorderEntity.getCurrentUserId());
        //we've achieved lock!
        return warehouseorderViewModel.getLockWarehouseorderBln(currentUser, "", cWarehouseorder.ordertypes.PICKEN.toString(), currentBranch, pickorderEntity.getOrdernumber(), cDeviceInfo.getSerialnumber(), "Pick_Picking", 10, ignoreBusy);
    }
    private Boolean mUnLockWarehouseOrder(cPickorderEntity pickorderEntity) {
        //we've achieved unlock!
        return warehouseorderViewModel.getLockReleaseWarehouseorderBln(currentUser, "", cWarehouseorder.ordertypes.PICKEN.toString(), currentBranch, pickorderEntity.getOrdernumber(), cDeviceInfo.getSerialnumber(), "Pick_Picking", 10);
    }
    private void mNoOrderlines(cPickorderEntity pickorderEntity) {
        if (mUnLockWarehouseOrder(pickorderEntity)) {
            cUserInterface.playSound(R.raw.badsound,0);
            //cUserInterface.showSnackbarMessage(thisContext, recyclerViewPickorders, R.string.order_has_no_lines, );
        }
    }
    private void mCheckHasNoOrders() {
        if (hasNoLockedOrders && hasNoNotLockedOrders) {
            m_setNoOrders();
        }
        else {
            m_removeNoOrders();
        }
    }
    private void m_removeNoOrders() {
        List<Fragment> fragments = cAppExtension.fragmentManager.getFragments();
        for (Fragment fragment : fragments) {
            if (fragment instanceof NoOrdersFragment) {
                FragmentTransaction fragmentTransaction = cAppExtension.fragmentManager.beginTransaction();
                fragmentTransaction.remove(fragment);
                fragmentTransaction.commit();
            }
        }
    }
    private void mCheckAllDone() {
        final ViewGroup container = findViewById(R.id.container);

        if (!gotImages || !gotPickLines || !gotBarcodes) {
            return;
        }

            //all done, any errors?
            List<cWeberrorEntity> weberrorEntities = weberrorViewModel.getAllForActivity(ACTIVITYNAME);
            if (weberrorEntities != null && weberrorEntities.size() > 0) {
                final FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
                boolean isSuccess = true;
                for (cWeberrorEntity weberrorEntity : weberrorEntities) {
                    //send to Firebase
                    Bundle bundle = new Bundle();
                    bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, FIREBASE_ITEMNAME);
                    bundle.putString(FIREBASE_ISSUCCESS, weberrorEntity.getIssucess().toString());
                    bundle.putString(FIREBASE_ISRESULT, weberrorEntity.getIsresult().toString());
                    bundle.putString(FIREBASE_ACTIVITY, weberrorEntity.getActivity());
                    bundle.putString(FIREBASE_DEVICE, weberrorEntity.getDevice());
                    bundle.putString(FIREBASE_PARAMETERS, weberrorEntity.getParameters());
                    bundle.putString(FIREBASE_METHOD, weberrorEntity.getWebmethod());
                    bundle.putString(FIREBASE_TIMESTAMP, weberrorEntity.getDatetime());
                    bundle.putString(FIREBASE_URL, cWebservice.WEBSERVICE_URL);
                    mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

                    if (!weberrorEntity.getIssucess()) {
                        isSuccess = false;
                    }
                }
                if (!isSuccess) {
                    checkAndCloseOpenDialogs();
                    cUserInterface.doWebserviceError(weberrorEntities, false, false );
                }
                //all right, handled.
                weberrorViewModel.deleteAll();
                gotImages = false;
                gotBarcodes = false;
                gotPickLines = false;
                return;
            }

            Intent intent = new Intent(cAppExtension.context, PickorderLinesActivity.class);
            intent.putExtra(cPublicDefinitions.SHOWCOMMENTS_EXTRA, true);
            View clickedOrder = container.findViewWithTag(chosenPickorderEntity.getOrdernumber());
            ActivityOptionsCompat activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(cAppExtension.activity, new Pair<>(clickedOrder, PickorderLinesActivity.VIEW_CHOSEN_ORDER));
            ActivityCompat.startActivity(cAppExtension.context,intent, activityOptions.toBundle());
        }

}

