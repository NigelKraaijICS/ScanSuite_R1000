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
import SSU_WHS.Basics.Settings.cSetting;
import SSU_WHS.Picken.PickorderLines.cPickorderLine;
import SSU_WHS.Picken.Pickorders.cPickorder;
import nl.icsvertex.scansuite.Activities.Ship.ShiporderLinesActivity;
import nl.icsvertex.scansuite.R;
import nl.icsvertex.scansuite.Activities.Sort.SortorderLinesActivity;
import ICS.cAppExtension;
import nl.icsvertex.scansuite.Fragments.Dialogs.NothingHereFragment;


public class SortorderLinesToSortFragment extends Fragment implements iICSDefaultFragment {


    //Region Public Properties

    //End Region Public Properties

    //Region Private Properties

    private static ConstraintLayout packingTableView;
    private static ConstraintLayout orderDoneView;
    private static RecyclerView recyclerViewSortorderLinesTosort;

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
        this.mFragmentInitialize();
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
            SortorderLinesToSortFragment.recyclerViewSortorderLinesTosort = getView().findViewById(R.id.recyclerViewSortorderLinesTosort);
            SortorderLinesToSortFragment.packingTableView = getView().findViewById(R.id.packingTableView);
            SortorderLinesToSortFragment.orderDoneView = getView().findViewById(R.id.orderDoneView);
        }

    }


    @Override
    public void mFieldsInitialize() {

        if (cSetting.PICK_SELECTEREN_BARCODE()) {
            SortorderLinesToSortFragment.packingTableView.setVisibility(View.VISIBLE);
        } else {
            SortorderLinesToSortFragment.packingTableView.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public void mSetListeners() {
        this.mSetPackTableListener();
        this.mSetOrderDoneListener();
    }


    //End Region iICSDefaultFragment defaults


    //Region Private Methods

    private void mSetOrderDoneListener() {
        SortorderLinesToSortFragment.orderDoneView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View pvView) {

                if (cAppExtension.activity instanceof SortorderLinesActivity) {
                    SortorderLinesActivity.pSortingDone();
                }
            }
        });
    }

    private void mSetPackTableListener() {

        SortorderLinesToSortFragment.packingTableView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SortorderLinesActivity.pHandleScan(null, true);
            }
        });

    }

    private void mGetData() {
        List<cPickorderLine> notHandledLinesObl = cPickorder.currentPickOrder.pGetLinesNotHandledFromDatabasObl();
        this.mFillRecycler(notHandledLinesObl);
    }

    private void mFillRecycler(List<cPickorderLine> pvDataObl) {

        if (pvDataObl.size() == 0) {
            this.mNoLinesAvailable(true);
            return;
        }

        cPickorderLine.getPickorderLinePickedAdapter().pFillData(pvDataObl);
        SortorderLinesToSortFragment.recyclerViewSortorderLinesTosort.setHasFixedSize(false);
        SortorderLinesToSortFragment.recyclerViewSortorderLinesTosort.setAdapter(cPickorderLine.getPickorderLinePickedAdapter());
        SortorderLinesToSortFragment.recyclerViewSortorderLinesTosort.setLayoutManager(new LinearLayoutManager(cAppExtension.context));


        SortorderLinesActivity.pChangeTabCounterText(cText.pDoubleToStringStr(cPickorder.currentPickOrder.pQuantityNotHandledDbl()) + "/" + cText.pDoubleToStringStr(cPickorder.currentPickOrder.pQuantityTotalDbl()));

    }

    //todo: check why this is always true
    private void mNoLinesAvailable(Boolean pvEnabledBln) {

        if (SortorderLinesActivity.currentLineFragment == this) {
            //Close no linesInt fragment if needed
            if (!pvEnabledBln) {
                List<Fragment> fragments = cAppExtension.fragmentManager.getFragments();
                for (Fragment fragment : fragments) {
                    if (fragment instanceof NothingHereFragment ) {
                        FragmentTransaction fragmentTransaction = cAppExtension.fragmentManager.beginTransaction();
                        fragmentTransaction.remove(fragment);
                        fragmentTransaction.commit();
                    }
                }

                return;

            }

            //Hide the recycler view
            SortorderLinesToSortFragment.recyclerViewSortorderLinesTosort.setVisibility(View.INVISIBLE);

            //Show nothing there fragment
            FragmentTransaction fragmentTransaction = cAppExtension.fragmentManager.beginTransaction();
            NothingHereFragment fragment = new NothingHereFragment();
            fragmentTransaction.replace(R.id.fragmentSortorderLinesToSort, fragment);
            fragmentTransaction.commit();

            //Change tabcounter text
            SortorderLinesActivity.pChangeTabCounterText(cText.pDoubleToStringStr(cPickorder.currentPickOrder.pQuantityNotHandledDbl()) + "/" + cText.pDoubleToStringStr(cPickorder.currentPickOrder.pQuantityTotalDbl()));
        }
    }

    //End Region Private Methods



}
