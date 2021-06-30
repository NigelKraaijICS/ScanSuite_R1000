package nl.icsvertex.scansuite.Activities.Returns;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ICS.Interfaces.iICSDefaultActivity;
import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cResult;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Basics.Article.cArticle;
import SSU_WHS.Basics.BarcodeLayouts.cBarcodeLayout;
import SSU_WHS.Basics.BranchReason.cBranchReason;
import SSU_WHS.Basics.Settings.cSetting;
import SSU_WHS.Basics.Users.cUser;
import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.General.cPublicDefinitions;
import SSU_WHS.Return.ReturnOrder.cReturnorder;
import SSU_WHS.Return.ReturnOrder.cReturnorderAdapter;
import SSU_WHS.Return.ReturnorderBarcode.cReturnorderBarcode;
import SSU_WHS.Return.ReturnorderDocument.cReturnorderDocument;
import SSU_WHS.Return.ReturnorderLine.cReturnorderLine;
import SSU_WHS.Return.ReturnorderLine.cReturnorderLineAdapter;
import SSU_WHS.Return.ReturnorderLine.cReturnorderLineRecyclerItemTouchHelper;
import SSU_WHS.Return.ReturnorderLineBarcode.cReturnorderLineBarcode;
import nl.icsvertex.scansuite.Activities.FinishShip.FinishShipLinesActivity;
import nl.icsvertex.scansuite.Fragments.Dialogs.AcceptRejectFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.AddArticleFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.CurrentLocationFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.NoOrdersFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.NothingHereFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.SendingFragment;
import nl.icsvertex.scansuite.Fragments.FinishShip.SinglePieceLinesToShipFragment;
import nl.icsvertex.scansuite.R;

public class ReturnorderDocumentActivity extends AppCompatActivity implements iICSDefaultActivity, cReturnorderLineRecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    //Region Public Properties
    public static Fragment currentLineFragment;
    public static Boolean busyBln =false;

    //End Region Public Properties

    //Region Private Properties
    private TextView documentText;
    private ImageView imageDocument;
    private ImageView toolbarImage;
    private ImageView imageDocumentDone;
    private TextView toolbarTitle;
    private TextView toolbarSubTitle;
    private RecyclerView recyclerViewReturnorderLines;
    private ImageView imageAddArticle;

    private cReturnorderLineAdapter returnorderLineAdapter;
    private cReturnorderLineAdapter getReturnorderLineAdapter(){
        if (this.returnorderLineAdapter == null) {
            this.returnorderLineAdapter = new cReturnorderLineAdapter();
        }
        return  this.returnorderLineAdapter;
    }

    private cReturnorderAdapter returnorderAdapter;
    private cReturnorderAdapter getReturnorderAdapter(){
        if (this.returnorderAdapter == null) {
            this.returnorderAdapter = new cReturnorderAdapter();
        }
        return  this.returnorderAdapter;
    }

    //End Region Private Properties

    //Region Default Methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_returnorder_document);
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
    }

    @Override
    protected void onPause() {
        super.onPause();
        cBarcodeScan.pUnregisterBarcodeReceiver(this.getClass().getSimpleName());
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem pvMenuItem) {

        if (pvMenuItem.getItemId() == android.R.id.home) {
            if (!cReturnorder.currentReturnOrder.getRetourMultiDocumentBln()){
                this.mTryToLeaveSingleDocumentActivity();
                return true;
            }
            this.mTryToLeaveActivity();
            return true;
        }

        return super.onOptionsItemSelected(pvMenuItem);
    }

    @Override
    public void onBackPressed() {
        if (!cReturnorder.currentReturnOrder.getRetourMultiDocumentBln()){
            this.mTryToLeaveSingleDocumentActivity();
            return;
        }
        this.mTryToLeaveActivity();
    }

    //End Region Default Methods

    //Region iICSDefaultActivity defaults

    @Override
    public void mActivityInitialize() {

        this.mSetAppExtensions();

        this.mFindViews();

        this.mSetToolbar(getResources().getString(R.string.screentitle_returnorderdocument));

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
        this.toolbarImage = findViewById(R.id.toolbarImage);
        this.toolbarTitle = findViewById(R.id.toolbarTitle);
        this.toolbarSubTitle = findViewById(R.id.toolbarSubtext);
        this.documentText = findViewById(R.id.documentText);
        this.imageDocument = findViewById(R.id.imageDocument);
        this.imageDocumentDone = findViewById(R.id.imageViewDocumentDone);
        this.recyclerViewReturnorderLines = findViewById(R.id.recyclerViewReturnorderLines);
        this.imageAddArticle = findViewById(R.id.imageAddArticle);
    }

    @Override
    public void mSetToolbar(String pvScreenTitleStr) {
        this.toolbarImage.setImageResource(R.drawable.ic_menu_return);
        this.toolbarTitle.setText(pvScreenTitleStr);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void mFieldsInitialize() {
        ViewCompat.setTransitionName(this.imageDocument, cPublicDefinitions.VIEW_CHOSEN_DOCUMENT_IMAGE);
        ViewCompat.setTransitionName(this.documentText, cPublicDefinitions.VIEW_CHOSEN_DOCUMENT);
        this.documentText.setText(cReturnorderDocument.currentReturnOrderDocument.getSourceDocumentStr());


        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new cReturnorderLineRecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(this.recyclerViewReturnorderLines);

    }

    @Override
    public void mSetListeners() {
        this.mSetToolbarTitleListeners();
        this.mSetAddArticleListener();
        this.mSetDoneListeners();
    }

    @Override
    public void mInitScreen() {
        this.pFillLines();

        if (cReturnorderDocument.currentReturnOrderDocument.barcodeScanToHandle != null) {
            this.pHandleScan(cReturnorderDocument.currentReturnOrderDocument.barcodeScanToHandle);
            cReturnorderDocument.currentReturnOrderDocument.barcodeScanToHandle = null;
        }

    }

    //End Region iICSDefaultActivity defaults

    //Region Public Methods

    public void pHandleScan(final cBarcodeScan pvBarcodeScan) {

        if (ReturnorderDocumentActivity.busyBln) {
            return;
        }

        //Close open Dialogs
        cUserInterface.pCheckAndCloseOpenDialogs();

        // Show that we are getting data and set busy boolean
        ReturnorderDocumentActivity.busyBln = true;
        cUserInterface.pShowGettingData();

        new Thread(() -> mHandleScan(pvBarcodeScan)).start();

    }

    public void pHandleSelectedLine(){
        //Close open Dialogs
        cUserInterface.pCheckAndCloseOpenDialogs();

        // Show that we are getting data and set busy boolean
        ReturnorderDocumentActivity.busyBln = true;
        cUserInterface.pShowGettingData();

        this.mHandleSelectedLine();
    }

    public void pFillLines() {

        List<cReturnorderLine> hulpObl = cReturnorderDocument.currentReturnOrderDocument.returnorderLineObl;
        if (hulpObl.size() == 0) {
            this.mShowNoLinesIcon(true);
            return;
        }

        this.mFillRecyclerView(hulpObl);
        this.mShowNoLinesIcon(false);

    }

    public void pLineHandled() {

        //We returned here, so we are not busy anymore
        ReturnorderDocumentActivity.busyBln = false;

        //Reset currents
        cReturnorderLine.currentReturnOrderLine = null;
        cReturnorderBarcode.currentReturnOrderBarcode = null;
        cReturnorderLineBarcode.currentreturnorderLineBarcode = null;
        cBranchReason.currentBranchReason = null;

        //Fill lines of Document
        this.pFillLines();
    }

    public  void pCloseDocument(String pvNewDocumentScanStr){
        cReturnorder.currentNewDocumentScan = pvNewDocumentScanStr;
        //If we still need to fill a current location, show pop-up and we are done.
        if (cReturnorder.currentReturnOrder.getCurrentLocationStr().isEmpty() && cSetting.RETOUR_VALIDATE_CURRENT_LOCATION()) {
            mShowCurrentLocationFragment();
            return;
        }

        boolean resultBln = cReturnorderDocument.currentReturnOrderDocument.pCloseBln();

        //Something went wrong
        if (! resultBln) {
            cUserInterface.pDoExplodingScreen(cAppExtension.activity.getString(R.string.message_document_close_fail),cReturnorderDocument.currentReturnOrderDocument.getSourceDocumentStr(),true,true);
            return;
        }

        if (!cReturnorder.currentReturnOrder.isRetourMultiDocument()) {

        mShowSending();
        new Thread(this::mOrderHandledViaWebservice).start();
        }

        else {
            //Clear cache
            cReturnorderDocument.currentReturnOrderDocument = null;

            //Start Documents activity
                this.mStartDocumentsActivity();
        }



    }

    public void pHandleFragmentDismissed(){
        cBarcodeScan.pRegisterBarcodeReceiver(this.getClass().getSimpleName());
    }

    public  void pSetCurrentLocation(String pvCurrentLocationStr) {

        if (!cReturnorder.currentReturnOrder.pUpdateCurrentLocationBln(pvCurrentLocationStr)) {
            cUserInterface.pDoExplodingScreen(cAppExtension.context.getString(R.string.error_currentlocation_could_not_update), "", true, false);
            return;
        }

        //We are done
        this.pCloseDocument("");

    }

    //End Region Public Methods

    //Region Private Methods

    private void mOrderHandledViaWebservice() {

        cResult hulpResult = cReturnorder.currentReturnOrder.pOrderHandledViaWebserviceRst();
        if (! hulpResult.resultBln) {
            this.mShowShipmentNotSent(hulpResult.messagesStr());
            return;
        }

        //Clear cache
        cReturnorderDocument.currentReturnOrderDocument = null;
        this.mShowSent();
        this.mStartOrderSelectActivity();

    }

    private void mHandleScan(cBarcodeScan pvBarcodeScan){


        if (cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(),cBarcodeLayout.barcodeLayoutEnu.DOCUMENT)) {

            //We scanned a NEW Document so check if we are allowed to add a Document
            if (cReturnorder.currentReturnOrder.isGeneratedBln() && !cReturnorder.currentReturnOrder.getRetourMultiDocumentBln() ) {
                this.mStepFailed(cAppExtension.activity.getString(R.string.message_document_add_not_allowed));
                ReturnorderDocumentActivity.busyBln = false;
                return;
            }

            //Close current Document
            //if we have a new scan, pass this new Document scan on to the Documents activity
            this.pCloseDocument(pvBarcodeScan.barcodeOriginalStr);

            //We are not busy anymore
            ReturnorderDocumentActivity.busyBln = false;

            //Pass this new Document scan on to the Documents activity
            //mStartDocumentsActivityWithScan(pvBarcodeScan.barcodeOriginalStr);
            //this.pHandleScan(pvBarcodeScan);
            return;
        }



        for ( cBarcodeLayout barcodeLayout :  cBarcodeLayout.allBarcodeLayoutsObl) {
            if (barcodeLayout.getBarcodeLayoutEnu() != cBarcodeLayout.barcodeLayoutEnu.ARTICLE){
                if (cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(), barcodeLayout.getBarcodeLayoutEnu())) {
                    this.mDoUnknownScan(cAppExtension.context.getString(R.string.error_article_scan_mandatory), pvBarcodeScan.getBarcodeOriginalStr());
                    ReturnorderDocumentActivity.busyBln = false;
                    return;
                }
            }
        }
        //Only ARTICLE scans are allowed


        //Check if this is a barcodeStr we already know
        cReturnorderBarcode returnorderBarcode = cReturnorder.currentReturnOrder.pGetOrderBarcode(pvBarcodeScan);

        //We scanned an unkown barcodeStr
        if (returnorderBarcode == null) {
            this.mHandleUnknownBarcodeScan(pvBarcodeScan);
            return;
        }

        //We scanned a barcodeStr we already know
        this.mHandleKnownBarcodeScan(returnorderBarcode, pvBarcodeScan);

    }

    private  void mDoUnknownScan(String pvErrorMessageStr, String pvScannedBarcodeStr) {
        cUserInterface.pDoExplodingScreen(pvErrorMessageStr, pvScannedBarcodeStr, true, true);
    }

    private void mTryToLeaveActivity() {

        if (cReturnorderDocument.currentReturnOrderDocument.returnorderLineObl.size() > 0) {
            mShowCloseDocumentDialog();
            return;
        }

        this.pCloseDocument("");
    }

    private  void mFillRecyclerView(List<cReturnorderLine> pvReturnorderLinesObl) {

        if (pvReturnorderLinesObl == null || pvReturnorderLinesObl.size() == 0) {
            return;
        }

        for (Fragment fragment: cAppExtension.fragmentManager.getFragments()) {
            if (fragment instanceof NoOrdersFragment) {
                cAppExtension.fragmentManager.beginTransaction().remove(fragment).commit();
            }
        }

        this.recyclerViewReturnorderLines.setHasFixedSize(false);
        this.recyclerViewReturnorderLines.setAdapter(this.getReturnorderLineAdapter());
        this.recyclerViewReturnorderLines.setLayoutManager(new LinearLayoutManager(cAppExtension.context));

        this.getReturnorderLineAdapter().pFillData(pvReturnorderLinesObl);
    }

    private void mSetToolbarTitleListeners() {
        this.toolbarTitle.setOnClickListener(view -> mScrollToTop());
        this.toolbarTitle.setOnLongClickListener(view -> {
            mScrollToBottom();
            return true;
        });
    }

    private void mSetDoneListeners() {

        this.imageDocumentDone.setOnClickListener(view -> mTryToLeaveActivity());

    }

    private void mScrollToTop() {
        recyclerViewReturnorderLines.smoothScrollToPosition(0);
    }

    private void mScrollToBottom() {
        if (this.getReturnorderAdapter() != null) {
            if (this.getReturnorderAdapter().getItemCount() > 0) {
                recyclerViewReturnorderLines.smoothScrollToPosition(this.getReturnorderLineAdapter().getItemCount() - 1);
            }
        }
    }

    private void mStartDocumentsActivity() {
        Intent intent = new Intent(cAppExtension.context, ReturnorderDocumentsActivity.class);
        startActivity(intent);
        finish();
    }

    private void mAddUnkownArticle(cBarcodeScan pvBarcodeScan){

        //Add the barcodeStr via the webservice
        if (!cReturnorder.currentReturnOrder.pAddUnkownBarcodeBln(pvBarcodeScan)) {
            cUserInterface.pDoExplodingScreen(cAppExtension.activity.getString(R.string.message_adding_unkown_article_failed),"",true,true);
            ReturnorderDocumentActivity.busyBln = false;
            return;
        }

        //Add line barcode to line
        cReturnorderLine.currentReturnOrderLine.pAddLineBarcode(cReturnorderBarcode.currentReturnOrderBarcode.getBarcodeStr(), cReturnorderBarcode.currentReturnOrderBarcode.getQuantityHandledDbl());

        //Add quantityDbl of the current barcodeStr
        cReturnorderLine.currentReturnOrderLine.quantityHandledTakeDbl += cReturnorderBarcode.currentReturnOrderBarcode.getQuantityPerUnitOfMeasureDbl();
        cReturnorderLineBarcode.currentreturnorderLineBarcode.quantityHandledDbl += cReturnorderBarcode.currentReturnOrderBarcode.getQuantityPerUnitOfMeasureDbl();

        //Make line barcodeStr the current line barcodeStr
        if (cReturnorderLine.currentReturnOrderLine.lineBarcodesObl != null) {
            cReturnorderLineBarcode.currentreturnorderLineBarcode = cReturnorderLine.currentReturnOrderLine.lineBarcodesObl.get(0);
        }

        //Open the line, so we can edit it
        this.mShowArticleDetailActivity();

    }

    private void mAddERPArticle(cBarcodeScan pvBarcodeScan){

        //Add the barcodeStr via the webservice
        if (!cReturnorder.currentReturnOrder.pAddERPBarcodeBln(pvBarcodeScan)) {
            this.mStepFailed(cAppExtension.activity.getString(R.string.message_adding_erp_article_failed, pvBarcodeScan.getBarcodeOriginalStr()));
            ReturnorderDocumentActivity.busyBln = false;
            return;
        }

        cReturnorderLine.currentReturnOrderLine.pAddLineBarcode(cReturnorderBarcode.currentReturnOrderBarcode.getBarcodeStr(), cReturnorderBarcode.currentReturnOrderBarcode.getQuantityHandledDbl());

        cReturnorderLine.currentReturnOrderLine.quantityHandledTakeDbl += cReturnorderBarcode.currentReturnOrderBarcode.getQuantityPerUnitOfMeasureDbl();
        cReturnorderLineBarcode.currentreturnorderLineBarcode.quantityHandledDbl += cReturnorderBarcode.currentReturnOrderBarcode.getQuantityPerUnitOfMeasureDbl();

        //Open the line, so we can edit it
        this.mShowArticleDetailActivity();
    }

    private void mHandleUnknownBarcodeScan(cBarcodeScan pvBarcodeScan) {

        // Check if we can add a line
        if (! cReturnorder.currentReturnOrder.isGeneratedBln()) {
            this.mStepFailed(cAppExtension.activity.getString(R.string.message_add_article_now_allowed));
            ReturnorderDocumentActivity.busyBln = false;
            return;
        }

        //We can add a line, but we don't check with the ERP, so add line and open it
        if (! cSetting.RETOUR_BARCODE_CHECK()) {
            this.mAddUnkownArticle(pvBarcodeScan);
            ReturnorderDocumentActivity.busyBln = false;
            return;
        }

        //We can add a line, and we need to check with the ERP, so check, add and open it
        this.mAddERPArticle(pvBarcodeScan);
    }

    public void mHandleSelectedLine(){

        //Set the current barcodeStr
        cReturnorderBarcode.currentReturnOrderBarcode = cReturnorder.currentReturnOrder.pGetBarcodeForSelectedLine();

        //Create new line barcodeStr
        cReturnorderLine.currentReturnOrderLine.pAddLineBarcode(cReturnorderBarcode.currentReturnOrderBarcode.getBarcodeStr(), cReturnorderBarcode.currentReturnOrderBarcode.getQuantityHandledDbl());

        //Set current reason to reason of the line
        cBranchReason branchReason = cUser.currentUser.currentBranch.pGetReasonByName(cReturnorderLine.currentReturnOrderLine.getRetourRedenStr());
        if (branchReason != null) {
            cBranchReason.currentBranchReason = branchReason;
        }

        //Open the line (found or created), so we can edit it
        this.mShowArticleDetailActivity();
    }

    private void mHandleKnownBarcodeScan(cReturnorderBarcode pvReturnorderBarcode, cBarcodeScan pvBarcodeScan) {

        //Set the current barcodeStr
        cReturnorderBarcode.currentReturnOrderBarcode = pvReturnorderBarcode;

        //Check if this barcodeStr belongs to this Document
        cReturnorderLine.currentReturnOrderLine = cReturnorder.currentReturnOrder.pGetLineForArticle();

        //Line doesn't belong to this Document and we are not allowed to add lines
        if (cReturnorderLine.currentReturnOrderLine == null )  {

            if (cSetting.RETOUR_BARCODE_CHECK()) {
            if (!cReturnorder.currentReturnOrder.isGeneratedBln()) {
                this.mStepFailed(cAppExtension.activity.getString(R.string.message_add_article_now_allowed));
                ReturnorderDocumentActivity.busyBln = false;
                return;
            }

            //Get article info via the web service
            cReturnorderLine returnorderLine = new cReturnorderLine(cArticle.currentArticle);
            returnorderLine.pInsertInDatabaseBln();
            returnorderLine.nieuweRegelBln = true;
            cReturnorderLine.currentReturnOrderLine = returnorderLine;

                if (!cReturnorder.currentReturnOrder.pAddERPBarcodeBln(pvBarcodeScan)) {
                    this.mStepFailed(cAppExtension.activity.getString(R.string.message_adding_erp_article_failed, pvBarcodeScan.getBarcodeOriginalStr()));
                    ReturnorderDocumentActivity.busyBln = false;
                    return;
                }
            }
            else {
                this.mHandleUnknownBarcodeScan(pvBarcodeScan);
                return;
            }

        }

        //Create new line barcodeStr
        cReturnorderLine.currentReturnOrderLine.pAddLineBarcode(cReturnorderBarcode.currentReturnOrderBarcode.getBarcodeStr(), cReturnorderBarcode.currentReturnOrderBarcode.getQuantityHandledDbl());

        //Add quantityDbl of the current barcodeStr
        if (cReturnorderLine.currentReturnOrderLine.getQuantitytakeDbl() == 0.0){
            cReturnorderLine.currentReturnOrderLine.quantityHandledTakeDbl += cReturnorderBarcode.currentReturnOrderBarcode.getQuantityPerUnitOfMeasureDbl();
            cReturnorderLineBarcode.currentreturnorderLineBarcode.quantityHandledDbl += cReturnorderBarcode.currentReturnOrderBarcode.getQuantityPerUnitOfMeasureDbl();
        }
        else{
            if (cReturnorderLine.currentReturnOrderLine.quantityHandledTakeDbl < cReturnorderLine.currentReturnOrderLine.quantitytakeDbl){
                cReturnorderLine.currentReturnOrderLine.quantityHandledTakeDbl += cReturnorderBarcode.currentReturnOrderBarcode.getQuantityPerUnitOfMeasureDbl();
                cReturnorderLineBarcode.currentreturnorderLineBarcode.quantityHandledDbl += cReturnorderBarcode.currentReturnOrderBarcode.getQuantityPerUnitOfMeasureDbl();
            }
        }

        //Open the line (found or created), so we can edit it
        this.mShowArticleDetailActivity();
    }

    private  void mShowArticleDetailActivity() {


        cUserInterface.pCheckAndCloseOpenDialogs();

        Intent intent = new Intent(cAppExtension.context, ReturnArticleDetailActivity.class);

        startActivity(intent);
        finish();


    }

    private void mStepFailed(String pvErrorMessageStr ){
        cUserInterface.pDoExplodingScreen(pvErrorMessageStr, cReturnorder.currentReturnOrder.getOrderNumberStr(), true, true );
        cUserInterface.pCheckAndCloseOpenDialogs();
    }

    private void mShowNoLinesIcon(final Boolean pvShowBln){

        cAppExtension.activity.runOnUiThread(() -> {

            cUserInterface.pHideGettingData();

            mSetToolBarTitleWithCounters();
            imageDocumentDone.setVisibility(View.INVISIBLE);

            if (pvShowBln) {

                recyclerViewReturnorderLines.setVisibility(View.INVISIBLE);

                FragmentTransaction fragmentTransaction = cAppExtension.fragmentManager.beginTransaction();
                NothingHereFragment fragment = new NothingHereFragment();
                fragmentTransaction.replace(R.id.returnorderContainer, fragment);
                fragmentTransaction.commit();
                return;
            }

            recyclerViewReturnorderLines.setVisibility(View.VISIBLE);
            imageDocumentDone.setVisibility(View.VISIBLE);

            List<Fragment> fragments = cAppExtension.fragmentManager.getFragments();
            for (Fragment fragment : fragments) {
                if (fragment instanceof NothingHereFragment) {
                    FragmentTransaction fragmentTransaction = cAppExtension.fragmentManager.beginTransaction();
                    fragmentTransaction.remove(fragment);
                    fragmentTransaction.commit();
                }
            }
        });
    }

    private void mSetToolBarTitleWithCounters(){
        this.toolbarSubTitle.setText(cReturnorder.currentReturnOrder.getCountForCurrentSourceDocumentStr() + " " +  cAppExtension.activity.getString(R.string.items));
    }

    private void mShowCloseDocumentDialog() {

        cUserInterface.pCheckAndCloseOpenDialogs();

        final AcceptRejectFragment acceptRejectFragment;// show my popup
        if (!cReturnorder.currentReturnOrder.getRetourMultiDocumentBln()){
            acceptRejectFragment = new AcceptRejectFragment(cAppExtension.activity.getString(R.string.message_close_order),
                    cAppExtension.activity.getString(R.string.message_close_order_text), cAppExtension.activity.getString(R.string.message_cancel), cAppExtension.activity.getString(R.string.message_close), false);
        }
        else {
            acceptRejectFragment = new AcceptRejectFragment(cAppExtension.activity.getString(R.string.message_close_document),
                    cAppExtension.activity.getString(R.string.message_document_text_sure), cAppExtension.activity.getString(R.string.message_cancel), cAppExtension.activity.getString(R.string.message_close), false);
        }
        acceptRejectFragment.setCancelable(true);
        runOnUiThread(() -> {
            // show my popup
            acceptRejectFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.ACCEPTREJECTFRAGMENT_TAG);
        });
    }

    private  void mShowCurrentLocationFragment() {

        cUserInterface.pCheckAndCloseOpenDialogs();
        cUserInterface.pPlaySound(R.raw.goodsound, null);

        final CurrentLocationFragment currentLocationFragment = new CurrentLocationFragment();
        currentLocationFragment.setCancelable(true);
        currentLocationFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.CURRENTLOCATION_TAG);
    }

    private void mSetAddArticleListener() {
        this.imageAddArticle.setOnClickListener(view -> mShowAddArticleFragment());
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder pvViewHolder, int pvDirectionInt, int pvPositionInt) {

        if (!(pvViewHolder instanceof cReturnorderLineAdapter.ReturnorderLineViewHolder)) {
            return;
        }

        cReturnorderLine.currentReturnOrderLine = cReturnorderDocument.currentReturnOrderDocument.returnorderLineObl.get(pvPositionInt);

        //Remove the enviroment
        if (cReturnorder.currentReturnOrder.isGeneratedBln()){
            this.mRemoveAdapterFromFragment();
        }
        else {
            this.mResetLineToZero();
        }


    }

    private void mRemoveAdapterFromFragment(){

        //remove the item from recyclerview
        cResult hulpRst = cReturnorderLine.currentReturnOrderLine.pResetRst();
        if (! hulpRst.resultBln) {
            cUserInterface.pDoExplodingScreen(hulpRst.messagesStr(),"",true,true);
            return;
        }

        //Renew data, so only current lines are shown
        this.pLineHandled();

    }

    private void mResetLineToZero () {

        //Set the current barcodeStr
        cReturnorderBarcode.currentReturnOrderBarcode = cReturnorder.currentReturnOrder.pGetBarcodeForSelectedLine();

        //Create new line barcodeStr
        cReturnorderLine.currentReturnOrderLine.pAddLineBarcode(cReturnorderBarcode.currentReturnOrderBarcode.getBarcodeStr(), -(cReturnorderLine.currentReturnOrderLine.getQuantityHandledTakeDbl()));
        cReturnorderLine.currentReturnOrderLine.quantityHandledTakeDbl = 0.0;

        if (!cReturnorderLine.currentReturnOrderLine.pSaveLineViaWebserviceBln()) {
            cUserInterface.pDoExplodingScreen(cAppExtension.activity.getString(R.string.message_line_save_failed),"",true,true);
            this.pHandleFragmentDismissed();
            cAppExtension.dialogFragment.dismiss();
            return;
        }
        //Change quantityDbl handled in database
        cReturnorderLine.currentReturnOrderLine.pUpdateQuantityInDatabase();
        //Renew data, so only current lines are shown
        this.pLineHandled();
    }

    private void mShowAddArticleFragment(){

        cUserInterface.pShowToastMessage(cReturnorderDocument.currentReturnOrderDocument.getSourceDocumentStr(),null);

        AddArticleFragment addArticleFragment = new AddArticleFragment();
        addArticleFragment.setCancelable(true);
        addArticleFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.ADDARTICLE_TAG);
    }

    private void mTryToLeaveSingleDocumentActivity() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(cAppExtension.context);
        alertDialog.setTitle(R.string.message_sure_leave_screen_title);
        alertDialog.setMessage(getString(R.string.message_sure_leave_screen_text));
        alertDialog.setPositiveButton(R.string.message_sure_leave_screen_positive, (pvDialogInterface, i) -> {

            if (!cReturnorder.currentReturnOrder.pLockReleaseViaWebserviceBln(cWarehouseorder.StepCodeEnu.Retour, cWarehouseorder.WorkflowReturnStepEnu.Return)) {
                cUserInterface.pDoExplodingScreen(getString(R.string.error_couldnt_release_lock_order), "", true, true);
                return;
            }
            mStartOrderSelectActivity();
        });
        alertDialog.setNeutralButton(R.string.cancel, (dialogInterface, i) -> {
            //do nothing (close the dialog)
        });

        alertDialog.setCancelable(true);
        alertDialog.show();
    }

    private  void mStartOrderSelectActivity() {
        Intent intent = new Intent(cAppExtension.context, ReturnorderSelectActivity.class);
        startActivity(intent);
    }

    private void mShowSending() {
        final SendingFragment sendingFragment = new SendingFragment();
        sendingFragment.setCancelable(true);
        cAppExtension.activity.runOnUiThread(() -> {
            // show my popup
            sendingFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.SENDING_TAG);
        });
    }

    private void mShowSent() {

        Fragment fragment = cAppExtension.fragmentManager.findFragmentByTag(cPublicDefinitions.SENDING_TAG);
        if (fragment != null) {
            if (fragment instanceof SendingFragment) {
                ((SendingFragment) fragment).pShowFlyAwayAnimation();
            }
        }


        if (FinishShipLinesActivity.currentLineFragment instanceof SinglePieceLinesToShipFragment) {
            SinglePieceLinesToShipFragment singlePieceLinesToShipFragment = (SinglePieceLinesToShipFragment) FinishShipLinesActivity.currentLineFragment;
            singlePieceLinesToShipFragment.pGetData();
            mInitScreen();
        }

    }

    private void mShowShipmentNotSent(String pvErrorMessageStr) {
        Fragment fragment = cAppExtension.fragmentManager.findFragmentByTag(cPublicDefinitions.SENDING_TAG);
        if (fragment != null) {
            if (fragment instanceof SendingFragment) {
                ((SendingFragment) fragment).pShowCrashAnimation(pvErrorMessageStr);
            }
        }


    }

    //End Region Private Methods

}
