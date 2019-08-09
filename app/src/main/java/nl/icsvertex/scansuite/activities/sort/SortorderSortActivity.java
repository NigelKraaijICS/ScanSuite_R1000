package nl.icsvertex.scansuite.activities.sort;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.List;

import ICS.Weberror.cWeberrorEntity;
import ICS.Weberror.cWeberrorViewModel;
import SSU_WHS.Basics.ArticleImages.cArticleImageEntity;
import SSU_WHS.Basics.ArticleImages.cArticleImageViewModel;
import SSU_WHS.Basics.BarcodeLayouts.cBarcodeLayout;
import SSU_WHS.Basics.BarcodeLayouts.cBarcodeLayoutEntity;
import SSU_WHS.Basics.BarcodeLayouts.cBarcodeLayoutViewModel;
import SSU_WHS.Picken.PickorderBarcodes.cPickorderBarcodeEntity;
import SSU_WHS.Picken.PickorderBarcodes.cPickorderBarcodeViewModel;
import SSU_WHS.Picken.PickorderLines.cPickorderLine;
import SSU_WHS.Picken.PickorderLines.cPickorderLineEntity;
import SSU_WHS.Picken.PickorderLines.cPickorderLineViewModel;
import SSU_WHS.Picken.Pickorders.cPickorderViewModel;
import SSU_WHS.Picken.SalesOrderPackingTable.cSalesOrderPackingTableEntity;
import SSU_WHS.Picken.SalesOrderPackingTable.cSalesOrderPackingTableViewModel;
import SSU_WHS.Basics.Settings.cSettingsEnums;
import SSU_WHS.Basics.Settings.cSettingsViewModel;
import SSU_WHS.Picken.Warehouseorder.cWarehouseorderViewModel;
import SSU_WHS.Webservice.cWebservice;
import SSU_WHS.General.cPublicDefinitions;
import ICS.Utils.Scanning.cBarcodeScanDefinitions;
import ICS.Utils.cRegex;
import ICS.Utils.cSharedPreferences;
import ICS.Utils.cText;
import ICS.Utils.cUserInterface;
import nl.icsvertex.scansuite.fragments.dialogs.AcceptRejectFragment;
import nl.icsvertex.scansuite.fragments.dialogs.ArticleFullViewFragment;
import nl.icsvertex.scansuite.fragments.dialogs.NumberpickerFragment;
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
import static ICS.Utils.cDateAndTime.m_GetCurrentDateTimeForWebservice;
import static android.view.View.GONE;

public class SortorderSortActivity extends AppCompatActivity {
    Context thisContext = null;
    android.support.v4.app.FragmentManager fragmentManager;

    static final String NUMBERPICKERFRAGMENT_TAG = "NUMBERPICKERFRAGMENT_TAG";
    static final String BARCODEPICKERFRAGMENT_TAG = "BARCODEPICKERFRAGMENT_TAG";
    static final String ACCEPTREJECTFRAGMENT_TAG = "ACCEPTREJECTFRAGMENT_TAG";
    static final String ACTIVITYNAME = "SortorderSortActivity";

    cPickorderLineViewModel pickorderLineViewModel;
    cPickorderLineEntity pickorderLineEntity;
    cSettingsViewModel settingsViewModel;
    cArticleImageViewModel articleImageViewModel;
    cWarehouseorderViewModel warehouseorderViewModel;
    cPickorderBarcodeViewModel pickorderBarcodeViewModel;
    cBarcodeLayoutViewModel barcodeLayoutViewModel;
    cPickorderViewModel pickorderViewModel;
    cSalesOrderPackingTableViewModel salesOrderPackingTableViewModel;
    cWeberrorViewModel weberrorViewModel;

    List<cPickorderBarcodeEntity> barcodesObl;
    List<cBarcodeLayoutEntity> packtableBarcodeLayouts;
    List<cBarcodeLayoutEntity> articleBarcodeLayouts;

    //region Settings
    Boolean pickWithPicturesBln;
    String genericItemExtraField1Str;
    String genericItemExtraField2Str;
    String genericItemExtraField3Str;
    String genericItemExtraField4Str;
    //endregion


    Boolean articleScanned;
    Boolean packingTableScanned;

    int barcodeQuantity;

    //things we got from pickorderlines
    Integer chosenItemno;
    String scannedBarcode;

    String currentUser;
    String currentBranch;
    String currentOrder;

    int pickedQuantity;
    Double requiredQuantity;

    int pickCounterMinusHelper;
    int pickCounterPlusHelper;

    IntentFilter barcodeIntentFilter;
    private BroadcastReceiver barcodeReceiver;

    Handler minusHandler;
    Handler plusHandler;

    ImageView toolbarImage;
    TextView toolbarTitle;
    TextView toolbarSubtext;
    ImageView toolbarImageHelp;

    TextView genericItemExtraField1Text;
    TextView genericItemExtraField2Text;
    TextView genericItemExtraField3Text;
    TextView genericItemExtraField4Text;

    CardView articleContainer;
    CardView locationContainer;
    CardView cardViewAdviceLocation;
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
    TextView textViewPackingTable;
    TextView textViewAction;
    TextView textAdviceLocation;

    AppCompatImageButton imageButtonMinus;
    AppCompatImageButton imageButtonPlus;
    AppCompatImageButton imageButtonDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sortorder_sort);

        thisContext = this;
        barcodeQuantity = 1;

        chosenItemno = getIntent().getIntExtra(cPublicDefinitions.SORTING_CHOSENITEMNO, -1);
        scannedBarcode = getIntent().getStringExtra(cPublicDefinitions.SORTING_SCANNEDBARCODE);
        currentOrder = cSharedPreferences.getSharedPreferenceString(cPublicDefinitions.PREFERENCE_CURRENT_ORDER, "");

        pickorderLineViewModel = ViewModelProviders.of(this).get(cPickorderLineViewModel.class);
        pickorderBarcodeViewModel = ViewModelProviders.of(this).get(cPickorderBarcodeViewModel.class);
        articleImageViewModel = ViewModelProviders.of(this).get(cArticleImageViewModel.class);
        settingsViewModel = ViewModelProviders.of(this).get(cSettingsViewModel.class);
        pickorderViewModel = ViewModelProviders.of(this).get(cPickorderViewModel.class);
        warehouseorderViewModel = ViewModelProviders.of(this).get(cWarehouseorderViewModel.class);
        barcodeLayoutViewModel = ViewModelProviders.of(this).get(cBarcodeLayoutViewModel.class);
        salesOrderPackingTableViewModel = ViewModelProviders.of(this).get(cSalesOrderPackingTableViewModel.class);
        weberrorViewModel = ViewModelProviders.of(this).get(cWeberrorViewModel.class);

        fragmentManager = getSupportFragmentManager();
        //so what did we get?
        if (chosenItemno != null && chosenItemno != -1) {
            pickorderLineEntity = pickorderLineViewModel.getPickLineByRecordid(chosenItemno);
        }
        if (scannedBarcode != null) {
            cPickorderBarcodeEntity pickorderBarcodeEntity = pickorderBarcodeViewModel.getPickOrderBarcodeByBarcode(scannedBarcode);
            String itemno =  pickorderBarcodeEntity.getItemno();
            String variant =  pickorderBarcodeEntity.getVariantcode();
            pickorderLineEntity = pickorderLineViewModel.getSortorderLineNotHandledByItemNoAndVariant(itemno, variant);
        }
        //update orderline status
        pickorderLineViewModel.updateOrderLineLocalStatus(pickorderLineEntity.getRecordid(), cPickorderLine.LOCALSTATUS_BUSY);

        currentUser = cSharedPreferences.getSharedPreferenceString(cPublicDefinitions.PREFERENCE_CURRENT_USER, "");
        currentBranch = cSharedPreferences.getSharedPreferenceString(cPublicDefinitions.PREFERENCE_CURRENT_BRANCH, "");

        //fill picktable barcodeLayoutList
        packtableBarcodeLayouts = barcodeLayoutViewModel.getBarcodeLayoutsOfType(cBarcodeLayout.barcodeLayoutEnu.PACKINGTABLEBIN.toString());
        //fill picktable barcodeLayoutList
        articleBarcodeLayouts = barcodeLayoutViewModel.getBarcodeLayoutsOfType(cBarcodeLayout.barcodeLayoutEnu.ARTICLE.toString());

        mBarcodeReceiver();

        LocalBroadcastManager.getInstance(thisContext).registerReceiver(m_numberReceiver, new IntentFilter(cPublicDefinitions.NUMBERINTENT_NUMBER));

        mFindViews();
        mSetSettings();
        mFieldsInitialize();
        mSetToolbar();
        mSetListeners();
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
        super.onPause();
    }
    @Override
    protected void onResume() {
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
        registerReceiver(barcodeReceiver,barcodeIntentFilter);

    }

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

    private void mFindViews() {
        toolbarImage = findViewById(R.id.toolbarImage);
        toolbarTitle = findViewById(R.id.toolbarTitle);
        toolbarSubtext = findViewById(R.id.toolbarSubtext);
        toolbarImageHelp = findViewById(R.id.toolbarImageHelp);

        genericItemExtraField1Text = findViewById(R.id.genericItemExtraField1Text);
        genericItemExtraField2Text = findViewById(R.id.genericItemExtraField2Text);
        genericItemExtraField3Text = findViewById(R.id.genericItemExtraField3Text);
        genericItemExtraField4Text = findViewById(R.id.genericItemExtraField4Text);

        articleContainer = findViewById(R.id.addressContainer);
        articleDescriptionText = findViewById(R.id.articleDescriptionText);
        articleDescription2Text = findViewById(R.id.articleDescription2Text);
        articleItemText = findViewById(R.id.articleItemText);
        articleBarcodeText = findViewById(R.id.articleBarcodeText);
        articleVendorItemText = findViewById(R.id.articleVendorItemText);
        locationContainer = findViewById(R.id.locationContainer);
        locationContainer.setVisibility(GONE);
        binText = findViewById(R.id.binText);
        cardViewAdviceLocation = findViewById(R.id.cardViewAdviceLocation);
        sourcenoText = findViewById(R.id.sourcenoText);
        sourcenoContainer = findViewById(R.id.sourcenoContainer);
        textViewPackingTable = findViewById(R.id.textViewPackingTable);

        containerText = findViewById(R.id.containerText);
        quantityText = findViewById(R.id.quantityText);
        quantityRequiredText = findViewById(R.id.quantityRequiredText);
        articleThumbImageView = findViewById(R.id.articleThumbImageView);
        imageButtonBarcode = findViewById(R.id.imageButtonBarcode);
        //imageButtonBarcode.setVisibility(GONE);
        imageButtonMinus = findViewById(R.id.imageButtonMinus);
        imageButtonPlus = findViewById(R.id.imageButtonPlus);
        imageButtonDone = findViewById(R.id.imageButtonDone);
        textViewAction = findViewById(R.id.textViewAction);
        textAdviceLocation = findViewById(R.id.textAdviceLocation);
    }
    private void mSetSettings() {
        String pickWithPictures = settingsViewModel.getSetting(cSettingsEnums.p_Settings14Enu.PICK_WITH_PICTURE.toString());
        pickWithPicturesBln = cText.stringToBoolean(pickWithPictures, false);
        genericItemExtraField1Str = settingsViewModel.getSetting(cSettingsEnums.p_Settings14Enu.GENERIC_ITEM_EXTRA_FIELD1.toString());
        genericItemExtraField2Str = settingsViewModel.getSetting(cSettingsEnums.p_Settings14Enu.GENERIC_ITEM_EXTRA_FIELD2.toString());
        genericItemExtraField3Str = settingsViewModel.getSetting(cSettingsEnums.p_Settings14Enu.GENERIC_ITEM_EXTRA_FIELD3.toString());
        genericItemExtraField4Str = settingsViewModel.getSetting(cSettingsEnums.p_Settings14Enu.GENERIC_ITEM_EXTRA_FIELD4.toString());
    }
    private void mFieldsInitialize() {
        imageButtonDone.setImageResource(R.drawable.ic_check_black_24dp);
        barcodeQuantity = 1;
        pickedQuantity = 0;
        mSetExtraFields();
        if (articleScanned == null) {

        }
        textAdviceLocation.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        textAdviceLocation.setSingleLine(true);
        textAdviceLocation.setMarqueeRepeatLimit(5);
        textAdviceLocation.setSelected(true);
        mSetAdviceLocation();
        pickCounterPlusHelper = 0;
        pickCounterMinusHelper = 0;
        toolbarSubtext.setText(currentOrder);
        containerText.setVisibility(GONE);
        articleDescriptionText.setText(pickorderLineEntity.getDescription());
        articleDescription2Text.setText(pickorderLineEntity.getDescription2());
        String itemText = pickorderLineEntity.getItemno() + " " + pickorderLineEntity.getVariantcode();
        articleItemText.setText(itemText);
        barcodesObl = pickorderBarcodeViewModel.getPickorderBarcodesForItemVariantCode(pickorderLineEntity.getItemno(), pickorderLineEntity.getVariantcode());
        if (barcodesObl !=null && barcodesObl.size()>0) {
            cPickorderBarcodeEntity barcodeEntity =  barcodesObl.get(0);
            String barcodeText = barcodeEntity.getBarcode() + " (" + cText.stringToInteger(barcodeEntity.getQuantityperunitofmeasure()) + ")";
            articleBarcodeText.setText(barcodeText);
        }
        String l_vendorInfoStr = pickorderLineEntity.getVendoritemno() + ' ' + pickorderLineEntity.getVendoritemdescription();
        articleVendorItemText.setText(l_vendorInfoStr);
        sourcenoText.setText(pickorderLineEntity.getSourceno());
        if (pickorderLineEntity.getSourceno().trim().isEmpty()) {
            sourcenoContainer.setVisibility(View.INVISIBLE);
        }

        containerText.setText(pickorderLineEntity.getContainer());
        containerText.setText("");
        quantityText.setText("0");
        requiredQuantity = pickorderLineEntity.getQuantity();
        quantityRequiredText.setText(cText.doubleToString(requiredQuantity));
        textViewPackingTable.setText("");
        //imageButtonBarcode.setVisibility(View.GONE);
        m_setManual(false);

        //Are we doing pictures?
        if (pickWithPicturesBln) {
            articleThumbImageView.setVisibility(View.VISIBLE);
            //pictures!
            articleImageViewModel.getArticleImages(false,currentUser, "", pickorderLineEntity.getItemno(), pickorderLineEntity.getVariantcode(), true).observe(this, new Observer<List<cArticleImageEntity>>() {
                @Override
                public void onChanged(@Nullable List<cArticleImageEntity> articleImageEntities) {
                    if (articleImageEntities != null) {
                        if (articleImageEntities.size() > 0) {
                            byte[] decodedString = Base64.decode(articleImageEntities.get(0).getImage(), Base64.DEFAULT);
                            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                            articleThumbImageView.setImageBitmap(decodedByte);
                        } else {
                            articleThumbImageView.setImageDrawable(ContextCompat.getDrawable(thisContext, R.drawable.ic_no_image_lightgrey_24dp));
                        }
                    }
                    else {
                        articleThumbImageView.setImageDrawable(ContextCompat.getDrawable(thisContext, R.drawable.ic_no_image_lightgrey_24dp));
                    }
                }
            });
        }
        else {
            //no pictures
            articleThumbImageView.setVisibility(View.INVISIBLE);
        }

        }
    private void mSetExtraFields() {
        //not with sorting, maybe later
//        if (!genericItemExtraField1Str.trim().isEmpty()) {
//            genericItemExtraField1Text.setVisibility(View.VISIBLE);
//            genericItemExtraField1Text.setText(pickorderLineEntity.getExtrafield1());
//        }
//        if (!genericItemExtraField2Str.trim().isEmpty()) {
//            genericItemExtraField2Text.setVisibility(View.VISIBLE);
//            genericItemExtraField2Text.setText(pickorderLineEntity.getExtrafield2());
//        }
//        if (!genericItemExtraField3Str.trim().isEmpty()) {
//            genericItemExtraField3Text.setVisibility(View.VISIBLE);
//            genericItemExtraField3Text.setText(pickorderLineEntity.getExtrafield3());
//        }
//        if (!genericItemExtraField4Str.trim().isEmpty()) {
//            genericItemExtraField4Text.setVisibility(View.VISIBLE);
//            genericItemExtraField4Text.setText(pickorderLineEntity.getExtrafield4());
//        }
    }
    private void mSetToolbar() {
        toolbarImage.setImageResource(R.drawable.ic_menu_pick);
        toolbarTitle.setText(R.string.screentitle_sortordersort);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void mSetListeners() {
        mSetArticleImageListener();
        mSetImageButtonBarcodeListener();
        m_SetPlusListener();
        m_setMinusListener();
        mSetDoneListener();
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

    private void m_SetPlusListener() {

        imageButtonPlus.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (plusHandler != null) return true;
                    plusHandler = new Handler();
                    plusHandler.postDelayed(plusAction, 750);
                }
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(plusHandler == null) return true;
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
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (minusHandler != null) return true;
                    minusHandler = new Handler();
                    minusHandler.postDelayed(minusAction, 750);
                }
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(minusHandler == null) return true;
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

    private void mSetArticleImageListener() {
        articleThumbImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mShowFullArticleFragment();

            }
        });
    }

    private void mSetImageButtonBarcodeListener() {
        imageButtonBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (barcodesObl != null && barcodesObl.size()>1) {
//                    ArrayList<cPickorderBarcodeEntity> showBarcodes = new ArrayList<>(barcodesObl.size());
//                    showBarcodes.addAll(barcodesObl);
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable(cPublicDefinitions.BARCODEFRAGMENT_LIST_TAG, showBarcodes);
//                    final android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
//                    final BarcodePickerFragment barcodePickerFragment = new BarcodePickerFragment();
//                    barcodePickerFragment.setArguments(bundle);
//                    barcodePickerFragment.show(fragmentManager, BARCODEPICKERFRAGMENT_TAG);
//                }
                mHandleScan(barcodesObl.get(0).getBarcode());
                //m_setManual(true);
            }
        });
    }

    private void mSetDoneListener() {
        imageButtonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pickedQuantity == requiredQuantity) {
                    m_sortingDone();
                }
                else {
                    //m_askSureUnderpick();
                }
            }
        });
    }
    private void mSetAdviceLocation() {
        //get advicelocation
        pickorderLineViewModel.getSortLocationAdvice("PICKEN", currentBranch, currentOrder, pickorderLineEntity.getSourceno()).observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(@Nullable List<String> strings) {
                if (strings != null && strings.size() > 0) {
                    //String advicelocations = getString(R.string.advice_locations);
                    String advicelocations = "";
                    for (String advicelocation:strings) {
                        advicelocations +=  " " + advicelocation;
                    }
                    textAdviceLocation.setText(advicelocations);
                }
            }
        });
    }
    private void mSetNumberListener() {
        quantityText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNumberClicked();
            }
        });
        quantityRequiredText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNumberClicked();
            }
        });
    }
    private void mNumberClicked() {
        mShowNumberPicker();
    }

    private void mShowNumberPicker() {
        m_checkAndCloseOpenDialogs();
        final NumberpickerFragment numberpickerFragment = new NumberpickerFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(cPublicDefinitions.NUMBERINTENT_CURRENTQUANTITY, pickedQuantity);
        bundle.putDouble(cPublicDefinitions.NUMBERINTENT_MAXQUANTITY, requiredQuantity);
        numberpickerFragment.setArguments(bundle);
        numberpickerFragment.show(fragmentManager, NUMBERPICKERFRAGMENT_TAG);
    }
    private void mShowFullArticleFragment() {
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

        String barcodePrefixStripped;
        String barcodeType = "";
        //what did we scan?
        for (cBarcodeLayoutEntity barcodeLayout: packtableBarcodeLayouts) {
            String pattern = barcodeLayout.getLayoutValue();
            if (cRegex.p_checkRegexBln(pattern,barcodeStr)) {
                barcodeType = cBarcodeLayout.barcodeLayoutEnu.PACKINGTABLEBIN.toString();
                break;
            }
        }
        if (barcodeType.isEmpty()) {
            //it wasn't a packing table
            for (cBarcodeLayoutEntity barcodeLayout: articleBarcodeLayouts) {
                String pattern = barcodeLayout.getLayoutValue();
                if (cRegex.p_checkRegexBln(pattern, barcodeStr)) {
                    barcodeType = cBarcodeLayout.barcodeLayoutEnu.ARTICLE.toString();
                    break;
                }
            }
        }
        if (barcodeType.isEmpty()) {
            //not a packing table, not an article
            doUnknownScan(getString(R.string.unknow_sort_barcode_type), barcodeStr);
        }
        else {
            Boolean articleBarcodeFound = false;
            barcodePrefixStripped = cRegex.p_stripRegexPrefixStr(barcodeStr);
            if (barcodeType.equalsIgnoreCase(cBarcodeLayout.barcodeLayoutEnu.ARTICLE.toString())) {
                if (articleScanned !=null && articleScanned) {
                    doUnknownScan(getString(R.string.error_scan_packintable_first), barcodePrefixStripped);
                }
                else {
                    for (cPickorderBarcodeEntity pickorderBarcodeEntity : barcodesObl) {
                        if (pickorderBarcodeEntity.getBarcode().equalsIgnoreCase(barcodePrefixStripped)) {
                            articleBarcodeFound = true;
                            articleScanned = true;
                            String salesOrder = pickorderLineEntity.getSourceno();
                            cSalesOrderPackingTableEntity salesOrderPackingTableEntity = salesOrderPackingTableViewModel.getPackingTableForSalesorder(salesOrder);
                            if (salesOrderPackingTableEntity != null) {
                                if (salesOrderPackingTableEntity.getPackingtable() != null && !salesOrderPackingTableEntity.getPackingtable().isEmpty()) {
                                    textViewAction.setText(getString(R.string.scan_sort_location));
                                    //textViewPackingTable.setText(salesOrderPackingTableEntity.getPackingtable());
                                    textViewPackingTable.setText(salesOrderPackingTableEntity.getPackingtable());
                                    cardViewAdviceLocation.setVisibility(View.INVISIBLE);
                                }
                                else {
                                    textViewPackingTable.setText("");
                                    cardViewAdviceLocation.setVisibility(View.VISIBLE);
                                }
                            }
                            else {
                                textViewPackingTable.setText("");
                                cardViewAdviceLocation.setVisibility(View.VISIBLE);
                            }
                            textViewAction.setText(getString(R.string.scan_sort_location));

                            m_changeQuantity(true, 1);
                            break;
                        }
                    }

                    if (!articleBarcodeFound) {
                        doUnknownScan(getString(R.string.error_unknown_barcode), barcodePrefixStripped);
                        return;
                    }
                }
            }
            if (barcodeType.equalsIgnoreCase(cBarcodeLayout.barcodeLayoutEnu.PACKINGTABLEBIN.toString())) {
                if (articleScanned==null || !articleScanned) {
                    cUserInterface.showToastMessage(getString(R.string.message_scan_article_first), R.raw.headsupsound);
                    return;
                }
                else {
                    packingTableScanned = true;
                    String salesOrder = pickorderLineEntity.getSourceno();
                    cSalesOrderPackingTableEntity salesOrderPackingTableEntity = salesOrderPackingTableViewModel.getPackingTableForSalesorder(salesOrder);
                    cSalesOrderPackingTableEntity packingTableSalesOrderEntity = salesOrderPackingTableViewModel.getSalesorderForPackingTable(barcodePrefixStripped);
                    if (packingTableSalesOrderEntity != null && !packingTableSalesOrderEntity.getSalesorder().equalsIgnoreCase(pickorderLineEntity.getSourceno())) {
                        //packingtable already assigned to different sourceno
                        doUnknownScan(getString(R.string.message_packingtable_already_assigned),"");
                        return;
                    }
                    if (salesOrderPackingTableEntity == null) {
                        //doesn't exist yet, insert
                        textViewPackingTable.setText(barcodePrefixStripped);
                        cardViewAdviceLocation.setVisibility(View.INVISIBLE);
                        cSalesOrderPackingTableEntity newSalesOrderPackingTableEntity = new cSalesOrderPackingTableEntity();
                        newSalesOrderPackingTableEntity.setSalesorder(salesOrder);
                        newSalesOrderPackingTableEntity.setPackingtable(barcodePrefixStripped);
                        salesOrderPackingTableViewModel.insert(newSalesOrderPackingTableEntity);
                        mUpdateSortLine(barcodePrefixStripped);
                    }
                    else {
                        //salesorder already exists, packingtable the same?
                        if (barcodePrefixStripped.equalsIgnoreCase(salesOrderPackingTableEntity.getPackingtable())) {
                            //same packingtable
                            mUpdateSortLine(barcodePrefixStripped);
                            textViewPackingTable.setText(salesOrderPackingTableEntity.getPackingtable());
                            cardViewAdviceLocation.setVisibility(View.INVISIBLE);
                        }
                        else {
                            //different packingtable
                            doUnknownScan(getString(R.string.message_salesorder_assigned_different_packingtable),"");
                        }
                    }
                    //textViewPackingTable.setText(barcodePrefixStripped);
                }
            }
        }
    }
    private void m_changeQuantity(Boolean isPositive, Integer amount) {
        if (isPositive) {
            if (pickedQuantity >= requiredQuantity) {
                quantityText.setText(quantityRequiredText.getText());
                cUserInterface.doNope(quantityText, true, true);
                cUserInterface.doNope(quantityRequiredText, false, false);
            }
            else {
                int newQuantityInt = pickedQuantity + amount;
                if (newQuantityInt > requiredQuantity) {
                    //newQuantityInt = requiredQuantity.intValue();
                    cUserInterface.doNope(quantityText, true, true);
                    cUserInterface.doNope(quantityRequiredText, false, false);
                }
                else {
                    pickedQuantity = newQuantityInt;
                    quantityText.setText(Integer.toString(newQuantityInt));
                }

                if (newQuantityInt == requiredQuantity) {
                    m_setQuantityReached();
                }
            }
        }
        else {
            //negative
            if (pickedQuantity >= 1) {
                int newQuantityInt = pickedQuantity - amount;
                if (newQuantityInt <= 0) {
                    newQuantityInt = 0;
                }
                pickedQuantity = newQuantityInt;
                quantityText.setText(Integer.toString(newQuantityInt));
            }
            else {
                cUserInterface.doNope(quantityText, true, true);
            }
        }
    }
    private void m_setQuantityReached() {
        imageButtonDone.setImageResource(R.drawable.ic_doublecheck_black_24dp);

    }
    private void m_sortingDone() {
        m_updatePickorderLine();
        mGoOverView();
    }
    private void mGoOverView() {
        Intent intent = new Intent(thisContext, SortorderLinesActivity.class);
        startActivity(intent);
    }
    private void m_updatePickorderLine() {
        Double d = (double) pickedQuantity;
        pickorderLineViewModel.updateOrderLineQuantity(pickorderLineEntity.getRecordid(), d);
        pickorderLineViewModel.updateOrderLineLocalStatus(pickorderLineEntity.getRecordid(), cPickorderLine.LOCALSTATUS_DONE_NOTSENT);
    }
    public void m_rejectPickorderLine() {
        pickorderLineViewModel.updateOrderLineQuantity(pickorderLineEntity.getRecordid(), 0d);
        pickorderLineViewModel.updateOrderLineLocalStatus(pickorderLineEntity.getRecordid(), cPickorderLine.LOCALSTATUS_NEW);
        mGoBack();
    }
    public void m_acceptPickorderLine() {
        Double d = (double) pickedQuantity;
        pickorderLineViewModel.updateOrderLineQuantity(pickorderLineEntity.getRecordid(), d);
        pickorderLineViewModel.updateOrderLineLocalStatus(pickorderLineEntity.getRecordid(), cPickorderLine.LOCALSTATUS_DONE_NOTSENT);
        mGoBack();
    }
    private void mGoBack() {
        Intent intent = new Intent(thisContext, SortorderLinesActivity.class);
        startActivity(intent);
    }


    public void m_HandleQuantityChosen(int newQuantity) {
        pickedQuantity = newQuantity;
        quantityText.setText(Integer.toString(newQuantity));
        if (newQuantity == requiredQuantity) {
            imageButtonDone.setImageResource(R.drawable.ic_doublecheck_black_24dp);
        }
        else {
            imageButtonDone.setImageResource(R.drawable.ic_check_black_24dp);
        }
    }
    private void m_setManual(Boolean manual) {
        if (!manual) {
            imageButtonMinus.setVisibility(View.INVISIBLE);
            imageButtonPlus.setVisibility(View.INVISIBLE);
        }
        else {
            imageButtonMinus.setVisibility(View.VISIBLE);
            imageButtonPlus.setVisibility(View.VISIBLE);
        }
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

    private void mSendBarcodes() {
        pickorderLineViewModel.getPickorderLineEntitiesToSend().observe(this, new Observer<List<cPickorderLineEntity>>() {
            String userName = currentUser;
            String branch = currentBranch;
            String orderNumber = currentOrder;
            Long lineNumber;
            String handledTimeStamp;

            String barcode;
            String location;
            Double quantityHandled;

            @Override
            public void onChanged(@Nullable List<cPickorderLineEntity> cPickorderLineEntities) {
                if (cPickorderLineEntities != null) {
                    for (cPickorderLineEntity pickOrderLine: cPickorderLineEntities) {
                        lineNumber = pickOrderLine.getLineNo().longValue();
                        quantityHandled = pickOrderLine.getQuantityhandled();
                        location = pickOrderLine.getLocalSortLocation();
                        List<cPickorderBarcodeEntity> barcodesForThisEntities = pickorderBarcodeViewModel.getPickorderBarcodesForItemVariantCode(pickOrderLine.getItemno(),pickOrderLine.getVariantcode());
                        if (barcodesForThisEntities != null) {
                            if(barcodesForThisEntities.size() > 0) {
                                barcode =  barcodesForThisEntities.get(0).getBarcode();
                            }
                        }
                        handledTimeStamp = m_GetCurrentDateTimeForWebservice();
                        pickorderLineViewModel.sortOrderLineHandled(userName, branch, orderNumber, lineNumber, handledTimeStamp, quantityHandled , barcode, null, null, location);
                        pickorderLineViewModel.updateOrderLineLocalStatus(pickOrderLine.getRecordid(), cPickorderLine.LOCALSTATUS_DONE_SENT);
                    }
                }
            }
        });
    }

    private void mUpdateSortLine(String location) {
        articleScanned = false;
        packingTableScanned = false;
        pickorderLineViewModel.updateSortOrder(pickorderLineEntity.getRecordid(), pickedQuantity, location);


            if (pickedQuantity == pickorderLineEntity.getQuantity()) {
                pickorderLineViewModel.updateOrderLineLocalStatus(pickorderLineEntity.getRecordid(), cPickorderLine.LOCALSTATUS_DONE_NOTSENT);
                Intent intent = new Intent(thisContext, SortorderLinesActivity.class);
                startActivity(intent);
            }
        }
    Runnable minusAction = new Runnable() {
        @Override public void run() {
            imageButtonMinus.performClick();
            long millisecs;
            if (pickCounterMinusHelper < 10) {
                millisecs = 200;
            }
            else if (pickCounterMinusHelper < 20) {
                millisecs = 150;
            }
            else if (pickCounterMinusHelper < 30) {
                millisecs = 100;
            }
            else if (pickCounterMinusHelper < 40) {
                millisecs = 50;
            }
            else {
                millisecs = 50;
            }
            m_doMinus(this, millisecs);
        }
    };
    private void m_doMinus(Runnable runnable, long milliSecs) {
        minusHandler.postDelayed(runnable, milliSecs);
        pickCounterMinusHelper += 1;
    }

    Runnable plusAction = new Runnable() {
        @Override public void run() {
            imageButtonPlus.performClick();
            long millisecs;
            if (pickCounterPlusHelper < 10) {
                millisecs = 200;
            }
            else if (pickCounterPlusHelper < 20) {
                millisecs = 150;
            }
            else if (pickCounterPlusHelper < 30) {
                millisecs = 100;
            }
            else if (pickCounterPlusHelper < 40) {
                millisecs = 50;
            }
            else {
                millisecs = 50;
            }
            m_doPlus(this,millisecs);
        }
    };
    private void m_doPlus(Runnable runnable, long milliSecs) {
        plusHandler.postDelayed(runnable, milliSecs);
        pickCounterPlusHelper += 1;
    }
}
