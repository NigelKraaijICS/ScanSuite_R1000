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
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import java.util.Objects;

import ICS.Interfaces.iICSDefaultFragment;
import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cNumberTextWatcher;
import ICS.Utils.cRegex;
import ICS.Utils.cText;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Basics.BarcodeLayouts.cBarcodeLayout;
import SSU_WHS.Basics.BranchBin.cBranchBin;
import SSU_WHS.Basics.Settings.cSetting;
import SSU_WHS.Basics.Users.cUser;
import SSU_WHS.General.cPublicDefinitions;
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
    private  int inventoryCounterMinusHelperInt;
    private  int inventoryCounterPlusHelperInt;
    private  Handler minusHandler;
    private  Handler plusHandler;

    private  ConstraintLayout inventoryArticleDetailContainer;

    private Toolbar toolbar;
    private  ImageView toolbarImage;
    private  TextView toolbarTitle;

    private  ImageView articleThumbImageView;

    private  TextView binText;
    private  EditText quantityText;
    private  AppCompatImageButton imageButtonMinus;
    private  AppCompatImageButton imageButtonPlus;

    private CardView articleContainer;
    private ConstraintLayout articleInfoContainer;

    private  TextView articleDescriptionText;
    private  TextView articleDescription2Text;
    private  TextView articleItemText;
    private  TextView articleBarcodeText;

    private  ImageView imageButtonDone;

    private  ImageButton imageButtonBarcode;


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
            cBarcodeScan.pUnregisterBarcodeFragmentReceiver(this.getClass().getSimpleName());
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
        cBarcodeScan.pRegisterBarcodeFragmentReceiver(this.getClass().getSimpleName());
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

        cBarcodeScan.pRegisterBarcodeFragmentReceiver(this.getClass().getSimpleName());
    }

    @Override
    public void mFindViews() {

        this.inventoryArticleDetailContainer = getView().findViewById(R.id.inventoryArticleDetailContainer);

        this.toolbar =  getView().findViewById(R.id.toolbar);
        this.toolbarImage = requireView().findViewById(R.id.toolbarImage);
        this.toolbarTitle = getView().findViewById(R.id.toolbarTitle);

        this.articleContainer = getView().findViewById(R.id.addressContainer);
        this.articleInfoContainer = getView().findViewById(R.id.articleInfoContainer);
        this.articleThumbImageView = getView().findViewById(R.id.articleThumbImageView);

        this.articleDescriptionText = getView().findViewById(R.id.articleDescriptionText);
        this.articleDescription2Text = getView().findViewById(R.id.articleDescription2Text);
        this.articleItemText = getView().findViewById(R.id.articleItemText);
        this.articleBarcodeText = getView().findViewById(R.id.articleBarcodeText);


        this.binText = getView().findViewById(R.id.binText);
        this.quantityText = getView().findViewById(R.id.quantityText);
        this.imageButtonMinus = getView().findViewById(R.id.imageButtonMinus);
        this.imageButtonPlus = getView().findViewById(R.id.imageButtonPlus);
        this.imageButtonBarcode = getView().findViewById(R.id.imageButtonBarcode);

        this.imageButtonDone = getView().findViewById(R.id.imageButtonDone);

    }

    @Override
    public void mFieldsInitialize() {

        if (!cInventoryorder.currentInventoryOrder.isInvAmountManualBln()) {
            this.imageButtonBarcode.setVisibility(View.INVISIBLE);
        } else {
            this.imageButtonBarcode.setVisibility(View.VISIBLE);
        }

        this.inventoryCounterPlusHelperInt = 0;
        this. inventoryCounterMinusHelperInt = 0;

        this.articleDescriptionText.setText(cInventoryorderLine.currentInventoryOrderLine.getDescriptionStr());
        this.articleDescription2Text.setText(cInventoryorderLine.currentInventoryOrderLine.getDescription2Str());
        this.articleItemText.setText(cInventoryorderLine.currentInventoryOrderLine.getItemNoAndVariantCodeStr());

        this.binText.setText(cInventoryorderBin.currentInventoryOrderBin.getBinCodeStr());

        this.quantityText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        this.quantityText.setSelectAllOnFocus(true);
        this.quantityText.requestFocus();
        this.quantityText.setSingleLine();
        this.quantityText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_TEXT_VARIATION_NORMAL);
        this.quantityText.setCursorVisible(false);

        TextWatcher tw = new cNumberTextWatcher(this.quantityText, 2,999999d);
        this.quantityText.addTextChangedListener((tw));
        this.quantityText.setText(cText.pDoubleToStringStr(cInventoryorderLine.currentInventoryOrderLine.getQuantityHandledDbl()));

        this.mShowArticleImage();
        this.mShowOrHideGenericExtraFields();
        this.mShowBarcodeInfo();

        this.mHideArticleInfo();

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

    public void pHandleScan(final cBarcodeScan pvBarcodeScan){

        boolean binCheckedBln = false;

        //This barcode matches multiple lay-outs so this can be a BIN or an article
        if (Objects.requireNonNull(cBarcodeLayout.pGetBarcodeLayoutByBarcodeObl(pvBarcodeScan.getBarcodeOriginalStr())).size() > 1) {

            //First check if this is a BIN
            cBranchBin branchBin =  cUser.currentUser.currentBranch.pGetBinByCode(cRegex.pStripRegexPrefixStr(pvBarcodeScan.getBarcodeOriginalStr()));

            if (branchBin != null) {
                //Click done
                this.imageButtonDone.performClick();

                //Pass this scan to the BIN activity
                if (cAppExtension.activity instanceof InventoryorderBinActivity) {
                    InventoryorderBinActivity inventoryorderBinActivity = (InventoryorderBinActivity)cAppExtension.activity;
                    inventoryorderBinActivity.pHandleScan(pvBarcodeScan, false);
                    dismiss();
                    return;
                }

            }

            binCheckedBln = true;

        }

        //BIN scans are allowed
        if (!binCheckedBln && cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(),cBarcodeLayout.barcodeLayoutEnu.BIN)) {

            //Click done
            this.imageButtonDone.performClick();

            if (cAppExtension.activity instanceof InventoryorderBinActivity) {

                cAppExtension.activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        InventoryorderBinActivity inventoryorderBinActivity = (InventoryorderBinActivity)cAppExtension.activity;
                        inventoryorderBinActivity.pHandleScan(pvBarcodeScan, true);
                    }
                });

            }

            dismiss();
            return;

        }

        //Only ARTICLE scans are allowed
        if (!cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(),cBarcodeLayout.barcodeLayoutEnu.ARTICLE)) {
            mDoUnknownScan(cAppExtension.context.getString(R.string.error_article_scan_mandatory));
            return;
        }

        //Check if the scanned value belongs to this line
        if (!this.mCheckBarcodeWithLineBarcodesBln(pvBarcodeScan)) {

            //Close this fragment, we are done here
            this.mHandleDone();

            if (cAppExtension.activity instanceof InventoryorderBinActivity) {

                cAppExtension.activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        InventoryorderBinActivity inventoryorderBinActivity = (InventoryorderBinActivity)cAppExtension.activity;
                        inventoryorderBinActivity.pHandleScan(pvBarcodeScan, true);
                    }
                });


                dismiss();
                return;
            }

        }

        //Try to raise quantityDbl
        this.mTryToChangeInventoryQuantity(true, false,cInventoryorderBarcode.currentInventoryOrderBarcode.getQuantityPerUnitOfMeasureDbl() );

    }

    //End Region Public Methods

    //Region Private Methods


    private  boolean mCheckBarcodeWithLineBarcodesBln(cBarcodeScan pvBarcodeScan){

        //If scanned value matches the current barcodeStr, then we have a match
        if (pvBarcodeScan.getBarcodeOriginalStr().equalsIgnoreCase(cInventoryorderLineBarcode.currentInventoryorderLineBarcode.getBarcodeStr()) ||
           pvBarcodeScan.getBarcodeFormattedStr().equalsIgnoreCase(cInventoryorderBarcode.currentInventoryOrderBarcode.getBarcodeWithoutCheckDigitStr()) ) {
            //We have a match, so leave
            return  true;
        }

        //Check if this is a barcodeStr we already know
        cInventoryorderBarcode inventoryorderBarcode = cInventoryorder.currentInventoryOrder.pGetOrderBarcode(pvBarcodeScan);

        //We scanned a barcodeStr unknown to the order
        if (inventoryorderBarcode == null) {
            return false;
        }

        //We scanned a barcodeStr for a different article
        if (!inventoryorderBarcode.getItemNoStr().equalsIgnoreCase(cInventoryorderBarcode.currentInventoryOrderBarcode.getItemNoStr()) ||
            ! inventoryorderBarcode.getVariantCodeStr().equalsIgnoreCase(cInventoryorderBarcode.currentInventoryOrderBarcode.getVariantCodeStr())) {
            return false;
        }

        //We scanned a barcodeStr that belongs to the current article, so check if we already have a line barcodeStr
        for (cInventoryorderLineBarcode inventoryorderLineBarcode : cInventoryorderLine.currentInventoryOrderLine.lineBarcodesObl()) {

            //We have a match, so set
            if (inventoryorderLineBarcode.getBarcodeStr().equalsIgnoreCase(pvBarcodeScan.getBarcodeOriginalStr()) ||
               inventoryorderLineBarcode.getBarcodeStr().equalsIgnoreCase(pvBarcodeScan.getBarcodeFormattedStr())) {
                cInventoryorderLineBarcode.currentInventoryorderLineBarcode = inventoryorderLineBarcode;
                cInventoryorderBarcode.currentInventoryOrderBarcode = inventoryorderBarcode;
                this.mShowBarcodeInfo();
                return  true;
            }
        }

        //Scanned barcodeStr is correct, but we need to create a line barcodeStr
        cInventoryorderLineBarcode.currentInventoryorderLineBarcode =  cInventoryorderLine.currentInventoryOrderLine.pAddLineBarcode(inventoryorderBarcode.getBarcodeStr(),inventoryorderBarcode.getQuantityPerUnitOfMeasureDbl());
        cInventoryorderBarcode.currentInventoryOrderBarcode = inventoryorderBarcode;
        this.mShowBarcodeInfo();
        return  true;

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
                    inventoryCounterPlusHelperInt = 0;
                }

                return false;
            }
        });

        this.imageButtonPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //There is no selected barcodeStr, select one first
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


                //There is no selected barcodeStr, select one first
                if (cInventoryorderBarcode.currentInventoryOrderBarcode == null) {
                    cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.message_select_one_of_multiple_barcodes),null);
                    return;
                }
                mTryToChangeInventoryQuantity(false, false, cInventoryorderBarcode.currentInventoryOrderBarcode.getQuantityPerUnitOfMeasureDbl());
            }
        });
    }

    private void mSetEditorActionListener() {


        this.quantityText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantityText.requestFocus();
                quantityText.setSelection(0,quantityText.getText().toString().length());
            }
        });


        this.quantityText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE || i == EditorInfo.IME_ACTION_GO ) {


                  mTryToChangeInventoryQuantity(true,
                                                                               true,
                                                                                cText.pStringToDoubleDbl(String.valueOf(quantityText.getText())));

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

    private void mDoDelayedMinus(Runnable pvRunnable, long pvMilliSecsLng) {
        this.minusHandler.postDelayed(pvRunnable, pvMilliSecsLng);
        this.inventoryCounterMinusHelperInt += 1;
    }

    private void mDoDelayedPlus(Runnable pvRunnable, long pvMilliSecsLng) {
        this.plusHandler.postDelayed(pvRunnable, pvMilliSecsLng);
        this.inventoryCounterPlusHelperInt += 1;
    }

    private void mTryToChangeInventoryQuantity(Boolean pvIsPositiveBln, Boolean pvAmountFixedBln, double pvAmountDbl) {


        if (pvIsPositiveBln) {

            //Determine the new amount
            if (pvAmountFixedBln) {
                cInventoryorderLine.currentInventoryOrderLine.quantityHandledDbl = pvAmountDbl;
                cInventoryorderLineBarcode.currentInventoryorderLineBarcode.quantityHandledDbl =  pvAmountDbl;
            } else {
                cInventoryorderLine.currentInventoryOrderLine.quantityHandledDbl += pvAmountDbl;
                cInventoryorderLineBarcode.currentInventoryorderLineBarcode.quantityHandledDbl += pvAmountDbl;
            }

            this.quantityText.setText(cText.pDoubleToStringStr(cInventoryorderLine.currentInventoryOrderLine.getQuantityHandledDbl()));

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
                cUserInterface.pDoNope(this.quantityText, true, true);
                return;
            }
        }

        if (pvAmountDbl < 0) {
            cUserInterface.pDoNope(this.quantityText, true, true);
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
        this.quantityText.setText(cText.pDoubleToStringStr(cInventoryorderLine.currentInventoryOrderLine.getQuantityHandledDbl()));

    }

    private  void mShowArticleImage() {

        //If pick with picture is false, then hide image view
        if (!cInventoryorder.currentInventoryOrder.isInventoryWithPictureBln()) {
            this.articleThumbImageView.setVisibility(View.GONE);
            return;
        }

        //If picture is not in cache (via webservice) then show no image
        if (!cInventoryorderLine.currentInventoryOrderLine.pGetArticleImageBln()) {
            cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.could_not_get_article_image), null);
            this.articleThumbImageView.setImageDrawable(ContextCompat.getDrawable(cAppExtension.context, R.drawable.ic_no_image_lightgrey_24dp));
            return;
        }

        //If picture is in cache but can't be converted, then show no image
        if (cInventoryorderLine.currentInventoryOrderLine.articleImage == null || cInventoryorderLine.currentInventoryOrderLine.articleImage.imageBitmap() == null) {
            cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.could_not_get_article_image), null);
            this.articleThumbImageView.setImageDrawable(ContextCompat.getDrawable(cAppExtension.context, R.drawable.ic_no_image_lightgrey_24dp));
            return;
        }

        //Show the image
        this.articleThumbImageView.setImageBitmap(cInventoryorderLine.currentInventoryOrderLine.articleImage.imageBitmap());

    }

    private  void mShowOrHideGenericExtraFields() {

    }

    private  void mHandleDone() {

        //Try to save the line to the database
        if (!cInventoryorderLine.currentInventoryOrderLine.pSaveLineViaWebserviceBln()) {
            cUserInterface.pDoExplodingScreen(cAppExtension.activity.getString(R.string.message_line_save_failed),"",true,true);
            return;
        }

        //Change quantityDbl handled in database
        cInventoryorderLine.currentInventoryOrderLine.pUpdateQuantityInDatabase();

       cUserInterface.pHideGettingData();

        if (cAppExtension.activity instanceof InventoryorderBinActivity) {
            InventoryorderBinActivity inventoryorderBinActivity = (InventoryorderBinActivity)cAppExtension.activity;
            inventoryorderBinActivity.pLineHandled();
            cAppExtension.dialogFragment.dismiss();
            inventoryorderBinActivity.pHandleAddArticleFragmentDismissed();
        }
    }

    private  void mDoUnknownScan(String pvErrorMessageStr) {
        cUserInterface.pShowSnackbarMessage(this.inventoryArticleDetailContainer,pvErrorMessageStr,null,true);
    }

    private void mHideArticleInfo(){

        this.articleInfoContainer.setVisibility(View.GONE);
        ConstraintLayout.LayoutParams newCardViewLayoutParams = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        newCardViewLayoutParams.setMargins(15,15,15,15);
        this.articleContainer.setLayoutParams(newCardViewLayoutParams);

        ConstraintSet constraintSetSpace = new ConstraintSet();
        constraintSetSpace.clone(this.inventoryArticleDetailContainer);
        constraintSetSpace.connect(this.articleContainer.getId(), ConstraintSet.TOP, toolbar.getId(), ConstraintSet.BOTTOM);
        constraintSetSpace.applyTo(this.inventoryArticleDetailContainer);

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

    private  void mShowBarcodeInfo(){


        if (cInventoryorderBarcode.currentInventoryOrderBarcode != null) {
            this.articleBarcodeText.setText(cInventoryorderBarcode.currentInventoryOrderBarcode.getBarcodeAndQuantityStr() + " " + cInventoryorderBarcode.currentInventoryOrderBarcode.getUnitOfMeasureStr());
        } else {
            this.articleBarcodeText.setText(cAppExtension.context.getString(R.string.message_unknown_barcode));
        }

    }

    private void mSetImageButtonBarcodeListener() {
        this.imageButtonBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View pvView) {

                if (cInventoryorderLine.currentInventoryOrderLine.barcodesObl() == null || cInventoryorderLine.currentInventoryOrderLine.barcodesObl().size() == 0) {
                    return;
                }

                //If we only have one barcodeStr, then automatticaly select that barcodeStr
                if (cInventoryorderLine.currentInventoryOrderLine.barcodesObl().size() == 1) {
                    pHandleScan(cBarcodeScan.pFakeScan(cInventoryorderLine.currentInventoryOrderLine.barcodesObl().get(0).getBarcodeStr()));
                    return;
                }

                mShowBarcodeSelectFragment();

            }
        });
    }

    private void mShowBarcodeSelectFragment() {
        BarcodeFragment barcodeFragment = new BarcodeFragment();
        barcodeFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.BARCODEFRAGMENT_LIST_TAG);
    }

    //End Region Private Methods
}
