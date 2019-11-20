package nl.icsvertex.scansuite.Fragments.Dialogs;


import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import ICS.Interfaces.iICSDefaultFragment;
import SSU_WHS.Intake.IntakeorderBarcodes.cIntakeorderBarcode;
import SSU_WHS.Inventory.InventoryorderBarcodes.cInventoryorderBarcode;
import SSU_WHS.Picken.PickorderBarcodes.cPickorderBarcode;
import ICS.cAppExtension;
import nl.icsvertex.scansuite.Activities.Intake.IntakeOrderIntakeActivity;
import nl.icsvertex.scansuite.Activities.Inventory.InventoryorderBinActivity;
import nl.icsvertex.scansuite.Activities.Pick.PickorderPickActivity;
import nl.icsvertex.scansuite.R;


public class BarcodeFragment extends DialogFragment implements iICSDefaultFragment {

    //Region Public Properties


    //End region

    //Region Private Properties

    private RecyclerView barcodeRecyclerview;
    private Button buttonClose;

    //End Region Private Properties


    //Region Constructor
    public BarcodeFragment() {

    }
    //End Region Constructor

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_barcode_picker, container);
        return rootview;
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
    }

    @Override
    public void mFindViews() {
        this.barcodeRecyclerview = getView().findViewById(R.id.barcodeRecyclerview);
        this.buttonClose = getView().findViewById(R.id.buttonClose);
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
        this.buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               dismiss();
            }
        });
    }

    private void mFillRecyclerView() {
        this.barcodeRecyclerview.setHasFixedSize(false);


        if (cAppExtension.activity instanceof PickorderPickActivity) {
            this.barcodeRecyclerview.setAdapter(cPickorderBarcode.getPickorderBarcodeAdapter());
        }

        if (cAppExtension.activity instanceof IntakeOrderIntakeActivity) {
            this.barcodeRecyclerview.setAdapter(cIntakeorderBarcode.getIntakeorderBarcodeAdapter());
        }

        if (cAppExtension.activity instanceof InventoryorderBinActivity) {
            this.barcodeRecyclerview.setAdapter(cInventoryorderBarcode.getInventoryorderBarcodeAdapter());
        }

        this.barcodeRecyclerview.setLayoutManager(new LinearLayoutManager(cAppExtension.context));
    }
}
