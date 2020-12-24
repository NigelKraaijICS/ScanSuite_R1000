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
import android.view.ViewGroup;
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
import androidx.constraintlayout.widget.ConstraintSet;
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
import ICS.Utils.cNumberTextWatcher;
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
import SSU_WHS.Receive.ReceiveSummaryLine.cReceiverorderSummaryLineAdapter;
import nl.icsvertex.scansuite.BuildConfig;
import nl.icsvertex.scansuite.Fragments.Dialogs.AcceptRejectFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.ArticleFullViewFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.BarcodeFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.NumberpickerFragment;
import nl.icsvertex.scansuite.R;

public class ReceiveOrderReceiveActivity extends AppCompatActivity implements iICSDefaultActivity, cReceiveorderLineRecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    //Region Private Properties
    public  boolean amountExceededDialogShowedBln = false;
    public  int positionSwiped;

    private  int counterMinusHelperInt;
    private  int counterPlusHelperInt;
    private  Handler minusHandler;
    private  Handler plusHandler;
    private  AppCompatImageButton imageButtonMinus;
    private  AppCompatImageButton imageButtonPlus;
    private  AppCompatImageButton imageButtonDone;

    private  ConstraintLayout receiveOrderReceiveContainer;

    private  Toolbar toolbar;
    private  ImageView toolbarImage;
    private  TextView toolbarTitle;
    private  TextView toolbarSubtext;


    private  CardView articleContainer;
    private ConstraintLayout articleInfoContainer;

    private  TextView articleDescriptionText;
    private  TextView articleDescription2Text;
    private  TextView articleItemText;
    private  TextView articleBarcodeText;

    private  ImageView articleThumbImageView;
    private  ImageView imageButtonBarcode;

    private  EditText quantityText;
    private  TextView quantityRequiredText;


    private  TextView sourcenoText;

    private  TextView binCodeText;

    private  Double quantityScannedDbl = 0.0;
    private  List<cIntakeorderBarcode> scannedBarcodesObl;
    private  RecyclerView recyclerScanActions;


    private cReceiverorderSummaryLineAdapter receiverorderSummaryLineAdapter;
    private cReceiverorderSummaryLineAdapter getReceiverorderSummaryLineAdapter(){

        if (this.receiverorderSummaryLineAdapter == null) {
            this.receiverorderSummaryLineAdapter = new cReceiverorderSummaryLineAdapter();
        }
        return  this.receiverorderSummaryLineAdapter;

    }

    private cReceiveorderLineAdapter receiveorderLineAdapter;
    private cReceiveorderLineAdapter getReceiveorderLineAdapter(){

        if (this.receiveorderLineAdapter == null) {
            this.receiveorderLineAdapter = new cReceiveorderLineAdapter();
        }
        return  this.receiveorderLineAdapter;

    }

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
            cBarcodeScan.pUnregisterBarcodeReceiver(this.getClass().getSimpleName());
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
        cBarcodeScan.pRegisterBarcodeReceiver(this.getClass().getSimpleName());
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
            if (this.quantityScannedDbl == 0 ) {
                this.mResetCurrents();
                this.mGoBackToLinesActivity();
                return  true;
            }

            this.mShowAcceptFragment();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        if (this.quantityScannedDbl == 0 ) {
            this.mResetCurrents();
            this.mGoBackToLinesActivity();
            return;
        }

        this.mShowAcceptFragment();
    }


    @Override
    public void onSwiped(RecyclerView.ViewHolder pvViewHolder, int direction, int pvPositionInt) {

        if (!(pvViewHolder instanceof  cReceiveorderLineAdapter.ReceiveorderLineViewHolder)) {
            return;
        }

        this.positionSwiped = pvPositionInt;
        cReceiveorderLine.currentReceiveorderLine = cReceiveorderSummaryLine.currentReceiveorderSummaryLine.receivedLinesReversedObl().get(pvPositionInt);

        //do we need an adult for this?
        if (!cSetting.RECEIVE_RESET_PASSWORD().isEmpty()) {
            cUserInterface.pShowpasswordDialog(getString(R.string.supervisor_password_header), getString(R.string.supervisor_password_text), false);
            return;
        }

        //Remove the enviroment
        this.mRemoveAdapterFromFragment();

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

        this.receiveOrderReceiveContainer = findViewById(R.id.receiveOrderReceiveContainer);

        this.toolbar = findViewById(R.id.toolbar);
        this.toolbarImage = findViewById(R.id.toolbarImage);
        this.toolbarTitle = findViewById(R.id.toolbarTitle);
        this.toolbarSubtext = findViewById(R.id.toolbarSubtext);

        this.articleContainer = findViewById(R.id.addressContainer);
        this.articleInfoContainer = findViewById(R.id.articleInfoContainer);

        this.articleDescriptionText = findViewById(R.id.articleDescriptionText);
        this.articleDescription2Text = findViewById(R.id.articleDescription2Text);
        this.articleItemText = findViewById(R.id.articleItemText);
        this.articleBarcodeText = findViewById(R.id.articleBarcodeText);

        this.sourcenoText = findViewById(R.id.sourcenoText);
        this.binCodeText = findViewById(R.id.binText);


        this.quantityText = findViewById(R.id.quantityText);
        this.quantityRequiredText = findViewById(R.id.quantityRequiredText);

        this.articleThumbImageView = findViewById(R.id.articleThumbImageView);
        this.imageButtonBarcode = findViewById(R.id.imageButtonBarcode);
        this.imageButtonMinus = findViewById(R.id.imageButtonMinus);
        this.imageButtonPlus = findViewById(R.id.imageButtonPlus);
        this.imageButtonDone = findViewById(R.id.imageButtonDone);

        this.recyclerScanActions = findViewById(R.id.recyclerScanActions);

    }

    @Override
    public void mSetToolbar(String pvScreenTitle) {
        this.toolbarImage.setImageResource(R.drawable.ic_menu_intake_eo);
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

        this.counterPlusHelperInt = 0;
        this.counterMinusHelperInt = 0;

        this.toolbarSubtext.setText(cIntakeorder.currentIntakeOrder.getOrderNumberStr());

        this.mSetArticleInfo();
        this.mSetSourceNoInfo();
        this.mSetBinInfo();
        this.mSetQuantityInfo();

        this.mEnablePlusMinusAndBarcodeSelectViews();
        this.mShowArticleImage();
        this.mShowOrHideGenericExtraFields();
        this.mShowBarcodeInfo();
        this.mShowLines();
        this.mHideArticleInfo();

    }

    @Override
    public void mInitScreen() {
        cBarcodeScan.pRegisterBarcodeReceiver(this.getClass().getSimpleName());
        //Raise quantity with scanned barcodeStr, if we started this activity with a scan
        if (cIntakeorder.currentIntakeOrder.intakeorderBarcodeScanned != null) {
            this.pHandleScan(cBarcodeScan.pFakeScan(cIntakeorder.currentIntakeOrder.intakeorderBarcodeScanned.getBarcodeStr()));
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

    public  void pHandleScan(cBarcodeScan pvBarcodeScan) {

        cUserInterface.pCheckAndCloseOpenDialogs();
        cResult hulpRst;

        //Check if we have scanned an ARTICLE and check if there are not handled linesInt for this BIN
        if (cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(), cBarcodeLayout.barcodeLayoutEnu.ARTICLE)) {

            hulpRst = this.mHandleArticleScanRst(pvBarcodeScan);
            if (! hulpRst.resultBln) {
                cUserInterface.pDoExplodingScreen(hulpRst.messagesStr(),"",true,true);
                return;
            }
            return;
        }

        cUserInterface.pShowSnackbarMessage(receiveOrderReceiveContainer,cAppExtension.activity.getString(R.string.message_unknown_barcode),null,true);

    }

    public  void pAcceptReceive() {

        if (this.quantityScannedDbl == 0 ) {
            this.mResetCurrents();
            this.mGoBackToLinesActivity();
            return;
        }

        if (!this.amountExceededDialogShowedBln) {
            //Check if we exceed the allowed quantity, so we can inform the user
            if (this.quantityScannedDbl > cReceiveorderSummaryLine.currentReceiveorderSummaryLine.getAllowedQuantityDbl()
                && cSetting.RECEIVE_EXTRA_PIECES_PERCENTAGE() > 0
                && cReceiveorderSummaryLine.currentReceiveorderSummaryLine.getQuantityDbl() > 0)  {
                this.mShowExtraPiecesPercentageExceededFragment(this.quantityScannedDbl);
                return;
            }
        }


        //We are done
        this.mReceiveDone();

    }

    public void pRegisterBarcodeReceiver(){
        cBarcodeScan.pRegisterBarcodeReceiver(this.getClass().getSimpleName());
    }

    public  void pCancelReceive() {
        this.mResetCurrents();
        this.mGoBackToLinesActivity();
    }

    public  void pPasswordSuccess() {
        cBarcodeScan.pRegisterBarcodeReceiver(this.getClass().getSimpleName());

        //Remove the line
        this.mRemoveAdapterFromFragment();
    }

    public  void pPasswordCancelled() {
        cBarcodeScan.pRegisterBarcodeReceiver(this.getClass().getSimpleName());
        this.getReceiverorderSummaryLineAdapter().notifyItemChanged( this.positionSwiped );
    }

    //End Region Public Methods

    //Region Private Methods

    //Views

    private  void mShowLines() {

        if (cReceiveorderSummaryLine.currentReceiveorderSummaryLine.receiveLinesObl == null  || cReceiveorderSummaryLine.currentReceiveorderSummaryLine.receiveLinesObl.size() == 0) {
            this.mFillRecycler(new ArrayList<cReceiveorderLine>());
            return;
        }

        this.mFillRecycler(cReceiveorderSummaryLine.currentReceiveorderSummaryLine.receivedLinesReversedObl());
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

                if (cReceiveorderSummaryLine.currentReceiveorderSummaryLine.barcodesObl() == null || cReceiveorderSummaryLine.currentReceiveorderSummaryLine.barcodesObl().size() == 0) {
                    return;
                }

                mEnablePlusMinusAndBarcodeSelectViews();

                //If we only have one barcodeStr, then automatticaly select that barcodeStr
                if (cReceiveorderSummaryLine.currentReceiveorderSummaryLine.barcodesObl().size() == 1) {
                    cIntakeorder.currentIntakeOrder.intakeorderBarcodeScanned = cReceiveorderSummaryLine.currentReceiveorderSummaryLine.barcodesObl().get(0);
                    pHandleScan(cBarcodeScan.pFakeScan( cIntakeorder.currentIntakeOrder.intakeorderBarcodeScanned.getBarcodeStr()));
                    return;
                }

                mShowBarcodeSelectFragment();

            }
        });
    }

    private void mSetRecyclerTouchListener(){


        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new cReceiveorderLineRecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(this.recyclerScanActions);
    }

    private  void mShowOrHideGenericExtraFields() {

    }

    private  void mShowBarcodeInfo() {

        if (cReceiveorderSummaryLine.currentReceiveorderSummaryLine== null) {
            this.articleBarcodeText.setText("???");
            return;
        }

        if (cIntakeorderBarcode.currentIntakeOrderBarcode == null) {
            if (cReceiveorderSummaryLine.currentReceiveorderSummaryLine.barcodesObl().size() == 1) {
                cIntakeorderBarcode.currentIntakeOrderBarcode = cReceiveorderSummaryLine.currentReceiveorderSummaryLine.barcodesObl().get(0);
            }
        }

        if (cIntakeorderBarcode.currentIntakeOrderBarcode!= null) {
            this.articleBarcodeText.setText(cIntakeorderBarcode.currentIntakeOrderBarcode.getBarcodeAndQuantityStr() + " " + cIntakeorderBarcode.currentIntakeOrderBarcode.getUnitOfMeasureStr() );
        } else {
            this.articleBarcodeText.setText(cAppExtension.context.getString(R.string.mutiple_barcodes_posible));
        }
    }

    private void mShowArticleImage() {

        this.articleThumbImageView.setImageDrawable(ContextCompat.getDrawable(cAppExtension.context, R.drawable.ic_no_image_lightgrey_24dp));

        //If pick with picture is false, then hide image view
        if (!cIntakeorder.currentIntakeOrder.isReceiveWithPictureBln()) {
            this.articleThumbImageView.setVisibility(View.INVISIBLE);
            return;
        }

        //If picture is not in cache (via webservice) then show no image
        if (!cReceiveorderSummaryLine.currentReceiveorderSummaryLine.pGetArticleImageBln()) {
            cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.could_not_get_article_image), null);
            this.articleThumbImageView.setImageDrawable(ContextCompat.getDrawable(cAppExtension.context, R.drawable.ic_no_image_lightgrey_24dp));
            return;
        }

        //If picture is in cache but can't be converted, then show no image
        if (cReceiveorderSummaryLine.currentReceiveorderSummaryLine.articleImage == null || cReceiveorderSummaryLine.currentReceiveorderSummaryLine.articleImage.imageBitmap() == null) {
            cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.could_not_get_article_image), null);
            this.articleThumbImageView.setImageDrawable(ContextCompat.getDrawable(cAppExtension.context, R.drawable.ic_no_image_lightgrey_24dp));
            return;
        }

        //Show the image
        this.articleThumbImageView.setImageBitmap(cReceiveorderSummaryLine.currentReceiveorderSummaryLine.articleImage.imageBitmap());

        //Open the image
        if ((cIntakeorder.currentIntakeOrder.isReceiveWithPictureAutoOpenBln())) {
            this.mShowFullArticleFragment();
        }

    }

    private  void mEnablePlusMinusAndBarcodeSelectViews() {

        if (cReceiveorderSummaryLine.currentReceiveorderSummaryLine== null) {
            this.imageButtonMinus.setVisibility(View.INVISIBLE);
            this.imageButtonPlus.setVisibility(View.INVISIBLE);
            this.imageButtonBarcode.setVisibility(View.INVISIBLE);
            this.imageButtonDone.setVisibility(View.INVISIBLE);
            return;
        }

        this.imageButtonDone.setVisibility(View.VISIBLE);

        if (!cSetting.RECEIVE_AMOUNT_MANUAL_MA()) {
            this.imageButtonMinus.setVisibility(View.INVISIBLE);
            this.imageButtonPlus.setVisibility(View.INVISIBLE);
            this.imageButtonBarcode.setVisibility(View.INVISIBLE);
        } else {
            this.imageButtonMinus.setVisibility(View.VISIBLE);
            this.imageButtonPlus.setVisibility(View.VISIBLE);
            this.imageButtonBarcode.setVisibility(View.VISIBLE);
        }
    }

    private void mHideArticleInfo(){

        this.articleInfoContainer.setVisibility(View.GONE);
        ConstraintLayout.LayoutParams newCardViewLayoutParams = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        newCardViewLayoutParams.setMargins(15,15,15,15);
        this.articleContainer.setLayoutParams(newCardViewLayoutParams);

        ConstraintSet constraintSetSpace = new ConstraintSet();
        constraintSetSpace.clone(this.receiveOrderReceiveContainer);
        constraintSetSpace.connect(this.articleContainer.getId(), ConstraintSet.TOP, toolbar.getId(), ConstraintSet.BOTTOM);
        constraintSetSpace.applyTo(this.receiveOrderReceiveContainer);

    }


    //Scans and manual input

    private  cResult mHandleArticleScanRst(final cBarcodeScan pvBarcodeScan){

        cResult result = new cResult();
        result.resultBln = true;


        //We opened the line withoud scanning, so handle the scan in previous activity
        if (cIntakeorder.currentIntakeOrder.intakeorderBarcodeScanned == null){
            this.mResetCurrents();
            this.mGoBackToLinesActivity();

            cAppExtension.activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ReceiveLinesActivity receiveLinesActivity = new ReceiveLinesActivity();
                    receiveLinesActivity.pHandleScan(pvBarcodeScan,false);
                    finish();
                }
            });

            return result;
        }

        if (cIntakeorder.currentIntakeOrder.isGenerated()) {

            //Check if we scanned the same barcode
            if (pvBarcodeScan.getBarcodeOriginalStr().equalsIgnoreCase(cIntakeorder.currentIntakeOrder.intakeorderBarcodeScanned.getBarcodeStr())) {
                this.mBarcodeSelected(cIntakeorder.currentIntakeOrder.intakeorderBarcodeScanned);
                return result;
            }

            //We scanned a new barcode, finish barcode and pass scan on
            if (this.mSendScansBln()) {
                this.mResetCurrents();
                this.mGoBackToLinesActivity();

                cAppExtension.activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ReceiveLinesActivity receiveLinesActivity = new ReceiveLinesActivity();
                        receiveLinesActivity.pHandleScan(pvBarcodeScan,false);
                        finish();
                    }
                });

                return result;
            }

        }

        //Check if scanned barcode, belong to this item variant, if so raise quantity
        cIntakeorder.currentIntakeOrder.intakeorderBarcodeScanned  = cReceiveorderSummaryLine.currentReceiveorderSummaryLine.pGetBarcode(pvBarcodeScan);
        if (cIntakeorder.currentIntakeOrder.intakeorderBarcodeScanned  != null) {
            this.mBarcodeSelected(cIntakeorder.currentIntakeOrder.intakeorderBarcodeScanned );
            return result;
        }

        //We scanned a barcode that doesn't belong to this item variant, so let the previous activity handle the scan
        if (this.mSendScansBln()) {
            this.mResetCurrents();
            this.mGoBackToLinesActivity();


            cAppExtension.activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ReceiveLinesActivity receiveLinesActivity = new ReceiveLinesActivity();
                    receiveLinesActivity.pHandleScan(pvBarcodeScan,false);
                    finish();
                }
            });

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
            this.mTryToChangeQuantityBln(false, true,pvQuantityDbl);
        }
        else {
            this.mTryToChangeQuantityBln(true, true,pvQuantityDbl);
        }

    }

    private  boolean mTryToChangeQuantityBln(Boolean pvIsPositiveBln, Boolean pvAmountFixedBln, double pvAmountDbl) {

      double newQuantityDbl;
      double restQuantityDbl;
      cIntakeorderBarcode restIntakeOrderBarcode;

        if ( this.scannedBarcodesObl == null) {
            this.scannedBarcodesObl = new ArrayList<>();
        }

        if (pvIsPositiveBln) {

            //Determine the new amount
            if (pvAmountFixedBln) {

                newQuantityDbl = pvAmountDbl;
                restQuantityDbl = pvAmountDbl;

                //Clear the barcodeStr list and refill it
                this.scannedBarcodesObl.clear();
                int countInt = 0;
                do{
                    countInt += 1;
                    restQuantityDbl -= 1;
                    //Add a barcodeStr to the scanned barcodeStr list, so you can use it later when line is determined
                    this.scannedBarcodesObl.add(cIntakeorderBarcode.currentIntakeOrderBarcode);

                    if (restQuantityDbl <1 && restQuantityDbl > 0) {
                        restIntakeOrderBarcode = new cIntakeorderBarcode(cIntakeorderBarcode.currentIntakeOrderBarcode);
                        restIntakeOrderBarcode.quantityPerUnitOfMeasureDbl = restQuantityDbl;
                        this.scannedBarcodesObl.add(restIntakeOrderBarcode);
                        break;
                    }

                }
                while(countInt < newQuantityDbl);
            } else {
                newQuantityDbl = this.quantityScannedDbl + pvAmountDbl;
            }

            //Check if we would exceed amount, then show message if needed
            if (newQuantityDbl > cReceiveorderSummaryLine.currentReceiveorderSummaryLine.getQuantityDbl()) {

                if (cIntakeorder.currentIntakeOrder.getReceiveNoExtraPiecesBln() && !cIntakeorder.currentIntakeOrder.isGenerated() && cReceiveorderSummaryLine.currentReceiveorderSummaryLine.getQuantityDbl() > 0 ) {
                    this.mShowExtraPiecesNotAllowed();
                    return false;
                }

                if (cSetting.RECEIVE_EXTRA_PIECES_PERCENTAGE() > 0 && cReceiveorderSummaryLine.currentReceiveorderSummaryLine.getQuantityDbl() > 0  && (cSetting.RECEIVE_EXTRA_PIECES_PERCENTAGE_MANDATORY())) {

                    //Check if the new quantity would exceed the allowed quantity
                    if (newQuantityDbl > cReceiveorderSummaryLine.currentReceiveorderSummaryLine.getAllowedQuantityDbl()) {

                        //We would exceed the allowed quantity so show that this is not allowed
                        this.mShowExtraPiecesNotAllowedByPercentage(cReceiveorderSummaryLine.currentReceiveorderSummaryLine.getAllowedQuantityDbl());
                        return false;
                    }
                }
            }

            //Set the new quantityDbl and show in Activity
            this.quantityScannedDbl = newQuantityDbl;
            this.quantityText.setText(cText.pDoubleToStringStr(this.quantityScannedDbl));

            if (!pvAmountFixedBln){
                //Add a barcodeStr to the scanned barcodeStr list, so you can use it later when line is determined
                this.scannedBarcodesObl.add(cIntakeorderBarcode.currentIntakeOrderBarcode);
            }

            //Check if this line is done
            this.mCheckLineDone();
            return true;
        }

        //negative

        //Check if value already is zero
        if ( this.quantityScannedDbl < 1 ) {

            //If we have a decimal, correct it to zero
            if (this.quantityScannedDbl > 0 ) {
                pvAmountDbl = 0;
                pvAmountFixedBln = true;

            } else {
                cUserInterface.pDoNope(this.quantityText, true, true);
                return false;
            }
        }

        if (pvAmountDbl < 0) {
            cUserInterface.pDoNope(this.quantityText, true, true);
            return false;
        }

        //Determine the new amount
        if (pvAmountFixedBln) {
            newQuantityDbl = pvAmountDbl;

            //Clear the barcodeStr list and refill it
            this.scannedBarcodesObl.clear();
            int countInt = 0;
            do{
                countInt += 1;
                //Add a barcodeStr to the scanned barcodeStr list, so you can use it later when line is determined
                this.scannedBarcodesObl.add(cIntakeorderBarcode.currentIntakeOrderBarcode);
                            }while(countInt < newQuantityDbl);

            //Set the new quantityDbl and show in Activity
            this.quantityScannedDbl = newQuantityDbl;
            this.quantityText.setText(cText.pDoubleToStringStr(this.quantityScannedDbl));
            return true;


        }else {
            //Remove the last barcodeStr in the list
            this.scannedBarcodesObl.remove( this.scannedBarcodesObl.size()-1);
            newQuantityDbl= this.quantityScannedDbl - pvAmountDbl;
        }

        if (newQuantityDbl <= 0) {
            this.quantityScannedDbl = (double) 0;
        }else {
            //Set the new quantityDbl and show in Activity
            this.quantityScannedDbl = newQuantityDbl;
        }

        this.quantityText.setText(cText.pDoubleToStringStr(this.quantityScannedDbl));
        this.imageButtonDone.setImageResource(R.drawable.ic_check_black_24dp);
        return  true;
    }

    private  void mBarcodeSelected(cIntakeorderBarcode pvBarcode) {

        cUserInterface.pCheckAndCloseOpenDialogs();

        cIntakeorderBarcode.currentIntakeOrderBarcode = pvBarcode;
        this.mShowBarcodeInfo();

        this.mTryToChangeQuantityBln(true, false, cIntakeorderBarcode.currentIntakeOrderBarcode.getQuantityPerUnitOfMeasureDbl());
    }

    // Lines, Barcodes

    private  void mCheckLineDone() {

        //Start with complete
        this.imageButtonDone.setVisibility(View.VISIBLE);

        //Check if quantityDbl is sufficient
        if (!cReceiveorderSummaryLine.currentReceiveorderSummaryLine.pQuantityReachedBln(this.scannedBarcodesObl)) {
            this.imageButtonDone.setVisibility(View.VISIBLE);
            this.imageButtonDone.setImageResource(R.drawable.ic_check_black_24dp);
            return;
        }

        //We are complete
        this.imageButtonDone.setVisibility(View.VISIBLE);
        this.imageButtonDone.setImageResource(R.drawable.ic_doublecheck_black_24dp);

    }

    private  boolean mSendScansBln() {

        //If internet is not connected
        if (!cConnection.isInternetConnectedBln()) {
            //could not send line, let user know but answer succes so user can go to next line
            cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.couldnt_send_line_no_connection), null);
            return false;
        }

        List<cIntakeorderBarcode> sortedBarcodeList = this.mSortBarcodeList(this.scannedBarcodesObl);

       if(!cReceiveorderSummaryLine.currentReceiveorderSummaryLine.pItemVariantHandledBln(sortedBarcodeList)) {
            cUserInterface.pDoExplodingScreen(cAppExtension.context.getString(R.string.couldnt_send_line), "",true,true);
            return false;
        }

  return  true;
    }

    private  void mReceiveDone() {

    if (this.mSendScansBln()) {
        this.mResetCurrents();
        this.mGoBackToLinesActivity();
    }

     }

     //ScanActions
     private  void mFillRecycler(List<cReceiveorderLine> pvDataObl) {
         this.getReceiveorderLineAdapter().pFillData(pvDataObl);
         this.recyclerScanActions.setHasFixedSize(false);
         this.recyclerScanActions.setAdapter(this.getReceiveorderLineAdapter());
         this.recyclerScanActions.setLayoutManager(new LinearLayoutManager(cAppExtension.context));
     }

    //Listeners
    private void mSetDoneListener() {
        this.imageButtonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               pAcceptReceive();
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

        this.imageButtonPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //There is no selected barcodeStr, select one first
                if (cIntakeorderBarcode.currentIntakeOrderBarcode == null) {
                    cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.message_select_one_of_multiple_barcodes),null);
                    return;
                }
                mTryToChangeQuantityBln(true, false, cIntakeorderBarcode.currentIntakeOrderBarcode.getQuantityPerUnitOfMeasureDbl());
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

                //There is no selected barcodeStr, select one first
                if (cIntakeorderBarcode.currentIntakeOrderBarcode == null) {
                    cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.message_select_one_of_multiple_barcodes),null);
                    return;
                }


                mTryToChangeQuantityBln(false, false, cIntakeorderBarcode.currentIntakeOrderBarcode.getQuantityPerUnitOfMeasureDbl());
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
    }

    //Dialogs and Activitys

    private  void mShowFullArticleFragment() {

        cUserInterface.pCheckAndCloseOpenDialogs();

        ArticleFullViewFragment articleFullViewFragment = new ArticleFullViewFragment();
        articleFullViewFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.ARTICLEFULL_TAG);



    }

    private void mShowAcceptFragment(){

        cUserInterface.pCheckAndCloseOpenDialogs();

        this.amountExceededDialogShowedBln = false;


        String acceptStr = cAppExtension.activity.getString(R.string.message_accept_line);
        String rejectStr = cAppExtension.activity.getString(R.string.message_cancel_line);

        if (BuildConfig.FLAVOR.toUpperCase().equalsIgnoreCase("BMN")) {
            acceptStr =  cAppExtension.activity.getString(R.string.message_yes);
            rejectStr = cAppExtension.activity.getString(R.string.message_no);
        }

        final AcceptRejectFragment acceptRejectFragment = new AcceptRejectFragment(cAppExtension.activity.getString(R.string.message_orderlinebusy_header),
                                                                                   cAppExtension.activity.getString(R.string.message_orderlinebusy_text),
                                                                                   rejectStr, acceptStr, false);
        acceptRejectFragment.setCancelable(true);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // show my popup
                acceptRejectFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.ACCEPTREJECTFRAGMENT_TAG);
            }
        });
    }

    private  void mShowExtraPiecesPercentageExceededFragment(Double pvValueDbl){

        cUserInterface.pCheckAndCloseOpenDialogs();

        this.amountExceededDialogShowedBln = true;

        String acceptStr = cAppExtension.activity.getString(R.string.message_accept_line);
        String rejectStr = cAppExtension.activity.getString(R.string.message_cancel_line);

        if (BuildConfig.FLAVOR.toUpperCase().equalsIgnoreCase("BMN")) {
            acceptStr =  cAppExtension.activity.getString(R.string.message_yes);
            rejectStr = cAppExtension.activity.getString(R.string.message_no);
        }

        final AcceptRejectFragment acceptRejectFragment = new AcceptRejectFragment(cAppExtension.activity.getString(R.string.message_extra_pieces_exceeded_header),
                cAppExtension.context.getString(R.string.number_received_total_eminently_more, cText.pDoubleToStringStr(pvValueDbl), cText.pDoubleToStringStr(cReceiveorderSummaryLine.currentReceiveorderSummaryLine.getQuantityDbl())),
                rejectStr, acceptStr, false);
        acceptRejectFragment.setCancelable(true);

        cAppExtension.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // show my popup
                acceptRejectFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.ACCEPTREJECTFRAGMENT_TAG);
            }
        });
    }

    private  void mShowExtraPiecesNotAllowed(){
        cUserInterface.pHideKeyboard();
        this.quantityText.setText(cText.pDoubleToStringStr(cReceiveorderSummaryLine.currentReceiveorderSummaryLine.getQuantityDbl()));
        cUserInterface.pShowSnackbarMessage(toolbarImage , cAppExtension.context.getString(R.string.number_cannot_be_higher), null, false);
        cUserInterface.pDoNope(quantityText, true, true);
        cUserInterface.pDoNope(quantityText, false, false);
    }

    private  void mShowExtraPiecesNotAllowedByPercentage(Double pvValueDbl){

            cUserInterface.pShowSnackbarMessage(toolbarImage , cAppExtension.context.getString(R.string.number_received_total_cant_be_higher_then, cText.pDoubleToStringStr(pvValueDbl)) , null, false);
            cUserInterface.pDoNope(quantityText, true, true);
            cUserInterface.pDoNope(quantityText, false, false);

    }

    private  void mGoBackToLinesActivity() {
        Intent intent = new Intent(cAppExtension.context, ReceiveLinesActivity.class);
        cAppExtension.activity.startActivity(intent);
        cAppExtension.activity.finish();
    }

    private  void mResetCurrents() {

        cReceiveorderSummaryLine.currentReceiveorderSummaryLine = null;
        cReceiveorderLine.currentReceiveorderLine = null;
        cIntakeorder.currentIntakeOrder.intakeorderBarcodeScanned = null;
        cIntakeorderBarcode.currentIntakeOrderBarcode = null;
        this.scannedBarcodesObl = null;
        this.quantityScannedDbl = 0.0;

    }

    private void mShowBarcodeSelectFragment() {
        BarcodeFragment barcodeFragment = new BarcodeFragment();
        barcodeFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.BARCODEFRAGMENT_TAG);
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

        numberpickerFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.NUMBERFRAGMENT_TAG);
    }

    private  void mSetArticleInfo(){

        if ( cReceiveorderSummaryLine.currentReceiveorderSummaryLine == null) {
            this.articleDescriptionText.setText("???");
            this.articleDescription2Text.setText("???");
            this.articleItemText.setText("???");
            return;
        }
            this.articleDescriptionText.setText(cReceiveorderSummaryLine.currentReceiveorderSummaryLine.getDescriptionStr());
            this.articleDescription2Text.setText(cReceiveorderSummaryLine.currentReceiveorderSummaryLine.getDescription2Str());
            this.articleItemText.setText(cReceiveorderSummaryLine.currentReceiveorderSummaryLine.getItemNoAndVariantCodeStr());
    }

    private  void mSetQuantityInfo(){
        this.quantityText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        this.quantityText.setSelectAllOnFocus(true);
        this.quantityText.setSingleLine();
        this.quantityText.requestFocus();
        this.quantityText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_TEXT_VARIATION_NORMAL);
        this.quantityText.setCursorVisible(false);

        TextWatcher tw = new cNumberTextWatcher(this.quantityText, 2,999999d);
        this.quantityText.addTextChangedListener((tw));

        if ( cReceiveorderSummaryLine.currentReceiveorderSummaryLine== null) {
            this.quantityText.setText("0");
            this.quantityRequiredText.setText("0");
            return;
        }

        this.quantityText.setText("0");

        if (!cIntakeorder.currentIntakeOrder.isGenerated()) {
            this.quantityRequiredText.setVisibility(View.VISIBLE);
            this.quantityRequiredText.setText(cText.pDoubleToStringStr(cReceiveorderSummaryLine.currentReceiveorderSummaryLine.pGetQuantityToHandleDbl()));

            if (cReceiveorderSummaryLine.currentReceiveorderSummaryLine.pGetQuantityToHandleDbl() == 0) {
                this.quantityRequiredText.setVisibility(View.GONE);
            }

        }
        else {
            this.quantityRequiredText.setVisibility(View.GONE);
        }

    }

    private  void mSetSourceNoInfo(){
        this.sourcenoText.setText( cIntakeorder.currentIntakeOrder.getOrderNumberStr());
    }

    private  void mSetBinInfo(){
        this.binCodeText.setText( cReceiveorderSummaryLine.currentReceiveorderSummaryLine.getBinCodeStr());
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
    }

    private void mDoDelayedPlus(Runnable pvRunnable, long pvMilliSecsLng) {
        this.plusHandler.postDelayed(pvRunnable, pvMilliSecsLng);
        this.counterPlusHelperInt += 1;
    }

    private  void mRemoveAdapterFromFragment(){

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
        this.mFieldsInitialize();

    }

    private  List<cIntakeorderBarcode> mSortBarcodeList(List<cIntakeorderBarcode> pvUnsortedBarcodeObl) {
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

        this.quantityText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //There is no selected barcodeStr, select one first
                if (cIntakeorderBarcode.currentIntakeOrderBarcode == null) {
                    cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.message_select_one_of_multiple_barcodes),null);
                    cUserInterface.pHideKeyboard();
                    return;
                }

                quantityText.requestFocus();
                quantityText.setSelection(0,quantityText.getText().toString().length());
            }
        });


        this.quantityText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE || i == EditorInfo.IME_ACTION_GO ) {

                if(mTryToChangeQuantityBln(true,
                        true,
                        cText.pStringToDoubleDbl(String.valueOf(quantityText.getText()))))  {
                    imageButtonDone.callOnClick();
                }
                }
                return true;
            }
        });
    }

    //End Region Number Broadcaster

    //End Regin Private Methods

}

