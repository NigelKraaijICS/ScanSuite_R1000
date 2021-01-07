package nl.icsvertex.scansuite.Activities.PackAndShip;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.crashlytics.FirebaseCrashlytics;

import java.util.ArrayList;
import java.util.List;

import ICS.Interfaces.iICSDefaultActivity;
import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cRegex;
import ICS.Utils.cResult;
import ICS.Utils.cText;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Basics.ShippingAgentServiceShippingUnits.cShippingAgentServiceShippingUnit;
import SSU_WHS.Basics.ShippingAgentServiceShippingUnits.cShippingAgentServiceShippingUnitAdapter;
import SSU_WHS.Basics.ShippingAgentServices.cShippingAgentService;
import SSU_WHS.Basics.ShippingAgents.cShippingAgent;
import SSU_WHS.Basics.Users.cUser;
import SSU_WHS.Basics.Workplaces.cWorkplace;
import SSU_WHS.General.cPublicDefinitions;
import SSU_WHS.Intake.IntakeorderMATLineSummary.cIntakeorderMATSummaryLine;
import SSU_WHS.Intake.Intakeorders.cIntakeorder;
import SSU_WHS.PackAndShip.PackAndShipAddress.cPackAndShipAddress;
import SSU_WHS.PackAndShip.PackAndShipBarcode.cPackAndShipBarcode;
import SSU_WHS.PackAndShip.PackAndShipLines.cPackAndShipLine;
import SSU_WHS.PackAndShip.PackAndShipOrders.cPackAndShipOrder;
import SSU_WHS.PackAndShip.PackAndShipShipment.cPackAndShipShipment;
import nl.icsvertex.scansuite.Fragments.Dialogs.NothingHereFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.ScanDocumentFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.SendingFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.WorkplaceFragment;
import nl.icsvertex.scansuite.R;

public class PackAndShipSingleActivity extends AppCompatActivity implements iICSDefaultActivity {


    //Region Public Properties

    //End Region Public Properties

    //Region Private Properties

    private ConstraintLayout packAndShipSingleConstraintLayout;

    private ImageView toolbarImage;
    private TextView toolbarTitle;
    private TextView toolbarSubTitle;

    private CardView addressContainer;
    private TextView addressNameText;
    private TextView addressAddressText;
    private TextView addressZipCodeText;
    private TextView addressCityText;
    private TextView addressCountryText;

    private Spinner shippinAgentSpinner;
    private Spinner shippinAgentServiceSpinner;

    private RecyclerView recyclerUnitsUsed;

    private ImageView imageViewShippingDone;
    private TextView actionTextView;

    private String scannedDocumentStr;
    private String scannedDocumentToHandleStr;

    private cShippingAgentServiceShippingUnitAdapter shippingAgentServiceShippingUnitAdapter;
    private cShippingAgentServiceShippingUnitAdapter getShippingAgentServiceShippingUnitAdapter(){
        if (this.shippingAgentServiceShippingUnitAdapter == null) {
            this.shippingAgentServiceShippingUnitAdapter = new cShippingAgentServiceShippingUnitAdapter();
        }
        return  this.shippingAgentServiceShippingUnitAdapter;
    }

    private cShippingAgent selectedShippingAgent;
    private cShippingAgentService selectedShippingAgentService;

    //End Region Private Properties

    //Region Default Methods


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_packandshiporder_ship_single);
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

        this.mSetToolbar(getResources().getString(R.string.screentitle_packandship_single));

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

        this.packAndShipSingleConstraintLayout = findViewById(R.id.packAndShipSingleConstraintLayout);
        this.toolbarImage = findViewById(R.id.toolbarImage);
        this.toolbarTitle = findViewById(R.id.toolbarTitle);
        this.toolbarSubTitle = findViewById(R.id.toolbarSubtext);

        this.addressContainer = findViewById(R.id.addressContainer);
        this.addressNameText = findViewById(R.id.addressNameText);
        this.addressAddressText = findViewById(R.id.addressAddressText);
        this.addressZipCodeText = findViewById(R.id.addressZipCodeText);
        this.addressCityText = findViewById(R.id.addressCityText);
        this.addressCountryText = findViewById(R.id.addressCountryText);

        this.recyclerUnitsUsed = findViewById(R.id.recyclerUnitsUsed);
        this.actionTextView = findViewById(R.id.actionTextView);

        this.shippinAgentSpinner = findViewById(R.id.shippinAgentSpinner);
        this.shippinAgentServiceSpinner = findViewById(R.id.shippinAgentServiceSpinner);

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

        String toolBarSubTitleStr = cAppExtension.activity.getString(R.string.novalueyet);

        if (cPackAndShipOrder.currentPackAndShipOrder != null) {
            toolBarSubTitleStr = cPackAndShipOrder.currentPackAndShipOrder.getOrderNumberStr();
        }

        if (this.scannedDocumentStr != null && !this.scannedDocumentStr.isEmpty()) {
            toolBarSubTitleStr += " " + this.scannedDocumentStr;
        }

        this.toolbarSubTitle.setText(toolBarSubTitleStr);
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
                mSetToolbar(getResources().getString(R.string.screentitle_packandship_single));
                mSetActionText();
                mSetAddress();
                mSetShippingInfo();
                mSetShippingUnits();
                mSetButtons();
                mCheckEmptyScreen();
            }
        });
    }

    @Override
    public void mSetListeners() {
        this.mSetDocumentDoneListener();
        this.mSetShippingAgentSpinnerListener();
        this.mSetShippingAgentServiceSpinnerListener();
    }

    @Override
    public void mInitScreen() {
        cBarcodeScan.pRegisterBarcodeReceiver(this.getClass().getSimpleName());

        if (cWorkplace.currentWorkplace == null) {
            if (cWorkplace.allWorkplacesObl.size() > 1) {
                //Show the workplaceStr fragment
                this.mShowWorkplaceFragment();
            }
            else {
                //Pop-up is not needed, we just select the only workplace there is
                cWorkplace.currentWorkplace = cWorkplace.allWorkplacesObl.get(0);
                this.pWorkplaceSelected(false);
            }
        }
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

        if (!this.mCheckShipmentOkeBln()) {
            cUserInterface.pDoExplodingScreen(cAppExtension.activity.getString(R.string.message_shipment_incomplete),"",true,true);
            return;
        }

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

    public  void pWorkplaceSelected(boolean pvShowBln){

        List<Fragment> fragments = cAppExtension.fragmentManager.getFragments();
        for (Fragment fragment : fragments) {
            if (fragment instanceof WorkplaceFragment) {
                FragmentTransaction fragmentTransaction = cAppExtension.fragmentManager.beginTransaction();
                fragmentTransaction.remove(fragment);
                fragmentTransaction.commit();
            }
        }

        //Register barcodeStr receiver, because the workplaceStr fragment has been shown
        cBarcodeScan.pRegisterBarcodeReceiver(this.getClass().getSimpleName());

        if (pvShowBln) {
            cUserInterface.pShowSnackbarMessage(this.actionTextView,cAppExtension.activity.getString(R.string.message_workplace_selected) + ' ' + cWorkplace.currentWorkplace.getWorkplaceStr() ,R.raw.headsupsound,false);

        }

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

        for (cShippingAgentServiceShippingUnit shippingAgentServiceShippingUnit : cShippingAgentServiceShippingUnit.allShippingAgentServiceShippingUnitsObl) {
            shippingAgentServiceShippingUnit.ShippingUnitQuantityUsedInt = 0;
        }

    }

    private void mSetActionText() {

        this.actionTextView.setVisibility(View.VISIBLE);

        if (cPackAndShipShipment.currentShipment  == null) {
            this.actionTextView.setText(getString(R.string.scan_document));
            return;
        }

        this.actionTextView.setText(getString(R.string.scan_next_document_to_close_current));

    }

    private void mSetAddress() {

        if (cPackAndShipOrder.currentPackAndShipOrder == null || cPackAndShipShipment.currentShipment == null) {
             this.addressContainer.setVisibility(View.INVISIBLE);
            return;
        }

        this.addressContainer.setVisibility(View.VISIBLE);
        this.addressNameText.setText(cPackAndShipShipment.currentShipment.deliveryAddress().getAddressNameStr());
        this.addressAddressText.setText(cPackAndShipShipment.currentShipment.deliveryAddress().getAddressStr());
        this.addressZipCodeText.setText(cPackAndShipShipment.currentShipment .deliveryAddress().getZipcodeStr());
        this.addressCityText.setText(cPackAndShipShipment.currentShipment.deliveryAddress().getCityStr());
        this.addressCountryText.setText(cPackAndShipShipment.currentShipment.deliveryAddress().getCountryStr());

    }

    private void mSetShippingInfo() {

        if (cPackAndShipShipment.currentShipment  == null) {
            this.shippinAgentSpinner.setVisibility(View.GONE);
            this.shippinAgentServiceSpinner.setVisibility(View.GONE);
            return;
        }

        this.shippinAgentSpinner.setVisibility(View.VISIBLE);
        this.shippinAgentServiceSpinner.setVisibility(View.VISIBLE);

          this.mFillShippingAgentSpinner();
    }

    private void mSetButtons() {


        this.imageViewShippingDone.setVisibility(View.INVISIBLE);

       if (cPackAndShipShipment.currentShipment == null) {
           return;
       }
       if (cPackAndShipShipment.currentShipment.isShippingBln() && !this.isReadyToSendBln()) {
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

    private void mSetShippingAgentSpinnerListener() {

        this.shippinAgentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedShippingAgent = cShippingAgent.pGetShippingAgentByDescriptionStr(shippinAgentSpinner.getSelectedItem().toString());
                if (selectedShippingAgent != null) {
                    cPackAndShipShipment.currentShipment.shippingAgentCodeStr = selectedShippingAgent.getShippingAgentStr();
                    mShippingAgentChanged();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
    }

    private void mSetShippingAgentServiceSpinnerListener() {

        this.shippinAgentServiceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedShippingAgentService = selectedShippingAgent.pGetShippingAgentServiceByDescriptionStr(shippinAgentServiceSpinner.getSelectedItem().toString());
                cPackAndShipShipment.currentShipment.shippingAgentServiceCodeStr =selectedShippingAgentService.getServiceStr();
                mSetButtons();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
    }

    private void mDocumentDone() {

        cResult hulpResult;

        if (cPackAndShipShipment.currentShipment.isShippingBln()) {

            cPackAndShipOrder.currentPackAndShipOrder.pUpdateWorkplaceViaWebserviceBln();

            hulpResult = cPackAndShipShipment.currentShipment.pShipViaWebserviceRst(this.selectedShippingAgentService.shippingUnitsObl());
        }
        else
        {
            hulpResult = cPackAndShipShipment.currentShipment.pHandledViaWebserviceRst();
        }
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

        if (this.scannedDocumentToHandleStr != null && !this.scannedDocumentToHandleStr .isEmpty()) {
            //Handle new scan
            this.pHandleScan(cBarcodeScan.pFakeScan(this.scannedDocumentToHandleStr));
            this.scannedDocumentToHandleStr = "";
        }

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

            this.scannedDocumentToHandleStr = pvBarcodeWithoutPrefixStr;

            //Hide getting data
            cUserInterface.pHideGettingData();

            //Close current order
            this.pHandleDocumentDone();

            return;
        }

        hulpRst = cPackAndShipOrder.pCreatePackAndShipOrderPS1ViaWebserviceRst(pvBarcodeWithoutPrefixStr);
        if (!hulpRst.resultBln) {
            cUserInterface.pDoExplodingScreen(hulpRst.messagesStr(),pvBarcodeWithoutPrefixStr,true,true);
            return;
        }

        FirebaseCrashlytics.getInstance().setCustomKey("Ordernumber", cPackAndShipOrder.currentPackAndShipOrder.getOrderNumberStr());

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

        hulpRst =   cPackAndShipOrder.currentPackAndShipOrder.pGetFirstDocumentAndDetailsRst(pvBarcodeWithoutPrefixStr);
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

        if (cPackAndShipShipment.currentShipment == null || !cPackAndShipShipment.currentShipment.isShippingBln()) {
            this.recyclerUnitsUsed.setVisibility(View.GONE);
            return;
        }

        if (cPackAndShipShipment.currentShipment == null || cPackAndShipShipment.currentShipment.shippingAgent() == null || cPackAndShipShipment.currentShipment.shippingAgentService() == null ||
            cPackAndShipShipment.currentShipment.shippingAgentService().shippingUnitsObl() == null || cPackAndShipShipment.currentShipment.shippingAgentService().shippingUnitsObl().size() == 0) {
            return;
        }

        this.mFillShippingUnitRecycler(cPackAndShipShipment.currentShipment.shippingAgentService().shippingUnitsObl());
    }

    private void mFillShippingUnitRecycler(List<cShippingAgentServiceShippingUnit> pvDataObl) {

        if (pvDataObl.size() == 0) {
            this.actionTextView.setVisibility(View.VISIBLE);
            this.actionTextView.setText(cAppExtension.activity.getString(R.string.select_shippingunit));
            return;
        }

        if (cPackAndShipShipment.currentShipment != null && cPackAndShipShipment.currentShipment.shippingAgent() != null && cPackAndShipShipment.currentShipment.shippingAgentService() != null) {
            if (cPackAndShipShipment.currentShipment.shippingAgent() != null && cPackAndShipShipment.currentShipment.shippingAgentService() != null) {
                if (cPackAndShipShipment.currentShipment.shippingAgentService().shippingUnitsObl().size() == 1) {
                    for (cShippingAgentServiceShippingUnit shippingAgentServiceShippingUnit :cPackAndShipShipment.currentShipment.shippingAgentService().shippingUnitsObl() ) {
                        shippingAgentServiceShippingUnit.ShippingUnitQuantityUsedInt = 1;
                    }
                }
            }
        }

        this.recyclerUnitsUsed.setVisibility(View.VISIBLE);
        this.getShippingAgentServiceShippingUnitAdapter().pFillData(pvDataObl);
        this.recyclerUnitsUsed.setHasFixedSize(false);
        this.recyclerUnitsUsed.setAdapter(this.getShippingAgentServiceShippingUnitAdapter());
        this.recyclerUnitsUsed.setLayoutManager(new LinearLayoutManager(cAppExtension.context));



    }

    private boolean mCheckShipmentOkeBln() {

        if (cPackAndShipShipment.currentShipment == null) {
            return false;
        }

        if (!cPackAndShipShipment.currentShipment.isShippingBln()) {
            return  true;
        }

       return this.isReadyToSendBln();


    }

    private void mFillShippingAgentSpinner() {

        if (cPackAndShipShipment.currentShipment == null || cShippingAgent.allShippingAgentsObl == null ||  cShippingAgent.allShippingAgentsObl.size() == 0 ) {
            return;
        }

        List<String> shippingAgentObl = new ArrayList<>();

        for (cShippingAgent shippingAgent :cShippingAgent.allShippingAgentsObl ) {
            shippingAgentObl.add(shippingAgent.getDescriptionStr());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(cAppExtension.context,
                                                          android.R.layout.simple_spinner_dropdown_item,
                                                           shippingAgentObl);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.shippinAgentSpinner.setAdapter(adapter);
        this.shippinAgentSpinner.setSelection(adapter.getPosition(cPackAndShipShipment.currentShipment.shippingAgent().getDescriptionStr()));

    }

    private void mShippingAgentChanged() {

        if (this.selectedShippingAgent == null || this.selectedShippingAgent.shippingAgentServicesObl() == null ||  this.selectedShippingAgent.shippingAgentServicesObl() .size() == 0 ) {
            return;
        }

        List<String> shippingAgentServiceObl = new ArrayList<>();

        for (cShippingAgentService shippingAgentService :this.selectedShippingAgent.shippingAgentServicesObl() ) {
            shippingAgentServiceObl.add(shippingAgentService.getDescriptionStr());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(cAppExtension.context,
                android.R.layout.simple_spinner_dropdown_item,
                shippingAgentServiceObl);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.shippinAgentServiceSpinner.setAdapter(adapter);
        this.shippinAgentServiceSpinner.setSelection(adapter.getPosition(cPackAndShipShipment.currentShipment.shippingAgentService().getDescriptionStr()));

        cShippingAgentService shippingAgentService = this.selectedShippingAgent.pGetShippingAgentServiceByDescriptionStr(cPackAndShipShipment.currentShipment.shippingAgentService().getDescriptionStr());
        this.mFillShippingUnitRecycler(shippingAgentService.shippingUnitsObl());
    }

    public  boolean isReadyToSendBln() {

        if (this.selectedShippingAgent != null && this.selectedShippingAgentService != null) {
            for (cShippingAgentServiceShippingUnit shippingAgentServiceShippingUnit :this.selectedShippingAgentService.shippingUnitsObl() ) {
                if (shippingAgentServiceShippingUnit.getShippingUnitQuantityUsedInt() > 0 ) {
                    return  true;
                }
            }
        }
        return  false;

    }

    private  void mShowWorkplaceFragment() {
        WorkplaceFragment workplaceFragment = new WorkplaceFragment();
        workplaceFragment.setCancelable(false);
        workplaceFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.WORKPLACEFRAGMENT_TAG);
    }

    private void mCheckEmptyScreen() {

        cAppExtension.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                cUserInterface.pHideGettingData();

                if (cPackAndShipShipment.currentShipment == null) {
                    FragmentTransaction fragmentTransaction = cAppExtension.fragmentManager.beginTransaction();
                    ScanDocumentFragment fragment = new ScanDocumentFragment();
                    fragmentTransaction.replace(R.id.packAndShipSingleConstraintLayout, fragment);
                    fragmentTransaction.commit();
                    actionTextView.setVisibility(View.GONE);
                    return;
                }

                List<Fragment> fragments = cAppExtension.fragmentManager.getFragments();
                for (Fragment fragment : fragments) {
                    if (fragment instanceof ScanDocumentFragment) {
                        FragmentTransaction fragmentTransaction = cAppExtension.fragmentManager.beginTransaction();
                        fragmentTransaction.remove(fragment);
                        fragmentTransaction.commit();
                        actionTextView.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

    }

}
