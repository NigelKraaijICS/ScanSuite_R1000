package nl.icsvertex.scansuite.Activities.Pick;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import ICS.Interfaces.iICSDefaultActivity;
import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.Scanning.cProGlove;
import ICS.Utils.cConnection;
import ICS.Utils.cNoSwipeViewPager;
import ICS.Utils.cRegex;
import ICS.Utils.cResult;
import ICS.Utils.cSharedPreferences;
import ICS.Utils.cText;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Basics.Article.cArticle;
import SSU_WHS.Basics.BarcodeLayouts.cBarcodeLayout;
import SSU_WHS.Basics.ContentlabelContainer.cContentlabelContainer;
import SSU_WHS.Basics.Settings.cSetting;
import SSU_WHS.Basics.Users.cUser;
import SSU_WHS.Basics.Workplaces.cWorkplace;
import SSU_WHS.General.Comments.cComment;
import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.General.cPublicDefinitions;
import SSU_WHS.Picken.PickorderBarcodes.cPickorderBarcode;
import SSU_WHS.Picken.PickorderLines.cPickorderLine;
import SSU_WHS.Picken.Pickorders.cPickorder;
import nl.icsvertex.scansuite.Activities.Ship.ShiporderLinesActivity;
import nl.icsvertex.scansuite.Activities.Sort.SortorderLinesActivity;
import nl.icsvertex.scansuite.Activities.Store.StoreorderLinesActivity;
import nl.icsvertex.scansuite.Fragments.Dialogs.AcceptRejectFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.BinItemsFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.CommentFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.ContentlabelContainerFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.CurrentLocationFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.ItemStockFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.PrintBinLabelFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.PrintItemLabelFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.SendingFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.StepDoneFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.WorkplaceFragment;
import nl.icsvertex.scansuite.Fragments.Pick.PickorderLinesToPickFragment;
import nl.icsvertex.scansuite.PagerAdapters.PickorderLinesPagerAdapter;
import nl.icsvertex.scansuite.R;

import static ICS.Utils.Scanning.cProGlove.PROGLOVE_DISPLAY_TEMPLATE_2FIELD_2HEADER;
import static ICS.Utils.Scanning.cProGlove.PROGLOVE_FEEDBACK_NEGATIVE;
import static ICS.Utils.Scanning.cProGlove.PROGLOVE_FEEDBACK_PURPLE;
import static SSU_WHS.General.cPublicDefinitions.SHAREDPREFERENCE_USEPROGLOVE;

public class PickorderLinesActivity extends AppCompatActivity implements iICSDefaultActivity {

    //Region Public Properties
    public static Fragment currentLineFragment;
    public static boolean startedViaOrderSelectBln;
    //End Region Public Properties

    //Region Private Properties

    //Region Views
    private TextView quantityPickordersText;
    private TabLayout pickorderLinesTabLayout;
    private cNoSwipeViewPager pickorderLinesViewPager;
    private ImageView imageButtonComments;

    private ImageView toolbarImage;
    private TextView toolbarTitle;
    private  TextView toolbarSubtext;

    private ImageView imageButtonCloseOrder;
    private DrawerLayout menuActionsDrawer;
    private NavigationView actionMenuNavigation;

    public static Boolean shipFromPickBln;
    //End Region Views

    //End Region Private Properties

    //Region Default Methods

    @Override
    protected void onCreate(Bundle pvSavedInstanceState) {
        super.onCreate(pvSavedInstanceState);
        setContentView(R.layout.activity_pickorderlines);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);


    }

    @Override
    protected void onDestroy() {
                super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.mActivityInitialize();
        cBarcodeScan.pRegisterBarcodeReceiver(this.getClass().getSimpleName());
        cConnection.pRegisterWifiChangedReceiver();
        cUserInterface.pEnableScanner();
    }

    @Override
    protected void onPause() {
        try {
            cBarcodeScan.pUnregisterBarcodeReceiver(this.getClass().getSimpleName());
            cConnection.pUnregisterWifiChangedReceiver();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu pvMenu) {
        getMenuInflater().inflate(R.menu.menu_pick,pvMenu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu pvMenu) {
        //  invalidateOptionsMenu();

        MenuItem item_bin_stock = pvMenu.findItem(R.id.item_bin_stock);
        MenuItem item_article_stock = pvMenu.findItem(R.id.item_article_stock);

        item_bin_stock.setVisible(cPickorderLine.currentPickOrderLine != null);
        item_article_stock.setVisible(cPickorderLine.currentPickOrderLine != null);

        if (cSetting.GENERIC_PRINT_BINLABEL()){
            MenuItem item_print_bin = pvMenu.findItem(R.id.item_print_bin);
            item_print_bin.setVisible(cPickorderLine.currentPickOrderLine != null);
        }
        if (cSetting.GENERIC_PRINT_ITEMLABEL()){
            MenuItem item_print_item = pvMenu.findItem(R.id.item_print_item);
            item_print_item.setVisible(cPickorderLine.currentPickOrderLine != null);
        }
        if (cPickorder.currentPickOrder.isPickPickToContainerBln() || cSetting.PICK_PICK_TO_CONTAINER()){
            MenuItem item_new_container = pvMenu.findItem(R.id.item_new_container);
            item_new_container.setVisible(true);

            if (cPickorder.currentPickOrder.contentlabelContainerObl != null){
                if (cPickorder.currentPickOrder.contentlabelContainerObl.size() > 1){
                    MenuItem item_switch_container = pvMenu.findItem(R.id.item_switch_container);
                    item_switch_container.setVisible(true);
                }
            }
        }

        MenuItem itemPairProglove = pvMenu.findItem(R.id.item_pair_proglove);
        if ( cSharedPreferences.pGetSharedPreferenceBoolean(SHAREDPREFERENCE_USEPROGLOVE, false)) {
            if (itemPairProglove != null) {
                itemPairProglove.setVisible(true);
            }
        }


        return super.onPrepareOptionsMenu(pvMenu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem pvMenuItem) {


        DialogFragment selectedFragment = null;

        switch (pvMenuItem.getItemId()) {

            case android.R.id.home:
                this.mTryToLeaveActivity();
                return true;

            case R.id.item_bin_stock:
                selectedFragment = new BinItemsFragment(cPickorderLine.currentPickOrderLine.getBinCodeStr());
                break;

            case R.id.item_article_stock:

                cArticle.currentArticle= cPickorder.currentPickOrder.articleObl.get(cPickorderLine.currentPickOrderLine.getItemNoAndVariantStr());
                selectedFragment = new ItemStockFragment();
                break;

            case R.id.item_print_bin:
                selectedFragment = new PrintBinLabelFragment();
                break;

            case R.id.item_print_item:
                if (cPickorderLine.currentPickOrderLine.pGetBarcodesObl()){
                    cArticle.currentArticle= cPickorder.currentPickOrder.articleObl.get(cPickorderLine.currentPickOrderLine.getItemNoAndVariantStr());
                    selectedFragment = new PrintItemLabelFragment();
                }
                else
                {  cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.no_barcodes_availabe_for_this_line),null);}
                break;
            case R.id.item_pair_proglove:
                cProGlove myProGlove = new cProGlove();
                myProGlove.pShowPairGlove();
                break;

            case R.id.item_new_container:
                this.mAddContentContainer();
                break;

            case R.id.item_switch_container:
                selectedFragment = new ContentlabelContainerFragment();
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

        return super.onOptionsItemSelected(pvMenuItem);
    }

    @Override
    public void onBackPressed() {
        mTryToLeaveActivity();
    }


    //End Region Default Methods

    //Region iICSDefaultActivity defaults

    @Override
    public void mActivityInitialize() {

        this.mSetAppExtensions();

        this.mFindViews();

        this.mSetListeners();

        if (cPickorder.currentPickOrder == null) {
            return;
        }

        this.mSetToolbar(getResources().getString(R.string.screentitle_pickorderlines));

        this.mFieldsInitialize();

        this.mInitScreen();

        this.mSetProGloveScreen();
    }

    @Override
    public void mSetAppExtensions() {
        cAppExtension.context = this;
        cAppExtension.fragmentActivity  = this;
        cAppExtension.activity = this;
        cAppExtension.fragmentManager  = getSupportFragmentManager();
    }

    @Override
    public void mFindViews() {

        this.toolbarImage = findViewById(R.id.toolbarImage);
        this.toolbarTitle = findViewById(R.id.toolbarTitle);
        this.toolbarSubtext = findViewById(R.id.toolbarSubtext);

        this.quantityPickordersText = findViewById(R.id.quantityPickordersText);
        this.pickorderLinesTabLayout = findViewById(R.id.pickorderLinesTabLayout);
        this.pickorderLinesViewPager = findViewById(R.id.pickorderLinesViewpager);

        this.imageButtonComments = findViewById(R.id.imageButtonComments);
        this.imageButtonCloseOrder = findViewById(R.id.imageButtonCloseOrder);
        this.menuActionsDrawer = findViewById(R.id.menuActionsDrawer);
        this.actionMenuNavigation = findViewById(R.id.actionMenuNavigation);
    }

    @Override
    public void mSetToolbar(String pvScreenTitleStr) {

        this.toolbarImage.setImageResource(R.drawable.ic_menu_pick);
        this.toolbarTitle.setText(pvScreenTitleStr);
        this.toolbarTitle.setSelected(true);
        this.toolbarSubtext.setText(cPickorder.currentPickOrder.getOrderNumberStr());
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

        this.imageButtonCloseOrder.setVisibility(View.INVISIBLE);
        this.mSetTabLayout();
    }

    @Override
    public void mSetListeners() {
        this.mSetShowCommentListener();
        this.mSetCloseOrderListener();
    }

    @Override
    public void mInitScreen() {

        this.mShowComments();

        //Call this here, because this is called everytime the activiy gets shown
        this.pCheckAllDone();

    }

    private void mSetProGloveScreen() {
        cProGlove myproglove= new cProGlove();
        String proglovedata = "1|" + getString(R.string.proglove_header_order) + "|" + cPickorder.currentPickOrder.getOrderNumberStr() + "|2|" +  "|" + getString(R.string.proglove_scan_article_or_bin);
        myproglove.pSendScreen(PROGLOVE_DISPLAY_TEMPLATE_2FIELD_2HEADER, proglovedata, true,0,0);
    }

    //End Region iICSDefaultActivity defaults

    //Region Public Methods

    public  void pChangeTabCounterText(String pvTextStr){
        this.quantityPickordersText.setText(pvTextStr);
    }

    public  void pPicklineSelected(cPickorderLine pvPickorderLine) {
        cPickorderLine.currentPickOrderLine = pvPickorderLine;

        if (PickorderLinesActivity.currentLineFragment instanceof  PickorderLinesToPickFragment) {
            PickorderLinesToPickFragment pickorderLinesToPickFragment = (PickorderLinesToPickFragment)PickorderLinesActivity.currentLineFragment;
            pickorderLinesToPickFragment.pSetChosenBinCode();
        }

    }

    public  void pPicklineToResetSelected(cPickorderLine pvPickorderLine) {
        cPickorderLine.currentPickOrderLine = pvPickorderLine;
    }

    public  void pSetCurrentLocation(String pvCurrentLocationStr) {

        if (!cPickorder.currentPickOrder.pUpdateCurrentLocationBln(pvCurrentLocationStr)) {
            cUserInterface.pDoExplodingScreen(cAppExtension.context.getString(R.string.error_currentlocation_could_not_update), "", true, false);

            String proglovedata = "1||" + cAppExtension.context.getString(R.string.error_currentlocation_could_not_update);

            cProGlove myproglove= new cProGlove();
            myproglove.pSendScreen(cProGlove.PROGLOVE_DISPLAY_TEMPLATE_1FIELD_0HEADER, proglovedata, false, 10, PROGLOVE_FEEDBACK_NEGATIVE);
            return;
        }

        //Check if we are done
        if (cPickorder.currentPickOrder.pGetLinesNotHandledFromDatabaseObl().size() > 0 ) {
            this.mStartOrderSelectActivity();
            return;
        }

        //We are done
        this.pPickingDone("");

    }

    public  void pHandleScan(cBarcodeScan pvBarcodeScan, Boolean pvBinSelectedBln) {

        cUserInterface.pCheckAndCloseOpenDialogs();
        cResult hulpResult;

        //BIN button has been pressed, so we already have a current line
        if (pvBinSelectedBln) {

            //Clear current barcodeStr
            cPickorderBarcode.currentPickorderBarcode = null;

            hulpResult = cPickorderLine.currentPickOrderLine.pLineBusyRst();
            if (!hulpResult.resultBln) {
                mStepFailed(hulpResult.messagesStr(),cWarehouseorder.StepCodeEnu.Pick_Picking,cWarehouseorder.WorkflowPickStepEnu.PickPicking);
                cPickorderLine.currentPickOrderLine = null;
                return;
            }

            //Set last selected index
            cPickorder.currentPickOrder.lastSelectedIndexInt = cPickorder.currentPickOrder.pGetIndexOfNotHandledLineInt(cPickorderLine.currentPickOrderLine);

            //we have a line to handle, so start Pick activity
            this.mStartPickActivity();
            return;
        }

        //If we scan a branch reset current branch
        if (cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(),cBarcodeLayout.barcodeLayoutEnu.LOCATION)) {
            cPickorder.currentPickOrder.scannedBranch  = null;
        }

        //If we still need a destination scan, make sure we scan this first
        if (cPickorder.currentPickOrder.scannedBranch  == null && cPickorder.currentPickOrder.isPFBln() ) {
            cResult hulpRst = this.mCheckDestionationRst(pvBarcodeScan);
            if (! hulpRst.resultBln) {
                cUserInterface.pDoExplodingScreen(hulpRst.messagesStr(),"", true, true);

                String proglovedata = "1||" + hulpRst.messagesStr();

                cProGlove myproglove= new cProGlove();
                myproglove.pSendScreen(cProGlove.PROGLOVE_DISPLAY_TEMPLATE_1FIELD_0HEADER, proglovedata, false, 10, PROGLOVE_FEEDBACK_NEGATIVE);
            }

            //If we scanned, refresh to pick fragment and leave this void
            if (PickorderLinesActivity.currentLineFragment instanceof PickorderLinesToPickFragment ) {
                PickorderLinesToPickFragment pickorderLinesToPickFragment = (PickorderLinesToPickFragment)PickorderLinesActivity.currentLineFragment;
                pickorderLinesToPickFragment.pBranchScanned();
                return;
            }
        }

        //Check if we have scanned a BIN and check if there are not handled linesInt for this BIN
        if (cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(),cBarcodeLayout.barcodeLayoutEnu.BIN)) {

            String barcodewithoutPrefix = cRegex.pStripRegexPrefixStr(pvBarcodeScan.getBarcodeOriginalStr());

            //Clear current barcodeStr
            cPickorderBarcode.currentPickorderBarcode = null;

            cPickorderLine.currentPickOrderLine = cPickorder.currentPickOrder.pGetNextLineToHandleForBin(barcodewithoutPrefix);
            if (cPickorderLine.currentPickOrderLine == null) {
                this.mDoUnknownScan(cAppExtension.context.getString(R.string.nothing_more_todo_for_bin), barcodewithoutPrefix);
                return;
            }

            hulpResult = cPickorderLine.currentPickOrderLine.pLineBusyRst();
            if (!hulpResult.resultBln) {
                this.mStepFailed(hulpResult.messagesStr(),cWarehouseorder.StepCodeEnu.Pick_Picking,cWarehouseorder.WorkflowPickStepEnu.PickPackAndShip);
                cPickorderLine.currentPickOrderLine = null;
                return;
            }

            //Set last selected index
            cPickorder.currentPickOrder.lastSelectedIndexInt = cPickorder.currentPickOrder.pGetIndexOfNotHandledLineInt(cPickorderLine.currentPickOrderLine);

            //we have a line to handle, so start Pick activity
            this.mStartPickActivity();
            return;
        }

        //Check if we have scanned an ARTICLE and check if there are not handled linesInt for this ARTICLE
        if (cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(),cBarcodeLayout.barcodeLayoutEnu.ARTICLE)) {

            if (!cSetting.PICK_BIN_IS_ITEM() && !cPickorder.currentPickOrder.isSingleBinBln() ) {
                //unknown scan
                this.mDoUnknownScan(cAppExtension.context.getString(R.string.error_article_scan_not_allowed), pvBarcodeScan.getBarcodeOriginalStr());
                return;
            }

            String barcodewithoutPrefix = cRegex.pStripRegexPrefixStr(pvBarcodeScan.getBarcodeOriginalStr());

            cPickorderLine pickorderLineMatched = cPickorder.currentPickOrder.pGetLineNotHandledByBarcode(pvBarcodeScan);

            // Article always had BIN, so ARTICLE is EQUAL to BIN
            if (pickorderLineMatched== null) {
                this.mDoUnknownScan(cAppExtension.context.getString(R.string.nothing_more_todo_for_article), barcodewithoutPrefix);
                return;
            }

            cPickorderLine.currentPickOrderLine = pickorderLineMatched;

            cPickorderBarcode.currentPickorderBarcode = cPickorderBarcode.pGetPickbarcodeViaBarcode(pvBarcodeScan);
            if (cPickorderBarcode.currentPickorderBarcode == null) {
                this.mDoUnknownScan(cAppExtension.context.getString(R.string.nothing_more_todo_for_article), barcodewithoutPrefix);
                return;
            }

            hulpResult = cPickorderLine.currentPickOrderLine.pLineBusyRst();
            if (!hulpResult.resultBln) {
                this.mStepFailed(hulpResult.messagesStr(),cWarehouseorder.StepCodeEnu.Pick_Picking,cWarehouseorder.WorkflowPickStepEnu.PickPackAndShip);
                return;
            }

            //Set last selected index
            cPickorder.currentPickOrder.lastSelectedIndexInt = cPickorder.currentPickOrder.pGetIndexOfNotHandledLineInt(cPickorderLine.currentPickOrderLine);

            if (cPickorderLine.currentPickOrderLine.getQuantityDbl() == 1) {
                if (cSetting.PICK_BIN_IS_ITEM() || cPickorder.currentPickOrder.isSingleBinBln() && !cPickorder.currentPickOrder.isPickAutoNextBln()) {
                    //Add barcode
                    cPickorderLine.currentPickOrderLine.pAddOrUpdateLineBarcode(cPickorderBarcode.currentPickorderBarcode.getQuantityPerUnitOfMeasureDbl());

                    //Update orderline info (quantityDbl, timestamp, localStatusInt)
                    cPickorderLine.currentPickOrderLine.pHandledIndatabase();
                    this.mSendPickorderLine();
                    return;
                }
            }

            cPickorder.currentPickOrder.pickorderBarcodeScanned = cPickorderBarcode.currentPickorderBarcode;

            //we have a line to handle, so start Pick activity
            this.mStartPickActivity();
            return;
        }

        //unknown scan
        this.mDoUnknownScan(cAppExtension.context.getString(R.string.error_unknown_barcode), pvBarcodeScan.getBarcodeOriginalStr());

    }

    public void pCheckAllDone() {

        // If not everything is sent, then leave
        if (cConnection.isInternetConnectedBln()){
            if (!this.mCheckAndSentLinesBln()) {
                return;
            }
        }

        // If not everything is done, then leave
        if (!this.mAllLinesDoneBln()) {
            this.imageButtonCloseOrder.setVisibility(View.INVISIBLE);
            return;
        }

        //All lines are done

        if (!cConnection.isInternetConnectedBln()) {
            cConnection.pShowTryAgainDialog();
            this.imageButtonCloseOrder.setVisibility(View.INVISIBLE);
            return;
        }

        // Show close button, so user can close the order manually
        this.imageButtonCloseOrder.setVisibility(View.VISIBLE);


        //We started an split order, so don't show done pop-up
        if (PickorderLinesActivity.startedViaOrderSelectBln && !cPickorder.currentPickOrder.getDocumentStr().isEmpty()) {
            return;
        }

        if (cPickorder.currentPickOrder.pQuantityHandledDbl() == 0) {
            // Show order done fragment
            this.mShowPickDoneFragment(false);
            return;
        }

        // If there is no next step, then we are done
        if (!cPickorder.currentPickOrder.isPackAndShipNeededBln() && !cPickorder.currentPickOrder.isBPBln() && !cPickorder.currentPickOrder.isBCBln()) {
            if (!cPickorder.currentPickOrder.isPickActivityBinRequiredBln() ||
                    !cPickorder.currentPickOrder.getCurrentLocationStr().isEmpty()) {

                // Show pick done fragment
                this.mShowPickDoneFragment(false);
                return;
            }

            // Show order done fragment
            this.mShowPickDoneFragment(true);
        }

        //Show Current Location fragment
        if (cPickorder.currentPickOrder.isPickActivityBinRequiredBln() &&
                cPickorder.currentPickOrder.getCurrentLocationStr().isEmpty()) {
            // Show order done fragment
            this.mShowCurrentLocationFragment();
            return;
        }

        //We are done
        this.pPickingDone("");

    }

    public  void pPickingDone(String pvCurrentLocationStr) {

        //If we received a current location, then update it via the webservice and locally
        //If we didn't receive a location, then we picked 0 items, so it's oke


        if (!pvCurrentLocationStr.isEmpty()){
            if (!cPickorder.currentPickOrder.pUpdateCurrentLocationBln(pvCurrentLocationStr)) {
                cUserInterface.pDoExplodingScreen(cAppExtension.context.getString(R.string.error_currentlocation_could_not_update), "", true, false);

                String proglovedata = "1||" + cAppExtension.context.getString(R.string.error_currentlocation_could_not_update);

                cProGlove myproglove= new cProGlove();
                myproglove.pSendScreen(cProGlove.PROGLOVE_DISPLAY_TEMPLATE_1FIELD_0HEADER, proglovedata, false, 10, PROGLOVE_FEEDBACK_NEGATIVE);

                return;
            }
        }

        if (cPickorder.currentPickOrder.isPickPickToContainerBln() || cSetting.PICK_PICK_TO_CONTAINER()){
            this.mShowWorkplaceFragment();
            return;
        }

        if (cPickorder.currentPickOrder.pQuantityHandledDbl() > 0 ) {
            //Pick Transfer
            if (cPickorder.currentPickOrder.isTransferBln()) {
                if (!cPickorder.currentPickOrder.isSortableBln()&&
                        cPickorder.currentPickOrder.isPickTransferAskWorkplaceBln() && cWorkplace.currentWorkplace == null ) {

                    //If we don't need to ship, then we ask for a workplace now otherwise ask it later
                    if (!cPickorder.currentPickOrder.isPackAndShipNeededBln()) {
                        this.mShowWorkplaceFragment();
                        return;
                    }
                }
            }

            else {

                //Pick Sales

                //Check if we need to select a workplaceStr
                if (!cPickorder.currentPickOrder.isSortableBln()&&
                   cPickorder.currentPickOrder.isPickSalesAskWorkplaceBln() && cWorkplace.currentWorkplace == null ) {

                    //If we don't need to ship, then we ask for a workplace now otherwise ask it later
                    if (!cPickorder.currentPickOrder.isPackAndShipNeededBln()) {
                        this.mShowWorkplaceFragment();
                        return;
                    }

                }

            }
        }
        else {

            //We did nothing, but still want to print manco documents
            if (!cPickorder.currentPickOrder.getDocumentStr().isEmpty()) {
                this.mShowWorkplaceFragment();
                return;
            }
        }

        this.pClosePickAndDecideNextStep();

    }

    public  void pClosePickAndDecideNextStep(){

        // Show that we are getting data
        cUserInterface.pShowGettingData();

        new Thread(this::mHandlePickFaseHandledAndDecideNextStep).start();

    }

    public  void pLeaveActivity(){

        cPickorder.currentPickOrder.pLockReleaseViaWebserviceBln(cWarehouseorder.StepCodeEnu.Pick_Picking,cWarehouseorder.WorkflowPickStepEnu.PickPicking);

        //If activity bin is not required, then don't show the fragment
        if (!cPickorder.currentPickOrder.isPickActivityBinRequiredBln() ||
            cPickorder.currentPickOrder.pQuantityHandledDbl() == 0 ||
            !cPickorder.currentPickOrder.getCurrentLocationStr().isEmpty()) {
            this.mStartOrderSelectActivity();
            return;
        }

        this.mShowCurrentLocationFragment();

    }

    public void pAskAbort() {

        cUserInterface.pCheckAndCloseOpenDialogs();

        AlertDialog.Builder builder = new AlertDialog.Builder(cAppExtension.context);
        builder.setTitle(R.string.message_abort_header);
        builder.setMessage(cAppExtension.activity.getString(R.string.message_abort_text));
        builder.setPositiveButton(R.string.button_abort, (dialogInterface, i) -> {
            cUserInterface.pCheckAndCloseOpenDialogs();
            mAbortOrder();
        });

        builder.setNeutralButton(R.string.cancel, (dialogInterface, i) -> cUserInterface.pCheckAndCloseOpenDialogs());

        builder.show();
    }

    //End Region Public Methods

    //Region Private Methods

    private  boolean mPickFaseHandledBln(){

        cResult hulpResult;
        hulpResult = new cResult();
        hulpResult.resultBln = false;

        hulpResult = cPickorder.currentPickOrder.pPickFaseHandledViaWebserviceRst();

        //Everything was fine, so we are done
        if (hulpResult.resultBln) {
            return true;
        }

        //Something went wrong, but no further actions are needed, so ony show reason of failure
        if ( hulpResult.activityActionEnu == cWarehouseorder.ActivityActionEnu.Unknown ) {
            cUserInterface.pDoExplodingScreen(hulpResult.messagesStr(),"",true,true);

            String proglovedata = "1||" + hulpResult.messagesStr();

            cProGlove myproglove= new cProGlove();
            myproglove.pSendScreen(cProGlove.PROGLOVE_DISPLAY_TEMPLATE_1FIELD_0HEADER, proglovedata, false, 10, PROGLOVE_FEEDBACK_NEGATIVE);


            return  false;
        }

        //Something went wrong, the order has been deleted, so show comments and refresh
        if (hulpResult.activityActionEnu == cWarehouseorder.ActivityActionEnu.Hold ) {

            //If we got any comments, show them
            if (cPickorder.currentPickOrder.pFeedbackCommentObl() != null && cPickorder.currentPickOrder.pFeedbackCommentObl().size() > 0 ) {
                //Process comments from webresult
                this.mShowCommentsFragment(cPickorder.currentPickOrder.pFeedbackCommentObl(), hulpResult.messagesStr());
            }

            return  false;
        }

        return true;

    }

    private  void mHandlePickFaseHandledAndDecideNextStep(){

        if (!this.mPickFaseHandledBln()) {
            return;
        }

        if (cPickorder.currentPickOrder.isStorableBln()) {
            this.mStoreNextStep();
            return;
        }


        if (cPickorder.currentPickOrder.isSortableBln()) {
            this.mSortNextStep();
            return;
        }

        //If Pack or Ship is not required, then we are dibe
        if (cPickorder.currentPickOrder.isPackAndShipNeededBln()) {
            this.mPackAndShipNextStap();
            return;
        }

        this.mStartOrderSelectActivity();

    }

    private void mChangeSelectedTab(TabLayout.Tab pvTab) {

        switch (pvTab.getPosition()) {
            case 0:
                this.pChangeTabCounterText(cText.pDoubleToStringStr(cPickorder.currentPickOrder.pQuantityNotHandledDbl()) + "/" + cText.pDoubleToStringStr(cPickorder.currentPickOrder.pQuantityTotalDbl()));
                break;
            case 1:
                this.pChangeTabCounterText(cText.pDoubleToStringStr(cPickorder.currentPickOrder.pQuantityHandledDbl()) + "/" + cText.pDoubleToStringStr(cPickorder.currentPickOrder.pQuantityTotalDbl()));
                break;
            case 2:
                this.pChangeTabCounterText(cText.pDoubleToStringStr(cPickorder.currentPickOrder.pQuantityTotalDbl()));
                break;
            default:

        }
    }

    private void mSetShowCommentListener() {
        this.imageButtonComments.setOnClickListener(view -> mShowCommentsFragment(cPickorder.currentPickOrder.pFeedbackAndPickCommentObl(),""));
    }

    private void mSetCloseOrderListener() {

        this.imageButtonCloseOrder.setOnClickListener(view -> {

          PickorderLinesActivity.startedViaOrderSelectBln = false;
          pCheckAllDone();

        });
    }

    private   void mDoUnknownScan(String pvErrorMessageStr, String pvScannedBarcodeStr) {
        cUserInterface.pDoExplodingScreen(pvErrorMessageStr, pvScannedBarcodeStr, true, true);

        String proglovedata = "1||" + pvErrorMessageStr;

        cProGlove myproglove= new cProGlove();
        myproglove.pSendScreen(cProGlove.PROGLOVE_DISPLAY_TEMPLATE_1FIELD_0HEADER, proglovedata, false, 10, PROGLOVE_FEEDBACK_NEGATIVE);

    }

    private  void mShowCommentsFragment(List<cComment> pvDataObl, String pvTitleStr) {

        cUserInterface.pCheckAndCloseOpenDialogs();

        Bundle bundle = new Bundle();
        bundle.putString(cPublicDefinitions.KEY_COMMENTHEADER, pvTitleStr);

        CommentFragment commentFragment = new CommentFragment(pvDataObl);
        commentFragment.setArguments(bundle);

        commentFragment.show(cAppExtension.fragmentManager , cPublicDefinitions.COMMENTFRAGMENT_TAG);
        cUserInterface.pPlaySound(R.raw.message, 0);
    }

    private  void mShowCurrentLocationFragment() {

        cUserInterface.pCheckAndCloseOpenDialogs();
        cUserInterface.pPlaySound(R.raw.goodsound, null);

        final CurrentLocationFragment currentLocationFragment = new CurrentLocationFragment();
        currentLocationFragment.setCancelable(true);
        currentLocationFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.CURRENTLOCATION_TAG);
    }

    private  void mShowWorkplaceFragment() {

        WorkplaceFragment workplaceFragment = new WorkplaceFragment();
        workplaceFragment.setCancelable(false);
        workplaceFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.WORKPLACEFRAGMENT_TAG);
    }

    private  void mShowPickDoneFragment(Boolean pvShowCurrentLocationBln) {

        cUserInterface.pPlaySound(R.raw.goodsound, null);

        final StepDoneFragment stepDoneFragment = new StepDoneFragment(cAppExtension.activity.getString(R.string.message_pick_done), cAppExtension.activity.getString(R.string.message_close_pick_fase) , pvShowCurrentLocationBln);
        stepDoneFragment.setCancelable(false);
        stepDoneFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.ORDERDONE_TAG);

        String proglovedata = "1||" + getResources().getString(R.string.proglove_check_terminal_screen);
        cProGlove myproglove= new cProGlove();
        myproglove.pSendScreen(cProGlove.PROGLOVE_DISPLAY_TEMPLATE_1FIELD_0HEADER, proglovedata, false, 10, PROGLOVE_FEEDBACK_PURPLE);
    }

    private  Boolean mCheckAndSentLinesBln() {

        final List<cPickorderLine> linesToSendObl = cPickorder.currentPickOrder.pGetLinesToSendFromDatabaseObl();

        // If there is nothing to send, then we are done
        if (linesToSendObl.size() == 0 ) {
            return  true;
        }

        this.mShowSending();


        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Callable<Boolean> callableBln = () -> {

            // Try to send each line, if one failes then stop
            for (cPickorderLine pickorderLine : linesToSendObl) {

                //Set the current line
                cPickorderLine.currentPickOrderLine = pickorderLine;

                //Try to send the line
               cPickorderLine.currentPickOrderLine .pHandledBln();

            }
            return  true;
        };

        try {
            Future<Boolean> callableResultBln = executorService.submit(callableBln);
            Boolean hulpBln = callableResultBln.get();

            if (!hulpBln) {
                    mShowNotSent();
                 return false;
            }
            this.mShowSent();
            return  true;
        }
        catch (InterruptedException | ExecutionException ignored) {
        }
      return  false;
    }

    private  Boolean mAllLinesDoneBln() {
        return cPickorder.currentPickOrder.pGetLinesNotHandledFromDatabaseObl().size() <= 0;
    }

    private  void mShowSending() {
        final SendingFragment sendingFragment = new SendingFragment();
        sendingFragment.setCancelable(true);
        cAppExtension.activity.runOnUiThread(() -> {
            // show my popup
            sendingFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.SENDING_TAG);
        });
    }

    private void mShowNotSent() {
        Fragment fragment = cAppExtension.fragmentManager.findFragmentByTag(cPublicDefinitions.SENDING_TAG);
        if (fragment != null) {
            if (fragment instanceof SendingFragment) {
                ((SendingFragment) fragment).pShowCrashAnimation("");
            }
        }
    }

    private  void mShowSent() {
        Fragment fragment = cAppExtension.fragmentManager.findFragmentByTag(cPublicDefinitions.SENDING_TAG);
        if (fragment != null) {
            if (fragment instanceof SendingFragment) {
                ((SendingFragment) fragment).pShowFlyAwayAnimation();
            }
        }
    }

    private  void mStepFailed(String pvErrorMessageStr, cWarehouseorder.StepCodeEnu pvStepCodeEnu,int pvWorkflowPickStepInt ){
        cUserInterface.pDoExplodingScreen(pvErrorMessageStr, cPickorder.currentPickOrder.getOrderNumberStr(), true, true );

        String proglovedata = "1||" + pvErrorMessageStr;

        cProGlove myproglove= new cProGlove();
        myproglove.pSendScreen(cProGlove.PROGLOVE_DISPLAY_TEMPLATE_1FIELD_0HEADER, proglovedata, false, 10, PROGLOVE_FEEDBACK_NEGATIVE);


        cPickorder.currentPickOrder.pLockReleaseViaWebserviceBln(pvStepCodeEnu,pvWorkflowPickStepInt);
        cUserInterface.pCheckAndCloseOpenDialogs();
    }

    private  void mAskSort() {

        cAppExtension.activity.runOnUiThread(() -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(cAppExtension.context);

            builder.setMessage(cAppExtension.context.getString(R.string.question_open_sort));
            builder.setTitle(cAppExtension.context.getString(R.string.question_open_sort_title));
            builder.setPositiveButton(R.string.open, (dialog, id) -> mStartSortActivity());
            builder.setNegativeButton(R.string.no, (dialogInterface, i) -> mStartOrderSelectActivity());
            builder.setIcon(R.drawable.ic_menu_sort);
            builder.show();
        });
    }

    private  void mAskShip() {

        cAppExtension.activity.runOnUiThread(() -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(cAppExtension.context);

            builder.setMessage(cAppExtension.context.getString(R.string.question_open_ship));
            builder.setTitle(cAppExtension.context.getString(R.string.question_open_ship_title));
            builder.setPositiveButton(R.string.open, (dialog, id) -> mStartShipActivity());
            builder.setNegativeButton(R.string.no, (dialogInterface, i) -> mStartOrderSelectActivity());
            builder.setIcon(R.drawable.ic_menu_ship);
            builder.show();
        });



    }

    private  void mStoreNextStep(){

        //If activity bin is not required, then don't show the fragment
        if ( cPickorder.currentPickOrder.pQuantityHandledDbl() == 0 ) {
            this.mStartOrderSelectActivity();
            return;
        }

        // If setting is not defined, then ask user
        if (cSetting.PICK_STORAGE_AUTO_START() == null) {
            this.mAskSort();
            return;
        }

        // If settings is false, then go back to order select
        if (!cSetting.PICK_STORAGE_AUTO_START()) {
            this.mStartOrderSelectActivity();
            return;
        }

        // If settings is true, then go  to sort
        if (cSetting.PICK_STORAGE_AUTO_START()) {
            this.mStartStoreActivity();
        }

    }

    private  void mSortNextStep(){

        //If activity bin is not required, then don't show the fragment
        if ( cPickorder.currentPickOrder.pQuantityHandledDbl() == 0 ) {
            this.mStartOrderSelectActivity();
            return;
        }

        // If setting is not defined, then ask user
        if (cSetting.PICK_PACK_AND_SHIP_AUTO_START() == null) {
            this.mAskSort();
            return;
        }

        // If settings is false, then go back to order select
        if (!cSetting.PICK_SORT_AUTO_START()) {
            this.mStartOrderSelectActivity();
            return;
        }

        // If settings is true, then go  to sort
        if (cSetting.PICK_SORT_AUTO_START()) {
            this.mStartSortActivity();
        }

    }

    private void mPackAndShipNextStap(){

        //If activity bin is not required, then don't show the fragment
        if ( cPickorder.currentPickOrder.pQuantityHandledDbl() == 0 ) {
            this.mStartOrderSelectActivity();
            return;
        }



        // If setting is not defined, then ask user
        if (cSetting.PICK_PACK_AND_SHIP_AUTO_START() == null) {
            this.mAskShip();
            return;
        }

        // If settings is false, then go back to order select
            if (!cSetting.PICK_PACK_AND_SHIP_AUTO_START()) {
                this.mStartOrderSelectActivity();
                return;
            }

        // If settings is true, then go  to ship
            if (cSetting.PICK_PACK_AND_SHIP_AUTO_START()) {
                this.mStartShipActivity();
            }

    }

    private void mTryToLeaveActivity(){

        cUserInterface.pCheckAndCloseOpenDialogs();

        final AcceptRejectFragment acceptRejectFragment = new AcceptRejectFragment(cAppExtension.activity.getString(R.string.message_sure_leave_pick_screen_title),
                cAppExtension.activity.getString(R.string.message_sure_leave_pick_screen_text),cAppExtension.activity.getString(R.string.message_cancel),cAppExtension.activity.getString(R.string.message_leave), false);
        acceptRejectFragment.setCancelable(true);
        cAppExtension.activity.runOnUiThread(() -> {
            // show my popup
            acceptRejectFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.ACCEPTREJECTFRAGMENT_TAG);
        });

    }

    private  void mStartOrderSelectActivity() {

        cAppExtension.activity.runOnUiThread(() -> {
            Intent intent = new Intent(cAppExtension.context, PickorderSelectActivity.class);
            PickorderSelectActivity.startedViaMenuBln = false;
           startActivity(intent);
           finish();
        });

    }

    private  void mStartSortActivity() {

        cUserInterface.pShowGettingData();

        new Thread(this::mHandleStartSortActivity).start();

    }

    private  void mStartStoreActivity() {

        cUserInterface.pShowGettingData();

        new Thread(this::mHandleStartStoreActivity).start();

    }

    private  void mHandleStartStoreActivity(){

        //Clear workplaceStr, so you have to select it in the next step
        cWorkplace.currentWorkplace = null;

        //Try to lock the pickorder
        if (!cPickorder.currentPickOrder.pLockViaWebserviceRst(cWarehouseorder.StepCodeEnu.Pick_Picking, cWarehouseorder.WorkflowPickStepEnu.PickStorage).resultBln) {
            this.mStepFailed(cAppExtension.context.getString(R.string.error_couldnt_lock_order),cWarehouseorder.StepCodeEnu.Pick_Picking,cWarehouseorder.WorkflowPickStepEnu.PickStorage);
            return;
        }

        //Get sort linesInt
        if (!cPickorder.currentPickOrder.pGetStorageLinesViaWebserviceBln(true)) {
            this.mStepFailed(cAppExtension.context.getString(R.string.error_getting_sort_lines_failed),cWarehouseorder.StepCodeEnu.Pick_Picking,cWarehouseorder.WorkflowPickStepEnu.PickStorage);
            return;
        }

        cAppExtension.activity.runOnUiThread(() -> {
            //Show Sort Activity
            Intent intent = new Intent(cAppExtension.context, StoreorderLinesActivity.class);
            cAppExtension.activity.startActivity(intent);
        });
    }

    private  void mHandleStartSortActivity(){

        //Clear workplaceStr, so you have to select it in the next step
        cWorkplace.currentWorkplace = null;

        //Try to lock the pickorder
        if (!cPickorder.currentPickOrder.pLockViaWebserviceRst(cWarehouseorder.StepCodeEnu.Pick_Picking, cWarehouseorder.WorkflowPickStepEnu.PickSorting).resultBln) {
            this.mStepFailed(cAppExtension.context.getString(R.string.error_couldnt_lock_order),cWarehouseorder.StepCodeEnu.Pick_Picking,cWarehouseorder.WorkflowPickStepEnu.PickPackAndShip);
            return;
        }

        //Get sort linesInt
        if (!cPickorder.currentPickOrder.pGetLinesViaWebserviceBln(true,cWarehouseorder.PickOrderTypeEnu.SORT)) {
            this.mStepFailed(cAppExtension.context.getString(R.string.error_getting_sort_lines_failed),cWarehouseorder.StepCodeEnu.Pick_Picking,cWarehouseorder.WorkflowPickStepEnu.PickPackAndShip);
            return;
        }

        if (!cPickorder.currentPickOrder.pGetPropertyLineDataViaWebserviceBln()) {
            this.mStepFailed(cAppExtension.context.getString(R.string.error_get_property_line_data_failed),cWarehouseorder.StepCodeEnu.Pick_Picking,cWarehouseorder.WorkflowPickStepEnu.PickPackAndShip);
            return;
        }

        cAppExtension.activity.runOnUiThread(() -> {
            //Show Sort Activity
            Intent intent = new Intent(cAppExtension.context, SortorderLinesActivity.class);
            cAppExtension.activity.startActivity(intent);
        });
    }

    private  void mStartShipActivity() {

        PickorderLinesActivity.shipFromPickBln = true;
        ShiporderLinesActivity.startedFromOrderSelectBln = true;
        cUserInterface.pShowGettingData();

        new Thread(this::mHandleStartShipActivity).start();

        String proglovedata = "1||" + getResources().getString(R.string.proglove_check_terminal_screen);
        cProGlove myproglove= new cProGlove();
        myproglove.pSendScreen(cProGlove.PROGLOVE_DISPLAY_TEMPLATE_1FIELD_0HEADER, proglovedata, false, 0, PROGLOVE_FEEDBACK_PURPLE);

    }

    private  void mHandleStartShipActivity(){

        cResult hulpResult;

        if (!this.mTryToLockShipOrderBln()) {
            return;
        }

        hulpResult = cPickorder.currentPickOrder.pGetShipmentDetailsRst();
        if (!hulpResult.resultBln) {
            this.mStepFailed(hulpResult.messagesStr(),cWarehouseorder.StepCodeEnu.Pick_PackAndShip, cWarehouseorder.WorkflowPickStepEnu.PickPackAndShip);
            return;
        }

        //Show ShipLines
        Intent intent = new Intent(cAppExtension.context, ShiporderLinesActivity.class);
        cAppExtension.activity.startActivity(intent);

    }

    private  void mStartPickActivity(){
        //we have a line to handle, so start Pick activity
        Intent intent = new Intent(cAppExtension.context, PickorderPickActivity.class);
        cAppExtension.activity.startActivity(intent);
    }

    private  void mShowComments(){

        if (cPickorder.currentPickOrder.pFeedbackAndPickCommentObl() == null || cPickorder.currentPickOrder.pFeedbackAndPickCommentObl().size() == 0) {
            this.imageButtonComments.setVisibility(View.INVISIBLE);
            return;
        }

        this.imageButtonComments.setVisibility(View.VISIBLE);

        //We already showed the comments
        if (cComment.commentsShownBln) {
            return;
        }

        this.mShowCommentsFragment(cPickorder.currentPickOrder.pFeedbackAndPickCommentObl(),"");
        cComment.commentsShownBln = true;
    }

    private  boolean mTryToLockShipOrderBln(){

        cResult hulpResult;
        hulpResult = cPickorder.currentPickOrder.pLockViaWebserviceRst(cWarehouseorder.StepCodeEnu.Pick_PackAndShip, cWarehouseorder.WorkflowPickStepEnu.PickPackAndShip);

        //Everything was fine, so we are done
        if (hulpResult.resultBln) {
            return true;
        }

        //Something went wrong, but no further actions are needed, so ony show reason of failure
        if (hulpResult.activityActionEnu == cWarehouseorder.ActivityActionEnu.Unknown ) {
            this.mStepFailed(hulpResult.messagesStr(),cWarehouseorder.StepCodeEnu.Pick_PackAndShip, cWarehouseorder.WorkflowPickStepEnu.PickPackAndShip);
            return  false;
        }

        //Something went wrong, the order has been deleted, so show comments and refresh
        if ( hulpResult.activityActionEnu == cWarehouseorder.ActivityActionEnu.Delete ||
              hulpResult.activityActionEnu == cWarehouseorder.ActivityActionEnu.NoStart ) {


            //If we got any comments, show them
            if (cPickorder.currentPickOrder.pFeedbackCommentObl() != null && cPickorder.currentPickOrder.pFeedbackCommentObl().size() > 0 ) {
                //Process comments from webresult
                this.mShowCommentsFragment(cPickorder.currentPickOrder.pFeedbackCommentObl(), hulpResult.messagesStr());
            }

            return  false;
        }


        return true;

    }

    private  cResult mCheckDestionationRst(cBarcodeScan pvBarcodeScan) {

        cResult resultRst = new cResult();

        //If we don't need a branch, we are done
        if (!cPickorder.currentPickOrder.isPFBln()) {
            resultRst.resultBln = true;
            return  resultRst;
        }

        if (cPickorder.currentPickOrder.destionationBranch() != null) {
            cPickorder.currentPickOrder.scannedBranch = cPickorder.currentPickOrder.destionationBranch();
            resultRst.resultBln = true;
            return  resultRst;
        }

        //Check if scan matches a branch in open lines
        cPickorder.currentPickOrder.scannedBranch = cPickorder.currentPickOrder.pGetBranchForOpenLines(pvBarcodeScan.getBarcodeOriginalStr());
        if (cPickorder.currentPickOrder.scannedBranch  != null) {
            resultRst.resultBln = true;
            return  resultRst;
        }

        //If we don't have a match, check if we have a location scan
        if (!cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(),cBarcodeLayout.barcodeLayoutEnu.LOCATION)) {
            cPickorder.currentPickOrder.scannedBranch  = null;
            resultRst.resultBln = false;
            resultRst.pAddErrorMessage(cAppExtension.activity.getString(R.string.message_scan_is_not_location));
            return  resultRst;
        }

        //We have a location scan, now strip the prefix and check if plain value matches a branch in open lines
        String barcodewithoutPrefix = cRegex.pStripRegexPrefixStr(pvBarcodeScan.getBarcodeOriginalStr());
        cPickorder.currentPickOrder.scannedBranch  = cPickorder.currentPickOrder.pGetBranchForOpenLines(barcodewithoutPrefix);
        if (cPickorder.currentPickOrder.scannedBranch  != null) {
            resultRst.resultBln = true;
            return  resultRst;
        }

        resultRst.resultBln = false;
        resultRst.pAddErrorMessage(cAppExtension.activity.getString(R.string.message_location_incorrect));
        return  resultRst;

    }

    private void mSetTabLayout(){

        if (this.pickorderLinesTabLayout.getTabCount() == 0) {
            this.pickorderLinesTabLayout.addTab(this.pickorderLinesTabLayout.newTab().setText(R.string.tab_pickorderline_topick));
            this.pickorderLinesTabLayout.addTab(this.pickorderLinesTabLayout.newTab().setText(R.string.tab_pickorderline_picked));
            this.pickorderLinesTabLayout.addTab(this.pickorderLinesTabLayout.newTab().setText(R.string.tab_pickorderline_total));
        }


        PickorderLinesPagerAdapter pickorderLinesPagerAdapter = new PickorderLinesPagerAdapter(this.pickorderLinesTabLayout.getTabCount());
        this.pickorderLinesViewPager.setAdapter(pickorderLinesPagerAdapter);
        this.pickorderLinesViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(this.pickorderLinesTabLayout));
        this.pickorderLinesTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab pvTab) {
                pickorderLinesViewPager.setCurrentItem(pvTab.getPosition());
                mChangeSelectedTab(pvTab);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab pvTab) {
                cUserInterface.pKillAllSounds();
            }

            @Override
            public void onTabReselected(TabLayout.Tab pvTab) {

            }
        });

    }

    private void mAbortOrder() {

        cUserInterface.pShowGettingData();

        if (!cPickorder.currentPickOrder.pAbortBln()) {
            cUserInterface.pDoExplodingScreen(cAppExtension.context.getString(R.string.error_couldnt_abort_order), cPickorderLine.currentPickOrderLine.getLineNoInt().toString(), true, true );

            String proglovedata = "1||" + cAppExtension.context.getString(R.string.error_couldnt_abort_order);

            cProGlove myproglove= new cProGlove();
            myproglove.pSendScreen(cProGlove.PROGLOVE_DISPLAY_TEMPLATE_1FIELD_0HEADER, proglovedata, false, 10, PROGLOVE_FEEDBACK_NEGATIVE);


            return;
        }

        cUserInterface.pHideGettingData();

        //Check if we are done
        PickorderLinesActivity.startedViaOrderSelectBln = false;
        this.pCheckAllDone();

    }

    private  void mSendPickorderLine() {

        //If internet is not connected
        if (!cConnection.isInternetConnectedBln()) {
            //could not send line, let user know but answer succes so user can go to next line
            cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.couldnt_send_line), R.raw.badsound);
            cPickorderLine.currentPickOrderLine.pErrorSending();
            return;
        }

        if (!cPickorderLine.currentPickOrderLine.pHandledBln()) {
            //could not send line, let user know but answer succes so user can go to next line
            cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.couldnt_send_line), R.raw.badsound);
            cPickorderLine.currentPickOrderLine.pErrorSending();
        }

        cPickorder.currentPickOrder.lastSelectedIndexInt = 0;
        cPickorderLine.currentPickOrderLine = null;
        cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.message_line_send), R.raw.headsupsound);
        this.mActivityInitialize();

    }

    private void mAddContentContainer(){

        Long sequenceNoLng = 0L;

        sequenceNoLng = cText.pIntegerToLongLng(cPickorder.currentPickOrder.contentlabelContainerObl.size() + 1);

        cContentlabelContainer contentlabelContainer = new cContentlabelContainer(sequenceNoLng,0.0);
        cPickorder.currentPickOrder.contentlabelContainerObl.add(contentlabelContainer);
    }

    //End Region Private Methods

}
