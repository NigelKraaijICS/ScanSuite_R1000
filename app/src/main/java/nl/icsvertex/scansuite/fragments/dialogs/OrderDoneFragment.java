package nl.icsvertex.scansuite.fragments.dialogs;


import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.DialogFragment;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import ICS.Interfaces.iICSDefaultFragment;
import SSU_WHS.Basics.BarcodeLayouts.cBarcodeLayout;
import SSU_WHS.Basics.BarcodeLayouts.cBarcodeLayoutEntity;
import SSU_WHS.Basics.BarcodeLayouts.cBarcodeLayoutViewModel;
import SSU_WHS.General.cPublicDefinitions;
import ICS.Utils.Scanning.cBarcodeScanDefinitions;
import ICS.Utils.cRegex;
import ICS.Utils.cUserInterface;
import nl.icsvertex.scansuite.activities.pick.PickorderLinesActivity;
import nl.icsvertex.scansuite.activities.ship.ShiporderLinesActivity;
import nl.icsvertex.scansuite.activities.sort.SortorderLinesActivity;
import nl.icsvertex.scansuite.R;

import static ICS.Utils.cUserInterface.mShowKeyboard;


public class OrderDoneFragment extends DialogFragment implements iICSDefaultFragment {
    String orderDoneHeader;
    String orderDoneText;

    ConstraintLayout orderDoneContainer;
    TextView textViewOrderDoneHeader;
    TextView textViewOrderDoneText;
    Boolean showCurrentLocation;
    EditText editTextCurrentLocation;
    Button closeOrderButton;
    Button cancelButton;

    IntentFilter barcodeFragmentIntentFilter;
    private BroadcastReceiver barcodeFragmentReceiver;
    cBarcodeLayoutViewModel barcodeLayoutViewModel;

    public OrderDoneFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order_done, container, false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        orderDoneHeader = args.getString(cPublicDefinitions.ORDERDONE_HEADER, getString(R.string.orderdone_header_default));
        orderDoneText = args.getString(cPublicDefinitions.ORDERDONE_TEXT, getString(R.string.orderdone_text_default));
        showCurrentLocation = args.getBoolean(cPublicDefinitions.ORDERDONE_SHOWCURRENTLOCATION, true);

        mFragmentInitialize();

        mBarcodeReceiver();
    }

    @Override
    public void onDestroy() {
        try {
            getActivity().unregisterReceiver(barcodeFragmentReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }
    @Override
    public void onPause() {
        try {
            getActivity().unregisterReceiver(barcodeFragmentReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onPause();
    }
    @Override
    public void onResume() {
        getActivity().registerReceiver(barcodeFragmentReceiver, barcodeFragmentIntentFilter);
        super.onResume();
    }

    @Override
    public void mFragmentInitialize() {
        mFindViews();
        mSetViewModels();
        mFieldsInitialize();
        mSetListeners();
    }

    @Override
    public void mFindViews() {
        textViewOrderDoneHeader = getView().findViewById(R.id.textViewOrderDoneHeader);
        textViewOrderDoneText = getView().findViewById(R.id.textViewOrderDoneText);
        editTextCurrentLocation = getView().findViewById(R.id.editTextCurrentLocation);
        orderDoneContainer = getView().findViewById(R.id.orderDoneContainer);
        closeOrderButton = getView().findViewById(R.id.closeOrderButton);
        cancelButton = getView().findViewById(R.id.cancelButton);
    }

    @Override
    public void mSetViewModels() {
        barcodeLayoutViewModel = ViewModelProviders.of(this).get(cBarcodeLayoutViewModel.class);
    }

    @Override
    public void mFieldsInitialize() {
        textViewOrderDoneHeader.setText(orderDoneHeader);
        textViewOrderDoneText.setText(orderDoneText);
        if (!showCurrentLocation) {
            editTextCurrentLocation.setVisibility(View.INVISIBLE);
        }
        else {
            InputFilter[] filterArray = new InputFilter[1];
            filterArray[0] = new InputFilter.LengthFilter(20);
            editTextCurrentLocation.setFilters(filterArray);
            mShowKeyboard(editTextCurrentLocation);
        }
    }
    @Override
    public void mSetListeners() {
        mSetCloseListener();
        mSetCancelListener();
    }

    private void mSetCancelListener() {
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    private void mSetCloseListener() {
        closeOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Activity activity = getActivity();
                if (activity instanceof PickorderLinesActivity) {
                    if (showCurrentLocation) {
                        //we need a location
                        if (editTextCurrentLocation.getText().toString().trim().isEmpty()) {
                            cUserInterface.doNope(editTextCurrentLocation, true, true);
                            return;
                        }
                    }
                    String currentLocation = editTextCurrentLocation.getText().toString().trim();
                    ((PickorderLinesActivity)activity).closeCurrentOrder(currentLocation);
                    dismiss();
                }
                if (activity instanceof SortorderLinesActivity) {
                    ((SortorderLinesActivity)activity).closeCurrentOrder();
                }
                if (activity instanceof ShiporderLinesActivity) {
                    ((ShiporderLinesActivity)activity).closeCurrentOrder();
                }
            }
        });
    }
    private void mBarcodeReceiver() {
        barcodeFragmentIntentFilter = new IntentFilter();
        for (String str : cBarcodeScanDefinitions.getBarcodeActions()) {
            barcodeFragmentIntentFilter.addAction(str);
        }
        for (String str : cBarcodeScanDefinitions.getBarcodeCategories()) {
            barcodeFragmentIntentFilter.addCategory(str);
        }

        barcodeFragmentReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String barcodeStr = ICS.Utils.Scanning.cBarcodeScan.p_GetBarcode(intent, true);
                if (barcodeStr == null) {
                    barcodeStr = "";
                }
                mHandleScan(barcodeStr);
            }
        };
        //don't forget to unregister on destroy.
        getActivity().registerReceiver(barcodeFragmentReceiver,barcodeFragmentIntentFilter);
    }

    private void mHandleScan(String barcode) {
        if (cRegex.hasPrefix(barcode)) {
            String barcodePrefixStr = cRegex.getPrefix(barcode);
            List<cBarcodeLayoutEntity> binLayouts = barcodeLayoutViewModel.getBarcodeLayoutsOfType(cBarcodeLayout.barcodeLayoutEnu.BIN.toString());
            Boolean foundBin = false;
            for (cBarcodeLayoutEntity layout : binLayouts) {
                if (cRegex.p_checkRegexBln(layout.getLayoutValue(), barcode)) {
                    foundBin = true;
                }
            }
            if (foundBin) {
                //has prefix, is bin
                editTextCurrentLocation.setText(cRegex.p_stripRegexPrefixStr(barcode));
                closeOrderButton.callOnClick();
            }
            else {
                //has prefix, isn't bin
                cUserInterface.doNope(editTextCurrentLocation, true, true);
            }
        }
        else {
            //no prefix, fine
            editTextCurrentLocation.setText(barcode);
            closeOrderButton.callOnClick();
        }

    }
}
