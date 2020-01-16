package nl.icsvertex.scansuite.Fragments.Inventory;


import android.annotation.SuppressLint;

import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.text.TextUtils;

import android.text.TextWatcher;
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

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import java.util.Objects;

import ICS.Interfaces.iICSDefaultFragment;
import ICS.Utils.Scanning.cBarcodeScan;

import ICS.Utils.cNumberTextWatcher;
import ICS.Utils.cText;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Basics.BarcodeLayouts.cBarcodeLayout;
import SSU_WHS.Basics.Settings.cSetting;
import SSU_WHS.Inventory.InventoryOrders.cInventoryorder;
import SSU_WHS.Inventory.InventoryorderBarcodes.cInventoryorderBarcode;
import SSU_WHS.Inventory.InventoryorderBins.cInventoryorderBin;
import SSU_WHS.Inventory.InventoryorderLineBarcodes.cInventoryorderLineBarcode;
import SSU_WHS.Inventory.InventoryorderLines.cInventoryorderLine;
import nl.icsvertex.scansuite.Activities.Inventory.InventoryorderBinActivity;
import nl.icsvertex.scansuite.Fragments.Dialogs.BarcodeFragment;
import nl.icsvertex.scansuite.R;

public class InventoryArticleDetailFragment extends DialogFragment implements iICSDefaultFragment {

    //Region Public Properties
    //End Region Public Properties

    //Region Private
    private static final String BARCODEFRAGMENT_TAG = "BARCODERAGMENT_TAG";
    private static int inventoryCounterMinusHelperInt;
    private static int inventoryCounterPlusHelperInt;
    private static Handler minusHandler;
    private static Handler plusHandler;

    private static ImageView toolbarImage;
    private static TextView toolbarTitle;

    private static ImageView articleThumbImageView;

    private static TextView binText;
    private static EditText quantityText;
    private static AppCompatImageButton imageButtonMinus;
    private static AppCompatImageButton imageButtonPlus;

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
    private static ConstraintLayout probeerselContainer;


    //End Region Private Properties

    //Region Constructor
    public InventoryArticleDetailFragment() {

    }

    //End Region Constructor

    //Region Default Methods

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_inventoryarticle_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        cAppExtension.dialogFragment = this;
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

        InventoryArticleDetailFragment.toolbarImage = Objects.requireNonNull(getView()).findViewById(R.id.toolbarImage);
        InventoryArticleDetailFragment.toolbarTitle = getView().findViewById(R.id.toolbarTitle);
        InventoryArticleDetailFragment.articleThumbImageView = getView().findViewById(R.id.articleThumbImageView);

        InventoryArticleDetailFragment.binText = getView().findViewById(R.id.binText);
        InventoryArticleDetailFragment.quantityText = getView().findViewById(R.id.quantityText);
        InventoryArticleDetailFragment.imageButtonMinus = getView().findViewById(R.id.imageButtonMinus);
        InventoryArticleDetailFragment.imageButtonPlus = getView().findViewById(R.id.imageButtonPlus);
        InventoryArticleDetailFragment.imageButtonBarcode = getView().findViewById(R.id.imageButtonBarcode);

        InventoryArticleDetailFragment.articleDescriptionText = getView().findViewById(R.id.articleDescriptionText);
        InventoryArticleDetailFragment.articleDescription2Text = getView().findViewById(R.id.articleDescription2Text);
        InventoryArticleDetailFragment.articleItemText = getView().findViewById(R.id.articleItemText);
        InventoryArticleDetailFragment.articleBarcodeText = getView().findViewById(R.id.articleBarcodeText);
        InventoryArticleDetailFragment.articleUnitOfMeasureText = getView().findViewById(R.id.articleUnitOfMeasureText);
        InventoryArticleDetailFragment.articleVendorItemText = getView().findViewById(R.id.articleVendorItemText);

        InventoryArticleDetailFragment.genericItemExtraField1Text = getView().findViewById(R.id.genericItemExtraField1Text);
        InventoryArticleDetailFragment.genericItemExtraField2Text = getView().findViewById(R.id.genericItemExtraField2Text);
        InventoryArticleDetailFragment.genericItemExtraField3Text = getView().findViewById(R.id.genericItemExtraField3Text);
        InventoryArticleDetailFragment.genericItemExtraField4Text = getView().findViewById(R.id.genericItemExtraField4Text);
        InventoryArticleDetailFragment.genericItemExtraField5Text = getView().findViewById(R.id.genericItemExtraField5Text);
        InventoryArticleDetailFragment.genericItemExtraField6Text = getView().findViewById(R.id.genericItemExtraField6Text);
        InventoryArticleDetailFragment.genericItemExtraField7Text = getView().findViewById(R.id.genericItemExtraField7Text);
        InventoryArticleDetailFragment.genericItemExtraField8Text = getView().findViewById(R.id.genericItemExtraField8Text);

        InventoryArticleDetailFragment.probeerselContainer = getView().findViewById(R.id.probeersel);

        InventoryArticleDetailFragment.imageButtonDone = getView().findViewById(R.id.imageButtonDone);
    }

    @Override
    public void mFieldsInitialize() {

        if (!cInventoryorder.currentInventoryOrder.isInvAmountManualBln()) {
            InventoryArticleDetailFragment.imageButtonBarcode.setVisibility(View.INVISIBLE);
        } else {
            InventoryArticleDetailFragment.imageButtonBarcode.setVisibility(View.VISIBLE);
        }

        InventoryArticleDetailFragment.inventoryCounterPlusHelperInt = 0;
        InventoryArticleDetailFragment. inventoryCounterMinusHelperInt = 0;

        InventoryArticleDetailFragment.articleDescriptionText.setText(cInventoryorderLine.currentInventoryOrderLine.getDescriptionStr());
        InventoryArticleDetailFragment.articleDescription2Text.setText(cInventoryorderLine.currentInventoryOrderLine.getDescription2Str());
        InventoryArticleDetailFragment.articleItemText.setText(cInventoryorderLine.currentInventoryOrderLine.getItemNoAndVariantCodeStr());

        InventoryArticleDetailFragment.articleVendorItemText.setText(cInventoryorderLine.currentInventoryOrderLine.getVendorItemNoAndVendorDescriptionStr());

        InventoryArticleDetailFragment.binText.setText(cInventoryorderBin.currentInventoryOrderBin.getBinCodeStr());

        InventoryArticleDetailFragment.quantityText.setSelectAllOnFocus(true);
        InventoryArticleDetailFragment.quantityText.requestFocus();
        InventoryArticleDetailFragment.quantityText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_TEXT_VARIATION_NORMAL);
        InventoryArticleDetailFragment.quantityText.setCursorVisible(false);

        TextWatcher tw = new cNumberTextWatcher(InventoryArticleDetailFragment.quantityText, 2,999999d);
        InventoryArticleDetailFragment.quantityText.addTextChangedListener((tw));
        InventoryArticleDetailFragment.quantityText.setText(cText.pDoubleToStringStr(cInventoryorderLine.currentInventoryOrderLine.getQuantityHandledDbl()));

        InventoryArticleDetailFragment.mShowArticleImage();
        InventoryArticleDetailFragment.mShowOrHideGenericExtraFields();
        InventoryArticleDetailFragment.mShowBarcodeInfo();

    }

    @Override
    public void mSetListeners() {
             this.mSetDoneListener();

        if (cSetting.INV_AMOUNT_MANUAL()) {
            this.mSetPlusListener();
            this.mSetMinusListener();
            this.mSetEditorActionListener();
            this.mSetImageButtonBarcodeListener();
        }
    }

    //End Region iICSDefaultActivity defaults


    //Region Public Methods

    public static void pHandleScan(cBarcodeScan pvBarcodeScan){

        //BIN scans are allowed
        if (cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(),cBarcodeLayout.barcodeLayoutEnu.BIN)) {

            //Click done
            InventoryArticleDetailFragment.imageButtonDone.performClick();

            //Pass this scan to the BIN activity
            InventoryorderBinActivity.pHandleScan(pvBarcodeScan);
            return;
        }

        //Only ARTICLE scans are allowed
        if (!cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(),cBarcodeLayout.barcodeLayoutEnu.ARTICLE)) {
            mDoUnknownScan(cAppExtension.context.getString(R.string.error_article_scan_mandatory));
            return;
        }

        //Check if the scanned value belongs to this line
        if (!InventoryArticleDetailFragment.mCheckBarcodeWithLineBarcodesBln(pvBarcodeScan)) {

            //Close this fragment, we are done here
            InventoryArticleDetailFragment.mHandleDone();

            //Handle scan in BIN activity,
            InventoryorderBinActivity.pHandleScan(pvBarcodeScan);
            return;
        }

        //Try to raise quantityDbl
        InventoryArticleDetailFragment.mTryToChangeInventoryQuantity(true, false,cInventoryorderBarcode.currentInventoryOrderBarcode.getQuantityPerUnitOfMeasureDbl() );

    }

    //End Region Public Methods

    //Region Private Methods


    private static boolean mCheckBarcodeWithLineBarcodesBln(cBarcodeScan pvBarcodeScan){

        //If scanned value matches the current barcode, then we have a match
        if (pvBarcodeScan.getBarcodeOriginalStr().equalsIgnoreCase(cInventoryorderLineBarcode.currentInventoryorderLineBarcode.getBarcodeStr()) ||
           pvBarcodeScan.getBarcodeFormattedStr().equalsIgnoreCase(cInventoryorderBarcode.currentInventoryOrderBarcode.getBarcodeWithoutCheckDigitStr()) ) {
            //We have a match, so leave
            return  true;
        }

        //Check if this is a barcode we already know
        cInventoryorderBarcode inventoryorderBarcode = cInventoryorder.currentInventoryOrder.pGetOrderBarcode(pvBarcodeScan);

        //We scanned a barcode unknown to the order
        if (inventoryorderBarcode == null) {
            return false;
        }

        //We scanned a barcode for a different article
        if (!inventoryorderBarcode.getItemNoStr().equalsIgnoreCase(cInventoryorderBarcode.currentInventoryOrderBarcode.getItemNoStr()) ||
            ! inventoryorderBarcode.getVariantCodeStr().equalsIgnoreCase(cInventoryorderBarcode.currentInventoryOrderBarcode.getVariantCodeStr())) {
            return false;
        }

        //We scanned a barcode that belongs to the current article, so check if we already have a line barcode
        for (cInventoryorderLineBarcode inventoryorderLineBarcode : cInventoryorderLine.currentInventoryOrderLine.lineBarcodesObl()) {

            //We have a match, so set
            if (inventoryorderLineBarcode.getBarcodeStr().equalsIgnoreCase(pvBarcodeScan.getBarcodeOriginalStr()) ||
               inventoryorderLineBarcode.getBarcodeStr().equalsIgnoreCase(pvBarcodeScan.getBarcodeFormattedStr())) {
                cInventoryorderLineBarcode.currentInventoryorderLineBarcode = inventoryorderLineBarcode;
                cInventoryorderBarcode.currentInventoryOrderBarcode = inventoryorderBarcode;
                InventoryArticleDetailFragment.mShowBarcodeInfo();
                return  true;
            }
        }

        //Scanned barcode is correct, but we need to create a line barcode
        cInventoryorderLineBarcode.currentInventoryorderLineBarcode =  cInventoryorderLine.currentInventoryOrderLine.pAddLineBarcode(inventoryorderBarcode.getBarcodeStr(),inventoryorderBarcode.getQuantityPerUnitOfMeasureDbl());
        cInventoryorderBarcode.currentInventoryOrderBarcode = inventoryorderBarcode;
        InventoryArticleDetailFragment.mShowBarcodeInfo();
        return  true;

    }

    private void mSetToolbar() {

        InventoryArticleDetailFragment.toolbarTitle.setText(cAppExtension.activity.getString(R.string.message_scan_article));
        InventoryArticleDetailFragment.toolbarTitle.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        InventoryArticleDetailFragment.toolbarTitle.setSingleLine(true);
        InventoryArticleDetailFragment.toolbarTitle.setMarqueeRepeatLimit(5);
        InventoryArticleDetailFragment.toolbarTitle.postDelayed(new Runnable() {
            @Override
            public void run() {
                toolbarTitle.setSelected(true);
            }
        },1500);

        InventoryArticleDetailFragment.toolbarImage.setImageResource(R.drawable.ic_info);

    }

    @SuppressLint("ClickableViewAccessibility")
    private void mSetPlusListener() {

        InventoryArticleDetailFragment.imageButtonPlus.setOnTouchListener(new View.OnTouchListener() {

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
                    inventoryCounterPlusHelperInt = 0;
                }

                return false;
            }
        });

        InventoryArticleDetailFragment.imageButtonPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //There is no selected barcode, select one first
                if (cInventoryorderBarcode.currentInventoryOrderBarcode == null) {
                    cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.message_select_one_of_multiple_barcodes),null);
                    return;
                }

                mTryToChangeInventoryQuantity(true, false, cInventoryorderBarcode.currentInventoryOrderBarcode.getQuantityPerUnitOfMeasureDbl());
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void mSetMinusListener() {

        InventoryArticleDetailFragment.imageButtonMinus.setOnTouchListener(new View.OnTouchListener() {
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
                    inventoryCounterMinusHelperInt = 0;
                }
                return false;
            }

        });

        InventoryArticleDetailFragment.imageButtonMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //There is no selected barcode, select one first
                if (cInventoryorderBarcode.currentInventoryOrderBarcode == null) {
                    cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.message_select_one_of_multiple_barcodes),null);
                    return;
                }
                mTryToChangeInventoryQuantity(false, false, cInventoryorderBarcode.currentInventoryOrderBarcode.getQuantityPerUnitOfMeasureDbl());
            }
        });
    }

    private void mSetEditorActionListener() {


        InventoryArticleDetailFragment.quantityText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InventoryArticleDetailFragment.quantityText.requestFocus();
                InventoryArticleDetailFragment.quantityText.setSelection(0,InventoryArticleDetailFragment.quantityText.getText().toString().length());
            }
        });


        InventoryArticleDetailFragment.quantityText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE || i == EditorInfo.IME_ACTION_GO ) {


                  InventoryArticleDetailFragment.mTryToChangeInventoryQuantity(true,
                                                                               true,
                                                                                cText.pStringToDoubleDbl(String.valueOf(InventoryArticleDetailFragment.quantityText.getText())));

                    InventoryArticleDetailFragment.imageButtonDone.callOnClick();
                }
                return true;
            }
        });
    }

    private void mSetDoneListener(){

        InventoryArticleDetailFragment.imageButtonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               InventoryArticleDetailFragment.mHandleDone();
            }
        });

    }

    private void mDoDelayedMinus(Runnable pvRunnable, long pvMilliSecsLng) {
        InventoryArticleDetailFragment.minusHandler.postDelayed(pvRunnable, pvMilliSecsLng);
        InventoryArticleDetailFragment.inventoryCounterMinusHelperInt += 1;
    }

    private void mDoDelayedPlus(Runnable pvRunnable, long pvMilliSecsLng) {
        InventoryArticleDetailFragment.plusHandler.postDelayed(pvRunnable, pvMilliSecsLng);
        InventoryArticleDetailFragment.inventoryCounterPlusHelperInt += 1;
    }

    private static void mTryToChangeInventoryQuantity(Boolean pvIsPositiveBln, Boolean pvAmountFixedBln, double pvAmountDbl) {


        if (pvIsPositiveBln) {

            //Determine the new amount
            if (pvAmountFixedBln) {
                cInventoryorderLine.currentInventoryOrderLine.quantityHandledDbl = pvAmountDbl;
                cInventoryorderLineBarcode.currentInventoryorderLineBarcode.quantityHandledDbl =  pvAmountDbl;
            } else {
                cInventoryorderLine.currentInventoryOrderLine.quantityHandledDbl += pvAmountDbl;
                cInventoryorderLineBarcode.currentInventoryorderLineBarcode.quantityHandledDbl += pvAmountDbl;
            }

            InventoryArticleDetailFragment.quantityText.setText(cText.pDoubleToStringStr(cInventoryorderLine.currentInventoryOrderLine.getQuantityHandledDbl()));

            return;
        }

        //negative

        //Check if value already is zero
        if (cInventoryorderLine.currentInventoryOrderLine.quantityHandledDbl < 1 ) {

            //If we have a decimal, correct it to zero
            if (cInventoryorderLine.currentInventoryOrderLine.quantityHandledDbl > 0 ) {
                pvAmountDbl = 0;
                pvAmountFixedBln = true;

            } else {
                cUserInterface.pDoNope(InventoryArticleDetailFragment.quantityText, true, true);
                return;
            }
        }

        if (pvAmountDbl < 0) {
            cUserInterface.pDoNope(InventoryArticleDetailFragment.quantityText, true, true);
            return;
        }

        //Determine the new amount
        if (pvAmountFixedBln) {
            cInventoryorderLine.currentInventoryOrderLine.quantityHandledDbl = pvAmountDbl;
            cInventoryorderLineBarcode.currentInventoryorderLineBarcode.quantityHandledDbl =  pvAmountDbl;
        }else {
            cInventoryorderLine.currentInventoryOrderLine.quantityHandledDbl -= pvAmountDbl;
            cInventoryorderLineBarcode.currentInventoryorderLineBarcode.quantityHandledDbl -= pvAmountDbl;
        }

        //Change quantityDbl in activity
        InventoryArticleDetailFragment.quantityText.setText(cText.pDoubleToStringStr(cInventoryorderLine.currentInventoryOrderLine.getQuantityHandledDbl()));

    }

    private static void mShowArticleImage() {

        //If pick with picture is false, then hide image view
        if (!cInventoryorder.currentInventoryOrder.isInventoryWithPictureBln()) {
            InventoryArticleDetailFragment.articleThumbImageView.setVisibility(View.GONE);
            return;
        }

        //If picture is not in cache (via webservice) then show no image
        if (!cInventoryorderLine.currentInventoryOrderLine.pGetArticleImageBln()) {
            cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.could_not_get_article_image), null);
            InventoryArticleDetailFragment.articleThumbImageView.setImageDrawable(ContextCompat.getDrawable(cAppExtension.context, R.drawable.ic_no_image_lightgrey_24dp));
            return;
        }

        //If picture is in cache but can't be converted, then show no image
        if (cInventoryorderLine.currentInventoryOrderLine.articleImage == null || cInventoryorderLine.currentInventoryOrderLine.articleImage.imageBitmap() == null) {
            cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.could_not_get_article_image), null);
            InventoryArticleDetailFragment.articleThumbImageView.setImageDrawable(ContextCompat.getDrawable(cAppExtension.context, R.drawable.ic_no_image_lightgrey_24dp));
            return;
        }

        //Show the image
        InventoryArticleDetailFragment.articleThumbImageView.setImageBitmap(cInventoryorderLine.currentInventoryOrderLine.articleImage.imageBitmap());

    }

    private static void mShowOrHideGenericExtraFields() {

        if (!cInventoryorderLine.currentInventoryOrderLine.getExtraField1Str().isEmpty()) {
            genericItemExtraField1Text.setVisibility(View.VISIBLE);
            genericItemExtraField1Text.setText(cInventoryorderLine.currentInventoryOrderLine.getExtraField1Str());
        }
        if (!cInventoryorderLine.currentInventoryOrderLine.getExtraField2Str().isEmpty()) {
            genericItemExtraField2Text.setVisibility(View.VISIBLE);
            genericItemExtraField2Text.setText(cInventoryorderLine.currentInventoryOrderLine.getExtraField2Str());
        }
        if (!cInventoryorderLine.currentInventoryOrderLine.getExtraField3Str().isEmpty()) {
            genericItemExtraField3Text.setVisibility(View.VISIBLE);
            genericItemExtraField3Text.setText(cInventoryorderLine.currentInventoryOrderLine.getExtraField3Str());
        }
        if (!cInventoryorderLine.currentInventoryOrderLine.getExtraField4Str().isEmpty()) {
            genericItemExtraField4Text.setVisibility(View.VISIBLE);
            genericItemExtraField4Text.setText(cInventoryorderLine.currentInventoryOrderLine.getExtraField4Str());
        }
        if (!cInventoryorderLine.currentInventoryOrderLine.getExtraField5Str().isEmpty()) {
            genericItemExtraField5Text.setVisibility(View.VISIBLE);
            genericItemExtraField5Text.setText(cInventoryorderLine.currentInventoryOrderLine.getExtraField5Str());
        }
        if (!cInventoryorderLine.currentInventoryOrderLine.getExtraField6Str().isEmpty()) {
            genericItemExtraField6Text.setVisibility(View.VISIBLE);
            genericItemExtraField6Text.setText(cInventoryorderLine.currentInventoryOrderLine.getExtraField6Str());
        }
        if (!cInventoryorderLine.currentInventoryOrderLine.getExtraField7Str().isEmpty()) {
            genericItemExtraField7Text.setVisibility(View.VISIBLE);
            genericItemExtraField7Text.setText(cInventoryorderLine.currentInventoryOrderLine.getExtraField7Str());
        }
        if (!cInventoryorderLine.currentInventoryOrderLine.getExtraField8Str().isEmpty()) {
            genericItemExtraField8Text.setVisibility(View.VISIBLE);
            genericItemExtraField8Text.setText(cInventoryorderLine.currentInventoryOrderLine.getExtraField8Str());
        }
    }

    private static void mHandleDone() {

        //Try to save the line to the database
        if (!cInventoryorderLine.currentInventoryOrderLine.pSaveLineViaWebserviceBln()) {
            cUserInterface.pDoExplodingScreen(cAppExtension.activity.getString(R.string.message_line_save_failed),"",true,true);
            return;
        }

        //Change quantityDbl handled in database
        cInventoryorderLine.currentInventoryOrderLine.pUpdateQuantityInDatabaseBln();

       cUserInterface.pHideGettingData();
       InventoryorderBinActivity.pLineHandled();
       cAppExtension.dialogFragment.dismiss();
       InventoryorderBinActivity.pHandleAddArticleFragmentDismissed();

    }

    private static void mDoUnknownScan(String pvErrorMessageStr) {
        cUserInterface.pShowSnackbarMessage(InventoryArticleDetailFragment.probeerselContainer,pvErrorMessageStr,null,true);
    }

    private Runnable mMinusAction = new Runnable() {
        @Override
        public void run() {
            imageButtonMinus.performClick();
            long milliSecsLng;
            if (inventoryCounterMinusHelperInt < 10) {
                milliSecsLng = 200;
            } else if (inventoryCounterMinusHelperInt < 20) {
                milliSecsLng = 150;
            } else if (inventoryCounterMinusHelperInt < 30) {
                milliSecsLng = 100;
            } else if (inventoryCounterMinusHelperInt < 40) {
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
            if (inventoryCounterPlusHelperInt < 10) {
                milliSecsLng = 200;
            } else if (inventoryCounterPlusHelperInt < 20) {
                milliSecsLng = 150;
            } else if (inventoryCounterPlusHelperInt < 30) {
                milliSecsLng = 100;
            } else if (inventoryCounterPlusHelperInt < 40) {
                milliSecsLng = 50;
            } else {
                milliSecsLng = 50;
            }
            mDoDelayedPlus(this, milliSecsLng);
        }
    };

    private static void mShowBarcodeInfo(){


        if (cInventoryorderBarcode.currentInventoryOrderBarcode != null) {
            InventoryArticleDetailFragment.articleBarcodeText.setText(cInventoryorderBarcode.currentInventoryOrderBarcode.getBarcodeAndQuantityStr());
            InventoryArticleDetailFragment.articleUnitOfMeasureText.setText(cInventoryorderBarcode.currentInventoryOrderBarcode.getUnitOfMeasureStr());
        } else {
            InventoryArticleDetailFragment.articleBarcodeText.setText(cAppExtension.context.getString(R.string.message_unknown_barcode));
            InventoryArticleDetailFragment.articleUnitOfMeasureText.setText("");
        }

    }

    private void mSetImageButtonBarcodeListener() {
        InventoryArticleDetailFragment.imageButtonBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View pvView) {

                if (cInventoryorderLine.currentInventoryOrderLine.barcodesObl() == null || cInventoryorderLine.currentInventoryOrderLine.barcodesObl().size() == 0) {
                    return;
                }

                //If we only have one barcode, then automatticaly select that barcode
                if (cInventoryorderLine.currentInventoryOrderLine.barcodesObl().size() == 1) {
                    InventoryArticleDetailFragment.pHandleScan(cBarcodeScan.pFakeScan(cInventoryorderLine.currentInventoryOrderLine.barcodesObl().get(0).getBarcodeStr()));
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
