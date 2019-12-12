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
    private Button buttonClose;

    private ShimmerFrameLayout shimmerViewContainer;

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
        shimmerViewContainer.stopShimmerAnimation();
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

        this.shimmerViewContainer.startShimmerAnimation();
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels - getResources().getDimensionPixelSize(R.dimen.default_double_margin);

        getDialog().getWindow().setLayout(width, height);
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
        this.branchRecyclerview = getView().findViewById(R.id.branchRecyclerview);
        this.buttonClose = getView().findViewById(R.id.buttonClose);
        this.shimmerViewContainer = getView().findViewById(R.id.shimmerViewContainer);
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

        Boolean foundBln = false;

        if (cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(),cBarcodeLayout.barcodeLayoutEnu.LOCATION) == true) {
            foundBln = true;
        }

        //has prefix, is branch
        if (foundBln) {
            barcodeWithoutPrefixStr = cRegex.pStripRegexPrefixStr(pvBarcodeScan.getBarcodeOriginalStr());
            BranchFragment.mBranchScanned(barcodeWithoutPrefixStr);
            return;
        }
        else {
            //has prefix, isn't branch
            cUserInterface.pDoNope(branchRecyclerview, true, true);
            return;
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

        this.branchRecyclerview.setHasFixedSize(false);
        this.branchRecyclerview.setAdapter(cBranch.getBranchAdapter());
        this.branchRecyclerview.setLayoutManager(new LinearLayoutManager(cAppExtension.context));

        //Stopping Shimmer Effect's animation after data is loaded
        this.shimmerViewContainer.stopShimmerAnimation();
        this.shimmerViewContainer.setVisibility(View.GONE);
    }

    private void mSetCloseListener() {
        buttonClose.setOnClickListener(new View.OnClickListener() {
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


