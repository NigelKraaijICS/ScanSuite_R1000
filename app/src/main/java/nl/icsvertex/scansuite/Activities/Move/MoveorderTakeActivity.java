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

import org.json.JSONObject;

import java.util.List;

import ICS.Interfaces.iICSDefaultActivity;
import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cConnection;
import ICS.Utils.cRegex;
import ICS.Utils.cText;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Basics.Article.cArticle;
import SSU_WHS.Basics.ArticleImages.cArticleImage;
import SSU_WHS.Basics.BarcodeLayouts.cBarcodeLayout;
import SSU_WHS.Basics.BranchBin.cBranchBin;
import SSU_WHS.Basics.Settings.cSetting;
import SSU_WHS.Basics.Users.cUser;
import SSU_WHS.General.cPublicDefinitions;
import SSU_WHS.Move.MoveOrders.cMoveorder;
import SSU_WHS.Move.MoveorderBarcodes.cMoveorderBarcode;
import SSU_WHS.Move.MoveorderLines.cMoveorderLine;
import SSU_WHS.Move.MoveorderLines.cMoveorderLineEntity;
import SSU_WHS.Webservice.cWebresult;
import nl.icsvertex.scansuite.Fragments.Dialogs.AcceptRejectFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.ArticleFullViewFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.BarcodeFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.NumberpickerFragment;
import nl.icsvertex.scansuite.R;

public class MoveorderTakeActivity extends AppCompatActivity implements iICSDefaultActivity {

    //Region Private Properties
    static final String NUMBERPICKERFRAGMENT_TAG = "NUMBERPICKERFRAGMENT_TAG";
    static final String BARCODEPICKERFRAGMENT_TAG = "BARCODEPICKERFRAGMENT_TAG";
    static final String ACCEPTREJECTFRAGMENT_TAG = "ACCEPTREJECTFRAGMENT_TAG";

    private static Boolean articleScannedLastBln;
    private static int moveCounterMinusHelperInt;
    private static int moveCounterPlusHelperInt;

    private static Handler minusHandler;
    private static Handler plusHandler;
    private static ConstraintLayout moveorderTakeContainer;
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
        setContentView(R.layout.activity_moveorder_take);
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

        this.mSetToolbar(getResources().getString(R.string.screentitle_moveordertake));

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

        moveorderTakeContainer = findViewById(R.id.moveorderTakeContainer);
        toolbarImage = findViewById(R.id.toolbarImage);
        toolbarTitle = findViewById(R.id.toolbarTitle);
        toolbarSubtext = findViewById(R.id.toolbarSubtext);


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
        articleThumbImageView = findViewById(R.id.articleThumbImageView);
        imageButtonBarcode = findViewById(R.id.imageButtonBarcode);
        imageButtonMinus = findViewById(R.id.imageButtonMinus);
        imageButtonPlus = findViewById(R.id.imageButtonPlus);
        imageButtonDone = findViewById(R.id.imageButtonDone);

        binText = findViewById(R.id.binText);
        textViewAction = findViewById(R.id.textViewAction);

    }

    @Override
    public void mSetToolbar(String pvScreenTitle) {
        toolbarImage.setImageResource(R.drawable.ic_menu_move);
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
        MoveorderTakeActivity.mFieldsInitializeStatic();
    }

    @Override
    public void mInitScreen() {
        cBarcodeScan.pRegisterBarcodeReceiver();
    }

    @Override
    public void mSetListeners() {

        this.mSetArticleImageListener();
        this.mSetImageButtonBarcodeListener();
        this.mSetNumberListener();
        this.mSetPlusListener();
        this.mSetMinusListener();
        this.mSetDoneListener();

    }

    // End Region iICSDefaultActivity Methods

    //Region Public Methods

    public static void pHandleScan(final cBarcodeScan pvBarcodeScan) {

        cUserInterface.pCheckAndCloseOpenDialogs();

        cBranchBin branchBin;
        //is it a BIN?
        if (cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(), cBarcodeLayout.barcodeLayoutEnu.BIN)) {
            String binStr = cRegex.pStripRegexPrefixStr(pvBarcodeScan.getBarcodeOriginalStr());
            branchBin = cUser.currentUser.currentBranch.pGetBinByCode(binStr);
            if (branchBin !=null) {
                String binString = branchBin.getDescriptionStr() + " (" + branchBin.getBinCodeStr() + ")";
                binText.setText(binString);
            }
            else {
                cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.message_unknown_bin), R.raw.badsound);
                return;
            }
        }

        //is it an article?
        if (cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(), cBarcodeLayout.barcodeLayoutEnu.ARTICLE)) {
            cArticle.currentArticle = cArticle.pGetArticleByBarcodeViaWebservice(pvBarcodeScan);
            if (cArticle.currentArticle == null) {
                if (cMoveorder.currentMoveOrder.pAddUnkownBarcodeBln(pvBarcodeScan)) {
                    cArticle.currentArticle = cArticle.pGetArticleByBarcodeViaWebservice(pvBarcodeScan);
                }
            }
            else {
                cMoveorderBarcode.currentMoveOrderBarcode = new cMoveorderBarcode();
            }
            //movecheck barcode
            cArticleImage articleImage = cArticleImage.pGetArticleImageByItemNoAndVariantCode(cArticle.currentArticle.getItemNoStr(), cArticle.currentArticle.getVariantCodeStr());

            if (articleImage == null) {
                cWebresult Webresult;

                Webresult = cArticleImage.getArticleImageViewModel().pGetArticleImageFromWebserviceWrs(cArticle.currentArticle.getItemNoStr(), cArticle.currentArticle.getVariantCodeStr());
                if (!Webresult.getSuccessBln() || !Webresult.getResultBln()) {

                }
                cArticleImage articleImageWeb;

                List<JSONObject> myList = Webresult.getResultDtt();
                for (int i = 0; i < myList.size(); i++) {
                    JSONObject jsonObject;
                    jsonObject = myList.get(i);

                    articleImageWeb = new cArticleImage(jsonObject);
                    articleImageWeb.pInsertInDatabaseBln();
                    articleImage = articleImageWeb;
                }
            }

            if (articleImage != null) {
                articleThumbImageView.setVisibility(View.VISIBLE);
                articleThumbImageView.setImageBitmap(articleImage.imageBitmap());
            }

            if (!mFindBarcodeInOrderBarcodes(pvBarcodeScan.getBarcodeOriginalStr())) {
//            cUserInterface.pDoExplodingScreen(cAppExtension.context.getString(R.string.error_unknown_barcode), pvBarcodeScan.getBarcodeOriginalStr(), true, true);
//            return;
            }
            mFillArticleInfo(pvBarcodeScan.getBarcodeOriginalStr(), cArticle.currentArticle);

        }
//        if (cBranchBin.currentBranchBin !=null && cArticle.currentArticle != null) {
//            //is this already handled?
//            //get lines for this Bin
//            if (mCheckLinesBln()) {
//                return;
//            }
//        }
    }

    private static void mHandleUnknownBarcodeScan(cBarcodeScan pvBarcodeScan) {

        //We can add a line, but we don't check with the ERP, so add line and open it
        if (!cSetting.MOVE_BARCODE_CHECK()) {
            MoveorderTakeActivity.mAddUnkownArticle(pvBarcodeScan);
            return;
        }

        //We can add a line, and we need to check with the ERP, so check, add and open it
        MoveorderTakeActivity.mAddERPArticle(pvBarcodeScan);
        return;
    }

    private static void mAddERPArticle(cBarcodeScan pvBarcodeScan){

        //Add the barcode via the webservice
        if (!cMoveorder.currentMoveOrder.pAddERPBarcodeBln(pvBarcodeScan)) {
            MoveorderTakeActivity.mStepFailed(cAppExtension.activity.getString(R.string.message_adding_erp_article_failed));
            return;
        }
    }


    private static void mAddUnkownArticle(cBarcodeScan pvBarcodeScan){

        //Add the barcode via the webservice
        if (!cMoveorder.currentMoveOrder.pAddUnkownBarcodeBln(pvBarcodeScan)) {
            cUserInterface.pDoExplodingScreen(cAppExtension.activity.getString(R.string.message_adding_unkown_article_failed),"",true,true);
            return;
        }
    }


    private static void mStepFailed(String pvErrorMessageStr ){
        cUserInterface.pDoExplodingScreen(pvErrorMessageStr, cMoveorder.currentMoveOrder.getOrderNumberStr(), true, true );
        cUserInterface.pCheckAndCloseOpenDialogs();
    }


    private static boolean mCheckLinesBln() {
        List<cMoveorderLineEntity> moveorderLines = cMoveorderLine.getMoveorderLineViewModel().pGetLinesForBinItemNoVariantCodeFromDatabaseObl();
        if (moveorderLines == null || moveorderLines.size() == 0) {

            return false;
        }
        else {
            cMoveorderLine.currentMoveOrderLine = new cMoveorderLine(moveorderLines.get(0));
            mTryToChangeMovedQuantity(true, false, 1d);
            return true;
        }
    }

    private static void mFillArticleInfo(String barcode, cArticle pvArticle) {
        articleDescriptionText.setText(pvArticle.getDescriptionStr());
        articleDescription2Text.setText(pvArticle.getDescription2Str());
        articleItemText.setText(pvArticle.getItemNoStr()+" "+ pvArticle.getVariantCodeStr());
        articleBarcodeText.setText(barcode);
        articleVendorItemText.setText(pvArticle.getVendorItemNoStr()+ " " + pvArticle.getItemDescriptionStr());
    }
    public static void pAcceptMove() {
        cMoveorderLine.currentMoveOrderLine.pHandledIndatabaseBln();
        MoveorderTakeActivity.mMoveDone();
        MoveorderTakeActivity.mGoBackToLinesActivity();
    }

    public static void pCancelMove() {
        cMoveorderLine.currentMoveOrderLine.quantityHandledDbl = Double.valueOf(0);
        cMoveorderLine.currentMoveOrderLine.pCancelIndatabaseBln();
        MoveorderTakeActivity.mGoBackToLinesActivity();
        return;
    }

    public  static  void pRegisterBarcodeReceiver(){
        cBarcodeScan.pRegisterBarcodeReceiver();
    }

    //End Region Public Methods

    //Region Private Methods

    //Views
    private static void mFieldsInitializeStatic(){

        MoveorderTakeActivity.imageButtonDone.setImageResource(R.drawable.ic_check_black_24dp);

        MoveorderTakeActivity.moveCounterPlusHelperInt = 0;
        MoveorderTakeActivity.moveCounterMinusHelperInt = 0;
        MoveorderTakeActivity.toolbarSubtext.setText(cMoveorder.currentMoveOrder.getOrderNumberStr());

        MoveorderTakeActivity.quantityText.setText("0");
        imageButtonBarcode.setVisibility(View.INVISIBLE);
        MoveorderTakeActivity.mEnablePlusMinusAndBarcodeSelectViews();
        //MoveorderTakeActivity.mShowArticleImage();
        //MoveorderTakeActivity.mShowOrHideGenericExtraFields();
        //MoveorderTakeActivity.mShowBarcodeInfo();

        //        MoveorderTakeActivity.articleDescriptionText.setText(cMoveorderLine.currentMoveOrderLine.getDescriptionStr());
//        MoveorderTakeActivity.articleDescription2Text.setText(cMoveorderLine.currentMoveOrderLine.getDescription2Str());
//        MoveorderTakeActivity.articleItemText.setText(cMoveorderLine.currentMoveOrderLine.getItemNoStr() + " " + cMoveorderLine.currentMoveOrderLine.getVariantCodeStr());
//        MoveorderTakeActivity.articleVendorItemText.setText(cMoveorderLine.currentMoveOrderLine.getVendorItemNoStr() + ' ' + cMoveorderLine.currentMoveOrderLine.getVendorItemDescriptionStr());
//
//        MoveorderTakeActivity.binText.setText(cMoveorderLine.currentMoveOrderLine.getBinCodeStr());
//        MoveorderTakeActivity.textViewAction.setText(cAppExtension.context.getString(R.string.scan_article));

        //MoveorderTakeActivity.containerText.setVisibility(View.GONE);
        //MoveorderTakeActivity.containerText.setText(cMoveorderLine.currentMoveOrderLine.getContainerStr());
        //MoveorderTakeActivity.containerText.setText("");

    }

    private static void mEnablePlusMinusAndBarcodeSelectViews() {
        MoveorderTakeActivity.imageButtonMinus.setVisibility(View.VISIBLE);
        MoveorderTakeActivity.imageButtonPlus.setVisibility(View.VISIBLE);

        //MoveorderTakeActivity.imageButtonBarcode.setVisibility(View.VISIBLE);
    }

    private void mSetArticleImageListener() {
        this.articleThumbImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mShowFullArticleFragment();
            }
        });
    }

    private void mSetImageButtonBarcodeListener() {
        this.imageButtonBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View pvView) {

                if (cMoveorderLine.currentMoveOrderLine.barcodesObl == null || cMoveorderLine.currentMoveOrderLine.barcodesObl.size() == 0) {
                    return;
                }

                mEnablePlusMinusAndBarcodeSelectViews();

                //If we only have one barcode, then automatticaly select that barcode
                if (cMoveorderLine.currentMoveOrderLine.barcodesObl.size() == 1) {
                    //MoveorderTakeActivity.pHandleScan(cMoveorderLine.currentMoveOrderLine.barcodesObl.get(0).getBarcodeStr());
                    return;
                }

                mShowBarcodeSelectFragment();
                return;

            }
        });
    }

    private static void mShowOrHideGenericExtraFields() {

        if (!cMoveorderLine.currentMoveOrderLine.getExtraField1Str().isEmpty()) {
            genericItemExtraField1Text.setVisibility(View.VISIBLE);
            genericItemExtraField1Text.setText(cMoveorderLine.currentMoveOrderLine.getExtraField1Str());
        }

        if (!cMoveorderLine.currentMoveOrderLine.getExtraField2Str().isEmpty()) {
            genericItemExtraField2Text.setVisibility(View.VISIBLE);
            genericItemExtraField2Text.setText(cMoveorderLine.currentMoveOrderLine.getExtraField2Str());
        }

        if (!cMoveorderLine.currentMoveOrderLine.getExtraField3Str().isEmpty()) {
            genericItemExtraField3Text.setVisibility(View.VISIBLE);
            genericItemExtraField3Text.setText(cMoveorderLine.currentMoveOrderLine.getExtraField3Str());
        }

        if (!cMoveorderLine.currentMoveOrderLine.getExtraField4Str().isEmpty()) {
            genericItemExtraField4Text.setVisibility(View.VISIBLE);
            genericItemExtraField4Text.setText(cMoveorderLine.currentMoveOrderLine.getExtraField4Str());
        }

        if (!cMoveorderLine.currentMoveOrderLine.getExtraField5Str().isEmpty()) {
            genericItemExtraField5Text.setVisibility(View.VISIBLE);
            genericItemExtraField5Text.setText(cMoveorderLine.currentMoveOrderLine.getExtraField5Str());
        }

        if (!cMoveorderLine.currentMoveOrderLine.getExtraField6Str().isEmpty()) {
            genericItemExtraField6Text.setVisibility(View.VISIBLE);
            genericItemExtraField6Text.setText(cMoveorderLine.currentMoveOrderLine.getExtraField6Str());
        }

        if (!cMoveorderLine.currentMoveOrderLine.getExtraField7Str().isEmpty()) {
            genericItemExtraField7Text.setVisibility(View.VISIBLE);
            genericItemExtraField7Text.setText(cMoveorderLine.currentMoveOrderLine.getExtraField7Str());
        }

        if (!cMoveorderLine.currentMoveOrderLine.getExtraField8Str().isEmpty()) {
            genericItemExtraField8Text.setVisibility(View.VISIBLE);
            genericItemExtraField8Text.setText(cMoveorderLine.currentMoveOrderLine.getExtraField8Str());
        }
    }

    private static void mShowBarcodeInfo() {

        if (cMoveorderBarcode.currentMoveOrderBarcode == null) {
            if (cMoveorderLine.currentMoveOrderLine.barcodesObl.size() == 1) {
                cMoveorderBarcode.currentMoveOrderBarcode = cMoveorderLine.currentMoveOrderLine.barcodesObl.get(0);
            }
        }

        if (cMoveorderBarcode.currentMoveOrderBarcode != null) {
            MoveorderTakeActivity.articleBarcodeText.setText(cMoveorderBarcode.currentMoveOrderBarcode.getBarcodeStr() + " (" + cMoveorderBarcode.currentMoveOrderBarcode.getQuantityPerUnitOfMeasureDbl().intValue() + ")");
        } else {
            MoveorderTakeActivity.articleBarcodeText.setText(cAppExtension.context.getString(R.string.mutiple_barcodes_posible));
        }
    }

    private static void mShowArticleImage() {
        //If picture is not in cache (via webservice) then show no image
        if (cMoveorderLine.currentMoveOrderLine.pGetArticleImageBln() == false) {
            cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.could_not_get_article_image), null);
            MoveorderTakeActivity.articleThumbImageView.setImageDrawable(ContextCompat.getDrawable(cAppExtension.context, R.drawable.ic_no_image_lightgrey_24dp));
            return;
        }

        //If picture is in cache but can't be converted, then show no image
        if (cMoveorderLine.currentMoveOrderLine.articleImage == null || cMoveorderLine.currentMoveOrderLine.articleImage.imageBitmap() == null) {
            cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.could_not_get_article_image), null);
            MoveorderTakeActivity.articleThumbImageView.setImageDrawable(ContextCompat.getDrawable(cAppExtension.context, R.drawable.ic_no_image_lightgrey_24dp));
            return;
        }

        //Show the image
        MoveorderTakeActivity.articleThumbImageView.setImageBitmap(cMoveorderLine.currentMoveOrderLine.articleImage.imageBitmap());

    }
    //Scans and manual input

    private void mNumberClicked() {

        if (cMoveorderBarcode.currentMoveOrderBarcode == null) {
            cUserInterface.pDoNope(quantityText, false, false);
            cUserInterface.pShowSnackbarMessage(moveorderTakeContainer, getString(R.string.choose_barcode_first), null, false);
            return;
        }

        if (cMoveorderBarcode.currentMoveOrderBarcode.getQuantityHandled() > 1) {
            cUserInterface.pDoNope(quantityText, true, true);
            cUserInterface.pShowSnackbarMessage(moveorderTakeContainer, getString(R.string.manual_input_only_barcodenumber_bigger1), null, false);
            return;
        }
        this.mShowNumberPickerFragment();
    }

    private void mHandleQuantityChosen(double pvQuantityDbl) {

        if (pvQuantityDbl == 0) {
            this.mTryToChangeMovedQuantity(false, true,pvQuantityDbl);
        } else {
            this.mTryToChangeMovedQuantity(true, true,pvQuantityDbl);

        }
    }

    private static void mTryToChangeMovedQuantity(Boolean pvIsPositiveBln, Boolean pvAmountFixedBln, double pvAmountDbl) {

      double newQuantityDbl;

        if (pvIsPositiveBln) {
            newQuantityDbl = cMoveorderLine.currentMoveOrderLine.getQuantityHandledDbl() + pvAmountDbl;

            //Set the new quantity and show in Activity
            cMoveorderLine.currentMoveOrderLine.quantityHandledDbl = newQuantityDbl;
            MoveorderTakeActivity.quantityText.setText(cText.pDoubleToStringStr(cMoveorderLine.currentMoveOrderLine.getQuantityHandledDbl()));

            //Add or update line barcode
            cMoveorderLine.currentMoveOrderLine.pAddOrUpdateLineBarcodeBln(pvAmountDbl);

            //Update orderline info (quantity, timestamp, localstatus)
            cMoveorderLine.currentMoveOrderLine.pHandledIndatabaseBln();

            return;
        }

        //negative
        if (cMoveorderLine.currentMoveOrderLine.quantityHandledDbl == 0 ) {
            cUserInterface.pDoNope(quantityText, true, true);
            return;
        }

        //Determine the new amount

        if (pvAmountFixedBln == true) {
            newQuantityDbl = pvAmountDbl;
        }else {
            newQuantityDbl= cMoveorderLine.currentMoveOrderLine.getQuantityHandledDbl() - pvAmountDbl;
        }

        if (newQuantityDbl <= 0) {
            cMoveorderLine.currentMoveOrderLine.quantityHandledDbl = Double.valueOf(0);
        }else {
            //Set the new quantity and show in Activity
            cMoveorderLine.currentMoveOrderLine.quantityHandledDbl = newQuantityDbl;
        }

        MoveorderTakeActivity.quantityText.setText(cText.pDoubleToStringStr(cMoveorderLine.currentMoveOrderLine.getQuantityHandledDbl()));
        MoveorderTakeActivity.imageButtonDone.setImageResource(R.drawable.ic_check_black_24dp);

        //Remove or update line barcode
        cMoveorderLine.currentMoveOrderLine.pRemoveOrUpdateLineBarcodeBln();
        return;

    }

    private static void mBarcodeSelected(cMoveorderBarcode pvBarcode) {

        cUserInterface.pCheckAndCloseOpenDialogs();

        cMoveorderBarcode.currentMoveOrderBarcode = pvBarcode;
        MoveorderTakeActivity.mShowBarcodeInfo();
        MoveorderTakeActivity.mTryToChangeMovedQuantity(true, false, cMoveorderBarcode.currentMoveOrderBarcode.getQuantityPerUnitOfMeasureDbl());
        return;
    }

    // Lines, Barcodes

    private static Boolean mFindBarcodeInOrderBarcodes(String pvScannedBarcodeStr) {

        if (cMoveorder.currentMoveOrder.barcodesObl() == null || cMoveorder.currentMoveOrder.barcodesObl().size() == 0) {
            return false;
        }

        for (cMoveorderBarcode moveorderBarcode : cMoveorder.currentMoveOrder.barcodesObl()) {
            if (moveorderBarcode.getBarcodeStr().equalsIgnoreCase(pvScannedBarcodeStr) || moveorderBarcode.getBarcodeWithoutCheckDigitStr().equalsIgnoreCase(pvScannedBarcodeStr)) {
                cMoveorderBarcode.currentMoveOrderBarcode = moveorderBarcode;
                return true;
            }
        }

        return false;
    }



    private static Boolean mSendMoveorderLine() {

        //If internet is not connected
        if (!cConnection.isInternetConnectedBln()) {
            //could not send line, let user know but answer succes so user can go to next line
            cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.couldnt_send_line), null);
            cMoveorderLine.currentMoveOrderLine.pErrorSendingBln();
            return true;
        }

        if (cMoveorderLine.currentMoveOrderLine.pMoveItemHandledBln() == false) {
            //could not send line, let user know but answer succes so user can go to next line
            cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.couldnt_send_line), null);
            cMoveorderLine.currentMoveOrderLine.pErrorSendingBln();
            return true;
        }

        return true;
    }

    private void mHandleMoveDoneClick() {
        //All is done
        this.mMoveDone();

    }

    private static void mMoveDone() {

        MoveorderTakeActivity.mSendMoveorderLine();

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
                if (cMoveorderBarcode.currentMoveOrderBarcode == null) {
                    cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.message_select_one_of_multiple_barcodes),null);
                    return;
                }

                mTryToChangeMovedQuantity(true, false, cMoveorderBarcode.currentMoveOrderBarcode.getQuantityPerUnitOfMeasureDbl());
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
                if (cMoveorderBarcode.currentMoveOrderBarcode == null) {
                    cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.message_select_one_of_multiple_barcodes),null);
                    return;
                }


                mTryToChangeMovedQuantity(false, false, cMoveorderBarcode.currentMoveOrderBarcode.getQuantityPerUnitOfMeasureDbl());
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
    }

    //Dialogs and Activitys

    private static void mShowFullArticleFragment() {

        cUserInterface.pCheckAndCloseOpenDialogs();

        ArticleFullViewFragment articleFullViewFragment = new ArticleFullViewFragment();
        articleFullViewFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.ARTICLEFULL_TAG);



    }

    private void mShowAcceptFragment(){

        cUserInterface.pCheckAndCloseOpenDialogs();

//        final AcceptRejectFragment acceptRejectFragment = new AcceptRejectFragment(cAppExtension.activity.getString(R.string.message_moveorderbusy_header),
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
        bundle.putInt(cPublicDefinitions.NUMBERINTENT_CURRENTQUANTITY, cMoveorderLine.currentMoveOrderLine.getQuantityHandledDbl().intValue());
        bundle.putDouble(cPublicDefinitions.NUMBERINTENT_MAXQUANTITY, cMoveorderLine.currentMoveOrderLine.getQuantityDbl().intValue());

        NumberpickerFragment numberpickerFragment = new NumberpickerFragment();
        numberpickerFragment.setArguments(bundle);

        numberpickerFragment.show(cAppExtension.fragmentManager, NUMBERPICKERFRAGMENT_TAG);
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

