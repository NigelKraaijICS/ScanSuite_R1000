package nl.icsvertex.scansuite.Activities.Pick;

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
import java.util.List;
import java.util.Objects;

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
import SSU_WHS.Picken.PickorderBarcodes.cPickorderBarcode;
import SSU_WHS.Picken.PickorderLineBarcodes.cPickorderLineBarcode;
import SSU_WHS.Picken.PickorderLines.cPickorderLine;
import SSU_WHS.Picken.Pickorders.cPickorder;
import SSU_WHS.Picken.SalesOrderPackingTable.cSalesOrderPackingTable;
import nl.icsvertex.scansuite.Fragments.Dialogs.AcceptRejectFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.ArticleFullViewFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.BarcodeFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.NumberpickerFragment;
import nl.icsvertex.scansuite.R;

public class PickorderPickActivity extends AppCompatActivity implements iICSDefaultActivity {

    //Region Private Properties


    private static boolean articleScannedLastBln;
    private static boolean destionationScannedBln;

    private int pickCounterMinusHelperInt;
    private int pickCounterPlusHelperInt;

    private Handler minusHandler;
    private Handler plusHandler;

    private  ConstraintLayout pickorderPickContainer;

    private  ImageView toolbarImage;
    private  TextView toolbarTitle;
    private  TextView toolbarSubtext;

    private  TextView articleDescriptionText;
    private  TextView articleDescription2Text;
    private  TextView articleItemText;
    private  TextView articleBarcodeText;
    private  TextView articleVendorItemText;
    private  TextView genericItemExtraField1Text;
    private  TextView genericItemExtraField2Text;
    private  TextView genericItemExtraField3Text;
    private  TextView genericItemExtraField4Text;
    private  TextView genericItemExtraField5Text;
    private  TextView genericItemExtraField6Text;
    private  TextView genericItemExtraField7Text;
    private  TextView genericItemExtraField8Text;
    private  TextView binText;

    private  TextView quantityText;
    private  TextView quantityRequiredText;

    private  ImageView articleThumbImageView;
    private  ImageView imageButtonBarcode;

    private  TextView sourcenoText;
    private  CardView sourcenoContainer;

    private  TextView destinationText;
    private  CardView destinationContainer;

    private  AppCompatImageButton imageButtonMinus;
    private  AppCompatImageButton imageButtonPlus;
    private  AppCompatImageButton imageButtonDone;
    private  TextView textViewAction;


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
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(cAppExtension.context).registerReceiver(mNumberReceiver, new IntentFilter(cPublicDefinitions.NUMBERINTENT_NUMBER));
        cBarcodeScan.pRegisterBarcodeReceiver();
        cUserInterface.pEnableScanner();
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            this.mShowAcceptFragment();
            return true;
        }

        return super.onOptionsItemSelected(item);

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

        this.pickorderPickContainer = findViewById(R.id.pickorderPickContainer);

        this.toolbarImage = findViewById(R.id.toolbarImage);
        this.toolbarTitle = findViewById(R.id.toolbarTitle);
        this.toolbarSubtext = findViewById(R.id.toolbarSubtext);

        this.articleDescriptionText = findViewById(R.id.articleDescriptionText);
        this.articleDescription2Text = findViewById(R.id.articleDescription2Text);
        this.articleItemText = findViewById(R.id.articleItemText);
        this.articleBarcodeText = findViewById(R.id.articleBarcodeText);
        this.articleVendorItemText = findViewById(R.id.articleVendorItemText);
        this.binText = findViewById(R.id.binText);
        this.genericItemExtraField1Text = findViewById(R.id.genericItemExtraField1Text);
        this.genericItemExtraField2Text = findViewById(R.id.genericItemExtraField2Text);
        this.genericItemExtraField3Text = findViewById(R.id.genericItemExtraField3Text);
        this.genericItemExtraField4Text = findViewById(R.id.genericItemExtraField4Text);
        this.genericItemExtraField5Text = findViewById(R.id.genericItemExtraField5Text);
        this.genericItemExtraField6Text = findViewById(R.id.genericItemExtraField6Text);
        this.genericItemExtraField7Text = findViewById(R.id.genericItemExtraField7Text);
        this.genericItemExtraField8Text = findViewById(R.id.genericItemExtraField8Text);

        this.sourcenoText = findViewById(R.id.sourcenoText);
        this.sourcenoContainer = findViewById(R.id.sourceNoContainer);

        this.destinationText = findViewById(R.id.destinationText);
        this.destinationContainer = findViewById(R.id.destinationContainer);

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
        this.toolbarImage.setImageResource(R.drawable.ic_menu_pick);
        this.toolbarTitle.setText(pvScreenTitle);
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

        this.imageButtonDone.setImageResource(R.drawable.ic_check_black_24dp);

        this.pickCounterPlusHelperInt = 0;
        this.pickCounterMinusHelperInt = 0;

        if (cPickorderLine.currentPickOrderLine == null) {
            return;
        }

        this.toolbarSubtext.setText(cPickorder.currentPickOrder.getOrderNumberStr());

        this.articleDescriptionText.setText(cPickorderLine.currentPickOrderLine.getDescriptionStr());
        this.articleDescription2Text.setText(cPickorderLine.currentPickOrderLine.getDescription2Str());
        this.articleItemText.setText(cPickorderLine.currentPickOrderLine.getItemNoAndVariantStr());
        this.articleVendorItemText.setText(cPickorderLine.currentPickOrderLine.getVendorItemNoAndDescriptionStr());

        this.binText.setText(cPickorderLine.currentPickOrderLine.getBinCodeStr());



        this.quantityRequiredText.setText(cText.pDoubleToStringStr(cPickorderLine.currentPickOrderLine.getQuantityDbl()));

        this.mEnablePlusMinusAndBarcodeSelectViews();
        this.mShowArticleImage();
        this.mShowOrHideGenericExtraFields();

        this.mShowBarcodeInfo();
        this.mShowQuantityInfo();
        this.mShowSortingInstruction();
        this.mShowDestination();

        this.mCheckLineDone();
    }

    @Override
    public void mInitScreen() {

        cBarcodeScan.pRegisterBarcodeReceiver();

        PickorderPickActivity.destionationScannedBln = cPickorder.currentPickOrder.destionationBranch() != null;

        //We scanned a BIN, so nu current barcodeStr known
        if (cPickorderBarcode.currentPickorderBarcode == null) {
            //Initialise article scanned boolean
            PickorderPickActivity.articleScannedLastBln = false;
            return;
        }

         // We scanned an ARTICLE, so handle barcide

        if (cSetting.PICK_BIN_IS_ITEM() || cPickorder.currentPickOrder.isSingleBinBln()) {
            PickorderPickActivity.articleScannedLastBln = false;
            this.pHandleScan(cBarcodeScan.pFakeScan(cPickorderBarcode.currentPickorderBarcode.getBarcodeStr()));
        }

    }

    @Override
    public void mSetListeners() {

        this.mSetArticleImageListener();
        this.mSetImageButtonBarcodeListener();

        if (cSetting.PICK_SELECTEREN_BARCODE()) {
            this.mSetNumberListener();
        }

        this.mSetPlusListener();
        this.mSetMinusListener();
        this.mSetDoneListener();

    }

    // End Region iICSDefaultActivity Methods

    //Region Public Methods

    public  void pHandleScan(cBarcodeScan pvBarcodeScan) {

        cUserInterface.pCheckAndCloseOpenDialogs();

        if (!cPickorder.currentPickOrder.isPABln()) {
            PickorderPickActivity.destionationScannedBln = true;
        }

        //If we still need a destination scan, make sure we scan this first
        if (!PickorderPickActivity.destionationScannedBln) {
            cResult hulpRst = this.mCheckDestionationRst(pvBarcodeScan);
            if (! hulpRst.resultBln) {
                cUserInterface.pDoExplodingScreen(hulpRst.messagesStr(),"", true, true);
                return;
            }

            return;
        }

        if (cPickorder.currentPickOrder.isPVBln()) {
            this.mHandlePVScan(pvBarcodeScan);
            return;
        }

        if (!this.mFindBarcodeInLineBarcodes(pvBarcodeScan)) {
            cUserInterface.pDoExplodingScreen(cAppExtension.context.getString(R.string.error_unknown_barcode), pvBarcodeScan.getBarcodeOriginalStr(), true, true);
            return;
        }

        //If we found the barcodeStr, currentbarcode is alreay filled, so make this selected
        this.mBarcodeSelected(cPickorderBarcode.currentPickorderBarcode);

    }

    public  void pAcceptPick(boolean ignoreAccept) {
        if (ignoreAccept) {
            cUserInterface.pPlaySound(R.raw.headsupsound,null);
            cUserInterface.pDoBoing(this.textViewAction);
            return;
        }
             cPickorderLine.currentPickOrderLine.pHandledIndatabase();
        this.mPickDone();
    }

    public  void pAcceptRejectDialogDismissed() {

        if (!cPickorderLine.currentPickOrderLine.getProcessingSequenceStr().isEmpty() && !cPickorder.currentPickOrder.isPickPickPVVKOEachPieceBln()) {
            cPickorderLine.currentPickOrderLine.processingSequenceStr = "";
            cPickorderLine.currentPickOrderLine.pUpdateProcessingSequenceBln("");
            PickorderPickActivity.articleScannedLastBln = true;
        }

    }

    public  void pCancelPick() {
        //Check if we need to remove the SalesorderPackingTableLines
        if (cPickorderLine.currentPickOrderLine.pGetLinesForProcessingSequenceObl().size() <= 1)  {
            cSalesOrderPackingTable.pDeleteFromDatabaseBln(cPickorderLine.currentPickOrderLine.getProcessingSequenceStr());
        }
        cPickorderLine.currentPickOrderLine.pUpdateProcessingSequenceBln("");
        cPickorderLine.currentPickOrderLine.pCancelIndatabase();
        this.mGoBackToLinesActivity();
    }

    public  void pRegisterBarcodeReceiver(){
        cBarcodeScan.pRegisterBarcodeReceiver();
    }

    //End Region Public Methods

    //Region Private Methods

    //Views


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
                    pHandleScan(cBarcodeScan.pFakeScan(cPickorderLine.currentPickOrderLine.barcodesObl.get(0).getBarcodeStr()));
                    return;
                }

                mShowBarcodeSelectFragment();
            }
        });
    }

    private  void mShowOrHideGenericExtraFields() {

        if (!cPickorderLine.currentPickOrderLine.getExtraField1Str().isEmpty()) {
            this.genericItemExtraField1Text.setVisibility(View.VISIBLE);
            this.genericItemExtraField1Text.setText(cPickorderLine.currentPickOrderLine.getExtraField1Str());
        }

        if (!cPickorderLine.currentPickOrderLine.getExtraField2Str().isEmpty()) {
            this.genericItemExtraField2Text.setVisibility(View.VISIBLE);
            this.genericItemExtraField2Text.setText(cPickorderLine.currentPickOrderLine.getExtraField2Str());
        }

        if (!cPickorderLine.currentPickOrderLine.getExtraField3Str().isEmpty()) {
            this.genericItemExtraField3Text.setVisibility(View.VISIBLE);
            this.genericItemExtraField3Text.setText(cPickorderLine.currentPickOrderLine.getExtraField3Str());
        }

        if (!cPickorderLine.currentPickOrderLine.getExtraField4Str().isEmpty()) {
            this.genericItemExtraField4Text.setVisibility(View.VISIBLE);
            this.genericItemExtraField4Text.setText(cPickorderLine.currentPickOrderLine.getExtraField4Str());
        }

        if (!cPickorderLine.currentPickOrderLine.getExtraField5Str().isEmpty()) {
            this.genericItemExtraField5Text.setVisibility(View.VISIBLE);
            this.genericItemExtraField5Text.setText(cPickorderLine.currentPickOrderLine.getExtraField5Str());
        }

        if (!cPickorderLine.currentPickOrderLine.getExtraField6Str().isEmpty()) {
            this.genericItemExtraField6Text.setVisibility(View.VISIBLE);
            this.genericItemExtraField6Text.setText(cPickorderLine.currentPickOrderLine.getExtraField6Str());
        }

        if (!cPickorderLine.currentPickOrderLine.getExtraField7Str().isEmpty()) {
            this.genericItemExtraField7Text.setVisibility(View.VISIBLE);
            this.genericItemExtraField7Text.setText(cPickorderLine.currentPickOrderLine.getExtraField7Str());
        }

        if (!cPickorderLine.currentPickOrderLine.getExtraField8Str().isEmpty()) {
            this. genericItemExtraField8Text.setVisibility(View.VISIBLE);
            this. genericItemExtraField8Text.setText(cPickorderLine.currentPickOrderLine.getExtraField8Str());
        }

    }

    private  void mShowBarcodeInfo() {

        if (cPickorderBarcode.currentPickorderBarcode == null) {
            if (cPickorderLine.currentPickOrderLine.barcodesObl.size() == 1) {
                cPickorderBarcode.currentPickorderBarcode = cPickorderLine.currentPickOrderLine.barcodesObl.get(0);
            }
        }

        if (cPickorderBarcode.currentPickorderBarcode != null) {
            this.articleBarcodeText.setText(cPickorderBarcode.currentPickorderBarcode.getBarcodeAndQuantityStr());
        } else {
            this.articleBarcodeText.setText(cAppExtension.context.getString(R.string.mutiple_barcodes_posible));
        }
    }

    private void mShowQuantityInfo(){

        double quantityDbl = 0;

        if (cPickorderLine.currentPickOrderLine.handledBarcodesObl().size() > 0) {
            for (cPickorderLineBarcode pickorderLineBarcode : cPickorderLine.currentPickOrderLine.handledBarcodesObl())

                quantityDbl += pickorderLineBarcode.getQuantityhandledDbl();
        }

        this.quantityText.setText(cText.pDoubleToStringStr(quantityDbl));


    }

    private  void mShowArticleImage() {

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

        //Open the image
        if ((cPickorder.currentPickOrder.isPickWithPictureAutoOpenBln())) {
            this.mShowFullArticleFragment();
        }
    }

    private  void mShowSortingInstruction() {

        //First show the SourceNumber
        this.sourcenoText.setText(cPickorderLine.currentPickOrderLine.getSourceNoStr());
        this.sourcenoContainer.setVisibility(View.VISIBLE);

        if (cPickorderLine.currentPickOrderLine.getSourceNoStr().trim().isEmpty()) {
            this.sourcenoContainer.setVisibility(View.INVISIBLE);
        }

        //If workflow is not PV, then d we are ready
        if (!cPickorder.currentPickOrder.isPVBln()) {
            return;
        }

        // We already have a processing sequence, show it and pInsertInDatabase a SalesOrderPackingTable in database
        if (!cPickorderLine.currentPickOrderLine.getProcessingSequenceStr().isEmpty()) {
            this.sourcenoText.setText(cPickorderLine.currentPickOrderLine.getProcessingSequenceStr());
            this.mAddSalesOrderPackingTableBln();
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
            this.sourcenoText.setText(recordForSalesOrder.getPackingtableStr());
        }
    }

    private void mShowDestination() {

        //First show the SourceNumber
        if (cPickorder.currentPickOrder.isTransferBln()) {
            this.destinationText.setText(cPickorderLine.currentPickOrderLine.getDestinationAndDescriptionStr());
            this.destinationContainer.setVisibility(View.VISIBLE);
            return;
        }

        this.destinationText.setText("");
        this.destinationContainer.setVisibility(View.GONE);

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

    //Scans and manual input

    private void mNumberClicked() {

        if (cSetting.PICK_PER_SCAN()) {
            return;
        }

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

    private  void mHandlePVScan(cBarcodeScan pvBarcodeScan) {

        boolean isSalesorderOrPickcartboxBln = false;

        //This barcode matches multiple lay-outs so this can be a BIN or an article
        if (Objects.requireNonNull(cBarcodeLayout.pGetBarcodeLayoutByBarcodeObl(pvBarcodeScan.getBarcodeOriginalStr())).size() > 1)
            if (cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(), cBarcodeLayout.barcodeLayoutEnu.SALESORDER) || cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(), cBarcodeLayout.barcodeLayoutEnu.PICKCARTBOX)) {
                isSalesorderOrPickcartboxBln = true;
            }

        if (!isSalesorderOrPickcartboxBln) {
            if (cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(), cBarcodeLayout.barcodeLayoutEnu.ARTICLE)) {
                this.mHandlePVArticleScanned(pvBarcodeScan);
                return;
            }
        }

        if (cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(), cBarcodeLayout.barcodeLayoutEnu.SALESORDER) || cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(), cBarcodeLayout.barcodeLayoutEnu.PICKCARTBOX) ) {
            this.mHandleSalesOrderOrPickCartScanned(pvBarcodeScan);
            return;
        }

        cUserInterface.pDoExplodingScreen(cAppExtension.activity.getString(R.string.message_unknown_barcode),"",true,true);


    }

    private  void mTryToChangePickedQuantity(Boolean pvIsPositiveBln, Boolean pvAmountFixedBln, double pvAmountDbl) {

      double newQuantityDbl;
        List<cPickorderLineBarcode>  hulpObl;
        if (pvIsPositiveBln) {

            //Determine the new amount
            if (pvAmountFixedBln) {

                //Check if we already have barcodes and clear them
                if (cPickorderLine.currentPickOrderLine.handledBarcodesObl().size() > 0 ) {

                    hulpObl = new ArrayList<>(cPickorderLine.currentPickOrderLine.handledBarcodesObl());

                    for (cPickorderLineBarcode pickorderLineBarcode : hulpObl) {
                        pickorderLineBarcode.pDeleteFromDatabaseBln();
                    }
                }

                PickorderPickActivity.articleScannedLastBln = true;
                newQuantityDbl = pvAmountDbl;
            } else {
                newQuantityDbl = cPickorderLine.currentPickOrderLine.getQuantityHandledDbl() + pvAmountDbl;
            }

            //Check if we would exceed amount, then show message
            if (newQuantityDbl > cPickorderLine.currentPickOrderLine.getQuantityDbl()) {
                this.mShowOverpickNotAllowed();

                if (cPickorderLine.currentPickOrderLine.getQuantityDbl() > 0) {
                    PickorderPickActivity.articleScannedLastBln = true;
                    return;
                }

                PickorderPickActivity.articleScannedLastBln = false;
                return;
            }

            //Set the new quantityDbl and show in Activity
            cPickorderLine.currentPickOrderLine.quantityHandledDbl = newQuantityDbl;
            this.quantityText.setText(cText.pDoubleToStringStr(cPickorderLine.currentPickOrderLine.getQuantityHandledDbl()));

            //Add or update line barcodeStr
            cPickorderLine.currentPickOrderLine.pAddOrUpdateLineBarcode(pvAmountDbl);

            //Update orderline info (quantityDbl, timestamp, localStatusInt)
            cPickorderLine.currentPickOrderLine.pHandledIndatabase();

            //Check if this line is done
            this.mCheckLineDone();
            return;
        }

        //negative
        if (cPickorderLine.currentPickOrderLine.getQuantityHandledDbl() == 0 ) {
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
            cPickorderLine.currentPickOrderLine.quantityHandledDbl = 0.0;
        }else {
            //Set the new quantityDbl and show in Activity
            cPickorderLine.currentPickOrderLine.quantityHandledDbl = newQuantityDbl;
        }

        this.quantityText.setText(cText.pDoubleToStringStr(cPickorderLine.currentPickOrderLine.getQuantityHandledDbl()));
        this.imageButtonDone.setImageResource(R.drawable.ic_check_black_24dp);

        //Remove or update line barcodeStr
        cPickorderLine.currentPickOrderLine.pRemoveOrUpdateLineBarcode();

    }

    private  void mHandleSalesOrderOrPickCartScanned(cBarcodeScan pvBarcodeScan) {

        //Strip barcodeStr from regex
        String barcodeWithoutPrefixStr = cRegex.pStripRegexPrefixStr(pvBarcodeScan.getBarcodeOriginalStr());

        // Check if article is already scanned
        if (!PickorderPickActivity.articleScannedLastBln) {
            // we've scanned a pickCart or a salesOrder, but we need an article
            cUserInterface.pDoExplodingScreen(cAppExtension.context.getString(R.string.message_scan_article_first), barcodeWithoutPrefixStr, true, true);
            return;
        }

        //Check if scanned barcodeStr is a SalesOrder or PickCartBox
        boolean isSalesOrderBln = cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(), cBarcodeLayout.barcodeLayoutEnu.SALESORDER);
        boolean isPickCartBoxBln = cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(), cBarcodeLayout.barcodeLayoutEnu.PICKCARTBOX);

        //If we scanned a salesorder, then check if it matches the SourceNo
        if (isSalesOrderBln) {
            //If scanned value doesn't match then we are done
            if (!barcodeWithoutPrefixStr.equalsIgnoreCase(cPickorderLine.currentPickOrderLine.getSourceNoStr())) {
                cUserInterface.pDoExplodingScreen(cAppExtension.context.getString(R.string.message_wrong_sourceno), barcodeWithoutPrefixStr, true, true);
                return;
            }
        }

        //If we scanned a pickcartbox, then check if it matches the ProcessingSequence if ProcessingSequence is not empty
        if (isPickCartBoxBln && !cPickorderLine.currentPickOrderLine.getProcessingSequenceStr().isEmpty()) {
            //If scanned value doesn't match then we are done
            if (!barcodeWithoutPrefixStr.equalsIgnoreCase(cPickorderLine.currentPickOrderLine.getProcessingSequenceStr())) {
                cUserInterface.pDoExplodingScreen(cAppExtension.context.getString(R.string.message_wrong_pickcartbox), barcodeWithoutPrefixStr, true, true);
                return;
            }
        }

        // There are  previous salesorder/pickcaerbox scans, so check if input is correct
        if (!this.mCheckPackingTableAndSourceNoBln(barcodeWithoutPrefixStr)) {
            return;
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
            this.imageButtonDone.setVisibility(View.VISIBLE);
            this.textViewAction.setText(cAppExtension.context.getString(R.string.message_scan_article));
            PickorderPickActivity.articleScannedLastBln = false;
        }

        //Update orderline info (quantityDbl, timestamp, localStatusInt)
        cPickorderLine.currentPickOrderLine.pHandledIndatabase();
        PickorderPickActivity.articleScannedLastBln = false;

        //Check if quantityDbl is sufficient
        if (cPickorderLine.currentPickOrderLine.quantityHandledDbl < cPickorderLine.currentPickOrderLine.getQuantityDbl()) {
            this.imageButtonDone.setImageResource(R.drawable.ic_check_black_24dp);
            this.mShowUnderPickDialog(cAppExtension.activity.getString(R.string.message_cancel_line), cAppExtension.activity.getString(R.string.message_accept_line));
            return;
        }


        //Check if line is done
        this.mCheckLineDone();

    }

    private  void mHandlePVArticleScanned(cBarcodeScan pvBarcodeScan) {

        //We didn't scan an article yet, so handle it as a "normal" scan
        //We can scan article multiple times
        if (!PickorderPickActivity.articleScannedLastBln || !cPickorder.currentPickOrder.isPickPickPVVKOEachPieceBln() ) {

            if (!this.mFindBarcodeInLineBarcodes(pvBarcodeScan)) {
                cUserInterface.pDoExplodingScreen(cAppExtension.context.getString(R.string.error_unknown_barcode), pvBarcodeScan.getBarcodeOriginalStr(), true, true);
                return;
            }

            //Succesfull article scanned
            PickorderPickActivity.articleScannedLastBln = true;

            //If we found the barcodeStr, currentbarcode is alreay filled, so make this selected
            this.mBarcodeSelected(cPickorderBarcode.currentPickorderBarcode);
            return;
        }

        //You have to scan a pickcart or salesorder after the last article scan
        cUserInterface.pDoExplodingScreen(cAppExtension.context.getString(R.string.message_scan_pickcart_or_salesorder), pvBarcodeScan.getBarcodeOriginalStr(), true, true);
    }

    private  void mBarcodeSelected(cPickorderBarcode pvBarcode) {

        cUserInterface.pCheckAndCloseOpenDialogs();

        cPickorderBarcode.currentPickorderBarcode = pvBarcode;
        this.mShowBarcodeInfo();
        this.mTryToChangePickedQuantity(true, false, cPickorderBarcode.currentPickorderBarcode.getQuantityPerUnitOfMeasureDbl());
    }

    // Lines, Barcodes, Packing Tables and destionation

    private  boolean mFindBarcodeInLineBarcodes(cBarcodeScan pvBarcodeScan) {

        if (cPickorderLine.currentPickOrderLine.barcodesObl == null || cPickorderLine.currentPickOrderLine.barcodesObl.size() == 0) {
            return false;
        }

        for (cPickorderBarcode pickorderBarcode : cPickorderLine.currentPickOrderLine.barcodesObl) {

            if (pickorderBarcode.getBarcodeStr().equalsIgnoreCase(pvBarcodeScan.getBarcodeOriginalStr()) || pickorderBarcode.getBarcodeWithoutCheckDigitStr().equalsIgnoreCase(pvBarcodeScan.getBarcodeFormattedStr())) {
                cPickorderBarcode.currentPickorderBarcode = pickorderBarcode;
                return true;
            }
        }
        return false;
    }

    private  cResult mCheckDestionationRst(cBarcodeScan pvBarcodeScan) {

        cResult resultRst = new cResult();

        if (PickorderPickActivity.destionationScannedBln) {
            resultRst.resultBln = true;
            return  resultRst;
        }

        if (cPickorder.currentPickOrder.destionationBranch() != null) {
            PickorderPickActivity.destionationScannedBln = true;
            resultRst.resultBln = true;
            return  resultRst;
        }

        if (pvBarcodeScan.getBarcodeOriginalStr().equalsIgnoreCase(cPickorderLine.currentPickOrderLine.getDestinationNoStr())) {
            PickorderPickActivity.destionationScannedBln = true;
            resultRst.resultBln = true;
            return  resultRst;
        }

        //Check if we have scanned a BIN and check if there are not handled linesInt for this BIN
        if (!cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(),cBarcodeLayout.barcodeLayoutEnu.LOCATION)) {
            PickorderPickActivity.destionationScannedBln = false;
            resultRst.resultBln = false;
            resultRst.pAddErrorMessage(cAppExtension.activity.getString(R.string.message_scan_is_not_location));
            return  resultRst;
        }

        //Strip regex
        String barcodewithoutPrefix = cRegex.pStripRegexPrefixStr(pvBarcodeScan.getBarcodeOriginalStr());

        //Check if destination is correct
        if (!barcodewithoutPrefix.equalsIgnoreCase(cPickorderLine.currentPickOrderLine.getDestinationNoStr())) {
            PickorderPickActivity.destionationScannedBln = false;
            resultRst.resultBln = false;
            resultRst.pAddErrorMessage(cAppExtension.activity.getString(R.string.message_location_incorrect));
            return  resultRst;
        }

        PickorderPickActivity.destionationScannedBln = true;
        resultRst.resultBln = true;
        return  resultRst;

    }

    private  void mCheckLineDone() {

        //Start with defaults
        boolean incompleteBln = false;
        this.imageButtonDone.setVisibility(View.VISIBLE);
        this.textViewAction.setText(cAppExtension.context.getString(R.string.message_scan_article));

        //Set instruction
        if (cPickorder.currentPickOrder.isPABln() && !PickorderPickActivity.destionationScannedBln) {
            this.textViewAction.setText(cAppExtension.context.getString(R.string.message_scan_destination));
        }

        //Check if quantityDbl is sufficient
        if (cPickorderLine.currentPickOrderLine.quantityHandledDbl < cPickorderLine.currentPickOrderLine.getQuantityDbl()) {
            this.imageButtonDone.setImageResource(R.drawable.ic_check_black_24dp);
            incompleteBln = true;
        }

        //PV
        if (cPickorder.currentPickOrder.isPVBln()) {

            //todo: check this
//            if (incompleteBln) {
//                PickorderPickActivity.imageButtonDone.setVisibility(View.INVISIBLE);+
//            }

            //todo: check if this code is still correct
            //We have to scan a pickcart/salesorder after each article scan
            if (cPickorder.currentPickOrder.isPickPickPVVKOEachPieceBln()) {

                //Not complete and article last scanned so we have to scan a pickcart/salesorder, set the instruction
                if (PickorderPickActivity.articleScannedLastBln && incompleteBln) {
                    this.imageButtonDone.setImageResource(R.drawable.ic_check_black_24dp);
                    this.textViewAction.setText(cAppExtension.context.getString(R.string.message_scan_pickcart_or_salesorder));
                    return;
                }

                //Not complete and pickcart/salesorder last scanned so we have to scan an article, set the instruction
                if (PickorderPickActivity.articleScannedLastBln && incompleteBln) {
                    this.imageButtonDone.setImageResource(R.drawable.ic_check_black_24dp);
                    this.textViewAction.setText(cAppExtension.context.getString(R.string.message_scan_article));
                    this.imageButtonDone.setVisibility(View.VISIBLE);
                    return;
                }

                //We have to scan a processingsequence to complete the line, so we are not ready
                if (PickorderPickActivity.articleScannedLastBln && !incompleteBln) {
                    this.imageButtonDone.setImageResource(R.drawable.ic_check_black_24dp);
                    this.textViewAction.setText(cAppExtension.context.getString(R.string.message_scan_pickcart_or_salesorder));
                    return;
                }

                //We are complete, so we are done, will be detected later
            }

            //We can scan all articles first and then pickcart/salesorder, so set this as the instruction
            if (incompleteBln && cPickorderLine.currentPickOrderLine.getProcessingSequenceStr().isEmpty()) {
                this.textViewAction.setText(cAppExtension.context.getString(R.string.scan_article_or_salesorder_or_pickcartbox));
                return;
            }

            // We reached the quantityDbl, but still have to scan the pickcart/salesorder, so set the instruction
            if (cPickorderLine.currentPickOrderLine.getProcessingSequenceStr().isEmpty()) {
                this.imageButtonDone.setImageResource(R.drawable.ic_check_black_24dp);
                this.textViewAction.setText(cAppExtension.context.getString(R.string.message_scan_pickcart_or_salesorder));
                this.imageButtonDone.setVisibility(View.INVISIBLE);
                return;
            }

            //We picked less then required, and we have a processing sequence so we are done anyway
            if (incompleteBln && !cPickorderLine.currentPickOrderLine.getProcessingSequenceStr().isEmpty() && !cPickorder.currentPickOrder.isPickPickPVVKOEachPieceBln() ) {
                incompleteBln = false;
            }

        }

        //If we are incomplete, we are done here
        if (incompleteBln)  {
            return;
        }

        if (!cSetting.PICK_AUTO_ACCEPT() || !cSetting.PICK_AUTO_NEXT() ) {
            this.imageButtonDone.setVisibility(View.VISIBLE);
            this.imageButtonDone.setImageResource(R.drawable.ic_doublecheck_black_24dp);
            return;
        }

        PickorderPickActivity.articleScannedLastBln = false;
        this.mPickDone();
    }

    private  void mGetNextPickLineForCurrentBin() {

        cResult hulpResult;

        //check if there is a next line for this BIN
        cPickorderLine nextLine = cPickorder.currentPickOrder.pGetNextLineToHandleForBin(cPickorderLine.currentPickOrderLine.getBinCodeStr());

       //There is no next line, so close this activity
        if (nextLine == null || ! cSetting.PICK_AUTO_NEXT()) {
            //Clear current barcodeStr and reset defaults
            cPickorderLine.currentPickOrderLine = null;
            PickorderPickActivity.articleScannedLastBln = false;
            this.mGoBackToLinesActivity();
            return;
        }

        //Set the current line, and update it to busy

        cPickorderLine.currentPickOrderLine = nextLine;

        hulpResult = cPickorderLine.currentPickOrderLine.pLineBusyRst();
        if (!hulpResult.resultBln) {
            cUserInterface.pDoExplodingScreen(hulpResult.messagesStr(),"",true,true);
            cPickorderLine.currentPickOrderLine = null;
            this.mGoBackToLinesActivity();
            return;
        }

        this.mInitnewLineForBin();
    }

    private void mInitnewLineForBin() {

        //Play a sound
        cUserInterface.pPlaySound(R.raw.message, null);

        //Clear current barcodeStr and reset defaults
        cPickorderBarcode.currentPickorderBarcode = null;
        PickorderPickActivity.articleScannedLastBln = false;

        if (cPickorder.currentPickOrder.destionationBranch() == null) {
            PickorderPickActivity.destionationScannedBln = false;
        }

        //Show animation and initialize fields
        Animation animation = AnimationUtils.loadAnimation(cAppExtension.context.getApplicationContext(), R.anim.shrink_and_fade);
        this.pickorderPickContainer.startAnimation(animation);
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

    private  void mSendPickorderLine() {

        //If internet is not connected
        if (!cConnection.isInternetConnectedBln()) {
            //could not send line, let user know but answer succes so user can go to next line
            cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.couldnt_send_line), null);
            cPickorderLine.currentPickOrderLine.pErrorSending();
            return;
        }


        if (!cPickorderLine.currentPickOrderLine.pHandledBln()) {
            //could not send line, let user know but answer succes so user can go to next line
            cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.couldnt_send_line), null);
            cPickorderLine.currentPickOrderLine.pErrorSending();
        }

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

    private void mHandlePickDoneClick() {

        //If workflow is then call specific void
        if (cPickorder.currentPickOrder.isPVBln()) {
            this.mHandlePVDone();
            return;
        }

        //Check if we picked less then asked, if so then show dialog
        if (!cPickorderLine.currentPickOrderLine.getQuantityHandledDbl().equals(cPickorderLine.currentPickOrderLine.getQuantityDbl()) ) {
            this.mShowUnderPickDialog(cAppExtension.activity.getString(R.string.message_cancel_line), cAppExtension.activity.getString(R.string.message_accept_line));
            return;
        }

        //All is done
        this.mPickDone();

    }

    private void mHandlePVDone() {

        if (PickorderPickActivity.articleScannedLastBln) {
            this.textViewAction.setText(R.string.message_scan_pickcart_or_salesorder);
            cUserInterface.pShowToastMessage(getString(R.string.message_scan_pickcart_or_salesorder), R.raw.badsound);
            return;
        }

        //If we picked less then asked, show dialog
        if (!cPickorderLine.currentPickOrderLine.getQuantityHandledDbl().equals(cPickorderLine.currentPickOrderLine.getQuantityDbl())) {
            this.mShowUnderPickDialog(cAppExtension.activity.getString(R.string.message_cancel_line), cAppExtension.activity.getString(R.string.message_accept_line));
            return;
        }

        this.mPickDone();
    }

    private  void mPickDone() {

        this.mSendPickorderLine();
        this.mGetNextPickLineForCurrentBin();

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

        this.imageButtonPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //There is no selected barcodeStr, select one first
                if (cPickorderBarcode.currentPickorderBarcode == null) {
                    cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.message_select_one_of_multiple_barcodes),null);
                    return;
                }

                PickorderPickActivity.articleScannedLastBln = true;
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
                    pickCounterMinusHelperInt = 0;
                }
                return false;
            }

        });

        this.imageButtonMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //There is no selected barcodeStr, select one first
                if (cPickorderBarcode.currentPickorderBarcode == null) {
                    cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.message_select_one_of_multiple_barcodes),null);
                    return;
                }


                mTryToChangePickedQuantity(false, false, cPickorderBarcode.currentPickorderBarcode.getQuantityPerUnitOfMeasureDbl());
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
        quantityRequiredText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNumberClicked();
            }
        });
    }

    //Dialogs and Activitys

    private  void mShowFullArticleFragment() {

        cUserInterface.pCheckAndCloseOpenDialogs();

        ArticleFullViewFragment articleFullViewFragment = new ArticleFullViewFragment();
        articleFullViewFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.ARTICLEFULL_TAG);



    }

    private  void mShowUnderPickDialog(String pvRejectStr,String pvAcceptStr) {

        cUserInterface.pCheckAndCloseOpenDialogs();

        final AcceptRejectFragment acceptRejectFragment = new AcceptRejectFragment(cAppExtension.activity.getString(R.string.message_underpick_header),
                                                                                   cAppExtension.activity.getString(R.string.message_underpick_text,
                                                                                   cText.pDoubleToStringStr(cPickorderLine.currentPickOrderLine.getQuantityDbl()),
                                                                                   cText.pDoubleToStringStr(cPickorderLine.currentPickOrderLine.getQuantityHandledDbl())),
                                                                                   pvRejectStr,
                                                                                   pvAcceptStr ,
                                                                     false);
        acceptRejectFragment.setCancelable(true);
        cAppExtension.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // show my popup
                acceptRejectFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.ACCEPTREJECTFRAGMENT_TAG);
            }
        });
    }

    private void mShowAcceptFragment(){
        boolean ignoreAccept = false;
        if (cPickorder.currentPickOrder.isPVBln()) {
            ignoreAccept = true;
        }

        cUserInterface.pCheckAndCloseOpenDialogs();

        final AcceptRejectFragment acceptRejectFragment = new AcceptRejectFragment(cAppExtension.activity.getString(R.string.message_orderbusy_header),
                                                                                   cAppExtension.activity.getString(R.string.message_orderbusy_text),
                                                                                   cAppExtension.activity.getString(R.string.message_cancel_line), cAppExtension.activity.getString(R.string.message_accept_line),ignoreAccept);
        acceptRejectFragment.setCancelable(true);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // show my popup
                acceptRejectFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.ACCEPTREJECTFRAGMENT_TAG);
            }
        });
    }

    private  void mGoBackToLinesActivity() {

        //Reset current branch
        if (cPickorder.currentPickOrder.destionationBranch() == null) {
            cPickorder.currentPickOrder.scannedBranch =  null;
        }

        Intent intent = new Intent(cAppExtension.context, PickorderLinesActivity.class);
        cAppExtension.activity.startActivity(intent);
        cAppExtension.activity.finish();
    }

    private void mShowBarcodeSelectFragment() {
        BarcodeFragment barcodeFragment = new BarcodeFragment();
        barcodeFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.BARCODEPICKERFRAGMENT_TAG);
    }

    private void mShowNumberPickerFragment() {

        cUserInterface.pCheckAndCloseOpenDialogs();

        Bundle bundle = new Bundle();
        bundle.putInt(cPublicDefinitions.NUMBERINTENT_CURRENTQUANTITY, cPickorderLine.currentPickOrderLine.getQuantityHandledDbl().intValue());
        bundle.putDouble(cPublicDefinitions.NUMBERINTENT_MAXQUANTITY, cPickorderLine.currentPickOrderLine.getQuantityDbl().intValue());

        NumberpickerFragment numberpickerFragment = new NumberpickerFragment();
        numberpickerFragment.setArguments(bundle);

        numberpickerFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.NUMBERPICKERFRAGMENT_TAG);
    }

    private  void mShowOverpickNotAllowed(){
        this.quantityText.setText(quantityRequiredText.getText());
        cUserInterface.pShowSnackbarMessage(textViewAction , cAppExtension.context.getString(R.string.number_cannot_be_higher), null, false);
        cUserInterface.pDoNope(quantityText, true, true);
        cUserInterface.pDoNope(quantityRequiredText, false, false);
    }

    //Region Number Broadcaster

    private Runnable mMinusAction = new Runnable() {
        @Override
        public void run() {
            imageButtonMinus.performClick();
            long milliSecsLng;
            if (pickCounterMinusHelperInt < 10) {
                milliSecsLng = 200;
            } else if (pickCounterMinusHelperInt < 20) {
                milliSecsLng = 150;
            } else if (pickCounterMinusHelperInt < 30) {
                milliSecsLng = 100;
            } else if (pickCounterMinusHelperInt < 40) {
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
            if (pickCounterPlusHelperInt < 10) {
                milliSecsLng = 200;
            } else if (pickCounterPlusHelperInt < 20) {
                milliSecsLng = 150;
            } else if (pickCounterPlusHelperInt < 30) {
                milliSecsLng = 100;
            } else if (pickCounterPlusHelperInt < 40) {
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
        this.pickCounterMinusHelperInt += 1;
    }

    private void mDoDelayedPlus(Runnable pvRunnable, long pvMilliSecsLng) {
        this.plusHandler.postDelayed(pvRunnable, pvMilliSecsLng);
        this.pickCounterPlusHelperInt += 1;
    }

    //End Region Number Broadcaster

    //End Regin Private Methods

}

