package nl.icsvertex.scansuite.Activities.Sort;


import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ICS.Interfaces.iICSDefaultActivity;
import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cRegex;
import ICS.Utils.cText;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Basics.BarcodeLayouts.cBarcodeLayout;
import SSU_WHS.Basics.Settings.cSetting;
import SSU_WHS.General.cPublicDefinitions;
import SSU_WHS.Picken.PickorderBarcodes.cPickorderBarcode;
import SSU_WHS.Picken.PickorderLinePackAndShip.cPickorderLinePackAndShip;
import SSU_WHS.Picken.PickorderLines.cPickorderLine;
import SSU_WHS.Picken.Pickorders.cPickorder;
import SSU_WHS.Picken.SalesOrderPackingTable.cSalesOrderPackingTable;
import nl.icsvertex.scansuite.Fragments.Dialogs.AcceptRejectFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.ArticleFullViewFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.BarcodeFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.NumberpickerFragment;
import nl.icsvertex.scansuite.R;

public class SortorderSortActivity extends AppCompatActivity implements iICSDefaultActivity {

    //Region Public Properties

    //End Region Public Properties

    //Region Private Properties

    private static boolean articleScannedLastBln;
    private boolean defaultBarcodeSelectedBln;
    private List<String> sortingAdviceObl;

    private int pickCounterMinusHelperInt;
    private int pickCounterPlusHelperInt;

    private Handler minusHandler;
    private Handler plusHandler;

    //Region Views

    private ImageView toolbarImage;
    private TextView toolbarTitle;
    private TextView toolbarSubtext;

    private TextView articleDescriptionText;
    private TextView articleDescription2Text;
    private TextView articleItemText;
    private TextView articleBarcodeText;
    private TextView articleVendorItemText;
    private TextView quantityText;
    private TextView quantityRequiredText;
    private ImageView articleThumbImageView;
    private ImageView imageButtonBarcode;
    private TextView sourcenoText;
    private CardView sourcenoContainer;
    private TextView textViewPackingTable;
    private TextView textViewAction;
    private TextView textAdviceLocation;

    private AppCompatImageButton imageButtonMinus;
    private AppCompatImageButton imageButtonPlus;
    private AppCompatImageButton imageButtonDone;

    //End Region Views

    //End Region Private Properties

    //Region Default Methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sortorder_sort);
        LocalBroadcastManager.getInstance(cAppExtension.context).registerReceiver(mNumberReceiver,new IntentFilter(cPublicDefinitions.NUMBERINTENT_NUMBER));
        this.mActivityInitialize();
    }

    @Override
    protected void onDestroy() {
        try {
            super.onDestroy();
            LocalBroadcastManager.getInstance(cAppExtension.context).unregisterReceiver(mNumberReceiver);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        try {
            cBarcodeScan.pUnregisterBarcodeReceiver(this.getClass().getSimpleName());
            LocalBroadcastManager.getInstance(cAppExtension.context).unregisterReceiver(mNumberReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(cAppExtension.context).registerReceiver(mNumberReceiver,new IntentFilter(cPublicDefinitions.NUMBERINTENT_NUMBER));
        cBarcodeScan.pRegisterBarcodeReceiver(this.getClass().getSimpleName());
        cUserInterface.pEnableScanner();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem pvMenuItem) {
        if (pvMenuItem.getItemId() == android.R.id.home) {
            this.mShowAcceptFragment();
            return true;
        }
        return super.onOptionsItemSelected(pvMenuItem);
    }

    @Override
    public void onBackPressed() {
        this.mShowAcceptFragment();
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }



    //End Region Default Methods


    //Region iICSDefaultActivity Methods

    @Override
    public void mActivityInitialize() {

        this.mSetAppExtensions();

        this.mFindViews();

        this.mSetToolbar(getResources().getString(R.string.screentitle_sortordersort));

        this.mFieldsInitialize();

        this.mSetListeners();

        this.mInitScreen();

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
        this.toolbarSubtext = findViewById(R.id.toolbarSubtext);

        this.articleDescriptionText = findViewById(R.id.articleDescriptionText);
        this.articleDescription2Text = findViewById(R.id.articleDescription2Text);
        this.articleItemText = findViewById(R.id.articleItemText);
        this.articleBarcodeText = findViewById(R.id.articleBarcodeText);
        this.articleVendorItemText = findViewById(R.id.articleVendorItemText);

        this.sourcenoText = findViewById(R.id.sourcenoText);
        this.sourcenoContainer = findViewById(R.id.destinationContainer);
        this.textViewPackingTable = findViewById(R.id.textViewPackingTable);

        this.quantityText = findViewById(R.id.quantityText);
        this.quantityRequiredText = findViewById(R.id.quantityRequiredText);
        this.articleThumbImageView = findViewById(R.id.articleThumbImageView);
        this.imageButtonBarcode = findViewById(R.id.imageButtonBarcode);

        this.imageButtonMinus = findViewById(R.id.imageButtonMinus);
        this.imageButtonPlus = findViewById(R.id.imageButtonPlus);
        this.imageButtonDone = findViewById(R.id.imageButtonDone);
        this.textViewAction = findViewById(R.id.textViewAction);
        this.textAdviceLocation = findViewById(R.id.textAdviceLocation);

    }

    @Override
    public void mSetToolbar(String pvScreenTitleStr) {
        this.toolbarImage.setImageResource(R.drawable.ic_menu_sort);
        this.toolbarTitle.setText(pvScreenTitleStr);
        this.toolbarTitle.setSelected(true);
        this.toolbarSubtext.setSelected(true);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void mFieldsInitialize() {

        this.textAdviceLocation.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        this.textAdviceLocation.setSingleLine(true);
        this.textAdviceLocation.setMarqueeRepeatLimit(5);
        this.textAdviceLocation.setSelected(true);
        this.textAdviceLocation.setText(cAppExtension.context.getString(R.string.message_scan_article));

        this.pickCounterPlusHelperInt = 0;
        this.pickCounterMinusHelperInt = 0;
        this.toolbarSubtext.setText(cPickorder.currentPickOrder.getOrderNumberStr());

        this.articleDescriptionText.setText(cPickorderLine.currentPickOrderLine.getDescriptionStr());
        this.articleDescription2Text.setText(cPickorderLine.currentPickOrderLine.getDescription2Str());
        this.articleItemText.setText(cPickorderLine.currentPickOrderLine.getItemNoAndVariantStr());

        this.articleVendorItemText.setText(cPickorderLine.currentPickOrderLine.getVendorItemNoAndDescriptionStr());
        this.sourcenoText.setText(cPickorderLine.currentPickOrderLine.getSourceNoStr());
        if (cPickorderLine.currentPickOrderLine.getSourceNoStr().trim().isEmpty()) {
            this.sourcenoContainer.setVisibility(View.INVISIBLE);
        }

        this.quantityText.setText("0");

        this.quantityRequiredText.setText(cText.pIntToStringStr(cPickorderLine.currentPickOrderLine.getQuantityDbl().intValue()));
        this.textViewPackingTable.setText("");

        this.imageButtonDone.setVisibility(View.INVISIBLE);

        this.mEnablePlusMinusAndBarcodeSelectViews();
        this.mShowArticleImage();
        this.mSetAdviceLocation();
        this.mShowBarcodeInfo();

    }

    @Override
    public void mSetListeners() {
        this.mSetArticleImageListener();
        this.mSetImageButtonBarcodeListener();
        this.mSetPlusListener();
        this.mSetMinusListener();
        this.mSetDoneListener();
    }

    @Override
    public void mInitScreen() {

        cBarcodeScan.pRegisterBarcodeReceiver(this.getClass().getSimpleName());

        //Register scan here, so we start things off
        SortorderSortActivity.articleScannedLastBln = false;

        //No barcodeStr selected, so don't simulate scan
        if (cPickorderBarcode.currentPickorderBarcode == null) {
            return;
        }

        //Fake a scan
        if (! this.defaultBarcodeSelectedBln){
            this.pHandleScan(cBarcodeScan.pFakeScan(cPickorderBarcode.currentPickorderBarcode.getBarcodeStr()));
        }
    }

    //End Region iICSDefaultActivity Methods

    //Region Public Methods

    public void pHandleScan(cBarcodeScan pvBarcodeScan){

        cUserInterface.pCheckAndCloseOpenDialogs();
        boolean packingTableScannedBln = false;

        if (Objects.requireNonNull(cBarcodeLayout.pGetBarcodeLayoutByBarcodeObl(pvBarcodeScan.getBarcodeOriginalStr())).size() > 1) {
            if (cRegex.pHasPrefix(pvBarcodeScan.barcodeOriginalStr)) {
                packingTableScannedBln = true;
            }
        }

        if (!packingTableScannedBln && cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(), cBarcodeLayout.barcodeLayoutEnu.ARTICLE)) {
            this.mHandleArticleScanned(pvBarcodeScan);
            return;
        }

        this.mHandleSalesOrderOrPackingTableBinScanned(pvBarcodeScan);
    }

    public  void pAcceptPick() {
        cPickorderLine.currentPickOrderLine.pHandledIndatabase();
        this.mSortDoneForNow();
    }

    public void pCancelPick() {
        cPickorderLine.currentPickOrderLine.quantityHandledDbl = (double) 0;
        cPickorderLine.currentPickOrderLine.pCancelIndatabase();
        this.mGoBackToLinesActivity();
    }

    //End Region Public Methods

    //Region Private Methods

    private  void mHandleArticleScanned(cBarcodeScan pvBarcodeScan) {


        if (cPickorderLine.currentPickOrderLine.getQuantityHandledDbl().equals(cPickorderLine.currentPickOrderLine.getQuantityDbl())) {
            //You have to scan a pickcart or salesorder after the last article scan
            cUserInterface.pDoExplodingScreen(cAppExtension.context.getString(R.string.message_scan_packinglocation_or_salesorder), pvBarcodeScan.getBarcodeOriginalStr(), true, true);
            return;
        }

        //We didn't scan an article yet, so handle it as a "normal" scan
        if (!articleScannedLastBln) {

            if (!this.mFindBarcodeInLineBarcodes(pvBarcodeScan)) {
                cUserInterface.pDoExplodingScreen(cAppExtension.context.getString(R.string.error_unknown_barcode), pvBarcodeScan.getBarcodeOriginalStr(), true, true);
                return;
            }

            this.imageButtonDone.setVisibility(View.INVISIBLE);

            //Succesfull article scanned
            articleScannedLastBln = true;

            //If we found the barcodeStr, currentbarcode is alreay filled, so make this selected
            this.mBarcodeSelected(cPickorderBarcode.currentPickorderBarcode);
            return;
        }

        //We last scanned an article, but thats oke
        if (!cPickorder.currentPickOrder.isPickPickPVVKOEachPieceBln()) {
            this.pHandleScan(pvBarcodeScan);
            return;
        }

        //You have to scan a pickcart or salesorder after the last article scan
        cUserInterface.pDoExplodingScreen(cAppExtension.context.getString(R.string.message_scan_packinglocation_or_salesorder), pvBarcodeScan.getBarcodeOriginalStr(), true, true);

    }

    private  boolean mCheckPackingTableAndSourceNoBln(String pvBarcodeStr) {


        if (cPickorder.currentPickOrder.salesOrderPackingTableObl() == null || cPickorder.currentPickOrder.salesOrderPackingTableObl().size() == 0) {
            return true;
        }

        //Record for Current Sales order
        cSalesOrderPackingTable recordForSalesOrder = null;

        //Record for Scanned Barcode
        cSalesOrderPackingTable recordForBarcode = null;

        for (cSalesOrderPackingTable loopRecord : cPickorder.currentPickOrder.salesOrderPackingTableObl()) {

            if (loopRecord.getSalesorderStr().equalsIgnoreCase(cPickorderLine.currentPickOrderLine.getSourceNoStr()))
                recordForSalesOrder = loopRecord;

            if (loopRecord.getPackingtableStr().equalsIgnoreCase(pvBarcodeStr)) {
                recordForBarcode = loopRecord;
            }
        }

        //Could not find both, so everything is fine
        if (recordForBarcode == null && recordForSalesOrder == null) {
            return true;
        }


        //We found a record for this barcodeStr
        if (recordForBarcode != null) {

            if (!recordForBarcode.getSalesorderStr().equalsIgnoreCase(cPickorderLine.currentPickOrderLine.getSourceNoStr())) {
                cUserInterface.pDoExplodingScreen(cAppExtension.context.getString(R.string.message_location_already_assigned), "", true, true);
                return false;
            }
        }

        //We found a record for this salesorder
        if (recordForSalesOrder != null) {

            if (!recordForSalesOrder.getPackingtableStr().equalsIgnoreCase(pvBarcodeStr)) {
                cUserInterface.pDoExplodingScreen(cAppExtension.context.getString(R.string.message_wrong_pickcartbox), recordForSalesOrder.getPackingtableStr(), true, true);
                return false;
            }
        }

        return true;
    }

    private  boolean mAddSalesOrderPackingTableBln() {

        cSalesOrderPackingTable salesOrderPackingTable = new cSalesOrderPackingTable(cPickorderLine.currentPickOrderLine.getSourceNoStr(), cPickorderLine.currentPickOrderLine.getProcessingSequenceStr());

        //If there are no known salesOrderPackingTables then initiaite so we can add later
        if (cPickorder.currentPickOrder.salesOrderPackingTableObl() == null || cPickorder.currentPickOrder.salesOrderPackingTableObl().size() == 0) {
            cSalesOrderPackingTable.allSalesOrderPackingTabelsObl = new ArrayList<>();
        }

        //if salesOrderPackingTable already exists, then we are done
        if (cPickorder.currentPickOrder.salesOrderPackingTableObl().contains(salesOrderPackingTable)) {
            return true;
        }

        // Does not exist, so pInsertInDatabase in database
        salesOrderPackingTable.pInsertInDatabaseBln();
        return true;

    }

    private  void mHandleSalesOrderOrPackingTableBinScanned(cBarcodeScan pvBarcodeScan) {

        // Check if article is already scanned
        if (!SortorderSortActivity.articleScannedLastBln) {
            // we've scanned a pickCart or a salesOrder, but we need an article
            cUserInterface.pDoExplodingScreen(cAppExtension.context.getString(R.string.message_scan_article_first), pvBarcodeScan.getBarcodeOriginalStr(), true, true);
            return;
        }

        this.imageButtonDone.setVisibility(View.VISIBLE);

        //Strip barcodeStr from regex
        String barcodeWithoutPrefixStr = cRegex.pStripRegexPrefixStr(pvBarcodeScan.getBarcodeOriginalStr());
        boolean adviceMatchedBln = false;

        //We have advice(s) and it is mandatory, so check if we have a correct scan
        if (this.sortingAdviceObl.size() > 0 && cSetting.PICK_SORT_LOCATION_ADVICE_MANDATORY()) {

            for (String adviceStr : this.sortingAdviceObl) {
                if (adviceStr.equalsIgnoreCase(barcodeWithoutPrefixStr)) {
                    //We have a match, so we are done
                    adviceMatchedBln = true;
                    break;
                }
            }
        }

        //We don't have a match check differently
        if (!adviceMatchedBln) {
            //Check if scanned barcodeStr is a SalesOrder or PickCartBox
            boolean isSalesOrderBln = cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(), cBarcodeLayout.barcodeLayoutEnu.SALESORDER);
            boolean isPackingTableBinBln = cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(), cBarcodeLayout.barcodeLayoutEnu.PACKINGTABLEBIN);

            //If we scanned a salesorder, then check if it matches the SourceNo
            if (isSalesOrderBln) {
                //If scanned value doesn't match then we are done
                if (!barcodeWithoutPrefixStr.equalsIgnoreCase(cPickorderLine.currentPickOrderLine.getSourceNoStr())) {
                    cUserInterface.pDoExplodingScreen(cAppExtension.context.getString(R.string.message_wrong_sourceno), barcodeWithoutPrefixStr, true, true);
                    return;
                }
            }

            //If we scanned a pickcartbox, then check if it matches the ProcessingSequence if ProcessingSequence is not empty
            if (isPackingTableBinBln && !cPickorderLine.currentPickOrderLine.pGetPackingTableForSourceNoStr().isEmpty()) {
                //If scanned value doesn't match then we are done
                if (!barcodeWithoutPrefixStr.equalsIgnoreCase(cPickorderLine.currentPickOrderLine.pGetPackingTableForSourceNoStr())) {
                    cUserInterface.pDoExplodingScreen(cAppExtension.context.getString(R.string.message_wrong_pickcartbox), barcodeWithoutPrefixStr, true, true);
                    return;
                }
            }

            // There are  previous salesorder/pickcaerbox scans, so check if input is correct
            if (!this.mCheckPackingTableAndSourceNoBln(barcodeWithoutPrefixStr)) {
                return;
            }
        }

        //Set the Processing Sequence
        cPickorderLine.currentPickOrderLine.processingSequenceStr = barcodeWithoutPrefixStr;

        //try to add SalesOrderPackingtable to database
        if (!this.mAddSalesOrderPackingTableBln()) {
            cUserInterface.pDoExplodingScreen(cAppExtension.context.getString(R.string.error_inserting_salesorderpackingtable), barcodeWithoutPrefixStr, true, true);
            cPickorderLine.currentPickOrderLine.processingSequenceStr = "";
            return;
        }

        //try to update ProcessingSequence in database
        if (!cPickorderLine.currentPickOrderLine.pUpdateProcessingSequenceBln(barcodeWithoutPrefixStr)) {
            cUserInterface.pDoExplodingScreen(cAppExtension.context.getString(R.string.error_updating_processing_sequence), barcodeWithoutPrefixStr, true, true);
            return;
        }

        //Set the ProcessingSequence
        cPickorderLine.currentPickOrderLine.processingSequenceStr = barcodeWithoutPrefixStr;
        this.sourcenoText.setText(cPickorderLine.currentPickOrderLine.processingSequenceStr);

        // If this is VKO after each piece, then show new instructions
        if (cPickorder.currentPickOrder.isPickPickPVVKOEachPieceBln()) {
            this.textViewAction.setText(cAppExtension.context.getString(R.string.message_scan_article));
            SortorderSortActivity.articleScannedLastBln = false;
        }


        //Check if we complete handled this line, if so then handled else only update it
        if (cPickorderLine.currentPickOrderLine.getQuantityHandledDbl().equals(cPickorderLine.currentPickOrderLine.getQuantityDbl())) {
            //Update orderline info (quantityDbl, timestamp, localStatusInt)
            cPickorderLine.currentPickOrderLine.pHandledIndatabase();
        } else {
            cPickorderLine.currentPickOrderLine.pUpdateSortLineIndatabaseBln();
        }

        //Check if line is done
        this.mCheckLineDone();

    }

    //End Region Private Methods

    @SuppressLint("ClickableViewAccessibility")
    private void mSetPlusListener() {

        this.imageButtonPlus.setOnTouchListener(new View.OnTouchListener() {

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
                    pickCounterPlusHelperInt = 0;
                }
                return false;
            }
        });

        this.imageButtonPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTryToChangeSortedQuantity(true, false,  cPickorderBarcode.currentPickorderBarcode.getQuantityPerUnitOfMeasureDbl());
            }
        });

    }

    private void mSetNumberListener() {
        this.quantityText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNumberClicked();
            }
        });
        this.quantityRequiredText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNumberClicked();
            }
        });
    }

    private void mNumberClicked() {

        if (cSetting.PICK_PER_SCAN()) {
            return;
        }

        if (cPickorderBarcode.currentPickorderBarcode == null) {
            cUserInterface.pDoNope(quantityText, false, false);
            cUserInterface.pShowSnackbarMessage(sourcenoContainer, getString(R.string.choose_barcode_first), null, false);
            return;
        }

        if (cPickorderBarcode.currentPickorderBarcode.getQuantityHandledDbl() > 1) {
            cUserInterface.pDoNope(quantityText, true, true);
            cUserInterface.pShowSnackbarMessage(sourcenoContainer, getString(R.string.manual_input_only_barcodenumber_bigger1), null, false);
            return;
        }

        this.mShowNumberPickerFragment();
    }

    private void mShowNumberPickerFragment() {

        cUserInterface.pCheckAndCloseOpenDialogs();

        Bundle bundle = new Bundle();
        bundle.putInt(cPublicDefinitions.NUMBERINTENT_CURRENTQUANTITY, cPickorderLinePackAndShip.currentPickorderLinePackAndShip.getQuantityCheckedDbl().intValue());
        bundle.putDouble(cPublicDefinitions.NUMBERINTENT_MAXQUANTITY, cPickorderLinePackAndShip.currentPickorderLinePackAndShip.getQuantityDbl().intValue());

        NumberpickerFragment numberpickerFragment = new NumberpickerFragment();
        numberpickerFragment.setArguments(bundle);

        numberpickerFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.NUMBERPICKERFRAGMENT_TAG);
    }


    @SuppressLint("ClickableViewAccessibility")
    private void mSetMinusListener() {

        this.imageButtonMinus.setOnTouchListener(new View.OnTouchListener() {
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
                    pickCounterMinusHelperInt = 0;
                }
                return false;
            }

        });

        this.imageButtonMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageButtonDone.setImageResource(R.drawable.ic_check_black_24dp);
                mTryToChangeSortedQuantity(false, false,  cPickorderBarcode.currentPickorderBarcode.getQuantityPerUnitOfMeasureDbl());
            }
        });
    }

    private void mSetArticleImageListener() {
        this.articleThumbImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mShowFullArticleFragment();
            }
        });
    }

    private void mSetImageButtonBarcodeListener() {
        this.imageButtonBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View pvView) {

                if (cPickorderLine.currentPickOrderLine.barcodesObl == null || cPickorderLine.currentPickOrderLine.barcodesObl.size() == 0) {
                    return;
                }

                mEnablePlusMinusAndBarcodeSelectViews();

                //If we only have one barcodeStr, then automatticaly select that barcodeStr
                if (cPickorderLine.currentPickOrderLine.barcodesObl.size() == 1) {
                   pHandleScan(cBarcodeScan.pFakeScan(cPickorderBarcode.currentPickorderBarcode.getBarcodeStr()));
                    return;
                }

                mShowBarcodeSelectFragment();

            }
        });
    }

    private void mSetDoneListener() {
        this.imageButtonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (cPickorderLine.currentPickOrderLine.getQuantityHandledDbl().equals(cPickorderLine.currentPickOrderLine.getQuantityDbl())) {
                    mSortDone();
                } else {
                    mSortDoneForNow();
                }
            }
        });
    }

    private  void mSetAdviceLocation() {

        String hulpStr;

        hulpStr = cPickorderLine.currentPickOrderLine.pGetPackingTableForSourceNoStr();

        if (!hulpStr.isEmpty()) {
            cPickorderLine.currentPickOrderLine.processingSequenceStr = hulpStr;
            this.textAdviceLocation.setText(hulpStr);
            this.sortingAdviceObl = new ArrayList<>();
            return;
        }

        this.sortingAdviceObl = cPickorderLine.currentPickOrderLine.pGetAdvicedSortLocationsFromWebserviceObl();

        if ( this.sortingAdviceObl == null ||  this.sortingAdviceObl.size() == 0) {
            this.textAdviceLocation.setText(cAppExtension.activity.getString(R.string.message_no_sorting_location_adviced));
            return;
        }

        StringBuilder advicelocations = new StringBuilder();

        for (String advicedLocationStr:this.sortingAdviceObl) {
            advicelocations.append(" ").append(advicedLocationStr);
        }

        this.textAdviceLocation.setText(advicelocations.toString());

    }

    private  void mSortDone() {
        this.mSendLine();
        this.mGoBackToLinesActivity();
    }

    private void mSortDoneForNow() {

        if (!cPickorderLine.currentPickOrderLine.pUpdateSortLineIndatabaseBln()) {
            //could not send line, let user know but answer succes so user can go to next line
            cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.couldnt_update_line), null);
            return;
        }

        this.mGoBackToLinesActivity();
    }

    private  void mGoBackToLinesActivity() {
        Intent intent = new Intent(cAppExtension.context, SortorderLinesActivity.class);
        cAppExtension.activity.startActivity(intent);
        cAppExtension.activity.finish();
    }

    public void mHandleQuantityChosen(int pvQuantityDbl) {

        if (pvQuantityDbl == 0) {
            this.mTryToChangeSortedQuantity(false, true,pvQuantityDbl);
        } else {
            this.mTryToChangeSortedQuantity(true, true,pvQuantityDbl);
        }
    }

    private  void mSendLine() {

        if (!cPickorderLine.currentPickOrderLine.pSortedBln()) {
            //could not send line, let user know but answer succes so user can go to next line
            cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.couldnt_send_line), null);
            cPickorderLine.currentPickOrderLine.pErrorSending();
        }

    }

    private  void mTryToChangeSortedQuantity(Boolean pvIsPositiveBln, Boolean pvAmountFixedBln, double pvAmountDbl) {

        double newQuantityDbl;

        if (pvIsPositiveBln) {

            //Determine the new amount
            if (pvAmountFixedBln) {
                newQuantityDbl = pvAmountDbl;
            } else {
                newQuantityDbl = cPickorderLine.currentPickOrderLine.getQuantityHandledDbl() + pvAmountDbl;
            }

            //Check if we would exceed amount, then show message
            if (newQuantityDbl > cPickorderLine.currentPickOrderLine.getQuantityDbl()) {
                this.mShowOverpickNotAllowed();
                articleScannedLastBln = false;
                return;
            }

            //Set the new quantityDbl and show in Activity
            cPickorderLine.currentPickOrderLine.quantityHandledDbl = newQuantityDbl;
            this.quantityText.setText(cText.pDoubleToStringStr(cPickorderLine.currentPickOrderLine.getQuantityHandledDbl()));

            //Add or update line barcodeStr
            cPickorderLine.currentPickOrderLine.pAddOrUpdateLineBarcode(pvAmountDbl);

            //Update orderline info (quantityDbl, timestamp, localStatusInt)
            cPickorderLine.currentPickOrderLine.pUpdateSortLineIndatabaseBln();

            if (cPickorder.currentPickOrder.isPickPickPVVKOEachPieceBln() ||
                cPickorderLine.currentPickOrderLine.getQuantityHandledDbl().equals(cPickorderLine.currentPickOrderLine.getQuantityDbl())) {
                this.textViewAction.setText(cAppExtension.context.getString(R.string.scan_sort_location));
            }
            else {
                this.textViewAction.setText(cAppExtension.context.getString(R.string.scan_article_or_sort_location));
            }

            return;

        }

        //negative
        if (cPickorderLine.currentPickOrderLine.quantityHandledDbl == 0 ) {
            cUserInterface.pDoNope(quantityText, true, true);
            return;
        }

        //Determine the new amount
        if (pvAmountFixedBln) {
            newQuantityDbl = pvAmountDbl;
        }else {
            newQuantityDbl= cPickorderLine.currentPickOrderLine.getQuantityHandledDbl() - pvAmountDbl;
        }

        if (newQuantityDbl <= 0) {
            cPickorderLine.currentPickOrderLine.quantityHandledDbl = (double) 0;
        }else {
            //Set the new quantityDbl and show in Activity
            cPickorderLine.currentPickOrderLine.quantityHandledDbl = newQuantityDbl;
        }

        this.quantityText.setText(cText.pDoubleToStringStr(cPickorderLine.currentPickOrderLine.getQuantityHandledDbl()));
        this.imageButtonDone.setImageResource(R.drawable.ic_check_black_24dp);

        if (cPickorder.currentPickOrder.isPickPickPVVKOEachPieceBln()) {
            this.textViewAction.setText(cAppExtension.context.getString(R.string.scan_article_or_sort_location));
        }
        else {
            this.textViewAction.setText(cAppExtension.context.getString(R.string.scan_sort_location));
        }

        //Remove or update line barcodeStr
        cPickorderLine.currentPickOrderLine.pRemoveOrUpdateLineBarcode();

    }

    private void mShowArticleImage() {

        //If pick with picture is false, then hide image view
        if (!cPickorder.currentPickOrder.isPickWithPictureBln()) {
            this.articleThumbImageView.setVisibility(View.INVISIBLE);
            return;
        }

        //If picture is not in cache (via webservice) then show no image
        if (!cPickorderLine.currentPickOrderLine.pGetArticleImageBln()) {
            cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.could_not_get_article_image), null);
            this.articleThumbImageView.setImageDrawable(ContextCompat.getDrawable(cAppExtension.context, R.drawable.ic_no_image_lightgrey_24dp));
            return;
        }

        //If picture is in cache but can't be converted, then show no image
        if (cPickorderLine.currentPickOrderLine.articleImage == null || cPickorderLine.currentPickOrderLine.articleImage.imageBitmap() == null) {
            cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.could_not_get_article_image), null);
            this.articleThumbImageView.setImageDrawable(ContextCompat.getDrawable(cAppExtension.context, R.drawable.ic_no_image_lightgrey_24dp));
            return;
        }

        //Show the image
        this.articleThumbImageView.setImageBitmap(cPickorderLine.currentPickOrderLine.articleImage.imageBitmap());
    }

    private  void mShowFullArticleFragment() {

        cUserInterface.pCheckAndCloseOpenDialogs();

        ArticleFullViewFragment articleFullViewFragment = new ArticleFullViewFragment();
        articleFullViewFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.ARTICLEFULL_TAG);
    }

    private void mShowAcceptFragment(){

        cUserInterface.pCheckAndCloseOpenDialogs();

        final AcceptRejectFragment acceptRejectFragment = new AcceptRejectFragment(cAppExtension.activity.getString(R.string.message_orderbusy_header),
                                                                                   cAppExtension.activity.getString(R.string.message_orderbusy_text),cAppExtension.activity.getString(R.string.message_cancel_line), cAppExtension.activity.getString(R.string.message_accept_line), false);
        acceptRejectFragment.setCancelable(true);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // show my popup
                acceptRejectFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.ACCEPTREJECTFRAGMENT_TAG);
            }
        });
    }

    public  void mBarcodeSelected(cPickorderBarcode pvBarcode) {

        cUserInterface.pCheckAndCloseOpenDialogs();

        cPickorderBarcode.currentPickorderBarcode = pvBarcode;
        this.mShowBarcodeInfo();
        this.mTryToChangeSortedQuantity(true, false, cPickorderBarcode.currentPickorderBarcode.getQuantityPerUnitOfMeasureDbl());
    }

    private  void mShowBarcodeInfo() {

        if (cPickorderBarcode.currentPickorderBarcode == null) {
            if (cPickorderLine.currentPickOrderLine.barcodesObl.size() == 1) {
                cPickorderBarcode.currentPickorderBarcode = cPickorderLine.currentPickOrderLine.barcodesObl.get(0);
                this.defaultBarcodeSelectedBln = true;
            }
        }

        if (cPickorderBarcode.currentPickorderBarcode != null) {
            this.articleBarcodeText.setText(cPickorderBarcode.currentPickorderBarcode.getBarcodeAndQuantityStr());
        } else {
            this.articleBarcodeText.setText(cAppExtension.context.getString(R.string.mutiple_barcodes_posible));
        }
    }

    private void mShowBarcodeSelectFragment() {
        BarcodeFragment barcodeFragment = new BarcodeFragment();
        barcodeFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.BARCODEPICKERFRAGMENT_TAG);
    }

    private  void mEnablePlusMinusAndBarcodeSelectViews() {

        if (cSetting.PICK_PER_SCAN()) {
            this.imageButtonMinus.setVisibility(View.INVISIBLE);
            this.imageButtonPlus.setVisibility(View.INVISIBLE);
        } else {
            this.imageButtonMinus.setVisibility(View.VISIBLE);
            this.imageButtonPlus.setVisibility(View.VISIBLE);
        }

        if (!cSetting.PICK_SELECTEREN_BARCODE()) {
            this.imageButtonBarcode.setVisibility(View.INVISIBLE);
        } else {
            this.imageButtonBarcode.setVisibility(View.VISIBLE);
        }
    }

    private  void mCheckLineDone() {

        //We scanned a sortlocation
        SortorderSortActivity.articleScannedLastBln = false;

        //If we didn't complete this line, then show done for now
        if (cPickorderLine.currentPickOrderLine.quantityHandledDbl < cPickorderLine.currentPickOrderLine.getQuantityDbl()) {
            this.imageButtonDone.setImageResource(R.drawable.ic_check_black_24dp);
            this.imageButtonDone.setVisibility(View.VISIBLE);

            // If auto accept is false or  if we have to scan VKO after each piece , then return
            if (!cSetting.PICK_AUTO_ACCEPT() || cPickorder.currentPickOrder.isPickPickPVVKOEachPieceBln() ) {
                return;
            }

            // We are done for now
            this.mSortDoneForNow();
            return;
        }

        //We didn complete this line, then show done
        if (!cSetting.PICK_AUTO_ACCEPT()) {
            this.imageButtonDone.setImageResource(R.drawable.ic_doublecheck_black_24dp);
            this.imageButtonDone.setVisibility(View.VISIBLE);
            return;
        }

        // We are completely done
        this.mSortDone();
    }

    private BroadcastReceiver mNumberReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context pvContext, Intent pvIntent) {
            int numberChosenInt = 0;
            Bundle extras = pvIntent.getExtras();

            if (extras != null) {
                numberChosenInt = extras.getInt(cPublicDefinitions.NUMBERINTENT_EXTRANUMBER);
            }

            mHandleQuantityChosen(numberChosenInt);
        }
    };

    //Region Number Broadcaster
    Runnable plusAction = new Runnable() {
        @Override public void run() {
            imageButtonPlus.performClick();
            long milliSecsLng;
            if (pickCounterPlusHelperInt < 10) {
                milliSecsLng = 200;
            }
            else if (pickCounterPlusHelperInt < 20) {
                milliSecsLng = 150;
            }
            else if (pickCounterPlusHelperInt < 30) {
                milliSecsLng = 100;
            }
            else if (pickCounterPlusHelperInt < 40) {
                milliSecsLng = 50;
            }
            else {
                milliSecsLng = 50;
            }
            mDoDelayedPlus(this,milliSecsLng);
        }
    };

    Runnable minusAction = new Runnable() {
        @Override public void run() {
            imageButtonMinus.performClick();
            long milliSecsLng;
            if (pickCounterMinusHelperInt < 10) {
                milliSecsLng = 200;
            }
            else if (pickCounterMinusHelperInt < 20) {
                milliSecsLng = 150;
            }
            else if (pickCounterMinusHelperInt < 30) {
                milliSecsLng = 100;
            }
            else if (pickCounterMinusHelperInt < 40) {
                milliSecsLng = 50;
            }
            else {
                milliSecsLng = 50;
            }
            mDoDelayedMinus(this, milliSecsLng);
        }
    };

    private void mDoDelayedMinus(Runnable pvRunnable, long milliSecsLng) {
        this.minusHandler.postDelayed(pvRunnable, milliSecsLng);
        this.pickCounterMinusHelperInt += 1;
    }

    private void mDoDelayedPlus(Runnable pvRunnable, long milliSecsLng) {
        this.plusHandler.postDelayed(pvRunnable, milliSecsLng);
        this.pickCounterPlusHelperInt += 1;
    }

    private  void mShowOverpickNotAllowed(){
        this.quantityText.setText(this.quantityRequiredText.getText());
        cUserInterface.pShowSnackbarMessage(this.textViewAction , cAppExtension.context.getString(R.string.number_cannot_be_higher), null, false);
        cUserInterface.pDoNope(this.quantityText, true, true);
        cUserInterface.pDoNope(this.quantityRequiredText, false, false);
    }

    private  Boolean mFindBarcodeInLineBarcodes(cBarcodeScan pvBarcodeScan) {

        if (cPickorderLine.currentPickOrderLine.barcodesObl == null || cPickorderLine.currentPickOrderLine.barcodesObl.size() == 0) {
            return false;
        }

        for (cPickorderBarcode pickorderBarcode : cPickorderLine.currentPickOrderLine.barcodesObl) {

            if (pickorderBarcode.getBarcodeStr().equalsIgnoreCase(pvBarcodeScan.getBarcodeOriginalStr()) ||
                pickorderBarcode.getBarcodeWithoutCheckDigitStr().equalsIgnoreCase(pvBarcodeScan.getBarcodeFormattedStr())) {
                cPickorderBarcode.currentPickorderBarcode = pickorderBarcode;
                return true;
            }
        }

        return false;
    }


    //End Region Number Broadcaster


}
