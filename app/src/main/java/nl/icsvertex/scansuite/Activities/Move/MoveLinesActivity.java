package nl.icsvertex.scansuite.Activities.Move;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;

import java.util.List;

import ICS.Interfaces.iICSDefaultActivity;
import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cNoSwipeViewPager;
import ICS.Utils.cResult;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.General.Comments.cComment;
import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.General.cPublicDefinitions;
import SSU_WHS.Move.MoveOrders.cMoveorder;
import nl.icsvertex.scansuite.Activities.Receive.ReceiveLinesActivity;
import nl.icsvertex.scansuite.Fragments.Dialogs.AcceptRejectFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.CommentFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.SendingFragment;
import nl.icsvertex.scansuite.Fragments.Move.MoveLinesPlaceFragment;
import nl.icsvertex.scansuite.Fragments.Move.MoveLinesTakeFragment;
import nl.icsvertex.scansuite.PagerAdapters.MoveLinesPagerAdapter;
import nl.icsvertex.scansuite.R;


public class MoveLinesActivity extends AppCompatActivity implements iICSDefaultActivity {

    //Region Public Properties
    public Fragment currentLineFragment;
    //End Region Public Properties

    private  TextView textViewChosenOrder;
    private TabLayout moveLinesTabLayout;
    private cNoSwipeViewPager moveLinesViewpager;

    private ImageView imageButtonComments;
    private ImageView toolbarImage;
    private TextView toolbarTitle;
    private TextView toolbarSubTitle;

    private ConstraintLayout quickHelpContainer;
    private TextView quickhelpText;
    private ImageView quickhelpIcon;
    private Switch switchTodo;

    private ImageView imageButtonCloseOrder;
    public boolean closeOrderClickedBln = false;

    //End Region Private Properties

    //Region Default Methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movelines);
        cBarcodeScan.pRegisterBarcodeReceiver();
        this.mActivityInitialize();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (cAppExtension.activity instanceof  MoveLinesActivity) {
            cBarcodeScan.pUnregisterBarcodeReceiver();
        }
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

        this.mSetToolbar(getResources().getString(R.string.screentitle_moveorderlines));

        this.mFieldsInitialize();

        this.mInitScreen();

        this.mSetListeners();

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

        this.quickhelpText = findViewById(R.id.quickhelpText);
        this.quickhelpText.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        this.quickhelpText.setSingleLine(true);
        this.quickhelpText.setMarqueeRepeatLimit(5);
        this.quickhelpText.setSelected(true);

        this.quickHelpContainer = findViewById(R.id.quickHelpContainer);
        this.quickhelpIcon = findViewById(R.id.quickhelpIcon);
        this.switchTodo = findViewById(R.id.switchTodo);


        this.imageButtonCloseOrder = findViewById(R.id.imageButtonCloseOrder);
    }

    @Override
    public void mSetToolbar(String pvScreenTitleStr) {
        this.toolbarImage.setImageResource(R.drawable.ic_menu_move);
        this.toolbarTitle.setText(pvScreenTitleStr);
        this.toolbarTitle.setSelected(true);
        this.toolbarSubTitle.setSelected(true);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void mFieldsInitialize() {

        this.quickhelpText.setVisibility(View.GONE);
        this.switchTodo.setVisibility(View.GONE);

        this.imageButtonCloseOrder.setVisibility(View.INVISIBLE);
        if (cMoveorder.currentMoveOrder.placeLinesTodoObl().size() == 0 && cMoveorder.currentMoveOrder.takeLinesObl.size() > 0) {
            this.imageButtonCloseOrder.setVisibility(View.VISIBLE);
        }


        ViewCompat.setTransitionName(this.textViewChosenOrder, cPublicDefinitions.VIEW_CHOSEN_ORDER);
        this.textViewChosenOrder.setText(cMoveorder.currentMoveOrder.getOrderNumberStr());
        this.moveLinesTabLayout.addTab(this.moveLinesTabLayout.newTab().setText(R.string.tab_moveorderline_take));
        this.moveLinesTabLayout.addTab(this.moveLinesTabLayout.newTab().setText(R.string.tab_moveorderline_place));

        MoveLinesPagerAdapter moveLinesPagerAdapter = new MoveLinesPagerAdapter(this.moveLinesTabLayout.getTabCount());
        this.moveLinesViewpager.setAdapter(moveLinesPagerAdapter);

        this.moveLinesViewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(this.moveLinesTabLayout));
        this.moveLinesTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab pvTab) {
                moveLinesViewpager.setCurrentItem(pvTab.getPosition());
                mChangeSelectedTab(pvTab);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab pvTab) {
                cUserInterface.pKillAllSounds();
            }

            @Override
            public void onTabReselected(TabLayout.Tab pvTab) {

            }
        });

        if (cMoveorder.currentMoveOrder.currenLineModusEnu == cMoveorder.lineModusEnu.TAKE) {
            this.moveLinesViewpager.setCurrentItem(0);
            this.quickhelpText.setText(R.string.scan_bincode);
       }
        else {
            this.moveLinesViewpager.setCurrentItem(1);
            this.quickhelpText.setText(R.string.message_scan_bin_or_article);
            }

    }

    @Override
    public void mSetListeners() {
        this.mSetShowCommentListener();
        this.mSetQuickHelpListener();
        this.mSetTodoListener();
        this.mCloseOrderListener();
    }

    @Override
    public void mInitScreen() {

    }

    //End Region iICSDefaultActivity defaults

    //Region Public Methods

    public void pHandleScan(final cBarcodeScan pvBarcodeScan) {

        new Thread(new Runnable() {
            public void run() {
                mHandleScan(pvBarcodeScan);
            }
        }).start();

    }

    public void pChangeToolBarSubText(String pvTextStr){
        this.toolbarSubTitle.setText(pvTextStr);
    }

    public void pLeaveActivity(){

        cMoveorder.currentMoveOrder.pLockReleaseViaWebserviceBln();
        this.mStartOrderSelectActivity();

    }

    public void pDone() {

        ReceiveLinesActivity.closeOrderClickedBln = false;

        // Show that we are getting data
        cUserInterface.pShowGettingData();

        new Thread(new Runnable() {
            public void run() {
                mHandleClose();
            }
        }).start();

    }

    //End Region Public Methods

    //Region Private Methods

    private void mHandleScan(cBarcodeScan pvBarcodeScan){

        if (this.currentLineFragment instanceof  MoveLinesTakeFragment) {
            MoveLinesTakeFragment moveLinesTakeFragment = (MoveLinesTakeFragment)this.currentLineFragment;
            moveLinesTakeFragment.pHandleScan(pvBarcodeScan);
        }

        if (this.currentLineFragment instanceof MoveLinesPlaceFragment) {
            MoveLinesPlaceFragment moveLinesPlaceFragment = (MoveLinesPlaceFragment)this.currentLineFragment;
            moveLinesPlaceFragment.pHandleScan(pvBarcodeScan);
        }

    }

    private void mHandleClose() {

        mShowSending();
        new Thread(new Runnable() {
            public void run() {
                mTryToCloseOrder();
            }
        }).start();


    }

    private void mTryToCloseOrder() {

        cResult hulpResult;
        hulpResult = new cResult();
        hulpResult.resultBln = false;

        hulpResult = cMoveorder.currentMoveOrder.pOrderHandledViaWebserviceRst();

        //Everything was fine, so we are done
        if (hulpResult.resultBln) {
            this.mShowSent();
            this.mStartOrderSelectActivity();
            return;
        }

        //Something went wrong, but no further actions are needed, so ony show reason of failure
        if (hulpResult.activityActionEnu == cWarehouseorder.ActivityActionEnu.Unknown) {
            this.mShowSendFailed(hulpResult.messagesStr());
            return;
        }

        //Something went wrong, the order has been deleted, so show comments and refresh
        if (hulpResult.activityActionEnu == cWarehouseorder.ActivityActionEnu.Hold) {

            cUserInterface.pCheckAndCloseOpenDialogs();

            //If we got any comments, show them
            if (cMoveorder.currentMoveOrder.pFeedbackCommentObl() != null && cMoveorder.currentMoveOrder.pFeedbackCommentObl().size() > 0) {
                //Process comments from webresult
                this.mShowCommentsFragment(cMoveorder.currentMoveOrder.pFeedbackCommentObl(), hulpResult.messagesStr());
            }

            return;
        }

        this.mShowSent();
        this.mStartOrderSelectActivity();

    }

    private void mChangeSelectedTab(TabLayout.Tab pvTab) {

        switch (pvTab.getPosition()) {
            case 0:

                this.switchTodo.setVisibility(View.GONE);
                this.quickhelpText.setText(R.string.scan_bincode);
                break;
            case 1:

                this.switchTodo.setVisibility(View.VISIBLE);
                this.switchTodo.setChecked(cMoveorder.currentMoveOrder.showTodoBln);
                this.quickhelpText.setText(R.string.message_scan_bin_or_article);
                break;

            default:

        }
    }

    private void mStartOrderSelectActivity() {

        cMoveorder.currentMoveOrder.pLockReleaseViaWebserviceBln();
        Intent intent = new Intent(cAppExtension.context, MoveorderSelectActivity.class);
        MoveorderSelectActivity.startedViaMenuBln = false;

        cAppExtension.activity.startActivity(intent);
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

    private void mShowSent() {
        Fragment fragment = cAppExtension.fragmentManager.findFragmentByTag(cPublicDefinitions.SENDING_TAG);
        if (fragment != null) {
            if (fragment instanceof SendingFragment) {
                ((SendingFragment) fragment).pShowFlyAwayAnimation();
            }
        }
    }

    private void mShowSendFailed(String pvErrorMessageStr) {
        Fragment fragment = cAppExtension.fragmentManager.findFragmentByTag(cPublicDefinitions.SENDING_TAG);
        if (fragment != null) {
            if (fragment instanceof SendingFragment) {
                ((SendingFragment) fragment).pShowCrashAnimation(pvErrorMessageStr);
            }
        }
    }

    private void mShowAcceptFragment(){

        cUserInterface.pCheckAndCloseOpenDialogs();

        final AcceptRejectFragment acceptRejectFragment = new AcceptRejectFragment(cAppExtension.activity.getString(R.string.message_leave),
                cAppExtension.activity.getString(R.string.message_sure_leave_screen_text),
                cAppExtension.activity.getString(R.string.message_cancel), cAppExtension.activity.getString(R.string.message_leave), false);
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

    private void mSetQuickHelpListener() {
        this.quickHelpContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cUserInterface.pDoRotate(quickhelpIcon, 0);
                if (quickhelpText.getVisibility() == View.VISIBLE) {
                    quickhelpText.setVisibility(View.GONE);
                }
                else {
                    quickhelpText.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void mSetTodoListener() {
        this.switchTodo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean show) {

                cMoveorder.currentMoveOrder.showTodoBln = switchTodo.isChecked();


                if (cMoveorder.currentMoveOrder.showTodoBln) {
                    if (currentLineFragment instanceof MoveLinesPlaceFragment) {
                        MoveLinesPlaceFragment moveLinesPlaceFragment = (MoveLinesPlaceFragment)currentLineFragment;
                        moveLinesPlaceFragment.pShowTodo();
                        return;
                    }
                }

                if (currentLineFragment instanceof MoveLinesPlaceFragment) {
                    MoveLinesPlaceFragment moveLinesPlaceFragment = (MoveLinesPlaceFragment)currentLineFragment;
                    moveLinesPlaceFragment.pGetData(cMoveorder.currentMoveOrder.sortedPlaceLinesObl());
                }


            }
        });
    }

    private void mCloseOrderListener() {

        this.imageButtonCloseOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mShowCloseOrderDialog(cAppExtension.activity.getString(R.string.message_no), cAppExtension.activity.getString(R.string.message_close));
            }
        });
    }

    private void mShowCloseOrderDialog(String pvRejectStr, String pvAcceptStr) {

        cUserInterface.pCheckAndCloseOpenDialogs();

        this.closeOrderClickedBln = true;

            final AcceptRejectFragment acceptRejectFragment = new AcceptRejectFragment(cAppExtension.activity.getString(R.string.message_close_order),
                    cAppExtension.activity.getString(R.string.orderdone_text_default),
                    pvRejectStr,
                    pvAcceptStr,
                    false);

            acceptRejectFragment.setCancelable(true);
            cAppExtension.activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // show my popup
                    acceptRejectFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.ACCEPTREJECTFRAGMENT_TAG);
                }
            });

    }



    //End Region Private Methods

}
