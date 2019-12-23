package nl.icsvertex.scansuite.Activities.Move;

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
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import ICS.Interfaces.iICSDefaultActivity;
import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cRegex;
import ICS.Utils.cResult;
import ICS.Utils.cText;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Basics.BarcodeLayouts.cBarcodeLayout;
import SSU_WHS.General.Comments.cComment;
import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.General.cPublicDefinitions;
import SSU_WHS.Move.MoveOrders.cMoveorder;
import SSU_WHS.Move.MoveorderLines.cMoveorderLine;

import nl.icsvertex.scansuite.Fragments.Dialogs.AcceptRejectFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.CommentFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.OrderDoneFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.SendingFragment;
import nl.icsvertex.scansuite.Fragments.Move.MoveorderLinesNotDoneFragment;
import nl.icsvertex.scansuite.PagerAdapters.MoveorderLinesPagerAdapter;
import nl.icsvertex.scansuite.R;

public class MoveorderLinesActivity extends AppCompatActivity implements iICSDefaultActivity {

    //Region Public Properties
    public static String VIEW_CHOSEN_ORDER = "detail:header:text";
    static final String ACCEPTREJECTFRAGMENT_TAG = "ACCEPTREJECTFRAGMENT_TAG";
    private static String SENDING_TAG = "SENDING_TAG";
    public static Fragment currentLineFragment;
    //End Region Public Properties

    //Region Private Properties

    //Region Views
    private TextView textViewChosenOrder;
    static private TextView quantityMoveordersText;
    static private TabLayout moveorderLinesTabLayout;
    static private ViewPager moveorderLinesViewpager;

    private MoveorderLinesPagerAdapter moveorderLinesPagerAdapter;
    static private ImageView imageButtonComments;

    private ImageView imageStart;

    private ImageView toolbarImage;
    private TextView toolbarTitle;


    //End Region Views

    //End Region Private Properties

    //Region Default Methods

    @Override
    protected void onCreate(Bundle pvSavedInstanceState) {
        super.onCreate(pvSavedInstanceState);
        setContentView(R.layout.activity_moveorderlines);
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
        cBarcodeScan.pRegisterBarcodeReceiver();
        cUserInterface.pEnableScanner();
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

    //End Region Default Methods

    //Region iICSDefaultActivity defaults

    @Override
    public void mActivityInitialize() {

        this.mSetAppExtensions();

        this.mFindViews();

        this.mSetToolbar(getResources().getString(R.string.screentitle_moveorderlines));

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
        this.quantityMoveordersText = findViewById(R.id.quantityMoveordersText);
        this.moveorderLinesTabLayout = findViewById(R.id.moveorderLinesTabLayout);
        this.moveorderLinesViewpager = findViewById(R.id.moveorderLinesViewpager);
        this.textViewChosenOrder = findViewById(R.id.textViewChosenOrder);
        this.imageButtonComments = findViewById(R.id.imageButtonComments);
        this.imageStart = findViewById(R.id.imageStart);
    }

    @Override
    public void mSetToolbar(String pvScreenTitleStr) {
        this.toolbarImage.setImageResource(R.drawable.ic_menu_move);
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

        ViewCompat.setTransitionName(textViewChosenOrder, VIEW_CHOSEN_ORDER);

        this.textViewChosenOrder.setText(cMoveorder.currentMoveOrder.getOrderNumberStr());


        moveorderLinesTabLayout.addTab(moveorderLinesTabLayout.newTab().setText(R.string.tab_moveorderline_notdone));
        //moveorderLinesTabLayout.addTab(moveorderLinesTabLayout.newTab().setText(R.string.tab_moveorderline_done));
        //moveorderLinesTabLayout.addTab(moveorderLinesTabLayout.newTab().setText(R.string.tab_moveorderline_total));

        moveorderLinesPagerAdapter = new MoveorderLinesPagerAdapter( moveorderLinesTabLayout.getTabCount());
        moveorderLinesViewpager.setAdapter(moveorderLinesPagerAdapter);
        moveorderLinesViewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(moveorderLinesTabLayout));
        moveorderLinesTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab pvTab) {
                moveorderLinesViewpager.setCurrentItem(pvTab.getPosition());
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
        this.mSetStartListener();
        this.mSetShowCommentListener();
    }

    @Override
    public void mInitScreen() {

        this.mShowComments();

        //Call this here, because this is called everytime the activiy gets shown
        this.mCheckAllDone();

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
        MoveorderLinesActivity.quantityMoveordersText.setText(pvTextStr);
    }

    public static void pMovelineSelected(cMoveorderLine pvMoveorderLine) {
        cMoveorderLine.currentMoveOrderLine = pvMoveorderLine;
        MoveorderLinesNotDoneFragment.pSetChosenBinCode();
    }

    public static void pMovelineToResetSelected(cMoveorderLine pvMoveorderLine) {
        cMoveorderLine.currentMoveOrderLine = pvMoveorderLine;
    }

    public static void pSetCurrentLocation(String pvCurrentLocationStr) {

        if (!cMoveorder.currentMoveOrder.pUpdateCurrentLocationBln(pvCurrentLocationStr)) {
            cUserInterface.pDoExplodingScreen(cAppExtension.context.getString(R.string.error_currentlocation_could_not_update), "", true, false);
            return;
        }

        //Check if we are done
        //todo error
//        if (cMoveorder.currentMoveOrder.pGetLinesNotHandledFromDatabasObl().size() > 0 ) {
//            MoveorderLinesActivity.mStartOrderSelectActivity();
//            return;
//        }

        //We are done
        MoveorderLinesActivity.pPickingDone("");

    }

    public static void pHandleScan(String pvScannedBarcodeStr) {

        cUserInterface.pCheckAndCloseOpenDialogs();

        cResult hulpResult;

        //Check if we have scanned a BIN and check if there are not handled lines for this BIN
        if (cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvScannedBarcodeStr, cBarcodeLayout.barcodeLayoutEnu.BIN)) {

            String barcodewithoutPrefix = cRegex.pStripRegexPrefixStr(pvScannedBarcodeStr);

            //todo: check Bin
            if (cMoveorderLine.currentMoveOrderLine == null) {
                mDoUnknownScan(cAppExtension.context.getString(R.string.nothing_more_todo_for_bin), barcodewithoutPrefix);
                return;
            }
        }

        //Check if we have scanned an ARTICLE and check if there are not handled linesInt for this ARTICLE
        if (cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvScannedBarcodeStr, cBarcodeLayout.barcodeLayoutEnu.ARTICLE)) {

            String barcodewithoutPrefix = cRegex.pStripRegexPrefixStr(pvScannedBarcodeStr);
        }

        //unknown scan
        mDoUnknownScan(cAppExtension.context.getString(R.string.error_unknown_barcode), pvScannedBarcodeStr);

    }

    public static void pHandleLineReset() {

//        cUserInterface.pCheckAndCloseOpenDialogs();
//
//        if (!cPickorderLine.currentPickOrderLine.pResetBln()) {
//            cUserInterface.pDoExplodingScreen(cAppExtension.context.getString(R.string.error_couldnt_reset_line), cPickorderLine.currentPickOrderLine.getLineNoInt().toString(), true, true );
//            return;
//        }
//        //Set the selected tab to 0, and after that change it back to this tab
//        MoveorderLinesActivity.moveorderLinesViewpager.setCurrentItem(0);
    }

    public static void pPickingDone(String pvCurrentLocationStr) {

//        //If we received a current location, then update it via the webservice and locally
//        //If we didn't receive a location, then we picked 0 items, so it's oke
//        if (!pvCurrentLocationStr.isEmpty()){
//            if (!cPickorder.currentPickOrder.pUpdateCurrentLocationBln(pvCurrentLocationStr)) {
//                cUserInterface.pDoExplodingScreen(cAppExtension.context.getString(R.string.error_currentlocation_could_not_update), "", true, false);
//                return;
//            }
//        }

        MoveorderLinesActivity.pClosePickAndDecideNextStep();

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

        cMoveorder.currentMoveOrder.pLockReleaseViaWebserviceBln(cWarehouseorder.StepCodeEnu.Move, cWarehouseorder.WorkflowMoveStepEnu.Move);
        mStartOrderSelectActivity();
        return;
    }

    //End Region Public Methods

    //Region Private Methods

    private static boolean mTryToCloseOrderBln(){

        cResult hulpResult;
        hulpResult = new cResult();
        hulpResult.resultBln = false;
//
//        hulpResult = cPickorder.currentPickOrder.pPickHandledViaWebserviceRst();
//
//        //Everything was fine, so we are done
//        if (hulpResult.resultBln) {
//            return true;
//        }
//
//        //Something went wrong, but no further actions are needed, so ony show reason of failure
//        if (!hulpResult.resultBln && hulpResult.activityActionEnu == cWarehouseorder.ActivityActionEnu.Unknown ) {
//            cUserInterface.pDoExplodingScreen(hulpResult.messagesStr(),"",true,true);
//            return  false;
//        }
//
//        //Something went wrong, the order has been deleted, so show comments and refresh
//        if (!hulpResult.resultBln && hulpResult.activityActionEnu == cWarehouseorder.ActivityActionEnu.Hold ) {
//
//            //If we got any comments, show them
//            if (cPickorder.currentPickOrder.pFeedbackCommentObl() != null && cPickorder.currentPickOrder.pFeedbackCommentObl().size() > 0 ) {
//                //Process comments from webresult
//                MoveorderLinesActivity.mShowCommentsFragment(cPickorder.currentPickOrder.pFeedbackCommentObl(), hulpResult.messagesStr());
//            }
//
//            return  false;
//        }

        return true;

    }

    private static void mHandleClosePickAndDecideNextStep(){

        if (!MoveorderLinesActivity.mTryToCloseOrderBln()) {
            return;
        }
    }

    private void mCheckAllDone() {

        // If not everything is done, then leave
        if (!this.mAllLinesDoneBln()) {
            return;
        }

        // If not everything is sent, then leave
        if (!this.mCheckAndSentLinesBln(true)) {
            return;
        }

//        // If there is no next step, then we are done
//        if (!cPickorder.currentPickOrder.PackAndShipNeededBln() && !cPickorder.currentPickOrder.isBPBln() && !cPickorder.currentPickOrder.isBCBln()) {
//            if (!cPickorder.currentPickOrder.pickActivityBinRequiredBln ||
//                    !cPickorder.currentPickOrder.getCurrentLocationStr().isEmpty()) {
//                // Show order done fragment
//                MoveorderLinesActivity.mShowOrderDoneFragment(false);
//                return;
//            }
//
//            // Show order done fragment
//            MoveorderLinesActivity.mShowOrderDoneFragment(true);
//        }
//
//        //We are done
//        MoveorderLinesActivity.pPickingDone("");

    }

    private void mChangeSelectedTab(TabLayout.Tab pvTab) {

        switch (pvTab.getPosition()) {
            case 0:
                MoveorderLinesActivity.pChangeTabCounterText(cText.pDoubleToStringStr(cMoveorder.currentMoveOrder.pQuantityNotHandledDbl()) + "/" + cText.pDoubleToStringStr(cMoveorder.currentMoveOrder.pQuantityTotalDbl()));
                break;
            case 1:
                MoveorderLinesActivity.pChangeTabCounterText(cText.pDoubleToStringStr(cMoveorder.currentMoveOrder.pQuantityHandledDbl()) + "/" + cText.pDoubleToStringStr(cMoveorder.currentMoveOrder.pQuantityTotalDbl()));
                break;
            case 2:
                MoveorderLinesActivity.pChangeTabCounterText(cText.pDoubleToStringStr(cMoveorder.currentMoveOrder.pQuantityTotalDbl()));
                break;
            default:

        }
    }

    private void mSetShowCommentListener() {
//        this.imageButtonComments.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mShowCommentsFragment(cPickorder.currentPickOrder.pPickCommentObl(),"");
//            }
//        });
    }

    private void mSetStartListener() {
        this.imageStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGotoScanActivity();
            }
        });
    }

    private static void mGotoScanActivity() {
        Intent intent = new Intent(cAppExtension.context, MoveorderTakeActivity.class);
        cAppExtension.activity.startActivity(intent);
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


    private static void mShowOrderDoneFragment(Boolean pvShowCurrentLocationBln) {

        cUserInterface.pPlaySound(R.raw.goodsound, null);

        final OrderDoneFragment orderDoneFragment = new OrderDoneFragment(pvShowCurrentLocationBln);
        orderDoneFragment.setCancelable(false);
        orderDoneFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.ORDERDONE_TAG);
    }

    private Boolean mCheckAndSentLinesBln(Boolean pvShowAnimationBln) {

//        final List<cPickorderLine> linesToSendObl = cPickorder.currentPickOrder.pGetLinesToSendFromDatabasObl();
//
//        // If there is nothing to send, then we are done
//        if (linesToSendObl.size() == 0 ) {
//            return  true;
//        }
//
//
//        if (pvShowAnimationBln) {
//            mShowSending();
//        }
//
//        ExecutorService executorService = Executors.newSingleThreadExecutor();
//        Callable<Boolean> callableBln = new Callable<Boolean>() {
//            @Override
//            public Boolean call() throws Exception {
//
//                // Try to send each line, if one failes then stop
//                for (cPickorderLine pickorderLine : linesToSendObl) {
//
//                    //Set the current line
//                    cPickorderLine.currentPickOrderLine = pickorderLine;
//
//                    //Try to send the line
//                    return cPickorderLine.currentPickOrderLine .pHandledBln();
//
//                }
//                return  false;
//            }
//        };
//
//
//        try {
//            Future<Boolean> callableResultBln = executorService.submit(callableBln);
//            Boolean hulpBln = callableResultBln.get();
//
//            if (!hulpBln) {
//
//                if (pvShowAnimationBln) {
//                    mShowNotSent();
//                }
//
//                return false;
//            }
//
//            if (pvShowAnimationBln) {
//                this.mShowSent();
//            }
//
//            return  true;
//
//        }
//        catch (InterruptedException | ExecutionException e) {
//
//        }

      return  false;


    }

    private Boolean mAllLinesDoneBln() {
//todo error
//        if (cPickorder.currentPickOrder.pGetLinesNotHandledFromDatabasObl().size() <= 0) {
//            return true;
//        }
        return  false;
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

    private static void mStepFailed(String pvErrorMessageStr, cWarehouseorder.StepCodeEnu pvStepCodeEnu, int pvWorkflowPickStepInt ){
//        cUserInterface.pDoExplodingScreen(pvErrorMessageStr, cPickorder.currentPickOrder.getOrderNumberStr(), true, true );
//        cPickorder.currentPickOrder.pLockReleaseViaWebserviceBln(pvStepCodeEnu,pvWorkflowPickStepInt);
//        cUserInterface.pCheckAndCloseOpenDialogs();
    }

    private void mTryToLeaveActivity(){

        cUserInterface.pCheckAndCloseOpenDialogs();

        final AcceptRejectFragment acceptRejectFragment = new AcceptRejectFragment(cAppExtension.activity.getString(R.string.message_sure_leave_screen_title),
                                                                                   cAppExtension.activity.getString(R.string.message_sure_leave_screen_text),
                                                                                    cAppExtension.activity.getString(R.string.message_cancel),
                                                                                    cAppExtension.activity.getString(R.string.message_leave),
                                                                                    false);
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
                Intent intent = new Intent(cAppExtension.context, MoveorderSelectActivity.class);
                cAppExtension.activity.startActivity(intent);
            }
        });

    }


    private static void mShowComments(){
        //todo error
//        if (cMoveorder.currentMoveOrder.pFeedbackCommentObl() == null || cMoveorder.currentMoveOrder.pFeedbackCommentObl().size() == 0) {
//            MoveorderLinesActivity.imageButtonComments.setVisibility(View.INVISIBLE);
//            return;
//        }
//
//        MoveorderLinesActivity.imageButtonComments.setVisibility(View.VISIBLE);
//
//        //We already showed the comments
//        if (cComment.commentsShownBln) {
//            return;
//        }
//
//        MoveorderLinesActivity.mShowCommentsFragment(cPickorder.currentPickOrder.pPickCommentObl(),"");
//        cComment.commentsShownBln = true;
    }

    //End Region Private Methods

}
