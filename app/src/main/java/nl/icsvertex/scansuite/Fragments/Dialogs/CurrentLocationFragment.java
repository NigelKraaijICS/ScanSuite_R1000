package nl.icsvertex.scansuite.Fragments.Dialogs;


import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import ICS.Interfaces.iICSDefaultFragment;
import ICS.Utils.Scanning.cBarcodeScan;
import SSU_WHS.Basics.BarcodeLayouts.cBarcodeLayout;
import ICS.Utils.cRegex;
import ICS.Utils.cUserInterface;
import nl.icsvertex.scansuite.Activities.Pick.PickorderLinesActivity;
import nl.icsvertex.scansuite.R;
import ICS.cAppExtension;


public class CurrentLocationFragment extends DialogFragment implements iICSDefaultFragment {

    //Region Public Properties


    //End Region Public Properties

    //Region Private Properties
    private ConstraintLayout currentLocationContainer;
    private TextView textViewCurrentLocationHeader;
    private TextView textViewCurrentLocationText;

    private  static EditText editTextCurrentLocation;
    private static Button setLocationButton;
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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        cAppExtension.dialogFragment  = this;
        this.mFragmentInitialize();
    }

    @Override
    public void onDestroy() {
        try {
            cBarcodeScan.pUnregisterBarcodeFragmentReceiver();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    @Override
    public void onPause() {
        try {
            cBarcodeScan.pUnregisterBarcodeFragmentReceiver();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onPause();
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
        this.textViewCurrentLocationHeader = getView().findViewById(R.id.textViewCurrentLocationHeader);
        this.textViewCurrentLocationText = getView().findViewById(R.id.textViewCurrentLocationText);
        this.editTextCurrentLocation = getView().findViewById(R.id.editTextCurrentLocation);
        this.currentLocationContainer = getView().findViewById(R.id.currentLocationContainer);
        this.setLocationButton = getView().findViewById(R.id.setLocationButton);
        this.cancelButton = getView().findViewById(R.id.cancelButton);
    }


    @Override
    public void mFieldsInitialize() {
        this.textViewCurrentLocationHeader.setText(R.string.currentlocation_header_default);
        this.textViewCurrentLocationText.setText(R.string.currentlocation_text_default);

        InputFilter[] filterArray = new InputFilter[1];
        filterArray[0] = new InputFilter.LengthFilter(20);
        this.editTextCurrentLocation.setFilters(filterArray);
        cUserInterface.pShowKeyboard(editTextCurrentLocation);
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

                    if (editTextCurrentLocation.getText().toString().trim().isEmpty()) {
                        cUserInterface.pDoNope(editTextCurrentLocation, true, true);
                    }
                    else {
                        dismiss();
                        PickorderLinesActivity.pSetCurrentLocation(editTextCurrentLocation.getText().toString());
                    }
                }
            }
        });
    }


    public static void pHandleScan(cBarcodeScan pvBarcodeScan) {


        //Has prefix, so check if this is a BIN
        if (cRegex.hasPrefix(pvBarcodeScan.getBarcodeOriginalStr())) {

            Boolean foundBin = false;

            if (cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(),cBarcodeLayout.barcodeLayoutEnu.BIN)) {
                foundBin = true;
            }

            if (foundBin) {
                //has prefix, is bin
                editTextCurrentLocation.setText(cRegex.pStripRegexPrefixStr(pvBarcodeScan.getBarcodeOriginalStr()));
                setLocationButton.callOnClick();
                return;
            }
            else {
                //has prefix, isn't bin
                cUserInterface.pDoNope(editTextCurrentLocation, true, true);
                return;
            }

        }

            //no prefix, fine
            editTextCurrentLocation.setText(pvBarcodeScan.getBarcodeOriginalStr());
            setLocationButton.callOnClick();
    }
}
