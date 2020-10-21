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
import SSU_WHS.Basics.Branches.cBranch;
import SSU_WHS.Basics.Branches.cBranchAdapter;
import SSU_WHS.Basics.Users.cUser;
import nl.icsvertex.scansuite.Activities.General.LoginActivity;
import nl.icsvertex.scansuite.R;

public class BranchFragment extends DialogFragment implements iICSDefaultFragment {

    //Region Public Properties

    //End Region Public Properties

    //Region Private Properties
    private  RecyclerView branchRecyclerview;
    private  Button buttonClose;

    private  ShimmerFrameLayout shimmerViewContainer;

    private cBranchAdapter branchAdapter;
    private  cBranchAdapter getBranchAdapter(){
        if (this.branchAdapter == null) {
            this.branchAdapter = new cBranchAdapter();
        }

        return  branchAdapter;
    }

    //End Region Private Properties


    //Region Constructor
    public BranchFragment() {

    }
    //End region Constructor


    //Region Default Methods
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_branch, container);
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
            this.branchRecyclerview = getView().findViewById(R.id.branchRecyclerview);
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
            this.mBranchScanned(pvBarcodeScan.getBarcodeOriginalStr());
            return;
        }

        boolean foundBln = false;

        if (cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(), cBarcodeLayout.barcodeLayoutEnu.LOCATION)) {
            foundBln = true;
        }

        //has prefix, is branch
        if (foundBln) {
            barcodeWithoutPrefixStr = cRegex.pStripRegexPrefixStr(pvBarcodeScan.getBarcodeOriginalStr());
            this.mBranchScanned(barcodeWithoutPrefixStr);
        }
        else {
            //has prefix, isn't branch
            cUserInterface.pDoNope(branchRecyclerview, true, true);
        }
    }
    //End Region Public Methods

    //Region Private Methods

    private  void mBranchScanned(String pvBarcodeStr) {

        cBranch branch = cBranch.pGetUserBranchByCode(pvBarcodeStr);
        if (branch == null) {
            cUserInterface.pDoNope(this.branchRecyclerview, true, true);
            return;
        }

        if (cAppExtension.activity instanceof LoginActivity) {
            cUser.currentUser.currentBranch = branch;
            LoginActivity loginActivity = (LoginActivity)cAppExtension.activity;
            loginActivity.pBranchSelected(branch);
        }
    }

    private void mFillRecyclerView() {

        this.branchRecyclerview.setHasFixedSize(false);
        this.branchRecyclerview.setAdapter(this.getBranchAdapter());
        this.branchRecyclerview.setLayoutManager(new LinearLayoutManager(cAppExtension.context));

        //Stopping Shimmer Effect's animation after data is loaded
        this.shimmerViewContainer.stopShimmerAnimation();
        this.shimmerViewContainer.setVisibility(View.GONE);
    }

    private void mSetCloseListener() {
        this.buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cAppExtension.dialogFragment != null) {
                    cAppExtension.dialogFragment.dismiss();
                }
            }
        });
    }

    //End Region Private Methods










}


