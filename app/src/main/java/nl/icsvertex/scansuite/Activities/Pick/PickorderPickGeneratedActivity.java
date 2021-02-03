package nl.icsvertex.scansuite.Activities.Pick;

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
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.ArrayList;
import java.util.List;

import ICS.Interfaces.iICSDefaultActivity;
import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cConnection;
import ICS.Utils.cResult;
import ICS.Utils.cText;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Basics.Article.cArticle;
import SSU_WHS.Basics.ArticleBarcode.cArticleBarcode;
import SSU_WHS.Basics.BarcodeLayouts.cBarcodeLayout;
import SSU_WHS.Basics.Branches.cBranch;
import SSU_WHS.Basics.Settings.cSetting;
import SSU_WHS.Basics.Users.cUser;
import SSU_WHS.General.cPublicDefinitions;
import SSU_WHS.Move.MoveOrders.cMoveorder;
import SSU_WHS.Picken.PickorderBarcodes.cPickorderBarcode;
import SSU_WHS.Picken.PickorderLineBarcodes.cPickorderLineBarcode;
import SSU_WHS.Picken.PickorderLines.cPickorderLine;
import SSU_WHS.Picken.Pickorders.cPickorder;
import nl.icsvertex.scansuite.Fragments.Dialogs.AcceptRejectFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.ArticleFullViewFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.ArticleInfoFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.BarcodeFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.NumberpickerFragment;
import nl.icsvertex.scansuite.R;

public class PickorderPickGeneratedActivity extends AppCompatActivity implements iICSDefaultActivity {

    //Region Private Properties
    private static boolean destionationScannedBln;

    private int pickCounterMinusHelperInt;
    private int pickCounterPlusHelperInt;

    private Handler minusHandler;
    private Handler plusHandler;

    private  ConstraintLayout pickorderPickContainer;

    private  CardView articleContainer;
    private ConstraintLayout articleInfoContainer;

    private Spinner destinationSpinner;

    private  Toolbar toolbar;
    private  ImageView toolbarImage;
    private  TextView toolbarTitle;
    private  TextView toolbarSubtext;

    private  TextView articleDescriptionText;
    private  TextView articleDescription2Text;
    private  TextView articleItemText;
    private  TextView articleBarcodeText;

    private  ImageView articleThumbImageView;
    private  ImageView imageButtonBarcode;
    private ImageView imageButtonNoInputPropertys;

    private  TextView quantityText;
    private  TextView quantityRequiredText;

    private  AppCompatImageButton imageButtonMinus;
    private  AppCompatImageButton imageButtonPlus;
    private  AppCompatImageButton imageButtonDone;

    private  TextView textViewAction;

    //End Region Private Properties

    //Region Default Methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickorder_pick_generated);
        this.mActivityInitialize();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        //Set listeners here, so click listeners only work after activity is shown
        this.mSetListeners();
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

        this.mSetToolbar(getResources().getString(R.string.screentitle_pickorderpick));

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

        this.pickorderPickContainer = findViewById(R.id.pickorderPickContainer);
        this.articleInfoContainer = findViewById(R.id.articleInfoContainer);
        this.articleContainer = findViewById(R.id.articleContainer);

        this.toolbar = findViewById(R.id.toolbar);
        this.toolbarImage = findViewById(R.id.toolbarImage);
        this.toolbarTitle = findViewById(R.id.toolbarTitle);
        this.toolbarSubtext = findViewById(R.id.toolbarSubtext);

        this.articleDescriptionText = findViewById(R.id.articleDescriptionText);
        this.articleDescription2Text = findViewById(R.id.articleDescription2Text);
        this.articleItemText = findViewById(R.id.articleItemText);
        this.articleBarcodeText = findViewById(R.id.articleBarcodeText);
        this.articleThumbImageView = findViewById(R.id.articleThumbImageView);
        this.imageButtonBarcode = findViewById(R.id.imageButtonBarcode);
        this.imageButtonNoInputPropertys = findViewById(R.id.imageButtonNoInputPropertys);

        this.destinationSpinner = findViewById(R.id.destinationSpinner);

        this.quantityText = findViewById(R.id.quantityText);
        this.quantityRequiredText = findViewById(R.id.quantityRequiredText);

        this.imageButtonMinus = findViewById(R.id.imageButtonMinus);
        this.imageButtonPlus = findViewById(R.id.imageButtonPlus);
        this.imageButtonDone = findViewById(R.id.imageButtonDone);

        this.textViewAction = findViewById(R.id.textViewAction);
    }

    @Override
    public void mSetToolbar(String pvScreenTitle) {
        this.toolbarImage.setImageResource(R.drawable.ic_menu_pick_pf);
        this.toolbarTitle.setText(pvScreenTitle);
        this.toolbarTitle.setSelected(true);
        this.toolbarSubtext.setSelected(true);
        this.toolbarSubtext.setText(cPickorder.currentPickOrder.currentBranchBin.getBinCodeStr());
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void mFieldsInitialize() {

        this.pickCounterPlusHelperInt = 0;
        this.pickCounterMinusHelperInt = 0;


        this.mShowArticleImage();
        this.mShowOrHideArticleInfoContainer();

        this.mShowArticleInfo();
        this.mShowBarcodeInfo();
        this.mShowNoInputPropertyInfo();
        this.mFillDestinationSpinner();
        this.mShowQuantityInfo();
        this.mEnablePlusMinusAndBarcodeSelectViews();
        this.mCheckLineDone();
    }

    @Override
    public void mInitScreen() {

        cBarcodeScan.pRegisterBarcodeReceiver(this.getClass().getSimpleName());
    }

    @Override
    public void mSetListeners() {

        this.mSetArticleImageListener();
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

        //If we scan a branch reset current branch
        if (cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(),cBarcodeLayout.barcodeLayoutEnu.LOCATION)) {
            cPickorder.currentPickOrder.scannedBranch  = null;
        }

//        //If we still need a destination scan, make sure we scan this first
//        if (cPickorder.currentPickOrder.scannedBranch  == null && cPickorder.currentPickOrder.isPFBln() ) {
//            cResult hulpRst = this.mCheckDestionationRst(pvBarcodeScan);
//            if (! hulpRst.resultBln) {
//                cUserInterface.pDoExplodingScreen(hulpRst.messagesStr(),"", true, true);
//            }
//
//            //If we scanned, refresh to pick fragment and leave this void
//            if (PickorderLinesActivity.currentLineFragment instanceof PickorderLinesToPickFragment) {
//                PickorderLinesToPickFragment pickorderLinesToPickFragment = (PickorderLinesToPickFragment)PickorderLinesActivity.currentLineFragment;
//                pickorderLinesToPickFragment.pBranchScanned();
//                return;
//            }
//        }


        //If we still need a destination scan, make sure we scan this first
        if (!this.mFindBarcodeInLineBarcodes(pvBarcodeScan)) {
            cUserInterface.pDoExplodingScreen(cAppExtension.context.getString(R.string.error_unknown_barcode), pvBarcodeScan.getBarcodeOriginalStr(), true, true);
            return;
        }

        //If we found the barcodeStr, currentbarcode is alreay filled, so make this selected
        this.mBarcodeSelected(cPickorderBarcode.currentPickorderBarcode);

    }

    public  void pAcceptPick(boolean ignoreAccept) {
        if (ignoreAccept) {
            cUserInterface.pPlaySound(R.raw.headsupsound,null);
            cUserInterface.pDoBoing(this.textViewAction);
            return;
        }
        cPickorderLine.currentPickOrderLine.pHandledIndatabase();
        this.mPickDone();
    }


    public  void pCancelPick() {

        cPickorderLine.currentPickOrderLine.pCancelIndatabase();
        this.mGoBackToLinesActivity();
    }

    public  void pRegisterBarcodeReceiver(){
        cBarcodeScan.pRegisterBarcodeReceiver(this.getClass().getSimpleName());
    }

    //End Region Public Methods

    //Region Private Methods

    //Views


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

                if (cPickorderLine.currentPickOrderLine.barcodesObl == null || cPickorderLine.currentPickOrderLine.barcodesObl.size() == 0) {
                    return;
                }

                mEnablePlusMinusAndBarcodeSelectViews();

                //If we only have one barcodeStr, then automatticaly select that barcodeStr
                if (cPickorderLine.currentPickOrderLine.barcodesObl.size() == 1) {
                    pHandleScan(cBarcodeScan.pFakeScan(cPickorderLine.currentPickOrderLine.barcodesObl.get(0).getBarcodeStr()));
                    return;
                }

                mShowBarcodeSelectFragment();
            }
        });
    }

    private  void mShowOrHideArticleInfoContainer() {

        boolean hideArticleInfoContainerBln = false;

        if (cPickorderLine.currentPickOrderLine == null) {
            hideArticleInfoContainerBln =true;
        }
        else
        {
            //Get article info via the web service
            cArticle.currentArticle  = new cArticle(cPickorderLine.currentPickOrderLine.getItemNoStr(), cPickorderLine.currentPickOrderLine.getVariantCodeStr());
            if ( cPickorderLine.currentPickOrderLine.itemProperyDataObl() == null) {
                hideArticleInfoContainerBln =true;
            }
        }

        if (hideArticleInfoContainerBln) {
            this.articleInfoContainer.setVisibility(View.GONE);
            ConstraintLayout.LayoutParams newCardViewLayoutParams = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            newCardViewLayoutParams.setMargins(15,15,15,15);
            this.articleContainer.setLayoutParams(newCardViewLayoutParams);

            ConstraintSet constraintSetSpace = new ConstraintSet();
            constraintSetSpace.clone(pickorderPickContainer);
            constraintSetSpace.connect(articleContainer.getId(), ConstraintSet.TOP, toolbar.getId(), ConstraintSet.BOTTOM);
            constraintSetSpace.applyTo(pickorderPickContainer);
            return;
        }

        this.articleInfoContainer.setVisibility(View.VISIBLE);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.articleInfoContainer, new ArticleInfoFragment(cPickorderLine.currentPickOrderLine.itemProperyDataObl()));
        transaction.commit();
    }

    private  void mShowArticleInfo() {

        if (cPickorderLine.currentPickOrderLine == null) {
            this.articleDescriptionText.setText(cAppExtension.activity.getString(R.string.novalueyet));
            this.articleDescription2Text.setText(cAppExtension.activity.getString(R.string.novalueyet));
            this.articleItemText.setText(cAppExtension.activity.getString(R.string.novalueyet));
            return;
        }

        this.articleDescriptionText.setText(cPickorderLine.currentPickOrderLine.getDescriptionStr());
        this.articleDescription2Text.setText(cPickorderLine.currentPickOrderLine.getDescription2Str());
        this.articleItemText.setText(cPickorderLine.currentPickOrderLine.getItemNoAndVariantStr());

        if (cPickorderLine.currentPickOrderLine.getDescription2Str().isEmpty()) {
            this.articleDescription2Text.setVisibility(View.GONE);
        }
    }

    private  void mShowBarcodeInfo() {

        if (cPickorderLine.currentPickOrderLine == null) {
            this.articleBarcodeText.setText(cAppExtension.context.getString(R.string.novalueyet));
            return;
        }

        if (cPickorderBarcode.currentPickorderBarcode == null) {
            if (cPickorderLine.currentPickOrderLine.barcodesObl.size() == 1) {
                cPickorderBarcode.currentPickorderBarcode = cPickorderLine.currentPickOrderLine.barcodesObl.get(0);
            }
        }

        if (cPickorderBarcode.currentPickorderBarcode != null) {
            this.articleBarcodeText.setText(cPickorderBarcode.currentPickorderBarcode.getBarcodeAndQuantityStr());
        } else {
            this.articleBarcodeText.setText(cAppExtension.context.getString(R.string.mutiple_barcodes_posible));
        }
    }

    private  void mShowNoInputPropertyInfo() {

        if (cPickorderLine.currentPickOrderLine == null) {
            this.imageButtonNoInputPropertys.setVisibility(View.GONE);
            return;
        }

        if (!cPickorderLine.currentPickOrderLine.hasPropertysBln() || cPickorderLine.currentPickOrderLine.pickorderLinePropertyNoInputObl() == null || cPickorderLine.currentPickOrderLine.pickorderLinePropertyNoInputObl().size() == 0) {
            this.imageButtonNoInputPropertys.setVisibility(View.GONE);
        }
        else {
            this.imageButtonNoInputPropertys.setVisibility(View.VISIBLE);
        }
    }

    private void mShowQuantityInfo(){

        double quantityDbl = 0;

        if (cPickorderLine.currentPickOrderLine == null) {
            this.quantityText.setVisibility(View.INVISIBLE);
            this.quantityRequiredText.setVisibility(View.INVISIBLE);
            this.imageButtonDone.setVisibility(View.INVISIBLE);
            return;
        }

        if (cPickorderLine.currentPickOrderLine.handledBarcodesObl().size() > 0) {
            for (cPickorderLineBarcode pickorderLineBarcode : cPickorderLine.currentPickOrderLine.handledBarcodesObl())

                quantityDbl += pickorderLineBarcode.getQuantityhandledDbl();
        }

        this.quantityText.setText(cText.pDoubleToStringStr(quantityDbl));


    }

    private  void mShowArticleImage() {

        //If pick with picture is false, then hide image view
        if (cPickorderLine.currentPickOrderLine == null || !cPickorder.currentPickOrder.isPickWithPictureBln()) {
            this.articleThumbImageView.setImageDrawable(ContextCompat.getDrawable(cAppExtension.context, R.drawable.ic_no_image_lightgrey_24dp));
            this.articleThumbImageView.setVisibility(View.GONE);
            return;
        }

        this.articleThumbImageView.setVisibility(View.VISIBLE);

        //If picture is not in cache (via webservice) then show no image
        if (!cPickorderLine.currentPickOrderLine.pGetArticleImageBln()) {
            cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.could_not_get_article_image), null);
            this.articleThumbImageView.setImageDrawable(ContextCompat.getDrawable(cAppExtension.context, R.drawable.ic_no_image_lightgrey_24dp));
            return;
        }

        //If picture is in cache but can't be converted, then show no image
        if (cPickorderLine.currentPickOrderLine.articleImage == null || cPickorderLine.currentPickOrderLine.articleImage.imageBitmap() == null) {
            cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.could_not_get_article_image), null);
            this.articleThumbImageView.setImageDrawable(ContextCompat.getDrawable(cAppExtension.context, R.drawable.ic_no_image_lightgrey_24dp));
            return;
        }

        //Show the image
        this.articleThumbImageView.setImageBitmap(cPickorderLine.currentPickOrderLine.articleImage.imageBitmap());

        //Open the image
        if ((cPickorder.currentPickOrder.isPickWithPictureAutoOpenBln())) {
            this.mShowFullArticleFragment();
        }
    }

    private  void mEnablePlusMinusAndBarcodeSelectViews() {

        if (cPickorderLine.currentPickOrderLine == null) {
            this.imageButtonMinus.setVisibility(View.INVISIBLE);
            this.imageButtonPlus.setVisibility(View.INVISIBLE);
            this.imageButtonBarcode.setVisibility(View.INVISIBLE);
            return;
        }

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
            cUserInterface.pShowSnackbarMessage(pickorderPickContainer, getString(R.string.choose_barcode_first), null, false);
            return;
        }

        if (cPickorderBarcode.currentPickorderBarcode.getQuantityHandledDbl() > 1) {
            cUserInterface.pDoNope(quantityText, true, true);
            cUserInterface.pShowSnackbarMessage(pickorderPickContainer, getString(R.string.manual_input_only_barcodenumber_bigger1), null, false);
            return;
        }

        this.mShowNumberPickerFragment();
    }

    private void mHandleQuantityChosen(double pvQuantityDbl) {
        this.mTryToChangePickedQuantity(pvQuantityDbl != 0, true,pvQuantityDbl);
    }

    private  void mTryToChangePickedQuantity(Boolean pvIsPositiveBln, Boolean pvAmountFixedBln, double pvAmountDbl) {

        double newQuantityDbl;
        List<cPickorderLineBarcode>  hulpObl;
        if (pvIsPositiveBln) {

            //Determine the new amount
            if (pvAmountFixedBln) {

                //Check if we already have barcodes and clear them
                if (cPickorderLine.currentPickOrderLine.handledBarcodesObl().size() > 0 ) {

                    hulpObl = new ArrayList<>(cPickorderLine.currentPickOrderLine.handledBarcodesObl());

                    for (cPickorderLineBarcode pickorderLineBarcode : hulpObl) {
                        pickorderLineBarcode.pDeleteFromDatabaseBln();
                    }
                }

                newQuantityDbl = pvAmountDbl;
            } else {
                newQuantityDbl = cPickorderLine.currentPickOrderLine.getQuantityHandledDbl() + pvAmountDbl;
            }

            //Check if we would exceed amount, then show message
            if (newQuantityDbl > cPickorderLine.currentPickOrderLine.getQuantityDbl()) {
                this.mShowOverpickNotAllowed();

                if (cPickorderLine.currentPickOrderLine.getQuantityDbl() > 0) {
                    return;
                }
                return;
            }

            //Set the new quantityDbl and show in Activity
            cPickorderLine.currentPickOrderLine.quantityHandledDbl = newQuantityDbl;
            this.quantityText.setText(cText.pDoubleToStringStr(cPickorderLine.currentPickOrderLine.getQuantityHandledDbl()));

            //Add or update line barcodeStr
            cPickorderLine.currentPickOrderLine.pAddOrUpdateLineBarcode(pvAmountDbl);

            //Update orderline info (quantityDbl, timestamp, localStatusInt)
            cPickorderLine.currentPickOrderLine.pHandledIndatabase();

            //Check if this line is done
            this.mCheckLineDone();
            return;
        }

        //negative
        if (cPickorderLine.currentPickOrderLine.getQuantityHandledDbl() == 0 ) {
            cUserInterface.pDoNope(quantityText, true, true);
            return;
        }

        //Determine the new amount

        if (pvAmountFixedBln) {
            newQuantityDbl = pvAmountDbl;
        }else {
            newQuantityDbl= cPickorderLine.currentPickOrderLine.getQuantityHandledDbl() - pvAmountDbl;
        }

        if (newQuantityDbl <= 0) {
            cPickorderLine.currentPickOrderLine.quantityHandledDbl = 0.0;
        }else {
            //Set the new quantityDbl and show in Activity
            cPickorderLine.currentPickOrderLine.quantityHandledDbl = newQuantityDbl;
        }

        this.quantityText.setText(cText.pDoubleToStringStr(cPickorderLine.currentPickOrderLine.getQuantityHandledDbl()));
        this.imageButtonDone.setImageResource(R.drawable.ic_check_black_24dp);

        //Remove or update line barcodeStr
        cPickorderLine.currentPickOrderLine.pRemoveOrUpdateLineBarcode();

    }

    private  void mBarcodeSelected(cPickorderBarcode pvBarcode) {

        cUserInterface.pCheckAndCloseOpenDialogs();

        cPickorderBarcode.currentPickorderBarcode = pvBarcode;
        this.mShowBarcodeInfo();
        this.mTryToChangePickedQuantity(true, false, cPickorderBarcode.currentPickorderBarcode.getQuantityPerUnitOfMeasureDbl());
    }

    // Lines, Barcodes, Packing Tables and destionation

    private  boolean mFindBarcodeInLineBarcodes(cBarcodeScan pvBarcodeScan) {

        if (cPickorderLine.currentPickOrderLine.barcodesObl == null || cPickorderLine.currentPickOrderLine.barcodesObl.size() == 0) {
            return false;
        }

        for (cPickorderBarcode pickorderBarcode : cPickorderLine.currentPickOrderLine.barcodesObl) {

            if (pickorderBarcode.getBarcodeStr().equalsIgnoreCase(pvBarcodeScan.getBarcodeOriginalStr()) || pickorderBarcode.getBarcodeWithoutCheckDigitStr().equalsIgnoreCase(pvBarcodeScan.getBarcodeFormattedStr())) {
                cPickorderBarcode.currentPickorderBarcode = pickorderBarcode;
                return true;
            }
        }
        return false;
    }

    private  void mCheckLineDone() {

        if (cPickorderLine.currentPickOrderLine == null) {
            return;
        }

        //Start with defaults
        boolean incompleteBln = false;
        this.imageButtonDone.setVisibility(View.VISIBLE);
        this.textViewAction.setText(cAppExtension.context.getString(R.string.message_scan_article));


        //Check if quantityDbl is sufficient
        if (cPickorderLine.currentPickOrderLine.quantityHandledDbl < cPickorderLine.currentPickOrderLine.getQuantityDbl()) {
            this.imageButtonDone.setImageResource(R.drawable.ic_check_black_24dp);
            incompleteBln = true;
        }

        //PV
        if (cPickorder.currentPickOrder.isPVBln()) {
            //We have to scan a pickcart/salesorder after each article scan
            if (cPickorder.currentPickOrder.isPickPickPVVKOEachPieceBln()) {

                //Not complete and article last scanned so we have to scan a pickcart/salesorder, set the instruction
                if ( incompleteBln) {
                    this.imageButtonDone.setImageResource(R.drawable.ic_check_black_24dp);
                    this.textViewAction.setText(cAppExtension.context.getString(R.string.message_scan_pickcart_or_salesorder));
                    return;
                }

                //We have to scan a processingsequence to complete the line, so we are not ready
                if ( !incompleteBln) {
                    this.imageButtonDone.setImageResource(R.drawable.ic_check_black_24dp);
                    this.textViewAction.setText(cAppExtension.context.getString(R.string.message_scan_pickcart_or_salesorder));
                    return;
                }
                //We are complete, so we are done, will be detected later
            }

            //We can scan all articles first and then pickcart/salesorder, so set this as the instruction
            if (incompleteBln && cPickorderLine.currentPickOrderLine.getProcessingSequenceStr().isEmpty()) {
                this.textViewAction.setText(cAppExtension.context.getString(R.string.scan_article_or_salesorder_or_pickcartbox));
                return;
            }

            // We reached the quantityDbl, but still have to scan the pickcart/salesorder, so set the instruction
            if (cPickorderLine.currentPickOrderLine.getProcessingSequenceStr().isEmpty()) {
                this.imageButtonDone.setImageResource(R.drawable.ic_check_black_24dp);
                this.textViewAction.setText(cAppExtension.context.getString(R.string.message_scan_pickcart_or_salesorder));
                this.imageButtonDone.setVisibility(View.INVISIBLE);
                return;
            }

            //We picked less then required, and we have a processing sequence so we are done anyway
            if (incompleteBln && !cPickorderLine.currentPickOrderLine.getProcessingSequenceStr().isEmpty() && !cPickorder.currentPickOrder.isPickPickPVVKOEachPieceBln() ) {
                incompleteBln = false;
            }

        }

        //If we are incomplete, we are done here
        if (incompleteBln)  {
            return;
        }

        if (!cSetting.PICK_AUTO_ACCEPT()) {
            this.imageButtonDone.setVisibility(View.VISIBLE);
            this.imageButtonDone.setImageResource(R.drawable.ic_doublecheck_black_24dp);
            return;
        }

        this.mPickDone();
    }

    private  void mSendPickorderLine() {

        //If internet is not connected
        if (!cConnection.isInternetConnectedBln()) {
            //could not send line, let user know but answer succes so user can go to next line
            cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.couldnt_send_line), null);
            cPickorderLine.currentPickOrderLine.pErrorSending();
            return;
        }


        if (!cPickorderLine.currentPickOrderLine.pHandledBln()) {
            //could not send line, let user know but answer succes so user can go to next line
            cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.couldnt_send_line), null);
            cPickorderLine.currentPickOrderLine.pErrorSending();
        }

    }

    private void mHandlePickDoneClick() {

        //Check if we picked less then asked, if so then show dialog
        if (!cPickorderLine.currentPickOrderLine.getQuantityHandledDbl().equals(cPickorderLine.currentPickOrderLine.getQuantityDbl()) ) {
            this.mShowUnderPickDialog(cAppExtension.activity.getString(R.string.message_cancel_line), cAppExtension.activity.getString(R.string.message_accept_line));
            return;
        }

        //All is done
        this.mPickDone();

    }

    private  void mPickDone() {
        this.mSendPickorderLine();
    }

    //Listeners
    private void mSetDoneListener() {
        this.imageButtonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mHandlePickDoneClick();
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
                    pickCounterPlusHelperInt = 0;
                }

                return false;
            }
        });

        this.imageButtonPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //There is no selected barcodeStr, select one first
                if (cPickorderBarcode.currentPickorderBarcode == null) {
                    cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.message_select_one_of_multiple_barcodes),null);
                    return;
                }

                mTryToChangePickedQuantity(true, false, cPickorderBarcode.currentPickorderBarcode.getQuantityPerUnitOfMeasureDbl());
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
                    pickCounterMinusHelperInt = 0;
                }
                return false;
            }

        });

        this.imageButtonMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //There is no selected barcodeStr, select one first
                if (cPickorderBarcode.currentPickorderBarcode == null) {
                    cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.message_select_one_of_multiple_barcodes),null);
                    return;
                }


                mTryToChangePickedQuantity(false, false, cPickorderBarcode.currentPickorderBarcode.getQuantityPerUnitOfMeasureDbl());
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
        quantityRequiredText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNumberClicked();
            }
        });
    }

    //Dialogs and Activitys

//    private void mSetArticleInfoButtonListener() {
//        this.imageButtonArticleInfo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mShowArticleInfoFragment();
//            }
//        });
//    }

    private  void mShowFullArticleFragment() {

        cUserInterface.pCheckAndCloseOpenDialogs();

        ArticleFullViewFragment articleFullViewFragment = new ArticleFullViewFragment();
        articleFullViewFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.ARTICLEFULL_TAG);



    }

    private  void mShowUnderPickDialog(String pvRejectStr,String pvAcceptStr) {

        cUserInterface.pCheckAndCloseOpenDialogs();

        final AcceptRejectFragment acceptRejectFragment = new AcceptRejectFragment(cAppExtension.activity.getString(R.string.message_underpick_header),
                cAppExtension.activity.getString(R.string.message_underpick_text,
                        cText.pDoubleToStringStr(cPickorderLine.currentPickOrderLine.getQuantityDbl()),
                        cText.pDoubleToStringStr(cPickorderLine.currentPickOrderLine.getQuantityHandledDbl())),
                pvRejectStr,
                pvAcceptStr ,
                false);
        acceptRejectFragment.setCancelable(true);
        cAppExtension.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // show my popup
                acceptRejectFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.ACCEPTREJECTFRAGMENT_TAG);
            }
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

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // show my popup
                acceptRejectFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.ACCEPTREJECTFRAGMENT_TAG);
            }
        });
    }

    private  void mGoBackToLinesActivity() {

        //Reset current branch
        if (cPickorder.currentPickOrder.destionationBranch() == null) {
            cPickorder.currentPickOrder.scannedBranch =  null;
        }

        Intent intent = new Intent(cAppExtension.context, PickorderLinesActivity.class);
        PickorderLinesActivity.startedViaOrderSelectBln = false;

        cAppExtension.activity.startActivity(intent);
        cAppExtension.activity.finish();
    }

    private void mShowBarcodeSelectFragment() {
        BarcodeFragment barcodeFragment = new BarcodeFragment();
        barcodeFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.BARCODEPICKERFRAGMENT_TAG);
    }

    private void mShowNumberPickerFragment() {

        cUserInterface.pCheckAndCloseOpenDialogs();

        Bundle bundle = new Bundle();
        bundle.putInt(cPublicDefinitions.NUMBERINTENT_CURRENTQUANTITY, cPickorderLine.currentPickOrderLine.getQuantityHandledDbl().intValue());
        bundle.putDouble(cPublicDefinitions.NUMBERINTENT_MAXQUANTITY, cPickorderLine.currentPickOrderLine.getQuantityDbl().intValue());

        NumberpickerFragment numberpickerFragment = new NumberpickerFragment();
        numberpickerFragment.setArguments(bundle);

        numberpickerFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.NUMBERPICKERFRAGMENT_TAG);
    }

    private  void mShowOverpickNotAllowed(){
        this.quantityText.setText(quantityRequiredText.getText());
        cUserInterface.pShowSnackbarMessage(textViewAction , cAppExtension.context.getString(R.string.number_cannot_be_higher), null, false);
        cUserInterface.pDoNope(quantityText, true, true);
        cUserInterface.pDoNope(quantityRequiredText, false, false);
    }

    private void mFillDestinationSpinner() {

        List<String> destinationObl = new ArrayList<>();

        destinationObl.add(cAppExtension.activity.getString(R.string.message_select_a_destination));

        for (cBranch branch :cBranch.allBranchesObl ) {

            if (branch.getBranchStr().equalsIgnoreCase(cUser.currentUser.currentBranch.getBranchStr())) {
                continue;
            }

            destinationObl.add(branch.getBranchNameStr());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(cAppExtension.context,
                                                          android.R.layout.simple_spinner_dropdown_item,
                                                          destinationObl);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.destinationSpinner.setAdapter(adapter);

        if (cPickorderLine.currentPickOrderLine == null || cPickorderLine.currentPickOrderLine.getDestinationNoStr().isEmpty()) {
            return;
        }

        this.destinationSpinner.setSelection(adapter.getPosition(cPickorderLine.currentPickOrderLine.getDestinationDescriptionStr()));

    }

    private cResult mHandleScanRst(cBarcodeScan pvBarcodeScan){

        cResult result = new cResult();
        result.resultBln = true;

        boolean scanHandledBln = false;

        //We scanned an article, so first check if we already have a current baroce

        //We have a current barcode
        if (cPickorderBarcode.currentPickorderBarcode != null) {
            //Handle the scan for the current barcode
            result = this.mHandleBarcodeScanForCurrentItemRst(pvBarcodeScan);
            if (!result.resultBln) {
                scanHandledBln = true;
            }
            else
            {
                result.resultBln = false;
                result.pAddErrorMessage(cAppExtension.activity.getString(R.string.error_barcode_doesnt_belong_to_line));
                return result;
            }

        }

        //If we still need to handle the scan, do this
        if (!scanHandledBln){

            //Check if we already know the barcode
            cPickorderBarcode.currentPickorderBarcode = cPickorderBarcode.pGetPickbarcodeViaBarcode(pvBarcodeScan);

            //This is a new barcode
            if (cPickorderBarcode.currentPickorderBarcode == null) {
                //if something went wrong we are done, so return result
                result = this.mHandleUnknownBarcodeScanRst(pvBarcodeScan);
                if (!result.resultBln) {
                    return  result;
                }
            }
            else {
                //Get article from cache
                cPickorder.currentPickOrder.currentArticle = cPickorder.currentPickOrder.articleObl.get( cPickorderBarcode.currentPickorderBarcode.getItemNoAndVariantCodeStr());
                if ( cPickorder.currentPickOrder.currentArticle == null) {
                    result.resultBln = false;
                    result.pAddErrorMessage(cAppExtension.activity.getString(R.string.message_unknown_article));
                    return  result;
                }
            }
        }

        //Scanned barcode matches current barcode so raise barcode scanned
        this.mArticleBarcodeScanned();
        this.mFieldsInitialize();

        //if we receive false, we are done and scan is handled
        result.resultBln = true;
        return  result;

    }

    private void mArticleBarcodeScanned() {

        cResult result;

        cUserInterface.pCheckAndCloseOpenDialogs();

//        result = this.mGetArticleStockForBINRst();
//        if (!result.resultBln) {
//            this.mStepFailed(result.messagesStr());
//            this.imageButtonDone.setVisibility(View.INVISIBLE);
//            result.resultBln = false;
//            return;
//        }
//
//        this.mShowBarcodeInfo();
//        this.mTryToChangeQuantity(true, false, cMoveorder.currentMoveOrder.currentMoveorderBarcode.getQuantityPerUnitOfMeasureDbl());
    }

    private cResult mGetArticleStockForBINRst(){

        cResult result = new cResult();
        result.resultBln = true;

        if (!cMoveorder.currentMoveOrder.isMoveValidateStockBln() || cMoveorder.currentMoveOrder.currentBranchBin == null || cMoveorder.currentMoveOrder.currentArticle == null) {
            this.quantityRequiredText.setVisibility(View.GONE);
            return result;
        }

//        if ( this.articleStock != null) {
//            return  result;
//        }
//
//        this.quantityRequiredText.setVisibility(View.VISIBLE);
//
//
//        cUserInterface.pShowGettingData();
//
//        this.articleStock = cMoveorder.currentMoveOrder.currentArticle.pGetStockForBINViaWebservice(cMoveorder.currentMoveOrder.currentBranchBin.getBinCodeStr());
//        if (articleStock == null) {
//            this.quantityRequiredText.setVisibility(View.GONE);
//            result.resultBln = false;
//            result.pAddErrorMessage(cAppExtension.activity.getString(R.string.message_no_stock_available));
//            return result;
//        }
//
//        this.quantityRequiredText.setText(cText.pDoubleToStringStr(articleStock.getQuantityDbl()));
//        cUserInterface.pHideGettingData();
        return  result;
    }

    private cResult mHandleUnknownBarcodeScanRst(cBarcodeScan pvBarcodeScan) {

        cResult result = new cResult();
        result.resultBln = true;
        result = this.mAddUnknownScanRst(pvBarcodeScan);
        return  result;
    }

    private cResult mAddUnknownScanRst(cBarcodeScan pvBarcodeScan) {

        cResult result = new cResult();
        result.resultBln = true;

//        //We can add a line, but we don't check with the ERP, so add line and open it
//        if (!cMoveorder.currentMoveOrder.isMoveBarcodeCheckBln()) {
//            result = this.mAddUnkownArticleRst(pvBarcodeScan);
//            return result;
//        }
//
//        //We can add a line, and we need to check with the ERP, so check, add and open it
//        result = this.mAddERPArticleRst(pvBarcodeScan);
        return  result;
    }

    private cResult mHandleBarcodeScanForCurrentItemRst(cBarcodeScan pvBarcodeScan) {

        cResult result = new cResult();
        result.resultBln = true;

        //Check if scanned barcode matched the current barcode
        if (cPickorderBarcode.currentPickorderBarcode.getBarcodeStr().equalsIgnoreCase(pvBarcodeScan.getBarcodeOriginalStr())) {
            result.resultBln = false;
            return  result;
        }

        //We have a different barcode, so check if this barcode belong to the current article
        for (cArticleBarcode articleBarcode : cPickorder.currentPickOrder.currentArticle.barcodesObl) {
            if (articleBarcode.getBarcodeStr().equalsIgnoreCase(pvBarcodeScan.getBarcodeOriginalStr()) ||
                articleBarcode.getBarcodeWithoutCheckDigitStr().equalsIgnoreCase(pvBarcodeScan.getBarcodeOriginalStr())) {

                //We have a match, try to get this barcode from the order
                cPickorderBarcode.currentPickorderBarcode = cPickorderBarcode.pGetPickbarcodeViaBarcode(pvBarcodeScan);

                // We are done
                if (cPickorderBarcode.currentPickorderBarcode != null) {
                    result.resultBln = false;
                    return result;
                }

                //We don't have a match, so add new barcode to the order originated from article barcode
                cPickorderBarcode pickorderBarcode = new cPickorderBarcode(articleBarcode);
                cPickorderBarcode.allBarcodesObl.add(pickorderBarcode);
                cPickorderBarcode.currentPickorderBarcode = pickorderBarcode;

                //Set result to false, so we know that we just can raise the quantity
                result.resultBln = false;
                return result;
            }
        }

        result.resultBln = true;
        return result;

    }

    //Region Number Broadcaster

    private Runnable mMinusAction = new Runnable() {
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

    private Runnable mPlusAction = new Runnable() {
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
        this.pickCounterMinusHelperInt += 1;
    }

    private void mDoDelayedPlus(Runnable pvRunnable, long pvMilliSecsLng) {
        this.plusHandler.postDelayed(pvRunnable, pvMilliSecsLng);
        this.pickCounterPlusHelperInt += 1;
    }

    //End Region Number Broadcaster

    //End Regin Private Methods

}

