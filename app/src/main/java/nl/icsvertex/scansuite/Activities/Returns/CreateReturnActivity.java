package nl.icsvertex.scansuite.Activities.Returns;


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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.ViewCompat;

import java.util.List;

import ICS.Interfaces.iICSDefaultActivity;
import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cRegex;
import ICS.Utils.cResult;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Basics.BarcodeLayouts.cBarcodeLayout;
import SSU_WHS.Basics.BranchBin.cBranchBin;
import SSU_WHS.Basics.BranchReason.cBranchReason;
import SSU_WHS.Basics.Settings.cSetting;
import SSU_WHS.Basics.Users.cUser;
import SSU_WHS.General.Comments.cComment;
import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.General.cPublicDefinitions;
import SSU_WHS.Return.ReturnOrder.cReturnorder;
import nl.icsvertex.scansuite.Fragments.Dialogs.CommentFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.ReasonFragment;
import nl.icsvertex.scansuite.R;


public class CreateReturnActivity extends AppCompatActivity implements iICSDefaultActivity {

    //Region Public Properties

    //End Region Public Properties

    //Region Private Properties

    private  ConstraintLayout createReturnContainer;

    private  ImageView toolbarImage;
    private  TextView toolbarTitle;

    private TextView textViewReturnReason;
    private EditText editTextDocument;
    private EditText editTextBin;

    private Button createReturnButton;
    private Button cancelButton;

    private Switch switchMultipleDocuments;
    private Switch switchReason;
    private ImageView imageReason;
    //End Region private Properties

    //Region Constructor
    public CreateReturnActivity() {
    }
    //End Region Constructor


    //Region Default Methods

    @Override
    protected void onCreate(Bundle pvSavedInstanceState) {
        super.onCreate(pvSavedInstanceState);
        setContentView(R.layout.activity_create_return_order);
        this.mActivityInitialize();

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
        cBarcodeScan.pRegisterBarcodeReceiver(this.getClass().getSimpleName());
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    @Override
    public void onBackPressed() {
        this.mLeaveActivity();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem pvMenuItem) {

        if (pvMenuItem.getItemId() == android.R.id.home) {
            mLeaveActivity();
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

        this.mSetToolbar(getResources().getString(R.string.create_return));

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
        this.textViewReturnReason = findViewById(R.id.textViewCreateReturnReason);
        this.editTextDocument = findViewById(R.id.editTextDocument);
        this.editTextBin = findViewById(R.id.editTextBin);
        this.createReturnContainer = findViewById(R.id.createReturnContainer);
        this.createReturnButton = findViewById(R.id.createButton);
        this.cancelButton = findViewById(R.id.cancelButton);
        this.imageReason = findViewById(R.id.imageButtonReason);
        this.switchMultipleDocuments = findViewById(R.id.multipleDocumentSwitch);
        this.switchReason = findViewById(R.id.selectReasonSwitch);
    }

    @Override
    public void mSetToolbar(String pvScreenTitle) {
        this.toolbarImage.setImageResource(R.drawable.ic_menu_return);
        this.toolbarTitle.setText(pvScreenTitle);
        this.toolbarTitle.setSelected(true);
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

        cBranchReason.currentBranchReason = null;

        InputFilter[] filterArray = new InputFilter[1];
        filterArray[0] = new InputFilter.LengthFilter(50);
        this.editTextDocument.setFilters(filterArray);
        this.imageReason.setVisibility(View.INVISIBLE);
        this.textViewReturnReason.setVisibility(View.INVISIBLE);

        if(!cUser.currentUser.currentBranch.pGetReasonBln(true)){
            cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.error_getting_return_reasons), null);
        }


        if (cUser.currentUser.currentBranch.isBinMandatoryBln()) {
            this.editTextBin.setVisibility(View.VISIBLE);
            mSetBin();
        }
        else
        {
            this.editTextBin.setVisibility(View.GONE);
        }


        if (cSetting.RETOUR_NEW_WORKFLOWS().contains(cWarehouseorder.WorkflowEnu.RVR.toString())) {
            this.switchMultipleDocuments.setVisibility(View.GONE);
        }

    }

    @Override
    public void mSetListeners() {
        this.mSetImageListener();
        this.mSetCreateListener();
        this.mSetCancelListener();
        this.mSetReasonSwitchListener();
        this.mSetEditorActionListener();
    }

    @Override
    public void mInitScreen() {

    }

    //End Region iICSDefaultActivity defaults

    //Region Public Methods

    public  void pSetReason(){
        this.textViewReturnReason.setText(cBranchReason.currentBranchReason.getDescriptionStr());
    }

    public  void pHandleScan(cBarcodeScan pvBarcodeScan, boolean pvDocumentBln, boolean pvBinBln) {

        String barcodeWithoutPrefixStr;
        cBranchBin branchBin;
        boolean documentBln = false;
        boolean binBln = false;
        boolean reasonBln = false;

        cUserInterface.pCheckAndCloseOpenDialogs();


        if (cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(), cBarcodeLayout.barcodeLayoutEnu.DOCUMENT)) {
            documentBln = true;
        }

        if (cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(), cBarcodeLayout.barcodeLayoutEnu.BIN)) {
            binBln = true;
        }

        if (cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(), cBarcodeLayout.barcodeLayoutEnu.REASON)){
            reasonBln = true;
        }

        if (pvDocumentBln) {
            documentBln = true;
        }

        if (pvBinBln) {
            binBln = true;
        }

        //has prefix, is DOCUMENT
        if (documentBln) {
            barcodeWithoutPrefixStr = cRegex.pStripRegexPrefixStr(pvBarcodeScan.getBarcodeOriginalStr());
            this.editTextDocument.setText(barcodeWithoutPrefixStr);
            this.editTextBin.requestFocus();
            //has prefix, is Bin
            return;

        }
        if (binBln) {
            barcodeWithoutPrefixStr = cRegex.pStripRegexPrefixStr(pvBarcodeScan.getBarcodeOriginalStr());

            if (!cSetting.RETOUR_ORDER_BIN_NO_CHECK() ) {
                branchBin = cUser.currentUser.currentBranch.pGetBinByCode(barcodeWithoutPrefixStr);

                if (branchBin == null) {
                    cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.message_unknown_bin), null);
                    return;
                }

                if (!branchBin.getBinCodeStr().equalsIgnoreCase(cUser.currentUser.currentBranch.getReturnDefaultBinStr())) {
                    if (!branchBin.isUseForReturnSalesBln()){
                        cUserInterface.pDoExplodingScreen(cAppExtension.context.getString(R.string.message_bin_not_allowed_for_return),"",true,true);
                        return;
                    }
                }
            }

            this.editTextBin.setText(barcodeWithoutPrefixStr);
            return;


        }
        if (reasonBln){
            this.switchReason.setChecked(true);
            this.mHandleReasonSwitch(true);
            barcodeWithoutPrefixStr = cRegex.pStripRegexPrefixStr(pvBarcodeScan.getBarcodeOriginalStr());
            cBranchReason branchReason = cUser.currentUser.currentBranch.pGetReasonByName(barcodeWithoutPrefixStr);
            if (branchReason != null) {
                cBranchReason.currentBranchReason = branchReason;
                this.textViewReturnReason.setText(cBranchReason.currentBranchReason.getDescriptionStr());
            }
        }

        else {


            barcodeWithoutPrefixStr = cRegex.pStripRegexPrefixStr(pvBarcodeScan.getBarcodeOriginalStr());

            if (this.editTextDocument.hasFocus()) {
                this.pHandleScan(cBarcodeScan.pFakeScan(barcodeWithoutPrefixStr), true, false);
                return;
            }

            if (this.editTextBin.hasFocus()) {
                this.pHandleScan(cBarcodeScan.pFakeScan(barcodeWithoutPrefixStr), false, true);
                return;
            }


            //has prefix, isn't DOCUMENT
            cUserInterface.pDoNope(createReturnContainer, true, true);
        }
    }

    public void pHandleFragmentDismissed(){
        cBarcodeScan.pRegisterBarcodeReceiver(this.getClass().getSimpleName());
    }

    //End Region Public Methods

    //Region Private Methods

    private void mSetImageListener() {
        this.imageReason.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View pvView) {
                mShowAddReasonFragment();
            }
        });
    }

    private void mSetCancelListener() {
        this.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View pvView) {
                mStartOrderSelectActivity();
            }
        });
    }

    private void mSetReasonSwitchListener() {
        this.switchReason.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View pvView) {
                if (switchReason.isChecked()) {
                    switchReason.setText(cAppExtension.activity.getString(R.string.message_clear_reason));
                    mHandleReasonSwitch(false);
                } else {
                    cBranchReason.currentBranchReason = null;
                    textViewReturnReason.setText("");
                    imageReason.setVisibility(View.INVISIBLE);
                    textViewReturnReason.setVisibility(View.INVISIBLE);
                    switchReason.setText(cAppExtension.activity.getString(R.string.select_reason));
                }
            }
        });
    }

    private  void mStartOrderSelectActivity() {
        Intent intent = new Intent(cAppExtension.context, ReturnorderSelectActivity.class);
        ReturnorderSelectActivity.startedViaMenuBln = false;
        cAppExtension.activity.startActivity(intent);
    }

    private void mSetCreateListener() {
        this.createReturnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View pvView) {

                if (editTextDocument.getText().toString().isEmpty()){
                    cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.message_scan_return_document),null);
                    return;
                }
                if (editTextBin.getText().toString().isEmpty() && cUser.currentUser.currentBranch.isBinMandatoryBln()){
                    cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.message_scan_return_bin),null);
                    return;
                }

                ReturnorderSelectActivity.startedViaMenuBln = false;
                mCreateOrder(editTextDocument.getText().toString().trim(), switchMultipleDocuments.isChecked(), editTextBin.getText().toString().trim());

            }
        });
    }

    private void mShowAddReasonFragment(){

        ReasonFragment reasonFragment = new ReasonFragment();
        reasonFragment.setCancelable(true);
        reasonFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.ADDREASON_TAG);


    }

    private void mLeaveActivity(){
        ReturnorderSelectActivity.startedViaMenuBln = false;
        Intent intent = new Intent(cAppExtension.context, ReturnorderSelectActivity.class);
        cAppExtension.activity.startActivity(intent);
        cAppExtension.activity.finish();

    }

    private  void mHandleReasonSwitch(boolean pvScanBln){
        this.imageReason.setVisibility(View.VISIBLE);
        this.textViewReturnReason.setVisibility(View.VISIBLE);
        if (!pvScanBln) {
            this.mShowAddReasonFragment();
        }
    }

    private void mSetEditorActionListener() {
        this.editTextDocument.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE || i == EditorInfo.IME_ACTION_GO ) {

                    pHandleScan(cBarcodeScan.pFakeScan(editTextDocument.getText().toString()),true,false);
                    cUserInterface.pHideKeyboard();

                }
                return true;
            }
        });

        this.editTextBin.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE || i == EditorInfo.IME_ACTION_GO ) {

                    pHandleScan(cBarcodeScan.pFakeScan(editTextBin.getText().toString()),false,true);
                    cUserInterface.pHideKeyboard();

                }
                return true;
            }
        });
    }

    private void mSetBin(){

        if (!cUser.currentUser.currentBranch.getReturnDefaultBinStr().isEmpty()) {
            this.editTextBin.setText(cUser.currentUser.currentBranch.getReturnDefaultBinStr());
        }

    }

    private  void mCreateOrder(final String pvDocumentStr, final Boolean pvMultipleDocumentsBln, final String pvBincodeStr){

        // Show that we are getting data
        cUserInterface.pShowGettingData();

        new Thread(new Runnable() {
            public void run() {
                mHandleCreateOrder(pvDocumentStr, pvMultipleDocumentsBln, pvBincodeStr);
            }
        }).start();
    }

    //End Region Public Methods

    // Region Private Methods

    private  void mHandleCreateOrder(String pvDocumentstr, Boolean pvMultipleDocumentsBln, String pvBincodeStr){

        cResult hulpResult;

        //Try to create the order
        hulpResult = this.mTryToCreateOrderRst(pvDocumentstr, pvMultipleDocumentsBln, pvBincodeStr);
        if (!hulpResult.resultBln) {
            return;
        }

        // We created an assignment, but need to open a different assignment
        if (hulpResult.activityActionEnu == cWarehouseorder.ActivityActionEnu.Next && hulpResult.resultAction != null) {

            //all Intakeorders
            if (!cReturnorder.pGetReturnOrdersViaWebserviceBln(true, "")) {
                cUserInterface.pDoExplodingScreen(cAppExtension.context.getString(R.string.error_get_returnorders_failed), "", true, true );
                return;
            }

            if (cReturnorder.allReturnordersObl == null || cReturnorder.allReturnordersObl.size() == 0) {
                cUserInterface.pDoExplodingScreen(cAppExtension.context.getString(R.string.error_get_next_activity_failed), "", true, true );
                return;
            }


            for (cReturnorder returnorder : cReturnorder.allReturnordersObl) {

                if (returnorder.getOrderNumberStr().equalsIgnoreCase(hulpResult.resultAction.nextAssignmentStr)) {
                    cReturnorder.currentReturnOrder = returnorder;
                    break;
                }
            }

            if (cReturnorder.currentReturnOrder == null) {
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
        cReturnorder.currentReturnOrder.pDeleteDetailsBln();

        hulpResult = cReturnorder.currentReturnOrder.pGetOrderDetailsRst();
        if (!hulpResult.resultBln) {
            this.mStepFailed(hulpResult.messagesStr());
            return;
        }

        cAppExtension.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // If everything went well, then start Lines Activity
                mShowReturnorderDocumentsActivity();
            }
        });

    }

    private  cResult mTryToCreateOrderRst(String pvDocumentstr, Boolean pvMultipleDocumentsBln, String pvBincodeStr){

        cResult result =  cReturnorder.pCreateReturnOrderViaWebserviceRst(pvDocumentstr, pvMultipleDocumentsBln, pvBincodeStr);
        if (!result.resultBln) {
            this.editTextDocument.setText("");
            mStepFailed(result.messagesStr());
            return  result;
        }

        return result;
    }

    private  void mStepFailed(String pvErrorMessageStr){
        cUserInterface.pDoExplodingScreen(pvErrorMessageStr, "", true, true );
        cUserInterface.pCheckAndCloseOpenDialogs();
        cReturnorder.currentReturnOrder = null;
    }

    private boolean mTryToLockOrderBln(){

        cResult hulpResult;
        hulpResult = cReturnorder.currentReturnOrder.pLockViaWebserviceRst(cWarehouseorder.StepCodeEnu.Retour, cWarehouseorder.WorkflowReturnStepEnu.Return);

        //Everything was fine, so we are done
        if (hulpResult.resultBln) {
            return true;
        }

        //Something went wrong, but no further actions are needed, so ony show reason of failure
        if (hulpResult.activityActionEnu == cWarehouseorder.ActivityActionEnu.Unknown ) {
            this.mStepFailed(hulpResult.messagesStr());
            return  false;
        }

        //Something went wrong, the order has been deleted, so show comments and refresh
        if ( hulpResult.activityActionEnu == cWarehouseorder.ActivityActionEnu.Delete ||
                hulpResult.activityActionEnu == cWarehouseorder.ActivityActionEnu.NoStart ) {


            //If we got any comments, show them
            if (cReturnorder.currentReturnOrder.pFeedbackCommentObl() != null && cReturnorder.currentReturnOrder.pFeedbackCommentObl().size() > 0 ) {
                //Process comments from webresult
                this.mShowCommentsFragment(cReturnorder.currentReturnOrder.pFeedbackCommentObl(), hulpResult.messagesStr());
            }

            return  false;
        }


        return true;

    }



    private void mShowCommentsFragment(List<cComment> pvDataObl, String pvTitleStr) {

        cUserInterface.pCheckAndCloseOpenDialogs();

        Bundle bundle = new Bundle();
        bundle.putString(cPublicDefinitions.KEY_COMMENTHEADER, pvTitleStr);

        CommentFragment commentFragment = new CommentFragment(pvDataObl);
        commentFragment.setArguments(bundle);

        commentFragment.show(cAppExtension.fragmentManager , cPublicDefinitions.COMMENTFRAGMENT_TAG);
        cUserInterface.pPlaySound(R.raw.message, 0);
    }

    private void mShowReturnorderDocumentsActivity() {

        cUserInterface.pCheckAndCloseOpenDialogs();

        Intent intent = new Intent(cAppExtension.context, ReturnorderDocumentsActivity.class);

            cAppExtension.activity.startActivity(intent);
            cAppExtension.activity.finish();
    }

    //End Region Private Methods


}
