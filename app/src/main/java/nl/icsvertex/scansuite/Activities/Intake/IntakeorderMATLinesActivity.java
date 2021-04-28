package nl.icsvertex.scansuite.Activities.Intake;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

import java.util.List;

import ICS.Interfaces.iICSDefaultActivity;
import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cRegex;
import ICS.Utils.cResult;
import ICS.Utils.cText;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Basics.BarcodeLayouts.cBarcodeLayout;
import SSU_WHS.Basics.Settings.cSetting;
import SSU_WHS.Basics.Users.cUser;
import SSU_WHS.General.Comments.cComment;
import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.General.cPublicDefinitions;
import SSU_WHS.Intake.IntakeorderBarcodes.cIntakeorderBarcode;
import SSU_WHS.Intake.IntakeorderMATLineSummary.cIntakeorderMATSummaryLine;
import SSU_WHS.Intake.IntakeorderMATLineSummary.cIntakeorderMATSummaryLineAdapter;
import SSU_WHS.Intake.Intakeorders.cIntakeorder;
import nl.icsvertex.scansuite.Activities.IntakeAndReceive.IntakeAndReceiveSelectActivity;
import nl.icsvertex.scansuite.Activities.Receive.ReceiveLinesActivity;
import nl.icsvertex.scansuite.Fragments.Dialogs.AcceptRejectFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.CommentFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.PrintBinLabelFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.PrintItemLabelFragment;
import nl.icsvertex.scansuite.R;

public class IntakeorderMATLinesActivity extends AppCompatActivity implements iICSDefaultActivity {

    //Region Public Properties
    public static Fragment currentLineFragment;
    //End Region Public Properties

    //Region Private Properties

    //Region Views

    private TextView textViewChosenOrder;
    private ImageView imageButtonComments;

    private ImageView toolbarImage;
    private TextView toolbarTitle;
    private TextView toolbarSubTitle;

    private ImageView imageViewStart;
    private SearchView recyclerSearchView;
    private ImageView closeButton;
    private RecyclerView recyclerViewLines;

    private SwitchCompat switchDeviations;

    private DrawerLayout menuActionsDrawer;
    private NavigationView actionMenuNavigation;

    public enum InputType {
        UNKNOWN,
        BIN,
        ARTICLE
    }

    private InputType currentInputType = InputType.UNKNOWN;

    private ImageView imageButtonCloseOrder;

    private cIntakeorderMATSummaryLineAdapter intakeorderMATSummaryLineAdapter;
    private cIntakeorderMATSummaryLineAdapter getIntakeorderMATSummaryLineAdapter(){
        if (this.intakeorderMATSummaryLineAdapter == null) {
            this.intakeorderMATSummaryLineAdapter = new cIntakeorderMATSummaryLineAdapter();
        }

        return  this.intakeorderMATSummaryLineAdapter;
    }

    //End Region Views

    //End Region Private Properties

    //Region Default Methods

    @Override
    protected void onCreate(Bundle pvSavedInstanceState) {
        super.onCreate(pvSavedInstanceState);
        setContentView(R.layout.activity_intakeorder_lines);
        this.mActivityInitialize();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        //Set listeners here, so click listeners only work after activity is shown
        this.mSetListeners();
        this.switchDeviations.setChecked(cIntakeorder.currentIntakeOrder.showDeviationsBln);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cBarcodeScan.pRegisterBarcodeReceiver(this.getClass().getSimpleName());
        cUserInterface.pEnableScanner();
    }

    @Override
    protected void onPause() {
        try {
            cBarcodeScan.pUnregisterBarcodeReceiver(this.getClass().getSimpleName());
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
    public boolean onCreateOptionsMenu(Menu pvMenu) {
        getMenuInflater().inflate(R.menu.menu_intakeactions,pvMenu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu pvMenu) {

//        invalidateOptionsMenu();

        if (cSetting.GENERIC_PRINT_BINLABEL() && cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine != null){
            MenuItem item_print_bin = pvMenu.findItem(R.id.item_print_bin);
            item_print_bin.setVisible(true);
        }

        if (cSetting.GENERIC_PRINT_ITEMLABEL() && cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine != null){
            MenuItem item_print_item = pvMenu.findItem(R.id.item_print_item);
            item_print_item.setVisible(true);
        }

        return super.onPrepareOptionsMenu(pvMenu);
    }

    //End Region Default Methods

    //Region iICSDefaultActivity defaults

    @Override
    public void mActivityInitialize() {

        this.mSetAppExtensions();

        this.mFindViews();

        this.mSetToolbar(getResources().getString(R.string.screentitle_intakeorderlines));

        this.mFieldsInitialize();

        this.mInitScreen();

        cBarcodeScan.pRegisterBarcodeReceiver(this.getClass().getSimpleName());
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
        this.textViewChosenOrder = findViewById(R.id.textViewChosenOrder);
        this.imageButtonComments = findViewById(R.id.imageButtonComments);
        this.recyclerSearchView = findViewById(R.id.recyclerSearchView);
        this.closeButton =  this.recyclerSearchView.findViewById(R.id.search_close_btn);
        this.recyclerViewLines = findViewById(R.id.recyclerViewLines);
        this.imageViewStart = findViewById(R.id.imageViewStart);
        this.switchDeviations = findViewById(R.id.switchDeviations);
        this.imageButtonCloseOrder = findViewById(R.id.imageButtonCloseOrder);
        this.menuActionsDrawer = findViewById(R.id.menuActionsDrawer);
        this.actionMenuNavigation = findViewById(R.id.actionMenuNavigation);

    }

    @Override
    public void mSetToolbar(String pvScreenTitleStr) {
        this.toolbarImage.setImageResource(R.drawable.ic_menu_intake);
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

        ViewCompat.setTransitionName(this.textViewChosenOrder, cPublicDefinitions.VIEW_CHOSEN_ORDER);
        this.textViewChosenOrder.setText(cIntakeorder.currentIntakeOrder.getOrderNumberStr());
        this.imageButtonCloseOrder.setVisibility(View.VISIBLE);

    }

    @Override
    public void mSetListeners() {
        this.mSetRecyclerOnScrollListener();
        this.mSetSearchListener();
        this.mSetSearchCloseListener();
        this.mSetShowCommentListener();
        this.mSetStartLineListener();
        this.mSetDeviationsListener();
        this.mSetSendOrderListener();
    }

    @Override
    public void mInitScreen() {
        this.mShowComments();
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
        this.pLeaveActivity();
    }

    //End Region iICSDefaultActivity defaults

    //Region Public Methods

    public  void pIntakelineSelected(cIntakeorderMATSummaryLine pvIntakeorderMATSummaryLine) {
        cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine = pvIntakeorderMATSummaryLine;
    }

    public  void pHandleScan(cBarcodeScan pvBarcodeScan,
                                   Boolean pvLineSelectedBln) {

        cUserInterface.pCheckAndCloseOpenDialogs();
        cResult hulpResult;

        //BIN button has been pressed, so we already have a current line
        if (pvLineSelectedBln) {

            //Clear current barcodeStr
            cIntakeorderBarcode.currentIntakeOrderBarcode = null;
            hulpResult = cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.pSummaryLineBusyRst();
            if (!hulpResult.resultBln) {
                this.mStepFailed(hulpResult.messagesStr());
                cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine = null;
                return;
            }

            this.mStartStoreActivity();
            return;
        }

        //Check if we have scanned a BIN and check if there are not handled linesInt for this BIN
        if (cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(), cBarcodeLayout.barcodeLayoutEnu.BIN)) {

            if (!cIntakeorder.currentIntakeOrder.isBINScanPossible()) {
                //unknown scan
                cIntakeorder.currentIntakeOrder.currentBin= null;
                this.mDoUnknownScan(cAppExtension.context.getString(R.string.message_bin_not_allowed), pvBarcodeScan.getBarcodeOriginalStr());
                return;
            }

            //Handle the BIN scan
            hulpResult = this.mHandleBINScan(cRegex.pStripRegexPrefixStr(pvBarcodeScan.getBarcodeOriginalStr()));

            //Something went wrong, so show message and stop
            if (!hulpResult.resultBln) {
                this.mDoUnknownScan(hulpResult.messagesStr(), pvBarcodeScan.getBarcodeOriginalStr());
                this.mFillRecycler(cIntakeorderMATSummaryLine.sortedMATSummaryLinesObl());
                return;
            }

            //Hide the keyboard
            cUserInterface.pHideKeyboard();

            //Filter has been set in mHandleBINScan so we are donereturn;
            return;
        }

        //Check if we have scanned an ARTICLE and check if there are not handled linesInt for this ARTICLE
        if (cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(), cBarcodeLayout.barcodeLayoutEnu.ARTICLE)) {



            //Handle the ARTICLE scan
            hulpResult = this.mHandleArticleScan(pvBarcodeScan);

            //Something went wrong, so show message and stop
            if (!hulpResult.resultBln) {
                this.mDoUnknownScan(hulpResult.messagesStr(), pvBarcodeScan.getBarcodeOriginalStr());
                this.mFillRecycler(cIntakeorderMATSummaryLine.sortedMATSummaryLinesObl());
                return;
            }

            //Hide the keyboard
            cUserInterface.pHideKeyboard();

            //Filter has been set in mHandleArticleScan so we are done
            return;

        }

        //unknown scan
        this.mDoUnknownScan(cAppExtension.context.getString(R.string.error_unknown_barcode), pvBarcodeScan.getBarcodeOriginalStr());

    }

    public void pDone() {

        // Show that we are getting data
        cUserInterface.pShowGettingData();

        new Thread(new Runnable() {
            public void run() {
                mHandleClose();
            }
        }).start();

    }

    public void pLeaveActivity() {

        cIntakeorder.currentIntakeOrder.pLockReleaseViaWebserviceBln();
        this.mStartOrderSelectActivity();
    }

    public  void pSetToolBarTitleWithCounters(final String pvTextStr){


        cAppExtension.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                toolbarSubTitle.setText(pvTextStr);

                //Close open dialogs, so keyboard will also close
                cUserInterface.pHideKeyboard();

                //Click to make even more sure that keyboard gets hidden
                toolbarSubTitle.performClick();
            }
        });


    }

    public void pStartLine(){
        IntakeOrderIntakeActivity.handledViaPropertysBln = false;
        cAppExtension.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                imageViewStart.performClick();
            }});
    }

    public  void pShowData(List<cIntakeorderMATSummaryLine> pvDataObl) {
        this.mFillRecycler(pvDataObl);
    }

    public  void pPasswordSuccess() {
        cBarcodeScan.pRegisterBarcodeReceiver(this.getClass().getSimpleName());
        this.mShowCloseOrderDialog(cAppExtension.activity.getString(R.string.message_leave), cAppExtension.activity.getString(R.string.message_close));
    }

    public  void pPasswordCancelled() {
        cBarcodeScan.pRegisterBarcodeReceiver(this.getClass().getSimpleName());
    }


    //End Region Public Methods

    //Region Private Methods

    private  cResult mHandleArticleScan(cBarcodeScan pvBarcodeScan) {

        cResult result = new cResult();
        result.resultBln = true;

        String searchStr;
        this.currentInputType = InputType.ARTICLE;

        //Check if this is a barcodeStr we already know
        cIntakeorderBarcode.currentIntakeOrderBarcode = cIntakeorder.currentIntakeOrder.pGetOrderBarcode(pvBarcodeScan);
        if (cIntakeorderBarcode.currentIntakeOrderBarcode == null) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.activity.getString(R.string.message_unknown_barcode));
            return result;
        }

        //Get all lines for this barcodeStr
        if (cIntakeorderBarcode.currentIntakeOrderBarcode.linesObl().size() == 0) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.activity.getString(R.string.message_no_lines_for_this_barcode));
            return result;
        }

        //Set the scanned barcodeStr, so we can raise quantity in next activity
        cIntakeorder.currentIntakeOrder.intakeorderBarcodeScanned = cIntakeorderBarcode.currentIntakeOrderBarcode;

        searchStr = cIntakeorderBarcode.currentIntakeOrderBarcode.getItemNoStr();

        if (!cIntakeorderBarcode.currentIntakeOrderBarcode.getVariantCodeStr().isEmpty()) {
            searchStr += ' ' + cIntakeorderBarcode.currentIntakeOrderBarcode.getVariantCodeStr();
        }


        this.closeButton.callOnClick();
        this.currentInputType = InputType.ARTICLE;
        this.recyclerSearchView.setQuery(searchStr, true);
        this.recyclerSearchView.callOnClick();

        //Article is known and also not handled, so everything is fine
        this.currentInputType = InputType.UNKNOWN;
        return result;
    }

    private cResult mHandleBINScan(String pvBinCodeStr) {

        cResult result = new cResult();
        result.resultBln = true;

        this.currentInputType = InputType.BIN;

        //Get the current BIN
        cIntakeorder.currentIntakeOrder.currentBin =  cUser.currentUser.currentBranch.pGetBinByCode(pvBinCodeStr);
        if (  cIntakeorder.currentIntakeOrder.currentBin  == null) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.activity.getString(R.string.message_bin_unknown,pvBinCodeStr));
            return result;
        }

        List<cIntakeorderMATSummaryLine> hulpObl = cIntakeorderMATSummaryLine.pGetSummaryLinesWithBINCode(cIntakeorder.currentIntakeOrder.currentBin.getBinCodeStr());

        //If there are no lines for the current BIN, then refresh recycler
        if ( hulpObl  == null ||   hulpObl.size() == 0) {
            this.mDoUnknownScan(cAppExtension.activity.getString(R.string.message_no_lines_for_this_bin), pvBinCodeStr);
            cIntakeorder.currentIntakeOrder.currentBin= null;
            ReceiveLinesActivity.busyBln = false;
            result.resultBln = true;

            if (!cIntakeorder.currentIntakeOrder.showDeviationsBln) {
                this.getIntakeorderMATSummaryLineAdapter().pFillData(cIntakeorderMATSummaryLine.sortedMATSummaryLinesObl());
            } else {
                this.getIntakeorderMATSummaryLineAdapter().pShowDeviations();
            }
            return result;
        }

        this.recyclerSearchView.setQuery(pvBinCodeStr, true);
        this.recyclerSearchView.callOnClick();

        this.currentInputType = InputType.UNKNOWN;
        return result;
    }

    private  boolean mTryToCloseOrderBln() {

        cResult hulpResult;
        hulpResult = new cResult();
        hulpResult.resultBln = false;

        hulpResult = cIntakeorder.currentIntakeOrder.pMATHandledViaWebserviceRst();

        //Everything was fine, so we are done
        if (hulpResult.resultBln) {
            return true;
        }

        //Something went wrong, but no further actions are needed, so ony show reason of failure
        if (hulpResult.activityActionEnu == cWarehouseorder.ActivityActionEnu.Unknown) {
            cUserInterface.pDoExplodingScreen(hulpResult.messagesStr(), "", true, true);
            return false;
        }

        //Something went wrong, the order has been deleted, so show comments and refresh
        if (hulpResult.activityActionEnu == cWarehouseorder.ActivityActionEnu.Hold) {

            //If we got any comments, show them
            if (cIntakeorder.currentIntakeOrder.pFeedbackCommentObl() != null && cIntakeorder.currentIntakeOrder.pFeedbackCommentObl().size() > 0) {
                //Process comments from webresult
                this.mShowCommentsFragment(cIntakeorder.currentIntakeOrder.pFeedbackCommentObl(), hulpResult.messagesStr());
            }

            return false;
        }

        return true;

    }

    private  void mHandleClose() {

        if (!this.mTryToCloseOrderBln()) {
            return;
        }

        this.mStartOrderSelectActivity();

    }

    private void mSetShowCommentListener() {
        this.imageButtonComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mShowCommentsFragment(cIntakeorder.currentIntakeOrder.pCommentObl(), "");
            }
        });
    }

    private void mSetDeviationsListener() {
        this.switchDeviations.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean show) {

                if (switchDeviations.isChecked()) {
                    cIntakeorder.currentIntakeOrder.showDeviationsBln = true;
                    getIntakeorderMATSummaryLineAdapter().pShowDeviations();
                }
                else {
                    cIntakeorder.currentIntakeOrder.showDeviationsBln = false;
                    getIntakeorderMATSummaryLineAdapter().pFillData(cIntakeorderMATSummaryLine.sortedMATSummaryLinesObl());
                }
            }
        });
}

    private void mSetStartLineListener() {
        this.imageViewStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pHandleScan(null,true);
            }
        });
    }

    private void mSetSendOrderListener() {

        this.imageButtonCloseOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //do we need an administrator for this?
                if (!cSetting.RECEIVE_STORE_DEVIATIONS_PASSWORD().isEmpty() && cIntakeorderMATSummaryLine.totalItemsDifference() > 0 ) {
                    cUserInterface.pShowpasswordDialog(getString(R.string.supervisor_password_header), getString(R.string.supervisor_deviations_password_text), false);
                    return;
                }

                mShowCloseOrderDialog(cAppExtension.activity.getString(R.string.message_leave), cAppExtension.activity.getString(R.string.message_close));
            }
        });
    }

    private  void mDoUnknownScan(String pvErrorMessageStr, String pvScannedBarcodeStr) {
        this.currentInputType = InputType.UNKNOWN;
        cUserInterface.pDoExplodingScreen(pvErrorMessageStr, pvScannedBarcodeStr, true, true);
    }

    private  void mShowCommentsFragment(List<cComment> pvDataObl, String pvTitleStr) {

        cUserInterface.pCheckAndCloseOpenDialogs();

        Bundle bundle = new Bundle();
        bundle.putString(cPublicDefinitions.KEY_COMMENTHEADER, pvTitleStr);

        CommentFragment commentFragment = new CommentFragment(pvDataObl);
        commentFragment.setArguments(bundle);

        commentFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.COMMENTFRAGMENT_TAG);
        cUserInterface.pPlaySound(R.raw.message, 0);
    }

    private  void mStepFailed(String pvErrorMessageStr) {
        cUserInterface.pDoExplodingScreen(pvErrorMessageStr, cIntakeorder.currentIntakeOrder.getOrderNumberStr(), true, true);
        cIntakeorder.currentIntakeOrder.pLockReleaseViaWebserviceBln();
        cUserInterface.pCheckAndCloseOpenDialogs();
    }

    private  void mStartOrderSelectActivity() {

        cAppExtension.activity.runOnUiThread(new Runnable() {
            public void run() {
                Intent intent = new Intent(cAppExtension.context, IntakeAndReceiveSelectActivity.class);
                IntakeAndReceiveSelectActivity.startedViaMenuBln = false;
                cAppExtension.activity.startActivity(intent);
            }
        });

    }

    private  void mShowComments() {

        if (cIntakeorder.currentIntakeOrder.pCommentObl() == null || cIntakeorder.currentIntakeOrder.pCommentObl().size() == 0) {
            this.imageButtonComments.setVisibility(View.INVISIBLE);
            return;
        }

        this.imageButtonComments.setVisibility(View.VISIBLE);

        //We already showed the comments
        if (cComment.commentsShownBln) {
            return;
        }

        this.mShowCommentsFragment(cIntakeorder.currentIntakeOrder.pCommentObl(), "");
        cComment.commentsShownBln = true;
    }

    private  void mShowCloseOrderDialog(String pvRejectStr,String pvAcceptStr) {

        cUserInterface.pCheckAndCloseOpenDialogs();
        String messageStr = "";
        if (cIntakeorderMATSummaryLine.totalItemsDifference() == 0 ) {
            messageStr = (cAppExtension.activity.getString(R.string.message_exactly_what_you_needed));
        }

        if (cIntakeorderMATSummaryLine.totalItems() > cIntakeorderMATSummaryLine.totalItemsHandled()) {
            messageStr =   cText.pDoubleToStringStr(cIntakeorderMATSummaryLine.totalItemsDifference()) + " LESS items";
        }

        if (cIntakeorderMATSummaryLine.totalItems() < cIntakeorderMATSummaryLine.totalItemsHandled()) {
            messageStr =   cText.pDoubleToStringStr(cIntakeorderMATSummaryLine.totalItemsDifference()) + " EXTRA items";
        }

        final AcceptRejectFragment acceptRejectFragment = new AcceptRejectFragment(cAppExtension.activity.getString(R.string.message_close_order),
                cAppExtension.activity.getString(R.string.message_close_storeorder_text,
                        cText.pDoubleToStringStr(cIntakeorderMATSummaryLine.totalItemsHandled()),
                        cText.pDoubleToStringStr(cIntakeorderMATSummaryLine.totalItems()), messageStr),
                pvRejectStr,
                pvAcceptStr ,
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

    private void mStartStoreActivity(){
        IntakeOrderIntakeActivity.handledViaPropertysBln = false;

        //we have a LINE to handle, so start Store activity
        Intent intent = new Intent(cAppExtension.context, IntakeOrderIntakeActivity.class);
        cAppExtension.activity.startActivity(intent);

    }

    private void mFillRecycler(List<cIntakeorderMATSummaryLine> pvDataObl) {

        if (pvDataObl.size() == 0) {
            this.imageViewStart.setVisibility(View.INVISIBLE);
            return;
        }

        this.imageViewStart.setVisibility(View.VISIBLE);

        //Show the recycler view
        this.recyclerViewLines.setHasFixedSize(false);
        this.recyclerViewLines.setAdapter( this.getIntakeorderMATSummaryLineAdapter());
        this.recyclerViewLines.setLayoutManager(new LinearLayoutManager(cAppExtension.context));
        this.recyclerViewLines.setVisibility(View.VISIBLE);
    }

    private void mSetRecyclerOnScrollListener() {
        this.recyclerViewLines.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView pvRecyclerView, int pvNewStateInt) {
                super.onScrollStateChanged(pvRecyclerView, pvNewStateInt);
            }
        });
    }

    private void mSetSearchListener() {
        //make whole view clickable
        this.recyclerSearchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View pvView) {

                cAppExtension.activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerSearchView.setIconified(false);
                    }
                });
            }
        });

//        query entered
        this.recyclerSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String pvQueryTextStr) {
                switch (currentInputType) {
                    case UNKNOWN:
                        getIntakeorderMATSummaryLineAdapter().pSetFilter(pvQueryTextStr, false);
                        return  true;

                    case ARTICLE:
                        getIntakeorderMATSummaryLineAdapter().pSetFilter(pvQueryTextStr, true);
                        return  true;

                    case BIN:
                        getIntakeorderMATSummaryLineAdapter().pSetBINFilter(pvQueryTextStr);
                        return  true;
                }

                return  true;
            }

            @Override
            public boolean onQueryTextChange(String pvQueryTextStr) {
             return  true;
            }
        });

    }

    private void mSetSearchCloseListener() {
        //make whole view clickable
        this.closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View pvView) {
                recyclerSearchView.setQuery("",false);
                recyclerSearchView.setIconified(true);
                currentInputType = InputType.UNKNOWN;
                cIntakeorder.currentIntakeOrder.currentBin = null;

                if (switchDeviations.isChecked()) {
                    cIntakeorder.currentIntakeOrder.showDeviationsBln = true;
                    getIntakeorderMATSummaryLineAdapter().pShowDeviations();
                }
                else {
                    cIntakeorder.currentIntakeOrder.showDeviationsBln = false;
                    getIntakeorderMATSummaryLineAdapter().pFillData(cIntakeorderMATSummaryLine.sortedMATSummaryLinesObl());
                }

            }
        });

        //query entered


    }

    //End Region Private Methods

    }



