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

import java.util.Objects;

import ICS.Interfaces.iICSDefaultFragment;
import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cRegex;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Basics.BarcodeLayouts.cBarcodeLayout;
import SSU_WHS.Basics.Users.cUser;
import nl.icsvertex.scansuite.Activities.Inventory.InventoryorderSelectActivity;
import nl.icsvertex.scansuite.R;


public class CreateInventoryFragment extends DialogFragment implements iICSDefaultFragment {

    //Region Private Properties

    private  ConstraintLayout createInventoryContainer;
    private  TextView textViewCreateInventoryHeader;
    private  TextView textViewCreateInventoryText;
    private  TextView textViewBranch;
    private  EditText editTextDocument;
    private  Button createInventoryButton;
    private  Button cancelButton;

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
        cBarcodeScan.pUnregisterBarcodeFragmentReceiver(this.getClass().getSimpleName());
    }

    @Override
    public void onResume() {
        super.onResume();
        cBarcodeScan.pRegisterBarcodeFragmentReceiver(this.getClass().getSimpleName());
        cUserInterface.pEnableScanner();
        Objects.requireNonNull(Objects.requireNonNull(getDialog()).getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void mFragmentInitialize() {

        this.mFindViews();
        this.mFieldsInitialize();
        this.mSetListeners();

        cBarcodeScan.pRegisterBarcodeFragmentReceiver(this.getClass().getSimpleName());

    }

    @Override
    public void mFindViews() {

        if (getView() != null) {
            this.textViewCreateInventoryHeader = getView().findViewById(R.id.textViewCreateInventoryHeader);
            this.textViewCreateInventoryText = getView().findViewById(R.id.textViewCreateInventoryText);
            this.textViewBranch = getView().findViewById(R.id.textViewBranch);
            this.editTextDocument = getView().findViewById(R.id.editTextDocument);
            this.createInventoryContainer = getView().findViewById(R.id.createInventoryContainer);
            this.createInventoryButton = getView().findViewById(R.id.closeButton);
            this.cancelButton = getView().findViewById(R.id.cancelButton);

        }
    }


    @Override
    public void mFieldsInitialize() {

        this.textViewCreateInventoryHeader.setText(cAppExtension.context.getString(R.string.createinventory_header_default));
        this.textViewCreateInventoryText.setText(cAppExtension.context.getString(R.string.createinventory_text_default));
        this.textViewBranch.setText(cUser.currentUser.currentBranch.getBranchNameStr());

        if (!CreateInventoryFragment.showDocumentBln) {
            this.editTextDocument.setVisibility(View.INVISIBLE);
            return;
        }

        InputFilter[] filterArray = new InputFilter[1];
        filterArray[0] = new InputFilter.LengthFilter(50);
        this.editTextDocument.setFilters(filterArray);
        cUserInterface.pShowKeyboard(editTextDocument);
    }

    @Override
    public void mSetListeners() {

        this.mSetCreateListener();
        this.mSetCancelListener();
        this.mSetEditorActionListener();
    }


    private void mSetCancelListener() {
        this.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View pvView) {
                cAppExtension.dialogFragment.dismiss();
            }
        });
    }

    private void mSetCreateListener() {
        this.createInventoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View pvView) {
                if (cAppExtension.activity  instanceof InventoryorderSelectActivity) {
                    InventoryorderSelectActivity inventoryorderSelectActivity = new InventoryorderSelectActivity();
                    inventoryorderSelectActivity.pCreateOrder(editTextDocument.getText().toString().trim());
                    cAppExtension.dialogFragment.dismiss();
                }
            }
        });
    }

    public void pHandleScan(cBarcodeScan pvBarcodeScan) {

        String barcodeWithoutPrefixStr;

        //No prefix
        if (!cRegex.pHasPrefix(pvBarcodeScan.getBarcodeOriginalStr())) {
            this.editTextDocument.setText(pvBarcodeScan.getBarcodeOriginalStr());
            this.createInventoryButton.performClick();
            return;
        }

        boolean foundBln = false;

        if (cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(), cBarcodeLayout.barcodeLayoutEnu.DOCUMENT)) {
            foundBln = true;
        }

        //has prefix, is DOCUMENT
        if (foundBln) {
            barcodeWithoutPrefixStr = cRegex.pStripRegexPrefixStr(pvBarcodeScan.getBarcodeOriginalStr());
            this.editTextDocument.setText(barcodeWithoutPrefixStr);
            this.createInventoryButton.performClick();
        }
        else {
            //has prefix, isn't DOCUMENT
            cUserInterface.pDoNope(createInventoryContainer, true, true);
        }
    }
    private void mSetEditorActionListener() {
        this.editTextDocument.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE || i == EditorInfo.IME_ACTION_GO ) {
                    createInventoryButton.callOnClick();
                }
                return true;
            }
        });
    }

}
