package nl.icsvertex.scansuite.Activities.Move;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
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
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.material.navigation.NavigationView;

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
import SSU_WHS.Basics.BarcodeLayouts.cBarcodeLayout;
import SSU_WHS.Basics.Settings.cSetting;
import SSU_WHS.General.cPublicDefinitions;
import SSU_WHS.Move.MoveOrders.cMoveorder;
import SSU_WHS.Move.MoveorderBarcodes.cMoveorderBarcode;
import SSU_WHS.Move.MoveorderLines.cMoveorderLine;
import nl.icsvertex.scansuite.Fragments.Dialogs.AcceptRejectFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.ArticleFullViewFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.BarcodeFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.BinItemsFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.ItemStockFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.NumberpickerFragment;
import nl.icsvertex.scansuite.R;

import static ICS.Utils.cText.pDoubleToStringStr;

public class MoveLinePlaceMTActivity extends AppCompatActivity implements iICSDefaultActivity {

    //Region Private Properties
    private int counterMinusHelperInt;
    private int counterPlusHelperInt;
    private Handler minusHandler;
    private Handler plusHandler;
    private AppCompatImageButton imageButtonMinus;
    private AppCompatImageButton imageButtonPlus;
    private AppCompatImageButton imageButtonDone;

    private ConstraintLayout moveLinePlaceContainer;

    private  Toolbar toolbar;
    private ImageView toolbarImage;
    private TextView toolbarTitle;
    private TextView toolbarSubtext;

    private TextView articleDescriptionText;
    private TextView articleDescription2Text;
    private TextView articleItemText;
    private TextView articleBarcodeText;
    private ImageView articleThumbImageView;

    private ImageView imageButtonBarcode;

    private CardView binContainer;
    private TextView binText;

    private TextView quantityText;
    private TextView quantityRequiredText;

    private TextView textViewAction;

    private Double quantityScannedDbl = 0.0;
    private List<cMoveorderBarcode> scannedBarcodesObl;

    private DrawerLayout menuActionsDrawer;
    private NavigationView actionMenuNavigation;

    private  CardView articleContainer;
    private ConstraintLayout articleInfoContainer;


    //End Region Private Properties

    //Region Default Methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_moveline_place);
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
    public boolean onCreateOptionsMenu(Menu pvMenu) {
        getMenuInflater().inflate(R.menu.menu_stockactions,pvMenu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu pvMenu) {
        invalidateOptionsMenu();

        if (!cMoveorder.currentMoveOrder.isMoveValidateStockBln())  {
            return true;
        }

        if (cMoveorder.currentMoveOrder.currentBranchBin != null) {
            pvMenu.findItem(R.id.item_bin_stock).setVisible(true);
        }
        else {
            pvMenu.findItem(R.id.item_bin_stock).setVisible(false);
        }

        if (cMoveorder.currentMoveOrder.currentArticle != null) {
            pvMenu.findItem(R.id.item_article_stock).setVisible(true);
        }
        else {
            pvMenu.findItem(R.id.item_article_stock).setVisible(false);
        }

        return super.onPrepareOptionsMenu(pvMenu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem pvMenuItem) {

        DialogFragment selectedFragment = null;

        switch (pvMenuItem.getItemId()) {

            case android.R.id.home:
                if (this.quantityScannedDbl == 0) {
                    this.mResetCurrents();
                    this.mGoBackToLinesActivity();
                    return  true;
                }

                this.mShowAcceptFragment();
                return true;

            case R.id.item_bin_stock:
                selectedFragment = new BinItemsFragment(cMoveorder.currentMoveOrder.currentBranchBin.getBinCodeStr());
                break;

            case R.id.item_article_stock:

                cArticle.currentArticle= cMoveorder.currentMoveOrder.currentArticle;
                selectedFragment = new ItemStockFragment();
                break;

            default:
                break;
        }


        // deselect everything
        int size = actionMenuNavigation.getMenu().size();
        for (int i = 0; i < size; i++) {
            actionMenuNavigation.getMenu().getItem(i).setChecked(false);
        }

        // set item as selected to persist highlight
        pvMenuItem.setChecked(true);
        // close drawer when item is tapped
        this.menuActionsDrawer.closeDrawers();

        if (selectedFragment != null) {
            selectedFragment.setCancelable(true);
            selectedFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.BINITEMSFRAGMENT_TAG);
        }

        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(cAppExtension.context).unregisterReceiver(mNumberReceiver);

        if (cAppExtension.activity instanceof MoveLinePlaceMTActivity) {
            cBarcodeScan.pUnregisterBarcodeReceiver(this.getClass().getSimpleName());
        }

    }

    @Override
    protected void onPause() {
        try {
            cBarcodeScan.pUnregisterBarcodeReceiver(this.getClass().getSimpleName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        LocalBroadcastManager.getInstance(cAppExtension.context).unregisterReceiver(mNumberReceiver);
        cBarcodeScan.pUnregisterBarcodeReceiver(this.getClass().getSimpleName());
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
    public void onBackPressed() {

        if (this.quantityScannedDbl == 0 ) {
            this.mResetCurrents();
            this.mGoBackToLinesActivity();
            return;
        }

        this.mShowAcceptFragment();
    }

    //End Region Default Methods

    //Region iICSDefaultActivity Methods

    @Override
    public void mActivityInitialize() {

        this.mSetAppExtensions();

        this.mFindViews();

        this.mSetToolbar(getResources().getString(R.string.screentitle_moveline_place));

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

        this.moveLinePlaceContainer = findViewById(R.id.moveLinePlaceContainer);

        this.toolbar = findViewById(R.id.toolbar);
        this.toolbarImage = findViewById(R.id.toolbarImage);
        this.toolbarTitle = findViewById(R.id.toolbarTitle);
        this.toolbarSubtext = findViewById(R.id.toolbarSubtext);

        this.articleContainer = findViewById(R.id.articleContainer);
        this.articleInfoContainer = findViewById(R.id.articleInfoContainer);

        this.articleDescriptionText = findViewById(R.id.articleDescriptionText);
        this.articleDescription2Text = findViewById(R.id.articleDescription2Text);
        this.articleItemText = findViewById(R.id.articleItemText);
        this.articleBarcodeText = findViewById(R.id.articleBarcodeText);

        this.binContainer = findViewById(R.id.binContainer);
        this.binText = findViewById(R.id.binText);

        this.quantityText = findViewById(R.id.quantityText);
        this.quantityRequiredText = findViewById(R.id.quantityRequiredText);

        this.articleThumbImageView = findViewById(R.id.articleThumbImageView);
        this.imageButtonBarcode = findViewById(R.id.imageButtonBarcode);
        this.imageButtonMinus = findViewById(R.id.imageButtonMinus);
        this.imageButtonPlus = findViewById(R.id.imageButtonPlus);
        this.imageButtonDone = findViewById(R.id.imageButtonDone);

        this.textViewAction = findViewById(R.id.textViewAction);

        this.menuActionsDrawer = findViewById(R.id.menuActionsDrawer);
        this.actionMenuNavigation = findViewById(R.id.actionMenuNavigation);

    }

    @Override
    public void mSetToolbar(String pvScreenTitleStr) {
        this.toolbarImage.setImageResource(R.drawable.ic_menu_move);
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

        if (cMoveorderLine.currentMoveOrderLine == null) {
            return;
        }

        cMoveorder.currentMoveOrder.currenLineModusEnu = cMoveorder.lineModusEnu.PLACE;

        this.counterPlusHelperInt = 0;
        this.counterMinusHelperInt = 0;
        this.toolbarSubtext.setText(cMoveorder.currentMoveOrder.getOrderNumberStr());

        this.mSetArticleInfo();
        this.mSetBinInfo();
        this.mSetQuantityInfo();

        this.mEnablePlusMinusAndBarcodeSelectViews();
        this.mShowArticleImage();
        this.mShowOrHideGenericExtraFields();
        this.mShowBarcodeInfo();
        this.mSetInstructions();

        this.mHideArticleInfo();
    }

    @Override
    public void mInitScreen() {


        //Raise quantity with scanned barcodeStr, if we started this activity with a scan
        if (cMoveorder.currentMoveOrder.moveorderBarcodeToHandle!= null) {
            this.pHandleScan(cBarcodeScan.pFakeScan(cMoveorder.currentMoveOrder.moveorderBarcodeToHandle.getBarcodeStr()));
            cMoveorder.currentMoveOrder.moveorderBarcodeToHandle =null;
        }

    }

    @Override
    public void mSetListeners() {

        this.mSetArticleImageListener();
        this.mSetImageButtonBarcodeListener();

        if (cMoveorder.currentMoveOrder.getMoveAmountManualBln()) {
            this.mSetNumberListener();
        }

        this.mSetPlusListener();
        this.mSetMinusListener();
        this.mSetDoneListener();

    }

    // End Region iICSDefaultActivity Methods

    //Region Public Methods

    public void pHandleScan(cBarcodeScan pvBarcodeScan) {

        cUserInterface.pCheckAndCloseOpenDialogs();

        //BIN scan is not allowed here
        if (cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(), cBarcodeLayout.barcodeLayoutEnu.BIN)) {
            cUserInterface.pShowSnackbarMessage(this.moveLinePlaceContainer,cAppExtension.activity.getString(R.string.message_bin_not_allowed),null,true);
            return;
        }

        //Scan must be an article, so handle the scan
        cResult hulpRst = this.mHandleScanRst(pvBarcodeScan);
        if (! hulpRst.resultBln) {
            cUserInterface.pShowSnackbarMessage(this.moveLinePlaceContainer,hulpRst.messagesStr(),null,true);
           this.mSetInstructions();
           this.mFieldsInitialize();
        }

    }

    public void pAcceptMove() {

        if (cMoveorder.currentMoveOrder.currentBranchBin == null) {
            cUserInterface.pShowSnackbarMessage(moveLinePlaceContainer, cAppExtension.activity.getString(R.string.message_bin_required),null, true);
            return;
        }

        if (cMoveorder.currentMoveOrder.currentArticle == null) {
            cUserInterface.pShowSnackbarMessage(moveLinePlaceContainer, cAppExtension.activity.getString(R.string.message_article_required),null, true);
            return;
        }

        if (this.quantityScannedDbl == 0 ) {
            this.mResetCurrents();
            this.mGoBackToLinesActivity();
            return;
        }

        cResult result = this.mMoveDoneRst();
        if (!result.resultBln) {
            cUserInterface.pDoExplodingScreen(result.messagesStr(),"",null, true);
        }
    }

    public  void pCancelMove() {
        this.mResetCurrents();
        this.mGoBackToLinesActivity();
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

                if (cMoveorderLine.currentMoveOrderLine.orderBarcodesObl() == null || cMoveorderLine.currentMoveOrderLine.orderBarcodesObl().size() == 0) {
                    return;
                }

                mEnablePlusMinusAndBarcodeSelectViews();

                //If we only have one barcodeStr, then automatticaly select that barcodeStr
                if (cMoveorderLine.currentMoveOrderLine.orderBarcodesObl().size() == 1) {
                    pHandleScan(cBarcodeScan.pFakeScan(cMoveorderLine.currentMoveOrderLine.orderBarcodesObl().get(0).getBarcodeStr()));
                    return;
                }

                mShowBarcodeSelectFragment();

            }
        });
    }

    private void mShowOrHideGenericExtraFields() {
    }

    private void mShowBarcodeInfo() {

        if (cMoveorder.currentMoveOrder.currentMoveorderBarcode == null) {
            this.articleBarcodeText.setText("???");
            return;
        }

        this.articleBarcodeText.setText((cMoveorder.currentMoveOrder.currentMoveorderBarcode.getBarcodeAndQuantityStr()));

    }

    private void mShowArticleImage() {

        this.articleThumbImageView.setImageDrawable(ContextCompat.getDrawable(cAppExtension.context, R.drawable.ic_no_image_lightgrey_24dp));

        //If pick with picture is false, then hide image view
        if (!cSetting.MOVE_WITH_PICTURE()) {
            this.articleThumbImageView.setVisibility(View.INVISIBLE);
            return;
        }

        //If picture is not in cache (via webservice) then show no image
        if (cMoveorder.currentMoveOrder.currentArticle == null ||  cMoveorder.currentMoveOrder.currentArticle .pGetArticleImageBln()) {
            cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.could_not_get_article_image), null);
            this.articleThumbImageView.setImageDrawable(ContextCompat.getDrawable(cAppExtension.context, R.drawable.ic_no_image_lightgrey_24dp));
            return;
        }

        //If picture is in cache but can't be converted, then show no image
        if (cMoveorder.currentMoveOrder.currentArticle .articleImage == null || cMoveorder.currentMoveOrder.currentArticle.articleImage.imageBitmap() == null) {
            cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.could_not_get_article_image), null);
            this.articleThumbImageView.setImageDrawable(ContextCompat.getDrawable(cAppExtension.context, R.drawable.ic_no_image_lightgrey_24dp));
            return;
        }

        //Show the image
        this.articleThumbImageView.setImageBitmap(cMoveorder.currentMoveOrder.currentArticle .articleImage.imageBitmap());

        //Open the image
        if ((cSetting.MOVE_WITH_PICTURE_AUTO_OPEN())) {
            this.mShowFullArticleFragment();
        }

    }

    private void mEnablePlusMinusAndBarcodeSelectViews() {

        if (cMoveorder.currentMoveOrder.currentMoveorderBarcode == null) {
            this.imageButtonMinus.setVisibility(View.INVISIBLE);
            this.imageButtonPlus.setVisibility(View.INVISIBLE);
            this.imageButtonBarcode.setVisibility(View.INVISIBLE);
            return;
        }

        if (!cSetting.MOVE_AMOUNT_MANUAL()) {
            this.imageButtonMinus.setVisibility(View.INVISIBLE);
            this.imageButtonPlus.setVisibility(View.INVISIBLE);
            this.imageButtonBarcode.setVisibility(View.INVISIBLE);
        } else {
            this.imageButtonMinus.setVisibility(View.VISIBLE);
            this.imageButtonPlus.setVisibility(View.VISIBLE);
            this.imageButtonBarcode.setVisibility(View.VISIBLE);
        }
    }

    private void mShowExtraPiecesNotAllowed(){
        this.quantityText.setText(this.quantityRequiredText.getText());
        cUserInterface.pShowSnackbarMessage(this.moveLinePlaceContainer, cAppExtension.context.getString(R.string.number_cannot_be_higher), null, false);
        cUserInterface.pDoNope(this.quantityText, true, true);
        cUserInterface.pDoNope(this.quantityRequiredText, false, false);
    }

    private void mStepFailed(String pvErrorMessageStr) {
        cUserInterface.pDoExplodingScreen(pvErrorMessageStr, "", true, true);
    }

    private void mHideArticleInfo(){

        this.articleInfoContainer.setVisibility(View.GONE);
        ConstraintLayout.LayoutParams newCardViewLayoutParams = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        newCardViewLayoutParams.setMargins(15,15,15,15);
        this.articleContainer.setLayoutParams(newCardViewLayoutParams);

        ConstraintSet constraintSetSpace = new ConstraintSet();
        constraintSetSpace.clone(this.moveLinePlaceContainer);
        constraintSetSpace.connect(this.articleContainer.getId(), ConstraintSet.TOP, toolbar.getId(), ConstraintSet.BOTTOM);
        constraintSetSpace.applyTo(this.moveLinePlaceContainer);

    }

    //Scans and manual input

    private cResult mHandleScanRst(cBarcodeScan pvBarcodeScan){

        cResult result = new cResult();
        result.resultBln = true;

        //Handle the scan for the current barcode
        result = this.mHandleBarcodeScanForCurrentItemRst(pvBarcodeScan);
        if (!result.resultBln) {
            this.mStepFailed(result.messagesStr());
            return result;
        }

        //Scanned barcode matches current barcode so raise barcode scanned
        this.mBarcodeScanned();
        this.mFieldsInitialize();

        //if we receive false, we are done and scan is handled
        result.resultBln = true;
        return  result;

    }

    private cResult mHandleBarcodeScanForCurrentItemRst(cBarcodeScan pvBarcodeScan) {

        cResult result = new cResult();
        result.resultBln = true;

        //Check if scanned barcode matched the current barcode
        if (cMoveorder.currentMoveOrder.currentMoveorderBarcode.getBarcodeStr().equalsIgnoreCase(pvBarcodeScan.getBarcodeOriginalStr())) {
            result.resultBln = true;
            return  result;
        }

        //We have a different barcode, so check if this barcode belong to the current article
        for (cMoveorderBarcode moveorderBarcode : cMoveorderLine.currentMoveOrderLine.orderBarcodesObl()) {
            if (moveorderBarcode.getBarcodeStr().equalsIgnoreCase(pvBarcodeScan.getBarcodeOriginalStr()) ||
                    moveorderBarcode.getBarcodeWithoutCheckDigitStr().equalsIgnoreCase(pvBarcodeScan.getBarcodeOriginalStr())) {

                //We have a match, try to get this barcode from the order
                cMoveorder.currentMoveOrder.currentMoveorderBarcode = moveorderBarcode;
                result.resultBln = true;
                return result;
            }
        }

        result.resultBln = false;
        return result;

    }

    private void mNumberClicked() {

        if (cMoveorder.currentMoveOrder.currentMoveorderBarcode== null) {
            cUserInterface.pDoNope(quantityText, false, false);
            cUserInterface.pShowSnackbarMessage(moveLinePlaceContainer, getString(R.string.choose_barcode_first), null, false);
            return;
        }

        if (cMoveorder.currentMoveOrder.currentMoveorderBarcode.getQuantityPerUnitOfMeasureDbl() > 1) {
            cUserInterface.pDoNope(quantityText, true, true);
            cUserInterface.pShowSnackbarMessage(moveLinePlaceContainer, getString(R.string.manual_input_only_barcodenumber_bigger1), null, false);
            return;
        }

        this.mShowNumberFragment();
    }

    private void mHandleQuantityChosen(double pvQuantityDbl) {

        if (pvQuantityDbl == 0) {
            this.mTryToChangeQuantity(false, true,pvQuantityDbl);
        }
        else {
            this.mTryToChangeQuantity(true, true,pvQuantityDbl);
            this.mCheckLineDone();
        }

    }

    private void mTryToChangeQuantity(Boolean pvIsPositiveBln, Boolean pvAmountFixedBln, double pvAmountDbl) {

      double newQuantityDbl;


        if ( this.scannedBarcodesObl == null) {
            this.scannedBarcodesObl = new ArrayList<>();
        }

        if (pvIsPositiveBln) {

            //Determine the new amount
            if (pvAmountFixedBln) {
                newQuantityDbl = pvAmountDbl;

                //Check if we would exceed   available, then show message
                if (newQuantityDbl > cMoveorderLine.currentMoveOrderLine.getQuantityPlaceable()) {
                    this.mShowExtraPiecesNotAllowed();
                    return;
                }


                //Clear the barcodeStr list and refill it
                this.scannedBarcodesObl.clear();
                int countInt = 0;
                do{
                    countInt += 1;
                    //Add a barcodeStr to the scanned barcodeStr list, so you can use it later when line is determined
                    this.scannedBarcodesObl.add(cMoveorder.currentMoveOrder.currentMoveorderBarcode);
                }
                while(countInt < newQuantityDbl);

                //Update activity and Check if this line is done
                this.quantityScannedDbl = newQuantityDbl;
                this.quantityText.setText(cText.pDoubleToStringStr(this.quantityScannedDbl));
                this.mFieldsInitialize();
                return;


            } else {
                newQuantityDbl = this.quantityScannedDbl + pvAmountDbl;
            }

            //Check if we would exceed amount stock available, then show message
            if (newQuantityDbl > cMoveorderLine.currentMoveOrderLine.getQuantityPlaceable()) {
                this.mShowExtraPiecesNotAllowed();
                return;
            }

            //Set the new quantityDbl and show in Activity
            this.quantityScannedDbl = newQuantityDbl;
            this.quantityText.setText(pDoubleToStringStr(this.quantityScannedDbl));

            //Add a barcodeStr to the scanned barcodeStr list, so you can use it later when line is determined
            this.scannedBarcodesObl.add(cMoveorder.currentMoveOrder.currentMoveorderBarcode);

            //Check if this line is done
            this.mFieldsInitialize();
            return;
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
                return;
            }
        }

        if (pvAmountDbl < 0) {
            cUserInterface.pDoNope(this.quantityText, true, true);
            return;
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
                this.scannedBarcodesObl.add(cMoveorder.currentMoveOrder.currentMoveorderBarcode);
                            }while(countInt < newQuantityDbl);

            //Set the new quantityDbl and show in Activity
            this.quantityScannedDbl = newQuantityDbl;
            this.quantityText.setText(pDoubleToStringStr(this.quantityScannedDbl));
            return;


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

        this.quantityText.setText(pDoubleToStringStr(this.quantityScannedDbl));
    }

    private void mBarcodeScanned() {

        cUserInterface.pCheckAndCloseOpenDialogs();

        this.mShowBarcodeInfo();
        this.mTryToChangeQuantity(true, false, cMoveorder.currentMoveOrder.currentMoveorderBarcode.getQuantityPerUnitOfMeasureDbl());
        this.mCheckLineDone();
    }

    private void mSetInstructions() {

        cResult result =  new cResult();
        result.resultBln = true;

        this.imageButtonDone.setVisibility(View.VISIBLE);
        if (this.quantityScannedDbl < cMoveorderLine.currentMoveOrderLine.getQuantityPlaceable()) {
            this.imageButtonDone.setImageResource(R.drawable.ic_check_black_24dp);

        }
        else{
            this.imageButtonDone.setVisibility(View.VISIBLE);
            this.imageButtonDone.setImageResource(R.drawable.ic_doublecheck_black_24dp);
        }

        //If we don't have a current barcode, you can't close the line
        if (cMoveorder.currentMoveOrder.currentArticle == null) {
            this.textViewAction.setText(cAppExtension.activity.getString(R.string.message_scan_article));
            this.imageButtonDone.setVisibility(View.INVISIBLE);
            return;
        }

        this.textViewAction.setText(cAppExtension.activity.getString(R.string.message_scan_article_or_close_line));
    }

    private cResult mSendLineRst() {

        cResult result =  new cResult();
        result.resultBln = true;

        //If internet is not connected
        if (!cConnection.isInternetConnectedBln()) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.couldnt_send_line_no_connection));
            return result;
        }

       if(!cMoveorder.currentMoveOrder.pMovePlaceMTItemHandledBln(this.scannedBarcodesObl)) {
           result.resultBln = false;
           result.pAddErrorMessage(cAppExtension.context.getString(R.string.couldnt_send_line));
           return result;
        }

       cMoveorderLine.currentMoveOrderLine.quantityHandledDbl = this.quantityScannedDbl;
       cMoveorderLine.currentMoveOrderLine.handledBln = true;

        return  result;

    }

    private cResult mMoveDoneRst () {

        cResult result = this.mSendLineRst();
        if (!result.resultBln ) {
            return result;
        }

        this.mUpdatePlaceLines();

        this.mResetCurrents();
        this.mGoBackToLinesActivity();
        return result;


    }

    private void mUpdatePlaceLines(){

        for (cMoveorderLine moveorderLine : cMoveorderLine.currentMoveOrderLine.moveItemVariant().todoPlaceObl()) {

            if (moveorderLine.getQuantityPlaceable() == 0 ) {
                moveorderLine.handledBln = true;
            }

        }

    }

    private void mHandleDoneClick() {

        if (cMoveorderLine.currentMoveOrderLine == null) {
            return;
        }

        double quantityLeftDbl = cMoveorderLine.currentMoveOrderLine.moveItemVariant().pCheckPlaceLeftDbl(this.quantityScannedDbl);

        if (quantityLeftDbl > 0) {

            String messageStr =   cAppExtension.activity.getString(R.string.message_underplace_text,
                    cText.pDoubleToStringStr(this.quantityScannedDbl),
                    cText.pDoubleToStringStr(cMoveorderLine.currentMoveOrderLine.moveItemVariant().getQuantityTodoPlaceNewDbl(this.quantityScannedDbl)),
                    cText.pDoubleToStringStr(quantityLeftDbl));

            cUserInterface.pDoExplodingScreen(messageStr,"",true,true);
            return;
        }

        //All is done
        this.mMoveDoneRst();

    }

    private void mCheckLineDone(){


        if (cSetting.MOVE_AUTO_ACCEPT_AT_REQUESTED() && this.quantityScannedDbl.equals(cMoveorderLine.currentMoveOrderLine.getQuantityPlaceable())) {
            this.mHandleDoneClick();
        }

    }

    //Listeners
    private void mSetDoneListener() {
        this.imageButtonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mHandleDoneClick();
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
                if (cMoveorder.currentMoveOrder.currentMoveorderBarcode  == null) {
                    cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.message_select_one_of_multiple_barcodes),null);
                    return;
                }
                pHandleScan(cBarcodeScan.pFakeScan(cMoveorder.currentMoveOrder.currentMoveorderBarcode.getBarcodeStr()));
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
                if (cMoveorder.currentMoveOrder.currentMoveorderBarcode  == null) {
                    cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.message_select_one_of_multiple_barcodes),null);
                    return;
                }


                mTryToChangeQuantity(false, false, cMoveorder.currentMoveOrder.currentMoveorderBarcode.getQuantityPerUnitOfMeasureDbl());
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

    private void mShowFullArticleFragment() {

        cUserInterface.pCheckAndCloseOpenDialogs();

        ArticleFullViewFragment articleFullViewFragment = new ArticleFullViewFragment();
        articleFullViewFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.ARTICLEFULL_TAG);



    }

    private void mShowAcceptFragment(){

        cUserInterface.pCheckAndCloseOpenDialogs();

        final AcceptRejectFragment acceptRejectFragment = new AcceptRejectFragment(cAppExtension.activity.getString(R.string.message_orderbusy_header),
                                                                                   cAppExtension.activity.getString(R.string.message_orderbusy_text),
                                                                                   cAppExtension.activity.getString(R.string.message_cancel_line), cAppExtension.activity.getString(R.string.message_accept_line), false);
        acceptRejectFragment.setCancelable(true);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // show my popup
                acceptRejectFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.ACCEPTREJECTFRAGMENT_TAG);
            }
        });
    }

    private void mGoBackToLinesActivity() {
        Intent intent = new Intent(cAppExtension.context, MoveLinesPlaceMTActivity.class);
        cAppExtension.activity.startActivity(intent);
        cAppExtension.activity.finish();
    }

    private void mResetCurrents() {

        cMoveorderLine.currentMoveOrderLine = null;
        cMoveorder.currentMoveOrder.currentArticle = null;
        cMoveorder.currentMoveOrder.currentBranchBin = null;
        cMoveorder.currentMoveOrder.currentMoveorderBarcode = null;
        cMoveorder.currentMoveOrder.moveorderBarcodeToHandle = null;

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
        bundle.putInt(cPublicDefinitions.NUMBERINTENT_CURRENTQUANTITY, cMoveorder.currentMoveOrder.currentMoveorderBarcode.getQuantityHandledDbl().intValue());
        bundle.putDouble(cPublicDefinitions.NUMBERINTENT_MAXQUANTITY, cMoveorderLine.currentMoveOrderLine.getQuantityPlaceable() );
        NumberpickerFragment numberpickerFragment = new NumberpickerFragment();
        numberpickerFragment.setArguments(bundle);

        numberpickerFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.NUMBERFRAGMENT_TAG);
    }

    private void mSetArticleInfo(){

        if (cMoveorderLine.currentMoveOrderLine == null) {
            this.articleDescriptionText.setText("???");
            this.articleDescription2Text.setText("???");
            this.articleItemText.setText("???");
            return;
        }
        this.articleDescriptionText.setText(cMoveorderLine.currentMoveOrderLine.getDescriptionStr());
        this.articleDescription2Text.setText(cMoveorderLine.currentMoveOrderLine.getDescription2Str());
        this.articleItemText.setText(cMoveorderLine.currentMoveOrderLine.getItemNoAndVariantCodeStr());
    }

    private void mSetQuantityInfo(){

        this.quantityRequiredText.setText(cText.pDoubleToStringStr(cMoveorderLine.currentMoveOrderLine.getQuantityPlaceable()));

        if (cMoveorder.currentMoveOrder.currentMoveorderBarcode == null) {
            this.quantityText.setText("0");
            return;
        }

        this.quantityText.setText(cText.pDoubleToStringStr(this.quantityScannedDbl));

    }

    private void mSetBinInfo(){

        if (cMoveorder.currentMoveOrder.currentBranchBin == null) {
            this.binContainer.setVisibility(View.GONE);
            return;
        }

        this.binContainer.setVisibility(View.VISIBLE);
        this.binText.setText(cMoveorder.currentMoveOrder.currentBranchBin.getBinCodeStr());
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

    //End Region Number Broadcaster

    //End Regin Private Methods

}

