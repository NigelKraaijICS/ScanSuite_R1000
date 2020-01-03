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

import java.util.Objects;

import ICS.Interfaces.iICSDefaultFragment;
import ICS.Utils.Scanning.cBarcodeScan;
import SSU_WHS.Basics.BarcodeLayouts.cBarcodeLayout;
import SSU_WHS.Basics.Workplaces.cWorkplace;
import nl.icsvertex.scansuite.Activities.Pick.PickorderLinesActivity;
import nl.icsvertex.scansuite.Activities.Ship.ShiporderLinesActivity;
import nl.icsvertex.scansuite.Activities.Sort.SortorderLinesActivity;
import ICS.cAppExtension;
import ICS.Utils.cRegex;
import ICS.Utils.cUserInterface;
import nl.icsvertex.scansuite.R;

public class WorkplaceFragment extends DialogFragment implements iICSDefaultFragment {

    //Region Public Properties


    //End Region Public Properties

    //Region Private Properties
    private static RecyclerView workplaceRecyclerView;
    //End Region Private Properties


    //Region Constructor
    public WorkplaceFragment() {

    }
    //End Region Constructor


    //Region Default Methods
    @Nullable
    @Override
    public View onCreateView(LayoutInflater pvInflater, ViewGroup pvContainer, Bundle pvSavedInstanceState) {
        View rootview = pvInflater.inflate(R.layout.fragment_workplaces, pvContainer);
        cAppExtension.dialogFragment = this;
        return rootview;
    }

    @Override
    public void onViewCreated(@NonNull View pvView, @Nullable Bundle pvSavedInstanceState) {
        this.mFragmentInitialize();
    }

    @Override
    public void onPause() {
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

        cBarcodeScan.pRegisterBarcodeFragmentReceiver();
    }

    @Override
    public void mFindViews() {

        if (getView() != null) {
            WorkplaceFragment.workplaceRecyclerView = getView().findViewById(R.id.workplaceRecyclerview);
        }

    }


    @Override
    public void mFieldsInitialize() {
        this.mGetData();
    }

    @Override
    public void mSetListeners() {

    }

    //End Region iICSDefaultFragment methods

    //Region Private Methods

    public static void pHandleScan(cBarcodeScan pvBarcodeScan) {
        String barcodeWithoutPrefixStr ;

        if (cRegex.hasPrefix(pvBarcodeScan.getBarcodeOriginalStr())) {
            boolean foundBin = false;

            if (cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(), cBarcodeLayout.barcodeLayoutEnu.WORKPLACE)) {
                foundBin = true;
            }

            if (foundBin) {
                //has prefix, is workPlaceStr
                barcodeWithoutPrefixStr = cRegex.pStripRegexPrefixStr(pvBarcodeScan.getBarcodeOriginalStr());
                WorkplaceFragment.mWorkplaceScanned(barcodeWithoutPrefixStr);
            }
            else {
                //has prefix, isn't workPlaceStr
                cUserInterface.pDoNope(workplaceRecyclerView, true, true);
            }
        }
        else {
            //no prefix, fine
            WorkplaceFragment.mWorkplaceScanned(pvBarcodeScan.getBarcodeOriginalStr());
        }
    }

    private static void mWorkplaceScanned(String pvWorkplaceStr) {

        cWorkplace Workplace = cWorkplace.pGetWorkplaceByName(pvWorkplaceStr);

        if (Workplace != null) {

            cWorkplace.currentWorkplace = Workplace;

            if (cAppExtension.activity instanceof PickorderLinesActivity) {
                cAppExtension.dialogFragment.dismiss();
                PickorderLinesActivity.pClosePickAndDecideNextStep();
            }

            if (cAppExtension.activity instanceof SortorderLinesActivity) {
                SortorderLinesActivity.pCloseSortAndDecideNextStep();
            }

            if (cAppExtension.activity instanceof ShiporderLinesActivity) {
                cAppExtension.dialogFragment.dismiss();
            }

        }
        else {
            cUserInterface.pDoNope(workplaceRecyclerView, true, true);
        }
    }

    private void mGetData() {

        boolean webserviceResult;
        webserviceResult = cWorkplace.pGetWorkplacesViaWebserviceBln();

        if (webserviceResult) {
            this.mSetWorkplaceRecycler();
        }
    }

    private void mSetWorkplaceRecycler() {
        WorkplaceFragment.workplaceRecyclerView.setHasFixedSize(false);
        WorkplaceFragment.workplaceRecyclerView.setAdapter(cWorkplace.getWorkplaceAdapter());
        WorkplaceFragment.workplaceRecyclerView.setLayoutManager(new LinearLayoutManager(cAppExtension.context));
    }

    //End Region Private Methods





}
