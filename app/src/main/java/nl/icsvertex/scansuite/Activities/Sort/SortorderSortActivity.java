package nl.icsvertex.scansuite.Activities.Sort;


import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;

import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.cardview.widget.CardView;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ICS.Interfaces.iICSDefaultActivity;
import ICS.Utils.Scanning.cBarcodeScan;
import SSU_WHS.Basics.BarcodeLayouts.cBarcodeLayout;
import SSU_WHS.Basics.Settings.cSetting;

import SSU_WHS.Picken.PickorderBarcodes.cPickorderBarcode;
import SSU_WHS.Picken.PickorderLines.cPickorderLine;
import SSU_WHS.Picken.Pickorders.cPickorder;
import SSU_WHS.Picken.SalesOrderPackingTable.cSalesOrderPackingTable;
import SSU_WHS.General.cPublicDefinitions;
import ICS.Utils.cRegex;
import ICS.Utils.cText;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import nl.icsvertex.scansuite.Fragments.Dialogs.AcceptRejectFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.ArticleFullViewFragment;
import nl.icsvertex.scansuite.R;
import nl.icsvertex.scansuite.Fragments.Dialogs.BarcodeFragment;

import static android.view.View.GONE;

public class SortorderSortActivity extends AppCompatActivity implements iICSDefaultActivity {

    //Region Public Properties

    //End Region Public Properties

    //Region Private Properties

    static final String ACCEPTREJECTFRAGMENT_TAG = "ACCEPTREJECTFRAGMENT_TAG";
    static final String BARCODEPICKERFRAGMENT_TAG = "BARCODEPICKERFRAGMENT_TAG";

    private static Boolean articleScannedLastBln;
    private static List<String> sortingAdviceObl;

    private static int pickCounterMinusHelperInt;
    private static int pickCounterPlusHelperInt;

    private static Handler minusHandler;
    private static Handler plusHandler;

    //Region Views

    private static ImageView toolbarImage;
    private static TextView toolbarTitle;
    private static TextView toolbarSubtext;

    private static TextView articleDescriptionText;
    private static TextView articleDescription2Text;
    private static TextView articleItemText;
    private static TextView articleBarcodeText;
    private static TextView articleVendorItemText;
    private static TextView containerText;
    private static TextView quantityText;
    private static TextView quantityRequiredText;
    private static  ImageView articleThumbImageView;
    private static ImageView imageButtonBarcode;
    private static TextView sourcenoText;
    private static CardView sourcenoContainer;
    private static TextView textViewPackingTable;
    private static TextView textViewAction;
    private static TextView textAdviceLocation;

    private static AppCompatImageButton imageButtonMinus;
    private static AppCompatImageButton imageButtonPlus;
    private static AppCompatImageButton imageButtonDone;

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
            cBarcodeScan.pUnregisterBarcodeReceiver();
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
        cBarcodeScan.pRegisterBarcodeReceiver();
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
        mShowAcceptFragment();
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

        SortorderSortActivity.toolbarImage = findViewById(R.id.toolbarImage);
        SortorderSortActivity.toolbarTitle = findViewById(R.id.toolbarTitle);
        SortorderSortActivity.toolbarSubtext = findViewById(R.id.toolbarSubtext);

        SortorderSortActivity.articleDescriptionText = findViewById(R.id.articleDescriptionText);
        SortorderSortActivity.articleDescription2Text = findViewById(R.id.articleDescription2Text);
        SortorderSortActivity.articleItemText = findViewById(R.id.articleItemText);
        SortorderSortActivity.articleBarcodeText = findViewById(R.id.articleBarcodeText);
        SortorderSortActivity.articleVendorItemText = findViewById(R.id.articleVendorItemText);

        SortorderSortActivity.sourcenoText = findViewById(R.id.sourcenoText);
        SortorderSortActivity.sourcenoContainer = findViewById(R.id.destinationContainer);
        SortorderSortActivity.textViewPackingTable = findViewById(R.id.textViewPackingTable);

        SortorderSortActivity.containerText = findViewById(R.id.containerText);
        SortorderSortActivity.quantityText = findViewById(R.id.quantityText);
        SortorderSortActivity.quantityRequiredText = findViewById(R.id.quantityRequiredText);
        SortorderSortActivity.articleThumbImageView = findViewById(R.id.articleThumbImageView);
        SortorderSortActivity.imageButtonBarcode = findViewById(R.id.imageButtonBarcode);

        SortorderSortActivity.imageButtonMinus = findViewById(R.id.imageButtonMinus);
        SortorderSortActivity.imageButtonPlus = findViewById(R.id.imageButtonPlus);
        SortorderSortActivity.imageButtonDone = findViewById(R.id.imageButtonDone);
        SortorderSortActivity.textViewAction = findViewById(R.id.textViewAction);
        SortorderSortActivity.textAdviceLocation = findViewById(R.id.textAdviceLocation);

    }



    @Override
    public void mSetToolbar(String pvScreenTitleStr) {
        SortorderSortActivity.toolbarImage.setImageResource(R.drawable.ic_menu_sort);
        SortorderSortActivity.toolbarTitle.setText(pvScreenTitleStr);
        SortorderSortActivity.toolbarTitle.setSelected(true);
        SortorderSortActivity.toolbarSubtext.setSelected(true);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void mFieldsInitialize() {

        SortorderSortActivity.textAdviceLocation.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        SortorderSortActivity.textAdviceLocation.setSingleLine(true);
        SortorderSortActivity.textAdviceLocation.setMarqueeRepeatLimit(5);
        SortorderSortActivity.textAdviceLocation.setSelected(true);
        SortorderSortActivity.textAdviceLocation.setText(cAppExtension.context.getString(R.string.message_scan_article));

        SortorderSortActivity.pickCounterPlusHelperInt = 0;
        SortorderSortActivity.pickCounterMinusHelperInt = 0;
        SortorderSortActivity.toolbarSubtext.setText(cPickorder.currentPickOrder.getOrderNumberStr());
        SortorderSortActivity.containerText.setVisibility(GONE);

        SortorderSortActivity.articleDescriptionText.setText(cPickorderLine.currentPickOrderLine.getDescriptionStr());
        SortorderSortActivity.articleDescription2Text.setText(cPickorderLine.currentPickOrderLine.getDescription2Str());
        SortorderSortActivity.articleItemText.setText(cPickorderLine.currentPickOrderLine.getItemNoAndVariantStr());

        SortorderSortActivity.articleVendorItemText.setText(cPickorderLine.currentPickOrderLine.getVendorItemNoAndDescriptionStr());
        SortorderSortActivity.sourcenoText.setText(cPickorderLine.currentPickOrderLine.getSourceNoStr());
        if (cPickorderLine.currentPickOrderLine.getSourceNoStr().trim().isEmpty()) {
            SortorderSortActivity.sourcenoContainer.setVisibility(View.INVISIBLE);
        }

        SortorderSortActivity.containerText.setText(cPickorderLine.currentPickOrderLine.getContainerStr());
        SortorderSortActivity.containerText.setText("");
        SortorderSortActivity.quantityText.setText("0");

        SortorderSortActivity.quantityRequiredText.setText(cText.pIntToStringStr(cPickorderLine.currentPickOrderLine.getQuantityDbl().intValue()));
        SortorderSortActivity.textViewPackingTable.setText("");

        SortorderSortActivity.imageButtonDone.setVisibility(View.INVISIBLE);

        SortorderSortActivity.mEnablePlusMinusAndBarcodeSelectViews();
        SortorderSortActivity.mShowArticleImage();
        SortorderSortActivity.mSetAdviceLocation();
        SortorderSortActivity.mShowBarcodeInfo();

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

        cBarcodeScan.pRegisterBarcodeReceiver();

        //No barcode selected, so don't simulate scan
        if (cPickorderBarcode.currentPickorderBarcode == null) {
            return;
        }

        //Register scan here, so we start things off
        SortorderSortActivity.articleScannedLastBln = false;

        //Fake a scan
        SortorderSortActivity.pHandleScan(cBarcodeScan.pFakeScan(cPickorderBarcode.currentPickorderBarcode.getBarcodeStr()));
    }

    //End Region iICSDefaultActivity Methods

    //Region Public Methods

    public static void pHandleScan(cBarcodeScan pvBarcodeScan){

        cUserInterface.pCheckAndCloseOpenDialogs();

        if (cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(), cBarcodeLayout.barcodeLayoutEnu.ARTICLE)) {
            SortorderSortActivity.mHandleArticleScanned(pvBarcodeScan);
            return;
        }

        SortorderSortActivity.mHandleSalesOrderOrPackingTableBinScanned(pvBarcodeScan);
    }

    public static void pAcceptPick() {
        cPickorderLine.currentPickOrderLine.pHandledIndatabaseBln();
        SortorderSortActivity.mSortDoneForNow();
    }

    public static void pCancelPick() {
        cPickorderLine.currentPickOrderLine.quantityHandledDbl = (double) 0;
        cPickorderLine.currentPickOrderLine.pCancelIndatabaseBln();
        SortorderSortActivity.mGoBackToLinesActivity();
    }

    //End Region Public Methods

    //Region Private Methods

    private static void mHandleArticleScanned(cBarcodeScan pvBarcodeScan) {


        if (cPickorderLine.currentPickOrderLine.getQuantityHandledDbl().equals(cPickorderLine.currentPickOrderLine.getQuantityDbl())) {
            //You have to scan a pickcart or salesorder after the last article scan
            cUserInterface.pDoExplodingScreen(cAppExtension.context.getString(R.string.message_scan_packinglocation_or_salesorder), pvBarcodeScan.getBarcodeOriginalStr(), true, true);
            return;
        }

        //We didn't scan an article yet, so handle it as a "normal" scan
        if (!articleScannedLastBln) {

            if (!SortorderSortActivity.mFindBarcodeInLineBarcodes(pvBarcodeScan)) {
                cUserInterface.pDoExplodingScreen(cAppExtension.context.getString(R.string.error_unknown_barcode), pvBarcodeScan.getBarcodeOriginalStr(), true, true);
                return;
            }

            SortorderSortActivity.imageButtonDone.setVisibility(View.INVISIBLE);

            //Succesfull article scanned
            articleScannedLastBln = true;

            //If we found the barcode, currentbarcode is alreay filled, so make this selected
            SortorderSortActivity.mBarcodeSelected(cPickorderBarcode.currentPickorderBarcode);
            return;
        }

        //We last scanned an article, but thats oke
        if (!cPickorder.currentPickOrder.isPickPickPVVKOEachPieceBln()) {
            SortorderSortActivity.pHandleScan(pvBarcodeScan);
            return;
        }

        //You have to scan a pickcart or salesorder after the last article scan
        cUserInterface.pDoExplodingScreen(cAppExtension.context.getString(R.string.message_scan_packinglocation_or_salesorder), pvBarcodeScan.getBarcodeOriginalStr(), true, true);

    }

    private static boolean mCheckPackingTableAndSourceNoBln(String pvBarcodeStr) {


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


        //We found a record for this barcode
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

    private static boolean mAddSalesOrderPackingTableBln() {

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

    private static void mHandleSalesOrderOrPackingTableBinScanned(cBarcodeScan pvBarcodeScan) {

        // Check if article is already scanned
        if (!SortorderSortActivity.articleScannedLastBln) {
            // we've scanned a pickCart or a salesOrder, but we need an article
            cUserInterface.pDoExplodingScreen(cAppExtension.context.getString(R.string.message_scan_article_first), pvBarcodeScan.getBarcodeOriginalStr(), true, true);
            return;
        }

        SortorderSortActivity.imageButtonDone.setVisibility(View.VISIBLE);

        //Strip barcode from regex
        String barcodeWithoutPrefixStr = cRegex.pStripRegexPrefixStr(pvBarcodeScan.getBarcodeOriginalStr());
        boolean adviceMatchedBln = false;

        //We have advice(s) and it is mandatory, so check if we have a correct scan
        if (SortorderSortActivity.sortingAdviceObl.size() > 0 && cSetting.PICK_SORT_LOCATION_ADVICE_MANDATORY()) {

            for (String adviceStr : SortorderSortActivity.sortingAdviceObl) {
                if (adviceStr.equalsIgnoreCase(barcodeWithoutPrefixStr)) {
                    //We have a match, so we are done
                    adviceMatchedBln = true;
                    break;
                }
            }
        }

        //We don't have a match check differently
        if (!adviceMatchedBln) {
            //Check if scanned barcode is a SalesOrder or PickCartBox
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
            if (!SortorderSortActivity.mCheckPackingTableAndSourceNoBln(barcodeWithoutPrefixStr)) {
                return;
            }
        }

        //Set the Processing Sequence
        cPickorderLine.currentPickOrderLine.processingSequenceStr = barcodeWithoutPrefixStr;

        //try to add SalesOrderPackingtable to database
        if (!SortorderSortActivity.mAddSalesOrderPackingTableBln()) {
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
        SortorderSortActivity.sourcenoText.setText(cPickorderLine.currentPickOrderLine.processingSequenceStr);

        // If this is VKO after each piece, then show new instructions
        if (cPickorder.currentPickOrder.isPickPickPVVKOEachPieceBln()) {
            SortorderSortActivity.textViewAction.setText(cAppExtension.context.getString(R.string.message_scan_article));
            SortorderSortActivity.articleScannedLastBln = false;
        }


        //Check if we complete handled this line, if so then handled else only update it
        if (cPickorderLine.currentPickOrderLine.getQuantityHandledDbl().equals(cPickorderLine.currentPickOrderLine.getQuantityDbl())) {
            //Update orderline info (quantityDbl, timestamp, localStatusInt)
            cPickorderLine.currentPickOrderLine.pHandledIndatabaseBln();
        } else {
            cPickorderLine.currentPickOrderLine.pUpdateSortLineIndatabaseBln();
        }

        //Check if line is done
        SortorderSortActivity.mCheckLineDone();

    }

    //End Region Private Methods

    @SuppressLint("ClickableViewAccessibility")
    private void mSetPlusListener() {

        SortorderSortActivity.imageButtonPlus.setOnTouchListener(new View.OnTouchListener() {

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

        SortorderSortActivity.imageButtonPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTryToChangeSortedQuantity(true, false,  cPickorderBarcode.currentPickorderBarcode.getQuantityPerUnitOfMeasureDbl());
            }
        });

    }

    @SuppressLint("ClickableViewAccessibility")
    private void mSetMinusListener() {

        SortorderSortActivity.imageButtonMinus.setOnTouchListener(new View.OnTouchListener() {
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

        SortorderSortActivity.imageButtonMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageButtonDone.setImageResource(R.drawable.ic_check_black_24dp);
                mTryToChangeSortedQuantity(false, false,  cPickorderBarcode.currentPickorderBarcode.getQuantityPerUnitOfMeasureDbl());
            }
        });
    }

    private void mSetArticleImageListener() {
        SortorderSortActivity.articleThumbImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mShowFullArticleFragment();
            }
        });
    }

    private void mSetImageButtonBarcodeListener() {
        SortorderSortActivity.imageButtonBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View pvView) {

                if (cPickorderLine.currentPickOrderLine.barcodesObl == null || cPickorderLine.currentPickOrderLine.barcodesObl.size() == 0) {
                    return;
                }

                mEnablePlusMinusAndBarcodeSelectViews();

                //If we only have one barcode, then automatticaly select that barcode
                if (cPickorderLine.currentPickOrderLine.barcodesObl.size() == 1) {
                    mBarcodeSelected(cPickorderLine.currentPickOrderLine.barcodesObl.get(0));
                    return;
                }

                mShowBarcodeSelectFragment();

            }
        });
    }

    private void mSetDoneListener() {
        imageButtonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (cPickorderLine.currentPickOrderLine.getQuantityHandledDbl().equals(cPickorderLine.currentPickOrderLine.getQuantityDbl())) {
                    SortorderSortActivity.mSortDone();
                } else {
                    SortorderSortActivity.mSortDoneForNow();
                }
            }
        });
    }

    private static void mSetAdviceLocation() {

        String hulpStr;

        hulpStr = cPickorderLine.currentPickOrderLine.pGetPackingTableForSourceNoStr();

        if (!hulpStr.isEmpty()) {
            cPickorderLine.currentPickOrderLine.processingSequenceStr = hulpStr;
            SortorderSortActivity.textAdviceLocation.setText(hulpStr);
            SortorderSortActivity.sortingAdviceObl = new ArrayList<>();
            return;
        }

        SortorderSortActivity.sortingAdviceObl = cPickorderLine.currentPickOrderLine.pGetAdvicedSortLocationsFromWebserviceObl();

        if ( SortorderSortActivity.sortingAdviceObl == null ||  SortorderSortActivity.sortingAdviceObl.size() == 0) {
            SortorderSortActivity.textAdviceLocation.setText("");
            return;
        }

        StringBuilder advicelocations = new StringBuilder();

        for (String advicedLocationStr:SortorderSortActivity.sortingAdviceObl) {
            advicelocations.append(" ").append(advicedLocationStr);
        }

        SortorderSortActivity.textAdviceLocation.setText(advicelocations.toString());

    }

    private static void mSortDone() {
        SortorderSortActivity.mSendPickorderLine();
        mGoBackToLinesActivity();
    }

    private static void mSortDoneForNow() {

        if (!cPickorderLine.currentPickOrderLine.pUpdateSortLineIndatabaseBln()) {
            //could not send line, let user know but answer succes so user can go to next line
            cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.couldnt_update_line), null);
            return;
        }


        mGoBackToLinesActivity();
    }

    private static void mGoBackToLinesActivity() {
        Intent intent = new Intent(cAppExtension.context, SortorderLinesActivity.class);
        cAppExtension.activity.startActivity(intent);
        cAppExtension.activity.finish();
    }

    public void mHandleQuantityChosen(int pvQuantityDbl) {

        if (pvQuantityDbl == 0) {
            SortorderSortActivity.mTryToChangeSortedQuantity(false, true,pvQuantityDbl);
        } else {
            SortorderSortActivity.mTryToChangeSortedQuantity(true, true,pvQuantityDbl);
        }
    }

    private static void mSendPickorderLine() {

        if (!cPickorderLine.currentPickOrderLine.pHandledBln()) {
            //could not send line, let user know but answer succes so user can go to next line
            cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.couldnt_send_line), null);
            cPickorderLine.currentPickOrderLine.pErrorSending();
        }

    }

    private static void mTryToChangeSortedQuantity(Boolean pvIsPositiveBln, Boolean pvAmountFixedBln, double pvAmountDbl) {

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
                SortorderSortActivity.mShowOverpickNotAllowed();
                articleScannedLastBln = false;
                return;
            }

            //Set the new quantityDbl and show in Activity
            cPickorderLine.currentPickOrderLine.quantityHandledDbl = newQuantityDbl;
            SortorderSortActivity.quantityText.setText(cText.pDoubleToStringStr(cPickorderLine.currentPickOrderLine.getQuantityHandledDbl()));

            //Add or update line barcode
            cPickorderLine.currentPickOrderLine.pAddOrUpdateLineBarcodeBln(pvAmountDbl);

            //Update orderline info (quantityDbl, timestamp, localStatusInt)
            cPickorderLine.currentPickOrderLine.pUpdateSortLineIndatabaseBln();

            if (cPickorder.currentPickOrder.isPickPickPVVKOEachPieceBln() ||
                cPickorderLine.currentPickOrderLine.getQuantityHandledDbl().equals(cPickorderLine.currentPickOrderLine.getQuantityDbl())) {
                SortorderSortActivity.textViewAction.setText(cAppExtension.context.getString(R.string.scan_sort_location));
            }
            else {
                SortorderSortActivity.textViewAction.setText(cAppExtension.context.getString(R.string.scan_article_or_sort_location));
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

        SortorderSortActivity.quantityText.setText(cText.pDoubleToStringStr(cPickorderLine.currentPickOrderLine.getQuantityHandledDbl()));
        SortorderSortActivity.imageButtonDone.setImageResource(R.drawable.ic_check_black_24dp);

        if (cPickorder.currentPickOrder.isPickPickPVVKOEachPieceBln()) {
            SortorderSortActivity.textViewAction.setText(cAppExtension.context.getString(R.string.scan_article_or_sort_location));
        }
        else {
            SortorderSortActivity.textViewAction.setText(cAppExtension.context.getString(R.string.scan_sort_location));
        }

        //Remove or update line barcode
        cPickorderLine.currentPickOrderLine.pRemoveOrUpdateLineBarcodeBln();

    }

    private static void mShowArticleImage() {

        //If pick with picture is false, then hide image view
        if (!cPickorder.currentPickOrder.isPickWithPictureBln()) {
            SortorderSortActivity.articleThumbImageView.setVisibility(View.INVISIBLE);
            return;
        }

        //If picture is not in cache (via webservice) then show no image
        if (!cPickorderLine.currentPickOrderLine.pGetArticleImageBln()) {
            cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.could_not_get_article_image), null);
            SortorderSortActivity.articleThumbImageView.setImageDrawable(ContextCompat.getDrawable(cAppExtension.context, R.drawable.ic_no_image_lightgrey_24dp));
            return;
        }

        //If picture is in cache but can't be converted, then show no image
        if (cPickorderLine.currentPickOrderLine.articleImage == null || cPickorderLine.currentPickOrderLine.articleImage.imageBitmap() == null) {
            cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.could_not_get_article_image), null);
            SortorderSortActivity.articleThumbImageView.setImageDrawable(ContextCompat.getDrawable(cAppExtension.context, R.drawable.ic_no_image_lightgrey_24dp));
            return;
        }

        //Show the image
        SortorderSortActivity.articleThumbImageView.setImageBitmap(cPickorderLine.currentPickOrderLine.articleImage.imageBitmap());
    }

    private static void mShowFullArticleFragment() {

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
                acceptRejectFragment.show(cAppExtension.fragmentManager, ACCEPTREJECTFRAGMENT_TAG);
            }
        });
    }

    public static void mBarcodeSelected(cPickorderBarcode pvBarcode) {

        cUserInterface.pCheckAndCloseOpenDialogs();

        cPickorderBarcode.currentPickorderBarcode = pvBarcode;
        SortorderSortActivity.mShowBarcodeInfo();
        SortorderSortActivity.mTryToChangeSortedQuantity(true, false, cPickorderBarcode.currentPickorderBarcode.getQuantityPerUnitOfMeasureDbl());
    }

    private static void mShowBarcodeInfo() {

        if (cPickorderBarcode.currentPickorderBarcode == null) {
            if (cPickorderLine.currentPickOrderLine.barcodesObl.size() == 1) {
                cPickorderBarcode.currentPickorderBarcode = cPickorderLine.currentPickOrderLine.barcodesObl.get(0);
            }
        }

        if (cPickorderBarcode.currentPickorderBarcode != null) {
            SortorderSortActivity.articleBarcodeText.setText(cPickorderBarcode.currentPickorderBarcode.getBarcodeAndQuantityStr());
        } else {
            SortorderSortActivity.articleBarcodeText.setText(cAppExtension.context.getString(R.string.mutiple_barcodes_posible));
        }
    }

    private void mShowBarcodeSelectFragment() {
        BarcodeFragment barcodeFragment = new BarcodeFragment();
        barcodeFragment.show(cAppExtension.fragmentManager, BARCODEPICKERFRAGMENT_TAG);
    }

    private static void mEnablePlusMinusAndBarcodeSelectViews() {

        if (!cSetting.PICK_PER_SCAN()) {
            SortorderSortActivity.imageButtonMinus.setVisibility(View.INVISIBLE);
            SortorderSortActivity.imageButtonPlus.setVisibility(View.INVISIBLE);
        } else {
            SortorderSortActivity.imageButtonMinus.setVisibility(View.VISIBLE);
            SortorderSortActivity.imageButtonPlus.setVisibility(View.VISIBLE);
        }

        if (!cSetting.PICK_SELECTEREN_BARCODE()) {
            SortorderSortActivity.imageButtonBarcode.setVisibility(View.INVISIBLE);
        } else {
            SortorderSortActivity.imageButtonBarcode.setVisibility(View.VISIBLE);
        }
    }

    private static void mCheckLineDone() {

        //We scanned a sortlocation
        SortorderSortActivity.articleScannedLastBln = false;

        //If we didn't complete this line, then show done for now
        if (cPickorderLine.currentPickOrderLine.quantityHandledDbl < cPickorderLine.currentPickOrderLine.getQuantityDbl()) {
            SortorderSortActivity.imageButtonDone.setImageResource(R.drawable.ic_check_black_24dp);
            SortorderSortActivity.imageButtonDone.setVisibility(View.VISIBLE);

            // If auto accept is false or  if we have to scan VKO after each piece , then return
            if (!cSetting.PICK_AUTO_ACCEPT() || cPickorder.currentPickOrder.isPickPickPVVKOEachPieceBln() ) {
                return;
            }

            // We are done for now
            SortorderSortActivity.mSortDoneForNow();
            return;
        }

        //We didn complete this line, then show done
        if (!cSetting.PICK_AUTO_ACCEPT()) {
            SortorderSortActivity.imageButtonDone.setImageResource(R.drawable.ic_doublecheck_black_24dp);
            SortorderSortActivity.imageButtonDone.setVisibility(View.VISIBLE);
            return;
        }

        // We are completely done
        SortorderSortActivity.mSortDone();
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
        SortorderSortActivity.minusHandler.postDelayed(pvRunnable, milliSecsLng);
        SortorderSortActivity.pickCounterMinusHelperInt += 1;
    }

    private void mDoDelayedPlus(Runnable pvRunnable, long milliSecsLng) {
        SortorderSortActivity.plusHandler.postDelayed(pvRunnable, milliSecsLng);
        SortorderSortActivity.pickCounterPlusHelperInt += 1;
    }

    private static void mShowOverpickNotAllowed(){
        SortorderSortActivity.quantityText.setText(quantityRequiredText.getText());
        cUserInterface.pShowSnackbarMessage(textViewAction , cAppExtension.context.getString(R.string.number_cannot_be_higher), null, false);
        cUserInterface.pDoNope(quantityText, true, true);
        cUserInterface.pDoNope(quantityRequiredText, false, false);
    }

    private static Boolean mFindBarcodeInLineBarcodes(cBarcodeScan pvBarcodeScan) {

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
