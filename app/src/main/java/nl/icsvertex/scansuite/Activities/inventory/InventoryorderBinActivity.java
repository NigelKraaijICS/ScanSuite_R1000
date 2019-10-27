package nl.icsvertex.scansuite.Activities.inventory;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ICS.Interfaces.iICSDefaultActivity;
import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cRegex;
import ICS.Utils.cText;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Basics.BarcodeLayouts.cBarcodeLayout;
import SSU_WHS.Basics.Settings.cSetting;
import SSU_WHS.General.cPublicDefinitions;
import SSU_WHS.Inventory.InventoryOrders.cInventoryorder;
import SSU_WHS.Inventory.InventoryorderBarcodes.cInventoryorderBarcode;
import SSU_WHS.Inventory.InventoryorderBins.cInventoryorderBin;
import SSU_WHS.Inventory.InventoryorderLines.cInventoryorderLine;
import nl.icsvertex.scansuite.Fragments.NoOrdersFragment;
import nl.icsvertex.scansuite.Fragments.NothingHereFragment;
import nl.icsvertex.scansuite.Fragments.inventory.InventoryArticleDetailFragment;
import nl.icsvertex.scansuite.R;

public class InventoryorderBinActivity extends AppCompatActivity implements iICSDefaultActivity {

    //Region Public Properties
    public static String VIEW_CHOSEN_BIN = "detail:header:text";
    public static final String VIEW_CHOSEN_BIN_IMAGE = "detail:header:imageStr";
    //End Region Public Properties

    //Region Private Properties
    private TextView binText;
    private ImageView toolbarImage;
    private static ImageView imageBinDone;
    private TextView toolbarTitle;
    private static TextView toolbarSubTitle;
    private static RecyclerView recyclerViewInventoryorderLines;
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
        cBarcodeScan.pRegisterBarcodeReceiver();
        super.onResume();
    }

    @Override
    protected void onPause() {
        try {
            cBarcodeScan.pUnregisterBarcodeReceiver();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onPause();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem pvMenuItem) {

        if (pvMenuItem.getItemId() == android.R.id.home) {
            this.mTryToLeaveActivity();
            return true;
        }

        return super.onOptionsItemSelected(pvMenuItem);
    }

    @Override
    public void onBackPressed() {
        this.mTryToLeaveActivity();
    }

    //End Region Default Methods

    //Region iICSDefaultActivity defaults

    @Override
    public void mActivityInitialize() {

        this.mSetAppExtensions();

        this.mFindViews();

        this.mSetToolbar(getResources().getString(R.string.screentitle_inventoryordercount));

        this.mFieldsInitialize();

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
        this.toolbarImage = findViewById(R.id.toolbarImage);
        this.toolbarTitle = findViewById(R.id.toolbarTitle);
        this.toolbarSubTitle = findViewById(R.id.toolbarSubtext);
        this.binText = findViewById(R.id.binText);
        this.imageBinDone = findViewById(R.id.imageViewBinDone);
        this.recyclerViewInventoryorderLines = findViewById(R.id.recyclerViewInventoryorderLines);
    }

    @Override
    public void mSetToolbar(String pvScreenTitleStr) {
        this.toolbarImage.setImageResource(R.drawable.ic_menu_inventory);
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
        ViewCompat.setTransitionName(this.imageBinDone, this.VIEW_CHOSEN_BIN_IMAGE);
        ViewCompat.setTransitionName(this.binText, this.VIEW_CHOSEN_BIN);
        this.binText.setText(cInventoryorderBin.currentInventoryOrderBin.getBinCodeStr());
        InventoryorderBinActivity.pFillLines();
    }

    @Override
    public void mSetListeners() {
        mSetToolbarTitleListeners();
    }

    @Override
    public void mInitScreen() {

    }

    //End Region iICSDefaultActivity defaults

    //Region Public Methods

    public static void pHandleScan(final cBarcodeScan pvBarcodeScan) {

        //Close open Dialogs
        cUserInterface.pCheckAndCloseOpenDialogs();

        // Show that we are getting data
        cUserInterface.pShowGettingData();

        new Thread(new Runnable() {
            public void run() {
                mHandleScan(pvBarcodeScan);
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

    public static void pLineSelected() {

        //Close open Dialogs
        cUserInterface.pCheckAndCloseOpenDialogs();

        InventoryorderBinActivity.mShowArticleDetailFragment();

    }

    //End Region Public Methods

    //Region Private Methods

    private static void mHandleScan(cBarcodeScan pvBarcodeScan){

        //Only ARTICLE scans are allowed
        if (!cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(),cBarcodeLayout.barcodeLayoutEnu.ARTICLE)) {
            mDoUnknownScan(cAppExtension.context.getString(R.string.error_article_scan_mandatory), pvBarcodeScan.getBarcodeOriginalStr());
            return;
        }

        //Strip the prefix if thats neccesary
        String barcodewithoutPrefix = cRegex.pStripRegexPrefixStr(pvBarcodeScan.getBarcodeOriginalStr());

        //Check if this is a barcode we already know
        cInventoryorderBarcode inventoryorderBarcode = cInventoryorder.currentInventoryOrder.pGetOrderBarcode(barcodewithoutPrefix);

        //We scanned an unkown barcode
        if (inventoryorderBarcode == null) {
            InventoryorderBinActivity.mHandleUnknownBarcodeScan(pvBarcodeScan);
            return;
        }

        //We scanned a barcode we already know
        InventoryorderBinActivity.mHandleKnownBarcodeScan(inventoryorderBarcode);
        return;

    }

    private static void mDoUnknownScan(String pvErrorMessageStr, String pvScannedBarcodeStr) {
        cUserInterface.pDoExplodingScreen(pvErrorMessageStr, pvScannedBarcodeStr, true, true);
    }

    private void mTryToLeaveActivity() {
        this.mStartBinsActivity();
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
        return;
    }

    private void mSetToolbarTitleListeners() {
        this.toolbarTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mScrollToTop();
            }
        });
        this.toolbarTitle.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mScrollToBottom();
                return true;
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

    private void mStartBinsActivity() {
        Intent intent = new Intent(cAppExtension.context, InventoryorderBinsActivity.class);
        cAppExtension.activity.startActivity(intent);
    }

    private static void mAddUnkownArticle(cBarcodeScan pvBarcodeScan){

        //Add the barcode via the webservice
       if (!cInventoryorder.currentInventoryOrder.pAddUnkownBarcodeBln(pvBarcodeScan)) {
           cUserInterface.pDoExplodingScreen(cAppExtension.activity.getString(R.string.message_adding_unkown_article_failed),"",true,true);
           return;
       }

       //Add the line via the webservice
        if (!cInventoryorder.currentInventoryOrder.pAddLineBln()) {
            cUserInterface.pDoExplodingScreen(cAppExtension.activity.getString(R.string.message_adding_line_failed),"",true,true);
            return;
        }

        //Add quantity of the current barcode
        cInventoryorderLine.currentInventoryOrderLine.quantityHandledDbl += cInventoryorderBarcode.currentInventoryOrderBarcode.getQuantityPerUnitOfMeasureDbl();

        //Open the line, so we can edit it
        InventoryorderBinActivity.mShowArticleDetailFragment();

    }

    private static void mAddERPArticle(cBarcodeScan pvBarcodeScan){

        //Add the barcode via the webservice
        if (!cInventoryorder.currentInventoryOrder.pAddERPBarcodeBln(pvBarcodeScan)) {
            InventoryorderBinActivity.mStepFailed(cAppExtension.activity.getString(R.string.message_adding_erp_article_failed));
            return;
        }

        //Add the line via the webservice
        if (!cInventoryorder.currentInventoryOrder.pAddLineBln()) {
            InventoryorderBinActivity.mStepFailed(cAppExtension.activity.getString(R.string.message_adding_line_failed));
            return;
        }

        //Add quantity of the current barcode
        cInventoryorderLine.currentInventoryOrderLine.quantityHandledDbl += cInventoryorderBarcode.currentInventoryOrderBarcode.getQuantityPerUnitOfMeasureDbl();

        //Open the line, so we can edit it
        InventoryorderBinActivity.mShowArticleDetailFragment();

    }

    private static void mHandleUnknownBarcodeScan(cBarcodeScan pvBarcodeScan) {

            // Check if we can add a line
            if (! cSetting.INV_ADD_EXTRA_LINES()) {

                InventoryorderBinActivity.mStepFailed(cAppExtension.activity.getString(R.string.message_add_article_now_allowed));
                return;
            }

            //We can add a line, but we don't check with the ERP, so add line and open it
            if (! cInventoryorder.currentInventoryOrder.isInvBarcodeCheckBln()) {
                InventoryorderBinActivity.mAddUnkownArticle(pvBarcodeScan);
                return;
            }

            //We can add a line, and we need to check with the ERP, so check, add and open it
            InventoryorderBinActivity.mAddERPArticle(pvBarcodeScan);
            return;
    }

    private static void mHandleKnownBarcodeScan(cInventoryorderBarcode pvInventoryorderBarcode) {

        //Set the current barcode
        cInventoryorderBarcode.currentInventoryOrderBarcode = pvInventoryorderBarcode;

        //Check if this barcode belongs to this BIN
        cInventoryorderLine.currentInventoryOrderLine = cInventoryorder.currentInventoryOrder.pGetLineForArticleAndBin();

        //Line doesn't belong to this BIN and we are not allowed to add lines
        if (cInventoryorderLine.currentInventoryOrderLine == null )  {

            if (!cSetting.INV_ADD_EXTRA_LINES()) {
                InventoryorderBinActivity.mStepFailed(cAppExtension.activity.getString(R.string.message_add_article_for_this_bin_now_allowed));
                return;
            }

            //Add the line via the webservice
            if (!cInventoryorder.currentInventoryOrder.pAddLineBln()) {
                InventoryorderBinActivity.mStepFailed(cAppExtension.activity.getString(R.string.message_adding_line_failed));
                return;
            }
        }


        //Open the line (found or created), so we can edit it
        InventoryorderBinActivity.mShowArticleDetailFragment();
    }

    private static void mShowArticleDetailFragment() {

        InventoryArticleDetailFragment articleDetailFragment = new InventoryArticleDetailFragment();
        articleDetailFragment.setCancelable(false);
        articleDetailFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.ARTICLEDETAILFRAGMENT_TAG);
    }

    private static void mStepFailed(String pvErrorMessageStr ){
        cUserInterface.pDoExplodingScreen(pvErrorMessageStr, cInventoryorder.currentInventoryOrder.getOrderNumberStr(), true, true );
        cUserInterface.pCheckAndCloseOpenDialogs();
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
        InventoryorderBinActivity.toolbarSubTitle.setText(cText.pDoubleToStringStr(cInventoryorder.currentInventoryOrder.pGetCountForBinDbl(cInventoryorderBin.currentInventoryOrderBin.getBinCodeStr())) );
    }

    //End Region Private Methods

}
