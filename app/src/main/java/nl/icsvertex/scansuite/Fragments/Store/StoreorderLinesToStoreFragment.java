package nl.icsvertex.scansuite.Fragments.Store;


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

import ICS.Interfaces.iICSDefaultFragment;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Picken.Pickorders.cPickorder;
import SSU_WHS.Picken.Storement.cStorementAdapter;
import nl.icsvertex.scansuite.Activities.Store.StoreorderLinesActivity;
import nl.icsvertex.scansuite.Fragments.Dialogs.NothingHereFragment;
import nl.icsvertex.scansuite.R;


public class StoreorderLinesToStoreFragment extends Fragment implements iICSDefaultFragment {


    //Region Public Properties

    //End Region Public Properties

    //Region Private Properties
    private  RecyclerView recyclerViewStoreorderLinesToStore;

    private cStorementAdapter storementAdapter;
    private cStorementAdapter getStorementAdapter(){
        if (this.storementAdapter == null) {
            this.storementAdapter = new cStorementAdapter();
        }

        return  this.storementAdapter;
    }

    //End Region Private Properties

    //Region Constructor
    public StoreorderLinesToStoreFragment() {

    }
    //End Region Constructor

    //Region Default Methods

    @Override
    public View onCreateView(LayoutInflater pvInflater, ViewGroup pvContainer,
                             Bundle pvSavedInstanceState) {
        // Inflate the layout for this fragment
        return pvInflater.inflate(R.layout.fragment_storeorder_lines_to_store, pvContainer, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
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
        StoreorderLinesActivity.currentLineFragment = this;
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
            this.recyclerViewStoreorderLinesToStore = getView().findViewById(R.id.recyclerViewStoreorderLinesToStore);
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
        this.mFillRecycler();
    }

    private void mFillRecycler() {

        if (cPickorder.currentPickOrder.pGetNotHandledStorementsObl().size() == 0) {
            this.mNoLinesAvailable();
            return;
        }

        this.getStorementAdapter().pFillData(cPickorder.currentPickOrder.pGetNotHandledStorementsObl());
        this.recyclerViewStoreorderLinesToStore.setHasFixedSize(false);
        this.recyclerViewStoreorderLinesToStore.setAdapter(this.getStorementAdapter());
        this.recyclerViewStoreorderLinesToStore.setLayoutManager(new LinearLayoutManager(cAppExtension.context));

        if (cAppExtension.activity instanceof  StoreorderLinesActivity) {
            StoreorderLinesActivity storeorderLinesActivity = (StoreorderLinesActivity)cAppExtension.activity;
            storeorderLinesActivity.pChangeTabCounterText(cPickorder.currentPickOrder.pGetNotHandledStorementsObl().size() + "/" + cPickorder.currentPickOrder.storementObl().size());
        }
    }

    private void mNoLinesAvailable() {

        if (StoreorderLinesActivity.currentLineFragment == this) {
            //Close no linesInt fragment if needed

            //Hide the recycler view
            this.recyclerViewStoreorderLinesToStore.setVisibility(View.INVISIBLE);

            //Show nothing there fragment
            FragmentTransaction fragmentTransaction = cAppExtension.fragmentManager.beginTransaction();
            NothingHereFragment fragment = new NothingHereFragment();
            fragmentTransaction.replace(R.id.fragmentStoreorderLinesToStore, fragment);
            fragmentTransaction.commit();

            //Change tabcounter text
            if (cAppExtension.activity instanceof  StoreorderLinesActivity) {
                StoreorderLinesActivity storeorderLinesActivity = (StoreorderLinesActivity)cAppExtension.activity;
                storeorderLinesActivity.pChangeTabCounterText(cPickorder.currentPickOrder.pGetNotHandledStorementsObl().size() + "/" + cPickorder.currentPickOrder.storementObl().size());
            }
        }
    }

    //End Region Private Methods



}
