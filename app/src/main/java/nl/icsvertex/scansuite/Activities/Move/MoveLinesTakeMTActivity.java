package nl.icsvertex.scansuite.Activities.Move;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
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
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

import java.util.List;

import ICS.Interfaces.iICSDefaultActivity;
import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cRegex;
import ICS.Utils.cResult;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Basics.BarcodeLayouts.cBarcodeLayout;
import SSU_WHS.Basics.Settings.cSetting;
import SSU_WHS.General.Comments.cComment;
import SSU_WHS.General.cPublicDefinitions;
import SSU_WHS.Move.MoveItemVariant.cMoveItemVariant;
import SSU_WHS.Move.Moveorders.cMoveorder;
import SSU_WHS.Move.MoveorderLines.cMoveorderLine;
import SSU_WHS.Move.MoveorderLines.cMoveorderLineAdapter;
import SSU_WHS.Move.MoveorderLines.cMoveorderLineRecyclerItemTouchHelper;
import nl.icsvertex.scansuite.Fragments.Dialogs.AcceptRejectFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.CommentFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.NothingHereFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.PrintBinLabelFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.PrintItemLabelFragment;
import nl.icsvertex.scansuite.R;


public class MoveLinesTakeMTActivity extends AppCompatActivity implements iICSDefaultActivity, cMoveorderLineRecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    //Region Public Properties
    //End Region Public Properties

    private ImageView imageButtonComments;
    private ImageView toolbarImage;
    private TextView toolbarTitle;
    private TextView toolbarSubTitle;

    private  TextView textViewChosenOrder;

    private ConstraintLayout currentBINView;
    private TextView textViewBin;

    private RecyclerView recyclerViewMoveLinesTake;

    private ConstraintLayout quickHelpContainer;
    private TextView quickhelpText;
    private ImageView quickhelpIcon;
    private SwitchCompat switchTodo;

    private ConstraintLayout bottomContainer;

    private DrawerLayout menuActionsDrawer;
    private NavigationView actionMenuNavigation;

    private cMoveorderLineAdapter moveorderLineAdapter;
    private cMoveorderLineAdapter getMoveorderLineAdapter() {
        if (this.moveorderLineAdapter == null) {
            this.moveorderLineAdapter = new cMoveorderLineAdapter();
        }

        return this.moveorderLineAdapter;
    }


    //End Region Private Properties

    //Region Default Methods

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        //Set listeners here, so click listeners only work after activity is shown
        this.mSetListeners();

        this.switchTodo.setChecked(cMoveorder.currentMoveOrder.showTodoBln);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movelines_take_mt);
        cBarcodeScan.pRegisterBarcodeReceiver(this.getClass().getSimpleName());
        this.mActivityInitialize();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (cAppExtension.activity instanceof MoveLinesTakeMTActivity) {
            cBarcodeScan.pUnregisterBarcodeReceiver(this.getClass().getSimpleName());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
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
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu pvMenu) {
        getMenuInflater().inflate(R.menu.menu_movemtactions,pvMenu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu pvMenu) {
        invalidateOptionsMenu();

        pvMenu.findItem(R.id.item_close_take).setVisible(false);


        if (cSetting.MOVE_MT_TAKE_ALLOW_END()) {
            pvMenu.findItem(R.id.item_close_take).setVisible(true);
        }
        else {

            if (cMoveorder.currentMoveOrder.takeLinesTodoObl("").size() <=0 )  {
                pvMenu.findItem(R.id.item_close_take).setVisible(true);
            }
        }
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
                this.mShowAcceptFragment();
                 return true;

            case R.id.item_close_take:
                this.mHandleTAKEDone();
                break;

            case R.id.item_print_bin:
                selectedFragment = new PrintBinLabelFragment();
                break;

            case R.id.item_print_item:
                selectedFragment = new PrintItemLabelFragment();


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

        this.mSetToolbar(getResources().getString(R.string.screentitle_moveorderlines_take));

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

        this.toolbarImage = findViewById(R.id.toolbarImage);
        this.toolbarTitle = findViewById(R.id.toolbarTitle);
        this.toolbarSubTitle = findViewById(R.id.toolbarSubtext);


        this.textViewChosenOrder = findViewById(R.id.textViewChosenOrder);
        this.imageButtonComments = findViewById(R.id.imageButtonComments);

        this.currentBINView = findViewById(R.id.currentBINContainer);
        this.textViewBin = findViewById(R.id.textViewBin);

        this.quickhelpText = findViewById(R.id.quickhelpText);
        this.quickhelpText.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        this.quickhelpText.setSingleLine(true);
        this.quickhelpText.setMarqueeRepeatLimit(5);
        this.quickhelpText.setSelected(true);

        this.menuActionsDrawer = findViewById(R.id.menuActionsDrawer);
        this.actionMenuNavigation = findViewById(R.id.actionMenuNavigation);

        this.bottomContainer = findViewById(R.id.bottomContainer);

        this.quickHelpContainer = findViewById(R.id.quickHelpContainer);
        this.quickhelpIcon = findViewById(R.id.quickhelpIcon);
        this.switchTodo = findViewById(R.id.switchTodo);

        this.recyclerViewMoveLinesTake = findViewById(R.id.recyclerViewMoveLinesTake);


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


        ViewCompat.setTransitionName(this.textViewChosenOrder, cPublicDefinitions.VIEW_CHOSEN_ORDER);
        this.textViewChosenOrder.setText(cMoveorder.currentMoveOrder.getOrderNumberStr());
         this.quickhelpText.setText(R.string.scan_bincode);

         this.mSetBINInfo();

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new cMoveorderLineRecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(this.recyclerViewMoveLinesTake);

    }

    @Override
    public void mSetListeners() {
        this.mSetShowCommentListener();
        this.mSetQuickHelpListener();
        this.mSetClearBINListener();
        this.mSetTodoListener();
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

    public  void pShowData(List<cMoveorderLine> pvDataObl) {


        if (pvDataObl.size() == 0) {
            this.mShowNoLinesIcon(true);
            return;
        }

        this.mFillRecyclerView(pvDataObl);
        this.mShowNoLinesIcon(false);

    }

    private  void mFillRecyclerView(final List<cMoveorderLine> pvDataObl) {

        if (pvDataObl == null || pvDataObl.size() == 0) {
            return;
        }

        cAppExtension.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                recyclerViewMoveLinesTake.setHasFixedSize(false);
                recyclerViewMoveLinesTake.setAdapter(getMoveorderLineAdapter());
                recyclerViewMoveLinesTake.setLayoutManager(new LinearLayoutManager(cAppExtension.context));
            }
        });

    }

    public void pChangeToolBarSubText(final String pvTextStr){


        cAppExtension.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                toolbarSubTitle.setText(pvTextStr);

            }
        });
    }

    public void pLeaveActivity(){

        cMoveorder.currentMoveOrder.pLockReleaseViaWebserviceBln();
        this.mStartOrderSelectActivity();

    }


    //End Region Public Methods

    //Region Private Methods

    private void mHandleScan(cBarcodeScan pvBarcodeScan){

        //Check if we have scanned a BIN and check if there are not handled linesInt for this BIN
        if (cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(),cBarcodeLayout.barcodeLayoutEnu.BIN)) {

            cMoveorder.currentMoveOrder.currentBranchBin  =  cMoveorder.currentMoveOrder.pGetBin(cRegex.pStripRegexPrefixStr(pvBarcodeScan.getBarcodeOriginalStr()));
            if (cMoveorder.currentMoveOrder.currentBranchBin == null) {
                this.mStepFailed(cAppExtension.activity.getString(R.string.message_bin_not_valid), (cRegex.pStripRegexPrefixStr(pvBarcodeScan.getBarcodeOriginalStr())));
                this.mSetBINInfo();
                return;
            }

            //Get open lines for this BIN
            List<cMoveorderLine> linesForBinObl = cMoveorder.currentMoveOrder.pGetTakeLinesForCurrentBin();
            if (linesForBinObl == null || linesForBinObl.size() == 0) {
                this.mStepFailed(cAppExtension.activity.getString(R.string.message_no_lines_for_this_bin), (cRegex.pStripRegexPrefixStr(pvBarcodeScan.getBarcodeOriginalStr())));
                cMoveorder.currentMoveOrder.currentBranchBin = null;
                this.mSetBINInfo();
                return;
            }

            //Show lines for this BIN
            getMoveorderLineAdapter().pShowTAKETodo(cMoveorder.currentMoveOrder.currentBranchBin.getBinCodeStr());
            this.mSetBINInfo();
            this.mSetQuickhelp();
            return;

        }

        if (cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(),cBarcodeLayout.barcodeLayoutEnu.ARTICLE)) {

            if (cMoveorder.currentMoveOrder.currentBranchBin  == null) {
                this.mStepFailed(cAppExtension.activity.getString(R.string.message_scan_bin_first), (cRegex.pStripRegexPrefixStr(pvBarcodeScan.getBarcodeOriginalStr())));
                return;
            }


            //Check if we already know the barcode
            cMoveorder.currentMoveOrder.moveorderBarcodeToHandle = cMoveorder.currentMoveOrder.pGetOrderBarcode(pvBarcodeScan);
            cMoveorder.currentMoveOrder.currentMoveorderBarcode =   cMoveorder.currentMoveOrder.moveorderBarcodeToHandle;

            //This is a new barcode
            if (cMoveorder.currentMoveOrder.moveorderBarcodeToHandle == null) {
                this.mStepFailed(cAppExtension.activity.getString(R.string.message_unknown_article), (cRegex.pStripRegexPrefixStr(pvBarcodeScan.getBarcodeOriginalStr())));
                return;
                }
            }

            cMoveorderLine.currentMoveOrderLine = cMoveorder.currentMoveOrder.pGetTakeLineForCurrentArticleAndBin(cMoveorder.currentMoveOrder.moveorderBarcodeToHandle);
           if (cMoveorderLine.currentMoveOrderLine == null) {
               this.mStepFailed(cAppExtension.activity.getString(R.string.message_no_lines_for_this_bin_and_article), (cRegex.pStripRegexPrefixStr(pvBarcodeScan.getBarcodeOriginalStr())));
               return;
           }


        //Get ItemVariant with the key of the barcode
        cMoveorder.currentMoveOrder.currentMoveItemVariant = cMoveItemVariant.allMoveItemVariantObl.get(cMoveorder.currentMoveOrder.moveorderBarcodeToHandle.getKeyStr());
        if (cMoveorder.currentMoveOrder.currentMoveItemVariant == null) {
            this.mStepFailed(cAppExtension.activity.getString(R.string.message_unknown_itemvariant), (cRegex.pStripRegexPrefixStr(pvBarcodeScan.getBarcodeOriginalStr())));
            return;
        }

        //Get article from cache
        cMoveorder.currentMoveOrder.currentArticle = cMoveorder.currentMoveOrder.articleObl.get(cMoveorder.currentMoveOrder.moveorderBarcodeToHandle.getItemNoAndVariantCodeStr());
        if (cMoveorder.currentMoveOrder.currentArticle == null) {
            this.mStepFailed(cAppExtension.activity.getString(R.string.message_unknown_article), (cRegex.pStripRegexPrefixStr(pvBarcodeScan.getBarcodeOriginalStr())));
            return;
        }

        this.mStartMoveLineTakeActivity();

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

    private  void mShowNoLinesIcon(final Boolean pvShowBln){

        cAppExtension.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                cUserInterface.pHideGettingData();

                mSetToolBarTitleWithCounters();


                if (pvShowBln) {

                    recyclerViewMoveLinesTake.setVisibility(View.INVISIBLE);
                    bottomContainer.setVisibility(View.INVISIBLE);

                    FragmentTransaction fragmentTransaction = cAppExtension.fragmentManager.beginTransaction();
                    NothingHereFragment fragment = new NothingHereFragment();
                    fragmentTransaction.replace(R.id.container, fragment);
                    fragmentTransaction.commit();

                    // We are done taking, so start PLACE
                    if (cMoveorder.currentMoveOrder.takeLinesToHandleObl().size() == 0) {
                        mHandleTAKEDone();
                    }

                    return;
                }


                recyclerViewMoveLinesTake.setVisibility(View.VISIBLE);
                bottomContainer.setVisibility(View.VISIBLE);


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

    private void mSetToolBarTitleWithCounters(){
        String toolBarStr = cAppExtension.activity.getString(R.string.lines) + ' ' +  cMoveorder.currentMoveOrder.takeLinesObl.size();
        this.toolbarSubTitle.setText(toolBarStr);
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

    private void mSetClearBINListener() {
        this.currentBINView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    cMoveorder.currentMoveOrder.currentBranchBin = null;
                    mSetBINInfo();

                    if (cMoveorder.currentMoveOrder.showTodoBln) {
                        getMoveorderLineAdapter().pShowTAKETodo("");
                    }
                    else{
                        getMoveorderLineAdapter().pFillData(cMoveorder.currentMoveOrder.takeLinesObl);
                    }
            }
        });
    }

    private void mSetTodoListener() {
        this.switchTodo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean show) {

                if (switchTodo.isChecked()) {
                    cMoveorder.currentMoveOrder.showTodoBln = true;

                    if (cMoveorder.currentMoveOrder.currentBranchBin == null) {
                        getMoveorderLineAdapter().pShowTAKETodo("");
                    }
                    else {
                        getMoveorderLineAdapter().pShowTAKETodo(cMoveorder.currentMoveOrder.currentBranchBin.getBinCodeStr());
                    }

                }
                else {
                    cMoveorder.currentMoveOrder.showTodoBln= false;
                    getMoveorderLineAdapter().pFillData(cMoveorder.currentMoveOrder.takeLinesObl);
                }
            }
        });
    }

    private  void mStepFailed(String pvErrorMessageStr, String pvScannedBarcodeStr) {
        cUserInterface.pDoExplodingScreen(pvErrorMessageStr, pvScannedBarcodeStr, true, true);
    }

    private void mSetBINInfo(){

        cAppExtension.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (cMoveorder.currentMoveOrder.currentBranchBin  == null) {
                    currentBINView.setVisibility(View.GONE);
                    textViewBin.setVisibility(View.GONE);
                    quickHelpContainer.setVisibility(View.VISIBLE);
                    switchTodo.setVisibility(View.VISIBLE);
                    return;
                }

                currentBINView.setVisibility(View.VISIBLE);
                textViewBin.setVisibility(View.VISIBLE);
                textViewBin.setText(cMoveorder.currentMoveOrder.currentBranchBin.getBinCodeStr());
                quickHelpContainer.setVisibility(View.GONE);
                switchTodo.setVisibility(View.GONE);
            }
        });



    }

    private void mSetQuickhelp(){
        cAppExtension.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                quickhelpText.setText(R.string.message_scan_article);
            }
        });

    }

    private  void mStartMoveLineTakeActivity(){

        //we have an article or bin to handle, so start move activity
        Intent intent = new Intent(cAppExtension.context, MoveLineTakeMTActivity.class);
        cAppExtension.activity.startActivity(intent);

    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder pvViewHolder, int pvDirectionInt, int pvPositionInt) {

        if (!(pvViewHolder instanceof  cMoveorderLineAdapter.MoveorderLineViewHolder)) {
            return;
        }

        if (cMoveorder.currentMoveOrder.showTodoBln) {
            cUserInterface.pShowSnackbarMessage(recyclerViewMoveLinesTake, cAppExtension.activity.getString(R.string.message_zero_lines_cant_be_reset),null,true);
            getMoveorderLineAdapter().pShowTAKETodo("");
            return;
        }

        cMoveorderLine.currentMoveOrderLine =  cMoveorder.currentMoveOrder.takeLinesObl.get(pvPositionInt);

        if (!cMoveorderLine.currentMoveOrderLine.handledBln) {
            cUserInterface.pShowSnackbarMessage(recyclerViewMoveLinesTake, cAppExtension.activity.getString(R.string.message_zero_lines_cant_be_reset),null,true);
            getMoveorderLineAdapter().pShowTAKETodo("");
            return;
        }

        //Reset the line
        this.mRemoveAdapterFromFragment();

    }

    private void mRemoveAdapterFromFragment() {

        cUserInterface.pShowGettingData();

        //remove the item from recyclerview
        cResult resultRst = cMoveorderLine.currentMoveOrderLine.pResetRst();
        if (!resultRst.resultBln) {
            cUserInterface.pHideGettingData();
            this.mStepFailed(resultRst.messagesStr(), "");
            return;
        }

        cUserInterface.pShowSnackbarMessage(recyclerViewMoveLinesTake, cAppExtension.activity.getString(R.string.message_line_reset_succesfull),null,true);

        //Renew data, so only current lines are shown
        if (!cMoveorder.currentMoveOrder.showTodoBln) {
            getMoveorderLineAdapter().pFillData(cMoveorder.currentMoveOrder.takeLinesObl);

        } else {
            getMoveorderLineAdapter().pShowTAKETodo("");
        }

        cUserInterface.pHideGettingData();

    }

    private void mHandleTAKEDone(){

        cResult result =   cMoveorder.currentMoveOrder.pCloseTakeMTRst();
        if (!result.resultBln) {
            this.mStepFailed(result.messagesStr(),"");
            return;
        }

        if (!cMoveorder.currentMoveOrder.pGetLinesMTViaWebserviceBln(true)) {
            this.mStepFailed(cAppExtension.activity.getString(R.string.error_get_lines_failed),"");
            return;
        }

        if (!cMoveorder.currentMoveOrder.pGetBINSViaWebserviceBln()) {
            this.mStepFailed(cAppExtension.activity.getString(R.string.error_get_bins_failed),"");
            return;
        }

        Intent intent = new Intent(cAppExtension.context, MoveLinesPlaceMTActivity.class);
        cAppExtension.activity.startActivity(intent);



    }

    //End Region Private Methods

}
