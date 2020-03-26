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
import SSU_WHS.Move.MoveOrders.cMoveorder;
import nl.icsvertex.scansuite.Fragments.Dialogs.AcceptRejectFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.CommentFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.SendingFragment;
import nl.icsvertex.scansuite.Fragments.Move.moveLinesPlaceFragment;
import nl.icsvertex.scansuite.Fragments.Move.moveLinesTakeFragment;
import nl.icsvertex.scansuite.PagerAdapters.MoveLinesPagerAdapter;
import nl.icsvertex.scansuite.R;

public class MoveLinesActivity extends AppCompatActivity implements iICSDefaultActivity {

    //Region Public Properties
    public static Fragment currentLineFragment;
    //End Region Public Properties

    //Region Private Properties
    private  TextView textViewChosenOrder;
    private TabLayout moveLinesTabLayout;
    private cNoSwipeViewPager moveLinesViewpager;

    private  MoveLinesPagerAdapter moveLinesPagerAdapter;
    private ImageView imageButtonComments;
    private  ImageView toolbarImage;
    private  TextView toolbarTitle;
    private  TextView toolbarSubTitle;

    //End Region Private Properties

    //Region Default Methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movelines);
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
    protected void onStop() {
        super.onStop();
        finish();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem pvMenuItem) {

        if (pvMenuItem.getItemId() == android.R.id.home) {

//todo: fix this
//            if (cInventoryorder.currentInventoryOrder.pGetBinsNotDoneFromDatabasObl().size() == 0) {
//                MoveLinesActivity.mShowAcceptFragment();
//            }
//            else {
//                MoveLinesActivity.mStartOrderSelectActivity();
//            }

            this.mStartOrderSelectActivity();

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
        this.toolbarSubTitle = findViewById(R.id.toolbarSubtext);
        this.moveLinesTabLayout = findViewById(R.id.moveLinesTabLayout);
        this.moveLinesViewpager = findViewById(R.id.moveLinesViewpager);
        this.textViewChosenOrder = findViewById(R.id.textViewChosenOrder);
        this.imageButtonComments = findViewById(R.id.imageButtonComments);
    }

    @Override
    public void mSetToolbar(String pvScreenTitleStr) {
        this.toolbarImage.setImageResource(R.drawable.ic_menu_move);
        this.toolbarTitle.setText(pvScreenTitleStr);
        this.toolbarTitle.setSelected(true);
        this.toolbarSubTitle.setSelected(true);
        this.pChangeToolBarSubText(cAppExtension.activity.getString(R.string.lines) + ' ' +  cText.pIntToStringStr(cMoveorder.currentMoveOrder.linesObl().size()) );
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void mFieldsInitialize() {
        ViewCompat.setTransitionName(this.textViewChosenOrder, cPublicDefinitions.VIEW_CHOSEN_ORDER);
        this.textViewChosenOrder.setText(cMoveorder.currentMoveOrder.getOrderNumberStr());
        this.moveLinesTabLayout.addTab(this.moveLinesTabLayout.newTab().setText(R.string.tab_moveorderline_take));
        this.moveLinesTabLayout.addTab(this.moveLinesTabLayout.newTab().setText(R.string.tab_moveorderline_place));

        this.moveLinesPagerAdapter = new MoveLinesPagerAdapter(this.moveLinesTabLayout.getTabCount());
        this.moveLinesViewpager.setAdapter(this.moveLinesPagerAdapter);

        this.moveLinesViewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(this.moveLinesTabLayout));
        this.moveLinesTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab pvTab) {
                moveLinesViewpager.setCurrentItem(pvTab.getPosition());
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

    }

    //End Region iICSDefaultActivity defaults

    //Region Public Methods

    public void pHandleScan(final cBarcodeScan pvBarcodeScan) {

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

    public  void pHandleAddBinFragmentDismissed() {
        cBarcodeScan.pRegisterBarcodeReceiver();
    }

    public  void pHandleOrderCloseClick() {
        this.mShowOrderCloseDialog();
    }

    public void pCloseOrder(){


        mShowSending();
        new Thread(new Runnable() {
            public void run() {
                mHandleCloseOrder();
            }
        }).start();



    }

    public  void pInventoryorderBinSelected() {

        if (MoveLinesActivity.currentLineFragment instanceof moveLinesTakeFragment || MoveLinesActivity.currentLineFragment instanceof moveLinesPlaceFragment) {
            this.mOpenMoveLineActivity();
        }

    }


    public  void pChangeToolBarSubText(String pvTextStr){
        this.toolbarSubTitle.setText(pvTextStr);
    }

    public  void pAcceptRejectDialogDismissed() {
        cMoveorder.currentMoveOrder.pLockReleaseViaWebserviceBln();
        this.mStartOrderSelectActivity();

    }

    //End Region Public Methods

    //Region Private Methods

    private void mHandleCloseOrder(){

        cResult hulpResult;
        hulpResult = new cResult();
        hulpResult.resultBln = false;

        //todo
//        if (!cInventoryorder.currentInventoryOrder.isGeneratedBln()) {
//            for (cInventoryorderBin inventoryorderBin : cInventoryorder.currentInventoryOrder.pGetBinsNotDoneFromDatabasObl()) {
//
//                if (!inventoryorderBin.pCloseBln(true)) {
//                    mShowNotSend(inventoryorderBin.getBinCodeStr() + ' ' +  cAppExtension.activity.getString(R.string.message_bin_close_failed));
//                    return;
//                }
//            }
//        }
//        else {
//            for (cInventoryorderBin inventoryorderBin : cInventoryorder.currentInventoryOrder.pGetBinsTotalFromDatabasObl()) {
//                if (!inventoryorderBin.pCloseBln(true)) {
//                    mShowNotSend(inventoryorderBin.getBinCodeStr() + ' ' +  cAppExtension.activity.getString(R.string.message_bin_close_failed));
//                    return;
//                }
//            }
//        }
//        hulpResult = cInventoryorder.currentInventoryOrder.pOrderHandledViaWebserviceRst();
//
//        //Everything was fine, so we are done
//        if (hulpResult.resultBln) {
//            mShowSent();
//            MoveLinesActivity.mStartOrderSelectActivity();
//            return;
//        }
//
//        //Something went wrong, but no further actions are needed, so ony show reason of failure
//        if (hulpResult.activityActionEnu == cWarehouseorder.ActivityActionEnu.Unknown ) {
//            mShowNotSend(hulpResult.messagesStr());
//            return;
//        }
//
//        //Something went wrong, the order has been deleted, so show comments and refresh
//        if ( hulpResult.activityActionEnu == cWarehouseorder.ActivityActionEnu.Hold ) {
//            mShowNotSend("");
//            //If we got any comments, show them
//            if (cInventoryorder.currentInventoryOrder.pFeedbackCommentObl() != null && cInventoryorder.currentInventoryOrder.pFeedbackCommentObl().size() > 0 ) {
//                //Process comments from webresult
//                MoveLinesActivity.mShowCommentsFragment(cInventoryorder.currentInventoryOrder.pFeedbackCommentObl(), hulpResult.messagesStr());
//            }
//        }

    }

    private  void mHandleScan(cBarcodeScan pvBarcodeScan){

        //Only BIN scans are allowed
        if (!cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(),cBarcodeLayout.barcodeLayoutEnu.BIN)) {
            mDoUnknownScan(cAppExtension.context.getString(R.string.error_unknown_location), pvBarcodeScan.getBarcodeOriginalStr());
            return;
        }

        //Strip the prefix if thats neccesary
        String barcodewithoutPrefix = cRegex.pStripRegexPrefixStr(pvBarcodeScan.getBarcodeOriginalStr());

        //todo
//        cResult hulpRst;
//        hulpRst = MoveLinesActivity.mCheckAndGetBinRst(barcodewithoutPrefix);
//        //Something went wrong, so show message and return
//        if (! hulpRst.resultBln) {
//            MoveLinesActivity.mStepFailed(hulpRst.messagesStr());
//            return;
//        }
//
//        //Everything went well, so we can open the BIN
//        MoveLinesActivity.pInventoryorderBinSelected();

    }

    private void mChangeSelectedTab(TabLayout.Tab pvTab) {
//todo
        switch (pvTab.getPosition()) {
            case 0:
//                MoveLinesActivity.pChangeTabCounterText( cAppExtension.activity.getString(R.string.tab_moveorderline_take) + " " + cAppExtension.activity.getString(R.string.lines) + " " +   cText.pIntToStringStr(cMoveorder.currentMoveOrder.takeLinesObl().size()));
                break;
            case 1:
//                MoveLinesActivity.pChangeTabCounterText( cAppExtension.activity.getString(R.string.tab_moveorderline_place) + " " + cAppExtension.activity.getString(R.string.lines) + " " +   cText.pIntToStringStr(cMoveorder.currentMoveOrder.placeLinesObl().size()));
                break;

            default:

        }
    }

    private  void mDoUnknownScan(String pvErrorMessageStr, String pvScannedBarcodeStr) {
        cUserInterface.pDoExplodingScreen(pvErrorMessageStr, pvScannedBarcodeStr, true, true);
    }

    private void mOpenMoveLineActivity() {

        cUserInterface.pCheckAndCloseOpenDialogs();

        //todo
//        final ViewGroup container = cAppExtension.activity.findViewById(R.id.container);
//
//        final Intent intent = new Intent(cAppExtension.context, InventoryorderBinActivity.class);
//        final View clickedBin = container.findViewWithTag(cInventoryorderBin.currentInventoryOrderBin.getBinCodeStr());
//        final View clickedBinImage = container.findViewWithTag(cInventoryorderBin.currentInventoryOrderBin.getBinCodeStr() + "_IMG");
//        if (clickedBin != null &&clickedBinImage != null) {
//            cAppExtension.activity.runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    ActivityOptionsCompat activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(cAppExtension.activity, new Pair<>(clickedBin, InventoryorderBinActivity.VIEW_CHOSEN_BIN),new Pair<>(clickedBinImage, InventoryorderBinActivity.VIEW_CHOSEN_BIN_IMAGE) );
//                    ActivityCompat.startActivity(cAppExtension.context,intent, activityOptions.toBundle());
//                }
//            });
//
//        }
//        else {
//            cAppExtension.activity.startActivity(intent);
//            cAppExtension.activity.finish();
//        }
    }

    private void mStartOrderSelectActivity() {

        cMoveorder.currentMoveOrder.pLockReleaseViaWebserviceBln();
        Intent intent = new Intent(cAppExtension.context, MoveorderSelectActivity.class);
        cAppExtension.activity.startActivity(intent);
    }

    private  void mStepFailed(String pvErrorMessageStr ){
        cUserInterface.pDoExplodingScreen(pvErrorMessageStr, cMoveorder.currentMoveOrder.getOrderNumberStr(), true, true );
    }

    private  void mShowOrderCloseDialog() {

        cUserInterface.pCheckAndCloseOpenDialogs();

        final AcceptRejectFragment acceptRejectFragment = new AcceptRejectFragment(cAppExtension.activity.getString(R.string.message_close_order),
                cAppExtension.activity.getString(R.string.message_close_order_text), cAppExtension.activity.getString(R.string.message_cancel), cAppExtension.activity.getString(R.string.message_close), false);
        acceptRejectFragment.setCancelable(true);
        cAppExtension.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // show my popup
                acceptRejectFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.ACCEPTREJECTFRAGMENT_TAG);
            }
        });
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

    private void mShowSending() {
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

    private  void mShowSent() {
        Fragment fragment = cAppExtension.fragmentManager.findFragmentByTag(cPublicDefinitions.SENDING_TAG);
        if (fragment != null) {
            if (fragment instanceof SendingFragment) {
                ((SendingFragment) fragment).pShowFlyAwayAnimation();
            }
        }
    }

    private  void mShowNotSend(String pvErrorMessageStr) {
        Fragment fragment = cAppExtension.fragmentManager.findFragmentByTag(cPublicDefinitions.SENDING_TAG);
        if (fragment != null) {
            if (fragment instanceof SendingFragment) {
                ((SendingFragment) fragment).pShowCrashAnimation(pvErrorMessageStr);
            }
        }
    }

    private  void mShowAcceptFragment(){

        cUserInterface.pCheckAndCloseOpenDialogs();

        final AcceptRejectFragment acceptRejectFragment = new AcceptRejectFragment(cAppExtension.activity.getString(R.string.message_close_order),
                cAppExtension.activity.getString(R.string.message_orderbusy_text),
                cAppExtension.activity.getString(R.string.message_finish_later), cAppExtension.activity.getString(R.string.message_close_now), false);
        acceptRejectFragment.setCancelable(true);

        cAppExtension.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // show my popup
                acceptRejectFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.ACCEPTREJECTFRAGMENT_TAG);
            }
        });
    }

    private void mSetShowCommentListener() {
        this.imageButtonComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mShowCommentsFragment(cMoveorder.currentMoveOrder.commentsObl(),"");
            }
        });
    }

    //End Region Private Methods

}
