package nl.icsvertex.scansuite.Fragments.ship;

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
import SSU_WHS.Picken.Pickorders.cPickorder;
import SSU_WHS.Picken.Shipment.cShipment;
import nl.icsvertex.scansuite.R;
import nl.icsvertex.scansuite.Activities.ship.ShiporderLinesActivity;
import ICS.cAppExtension;
import nl.icsvertex.scansuite.Fragments.NothingHereFragment;


public class ShiporderLinesToShipFragment extends Fragment implements iICSDefaultFragment {

    //Region Public Properties

    //End Region Public Properties

    //Region Private Properties

    private TextView textViewSelectedOrder;
    private ConstraintLayout shipThisView;

    private TextView quickhelpText;
    private ImageView quickhelpIcon;
    private ConstraintLayout quickhelpContainer;
    private RecyclerView recyclerViewShiporderLinesToship;

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
        this.mSetViewModels();
        this.mFieldsInitialize();
        this.mSetListeners();
        this.mGetData();
    }

    @Override
    public void mFindViews() {
            this.recyclerViewShiporderLinesToship = getView().findViewById(R.id.recyclerViewShiporderLinesToship);
            this.textViewSelectedOrder = getView().findViewById(R.id.textViewSelectedOrder);
            this.shipThisView = getView().findViewById(R.id.shipThisView);
            this.quickhelpText = getView().findViewById(R.id.quickhelpText);
            this.quickhelpContainer = getView().findViewById(R.id.quickhelpContainer);
            this.quickhelpIcon = getView().findViewById(R.id.quickhelpIcon);
    }

    @Override
    public void mSetViewModels() {

    }

    @Override
    public void mFieldsInitialize() {
        this.quickhelpText.setText(R.string.scan_salesorder);
;
    }

    @Override
    public void mSetListeners() {
        this.mSetCurrentSourceNoListener();
        this.setQuickHelpListener();
    }

    //End Region iICSDefaultFragment defaults

    //Region Private Methods

    private void setQuickHelpListener() {
        this.quickhelpContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cUserInterface.pDoRotate( quickhelpIcon, 0);
                if (quickhelpText.getVisibility() == View.VISIBLE) {
                    quickhelpText.setVisibility(View.GONE);
                }
                else {
                    quickhelpText.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void mSetCurrentSourceNoListener() {
        shipThisView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //todo: call methods in lines activity
             ShiporderLinesActivity.pHandleScan("",true);
            }
        });
    }

    private void mGetData() {

        List<cShipment> notHandledShipmentsObl = cPickorder.currentPickOrder.pGetNotHandledShipmentsObl();
        this.mFillRecycler(notHandledShipmentsObl);
    }

    private void mFillRecycler(List<cShipment> pvDataObl) {

        if (pvDataObl.size() == 0) {
            this.mNoLinesAvailable(true);
            return;
        }

        cShipment.getShipmentsToshipAdapter().pFillData(pvDataObl);
        this.recyclerViewShiporderLinesToship.setHasFixedSize(false);
        this.recyclerViewShiporderLinesToship.setAdapter(cShipment.getShipmentsToshipAdapter());
        this.recyclerViewShiporderLinesToship.setLayoutManager(new LinearLayoutManager(cAppExtension.context));

        ShiporderLinesActivity.pChangeTabCounterText(cText.intToString(cPickorder.currentPickOrder.pGetNotHandledShipmentsObl().size()) + "/" + cText.intToString(cPickorder.currentPickOrder.shipmentObl().size()));

    }

    private void mNoLinesAvailable(Boolean pvEnabledBln) {

        if (ShiporderLinesActivity.currentLineFragment != null && ShiporderLinesActivity.currentLineFragment == this) {
            //Close no lines fragment if needed
            if (!pvEnabledBln) {
                List<Fragment> fragments = cAppExtension.fragmentManager.getFragments();
                for (Fragment fragment : fragments) {
                    if (fragment instanceof NothingHereFragment || fragment instanceof NothingHereFragment ) {
                        FragmentTransaction fragmentTransaction = cAppExtension.fragmentManager.beginTransaction();
                        fragmentTransaction.remove(fragment);
                        fragmentTransaction.commit();
                    }
                }

                return;

            }

            //Hide the recycler view
            this.recyclerViewShiporderLinesToship.setVisibility(View.INVISIBLE);

            //Show nothing there fragment
            FragmentTransaction fragmentTransaction = cAppExtension.fragmentManager.beginTransaction();
            NothingHereFragment fragment = new NothingHereFragment();
            fragmentTransaction.replace(R.id.fragmentShiporderLinesToShip, fragment);
            fragmentTransaction.commit();

            //Change tabcounter text
            ShiporderLinesActivity.pChangeTabCounterText(cText.intToString(cPickorder.currentPickOrder.pGetNotHandledShipmentsObl().size()) + "/" + cText.intToString(cPickorder.currentPickOrder.shipmentObl().size()));

        }
    }


    //End Region Private Methods



}
