package nl.icsvertex.scansuite.fragments.dialogs;


import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.List;

import ICS.Interfaces.iICSDefaultFragment;
import SSU_WHS.BarcodeLayouts.cBarcodeLayout;
import SSU_WHS.BarcodeLayouts.cBarcodeLayoutEntity;
import SSU_WHS.BarcodeLayouts.cBarcodeLayoutViewModel;
import SSU_WHS.Workplaces.cWorkplaceAdapter;
import SSU_WHS.Workplaces.cWorkplaceEntity;
import SSU_WHS.Workplaces.cWorkplaceViewModel;
import SSU_WHS.cPublicDefinitions;
import ICS.Utils.Scanning.cBarcodeScanDefinitions;
import ICS.Utils.cRegex;
import ICS.Utils.cSharedPreferences;
import ICS.Utils.cUserInterface;
import nl.icsvertex.scansuite.R;
import nl.icsvertex.scansuite.activities.ship.ShiporderLinesActivity;
import nl.icsvertex.scansuite.activities.sort.SortorderLinesActivity;

public class WorkplaceFragment extends android.support.v4.app.DialogFragment implements iICSDefaultFragment {
    RecyclerView workplaceRecyclerView;
    Button buttonClose;
    DialogFragment thisFragment;
    cWorkplaceViewModel workplaceViewModel;
    cWorkplaceAdapter workplaceAdapter;

    IntentFilter barcodeFragmentIntentFilter;
    private BroadcastReceiver barcodeFragmentReceiver;
    cBarcodeLayoutViewModel barcodeLayoutViewModel;

    Context thisContext;
    String currentBranch;

    public WorkplaceFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_workplaces, container);
        thisFragment = this;
        return rootview;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        thisContext = this.getContext();
        currentBranch = cSharedPreferences.getSharedPreferenceString(cPublicDefinitions.PREFERENCE_CURRENT_BRANCH, "");

        mFragmentInitialize();

        mBarcodeReceiver();

    }
    @Override
    public void onPause() {
        try {
            getActivity().unregisterReceiver(barcodeFragmentReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }

        super.onPause();
    }
    @Override
    public void onDestroy() {
        try {
            getActivity().unregisterReceiver(barcodeFragmentReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
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
        workplaceRecyclerView = getView().findViewById(R.id.workplaceRecyclerview);
        buttonClose = getView().findViewById(R.id.buttonClose);
    }

    @Override
    public void mSetViewModels() {

    }

    @Override
    public void mFieldsInitialize() {
        mGetData();
    }

    @Override
    public void mSetListeners() {
        m_setCloseListener();
    }
    private void mBarcodeReceiver() {
        barcodeFragmentIntentFilter = new IntentFilter();
        for (String str : cBarcodeScanDefinitions.getBarcodeActions()) {
            barcodeFragmentIntentFilter.addAction(str);
        }
        for (String str : cBarcodeScanDefinitions.getBarcodeCategories()) {
            barcodeFragmentIntentFilter.addCategory(str);
        }

        barcodeFragmentReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String barcodeStr = ICS.Utils.Scanning.cBarcodeScan.p_GetBarcode(intent, true);
                if (barcodeStr == null) {
                    barcodeStr = "";
                }
                mHandleScan(barcodeStr);
            }
        };
        //don't forget to unregister on destroy.
        getActivity().registerReceiver(barcodeFragmentReceiver,barcodeFragmentIntentFilter);
    }
    private void mHandleScan(String barcode) {
        String barcodeWithoutPrefixStr = "";
        if (cRegex.hasPrefix(barcode)) {
            String barcodePrefixStr = cRegex.getPrefix(barcode);
            Boolean foundBin = false;
            List<cBarcodeLayoutEntity> workplaceLayouts = barcodeLayoutViewModel.getBarcodeLayoutsOfType(cBarcodeLayout.barcodeLayoutEnu.WORKPLACE.toString());
            for (cBarcodeLayoutEntity layout : workplaceLayouts) {
                if (cRegex.p_checkRegexBln(layout.getLayoutValue(), barcode)) {
                    foundBin = true;
                }
            }
            if (foundBin) {
                //has prefix, is workplace
                barcodeWithoutPrefixStr = cRegex.p_stripRegexPrefixStr(barcode);
                workplaceScanned(barcodeWithoutPrefixStr);
            }
            else {
                //has prefix, isn't workplace
                cUserInterface.doNope(workplaceRecyclerView, true, true);
            }
        }
        else {
            //no prefix, fine
            workplaceScanned(barcode);
        }
    }
    private void workplaceScanned(String barcode) {
        cWorkplaceEntity workplaceEntity = workplaceViewModel.getWorkplaceByCode(barcode);
        if (workplaceEntity != null) {
            Activity activity = getActivity();
            if (activity instanceof SortorderLinesActivity) {
                ((SortorderLinesActivity)getActivity()).setChosenWorkplace(workplaceEntity);
            }
            if (activity instanceof ShiporderLinesActivity) {
                ((ShiporderLinesActivity)getActivity()).setChosenWorkplace(workplaceEntity);
            }
        }
        else {
            cUserInterface.doNope(workplaceRecyclerView, true, true);
        }
    }

    private void m_setCloseListener() {
        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                thisFragment.dismiss();
            }
        });
    }
    private void mGetData() {
        Boolean forceRefresh = false;
        barcodeLayoutViewModel = ViewModelProviders.of(this).get(cBarcodeLayoutViewModel.class);
        workplaceViewModel = ViewModelProviders.of(this).get(cWorkplaceViewModel.class);
        workplaceViewModel.getWorkplaces(forceRefresh, currentBranch).observe(this, new Observer<List<cWorkplaceEntity>>() {
            @Override
            public void onChanged(@Nullable List<cWorkplaceEntity> workplaceEntities) {
                if (workplaceEntities != null) {
                    m_setWorkplaceRecycler(workplaceEntities);
                }
            }
        });
    }
    private void m_setWorkplaceRecycler(List<cWorkplaceEntity> workplaceEntities) {
        workplaceAdapter = new cWorkplaceAdapter(thisContext);
        workplaceRecyclerView.setHasFixedSize(false);
        workplaceRecyclerView.setAdapter(workplaceAdapter);
        workplaceRecyclerView.setLayoutManager(new LinearLayoutManager(thisContext));

        workplaceAdapter.setWorkplaces(workplaceEntities);
    }
    @Override
    public void onResume() {
        getActivity().registerReceiver(barcodeFragmentReceiver, barcodeFragmentIntentFilter);
        super.onResume();
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels - getResources().getDimensionPixelSize(R.dimen.default_double_margin);

        getDialog().getWindow().setLayout(width, height);
    }


}
