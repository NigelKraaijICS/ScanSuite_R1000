package nl.icsvertex.scansuite.Fragments.intake;

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

import ICS.Utils.cText;
import ICS.cAppExtension;
import SSU_WHS.Intake.IntakeorderMATLines.cIntakeorderMATLine;
import SSU_WHS.Intake.Intakeorders.cIntakeorder;
import nl.icsvertex.scansuite.Activities.intake.IntakeorderLinesActivity;
import nl.icsvertex.scansuite.R;

public class IntakeorderLinesTotalFragment extends Fragment {


    //Region Public Properties

    //End Region Public Properties

    //Region Private Properties
    private  RecyclerView recyclerViewIntakeorderLinesTotal;
    //End Region Private Properties

    //Region Constructor
    public IntakeorderLinesTotalFragment() {

    }
    //End Region Constructor


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_intake_lines_total, container, false);
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

            IntakeorderLinesActivity.currentLineFragment = this;

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.detach(this).attach(this).commit();
        }
    }


    private void mFindViews() {
        this.recyclerViewIntakeorderLinesTotal = getView().findViewById(R.id.recyclerViewIntakeorderLinesTotal);
    }


    private void mFillRecycler() {

        this.recyclerViewIntakeorderLinesTotal.setHasFixedSize(false);
        this.recyclerViewIntakeorderLinesTotal.setAdapter(cIntakeorderMATLine.getIntakeorderMATLineDoneAdapter());
        this.recyclerViewIntakeorderLinesTotal.setLayoutManager(new LinearLayoutManager(cAppExtension.context));

        cIntakeorderMATLine.getIntakeorderMATLineDoneAdapter().pFillData(cIntakeorder.currentIntakeOrder.pGetLinesFromDatabasObl());

        IntakeorderLinesActivity.pChangeTabCounterText(cText.pDoubleToStringStr(cIntakeorder.currentIntakeOrder.pQuantityTotalDbl()));

    }
}
