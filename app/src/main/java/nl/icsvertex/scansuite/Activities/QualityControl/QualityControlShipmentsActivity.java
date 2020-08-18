package nl.icsvertex.scansuite.Activities.QualityControl;

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
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import ICS.Interfaces.iICSDefaultActivity;
import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cResult;
import ICS.Utils.cText;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Basics.Workplaces.cWorkplace;
import SSU_WHS.General.Comments.cComment;
import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.General.cPublicDefinitions;
import SSU_WHS.Intake.Intakeorders.cIntakeorder;
import SSU_WHS.Picken.PickorderLines.cPickorderLine;
import SSU_WHS.Picken.Pickorders.cPickorder;
import SSU_WHS.Picken.Shipment.cShipment;
import nl.icsvertex.scansuite.Activities.Ship.ShiporderShipActivity;
import nl.icsvertex.scansuite.Fragments.Dialogs.AcceptRejectFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.CommentFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.StepDoneFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.WebserviceErrorFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.WorkplaceFragment;
import nl.icsvertex.scansuite.PagerAdapters.QCShipmentsPagerAdapter;
import nl.icsvertex.scansuite.R;


//import android.app.Fragment;

public class QualityControlShipmentsActivity extends AppCompatActivity implements iICSDefaultActivity {

    //Region Public Properties
    public static Fragment currentShipmentFragment;
    //End Region Public Properties

    //Region Private Properties
    //Region Views

    private ConstraintLayout container;
    private TextView textViewChosenOrder;
    private TextView quantityQCOrdersText;
    private TabLayout QCShipmentsTabLayout;
    private ViewPager QCShipmentsViewPager;
    private ImageView imageButtonComments;
    private ImageView imageButtonCloseOrder;

    private ImageView toolbarImage;
    private TextView toolbarTitle;

    //End Region Views
    //End Region Private Properties

    //Region Default Methods

    @Override
    protected void onCreate(Bundle pvSavedInstanceState) {
        super.onCreate(pvSavedInstanceState);
        setContentView(R.layout.activity_qcordershipments);
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
        try {
            cBarcodeScan.pUnregisterBarcodeReceiver();
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

    //End Region Default Methods

    //Region iICSDefaultActivity defaults

    @Override
    public void mActivityInitialize() {

        this.mSetAppExtensions();

        this.mFindViews();

        this.mSetToolbar(getResources().getString(R.string.screentitle_qcorders));

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

        this.quantityQCOrdersText = findViewById(R.id.QCordersText);
        this.QCShipmentsTabLayout = findViewById(R.id.QCOrdersTabLayout);
        this.QCShipmentsViewPager = findViewById(R.id.QCOrdersViewpager);
        this.textViewChosenOrder = findViewById(R.id.textViewChosenOrder);
        this.imageButtonComments = findViewById(R.id.imageButtonComments);

        this.imageButtonCloseOrder = findViewById(R.id.imageButtonCloseOrder);
    }

    @Override
    public void mSetToolbar(String pvScreenTitleStr) {

        this.toolbarImage.setImageResource(R.drawable.ic_menu_pick);
        this.toolbarTitle.setText(pvScreenTitleStr);
        this.toolbarTitle.setSelected(true);

        ViewCompat.setTransitionName(textViewChosenOrder, cPublicDefinitions.VIEW_CHOSEN_ORDER);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void mFieldsInitialize() {

        this.imageButtonCloseOrder.setVisibility(View.VISIBLE);

        this.textViewChosenOrder.setText(cPickorder.currentPickOrder.getOrderNumberStr());
        this.QCShipmentsTabLayout.addTab(QCShipmentsTabLayout.newTab().setText(R.string.tab_qcorderline_tocheck));
        this.QCShipmentsTabLayout.addTab(QCShipmentsTabLayout.newTab().setText(R.string.tab_qcorderline_checked));

        QCShipmentsPagerAdapter qcShipmentsPagerAdapter = new QCShipmentsPagerAdapter(this.QCShipmentsTabLayout.getTabCount());
        this.QCShipmentsViewPager.setAdapter(qcShipmentsPagerAdapter);
        this.QCShipmentsViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(this.QCShipmentsTabLayout));
        this.QCShipmentsTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab pvTab) {
                QCShipmentsViewPager.setCurrentItem(pvTab.getPosition());
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
        this.mCloseOrderListener();
    }

    @Override
    public void mInitScreen() {

        //Show comments first
        this.mShowComments();

        //Check if we need to register a workplaceStr
        if (cWorkplace.currentWorkplace == null ) {

            if (cWorkplace.allWorkplacesObl.size() > 1) {
                //Show the workplaceStr fragment
                this.mShowWorkplaceFragment();
            }
            else {
                //Pop-up is not needed, we just select the only workplace there is
                cWorkplace.currentWorkplace = cWorkplace.allWorkplacesObl.get(0);
                this.pWorkplaceSelected();
            }
            return;

        }


        //Call this here, because this is called everytime the activiy gets shown
        this.mCheckAllDone();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem pvMenuItem) {
        if (pvMenuItem.getItemId() == android.R.id.home) {
            this.mTryToLeaveActivity();
            return true;
        }

        return super.onOptionsItemSelected(pvMenuItem);
    }

    @Override
    public void onBackPressed() {
        this.mTryToLeaveActivity();
    }

    //End Region iICSDefaultActivity defaults

    //Region Public Methods

    public  void pShipmentSelected(cShipment pvShipment) {
        cShipment.currentShipment = pvShipment;
    }

    public  void pChangeTabCounterText(String pvTextStr){
        this.quantityQCOrdersText.setText(pvTextStr);
    }

    public  void pCloseQCAndDecideNextStep(){

        //Close this step
        if (!this.mTryToCloseOrderBln()) {
            return;
        }

        //If there is nothing more to do, then we are done
        if (!cPickorder.currentPickOrder.isPackAndShipNeededBln()) {
            this.mShowOrderDoneFragment();
        }

    }

    public  void pHandleScan(cBarcodeScan pvBarcodeScan, Boolean pvSourceNoSelectedBln) {

        cUserInterface.pCheckAndCloseOpenDialogs();

        cResult hulpRst;

        //SourceNo button has been pressed, so we already have a current line
        if (!pvSourceNoSelectedBln) {

            //Get shipment by SourceNo or pickcartbox
            cShipment.currentShipment = cShipment.pGetShipmentWithScannedBarcode(pvBarcodeScan);
        }

        // We did not find a match, so stop
        if (cShipment.currentShipment == null) {
            this.mStepFailed(cAppExtension.context.getString(R.string.message_unknown_barcode), pvBarcodeScan.getBarcodeOriginalStr());
            return;
        }

        hulpRst = cShipment.currentShipment.pCheckShipmentRst();
        if (!hulpRst.resultBln ){
            this.mStepFailed(hulpRst.messagesStr(),"");
            return;
        }

        this.mStartQCLinesActivity();

    }

    public void pQCDone() {

        //Check if we need to select a workplaceStr
        if (!cPickorder.currentPickOrder.isPackAndShipNeededBln() && cPickorder.currentPickOrder.isPickSalesAskWorkplaceBln() && cWorkplace.currentWorkplace == null ) {
            this.mShowWorkplaceFragment();
            return;
        }

        this.pCloseQCAndDecideNextStep();

    }

    public  void pStartOrderSelectActivity() {
        Intent intent = new Intent(cAppExtension.context, QualityControlSelectActivity.class);
        cAppExtension.activity.startActivity(intent);
    }

    public  void pWorkplaceSelected(){

        List<Fragment> fragments = cAppExtension.fragmentManager.getFragments();
        for (Fragment fragment : fragments) {
            if (fragment instanceof WorkplaceFragment) {
                FragmentTransaction fragmentTransaction = cAppExtension.fragmentManager.beginTransaction();
                fragmentTransaction.remove(fragment);
                fragmentTransaction.commit();
            }
        }

        if (!cPickorder.currentPickOrder.pUpdateWorkplaceViaWebserviceBln(cWorkplace.currentWorkplace.getWorkplaceStr())) {
            cUserInterface.pShowSnackbarMessage(this.container,cAppExtension.activity.getString(R.string.message_workplace_not_updated),null, true);
            return;
        }

        //Register barcodeStr receiver, because the workplaceStr fragment has been shown
        cBarcodeScan.pRegisterBarcodeReceiver();

        cUserInterface.pShowSnackbarMessage(this.container,cAppExtension.activity.getString(R.string.message_workplace_selected) + ' ' + cWorkplace.currentWorkplace.getWorkplaceStr() ,R.raw.headsupsound,false);
    }

    //End Region Public Methods

    //Region Private Methods

    //Region Fragments and Activities

    private  void mShowWorkplaceFragment() {
        WorkplaceFragment workplaceFragment = new WorkplaceFragment();
        workplaceFragment.setCancelable(false);
        workplaceFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.WORKPLACEFRAGMENT_TAG);
    }

    private void mShowOrderDoneFragment() {

        cUserInterface.pPlaySound(R.raw.goodsound, null);

        final StepDoneFragment stepDoneFragment = new StepDoneFragment(cAppExtension.activity.getString(R.string.message_qcdone), cAppExtension.activity.getString(R.string.message_close_qc_fase), false);
        stepDoneFragment.setCancelable(false);
        stepDoneFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.ORDERDONE_TAG);
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

    private void mShowComments(){

        if (cPickorder.currentPickOrder.pSortCommentObl() == null || cPickorder.currentPickOrder.pSortCommentObl().size() == 0) {
            this.imageButtonComments.setVisibility(View.INVISIBLE);
            return;
        }

        this.imageButtonComments.setVisibility(View.VISIBLE);

        //We already showed the comments
        if (cComment.commentsShownBln) {
            return;
        }

        this.mShowCommentsFragment(cPickorder.currentPickOrder.pSortCommentObl(),"");
        cComment.commentsShownBln = true;
    }

    private void mShowCommentsFragment(List<cComment> pvDataObl, String pvTitleStr) {

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

            }
        });


        //If we want to leave, check if we still need to send progress
        alertDialog.setPositiveButton(R.string.message_sure_leave_screen_positive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface pvDialogInterface, int i) {

                //Check if there is progress, and ask if user wants to send now
                if (cPickorder.currentPickOrder.pGetLinesToSendFromDatabaseObl().size() > 0 || cPickorder.currentPickOrder.pGetLinesBusyFromDatabaseObl().size() >0 ) {
                    AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(cAppExtension.context);
                    alertDialog2.setTitle(R.string.message_progress_to_send);
                    alertDialog2.setMessage(getString(R.string.message_send_now));
                    alertDialog2.setCancelable(true);

                    // User doesn't wanna send, so we are done
                    alertDialog2.setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

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
                            cPickorder.currentPickOrder.pLockReleaseViaWebserviceBln(cWarehouseorder.StepCodeEnu.Pick_QualityContol, cWarehouseorder.WorkflowPickStepEnu.PickQualityControl);
                            pStartOrderSelectActivity();
                        }
                    });

                    alertDialog2.show();
                }
                //There is no progress, so we are done
                else {
                    cPickorder.currentPickOrder.pLockReleaseViaWebserviceBln(cWarehouseorder.StepCodeEnu.Pick_QualityContol, cWarehouseorder.WorkflowPickStepEnu.PickQualityControl);
                    pStartOrderSelectActivity();
                }

            }
        });

        alertDialog.show();

    }

    private void mStartQCLinesActivity(){
        //we have a line to handle, so start Sort activity
        Intent intent = new Intent(cAppExtension.context, QualityControlLinesActivity.class);
        cAppExtension.activity.startActivity(intent);
    }


    private void mCloseOrderListener() {

        this.imageButtonCloseOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                mShowCloseOrderDialog(cAppExtension.activity.getString(R.string.message_leave), cAppExtension.activity.getString(R.string.message_close));

            }
        });
    }

    private void mShowCloseOrderDialog(String pvRejectStr, String pvAcceptStr) {

        cUserInterface.pCheckAndCloseOpenDialogs();
        String messageStr = "";


            if (cPickorder.currentPickOrder.pQCNotCheckedDbl() == 0) {
                messageStr = (cAppExtension.activity.getString(R.string.message_exactly_what_you_needed));
            }

            if (cPickorder.currentPickOrder.pQCNotCheckedDbl() > 0) {
                messageStr = cText.pDoubleToStringStr(cPickorder.currentPickOrder.pQCNotCheckedDbl()) + " " + cAppExtension.activity.getString(R.string.message_less_items);
            }


            final AcceptRejectFragment acceptRejectFragment = new AcceptRejectFragment(cAppExtension.activity.getString(R.string.message_close_order),
                    cAppExtension.activity.getString(R.string.message_close_qcorder_text,
                            cText.pDoubleToStringStr(cPickorder.currentPickOrder.pQuantityTotalDbl()),
                            cText.pDoubleToStringStr(cPickorder.currentPickOrder.pQCCheckedDbl()), messageStr),
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

    private void mSetTabselected(TabLayout.Tab pvTab) {

        switch (pvTab.getPosition()) {
            case 0:
                this.pChangeTabCounterText(cText.pIntToStringStr(cPickorder.currentPickOrder.pGetNotHandledShipmentsObl().size()) + "/" + cText.pIntToStringStr(cPickorder.currentPickOrder.shipmentObl().size()));
                break;
            case 1:
                this.pChangeTabCounterText(cText.pIntToStringStr(cPickorder.currentPickOrder.pGetHandledShipmentsObl().size()) + "/" + cText.pIntToStringStr(cPickorder.currentPickOrder.shipmentObl().size()));
                break;
            default:

        }
    }

    //End Region Fragments and Activities

    private Boolean mCheckAndSentLinesToBeSendBln() {

        List<cPickorderLine> linesToSendObl = cPickorder.currentPickOrder.pGetLinesToSendFromDatabaseObl();
        boolean hulpBln = false;

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
            hulpBln = cPickorderLine.currentPickOrderLine.pCheckedBln();
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

        List<cPickorderLine> busyLinesObl = cPickorder.currentPickOrder.pGetLinesBusyFromDatabaseObl();

        // If there is nothing to send, then we are done
        if (busyLinesObl.size() == 0 ) {
            return  true;
        }

        // Try to send each line, if one failes then stop
        for (cPickorderLine pickorderLine : busyLinesObl) {

            //Set the current line
            cPickorderLine.currentPickOrderLine = pickorderLine;
            cPickorderLine.currentPickOrderLine.pHandledIndatabase();

        }

        return  true;

    }

    private void mCheckAllDone() {

        // If not everything is done, then leave
        if (!this.mAllLinesDoneBln()) {
            return;
        }

        // If not everything is sent, then leave
        if (!this.mCheckAndSentLinesToBeSendBln()) {
            return;
        }

        // Set sorting done
        this.pQCDone();

    }

    private Boolean mAllLinesDoneBln() {
        return cPickorder.currentPickOrder.pQCNotCheckedDbl() <= 0;
    }

    private void mSetShowCommentListener() {
        this.imageButtonComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mShowCommentsFragment(cPickorder.currentPickOrder.pSortCommentObl(),"");
            }
        });
    }

    private boolean mTryToCloseOrderBln(){

        cResult hulpResult;
        hulpResult = new cResult();
        hulpResult.resultBln = false;

        hulpResult = cPickorder.currentPickOrder.pQCHandledViaWebserviceRst();
        //Something went wrong, but no further actions are needed, so ony show reason of failure
        if (!hulpResult.resultBln) {
            cUserInterface.pDoExplodingScreen(hulpResult.messagesStr(),"",true,true);
            return  false;
        }

        hulpResult = cPickorder.currentPickOrder.pShipHandledViaWebserviceRst();
        //Something went wrong, but no further actions are needed, so ony show reason of failure
        if (!hulpResult.resultBln) {
            cUserInterface.pDoExplodingScreen(hulpResult.messagesStr(),"",true,true);
            return  false;
        }

        return true;

    }

    private  void mStepFailed(String pvErrorMessageStr, String pvScannedBarcodeStr) {
        cUserInterface.pDoExplodingScreen(pvErrorMessageStr, pvScannedBarcodeStr, true, true);
    }




    //End Region Private Methods


}
