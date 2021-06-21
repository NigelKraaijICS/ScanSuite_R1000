package nl.icsvertex.scansuite.Activities.Move;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.ViewCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

import ICS.Interfaces.iICSDefaultActivity;
import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cNoSwipeViewPager;
import ICS.Utils.cResult;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Basics.Settings.cSetting;
import SSU_WHS.Basics.Users.cUser;
import SSU_WHS.General.Comments.cComment;
import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.General.cPublicDefinitions;
import SSU_WHS.Move.MoveorderLines.cMoveorderLine;
import SSU_WHS.Move.Moveorders.cMoveorder;
import nl.icsvertex.scansuite.Activities.Receive.ReceiveLinesActivity;
import nl.icsvertex.scansuite.Fragments.Dialogs.AcceptRejectFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.CommentFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.PrintBinLabelFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.PrintItemLabelFragment;
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
    private SwitchCompat switchTodo;

    private DrawerLayout menuActionsDrawer;
    private NavigationView actionMenuNavigation;
    private ImageView imageButtonCloseOrder;
    public boolean closeOrderClickedBln = false;
    public static boolean activityInitializedBln;

    //End Region Private Properties

    //Region Default Methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movelines);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (cAppExtension.activity instanceof  MoveLinesActivity) {
            cBarcodeScan.pUnregisterBarcodeReceiver(this.getClass().getSimpleName());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.mActivityInitialize();
        cBarcodeScan.pRegisterBarcodeReceiver(this.getClass().getSimpleName());
        cUserInterface.pEnableScanner();
    }

    @Override
    protected void onPause() {
        super.onPause();
        cBarcodeScan.pUnregisterBarcodeReceiver(this.getClass().getSimpleName());
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu pvMenu) {
        getMenuInflater().inflate(R.menu.menu_intakeactions,pvMenu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu pvMenu) {

//        invalidateOptionsMenu();

        if (cSetting.GENERIC_PRINT_BINLABEL() && cMoveorderLine.currentMoveOrderLine != null){
            MenuItem item_print_bin = pvMenu.findItem(R.id.item_print_bin);
            item_print_bin.setVisible(true);
        }

        if (cSetting.GENERIC_PRINT_ITEMLABEL() && cMoveorderLine.currentMoveOrderLine != null){
            MenuItem item_print_item = pvMenu.findItem(R.id.item_print_item);
            item_print_item.setVisible(true);
        }

        return super.onPrepareOptionsMenu(pvMenu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem pvMenuItem) {


        DialogFragment selectedFragment = null;
        switch (pvMenuItem.getItemId()) {

            case android.R.id.home:

                this.pLeaveActivity();
                return true;

            case R.id.item_print_bin:
                selectedFragment = new PrintBinLabelFragment();
                break;

            case R.id.item_print_item:
                selectedFragment = new PrintItemLabelFragment();

            default:
                break;
        }

        // deselect everything
        int size = actionMenuNavigation.getMenu().size();
        for (int i = 0; i < size; i++) {
            actionMenuNavigation.getMenu().getItem(i).setChecked(false);
        }

        // set item as selected to persist highlight
        pvMenuItem.setChecked(true);
        // close drawer when item is tapped
        this.menuActionsDrawer.closeDrawers();

        if (selectedFragment != null) {
            selectedFragment.setCancelable(true);
            selectedFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.BINITEMSFRAGMENT_TAG);
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
        this.menuActionsDrawer = findViewById(R.id.menuActionsDrawer);
        this.actionMenuNavigation = findViewById(R.id.actionMenuNavigation);

        this.imageButtonCloseOrder = findViewById(R.id.imageButtonCloseOrder);
    }

    @Override
    public void mSetToolbar(String pvScreenTitleStr) {

        if (cUser.currentUser.currentAuthorisation.getCustomAuthorisation() != null) {
            this.toolbarImage.setImageBitmap(cUser.currentUser.currentAuthorisation.customImageBmp());
            this.toolbarTitle.setText(cUser.currentUser.currentAuthorisation.getCustomAuthorisation().getDescriptionStr());
        }
        else {
            this.toolbarImage.setImageResource(R.drawable.ic_menu_move);
            this.toolbarTitle.setText(pvScreenTitleStr);
        }

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

        if (!MoveLinesActivity.activityInitializedBln){
            this.moveLinesTabLayout.addTab(this.moveLinesTabLayout.newTab().setText(R.string.tab_moveorderline_take));
            this.moveLinesTabLayout.addTab(this.moveLinesTabLayout.newTab().setText(R.string.tab_moveorderline_place));
            MoveLinesActivity.activityInitializedBln = true;
       }


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

        new Thread(() -> mHandleScan(pvBarcodeScan)).start();

    }

    public void pChangeToolBarSubText(String pvTextStr){
        this.toolbarSubTitle.setText(pvTextStr);
    }

    public void pLeaveActivity(){

        if (cMoveorder.currentMoveOrder.linesObl() == null || cMoveorder.currentMoveOrder.linesObl().size() == 0 ) {
            cMoveorder.currentMoveOrder.pHandledViaWebserviceRst();
        }
        else
        {
            cMoveorder.currentMoveOrder.pLockReleaseViaWebserviceBln();
        }

        this.mStartOrderSelectActivity();

    }

    public void pDone() {

        ReceiveLinesActivity.closeOrderClickedBln = false;

        // Show that we are getting data
        cUserInterface.pShowGettingData();

        new Thread(this::mHandleClose).start();

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
        new Thread(this::mTryToCloseOrder).start();


    }

    private void mTryToCloseOrder() {

        cResult hulpResult;
        hulpResult = new cResult();
        hulpResult.resultBln = false;

        hulpResult = cMoveorder.currentMoveOrder.pHandledViaWebserviceRst();

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

        MoveLinesActivity.activityInitializedBln = false;
        cMoveorder.currentMoveOrder.pLockReleaseViaWebserviceBln();
        Intent intent = new Intent(cAppExtension.context, MoveorderSelectActivity.class);
        MoveorderSelectActivity.startedViaMenuBln = false;
        startActivity(intent);
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
        cAppExtension.activity.runOnUiThread(() -> {
            // show my popup
            sendingFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.SENDING_TAG);
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

        cAppExtension.activity.runOnUiThread(() -> {
            // show my popup
            acceptRejectFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.ACCEPTREJECTFRAGMENT_TAG);
        });
    }

    private void mSetShowCommentListener() {
        this.imageButtonComments.setOnClickListener(view -> mShowCommentsFragment(cMoveorder.currentMoveOrder.commentsObl(),""));
    }

    private void mSetQuickHelpListener() {
        this.quickHelpContainer.setOnClickListener(view -> {
            cUserInterface.pDoRotate(quickhelpIcon, 0);
            if (quickhelpText.getVisibility() == View.VISIBLE) {
                quickhelpText.setVisibility(View.GONE);
            }
            else {
                quickhelpText.setVisibility(View.VISIBLE);
            }
        });
    }

    private void mSetTodoListener() {
        this.switchTodo.setOnCheckedChangeListener((compoundButton, show) -> {

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


        });
    }

    private void mCloseOrderListener() {

        this.imageButtonCloseOrder.setOnClickListener(view -> mShowCloseOrderDialog(cAppExtension.activity.getString(R.string.message_no), cAppExtension.activity.getString(R.string.message_close)));
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
            cAppExtension.activity.runOnUiThread(() -> {
                // show my popup
                acceptRejectFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.ACCEPTREJECTFRAGMENT_TAG);
            });

    }



    //End Region Private Methods

}
