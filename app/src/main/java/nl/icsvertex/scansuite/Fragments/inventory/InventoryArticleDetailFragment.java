package nl.icsvertex.scansuite.Fragments.inventory;


import android.annotation.SuppressLint;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import ICS.Interfaces.iICSDefaultFragment;
import ICS.Utils.Scanning.cBarcodeScan;

import ICS.Utils.cRegex;
import ICS.Utils.cText;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Basics.ArticleImages.cArticleImage;
import SSU_WHS.Basics.BarcodeLayouts.cBarcodeLayout;
import SSU_WHS.Basics.Settings.cSetting;
import SSU_WHS.Inventory.InventoryOrders.cInventoryorder;
import SSU_WHS.Inventory.InventoryorderBarcodes.cInventoryorderBarcode;
import SSU_WHS.Inventory.InventoryorderBins.cInventoryorderBin;
import SSU_WHS.Inventory.InventoryorderLineBarcodes.cInventoryorderLineBarcode;
import SSU_WHS.Inventory.InventoryorderLines.cInventoryorderLine;
import nl.icsvertex.scansuite.Activities.inventory.InventoryorderBinActivity;
import nl.icsvertex.scansuite.R;

public class InventoryArticleDetailFragment extends DialogFragment implements iICSDefaultFragment {

    //Region Public Properties

    //End Region Public Properties

    //Region Private Properties
    private static int inventoryCounterMinusHelperInt;
    private static int inventoryCounterPlusHelperInt;
    private static Handler minusHandler;
    private static Handler plusHandler;

    private ImageView toolbarImage;
    private  TextView toolbarTitle;
    private TextView toolbarSubtext;
    private ImageView toolbarImageHelp;

    private static ImageView articleThumbImageView;
    private TextView binText;
    private static TextView quantityText;
    private static AppCompatImageButton imageButtonMinus;
    private static AppCompatImageButton imageButtonPlus;

    //Region article info
    private TextView articleDescriptionText;
    private TextView articleDescription2Text;
    private TextView articleItemText;
    private TextView articleBarcodeText;
    private TextView articleVendorItemText;

    private static TextView genericItemExtraField1Text;
    private static TextView genericItemExtraField2Text;
    private static TextView genericItemExtraField3Text;
    private static TextView genericItemExtraField4Text;
    private static TextView genericItemExtraField5Text;
    private static TextView genericItemExtraField6Text;
    private static TextView genericItemExtraField7Text;
    private static TextView genericItemExtraField8Text;

    private static ImageView imageButtonDone;

    //End Region article info

    //End Region Private Properties


    //Region Constructor
    public InventoryArticleDetailFragment() {
        // Required empty public constructor
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
        try {
            cBarcodeScan.pUnregisterBarcodeFragmentReceiver();

            if (cAppExtension.activity instanceof InventoryorderBinActivity) {
                cBarcodeScan.pRegisterBarcodeReceiver();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        super.onPause();
    }

    @Override
    public void onDestroy() {
        try {
            cBarcodeScan.pUnregisterBarcodeFragmentReceiver();

            if (cAppExtension.activity instanceof InventoryorderBinActivity) {
                cBarcodeScan.pRegisterBarcodeReceiver();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels - getResources().getDimensionPixelSize(R.dimen.default_double_margin);

        getDialog().getWindow().setLayout(width, height);
        cBarcodeScan.pRegisterBarcodeFragmentReceiver();
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

        this.toolbarImage = getView().findViewById(R.id.toolbarImage);
        this.toolbarTitle = getView().findViewById(R.id.toolbarTitle);
        this.toolbarSubtext = getView().findViewById(R.id.toolbarSubtext);
        this.toolbarImageHelp = getView().findViewById(R.id.toolbarImageHelp);

        this.articleThumbImageView = getView().findViewById(R.id.articleThumbImageView);
        this.binText = getView().findViewById(R.id.binText);
        this.quantityText = getView().findViewById(R.id.quantityText);
        this.imageButtonMinus = getView().findViewById(R.id.imageButtonMinus);
        this.imageButtonPlus = getView().findViewById(R.id.imageButtonPlus);

        this.articleDescriptionText = getView().findViewById(R.id.articleDescriptionText);
        this.articleDescription2Text = getView().findViewById(R.id.articleDescription2Text);
        this.articleItemText = getView().findViewById(R.id.articleItemText);
        this.articleBarcodeText = getView().findViewById(R.id.articleBarcodeText);
        this.articleVendorItemText = getView().findViewById(R.id.articleVendorItemText);

        this.genericItemExtraField1Text = getView().findViewById(R.id.genericItemExtraField1Text);
        this.genericItemExtraField2Text = getView().findViewById(R.id.genericItemExtraField2Text);
        this.genericItemExtraField3Text = getView().findViewById(R.id.genericItemExtraField3Text);
        this.genericItemExtraField4Text = getView().findViewById(R.id.genericItemExtraField4Text);
        this.genericItemExtraField5Text = getView().findViewById(R.id.genericItemExtraField5Text);
        this.genericItemExtraField6Text = getView().findViewById(R.id.genericItemExtraField6Text);
        this.genericItemExtraField7Text = getView().findViewById(R.id.genericItemExtraField7Text);
        this.genericItemExtraField8Text = getView().findViewById(R.id.genericItemExtraField8Text);

        InventoryArticleDetailFragment.imageButtonDone = getView().findViewById(R.id.imageButtonDone);
    }

    @Override
    public void mFieldsInitialize() {

        this.inventoryCounterPlusHelperInt = 0;
        this. inventoryCounterMinusHelperInt = 0;

        if (!cInventoryorderLine.currentInventoryOrderLine.getDescription2Str().isEmpty()) {
            this.toolbarSubtext.setText(cInventoryorderLine.currentInventoryOrderLine.getDescription2Str());
            this.toolbarSubtext.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            this.toolbarSubtext.setSingleLine(true);
            this.toolbarSubtext.setMarqueeRepeatLimit(5);
            this.toolbarSubtext.postDelayed(new Runnable() {
                @Override
                public void run() {
                    toolbarSubtext.setSelected(true);
                }
            },1500);
        }

        this.articleDescriptionText.setText(cInventoryorderLine.currentInventoryOrderLine.getDescriptionStr());
        this.articleDescription2Text.setText(cInventoryorderLine.currentInventoryOrderLine.getDescription2Str());
        this.articleItemText.setText(cInventoryorderLine.currentInventoryOrderLine.getItemNoStr() + " " + cInventoryorderLine.currentInventoryOrderLine.getVariantCodeStr());
        this.articleBarcodeText.setText("");
        this.articleVendorItemText.setText(cInventoryorderLine.currentInventoryOrderLine.getVendorItemNoStr() + ' ' + cInventoryorderLine.currentInventoryOrderLine.getVendorItemDescriptionStr());

        this. binText.setText(cInventoryorderBin.currentInventoryOrderBin.getBinCodeStr());
        this.quantityText.setText(cText.pDoubleToStringStr(cInventoryorderLine.currentInventoryOrderLine.getQuantityHandledDbl()));
        InventoryArticleDetailFragment.mShowArticleImage();
        InventoryArticleDetailFragment.mShowOrHideGenericExtraFields();
    }

    @Override
    public void mSetListeners() {
        this.mDismissListener();
        this.mSetPlusListener();
        this.mSetMinusListener();
        this.mSetDoneListener();
    }

    //End Region iICSDefaultActivity defaults


    //Region Public Methods

    public static void pHandleScan(cBarcodeScan pvBarcodeScan){

        //Only ARTICLE scans are allowed
        if (!cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(),cBarcodeLayout.barcodeLayoutEnu.ARTICLE)) {
            mDoUnknownScan(cAppExtension.context.getString(R.string.error_article_scan_mandatory), pvBarcodeScan.getBarcodeOriginalStr());
            return;
        }

        //Check if the scanned value belongs to this line
        if (!InventoryArticleDetailFragment.mCheckBarcodeWithLineBarcodesBln(pvBarcodeScan)) {
            mDoUnknownScan(cAppExtension.context.getString(R.string.error_barcode_doesnt_belong_to_line), pvBarcodeScan.getBarcodeOriginalStr());
            return;
        }

        //Try to raise quantity
        InventoryArticleDetailFragment.mTryToChangeInventoryQuantity(true, false,cInventoryorderBarcode.currentInventoryOrderBarcode.getQuantityPerUnitOfMeasureDbl() );

    }

    //End Region Public Methods

    //Region Private Methods

    private static boolean mCheckBarcodeWithLineBarcodesBln(cBarcodeScan pvBarcodeScan){

        //If scanned value matches the current barcode, then we have a match
        if (pvBarcodeScan.getBarcodeStr().equalsIgnoreCase(cInventoryorderLineBarcode.currentInventoryorderLineBarcode.getBarcodeStr()) ||
           pvBarcodeScan.getBarcodeOriginalStr().equalsIgnoreCase(cInventoryorderBarcode.currentInventoryOrderBarcode.getBarcodeStr()) ) {
            //We have a match, so leave
            return  true;
        }

        //Check if this is a barcode we already know
        cInventoryorderBarcode inventoryorderBarcode = cInventoryorder.currentInventoryOrder.pGetOrderBarcode(pvBarcodeScan);

        //We scanned a barcode unknown to the order
        if (inventoryorderBarcode == null) {
            cUserInterface.pDoExplodingScreen(cAppExtension.activity.getString(R.string.message_unknown_barcode),"",true,true);
            return false;
        }

        //We scanned a barcode for a different article
        if (!inventoryorderBarcode.getItemNoStr().equalsIgnoreCase(cInventoryorderBarcode.currentInventoryOrderBarcode.getItemNoStr()) ||
            ! inventoryorderBarcode.getVariantCodeStr().equalsIgnoreCase(cInventoryorderBarcode.currentInventoryOrderBarcode.getVariantCodeStr())) {
            cUserInterface.pDoExplodingScreen(cAppExtension.activity.getString(R.string.message_unknown_barcode),"",true,true);
            return false;

        }

        //We scanned a barcode that belongs to the current article, so check if we already have a line barcode
        for (cInventoryorderLineBarcode inventoryorderLineBarcode : cInventoryorderLine.currentInventoryOrderLine.lineBarcodesObl) {

            //We have a match, so set
            if (inventoryorderLineBarcode.getBarcodeStr().equalsIgnoreCase(pvBarcodeScan.getBarcodeStr()) ||
               inventoryorderLineBarcode.getBarcodeStr().equalsIgnoreCase(pvBarcodeScan.getBarcodeOriginalStr())) {
                cInventoryorderLineBarcode.currentInventoryorderLineBarcode = inventoryorderLineBarcode;
                cInventoryorderBarcode.currentInventoryOrderBarcode = inventoryorderBarcode;
                return true;
            }
        }

        return  false;

    }

    private void mSetToolbar() {

        this.toolbarTitle.setText(cInventoryorderLine.currentInventoryOrderLine.getDescriptionStr());
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
        this.toolbarImageHelp.setVisibility(View.GONE);
    }

    private void mDismissListener() {
        getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
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
                    inventoryCounterPlusHelperInt = 0;
                }

                return false;
            }
        });

        this.imageButtonPlus.setOnClickListener(new View.OnClickListener() {
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
                    inventoryCounterMinusHelperInt = 0;
                }
                return false;
            }

        });

        this.imageButtonMinus.setOnClickListener(new View.OnClickListener() {
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

    private void mSetDoneListener(){

        InventoryArticleDetailFragment.imageButtonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               InventoryArticleDetailFragment.mHandleDone();
            }
        });

    }

    private void mDoDelayedMinus(Runnable pvRunnable, long pvMilliSecsLng) {
        this.minusHandler.postDelayed(pvRunnable, pvMilliSecsLng);
        this.inventoryCounterMinusHelperInt += 1;
        return;
    }

    private void mDoDelayedPlus(Runnable pvRunnable, long pvMilliSecsLng) {
        this.plusHandler.postDelayed(pvRunnable, pvMilliSecsLng);
        this.inventoryCounterPlusHelperInt += 1;
        return;
    }

    private static void mTryToChangeInventoryQuantity(Boolean pvIsPositiveBln, Boolean pvAmountFixedBln, double pvAmountDbl) {

        double newQuantityDbl;

        if (pvIsPositiveBln) {

            //Determine the new amount
            if (pvAmountFixedBln == true) {
                cInventoryorderLine.currentInventoryOrderLine.quantityHandledDbl = pvAmountDbl;
                cInventoryorderLineBarcode.currentInventoryorderLineBarcode.quantityHandledDbl =  pvAmountDbl;
            } else {
                cInventoryorderLine.currentInventoryOrderLine.quantityHandledDbl += pvAmountDbl;
                cInventoryorderLineBarcode.currentInventoryorderLineBarcode.quantityHandledDbl += pvAmountDbl;
            }

            //Change quantity in activuty
            InventoryArticleDetailFragment.quantityText.setText(cText.pDoubleToStringStr(cInventoryorderLine.currentInventoryOrderLine.getQuantityHandledDbl()));
            return;
        }

        //negative

        //Check if value already is zero
        if (cInventoryorderLine.currentInventoryOrderLine.quantityHandledDbl == 0 ) {
            cUserInterface.pDoNope(InventoryArticleDetailFragment.quantityText, true, true);
            return;
        }

        //Determine the new amount
        if (pvAmountFixedBln == true) {
            newQuantityDbl = pvAmountDbl;
        }else {
            newQuantityDbl= cInventoryorderLine.currentInventoryOrderLine.getQuantityHandledDbl() - pvAmountDbl;
        }

        if (newQuantityDbl < 0) {
            newQuantityDbl = 0;
        }


        //Set the new quantity of the current line
        cInventoryorderLine.currentInventoryOrderLine.quantityHandledDbl = newQuantityDbl;

        //Change line barcode quantity
        cInventoryorderBarcode.currentInventoryOrderBarcode.quantityHandled -= pvAmountDbl;
        return;

    }

    private static void mShowArticleImage() {

        //If pick with picture is false, then hide image view

        //todo: do this differently
        if (cSetting.PICK_WITH_PICTURE() == false) {
            InventoryArticleDetailFragment.articleThumbImageView.setVisibility(View.INVISIBLE);
            return;
        }

        //If picture is not in cache (via webservice) then show no image
        if (cInventoryorderLine.currentInventoryOrderLine.pGetArticleImageBln()) {
            cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.could_not_get_article_image), null);
            InventoryArticleDetailFragment.articleThumbImageView.setImageDrawable(ContextCompat.getDrawable(cAppExtension.context, R.drawable.ic_no_image_lightgrey_24dp));
            return;
        }

        //If picture is in cache but can't be converted, then show no image
        if (cInventoryorderLine.currentInventoryOrderLine.articleImage == null ||cInventoryorderLine.currentInventoryOrderLine.articleImage.imageBitmap() == null) {
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

        //Change quantity handled in database
        cInventoryorderLine.currentInventoryOrderLine.pUpdateQuantityInDatabaseBln();

       cUserInterface.pHideGettingData();
        InventoryorderBinActivity.pLineHandled();
        cAppExtension.dialogFragment.dismiss();

    }

    private static void mDoUnknownScan(String pvErrorMessageStr, String pvScannedBarcodeStr) {
        cUserInterface.pDoExplodingScreen(pvErrorMessageStr, pvScannedBarcodeStr, true, true);
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


    //End Region Private Methods
}
