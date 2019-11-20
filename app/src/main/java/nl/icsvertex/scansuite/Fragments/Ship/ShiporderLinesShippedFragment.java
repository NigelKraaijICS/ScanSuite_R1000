package nl.icsvertex.scansuite.Fragments.Ship;

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
import SSU_WHS.Picken.Pickorders.cPickorder;
import SSU_WHS.Picken.Shipment.cShipment;
import nl.icsvertex.scansuite.R;
import nl.icsvertex.scansuite.Activities.Ship.ShiporderLinesActivity;
import ICS.cAppExtension;
import nl.icsvertex.scansuite.Fragments.Dialogs.NothingHereFragment;

public class ShiporderLinesShippedFragment extends Fragment implements iICSDefaultFragment {

    //Region Public Properties

    //End Region Public Properties

    //Region Private Properties

    private RecyclerView recyclerViewShiporderLinesShipped;

    //End Region Private Properties

    //Region Constructor
    public ShiporderLinesShippedFragment() {
        // Required empty public constructor
    }
    //End Region Constructor

    //Region Default Methods

    @Override
    public View onCreateView(LayoutInflater pvLayoutInflater, ViewGroup pvViewGroup,
                             Bundle pvSavedInstanceState) {
        // Inflate the layout for this fragment
        return pvLayoutInflater.inflate(R.layout.fragment_shiporder_lines_shipped, pvViewGroup, false);

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
            this.recyclerViewShiporderLinesShipped = getView().findViewById(R.id.recyclerViewShiporderLinesShipped);

    }



    @Override
    public void mFieldsInitialize() {

    }

    @Override
    public void mSetListeners() {

    }

    //End Region iICSDefaultFragment defaults

    //Region Private Methods

    private void mGetData() {
        List<cShipment> handledShipmentsObl = cPickorder.currentPickOrder.pGetHandledShipmentsObl();
        this.mFillRecycler(handledShipmentsObl);
    }

    private void mFillRecycler( List<cShipment> pvDataObl) {

        if (pvDataObl.size() == 0) {
            this.mNoLinesAvailable(true);
            return;
        }

        cShipment.gethipmentsShippedAdapter().pFillData(pvDataObl);
        this.recyclerViewShiporderLinesShipped.setHasFixedSize(false);
        this.recyclerViewShiporderLinesShipped.setAdapter( cShipment.gethipmentsShippedAdapter());
        this.recyclerViewShiporderLinesShipped.setLayoutManager(new LinearLayoutManager(cAppExtension.context));

        ShiporderLinesActivity.pChangeTabCounterText(cText.pIntToStringStr(cPickorder.currentPickOrder.pGetHandledShipmentsObl().size()) + "/" + cText.pIntToStringStr(cPickorder.currentPickOrder.shipmentObl().size()));

    }

    private void mNoLinesAvailable(Boolean pvEnabledBln) {

        if (ShiporderLinesActivity.currentLineFragment != null && ShiporderLinesActivity.currentLineFragment == this) {
            //Close no linesInt fragment if needed
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
            this.recyclerViewShiporderLinesShipped.setVisibility(View.INVISIBLE);

            //Show nothing there fragment
            FragmentTransaction fragmentTransaction = cAppExtension.fragmentManager.beginTransaction();
            NothingHereFragment fragment = new NothingHereFragment();
            fragmentTransaction.replace(R.id.fragmentShiporderLinesShipped, fragment);
            fragmentTransaction.commit();

            //Change tabcounter text
            ShiporderLinesActivity.pChangeTabCounterText(cText.pIntToStringStr(cPickorder.currentPickOrder.pGetHandledShipmentsObl().size()) + "/" + cText.pIntToStringStr(cPickorder.currentPickOrder.shipmentObl().size()));

        }
    }

    //End Region Private Methods


}
