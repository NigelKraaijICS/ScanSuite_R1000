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
import SSU_WHS.Basics.Settings.cSetting;
import SSU_WHS.Basics.Workflow.cWorkflowAdapter;
import SSU_WHS.Basics.Workplaces.cWorkplace;
import nl.icsvertex.scansuite.Activities.FinishShip.FinishShipLinesActivity;
import nl.icsvertex.scansuite.Activities.IntakeAndReceive.IntakeAndReceiveSelectActivity;
import nl.icsvertex.scansuite.Activities.Move.MoveorderSelectActivity;
import nl.icsvertex.scansuite.Activities.Pick.PickorderLinesActivity;
import nl.icsvertex.scansuite.Activities.Pick.PickorderLinesGeneratedActivity;
import nl.icsvertex.scansuite.Activities.Returns.ReturnorderSelectActivity;
import nl.icsvertex.scansuite.Activities.Ship.ShiporderLinesActivity;
import nl.icsvertex.scansuite.Activities.Sort.SortorderLinesActivity;
import nl.icsvertex.scansuite.R;

public class WorkflowFragment extends DialogFragment implements iICSDefaultFragment {

    //Region Public Properties


    //End Region Public Properties

    //Region Private Properties
    private RecyclerView workflowRecyclerView;


    private cWorkflowAdapter workflowAdapter;
    private cWorkflowAdapter getWorkflowAdapter(){
        if (this.workflowAdapter == null) {
            this.workflowAdapter = new cWorkflowAdapter();
        }

        return  workflowAdapter;
    }
    //End Region Private Properties


    //Region Constructor
    public WorkflowFragment() {

    }
    //End Region Constructor


    //Region Default Methods
    @Nullable
    @Override
    public View onCreateView(LayoutInflater pvInflater, ViewGroup pvContainer, Bundle pvSavedInstanceState) {
        return pvInflater.inflate(R.layout.fragment_workflows, pvContainer);
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
            this.workflowRecyclerView = getView().findViewById(R.id.workflowsRecyclerview);
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
                cUserInterface.pDoNope(workflowRecyclerView, true, true);
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

        }
        else {
            cUserInterface.pDoNope(workflowRecyclerView, true, true);
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

        if (cAppExtension.activity instanceof MoveorderSelectActivity) {
            this.getWorkflowAdapter().pFillData(cSetting.MOVE_NEW_WORKFLOWS());
        }

        if (cAppExtension.activity instanceof IntakeAndReceiveSelectActivity) {
            this.getWorkflowAdapter().pFillData(cSetting.RECEIVE_NEW_WORKFLOWS());
        }

        if (cAppExtension.activity instanceof ReturnorderSelectActivity) {
            this.getWorkflowAdapter().pFillData(cSetting.RETOUR_NEW_WORKFLOWS());
        }

        this.workflowRecyclerView.setHasFixedSize(false);
        this.workflowRecyclerView.setAdapter(this.getWorkflowAdapter());
        this.workflowRecyclerView.setLayoutManager(new LinearLayoutManager(cAppExtension.context));
    }

    //End Region Private Methods

}
