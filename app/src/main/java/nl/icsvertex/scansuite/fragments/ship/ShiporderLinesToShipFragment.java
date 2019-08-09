package nl.icsvertex.scansuite.fragments.ship;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import SSU_WHS.Picken.PickorderLinePackAndShip.cPickorderLinePackAndShip;
import SSU_WHS.Picken.PickorderLinePackAndShip.cPickorderLinePackAndShipAdapter;
import SSU_WHS.Picken.PickorderLinePackAndShip.cPickorderLinePackAndShipEntity;
import SSU_WHS.Picken.PickorderLinePackAndShip.cPickorderLinePackAndShipGroupBySourceNo;
import SSU_WHS.Picken.PickorderLinePackAndShip.cPickorderLinePackAndShipGroupBySourceNoAdapter;
import SSU_WHS.Picken.PickorderLinePackAndShip.cPickorderLinePackAndShipViewModel;
import SSU_WHS.Basics.Settings.cSettingsViewModel;
import SSU_WHS.General.cPublicDefinitions;
import ICS.Utils.cSharedPreferences;
import ICS.Utils.cUserInterface;
import nl.icsvertex.scansuite.R;
import nl.icsvertex.scansuite.activities.ship.ShipDetermineTransportActivity;
import nl.icsvertex.scansuite.activities.ship.ShiporderLinesActivity;


public class ShiporderLinesToShipFragment extends Fragment {

    Context thisContext;

    cSettingsViewModel settingsViewModel;

    String currentUser;
    String currentBranch;
    String chosenOrder;

    TextView textViewSelectedOrder;
    ConstraintLayout shipThisView;
    //region Settings

    //endregion Settings
    //region quickhelp
    TextView quickhelpText;
    ImageView quickhelpIcon;
    ConstraintLayout quickhelpContainer;
    //endregion quickhelp


    RecyclerView recyclerViewShiporderLinesToship;
    private cPickorderLinePackAndShipViewModel pickorderLinePackAndShipViewModel;
    private cPickorderLinePackAndShipAdapter pickorderLinePackAndShipAdapter;
    private cPickorderLinePackAndShipGroupBySourceNoAdapter pickorderLinePackAndShipGroupBySourceNoAdapter;

    public ShiporderLinesToShipFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shiporder_lines_to_ship, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        thisContext = this.getContext();
        currentUser = cSharedPreferences.getSharedPreferenceString(cPublicDefinitions.PREFERENCE_CURRENT_USER, "");
        currentBranch = cSharedPreferences.getSharedPreferenceString(cPublicDefinitions.PREFERENCE_CURRENT_BRANCH, "");

        Intent intent = getActivity().getIntent();
        chosenOrder = intent.getStringExtra(cPublicDefinitions.PICKING_CHOSENORDER);
        settingsViewModel = ViewModelProviders.of(this).get(cSettingsViewModel.class);

        m_findViews();
        m_getSettings();
        m_fieldsInitialize();
        m_setListeners();
        m_getData();
    }

    private void m_findViews() {
        recyclerViewShiporderLinesToship = getView().findViewById(R.id.recyclerViewShiporderLinesToship);
        textViewSelectedOrder = getView().findViewById(R.id.textViewSelectedOrder);
        shipThisView = getView().findViewById(R.id.shipThisView);
        quickhelpText = getView().findViewById(R.id.quickhelpText);
        quickhelpContainer = getView().findViewById(R.id.quickhelpContainer);
        quickhelpIcon = getView().findViewById(R.id.quickhelpIcon);
    }
    private void m_getSettings() {

    }
    private void m_fieldsInitialize() {
        quickhelpText.setText(R.string.scan_salesorder);
    }
    private void m_setListeners() {
        mSetCurrentSourceNoListener();
        setQuickHelpListener();
    }
    private void setQuickHelpListener() {
        quickhelpContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cUserInterface.doRotate( quickhelpIcon, 0);
                if (quickhelpText.getVisibility() == View.VISIBLE) {
                    quickhelpText.setVisibility(View.GONE);
                }
                else {
                    quickhelpText.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public void setChosenPickOrderLinePackAndShip(cPickorderLinePackAndShipGroupBySourceNo pickorderLinePackAndShipGroupBySourceNo) {
        textViewSelectedOrder.setText(pickorderLinePackAndShipGroupBySourceNo.getSourceno());
    }
    private void mSetCurrentSourceNoListener() {
        shipThisView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(thisContext, ShipDetermineTransportActivity.class);
                cPickorderLinePackAndShipEntity chosenPickorderLinePackAndShip;
                chosenPickorderLinePackAndShip = ((ShiporderLinesActivity)getActivity()).getChosenShiporderLine();
                intent.putExtra(cPublicDefinitions.SHIPPING_CHOSENSOURCENO, chosenPickorderLinePackAndShip.getSourceno());
                startActivity(intent);
            }
        });
    }
    private void m_getData() {
        pickorderLinePackAndShipViewModel = ViewModelProviders.of(this).get(cPickorderLinePackAndShipViewModel.class);
        pickorderLinePackAndShipViewModel.getNotHandledPickorderinePackAndShipEntitiesDistinctSourceno().observe(this, new Observer<List<cPickorderLinePackAndShipGroupBySourceNo>>() {
            @Override
            public void onChanged(@Nullable List<cPickorderLinePackAndShipGroupBySourceNo> pickorderLinePackAndShipGroupBySourceNos) {
                List<cPickorderLinePackAndShipGroupBySourceNo> pickorderLinePackAndShipGroupBySourceNoNotZeros = new ArrayList<>();
                if (pickorderLinePackAndShipGroupBySourceNos != null) {
                    for (cPickorderLinePackAndShipGroupBySourceNo pickorderLinePackAndShipGroupBySourceNo : pickorderLinePackAndShipGroupBySourceNos) {
                        if (pickorderLinePackAndShipGroupBySourceNo.getQuantityhandled() > 0) {
                            pickorderLinePackAndShipGroupBySourceNoNotZeros.add(pickorderLinePackAndShipGroupBySourceNo);
                        }
                        else {
                            pickorderLinePackAndShipViewModel.updateOrderLinePackAndShipLocalStatusBySourceno(pickorderLinePackAndShipGroupBySourceNo.getSourceno(), cPickorderLinePackAndShip.LOCALSTATUS_DONE_SENT);
                        }
                    }
                    m_setShiporderLineRecycler(pickorderLinePackAndShipGroupBySourceNoNotZeros);
                }
            }
        });

    }
    private void  m_setShiporderLineRecycler(List<cPickorderLinePackAndShipGroupBySourceNo> shiporderLinePackAndShipsNotHandledEntities) {
        pickorderLinePackAndShipGroupBySourceNoAdapter = new cPickorderLinePackAndShipGroupBySourceNoAdapter(thisContext);
        //pickorderLinePackAndShipAdapter = new cPickorderLinePackAndShipAdapter(thisContext);
        recyclerViewShiporderLinesToship.setHasFixedSize(false);
        recyclerViewShiporderLinesToship.setAdapter(pickorderLinePackAndShipGroupBySourceNoAdapter);
        recyclerViewShiporderLinesToship.setLayoutManager(new LinearLayoutManager(thisContext));
        pickorderLinePackAndShipGroupBySourceNoAdapter.setPickorderLinePacikAndShips(shiporderLinePackAndShipsNotHandledEntities);
    }

}
