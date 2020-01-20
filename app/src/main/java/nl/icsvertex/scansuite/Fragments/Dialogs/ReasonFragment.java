package nl.icsvertex.scansuite.Fragments.Dialogs;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import SSU_WHS.Basics.BranchReason.cBranchReason;
import SSU_WHS.Basics.Users.cUser;
import nl.icsvertex.scansuite.Activities.Returns.CreateReturnActivity;
import nl.icsvertex.scansuite.Fragments.Returns.ReturnArticleDetailFragment;
import nl.icsvertex.scansuite.R;

public class ReasonFragment extends DialogFragment implements iICSDefaultFragment {

    //Region Public Properties


    //End Region Public Properties

    //Region Private Properties
    private static RecyclerView reasonRecyclerView;
    private static Button buttonClose;
    private static ShimmerFrameLayout shimmerViewContainer;
    //End Region Private Properties


    //Region Constructor
    public ReasonFragment() {

    }
    //End Region Constructor


    //Region Default Methods
    @Nullable
    @Override
    public View onCreateView(LayoutInflater pvInflater, ViewGroup pvContainer, Bundle pvSavedInstanceState) {
        View rootview = pvInflater.inflate(R.layout.fragment_reason, pvContainer);
        cBarcodeScan.pRegisterBarcodeFragmentReceiver();

        return rootview;
    }

    @Override
    public void onViewCreated(@NonNull View pvView, @Nullable Bundle pvSavedInstanceState) {
        this.mFragmentInitialize();
        cAppExtension.dialogFragment = this;
    }

    @Override
    public void onPause() {
        try {
            shimmerViewContainer.stopShimmerAnimation();
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

        ReasonFragment.shimmerViewContainer.startShimmerAnimation();
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels - getResources().getDimensionPixelSize(R.dimen.default_double_margin);

        Objects.requireNonNull(Objects.requireNonNull(getDialog()).getWindow()).setLayout(width, height);
    }

    //End Region Default Methods

    //Region iICSDefaultFragment methods
    @Override
    public void mFragmentInitialize() {
        this.mFindViews();
        this.mFieldsInitialize();
        this.mSetListeners();
    }

    @Override
    public void mFindViews() {

        if (getView() != null) {
            ReasonFragment.reasonRecyclerView = getView().findViewById(R.id.reasonRecyclerview);
            ReasonFragment.buttonClose = getView().findViewById(R.id.buttonClose);
            ReasonFragment.shimmerViewContainer = getView().findViewById(R.id.shimmerViewContainer);
        }

    }

    @Override
    public void mFieldsInitialize() {
        this.mGetData();
    }

    @Override
    public void mSetListeners() {
        this.mSetCloseListener();
    }

    //End Region iICSDefaultFragment methods

    //Region Private Methods

    public static void pHandleScan(cBarcodeScan pvBarcodeScan) {
        String barcodeWithoutPrefixStr ;
        if (cRegex.hasPrefix(pvBarcodeScan.getBarcodeOriginalStr())) {
            boolean foundBin = false;

            if (cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(), cBarcodeLayout.barcodeLayoutEnu.REASON)) {
                foundBin = true;
            }

            if (foundBin) {
                //has prefix, is workPlaceStr
                barcodeWithoutPrefixStr = cRegex.pStripRegexPrefixStr(pvBarcodeScan.getBarcodeOriginalStr());
                mReasonScanned(barcodeWithoutPrefixStr);
            }
            else {
                //has prefix, isn't workPlaceStr
                cUserInterface.pDoNope(reasonRecyclerView, true, true);
            }
        }
        else {
            //no prefix, fine
            mReasonScanned(pvBarcodeScan.getBarcodeOriginalStr());
        }
    }

    private static void mReasonScanned(String pvBarcodeStr) {

        cBranchReason branchReason = cUser.currentUser.currentBranch.pGetReasonByName(pvBarcodeStr);

        if (branchReason != null) {

            cBranchReason.currentBranchReason = branchReason;

            if (cAppExtension.activity instanceof CreateReturnActivity) {
                CreateReturnActivity.pSetReason();
                CreateReturnActivity.pHandleFragmentDismissed();
                cAppExtension.dialogFragment.dismiss();
            }
            else {
                ReturnArticleDetailFragment.pSetReason();
                cAppExtension.dialogFragment.dismiss();

                Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    public void run() {
                        ReturnArticleDetailFragment.pHandleFragmentDismissed();
                        // Actions to do after 0.2 seconds
                    }
                }, 200);
            }
        }
        else {
            cUserInterface.pDoNope(reasonRecyclerView, true, true);
        }
    }

    private void mGetData() {

        boolean webserviceResult = true;
        if (cUser.currentUser.currentBranch.returnReasonObl == null){
            webserviceResult = cUser.currentUser.currentBranch.pGetReasonBln(true);
        }

        if (webserviceResult) {
            mSetReasonRecycler();
        }
    }

    private void mSetReasonRecycler() {
        ReasonFragment.reasonRecyclerView.setHasFixedSize(false);
        ReasonFragment.reasonRecyclerView.setAdapter(cBranchReason.getBranchReasonAdapter());
        ReasonFragment.reasonRecyclerView.setLayoutManager(new LinearLayoutManager(cAppExtension.context));
        //Stopping Shimmer Effect's animation after data is loaded
        ReasonFragment.shimmerViewContainer.stopShimmerAnimation();
        ReasonFragment.shimmerViewContainer.setVisibility(View.GONE);
    }

    private void mSetCloseListener() {
        ReasonFragment.buttonClose.setOnClickListener(new View.OnClickListener() {
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
