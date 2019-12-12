package nl.icsvertex.scansuite.Fragments.Dialogs;


import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;

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
    private ConstraintLayout addBinContainer;
    private TextView textViewAddBinHeader;
    private TextView textViewAddBinText;
    private  static EditText editTextAddBin;
    private static Button addBinButton;
    private Button cancelButton;
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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
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
        this.textViewAddBinHeader = getView().findViewById(R.id.textViewAddBinHeader );
        this.textViewAddBinText = getView().findViewById(R.id.textViewAddBinText);
        this.editTextAddBin = getView().findViewById(R.id.editTextAddBin);
        this.addBinContainer = getView().findViewById(R.id.addBinContainer);
        this.addBinButton = getView().findViewById(R.id.addBinButton);
        this.cancelButton = getView().findViewById(R.id.cancelButton);
    }


    @Override
    public void mFieldsInitialize() {
        this.textViewAddBinHeader.setText(R.string.add_bin_header_default);

        InputFilter[] filterArray = new InputFilter[1];
        filterArray[0] = new InputFilter.LengthFilter(20);
        this.editTextAddBin.setFilters(filterArray);
        cUserInterface.pShowKeyboard(editTextAddBin);
        this.textViewAddBinText.setVisibility(View.GONE);
    }

    @Override
    public void mSetListeners() {
        this.mSetAddBinListener();
        this.mSetCancelListener();
    }

    private void mSetCancelListener() {
       this.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cAppExtension.dialogFragment.dismiss();
                InventoryorderBinsActivity.pHandleAddBinFragmentDismissed();
            }
        });
    }
    private void mSetAddBinListener() {
        this.addBinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cAppExtension.activity instanceof InventoryorderBinsActivity) {

                    if (AddBinFragment.editTextAddBin.getText().toString().trim().isEmpty()) {
                        cUserInterface.pDoNope(AddBinFragment.editTextAddBin, true, true);
                        return;
                    }

                    cAppExtension.dialogFragment.dismiss();

                    InventoryorderBinsActivity.pHandleScan(cBarcodeScan.pFakeScan(AddBinFragment.editTextAddBin.getText().toString()));

                }
            }
        });
    }


    public static void pHandleScan(cBarcodeScan pvBarcodeScan) {

        //Has prefix, so check if this is a BIN
        if (cRegex.hasPrefix(pvBarcodeScan.getBarcodeOriginalStr())) {

            Boolean foundBin = false;

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
