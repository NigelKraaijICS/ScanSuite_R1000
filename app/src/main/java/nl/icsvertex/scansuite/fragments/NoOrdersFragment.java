package nl.icsvertex.scansuite.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import ICS.Utils.cUserInterface;
import nl.icsvertex.scansuite.activities.pick.PickorderSelectActivity;
import nl.icsvertex.scansuite.R;
import nl.icsvertex.scansuite.activities.ship.ShiporderSelectActivity;
import nl.icsvertex.scansuite.activities.sort.SortorderSelectActivity;


public class NoOrdersFragment extends Fragment {
    Fragment thisFragment;
    ImageView imageNoOrders;
    ImageButton buttonRefreshOrders;

    public NoOrdersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_no_orders, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        thisFragment = this;
        m_findViews();
        m_setListeners();
    }
    private void m_findViews() {
        imageNoOrders = getView().findViewById(R.id.imageNoOrders);
        buttonRefreshOrders = getView().findViewById(R.id.buttonRefreshOrders);
    }
    private void m_setListeners() {
        buttonRefreshOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cUserInterface.doRotate(buttonRefreshOrders, 2 );
                m_getData();
            }
        });
    }
    private void m_getData() {
        Activity activity = getActivity();
        if (activity instanceof PickorderSelectActivity) {
            ((PickorderSelectActivity)getActivity()).mGetData();
        }
        if (activity instanceof ShiporderSelectActivity) {
            ((ShiporderSelectActivity)getActivity()).mGetData();
        }
        if (activity instanceof SortorderSelectActivity) {
            ((SortorderSelectActivity)getActivity()).mGetData();
        }

    }
}
