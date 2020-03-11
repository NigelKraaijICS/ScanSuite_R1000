package nl.icsvertex.scansuite.Activities.Receive;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
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
import java.util.Collections;
import java.util.List;

import ICS.Interfaces.iICSDefaultActivity;
import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cConnection;
import ICS.Utils.cNumberTextWatcher;
import ICS.Utils.cProductFlavor;
import ICS.Utils.cResult;
import ICS.Utils.cText;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Basics.BarcodeLayouts.cBarcodeLayout;
import SSU_WHS.Basics.Settings.cSetting;
import SSU_WHS.General.cPublicDefinitions;
import SSU_WHS.Intake.IntakeorderBarcodes.cIntakeorderBarcode;
import SSU_WHS.Intake.Intakeorders.cIntakeorder;
import SSU_WHS.Receive.ReceiveLines.cReceiveorderLine;
import SSU_WHS.Receive.ReceiveLines.cReceiveorderLineAdapter;
import SSU_WHS.Receive.ReceiveLines.cReceiveorderLineRecyclerItemTouchHelper;
import SSU_WHS.Receive.ReceiveSummaryLine.cReceiveorderSummaryLine;
import nl.icsvertex.scansuite.BuildConfig;
import nl.icsvertex.scansuite.Fragments.Dialogs.AcceptRejectFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.ArticleFullViewFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.BarcodeFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.NumberpickerFragment;
import nl.icsvertex.scansuite.R;

import static ICS.Utils.cText.pDoubleToStringStr;

public class ReceiveOrderReceiveActivity extends AppCompatActivity implements iICSDefaultActivity, cReceiveorderLineRecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    //Region Private Properties
    static final String NUMBERFRAGMENT_TAG = "NUMBERFRAGMENT_TAG";
    static final String BARCODEFRAGMENT_TAG = "BARCODEFRAGMENT_TAG";
    static final String ACCEPTREJECTFRAGMENT_TAG = "ACCEPTREJECTFRAGMENT_TAG";

    public static boolean amountExceededDialogShowedBln = false;
    public static int positionSwiped;

    private static int counterMinusHelperInt;
    private static int counterPlusHelperInt;
    private static Handler minusHandler;
    private static Handler plusHandler;
    private static AppCompatImageButton imageButtonMinus;
    private static AppCompatImageButton imageButtonPlus;
    private static AppCompatImageButton imageButtonDone;

    private static ConstraintLayout receiveOrderReceiveContainer;

    private static ImageView toolbarImage;
    private static TextView toolbarTitle;
    private static TextView toolbarSubtext;

    private static TextView articleDescriptionText;
    private static TextView articleDescription2Text;
    private static TextView articleItemText;
    private static TextView articleBarcodeText;
    private static TextView articleUnitOfMeasureText;
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

    private static EditText quantityText;
    private static TextView quantityRequiredText;

    private static CardView sourceNoContainer;
    private static TextView sourcenoText;

    private static TextView binCodeText;

    private static Double quantityScannedDbl = 0.0;
    private static List<cIntakeorderBarcode> scannedBarcodesObl;
    private static RecyclerView recyclerScanActions;

    //End Region Private Properties

    //Region Default Methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiveorder_receive);
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
            if (ReceiveOrderReceiveActivity.quantityScannedDbl == 0 ) {
                ReceiveOrderReceiveActivity.mResetCurrents();
                ReceiveOrderReceiveActivity.mGoBackToLinesActivity();
                return  true;
            }

            this.mShowAcceptFragment();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        if (ReceiveOrderReceiveActivity.quantityScannedDbl == 0 ) {
            ReceiveOrderReceiveActivity.mResetCurrents();
            ReceiveOrderReceiveActivity.mGoBackToLinesActivity();
            return;
        }

        this.mShowAcceptFragment();
    }


    @Override
    public void onSwiped(RecyclerView.ViewHolder pvViewHolder, int direction, int pvPositionInt) {

        if (!(pvViewHolder instanceof  cReceiveorderLineAdapter.ReceiveorderLineViewHolder)) {
            return;
        }

        ReceiveOrderReceiveActivity.positionSwiped = pvPositionInt;
        cReceiveorderLine.currentReceiveorderLine = cReceiveorderSummaryLine.currentReceiveorderSummaryLine.receivedLinesReversedObl().get(pvPositionInt);

        //do we need an adult for this?
        if (!cSetting.RECEIVE_RESET_PASSWORD().isEmpty()) {
            cUserInterface.pShowpasswordDialog(getString(R.string.supervisor_password_header), getString(R.string.supervisor_password_text), false);
            return;
        }

        //Remove the enviroment
        ReceiveOrderReceiveActivity.mRemoveAdapterFromFragment();

    }

    //End Region Default Methods

    //Region iICSDefaultActivity Methods

    @Override
    public void mActivityInitialize() {

        this.mSetAppExtensions();

        this.mFindViews();

        this.mSetToolbar(getResources().getString(R.string.screentitle_receive));

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

        ReceiveOrderReceiveActivity.receiveOrderReceiveContainer = findViewById(R.id.receiveOrderReceiveContainer);
        ReceiveOrderReceiveActivity.toolbarImage = findViewById(R.id.toolbarImage);
        ReceiveOrderReceiveActivity.toolbarTitle = findViewById(R.id.toolbarTitle);
        ReceiveOrderReceiveActivity.toolbarSubtext = findViewById(R.id.toolbarSubtext);

        ReceiveOrderReceiveActivity.articleDescriptionText = findViewById(R.id.articleDescriptionText);
        ReceiveOrderReceiveActivity.articleDescription2Text = findViewById(R.id.articleDescription2Text);
        ReceiveOrderReceiveActivity.articleItemText = findViewById(R.id.articleItemText);
        ReceiveOrderReceiveActivity.articleBarcodeText = findViewById(R.id.articleBarcodeText);
        ReceiveOrderReceiveActivity.articleVendorItemText = findViewById(R.id.articleVendorItemText);
        ReceiveOrderReceiveActivity.articleUnitOfMeasureText = findViewById(R.id.articleUnitOfMeasureText);

        ReceiveOrderReceiveActivity.sourceNoContainer = findViewById(R.id.sourceNoContainer);
        ReceiveOrderReceiveActivity.sourcenoText = findViewById(R.id.sourcenoText);
        ReceiveOrderReceiveActivity.binCodeText = findViewById(R.id.binText);

        ReceiveOrderReceiveActivity.genericItemExtraField1Text = findViewById(R.id.genericItemExtraField1Text);
        ReceiveOrderReceiveActivity.genericItemExtraField2Text = findViewById(R.id.genericItemExtraField2Text);
        ReceiveOrderReceiveActivity.genericItemExtraField3Text = findViewById(R.id.genericItemExtraField3Text);
        ReceiveOrderReceiveActivity.genericItemExtraField4Text = findViewById(R.id.genericItemExtraField4Text);
        ReceiveOrderReceiveActivity.genericItemExtraField5Text = findViewById(R.id.genericItemExtraField5Text);
        ReceiveOrderReceiveActivity.genericItemExtraField6Text = findViewById(R.id.genericItemExtraField6Text);
        ReceiveOrderReceiveActivity.genericItemExtraField7Text = findViewById(R.id.genericItemExtraField7Text);
        ReceiveOrderReceiveActivity.genericItemExtraField8Text = findViewById(R.id.genericItemExtraField8Text);

        ReceiveOrderReceiveActivity.quantityText = findViewById(R.id.quantityText);
        ReceiveOrderReceiveActivity.quantityRequiredText = findViewById(R.id.quantityRequiredText);

        ReceiveOrderReceiveActivity.articleThumbImageView = findViewById(R.id.articleThumbImageView);
        ReceiveOrderReceiveActivity.imageButtonBarcode = findViewById(R.id.imageButtonBarcode);
        ReceiveOrderReceiveActivity.imageButtonMinus = findViewById(R.id.imageButtonMinus);
        ReceiveOrderReceiveActivity.imageButtonPlus = findViewById(R.id.imageButtonPlus);
        ReceiveOrderReceiveActivity.imageButtonDone = findViewById(R.id.imageButtonDone);

        ReceiveOrderReceiveActivity.recyclerScanActions = findViewById(R.id.recyclerScanActions);
    }

    @Override
    public void mSetToolbar(String pvScreenTitle) {
        ReceiveOrderReceiveActivity.toolbarImage.setImageResource(R.drawable.ic_menu_intake_eo);
        ReceiveOrderReceiveActivity.toolbarTitle.setText(pvScreenTitle);
        ReceiveOrderReceiveActivity.toolbarTitle.setSelected(true);
        ReceiveOrderReceiveActivity.toolbarSubtext.setSelected(true);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void mFieldsInitialize() {
        ReceiveOrderReceiveActivity.mFieldsInitializeStatic();
    }

    @Override
    public void mInitScreen() {
        cBarcodeScan.pRegisterBarcodeReceiver();
        //Raise quantity with scanned barcodeStr, if we started this activity with a scan
        if (cIntakeorder.currentIntakeOrder.intakeorderBarcodeScanned != null) {
            ReceiveOrderReceiveActivity.pHandleScan(cBarcodeScan.pFakeScan(cIntakeorder.currentIntakeOrder.intakeorderBarcodeScanned.getBarcodeStr()));
        }
    }

    @Override
    public void mSetListeners() {

        this.mSetArticleImageListener();
        this.mSetImageButtonBarcodeListener();

        if (cSetting.RECEIVE_AMOUNT_MANUAL_MA()) {
            this.mSetNumberListener();
            this.mSetPlusListener();
            this.mSetMinusListener();
            this.mSetEditorActionListener();
        }

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

            hulpRst = ReceiveOrderReceiveActivity.mHandleArticleScanRst(pvBarcodeScan);
            if (! hulpRst.resultBln) {
                cUserInterface.pDoExplodingScreen(hulpRst.messagesStr(),"",true,true);
                return;
            }
            return;
        }

        cUserInterface.pShowSnackbarMessage(receiveOrderReceiveContainer,cAppExtension.activity.getString(R.string.message_unknown_barcode),null,true);

    }

    public static void pAcceptReceive() {

        if (ReceiveOrderReceiveActivity.quantityScannedDbl == 0 ) {
            ReceiveOrderReceiveActivity.mResetCurrents();
            ReceiveOrderReceiveActivity.mGoBackToLinesActivity();
            return;
        }

        if (!ReceiveOrderReceiveActivity.amountExceededDialogShowedBln) {
            //Check if we exceed the allowed quantity, so we can inform the user
            if (ReceiveOrderReceiveActivity.quantityScannedDbl > cReceiveorderSummaryLine.currentReceiveorderSummaryLine.getAllowedQuantityDbl()
                && cSetting.RECEIVE_EXTRA_PIECES_PERCENTAGE() > 0
                && cReceiveorderSummaryLine.currentReceiveorderSummaryLine.getQuantityDbl() > 0)  {
                ReceiveOrderReceiveActivity.mShowExtraPiecesPercentageExceededFragment(ReceiveOrderReceiveActivity.quantityScannedDbl);
                return;
            }
        }


        //We are done
         ReceiveOrderReceiveActivity.mReceiveDone();

    }

    public  static  void pRegisterBarcodeReceiver(){
        cBarcodeScan.pRegisterBarcodeReceiver();
    }

    public static void pCancelReceive() {
        ReceiveOrderReceiveActivity.mResetCurrents();
        ReceiveOrderReceiveActivity.mGoBackToLinesActivity();
    }

    public static void pPasswordSuccess() {
        cBarcodeScan.pRegisterBarcodeReceiver();

        //Remove the line
        ReceiveOrderReceiveActivity.mRemoveAdapterFromFragment();
    }

    public static void pPasswordCancelled() {
        cBarcodeScan.pRegisterBarcodeReceiver();
        cReceiveorderSummaryLine.getSummaryLinesAdapter().notifyItemChanged( ReceiveOrderReceiveActivity.positionSwiped );
    }

    //End Region Public Methods

    //Region Private Methods

    //Views
    private static void mFieldsInitializeStatic(){

        ReceiveOrderReceiveActivity.imageButtonDone.setImageResource(R.drawable.ic_check_black_24dp);

        ReceiveOrderReceiveActivity.counterPlusHelperInt = 0;
        ReceiveOrderReceiveActivity.counterMinusHelperInt = 0;
        ReceiveOrderReceiveActivity.toolbarSubtext.setText(cIntakeorder.currentIntakeOrder.getOrderNumberStr());

        ReceiveOrderReceiveActivity.mSetArticleInfo();
        ReceiveOrderReceiveActivity.mSetSourceNoInfo();
        ReceiveOrderReceiveActivity.mSetBinInfo();
        ReceiveOrderReceiveActivity.mSetQuantityInfo();

        ReceiveOrderReceiveActivity.mEnablePlusMinusAndBarcodeSelectViews();
        ReceiveOrderReceiveActivity.mShowArticleImage();
        ReceiveOrderReceiveActivity.mShowOrHideGenericExtraFields();
        ReceiveOrderReceiveActivity.mShowBarcodeInfo();
        ReceiveOrderReceiveActivity.mShowLines();

    }

    private static void mShowLines() {

        if (cReceiveorderSummaryLine.currentReceiveorderSummaryLine.receiveLinesObl == null  || cReceiveorderSummaryLine.currentReceiveorderSummaryLine.receiveLinesObl.size() == 0) {
            ReceiveOrderReceiveActivity.mFillRecycler(new ArrayList<cReceiveorderLine>());
            return;
        }

        ReceiveOrderReceiveActivity.mFillRecycler(cReceiveorderSummaryLine.currentReceiveorderSummaryLine.receivedLinesReversedObl());
    }

    private void mSetArticleImageListener() {
        ReceiveOrderReceiveActivity.articleThumbImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mShowFullArticleFragment();
            }
        });
    }

    private void mSetImageButtonBarcodeListener() {
        ReceiveOrderReceiveActivity.imageButtonBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View pvView) {

                if (cReceiveorderSummaryLine.currentReceiveorderSummaryLine.barcodesObl() == null || cReceiveorderSummaryLine.currentReceiveorderSummaryLine.barcodesObl().size() == 0) {
                    return;
                }

                mEnablePlusMinusAndBarcodeSelectViews();

                //If we only have one barcodeStr, then automatticaly select that barcodeStr
                if (cReceiveorderSummaryLine.currentReceiveorderSummaryLine.barcodesObl().size() == 1) {
                    cIntakeorder.currentIntakeOrder.intakeorderBarcodeScanned = cReceiveorderSummaryLine.currentReceiveorderSummaryLine.barcodesObl().get(0);
                    ReceiveOrderReceiveActivity.pHandleScan(cBarcodeScan.pFakeScan( cIntakeorder.currentIntakeOrder.intakeorderBarcodeScanned.getBarcodeStr()));
                    return;
                }

                mShowBarcodeSelectFragment();

            }
        });
    }

    private void mSetRecyclerTouchListener(){


        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new cReceiveorderLineRecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(ReceiveOrderReceiveActivity.recyclerScanActions);
    }

    private static void mShowOrHideGenericExtraFields() {


        if (cReceiveorderSummaryLine.currentReceiveorderSummaryLine == null) {
            ReceiveOrderReceiveActivity.genericItemExtraField1Text.setVisibility(View.INVISIBLE);
            ReceiveOrderReceiveActivity.genericItemExtraField2Text.setVisibility(View.INVISIBLE);
            ReceiveOrderReceiveActivity.genericItemExtraField3Text.setVisibility(View.INVISIBLE);
            ReceiveOrderReceiveActivity.genericItemExtraField4Text.setVisibility(View.INVISIBLE);
            ReceiveOrderReceiveActivity.genericItemExtraField5Text.setVisibility(View.INVISIBLE);
            ReceiveOrderReceiveActivity.genericItemExtraField6Text.setVisibility(View.INVISIBLE);
            ReceiveOrderReceiveActivity.genericItemExtraField7Text.setVisibility(View.INVISIBLE);
            ReceiveOrderReceiveActivity.genericItemExtraField8Text.setVisibility(View.INVISIBLE);
            return;
        }
        else{

            if (!cReceiveorderSummaryLine.currentReceiveorderSummaryLine.getExtraField1Str().isEmpty()) {
                ReceiveOrderReceiveActivity.genericItemExtraField1Text.setVisibility(View.VISIBLE);
                ReceiveOrderReceiveActivity.genericItemExtraField1Text.setText(cReceiveorderSummaryLine.currentReceiveorderSummaryLine.getExtraField1Str());
            }

            if (!cReceiveorderSummaryLine.currentReceiveorderSummaryLine.getExtraField2Str().isEmpty()) {
                ReceiveOrderReceiveActivity.genericItemExtraField2Text.setVisibility(View.VISIBLE);
                ReceiveOrderReceiveActivity.genericItemExtraField2Text.setText(cReceiveorderSummaryLine.currentReceiveorderSummaryLine.getExtraField2Str());
            }

            if (!cReceiveorderSummaryLine.currentReceiveorderSummaryLine.getExtraField3Str().isEmpty()) {
                ReceiveOrderReceiveActivity.genericItemExtraField3Text.setVisibility(View.VISIBLE);
                ReceiveOrderReceiveActivity.genericItemExtraField3Text.setText(cReceiveorderSummaryLine.currentReceiveorderSummaryLine.getExtraField3Str());
            }

            if (!cReceiveorderSummaryLine.currentReceiveorderSummaryLine.getExtraField4Str().isEmpty()) {
                ReceiveOrderReceiveActivity.genericItemExtraField4Text.setVisibility(View.VISIBLE);
                ReceiveOrderReceiveActivity.genericItemExtraField4Text.setText(cReceiveorderSummaryLine.currentReceiveorderSummaryLine.getExtraField4Str());
            }

            if (!cReceiveorderSummaryLine.currentReceiveorderSummaryLine.getExtraField5Str().isEmpty()) {
                ReceiveOrderReceiveActivity.genericItemExtraField5Text.setVisibility(View.VISIBLE);
                ReceiveOrderReceiveActivity.genericItemExtraField5Text.setText(cReceiveorderSummaryLine.currentReceiveorderSummaryLine.getExtraField5Str());
            }

            if (!cReceiveorderSummaryLine.currentReceiveorderSummaryLine.getExtraField6Str().isEmpty()) {
                ReceiveOrderReceiveActivity.genericItemExtraField6Text.setVisibility(View.VISIBLE);
                ReceiveOrderReceiveActivity.genericItemExtraField6Text.setText(cReceiveorderSummaryLine.currentReceiveorderSummaryLine.getExtraField6Str());
            }

            if (!cReceiveorderSummaryLine.currentReceiveorderSummaryLine.getExtraField7Str().isEmpty()) {
                ReceiveOrderReceiveActivity.genericItemExtraField7Text.setVisibility(View.VISIBLE);
                ReceiveOrderReceiveActivity.genericItemExtraField7Text.setText(cReceiveorderSummaryLine.currentReceiveorderSummaryLine.getExtraField7Str());
            }

            if (!cReceiveorderSummaryLine.currentReceiveorderSummaryLine.getExtraField8Str().isEmpty()) {
                ReceiveOrderReceiveActivity.genericItemExtraField8Text.setVisibility(View.VISIBLE);
                ReceiveOrderReceiveActivity. genericItemExtraField8Text.setText(cReceiveorderSummaryLine.currentReceiveorderSummaryLine.getExtraField8Str());
            }


        }

    }

    private static void mShowBarcodeInfo() {

        if (cReceiveorderSummaryLine.currentReceiveorderSummaryLine== null) {
            ReceiveOrderReceiveActivity.articleBarcodeText.setText("???");
            return;
        }

        if (cIntakeorderBarcode.currentIntakeOrderBarcode == null) {
            if (cReceiveorderSummaryLine.currentReceiveorderSummaryLine.barcodesObl().size() == 1) {
                cIntakeorderBarcode.currentIntakeOrderBarcode = cReceiveorderSummaryLine.currentReceiveorderSummaryLine.barcodesObl().get(0);
            }
        }

        if (cIntakeorderBarcode.currentIntakeOrderBarcode!= null) {
            ReceiveOrderReceiveActivity.articleBarcodeText.setText(cIntakeorderBarcode.currentIntakeOrderBarcode.getBarcodeAndQuantityStr());
            ReceiveOrderReceiveActivity.articleUnitOfMeasureText.setText(cIntakeorderBarcode.currentIntakeOrderBarcode.getUnitOfMeasureStr());
        } else {
            ReceiveOrderReceiveActivity.articleBarcodeText.setText(cAppExtension.context.getString(R.string.mutiple_barcodes_posible));
            ReceiveOrderReceiveActivity.articleUnitOfMeasureText.setText("");
        }
    }

    private static void mShowArticleImage() {

        ReceiveOrderReceiveActivity.articleThumbImageView.setImageDrawable(ContextCompat.getDrawable(cAppExtension.context, R.drawable.ic_no_image_lightgrey_24dp));

        //If pick with picture is false, then hide image view
        if (!cIntakeorder.currentIntakeOrder.isReceiveWithPictureBln()) {
            ReceiveOrderReceiveActivity.articleThumbImageView.setVisibility(View.INVISIBLE);
            return;
        }

        //If picture is not in cache (via webservice) then show no image
        if (!cReceiveorderSummaryLine.currentReceiveorderSummaryLine.pGetArticleImageBln()) {
            cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.could_not_get_article_image), null);
            ReceiveOrderReceiveActivity.articleThumbImageView.setImageDrawable(ContextCompat.getDrawable(cAppExtension.context, R.drawable.ic_no_image_lightgrey_24dp));
            return;
        }

        //If picture is in cache but can't be converted, then show no image
        if (cReceiveorderSummaryLine.currentReceiveorderSummaryLine.articleImage == null || cReceiveorderSummaryLine.currentReceiveorderSummaryLine.articleImage.imageBitmap() == null) {
            cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.could_not_get_article_image), null);
            ReceiveOrderReceiveActivity.articleThumbImageView.setImageDrawable(ContextCompat.getDrawable(cAppExtension.context, R.drawable.ic_no_image_lightgrey_24dp));
            return;
        }

        //Show the image
        ReceiveOrderReceiveActivity.articleThumbImageView.setImageBitmap(cReceiveorderSummaryLine.currentReceiveorderSummaryLine.articleImage.imageBitmap());

        //Open the image
        if ((cIntakeorder.currentIntakeOrder.isReceiveWithAutoOpenBln())) {
            ReceiveOrderReceiveActivity.mShowFullArticleFragment();
        }

    }

    private static void mEnablePlusMinusAndBarcodeSelectViews() {

        if (cReceiveorderSummaryLine.currentReceiveorderSummaryLine== null) {
            ReceiveOrderReceiveActivity.imageButtonMinus.setVisibility(View.INVISIBLE);
            ReceiveOrderReceiveActivity.imageButtonPlus.setVisibility(View.INVISIBLE);
            ReceiveOrderReceiveActivity.imageButtonBarcode.setVisibility(View.INVISIBLE);
            ReceiveOrderReceiveActivity.imageButtonDone.setVisibility(View.INVISIBLE);
            return;
        }

        ReceiveOrderReceiveActivity.imageButtonDone.setVisibility(View.VISIBLE);

        if (!cSetting.RECEIVE_AMOUNT_MANUAL_MA()) {
            ReceiveOrderReceiveActivity.imageButtonMinus.setVisibility(View.INVISIBLE);
            ReceiveOrderReceiveActivity.imageButtonPlus.setVisibility(View.INVISIBLE);
            ReceiveOrderReceiveActivity.imageButtonBarcode.setVisibility(View.INVISIBLE);
        } else {
            ReceiveOrderReceiveActivity.imageButtonMinus.setVisibility(View.VISIBLE);
            ReceiveOrderReceiveActivity.imageButtonPlus.setVisibility(View.VISIBLE);

            if (BuildConfig.FLAVOR.equalsIgnoreCase(cProductFlavor.FlavorEnu.BMN.toString())) {
                ReceiveOrderReceiveActivity.imageButtonBarcode.setVisibility(View.INVISIBLE);
            }
            else{
                ReceiveOrderReceiveActivity.imageButtonBarcode.setVisibility(View.VISIBLE);
            }
        }
    }

    //Scans and manual input

    private static cResult mHandleArticleScanRst(final cBarcodeScan pvBarcodeScan){

        cResult result = new cResult();
        result.resultBln = true;


        //We opened the line withoud scanning, so handle the scan in previous activity
        if (cIntakeorder.currentIntakeOrder.intakeorderBarcodeScanned == null){
            mResetCurrents();
            mGoBackToLinesActivity();
            ReceiveLinesActivity.pHandleScan(pvBarcodeScan,false);
            return result;
        }

        if (cIntakeorder.currentIntakeOrder.isGenerated()) {

            //Check if we scanned the same barcode
            if (pvBarcodeScan.getBarcodeOriginalStr().equalsIgnoreCase(cIntakeorder.currentIntakeOrder.intakeorderBarcodeScanned.getBarcodeStr())) {
                ReceiveOrderReceiveActivity.mBarcodeSelected(cIntakeorder.currentIntakeOrder.intakeorderBarcodeScanned );
                return result;
            }

            //We scanned a new barcode, finish barcode and pass scan on
            if (ReceiveOrderReceiveActivity.mSendScansBln()) {
                ReceiveOrderReceiveActivity.mResetCurrents();
                mGoBackToLinesActivity();
                ReceiveLinesActivity.pHandleScan(pvBarcodeScan,false);
                return result;
            }

        }

        //Check if scanned barcode, belong to this item variant, if so raise quantity
        cIntakeorder.currentIntakeOrder.intakeorderBarcodeScanned  = cReceiveorderSummaryLine.currentReceiveorderSummaryLine.pGetBarcode(pvBarcodeScan);
        if (cIntakeorder.currentIntakeOrder.intakeorderBarcodeScanned  != null) {
            ReceiveOrderReceiveActivity.mBarcodeSelected(cIntakeorder.currentIntakeOrder.intakeorderBarcodeScanned );
            return result;
        }

        //We scanned a barcode that doesn't belong to this item variant, so let the previous activity handle the scan
        if (ReceiveOrderReceiveActivity.mSendScansBln()) {
            ReceiveOrderReceiveActivity.mResetCurrents();
            mGoBackToLinesActivity();
            ReceiveLinesActivity.pHandleScan(pvBarcodeScan,false);
            return result;
        }

        return  result;

    }

    private void mNumberClicked() {

        if (cIntakeorderBarcode.currentIntakeOrderBarcode == null) {
            cUserInterface.pDoNope(quantityText, false, false);
            cUserInterface.pShowSnackbarMessage(receiveOrderReceiveContainer, getString(R.string.choose_barcode_first), null, false);
            return;
        }

        if (cIntakeorderBarcode.currentIntakeOrderBarcode.getQuantityHandledDbl() > 1) {
            cUserInterface.pDoNope(quantityText, true, true);
            cUserInterface.pShowSnackbarMessage(receiveOrderReceiveContainer, getString(R.string.manual_input_only_barcodenumber_bigger1), null, false);
            return;
        }

        this.mShowNumberFragment();
    }

    private void mHandleQuantityChosen(double pvQuantityDbl) {


        if (pvQuantityDbl == 0) {
            ReceiveOrderReceiveActivity.mTryToChangeQuantity(false, true,pvQuantityDbl);
        }
        else {
            ReceiveOrderReceiveActivity.mTryToChangeQuantity(true, true,pvQuantityDbl);
        }

    }

    private static void mTryToChangeQuantity(Boolean pvIsPositiveBln, Boolean pvAmountFixedBln, double pvAmountDbl) {

      double newQuantityDbl;
      double restQuantityDbl;
      cIntakeorderBarcode restIntakeOrderBarcode;

        if ( ReceiveOrderReceiveActivity.scannedBarcodesObl == null) {
            ReceiveOrderReceiveActivity.scannedBarcodesObl = new ArrayList<>();
        }

        if (pvIsPositiveBln) {

            //Determine the new amount
            if (pvAmountFixedBln) {

                newQuantityDbl = pvAmountDbl;
                restQuantityDbl = pvAmountDbl;

                //Clear the barcodeStr list and refill it
                ReceiveOrderReceiveActivity.scannedBarcodesObl.clear();
                int countInt = 0;
                do{
                    countInt += 1;
                    restQuantityDbl -= 1;
                    //Add a barcodeStr to the scanned barcodeStr list, so you can use it later when line is determined
                    ReceiveOrderReceiveActivity.scannedBarcodesObl.add(cIntakeorderBarcode.currentIntakeOrderBarcode);

                    if (restQuantityDbl <1 && restQuantityDbl > 0) {
                        restIntakeOrderBarcode = new cIntakeorderBarcode(cIntakeorderBarcode.currentIntakeOrderBarcode);
                        restIntakeOrderBarcode.quantityPerUnitOfMeasureDbl = restQuantityDbl;
                        ReceiveOrderReceiveActivity.scannedBarcodesObl.add(restIntakeOrderBarcode);
                        break;
                    }

                }
                while(countInt < newQuantityDbl);
            } else {
                newQuantityDbl = ReceiveOrderReceiveActivity.quantityScannedDbl + pvAmountDbl;
            }

            //Check if we would exceed amount, then show message if needed
            if (newQuantityDbl > cReceiveorderSummaryLine.currentReceiveorderSummaryLine.getQuantityDbl()) {

                if (cIntakeorder.currentIntakeOrder.getReceiveNoExtraPiecesBln() && !cIntakeorder.currentIntakeOrder.isGenerated() && cReceiveorderSummaryLine.currentReceiveorderSummaryLine.getQuantityDbl() > 0 ) {
                    ReceiveOrderReceiveActivity.mShowExtraPiecesNotAllowed();
                    return;
                }

                if (cSetting.RECEIVE_EXTRA_PIECES_PERCENTAGE() > 0 && cReceiveorderSummaryLine.currentReceiveorderSummaryLine.getQuantityDbl() > 0  && (cSetting.RECEIVE_EXTRA_PIECES_PERCENTAGE_MANDATORY())) {

                    //Check if the new quantity would exceed the allowed quantity
                    if (newQuantityDbl > cReceiveorderSummaryLine.currentReceiveorderSummaryLine.getAllowedQuantityDbl()) {

                        //We would exceed the allowed quantity so show that this is not allowed
                        ReceiveOrderReceiveActivity.mShowExtraPiecesNotAllowedByPercentage(cReceiveorderSummaryLine.currentReceiveorderSummaryLine.getAllowedQuantityDbl());
                        return;
                    }
                }
            }

            //Set the new quantityDbl and show in Activity
            ReceiveOrderReceiveActivity.quantityScannedDbl = newQuantityDbl;
            ReceiveOrderReceiveActivity.quantityText.setText(pDoubleToStringStr(ReceiveOrderReceiveActivity.quantityScannedDbl));

            if (!pvAmountFixedBln){
                //Add a barcodeStr to the scanned barcodeStr list, so you can use it later when line is determined
                ReceiveOrderReceiveActivity.scannedBarcodesObl.add(cIntakeorderBarcode.currentIntakeOrderBarcode);
            }

            //Check if this line is done
            ReceiveOrderReceiveActivity.mCheckLineDone();
            return;
        }

        //negative

        //Check if value already is zero
        if ( ReceiveOrderReceiveActivity.quantityScannedDbl < 1 ) {

            //If we have a decimal, correct it to zero
            if (ReceiveOrderReceiveActivity.quantityScannedDbl > 0 ) {
                pvAmountDbl = 0;
                pvAmountFixedBln = true;

            } else {
                cUserInterface.pDoNope(ReceiveOrderReceiveActivity.quantityText, true, true);
                return;
            }
        }

        if (pvAmountDbl < 0) {
            cUserInterface.pDoNope(ReceiveOrderReceiveActivity.quantityText, true, true);
            return;
        }

        //Determine the new amount
        if (pvAmountFixedBln) {
            newQuantityDbl = pvAmountDbl;

            //Clear the barcodeStr list and refill it
            ReceiveOrderReceiveActivity.scannedBarcodesObl.clear();
            int countInt = 0;
            do{
                countInt += 1;
                //Add a barcodeStr to the scanned barcodeStr list, so you can use it later when line is determined
                ReceiveOrderReceiveActivity.scannedBarcodesObl.add(cIntakeorderBarcode.currentIntakeOrderBarcode);
                            }while(countInt < newQuantityDbl);

            //Set the new quantityDbl and show in Activity
            ReceiveOrderReceiveActivity.quantityScannedDbl = newQuantityDbl;
            ReceiveOrderReceiveActivity.quantityText.setText(pDoubleToStringStr(ReceiveOrderReceiveActivity.quantityScannedDbl));
            return;


        }else {
            //Remove the last barcodeStr in the list
            ReceiveOrderReceiveActivity.scannedBarcodesObl.remove( ReceiveOrderReceiveActivity.scannedBarcodesObl.size()-1);
            newQuantityDbl= ReceiveOrderReceiveActivity.quantityScannedDbl - pvAmountDbl;
        }

        if (newQuantityDbl <= 0) {
            ReceiveOrderReceiveActivity.quantityScannedDbl = (double) 0;
        }else {
            //Set the new quantityDbl and show in Activity
            ReceiveOrderReceiveActivity.quantityScannedDbl = newQuantityDbl;
        }

        ReceiveOrderReceiveActivity.quantityText.setText(pDoubleToStringStr(ReceiveOrderReceiveActivity.quantityScannedDbl));
        ReceiveOrderReceiveActivity.imageButtonDone.setImageResource(R.drawable.ic_check_black_24dp);
    }

    private static void mBarcodeSelected(cIntakeorderBarcode pvBarcode) {

        cUserInterface.pCheckAndCloseOpenDialogs();

        cIntakeorderBarcode.currentIntakeOrderBarcode = pvBarcode;
        ReceiveOrderReceiveActivity.mShowBarcodeInfo();

        ReceiveOrderReceiveActivity.mTryToChangeQuantity(true, false, cIntakeorderBarcode.currentIntakeOrderBarcode.getQuantityPerUnitOfMeasureDbl());
    }

    // Lines, Barcodes

    private static void mCheckLineDone() {

        //Start with complete
        ReceiveOrderReceiveActivity.imageButtonDone.setVisibility(View.VISIBLE);

        //Check if quantityDbl is sufficient
        if (!cReceiveorderSummaryLine.currentReceiveorderSummaryLine.pQuantityReachedBln(ReceiveOrderReceiveActivity.scannedBarcodesObl)) {
            ReceiveOrderReceiveActivity.imageButtonDone.setVisibility(View.VISIBLE);
            ReceiveOrderReceiveActivity.imageButtonDone.setImageResource(R.drawable.ic_check_black_24dp);
            return;
        }


        //We are complete
        ReceiveOrderReceiveActivity.imageButtonDone.setVisibility(View.VISIBLE);
        ReceiveOrderReceiveActivity.imageButtonDone.setImageResource(R.drawable.ic_doublecheck_black_24dp);

    }

    private static boolean mSendScansBln() {

        //If internet is not connected
        if (!cConnection.isInternetConnectedBln()) {
            //could not send line, let user know but answer succes so user can go to next line
            cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.couldnt_send_line_no_connection), null);
            return false;
        }

        List<cIntakeorderBarcode> sortedBarcodeList = mSortBarcodeList(ReceiveOrderReceiveActivity.scannedBarcodesObl);

       if(!cReceiveorderSummaryLine.currentReceiveorderSummaryLine.pItemVariantHandledBln(sortedBarcodeList)) {
            cUserInterface.pDoExplodingScreen(cAppExtension.context.getString(R.string.couldnt_send_line), "",true,true);
            return false;
        }

  return  true;
    }

    private static void mReceiveDone() {

    if (ReceiveOrderReceiveActivity.mSendScansBln()) {
        ReceiveOrderReceiveActivity.mResetCurrents();
        ReceiveOrderReceiveActivity.mGoBackToLinesActivity();
    }

     }

     //ScanActions
     private static void mFillRecycler(List<cReceiveorderLine> pvDataObl) {
         cReceiveorderLine.getReceiveorderLineAdapter().pFillData(pvDataObl);
         ReceiveOrderReceiveActivity.recyclerScanActions.setHasFixedSize(false);
         ReceiveOrderReceiveActivity.recyclerScanActions.setAdapter(cReceiveorderLine.getReceiveorderLineAdapter());
         ReceiveOrderReceiveActivity.recyclerScanActions.setLayoutManager(new LinearLayoutManager(cAppExtension.context));
     }

    //Listeners
    private void mSetDoneListener() {
        ReceiveOrderReceiveActivity.imageButtonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               ReceiveOrderReceiveActivity.pAcceptReceive();
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void mSetPlusListener() {

        ReceiveOrderReceiveActivity.imageButtonPlus.setOnTouchListener(new View.OnTouchListener() {

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

        ReceiveOrderReceiveActivity.imageButtonPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (BuildConfig.FLAVOR.equalsIgnoreCase(cProductFlavor.FlavorEnu.BMN.toString())) {
                    cIntakeorderBarcode.pTryToSetStandardBarcode(cReceiveorderSummaryLine.currentReceiveorderSummaryLine);
                }

                //There is no selected barcodeStr, select one first
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

        ReceiveOrderReceiveActivity.imageButtonMinus.setOnTouchListener(new View.OnTouchListener() {
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

        ReceiveOrderReceiveActivity.imageButtonMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (BuildConfig.FLAVOR.equalsIgnoreCase(cProductFlavor.FlavorEnu.BMN.toString())) {
                    cIntakeorderBarcode.pTryToSetStandardBarcode(cReceiveorderSummaryLine.currentReceiveorderSummaryLine);
                }

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
        ReceiveOrderReceiveActivity.quantityText.setOnClickListener(new View.OnClickListener() {
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

        ReceiveOrderReceiveActivity.amountExceededDialogShowedBln = false;

        final AcceptRejectFragment acceptRejectFragment = new AcceptRejectFragment(cAppExtension.activity.getString(R.string.message_orderlinebusy_header),
                                                                                   cAppExtension.activity.getString(R.string.message_orderlinebusy_text),
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

    private static void mShowExtraPiecesPercentageExceededFragment(Double pvValueDbl){

        cUserInterface.pCheckAndCloseOpenDialogs();

        ReceiveOrderReceiveActivity.amountExceededDialogShowedBln = true;

        final AcceptRejectFragment acceptRejectFragment = new AcceptRejectFragment(cAppExtension.activity.getString(R.string.message_orderlinebusy_header),
                cAppExtension.context.getString(R.string.number_received_total_eminently_more, cText.pDoubleToStringStr(pvValueDbl), cText.pDoubleToStringStr(cReceiveorderSummaryLine.currentReceiveorderSummaryLine.getQuantityDbl())),
                cAppExtension.activity.getString(R.string.message_cancel_line), cAppExtension.activity.getString(R.string.message_accept_line), false);
        acceptRejectFragment.setCancelable(true);

        cAppExtension.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // show my popup
                acceptRejectFragment.show(cAppExtension.fragmentManager, ACCEPTREJECTFRAGMENT_TAG);
            }
        });
    }

    private static void mShowExtraPiecesNotAllowed(){
        ReceiveOrderReceiveActivity.quantityText.setText(cText.pDoubleToStringStr(cReceiveorderSummaryLine.currentReceiveorderSummaryLine.getQuantityDbl()));
        cUserInterface.pShowSnackbarMessage(toolbarImage , cAppExtension.context.getString(R.string.number_cannot_be_higher), null, false);
        cUserInterface.pDoNope(quantityText, true, true);
        cUserInterface.pDoNope(quantityText, false, false);
    }

    private static void mShowExtraPiecesNotAllowedByPercentage(Double pvValueDbl){

            cUserInterface.pShowSnackbarMessage(toolbarImage , cAppExtension.context.getString(R.string.number_received_total_cant_be_higher_then, cText.pDoubleToStringStr(pvValueDbl)) , null, false);
            cUserInterface.pDoNope(quantityText, true, true);
            cUserInterface.pDoNope(quantityText, false, false);

    }

    private static void mGoBackToLinesActivity() {
        Intent intent = new Intent(cAppExtension.context, ReceiveLinesActivity.class);
        cAppExtension.activity.startActivity(intent);
        cAppExtension.activity.finish();
    }

    private static void mResetCurrents() {

        cReceiveorderSummaryLine.currentReceiveorderSummaryLine = null;
        cReceiveorderLine.currentReceiveorderLine = null;
        cIntakeorder.currentIntakeOrder.intakeorderBarcodeScanned = null;
        cIntakeorderBarcode.currentIntakeOrderBarcode = null;
        ReceiveOrderReceiveActivity.scannedBarcodesObl = null;
        ReceiveOrderReceiveActivity.quantityScannedDbl = 0.0;
        ReceiveOrderReceiveActivity.scannedBarcodesObl = null;

    }

    private void mShowBarcodeSelectFragment() {
        BarcodeFragment barcodeFragment = new BarcodeFragment();
        barcodeFragment.show(cAppExtension.fragmentManager, BARCODEFRAGMENT_TAG);
    }

    private void mShowNumberFragment() {

        cUserInterface.pCheckAndCloseOpenDialogs();

        Bundle bundle = new Bundle();
        bundle.putInt(cPublicDefinitions.NUMBERINTENT_CURRENTQUANTITY,  cReceiveorderSummaryLine.currentReceiveorderSummaryLine.getQuantityHandledDbl().intValue());

        if (cIntakeorder.currentIntakeOrder.getReceiveStoreAutoAcceptAtRequestedBln()) {
            bundle.putDouble(cPublicDefinitions.NUMBERINTENT_MAXQUANTITY,  cReceiveorderSummaryLine.currentReceiveorderSummaryLine.getQuantityDbl().intValue());
        } else {
            bundle.putDouble(cPublicDefinitions.NUMBERINTENT_MAXQUANTITY,99999);
        }

        NumberpickerFragment numberpickerFragment = new NumberpickerFragment();
        numberpickerFragment.setArguments(bundle);

        numberpickerFragment.show(cAppExtension.fragmentManager, NUMBERFRAGMENT_TAG);
    }

    private static void mSetArticleInfo(){

        if ( cReceiveorderSummaryLine.currentReceiveorderSummaryLine == null) {
            ReceiveOrderReceiveActivity.articleDescriptionText.setText("???");
            ReceiveOrderReceiveActivity.articleDescription2Text.setText("???");
            ReceiveOrderReceiveActivity.articleItemText.setText("???");
            ReceiveOrderReceiveActivity.articleVendorItemText.setText("???");
            return;
        }
            ReceiveOrderReceiveActivity.articleDescriptionText.setText(cReceiveorderSummaryLine.currentReceiveorderSummaryLine.getDescriptionStr());
            ReceiveOrderReceiveActivity.articleDescription2Text.setText(cReceiveorderSummaryLine.currentReceiveorderSummaryLine.getDescription2Str());
            ReceiveOrderReceiveActivity.articleItemText.setText(cReceiveorderSummaryLine.currentReceiveorderSummaryLine.getItemNoAndVariantCodeStr());
            ReceiveOrderReceiveActivity.articleVendorItemText.setText(cReceiveorderSummaryLine.currentReceiveorderSummaryLine.getVendorItemNoAndDescriptionStr());
    }

    private static void mSetQuantityInfo(){
        ReceiveOrderReceiveActivity.quantityText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        ReceiveOrderReceiveActivity.quantityText.setSelectAllOnFocus(true);
        ReceiveOrderReceiveActivity.quantityText.setSingleLine();
        ReceiveOrderReceiveActivity.quantityText.requestFocus();
        ReceiveOrderReceiveActivity.quantityText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_TEXT_VARIATION_NORMAL);
        ReceiveOrderReceiveActivity.quantityText.setCursorVisible(false);

        TextWatcher tw = new cNumberTextWatcher(ReceiveOrderReceiveActivity.quantityText, 2,999999d);
        ReceiveOrderReceiveActivity.quantityText.addTextChangedListener((tw));

        if ( cReceiveorderSummaryLine.currentReceiveorderSummaryLine== null) {
            ReceiveOrderReceiveActivity.quantityText.setText("0");
            ReceiveOrderReceiveActivity.quantityRequiredText.setText("0");
            return;
        }

        ReceiveOrderReceiveActivity.quantityText.setText("0");

        if (!cIntakeorder.currentIntakeOrder.isGenerated()) {
            ReceiveOrderReceiveActivity.quantityRequiredText.setVisibility(View.VISIBLE);
            ReceiveOrderReceiveActivity.quantityRequiredText.setText(pDoubleToStringStr(cReceiveorderSummaryLine.currentReceiveorderSummaryLine.pGetQuantityToHandleDbl()));

            if (cReceiveorderSummaryLine.currentReceiveorderSummaryLine.pGetQuantityToHandleDbl() == 0) {
                ReceiveOrderReceiveActivity.quantityRequiredText.setVisibility(View.GONE);
            }

        }
        else {
            ReceiveOrderReceiveActivity.quantityRequiredText.setVisibility(View.GONE);
        }

    }

    private static void mSetSourceNoInfo(){

        if (nl.icsvertex.scansuite.BuildConfig.FLAVOR.equalsIgnoreCase(cProductFlavor.FlavorEnu.BMN.toString())) {
            ReceiveOrderReceiveActivity.sourceNoContainer.setVisibility(View.GONE);
            return;
        }

        ReceiveOrderReceiveActivity.sourcenoText.setText( cIntakeorder.currentIntakeOrder.getOrderNumberStr());
    }

    private static void mSetBinInfo(){
        ReceiveOrderReceiveActivity.binCodeText.setText( cReceiveorderSummaryLine.currentReceiveorderSummaryLine.getBinCodeStr());
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
        ReceiveOrderReceiveActivity.minusHandler.postDelayed(pvRunnable, pvMilliSecsLng);
        ReceiveOrderReceiveActivity.counterMinusHelperInt += 1;
    }

    private void mDoDelayedPlus(Runnable pvRunnable, long pvMilliSecsLng) {
        ReceiveOrderReceiveActivity.plusHandler.postDelayed(pvRunnable, pvMilliSecsLng);
        ReceiveOrderReceiveActivity.counterPlusHelperInt += 1;
    }

    private static void mRemoveAdapterFromFragment(){

        if (cReceiveorderLine.currentReceiveorderLine.quantityHandledDbl == 0) {
            cAppExtension.activity.getString(R.string.message_zero_lines_cant_be_reset);
            return;
        }

        //remove the item from recyclerview
        boolean resultBln = cReceiveorderLine.currentReceiveorderLine.pResetBln();
        if (! resultBln) {
            cUserInterface.pDoExplodingScreen(cAppExtension.activity.getString(R.string.message_reset_line_via_webservice_failed),"",true,true);
            return;
        }

        //Renew data, so only current lines are shown
        ReceiveOrderReceiveActivity.mFieldsInitializeStatic();

    }

    private static List<cIntakeorderBarcode> mSortBarcodeList(List<cIntakeorderBarcode> pvUnsortedBarcodeObl) {
        List<cIntakeorderBarcode> resultList = new ArrayList<>();
        boolean barcodeFoundBln = false;
        for (cIntakeorderBarcode intakeorderBarcode : pvUnsortedBarcodeObl) {
            for (cIntakeorderBarcode resultBarcode : resultList) {
                if (resultBarcode.getBarcodeStr().equalsIgnoreCase(intakeorderBarcode.getBarcodeStr())) {
                    resultBarcode.quantityHandledDbl +=  intakeorderBarcode.getQuantityPerUnitOfMeasureDbl();
                    barcodeFoundBln = true;
                }
            }
            if (barcodeFoundBln) {
                barcodeFoundBln = false;
            }
            else {
                //new barcode, so add
                intakeorderBarcode.quantityHandledDbl = intakeorderBarcode.getQuantityPerUnitOfMeasureDbl();
                resultList.add(intakeorderBarcode);
            }
        }
        return resultList;
    }

    private void mSetEditorActionListener() {

        ReceiveOrderReceiveActivity.quantityText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (BuildConfig.FLAVOR.equalsIgnoreCase(cProductFlavor.FlavorEnu.BMN.toString())) {
                    cIntakeorderBarcode.pTryToSetStandardBarcode(cReceiveorderSummaryLine.currentReceiveorderSummaryLine);
                }

                //There is no selected barcodeStr, select one first
                if (cIntakeorderBarcode.currentIntakeOrderBarcode == null) {
                    cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.message_select_one_of_multiple_barcodes),null);
                    cUserInterface.pHideKeyboard();
                    return;
                }

                ReceiveOrderReceiveActivity.quantityText.requestFocus();
                ReceiveOrderReceiveActivity.quantityText.setSelection(0,ReceiveOrderReceiveActivity.quantityText.getText().toString().length());
            }
        });


        ReceiveOrderReceiveActivity.quantityText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE || i == EditorInfo.IME_ACTION_GO ) {

                    ReceiveOrderReceiveActivity.mTryToChangeQuantity(true,
                            true,
                            cText.pStringToDoubleDbl(String.valueOf(ReceiveOrderReceiveActivity.quantityText.getText())));

                    ReceiveOrderReceiveActivity.imageButtonDone.callOnClick();
                }
                return true;
            }
        });
    }

    //End Region Number Broadcaster

    //End Regin Private Methods

}

