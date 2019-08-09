package nl.icsvertex.scansuite.activities.ship;

import android.animation.LayoutTransition;
import android.app.Activity;
import android.app.AlertDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.List;

import ICS.Weberror.cWeberrorEntity;
import ICS.Weberror.cWeberrorViewModel;
import SSU_WHS.Basics.BarcodeLayouts.cBarcodeLayout;
import SSU_WHS.Basics.BarcodeLayouts.cBarcodeLayoutEntity;
import SSU_WHS.Basics.BarcodeLayouts.cBarcodeLayoutViewModel;
import SSU_WHS.Picken.Comments.cCommentViewModel;
import SSU_WHS.Picken.PickorderBarcodes.cPickorderBarcodeEntity;
import SSU_WHS.Picken.PickorderBarcodes.cPickorderBarcodeViewModel;
import SSU_WHS.Picken.PickorderLinePackAndShip.cPickorderLinePackAndShipEntity;
import SSU_WHS.Picken.PickorderLinePackAndShip.cPickorderLinePackAndShipGroupBySourceNo;
import SSU_WHS.Picken.PickorderLinePackAndShip.cPickorderLinePackAndShipViewModel;
import SSU_WHS.Picken.Pickorders.cPickorder;
import SSU_WHS.Picken.Pickorders.cPickorderEntity;
import SSU_WHS.Picken.Pickorders.cPickorderViewModel;
import SSU_WHS.Basics.Settings.cSettingsViewModel;
import SSU_WHS.Basics.ShippingAgentServiceShippingUnits.cShippingAgentServiceShippingUnitViewModel;
import SSU_WHS.Picken.Warehouseorder.cWarehouseorder;
import SSU_WHS.Picken.Warehouseorder.cWarehouseorderViewModel;
import SSU_WHS.Webservice.cWebservice;
import SSU_WHS.Basics.Workplaces.cWorkplaceEntity;
import SSU_WHS.General.cPublicDefinitions;
import ICS.Utils.Scanning.cBarcodeScanDefinitions;
import ICS.Utils.cDeviceInfo;
import ICS.Utils.cRegex;
import ICS.Utils.cSharedPreferences;
import ICS.Utils.cUserInterface;
import nl.icsvertex.scansuite.fragments.dialogs.OrderDoneFragment;
import nl.icsvertex.scansuite.fragments.dialogs.WorkplaceFragment;
import nl.icsvertex.scansuite.fragments.ship.ShiporderLinesShippedFragment;
import nl.icsvertex.scansuite.fragments.ship.ShiporderLinesToShipFragment;
import nl.icsvertex.scansuite.R;
import nl.icsvertex.scansuite.ShiporderLinesPagerAdapter;

import static ICS.Weberror.cWeberror.FIREBASE_ACTIVITY;
import static ICS.Weberror.cWeberror.FIREBASE_DEVICE;
import static ICS.Weberror.cWeberror.FIREBASE_ISRESULT;
import static ICS.Weberror.cWeberror.FIREBASE_ISSUCCESS;
import static ICS.Weberror.cWeberror.FIREBASE_ITEMNAME;
import static ICS.Weberror.cWeberror.FIREBASE_METHOD;
import static ICS.Weberror.cWeberror.FIREBASE_PARAMETERS;
import static ICS.Weberror.cWeberror.FIREBASE_TIMESTAMP;
import static ICS.Weberror.cWeberror.FIREBASE_URL;

//import android.app.Fragment;

public class ShiporderLinesActivity extends AppCompatActivity {
    public static final String VIEW_CHOSEN_ORDER = "detail:header:text";
    private Context thisContext;
    private Activity thisActivity;

    static final String HUGEERROR_TAG = "HUGEERROR_TAG";
    static final String COMMENTFRAGMENT_TAG = "COMMENTFRAGMENT_TAG";
    static final String WORKPLACEFRAGMENT_TAG = "WORKPLACEFRAGMENT_TAG";
    static final String ACTIVITYNAME = "ShiporderLinesActivity";

    android.support.v4.app.FragmentManager fragmentManager;

    //region controls
    TextView textViewChosenOrder;
    TextView quantityShipordersText;
    ConstraintLayout container;
    TabLayout shiporderLinesTabLayout;
    ViewPager shiporderLinesViewPager;

    ShiporderLinesPagerAdapter shiporderLinesPagerAdapter;
    cPickorderViewModel pickorderViewModel;
    cPickorderBarcodeViewModel shiporderBarcodeViewModel;
    cWarehouseorderViewModel warehouseorderViewModel;
    cPickorderLinePackAndShipViewModel pickorderLinePackAndShipViewModel;
    cShippingAgentServiceShippingUnitViewModel shippingAgentServiceShippingUnitViewModel;
    cBarcodeLayoutViewModel barcodeLayoutViewModel;
    cCommentViewModel commentViewModel;
    cSettingsViewModel settingsViewModel;
    cWeberrorViewModel weberrorViewModel;
    CardView chosenOrderContainer;
    ConstraintLayout defaultOrderInfoCardViewExtraInfoContainer;
    ImageView imageButtonComments;
    RecyclerView recyclerViewPickorderLinesTotal;
    //endregion controls



    //region barcodereceiver
    IntentFilter barcodeIntentFilter;
    private BroadcastReceiver barcodeReceiver;
    //endregion barcodereceiver

    //region settings
    //Boolean pickBinManualBln;

    //endregion settings

    //region currents
    String currentUser;
    String currentBranch;
    String currentOrder;
    cPickorderEntity pickorderEntity;
    Boolean isPVWorkflow;

    cPickorderLinePackAndShipEntity chosenShiporderLine;
    cWorkplaceEntity chosenWorkplace;
    //endregions currents

    //region toolbar
    ImageView toolbarImage;
    TextView toolbarTitle;
    ImageView toolbarImageHelp;
    //endregion toolbar


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shiporderlines);
        thisContext = this;
        thisActivity = this;

        currentOrder = cSharedPreferences.getSharedPreferenceString(cPublicDefinitions.PREFERENCE_CURRENT_ORDER, "");
        currentUser = cSharedPreferences.getSharedPreferenceString(cPublicDefinitions.PREFERENCE_CURRENT_USER, "");
        currentBranch = cSharedPreferences.getSharedPreferenceString(cPublicDefinitions.PREFERENCE_CURRENT_BRANCH, "");

        barcodeLayoutViewModel = ViewModelProviders.of(this).get(cBarcodeLayoutViewModel.class);
        pickorderLinePackAndShipViewModel = ViewModelProviders.of(this).get(cPickorderLinePackAndShipViewModel.class);
        warehouseorderViewModel = ViewModelProviders.of(this).get(cWarehouseorderViewModel.class);

        shiporderBarcodeViewModel = ViewModelProviders.of(this).get(cPickorderBarcodeViewModel.class);
        pickorderViewModel = ViewModelProviders.of(this).get(cPickorderViewModel.class);
        pickorderEntity = pickorderViewModel.getPickorderByOrderNumber(currentOrder);
        shippingAgentServiceShippingUnitViewModel = ViewModelProviders.of(this).get(cShippingAgentServiceShippingUnitViewModel.class);
        shippingAgentServiceShippingUnitViewModel.resetShippingUnitQuantityUsed();
        commentViewModel = ViewModelProviders.of(this).get(cCommentViewModel.class);
        weberrorViewModel = ViewModelProviders.of(this).get(cWeberrorViewModel.class);
        //todo: put this back
//        pickorderLinePackAndShipViewModel.getNotHandledPickorderLineEntities().observe(this, new Observer<List<cPickorderLineEntity>>() {
//            @Override
//            public void onChanged(@Nullable List<cPickorderLineEntity> cPickorderLineEntities) {
//                if (cPickorderLineEntities != null) {
//                    m_setCounters();
//                }
//            }
//        });
        shiporderBarcodeViewModel.getPickorderBarcodes(true, currentBranch, currentOrder).observe(this, new Observer<List<cPickorderBarcodeEntity>>() {
            @Override
            public void onChanged(@Nullable List<cPickorderBarcodeEntity> pickorderBarcodeEntities) {
                if (pickorderBarcodeEntities != null) {
                }
            }
        });

        settingsViewModel = ViewModelProviders.of(this).get(cSettingsViewModel.class);
        fragmentManager = getSupportFragmentManager();

        m_findViews();
        m_barcodeReceiver();
        m_setSettings();
        m_fieldsInitialize();
        m_setToolbar();
        m_setListeners();
        m_initProgram();
        mCheckAllDone();

        ViewCompat.setTransitionName(textViewChosenOrder, VIEW_CHOSEN_ORDER);
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
    protected void onResume() {
        try {
            registerReceiver(barcodeReceiver, barcodeIntentFilter);
        }
        catch (Exception e) {
            String sdfg = e.getMessage();
        }
        super.onResume();
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
        toolbarImageHelp = findViewById(R.id.toolbarImageHelp);
        quantityShipordersText = findViewById(R.id.quantityShipordersText);
        shiporderLinesTabLayout = findViewById(R.id.shiporderLinesTabLayout);
        shiporderLinesViewPager = findViewById(R.id.shiporderLinesViewpager);
        //currentLocationView = findViewById(R.id.currentLocationView);
        //textViewSelectedBin = findViewById(R.id.textViewSelectedBin);
        textViewChosenOrder = findViewById(R.id.textViewChosenOrder);
        chosenOrderContainer = findViewById(R.id.chosenOrderContainer);
        imageButtonComments = findViewById(R.id.imageButtonComments);
//        imageViewIdea = findViewById(R.id.imageViewIdea);
//        textViewAction = findViewById(R.id.textViewAction);
    }
    private void m_setSettings() {
        String workflow = pickorderEntity.getOrdertype();
        if (workflow.equalsIgnoreCase(cPickorder.eOrderTypes.PV.toString())) {
            isPVWorkflow = true;
        }
        else {
            isPVWorkflow = false;
        }
//        String pickBinManual = settingsViewModel.getSetting(cSettingsEnums.p_Settings14Enu.PICK_BIN_MANUAL.toString());
//        pickBinManualBln = cText.stringToBoolean(pickBinManual, false);
    }
    private void m_fieldsInitialize() {
        int numberToShip = pickorderLinePackAndShipViewModel.getNumberToShipForCounter();
        int numberTotal = pickorderLinePackAndShipViewModel.getNumberTotalForCounter();

        quantityShipordersText.setText(numberToShip + "/" + numberTotal);

        textViewChosenOrder.setText(currentOrder);
        shiporderLinesTabLayout.addTab(shiporderLinesTabLayout.newTab().setText(R.string.tab_shiporderline_toship));
        shiporderLinesTabLayout.addTab(shiporderLinesTabLayout.newTab().setText(R.string.tab_shiporderline_shipped));
        shiporderLinesTabLayout.addTab(shiporderLinesTabLayout.newTab().setText(R.string.tab_shiporderline_total));

        shiporderLinesPagerAdapter = new ShiporderLinesPagerAdapter(getSupportFragmentManager(), shiporderLinesTabLayout.getTabCount());
        shiporderLinesViewPager.setAdapter(shiporderLinesPagerAdapter);
        shiporderLinesViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(shiporderLinesTabLayout));
        shiporderLinesTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                shiporderLinesViewPager.setCurrentItem(tab.getPosition());
                m_setTabselected(tab);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
    private void m_setTabselected(TabLayout.Tab tab) {
        Integer tabSelected = tab.getPosition();
        int numberToShip = pickorderLinePackAndShipViewModel.getNumberToShipForCounter();
        int numberShipped = pickorderLinePackAndShipViewModel.getNumberShippedForCounter();
        int numberTotal = pickorderLinePackAndShipViewModel.getNumberTotalForCounter();

        switch(tabSelected) {
            case 0:
                quantityShipordersText.setText(numberToShip + "/" + numberTotal);
                //currentLocationView.setVisibility(View.VISIBLE);
                break;
            case 1:
                quantityShipordersText.setText(numberShipped + "/" + numberTotal);
                //currentLocationView.setVisibility(View.INVISIBLE);
                break;
            case 2:
                quantityShipordersText.setText(numberTotal + "/" + numberTotal);
                //currentLocationView.setVisibility(View.INVISIBLE);
                break;
            default:
                //currentLocationView.setVisibility(View.VISIBLE);
        }
    }
    public void mSetCountersTab(int numberShown, int totalShown) {
        quantityShipordersText.setText(numberShown + "/" + totalShown);
    }

    private void m_setCounters() {
        Double totalArticlesDbl = pickorderLinePackAndShipViewModel.getTotalArticles();
        Integer totalArticlesInt = totalArticlesDbl.intValue();
        Double handledArticlesDbl = pickorderLinePackAndShipViewModel.getHandledArticles();
        Integer handledArticlesInt = handledArticlesDbl.intValue();

    }
    private void m_setToolbar() {
        toolbarImage.setImageResource(R.drawable.ic_menu_ship);
        toolbarTitle.setText(R.string.screentitle_shiporderlines);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
    private void m_setListeners() {
        mSetWeberrorOberver();
    }

    private void m_initProgram() {

        cPickorderEntity pickorder = pickorderViewModel.getPickorderByOrderNumber(currentOrder);
        String workplace = pickorder.getWorkplace().trim();
        if (workplace.isEmpty()) {
            m_showWorkplacePicker();
        }
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

    public void setChosenShiporderLineToReset(cPickorderLinePackAndShipGroupBySourceNo pickorderLinePackAndShipGroupBySourceNo) {
        for (Fragment fragment: fragmentManager.getFragments()) {
            if (fragment instanceof ShiporderLinesShippedFragment) {
                //((ShiporderLinesShippedFragment) fragment).setChosenShiporderLinePackAndShipEntity(pickorderLinePackAndShipGroupBySourceNo);
            }
        }
    }

    private void mSetChoseorderContainerListener() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            ((ViewGroup) findViewById(R.id.chosenOrderContainer)).getLayoutTransition()
                    .enableTransitionType(LayoutTransition.CHANGING);
        }
        chosenOrderContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (defaultOrderInfoCardViewExtraInfoContainer.getVisibility() == View.GONE || defaultOrderInfoCardViewExtraInfoContainer.getVisibility() == View.INVISIBLE) {
                    defaultOrderInfoCardViewExtraInfoContainer.setVisibility(View.VISIBLE);
                }
                else {
                    defaultOrderInfoCardViewExtraInfoContainer.setVisibility(View.GONE);
                }
            }
        });
    }

    public cPickorderLinePackAndShipEntity getChosenShiporderLine() {
        return chosenShiporderLine;
    }

    private void m_handleScan(String barcode) {
        //if workplacefragment is active, let that one handle the scan
        List<Fragment> fragments = fragmentManager.getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                if (fragment instanceof WorkplaceFragment) {
                    return;
                }
            }
        }

        m_checkAndCloseOpenDialogs();
        Boolean isSingleArticle = false;
        if (pickorderEntity.getSingleArticleOrders().equalsIgnoreCase("1")) {
            isSingleArticle = true;
        }

        Boolean salesOrderScanned = false;
        Boolean pickCartBoxScanned = false;
        String barcodeWithoutPrefixStr = "";
        if (cRegex.hasPrefix(barcode)) {
            List<cBarcodeLayoutEntity> salesOrderLayouts = barcodeLayoutViewModel.getBarcodeLayoutsOfType(cBarcodeLayout.barcodeLayoutEnu.SALESORDER.toString());
            Boolean foundBln = false;
            for (cBarcodeLayoutEntity layout : salesOrderLayouts) {
                if (cRegex.p_checkRegexBln(layout.getLayoutValue(), barcode)) {
                    foundBln = true;
                }
            }
            if (foundBln) {
                //has prefix, is salesorder
                salesOrderScanned = true;
                barcodeWithoutPrefixStr = cRegex.p_stripRegexPrefixStr(barcode);

            }
            else {
                //has prefix, isn't salesorder
                if (isPVWorkflow) {
                    Boolean foundPickCart = false;
                    List<cBarcodeLayoutEntity> pickCartBoxLayouts = barcodeLayoutViewModel.getBarcodeLayoutsOfType(cBarcodeLayout.barcodeLayoutEnu.PICKCARTBOX.toString());
                    for (cBarcodeLayoutEntity barcodeLayoutEntity : pickCartBoxLayouts) {
                        if (cRegex.p_checkRegexBln(barcodeLayoutEntity.getLayoutValue(), barcode)) {
                            foundPickCart = true;
                        }
                    }
                    if (foundPickCart) {
                        pickCartBoxScanned = true;
                        barcodeWithoutPrefixStr = cRegex.p_stripRegexPrefixStr(barcode);
                    }
                }
                else {
                    cUserInterface.doExplodingScreen(getString(R.string.message_unknown_salesorder), barcodeWithoutPrefixStr, true,true);
                }

            }
        }
        else {
            //no prefix, fine
            salesOrderScanned = true;
            barcodeWithoutPrefixStr = barcode;
        }
        if (salesOrderScanned) {
            List<cPickorderLinePackAndShipEntity> pickorderLinePackAndShipEntities = pickorderLinePackAndShipViewModel.getNotHandledPickorderLinePackAndShipEntitiesBySourceNo(barcodeWithoutPrefixStr);

                if (pickorderLinePackAndShipEntities !=null && pickorderLinePackAndShipEntities.size() > 0) {
                    Intent intent = new Intent(thisContext, ShipDetermineTransportActivity.class);
                    if (chosenWorkplace != null) {
                        intent.putExtra(cPublicDefinitions.SHIPPING_CHOSENWORKPLACE, chosenWorkplace.getWorkplace());
                    }
                    else {
                        intent.putExtra(cPublicDefinitions.SHIPPING_CHOSENWORKPLACE, pickorderEntity.getWorkplace());
                    }
                    intent.putExtra(cPublicDefinitions.SHIPPING_CHOSENSOURCENO, barcodeWithoutPrefixStr);
                    startActivity(intent);
                }
                else {
                    if (isSingleArticle) {
                        checkArticles(barcodeWithoutPrefixStr);
                    }
                    else {
                        cUserInterface.doExplodingScreen(getString(R.string.message_unknown_salesorder), barcodeWithoutPrefixStr, true,true);
                    }
                }
        }
        else if (isPVWorkflow && pickCartBoxScanned) {
            List<cPickorderLinePackAndShipEntity> pickorderLinePackAndShipEntities = pickorderLinePackAndShipViewModel.getNotHandledPickorderLinePackAndShipEntitiesByProcessingSequence(barcodeWithoutPrefixStr);

            if (pickorderLinePackAndShipEntities !=null && pickorderLinePackAndShipEntities.size() > 0) {
                Intent intent = new Intent(thisContext, ShipDetermineTransportActivity.class);
                if (chosenWorkplace != null) {
                    intent.putExtra(cPublicDefinitions.SHIPPING_CHOSENWORKPLACE, chosenWorkplace.getWorkplace());
                }
                else {
                    intent.putExtra(cPublicDefinitions.SHIPPING_CHOSENWORKPLACE, pickorderEntity.getWorkplace());
                }
                String l_chosensourceno = pickorderLinePackAndShipEntities.get(0).getSourceno();
                intent.putExtra(cPublicDefinitions.SHIPPING_CHOSENSOURCENO, l_chosensourceno);
                startActivity(intent);
            }
            else {
                if (isSingleArticle) {
                    checkArticles(barcodeWithoutPrefixStr);
                }
                else {
                    cUserInterface.doExplodingScreen(getString(R.string.message_unknown_pickcartbox), barcodeWithoutPrefixStr, true,true);
                }
            }
        }

    }

    private void checkArticles(final String barcodeWithoutPrefixStr) {
        pickorderLinePackAndShipViewModel.getNotHandledPickorderLinePackAndShipEntities().observe(this, new Observer<List<cPickorderLinePackAndShipEntity>>() {
            @Override
            public void onChanged(@Nullable List<cPickorderLinePackAndShipEntity> pickorderLinePackAndShipEntities) {
                Boolean barcodeFound = false;
                for (cPickorderLinePackAndShipEntity pickorderLinePackAndShipEntity : pickorderLinePackAndShipEntities) {
                    String itemNo = pickorderLinePackAndShipEntity.getItemno();
                    String variantCode = pickorderLinePackAndShipEntity.getVariantcode();
                    List<cPickorderBarcodeEntity> pickorderBarcodeEntities = shiporderBarcodeViewModel.getPickorderBarcodesForItemVariantCode(itemNo,variantCode);
                    for (cPickorderBarcodeEntity pickorderBarcodeEntity : pickorderBarcodeEntities) {
                        if (pickorderBarcodeEntity.getBarcode().equalsIgnoreCase(barcodeWithoutPrefixStr)) {
                            //barcode found
                            barcodeFound = true;
                            goShip(pickorderLinePackAndShipEntity);
                            break;

                        }
                    }
                }
                if (!barcodeFound) {
                    cUserInterface.doExplodingScreen(getString(R.string.error_unknown_salesorder_or_article), barcodeWithoutPrefixStr, true,true);
                }
            }
        });

    }
    private void goShip(cPickorderLinePackAndShipEntity pickorderLinePackAndShipEntity) {
        Intent intent = new Intent(thisContext, ShipDetermineTransportActivity.class);
        String workplace = pickorderEntity.getWorkplace();
        intent.putExtra(cPublicDefinitions.SHIPPING_CHOSENWORKPLACE, workplace);
        intent.putExtra(cPublicDefinitions.SHIPPING_CHOSENSOURCENO, pickorderLinePackAndShipEntity.getSourceno());
        startActivity(intent);
    }
    public void setChosenShiporderLine(cPickorderLinePackAndShipGroupBySourceNo pickorderLinePackAndShipGroupBySourceNo) {
        for (Fragment fragment: fragmentManager.getFragments()) {
            if (fragment instanceof ShiporderLinesToShipFragment) {
                ((ShiporderLinesToShipFragment) fragment).setChosenPickOrderLinePackAndShip(pickorderLinePackAndShipGroupBySourceNo);
            }
        }
        List<cPickorderLinePackAndShipEntity> pickorderLinePackAndShipEntities =pickorderLinePackAndShipViewModel.getNotHandledPickorderLinePackAndShipEntitiesBySourceNo(pickorderLinePackAndShipGroupBySourceNo.getSourceno());
        chosenShiporderLine = pickorderLinePackAndShipEntities.get(0);
    }

    private void m_showWorkplacePicker() {
        final android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        final WorkplaceFragment workplaceFragment = new WorkplaceFragment();
        workplaceFragment.show(fragmentManager, WORKPLACEFRAGMENT_TAG);
    }

    private void doUnknownScan(String errormessage, String barcode) {
        cUserInterface.doExplodingScreen(errormessage, barcode, true, true );
    }
    private void m_checkAndCloseOpenDialogs() {
        List<Fragment> fragments = fragmentManager.getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                if (fragment instanceof DialogFragment) {
                    ((DialogFragment) fragment).dismiss();
                }
            }
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                m_checkAndCloseOpenDialogs();
                AlertDialog.Builder builder = new AlertDialog.Builder(thisContext);
                builder.setTitle(R.string.message_sure_leave_screen_title);
                builder.setMessage(getString(R.string.message_sure_leave_screen_text));
                builder.setPositiveButton(R.string.message_sure_leave_screen_positive, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                         mGoBack();
                    }
                });
                builder.setNegativeButton(R.string.message_sure_leave_screen_negative, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void mGoBack() {
        if (mUnLockWarehouseOrder()) {
            Intent intent = new Intent(thisContext, ShiporderSelectActivity.class);
            startActivity(intent);
        }
        else {
          //todo: what to do?
        }

    }
    private Boolean mUnLockWarehouseOrder() {
        if (warehouseorderViewModel.getLockReleaseWarehouseorderBln(currentUser, "", cWarehouseorder.ordertypes.PICKEN.toString(), currentBranch, currentOrder, cDeviceInfo.getSerialnumber(), cWarehouseorder.stepCodes.Pick_PackAndShip.toString(), 40)) {
            //we've achieved unlock!
            return true;
        }
        else {
            return false;
        }
    }
    public void setChosenWorkplace(cWorkplaceEntity workplaceEntity) {
        chosenWorkplace = workplaceEntity;
        android.support.v4.app.Fragment l_FragmentFrg = getSupportFragmentManager().findFragmentByTag(WORKPLACEFRAGMENT_TAG);
        if (l_FragmentFrg != null) {
            android.support.v4.app.DialogFragment l_DialogFragmentDfr = (android.support.v4.app.DialogFragment) l_FragmentFrg;
            l_DialogFragmentDfr.dismiss();
        }
        if (pickorderViewModel.pickorderUpdateWorkplace(currentUser, currentBranch, currentOrder, workplaceEntity.getWorkplace())) {
            pickorderViewModel.updatePickorderWorkplaceLocal(currentOrder, workplaceEntity.getWorkplace());
            cUserInterface.showSnackbarMessage(container, getString(R.string.workplace_updated),  R.raw.goodsound,false );
        }

    }


    private void mCheckAllDone() {
        pickorderLinePackAndShipViewModel.getNotHandledPickorderLinePackAndShipEntities().observe(this, new Observer<List<cPickorderLinePackAndShipEntity>>() {
            @Override
            public void onChanged(@Nullable List<cPickorderLinePackAndShipEntity> pickorderLinePackAndShipEntities) {
                if (pickorderLinePackAndShipEntities!=null && pickorderLinePackAndShipEntities.size() == 0) {
                    mAllDone();
                }
            }
        });
    }

    private void mAllDone() {
        m_checkAndCloseOpenDialogs();
        String orderDoneHeader = getString(R.string.message_header_orderdone, currentOrder);
        String orderDoneText = getString(R.string.message_text_orderdone);
        cUserInterface.playSound(R.raw.goodsound, null);
        final OrderDoneFragment orderDoneFragment = new OrderDoneFragment();
        Bundle bundle = new Bundle();
        bundle.putString(cPublicDefinitions.ORDERDONE_HEADER, orderDoneHeader);
        bundle.putString(cPublicDefinitions.ORDERDONE_TEXT, orderDoneText);
        bundle.putBoolean(cPublicDefinitions.ORDERDONE_SHOWCURRENTLOCATION, false);
        //bundle.putBoolean(cPublicDefinitions.ORDERDONE_SHOW_)
        orderDoneFragment.setArguments(bundle);
        orderDoneFragment.setCancelable(true);
        orderDoneFragment.show(fragmentManager, cPublicDefinitions.ORDERDONE_TAG);
    }

    public void closeCurrentOrder() {
        Boolean orderClosed = pickorderViewModel.pickorderStepHandled(currentUser, "", currentBranch, currentOrder, cDeviceInfo.getSerialnumber(),pickorderEntity.getWorkplace() , "3",40, "" );
        if (orderClosed) {
            cUserInterface.showToastMessage("closed", null);
        }
        else {
            cUserInterface.showToastMessage( "not closed", null);
        }
        Intent intent = new Intent(thisContext, ShiporderSelectActivity.class);
        startActivity(intent);
    }



}
