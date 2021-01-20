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
import SSU_WHS.Basics.Users.cUser;
import SSU_WHS.Picken.Storement.cStorement;
import nl.icsvertex.scansuite.Activities.Store.StoreorderLinesActivity;
import nl.icsvertex.scansuite.R;

public class SetBinFragment extends DialogFragment implements iICSDefaultFragment {

    //Region Public Properties


    //End Region Public Properties

    //Region Private Properties
    private TextView textViewSetBinHeader;
    private TextView textViewSetBinText;
    private EditText editTextSetBin;
    private Button setBinButton;
    private Button cancelButton;
    //End Region Private Properties


    //Region Constructor
    public SetBinFragment() {

    }
    //End Region Constructor

    //Region Default Methods

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_set_bin, container, false);
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
        cBarcodeScan.pUnregisterBarcodeFragmentReceiver(this.getClass().getSimpleName());
    }

    @Override
    public void onResume() {
        super.onResume();
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = WindowManager.LayoutParams.WRAP_CONTENT;
        Objects.requireNonNull(Objects.requireNonNull(getDialog()).getWindow()).setLayout(width, height);

        cBarcodeScan.pRegisterBarcodeFragmentReceiver(this.getClass().getSimpleName());
        cUserInterface.pEnableScanner();
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
        this.textViewSetBinHeader = requireView().findViewById(R.id.textViewSetBinHeader );
        this.textViewSetBinText = getView().findViewById(R.id.textViewSetBinText);
        this.editTextSetBin = getView().findViewById(R.id.editTextSetBin);
        this.setBinButton = getView().findViewById(R.id.setBinButton);
        this.cancelButton = getView().findViewById(R.id.cancelButton);
    }


    @Override
    public void mFieldsInitialize() {

        String headerStr = cAppExtension.activity.getString(R.string.set_bin_header_default);

        if (cStorement.currentStorement != null) {
            if (cStorement.currentStorement.getProcessingSequenceStr().isEmpty()) {
                headerStr += " " + cStorement.currentStorement.getSourceNoStr();
            }
            else
            {
                headerStr += " " + cStorement.currentStorement.getProcessingSequenceStr();
            }
        }

        this.textViewSetBinHeader.setText(headerStr);

        InputFilter[] filterArray = new InputFilter[1];
        filterArray[0] = new InputFilter.LengthFilter(20);
        this.editTextSetBin.setFilters(filterArray);
        cUserInterface.pShowKeyboard(editTextSetBin);
        this.textViewSetBinText.setVisibility(View.GONE);

        if (!cUser.currentUser.currentBranch.getPickDefaultStorageBinStr().isEmpty()) {
            this.editTextSetBin.setText(cUser.currentUser.currentBranch.getPickDefaultStorageBinStr());
        }

    }

    @Override
    public void mSetListeners() {
        this.mSetSetBinListener();
        this.mSetCancelListener();
        this.mSetEditorActionListener();
    }

    private void mSetCancelListener() {
        this.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }
    private void mSetSetBinListener() {
        this.setBinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (editTextSetBin.getText().toString().trim().isEmpty()) {
                    cUserInterface.pDoNope(editTextSetBin, true, true);
                    return;
                }

                if (cAppExtension.activity instanceof StoreorderLinesActivity) {

                    StoreorderLinesActivity storeorderLinesActivity = (StoreorderLinesActivity)cAppExtension.activity;
                    storeorderLinesActivity.pStorementStored(editTextSetBin.getText().toString());
                    cAppExtension.dialogFragment.dismiss();
                }

            }
        });
    }
    private void mSetEditorActionListener() {
        this.editTextSetBin.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE || i == EditorInfo.IME_ACTION_GO ) {
                    setBinButton.callOnClick();
                }
                return true;
            }
        });
    }


    public void pHandleScan(cBarcodeScan pvBarcodeScan) {

        //Has prefix, so check if this is a BIN
        if (cRegex.pHasPrefix(pvBarcodeScan.getBarcodeOriginalStr())) {

            boolean foundBin = false;

            if (cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(), cBarcodeLayout.barcodeLayoutEnu.BIN)) {
                foundBin = true;
            }

            if (foundBin) {
                //has prefix, is bin
                this.editTextSetBin.setText(cRegex.pStripRegexPrefixStr(pvBarcodeScan.getBarcodeOriginalStr()));
                this.setBinButton.callOnClick();
            }
            else {
                //has prefix, isn't bin
                cUserInterface.pDoNope(this.editTextSetBin, true, true);
            }
            return;
        }

        //no prefix, fine
        this.editTextSetBin.setText(pvBarcodeScan.getBarcodeOriginalStr());
        this.setBinButton.callOnClick();
    }
}
