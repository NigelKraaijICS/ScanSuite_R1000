package nl.icsvertex.scansuite.Fragments.Returns;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.Objects;

import ICS.Interfaces.iICSDefaultFragment;
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
import nl.icsvertex.scansuite.Activities.Returns.ReturnorderDocumentActivity;
import nl.icsvertex.scansuite.Fragments.Dialogs.BarcodeFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.ReasonFragment;
import nl.icsvertex.scansuite.R;

public class ReturnArticleDetailFragment extends DialogFragment implements iICSDefaultFragment {


    //Region Private Properties

    private int returnCounterMinusHelperInt;
    private int returnCounterPlusHelperInt;
    private Handler minusHandler;
    private Handler plusHandler;

    private ImageView toolbarImage;
    private TextView toolbarTitle;
    private TextView toolbarSubtext;

    private ImageView articleThumbImageView;
    private TextView binText;
    private EditText quantityText;
    private AppCompatImageButton imageButtonMinus;
    private AppCompatImageButton imageButtonPlus;

    private TextView reasonText;
    private ImageButton imageButtonReason;

    private TextView articleDescriptionText;
    private TextView articleDescription2Text;
    private TextView articleItemText;
    private TextView articleBarcodeText;
    private TextView articleUnitOfMeasureText;
    private TextView articleVendorItemText;

    private TextView genericItemExtraField1Text;
    private TextView genericItemExtraField2Text;
    private TextView genericItemExtraField3Text;
    private TextView genericItemExtraField4Text;
    private TextView genericItemExtraField5Text;
    private TextView genericItemExtraField6Text;
    private TextView genericItemExtraField7Text;
    private TextView genericItemExtraField8Text;

    private ImageView imageButtonDone;

    private ImageButton imageButtonBarcode;
    private static DialogFragment dialogFragment;

    private cReturnorderLineViewModel getReturnorderLineViewModel() {
        return new ViewModelProvider(cAppExtension.fragmentActivity).get(cReturnorderLineViewModel.class);
    }

    //End Region Private Properties

    //Region Constructor
    public ReturnArticleDetailFragment() {

    }

    //End Region Constructor

    //Region Default Methods

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_returnarticle_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        cAppExtension.dialogFragment = this;
        ReturnArticleDetailFragment.dialogFragment = this;
        this.mFragmentInitialize();

    }

    @Override
    public void onPause() {
        super.onPause();
        cBarcodeScan.pUnregisterBarcodeFragmentReceiver();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels - getResources().getDimensionPixelSize(R.dimen.default_double_margin);

        Objects.requireNonNull(Objects.requireNonNull(getDialog()).getWindow()).setLayout(width, height);
        cBarcodeScan.pRegisterBarcodeFragmentReceiver();
        cUserInterface.pEnableScanner();
    }

    //End Region Default Methods

    //Region iICSDefaultActivity defaults

    @Override
    public void mFragmentInitialize() {
        this.mFindViews();
        this.mFieldsInitialize();
        this.mSetListeners();
        this.mSetToolbar();

        cBarcodeScan.pRegisterBarcodeFragmentReceiver();
    }

    @Override
    public void mFindViews() {

        if (getView() != null) {
            this.toolbarImage = getView().findViewById(R.id.toolbarImage);
            this.toolbarTitle = getView().findViewById(R.id.toolbarTitle);
            this.toolbarSubtext = getView().findViewById(R.id.toolbarSubtext);

            this.articleThumbImageView = getView().findViewById(R.id.articleThumbImageView);
            this.binText = getView().findViewById(R.id.binText);
            this.quantityText = getView().findViewById(R.id.quantityText);
            this.imageButtonMinus = getView().findViewById(R.id.imageButtonMinus);
            this.imageButtonPlus = getView().findViewById(R.id.imageButtonPlus);
            this.imageButtonBarcode = getView().findViewById(R.id.imageButtonBarcode);

            this.reasonText = getView().findViewById(R.id.reasonText);
            this.reasonText.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            this.reasonText.setSingleLine(true);
            this.reasonText.setMarqueeRepeatLimit(5);
            this.reasonText.setSelected(true);
            this.imageButtonReason = getView().findViewById(R.id.imageButtonReason);

            this.articleDescriptionText = getView().findViewById(R.id.articleDescriptionText);
            this.articleDescription2Text = getView().findViewById(R.id.articleDescription2Text);
            this.articleItemText = getView().findViewById(R.id.articleItemText);
            this.articleBarcodeText = getView().findViewById(R.id.articleBarcodeText);
            this.articleUnitOfMeasureText = getView().findViewById(R.id.articleUnitOfMeasureText);
            this.articleVendorItemText = getView().findViewById(R.id.articleVendorItemText);

            this.genericItemExtraField1Text = getView().findViewById(R.id.genericItemExtraField1Text);
            this.genericItemExtraField2Text = getView().findViewById(R.id.genericItemExtraField2Text);
            this.genericItemExtraField3Text = getView().findViewById(R.id.genericItemExtraField3Text);
            this.genericItemExtraField4Text = getView().findViewById(R.id.genericItemExtraField4Text);
            this.genericItemExtraField5Text = getView().findViewById(R.id.genericItemExtraField5Text);
            this.genericItemExtraField6Text = getView().findViewById(R.id.genericItemExtraField6Text);
            this.genericItemExtraField7Text = getView().findViewById(R.id.genericItemExtraField7Text);
            this.genericItemExtraField8Text = getView().findViewById(R.id.genericItemExtraField8Text);

            this.imageButtonDone = getView().findViewById(R.id.imageButtonDone);
        }


    }

    @Override
    public void mFieldsInitialize() {

        if (!cSetting.RETOUR_AMOUNT_MANUAL()) {
            this.imageButtonBarcode.setVisibility(View.INVISIBLE);
        } else {
            this.imageButtonBarcode.setVisibility(View.VISIBLE);
        }

        this.returnCounterPlusHelperInt = 0;
        this.returnCounterMinusHelperInt = 0;
        this.toolbarSubtext.setText(cReturnorderDocument.currentReturnOrderDocument.getSourceDocumentStr());

        this.articleDescriptionText.setText(cReturnorderLine.currentReturnOrderLine.getDescriptionStr());
        this.articleDescription2Text.setText(cReturnorderLine.currentReturnOrderLine.getDescription2Str());
        this.articleItemText.setText(cReturnorderLine.currentReturnOrderLine.getItemNoAndVariantCodeStr());

        if (cReturnorderBarcode.currentReturnOrderBarcode != null) {
            this.articleBarcodeText.setText(cReturnorderBarcode.currentReturnOrderBarcode.getBarcodeAndQuantityStr());
            this.articleUnitOfMeasureText.setText(cReturnorderBarcode.currentReturnOrderBarcode.getUnitOfMeasureStr());
        } else {
            this.articleBarcodeText.setText(cAppExtension.context.getString(R.string.message_unknown_barcode));
            this.articleUnitOfMeasureText.setText("");
        }

        this.articleVendorItemText.setText(cReturnorderLine.currentReturnOrderLine.getVendorItemNoAndDescriptionStr());

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
        this.mShowOrHideGenericExtraFields();

    }

    @Override
    public void mSetListeners() {
        this.mSetDoneListener();
        this.mSetReasonListener();

        if (cSetting.RETOUR_AMOUNT_MANUAL()) {
            this.mSetPlusListener();
            this.mSetMinusListener();
            this.mSetEditorActionListener();
            this.mSetImageButtonBarcodeListener();
        }
    }

    //End Region iICSDefaultActivity defaults


    //Region Public Methods

    public  void pHandleScan(cBarcodeScan pvBarcodeScan){

        String barcodeWithoutPrefixStr = cRegex.pStripRegexPrefixStr(pvBarcodeScan.getBarcodeOriginalStr());
        boolean newReasonAllowedBln;

        //Check if REASON scans are allowed
        if (!cReturnorderLine.currentReturnOrderLine.isGeneratedBln()) {
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
                return;
            }
            else {
                cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.message_reason_unknown), null);
                return;
            }
        }

        if (isArticleBln) {

            if (!this.mCheckBarcodeWithLineBarcodesBln(pvBarcodeScan)) {

                if (cReturnorderLine.currentReturnOrderLine.getRetourRedenStr().isEmpty()) {
                    cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.message_scan_reason_first), null);
                    return;
                }

                //Close this fragment, we are done here
                this.mHandleDone();

                //Handle scan in BIN activity,
                this.pHandleScan(pvBarcodeScan);
                return;
            }

            //Try to raise quantityDbl
            this.mTryToChangeReturnorderQuantity(true, false,cReturnorderBarcode.currentReturnOrderBarcode.getQuantityPerUnitOfMeasureDbl() );

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
        }

    }

    public void pHandleFragmentDismissed(){
        cAppExtension.dialogFragment = dialogFragment;
        cBarcodeScan.pRegisterBarcodeFragmentReceiver();
    }

    public void pSetReason(){
        this.reasonText.setText(cBranchReason.currentBranchReason.getDescriptionStr());
        cReturnorderLine.currentReturnOrderLine.retourRedenStr = cBranchReason.currentBranchReason.getReasonStr();
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


    private void mSetToolbar() {

        this.toolbarTitle.setText(cAppExtension.activity.getString(R.string.message_scan_article));
        this.toolbarTitle.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        this.toolbarTitle.setSingleLine(true);
        this.toolbarTitle.setMarqueeRepeatLimit(5);
        this.toolbarTitle.postDelayed(new Runnable() {
            @Override
            public void run() {
                toolbarTitle.setSelected(true);
            }
        },1500);

        this.toolbarImage.setImageResource(R.drawable.ic_info);
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
                cReturnorderLine.currentReturnOrderLine.quantityHandledTakeDbl = pvAmountDbl;
                cReturnorderLineBarcode.currentreturnorderLineBarcode.quantityHandledDbl = pvAmountDbl;
            } else {
                cReturnorderLine.currentReturnOrderLine.quantityHandledTakeDbl += pvAmountDbl;
                cReturnorderLineBarcode.currentreturnorderLineBarcode.quantityHandledDbl += pvAmountDbl;
            }


            //Change quantityDbl in activuty
            if (cReturnorderLine.currentReturnOrderLine.getQuantitytakeDbl() == 0.0 ){
                newQuantityStr = cText.pDoubleToStringStr(cReturnorderLine.currentReturnOrderLine.getQuantityHandledTakeDbl());
                this.quantityText.setText(newQuantityStr);
            }
            else {
                newQuantityStr = cText.pDoubleToStringStr(cReturnorderLine.currentReturnOrderLine.getQuantityHandledTakeDbl()) + "/" + cText.pDoubleToStringStr(cReturnorderLine.currentReturnOrderLine.getQuantitytakeDbl());
                this.quantityText.setText(newQuantityStr);
            }
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
                this.quantityText.setText(newQuantityStr);
            }
            else {
                newQuantityStr = cText.pDoubleToStringStr(cReturnorderLine.currentReturnOrderLine.getQuantityHandledTakeDbl()) + "/" + cText.pDoubleToStringStr(cReturnorderLine.currentReturnOrderLine.getQuantitytakeDbl());
                this.quantityText.setText(newQuantityStr);
            }

        }
    }

    private void mShowArticleImage() {
        this.articleThumbImageView.setImageDrawable(ContextCompat.getDrawable(cAppExtension.context, R.drawable.ic_no_image_lightgrey_24dp));
    }

    private  void mShowOrHideGenericExtraFields() {

        if (!cReturnorderLine.currentReturnOrderLine.getExtraField1Str().isEmpty()) {
            this.genericItemExtraField1Text.setVisibility(View.VISIBLE);
            this.genericItemExtraField1Text.setText(cReturnorderLine.currentReturnOrderLine.getExtraField1Str());
        }
        if (!cReturnorderLine.currentReturnOrderLine.getExtraField2Str().isEmpty()) {
            this.genericItemExtraField2Text.setVisibility(View.VISIBLE);
            this.genericItemExtraField2Text.setText(cReturnorderLine.currentReturnOrderLine.getExtraField2Str());
        }
        if (!cReturnorderLine.currentReturnOrderLine.getExtraField3Str().isEmpty()) {
            this.genericItemExtraField3Text.setVisibility(View.VISIBLE);
            this.genericItemExtraField3Text.setText(cReturnorderLine.currentReturnOrderLine.getExtraField3Str());
        }
        if (!cReturnorderLine.currentReturnOrderLine.getExtraField4Str().isEmpty()) {
            this.genericItemExtraField4Text.setVisibility(View.VISIBLE);
            this.genericItemExtraField4Text.setText(cReturnorderLine.currentReturnOrderLine.getExtraField4Str());
        }
        if (!cReturnorderLine.currentReturnOrderLine.getExtraField5Str().isEmpty()) {
            this.genericItemExtraField5Text.setVisibility(View.VISIBLE);
            this.genericItemExtraField5Text.setText(cReturnorderLine.currentReturnOrderLine.getExtraField5Str());
        }
        if (!cReturnorderLine.currentReturnOrderLine.getExtraField6Str().isEmpty()) {
            this.genericItemExtraField6Text.setVisibility(View.VISIBLE);
            this.genericItemExtraField6Text.setText(cReturnorderLine.currentReturnOrderLine.getExtraField6Str());
        }
        if (!cReturnorderLine.currentReturnOrderLine.getExtraField7Str().isEmpty()) {
            this.genericItemExtraField7Text.setVisibility(View.VISIBLE);
            this.genericItemExtraField7Text.setText(cReturnorderLine.currentReturnOrderLine.getExtraField7Str());
        }
        if (!cReturnorderLine.currentReturnOrderLine.getExtraField8Str().isEmpty()) {
            this.genericItemExtraField8Text.setVisibility(View.VISIBLE);
            this.genericItemExtraField8Text.setText(cReturnorderLine.currentReturnOrderLine.getExtraField8Str());
        }
    }

    private void mHandleDone() {

        cAppExtension.dialogFragment = dialogFragment;

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

        if (cAppExtension.activity instanceof  ReturnorderDocumentActivity) {
            ReturnorderDocumentActivity returnorderDocumentActivity = (ReturnorderDocumentActivity) cAppExtension.activity;
            //Change quantityDbl handled in database
            cReturnorderLine.currentReturnOrderLine.pUpdateQuantityInDatabase();
            returnorderDocumentActivity.pLineHandled();
            returnorderDocumentActivity.pHandleFragmentDismissed();
            cUserInterface.pHideGettingData();
            cAppExtension.dialogFragment.dismiss();
        }
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

     //End Region Private Methods
}
