package nl.icsvertex.scansuite.Activities.QualityControl;

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
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.ArrayList;
import java.util.List;

import ICS.Interfaces.iICSDefaultActivity;
import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cConnection;
import ICS.Utils.cText;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Basics.Settings.cSetting;
import SSU_WHS.General.cPublicDefinitions;
import SSU_WHS.Picken.PickorderBarcodes.cPickorderBarcode;
import SSU_WHS.Picken.PickorderLineBarcodes.cPickorderLineBarcode;
import SSU_WHS.Picken.PickorderLinePackAndShip.cPickorderLinePackAndShip;
import SSU_WHS.Picken.Pickorders.cPickorder;
import nl.icsvertex.scansuite.Fragments.Dialogs.AcceptRejectFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.BarcodeFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.NumberpickerFragment;
import nl.icsvertex.scansuite.R;

public class PickorderQCActivity extends AppCompatActivity implements iICSDefaultActivity {

    //Region Private Propertie
    private int pickCounterMinusHelperInt;
    private int pickCounterPlusHelperInt;

    private Handler minusHandler;
    private Handler plusHandler;

    private  ConstraintLayout pickorderQCContainer;

    private  Toolbar toolbar;
    private  ImageView toolbarImage;
    private  TextView toolbarTitle;
    private  TextView toolbarSubtext;

    private  TextView articleDescriptionText;
    private  TextView articleDescription2Text;
    private  TextView articleItemText;
    private  TextView articleBarcodeText;
    private CardView sourceNoContainer;
    private CardView binContainer;
    private CardView destinationContainer;

    private  TextView quantityText;
    private  TextView quantityRequiredText;

    private ImageView articleThumbImageView;
    private  ImageView imageButtonBarcode;
    private ImageView  imageButtonNoInputPropertys;

    private  AppCompatImageButton imageButtonMinus;
    private  AppCompatImageButton imageButtonPlus;
    private  AppCompatImageButton imageButtonDone;
    private  TextView textViewAction;

    private CardView articleContainer;
    private ConstraintLayout articleInfoContainer;

    //End Region Private Properties

    //Region Default Methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickorder_qc);
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
        this.mActivityInitialize();
        LocalBroadcastManager.getInstance(cAppExtension.context).registerReceiver(mNumberReceiver, new IntentFilter(cPublicDefinitions.NUMBERINTENT_NUMBER));
        cBarcodeScan.pRegisterBarcodeReceiver(this.getClass().getSimpleName());
        cUserInterface.pEnableScanner();
    }

    @Override
    protected void onStop() {
        super.onStop();
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

    //Region iICSDefaultActivity Methods

    @Override
    public void mActivityInitialize() {

        this.mSetAppExtensions();

        this.mFindViews();

        this.mSetToolbar(getResources().getString(R.string.screentitle_qc));

        this.mSetListeners();

        this.mInitScreen();

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

        this.pickorderQCContainer = findViewById(R.id.pickorderQCContainer);

        this.toolbar = findViewById(R.id.toolbar);
        this.toolbarImage = findViewById(R.id.toolbarImage);
        this.toolbarTitle = findViewById(R.id.toolbarTitle);
        this.toolbarSubtext = findViewById(R.id.toolbarSubtext);

        this.articleDescriptionText = findViewById(R.id.articleDescriptionText);
        this.articleDescription2Text = findViewById(R.id.articleDescription2Text);
        this.articleItemText = findViewById(R.id.articleItemText);
        this.articleBarcodeText = findViewById(R.id.articleBarcodeText);

        this.sourceNoContainer = findViewById(R.id.sourceNoContainer);
        this.binContainer = findViewById(R.id.binContainer);
        this.destinationContainer = findViewById(R.id.destinationContainer);

        this.quantityText = findViewById(R.id.quantityText);
        this.quantityRequiredText = findViewById(R.id.quantityRequiredText);

        this.imageButtonBarcode = findViewById(R.id.imageButtonBarcode);
        this.imageButtonMinus = findViewById(R.id.imageButtonMinus);
        this.imageButtonPlus = findViewById(R.id.imageButtonPlus);
        this.imageButtonDone = findViewById(R.id.imageButtonDone);

        this.articleThumbImageView = findViewById(R.id.articleThumbImageView);
        this.imageButtonNoInputPropertys = findViewById(R.id.imageButtonNoInputPropertys);

        this.textViewAction = findViewById(R.id.textViewAction);

        this.articleContainer = findViewById(R.id.articleContainer);
        this.articleInfoContainer = findViewById(R.id.articleInfoContainer);
    }

    @Override
    public void mSetToolbar(String pvScreenTitleStr) {
        this.toolbarImage.setImageResource(R.drawable.ic_menu_pick);
        this.toolbarTitle.setText(pvScreenTitleStr);
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

        this.pickCounterPlusHelperInt = 0;
        this.pickCounterMinusHelperInt = 0;
        this.toolbarSubtext.setText(cPickorderLinePackAndShip.currentPickorderLinePackAndShip.getProcessingSequenceStr());

        this.articleDescriptionText.setText(cPickorderLinePackAndShip.currentPickorderLinePackAndShip.getDescriptionStr());
        this.articleDescription2Text.setText(cPickorderLinePackAndShip.currentPickorderLinePackAndShip.getDescription2Str());

        this.articleItemText.setText(cPickorderLinePackAndShip.currentPickorderLinePackAndShip.getItemNoAndVariantStr());

        this.sourceNoContainer.setVisibility(View.GONE);
        this.binContainer.setVisibility(View.GONE);
        this.destinationContainer.setVisibility(View.GONE);

        this.quantityText.setText(cText.pDoubleToStringStr(cPickorderLinePackAndShip.currentPickorderLinePackAndShip.getQuantityCheckedDbl()));
        this.quantityRequiredText.setText(cText.pDoubleToStringStr(cPickorderLinePackAndShip.currentPickorderLinePackAndShip.getQuantityDbl()));

        this.mEnablePlusMinusAndBarcodeSelectViews();

        this.mShowBarcodeInfo();

        this.mCheckLineDone();
        this.mHideArticleInfo();

        this.articleThumbImageView.setVisibility(View.GONE);
        this.imageButtonNoInputPropertys.setVisibility(View.GONE);

    }

    @Override
    public void mInitScreen() {

        cBarcodeScan.pRegisterBarcodeReceiver(this.getClass().getSimpleName());

        //Raise quantity with scanned barcodeStr, if we started this activity with a scan
        if (cPickorder.currentPickOrder.pickorderQCBarcodeScanned != null) {
            this.pHandleScan(cBarcodeScan.pFakeScan(cPickorder.currentPickOrder.pickorderQCBarcodeScanned.getBarcodeStr()));
            cPickorder.currentPickOrder.pickorderQCBarcodeScanned =null;
        }


    }

    @Override
    public void mSetListeners() {

        this.mSetImageButtonBarcodeListener();

        if (cSetting.PICK_SELECTEREN_BARCODE()) {
            this.mSetNumberListener();
        }

        this.mSetPlusListener();
        this.mSetMinusListener();
        this.mSetDoneListener();

    }

    // End Region iICSDefaultActivity Methods

    //Region Public Methods

    public  void pHandleScan(cBarcodeScan pvBarcodeScan) {

        cUserInterface.pCheckAndCloseOpenDialogs();

        if (!this.mFindBarcodeInLineBarcodes(pvBarcodeScan)) {
            cUserInterface.pDoExplodingScreen(cAppExtension.context.getString(R.string.error_unknown_barcode), pvBarcodeScan.getBarcodeOriginalStr(), true, true);
            return;
        }

        //If we found the barcodeStr, currentbarcode is alreay filled, so make this selected
        this.mBarcodeSelected(cPickorderBarcode.currentPickorderBarcode);

    }

    public  void pAcceptQC(boolean ignoreAcceptBln) {
        if (ignoreAcceptBln) {
            cUserInterface.pPlaySound(R.raw.headsupsound,null);
            cUserInterface.pDoBoing(this.textViewAction);
            return;
        }

        this.mQCDone();
    }

    public  void pCancelQC() {
        cPickorderLinePackAndShip.currentPickorderLinePackAndShip.quantityCheckedDbl = 0;
        cPickorderLinePackAndShip.currentPickorderLinePackAndShip = null;
        this.mGoBackToLinesActivity();
    }

    public  void pRegisterBarcodeReceiver(){
        cBarcodeScan.pRegisterBarcodeReceiver(this.getClass().getSimpleName());
    }

    //End Region Public Methods

    //Region Private Methods

    //Views

    private void mSetImageButtonBarcodeListener() {
        this.imageButtonBarcode.setOnClickListener(pvView -> {

            if (cPickorderLinePackAndShip.currentPickorderLinePackAndShip.barcodesObl == null || cPickorderLinePackAndShip.currentPickorderLinePackAndShip.barcodesObl.size() == 0) {
                return;
            }

            mEnablePlusMinusAndBarcodeSelectViews();

            //If we only have one barcodeStr, then automatticaly select that barcodeStr
            if (cPickorderLinePackAndShip.currentPickorderLinePackAndShip.barcodesObl.size() == 1) {
                pHandleScan(cBarcodeScan.pFakeScan(cPickorderLinePackAndShip.currentPickorderLinePackAndShip.barcodesObl.get(0).getBarcodeStr()));
                return;
            }

            mShowBarcodeSelectFragment();
        });
    }

    private void mShowBarcodeInfo() {

        if (cPickorderBarcode.currentPickorderBarcode == null) {
                if (cPickorderLinePackAndShip.currentPickorderLinePackAndShip.barcodesObl.size() == 1) {
                    cPickorderBarcode.currentPickorderBarcode = cPickorderLinePackAndShip.currentPickorderLinePackAndShip.barcodesObl.get(0);
                }
        }

        if (cPickorderBarcode.currentPickorderBarcode != null) {
            this.articleBarcodeText.setText(cPickorderBarcode.currentPickorderBarcode.getBarcodeAndQuantityStr());
        } else {
            this.articleBarcodeText.setText(cAppExtension.context.getString(R.string.mutiple_barcodes_posible));
        }
    }

    private void mEnablePlusMinusAndBarcodeSelectViews() {

        if (cSetting.PICK_PER_SCAN()) {
            this.imageButtonMinus.setVisibility(View.INVISIBLE);
            this.imageButtonPlus.setVisibility(View.INVISIBLE);
        } else {
            this.imageButtonMinus.setVisibility(View.VISIBLE);
            this.imageButtonPlus.setVisibility(View.VISIBLE);
        }

        if (!cSetting.PICK_SELECTEREN_BARCODE()) {
            this.imageButtonBarcode.setVisibility(View.GONE);
        } else {
            this.imageButtonBarcode.setVisibility(View.VISIBLE);
        }

    }

    //Scans and manual input

    private void mNumberClicked() {

        if (cSetting.PICK_PER_SCAN()) {
            return;
        }

        if (cPickorderBarcode.currentPickorderBarcode == null) {
            cUserInterface.pDoNope(quantityText, false, false);
            cUserInterface.pShowSnackbarMessage(pickorderQCContainer, getString(R.string.choose_barcode_first), null, false);
            return;
        }

        if (cPickorderBarcode.currentPickorderBarcode.getQuantityHandledDbl() > 1) {
            cUserInterface.pDoNope(quantityText, true, true);
            cUserInterface.pShowSnackbarMessage(pickorderQCContainer, getString(R.string.manual_input_only_barcodenumber_bigger1), null, false);
            return;
        }

        this.mShowNumberPickerFragment();
    }

    private void mHandleQuantityChosen(double pvQuantityDbl) {
        this.mTryToChangePickedQuantity(pvQuantityDbl != 0, true,pvQuantityDbl);
    }

    private void mTryToChangePickedQuantity(Boolean pvIsPositiveBln, Boolean pvAmountFixedBln, double pvAmountDbl) {

      double newQuantityDbl;
        List<cPickorderLineBarcode>  hulpObl;
        if (pvIsPositiveBln) {

            //Determine the new amount
            if (pvAmountFixedBln) {

                //Check if we already have barcodes and clear them
                if (cPickorderLinePackAndShip.currentPickorderLinePackAndShip.handledBarcodesObl().size() > 0 ) {

                    hulpObl = new ArrayList<>(cPickorderLinePackAndShip.currentPickorderLinePackAndShip.handledBarcodesObl());

                    for (cPickorderLineBarcode pickorderLineBarcode : hulpObl) {
                        pickorderLineBarcode.pDeleteFromDatabaseBln();
                    }
                }

                newQuantityDbl = pvAmountDbl;
            } else {
                newQuantityDbl = cPickorderLinePackAndShip.currentPickorderLinePackAndShip.getQuantityCheckedDbl() + pvAmountDbl;
            }

            //Check if we would exceed amount, then show message
            if (newQuantityDbl >cPickorderLinePackAndShip.currentPickorderLinePackAndShip.getQuantityDbl()) {
                this.mShowOverpickNotAllowed();

                if (cPickorderLinePackAndShip.currentPickorderLinePackAndShip.getQuantityDbl() > 0) {

                    return;
                }

                return;
            }

            //Set the new quantityDbl and show in Activity
            cPickorderLinePackAndShip.currentPickorderLinePackAndShip.quantityCheckedDbl = newQuantityDbl;
            this.quantityText.setText(cText.pDoubleToStringStr(cPickorderLinePackAndShip.currentPickorderLinePackAndShip.getQuantityCheckedDbl()));

            //Add or update line barcodeStr
            cPickorderLinePackAndShip.currentPickorderLinePackAndShip.pAddOrUpdateLineBarcode(pvAmountDbl);

            //Check if this line is done
            this.mCheckLineDone();
            return;
        }

        //negative
        if ( cPickorderLinePackAndShip.currentPickorderLinePackAndShip.getQuantityCheckedDbl() == 0 ) {
            cUserInterface.pDoNope(quantityText, true, true);
            return;
        }

        //Determine the new amount

        if (pvAmountFixedBln) {
            newQuantityDbl = pvAmountDbl;
        }else {
            newQuantityDbl=  cPickorderLinePackAndShip.currentPickorderLinePackAndShip.getQuantityCheckedDbl() - pvAmountDbl;
        }

        if (newQuantityDbl <= 0) {
            cPickorderLinePackAndShip.currentPickorderLinePackAndShip.quantityCheckedDbl = 0.0;
        }else {
            //Set the new quantityDbl and show in Activity
            cPickorderLinePackAndShip.currentPickorderLinePackAndShip.quantityCheckedDbl = newQuantityDbl;
        }

        this.quantityText.setText(cText.pDoubleToStringStr( cPickorderLinePackAndShip.currentPickorderLinePackAndShip.getQuantityCheckedDbl()));
        this.imageButtonDone.setImageResource(R.drawable.ic_check_black_24dp);

        //Remove or update line barcodeStr
        cPickorderLinePackAndShip.currentPickorderLinePackAndShip.pRemoveOrUpdateLineBarcode();

    }

    private void mBarcodeSelected(cPickorderBarcode pvBarcode) {

        cUserInterface.pCheckAndCloseOpenDialogs();

        cPickorderBarcode.currentPickorderBarcode = pvBarcode;
        this.mShowBarcodeInfo();
        this.mTryToChangePickedQuantity(true, false, cPickorderBarcode.currentPickorderBarcode.getQuantityPerUnitOfMeasureDbl());
    }

    // Lines, Barcodes, Packing Tables and destionation

    private boolean mFindBarcodeInLineBarcodes(cBarcodeScan pvBarcodeScan) {

        if ( cPickorderLinePackAndShip.currentPickorderLinePackAndShip.barcodesObl == null ||  cPickorderLinePackAndShip.currentPickorderLinePackAndShip.barcodesObl.size() == 0) {
            return false;
        }

        for (cPickorderBarcode pickorderBarcode :  cPickorderLinePackAndShip.currentPickorderLinePackAndShip.barcodesObl) {

            if (pickorderBarcode.getBarcodeStr().equalsIgnoreCase(pvBarcodeScan.getBarcodeOriginalStr()) || pickorderBarcode.getBarcodeWithoutCheckDigitStr().equalsIgnoreCase(pvBarcodeScan.getBarcodeFormattedStr())) {
                cPickorderBarcode.currentPickorderBarcode = pickorderBarcode;
                return true;
            }
        }
        return false;
    }

    private void mCheckLineDone() {

        //Start with defaults
        boolean incompleteBln = false;
        this.imageButtonDone.setVisibility(View.VISIBLE);
        this.textViewAction.setText(cAppExtension.context.getString(R.string.message_scan_article));

        //Check if quantityDbl is sufficient
        if ( cPickorderLinePackAndShip.currentPickorderLinePackAndShip.quantityCheckedDbl <  cPickorderLinePackAndShip.currentPickorderLinePackAndShip.getQuantityDbl()) {
            this.imageButtonDone.setImageResource(R.drawable.ic_check_black_24dp);
            incompleteBln = true;
        }

        //If we are incomplete, we are done here
        if (incompleteBln)  {
            return;
        }

        this.imageButtonDone.setVisibility(View.VISIBLE);
        this.imageButtonDone.setImageResource(R.drawable.ic_doublecheck_black_24dp);
        this.imageButtonDone.callOnClick();

    }

    private void mSendQCOrderLine() {

        //If internet is not connected
        if (!cConnection.isInternetConnectedBln()) {
            //could not send line, let user know but answer succes so user can go to next line
            cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.couldnt_send_line), null);
            return;
        }


        if (!cPickorderLinePackAndShip.currentPickorderLinePackAndShip.pCheckedBln()) {
            //could not send line, let user know but answer succes so user can go to next line
            cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.couldnt_send_line), null);
            return;
        }

        this.mGoBackToLinesActivity();
    }

    private void mHandlePickDoneClick() {

        //Check if we picked less then asked, if so then show dialog
        if (!cPickorderLinePackAndShip.currentPickorderLinePackAndShip.getQuantityCheckedDbl().equals(cPickorderLinePackAndShip.currentPickorderLinePackAndShip.getQuantityDbl()) ) {
            this.mShowUnderPickDialog(cAppExtension.activity.getString(R.string.message_cancel_line), cAppExtension.activity.getString(R.string.message_accept_line));
            return;
        }

        //All is done
        this.mQCDone();

    }

    private void mQCDone() {
        this.mSendQCOrderLine();
    }

    private void mHideArticleInfo(){

        this.articleInfoContainer.setVisibility(View.GONE);
        ConstraintLayout.LayoutParams newCardViewLayoutParams = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        newCardViewLayoutParams.setMargins(15,15,15,15);
        this.articleContainer.setLayoutParams(newCardViewLayoutParams);

        ConstraintSet constraintSetSpace = new ConstraintSet();
        constraintSetSpace.clone(this.pickorderQCContainer);
        constraintSetSpace.connect(this.articleContainer.getId(), ConstraintSet.TOP, toolbar.getId(), ConstraintSet.BOTTOM);
        constraintSetSpace.applyTo(this.pickorderQCContainer);

    }

    //Listeners
    private void mSetDoneListener() {
        this.imageButtonDone.setOnClickListener(view -> mHandlePickDoneClick());
    }

    @SuppressLint("ClickableViewAccessibility")
    private void mSetPlusListener() {

        this.imageButtonPlus.setOnTouchListener((v, event) -> {

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                if (plusHandler != null) return true;
                plusHandler = new Handler();
                plusHandler.postDelayed(mPlusAction, 750);
            }

            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (plusHandler == null) return true;
                plusHandler.removeCallbacks(mPlusAction);
                plusHandler = null;
                pickCounterPlusHelperInt = 0;
            }

            return false;
        });

        this.imageButtonPlus.setOnClickListener(view -> {

            //There is no selected barcodeStr, select one first
            if (cPickorderBarcode.currentPickorderBarcode == null) {
                cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.message_select_one_of_multiple_barcodes),null);
                return;
            }


            mTryToChangePickedQuantity(true, false, cPickorderBarcode.currentPickorderBarcode.getQuantityPerUnitOfMeasureDbl());
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void mSetMinusListener() {

        this.imageButtonMinus.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                if (minusHandler != null) return true;
                minusHandler = new Handler();
                minusHandler.postDelayed(mMinusAction, 750);
            }
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (minusHandler == null) return true;
                minusHandler.removeCallbacks(mMinusAction);
                minusHandler = null;
                pickCounterMinusHelperInt = 0;
            }
            return false;
        });

        this.imageButtonMinus.setOnClickListener(view -> {


            //There is no selected barcodeStr, select one first
            if (cPickorderBarcode.currentPickorderBarcode == null) {
                cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.message_select_one_of_multiple_barcodes),null);
                return;
            }


            mTryToChangePickedQuantity(false, false, cPickorderBarcode.currentPickorderBarcode.getQuantityPerUnitOfMeasureDbl());
        });
    }

    private void mSetNumberListener() {
        this.quantityText.setOnClickListener(view -> mNumberClicked());
        this.quantityRequiredText.setOnClickListener(view -> mNumberClicked());
    }

    //Dialogs and Activitys


    private void mShowUnderPickDialog(String pvRejectStr,String pvAcceptStr) {

        cUserInterface.pCheckAndCloseOpenDialogs();

        final AcceptRejectFragment acceptRejectFragment = new AcceptRejectFragment(cAppExtension.activity.getString(R.string.message_correcy_line_header),
                                                                                   cAppExtension.activity.getString(R.string.message_undercheck_text,
                                                                                   cText.pDoubleToStringStr(cPickorderLinePackAndShip.currentPickorderLinePackAndShip.getQuantityDbl()),
                                                                                   cText.pDoubleToStringStr(cPickorderLinePackAndShip.currentPickorderLinePackAndShip.getQuantityCheckedDbl())),
        pvRejectStr,
                                                                                   pvAcceptStr ,
                                                                     false);
        acceptRejectFragment.setCancelable(true);
        cAppExtension.activity.runOnUiThread(() -> {
            // show my popup
            acceptRejectFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.ACCEPTREJECTFRAGMENT_TAG);
        });
    }

    private void mShowAcceptFragment(){
        boolean ignoreAccept = false;
        if (cPickorder.currentPickOrder.isPVBln()) {
            ignoreAccept = true;
        }

        cUserInterface.pCheckAndCloseOpenDialogs();

        final AcceptRejectFragment acceptRejectFragment = new AcceptRejectFragment(cAppExtension.activity.getString(R.string.message_orderbusy_header),
                                                                                   cAppExtension.activity.getString(R.string.message_orderbusy_text),
                                                                                   cAppExtension.activity.getString(R.string.message_cancel_line), cAppExtension.activity.getString(R.string.message_accept_line),ignoreAccept);
        acceptRejectFragment.setCancelable(true);

        runOnUiThread(() -> {
            // show my popup
            acceptRejectFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.ACCEPTREJECTFRAGMENT_TAG);
        });
    }

    private void mGoBackToLinesActivity() {
        cPickorderLinePackAndShip.currentPickorderLinePackAndShip = null;
        Intent intent = new Intent(cAppExtension.context, QualityControlLinesActivity.class);
        startActivity(intent);
        finish();
    }

    private void mShowBarcodeSelectFragment() {
        BarcodeFragment barcodeFragment = new BarcodeFragment();
        barcodeFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.BARCODEPICKERFRAGMENT_TAG);
    }

    private void mShowNumberPickerFragment() {

        cUserInterface.pCheckAndCloseOpenDialogs();

        Bundle bundle = new Bundle();
        bundle.putInt(cPublicDefinitions.NUMBERINTENT_CURRENTQUANTITY, cPickorderLinePackAndShip.currentPickorderLinePackAndShip.getQuantityCheckedDbl().intValue());
        bundle.putDouble(cPublicDefinitions.NUMBERINTENT_MAXQUANTITY, cPickorderLinePackAndShip.currentPickorderLinePackAndShip.getQuantityDbl().intValue());

        NumberpickerFragment numberpickerFragment = new NumberpickerFragment();
        numberpickerFragment.setArguments(bundle);

        numberpickerFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.NUMBERPICKERFRAGMENT_TAG);
    }

    private void mShowOverpickNotAllowed(){
        this.quantityText.setText(quantityRequiredText.getText());
        cUserInterface.pShowSnackbarMessage(textViewAction , cAppExtension.context.getString(R.string.number_cannot_be_higher), null, false);
        cUserInterface.pDoNope(quantityText, true, true);
        cUserInterface.pDoNope(quantityRequiredText, false, false);
    }

    //Region Number Broadcaster

    private final Runnable mMinusAction = new Runnable() {
        @Override
        public void run() {
            imageButtonMinus.performClick();
            long milliSecsLng;
            if (pickCounterMinusHelperInt < 10) {
                milliSecsLng = 200;
            } else if (pickCounterMinusHelperInt < 20) {
                milliSecsLng = 150;
            } else if (pickCounterMinusHelperInt < 30) {
                milliSecsLng = 100;
            } else if (pickCounterMinusHelperInt < 40) {
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
            if (pickCounterPlusHelperInt < 10) {
                milliSecsLng = 200;
            } else if (pickCounterPlusHelperInt < 20) {
                milliSecsLng = 150;
            } else if (pickCounterPlusHelperInt < 30) {
                milliSecsLng = 100;
            } else if (pickCounterPlusHelperInt < 40) {
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
        this.pickCounterMinusHelperInt += 1;
    }

    private void mDoDelayedPlus(Runnable pvRunnable, long pvMilliSecsLng) {
        this.plusHandler.postDelayed(pvRunnable, pvMilliSecsLng);
        this.pickCounterPlusHelperInt += 1;
    }

    //End Region Number Broadcaster

    //End Regin Private Methods

}

