package nl.icsvertex.scansuite.Activities.Inventory;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;

import java.util.List;

import ICS.Interfaces.iICSDefaultActivity;
import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cNoSwipeViewPager;
import ICS.Utils.cRegex;
import ICS.Utils.cResult;
import ICS.Utils.cText;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Basics.BarcodeLayouts.cBarcodeLayout;
import SSU_WHS.Basics.BranchBin.cBranchBin;
import SSU_WHS.Basics.Users.cUser;
import SSU_WHS.General.Comments.cComment;
import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.General.cPublicDefinitions;
import SSU_WHS.Inventory.InventoryOrders.cInventoryorder;
import SSU_WHS.Inventory.InventoryorderBins.cInventoryorderBin;
import nl.icsvertex.scansuite.Fragments.Dialogs.AcceptRejectFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.CommentFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.SendingFragment;
import nl.icsvertex.scansuite.PagerAdapters.InventoryorderBinsPagerAdapter;
import nl.icsvertex.scansuite.R;

import static nl.icsvertex.scansuite.Activities.Inventory.InventoryorderBinActivity.ACCEPTREJECTFRAGMENT_TAG;

public class InventoryorderBinsActivity extends AppCompatActivity implements iICSDefaultActivity {
    public static String VIEW_CHOSEN_ORDER = "detail:header:text";

    //Region Public Properties
    public static Fragment currentBinFragment;
    //End Region Public Properties

    //Region Private Properties
    private TextView textViewChosenOrder;
    static private TextView quantityText;
    static private TabLayout inventoryorderBinsTabLayout;
    static private cNoSwipeViewPager inventoryorderBinsViewpager;

    private InventoryorderBinsPagerAdapter inventoryorderBinsPagerAdapter;
    static private ImageView imageButtonComments;

    private ImageView toolbarImage;
    private TextView toolbarTitle;
    private static TextView toolbarSubTitle;

    private static String SENDING_TAG = "SENDING_TAG";

    //End Region Private Properties

    //Region Default Methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventoryorder_bins);
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
            this.mShowAcceptFragment();
            return true;
        }

        return super.onOptionsItemSelected(pvMenuItem);
    }

    @Override
    public void onBackPressed() {
       this.mShowAcceptFragment();
    }

    //End Region Default Methods

    //Region iICSDefaultActivity defaults

    @Override
    public void mActivityInitialize() {

        this.mSetAppExtensions();

        this.mFindViews();

        this.mSetToolbar(getResources().getString(R.string.screentitle_inventoryorderbins));

        this.mFieldsInitialize();

        this.mInitScreen();

        cBarcodeScan.pRegisterBarcodeReceiver();
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
        this.toolbarSubTitle = findViewById(R.id.toolbarSubtext);
        this.inventoryorderBinsTabLayout = findViewById(R.id.inventoryorderBinsTabLayout);
        this.inventoryorderBinsViewpager = findViewById(R.id.inventoryorderBinsViewpager);
        this.textViewChosenOrder = findViewById(R.id.textViewChosenOrder);
        this.imageButtonComments = findViewById(R.id.imageButtonComments);
        this.quantityText = findViewById(R.id.quantityText);
    }

    @Override
    public void mSetToolbar(String pvScreenTitleStr) {
        this.toolbarImage.setImageResource(R.drawable.ic_menu_inventory);
        this.toolbarTitle.setText(pvScreenTitleStr);
        toolbarTitle.setSelected(true);
        toolbarSubTitle.setSelected(true);
        InventoryorderBinsActivity.pChangeToolBarSubText(cAppExtension.activity.getString(R.string.items) + ' ' +  cText.pDoubleToStringStr(cInventoryorder.currentInventoryOrder.pGetTotalCountDbl()) );
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void mFieldsInitialize() {
        ViewCompat.setTransitionName(this.textViewChosenOrder, this.VIEW_CHOSEN_ORDER);
        this.textViewChosenOrder.setText(cInventoryorder.currentInventoryOrder.getOrderNumberStr());
        this.inventoryorderBinsTabLayout.addTab(this.inventoryorderBinsTabLayout.newTab().setText(R.string.tab_inventorybin_todo));
        this.inventoryorderBinsTabLayout.addTab(this.inventoryorderBinsTabLayout.newTab().setText(R.string.tab_inventorybin_done));
        this.inventoryorderBinsTabLayout.addTab(this.inventoryorderBinsTabLayout.newTab().setText(R.string.tab_inventorybin_total));

        this.inventoryorderBinsPagerAdapter = new InventoryorderBinsPagerAdapter(this.inventoryorderBinsTabLayout.getTabCount());
        this.inventoryorderBinsViewpager.setAdapter(this.inventoryorderBinsPagerAdapter);

        this.inventoryorderBinsViewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(this.inventoryorderBinsTabLayout));
        this.inventoryorderBinsTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab pvTab) {
                inventoryorderBinsViewpager.setCurrentItem(pvTab.getPosition());
                mChangeSelectedTab(pvTab);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab pvTab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab pvTab) {

            }
        });



    }

    @Override
    public void mSetListeners() {

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

    public static void pHandleAddBinFragmentDismissed() {
        cBarcodeScan.pRegisterBarcodeReceiver();
    }

    public static void pHandleOrderCloseClick() {
        InventoryorderBinsActivity.mShowOrderCloseDialog();
    }

    public static void pCloseOrder(){


        mShowSending();
        new Thread(new Runnable() {
            public void run() {
                mHandleCloseOrder();
            }
        }).start();



    }

    public static void pInventoryorderBinSelected() {
         InventoryorderBinsActivity.mOpenInventoryCountActivity();
    }

    public static void pChangeTabCounterText(String pvTextStr){
        InventoryorderBinsActivity.quantityText.setText(pvTextStr);
    }

    public static void pChangeToolBarSubText(String pvTextStr){

        InventoryorderBinsActivity.toolbarSubTitle.setText(pvTextStr);

    }

    public static void pAcceptRejectDialogDismissed() {

        cInventoryorder.currentInventoryOrder.pLockReleaseViaWebserviceBln(cWarehouseorder.StepCodeEnu.Inventory,cWarehouseorder.WorkflowInventoryStepEnu.Inventory);
        mStartOrderSelectActivity();

    }

    //End Region Public Methods

    //Region Private Methods

    private static void mHandleCloseOrder(){

        cResult hulpResult;
        hulpResult = new cResult();
        hulpResult.resultBln = false;

        for (cInventoryorderBin inventoryorderBin : cInventoryorder.currentInventoryOrder.pGetBinsDoneFromDatabasObl()) {

            if (!inventoryorderBin.pCloseBln(true)) {
                mShowNotSend(inventoryorderBin.getBinCodeStr() + ' ' +  cAppExtension.activity.getString(R.string.message_bin_close_failed));
                return;
            }
        }

        hulpResult = cInventoryorder.currentInventoryOrder.pOrderHandledViaWebserviceRst();

        //Everything was fine, so we are done
        if (hulpResult.resultBln) {
            mShowSent();
            InventoryorderBinsActivity.mStartOrderSelectActivity();
            return;
        }

        //Something went wrong, but no further actions are needed, so ony show reason of failure
        if (!hulpResult.resultBln && hulpResult.activityActionEnu == cWarehouseorder.ActivityActionEnu.Unknown ) {
            mShowNotSend(hulpResult.messagesStr());
            return;
        }

        //Something went wrong, the order has been deleted, so show comments and refresh
        if (!hulpResult.resultBln && hulpResult.activityActionEnu == cWarehouseorder.ActivityActionEnu.Hold ) {
            mShowNotSend("");
            //If we got any comments, show them
            if (cInventoryorder.currentInventoryOrder.pFeedbackCommentObl() != null && cInventoryorder.currentInventoryOrder.pFeedbackCommentObl().size() > 0 ) {
                //Process comments from webresult
                InventoryorderBinsActivity.mShowCommentsFragment(cInventoryorder.currentInventoryOrder.pFeedbackCommentObl(), hulpResult.messagesStr());
            }

            return;
        }

    }

    private  static void mHandleScan(cBarcodeScan pvBarcodeScan){

        //Only BIN scans are allowed
        if (!cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(),cBarcodeLayout.barcodeLayoutEnu.BIN)) {
            mDoUnknownScan(cAppExtension.context.getString(R.string.error_unknown_location), pvBarcodeScan.getBarcodeOriginalStr());
            return;
        }

        //Strip the prefix if thats neccesary
        String barcodewithoutPrefix = cRegex.pStripRegexPrefixStr(pvBarcodeScan.getBarcodeOriginalStr());

        cResult hulpRst;
        hulpRst = InventoryorderBinsActivity.mCheckAndGetBinRst(barcodewithoutPrefix);
        //Something went wrong, so show message and return
        if (! hulpRst.resultBln) {
            InventoryorderBinsActivity.mStepFailed(hulpRst.messagesStr());
            return;
        }

        //Everything went well, so we can open the BIN
        InventoryorderBinsActivity.pInventoryorderBinSelected();

    }

    private static cResult mCheckAndGetBinRst(String pvBinCodeStr){

        cResult result = new cResult();
        result.resultBln = true;

        //Search for the BIN in current BINS
        cInventoryorderBin.currentInventoryOrderBin = cInventoryorder.currentInventoryOrder.pGetBin(pvBinCodeStr);

        //We found a BIN so we are done
        if (cInventoryorderBin.currentInventoryOrderBin  != null) {
            return result;
        }

        //We scanned a NEW BIN so check of we are allowed to add a BIN
        if (!cInventoryorder.currentInventoryOrder.isInvAddExtraBinBln() && !cInventoryorder.currentInventoryOrder.isGeneratedBln() ) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.activity.getString(R.string.message_add_bin_not_allowed));
            return result;
        }

        //Search for BIN in Branc cache/via webservice
        cBranchBin branchBin = cUser.currentUser.currentBranch.pGetBinByCode(pvBinCodeStr);
        if (branchBin == null) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.activity.getString(R.string.message_bin_unknown,pvBinCodeStr));
            return result;
        }

        //Add BIn to the order via the webservice
        cInventoryorderBin.currentInventoryOrderBin = cInventoryorder.currentInventoryOrder.pAddInventoryBin(branchBin);
        if (cInventoryorder.currentInventoryOrder == null) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.activity.getString(R.string.message_bin_could_not_be_added_via_webservice));
            return result;
        }

        return  result;


    }

    private void mChangeSelectedTab(TabLayout.Tab pvTab) {

        switch (pvTab.getPosition()) {
            case 0:
                InventoryorderBinsActivity.pChangeTabCounterText(cText.pIntToStringStr(cInventoryorder.currentInventoryOrder.pGetBinsNotDoneFromDatabasObl().size()) + "/" + cText.pIntToStringStr(cInventoryorder.currentInventoryOrder.pGetBinsTotalFromDatabasObl().size()));
                break;
            case 1:
                InventoryorderBinsActivity.pChangeTabCounterText(cText.pIntToStringStr(cInventoryorder.currentInventoryOrder.pGetBinsDoneFromDatabasObl().size()) + "/" + cText.pIntToStringStr(cInventoryorder.currentInventoryOrder.pGetBinsTotalFromDatabasObl().size()));
                break;
            case 2:
                InventoryorderBinsActivity.pChangeTabCounterText(cText.pIntToStringStr(cInventoryorder.currentInventoryOrder.pGetBinsTotalFromDatabasObl().size()));
                break;
            default:

        }
    }

    private  static void mDoUnknownScan(String pvErrorMessageStr, String pvScannedBarcodeStr) {
        cUserInterface.pDoExplodingScreen(pvErrorMessageStr, pvScannedBarcodeStr, true, true);
    }

    private static void mOpenInventoryCountActivity() {

        cUserInterface.pCheckAndCloseOpenDialogs();

        final ViewGroup container = cAppExtension.activity.findViewById(R.id.container);

        final Intent intent = new Intent(cAppExtension.context, InventoryorderBinActivity.class);
        final View clickedBin = container.findViewWithTag(cInventoryorderBin.currentInventoryOrderBin.getBinCodeStr());
        final View clickedBinImage = container.findViewWithTag(cInventoryorderBin.currentInventoryOrderBin.getBinCodeStr() + "_IMG");
        if (clickedBin != null &&clickedBinImage != null) {
            cAppExtension.activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ActivityOptionsCompat activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(cAppExtension.activity, new Pair<>(clickedBin, InventoryorderBinActivity.VIEW_CHOSEN_BIN),new Pair<>(clickedBinImage, InventoryorderBinActivity.VIEW_CHOSEN_BIN_IMAGE) );
                    ActivityCompat.startActivity(cAppExtension.context,intent, activityOptions.toBundle());
                    return;
                }
            });

        }
        else {
            cAppExtension.activity.startActivity(intent);
            cAppExtension.activity.finish();
        }
    }

    private static void mStartOrderSelectActivity() {
        Intent intent = new Intent(cAppExtension.context, InventoryorderSelectActivity.class);
        cAppExtension.activity.startActivity(intent);
    }

    private static void mStepFailed(String pvErrorMessageStr ){
        cUserInterface.pDoExplodingScreen(pvErrorMessageStr, cInventoryorder.currentInventoryOrder.getOrderNumberStr(), true, true );
    }

    private static void mShowOrderCloseDialog() {

        cUserInterface.pCheckAndCloseOpenDialogs();

        final AcceptRejectFragment acceptRejectFragment = new AcceptRejectFragment(cAppExtension.activity.getString(R.string.message_close_order),
                                                                                   cAppExtension.activity.getString(R.string.message_close_order_text), cAppExtension.activity.getString(R.string.message_cancel), cAppExtension.activity.getString(R.string.message_close), false);
        acceptRejectFragment.setCancelable(true);
       cAppExtension.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // show my popup
                acceptRejectFragment.show(cAppExtension.fragmentManager, ACCEPTREJECTFRAGMENT_TAG);
            }
        });
    }

    private static void mShowCommentsFragment(List<cComment> pvDataObl, String pvTitleStr) {

        cUserInterface.pCheckAndCloseOpenDialogs();

        Bundle bundle = new Bundle();
        bundle.putString(cPublicDefinitions.KEY_COMMENTHEADER, pvTitleStr);

        CommentFragment commentFragment = new CommentFragment(pvDataObl);
        commentFragment.setArguments(bundle);

        commentFragment.show(cAppExtension.fragmentManager , cPublicDefinitions.COMMENTFRAGMENT_TAG);
        cUserInterface.pPlaySound(R.raw.message, 0);
    }

    private static void mShowSending() {
        final SendingFragment sendingFragment = new SendingFragment();
        sendingFragment.setCancelable(true);
        cAppExtension.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // show my popup
                sendingFragment.show(cAppExtension.fragmentManager, SENDING_TAG);
            }
        });
    }

    private static void mShowSent() {
        Fragment fragment = cAppExtension.fragmentManager.findFragmentByTag(SENDING_TAG);
        if (fragment != null) {
            if (fragment instanceof SendingFragment) {
                ((SendingFragment) fragment).pShowFlyAwayAnimation();
            }
        }
    }

    private static void mShowNotSend(String pvErrorMessageStr) {
        Fragment fragment = cAppExtension.fragmentManager.findFragmentByTag(SENDING_TAG);
        if (fragment != null) {
            if (fragment instanceof SendingFragment) {
                ((SendingFragment) fragment).pShowCrashAnimation(pvErrorMessageStr);
            }
        }
    }

    private void mShowAcceptFragment(){

        cUserInterface.pCheckAndCloseOpenDialogs();

        final AcceptRejectFragment acceptRejectFragment = new AcceptRejectFragment(cAppExtension.activity.getString(R.string.message_close_order),
                cAppExtension.activity.getString(R.string.message_orderbusy_text),
                cAppExtension.activity.getString(R.string.message_finish_later), cAppExtension.activity.getString(R.string.message_close_now), false);
        acceptRejectFragment.setCancelable(true);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // show my popup
                acceptRejectFragment.show(cAppExtension.fragmentManager, ACCEPTREJECTFRAGMENT_TAG);
            }
        });
    }


    //End Region Private Methods

}
