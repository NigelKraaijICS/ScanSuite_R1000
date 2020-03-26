package nl.icsvertex.scansuite.Fragments.Dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ICS.Interfaces.iICSDefaultFragment;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import nl.icsvertex.scansuite.Activities.IntakeAndReceive.IntakeAndReceiveSelectActivity;
import nl.icsvertex.scansuite.Activities.Inventory.InventoryorderSelectActivity;
import nl.icsvertex.scansuite.Activities.Pick.PickorderSelectActivity;
import nl.icsvertex.scansuite.Activities.Returns.ReturnorderSelectActivity;
import nl.icsvertex.scansuite.Activities.Ship.ShiporderSelectActivity;
import nl.icsvertex.scansuite.Activities.Sort.SortorderSelectActivity;
import nl.icsvertex.scansuite.R;


public class NoOrdersFragment extends Fragment implements iICSDefaultFragment {

    //Region Public Properties


    //End Region Public Properties

    //Region Private Properties
    private ImageButton buttonRefreshOrders;
    private TextView textViewNothingHere;
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
        this.mFieldsInitialize();
        this.mSetListeners();
        cUserInterface.pEnableScanner();
    }

    @Override
    public void mFindViews() {

        if (getView() != null) {
            this.buttonRefreshOrders = getView().findViewById(R.id.buttonRefreshOrders);
            this.textViewNothingHere = getView().findViewById(R.id.textViewNothingHere);
        }

    }


     @Override
    public void mFieldsInitialize() {

         this.textViewNothingHere.setText(cAppExtension.activity.getString(R.string.no_orders));

         if (cAppExtension.activity instanceof InventoryorderSelectActivity) {
             this.textViewNothingHere.setText(cAppExtension.activity.getString(R.string.no_inventory_orders));
         }

    }

    @Override
    public void mSetListeners() {

        this.buttonRefreshOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cUserInterface.pDoRotate(buttonRefreshOrders, 2);

                if (cAppExtension.activity instanceof PickorderSelectActivity) {
                    PickorderSelectActivity pickorderSelectActivity = (PickorderSelectActivity)cAppExtension.activity;
                    pickorderSelectActivity.pFillOrders();
                }


                if (cAppExtension.activity instanceof SortorderSelectActivity) {
                    SortorderSelectActivity sortorderSelectActivity = (SortorderSelectActivity)cAppExtension.activity;
                    sortorderSelectActivity.pFillOrders();
                }

                if (cAppExtension.activity instanceof ShiporderSelectActivity) {
                    ShiporderSelectActivity shiporderSelectActivity = (ShiporderSelectActivity)cAppExtension.activity;
                    shiporderSelectActivity.pFillOrders();
                }

                if (cAppExtension.activity instanceof IntakeAndReceiveSelectActivity) {
                    IntakeAndReceiveSelectActivity intakeAndReceiveSelectActivity = (IntakeAndReceiveSelectActivity)cAppExtension.activity;
                    intakeAndReceiveSelectActivity.pFillOrders();
                }

                if (cAppExtension.activity instanceof ReturnorderSelectActivity) {
                    ReturnorderSelectActivity returnorderSelectActivity = (ReturnorderSelectActivity)cAppExtension.activity;
                    returnorderSelectActivity.pFillOrders();
                }

                if (cAppExtension.activity instanceof InventoryorderSelectActivity) {
                    InventoryorderSelectActivity inventoryorderSelectActivity = (InventoryorderSelectActivity)cAppExtension.activity;
                    inventoryorderSelectActivity.pFillOrders();
                }

            }
        });

    }
}

