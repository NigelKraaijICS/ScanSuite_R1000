package nl.icsvertex.scansuite.Activities.Move;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.ArrayList;

import ICS.Interfaces.iICSDefaultActivity;
import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cConnection;
import ICS.Utils.cRegex;
import ICS.Utils.cResult;
import ICS.Utils.cText;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Basics.BarcodeLayouts.cBarcodeLayout;
import SSU_WHS.Basics.Settings.cSetting;
import SSU_WHS.General.cPublicDefinitions;
import SSU_WHS.Move.MoveOrders.cMoveorder;
import SSU_WHS.Move.MoveorderLines.cMoveorderLine;
import SSU_WHS.Picken.PickorderBarcodes.cPickorderBarcode;
import SSU_WHS.Picken.PickorderLines.cPickorderLine;
import SSU_WHS.Picken.Pickorders.cPickorder;
import SSU_WHS.Picken.SalesOrderPackingTable.cSalesOrderPackingTable;

import nl.icsvertex.scansuite.Fragments.Dialogs.AcceptRejectFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.ArticleFullViewFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.BarcodeFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.NumberpickerFragment;
import nl.icsvertex.scansuite.R;

public class MoveorderScanPlaceActivity extends AppCompatActivity implements iICSDefaultActivity {

    //Region Private Properties
    static final String NUMBERPICKERFRAGMENT_TAG = "NUMBERPICKERFRAGMENT_TAG";
    static final String BARCODEPICKERFRAGMENT_TAG = "BARCODEPICKERFRAGMENT_TAG";
    static final String ACCEPTREJECTFRAGMENT_TAG = "ACCEPTREJECTFRAGMENT_TAG";

    private static Boolean articleScannedLastBln;
    private static int moveCounterMinusHelperInt;
    private static int moveCounterPlusHelperInt;

    private static Handler minusHandler;
    private static Handler plusHandler;
    private static ConstraintLayout moveorderPickContainer;
    private static ConstraintLayout probeersel;
    private static ImageView toolbarImage;
    private static TextView toolbarTitle;
    private static TextView toolbarSubtext;

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
    private static AppCompatImageButton imageButtonDone;
    private static TextView textViewAction;

    //End Region Private Properties

    //Region Default Methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moveorderscan_place);
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
        super.onDestroy();
        LocalBroadcastManager.getInstance(cAppExtension.context).unregisterReceiver(mNumberReceiver);
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

        this.moveorderPickContainer = findViewById(R.id.pickorderPickContainer);
        this.probeersel = findViewById(R.id.probeersel);
        this.toolbarImage = findViewById(R.id.toolbarImage);
        this.toolbarTitle = findViewById(R.id.toolbarTitle);
        this.toolbarSubtext = findViewById(R.id.toolbarSubtext);

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
        MoveorderScanPlaceActivity.mFieldsInitializeStatic();
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
            MoveorderScanPlaceActivity.mHandlePVScan(pvScannedBarcodeStr);
            return;
        }

        if (MoveorderScanPlaceActivity.mFindBarcodeInLineBarcodes(pvScannedBarcodeStr) == false) {
            cUserInterface.pDoExplodingScreen(cAppExtension.context.getString(R.string.error_unknown_barcode), pvScannedBarcodeStr, true, true);
            return;
        }

        //If we found the barcode, currentbarcode is alreay filled, so make this selected
        MoveorderScanPlaceActivity.mBarcodeSelected(cPickorderBarcode.currentPickorderBarcode);
        return;

    }

    public static void pAcceptPick() {
         cMoveorderLine.currentMoveOrderLine.pHandledIndatabaseBln();
         MoveorderScanPlaceActivity.mMoveDone();
    }

    public static void pCancelPick() {
        cMoveorderLine.currentMoveOrderLine.quantityHandledDbl = Double.valueOf(0);
        cMoveorderLine.currentMoveOrderLine.pCancelIndatabaseBln();
        MoveorderScanPlaceActivity.mGoBackToLinesActivity();
        return;
    }

    public  static  void pRegisterBarcodeReceiver(){
        cBarcodeScan.pRegisterBarcodeReceiver();
    }

    //End Region Public Methods

    //Region Private Methods

    //Views
    private static void mFieldsInitializeStatic(){

        MoveorderScanPlaceActivity.imageButtonDone.setImageResource(R.drawable.ic_check_black_24dp);

        MoveorderScanPlaceActivity.moveCounterPlusHelperInt = 0;
        MoveorderScanPlaceActivity.moveCounterMinusHelperInt = 0;
        MoveorderScanPlaceActivity.toolbarSubtext.setText(cMoveorder.currentMoveOrder.getOrderNumberStr());

        MoveorderScanPlaceActivity.articleDescriptionText.setText(cMoveorderLine.currentMoveOrderLine.getDescriptionStr());
        MoveorderScanPlaceActivity.articleDescription2Text.setText(cMoveorderLine.currentMoveOrderLine.getDescription2Str());
        MoveorderScanPlaceActivity.articleItemText.setText(cMoveorderLine.currentMoveOrderLine.getItemNoStr() + " " + cMoveorderLine.currentMoveOrderLine.getVariantCodeStr());
        MoveorderScanPlaceActivity.articleVendorItemText.setText(cMoveorderLine.currentMoveOrderLine.getVendorItemNoStr() + ' ' + cMoveorderLine.currentMoveOrderLine.getVendorItemDescriptionStr());

        MoveorderScanPlaceActivity.binText.setText(cMoveorderLine.currentMoveOrderLine.getBinCodeStr());
        MoveorderScanPlaceActivity.textViewAction.setText(cAppExtension.context.getString(R.string.scan_article));

        MoveorderScanPlaceActivity.containerText.setVisibility(View.GONE);
        MoveorderScanPlaceActivity.containerText.setText(cMoveorderLine.currentMoveOrderLine.getContainerStr());
        MoveorderScanPlaceActivity.containerText.setText("");
        MoveorderScanPlaceActivity.quantityText.setText("0");


        MoveorderScanPlaceActivity.mEnablePlusMinusAndBarcodeSelectViews();
        MoveorderScanPlaceActivity.mShowArticleImage();
        MoveorderScanPlaceActivity.mShowOrHideGenericExtraFields();
        MoveorderScanPlaceActivity.mShowBarcodeInfo();
        MoveorderScanPlaceActivity.mShowSortingInstruction();

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

                //If we only have one barcode, then automatticaly select that barcode
                if (cPickorderLine.currentPickOrderLine.barcodesObl.size() == 1) {
                    MoveorderScanPlaceActivity.pHandleScan(cPickorderLine.currentPickOrderLine.barcodesObl.get(0).getBarcodeStr());
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
            MoveorderScanPlaceActivity.articleBarcodeText.setText(cPickorderBarcode.currentPickorderBarcode.getBarcodeStr() + " (" + cPickorderBarcode.currentPickorderBarcode.getQuantityPerUnitOfMeasureDbl().intValue() + ")");
        } else {
            MoveorderScanPlaceActivity.articleBarcodeText.setText(cAppExtension.context.getString(R.string.mutiple_barcodes_posible));
        }
    }

    private static void mShowArticleImage() {

        //If pick with picture is false, then hide image view
        if (cPickorder.currentPickOrder.isPickWithPictureBln() == false) {
            MoveorderScanPlaceActivity.articleThumbImageView.setVisibility(View.INVISIBLE);
            return;
        }

        //If picture is not in cache (via webservice) then show no image
        if (cPickorderLine.currentPickOrderLine.pGetArticleImageBln() == false) {
            cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.could_not_get_article_image), null);
            MoveorderScanPlaceActivity.articleThumbImageView.setImageDrawable(ContextCompat.getDrawable(cAppExtension.context, R.drawable.ic_no_image_lightgrey_24dp));
            return;
        }

        //If picture is in cache but can't be converted, then show no image
        if (cPickorderLine.currentPickOrderLine.articleImage == null || cPickorderLine.currentPickOrderLine.articleImage.imageBitmap() == null) {
            cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.could_not_get_article_image), null);
            MoveorderScanPlaceActivity.articleThumbImageView.setImageDrawable(ContextCompat.getDrawable(cAppExtension.context, R.drawable.ic_no_image_lightgrey_24dp));
            return;
        }

        //Show the image
        MoveorderScanPlaceActivity.articleThumbImageView.setImageBitmap(cPickorderLine.currentPickOrderLine.articleImage.imageBitmap());

        //Open the image
        if ((cPickorder.currentPickOrder.isPickWithPictureAutoOpenBln())) {
            MoveorderScanPlaceActivity.mShowFullArticleFragment();
        }

    }

    private static void mShowSortingInstruction() {

        //First show the SourceNumber
        MoveorderScanPlaceActivity.sourcenoText.setText(cPickorderLine.currentPickOrderLine.getSourceNoStr());
        MoveorderScanPlaceActivity.sourcenoContainer.setVisibility(View.VISIBLE);

        if (cPickorderLine.currentPickOrderLine.getSourceNoStr().trim().isEmpty()) {
            MoveorderScanPlaceActivity.sourcenoContainer.setVisibility(View.INVISIBLE);
        }

        //If workflow is not PV, then d we are ready
        if (!cPickorder.currentPickOrder.isPVBln()) {
            return;
        }

        //Set scan instruction
        MoveorderScanPlaceActivity.textViewAction.setText(cAppExtension.context.getString(R.string.scan_article));

        // We already have a processing sequence, show it and insert a SalesOrderPackingTable in database
        if (cPickorderLine.currentPickOrderLine.getProcessingSequenceStr().isEmpty() == false) {
            MoveorderScanPlaceActivity.sourcenoText.setText(cPickorderLine.currentPickOrderLine.getProcessingSequenceStr());
            MoveorderScanPlaceActivity.mAddSalesOrderPackingTableBln();
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
            MoveorderScanPlaceActivity.sourcenoText.setText(recordForSalesOrder.getPackingtableStr());
            MoveorderScanPlaceActivity.textViewAction.setText(cAppExtension.context.getString(R.string.scan_pickcartbox));
            return;
        }
    }

    private static void mEnablePlusMinusAndBarcodeSelectViews() {

        if (!cSetting.PICK_PER_SCAN()) {
            MoveorderScanPlaceActivity.imageButtonMinus.setVisibility(View.INVISIBLE);
            MoveorderScanPlaceActivity.imageButtonPlus.setVisibility(View.INVISIBLE);
        } else {
            MoveorderScanPlaceActivity.imageButtonMinus.setVisibility(View.VISIBLE);
            MoveorderScanPlaceActivity.imageButtonPlus.setVisibility(View.VISIBLE);
        }

        if (!cSetting.PICK_SELECTEREN_BARCODE()) {
            MoveorderScanPlaceActivity.imageButtonBarcode.setVisibility(View.INVISIBLE);
        } else {
            MoveorderScanPlaceActivity.imageButtonBarcode.setVisibility(View.VISIBLE);
        }

    }

    //Scans and manual input

    private void mNumberClicked() {

        if (cPickorderBarcode.currentPickorderBarcode == null) {
            cUserInterface.pDoNope(quantityText, false, false);
            cUserInterface.pShowSnackbarMessage(moveorderPickContainer, getString(R.string.choose_barcode_first), null, false);
            return;
        }

        if (cPickorderBarcode.currentPickorderBarcode.getQuantityHandledDbl() > 1) {
            cUserInterface.pDoNope(quantityText, true, true);
            cUserInterface.pShowSnackbarMessage(moveorderPickContainer, getString(R.string.manual_input_only_barcodenumber_bigger1), null, false);
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
            MoveorderScanPlaceActivity.mHandlePVArticleScanned(pvScannedBarcodeStr);
            return;
        }

        MoveorderScanPlaceActivity.mHandleSalesOrderOrPickCartScanned(pvScannedBarcodeStr);
        return;

    }

    private static void mTryToChangePickedQuantity(Boolean pvIsPositiveBln, Boolean pvAmountFixedBln, double pvAmountDbl) {

      double newQuantityDbl;

        if (pvIsPositiveBln) {

            //Determine the new amount
            if (pvAmountFixedBln == true) {
                articleScannedLastBln = true;
                newQuantityDbl = pvAmountDbl;
            } else {
                newQuantityDbl = cPickorderLine.currentPickOrderLine.getQuantityHandledDbl() + pvAmountDbl;
            }

            //Check if we would exceed amount, then show message
            if (newQuantityDbl > cPickorderLine.currentPickOrderLine.getQuantityDbl()) {
                MoveorderScanPlaceActivity.mShowOverpickNotAllowed();
                articleScannedLastBln = false;
                return;
            }

            //Set the new quantity and show in Activity
            cPickorderLine.currentPickOrderLine.quantityHandledDbl = newQuantityDbl;
            MoveorderScanPlaceActivity.quantityText.setText(cText.pDoubleToStringStr(cPickorderLine.currentPickOrderLine.getQuantityHandledDbl()));

            //Add or update line barcode
            cPickorderLine.currentPickOrderLine.pAddOrUpdateLineBarcodeBln(pvAmountDbl);

            //Update orderline info (quantity, timestamp, localstatus)
            cPickorderLine.currentPickOrderLine.pHandledIndatabaseBln();

            //Check if this line is done
            MoveorderScanPlaceActivity.mCheckLineDone();
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

        MoveorderScanPlaceActivity.quantityText.setText(cText.pDoubleToStringStr(cPickorderLine.currentPickOrderLine.getQuantityHandledDbl()));
        MoveorderScanPlaceActivity.imageButtonDone.setImageResource(R.drawable.ic_check_black_24dp);

        //Remove or update line barcode
        cPickorderLine.currentPickOrderLine.pRemoveOrUpdateLineBarcodeBln();
        return;

    }

    private static void mHandleSalesOrderOrPickCartScanned(String pvScannedBarcodeStr) {

        //Strip barcode from regex
        String barcodeWithoutPrefixStr = cRegex.pStripRegexPrefixStr(pvScannedBarcodeStr);

        // Check if article is already scanned
        if (MoveorderScanPlaceActivity.articleScannedLastBln == false) {
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
        if (MoveorderScanPlaceActivity.mCheckPackingTableAndSourceNoBln(barcodeWithoutPrefixStr) == false) {
            return;
        }

        //Set the Processing Sequence
        cPickorderLine.currentPickOrderLine.processingSequenceStr = barcodeWithoutPrefixStr;

        //try to add SalesOrderPackingtable to database
        if (MoveorderScanPlaceActivity.mAddSalesOrderPackingTableBln() == false) {
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
        MoveorderScanPlaceActivity.sourcenoText.setText(cPickorderLine.currentPickOrderLine.processingSequenceStr);

        // If this is VKO after each piece, then show new instructions
        if (cPickorder.currentPickOrder.isPickPickPVVKOEachPieceBln() == true) {
            MoveorderScanPlaceActivity.imageButtonDone.setVisibility(View.VISIBLE);
            MoveorderScanPlaceActivity.textViewAction.setText(cAppExtension.context.getString(R.string.scan_article));
            MoveorderScanPlaceActivity.articleScannedLastBln = false;
        }

        //Update orderline info (quantity, timestamp, localstatus)
        cPickorderLine.currentPickOrderLine.pHandledIndatabaseBln();
        articleScannedLastBln = false;

        //Check if line is done
        MoveorderScanPlaceActivity.mCheckLineDone();
        return;


    }

    private static void mHandlePVArticleScanned(String pvScannedBarcodeStr) {

        //We didn't scan an article yet, so handle it as a "normal" scan
        //We can scan article multiple times
        if (articleScannedLastBln == false || !cPickorder.currentPickOrder.isPickPickPVVKOEachPieceBln() ) {

            if (MoveorderScanPlaceActivity.mFindBarcodeInLineBarcodes(pvScannedBarcodeStr) == false) {
                cUserInterface.pDoExplodingScreen(cAppExtension.context.getString(R.string.error_unknown_barcode), pvScannedBarcodeStr, true, true);
                return;
            }

            //Succesfull article scanned
            articleScannedLastBln = true;

            //If we found the barcode, currentbarcode is alreay filled, so make this selected
            MoveorderScanPlaceActivity.mBarcodeSelected(cPickorderBarcode.currentPickorderBarcode);
            return;
        }

        //You have to scan a pickcart or salesorder after the last article scan
        cUserInterface.pDoExplodingScreen(cAppExtension.context.getString(R.string.message_scan_pickcart_or_salesorder), pvScannedBarcodeStr, true, true);
        return;

    }

    private static void mBarcodeSelected(cPickorderBarcode pvBarcode) {

        cUserInterface.pCheckAndCloseOpenDialogs();

        cPickorderBarcode.currentPickorderBarcode = pvBarcode;
        MoveorderScanPlaceActivity.mShowBarcodeInfo();
        MoveorderScanPlaceActivity.mTryToChangePickedQuantity(true, false, cPickorderBarcode.currentPickorderBarcode.getQuantityPerUnitOfMeasureDbl());
        return;
    }

    // Lines, Barcodes and Packing Tables

    private static Boolean mFindBarcodeInLineBarcodes(String pvScannedBarcodeStr) {

        if (cPickorderLine.currentPickOrderLine.barcodesObl == null || cPickorderLine.currentPickOrderLine.barcodesObl.size() == 0) {
            return false;
        }

        for (cPickorderBarcode pickorderBarcode : cPickorderLine.currentPickOrderLine.barcodesObl) {

            if (pickorderBarcode.getBarcodeStr().equalsIgnoreCase(pvScannedBarcodeStr) || pickorderBarcode.getBarcodeWithoutCheckDigitStr().equalsIgnoreCase(pvScannedBarcodeStr)) {
                cPickorderBarcode.currentPickorderBarcode = pickorderBarcode;
                return true;
            }
        }

        return false;
    }

    private static void mCheckLineDone() {

        //Start with complete
        Boolean incompleteBln = false;
        MoveorderScanPlaceActivity.imageButtonDone.setVisibility(View.VISIBLE);


        //Check if quantity is sufficient
        if (cPickorderLine.currentPickOrderLine.quantityHandledDbl < cPickorderLine.currentPickOrderLine.quantityDbl) {
            MoveorderScanPlaceActivity.imageButtonDone.setImageResource(R.drawable.ic_check_black_24dp);
            incompleteBln = true;
        }

        //PV
        if (cPickorder.currentPickOrder.isPVBln()) {

            if (incompleteBln) {
                MoveorderScanPlaceActivity.imageButtonDone.setVisibility(View.INVISIBLE);
            }

            //We have to scan a pickcart/salesorder after each article scan
            if (cPickorder.currentPickOrder.isPickPickPVVKOEachPieceBln()) {

                //Not complete and article last scanned so we have to scan a pickcart/salesorder, set the instruction
                if (articleScannedLastBln == true && incompleteBln == true) {
                    MoveorderScanPlaceActivity.imageButtonDone.setImageResource(R.drawable.ic_check_black_24dp);
                    MoveorderScanPlaceActivity.textViewAction.setText(cAppExtension.context.getString(R.string.message_scan_pickcart_or_salesorder));
                    return;
                }

                //Not complete and pickcart/salesorder last scanned so we have to scan an article, set the instruction
                if (articleScannedLastBln == false && incompleteBln == true) {
                    MoveorderScanPlaceActivity.imageButtonDone.setImageResource(R.drawable.ic_check_black_24dp);
                    MoveorderScanPlaceActivity.textViewAction.setText(cAppExtension.context.getString(R.string.scan_article));
                    MoveorderScanPlaceActivity.imageButtonDone.setVisibility(View.VISIBLE);
                    return;
                }

                //We have to scan a processingsequence to complete the line, so we are not ready
                if (articleScannedLastBln == true && incompleteBln == false) {
                    MoveorderScanPlaceActivity.imageButtonDone.setImageResource(R.drawable.ic_check_black_24dp);
                    MoveorderScanPlaceActivity.textViewAction.setText(cAppExtension.context.getString(R.string.message_scan_pickcart_or_salesorder));
                    return;
                }

                //We are complete, so we are done, will be detected later

            }

            //We can scan all articles first and then pickcart/salesorder, so set this as the instruction
            if (incompleteBln && cPickorderLine.currentPickOrderLine.processingSequenceStr.isEmpty()) {
                MoveorderScanPlaceActivity.textViewAction.setText(cAppExtension.context.getString(R.string.scan_article_or_salesorder_or_pickcartbox));
                return;
            }

            // We reached the quantity, but still have to scan the pickcart/salesorder, so set the instruction
            if (cPickorderLine.currentPickOrderLine.processingSequenceStr.isEmpty()) {
                MoveorderScanPlaceActivity.imageButtonDone.setImageResource(R.drawable.ic_check_black_24dp);
                MoveorderScanPlaceActivity.textViewAction.setText(cAppExtension.context.getString(R.string.message_scan_pickcart_or_salesorder));
                MoveorderScanPlaceActivity.imageButtonDone.setVisibility(View.INVISIBLE);
                return;
            }

            //We picked less then required, and we have a processing sequence so we are done anyway
            if (incompleteBln && !cPickorderLine.currentPickOrderLine.processingSequenceStr.isEmpty() && !cPickorder.currentPickOrder.isPickPickPVVKOEachPieceBln() ) {
                incompleteBln = false;
            }

        }

        //If we are incomplete, we are done here
        if (incompleteBln)  {
            return;
        }

        if (cSetting.PICK_AUTO_ACCEPT() == false) {
            MoveorderScanPlaceActivity.imageButtonDone.setVisibility(View.VISIBLE);
            MoveorderScanPlaceActivity.imageButtonDone.setImageResource(R.drawable.ic_doublecheck_black_24dp);
            return;
        }

        MoveorderScanPlaceActivity.articleScannedLastBln = false;
        MoveorderScanPlaceActivity.mMoveDone();
    }

    private static void mGetNextPickLineFromLocation() {

        cResult hulpResult;

        //check if there is a next line for this BIN
        cPickorderLine nextLine = cPickorder.currentPickOrder.pGetNetxLineToHandleForBin(cPickorderLine.currentPickOrderLine.getBinCodeStr());

       //There is no next line, so close this activity
        if (nextLine == null) {
            //Clear current barcode and reset defaults
            cPickorderLine.currentPickOrderLine = null;
            MoveorderScanPlaceActivity.articleScannedLastBln = false;
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

        MoveorderScanPlaceActivity.mNextLineForBin();
        return;

    }

    private static void mNextLineForBin() {

        //Play a sound
        cUserInterface.pPlaySound(R.raw.message, null);

        //Clear current barcode and reset defaults
        cPickorderBarcode.currentPickorderBarcode = null;
        MoveorderScanPlaceActivity.articleScannedLastBln = false;

        //Show animation and initialize fields
        Animation animation = AnimationUtils.loadAnimation(cAppExtension.context.getApplicationContext(), R.anim.shrink_and_fade);
        MoveorderScanPlaceActivity.probeersel.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                MoveorderScanPlaceActivity.mFieldsInitializeStatic();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


    }

    private static Boolean mSendPickorderLine() {

        //If internet is not connected
        if (!cConnection.isInternetConnectedBln()) {
            //could not send line, let user know but answer succes so user can go to next line
            cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.couldnt_send_line), null);
            cPickorderLine.currentPickOrderLine.pErrorSendingBln();
            return true;
        }


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

    private void mHandleMoveDoneClick() {

//        //If workflow is then call specific void
//        if (cPickorder.currentPickOrder.isPVBln() == true) {
//            this.mHandlePVDone();
//            return;
//        }
//
//        //Check if we picked less then asked, if so then show dialog
//        if (cPickorderLine.currentPickOrderLine.getQuantityHandledDbl() != cPickorderLine.currentPickOrderLine.getQuantityDbl()) {
//            this.mShowUnderPickDialog();
//            return;
//        }

        //All is done
        this.mMoveDone();

    }


    private static void mMoveDone() {

        MoveorderScanPlaceActivity.mSendPickorderLine();
        MoveorderScanPlaceActivity.mGetNextPickLineFromLocation();

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
                mHandleMoveDoneClick();
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
                    moveCounterPlusHelperInt = 0;
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


                mTryToChangePickedQuantity(true, false, cPickorderBarcode.currentPickorderBarcode.getQuantityPerUnitOfMeasureDbl());
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
                    moveCounterMinusHelperInt = 0;
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


                mTryToChangePickedQuantity(false, false, cPickorderBarcode.currentPickorderBarcode.getQuantityPerUnitOfMeasureDbl());
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

    //Dialogs and Activitys

    private static void mShowFullArticleFragment() {

        cUserInterface.pCheckAndCloseOpenDialogs();

        ArticleFullViewFragment articleFullViewFragment = new ArticleFullViewFragment();
        articleFullViewFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.ARTICLEFULL_TAG);



    }


    private void mShowAcceptFragment(){

//        cUserInterface.pCheckAndCloseOpenDialogs();
//
//        final AcceptRejectFragment acceptRejectFragment = new AcceptRejectFragment(cAppExtension.activity.getString(R.string.message_pickorderbusy_header),
//                                                                                   cAppExtension.activity.getString(R.string.message_pickorderbusy_text));
//        acceptRejectFragment.setCancelable(true);
//
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                // show my popup
//                acceptRejectFragment.show(cAppExtension.fragmentManager, ACCEPTREJECTFRAGMENT_TAG);
//            }
//        });
    }

    private static void mGoBackToLinesActivity() {
        Intent intent = new Intent(cAppExtension.context, MoveorderLinesActivity.class);
        cAppExtension.activity.startActivity(intent);
        cAppExtension.activity.finish();
    }

    private void mShowBarcodeSelectFragment() {
        BarcodeFragment barcodeFragment = new BarcodeFragment();
        barcodeFragment.show(cAppExtension.fragmentManager, BARCODEPICKERFRAGMENT_TAG);
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
        MoveorderScanPlaceActivity.quantityText.setText(quantityRequiredText.getText());
        cUserInterface.pShowSnackbarMessage(textViewAction , cAppExtension.context.getString(R.string.number_cannot_be_higher), null, false);
        cUserInterface.pDoNope(quantityText, true, true);
        cUserInterface.pDoNope(quantityRequiredText, false, false);
        return;
    }

    //Region Number Broadcaster

    private Runnable mMinusAction = new Runnable() {
        @Override
        public void run() {
            imageButtonMinus.performClick();
            long milliSecsLng;
            if (moveCounterMinusHelperInt < 10) {
                milliSecsLng = 200;
            } else if (moveCounterMinusHelperInt < 20) {
                milliSecsLng = 150;
            } else if (moveCounterMinusHelperInt < 30) {
                milliSecsLng = 100;
            } else if (moveCounterMinusHelperInt < 40) {
                milliSecsLng = 50;
            } else {
                milliSecsLng = 50;
            }
            mDoDelayedMinus(this, milliSecsLng);
        }
    };

    private Runnable mPlusAction = new Runnable() {
        @Override
        public void run() {
            imageButtonPlus.performClick();
            long milliSecsLng;
            if (moveCounterPlusHelperInt < 10) {
                milliSecsLng = 200;
            } else if (moveCounterPlusHelperInt < 20) {
                milliSecsLng = 150;
            } else if (moveCounterPlusHelperInt < 30) {
                milliSecsLng = 100;
            } else if (moveCounterPlusHelperInt < 40) {
                milliSecsLng = 50;
            } else {
                milliSecsLng = 50;
            }
            mDoDelayedPlus(this, milliSecsLng);
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
        this.moveCounterMinusHelperInt += 1;
        return;
    }

    private void mDoDelayedPlus(Runnable pvRunnable, long pvMilliSecsLng) {
        this.plusHandler.postDelayed(pvRunnable, pvMilliSecsLng);
        this.moveCounterPlusHelperInt += 1;
        return;
    }

    //End Region Number Broadcaster

    //End Regin Private Methods

}

