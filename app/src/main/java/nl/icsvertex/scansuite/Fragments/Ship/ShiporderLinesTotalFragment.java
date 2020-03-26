package nl.icsvertex.scansuite.Fragments.Ship;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ICS.Interfaces.iICSDefaultFragment;
import ICS.Utils.cText;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Picken.Shipment.cShipment;
import SSU_WHS.Picken.Shipment.cShipmentAdapter;
import nl.icsvertex.scansuite.Activities.Ship.ShiporderLinesActivity;
import nl.icsvertex.scansuite.R;

public class ShiporderLinesTotalFragment extends Fragment implements iICSDefaultFragment {

    //Region Public Properties

    //End Region Public Properties

    //Region Private Properties
    private  RecyclerView recyclerViewShiporderLinesTotal;

    private cShipmentAdapter shipmentAdapter;
    private  cShipmentAdapter getShipmentAdapter(){
        if (this.shipmentAdapter == null) {
            this.shipmentAdapter = new cShipmentAdapter();
        }

        return  this.shipmentAdapter;
    }
    //End Region Private Properties


    //Region Constructor
    public ShiporderLinesTotalFragment() {
        // Required empty public constructor
    }

    //End Region Constructor

    //Region Default Methods

    @Override
    public View onCreateView(LayoutInflater pvLayoutInflater, ViewGroup pvViewGroup,
                             Bundle pvSavedInstanceState) {
        // Inflate the layout for this fragment
        return pvLayoutInflater.inflate(R.layout.fragment_shiporder_lines_total, pvViewGroup, false);
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
        this.mGetData();
        cUserInterface.pEnableScanner();
    }

    @Override
    public void mFindViews() {

        if (getView() != null) {
            this.recyclerViewShiporderLinesTotal = getView().findViewById(R.id.recyclerViewShiporderLinesTotal);
        }
    }



    @Override
    public void mFieldsInitialize() {

    }

    @Override
    public void mSetListeners() {

    }

    //End Region iICSDefaultFragment defaults

    private void mGetData() {

        this.mFillRecycler(cShipment.allShipmentsObl);
    }

    private void mFillRecycler(List<cShipment> pvDataObl) {

        this.getShipmentAdapter().pFillData(pvDataObl);
        this.recyclerViewShiporderLinesTotal.setHasFixedSize(false);
        this.recyclerViewShiporderLinesTotal.setAdapter(this.getShipmentAdapter());
        this.recyclerViewShiporderLinesTotal.setLayoutManager(new LinearLayoutManager(cAppExtension.context));

        if (cAppExtension.activity instanceof  ShiporderLinesActivity) {
            ShiporderLinesActivity shiporderLinesActivity = (ShiporderLinesActivity)cAppExtension.activity;
            shiporderLinesActivity.pChangeTabCounterText( cText.pIntToStringStr(pvDataObl.size()));
        }
    }
}
