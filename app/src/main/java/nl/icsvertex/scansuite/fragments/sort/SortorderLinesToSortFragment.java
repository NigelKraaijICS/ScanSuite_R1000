package nl.icsvertex.scansuite.fragments.sort;

import android.content.Intent;
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
import SSU_WHS.Basics.Settings.cSetting;
import SSU_WHS.Picken.PickorderLines.cPickorderLine;
import SSU_WHS.Picken.Pickorders.cPickorder;
import nl.icsvertex.scansuite.R;
import nl.icsvertex.scansuite.activities.sort.SortorderLinesActivity;
import nl.icsvertex.scansuite.activities.sort.SortorderSortActivity;
import nl.icsvertex.scansuite.cAppExtension;
import nl.icsvertex.scansuite.fragments.NothingHereFragment;


public class SortorderLinesToSortFragment extends Fragment implements iICSDefaultFragment {


    //Region Public Properties

    //End Region Public Properties

    //Region Private Properties

    private ConstraintLayout packingTableView;
    private ConstraintLayout orderDoneView;
    private RecyclerView recyclerViewSortorderLinesTosort;

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

        mFragmentInitialize();

    }

    @Override
    public void setUserVisibleHint(boolean pvIsVisibleToUserBln) {
        super.setUserVisibleHint(pvIsVisibleToUserBln);

        if (pvIsVisibleToUserBln) {

            SortorderLinesActivity.currentLineFragment = this;

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.detach(this).attach(this).commit();
        }
    }


    //End Region Default Methods

    //Region iICSDefaultFragment defaults

    @Override
    public void mFragmentInitialize() {
        this.mFindViews();
        this.mSetViewModels();
        this.mFieldsInitialize();
        this.mSetListeners();
        this.mGetData();
    }

    @Override
    public void mFindViews() {
            this.recyclerViewSortorderLinesTosort = getView().findViewById(R.id.recyclerViewSortorderLinesTosort);
            this.packingTableView = getView().findViewById(R.id.packingTableView);
            this.orderDoneView = getView().findViewById(R.id.orderDoneView);
    }

    @Override
    public void mSetViewModels() {

    }

    @Override
    public void mFieldsInitialize() {

        if (cSetting.PICK_SELECTEREN_BARCODE()) {
            this.packingTableView.setVisibility(View.VISIBLE);
        } else {
            this.packingTableView.setVisibility(View.INVISIBLE);
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
        this.orderDoneView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View pvView) {

                if (cAppExtension.activity instanceof SortorderLinesActivity) {
                    SortorderLinesActivity.pSortingDone();
                }
            }
        });
    }

    private void mSetPackTableListener() {

        this.packingTableView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SortorderLinesActivity.pHandleScan("", true);
            }
        });

    }

    private void mGetData() {
        List<cPickorderLine> HandledLinesObl = cPickorder.currentPickOrder.pGetLinesNotHandledFromDatabasObl();
        this.mFillRecycler(HandledLinesObl);
    }

    private void mFillRecycler(List<cPickorderLine> pvDataObl) {

        if (pvDataObl.size() == 0) {
            this.mNoLinesAvailable(true);
            return;
        }

        cPickorderLine.getPickorderLinePickedAdapter().pFillData(pvDataObl);
        this.recyclerViewSortorderLinesTosort.setHasFixedSize(false);
        this.recyclerViewSortorderLinesTosort.setAdapter(cPickorderLine.getPickorderLinePickedAdapter());
        this.recyclerViewSortorderLinesTosort.setLayoutManager(new LinearLayoutManager(cAppExtension.context));


        SortorderLinesActivity.pChangeTabCounterText(cText.doubleToString(cPickorder.currentPickOrder.pQuantityNotHandledDbl()) + "/" + cText.doubleToString(cPickorder.currentPickOrder.pQuantityTotalDbl()));

    }

    private void mNoLinesAvailable(Boolean pvEnabledBln) {

        if (SortorderLinesActivity.currentLineFragment != null && SortorderLinesActivity.currentLineFragment == this) {
            //Close no lines fragment if needed
            if (!pvEnabledBln) {
                List<Fragment> fragments = cAppExtension.fragmentManager.getFragments();
                for (Fragment fragment : fragments) {
                    if (fragment instanceof NothingHereFragment || fragment instanceof NothingHereFragment ) {
                        FragmentTransaction fragmentTransaction = cAppExtension.fragmentManager.beginTransaction();
                        fragmentTransaction.remove(fragment);
                        fragmentTransaction.commit();
                    }
                }

                return;

            }

            //Hide the recycler view
            this.recyclerViewSortorderLinesTosort.setVisibility(View.INVISIBLE);

            //Show nothing there fragment
            FragmentTransaction fragmentTransaction = cAppExtension.fragmentManager.beginTransaction();
            NothingHereFragment fragment = new NothingHereFragment();
            fragmentTransaction.replace(R.id.fragmentSortorderLinesToSort, fragment);
            fragmentTransaction.commit();

            //Change tabcounter text
            SortorderLinesActivity.pChangeTabCounterText(cText.doubleToString(cPickorder.currentPickOrder.pQuantityNotHandledDbl()) + "/" + cText.doubleToString(cPickorder.currentPickOrder.pQuantityTotalDbl()));
        }
    }

    //End Region Private Methods



}
