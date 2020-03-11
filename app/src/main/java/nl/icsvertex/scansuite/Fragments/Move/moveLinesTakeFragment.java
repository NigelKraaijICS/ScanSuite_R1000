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

import java.util.List;

import ICS.Interfaces.iICSDefaultFragment;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Move.MoveOrders.cMoveorder;
import SSU_WHS.Move.MoveorderLines.cMoveorderLine;
import nl.icsvertex.scansuite.Activities.Move.MoveLinesActivity;
import nl.icsvertex.scansuite.Fragments.Dialogs.NothingHereFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.SendOrderFragment;
import nl.icsvertex.scansuite.R;

public class moveLinesTakeFragment extends Fragment implements iICSDefaultFragment {

    //Region Public Properties

    //End Region Public Properties

    //Region Private Properties

    private static RecyclerView recyclerViewMoveLinesTake;


    //End Region Private Properties


    //Region Default Methods

    @Override
    public View onCreateView(LayoutInflater pvInflater, ViewGroup pvContainer,
                             Bundle pvSavedInstanceState) {
        // Inflate the layout for this fragment
        return pvInflater.inflate(R.layout.fragment_movelines_take, pvContainer, false);
    }

    @Override
    public void onViewCreated(@NonNull View pvView, @Nullable Bundle pvSavedInstanceState) {

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
        MoveLinesActivity.currentLineFragment = this;
        this.mFragmentInitialize();
    }

    //End Region Default Methods

    //Region iICSDefaultActivity defaults

    @Override
    public void mFragmentInitialize() {

        this.mFindViews();
        this.mFieldsInitialize();
        this.mSetListeners();
        moveLinesTakeFragment.mGetData();

    }

    @Override
    public void mFindViews() {

        if (getView() != null) {
            moveLinesTakeFragment.recyclerViewMoveLinesTake = getView().findViewById(R.id.recyclerViewMoveLinesTake);
        }

    }

    @Override
    public void mFieldsInitialize() {

    }

    @Override
    public void mSetListeners() {

    }

    //End Region iICSDefaultActivity defaults

    //Region Public Methods

    //End Region Public Methods

    //Region Private Methods

    private static void mGetData() {
        moveLinesTakeFragment.mFillRecycler(cMoveorder.currentMoveOrder.takeLinesObl());
    }

    private static void mFillRecycler(List<cMoveorderLine> pvDataObl) {

        if (pvDataObl.size() == 0) {
            moveLinesTakeFragment.mNoLinesAvailable(true);
            return;
        }
        moveLinesTakeFragment.mNoLinesAvailable(false);

        cMoveorderLine.getMoveorderLineAdapter().pFillData(pvDataObl);
        moveLinesTakeFragment.recyclerViewMoveLinesTake.setHasFixedSize(false);
        moveLinesTakeFragment.recyclerViewMoveLinesTake.setAdapter(cMoveorderLine.getMoveorderLineAdapter());
        moveLinesTakeFragment.recyclerViewMoveLinesTake.setLayoutManager(new LinearLayoutManager(cAppExtension.context));
        moveLinesTakeFragment.recyclerViewMoveLinesTake.setVisibility(View.VISIBLE);
    }

    private static void mNoLinesAvailable(Boolean pvEnabledBln) {

        if (MoveLinesActivity.currentLineFragment  instanceof moveLinesTakeFragment) {
            //Close no orders fragment if needed
            if (!pvEnabledBln) {
                List<Fragment> fragments = cAppExtension.fragmentManager.getFragments();
                for (Fragment fragment : fragments) {
                    if (fragment instanceof NothingHereFragment || fragment instanceof SendOrderFragment) {
                        FragmentTransaction fragmentTransaction = cAppExtension.fragmentManager.beginTransaction();
                        fragmentTransaction.remove(fragment);
                        fragmentTransaction.commit();
                    }
                }
                return;
            }

            //Hide the recycler view
           moveLinesTakeFragment.recyclerViewMoveLinesTake.setVisibility(View.INVISIBLE);

            //Hide location button and clear text

            //Show nothing there fragment
            FragmentTransaction fragmentTransaction = cAppExtension.fragmentManager.beginTransaction();
            NothingHereFragment fragment = new NothingHereFragment();
            fragmentTransaction.replace(R.id.fragmentMoveLinesTake, fragment);
            fragmentTransaction.commit();
        }
    }

    //End Region Private Methods
}






