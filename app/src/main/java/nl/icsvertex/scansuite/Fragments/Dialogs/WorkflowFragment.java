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
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Basics.Settings.cSetting;
import SSU_WHS.Basics.Workflow.cWorkflowAdapter;
import nl.icsvertex.scansuite.Activities.IntakeAndReceive.IntakeAndReceiveSelectActivity;
import nl.icsvertex.scansuite.Activities.Move.MoveorderSelectActivity;
import nl.icsvertex.scansuite.Activities.PackAndShip.PackAndShipSelectActivity;
import nl.icsvertex.scansuite.Activities.Returns.ReturnorderSelectActivity;
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
        this.mSetWorkflowRecycler();
    }

    @Override
    public void mSetListeners() {

    }

    //End Region iICSDefaultFragment methods

    //Region Private Methods



    private void mSetWorkflowRecycler() {

        if (cAppExtension.activity instanceof MoveorderSelectActivity) {
            this.getWorkflowAdapter().pFillData(cSetting.MOVE_NEW_WORKFLOWS());
        }

        if (cAppExtension.activity instanceof IntakeAndReceiveSelectActivity) {
            this.getWorkflowAdapter().pFillData(cSetting.RECEIVE_NEW_WORKFLOWS());
        }

        if (cAppExtension.activity instanceof ReturnorderSelectActivity) {
            this.getWorkflowAdapter().pFillData(cSetting.RETOUR_NEW_WORKFLOWS());
        }
        if (cAppExtension.activity instanceof PackAndShipSelectActivity) {
            this.getWorkflowAdapter().pFillData(cSetting.PACK_AND_SHIP_NEW_WORKFLOWS());
        }

        this.workflowRecyclerView.setHasFixedSize(false);
        this.workflowRecyclerView.setAdapter(this.getWorkflowAdapter());
        this.workflowRecyclerView.setLayoutManager(new LinearLayoutManager(cAppExtension.context));
    }

    //End Region Private Methods

}
