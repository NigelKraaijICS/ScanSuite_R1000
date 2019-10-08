package nl.icsvertex.scansuite.activities.pick;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import com.google.android.material.tabs.TabLayout;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ICS.Interfaces.iICSDefaultActivity;
import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cResult;
import ICS.Utils.cText;
import SSU_WHS.Basics.BarcodeLayouts.cBarcodeLayout;
import SSU_WHS.Basics.Settings.cSetting;
import SSU_WHS.Basics.Workplaces.cWorkplace;
import SSU_WHS.General.Comments.cComment;
import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.Picken.PickorderBarcodes.cPickorderBarcode;
import SSU_WHS.Picken.PickorderLines.cPickorderLine;
import SSU_WHS.Picken.Pickorders.cPickorder;
import nl.icsvertex.scansuite.activities.sort.SortorderLinesActivity;
import nl.icsvertex.scansuite.cAppExtension;
import SSU_WHS.General.cPublicDefinitions;
import ICS.Utils.cRegex;
import ICS.Utils.cUserInterface;
import nl.icsvertex.scansuite.fragments.dialogs.CommentFragment;
import nl.icsvertex.scansuite.fragments.dialogs.CurrentLocationFragment;
import nl.icsvertex.scansuite.fragments.dialogs.OrderDoneFragment;
import nl.icsvertex.scansuite.fragments.dialogs.WebserviceErrorFragment;
import nl.icsvertex.scansuite.fragments.dialogs.WorkplaceFragment;
import nl.icsvertex.scansuite.fragments.pick.PickorderLinesPickedFragment;
import nl.icsvertex.scansuite.fragments.pick.PickorderLinesToPickFragment;
import nl.icsvertex.scansuite.PickorderLinesPagerAdapter;
import nl.icsvertex.scansuite.R;

public class PickorderLinesActivity extends AppCompatActivity implements iICSDefaultActivity {

    //Region Public Properties
    public static String VIEW_CHOSEN_ORDER = "detail:header:text";
    public static Fragment currentLineFragment;
    //End Region Public Properties

    //Region Private Properties

    //Region Views
    private TextView textViewChosenOrder;
    static private TextView quantityPickordersText;
    static private TabLayout pickorderLinesTabLayout;
    static private ViewPager pickorderLinesViewPager;

    private PickorderLinesPagerAdapter pickorderLinesPagerAdapter;
    static private ImageView imageButtonComments;

    private ImageView toolbarImage;
    private TextView toolbarTitle;


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

    //End Region Default Methods

    //Region iICSDefaultActivity defaults

    @Override
    public void mActivityInitialize() {

        this.mSetAppExtensions();

        this.mFindViews();

        this.mSetViewModels();

        this.mSetSettings();

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

        this.toolbarImage = findViewById(R.id.toolbarImage);
        this.toolbarTitle = findViewById(R.id.toolbarTitle);
        this.quantityPickordersText = findViewById(R.id.quantityPickordersText);
        this.pickorderLinesTabLayout = findViewById(R.id.pickorderLinesTabLayout);
        this.pickorderLinesViewPager = findViewById(R.id.pickorderLinesViewpager);
        this.textViewChosenOrder = findViewById(R.id.textViewChosenOrder);
        this.imageButtonComments = findViewById(R.id.imageButtonComments);
    }

    @Override
    public void mSetViewModels() {

    }

    @Override
    public void mSetToolbar(String pvScreenTitleStr) {
        this.toolbarImage.setImageResource(R.drawable.ic_menu_pick);
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

        ViewCompat.setTransitionName(this.textViewChosenOrder, this.VIEW_CHOSEN_ORDER);

        this.textViewChosenOrder.setText(cPickorder.currentPickOrder.getOrderNumberStr());
        this.pickorderLinesTabLayout.addTab(this.pickorderLinesTabLayout.newTab().setText(R.string.tab_pickorderline_topick));
        this.pickorderLinesTabLayout.addTab(this.pickorderLinesTabLayout.newTab().setText(R.string.tab_pickorderline_picked));
        this.pickorderLinesTabLayout.addTab(this.pickorderLinesTabLayout.newTab().setText(R.string.tab_pickorderline_total));

        this.pickorderLinesPagerAdapter = new PickorderLinesPagerAdapter( this.pickorderLinesTabLayout.getTabCount());
        this.pickorderLinesViewPager.setAdapter(this.pickorderLinesPagerAdapter);
        this.pickorderLinesViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(pickorderLinesTabLayout));
        this.pickorderLinesTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab pvTab) {
                pickorderLinesViewPager.setCurrentItem(pvTab.getPosition());
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
    public void mSetSettings() {

    }

    @Override
    public void mSetListeners() {
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
        PickorderLinesActivity.quantityPickordersText.setText(pvTextStr);

    }

    public static void pPicklineSelected(cPickorderLine pvPickorderLine) {
        cPickorderLine.currentPickOrderLine = pvPickorderLine;
        PickorderLinesToPickFragment.pSetChosenBinCode();
    }

    public static void pPicklineToResetSelected(cPickorderLine pvPickorderLine) {
        cPickorderLine.currentPickOrderLine = pvPickorderLine;
        PickorderLinesPickedFragment.pSetChosenItemNo();
    }

    public static void pSetCurrentLocation(String pvCurrentLocationStr) {

        if (!cPickorder.currentPickOrder.pUpdateCurrentLocationBln(pvCurrentLocationStr)) {
            cUserInterface.pDoExplodingScreen(cAppExtension.context.getString(R.string.error_currentlocation_could_not_update), "", true, false);
            return;
        }

        PickorderLinesActivity.mStartOrderSelectActivity();
    }

    public static void pHandleScan(String pvScannedBarcodeStr, Boolean pvBinSelectedBln) {

        cUserInterface.pCheckAndCloseOpenDialogs();

        cResult hulpResult;

        //BIN button has been pressed, so we already have a current line
        if (pvBinSelectedBln) {

            hulpResult = cPickorderLine.currentPickOrderLine.pLineBusyRst();
            if (!hulpResult.resultBln) {
                mStepFailed(hulpResult.messagesStr());
                cPickorderLine.currentPickOrderLine = null;
                return;
            }

            //we have a line to handle, so start Pick activity
            PickorderLinesActivity.mStartPickActivity();
            return;
        }


        //Check if we have scanned a BIN and check if there are not handled lines for this BIN
        if (cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvScannedBarcodeStr,cBarcodeLayout.barcodeLayoutEnu.BIN)) {

            String barcodewithoutPrefix = cRegex.pStripRegexPrefixStr(pvScannedBarcodeStr);

            cPickorderLine.currentPickOrderLine = cPickorder.currentPickOrder.pGetNetxLineToHandleForBin(barcodewithoutPrefix);
            if (cPickorderLine.currentPickOrderLine == null) {
                mDoUnknownScan(cAppExtension.context.getString(R.string.nothing_more_todo_for_bin), barcodewithoutPrefix);
                return;
            }

            hulpResult = cPickorderLine.currentPickOrderLine.pLineBusyRst();
            if (!hulpResult.resultBln) {
                mStepFailed(hulpResult.messagesStr());
                cPickorderLine.currentPickOrderLine = null;
                return;
            }

              //we have a line to handle, so start Pick activity
            PickorderLinesActivity.mStartPickActivity();
            return;
        }

        //Check if we have scanned an ARTICLE and check if there are not handled lines for this ARTICLE
        if (cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvScannedBarcodeStr,cBarcodeLayout.barcodeLayoutEnu.ARTICLE)) {


            if (!cSetting.PICK_BIN_IS_ITEM()) {
                //unknown scan
                mDoUnknownScan(cAppExtension.context.getString(R.string.error_article_scan_not_allowed), pvScannedBarcodeStr);
                return;
            }

            String barcodewithoutPrefix = cRegex.pStripRegexPrefixStr(pvScannedBarcodeStr);

            // Article always had BIN, so ARTICLE is EQUAL to BIN
            cPickorderLine.currentPickOrderLine = cPickorder.currentPickOrder.pGetLineNotHandledByBarcode(barcodewithoutPrefix);
            if (cPickorderLine.currentPickOrderLine == null) {
                mDoUnknownScan(cAppExtension.context.getString(R.string.nothing_more_todo_for_article), barcodewithoutPrefix);
                return;
            }

            cPickorderBarcode.currentPickorderBarcode = cPickorderBarcode.pGetPickbarcodeViaBarcode(pvScannedBarcodeStr);
            if (cPickorderBarcode.currentPickorderBarcode == null) {
                cPickorderLine.currentPickOrderLine = null;
                mDoUnknownScan(cAppExtension.context.getString(R.string.nothing_more_todo_for_article), barcodewithoutPrefix);
                return;
            }

            hulpResult = cPickorderLine.currentPickOrderLine.pLineBusyRst();
            if (!hulpResult.resultBln) {
                mStepFailed(hulpResult.messagesStr());
                cPickorderLine.currentPickOrderLine = null;
                return;
            }

            //we have a line to handle, so start Pick activity
            PickorderLinesActivity.mStartPickActivity();
            return;
        }

        //unknown scan
        mDoUnknownScan(cAppExtension.context.getString(R.string.error_unknown_barcode), pvScannedBarcodeStr);

    }

    public static void pHandleLineReset() {

        cUserInterface.pCheckAndCloseOpenDialogs();

        if (!cPickorderLine.currentPickOrderLine.pResetBln()) {
            cUserInterface.pDoExplodingScreen(cAppExtension.context.getString(R.string.error_couldnt_reset_line), cPickorderLine.currentPickOrderLine.getLineNoInt().toString(), true, true );
            return;
        }

        //Set the selected tab to 0, and after that change it back to this tab
        PickorderLinesActivity.pickorderLinesViewPager.setCurrentItem(0);

    }

    public static void pPickingDone(String pvCurrentLocationStr) {

        //If we receiver a current location, then update it via the webservice and locally
        //If we didn't receive a location, then we picked 0 items, so it's oke
        if (!pvCurrentLocationStr.isEmpty()){
            if (!cPickorder.currentPickOrder.pUpdateCurrentLocationBln(pvCurrentLocationStr)) {
                cUserInterface.pDoExplodingScreen(cAppExtension.context.getString(R.string.error_currentlocation_could_not_update), "", true, false);
                return;
            }
        }

        //Check if we need to select a workplace
        if (!cPickorder.currentPickOrder.PackAndShipNeededBln() && !cPickorder.currentPickOrder.SortNeededBln()&&
            cPickorder.currentPickOrder.isPickSalesAskWorkplaceBln() && cWorkplace.currentWorkplace == null ) {

            mShowWorkplaceFragment();
            return;
        }

        PickorderLinesActivity.pClosePickAndDecideNextStep();

    }

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
        if (!hulpResult.resultBln && hulpResult.activityActionEnu == cWarehouseorder.ActivityActionEnu.Unknown ) {
            cUserInterface.pDoExplodingScreen(hulpResult.messagesStr(),"",true,true);
            return  false;
        }

        //Something went wrong, the order has been deleted, so show comments and refresh
        if (!hulpResult.resultBln && hulpResult.activityActionEnu == cWarehouseorder.ActivityActionEnu.Hold ) {

            //If we got any comments, show them
            if (cPickorder.currentPickOrder.pFeedbackCommentObl() != null && cPickorder.currentPickOrder.pFeedbackCommentObl().size() > 0 ) {
                //Process comments from webresult
                PickorderLinesActivity.mShowCommentsFragment(cPickorder.currentPickOrder.pFeedbackCommentObl(), hulpResult.messagesStr());
            }

            return  false;
        }

        return true;

    }

    public static void pClosePickAndDecideNextStep(){

        if (!PickorderLinesActivity.mTryToCloseOrderBln()) {
            return;
        }

        if (cPickorder.currentPickOrder.isBCBln() || cPickorder.currentPickOrder.isBPBln()) {
            mSortNextStap();
        }

        if (cPickorder.currentPickOrder.isBMBln() ||cPickorder.currentPickOrder.isPVBln() ) {
            mPackAndShipNextStap();
        }

    }

    //End Region Public Methods

    //Region Private Methods

    private void mCheckAllDone() {

        // If not everything is done, then leave
        if (this.mAllLinesDoneBln() == false) {
            return;
        }

        // If not everything is sent, then leave
        if (!this.mCheckAndSentLinesBln()) {
            return;
        }

        if (!cPickorder.currentPickOrder.pickActivityBinRequiredBln ||
           cPickorder.currentPickOrder.pQuantityHandledDbl() == 0 ||
           !cPickorder.currentPickOrder.getCurrentLocationStr().isEmpty()) {
            // Show order done fragment
            PickorderLinesActivity.mShowOrderDoneFragment(false);
            return;
        }
            // Show order done fragment
            PickorderLinesActivity.mShowOrderDoneFragment(true);
    }

    private void mChangeSelectedTab(TabLayout.Tab pvTab) {

        switch (pvTab.getPosition()) {
            case 0:
                PickorderLinesActivity.pChangeTabCounterText(cText.doubleToString(cPickorder.currentPickOrder.pQuantityNotHandledDbl()) + "/" + cText.doubleToString(cPickorder.currentPickOrder.pQuantityTotalDbl()));
                break;
            case 1:
                PickorderLinesActivity.pChangeTabCounterText(cText.doubleToString(cPickorder.currentPickOrder.pQuantityHandledDbl()) + "/" + cText.doubleToString(cPickorder.currentPickOrder.pQuantityTotalDbl()));
                break;
            case 2:
                PickorderLinesActivity.pChangeTabCounterText(cText.doubleToString(cPickorder.currentPickOrder.pQuantityTotalDbl()));
                break;
            default:

        }
    }

    private void mSetShowCommentListener() {
        this.imageButtonComments.setOnClickListener(new View.OnClickListener() {
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

    private void mShowCurrentLocationFragment() {

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

    private Boolean mCheckAndSentLinesBln() {

        List<cPickorderLine> linesToSendObl = cPickorder.currentPickOrder.pGetLinesToSendFromDatabasObl();
        Boolean hulpBln = false;

        // If there is nothing to send, then we are done
        if (linesToSendObl.size() == 0 ) {
            return  true;
        }

        //Show sending animation
        this.mShowSending();

        // Try to send each line, if one failes then stop
        for (cPickorderLine pickorderLine : linesToSendObl) {

            //Set the current line
            cPickorderLine.currentPickOrderLine = pickorderLine;

            //Try to send the line
            hulpBln = cPickorderLine.currentPickOrderLine .pHandledBln();
            if ( !hulpBln) {
                break;
            }

        }

        if (!hulpBln) {
            this.mShowNotSent();
            return false;
        }

        this.mShowSent();
        return  true;

    }

    private Boolean mAllLinesDoneBln() {

        if (cPickorder.currentPickOrder.pGetLinesNotHandledFromDatabasObl().size() <= 0) {
            return true;
        }
        return  false;
    }

    private void mShowSending() {
        //todo: show airplane animation until done
        //cUserInterface.showToastMessage(thisContext, getString(R.string.sending_unsent_lines), R.raw.headsupsound);
    }

    private void mShowNotSent() {

        ArrayList<String> errorObl = new ArrayList<>();
        errorObl.add(cAppExtension.context.getString(R.string.lines_to_be_send));

        Bundle bundle = new Bundle();
        bundle.putStringArrayList(cPublicDefinitions.WEBSERVICEERROR_LIST_TAG, errorObl );
        WebserviceErrorFragment webserviceErrorFragment = new WebserviceErrorFragment();
        webserviceErrorFragment.setArguments(bundle);
        webserviceErrorFragment.setCancelable(true);
        webserviceErrorFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.WEBSERVICEERROR_TAG);
    }

    private void mShowSent() {
        cUserInterface.pShowToastMessage(getString(R.string.all_lines_sent), R.raw.goodsound);
    }

    private static void mStepFailed(String pvErrorMessageStr){
        cUserInterface.pDoExplodingScreen(pvErrorMessageStr, cPickorder.currentPickOrder.getOrderNumberStr(), true, true );
        cPickorder.currentPickOrder.pLockReleaseViaWebserviceBln(cWarehouseorder.StepCodeEnu.Pick_Picking,cWarehouseorder.WorkflowPickStepEnu.PickPicking);
        cUserInterface.pCheckAndCloseOpenDialogs();
    }

    private static void mAskSort() {
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

    private static void mAskShip() {
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

    private static void mSortNextStap(){

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
            return;
        }

    }

    private static void mPackAndShipNextStap(){

        //If activity bin is not required, then don't show the fragment
        if ( cPickorder.currentPickOrder.pQuantityHandledDbl() == 0 ) {
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

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(cAppExtension.context);
        alertDialog.setTitle(R.string.message_sure_leave_screen_title);
        alertDialog.setMessage(getString(R.string.message_sure_leave_screen_text));
        alertDialog.setPositiveButton(R.string.message_sure_leave_screen_positive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface pvDialogInterface, int i) {

                cPickorder.currentPickOrder.pLockReleaseViaWebserviceBln(cWarehouseorder.StepCodeEnu.Pick_Picking,cWarehouseorder.WorkflowPickStepEnu.PickPicking);

                //If activity bin is not required, then don't show the fragment
                if (!cPickorder.currentPickOrder.pickActivityBinRequiredBln || cPickorder.currentPickOrder.pQuantityHandledDbl() == 0 || !cPickorder.currentPickOrder.getCurrentLocationStr().isEmpty()) {
                    mStartOrderSelectActivity();
                    return;
                }

                mShowCurrentLocationFragment();
                return;
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
        Intent intent = new Intent(cAppExtension.context, PickorderSelectActivity.class);
        cAppExtension.activity.startActivity(intent);
    }

    private static void mStartSortActivity() {

        //Try to lock the pickorder
        if (!cPickorder.currentPickOrder.pLockViaWebserviceRst(cWarehouseorder.StepCodeEnu.Pick_Picking, cWarehouseorder.WorkflowPickStepEnu.PickSorting).resultBln) {
            mStepFailed(cAppExtension.context.getString(R.string.error_couldnt_lock_order));
            return;
        }

        //Get sort lines
        if (!cPickorder.currentPickOrder.pGetLinesViaWebserviceBln(true,cWarehouseorder.PickOrderTypeEnu.SORT)) {
            mStepFailed(cAppExtension.context.getString(R.string.error_getting_sort_lines_failed));
            return;
        }

        //Show Sort Activity
        Intent intent = new Intent(cAppExtension.context, SortorderLinesActivity.class);
        cAppExtension.activity.startActivity(intent);
        return;
    }

    private static void mStartShipActivity() {

        //Try to lock the pickorder
        if (!cPickorder.currentPickOrder.pLockViaWebserviceRst(cWarehouseorder.StepCodeEnu.Pick_Picking, 40).resultBln) {
            mStepFailed(cAppExtension.context.getString(R.string.error_couldnt_lock_order));
            return;
        }

        //todo: get pack and ship lines
        if (!cPickorder.currentPickOrder.pGetLinesViaWebserviceBln(true,cWarehouseorder.PickOrderTypeEnu.SORT)) {
            mStepFailed(cAppExtension.context.getString(R.string.error_getting_sort_lines_failed));
            return;
        }

        //show Shiporder lines
        //todo: put this back
//        Intent intent = new Intent(cAppExtension.context, ShiporderLinesActivity.class);
//        cAppExtension.activity.startActivity(intent);
//        return;

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


    //End Region Private Methods


}
