package nl.icsvertex.scansuite.activities.pick;

import android.app.AlertDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


import ICS.Interfaces.iICSDefaultActivity;
import ICS.Utils.cText;
import ICS.Weberror.cWeberrorViewModel;
import SSU_WHS.Basics.ArticleImages.cArticleImageEntity;
import SSU_WHS.Basics.ArticleImages.cArticleImageViewModel;
import SSU_WHS.Basics.BarcodeLayouts.cBarcodeLayout;
import SSU_WHS.Basics.BarcodeLayouts.cBarcodeLayoutEntity;
import SSU_WHS.Basics.BarcodeLayouts.cBarcodeLayoutViewModel;
import ICS.Complex_types.c_BarcodeHandledUwbh;
import SSU_WHS.Picken.PickorderBarcodes.cPickorderBarcodeEntity;
import SSU_WHS.Picken.PickorderBarcodes.cPickorderBarcodeViewModel;
import SSU_WHS.Picken.PickorderLineBarcodes.cPickorderLineBarcodeEntity;
import SSU_WHS.Picken.PickorderLineBarcodes.cPickorderLineBarcodeViewModel;
import SSU_WHS.Picken.PickorderLines.cPickorderLine;
import SSU_WHS.Picken.PickorderLines.cPickorderLineEntity;
import SSU_WHS.Picken.PickorderLines.cPickorderLineViewModel;
import SSU_WHS.Picken.Pickorders.cPickorder;
import SSU_WHS.Picken.Pickorders.cPickorderEntity;
import SSU_WHS.Picken.Pickorders.cPickorderViewModel;
import SSU_WHS.Picken.SalesOrderPackingTable.cSalesOrderPackingTableEntity;
import SSU_WHS.Picken.SalesOrderPackingTable.cSalesOrderPackingTableViewModel;
import SSU_WHS.Basics.Settings.cSettingsEnums;
import SSU_WHS.Basics.Settings.cSettingsViewModel;
import SSU_WHS.Picken.Warehouseorder.cWarehouseorderViewModel;
import SSU_WHS.Webservice.cContainer;
import SSU_WHS.Basics.Workplaces.cWorkplaceEntity;

import SSU_WHS.General.cAppExtension;
import SSU_WHS.General.cPublicDefinitions;
import ICS.Utils.Scanning.cBarcodeScanDefinitions;
import ICS.Utils.cRegex;
import ICS.Utils.cSharedPreferences;
import ICS.Utils.cUserInterface;
import nl.icsvertex.scansuite.fragments.dialogs.AcceptRejectFragment;
import nl.icsvertex.scansuite.fragments.dialogs.ArticleFullViewFragment;
import nl.icsvertex.scansuite.fragments.dialogs.BarcodePickerFragment;
import nl.icsvertex.scansuite.fragments.dialogs.NumberpickerFragment;
import nl.icsvertex.scansuite.fragments.dialogs.WorkplaceFragment;
import nl.icsvertex.scansuite.R;

import static ICS.Utils.cDateAndTime.m_GetCurrentDateTimeForWebservice;

public class PickorderPickActivity extends AppCompatActivity implements iICSDefaultActivity {
    static final String NUMBERPICKERFRAGMENT_TAG = "NUMBERPICKERFRAGMENT_TAG";
    static final String WORKPLACEFRAGMENT_TAG = "WORKPLACEFRAGMENT_TAG";
    static final String BARCODEPICKERFRAGMENT_TAG = "BARCODEPICKERFRAGMENT_TAG";
    static final String ACCEPTREJECTFRAGMENT_TAG = "ACCEPTREJECTFRAGMENT_TAG";
    static final String ACTIVITYNAME = "PickorderPickActivity";

    Context thisContext = null;
    android.support.v4.app.FragmentManager fragmentManager;
    cPickorderLineViewModel pickorderLineViewModel;
    cPickorderLineEntity pickorderLineEntity;
    cSettingsViewModel settingsViewModel;
    cArticleImageViewModel articleImageViewModel;
    cWarehouseorderViewModel warehouseorderViewModel;
    cPickorderBarcodeViewModel pickorderBarcodeViewModel;
    cPickorderViewModel pickorderViewModel;
    cPickorderEntity pickorderEntity;
    cBarcodeLayoutViewModel barcodeLayoutViewModel;
    cSalesOrderPackingTableViewModel salesOrderPackingTableViewModel;
    cWeberrorViewModel weberrorViewModel;
    cPickorderLineBarcodeViewModel pickorderLineBarcodeViewModel;

    List<cPickorderBarcodeEntity> barcodesObl;

    List<cBarcodeLayoutEntity> salesOrderBarcodeLayouts;
    List<cBarcodeLayoutEntity> pickCartBoxBarcodeLayouts;

    Boolean articleScanned;

    //region Settings
    String workflow;
    Boolean isPVWorkflow;
    Boolean pickPickPvVkoEachPieceBln;
    Boolean pickSelecterenBarcodeBln;
    Boolean pickWithPicturesPrefetchBln;
    Boolean pickWithPicturesBln;
    Boolean pickWithPicturesAutoOpenBln;
    Boolean pickAutoAcceptBln;
    Boolean pickShippingSalesBln;
    String genericItemExtraField1Str;
    String genericItemExtraField2Str;
    String genericItemExtraField3Str;
    String genericItemExtraField4Str;
    String genericItemExtraField5Str;
    String genericItemExtraField6Str;
    String genericItemExtraField7Str;
    String genericItemExtraField8Str;
    //endregion

    int barcodeQuantity;
    cPickorderBarcodeEntity currentBarcode;

    //things we got from pickorderlines
    Integer chosenItemno;
    String chosenLocation;
    String scannedBarcode;

    String currentUser;
    String currentBranch;
    String currentOrder;

    int pickedQuantity;
    Double requiredQuantity;

    int pickCounterMinusHelper;
    int pickCounterPlusHelper;

    IntentFilter barcodeIntentFilter;
    Handler minusHandler;
    Handler plusHandler;
    ConstraintLayout pickorderPickContainer;
    ImageView toolbarImage;
    TextView toolbarTitle;
    TextView toolbarSubtext;
    ImageView toolbarImageHelp;
    CardView articleContainer;
    TextView articleDescriptionText;
    TextView articleDescription2Text;
    TextView articleItemText;
    TextView articleBarcodeText;
    TextView articleVendorItemText;
    TextView binText;
    TextView containerText;
    TextView quantityText;
    TextView quantityRequiredText;
    ImageView articleThumbImageView;
    ImageView imageButtonBarcode;
    TextView sourcenoText;
    CardView sourcenoContainer;
    TextView genericItemExtraField1Text;
    TextView genericItemExtraField2Text;
    TextView genericItemExtraField3Text;
    TextView genericItemExtraField4Text;
    TextView genericItemExtraField5Text;
    TextView genericItemExtraField6Text;
    TextView genericItemExtraField7Text;
    TextView genericItemExtraField8Text;
    AppCompatImageButton imageButtonMinus;
    AppCompatImageButton imageButtonPlus;
    AppCompatImageButton imageButtonDone;
    TextView textViewAction;
    Runnable minusAction = new Runnable() {
        @Override
        public void run() {
            imageButtonMinus.performClick();
            long millisecs;
            if (pickCounterMinusHelper < 10) {
                millisecs = 200;
            } else if (pickCounterMinusHelper < 20) {
                millisecs = 150;
            } else if (pickCounterMinusHelper < 30) {
                millisecs = 100;
            } else if (pickCounterMinusHelper < 40) {
                millisecs = 50;
            } else {
                millisecs = 50;
            }
            m_doMinus(this, millisecs);
        }
    };
    Runnable plusAction = new Runnable() {
        @Override
        public void run() {
            imageButtonPlus.performClick();
            long millisecs;
            if (pickCounterPlusHelper < 10) {
                millisecs = 200;
            } else if (pickCounterPlusHelper < 20) {
                millisecs = 150;
            } else if (pickCounterPlusHelper < 30) {
                millisecs = 100;
            } else if (pickCounterPlusHelper < 40) {
                millisecs = 50;
            } else {
                millisecs = 50;
            }
            m_doPlus(this, millisecs);
        }
    };
    private BroadcastReceiver barcodeReceiver;

    private BroadcastReceiver m_numberReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int l_numberChosenInt = 0;
            Bundle extras = intent.getExtras();

            if (extras != null) {
                l_numberChosenInt = extras.getInt(cPublicDefinitions.NUMBERINTENT_EXTRANUMBER);
            }
            m_HandleQuantityChosen(l_numberChosenInt);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickorder_pick);

        thisContext = this;
        barcodeQuantity = 1;

        chosenItemno = getIntent().getIntExtra(cPublicDefinitions.PICKING_CHOSENITEMNO, 0);
        chosenLocation = getIntent().getStringExtra(cPublicDefinitions.PICKING_CHOSENLOCATION);
        scannedBarcode = getIntent().getStringExtra(cPublicDefinitions.PICKING_SCANNEDBARCODE);
        currentOrder = cSharedPreferences.getSharedPreferenceString(cPublicDefinitions.PREFERENCE_CURRENT_ORDER, "");

        pickorderLineViewModel = ViewModelProviders.of(this).get(cPickorderLineViewModel.class);
        pickorderBarcodeViewModel = ViewModelProviders.of(this).get(cPickorderBarcodeViewModel.class);
        articleImageViewModel = ViewModelProviders.of(this).get(cArticleImageViewModel.class);
        settingsViewModel = ViewModelProviders.of(this).get(cSettingsViewModel.class);
        pickorderViewModel = ViewModelProviders.of(this).get(cPickorderViewModel.class);
        barcodeLayoutViewModel = ViewModelProviders.of(this).get(cBarcodeLayoutViewModel.class);
        warehouseorderViewModel = ViewModelProviders.of(this).get(cWarehouseorderViewModel.class);
        salesOrderPackingTableViewModel = ViewModelProviders.of(this).get(cSalesOrderPackingTableViewModel.class);
        pickorderLineBarcodeViewModel = ViewModelProviders.of(this).get(cPickorderLineBarcodeViewModel.class);
        weberrorViewModel = ViewModelProviders.of(this).get(cWeberrorViewModel.class);

        salesOrderBarcodeLayouts = barcodeLayoutViewModel.getBarcodeLayoutsOfType(cBarcodeLayout.barcodeLayoutEnu.SALESORDER.toString());
        pickCartBoxBarcodeLayouts = barcodeLayoutViewModel.getBarcodeLayoutsOfType(cBarcodeLayout.barcodeLayoutEnu.PICKCARTBOX.toString());

        //delete all scanned barcodes
        pickorderLineBarcodeViewModel.deleteAll();

        fragmentManager = getSupportFragmentManager();


        //so what did we get?
        if (chosenItemno != null) {
            pickorderLineEntity = pickorderLineViewModel.getPickLineByRecordid(chosenItemno);
        }
        if (chosenLocation != null) {
            pickorderLineEntity = pickorderLineViewModel.getPickorderLineNotHandledByBin(chosenLocation);
        }
        if (scannedBarcode != null) {
            cPickorderBarcodeEntity pickorderBarcodeEntity = pickorderBarcodeViewModel.getPickOrderBarcodeByBarcode(scannedBarcode);
            String itemno = pickorderBarcodeEntity.getItemno();
            pickorderLineEntity = pickorderLineViewModel.getPickorderLineByItemNo(itemno);
            barcodeQuantity = cText.stringToInteger(pickorderBarcodeEntity.getQuantityperunitofmeasure());
            currentBarcode = pickorderBarcodeEntity;
        }
        //update orderline status
        pickorderLineViewModel.updateOrderLineLocalStatus(pickorderLineEntity.getRecordid(), cPickorderLine.LOCALSTATUS_BUSY);

        currentUser = cSharedPreferences.getSharedPreferenceString(cPublicDefinitions.PREFERENCE_CURRENT_USER, "");
        currentBranch = cSharedPreferences.getSharedPreferenceString(cPublicDefinitions.PREFERENCE_CURRENT_BRANCH, "");

        pickorderEntity = pickorderViewModel.getPickorderByOrderNumber(currentOrder);

        mActivityInitialize();

        mBarcodeReceiver();

        mSendBarcodes();

        if (scannedBarcode != null) {
            mHandleScan(scannedBarcode);
        }
    }

    @Override
    protected void onDestroy() {
        try {
            unregisterReceiver(barcodeReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
        LocalBroadcastManager.getInstance(thisContext).unregisterReceiver(m_numberReceiver);
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        try {
            unregisterReceiver(barcodeReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
        LocalBroadcastManager.getInstance(thisContext).unregisterReceiver(m_numberReceiver);
        super.onPause();
    }

    @Override
    protected void onResume() {
        LocalBroadcastManager.getInstance(thisContext).registerReceiver(m_numberReceiver, new IntentFilter(cPublicDefinitions.NUMBERINTENT_NUMBER));
        registerReceiver(barcodeReceiver, barcodeIntentFilter);

        super.onResume();
        cPublicDefinitions.CURRENT_ACTIVITY = ACTIVITYNAME;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:




                if (pickorderLineEntity != null) {

                    Integer recordid = pickorderLineEntity.getRecordid();
                    cPickorderLineEntity newEntity = pickorderLineViewModel.getPickLineByRecordid(recordid);
                    if (newEntity.getLocalstatus() == cPickorderLine.LOCALSTATUS_BUSY) {
                        m_checkAndCloseOpenDialogs();

                        final AcceptRejectFragment acceptRejectFragment = new AcceptRejectFragment();
                        acceptRejectFragment.setCancelable(true);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // show my popup
                                acceptRejectFragment.show(fragmentManager, ACCEPTREJECTFRAGMENT_TAG);
                            }
                        });
                    }
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
        registerReceiver(barcodeReceiver, barcodeIntentFilter);
    }


    @Override
    public void mActivityInitialize() {
        mSetAppExtensions();

        mFindViews();

        mSetViewModels();

        mSetSettings();

        mSetToolbar(getResources().getString(R.string.screentitle_pickorderpick));

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
        pickorderPickContainer = findViewById(R.id.pickorderPickContainer);
        toolbarImage = findViewById(R.id.toolbarImage);
        toolbarTitle = findViewById(R.id.toolbarTitle);
        toolbarSubtext = findViewById(R.id.toolbarSubtext);
        toolbarImageHelp = findViewById(R.id.toolbarImageHelp);

        articleContainer = findViewById(R.id.addressContainer);
        articleDescriptionText = findViewById(R.id.articleDescriptionText);
        articleDescription2Text = findViewById(R.id.articleDescription2Text);
        articleItemText = findViewById(R.id.articleItemText);
        articleBarcodeText = findViewById(R.id.articleBarcodeText);
        articleVendorItemText = findViewById(R.id.articleVendorItemText);
        binText = findViewById(R.id.binText);
        sourcenoText = findViewById(R.id.sourcenoText);
        sourcenoContainer = findViewById(R.id.sourcenoContainer);

        genericItemExtraField1Text = findViewById(R.id.genericItemExtraField1Text);
        genericItemExtraField2Text = findViewById(R.id.genericItemExtraField2Text);
        genericItemExtraField3Text = findViewById(R.id.genericItemExtraField3Text);
        genericItemExtraField4Text = findViewById(R.id.genericItemExtraField4Text);
        genericItemExtraField5Text = findViewById(R.id.genericItemExtraField5Text);
        genericItemExtraField6Text = findViewById(R.id.genericItemExtraField6Text);
        genericItemExtraField7Text = findViewById(R.id.genericItemExtraField7Text);
        genericItemExtraField8Text = findViewById(R.id.genericItemExtraField8Text);

        containerText = findViewById(R.id.containerText);
        quantityText = findViewById(R.id.quantityText);
        quantityRequiredText = findViewById(R.id.quantityRequiredText);
        articleThumbImageView = findViewById(R.id.articleThumbImageView);
        imageButtonBarcode = findViewById(R.id.imageButtonBarcode);
        imageButtonMinus = findViewById(R.id.imageButtonMinus);
        imageButtonPlus = findViewById(R.id.imageButtonPlus);
        imageButtonDone = findViewById(R.id.imageButtonDone);

        textViewAction = findViewById(R.id.textViewAction);
    }

    @Override
    public void mSetViewModels() {

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
    public void mSetSettings() {
        workflow = pickorderEntity.getOrdertype();
        if (workflow.equalsIgnoreCase(cPickorder.eOrderTypes.PV.toString())) {
            isPVWorkflow = true;
        } else {
            isPVWorkflow = false;
        }
        String pickPickPvVkoEachPiece = pickorderEntity.getPickPickPVVKOeachpiece();
        pickPickPvVkoEachPieceBln = cText.stringToBoolean(pickPickPvVkoEachPiece, false);
        String pickSelecterenBarcode = settingsViewModel.getSetting(cSettingsEnums.p_Settings14Enu.PICK_SELECTEREN_BARCODE.toString());
        pickSelecterenBarcodeBln = cText.stringToBoolean(pickSelecterenBarcode, false);
        String pickWithPictures = settingsViewModel.getSetting(cSettingsEnums.p_Settings14Enu.PICK_WITH_PICTURE.toString());
        pickWithPicturesBln = cText.stringToBoolean(pickWithPictures, false);
        String pickWithPicturesAutoOpen = settingsViewModel.getSetting(cSettingsEnums.p_Settings14Enu.PICK_WITH_PICTURE_AUTO_OPEN.toString());
        pickWithPicturesAutoOpenBln = cText.stringToBoolean(pickWithPicturesAutoOpen, false);
        String pickWithPicturesPrefetch = settingsViewModel.getSetting(cSettingsEnums.p_Settings14Enu.PICK_WITH_PICTURE_PREFETCH.toString());
        pickWithPicturesPrefetchBln = cText.stringToBoolean(pickWithPicturesPrefetch, false);
        String pickAutoAccept = settingsViewModel.getSetting(cSettingsEnums.p_Settings14Enu.PICK_AUTO_ACCEPT.toString());
        pickAutoAcceptBln = cText.stringToBoolean(pickAutoAccept, false);
        String pickShippingSales = settingsViewModel.getSetting(cSettingsEnums.p_Settings14Enu.PICK_SHIPPING_SALES.toString());
        pickShippingSalesBln = cText.stringToBoolean(pickShippingSales, false);
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
    public void mFieldsInitialize() {
        mSetExtraFields();

        //delete all scanned barcodes
        pickorderLineBarcodeViewModel.deleteAll();

        imageButtonDone.setImageResource(R.drawable.ic_check_black_24dp);
        barcodeQuantity = 1;
        pickedQuantity = 0;

        pickCounterPlusHelper = 0;
        pickCounterMinusHelper = 0;
        toolbarSubtext.setText(currentOrder);
        containerText.setVisibility(View.GONE);
        articleDescriptionText.setText(pickorderLineEntity.getDescription());
        articleDescription2Text.setText(pickorderLineEntity.getDescription2());
        String iteminfo = pickorderLineEntity.getItemno() + " " + pickorderLineEntity.getVariantcode();
        articleItemText.setText(iteminfo);
        barcodesObl = pickorderBarcodeViewModel.getPickorderBarcodesForItemVariantCode(pickorderLineEntity.getItemno(), pickorderLineEntity.getVariantcode());
        if (barcodesObl != null && barcodesObl.size() > 0) {
            cPickorderBarcodeEntity barcodeEntity = barcodesObl.get(0);
            String barcodeText = barcodeEntity.getBarcode() + " (" + cText.stringToInteger(barcodeEntity.getQuantityperunitofmeasure()) + ")";
            articleBarcodeText.setText(barcodeText);
            barcodeQuantity = cText.stringToInteger(barcodesObl.get(0).getQuantityperunitofmeasure());
            currentBarcode = barcodeEntity;
        }
        String l_vendorInfoStr = pickorderLineEntity.getVendoritemno() + ' ' + pickorderLineEntity.getVendoritemdescription();
        articleVendorItemText.setText(l_vendorInfoStr);
        binText.setText(pickorderLineEntity.getBincode());
        sourcenoText.setText(pickorderLineEntity.getSourceno());
        if (pickorderLineEntity.getSourceno().trim().isEmpty()) {
            sourcenoContainer.setVisibility(View.INVISIBLE);
        }

        containerText.setText(pickorderLineEntity.getContainer());
        containerText.setText("");
        quantityText.setText("0");
        requiredQuantity = pickorderLineEntity.getQuantity();
        quantityRequiredText.setText(cText.doubleToString(requiredQuantity));

        //Manual barcode input?
        if (!pickSelecterenBarcodeBln) {
            imageButtonBarcode.setVisibility(View.INVISIBLE);
            m_setManual(false);
        } else {
            imageButtonBarcode.setVisibility(View.VISIBLE);
            m_setManual(false);
        }

        //Are we doing pictures?
        if (pickWithPicturesBln) {
            articleThumbImageView.setVisibility(View.VISIBLE);
            //pictures!
            articleImageViewModel.getArticleImages(!pickWithPicturesPrefetchBln, currentUser, "", pickorderLineEntity.getItemno(), pickorderLineEntity.getVariantcode(), true).observe(cAppExtension.fragmentActivity, new Observer<List<cArticleImageEntity>>() {
                @Override
                public void onChanged(List<cArticleImageEntity> articleImageEntities) {
                    if (articleImageEntities != null) {
                        if (articleImageEntities.size() > 0) {
                            byte[] decodedString = Base64.decode(articleImageEntities.get(0).getImage(), Base64.DEFAULT);
                            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                            articleThumbImageView.setImageBitmap(decodedByte);
                        } else {
                            articleThumbImageView.setImageDrawable(ContextCompat.getDrawable(cAppExtension.context, R.drawable.ic_no_image_lightgrey_24dp));
                        }
                    } else {
                        articleThumbImageView.setImageDrawable(ContextCompat.getDrawable(cAppExtension.context, R.drawable.ic_no_image_lightgrey_24dp));
                    }
                    if (pickWithPicturesAutoOpenBln) {
                        m_showFullArticleFragment();
                    }
                }
            });
        } else {
            //no pictures
            articleThumbImageView.setVisibility(View.INVISIBLE);
        }

        articleScanned = false;
        if (isPVWorkflow) {
            textViewAction.setText(getString(R.string.scan_article));
            String processingSequence = pickorderLineEntity.getProcessingsequence().trim();
            if (!processingSequence.isEmpty()) {
                cSalesOrderPackingTableEntity newSalesOrderPackingTableEntity = new cSalesOrderPackingTableEntity();
                newSalesOrderPackingTableEntity.setSalesorder(pickorderLineEntity.getSourceno());
                newSalesOrderPackingTableEntity.setPackingtable(processingSequence);
                salesOrderPackingTableViewModel.insert(newSalesOrderPackingTableEntity);
                mUpdatePickorderLineProcessingSequence(processingSequence);
                sourcenoText.setText(processingSequence);
            }
            else {
                //check if this sourceno already has a location
                cSalesOrderPackingTableEntity salesOrderPackingTableEntity = salesOrderPackingTableViewModel.getPackingTableForSalesorder(pickorderLineEntity.getSourceno());
                if (salesOrderPackingTableEntity != null) {
                    sourcenoText.setText(salesOrderPackingTableEntity.getPackingtable());
                }
            }
        }
    }

    private void mSetExtraFields() {
        if (!genericItemExtraField1Str.trim().isEmpty()) {
            genericItemExtraField1Text.setVisibility(View.VISIBLE);
            genericItemExtraField1Text.setText(pickorderLineEntity.getExtrafield1());
        }
        if (!genericItemExtraField2Str.trim().isEmpty()) {
            genericItemExtraField2Text.setVisibility(View.VISIBLE);
            genericItemExtraField2Text.setText(pickorderLineEntity.getExtrafield2());
        }
        if (!genericItemExtraField3Str.trim().isEmpty()) {
            genericItemExtraField3Text.setVisibility(View.VISIBLE);
            genericItemExtraField3Text.setText(pickorderLineEntity.getExtrafield3());
        }
        if (!genericItemExtraField4Str.trim().isEmpty()) {
            genericItemExtraField4Text.setVisibility(View.VISIBLE);
            genericItemExtraField4Text.setText(pickorderLineEntity.getExtrafield4());
        }
        if (!genericItemExtraField5Str.trim().isEmpty()) {
            genericItemExtraField5Text.setVisibility(View.VISIBLE);
            genericItemExtraField5Text.setText(pickorderLineEntity.getExtrafield5());
        }
        if (!genericItemExtraField6Str.trim().isEmpty()) {
            genericItemExtraField6Text.setVisibility(View.VISIBLE);
            genericItemExtraField6Text.setText(pickorderLineEntity.getExtrafield6());
        }
        if (!genericItemExtraField7Str.trim().isEmpty()) {
            genericItemExtraField7Text.setVisibility(View.VISIBLE);
            genericItemExtraField7Text.setText(pickorderLineEntity.getExtrafield7());
        }
        if (!genericItemExtraField8Str.trim().isEmpty()) {
            genericItemExtraField8Text.setVisibility(View.VISIBLE);
            genericItemExtraField8Text.setText(pickorderLineEntity.getExtrafield8());
        }
    }

    @Override
    public void mSetListeners() {
        m_setArticleImageListener();
        m_setImageButtonBarcodeListener();
        if (pickSelecterenBarcodeBln) {
            m_setNumberListener();
        }
        m_SetPlusListener();
        m_setMinusListener();
        m_setDoneListener();

//        mSetWeberrorOberver();
    }

    @Override
    public void mInitScreen() {

    }


    //TODO: put Errorhandling back, but better
//    private void mSetWeberrorOberver() {
//        weberrorViewModel.getAllForActivityLive(ACTIVITYNAME).observe(thisContext, new Observer<List<cWeberrorEntity>>() {
//            @Override
//            public void onChanged(List<cWeberrorEntity> cWeberrorEntities) {
//                if (cWeberrorEntities != null && cWeberrorEntities.size() > 0) {
//
//                    boolean isSuccess = true;
//                    for (cWeberrorEntity weberrorEntity : cWeberrorEntities) {
//
//                        if (!weberrorEntity.getIssucess()) {
//                            isSuccess = false;
//                        }
//                    }
//                    if (!isSuccess) {
//                        cUserInterface.doWebserviceError(cWeberrorEntities, false, false );
//                    }
//                }
//                //all right, handled.
//                weberrorViewModel.deleteAll();
//            }
//        });
//    }

    private void m_setArticleImageListener() {
        articleThumbImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_showFullArticleFragment();

            }
        });
    }

    private void m_setImageButtonBarcodeListener() {
        imageButtonBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_setManual(true);
                if (barcodesObl != null && barcodesObl.size() > 0) {
                    if (barcodesObl.size() == 1) {
                        setChosenBarcode(barcodesObl.get(0));
                        return;
                    }
                    ArrayList<cPickorderBarcodeEntity> showBarcodes = new ArrayList<>(barcodesObl.size());
                    showBarcodes.addAll(barcodesObl);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(cPublicDefinitions.BARCODEFRAGMENT_LIST_TAG, showBarcodes);
                    final android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                    final BarcodePickerFragment barcodePickerFragment = new BarcodePickerFragment();
                    barcodePickerFragment.setArguments(bundle);
                    barcodePickerFragment.show(fragmentManager, BARCODEPICKERFRAGMENT_TAG);
                }

            }
        });
    }

    private void m_showWorkplacePicker() {
        final android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        final WorkplaceFragment workplaceFragment = new WorkplaceFragment();
        workplaceFragment.show(fragmentManager, WORKPLACEFRAGMENT_TAG);
    }

    private void m_setDoneListener() {
        imageButtonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPVWorkflow) {
                    m_HandlePVDone();
                    return;
                }
                if (pickedQuantity == requiredQuantity) {
                    m_pickingDone();
                } else {
                    m_askSureUnderpick();
                }
            }
        });
    }
    private void m_HandlePVDone() {
        if (articleScanned) {
            textViewAction.setText(R.string.message_scan_pickcart_or_salesorder);
            return;
        }
        if (!articleScanned) {
            if (pickedQuantity == requiredQuantity) {
                m_pickingPVDone();
            } else {
                m_askSureUnderpick();
            }
        }
    }
    private void m_SetPlusListener() {

        imageButtonPlus.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (plusHandler != null) return true;
                    plusHandler = new Handler();
                    plusHandler.postDelayed(plusAction, 750);
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (plusHandler == null) return true;
                    plusHandler.removeCallbacks(plusAction);
                    plusHandler = null;
                    pickCounterPlusHelper = 0;
                }
                return false;
            }
        });

        imageButtonPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_changeQuantity(true, barcodeQuantity);
            }
        });
    }

    private void m_setMinusListener() {

        imageButtonMinus.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (minusHandler != null) return true;
                    minusHandler = new Handler();
                    minusHandler.postDelayed(minusAction, 750);
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (minusHandler == null) return true;
                    minusHandler.removeCallbacks(minusAction);
                    minusHandler = null;
                    pickCounterMinusHelper = 0;
                }
                return false;
            }

        });

        imageButtonMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageButtonDone.setImageResource(R.drawable.ic_check_black_24dp);
                m_changeQuantity(false, barcodeQuantity);
            }
        });
    }

    private void m_setNumberListener() {
        quantityText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_numberClicked();
            }
        });
        quantityRequiredText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_numberClicked();
            }
        });
    }

    private void m_numberClicked() {
        if (currentBarcode != null) {
            if (cText.stringToInteger(currentBarcode.getQuantityperunitofmeasure()) > 1 ) {
                cUserInterface.doNope(quantityText, true, true);
                cUserInterface.showSnackbarMessage(pickorderPickContainer, getString(R.string.manual_input_only_barcodenumber_bigger1), null, false);
                return;
            }
            m_showNumberPicker();
        }
        else {
            cUserInterface.doNope(quantityText, false, false);
            cUserInterface.showSnackbarMessage(pickorderPickContainer, getString(R.string.choose_barcode_first), null, false);
        }

    }

    private void m_showNumberPicker() {
        m_checkAndCloseOpenDialogs();
        final NumberpickerFragment numberpickerFragment = new NumberpickerFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(cPublicDefinitions.NUMBERINTENT_CURRENTQUANTITY, pickedQuantity);
        bundle.putDouble(cPublicDefinitions.NUMBERINTENT_MAXQUANTITY, requiredQuantity);
        numberpickerFragment.setArguments(bundle);
        numberpickerFragment.show(fragmentManager, NUMBERPICKERFRAGMENT_TAG);
    }

    private void m_showFullArticleFragment() {
        //m_checkAndCloseOpenDialogs();
        final ArticleFullViewFragment articleFullViewFragment = new ArticleFullViewFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(cPublicDefinitions.ARTICLEFULL_RECORDID, pickorderLineEntity.getRecordid());
        articleFullViewFragment.setArguments(bundle);
        articleFullViewFragment.show(fragmentManager, cPublicDefinitions.ARTICLEFULL_TAG);
    }

    private void m_showBarcodePicker() {

    }

    private void mHandleScan(String barcodeStr) {

        m_checkAndCloseOpenDialogs();

        if (isPVWorkflow) {
            mHandlePVScan(barcodeStr);
            return;
        }

        if (!mFindBarcodeInOrderline(barcodeStr)) {
            doUnknownScan(getString(R.string.error_unknown_barcode), barcodeStr);
            return;
        }
    }

    private void mHandlePVScan(String barcodeStr) {
        //what did we scan?
        Boolean pickCartFound = false;
        Boolean salesOrderFound = false;

        //What did we scan?
        for (cBarcodeLayoutEntity layout : pickCartBoxBarcodeLayouts) {
            if (cRegex.p_checkRegexBln(layout.getLayoutValue(), barcodeStr)) {
                pickCartFound = true;
            }
        }
        for (cBarcodeLayoutEntity layout : salesOrderBarcodeLayouts) {
            if (cRegex.p_checkRegexBln(layout.getLayoutValue(), barcodeStr)) {
                salesOrderFound = true;
            }
        }

        String barcodeWithoutPrefix = cRegex.p_stripRegexPrefixStr(barcodeStr);

        if (!pickCartFound && !salesOrderFound) {

            //Handle article scan
            if (articleScanned && pickPickPvVkoEachPieceBln) {
                doUnknownScan(getString(R.string.message_scan_pickcart_or_salesorder), barcodeWithoutPrefix);
                return;
            }
            //article accepted!
            //known article?
            if (!mFindBarcodeInOrderline(barcodeWithoutPrefix)) {
                doUnknownScan(getString(R.string.error_unknown_barcode), barcodeStr);
                return;
            }
            return;
        }


        // we've scanned a pickCart or a salesOrder
        // Check if article is already scanned
        if (!articleScanned) {
            // we've scanned a pickCart or a salesOrder, but we need an article
            doUnknownScan(getString(R.string.message_scan_article_first), barcodeWithoutPrefix);
            return;
        }

        //check if scan == salesorder if regex is salesorder
        String salesOrder = pickorderLineEntity.getSourceno();
        if (salesOrderFound) {
            //sales order must be the same as order's salesorder
            if (!barcodeWithoutPrefix.equalsIgnoreCase(salesOrder)) {
                doUnknownScan(getString(R.string.message_wrong_sourceno), barcodeWithoutPrefix);
                return;
            }
        }

        //correct pickCart or salesOrder scan
        cSalesOrderPackingTableEntity salesOrderPackingTableEntity = salesOrderPackingTableViewModel.getPackingTableForSalesorder(salesOrder);
        cSalesOrderPackingTableEntity packingTableSalesOrderEntity = salesOrderPackingTableViewModel.getSalesorderForPackingTable(barcodeWithoutPrefix);

        if (packingTableSalesOrderEntity != null && !packingTableSalesOrderEntity.getSalesorder().equalsIgnoreCase(pickorderLineEntity.getSourceno())) {
            //packingtable already assigned to different sourceno
            doUnknownScan(getString(R.string.message_location_already_assigned), "");
            return;
        }
        //Check if record already is available in database for the sourceno
        if (salesOrderPackingTableEntity != null) {
            //Check if scanned value is equel to record in database for this sourceno
            if (!salesOrderPackingTableEntity.getPackingtable().equalsIgnoreCase(barcodeWithoutPrefix)) {
                doUnknownScan(getString(R.string.message_salesorder_assigned_different_location, salesOrderPackingTableEntity.getPackingtable()), "");
                return;
            }
            mUpdatePickorderLineProcessingSequence(barcodeWithoutPrefix);
            mUpdatePickorderLine();
            if (pickedQuantity == requiredQuantity) {
                mAllScannedPV();
            } else {
                textViewAction.setText(getString(R.string.scan_article));
                articleScanned = false;
            }
            //return;
        } else {
            //doesn't exist yet, insert
            cSalesOrderPackingTableEntity newSalesOrderPackingTableEntity = new cSalesOrderPackingTableEntity();
            newSalesOrderPackingTableEntity.setSalesorder(salesOrder);
            newSalesOrderPackingTableEntity.setPackingtable(barcodeWithoutPrefix);
            salesOrderPackingTableViewModel.insert(newSalesOrderPackingTableEntity);
            mUpdatePickorderLineProcessingSequence(barcodeWithoutPrefix);
            mUpdatePickorderLine();
            sourcenoText.setText(barcodeWithoutPrefix);
            textViewAction.setText(getString(R.string.scan_article));
            articleScanned = false;
        }
        if (pickedQuantity > 0 && !pickPickPvVkoEachPieceBln) {
            m_pickingPVDone();
        }

    }

    private Boolean mFindBarcodeInOrderline(String barcodeStr) {
        Boolean barcodeFoundBln = false;
        for (cPickorderBarcodeEntity pickorderBarcodeEntity : barcodesObl) {
            if (pickorderBarcodeEntity.getBarcode().equalsIgnoreCase(barcodeStr)) {
                barcodeFoundBln = true;
                currentBarcode = pickorderBarcodeEntity;
                m_setManual(true);
                String barcodeText = pickorderBarcodeEntity.getBarcode() + " (" + cText.stringToInteger(pickorderBarcodeEntity.getQuantityperunitofmeasure()) + ")";
                articleBarcodeText.setText(barcodeText);
                barcodeQuantity = cText.stringToInteger(pickorderBarcodeEntity.getQuantityperunitofmeasure());
                String quantity = pickorderBarcodeEntity.getQuantityperunitofmeasure();
                Integer quant = cText.stringToInteger(quantity);
                m_changeQuantity(true, quant);
                break;
            }
        }
        return barcodeFoundBln;
    }

    private void m_doMinus(Runnable runnable, long milliSecs) {
        minusHandler.postDelayed(runnable, milliSecs);
        pickCounterMinusHelper += 1;
    }

    private void m_doPlus(Runnable runnable, long milliSecs) {
        plusHandler.postDelayed(runnable, milliSecs);
        pickCounterPlusHelper += 1;
    }

    private void m_changeQuantity(Boolean isPositive, Integer amount) {

        if (isPositive) {
            if (pickedQuantity >= requiredQuantity) {
                quantityText.setText(quantityRequiredText.getText());
                cUserInterface.showSnackbarMessage(textViewAction , getString(R.string.number_cannot_be_higher), null, false);
                cUserInterface.doNope(quantityText, true, true);
                cUserInterface.doNope(quantityRequiredText, false, false);
            } else {
                int newQuantityInt = pickedQuantity + amount;
                if (newQuantityInt > requiredQuantity) {
                    //newQuantityInt = requiredQuantity.intValue();
                    cUserInterface.showSnackbarMessage(textViewAction , getString(R.string.number_cannot_be_higher), null, false);
                    cUserInterface.doNope(quantityText, true, true);
                    cUserInterface.doNope(quantityRequiredText, false, false);
                } else {
                    pickedQuantity = newQuantityInt;
                    quantityText.setText(Integer.toString(newQuantityInt));
                    mPlusBarcodeScan();
                }

                if (newQuantityInt == requiredQuantity) {
                    m_setQuantityReached();
                }
                if (isPVWorkflow) {
                    if (newQuantityInt == 0) {
                        articleScanned = false;
                    } else {
                        articleScanned = true;
                    }
                    if (!pickPickPvVkoEachPieceBln) {
                        if (newQuantityInt > 0) {
                            if (newQuantityInt < requiredQuantity) {
                                textViewAction.setText(getString(R.string.scan_article_or_salesorder_or_pickcartbox));

                            } else if (newQuantityInt == requiredQuantity) {
                                textViewAction.setText(getString(R.string.message_scan_pickcart_or_salesorder));
                            }
                        }
                    }
                }
            }
        } else {
            //negative
            if (pickedQuantity >= 1) {
                int newQuantityInt = pickedQuantity - amount;
                if (newQuantityInt <= 0) {
                    newQuantityInt = 0;
                }
                pickedQuantity = newQuantityInt;
                quantityText.setText(Integer.toString(newQuantityInt));
                mMinusBarcodeScan();
            } else {
                cUserInterface.doNope(quantityText, true, true);
            }
        }
    }
    private void mPlusBarcodeScan() {
        String barcode = currentBarcode.getBarcode();
        int quantityperunitofmeasure = cText.stringToInteger(currentBarcode.getQuantityperunitofmeasure());
        cPickorderLineBarcodeEntity pickorderLineBarcodeEntity = pickorderLineBarcodeViewModel.getPickorderLineBarcodeByBarcode(barcode);
        if (pickorderLineBarcodeEntity != null) {
            //already exists
            int currentAmount = cText.stringToInteger(pickorderLineBarcodeEntity.getQuantityhandled());
            int newAmount = currentAmount + quantityperunitofmeasure;
            pickorderLineBarcodeViewModel.updateBarcodeAmount(barcode, newAmount);
        }
        else {
            //doesn't exist yet
            cPickorderLineBarcodeEntity pickorderLineBarcodeEntity1 = new cPickorderLineBarcodeEntity();
            pickorderLineBarcodeEntity1.setLineno(Integer.toString(pickorderLineEntity.getLineNo()));
            pickorderLineBarcodeEntity1.setBarcode(barcode);
            pickorderLineBarcodeEntity1.setQuantityhandled(currentBarcode.getQuantityperunitofmeasure());
            pickorderLineBarcodeViewModel.insert(pickorderLineBarcodeEntity1);
        }
    }
    private void mMinusBarcodeScan() {
        String barcode = currentBarcode.getBarcode();
        int quantityperunitofmeasure = cText.stringToInteger(currentBarcode.getQuantityperunitofmeasure());
        cPickorderLineBarcodeEntity pickorderLineBarcodeEntity = pickorderLineBarcodeViewModel.getPickorderLineBarcodeByBarcode(barcode);
        if (pickorderLineBarcodeEntity != null) {
            //already exists
            int currentAmount = cText.stringToInteger(pickorderLineBarcodeEntity.getQuantityhandled());
            int newAmount = currentAmount - quantityperunitofmeasure;
            if (newAmount <= 0) {
                pickorderLineBarcodeViewModel.delete(pickorderLineBarcodeEntity);
            }
            else {
                //bigger than zero, update
                pickorderLineBarcodeViewModel.updateBarcodeAmount(barcode, newAmount);
            }
        }
        else {
            //doesn't exist yet, weird, no nothing
        }
    }

    private void m_setQuantityReached() {
        if (isPVWorkflow) {
            mSetQuantityPVReached();
            return;
        }
        imageButtonDone.setImageResource(R.drawable.ic_doublecheck_black_24dp);
        if (pickAutoAcceptBln) {
            m_pickingDone();
            return;
        }
    }

    private void mSetQuantityPVReached() {
        if (!pickPickPvVkoEachPieceBln) {
            textViewAction.setText(getString(R.string.message_scan_pickcart_or_salesorder));
        } else {
            articleScanned = true;
            textViewAction.setText(getString(R.string.message_scan_pickcart_or_salesorder));
        }
    }

    private void mAllScannedPV() {
        imageButtonDone.setImageResource(R.drawable.ic_doublecheck_black_24dp);
        if (pickAutoAcceptBln) {
            m_pickingPVDone();
            return;
        }
    }

    private void m_askSureUnderpick() {
        m_checkAndCloseOpenDialogs();
        AlertDialog.Builder builder = new AlertDialog.Builder(thisContext);
        builder.setTitle(R.string.message_underpick_header);
        builder.setMessage(getString(R.string.message_underpick_text, cText.doubleToString(requiredQuantity), Integer.toString(pickedQuantity)));
        builder.setPositiveButton(R.string.button_close_orderline, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (isPVWorkflow) {
                    m_pickingPVDone();
                    return;
                }
                m_pickingDone();
            }
        });
        builder.setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //do nothing (close the dialog)
            }
        });
        builder.show();
    }

    private void m_pickingDone() {
        mUpdatePickorderLine();
        m_getNextPickLineFromLocation();
        //m_showWorkplacePicker();
    }
    private void m_pickingPVDone() {
        cPickorderLineEntity refreshedpickorderLineEntity = pickorderLineViewModel.getPickLineByRecordid(pickorderLineEntity.getRecordid());
        if (pickedQuantity > 0 && refreshedpickorderLineEntity.getProcessingsequence().isEmpty()) {
            cUserInterface.doNope(textViewAction, true, false);
            textViewAction.setText(R.string.message_scan_pickcart_or_salesorder);
            return;
        }
        m_getNextPickLineFromLocation();
        //m_showWorkplacePicker();
    }

    public void mUpdatePickorderLine() {
        Double d = (double) pickedQuantity;
        pickorderLineViewModel.updateOrderLineQuantity(pickorderLineEntity.getRecordid(), d);
        pickorderLineViewModel.updateOrderLineLocalStatus(pickorderLineEntity.getRecordid(), cPickorderLine.LOCALSTATUS_DONE_NOTSENT);
    }

//    public Boolean mUpdatePickorderLine() {
//        Double d = (double) pickedQuantity;
//        pickorderLineViewModel.updateOrderLineQuantity(pickorderLineEntity.getRecordid(), d);
//        //pickorderLineViewModel.updateOrderLineLocalStatus(pickorderLineEntity.getRecordid(), cPickorderLine.LOCALSTATUS_DONE_NOTSENT);
//        cPickorderLineEntity updatedPickorderLineEntity = pickorderLineViewModel.getPickLineByRecordid(pickorderLineEntity.getRecordid());
//        return mSendPickorderLine(updatedPickorderLineEntity);
//    }

    public void mUpdatePickorderLineProcessingSequence(String processingsequence) {
        pickorderLineViewModel.updateOrderLineProcessingSequence(pickorderLineEntity.getRecordid(), processingsequence);
    }

    public void m_rejectPickorderLine() {
        pickorderLineViewModel.updateOrderLineQuantity(pickorderLineEntity.getRecordid(), 0d);
        pickorderLineViewModel.updateOrderLineLocalStatus(pickorderLineEntity.getRecordid(), cPickorderLine.LOCALSTATUS_NEW);
        m_goBack();
    }

    public void m_acceptPickorderLine() {
        Double d = (double) pickedQuantity;
        pickorderLineViewModel.updateOrderLineQuantity(pickorderLineEntity.getRecordid(), d);
        pickorderLineViewModel.updateOrderLineLocalStatus(pickorderLineEntity.getRecordid(), cPickorderLine.LOCALSTATUS_DONE_NOTSENT);
        m_goBack();
    }

    private void m_goBack() {
        Intent intent = new Intent(thisContext, PickorderLinesActivity.class);
        startActivity(intent);
    }

    private void m_getNextPickLineFromLocation() {
        cPickorderLineEntity newPickorderLineEntity = pickorderLineViewModel.getNextPickLineFromLocation(pickorderLineEntity.getBincode());
        if (newPickorderLineEntity != null) {
            pickorderLineEntity = newPickorderLineEntity;
            pickorderLineViewModel.updateOrderLineLocalStatus(newPickorderLineEntity.getRecordid(), cPickorderLine.LOCALSTATUS_BUSY);
            mDoNewArticle();
        } else {
            //nothing left for this location
            Intent intent = new Intent(thisContext, PickorderLinesActivity.class);
            startActivity(intent);
        }
    }

    private void mDoNewArticle() {
        if (isPVWorkflow) {
            textViewAction.setText(getString(R.string.scan_article));
        }
        cUserInterface.playSound(R.raw.message, null);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shrink_and_fade);
        pickorderPickContainer.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mFieldsInitialize();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public void m_HandleQuantityChosen(int newQuantity) {
        mWriteBarcodes(newQuantity);
        pickedQuantity = newQuantity;
        quantityText.setText(Integer.toString(newQuantity));
        if (newQuantity == requiredQuantity) {
            imageButtonDone.setImageResource(R.drawable.ic_doublecheck_black_24dp);
        } else {
            imageButtonDone.setImageResource(R.drawable.ic_check_black_24dp);
        }
    }
    private void mWriteBarcodes(int quantity){
        String barcode = currentBarcode.getBarcode();
        int barcodeQuantity = quantity * cText.stringToInteger(currentBarcode.getQuantityperunitofmeasure());
        cPickorderLineBarcodeEntity pickorderLineBarcodeEntity = pickorderLineBarcodeViewModel.getPickorderLineBarcodeByBarcode(barcode);
        if (pickorderLineBarcodeEntity != null) {
            if (quantity == 0) {
                pickorderLineBarcodeViewModel.delete(pickorderLineBarcodeEntity);
                return;
            }
            //already exists
            pickorderLineBarcodeViewModel.updateBarcodeAmount(barcode, barcodeQuantity);
        }
        else {
            if (quantity == 0) {
                return;
            }
            //doesn't exist yet
            cPickorderLineBarcodeEntity pickorderLineBarcodeEntity1 = new cPickorderLineBarcodeEntity();
            pickorderLineBarcodeEntity1.setLineno(Integer.toString(pickorderLineEntity.getLineNo()));
            pickorderLineBarcodeEntity1.setBarcode(barcode);
            pickorderLineBarcodeEntity1.setQuantityhandled(Integer.toString(barcodeQuantity));
            pickorderLineBarcodeViewModel.insert(pickorderLineBarcodeEntity1);
        }
    }

    private void m_setManual(Boolean manual) {
        if (!manual) {
            imageButtonMinus.setVisibility(View.INVISIBLE);
            imageButtonPlus.setVisibility(View.INVISIBLE);
        } else {
            imageButtonMinus.setVisibility(View.VISIBLE);
            imageButtonPlus.setVisibility(View.VISIBLE);
        }
    }

    public void setChosenWorkplace(cWorkplaceEntity workplaceEntity) {
        android.support.v4.app.Fragment l_FragmentFrg = getSupportFragmentManager().findFragmentByTag(WORKPLACEFRAGMENT_TAG);
        if (l_FragmentFrg != null) {
            android.support.v4.app.DialogFragment l_DialogFragmentDfr = (android.support.v4.app.DialogFragment) l_FragmentFrg;
            l_DialogFragmentDfr.dismiss();
        }
    }

    public void setChosenBarcode(cPickorderBarcodeEntity pickorderBarcodeEntity) {
        android.support.v4.app.Fragment l_FragmentFrg = getSupportFragmentManager().findFragmentByTag(BARCODEPICKERFRAGMENT_TAG);
        if (l_FragmentFrg != null) {
            android.support.v4.app.DialogFragment l_DialogFragmentDfr = (android.support.v4.app.DialogFragment) l_FragmentFrg;
            l_DialogFragmentDfr.dismiss();
        }
        currentBarcode = pickorderBarcodeEntity;
        barcodeQuantity = cText.stringToInteger(pickorderBarcodeEntity.getQuantityperunitofmeasure());
        String barcodeText = pickorderBarcodeEntity.getBarcode() + " (" + cText.stringToInteger(pickorderBarcodeEntity.getQuantityperunitofmeasure()) + ")";
        articleBarcodeText.setText(barcodeText);
        m_changeQuantity(true, barcodeQuantity);
    }

    private void doUnknownScan(String errormessage, String barcode) {
        cUserInterface.doExplodingScreen(errormessage, barcode, true, true);
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


    private void mSendBarcodes() {
        pickorderLineViewModel.getPickorderLineEntitiesToSend().observe(this, new Observer<List<cPickorderLineEntity>>() {
            String userName = currentUser;
            String branch = currentBranch;
            String orderNumber = currentOrder;
            Long lineNumber;
            String handledTimeStamp;
            String containerHandled = "";
            Double quantityHandled;
            ArrayList<cContainer> containers;
            cContainer container;
            String processingSequence = "";

            @Override
            public void onChanged(List<cPickorderLineEntity> cPickorderLineEntities) {
                List<c_BarcodeHandledUwbh> barcodeHandledUwbhsList = new ArrayList<>();
                if (cPickorderLineEntities != null) {
                    for (cPickorderLineEntity pickOrderLine : cPickorderLineEntities) {
                        //pickorderLineViewModel.updateOrderLineLocalStatus(pickOrderLine.getRecordid(), cPickorderLine.LOCALSTATUS_DONE_SENDING);
                        lineNumber = pickOrderLine.getLineNo().longValue();
                        quantityHandled = pickOrderLine.getQuantityhandled();
                        if (isPVWorkflow) {
                            processingSequence = pickOrderLine.getProcessingsequence();
                        }
                        if (quantityHandled == 0) {

                        }
                        List<cPickorderLineBarcodeEntity> pickorderLineBarcodeEntities = pickorderLineBarcodeViewModel.getPickorderLineBarcodesForLineNo(pickOrderLine.getLineNo());
                        if (pickorderLineBarcodeEntities != null && pickorderLineBarcodeEntities.size() > 0) {
                            for (cPickorderLineBarcodeEntity pickorderLineBarcodeEntity : pickorderLineBarcodeEntities) {
                                c_BarcodeHandledUwbh barcodeHandledUwbh = new c_BarcodeHandledUwbh();
                                barcodeHandledUwbh.setG_BarcodeStr(pickorderLineBarcodeEntity.getBarcode());
                                barcodeHandledUwbh.setG_QuantityHandledDbl(cText.stringToDouble(pickorderLineBarcodeEntity.getQuantityhandled()));
                                barcodeHandledUwbhsList.add(barcodeHandledUwbh);
                                pickorderLineBarcodeViewModel.delete(pickorderLineBarcodeEntity);
                            }
                        }

                        handledTimeStamp = m_GetCurrentDateTimeForWebservice();

                        pickorderLineViewModel.pickOrderLineHandled(userName, branch, orderNumber, lineNumber, handledTimeStamp, containerHandled, barcodeHandledUwbhsList, "", processingSequence);

                        pickorderLineViewModel.updateOrderLineLocalStatus(pickOrderLine.getRecordid(), cPickorderLine.LOCALSTATUS_DONE_SENT);

                        //pickorderLineViewModel.pickOrderLineHandled(userName, branch, "error", lineNumber, handledTimeStamp, containerHandled, barcodeHandledUwbhsList, "", processingSequence);
//                        cWebresult webresult = null;
//                        try {
//                            webresult = pickorderLineViewModel.pickOrderLineHandled(userName, branch, "error", lineNumber, handledTimeStamp, containerHandled, barcodeHandledUwbhsList, "", processingSequence);
//                        } catch (ExecutionException e) {
//                            e.printStackTrace();
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                        //any errors?
//                        if (!webresult.getResultBln()) {
//                            //couldn't update
//                        }


                    }
                }
            }
        });
    }
//    private Boolean mSendPickorderLine(cPickorderLineEntity pickorderLineEntity) {
//        List<c_BarcodeHandledUwbh> barcodeHandledUwbhsList = new ArrayList<>();
//        String userName = currentUser;
//        String branch = currentBranch;
//        String orderNumber = currentOrder;
//        Long lineNumber;
//        String handledTimeStamp;
//        String containerHandled = "";
//        Double quantityHandled;
//        ArrayList<cContainer> containers;
//        cContainer container;
//        String processingSequence = "";
//
//
//        lineNumber = pickorderLineEntity.getLineNo().longValue();
//        quantityHandled = pickorderLineEntity.getQuantityhandled();
//        if (isPVWorkflow) {
//            processingSequence = pickorderLineEntity.getProcessingsequence();
//        }
//        if (quantityHandled == 0) {
//
//        }
//        List<cPickorderLineBarcodeEntity> pickorderLineBarcodeEntities = pickorderLineBarcodeViewModel.getPickorderLineBarcodesForLineNo(pickorderLineEntity.getLineNo());
//        if (pickorderLineBarcodeEntities != null && pickorderLineBarcodeEntities.size() > 0) {
//            for (cPickorderLineBarcodeEntity pickorderLineBarcodeEntity : pickorderLineBarcodeEntities) {
//                c_BarcodeHandledUwbh barcodeHandledUwbh = new c_BarcodeHandledUwbh();
//                barcodeHandledUwbh.setG_BarcodeStr(pickorderLineBarcodeEntity.getBarcode());
//                barcodeHandledUwbh.setG_QuantityHandledDbl(cText.stringToDouble(pickorderLineBarcodeEntity.getQuantityhandled()));
//                barcodeHandledUwbhsList.add(barcodeHandledUwbh);
//            }
//        }
//
//        handledTimeStamp = m_GetCurrentDateTimeForWebservice();
//
//        //pickorderLineViewModel.pickOrderLineHandled(userName, branch, orderNumber, lineNumber, handledTimeStamp, containerHandled, barcodeHandledUwbhsList, "", processingSequence);
//        cWebresult webresult = new cWebresult();
//        try {
//            webresult = pickorderLineViewModel.pickOrderLineHandled(userName, branch, orderNumber, lineNumber, handledTimeStamp, containerHandled, barcodeHandledUwbhsList, "", processingSequence);
//        } catch (ExecutionException e) {
//            webresult.setSuccessBln(false);
//            webresult.setResultBln(false);
//            webresult.getResultObl().add(e.getMessage());
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            webresult.setSuccessBln(false);
//            webresult.setResultBln(false);
//            webresult.getResultObl().add(e.getMessage());
//            e.printStackTrace();
//        }
//        //any errors?
//        if (!webresult.getResultBln()) {
//            //couldn't send
//            String messages = "";
//            for (String message : webresult.getResultObl()) {
//                messages += message + cText.NEWLINE;
//            }
//            cUserInterface.doExplodingScreen(thisContext, fragmentManager, "not sent", messages, true, true);
//            return false;
//        }
//        //delete all scanned barcodes
//        pickorderLineBarcodeViewModel.deleteAll();
//        pickorderLineViewModel.updateOrderLineLocalStatus(pickorderLineEntity.getRecordid(), cPickorderLine.LOCALSTATUS_DONE_SENT);
//        return true;
//    }

}
