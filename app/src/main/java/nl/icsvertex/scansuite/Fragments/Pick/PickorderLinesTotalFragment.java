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
import SSU_WHS.Picken.PickorderLines.cPickorderLine;
import SSU_WHS.Picken.Pickorders.cPickorder;
import nl.icsvertex.scansuite.Activities.Pick.PickorderLinesActivity;
import nl.icsvertex.scansuite.R;

public class PickorderLinesTotalFragment extends Fragment {


    //Region Public Properties

    //End Region Public Properties

    //Region Private Properties
    private static  RecyclerView recyclerViewPickorderLinesTotal;
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
            PickorderLinesTotalFragment.recyclerViewPickorderLinesTotal = getView().findViewById(R.id.recyclerViewPickorderLinesTotal);
        }
    }

    private void mFillRecycler() {

        recyclerViewPickorderLinesTotal.setHasFixedSize(false);
        recyclerViewPickorderLinesTotal.setAdapter(cPickorderLine.getPickorderLineTotalAdapter());
        recyclerViewPickorderLinesTotal.setLayoutManager(new LinearLayoutManager(cAppExtension.context));
        recyclerViewPickorderLinesTotal.setVisibility(View.VISIBLE);
        cPickorderLine.getPickorderLineTotalAdapter().pFillData(cPickorder.currentPickOrder.linesObl());

        PickorderLinesActivity.pChangeTabCounterText(cText.pDoubleToStringStr(cPickorder.currentPickOrder.pQuantityTotalDbl()));

    }
}
