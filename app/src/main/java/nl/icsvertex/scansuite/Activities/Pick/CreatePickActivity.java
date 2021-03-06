package nl.icsvertex.scansuite.Activities.Pick;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.KeyEvent;
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
import androidx.core.app.ActivityCompat;
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
import SSU_WHS.Basics.Settings.cSetting;
import SSU_WHS.Basics.StockOwner.cStockOwner;
import SSU_WHS.Basics.Users.cUser;
import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.General.cPublicDefinitions;
import SSU_WHS.Picken.Pickorders.cPickorder;
import nl.icsvertex.scansuite.R;


public class CreatePickActivity extends AppCompatActivity implements iICSDefaultActivity {

    //Region Public Properties

    //End Region Public Properties

    //Region Private Properties

    private ImageView toolbarImage;
    private TextView toolbarTitle;
    private TextView toolbarSubTitle;

    private Spinner spinner;
    private EditText editTextDocument;
    private SwitchCompat switchCheckBarcodes;

    private Button createButton;
    private Button cancelButton;

    public static cWarehouseorder.PickMainTypeEnu pickMainTypeEnu;

    //End Region private Properties

    //Region Constructor
    public CreatePickActivity() {
    }
    //End Region Constructor


    //Region Default Methods

    @Override
    protected void onCreate(Bundle pvSavedInstanceState) {
        super.onCreate(pvSavedInstanceState);
        setContentView(R.layout.activity_create_pick_order);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (cAppExtension.activity instanceof CreatePickActivity) {
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

        this.mSetToolbar(getResources().getString(R.string.create_pick));

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
        this.spinner = findViewById(R.id.stockownerSpinner);
        this.toolbarSubTitle = findViewById(R.id.toolbarSubtext);
        this.editTextDocument = findViewById(R.id.editTextDocument);
        this.switchCheckBarcodes = findViewById(R.id.checkBarcodesSwitch);
        this.createButton = findViewById(R.id.createButton);
        this.cancelButton = findViewById(R.id.cancelButton);
    }

    @Override
    public void mSetToolbar(String pvScreenTitleStr) {
        this.toolbarImage.setImageResource(R.drawable.ic_menu_pick_pf);
        this.toolbarTitle.setText(pvScreenTitleStr);
        this.toolbarTitle.setSelected(true);
        this.toolbarSubTitle.setText(cUser.currentUser.currentBranch.getBranchNameStr());

        ViewCompat.setTransitionName(this.toolbarImage, cPublicDefinitions.VIEW_NAME_HEADER_IMAGE);
        ViewCompat.setTransitionName(this.toolbarTitle, cPublicDefinitions.VIEW_NAME_HEADER_TEXT);
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
        if (!cSetting.PICK_BARCODE_CHECK()) {
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
        this.mSetStockOwnerSpinnerListener();
    }

    @Override
    public void mInitScreen() {

    }

    //End Region iICSDefaultActivity defaults

    //Region Public Methods

    public void pHandleScan(cBarcodeScan pvBarcodeScan) {
        cUserInterface.pCheckAndCloseOpenDialogs();
        this.editTextDocument.setText(cRegex.pStripRegexPrefixStr(pvBarcodeScan.getBarcodeOriginalStr()));
    }

    //End Region Public Methods

    //Region Private Method

    private void mCreateOrder(final String pvDocumentStr, final boolean pvCheckBarcodesBln){

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

    private void mStartOrderSelectActivity() {
        Intent intent = new Intent(cAppExtension.context, PickorderSelectActivity.class);
        PickorderSelectActivity.startedViaMenuBln = false;
        cAppExtension.activity.startActivity(intent);
    }

    private void mSetCreateListener() {
        this.createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View pvView) {
                PickorderSelectActivity.startedViaMenuBln = false;
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

    private void mHandleCreateOrder(String pvDocumentstr,boolean pvCheckBarcodesBln ){

        cResult hulpResult;

        hulpResult = this.mTryToCreateOrderRst(pvDocumentstr,pvCheckBarcodesBln);
        //Try to create the order
        if (!hulpResult.resultBln) {
            return;
        }

        // We created an assignment, but need to open a different assignment
        if (hulpResult.activityActionEnu == cWarehouseorder.ActivityActionEnu.Next && hulpResult.resultAction != null) {

            //all Pickorder
            if (!cPickorder.pGetPickOrdersViaWebserviceBln(true, false,"")) {
                cUserInterface.pDoExplodingScreen(cAppExtension.context.getString(R.string.error_get_pickorders_failed), "", true, true );
                return;
            }

            if (!cPickorder.pGetPickOrdersViaWebserviceBln(false, true,"")) {
                cUserInterface.pDoExplodingScreen(cAppExtension.context.getString(R.string.error_get_pickorders_failed), "", true, true );
                return;
            }


            if (cPickorder.allPickordersObl == null || cPickorder.allPickordersObl.size() == 0) {
                cUserInterface.pDoExplodingScreen(cAppExtension.context.getString(R.string.error_get_next_activity_failed), "", true, true );
                return;
            }


            for (cPickorder pickorder : cPickorder.allPickordersObl) {

                if (pickorder.getOrderNumberStr().equalsIgnoreCase(hulpResult.resultAction.nextAssignmentStr)) {
                    cPickorder.currentPickOrder = pickorder;
                    break;
                }
            }

            if ( cPickorder.currentPickOrder == null) {
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
        cPickorder.currentPickOrder.pDeleteDetailsBln();

        hulpResult = cPickorder.currentPickOrder.pGetSelfpickDetailsRst();
        if (!hulpResult.resultBln) {
            this.mStepFailed(hulpResult.messagesStr());
            return;
        }

        FirebaseCrashlytics.getInstance().setCustomKey("Ordernumber", cPickorder.currentPickOrder.getOrderNumberStr());

        cAppExtension.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // If everything went well, then start Lines Activity
                mShowPickGeneratedLines();
            }
        });

    }

    private  cResult mTryToCreateOrderRst(String pvDocumentstr,  boolean pvCheckBarcodesBln){

        cResult result;

             result =  cPickorder.pCreatePickOrderViaWebserviceRst(pvDocumentstr, pvCheckBarcodesBln);
            if (!result.resultBln) {
                this.editTextDocument.setText("");
                this.mStepFailed(result.messagesStr());
                return  result;
            }

        return result;

    }

    private boolean mTryToLockOrderBln(){

        cResult hulpResult  = cPickorder.currentPickOrder.pLockViaWebserviceRst(cWarehouseorder.StepCodeEnu.Pick_Picking,cWarehouseorder.WorkflowPickStepEnu.PickPicking);

        //Everything was fine, so we are done
        if (hulpResult.resultBln) {
            return true;
        }

        //Something went wrong, but no further actions are needed, so ony show reason of failure
        if (hulpResult.activityActionEnu == cWarehouseorder.ActivityActionEnu.Unknown ) {
            this.mStepFailed(hulpResult.messagesStr());
            return  false;
        }
        return (hulpResult.activityActionEnu != cWarehouseorder.ActivityActionEnu.Delete) &&
                (hulpResult.activityActionEnu != cWarehouseorder.ActivityActionEnu.NoStart);

    }



    private void mStepFailed(String pvErrorMessageStr){

        if (cPickorder.currentPickOrder != null) {
            cUserInterface.pDoExplodingScreen(pvErrorMessageStr, cPickorder.currentPickOrder.getOrderNumberStr(), true, true );
        }
        else
        {
            cUserInterface.pDoExplodingScreen(pvErrorMessageStr,cAppExtension.activity.getString(R.string.message_couldnt_create_order) + " " +  this.editTextDocument.getText().toString(), true, true );
        }

        if (cPickorder.currentPickOrder != null) {
            cPickorder.currentPickOrder.pLockReleaseViaWebserviceBln(cWarehouseorder.StepCodeEnu.Pick_Picking, cWarehouseorder.WorkflowPickStepEnu.PickPicking);
        }

        cUserInterface.pCheckAndCloseOpenDialogs();
    }

    private void mShowPickGeneratedLines() {

        cUserInterface.pCheckAndCloseOpenDialogs();

        Intent intent = new Intent(cAppExtension.context, PickorderLinesGeneratedActivity.class);
        ActivityCompat.startActivity(cAppExtension.context,intent, null);

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

    //End Region Private Methods


}
