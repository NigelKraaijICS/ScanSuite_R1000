package nl.icsvertex.scansuite.Fragments.Inventory;


import android.os.Bundle;
import android.text.InputFilter;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;

import ICS.Interfaces.iICSDefaultFragment;
import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cRegex;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Basics.BarcodeLayouts.cBarcodeLayout;
import SSU_WHS.Basics.Users.cUser;
import nl.icsvertex.scansuite.Activities.Inventory.InventoryorderSelectActivity;
import nl.icsvertex.scansuite.Activities.Receive.CreateReceiveActivity;
import nl.icsvertex.scansuite.R;


public class CreateInventoryFragment extends DialogFragment implements iICSDefaultFragment {

    //Region Private Properties

    private static ConstraintLayout createInventoryContainer;
    private static TextView textViewCreateInventoryHeader;
    private static TextView textViewCreateInventoryText;
    private static TextView textViewBranch;
    private static EditText editTextDocument;
    private static Button createInventoryButton;
    private static Button cancelButton;

    private static Boolean showDocumentBln;

    //End Region private Properties

    //Region Constructor
    public CreateInventoryFragment(Boolean pvShowDocumentBln) {
        CreateInventoryFragment.showDocumentBln = pvShowDocumentBln;
    }
    //End Region Constructor

    @Override
    public View onCreateView(LayoutInflater pvInflater, ViewGroup pvContainer,
                             Bundle pvSavedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = pvInflater.inflate(R.layout.fragment_create_inventory, pvContainer, false);
        cAppExtension.dialogFragment = this;
        return rootview;
    }

    @Override
    public void onViewCreated(@NonNull View pvView, @Nullable Bundle pvSavedInstanceState) {
        this.mFragmentInitialize();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        cBarcodeScan.pUnregisterBarcodeFragmentReceiver();
    }
    @Override
    public void onResume() {
        super.onResume();
        cBarcodeScan.pRegisterBarcodeFragmentReceiver();
        cUserInterface.pEnableScanner();
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void mFragmentInitialize() {

        this.mFindViews();
        this.mFieldsInitialize();
        this.mSetListeners();

        cBarcodeScan.pRegisterBarcodeFragmentReceiver();

    }

    @Override
    public void mFindViews() {

        if (getView() != null) {
            CreateInventoryFragment.textViewCreateInventoryHeader = getView().findViewById(R.id.textViewCreateInventoryHeader);
            CreateInventoryFragment.textViewCreateInventoryText = getView().findViewById(R.id.textViewCreateInventoryText);
            CreateInventoryFragment.textViewBranch = getView().findViewById(R.id.textViewBranch);
            CreateInventoryFragment.editTextDocument = getView().findViewById(R.id.editTextDocument);
            CreateInventoryFragment.createInventoryContainer = getView().findViewById(R.id.createInventoryContainer);
            CreateInventoryFragment.createInventoryButton = getView().findViewById(R.id.createOrderButton);
            CreateInventoryFragment.cancelButton = getView().findViewById(R.id.cancelButton);

        }
    }


    @Override
    public void mFieldsInitialize() {

        CreateInventoryFragment.textViewCreateInventoryHeader.setText(cAppExtension.context.getString(R.string.createinventory_header_default));
        CreateInventoryFragment.textViewCreateInventoryText.setText(cAppExtension.context.getString(R.string.createinventory_text_default));
        CreateInventoryFragment.textViewBranch.setText(cUser.currentUser.currentBranch.getBranchNameStr());

        if (!CreateInventoryFragment.showDocumentBln) {
            CreateInventoryFragment.editTextDocument.setVisibility(View.INVISIBLE);
            return;
        }

        InputFilter[] filterArray = new InputFilter[1];
        filterArray[0] = new InputFilter.LengthFilter(50);
        CreateInventoryFragment.editTextDocument.setFilters(filterArray);
        cUserInterface.pShowKeyboard(editTextDocument);
    }

    @Override
    public void mSetListeners() {

        this.mSetCreateListener();
        this.mSetCancelListener();
        this.mSetEditorActionListener();
    }


    private void mSetCancelListener() {
        CreateInventoryFragment.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View pvView) {
                cAppExtension.dialogFragment.dismiss();
            }
        });
    }

    private void mSetCreateListener() {
        CreateInventoryFragment.createInventoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View pvView) {
                if (cAppExtension.activity  instanceof InventoryorderSelectActivity) {
                    InventoryorderSelectActivity.pCreateOrder(editTextDocument.getText().toString().trim());
                    cAppExtension.dialogFragment.dismiss();
                }
            }
        });
    }

    public static void pHandleScan(cBarcodeScan pvBarcodeScan) {

        String barcodeWithoutPrefixStr;

        //No prefix
        if (!cRegex.pHasPrefix(pvBarcodeScan.getBarcodeOriginalStr())) {
            CreateInventoryFragment.editTextDocument.setText(pvBarcodeScan.getBarcodeOriginalStr());
            CreateInventoryFragment.createInventoryButton.performClick();
            return;
        }

        boolean foundBln = false;

        if (cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(), cBarcodeLayout.barcodeLayoutEnu.DOCUMENT)) {
            foundBln = true;
        }

        //has prefix, is DOCUMENT
        if (foundBln) {
            barcodeWithoutPrefixStr = cRegex.pStripRegexPrefixStr(pvBarcodeScan.getBarcodeOriginalStr());
            CreateInventoryFragment.editTextDocument.setText(barcodeWithoutPrefixStr);
            CreateInventoryFragment.createInventoryButton.performClick();
        }
        else {
            //has prefix, isn't DOCUMENT
            cUserInterface.pDoNope(createInventoryContainer, true, true);
        }
    }
    private void mSetEditorActionListener() {
        CreateInventoryFragment.editTextDocument.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE || i == EditorInfo.IME_ACTION_GO ) {
                    CreateInventoryFragment.createInventoryButton.callOnClick();
                }
                return true;
            }
        });
    }

}
