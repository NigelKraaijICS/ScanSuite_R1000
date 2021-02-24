package nl.icsvertex.scansuite.Activities.Inventory;


import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
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
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import ICS.Interfaces.iICSDefaultActivity;
import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cText;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Basics.BarcodeLayouts.cBarcodeLayout;
import SSU_WHS.Basics.Settings.cSetting;
import SSU_WHS.General.cPublicDefinitions;
import SSU_WHS.Inventory.InventoryOrders.cInventoryorder;
import SSU_WHS.Inventory.InventoryorderBarcodes.cInventoryorderBarcode;
import SSU_WHS.Inventory.InventoryorderBins.cInventoryorderBin;
import SSU_WHS.Inventory.InventoryorderLineBarcodes.cInventoryorderLineBarcode;
import SSU_WHS.Inventory.InventoryorderLines.cInventoryorderLine;
import nl.icsvertex.scansuite.Fragments.Dialogs.BarcodeFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.NumberpickerFragment;
import nl.icsvertex.scansuite.R;

public class InventoryArticleActivity extends AppCompatActivity implements iICSDefaultActivity {

    //Region Public Properties
    //End Region Public Properties

    //Region Private

    private  ConstraintLayout inventoryArticleDetailContainer;

    private Toolbar toolbar;
    private ImageView toolbarImage;
    private TextView toolbarTitle;
    private TextView toolbarSubTitle;

    private CardView articleContainer;
    private ConstraintLayout articleInfoContainer;
    private  TextView articleDescriptionText;
    private  TextView articleDescription2Text;
    private  TextView articleItemText;
    private  TextView articleBarcodeText;
    private  ImageView articleThumbImageView;

    private TextView binText;

    private TextView quantityText;
    private TextView quantityRequiredText;
    private AppCompatImageButton imageButtonMinus;
    private AppCompatImageButton imageButtonPlus;

    private  int inventoryCounterMinusHelperInt;
    private  int inventoryCounterPlusHelperInt;
    private  Handler minusHandler;
    private  Handler plusHandler;

    private  ImageView imageButtonDone;
    private  ImageButton imageButtonBarcode;
    private ImageView imageButtonNoInputPropertys;

    //End Region Private Properties

    //Region Constructor
    public InventoryArticleActivity() {

    }

    //End Region Constructor

    //Region Default Methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventoryarticle);
        this.mActivityInitialize();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(cAppExtension.context).unregisterReceiver(mNumberReceiver);
    }

    @Override
    public void onPause() {
            super.onPause();

            if (cAppExtension.activity instanceof  InventoryArticleActivity) {
                cBarcodeScan.pUnregisterBarcodeReceiver(this.getClass().getSimpleName());
            }
             LocalBroadcastManager.getInstance(cAppExtension.context).unregisterReceiver(mNumberReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        cBarcodeScan.pRegisterBarcodeReceiver(this.getClass().getSimpleName());
        cUserInterface.pEnableScanner();
        LocalBroadcastManager.getInstance(cAppExtension.context).registerReceiver(mNumberReceiver, new IntentFilter(cPublicDefinitions.NUMBERINTENT_NUMBER));
    }

    @Override
    public void onBackPressed() {
        this.mStartInventoryBINActivity();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem pvMenuItem) {

        if (pvMenuItem.getItemId() == android.R.id.home) {
            this.mStartInventoryBINActivity();
            return true;
        }

        return super.onOptionsItemSelected(pvMenuItem);
    }

    //End Region Default Methods

    //Region iICSDefaultActivity defaults

    @Override
    public void mActivityInitialize() {
        this.mSetAppExtensions();

        this.mFindViews();

        this.mSetToolbar(getResources().getString(R.string.message_scan_article));

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

        this.inventoryArticleDetailContainer = findViewById(R.id.inventoryArticleDetailContainer);

        this.toolbar =  findViewById(R.id.toolbar);
        this.toolbarImage = findViewById(R.id.toolbarImage);
        this.toolbarTitle = findViewById(R.id.toolbarTitle);
        this.toolbarSubTitle = findViewById(R.id.toolbarSubtext);

        this.articleContainer = findViewById(R.id.articleContainer);
        this.articleInfoContainer = findViewById(R.id.articleInfoContainer);
        this.articleThumbImageView = findViewById(R.id.articleThumbImageView);

        this.articleDescriptionText = findViewById(R.id.articleDescriptionText);
        this.articleDescription2Text = findViewById(R.id.articleDescription2Text);
        this.articleItemText = findViewById(R.id.articleItemText);
        this.articleBarcodeText = findViewById(R.id.articleBarcodeText);

        this.binText = findViewById(R.id.binText);
        this.quantityText = findViewById(R.id.quantityText);
        this.quantityRequiredText = findViewById(R.id.quantityRequiredText);

        this.imageButtonMinus = findViewById(R.id.imageButtonMinus);
        this.imageButtonPlus = findViewById(R.id.imageButtonPlus);
        this.imageButtonBarcode = findViewById(R.id.imageButtonBarcode);
        this.imageButtonNoInputPropertys = findViewById(R.id.imageButtonNoInputPropertys);
        this.imageButtonDone = findViewById(R.id.imageButtonDone);

    }

    @Override
    public void mSetToolbar(String pvScreenTitleStr) {
        this.toolbarImage.setImageResource(R.drawable.ic_menu_inventory);
        this.toolbarTitle.setText(pvScreenTitleStr);
        this.toolbarTitle.setSelected(true);

        String subTitleStr = cInventoryorder.currentInventoryOrder.getOrderNumberStr();
        this.toolbarSubTitle.setText(subTitleStr);
        this.toolbarSubTitle.setSelected(true);

        setSupportActionBar(this.toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void mFieldsInitialize() {

        if (!cInventoryorder.currentInventoryOrder.isInvAmountManualBln()) {
            this.imageButtonBarcode.setVisibility(View.GONE);
        } else {
            this.imageButtonBarcode.setVisibility(View.VISIBLE);
        }

        this.inventoryCounterPlusHelperInt = 0;
        this. inventoryCounterMinusHelperInt = 0;

        this.articleDescriptionText.setText(cInventoryorderLine.currentInventoryOrderLine.getDescriptionStr());
        this.articleDescription2Text.setText(cInventoryorderLine.currentInventoryOrderLine.getDescription2Str());
        if (cInventoryorderLine.currentInventoryOrderLine.getDescription2Str().isEmpty()) {
            this.articleDescription2Text.setVisibility(View.GONE);
        }
        else
        {
            this.articleDescription2Text.setVisibility(View.VISIBLE);
        }
        this.articleItemText.setText(cInventoryorderLine.currentInventoryOrderLine.getItemNoAndVariantCodeStr());

        this.binText.setText(cInventoryorderBin.currentInventoryOrderBin.getBinCodeStr());

        this.mShowQuantityInfo();
        this.mShowArticleImage();
        this.mShowBarcodeInfo();
        this.mHideArticleInfo();

        this.imageButtonNoInputPropertys.setVisibility(View.GONE);
    }

    @Override
    public void mSetListeners() {
       this.mSetDoneListener();

        if (cSetting.INV_AMOUNT_MANUAL()) {
            this.mSetNumberListener();
            this.mSetImageButtonBarcodeListener();
        }

        this.mSetPlusListener();
        this.mSetMinusListener();
        this.mSetDoneListener();
    }

    @Override
    public void mInitScreen() {

    }

    //End Region iICSDefaultActivity defaults


    //Region Public Methods

    public void pHandleScan(final cBarcodeScan pvBarcodeScan){

        cUserInterface.pCheckAndCloseOpenDialogs();

        //Only ARTICLE scans are allowed
        if (!cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(),cBarcodeLayout.barcodeLayoutEnu.ARTICLE)) {
            mDoUnknownScan(cAppExtension.context.getString(R.string.error_article_scan_mandatory));
            return;
        }

        //Check if the scanned value belongs to this line
        if (! cInventoryorder.currentInventoryOrder.pCheckBarcodeWithLineBarcodesBln(pvBarcodeScan)) {

            //Keep the scan, so BIN activity can handle it
            InventoryorderBinActivity.barcodeScanToHandle = pvBarcodeScan;

            //Close this activity, we are done with the current article
            this.mHandleDone();
           return;
        }

        this.mShowBarcodeInfo();

        //Try to raise quantityDbl
        this.mTryToChangeInventoryQuantity(true, false,cInventoryorderBarcode.currentInventoryOrderBarcode.getQuantityPerUnitOfMeasureDbl() );

    }

    //End Region Public Methods

    //Region Private Methods

    private void mTryToChangeInventoryQuantity(Boolean pvIsPositiveBln, Boolean pvAmountFixedBln, double pvAmountDbl) {

        if (pvIsPositiveBln) {

            //Determine the new amount
            if (pvAmountFixedBln) {

                cInventoryorderLineBarcode.pDeleteAllOtherLinesForBarcode(cInventoryorderLine.currentInventoryOrderLine.getLineNoInt(),
                                                                          cInventoryorderLineBarcode.currentInventoryorderLineBarcode.getBarcodeStr() );

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

    private void mShowQuantityInfo() {

        this.quantityText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        this.quantityText.setSelectAllOnFocus(true);
        this.quantityText.requestFocus();
        this.quantityText.setSingleLine();
        this.quantityText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_TEXT_VARIATION_NORMAL);
        this.quantityText.setCursorVisible(false);

        this.quantityText.setText(cText.pDoubleToStringStr(cInventoryorderLine.currentInventoryOrderLine.getQuantityHandledDbl()));
        this.quantityRequiredText.setVisibility(View.INVISIBLE);
    }

    private void mNumberClicked() {

        if (!cSetting.INV_AMOUNT_MANUAL()) {
            return;
        }

        if (cInventoryorderBarcode.currentInventoryOrderBarcode == null) {
            cUserInterface.pDoNope(this.quantityText, false, false);
            cUserInterface.pShowSnackbarMessage(this.inventoryArticleDetailContainer, getString(R.string.choose_barcode_first), null, false);
            return;
        }

        if (cInventoryorderBarcode.currentInventoryOrderBarcode.getQuantityPerUnitOfMeasureDbl() > 1) {

            //Try to switch to another barcode for the same article, so the user doesn't have to manually
            cInventoryorderBarcode inventoryorderBarcode = cInventoryorderBarcode.getSingleQuantityBarcodeForItemAndVariant(cInventoryorderBarcode.currentInventoryOrderBarcode);
            if (inventoryorderBarcode != null) {
                cInventoryorderBarcode.currentInventoryOrderBarcode = inventoryorderBarcode;
            }
            else
            {
                cUserInterface.pDoNope(this.quantityText, true, true);
                cUserInterface.pShowSnackbarMessage(this.inventoryArticleDetailContainer, getString(R.string.manual_input_only_barcodenumber_bigger1), null, false);
                return;
            }
        }

        this.mShowNumberPickerFragment();
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
        this.mResetCurrents();
        this.mStartInventoryBINActivity();
    }

    private void mDoUnknownScan(String pvErrorMessageStr) {
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

    private  void mShowBarcodeInfo(){
        if (cInventoryorderBarcode.currentInventoryOrderBarcode != null) {
            this.articleBarcodeText.setText(cInventoryorderBarcode.currentInventoryOrderBarcode.getUnitOfMeasureInfoStr());
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

    private void mStartInventoryBINActivity() {
        cUserInterface.pCheckAndCloseOpenDialogs();

        InventoryorderBinActivity.busyBln = false;

        final Intent intent = new Intent(cAppExtension.context, InventoryorderBinActivity.class);
            cAppExtension.activity.startActivity(intent);
            cAppExtension.activity.finish();
    }

    private void mResetCurrents(){
        cInventoryorderLine.currentInventoryOrderLine = null;
        cInventoryorderBarcode.currentInventoryOrderBarcode = null;
        cInventoryorderLineBarcode.currentInventoryorderLineBarcode = null;
    }

    //Region Number Broadcaster

    private void mShowNumberPickerFragment() {

        cUserInterface.pCheckAndCloseOpenDialogs();

        Bundle bundle = new Bundle();
        bundle.putInt(cPublicDefinitions.NUMBERINTENT_CURRENTQUANTITY, cInventoryorderBarcode.currentInventoryOrderBarcode.getQuantityHandledDbl().intValue());
        bundle.putDouble(cPublicDefinitions.NUMBERINTENT_MAXQUANTITY,99999);

        NumberpickerFragment numberpickerFragment = new NumberpickerFragment();
        numberpickerFragment.setArguments(bundle);

        numberpickerFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.NUMBERPICKERFRAGMENT_TAG);
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

    private void mSetNumberListener() {
        this.quantityText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNumberClicked();
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

    private final Runnable mMinusAction = new Runnable() {
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

    private final Runnable mPlusAction = new Runnable() {
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

    private final BroadcastReceiver mNumberReceiver = new BroadcastReceiver() {
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
        this.inventoryCounterMinusHelperInt += 1;
    }

    private void mDoDelayedPlus(Runnable pvRunnable, long pvMilliSecsLng) {
        this.plusHandler.postDelayed(pvRunnable, pvMilliSecsLng);
        this.inventoryCounterPlusHelperInt += 1;
    }

    private void mHandleQuantityChosen(double pvQuantityDbl) {
        this.mTryToChangeInventoryQuantity(pvQuantityDbl != 0, true,pvQuantityDbl);
    }

    //End Region Private Methods
}
