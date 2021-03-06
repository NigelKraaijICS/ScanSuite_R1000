package nl.icsvertex.scansuite.Activities.Move;

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
import SSU_WHS.Basics.Authorisations.cAuthorisation;
import SSU_WHS.Basics.BarcodeLayouts.cBarcodeLayout;
import SSU_WHS.Basics.BranchBin.cBranchBin;
import SSU_WHS.Basics.Settings.cSetting;
import SSU_WHS.Basics.StockOwner.cStockOwner;
import SSU_WHS.Basics.Users.cUser;
import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.General.cPublicDefinitions;
import SSU_WHS.Move.Moveorders.cMoveorder;
import nl.icsvertex.scansuite.R;


public class CreateMoveActivity extends AppCompatActivity implements iICSDefaultActivity {

    //Region Public Properties

    //End Region Public Properties

    //Region Private Properties

    private ImageView toolbarImage;
    private TextView toolbarTitle;
    private Spinner spinner;
    private TextView toolbarSubTitle;
    private EditText editTextDocument;
    private EditText editTextBin;
    private SwitchCompat switchCheckBarcodes;
    private Button createMoveButton;
    private Button cancelButton;

    public static cWarehouseorder.MoveMainTypeEnu moveMainTypeEnu;

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
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (cAppExtension.activity instanceof CreateMoveActivity) {
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
        this.toolbarImage = findViewById(R.id.toolbarImage);
        this.toolbarTitle = findViewById(R.id.toolbarTitle);
        this.spinner = findViewById(R.id.stockownerSpinner);
        this.toolbarSubTitle = findViewById(R.id.toolbarSubtext);
        this.editTextDocument = findViewById(R.id.editTextDocument);
        this.editTextBin = findViewById(R.id.editTextBin);
        this.switchCheckBarcodes = findViewById(R.id.checkBarcodesSwitch);
        this.createMoveButton = findViewById(R.id.createButton);
        this.cancelButton = findViewById(R.id.cancelButton);
    }

    @Override
    public void mSetToolbar(String pvScreenTitleStr) {

        if (cUser.currentUser.currentAuthorisation.getCustomAuthorisation() != null) {
            this.toolbarImage.setImageBitmap(cUser.currentUser.currentAuthorisation.customImageBmp());
            this.toolbarTitle.setText(cUser.currentUser.currentAuthorisation.getCustomAuthorisation().getDescriptionStr());
        }
        else {
            this.toolbarImage.setImageResource(R.drawable.ic_menu_move);
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
        this.mShowStockOwnerSpinner();

       if (!cUser.currentUser.currentBranch.isBinMandatoryBln() || cUser.currentUser.currentAuthorisation.getAutorisationEnu() == cAuthorisation.AutorisationEnu.MOVE_MV ) {
           this.editTextBin.setVisibility(View.GONE);
       }
       else {
           this.mSetBin();
       }

       if (cSetting.MOVE_NEW_WORKFLOWS().size() == 1 && cSetting.MOVE_NEW_WORKFLOWS().get(0).equalsIgnoreCase(cAuthorisation.AutorisationEnu.MOVE_MV.toString())) {
            this.editTextBin.setVisibility(View.GONE);
        }

        if (!cUser.currentUser.currentBranch.getMoveDefaultBinStr().isEmpty()) {
            this.editTextBin.setText(cUser.currentUser.currentBranch.getMoveDefaultBinStr());
        }

        if (!cSetting.MOVE_BARCODE_CHECK()) {
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

    public void pHandleScan(cBarcodeScan pvBarcodeScan, boolean pvIsDocumentBln, boolean pvBinBln) {

        String barcodeWithoutPrefixStr = cRegex.pStripRegexPrefixStr(pvBarcodeScan.getBarcodeOriginalStr());

        boolean documentBln = false;
        boolean binBln = false;

        if (pvIsDocumentBln) {
            this.editTextDocument.setText(barcodeWithoutPrefixStr);
            this.editTextBin.requestFocus();
            return;
        }
        if (pvBinBln) {
            this.editTextBin.setText(barcodeWithoutPrefixStr);
            this.editTextBin.requestFocus();
        }

        cUserInterface.pCheckAndCloseOpenDialogs();

        if (cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(), cBarcodeLayout.barcodeLayoutEnu.BIN)) {
            binBln = true;
        }

        if (cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(), cBarcodeLayout.barcodeLayoutEnu.DOCUMENT)) {
            documentBln = true;
        }

        //has prefix, is DOCUMENT
        if (documentBln   || this.editTextDocument.getText().toString().isEmpty()) {
            this.editTextDocument.setText(barcodeWithoutPrefixStr);
            this.editTextBin.requestFocus();
            return;
        }

        if (binBln) {
            barcodeWithoutPrefixStr = cRegex.pStripRegexPrefixStr(pvBarcodeScan.getBarcodeOriginalStr());

            cBranchBin branchBin = cUser.currentUser.currentBranch.pGetBinByCode(barcodeWithoutPrefixStr);

            if (branchBin == null) {
                cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.message_unknown_bin), null);
                return;
            }

            this.editTextBin.setText(barcodeWithoutPrefixStr);
        }
    }

    //End Region Public Methods

    //Region Private Method

    private void mCreateOrder(final String pvDocumentStr, final String pvBinCodeStr, final boolean pvCheckBarcodesBln){


        // Show that we are getting data
        cUserInterface.pShowGettingData();

        new Thread(() -> mHandleCreateOrder(pvDocumentStr,pvBinCodeStr,pvCheckBarcodesBln)).start();

    }

    private void mSetCancelListener() {
        this.cancelButton.setOnClickListener(pvView -> mStartOrderSelectActivity());
    }

    private void mStartOrderSelectActivity() {
        Intent intent = new Intent(cAppExtension.context, MoveorderSelectActivity.class);
        MoveorderSelectActivity.startedViaMenuBln = false;
        startActivity(intent);
    }

    private void mSetCreateListener() {
        this.createMoveButton.setOnClickListener(pvView -> {
             MoveorderSelectActivity.startedViaMenuBln = false;
             mCreateOrder(editTextDocument.getText().toString().trim(),
                                            editTextBin.getText().toString().trim(),
                                            switchCheckBarcodes.isChecked());
        });
    }

    private void mSetEditorActionListener() {
        this.editTextDocument.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_DONE || i == EditorInfo.IME_ACTION_GO ) {
                pHandleScan(cBarcodeScan.pFakeScan(editTextDocument.getText().toString()),true,false);
                cUserInterface.pHideKeyboard();
            }
            return true;
        });

        this.editTextBin.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_DONE || i == EditorInfo.IME_ACTION_GO ) {

                pHandleScan(cBarcodeScan.pFakeScan(editTextBin.getText().toString()),false,false);
                cUserInterface.pHideKeyboard();

            }
            return true;
        });


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

    private void mSetBin(){

        if (!cUser.currentUser.currentBranch.getMoveDefaultBinStr().isEmpty()) {
            this.editTextBin.setText(cUser.currentUser.currentBranch.getMoveDefaultBinStr());
        }
    }

    private void mHandleCreateOrder(String pvDocumentstr, String pvBinCodeStr, boolean pvCheckBarcodesBln ){

        cResult hulpResult;

        if (this.spinner.getVisibility() == View.VISIBLE && cUser.currentUser.currentStockOwner == null){
            cUserInterface.pDoExplodingScreen(cAppExtension.context.getString(R.string.message_select_stockowner), "", true, true );
            return;
        }


        hulpResult = this.mTryToCreateOrderRst(pvDocumentstr,pvBinCodeStr,pvCheckBarcodesBln);
        //Try to create the order
        if (!hulpResult.resultBln) {
            return;
        }

        // We created an assignment, but need to open a different assignment
        if (hulpResult.activityActionEnu == cWarehouseorder.ActivityActionEnu.Next && hulpResult.resultAction != null) {

            //all Moveorders
            if (!cMoveorder.pGetMoveOrdersViaWebserviceBln(true, "")) {
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

        }

        //Try to lock the order
        if (!this.mTryToLockOrderBln()) {
            cUserInterface.pDoExplodingScreen(cAppExtension.activity.getString(R.string.message_locking_order_failed),"",true,true);
            return;
        }

        //Delete the detail, so we can get them from the webservice
        cMoveorder.currentMoveOrder.pDeleteDetailsBln();

        hulpResult = this.mGetOrderDetailsRst();
        if (!hulpResult.resultBln) {
            this.mStepFailed(hulpResult.messagesStr());
            return;
        }

        FirebaseCrashlytics.getInstance().setCustomKey("Ordernumber", cMoveorder.currentMoveOrder.getOrderNumberStr());

        // If everything went well, then start Lines Activity
        cAppExtension.activity.runOnUiThread(this::mShowMoveLinesActivity);

    }

    private  cResult mTryToCreateOrderRst(String pvDocumentstr,  String pvBinCodeStr, boolean pvCheckBarcodesBln){

        cResult result = null;

     if (CreateMoveActivity.moveMainTypeEnu == cWarehouseorder.MoveMainTypeEnu.TAKEANDPLACE) {
         result =  cMoveorder.pCreateMoveOrderMVViaWebserviceRst(pvDocumentstr, pvBinCodeStr, pvCheckBarcodesBln);
         if (!result.resultBln) {
             this.editTextDocument.setText("");
             this.mStepFailed(result.messagesStr());
             return  result;
         }
     }

        if (CreateMoveActivity.moveMainTypeEnu == cWarehouseorder.MoveMainTypeEnu.PLACE) {
            result =  cMoveorder.pCreateMoveOrderMIViaWebserviceRst(pvDocumentstr, pvBinCodeStr, pvCheckBarcodesBln);
            if (!result.resultBln) {
                this.editTextDocument.setText("");
                this.mStepFailed(result.messagesStr());
                return  result;
            }
        }

        return result;

    }

    private boolean mTryToLockOrderBln(){

        cResult hulpResult  = cMoveorder.currentMoveOrder.pLockViaWebserviceRst();

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

        // Get all barcodes, if webservice error then stop
        if (!cMoveorder.currentMoveOrder.pGetLineBarcodesViaWebserviceBln(true)) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_line_barcodes_failed));
            return result;
        }

        if (cMoveorder.currentMoveOrder.getOrderTypeStr().equalsIgnoreCase("MI")) {
            if (!cMoveorder.currentMoveOrder.pMatchBarcodesAndLinesBln()) {
                result.resultBln = false;
                result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_matching_lines_and_barcodes_failed));
                return result;
            }
        }

        // Get all comments
        if (!cMoveorder.currentMoveOrder.pGetCommentsViaWebserviceBln(true)) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_comments_failed));
            return result;
        }

        return  result;
    }

    private void mStepFailed(String pvErrorMessageStr){

        if (cMoveorder.currentMoveOrder != null) {
            cUserInterface.pDoExplodingScreen(pvErrorMessageStr, cMoveorder.currentMoveOrder.getOrderNumberStr(), true, true );
        }
        else
        {
            cUserInterface.pDoExplodingScreen(pvErrorMessageStr,cAppExtension.activity.getString(R.string.message_couldnt_create_order) + " " +  this.editTextDocument.getText().toString(), true, true );
        }

        if (cMoveorder.currentMoveOrder != null) {
            cMoveorder.currentMoveOrder.pLockReleaseViaWebserviceBln();
        }

        cUserInterface.pCheckAndCloseOpenDialogs();
    }

    private void mShowMoveLinesActivity() {

        cUserInterface.pCheckAndCloseOpenDialogs();
        Intent intent;

        switch ( cMoveorder.currentMoveOrder.getOrderTypeStr()) {

            case "MV":
                intent = new Intent(cAppExtension.context, MoveLinesActivity.class);
                break;

            case"MI":

                if (cMoveorder.currentMoveOrder.isGeneratedBln()) {
                    intent = new Intent(cAppExtension.context, MoveorderLinesPlaceGeneratedActivity.class);
                }
                else
                {
                    intent = new Intent(cAppExtension.context, MoveLinesActivity.class);
                }
                break;


            default:
                throw new IllegalStateException("Unexpected value: " + cMoveorder.currentMoveOrder.getOrderTypeStr());
        }

        startActivity(intent);

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
