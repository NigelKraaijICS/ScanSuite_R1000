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
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import ICS.Interfaces.iICSDefaultActivity;
import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cConnection;
import ICS.Utils.cRegex;
import ICS.Utils.cResult;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Basics.BarcodeLayouts.cBarcodeLayout;
import SSU_WHS.Basics.BranchBin.cBranchBin;
import SSU_WHS.Basics.Settings.cSetting;
import SSU_WHS.Basics.Users.cUser;
import SSU_WHS.General.cPublicDefinitions;
import SSU_WHS.Intake.IntakeorderBarcodes.cIntakeorderBarcode;
import SSU_WHS.Intake.IntakeorderMATLineSummary.cIntakeorderMATSummaryLine;
import SSU_WHS.Intake.IntakeorderMATLines.cIntakeorderMATLine;
import SSU_WHS.Intake.IntakeorderMATLines.cIntakeorderMATLineRecyclerItemTouchHelper;
import SSU_WHS.Intake.Intakeorders.cIntakeorder;
import SSU_WHS.Move.MoveOrders.cMoveorder;
import SSU_WHS.Move.MoveorderLines.cMoveorderLine;
import nl.icsvertex.scansuite.Activities.Intake.IntakeorderLinesActivity;
import nl.icsvertex.scansuite.Fragments.Dialogs.AcceptRejectFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.ArticleFullViewFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.BarcodeFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.NumberpickerFragment;
import nl.icsvertex.scansuite.R;

import static ICS.Utils.cText.pDoubleToStringStr;

public class MoveLineTakeActivity extends AppCompatActivity implements iICSDefaultActivity {

    //Region Private Properties
    static final String NUMBERFRAGMENT_TAG = "NUMBERFRAGMENT_TAG";
    static final String BARCODEFRAGMENT_TAG = "BARCODEFRAGMENT_TAG";
    static final String ACCEPTREJECTFRAGMENT_TAG = "ACCEPTREJECTFRAGMENT_TAG";

    private static int counterMinusHelperInt;
    private static int counterPlusHelperInt;
    private static Handler minusHandler;
    private static Handler plusHandler;
    private static AppCompatImageButton imageButtonMinus;
    private static AppCompatImageButton imageButtonPlus;
    private static AppCompatImageButton imageButtonDone;

    private static ConstraintLayout container;

    private static ImageView toolbarImage;
    private static TextView toolbarTitle;
    private static TextView toolbarSubtext;

    private static TextView articleDescriptionText;
    private static TextView articleDescription2Text;
    private static TextView articleItemText;
    private static TextView articleBarcodeText;
    private static TextView articleVendorItemText;
    private static TextView genericItemExtraField1Text;
    private static TextView genericItemExtraField2Text;
    private static TextView genericItemExtraField3Text;
    private static TextView genericItemExtraField4Text;
    private static TextView genericItemExtraField5Text;
    private static TextView genericItemExtraField6Text;
    private static TextView genericItemExtraField7Text;
    private static TextView genericItemExtraField8Text;
    private static ImageView articleThumbImageView;
    private static ImageView imageButtonBarcode;

    private static CardView binContainer;
    private static TextView quantityText;
    private static TextView quantityRequiredText;

    private static Boolean articleScannedBln = false;
    private static Boolean binScannedBln = false;
    private static Double quantityScannedDbl = 0.0;
    private static List<cIntakeorderBarcode> scannedBarcodesObl;

    //End Region Private Properties

    //Region Default Methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_moveline_take);
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
        super.onResume();
        LocalBroadcastManager.getInstance(cAppExtension.context).registerReceiver(mNumberReceiver, new IntentFilter(cPublicDefinitions.NUMBERINTENT_NUMBER));
        cBarcodeScan.pRegisterBarcodeReceiver();
        cUserInterface.pEnableScanner();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            if (MoveLineTakeActivity.quantityScannedDbl == 0 ) {
                MoveLineTakeActivity.mResetCurrents();
                MoveLineTakeActivity.mGoBackToLinesActivity();
                return  true;
            }

            this.mShowAcceptFragment();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        if (MoveLineTakeActivity.quantityScannedDbl == 0 ) {
            MoveLineTakeActivity.mResetCurrents();
            MoveLineTakeActivity.mGoBackToLinesActivity();
            return;
        }

        this.mShowAcceptFragment();
    }

    //End Region Default Methods

    //Region iICSDefaultActivity Methods

    @Override
    public void mActivityInitialize() {

        this.mSetAppExtensions();

        this.mFindViews();

        this.mSetToolbar(getResources().getString(R.string.screentitle_moveline_take));

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

        MoveLineTakeActivity.container = findViewById(R.id.container);
        MoveLineTakeActivity.toolbarImage = findViewById(R.id.toolbarImage);
        MoveLineTakeActivity.toolbarTitle = findViewById(R.id.toolbarTitle);
        MoveLineTakeActivity.toolbarSubtext = findViewById(R.id.toolbarSubtext);

        MoveLineTakeActivity.articleDescriptionText = findViewById(R.id.articleDescriptionText);
        MoveLineTakeActivity.articleDescription2Text = findViewById(R.id.articleDescription2Text);
        MoveLineTakeActivity.articleItemText = findViewById(R.id.articleItemText);
        MoveLineTakeActivity.articleBarcodeText = findViewById(R.id.articleBarcodeText);
        MoveLineTakeActivity.articleVendorItemText = findViewById(R.id.articleVendorItemText);

        MoveLineTakeActivity.binContainer = findViewById(R.id.binContainer);
        MoveLineTakeActivity.genericItemExtraField1Text = findViewById(R.id.genericItemExtraField1Text);
        MoveLineTakeActivity.genericItemExtraField2Text = findViewById(R.id.genericItemExtraField2Text);
        MoveLineTakeActivity.genericItemExtraField3Text = findViewById(R.id.genericItemExtraField3Text);
        MoveLineTakeActivity.genericItemExtraField4Text = findViewById(R.id.genericItemExtraField4Text);
        MoveLineTakeActivity.genericItemExtraField5Text = findViewById(R.id.genericItemExtraField5Text);
        MoveLineTakeActivity.genericItemExtraField6Text = findViewById(R.id.genericItemExtraField6Text);
        MoveLineTakeActivity.genericItemExtraField7Text = findViewById(R.id.genericItemExtraField7Text);
        MoveLineTakeActivity.genericItemExtraField8Text = findViewById(R.id.genericItemExtraField8Text);

        MoveLineTakeActivity.quantityText = findViewById(R.id.quantityText);
        MoveLineTakeActivity.quantityRequiredText = findViewById(R.id.quantityRequiredText);

        MoveLineTakeActivity.articleThumbImageView = findViewById(R.id.articleThumbImageView);
        MoveLineTakeActivity.imageButtonBarcode = findViewById(R.id.imageButtonBarcode);
        MoveLineTakeActivity.imageButtonMinus = findViewById(R.id.imageButtonMinus);
        MoveLineTakeActivity.imageButtonPlus = findViewById(R.id.imageButtonPlus);
        MoveLineTakeActivity.imageButtonDone = findViewById(R.id.imageButtonDone);

    }

    @Override
    public void mSetToolbar(String pvScreenTitle) {
        MoveLineTakeActivity.toolbarImage.setImageResource(R.drawable.ic_menu_move);
        MoveLineTakeActivity.toolbarTitle.setText(pvScreenTitle);
        MoveLineTakeActivity.toolbarTitle.setSelected(true);
        MoveLineTakeActivity.toolbarSubtext.setSelected(true);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void mFieldsInitialize() {
        MoveLineTakeActivity.mFieldsInitializeStatic();
    }

    @Override
    public void mInitScreen() {
        cBarcodeScan.pRegisterBarcodeReceiver();
        //Raise quantity with scanned barcodeStr, if we started this activity with a scan
        if (cMoveorder.currentMoveOrder.currentMoveorderLineBarcode != null) {
            MoveLineTakeActivity.pHandleScan(cBarcodeScan.pFakeScan(cMoveorder.currentMoveOrder.currentMoveorderLineBarcode.getBarcodeStr()));
            cMoveorder.currentMoveOrder.currentMoveorderLineBarcode = null;
        }
    }

    @Override
    public void mSetListeners() {

        this.mSetArticleImageListener();
        this.mSetImageButtonBarcodeListener();

        if (cMoveorder.currentMoveOrder.getMoveAmountManualBln()) {
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

        boolean binCheckedBln = false;

        //This barcode matches multiple lay-outs so this can be a BIN or an article
        if (cBarcodeLayout.pGetBarcodeLayoutByBarcodeObl(pvBarcodeScan.getBarcodeOriginalStr()).size() > 1) {

            //First check if this is a BIN
            cBranchBin branchBin =  cUser.currentUser.currentBranch.pGetBinByCode(cRegex.pStripRegexPrefixStr(pvBarcodeScan.getBarcodeOriginalStr()));

            if (branchBin != null) {

                hulpRst = MoveLineTakeActivity.mHandleBINScanRst(pvBarcodeScan);
                if (! hulpRst.resultBln) {
                    cUserInterface.pShowSnackbarMessage(container,hulpRst.messagesStr(),null,true);
                    return;
                }

                //We are done
                return;
            }

            binCheckedBln = true;

        }

        //Check if we have scanned a BIN and check if there are not handled linesInt for this BIN
        if (!binCheckedBln && cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(), cBarcodeLayout.barcodeLayoutEnu.BIN)) {

            hulpRst = MoveLineTakeActivity.mHandleBINScanRst(pvBarcodeScan);
            if (! hulpRst.resultBln) {
                cUserInterface.pShowSnackbarMessage(container,hulpRst.messagesStr(),null,true);
                return;
            }

            //We are done
            return;

        }

        //Check if we have scanned an ARTICLE and check if there are not handled linesInt for this BIN
        if (cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(), cBarcodeLayout.barcodeLayoutEnu.ARTICLE)) {

            hulpRst = MoveLineTakeActivity.mHandleArticleScanRst(pvBarcodeScan);
            if (! hulpRst.resultBln) {
                cUserInterface.pDoExplodingScreen(hulpRst.messagesStr(),"",true,true);
                return;
            }
            return;
        }

        cUserInterface.pShowSnackbarMessage(container,cAppExtension.activity.getString(R.string.message_unknown_barcode),null,true);

    }

    public static void pAcceptMove() {


        if (cMoveorder.currentMoveOrder.currentBranchBin == null) {
            cUserInterface.pShowSnackbarMessage(container, cAppExtension.activity.getString(R.string.message_bin_required),null, true);
            return;
        }
        if (cMoveorder.currentMoveOrder.currentArticle == null) {
            cUserInterface.pShowSnackbarMessage(container, cAppExtension.activity.getString(R.string.message_article_required),null, true);
            return;
        }


        if (MoveLineTakeActivity.quantityScannedDbl == 0 ) {
            MoveLineTakeActivity.mResetCurrents();
            MoveLineTakeActivity.mGoBackToLinesActivity();
            return;
        }

        //We are done
         MoveLineTakeActivity.mStoreDone();

    }

    public  static  void pRegisterBarcodeReceiver(){
        cBarcodeScan.pRegisterBarcodeReceiver();
    }

    public static void pCancelMove() {
        MoveLineTakeActivity.mResetCurrents();
        MoveLineTakeActivity.mGoBackToLinesActivity();
    }

    //End Region Public Methods

    //Region Private Methods

    //Views
    private static void mFieldsInitializeStatic(){

        MoveLineTakeActivity.imageButtonDone.setImageResource(R.drawable.ic_check_black_24dp);

        MoveLineTakeActivity.counterPlusHelperInt = 0;
        MoveLineTakeActivity.counterMinusHelperInt = 0;
        MoveLineTakeActivity.toolbarSubtext.setText(cMoveorder.currentMoveOrder.getOrderNumberStr());

        MoveLineTakeActivity.mSetArticleInfo();
        MoveLineTakeActivity.mSetBinInfo();
        MoveLineTakeActivity.mSetQuantityInfo();

        MoveLineTakeActivity.mEnablePlusMinusAndBarcodeSelectViews();
        MoveLineTakeActivity.mShowArticleImage();
        MoveLineTakeActivity.mShowOrHideGenericExtraFields();
        MoveLineTakeActivity.mShowBarcodeInfo();

    }


    private void mSetArticleImageListener() {
        MoveLineTakeActivity.articleThumbImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mShowFullArticleFragment();
            }
        });
    }

    private void mSetImageButtonBarcodeListener() {
        MoveLineTakeActivity.imageButtonBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View pvView) {

                if (cMoveorder.currentMoveOrder.currentArticle.barcodesObl == null || cMoveorder.currentMoveOrder.currentArticle.barcodesObl.size() == 0) {
                    return;
                }

                mEnablePlusMinusAndBarcodeSelectViews();

                //If we only have one barcodeStr, then automatticaly select that barcodeStr
                if (cMoveorder.currentMoveOrder.currentArticle.barcodesObl.size() == 1) {
                    MoveLineTakeActivity.pHandleScan(cBarcodeScan.pFakeScan(cMoveorder.currentMoveOrder.currentArticle.barcodesObl.get(0).getBarcodeStr()));
                    return;
                }

                mShowBarcodeSelectFragment();

            }
        });
    }

    private static void mShowOrHideGenericExtraFields() {

        if (cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine == null) {
            MoveLineTakeActivity.genericItemExtraField1Text.setVisibility(View.INVISIBLE);
            MoveLineTakeActivity.genericItemExtraField2Text.setVisibility(View.INVISIBLE);
            MoveLineTakeActivity.genericItemExtraField3Text.setVisibility(View.INVISIBLE);
            MoveLineTakeActivity.genericItemExtraField4Text.setVisibility(View.INVISIBLE);
            MoveLineTakeActivity.genericItemExtraField5Text.setVisibility(View.INVISIBLE);
            MoveLineTakeActivity.genericItemExtraField6Text.setVisibility(View.INVISIBLE);
            MoveLineTakeActivity.genericItemExtraField7Text.setVisibility(View.INVISIBLE);
            MoveLineTakeActivity.genericItemExtraField8Text.setVisibility(View.INVISIBLE);
            return;
        }

        if (!cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getExtraField1Str().isEmpty()) {
            MoveLineTakeActivity.genericItemExtraField1Text.setVisibility(View.VISIBLE);
            MoveLineTakeActivity.genericItemExtraField1Text.setText(cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getExtraField1Str());
        }

        if (!cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getExtraField2Str().isEmpty()) {
            MoveLineTakeActivity.genericItemExtraField2Text.setVisibility(View.VISIBLE);
            MoveLineTakeActivity.genericItemExtraField2Text.setText(cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getExtraField2Str());
        }

        if (!cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getExtraField3Str().isEmpty()) {
            MoveLineTakeActivity.genericItemExtraField3Text.setVisibility(View.VISIBLE);
            MoveLineTakeActivity.genericItemExtraField3Text.setText(cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getExtraField3Str());
        }

        if (!cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getExtraField4Str().isEmpty()) {
            MoveLineTakeActivity.genericItemExtraField4Text.setVisibility(View.VISIBLE);
            MoveLineTakeActivity.genericItemExtraField4Text.setText(cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getExtraField4Str());
        }

        if (!cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getExtraField5Str().isEmpty()) {
            MoveLineTakeActivity.genericItemExtraField5Text.setVisibility(View.VISIBLE);
            MoveLineTakeActivity.genericItemExtraField5Text.setText(cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getExtraField5Str());
        }

        if (!cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getExtraField6Str().isEmpty()) {
            MoveLineTakeActivity.genericItemExtraField6Text.setVisibility(View.VISIBLE);
            MoveLineTakeActivity.genericItemExtraField6Text.setText(cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getExtraField6Str());
        }

        if (!cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getExtraField7Str().isEmpty()) {
            MoveLineTakeActivity.genericItemExtraField7Text.setVisibility(View.VISIBLE);
            MoveLineTakeActivity.genericItemExtraField7Text.setText(cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getExtraField7Str());
        }

        if (!cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getExtraField8Str().isEmpty()) {
            MoveLineTakeActivity.genericItemExtraField8Text.setVisibility(View.VISIBLE);
            MoveLineTakeActivity.genericItemExtraField8Text.setText(cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getExtraField8Str());
        }

    }

    private static void mShowBarcodeInfo() {

        if (cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine == null) {
            MoveLineTakeActivity.articleBarcodeText.setText("???");
            return;
        }

        if (cIntakeorderBarcode.currentIntakeOrderBarcode == null) {
            if (cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.barcodesObl().size() == 1) {
                cIntakeorderBarcode.currentIntakeOrderBarcode = cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.barcodesObl().get(0);
            }
        }

        if (cIntakeorderBarcode.currentIntakeOrderBarcode!= null) {
            MoveLineTakeActivity.articleBarcodeText.setText(cIntakeorderBarcode.currentIntakeOrderBarcode.getBarcodeAndQuantityStr());
        } else {
            MoveLineTakeActivity.articleBarcodeText.setText(cAppExtension.context.getString(R.string.mutiple_barcodes_posible));
        }
    }

    private static void mShowArticleImage() {

        MoveLineTakeActivity.articleThumbImageView.setImageDrawable(ContextCompat.getDrawable(cAppExtension.context, R.drawable.ic_no_image_lightgrey_24dp));

        //If pick with picture is false, then hide image view
        if (!cIntakeorder.currentIntakeOrder.isReceiveWithPictureBln()) {
            MoveLineTakeActivity.articleThumbImageView.setVisibility(View.INVISIBLE);
            return;
        }

        //If picture is not in cache (via webservice) then show no image
        if (!cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.pGetArticleImageBln()) {
            cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.could_not_get_article_image), null);
            MoveLineTakeActivity.articleThumbImageView.setImageDrawable(ContextCompat.getDrawable(cAppExtension.context, R.drawable.ic_no_image_lightgrey_24dp));
            return;
        }

        //If picture is in cache but can't be converted, then show no image
        if (cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.articleImage == null || cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.articleImage.imageBitmap() == null) {
            cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.could_not_get_article_image), null);
            MoveLineTakeActivity.articleThumbImageView.setImageDrawable(ContextCompat.getDrawable(cAppExtension.context, R.drawable.ic_no_image_lightgrey_24dp));
            return;
        }

        //Show the image
        MoveLineTakeActivity.articleThumbImageView.setImageBitmap(cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.articleImage.imageBitmap());

        //Open the image
        if ((cIntakeorder.currentIntakeOrder.isReceiveWithAutoOpenBln())) {
            MoveLineTakeActivity.mShowFullArticleFragment();
        }

    }

    private static void mEnablePlusMinusAndBarcodeSelectViews() {

        if (cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine == null) {
            MoveLineTakeActivity.imageButtonMinus.setVisibility(View.INVISIBLE);
            MoveLineTakeActivity.imageButtonPlus.setVisibility(View.INVISIBLE);
            MoveLineTakeActivity.imageButtonBarcode.setVisibility(View.INVISIBLE);
            MoveLineTakeActivity.imageButtonDone.setVisibility(View.INVISIBLE);
            return;
        }

        MoveLineTakeActivity.imageButtonDone.setVisibility(View.VISIBLE);

        if (!cSetting.RECEIVE_AMOUNT_MANUAL_MA()) {
            MoveLineTakeActivity.imageButtonMinus.setVisibility(View.INVISIBLE);
            MoveLineTakeActivity.imageButtonPlus.setVisibility(View.INVISIBLE);
            MoveLineTakeActivity.imageButtonBarcode.setVisibility(View.INVISIBLE);
        } else {
            MoveLineTakeActivity.imageButtonMinus.setVisibility(View.VISIBLE);
            MoveLineTakeActivity.imageButtonPlus.setVisibility(View.VISIBLE);
            MoveLineTakeActivity.imageButtonBarcode.setVisibility(View.VISIBLE);
        }
    }

    //Scans and manual input

    private static cResult mHandleArticleScanRst(cBarcodeScan pvBarcodeScan){

        cResult result = new cResult();
        result.resultBln = true;

        cIntakeorderBarcode intakeorderBarcode = cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.pGetBarcode(pvBarcodeScan);
        if (intakeorderBarcode == null) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.activity.getString(R.string.message_unknown_barcode_for_this_line));
            return result;
        }

        MoveLineTakeActivity.mBarcodeSelected(intakeorderBarcode);
        return result;

    }

    private static cResult mHandleBINScanRst(cBarcodeScan pvBarcodescan){

        cResult result = new cResult();
        result.resultBln = true;

        //We scanned a BIN, but we need to scan an article first
        if (! MoveLineTakeActivity.articleScannedBln) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.activity.getString(R.string.message_scan_article_first));
            return result;
        }

        //Strip barcodeStr from regex
        String barcodeWithoutPrefixStr = cRegex.pStripRegexPrefixStr(pvBarcodescan.getBarcodeOriginalStr());

        //Get the current BIN

//todo
//        MoveLineTakeActivity.currentBin =  cUser.currentUser.currentBranch.pGetBinByCode(barcodeWithoutPrefixStr);
//        if ( MoveLineTakeActivity.currentBin  == null) {
//            result.resultBln = false;
//            result.pAddErrorMessage(cAppExtension.activity.getString(R.string.message_bin_unknown,barcodeWithoutPrefixStr));
//            return result;
//        }
//
//        //Get the current line based on the scanned BIN
//        cIntakeorderMATLine.currentIntakeorderMATLine = cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.pGetLineForBinCode(MoveLineTakeActivity.currentBin);
//
//        //We could not find a match, and we are not allowed to add BINS
//        if (cIntakeorderMATLine.currentIntakeorderMATLine == null && cIntakeorder.currentIntakeOrder.getReceiveNoExtraBinsBln()) {
//            MoveLineTakeActivity.currentBin = null;
//            result.resultBln = false;
//            result.pAddErrorMessage(cAppExtension.activity.getString(R.string.message_adding_or_changing_bin_now_allowed));
//            return result;
//        }

        //Current BIN has been set, so BIN is scanned
        MoveLineTakeActivity.binScannedBln = true;
        return  result;

    }

    private void mNumberClicked() {

        if (cIntakeorderBarcode.currentIntakeOrderBarcode == null) {
            cUserInterface.pDoNope(quantityText, false, false);
            cUserInterface.pShowSnackbarMessage(container, getString(R.string.choose_barcode_first), null, false);
            return;
        }

        if (cIntakeorderBarcode.currentIntakeOrderBarcode.getQuantityHandledDbl() > 1) {
            cUserInterface.pDoNope(quantityText, true, true);
            cUserInterface.pShowSnackbarMessage(container, getString(R.string.manual_input_only_barcodenumber_bigger1), null, false);
            return;
        }

        this.mShowNumberFragment();
    }

    private void mHandleQuantityChosen(double pvQuantityDbl) {


        if (pvQuantityDbl == 0) {
            MoveLineTakeActivity.mTryToChangeQuantity(false, true,pvQuantityDbl);
        }
        else {
            MoveLineTakeActivity.mTryToChangeQuantity(true, true,pvQuantityDbl);
        }

    }

    private static void mTryToChangeQuantity(Boolean pvIsPositiveBln, Boolean pvAmountFixedBln, double pvAmountDbl) {

      double newQuantityDbl;


        if ( MoveLineTakeActivity.scannedBarcodesObl == null) {
            MoveLineTakeActivity.scannedBarcodesObl = new ArrayList<>();
        }

        if (pvIsPositiveBln) {

            //Determine the new amount
            if (pvAmountFixedBln) {
                newQuantityDbl = pvAmountDbl;


                //Clear the barcodeStr list and refill it
                MoveLineTakeActivity.scannedBarcodesObl.clear();
                int countInt = 0;
                do{
                    countInt += 1;
                    //Add a barcodeStr to the scanned barcodeStr list, so you can use it later when line is determined
                    MoveLineTakeActivity.scannedBarcodesObl.add(cIntakeorderBarcode.currentIntakeOrderBarcode);
                }
                while(countInt < newQuantityDbl);

                //Update activity and Check if this line is done
                MoveLineTakeActivity.quantityScannedDbl = newQuantityDbl;
                MoveLineTakeActivity.quantityText.setText(pDoubleToStringStr(MoveLineTakeActivity.quantityScannedDbl));
                MoveLineTakeActivity.mCheckLineDone();
                return;


            } else {
                newQuantityDbl = MoveLineTakeActivity.quantityScannedDbl + pvAmountDbl;
            }

            //Check if we would exceed amount, then show message if needed
            if (newQuantityDbl > cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getQuantityDbl()) {

                if (cIntakeorder.currentIntakeOrder.getReceiveNoExtraPiecesBln()) {
                    MoveLineTakeActivity.mShowExtraPiecesNotAllowed();

                    if (MoveLineTakeActivity.quantityScannedDbl> 0) {
                        MoveLineTakeActivity.articleScannedBln = true;
                        return;
                    }

                    MoveLineTakeActivity.articleScannedBln = false;
                    return;
                }
            }

            //Set the new quantityDbl and show in Activity
            MoveLineTakeActivity.quantityScannedDbl = newQuantityDbl;
            MoveLineTakeActivity.quantityText.setText(pDoubleToStringStr(MoveLineTakeActivity.quantityScannedDbl));

            //Add a barcodeStr to the scanned barcodeStr list, so you can use it later when line is determined
            MoveLineTakeActivity.scannedBarcodesObl.add(cIntakeorderBarcode.currentIntakeOrderBarcode);

            //Check if this line is done
            MoveLineTakeActivity.mCheckLineDone();
            return;
        }

        //negative

        //Check if value already is zero
        if ( MoveLineTakeActivity.quantityScannedDbl < 1 ) {

            //If we have a decimal, correct it to zero
            if (MoveLineTakeActivity.quantityScannedDbl > 0 ) {
                pvAmountDbl = 0;
                pvAmountFixedBln = true;

            } else {
                cUserInterface.pDoNope(MoveLineTakeActivity.quantityText, true, true);
                return;
            }
        }

        if (pvAmountDbl < 0) {
            cUserInterface.pDoNope(MoveLineTakeActivity.quantityText, true, true);
            return;
        }

        //Determine the new amount
        if (pvAmountFixedBln) {
            newQuantityDbl = pvAmountDbl;

            //Clear the barcodeStr list and refill it
            MoveLineTakeActivity.scannedBarcodesObl.clear();
            int countInt = 0;
            do{
                countInt += 1;
                //Add a barcodeStr to the scanned barcodeStr list, so you can use it later when line is determined
                MoveLineTakeActivity.scannedBarcodesObl.add(cIntakeorderBarcode.currentIntakeOrderBarcode);
                            }while(countInt < newQuantityDbl);

            //Set the new quantityDbl and show in Activity
            MoveLineTakeActivity.quantityScannedDbl = newQuantityDbl;
            MoveLineTakeActivity.quantityText.setText(pDoubleToStringStr(MoveLineTakeActivity.quantityScannedDbl));
            return;


        }else {
            //Remove the last barcodeStr in the list
            MoveLineTakeActivity.scannedBarcodesObl.remove( MoveLineTakeActivity.scannedBarcodesObl.size()-1);
            newQuantityDbl= MoveLineTakeActivity.quantityScannedDbl - pvAmountDbl;
        }

        if (newQuantityDbl <= 0) {
            MoveLineTakeActivity.quantityScannedDbl = (double) 0;
        }else {
            //Set the new quantityDbl and show in Activity
            MoveLineTakeActivity.quantityScannedDbl = newQuantityDbl;
        }

        MoveLineTakeActivity.quantityText.setText(pDoubleToStringStr(MoveLineTakeActivity.quantityScannedDbl));
        MoveLineTakeActivity.imageButtonDone.setImageResource(R.drawable.ic_check_black_24dp);
    }

    private static void mBarcodeSelected(cIntakeorderBarcode pvBarcode) {

        cUserInterface.pCheckAndCloseOpenDialogs();

        cIntakeorderBarcode.currentIntakeOrderBarcode = pvBarcode;
        MoveLineTakeActivity.articleScannedBln = true;
        MoveLineTakeActivity.mShowBarcodeInfo();

        MoveLineTakeActivity.mTryToChangeQuantity(true, false, cIntakeorderBarcode.currentIntakeOrderBarcode.getQuantityPerUnitOfMeasureDbl());
    }

    // Lines, Barcodes

    private static void mCheckLineDone() {

        //Start with complete
        MoveLineTakeActivity.imageButtonDone.setVisibility(View.VISIBLE);

        //If article or BIN hasn't been scanned then we are not done
        if (!MoveLineTakeActivity.articleScannedBln || !MoveLineTakeActivity.binScannedBln) {
            MoveLineTakeActivity.imageButtonDone.setVisibility(View.INVISIBLE);
            return;
        }

        //Check if quantityDbl is sufficient
        if (!cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.pQuantityReachedBln(MoveLineTakeActivity.scannedBarcodesObl)) {
            MoveLineTakeActivity.imageButtonDone.setVisibility(View.VISIBLE);
            MoveLineTakeActivity.imageButtonDone.setImageResource(R.drawable.ic_check_black_24dp);
            return;
        }


        //We are complete
        MoveLineTakeActivity.imageButtonDone.setVisibility(View.VISIBLE);
        MoveLineTakeActivity.imageButtonDone.setImageResource(R.drawable.ic_doublecheck_black_24dp);

        //If auto accept is false, we are done here
        if (!cIntakeorder.currentIntakeOrder.getReceiveStoreAutoAcceptAtRequestedBln()) {
            return;
        }

        //All is done
        MoveLineTakeActivity.mStoreDone();

    }

    private static void mSendLine() {

        //If internet is not connected
        if (!cConnection.isInternetConnectedBln()) {
            //could not send line, let user know but answer succes so user can go to next line
            cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.couldnt_send_line_no_connection), null);
            return;
        }

        //todo
//       if(!cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.pItemVariantHandledBln(MoveLineTakeActivity.currentBin.getBinCodeStr(),
//            MoveLineTakeActivity.scannedBarcodesObl)) {
//            cUserInterface.pDoExplodingScreen(cAppExtension.context.getString(R.string.couldnt_send_line), "",true,true);
//            return;
//        }

        MoveLineTakeActivity.mResetCurrents();
        MoveLineTakeActivity.mGoBackToLinesActivity();

    }

     private static void mStoreDone() {
        MoveLineTakeActivity.mSendLine();
     }

     //ScanActions
     private static void mFillRecycler(List<cIntakeorderMATLine> pvDataObl) {
        //todo
//         cIntakeorderMATLine.getIntakeorderMATLineAdapter().pFillData(pvDataObl);
//         MoveLineTakeActivity.recyclerScanActions.setHasFixedSize(false);
//         MoveLineTakeActivity.recyclerScanActions.setAdapter( cIntakeorderMATLine.getIntakeorderMATLineAdapter());
//         MoveLineTakeActivity.recyclerScanActions.setLayoutManager(new LinearLayoutManager(cAppExtension.context));
     }

    //Listeners
    private void mSetDoneListener() {
        MoveLineTakeActivity.imageButtonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               MoveLineTakeActivity.pAcceptMove();
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void mSetPlusListener() {

        MoveLineTakeActivity.imageButtonPlus.setOnTouchListener(new View.OnTouchListener() {

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

                //There is no selected barcodeStr, select one first
                if (cIntakeorderBarcode.currentIntakeOrderBarcode == null) {
                    cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.message_select_one_of_multiple_barcodes),null);
                    return;
                }
                MoveLineTakeActivity.pHandleScan(cBarcodeScan.pFakeScan(cIntakeorderBarcode.currentIntakeOrderBarcode.getBarcodeStr()));
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void mSetMinusListener() {

        MoveLineTakeActivity.imageButtonMinus.setOnTouchListener(new View.OnTouchListener() {
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

        MoveLineTakeActivity.imageButtonMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //There is no selected barcodeStr, select one first
                if (cIntakeorderBarcode.currentIntakeOrderBarcode == null) {
                    cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.message_select_one_of_multiple_barcodes),null);
                    return;
                }


                mTryToChangeQuantity(false, false, cIntakeorderBarcode.currentIntakeOrderBarcode.getQuantityPerUnitOfMeasureDbl());
            }
        });
    }

    private void mSetNumberListener() {
        MoveLineTakeActivity.quantityText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNumberClicked();
            }
        });
        MoveLineTakeActivity.quantityRequiredText.setOnClickListener(new View.OnClickListener() {
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

        final AcceptRejectFragment acceptRejectFragment = new AcceptRejectFragment(cAppExtension.activity.getString(R.string.message_orderbusy_header),
                                                                                   cAppExtension.activity.getString(R.string.message_orderbusy_text),
                                                                                   cAppExtension.activity.getString(R.string.message_cancel_line), cAppExtension.activity.getString(R.string.message_accept_line), false);
        acceptRejectFragment.setCancelable(true);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // show my popup
                acceptRejectFragment.show(cAppExtension.fragmentManager, ACCEPTREJECTFRAGMENT_TAG);
            }
        });
    }

    private static void mShowExtraPiecesNotAllowed(){
        MoveLineTakeActivity.quantityText.setText(quantityRequiredText.getText());
        cUserInterface.pShowSnackbarMessage(toolbarImage , cAppExtension.context.getString(R.string.number_cannot_be_higher), null, false);
        cUserInterface.pDoNope(quantityText, true, true);
        cUserInterface.pDoNope(quantityRequiredText, false, false);
    }

    private static void mGoBackToLinesActivity() {
        Intent intent = new Intent(cAppExtension.context, IntakeorderLinesActivity.class);
        cAppExtension.activity.startActivity(intent);
        cAppExtension.activity.finish();
    }

    private static void mResetCurrents() {

        cMoveorderLine.currentMoveOrderLine = null;
        cMoveorder.currentMoveOrder.currentArticle = null;
        cMoveorder.currentMoveOrder.currentBranchBin = null;
        cMoveorder.currentMoveOrder.currentMoveorderLineBarcode = null;

        MoveLineTakeActivity.articleScannedBln = false;
        MoveLineTakeActivity.binScannedBln = false;
        MoveLineTakeActivity.scannedBarcodesObl = null;
        MoveLineTakeActivity.quantityScannedDbl = 0.0;
        MoveLineTakeActivity.scannedBarcodesObl = null;

    }

    private void mShowBarcodeSelectFragment() {
        BarcodeFragment barcodeFragment = new BarcodeFragment();
        barcodeFragment.show(cAppExtension.fragmentManager, BARCODEFRAGMENT_TAG);
    }

    private void mShowNumberFragment() {

        cUserInterface.pCheckAndCloseOpenDialogs();

        Bundle bundle = new Bundle();
        bundle.putInt(cPublicDefinitions.NUMBERINTENT_CURRENTQUANTITY, cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getQuantityHandledDbl().intValue());

        if (cIntakeorder.currentIntakeOrder.getReceiveStoreAutoAcceptAtRequestedBln()) {
            bundle.putDouble(cPublicDefinitions.NUMBERINTENT_MAXQUANTITY, cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getQuantityDbl().intValue());
        } else {
            bundle.putDouble(cPublicDefinitions.NUMBERINTENT_MAXQUANTITY,99999);
        }

        NumberpickerFragment numberpickerFragment = new NumberpickerFragment();
        numberpickerFragment.setArguments(bundle);

        numberpickerFragment.show(cAppExtension.fragmentManager, NUMBERFRAGMENT_TAG);
    }

    private static void mSetArticleInfo(){

        if (cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine == null) {
            MoveLineTakeActivity.articleDescriptionText.setText("???");
            MoveLineTakeActivity.articleDescription2Text.setText("???");
            MoveLineTakeActivity.articleItemText.setText("???");
            MoveLineTakeActivity.articleVendorItemText.setText("???");
            return;
        }
            MoveLineTakeActivity.articleDescriptionText.setText(cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getDescriptionStr());
            MoveLineTakeActivity.articleDescription2Text.setText(cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getDescription2Str());
            MoveLineTakeActivity.articleItemText.setText(cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getItemNoAndVariantCodeStr());
            MoveLineTakeActivity.articleVendorItemText.setText(cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getVendorItemNoAndDescriptionStr());
    }

    private static void mSetQuantityInfo(){

        if (cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine == null) {
            MoveLineTakeActivity.quantityText.setText("0");
            MoveLineTakeActivity.quantityRequiredText.setText("???");
                return;
        }

        MoveLineTakeActivity.quantityText.setText("0");
        MoveLineTakeActivity.quantityRequiredText.setText(pDoubleToStringStr(cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.pGetQuantityToHandleDbl()));

    }

    private static void mSetBinInfo(){
        MoveLineTakeActivity.binContainer.setVisibility(View.GONE);
    }

    private static void mSetSourceNoInfo(){
        //todo
//        MoveLineTakeActivity.sourcenoText.setText(cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getSourceNoStr());
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
        MoveLineTakeActivity.minusHandler.postDelayed(pvRunnable, pvMilliSecsLng);
        MoveLineTakeActivity.counterMinusHelperInt += 1;
    }

    private void mDoDelayedPlus(Runnable pvRunnable, long pvMilliSecsLng) {
        MoveLineTakeActivity.plusHandler.postDelayed(pvRunnable, pvMilliSecsLng);
        MoveLineTakeActivity.counterPlusHelperInt += 1;
    }

    private void mRemoveAdapterFromFragment(){

        //remove the item from recyclerview
        boolean resultBln = cIntakeorderMATLine.currentIntakeorderMATLine.pResetBln();
        if (! resultBln) {
            cUserInterface.pDoExplodingScreen(cAppExtension.activity.getString(R.string.message_reset_line_via_webservice_failed),"",true,true);
            return;
        }

        //Renew data, so only current lines are shown
        MoveLineTakeActivity.mFieldsInitializeStatic();

    }



    //End Region Number Broadcaster

    //End Regin Private Methods

}

