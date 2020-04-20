package nl.icsvertex.scansuite.Fragments.Packaging;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ICS.Interfaces.iICSDefaultFragment;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Basics.Packaging.cPackaging;
import SSU_WHS.Basics.Packaging.cPackagingInAdapter;
import SSU_WHS.Intake.Intakeorders.cIntakeorder;
import nl.icsvertex.scansuite.Activities.Packaging.PackagingActivity;
import nl.icsvertex.scansuite.R;


public class PackagingInFragment extends  Fragment  implements iICSDefaultFragment{

    //Region Public Properties

    //End Region Public Properties

    //Region Private Properties


    private ConstraintLayout fragmentPackagingIn;

    private RecyclerView recyclerUnitsUsedIn;

    private cPackagingInAdapter packagingInAdapter;
    private cPackagingInAdapter getPackagingInAdapter(){
        if (this.packagingInAdapter == null) {
            this.packagingInAdapter = new cPackagingInAdapter();
        }
        return  this.packagingInAdapter;
    }


    //End Region Private Properties

    //Region Constructor
    public PackagingInFragment() {

    }
    //End Region Constructor

    //Region Default Methods

    @Override
    public View onCreateView(LayoutInflater pvInflater, ViewGroup pvContainer,
                             Bundle pvSavedInstanceState) {

        // Inflate the layout for this fragment
        return pvInflater.inflate(R.layout.fragment_packaging_in, pvContainer, false);

    }

    @Override
    public void onViewCreated(@NonNull View pvView, @Nullable Bundle pvSavedInstanceState) {



    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        cUserInterface.pEnableScanner();
        PackagingActivity.currentPackagingFragment = this;
        this.mFragmentInitialize();
    }

    //End Region Default Methods

    //Region iICSDefaultFragment defaults
    @Override
    public void mFragmentInitialize() {
        this.mFindViews();
        this.mFieldsInitialize();
        this.mSetListeners();
        this.mGetData();
        cUserInterface.pEnableScanner();

    }

    @Override
    public void mFindViews() {

        if (getView() != null) {
            this.recyclerUnitsUsedIn = getView().findViewById(R.id.recyclerUnitsUsedIn);
            this.fragmentPackagingIn = getView().findViewById(R.id.fragmentPackagingIn);
        }

    }


    @Override
    public void mFieldsInitialize() {

    }

    @Override
    public void mSetListeners() {

    }

    //End Region iICSDefaultFragment defaults

    //Region Public Methods


    //End Region Public Methods

    //Region Private Methods


    private void mGetData() {

        List<cPackaging> localPackagesObl = cIntakeorder.currentIntakeOrder.packagingInObl;

        if (localPackagesObl.size()>0) {
            this.fragmentPackagingIn.setVisibility(View.VISIBLE);
            this.mFieldsInitialize();
        }
        else {
            this.fragmentPackagingIn.setVisibility(View.INVISIBLE);
        }

        this.mFillRecycler(localPackagesObl);

    }

    private void mFillRecycler(List<cPackaging> pvDataObl) {

        //Show the recycler view
        this.getPackagingInAdapter().pFillData(pvDataObl);
        this.recyclerUnitsUsedIn.setHasFixedSize(false);
        this.recyclerUnitsUsedIn.setAdapter(this.getPackagingInAdapter());
        this.recyclerUnitsUsedIn.setLayoutManager(new LinearLayoutManager(cAppExtension.context));
        this.recyclerUnitsUsedIn.setVisibility(View.VISIBLE);


    }


    //End Region private Methods


}
