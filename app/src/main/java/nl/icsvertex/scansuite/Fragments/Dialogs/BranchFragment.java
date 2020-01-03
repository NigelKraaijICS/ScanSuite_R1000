package nl.icsvertex.scansuite.Fragments.Dialogs;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.Objects;

import ICS.Interfaces.iICSDefaultFragment;
import ICS.Utils.Scanning.cBarcodeScan;
import SSU_WHS.Basics.BarcodeLayouts.cBarcodeLayout;
import SSU_WHS.Basics.Branches.cBranch;
import SSU_WHS.Basics.Users.cUser;
import ICS.cAppExtension;
import ICS.Utils.cRegex;
import ICS.Utils.cUserInterface;
import nl.icsvertex.scansuite.R;
import nl.icsvertex.scansuite.Activities.General.LoginActivity;

public class BranchFragment extends DialogFragment implements iICSDefaultFragment {

    //Region Public Properties

    //End Region Public Properties

    //Region Private Properties
    private static RecyclerView branchRecyclerview;
    private static Button buttonClose;

    private static ShimmerFrameLayout shimmerViewContainer;

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
        cBarcodeScan.pRegisterBarcodeFragmentReceiver();
        return rootview;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        cAppExtension.dialogFragment = this;
        this.mFragmentInitialize();
    }

    @Override
    public void onPause() {
        BranchFragment.shimmerViewContainer.stopShimmerAnimation();
        try {
            cBarcodeScan.pUnregisterBarcodeFragmentReceiver();
        } catch (Exception e) {
            e.printStackTrace();
        }

        super.onPause();
    }
    @Override
    public void onDestroy() {
        try {
            cBarcodeScan.pUnregisterBarcodeFragmentReceiver();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    @Override
    public void onResume() {

        super.onResume();
        cBarcodeScan.pRegisterBarcodeFragmentReceiver();
        cUserInterface.pEnableScanner();

        BranchFragment.shimmerViewContainer.startShimmerAnimation();
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
            BranchFragment.branchRecyclerview = getView().findViewById(R.id.branchRecyclerview);
            BranchFragment.buttonClose = getView().findViewById(R.id.buttonClose);
            BranchFragment.shimmerViewContainer = getView().findViewById(R.id.shimmerViewContainer);
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

    public static void pHandleScan(cBarcodeScan pvBarcodeScan){

        String barcodeWithoutPrefixStr;

        //No prefix
        if (!cRegex.hasPrefix(pvBarcodeScan.getBarcodeOriginalStr())) {

            BranchFragment.mBranchScanned(pvBarcodeScan.getBarcodeOriginalStr());
            return;
        }

        boolean foundBln = false;

        if (cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(), cBarcodeLayout.barcodeLayoutEnu.LOCATION)) {
            foundBln = true;
        }

        //has prefix, is branch
        if (foundBln) {
            barcodeWithoutPrefixStr = cRegex.pStripRegexPrefixStr(pvBarcodeScan.getBarcodeOriginalStr());
            BranchFragment.mBranchScanned(barcodeWithoutPrefixStr);
        }
        else {
            //has prefix, isn't branch
            cUserInterface.pDoNope(branchRecyclerview, true, true);
        }
    }
    //End Region Public Methods

    //Region Private Methods

    private static void mBranchScanned(String pvBarcodeStr) {

        cBranch branch = cBranch.pGetBranchByCode(pvBarcodeStr);
        if (branch == null) {
            cUserInterface.pDoNope(branchRecyclerview, true, true);
            return;
        }

        if (cAppExtension.activity instanceof LoginActivity) {
            cUser.currentUser.currentBranch = branch;
            LoginActivity.pBranchSelected(branch);
        }
    }

    private void mFillRecyclerView() {

        BranchFragment.branchRecyclerview.setHasFixedSize(false);
        BranchFragment.branchRecyclerview.setAdapter(cBranch.getBranchAdapter());
        BranchFragment.branchRecyclerview.setLayoutManager(new LinearLayoutManager(cAppExtension.context));

        //Stopping Shimmer Effect's animation after data is loaded
        BranchFragment.shimmerViewContainer.stopShimmerAnimation();
        BranchFragment.shimmerViewContainer.setVisibility(View.GONE);
    }

    private void mSetCloseListener() {
        BranchFragment.buttonClose.setOnClickListener(new View.OnClickListener() {
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


