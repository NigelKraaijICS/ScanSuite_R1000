package nl.icsvertex.scansuite.Fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import ICS.Interfaces.iICSDefaultFragment;
import ICS.Utils.cUserInterface;

import nl.icsvertex.scansuite.R;
import nl.icsvertex.scansuite.Activities.pick.PickorderSelectActivity;
import nl.icsvertex.scansuite.Activities.ship.ShiporderSelectActivity;
import nl.icsvertex.scansuite.Activities.sort.SortorderSelectActivity;
import ICS.cAppExtension;


public class NoOrdersFragment extends Fragment implements iICSDefaultFragment {

    //Region Public Properties


    //End Region Public Properties

    //Region Private Properties
    private ImageView imageNoOrders;
    private ImageButton buttonRefreshOrders;
    //End Region Private Properties

    //Region Constructor
    public NoOrdersFragment() {
        // Required empty public constructor
    }
    //End Region Constructor

    //Region Default Methods

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_no_orders, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        this.mFragmentInitialize();
    }

    //End Region Default Methods

    @Override
    public void mFragmentInitialize() {

        this.mFindViews();
        this.mSetViewModels();
        this.mFieldsInitialize();
        this.mSetListeners();
    }

    @Override
    public void mFindViews() {
        this.imageNoOrders = getView().findViewById(R.id.imageNoOrders);
        this.buttonRefreshOrders = getView().findViewById(R.id.buttonRefreshOrders);
    }


    @Override
    public void mSetViewModels() {

    }

    @Override
    public void mFieldsInitialize() {


    }

    @Override
    public void mSetListeners() {

        this.buttonRefreshOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cUserInterface.pDoRotate(buttonRefreshOrders, 2);

                if (cAppExtension.activity instanceof PickorderSelectActivity) {
                    PickorderSelectActivity.pFillOrders();
                }


                if (cAppExtension.activity instanceof SortorderSelectActivity) {
                    SortorderSelectActivity.pFillOrders();
                }

                if (cAppExtension.activity instanceof ShiporderSelectActivity) {
                    ShiporderSelectActivity.pFillOrders();
                }

            }
        });

    }
}

