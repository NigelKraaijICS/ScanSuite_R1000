package nl.icsvertex.scansuite.fragments.dialogs;


import android.app.Activity;
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

import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.List;

import ICS.Interfaces.iICSDefaultFragment;
import SSU_WHS.BarcodeLayouts.cBarcodeLayout;
import SSU_WHS.BarcodeLayouts.cBarcodeLayoutEntity;
import SSU_WHS.BarcodeLayouts.cBarcodeLayoutViewModel;
import SSU_WHS.Branches.cBranchAdapter;
import SSU_WHS.Branches.cBranchEntity;
import SSU_WHS.Branches.cBranchViewModel;
import SSU_WHS.cAppExtension;
import SSU_WHS.cPublicDefinitions;
import ICS.Utils.Scanning.cBarcodeScanDefinitions;
import ICS.Utils.cRegex;
import ICS.Utils.cUserInterface;
import nl.icsvertex.scansuite.R;
import nl.icsvertex.scansuite.activities.general.LoginActivity;

public class BranchFragment extends android.support.v4.app.DialogFragment implements iICSDefaultFragment {
    RecyclerView branchRecyclerview;
    Button buttonClose;
    DialogFragment thisFragment = null;
    cBranchViewModel branchViewModel;
    cBranchAdapter branchAdapter;
    List<cBranchEntity> branches;
    ShimmerFrameLayout shimmerViewContainer;

    IntentFilter barcodeFragmentIntentFilter;
    private BroadcastReceiver barcodeFragmentReceiver;
    cBarcodeLayoutViewModel barcodeLayoutViewModel;


    public BranchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_branch, container);
        return rootview;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        thisFragment = this;
        Bundle bundle = this.getArguments();
        branches = (List<cBranchEntity>)bundle.getSerializable(cPublicDefinitions.BRANCHFRAGMENT_LIST_TAG);

        mFragmentInitialize();

        mBarcodeReceiver();

    }

    @Override
    public void onPause() {
        shimmerViewContainer.stopShimmerAnimation();
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
        branchRecyclerview = getView().findViewById(R.id.branchRecyclerview);
        buttonClose = getView().findViewById(R.id.buttonClose);
        shimmerViewContainer = getView().findViewById(R.id.shimmerViewContainer);
    }

    @Override
    public void mSetViewModels() {
        barcodeLayoutViewModel = ViewModelProviders.of(this).get(cBarcodeLayoutViewModel.class);
        branchViewModel = ViewModelProviders.of(this).get(cBranchViewModel.class);
    }

    @Override
    public void mFieldsInitialize() {
        mFillRecyclerView();
    }

    @Override
    public void mSetListeners() {
        mSetCloseListener();
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
    private void mSetCloseListener() {
        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (thisFragment != null) {
                    thisFragment.dismiss();
                }
            }
        });
    }
    private void mFillRecyclerView() {
        branchAdapter = new cBranchAdapter(cAppExtension.context);
        branchRecyclerview.setHasFixedSize(false);
        branchRecyclerview.setAdapter(branchAdapter);
        branchRecyclerview.setLayoutManager(new LinearLayoutManager(cAppExtension.context));

        branchAdapter.setBranches(branches);
            //Stopping Shimmer Effect's animation after data is loaded
            shimmerViewContainer.stopShimmerAnimation();
            shimmerViewContainer.setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        getActivity().registerReceiver(barcodeFragmentReceiver, barcodeFragmentIntentFilter);
        super.onResume();
        shimmerViewContainer.startShimmerAnimation();
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels - getResources().getDimensionPixelSize(R.dimen.default_double_margin);

        getDialog().getWindow().setLayout(width, height);
    }
    private void mHandleScan(String barcode) {
        String barcodeWithoutPrefixStr = "";
        if (cRegex.hasPrefix(barcode)) {
            String barcodePrefixStr = cRegex.getPrefix(barcode);
            Boolean foundBin = false;
            List<cBarcodeLayoutEntity> locationLayouts = barcodeLayoutViewModel.getBarcodeLayoutsOfType(cBarcodeLayout.barcodeLayoutEnu.LOCATION.toString());
            for (cBarcodeLayoutEntity layout : locationLayouts) {
                if (cRegex.p_checkRegexBln(layout.getLayoutValue(), barcode)) {
                    foundBin = true;
                }
            }
            if (foundBin) {
                //has prefix, is location
                barcodeWithoutPrefixStr = cRegex.p_stripRegexPrefixStr(barcode);
                branchScanned(barcodeWithoutPrefixStr);
            }
            else {
                //has prefix, isn't location
                cUserInterface.doNope(branchRecyclerview, true, true);
            }
        }
        else {
            //no prefix, fine
            branchScanned(barcode);
        }
    }
    private void branchScanned(String barcode) {
        cBranchEntity branchEntity = branchViewModel.getBranchByCode(barcode);
        if (branchEntity != null) {
            Activity activity = getActivity();
            if (activity instanceof LoginActivity) {
                ((LoginActivity)getActivity()).setChosenBranch(branchEntity);
            }
        }
        else {
            cUserInterface.doNope(branchRecyclerview, true, true);
        }

    }

}
