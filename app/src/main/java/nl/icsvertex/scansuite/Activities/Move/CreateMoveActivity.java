package nl.icsvertex.scansuite.Activities.Move;
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
import SSU_WHS.Move.MoveOrders.cMoveorder;
import nl.icsvertex.scansuite.Activities.Receive.ReceiveLinesActivity;
import nl.icsvertex.scansuite.BuildConfig;
import nl.icsvertex.scansuite.R;


public class CreateMoveActivity extends AppCompatActivity implements iICSDefaultActivity {

    //Region Public Properties
    public static final String VIEW_NAME_HEADER_IMAGE = "detail:header:imageStr";
    public static final String VIEW_NAME_HEADER_TEXT = "detail:header:text";
    //End Region Public Properties

    //Region Private Properties

    private static ImageView toolbarImage;
    private static TextView toolbarTitle;
    private static TextView toolbarSubTitle;
    static private EditText editTextDocument;
    static private EditText editTextBin;
    private static Switch switchCheckBarcodes;
    static private Button createMoveButton;
    static private Button cancelButton;
    //End Region private Properties

    //Region Constructor
    public CreateMoveActivity() {
    }
    //End Region Constructor


    //Region Default Methods

    @Override
    protected void onCreate(Bundle pvSavedInstanceState) {
        super.onCreate(pvSavedInstanceState);
        setContentView(R.layout.activity_create_move_order);
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

        this.mSetToolbar(getResources().getString(R.string.create_move));

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
        CreateMoveActivity.toolbarImage = findViewById(R.id.toolbarImage);
        CreateMoveActivity.toolbarTitle = findViewById(R.id.toolbarTitle);
        CreateMoveActivity.toolbarSubTitle = findViewById(R.id.toolbarSubtext);
        CreateMoveActivity.editTextDocument = findViewById(R.id.editTextDocument);
        CreateMoveActivity.editTextBin = findViewById(R.id.editTextBin);
        CreateMoveActivity.switchCheckBarcodes = findViewById(R.id.checkBarcodesSwitch);
        CreateMoveActivity.createMoveButton = findViewById(R.id.createOrderButton);
        CreateMoveActivity.cancelButton = findViewById(R.id.cancelButton);
    }

    @Override
    public void mSetToolbar(String pvScreenTitle) {
        CreateMoveActivity.toolbarImage.setImageResource(R.drawable.ic_menu_move);
        CreateMoveActivity.toolbarTitle.setText(pvScreenTitle);
        CreateMoveActivity.toolbarTitle.setSelected(true);
        CreateMoveActivity.toolbarSubTitle.setText(cUser.currentUser.currentBranch.getBranchNameStr());

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
        CreateMoveActivity.editTextDocument.setFilters(filterArray);

       if (!cUser.currentUser.currentBranch.isBinMandatoryBln()) {
           CreateMoveActivity.editTextBin.setVisibility(View.GONE);
       }
       else {
           mSetBin();
       }

        if (!cUser.currentUser.currentBranch.getReturnDefaultBinStr().isEmpty()) {
            CreateMoveActivity.editTextBin.setText(cUser.currentUser.currentBranch.getMoveDefaultBinStr());
        }

        if (!cSetting.RECEIVE_BARCODE_CHECK()) {
            CreateMoveActivity.switchCheckBarcodes.setVisibility(View.GONE);
        }
        else{
            CreateMoveActivity.switchCheckBarcodes.setChecked(true);
        }

        if (BuildConfig.FLAVOR.equalsIgnoreCase(cProductFlavor.FlavorEnu.BMN.toString())) {
            CreateMoveActivity.editTextBin.setVisibility(View.GONE);
            CreateMoveActivity.switchCheckBarcodes.setVisibility(View.GONE);
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

    public static void pHandleScan(cBarcodeScan pvBarcodeScan, boolean pvIsDocumentBln, boolean pvBinBln) {

        String barcodeWithoutPrefixStr = cRegex.pStripRegexPrefixStr(pvBarcodeScan.getBarcodeOriginalStr());

        boolean documentBln = false;
        boolean binBln = false;

        if (pvIsDocumentBln) {
            CreateMoveActivity.editTextDocument.setText(barcodeWithoutPrefixStr);
            CreateMoveActivity.editTextBin.requestFocus();
            return;
        }
        if (pvBinBln) {
            CreateMoveActivity.editTextBin.setText(barcodeWithoutPrefixStr);
            CreateMoveActivity.editTextBin.requestFocus();
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
        if (documentBln   || CreateMoveActivity.editTextDocument.getText().toString().isEmpty()) {
            CreateMoveActivity.editTextDocument.setText(barcodeWithoutPrefixStr);

            if (BuildConfig.FLAVOR.equalsIgnoreCase(cProductFlavor.FlavorEnu.BMN.toString())) {

                MoveorderSelectActivity.startedViaMenuBln = false;
                CreateMoveActivity.mCreateOrder(editTextDocument.getText().toString().trim(),
                        editTextBin.getText().toString().trim(),
                        switchCheckBarcodes.isChecked());
                return;
            }

            CreateMoveActivity.editTextBin.requestFocus();
            return;
        }

        if (binBln) {
            barcodeWithoutPrefixStr = cRegex.pStripRegexPrefixStr(pvBarcodeScan.getBarcodeOriginalStr());

            cBranchBin branchBin = cUser.currentUser.currentBranch.pGetBinByCode(barcodeWithoutPrefixStr);

            if (branchBin == null) {
                cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.message_unknown_bin), null);
                return;
            }

            CreateMoveActivity.editTextBin.setText(barcodeWithoutPrefixStr);
            return;


        }
    }

    //End Region Public Methods

    //Region Private Method

    private static void mCreateOrder(final String pvDocumentStr, final String pvBinCodeStr, final boolean pvCheckBarcodesBln){


        // Show that we are getting data
        cUserInterface.pShowGettingData();

        new Thread(new Runnable() {
            public void run() {
                mHandleCreateOrder(pvDocumentStr,pvBinCodeStr,pvCheckBarcodesBln);
            }
        }).start();

    }

    private void mSetCancelListener() {
        CreateMoveActivity.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View pvView) {
                CreateMoveActivity.mStartOrderSelectActivity();
            }
        });
    }

    private static void mStartOrderSelectActivity() {
        Intent intent = new Intent(cAppExtension.context, MoveorderSelectActivity.class);
        MoveorderSelectActivity.startedViaMenuBln = false;
        cAppExtension.activity.startActivity(intent);
    }

    private void mSetCreateListener() {
        CreateMoveActivity.createMoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View pvView) {

                if (CreateMoveActivity.editTextDocument.getText().toString().isEmpty()){
                    cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.message_scan_receive_document),null);
                    return;
                }

                 MoveorderSelectActivity.startedViaMenuBln = false;
                CreateMoveActivity.mCreateOrder(editTextDocument.getText().toString().trim(),
                                                editTextBin.getText().toString().trim(),
                                                switchCheckBarcodes.isChecked());
            }
        });
    }

    private void mSetEditorActionListener() {
        CreateMoveActivity.editTextDocument.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE || i == EditorInfo.IME_ACTION_GO ) {
                    CreateMoveActivity.pHandleScan(cBarcodeScan.pFakeScan(CreateMoveActivity.editTextDocument.getText().toString()),true,false);
                    cUserInterface.pHideKeyboard();
                }
                return true;
            }
        });

        CreateMoveActivity.editTextBin.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE || i == EditorInfo.IME_ACTION_GO ) {

                    CreateMoveActivity.pHandleScan(cBarcodeScan.pFakeScan(CreateMoveActivity.editTextBin.getText().toString()),false,false);
                    cUserInterface.pHideKeyboard();

                }
                return true;
            }
        });


    }

    private void mSetBin(){

        if (!cUser.currentUser.currentBranch.getMoveDefaultBinStr().isEmpty()) {
            CreateMoveActivity.editTextBin.setText(cUser.currentUser.currentBranch.getMoveDefaultBinStr());
        }
    }

    private static void mHandleCreateOrder(String pvDocumentstr, String pvBinCodeStr, boolean pvCheckBarcodesBln ){

        cResult hulpResult;

        hulpResult = CreateMoveActivity.mTryToCreateOrderRst(pvDocumentstr,pvBinCodeStr,pvCheckBarcodesBln);
        //Try to create the order
        if (!hulpResult.resultBln) {
            return;
        }

        // We created an assignment, but need to open a different assignment
        if (hulpResult.activityActionEnu == cWarehouseorder.ActivityActionEnu.Next && hulpResult.resultAction != null) {

            //all Moveorders
            if (!cMoveorder.pGetMoveOrdersViaWebserviceBln(true, "","")) {
                cUserInterface.pDoExplodingScreen(cAppExtension.context.getString(R.string.error_get_moveorders_failed), "", true, true );
                return;
            }

            if (cMoveorder.allMoveordersObl == null || cMoveorder.allMoveordersObl.size() == 0) {
                cUserInterface.pDoExplodingScreen(cAppExtension.context.getString(R.string.error_get_next_activity_failed), "", true, true );
                return;
            }


            for (cMoveorder moveorder : cMoveorder.allMoveordersObl) {

                if (moveorder.getOrderNumberStr().equalsIgnoreCase(hulpResult.resultAction.nextAssignmentStr)) {
                    cMoveorder.currentMoveOrder = moveorder;
                    break;
                }
            }

            if ( cMoveorder.currentMoveOrder == null) {
                cUserInterface.pDoExplodingScreen(cAppExtension.activity.getString(R.string.message_next_activity_not_found),"",true,true);
                return;
            }


            if ( cMoveorder.currentMoveOrder == null) {
                cUserInterface.pDoExplodingScreen(cAppExtension.activity.getString(R.string.message_next_activity_not_found),"",true,true);
                return;
            }

        }

        //Try to lock the order
        if (!CreateMoveActivity.mTryToLockOrderBln()) {
            cUserInterface.pDoExplodingScreen(cAppExtension.activity.getString(R.string.message_locking_order_failed),"",true,true);
            return;
        }

        //Delete the detail, so we can get them from the webservice
        cMoveorder.currentMoveOrder.pDeleteDetailsBln();

        hulpResult = CreateMoveActivity.mGetOrderDetailsRst();
        if (!hulpResult.resultBln) {
            CreateMoveActivity.mStepFailed(hulpResult.messagesStr());
            return;
        }

        cAppExtension.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // If everything went well, then start Lines Activity
                CreateMoveActivity.mShowMoveLinesActivity();
            }
        });

    }

    private static cResult mTryToCreateOrderRst(String pvDocumentstr,  String pvBinCodeStr, boolean pvCheckBarcodesBln){

        cResult result =  cMoveorder.pCreateMoveOrderViaWebserviceRst(pvDocumentstr, pvBinCodeStr, pvCheckBarcodesBln);
        if (!result.resultBln) {
            CreateMoveActivity.editTextDocument.setText("");
            mStepFailed(result.messagesStr());
            return  result;
        }

        return result;

    }

    private static boolean mTryToLockOrderBln(){

        cResult hulpResult  = cMoveorder.currentMoveOrder.pLockViaWebserviceRst();

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

    private static cResult mGetOrderDetailsRst(){

        cResult result;

        result = new cResult();
        result.resultBln = true;

        //Get all linesInt for current order, if webservice error then stop
        if (!cMoveorder.currentMoveOrder.pGetLinesViaWebserviceBln(true)) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_movelines_failed));
            return result;
        }

        // Get all barcodes, if webservice error then stop
        if (!cMoveorder.currentMoveOrder.pGetBarcodesViaWebserviceBln(true)) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_barcodes_failed));
            return result;
        }

        // Get all line barcodes, if webservice error then stop
        if (!cMoveorder.currentMoveOrder.pGetLineBarcodesViaWebserviceBln(true)) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_barcodes_failed));
            return result;
        }

        // Get all comments
        if (!cMoveorder.currentMoveOrder.pGetCommentsViaWebserviceBln(true)) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_comments_failed));
            return result;
        }

        return  result;
    }

    private static void mStepFailed(String pvErrorMessageStr){

        if (cMoveorder.currentMoveOrder != null) {
            cUserInterface.pDoExplodingScreen(pvErrorMessageStr, cMoveorder.currentMoveOrder.getOrderNumberStr(), true, true );
        }
        else
        {
            cUserInterface.pDoExplodingScreen(pvErrorMessageStr,cAppExtension.activity.getString(R.string.message_couldnt_create_order) + " " +  CreateMoveActivity.editTextDocument.getText().toString(), true, true );
        }

        if (cMoveorder.currentMoveOrder != null) {
            cMoveorder.currentMoveOrder.pLockReleaseViaWebserviceBln();
        }


        cUserInterface.pCheckAndCloseOpenDialogs();
    }

    private static void mShowMoveLinesActivity() {

        cUserInterface.pCheckAndCloseOpenDialogs();

        Intent intent = new Intent(cAppExtension.context, MoveLinesActivity.class);
        ActivityCompat.startActivity(cAppExtension.context,intent, null);

    }

    //End Region Private Methods


}
