package nl.icsvertex.scansuite.Fragments.Dialogs;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ICS.Interfaces.iICSDefaultFragment;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Intake.IntakeorderBarcodes.cIntakeorderBarcode;
import SSU_WHS.Inventory.InventoryorderBarcodes.cInventoryorderBarcode;
import SSU_WHS.Picken.PickorderBarcodes.cPickorderBarcode;
import nl.icsvertex.scansuite.Activities.Intake.IntakeOrderIntakeActivity;
import nl.icsvertex.scansuite.Activities.Inventory.InventoryorderBinActivity;
import nl.icsvertex.scansuite.Activities.Pick.PickorderPickActivity;
import nl.icsvertex.scansuite.Activities.Receive.ReceiveOrderReceiveActivity;
import nl.icsvertex.scansuite.R;


public class BarcodeFragment extends DialogFragment implements iICSDefaultFragment {

    //Region Public Properties


    //End region

    //Region Private Properties

    private static RecyclerView barcodeRecyclerview;
    private static Button buttonClose;

    //End Region Private Properties


    //Region Constructor
    public BarcodeFragment() {

    }
    //End Region Constructor

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_barcode_picker, container);
    }

    @Override
    public void onViewCreated(@NonNull View pvView, @Nullable Bundle pvSavedInstanceState) {
        this.mFragmentInitialize();
    }

    @Override
    public void mFragmentInitialize() {
        this.mFindViews();
        this.mFieldsInitialize();
        this.mSetListeners();
        cUserInterface.pEnableScanner();
    }

    @Override
    public void mFindViews() {

        if (getView() != null) {
            BarcodeFragment.barcodeRecyclerview = getView().findViewById(R.id.barcodeRecyclerview);
            BarcodeFragment.buttonClose = getView().findViewById(R.id.buttonClose);
        }
    }

    @Override
    public void mFieldsInitialize() {
        this.mFillRecyclerView();
    }

    @Override
    public void mSetListeners() {
        this.mSetCloseListener();
    }

    private void mSetCloseListener() {
        BarcodeFragment.buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               dismiss();
            }
        });
    }

    private void mFillRecyclerView() {
        BarcodeFragment.barcodeRecyclerview.setHasFixedSize(false);


        if (cAppExtension.activity instanceof PickorderPickActivity) {
            BarcodeFragment.barcodeRecyclerview.setAdapter(cPickorderBarcode.getPickorderBarcodeAdapter());
        }

        if (cAppExtension.activity instanceof IntakeOrderIntakeActivity) {
            BarcodeFragment.barcodeRecyclerview.setAdapter(cIntakeorderBarcode.getIntakeorderBarcodeAdapter());
        }

        if (cAppExtension.activity instanceof ReceiveOrderReceiveActivity) {
            BarcodeFragment.barcodeRecyclerview.setAdapter(cIntakeorderBarcode.getIntakeorderBarcodeAdapter());
        }

        if (cAppExtension.activity instanceof InventoryorderBinActivity) {
            BarcodeFragment.barcodeRecyclerview.setAdapter(cInventoryorderBarcode.getInventoryorderBarcodeAdapter());
        }

        BarcodeFragment.barcodeRecyclerview.setLayoutManager(new LinearLayoutManager(cAppExtension.context));
    }
}
