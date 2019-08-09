package nl.icsvertex.scansuite.fragments.ship;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import SSU_WHS.Picken.PickorderLinePackAndShip.cPickorderLinePackAndShipGroupBySourceNo;
import SSU_WHS.Picken.PickorderLinePackAndShip.cPickorderLinePackAndShipGroupBySourceNoAdapter;
import SSU_WHS.Picken.PickorderLinePackAndShip.cPickorderLinePackAndShipViewModel;
import SSU_WHS.General.cPublicDefinitions;
import ICS.Utils.cSharedPreferences;
import nl.icsvertex.scansuite.R;

public class ShiporderLinesTotalFragment extends Fragment {
    Context thisContext;
    String currentUser;
    String currentBranch;
    String chosenOrder;

    RecyclerView recyclerViewShiporderLinesTotal;
    private cPickorderLinePackAndShipViewModel pickorderLinePackAndShipViewModel;
    //private cPickorderLinePackAndShipAdapter pickorderLinePackAndShipAdapter;
    private cPickorderLinePackAndShipGroupBySourceNoAdapter pickorderLinePackAndShipGroupBySourceNoAdapter;

    public ShiporderLinesTotalFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shiporder_lines_total, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        thisContext = this.getContext();
        currentUser = cSharedPreferences.getSharedPreferenceString(cPublicDefinitions.PREFERENCE_CURRENT_USER, "");
        currentBranch = cSharedPreferences.getSharedPreferenceString(cPublicDefinitions.PREFERENCE_CURRENT_BRANCH, "");

        Intent intent = getActivity().getIntent();
        chosenOrder = intent.getStringExtra(cPublicDefinitions.PICKING_CHOSENORDER);

        m_findViews();
        m_getData();
    }

    private void m_findViews() {
        recyclerViewShiporderLinesTotal = getView().findViewById(R.id.recyclerViewShiporderLinesTotal);
    }
    private void m_getData() {
        pickorderLinePackAndShipViewModel = ViewModelProviders.of(this).get(cPickorderLinePackAndShipViewModel.class);
        pickorderLinePackAndShipViewModel.getAllPickorderinePackAndShipEntitiesDistinctSourceno().observe(this, new Observer<List<cPickorderLinePackAndShipGroupBySourceNo>>() {
            @Override
            public void onChanged(@Nullable List<cPickorderLinePackAndShipGroupBySourceNo> pickorderLinePackAndShipGroupBySourceNos) {
                m_setShiporderLineRecycler(pickorderLinePackAndShipGroupBySourceNos);
            }
        });
    }

    private void m_setShiporderLineRecycler(List<cPickorderLinePackAndShipGroupBySourceNo> pickorderLinePackAndShipGroupBySourceNos) {
        pickorderLinePackAndShipGroupBySourceNoAdapter = new cPickorderLinePackAndShipGroupBySourceNoAdapter(thisContext);
        //pickorderLinePackAndShipAdapter = new cPickorderLinePackAndShipAdapter(thisContext);
        recyclerViewShiporderLinesTotal.setHasFixedSize(false);
        recyclerViewShiporderLinesTotal.setAdapter(pickorderLinePackAndShipGroupBySourceNoAdapter);
        recyclerViewShiporderLinesTotal.setLayoutManager(new LinearLayoutManager(thisContext));
        pickorderLinePackAndShipGroupBySourceNoAdapter.setPickorderLinePacikAndShips(pickorderLinePackAndShipGroupBySourceNos);
    }
}
