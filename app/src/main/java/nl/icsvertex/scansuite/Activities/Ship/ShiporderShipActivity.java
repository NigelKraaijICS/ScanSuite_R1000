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
import SSU_WHS.General.cPublicDefinitions;
import SSU_WHS.Picken.Shipment.cShipment;
import nl.icsvertex.scansuite.Fragments.Dialogs.SendingFragment;
import nl.icsvertex.scansuite.Fragments.Ship.ShippingUnitFragment;
import nl.icsvertex.scansuite.R;

public class ShiporderShipActivity extends AppCompatActivity implements iICSDefaultActivity {


    //Region Public Properties

    //End Region Public Properties

    //Region Private Properties

    private static String SENDING_TAG = "SENDING_TAG";

    private static ImageView toolbarImage;
    private static TextView toolbarTitle;

    private static TextView sourcenoText;
    private static TextView addressNameText;
    private static TextView addressAddressText;
    private static TextView addressZipCodeText;
    private static TextView addressCityText;
    private static TextView addressCountryText;
    private static TextView actionTextView;
    private static TextView shippingAgentText;
    private static TextView shippingServiceText;
    private static RecyclerView recyclerUnitsUsed;
    private static ImageView imageViewPackaging;
    private static ImageView imageViewShippingDone;

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
            ShiporderShipActivity.pHandleBackToLines();
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
    public boolean onOptionsItemSelected(MenuItem pvMenuItem) {
        if (pvMenuItem.getItemId() == android.R.id.home) {
            ShiporderShipActivity.pHandleBackToLines();
            return true;
        }
        return super.onOptionsItemSelected(pvMenuItem);
    }

    @Override
    public void onBackPressed() {
        ShiporderShipActivity.pHandleBackToLines();
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

        ShiporderShipActivity.toolbarImage = findViewById(R.id.toolbarImage);
        ShiporderShipActivity.toolbarTitle = findViewById(R.id.toolbarTitle);

        ShiporderShipActivity.sourcenoText = findViewById(R.id.sourcenoText);

        ShiporderShipActivity.addressNameText = findViewById(R.id.addressNameText);
        ShiporderShipActivity.addressAddressText = findViewById(R.id.addressAddressText);
        ShiporderShipActivity.addressZipCodeText = findViewById(R.id.addressZipCodeText);
        ShiporderShipActivity.addressCityText = findViewById(R.id.addressCityText);
        ShiporderShipActivity.addressCountryText = findViewById(R.id.addressCountryText);

        ShiporderShipActivity.actionTextView = findViewById(R.id.actionTextView);

        ShiporderShipActivity.shippingAgentText = findViewById(R.id.shippingAgentText);
        ShiporderShipActivity.shippingServiceText = findViewById(R.id.shippingServiceText);

        ShiporderShipActivity.recyclerUnitsUsed = findViewById(R.id.recyclerUnitsUsed);

        ShiporderShipActivity.imageViewPackaging = findViewById(R.id.imageViewPackaging);
        ShiporderShipActivity.imageViewShippingDone = findViewById(R.id.imageViewShippingDone);
    }

    @Override
    public void mSetToolbar(String pvScreenTitle) {
        ShiporderShipActivity.toolbarImage.setImageResource(R.drawable.ic_menu_ship);
        ShiporderShipActivity.toolbarTitle.setText(pvScreenTitle);
        ShiporderShipActivity.toolbarTitle.setSelected(true);

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
        ShiporderShipActivity.mSetShippingUnits();
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

    public static void pHandleScan(cBarcodeScan pvBarcodeScan) {

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
        ShiporderShipActivity.mSetShippingUnits();
    }

    public static void pHandleSourceDocumentDone(){

        int numberShippingUnits = 0;

        for (cShippingAgentServiceShippingUnit shippingAgentServiceShippingUnit: cShipment.currentShipment.shippingAgentService().shippingUnitsObl()) {
            numberShippingUnits += shippingAgentServiceShippingUnit.getShippingUnitQuantityUsedInt();
        }

        if (numberShippingUnits == 0) {
            cUserInterface.pShowSnackbarMessage(ShiporderShipActivity.recyclerUnitsUsed, cAppExtension.context.getString(R.string.ship_pick_at_least_one_package), R.raw.headsupsound, true);
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

    public static void pHandleBackToLines(){

        //Reset quanity's
        for (cShippingAgentServiceShippingUnit shippingAgentServiceShippingUnit : cShipment.currentShipment.shippingAgentService().shippingUnitsObl()) {
            shippingAgentServiceShippingUnit.ShippingUnitQuantityUsedInt = 0;
        }

        ShiporderShipActivity.mGoBackToLinesActivity();


    }

    //End Region Public Methods

    private void mSetActionText() {
        ShiporderShipActivity.actionTextView.setVisibility(View.VISIBLE);
        ShiporderShipActivity.actionTextView.setText(getString(R.string.select_shippingunit));
    }

    private static void mGoBackToLinesActivity() {
        Intent intent = new Intent(cAppExtension.context, ShiporderLinesActivity.class);
        cAppExtension.activity.startActivity(intent);
        cAppExtension.activity.finish();
    }

    //region default private voids

    //region private voids

    private void mSetSourceNo() {

        ShiporderShipActivity.sourcenoText.setText(cShipment.currentShipment.getSourceNoStr());
        if (! cShipment.currentShipment.getProcessingSequenceStr().isEmpty()) {
            ShiporderShipActivity.sourcenoText.setText(cShipment.currentShipment.getProcessingSequenceStr());
        }

    }

    private void mSetAddress() {

        if (cShipment.currentShipment.pickorderAddress() == null) {
            return;
        }

        ShiporderShipActivity.addressNameText.setText(cShipment.currentShipment.pickorderAddress().getNameStr());
        ShiporderShipActivity.addressAddressText.setText(cShipment.currentShipment.pickorderAddress().getAddressStr());
        ShiporderShipActivity.addressZipCodeText.setText(cShipment.currentShipment.pickorderAddress().getZipcodeStr());
        ShiporderShipActivity.addressCityText.setText(cShipment.currentShipment.pickorderAddress().getCityStr());
        ShiporderShipActivity.addressCountryText.setText(cShipment.currentShipment.pickorderAddress().getCountryStr());

    }

    private void mSetShippingInfo() {

        ShiporderShipActivity.shippingServiceText.setText(R.string.unknown_shippingagent);
        ShiporderShipActivity.shippingServiceText.setText(R.string.unknown_shippingagentservice);

        if (cShipment.currentShipment.shippingAgent() == null) {
            return;
        }

        //Set the descriptionStr for the shipping agent
        ShiporderShipActivity.shippingAgentText.setText(cShipment.currentShipment.shippingAgent().getDescriptionStr());

        //If service is unknown, then exit
        if (cShipment.currentShipment.shippingAgentService() == null) {
            return;
        }

        //Set the descriptionStr for the shipping agent
        ShiporderShipActivity.shippingServiceText.setText(cShipment.currentShipment.shippingAgentService().getDescriptionStr());
    }

    private static void mSetShippingUnits() {

        if (cShipment.currentShipment.shippingAgentService() == null || cShipment.currentShipment.shippingAgentService().shippingUnitsObl() == null || cShipment.currentShipment.shippingAgentService().shippingUnitsObl().size() == 0) {
            return;
        }

        ShiporderShipActivity.mFillRecycler(cShipment.currentShipment.shippingAgentService().shippingUnitsObl());
    }

    private static void mFillRecycler(List<cShippingAgentServiceShippingUnit> pvDataObl) {

        if (pvDataObl.size() == 0) {
            ShiporderShipActivity.actionTextView.setVisibility(View.VISIBLE);
            ShiporderShipActivity.actionTextView.setText(cAppExtension.activity.getString(R.string.select_shippingunit));
        }

        if (pvDataObl.size() > 0) {
            ShiporderShipActivity.actionTextView.setVisibility(View.GONE);
        }

        cShippingAgentServiceShippingUnit.getShippingAgentServiceShippingUnitAdapter().pFillData(pvDataObl);
        ShiporderShipActivity.recyclerUnitsUsed.setHasFixedSize(false);
        ShiporderShipActivity.recyclerUnitsUsed.setAdapter(cShippingAgentServiceShippingUnit.getShippingAgentServiceShippingUnitAdapter());
        ShiporderShipActivity.recyclerUnitsUsed.setLayoutManager(new LinearLayoutManager(cAppExtension.context));
    }

    private void mSetShippingUnitsListener() {
        ShiporderShipActivity.imageViewPackaging.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mShowShippingUnitFragment();
            }
        });
    }

    private void mSetOrderDoneListener() {

        ShiporderShipActivity.imageViewShippingDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShiporderShipActivity.pHandleSourceDocumentDone();
            }
        });
    }

    private static void mSalesOrderDone() {

        cResult result = cShipment.currentShipment.pShipmentDoneRst();

        //Something went wrong
        if (! result.resultBln) {
            ShiporderShipActivity.mShowShipmentNotSent(result.messagesStr());
            return;
        }
        //We are done, so show we are done
        ShiporderShipActivity.mShowSent();

    }

    private static void mShowSending() {
        final SendingFragment sendingFragment = new SendingFragment();
        sendingFragment.setCancelable(true);
        cAppExtension.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // show my popup
                sendingFragment.show(cAppExtension.fragmentManager, SENDING_TAG);
            }
        });
    }

    private static void mShowSent() {
        Fragment fragment = cAppExtension.fragmentManager.findFragmentByTag(SENDING_TAG);
        if (fragment != null) {
            if (fragment instanceof SendingFragment) {
                ((SendingFragment) fragment).pShowFlyAwayAnimation();
            }
        }
    }

    private static void mShowShipmentNotSent(String pvErrorMessageStr) {
        Fragment fragment = cAppExtension.fragmentManager.findFragmentByTag(SENDING_TAG);
        if (fragment != null) {
            if (fragment instanceof SendingFragment) {
                ((SendingFragment) fragment).pShowCrashAnimation(pvErrorMessageStr);
            }
        }
    }

    private void mShowShippingUnitFragment() {
        final ShippingUnitFragment shippingUnitFragment = new ShippingUnitFragment();
        shippingUnitFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.BRANCHFRAGMENT_LIST_TAG);
    }

    //endregion private voids

}
