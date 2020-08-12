package nl.icsvertex.scansuite.Fragments.Sort;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ICS.Interfaces.iICSDefaultFragment;
import ICS.Utils.cText;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Picken.PickorderLines.cPickorderLine;
import SSU_WHS.Picken.PickorderLines.cPickorderLineAdapter;
import SSU_WHS.Picken.Pickorders.cPickorder;
import nl.icsvertex.scansuite.Activities.Sort.SortorderLinesActivity;
import nl.icsvertex.scansuite.R;

public class SortorderLinesTotalFragment extends Fragment implements iICSDefaultFragment {

    //Region Public Properties

    //End Region Public Properties

    //Region Private Properties

    private RecyclerView recyclerViewSortorderLinesTotal;
    private cPickorderLineAdapter pickorderLineAdapter;
    private cPickorderLineAdapter getPickorderLineAdapter(){
        if (this.pickorderLineAdapter == null) {
            this.pickorderLineAdapter = new cPickorderLineAdapter();
        }

        return  this.pickorderLineAdapter;
    }

    //End Region Private Properties

    //Region Constructor

    public SortorderLinesTotalFragment() {

    }

    //End Region Constructor



    //Region Default Methods

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sortorder_lines_total, container, false);
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
        this.mGetData();
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
            this.recyclerViewSortorderLinesTotal = getView().findViewById(R.id.recyclerViewSortorderLinesTotal);
        }
    }

    @Override
    public void mFieldsInitialize() {

    }

    @Override
    public void mSetListeners() {

    }

    //End Region iICSDefaultFragment defaults

    //Region Private Methods

    private void mGetData() {

        List<cPickorderLine> HandledLinesObl = cPickorder.currentPickOrder.pGetLinesFromDatabaseObl();
        this.mFillRecycler(HandledLinesObl);

    }


    private void mFillRecycler(List<cPickorderLine> pvDataObl) {


        this.getPickorderLineAdapter().pFillData(pvDataObl);
        this.recyclerViewSortorderLinesTotal.setHasFixedSize(false);
        this.recyclerViewSortorderLinesTotal.setAdapter(this.getPickorderLineAdapter());
        this.recyclerViewSortorderLinesTotal.setLayoutManager(new LinearLayoutManager(cAppExtension.context));

        if (cAppExtension.activity instanceof  SortorderLinesActivity) {
            SortorderLinesActivity sortorderLinesActivity = (SortorderLinesActivity)cAppExtension.activity;
            sortorderLinesActivity.pChangeTabCounterText(cText.pDoubleToStringStr(cPickorder.currentPickOrder.pQuantityTotalDbl()));
        }
    }

    //End Region Private Methods





}
