package nl.icsvertex.scansuite.Activities.Intake;


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
import ICS.Utils.cRegex;
import ICS.Utils.cResult;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Basics.Settings.cSetting;
import SSU_WHS.Basics.Users.cUser;
import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.General.cPublicDefinitions;
import SSU_WHS.Intake.Intakeorders.cIntakeorder;
import nl.icsvertex.scansuite.Activities.IntakeAndReceive.IntakeAndReceiveSelectActivity;
import nl.icsvertex.scansuite.R;


public class CreateIntakeActivity extends AppCompatActivity implements iICSDefaultActivity {

    //Region Public Properties

    //End Region Public Properties

    //Region Private Properties

    private ImageView toolbarImage;
    private TextView toolbarTitle;
    private TextView toolbarSubTitle;
    private EditText editTextDocument;
    private EditText editTextBin;
    private Switch switchCheckBarcodes;
    private  Button createIntakeButton;
    private  Button cancelButton;
    //End Region private Properties

    //Region Constructor
    public CreateIntakeActivity() {
    }
    //End Region Constructor


    //Region Default Methods

    @Override
    protected void onCreate(Bundle pvSavedInstanceState) {
        super.onCreate(pvSavedInstanceState);
        setContentView(R.layout.activity_create_intake_order);
        this.mActivityInitialize();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (cAppExtension.activity instanceof CreateIntakeActivity) {
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
        cBarcodeScan.pRegisterBarcodeReceiver(this.getClass().getSimpleName());
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
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

        this.mSetToolbar(getResources().getString(R.string.create_intake_order));

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
        this.editTextDocument = findViewById(R.id.editTextDocument);
        this.editTextBin = findViewById(R.id.editTextBin);
        this.switchCheckBarcodes = findViewById(R.id.checkBarcodesSwitch);
        this.createIntakeButton = findViewById(R.id.createButton);
        this.cancelButton = findViewById(R.id.cancelButton);
    }

    @Override
    public void mSetToolbar(String pvScreenTitleStr) {
        this.toolbarImage.setImageResource(R.drawable.ic_menu_intake_ma);

        if (cUser.currentUser.currentAuthorisation.getCustomAuthorisation() != null) {
            this.toolbarImage.setImageBitmap(cUser.currentUser.currentAuthorisation.customImageBmp());
            this.toolbarTitle.setText(cAppExtension.activity.getString(R.string.create) + " " + cUser.currentUser.currentAuthorisation.getCustomAuthorisation().getDescriptionStr());
        }
        else {
            this.toolbarImage.setImageResource(R.drawable.ic_menu_intake);
            this.toolbarTitle.setText(pvScreenTitleStr);
        }

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

        if (!cUser.currentUser.currentBranch.isBinMandatoryBln()) {
            this.editTextBin.setVisibility(View.GONE);
        }
        else {
            this.mSetBin();
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
    }

    @Override
    public void mInitScreen() {

    }

    //End Region iICSDefaultActivity defaults

    //Region Public Methods

    public void pHandleScan(cBarcodeScan pvBarcodeScan) {

        String barcodeWithoutPrefixStr = cRegex.pStripRegexPrefixStr(pvBarcodeScan.getBarcodeOriginalStr());
        cUserInterface.pCheckAndCloseOpenDialogs();
        this.editTextDocument.setText(barcodeWithoutPrefixStr);

    }

    //End Region Public Methods

    //Region Private Method

    private  void mCreateOrder(final String pvDocumentStr, final boolean pvCheckBarcodesBln){


        // Show that we are getting data
        cUserInterface.pShowGettingData();

        new Thread(new Runnable() {
            public void run() {
                mHandleCreateOrder(pvDocumentStr,pvCheckBarcodesBln);
            }
        }).start();

    }

    private void mSetCancelListener() {
        this.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View pvView) {
                mStartOrderSelectActivity();
            }
        });
    }

    private  void mStartOrderSelectActivity() {
        Intent intent = new Intent(cAppExtension.context, IntakeAndReceiveSelectActivity.class);
        IntakeAndReceiveSelectActivity.startedViaMenuBln = false;
        cAppExtension.activity.startActivity(intent);
    }

    private void mSetCreateListener() {
        this.createIntakeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View pvView) {
                IntakeAndReceiveSelectActivity.startedViaMenuBln = false;
                mCreateOrder(editTextDocument.getText().toString().trim(),
                        switchCheckBarcodes.isChecked());
            }
        });
    }

    private void mSetEditorActionListener() {
        this.editTextDocument.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE || i == EditorInfo.IME_ACTION_GO ) {

                    pHandleScan(cBarcodeScan.pFakeScan(editTextDocument.getText().toString()));
                    cUserInterface.pHideKeyboard();

                }
                return true;
            }
        });

    }

    private  void mHandleCreateOrder(String pvDocumentstr, boolean pvCheckBarcodesBln ){

        cResult hulpResult;

        hulpResult = this.mTryToCreateOrderRst(pvDocumentstr,pvCheckBarcodesBln);
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

        cAppExtension.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // If everything went well, then start Lines Activity
                mShowIntakeOrderLinesActivity();
            }
        });

    }

    private  cResult mTryToCreateOrderRst(String pvDocumentstr, boolean pvCheckBarcodesBln){

        cResult result =  cIntakeorder.pCreateIntakeOrderViaWebserviceRst(pvDocumentstr, pvCheckBarcodesBln);
        if (!result.resultBln) {
            this.editTextDocument.setText("");
            mStepFailed(result.messagesStr());
            return  result;
        }

        return result;

    }

    private  boolean mTryToLockOrderBln(){

        cResult hulpResult;
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

    private  void mShowIntakeOrderLinesActivity() {

        cUserInterface.pCheckAndCloseOpenDialogs();
        Intent intent = new Intent(cAppExtension.context, IntakeorderMASLinesActivity.class);
        ActivityCompat.startActivity(cAppExtension.context,intent, null);

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

    //End Region Private Methods


}
