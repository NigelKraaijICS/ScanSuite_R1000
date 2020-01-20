package nl.icsvertex.scansuite.Activities.Returns;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import SSU_WHS.Return.ReturnorderBarcode.cReturnorderBarcode;
import SSU_WHS.Return.ReturnorderDocument.cReturnorderDocument;
import SSU_WHS.Return.ReturnorderLine.cReturnorderLine;
import SSU_WHS.Return.ReturnorderLine.cReturnorderLineAdapter;
import SSU_WHS.Return.ReturnorderLine.cReturnorderLineRecyclerItemTouchHelper;
import SSU_WHS.Return.ReturnorderLineBarcode.cReturnorderLineBarcode;
import nl.icsvertex.scansuite.Fragments.Dialogs.AcceptRejectFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.AddArticleFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.NoOrdersFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.NothingHereFragment;
import nl.icsvertex.scansuite.Fragments.Returns.ReturnArticleDetailFragment;
import nl.icsvertex.scansuite.R;

public class ReturnorderDocumentActivity extends AppCompatActivity implements iICSDefaultActivity, cReturnorderLineRecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    //Region Public Properties
    static final String ACCEPTREJECTFRAGMENT_TAG = "ACCEPTREJECTFRAGMENT_TAG";
    public static String VIEW_CHOSEN_DOCUMENT = "detail:header:text";
    public static final String VIEW_CHOSEN_DOCUMENT_IMAGE = "detail:header:imageStr";
    public static Fragment currentLineFragment;
    public static Boolean busyBln =false;


    //End Region Public Properties

    //Region Private Properties
    private static TextView documentText;
    private static ImageView imageDocument;
    private static ImageView toolbarImage;
    private static ImageView imageDocumentDone;
    private static TextView toolbarTitle;
    private static TextView toolbarSubTitle;
    private static RecyclerView recyclerViewReturnorderLines;
    private static ImageView imageAddArticle;

    //End Region Private Properties

    //Region Default Methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_returnorder_document);
        this.mActivityInitialize();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cBarcodeScan.pRegisterBarcodeReceiver();
    }

    @Override
    protected void onPause() {
        super.onPause();
        cBarcodeScan.pUnregisterBarcodeReceiver();
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

        cBarcodeScan.pRegisterBarcodeReceiver();
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
        ReturnorderDocumentActivity.toolbarImage = findViewById(R.id.toolbarImage);
        ReturnorderDocumentActivity.toolbarTitle = findViewById(R.id.toolbarTitle);
        ReturnorderDocumentActivity.toolbarSubTitle = findViewById(R.id.toolbarSubtext);
        ReturnorderDocumentActivity.documentText = findViewById(R.id.binText);
        ReturnorderDocumentActivity.imageDocument = findViewById(R.id.imageBin);
        ReturnorderDocumentActivity.imageDocumentDone = findViewById(R.id.imageViewDocumentDone);
        ReturnorderDocumentActivity.recyclerViewReturnorderLines = findViewById(R.id.recyclerViewReturnorderLines);
        ReturnorderDocumentActivity.imageAddArticle = findViewById(R.id.imageAddArticle);
    }

    @Override
    public void mSetToolbar(String pvScreenTitleStr) {
        ReturnorderDocumentActivity.toolbarImage.setImageResource(R.drawable.ic_menu_return);
        ReturnorderDocumentActivity.toolbarTitle.setText(pvScreenTitleStr);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void mFieldsInitialize() {
        ViewCompat.setTransitionName(ReturnorderDocumentActivity.imageDocument, ReturnorderDocumentActivity.VIEW_CHOSEN_DOCUMENT_IMAGE);
        ViewCompat.setTransitionName(ReturnorderDocumentActivity.documentText, ReturnorderDocumentActivity.VIEW_CHOSEN_DOCUMENT);
        ReturnorderDocumentActivity.documentText.setText(cReturnorderDocument.currentReturnOrderDocument.getSourceDocumentStr());


        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new cReturnorderLineRecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(ReturnorderDocumentActivity.recyclerViewReturnorderLines);

    }

    @Override
    public void mSetListeners() {
        this.mSetToolbarTitleListeners();
        this.mSetAddArticleListener();
        this.mSetDoneListeners();
    }

    @Override
    public void mInitScreen() {
        ReturnorderDocumentActivity.pFillLines();
    }

    //End Region iICSDefaultActivity defaults

    //Region Public Methods

    public static void pHandleScan(final cBarcodeScan pvBarcodeScan) {

        if (ReturnorderDocumentActivity.busyBln) {
            return;
        }

        //Close open Dialogs
        cUserInterface.pCheckAndCloseOpenDialogs();

        // Show that we are getting data and set busy boolean
        ReturnorderDocumentActivity.busyBln = true;
        cUserInterface.pShowGettingData();

        new Thread(new Runnable() {
            public void run() {
                mHandleScan(pvBarcodeScan);
            }
        }).start();

    }

    public static void pHandleSelectedLine(){
        //Close open Dialogs
        cUserInterface.pCheckAndCloseOpenDialogs();

        // Show that we are getting data and set busy boolean
        ReturnorderDocumentActivity.busyBln = true;
        cUserInterface.pShowGettingData();

        mHandleSelectedLine();
    }

    public static void pFillLines() {

        List<cReturnorderLine> hulpObl = cReturnorderDocument.currentReturnOrderDocument.returnorderLineObl;
        if (hulpObl.size() == 0) {
            ReturnorderDocumentActivity.mShowNoLinesIcon(true);
            return;
        }

        ReturnorderDocumentActivity.mFillRecyclerView(hulpObl);
        ReturnorderDocumentActivity.mShowNoLinesIcon(false);

    }

    public static void pLineHandled() {

        //We returned here, so we are not busy anymore
        ReturnorderDocumentActivity.busyBln = false;

        //Reset currents
        cReturnorderLine.currentReturnOrderLine = null;
        cReturnorderBarcode.currentReturnOrderBarcode = null;
        cReturnorderLineBarcode.currentreturnorderLineBarcode = null;

        //Fill lines of Document
        ReturnorderDocumentActivity.pFillLines();
    }

    public  static void pCloseDocument(){


        boolean resultBln = cReturnorderDocument.currentReturnOrderDocument.pCloseBln();

        //Something went wrong
        if (! resultBln) {
            cUserInterface.pDoExplodingScreen(cAppExtension.activity.getString(R.string.message_document_close_fail),cReturnorderDocument.currentReturnOrderDocument.getSourceDocumentStr(),true,true);
            return;
        }

        //Clear cache
        cReturnorderDocument.currentReturnOrderDocument = null;

        //Start Documents activity
        ReturnorderDocumentActivity.mStartDocumentsActivity();

    }

    public static void pHandleFragmentDismissed(){
        cBarcodeScan.pRegisterBarcodeReceiver();
    }

    //End Region Public Methods

    //Region Private Methods



    private static void mHandleScan(cBarcodeScan pvBarcodeScan){


        if (cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(),cBarcodeLayout.barcodeLayoutEnu.DOCUMENT)) {

            //We scanned a NEW Document so check of we are allowed to add a Document
            if (cReturnorder.currentReturnOrder.isGeneratedBln() && !cReturnorder.currentReturnOrder.getRetourMultiDocumentBln() ) {
                ReturnorderDocumentActivity.mStepFailed(cAppExtension.activity.getString(R.string.message_document_add_not_allowed));
                ReturnorderDocumentActivity.busyBln = false;
               return;
            }

            //Close current Document
            ReturnorderDocumentActivity.pCloseDocument();

            //We are not busy anymore
            ReturnorderDocumentActivity.busyBln = false;

            //Pass this new Document scan on to the Documents activity
            ReturnorderDocumentsActivity.pHandleScan(pvBarcodeScan);
            return;
        }



        for ( cBarcodeLayout barcodeLayout :  cBarcodeLayout.allBarcodeLayoutsObl) {
            if (barcodeLayout.getBarcodeLayoutEnu() != cBarcodeLayout.barcodeLayoutEnu.ARTICLE){
                if (cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(), barcodeLayout.getBarcodeLayoutEnu())) {
                    mDoUnknownScan(cAppExtension.context.getString(R.string.error_article_scan_mandatory), pvBarcodeScan.getBarcodeOriginalStr());
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
            ReturnorderDocumentActivity.mHandleUnknownBarcodeScan(pvBarcodeScan);
            return;
        }

        cArticle.currentArticle  = cArticle.pGetArticleByBarcodeViaWebservice(pvBarcodeScan);

        //We scanned a barcodeStr we already know
        ReturnorderDocumentActivity.mHandleKnownBarcodeScan(returnorderBarcode, pvBarcodeScan);

    }

    private static void mDoUnknownScan(String pvErrorMessageStr, String pvScannedBarcodeStr) {
        cUserInterface.pDoExplodingScreen(pvErrorMessageStr, pvScannedBarcodeStr, true, true);
    }

    private void mTryToLeaveActivity() {

        if (cReturnorder.currentReturnOrder.linesObl().size() > 0) {
            mShowCloseDocumentDialog();
            return;
        }

        ReturnorderDocumentActivity.pCloseDocument();
    }

    private static void mFillRecyclerView(List<cReturnorderLine> pvReturnorderLinesObl) {

        if (pvReturnorderLinesObl == null || pvReturnorderLinesObl.size() == 0) {
            return;
        }

        for (Fragment fragment: cAppExtension.fragmentManager.getFragments()) {
            if (fragment instanceof NoOrdersFragment) {
                cAppExtension.fragmentManager.beginTransaction().remove(fragment).commit();
            }
        }

        ReturnorderDocumentActivity.recyclerViewReturnorderLines.setHasFixedSize(false);
        ReturnorderDocumentActivity.recyclerViewReturnorderLines.setAdapter(cReturnorderLine.getReturnorderLineAdapter());
        ReturnorderDocumentActivity.recyclerViewReturnorderLines.setLayoutManager(new LinearLayoutManager(cAppExtension.context));

        cReturnorderLine.getReturnorderLineAdapter().pFillData(pvReturnorderLinesObl);
    }

    private void mSetToolbarTitleListeners() {
        ReturnorderDocumentActivity.toolbarTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mScrollToTop();
            }
        });
        ReturnorderDocumentActivity.toolbarTitle.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mScrollToBottom();
                return true;
            }
        });
    }

    private void mSetDoneListeners() {

        ReturnorderDocumentActivity.imageDocumentDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTryToLeaveActivity();
            }
        });

    }

    private void mScrollToTop() {
        recyclerViewReturnorderLines.smoothScrollToPosition(0);
    }

    private void mScrollToBottom() {
        if (cReturnorder.getReturnorderAdapter() != null) {
            if (cReturnorder.getReturnorderAdapter().getItemCount() > 0) {
                recyclerViewReturnorderLines.smoothScrollToPosition(cReturnorderLine.getReturnorderLineAdapter().getItemCount() - 1);
            }
        }
    }

    private static void mStartDocumentsActivity() {
        Intent intent = new Intent(cAppExtension.context, ReturnorderDocumentsActivity.class);
        cAppExtension.activity.startActivity(intent);
        cAppExtension.activity.finish();
    }

    private static void mAddUnkownArticle(cBarcodeScan pvBarcodeScan){

        //Add the barcodeStr via the webservice
        if (!cReturnorder.currentReturnOrder.pAddUnkownBarcodeBln(pvBarcodeScan)) {
            cUserInterface.pDoExplodingScreen(cAppExtension.activity.getString(R.string.message_adding_unkown_article_failed),"",true,true);
            ReturnorderDocumentActivity.busyBln = false;
            return;
        }

        //Add quantityDbl of the current barcodeStr
        cReturnorderLine.currentReturnOrderLine.quantitytakeDbl += cReturnorderBarcode.currentReturnOrderBarcode.getQuantityPerUnitOfMeasureDbl();
        cReturnorderBarcode.currentReturnOrderBarcode.quantityHandled += cReturnorderBarcode.currentReturnOrderBarcode.getQuantityPerUnitOfMeasureDbl();

        //Make line barcodeStr the current line barcodeStr
        if (cReturnorderLine.currentReturnOrderLine.lineBarcodesObl != null) {
            cReturnorderLineBarcode.currentreturnorderLineBarcode = cReturnorderLine.currentReturnOrderLine.lineBarcodesObl.get(0);
        }

        //Open the line, so we can edit it
        ReturnorderDocumentActivity.mShowArticleDetailFragment();

    }

    private static void mAddERPArticle(cBarcodeScan pvBarcodeScan){

        //Add the barcodeStr via the webservice
        if (!cReturnorder.currentReturnOrder.pAddERPBarcodeBln(pvBarcodeScan)) {
            ReturnorderDocumentActivity.mStepFailed(cAppExtension.activity.getString(R.string.message_adding_erp_article_failed));
            ReturnorderDocumentActivity.busyBln = false;
            return;
        }


        cReturnorderLine.currentReturnOrderLine.pAddLineBarcode(cReturnorderBarcode.currentReturnOrderBarcode.getBarcodeStr(), cReturnorderBarcode.currentReturnOrderBarcode.getQuantityHandled());

        cReturnorderLine.currentReturnOrderLine.quantityHandledTakeDbl += cReturnorderBarcode.currentReturnOrderBarcode.getQuantityPerUnitOfMeasureDbl();
        cReturnorderLineBarcode.currentreturnorderLineBarcode.quantityHandledDbl += cReturnorderBarcode.currentReturnOrderBarcode.getQuantityPerUnitOfMeasureDbl();

        //Open the line, so we can edit it
        ReturnorderDocumentActivity.mShowArticleDetailFragment();
    }

    private static void mHandleUnknownBarcodeScan(cBarcodeScan pvBarcodeScan) {

        // Check if we can add a line
        if (! cReturnorder.currentReturnOrder.isGeneratedBln()) {
            ReturnorderDocumentActivity.mStepFailed(cAppExtension.activity.getString(R.string.message_add_article_now_allowed));
            ReturnorderDocumentActivity.busyBln = false;
            return;
        }

        //We can add a line, but we don't check with the ERP, so add line and open it
        if (! cSetting.RETOUR_BARCODE_CHECK()) {
            ReturnorderDocumentActivity.mAddUnkownArticle(pvBarcodeScan);
            ReturnorderDocumentActivity.busyBln = false;
            return;
        }

        //We can add a line, and we need to check with the ERP, so check, add and open it
        ReturnorderDocumentActivity.mAddERPArticle(pvBarcodeScan);
    }

    public static void mHandleSelectedLine(){

        //Set the current barcodeStr
        cReturnorderBarcode.currentReturnOrderBarcode = cReturnorder.currentReturnOrder.pGetBarcodeForSelectedLine();

        //Create new line barcodeStr
        cReturnorderLine.currentReturnOrderLine.pAddLineBarcode(cReturnorderBarcode.currentReturnOrderBarcode.getBarcodeStr(), cReturnorderBarcode.currentReturnOrderBarcode.getQuantityHandled());

        //Set current reason to reason of the line
        cBranchReason branchReason = cUser.currentUser.currentBranch.pGetReasonByName(cReturnorderLine.currentReturnOrderLine.getRetourredenStr());
        if (branchReason != null) {
            cBranchReason.currentBranchReason = branchReason;
        }

        //Open the line (found or created), so we can edit it
        ReturnorderDocumentActivity.mShowArticleDetailFragment();
    }

    private static void mHandleKnownBarcodeScan(cReturnorderBarcode pvReturnorderBarcode, cBarcodeScan pvBarcodeScan) {

        //Set the current barcodeStr
        cReturnorderBarcode.currentReturnOrderBarcode = pvReturnorderBarcode;

        //Check if this barcodeStr belongs to this Document
        cReturnorderLine.currentReturnOrderLine = cReturnorder.currentReturnOrder.pGetLineForArticle();

        //Line doesn't belong to this Document and we are not allowed to add lines
        if (cReturnorderLine.currentReturnOrderLine == null )  {

            if (!cReturnorder.currentReturnOrder.isGeneratedBln()) {
                ReturnorderDocumentActivity.mStepFailed(cAppExtension.activity.getString(R.string.message_add_article_now_allowed));
                ReturnorderDocumentActivity.busyBln = false;
                return;
            }

            //Get article info via the web service
            cReturnorderLine returnorderLine = new cReturnorderLine(cArticle.currentArticle);
            returnorderLine.pInsertInDatabaseBln();
            returnorderLine.nieuweRegelBln = true;
            cReturnorderLine.currentReturnOrderLine = returnorderLine;

            if (!cReturnorder.currentReturnOrder.pAddERPBarcodeBln(pvBarcodeScan)) {
                ReturnorderDocumentActivity.mStepFailed(cAppExtension.activity.getString(R.string.message_adding_erp_article_failed));
                ReturnorderDocumentActivity.busyBln = false;
                return;
            }
        }

        //Create new line barcodeStr
        cReturnorderLine.currentReturnOrderLine.pAddLineBarcode(cReturnorderBarcode.currentReturnOrderBarcode.getBarcodeStr(), cReturnorderBarcode.currentReturnOrderBarcode.getQuantityHandled());

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
        ReturnorderDocumentActivity.mShowArticleDetailFragment();
    }

    private static void mShowArticleDetailFragment() {

        ReturnArticleDetailFragment articleDetailFragment = new ReturnArticleDetailFragment();
        articleDetailFragment.setCancelable(false);
        articleDetailFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.ARTICLEDETAILFRAGMENT_TAG);

    }

    private static void mStepFailed(String pvErrorMessageStr ){
        cUserInterface.pDoExplodingScreen(pvErrorMessageStr, cReturnorder.currentReturnOrder.getOrderNumberStr(), true, true );
        cUserInterface.pCheckAndCloseOpenDialogs();
    }

    private static void mShowNoLinesIcon(final Boolean pvShowBln){

        cAppExtension.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                cUserInterface.pHideGettingData();

                ReturnorderDocumentActivity.mSetToolBarTitleWithCounters();
                ReturnorderDocumentActivity.imageDocumentDone.setVisibility(View.INVISIBLE);

                if (pvShowBln) {

                    ReturnorderDocumentActivity.recyclerViewReturnorderLines.setVisibility(View.INVISIBLE);

                    FragmentTransaction fragmentTransaction = cAppExtension.fragmentManager.beginTransaction();
                    NothingHereFragment fragment = new NothingHereFragment();
                    fragmentTransaction.replace(R.id.returnorderContainer, fragment);
                    fragmentTransaction.commit();
                    return;
                }

                ReturnorderDocumentActivity.recyclerViewReturnorderLines.setVisibility(View.VISIBLE);
                ReturnorderDocumentActivity.imageDocumentDone.setVisibility(View.VISIBLE);

                List<Fragment> fragments = cAppExtension.fragmentManager.getFragments();
                for (Fragment fragment : fragments) {
                    if (fragment instanceof NothingHereFragment) {
                        FragmentTransaction fragmentTransaction = cAppExtension.fragmentManager.beginTransaction();
                        fragmentTransaction.remove(fragment);
                        fragmentTransaction.commit();
                    }
                }
            }
        });
    }

    private static void mSetToolBarTitleWithCounters(){
        ReturnorderDocumentActivity.toolbarSubTitle.setText(cReturnorder.currentReturnOrder.getCountForCurrentSourceDocumentStr());
    }

    private void mShowCloseDocumentDialog() {

        cUserInterface.pCheckAndCloseOpenDialogs();

        if (!cReturnorder.currentReturnOrder.getRetourMultiDocumentBln()){
            final AcceptRejectFragment acceptRejectFragment = new AcceptRejectFragment(cAppExtension.activity.getString(R.string.message_close_order),
                    cAppExtension.activity.getString(R.string.message_close_order_text), cAppExtension.activity.getString(R.string.message_cancel), cAppExtension.activity.getString(R.string.message_close),false);
            acceptRejectFragment.setCancelable(true);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // show my popup
                    acceptRejectFragment.show(cAppExtension.fragmentManager, ACCEPTREJECTFRAGMENT_TAG);
                }
            });
        }
        else {
            final AcceptRejectFragment acceptRejectFragment = new AcceptRejectFragment(cAppExtension.activity.getString(R.string.message_close_document),
                    cAppExtension.activity.getString(R.string.message_document_text_sure), cAppExtension.activity.getString(R.string.message_cancel),cAppExtension.activity.getString(R.string.message_close),false);
            acceptRejectFragment.setCancelable(true);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // show my popup
                    acceptRejectFragment.show(cAppExtension.fragmentManager, ACCEPTREJECTFRAGMENT_TAG);
                }
            });
        }
      }


    private void mSetAddArticleListener() {
        ReturnorderDocumentActivity.imageAddArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mShowAddArticleFragment();
            }
        });
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
        ReturnorderDocumentActivity.pLineHandled();

    }

    private void mResetLineToZero () {

        //Set the current barcodeStr
        cReturnorderBarcode.currentReturnOrderBarcode = cReturnorder.currentReturnOrder.pGetBarcodeForSelectedLine();

        //Create new line barcodeStr
        cReturnorderLine.currentReturnOrderLine.pAddLineBarcode(cReturnorderBarcode.currentReturnOrderBarcode.getBarcodeStr(), -(cReturnorderLine.currentReturnOrderLine.getQuantityHandledTakeDbl()));
        cReturnorderLine.currentReturnOrderLine.quantityHandledTakeDbl = 0.0;

        if (!cReturnorderLine.currentReturnOrderLine.pSaveLineViaWebserviceBln()) {
            cUserInterface.pDoExplodingScreen(cAppExtension.activity.getString(R.string.message_line_save_failed),"",true,true);
            ReturnorderDocumentActivity.pHandleFragmentDismissed();
            cAppExtension.dialogFragment.dismiss();
            return;
        }
        //Change quantityDbl handled in database
        cReturnorderLine.currentReturnOrderLine.pUpdateQuantityInDatabase();
        //Renew data, so only current lines are shown
        ReturnorderDocumentActivity.pLineHandled();
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
        alertDialog.setPositiveButton(R.string.message_sure_leave_screen_positive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface pvDialogInterface, int i) {

                if (!cReturnorder.currentReturnOrder.pLockReleaseViaWebserviceBln(cWarehouseorder.StepCodeEnu.Retour, cWarehouseorder.WorkflowReturnStepEnu.Return)) {
                    cUserInterface.pDoExplodingScreen(getString(R.string.error_couldnt_release_lock_order), "", true, true);
                    return;
                }
                mStartOrderSelectActivity();
            }
        });
        alertDialog.setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //do nothing (close the dialog)
            }
        });

        alertDialog.setCancelable(true);
        alertDialog.show();
    }
    private static void mStartOrderSelectActivity() {
        Intent intent = new Intent(cAppExtension.context, ReturnorderSelectActivity.class);
        cAppExtension.activity.startActivity(intent);
    }

    //End Region Private Methods

}
