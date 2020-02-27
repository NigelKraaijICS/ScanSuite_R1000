package nl.icsvertex.scansuite.Activities.Inventory;

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
import SSU_WHS.Inventory.InventoryOrders.cInventoryorder;
import SSU_WHS.Inventory.InventoryorderBarcodes.cInventoryorderBarcode;
import SSU_WHS.Inventory.InventoryorderBins.cInventoryorderBin;
import SSU_WHS.Inventory.InventoryorderLineBarcodes.cInventoryorderLineBarcode;
import SSU_WHS.Inventory.InventoryorderLines.cInventoryorderLine;
import SSU_WHS.Inventory.InventoryorderLines.cInventoryorderLineAdapter;
import SSU_WHS.Inventory.InventoryorderLines.cInventoryorderLineRecyclerItemTouchHelper;
import nl.icsvertex.scansuite.Fragments.Dialogs.AcceptRejectFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.AddArticleFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.NoOrdersFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.NothingHereFragment;
import nl.icsvertex.scansuite.Fragments.Inventory.InventoryArticleDetailFragment;
import nl.icsvertex.scansuite.R;

public class InventoryorderBinActivity extends AppCompatActivity implements iICSDefaultActivity,cInventoryorderLineRecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    //Region Public Properties
    static final String ACCEPTREJECTFRAGMENT_TAG = "ACCEPTREJECTFRAGMENT_TAG";
    public static String VIEW_CHOSEN_BIN = "detail:header:text";
    public static final String VIEW_CHOSEN_BIN_IMAGE = "detail:header:imageStr";
    public static Fragment currentLineFragment;
    public static Boolean busyBln =false;


    //End Region Public Properties

    //Region Private Properties
    private static TextView binText;
    private static ImageView imageBin;
    private static ImageView toolbarImage;
    private static ImageView imageBinDone;
    private static TextView toolbarTitle;
    private static TextView toolbarSubTitle;
    private static RecyclerView recyclerViewInventoryorderLines;

    private static ImageView imageAddArticle;

    public static int positionSwiped;

    //End Region Private Properties

    //Region Default Methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventoryorder_bin);
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
        cUserInterface.pEnableScanner();
    }

    @Override
    protected void onPause() {
        super.onPause();
        cBarcodeScan.pUnregisterBarcodeReceiver();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem pvMenuItem) {

        if (pvMenuItem.getItemId() == android.R.id.home) {
            this.mTryToLeaveActivity(true);
            return true;
        }

        return super.onOptionsItemSelected(pvMenuItem);
    }

    @Override
    public void onBackPressed() {
        this.mTryToLeaveActivity(true);
    }

    //End Region Default Methods

    //Region iICSDefaultActivity defaults

    @Override
    public void mActivityInitialize() {

        this.mSetAppExtensions();

        this.mFindViews();

        this.mSetToolbar(getResources().getString(R.string.screentitle_inventorybincount));

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
        InventoryorderBinActivity.toolbarImage = findViewById(R.id.toolbarImage);
        InventoryorderBinActivity.toolbarTitle = findViewById(R.id.toolbarTitle);
        InventoryorderBinActivity.toolbarSubTitle = findViewById(R.id.toolbarSubtext);
        InventoryorderBinActivity.binText = findViewById(R.id.binText);
        InventoryorderBinActivity.imageBin = findViewById(R.id.imageTime);
        InventoryorderBinActivity.imageBinDone = findViewById(R.id.imageViewNewOrder);
        InventoryorderBinActivity.recyclerViewInventoryorderLines = findViewById(R.id.recyclerViewInventoryorderLines);
        InventoryorderBinActivity.imageAddArticle = findViewById(R.id.imageAddArticle);
    }

    @Override
    public void mSetToolbar(String pvScreenTitleStr) {
        InventoryorderBinActivity.toolbarImage.setImageResource(R.drawable.ic_menu_inventory);
        InventoryorderBinActivity.toolbarTitle.setText(pvScreenTitleStr);
        InventoryorderBinActivity.toolbarTitle.setSelected(true);
        InventoryorderBinActivity.toolbarSubTitle.setSelected(true);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void mFieldsInitialize() {
        ViewCompat.setTransitionName(InventoryorderBinActivity.imageBin, InventoryorderBinActivity.VIEW_CHOSEN_BIN_IMAGE);
        ViewCompat.setTransitionName(InventoryorderBinActivity.binText, InventoryorderBinActivity.VIEW_CHOSEN_BIN);
        InventoryorderBinActivity.binText.setText(cInventoryorderBin.currentInventoryOrderBin.getBinCodeStr());

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new cInventoryorderLineRecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(InventoryorderBinActivity.recyclerViewInventoryorderLines);

    }

    @Override
    public void mSetListeners() {
        this.mSetToolbarTitleListeners();
        this.mSetAddArticleListener();
        this.mSetDoneListeners();
    }

    @Override
    public void mInitScreen() {
        InventoryorderBinActivity.pFillLines();
    }

    //End Region iICSDefaultActivity defaults

    //Region Public Methods

    public static void pHandleScan(final cBarcodeScan pvBarcodeScan, final boolean pvIsArticleBln) {

        if (InventoryorderBinActivity.busyBln) {
            return;
        }

        //Close open Dialogs
        cUserInterface.pCheckAndCloseOpenDialogs();

        // Show that we are getting data and set busy boolean
        InventoryorderBinActivity.busyBln = true;
        cUserInterface.pShowGettingData();

        new Thread(new Runnable() {
            public void run() {
                mHandleScan(pvBarcodeScan, pvIsArticleBln);
            }
        }).start();

    }

    public static void pFillLines() {

        List<cInventoryorderLine> hulpObl = cInventoryorder.currentInventoryOrder.pGetLinesForBinObl(cInventoryorderBin.currentInventoryOrderBin.getBinCodeStr());
        if (hulpObl.size() == 0) {
            InventoryorderBinActivity.mShowNoLinesIcon(true);
            return;
        }

        InventoryorderBinActivity.mFillRecyclerView(hulpObl);
        InventoryorderBinActivity.mShowNoLinesIcon(false);

    }

    public static void pLineHandled() {

        //We returned here, so we are not busy anymore
        InventoryorderBinActivity.busyBln = false;

        //Reset currents
        cInventoryorderLine.currentInventoryOrderLine = null;
        cInventoryorderBarcode.currentInventoryOrderBarcode = null;
        cInventoryorderLineBarcode.currentInventoryorderLineBarcode = null;

        //Fill lines of BIN
        InventoryorderBinActivity.pFillLines();

    }

    public  static void pCloseBin(){


        boolean closeViaWebserviceBln = !cInventoryorder.currentInventoryOrder.isGeneratedBln();

        boolean resultBln = cInventoryorderBin.currentInventoryOrderBin.pCloseBln(closeViaWebserviceBln);

        //Something went wrong
        if (! resultBln) {
            cUserInterface.pDoExplodingScreen(cAppExtension.activity.getString(R.string.message_bin_close_failed),cInventoryorderBin.currentInventoryOrderBin.getBinCodeStr(),true,true);
            return;
        }

        //Clear cache
        cInventoryorderBin.currentInventoryOrderBin = null;

        //Start BIN activity
        InventoryorderBinActivity.mStartBinsActivity();

    }

    public static void pHandleAddArticleFragmentDismissed(){
        cBarcodeScan.pRegisterBarcodeReceiver();
    }

    public static void pAcceptRejectDialogDismissed() {
        mStartBinsActivity();
    }

    //End Region Public Methods

    //Region Private Methods

    private static void mHandleScan(cBarcodeScan pvBarcodeScan, boolean pvIsArticleBln){


        boolean binCheckedBln = false;

        //This barcode matches multiple lay-outs so this can be a BIN or an article
        if (cBarcodeLayout.pGetBarcodeLayoutByBarcodeObl(pvBarcodeScan.getBarcodeOriginalStr()).size() > 1) {

          //First check if this is a BIN
          cBranchBin branchBin =  cUser.currentUser.currentBranch.pGetBinByCode(cRegex.pStripRegexPrefixStr(pvBarcodeScan.getBarcodeOriginalStr()));

          if (branchBin != null) {
              //Close current BIN
              InventoryorderBinActivity.pCloseBin();

              //We are not busy anymore
              InventoryorderBinActivity.busyBln = false;

              //Pass this new BIN scan on to the BINS activity
              InventoryorderBinsActivity.pHandleScan(pvBarcodeScan);
              return;
          }

            binCheckedBln = true;

        }


        if (!binCheckedBln &&  !pvIsArticleBln && cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(),cBarcodeLayout.barcodeLayoutEnu.BIN) )  {

            if (!cInventoryorder.currentInventoryOrder.isGeneratedBln()) {
                mDoUnknownScan(cAppExtension.context.getString(R.string.message_bin_not_allowed),pvBarcodeScan.getBarcodeOriginalStr());
                return;
            }

            //Close current BIN
            InventoryorderBinActivity.pCloseBin();

            //We are not busy anymore
            InventoryorderBinActivity.busyBln = false;

            //Pass this new BIN scan on to the BINS activity
            InventoryorderBinsActivity.pHandleScan(pvBarcodeScan);
            return;
        }

        //Only ARTICLE scans are allowed
        if (!cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(),cBarcodeLayout.barcodeLayoutEnu.ARTICLE)) {
            mDoUnknownScan(cAppExtension.context.getString(R.string.error_article_scan_mandatory), pvBarcodeScan.getBarcodeOriginalStr());
            return;
        }

        //Check if this is a barcodeStr we already know
        cInventoryorderBarcode inventoryorderBarcode = cInventoryorder.currentInventoryOrder.pGetOrderBarcode(pvBarcodeScan);

        //We scanned an unkown barcodeStr
        if (inventoryorderBarcode == null) {
            InventoryorderBinActivity.mHandleUnknownBarcodeScan(pvBarcodeScan);
            return;
        }

        //We scanned a barcodeStr we already know
        InventoryorderBinActivity.mHandleKnownBarcodeScan(inventoryorderBarcode);

    }

    private static void mDoUnknownScan(String pvErrorMessageStr, String pvScannedBarcodeStr) {
        cUserInterface.pDoExplodingScreen(pvErrorMessageStr, pvScannedBarcodeStr, true, true);
        //We are not busy anymore
        InventoryorderBinActivity.busyBln = false;
    }

    private void mTryToLeaveActivity(Boolean pvAskCloseBln) {

        if(cInventoryorder.currentInventoryOrder.isGeneratedBln()) {
            if (cInventoryorder.currentInventoryOrder.pGetLinesForBinObl(cInventoryorderBin.currentInventoryOrderBin.getBinCodeStr()).size() == 0) {
                mShowCloseBinDialog();
                return;
            }

            InventoryorderBinActivity.pCloseBin();
            return;
        }

        if (! cInventoryorder.currentInventoryOrder.isGeneratedBln() && cInventoryorder.currentInventoryOrder.pGetItemCountForBinDbl(cInventoryorderBin.currentInventoryOrderBin.getBinCodeStr()) == 0) {

            if (pvAskCloseBln) {
                mShowCloseBinDialog();
                return;
            }

            //Clear cache
            cInventoryorderBin.currentInventoryOrderBin = null;

            //Start BIN activity
            InventoryorderBinActivity.mStartBinsActivity();
        }
        else {
            InventoryorderBinActivity.pCloseBin();
        }
    }

    private static void mFillRecyclerView(List<cInventoryorderLine> pvInventoryorderLinesObl) {

        if (pvInventoryorderLinesObl == null || pvInventoryorderLinesObl.size() == 0) {
            return;
        }

        for (Fragment fragment: cAppExtension.fragmentManager.getFragments()) {
            if (fragment instanceof NoOrdersFragment) {
                cAppExtension.fragmentManager.beginTransaction().remove(fragment).commit();
            }
        }

        InventoryorderBinActivity.recyclerViewInventoryorderLines.setHasFixedSize(false);
        InventoryorderBinActivity.recyclerViewInventoryorderLines.setAdapter(cInventoryorderLine.getInventoryorderLineAdapter());
        InventoryorderBinActivity.recyclerViewInventoryorderLines.setLayoutManager(new LinearLayoutManager(cAppExtension.context));

        cInventoryorderLine.getInventoryorderLineAdapter().pFillData(pvInventoryorderLinesObl);
    }

    private void mSetToolbarTitleListeners() {
        InventoryorderBinActivity.toolbarTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mScrollToTop();
            }
        });
        InventoryorderBinActivity.toolbarTitle.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mScrollToBottom();
                return true;
            }
        });
    }

    private void mSetDoneListeners() {

        InventoryorderBinActivity.imageBinDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTryToLeaveActivity(true);
            }
        });

    }

    private void mScrollToTop() {
        recyclerViewInventoryorderLines.smoothScrollToPosition(0);
    }

    private void mScrollToBottom() {
        if (cInventoryorder.getInventoryorderAdapter() != null) {
            if (cInventoryorder.getInventoryorderAdapter().getItemCount() > 0) {
                recyclerViewInventoryorderLines.smoothScrollToPosition(cInventoryorderLine.getInventoryorderLineAdapter().getItemCount() - 1);
            }
        }
    }

    private static void mStartBinsActivity() {
        Intent intent = new Intent(cAppExtension.context, InventoryorderBinsActivity.class);
        cAppExtension.activity.startActivity(intent);
        cAppExtension.activity.finish();
    }

    private static void mAddUnkownArticle(cBarcodeScan pvBarcodeScan){

        //Add the barcodeStr via the webservice
        if (!cInventoryorder.currentInventoryOrder.pAddUnkownBarcodeBln(pvBarcodeScan)) {
            cUserInterface.pDoExplodingScreen(cAppExtension.activity.getString(R.string.message_adding_unkown_article_failed),"",true,true);
            InventoryorderBinActivity.busyBln = false;
            return;
        }

        //Add the line via the webservice
        if (!cInventoryorder.currentInventoryOrder.pAddLineBln()) {
            cUserInterface.pDoExplodingScreen(cAppExtension.activity.getString(R.string.message_adding_line_failed),"",true,true);
            InventoryorderBinActivity.busyBln = false;
            return;
        }

        //Add quantityDbl of the current barcodeStr
        cInventoryorderLine.currentInventoryOrderLine.quantityHandledDbl += cInventoryorderBarcode.currentInventoryOrderBarcode.getQuantityPerUnitOfMeasureDbl();
        cInventoryorderBarcode.currentInventoryOrderBarcode.quantityHandled += cInventoryorderBarcode.currentInventoryOrderBarcode.getQuantityPerUnitOfMeasureDbl();

        //Make line barcodeStr the current line barcodeStr
        cInventoryorderLineBarcode.currentInventoryorderLineBarcode = cInventoryorderLine.currentInventoryOrderLine.lineBarcodesObl().get(0);


        //Open the line, so we can edit it
        InventoryorderBinActivity.mShowArticleDetailFragment();

    }

    private static void mAddERPArticle(cBarcodeScan pvBarcodeScan){

        //Add the barcodeStr via the webservice
        if (!cInventoryorder.currentInventoryOrder.pAddERPBarcodeBln(pvBarcodeScan)) {
            InventoryorderBinActivity.mStepFailed(cAppExtension.activity.getString(R.string.message_adding_erp_article_failed), pvBarcodeScan.barcodeOriginalStr);
            InventoryorderBinActivity.busyBln = false;
            return;
        }

        //Add the line via the webservice
        if (!cInventoryorder.currentInventoryOrder.pAddLineBln()) {
            InventoryorderBinActivity.mStepFailed(cAppExtension.activity.getString(R.string.message_adding_line_failed),pvBarcodeScan.barcodeOriginalStr);
            InventoryorderBinActivity.busyBln = false;
            return;
        }

        //Add quantityDbl of the current barcodeStr
        cInventoryorderLine.currentInventoryOrderLine.quantityHandledDbl += cInventoryorderBarcode.currentInventoryOrderBarcode.getQuantityPerUnitOfMeasureDbl();
        cInventoryorderBarcode.currentInventoryOrderBarcode.quantityHandled += cInventoryorderBarcode.currentInventoryOrderBarcode.getQuantityPerUnitOfMeasureDbl();

        //Make line barcodeStr the current line barcodeStr
        cInventoryorderLineBarcode.currentInventoryorderLineBarcode = cInventoryorderLine.currentInventoryOrderLine.lineBarcodesObl().get(0);


        //Open the line, so we can edit it
        InventoryorderBinActivity.mShowArticleDetailFragment();

    }

    private static void mHandleUnknownBarcodeScan(cBarcodeScan pvBarcodeScan) {

        // Check if we can add a line
        if (! cSetting.INV_ADD_EXTRA_LINES()) {
            InventoryorderBinActivity.mStepFailed(cAppExtension.activity.getString(R.string.message_add_article_now_allowed),pvBarcodeScan.barcodeOriginalStr);
            InventoryorderBinActivity.busyBln = false;
            return;
        }

        //We can add a line, but we don't check with the ERP, so add line and open it
        if (! cInventoryorder.currentInventoryOrder.isInvBarcodeCheckBln()) {
            InventoryorderBinActivity.mAddUnkownArticle(pvBarcodeScan);
            InventoryorderBinActivity.busyBln = false;
            return;
        }

        //We can add a line, and we need to check with the ERP, so check, add and open it
        InventoryorderBinActivity.mAddERPArticle(pvBarcodeScan);
    }

    private static void mHandleKnownBarcodeScan(cInventoryorderBarcode pvInventoryorderBarcode) {

        boolean updateQuantityBln = true;

        //Set the current barcodeStr
        cInventoryorderBarcode.currentInventoryOrderBarcode = pvInventoryorderBarcode;

        //Check if this barcodeStr belongs to this BIN
        cInventoryorderLine.currentInventoryOrderLine = cInventoryorder.currentInventoryOrder.pGetLineForArticleAndBin();

        //Line doesn't belong to this BIN and we are not allowed to add lines
        if (cInventoryorderLine.currentInventoryOrderLine == null )  {

            if (!cSetting.INV_ADD_EXTRA_LINES()) {
                InventoryorderBinActivity.mStepFailed(cAppExtension.activity.getString(R.string.message_add_article_for_this_bin_now_allowed),pvInventoryorderBarcode.getBarcodeStr());
                InventoryorderBinActivity.busyBln = false;
                return;
            }

            //Add the line via the webservice
            if (!cInventoryorder.currentInventoryOrder.pAddLineBln()) {
                InventoryorderBinActivity.mStepFailed(cAppExtension.activity.getString(R.string.message_adding_line_failed),pvInventoryorderBarcode.getBarcodeStr());
                InventoryorderBinActivity.busyBln = false;
                return;
            }

            updateQuantityBln = false;

        }

        //We scanned a barcodeStr that belongs to the current article, so check if we already have a line barcodeStr for this barcodeStr
        cInventoryorderLineBarcode.currentInventoryorderLineBarcode = cInventoryorderLine.currentInventoryOrderLine.pGetLineBarcodeByScannedBarcode(cBarcodeScan.pFakeScan(cInventoryorderBarcode.currentInventoryOrderBarcode.getBarcodeStr()));

        if (cInventoryorderLineBarcode.currentInventoryorderLineBarcode != null) {
            //Raise the quantity handled
            if (updateQuantityBln) {
                cInventoryorderLineBarcode.currentInventoryorderLineBarcode.quantityHandledDbl += cInventoryorderBarcode.currentInventoryOrderBarcode.getQuantityPerUnitOfMeasureDbl();
            }

        } else {

            //This is a new barcodeStr, so add it
            cInventoryorderLine.currentInventoryOrderLine.pAddLineBarcode(cInventoryorderBarcode.currentInventoryOrderBarcode.getBarcodeStr(),cInventoryorderBarcode.currentInventoryOrderBarcode.getQuantityPerUnitOfMeasureDbl());

            //Make added line the current line
            cInventoryorderLineBarcode.currentInventoryorderLineBarcode = cInventoryorderLine.currentInventoryOrderLine.pGetLineBarcodeByScannedBarcode(cBarcodeScan.pFakeScan(cInventoryorderBarcode.currentInventoryOrderBarcode.getBarcodeStr()));
        }

        //Add quantityDbl of the current barcodeStr
        cInventoryorderLine.currentInventoryOrderLine.quantityHandledDbl += cInventoryorderBarcode.currentInventoryOrderBarcode.getQuantityPerUnitOfMeasureDbl();

        //Open the line (found or created), so we can edit it
        InventoryorderBinActivity.mShowArticleDetailFragment();
    }

    private static void mShowArticleDetailFragment() {

        InventoryArticleDetailFragment articleDetailFragment = new InventoryArticleDetailFragment();
        articleDetailFragment.setCancelable(false);
        articleDetailFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.ARTICLEDETAILFRAGMENT_TAG);
    }

    private static void mStepFailed(String pvErrorMessageStr , String scannedBarcode){
        cUserInterface.pDoExplodingScreen(pvErrorMessageStr, scannedBarcode, true, true );
    }

    private static void mShowNoLinesIcon(final Boolean pvShowBln){

        cAppExtension.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                cUserInterface.pHideGettingData();

                InventoryorderBinActivity.mSetToolBarTitleWithCounters();
                InventoryorderBinActivity.imageBinDone.setVisibility(View.INVISIBLE);

                if (pvShowBln) {

                    InventoryorderBinActivity.recyclerViewInventoryorderLines.setVisibility(View.INVISIBLE);

                    FragmentTransaction fragmentTransaction = cAppExtension.fragmentManager.beginTransaction();
                    NothingHereFragment fragment = new NothingHereFragment();
                    fragmentTransaction.replace(R.id.inventoryorderContainer, fragment);
                    fragmentTransaction.commit();
                    return;
                }

                InventoryorderBinActivity.recyclerViewInventoryorderLines.setVisibility(View.VISIBLE);
                InventoryorderBinActivity.imageBinDone.setVisibility(View.VISIBLE);

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
        String toolBarStr = cAppExtension.activity.getString(R.string.items) + ' ' +  cText.pDoubleToStringStr(cInventoryorder.currentInventoryOrder.pGetItemCountForBinDbl(cInventoryorderBin.currentInventoryOrderBin.getBinCodeStr()));
        InventoryorderBinActivity.toolbarSubTitle.setText(toolBarStr);
    }

    private void mShowCloseBinDialog() {

        cUserInterface.pCheckAndCloseOpenDialogs();
        String messageStr =  cAppExtension.activity.getString(R.string.message_bin_text_sure) ;

        if (!cInventoryorder.currentInventoryOrder.isGeneratedBln() &&
            cInventoryorder.currentInventoryOrder.pGetItemCountForBinDbl(cInventoryorderBin.currentInventoryOrderBin.getBinCodeStr()) == 0) {
            messageStr = cAppExtension.activity.getString(R.string.message_bin_empty_close);
        }

        final AcceptRejectFragment acceptRejectFragment = new AcceptRejectFragment( cAppExtension.activity.getString(R.string.message_close_bin),
                messageStr, cAppExtension.activity.getString(R.string.message_cancel),cAppExtension.activity.getString(R.string.message_close), false);
        acceptRejectFragment.setCancelable(true);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // show my popup
                acceptRejectFragment.show(cAppExtension.fragmentManager, ACCEPTREJECTFRAGMENT_TAG);
            }
        });
    }

    private void mSetAddArticleListener() {
        InventoryorderBinActivity.imageAddArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mShowAddArticleFragment();
            }
        });
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder pvViewHolder, int pvDirectionInt, int pvPositionInt) {

        if (!(pvViewHolder instanceof cInventoryorderLineAdapter.InventoryorderLineViewHolder)) {
            return;
        }
        InventoryorderBinActivity.positionSwiped = pvPositionInt;

        cInventoryorderLine.currentInventoryOrderLine = cInventoryorder.currentInventoryOrder.pGetLinesForBinObl(cInventoryorderBin.currentInventoryOrderBin.getBinCodeStr()).get(pvPositionInt);
        if (cInventoryorderLine.currentInventoryOrderLine.getQuantityHandledDbl() <= 0) {
            cUserInterface.pShowSnackbarMessage(pvViewHolder.itemView,cAppExtension.activity.getString(R.string.message_zero_lines_cant_be_reset),null,true);
            cInventoryorderLine.getInventoryorderLineAdapter().notifyItemChanged(pvPositionInt);
            return;
        }

        //do we need an adult for this?
        if (!cSetting.INV_RESET_PASSWORD().isEmpty()) {
            cUserInterface.pShowpasswordDialog(getString(R.string.supervisor_password_header), getString(R.string.supervisor_password_text), false);
            return;
        }

        //Remove the enviroment
        InventoryorderBinActivity.mRemoveAdapterFromFragment();

    }

    public static void pPasswordSuccess() {
        cBarcodeScan.pRegisterBarcodeReceiver();
        mRemoveAdapterFromFragment();
    }

    public static void pPasswordCancelled() {
        cBarcodeScan.pRegisterBarcodeReceiver();
        cInventoryorderLine.getInventoryorderLineAdapter().notifyItemChanged(positionSwiped);
    }

    private static void mRemoveAdapterFromFragment(){

        //remove the item from recyclerview
        cResult hulpRst = cInventoryorderLine.currentInventoryOrderLine.pResetRst();
        if (! hulpRst.resultBln) {
            cUserInterface.pDoExplodingScreen(hulpRst.messagesStr(),"",true,true);
            return;
        }

        //Renew data, so only current lines are shown
        InventoryorderBinActivity.pFillLines();

    }

    private void mShowAddArticleFragment(){
        AddArticleFragment addArticleFragment = new AddArticleFragment();
        addArticleFragment.setCancelable(true);
        addArticleFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.ADDARTICLE_TAG);

    }

    //End Region Private Methods

}
