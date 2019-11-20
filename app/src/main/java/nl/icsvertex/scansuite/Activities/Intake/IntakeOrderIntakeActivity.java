package nl.icsvertex.scansuite.Activities.Intake;

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

import java.util.List;

import ICS.Interfaces.iICSDefaultActivity;
import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cConnection;
import ICS.Utils.cResult;
import ICS.Utils.cText;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Basics.BranchBin.cBranchBin;
import SSU_WHS.Basics.Settings.cSetting;
import SSU_WHS.General.cPublicDefinitions;
import SSU_WHS.Intake.IntakeorderBarcodes.cIntakeorderBarcode;
import SSU_WHS.Intake.IntakeorderMATLines.cIntakeorderMATLine;
import SSU_WHS.Intake.Intakeorders.cIntakeorder;
import nl.icsvertex.scansuite.Fragments.Dialogs.AcceptRejectFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.ArticleFullViewFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.BarcodeFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.NumberpickerFragment;
import nl.icsvertex.scansuite.R;

public class IntakeOrderIntakeActivity extends AppCompatActivity implements iICSDefaultActivity {

    //Region Private Properties
    static final String NUMBERFRAGMENT_TAG = "NUMBERFRAGMENT_TAG";
    static final String BARCODEFRAGMENT_TAG = "BARCODEFRAGMENT_TAG";
    static final String ACCEPTREJECTFRAGMENT_TAG = "ACCEPTREJECTFRAGMENT_TAG";

    private static int counterMinusHelperInt;
    private static int counterPlusHelperInt;

    private static Handler minusHandler;
    private static Handler plusHandler;
    private static ConstraintLayout intakeorderIntakeContainer;
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

    private static cBranchBin currentBranchBin;

    private static List<cIntakeorderMATLine> linesToHandleObl;

    //End Region Private Properties

    //Region Default Methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intakeorder_intake);
        this.mActivityInitialize();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        //Set listeners here, so click listeners only work after activity is shown
        this.mSetListeners();

        //Init the screen
        this.mInitScreen();
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

        this.mSetToolbar(getResources().getString(R.string.screentitle_intake));

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

        this.intakeorderIntakeContainer = findViewById(R.id.intakeorderIntakeContainer);
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
        toolbarImage.setImageResource(R.drawable.ic_menu_intake);
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
        IntakeOrderIntakeActivity.mFieldsInitializeStatic();
    }

    @Override
    public void mInitScreen() {

        cBarcodeScan.pRegisterBarcodeReceiver();


//todo:  fix this
//        //BIN scanned in lines activity
//        if (cIntakeorder.currentIntakeOrder.currentBranchBin != null) {
//
//
//            cBarcodeScan barcodeScan = new cBarcodeScan();
//            barcodeScan.barcodeOriginalStr = cIntakeorder.currentIntakeOrder.currentBranchBin.getBinCodeStr();
//            barcodeScan.barcodeStr = cIntakeorder.currentIntakeOrder.currentBranchBin.getBinCodeStr();
//            barcodeScan.barcodeTypeStr = cText.pIntToStringStr(cBarcodeScan.BarcodeType.CODE128);
//
//          IntakeOrderIntakeActivity.pHandleScan(barcodeScan);
//            cIntakeorder.currentIntakeOrder.currentBranchBin = null;
//        }
//
//       if (cIntakeorder.currentIntakeOrder.currentIntakeOrderbarcode != null) {
//
//           cBarcodeScan barcodeScan = new cBarcodeScan();
//           barcodeScan.barcodeOriginalStr = cIntakeorder.currentIntakeOrder.currentIntakeOrderbarcode.getBarcodeStr();
//           barcodeScan.barcodeStr = cIntakeorder.currentIntakeOrder.currentIntakeOrderbarcode.getBarcodeStr();
//           barcodeScan.barcodeTypeStr = cText.pIntToStringStr(cBarcodeScan.BarcodeType.CODE128);
//
//           IntakeOrderIntakeActivity.pHandleScan(barcodeScan);
//           cIntakeorder.currentIntakeOrder.currentIntakeOrderbarcode= null;
//       }

    }

    @Override
    public void mSetListeners() {

        this.mSetArticleImageListener();
        this.mSetImageButtonBarcodeListener();

        if (cSetting.RECEIVE_AMOUNT_MANUAL_MA() == true) {
            this.mSetNumberListener();
        }

        this.mSetPlusListener();
        this.mSetMinusListener();
        this.mSetDoneListener();

    }

    // End Region iICSDefaultActivity Methods

    //Region Public Methods

    public static void pHandleScan(cBarcodeScan pvBarcodeScan) {

        cUserInterface.pCheckAndCloseOpenDialogs();

        cResult hulpRst;

        //todo: fix this
//        //Check if we have scanned a BIN and check if there are not handled linesInt for this BIN
//        if (cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(), cBarcodeLayout.barcodeLayoutEnu.BIN)) {
//
//            hulpRst = IntakeOrderIntakeActivity.mHandleBINScanRst(pvBarcodeScan.getBarcodeOriginalStr());
//            if (! hulpRst.resultBln) {
//                cUserInterface.pDoExplodingScreen(hulpRst.messagesStr(),pvBarcodeScan.getBarcodeOriginalStr(),true,true);
//                return;
//            }
//
//        }

        //todo: fix this
//        //Check if we have scanned a BIN and check if there are not handled linesInt for this BIN
//        if (cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(), cBarcodeLayout.barcodeLayoutEnu.ARTICLE)) {
//
//            hulpRst = IntakeOrderIntakeActivity.mHandleArticleScanRst(pvBarcodeScan);
//            if (! hulpRst.resultBln) {
//                cUserInterface.pDoExplodingScreen(hulpRst.messagesStr(),pvBarcodeScan.getBarcodeOriginalStr(),true,true);
//                return;
//            }
//        }

        cUserInterface.pDoExplodingScreen(cAppExtension.activity.getString(R.string.message_unknown_barcode),pvBarcodeScan.getBarcodeOriginalStr(),true,true);
        return;


    }

    public static void pAcceptStore() {
         cIntakeorderMATLine.currentIntakeorderMATLine.pHandledBln();
         IntakeOrderIntakeActivity.mStoreDone();
    }

    public static void pCancelStore() {
        cIntakeorderMATLine.currentIntakeorderMATLine.quantityHandledDbl = Double.valueOf(0);
        cIntakeorderMATLine.currentIntakeorderMATLine.pCancelIndatabaseBln();
        IntakeOrderIntakeActivity.mGoBackToLinesActivity();
        return;
    }

    public  static  void pRegisterBarcodeReceiver(){
        cBarcodeScan.pRegisterBarcodeReceiver();
    }

    //End Region Public Methods

    //Region Private Methods

    //Views
    private static void mFieldsInitializeStatic(){

        IntakeOrderIntakeActivity.imageButtonDone.setImageResource(R.drawable.ic_check_black_24dp);

        IntakeOrderIntakeActivity.counterPlusHelperInt = 0;
        IntakeOrderIntakeActivity.counterMinusHelperInt = 0;
        IntakeOrderIntakeActivity.toolbarSubtext.setText(cIntakeorder.currentIntakeOrder.getOrderNumberStr());

        IntakeOrderIntakeActivity.mSetArticleInfo();

        //todo
//        IntakeOrderIntakeActivity.mSetBINInfo();
        IntakeOrderIntakeActivity.mSetQuantityInfo();
        IntakeOrderIntakeActivity.mSetInstruction("");

        IntakeOrderIntakeActivity.mEnablePlusMinusAndBarcodeSelectViews();
        IntakeOrderIntakeActivity.mShowArticleImage();
        IntakeOrderIntakeActivity.mShowOrHideGenericExtraFields();
        IntakeOrderIntakeActivity.mShowBarcodeInfo();

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

                if (cIntakeorderMATLine.currentIntakeorderMATLine.barcodesObl == null || cIntakeorderMATLine.currentIntakeorderMATLine.barcodesObl.size() == 0) {
                    return;
                }

                mEnablePlusMinusAndBarcodeSelectViews();

                //If we only have one barcode, then automatticaly select that barcode
                if (cIntakeorderMATLine.currentIntakeorderMATLine.barcodesObl.size() == 1) {

                    cBarcodeScan barcodeScan = new cBarcodeScan();
                    barcodeScan.barcodeOriginalStr = cIntakeorderMATLine.currentIntakeorderMATLine.barcodesObl.get(0).getBarcodeStr();
                    barcodeScan.barcodeStr = cIntakeorderMATLine.currentIntakeorderMATLine.barcodesObl.get(0).getBarcodeStr();
                    barcodeScan.barcodeOriginalStr = cIntakeorderMATLine.currentIntakeorderMATLine.barcodesObl.get(0).getBarcodeTypeStr();

                    IntakeOrderIntakeActivity.pHandleScan(barcodeScan);
                    return;
                }

                mShowBarcodeSelectFragment();
                return;

            }
        });
    }

    private static void mShowOrHideGenericExtraFields() {


        if (cIntakeorderMATLine.currentIntakeorderMATLine == null) {
            IntakeOrderIntakeActivity.genericItemExtraField1Text.setVisibility(View.INVISIBLE);
            IntakeOrderIntakeActivity.genericItemExtraField2Text.setVisibility(View.INVISIBLE);
            IntakeOrderIntakeActivity.genericItemExtraField3Text.setVisibility(View.INVISIBLE);
            IntakeOrderIntakeActivity.genericItemExtraField4Text.setVisibility(View.INVISIBLE);
            IntakeOrderIntakeActivity.genericItemExtraField5Text.setVisibility(View.INVISIBLE);
            IntakeOrderIntakeActivity.genericItemExtraField6Text.setVisibility(View.INVISIBLE);
            IntakeOrderIntakeActivity.genericItemExtraField7Text.setVisibility(View.INVISIBLE);
            IntakeOrderIntakeActivity.genericItemExtraField8Text.setVisibility(View.INVISIBLE);
            return;
        }

        if (!cIntakeorderMATLine.currentIntakeorderMATLine.getExtraField1Str().isEmpty()) {
            IntakeOrderIntakeActivity.genericItemExtraField1Text.setVisibility(View.VISIBLE);
            IntakeOrderIntakeActivity.genericItemExtraField1Text.setText(cIntakeorderMATLine.currentIntakeorderMATLine.getExtraField1Str());
        }

        if (!cIntakeorderMATLine.currentIntakeorderMATLine.getExtraField2Str().isEmpty()) {
            IntakeOrderIntakeActivity.genericItemExtraField2Text.setVisibility(View.VISIBLE);
            IntakeOrderIntakeActivity.genericItemExtraField2Text.setText(cIntakeorderMATLine.currentIntakeorderMATLine.getExtraField2Str());
        }

        if (!cIntakeorderMATLine.currentIntakeorderMATLine.getExtraField3Str().isEmpty()) {
            IntakeOrderIntakeActivity.genericItemExtraField3Text.setVisibility(View.VISIBLE);
            IntakeOrderIntakeActivity.genericItemExtraField3Text.setText(cIntakeorderMATLine.currentIntakeorderMATLine.getExtraField3Str());
        }

        if (!cIntakeorderMATLine.currentIntakeorderMATLine.getExtraField4Str().isEmpty()) {
            IntakeOrderIntakeActivity.genericItemExtraField4Text.setVisibility(View.VISIBLE);
            IntakeOrderIntakeActivity.genericItemExtraField4Text.setText(cIntakeorderMATLine.currentIntakeorderMATLine.getExtraField4Str());
        }

        if (!cIntakeorderMATLine.currentIntakeorderMATLine.getExtraField5Str().isEmpty()) {
            IntakeOrderIntakeActivity.genericItemExtraField5Text.setVisibility(View.VISIBLE);
            IntakeOrderIntakeActivity.genericItemExtraField5Text.setText(cIntakeorderMATLine.currentIntakeorderMATLine.getExtraField5Str());
        }

        if (!cIntakeorderMATLine.currentIntakeorderMATLine.getExtraField6Str().isEmpty()) {
            IntakeOrderIntakeActivity.genericItemExtraField6Text.setVisibility(View.VISIBLE);
            IntakeOrderIntakeActivity.genericItemExtraField6Text.setText(cIntakeorderMATLine.currentIntakeorderMATLine.getExtraField6Str());
        }

        if (!cIntakeorderMATLine.currentIntakeorderMATLine.getExtraField7Str().isEmpty()) {
            IntakeOrderIntakeActivity.genericItemExtraField7Text.setVisibility(View.VISIBLE);
            IntakeOrderIntakeActivity.genericItemExtraField7Text.setText(cIntakeorderMATLine.currentIntakeorderMATLine.getExtraField7Str());
        }

        if (!cIntakeorderMATLine.currentIntakeorderMATLine.getExtraField8Str().isEmpty()) {
            IntakeOrderIntakeActivity.genericItemExtraField8Text.setVisibility(View.VISIBLE);
            IntakeOrderIntakeActivity.genericItemExtraField8Text.setText(cIntakeorderMATLine.currentIntakeorderMATLine.getExtraField8Str());
        }

    }

    private static void mShowBarcodeInfo() {

        if (cIntakeorderMATLine.currentIntakeorderMATLine == null) {
            IntakeOrderIntakeActivity.articleBarcodeText.setText("???");
            return;
        }

        if (cIntakeorderBarcode.currentIntakeOrderBarcode == null) {
            if (cIntakeorderMATLine.currentIntakeorderMATLine.barcodesObl.size() == 1) {
                cIntakeorderBarcode.currentIntakeOrderBarcode = cIntakeorderMATLine.currentIntakeorderMATLine.barcodesObl.get(0);
            }
        }

        if (cIntakeorderBarcode.currentIntakeOrderBarcode!= null) {
            IntakeOrderIntakeActivity.articleBarcodeText.setText(cIntakeorderBarcode.currentIntakeOrderBarcode.getBarcodeStr() + " (" + cIntakeorderBarcode.currentIntakeOrderBarcode.getQuantityPerUnitOfMeasureDbl().intValue() + ")");
        } else {
            IntakeOrderIntakeActivity.articleBarcodeText.setText(cAppExtension.context.getString(R.string.mutiple_barcodes_posible));
        }
    }

    private static void mShowArticleImage() {

        IntakeOrderIntakeActivity.articleThumbImageView.setImageDrawable(ContextCompat.getDrawable(cAppExtension.context, R.drawable.ic_no_image_lightgrey_24dp));
        return;

        //todo: do this when correct settings are available
//        //If pick with picture is false, then hide image view
//        if (cPickorder.currentPickOrder.isPickWithPictureBln() == false) {
//            IntakeOrderIntakeActivity.articleThumbImageView.setVisibility(View.INVISIBLE);
//            return;
//        }
//
//        //If picture is not in cache (via webservice) then show no image
//        if (cPickorderLine.currentPickOrderLine.pGetArticleImageBln() == false) {
//            cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.could_not_get_article_image), null);
//            IntakeOrderIntakeActivity.articleThumbImageView.setImageDrawable(ContextCompat.getDrawable(cAppExtension.context, R.drawable.ic_no_image_lightgrey_24dp));
//            return;
//        }
//
//        //If picture is in cache but can't be converted, then show no image
//        if (cPickorderLine.currentPickOrderLine.articleImage == null || cPickorderLine.currentPickOrderLine.articleImage.imageBitmap() == null) {
//            cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.could_not_get_article_image), null);
//            IntakeOrderIntakeActivity.articleThumbImageView.setImageDrawable(ContextCompat.getDrawable(cAppExtension.context, R.drawable.ic_no_image_lightgrey_24dp));
//            return;
//        }
//
//        //Show the image
//        IntakeOrderIntakeActivity.articleThumbImageView.setImageBitmap(cPickorderLine.currentPickOrderLine.articleImage.imageBitmap());
//
//        //Open the image
//        if ((cPickorder.currentPickOrder.isPickWithPictureAutoOpenBln())) {
//            IntakeOrderIntakeActivity.mShowFullArticleFragment();
//        }

    }

    private static void mEnablePlusMinusAndBarcodeSelectViews() {

        if (cIntakeorderMATLine.currentIntakeorderMATLine == null) {
            IntakeOrderIntakeActivity.imageButtonMinus.setVisibility(View.INVISIBLE);
            IntakeOrderIntakeActivity.imageButtonPlus.setVisibility(View.INVISIBLE);
            IntakeOrderIntakeActivity.imageButtonBarcode.setVisibility(View.INVISIBLE);
            IntakeOrderIntakeActivity.imageButtonDone.setVisibility(View.INVISIBLE);
            return;
        }

        IntakeOrderIntakeActivity.imageButtonDone.setVisibility(View.VISIBLE);

        if (!cSetting.RECEIVE_AMOUNT_MANUAL_MA()) {
            IntakeOrderIntakeActivity.imageButtonMinus.setVisibility(View.INVISIBLE);
            IntakeOrderIntakeActivity.imageButtonPlus.setVisibility(View.INVISIBLE);
            IntakeOrderIntakeActivity.imageButtonBarcode.setVisibility(View.INVISIBLE);
        } else {
            IntakeOrderIntakeActivity.imageButtonMinus.setVisibility(View.VISIBLE);
            IntakeOrderIntakeActivity.imageButtonPlus.setVisibility(View.VISIBLE);
            IntakeOrderIntakeActivity.imageButtonBarcode.setVisibility(View.VISIBLE);
        }
    }

    //Scans and manual input

    private void mNumberClicked() {

        if (cIntakeorderBarcode.currentIntakeOrderBarcode == null) {
            cUserInterface.pDoNope(quantityText, false, false);
            cUserInterface.pShowSnackbarMessage(intakeorderIntakeContainer, getString(R.string.choose_barcode_first), null, false);
            return;
        }

        if (cIntakeorderBarcode.currentIntakeOrderBarcode.getQuantityHandled() > 1) {
            cUserInterface.pDoNope(quantityText, true, true);
            cUserInterface.pShowSnackbarMessage(intakeorderIntakeContainer, getString(R.string.manual_input_only_barcodenumber_bigger1), null, false);
            return;
        }

        this.mShowNumberFragment();
    }

    private void mHandleQuantityChosen(double pvQuantityDbl) {


        if (pvQuantityDbl == 0) {
            this.mTryToChangeQuantity(false, true,pvQuantityDbl);
        } else {
            this.mTryToChangeQuantity(true, true,pvQuantityDbl);

        }

    }

    private static void mTryToChangeQuantity(Boolean pvIsPositiveBln, Boolean pvAmountFixedBln, double pvAmountDbl) {

      double newQuantityDbl;

        if (pvIsPositiveBln) {

            //Determine the new amount
            if (pvAmountFixedBln == true) {
                newQuantityDbl = pvAmountDbl;
            } else {
                newQuantityDbl = cIntakeorderMATLine.currentIntakeorderMATLine.getQuantityHandledDbl() + pvAmountDbl;
            }

            //Set the new quantityDbl and show in Activity
            cIntakeorderMATLine.currentIntakeorderMATLine.quantityHandledDbl = newQuantityDbl;
            IntakeOrderIntakeActivity.quantityText.setText(cText.pDoubleToStringStr(cIntakeorderMATLine.currentIntakeorderMATLine.getQuantityHandledDbl()));

            //Add or update line barcode
            cIntakeorderMATLine.currentIntakeorderMATLine.pAddOrUpdateLineBarcodeBln(pvAmountDbl);

            //Update orderline info (quantityDbl,localStatusInt)
            cIntakeorderMATLine.currentIntakeorderMATLine.pHandledIndatabaseBln();

            //Check if this line is done
            IntakeOrderIntakeActivity.mCheckLineDone();
            return;
        }

        //negative
        if (cIntakeorderMATLine.currentIntakeorderMATLine.quantityHandledDbl == 0 ) {
            cUserInterface.pDoNope(quantityText, true, true);
            return;
        }

        //Determine the new amount

        if (pvAmountFixedBln == true) {
            newQuantityDbl = pvAmountDbl;
        }else {
            newQuantityDbl= cIntakeorderMATLine.currentIntakeorderMATLine.getQuantityHandledDbl() - pvAmountDbl;
        }

        if (newQuantityDbl <= 0) {
            cIntakeorderMATLine.currentIntakeorderMATLine.quantityHandledDbl = Double.valueOf(0);
        }else {
            //Set the new quantityDbl and show in Activity
            cIntakeorderMATLine.currentIntakeorderMATLine.quantityHandledDbl = newQuantityDbl;
        }

        IntakeOrderIntakeActivity.quantityText.setText(cText.pDoubleToStringStr(cIntakeorderMATLine.currentIntakeorderMATLine.getQuantityHandledDbl()));
        IntakeOrderIntakeActivity.imageButtonDone.setImageResource(R.drawable.ic_check_black_24dp);

        //Remove or update line barcode
        cIntakeorderMATLine.currentIntakeorderMATLine.pRemoveOrUpdateLineBarcodeBln();
        return;

    }

    private static void mBarcodeSelected(cIntakeorderBarcode pvBarcode) {

        cUserInterface.pCheckAndCloseOpenDialogs();

        cIntakeorderBarcode.currentIntakeOrderBarcode = pvBarcode;
        IntakeOrderIntakeActivity.mShowBarcodeInfo();
        IntakeOrderIntakeActivity.mTryToChangeQuantity(true, false, cIntakeorderBarcode.currentIntakeOrderBarcode.getQuantityPerUnitOfMeasureDbl());
        return;
    }

    // Lines, Barcodes

    private static void mCheckLineDone() {

        //Start with complete
        Boolean incompleteBln = false;
        IntakeOrderIntakeActivity.imageButtonDone.setVisibility(View.VISIBLE);

        //Check if quantityDbl is sufficient
        if (cIntakeorderMATLine.currentIntakeorderMATLine.quantityHandledDbl < cIntakeorderMATLine.currentIntakeorderMATLine.quantityDbl) {
            IntakeOrderIntakeActivity.imageButtonDone.setImageResource(R.drawable.ic_check_black_24dp);
            incompleteBln = true;
        }


        if (incompleteBln) {
            IntakeOrderIntakeActivity.imageButtonDone.setVisibility(View.VISIBLE);
            IntakeOrderIntakeActivity.imageButtonDone.setImageResource(R.drawable.ic_doublecheck_black_24dp);
            return;
        }

        //Add or update line BIN
        cIntakeorderMATLine.currentIntakeorderMATLine.binCodeHandledStr = IntakeOrderIntakeActivity.currentBranchBin.getBinCodeStr();
    }

    private static Boolean mSendLine() {

        //If internet is not connected
        if (!cConnection.isInternetConnectedBln()) {
            //could not send line, let user know but answer succes so user can go to next line
            cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.couldnt_send_line), null);
            cIntakeorderMATLine.currentIntakeorderMATLine.pErrorSendingBln();
            return true;
        }


        if (cIntakeorderMATLine.currentIntakeorderMATLine.pHandledBln() == false) {
            //could not send line, let user know but answer succes so user can go to next line
            cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.couldnt_send_line), null);
            cIntakeorderMATLine.currentIntakeorderMATLine.pErrorSendingBln();
            return true;
        }

        return true;
    }

    private void mHandleStoreDoneClick() {

        //Check if we placed less then asked, if so then show dialog
        if (cIntakeorderMATLine.currentIntakeorderMATLine.getQuantityHandledDbl() != cIntakeorderMATLine.currentIntakeorderMATLine.getQuantityDbl()) {
            this.mShowUnderStoreDialog(cAppExtension.activity.getString(R.string.message_cancel_line), cAppExtension.activity.getString(R.string.message_accept_line));
            return;
        }

        //All is done
        this.mStoreDone();

    }

    private static void mStoreDone() {
        IntakeOrderIntakeActivity.mSendLine();
     }

    //Listeners
    private void mSetDoneListener() {
        this.imageButtonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mHandleStoreDoneClick();
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
                    counterPlusHelperInt = 0;
                }

                return false;
            }
        });

        imageButtonPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //There is no selected barcode, select one first
                if (cIntakeorderBarcode.currentIntakeOrderBarcode == null) {
                    cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.message_select_one_of_multiple_barcodes),null);
                    return;
                }


                mTryToChangeQuantity(true, false, cIntakeorderBarcode.currentIntakeOrderBarcode.getQuantityPerUnitOfMeasureDbl());
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
                    counterMinusHelperInt = 0;
                }
                return false;
            }

        });

        this.imageButtonMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //There is no selected barcode, select one first
                if (cIntakeorderBarcode.currentIntakeOrderBarcode == null) {
                    cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.message_select_one_of_multiple_barcodes),null);
                    return;
                }


                mTryToChangeQuantity(false, false, cIntakeorderBarcode.currentIntakeOrderBarcode.getQuantityPerUnitOfMeasureDbl());
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

    //Dialogs and Activitys

    private static void mShowFullArticleFragment() {

        cUserInterface.pCheckAndCloseOpenDialogs();

        ArticleFullViewFragment articleFullViewFragment = new ArticleFullViewFragment();
        articleFullViewFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.ARTICLEFULL_TAG);



    }

    private static void mShowUnderStoreDialog(String pvRejectStr, String pvAcceptStr) {

        cUserInterface.pCheckAndCloseOpenDialogs();

        final AcceptRejectFragment acceptRejectFragment = new AcceptRejectFragment(cAppExtension.activity.getString(R.string.message_underpick_header),
                                                                                   cAppExtension.activity.getString(R.string.message_underpick_text, cText.pDoubleToStringStr(cIntakeorderMATLine.currentIntakeorderMATLine.getQuantityDbl()), cText.pDoubleToStringStr(cIntakeorderMATLine.currentIntakeorderMATLine.getQuantityHandledDbl())),pvRejectStr,pvAcceptStr );
        acceptRejectFragment.setCancelable(true);
        cAppExtension.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // show my popup
                acceptRejectFragment.show(cAppExtension.fragmentManager, ACCEPTREJECTFRAGMENT_TAG);
            }
        });
    }

    private void mShowAcceptFragment(){

        cUserInterface.pCheckAndCloseOpenDialogs();

        final AcceptRejectFragment acceptRejectFragment = new AcceptRejectFragment(cAppExtension.activity.getString(R.string.message_orderbusy_header),
                                                                                   cAppExtension.activity.getString(R.string.message_orderbusy_text),
                                                                                   cAppExtension.activity.getString(R.string.message_cancel_line), cAppExtension.activity.getString(R.string.message_accept_line));
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
        Intent intent = new Intent(cAppExtension.context, IntakeorderLinesActivity.class);
        cAppExtension.activity.startActivity(intent);
        cAppExtension.activity.finish();
    }

    private void mShowBarcodeSelectFragment() {
        BarcodeFragment barcodeFragment = new BarcodeFragment();
        barcodeFragment.show(cAppExtension.fragmentManager, BARCODEFRAGMENT_TAG);
        return;
    }

    private void mShowNumberFragment() {

        cUserInterface.pCheckAndCloseOpenDialogs();

        Bundle bundle = new Bundle();
        bundle.putInt(cPublicDefinitions.NUMBERINTENT_CURRENTQUANTITY, cIntakeorderMATLine.currentIntakeorderMATLine.getQuantityHandledDbl().intValue());

        if (cIntakeorder.currentIntakeOrder.receiveStoreAutoAcceptAtRequestedBln) {
            bundle.putDouble(cPublicDefinitions.NUMBERINTENT_MAXQUANTITY, cIntakeorderMATLine.currentIntakeorderMATLine.getQuantityDbl().intValue());
        } else {
            bundle.putDouble(cPublicDefinitions.NUMBERINTENT_MAXQUANTITY,99999);
        }

        NumberpickerFragment numberpickerFragment = new NumberpickerFragment();
        numberpickerFragment.setArguments(bundle);

        numberpickerFragment.show(cAppExtension.fragmentManager, NUMBERFRAGMENT_TAG);
        return;

    }

    //Region Number Broadcaster

    private Runnable mMinusAction = new Runnable() {
        @Override
        public void run() {
            imageButtonMinus.performClick();
            long milliSecsLng;
            if (counterMinusHelperInt < 10) {
                milliSecsLng = 200;
            } else if (counterMinusHelperInt < 20) {
                milliSecsLng = 150;
            } else if (counterMinusHelperInt < 30) {
                milliSecsLng = 100;
            } else if (counterMinusHelperInt < 40) {
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
            if (counterPlusHelperInt < 10) {
                milliSecsLng = 200;
            } else if (counterPlusHelperInt < 20) {
                milliSecsLng = 150;
            } else if (counterPlusHelperInt < 30) {
                milliSecsLng = 100;
            } else if (counterPlusHelperInt < 40) {
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
        this.counterMinusHelperInt += 1;
        return;
    }

    private void mDoDelayedPlus(Runnable pvRunnable, long pvMilliSecsLng) {
        this.plusHandler.postDelayed(pvRunnable, pvMilliSecsLng);
        this.counterPlusHelperInt += 1;
        return;
    }

    private static void mSetArticleInfo(){

        if (cIntakeorderMATLine.currentIntakeorderMATLine == null) {
            IntakeOrderIntakeActivity.articleDescriptionText.setText("???");
            IntakeOrderIntakeActivity.articleDescription2Text.setText("???");
            IntakeOrderIntakeActivity.articleItemText.setText("???");
            IntakeOrderIntakeActivity.articleVendorItemText.setText("???");
            return;
        }
            IntakeOrderIntakeActivity.articleDescriptionText.setText(cIntakeorderMATLine.currentIntakeorderMATLine.getDescriptionStr());
            IntakeOrderIntakeActivity.articleDescription2Text.setText(cIntakeorderMATLine.currentIntakeorderMATLine.getDescription2Str());
            IntakeOrderIntakeActivity.articleItemText.setText(cIntakeorderMATLine.currentIntakeorderMATLine.getItemNoStr() + " " + cIntakeorderMATLine.currentIntakeorderMATLine.getVariantCodeStr());
            IntakeOrderIntakeActivity.articleVendorItemText.setText(cIntakeorderMATLine.currentIntakeorderMATLine.getVendorItemNoStr() + ' ' + cIntakeorderMATLine.currentIntakeorderMATLine.getVendorItemDescriptionStr());
    }

    private static void mSetQuantityInfo(){

        if (cIntakeorderMATLine.currentIntakeorderMATLine == null) {
            IntakeOrderIntakeActivity.quantityText.setText("0");
            IntakeOrderIntakeActivity.quantityRequiredText.setText("???");
                return;
        }

        IntakeOrderIntakeActivity.quantityText.setText("0");
        IntakeOrderIntakeActivity.quantityRequiredText.setText(cText.pDoubleToStringStr(cIntakeorderMATLine.currentIntakeorderMATLine.getQuantityDbl()));

    }

    private static void mSetInstruction(String pvTextStr){

        IntakeOrderIntakeActivity.textViewAction.setText(pvTextStr);


    }

    //End Region Number Broadcaster

    //End Regin Private Methods

}

