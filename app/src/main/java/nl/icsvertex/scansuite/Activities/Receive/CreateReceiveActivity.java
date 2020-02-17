package nl.icsvertex.scansuite.Activities.Receive;


import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.ViewCompat;

import ICS.Interfaces.iICSDefaultActivity;
import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cProductFlavor;
import ICS.Utils.cRegex;
import ICS.Utils.cResult;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Basics.BarcodeLayouts.cBarcodeLayout;
import SSU_WHS.Basics.BranchBin.cBranchBin;
import SSU_WHS.Basics.Settings.cSetting;
import SSU_WHS.Basics.Users.cUser;
import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.Intake.Intakeorders.cIntakeorder;
import nl.icsvertex.scansuite.Activities.IntakeAndReceive.IntakeAndReceiveSelectActivity;
import nl.icsvertex.scansuite.BuildConfig;
import nl.icsvertex.scansuite.R;


public class CreateReceiveActivity extends AppCompatActivity implements iICSDefaultActivity {

    //Region Public Properties
    public static final String VIEW_NAME_HEADER_IMAGE = "detail:header:imageStr";
    public static final String VIEW_NAME_HEADER_TEXT = "detail:header:text";
    //End Region Public Properties

    //Region Private Properties

    private static ImageView toolbarImage;
    private static TextView toolbarTitle;
    private static TextView toolbarSubTitle;
    static private EditText editTextDocument;
    static private EditText editTextPackingslip;
    static private EditText editTextBin;
    private static Switch switchCheckBarcodes;
    static private Button createReceiveButton;
    static private Button cancelButton;
    //End Region private Properties

    //Region Constructor
    public CreateReceiveActivity() {
    }
    //End Region Constructor


    //Region Default Methods

    @Override
    protected void onCreate(Bundle pvSavedInstanceState) {
        super.onCreate(pvSavedInstanceState);
        setContentView(R.layout.activity_create_receive_order);
        this.mActivityInitialize();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cBarcodeScan.pUnregisterBarcodeReceiver();
    }

    @Override
    public void onPause() {
        super.onPause();
        cBarcodeScan.pUnregisterBarcodeReceiver();
    }

    @Override
    public void onResume() {
        super.onResume();
        cBarcodeScan.pRegisterBarcodeReceiver();
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

        this.mSetToolbar(getResources().getString(R.string.create_external_receive));

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
        CreateReceiveActivity.toolbarImage = findViewById(R.id.toolbarImage);
        CreateReceiveActivity.toolbarTitle = findViewById(R.id.toolbarTitle);
        CreateReceiveActivity.toolbarSubTitle = findViewById(R.id.toolbarSubtext);
        CreateReceiveActivity.editTextDocument = findViewById(R.id.editTextDocument);
        CreateReceiveActivity.editTextPackingslip = findViewById(R.id.editTextPackingslip);
        CreateReceiveActivity.editTextBin = findViewById(R.id.editTextBin);
        CreateReceiveActivity.switchCheckBarcodes = findViewById(R.id.checkBarcodesSwitch);
        CreateReceiveActivity.createReceiveButton = findViewById(R.id.createOrderButton);
        CreateReceiveActivity.cancelButton = findViewById(R.id.cancelButton);
    }

    @Override
    public void mSetToolbar(String pvScreenTitle) {
        CreateReceiveActivity.toolbarImage.setImageResource(R.drawable.ic_menu_intake_eo);
        CreateReceiveActivity.toolbarTitle.setText(pvScreenTitle);
        CreateReceiveActivity.toolbarTitle.setSelected(true);
        CreateReceiveActivity.toolbarSubTitle.setText(cUser.currentUser.currentBranch.getBranchNameStr());

        ViewCompat.setTransitionName(toolbarImage, VIEW_NAME_HEADER_IMAGE);
        ViewCompat.setTransitionName(toolbarTitle, VIEW_NAME_HEADER_TEXT);
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
        CreateReceiveActivity.editTextDocument.setFilters(filterArray);

       if (!cUser.currentUser.currentBranch.isBinMandatoryBln()) {
           CreateReceiveActivity.editTextBin.setVisibility(View.GONE);
       }
       else {
           mSetBin();
       }

        if (!cUser.currentUser.currentBranch.getReturnDefaultBinStr().isEmpty()) {
            CreateReceiveActivity.editTextBin.setText(cUser.currentUser.currentBranch.getReceiveDefaultBinStr());
        }

        if (!cSetting.RECEIVE_BARCODE_CHECK()) {
            CreateReceiveActivity.switchCheckBarcodes.setVisibility(View.GONE);
        }
        else{
            CreateReceiveActivity.switchCheckBarcodes.setChecked(true);
        }

        if (BuildConfig.FLAVOR.equalsIgnoreCase(cProductFlavor.FlavorEnu.BMN.toString())) {
            CreateReceiveActivity.editTextBin.setVisibility(View.GONE);
            CreateReceiveActivity.editTextPackingslip.setVisibility(View.GONE);
            CreateReceiveActivity.switchCheckBarcodes.setVisibility(View.GONE);
        }

    }

    @Override
    public void mSetListeners() {
        this.mSetCreateListener();
        this.mSetCancelListener();
        this.mSetEditorActionListener();
    }

    @Override
    public void mInitScreen() {

    }

    //End Region iICSDefaultActivity defaults

    //Region Public Methods

    public static void pHandleScan(cBarcodeScan pvBarcodeScan, boolean pvIsDocumentBln, boolean pvIsPackingSlipBln, boolean pvBinBln) {

        String barcodeWithoutPrefixStr = cRegex.pStripRegexPrefixStr(pvBarcodeScan.getBarcodeOriginalStr());

        boolean documentBln = false;
        boolean binBln = false;

        if (pvIsDocumentBln) {
            CreateReceiveActivity.editTextDocument.setText(barcodeWithoutPrefixStr);
            CreateReceiveActivity.editTextPackingslip.requestFocus();
            return;
        }

        if (pvIsPackingSlipBln) {
            CreateReceiveActivity.editTextPackingslip.setText(barcodeWithoutPrefixStr);
            CreateReceiveActivity.editTextBin.requestFocus();
            return;
        }

        if (pvBinBln) {
            CreateReceiveActivity.editTextBin.setText(barcodeWithoutPrefixStr);
            CreateReceiveActivity.editTextBin.requestFocus();
        }

        cUserInterface.pCheckAndCloseOpenDialogs();


        if (BuildConfig.FLAVOR.equalsIgnoreCase(cProductFlavor.FlavorEnu.BMN.toString())) {
            if (!cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(), cBarcodeLayout.barcodeLayoutEnu.DOCUMENT)) {
                cUserInterface.pDoExplodingScreen(cAppExtension.context.getString(R.string.message_scan_is_not_document),"",true,true);
                return;
            }
        }

        if (cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(), cBarcodeLayout.barcodeLayoutEnu.BIN)) {
            binBln = true;
        }

        if (cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(), cBarcodeLayout.barcodeLayoutEnu.DOCUMENT)) {
            documentBln = true;
        }

        //has prefix, is DOCUMENT
        if (documentBln   ||CreateReceiveActivity.editTextDocument.getText().toString().isEmpty()) {
            CreateReceiveActivity.editTextDocument.setText(barcodeWithoutPrefixStr);

            if (BuildConfig.FLAVOR.equalsIgnoreCase(cProductFlavor.FlavorEnu.BMN.toString())) {

                IntakeAndReceiveSelectActivity.startedViaMenuBln = false;
                CreateReceiveActivity.mCreateOrder(editTextDocument.getText().toString().trim(),
                        editTextPackingslip.getText().toString().trim(),
                        editTextBin.getText().toString().trim(),
                        switchCheckBarcodes.isChecked());
                return;
            }

            CreateReceiveActivity.editTextPackingslip.requestFocus();
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

            CreateReceiveActivity.editTextBin.setText(barcodeWithoutPrefixStr);
            return;


        }



        CreateReceiveActivity.editTextPackingslip.setText(barcodeWithoutPrefixStr);
        CreateReceiveActivity.editTextPackingslip.requestFocus();
    }

    //End Region Public Methods

    //Region Private Method

    private static void mCreateOrder(final String pvDocumentStr, final String pvPackingSlipStr, final String pvBinCodeStr, final boolean pvCheckBarcodesBln){


        // Show that we are getting data
        cUserInterface.pShowGettingData();

        new Thread(new Runnable() {
            public void run() {
                mHandleCreateOrder(pvDocumentStr,pvPackingSlipStr,pvBinCodeStr,pvCheckBarcodesBln);
            }
        }).start();

    }

    private void mSetCancelListener() {
        CreateReceiveActivity.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View pvView) {
                CreateReceiveActivity.mStartOrderSelectActivity();
            }
        });
    }

    private static void mStartOrderSelectActivity() {
        Intent intent = new Intent(cAppExtension.context, IntakeAndReceiveSelectActivity.class);
        IntakeAndReceiveSelectActivity.startedViaMenuBln = false;
        cAppExtension.activity.startActivity(intent);
    }

    private void mSetCreateListener() {
        CreateReceiveActivity.createReceiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View pvView) {

                if (CreateReceiveActivity.editTextDocument.getText().toString().isEmpty()){
                    cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.message_scan_receive_document),null);
                    return;
                }

                if (!BuildConfig.FLAVOR.equalsIgnoreCase(cProductFlavor.FlavorEnu.BMN.toString())) {
                }  if (CreateReceiveActivity.editTextBin.getText().toString().isEmpty() && cUser.currentUser.currentBranch.isBinMandatoryBln()){
                    cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.message_scan_receive_bin),null);
                    return;
                }


                IntakeAndReceiveSelectActivity.startedViaMenuBln = false;
                CreateReceiveActivity.mCreateOrder(editTextDocument.getText().toString().trim(),
                                                            editTextPackingslip.getText().toString().trim(),
                                                            editTextBin.getText().toString().trim(),
                                                            switchCheckBarcodes.isChecked());
            }
        });
    }

    private void mSetEditorActionListener() {
        CreateReceiveActivity.editTextDocument.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE || i == EditorInfo.IME_ACTION_GO ) {

                    CreateReceiveActivity.pHandleScan(cBarcodeScan.pFakeScan(CreateReceiveActivity.editTextDocument.getText().toString()),true,false, false);
                    cUserInterface.pHideKeyboard();

                }
                return true;
            }
        });

        CreateReceiveActivity.editTextPackingslip.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE || i == EditorInfo.IME_ACTION_GO ) {

                    CreateReceiveActivity.pHandleScan(cBarcodeScan.pFakeScan(CreateReceiveActivity.editTextPackingslip.getText().toString()),false,true,false);
                    cUserInterface.pHideKeyboard();

                }
                return true;
            }
        });

        CreateReceiveActivity.editTextBin.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE || i == EditorInfo.IME_ACTION_GO ) {

                    CreateReceiveActivity.pHandleScan(cBarcodeScan.pFakeScan(CreateReceiveActivity.editTextBin.getText().toString()),false,false,true);
                    cUserInterface.pHideKeyboard();

                }
                return true;
            }
        });


    }

    private void mSetBin(){


        if (!cUser.currentUser.currentBranch.getReceiveDefaultBinStr().isEmpty()) {
            CreateReceiveActivity.editTextBin.setText(cUser.currentUser.currentBranch.getReceiveDefaultBinStr());
        }
        else {

            if (!cUser.currentUser.currentBranch.pGetReceiveBinsViaWebserviceBln()) {
                return;
            }

            if (cUser.currentUser.currentBranch.receiveBinsObl != null&& cUser.currentUser.currentBranch.receiveBinsObl.size() == 1) {
                CreateReceiveActivity.editTextBin.setText(cUser.currentUser.currentBranch.receiveBinsObl.get(0).getBinCodeStr());
            }

        }



    }

    private static void mHandleCreateOrder(String pvDocumentstr, String pvPackingSlipStr, String pvBinCodeStr, boolean pvCheckBarcodesBln ){

        cResult hulpResult;

        hulpResult = CreateReceiveActivity.mTryToCreateOrderRst(pvDocumentstr,pvPackingSlipStr,pvBinCodeStr,pvCheckBarcodesBln);
        //Try to create the order
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


            if (cIntakeorder.currentIntakeOrder == null) {
                cUserInterface.pDoExplodingScreen(cAppExtension.activity.getString(R.string.message_next_activity_not_found),"",true,true);
                return;
            }

        }

        //Try to lock the order
        if (!CreateReceiveActivity.mTryToLockOrderBln()) {
            cUserInterface.pDoExplodingScreen(cAppExtension.activity.getString(R.string.message_locking_order_failed),"",true,true);
            return;
        }

        //Delete the detail, so we can get them from the webservice
        cIntakeorder.currentIntakeOrder.pDeleteDetailsBln();

        hulpResult = CreateReceiveActivity.mGetReceiveOrderDetailsRst();
        if (!hulpResult.resultBln) {
            CreateReceiveActivity.mStepFailed(hulpResult.messagesStr());
            return;
        }

        cAppExtension.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // If everything went well, then start Lines Activity
                CreateReceiveActivity.mShowReceiveLinesActivity();
            }
        });

    }

    private static cResult mTryToCreateOrderRst(String pvDocumentstr, String pvPackingSlipStr, String pvBinCodeStr, boolean pvCheckBarcodesBln){

        cResult result =  cIntakeorder.pCreateReceiveOrderViaWebserviceRst(pvDocumentstr, pvPackingSlipStr, pvBinCodeStr, pvCheckBarcodesBln);
        if (!result.resultBln) {
            CreateReceiveActivity.editTextDocument.setText("");
            mStepFailed(result.messagesStr());
            return  result;
        }

        return result;

    }

    private static boolean mTryToLockOrderBln(){

        cResult hulpResult  = cIntakeorder.currentIntakeOrder.pLockViaWebserviceRst(cWarehouseorder.StepCodeEnu.Receive_InTake, cWarehouseorder.WorkflowExternalReceiveStepEnu.Receive_External);

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

    private static cResult mGetReceiveOrderDetailsRst(){

        cResult result;

        result = new cResult();
        result.resultBln = true;

        //Get all Items for current order, if size = 0 or webservice error then stop
        if (!cIntakeorder.currentIntakeOrder.pGetReceiveItemsViaWebserviceBln(true)) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_intakelines_failed));
            return result;
        }

        //Get all linesInt for current order, if size = 0 or webservice error then stop
        if (!cIntakeorder.currentIntakeOrder.pGetReceiveLinesViaWebserviceBln(true)) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_intakelines_failed));
            return result;
        }

        // Get all barcodes, if size =0 or webservice error then stop
        if (!cIntakeorder.currentIntakeOrder.pGetBarcodesViaWebserviceBln(true)) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_barcodes_failed));
            return result;
        }

        // Get all comments
        if (!cIntakeorder.currentIntakeOrder.pGetCommentsViaWebserviceBln(true)) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_comments_failed));
            return result;
        }

        return  result;
    }

    private static void mStepFailed(String pvErrorMessageStr){

        if (cIntakeorder.currentIntakeOrder != null) {
            cUserInterface.pDoExplodingScreen(pvErrorMessageStr, cIntakeorder.currentIntakeOrder.getOrderNumberStr(), true, true );
        }
        else
        {
            cUserInterface.pDoExplodingScreen(pvErrorMessageStr,cAppExtension.activity.getString(R.string.message_couldnt_create_order) + " " +  CreateReceiveActivity.editTextDocument.getText().toString(), true, true );
        }

        if (cIntakeorder.currentIntakeOrder != null) {
            cIntakeorder.currentIntakeOrder.pLockReleaseViaWebserviceBln();
        }


        cUserInterface.pCheckAndCloseOpenDialogs();
    }

    private static void mShowReceiveLinesActivity() {

        cUserInterface.pCheckAndCloseOpenDialogs();
        Intent intent = new Intent(cAppExtension.context, ReceiveLinesActivity.class);
        ActivityCompat.startActivity(cAppExtension.context,intent, null);

    }

    //End Region Private Methods


}
