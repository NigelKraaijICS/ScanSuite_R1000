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
import SSU_WHS.Basics.Packaging.cPackagingOutAdapter;
import SSU_WHS.Intake.Intakeorders.cIntakeorder;
import nl.icsvertex.scansuite.Activities.Packaging.PackagingActivity;
import nl.icsvertex.scansuite.R;


public class PackagingOutFragment extends  Fragment  implements iICSDefaultFragment{

    //Region Public Properties

    //End Region Public Properties

    //Region Private Properties


    private ConstraintLayout fragmentPackagingOut;

    private RecyclerView recyclerUnitsUsedOut;

    private cPackagingOutAdapter packagingOutAdapter;
    private cPackagingOutAdapter getPackagingOutAdapter(){
        if (this.packagingOutAdapter == null) {
            this.packagingOutAdapter = new cPackagingOutAdapter();
        }
        return  this.packagingOutAdapter;
    }


    //End Region Private Properties

    //Region Constructor
    public PackagingOutFragment() {

    }
    //End Region Constructor

    //Region Default Methods

    @Override
    public View onCreateView(LayoutInflater pvInflater, ViewGroup pvContainer,
                             Bundle pvSavedInstanceState) {

        // Inflate the layout for this fragment
        return pvInflater.inflate(R.layout.fragment_packaging_out, pvContainer, false);

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
            this.recyclerUnitsUsedOut = getView().findViewById(R.id.recyclerUnitsUsedOut);
            this.fragmentPackagingOut = getView().findViewById(R.id.fragmentPackagingOut);
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

        List<cPackaging> localPackagesObl = cIntakeorder.currentIntakeOrder.packagingOutObl;

        if (localPackagesObl.size()>0) {
            this.fragmentPackagingOut.setVisibility(View.VISIBLE);
            this.mFieldsInitialize();
        }
        else {
            this.fragmentPackagingOut.setVisibility(View.INVISIBLE);
    }

        this.mFillRecycler(localPackagesObl);

}

    private void mFillRecycler(List<cPackaging> pvDataObl) {

        //Show the recycler view
        this.getPackagingOutAdapter().pFillData(pvDataObl);
        this.recyclerUnitsUsedOut.setHasFixedSize(false);
        this.recyclerUnitsUsedOut.setAdapter(this.getPackagingOutAdapter());
        this.recyclerUnitsUsedOut.setLayoutManager(new LinearLayoutManager(cAppExtension.context));
        this.recyclerUnitsUsedOut.setVisibility(View.VISIBLE);


    }



    //End Region private Methods


}
