package nl.icsvertex.scansuite.Fragments.Packaging;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ICS.Interfaces.iICSDefaultFragment;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Basics.Packaging.cPackaging;
import SSU_WHS.Basics.Packaging.cPackagingUnitUsedAdapter;
import SSU_WHS.Intake.Intakeorders.cIntakeorder;
import nl.icsvertex.scansuite.Activities.Packaging.PackagingActivity;
import nl.icsvertex.scansuite.Fragments.Dialogs.NothingHereFragment;
import nl.icsvertex.scansuite.R;


public class PackagingUsedFragment extends  Fragment  implements iICSDefaultFragment{

    //Region Public Properties

    //End Region Public Properties

    //Region Private Properties


    private RecyclerView recyclerUnitsUsed;

    private cPackagingUnitUsedAdapter packagingUnitUsedAdapter;
    private cPackagingUnitUsedAdapter getPackagingUnitUsedAdapter(){
        if (this.packagingUnitUsedAdapter == null) {
            this.packagingUnitUsedAdapter = new cPackagingUnitUsedAdapter();
        }
        return  this.packagingUnitUsedAdapter;
    }


    //End Region Private Properties

    //Region Constructor
    public PackagingUsedFragment() {

    }
    //End Region Constructor

    //Region Default Methods

    @Override
    public View onCreateView(LayoutInflater pvInflater, ViewGroup pvContainer,
                             Bundle pvSavedInstanceState) {

        // Inflate the layout for this fragment
        return pvInflater.inflate(R.layout.fragment_packaging_used, pvContainer, false);

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
            this.recyclerUnitsUsed = getView().findViewById(R.id.recyclerUnitsUsed);
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

    private void mNoLinesAvailable(Boolean pvEnabledBln) {

        if (cAppExtension.activity instanceof  PackagingActivity) {
            if (PackagingActivity.currentPackagingFragment instanceof PackagingUsedFragment) {

                if (!pvEnabledBln) {
                    List<Fragment> fragments = cAppExtension.fragmentManager.getFragments();
                    for (Fragment fragment : fragments) {
                        if (fragment instanceof NothingHereFragment) {
                            FragmentTransaction fragmentTransaction = cAppExtension.fragmentManager.beginTransaction();
                            fragmentTransaction.remove(fragment);
                            fragmentTransaction.commit();
                        }
                    }
                    return;
                }

                this.recyclerUnitsUsed.setVisibility(View.INVISIBLE);

                FragmentTransaction fragmentTransaction = cAppExtension.fragmentManager.beginTransaction();
                NothingHereFragment fragment = new NothingHereFragment();
                fragmentTransaction.replace(R.id.fragmentPackagingUsed, fragment);
                fragmentTransaction.commit();
            }
        }
    }



    private void mGetData() {

        List<cPackaging> localPackagesObl = new ArrayList<>();

        for (cPackaging packaging : cIntakeorder.currentIntakeOrder.packagingInObl) {
            if (packaging.getQuantityInUsedInt() > 0 ) {
                cPackaging packagingToAdd = new cPackaging(packaging, "IN");
                localPackagesObl.add((packagingToAdd));
            }
        }

        for (cPackaging packaging : cIntakeorder.currentIntakeOrder.packagingOutObl) {
            if (packaging.getQuantityOutUsedInt() > 0 ) {
                cPackaging packagingToAdd = new cPackaging(packaging, "OUT");
                localPackagesObl.add((packagingToAdd));
            }
        }
        this.mFillRecycler(localPackagesObl);

}

    private void mFillRecycler(List<cPackaging> pvDataObl) {

        if (pvDataObl.size() == 0) {
            this.mNoLinesAvailable(true);
            return;
        }

        this.mNoLinesAvailable(false);

        //Show the recycler view
        this.getPackagingUnitUsedAdapter().pFillData(pvDataObl);
        this.recyclerUnitsUsed.setHasFixedSize(false);
        this.recyclerUnitsUsed.setAdapter(this.getPackagingUnitUsedAdapter());
        this.recyclerUnitsUsed.setLayoutManager(new LinearLayoutManager(cAppExtension.context));
        this.recyclerUnitsUsed.setVisibility(View.VISIBLE);


    }



    //End Region private Methods


}
