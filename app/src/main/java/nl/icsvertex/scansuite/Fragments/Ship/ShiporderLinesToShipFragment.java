package nl.icsvertex.scansuite.Fragments.Ship;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ICS.Interfaces.iICSDefaultFragment;
import ICS.Utils.cText;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Basics.Settings.cSetting;
import SSU_WHS.Picken.Pickorders.cPickorder;
import SSU_WHS.Picken.Shipment.cShipment;
import SSU_WHS.Picken.Shipment.cShipmentAdapter;
import nl.icsvertex.scansuite.Activities.Ship.ShiporderLinesActivity;
import nl.icsvertex.scansuite.Fragments.Dialogs.NothingHereFragment;
import nl.icsvertex.scansuite.R;


public class ShiporderLinesToShipFragment extends Fragment implements iICSDefaultFragment {

    //Region Public Properties

    //End Region Public Properties

    //Region Private Properties

    private TextView quickhelpText;
    private ImageView quickhelpIcon;
    private ConstraintLayout quickhelpContainer;
   private RecyclerView recyclerViewShiporderLinesToship;

    private cShipmentAdapter shipmentAdapter;
    private  cShipmentAdapter getShipmentAdapter(){
        if (this.shipmentAdapter == null) {
            this.shipmentAdapter = new cShipmentAdapter();
        }

        return  this.shipmentAdapter;
    }

    //End Region Private Properties


    //Region Constructor
    public ShiporderLinesToShipFragment() {
        // Required empty public constructor
    }
    //End Region Constructor



    //Region Default Methods

    @Override
    public View onCreateView(LayoutInflater pvLayoutInflater, ViewGroup pvViewGroup,
                             Bundle pvSavedInstanceState) {
        // Inflate the layout for this fragment
        return pvLayoutInflater.inflate(R.layout.fragment_shiporder_lines_to_ship, pvViewGroup, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        cUserInterface.pEnableScanner();
        ShiporderLinesActivity.currentLineFragment = this;
        this.mFragmentInitialize();

    }

    //End Region Default Methods

    //Region iICSDefaultFragment defaults


    @Override
    public void mFragmentInitialize() {
        this.mFindViews();
        this.mFieldsInitialize();
        this.mSetListeners();
        this.pGetData();
        cUserInterface.pEnableScanner();
    }

    @Override
    public void mFindViews() {

        if (getView() != null) {
            this.recyclerViewShiporderLinesToship = getView().findViewById(R.id.recyclerViewShiporderLinesToship);
            this.quickhelpText = getView().findViewById(R.id.quickhelpText);
            this.quickhelpContainer = getView().findViewById(R.id.quickHelpContainer);
            this.quickhelpIcon = getView().findViewById(R.id.quickhelpIcon);
        }


    }


    @Override
    public void mFieldsInitialize() {
        this.recyclerViewShiporderLinesToship.setVisibility(View.VISIBLE);
        this.quickhelpText.setText(R.string.scan_salesorder);
    }

    @Override
    public void mSetListeners() {
        this.setQuickHelpListener();
    }

    //End Region iICSDefaultFragment defaults

    //Region Public Methods

    //End Region Public Methods

    public  void pSetChosenShipment(){

        if (cSetting.PICK_SELECTEREN_BARCODE()){
            ShiporderLinesActivity shiporderLinesActivity = (ShiporderLinesActivity)cAppExtension.activity;
            shiporderLinesActivity.pHandleScan(null,true);
        }
    }

    //Region Private Methods

    private void setQuickHelpListener() {
        this.quickhelpContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cUserInterface.pDoRotate(quickhelpIcon, 0);
                if (quickhelpText.getVisibility() == View.VISIBLE) {
                    quickhelpText.setVisibility(View.GONE);
                }
                else {
                    quickhelpText.setVisibility(View.VISIBLE);
                }
            }
        });
    }


    public void pGetData() {
        List<cShipment> notHandledShipmentsObl = cPickorder.currentPickOrder.pGetNotHandledShipmentsObl();
        this.mFillRecycler(notHandledShipmentsObl);
    }

    private void mFillRecycler(List<cShipment> pvDataObl) {

        if (pvDataObl.size() == 0) {
            this.mNoLinesAvailable();
            return;
        }

        this.getShipmentAdapter().pFillData(pvDataObl);
        this.recyclerViewShiporderLinesToship.setHasFixedSize(false);
        this.recyclerViewShiporderLinesToship.setAdapter(this.getShipmentAdapter());
        this.recyclerViewShiporderLinesToship.setLayoutManager(new LinearLayoutManager(cAppExtension.context));

        if (cAppExtension.activity instanceof  ShiporderLinesActivity) {
            ShiporderLinesActivity shiporderLinesActivity = (ShiporderLinesActivity)cAppExtension.activity;
            shiporderLinesActivity.pChangeTabCounterText(cText.pIntToStringStr(cPickorder.currentPickOrder.pGetNotHandledShipmentsObl().size()) + "/" + cText.pIntToStringStr(cPickorder.currentPickOrder.shipmentObl().size()));
        }

    }

    private void mNoLinesAvailable() {

        if (ShiporderLinesActivity.currentLineFragment == this) {
            //Close no linesInt fragment if needed


            //Hide the recycler view
            this.recyclerViewShiporderLinesToship.setVisibility(View.INVISIBLE);

            //Show nothing there fragment
            FragmentTransaction fragmentTransaction = cAppExtension.fragmentManager.beginTransaction();
            NothingHereFragment fragment = new NothingHereFragment();
            fragmentTransaction.replace(R.id.fragmentShiporderLinesToShip, fragment);
            fragmentTransaction.commit();

            //Change tabcounter text
            if (cAppExtension.activity instanceof  ShiporderLinesActivity) {
                ShiporderLinesActivity shiporderLinesActivity = (ShiporderLinesActivity) cAppExtension.activity;
                shiporderLinesActivity.pChangeTabCounterText(cText.pIntToStringStr(cPickorder.currentPickOrder.pGetNotHandledShipmentsObl().size()) + "/" + cText.pIntToStringStr(cPickorder.currentPickOrder.shipmentObl().size()));
            }

        }
    }

    //End Region Private Methods

}
