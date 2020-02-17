package nl.icsvertex.scansuite.Activities.Returns;


import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.KeyEvent;
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

import ICS.Interfaces.iICSDefaultActivity;
import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cRegex;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Basics.BarcodeLayouts.cBarcodeLayout;
import SSU_WHS.Basics.BranchBin.cBranchBin;
import SSU_WHS.Basics.BranchReason.cBranchReason;
import SSU_WHS.Basics.Users.cUser;
import SSU_WHS.General.cPublicDefinitions;
import nl.icsvertex.scansuite.Fragments.Dialogs.ReasonFragment;
import nl.icsvertex.scansuite.R;


public class CreateReturnActivity extends AppCompatActivity implements iICSDefaultActivity {

    //Region Public Properties
    public static final String VIEW_NAME_HEADER_IMAGE = "detail:header:imageStr";
    public static final String VIEW_NAME_HEADER_TEXT = "detail:header:text";
    //End Region Public Properties

    //Region Private Properties

    private static ConstraintLayout createReturnContainer;
    private static ImageView toolbarImage;
    private static TextView toolbarTitle;
    private static TextView textViewReturnReason;
    static private EditText editTextDocument;
    static private EditText editTextBin;
    static private Button createReturnButton;
    static private Button cancelButton;
    private static Switch switchMultipleDocuments;
    private static Switch switchReason;
    private static ImageView imageReason;
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
        cBarcodeScan.pUnregisterBarcodeReceiver();
    }

    @Override
    public void onResume() {
        super.onResume();
        cBarcodeScan.pRegisterBarcodeReceiver();
    }

    @Override
    public void onBackPressed() {
        this.mLeaveActivity();
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

        cBarcodeScan.pRegisterBarcodeReceiver();
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
        CreateReturnActivity.toolbarImage = findViewById(R.id.toolbarImage);
        CreateReturnActivity.toolbarTitle = findViewById(R.id.toolbarTitle);
        CreateReturnActivity.textViewReturnReason = findViewById(R.id.textViewCreateReturnReason);
        CreateReturnActivity.editTextDocument = findViewById(R.id.editTextDocument);
        CreateReturnActivity.editTextBin = findViewById(R.id.editTextBin);
        CreateReturnActivity.createReturnContainer = findViewById(R.id.createReturnContainer);
        CreateReturnActivity.createReturnButton = findViewById(R.id.createOrderButton);
        CreateReturnActivity.cancelButton = findViewById(R.id.cancelButton);
        CreateReturnActivity.imageReason = findViewById(R.id.imageButtonReason);
        CreateReturnActivity.switchMultipleDocuments = findViewById(R.id.multipleDocumentSwitch);
        CreateReturnActivity.switchReason = findViewById(R.id.selectReasonSwitch);
    }

    @Override
    public void mSetToolbar(String pvScreenTitle) {
        CreateReturnActivity.toolbarImage.setImageResource(R.drawable.ic_menu_return);
        CreateReturnActivity.toolbarTitle.setText(pvScreenTitle);
        CreateReturnActivity.toolbarTitle.setSelected(true);
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

        cBranchReason.currentBranchReason = null;

        InputFilter[] filterArray = new InputFilter[1];
        filterArray[0] = new InputFilter.LengthFilter(50);
        CreateReturnActivity.editTextDocument.setFilters(filterArray);
        CreateReturnActivity.imageReason.setVisibility(View.INVISIBLE);
        CreateReturnActivity.textViewReturnReason.setVisibility(View.INVISIBLE);

        if(!cUser.currentUser.currentBranch.pGetReasonBln(true)){
            cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.error_getting_return_reasons), null);
        }


        if (cUser.currentUser.currentBranch.isBinMandatoryBln()) {
            CreateReturnActivity.editTextBin.setVisibility(View.VISIBLE);
            mSetBin();
        }
        else
        {
            CreateReturnActivity.editTextBin.setVisibility(View.GONE);
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

    public static void pSetReason(){
        CreateReturnActivity.textViewReturnReason.setText(cBranchReason.currentBranchReason.getDescriptionStr());
    }

    public static void pHandleScan(cBarcodeScan pvBarcodeScan, boolean pvDocumentBln, boolean pvBinBln) {

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
            CreateReturnActivity.editTextDocument.setText(barcodeWithoutPrefixStr);
            CreateReturnActivity.editTextBin.requestFocus();
            //has prefix, is Bin
            return;

        }
        if (binBln) {
            barcodeWithoutPrefixStr = cRegex.pStripRegexPrefixStr(pvBarcodeScan.getBarcodeOriginalStr());

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

            CreateReturnActivity.editTextBin.setText(barcodeWithoutPrefixStr);
            return;


        }
        if (reasonBln){
            CreateReturnActivity.switchReason.setChecked(true);
            CreateReturnActivity.mHandleReasonSwitch(true);
            barcodeWithoutPrefixStr = cRegex.pStripRegexPrefixStr(pvBarcodeScan.getBarcodeOriginalStr());
            cBranchReason branchReason = cUser.currentUser.currentBranch.pGetReasonByName(barcodeWithoutPrefixStr);
            if (branchReason != null) {
                cBranchReason.currentBranchReason = branchReason;
                CreateReturnActivity.textViewReturnReason.setText(cBranchReason.currentBranchReason.getDescriptionStr());
            }
        }

        else {
            //has prefix, isn't DOCUMENT
            cUserInterface.pDoNope(createReturnContainer, true, true);
        }
    }

    public static void pHandleFragmentDismissed(){
        cBarcodeScan.pRegisterBarcodeReceiver();
    }

    //End Region Public Methods

    //Region Private Methods

    private void mSetImageListener() {
        CreateReturnActivity.imageReason.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View pvView) {
                mShowAddReasonFragment();
            }
        });
    }

    private void mSetCancelListener() {
        CreateReturnActivity.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View pvView) {
                CreateReturnActivity.mStartOrderSelectActivity();
            }
        });
    }

    private void mSetReasonSwitchListener() {
        CreateReturnActivity.switchReason.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View pvView) {
                if (CreateReturnActivity.switchReason.isChecked()) {
                    CreateReturnActivity.switchReason.setText(cAppExtension.activity.getString(R.string.message_clear_reason));
                    CreateReturnActivity.mHandleReasonSwitch(false);
                } else {
                    cBranchReason.currentBranchReason = null;
                    CreateReturnActivity.textViewReturnReason.setText("");
                    CreateReturnActivity.imageReason.setVisibility(View.INVISIBLE);
                    CreateReturnActivity.textViewReturnReason.setVisibility(View.INVISIBLE);
                    CreateReturnActivity.switchReason.setText(cAppExtension.activity.getString(R.string.select_reason));
                }
            }
        });
    }

    private static void mStartOrderSelectActivity() {
        Intent intent = new Intent(cAppExtension.context, ReturnorderSelectActivity.class);
        cAppExtension.activity.startActivity(intent);
    }

    private void mSetCreateListener() {
        CreateReturnActivity.createReturnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View pvView) {
                if (CreateReturnActivity.editTextDocument.getText().toString().isEmpty()){
                    cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.message_scan_return_document),null);
                    return;
                }
                if (CreateReturnActivity.editTextBin.getText().toString().isEmpty() && cUser.currentUser.currentBranch.isBinMandatoryBln()){
                    cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.message_scan_return_bin),null);
                    return;
                }
                ReturnorderSelectActivity.pCreateOrder(editTextDocument.getText().toString().trim(), switchMultipleDocuments.isChecked(), editTextBin.getText().toString().trim());
            }
        });
    }

    private static void mShowAddReasonFragment(){

        ReasonFragment reasonFragment = new ReasonFragment();
        reasonFragment.setCancelable(true);
        reasonFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.ADDREASON_TAG);


    }

    private void mLeaveActivity(){

        Intent intent = new Intent(cAppExtension.context, ReturnorderSelectActivity.class);
        cAppExtension.activity.startActivity(intent);
        cAppExtension.activity.finish();

    }

    private static void mHandleReasonSwitch(boolean pvScanBln){
        CreateReturnActivity.imageReason.setVisibility(View.VISIBLE);
        CreateReturnActivity.textViewReturnReason.setVisibility(View.VISIBLE);
        if (!pvScanBln) {
            CreateReturnActivity.mShowAddReasonFragment();
        }
    }

    private void mSetEditorActionListener() {
        CreateReturnActivity.editTextDocument.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE || i == EditorInfo.IME_ACTION_GO ) {

                    CreateReturnActivity.pHandleScan(cBarcodeScan.pFakeScan(CreateReturnActivity.editTextDocument.getText().toString()),true,false);
                    cUserInterface.pHideKeyboard();

                }
                return true;
            }
        });

        CreateReturnActivity.editTextBin.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE || i == EditorInfo.IME_ACTION_GO ) {

                    CreateReturnActivity.pHandleScan(cBarcodeScan.pFakeScan(CreateReturnActivity.editTextBin.getText().toString()),false,true);
                    cUserInterface.pHideKeyboard();

                }
                return true;
            }
        });

    }

    private void mSetBin(){


        if (!cUser.currentUser.currentBranch.getReturnDefaultBinStr().isEmpty()) {
            CreateReturnActivity.editTextBin.setText(cUser.currentUser.currentBranch.getReturnDefaultBinStr());
        }



    }
    //End Region Private Methods


}
