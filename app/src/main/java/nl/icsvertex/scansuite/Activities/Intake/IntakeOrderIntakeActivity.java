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
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import SSU_WHS.Intake.IntakeorderMATLines.cIntakeorderMATLineAdapter;
import SSU_WHS.Intake.IntakeorderMATLines.cIntakeorderMATLineRecyclerItemTouchHelper;
import SSU_WHS.Intake.Intakeorders.cIntakeorder;
import nl.icsvertex.scansuite.Fragments.Dialogs.AcceptRejectFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.ArticleFullViewFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.BarcodeFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.NumberpickerFragment;
import nl.icsvertex.scansuite.R;

import static ICS.Utils.cText.pDoubleToStringStr;

public class IntakeOrderIntakeActivity extends AppCompatActivity implements iICSDefaultActivity, cIntakeorderMATLineRecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

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

 private static ConstraintLayout intakeorderIntakeContainer;

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
    private static TextView sourcenoText;

    private static Boolean articleScannedBln = false;
    private static Boolean binScannedBln = false;
    private static Double quantityScannedDbl = 0.0;
    private static List<cIntakeorderBarcode> scannedBarcodesObl;
    private static cBranchBin currentBin;

    private static RecyclerView recyclerScanActions;

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
        super.onResume();
        LocalBroadcastManager.getInstance(cAppExtension.context).registerReceiver(mNumberReceiver, new IntentFilter(cPublicDefinitions.NUMBERINTENT_NUMBER));
        cBarcodeScan.pRegisterBarcodeReceiver();
        cUserInterface.pEnableScanner();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            if (IntakeOrderIntakeActivity.quantityScannedDbl == 0 ) {
                IntakeOrderIntakeActivity.mResetCurrents();
                IntakeOrderIntakeActivity.mGoBackToLinesActivity();
                return  true;
            }

            this.mShowAcceptFragment();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        if (IntakeOrderIntakeActivity.quantityScannedDbl == 0 ) {
            IntakeOrderIntakeActivity.mResetCurrents();
            IntakeOrderIntakeActivity.mGoBackToLinesActivity();
            return;
        }

        this.mShowAcceptFragment();
    }


    @Override
    public void onSwiped(RecyclerView.ViewHolder pvViewHolder, int direction, int pvPositionInt) {

        if (!(pvViewHolder instanceof  cIntakeorderMATLineAdapter.IntakeorderMATLineViewHolder)) {
            return;
        }

        cIntakeorderMATLine.currentIntakeorderMATLine = cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.MATLinestoShowObl().get(pvPositionInt);

        //Remove the enviroment
        this.mRemoveAdapterFromFragment();

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

        IntakeOrderIntakeActivity.intakeorderIntakeContainer = findViewById(R.id.intakeorderIntakeContainer);
        IntakeOrderIntakeActivity.toolbarImage = findViewById(R.id.toolbarImage);
        IntakeOrderIntakeActivity.toolbarTitle = findViewById(R.id.toolbarTitle);
        IntakeOrderIntakeActivity.toolbarSubtext = findViewById(R.id.toolbarSubtext);

        IntakeOrderIntakeActivity.articleDescriptionText = findViewById(R.id.articleDescriptionText);
        IntakeOrderIntakeActivity.articleDescription2Text = findViewById(R.id.articleDescription2Text);
        IntakeOrderIntakeActivity.articleItemText = findViewById(R.id.articleItemText);
        IntakeOrderIntakeActivity.articleBarcodeText = findViewById(R.id.articleBarcodeText);
        IntakeOrderIntakeActivity.articleVendorItemText = findViewById(R.id.articleVendorItemText);

        IntakeOrderIntakeActivity.binContainer = findViewById(R.id.binContainer);

        IntakeOrderIntakeActivity.sourcenoText = findViewById(R.id.sourcenoText);

        IntakeOrderIntakeActivity.genericItemExtraField1Text = findViewById(R.id.genericItemExtraField1Text);
        IntakeOrderIntakeActivity.genericItemExtraField2Text = findViewById(R.id.genericItemExtraField2Text);
        IntakeOrderIntakeActivity.genericItemExtraField3Text = findViewById(R.id.genericItemExtraField3Text);
        IntakeOrderIntakeActivity.genericItemExtraField4Text = findViewById(R.id.genericItemExtraField4Text);
        IntakeOrderIntakeActivity.genericItemExtraField5Text = findViewById(R.id.genericItemExtraField5Text);
        IntakeOrderIntakeActivity.genericItemExtraField6Text = findViewById(R.id.genericItemExtraField6Text);
        IntakeOrderIntakeActivity.genericItemExtraField7Text = findViewById(R.id.genericItemExtraField7Text);
        IntakeOrderIntakeActivity.genericItemExtraField8Text = findViewById(R.id.genericItemExtraField8Text);

        IntakeOrderIntakeActivity.quantityText = findViewById(R.id.quantityText);
        IntakeOrderIntakeActivity.quantityRequiredText = findViewById(R.id.quantityRequiredText);

        IntakeOrderIntakeActivity.articleThumbImageView = findViewById(R.id.articleThumbImageView);
        IntakeOrderIntakeActivity.imageButtonBarcode = findViewById(R.id.imageButtonBarcode);
        IntakeOrderIntakeActivity.imageButtonMinus = findViewById(R.id.imageButtonMinus);
        IntakeOrderIntakeActivity.imageButtonPlus = findViewById(R.id.imageButtonPlus);
        IntakeOrderIntakeActivity.imageButtonDone = findViewById(R.id.imageButtonDone);

        IntakeOrderIntakeActivity.recyclerScanActions = findViewById(R.id.recyclerScanActions);
    }

    @Override
    public void mSetToolbar(String pvScreenTitle) {
        IntakeOrderIntakeActivity.toolbarImage.setImageResource(R.drawable.ic_menu_intake);
        IntakeOrderIntakeActivity.toolbarTitle.setText(pvScreenTitle);
        IntakeOrderIntakeActivity.toolbarTitle.setSelected(true);
        IntakeOrderIntakeActivity.toolbarSubtext.setSelected(true);
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

        //Raise quantity with scanned barcode, if we started this activity with a scan
        if (cIntakeorder.currentIntakeOrder.intakeorderBarcodeScanned != null) {

            IntakeOrderIntakeActivity.pHandleScan(cBarcodeScan.pFakeScan(cIntakeorder.currentIntakeOrder.intakeorderBarcodeScanned.getBarcodeStr()));
            cIntakeorder.currentIntakeOrder.intakeorderBarcodeScanned =null;
        }

    }

    @Override
    public void mSetListeners() {

        this.mSetArticleImageListener();
        this.mSetImageButtonBarcodeListener();

        if (cSetting.RECEIVE_AMOUNT_MANUAL_MA()) {
            this.mSetNumberListener();
        }

        this.mSetPlusListener();
        this.mSetMinusListener();
        this.mSetDoneListener();

        this.mSetRecyclerTouchListener();

    }

    // End Region iICSDefaultActivity Methods

    //Region Public Methods

    public static void pHandleScan(cBarcodeScan pvBarcodeScan) {

        cUserInterface.pCheckAndCloseOpenDialogs();

        cResult hulpRst;

        //Check if we have scanned an ARTICLE and check if there are not handled linesInt for this BIN
        if (cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(), cBarcodeLayout.barcodeLayoutEnu.ARTICLE)) {

            hulpRst = IntakeOrderIntakeActivity.mHandleArticleScanRst(pvBarcodeScan);
            if (! hulpRst.resultBln) {
                cUserInterface.pDoExplodingScreen(hulpRst.messagesStr(),"",null,true);
                return;
            }
            return;
        }


        //Check if we have scanned a BIN and check if there are not handled linesInt for this BIN
        if (cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(), cBarcodeLayout.barcodeLayoutEnu.BIN)) {

            hulpRst = IntakeOrderIntakeActivity.mHandleBINScanRst(pvBarcodeScan);
            if (! hulpRst.resultBln) {
                cUserInterface.pShowSnackbarMessage(intakeorderIntakeContainer,hulpRst.messagesStr(),null,true);
                return;
            }

            //We are done
            IntakeOrderIntakeActivity.mStoreDone();


        }

        cUserInterface.pShowSnackbarMessage(intakeorderIntakeContainer,cAppExtension.activity.getString(R.string.message_unknown_barcode),null,true);

    }

    public static void pAcceptStore() {


        if (IntakeOrderIntakeActivity.currentBin == null) {
            cUserInterface.pShowSnackbarMessage(intakeorderIntakeContainer, cAppExtension.activity.getString(R.string.message_bin_required),null, true);
            return;
        }

        if (IntakeOrderIntakeActivity.quantityScannedDbl == 0 ) {
            IntakeOrderIntakeActivity.mResetCurrents();
            IntakeOrderIntakeActivity.mGoBackToLinesActivity();
            return;
        }

        //We are done
         IntakeOrderIntakeActivity.mStoreDone();

    }

    public  static  void pRegisterBarcodeReceiver(){
        cBarcodeScan.pRegisterBarcodeReceiver();
    }

    public static void pCancelStore() {
        IntakeOrderIntakeActivity.mResetCurrents();
        IntakeOrderIntakeActivity.mGoBackToLinesActivity();
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
        IntakeOrderIntakeActivity.mSetBinInfo();
        IntakeOrderIntakeActivity.mSetSourceNoInfo();
        IntakeOrderIntakeActivity.mSetQuantityInfo();

        IntakeOrderIntakeActivity.mEnablePlusMinusAndBarcodeSelectViews();
        IntakeOrderIntakeActivity.mShowArticleImage();
        IntakeOrderIntakeActivity.mShowOrHideGenericExtraFields();
        IntakeOrderIntakeActivity.mShowBarcodeInfo();
        IntakeOrderIntakeActivity.mShowLines();

    }

    private static void mShowLines() {

        if (cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.MATLinestoShowObl() == null  || cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.MATLinestoShowObl().size() == 0) {
            IntakeOrderIntakeActivity.mFillRecycler(new ArrayList<cIntakeorderMATLine>());
            return;
        }

        IntakeOrderIntakeActivity.mFillRecycler(cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.MATLinestoShowObl());
    }

    private void mSetArticleImageListener() {
        IntakeOrderIntakeActivity.articleThumbImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mShowFullArticleFragment();
            }
        });
    }

    private void mSetImageButtonBarcodeListener() {
        IntakeOrderIntakeActivity.imageButtonBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View pvView) {

                if (cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.barcodesObl() == null ||cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.barcodesObl().size() == 0) {
                    return;
                }

                mEnablePlusMinusAndBarcodeSelectViews();

                //If we only have one barcode, then automatticaly select that barcode
                if (cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.barcodesObl().size() == 1) {
                    IntakeOrderIntakeActivity.pHandleScan(cBarcodeScan.pFakeScan(cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.barcodesObl().get(0).getBarcodeStr()));
                    return;
                }

                mShowBarcodeSelectFragment();

            }
        });
    }

    private void mSetRecyclerTouchListener(){

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new cIntakeorderMATLineRecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(IntakeOrderIntakeActivity.recyclerScanActions);
    }

    private static void mShowOrHideGenericExtraFields() {


        if (cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine == null) {
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

        if (!cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getExtraField1Str().isEmpty()) {
            IntakeOrderIntakeActivity.genericItemExtraField1Text.setVisibility(View.VISIBLE);
            IntakeOrderIntakeActivity.genericItemExtraField1Text.setText(cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getExtraField1Str());
        }

        if (!cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getExtraField2Str().isEmpty()) {
            IntakeOrderIntakeActivity.genericItemExtraField2Text.setVisibility(View.VISIBLE);
            IntakeOrderIntakeActivity.genericItemExtraField2Text.setText(cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getExtraField2Str());
        }

        if (!cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getExtraField3Str().isEmpty()) {
            IntakeOrderIntakeActivity.genericItemExtraField3Text.setVisibility(View.VISIBLE);
            IntakeOrderIntakeActivity.genericItemExtraField3Text.setText(cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getExtraField3Str());
        }

        if (!cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getExtraField4Str().isEmpty()) {
            IntakeOrderIntakeActivity.genericItemExtraField4Text.setVisibility(View.VISIBLE);
            IntakeOrderIntakeActivity.genericItemExtraField4Text.setText(cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getExtraField4Str());
        }

        if (!cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getExtraField5Str().isEmpty()) {
            IntakeOrderIntakeActivity.genericItemExtraField5Text.setVisibility(View.VISIBLE);
            IntakeOrderIntakeActivity.genericItemExtraField5Text.setText(cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getExtraField5Str());
        }

        if (!cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getExtraField6Str().isEmpty()) {
            IntakeOrderIntakeActivity.genericItemExtraField6Text.setVisibility(View.VISIBLE);
            IntakeOrderIntakeActivity.genericItemExtraField6Text.setText(cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getExtraField6Str());
        }

        if (!cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getExtraField7Str().isEmpty()) {
            IntakeOrderIntakeActivity.genericItemExtraField7Text.setVisibility(View.VISIBLE);
            IntakeOrderIntakeActivity.genericItemExtraField7Text.setText(cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getExtraField7Str());
        }

        if (!cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getExtraField8Str().isEmpty()) {
            IntakeOrderIntakeActivity.genericItemExtraField8Text.setVisibility(View.VISIBLE);
            IntakeOrderIntakeActivity.genericItemExtraField8Text.setText(cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getExtraField8Str());
        }

    }

    private static void mShowBarcodeInfo() {

        if (cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine == null) {
            IntakeOrderIntakeActivity.articleBarcodeText.setText("???");
            return;
        }

        if (cIntakeorderBarcode.currentIntakeOrderBarcode == null) {
            if (cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.barcodesObl().size() == 1) {
                cIntakeorderBarcode.currentIntakeOrderBarcode = cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.barcodesObl().get(0);
            }
        }

        if (cIntakeorderBarcode.currentIntakeOrderBarcode!= null) {
            IntakeOrderIntakeActivity.articleBarcodeText.setText(cIntakeorderBarcode.currentIntakeOrderBarcode.getBarcodeAndQuantityStr());
        } else {
            IntakeOrderIntakeActivity.articleBarcodeText.setText(cAppExtension.context.getString(R.string.mutiple_barcodes_posible));
        }
    }

    private static void mShowArticleImage() {

        IntakeOrderIntakeActivity.articleThumbImageView.setImageDrawable(ContextCompat.getDrawable(cAppExtension.context, R.drawable.ic_no_image_lightgrey_24dp));

        //If pick with picture is false, then hide image view
        if (!cIntakeorder.currentIntakeOrder.isReceiveWithPictureBln()) {
            IntakeOrderIntakeActivity.articleThumbImageView.setVisibility(View.INVISIBLE);
            return;
        }

        //If picture is not in cache (via webservice) then show no image
        if (!cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.pGetArticleImageBln()) {
            cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.could_not_get_article_image), null);
            IntakeOrderIntakeActivity.articleThumbImageView.setImageDrawable(ContextCompat.getDrawable(cAppExtension.context, R.drawable.ic_no_image_lightgrey_24dp));
            return;
        }

        //If picture is in cache but can't be converted, then show no image
        if (cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.articleImage == null || cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.articleImage.imageBitmap() == null) {
            cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.could_not_get_article_image), null);
            IntakeOrderIntakeActivity.articleThumbImageView.setImageDrawable(ContextCompat.getDrawable(cAppExtension.context, R.drawable.ic_no_image_lightgrey_24dp));
            return;
        }

        //Show the image
        IntakeOrderIntakeActivity.articleThumbImageView.setImageBitmap(cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.articleImage.imageBitmap());

        //Open the image
        if ((cIntakeorder.currentIntakeOrder.isReceiveWithAutoOpenBln())) {
            IntakeOrderIntakeActivity.mShowFullArticleFragment();
        }

    }

    private static void mEnablePlusMinusAndBarcodeSelectViews() {

        if (cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine == null) {
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

    private static cResult mHandleArticleScanRst(cBarcodeScan pvBarcodeScan){

        cResult result = new cResult();
        result.resultBln = true;

        cIntakeorderBarcode intakeorderBarcode = cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.pGetBarcode(pvBarcodeScan);
        if (intakeorderBarcode == null) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.activity.getString(R.string.message_unknown_barcode_for_this_line));
            return result;
        }

        IntakeOrderIntakeActivity.mBarcodeSelected(intakeorderBarcode);
        return result;

    }

    private static cResult mHandleBINScanRst(cBarcodeScan pvBarcodescan){

        cResult result = new cResult();
        result.resultBln = true;

        //We scanned a BIN, but we need to scan an article first
        if (! IntakeOrderIntakeActivity.articleScannedBln) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.activity.getString(R.string.message_scan_article_first));
            return result;
        }

        //Strip barcode from regex
        String barcodeWithoutPrefixStr = cRegex.pStripRegexPrefixStr(pvBarcodescan.getBarcodeOriginalStr());

        //Get the current BIN
        IntakeOrderIntakeActivity.currentBin =  cUser.currentUser.currentBranch.pGetBinByCode(barcodeWithoutPrefixStr);
        if ( IntakeOrderIntakeActivity.currentBin  == null) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.activity.getString(R.string.message_bin_unknown));
            return result;
        }

        //Get the current line based on the scanned BIN
        cIntakeorderMATLine.currentIntakeorderMATLine = cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.pGetLineForBinCode(IntakeOrderIntakeActivity.currentBin);

        //We could not find a match, and we are not allowed to add BINS
        if (cIntakeorderMATLine.currentIntakeorderMATLine == null && cIntakeorder.currentIntakeOrder.getReceiveNoExtraBinsBln()) {
            IntakeOrderIntakeActivity.currentBin = null;
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.activity.getString(R.string.message_adding_or_changing_bin_now_allowed));
            return result;
        }

        //Current BIN has been set, so BIN is scanned
        IntakeOrderIntakeActivity.binScannedBln = true;
        return  result;

    }

    private void mNumberClicked() {

        if (cIntakeorderBarcode.currentIntakeOrderBarcode == null) {
            cUserInterface.pDoNope(quantityText, false, false);
            cUserInterface.pShowSnackbarMessage(intakeorderIntakeContainer, getString(R.string.choose_barcode_first), null, false);
            return;
        }

        if (cIntakeorderBarcode.currentIntakeOrderBarcode.getQuantityHandledDbl() > 1) {
            cUserInterface.pDoNope(quantityText, true, true);
            cUserInterface.pShowSnackbarMessage(intakeorderIntakeContainer, getString(R.string.manual_input_only_barcodenumber_bigger1), null, false);
            return;
        }

        this.mShowNumberFragment();
    }

    private void mHandleQuantityChosen(double pvQuantityDbl) {


        if (pvQuantityDbl == 0) {
            IntakeOrderIntakeActivity.mTryToChangeQuantity(false, true,pvQuantityDbl);
        }
        else {
            IntakeOrderIntakeActivity.mTryToChangeQuantity(true, true,pvQuantityDbl);
        }

    }

    private static void mTryToChangeQuantity(Boolean pvIsPositiveBln, Boolean pvAmountFixedBln, double pvAmountDbl) {

      double newQuantityDbl;


        if ( IntakeOrderIntakeActivity.scannedBarcodesObl == null) {
            IntakeOrderIntakeActivity.scannedBarcodesObl = new ArrayList<>();
        }

        if (pvIsPositiveBln) {

            //Determine the new amount
            if (pvAmountFixedBln) {
                newQuantityDbl = pvAmountDbl;


                //Clear the barcode list and refill it
                IntakeOrderIntakeActivity.scannedBarcodesObl.clear();
                int countInt = 0;
                do{
                    countInt += 1;
                    //Add a barcode to the scanned barcode list, so you can use it later when line is determined
                    IntakeOrderIntakeActivity.scannedBarcodesObl.add(cIntakeorderBarcode.currentIntakeOrderBarcode);
                }
                while(countInt < newQuantityDbl);

                //Update activity and Check if this line is done
                IntakeOrderIntakeActivity.quantityScannedDbl = newQuantityDbl;
                IntakeOrderIntakeActivity.quantityText.setText(pDoubleToStringStr(IntakeOrderIntakeActivity.quantityScannedDbl));
                IntakeOrderIntakeActivity.mCheckLineDone();
                return;


            } else {
                newQuantityDbl = IntakeOrderIntakeActivity.quantityScannedDbl + pvAmountDbl;
            }

            //Check if we would exceed amount, then show message if needed
            if (newQuantityDbl > cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getQuantityDbl()) {

                if (cIntakeorder.currentIntakeOrder.getReceiveNoExtraPiecesBln()) {
                    IntakeOrderIntakeActivity.mShowExtraPiecesNotAllowed();

                    if (IntakeOrderIntakeActivity.quantityScannedDbl> 0) {
                        IntakeOrderIntakeActivity.articleScannedBln = true;
                        return;
                    }

                    IntakeOrderIntakeActivity.articleScannedBln = false;
                    return;
                }
            }

            //Set the new quantityDbl and show in Activity
            IntakeOrderIntakeActivity.quantityScannedDbl = newQuantityDbl;
            IntakeOrderIntakeActivity.quantityText.setText(pDoubleToStringStr(IntakeOrderIntakeActivity.quantityScannedDbl));

            //Add a barcode to the scanned barcode list, so you can use it later when line is determined
            IntakeOrderIntakeActivity.scannedBarcodesObl.add(cIntakeorderBarcode.currentIntakeOrderBarcode);

            //Check if this line is done
            IntakeOrderIntakeActivity.mCheckLineDone();
            return;
        }

        //negative

        //Check if value already is zero
        if ( IntakeOrderIntakeActivity.quantityScannedDbl < 1 ) {

            //If we have a decimal, correct it to zero
            if (IntakeOrderIntakeActivity.quantityScannedDbl > 0 ) {
                pvAmountDbl = 0;
                pvAmountFixedBln = true;

            } else {
                cUserInterface.pDoNope(IntakeOrderIntakeActivity.quantityText, true, true);
                return;
            }
        }

        if (pvAmountDbl < 0) {
            cUserInterface.pDoNope(IntakeOrderIntakeActivity.quantityText, true, true);
            return;
        }

        //Determine the new amount
        if (pvAmountFixedBln) {
            newQuantityDbl = pvAmountDbl;

            //Clear the barcode list and refill it
            IntakeOrderIntakeActivity.scannedBarcodesObl.clear();
            int countInt = 0;
            do{
                countInt += 1;
                //Add a barcode to the scanned barcode list, so you can use it later when line is determined
                IntakeOrderIntakeActivity.scannedBarcodesObl.add(cIntakeorderBarcode.currentIntakeOrderBarcode);
                            }while(countInt < newQuantityDbl);

            //Set the new quantityDbl and show in Activity
            IntakeOrderIntakeActivity.quantityScannedDbl = newQuantityDbl;
            IntakeOrderIntakeActivity.quantityText.setText(pDoubleToStringStr(IntakeOrderIntakeActivity.quantityScannedDbl));
            return;


        }else {
            //Remove the last barcode in the list
            IntakeOrderIntakeActivity.scannedBarcodesObl.remove( IntakeOrderIntakeActivity.scannedBarcodesObl.size()-1);
            newQuantityDbl= IntakeOrderIntakeActivity.quantityScannedDbl - pvAmountDbl;
        }

        if (newQuantityDbl <= 0) {
            IntakeOrderIntakeActivity.quantityScannedDbl = (double) 0;
        }else {
            //Set the new quantityDbl and show in Activity
            IntakeOrderIntakeActivity.quantityScannedDbl = newQuantityDbl;
        }

        IntakeOrderIntakeActivity.quantityText.setText(pDoubleToStringStr(IntakeOrderIntakeActivity.quantityScannedDbl));
        IntakeOrderIntakeActivity.imageButtonDone.setImageResource(R.drawable.ic_check_black_24dp);
    }

    private static void mBarcodeSelected(cIntakeorderBarcode pvBarcode) {

        cUserInterface.pCheckAndCloseOpenDialogs();

        cIntakeorderBarcode.currentIntakeOrderBarcode = pvBarcode;
        IntakeOrderIntakeActivity.articleScannedBln = true;
        IntakeOrderIntakeActivity.mShowBarcodeInfo();

        IntakeOrderIntakeActivity.mTryToChangeQuantity(true, false, cIntakeorderBarcode.currentIntakeOrderBarcode.getQuantityPerUnitOfMeasureDbl());
    }

    // Lines, Barcodes

    private static void mCheckLineDone() {

        //Start with complete
        IntakeOrderIntakeActivity.imageButtonDone.setVisibility(View.VISIBLE);

        //If article or BIN hasn't been scanned then we are not done
        if (!IntakeOrderIntakeActivity.articleScannedBln || !IntakeOrderIntakeActivity.binScannedBln) {
            IntakeOrderIntakeActivity.imageButtonDone.setVisibility(View.INVISIBLE);
            return;
        }

        //Check if quantityDbl is sufficient
        if (!cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.pQuantityReachedBln(IntakeOrderIntakeActivity.scannedBarcodesObl)) {
            IntakeOrderIntakeActivity.imageButtonDone.setVisibility(View.VISIBLE);
            IntakeOrderIntakeActivity.imageButtonDone.setImageResource(R.drawable.ic_check_black_24dp);
            return;
        }


        //We are complete
        IntakeOrderIntakeActivity.imageButtonDone.setVisibility(View.VISIBLE);
        IntakeOrderIntakeActivity.imageButtonDone.setImageResource(R.drawable.ic_doublecheck_black_24dp);

        //If auto accept is false, we are done here
        if (!cIntakeorder.currentIntakeOrder.getReceiveStoreAutoAcceptAtRequestedBln()) {
            return;
        }

        //All is done
        IntakeOrderIntakeActivity.mStoreDone();

    }

    private static void mSendLine() {

        //If internet is not connected
        if (!cConnection.isInternetConnectedBln()) {
            //could not send line, let user know but answer succes so user can go to next line
            cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.couldnt_send_line_no_connection), null);
            return;
        }

       if(!cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.pItemVariantHandledBln(IntakeOrderIntakeActivity.currentBin.getBinCodeStr(),
            IntakeOrderIntakeActivity.scannedBarcodesObl)) {
            cUserInterface.pDoExplodingScreen(cAppExtension.context.getString(R.string.couldnt_send_line), "",true,true);
            return;
        }

        IntakeOrderIntakeActivity.mResetCurrents();
        IntakeOrderIntakeActivity.mGoBackToLinesActivity();

    }

     private static void mStoreDone() {
        IntakeOrderIntakeActivity.mSendLine();
     }

     //ScanActions
     private static void mFillRecycler(List<cIntakeorderMATLine> pvDataObl) {
         cIntakeorderMATLine.getIntakeorderMATLineAdapter().pFillData(pvDataObl);
         IntakeOrderIntakeActivity.recyclerScanActions.setHasFixedSize(false);
         IntakeOrderIntakeActivity.recyclerScanActions.setAdapter( cIntakeorderMATLine.getIntakeorderMATLineAdapter());
         IntakeOrderIntakeActivity.recyclerScanActions.setLayoutManager(new LinearLayoutManager(cAppExtension.context));
     }

    //Listeners
    private void mSetDoneListener() {
        IntakeOrderIntakeActivity.imageButtonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               IntakeOrderIntakeActivity.pAcceptStore();
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void mSetPlusListener() {

        IntakeOrderIntakeActivity.imageButtonPlus.setOnTouchListener(new View.OnTouchListener() {

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
                IntakeOrderIntakeActivity.pHandleScan(cBarcodeScan.pFakeScan(cIntakeorderBarcode.currentIntakeOrderBarcode.getBarcodeStr()));
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void mSetMinusListener() {

        IntakeOrderIntakeActivity.imageButtonMinus.setOnTouchListener(new View.OnTouchListener() {
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

        IntakeOrderIntakeActivity.imageButtonMinus.setOnClickListener(new View.OnClickListener() {
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
        IntakeOrderIntakeActivity.quantityText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNumberClicked();
            }
        });
        IntakeOrderIntakeActivity.quantityRequiredText.setOnClickListener(new View.OnClickListener() {
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
        IntakeOrderIntakeActivity.quantityText.setText(quantityRequiredText.getText());
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

        cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine = null;
        cIntakeorderMATLine.currentIntakeorderMATLine = null;
        IntakeOrderIntakeActivity.currentBin = null;
        IntakeOrderIntakeActivity.articleScannedBln = false;
        IntakeOrderIntakeActivity.binScannedBln = false;
        IntakeOrderIntakeActivity.scannedBarcodesObl = null;
        IntakeOrderIntakeActivity.quantityScannedDbl = 0.0;
        IntakeOrderIntakeActivity.scannedBarcodesObl = null;

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
            IntakeOrderIntakeActivity.articleDescriptionText.setText("???");
            IntakeOrderIntakeActivity.articleDescription2Text.setText("???");
            IntakeOrderIntakeActivity.articleItemText.setText("???");
            IntakeOrderIntakeActivity.articleVendorItemText.setText("???");
            return;
        }
            IntakeOrderIntakeActivity.articleDescriptionText.setText(cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getDescriptionStr());
            IntakeOrderIntakeActivity.articleDescription2Text.setText(cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getDescription2Str());
            IntakeOrderIntakeActivity.articleItemText.setText(cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getItemNoAndVariantCodeStr());
            IntakeOrderIntakeActivity.articleVendorItemText.setText(cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getVendorItemNoAndDescriptionStr());
    }

    private static void mSetQuantityInfo(){

        if (cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine == null) {
            IntakeOrderIntakeActivity.quantityText.setText("0");
            IntakeOrderIntakeActivity.quantityRequiredText.setText("???");
                return;
        }

        IntakeOrderIntakeActivity.quantityText.setText("0");
        IntakeOrderIntakeActivity.quantityRequiredText.setText(pDoubleToStringStr(cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.pGetQuantityToHandleDbl()));

    }


    private static void mSetBinInfo(){
        IntakeOrderIntakeActivity.binContainer.setVisibility(View.GONE);
    }

    private static void mSetSourceNoInfo(){
        IntakeOrderIntakeActivity.sourcenoText.setText(cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getSourceNoStr());
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
        IntakeOrderIntakeActivity.minusHandler.postDelayed(pvRunnable, pvMilliSecsLng);
        IntakeOrderIntakeActivity.counterMinusHelperInt += 1;
    }

    private void mDoDelayedPlus(Runnable pvRunnable, long pvMilliSecsLng) {
        IntakeOrderIntakeActivity.plusHandler.postDelayed(pvRunnable, pvMilliSecsLng);
        IntakeOrderIntakeActivity.counterPlusHelperInt += 1;
    }

    private void mRemoveAdapterFromFragment(){

        //remove the item from recyclerview
        boolean resultBln = cIntakeorderMATLine.currentIntakeorderMATLine.pResetBln();
        if (! resultBln) {
            cUserInterface.pDoExplodingScreen(cAppExtension.activity.getString(R.string.message_reset_line_via_webservice_failed),"",true,true);
            return;
        }

        //Renew data, so only current lines are shown
        IntakeOrderIntakeActivity.mFieldsInitializeStatic();

    }



    //End Region Number Broadcaster

    //End Regin Private Methods

}

