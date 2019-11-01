package nl.icsvertex.scansuite.Activities.sort;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.android.material.tabs.TabLayout;

import androidx.fragment.app.Fragment;
import androidx.core.view.ViewCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ICS.Interfaces.iICSDefaultActivity;
import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cRegex;
import ICS.Utils.cResult;
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
import SSU_WHS.General.cPublicDefinitions;
import ICS.Utils.cText;
import ICS.Utils.cUserInterface;
import SSU_WHS.Picken.Shipment.cShipment;
import nl.icsvertex.scansuite.PagerAdapters.SortorderLinesPagerAdapter;
import ICS.cAppExtension;
import nl.icsvertex.scansuite.Fragments.dialogs.CommentFragment;
import nl.icsvertex.scansuite.Fragments.dialogs.OrderDoneFragment;
import nl.icsvertex.scansuite.Fragments.dialogs.WebserviceErrorFragment;
import nl.icsvertex.scansuite.Fragments.dialogs.WorkplaceFragment;
import nl.icsvertex.scansuite.R;


//import android.app.Fragment;

public class SortorderLinesActivity extends AppCompatActivity implements iICSDefaultActivity {

    //Region Public Properties
    public static final String VIEW_CHOSEN_ORDER = "detail:header:text";
    public static Fragment currentLineFragment;
    //End Region Public Properties

    //Region Private Properties
    //Region Views

    private TextView textViewChosenOrder;
    static private TextView quantitySortordersText;
    private ConstraintLayout container;
    private TabLayout sortorderLinesTabLayout;
    private static ViewPager sortorderLinesViewPager;
    private SortorderLinesPagerAdapter sortorderLinesPagerAdapter;
    private static ImageView imageButtonComments;

    private ImageView toolbarImage;
    private TextView toolbarTitle;
    private ImageView toolbarImageHelp;

    //End Region Views
    //End Region Private Properties

    //Region Default Methods

    @Override
    protected void onCreate(Bundle pvSavedInstanceState) {
        super.onCreate(pvSavedInstanceState);
        setContentView(R.layout.activity_sortorderlines);
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

        this.mSetToolbar(getResources().getString(R.string.screentitle_sortorderlines));

        this.mFieldsInitialize();

        this.mSetListeners();

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
        this.container = findViewById(R.id.container);
        this.toolbarImage = findViewById(R.id.toolbarImage);
        this.toolbarTitle = findViewById(R.id.toolbarTitle);
        this.toolbarImageHelp = findViewById(R.id.toolbarImageHelp);
        this.quantitySortordersText = findViewById(R.id.quantitySortordersText);
        this.sortorderLinesTabLayout = findViewById(R.id.sortorderLinesTabLayout);
        this.sortorderLinesViewPager = findViewById(R.id.sortorderLinesViewpager);
        this.textViewChosenOrder = findViewById(R.id.textViewChosenOrder);
        this.imageButtonComments = findViewById(R.id.imageButtonComments);
    }

    @Override
    public void mSetToolbar(String pvScreenTitleStr) {

        this.toolbarImage.setImageResource(R.drawable.ic_menu_sort);
        this.toolbarTitle.setText(pvScreenTitleStr);
        ViewCompat.setTransitionName(textViewChosenOrder, VIEW_CHOSEN_ORDER);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void mFieldsInitialize() {

        this.textViewChosenOrder.setText(cPickorder.currentPickOrder.getOrderNumberStr());
        this.sortorderLinesTabLayout.addTab(sortorderLinesTabLayout.newTab().setText(R.string.tab_sortorderline_tosort));
        this.sortorderLinesTabLayout.addTab(sortorderLinesTabLayout.newTab().setText(R.string.tab_sortorderline_sorted));
        this.sortorderLinesTabLayout.addTab(sortorderLinesTabLayout.newTab().setText(R.string.tab_pickorderline_total));

        this.sortorderLinesPagerAdapter = new SortorderLinesPagerAdapter(this.sortorderLinesTabLayout.getTabCount());
        this.sortorderLinesViewPager.setAdapter(this.sortorderLinesPagerAdapter);
        this.sortorderLinesViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(this.sortorderLinesTabLayout));
        this. sortorderLinesTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab pvTab) {
                sortorderLinesViewPager.setCurrentItem(pvTab.getPosition());
                mSetTabselected(pvTab);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    @Override
    public void mSetListeners() {
        this.mSetShowCommentListener();
    }

    @Override
    public void mInitScreen() {

        //Show comments first
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

    public static void pPicklineSelected(cPickorderLine pvPickorderLine) {
        cPickorderLine.currentPickOrderLine = pvPickorderLine;
    }

    public static void pPicklineToResetSelected(cPickorderLine pvPickorderLine) {
        cPickorderLine.currentPickOrderLine = pvPickorderLine;
    }

    public static void pChangeTabCounterText(String pvTextStr){
        SortorderLinesActivity.quantitySortordersText.setText(pvTextStr);
    }

    public static void pCloseSortAndDecideNextStep(){

        //Close this step
        if (!SortorderLinesActivity.mTryToCloseOrderBln()) {
            return;
        }

        //If there is nothing more to do, then we are done
        if (!cPickorder.currentPickOrder.PackAndShipNeededBln()) {
            mShowOrderDoneFragment();
            return;
        }

        //If we have to ship, then start ship/ask if ship has to be started/go back or order select
        if (cPickorder.currentPickOrder.isBCBln() || cPickorder.currentPickOrder.isBPBln()) {
            mPackAndShipNextStap();
            return;
        }
    }

    public static void pHandleScan(String pvScannedBarcodeStr,Boolean pvLineSelectedBln) {

        cUserInterface.pCheckAndCloseOpenDialogs();
        cResult hulpResult;

        //BIN button has been pressed, so we already have a current line
        if (pvLineSelectedBln) {

            hulpResult = cPickorderLine.currentPickOrderLine.pSortLineBusyRst();
            if (!hulpResult.resultBln) {
                mStepFailed(hulpResult.messagesStr());
                cPickorderLine.currentPickOrderLine = null;
                return;
            }

            //we have a line to handle, so start Pick activity
            SortorderLinesActivity.mStartSortActivity();
            return;
        }

        //Check if we have scanned an ARTICLE and check if there are not handled linesInt for this ARTICLE
        if (!cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvScannedBarcodeStr,cBarcodeLayout.barcodeLayoutEnu.ARTICLE)) {

            cUserInterface.pDoExplodingScreen(cAppExtension.context.getString(R.string.error_article_scan_mandatory), "", true, true);
            return;
        }

        String barcodewithoutPrefix = cRegex.pStripRegexPrefixStr(pvScannedBarcodeStr);

        // Get line forthis barcode
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

        hulpResult = cPickorderLine.currentPickOrderLine.pSortLineBusyRst();
        if (!hulpResult.resultBln) {
            mStepFailed(hulpResult.messagesStr());
            cPickorderLine.currentPickOrderLine = null;
            return;
        }

        //we have a line to handle, so start Pick activity
        SortorderLinesActivity.mStartSortActivity();
        return;
    }

    public static void pHandleLineReset() {

        cUserInterface.pCheckAndCloseOpenDialogs();

        if (!cPickorderLine.currentPickOrderLine.pResetBln()) {
            cUserInterface.pDoExplodingScreen(cAppExtension.context.getString(R.string.error_couldnt_reset_line), cPickorderLine.currentPickOrderLine.getLineNoInt().toString(), true, true );
            return;
        }

        //Set the selected tab to 0, and after that change it back to this tab
        SortorderLinesActivity.sortorderLinesViewPager.setCurrentItem(0);

    }

    public static void pSortingDone() {

        //Check if we need to select a workplace
        if (!cPickorder.currentPickOrder.PackAndShipNeededBln() && cPickorder.currentPickOrder.isPickSalesAskWorkplaceBln() && cWorkplace.currentWorkplace == null ) {
            mShowWorkplaceFragment();
            return;
        }

        SortorderLinesActivity.pCloseSortAndDecideNextStep();

    }

    public static void pStartOrderSelectActivity() {
        Intent intent = new Intent(cAppExtension.context, SortorderSelectActivity.class);
        cAppExtension.activity.startActivity(intent);
    }

    //End Region Public Methods

    //Region Private Methods

    //Region Fragments and Activities

    private static void mShowWorkplaceFragment() {

        WorkplaceFragment workplaceFragment = new WorkplaceFragment();
        workplaceFragment.setCancelable(false);
        workplaceFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.WORKPLACEFRAGMENT_TAG);
    }

    private static void mShowOrderDoneFragment() {

        cUserInterface.pPlaySound(R.raw.goodsound, null);

        final OrderDoneFragment orderDoneFragment = new OrderDoneFragment(false);
        orderDoneFragment.setCancelable(false);
        orderDoneFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.ORDERDONE_TAG);
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

    private static void mShowComments(){

        if (cPickorder.currentPickOrder.pSortCommentObl() == null || cPickorder.currentPickOrder.pSortCommentObl().size() == 0) {
            SortorderLinesActivity.imageButtonComments.setVisibility(View.INVISIBLE);
            return;
        }

        SortorderLinesActivity.imageButtonComments.setVisibility(View.VISIBLE);

        //We already showed the comments
        if (cComment.commentsShownBln) {
            return;
        }

        SortorderLinesActivity.mShowCommentsFragment(cPickorder.currentPickOrder.pSortCommentObl(),"");
        cComment.commentsShownBln = true;
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

    private void mTryToLeaveActivity(){


        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(cAppExtension.context);
        alertDialog.setTitle(R.string.message_sure_leave_screen_title);
        alertDialog.setMessage(getString(R.string.message_sure_leave_screen_text));
        alertDialog.setCancelable(true);

        //If we don't want to leave then we are done
        alertDialog.setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                return;
            }
        });


        //If we want to leave, check if we still need to send progress
        alertDialog.setPositiveButton(R.string.message_sure_leave_screen_positive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface pvDialogInterface, int i) {

                //Check if there is progress, and ask if user wants to send now
                if (cPickorder.currentPickOrder.pGetLinesToSendFromDatabasObl().size() > 0 || cPickorder.currentPickOrder.pGetLinesBusyFromDatabasObl().size() >0 ) {
                    AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(cAppExtension.context);
                    alertDialog2.setTitle(R.string.message_progress_to_send);
                    alertDialog2.setMessage(getString(R.string.message_send_now));
                    alertDialog2.setCancelable(true);

                    // User doesn't wanna send, so we are done
                    alertDialog2.setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            return;
                        }
                    });

                    // Try to send everything
                    alertDialog2.setPositiveButton(R.string.message_send, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface pvDialogInterface, int i) {

                            // Update alle busy linesInt to statusInt to be send
                            if (! mHandleBusyLinesBln()) {
                                return;
                            }

                            // Check if there are linesInt to be send, and send them, if we fail we stop
                            if (! mCheckAndSentLinesToBeSendBln()) {
                                return;
                            }

                            //We managed to send everuthing, so we are done
                            cPickorder.currentPickOrder.pLockReleaseViaWebserviceBln(cWarehouseorder.StepCodeEnu.Pick_Sorting, cWarehouseorder.WorkflowPickStepEnu.PickPicking);
                            pStartOrderSelectActivity();
                            return;
                        }
                    });

                    alertDialog2.show();
                }
                //There is no progress, so we are done
                else {
                    cPickorder.currentPickOrder.pLockReleaseViaWebserviceBln(cWarehouseorder.StepCodeEnu.Pick_Sorting, cWarehouseorder.WorkflowPickStepEnu.PickPicking);
                    pStartOrderSelectActivity();
                    return;
                }

            }
        });

        alertDialog.show();

    }

    private static void mStartSortActivity(){
        //we have a line to handle, so start Sort activity
        Intent intent = new Intent(cAppExtension.context, SortorderSortActivity.class);
        cAppExtension.activity.startActivity(intent);
    }

    private static void mStartShipActivity() {

        cResult hulpResult;

        //Clear workplace, so you have to select it in the next step
        cWorkplace.currentWorkplace = null;

       if (! mTryToLockShipOrderBln()) {
           return;
       }

        //Delete the detail, so we can get them from the webservice
        if (cPickorder.currentPickOrder.pDeleteDetailsBln() == false) {
            mStepFailed(cAppExtension.context.getString(R.string.error_couldnt_delete_details));
            return;
        }

        //Get all new details
        hulpResult = SortorderLinesActivity.mGetShipOrderDetailsRst();
        if (hulpResult.resultBln == false ) {
            SortorderLinesActivity.mStepFailed(hulpResult.messagesStr());
            return;
        }

        //Show Ship activity
        mStartShipActivity();


    }    private void mSetTabselected(TabLayout.Tab pvTab) {

        switch (pvTab.getPosition()) {
            case 0:
                SortorderLinesActivity.pChangeTabCounterText(cText.pDoubleToStringStr(cPickorder.currentPickOrder.pQuantityNotHandledDbl()) + "/" + cText.pDoubleToStringStr(cPickorder.currentPickOrder.pQuantityTotalDbl()));
                break;
            case 1:
                SortorderLinesActivity.pChangeTabCounterText(cText.pDoubleToStringStr(cPickorder.currentPickOrder.pQuantityHandledDbl()) + "/" + cText.pDoubleToStringStr(cPickorder.currentPickOrder.pQuantityTotalDbl()));
                break;
            case 2:
                SortorderLinesActivity.pChangeTabCounterText(cText.pDoubleToStringStr(cPickorder.currentPickOrder.pQuantityTotalDbl()));
                break;
            default:

        }
    }

    //End Region Fragments and Activities

    private Boolean mCheckAndSentLinesToBeSendBln() {

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

    private Boolean mHandleBusyLinesBln() {

        List<cPickorderLine> busyLinesObl = cPickorder.currentPickOrder.pGetLinesBusyFromDatabasObl();

        // If there is nothing to send, then we are done
        if (busyLinesObl.size() == 0 ) {
            return  true;
        }

        // Try to send each line, if one failes then stop
        for (cPickorderLine pickorderLine : busyLinesObl) {

            //Set the current line
            cPickorderLine.currentPickOrderLine = pickorderLine;
            cPickorderLine.currentPickOrderLine.pHandledIndatabaseBln();

        }

        return  true;

    }

    private static void mPackAndShipNextStap(){

        //If activity bin is not required, then don't show the fragment
        if ( cPickorder.currentPickOrder.pQuantityHandledDbl() == 0 ) {
            pStartOrderSelectActivity();
            return;
        }

        //We are done with this order, so show order done
        if (!cPickorder.currentPickOrder.PackAndShipNeededBln()) {
            mShowOrderDoneFragment();
            return;
        }

        // If setting is not defined, then ask user
        if (cSetting.PICK_PACK_AND_SHIP_AUTO_START() == null) {
            SortorderLinesActivity.mAskShip();
            return;
        }

        // If settings is false, then go back to order select
        if (!cSetting.PICK_PACK_AND_SHIP_AUTO_START()) {
            SortorderLinesActivity.pStartOrderSelectActivity();
            return;
        }

        // If settings is true, then go  to ship
        if (cSetting.PICK_PACK_AND_SHIP_AUTO_START()) {
            mStartShipActivity();
        }

    }

    private void mCheckAllDone() {

        // If not everything is done, then leave
        if (this.mAllLinesDoneBln() == false) {
            return;
        }

        // If not everything is sent, then leave
        if (!this.mCheckAndSentLinesToBeSendBln()) {
            return;
        }

        // Set sorting done
        SortorderLinesActivity.pSortingDone();

    }

    private Boolean mAllLinesDoneBln() {

        if (cPickorder.currentPickOrder.pGetLinesNotHandledFromDatabasObl().size() <= 0) {
            return true;
        }
        return  false;
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
                pStartOrderSelectActivity();
            }
        });
        builder.setIcon(R.drawable.ic_menu_ship);
        builder.show();
    }

    private void mSetShowCommentListener() {
        this.imageButtonComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mShowCommentsFragment(cPickorder.currentPickOrder.pSortCommentObl(),"");
            }
        });
    }

    private static boolean mTryToCloseOrderBln(){

        cResult hulpResult;
        hulpResult = new cResult();
        hulpResult.resultBln = false;

        hulpResult = cPickorder.currentPickOrder.pSortHandledViaWebserviceRst();

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
                SortorderLinesActivity.mShowCommentsFragment(cPickorder.currentPickOrder.pFeedbackCommentObl(), hulpResult.messagesStr());
            }

            return  false;
        }

        return true;

    }

    private static void mStepFailed(String pvErrorMessageStr){
        cUserInterface.pDoExplodingScreen(pvErrorMessageStr, cPickorder.currentPickOrder.getOrderNumberStr(), true, true );
        cPickorder.currentPickOrder.pLockReleaseViaWebserviceBln(cWarehouseorder.StepCodeEnu.Pick_Sorting,cWarehouseorder.WorkflowPickStepEnu.PickSorting);
        cUserInterface.pCheckAndCloseOpenDialogs();
    }

    private static void mDoUnknownScan(String pvErrorMessageStr, String pvScannedBarcodeStr) {
        cUserInterface.pDoExplodingScreen(pvErrorMessageStr, pvScannedBarcodeStr, true, true);
    }

    private static boolean mTryToLockShipOrderBln(){

        cResult hulpResult;
        hulpResult = cPickorder.currentPickOrder.pLockViaWebserviceRst(cWarehouseorder.StepCodeEnu.Pick_PackAndShip, cWarehouseorder.WorkflowPickStepEnu.PickPackAndShip);

        //Everything was fine, so we are done
        if (hulpResult.resultBln == true) {
            return true;
        }

        //Something went wrong, but no further actions are needed, so ony show reason of failure
        if (hulpResult.resultBln == false  && hulpResult.activityActionEnu == cWarehouseorder.ActivityActionEnu.Unknown ) {
            mStepFailed(hulpResult.messagesStr());
            return  false;
        }

        //Something went wrong, the order has been deleted, so show comments and refresh
        if (hulpResult.resultBln == false  && hulpResult.activityActionEnu == cWarehouseorder.ActivityActionEnu.Delete ||
                hulpResult.resultBln == false  && hulpResult.activityActionEnu == cWarehouseorder.ActivityActionEnu.NoStart ) {


            //If we got any comments, show them
            if (cPickorder.currentPickOrder.pFeedbackCommentObl() != null && cPickorder.currentPickOrder.pFeedbackCommentObl().size() > 0 ) {
                //Process comments from webresult
                SortorderLinesActivity.mShowCommentsFragment(cPickorder.currentPickOrder.pFeedbackCommentObl(), hulpResult.messagesStr());
            }

            return  false;
        }


        return true;

    }

    private static cResult mGetShipOrderDetailsRst() {

        cResult result;

        result = new cResult();
        result.resultBln = true;

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
        if (cPickorder.currentPickOrder.pGetPackAndShipLinesViaWebserviceBln(true) == false) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_getting_pack_and_ship_lines_failed));
            return result;
        }

        // Get all packages
        if (cPickorder.currentPickOrder.pGetShippingPackagedViaWebserviceBln(true) == false) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_getting_packages_failed));
            return result;
        }

        // Get all adresses, if system settings Pick Shipping Sales == false then don't ask web service
        if (cPickorder.currentPickOrder.pGetAdressesViaWebserviceBln(true) == false) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_adresses_failed));
            return result;
        }

        // Get all comments
        if (cPickorder.currentPickOrder.pGetCommentsViaWebserviceBln(true) == false) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_comments_failed));
            return result;
        }

        //If this is single article, then get barcodes
        if (cPickorder.currentPickOrder.isSingleArticleOrdersBln()) {
            if (cPickorder.currentPickOrder.pGetBarcodesViaWebserviceBln(true) == false) {
                result.resultBln = false;
                result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_barcodes_failed));
                return result;
            }
        }

        return result;
    }

    //End Region Private Methods


}
