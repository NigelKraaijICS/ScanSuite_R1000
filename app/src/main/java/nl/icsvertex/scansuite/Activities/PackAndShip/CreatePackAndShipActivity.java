package nl.icsvertex.scansuite.Activities.PackAndShip;

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
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.ViewCompat;

import java.util.Objects;

import ICS.Interfaces.iICSDefaultActivity;
import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cRegex;
import ICS.Utils.cResult;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Basics.Users.cUser;
import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.General.cPublicDefinitions;
import SSU_WHS.PackAndShip.PackAndShipOrders.cPackAndShipOrder;
import nl.icsvertex.scansuite.Activities.Move.MoveLinesActivity;
import nl.icsvertex.scansuite.R;


public class CreatePackAndShipActivity extends AppCompatActivity implements iICSDefaultActivity {

    //Region Public Properties

    //End Region Public Properties

    //Region Private Properties

    private ImageView toolbarImage;
    private TextView toolbarTitle;
    private TextView toolbarSubTitle;
    private EditText editTextDocument;
    private Button createButton;
    private Button cancelButton;

    public static cWarehouseorder.PackAndShipMainTypeEnu packAndShipMainTypeEnu;

    //End Region private Properties

    //Region Constructor
    public CreatePackAndShipActivity() {
    }
    //End Region Constructor


    //Region Default Methods

    @Override
    protected void onCreate(Bundle pvSavedInstanceState) {
        super.onCreate(pvSavedInstanceState);
        setContentView(R.layout.activity_create_pack_and_ship_order);
        this.mActivityInitialize();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (cAppExtension.activity instanceof CreatePackAndShipActivity) {
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

        this.mSetToolbar(getResources().getString(R.string.create_pack_and_ship));

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
        this.createButton = findViewById(R.id.createButton);
        this.cancelButton = findViewById(R.id.cancelButton);
    }

    @Override
    public void mSetToolbar(String pvScreenTitleStr) {

        if (cUser.currentUser.currentAuthorisation.getCustomAuthorisation() != null) {
            this.toolbarImage.setImageBitmap(cUser.currentUser.currentAuthorisation.customImageBmp());
            this.toolbarTitle.setText(cUser.currentUser.currentAuthorisation.getCustomAuthorisation().getDescriptionStr());
        }
        else {
            this.toolbarImage.setImageResource(R.drawable.ic_menu_ship);
            this.toolbarTitle.setText(pvScreenTitleStr);
        }

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
        this.editTextDocument.setText(barcodeWithoutPrefixStr);
    }

    //End Region Public Methods

    //Region Private Method

    private void mCreateOrder(final String pvDocumentStr){


        // Show that we are getting data
        cUserInterface.pShowGettingData();

        new Thread(new Runnable() {
            public void run() {
                mHandleCreateOrder(pvDocumentStr);
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
        Intent intent = new Intent(cAppExtension.context, PackAndShipSelectActivity.class);
        PackAndShipSelectActivity.startedViaMenuBln = false;
        cAppExtension.activity.startActivity(intent);
    }

    private void mSetCreateListener() {
        this.createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View pvView) {
                PackAndShipSelectActivity.startedViaMenuBln = false;
                 mCreateOrder(editTextDocument.getText().toString().trim());
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

    private void mHandleCreateOrder(String pvDocumentstr ){

        cResult hulpResult;

        hulpResult = this.mTryToCreateOrderRst(pvDocumentstr);
        //Try to create the order
        if (!hulpResult.resultBln) {
            return;
        }

        // We created an assignment, but need to open a different assignment
        if (hulpResult.activityActionEnu == cWarehouseorder.ActivityActionEnu.Next && hulpResult.resultAction != null) {

            //all Pack and Ship orders
            if (!cPackAndShipOrder.pGetPackAndShipOrdersViaWebserviceBln(true, "")) {
                cUserInterface.pDoExplodingScreen(cAppExtension.context.getString(R.string.error_get_packandshiporders_failed), "", true, true );
                return;
            }

            if (cPackAndShipOrder.allPackAndShipOrdersObl == null || cPackAndShipOrder.allPackAndShipOrdersObl .size() == 0) {
                cUserInterface.pDoExplodingScreen(cAppExtension.context.getString(R.string.error_get_next_activity_failed), "", true, true );
                return;
            }


            for (cPackAndShipOrder packAndShipOrder : cPackAndShipOrder.allPackAndShipOrdersObl ) {

                if (packAndShipOrder.getOrderNumberStr().equalsIgnoreCase(hulpResult.resultAction.nextAssignmentStr)) {
                    cPackAndShipOrder.currentPackAndShipOrder = packAndShipOrder;
                    break;
                }
            }

            if ( cPackAndShipOrder.currentPackAndShipOrder == null) {
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
        cPackAndShipOrder.currentPackAndShipOrder.pDeleteDetailsBln();

        hulpResult = this.mGetOrderDetailsRst();
        if (!hulpResult.resultBln) {
            this.mStepFailed(hulpResult.messagesStr());
            return;
        }

        cAppExtension.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // If everything went well, then start Lines Activity
                mShowPackAndShipActivity();
            }
        });

    }

    private  cResult mTryToCreateOrderRst(String pvDocumentstr){

        cResult result = null;

        if (CreatePackAndShipActivity.packAndShipMainTypeEnu ==cWarehouseorder.PackAndShipMainTypeEnu.SINGLE ) {
            result =  cPackAndShipOrder.pCreatePackAndShipOrderPS1ViaWebserviceRst(pvDocumentstr);
        }

        if (CreatePackAndShipActivity.packAndShipMainTypeEnu ==cWarehouseorder.PackAndShipMainTypeEnu.MULTI ) {
            result =  cPackAndShipOrder.pCreatePackAndShipOrderPSMViaWebserviceRst(pvDocumentstr);
        }

        if (!Objects.requireNonNull(result).resultBln) {
            this.editTextDocument.setText("");
            this.mStepFailed(result.messagesStr());
            return  result;
        }

        return result;
    }

    private boolean mTryToLockOrderBln(){

        cResult hulpResult  = cPackAndShipOrder.currentPackAndShipOrder.pLockViaWebserviceRst();

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

    private cResult mGetOrderDetailsRst(){

        cResult result;

        result = new cResult();
        result.resultBln = true;

        //Get all settings for current order, if webservice error then stop
        if (!cPackAndShipOrder.currentPackAndShipOrder.pGetSettingsViaWebserviceBln(true)) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_settings_failed));
            return result;
        }

        //Get all lines for current order, if webservice error then stop
        if (!cPackAndShipOrder.currentPackAndShipOrder.pGetLinesViaWebserviceBln(true)) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_movelines_failed));
            return result;
        }

        // Get all barcodes, if webservice error then stop
        if (!cPackAndShipOrder.currentPackAndShipOrder.pGetBarcodesViaWebserviceBln(true)) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_barcodes_failed));
            return result;
        }

        // Get all shipments, if webservice error then stop
        if (!cPackAndShipOrder.currentPackAndShipOrder.pGetShipmentsViaWebserviceBln(true)) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_line_barcodes_failed));
            return result;
        }

        // Get all addresses, if webservice error then stop
        if (!cPackAndShipOrder.currentPackAndShipOrder.pGetAddressesViaWebserviceBln(true)) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_adresses_failed));
            return result;
        }

        // Get all shipping methods, if webservice error then stop
        if (!cPackAndShipOrder.currentPackAndShipOrder.pGetShippingMethodsViaWebserviceBln(true)) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_shipping_methods_failed));
            return result;
        }

        // Get all packages, if webservice error then stop
        if (!cPackAndShipOrder.currentPackAndShipOrder.pGetShippingPackagesViaWebserviceBln(true)) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_getting_packages_failed));
            return result;
        }

        // Get all comments
        if (!cPackAndShipOrder.currentPackAndShipOrder.pGetCommentsViaWebserviceBln(true)) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_comments_failed));
            return result;
        }

        return  result;
    }

    private void mStepFailed(String pvErrorMessageStr){

        if (cPackAndShipOrder.currentPackAndShipOrder != null) {
            cUserInterface.pDoExplodingScreen(pvErrorMessageStr, cPackAndShipOrder.currentPackAndShipOrder.getOrderNumberStr(), true, true );
        }
        else
        {
            cUserInterface.pDoExplodingScreen(pvErrorMessageStr,cAppExtension.activity.getString(R.string.message_couldnt_create_order) + " " +  this.editTextDocument.getText().toString(), true, true );
        }

        if (cPackAndShipOrder.currentPackAndShipOrder  != null) {
            cPackAndShipOrder.currentPackAndShipOrder .pLockReleaseViaWebserviceBln();
        }

        cUserInterface.pCheckAndCloseOpenDialogs();
    }

    private void mShowPackAndShipActivity() {

        Intent   intent = null;

        if (cPackAndShipOrder.currentPackAndShipOrder.getOrderTypeStr().equalsIgnoreCase(cWarehouseorder.WorkflowEnu.PS1.toString())) {
            intent =  new Intent(cAppExtension.context, PackAndShipSingleActivity.class);
        }

        if (cPackAndShipOrder.currentPackAndShipOrder.getOrderTypeStr().equalsIgnoreCase(cWarehouseorder.WorkflowEnu.PSM.toString())) {
            intent = new Intent(cAppExtension.context, PackAndShipSelectActivity.class);
        }

        cUserInterface.pCheckAndCloseOpenDialogs();
        ActivityCompat.startActivity(cAppExtension.context,intent, null);

    }


    //End Region Private Methods


}
