package nl.icsvertex.scansuite.Fragments.Pick;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ICS.Utils.cText;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Picken.PickorderLines.cPickorderLineAdapter;
import SSU_WHS.Picken.Pickorders.cPickorder;
import nl.icsvertex.scansuite.Activities.Pick.PickorderLinesActivity;
import nl.icsvertex.scansuite.R;

public class PickorderLinesTotalFragment extends Fragment {


    //Region Public Properties

    //End Region Public Properties

    //Region Private Properties
    private  RecyclerView recyclerViewPickorderLinesTotal;

    private cPickorderLineAdapter pickorderLineAdapter;
    private cPickorderLineAdapter getPickorderLineAdapter(){
        if (this.pickorderLineAdapter == null) {
            this.pickorderLineAdapter = new cPickorderLineAdapter();
        }

        return  this.pickorderLineAdapter;
    }
    //End Region Private Properties

    //Region Constructor
    public PickorderLinesTotalFragment() {

    }
    //End Region Constructor


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pickorder_lines_total, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        this.mFindViews();
        cUserInterface.pEnableScanner();
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
        PickorderLinesActivity.currentLineFragment = this;
        this.mFillRecycler();
    }

    private void mFindViews() {
        if (getView() != null) {
            this.recyclerViewPickorderLinesTotal = getView().findViewById(R.id.recyclerViewPickorderLinesTotal);
        }
    }

    private void mFillRecycler() {

        this.recyclerViewPickorderLinesTotal.setHasFixedSize(false);
        this.recyclerViewPickorderLinesTotal.setAdapter(this.getPickorderLineAdapter());
        this.recyclerViewPickorderLinesTotal.setLayoutManager(new LinearLayoutManager(cAppExtension.context));
        this.recyclerViewPickorderLinesTotal.setVisibility(View.VISIBLE);
        this.getPickorderLineAdapter().pFillData(cPickorder.currentPickOrder.linesObl());

        if (cAppExtension.activity instanceof PickorderLinesActivity) {
            PickorderLinesActivity pickorderLinesActivity = (PickorderLinesActivity)cAppExtension.activity;
            pickorderLinesActivity.pChangeTabCounterText(cText.pDoubleToStringStr(cPickorder.currentPickOrder.pQuantityTotalDbl()));
        }
    }
}
