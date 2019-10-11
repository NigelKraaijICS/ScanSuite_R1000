package nl.icsvertex.scansuite.Fragments.ship;

import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.List;

import ICS.Interfaces.iICSDefaultFragment;
import SSU_WHS.Basics.ShippingAgentServiceShippingUnits.cShippingAgentServiceShippingUnitAdapter;
import SSU_WHS.Basics.ShippingAgentServiceShippingUnits.cShippingAgentServiceShippingUnitEntity;
import SSU_WHS.Basics.ShippingAgentServiceShippingUnits.cShippingAgentServiceShippingUnitViewModel;
import nl.icsvertex.scansuite.Activities.ship.ShiporderSelectActivity;
import nl.icsvertex.scansuite.R;


public class ShippingUnitFragment extends DialogFragment implements iICSDefaultFragment {
    Context thisContext;
    String shippingAgent;
    String shippingAgentService;
    cShippingAgentServiceShippingUnitAdapter shippingAgentServiceShippingUnitAdapter;
    cShippingAgentServiceShippingUnitViewModel shippingAgentServiceShippingUnitViewModel;
    RecyclerView shippingUnitRecyclerView;
    Button buttonClose;
    Button buttonDone;

    public static ShippingUnitFragment newInstance() {
        return new ShippingUnitFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_shipping_unit, container, false);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle args = getArguments();
        thisContext = this.getContext();
        shippingAgentServiceShippingUnitViewModel = ViewModelProviders.of(this).get(cShippingAgentServiceShippingUnitViewModel.class);

        mFragmentInitialize();
    }
    @Override
    public void onResume() {
        super.onResume();
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels - getResources().getDimensionPixelSize(R.dimen.default_double_margin);

        getDialog().getWindow().setLayout(width, height);
    }
    @Override
    public void mFragmentInitialize() {
        mFindViews();
        mSetViewModels();
        mFieldsInitialize();
        mSetListeners();
    }
    @Override
    public void mFindViews() {
        buttonClose = getView().findViewById(R.id.buttonClose);
        buttonDone = getView().findViewById(R.id.buttonDone);
        shippingUnitRecyclerView = getView().findViewById(R.id.shippingUnitRecyclerView);
        shippingUnitRecyclerView.setHapticFeedbackEnabled(true);
    }
    @Override
    public void mFieldsInitialize() {
        mGetData();
    }
    @Override
    public void mSetViewModels() {

    }
    @Override
    public void mSetListeners() {
        mSetCloseListener();
        mSetOKListener();
    }
    private void mGetData() {

        //todo: do this, but via object
        //List<cShippingAgentServiceShippingUnitEntity> shippingUnits = shippingAgentServiceShippingUnitViewModel.getShippingUnitsByAgentAndService(shippingAgent, shippingAgentService);
       //mFillShippingUnitsRecycler(shippingUnits);

        //livedata doesn't work here because of the numberchoosers, would be cool if it did.
//        shippingAgentServiceShippingUnitViewModel.getShippingUnitsByAgentAndService(shippingAgent, shippingAgentService).observe(getViewLifecycleOwner(), new Observer<List<cShippingAgentServiceShippingUnitEntity>>() {
//            @Override
//            public void onChanged(@Nullable List<cShippingAgentServiceShippingUnitEntity> shippingAgentServiceShippingUnitEntities) {
//                if (shippingAgentServiceShippingUnitEntities != null) {
//                    mFillShippingUnitsRecycler(shippingAgentServiceShippingUnitEntities);
//                }
//            }
//        });
    }


    private void mSetCloseListener() {
        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getContext() instanceof ShiporderSelectActivity) {
//              todo: put this back
//                    ((ShiporderSelectActivity) getContext()).resetShippingUnitQuantityUsed();
                }
                dismiss();
            }
        });
    }
    private void mSetOKListener() {
        buttonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }
    private void mFillShippingUnitsRecycler(List<cShippingAgentServiceShippingUnitEntity> shippingAgentServiceShippingUnitEntities) {
        shippingAgentServiceShippingUnitAdapter = new cShippingAgentServiceShippingUnitAdapter();
        ((SimpleItemAnimator)shippingUnitRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        shippingUnitRecyclerView.setHasFixedSize(false);
        shippingUnitRecyclerView.setAdapter(shippingAgentServiceShippingUnitAdapter);
        shippingUnitRecyclerView.setLayoutManager(new LinearLayoutManager(thisContext));

        shippingAgentServiceShippingUnitAdapter.setShippingUnits(shippingAgentServiceShippingUnitEntities);
    }
}
