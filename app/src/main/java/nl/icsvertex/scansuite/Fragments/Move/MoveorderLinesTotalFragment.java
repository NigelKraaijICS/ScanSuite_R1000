package nl.icsvertex.scansuite.Fragments.Move;

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

import ICS.Utils.cText;
import ICS.cAppExtension;
import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.Move.MoveOrders.cMoveorder;
import SSU_WHS.Move.MoveorderLines.cMoveorderLine;
import nl.icsvertex.scansuite.Activities.Move.MoveorderLinesActivity;
import nl.icsvertex.scansuite.R;

public class MoveorderLinesTotalFragment extends Fragment {


    //Region Public Properties

    //End Region Public Properties

    //Region Private Properties
    private RecyclerView recyclerViewMoveorderLinesTotal;
    //End Region Private Properties

    //Region Constructor
    public MoveorderLinesTotalFragment() {

    }
    //End Region Constructor


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_moveorder_lines_total, container, false);
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

            MoveorderLinesActivity.currentLineFragment = this;

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.detach(this).attach(this).commit();
        }
    }

    private void mFindViews() {
        this.recyclerViewMoveorderLinesTotal = getView().findViewById(R.id.recyclerViewMoveorderLinesTotal);
    }

    private void mFillRecycler() {

        recyclerViewMoveorderLinesTotal.setHasFixedSize(false);
        recyclerViewMoveorderLinesTotal.setAdapter(cMoveorderLine.getMoveorderLineTotalAdapter());
        recyclerViewMoveorderLinesTotal.setLayoutManager(new LinearLayoutManager(cAppExtension.context));

        List<cMoveorderLine> moveorderLines = new ArrayList<>();

        for (cMoveorderLine moveorderLine : cMoveorder.currentMoveOrder.linesObl()) {
            if (moveorderLine.getActionTypeCodeStr().equalsIgnoreCase(cWarehouseorder.ActionTypeEnu.PLACE.toString())) {
                moveorderLines.add(moveorderLine);
            }
        }

        //cMoveorderLine.getMoveorderLineTotalAdapter().pFillData(cMoveorder.currentMoveOrder.linesObl());
        cMoveorderLine.getMoveorderLineTotalAdapter().pFillData(moveorderLines);

        MoveorderLinesActivity.pChangeTabCounterText(cText.pDoubleToStringStr(cMoveorder.currentMoveOrder.pQuantityTotalDbl()));

    }
}
