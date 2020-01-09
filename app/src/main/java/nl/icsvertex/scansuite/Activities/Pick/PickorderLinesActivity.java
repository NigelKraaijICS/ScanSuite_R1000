package nl.icsvertex.scansuite.Activities.Pick;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import com.google.android.material.tabs.TabLayout;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import ICS.Interfaces.iICSDefaultActivity;
import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cConnection;
import ICS.Utils.cNoSwipeViewPager;
import ICS.Utils.cResult;
import ICS.Utils.cText;
import SSU_WHS.Basics.BarcodeLayouts.cBarcodeLayout;
import SSU_WHS.Basics.Settings.cSetting;
import SSU_WHS.Basics.ShippingAgentServiceShippingUnits.cShippingAgentServiceShippingUnit;
import SSU_WHS.Basics.ShippingAgentServices.cShippingAgentService;
import SSU_WHS.Basics.ShippingAgents.cShippingAgent;
import SSU_WHS.Basics.Workplaces.cWorkplace;
import SSU_WHS.General.Comments.cComment;
import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.Picken.PickorderBarcodes.cPickorderBarcode;
import SSU_WHS.Picken.PickorderLines.cPickorderLine;
import SSU_WHS.Picken.Pickorders.cPickorder;
import SSU_WHS.Picken.Shipment.cShipment;
import nl.icsvertex.scansuite.Activities.Ship.ShiporderLinesActivity;
import nl.icsvertex.scansuite.Activities.Sort.SortorderLinesActivity;
import ICS.cAppExtension;
import SSU_WHS.General.cPublicDefinitions;
import ICS.Utils.cRegex;
import ICS.Utils.cUserInterface;
import nl.icsvertex.scansuite.Fragments.Dialogs.AcceptRejectFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.CommentFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.CurrentLocationFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.OrderDoneFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.SendingFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.WorkplaceFragment;
import nl.icsvertex.scansuite.Fragments.Pick.PickorderLinesToPickFragment;
import nl.icsvertex.scansuite.PagerAdapters.PickorderLinesPagerAdapter;
import nl.icsvertex.scansuite.R;

public class PickorderLinesActivity extends AppCompatActivity implements iICSDefaultActivity {

    //Region Public Properties
    public static String VIEW_CHOSEN_ORDER = "detail:header:text";
    static final String ACCEPTREJECTFRAGMENT_TAG = "ACCEPTREJECTFRAGMENT_TAG";
    private static String SENDING_TAG = "SENDING_TAG";
    public static Fragment currentLineFragment;
    //End Region Public Properties

    //Region Private Properties

    //Region Views
    private static TextView textViewChosenOrder;
    static private TextView quantityPickordersText;
    static private TabLayout pickorderLinesTabLayout;
    static private cNoSwipeViewPager pickorderLinesViewPager;

    private static PickorderLinesPagerAdapter pickorderLinesPagerAdapter;
    static private ImageView imageButtonComments;

    private static ImageView toolbarImage;
    private static TextView toolbarTitle;



    //End Region Views

    //End Region Private Properties

    //Region Default Methods

    @Override
    protected void onCreate(Bundle pvSavedInstanceState) {
        super.onCreate(pvSavedInstanceState);
        setContentView(R.layout.activity_pickorderlines);
        this.mActivityInitialize();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        //Set listeners here, so click listeners only work after activity is shown
        this.mSetListeners();
    }

    @Override
    protected void onDestroy() {
                super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cBarcodeScan.pRegisterBarcodeReceiver();
        cConnection.pRegisterWifiChangedReceiver();
        cUserInterface.pEnableScanner();
    }

    @Override
    protected void onPause() {
        try {
            cBarcodeScan.pUnregisterBarcodeReceiver();
            cConnection.pUnregisterWifiChangedReceiver();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onPause();
    }

    //End Region Default Methods

    //Region iICSDefaultActivity defaults

    @Override
    public void mActivityInitialize() {

        this.mSetAppExtensions();

        this.mFindViews();

        this.mSetToolbar(getResources().getString(R.string.screentitle_pickorderlines));

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

        PickorderLinesActivity.toolbarImage = findViewById(R.id.toolbarImage);
        PickorderLinesActivity.toolbarTitle = findViewById(R.id.toolbarTitle);
        PickorderLinesActivity.quantityPickordersText = findViewById(R.id.quantityPickordersText);
        PickorderLinesActivity.pickorderLinesTabLayout = findViewById(R.id.pickorderLinesTabLayout);
        PickorderLinesActivity.pickorderLinesViewPager = findViewById(R.id.pickorderLinesViewpager);
        PickorderLinesActivity.textViewChosenOrder = findViewById(R.id.textViewChosenOrder);
        PickorderLinesActivity.imageButtonComments = findViewById(R.id.imageButtonComments);
    }

    @Override
    public void mSetToolbar(String pvScreenTitleStr) {
        PickorderLinesActivity.toolbarImage.setImageResource(R.drawable.ic_menu_pick);
        PickorderLinesActivity.toolbarTitle.setText(pvScreenTitleStr);
        PickorderLinesActivity.toolbarTitle.setSelected(true);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void mFieldsInitialize() {

        ViewCompat.setTransitionName(PickorderLinesActivity.textViewChosenOrder, PickorderLinesActivity.VIEW_CHOSEN_ORDER);

        PickorderLinesActivity.textViewChosenOrder.setText(cPickorder.currentPickOrder.getOrderNumberStr());
        PickorderLinesActivity.pickorderLinesTabLayout.addTab(PickorderLinesActivity.pickorderLinesTabLayout.newTab().setText(R.string.tab_pickorderline_topick));
        PickorderLinesActivity.pickorderLinesTabLayout.addTab(PickorderLinesActivity.pickorderLinesTabLayout.newTab().setText(R.string.tab_pickorderline_picked));
        PickorderLinesActivity.pickorderLinesTabLayout.addTab(PickorderLinesActivity.pickorderLinesTabLayout.newTab().setText(R.string.tab_pickorderline_total));

        PickorderLinesActivity.pickorderLinesPagerAdapter = new PickorderLinesPagerAdapter( PickorderLinesActivity.pickorderLinesTabLayout.getTabCount());
        PickorderLinesActivity.pickorderLinesViewPager.setAdapter(PickorderLinesActivity.pickorderLinesPagerAdapter);
        PickorderLinesActivity.pickorderLinesViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(PickorderLinesActivity.pickorderLinesTabLayout));
        PickorderLinesActivity.pickorderLinesTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab pvTab) {
                PickorderLinesActivity.pickorderLinesViewPager.setCurrentItem(pvTab.getPosition());
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
        this.mSetShowCommentListener();
    }

    @Override
    public void mInitScreen() {

        PickorderLinesActivity.mShowComments();

        //Call this here, because this is called everytime the activiy gets shown
        PickorderLinesActivity.pCheckAllDone();

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

    //End Region iICSDefaultActivity defaults

    //Region Public Methods

    public static void pChangeTabCounterText(String pvTextStr){
        PickorderLinesActivity.quantityPickordersText.setText(pvTextStr);
    }

    public static void pPicklineSelected(cPickorderLine pvPickorderLine) {
        cPickorderLine.currentPickOrderLine = pvPickorderLine;
        PickorderLinesToPickFragment.pSetChosenBinCode();
    }

    public static void pPicklineToResetSelected(cPickorderLine pvPickorderLine) {
        cPickorderLine.currentPickOrderLine = pvPickorderLine;
    }

    public static void pSetCurrentLocation(String pvCurrentLocationStr) {

        if (!cPickorder.currentPickOrder.pUpdateCurrentLocationBln(pvCurrentLocationStr)) {
            cUserInterface.pDoExplodingScreen(cAppExtension.context.getString(R.string.error_currentlocation_could_not_update), "", true, false);
            return;
        }

        //Check if we are done
        if (cPickorder.currentPickOrder.pGetLinesNotHandledFromDatabasObl().size() > 0 ) {
            PickorderLinesActivity.mStartOrderSelectActivity();
            return;
        }

        //We are done
        PickorderLinesActivity.pPickingDone("");

    }

    public static void pHandleScan(cBarcodeScan pvBarcodeScan, Boolean pvBinSelectedBln) {

        cUserInterface.pCheckAndCloseOpenDialogs();

        cResult hulpResult;

        //BIN button has been pressed, so we already have a current line
        if (pvBinSelectedBln) {

            //Clear current barcode
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
            PickorderLinesActivity.mStartPickActivity();
            return;
        }

        //Check if we have scanned a BIN and check if there are not handled linesInt for this BIN
        if (cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(),cBarcodeLayout.barcodeLayoutEnu.BIN)) {

            String barcodewithoutPrefix = cRegex.pStripRegexPrefixStr(pvBarcodeScan.getBarcodeOriginalStr());

            //Clear current barcode
            cPickorderBarcode.currentPickorderBarcode = null;

            cPickorderLine.currentPickOrderLine = cPickorder.currentPickOrder.pGetNetxLineToHandleForBin(barcodewithoutPrefix);
            if (cPickorderLine.currentPickOrderLine == null) {
                mDoUnknownScan(cAppExtension.context.getString(R.string.nothing_more_todo_for_bin), barcodewithoutPrefix);
                return;
            }

            hulpResult = cPickorderLine.currentPickOrderLine.pLineBusyRst();
            if (!hulpResult.resultBln) {
                mStepFailed(hulpResult.messagesStr(),cWarehouseorder.StepCodeEnu.Pick_Picking,cWarehouseorder.WorkflowPickStepEnu.PickPackAndShip);
                cPickorderLine.currentPickOrderLine = null;
                return;
            }

            //Set last selected index
            cPickorder.currentPickOrder.lastSelectedIndexInt = cPickorder.currentPickOrder.pGetIndexOfNotHandledLineInt(cPickorderLine.currentPickOrderLine);

            //we have a line to handle, so start Pick activity
            PickorderLinesActivity.mStartPickActivity();
            return;
        }

        //Check if we have scanned an ARTICLE and check if there are not handled linesInt for this ARTICLE
        if (cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(),cBarcodeLayout.barcodeLayoutEnu.ARTICLE)) {

            if (!cSetting.PICK_BIN_IS_ITEM()) {
                //unknown scan
                mDoUnknownScan(cAppExtension.context.getString(R.string.error_article_scan_not_allowed), pvBarcodeScan.getBarcodeOriginalStr());
                return;
            }

            String barcodewithoutPrefix = cRegex.pStripRegexPrefixStr(pvBarcodeScan.getBarcodeOriginalStr());

            // Article always had BIN, so ARTICLE is EQUAL to BIN
            cPickorderLine.currentPickOrderLine = cPickorder.currentPickOrder.pGetLineNotHandledByBarcode(barcodewithoutPrefix);
            if (cPickorderLine.currentPickOrderLine == null) {
                mDoUnknownScan(cAppExtension.context.getString(R.string.nothing_more_todo_for_article), barcodewithoutPrefix);
                return;
            }

            cPickorderBarcode.currentPickorderBarcode = cPickorderBarcode.pGetPickbarcodeViaBarcode(pvBarcodeScan);
            if (cPickorderBarcode.currentPickorderBarcode == null) {
                cPickorderLine.currentPickOrderLine = null;
                mDoUnknownScan(cAppExtension.context.getString(R.string.nothing_more_todo_for_article), barcodewithoutPrefix);
                return;
            }

            hulpResult = cPickorderLine.currentPickOrderLine.pLineBusyRst();
            if (!hulpResult.resultBln) {
                mStepFailed(hulpResult.messagesStr(),cWarehouseorder.StepCodeEnu.Pick_Picking,cWarehouseorder.WorkflowPickStepEnu.PickPackAndShip);
                cPickorderLine.currentPickOrderLine = null;
                return;
            }

            //Set last selected index
            cPickorder.currentPickOrder.lastSelectedIndexInt = cPickorder.currentPickOrder.pGetIndexOfNotHandledLineInt(cPickorderLine.currentPickOrderLine);

            //we have a line to handle, so start Pick activity
            PickorderLinesActivity.mStartPickActivity();
            return;
        }

        //unknown scan
        mDoUnknownScan(cAppExtension.context.getString(R.string.error_unknown_barcode), pvBarcodeScan.getBarcodeOriginalStr());

    }

     public static void pPickingDone(String pvCurrentLocationStr) {

        //If we received a current location, then update it via the webservice and locally
        //If we didn't receive a location, then we picked 0 items, so it's oke
        if (!pvCurrentLocationStr.isEmpty()){
            if (!cPickorder.currentPickOrder.pUpdateCurrentLocationBln(pvCurrentLocationStr)) {
                cUserInterface.pDoExplodingScreen(cAppExtension.context.getString(R.string.error_currentlocation_could_not_update), "", true, false);
                return;
            }
        }

        //Check if we need to select a workplaceStr
        if (!cPickorder.currentPickOrder.PackAndShipNeededBln() && !cPickorder.currentPickOrder.SortNeededBln()&&
                cPickorder.currentPickOrder.isPickSalesAskWorkplaceBln() && cWorkplace.currentWorkplace == null ) {
            mShowWorkplaceFragment();
            return;
        }

        PickorderLinesActivity.pClosePickAndDecideNextStep();

    }

    public static void pClosePickAndDecideNextStep(){

        // Show that we are getting data
        cUserInterface.pShowGettingData();

        new Thread(new Runnable() {
            public void run() {
                mHandleClosePickAndDecideNextStep();
            }
        }).start();

    }

    public static void pLeaveActivity(){

        cPickorder.currentPickOrder.pLockReleaseViaWebserviceBln(cWarehouseorder.StepCodeEnu.Pick_Picking,cWarehouseorder.WorkflowPickStepEnu.PickPicking);

        //If activity bin is not required, then don't show the fragment
        if (!cPickorder.currentPickOrder.isPickActivityBinRequiredBln() ||
            cPickorder.currentPickOrder.pQuantityHandledDbl() == 0 ||
            !cPickorder.currentPickOrder.getCurrentLocationStr().isEmpty()) {
            mStartOrderSelectActivity();
            return;
        }

        mShowCurrentLocationFragment();

    }

    //End Region Public Methods

    //Region Private Methods

    private static boolean mTryToCloseOrderBln(){

        cResult hulpResult;
        hulpResult = new cResult();
        hulpResult.resultBln = false;

        hulpResult = cPickorder.currentPickOrder.pPickHandledViaWebserviceRst();

        //Everything was fine, so we are done
        if (hulpResult.resultBln) {
            return true;
        }

        //Something went wrong, but no further actions are needed, so ony show reason of failure
        if ( hulpResult.activityActionEnu == cWarehouseorder.ActivityActionEnu.Unknown ) {
            cUserInterface.pDoExplodingScreen(hulpResult.messagesStr(),"",true,true);
            return  false;
        }

        //Something went wrong, the order has been deleted, so show comments and refresh
        if (hulpResult.activityActionEnu == cWarehouseorder.ActivityActionEnu.Hold ) {

            //If we got any comments, show them
            if (cPickorder.currentPickOrder.pFeedbackCommentObl() != null && cPickorder.currentPickOrder.pFeedbackCommentObl().size() > 0 ) {
                //Process comments from webresult
                PickorderLinesActivity.mShowCommentsFragment(cPickorder.currentPickOrder.pFeedbackCommentObl(), hulpResult.messagesStr());
            }

            return  false;
        }

        return true;

    }

    private static void mHandleClosePickAndDecideNextStep(){

        if (!PickorderLinesActivity.mTryToCloseOrderBln()) {
            return;
        }

        if (cPickorder.currentPickOrder.isBCBln() || cPickorder.currentPickOrder.isBPBln()) {
            mSortNextStep();
        }

        if (cPickorder.currentPickOrder.isBMBln() ||cPickorder.currentPickOrder.isPVBln() ) {
            mPackAndShipNextStap();
        }


    }

    public static void pCheckAllDone() {

        // If not everything is done, then leave
        if (!PickorderLinesActivity.mAllLinesDoneBln()) {
            return;
        }

        //All lines are done
        if (!cConnection.isInternetConnectedBln()) {
            cConnection.pShowTryAgainDialog();
            return;
        }

        // If not everything is sent, then leave
        if (!PickorderLinesActivity.mCheckAndSentLinesBln()) {
            return;
        }

        // If there is no next step, then we are done
        if (!cPickorder.currentPickOrder.PackAndShipNeededBln() && !cPickorder.currentPickOrder.isBPBln() && !cPickorder.currentPickOrder.isBCBln()) {
            if (!cPickorder.currentPickOrder.isPickActivityBinRequiredBln() ||
                    !cPickorder.currentPickOrder.getCurrentLocationStr().isEmpty()) {
                // Show order done fragment
                PickorderLinesActivity.mShowOrderDoneFragment(false);
                return;
            }

            // Show order done fragment
            PickorderLinesActivity.mShowOrderDoneFragment(true);
        }

        //Show Current Location fragment
        if (cPickorder.currentPickOrder.isPickActivityBinRequiredBln() &&
                cPickorder.currentPickOrder.getCurrentLocationStr().isEmpty()) {
            // Show order done fragment
            PickorderLinesActivity.mShowCurrentLocationFragment();
            return;
        }

        //We are done
        PickorderLinesActivity.pPickingDone("");

    }

    private void mChangeSelectedTab(TabLayout.Tab pvTab) {

        switch (pvTab.getPosition()) {
            case 0:
                PickorderLinesActivity.pChangeTabCounterText(cText.pDoubleToStringStr(cPickorder.currentPickOrder.pQuantityNotHandledDbl()) + "/" + cText.pDoubleToStringStr(cPickorder.currentPickOrder.pQuantityTotalDbl()));
                break;
            case 1:
                PickorderLinesActivity.pChangeTabCounterText(cText.pDoubleToStringStr(cPickorder.currentPickOrder.pQuantityHandledDbl()) + "/" + cText.pDoubleToStringStr(cPickorder.currentPickOrder.pQuantityTotalDbl()));
                break;
            case 2:
                PickorderLinesActivity.pChangeTabCounterText(cText.pDoubleToStringStr(cPickorder.currentPickOrder.pQuantityTotalDbl()));
                break;
            default:

        }
    }

    private void mSetShowCommentListener() {
        PickorderLinesActivity.imageButtonComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mShowCommentsFragment(cPickorder.currentPickOrder.pPickCommentObl(),"");
            }
        });
    }

    private  static void mDoUnknownScan(String pvErrorMessageStr, String pvScannedBarcodeStr) {
        cUserInterface.pDoExplodingScreen(pvErrorMessageStr, pvScannedBarcodeStr, true, true);
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

    private static void mShowCurrentLocationFragment() {

        cUserInterface.pCheckAndCloseOpenDialogs();
        cUserInterface.pPlaySound(R.raw.goodsound, null);

        final CurrentLocationFragment currentLocationFragment = new CurrentLocationFragment();
        currentLocationFragment.setCancelable(true);
        currentLocationFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.CURRENTLOCATION_TAG);
    }

    private static void mShowWorkplaceFragment() {

        WorkplaceFragment workplaceFragment = new WorkplaceFragment();
        workplaceFragment.setCancelable(false);
        workplaceFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.WORKPLACEFRAGMENT_TAG);
    }

    private static void mShowOrderDoneFragment(Boolean pvShowCurrentLocationBln) {

        cUserInterface.pPlaySound(R.raw.goodsound, null);

        final OrderDoneFragment orderDoneFragment = new OrderDoneFragment(pvShowCurrentLocationBln);
        orderDoneFragment.setCancelable(false);
        orderDoneFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.ORDERDONE_TAG);
    }

    private static Boolean mCheckAndSentLinesBln() {

        final List<cPickorderLine> linesToSendObl = cPickorder.currentPickOrder.pGetLinesToSendFromDatabasObl();

        // If there is nothing to send, then we are done
        if (linesToSendObl.size() == 0 ) {
            return  true;
        }

            mShowSending();


        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Callable<Boolean> callableBln = new Callable<Boolean>() {
            @Override
            public Boolean call() {

                // Try to send each line, if one failes then stop
                for (cPickorderLine pickorderLine : linesToSendObl) {

                    //Set the current line
                    cPickorderLine.currentPickOrderLine = pickorderLine;

                    //Try to send the line
                   cPickorderLine.currentPickOrderLine .pHandledBln();

                }
                return  true;
            }
        };


        try {
            Future<Boolean> callableResultBln = executorService.submit(callableBln);
            Boolean hulpBln = callableResultBln.get();

            if (!hulpBln) {
                    mShowNotSent();
                 return false;
            }
                PickorderLinesActivity.mShowSent();
            return  true;
        }
        catch (InterruptedException | ExecutionException ignored) {
        }
      return  false;


    }

    private static Boolean mAllLinesDoneBln() {
        return cPickorder.currentPickOrder.pGetLinesNotHandledFromDatabasObl().size() <= 0;
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

    private static void mShowNotSent() {
        Fragment fragment = cAppExtension.fragmentManager.findFragmentByTag(SENDING_TAG);
        if (fragment != null) {
            if (fragment instanceof SendingFragment) {
                ((SendingFragment) fragment).pShowCrashAnimation("");
            }
        }
    }

    private static void mShowSent() {
        Fragment fragment = cAppExtension.fragmentManager.findFragmentByTag(SENDING_TAG);
        if (fragment != null) {
            if (fragment instanceof SendingFragment) {
                ((SendingFragment) fragment).pShowFlyAwayAnimation();
            }
        }
    }

    private static void mStepFailed(String pvErrorMessageStr, cWarehouseorder.StepCodeEnu pvStepCodeEnu,int pvWorkflowPickStepInt ){
        cUserInterface.pDoExplodingScreen(pvErrorMessageStr, cPickorder.currentPickOrder.getOrderNumberStr(), true, true );
        cPickorder.currentPickOrder.pLockReleaseViaWebserviceBln(pvStepCodeEnu,pvWorkflowPickStepInt);
        cUserInterface.pCheckAndCloseOpenDialogs();
    }

    private static void mAskSort() {

        cAppExtension.activity.runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(cAppExtension.context);

                builder.setMessage(cAppExtension.context.getString(R.string.question_open_sort));
                builder.setTitle(cAppExtension.context.getString(R.string.question_open_sort_title));
                builder.setPositiveButton(R.string.open, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        mStartSortActivity();
                    }
                });
                builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mStartOrderSelectActivity();
                    }
                });
                builder.setIcon(R.drawable.ic_menu_sort);
                builder.show();
            }
        });
    }

    private static void mAskShip() {

        cAppExtension.activity.runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(cAppExtension.context);

                builder.setMessage(cAppExtension.context.getString(R.string.question_open_ship));
                builder.setTitle(cAppExtension.context.getString(R.string.question_open_ship_title));
                builder.setPositiveButton(R.string.open, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        mStartShipActivity();
                    }
                });
                builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mStartOrderSelectActivity();
                    }
                });
                builder.setIcon(R.drawable.ic_menu_ship);
                builder.show();
            }
        });



    }

    private static void mSortNextStep(){

        //If activity bin is not required, then don't show the fragment
        if ( cPickorder.currentPickOrder.pQuantityHandledDbl() == 0 ) {
            mStartOrderSelectActivity();
            return;
        }

        // If setting is not defined, then ask user
        if (cSetting.PICK_PACK_AND_SHIP_AUTO_START() == null) {
            PickorderLinesActivity.mAskSort();
            return;
        }

        // If settings is false, then go back to order select
        if (!cSetting.PICK_SORT_AUTO_START()) {
            mStartOrderSelectActivity();
            return;
        }

        // If settings is true, then go  to sort
        if (cSetting.PICK_SORT_AUTO_START()) {
            mStartSortActivity();
        }

    }

    private static void mPackAndShipNextStap(){

        //If activity bin is not required, then don't show the fragment
        if ( cPickorder.currentPickOrder.pQuantityHandledDbl() == 0 ) {
            mStartOrderSelectActivity();
            return;
        }

        //If Pack or Ship is not required, then we are dibe
        if (!cPickorder.currentPickOrder.PackAndShipNeededBln()) {
            mStartOrderSelectActivity();
            return;
        }

        // If setting is not defined, then ask user
        if (cSetting.PICK_PACK_AND_SHIP_AUTO_START() == null) {
            PickorderLinesActivity.mAskShip();
            return;
        }

        // If settings is false, then go back to order select
            if (!cSetting.PICK_PACK_AND_SHIP_AUTO_START()) {
                PickorderLinesActivity.mStartOrderSelectActivity();
                return;
            }

        // If settings is true, then go  to ship
            if (cSetting.PICK_PACK_AND_SHIP_AUTO_START()) {
                mStartShipActivity();
            }

    }

    private void mTryToLeaveActivity(){

        cUserInterface.pCheckAndCloseOpenDialogs();

        final AcceptRejectFragment acceptRejectFragment = new AcceptRejectFragment(cAppExtension.activity.getString(R.string.message_sure_leave_screen_title),
                cAppExtension.activity.getString(R.string.message_sure_leave_screen_text),cAppExtension.activity.getString(R.string.message_cancel),cAppExtension.activity.getString(R.string.message_leave), false);
        acceptRejectFragment.setCancelable(true);
        cAppExtension.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // show my popup
                acceptRejectFragment.show(cAppExtension.fragmentManager, ACCEPTREJECTFRAGMENT_TAG);
            }
        });

    }

    private static void mStartOrderSelectActivity() {

        cAppExtension.activity.runOnUiThread(new Runnable() {
            public void run() {
                Intent intent = new Intent(cAppExtension.context, PickorderSelectActivity.class);
                cAppExtension.activity.startActivity(intent);
            }
        });

    }

    private static void mStartSortActivity() {

        cUserInterface.pShowGettingData();

        new Thread(new Runnable() {
            public void run() {
                mHandleStartSortActivity();
            }
        }).start();

    }

    private static void mHandleStartSortActivity(){

        //Clear workplaceStr, so you have to select it in the next step
        cWorkplace.currentWorkplace = null;

        //Try to lock the pickorder
        if (!cPickorder.currentPickOrder.pLockViaWebserviceRst(cWarehouseorder.StepCodeEnu.Pick_Picking, cWarehouseorder.WorkflowPickStepEnu.PickSorting).resultBln) {
            mStepFailed(cAppExtension.context.getString(R.string.error_couldnt_lock_order),cWarehouseorder.StepCodeEnu.Pick_Picking,cWarehouseorder.WorkflowPickStepEnu.PickPackAndShip);
            return;
        }

        //Get sort linesInt
        if (!cPickorder.currentPickOrder.pGetLinesViaWebserviceBln(true,cWarehouseorder.PickOrderTypeEnu.SORT)) {
            mStepFailed(cAppExtension.context.getString(R.string.error_getting_sort_lines_failed),cWarehouseorder.StepCodeEnu.Pick_Picking,cWarehouseorder.WorkflowPickStepEnu.PickPackAndShip);
            return;
        }


        cAppExtension.activity.runOnUiThread(new Runnable() {
            public void run() {
                //Show Sort Activity
                Intent intent = new Intent(cAppExtension.context, SortorderLinesActivity.class);
                cAppExtension.activity.startActivity(intent);
            }
        });
    }

    private static void mStartShipActivity() {

        cUserInterface.pShowGettingData();

        new Thread(new Runnable() {
            public void run() {
                mHandleStartShipActivity();
            }
        }).start();

    }

    private static void mHandleStartShipActivity(){

        cResult hulpResult;

        if (!mTryToLockShipOrderBln()) {
            return;
        }


        hulpResult = PickorderLinesActivity.mGetShipOrderDetailsRst();
        if (!hulpResult.resultBln) {
            PickorderLinesActivity.mStepFailed(hulpResult.messagesStr(),cWarehouseorder.StepCodeEnu.Pick_PackAndShip, cWarehouseorder.WorkflowPickStepEnu.PickPackAndShip);
            return;
        }

        //Show ShipLines
        Intent intent = new Intent(cAppExtension.context, ShiporderLinesActivity.class);
        cAppExtension.activity.startActivity(intent);

    }

    private static void mStartPickActivity(){
        //we have a line to handle, so start Pick activity
        Intent intent = new Intent(cAppExtension.context, PickorderPickActivity.class);
        cAppExtension.activity.startActivity(intent);
    }

    private static void mShowComments(){

        if (cPickorder.currentPickOrder.pPickCommentObl() == null || cPickorder.currentPickOrder.pPickCommentObl().size() == 0) {
            PickorderLinesActivity.imageButtonComments.setVisibility(View.INVISIBLE);
            return;
        }

        PickorderLinesActivity.imageButtonComments.setVisibility(View.VISIBLE);

        //We already showed the comments
        if (cComment.commentsShownBln) {
            return;
        }

        PickorderLinesActivity.mShowCommentsFragment(cPickorder.currentPickOrder.pPickCommentObl(),"");
        cComment.commentsShownBln = true;
    }

    private static boolean mTryToLockShipOrderBln(){

        cResult hulpResult;
        hulpResult = cPickorder.currentPickOrder.pLockViaWebserviceRst(cWarehouseorder.StepCodeEnu.Pick_PackAndShip, cWarehouseorder.WorkflowPickStepEnu.PickPackAndShip);

        //Everything was fine, so we are done
        if (hulpResult.resultBln) {
            return true;
        }

        //Something went wrong, but no further actions are needed, so ony show reason of failure
        if (hulpResult.activityActionEnu == cWarehouseorder.ActivityActionEnu.Unknown ) {
            mStepFailed(hulpResult.messagesStr(),cWarehouseorder.StepCodeEnu.Pick_PackAndShip, cWarehouseorder.WorkflowPickStepEnu.PickPackAndShip);
            return  false;
        }

        //Something went wrong, the order has been deleted, so show comments and refresh
        if ( hulpResult.activityActionEnu == cWarehouseorder.ActivityActionEnu.Delete ||
              hulpResult.activityActionEnu == cWarehouseorder.ActivityActionEnu.NoStart ) {


            //If we got any comments, show them
            if (cPickorder.currentPickOrder.pFeedbackCommentObl() != null && cPickorder.currentPickOrder.pFeedbackCommentObl().size() > 0 ) {
                //Process comments from webresult
                PickorderLinesActivity.mShowCommentsFragment(cPickorder.currentPickOrder.pFeedbackCommentObl(), hulpResult.messagesStr());
            }

            return  false;
        }


        return true;

    }

    private static cResult mGetShipOrderDetailsRst() {

        cResult result;

        result = new cResult();
        result.resultBln = true;

        //Clear workplaceStr, so you have to select it in the next step
        cWorkplace.currentWorkplace = null;

        //Check all ShippingAgents
        if (cShippingAgent.allShippingAgentsObl == null || cShippingAgent.allShippingAgentsObl.size() == 0) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_no_shippingagents_available));
            return result;
        }

        //Check all ShippingAgents
        if (cShippingAgentService.allShippingAgentServicesObl == null || cShippingAgentService.allShippingAgentServicesObl.size() == 0) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_no_shippingagent_services_available));
            return result;
        }

        //Check all ShippingAgent Shipping Units
        if (cShippingAgentServiceShippingUnit.allShippingAgentServiceShippingUnitsObl == null || cShippingAgentServiceShippingUnit.allShippingAgentServiceShippingUnitsObl.size() == 0) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_no_shippingagent_services_units_available));
            return result;
        }

        //Clear all shipments
        cShipment.allShipmentsObl = null;

        // Get all linesInt, if zero than there is something wrong
        if (!cPickorder.currentPickOrder.pGetPackAndShipLinesViaWebserviceBln(true)) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_getting_pack_and_ship_lines_failed));
            return result;
        }

        // Get all packages
        if (!cPickorder.currentPickOrder.pGetShippingPackagedViaWebserviceBln(true)) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_getting_packages_failed));
            return result;
        }

        // Get all adresses, if system settings Pick Shipping Sales == false then don't ask web service
        if (!cPickorder.currentPickOrder.pGetAdressesViaWebserviceBln(true)) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_adresses_failed));
            return result;
        }

        // Get all comments
        if (!cPickorder.currentPickOrder.pGetCommentsViaWebserviceBln(true)) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_comments_failed));
            return result;
        }

        //If this is single article, then get barcodes
        if (cPickorder.currentPickOrder.isSingleArticleOrdersBln()) {
            if (!cPickorder.currentPickOrder.pGetBarcodesViaWebserviceBln(true)) {
                result.resultBln = false;
                result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_barcodes_failed));
                return result;
            }
        }

        return result;
    }

    //End Region Private Methods

}
