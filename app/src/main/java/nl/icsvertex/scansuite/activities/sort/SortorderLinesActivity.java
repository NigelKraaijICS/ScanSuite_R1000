package nl.icsvertex.scansuite.activities.sort;

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
import SSU_WHS.Comments.cCommentViewModel;
import SSU_WHS.PickorderBarcodes.cPickorderBarcodeEntity;
import SSU_WHS.PickorderBarcodes.cPickorderBarcodeViewModel;
import SSU_WHS.PickorderLines.cPickorderLineEntity;
import SSU_WHS.PickorderLines.cPickorderLineViewModel;
import SSU_WHS.Pickorders.cPickorderViewModel;
import SSU_WHS.Settings.cSettingsViewModel;
import SSU_WHS.Warehouseorder.cWarehouseorder;
import SSU_WHS.Warehouseorder.cWarehouseorderViewModel;
import SSU_WHS.Webservice.cWebservice;
import SSU_WHS.Workplaces.cWorkplaceEntity;
import SSU_WHS.cPublicDefinitions;
import ICS.Utils.Scanning.cBarcodeScanDefinitions;
import ICS.Utils.cDeviceInfo;
import ICS.Utils.cSharedPreferences;
import ICS.Utils.cText;
import ICS.Utils.cUserInterface;
import nl.icsvertex.scansuite.fragments.dialogs.CommentFragment;
import nl.icsvertex.scansuite.fragments.dialogs.OrderDoneFragment;
import nl.icsvertex.scansuite.fragments.dialogs.WorkplaceFragment;
import nl.icsvertex.scansuite.fragments.sort.SortorderLinesSortedFragment;
import nl.icsvertex.scansuite.fragments.sort.SortorderLinesToSortFragment;
import nl.icsvertex.scansuite.R;
import nl.icsvertex.scansuite.SortorderLinesPagerAdapter;

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

public class SortorderLinesActivity extends AppCompatActivity {
    public static final String VIEW_CHOSEN_ORDER = "detail:header:text";
    private Context thisContext;
    private Activity thisActivity;

    cWorkplaceEntity chosenWorkplace;

    static final String HUGEERROR_TAG = "HUGEERROR_TAG";
    static final String COMMENTFRAGMENT_TAG = "COMMENTFRAGMENT_TAG";
    static final String WORKPLACEFRAGMENT_TAG = "WORKPLACEFRAGMENT_TAG";
    static final String ACTIVITYNAME = "MainDefaultActivity";
    android.support.v4.app.FragmentManager fragmentManager;

    //region controls


    TextView textViewChosenOrder;
    TextView quantitySortordersText;
    ConstraintLayout container;
    TabLayout sortorderLinesTabLayout;
    ViewPager sortorderLinesViewPager;
    SortorderLinesPagerAdapter sortorderLinesPagerAdapter;
    cPickorderBarcodeViewModel sortorderBarcodeViewModel;
    cPickorderLineViewModel sortorderLineViewModel;
    cPickorderViewModel pickorderViewModel;
    cWarehouseorderViewModel warehouseorderViewModel;
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
    String currentLocation;
    String currentOrder;
    cPickorderLineEntity chosenSortorderLine;
    //endregions currents

    //region toolbar
    ImageView toolbarImage;
    TextView toolbarTitle;
    ImageView toolbarImageHelp;
    //endregion toolbar


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sortorderlines);
        thisContext = this;
        thisActivity = this;

        currentOrder = cSharedPreferences.getSharedPreferenceString(cPublicDefinitions.PREFERENCE_CURRENT_ORDER, "");
        currentUser = cSharedPreferences.getSharedPreferenceString(cPublicDefinitions.PREFERENCE_CURRENT_USER, "");
        currentBranch = cSharedPreferences.getSharedPreferenceString(cPublicDefinitions.PREFERENCE_CURRENT_BRANCH, "");

        sortorderLineViewModel = ViewModelProviders.of(this).get(cPickorderLineViewModel.class);
        sortorderBarcodeViewModel = ViewModelProviders.of(this).get(cPickorderBarcodeViewModel.class);
        pickorderViewModel = ViewModelProviders.of(this).get(cPickorderViewModel.class);
        commentViewModel = ViewModelProviders.of(this).get(cCommentViewModel.class);
        warehouseorderViewModel = ViewModelProviders.of(this).get(cWarehouseorderViewModel.class);
        sortorderBarcodeViewModel.getPickorderBarcodes(true, currentBranch, currentOrder).observe(this, new Observer<List<cPickorderBarcodeEntity>>() {
            @Override
            public void onChanged(@Nullable List<cPickorderBarcodeEntity> pickorderBarcodeEntities) {
                if (pickorderBarcodeEntities != null) {
                }
            }
        });
        settingsViewModel = ViewModelProviders.of(this).get(cSettingsViewModel.class);
        weberrorViewModel = ViewModelProviders.of(this).get(cWeberrorViewModel.class);

        fragmentManager = getSupportFragmentManager();

        //get empty picklines and delete them
        m_findEmptyPicklines();

        m_findViews();
        m_barcodeReceiver();
        m_setSettings();
        m_fieldsInitialize();
        m_setToolbar();
        m_setListeners();
        m_initProgram();
        m_checkAllDone();

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
    private void m_findEmptyPicklines() {
        List<cPickorderLineEntity> allSortordersLines = sortorderLineViewModel.getAll();
        for (cPickorderLineEntity sortorderLineEntity : allSortordersLines) {
            int quantitytaken = cText.stringToInteger(sortorderLineEntity.getQuantitytaken());
            if (quantitytaken == 0) {
                //sortorderLineViewModel.updateOrderLineLocalStatus(sortorderLineEntity.getRecordid(), cPickorderLine.LOCALSTATUS_DONE_NOTSENT);
                sortorderLineViewModel.delete(sortorderLineEntity);
            }
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
    private void m_findViews() {
        container = findViewById(R.id.container);
        toolbarImage = findViewById(R.id.toolbarImage);
        toolbarTitle = findViewById(R.id.toolbarTitle);
        toolbarImageHelp = findViewById(R.id.toolbarImageHelp);
        quantitySortordersText = findViewById(R.id.quantitySortordersText);
        sortorderLinesTabLayout = findViewById(R.id.sortorderLinesTabLayout);
        sortorderLinesViewPager = findViewById(R.id.sortorderLinesViewpager);
        //currentLocationView = findViewById(R.id.currentLocationView);
        //textViewSelectedBin = findViewById(R.id.textViewSelectedBin);
        textViewChosenOrder = findViewById(R.id.textViewChosenOrder);
        chosenOrderContainer = findViewById(R.id.chosenOrderContainer);
        imageButtonComments = findViewById(R.id.imageButtonComments);
//        imageViewIdea = findViewById(R.id.imageViewIdea);
//        textViewAction = findViewById(R.id.textViewAction);

    }
    private void m_setSettings() {
//        String pickBinManual = settingsViewModel.getSetting(cSettingsEnums.p_Settings14Enu.PICK_BIN_MANUAL.toString());
//        pickBinManualBln = cText.stringToBoolean(pickBinManual, false);

    }
    private void m_fieldsInitialize() {
        //m_setCounters();
        int numberNotSorted = sortorderLineViewModel.getNumberNotHandledForCounter();
        int numberTotalNotSorted = sortorderLineViewModel.getNumberTotalNotHandledForCounter();

        quantitySortordersText.setText(numberNotSorted + "/" + numberTotalNotSorted);

        textViewChosenOrder.setText(currentOrder);
        sortorderLinesTabLayout.addTab(sortorderLinesTabLayout.newTab().setText(R.string.tab_sortorderline_tosort));
        sortorderLinesTabLayout.addTab(sortorderLinesTabLayout.newTab().setText(R.string.tab_sortorderline_sorted));
        sortorderLinesTabLayout.addTab(sortorderLinesTabLayout.newTab().setText(R.string.tab_pickorderline_total));

        sortorderLinesPagerAdapter = new SortorderLinesPagerAdapter(getSupportFragmentManager(), sortorderLinesTabLayout.getTabCount());
        sortorderLinesViewPager.setAdapter(sortorderLinesPagerAdapter);
        sortorderLinesViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(sortorderLinesTabLayout));
        sortorderLinesTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                sortorderLinesViewPager.setCurrentItem(tab.getPosition());
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
        int numberNotSorted = sortorderLineViewModel.getNumberNotHandledForCounter();
        int numberTotalNotSorted = sortorderLineViewModel.getNumberTotalNotHandledForCounter();

        int numberSorted = sortorderLineViewModel.getNumberHandledForCounter();
        int numberTotalSorted = sortorderLineViewModel.getNumberTotalHandledForCounter();

        int numberTotal = sortorderLineViewModel.getNumberTotalForCounter();
        int numberTotalTotal = sortorderLineViewModel.getNumberTotalTotalForCounter();

        switch(tabSelected) {
            case 0:
                quantitySortordersText.setText(numberNotSorted + "/" + numberTotalNotSorted);
                //currentLocationView.setVisibility(View.VISIBLE);
                break;
            case 1:
                quantitySortordersText.setText(numberSorted + "/" + numberTotalSorted);
                //currentLocationView.setVisibility(View.INVISIBLE);
                break;
            case 2:
                quantitySortordersText.setText(numberTotal + "/" + numberTotalTotal);
                //currentLocationView.setVisibility(View.INVISIBLE);
                break;
            default:
                //currentLocationView.setVisibility(View.VISIBLE);
        }
    }
    private void m_setCounters() {
        Double totalArticlesDbl = sortorderLineViewModel.getTotalArticles();
        Integer totalArticlesInt = totalArticlesDbl.intValue();
        Double handledArticlesDbl = sortorderLineViewModel.getHandledArticles();
        Integer handledArticlesInt = handledArticlesDbl.intValue();
        quantitySortordersText.setText(handledArticlesInt + "/" + totalArticlesInt);
    }
    private void m_setToolbar() {
        toolbarImage.setImageResource(R.drawable.ic_menu_pick);
        toolbarTitle.setText(R.string.screentitle_sortorderlines);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
    private void m_setListeners() {
        mSetChosePackingTableListener();
        mSetWeberrorOberver();
    }

    private void m_initProgram() {

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

    private void mSetChosePackingTableListener() {

    }
    public void setChosenSortorderLineToReset(cPickorderLineEntity pickorderLineEntity) {
        for (Fragment fragment: fragmentManager.getFragments()) {
            if (fragment instanceof SortorderLinesSortedFragment) {
                ((SortorderLinesSortedFragment) fragment).setChosenSortorderLine(pickorderLineEntity);
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

    public cPickorderLineEntity getChosenSortorderLine() {
        return chosenSortorderLine;
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
        //did we scan a article?
        cPickorderBarcodeEntity sortorderBarcodeEntity = sortorderBarcodeViewModel.getPickOrderBarcodeByBarcode(barcode);
        if (sortorderBarcodeEntity != null) {
            String itemno = sortorderBarcodeEntity.getItemno();
            String variantcode = sortorderBarcodeEntity.getVariantcode();
            List<cPickorderLineEntity> sortorderLineEntities = sortorderLineViewModel.getNotHandledPickorderLinesByItemNoandVariantCode(itemno, variantcode);
            if (sortorderLineEntities != null) {
                if (sortorderLineEntities.size() == 0) {
                    doUnknownScan(getString(R.string.error_no_more_articles_to_sort), barcode);
                }
                else {
                    //start sorting with scanned barcode
                    Intent intent = new Intent(thisContext, SortorderSortActivity.class);

                    intent.putExtra(cPublicDefinitions.SORTING_SCANNEDBARCODE, sortorderBarcodeEntity.getBarcode());
                    startActivity(intent);
                }
            }
            else {
                doUnknownScan(getString(R.string.error_unknown_barcode), barcode);
            }
        }
        else {
            doUnknownScan(getString(R.string.error_unknown_barcode), barcode);
        }
   }
    public void setChosenSortorderLine(cPickorderLineEntity sortorderLineEntity) {
        for (Fragment fragment: fragmentManager.getFragments()) {
            if (fragment instanceof SortorderLinesToSortFragment) {
                ((SortorderLinesToSortFragment) fragment).setChosenSortorderLine(sortorderLineEntity);
            }
        }
        chosenSortorderLine = sortorderLineEntity;
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
    private void m_showComments() {
        final android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        final CommentFragment commentFragment = new CommentFragment();
        commentFragment.show(fragmentManager, COMMENTFRAGMENT_TAG);
        cUserInterface.playSound(R.raw.message,0);
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
            Intent intent = new Intent(thisContext, SortorderSelectActivity.class);
            startActivity(intent);
        }
        else {
            //todo: what do?
        }
    }
    private Boolean mUnLockWarehouseOrder() {
        if (warehouseorderViewModel.getLockReleaseWarehouseorderBln(currentUser, "", cWarehouseorder.ordertypes.PICKEN.toString(), currentBranch, currentOrder, cDeviceInfo.getSerialnumber(), cWarehouseorder.stepCodes.Pick_Sorting.toString(), 20)) {
            //we've achieved unlock!
            return true;
        }
        else {
            return false;
        }
    }

    private void m_checkAllDone() {
        if (m_allDone()) {
            m_showWorkplacePicker();
        }
    }

    private Boolean m_allDone() {
        Boolean resultBln = false;
        List<cPickorderLineEntity> pickorderLineEntities = sortorderLineViewModel.getNotHandledPickorderLineEntitiesLin();
        if (pickorderLineEntities.size() == 0) {
            resultBln = true;
        }
        return resultBln;
    }
    private void m_showWorkplacePicker() {
        final android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        final WorkplaceFragment workplaceFragment = new WorkplaceFragment();
        workplaceFragment.show(fragmentManager, WORKPLACEFRAGMENT_TAG);
    }
    public void setChosenWorkplace(cWorkplaceEntity workplaceEntity) {
        chosenWorkplace = workplaceEntity;
        android.support.v4.app.Fragment l_FragmentFrg = getSupportFragmentManager().findFragmentByTag(WORKPLACEFRAGMENT_TAG);
        if (l_FragmentFrg != null) {
            android.support.v4.app.DialogFragment l_DialogFragmentDfr = (android.support.v4.app.DialogFragment) l_FragmentFrg;
            l_DialogFragmentDfr.dismiss();
        }
        m_showAllDone();
    }
    public void closeCurrentOrder() {
        Boolean orderClosed = pickorderViewModel.pickorderStepHandled(currentUser, "", currentBranch, currentOrder, cDeviceInfo.getSerialnumber(), chosenWorkplace.getWorkplace(), "1",20, "" );
        if (orderClosed) {
            cUserInterface.showToastMessage("closed", null);
        }
        else {
            cUserInterface.showToastMessage( "not closed", null);
        }
        Intent intent = new Intent(thisContext, SortorderSelectActivity.class);
        startActivity(intent);
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
        bundle.putBoolean(cPublicDefinitions.ORDERDONE_SHOWCURRENTLOCATION, false);
        //bundle.putBoolean(cPublicDefinitions.ORDERDONE_SHOW_)
        orderDoneFragment.setArguments(bundle);
        orderDoneFragment.setCancelable(true);
        orderDoneFragment.show(fragmentManager, cPublicDefinitions.ORDERDONE_TAG);
    }
}
