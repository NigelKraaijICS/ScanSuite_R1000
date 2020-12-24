package nl.icsvertex.scansuite.Fragments.Dialogs;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;

import ICS.Interfaces.iICSDefaultFragment;
import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cRegex;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Basics.BarcodeLayouts.cBarcodeLayout;
import SSU_WHS.Basics.Workplaces.cWorkplace;
import SSU_WHS.Basics.Workplaces.cWorkplaceAdapter;
import nl.icsvertex.scansuite.Activities.FinishShip.FinishShipLinesActivity;
import nl.icsvertex.scansuite.Activities.PackAndShip.PackAndShipMultiActivity;
import nl.icsvertex.scansuite.Activities.PackAndShip.PackAndShipSingleActivity;
import nl.icsvertex.scansuite.Activities.Pick.PickorderLinesActivity;
import nl.icsvertex.scansuite.Activities.Pick.PickorderLinesGeneratedActivity;
import nl.icsvertex.scansuite.Activities.Ship.ShiporderLinesActivity;
import nl.icsvertex.scansuite.Activities.Sort.SortorderLinesActivity;
import nl.icsvertex.scansuite.R;

public class WorkplaceFragment extends DialogFragment implements iICSDefaultFragment {

    //Region Public Properties


    //End Region Public Properties

    //Region Private Properties
    private RecyclerView workplaceRecyclerView;


    private cWorkplaceAdapter workplaceAdapter;
    private cWorkplaceAdapter getWorkplaceAdapter(){
        if (this.workplaceAdapter == null) {
            this.workplaceAdapter = new cWorkplaceAdapter();
        }

        return  workplaceAdapter;
    }
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
        return rootview;
    }

    @Override
    public void onViewCreated(@NonNull View pvView, @Nullable Bundle pvSavedInstanceState) {
        this.mFragmentInitialize();
    }

    @Override
    public void onPause() {
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
        cAppExtension.dialogFragment = this;


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

        cBarcodeScan.pRegisterBarcodeFragmentReceiver(this.getClass().getSimpleName());
    }

    @Override
    public void mFindViews() {

        if (getView() != null) {
            this.workplaceRecyclerView = getView().findViewById(R.id.workplaceRecyclerview);
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

    public void pHandleScan(cBarcodeScan pvBarcodeScan) {
        String barcodeWithoutPrefixStr ;

        if (cRegex.pHasPrefix(pvBarcodeScan.getBarcodeOriginalStr())) {
            boolean foundBin = false;

            if (cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(), cBarcodeLayout.barcodeLayoutEnu.WORKPLACE)) {
                foundBin = true;
            }

            if (foundBin) {
                //has prefix, is workPlaceStr
                barcodeWithoutPrefixStr = cRegex.pStripRegexPrefixStr(pvBarcodeScan.getBarcodeOriginalStr());
                this.mWorkplaceScanned(barcodeWithoutPrefixStr);
            }
            else {
                //has prefix, isn't workPlaceStr
                cUserInterface.pDoNope(workplaceRecyclerView, true, true);
            }
        }
        else {
            //no prefix, fine
            this.mWorkplaceScanned(pvBarcodeScan.getBarcodeOriginalStr());
        }
    }

    private  void mWorkplaceScanned(String pvWorkplaceStr) {

        cWorkplace Workplace = cWorkplace.pGetWorkplaceByName(pvWorkplaceStr);

        if (Workplace != null) {

            cWorkplace.currentWorkplace = Workplace;

            if (cAppExtension.activity instanceof PickorderLinesActivity) {
                cAppExtension.dialogFragment.dismiss();
                PickorderLinesActivity pickorderLinesActivity = (PickorderLinesActivity)cAppExtension.activity;
                pickorderLinesActivity.pClosePickAndDecideNextStep();
            }

            if (cAppExtension.activity instanceof PickorderLinesGeneratedActivity) {
                cAppExtension.dialogFragment.dismiss();
                PickorderLinesGeneratedActivity pickorderLinesGeneratedActivity = (PickorderLinesGeneratedActivity)cAppExtension.activity;
                pickorderLinesGeneratedActivity.pClosePickAndDecideNextStep();
            }

            if (cAppExtension.activity instanceof SortorderLinesActivity) {
                SortorderLinesActivity sortorderLinesActivity = (SortorderLinesActivity)cAppExtension.activity;
                sortorderLinesActivity.pCloseSortAndDecideNextStep();
            }

            if (cAppExtension.context instanceof ShiporderLinesActivity) {
                ShiporderLinesActivity shiporderLinesActivity = (ShiporderLinesActivity)cAppExtension.activity;
                shiporderLinesActivity.pWorkplaceSelected();
            }

            if (cAppExtension.context instanceof FinishShipLinesActivity) {
                FinishShipLinesActivity finishShipLinesActivity = (FinishShipLinesActivity)cAppExtension.activity;
                finishShipLinesActivity.pWorkplaceSelected();
            }

            if (cAppExtension.context instanceof PackAndShipSingleActivity) {
                PackAndShipSingleActivity packAndShipSingleActivity = (PackAndShipSingleActivity)cAppExtension.activity;
                packAndShipSingleActivity.pWorkplaceSelected(true);
            }

            if (cAppExtension.context instanceof PackAndShipMultiActivity) {
                PackAndShipMultiActivity packAndShipMultiActivity = (PackAndShipMultiActivity)cAppExtension.activity;
                packAndShipMultiActivity.pWorkplaceSelected(true);
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
        this.workplaceRecyclerView.setHasFixedSize(false);
        this.workplaceRecyclerView.setAdapter(this.getWorkplaceAdapter());
        this.workplaceRecyclerView.setLayoutManager(new LinearLayoutManager(cAppExtension.context));
    }

    //End Region Private Methods

}
