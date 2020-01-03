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
import nl.icsvertex.scansuite.Activities.Ship.ShiporderLinesActivity;
import nl.icsvertex.scansuite.R;

public class ShiporderLinesTotalFragment extends Fragment implements iICSDefaultFragment {

    //Region Public Properties

    //End Region Public Properties

    //Region Private Properties
    private static RecyclerView recyclerViewShiporderLinesTotal;
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
        this.mFragmentInitialize();
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
            ShiporderLinesTotalFragment.recyclerViewShiporderLinesTotal = getView().findViewById(R.id.recyclerViewShiporderLinesTotal);
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

        cShipment.getShipmentsTotalAdapter().pFillData(pvDataObl);
        ShiporderLinesTotalFragment.recyclerViewShiporderLinesTotal.setHasFixedSize(false);
        ShiporderLinesTotalFragment.recyclerViewShiporderLinesTotal.setAdapter(cShipment.getShipmentsTotalAdapter());
        ShiporderLinesTotalFragment.recyclerViewShiporderLinesTotal.setLayoutManager(new LinearLayoutManager(cAppExtension.context));

        ShiporderLinesActivity.pChangeTabCounterText( cText.pIntToStringStr(pvDataObl.size()));

    }
}
