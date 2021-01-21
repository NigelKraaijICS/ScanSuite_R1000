package nl.icsvertex.scansuite.Activities.Returns;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
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
import androidx.lifecycle.ViewModelProvider;

import java.util.Objects;

import ICS.Interfaces.iICSDefaultActivity;
import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cRegex;
import ICS.Utils.cText;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Basics.BarcodeLayouts.cBarcodeLayout;
import SSU_WHS.Basics.BranchReason.cBranchReason;
import SSU_WHS.Basics.Settings.cSetting;
import SSU_WHS.Basics.Users.cUser;
import SSU_WHS.General.cPublicDefinitions;
import SSU_WHS.Return.ReturnOrder.cReturnorder;
import SSU_WHS.Return.ReturnorderBarcode.cReturnorderBarcode;
import SSU_WHS.Return.ReturnorderDocument.cReturnorderDocument;
import SSU_WHS.Return.ReturnorderLine.cReturnorderLine;
import SSU_WHS.Return.ReturnorderLine.cReturnorderLineViewModel;
import SSU_WHS.Return.ReturnorderLineBarcode.cReturnorderLineBarcode;
import nl.icsvertex.scansuite.Fragments.Dialogs.AcceptRejectFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.ArticleFullViewFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.BarcodeFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.ReasonFragment;
import nl.icsvertex.scansuite.R;

public class ReturnArticleDetailActivity extends AppCompatActivity implements iICSDefaultActivity {


    //Region Private Properties

    private ConstraintLayout returnArticleDetailContainer;

    private int returnCounterMinusHelperInt;
    private int returnCounterPlusHelperInt;
    private Handler minusHandler;
    private Handler plusHandler;

    private Toolbar toolbar;
    private ImageView toolbarImage;
    private TextView toolbarTitle;
    private TextView toolbarSubtext;

    private CardView articleContainer;
    private ConstraintLayout articleInfoContainer;

    private ImageView articleThumbImageView;
    private TextView articleDescriptionText;
    private TextView articleDescription2Text;
    private TextView articleItemText;
    private TextView articleBarcodeText;

    private TextView binText;
    private EditText quantityText;

    private TextView reasonText;
    private ImageButton imageButtonReason;

    private AppCompatImageButton imageButtonMinus;
    private AppCompatImageButton imageButtonPlus;
    private ImageView imageButtonDone;

    private ImageView imageButtonNoInputPropertys;
    private ImageButton imageButtonBarcode;

    private cReturnorderLineViewModel getReturnorderLineViewModel() {
        return new ViewModelProvider(cAppExtension.fragmentActivity).get(cReturnorderLineViewModel.class);
    }

    //End Region Private Properties

    //Region Constructor
    public ReturnArticleDetailActivity() {

    }

    //End Region Constructor

    //Region Default Methods

    @Override
    protected void onCreate(Bundle pvSavedInstanceState) {
        super.onCreate(pvSavedInstanceState);
        setContentView(R.layout.activity_returnarticle_detail);
        this.mActivityInitialize();

    }

    @Override
    public void onPause() {
        super.onPause();
        cBarcodeScan.pUnregisterBarcodeReceiver(this.getClass().getSimpleName());
    }

    @Override
    public void onResume() {
        super.onResume();
        cBarcodeScan.pRegisterBarcodeReceiver(this.getClass().getSimpleName());
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

    //Region iICSDefaultActivity defaults

    @Override
    public void mActivityInitialize() {
        this.mSetAppExtensions();
        this.mFindViews();
        this.mSetToolbar(getResources().getString(R.string.screentitle_return));
        this.mFieldsInitialize();
        this.mSetListeners();
        this.mInitScreen();

        cBarcodeScan.pRegisterBarcodeReceiver(this.getClass().getSimpleName());
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

        this.returnArticleDetailContainer = findViewById(R.id.returnArticleDetailContainer);

        this.toolbar = findViewById(R.id.toolbar);
        this.toolbarImage = findViewById(R.id.toolbarImage);
        this.toolbarTitle = findViewById(R.id.toolbarTitle);
        this.toolbarSubtext = findViewById(R.id.toolbarSubtext);

        this.articleContainer = findViewById(R.id.articleContainer);
        this.articleInfoContainer = findViewById(R.id.articleInfoContainer);

        this.articleThumbImageView = findViewById(R.id.articleThumbImageView);
        this.binText = findViewById(R.id.binText);
        this.quantityText = findViewById(R.id.quantityText);
        this.imageButtonMinus = findViewById(R.id.imageButtonMinus);
        this.imageButtonPlus = findViewById(R.id.imageButtonPlus);
        this.imageButtonBarcode = findViewById(R.id.imageButtonBarcode);

        this.reasonText = findViewById(R.id.reasonText);
        this.reasonText.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        this.reasonText.setSingleLine(true);
        this.reasonText.setMarqueeRepeatLimit(5);
        this.reasonText.setSelected(true);
        this.imageButtonReason = findViewById(R.id.imageButtonReason);

        this.articleDescriptionText = findViewById(R.id.articleDescriptionText);
        this.articleDescription2Text = findViewById(R.id.articleDescription2Text);
        this.articleItemText = findViewById(R.id.articleItemText);
        this.articleBarcodeText = findViewById(R.id.articleBarcodeText);
        this.imageButtonDone = findViewById(R.id.imageButtonDone);
        this.imageButtonNoInputPropertys = findViewById(R.id.imageButtonNoInputPropertys);
    }

    @Override
    public void mSetToolbar(String pvScreenTitle) {
        this.toolbarImage.setImageResource(R.drawable.ic_menu_return);
        this.toolbarTitle.setText(pvScreenTitle);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        this.toolbarSubtext.setText(cReturnorderDocument.currentReturnOrderDocument.getSourceDocumentStr());

    }

    @Override
    public void mFieldsInitialize() {

        if (!cSetting.RETOUR_AMOUNT_MANUAL()) {
            this.imageButtonBarcode.setVisibility(View.GONE);
            this.imageButtonMinus.setVisibility(View.INVISIBLE);
            this.imageButtonPlus.setVisibility(View.INVISIBLE);
        } else {
            this.imageButtonBarcode.setVisibility(View.VISIBLE);
            this.imageButtonMinus.setVisibility(View.VISIBLE);
            this.imageButtonPlus.setVisibility(View.VISIBLE);
        }

        this.returnCounterPlusHelperInt = 0;
        this.returnCounterMinusHelperInt = 0;

        this.articleDescriptionText.setText(cReturnorderLine.currentReturnOrderLine.getDescriptionStr());
        this.articleDescription2Text.setText(cReturnorderLine.currentReturnOrderLine.getDescription2Str());
        this.articleItemText.setText(cReturnorderLine.currentReturnOrderLine.getItemNoAndVariantCodeStr());

        if (cReturnorderBarcode.currentReturnOrderBarcode != null) {
            this.articleBarcodeText.setText(cReturnorderBarcode.currentReturnOrderBarcode.getBarcodeAndQuantityAndUnitOfMeasureStr());
        } else {
            this.articleBarcodeText.setText(cAppExtension.context.getString(R.string.message_unknown_barcode));
        }

        if (!cReturnorderLine.currentReturnOrderLine.isGeneratedBln()){
            cBranchReason.currentBranchReason = cUser.currentUser.currentBranch.pGetReasonByName(cReturnorderLine.currentReturnOrderLine.getRetourRedenStr());
        }
        else {
            if (cBranchReason.currentBranchReason != null){
                cReturnorderLine.currentReturnOrderLine.retourRedenStr = cBranchReason.currentBranchReason.getReasonStr();
            }
        }
        if (cBranchReason.currentBranchReason != null){
            this.reasonText.setText(cBranchReason.currentBranchReason.getDescriptionStr());
        }

        this.binText.setText(cReturnorder.currentReturnOrder.getBinCodeStr());

        this.quantityText.setSelectAllOnFocus(true);
        this.quantityText.requestFocus();
        if (cReturnorderLine.currentReturnOrderLine.getQuantitytakeDbl() == 0.0 ){
            this.quantityText.setText(cText.pDoubleToStringStr(cReturnorderLine.currentReturnOrderLine.getQuantityHandledTakeDbl()));
        }
        else {
            this.quantityText.setText(cReturnorderLine.currentReturnOrderLine.getQuantityToShowInAdapterStr());
        }

        this.quantityText.setInputType(InputType.TYPE_TEXT_VARIATION_NORMAL);
        this.quantityText.setCursorVisible(false);

        this.mShowArticleImage();

        this.articleInfoContainer.setVisibility(View.GONE);
        this.imageButtonNoInputPropertys.setVisibility(View.GONE);
    }

    @Override
    public void mSetListeners() {

        this.mSetArticleImageListener();
        this.mSetDoneListener();
        this.mSetReasonListener();

        if (cSetting.RETOUR_AMOUNT_MANUAL()) {
            this.mSetPlusListener();
            this.mSetMinusListener();
            this.mSetEditorActionListener();
            this.mSetImageButtonBarcodeListener();
        }
    }

    @Override
    public void mInitScreen() {

    }

    //End Region iICSDefaultActivity defaults


    //Region Public Methods

    public  void pHandleScan(cBarcodeScan pvBarcodeScan){

        String barcodeWithoutPrefixStr = cRegex.pStripRegexPrefixStr(pvBarcodeScan.getBarcodeOriginalStr());
        boolean newReasonAllowedBln;

        //Check if REASON scans are allowed
        if (!cReturnorderLine.currentReturnOrderLine.isGeneratedBln() && !cReturnorderLine.currentReturnOrderLine.getRetourRedenStr().isEmpty()) {
            newReasonAllowedBln = false;
        }
        else {
            newReasonAllowedBln = cReturnorderLine.currentReturnOrderLine.nieuweRegelBln;
        }

        //We scan 3 diffent things here
        // Article
        // Reason
        // Document
        boolean regexDecidedBln = false;
        boolean isArticleBln = false;
        boolean isReasonBln = false;
        boolean isDocumentBln = false;

        //If we have no match, we are done here
        if (Objects.requireNonNull(cBarcodeLayout.pGetBarcodeLayoutByBarcodeObl(pvBarcodeScan.getBarcodeOriginalStr())).size() == 0) {
            cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.message_unknown_barcode), null);
            return;
        }

        if (Objects.requireNonNull(cBarcodeLayout.pGetBarcodeLayoutByBarcodeObl(pvBarcodeScan.getBarcodeOriginalStr())).size() == 1) {

            if (cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(),cBarcodeLayout.barcodeLayoutEnu.ARTICLE)) {
                isArticleBln = true;
            }

            if (cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(),cBarcodeLayout.barcodeLayoutEnu.REASON)) {
                isReasonBln = true;

            }

            if (cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(),cBarcodeLayout.barcodeLayoutEnu.DOCUMENT)) {
                isDocumentBln = true;
            }
        }

        //We have multiple possible matches
        else
        {
            if (cBarcodeLayout.pCheckBarcodeWithLayoutPrefixBln(pvBarcodeScan.getBarcodeOriginalStr(),cBarcodeLayout.barcodeLayoutEnu.REASON)) {
                isReasonBln = true;
                regexDecidedBln = true;
            }

            if (cBarcodeLayout.pCheckBarcodeWithLayoutPrefixBln(pvBarcodeScan.getBarcodeOriginalStr(),cBarcodeLayout.barcodeLayoutEnu.DOCUMENT)) {
                isDocumentBln = true;
                regexDecidedBln = true;
            }

            if (!regexDecidedBln) {
                isArticleBln = true;
            }
        }

        if (isReasonBln && !newReasonAllowedBln) {
            cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.message_reason_scan_not_allowed), null);
            ReturnorderDocumentActivity.busyBln = false;
            return;
        }


        //Try to set the reason
        if (isReasonBln) {
            cBranchReason branchReason = cUser.currentUser.currentBranch.pGetReasonByName(barcodeWithoutPrefixStr);
            if (branchReason != null) {
                cBranchReason.currentBranchReason = branchReason;
                this.pSetReason();
            }
            else {
                cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.message_reason_unknown), null);
            }
            return;
        }

        if (isArticleBln) {

            if (!this.mCheckBarcodeWithLineBarcodesBln(pvBarcodeScan)) {

                if (!cReturnorder.currentReturnOrder.isGeneratedBln()) {
                    cUserInterface.pDoExplodingScreen(cAppExtension.activity.getString(R.string.message_unknown_barcode_for_this_line), pvBarcodeScan.getBarcodeOriginalStr(),true,true);
                    return;
                }


                if (cReturnorderLine.currentReturnOrderLine.getRetourRedenStr().isEmpty()) {
                    cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.message_scan_reason_first), null);
                    return;
                }

                //Save scan in current document,
                cReturnorderDocument.currentReturnOrderDocument.barcodeScanToHandle = pvBarcodeScan;

                //Close this activity, we are done here
                this.mHandleDone();
                return;
            }

            //Try to raise quantityDbl
            this.mTryToChangeReturnorderQuantity(true, false,cReturnorderBarcode.currentReturnOrderBarcode.getQuantityPerUnitOfMeasureDbl() );
            return;
        }

        if (isDocumentBln) {


            if (cReturnorderDocument.currentReturnOrderDocument.getSourceDocumentStr().equalsIgnoreCase(barcodeWithoutPrefixStr)) {
                cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.message_document_already_active), null);
                return;
            }

            if (cReturnorderLine.currentReturnOrderLine.getRetourRedenStr().isEmpty()) {
                cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.message_scan_reason_first), null);
                return;
            }

            //Close this fragment, we are done here
            this.mHandleDone();

            //Handle scan in BIN activity
            if (cAppExtension.activity instanceof ReturnorderDocumentActivity ) {
                ReturnorderDocumentActivity returnorderDocumentActivity = (ReturnorderDocumentActivity)cAppExtension.activity;
                returnorderDocumentActivity.pHandleScan(pvBarcodeScan);
            }

            return;
        }


        //You scanned somethin unknown
        cUserInterface.pDoExplodingScreen(cAppExtension.activity.getString(R.string.message_unknown_barcode),pvBarcodeScan.getBarcodeOriginalStr(),true,true);

    }

    public void pSetReason(){
        this.reasonText.setText(cBranchReason.currentBranchReason.getDescriptionStr());
        cReturnorderLine.currentReturnOrderLine.retourRedenStr = cBranchReason.currentBranchReason.getReasonStr();
    }

    public void pHandleFragmentDismissed(){
        cBarcodeScan.pRegisterBarcodeReceiver(this.getClass().getSimpleName());
        cReturnorderLine.currentReturnOrderLine.quantityHandledTakeDbl = (double) 0;
        this.mGoBackToDocumentActivity();
    }

    public  void pDone(){
        mHandleDone();
    }

    //End Region Public Methods

    //Region Private Methods

    private boolean mCheckBarcodeWithLineBarcodesBln(cBarcodeScan pvBarcodeScan){

        //If scanned value matches the current barcodeStr, then we have a match
        if (pvBarcodeScan.getBarcodeOriginalStr().equalsIgnoreCase(cReturnorderLineBarcode.currentreturnorderLineBarcode.getBarcodeStr()) ||
                pvBarcodeScan.getBarcodeFormattedStr().equalsIgnoreCase(cReturnorderBarcode.currentReturnOrderBarcode.getBarcodeStr()) ) {
            //We have a match, so leave
            return  true;
        }

        //Check if this is a barcodeStr we already know
        cReturnorderBarcode returnorderBarcode = cReturnorder.currentReturnOrder.pGetOrderBarcode(pvBarcodeScan);

        //We scanned a barcodeStr unknown to the order
        if (returnorderBarcode == null) {
            return false;
        }

        //We scanned a barcodeStr for a different article
        if (!returnorderBarcode.getItemNoStr().equalsIgnoreCase(cReturnorderBarcode.currentReturnOrderBarcode.getItemNoStr()) ||
                ! returnorderBarcode.getVariantCodeStr().equalsIgnoreCase(cReturnorderBarcode.currentReturnOrderBarcode.getVariantCodeStr())) {
            return false;
        }

        //We scanned a barcodeStr that belongs to the current article, so check if we already have a line barcodeStr
        for (cReturnorderLineBarcode returnorderLineBarcode : cReturnorderLine.currentReturnOrderLine.lineBarcodesObl) {

            //We have a match, so set
            if (returnorderLineBarcode.getBarcodeStr().equalsIgnoreCase(pvBarcodeScan.getBarcodeOriginalStr()) ||
                    returnorderLineBarcode.getBarcodeStr().equalsIgnoreCase(pvBarcodeScan.getBarcodeFormattedStr())) {
                cReturnorderLineBarcode.currentreturnorderLineBarcode = returnorderLineBarcode;
                cReturnorderBarcode.currentReturnOrderBarcode = returnorderBarcode;
                return true;
            }
        }
        return  false;
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
                    returnCounterPlusHelperInt = 0;
                }

                return false;
            }
        });

        this.imageButtonPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //There is no selected barcodeStr, select one first
                if (cReturnorderBarcode.currentReturnOrderBarcode == null) {
                    cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.message_select_one_of_multiple_barcodes),null);
                    return;
                }

                mTryToChangeReturnorderQuantity(true, false, cReturnorderBarcode.currentReturnOrderBarcode.getQuantityPerUnitOfMeasureDbl());
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
                    returnCounterMinusHelperInt = 0;
                }
                return false;
            }

        });

        this.imageButtonMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //There is no selected barcodeStr, select one first
                if (cReturnorderBarcode.currentReturnOrderBarcode == null) {
                    cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.message_select_one_of_multiple_barcodes),null);
                    return;
                }
                mTryToChangeReturnorderQuantity(false, false, cReturnorderBarcode.currentReturnOrderBarcode.getQuantityPerUnitOfMeasureDbl());
            }
        });
    }

    private void mSetEditorActionListener() {


        this.quantityText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantityText.requestFocus();
                quantityText.setSelection(0, quantityText.getText().toString().length());
            }
        });


        quantityText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE || i == EditorInfo.IME_ACTION_GO ) {
                    mTryToChangeReturnorderQuantity(true,
                            true,
                            cReturnorderLine.currentReturnOrderLine.getQuantityHandledTakeDbl());

                    imageButtonDone.callOnClick();
                }
                return true;
            }
        });
    }

    private void mSetDoneListener(){

        this.imageButtonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mHandleDone();
            }
        });

    }

    private void mSetReasonListener(){
        this.imageButtonReason.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (cReturnorderLine.currentReturnOrderLine.nieuweRegelBln) {
                    mShowAddReasonFragment();
                }
            }
        });
    }

    private void mDoDelayedMinus(Runnable pvRunnable, long pvMilliSecsLng) {
        this.minusHandler.postDelayed(pvRunnable, pvMilliSecsLng);
        this.returnCounterMinusHelperInt += 1;

    }

    private void mDoDelayedPlus(Runnable pvRunnable, long pvMilliSecsLng) {
        this.plusHandler.postDelayed(pvRunnable, pvMilliSecsLng);
        this.returnCounterPlusHelperInt += 1;
    }

    private  void mTryToChangeReturnorderQuantity(Boolean pvIsPositiveBln, Boolean pvAmountFixedBln, double pvAmountDbl) {

        String newQuantityStr;
        double newQuantityDbl;

        if (pvIsPositiveBln) {

            //Determine the new amount
            if (pvAmountFixedBln) {
                newQuantityDbl = cReturnorderLine.currentReturnOrderLine.quantityHandledTakeDbl = pvAmountDbl;
            } else {
                newQuantityDbl = cReturnorderLine.currentReturnOrderLine.quantityHandledTakeDbl + pvAmountDbl;
            }

            //Check if we would exceed amount, then show message
            if (newQuantityDbl > cReturnorderLine.currentReturnOrderLine.getQuantitytakeDbl() && !cReturnorder.currentReturnOrder.isGeneratedBln() ) {
                this.mShowExtrasNotAllowed();
                return;
            }

            cReturnorderLine.currentReturnOrderLine.quantityHandledTakeDbl += pvAmountDbl;
            cReturnorderLineBarcode.currentreturnorderLineBarcode.quantityHandledDbl += pvAmountDbl;

            //Change quantityDbl in activuty
            if (cReturnorderLine.currentReturnOrderLine.getQuantitytakeDbl() == 0.0 ){
                newQuantityStr = cText.pDoubleToStringStr(cReturnorderLine.currentReturnOrderLine.getQuantityHandledTakeDbl());
            }
            else {
                newQuantityStr = cText.pDoubleToStringStr(cReturnorderLine.currentReturnOrderLine.getQuantityHandledTakeDbl()) + "/" + cText.pDoubleToStringStr(cReturnorderLine.currentReturnOrderLine.getQuantitytakeDbl());
            }
            this.quantityText.setText(newQuantityStr);
            return;
        }

        //negative

        //Check if value already is zero
        if (cReturnorderLine.currentReturnOrderLine.getQuantityHandledTakeDbl() < 1) {

            //If we have a decimal, correct it to zero
            if (cReturnorderLine.currentReturnOrderLine.getQuantityHandledTakeDbl() > 0) {
                pvAmountDbl = 0;
                pvAmountFixedBln = true;

            } else {
                cUserInterface.pDoNope(this.quantityText, true, true);
                return;
            }

        }
        if (pvAmountDbl <= 0) {
            cUserInterface.pDoNope(this.quantityText, true, true);
            return;
        }

        //Determine the new amount
        if (pvAmountFixedBln) {
            cReturnorderLine.currentReturnOrderLine.quantityHandledTakeDbl = pvAmountDbl;
            cReturnorderLineBarcode.currentreturnorderLineBarcode.quantityHandledDbl = pvAmountDbl;
        } else {

            newQuantityDbl =  cReturnorderLine.currentReturnOrderLine.quantityHandledTakeDbl - pvAmountDbl;

            if (newQuantityDbl == 0 ) {
                cUserInterface.pDoNope(this.quantityText, true, true);
                return;
            }

            cReturnorderLine.currentReturnOrderLine.quantityHandledTakeDbl -= pvAmountDbl;
            cReturnorderLineBarcode.currentreturnorderLineBarcode.quantityHandledDbl -= pvAmountDbl;


            if (cReturnorderLine.currentReturnOrderLine.getQuantitytakeDbl() == 0.0 ){
                newQuantityStr = cText.pDoubleToStringStr(cReturnorderLine.currentReturnOrderLine.getQuantityHandledTakeDbl());
            }
            else {
                newQuantityStr = cText.pDoubleToStringStr(cReturnorderLine.currentReturnOrderLine.getQuantityHandledTakeDbl()) + "/" + cText.pDoubleToStringStr(cReturnorderLine.currentReturnOrderLine.getQuantitytakeDbl());
            }
            this.quantityText.setText(newQuantityStr);

        }
    }

    private  void mShowArticleImage() {


        //If pick with picture is false, then hide image view
        if (!cReturnorder.currentReturnOrder.isReturnWithPictureBln()) {
            this.articleThumbImageView.setVisibility(View.GONE);
            return;
        }

        this.articleThumbImageView.setVisibility(View.VISIBLE);
        this.articleThumbImageView.setImageDrawable(ContextCompat.getDrawable(cAppExtension.context, R.drawable.ic_no_image_lightgrey_24dp));


        //If picture is not in cache (via webservice) then show no image
        if (!cReturnorderLine.currentReturnOrderLine.pGetArticleImageBln()) {
            cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.could_not_get_article_image), null);
            this.articleThumbImageView.setImageDrawable(ContextCompat.getDrawable(cAppExtension.context, R.drawable.ic_no_image_lightgrey_24dp));
            return;
        }

        //If picture is in cache but can't be converted, then show no image
        if (cReturnorderLine.currentReturnOrderLine.articleImage == null || cReturnorderLine.currentReturnOrderLine.articleImage.imageBitmap() == null) {
            cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.could_not_get_article_image), null);
            this.articleThumbImageView.setImageDrawable(ContextCompat.getDrawable(cAppExtension.context, R.drawable.ic_no_image_lightgrey_24dp));
            return;
        }

        //Show the image
        this.articleThumbImageView.setImageBitmap(cReturnorderLine.currentReturnOrderLine.articleImage.imageBitmap());

    }

    private void mHandleDone() {

        if (cBranchReason.currentBranchReason == null){
            cUserInterface.pDoNope(imageButtonReason, true, true);
            cUserInterface.pShowToastMessage(cAppExtension.activity.getString(R.string.choose_reason), null);
            return;
        }
        cReturnorderLine.currentReturnOrderLine.retourRedenStr = cBranchReason.currentBranchReason.getReasonStr();

        //Try to match item, variantcode and reason with previous lines so we can show 1 line in recycler
        cReturnorderLine.pCheckIfLineIsAlreadyInUse();


        //Try to save the line to the database
        if (cReturnorderLine.currentReturnOrderLine.isGeneratedBln() && cReturnorderLine.currentReturnOrderLine.nieuweRegelBln){
            this.getReturnorderLineViewModel().pCreateItemVariantViaWebservice();
            cReturnorderLine.currentReturnOrderLine.nieuweRegelBln = false;
            cReturnorderDocument.currentReturnOrderDocument.returnorderLineObl.add(cReturnorderLine.currentReturnOrderLine);

        }

        if (!cReturnorderLine.currentReturnOrderLine.pSaveLineViaWebserviceBln()) {
            cUserInterface.pDoExplodingScreen(cAppExtension.activity.getString(R.string.message_line_save_failed),"",true,true);

            if (cAppExtension.activity instanceof  ReturnorderDocumentActivity) {
                ReturnorderDocumentActivity returnorderDocumentActivity = (ReturnorderDocumentActivity)cAppExtension.activity;
                returnorderDocumentActivity.pHandleFragmentDismissed();
            }

            cAppExtension.dialogFragment.dismiss();
            return;
        }

        //Change quantityDbl handled in database
        cReturnorderLine.currentReturnOrderLine.pUpdateQuantityInDatabase();
        this.mLineHandled();
        cUserInterface.pHideGettingData();
        this.mGoBackToDocumentActivity();
    }

    private void mLineHandled() {

        //We returned here, so we are not busy anymore
        ReturnorderDocumentActivity.busyBln = false;

        //Reset currents
        cReturnorderLine.currentReturnOrderLine = null;
        cReturnorderBarcode.currentReturnOrderBarcode = null;
        cReturnorderLineBarcode.currentreturnorderLineBarcode = null;
        cBranchReason.currentBranchReason = null;

    }

    private void mHideArticleInfo(){

        this.articleInfoContainer.setVisibility(View.GONE);
        ConstraintLayout.LayoutParams newCardViewLayoutParams = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        newCardViewLayoutParams.setMargins(15,15,15,15);
        this.articleContainer.setLayoutParams(newCardViewLayoutParams);

        ConstraintSet constraintSetSpace = new ConstraintSet();
        constraintSetSpace.clone(this.returnArticleDetailContainer);
        constraintSetSpace.connect(this.articleContainer.getId(), ConstraintSet.TOP, toolbar.getId(), ConstraintSet.BOTTOM);
        constraintSetSpace.applyTo(this.returnArticleDetailContainer);

    }


    private Runnable mMinusAction = new Runnable() {
        @Override
        public void run() {
            imageButtonMinus.performClick();
            long milliSecsLng;
            if (returnCounterMinusHelperInt < 10) {
                milliSecsLng = 200;
            } else if (returnCounterMinusHelperInt < 20) {
                milliSecsLng = 150;
            } else if (returnCounterMinusHelperInt < 30) {
                milliSecsLng = 100;
            } else if (returnCounterMinusHelperInt < 40) {
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
            if (returnCounterPlusHelperInt < 10) {
                milliSecsLng = 200;
            } else if (returnCounterPlusHelperInt < 20) {
                milliSecsLng = 150;
            } else if (returnCounterPlusHelperInt < 30) {
                milliSecsLng = 100;
            } else if (returnCounterPlusHelperInt < 40) {
                milliSecsLng = 50;
            } else {
                milliSecsLng = 50;
            }
            mDoDelayedPlus(this, milliSecsLng);
        }
    };

    private void mShowAddReasonFragment(){
        ReasonFragment reasonFragment = new ReasonFragment();
        reasonFragment.setCancelable(true);
        reasonFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.ADDREASON_TAG);

    }

    private void mSetImageButtonBarcodeListener() {
        this.imageButtonBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View pvView) {

                if (cReturnorderLine.currentReturnOrderLine.barcodeObl() == null || cReturnorderLine.currentReturnOrderLine.barcodeObl().size() == 0) {
                    return;
                }

                //If we only have one barcodeStr, then automatticaly select that barcodeStr
                if (cReturnorderLine.currentReturnOrderLine.barcodeObl().size() == 1) {
                    pHandleScan(cBarcodeScan.pFakeScan(cReturnorderLine.currentReturnOrderLine.barcodeObl().get(0).getBarcodeStr()));
                    return;
                }

                mShowBarcodeSelectFragment();

            }
        });
    }

    private void mShowBarcodeSelectFragment() {
        BarcodeFragment barcodeFragment = new BarcodeFragment();
        barcodeFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.BARCODEFRAGMENT_TAG);
    }

    private  void mGoBackToDocumentActivity() {
        Intent intent = new Intent(cAppExtension.context, ReturnorderDocumentActivity.class);
        ReturnorderDocumentActivity.busyBln = false;
        cAppExtension.activity.startActivity(intent);
        cAppExtension.activity.finish();
    }

    private void mShowAcceptFragment(){
        boolean ignoreAccept = false;


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

    private void mSetArticleImageListener() {
        this.articleThumbImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mShowFullArticleFragment();
            }
        });
    }

    private void mShowFullArticleFragment() {
        cUserInterface.pCheckAndCloseOpenDialogs();
        ArticleFullViewFragment articleFullViewFragment = new ArticleFullViewFragment();
        articleFullViewFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.ARTICLEFULL_TAG);

    }

    private  void mShowExtrasNotAllowed(){
        cUserInterface.pShowSnackbarMessage(quantityText , cAppExtension.context.getString(R.string.number_cannot_be_higher), null, false);
        cUserInterface.pDoNope(quantityText, true, true);
    }


    //End Region Private Methods
}
