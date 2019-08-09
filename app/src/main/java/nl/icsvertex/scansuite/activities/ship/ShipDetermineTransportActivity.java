package nl.icsvertex.scansuite.activities.ship;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.List;

import ICS.Weberror.cWeberrorEntity;
import ICS.Weberror.cWeberrorViewModel;
import SSU_WHS.BarcodeLayouts.cBarcodeLayout;
import SSU_WHS.BarcodeLayouts.cBarcodeLayoutEntity;
import SSU_WHS.BarcodeLayouts.cBarcodeLayoutViewModel;
import SSU_WHS.Complex_types.c_InterfaceShippingPackageIesp;
import SSU_WHS.PickorderAddresses.cPickorderAddressEntity;
import SSU_WHS.PickorderAddresses.cPickorderAddressViewModel;
import SSU_WHS.PickorderLinePackAndShip.cPickorderLinePackAndShip;
import SSU_WHS.PickorderLinePackAndShip.cPickorderLinePackAndShipEntity;
import SSU_WHS.PickorderLinePackAndShip.cPickorderLinePackAndShipViewModel;
import SSU_WHS.PickorderShipMethods.cPickorderShipMethodViewModel;
import SSU_WHS.Pickorders.cPickorderEntity;
import SSU_WHS.Pickorders.cPickorderViewModel;
import SSU_WHS.ShippingAgentServiceShippingUnits.cShippingAgentServiceShippingUnitEntity;
import SSU_WHS.ShippingAgentServiceShippingUnits.cShippingAgentServiceShippingUnitUsedAdapter;
import SSU_WHS.ShippingAgentServiceShippingUnits.cShippingAgentServiceShippingUnitViewModel;
import SSU_WHS.ShippingAgentServices.cShippingAgentServiceEntity;
import SSU_WHS.ShippingAgentServices.cShippingAgentServiceViewModel;
import SSU_WHS.ShippingAgents.cShippingAgentEntity;
import SSU_WHS.ShippingAgents.cShippingAgentViewModel;
import SSU_WHS.ShippingAgentsServiceShipMethods.cShippingAgentServiceShipMethodViewModel;
import SSU_WHS.Webservice.cWebservice;
import SSU_WHS.cPublicDefinitions;
import ICS.Utils.Scanning.cBarcodeScanDefinitions;
import ICS.Utils.cRegex;
import ICS.Utils.cSharedPreferences;
import ICS.Utils.cUserInterface;
import nl.icsvertex.scansuite.R;
import nl.icsvertex.scansuite.fragments.dialogs.SendingFragment;
import nl.icsvertex.scansuite.fragments.ship.ShippingUnitFragment;

import static ICS.Weberror.cWeberror.FIREBASE_ACTIVITY;
import static ICS.Weberror.cWeberror.FIREBASE_DEVICE;
import static ICS.Weberror.cWeberror.FIREBASE_ISRESULT;
import static ICS.Weberror.cWeberror.FIREBASE_ISSUCCESS;
import static ICS.Weberror.cWeberror.FIREBASE_ITEMNAME;
import static ICS.Weberror.cWeberror.FIREBASE_METHOD;
import static ICS.Weberror.cWeberror.FIREBASE_PARAMETERS;
import static ICS.Weberror.cWeberror.FIREBASE_TIMESTAMP;
import static ICS.Weberror.cWeberror.FIREBASE_URL;

public class ShipDetermineTransportActivity extends AppCompatActivity {
    Context thisContext;
    android.support.v4.app.FragmentManager fragmentManager;
    static final String ACTIVITYNAME = "ShipDetermineTransportActivity";
    private static String SENDING_TAG = "SENDING_TAG";

    //region barcodes
    IntentFilter barcodeIntentFilter;
    private BroadcastReceiver barcodeReceiver;
    //endregion barcodes

    //region locals
    List<cPickorderLinePackAndShipEntity> pickorderLinePackAndShipEntities;
    List<cShippingAgentServiceShippingUnitEntity> usedShippingUnits;
    //endregion locals

    Boolean isPVWorkflow;

    //region constants
    String chosenOrder;
    cPickorderEntity pickorderEntity;

    String currentUser;
    String currentBranch;
    String currentOrder;
    //endregion constants

    //region viewmodels
    cPickorderViewModel pickorderViewModel;
    cPickorderShipMethodViewModel pickorderShipMethodViewModel;
    cPickorderLinePackAndShipViewModel pickorderLinePackAndShipViewModel;
    cShippingAgentViewModel shippingAgentViewModel;
    cShippingAgentServiceViewModel shippingAgentServiceViewModel;
    cShippingAgentServiceShippingUnitViewModel shippingAgentServiceShippingUnitViewModel;
    cShippingAgentServiceShipMethodViewModel shippingAgentServiceShipMethodsViewModel;
    cBarcodeLayoutViewModel barcodeLayoutViewModel;
    cPickorderAddressViewModel pickorderAddressViewModel;
    cWeberrorViewModel weberrorViewModel;
    //endregion viewmodels

    private cShippingAgentServiceShippingUnitUsedAdapter shippingAgentServiceShippingUnitUsedAdapter;

    //region controls
    ImageView toolbarImage;
    TextView toolbarTitle;
    ImageView toolbarImageHelp;

    TextView sourcenoText;

    CardView addressContainer;
    TextView addressNameText;
    TextView addressAddressText;
    TextView addressZipCodeText;
    TextView addressCityText;
    TextView addressCountryText;
    TextView actionTextView;
    CardView shippingInfoContainer;
    TextView shippingAgentText;
    TextView shippingServiceText;

    RecyclerView recyclerUnitsUsed;

    ImageView imageViewPackaging;
    ImageView imageViewShippingDone;
    //endregion controls

    //region overrides
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ship_determine_transport);
        thisContext = this;
        fragmentManager  = getSupportFragmentManager();

        chosenOrder = getIntent().getStringExtra(cPublicDefinitions.SHIPPING_CHOSENSOURCENO);
        //chosenWorkplace = getIntent().getStringExtra(cPublicDefinitions.SHIPPING_CHOSENWORKPLACE);

        currentOrder = cSharedPreferences.getSharedPreferenceString(cPublicDefinitions.PREFERENCE_CURRENT_ORDER, "");
        currentUser = cSharedPreferences.getSharedPreferenceString(cPublicDefinitions.PREFERENCE_CURRENT_USER, "");
        currentBranch = cSharedPreferences.getSharedPreferenceString(cPublicDefinitions.PREFERENCE_CURRENT_BRANCH, "");
        pickorderViewModel = ViewModelProviders.of(this).get(cPickorderViewModel.class);
        pickorderEntity = pickorderViewModel.getPickorderByOrderNumber(chosenOrder);

        mSetViewModels();
        mFindViews();
        mSetCurrentOrder();
        mSetCurrentPickorderLinePackAndShipEntities();
        mBarcodeReceiver();
        mSetSettings();
        mFieldsInitialize();
        mSetToolbar();
        mSetListeners();
        mInitProgram();
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
        registerReceiver(barcodeReceiver, barcodeIntentFilter);
        cPublicDefinitions.CURRENT_ACTIVITY = ACTIVITYNAME;
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
    //endregion overrides

    //region default private voids
    private void mSetViewModels() {
        pickorderShipMethodViewModel = ViewModelProviders.of(this).get(cPickorderShipMethodViewModel.class);

        pickorderLinePackAndShipViewModel = ViewModelProviders.of(this).get(cPickorderLinePackAndShipViewModel.class);
        shippingAgentViewModel = ViewModelProviders.of(this).get(cShippingAgentViewModel.class);
        shippingAgentServiceViewModel = ViewModelProviders.of(this).get(cShippingAgentServiceViewModel.class);
        shippingAgentServiceShippingUnitViewModel = ViewModelProviders.of(this).get(cShippingAgentServiceShippingUnitViewModel.class);
        shippingAgentServiceShipMethodsViewModel = ViewModelProviders.of(this).get(cShippingAgentServiceShipMethodViewModel.class);
        barcodeLayoutViewModel = ViewModelProviders.of(this).get(cBarcodeLayoutViewModel.class);
        pickorderAddressViewModel = ViewModelProviders.of(this).get(cPickorderAddressViewModel.class);
        weberrorViewModel = ViewModelProviders.of(this).get(cWeberrorViewModel.class);
    }

    private void mFindViews() {
        toolbarImage = findViewById(R.id.toolbarImage);
        toolbarTitle = findViewById(R.id.toolbarTitle);
        toolbarImageHelp = findViewById(R.id.toolbarImageHelp);

        sourcenoText = findViewById(R.id.sourcenoText);

        addressContainer = findViewById(R.id.addressContainer);
        addressNameText = findViewById(R.id.addressNameText);
        addressAddressText = findViewById(R.id.addressAddressText);
        addressZipCodeText = findViewById(R.id.addressZipCodeText);
        addressCityText = findViewById(R.id.addressCityText);
        addressCountryText = findViewById(R.id.addressCountryText);

        actionTextView = findViewById(R.id.actionTextView);

        shippingInfoContainer = findViewById(R.id.shippingInfoContainer);
        shippingAgentText = findViewById(R.id.shippingAgentText);
        shippingServiceText = findViewById(R.id.shippingServiceText);

        recyclerUnitsUsed = findViewById(R.id.recyclerUnitsUsed);

        imageViewPackaging = findViewById(R.id.imageViewPackaging);
        imageViewShippingDone = findViewById(R.id.imageViewShippingDone);
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
                mHandleScan(barcodeStr);
            }
        };
        //don't forget to unregister on destroy.
        registerReceiver(barcodeReceiver,barcodeIntentFilter);
    }

    private void mSetSettings() {
//        String workflow = pickorderEntity.getOrdertype();
//        if (workflow.equalsIgnoreCase(cPickorder.eOrderTypes.PV.toString())) {
//            isPVWorkflow = true;
//        }
//        else {
//            isPVWorkflow = false;
//        }
    }

    private void mFieldsInitialize() {
        mSetActionText();
        mSetSourceNo();
        mSetAddress();
        mSetShippingInfo();
        mSetShippingUnits();
    }
    private void mSetActionText() {
        actionTextView.setVisibility(View.VISIBLE);
        actionTextView.setText(getString(R.string.select_shippingunit));
    }
    private void mSetToolbar() {
        toolbarImage.setImageResource(R.drawable.ic_menu_ship);
        toolbarTitle.setText(R.string.screentitle_determine_transport);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void mSetListeners() {
        mSetShippingUnitsListener();
        mSetOrderDoneListener();
        mSetWeberrorOberver();
    }

    private void mInitProgram() {
        shippingAgentServiceShippingUnitViewModel.resetShippingUnitQuantityUsed();
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
    //region default private voids

    //region private voids
    private void mHandleScan(String barcode) {
        mCheckAndCloseOpenDialogs();
        Boolean shippingpackageCodeScanned = false;
        String barcodeWithoutPrefixStr = "";
        if (cRegex.hasPrefix(barcode)) {


            List<cBarcodeLayoutEntity> binLayouts = barcodeLayoutViewModel.getBarcodeLayoutsOfType(cBarcodeLayout.barcodeLayoutEnu.SHIPPINGPACKAGE.toString());
            Boolean foundBin = false;
            for (cBarcodeLayoutEntity layout : binLayouts) {
                if (cRegex.p_checkRegexBln(layout.getLayoutValue(), barcode)) {
                    foundBin = true;
                }
            }
            if (foundBin) {
                //has prefix, is shippingpackage
                shippingpackageCodeScanned = true;
                barcodeWithoutPrefixStr = cRegex.p_stripRegexPrefixStr(barcode);
            }
            else {
                //has prefix, isn't shippingpackage
                cUserInterface.doExplodingScreen( getString(R.string.error_unknown_shippingunit), barcodeWithoutPrefixStr, true,true);
            }
        }
        else {
            //no prefix, fine
            shippingpackageCodeScanned = true;
            barcodeWithoutPrefixStr = barcode;
        }
        if (shippingpackageCodeScanned) {
            if (pickorderLinePackAndShipEntities != null && pickorderLinePackAndShipEntities.size() > 0) {
                cPickorderLinePackAndShipEntity firstPickorderLinePackAndShipEntity = pickorderLinePackAndShipEntities.get(0);
                cShippingAgentServiceShippingUnitEntity shippingAgentServiceShippingUnitEntity = shippingAgentServiceShippingUnitViewModel.getShippingUnitsByAgentAndServiceAndShippingUnit(firstPickorderLinePackAndShipEntity.getShippingagentcode(), firstPickorderLinePackAndShipEntity.getShippingagentservicecode(), barcodeWithoutPrefixStr);
                if (shippingAgentServiceShippingUnitEntity != null) {
                    int currentQuantity = shippingAgentServiceShippingUnitEntity.getShippingunitquantityused();
                    int newQuantity = currentQuantity + 1;
                    shippingAgentServiceShippingUnitViewModel.updateShippingUnitQuantityUsed(newQuantity, shippingAgentServiceShippingUnitEntity);
                }
                else {
                    cUserInterface.doExplodingScreen(getString(R.string.error_unknown_shippingunit), barcodeWithoutPrefixStr, true,true);
                }
            }
        }

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
    private void mSetCurrentOrder() {

    }
    private void mSetCurrentPickorderLinePackAndShipEntities() {
        pickorderLinePackAndShipEntities = pickorderLinePackAndShipViewModel.getNotHandledPickorderLinePackAndShipEntitiesBySourceNo(chosenOrder);
    }
    private void mSetSourceNo() {
        sourcenoText.setText(chosenOrder);
            if (pickorderLinePackAndShipEntities != null && pickorderLinePackAndShipEntities.size() > 0) {
                String processingSequence = pickorderLinePackAndShipEntities.get(0).getProcessingsequence();
                sourcenoText.setText(processingSequence);
                return;
            }

    }

    private void mSetAddress() {
        if (pickorderLinePackAndShipEntities != null && pickorderLinePackAndShipEntities.size() > 0) {
            cPickorderLinePackAndShipEntity firstPickorderLinePackAndShipEntity = pickorderLinePackAndShipEntities.get(0);
            cPickorderAddressEntity pickorderAddress = pickorderAddressViewModel.getAddressByAddresCode(firstPickorderLinePackAndShipEntity.getDeliveryaddresscode());
            if (pickorderAddress != null) {
                addressNameText.setText(pickorderAddress.getName());
                addressAddressText.setText(pickorderAddress.getAddress());
                addressZipCodeText.setText(pickorderAddress.getZipcode());
                addressCityText.setText(pickorderAddress.getCity());
                addressCountryText.setText(pickorderAddress.getCountry());
            }
        }
    }
    private void mSetShippingInfo() {

        if (pickorderLinePackAndShipEntities != null && pickorderLinePackAndShipEntities.size() > 0) {
            cPickorderLinePackAndShipEntity firstPickorderLinePackAndShipEntity = pickorderLinePackAndShipEntities.get(0);

            //ShippingAgent
            cShippingAgentEntity shippingAgentEntity = shippingAgentViewModel.getShippingAgentByAgentName(firstPickorderLinePackAndShipEntity.getShippingagentcode());
            if (shippingAgentEntity != null) {
                shippingAgentText.setText(shippingAgentEntity.getDescription());
            }
            else {
                shippingServiceText.setText(R.string.unknown_shippingagent);
            }

            //ShippingService
            cShippingAgentServiceEntity shippingAgentServiceEntity = shippingAgentServiceViewModel.getShippingAgentServiceByServiceCode(firstPickorderLinePackAndShipEntity.getShippingagentservicecode());
            if (shippingAgentServiceEntity != null) {
                shippingServiceText.setText(shippingAgentServiceEntity.getDescription());
            }
            else {
                shippingServiceText.setText(R.string.unknown_shippingagentservice);
            }
        }
    }
    private void mSetShippingUnits() {
        if (pickorderLinePackAndShipEntities != null && pickorderLinePackAndShipEntities.size() > 0) {
            final cPickorderLinePackAndShipEntity firstPickorderLinePackAndShipEntity = pickorderLinePackAndShipEntities.get(0);
            shippingAgentServiceShippingUnitViewModel.getShippingUnitsByAgentAndServiceLive(firstPickorderLinePackAndShipEntity.getShippingagentcode(), firstPickorderLinePackAndShipEntity.getShippingagentservicecode()).observe(this, new Observer<List<cShippingAgentServiceShippingUnitEntity>>() {
                @Override
                public void onChanged(@Nullable List<cShippingAgentServiceShippingUnitEntity> shippingAgentServiceShippingUnitEntities) {
                    usedShippingUnits = new ArrayList<>();
                    if (shippingAgentServiceShippingUnitEntities != null) {
                        for (cShippingAgentServiceShippingUnitEntity shippingAgentServiceShippingUnitEntity: shippingAgentServiceShippingUnitEntities) {
                            if (shippingAgentServiceShippingUnitEntity.getShippingunitquantityused() != 0) {
                                usedShippingUnits.add(shippingAgentServiceShippingUnitEntity);
                            }
                        }
                            mFillUsedUnitsRecycler(usedShippingUnits);
                    }
                }
            });
        }
    }
    private void mFillUsedUnitsRecycler(List<cShippingAgentServiceShippingUnitEntity> shippingAgentServiceShippingUnitEntities) {
        if (shippingAgentServiceShippingUnitEntities.size() == 0) {
            actionTextView.setVisibility(View.VISIBLE);
            actionTextView.setText(getString(R.string.select_shippingunit));
        }
        if (shippingAgentServiceShippingUnitEntities.size() > 0) {
            actionTextView.setVisibility(View.GONE);
        }
        shippingAgentServiceShippingUnitUsedAdapter = new cShippingAgentServiceShippingUnitUsedAdapter(thisContext);
        shippingAgentServiceShippingUnitUsedAdapter.setShippingUnits(shippingAgentServiceShippingUnitEntities);
        recyclerUnitsUsed.setHasFixedSize(false);
        recyclerUnitsUsed.setAdapter(shippingAgentServiceShippingUnitUsedAdapter);
        recyclerUnitsUsed.setLayoutManager(new LinearLayoutManager(thisContext));
    }
    private void mSetShippingUnitsListener() {
        imageViewPackaging.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mShowShippingUnits();
            }
        });
    }
    public void trySendAgain() {
        imageViewShippingDone.callOnClick();
    }
    private void mSetOrderDoneListener() {
        imageViewShippingDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int numberShippingUnits = 0;
                for (cShippingAgentServiceShippingUnitEntity shippingAgentServiceShippingUnitEntity: usedShippingUnits) {
                    numberShippingUnits += shippingAgentServiceShippingUnitEntity.getShippingunitquantityused();
                }

                if (numberShippingUnits == 0) {
                    cUserInterface.showSnackbarMessage(recyclerUnitsUsed, getString(R.string.ship_pick_at_least_one_package), R.raw.headsupsound, true);
                    cUserInterface.doNope(imageViewShippingDone, false, false);
                }
                else {
                    //setProgressBar
//                    Dialog dialog = new Dialog(thisContext);
//                    dialog.setContentView(R.layout.default_progressdialog);
//                    TextView messageText = dialog.findViewById(R.id.progressTextView);
//                    if (messageText != null) {
//                        messageText.setText(R.string.dialog_sending);
//                    }
//                    dialog.setCancelable(true);
//                    dialog.show();
                    mShowSending();

                    new Thread(new Runnable() {
                        public void run() {
                            mSalesOrderDone();
                        }
                    }).start();
                }
            }
        });
    }
    public void mSalesOrderDone() {
        List<c_InterfaceShippingPackageIesp> interfaceShippingPackages = new ArrayList<>();
        Integer sequencenumber = 0;
        for (cShippingAgentServiceShippingUnitEntity shippingAgentServiceShippingUnitEntity: usedShippingUnits) {
            for (double i=0 ; i <  shippingAgentServiceShippingUnitEntity.getShippingunitquantityused(); i++) {
                sequencenumber += 10;
                c_InterfaceShippingPackageIesp interfaceShippingPackage = new c_InterfaceShippingPackageIesp();
                //interfaceShippingPackage.setG_PackagetypeStr(shippingAgentServiceShippingUnitEntity.getContainertype());
                interfaceShippingPackage.setG_PackagetypeStr(shippingAgentServiceShippingUnitEntity.getShippingunit());
                interfaceShippingPackage.setG_SequenceNumberInt(sequencenumber);
                interfaceShippingPackage.setG_WeightinGLng(Long.parseLong(shippingAgentServiceShippingUnitEntity.getDefaultweightingrams()));
                //interfaceShippingPackage.setG_ItemcountDbl(Double.parseDouble(Integer.toString(shippingAgentServiceShippingUnitEntity.getShippingunitquantityused())) );
                interfaceShippingPackage.setG_ItemcountDbl(0d);
                interfaceShippingPackage.setG_ContainersoortStr(shippingAgentServiceShippingUnitEntity.getContainertype());
                interfaceShippingPackage.setG_ContainerStr("");
                interfaceShippingPackages.add(interfaceShippingPackage);
            }
        }
        final cPickorderLinePackAndShipEntity firstPickorderLinePackAndShipEntity = pickorderLinePackAndShipEntities.get(0);
        pickorderLinePackAndShipViewModel.updateOrderLinePackAndShipLocalStatusBySourceno(firstPickorderLinePackAndShipEntity.getSourceno(), cPickorderLinePackAndShip.LOCALSTATUS_DONE_NOTSENT);
        Boolean isOrderSend = pickorderViewModel.pickorderSourceDocumentShipped(currentUser,currentBranch, currentOrder, chosenOrder,"",firstPickorderLinePackAndShipEntity.getShippingagentcode(), firstPickorderLinePackAndShipEntity.getShippingagentservicecode(), interfaceShippingPackages);
        if (isOrderSend) {
            mShowSent();
            //we're doing this in the fragment now.
            //goLines();
        }
        else {
            mShowNotSent();
        }
    }
    public void goLines() {
        Intent intent = new Intent(thisContext, ShiporderLinesActivity.class);
        startActivity(intent);
    }
    private void mShowSending() {
        final SendingFragment sendingFragment = new SendingFragment();
        sendingFragment.setCancelable(true);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // show my popup
                sendingFragment.show(fragmentManager, SENDING_TAG);
            }
        });
    }

    private void mShowSent() {
        Fragment fragment = fragmentManager.findFragmentByTag(SENDING_TAG);
        if (fragment != null) {
            if (fragment instanceof SendingFragment) {
                ((SendingFragment) fragment).flyAway();
            }
        }

//        List<Fragment> fragments = fragmentManager.getFragments();
//        if (fragments != null) {
//            for (Fragment fragment : fragments) {
//                if (fragment instanceof SendingFragment) {
//                    ((SendingFragment) fragment).flyAway();
//                }
//            }
//        }
    }
    private void mShowNotSent() {
        Fragment fragment = fragmentManager.findFragmentByTag(SENDING_TAG);
        if (fragment != null) {
            if (fragment instanceof SendingFragment) {
                ((SendingFragment) fragment).notSendCrash();
            }
        }
    }
    private void mShowShippingUnits() {
        String shippingAgent = "";
        String shippingService = "";

        if (pickorderLinePackAndShipEntities != null && pickorderLinePackAndShipEntities.size() > 0) {
            cPickorderLinePackAndShipEntity firstPickorderLinePackAndShipEntity = pickorderLinePackAndShipEntities.get(0);
            shippingAgent = firstPickorderLinePackAndShipEntity.getShippingagentcode();
            shippingService = firstPickorderLinePackAndShipEntity.getShippingagentservicecode();
        }

            Bundle bundle = new Bundle();
            bundle.putString(cPublicDefinitions.SHIPPINGUNITFRAGMENT_SHIPPINGAGENT, shippingAgent);
            bundle.putString(cPublicDefinitions.SHIPPINGUNITFRAGMENT_SHIPPINGSERVICE, shippingService);
            final android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            final ShippingUnitFragment shippingUnitFragment = new ShippingUnitFragment();
            shippingUnitFragment.setArguments(bundle);
            shippingUnitFragment.show(fragmentManager, cPublicDefinitions.BRANCHFRAGMENT_LIST_TAG);
    }
    public void updateShippingUnitUsed(Integer newQuantity, cShippingAgentServiceShippingUnitEntity shippingAgentServiceShippingUnitEntity) {
        shippingAgentServiceShippingUnitViewModel.updateShippingUnitQuantityUsed(newQuantity, shippingAgentServiceShippingUnitEntity);
    }
    //endregion private voids

}
