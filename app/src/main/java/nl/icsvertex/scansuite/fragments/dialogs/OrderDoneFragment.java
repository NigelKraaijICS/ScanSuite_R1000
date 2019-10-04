package nl.icsvertex.scansuite.fragments.dialogs;


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
import SSU_WHS.Picken.Pickorders.cPickorder;
import nl.icsvertex.scansuite.activities.pick.PickorderLinesActivity;
import nl.icsvertex.scansuite.R;
import nl.icsvertex.scansuite.activities.sort.SortorderLinesActivity;
import nl.icsvertex.scansuite.cAppExtension;


public class OrderDoneFragment extends DialogFragment implements iICSDefaultFragment {

    //Region Private Properties

    static private ConstraintLayout orderDoneContainer;
    private TextView textViewOrderDoneHeader;
    private TextView textViewOrderDoneText;
    static private EditText editTextCurrentLocation;
    static private Button closeOrderButton;
    static private Button cancelButton;

    //End Region private Properties


    //Region Constructor
    public OrderDoneFragment() {

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
    public void onViewCreated(View pvView, @Nullable Bundle pvSavedInstanceState) {
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
        cBarcodeScan.pRegisterBarcodeFragmentReceiver();
        super.onResume();
    }

    @Override
    public void mFragmentInitialize() {
        this.mFindViews();
        this.mSetViewModels();
        this.mFieldsInitialize();
        this.mSetListeners();

        cBarcodeScan.pRegisterBarcodeFragmentReceiver();

    }

    @Override
    public void mFindViews() {
        this.textViewOrderDoneHeader = getView().findViewById(R.id.textViewOrderDoneHeader);
        this.textViewOrderDoneText = getView().findViewById(R.id.textViewOrderDoneText);
        this.editTextCurrentLocation = getView().findViewById(R.id.editTextCurrentLocation);
        this.orderDoneContainer = getView().findViewById(R.id.orderDoneContainer);
        this.closeOrderButton = getView().findViewById(R.id.closeOrderButton);
        this.cancelButton = getView().findViewById(R.id.cancelButton);
    }

    @Override
    public void mSetViewModels() {

    }

    @Override
    public void mFieldsInitialize() {

        this.textViewOrderDoneHeader.setText(cAppExtension.context.getString(R.string.orderdone_header_default));
        this.textViewOrderDoneText.setText(cAppExtension.context.getString(R.string.orderdone_text_default));

        if (cPickorder.currentPickOrder.pQuantityHandledDbl() == 0 && cPickorder.currentPickOrder.getCurrentLocationStr().isEmpty() == false ) {
            editTextCurrentLocation.setVisibility(View.INVISIBLE);
        } else {
            InputFilter[] filterArray = new InputFilter[1];
            filterArray[0] = new InputFilter.LengthFilter(20);
            editTextCurrentLocation.setFilters(filterArray);
            cUserInterface.pShowKeyboard(editTextCurrentLocation);
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
        this.closeOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View pvView) {


                if (cAppExtension.activity instanceof PickorderLinesActivity) {
                    if (cPickorder.currentPickOrder.pQuantityHandledDbl() > 0 && cPickorder.currentPickOrder.getCurrentLocationStr().isEmpty() == true) {
                        //we need a location
                        if (editTextCurrentLocation.getText().toString().trim().isEmpty()) {
                            cUserInterface.pDoNope(editTextCurrentLocation, true, true);
                            return;
                        }
                    }
                    dismiss();
                    PickorderLinesActivity.pPickingDone(editTextCurrentLocation.getText().toString().trim());
                }

                if (cAppExtension.activity  instanceof SortorderLinesActivity) {
                    SortorderLinesActivity.pSortingDone();
                }
//
//                //todo: call static method
//                if (cAppExtension.activity  instanceof ShiporderLinesActivity) {
//                    ((ShiporderLinesActivity)cAppExtension.activity ).closeCurrentOrder();
//                }


            }
        });
    }

    public static void pHandleScan(String pvScannedBarcodeStr) {


        String barcodeWithoutPrefixStr;

        //No prefix
        if (!cRegex.hasPrefix(pvScannedBarcodeStr)) {
            editTextCurrentLocation.setText(pvScannedBarcodeStr);
            closeOrderButton.callOnClick();
            return;
        }

        Boolean foundBln = false;

        if (cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvScannedBarcodeStr,cBarcodeLayout.barcodeLayoutEnu.BIN) == true) {
            foundBln = true;
        }

        //has prefix, is BIN
        if (foundBln) {
            barcodeWithoutPrefixStr = cRegex.pStripRegexPrefixStr(pvScannedBarcodeStr);
            editTextCurrentLocation.setText(barcodeWithoutPrefixStr);
            closeOrderButton.callOnClick();
            return;
        }
        else {
            //has prefix, isn't branch
            cUserInterface.pDoNope(orderDoneContainer, true, true);
            return;
        }
    }



}
