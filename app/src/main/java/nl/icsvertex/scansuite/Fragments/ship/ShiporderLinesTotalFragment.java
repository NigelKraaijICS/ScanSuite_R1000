package nl.icsvertex.scansuite.Fragments.ship;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ICS.Interfaces.iICSDefaultFragment;
import ICS.Utils.cText;
import SSU_WHS.Picken.Shipment.cShipment;
import nl.icsvertex.scansuite.R;
import nl.icsvertex.scansuite.Activities.ship.ShiporderLinesActivity;
import ICS.cAppExtension;

public class ShiporderLinesTotalFragment extends Fragment implements iICSDefaultFragment {

    //Region Public Properties

    //End Region Public Properties

    //Region Private Properties
    private RecyclerView recyclerViewShiporderLinesTotal;
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
    public void setUserVisibleHint(boolean pvIsVisibleToUserBln) {
        super.setUserVisibleHint(pvIsVisibleToUserBln);

        if (pvIsVisibleToUserBln) {

            ShiporderLinesActivity.currentLineFragment = this;

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.detach(this).attach(this).commit();
        }
    }

    //End Region Default Methods

    //Region iICSDefaultFragment defaults

    @Override
    public void mFragmentInitialize() {
        this.mFindViews();
        this.mFieldsInitialize();
        this.mSetListeners();
        this.mGetData();
    }

    @Override
    public void mFindViews() {
          this.recyclerViewShiporderLinesTotal = getView().findViewById(R.id.recyclerViewShiporderLinesTotal);
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
        this.recyclerViewShiporderLinesTotal.setHasFixedSize(false);
        this.recyclerViewShiporderLinesTotal.setAdapter(cShipment.getShipmentsTotalAdapter());
        this.recyclerViewShiporderLinesTotal.setLayoutManager(new LinearLayoutManager(cAppExtension.context));

        ShiporderLinesActivity.pChangeTabCounterText( cText.pIntToStringStr(pvDataObl.size()));

    }
}
