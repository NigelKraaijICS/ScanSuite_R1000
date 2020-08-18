package nl.icsvertex.scansuite.Activities.Ship;

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
import ICS.Utils.cRegex;
import ICS.Utils.cResult;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Basics.BarcodeLayouts.cBarcodeLayout;
import SSU_WHS.Basics.ShippingAgentServiceShippingUnits.cShippingAgentServiceShippingUnit;
import SSU_WHS.Basics.ShippingAgentServiceShippingUnits.cShippingAgentServiceShippingUnitAdapter;
import SSU_WHS.General.cPublicDefinitions;
import SSU_WHS.Picken.Shipment.cShipment;
import nl.icsvertex.scansuite.Activities.QualityControl.QualityControlShipmentsActivity;
import nl.icsvertex.scansuite.Fragments.Dialogs.SendingFragment;
import nl.icsvertex.scansuite.Fragments.Ship.ShippingUnitFragment;
import nl.icsvertex.scansuite.R;

public class ShiporderShipActivity extends AppCompatActivity implements iICSDefaultActivity {


    //Region Public Properties

    //End Region Public Properties

    //Region Private Properties

    private ImageView toolbarImage;
    private TextView toolbarTitle;

    private TextView sourcenoText;
    private TextView addressNameText;
    private TextView addressAddressText;
    private TextView addressZipCodeText;
    private TextView addressCityText;
    private TextView addressCountryText;
    private TextView actionTextView;
    private TextView shippingAgentText;
    private TextView shippingServiceText;
    private RecyclerView recyclerUnitsUsed;
    private ImageView imageViewPackaging;
    private ImageView imageViewShippingDone;

   private cShippingAgentServiceShippingUnitAdapter shippingAgentServiceShippingUnitAdapter;
   private cShippingAgentServiceShippingUnitAdapter getShippingAgentServiceShippingUnitAdapter(){
       if (this.shippingAgentServiceShippingUnitAdapter == null) {
           this.shippingAgentServiceShippingUnitAdapter = new cShippingAgentServiceShippingUnitAdapter();
       }
       return  this.shippingAgentServiceShippingUnitAdapter;
   }

    //End Region Private Properties


    //Region Default Methods


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shiporder_ship);

        if (cShipment.currentShipment.shippingAgent() != null && cShipment.currentShipment.shippingAgentService() != null) {
            if (cShipment.currentShipment.shippingAgentService().shippingUnitsObl().size() == 1) {
                for (cShippingAgentServiceShippingUnit shippingAgentServiceShippingUnit :cShipment.currentShipment.shippingAgentService().shippingUnitsObl() ) {
                    shippingAgentServiceShippingUnit.ShippingUnitQuantityUsedInt = 1;
                }
            }
        }
        else {
            this.pHandleBackToLines();
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

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem pvMenuItem) {
        if (pvMenuItem.getItemId() == android.R.id.home) {
            this.pHandleBackToLines();
            return true;
        }
        return super.onOptionsItemSelected(pvMenuItem);
    }

    @Override
    public void onBackPressed() {
        this.pHandleBackToLines();
    }

    //End Region Default Methods


    //Region iICSDefaultActivity defaults

    @Override
    public void mActivityInitialize() {

        this.mSetAppExtensions();

        this.mFindViews();

        this.mSetToolbar(getResources().getString(R.string.screentitle_determine_transport));

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

        this.sourcenoText = findViewById(R.id.sourcenoText);

        this.addressNameText = findViewById(R.id.addressNameText);
        this.addressAddressText = findViewById(R.id.addressAddressText);
        this.addressZipCodeText = findViewById(R.id.addressZipCodeText);
        this.addressCityText = findViewById(R.id.addressCityText);
        this.addressCountryText = findViewById(R.id.addressCountryText);

        this.actionTextView = findViewById(R.id.actionTextView);

        this.shippingAgentText = findViewById(R.id.shippingAgentText);
        this.shippingServiceText = findViewById(R.id.shippingServiceText);

        this.recyclerUnitsUsed = findViewById(R.id.recyclerUnitsUsed);

        this.imageViewPackaging = findViewById(R.id.imageViewPackaging);
        this.imageViewShippingDone = findViewById(R.id.imageViewShippingDone);
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
        this.mSetActionText();
        this.mSetSourceNo();
        this.mSetAddress();
        this.mSetShippingInfo();
        this.mSetShippingUnits();
    }

    @Override
    public void mSetListeners() {
        this.mSetShippingUnitsListener();
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

            if (cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(),cBarcodeLayout.barcodeLayoutEnu.SHIPPINGPACKAGE)) {
                foundBln = true;
            }
            if (foundBln) {
                //has prefix, is shippingpackage
                barcodeWithoutPrefixStr = cRegex.pStripRegexPrefixStr(pvBarcodeScan.getBarcodeOriginalStr());
            }
            else {
                //has prefix, isn't shippingpackage
                cUserInterface.pDoExplodingScreen( cAppExtension.activity.getString(R.string.error_unknown_shippingunit), barcodeWithoutPrefixStr, true,true);
                return;
            }
        }

        if (barcodeWithoutPrefixStr.isEmpty()) {
            barcodeWithoutPrefixStr = pvBarcodeScan.getBarcodeOriginalStr();
        }


        cShippingAgentServiceShippingUnit.currentShippingAgentServiceShippingUnit = cShipment.currentShipment.shippingAgentService().pGetShippingAgentShippingUnitByStr(barcodeWithoutPrefixStr);

        if (cShippingAgentServiceShippingUnit.currentShippingAgentServiceShippingUnit == null) {
            cUserInterface.pDoExplodingScreen(cAppExtension.activity.getString(R.string.error_unknown_shippingunit), barcodeWithoutPrefixStr, true,true);
            return;
        }

        cShippingAgentServiceShippingUnit.currentShippingAgentServiceShippingUnit.ShippingUnitQuantityUsedInt += 1;
        this.mSetShippingUnits();
    }

    public  void pHandleSourceDocumentDone(){

        int numberShippingUnits = 0;

        for (cShippingAgentServiceShippingUnit shippingAgentServiceShippingUnit: cShipment.currentShipment.shippingAgentService().shippingUnitsObl()) {
            numberShippingUnits += shippingAgentServiceShippingUnit.getShippingUnitQuantityUsedInt();
        }

        if (numberShippingUnits == 0) {
            cUserInterface.pShowSnackbarMessage(this.recyclerUnitsUsed, cAppExtension.context.getString(R.string.ship_pick_at_least_one_package), R.raw.headsupsound, true);
            cUserInterface.pDoNope(imageViewShippingDone, false, false);
        }
        else {
            mShowSending();
            new Thread(new Runnable() {
                public void run() {
                    mSalesOrderDone();
                }
            }).start();
        }
    }

    public  void pHandleBackToLines(){

        //Reset quanity's
        for (cShippingAgentServiceShippingUnit shippingAgentServiceShippingUnit : cShipment.currentShipment.shippingAgentService().shippingUnitsObl()) {
            shippingAgentServiceShippingUnit.ShippingUnitQuantityUsedInt = 0;
        }


        this.mGoBackToLinesActivity();

    }

    //End Region Public Methods

    private void mSetActionText() {
        this.actionTextView.setVisibility(View.VISIBLE);
        this.actionTextView.setText(getString(R.string.select_shippingunit));
    }

    private  void mGoBackToLinesActivity() {

       Intent intent = null;

        switch (cShipment.currentShipment.currentShipmentModus) {
           case Ship:
                intent = new Intent(cAppExtension.context, ShiporderLinesActivity.class);

           case QC:
                intent = new Intent(cAppExtension.context, QualityControlShipmentsActivity.class);
       }

        cAppExtension.activity.startActivity(intent);
        cAppExtension.activity.finish();

    }

    //region default private voids

    //region private voids

    private void mSetSourceNo() {

        this.sourcenoText.setText(cShipment.currentShipment.getSourceNoStr());
        if (! cShipment.currentShipment.getProcessingSequenceStr().isEmpty()) {
            this.sourcenoText.setText(cShipment.currentShipment.getProcessingSequenceStr());
        }

    }

    private void mSetAddress() {

        if (cShipment.currentShipment.pickorderAddress() == null) {
            return;
        }

        this.addressNameText.setText(cShipment.currentShipment.pickorderAddress().getNameStr());
        this.addressAddressText.setText(cShipment.currentShipment.pickorderAddress().getAddressStr());
        this.addressZipCodeText.setText(cShipment.currentShipment.pickorderAddress().getZipcodeStr());
        this.addressCityText.setText(cShipment.currentShipment.pickorderAddress().getCityStr());
        this.addressCountryText.setText(cShipment.currentShipment.pickorderAddress().getCountryStr());

    }

    private void mSetShippingInfo() {

        this.shippingServiceText.setText(R.string.unknown_shippingagent);
        this.shippingServiceText.setText(R.string.unknown_shippingagentservice);

        if (cShipment.currentShipment.shippingAgent() == null) {
            return;
        }

        //Set the descriptionStr for the shipping agent
        this.shippingAgentText.setText(cShipment.currentShipment.shippingAgent().getDescriptionStr());

        //If service is unknown, then exit
        if (cShipment.currentShipment.shippingAgentService() == null) {
            return;
        }

        //Set the descriptionStr for the shipping agent
        this.shippingServiceText.setText(cShipment.currentShipment.shippingAgentService().getDescriptionStr());
    }

    private void mSetShippingUnits() {

        if (cShipment.currentShipment.shippingAgentService() == null || cShipment.currentShipment.shippingAgentService().shippingUnitsObl() == null || cShipment.currentShipment.shippingAgentService().shippingUnitsObl().size() == 0) {
            return;
        }

        this.mFillRecycler(cShipment.currentShipment.shippingAgentService().shippingUnitsObl());
    }

    private void mFillRecycler(List<cShippingAgentServiceShippingUnit> pvDataObl) {

        if (pvDataObl.size() == 0) {
            this.actionTextView.setVisibility(View.VISIBLE);
            this.actionTextView.setText(cAppExtension.activity.getString(R.string.select_shippingunit));
        }

        if (pvDataObl.size() > 0) {
            this.actionTextView.setVisibility(View.GONE);
        }


        this.getShippingAgentServiceShippingUnitAdapter().pFillData(pvDataObl);
        this.recyclerUnitsUsed.setHasFixedSize(false);
        this.recyclerUnitsUsed.setAdapter(this.getShippingAgentServiceShippingUnitAdapter());
        this.recyclerUnitsUsed.setLayoutManager(new LinearLayoutManager(cAppExtension.context));
    }

    private void mSetShippingUnitsListener() {
        this.imageViewPackaging.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mShowShippingUnitFragment();
            }
        });
    }

    private void mSetOrderDoneListener() {

        this.imageViewShippingDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pHandleSourceDocumentDone();
            }
        });
    }

    private void mSalesOrderDone() {

        cResult result = cShipment.currentShipment.pShipmentDoneRst();

        //Something went wrong
        if (! result.resultBln) {
            this.mShowShipmentNotSent(result.messagesStr());
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
        Fragment fragment = cAppExtension.fragmentManager.findFragmentByTag(cPublicDefinitions.SENDING_TAG);
        if (fragment != null) {
            if (fragment instanceof SendingFragment) {
                ((SendingFragment) fragment).pShowFlyAwayAnimation();
            }
        }
    }

    private  void mShowShipmentNotSent(String pvErrorMessageStr) {
        Fragment fragment = cAppExtension.fragmentManager.findFragmentByTag(cPublicDefinitions.SENDING_TAG);
        if (fragment != null) {
            if (fragment instanceof SendingFragment) {
                ((SendingFragment) fragment).pShowCrashAnimation(pvErrorMessageStr);
            }
        }
    }

    private void mShowShippingUnitFragment() {
        final ShippingUnitFragment shippingUnitFragment = new ShippingUnitFragment();
        shippingUnitFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.SHIPPINGFRAGMENT_LIST_TAG);
    }

    //endregion private voids

}
