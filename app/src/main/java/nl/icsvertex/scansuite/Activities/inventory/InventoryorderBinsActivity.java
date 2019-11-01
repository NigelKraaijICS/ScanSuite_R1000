package nl.icsvertex.scansuite.Activities.inventory;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
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
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

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
import SSU_WHS.Basics.Users.cUser;
import SSU_WHS.General.Comments.cComment;
import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.General.cPublicDefinitions;
import SSU_WHS.Inventory.InventoryOrders.cInventoryorder;
import SSU_WHS.Inventory.InventoryorderBins.cInventoryorderBin;
import SSU_WHS.Inventory.InventoryorderBins.cInventoryorderBinAdapter;
import SSU_WHS.Inventory.InventoryorderBins.cInventoryorderBinRecyclerItemTouchHelper;
import nl.icsvertex.scansuite.Fragments.dialogs.AcceptRejectFragment;
import nl.icsvertex.scansuite.Fragments.dialogs.CommentFragment;
import nl.icsvertex.scansuite.PagerAdapters.InventoryorderBinsPagerAdapter;
import nl.icsvertex.scansuite.R;

import static nl.icsvertex.scansuite.Activities.inventory.InventoryorderBinActivity.ACCEPTREJECTFRAGMENT_TAG;

public class InventoryorderBinsActivity extends AppCompatActivity implements iICSDefaultActivity, cInventoryorderBinRecyclerItemTouchHelper.RecyclerItemTouchHelperListener {
    public static String VIEW_CHOSEN_ORDER = "detail:header:text";


    //Region Public Properties
    public static Fragment currentBinFragment;
    //End Region Public Properties

    //Region Private Properties
    private TextView textViewChosenOrder;
    static private TextView quantityText;
    static private TabLayout inventoryorderBinsTabLayout;
    static private ViewPager inventoryorderBinsViewpager;

    private InventoryorderBinsPagerAdapter inventoryorderBinsPagerAdapter;
    static private ImageView imageButtonComments;

    private ImageView toolbarImage;
    private TextView toolbarTitle;
    private static TextView toolbarSubTitle;
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
            mTryToLeaveActivity();
            return true;
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
        this.toolbarSubTitle.setText(cAppExtension.activity.getString(R.string.items) + ' ' + cInventoryorder.currentInventoryOrder.pGetTotalCountDbl());
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

    public static void pHandleScan(final String pvScannedBarcodeStr) {

        //Close open Dialogs
        cUserInterface.pCheckAndCloseOpenDialogs();

        // Show that we are getting data
        cUserInterface.pShowGettingData();

        new Thread(new Runnable() {
            public void run() {
                mHandleScan(pvScannedBarcodeStr);
            }
        }).start();

    }

    public static void pHandleOrderCloseClick() {
        InventoryorderBinsActivity.mShowOrderCloseDialog();
    }

    public static void pCloseOrder(){

        cResult hulpResult;
        hulpResult = new cResult();
        hulpResult.resultBln = false;

        hulpResult = cInventoryorder.currentInventoryOrder.pOrderHandledViaWebserviceRst();

        //Everything was fine, so we are done
        if (hulpResult.resultBln) {
            InventoryorderBinsActivity.mStartOrderSelectActivity();
            return;
        }

        //Something went wrong, but no further actions are needed, so ony show reason of failure
        if (!hulpResult.resultBln && hulpResult.activityActionEnu == cWarehouseorder.ActivityActionEnu.Unknown ) {
            cUserInterface.pDoExplodingScreen(hulpResult.messagesStr(),"",true,true);
            return;
        }

        //Something went wrong, the order has been deleted, so show comments and refresh
        if (!hulpResult.resultBln && hulpResult.activityActionEnu == cWarehouseorder.ActivityActionEnu.Hold ) {

            //If we got any comments, show them
            if (cInventoryorder.currentInventoryOrder.pFeedbackCommentObl() != null && cInventoryorder.currentInventoryOrder.pFeedbackCommentObl().size() > 0 ) {
                //Process comments from webresult
                InventoryorderBinsActivity.mShowCommentsFragment(cInventoryorder.currentInventoryOrder.pFeedbackCommentObl(), hulpResult.messagesStr());
            }

            return;
        }

    }

    public static void pInventoryorderBinSelected() {
         InventoryorderBinsActivity.mOpenInventoryCountActivity();
    }

    public static void pChangeTabCounterText(String pvTextStr){
        InventoryorderBinsActivity.quantityText.setText(pvTextStr);
    }

    //End Region Public Methods

    //Region Private Methods

    private  static void mHandleScan(String pvScannedBarcodeStr){

        //Only BIN scans are allowed
        if (!cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvScannedBarcodeStr,cBarcodeLayout.barcodeLayoutEnu.BIN)) {
            mDoUnknownScan(cAppExtension.context.getString(R.string.error_unknown_barcode), pvScannedBarcodeStr);
            return;
        }

        //Strip the prefix if thats neccesary
        String barcodewithoutPrefix = cRegex.pStripRegexPrefixStr(pvScannedBarcodeStr);

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


            if (cInventoryorderBin.currentInventoryOrderBin.getStatusInt() == cWarehouseorder.InventoryBinStatusEnu.InventoryDone) {
                result.resultBln = false;
                result.pAddErrorMessage(cAppExtension.activity.getString(R.string.message_bin_already_closed));
                return  result;
            }

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
            result.pAddErrorMessage(cAppExtension.activity.getString(R.string.message_bin_not_added));
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

        Intent intent = new Intent(cAppExtension.context, InventoryorderBinActivity.class);
        View clickedBin = container.findViewWithTag(cInventoryorderBin.currentInventoryOrderBin.getBinCodeStr());
        View clickedBinImage = container.findViewWithTag(cInventoryorderBin.currentInventoryOrderBin.getBinCodeStr() + "_IMG");
        if (clickedBin != null &&clickedBinImage != null) {
            ActivityOptionsCompat activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(cAppExtension.activity, new Pair<>(clickedBin, InventoryorderBinActivity.VIEW_CHOSEN_BIN),new Pair<>(clickedBinImage, InventoryorderBinActivity.VIEW_CHOSEN_BIN_IMAGE) );
            ActivityCompat.startActivity(cAppExtension.context,intent, activityOptions.toBundle());
            return;
        }
        else {
            cAppExtension.activity.startActivity(intent);
            cAppExtension.activity.finish();
        }
    }

    private void mTryToLeaveActivity() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(cAppExtension.context);
        alertDialog.setTitle(R.string.message_sure_leave_screen_title);
        alertDialog.setMessage(getString(R.string.message_sure_leave_screen_text));
        alertDialog.setPositiveButton(R.string.message_sure_leave_screen_positive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface pvDialogInterface, int i) {

                if (!cInventoryorder.currentInventoryOrder.pLockReleaseViaWebserviceBln(cWarehouseorder.StepCodeEnu.Inventory, cWarehouseorder.WorkflowInventoryStepEnu.Inventory)) {
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
        Intent intent = new Intent(cAppExtension.context, InventoryorderSelectActivity.class);
        cAppExtension.activity.startActivity(intent);
    }

    private static void mStepFailed(String pvErrorMessageStr ){
        cUserInterface.pDoExplodingScreen(pvErrorMessageStr, cInventoryorder.currentInventoryOrder.getOrderNumberStr(), true, true );
        cUserInterface.pCheckAndCloseOpenDialogs();
    }

    private static void mShowOrderCloseDialog() {

        cUserInterface.pCheckAndCloseOpenDialogs();

        final AcceptRejectFragment acceptRejectFragment = new AcceptRejectFragment(cAppExtension.activity.getString(R.string.message_close_order),
                                                                                   cAppExtension.activity.getString(R.string.message_close_order_text));
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

    @Override
    public void onSwiped(RecyclerView.ViewHolder pvViewHolder, int pvDirectionInt, int pvPositionInt) {

        if (!(pvViewHolder instanceof cInventoryorderBinAdapter.InventoryorderBinViewHolder)) {
            return;
        }

        cInventoryorderBin.currentInventoryOrderBin = cInventoryorderBin.allInventoryorderBinsObl.get(pvPositionInt);

        //Remove the enviroment
        this.mRemoveAdapterFromFragment();

    }

    private void mRemoveAdapterFromFragment(){

        //remove the item from recyclerview
        cResult hulpRst = cInventoryorderBin.currentInventoryOrderBin.pResetRst();
        if (! hulpRst.resultBln) {
            cUserInterface.pDoExplodingScreen(hulpRst.messagesStr(),"",true,true);
            return;
        }

        //Renew data, so only current lines are shown
        InventoryorderBinActivity.pFillLines();

    }

    //End Region Private Methods

}
