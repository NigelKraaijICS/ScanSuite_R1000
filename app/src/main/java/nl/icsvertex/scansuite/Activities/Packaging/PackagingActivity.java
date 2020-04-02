package nl.icsvertex.scansuite.Activities.Packaging;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ICS.Interfaces.iICSDefaultActivity;
import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cProductFlavor;
import ICS.Utils.cRegex;
import ICS.Utils.cResult;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Basics.BarcodeLayouts.cBarcodeLayout;
import SSU_WHS.Basics.Packaging.cPackaging;
import SSU_WHS.Basics.Packaging.cPackagingAdapter;
import SSU_WHS.General.cPublicDefinitions;
import SSU_WHS.Intake.Intakeorders.cIntakeorder;
import nl.icsvertex.scansuite.Activities.IntakeAndReceive.IntakeAndReceiveSelectActivity;
import nl.icsvertex.scansuite.Activities.Receive.ReceiveLinesActivity;
import nl.icsvertex.scansuite.BuildConfig;
import nl.icsvertex.scansuite.Fragments.Dialogs.SendingFragment;
import nl.icsvertex.scansuite.Fragments.Packaging.AcceptPackagingFragment;
import nl.icsvertex.scansuite.R;

public class PackagingActivity extends AppCompatActivity implements iICSDefaultActivity {

    //Region Public Properties
    //End Region Public Properties

    //Region Private Properties

    private ImageView toolbarImage;
    private TextView toolbarTitle;

    private TextView textViewChosenOrder;
    private RecyclerView recyclerUnitsUsed;

    private ImageView imageButtonCloseOrder;

    private cPackagingAdapter packagingAdapter;
    private cPackagingAdapter getPackagingAdapter(){
       if (this.packagingAdapter == null) {
           this.packagingAdapter = new cPackagingAdapter();
       }
       return  this.packagingAdapter;
   }

    //End Region Private Properties


    //Region Default Methods


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_packaging);

        if (cIntakeorder.currentIntakeOrder.packagingObl == null || cIntakeorder.currentIntakeOrder.packagingObl .size() ==0) {
            this.pDone();
        }

        this.mActivityInitialize();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
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

    @Override protected void onStop() {
        super.onStop();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem pvMenuItem) {
        if (pvMenuItem.getItemId() == android.R.id.home) {
            ReceiveLinesActivity.packagingHandledBln = false;
            this.pDone();
            return true;
        }
        return super.onOptionsItemSelected(pvMenuItem);
    }

    @Override
    public void onBackPressed() {
        ReceiveLinesActivity.packagingHandledBln = false;
        this.finish();
    }

    //End Region Default Methods


    //Region iICSDefaultActivity defaults

    @Override
    public void mActivityInitialize() {

        this.mSetAppExtensions();

        this.mFindViews();

        this.mSetToolbar(getResources().getString(R.string.screentitle_packaging));

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

        this.textViewChosenOrder = findViewById(R.id.textViewChosenOrder);

        this.recyclerUnitsUsed = findViewById(R.id.recyclerUnitsUsed);


        this.imageButtonCloseOrder = findViewById(R.id.imageButtonCloseOrder);
    }

    @Override
    public void mSetToolbar(String pvScreenTitle) {
        this.toolbarImage.setImageResource(R.drawable.ic_menu_ship);
        this.toolbarTitle.setText(pvScreenTitle);
        this.toolbarTitle.setSelected(true);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void mFieldsInitialize() {
        this.mSetSourceNo();
        this.mSetShippingUnits();

        this.imageButtonCloseOrder.setVisibility(View.VISIBLE);
    }

    @Override
    public void mSetListeners() {
        this.mSetOrderDoneListener();

    }

    @Override
    public void mInitScreen() {
        cBarcodeScan.pRegisterBarcodeReceiver();
    }

    //End Region iICSDefaultActivity defaults


    //Region Public Methods

    public void pHandleScan(cBarcodeScan pvBarcodeScan) {

        cUserInterface.pCheckAndCloseOpenDialogs();
        String barcodeWithoutPrefixStr = "";

        if (cRegex.pHasPrefix(pvBarcodeScan.getBarcodeOriginalStr())) {

            boolean foundBln = false;

            if (cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(),cBarcodeLayout.barcodeLayoutEnu.PACKAGING)) {
                foundBln = true;
            }
            if (foundBln) {
                //has prefix, is shippingpackage
                barcodeWithoutPrefixStr = cRegex.pStripRegexPrefixStr(pvBarcodeScan.getBarcodeOriginalStr());
            }
            else {
                //has prefix, isn't shippingpackage
                cUserInterface.pDoExplodingScreen( cAppExtension.activity.getString(R.string.error_unknown_packagingunit), barcodeWithoutPrefixStr, true,true);
                return;
            }
        }

        if (barcodeWithoutPrefixStr.isEmpty()) {
            barcodeWithoutPrefixStr = pvBarcodeScan.getBarcodeOriginalStr();
        }

        cPackaging.currentPackaging = cPackaging.pGetPackagingUnitByStr(pvBarcodeScan.getBarcodeOriginalStr());

        if (cPackaging.currentPackaging  == null) {
            cUserInterface.pDoExplodingScreen(cAppExtension.activity.getString(R.string.error_unknown_packagingunit), barcodeWithoutPrefixStr, true,true);
            return;
        }

        cPackaging.currentPackaging.quantityUsedInt += 1;
        this.mSetShippingUnits();
    }

    public  void pHandlePackagingDone(){

            mShowSending();
            new Thread(new Runnable() {
                public void run() {
                    mPackagingDone();
                }
            }).start();

    }

    public  void pDone(){
        this.mGoBackToIntakeAndReceiveSelectActivity();
    }

    //End Region Public Methods

    private  void mGoBackToIntakeAndReceiveSelectActivity() {
        Intent intent = new Intent(cAppExtension.context, IntakeAndReceiveSelectActivity.class);
        cAppExtension.activity.startActivity(intent);
        cAppExtension.activity.finish();
    }

    private void mShowClosePackagingDialog(String pvRejectStr, String pvAcceptStr) {

        cUserInterface.pCheckAndCloseOpenDialogs();

        final AcceptPackagingFragment acceptPackagingFragment = new AcceptPackagingFragment(cAppExtension.activity.getString(R.string.message_close_packaging_question),
                                                                                            pvRejectStr,
                                                                                            pvAcceptStr);

        acceptPackagingFragment.setCancelable(true);
        cAppExtension.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // show my popup
                acceptPackagingFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.ACCEPTPACKAGINGFRAGMENT_TAG);
            }
        });

    }


    //region default private voids

    //region private voids

    private void mSetSourceNo() {

        if (BuildConfig.FLAVOR.equalsIgnoreCase(cProductFlavor.FlavorEnu.BMN.toString())) {
            this.textViewChosenOrder.setText(cIntakeorder.currentIntakeOrder.getDocumentStr());

        }
        else
        {
            this.textViewChosenOrder.setText(cIntakeorder.currentIntakeOrder.getOrderNumberStr());
        }

    }

    private void mSetShippingUnits() {
        this.mFillRecycler(cIntakeorder.currentIntakeOrder.packagingObl);
    }

    private void mFillRecycler(List<cPackaging> pvDataObl) {

        this.getPackagingAdapter().pFillData(pvDataObl);
        this.recyclerUnitsUsed.setHasFixedSize(false);
        this.recyclerUnitsUsed.setAdapter(this.getPackagingAdapter());
        this.recyclerUnitsUsed.setLayoutManager(new LinearLayoutManager(cAppExtension.context));
    }

    private void mSetOrderDoneListener() {

        this.imageButtonCloseOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mShowClosePackagingDialog(cAppExtension.activity.getString(R.string.message_cancel),cAppExtension.activity.getString(R.string.message_close));
            }
        });
    }

    private void mPackagingDone() {

        cResult result = cIntakeorder.currentIntakeOrder.pPackagingHandledViaWebserviceRst();

        //Something went wrong
        if (! result.resultBln) {
            this.mShowpackagingNotSent(result.messagesStr());
            return;
        }

        result = cIntakeorder.currentIntakeOrder.pReceiveHandledViaWebserviceRst();
        //Everything was fine, so we are done
        if (! result.resultBln) {
            this.mShowpackagingNotSent(result.messagesStr());
            return;
        }

        //We are done, so show we are done
        this.mShowSent();

    }

    private  void mShowSending() {
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
        ReceiveLinesActivity.packagingHandledBln = true;
        Fragment fragment = cAppExtension.fragmentManager.findFragmentByTag(cPublicDefinitions.SENDING_TAG);
        if (fragment != null) {
            if (fragment instanceof SendingFragment) {
                ((SendingFragment) fragment).pShowFlyAwayAnimation();
            }
        }
    }

    private  void mShowpackagingNotSent(String pvErrorMessageStr) {
        Fragment fragment = cAppExtension.fragmentManager.findFragmentByTag(cPublicDefinitions.SENDING_TAG);
        if (fragment != null) {
            if (fragment instanceof SendingFragment) {
                ((SendingFragment) fragment).pShowCrashAnimation(pvErrorMessageStr);
            }
        }
    }

    //endregion private voids

}
