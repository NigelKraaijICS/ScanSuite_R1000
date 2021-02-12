package nl.icsvertex.scansuite.Activities.Pick;

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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import ICS.Interfaces.iICSDefaultActivity;
import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cConnection;
import ICS.Utils.cRegex;
import ICS.Utils.cResult;
import ICS.Utils.cText;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Basics.BarcodeLayouts.cBarcodeLayout;
import SSU_WHS.Basics.Settings.cSetting;
import SSU_WHS.Basics.Users.cUser;
import SSU_WHS.Basics.Workplaces.cWorkplace;
import SSU_WHS.General.Comments.cComment;
import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.General.cPublicDefinitions;
import SSU_WHS.Picken.PickorderLines.cPickorderLine;
import SSU_WHS.Picken.PickorderLines.cPickorderLineAdapter;
import SSU_WHS.Picken.Pickorders.cPickorder;
import nl.icsvertex.scansuite.Activities.Ship.ShiporderLinesActivity;
import nl.icsvertex.scansuite.Activities.Sort.SortorderLinesActivity;
import nl.icsvertex.scansuite.Fragments.Dialogs.AcceptRejectFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.CommentFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.CurrentLocationFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.NothingHereFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.SendingFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.StepDoneFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.WorkplaceFragment;
import nl.icsvertex.scansuite.R;

public class PickorderLinesGeneratedActivity extends AppCompatActivity implements iICSDefaultActivity,  SwipeRefreshLayout.OnRefreshListener  {

    //Region Public Properties

    //End Region Public Properties

    //Region Private Properties

    //Region Views
    private ConstraintLayout pickorderLinesGeneratedContainer;

    private TextView quantityPickordersText;
    private ImageView imageButtonComments;

    private ImageView toolbarImage;
    private TextView toolbarTitle;
    private  TextView toolbarSubtext;

    private RecyclerView recyclerViewPickorderLinesGenerated;

    private ImageView imageButtonCloseOrder;

    private cPickorderLineAdapter pickorderLineAdapter;
    private cPickorderLineAdapter getPickorderLineAdapter(){
        if (this.pickorderLineAdapter == null) {
            this.pickorderLineAdapter = new cPickorderLineAdapter();
        }

        return this.pickorderLineAdapter;
    }

    //End Region Views

    //End Region Private Properties

    //Region Default Methods

    @Override
    protected void onCreate(Bundle pvSavedInstanceState) {
        super.onCreate(pvSavedInstanceState);
        setContentView(R.layout.activity_pickorderlines_generated);
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
        finish();
    }

    @Override
    public void onRefresh() {
        this.pFillLines();
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

        cBarcodeScan.pRegisterBarcodeReceiver(this.getClass().getSimpleName());
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

        this.pickorderLinesGeneratedContainer = findViewById(R.id.pickorderLinesGeneratedContainer);

        this.toolbarImage = findViewById(R.id.toolbarImage);
        this.toolbarTitle = findViewById(R.id.toolbarTitle);
        this.toolbarSubtext = findViewById(R.id.toolbarSubtext);

        this.quantityPickordersText = findViewById(R.id.quantityPickordersText);
        this.recyclerViewPickorderLinesGenerated = findViewById(R.id.recyclerViewPickorderLinesGenerated);

        this.imageButtonComments = findViewById(R.id.imageButtonComments);
        this.imageButtonCloseOrder = findViewById(R.id.imageButtonCloseOrder);
    }

    @Override
    public void mSetToolbar(String pvScreenTitleStr) {

        this.toolbarImage.setImageResource(R.drawable.ic_menu_pick_pf);
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

        this.pFillLines();

    }

    @Override
    public void mSetListeners() {
        this.mSetShowCommentListener();
        this.mSetCloseOrderListener();
    }

    @Override
    public void mInitScreen() {

        this.mShowComments();

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

    public  void pPicklineToResetSelected(cPickorderLine pvPickorderLine) {
        cPickorderLine.currentPickOrderLine = pvPickorderLine;
    }

    public  void pSetCurrentLocation(String pvCurrentLocationStr) {

        if (!cPickorder.currentPickOrder.pUpdateCurrentLocationBln(pvCurrentLocationStr)) {
            cUserInterface.pDoExplodingScreen(cAppExtension.context.getString(R.string.error_currentlocation_could_not_update), "", true, false);
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

    public  void pHandleScan(cBarcodeScan pvBarcodeScan) {

        cUserInterface.pCheckAndCloseOpenDialogs();

        //Check if we have scanned a BIN and check if there are not handled linesInt for this BIN
        if (cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(),cBarcodeLayout.barcodeLayoutEnu.BIN)) {

            String barcodewithoutPrefix = cRegex.pStripRegexPrefixStr(pvBarcodeScan.getBarcodeOriginalStr());
            //Search for BIN in Branch cache/via webservice
            cPickorder.currentPickOrder.currentBranchBin  = cUser.currentUser.currentBranch.pGetBinByCode(barcodewithoutPrefix);
            if ( cPickorder.currentPickOrder.currentBranchBin == null) {
                this.mDoUnknownScan(cAppExtension.context.getString(R.string.message_bin_unknown), barcodewithoutPrefix);
                return;
            }

            //we have a line to handle, so start Pick activity
            this.mStartPickActivity();
            return;
        }

        //unknown scan
        this.mDoUnknownScan(cAppExtension.context.getString(R.string.error_unknown_barcode), pvBarcodeScan.getBarcodeOriginalStr());

    }

    public void pCloseOrder() {

        //All lines are done

        if (!cConnection.isInternetConnectedBln()) {
            cConnection.pShowTryAgainDialog();
            this.imageButtonCloseOrder.setVisibility(View.INVISIBLE);
            return;
        }

        // Show close button, so user can close the order manually
        this.imageButtonCloseOrder.setVisibility(View.VISIBLE);

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
                return;
            }
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

        new Thread(new Runnable() {
            public void run() {
                mHandlePickFaseHandledAndDecideNextStep();
            }
        }).start();

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

    public  void pFillLines() {

        // Show that we are getting data
        cUserInterface.pShowGettingData();

        new Thread(new Runnable() {
            public void run() {
                mHandleFillLines();
            }
        }).start();

    }

    //End Region Public Methods

    //Region Private Methods

    private  void mChangeTabCounterText(String pvTextStr){
        this.quantityPickordersText.setText(pvTextStr);
    }

    private void mFillRecycler() {
        cAppExtension.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (cPickorder.currentPickOrder.pGetLinesHandledFromDatabaseObl().size() == 0) {
                    mNoLinesAvailable(true);
                    return;
                }

                mNoLinesAvailable(false);

                getPickorderLineAdapter().pFillData(cPickorder.currentPickOrder.pGetLinesHandledFromDatabaseObl());
                recyclerViewPickorderLinesGenerated.setHasFixedSize(false);
                recyclerViewPickorderLinesGenerated.setAdapter(getPickorderLineAdapter());
                recyclerViewPickorderLinesGenerated.setLayoutManager(new LinearLayoutManager(cAppExtension.context));
                recyclerViewPickorderLinesGenerated.setVisibility(View.VISIBLE);

                mChangeTabCounterText(cText.pDoubleToStringStr(cPickorder.currentPickOrder.pQuantityHandledDbl()));
                if (cPickorder.currentPickOrder.linesObl().size() > 0) {
                    imageButtonCloseOrder.setVisibility(View.VISIBLE);
                }
                else
                {
                    imageButtonCloseOrder.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void mNoLinesAvailable(Boolean pvEnabledBln) {

        if (!pvEnabledBln) {
            List<Fragment> fragments = cAppExtension.fragmentManager.getFragments();
            for (Fragment fragment : fragments) {
                if (fragment instanceof NothingHereFragment) {
                    FragmentTransaction fragmentTransaction = cAppExtension.fragmentManager.beginTransaction();
                    fragmentTransaction.remove(fragment);
                    fragmentTransaction.commit();
                }
            }
            return;
        }

        this.recyclerViewPickorderLinesGenerated.setVisibility(View.INVISIBLE);

        FragmentTransaction fragmentTransaction = cAppExtension.fragmentManager.beginTransaction();
        NothingHereFragment fragment = new NothingHereFragment();
        fragmentTransaction.replace(R.id.pickorderLinesGeneratedContainer, fragment);
        fragmentTransaction.commit();

        this.mChangeTabCounterText(cText.pDoubleToStringStr(cPickorder.currentPickOrder.pQuantityTotalDbl()));
    }

    private  void mHandleFillLines(){


        cResult hulpResult;

            //Delete the detail, so we can get them from the webservice
        if (!cPickorder.currentPickOrder.pDeleteDetailsBln()) {
            this.mStepFailed(cAppExtension.context.getString(R.string.error_couldnt_delete_details));
            return;
        }

        hulpResult =  cPickorder.currentPickOrder.pGetPickDetailsRst();
        if (!hulpResult.resultBln) {
            this.mStepFailed(hulpResult.messagesStr());
            return;
        }

        cUserInterface.pHideGettingData();

        this.mFillRecycler();


    }

    //End Region Public Methods

    //Region Private Methods

    private  boolean mPickFaseHandledBln(){

        cResult hulpResult;
        hulpResult = new cResult();
        hulpResult.resultBln = false;

        if (!cUser.currentUser.currentBranch.pGetShipBinsViaWebserviceBln()) {
            return false;
        }

        String shipBinStr = "";

        if (cUser.currentUser.currentBranch.shipBinsObl != null&& cUser.currentUser.currentBranch.shipBinsObl.size() > 0) {
            shipBinStr = cUser.currentUser.currentBranch.shipBinsObl.get(0).getBinCodeStr();
        }

        hulpResult = cPickorder.currentPickOrder.pPickGeneratedFaseHandledViaWebserviceRst(shipBinStr);

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

    private void mSetShowCommentListener() {
        this.imageButtonComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mShowCommentsFragment(cPickorder.currentPickOrder.pFeedbackAndPickCommentObl(),"");
            }
        });
    }

    private void mSetCloseOrderListener() {

        this.imageButtonCloseOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              pCloseOrder();

            }
        });
    }

    private   void mDoUnknownScan(String pvErrorMessageStr, String pvScannedBarcodeStr) {
        cUserInterface.pDoExplodingScreen(pvErrorMessageStr, pvScannedBarcodeStr, true, true);
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
    }


    private  void mShowSending() {
        final SendingFragment sendingFragment = new SendingFragment();
        sendingFragment.setCancelable(true);
        cAppExtension.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // show my popup
                sendingFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.SENDING_TAG);
            }
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
        cPickorder.currentPickOrder.pLockReleaseViaWebserviceBln(pvStepCodeEnu,pvWorkflowPickStepInt);
        cUserInterface.pCheckAndCloseOpenDialogs();
    }

    private  void mAskSort() {

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

    private  void mAskShip() {

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

        final AcceptRejectFragment acceptRejectFragment = new AcceptRejectFragment(cAppExtension.activity.getString(R.string.message_sure_leave_screen_title),
                cAppExtension.activity.getString(R.string.message_sure_leave_screen_text),cAppExtension.activity.getString(R.string.message_cancel),cAppExtension.activity.getString(R.string.message_leave), false);
        acceptRejectFragment.setCancelable(true);
        cAppExtension.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // show my popup
                acceptRejectFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.ACCEPTREJECTFRAGMENT_TAG);
            }
        });

    }

    private  void mStartOrderSelectActivity() {

        cAppExtension.activity.runOnUiThread(new Runnable() {
            public void run() {
                Intent intent = new Intent(cAppExtension.context, PickorderSelectActivity.class);
                cAppExtension.activity.startActivity(intent);
            }
        });

    }

    private  void mStartSortActivity() {

        cUserInterface.pShowGettingData();

        new Thread(new Runnable() {
            public void run() {
                mHandleStartSortActivity();
            }
        }).start();

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

        cAppExtension.activity.runOnUiThread(new Runnable() {
            public void run() {
                //Show Sort Activity
                Intent intent = new Intent(cAppExtension.context, SortorderLinesActivity.class);
                cAppExtension.activity.startActivity(intent);
            }
        });
    }

    private  void mStartShipActivity() {

        cUserInterface.pShowGettingData();

        new Thread(new Runnable() {
            public void run() {
                mHandleStartShipActivity();
            }
        }).start();

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
        Intent intent = new Intent(cAppExtension.context, PickorderPickGeneratedActivity.class);
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

    private void mStepFailed(String pvErrorMessageStr){
        cUserInterface.pDoExplodingScreen(pvErrorMessageStr, cPickorder.currentPickOrder.getOrderNumberStr(), true, true );
        cUserInterface.pCheckAndCloseOpenDialogs();
    }

    //End Region Private Methods

}
