package nl.icsvertex.scansuite.Activities.ship;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
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
import nl.icsvertex.scansuite.Fragments.dialogs.SendingFragment;
import nl.icsvertex.scansuite.Fragments.ship.ShippingUnitFragment;
import nl.icsvertex.scansuite.R;

public class ShiporderShipActivity extends AppCompatActivity implements iICSDefaultActivity {


    //Region Public Properties

    //End Region Public Properties

    //Region Private Properties

    private static String SENDING_TAG = "SENDING_TAG";

    private ImageView toolbarImage;
    private TextView toolbarTitle;
    private ImageView toolbarImageHelp;
    private TextView sourcenoText;
    private CardView addressContainer;
    private TextView addressNameText;
    private TextView addressAddressText;
    private TextView addressZipCodeText;
    private TextView addressCityText;
    private TextView addressCountryText;
    private static TextView actionTextView;
    private CardView shippingInfoContainer;
    private  TextView shippingAgentText;
    private TextView shippingServiceText;
    private static RecyclerView recyclerUnitsUsed;
    private ImageView imageViewPackaging;
    private static ImageView imageViewShippingDone;

    //End Region Private Properties


    //Region Default Methods


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shiporder_ship);


        if (cShipment.currentShipment.shippingAgentService().shippingUnitsObl().size() == 1) {
            for (cShippingAgentServiceShippingUnit shippingAgentServiceShippingUnit :cShipment.currentShipment.shippingAgentService().shippingUnitsObl() ) {
                shippingAgentServiceShippingUnit.ShippingUnitQuantityUsedInt = 1;
            }

        }

        this.mActivityInitialize();




    }

    @Override
    protected void onDestroy() {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    @Override
    public void onResume() {
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

        this.toolbarImage = findViewById(R.id.toolbarImage);
        this.toolbarTitle = findViewById(R.id.toolbarTitle);
        this.toolbarImageHelp = findViewById(R.id.toolbarImageHelp);

        this.sourcenoText = findViewById(R.id.sourcenoText);

        this.addressContainer = findViewById(R.id.addressContainer);
        this.addressNameText = findViewById(R.id.addressNameText);
        this.addressAddressText = findViewById(R.id.addressAddressText);
        this.addressZipCodeText = findViewById(R.id.addressZipCodeText);
        this.addressCityText = findViewById(R.id.addressCityText);
        this.addressCountryText = findViewById(R.id.addressCountryText);

        this.actionTextView = findViewById(R.id.actionTextView);

        this.shippingInfoContainer = findViewById(R.id.shippingInfoContainer);
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

    public static void pHandleScan(String pvScannedBarcodeStr) {

        cUserInterface.pCheckAndCloseOpenDialogs();
        String barcodeWithoutPrefixStr = "";

        if (cRegex.hasPrefix(pvScannedBarcodeStr)) {

            Boolean foundBln = false;

            if (cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvScannedBarcodeStr,cBarcodeLayout.barcodeLayoutEnu.SHIPPINGPACKAGE)) {
                foundBln = true;
            }
            if (foundBln) {
                //has prefix, is shippingpackage
                barcodeWithoutPrefixStr = cRegex.pStripRegexPrefixStr(pvScannedBarcodeStr);
            }
            else {
                //has prefix, isn't shippingpackage
                cUserInterface.pDoExplodingScreen( cAppExtension.activity.getString(R.string.error_unknown_shippingunit), barcodeWithoutPrefixStr, true,true);
                return;
            }
        }

        if (barcodeWithoutPrefixStr.isEmpty()) {
            barcodeWithoutPrefixStr = pvScannedBarcodeStr;
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
            return;
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
        this.actionTextView.setVisibility(View.VISIBLE);
        this.actionTextView.setText(getString(R.string.select_shippingunit));
    }

    private static void mGoBackToLinesActivity() {
        Intent intent = new Intent(cAppExtension.context, ShiporderLinesActivity.class);
        cAppExtension.activity.startActivity(intent);
        cAppExtension.activity.finish();
    }

    //region default private voids

    //region private voids

    private void mSetSourceNo() {

        this.sourcenoText.setText(cShipment.currentShipment.getSourceNoStr());
        if (! cShipment.currentShipment.getProcessingSequenceStr().isEmpty()) {
            sourcenoText.setText(cShipment.currentShipment.getProcessingSequenceStr());
            return;
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

        //Set the description for the shipping agent
        this.shippingAgentText.setText(cShipment.currentShipment.shippingAgent().getDescriptionStr());

        //If service is unknown, then exit
        if (cShipment.currentShipment.shippingAgentService() == null) {
            return;
        }

        //Set the description for the shipping agent
        this.shippingServiceText.setText(cShipment.currentShipment.shippingAgentService().getDescriptionStr());
    }

    private static void mSetShippingUnits() {

        if (cShipment.currentShipment.shippingAgentService() == null || cShipment.currentShipment.shippingAgentService().shippingUnitsObl() == null || cShipment.currentShipment.shippingAgentService().shippingUnitsObl().size() == 0) {
            return;
        }

        ShiporderShipActivity.mFillRecycler(cShipment.currentShipment.shippingAgentService().shippingUnitsObl());
    }

    private static void mFillRecycler(List<cShippingAgentServiceShippingUnit> pvDataObl) {

        if (pvDataObl.size() == 0) {
            actionTextView.setVisibility(View.VISIBLE);
            actionTextView.setText(cAppExtension.activity.getString(R.string.select_shippingunit));
        }

        if (pvDataObl.size() > 0) {
            actionTextView.setVisibility(View.GONE);
        }

        cShippingAgentServiceShippingUnit.getShippingAgentServiceShippingUnitAdapter().pFillData(pvDataObl);
        ShiporderShipActivity.recyclerUnitsUsed.setHasFixedSize(false);
        ShiporderShipActivity.recyclerUnitsUsed.setAdapter(cShippingAgentServiceShippingUnit.getShippingAgentServiceShippingUnitAdapter());
        ShiporderShipActivity.recyclerUnitsUsed.setLayoutManager(new LinearLayoutManager(cAppExtension.context));
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
