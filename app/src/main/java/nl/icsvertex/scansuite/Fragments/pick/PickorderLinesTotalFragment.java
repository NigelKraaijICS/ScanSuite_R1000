package nl.icsvertex.scansuite.Fragments.pick;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ICS.Utils.cText;
import SSU_WHS.Picken.PickorderLines.cPickorderLine;
import SSU_WHS.Picken.Pickorders.cPickorder;
import nl.icsvertex.scansuite.R;
import nl.icsvertex.scansuite.Activities.pick.PickorderLinesActivity;
import ICS.cAppExtension;

public class PickorderLinesTotalFragment extends Fragment {


    //Region Public Properties

    //End Region Public Properties

    //Region Private Properties
    private  RecyclerView recyclerViewPickorderLinesTotal;
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
        this.mFillRecycler();
    }

    @Override
    public void setUserVisibleHint(boolean pvIsVisibleToUserBln) {
        super.setUserVisibleHint(pvIsVisibleToUserBln);

        if (pvIsVisibleToUserBln) {

            PickorderLinesActivity.currentLineFragment = this;

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.detach(this).attach(this).commit();
        }
    }


    private void mFindViews() {
        this.recyclerViewPickorderLinesTotal = getView().findViewById(R.id.recyclerViewPickorderLinesTotal);
    }


    private void mFillRecycler() {

        recyclerViewPickorderLinesTotal.setHasFixedSize(false);
        recyclerViewPickorderLinesTotal.setAdapter(cPickorderLine.getPickorderLineTotalAdapter());
        recyclerViewPickorderLinesTotal.setLayoutManager(new LinearLayoutManager(cAppExtension.context));

        cPickorderLine.getPickorderLineTotalAdapter().pFillData(cPickorder.currentPickOrder.linesObl());

        PickorderLinesActivity.pChangeTabCounterText(cText.pDoubleToStringStr(cPickorder.currentPickOrder.pQuantityTotalDbl()));

    }
}
