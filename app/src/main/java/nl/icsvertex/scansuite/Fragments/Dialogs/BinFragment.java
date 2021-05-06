package nl.icsvertex.scansuite.Fragments.Dialogs;

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

import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.Objects;

import ICS.Interfaces.iICSDefaultFragment;
import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cRegex;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Basics.BarcodeLayouts.cBarcodeLayout;
import SSU_WHS.Basics.BranchBin.cBranchBin;
import SSU_WHS.Basics.BranchBin.cBranchBinAdapter;
import nl.icsvertex.scansuite.Activities.Pick.PickorderLinesGeneratedActivity;
import nl.icsvertex.scansuite.R;

public class BinFragment extends DialogFragment implements iICSDefaultFragment {

    //Region Public Properties

    //End Region Public Properties

    //Region Private Properties
    private  RecyclerView binRecyclerview;
    private  Button buttonClose;

    private  ShimmerFrameLayout shimmerViewContainer;

    private cBranchBinAdapter branchBinAdapter;
    private  cBranchBinAdapter getBranchBinAdapter(){
        if (this.branchBinAdapter == null) {
            this.branchBinAdapter = new cBranchBinAdapter();
        }

        return  branchBinAdapter;
    }

    //End Region Private Properties


    //Region Constructor
    public BinFragment() {

    }
    //End region Constructor


    //Region Default Methods
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_bin, container);
        cBarcodeScan.pRegisterBarcodeFragmentReceiver(this.getClass().getSimpleName());
        return rootview;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        cAppExtension.dialogFragment = this;
        this.mFragmentInitialize();
    }

    @Override
    public void onPause() {
        this.shimmerViewContainer.stopShimmerAnimation();
        try {
            cBarcodeScan.pUnregisterBarcodeFragmentReceiver(this.getClass().getSimpleName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        super.onPause();
    }
    @Override
    public void onDestroy() {
        try {
            cBarcodeScan.pUnregisterBarcodeFragmentReceiver(this.getClass().getSimpleName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    @Override
    public void onResume() {

        super.onResume();
        cBarcodeScan.pRegisterBarcodeFragmentReceiver(this.getClass().getSimpleName());
        cUserInterface.pEnableScanner();

        this.shimmerViewContainer.startShimmerAnimation();
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels - getResources().getDimensionPixelSize(R.dimen.default_double_margin);

        Objects.requireNonNull(Objects.requireNonNull(getDialog()).getWindow()).setLayout(width, height);
    }

    //End Region Default Methods


   //Region iICSDefaultFragment defaults
    @Override
    public void mFragmentInitialize() {
        this.mFindViews();
        this.mFieldsInitialize();
        this.mSetListeners();
    }

    @Override
    public void mFindViews() {
        if (getView() != null) {
            this.binRecyclerview = getView().findViewById(R.id.binRecyclerview);
            this.buttonClose = getView().findViewById(R.id.buttonClose);
            this.shimmerViewContainer = getView().findViewById(R.id.shimmerViewContainer);
        }
    }


    @Override
    public void mFieldsInitialize() {
        this.mFillRecyclerView();
    }

    @Override
    public void mSetListeners() {
        this.mSetCloseListener();
    }

    //End Region iICSDefaultFragment defaults

    //Region Public Methods

    public void pHandleScan(cBarcodeScan pvBarcodeScan){

        String barcodeWithoutPrefixStr;

        //No prefix
        if (!cRegex.pHasPrefix(pvBarcodeScan.getBarcodeOriginalStr())) {
            this.mBINScanned(pvBarcodeScan.getBarcodeOriginalStr());
            return;
        }

        boolean foundBln = false;

        if (cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(), cBarcodeLayout.barcodeLayoutEnu.BIN)) {
            foundBln = true;
        }

        //has prefix, is branch
        if (foundBln) {
            barcodeWithoutPrefixStr = cRegex.pStripRegexPrefixStr(pvBarcodeScan.getBarcodeOriginalStr());
            this.mBINScanned(barcodeWithoutPrefixStr);
        }
        else {
            //has prefix, isn't branch
            cUserInterface.pDoNope(binRecyclerview, true, true);
        }
    }
    //End Region Public Methods

    //Region Private Methods

    private  void mBINScanned(String pvBarcodeStr) {

        cBranchBin branchBin = cBranchBin.pGetBranchBinByCode(pvBarcodeStr);
        if (branchBin == null) {
            cUserInterface.pDoNope(this.binRecyclerview, true, true);
            return;
        }

        if (cAppExtension.activity instanceof PickorderLinesGeneratedActivity) {
            cBranchBin.currentBranchBin = branchBin;
            PickorderLinesGeneratedActivity pickorderLinesGeneratedActivity = (PickorderLinesGeneratedActivity)cAppExtension.activity;
            pickorderLinesGeneratedActivity.pCurrentBranchBinSet();
            dismiss();
        }
    }

    private void mFillRecyclerView() {

        this.binRecyclerview.setHasFixedSize(false);
        this.binRecyclerview.setAdapter(this.getBranchBinAdapter());
        this.binRecyclerview.setLayoutManager(new LinearLayoutManager(cAppExtension.context));

        //Stopping Shimmer Effect's animation after data is loaded
        this.shimmerViewContainer.stopShimmerAnimation();
        this.shimmerViewContainer.setVisibility(View.GONE);



    }

    private void mSetCloseListener() {
        this.buttonClose.setOnClickListener(view -> {
            if (cAppExtension.dialogFragment != null) {
                cAppExtension.dialogFragment.dismiss();
            }
        });
    }

    //End Region Private Methods










}


