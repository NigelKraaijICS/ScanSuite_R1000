package nl.icsvertex.scansuite.activities.pick;

import android.animation.LayoutTransition;
//import android.app.Fragment;
import android.app.AlertDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.List;

import ICS.Interfaces.iICSDefaultActivity;
import ICS.Weberror.cWeberrorEntity;
import ICS.Weberror.cWeberrorViewModel;
import SSU_WHS.BarcodeLayouts.cBarcodeLayout;
import SSU_WHS.BarcodeLayouts.cBarcodeLayoutEntity;
import SSU_WHS.BarcodeLayouts.cBarcodeLayoutViewModel;
import SSU_WHS.Comments.cCommentEntity;
import SSU_WHS.Comments.cCommentViewModel;
import SSU_WHS.PickorderBarcodes.cPickorderBarcodeEntity;
import SSU_WHS.PickorderBarcodes.cPickorderBarcodeViewModel;
import SSU_WHS.PickorderLinePackAndShip.cPickorderLinePackAndShipEntity;
import SSU_WHS.PickorderLinePackAndShip.cPickorderLinePackAndShipViewModel;
import SSU_WHS.PickorderLines.cPickorderLineEntity;
import SSU_WHS.PickorderLines.cPickorderLineViewModel;
import SSU_WHS.Pickorders.cPickorder;
import SSU_WHS.Pickorders.cPickorderEntity;
import SSU_WHS.Pickorders.cPickorderViewModel;
import SSU_WHS.Settings.cSettingsEnums;
import SSU_WHS.Settings.cSettingsViewModel;
import SSU_WHS.Warehouseorder.cWarehouseorder;
import SSU_WHS.Warehouseorder.cWarehouseorderViewModel;
import SSU_WHS.Webservice.cWebservice;
import SSU_WHS.cAppExtension;
import SSU_WHS.cPublicDefinitions;
import ICS.Utils.Scanning.cBarcodeScanDefinitions;
import ICS.Utils.cDeviceInfo;
import ICS.Utils.cRegex;
import ICS.Utils.cSharedPreferences;
import ICS.Utils.cText;
import ICS.Utils.cUserInterface;
import nl.icsvertex.scansuite.activities.ship.ShiporderLinesActivity;
import nl.icsvertex.scansuite.activities.sort.SortorderLinesActivity;
import nl.icsvertex.scansuite.fragments.dialogs.CommentFragment;
import nl.icsvertex.scansuite.fragments.dialogs.CurrentLocationFragment;
import nl.icsvertex.scansuite.fragments.dialogs.OrderDoneFragment;
import nl.icsvertex.scansuite.fragments.pick.PickorderLinesPickedFragment;
import nl.icsvertex.scansuite.fragments.pick.PickorderLinesToPickFragment;
import nl.icsvertex.scansuite.PickorderLinesPagerAdapter;
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

public class PickorderLinesActivity extends AppCompatActivity implements iICSDefaultActivity {
    public static final String VIEW_CHOSEN_ORDER = "detail:header:text";

    static final String HUGEERROR_TAG = "HUGEERROR_TAG";
    static final String COMMENTFRAGMENT_TAG = "COMMENTFRAGMENT_TAG";
    static final String ACTIVITYNAME = "PickorderLinesActivity";

    boolean hasErrors = false;
    TextView textViewChosenOrder;
    TextView quantityPickordersText;
    ConstraintLayout container;
    TabLayout pickorderLinesTabLayout;
    ViewPager pickorderLinesViewPager;
    PickorderLinesPagerAdapter pickorderLinesPagerAdapter;

    CardView chosenOrderContainer;
    ConstraintLayout defaultOrderInfoCardViewExtraInfoContainer;
    ImageView imageButtonComments;

    cBarcodeLayoutViewModel barcodeLayoutViewModel;
    List<cBarcodeLayoutEntity> binBarcodeLayouts;
    //endregion controls

    //region viewModels
    cWeberrorViewModel weberrorViewModel;
    cPickorderBarcodeViewModel pickorderBarcodeViewModel;
    cPickorderLinePackAndShipViewModel pickorderLinePackAndShipViewModel;
    cPickorderLineViewModel pickorderLineViewModel;
    cWarehouseorderViewModel warehouseorderViewModel;
    cPickorderViewModel pickorderViewModel;
    cCommentViewModel commentViewModel;
    cSettingsViewModel settingsViewModel;
    //endregion viewModels


    //region barcodereceiver
    IntentFilter barcodeIntentFilter;
    private BroadcastReceiver barcodeReceiver;
    //endregion barcodereceiver

    //region settings
    //Boolean pickBinManualBln;
    Boolean pickWithPictureBln;
    Boolean pickWithPicturePrefetchBln;
    Boolean pickBinIsItemBln;
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

    //region currents
    String currentUser;
    String currentBranch;
    String currentOrder;
    cPickorderLineEntity chosenPickorderLine;
    cPickorderEntity currentPickOrderEntity;

    //endregions currents

    //region toolbar
    ImageView toolbarImage;
    TextView toolbarTitle;
    ImageView toolbarImageHelp;
    //endregion toolbar


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickorderlines);

        currentOrder = cSharedPreferences.getSharedPreferenceString(cPublicDefinitions.PREFERENCE_CURRENT_ORDER, "");
        currentUser = cSharedPreferences.getSharedPreferenceString(cPublicDefinitions.PREFERENCE_CURRENT_USER, "");
        currentBranch = cSharedPreferences.getSharedPreferenceString(cPublicDefinitions.PREFERENCE_CURRENT_BRANCH, "");

        Boolean showComments;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            try {
                showComments = extras.getBoolean(cPublicDefinitions.SHOWCOMMENTS_EXTRA);
            } catch (Exception e) {
                showComments = false;
            }
        }
        else {
            showComments = false;
        }

        mActivityInitialize();


        if (showComments) {
            commentViewModel.getComments(true, currentBranch, currentOrder).observe(this, new Observer<List<cCommentEntity>>() {
                @Override
                public void onChanged(@Nullable List<cCommentEntity> commentEntities) {
                    if (commentEntities != null && commentEntities.size() > 0) {
                        m_showComments();
                        imageButtonComments.setVisibility(View.VISIBLE);
                    }
                    else {
                        imageButtonComments.setVisibility(View.GONE);
                    }
                }
            });
        }
        else {
            if (commentViewModel.getAll().size() > 0) {
                imageButtonComments.setVisibility(View.VISIBLE);
            }
            else {
                imageButtonComments.setVisibility(View.GONE);

            }
        }

        mBarcodeReceiver();

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
        registerReceiver(barcodeReceiver, barcodeIntentFilter);
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

    @Override
    public void mActivityInitialize() {
        mSetAppExtensions();

        mFindViews();

        mSetViewModels();

        mSetSettings();

        mSetToolbar(getResources().getString(R.string.screentitle_pickorderlines));

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
        container = findViewById(R.id.container);
        toolbarImage = findViewById(R.id.toolbarImage);
        toolbarTitle = findViewById(R.id.toolbarTitle);
        toolbarImageHelp = findViewById(R.id.toolbarImageHelp);
        quantityPickordersText = findViewById(R.id.quantityPickordersText);
        pickorderLinesTabLayout = findViewById(R.id.pickorderLinesTabLayout);
        pickorderLinesViewPager = findViewById(R.id.pickorderLinesViewpager);
        textViewChosenOrder = findViewById(R.id.textViewChosenOrder);
        chosenOrderContainer = findViewById(R.id.chosenOrderContainer);
        imageButtonComments = findViewById(R.id.imageButtonComments);
    }

    @Override
    public void mSetViewModels() {
        weberrorViewModel = ViewModelProviders.of(this).get(cWeberrorViewModel.class);
        pickorderLinePackAndShipViewModel = ViewModelProviders.of(this).get(cPickorderLinePackAndShipViewModel.class);
        pickorderLineViewModel = ViewModelProviders.of(this).get(cPickorderLineViewModel.class);
        pickorderBarcodeViewModel = ViewModelProviders.of(this).get(cPickorderBarcodeViewModel.class);
        warehouseorderViewModel = ViewModelProviders.of(this).get(cWarehouseorderViewModel.class);
        commentViewModel = ViewModelProviders.of(this).get(cCommentViewModel.class);
        pickorderViewModel = ViewModelProviders.of(this).get(cPickorderViewModel.class);
        currentPickOrderEntity = pickorderViewModel.getPickorderByOrderNumber(currentOrder);
        pickorderLineViewModel.getNotHandledPickorderLineEntities().observe(this, new Observer<List<cPickorderLineEntity>>() {
            @Override
            public void onChanged(@Nullable List<cPickorderLineEntity> cPickorderLineEntities) {
                if (cPickorderLineEntities != null) {
                    //m_setCounters();
                }
            }
        });
        barcodeLayoutViewModel = ViewModelProviders.of(this).get(cBarcodeLayoutViewModel.class);
        binBarcodeLayouts = barcodeLayoutViewModel.getBarcodeLayoutsOfType(cBarcodeLayout.barcodeLayoutEnu.BIN.toString());

        settingsViewModel = ViewModelProviders.of(this).get(cSettingsViewModel.class);

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
        int numberNotPicked = pickorderLineViewModel.getNumberNotHandledForCounter();
        int numberTotalNotPicked = pickorderLineViewModel.getNumberTotalNotHandledForCounter();

        quantityPickordersText.setText(numberNotPicked + "/" + numberTotalNotPicked);
        textViewChosenOrder.setText(currentOrder);
        pickorderLinesTabLayout.addTab(pickorderLinesTabLayout.newTab().setText(R.string.tab_pickorderline_topick));
        pickorderLinesTabLayout.addTab(pickorderLinesTabLayout.newTab().setText(R.string.tab_pickorderline_picked));
        pickorderLinesTabLayout.addTab(pickorderLinesTabLayout.newTab().setText(R.string.tab_pickorderline_total));
        pickorderLinesPagerAdapter = new PickorderLinesPagerAdapter(getSupportFragmentManager(), pickorderLinesTabLayout.getTabCount());
        pickorderLinesViewPager.setAdapter(pickorderLinesPagerAdapter);
        pickorderLinesViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(pickorderLinesTabLayout));
        pickorderLinesTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pickorderLinesViewPager.setCurrentItem(tab.getPosition());
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

    @Override
    public void mSetListeners() {
        mSetShowCommentListener();
        mSetWeberrorOberver();
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
                mHandleScan(barcodeStr);
            }
        };
        //don't forget to unregister on destroy and onpause.
        registerReceiver(barcodeReceiver,barcodeIntentFilter);

    }
    @Override
    public void mSetSettings() {
        String pickWithPicture = settingsViewModel.getSetting(cSettingsEnums.p_Settings14Enu.PICK_WITH_PICTURE.toString());
        pickWithPictureBln = cText.stringToBoolean(pickWithPicture, false);
        String pickWithPicturePrefetch = settingsViewModel.getSetting(cSettingsEnums.p_Settings14Enu.PICK_WITH_PICTURE_PREFETCH.toString());
        pickWithPicturePrefetchBln = cText.stringToBoolean(pickWithPicturePrefetch, false);
        String pickBinIsItem = settingsViewModel.getSetting(cSettingsEnums.p_Settings14Enu.PICK_BIN_IS_ITEM.toString());
        pickBinIsItemBln = cText.stringToBoolean(pickBinIsItem, false);
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

    private void m_setTabselected(TabLayout.Tab tab) {
        Integer tabSelected = tab.getPosition();
        int numberNotPicked = pickorderLineViewModel.getNumberNotHandledForCounter();
        int numberTotalNotPicked = pickorderLineViewModel.getNumberTotalNotHandledForCounter();

        int numberPicked = pickorderLineViewModel.getNumberHandledForCounter();
        int numberTotalPicked = pickorderLineViewModel.getNumberTotalHandledForCounter();

        int numberTotal = pickorderLineViewModel.getNumberTotalForCounter();
        int numberTotalTotal = pickorderLineViewModel.getNumberTotalTotalForCounter();

        switch(tabSelected) {
            case 0:
                quantityPickordersText.setText(numberNotPicked + "/" + numberTotalNotPicked);
                //currentLocationView.setVisibility(View.VISIBLE);
                break;
            case 1:
                quantityPickordersText.setText(numberPicked + "/" + numberTotalPicked);
                //currentLocationView.setVisibility(View.INVISIBLE);
                break;
            case 2:
                quantityPickordersText.setText(numberTotal + "/" + numberTotalTotal);
                //currentLocationView.setVisibility(View.INVISIBLE);
                break;
            default:
                //currentLocationView.setVisibility(View.VISIBLE);
        }
    }
    private void m_setCounters() {
        Double totalArticlesDbl = pickorderLineViewModel.getTotalArticles();
        Integer totalArticlesInt = totalArticlesDbl.intValue();
        Double handledArticlesDbl = pickorderLineViewModel.getHandledArticles();
        Integer handledArticlesInt = handledArticlesDbl.intValue();
        quantityPickordersText.setText(handledArticlesInt + "/" + totalArticlesInt);
    }

    private void mSetWeberrorOberver() {
        final FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        weberrorViewModel.getAllForActivityLive(ACTIVITYNAME).observe(this, new Observer<List<cWeberrorEntity>>() {
            @Override
            public void onChanged(@Nullable List<cWeberrorEntity> cWeberrorEntities) {
                if (cWeberrorEntities != null && cWeberrorEntities.size() > 0) {
                    hasErrors = true;
                    //only if something is really wrong, else it's just a wrong login, probably
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

    private void mSetShowCommentListener() {
        imageButtonComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_showComments();
            }
        });
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

    public cPickorderLineEntity getChosenPickorderLine() {
        return chosenPickorderLine;
    }

    private void mHandleScan(String barcode) {
        //if currentlocationfragment is active, let that one handle the scan
        List<Fragment> fragments = cAppExtension.fragmentManager.getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                if (fragment instanceof CurrentLocationFragment) {
                    return;
                }
            }
        }
        //if orderdonefragment is active, let that one handle the scan
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                if (fragment instanceof OrderDoneFragment) {
                    return;
                }
            }
        }
        m_checkAndCloseOpenDialogs();
        String barcodeType = "";
        //did we scan a location?
        for (cBarcodeLayoutEntity barcodeLayout: binBarcodeLayouts) {
            String pattern = barcodeLayout.getLayoutValue();
            if (cRegex.p_checkRegexBln(pattern,barcode)) {
                barcodeType = cBarcodeLayout.barcodeLayoutEnu.BIN.toString();
                break;
            }
        }
        if (barcodeType.equalsIgnoreCase(cBarcodeLayout.barcodeLayoutEnu.BIN.toString())) {
            //we've scanned a bin
            String barcodewithoutPrefix = cRegex.p_stripRegexPrefixStr(barcode);
            cPickorderLineEntity pickorderLineEntityByBin = pickorderLineViewModel.getPickorderLineNotHandledByBin(barcodewithoutPrefix);
            if (pickorderLineEntityByBin != null) {
                //we have a location
                Intent intent = new Intent(cAppExtension.context, PickorderPickActivity.class);
                intent.putExtra(cPublicDefinitions.PICKING_CHOSENLOCATION, pickorderLineEntityByBin.getBincode());
                startActivity(intent);
                return;
            }
            else {
                doUnknownScan(getString(R.string.nothing_more_todo_for_bin), barcodewithoutPrefix);
                return;
            }
        }

        cPickorderLineEntity pickorderLineEntityByBin = pickorderLineViewModel.getPickorderLineNotHandledByBin(barcode);
            if (pickorderLineEntityByBin != null) {
                //we have a location
                Intent intent = new Intent(cAppExtension.context, PickorderPickActivity.class);
                intent.putExtra(cPublicDefinitions.PICKING_CHOSENLOCATION, pickorderLineEntityByBin.getBincode());
                startActivity(intent);
            }
            else {
                //no location
                if (pickBinIsItemBln) {
                    cPickorderBarcodeEntity pickorderBarcodeEntity = pickorderBarcodeViewModel.getPickOrderBarcodeByBarcode(barcode);
                    if (pickorderBarcodeEntity != null) {
                        //Alright. The barcode exists, but do we still have something to do?
                        List<cPickorderLineEntity> pickorderLineEntities = pickorderLineViewModel.getNotHandledPickorderLinesByItemNoandVariantCode(pickorderBarcodeEntity.getItemno(), pickorderBarcodeEntity.getVariantcode());
                        if (pickorderLineEntities.size() == 0) {
                            doUnknownScan(getString(R.string.error_no_more_articles_to_pick), barcode);
                        }
                        else {
                            //start picking with scanned barcode
                            Intent intent = new Intent(cAppExtension.context, PickorderPickActivity.class);
                            intent.putExtra(cPublicDefinitions.PICKING_SCANNEDBARCODE, pickorderBarcodeEntity.getBarcode());
                            startActivity(intent);
                        }
                    }
                    else {
                        //unknown scan
                        doUnknownScan(getString(R.string.error_unknown_barcode), barcode);
                    }
                }
                else {
                    //unknown scan
                    doUnknownScan(getString(R.string.error_unknown_barcode), barcode);
                }
            }
    }
    public void setChosenPickorderLine(cPickorderLineEntity pickorderLineEntity) {
        //PickorderLinesToPickFragment pickorderLinesToPickFragment = (PickorderLinesToPickFragment) fragmentManager.findFragmentById(R.id.fragmentPickorderLinesToPick);
        for (Fragment fragment: cAppExtension.fragmentManager.getFragments()) {
            if (fragment instanceof PickorderLinesToPickFragment) {
                ((PickorderLinesToPickFragment) fragment).setChosenPickorderLine(pickorderLineEntity);
            }
        }
        //pickorderLinesToPickFragment.setChosenPickorderLine(pickorderLineEntity);
        //textViewSelectedBin.setText(pickorderLineEntity.getBincode());
        chosenPickorderLine = pickorderLineEntity;
    }
    public void setChosenPickorderLineToReset(cPickorderLineEntity pickorderLineEntity) {
        for (Fragment fragment: cAppExtension.fragmentManager.getFragments()) {
            if (fragment instanceof PickorderLinesPickedFragment) {
                ((PickorderLinesPickedFragment) fragment).setChosenPickorderLine(pickorderLineEntity);
            }
        }
    }

    private void doUnknownScan(String errormessage, String barcode) {
        cUserInterface.doExplodingScreen(errormessage, barcode, true, true );
    }
    private void m_checkAndCloseOpenDialogs() {
        List<Fragment> fragments = cAppExtension.fragmentManager.getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                if (fragment instanceof DialogFragment) {
                    ((DialogFragment) fragment).dismiss();
                }
            }
        }
    }
    private void m_showComments() {
        final android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        final CommentFragment commentFragment = new CommentFragment();
        commentFragment.show(fragmentManager, COMMENTFRAGMENT_TAG);
        cUserInterface.playSound( R.raw.message,0);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                        m_checkAndCloseOpenDialogs();
                        AlertDialog.Builder builder = new AlertDialog.Builder(cAppExtension.context);
                        builder.setTitle(R.string.message_sure_leave_screen_title);
                        builder.setMessage(getString(R.string.message_sure_leave_screen_text));
                        builder.setPositiveButton(R.string.message_sure_leave_screen_positive, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //check if we scanned anthing, or already have a currentlocation
                                cPickorderEntity pickorderEntity = pickorderViewModel.getPickorderByOrderNumber(currentOrder);
                                if (pickorderLineViewModel.getHandledArticles() > 0 && pickorderEntity != null && pickorderEntity.getCurrentlocation().isEmpty()) {
                                    mShowCurrentLocation();
                                }
                                else {
                                    mGoBack();
                                }
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
    private Boolean mUnLockWarehouseOrder() {
        if (warehouseorderViewModel.getLockReleaseWarehouseorderBln(currentUser, "", cWarehouseorder.ordertypes.PICKEN.toString(), currentBranch, currentOrder, cDeviceInfo.getSerialnumber(), cWarehouseorder.stepCodes.Pick_Picking.toString(), 10)) {
            //we've achieved unlock!
            return true;
        }
        else {
            return false;
        }
    }
    private void mGoBack() {
        if (mUnLockWarehouseOrder()) {
            Intent intent = new Intent(cAppExtension.context, PickorderSelectActivity.class);
            startActivity(intent);
        }
        else {
            //todo: what to do?
        }
    }

    private void mShowCurrentLocation() {
        m_checkAndCloseOpenDialogs();
        //String currentLocationHeader = getString(R.string.message_header_curre, currentOrder);
        //String currentLocationText = getString(R.string.message_text_orderdone);
        cUserInterface.playSound(R.raw.goodsound, null);
        final CurrentLocationFragment currentLocationFragment = new CurrentLocationFragment();
        currentLocationFragment.setCancelable(true);
        currentLocationFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.CURRENTLOCATION_TAG);
    }

    public void mSetCurrentLocation(String currentLocation) {
        Boolean updateCurrentLocation = warehouseorderViewModel.updateCurrentOrderLocation(currentUser, currentBranch, currentOrder, currentLocation);
        if (updateCurrentLocation) {
            mGoBack();
        }
        else {
            //todo: what to do?
        }
    }

private void mCheckAllDone() {
        if (m_allDone()) {
            m_showAllDone();
        }
}

    private Boolean m_allDone() {
        Boolean resultBln = false;
        List<cPickorderLineEntity> pickorderLineEntities = pickorderLineViewModel.getNotHandledPickorderLineEntitiesLin();
        if (pickorderLineEntities.size() == 0) {
            resultBln = true;
        }
        return resultBln;
    }

    private void m_showAllDone() {
        m_checkAndCloseOpenDialogs();
        String orderDoneHeader = getString(R.string.message_header_orderdone, currentOrder);
        String orderDoneText = getString(R.string.message_text_orderdone);
        cUserInterface.playSound( R.raw.goodsound, null);
        final OrderDoneFragment orderDoneFragment = new OrderDoneFragment();
        Bundle bundle = new Bundle();
        bundle.putString(cPublicDefinitions.ORDERDONE_HEADER, orderDoneHeader);
        bundle.putString(cPublicDefinitions.ORDERDONE_TEXT, orderDoneText);

        //if we haven't picked anything, don't show currentlocation
        boolean showCurrentLocation;
        if (pickorderLineViewModel.getHandledArticles() > 0) {
            showCurrentLocation = true;
        }
        else {
            showCurrentLocation = false;
        }
        bundle.putBoolean(cPublicDefinitions.ORDERDONE_SHOWCURRENTLOCATION, showCurrentLocation);
        orderDoneFragment.setArguments(bundle);
        orderDoneFragment.setCancelable(false);
        orderDoneFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.ORDERDONE_TAG);
    }

    public void closeCurrentOrder(String currentLocation) {

        if (!currentLocation.isEmpty()) {
            Boolean updateCurrentLocation = warehouseorderViewModel.updateCurrentOrderLocation(currentUser, currentBranch, currentOrder, currentLocation);
            if (updateCurrentLocation) {
                cUserInterface.showToastMessage("updated", null);
            }
            else {
                cUserInterface.showToastMessage("NOT updated", null);
            }
        }

        Boolean orderClosed = pickorderViewModel.pickorderStepHandled(currentUser, "", currentBranch, currentOrder, cDeviceInfo.getSerialnumber(), "", "0",10, "" );
        if (orderClosed) {
            //cUserInterface.showToastMessage(thisContext, "closed", null);
        }
        else {
            //todo: what to do?
        }

        if (currentLocation.isEmpty()) {
            //this happens when we picked 0, so we're done
            Intent intent4 = new Intent(cAppExtension.context, PickorderSelectActivity.class);
            startActivity(intent4);
            return;
        }

        if (currentPickOrderEntity == null || currentPickOrderEntity.getOrdertype().equalsIgnoreCase(cPickorder.eOrderTypes.BC.toString())) {
            switch (pickSortAutoStartStr.toUpperCase()) {
                case "J":
                case "Y":
                case "JA":
                case "YES":
                case "TRUE":
                    mGoSort();
                    break;
                case "N":
                case "NO":
                case "NEE":
                    Intent intent2 = new Intent(cAppExtension.context, PickorderSelectActivity.class);
                    startActivity(intent2);
                    break;
                case "":
                    mAskSort();
                default:
                    Intent intent3 = new Intent(cAppExtension.context, PickorderSelectActivity.class);
                    startActivity(intent3);
            }
        }
        else if (currentPickOrderEntity.getOrdertype().equalsIgnoreCase(cPickorder.eOrderTypes.BM.toString())) {
            switch (pickPackAndShipAutoStartStr.toUpperCase()) {
                case "J":
                case "Y":
                case "JA":
                case "YES":
                case "TRUE":
                    mGoShip();
                    break;
                case "N":
                case "NO":
                case "NEE":
                    Intent intent2 = new Intent(cAppExtension.context, PickorderSelectActivity.class);
                    startActivity(intent2);
                    break;
                case "":
                    mAskShip();
                    break;
                default:
                    Intent intent3 = new Intent(cAppExtension.context, PickorderSelectActivity.class);
                    startActivity(intent3);
            }
        }
        else if (currentPickOrderEntity.getOrdertype().equalsIgnoreCase(cPickorder.eOrderTypes.PV.toString())) {
            switch (pickPackAndShipAutoStartStr.toUpperCase()) {
                case "J":
                case "Y":
                case "JA":
                case "YES":
                case "TRUE":
                    mGoShip();
                    break;
                case "N":
                case "NO":
                case "NEE":
                    Intent intent2 = new Intent(cAppExtension.context, PickorderSelectActivity.class);
                    startActivity(intent2);
                    break;
                case "":
                    mAskShip();
                    break;
                default:
                    Intent intent3 = new Intent(cAppExtension.context, PickorderSelectActivity.class);
                    startActivity(intent3);
            }
        }
        else {
            switch (pickSortAutoStartStr.toUpperCase()) {
                case "J":
                case "Y":
                case "JA":
                case "YES":
                case "TRUE":
                    mGoSort();
                    break;
                case "N":
                case "NO":
                case "NEE":
                    Intent intent2 = new Intent(cAppExtension.context, PickorderSelectActivity.class);
                    startActivity(intent2);
                    break;
                case "":
                    mAskSort();
                    break;
                default:
                    Intent intent3 = new Intent(cAppExtension.context, PickorderSelectActivity.class);
                    startActivity(intent3);
            }
        }
    }
    private void mAskSort() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(getString(R.string.question_open_sort));
        builder.setTitle(getString(R.string.question_open_sort_title));
        builder.setPositiveButton(R.string.open, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                mGoSort();
            }
        });
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(cAppExtension.context, PickorderSelectActivity.class);
                startActivity(intent);
            }
        });
        builder.setIcon(R.drawable.ic_menu_sort);
        builder.show();
    }
    private void mAskShip() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(getString(R.string.question_open_ship));
        builder.setTitle(getString(R.string.question_open_ship_title));
        builder.setPositiveButton(R.string.open, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                mGoShip();
            }
        });
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(cAppExtension.context, PickorderSelectActivity.class);
                startActivity(intent);
            }
        });
        builder.setIcon(R.drawable.ic_menu_ship);
        builder.show();
    }
    private void mGoSort() {
        boolean forceRefresh = true;
        if (mLockWarehouseOrder(currentPickOrderEntity, true, 20)) {

            pickorderLineViewModel.getPickorderLines(forceRefresh,
                                                    cPickorderLineEntity.pickOrderTypeEnu.SORT.toString(),
                                                    currentBranch,
                                                    currentOrder,
                                                    cWarehouseorder.actionTypes.PLACE.toString(),
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
                        Intent intent = new Intent(cAppExtension.context, SortorderLinesActivity.class);
                        startActivity(intent);
                    }
                    else {

                    }
                }
            });
        }
    }

    private void mGoShip() {
        boolean forceRefresh = true;
        if (mLockWarehouseOrder(currentPickOrderEntity, true, 40)) {
            pickorderLinePackAndShipViewModel.getPickorderLinePackAndShips(forceRefresh, currentBranch, currentOrder).observe(this, new Observer<List<cPickorderLinePackAndShipEntity>>() {
                @Override
                public void onChanged(@Nullable List<cPickorderLinePackAndShipEntity> pickorderLinePackAndShipEntities) {
                    if (pickorderLinePackAndShipEntities != null && pickorderLinePackAndShipEntities.size() != 0) {
                        cSharedPreferences.setSharedPreferenceString(cPublicDefinitions.PREFERENCE_CURRENT_ORDER, currentOrder);
                        Intent intent = new Intent(cAppExtension.context, ShiporderLinesActivity.class);
                        startActivity(intent);
                    }
                }
            });
        }
    }

    private Boolean mLockWarehouseOrder(cPickorderEntity pickorderEntity, Boolean ignoreBusy, Integer workflowStep) {

        if (warehouseorderViewModel.getLockWarehouseorderBln(currentUser, "", cWarehouseorder.ordertypes.PICKEN.toString(), currentBranch, pickorderEntity.getOrdernumber(), cDeviceInfo.getSerialnumber(), "1", workflowStep, ignoreBusy)) {
            //we've achieved lock!
            return true;
        }
        else {
            return false;
        }
    }



}
