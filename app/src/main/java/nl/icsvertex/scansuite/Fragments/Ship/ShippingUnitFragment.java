package nl.icsvertex.scansuite.Fragments.Ship;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import java.util.List;
import java.util.Objects;

import ICS.Interfaces.iICSDefaultFragment;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Basics.ShippingAgentServiceShippingUnits.cShippingAgentServiceShippingUnit;
import SSU_WHS.Basics.ShippingAgentServiceShippingUnits.cShippingAgentServiceShippingUnitAdapter;
import SSU_WHS.Picken.Shipment.cShipment;
import nl.icsvertex.scansuite.R;


public class ShippingUnitFragment extends DialogFragment implements iICSDefaultFragment {

    //Region Public Properties

    //End Region Public Properties

    //Region Private Properties
    private  RecyclerView shippingUnitRecyclerView;
    private  Button buttonClose;
    private  Button buttonDone;

    private cShippingAgentServiceShippingUnitAdapter shippingAgentServiceShippingUnitAdapter;
    private cShippingAgentServiceShippingUnitAdapter getShippingAgentServiceShippingUnitAdapter(){
        if (this.shippingAgentServiceShippingUnitAdapter == null) {
            this.shippingAgentServiceShippingUnitAdapter = new cShippingAgentServiceShippingUnitAdapter();
        }

        return  shippingAgentServiceShippingUnitAdapter;
    }

    //End Region Private Properties

    //Region Constructor
    public ShippingUnitFragment() {
        // Required empty public constructor
    }
    //End Region Constructor

    //Region Default Methods

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_shipping_unit, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle pvSavedInstanceState) {
        super.onActivityCreated(pvSavedInstanceState);
        this.mFragmentInitialize();
    }

    @Override
    public void onResume() {
        super.onResume();
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels - getResources().getDimensionPixelSize(R.dimen.default_double_margin);

        if (getDialog() != null) {
            Objects.requireNonNull(getDialog().getWindow()).setLayout(width, height);
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
        cUserInterface.pEnableScanner();
    }

    @Override
    public void mFindViews() {
        if (getView() != null) {
            this.buttonClose = getView().findViewById(R.id.buttonClose);
            this.buttonDone = getView().findViewById(R.id.buttonDone);
            this.shippingUnitRecyclerView = getView().findViewById(R.id.shippingUnitRecyclerView);
            this.shippingUnitRecyclerView.setHapticFeedbackEnabled(true);
        }
    }

    @Override
    public void mFieldsInitialize() {

    }


    @Override
    public void mSetListeners() {
        this.mSetCloseListener();
        this.mSetOKListener();
    }

    //End Region iICSDefaultFragment defaults


    private void mGetData() {
        this.mFillRecycler(cShipment.currentShipment.shippingAgentService().shippingUnitsObl());
    }


    private void mSetCloseListener() {
        this.buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cShippingAgentServiceShippingUnit.currentShippingAgentServiceShippingUnit != null) {
                    cShippingAgentServiceShippingUnit.currentShippingAgentServiceShippingUnit.ShippingUnitQuantityUsedInt = 0;
                }
                dismiss();
            }
        });
    }
    private void mSetOKListener() {
        this.buttonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    private void mFillRecycler(List<cShippingAgentServiceShippingUnit> pvDataObl) {

        ((SimpleItemAnimator) Objects.requireNonNull(shippingUnitRecyclerView.getItemAnimator())).setSupportsChangeAnimations(false);
        this.shippingUnitRecyclerView.setHasFixedSize(false);
        this.shippingUnitRecyclerView.setAdapter(this.getShippingAgentServiceShippingUnitAdapter());
        this.shippingUnitRecyclerView.setLayoutManager(new LinearLayoutManager(cAppExtension.context));
        this.getShippingAgentServiceShippingUnitAdapter().pFillData(pvDataObl);
    }
}
