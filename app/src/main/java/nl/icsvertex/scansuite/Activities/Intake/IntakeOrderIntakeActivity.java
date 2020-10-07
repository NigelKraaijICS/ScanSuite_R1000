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
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
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
import SSU_WHS.Basics.Article.cArticle;
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
import SSU_WHS.Move.MoveOrders.cMoveorder;
import nl.icsvertex.scansuite.Fragments.Dialogs.AcceptRejectFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.AddBinFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.ArticleFullViewFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.BarcodeFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.BinItemsFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.ItemStockFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.NumberpickerFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.ScanBinFragment;
import nl.icsvertex.scansuite.Fragments.Support.SupportFragment;
import nl.icsvertex.scansuite.R;

import static ICS.Utils.cText.pDoubleToStringStr;

public class IntakeOrderIntakeActivity extends AppCompatActivity implements iICSDefaultActivity, cIntakeorderMATLineRecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    //Region Private Properties

    public  boolean startedWithBINBln = false;

    private  int counterMinusHelperInt;
    private  int counterPlusHelperInt;
    private  Handler minusHandler;
    private  Handler plusHandler;
    private  AppCompatImageButton imageButtonMinus;
    private  AppCompatImageButton imageButtonPlus;
    private  AppCompatImageButton imageButtonDone;

    private  ConstraintLayout intakeorderIntakeContainer;

    private  ImageView toolbarImage;
    private  TextView toolbarTitle;
    private  TextView toolbarSubtext;

    private  TextView articleDescriptionText;
    private  TextView articleDescription2Text;
    private  TextView articleItemText;
    private  TextView articleBarcodeText;
    private  TextView articleVendorItemText;
    private  TextView genericItemExtraField1Text;
    private  TextView genericItemExtraField2Text;
    private  TextView genericItemExtraField3Text;
    private  TextView genericItemExtraField4Text;
    private  TextView genericItemExtraField5Text;
    private  TextView genericItemExtraField6Text;
    private  TextView genericItemExtraField7Text;
    private  TextView genericItemExtraField8Text;
    private  ImageView articleThumbImageView;
    private  ImageView imageButtonBarcode;

    private  CardView binContainer;
    private  TextView quantityText;
    private  TextView quantityRequiredText;
    private  TextView sourcenoText;

    private CardView containerContainer;
    private  TextView containerText;

    private  Boolean articleScannedBln = false;
    private  Boolean binScannedBln = false;
    private  Double quantityScannedDbl = 0.0;
    private  List<cIntakeorderBarcode> scannedBarcodesObl;
    private  cBranchBin currentBin;

    private  RecyclerView recyclerScanActions;
    private MenuItem item_enter_bin;

    private DrawerLayout menuActionsDrawer;
    private NavigationView actionMenuNavigation;

    private cIntakeorderMATLineAdapter intakeorderMATLineAdapter;
    private cIntakeorderMATLineAdapter getIntakeorderMATLineAdapter(){
        if (this.intakeorderMATLineAdapter == null) {
            this.intakeorderMATLineAdapter = new cIntakeorderMATLineAdapter();
        }
        return  this.intakeorderMATLineAdapter;
    }

    //End Region Private Properties

    //Region Default Methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intakeorder_intake);
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
            cBarcodeScan.pUnregisterBarcodeReceiver();
        } catch (Exception e) {
            e.printStackTrace();
        }
        LocalBroadcastManager.getInstance(cAppExtension.context).unregisterReceiver(mNumberReceiver);
        cBarcodeScan.pUnregisterBarcodeReceiver();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(cAppExtension.context).registerReceiver(mNumberReceiver, new IntentFilter(cPublicDefinitions.NUMBERINTENT_NUMBER));
        cBarcodeScan.pRegisterBarcodeReceiver();
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


        this.item_enter_bin = pvMenu.findItem(R.id.item_enter_bin);
        this.item_enter_bin .setVisible(true);

        if (cIntakeorder.currentIntakeOrder.currentBin != null)  {
            this.item_enter_bin.setVisible(false);
            return true;
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

        cIntakeorderMATLine.currentIntakeorderMATLine = cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.MATLinesToShowObl().get(pvPositionInt);

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

        this.intakeorderIntakeContainer = findViewById(R.id.intakeorderIntakeContainer);
        this.toolbarImage = findViewById(R.id.toolbarImage);
        this.toolbarTitle = findViewById(R.id.toolbarTitle);
        this.toolbarSubtext = findViewById(R.id.toolbarSubtext);

        this.articleDescriptionText = findViewById(R.id.articleDescriptionText);
        this.articleDescription2Text = findViewById(R.id.articleDescription2Text);
        this.articleItemText = findViewById(R.id.articleItemText);
        this.articleBarcodeText = findViewById(R.id.articleBarcodeText);
        this.articleVendorItemText = findViewById(R.id.articleVendorItemText);

        this.binContainer = findViewById(R.id.binContainer);
        this.sourcenoText = findViewById(R.id.sourcenoText);

        this.containerContainer = findViewById(R.id.containerContainer);
        this.containerText = findViewById(R.id.containerText);

        this.genericItemExtraField1Text = findViewById(R.id.genericItemExtraField1Text);
        this.genericItemExtraField2Text = findViewById(R.id.genericItemExtraField2Text);
        this.genericItemExtraField3Text = findViewById(R.id.genericItemExtraField3Text);
        this.genericItemExtraField4Text = findViewById(R.id.genericItemExtraField4Text);
        this.genericItemExtraField5Text = findViewById(R.id.genericItemExtraField5Text);
        this.genericItemExtraField6Text = findViewById(R.id.genericItemExtraField6Text);
        this.genericItemExtraField7Text = findViewById(R.id.genericItemExtraField7Text);
        this.genericItemExtraField8Text = findViewById(R.id.genericItemExtraField8Text);

        this.quantityText = findViewById(R.id.quantityText);
        this.quantityRequiredText = findViewById(R.id.quantityRequiredText);

        this.articleThumbImageView = findViewById(R.id.articleThumbImageView);
        this.imageButtonBarcode = findViewById(R.id.imageButtonBarcode);
        this.imageButtonMinus = findViewById(R.id.imageButtonMinus);
        this.imageButtonPlus = findViewById(R.id.imageButtonPlus);
        this.imageButtonDone = findViewById(R.id.imageButtonDone);

        this.recyclerScanActions = findViewById(R.id.recyclerScanActions);

        this.menuActionsDrawer = findViewById(R.id.menuActionsDrawer);
        this.actionMenuNavigation = findViewById(R.id.actionMenuNavigation);
    }

    @Override
    public void mSetToolbar(String pvScreenTitle) {
        this.toolbarImage.setImageResource(R.drawable.ic_menu_intake);
        this.toolbarTitle.setText(pvScreenTitle);
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

        this.counterPlusHelperInt = 0;
        this.counterMinusHelperInt = 0;
        this.toolbarSubtext.setText(cIntakeorder.currentIntakeOrder.getOrderNumberStr());

        this.mSetArticleInfo();
        this.mSetBinInfo();
        this.mSetSourceNoInfo();
        this.mSetContainerInfo();
        this.mSetQuantityInfo();

        this.mEnablePlusMinusAndBarcodeSelectViews();
        this.mShowArticleImage();
        this.mShowOrHideGenericExtraFields();
        this.mShowBarcodeInfo();
        this.mShowLines();



    }

    @Override
    public void mInitScreen() {
        cBarcodeScan.pRegisterBarcodeReceiver();

        //Raise quantity with scanned barcodeStr, if we started this activity with a scan
        if (cIntakeorder.currentIntakeOrder.intakeorderBarcodeScanned != null) {
            this.pHandleScan(cBarcodeScan.pFakeScan(cIntakeorder.currentIntakeOrder.intakeorderBarcodeScanned.getBarcodeStr()));
            cIntakeorder.currentIntakeOrder.intakeorderBarcodeScanned =null;
        }

        if (cIntakeorder.currentIntakeOrder.currentBin != null) {
            this.startedWithBINBln = true;
            this.pHandleScan(cBarcodeScan.pFakeScan(cIntakeorder.currentIntakeOrder.currentBin.getBinCodeStr()));
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

    public void pHandleScan(cBarcodeScan pvBarcodeScan) {

        cUserInterface.pCheckAndCloseOpenDialogs();

        cResult hulpRst;

        boolean binCheckedBln = false;

        //This barcode matches multiple lay-outs so this can be a BIN or an article
        if (Objects.requireNonNull(cBarcodeLayout.pGetBarcodeLayoutByBarcodeObl(pvBarcodeScan.getBarcodeOriginalStr())).size() > 1) {

            //First check if this is a BIN
            cBranchBin branchBin =  cUser.currentUser.currentBranch.pGetBinByCode(cRegex.pStripRegexPrefixStr(pvBarcodeScan.getBarcodeOriginalStr()));

            if (branchBin != null) {

                hulpRst = this.mHandleBINScanRst(pvBarcodeScan);
                if (! hulpRst.resultBln) {
                    cUserInterface.pShowSnackbarMessage(intakeorderIntakeContainer,hulpRst.messagesStr(),null,true);
                    return;
                }

                //We are done
                this.mSendLine();
                return;
            }

            binCheckedBln = true;

        }

        //Check if we have scanned a BIN and check if there are not handled linesInt for this BIN
        if (!binCheckedBln && cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(), cBarcodeLayout.barcodeLayoutEnu.BIN)) {

            hulpRst = this.mHandleBINScanRst(pvBarcodeScan);
            if (! hulpRst.resultBln) {
                cUserInterface.pShowSnackbarMessage(intakeorderIntakeContainer,hulpRst.messagesStr(),null,true);
                return;
            }

            if (!this.startedWithBINBln) {
                //We are done
                this.mSendLine();
                return;

            }

            return;

        }

        //Check if we have scanned an ARTICLE and check if there are not handled linesInt for this BIN
        if (cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(), cBarcodeLayout.barcodeLayoutEnu.ARTICLE)) {

            hulpRst = this.mHandleArticleScanRst(pvBarcodeScan);
            if (! hulpRst.resultBln) {
                cUserInterface.pDoExplodingScreen(hulpRst.messagesStr(),"",true,true);
                return;
            }
            return;
        }

        cUserInterface.pShowSnackbarMessage(intakeorderIntakeContainer,cAppExtension.activity.getString(R.string.message_unknown_barcode),null,true);

    }

    public  void pAcceptStore() {

        if (this.currentBin == null) {
            cUserInterface.pShowSnackbarMessage(intakeorderIntakeContainer, cAppExtension.activity.getString(R.string.message_bin_required),null, true);
            return;
        }

        if (this.quantityScannedDbl == 0 ) {
            this.mResetCurrents();
            this.mGoBackToLinesActivity();
            return;
        }

        //We are done
        this.mSendLine();

    }

    public void pRegisterBarcodeReceiver(){
        cBarcodeScan.pRegisterBarcodeReceiver();
    }

    public void pCancelStore() {
        this.mResetCurrents();
        this.mGoBackToLinesActivity();
    }

    public  void pHandleHandleBINScanned(cBarcodeScan pvBarcodeScan) {

        cResult hulpRst;

        hulpRst = this.mHandleBINScanRst(pvBarcodeScan);
        if (! hulpRst.resultBln) {
            cUserInterface.pShowSnackbarMessage(intakeorderIntakeContainer,hulpRst.messagesStr(),null,true);
            return;
        }

    }

    //End Region Public Methods

    //Region Private Methods

    //Views


    private void mShowLines() {

        if (cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.MATLinesToShowObl() == null  || cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.MATLinesToShowObl().size() == 0) {
            this.mFillRecycler(new ArrayList<cIntakeorderMATLine>());
            return;
        }

        this.mFillRecycler(cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.MATLinesToShowObl());
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
                    pHandleScan(cBarcodeScan.pFakeScan(cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.barcodesObl().get(0).getBarcodeStr()));
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

    private  void mShowOrHideGenericExtraFields() {


        if (cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine == null) {
            this.genericItemExtraField1Text.setVisibility(View.INVISIBLE);
            this.genericItemExtraField2Text.setVisibility(View.INVISIBLE);
            this.genericItemExtraField3Text.setVisibility(View.INVISIBLE);
            this.genericItemExtraField4Text.setVisibility(View.INVISIBLE);
            this.genericItemExtraField5Text.setVisibility(View.INVISIBLE);
            this.genericItemExtraField6Text.setVisibility(View.INVISIBLE);
            this.genericItemExtraField7Text.setVisibility(View.INVISIBLE);
            this.genericItemExtraField8Text.setVisibility(View.INVISIBLE);
            return;
        }

        if (!cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getExtraField1Str().isEmpty()) {
            this.genericItemExtraField1Text.setVisibility(View.VISIBLE);
            this.genericItemExtraField1Text.setText(cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getExtraField1Str());
        }

        if (!cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getExtraField2Str().isEmpty()) {
            this.genericItemExtraField2Text.setVisibility(View.VISIBLE);
            this.genericItemExtraField2Text.setText(cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getExtraField2Str());
        }

        if (!cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getExtraField3Str().isEmpty()) {
            this.genericItemExtraField3Text.setVisibility(View.VISIBLE);
            this.genericItemExtraField3Text.setText(cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getExtraField3Str());
        }

        if (!cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getExtraField4Str().isEmpty()) {
            this.genericItemExtraField4Text.setVisibility(View.VISIBLE);
            this.genericItemExtraField4Text.setText(cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getExtraField4Str());
        }

        if (!cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getExtraField5Str().isEmpty()) {
            this.genericItemExtraField5Text.setVisibility(View.VISIBLE);
            this.genericItemExtraField5Text.setText(cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getExtraField5Str());
        }

        if (!cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getExtraField6Str().isEmpty()) {
            this.genericItemExtraField6Text.setVisibility(View.VISIBLE);
            this.genericItemExtraField6Text.setText(cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getExtraField6Str());
        }

        if (!cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getExtraField7Str().isEmpty()) {
            this.genericItemExtraField7Text.setVisibility(View.VISIBLE);
            this.genericItemExtraField7Text.setText(cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getExtraField7Str());
        }

        if (!cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getExtraField8Str().isEmpty()) {
            this.genericItemExtraField8Text.setVisibility(View.VISIBLE);
            this.genericItemExtraField8Text.setText(cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getExtraField8Str());
        }

    }

    private  void mShowBarcodeInfo() {

        if (cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine == null) {
            this.articleBarcodeText.setText("???");
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

    private  void mShowArticleImage() {

        this.articleThumbImageView.setImageDrawable(ContextCompat.getDrawable(cAppExtension.context, R.drawable.ic_no_image_lightgrey_24dp));

        //If pick with picture is false, then hide image view
        if (!cIntakeorder.currentIntakeOrder.isReceiveWithPictureBln()) {
            this.articleThumbImageView.setVisibility(View.INVISIBLE);
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
        if ((cIntakeorder.currentIntakeOrder.isReceiveWithAutoOpenBln())) {
            this.mShowFullArticleFragment();
        }

    }

    private  void mEnablePlusMinusAndBarcodeSelectViews() {

        if (cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine == null) {
            this.imageButtonMinus.setVisibility(View.INVISIBLE);
            this.imageButtonPlus.setVisibility(View.INVISIBLE);
            this.imageButtonBarcode.setVisibility(View.INVISIBLE);
            this.imageButtonDone.setVisibility(View.INVISIBLE);
            return;
        }

        this.imageButtonDone.setVisibility(View.VISIBLE);

        if (!cSetting.RECEIVE_AMOUNT_MANUAL_MA()) {
            this.imageButtonMinus.setVisibility(View.INVISIBLE);
            this.imageButtonPlus.setVisibility(View.INVISIBLE);
            this.imageButtonBarcode.setVisibility(View.INVISIBLE);
        }
        else {
            this.imageButtonMinus.setVisibility(View.VISIBLE);
            this.imageButtonPlus.setVisibility(View.VISIBLE);
            this.imageButtonBarcode.setVisibility(View.VISIBLE);
        }
    }

    //Scans and manual input

    private cResult mHandleArticleScanRst(cBarcodeScan pvBarcodeScan){

        cResult result = new cResult();
        result.resultBln = true;

        cIntakeorderBarcode intakeorderBarcode = cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.pGetBarcode(pvBarcodeScan);
        if (intakeorderBarcode == null) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.activity.getString(R.string.message_unknown_barcode_for_this_line));
            return result;
        }

        this.mBarcodeSelected(intakeorderBarcode);
        return result;

    }

    private  cResult mHandleBINScanRst(cBarcodeScan pvBarcodescan){

        cResult result = new cResult();
        result.resultBln = true;

        //We scanned a BIN, but we need to scan an article first
        if (! this.articleScannedBln) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.activity.getString(R.string.message_scan_article_first));
            return result;
        }

        //Strip barcodeStr from regex
        String barcodeWithoutPrefixStr = cRegex.pStripRegexPrefixStr(pvBarcodescan.getBarcodeOriginalStr());

        //Get the current BIN
        this.currentBin =  cUser.currentUser.currentBranch.pGetBinByCode(barcodeWithoutPrefixStr);
        if ( this.currentBin  == null) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.activity.getString(R.string.message_bin_unknown,barcodeWithoutPrefixStr));
            return result;
        }

        //Get the current line based on the scanned BIN
        cIntakeorderMATLine.currentIntakeorderMATLine = cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.pGetLineForBinCode(this.currentBin);

        //We could not find a match, and we are not allowed to add BINS
        if (cIntakeorderMATLine.currentIntakeorderMATLine == null && cIntakeorder.currentIntakeOrder.getReceiveNoExtraBinsBln()) {
            this.currentBin = null;
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.activity.getString(R.string.message_adding_or_changing_bin_now_allowed));
            return result;
        }

        //Current BIN has been set, so BIN is scanned
        this.binScannedBln = true;
        return  result;

    }

    private void mNumberClicked() {

        if (cIntakeorderBarcode.currentIntakeOrderBarcode == null) {
            cUserInterface.pDoNope(quantityText, false, false);
            cUserInterface.pShowSnackbarMessage(intakeorderIntakeContainer, getString(R.string.choose_barcode_first), null, false);
            return;
        }

        if (cIntakeorderBarcode.currentIntakeOrderBarcode.getQuantityHandledDbl() > 1) {
            cUserInterface.pDoNope(quantityText, true, true);
            cUserInterface.pShowSnackbarMessage(intakeorderIntakeContainer, getString(R.string.manual_input_only_barcodenumber_bigger1), null, false);
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
        }

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
                this.mCheckLineDone();
                return;


            } else {
                newQuantityDbl = this.quantityScannedDbl + pvAmountDbl;
            }

            //Check if we would exceed amount, then show message if needed
            if (newQuantityDbl > cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.pGetQuantityToHandleDbl()) {

                if (cIntakeorder.currentIntakeOrder.getReceiveNoExtraPiecesBln()) {
                    this.mShowExtraPiecesNotAllowed();

                    if (this.quantityScannedDbl> 0) {
                        this.articleScannedBln = true;
                        return;
                    }

                    this.articleScannedBln = false;
                    return;
                }
            }

            //Set the new quantityDbl and show in Activity
            this.quantityScannedDbl = newQuantityDbl;
            this.quantityText.setText(cText.pDoubleToStringStr(this.quantityScannedDbl));

            //Add a barcodeStr to the scanned barcodeStr list, so you can use it later when line is determined
            this.scannedBarcodesObl.add(cIntakeorderBarcode.currentIntakeOrderBarcode);

            //Check if this line is done
            this.mCheckLineDone();
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
        this.imageButtonDone.setImageResource(R.drawable.ic_check_black_24dp);
    }

    private void mBarcodeSelected(cIntakeorderBarcode pvBarcode) {

        cUserInterface.pCheckAndCloseOpenDialogs();

        cIntakeorderBarcode.currentIntakeOrderBarcode = pvBarcode;
        this.articleScannedBln = true;
        this.mShowBarcodeInfo();

        this.mTryToChangeQuantity(true, false, cIntakeorderBarcode.currentIntakeOrderBarcode.getQuantityPerUnitOfMeasureDbl());
    }

    // Lines, Barcodes

    private  void mCheckLineDone() {

        //Start with complete
        this.imageButtonDone.setVisibility(View.VISIBLE);

        //If article or BIN hasn't been scanned then we are not done
        if (!this.articleScannedBln || !this.binScannedBln) {
            this.imageButtonDone.setVisibility(View.INVISIBLE);
            return;
        }

        //Check if quantityDbl is sufficient
        if (!cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.pQuantityReachedBln(this.scannedBarcodesObl)) {
            this.imageButtonDone.setVisibility(View.VISIBLE);
            this.imageButtonDone.setImageResource(R.drawable.ic_check_black_24dp);
            return;
        }

        //We are complete
        this.imageButtonDone.setVisibility(View.VISIBLE);
        this.imageButtonDone.setImageResource(R.drawable.ic_doublecheck_black_24dp);

        //If auto accept is false, we are done here
        if (!cIntakeorder.currentIntakeOrder.getReceiveStoreAutoAcceptAtRequestedBln()) {
            return;
        }

        //All is done
        this.mSendLine();

    }

    private void mSendLine() {

        //If internet is not connected
        if (!cConnection.isInternetConnectedBln()) {
            //could not send line, let user know but answer succes so user can go to next line
            cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.couldnt_send_line_no_connection), null);
            return;
        }

       if(!cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.pItemVariantHandledBln(this.currentBin.getBinCodeStr(),
               this.scannedBarcodesObl)) {
            cUserInterface.pDoExplodingScreen(cAppExtension.context.getString(R.string.couldnt_send_line), "",true,true);
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
                pHandleScan(cBarcodeScan.pFakeScan(cIntakeorderBarcode.currentIntakeOrderBarcode.getBarcodeStr()));
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
        this.quantityRequiredText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNumberClicked();
            }
        });
    }

    private void mSetMenuListener() {
        this.actionMenuNavigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment selectedFragment = null;
                switch (menuItem.getItemId()) {

                    case R.id.item_bin_stock:
                        selectedFragment = new BinItemsFragment(cMoveorder.currentMoveOrder.currentBranchBin.getBinCodeStr());
                        break;

                    case R.id.item_article_stock:
                        selectedFragment = new SupportFragment();
                        break;

                    default:
                        break;
                }

                if (selectedFragment != null) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.mainFramelayout, selectedFragment);
                    transaction.commit();
                }

                // deselect everything
                int size = actionMenuNavigation.getMenu().size();
                for (int i = 0; i < size; i++) {
                    actionMenuNavigation.getMenu().getItem(i).setChecked(false);
                }

                // set item as selected to persist highlight
                menuItem.setChecked(true);
                // close drawer when item is tapped
                menuActionsDrawer.closeDrawers();
                return true;
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

    private void mShowExtraPiecesNotAllowed(){
        this.quantityText.setText(this.quantityRequiredText.getText());
        cUserInterface.pShowSnackbarMessage(this.toolbarImage , cAppExtension.context.getString(R.string.number_cannot_be_higher), null, false);
        cUserInterface.pDoNope(this.quantityText, true, true);
        cUserInterface.pDoNope(this.quantityRequiredText, false, false);
    }

    private void mGoBackToLinesActivity() {
        Intent intent = new Intent(cAppExtension.context, IntakeorderLinesActivity.class);
        cAppExtension.activity.startActivity(intent);
        cAppExtension.activity.finish();
    }

    private void mResetCurrents() {

        cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine = null;
        cIntakeorderMATLine.currentIntakeorderMATLine = null;
        cIntakeorder.currentIntakeOrder.currentBin = null;
        this.currentBin = null;
        this.articleScannedBln = false;
        this.binScannedBln = false;
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
            this.articleDescriptionText.setText("???");
            this.articleDescription2Text.setText("???");
            this.articleItemText.setText("???");
            this.articleVendorItemText.setText("???");
            return;
        }
        this.articleDescriptionText.setText(cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getDescriptionStr());
        this.articleDescription2Text.setText(cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getDescription2Str());
        this.articleItemText.setText(cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getItemNoAndVariantCodeStr());
        this.articleVendorItemText.setText(cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getVendorItemNoAndDescriptionStr());
    }

    private void mSetQuantityInfo(){

        if (cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine == null) {
            this.quantityText.setText("0");
            this.quantityRequiredText.setText("???");
            return;
        }

        this.quantityText.setText("0");
        this.quantityRequiredText.setText(pDoubleToStringStr(cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.pGetQuantityToHandleDbl()));

    }

    private void mSetBinInfo(){
        this.binContainer.setVisibility(View.GONE);
    }

    private void mSetSourceNoInfo(){
        this.sourcenoText.setText(cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getSourceNoStr());
    }

    private void mSetContainerInfo(){

        if (cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getContainerStr().isEmpty()) {
            this.containerContainer.setVisibility(View.GONE);
            return;
        }
        this.containerContainer.setVisibility(View.VISIBLE);
        this.containerText.setText(cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getContainerStr());
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

    private void mRemoveAdapterFromFragment(){

        //remove the item from recyclerview
        boolean resultBln = cIntakeorderMATLine.currentIntakeorderMATLine.pResetBln();
        if (! resultBln) {
            cUserInterface.pDoExplodingScreen(cAppExtension.activity.getString(R.string.message_reset_line_via_webservice_failed),"",true,true);
            return;
        }

        //Renew data, so only current lines are shown
        this.mFieldsInitialize();
    }

    //End Region Number Broadcaster

    //End Regin Private Methods

}

