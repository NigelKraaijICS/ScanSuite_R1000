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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;

import java.util.Objects;

import ICS.Interfaces.iICSDefaultFragment;
import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cConnection;
import ICS.Utils.cRegex;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Basics.BarcodeLayouts.cBarcodeLayout;
import SSU_WHS.Picken.Pickorders.cPickorder;
import nl.icsvertex.scansuite.Activities.FinishShip.FinishShipLinesActivity;
import nl.icsvertex.scansuite.Activities.Intake.IntakeorderLinesActivity;
import nl.icsvertex.scansuite.Activities.Pick.PickorderLinesActivity;
import nl.icsvertex.scansuite.Activities.Ship.ShiporderLinesActivity;
import nl.icsvertex.scansuite.Activities.Sort.SortorderLinesActivity;
import nl.icsvertex.scansuite.R;


public class StepDoneFragment extends DialogFragment implements iICSDefaultFragment {

    //Region Private Properties

    private ConstraintLayout stepDoneContainer;
    private TextView textViewStepDoneHeader;
    private TextView textViewStepDoneText;

    private CardView cardViewConnection;
    private TextView textViewConnection;
    private ImageButton imageButtonWifiReconnect;

    private EditText editTextCurrentLocation;
    private Button closeButton;
    private Button cancelButton;

    private String headerStr;
    private String messageStr;
    private  Boolean showCurrentLocationBln;

    //End Region private Properties


    //Region Constructor
    public StepDoneFragment(String pvHeaderStr, String pvMessageStr, Boolean pvShowCurrentLocationBln) {
        this.headerStr = pvHeaderStr;
        this.messageStr = pvMessageStr;
        this.showCurrentLocationBln = pvShowCurrentLocationBln;
    }
    //End Region Constructor

    @Override
    public View onCreateView(LayoutInflater pvInflater, ViewGroup pvContainer,
                             Bundle pvSavedInstanceState) {

        // Inflate the layout for this fragment
        View rootview = pvInflater.inflate(R.layout.fragment_step_done, pvContainer, false);
        cAppExtension.dialogFragment = this;
        return rootview;

    }
    @Override
    public void onViewCreated(@NonNull View pvView, @Nullable Bundle pvSavedInstanceState) {
        this.mFragmentInitialize();

        if (!cConnection.isInternetConnectedBln()) {
            cConnection.pReconnectWifi();
        }

    }

    @Override
    public void onDestroy() {
        try {
            cBarcodeScan.pUnregisterBarcodeFragmentReceiver(this.getClass().getSimpleName());
            cConnection.pUnregisterWifiChangedFragmentReceiver();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    @Override
    public void onPause() {
        try {
            cBarcodeScan.pUnregisterBarcodeFragmentReceiver(this.getClass().getSimpleName());
            cConnection.pUnregisterWifiChangedFragmentReceiver();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();

        cBarcodeScan.pRegisterBarcodeFragmentReceiver(this.getClass().getSimpleName());
        cConnection.pRegisterWifiChangedFragmentReceiver();
        cUserInterface.pEnableScanner();

        if (getDialog() != null) {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

    }

    @Override
    public void mFragmentInitialize() {
        this.mFindViews();
        this.mFieldsInitialize();
        this.mSetListeners();

        cBarcodeScan.pRegisterBarcodeFragmentReceiver(this.getClass().getSimpleName());
        cConnection.pRegisterWifiChangedFragmentReceiver();

    }

    @Override
    public void mFindViews() {

        if (getView() != null) {

            this.stepDoneContainer = getView().findViewById(R.id.stepDoneContainer);

            this.editTextCurrentLocation = getView().findViewById(R.id.editTextCurrentLocation);

            this.textViewStepDoneHeader = getView().findViewById(R.id.textViewStepDoneHeader);
            this.textViewStepDoneText = getView().findViewById(R.id.textViewStepDoneText);

            this.cardViewConnection = getView().findViewById(R.id.cardViewConnection);
            this.textViewConnection = getView().findViewById(R.id.textViewConnection);
            this.imageButtonWifiReconnect = getView().findViewById(R.id.imageButtonWifiReconnect);

            this.closeButton = getView().findViewById(R.id.closeButton);
            this.cancelButton = getView().findViewById(R.id.cancelButton);
        }

    }

    @Override
    public void mFieldsInitialize() {

        this.textViewStepDoneHeader.setText(this.headerStr);
        this.textViewStepDoneText.setText(this.messageStr);
        this.pSetConnectionState();

       if (!this.showCurrentLocationBln) {
           this.editTextCurrentLocation.setVisibility(View.GONE);
           return;
       }


        if (cPickorder.currentPickOrder.pQuantityHandledDbl() == 0 && !cPickorder.currentPickOrder.getCurrentLocationStr().isEmpty()) {
            this.editTextCurrentLocation.setVisibility(View.GONE);
        } else {
            InputFilter[] filterArray = new InputFilter[1];
            filterArray[0] = new InputFilter.LengthFilter(20);
            this.editTextCurrentLocation.setFilters(filterArray);
            cUserInterface.pShowKeyboard(this.editTextCurrentLocation);
        }
    }

    @Override
    public void mSetListeners() {
        this.mSetCloseListener();
        this.mSetCancelListener();
    }

    private void mSetCancelListener() {
        this.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View pvView) {
                dismiss();
            }
        });
    }

    private void mSetCloseListener() {
        this.closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View pvView) {


                if (cAppExtension.activity instanceof PickorderLinesActivity) {

                    PickorderLinesActivity pickorderLinesActivity = (PickorderLinesActivity)cAppExtension.activity;

                    if (cPickorder.currentPickOrder.pQuantityHandledDbl() > 0 && cPickorder.currentPickOrder.getCurrentLocationStr().isEmpty()) {
                        //we need a location
                        if (editTextCurrentLocation.getText().toString().trim().isEmpty() && showCurrentLocationBln ) {
                            cUserInterface.pDoNope(editTextCurrentLocation, true, true);
                            return;
                        }
                    }
                    dismiss();
                    pickorderLinesActivity.pPickingDone(editTextCurrentLocation.getText().toString().trim());
                }

                if (cAppExtension.activity  instanceof SortorderLinesActivity) {
                    SortorderLinesActivity sortorderLinesActivity = (SortorderLinesActivity)cAppExtension.activity;
                    sortorderLinesActivity.pStartOrderSelectActivity();
                }

                if (cAppExtension.activity  instanceof ShiporderLinesActivity) {
                    ShiporderLinesActivity  shiporderLinesActivity = (ShiporderLinesActivity)cAppExtension.activity;
                    shiporderLinesActivity.pShippingDone();
                }

                if (cAppExtension.activity  instanceof FinishShipLinesActivity) {
                    FinishShipLinesActivity  finishShipLinesActivity = (FinishShipLinesActivity)cAppExtension.activity;
                    finishShipLinesActivity.pShippingDone();
                }

                if (cAppExtension.activity instanceof IntakeorderLinesActivity) {
                    IntakeorderLinesActivity intakeorderLinesActivity = (IntakeorderLinesActivity)cAppExtension.activity;
                    intakeorderLinesActivity.pDone();
                }


            }
        });
    }

    public void pHandleScan(cBarcodeScan pvBarcodeScan) {


        String barcodeWithoutPrefixStr;

        //No prefix
        if (!cRegex.pHasPrefix(pvBarcodeScan.getBarcodeOriginalStr())) {
            this.editTextCurrentLocation.setText(pvBarcodeScan.getBarcodeOriginalStr());
            this.closeButton.callOnClick();
            return;
        }

        boolean foundBln = false;

        if (cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(), cBarcodeLayout.barcodeLayoutEnu.BIN)) {
            foundBln = true;
        }

        //has prefix, is BIN
        if (foundBln) {
            barcodeWithoutPrefixStr = cRegex.pStripRegexPrefixStr(pvBarcodeScan.getBarcodeOriginalStr());
            this.editTextCurrentLocation.setText(barcodeWithoutPrefixStr);
            this.closeButton.callOnClick();
        }
        else {
            //has prefix, isn't branch
            cUserInterface.pDoNope(this.stepDoneContainer, true, true);
        }
    }

    public void pSetConnectionState() {
        if (cConnection.isInternetConnectedBln()) {
           this.textViewConnection.setText(R.string.connected);
           this.imageButtonWifiReconnect.setVisibility(View.GONE);
           this.cardViewConnection.setVisibility(View.GONE);
           this.closeButton.setVisibility(View.VISIBLE);
           this.cancelButton.setVisibility(View.VISIBLE);
        }
        else {
            this.textViewConnection.setText(R.string.not_connected);
            this.imageButtonWifiReconnect.setVisibility(View.VISIBLE);
            this.cardViewConnection.setVisibility(View.VISIBLE);
            this.closeButton.setVisibility(View.GONE);
            this.cancelButton.setVisibility(View.GONE);
        }
    }

}
