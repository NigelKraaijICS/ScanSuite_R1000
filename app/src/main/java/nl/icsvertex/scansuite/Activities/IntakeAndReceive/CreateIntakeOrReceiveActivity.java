package nl.icsvertex.scansuite.Activities.IntakeAndReceive;


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
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;

import com.google.firebase.crashlytics.FirebaseCrashlytics;

import java.util.ArrayList;
import java.util.List;

import ICS.Interfaces.iICSDefaultActivity;
import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cRegex;
import ICS.Utils.cResult;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Basics.BarcodeLayouts.cBarcodeLayout;
import SSU_WHS.Basics.BranchBin.cBranchBin;
import SSU_WHS.Basics.Settings.cSetting;
import SSU_WHS.Basics.StockOwner.cStockOwner;
import SSU_WHS.Basics.Users.cUser;
import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.General.cPublicDefinitions;
import SSU_WHS.Intake.Intakeorders.cIntakeorder;
import nl.icsvertex.scansuite.Activities.Intake.IntakeorderMASLinesActivity;
import nl.icsvertex.scansuite.Activities.Intake.IntakeorderMATLinesActivity;
import nl.icsvertex.scansuite.Activities.Receive.ReceiveLinesActivity;
import nl.icsvertex.scansuite.R;


public class CreateIntakeOrReceiveActivity extends AppCompatActivity implements iICSDefaultActivity {

    //Region Public Properties

    //End Region Public Properties

    //Region Private Properties

    private ImageView toolbarImage;
    private TextView toolbarTitle;
    private TextView toolbarSubTitle;
    private Spinner stockownerSpinner;
    private Spinner workflowSpinner;
    private EditText editTextDocument;
    private EditText editTextPackingslip;
    private EditText editTextBin;
    private SwitchCompat switchCheckBarcodes;
    private  Button createReceiveButton;
    private  Button cancelButton;
    //End Region private Properties

    //Region Constructor
    public CreateIntakeOrReceiveActivity() {
    }
    //End Region Constructor


    //Region Default Methods

    @Override
    protected void onCreate(Bundle pvSavedInstanceState) {
        super.onCreate(pvSavedInstanceState);
        setContentView(R.layout.activity_create_receive_or_intake_order);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (cAppExtension.activity instanceof CreateIntakeOrReceiveActivity) {
            cBarcodeScan.pUnregisterBarcodeReceiver(this.getClass().getSimpleName());
        }

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
    }

    @Override
    protected void onStop() {
        super.onStop();
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

    //End Region Default Methods

    //Region iICSDefaultActivity defaults

    @Override
    public void mActivityInitialize() {

        this.mSetAppExtensions();

        this.mFindViews();

        this.mSetToolbar(getResources().getString(R.string.create_receive_or_intake));

        this.mFieldsInitialize();

        this.mSetListeners();

        this.mInitScreen();

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
        this.stockownerSpinner = findViewById(R.id.stockownerSpinner);
        this.workflowSpinner = findViewById(R.id.workflowSpinner);
        this.editTextDocument = findViewById(R.id.editTextDocument);
        this.editTextPackingslip = findViewById(R.id.editTextPackingslip);
        this.editTextBin = findViewById(R.id.editTextBin);
        this.switchCheckBarcodes = findViewById(R.id.checkBarcodesSwitch);
        this.createReceiveButton = findViewById(R.id.createButton);
        this.cancelButton = findViewById(R.id.cancelButton);
    }

    @Override
    public void mSetToolbar(String pvScreenTitleStr) {
        this.toolbarImage.setImageResource(R.drawable.ic_menu_intake);
        this.toolbarTitle.setText(pvScreenTitleStr);
        this.toolbarTitle.setSelected(true);
        this.toolbarSubTitle.setText(cUser.currentUser.currentBranch.getBranchNameStr());

        ViewCompat.setTransitionName(toolbarImage, cPublicDefinitions.VIEW_NAME_HEADER_IMAGE);
        ViewCompat.setTransitionName(toolbarTitle, cPublicDefinitions.VIEW_NAME_HEADER_TEXT);
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
        this.editTextDocument.setFilters(filterArray);

        this.mShowStockOwnerSpinner();
        this.mFillWorkflowSpinner();
        if (!cUser.currentUser.currentBranch.isBinMandatoryBln()) {
            this.editTextBin.setVisibility(View.GONE);
        }
        else {
            this.mSetBin();
        }

        if (!cUser.currentUser.currentBranch.getReceiveDefaultBinStr().isEmpty()) {
            this.editTextBin.setText(cUser.currentUser.currentBranch.getReceiveDefaultBinStr());
        }

        if (!cSetting.RECEIVE_BARCODE_CHECK()) {
            this.switchCheckBarcodes.setVisibility(View.GONE);
        }
        else{
            this.switchCheckBarcodes.setChecked(true);
        }
    }

    @Override
    public void mSetListeners() {
        this.mSetCreateListener();
        this.mSetCancelListener();
        this.mSetEditorActionListener();
        this.mSetWorkflowSpinnerListener();
        this.mSetStockOwnerSpinnerListener();
    }

    @Override
    public void mInitScreen() {
        cIntakeorder.newWorkflowStr = "";
    }

    //End Region iICSDefaultActivity defaults

    //Region Public Methods

    public void pHandleScan(cBarcodeScan pvBarcodeScan, boolean pvIsDocumentBln, boolean pvIsPackingSlipBln, boolean pvBinBln) {

        String barcodeWithoutPrefixStr = cRegex.pStripRegexPrefixStr(pvBarcodeScan.getBarcodeOriginalStr());

        boolean documentBln = false;
        boolean binBln = false;

        if (pvIsDocumentBln) {
            this.editTextDocument.setText(barcodeWithoutPrefixStr);
            this.editTextPackingslip.requestFocus();
            return;
        }

        if (pvIsPackingSlipBln) {
            this.editTextPackingslip.setText(barcodeWithoutPrefixStr);
            this.editTextBin.requestFocus();
            return;
        }

        if (pvBinBln) {
            this.editTextBin.setText(barcodeWithoutPrefixStr);
            this.editTextBin.requestFocus();
            return;
        }

        cUserInterface.pCheckAndCloseOpenDialogs();

        if (cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(), cBarcodeLayout.barcodeLayoutEnu.BIN)) {
            binBln = true;
        }

        if (cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(), cBarcodeLayout.barcodeLayoutEnu.DOCUMENT)) {
            documentBln = true;
        }

        //has prefix, is DOCUMENT
        if (documentBln   ||this.editTextDocument.getText().toString().isEmpty()) {
            this.editTextDocument.setText(barcodeWithoutPrefixStr);
            this.editTextPackingslip.requestFocus();
            return;
        }

        if (binBln) {
            barcodeWithoutPrefixStr = cRegex.pStripRegexPrefixStr(pvBarcodeScan.getBarcodeOriginalStr());

            cBranchBin branchBin = cUser.currentUser.currentBranch.pGetBinByCode(barcodeWithoutPrefixStr);

            if (branchBin == null) {
                cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.message_unknown_bin), null);
                return;
            }

            if (!branchBin.getBinCodeStr().equalsIgnoreCase(cUser.currentUser.currentBranch.getReceiveDefaultBinStr())) {
                if (!branchBin.isUseForReturnSalesBln()){
                    cUserInterface.pDoExplodingScreen(cAppExtension.context.getString(R.string.message_bin_not_allowed_for_receive),"",true,true);
                    return;
                }
            }

            this.editTextBin.setText(barcodeWithoutPrefixStr);
            return;
        }

        this.editTextPackingslip.setText(barcodeWithoutPrefixStr);
        this.editTextPackingslip.requestFocus();
    }

    //End Region Public Methods

    //Region Private Method

    private  void mCreateOrder(final String pvDocumentStr, final String pvPackingSlipStr, final String pvBinCodeStr, final boolean pvCheckBarcodesBln){


        // Show that we are getting data
        cUserInterface.pShowGettingData();

        new Thread(() -> mHandleCreateOrder(pvDocumentStr,pvPackingSlipStr,pvBinCodeStr,pvCheckBarcodesBln)).start();

    }

    private void mSetCancelListener() {
        this.cancelButton.setOnClickListener(pvView -> mStartOrderSelectActivity());
    }

    private void mStartOrderSelectActivity() {
        Intent intent = new Intent(cAppExtension.context, IntakeAndReceiveSelectActivity.class);
        IntakeAndReceiveSelectActivity.startedViaMenuBln = false;
        startActivity(intent);
        finish();
    }

    private void mSetCreateListener() {
        this.createReceiveButton.setOnClickListener(pvView -> {

            if (cIntakeorder.newWorkflowStr.isEmpty()) {
                cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.message_select_workflow),null);
                return;
            }

            IntakeAndReceiveSelectActivity.startedViaMenuBln = false;
            mCreateOrder(editTextDocument.getText().toString().trim(),
                    editTextPackingslip.getText().toString().trim(),
                    editTextBin.getText().toString().trim(),
                    switchCheckBarcodes.isChecked());
        });
    }

    private void mSetEditorActionListener() {
        this.editTextDocument.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_DONE || i == EditorInfo.IME_ACTION_GO ) {

                pHandleScan(cBarcodeScan.pFakeScan(editTextDocument.getText().toString()),true,false, false);
                cUserInterface.pHideKeyboard();

            }
            return true;
        });

        this.editTextPackingslip.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_DONE || i == EditorInfo.IME_ACTION_GO ) {

                pHandleScan(cBarcodeScan.pFakeScan(editTextPackingslip.getText().toString()),false,true,false);
                cUserInterface.pHideKeyboard();

            }
            return true;
        });

        this.editTextBin.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_DONE || i == EditorInfo.IME_ACTION_GO ) {

                pHandleScan(cBarcodeScan.pFakeScan(editTextBin.getText().toString()),false,false,true);
                cUserInterface.pHideKeyboard();

            }
            return true;
        });


    }

    private void mSetBin(){

        if (!cUser.currentUser.currentBranch.getReceiveDefaultBinStr().isEmpty()) {
            this.editTextBin.setText(cUser.currentUser.currentBranch.getReceiveDefaultBinStr());
        }
        else {

            if (!cUser.currentUser.currentBranch.pGetReceiveBinsViaWebserviceBln()) {
                return;
            }

            if (cUser.currentUser.currentBranch.receiveBinsObl != null&& cUser.currentUser.currentBranch.receiveBinsObl.size() == 1) {
                this.editTextBin.setText(cUser.currentUser.currentBranch.receiveBinsObl.get(0).getBinCodeStr());
            }
        }
    }

    private  void mHandleCreateOrder(String pvDocumentstr, String pvPackingSlipStr, String pvBinCodeStr, boolean pvCheckBarcodesBln){

        cResult hulpResult;

        hulpResult = this.mTryToCreateOrderRst(pvDocumentstr,pvPackingSlipStr,pvBinCodeStr,pvCheckBarcodesBln);
        if (!hulpResult.resultBln) {
            return;
        }

        // We created an assignment, but need to open a different assignment
        if (hulpResult.activityActionEnu == cWarehouseorder.ActivityActionEnu.Next && hulpResult.resultAction != null) {

            //all Intakeorders
            if (!cIntakeorder.pGetIntakeOrdersViaWebserviceBln(true, "")) {
                cUserInterface.pDoExplodingScreen(cAppExtension.context.getString(R.string.error_get_intakeorders_failed), "", true, true );
                return;
            }

            if (cIntakeorder.allIntakeordersObl == null || cIntakeorder.allIntakeordersObl.size() == 0) {
                cUserInterface.pDoExplodingScreen(cAppExtension.context.getString(R.string.error_get_next_activity_failed), "", true, true );
                return;
            }


            for (cIntakeorder intakeorder : cIntakeorder.allIntakeordersObl) {

                if (intakeorder.getOrderNumberStr().equalsIgnoreCase(hulpResult.resultAction.nextAssignmentStr)) {
                    cIntakeorder.currentIntakeOrder = intakeorder;
                    break;
                }
            }

            if (cIntakeorder.currentIntakeOrder == null) {
                cUserInterface.pDoExplodingScreen(cAppExtension.activity.getString(R.string.message_next_activity_not_found),"",true,true);
                return;
            }

        }

        //Try to lock the order
        if (!this.mTryToLockOrderBln()) {
            cUserInterface.pDoExplodingScreen(cAppExtension.activity.getString(R.string.message_locking_order_failed),"",true,true);
            return;
        }

        //Delete the detail, so we can get them from the webservice
        cIntakeorder.currentIntakeOrder.pDeleteDetailsBln();

        hulpResult = cIntakeorder.currentIntakeOrder.pGetOrderDetailsRst();
        if (!hulpResult.resultBln) {
            this.mStepFailed(hulpResult.messagesStr());
            return;
        }

        FirebaseCrashlytics.getInstance().setCustomKey("Ordernumber", cIntakeorder.currentIntakeOrder.getOrderNumberStr());

        switch(cIntakeorder.currentIntakeOrder.getOrderTypeStr().toUpperCase()) {
            case "EOS":
                // If everything went well, then start Lines Activity
                cAppExtension.activity.runOnUiThread(this::mShowReceiveLinesActivity);
                break;
            case "MAS":
            case "MAT":
                cAppExtension.activity.runOnUiThread(this::mShowIntakeLinesActivity);
                break;
            default:
                // code block
        }



    }

    private  cResult mTryToCreateOrderRst(String pvDocumentstr, String pvPackingSlipStr, String pvBinCodeStr, boolean pvCheckBarcodesBln){

        cResult result = new cResult();

        switch(cIntakeorder.newWorkflowStr) {
            case "EOS":
            case "EOR":
                result   =  cIntakeorder.pCreateReceiveOrderViaWebserviceRst(pvDocumentstr, pvPackingSlipStr, pvBinCodeStr, pvCheckBarcodesBln);
                if (!result.resultBln) {
                    this.editTextDocument.setText("");
                    mStepFailed(result.messagesStr());
                    return  result;
                }
                break;
            case "MAS":
                result   =  cIntakeorder.pCreateIntakeOrderViaWebserviceRst(pvDocumentstr, pvBinCodeStr, pvCheckBarcodesBln);
                if (!result.resultBln) {
                    this.editTextDocument.setText("");
                    mStepFailed(result.messagesStr());
                    return  result;
                }
                break;
            default:
                // code block
        }



        return result;

    }

    private  boolean mTryToLockOrderBln(){

        cResult hulpResult;

        if (cIntakeorder.currentIntakeOrder == null) {
            return false;
        }

        hulpResult = cIntakeorder.currentIntakeOrder.pLockViaWebserviceRst();
        //Everything was fine, so we are done
        if (hulpResult.resultBln) {
            return true;
        }

        //Something went wrong, but no further actions are needed, so ony show reason of failure
        if (hulpResult.activityActionEnu == cWarehouseorder.ActivityActionEnu.Unknown ) {
            mStepFailed(hulpResult.messagesStr());
            return  false;
        }
        return (hulpResult.activityActionEnu != cWarehouseorder.ActivityActionEnu.Delete) &&
                (hulpResult.activityActionEnu != cWarehouseorder.ActivityActionEnu.NoStart);

    }

    private  void mStepFailed(String pvErrorMessageStr){

        if (cIntakeorder.currentIntakeOrder != null) {
            cUserInterface.pDoExplodingScreen(pvErrorMessageStr, cIntakeorder.currentIntakeOrder.getOrderNumberStr(), true, true );
        }
        else
        {
            cUserInterface.pDoExplodingScreen(pvErrorMessageStr,cAppExtension.activity.getString(R.string.message_couldnt_create_order) + " " +  this.editTextDocument.getText().toString(), true, true );
        }

        if (cIntakeorder.currentIntakeOrder != null) {
            cIntakeorder.currentIntakeOrder.pLockReleaseViaWebserviceBln();
        }


        cUserInterface.pCheckAndCloseOpenDialogs();
    }

    private  void mShowReceiveLinesActivity() {

        ReceiveLinesActivity.closeOrderClickedBln = false;
        ReceiveLinesActivity.packagingClickedBln = false;
        ReceiveLinesActivity.packagingHandledBln = false;

        cUserInterface.pCheckAndCloseOpenDialogs();
        Intent intent = new Intent(cAppExtension.context, ReceiveLinesActivity.class);
        startActivity(intent);
        finish();
    }

    private  void mShowIntakeLinesActivity() {

        final Intent intent;

        cUserInterface.pCheckAndCloseOpenDialogs();

        switch (cIntakeorder.currentIntakeOrder.getOrderTypeStr().toUpperCase()) {
            case  "MAT":
                intent = new Intent(cAppExtension.context, IntakeorderMATLinesActivity.class);
                break;

            case "MAS":
                intent = new Intent(cAppExtension.context, IntakeorderMASLinesActivity.class);
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + cIntakeorder.currentIntakeOrder.getOrderTypeStr().toUpperCase());
        }

        startActivity(intent);
        finish();
    }

    private void mShowStockOwnerSpinner() {

        if (cStockOwner.allStockOwnerObl  == null || cStockOwner.allStockOwnerObl.size() == 0) {
            this.stockownerSpinner.setVisibility(View.GONE);
            return;
        }

        this.stockownerSpinner.setVisibility(View.VISIBLE);
        this.stockownerSpinner.setVisibility(View.VISIBLE);

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

        this.stockownerSpinner.setAdapter(adapter);
        if (cUser.currentUser.currentStockOwner != null)
        { this.stockownerSpinner.setSelection(adapter.getPosition(cUser.currentUser.currentStockOwner.getDescriptionStr()));}
    }

    private void mSetStockOwnerSpinnerListener() {

        this.stockownerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                cUser.currentUser.currentStockOwner = cStockOwner.pGetStockOwnerByDescriptionStr(stockownerSpinner.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
    }

    private void mFillWorkflowSpinner() {

        if (cSetting.RECEIVE_NEW_WORKFLOWS() == null ||  cSetting.RECEIVE_NEW_WORKFLOWS().size() <= 0 ) {
            return;
        }

        List<String> workflowObl = new ArrayList<>();

        for (String workflowStr : cSetting.RECEIVE_NEW_WORKFLOWS()) {
            workflowObl.add(cWarehouseorder.pGetWorkflowDescriptionStr(workflowStr));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(cAppExtension.context,
                android.R.layout.simple_spinner_dropdown_item,
                workflowObl);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        this.workflowSpinner.setAdapter(adapter);
    }

    private void mSetWorkflowSpinnerListener() {

        this.workflowSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                cIntakeorder.newWorkflowStr =  cWarehouseorder.pGetWorkflowByDescriptionStr(workflowSpinner.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
    }

    //End Region Private Methods

}
