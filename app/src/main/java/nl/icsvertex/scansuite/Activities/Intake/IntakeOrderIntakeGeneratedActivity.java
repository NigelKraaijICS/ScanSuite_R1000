package nl.icsvertex.scansuite.Activities.Intake;

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
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

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
import SSU_WHS.Intake.IntakeorderBarcodes.cIntakeorderBarcode;
import SSU_WHS.Intake.IntakeorderMATLineSummary.cIntakeorderMATSummaryLine;
import SSU_WHS.Intake.IntakeorderMATLines.cIntakeorderMATLine;
import SSU_WHS.Intake.IntakeorderMATLines.cIntakeorderMATLineAdapter;
import SSU_WHS.Intake.IntakeorderMATLines.cIntakeorderMATLineRecyclerItemTouchHelper;
import SSU_WHS.Intake.Intakeorders.cIntakeorder;
import nl.icsvertex.scansuite.Activities.Receive.ReceiveLinesActivity;
import nl.icsvertex.scansuite.Fragments.Dialogs.AcceptRejectFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.ArticleFullViewFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.BarcodeFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.NumberpickerFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.PrintBinLabelFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.ScanBinFragment;
import nl.icsvertex.scansuite.R;

import static ICS.Utils.cText.pDoubleToStringStr;

public class IntakeOrderIntakeGeneratedActivity extends AppCompatActivity implements iICSDefaultActivity, cIntakeorderMATLineRecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    //Region Private Properties
    private  int counterMinusHelperInt;
    private  int counterPlusHelperInt;
    private  Handler minusHandler;
    private  Handler plusHandler;
    private  AppCompatImageButton imageButtonMinus;
    private  AppCompatImageButton imageButtonPlus;
    private  AppCompatImageButton imageButtonDone;

    private  ConstraintLayout intakeorderIntakeGeneratedContainer;

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
    private DrawerLayout menuActionsDrawer;
    private NavigationView actionMenuNavigation;

    private  Double quantityScannedDbl = 0.0;
    private  List<cIntakeorderBarcode> scannedBarcodesObl;

    private  RecyclerView recyclerScanActions;

    private cIntakeorderMATLineAdapter intakeorderMATLineAdapter;
    private cIntakeorderMATLineAdapter getIntakeorderMATLineAdapter(){
        if (this.intakeorderMATLineAdapter == null) {
            this.intakeorderMATLineAdapter = new cIntakeorderMATLineAdapter();
        }
        return  this.intakeorderMATLineAdapter;
    }

    public  static String binScanToHandleStr;

    //End Region Private Properties

    //Region Default Methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intakeorder_intake_generated);
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
        if (cIntakeorder.currentIntakeOrder.currentBin != null)  {
            item_enter_bin.setVisible(false);
            return true;
        }

        if (cSetting.GENERIC_PRINT_BINLABEL()){
            MenuItem item_print_bin = pvMenu.findItem(R.id.item_print_bin);
            item_print_bin.setVisible(true);
        }

        if (cSetting.GENERIC_PRINT_ITEMLABEL()){
            MenuItem item_print_item = pvMenu.findItem(R.id.item_print_item);
            item_print_item.setVisible(true);
            if (cIntakeorderBarcode.currentIntakeOrderBarcode == null && cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine != null){
                item_print_item.setVisible(false);
            }
        }
        return super.onPrepareOptionsMenu(pvMenu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem pvMenuItem) {
        DialogFragment selectedFragment = null;
        switch (pvMenuItem.getItemId()) {

            case android.R.id.home:

                if (this.quantityScannedDbl == 0 ) {
                    this.mResetCurrents();
                    this.mGoBackToLinesActivity();
                    return  true;
                }

                this.mShowAcceptFragment();
                return true;

            case R.id.item_enter_bin:
                selectedFragment = new ScanBinFragment();
                break;

            case R.id.item_print_bin:
                selectedFragment = new PrintBinLabelFragment();
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

        if (!(pvViewHolder instanceof  cIntakeorderMATLineAdapter.IntakeorderMATLineViewHolder)) {
            return;
        }

        cIntakeorderMATLine.currentIntakeorderMATLine = cIntakeorder.currentIntakeOrder.linesHandledMATForBinObl(cIntakeorder.currentIntakeOrder.currentBin.getBinCodeStr()).get(pvPositionInt);

        //Remove the enviroment
        this.mRemoveAdapterFromFragment();

    }

    //End Region Default Methods

    //Region iICSDefaultActivity Methods

    @Override
    public void mActivityInitialize() {

        this.mSetAppExtensions();

        this.mFindViews();

        this.mSetToolbar(getResources().getString(R.string.screentitle_intake));

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

        this.intakeorderIntakeGeneratedContainer = findViewById(R.id.intakeorderIntakeGeneratedContainer);

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
        this.imageButtonNoInputPropertys = findViewById(R.id.imageButtonNoInputPropertys);
        this.imageButtonBarcode = findViewById(R.id.imageButtonBarcode);
        this.imageButtonMinus = findViewById(R.id.imageButtonMinus);
        this.imageButtonPlus = findViewById(R.id.imageButtonPlus);
        this.imageButtonDone = findViewById(R.id.imageButtonDone);

        this.recyclerScanActions = findViewById(R.id.recyclerScanActions);
        this.menuActionsDrawer = findViewById(R.id.menuActionsDrawer);
        this.actionMenuNavigation = findViewById(R.id.actionMenuNavigation);

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
            this.toolbarImage.setImageResource(R.drawable.ic_menu_intake);
            this.toolbarTitle.setText(pvScreenTitleStr);
        }

        this.toolbarSubtext.setText(cIntakeorder.currentIntakeOrder.getOrderNumberStr());
        this.toolbarSubtext2.setText(cIntakeorder.currentIntakeOrder.getBinCodeStr());
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
        this.mShowBarcodeInfo();
        this.mShowLines();

        this.mHideArticleInfo();
        this.mSetScanlineConstraints();
        this.mShowDoneButton();

        this.imageButtonNoInputPropertys.setVisibility(View.GONE);

    }

    @Override
    public void mInitScreen() {
        cBarcodeScan.pRegisterBarcodeReceiver(this.getClass().getSimpleName());

        //Raise quantity with scanned barcodeStr, if we started this activity with a scan
        if (cIntakeorder.currentIntakeOrder.intakeorderBarcodeScanned != null) {
            this.pHandleScan(cBarcodeScan.pFakeScan(cIntakeorder.currentIntakeOrder.intakeorderBarcodeScanned.getBarcodeStr()), false);
            cIntakeorder.currentIntakeOrder.intakeorderBarcodeScanned =null;
        }

        if (IntakeOrderIntakeGeneratedActivity.binScanToHandleStr != null && !IntakeOrderIntakeGeneratedActivity.binScanToHandleStr.isEmpty()) {
            this.pHandleScan(cBarcodeScan.pFakeScan(IntakeOrderIntakeGeneratedActivity.binScanToHandleStr), true);
            IntakeOrderIntakeGeneratedActivity.binScanToHandleStr = null;
        }

    }

    @Override
    public void mSetListeners() {

        this.mSetArticleImageListener();
        this.mSetImageButtonBarcodeListener();

        if (cSetting.RECEIVE_AMOUNT_MANUAL_MA()) {
            this.mSetNumberListener();
        }

        this.mSetPlusListener();
        this.mSetMinusListener();
        this.mSetDoneListener();

        this.mSetRecyclerTouchListener();

    }

    // End Region iICSDefaultActivity Methods

    //Region Public Methods

    public void pHandleScan(cBarcodeScan pvBarcodeScan, boolean pvIsBinBln) {

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
                    cUserInterface.pShowSnackbarMessage(intakeorderIntakeGeneratedContainer,hulpRst.messagesStr(),null,true);
                    return;
                }
            }

            binCheckedBln = true;

        }

        //Check if we have scanned a BIN and check if there are not handled linesInt for this BIN
        if (pvIsBinBln || !binCheckedBln && cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(), cBarcodeLayout.barcodeLayoutEnu.BIN)) {
            //First check if this is a BIN
            cBranchBin branchBin =  cUser.currentUser.currentBranch.pGetBinByCode(cRegex.pStripRegexPrefixStr(pvBarcodeScan.getBarcodeOriginalStr()));
            hulpRst = this.mHandleBINScanRst(branchBin);
            if (! hulpRst.resultBln) {
                cUserInterface.pShowSnackbarMessage(intakeorderIntakeGeneratedContainer,hulpRst.messagesStr(),null,true);
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

            this.mFieldsInitialize();
            return;
        }

        cUserInterface.pDoExplodingScreen(cAppExtension.activity.getString(R.string.message_unknown_barcode), pvBarcodeScan.getBarcodeOriginalStr(), true,true);

    }

    public  void pAcceptStore() {

        if (this.quantityScannedDbl == 0 ) {
            this.mResetCurrents();
            this.mGoBackToLinesActivity();
            return;
        }

        //We are done
        this.mSendLine(null);

    }

    public  void pCancelStore() {
        this.mResetCurrents();
        this.mGoBackToLinesActivity();
    }

    //End Region Public Methods

    //Region Private Methods

    //Views

    private void mShowLines() {
        this.mFillRecycler(cIntakeorder.currentIntakeOrder.linesHandledMATForBinObl(cIntakeorder.currentIntakeOrder.currentBin.getBinCodeStr()));
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

                if (cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.barcodesObl() == null || cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.barcodesObl().size() == 0) {
                    return;
                }

                mEnablePlusMinusAndBarcodeSelectViews();

                //If we only have one barcodeStr, then automatticaly select that barcodeStr
                if (cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.barcodesObl().size() == 1) {
                    pHandleScan(cBarcodeScan.pFakeScan(cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.barcodesObl().get(0).getBarcodeStr()), false);
                    return;
                }

                mShowBarcodeSelectFragment();

            }
        });
    }

    private void mSetRecyclerTouchListener(){

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new cIntakeorderMATLineRecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(this.recyclerScanActions);
    }

    private  void mShowBarcodeInfo() {

        if (cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine == null) {
            this.articleBarcodeText.setText(cAppExtension.activity.getString(R.string.novalueyet));
            return;
        }

        if (cIntakeorderBarcode.currentIntakeOrderBarcode == null) {
            if (cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.barcodesObl().size() == 1) {
                cIntakeorderBarcode.currentIntakeOrderBarcode = cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.barcodesObl().get(0);
            }
        }

        if (cIntakeorderBarcode.currentIntakeOrderBarcode!= null) {
            this.articleBarcodeText.setText(cIntakeorderBarcode.currentIntakeOrderBarcode.getBarcodeAndQuantityStr());
        } else {
            this.articleBarcodeText.setText(cAppExtension.context.getString(R.string.mutiple_barcodes_posible));
        }
    }

    private void mShowDoneButton(){

        if (cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine == null) {
            this.imageButtonDone.setVisibility(View.GONE);
            return;
        }

        this.imageButtonDone.setVisibility(View.VISIBLE);
        this.imageButtonDone.setImageResource(R.drawable.ic_check_black_24dp);
    }

    private  void mShowArticleImage() {

        //If pick with picture is false, then hide image view
        if (!cIntakeorder.currentIntakeOrder.isReceiveWithPictureBln()) {
            this.articleThumbImageView.setVisibility(View.GONE);
            return;
        }

        this.articleThumbImageView.setVisibility(View.VISIBLE);
        this.articleThumbImageView.setImageDrawable(ContextCompat.getDrawable(cAppExtension.context, R.drawable.ic_no_image_lightgrey_24dp));


        if (cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine == null){
            this.articleThumbImageView.setImageDrawable(ContextCompat.getDrawable(cAppExtension.context, R.drawable.ic_no_image_lightgrey_24dp));
            return;
        }

        //If picture is not in cache (via webservice) then show no image
        if (!cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.pGetArticleImageBln()) {
            cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.could_not_get_article_image), null);
            this.articleThumbImageView.setImageDrawable(ContextCompat.getDrawable(cAppExtension.context, R.drawable.ic_no_image_lightgrey_24dp));
            return;
        }

        //If picture is in cache but can't be converted, then show no image
        if (cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.articleImage == null || cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.articleImage.imageBitmap() == null) {
            cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.could_not_get_article_image), null);
            this.articleThumbImageView.setImageDrawable(ContextCompat.getDrawable(cAppExtension.context, R.drawable.ic_no_image_lightgrey_24dp));
            return;
        }

        //Show the image
        this.articleThumbImageView.setImageBitmap(cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.articleImage.imageBitmap());

        //Open the image
        if ((cIntakeorder.currentIntakeOrder.isReceiveWithPictureAutoOpenBln())) {
            this.mShowFullArticleFragment();
        }

    }

    private  void mEnablePlusMinusAndBarcodeSelectViews() {

        if (cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine == null) {
            this.imageButtonMinus.setVisibility(View.INVISIBLE);
            this.imageButtonPlus.setVisibility(View.INVISIBLE);
            this.imageButtonBarcode.setVisibility(View.GONE);
            return;
        }

        if (!cSetting.RECEIVE_AMOUNT_MANUAL_MA()) {
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
        constraintSetSpace.clone(this.intakeorderIntakeGeneratedContainer);
        constraintSetSpace.connect(this.articleContainer.getId(), ConstraintSet.TOP, binContainer.getId(), ConstraintSet.BOTTOM);
        constraintSetSpace.applyTo(this.intakeorderIntakeGeneratedContainer);

    }

    //Scans and manual input

    private cResult mHandleArticleScan(cBarcodeScan pvBarcodeScan) {

        cResult result = new cResult();
        result.resultBln = true;


        if (cIntakeorderBarcode.currentIntakeOrderBarcode != null && pvBarcodeScan.getBarcodeOriginalStr().equalsIgnoreCase(cIntakeorderBarcode.currentIntakeOrderBarcode.getBarcodeStr())) {
            this.mBarcodeSelected(cIntakeorderBarcode.currentIntakeOrderBarcode);
            return result;
        }

        //Check if this is a barcodeStr we already know
        cIntakeorderBarcode.currentIntakeOrderBarcode = cIntakeorder.currentIntakeOrder.pGetOrderBarcode(pvBarcodeScan);

       // This is a new barcode, so handle the new barcode
        if (cIntakeorderBarcode.currentIntakeOrderBarcode == null) {
            this.mHandleUnknownBarcodeScan(pvBarcodeScan);
            return result;
        }

       if (cIntakeorderBarcode.currentIntakeOrderBarcode.getIsUniqueBarcodeBln()) {
           result.pAddErrorMessage(cAppExtension.activity.getString(R.string.message_barcode_already_scanned_and_unique));
           result.resultBln = false;
           cIntakeorderBarcode.currentIntakeOrderBarcode = null;
           return result;
        }

        //Set the scanned barcodeStr, so we can raise quantity in next activity
        cIntakeorder.currentIntakeOrder.intakeorderBarcodeScanned = cIntakeorderBarcode.currentIntakeOrderBarcode;

        return result;
    }

    private  cResult mHandleBINScanRst(cBranchBin pvBranchBin){

        cResult result = new cResult();
        result.resultBln = true;

        //If we already handled this BIN scan in previous activity, we are done
        if (pvBranchBin.getBinCodeStr().equalsIgnoreCase(cIntakeorder.currentIntakeOrder.currentBin.getBinCodeStr())) {
            return result;
        }

        if (cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine == null) {
            cIntakeorder.currentIntakeOrder.currentBin = pvBranchBin;
            this.mFieldsInitialize();
            return result;
        }

        this.mSendLine(pvBranchBin);
        return  result;

    }

    private void mNumberClicked() {

        if (cIntakeorderBarcode.currentIntakeOrderBarcode == null) {
            cUserInterface.pDoNope(quantityText, false, false);
            cUserInterface.pShowSnackbarMessage(intakeorderIntakeGeneratedContainer, getString(R.string.choose_barcode_first), null, false);
            return;
        }

        if (cIntakeorderBarcode.currentIntakeOrderBarcode.getQuantityHandledDbl() > 1) {
            cUserInterface.pDoNope(quantityText, true, true);
            cUserInterface.pShowSnackbarMessage(intakeorderIntakeGeneratedContainer, getString(R.string.manual_input_only_barcodenumber_bigger1), null, false);
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
                    this.scannedBarcodesObl.add(cIntakeorderBarcode.currentIntakeOrderBarcode);
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
            this.scannedBarcodesObl.add(cIntakeorderBarcode.currentIntakeOrderBarcode);
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
                this.scannedBarcodesObl.add(cIntakeorderBarcode.currentIntakeOrderBarcode);
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

    private void mBarcodeSelected(cIntakeorderBarcode pvBarcode) {

        cUserInterface.pCheckAndCloseOpenDialogs();

        cIntakeorderBarcode.currentIntakeOrderBarcode = pvBarcode;
        this.mShowBarcodeInfo();

        this.mTryToChangeQuantity(true, false, cIntakeorderBarcode.currentIntakeOrderBarcode.getQuantityPerUnitOfMeasureDbl());



        if (cIntakeorderBarcode.currentIntakeOrderBarcode.getIsUniqueBarcodeBln()) {
            this.mSendLine(null);
        }

    }

    private void mHandleUnknownBarcodeScan(cBarcodeScan pvBarcodeScan) {


        //We can add a line, but we don't check with the ERP, so add line and open it
        if (!cSetting.RECEIVE_BARCODE_CHECK()) {
            this.mAddUnkownArticle(pvBarcodeScan);
            cUserInterface.pHideGettingData();
            return;
        }

        //We can add a line, and we need to check with the ERP, so check, add and open it
        this.mAddERPArticle(pvBarcodeScan);
        this.mFieldsInitialize();
        cUserInterface.pHideGettingData();


        //End Region Private Methods

    }

    private void mAddUnkownArticle(cBarcodeScan pvBarcodeScan){

        cResult hulpResult = new cResult();
        hulpResult.resultBln = false;

        boolean hulpBln = true;

        if (!cIntakeorder.currentIntakeOrder.pIntakeAddUnkownBarcodeAndItemVariantBln(pvBarcodeScan)){
            hulpBln = false ;
        }

        //Add the barcodeStr via the webservice
        if (!hulpBln) {
            cUserInterface.pDoExplodingScreen(cAppExtension.activity.getString(R.string.message_adding_unkown_article_failed),"",true,true);
            return;
        }

        //Set the scanned barcodeStr, so we can raise quantity in next activity
        cIntakeorder.currentIntakeOrder.intakeorderBarcodeScanned = cIntakeorderBarcode.currentIntakeOrderBarcode;

        this.mFieldsInitialize();
    }

    private void mAddERPArticle(cBarcodeScan pvBarcodeScan){

        //Add the barcodeStr via the webservice
        if (!cIntakeorder.currentIntakeOrder.pAddERPBarcodeBln(pvBarcodeScan)) {
            cUserInterface.pDoExplodingScreen(cAppExtension.activity.getString(R.string.message_adding_erp_article_failed, pvBarcodeScan.barcodeOriginalStr),"",true,true);
            ReceiveLinesActivity.busyBln = false;
            return;
        }


        this.mFieldsInitialize();
        this.mBarcodeSelected(cIntakeorderBarcode.currentIntakeOrderBarcode);
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

        List<cIntakeorderBarcode> sortedBarcodeList = this.mSortBarcodeList(this.scannedBarcodesObl);


            if(!cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.pGeneratedItemVariantHandledBln(cIntakeorder.currentIntakeOrder.currentBin.getBinCodeStr(),sortedBarcodeList)) {
                cUserInterface.pDoExplodingScreen(cAppExtension.context.getString(R.string.couldnt_send_line), "",true,true);
                return;
            }

            if (cIntakeorderBarcode.currentIntakeOrderBarcode.getIsUniqueBarcodeBln() || pvBranchBin != null) {
                scannedBarcodeStr = cIntakeorderBarcode.currentIntakeOrderBarcode.getBarcodeStr();
                this.mResetCurrentsForUniqueBarcode();

                if (pvBranchBin != null) {
                    cIntakeorder.currentIntakeOrder.currentBin = pvBranchBin;
                }

                this.mFieldsInitialize();
                cUserInterface.pShowSnackbarMessage(this.quantityText, scannedBarcodeStr +  " send", R.raw.headsupsound, true);
                return;
            }

            this.mResetCurrents();
            this.mGoBackToLinesActivity();

    }

     //ScanActions
     private void mFillRecycler(List<cIntakeorderMATLine> pvDataObl) {
         this.getIntakeorderMATLineAdapter().pFillData(pvDataObl);
         this.recyclerScanActions.setHasFixedSize(false);
         this.recyclerScanActions.setAdapter(this.getIntakeorderMATLineAdapter());
         this.recyclerScanActions.setLayoutManager(new LinearLayoutManager(cAppExtension.context));
     }

    //Listeners
    private void mSetDoneListener() {
        this.imageButtonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               pAcceptStore();
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
                if (cIntakeorderBarcode.currentIntakeOrderBarcode == null) {
                    cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.message_select_one_of_multiple_barcodes),null);
                    return;
                }
                pHandleScan(cBarcodeScan.pFakeScan(cIntakeorderBarcode.currentIntakeOrderBarcode.getBarcodeStr()), false);
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
                if (cIntakeorderBarcode.currentIntakeOrderBarcode == null) {
                    cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.message_select_one_of_multiple_barcodes),null);
                    return;
                }


                mTryToChangeQuantity(false, false, cIntakeorderBarcode.currentIntakeOrderBarcode.getQuantityPerUnitOfMeasureDbl());
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

        Intent intent = null;

        if (cIntakeorder.currentIntakeOrder.getOrderTypeStr().equalsIgnoreCase("MAS")) {
            intent = new Intent(cAppExtension.context, IntakeorderMASLinesActivity.class);
        }

        if (cIntakeorder.currentIntakeOrder.getOrderTypeStr().equalsIgnoreCase("MAT")) {
            intent = new Intent(cAppExtension.context, IntakeorderMATLinesActivity.class);
        }

        cAppExtension.activity.startActivity(intent);
        cAppExtension.activity.finish();
    }

    private void mResetCurrents() {

        cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine = null;
        cIntakeorder.currentIntakeOrder.intakeorderBarcodeScanned = null;
        cIntakeorderMATLine.currentIntakeorderMATLine = null;
        cIntakeorder.currentIntakeOrder.currentBin = null;
        this.scannedBarcodesObl = null;
        this.quantityScannedDbl = 0.0;
    }

    private void mResetCurrentsForUniqueBarcode() {
        cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine = null;
        cIntakeorderMATLine.currentIntakeorderMATLine = null;
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
        bundle.putInt(cPublicDefinitions.NUMBERINTENT_CURRENTQUANTITY, cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getQuantityHandledDbl().intValue());

        if (cIntakeorder.currentIntakeOrder.getReceiveStoreAutoAcceptAtRequestedBln()) {
            bundle.putDouble(cPublicDefinitions.NUMBERINTENT_MAXQUANTITY, cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getQuantityDbl().intValue());
        } else {
            bundle.putDouble(cPublicDefinitions.NUMBERINTENT_MAXQUANTITY,99999);
        }

        NumberpickerFragment numberpickerFragment = new NumberpickerFragment();
        numberpickerFragment.setArguments(bundle);

        numberpickerFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.NUMBERFRAGMENT_TAG);
    }

    private void mSetArticleInfo(){

        if (cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine == null) {
            this.articleContainer.setVisibility(View.INVISIBLE);
            return;
        }

        this.articleContainer.setVisibility(View.VISIBLE);
        this.articleDescriptionText.setText(cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getDescriptionStr());
        this.articleDescription2Text.setText(cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getDescription2Str());
        this.articleItemText.setText(cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getItemNoAndVariantCodeStr());

        if (cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getItemNoAndVariantCodeStr().equalsIgnoreCase(cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getDescriptionStr())){
            this.articleDescriptionText.setVisibility(View.GONE);
            this.articleDescription2Text.setVisibility(View.GONE);
        }
    }

    private void mSetQuantityInfo(){

        if (cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine == null) {
            this.quantityControlsContainer.setVisibility(View.INVISIBLE);
            return;
        }

        this.quantityControlsContainer.setVisibility(View.VISIBLE);
        this.quantityText.setText(cText.pDoubleToStringStr(this.quantityScannedDbl));

    }

    private void mSetBinInfo(){

        if (cIntakeorder.currentIntakeOrder.currentBin == null) {
            this.binContainer.setVisibility(View.INVISIBLE);
            return;
        }

        this.binContainer.setVisibility(View.VISIBLE);
        this.binText.setText(cIntakeorder.currentIntakeOrder.currentBin.getBinCodeStr());

    }

    private  List<cIntakeorderBarcode> mSortBarcodeList(List<cIntakeorderBarcode> pvUnsortedBarcodeObl) {

        List<cIntakeorderBarcode> resultList = new ArrayList<>();

        boolean barcodeFoundBln = false;

        for (cIntakeorderBarcode intakeorderBarcode : pvUnsortedBarcodeObl) {
            for (cIntakeorderBarcode resultBarcode : resultList) {
                if (resultBarcode.getBarcodeStr().equalsIgnoreCase(intakeorderBarcode.getBarcodeStr())) {
                    resultBarcode.quantityHandledDbl +=  intakeorderBarcode.getQuantityPerUnitOfMeasureDbl();
                    barcodeFoundBln = true;
                }
            }
            if (barcodeFoundBln) {
                barcodeFoundBln = false;
            }
            else {
                //new barcode, so add
                intakeorderBarcode.quantityHandledDbl = intakeorderBarcode.getQuantityPerUnitOfMeasureDbl();
                resultList.add(intakeorderBarcode);
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
        boolean resultBln = cIntakeorderMATLine.currentIntakeorderMATLine.pResetBln();
        if (! resultBln) {
            cUserInterface.pDoExplodingScreen(cAppExtension.activity.getString(R.string.message_reset_line_via_webservice_failed),"",true,true);
            return;
        }

        cUserInterface.pShowSnackbarMessage(this.imageButtonDone,cAppExtension.activity.getString(R.string.message_line_reset_succesfull), R.raw.headsupsound, true);

        //Renew data, so only current lines are shown
        this.mFieldsInitialize();
    }

    private void mSetScanlineConstraints(){

        if (cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine == null){
            ConstraintSet constraintSetSpace = new ConstraintSet();
            constraintSetSpace.clone(intakeorderIntakeGeneratedContainer);
            constraintSetSpace.connect(recyclerScanActions.getId(), ConstraintSet.TOP, binContainer.getId(), ConstraintSet.BOTTOM);
            constraintSetSpace.applyTo(intakeorderIntakeGeneratedContainer);
        }



    }

    //End Region Number Broadcaster

    //End Regin Private Methods

}

