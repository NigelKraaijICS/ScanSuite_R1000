package nl.icsvertex.scansuite.fragments.ship;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.List;

import SSU_WHS.ShippingAgentServiceShippingUnits.cShippingAgentServiceShippingUnitAdapter;
import SSU_WHS.ShippingAgentServiceShippingUnits.cShippingAgentServiceShippingUnitEntity;
import SSU_WHS.ShippingAgentServiceShippingUnits.cShippingAgentServiceShippingUnitViewModel;
import nl.icsvertex.scansuite.activities.ship.ShiporderSelectActivity;
import nl.icsvertex.scansuite.R;

import static SSU_WHS.cPublicDefinitions.SHIPPINGUNITFRAGMENT_SHIPPINGAGENT;
import static SSU_WHS.cPublicDefinitions.SHIPPINGUNITFRAGMENT_SHIPPINGSERVICE;

public class ShippingUnitFragment extends android.support.v4.app.DialogFragment {
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
        shippingAgent = args.getString(SHIPPINGUNITFRAGMENT_SHIPPINGAGENT);
        shippingAgentService = args.getString(SHIPPINGUNITFRAGMENT_SHIPPINGSERVICE);
        thisContext = this.getContext();
        shippingAgentServiceShippingUnitViewModel = ViewModelProviders.of(this).get(cShippingAgentServiceShippingUnitViewModel.class);

        mFindViews();
        mGetData();
        mSetListeners();
    }
    @Override
    public void onResume() {
        super.onResume();
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels - getResources().getDimensionPixelSize(R.dimen.default_double_margin);

        getDialog().getWindow().setLayout(width, height);
    }
    private void mFindViews() {
        buttonClose = getView().findViewById(R.id.buttonClose);
        buttonDone = getView().findViewById(R.id.buttonDone);
        shippingUnitRecyclerView = getView().findViewById(R.id.shippingUnitRecyclerView);
        shippingUnitRecyclerView.setHapticFeedbackEnabled(true);
    }
    private void mGetData() {
        List<cShippingAgentServiceShippingUnitEntity> shippingUnits = shippingAgentServiceShippingUnitViewModel.getShippingUnitsByAgentAndService(shippingAgent, shippingAgentService);
        mFillShippingUnitsRecycler(shippingUnits);

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
    private void mSetListeners() {
        mSetCloseListener();
        mSetOKListener();
    }

    private void mSetCloseListener() {
        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getContext() instanceof ShiporderSelectActivity) {
                    ((ShiporderSelectActivity) getContext()).resetShippingUnitQuantityUsed();
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
        shippingAgentServiceShippingUnitAdapter = new cShippingAgentServiceShippingUnitAdapter(thisContext);
        ((SimpleItemAnimator)shippingUnitRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        shippingUnitRecyclerView.setHasFixedSize(false);
        shippingUnitRecyclerView.setAdapter(shippingAgentServiceShippingUnitAdapter);
        shippingUnitRecyclerView.setLayoutManager(new LinearLayoutManager(thisContext));

        shippingAgentServiceShippingUnitAdapter.setShippingUnits(shippingAgentServiceShippingUnitEntities);
    }
}
