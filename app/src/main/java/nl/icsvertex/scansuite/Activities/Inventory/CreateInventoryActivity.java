package nl.icsvertex.scansuite.Activities.Inventory;


import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.ArrayList;
import java.util.List;

import ICS.Interfaces.iICSDefaultActivity;
import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cRegex;
import ICS.Utils.cResult;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Basics.BarcodeLayouts.cBarcodeLayout;
import SSU_WHS.Basics.Settings.cSetting;
import SSU_WHS.Basics.StockOwner.cStockOwner;
import SSU_WHS.Basics.Users.cUser;
import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.Inventory.InventoryOrders.cInventoryorder;
import nl.icsvertex.scansuite.R;


public class CreateInventoryActivity  extends AppCompatActivity implements iICSDefaultActivity {

    //Region Private Properties

    private  ConstraintLayout createInventoryContainer;
    private ImageView toolbarImage;
    private TextView toolbarTitle;
    private Spinner spinner;

    private  EditText editTextDocument;
    private SwitchMaterial checkBarcodesSwitch;
    private  Button createButton;
    private  Button cancelButton;
    //End Region private Properties

    //Region Constructor
    public CreateInventoryActivity() {
    }
    //End Region Constructor

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_inventory_order);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        cBarcodeScan.pUnregisterBarcodeReceiver(this.getClass().getSimpleName());
    }

    @Override
    public void onResume() {
        super.onResume();
        this.mActivityInitialize();
        cBarcodeScan.pRegisterBarcodeReceiver(this.getClass().getSimpleName());
        cUserInterface.pEnableScanner();
    }

    @Override
    public void onBackPressed() {
        this.mStartOrderSelectActivity();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem pvMenuItem) {

        if (pvMenuItem.getItemId() == android.R.id.home) {
            this.mStartOrderSelectActivity();
            return true;
        }

        return super.onOptionsItemSelected(pvMenuItem);
    }

    @Override
    public void mActivityInitialize() {

        this.mSetAppExtensions();

        this.mFindViews();

        this.mSetToolbar(getResources().getString(R.string.create_inventory));

        this.mFieldsInitialize();

        this.mSetListeners();

        this.mInitScreen();

        cBarcodeScan.pRegisterBarcodeReceiver(this.getClass().getSimpleName());
    }


    @Override
    public void mSetAppExtensions() {
        cAppExtension.context = this;
        cAppExtension.fragmentActivity = this;
        cAppExtension.activity = this;
        cAppExtension.fragmentManager = getSupportFragmentManager();
    }

    @Override
    public void mFindViews() {
            this.toolbarImage = findViewById(R.id.toolbarImage);
            this.toolbarTitle = findViewById(R.id.toolbarTitle);
            this.createInventoryContainer = findViewById(R.id.createInventoryContainer);
            this.spinner = findViewById(R.id.stockownerSpinner);
            this.editTextDocument = findViewById(R.id.editTextDocument);
            this.checkBarcodesSwitch = findViewById(R.id.checkBarcodesSwitch);
            this.createButton = findViewById(R.id.createButton);
            this.cancelButton = findViewById(R.id.cancelButton);
    }

    @Override
    public void mSetToolbar(String pvScreenTitleStr) {
        this.toolbarImage.setImageResource(R.drawable.ic_menu_inventory);
        this.toolbarTitle.setText(pvScreenTitleStr);
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
        InputFilter[] filterArray = new InputFilter[1];
        filterArray[0] = new InputFilter.LengthFilter(50);

        this.mShowStockOwnerSpinner();
        this.editTextDocument.setFilters(filterArray);
        cUserInterface.pShowKeyboard(this.editTextDocument);

        if (!cSetting.INV_BARCODE_CHECK()) {
            this.checkBarcodesSwitch.setVisibility(View.GONE);
        }
        else{
            this.checkBarcodesSwitch.setChecked(true);
        }
    }

    @Override
    public void mSetListeners() {
        this.mSetCreateListener();
        this.mSetCancelListener();
        this.mSetEditorActionListener();
        this.mSetStockOwnerSpinnerListener();
    }

    @Override
    public void mInitScreen() {

    }


    private void mSetCancelListener() {
        this.cancelButton.setOnClickListener(pvView -> mStartOrderSelectActivity());
    }

    private void mSetCreateListener() {
        this.createButton.setOnClickListener(pvView -> {
            cUserInterface.pShowGettingData();
            Runnable runnable = this::mHandleCreateOrder;
            Thread createOrder = new Thread(runnable);
            createOrder.start();
        });
    }

    public void pHandleScan(cBarcodeScan pvBarcodeScan) {

        String barcodeWithoutPrefixStr;

        //No prefix
        if (!cRegex.pHasPrefix(pvBarcodeScan.getBarcodeOriginalStr())) {
            this.editTextDocument.setText(pvBarcodeScan.getBarcodeOriginalStr());
            this.createButton.performClick();
            return;
        }

        boolean foundBln = false;

        if (cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(), cBarcodeLayout.barcodeLayoutEnu.DOCUMENT)) {
            foundBln = true;
        }

        //has prefix, is DOCUMENT
        if (foundBln) {
            barcodeWithoutPrefixStr = cRegex.pStripRegexPrefixStr(pvBarcodeScan.getBarcodeOriginalStr());
            this.editTextDocument.setText(barcodeWithoutPrefixStr);
            this.createButton.performClick();
        }
        else {
            //has prefix, isn't DOCUMENT
            cUserInterface.pDoNope(createInventoryContainer, true, true);
        }
    }

    private void mSetEditorActionListener() {
        this.editTextDocument.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_DONE || i == EditorInfo.IME_ACTION_GO ) {
                cUserInterface.pHideKeyboard();
                createButton.callOnClick();
            }
            return true;
        });
    }

    private  void mHandleCreateOrder(){

        cResult hulpResult;

        //Try to create the order
        if (!this.mTryToCreateOrderBln()) {
            return;
        }

        //Try to lock the order
        if (!this.mTryToLockOrderBln()) {
            return;
        }

        InventoryorderSelectActivity.startedViaMenuBln = false;

        //Delete the detail, so we can get them from the webservice
        cInventoryorder.currentInventoryOrder.pDeleteDetails();

        hulpResult = cInventoryorder.currentInventoryOrder.pGetOrderDetailsRst();
        if (!hulpResult.resultBln) {
            cUserInterface.pDoExplodingScreen((hulpResult.messagesStr()),"", false, false);
            return;
        }

        // If everything went well, then start Lines Activity
        cAppExtension.activity.runOnUiThread(this::mShowInventoryorderBinsActivity);

    }

    private  boolean mTryToCreateOrderBln(){

        Boolean resultBln =  cInventoryorder.pCreateInventoryOrderViaWebserviceBln((this.editTextDocument.getText().toString()), this.checkBarcodesSwitch.isChecked());
        if (!resultBln) {
            cUserInterface.pDoExplodingScreen(cAppExtension.activity.getString(R.string.message_couldnt_create_order),"", true, true);
            return  false;
        }

        return true;

    }

    private  boolean mTryToLockOrderBln(){

        cResult hulpResult;
        hulpResult = cInventoryorder.currentInventoryOrder.pLockViaWebserviceRst(cWarehouseorder.StepCodeEnu.Inventory, cWarehouseorder.WorkflowInventoryStepEnu.Inventory);
        //Everything was fine, so we are done
        if (hulpResult.resultBln) {
            return true;
        }

        //Something went wrong, but no further actions are needed, so ony show reason of failure
        if (hulpResult.activityActionEnu == cWarehouseorder.ActivityActionEnu.Unknown ) {
            cUserInterface.pDoExplodingScreen((hulpResult.messagesStr()),"", false, false);
            return  false;
        }

        return true;

    }

    private  void mShowInventoryorderBinsActivity() {
        cUserInterface.pCheckAndCloseOpenDialogs();
        Intent intent = new Intent(cAppExtension.context, InventoryorderBinsActivity.class);
        startActivity(intent);
       finish();
    }

    private void mStartOrderSelectActivity() {
        Intent intent = new Intent(cAppExtension.context, InventoryorderSelectActivity.class);
        InventoryorderSelectActivity.startedViaMenuBln = false;
        startActivity(intent);
        finish();
    }
    private void mShowStockOwnerSpinner() {

        if (cStockOwner.allStockOwnerObl  == null || cStockOwner.allStockOwnerObl.size() == 0) {
            this.spinner.setVisibility(View.GONE);
            return;
        }

        this.spinner.setVisibility(View.VISIBLE);
        this.spinner.setVisibility(View.VISIBLE);

        this.mFillStockOwnerSpinner();
    }
    private void mFillStockOwnerSpinner() {

        if (cStockOwner.allStockOwnerObl == null ||  cStockOwner.allStockOwnerObl.size() <= 0 ) {
            return;
        }

        List<String> stockOwnerObl = new ArrayList<>();

        if (cUser.currentUser.currentBranch.stockOwnerObl().size() >= 1) {
            for (cStockOwner stockOwner :cUser.currentUser.currentBranch.stockOwnerObl() ) {
                stockOwnerObl.add(stockOwner.getDescriptionStr());
            }
        }
        else
        {
            for (cStockOwner stockOwner :cStockOwner.allStockOwnerObl ) {
                stockOwnerObl.add(stockOwner.getDescriptionStr());
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(cAppExtension.context,
                android.R.layout.simple_spinner_dropdown_item,
                stockOwnerObl);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        this.spinner.setAdapter(adapter);
        if (cUser.currentUser.currentStockOwner != null)
        { this.spinner.setSelection(adapter.getPosition(cUser.currentUser.currentStockOwner.getDescriptionStr()));}
    }
    private void mSetStockOwnerSpinnerListener() {

        this.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                cUser.currentUser.currentStockOwner = cStockOwner.pGetStockOwnerByDescriptionStr(spinner.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
    }

}
