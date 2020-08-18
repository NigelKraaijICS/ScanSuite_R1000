package nl.icsvertex.scansuite.Activities.QualityControl;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import ICS.Interfaces.iICSDefaultActivity;
import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cResult;
import ICS.Utils.cText;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Basics.BarcodeLayouts.cBarcodeLayout;
import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.General.cPublicDefinitions;
import SSU_WHS.Picken.PickorderLines.cPickorderLine;
import SSU_WHS.Picken.Pickorders.cPickorder;
import SSU_WHS.Picken.Shipment.cShipment;
import nl.icsvertex.scansuite.Activities.Ship.ShiporderShipActivity;
import nl.icsvertex.scansuite.Fragments.QualityControl.QCLinesToCheckFragment;
import nl.icsvertex.scansuite.PagerAdapters.QCLinesPagerAdapter;
import nl.icsvertex.scansuite.R;


//import android.app.Fragment;

public class QualityControlLinesActivity extends AppCompatActivity implements iICSDefaultActivity {

    //Region Public Properties
    public static Fragment currentLineFragment;
    //End Region Public Properties

    //Region Private Properties
    //Region Views

    private TextView textViewChosenOrder;
    private TextView quantityQCOrdersText;
    private TabLayout QCLinesTabLayout;
    private ViewPager QCLinesViewPager;

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

        this.mSetToolbar(getResources().getString(R.string.screentitle_qclines));

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

        this.toolbarImage = findViewById(R.id.toolbarImage);
        this.toolbarTitle = findViewById(R.id.toolbarTitle);

        this.quantityQCOrdersText = findViewById(R.id.QCordersText);
        this.QCLinesTabLayout = findViewById(R.id.QCOrdersTabLayout);
        this.QCLinesViewPager = findViewById(R.id.QCOrdersViewpager);
        this.textViewChosenOrder = findViewById(R.id.textViewChosenOrder);
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

        this.textViewChosenOrder.setText(cShipment.currentShipment.getProcessingSequenceStr());
        this.QCLinesTabLayout.addTab(QCLinesTabLayout.newTab().setText(R.string.tab_qcorderline_tocheck));
        this.QCLinesTabLayout.addTab(QCLinesTabLayout.newTab().setText(R.string.tab_qcorderline_checked));

        QCLinesPagerAdapter qcLinesPagerAdapter = new QCLinesPagerAdapter(this.QCLinesTabLayout.getTabCount());
        this.QCLinesViewPager.setAdapter(qcLinesPagerAdapter);
        this.QCLinesViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(this.QCLinesTabLayout));
        this.QCLinesTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab pvTab) {
                QCLinesViewPager.setCurrentItem(pvTab.getPosition());
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

}

    @Override
    public void mInitScreen() {

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

    public  void pChangeTabCounterText(String pvTextStr){
        this.quantityQCOrdersText.setText(pvTextStr);
    }

    public  void pHandleScan(cBarcodeScan pvBarcodeScan) {

        cUserInterface.pCheckAndCloseOpenDialogs();
        cResult hulpResult;

        if (!cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(),cBarcodeLayout.barcodeLayoutEnu.ARTICLE)) {
            this.mStepFailed(cAppExtension.activity.getString(R.string.message_article_required), pvBarcodeScan.getBarcodeOriginalStr());
            return;
        }

        if (QualityControlLinesActivity.currentLineFragment instanceof QCLinesToCheckFragment) {

            cPickorderLine.currentPickOrderLine = cShipment.currentShipment.pGetQCLineNotHandledByBarcode(pvBarcodeScan.getBarcodeOriginalStr());
            if ( cPickorderLine.currentPickOrderLine == null) {
                this.mStepFailed(cAppExtension.activity.getString(R.string.message_no_lines_for_this_barcode), pvBarcodeScan.getBarcodeOriginalStr());
                return;
            }

            hulpResult = cPickorderLine.currentPickOrderLine.pQCLineBusyRst();
            if (!hulpResult.resultBln) {
                this.mStepFailed(hulpResult.messagesStr(),"");
                cPickorderLine.currentPickOrderLine = null;
                return;
            }

            this.mStartQCActivity();
        }
    }

    //End Region Public Methods

    //Region Private Methods

    //Region Fragments and Activities

    private void mStartOrderSelectActivity() {
        Intent intent = new Intent(cAppExtension.context, QualityControlSelectActivity.class);
        cAppExtension.activity.startActivity(intent);
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


                            // Check if there are linesInt to be send, and send them, if we fail we stop
                            if (! mCheckAndSentLinesToBeSendBln()) {
                                return;
                            }

                            //We managed to send everuthing, so we are done
                            cPickorder.currentPickOrder.pLockReleaseViaWebserviceBln(cWarehouseorder.StepCodeEnu.Pick_QualityContol, cWarehouseorder.WorkflowPickStepEnu.PickQualityControl);
                            mStartOrderSelectActivity();
                        }
                    });

                    alertDialog2.show();
                }
                //There is no progress, so we are done
                else {
                    cPickorder.currentPickOrder.pLockReleaseViaWebserviceBln(cWarehouseorder.StepCodeEnu.Pick_QualityContol, cWarehouseorder.WorkflowPickStepEnu.PickQualityControl);
                    mStartOrderSelectActivity();
                }

            }
        });

        alertDialog.show();

    }

    private void mStartQCActivity(){
        //we have a line to handle, so start Sort activity
        Intent intent = new Intent(cAppExtension.context, PickorderQCActivity.class);
        cAppExtension.activity.startActivity(intent);
    }

    private void mSetTabselected(TabLayout.Tab pvTab) {

        switch (pvTab.getPosition()) {
            case 0:
                this.pChangeTabCounterText(cText.pIntToStringStr(cShipment.currentShipment.QCLinesToCheckObl().size()) + "/" + cText.pIntToStringStr(cShipment.currentShipment.QCLinesObl.size()));
                break;
            case 1:
                this.pChangeTabCounterText(cText.pIntToStringStr(cShipment.currentShipment.QCLinesCheckedObl().size()) + "/" + cText.pIntToStringStr(cShipment.currentShipment.QCLinesObl.size()));
                break;
            default:

        }
    }

    //End Region Fragments and Activities

    private Boolean mCheckAndSentLinesToBeSendBln() {

        boolean hulpBln;


        if (cShipment.currentShipment.QCLinesToCheckObl().size() > 0) {
            return  false;
        }

            // Try to send each line, if one failes then stop
            for (cPickorderLine pickorderLine : cShipment.currentShipment.QCLinesCheckedObl()) {

                //Set the current line
                cPickorderLine.currentPickOrderLine = pickorderLine;

                //Try to send the line
                hulpBln = cPickorderLine.currentPickOrderLine.pCheckedBln();
                if ( !hulpBln) {
                    return  false;
                }

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

        // Ship this order
        this.mStartShipActivity();
    }

    private Boolean mAllLinesDoneBln() {
        return cShipment.currentShipment.QCLinesToCheckObl().size() > 0;
    }

    private  void mStepFailed(String pvErrorMessageStr, String pvScannedBarcodeStr) {
        cUserInterface.pDoExplodingScreen(pvErrorMessageStr, pvScannedBarcodeStr, true, true);
    }

    private void mStartShipActivity(){
        //we have a SourceDocument to handle, so start Ship activity
        Intent intent = new Intent(cAppExtension.context, ShiporderShipActivity.class);
        cAppExtension.activity.startActivity(intent);
    }

    //End Region Private Methods


}
