package nl.icsvertex.scansuite.Fragments.Dialogs;


import android.os.Bundle;
import android.text.InputFilter;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Objects;

import ICS.Interfaces.iICSDefaultFragment;
import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cRegex;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Basics.BarcodeLayouts.cBarcodeLayout;
import nl.icsvertex.scansuite.Activities.Inventory.InventoryorderBinsActivity;
import nl.icsvertex.scansuite.R;

public class AddBinFragment extends DialogFragment implements iICSDefaultFragment {

    //Region Public Properties


    //End Region Public Properties

    //Region Private Properties
    private static TextView textViewAddBinHeader;
    private static TextView textViewAddBinText;
    private static EditText editTextAddBin;
    private static Button addBinButton;
    private static Button cancelButton;
    //End Region Private Properties


    //Region Constructor
    public AddBinFragment() {

    }
    //End Region Constructor

    //Region Default Methods

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_bin, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        cAppExtension.dialogFragment  = this;
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
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = WindowManager.LayoutParams.WRAP_CONTENT;
        Objects.requireNonNull(Objects.requireNonNull(getDialog()).getWindow()).setLayout(width, height);

        cBarcodeScan.pRegisterBarcodeFragmentReceiver();
        cUserInterface.pEnableScanner();
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
        AddBinFragment.textViewAddBinHeader = Objects.requireNonNull(getView()).findViewById(R.id.textViewAddBinHeader );
        AddBinFragment.textViewAddBinText = getView().findViewById(R.id.textViewAddBinText);
        AddBinFragment.editTextAddBin = getView().findViewById(R.id.editTextAddBin);
        AddBinFragment.addBinButton = getView().findViewById(R.id.addBinButton);
        AddBinFragment.cancelButton = getView().findViewById(R.id.cancelButton);
    }


    @Override
    public void mFieldsInitialize() {
        AddBinFragment.textViewAddBinHeader.setText(R.string.add_bin_header_default);

        InputFilter[] filterArray = new InputFilter[1];
        filterArray[0] = new InputFilter.LengthFilter(20);
        AddBinFragment.editTextAddBin.setFilters(filterArray);
        cUserInterface.pShowKeyboard(editTextAddBin);
        AddBinFragment.textViewAddBinText.setVisibility(View.GONE);
    }

    @Override
    public void mSetListeners() {
        this.mSetAddBinListener();
        this.mSetCancelListener();
        this.mSetEditorActionListener();
    }

    private void mSetCancelListener() {
       AddBinFragment.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InventoryorderBinsActivity.pHandleAddBinFragmentDismissed();
                cAppExtension.dialogFragment.dismiss();
            }
        });
    }
    private void mSetAddBinListener() {
        AddBinFragment.addBinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (AddBinFragment.editTextAddBin.getText().toString().trim().isEmpty()) {
                    cUserInterface.pDoNope(AddBinFragment.editTextAddBin, true, true);
                    return;
                }

                if (cAppExtension.activity instanceof InventoryorderBinsActivity) {
                    InventoryorderBinsActivity.pHandleScan(cBarcodeScan.pFakeScan(AddBinFragment.editTextAddBin.getText().toString()));
                    InventoryorderBinsActivity.pHandleAddBinFragmentDismissed();
                    cAppExtension.dialogFragment.dismiss();
                }

            }
        });
    }
    private void mSetEditorActionListener() {
        AddBinFragment.editTextAddBin.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE || i == EditorInfo.IME_ACTION_GO ) {
                    addBinButton.callOnClick();
                }
                return true;
            }
        });
    }


    public static void pHandleScan(cBarcodeScan pvBarcodeScan) {

        //Has prefix, so check if this is a BIN
        if (cRegex.pHasPrefix(pvBarcodeScan.getBarcodeOriginalStr())) {

            boolean foundBin = false;

            if (cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(), cBarcodeLayout.barcodeLayoutEnu.BIN)) {
                foundBin = true;
            }

            if (foundBin) {
                //has prefix, is bin
                AddBinFragment.editTextAddBin.setText(cRegex.pStripRegexPrefixStr(pvBarcodeScan.getBarcodeOriginalStr()));
                AddBinFragment.addBinButton.callOnClick();
                return;
            }
            else {
                //has prefix, isn't bin
                cUserInterface.pDoNope(AddBinFragment.editTextAddBin, true, true);
                return;
            }
        }

        //no prefix, fine
        AddBinFragment.editTextAddBin.setText(pvBarcodeScan.getBarcodeOriginalStr());
        AddBinFragment.addBinButton.callOnClick();
    }
}
