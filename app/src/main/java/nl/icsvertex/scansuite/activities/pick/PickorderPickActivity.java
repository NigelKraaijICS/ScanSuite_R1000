package nl.icsvertex.scansuite.activities.pick;

import android.annotation.SuppressLint;
import android.app.AlertDialog;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.cardview.widget.CardView;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ICS.Interfaces.iICSDefaultActivity;
import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cRegex;
import ICS.Utils.cResult;
import ICS.Utils.cText;
import ICS.Utils.cUserInterface;
import SSU_WHS.Basics.BarcodeLayouts.cBarcodeLayout;
import SSU_WHS.Basics.Settings.cSetting;
import SSU_WHS.Picken.PickorderBarcodes.cPickorderBarcode;
import SSU_WHS.Picken.Pickorders.cPickorder;
import SSU_WHS.Picken.SalesOrderPackingTable.cSalesOrderPackingTable;
import nl.icsvertex.scansuite.cAppExtension;
import SSU_WHS.General.cPublicDefinitions;

import SSU_WHS.Picken.PickorderLines.cPickorderLine;

import nl.icsvertex.scansuite.fragments.dialogs.AcceptRejectFragment;
import nl.icsvertex.scansuite.fragments.dialogs.ArticleFullViewFragment;
import nl.icsvertex.scansuite.fragments.dialogs.BarcodePickerFragment;
import nl.icsvertex.scansuite.fragments.dialogs.NumberpickerFragment;
import nl.icsvertex.scansuite.R;

public class PickorderPickActivity extends AppCompatActivity implements iICSDefaultActivity {

    //Region Private Properties
    static final String NUMBERPICKERFRAGMENT_TAG = "NUMBERPICKERFRAGMENT_TAG";
    static final String BARCODEPICKERFRAGMENT_TAG = "BARCODEPICKERFRAGMENT_TAG";
    static final String ACCEPTREJECTFRAGMENT_TAG = "ACCEPTREJECTFRAGMENT_TAG";

    private static Boolean articleScannedLastBln;
    private static int pickCounterMinusHelperInt;
    private static int pickCounterPlusHelperInt;

    private static Handler minusHandler;
    private static Handler plusHandler;
    private static ConstraintLayout pickorderPickContainer;
    private static ImageView toolbarImage;
    private static TextView toolbarTitle;
    private static TextView toolbarSubtext;
    private static ImageView toolbarImageHelp;
    private static CardView articleContainer;
    private static TextView articleDescriptionText;
    private static TextView articleDescription2Text;
    private static TextView articleItemText;
    private static TextView articleBarcodeText;
    private static TextView articleVendorItemText;
    private static TextView binText;
    private static TextView containerText;
    private static TextView quantityText;
    private static TextView quantityRequiredText;
    private static ImageView articleThumbImageView;
    private static ImageView imageButtonBarcode;
    private static TextView sourcenoText;
    private static CardView sourcenoContainer;
    private static TextView genericItemExtraField1Text;
    private static TextView genericItemExtraField2Text;
    private static TextView genericItemExtraField3Text;
    private static TextView genericItemExtraField4Text;
    private static TextView genericItemExtraField5Text;
    private static TextView genericItemExtraField6Text;
    private static TextView genericItemExtraField7Text;
    private static TextView genericItemExtraField8Text;
    private static AppCompatImageButton imageButtonMinus;
    private static AppCompatImageButton imageButtonPlus;
    private static  AppCompatImageButton imageButtonDone;
    private static TextView textViewAction;

    //End Region Private Properties

    //Region Default Methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickorder_pick);
        this.mActivityInitialize();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        //Set listeners here, so click listeners only work after activity is shown
        this.mSetListeners();
    }


    @Override
    protected void onDestroy() {
        try {
            cBarcodeScan.pUnregisterBarcodeReceiver();
        } catch (Exception e) {
            e.printStackTrace();
        }
        LocalBroadcastManager.getInstance(cAppExtension.context).unregisterReceiver(mNumberReceiver);
        cBarcodeScan.pUnregisterBarcodeReceiver();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        try {
            cBarcodeScan.pUnregisterBarcodeReceiver();
        } catch (Exception e) {
            e.printStackTrace();
        }
        LocalBroadcastManager.getInstance(cAppExtension.context).unregisterReceiver(mNumberReceiver);
        cBarcodeScan.pUnregisterBarcodeReceiver();
        super.onPause();
    }

    @Override
    protected void onResume() {
        LocalBroadcastManager.getInstance(cAppExtension.context).registerReceiver(mNumberReceiver, new IntentFilter(cPublicDefinitions.NUMBERINTENT_NUMBER));
        cBarcodeScan.pRegisterBarcodeReceiver();
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.mShowAcceptFragment();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //End Region Default Methods

    //Region iICSDefaultActivity Methods

    @Override
    public void mActivityInitialize() {

        this.mSetAppExtensions();

        this.mFindViews();

        this.mSetViewModels();

        this.mSetSettings();

        this.mSetToolbar(getResources().getString(R.string.screentitle_pickorderpick));

        this.mInitScreen();

        this.mFieldsInitialize();


    }

    @Override
    public void mSetAppExtensions() {
        cAppExtension.context = this;
        cAppExtension.fragmentActivity = this;
        cAppExtension.activity = this;
        cAppExtension.fragmentManager = getSupportFragmentManager();
    }

    @Override
    public void mFindViews() {

        this.pickorderPickContainer = findViewById(R.id.pickorderPickContainer);
        this.toolbarImage = findViewById(R.id.toolbarImage);
        this.toolbarTitle = findViewById(R.id.toolbarTitle);
        this.toolbarSubtext = findViewById(R.id.toolbarSubtext);
        this.toolbarImageHelp = findViewById(R.id.toolbarImageHelp);

        this.articleContainer = findViewById(R.id.addressContainer);
        this.articleDescriptionText = findViewById(R.id.articleDescriptionText);
        this.articleDescription2Text = findViewById(R.id.articleDescription2Text);
        this.articleItemText = findViewById(R.id.articleItemText);
        this.articleBarcodeText = findViewById(R.id.articleBarcodeText);
        this.articleVendorItemText = findViewById(R.id.articleVendorItemText);
        this.binText = findViewById(R.id.binText);
        this.sourcenoText = findViewById(R.id.sourcenoText);
        this.sourcenoContainer = findViewById(R.id.sourcenoContainer);

        this.genericItemExtraField1Text = findViewById(R.id.genericItemExtraField1Text);
        this.genericItemExtraField2Text = findViewById(R.id.genericItemExtraField2Text);
        this.genericItemExtraField3Text = findViewById(R.id.genericItemExtraField3Text);
        this.genericItemExtraField4Text = findViewById(R.id.genericItemExtraField4Text);
        this.genericItemExtraField5Text = findViewById(R.id.genericItemExtraField5Text);
        this.genericItemExtraField6Text = findViewById(R.id.genericItemExtraField6Text);
        this.genericItemExtraField7Text = findViewById(R.id.genericItemExtraField7Text);
        this.genericItemExtraField8Text = findViewById(R.id.genericItemExtraField8Text);

        this.containerText = findViewById(R.id.containerText);
        this.quantityText = findViewById(R.id.quantityText);
        this.quantityRequiredText = findViewById(R.id.quantityRequiredText);
        this.articleThumbImageView = findViewById(R.id.articleThumbImageView);
        this.imageButtonBarcode = findViewById(R.id.imageButtonBarcode);
        this.imageButtonMinus = findViewById(R.id.imageButtonMinus);
        this.imageButtonPlus = findViewById(R.id.imageButtonPlus);
        this.imageButtonDone = findViewById(R.id.imageButtonDone);

        this.textViewAction = findViewById(R.id.textViewAction);
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

    }

    @Override
    public void mFieldsInitialize() {
        PickorderPickActivity.mFieldsInitializeStatic();
    }

    @Override
    public void mInitScreen() {

        cBarcodeScan.pRegisterBarcodeReceiver();

        //We scanned a BIN, so nu current barcode known
        if (cPickorderBarcode.currentPickorderBarcode == null) {
            //Initialise article scanned boolean
            articleScannedLastBln = false;
            return;
        }

         // We scanned an ARTICLE, so handle barcide

        if (cSetting.PICK_BIN_IS_ITEM()) {
            articleScannedLastBln = true;
            this.pHandleScan(cPickorderBarcode.currentPickorderBarcode.getBarcodeStr());
        }

    }

    @Override
    public void mSetListeners() {

        this.mSetArticleImageListener();
        this.mSetImageButtonBarcodeListener();

        if (cSetting.PICK_SELECTEREN_BARCODE() == true) {
            this.mSetNumberListener();
        }

        this.mSetPlusListener();
        this.mSetMinusListener();
        this.mSetDoneListener();

    }

    // End Region iICSDefaultActivity Methods

    //Region Public Methods

    public static void pHandleScan(String pvScannedBarcodeStr) {

        cUserInterface.pCheckAndCloseOpenDialogs();

        if (cPickorder.currentPickOrder.isPVBln()) {
            PickorderPickActivity.mHandlePVScan(pvScannedBarcodeStr);
            return;
        }

        if (PickorderPickActivity.mFindBarcodeInOrderline(pvScannedBarcodeStr) == false) {
            cUserInterface.pDoExplodingScreen(cAppExtension.context.getString(R.string.error_unknown_barcode), pvScannedBarcodeStr, true, true);
            return;
        }

        //If we found the barcode, currentbarcode is alreay filled, so make this selected
        PickorderPickActivity.mBarcodeSelected(cPickorderBarcode.currentPickorderBarcode);
        return;

    }

    public static void pAcceptPick() {
         cPickorderLine.currentPickOrderLine.pHandledIndatabaseBln();
         PickorderPickActivity.mPickDone();
    }

    public static void pCancelPick() {
        cPickorderLine.currentPickOrderLine.quantityHandledDbl = Double.valueOf(0);
        cPickorderLine.currentPickOrderLine.pCancelIndatabaseBln();
        PickorderPickActivity.mGoBackToLinesActivity();
        return;
    }

    //End Region Public Methods

    //Region Private Methods

    //Views
    private static void mFieldsInitializeStatic(){

        PickorderPickActivity.imageButtonDone.setImageResource(R.drawable.ic_check_black_24dp);

        PickorderPickActivity.pickCounterPlusHelperInt = 0;
        PickorderPickActivity.pickCounterMinusHelperInt = 0;
        PickorderPickActivity.toolbarSubtext.setText(cPickorder.currentPickOrder.getOrderNumberStr());

        PickorderPickActivity.articleDescriptionText.setText(cPickorderLine.currentPickOrderLine.getDescriptionStr());
        PickorderPickActivity.articleDescription2Text.setText(cPickorderLine.currentPickOrderLine.getDescription2Str());
        PickorderPickActivity.articleItemText.setText(cPickorderLine.currentPickOrderLine.getItemNoStr() + " " + cPickorderLine.currentPickOrderLine.getVariantCodeStr());
        PickorderPickActivity.articleVendorItemText.setText(cPickorderLine.currentPickOrderLine.getVendorItemNo() + ' ' + cPickorderLine.currentPickOrderLine.getVendorItemDescriptionStr());

        PickorderPickActivity.binText.setText(cPickorderLine.currentPickOrderLine.getBinCodeStr());
        PickorderPickActivity.textViewAction.setText(cAppExtension.context.getString(R.string.scan_article));

        PickorderPickActivity.containerText.setVisibility(View.GONE);
        PickorderPickActivity.containerText.setText(cPickorderLine.currentPickOrderLine.getContainerStr());
        PickorderPickActivity.containerText.setText("");
        PickorderPickActivity.quantityText.setText("0");
        PickorderPickActivity.quantityRequiredText.setText(cText.doubleToString(cPickorderLine.currentPickOrderLine.getQuantityDbl()));

        PickorderPickActivity.mEnablePlusAndMinusButtons();
        PickorderPickActivity.mShowArticleImage();
        PickorderPickActivity.mShowOrHideGenericExtraFields();
        PickorderPickActivity.mShowBarcodeInfo();
        PickorderPickActivity.mShowSortingInstruction();

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

                mEnablePlusAndMinusButtons();

                //If we only have one barcode, then automatticaly select that barcode
                if (cPickorderLine.currentPickOrderLine.barcodesObl.size() == 1) {
                    mBarcodeSelected(cPickorderLine.currentPickOrderLine.barcodesObl.get(0));
                    return;
                }

                mShowBarcodeSelectFragment();
                return;

            }
        });
    }

    private static void mShowOrHideGenericExtraFields() {

        if (!cPickorderLine.currentPickOrderLine.getExtraField1Str().isEmpty()) {
            genericItemExtraField1Text.setVisibility(View.VISIBLE);
            genericItemExtraField1Text.setText(cPickorderLine.currentPickOrderLine.getExtraField1Str());
        }

        if (!cPickorderLine.currentPickOrderLine.getExtraField2Str().isEmpty()) {
            genericItemExtraField2Text.setVisibility(View.VISIBLE);
            genericItemExtraField2Text.setText(cPickorderLine.currentPickOrderLine.getExtraField2Str());
        }

        if (!cPickorderLine.currentPickOrderLine.getExtraField3Str().isEmpty()) {
            genericItemExtraField3Text.setVisibility(View.VISIBLE);
            genericItemExtraField3Text.setText(cPickorderLine.currentPickOrderLine.getExtraField3Str());
        }

        if (!cPickorderLine.currentPickOrderLine.getExtraField4Str().isEmpty()) {
            genericItemExtraField4Text.setVisibility(View.VISIBLE);
            genericItemExtraField4Text.setText(cPickorderLine.currentPickOrderLine.getExtraField4Str());
        }

        if (!cPickorderLine.currentPickOrderLine.getExtraField5Str().isEmpty()) {
            genericItemExtraField5Text.setVisibility(View.VISIBLE);
            genericItemExtraField5Text.setText(cPickorderLine.currentPickOrderLine.getExtraField5Str());
        }

        if (!cPickorderLine.currentPickOrderLine.getExtraField6Str().isEmpty()) {
            genericItemExtraField6Text.setVisibility(View.VISIBLE);
            genericItemExtraField6Text.setText(cPickorderLine.currentPickOrderLine.getExtraField6Str());
        }

        if (!cPickorderLine.currentPickOrderLine.getExtraField7Str().isEmpty()) {
            genericItemExtraField7Text.setVisibility(View.VISIBLE);
            genericItemExtraField7Text.setText(cPickorderLine.currentPickOrderLine.getExtraField7Str());
        }

        if (!cPickorderLine.currentPickOrderLine.getExtraField8Str().isEmpty()) {
            genericItemExtraField8Text.setVisibility(View.VISIBLE);
            genericItemExtraField8Text.setText(cPickorderLine.currentPickOrderLine.getExtraField8Str());
        }

    }

    private static void mShowBarcodeInfo() {

        if (cPickorderBarcode.currentPickorderBarcode == null) {
            if (cPickorderLine.currentPickOrderLine.barcodesObl.size() == 1) {
                cPickorderBarcode.currentPickorderBarcode = cPickorderLine.currentPickOrderLine.barcodesObl.get(0);
            }
        }

        if (cPickorderBarcode.currentPickorderBarcode != null) {
            PickorderPickActivity.articleBarcodeText.setText(cPickorderBarcode.currentPickorderBarcode.getBarcodeStr() + " (" + cPickorderBarcode.currentPickorderBarcode.getQuantityPerUnitOfMeasureDbl().intValue() + ")");
        } else {
            PickorderPickActivity.articleBarcodeText.setText(cAppExtension.context.getString(R.string.mutiple_barcodes_posible));
        }
    }

    private static void mShowArticleImage() {

        //If pick with picture is false, then hide image view
        if (cPickorder.currentPickOrder.isPickWithPictureBln() == false) {
            PickorderPickActivity.articleThumbImageView.setVisibility(View.INVISIBLE);
            return;
        }

        //If picture is not in cache (via webservice) then show no image
        if (cPickorderLine.currentPickOrderLine.pGetArticleImageBln() == false) {
            cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.could_not_get_article_image), null);
            PickorderPickActivity.articleThumbImageView.setImageDrawable(ContextCompat.getDrawable(cAppExtension.context, R.drawable.ic_no_image_lightgrey_24dp));
            return;
        }

        //If picture is in cache but can't be converted, then show no image
        if (cPickorderLine.currentPickOrderLine.articleImage == null || cPickorderLine.currentPickOrderLine.articleImage.imageBitmap() == null) {
            cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.could_not_get_article_image), null);
            PickorderPickActivity.articleThumbImageView.setImageDrawable(ContextCompat.getDrawable(cAppExtension.context, R.drawable.ic_no_image_lightgrey_24dp));
            return;
        }

        //Show the image
        PickorderPickActivity.articleThumbImageView.setImageBitmap(cPickorderLine.currentPickOrderLine.articleImage.imageBitmap());

        //Open the image
        if ((cPickorder.currentPickOrder.isPickWithPictureAutoOpenBln())) {
            PickorderPickActivity.mShowFullArticleFragment();
        }


    }

    private static void mShowSortingInstruction() {

        //First show the SourceNumber
        PickorderPickActivity.sourcenoText.setText(cPickorderLine.currentPickOrderLine.getSourceNoStr());
        PickorderPickActivity.sourcenoContainer.setVisibility(View.VISIBLE);

        if (cPickorderLine.currentPickOrderLine.getSourceNoStr().trim().isEmpty()) {
            PickorderPickActivity.sourcenoContainer.setVisibility(View.INVISIBLE);
        }

        //If workflow is not PV, then d we are ready
        if (!cPickorder.currentPickOrder.isPVBln()) {
            return;
        }

        //Set scan instruction
        PickorderPickActivity.textViewAction.setText(cAppExtension.context.getString(R.string.scan_article));

        // We already have a processing sequence, show it and insert a SalesOrderPackingTable in database
        if (cPickorderLine.currentPickOrderLine.getProcessingSequenceStr().isEmpty() == false) {
            PickorderPickActivity.sourcenoText.setText(cPickorderLine.currentPickOrderLine.getProcessingSequenceStr());
            PickorderPickActivity.mAddSalesOrderPackingTableBln();
            return;
        }

        // We don't have a processing sequence, so look for it in the database
        if (cPickorder.currentPickOrder.salesOrderPackingTableObl() == null || cPickorder.currentPickOrder.salesOrderPackingTableObl().size() == 0) {
            return;
        }

        //Record for Current Sales order
        cSalesOrderPackingTable recordForSalesOrder = null;

        for (cSalesOrderPackingTable loopRecord : cPickorder.currentPickOrder.salesOrderPackingTableObl()) {

            if (loopRecord.getSalesorderStr().equalsIgnoreCase(cPickorderLine.currentPickOrderLine.getSourceNoStr()))
                recordForSalesOrder = loopRecord;
        }

        //If we found something, show it
        if (recordForSalesOrder != null) {
            //Set scan instruction
            PickorderPickActivity.sourcenoText.setText(recordForSalesOrder.getPackingtableStr());
            PickorderPickActivity.textViewAction.setText(cAppExtension.context.getString(R.string.scan_pickcartbox));
            return;
        }
    }

    private static void mEnablePlusAndMinusButtons() {

        if (!cSetting.PICK_SELECTEREN_BARCODE()) {
            PickorderPickActivity.imageButtonMinus.setVisibility(View.INVISIBLE);
            PickorderPickActivity.imageButtonPlus.setVisibility(View.INVISIBLE);
        } else {
            PickorderPickActivity.imageButtonMinus.setVisibility(View.VISIBLE);
            PickorderPickActivity.imageButtonPlus.setVisibility(View.VISIBLE);
        }
    }

    //Scans and manual input

    private void mNumberClicked() {

        if (cPickorderBarcode.currentPickorderBarcode == null) {
            cUserInterface.pDoNope(quantityText, false, false);
            cUserInterface.pShowSnackbarMessage(pickorderPickContainer, getString(R.string.choose_barcode_first), null, false);
            return;
        }

        if (cPickorderBarcode.currentPickorderBarcode.getQuantityHandledDbl() > 1) {
            cUserInterface.pDoNope(quantityText, true, true);
            cUserInterface.pShowSnackbarMessage(pickorderPickContainer, getString(R.string.manual_input_only_barcodenumber_bigger1), null, false);
            return;
        }

        this.mShowNumberPickerFragment();
    }

    private void mHandleQuantityChosen(double pvQuantityDbl) {


        if (pvQuantityDbl == 0) {
            this.mTryToChangePickedQuantity(false, true,pvQuantityDbl);
        } else {
            this.mTryToChangePickedQuantity(true, true,pvQuantityDbl);

        }



    }

    private static void mHandlePVScan(String pvScannedBarcodeStr) {

        if (cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvScannedBarcodeStr, cBarcodeLayout.barcodeLayoutEnu.ARTICLE)) {
            PickorderPickActivity.mHandlePVArticleScanned(pvScannedBarcodeStr);
            return;
        }

        PickorderPickActivity.mHandleSalesOrderOrPickCartScanned(pvScannedBarcodeStr);
        return;

    }

    private static void mTryToChangePickedQuantity(Boolean pvIsPositiveBln, Boolean pvAmountFixedBln, double pvAmountDbl) {



        final double newQuantityDbl;

        if (pvIsPositiveBln) {

            //Determine the new amount
            if (pvAmountFixedBln == true) {
                newQuantityDbl = pvAmountDbl;
            } else {
                newQuantityDbl = cPickorderLine.currentPickOrderLine.getQuantityHandledDbl() + pvAmountDbl;
            }

            //Check if we would exceed amount, then show message
            if (newQuantityDbl > cPickorderLine.currentPickOrderLine.getQuantityDbl()) {
                PickorderPickActivity.mShowOverpickNotAllowed();
                articleScannedLastBln = false;
                return;
            }

            //Set the new quantity and show in Activity
            cPickorderLine.currentPickOrderLine.quantityHandledDbl = newQuantityDbl;
            PickorderPickActivity.quantityText.setText(cText.doubleToString(cPickorderLine.currentPickOrderLine.getQuantityHandledDbl()));

            //Add or update line barcode
            cPickorderLine.currentPickOrderLine.pAddOrUpdateLineBarcodeBln();

            //Update orderline info (quantity, timestamp, localstatus)
            cPickorderLine.currentPickOrderLine.pHandledIndatabaseBln();

            //Check if this line is done
            PickorderPickActivity.mCheckLineDone();
            return;
        }

        //negative
        if (cPickorderLine.currentPickOrderLine.quantityHandledDbl == 0 ) {
            cUserInterface.pDoNope(quantityText, true, true);
            return;
        }

        //Determine the new amount

        if (pvAmountFixedBln == true) {
            newQuantityDbl = pvAmountDbl;
        }else {
            newQuantityDbl= cPickorderLine.currentPickOrderLine.getQuantityHandledDbl() - pvAmountDbl;
        }

        if (newQuantityDbl <= 0) {
            cPickorderLine.currentPickOrderLine.quantityHandledDbl = Double.valueOf(0);
        }else {
            //Set the new quantity and show in Activity
            cPickorderLine.currentPickOrderLine.quantityHandledDbl = newQuantityDbl;
        }

        PickorderPickActivity.quantityText.setText(cText.doubleToString(cPickorderLine.currentPickOrderLine.getQuantityHandledDbl()));
        PickorderPickActivity.imageButtonDone.setImageResource(R.drawable.ic_check_black_24dp);

        //Remove or update line barcode
        cPickorderLine.currentPickOrderLine.pRemoveOrUpdateLineBarcodeBln();
        return;

    }

    private static void mHandleSalesOrderOrPickCartScanned(String pvScannedBarcodeStr) {

        //Strip barcode from regex
        String barcodeWithoutPrefixStr = cRegex.pStripRegexPrefixStr(pvScannedBarcodeStr);

        // Check if article is already scanned
        if (PickorderPickActivity.articleScannedLastBln == false) {
            // we've scanned a pickCart or a salesOrder, but we need an article
            cUserInterface.pDoExplodingScreen(cAppExtension.context.getString(R.string.message_scan_article_first), barcodeWithoutPrefixStr, true, true);
            return;
        }

        //Check if scanned barcode is a SalesOrder or PickCartBox
        Boolean isSalesOrderBln = cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvScannedBarcodeStr, cBarcodeLayout.barcodeLayoutEnu.SALESORDER);
        Boolean isPickCartBoxBln = cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvScannedBarcodeStr, cBarcodeLayout.barcodeLayoutEnu.PICKCARTBOX);

        //If we scanned a salesorder, then check if it matches the SourceNo
        if (isSalesOrderBln == true) {
            //If scanned value doesn't match then we are done
            if (barcodeWithoutPrefixStr.equalsIgnoreCase(cPickorderLine.currentPickOrderLine.getSourceNoStr()) == false) {
                cUserInterface.pDoExplodingScreen(cAppExtension.context.getString(R.string.message_wrong_sourceno), barcodeWithoutPrefixStr, true, true);
                return;
            }
        }

        //If we scanned a pickcartbox, then check if it matches the ProcessingSequence if ProcessingSequence is not empty
        if (isPickCartBoxBln == true && cPickorderLine.currentPickOrderLine.getProcessingSequenceStr().isEmpty() == false) {
            //If scanned value doesn't match then we are done
            if (barcodeWithoutPrefixStr.equalsIgnoreCase(cPickorderLine.currentPickOrderLine.getProcessingSequenceStr()) == false) {
                cUserInterface.pDoExplodingScreen(cAppExtension.context.getString(R.string.message_wrong_pickcartbox), barcodeWithoutPrefixStr, true, true);
                return;
            }
        }

        // There are  previous salesorder/pickcaerbox scans, so check if input is correct
        if (PickorderPickActivity.mCheckPackingTableAndSourceNoBln(barcodeWithoutPrefixStr) == false) {
            return;
        }

        //Set the Processing Sequence
        cPickorderLine.currentPickOrderLine.processingSequenceStr = barcodeWithoutPrefixStr;

        //try to add SalesOrderPackingtable to database
        if (PickorderPickActivity.mAddSalesOrderPackingTableBln() == false) {
            cUserInterface.pDoExplodingScreen(cAppExtension.context.getString(R.string.error_inserting_salesorderpackingtable), barcodeWithoutPrefixStr, true, true);
            cPickorderLine.currentPickOrderLine.processingSequenceStr = "";
            return;
        }

        //try to update ProcessingSequence in database
        if (cPickorderLine.currentPickOrderLine.pUpdateProcessingSequenceBln(barcodeWithoutPrefixStr) == false) {
            cUserInterface.pDoExplodingScreen(cAppExtension.context.getString(R.string.error_updating_processing_sequence), barcodeWithoutPrefixStr, true, true);
            return;
        }

        //Set the ProcessingSequence
        cPickorderLine.currentPickOrderLine.processingSequenceStr = barcodeWithoutPrefixStr;
        PickorderPickActivity.sourcenoText.setText(cPickorderLine.currentPickOrderLine.processingSequenceStr);

        // If this is VKO after each piece, then show new instructions
        if (cPickorder.currentPickOrder.isPickPickPVVKOEachPieceBln() == true) {
            PickorderPickActivity.textViewAction.setText(cAppExtension.context.getString(R.string.scan_article));
            PickorderPickActivity.articleScannedLastBln = false;
        }

        //Update orderline info (quantity, timestamp, localstatus)
        cPickorderLine.currentPickOrderLine.pHandledIndatabaseBln();

        //Check if line is done
        PickorderPickActivity.mCheckLineDone();
        return;


    }

    private static void mHandlePVArticleScanned(String pvScannedBarcodeStr) {

        //We didn't scan an article yet, so handle it as a "normal" scan
        if (articleScannedLastBln == false) {

            if (PickorderPickActivity.mFindBarcodeInOrderline(pvScannedBarcodeStr) == false) {
                cUserInterface.pDoExplodingScreen(cAppExtension.context.getString(R.string.error_unknown_barcode), pvScannedBarcodeStr, true, true);
                return;
            }

            //Succesfull article scanned
            articleScannedLastBln = true;

            //If we found the barcode, currentbarcode is alreay filled, so make this selected
            PickorderPickActivity.mBarcodeSelected(cPickorderBarcode.currentPickorderBarcode);
            return;
        }

        //We last scanned an article, but thats oke
        if (cPickorder.currentPickOrder.isPickPickPVVKOEachPieceBln() == false) {
            PickorderPickActivity.pHandleScan(pvScannedBarcodeStr);
            return;
        }

        //You have to scan a pickcart or salesorder after the last article scan
        cUserInterface.pDoExplodingScreen(cAppExtension.context.getString(R.string.message_scan_pickcart_or_salesorder), pvScannedBarcodeStr, true, true);
        return;

    }

    public static void mBarcodeSelected(cPickorderBarcode pvBarcode) {

        cUserInterface.pCheckAndCloseOpenDialogs();

        cPickorderBarcode.currentPickorderBarcode = pvBarcode;
        PickorderPickActivity.mShowBarcodeInfo();
        PickorderPickActivity.mTryToChangePickedQuantity(true, false, cPickorderBarcode.currentPickorderBarcode.getQuantityPerUnitOfMeasureDbl());
        return;
    }

    // Lines, Barcodes and Packing Tables

    private static Boolean mFindBarcodeInOrderline(String pvScannedBarcodeStr) {

        if (cPickorderLine.currentPickOrderLine.barcodesObl == null || cPickorderLine.currentPickOrderLine.barcodesObl.size() == 0) {
            return false;
        }

        for (cPickorderBarcode pickorderBarcode : cPickorderLine.currentPickOrderLine.barcodesObl) {

            if (pickorderBarcode.barcodeStr.equalsIgnoreCase(pvScannedBarcodeStr)) {
                cPickorderBarcode.currentPickorderBarcode = pickorderBarcode;
                return true;
            }
        }

        return false;
    }

    private static void mCheckLineDone() {

        if (cPickorderLine.currentPickOrderLine.quantityHandledDbl < cPickorderLine.currentPickOrderLine.quantityDbl) {
            PickorderPickActivity.imageButtonDone.setImageResource(R.drawable.ic_check_black_24dp);
            return;
        }

        //PV
        if (cPickorder.currentPickOrder.isPVBln()) {

            if (cPickorder.currentPickOrder.isPickPickPVVKOEachPieceBln() == false) {
                PickorderPickActivity.articleScannedLastBln = true;
            }

            // We still need to scan a pickkart/sales order so not done
            if (cPickorderLine.currentPickOrderLine.processingSequenceStr.isEmpty()) {
                PickorderPickActivity.textViewAction.setText(cAppExtension.context.getString(R.string.message_scan_pickcart_or_salesorder));
                return;
            }
        }



        if (cSetting.PICK_AUTO_ACCEPT() == false) {
            return;
        }

        PickorderPickActivity.articleScannedLastBln = false;
        PickorderPickActivity.mPickDone();
    }

    private static void mGetNextPickLineFromLocation() {

        cResult hulpResult;

        //check if there is a next line for this BIN
        cPickorderLine nextLine = cPickorder.currentPickOrder.pGetNetxLineToHandleForBin(cPickorderLine.currentPickOrderLine.getBinCodeStr());

       //There is no next line, so close this activity
        if (nextLine == null) {
            //Clear current barcode and reset defaults
            cPickorderLine.currentPickOrderLine = null;
            PickorderPickActivity.articleScannedLastBln = false;
            mGoBackToLinesActivity();
            return;
        }

        //Set the current line, and update it to busy
        cPickorderLine.currentPickOrderLine = nextLine;
        hulpResult = cPickorderLine.currentPickOrderLine.pLineBusyRst();
        if (hulpResult.resultBln == false) {
            cUserInterface.pDoExplodingScreen(hulpResult.messagesStr(),"",true,true);
            cPickorderLine.currentPickOrderLine = null;
            mGoBackToLinesActivity();
            return;
        }

        PickorderPickActivity.mNextLineForBin();
        return;

    }

    private static void mNextLineForBin() {

        //Play a sound
        cUserInterface.pPlaySound(R.raw.message, null);

        //Clear current barcode and reset defaults
        cPickorderBarcode.currentPickorderBarcode = null;
        PickorderPickActivity.articleScannedLastBln = false;

        //Show animation and initialize fields
        Animation animation = AnimationUtils.loadAnimation(cAppExtension.context.getApplicationContext(), R.anim.shrink_and_fade);
        PickorderPickActivity.pickorderPickContainer.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                PickorderPickActivity.mFieldsInitializeStatic();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


    }

    private static Boolean mSendPickorderLine() {

        if (cPickorderLine.currentPickOrderLine.pHandledBln() == false) {
            //could not send line, let user know but answer succes so user can go to next line
            cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.couldnt_send_line), null);
            cPickorderLine.currentPickOrderLine.pErrorSendingBln();
            return true;
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
        if (cPickorder.currentPickOrder.salesOrderPackingTableObl().contains(salesOrderPackingTable) == true) {
            return true;
        }

        // Does not exist, so insert in database
        salesOrderPackingTable.pInsertInDatabaseBln();
        return true;

    }

    private void mHandlePickDoneClick() {

        //If workflow is then call specific void
        if (cPickorder.currentPickOrder.isPVBln() == true) {
            this.mHandlePVDone();
            return;
        }

        //Check if we picked less then asked, if so then show dialog
        if (cPickorderLine.currentPickOrderLine.getQuantityHandledDbl() != cPickorderLine.currentPickOrderLine.getQuantityDbl()) {
            this.mShowUnderPickDialog();
            return;
        }

        //All is done
        this.mPickDone();

    }

    private void mHandlePVDone() {

        if (this.articleScannedLastBln) {
            this.textViewAction.setText(R.string.message_scan_pickcart_or_salesorder);
            cUserInterface.pShowToastMessage(getString(R.string.message_scan_pickcart_or_salesorder), R.raw.badsound);
            return;
        }

        //If we picked less then asked, show dialog
        if (cPickorderLine.currentPickOrderLine.getQuantityHandledDbl() != cPickorderLine.currentPickOrderLine.getQuantityDbl()) {
            this.mShowUnderPickDialog();
            return;
        }

        this.mPickDone();
        return;
    }

    private static void mPickDone() {

        PickorderPickActivity.mSendPickorderLine();
        PickorderPickActivity.mGetNextPickLineFromLocation();

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

            if (recordForSalesOrder.getPackingtableStr().equalsIgnoreCase(pvBarcodeStr)) {
                recordForBarcode = loopRecord;
            }
        }

        //Could not find both, so everything is fine
        if (recordForBarcode == null && recordForSalesOrder == null) {
            return true;
        }


        //We found a record for this barcode
        if (recordForBarcode != null) {

            if (recordForBarcode.getSalesorderStr().equalsIgnoreCase(cPickorderLine.currentPickOrderLine.getSourceNoStr()) == false) {
                cUserInterface.pDoExplodingScreen(cAppExtension.context.getString(R.string.message_location_already_assigned), "", true, true);
                return false;
            }
        }

        //We found a record for this salesorder
        if (recordForSalesOrder != null) {

            if (recordForSalesOrder.getPackingtableStr().equalsIgnoreCase(pvBarcodeStr) == false) {
                cUserInterface.pDoExplodingScreen(cAppExtension.context.getString(R.string.message_wrong_pickcartbox), recordForSalesOrder.getPackingtableStr(), true, true);
                return false;
            }
        }


        return true;
    }

    //Listeners
    private void mSetDoneListener() {
        this.imageButtonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mHandlePickDoneClick();
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void mSetPlusListener() {

        this.imageButtonPlus.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (plusHandler != null) return true;
                    plusHandler = new Handler();
                    plusHandler.postDelayed(mPlusAction, 750);
                }

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (plusHandler == null) return true;
                    plusHandler.removeCallbacks(mPlusAction);
                    plusHandler = null;
                    pickCounterPlusHelperInt = 0;
                }

                return false;
            }
        });

        imageButtonPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //There is no selected barcode, select one first
                if (cPickorderBarcode.currentPickorderBarcode == null) {
                    cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.message_select_one_of_multiple_barcodes),null);
                    return;
                }


                mTryToChangePickedQuantity(true, false, cPickorderBarcode.currentPickorderBarcode.getQuantityHandledDbl());
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void mSetMinusListener() {

        this.imageButtonMinus.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (minusHandler != null) return true;
                    minusHandler = new Handler();
                    minusHandler.postDelayed(mMinusAction, 750);
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (minusHandler == null) return true;
                    minusHandler.removeCallbacks(mMinusAction);
                    minusHandler = null;
                    pickCounterMinusHelperInt = 0;
                }
                return false;
            }

        });

        this.imageButtonMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //There is no selected barcode, select one first
                if (cPickorderBarcode.currentPickorderBarcode == null) {
                    cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.message_select_one_of_multiple_barcodes),null);
                    return;
                }


                mTryToChangePickedQuantity(false, false, cPickorderBarcode.currentPickorderBarcode.getQuantityHandledDbl());
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

    private Runnable mMinusAction = new Runnable() {
        @Override
        public void run() {
            imageButtonMinus.performClick();
            long millisecs;
            if (pickCounterMinusHelperInt < 10) {
                millisecs = 200;
            } else if (pickCounterMinusHelperInt < 20) {
                millisecs = 150;
            } else if (pickCounterMinusHelperInt < 30) {
                millisecs = 100;
            } else if (pickCounterMinusHelperInt < 40) {
                millisecs = 50;
            } else {
                millisecs = 50;
            }
            mDoDelayedMinus(this, millisecs);
        }
    };

    private Runnable mPlusAction = new Runnable() {
        @Override
        public void run() {
            imageButtonPlus.performClick();
            long millisecs;
            if (pickCounterPlusHelperInt < 10) {
                millisecs = 200;
            } else if (pickCounterPlusHelperInt < 20) {
                millisecs = 150;
            } else if (pickCounterPlusHelperInt < 30) {
                millisecs = 100;
            } else if (pickCounterPlusHelperInt < 40) {
                millisecs = 50;
            } else {
                millisecs = 50;
            }
            mDoDelayedPlus(this, millisecs);
        }
    };

    private BroadcastReceiver mNumberReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int numberChosenInt = 0;
            Bundle extras = intent.getExtras();

            if (extras != null) {
                numberChosenInt = extras.getInt(cPublicDefinitions.NUMBERINTENT_EXTRANUMBER);
            }
            mHandleQuantityChosen(numberChosenInt);
        }
    };

    private void mDoDelayedMinus(Runnable pvRunnable, long pvMilliSecsLng) {
        this.minusHandler.postDelayed(pvRunnable, pvMilliSecsLng);
        this.pickCounterMinusHelperInt += 1;
        return;
    }

    private void mDoDelayedPlus(Runnable pvRunnable, long pvMilliSecsLng) {
        this.plusHandler.postDelayed(pvRunnable, pvMilliSecsLng);
        this.pickCounterPlusHelperInt += 1;
        return;
    }

    //Dialogs and Activitys

    private static void mShowFullArticleFragment() {

        cUserInterface.pCheckAndCloseOpenDialogs();

        ArticleFullViewFragment articleFullViewFragment = new ArticleFullViewFragment();
        articleFullViewFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.ARTICLEFULL_TAG);
    }

    private void mShowUnderPickDialog() {

        cUserInterface.pCheckAndCloseOpenDialogs();

        AlertDialog.Builder builder = new AlertDialog.Builder(cAppExtension.context);
        builder.setTitle(R.string.message_underpick_header);
        builder.setMessage(getString(R.string.message_underpick_text, cText.doubleToString(cPickorderLine.currentPickOrderLine.getQuantityDbl()), cText.doubleToString(cPickorderLine.currentPickOrderLine.getQuantityHandledDbl())));
        builder.setPositiveButton(R.string.button_close_orderline, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                cPickorderLine.currentPickOrderLine.pHandledIndatabaseBln();
                mPickDone();
                return;
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

    private void mShowAcceptFragment(){

        cUserInterface.pCheckAndCloseOpenDialogs();

        final AcceptRejectFragment acceptRejectFragment = new AcceptRejectFragment();
        acceptRejectFragment.setCancelable(true);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // show my popup
                acceptRejectFragment.show(cAppExtension.fragmentManager, ACCEPTREJECTFRAGMENT_TAG);
            }
        });
    }

    private static void mGoBackToLinesActivity() {
        Intent intent = new Intent(cAppExtension.context, PickorderLinesActivity.class);
        cAppExtension.activity.startActivity(intent);
        cAppExtension.activity.finish();
    }

    private void mShowBarcodeSelectFragment() {
        final BarcodePickerFragment barcodePickerFragment = new BarcodePickerFragment();
        barcodePickerFragment.show(cAppExtension.fragmentManager, BARCODEPICKERFRAGMENT_TAG);
        return;
    }

    private void mShowNumberPickerFragment() {

        cUserInterface.pCheckAndCloseOpenDialogs();

        Bundle bundle = new Bundle();
        bundle.putInt(cPublicDefinitions.NUMBERINTENT_CURRENTQUANTITY, cPickorderLine.currentPickOrderLine.getQuantityHandledDbl().intValue());
        bundle.putDouble(cPublicDefinitions.NUMBERINTENT_MAXQUANTITY, cPickorderLine.currentPickOrderLine.getQuantityDbl().intValue());

        NumberpickerFragment numberpickerFragment = new NumberpickerFragment();
        numberpickerFragment.setArguments(bundle);

        numberpickerFragment.show(cAppExtension.fragmentManager, NUMBERPICKERFRAGMENT_TAG);
        return;

    }

    private static void mShowOverpickNotAllowed(){
        PickorderPickActivity.quantityText.setText(quantityRequiredText.getText());
        cUserInterface.pShowSnackbarMessage(textViewAction , cAppExtension.context.getString(R.string.number_cannot_be_higher), null, false);
        cUserInterface.pDoNope(quantityText, true, true);
        cUserInterface.pDoNope(quantityRequiredText, false, false);
        return;
    }

    //End Regin Private Methods

}

