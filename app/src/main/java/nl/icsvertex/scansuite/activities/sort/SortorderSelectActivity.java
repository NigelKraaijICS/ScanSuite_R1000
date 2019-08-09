package nl.icsvertex.scansuite.activities.sort;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.List;

import ICS.Weberror.cWeberrorEntity;
import ICS.Weberror.cWeberrorViewModel;
import SSU_WHS.ArticleImages.cArticleImage;
import SSU_WHS.ArticleImages.cArticleImageEntity;
import SSU_WHS.ArticleImages.cArticleImageViewModel;
import SSU_WHS.BarcodeLayouts.cBarcodeLayout;
import SSU_WHS.BarcodeLayouts.cBarcodeLayoutEntity;
import SSU_WHS.BarcodeLayouts.cBarcodeLayoutViewModel;
import SSU_WHS.PickorderLines.cPickorderLineEntity;
import SSU_WHS.PickorderLines.cPickorderLineViewModel;
import SSU_WHS.Pickorders.cPickorderAdapter;
import SSU_WHS.Pickorders.cPickorderEntity;
import SSU_WHS.Pickorders.cPickorderViewModel;
import SSU_WHS.SalesOrderPackingTable.cSalesOrderPackingTableViewModel;
import SSU_WHS.Settings.cSettingsEnums;
import SSU_WHS.Settings.cSettingsViewModel;
import SSU_WHS.Warehouseorder.cWarehouseorder;
import SSU_WHS.Warehouseorder.cWarehouseorderViewModel;
import SSU_WHS.Webservice.cWebservice;
import SSU_WHS.cPublicDefinitions;
import ICS.Utils.Scanning.cBarcodeScanDefinitions;
import ICS.Utils.cDeviceInfo;
import ICS.Utils.cRegex;
import ICS.Utils.cSharedPreferences;
import ICS.Utils.cText;
import ICS.Utils.cUserInterface;
import nl.icsvertex.scansuite.fragments.NoOrdersFragment;
import nl.icsvertex.scansuite.fragments.dialogs.HugeErrorFragment;
import nl.icsvertex.scansuite.R;

import static ICS.Weberror.cWeberror.FIREBASE_ACTIVITY;
import static ICS.Weberror.cWeberror.FIREBASE_DEVICE;
import static ICS.Weberror.cWeberror.FIREBASE_ISRESULT;
import static ICS.Weberror.cWeberror.FIREBASE_ISSUCCESS;
import static ICS.Weberror.cWeberror.FIREBASE_ITEMNAME;
import static ICS.Weberror.cWeberror.FIREBASE_METHOD;
import static ICS.Weberror.cWeberror.FIREBASE_PARAMETERS;
import static ICS.Weberror.cWeberror.FIREBASE_TIMESTAMP;
import static ICS.Weberror.cWeberror.FIREBASE_URL;

public class SortorderSelectActivity extends AppCompatActivity {
    public static final String VIEW_NAME_HEADER_IMAGE = "detail:header:image";
    public static final String VIEW_NAME_HEADER_TEXT = "detail:header:text";
    static final String ACTIVITYNAME = "SortorderSelectActivity";

    Context thisContext;
    Activity thisActivity;

    //region viewmodels
    cPickorderViewModel sortOrderViewModel;
    cPickorderAdapter sortOrderAdapter;
    cBarcodeLayoutViewModel barcodeLayoutViewModel;
    cSettingsViewModel settingsViewModel;
    cWarehouseorderViewModel warehouseorderViewModel;
    cPickorderLineViewModel pickorderLineViewModel;
    cSalesOrderPackingTableViewModel salesOrderPackingTableViewModel;
    cArticleImageViewModel articleImageViewModel;
    cWeberrorViewModel weberrorViewModel;
    //endregion viewmodels

    cPickorderEntity chosenSortOrder;

    String currentUser;
    String currentBranch;

    android.support.v4.app.FragmentManager fragmentManager;
    ShimmerFrameLayout shimmerViewContainer;

    IntentFilter barcodeIntentFilter;
    private BroadcastReceiver barcodeReceiver;

    android.support.v7.widget.SearchView recyclerSearchView;
    ImageView toolbarImage;
    TextView toolbarTitle;
    ImageView toolbarImageHelp;
    RecyclerView recyclerViewSortorders;

    //region settings
    Boolean genericUnlockBusyOrdersAllowedBln;
    Boolean pickWithPictureBln;
    Boolean pickWithPicturePrefetchBln;
    Boolean askUserLock;
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
    Boolean gotPickLines = false;
    Boolean gotImages = false;
    Boolean gotBarcodes = false;
    //endregion checkBooleans

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sortorder_select);
        thisContext = this;
        thisActivity = this;

        sortOrderViewModel = ViewModelProviders.of(this).get(cPickorderViewModel.class);
        sortOrderAdapter = new cPickorderAdapter(thisContext);
        settingsViewModel = ViewModelProviders.of(this).get(cSettingsViewModel.class);
        warehouseorderViewModel = ViewModelProviders.of(this).get(cWarehouseorderViewModel.class);
        pickorderLineViewModel = ViewModelProviders.of(this).get(cPickorderLineViewModel.class);
        salesOrderPackingTableViewModel = ViewModelProviders.of(this).get(cSalesOrderPackingTableViewModel.class);
        barcodeLayoutViewModel = ViewModelProviders.of(this).get(cBarcodeLayoutViewModel.class);
        articleImageViewModel = ViewModelProviders.of(this).get(cArticleImageViewModel.class);
        weberrorViewModel = ViewModelProviders.of(this).get(cWeberrorViewModel.class);

        fragmentManager = getSupportFragmentManager();

        currentUser = cSharedPreferences.getSharedPreferenceString(cPublicDefinitions.PREFERENCE_CURRENT_USER, "");
        currentBranch = cSharedPreferences.getSharedPreferenceString(cPublicDefinitions.PREFERENCE_CURRENT_BRANCH, "");

        m_findViews();
        m_setSettings();
        m_fieldsInitialize();
        m_setToolbar();
        m_setListeners();
        m_barcodeReceiver();

        ViewCompat.setTransitionName(toolbarImage, VIEW_NAME_HEADER_IMAGE);
        ViewCompat.setTransitionName(toolbarTitle, VIEW_NAME_HEADER_TEXT);
    }
    @Override
    public void onResume() {
        registerReceiver(barcodeReceiver, barcodeIntentFilter);
        super.onResume();
        shimmerViewContainer.startShimmerAnimation();
        cPublicDefinitions.CURRENT_ACTIVITY = ACTIVITYNAME;
    }

    @Override
    protected void onPause() {
        try {
            unregisterReceiver(barcodeReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
        shimmerViewContainer.stopShimmerAnimation();
        super.onPause();
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

    private void m_findViews() {
        toolbarImage = findViewById(R.id.toolbarImage);
        toolbarTitle = findViewById(R.id.toolbarTitle);
        recyclerViewSortorders = findViewById(R.id.recyclerViewSortorders);
        recyclerSearchView = findViewById(R.id.recyclerSearchView);
        shimmerViewContainer = findViewById(R.id.shimmerViewContainer);
    }
    private void m_setSettings() {
        String genericUnlockBusyOrdersAllowedStr = settingsViewModel.getSetting(cSettingsEnums.p_Settings14Enu.GENERIC_UNLOCK_BUSY_ORDERS_ALLOWED.toString());
        genericUnlockBusyOrdersAllowedBln  = cText.stringToBoolean(genericUnlockBusyOrdersAllowedStr, false);
        String pickWithPicture = settingsViewModel.getSetting(cSettingsEnums.p_Settings14Enu.PICK_WITH_PICTURE.toString());
        pickWithPictureBln = cText.stringToBoolean(pickWithPicture, false);
        String pickWithPicturePrefetch = settingsViewModel.getSetting(cSettingsEnums.p_Settings14Enu.PICK_WITH_PICTURE_PREFETCH.toString());
        pickWithPicturePrefetchBln = cText.stringToBoolean(pickWithPicturePrefetch, false);
        String pickSortAutoStartStr = settingsViewModel.getSetting(cSettingsEnums.p_Settings14Enu.PICK_SORT_AUTO_START.toString());

        if (cText.stringToBoolean(pickSortAutoStartStr, false)) {
            askUserLock = false;
        }
        else {
            askUserLock = true;
        }
        genericItemExtraField1Str = settingsViewModel.getSetting(cSettingsEnums.p_Settings14Enu.GENERIC_ITEM_EXTRA_FIELD1.toString());
        genericItemExtraField2Str = settingsViewModel.getSetting(cSettingsEnums.p_Settings14Enu.GENERIC_ITEM_EXTRA_FIELD2.toString());
        genericItemExtraField3Str = settingsViewModel.getSetting(cSettingsEnums.p_Settings14Enu.GENERIC_ITEM_EXTRA_FIELD3.toString());
        genericItemExtraField4Str = settingsViewModel.getSetting(cSettingsEnums.p_Settings14Enu.GENERIC_ITEM_EXTRA_FIELD4.toString());
        genericItemExtraField5Str = settingsViewModel.getSetting(cSettingsEnums.p_Settings14Enu.GENERIC_ITEM_EXTRA_FIELD5.toString());
        genericItemExtraField6Str = settingsViewModel.getSetting(cSettingsEnums.p_Settings14Enu.GENERIC_ITEM_EXTRA_FIELD6.toString());
        genericItemExtraField7Str = settingsViewModel.getSetting(cSettingsEnums.p_Settings14Enu.GENERIC_ITEM_EXTRA_FIELD7.toString());
        genericItemExtraField8Str = settingsViewModel.getSetting(cSettingsEnums.p_Settings14Enu.GENERIC_ITEM_EXTRA_FIELD8.toString());
    }
    private void m_fieldsInitialize() {
        mGetData();
    }
    //this is public because of the NoOrdersFragment
    public void mGetData() {
        sortOrderViewModel.deleteAll();
        mFillOrders();
    }
    private void mFillOrders() {
        sortOrderViewModel.getSortOrShiporders(true, currentUser, currentBranch, 20, "", "1").observe(this, new Observer<List<cPickorderEntity>>() {
            @Override
            public void onChanged(@Nullable List<cPickorderEntity> sortorderEntities) {
                //Stopping Shimmer Effect's animation after data is loaded
                shimmerViewContainer.stopShimmerAnimation();
                shimmerViewContainer.setVisibility(View.GONE);
                if (sortorderEntities != null) {
                    mSetSortOrderRecycler(sortorderEntities);
                }
            }
        });
    }
    private void mSetSortOrderRecycler(List<cPickorderEntity> sortorderEntities) {
        if (sortorderEntities.size() > 0) {
            m_removeNoOrders();
            recyclerViewSortorders.setHasFixedSize(false);
            recyclerViewSortorders.setAdapter(sortOrderAdapter);
            recyclerViewSortorders.setLayoutManager(new LinearLayoutManager(this));
            sortOrderAdapter.setPickorders(sortorderEntities);
        }
        else {
            mSetNoOrders();
        }
    }
    private void mSetNoOrders() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        NoOrdersFragment fragment = new NoOrdersFragment();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
    }
    private void m_removeNoOrders() {
        List<Fragment> fragments = fragmentManager.getFragments();
        for (Fragment fragment : fragments) {
            if (fragment instanceof NoOrdersFragment) {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.remove(fragment);
                fragmentTransaction.commit();
            }
        }
    }
    private void m_setToolbar() {
        toolbarImage.setImageResource(R.drawable.ic_menu_sort);
        toolbarTitle.setText(R.string.screentitle_sortorderselect);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
    private void m_setListeners() {
        m_setSearchListener();
    }
    private void m_setSearchListener() {
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
                sortOrderAdapter.setFilter(queryText);
                return true;
            }
        });
    }
    public void setChosenSortorder(final cPickorderEntity pv_PickorderEntity) {
        chosenSortOrder = pv_PickorderEntity;
        //clear salesorderpackingtable
        salesOrderPackingTableViewModel.deleteAll();
        if (!pv_PickorderEntity.getAssignedUserId().trim().isEmpty() && !pv_PickorderEntity.getAssignedUserId().equalsIgnoreCase(currentUser)) {
            final HugeErrorFragment hugeErrorFragment = new HugeErrorFragment();
            Bundle bundle = new Bundle();
            bundle.putString(cPublicDefinitions.HUGEERROR_ERRORMESSAGE, getString(R.string.lockorder_order_assigned_to_another_user));
            hugeErrorFragment.setArguments(bundle);
            hugeErrorFragment.setCancelable(true);
            hugeErrorFragment.show(fragmentManager, cPublicDefinitions.HUGEERROR_TAG);
            return;
        }
//        if (!pv_PickorderEntity.getCurrentUserId().trim().isEmpty() && pv_PickorderEntity.getCurrentUserId().equalsIgnoreCase(currentUser)) {
//            if (genericUnlockBusyOrdersAllowedBln) {
//                if (askUserLock) {
//                    //show dialog
//                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                    builder.setMessage(getString(R.string.lockorder_order_already_assigned_to_user));
//                    builder.setTitle("");
//                    builder.setPositiveButton(R.string.button_lock, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int id) {
//                            if (mLockWarehouseOrder(pv_PickorderEntity, true)) {
//                                orderLockedProceed(pv_PickorderEntity);
//                            }
//                            else {
//                                final HugeErrorFragment hugeErrorFragment = new HugeErrorFragment();
//                                Bundle bundle = new Bundle();
//                                bundle.putString(cPublicDefinitions.HUGEERROR_ERRORMESSAGE, getString(R.string.lockorder_order_could_not_be_locked));
//                                hugeErrorFragment.setArguments(bundle);
//                                hugeErrorFragment.setCancelable(true);
//                                hugeErrorFragment.show(fragmentManager, cPublicDefinitions.HUGEERROR_TAG);
//                                return;
//                            }
//                        }
//                    });
//                    builder.setNegativeButton(R.string.button_dont_lock, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            return;
//                        }
//                    });
//                    builder.show();
//                    return;
//                }
//                else {
//                    if (mLockWarehouseOrder(pv_PickorderEntity, true)) {
//                        orderLockedProceed(pv_PickorderEntity);
//                        return;
//                    }
//                    else {
//                        final HugeErrorFragment hugeErrorFragment = new HugeErrorFragment();
//                        Bundle bundle = new Bundle();
//                        bundle.putString(cPublicDefinitions.HUGEERROR_ERRORMESSAGE, getString(R.string.lockorder_order_could_not_be_locked));
//                        hugeErrorFragment.setArguments(bundle);
//                        hugeErrorFragment.setCancelable(true);
//                        hugeErrorFragment.show(fragmentManager, cPublicDefinitions.HUGEERROR_TAG);
//                        return;
//                    }
//                }
//
//            }
//            else {
//                final HugeErrorFragment hugeErrorFragment = new HugeErrorFragment();
//                Bundle bundle = new Bundle();
//                bundle.putString(cPublicDefinitions.HUGEERROR_ERRORMESSAGE, getString(R.string.lockorder_order_already_busy_on_other_scanner));
//                hugeErrorFragment.setArguments(bundle);
//                hugeErrorFragment.setCancelable(true);
//                hugeErrorFragment.show(fragmentManager, cPublicDefinitions.HUGEERROR_TAG);
//                return;
//            }
//        }
        if (mLockWarehouseOrder(pv_PickorderEntity)) {
            //succesfully locked, proceed
            orderLockedProceed(pv_PickorderEntity);
        }
        else {
            cUserInterface.showToastMessage(getString(R.string.error_couldnt_lock_order), R.raw.badsound);
        }
    }
    private void m_barcodeReceiver() {
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
            String barcodePrefixStr = cRegex.getPrefix(barcode);
            List<cBarcodeLayoutEntity> salesOrderLayouts = barcodeLayoutViewModel.getBarcodeLayoutsOfType(cBarcodeLayout.barcodeLayoutEnu.SALESORDER.toString());
            Boolean foundBln = false;
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
    private Boolean mLockWarehouseOrder(cPickorderEntity pickorderEntity) {
        Boolean ignoreBusy;
        if (cText.stringToInteger(pickorderEntity.getStatus())>10 && currentUser.equalsIgnoreCase(pickorderEntity.getCurrentUserId())) {
            ignoreBusy = true;
        }
        else {
            ignoreBusy = false;
        }
        if (warehouseorderViewModel.getLockWarehouseorderBln(currentUser, "", cWarehouseorder.ordertypes.PICKEN.toString(), currentBranch, pickorderEntity.getOrdernumber(), cDeviceInfo.getSerialnumber(), "1", 20, ignoreBusy)) {
            //we've achieved lock!
            return true;
        }
        else {
            return false;
        }
    }
    private void orderLockedProceed(final cPickorderEntity sortorderEntity) {
        final ViewGroup container = findViewById(R.id.container);
        Boolean forceRefresh = true;

        pickorderLineViewModel.getPickorderLines(forceRefresh,
                                                cPickorderLineEntity.pickOrderTypeEnu.SORT.toString(),
                                                currentBranch,
                                                sortorderEntity.getOrdernumber(),
                                                cWarehouseorder.actionTypes.PLACE.toString().toUpperCase(),
                                                genericItemExtraField1Str,
                                                genericItemExtraField2Str,
                                                genericItemExtraField3Str,
                                                genericItemExtraField4Str,
                                                genericItemExtraField5Str,
                                                genericItemExtraField6Str,
                                                genericItemExtraField7Str,
                                                genericItemExtraField8Str).observe(this, new Observer<List<cPickorderLineEntity>>() {
        //pickorderLineViewModel.getPickorderLines(forceRefresh, cPickorderLineEntity.pickOrderTypeEnu.SORT.toString(), currentBranch, sortorderEntity.getOrdernumber(), "2").observe(this, new Observer<List<cPickorderLineEntity>>() {
            @Override
            public void onChanged(@Nullable List<cPickorderLineEntity> pickorderLineEntities) {
                if (pickorderLineEntities != null && pickorderLineEntities.size() != 0) {
                    gotPickLines = true;
                    if (pickWithPictureBln && pickWithPicturePrefetchBln) {
                        //do we need cached pictures?
                        mPrefetchpicturesNew(pickorderLineEntities);
                    }
                    else {
                        gotImages = true;
                        mCheckAllDone();
                    }
                        //todo: move this
//                    cSharedPreferences.setSharedPreferenceString(thisContext, cPublicDefinitions.PREFERENCE_CURRENT_ORDER, sortorderEntity.getOrdernumber());
//                    Intent intent = new Intent(thisContext, SortorderLinesActivity.class);
//                    startActivity(intent);
                }
                else {
                    mNoOrderlines(sortorderEntity);
                }
            }
        });
    }
    private void mNoOrderlines(cPickorderEntity sortorderEntity) {

    }
    private void mPrefetchpicturesNew(List<cPickorderLineEntity> pickorderLineEntities) {
        //if there is already at least one picture, assume they are all there, so son't get new ones
        for(cPickorderLineEntity pickorderLineEntity: pickorderLineEntities) {
            cArticleImageEntity articleImageEntity = articleImageViewModel.getArticleImageByItemnoAndVariantCode(pickorderLineEntity.getItemno(), pickorderLineEntity.getVariantcode());
            if (articleImageEntity != null) {
                gotImages = true;
                mCheckAllDone();
                return;
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
    private void mCheckAllDone() {
        if (!gotImages || !gotPickLines) {
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
                mCheckAndCloseOpenDialogs();
                cUserInterface.doWebserviceError(weberrorEntities, false, false );
            }
            //all right, handled.
            weberrorViewModel.deleteAll();
            gotImages = false;
            gotPickLines = false;
            return;
            }

            cSharedPreferences.setSharedPreferenceString(cPublicDefinitions.PREFERENCE_CURRENT_ORDER, chosenSortOrder.getOrdernumber());
            Intent intent = new Intent(thisContext, SortorderLinesActivity.class);
            startActivity(intent);
    }
    private void mCheckAndCloseOpenDialogs() {
        List<Fragment> fragments = fragmentManager.getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                if (fragment instanceof DialogFragment) {
                    ((DialogFragment) fragment).dismiss();
                }
            }
        }
    }
}
