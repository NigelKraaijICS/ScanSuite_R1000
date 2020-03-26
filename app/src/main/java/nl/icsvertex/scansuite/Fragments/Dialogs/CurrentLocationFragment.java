package nl.icsvertex.scansuite.Fragments.Dialogs;


import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;

import ICS.Interfaces.iICSDefaultFragment;
import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cConnection;
import ICS.Utils.cRegex;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Basics.BarcodeLayouts.cBarcodeLayout;
import nl.icsvertex.scansuite.Activities.Pick.PickorderLinesActivity;
import nl.icsvertex.scansuite.R;


public class CurrentLocationFragment extends DialogFragment implements iICSDefaultFragment {

    //Region Public Properties


    //End Region Public Properties

    //Region Private Properties
    private TextView textViewCurrentLocationHeader;
    private TextView textViewCurrentLocationText;

    private CardView cardViewConnection;
    private TextView textViewConnection;
    private ImageButton imageButtonWifiReconnect;

    private EditText editTextCurrentLocation;
    private Button setLocationButton;
    private Button cancelButton;
    //End Region Private Properties


    //Region Constructor
    public CurrentLocationFragment() {
        // Required empty public constructor
    }
    //End Region Constructor

    //Region Default Methods

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_current_location, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        cAppExtension.dialogFragment  = this;
        this.mFragmentInitialize();

        if (!cConnection.isInternetConnectedBln()) {
            cConnection.pReconnectWifi();
        }


    }

    @Override
    public void onDestroy() {
        try {
            cBarcodeScan.pUnregisterBarcodeFragmentReceiver();
            cConnection.pUnregisterWifiChangedFragmentReceiver();

        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    @Override
    public void onPause() {
        try {
            cBarcodeScan.pUnregisterBarcodeFragmentReceiver();
            cConnection.pUnregisterWifiChangedFragmentReceiver();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        cBarcodeScan.pRegisterBarcodeFragmentReceiver();
        cConnection.pRegisterWifiChangedFragmentReceiver();
        cUserInterface.pEnableScanner();
    }

    @Override
    public void mFragmentInitialize() {
        this.mFindViews();
        this.mFieldsInitialize();
        this.mSetListeners();

        cBarcodeScan.pRegisterBarcodeFragmentReceiver();
        cConnection.pRegisterWifiChangedFragmentReceiver();
    }

    @Override
    public void mFindViews() {

        if (getView() != null) {
            this.textViewCurrentLocationHeader = getView().findViewById(R.id.textViewCurrentLocationHeader);
            this.textViewCurrentLocationText = getView().findViewById(R.id.textViewCurrentLocationText);
            this.editTextCurrentLocation = getView().findViewById(R.id.editTextCurrentLocation);
            this.cardViewConnection = getView().findViewById(R.id.cardViewConnection);
            this.textViewConnection = getView().findViewById(R.id.textViewConnection);
            this.imageButtonWifiReconnect = getView().findViewById(R.id.imageButtonWifiReconnect);
            this.editTextCurrentLocation = getView().findViewById(R.id.editTextCurrentLocation);
            this.setLocationButton = getView().findViewById(R.id.setLocationButton);
            this.cancelButton = getView().findViewById(R.id.cancelButton);
        }
    }


    @Override
    public void mFieldsInitialize() {
        this.textViewCurrentLocationHeader.setText(R.string.currentlocation_header_default);
        this.textViewCurrentLocationText.setText(R.string.currentlocation_text_default);
        this.pSetConnectionState();

        InputFilter[] filterArray = new InputFilter[1];
        filterArray[0] = new InputFilter.LengthFilter(20);
        this.editTextCurrentLocation.setFilters(filterArray);
        cUserInterface.pShowKeyboard(this.editTextCurrentLocation);
    }

    @Override
    public void mSetListeners() {
        this.mSetSetLocationListener();
        this.mSetCancelListener();
    }

    private void mSetCancelListener() {
        this.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }
    private void mSetSetLocationListener() {
        this.setLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (cAppExtension.activity instanceof  PickorderLinesActivity) {

                    PickorderLinesActivity pickorderLinesActivity = (PickorderLinesActivity)cAppExtension.activity;

                    if (editTextCurrentLocation.getText().toString().trim().isEmpty()) {
                        cUserInterface.pDoNope(editTextCurrentLocation, true, true);
                    }
                    else {
                        dismiss();
                        pickorderLinesActivity.pSetCurrentLocation(editTextCurrentLocation.getText().toString());
                    }
                }
            }
        });
    }


    public  void pHandleScan(cBarcodeScan pvBarcodeScan) {


        //Has prefix, so check if this is a BIN
        if (cRegex.pHasPrefix(pvBarcodeScan.getBarcodeOriginalStr())) {

            boolean foundBin = false;

            if (cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(),cBarcodeLayout.barcodeLayoutEnu.BIN)) {
                foundBin = true;
            }

            if (foundBin) {
                //has prefix, is bin
                this.editTextCurrentLocation.setText(cRegex.pStripRegexPrefixStr(pvBarcodeScan.getBarcodeOriginalStr()));
                this.setLocationButton.callOnClick();
                return;
            }
            else {
                //has prefix, isn't bin
                cUserInterface.pDoNope(this.editTextCurrentLocation, true, true);
                return;
            }

        }

            //no prefix, fine
        this.editTextCurrentLocation.setText(pvBarcodeScan.getBarcodeOriginalStr());
        this.setLocationButton.callOnClick();
    }

    public void pSetConnectionState() {
        if (cConnection.isInternetConnectedBln()) {
            this.textViewConnection.setText(R.string.connected);
            this.imageButtonWifiReconnect.setVisibility(View.INVISIBLE);
            this.cardViewConnection.setVisibility(View.INVISIBLE);
            this.cancelButton.setVisibility(View.VISIBLE);
            this.setLocationButton.setVisibility(View.VISIBLE);
        }
        else {
            this.textViewConnection.setText(R.string.not_connected);
            this.imageButtonWifiReconnect.setVisibility(View.VISIBLE);
            this.cardViewConnection.setVisibility(View.VISIBLE);
            this.cancelButton.setVisibility(View.INVISIBLE);
            this.setLocationButton.setVisibility(View.INVISIBLE);

        }
    }
}
