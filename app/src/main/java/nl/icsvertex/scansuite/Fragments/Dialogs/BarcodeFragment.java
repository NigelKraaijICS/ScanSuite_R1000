package nl.icsvertex.scansuite.Fragments.Dialogs;


import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;

import ICS.Interfaces.iICSDefaultFragment;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Intake.IntakeorderBarcodes.cIntakeorderBarcodeAdapter;
import SSU_WHS.Inventory.InventoryorderBarcodes.cInventoryOrderBarcodeAdapter;
import SSU_WHS.Move.MoveorderBarcodes.cMoveorderBarcodeAdapter;
import SSU_WHS.PackAndShip.PackAndShipBarcode.cPackAndShipOrderBarcodeAdapter;
import SSU_WHS.Picken.PickorderBarcodes.cPickorderBarcodeAdapter;
import nl.icsvertex.scansuite.Activities.Intake.IntakeOrderIntakeActivity;
import nl.icsvertex.scansuite.Activities.Move.MoveLinePlaceActivity;
import nl.icsvertex.scansuite.Activities.Move.MoveLinePlaceGeneratedActivity;
import nl.icsvertex.scansuite.Activities.Move.MoveLineTakeActivity;
import nl.icsvertex.scansuite.Activities.Move.MoveLineTakeMTActivity;
import nl.icsvertex.scansuite.Activities.PackAndShip.PackAndShipMultiActivity;
import nl.icsvertex.scansuite.Activities.Pick.PickorderPickActivity;
import nl.icsvertex.scansuite.Activities.QualityControl.PickorderQCActivity;
import nl.icsvertex.scansuite.Activities.Receive.ReceiveOrderReceiveActivity;
import nl.icsvertex.scansuite.Activities.Inventory.InventoryArticleActivity;
import nl.icsvertex.scansuite.R;


public class BarcodeFragment extends DialogFragment implements iICSDefaultFragment {

    //Region Public Properties


    //End region

    //Region Private Properties

    private TextView  textViewChooseBarcode;
    private  RecyclerView barcodeRecyclerview;
    private  Button buttonClose;

    private cIntakeorderBarcodeAdapter intakeorderBarcodeAdapter;
    private cIntakeorderBarcodeAdapter getIntakeorderBarcodeAdapter(){
        if (this.intakeorderBarcodeAdapter == null) {
            this.intakeorderBarcodeAdapter = new cIntakeorderBarcodeAdapter();
        }
        return  this.intakeorderBarcodeAdapter;
    }

    private cPickorderBarcodeAdapter pickorderBarcodeAdapter;
    private cPickorderBarcodeAdapter getPickorderBarcodeAdapter(){
        if (this.pickorderBarcodeAdapter == null) {
            this.pickorderBarcodeAdapter = new cPickorderBarcodeAdapter();
        }
        return  this.pickorderBarcodeAdapter;
    }

    private cMoveorderBarcodeAdapter moveorderBarcodeAdapter;
    private cMoveorderBarcodeAdapter getMoveorderBarcodeAdapter(){
        if (this.moveorderBarcodeAdapter == null) {
            this.moveorderBarcodeAdapter = new cMoveorderBarcodeAdapter();
        }
        return  this.moveorderBarcodeAdapter;
    }

    private cPackAndShipOrderBarcodeAdapter packAndShipOrderBarcodeAdapter;
    private cPackAndShipOrderBarcodeAdapter getPackAndShipOrderBarcodeAdapter(){
        if (this.packAndShipOrderBarcodeAdapter == null) {
            this.packAndShipOrderBarcodeAdapter = new cPackAndShipOrderBarcodeAdapter();
        }
        return  this.packAndShipOrderBarcodeAdapter;
    }

    private cInventoryOrderBarcodeAdapter inventoryOrderBarcodeAdapter;
    private cInventoryOrderBarcodeAdapter getInventoryOrderBarcodeAdapter(){
        if (this.inventoryOrderBarcodeAdapter == null) {
            this.inventoryOrderBarcodeAdapter = new cInventoryOrderBarcodeAdapter();
        }
        return  this.inventoryOrderBarcodeAdapter;
    }



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
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
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
            this.textViewChooseBarcode = getView().findViewById(R.id.textViewChooseBarcode);
            this.barcodeRecyclerview = getView().findViewById(R.id.barcodeRecyclerview);
            this.buttonClose = getView().findViewById(R.id.buttonClose);
        }
    }

    @Override
    public void mFieldsInitialize() {
        this.mFillRecyclerView();

        if (cAppExtension.activity instanceof PackAndShipMultiActivity) {
            this.textViewChooseBarcode.setText(R.string.documents);
        }

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
            this.barcodeRecyclerview.setAdapter(this.getPickorderBarcodeAdapter());
        }

        if (cAppExtension.activity instanceof IntakeOrderIntakeActivity) {
            this.barcodeRecyclerview.setAdapter(this.getIntakeorderBarcodeAdapter());
        }

        if (cAppExtension.activity instanceof ReceiveOrderReceiveActivity) {
            this.barcodeRecyclerview.setAdapter(this.getIntakeorderBarcodeAdapter());
        }

        if (cAppExtension.activity instanceof MoveLineTakeActivity) {
            this.barcodeRecyclerview.setAdapter(this.getMoveorderBarcodeAdapter());
        }

        if (cAppExtension.activity instanceof MoveLineTakeMTActivity) {
            this.barcodeRecyclerview.setAdapter(this.getMoveorderBarcodeAdapter());
        }

        if (cAppExtension.activity instanceof MoveLinePlaceActivity) {
            this.barcodeRecyclerview.setAdapter(this.getMoveorderBarcodeAdapter());
        }

        if (cAppExtension.activity instanceof MoveLinePlaceGeneratedActivity) {
            this.barcodeRecyclerview.setAdapter(this.getMoveorderBarcodeAdapter());
        }

        if (cAppExtension.activity instanceof PickorderQCActivity) {
            this.barcodeRecyclerview.setAdapter(this.getPickorderBarcodeAdapter());
        }

        if (cAppExtension.activity instanceof PackAndShipMultiActivity) {
            this.barcodeRecyclerview.setAdapter(this.getPackAndShipOrderBarcodeAdapter());
        }

        if (cAppExtension.activity instanceof InventoryArticleActivity) {
                this.barcodeRecyclerview.setAdapter(this.getInventoryOrderBarcodeAdapter());
        }

        this.barcodeRecyclerview.setLayoutManager(new LinearLayoutManager(cAppExtension.context));
    }
}
