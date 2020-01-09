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

import ICS.Interfaces.iICSDefaultFragment;
import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cConnection;
import ICS.Utils.cRegex;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Basics.BarcodeLayouts.cBarcodeLayout;
import SSU_WHS.Picken.Pickorders.cPickorder;
import nl.icsvertex.scansuite.Activities.Intake.IntakeorderLinesActivity;
import nl.icsvertex.scansuite.Activities.Pick.PickorderLinesActivity;
import nl.icsvertex.scansuite.Activities.Ship.ShiporderLinesActivity;
import nl.icsvertex.scansuite.Activities.Sort.SortorderLinesActivity;
import nl.icsvertex.scansuite.R;


public class OrderDoneFragment extends DialogFragment implements iICSDefaultFragment {

    //Region Private Properties

    private static ConstraintLayout orderDoneContainer;
    private static TextView textViewOrderDoneHeader;
    private static TextView textViewOrderDoneText;

    private static CardView cardViewConnection;
    private static TextView textViewConnection;
    private static ImageButton imageButtonWifiReconnect;

    private static EditText editTextCurrentLocation;
    private static Button closeOrderButton;
    private static Button cancelButton;
    private static Boolean showCurrentLocationBln;

    //End Region private Properties


    //Region Constructor
    public OrderDoneFragment(Boolean pvShowCurrentLocationBln) {
        OrderDoneFragment.showCurrentLocationBln = pvShowCurrentLocationBln;
    }
    //End Region Constructor

    @Override
    public View onCreateView(LayoutInflater pvInflater, ViewGroup pvContainer,
                             Bundle pvSavedInstanceState) {

        // Inflate the layout for this fragment
        View rootview = pvInflater.inflate(R.layout.fragment_order_done, pvContainer, false);
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
            OrderDoneFragment.textViewOrderDoneHeader = getView().findViewById(R.id.textViewOrderDoneHeader);
            OrderDoneFragment.textViewOrderDoneText = getView().findViewById(R.id.textViewOrderDoneText);
            OrderDoneFragment.cardViewConnection = getView().findViewById(R.id.cardViewConnection);
            OrderDoneFragment.textViewConnection = getView().findViewById(R.id.textViewConnection);
            OrderDoneFragment.imageButtonWifiReconnect = getView().findViewById(R.id.imageButtonWifiReconnect);
            OrderDoneFragment.editTextCurrentLocation = getView().findViewById(R.id.editTextCurrentLocation);
            OrderDoneFragment.orderDoneContainer = getView().findViewById(R.id.orderDoneContainer);
            OrderDoneFragment.closeOrderButton = getView().findViewById(R.id.closeOrderButton);
            OrderDoneFragment.cancelButton = getView().findViewById(R.id.cancelButton);
        }

    }

    @Override
    public void mFieldsInitialize() {

        OrderDoneFragment.textViewOrderDoneHeader.setText(cAppExtension.context.getString(R.string.orderdone_header_default));
        OrderDoneFragment.textViewOrderDoneText.setText(cAppExtension.context.getString(R.string.orderdone_text_default));
        OrderDoneFragment.pSetConnectionState();

       if (!OrderDoneFragment.showCurrentLocationBln) {
           OrderDoneFragment.editTextCurrentLocation.setVisibility(View.INVISIBLE);
           return;
       }


        if (cPickorder.currentPickOrder.pQuantityHandledDbl() == 0 && !cPickorder.currentPickOrder.getCurrentLocationStr().isEmpty()) {
            OrderDoneFragment.editTextCurrentLocation.setVisibility(View.INVISIBLE);
        } else {
            InputFilter[] filterArray = new InputFilter[1];
            filterArray[0] = new InputFilter.LengthFilter(20);
            OrderDoneFragment.editTextCurrentLocation.setFilters(filterArray);
            cUserInterface.pShowKeyboard(OrderDoneFragment.editTextCurrentLocation);
        }
    }

    @Override
    public void mSetListeners() {
        this.mSetCloseListener();
        this.mSetCancelListener();
    }

    private void mSetCancelListener() {
        OrderDoneFragment.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View pvView) {
                dismiss();
            }
        });
    }

    private void mSetCloseListener() {
        OrderDoneFragment.closeOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View pvView) {


                if (cAppExtension.activity instanceof PickorderLinesActivity) {
                    if (cPickorder.currentPickOrder.pQuantityHandledDbl() > 0 && cPickorder.currentPickOrder.getCurrentLocationStr().isEmpty()) {
                        //we need a location
                        if (OrderDoneFragment.editTextCurrentLocation.getText().toString().trim().isEmpty() && OrderDoneFragment.showCurrentLocationBln ) {
                            cUserInterface.pDoNope(OrderDoneFragment.editTextCurrentLocation, true, true);
                            return;
                        }
                    }
                    dismiss();
                    PickorderLinesActivity.pPickingDone(OrderDoneFragment.editTextCurrentLocation.getText().toString().trim());
                }

                if (cAppExtension.activity  instanceof SortorderLinesActivity) {
                    SortorderLinesActivity.pStartOrderSelectActivity();
                }

                if (cAppExtension.activity  instanceof ShiporderLinesActivity) {
                    ShiporderLinesActivity.pShippingDone();
                }

                if (cAppExtension.activity instanceof IntakeorderLinesActivity) {
                    IntakeorderLinesActivity.pDone();
                }


            }
        });
    }

    public static void pHandleScan(cBarcodeScan pvBarcodeScan) {


        String barcodeWithoutPrefixStr;

        //No prefix
        if (!cRegex.hasPrefix(pvBarcodeScan.getBarcodeOriginalStr())) {
            OrderDoneFragment.editTextCurrentLocation.setText(pvBarcodeScan.getBarcodeOriginalStr());
            OrderDoneFragment.closeOrderButton.callOnClick();
            return;
        }

        boolean foundBln = false;

        if (cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(), cBarcodeLayout.barcodeLayoutEnu.BIN)) {
            foundBln = true;
        }

        //has prefix, is BIN
        if (foundBln) {
            barcodeWithoutPrefixStr = cRegex.pStripRegexPrefixStr(pvBarcodeScan.getBarcodeOriginalStr());
            OrderDoneFragment.editTextCurrentLocation.setText(barcodeWithoutPrefixStr);
            OrderDoneFragment.closeOrderButton.callOnClick();
        }
        else {
            //has prefix, isn't branch
            cUserInterface.pDoNope( OrderDoneFragment.orderDoneContainer, true, true);
        }
    }

    public static void pSetConnectionState() {
        if (cConnection.isInternetConnectedBln()) {
           OrderDoneFragment.textViewConnection.setText(R.string.connected);
           OrderDoneFragment.imageButtonWifiReconnect.setVisibility(View.INVISIBLE);
           OrderDoneFragment.cardViewConnection.setVisibility(View.INVISIBLE);
           OrderDoneFragment.closeOrderButton.setVisibility(View.VISIBLE);
           OrderDoneFragment.cancelButton.setVisibility(View.VISIBLE);
        }
        else {
            OrderDoneFragment.textViewConnection.setText(R.string.not_connected);
            OrderDoneFragment.imageButtonWifiReconnect.setVisibility(View.VISIBLE);
            OrderDoneFragment.cardViewConnection.setVisibility(View.VISIBLE);
            OrderDoneFragment.closeOrderButton.setVisibility(View.INVISIBLE);
            OrderDoneFragment.cancelButton.setVisibility(View.INVISIBLE);
        }
    }



}
