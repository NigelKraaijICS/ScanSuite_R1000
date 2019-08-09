package nl.icsvertex.scansuite.activities.ship;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
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

import java.util.List;

import ICS.Weberror.cWeberrorEntity;
import ICS.Weberror.cWeberrorViewModel;
import SSU_WHS.BarcodeLayouts.cBarcodeLayout;
import SSU_WHS.BarcodeLayouts.cBarcodeLayoutEntity;
import SSU_WHS.BarcodeLayouts.cBarcodeLayoutViewModel;
import SSU_WHS.PickorderAddresses.cPickorderAddressViewModel;
import SSU_WHS.PickorderLinePackAndShip.cPickorderLinePackAndShipEntity;
import SSU_WHS.PickorderLinePackAndShip.cPickorderLinePackAndShipViewModel;
import SSU_WHS.PickorderLines.cPickorderLineViewModel;
import SSU_WHS.Pickorders.cPickorderAdapter;
import SSU_WHS.Pickorders.cPickorderEntity;
import SSU_WHS.Pickorders.cPickorderViewModel;
import SSU_WHS.Settings.cSettingsEnums;
import SSU_WHS.Settings.cSettingsViewModel;
import SSU_WHS.ShippingAgentServiceShippingUnits.cShippingAgentServiceShippingUnitViewModel;
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
import nl.icsvertex.scansuite.fragments.dialogs.HugeErrorFragment;
import nl.icsvertex.scansuite.fragments.NoOrdersFragment;
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

public class ShiporderSelectActivity extends AppCompatActivity {
    public static final String VIEW_NAME_HEADER_IMAGE = "detail:header:image";
    public static final String VIEW_NAME_HEADER_TEXT = "detail:header:text";
    static final String ACTIVITYNAME = "ShiporderSelectActivity";

    Context thisContext;
    Activity thisActivity;

    //region viewmodels
    cPickorderViewModel shipOrderViewModel;
    cPickorderAdapter shipOrderAdapter;
    cSettingsViewModel settingsViewModel;
    cWarehouseorderViewModel warehouseorderViewModel;
    cPickorderLineViewModel pickorderLineViewModel;
    cPickorderLinePackAndShipViewModel pickorderLinePackAndShipViewModel;
    cPickorderAddressViewModel pickorderAddressViewModel;
    cShippingAgentServiceShippingUnitViewModel shippingAgentServiceShippingUnitViewModel;
    cBarcodeLayoutViewModel barcodeLayoutViewModel;
    cWeberrorViewModel weberrorViewModel;
    //endregion viewmodels

    String currentUser;
    String currentBranch;

    android.support.v4.app.FragmentManager fragmentManager;
    ShimmerFrameLayout shimmerViewContainer;

    IntentFilter barcodeIntentFilter;
    private BroadcastReceiver barcodeReceiver;

    ConstraintLayout container;
    SearchView recyclerSearchView;
    ImageView toolbarImage;
    TextView toolbarTitle;
    ImageView toolbarImageHelp;
    RecyclerView recyclerViewShiporders;

    //region settings
    Boolean genericUnlockBusyOrdersAllowedBln;
    Boolean askUserLock;
    //endregion settings
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shiporder_select);
        thisContext = this;
        thisActivity = this;

        shipOrderViewModel = ViewModelProviders.of(this).get(cPickorderViewModel.class);
        shipOrderAdapter = new cPickorderAdapter(thisContext);
        settingsViewModel = ViewModelProviders.of(this).get(cSettingsViewModel.class);
        warehouseorderViewModel = ViewModelProviders.of(this).get(cWarehouseorderViewModel.class);
        pickorderLineViewModel = ViewModelProviders.of(this).get(cPickorderLineViewModel.class);
        pickorderLinePackAndShipViewModel = ViewModelProviders.of(this).get(cPickorderLinePackAndShipViewModel.class);
        pickorderAddressViewModel = ViewModelProviders.of(this).get(cPickorderAddressViewModel.class);
        shippingAgentServiceShippingUnitViewModel = ViewModelProviders.of(this).get(cShippingAgentServiceShippingUnitViewModel.class);
        barcodeLayoutViewModel = ViewModelProviders.of(this).get(cBarcodeLayoutViewModel.class);
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
    protected void onDestroy() {
        try {
            unregisterReceiver(barcodeReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }
    @Override
    public void onResume() {
        super.onResume();
        shimmerViewContainer.startShimmerAnimation();
        registerReceiver(barcodeReceiver, barcodeIntentFilter);
        cPublicDefinitions.CURRENT_ACTIVITY = ACTIVITYNAME;
    }

    @Override
    protected void onPause() {
        shimmerViewContainer.stopShimmerAnimation();
        try {
            unregisterReceiver(barcodeReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onPause();
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
    private void m_findViews() {
        container = findViewById(R.id.container);
        toolbarImage = findViewById(R.id.toolbarImage);
        toolbarTitle = findViewById(R.id.toolbarTitle);
        recyclerViewShiporders = findViewById(R.id.recyclerViewShiporders);
        recyclerSearchView = findViewById(R.id.recyclerSearchView);
        shimmerViewContainer = findViewById(R.id.shimmerViewContainer);
    }
    private void m_setSettings() {
        String genericUnlockBusyOrdersAllowedStr = settingsViewModel.getSetting(cSettingsEnums.p_Settings14Enu.GENERIC_UNLOCK_BUSY_ORDERS_ALLOWED.toString());
        genericUnlockBusyOrdersAllowedBln  = cText.stringToBoolean(genericUnlockBusyOrdersAllowedStr, false);
        String pickPackAndShipAutoStartStr = settingsViewModel.getSetting(cSettingsEnums.p_Settings14Enu.PICK_PACK_AND_SHIP_AUTO_START.toString());
        if (cText.stringToBoolean(pickPackAndShipAutoStartStr, true)) {
            askUserLock = false;
        }
        else {
            askUserLock = true;
        }
    }
    private void m_fieldsInitialize() {
        mGetData();
    }
    //this is public because of the NoOrdersFragment
    public void mGetData() {
        shipOrderViewModel.deleteAll();
        mFillOrders();
    }
    private void mFillOrders() {
        shipOrderViewModel.getSortOrShiporders(true, currentUser, currentBranch, 40, "", "1").observe(this, new Observer<List<cPickorderEntity>>() {
            @Override
            public void onChanged(@Nullable List<cPickorderEntity> sortorderEntities) {
                //Stopping Shimmer Effect's animation after data is loaded
                shimmerViewContainer.stopShimmerAnimation();
                shimmerViewContainer.setVisibility(View.GONE);
                if (sortorderEntities != null) {
                    mSetShipOrderRecycler(sortorderEntities);
                }
            }
        });
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
    private void mSetShipOrderRecycler(List<cPickorderEntity> sortorderEntities) {
        if (sortorderEntities.size() > 0) {
            m_removeNoOrders();
            recyclerViewShiporders.setHasFixedSize(false);
            recyclerViewShiporders.setAdapter(shipOrderAdapter);
            recyclerViewShiporders.setLayoutManager(new LinearLayoutManager(this));
            shipOrderAdapter.setPickorders(sortorderEntities);
        }
        else {
            mSetNoOrders();
        }
    }
    private void m_setToolbar() {
        toolbarImage.setImageResource(R.drawable.ic_menu_ship);
        toolbarTitle.setText(R.string.screentitle_shiporderselect);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
    private void m_setListeners() {
        m_setSearchListener();
        mSetWeberrorOberver();
    }

    private void mSetWeberrorOberver() {
        final FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        weberrorViewModel.getAllForActivityLive(ACTIVITYNAME).observe(this, new Observer<List<cWeberrorEntity>>() {
            @Override
            public void onChanged(@Nullable List<cWeberrorEntity> cWeberrorEntities) {
                if (cWeberrorEntities != null && cWeberrorEntities.size() > 0) {

                    boolean isSuccess = true;
                    for (cWeberrorEntity weberrorEntity : cWeberrorEntities) {
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
                        cUserInterface.doWebserviceError(cWeberrorEntities, false, false );
                    }
                }
                //all right, handled.
                weberrorViewModel.deleteAll();
            }
        });
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
                shipOrderAdapter.setFilter(queryText);
                return true;
            }
        });
    }

    public void setChosenShiporder(final cPickorderEntity pv_PickorderEntity) {
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
    private Boolean mLockWarehouseOrder(cPickorderEntity pickorderEntity) {
        Boolean ignoreBusy;
        if (cText.stringToInteger(pickorderEntity.getStatus())>10 && currentUser.equalsIgnoreCase(pickorderEntity.getCurrentUserId())) {
            ignoreBusy = true;
        }
        else {
            ignoreBusy = false;
        }
        if (warehouseorderViewModel.getLockWarehouseorderBln(currentUser, "", cWarehouseorder.ordertypes.PICKEN.toString(), currentBranch, pickorderEntity.getOrdernumber(), cDeviceInfo.getSerialnumber(), "1", 40, ignoreBusy)) {
            //we've achieved lock!
            return true;
        }
        else {
            return false;
        }
    }
    private void orderLockedProceed(final cPickorderEntity shiporderEntity) {
        final ViewGroup container = findViewById(R.id.container);
        Boolean forceRefresh = true;

        pickorderAddressViewModel.getPickorderAddresses(true, currentBranch, shiporderEntity.getOrdernumber());

        pickorderLinePackAndShipViewModel.getPickorderLinePackAndShips(forceRefresh, currentBranch, shiporderEntity.getOrdernumber()).observe(this, new Observer<List<cPickorderLinePackAndShipEntity>>() {
            @Override
            public void onChanged(@Nullable List<cPickorderLinePackAndShipEntity> pickorderLinePackAndShipEntities) {
                if (pickorderLinePackAndShipEntities != null && pickorderLinePackAndShipEntities.size() != 0) {
                    cSharedPreferences.setSharedPreferenceString(cPublicDefinitions.PREFERENCE_CURRENT_ORDER, shiporderEntity.getOrdernumber());
                    Intent intent = new Intent(thisContext, ShiporderLinesActivity.class);
                    startActivity(intent);
                }
                else {
                    mNoOrderlines(shiporderEntity);
                }
            }
        });
    }
    private void mNoOrderlines(cPickorderEntity sortorderEntity) {

    }

    public void resetShippingUnitQuantityUsed() {
        shippingAgentServiceShippingUnitViewModel.resetShippingUnitQuantityUsed();
    }
    private void m_handleScan(String barcode) {
        if (cRegex.hasPrefix(barcode)) {
            String barcodePrefixStr = cRegex.getPrefix(barcode);
            List<cBarcodeLayoutEntity> salesOrderLayouts = barcodeLayoutViewModel.getBarcodeLayoutsOfType(cBarcodeLayout.barcodeLayoutEnu.SALESORDER.toString());
            List<cBarcodeLayoutEntity> binLayouts = barcodeLayoutViewModel.getBarcodeLayoutsOfType(cBarcodeLayout.barcodeLayoutEnu.BIN.toString());
            Boolean foundBln = false;
            Boolean foundBin = false;
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
                for (cBarcodeLayoutEntity layoutBin : binLayouts) {
                    if (cRegex.p_checkRegexBln(layoutBin.getLayoutValue(), barcode)) {
                        foundBin = true;
                    }
                }
                if (foundBin) {
                    //has prefix, is bin
                    recyclerSearchView.setQuery(cRegex.p_stripRegexPrefixStr(barcode), true);
                    recyclerSearchView.callOnClick();
                }
                else {
                    //has prefix, is neither bin nor salesorder
                    cUserInterface.doNope(recyclerSearchView, true, true);
                }
            }
        }
        else {
            //no prefix, fine
            recyclerSearchView.setQuery(barcode, true);
            recyclerSearchView.callOnClick();
        }
    }
}
