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
    private static TextView textViewCurrentLocationHeader;
    private static TextView textViewCurrentLocationText;

    private static CardView cardViewConnection;
    private static TextView textViewConnection;
    private static ImageButton imageButtonWifiReconnect;

    private static EditText editTextCurrentLocation;
    private static Button setLocationButton;
    private static Button cancelButton;
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
            CurrentLocationFragment.textViewCurrentLocationHeader = getView().findViewById(R.id.textViewCurrentLocationHeader);
            CurrentLocationFragment.textViewCurrentLocationText = getView().findViewById(R.id.textViewCurrentLocationText);
            CurrentLocationFragment.editTextCurrentLocation = getView().findViewById(R.id.editTextCurrentLocation);
            CurrentLocationFragment.cardViewConnection = getView().findViewById(R.id.cardViewConnection);
            CurrentLocationFragment.textViewConnection = getView().findViewById(R.id.textViewConnection);
            CurrentLocationFragment.imageButtonWifiReconnect = getView().findViewById(R.id.imageButtonWifiReconnect);
            CurrentLocationFragment.editTextCurrentLocation = getView().findViewById(R.id.editTextCurrentLocation);
            CurrentLocationFragment.setLocationButton = getView().findViewById(R.id.setLocationButton);
            CurrentLocationFragment.cancelButton = getView().findViewById(R.id.cancelButton);
        }
    }


    @Override
    public void mFieldsInitialize() {
        CurrentLocationFragment.textViewCurrentLocationHeader.setText(R.string.currentlocation_header_default);
        CurrentLocationFragment.textViewCurrentLocationText.setText(R.string.currentlocation_text_default);
        CurrentLocationFragment.pSetConnectionState();

        InputFilter[] filterArray = new InputFilter[1];
        filterArray[0] = new InputFilter.LengthFilter(20);
        CurrentLocationFragment.editTextCurrentLocation.setFilters(filterArray);
        cUserInterface.pShowKeyboard(CurrentLocationFragment.editTextCurrentLocation);
    }

    @Override
    public void mSetListeners() {
        this.mSetSetLocationListener();
        this.mSetCancelListener();
    }

    private void mSetCancelListener() {
        CurrentLocationFragment.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }
    private void mSetSetLocationListener() {
        CurrentLocationFragment.setLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (cAppExtension.activity instanceof  PickorderLinesActivity) {

                    if (CurrentLocationFragment.editTextCurrentLocation.getText().toString().trim().isEmpty()) {
                        cUserInterface.pDoNope(CurrentLocationFragment.editTextCurrentLocation, true, true);
                    }
                    else {
                        dismiss();
                        PickorderLinesActivity.pSetCurrentLocation(CurrentLocationFragment.editTextCurrentLocation.getText().toString());
                    }
                }
            }
        });
    }


    public static void pHandleScan(cBarcodeScan pvBarcodeScan) {


        //Has prefix, so check if this is a BIN
        if (cRegex.hasPrefix(pvBarcodeScan.getBarcodeOriginalStr())) {

            boolean foundBin = false;

            if (cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(),cBarcodeLayout.barcodeLayoutEnu.BIN)) {
                foundBin = true;
            }

            if (foundBin) {
                //has prefix, is bin
                CurrentLocationFragment.editTextCurrentLocation.setText(cRegex.pStripRegexPrefixStr(pvBarcodeScan.getBarcodeOriginalStr()));
                CurrentLocationFragment.setLocationButton.callOnClick();
                return;
            }
            else {
                //has prefix, isn't bin
                cUserInterface.pDoNope(CurrentLocationFragment.editTextCurrentLocation, true, true);
                return;
            }

        }

            //no prefix, fine
        CurrentLocationFragment.editTextCurrentLocation.setText(pvBarcodeScan.getBarcodeOriginalStr());
        CurrentLocationFragment.setLocationButton.callOnClick();
    }

    public static void pSetConnectionState() {
        if (cConnection.isInternetConnectedBln()) {
            CurrentLocationFragment.textViewConnection.setText(R.string.connected);
            CurrentLocationFragment.imageButtonWifiReconnect.setVisibility(View.INVISIBLE);
            CurrentLocationFragment.cardViewConnection.setVisibility(View.INVISIBLE);
            CurrentLocationFragment.cancelButton.setVisibility(View.VISIBLE);
            CurrentLocationFragment.setLocationButton.setVisibility(View.VISIBLE);
        }
        else {
            CurrentLocationFragment.textViewConnection.setText(R.string.not_connected);
            CurrentLocationFragment.imageButtonWifiReconnect.setVisibility(View.VISIBLE);
            CurrentLocationFragment.cardViewConnection.setVisibility(View.VISIBLE);
            CurrentLocationFragment.cancelButton.setVisibility(View.INVISIBLE);
            CurrentLocationFragment.setLocationButton.setVisibility(View.INVISIBLE);

        }
    }
}
