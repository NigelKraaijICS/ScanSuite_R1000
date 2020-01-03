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
import SSU_WHS.Picken.Pickorders.cPickorder;
import SSU_WHS.Picken.Shipment.cShipment;
import nl.icsvertex.scansuite.R;
import nl.icsvertex.scansuite.Activities.Ship.ShiporderLinesActivity;
import ICS.cAppExtension;
import nl.icsvertex.scansuite.Fragments.Dialogs.NothingHereFragment;


public class ShiporderLinesToShipFragment extends Fragment implements iICSDefaultFragment {

    //Region Public Properties

    //End Region Public Properties

    //Region Private Properties

    private static TextView textViewSelectedOrder;
    private static ConstraintLayout shipThisView;

    private static TextView quickhelpText;
    private static ImageView quickhelpIcon;
    private static ConstraintLayout quickhelpContainer;
    private static RecyclerView recyclerViewShiporderLinesToship;

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
            ShiporderLinesToShipFragment.recyclerViewShiporderLinesToship = getView().findViewById(R.id.recyclerViewShiporderLinesToship);
            ShiporderLinesToShipFragment.textViewSelectedOrder = getView().findViewById(R.id.textViewSelectedOrder);
            ShiporderLinesToShipFragment.shipThisView = getView().findViewById(R.id.shipThisView);
            ShiporderLinesToShipFragment.quickhelpText = getView().findViewById(R.id.quickhelpText);
            ShiporderLinesToShipFragment.quickhelpContainer = getView().findViewById(R.id.actionsContainer);
            ShiporderLinesToShipFragment.quickhelpIcon = getView().findViewById(R.id.quickhelpIcon);
        }


    }


    @Override
    public void mFieldsInitialize() {
        ShiporderLinesToShipFragment.quickhelpText.setText(R.string.scan_salesorder);
    }

    @Override
    public void mSetListeners() {
        this.mSetCurrentSourceNoListener();
        this.setQuickHelpListener();
    }

    //End Region iICSDefaultFragment defaults

    //Region Public Methods

    //End Region Public Methods

    public static void pSetChosenShipment(){

        if (!cShipment.currentShipment.getProcessingSequenceStr().isEmpty()) {
            ShiporderLinesToShipFragment.textViewSelectedOrder.setText(cShipment.currentShipment.getProcessingSequenceStr());

        }
        else {
            ShiporderLinesToShipFragment.textViewSelectedOrder.setText(cShipment.currentShipment.getSourceNoStr());
        }

    }

    //Region Private Methods

    private void setQuickHelpListener() {
        ShiporderLinesToShipFragment.quickhelpContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cUserInterface.pDoRotate(ShiporderLinesToShipFragment.quickhelpIcon, 0);
                if (ShiporderLinesToShipFragment.quickhelpText.getVisibility() == View.VISIBLE) {
                    ShiporderLinesToShipFragment.quickhelpText.setVisibility(View.GONE);
                }
                else {
                    ShiporderLinesToShipFragment.quickhelpText.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void mSetCurrentSourceNoListener() {
        shipThisView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             ShiporderLinesActivity.pHandleScan(null,true);
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
        ShiporderLinesToShipFragment.recyclerViewShiporderLinesToship.setHasFixedSize(false);
        ShiporderLinesToShipFragment.recyclerViewShiporderLinesToship.setAdapter(cShipment.getShipmentsToshipAdapter());
        ShiporderLinesToShipFragment.recyclerViewShiporderLinesToship.setLayoutManager(new LinearLayoutManager(cAppExtension.context));

        ShiporderLinesActivity.pChangeTabCounterText(cText.pIntToStringStr(cPickorder.currentPickOrder.pGetNotHandledShipmentsObl().size()) + "/" + cText.pIntToStringStr(cPickorder.currentPickOrder.shipmentObl().size()));

    }

    //todo: check why this is always true
    private void mNoLinesAvailable(Boolean pvEnabledBln) {

        if (ShiporderLinesActivity.currentLineFragment == this) {
            //Close no linesInt fragment if needed
            if (!pvEnabledBln) {
                List<Fragment> fragments = cAppExtension.fragmentManager.getFragments();
                for (Fragment fragment : fragments) {
                    if (fragment instanceof NothingHereFragment ) {
                        FragmentTransaction fragmentTransaction = cAppExtension.fragmentManager.beginTransaction();
                        fragmentTransaction.remove(fragment);
                        fragmentTransaction.commit();
                    }
                }

                return;

            }

            //Hide the recycler view
            ShiporderLinesToShipFragment.recyclerViewShiporderLinesToship.setVisibility(View.INVISIBLE);

            //Show nothing there fragment
            FragmentTransaction fragmentTransaction = cAppExtension.fragmentManager.beginTransaction();
            NothingHereFragment fragment = new NothingHereFragment();
            fragmentTransaction.replace(R.id.fragmentShiporderLinesToShip, fragment);
            fragmentTransaction.commit();

            //Change tabcounter text
            ShiporderLinesActivity.pChangeTabCounterText(cText.pIntToStringStr(cPickorder.currentPickOrder.pGetNotHandledShipmentsObl().size()) + "/" + cText.pIntToStringStr(cPickorder.currentPickOrder.shipmentObl().size()));

        }
    }


    //End Region Private Methods



}
