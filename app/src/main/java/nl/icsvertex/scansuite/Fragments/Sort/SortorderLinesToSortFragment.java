package nl.icsvertex.scansuite.Fragments.Sort;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ICS.Interfaces.iICSDefaultFragment;
import ICS.Utils.cText;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Basics.Settings.cSetting;
import SSU_WHS.Picken.PickorderLines.cPickorderLine;
import SSU_WHS.Picken.PickorderLines.cPickorderLineAdapter;
import SSU_WHS.Picken.Pickorders.cPickorder;
import nl.icsvertex.scansuite.Activities.Sort.SortorderLinesActivity;
import nl.icsvertex.scansuite.Fragments.Dialogs.NothingHereFragment;
import nl.icsvertex.scansuite.R;


public class SortorderLinesToSortFragment extends Fragment implements iICSDefaultFragment {


    //Region Public Properties

    //End Region Public Properties

    //Region Private Properties

    private  ConstraintLayout showDetailsView;
    private  ConstraintLayout orderDoneView;
    private  RecyclerView recyclerViewSortorderLinesTosort;

    private cPickorderLineAdapter pickorderLineAdapter;
    private cPickorderLineAdapter getPickorderLineAdapter(){
        if (this.pickorderLineAdapter == null) {
            this.pickorderLineAdapter = new cPickorderLineAdapter();
        }

        return  this.pickorderLineAdapter;
    }

    //End Region Private Properties

    //Region Constructor
    public SortorderLinesToSortFragment() {

    }
    //End Region Constructor

    //Region Default Methods

    @Override
    public View onCreateView(LayoutInflater pvInflater, ViewGroup pvContainer,
                             Bundle pvSavedInstanceState) {
        // Inflate the layout for this fragment
        return pvInflater.inflate(R.layout.fragment_sortorder_lines_to_sort, pvContainer, false);
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
        SortorderLinesActivity.currentLineFragment = this;
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
            this.recyclerViewSortorderLinesTosort = getView().findViewById(R.id.recyclerViewSortorderLinesTosort);
            this.showDetailsView = getView().findViewById(R.id.showDetailsView);
            this.orderDoneView = getView().findViewById(R.id.orderDoneView);
        }

    }


    @Override
    public void mFieldsInitialize() {
        this.showDetailsView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void mSetListeners() {
        this.mSetPackTableListener();
        this.mSetOrderDoneListener();
    }

    //End Region iICSDefaultFragment defaults

    //Region Public Methods

    public  void pShowHideDetailButton(){

        if (!cSetting.PICK_SELECTEREN_BARCODE()) {
            this.showDetailsView.setVisibility(View.INVISIBLE);
            return;
        }

        this.showDetailsView.setVisibility(View.VISIBLE);


    }

    //End Region Public Methods

    //Region Private Methods

    private void mSetOrderDoneListener() {
        this.orderDoneView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View pvView) {

                if (cAppExtension.activity instanceof SortorderLinesActivity) {
                    SortorderLinesActivity sortorderLinesActivity = (SortorderLinesActivity)cAppExtension.activity;
                    sortorderLinesActivity.pSortingDone();
                }
            }
        });
    }

    private void mSetPackTableListener() {

        this.showDetailsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (cAppExtension.activity instanceof  SortorderLinesActivity) {
                    SortorderLinesActivity sortorderLinesActivity = (SortorderLinesActivity)cAppExtension.activity;
                    sortorderLinesActivity.pHandleScan(null, true);
                }

            }
        });

    }

    private void mGetData() {
        List<cPickorderLine> notHandledLinesObl = cPickorder.currentPickOrder.pGetLinesNotHandledFromDatabaseObl();
        this.mFillRecycler(notHandledLinesObl);
    }

    private void mFillRecycler(List<cPickorderLine> pvDataObl) {

        if (pvDataObl.size() == 0) {
            this.mNoLinesAvailable();
            return;
        }

        this.getPickorderLineAdapter().pFillData(pvDataObl);
        this.recyclerViewSortorderLinesTosort.setHasFixedSize(false);
        this.recyclerViewSortorderLinesTosort.setAdapter(this.getPickorderLineAdapter());
        this.recyclerViewSortorderLinesTosort.setLayoutManager(new LinearLayoutManager(cAppExtension.context));

        if (cAppExtension.activity instanceof  SortorderLinesActivity) {
            SortorderLinesActivity sortorderLinesActivity = (SortorderLinesActivity)cAppExtension.activity;
            sortorderLinesActivity.pChangeTabCounterText(cText.pDoubleToStringStr(cPickorder.currentPickOrder.pQuantityNotHandledDbl()) + "/" + cText.pDoubleToStringStr(cPickorder.currentPickOrder.pQuantityTotalDbl()));
        }
    }

    private void mNoLinesAvailable() {

        if (SortorderLinesActivity.currentLineFragment == this) {
            //Close no linesInt fragment if needed

            //Hide the recycler view
            this.recyclerViewSortorderLinesTosort.setVisibility(View.INVISIBLE);

            //Show nothing there fragment
            FragmentTransaction fragmentTransaction = cAppExtension.fragmentManager.beginTransaction();
            NothingHereFragment fragment = new NothingHereFragment();
            fragmentTransaction.replace(R.id.fragmentSortorderLinesToSort, fragment);
            fragmentTransaction.commit();

            //Change tabcounter text
            if (cAppExtension.activity instanceof  SortorderLinesActivity) {
                SortorderLinesActivity sortorderLinesActivity = (SortorderLinesActivity)cAppExtension.activity;
                sortorderLinesActivity.pChangeTabCounterText(cText.pDoubleToStringStr(cPickorder.currentPickOrder.pQuantityNotHandledDbl()) + "/" + cText.pDoubleToStringStr(cPickorder.currentPickOrder.pQuantityTotalDbl()));
            }
        }
    }

    //End Region Private Methods



}
