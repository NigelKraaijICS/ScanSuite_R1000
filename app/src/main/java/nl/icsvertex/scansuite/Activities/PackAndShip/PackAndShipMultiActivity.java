package nl.icsvertex.scansuite.Activities.PackAndShip;

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
import SSU_WHS.Basics.ShippingAgentServiceShippingUnits.cShippingAgentServiceShippingUnit;
import SSU_WHS.Basics.ShippingAgentServiceShippingUnits.cShippingAgentServiceShippingUnitAdapter;
import SSU_WHS.Basics.Users.cUser;
import SSU_WHS.General.cPublicDefinitions;
import SSU_WHS.PackAndShip.PackAndShipAddress.cPackAndShipAddress;
import SSU_WHS.PackAndShip.PackAndShipBarcode.cPackAndShipBarcode;
import SSU_WHS.PackAndShip.PackAndShipBarcode.cPackAndShipOrderBarcodeAdapter;
import SSU_WHS.PackAndShip.PackAndShipLines.cPackAndShipLine;
import SSU_WHS.PackAndShip.PackAndShipOrders.cPackAndShipOrder;
import SSU_WHS.PackAndShip.PackAndShipShipment.cPackAndShipShipment;
import nl.icsvertex.scansuite.Fragments.Dialogs.SendingFragment;
import nl.icsvertex.scansuite.Fragments.Ship.ShippingUnitFragment;
import nl.icsvertex.scansuite.R;

public class PackAndShipMultiActivity extends AppCompatActivity implements iICSDefaultActivity {


    //Region Public Properties

    //End Region Public Properties

    //Region Private Properties

    private ImageView toolbarImage;
    private TextView toolbarTitle;
    private TextView toolbarSubTitle;

    private TextView sourcenoText;

    private TextView addressNameText;
    private TextView addressAddressText;
    private TextView addressZipCodeText;
    private TextView addressCityText;
    private TextView addressCountryText;

    private TextView shippingAgentText;
    private TextView shippingServiceText;

    private RecyclerView recyclerBarcodes;
    private RecyclerView recyclerUnitsUsed;

    private ImageView imageViewShippingDone;
    private ImageView imageViewPackaging;

    private TextView actionTextView;

    private String scannedDocumentStr;

    private cShippingAgentServiceShippingUnitAdapter shippingAgentServiceShippingUnitAdapter;
    private cShippingAgentServiceShippingUnitAdapter getShippingAgentServiceShippingUnitAdapter(){
        if (this.shippingAgentServiceShippingUnitAdapter == null) {
            this.shippingAgentServiceShippingUnitAdapter = new cShippingAgentServiceShippingUnitAdapter();
        }
        return  this.shippingAgentServiceShippingUnitAdapter;
    }

    private cPackAndShipOrderBarcodeAdapter packAndShipOrderBarcodeAdapter;
    private cPackAndShipOrderBarcodeAdapter getPackAndShipOrderBarcodeAdapter(){
        if (this.packAndShipOrderBarcodeAdapter == null) {
            this.packAndShipOrderBarcodeAdapter = new cPackAndShipOrderBarcodeAdapter();
        }
        return  this.packAndShipOrderBarcodeAdapter;
    }

    //End Region Private Properties

    //Region Default Methods


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_packandshiporder_ship_multi);
        this.mActivityInitialize();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
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

        this.mSetToolbar(getResources().getString(R.string.screentitle_packandship_multi));

        this.mFieldsInitialize();

        this.mSetListeners();

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

        this.sourcenoText = findViewById(R.id.sourcenoText);

        this.addressNameText = findViewById(R.id.addressNameText);
        this.addressAddressText = findViewById(R.id.addressAddressText);
        this.addressZipCodeText = findViewById(R.id.addressZipCodeText);
        this.addressCityText = findViewById(R.id.addressCityText);
        this.addressCountryText = findViewById(R.id.addressCountryText);

        this.actionTextView = findViewById(R.id.actionTextView);

        this.shippingAgentText = findViewById(R.id.shippingAgentText);
        this.shippingServiceText = findViewById(R.id.shippingServiceText);

        this.recyclerBarcodes = findViewById(R.id.recyclerBarcodes);
        this.recyclerUnitsUsed = findViewById(R.id.recyclerUnitsUsed);

        this.imageViewPackaging = findViewById(R.id.imageViewPackaging);
        this.imageViewShippingDone = findViewById(R.id.imageViewShippingDone);
    }

    @Override
    public void mSetToolbar(final String pvScreenTitleStr) {

        if (cUser.currentUser.currentAuthorisation.getCustomAuthorisation() != null) {
            this.toolbarImage.setImageBitmap(cUser.currentUser.currentAuthorisation.customImageBmp());
            this.toolbarTitle.setText(cUser.currentUser.currentAuthorisation.getCustomAuthorisation().getDescriptionStr());
        }
        else {
            this.toolbarImage.setImageResource(R.drawable.ic_menu_ship);
            this.toolbarTitle.setText(pvScreenTitleStr);
        }

        this.toolbarTitle.setSelected(true);

        if (cPackAndShipOrder.currentPackAndShipOrder != null) {
            this.toolbarSubTitle.setText(cPackAndShipOrder.currentPackAndShipOrder.getOrderNumberStr());
        }

        else
        {
            this.toolbarSubTitle.setText(R.string.novalueyet);
        }

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

        if (cPackAndShipOrder.currentPackAndShipOrder != null && cPackAndShipOrder.currentPackAndShipOrder.barcodesObl() != null && cPackAndShipOrder.currentPackAndShipOrder.barcodesObl().size() > 0 ) {
            this.scannedDocumentStr = cPackAndShipOrder.currentPackAndShipOrder.barcodesObl().get(0).getBarcodeStr();
        }

        cAppExtension.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mSetToolbar(getResources().getString(R.string.screentitle_packandship_multi));
                mSetActionText();
                mSetSourceNo();
                mSetAddress();
                mSetShippingInfo();
                mSetBarcodesUnits();
                mSetShippingUnits();
                mSetButtons();
            }
        });
    }

    @Override
    public void mSetListeners() {
        this.mSetShippingUnitsListener();
        this.mSetDocumentDoneListener();
    }

    @Override
    public void mInitScreen() {
        cBarcodeScan.pRegisterBarcodeReceiver(this.getClass().getSimpleName());
    }

    //End Region iICSDefaultActivity defaults


    //Region Public Methods

    public void pHandleScan(final cBarcodeScan pvBarcodeScan) {

        cUserInterface.pCheckAndCloseOpenDialogs();

        // Show that we are getting data
        cUserInterface.pShowGettingData();

        new Thread(new Runnable() {
            public void run() {
                mHandleDocumentScan(cRegex.pStripRegexPrefixStr(pvBarcodeScan.getBarcodeOriginalStr()));
            }
        }).start();

    }

    public  void pHandleDocumentDone(){
        mShowSending();
        new Thread(new Runnable() {
            public void run() {
                mDocumentDone();
            }
        }).start();
    }

    public  void pHandleBackToLines(){
        this.mGoBackToSelectActivity();
    }

    //End Region Public Methods

    private  void mGoBackToSelectActivity() {

        if (cPackAndShipOrder.currentPackAndShipOrder != null) {


            if (cPackAndShipShipment.allShipmentsObl== null || cPackAndShipShipment.allShipmentsObl.size() == 0) {
                cPackAndShipOrder.currentPackAndShipOrder.pDeleteViaWebserviceBln();
            }
            else
            {
                cPackAndShipOrder.currentPackAndShipOrder.pLockReleaseViaWebserviceBln();
            }

        }

        this.mResetCurrents();

       PackAndShipSelectActivity.startedViaMenuBln = false;
       Intent intent =new Intent(cAppExtension.context, PackAndShipSelectActivity.class);
       cAppExtension.activity.startActivity(intent);
       cAppExtension.activity.finish();

    }

    private void mResetCurrents() {
        this.scannedDocumentStr = "";

        cPackAndShipOrder.currentPackAndShipOrder = null;
        cPackAndShipShipment.currentShipment = null;

        cPackAndShipAddress.pTruncateTableBln();
        cPackAndShipBarcode.pTruncateTableBln();
        cPackAndShipLine.pTruncateTableBln();
    }

    private void mSetActionText() {

        this.actionTextView.setVisibility(View.VISIBLE);

        if (cPackAndShipShipment.currentShipment  == null) {
            this.actionTextView.setText(getString(R.string.scan_document));
            return;
        }

        this.actionTextView.setText(getString(R.string.scan_next_document_to_close_current));

    }

    private void mSetSourceNo() {

        if (cPackAndShipShipment.currentShipment == null) {
            this.sourcenoText.setText(cAppExtension.activity.getString(R.string.novalueyet));
            return;
        }
        this.sourcenoText.setText(cPackAndShipShipment.currentShipment.getSourceNoStr());
    }

    private void mSetAddress() {

        if (cPackAndShipOrder.currentPackAndShipOrder == null ||cPackAndShipShipment.currentShipment == null ||  cPackAndShipShipment.currentShipment.deliveryAddress() == null) {
            this.addressNameText.setText(R.string.novalueyet);
            this.addressAddressText.setText(R.string.novalueyet);
            this.addressZipCodeText.setText(R.string.novalueyet);
            this.addressCityText.setText(R.string.novalueyet);
            this.addressCountryText.setText(R.string.novalueyet);
            return;
        }

        this.addressNameText.setText(cPackAndShipShipment.currentShipment.deliveryAddress().getAddressNameStr());
        this.addressAddressText.setText(cPackAndShipShipment.currentShipment.deliveryAddress().getAddressStr());
        this.addressZipCodeText.setText(cPackAndShipShipment.currentShipment.deliveryAddress().getZipcodeStr());
        this.addressCityText.setText(cPackAndShipShipment.currentShipment.deliveryAddress().getCityStr());
        this.addressCountryText.setText(cPackAndShipShipment.currentShipment.deliveryAddress().getCountryStr());

    }

    private void mSetShippingInfo() {

        if (cPackAndShipShipment.currentShipment  == null) {
            this.shippingAgentText.setText(R.string.novalueyet);
            this.shippingServiceText.setText(R.string.novalueyet);
            return;
        }

        this.shippingAgentText.setText(cPackAndShipShipment.currentShipment.getShippingAgentCodeDescriptionStr());
        this.shippingServiceText.setText(cPackAndShipShipment.currentShipment.getShippingAgentServiceCodeDescriptionStr());
    }

    private void mSetButtons() {

        this.imageViewShippingDone.setVisibility(View.GONE);

       if (cPackAndShipShipment.currentShipment == null) {
           return;
       }
        this.imageViewShippingDone.setVisibility(View.VISIBLE);

    }

    private void mSetDocumentDoneListener() {

        this.imageViewShippingDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pHandleDocumentDone();
            }
        });
    }

    private void mDocumentDone() {

        cResult hulpResult;

        hulpResult = cPackAndShipShipment.currentShipment.pHandledViaWebserviceRst();
        if  (!hulpResult.resultBln) {
            this.mShowShipmentNotSent(hulpResult.messagesStr());
            return;
        }

        hulpResult = cPackAndShipOrder.currentPackAndShipOrder.pHandledViaWebserviceRst();
        if  (!hulpResult.resultBln) {
            this.mShowShipmentNotSent(hulpResult.messagesStr());
            return;
        }

        //We are done, so show we are done
        this.mShowSent();

        //Reset currents
        this.mResetCurrents();

        //Rebuild activity
        this.mFieldsInitialize();

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
                cUserInterface.pShowSnackbarMessage(this.imageViewShippingDone,cAppExtension.activity.getString(R.string.message_shipment_send), R.raw.headsupsound, true);
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

    private void mHandleDocumentScan(String pvBarcodeWithoutPrefixStr){

        cResult hulpRst;

        //We scanned a new barcode and we already have a pack and ship order
        if (this.scannedDocumentStr != null && !this.scannedDocumentStr.isEmpty() && !this.scannedDocumentStr.equalsIgnoreCase(pvBarcodeWithoutPrefixStr)) {

            //Hide getting data
            cUserInterface.pHideGettingData();

            //Close current order
            this.pHandleDocumentDone();


            //Handle new scan
            this.pHandleScan(cBarcodeScan.pFakeScan(pvBarcodeWithoutPrefixStr));
            return;
        }

        hulpRst = cPackAndShipOrder.pCreatePackAndShipOrderPSMViaWebserviceRst(pvBarcodeWithoutPrefixStr);
        if (!hulpRst.resultBln) {
            cUserInterface.pDoExplodingScreen(hulpRst.messagesStr(),pvBarcodeWithoutPrefixStr,true,true);
            return;
        }

        hulpRst  = cPackAndShipOrder.currentPackAndShipOrder.pLockViaWebserviceRst();
        //Everything was fine, so we are done
        if (!hulpRst.resultBln) {
            cUserInterface.pDoExplodingScreen(hulpRst.messagesStr(),pvBarcodeWithoutPrefixStr,true,true);
            return;
        }

        hulpRst  = cPackAndShipOrder.currentPackAndShipOrder.pGetOrderDetailsRst();
        //Everything was fine, so we are done
        if (!hulpRst.resultBln) {
            cUserInterface.pDoExplodingScreen(hulpRst.messagesStr(),pvBarcodeWithoutPrefixStr,true,true);
            return;
        }

        hulpRst =   cPackAndShipOrder.currentPackAndShipOrder.pGetDocumentAndDetailsRst(pvBarcodeWithoutPrefixStr);
        if (!hulpRst.resultBln) {
            cUserInterface.pDoExplodingScreen(hulpRst.messagesStr(),"",true,true);
            return;
        }

        hulpRst = cPackAndShipOrder.currentPackAndShipOrder.pAddShipmentViaWebserviceRst();
        if (!hulpRst.resultBln) {
            cUserInterface.pDoExplodingScreen(hulpRst.messagesStr(), "", true, true);
            return;
        }

        this.scannedDocumentStr = pvBarcodeWithoutPrefixStr;
        cUserInterface.pHideGettingData();
        this.mFieldsInitialize();

    }

    private void mSetShippingUnits() {

        if (cPackAndShipShipment.currentShipment == null || cPackAndShipShipment.currentShipment.shippingAgent() == null || cPackAndShipShipment.currentShipment.shippingAgentService() == null ||
                cPackAndShipShipment.currentShipment.shippingAgentService().shippingUnitsObl() == null || cPackAndShipShipment.currentShipment.shippingAgentService().shippingUnitsObl().size() == 0) {
            return;
        }

        this.mFillShippingUnitRecycler(cPackAndShipShipment.currentShipment.shippingAgentService().shippingUnitsObl());
    }

    private void mSetBarcodesUnits() {

        if (cPackAndShipBarcode.allPackAndShipOrderBarcodesObl == null || cPackAndShipBarcode.allPackAndShipOrderBarcodesObl.size() == 0) {
            return;
        }

        this.mFillBarcodeRecycler(cPackAndShipBarcode.allPackAndShipOrderBarcodesObl);
    }

    private void mFillBarcodeRecycler(List<cPackAndShipBarcode> pvDataObl) {

        if (pvDataObl.size() == 0) {
           return;
        }

        this.recyclerBarcodes.setHasFixedSize(false);
        this.recyclerBarcodes.setAdapter(this.getPackAndShipOrderBarcodeAdapter());
        this.recyclerBarcodes.setLayoutManager(new LinearLayoutManager(cAppExtension.context));
    }

    private void mFillShippingUnitRecycler(List<cShippingAgentServiceShippingUnit> pvDataObl) {

        if (pvDataObl.size() == 0) {
            this.actionTextView.setVisibility(View.VISIBLE);
            this.actionTextView.setText(cAppExtension.activity.getString(R.string.select_shippingunit));
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

    private void mShowShippingUnitFragment() {
        final ShippingUnitFragment shippingUnitFragment = new ShippingUnitFragment();
        shippingUnitFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.SHIPPINGFRAGMENT_LIST_TAG);
    }
}
