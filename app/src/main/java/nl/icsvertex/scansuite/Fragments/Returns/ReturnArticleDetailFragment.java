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
import SSU_WHS.Return.ReturnorderLineBarcode.cReturnorderLineBarcode;
import nl.icsvertex.scansuite.Activities.Returns.ReturnorderDocumentActivity;
import nl.icsvertex.scansuite.Fragments.Dialogs.BarcodeFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.ReasonFragment;
import nl.icsvertex.scansuite.R;

public class ReturnArticleDetailFragment extends DialogFragment implements iICSDefaultFragment {


    //Region Private Properties
    private static final String BARCODEFRAGMENT_TAG = "BARCODERAGMENT_TAG";
    private static int returnCounterMinusHelperInt;
    private static int returnCounterPlusHelperInt;
    private static Handler minusHandler;
    private static Handler plusHandler;

    private static ImageView toolbarImage;
    private static  TextView toolbarTitle;
    private static TextView toolbarSubtext;

    private static ImageView articleThumbImageView;
    private static TextView binText;
    private static EditText quantityText;
    private static AppCompatImageButton imageButtonMinus;
    private static AppCompatImageButton imageButtonPlus;

    private static TextView reasonText;
    private static ImageButton imageButtonReason;

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

    private static ImageView imageButtonDone;

    private static ImageButton imageButtonBarcode;
    private static DialogFragment dialogFragment;


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

    public static void pSetReason(){
        ReturnArticleDetailFragment.reasonText.setText(cBranchReason.currentBranchReason.getDescriptionStr());
        cReturnorderLine.currentReturnOrderLine.retourredenStr = cBranchReason.currentBranchReason.getReasonStr();

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
            ReturnArticleDetailFragment.toolbarImage = getView().findViewById(R.id.toolbarImage);
            ReturnArticleDetailFragment.toolbarTitle = getView().findViewById(R.id.toolbarTitle);
            ReturnArticleDetailFragment.toolbarSubtext = getView().findViewById(R.id.toolbarSubtext);

            ReturnArticleDetailFragment.articleThumbImageView = getView().findViewById(R.id.articleThumbImageView);
            ReturnArticleDetailFragment.binText = getView().findViewById(R.id.binText);
            ReturnArticleDetailFragment.quantityText = getView().findViewById(R.id.quantityText);
            ReturnArticleDetailFragment.imageButtonMinus = getView().findViewById(R.id.imageButtonMinus);
            ReturnArticleDetailFragment.imageButtonPlus = getView().findViewById(R.id.imageButtonPlus);
            ReturnArticleDetailFragment.imageButtonBarcode = getView().findViewById(R.id.imageButtonBarcode);

            ReturnArticleDetailFragment.reasonText = getView().findViewById(R.id.reasonText);
            ReturnArticleDetailFragment.reasonText.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            ReturnArticleDetailFragment.reasonText.setSingleLine(true);
            ReturnArticleDetailFragment.reasonText.setMarqueeRepeatLimit(5);
            ReturnArticleDetailFragment.reasonText.setSelected(true);
            ReturnArticleDetailFragment.imageButtonReason = getView().findViewById(R.id.imageButtonReason);

            ReturnArticleDetailFragment.articleDescriptionText = getView().findViewById(R.id.articleDescriptionText);
            ReturnArticleDetailFragment.articleDescription2Text = getView().findViewById(R.id.articleDescription2Text);
            ReturnArticleDetailFragment.articleItemText = getView().findViewById(R.id.articleItemText);
            ReturnArticleDetailFragment.articleBarcodeText = getView().findViewById(R.id.articleBarcodeText);
            ReturnArticleDetailFragment.articleUnitOfMeasureText = getView().findViewById(R.id.articleUnitOfMeasureText);
            ReturnArticleDetailFragment.articleVendorItemText = getView().findViewById(R.id.articleVendorItemText);

            ReturnArticleDetailFragment.genericItemExtraField1Text = getView().findViewById(R.id.genericItemExtraField1Text);
            ReturnArticleDetailFragment.genericItemExtraField2Text = getView().findViewById(R.id.genericItemExtraField2Text);
            ReturnArticleDetailFragment.genericItemExtraField3Text = getView().findViewById(R.id.genericItemExtraField3Text);
            ReturnArticleDetailFragment.genericItemExtraField4Text = getView().findViewById(R.id.genericItemExtraField4Text);
            ReturnArticleDetailFragment.genericItemExtraField5Text = getView().findViewById(R.id.genericItemExtraField5Text);
            ReturnArticleDetailFragment.genericItemExtraField6Text = getView().findViewById(R.id.genericItemExtraField6Text);
            ReturnArticleDetailFragment.genericItemExtraField7Text = getView().findViewById(R.id.genericItemExtraField7Text);
            ReturnArticleDetailFragment.genericItemExtraField8Text = getView().findViewById(R.id.genericItemExtraField8Text);

            ReturnArticleDetailFragment.imageButtonDone = getView().findViewById(R.id.imageButtonDone);
        }


    }

    @Override
    public void mFieldsInitialize() {

        if (!cSetting.RETOUR_AMOUNT_MANUAL()) {
            ReturnArticleDetailFragment.imageButtonBarcode.setVisibility(View.INVISIBLE);
        } else {
            ReturnArticleDetailFragment.imageButtonBarcode.setVisibility(View.VISIBLE);
        }

        ReturnArticleDetailFragment.returnCounterPlusHelperInt = 0;
        ReturnArticleDetailFragment.returnCounterMinusHelperInt = 0;
        ReturnArticleDetailFragment.toolbarSubtext.setText(cReturnorderDocument.currentReturnOrderDocument.getSourceDocumentStr());

        ReturnArticleDetailFragment.articleDescriptionText.setText(cReturnorderLine.currentReturnOrderLine.getDescriptionStr());
        ReturnArticleDetailFragment.articleDescription2Text.setText(cReturnorderLine.currentReturnOrderLine.getDescription2Str());
        ReturnArticleDetailFragment.articleItemText.setText(cReturnorderLine.currentReturnOrderLine.getItemNoAndVariantCodeStr());

        if (cReturnorderBarcode.currentReturnOrderBarcode != null) {
            ReturnArticleDetailFragment.articleBarcodeText.setText(cReturnorderBarcode.currentReturnOrderBarcode.getBarcodeAndQuantityStr());

            ReturnArticleDetailFragment.articleUnitOfMeasureText.setText(cReturnorderBarcode.currentReturnOrderBarcode.getUnitOfMeasureStr());
        } else {
            ReturnArticleDetailFragment.articleBarcodeText.setText(cAppExtension.context.getString(R.string.message_unknown_barcode));
            ReturnArticleDetailFragment.articleUnitOfMeasureText.setText("");
        }

        ReturnArticleDetailFragment.articleVendorItemText.setText(cReturnorderLine.currentReturnOrderLine.getVendorItemNoAndDescriptionStr());

        if (!cReturnorderLine.currentReturnOrderLine.isGeneratedBln()){
            cBranchReason.currentBranchReason = cUser.currentUser.currentBranch.pGetReasonByName(cReturnorderLine.currentReturnOrderLine.getRetourredenStr());
        }
        else {
            if (cBranchReason.currentBranchReason != null){
                cReturnorderLine.currentReturnOrderLine.retourredenStr = cBranchReason.currentBranchReason.getReasonStr();
            }
        }
        if (cBranchReason.currentBranchReason != null){
            ReturnArticleDetailFragment.reasonText.setText(cBranchReason.currentBranchReason.getDescriptionStr());
        }

        ReturnArticleDetailFragment.binText.setText(cReturnorder.currentReturnOrder.getBinCodeStr());

        ReturnArticleDetailFragment.quantityText.setSelectAllOnFocus(true);
        ReturnArticleDetailFragment.quantityText.requestFocus();
        if (cReturnorderLine.currentReturnOrderLine.getQuantitytakeDbl() == 0.0 ){
            ReturnArticleDetailFragment.quantityText.setText(cText.pDoubleToStringStr(cReturnorderLine.currentReturnOrderLine.getQuantityHandledTakeDbl()));
        }
        else {
            ReturnArticleDetailFragment.quantityText.setText(cReturnorderLine.currentReturnOrderLine.getQuantityToShowInAdapterStr());
        }

        ReturnArticleDetailFragment.quantityText.setInputType(InputType.TYPE_TEXT_VARIATION_NORMAL);
        ReturnArticleDetailFragment.quantityText.setCursorVisible(false);

        ReturnArticleDetailFragment.mShowArticleImage();
        ReturnArticleDetailFragment.mShowOrHideGenericExtraFields();

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

    public static void pHandleScan(cBarcodeScan pvBarcodeScan){

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
        if (cBarcodeLayout.pGetBarcodeLayoutByBarcodeObl(pvBarcodeScan.getBarcodeOriginalStr()).size() == 0) {
            cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.message_unknown_barcode), null);
            return;
        }

        if (cBarcodeLayout.pGetBarcodeLayoutByBarcodeObl(pvBarcodeScan.getBarcodeOriginalStr()).size() == 1) {

            if (cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(),cBarcodeLayout.barcodeLayoutEnu.ARTICLE)) {
                isArticleBln = true;
                regexDecidedBln = true;
            }

            if (cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(),cBarcodeLayout.barcodeLayoutEnu.REASON)) {
                isReasonBln = true;
                regexDecidedBln = true;

            }

            if (cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(),cBarcodeLayout.barcodeLayoutEnu.DOCUMENT)) {
                isDocumentBln = true;
                regexDecidedBln = true;
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
                regexDecidedBln = true;
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
                ReturnArticleDetailFragment.pSetReason();
                return;
            }
            else {
                cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.message_reason_unknown), null);
                return;
            }
        }

        if (isArticleBln) {

            if (!ReturnArticleDetailFragment.mCheckBarcodeWithLineBarcodesBln(pvBarcodeScan)) {

                if (cReturnorderLine.currentReturnOrderLine.getRetourredenStr().isEmpty()) {
                    cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.message_scan_reason_first), null);
                    return;
                }

                //Close this fragment, we are done here
                ReturnArticleDetailFragment.mHandleDone();

                //Handle scan in BIN activity,
                ReturnorderDocumentActivity.pHandleScan(pvBarcodeScan);
                return;
            }

            //Try to raise quantityDbl
            ReturnArticleDetailFragment.mTryToChangeReturnorderQuantity(true, false,cReturnorderBarcode.currentReturnOrderBarcode.getQuantityPerUnitOfMeasureDbl() );

        }

        if (isDocumentBln) {


            if (cReturnorderDocument.currentReturnOrderDocument.getSourceDocumentStr().equalsIgnoreCase(barcodeWithoutPrefixStr)) {
                cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.message_document_already_active), null);
                return;
            }

            if (cReturnorderLine.currentReturnOrderLine.getRetourredenStr().isEmpty()) {
                cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.message_scan_reason_first), null);
                return;
            }

            //Close this fragment, we are done here
            ReturnArticleDetailFragment.mHandleDone();

            //Handle scan in BIN activity,
            ReturnorderDocumentActivity.pHandleScan(pvBarcodeScan);
            return;

        }

    }

    public static void pHandleFragmentDismissed(){
        cAppExtension.dialogFragment = dialogFragment;
        cBarcodeScan.pRegisterBarcodeFragmentReceiver();
    }

    //End Region Public Methods

    //Region Private Methods

    private static boolean mCheckBarcodeWithLineBarcodesBln(cBarcodeScan pvBarcodeScan){

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

        ReturnArticleDetailFragment.toolbarTitle.setText(cAppExtension.activity.getString(R.string.message_scan_article));
        ReturnArticleDetailFragment.toolbarTitle.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        ReturnArticleDetailFragment.toolbarTitle.setSingleLine(true);
        ReturnArticleDetailFragment.toolbarTitle.setMarqueeRepeatLimit(5);
        ReturnArticleDetailFragment.toolbarTitle.postDelayed(new Runnable() {
            @Override
            public void run() {
                toolbarTitle.setSelected(true);
            }
        },1500);

        ReturnArticleDetailFragment.toolbarImage.setImageResource(R.drawable.ic_info);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void mSetPlusListener() {

        ReturnArticleDetailFragment.imageButtonPlus.setOnTouchListener(new View.OnTouchListener() {

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

        ReturnArticleDetailFragment.imageButtonPlus.setOnClickListener(new View.OnClickListener() {
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

        ReturnArticleDetailFragment.imageButtonMinus.setOnTouchListener(new View.OnTouchListener() {
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

        ReturnArticleDetailFragment.imageButtonMinus.setOnClickListener(new View.OnClickListener() {
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


        ReturnArticleDetailFragment.quantityText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReturnArticleDetailFragment.quantityText.requestFocus();
                ReturnArticleDetailFragment.quantityText.setSelection(0, ReturnArticleDetailFragment.quantityText.getText().toString().length());
            }
        });


        ReturnArticleDetailFragment.quantityText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE || i == EditorInfo.IME_ACTION_GO ) {


                    ReturnArticleDetailFragment.mTryToChangeReturnorderQuantity(true,
                            true,
                            cReturnorderLine.currentReturnOrderLine.getQuantityHandledTakeDbl());

                    ReturnArticleDetailFragment.imageButtonDone.callOnClick();
                }
                return true;
            }
        });
    }

    private void mSetDoneListener(){

        ReturnArticleDetailFragment.imageButtonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReturnArticleDetailFragment.mHandleDone();
            }
        });

    }

    private void mSetReasonListener(){
        ReturnArticleDetailFragment.imageButtonReason.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (cReturnorderLine.currentReturnOrderLine.nieuweRegelBln) {
                    mShowAddReasonFragment();
                }
            }
        });
    }

    private void mDoDelayedMinus(Runnable pvRunnable, long pvMilliSecsLng) {
        ReturnArticleDetailFragment.minusHandler.postDelayed(pvRunnable, pvMilliSecsLng);
        ReturnArticleDetailFragment.returnCounterMinusHelperInt += 1;

    }

    private void mDoDelayedPlus(Runnable pvRunnable, long pvMilliSecsLng) {
        ReturnArticleDetailFragment.plusHandler.postDelayed(pvRunnable, pvMilliSecsLng);
        ReturnArticleDetailFragment.returnCounterPlusHelperInt += 1;
    }

    private static void mTryToChangeReturnorderQuantity(Boolean pvIsPositiveBln, Boolean pvAmountFixedBln, double pvAmountDbl) {

        String newQuantityStr;
        Double newQuantityDbl;

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
                ReturnArticleDetailFragment.quantityText.setText(newQuantityStr);
            }
            else {
                newQuantityStr = cText.pDoubleToStringStr(cReturnorderLine.currentReturnOrderLine.getQuantityHandledTakeDbl()) + "/" + cText.pDoubleToStringStr(cReturnorderLine.currentReturnOrderLine.getQuantitytakeDbl());
                ReturnArticleDetailFragment.quantityText.setText(newQuantityStr);
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
                cUserInterface.pDoNope(ReturnArticleDetailFragment.quantityText, true, true);
                return;
            }

        }
        if (pvAmountDbl <= 0) {
            cUserInterface.pDoNope(ReturnArticleDetailFragment.quantityText, true, true);
            return;
        }

        //Determine the new amount
        if (pvAmountFixedBln) {
            cReturnorderLine.currentReturnOrderLine.quantityHandledTakeDbl = pvAmountDbl;
            cReturnorderLineBarcode.currentreturnorderLineBarcode.quantityHandledDbl = pvAmountDbl;
        } else {

            newQuantityDbl =  cReturnorderLine.currentReturnOrderLine.quantityHandledTakeDbl - pvAmountDbl;

            if (newQuantityDbl == 0 ) {
                cUserInterface.pDoNope(ReturnArticleDetailFragment.quantityText, true, true);
                return;
            }

            cReturnorderLine.currentReturnOrderLine.quantityHandledTakeDbl -= pvAmountDbl;
            cReturnorderLineBarcode.currentreturnorderLineBarcode.quantityHandledDbl -= pvAmountDbl;


            if (cReturnorderLine.currentReturnOrderLine.getQuantitytakeDbl() == 0.0 ){
                newQuantityStr = cText.pDoubleToStringStr(cReturnorderLine.currentReturnOrderLine.getQuantityHandledTakeDbl());
                ReturnArticleDetailFragment.quantityText.setText(newQuantityStr);
            }
            else {
                newQuantityStr = cText.pDoubleToStringStr(cReturnorderLine.currentReturnOrderLine.getQuantityHandledTakeDbl()) + "/" + cText.pDoubleToStringStr(cReturnorderLine.currentReturnOrderLine.getQuantitytakeDbl());
                ReturnArticleDetailFragment.quantityText.setText(newQuantityStr);
            }

        }
    }

    private static void mShowArticleImage() {

        //If pick with picture is false, then hide image view

        ReturnArticleDetailFragment.articleThumbImageView.setImageDrawable(ContextCompat.getDrawable(cAppExtension.context, R.drawable.ic_no_image_lightgrey_24dp));

        //todo: do this with the correct settings
    }

    private static void mShowOrHideGenericExtraFields() {

        if (!cReturnorderLine.currentReturnOrderLine.getExtraField1Str().isEmpty()) {
            ReturnArticleDetailFragment.genericItemExtraField1Text.setVisibility(View.VISIBLE);
            ReturnArticleDetailFragment.genericItemExtraField1Text.setText(cReturnorderLine.currentReturnOrderLine.getExtraField1Str());
        }
        if (!cReturnorderLine.currentReturnOrderLine.getExtraField2Str().isEmpty()) {
            ReturnArticleDetailFragment.genericItemExtraField2Text.setVisibility(View.VISIBLE);
            ReturnArticleDetailFragment.genericItemExtraField2Text.setText(cReturnorderLine.currentReturnOrderLine.getExtraField2Str());
        }
        if (!cReturnorderLine.currentReturnOrderLine.getExtraField3Str().isEmpty()) {
            ReturnArticleDetailFragment.genericItemExtraField3Text.setVisibility(View.VISIBLE);
            ReturnArticleDetailFragment.genericItemExtraField3Text.setText(cReturnorderLine.currentReturnOrderLine.getExtraField3Str());
        }
        if (!cReturnorderLine.currentReturnOrderLine.getExtraField4Str().isEmpty()) {
            ReturnArticleDetailFragment.genericItemExtraField4Text.setVisibility(View.VISIBLE);
            ReturnArticleDetailFragment.genericItemExtraField4Text.setText(cReturnorderLine.currentReturnOrderLine.getExtraField4Str());
        }
        if (!cReturnorderLine.currentReturnOrderLine.getExtraField5Str().isEmpty()) {
            ReturnArticleDetailFragment.genericItemExtraField5Text.setVisibility(View.VISIBLE);
            ReturnArticleDetailFragment.genericItemExtraField5Text.setText(cReturnorderLine.currentReturnOrderLine.getExtraField5Str());
        }
        if (!cReturnorderLine.currentReturnOrderLine.getExtraField6Str().isEmpty()) {
            ReturnArticleDetailFragment.genericItemExtraField6Text.setVisibility(View.VISIBLE);
            ReturnArticleDetailFragment.genericItemExtraField6Text.setText(cReturnorderLine.currentReturnOrderLine.getExtraField6Str());
        }
        if (!cReturnorderLine.currentReturnOrderLine.getExtraField7Str().isEmpty()) {
            ReturnArticleDetailFragment.genericItemExtraField7Text.setVisibility(View.VISIBLE);
            ReturnArticleDetailFragment.genericItemExtraField7Text.setText(cReturnorderLine.currentReturnOrderLine.getExtraField7Str());
        }
        if (!cReturnorderLine.currentReturnOrderLine.getExtraField8Str().isEmpty()) {
            ReturnArticleDetailFragment.genericItemExtraField8Text.setVisibility(View.VISIBLE);
            ReturnArticleDetailFragment.genericItemExtraField8Text.setText(cReturnorderLine.currentReturnOrderLine.getExtraField8Str());
        }
    }

    private static void mHandleDone() {

        cAppExtension.dialogFragment = dialogFragment;

        if (cBranchReason.currentBranchReason == null){
            cUserInterface.pDoNope(imageButtonReason, true, true);
            cUserInterface.pShowToastMessage(cAppExtension.activity.getString(R.string.choose_reason), null);
            return;
        }
        cReturnorderLine.currentReturnOrderLine.retourredenStr = cBranchReason.currentBranchReason.getReasonStr();

        //Try to match item, variantcode and reason with previous lines so we can show 1 line in recycler
        cReturnorderLine.pCheckIfLineIsAlreadyInUse();


        //Try to save the line to the database
        if (cReturnorderLine.currentReturnOrderLine.isGeneratedBln() && cReturnorderLine.currentReturnOrderLine.nieuweRegelBln){
            cReturnorderLine.getReturnorderLineViewModel().pCreateItemVariantViaWebserviceWrs();
            cReturnorderLine.currentReturnOrderLine.nieuweRegelBln = false;
            cReturnorderDocument.currentReturnOrderDocument.returnorderLineObl.add(cReturnorderLine.currentReturnOrderLine);

        }

        if (!cReturnorderLine.currentReturnOrderLine.pSaveLineViaWebserviceBln()) {
            cUserInterface.pDoExplodingScreen(cAppExtension.activity.getString(R.string.message_line_save_failed),"",true,true);
            ReturnorderDocumentActivity.pHandleFragmentDismissed();
            cAppExtension.dialogFragment.dismiss();
            return;
        }


        //Change quantityDbl handled in database
        cReturnorderLine.currentReturnOrderLine.pUpdateQuantityInDatabase();
        ReturnorderDocumentActivity.pLineHandled();
        ReturnorderDocumentActivity.pHandleFragmentDismissed();
        cUserInterface.pHideGettingData();
        cAppExtension.dialogFragment.dismiss();
    }

    private Runnable mMinusAction = new Runnable() {
        @Override
        public void run() {
            ReturnArticleDetailFragment.imageButtonMinus.performClick();
            long milliSecsLng;
            if (ReturnArticleDetailFragment.returnCounterMinusHelperInt < 10) {
                milliSecsLng = 200;
            } else if (ReturnArticleDetailFragment.returnCounterMinusHelperInt < 20) {
                milliSecsLng = 150;
            } else if (ReturnArticleDetailFragment.returnCounterMinusHelperInt < 30) {
                milliSecsLng = 100;
            } else if (ReturnArticleDetailFragment.returnCounterMinusHelperInt < 40) {
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
            ReturnArticleDetailFragment.imageButtonPlus.performClick();
            long milliSecsLng;
            if (ReturnArticleDetailFragment.returnCounterPlusHelperInt < 10) {
                milliSecsLng = 200;
            } else if (ReturnArticleDetailFragment.returnCounterPlusHelperInt < 20) {
                milliSecsLng = 150;
            } else if (ReturnArticleDetailFragment.returnCounterPlusHelperInt < 30) {
                milliSecsLng = 100;
            } else if (ReturnArticleDetailFragment.returnCounterPlusHelperInt < 40) {
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
        ReturnArticleDetailFragment.imageButtonBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View pvView) {

                if (cReturnorderLine.currentReturnOrderLine.barcodeObl() == null || cReturnorderLine.currentReturnOrderLine.barcodeObl().size() == 0) {
                    return;
                }

                //If we only have one barcodeStr, then automatticaly select that barcodeStr
                if (cReturnorderLine.currentReturnOrderLine.barcodeObl().size() == 1) {
                    ReturnArticleDetailFragment.pHandleScan(cBarcodeScan.pFakeScan(cReturnorderLine.currentReturnOrderLine.barcodeObl().get(0).getBarcodeStr()));
                    return;
                }

                mShowBarcodeSelectFragment();

            }
        });
    }

    private void mShowBarcodeSelectFragment() {
        BarcodeFragment barcodeFragment = new BarcodeFragment();
        barcodeFragment.show(cAppExtension.fragmentManager, BARCODEFRAGMENT_TAG);
    }

     //End Region Private Methods
}
