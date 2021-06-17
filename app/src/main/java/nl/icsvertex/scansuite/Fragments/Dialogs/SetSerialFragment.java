package nl.icsvertex.scansuite.Fragments.Dialogs;


import android.os.Bundle;
import android.text.InputFilter;
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
import ICS.Utils.cSharedPreferences;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import nl.icsvertex.scansuite.Activities.General.MainDefaultActivity;
import nl.icsvertex.scansuite.R;

public class SetSerialFragment extends DialogFragment implements iICSDefaultFragment {

    //Region Public Properties


    //End Region Public Properties

    //Region Private Properties
    private TextView textViewSetSerialHeader;
    private TextView textViewSetSerialText;
    private EditText editTextSetSerial;
    private Button setSerialButton;
    private Button cancelButton;
    //End Region Private Properties


    //Region Constructor
    public SetSerialFragment() {

    }
    //End Region Constructor

    //Region Default Methods

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_set_serial, container, false);
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

    }

    @Override
    public void mFindViews() {

        if (getView() != null) {
            this.textViewSetSerialHeader = requireView().findViewById(R.id.textViewSetSerialHeader );
            this.textViewSetSerialText = getView().findViewById(R.id.textViewSetSerialText);
            this.editTextSetSerial = getView().findViewById(R.id.editTextSetSerial);
            this.setSerialButton = getView().findViewById(R.id.setSerialButton);
            this.cancelButton = getView().findViewById(R.id.cancelButton);
        }
    }


    @Override
    public void mFieldsInitialize() {
        this.textViewSetSerialHeader.setText(R.string.set_serial_header_default);

        InputFilter[] filterArray = new InputFilter[1];
        filterArray[0] = new InputFilter.LengthFilter(20);
        this.editTextSetSerial.setFilters(filterArray);
        cUserInterface.pShowKeyboard(editTextSetSerial);
        this.textViewSetSerialText.setVisibility(View.GONE);
    }

    @Override
    public void mSetListeners() {
        this.mSetSetSerialListener();
        this.mSetCancelListener();
        this.mSetEditorActionListener();
    }

    private void mSetCancelListener() {
        this.cancelButton.setOnClickListener(view -> dismiss());
    }

    private void mSetSetSerialListener() {
        this.setSerialButton.setOnClickListener(view -> {

            if (editTextSetSerial.getText().toString().trim().isEmpty()) {
                cUserInterface.pDoNope(editTextSetSerial, true, true);
                return;
            }
            cSharedPreferences.setSerialNumerStr(editTextSetSerial.getText().toString());

            if (cAppExtension.activity instanceof MainDefaultActivity) {
                MainDefaultActivity mainDefaultActivity = (MainDefaultActivity)cAppExtension.activity;
                mainDefaultActivity.mActivityInitialize();
            }

            dismiss();
        });
    }

    private void mSetEditorActionListener() {
        this.editTextSetSerial.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_DONE || i == EditorInfo.IME_ACTION_GO ) {
                setSerialButton.callOnClick();
            }
            return true;
        });
    }

    public void pHandleScan(cBarcodeScan pvBarcodeScan) {
        this.editTextSetSerial.setText(cRegex.pStripRegexPrefixStr(pvBarcodeScan.getBarcodeOriginalStr()));
        this.setSerialButton.callOnClick();

    }

}
