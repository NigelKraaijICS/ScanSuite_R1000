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
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ICS.Interfaces.iICSDefaultActivity;
import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cConnection;
import ICS.Utils.cRegex;
import ICS.Utils.cResult;
import ICS.Utils.cText;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Basics.BarcodeLayouts.cBarcodeLayout;
import SSU_WHS.Basics.BranchBin.cBranchBin;
import SSU_WHS.Basics.Settings.cSetting;
import SSU_WHS.Basics.Users.cUser;
import SSU_WHS.General.cPublicDefinitions;
import SSU_WHS.Move.MoveOrders.cMoveorder;
import SSU_WHS.Move.MoveorderBarcodes.cMoveorderBarcode;
import SSU_WHS.Move.MoveorderLines.cMoveorderLine;
import SSU_WHS.Move.MoveorderLines.cMoveorderLineAdapter;
import SSU_WHS.Move.MoveorderLines.cMoveorderLineRecyclerItemTouchHelper;
import nl.icsvertex.scansuite.Activities.Receive.ReceiveLinesActivity;
import nl.icsvertex.scansuite.Fragments.Dialogs.AcceptRejectFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.ArticleFullViewFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.BarcodeFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.NumberpickerFragment;
import nl.icsvertex.scansuite.R;

import static ICS.Utils.cText.pDoubleToStringStr;

public class MoveLinePlaceGeneratedActivity extends AppCompatActivity implements iICSDefaultActivity, cMoveorderLineRecyclerItemTouchHelper.RecyclerItemTouchHelperListener{

    //Region Private Properties
    private  int counterMinusHelperInt;
    private  int counterPlusHelperInt;
    private  Handler minusHandler;
    private  Handler plusHandler;
    private  AppCompatImageButton imageButtonMinus;
    private  AppCompatImageButton imageButtonPlus;
    private  AppCompatImageButton imageButtonDone;

    private  ConstraintLayout moveorderlinesPlaceGeneratedContainer;

    private  Toolbar toolbar;
    private  ImageView toolbarImage;
    private  TextView toolbarTitle;
    private  TextView toolbarSubtext;
    private TextView toolbarSubtext2;

    private  CardView articleContainer;
    private  ConstraintLayout articleInfoContainer;
    private  TextView articleDescriptionText;
    private  TextView articleDescription2Text;
    private  TextView articleItemText;
    private  TextView articleBarcodeText;
    private  ImageView articleThumbImageView;
    private  ImageView imageButtonBarcode;
    private ImageView imageButtonNoInputPropertys;

    private  CardView binContainer;
    private TextView binText;

    private  ConstraintLayout quantityControlsContainer;
    private  TextView quantityText;

    private  Double quantityScannedDbl = 0.0;
    private  List<cMoveorderBarcode> scannedBarcodesObl;

    private  RecyclerView recyclerScanActions;

    private cMoveorderLineAdapter moveorderLineAdapter;
    private cMoveorderLineAdapter getMoveorderLineAdapter(){
        if (this.moveorderLineAdapter == null) {
            this.moveorderLineAdapter = new cMoveorderLineAdapter();
        }
        return  this.moveorderLineAdapter;
    }

    //End Region Private Properties

    //Region Default Methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moveorderline_place_generated);
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
    public boolean onCreateOptionsMenu(Menu pvMenu) {
        getMenuInflater().inflate(R.menu.menu_intakeactions,pvMenu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu pvMenu) {
        invalidateOptionsMenu();


        MenuItem item_enter_bin = pvMenu.findItem(R.id.item_enter_bin);
        item_enter_bin.setVisible(true);

        if (cMoveorder.currentMoveOrder.currentBranchBin != null)  {
            item_enter_bin.setVisible(false);
            return true;
        }

        return super.onPrepareOptionsMenu(pvMenu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem pvMenuItem) {

        if (pvMenuItem.getItemId() == android.R.id.home) {
            if (this.quantityScannedDbl == 0) {
                this.mResetCurrents();
                this.mGoBackToLinesActivity();
                return true;
            }

            this.mShowAcceptFragment();
            return true;
        }

        return true;
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


    @Override
    public void onSwiped(RecyclerView.ViewHolder pvViewHolder, int direction, int pvPositionInt) {

        if (!(pvViewHolder instanceof  cMoveorderLineAdapter.MoveorderLineViewHolder)) {
            return;
        }

        cMoveorderLine.currentMoveOrderLine = cMoveorder.currentMoveOrder.placeLineForBinReversedObl(cMoveorder.currentMoveOrder.currentBranchBin.getBinCodeStr()).get(pvPositionInt);

        //Remove the enviroment
        this.mRemoveAdapterFromFragment();

    }

    //End Region Default Methods

    //Region iICSDefaultActivity Methods

    @Override
    public void mActivityInitialize() {

        this.mSetAppExtensions();

        this.mFindViews();

        this.mSetToolbar(getResources().getString(R.string.screentitle_moveorderlines_place));

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

        this.moveorderlinesPlaceGeneratedContainer = findViewById(R.id.moveorderlinesPlaceGeneratedContainer);

        this.toolbar = findViewById(R.id.toolbar);
        this.toolbarImage = findViewById(R.id.toolbarImage);
        this.toolbarTitle = findViewById(R.id.toolbarTitle);
        this.toolbarSubtext = findViewById(R.id.toolbarSubtext);
        this.toolbarSubtext2 = findViewById(R.id.toolbarSubtext2);

        this.articleDescriptionText = findViewById(R.id.articleDescriptionText);
        this.articleDescription2Text = findViewById(R.id.articleDescription2Text);
        this.articleItemText = findViewById(R.id.articleItemText);
        this.articleBarcodeText = findViewById(R.id.articleBarcodeText);

        this.binContainer = findViewById(R.id.binContainer);
        this.binText = findViewById(R.id.binText);

        this.quantityControlsContainer = findViewById(R.id.quantityControlsContainer);
        this.quantityText = findViewById(R.id.quantityText);

        this.articleThumbImageView = findViewById(R.id.articleThumbImageView);
        this.imageButtonBarcode = findViewById(R.id.imageButtonBarcode);
        this.imageButtonNoInputPropertys = findViewById(R.id.imageButtonNoInputPropertys);

        this.imageButtonMinus = findViewById(R.id.imageButtonMinus);
        this.imageButtonPlus = findViewById(R.id.imageButtonPlus);
        this.imageButtonDone = findViewById(R.id.imageButtonDone);

        this.recyclerScanActions = findViewById(R.id.recyclerScanActions);

        this.articleContainer = findViewById(R.id.articleContainer);
        this.articleInfoContainer = findViewById(R.id.articleInfoContainer);
    }

    @Override
    public void mSetToolbar(String pvScreenTitleStr) {

        if (cUser.currentUser.currentAuthorisation.getCustomAuthorisation() != null) {
            this.toolbarImage.setImageBitmap(cUser.currentUser.currentAuthorisation.customImageBmp());
            this.toolbarTitle.setText( cUser.currentUser.currentAuthorisation.getCustomAuthorisation().getDescriptionStr());
        }
        else {
            this.toolbarImage.setImageResource(R.drawable.ic_menu_move_mi);
            this.toolbarTitle.setText(pvScreenTitleStr);
        }

        this.toolbarSubtext.setText(cMoveorder.currentMoveOrder.getOrderNumberStr());
        this.toolbarSubtext2.setText(cMoveorder.currentMoveOrder.getBinCodeStr());
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

        this.counterPlusHelperInt = 0;
        this.counterMinusHelperInt = 0;

        this.mSetArticleInfo();
        this.mSetBinInfo();
        this.mSetQuantityInfo();

        this.mEnablePlusMinusAndBarcodeSelectViews();
        this.mShowArticleImage();
        this.mShowOrHideGenericExtraFields();
        this.mShowBarcodeInfo();
        this.mShowLines();

        this.mHideArticleInfo();
        this.mSetScanlineConstraints();

        this.imageButtonNoInputPropertys.setVisibility(View.GONE);

    }

    @Override
    public void mInitScreen() {
        cBarcodeScan.pRegisterBarcodeReceiver(this.getClass().getSimpleName());

        //Raise quantity with scanned barcodeStr, if we started this activity with a scan
        if (cMoveorder.currentMoveOrder.currentMoveorderBarcode != null) {
            this.pHandleScan(cBarcodeScan.pFakeScan(cMoveorder.currentMoveOrder.currentMoveorderBarcode.getBarcodeStr()));
            cMoveorder.currentMoveOrder.currentMoveorderBarcode =null;
        }

        if (cMoveorder.currentMoveOrder.currentBranchBin != null) {
            this.pHandleScan(cBarcodeScan.pFakeScan(cMoveorder.currentMoveOrder.currentBranchBin.getBinCodeStr()));
        }

    }

    @Override
    public void mSetListeners() {

        this.mSetArticleImageListener();
        this.mSetImageButtonBarcodeListener();

        if (cSetting.MOVE_AMOUNT_MANUAL()) {
            this.mSetNumberListener();
        }

        this.mSetPlusListener();
        this.mSetMinusListener();
        this.mSetDoneListener();

        this.mSetRecyclerTouchListener();

    }

    // End Region iICSDefaultActivity Methods

    //Region Public Methods

    public void pHandleScan(cBarcodeScan pvBarcodeScan) {

        cUserInterface.pCheckAndCloseOpenDialogs();

        cResult hulpRst;

        boolean binCheckedBln = false;

        //This barcode matches multiple lay-outs so this can be a BIN or an article
        if (Objects.requireNonNull(cBarcodeLayout.pGetBarcodeLayoutByBarcodeObl(pvBarcodeScan.getBarcodeOriginalStr())).size() > 1) {

            //First check if this is a BIN
            cBranchBin branchBin =  cUser.currentUser.currentBranch.pGetBinByCode(cRegex.pStripRegexPrefixStr(pvBarcodeScan.getBarcodeOriginalStr()));

            if (branchBin != null) {

                hulpRst = this.mHandleBINScanRst(branchBin);
                if (! hulpRst.resultBln) {
                    cUserInterface.pShowSnackbarMessage(moveorderlinesPlaceGeneratedContainer,hulpRst.messagesStr(),null,true);
                    return;
                }
            }

            binCheckedBln = true;

        }

        //Check if we have scanned a BIN and check if there are not handled linesInt for this BIN
        if (!binCheckedBln && cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(), cBarcodeLayout.barcodeLayoutEnu.BIN)) {
            //First check if this is a BIN
            cBranchBin branchBin =  cUser.currentUser.currentBranch.pGetBinByCode(cRegex.pStripRegexPrefixStr(pvBarcodeScan.getBarcodeOriginalStr()));
            hulpRst = this.mHandleBINScanRst(branchBin);
            if (! hulpRst.resultBln) {
                cUserInterface.pShowSnackbarMessage(moveorderlinesPlaceGeneratedContainer,hulpRst.messagesStr(),null,true);
                return;
            }
            return;
        }

        //Check if we have scanned an ARTICLE and check if there are not handled linesInt for this BIN
        if (cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(), cBarcodeLayout.barcodeLayoutEnu.ARTICLE)) {

            hulpRst = this.mHandleArticleScan(pvBarcodeScan);
            if (! hulpRst.resultBln) {
                cUserInterface.pDoExplodingScreen(hulpRst.messagesStr(),"",true,true);
                return;
            }
            return;
        }

        cUserInterface.pDoExplodingScreen(cAppExtension.activity.getString(R.string.message_unknown_barcode), pvBarcodeScan.getBarcodeOriginalStr(), true,true);

    }

    public   void pAcceptMove() {

        if (this.quantityScannedDbl == 0 ) {
            this.mResetCurrents();
            this.mGoBackToLinesActivity();
            return;
        }

        //We are done
        this.mSendLine(null);

    }

    public  void pCancelMove() {
        this.mResetCurrents();
        this.mGoBackToLinesActivity();
    }

    //End Region Public Methods

    //Region Private Methods

    //Views

    private void mShowLines() {
        this.mFillRecycler(cMoveorder.currentMoveOrder.placeLineForBinReversedObl(cMoveorder.currentMoveOrder.currentBranchBin.getBinCodeStr()));
    }

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

                if (cMoveorderLine.currentMoveOrderLine.barcodesObl == null || cMoveorderLine.currentMoveOrderLine.barcodesObl.size() == 0) {
                    return;
                }

                mEnablePlusMinusAndBarcodeSelectViews();

                //If we only have one barcodeStr, then automatticaly select that barcodeStr
                if (cMoveorderLine.currentMoveOrderLine.barcodesObl.size() == 1) {
                    pHandleScan(cBarcodeScan.pFakeScan(cMoveorderLine.currentMoveOrderLine.barcodesObl.get(0).getBarcodeStr()));
                    return;
                }

                mShowBarcodeSelectFragment();

            }
        });
    }

    private void mSetRecyclerTouchListener(){

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new cMoveorderLineRecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(this.recyclerScanActions);
    }

    private  void mShowOrHideGenericExtraFields() {

    }

    private  void mShowBarcodeInfo() {

        if (cMoveorderLine .currentMoveOrderLine == null) {
            this.articleBarcodeText.setText(cAppExtension.activity.getString(R.string.novalueyet));
            return;
        }

        if (cMoveorder.currentMoveOrder.currentMoveorderBarcode == null) {
            if (cMoveorderLine .currentMoveOrderLine.barcodesObl.size() == 1) {
                cMoveorder.currentMoveOrder.currentMoveorderBarcode = cMoveorderLine.currentMoveOrderLine.barcodesObl.get(0);
            }
        }

        if (cMoveorder.currentMoveOrder.currentMoveorderBarcode!= null) {
            this.articleBarcodeText.setText(cMoveorder.currentMoveOrder.currentMoveorderBarcode.getBarcodeAndQuantityStr());
        } else {
            this.articleBarcodeText.setText(cAppExtension.context.getString(R.string.mutiple_barcodes_posible));
        }
    }

    private  void mShowArticleImage() {


        //If pick with picture is false, then hide image view
        if (!cMoveorder.currentMoveOrder.isMoveWithPictureBln()) {
            this.articleThumbImageView.setVisibility(View.GONE);
            return;
        }
        this.articleThumbImageView.setVisibility(View.VISIBLE);
        this.articleThumbImageView.setImageDrawable(ContextCompat.getDrawable(cAppExtension.context, R.drawable.ic_no_image_lightgrey_24dp));


        if (cMoveorderLine.currentMoveOrderLine == null){
            this.articleThumbImageView.setImageDrawable(ContextCompat.getDrawable(cAppExtension.context, R.drawable.ic_no_image_lightgrey_24dp));
            return;
        }

        //If picture is not in cache (via webservice) then show no image
        if (!cMoveorderLine.currentMoveOrderLine.pGetArticleImageBln()) {
            cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.could_not_get_article_image), null);
            this.articleThumbImageView.setImageDrawable(ContextCompat.getDrawable(cAppExtension.context, R.drawable.ic_no_image_lightgrey_24dp));
            return;
        }

        //If picture is in cache but can't be converted, then show no image
        if (cMoveorderLine.currentMoveOrderLine.articleImage == null || cMoveorderLine.currentMoveOrderLine.articleImage.imageBitmap() == null) {
            cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.could_not_get_article_image), null);
            this.articleThumbImageView.setImageDrawable(ContextCompat.getDrawable(cAppExtension.context, R.drawable.ic_no_image_lightgrey_24dp));
            return;
        }

        //Show the image
        this.articleThumbImageView.setImageBitmap(cMoveorderLine.currentMoveOrderLine.articleImage.imageBitmap());

        //Open the image
        if ((cMoveorder.currentMoveOrder.isMoveWithPictureAutoOpenBln())) {
            this.mShowFullArticleFragment();
        }

    }

    private  void mEnablePlusMinusAndBarcodeSelectViews() {

        if (cMoveorderLine.currentMoveOrderLine == null) {
            this.imageButtonMinus.setVisibility(View.INVISIBLE);
            this.imageButtonPlus.setVisibility(View.INVISIBLE);
            this.imageButtonBarcode.setVisibility(View.GONE);
            return;
        }

        if (!cSetting.MOVE_AMOUNT_MANUAL()) {
            this.imageButtonMinus.setVisibility(View.INVISIBLE);
            this.imageButtonPlus.setVisibility(View.INVISIBLE);
            this.imageButtonBarcode.setVisibility(View.GONE);
        }
        else {
            this.imageButtonMinus.setVisibility(View.VISIBLE);
            this.imageButtonPlus.setVisibility(View.VISIBLE);
            this.imageButtonBarcode.setVisibility(View.VISIBLE);
        }
    }

    private void mHideArticleInfo(){

        this.articleInfoContainer.setVisibility(View.GONE);
        ConstraintLayout.LayoutParams newCardViewLayoutParams = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        newCardViewLayoutParams.setMargins(15,15,15,15);
        this.articleContainer.setLayoutParams(newCardViewLayoutParams);

        ConstraintSet constraintSetSpace = new ConstraintSet();
        constraintSetSpace.clone(this.moveorderlinesPlaceGeneratedContainer);
        constraintSetSpace.connect(this.articleContainer.getId(), ConstraintSet.TOP, toolbar.getId(), ConstraintSet.BOTTOM);
        constraintSetSpace.applyTo(this.moveorderlinesPlaceGeneratedContainer);

    }

    //Scans and manual input

    private cResult mHandleArticleScan(cBarcodeScan pvBarcodeScan) {

        cResult result = new cResult();
        result.resultBln = true;


        //Check if this is a barcodeStr we already know
        cMoveorder.currentMoveOrder.currentMoveorderBarcode = cMoveorder.currentMoveOrder.pGetOrderBarcode(pvBarcodeScan);

       // This is a new barcode, so handle the new barcode
        if ( cMoveorder.currentMoveOrder.currentMoveorderBarcode == null) {
            this.mHandleUnknownBarcodeScan(pvBarcodeScan);
            return result;
        }

       if ( cMoveorder.currentMoveOrder.currentMoveorderBarcode.getIsUniqueBarcodeBln() && cMoveorder.currentMoveOrder.currentMoveorderBarcode.pPlacedItemsAvailableBln()) {
           result.pAddErrorMessage(cAppExtension.activity.getString(R.string.message_barcode_already_scanned_and_unique));
           result.resultBln = false;
           cMoveorder.currentMoveOrder.currentMoveorderBarcode = null;
       }
       else
       {
           this.mHandleUnknownBarcodeScan(pvBarcodeScan);
       }
        return result;

    }

    private  cResult mHandleBINScanRst(cBranchBin pvBranchBin){

        cResult result = new cResult();
        result.resultBln = true;

        //If we already handled this BIN scan in previous activity, we are done
        if (pvBranchBin.getBinCodeStr().equalsIgnoreCase(cMoveorder.currentMoveOrder.currentBranchBin.getBinCodeStr())) {
            return result;
        }

        if (cMoveorderLine.currentMoveOrderLine == null) {
            cMoveorder.currentMoveOrder.currentBranchBin = pvBranchBin;
            this.mFieldsInitialize();
            return result;
        }

        this.mSendLine(pvBranchBin);
        return  result;

    }

    private void mNumberClicked() {

        if (cMoveorder.currentMoveOrder.currentMoveorderBarcode == null) {
            cUserInterface.pDoNope(quantityText, false, false);
            cUserInterface.pShowSnackbarMessage(moveorderlinesPlaceGeneratedContainer, getString(R.string.choose_barcode_first), null, false);
            return;
        }

        if (cMoveorder.currentMoveOrder.currentMoveorderBarcode.getQuantityHandledDbl() > 1) {
            cUserInterface.pDoNope(quantityText, true, true);
            cUserInterface.pShowSnackbarMessage(moveorderlinesPlaceGeneratedContainer, getString(R.string.manual_input_only_barcodenumber_bigger1), null, false);
            return;
        }

        this.mShowNumberFragment();
    }

    private void mHandleQuantityChosen(double pvQuantityDbl) {
        this.mTryToChangeQuantity(pvQuantityDbl != 0, true,pvQuantityDbl);
    }

    private  void mTryToChangeQuantity(Boolean pvIsPositiveBln, Boolean pvAmountFixedBln, double pvAmountDbl) {

      double newQuantityDbl;

        if ( this.scannedBarcodesObl == null) {
            this.scannedBarcodesObl = new ArrayList<>();
        }

        if (pvIsPositiveBln) {

            //Determine the new amount
                newQuantityDbl = pvAmountDbl;
                if (pvAmountFixedBln) {


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
                return;


            } else {
                newQuantityDbl = this.quantityScannedDbl + pvAmountDbl;
            }

            //Set the new quantityDbl and show in Activity
            this.quantityScannedDbl = newQuantityDbl;
            this.quantityText.setText(cText.pDoubleToStringStr(this.quantityScannedDbl));

            //Add a barcodeStr to the scanned barcodeStr list, so you can use it later when line is determined
            this.scannedBarcodesObl.add(cMoveorder.currentMoveOrder.currentMoveorderBarcode);
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

    private void mBarcodeSelected(cMoveorderBarcode pvBarcode) {

        cUserInterface.pCheckAndCloseOpenDialogs();

        cMoveorder.currentMoveOrder.currentMoveorderBarcode = pvBarcode;
        this.mShowBarcodeInfo();

        this.mTryToChangeQuantity(true, false, cMoveorder.currentMoveOrder.currentMoveorderBarcode.getQuantityPerUnitOfMeasureDbl());



        if (cMoveorder.currentMoveOrder.currentMoveorderBarcode.getIsUniqueBarcodeBln()) {
            this.mSendLine(null);
        }

    }

    private void mHandleUnknownBarcodeScan(cBarcodeScan pvBarcodeScan) {


        //We can add a line, but we don't check with the ERP, so add line and open it
        if (!cSetting.MOVE_BARCODE_CHECK()) {
            this.mAddUnkownArticle(pvBarcodeScan);
            cUserInterface.pHideGettingData();
            return;
        }

        //We can add a line, and we need to check with the ERP, so check, add and open it
        this.mAddERPArticle(pvBarcodeScan);
        cUserInterface.pHideGettingData();


        //End Region Private Methods

    }

    private void mAddUnkownArticle(cBarcodeScan pvBarcodeScan){

        cResult hulpResult = new cResult();
        hulpResult.resultBln = false;

        boolean hulpBln = true;

        if (!cMoveorder.currentMoveOrder.pAddUnkownBarcodeAndItemVariantBln(pvBarcodeScan)){
            hulpBln = false ;
        }

        //Add the barcodeStr via the webservice
        if (!hulpBln) {
            cUserInterface.pDoExplodingScreen(cAppExtension.activity.getString(R.string.message_adding_unkown_article_failed),"",true,true);
            return;
        }

        this.mFieldsInitialize();
    }

    private void mAddERPArticle(cBarcodeScan pvBarcodeScan){

        //Add the barcodeStr via the webservice
        if (!cMoveorder.currentMoveOrder.pAddERPBarcodeBln(pvBarcodeScan)) {
            cUserInterface.pDoExplodingScreen(cAppExtension.activity.getString(R.string.message_adding_erp_article_failed, pvBarcodeScan.barcodeOriginalStr),"",true,true);
            ReceiveLinesActivity.busyBln = false;
            return;
        }

        this.mBarcodeSelected(cMoveorder.currentMoveOrder.currentMoveorderBarcode);

        this.mFieldsInitialize();
    }

    // Lines, Barcodes

    private void mSendLine(cBranchBin pvBranchBin) {

        String scannedBarcodeStr;

        //If internet is not connected
        if (!cConnection.isInternetConnectedBln()) {
            //could not send line, let user know but answer succes so user can go to next line
            cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.couldnt_send_line_no_connection), null);
            return;
        }

        List<cMoveorderBarcode> sortedBarcodeList = this.mSortBarcodeList(this.scannedBarcodesObl);

        if(!cMoveorder.currentMoveOrder.pMovePlaceItemHandledBln(sortedBarcodeList)) {
            cUserInterface.pDoExplodingScreen(cAppExtension.context.getString(R.string.couldnt_send_line),"", true,true);
            return;
        }

        if (cMoveorder.currentMoveOrder.currentMoveorderBarcode.getIsUniqueBarcodeBln() || pvBranchBin != null) {
            scannedBarcodeStr = cMoveorder.currentMoveOrder.currentMoveorderBarcode.getBarcodeStr();
            this.mResetCurrentsForUniqueBarcode();

            if (pvBranchBin != null) {
                cMoveorder.currentMoveOrder.currentBranchBin = pvBranchBin;
            }

            this.mFieldsInitialize();
            cUserInterface.pShowSnackbarMessage(this.quantityText, scannedBarcodeStr +  " send", R.raw.headsupsound, true);
            return;
        }

        this.mResetCurrents();
        this.mGoBackToLinesActivity();

    }

     //ScanActions
     private void mFillRecycler(List<cMoveorderLine> pvDataObl) {

        if (pvDataObl == null | pvDataObl.size() == 0) {
            this.imageButtonDone.setVisibility(View.INVISIBLE);
        }
        else
        {
            this.imageButtonDone.setVisibility(View.VISIBLE);
            this.imageButtonDone.setImageResource(R.drawable.ic_doublecheck_black_24dp);
        }
         this.getMoveorderLineAdapter().pFillData(pvDataObl);
         this.recyclerScanActions.setHasFixedSize(false);
         this.recyclerScanActions.setAdapter(this.getMoveorderLineAdapter());
         this.recyclerScanActions.setLayoutManager(new LinearLayoutManager(cAppExtension.context));
     }

    //Listeners
    private void mSetDoneListener() {
        this.imageButtonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               pAcceptMove();
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
                if (cMoveorder.currentMoveOrder.currentMoveorderBarcode == null) {
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
                if (cMoveorder.currentMoveOrder.currentMoveorderBarcode == null) {
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

        Intent intent = new Intent(cAppExtension.context, MoveorderLinesPlaceGeneratedActivity.class);
        cAppExtension.activity.startActivity(intent);
        cAppExtension.activity.finish();
    }

    private void mResetCurrents() {

        cMoveorderLine.currentMoveOrderLine = null;
        cMoveorder.currentMoveOrder.currentMoveorderBarcode = null;
        cMoveorder.currentMoveOrder.moveorderBarcodeToHandle = null;
        cMoveorder.currentMoveOrder.currentBranchBin = null;
        this.scannedBarcodesObl = null;
        this.quantityScannedDbl = 0.0;
    }

    private void mResetCurrentsForUniqueBarcode() {
        cMoveorderLine.currentMoveOrderLine = null;
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
        bundle.putInt(cPublicDefinitions.NUMBERINTENT_CURRENTQUANTITY, (int) cMoveorderLine.currentMoveOrderLine.getQuantityHandledDbl());
        bundle.putDouble(cPublicDefinitions.NUMBERINTENT_MAXQUANTITY,99999);

        NumberpickerFragment numberpickerFragment = new NumberpickerFragment();
        numberpickerFragment.setArguments(bundle);

        numberpickerFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.NUMBERFRAGMENT_TAG);
    }

    private void mSetArticleInfo(){

        if (cMoveorderLine.currentMoveOrderLine == null) {
            this.articleContainer.setVisibility(View.GONE);
            return;
        }

        this.articleContainer.setVisibility(View.VISIBLE);
        this.articleDescriptionText.setText(cMoveorderLine.currentMoveOrderLine.getDescriptionStr());
        this.articleDescription2Text.setText(cMoveorderLine.currentMoveOrderLine.getDescription2Str());
        this.articleItemText.setText(cMoveorderLine.currentMoveOrderLine.getItemNoAndVariantCodeStr());

        if (cMoveorderLine.currentMoveOrderLine.getItemNoAndVariantCodeStr().equalsIgnoreCase(cMoveorderLine.currentMoveOrderLine.getDescriptionStr())){
            this.articleDescriptionText.setVisibility(View.GONE);
            this.articleDescription2Text.setVisibility(View.GONE);
        }
    }

    private void mSetQuantityInfo(){

        if (this.quantityScannedDbl == 0) {
            this.quantityControlsContainer.setVisibility(View.GONE);
            return;
        }

        this.quantityControlsContainer.setVisibility(View.VISIBLE);
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

    private  List<cMoveorderBarcode> mSortBarcodeList(List<cMoveorderBarcode> pvUnsortedBarcodeObl) {

        List<cMoveorderBarcode> resultList = new ArrayList<>();

        boolean barcodeFoundBln = false;

        for (cMoveorderBarcode  moveorderBarcode : pvUnsortedBarcodeObl) {
            for (cMoveorderBarcode resultBarcode : resultList) {
                if (resultBarcode.getBarcodeStr().equalsIgnoreCase(moveorderBarcode.getBarcodeStr())) {
                    resultBarcode.quantityHandledDbl +=  moveorderBarcode.getQuantityPerUnitOfMeasureDbl();
                    barcodeFoundBln = true;
                }
            }
            if (barcodeFoundBln) {
                barcodeFoundBln = false;
            }
            else {
                //new barcode, so add
                moveorderBarcode.quantityHandledDbl = moveorderBarcode.getQuantityPerUnitOfMeasureDbl();
                resultList.add(moveorderBarcode);
            }
        }
        return resultList;
    }

    //Region Number Broadcaster

    private final Runnable mMinusAction = new Runnable() {
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

    private final Runnable mPlusAction = new Runnable() {
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
        this.counterMinusHelperInt += 1;
    }

    private void mDoDelayedPlus(Runnable pvRunnable, long pvMilliSecsLng) {
        this.plusHandler.postDelayed(pvRunnable, pvMilliSecsLng);
        this.counterPlusHelperInt += 1;
    }

    private void mRemoveAdapterFromFragment(){

        //remove the item from recyclerview
        cResult resultRst = cMoveorderLine.currentMoveOrderLine.pResetRst();
        if (! resultRst.resultBln) {
            cUserInterface.pDoExplodingScreen(cAppExtension.activity.getString(R.string.message_reset_line_via_webservice_failed),"",true,true);
            return;
        }

        cUserInterface.pShowSnackbarMessage(this.imageButtonDone,cAppExtension.activity.getString(R.string.message_line_reset_succesfull), R.raw.headsupsound, true);

        cMoveorder.currentMoveOrder.currentMoveorderBarcode = null;
        cMoveorder.currentMoveOrder.moveorderBarcodeToHandle = null;
        cMoveorderLine.currentMoveOrderLine = null;

        //Renew data, so only current lines are shown
        this.mFieldsInitialize();
    }

    private void mSetScanlineConstraints(){

        if (cMoveorderLine.currentMoveOrderLine == null){
            ConstraintSet constraintSetSpace = new ConstraintSet();
            constraintSetSpace.clone(moveorderlinesPlaceGeneratedContainer);
            constraintSetSpace.connect(recyclerScanActions.getId(), ConstraintSet.TOP, binContainer.getId(), ConstraintSet.BOTTOM);
            constraintSetSpace.applyTo(moveorderlinesPlaceGeneratedContainer);
        }
    }

    //End Region Number Broadcaster

    //End Regin Private Methods

}

