package nl.icsvertex.scansuite.Fragments.Move;


import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ICS.Interfaces.iICSDefaultFragment;
import ICS.Utils.cText;
import ICS.cAppExtension;
import SSU_WHS.Move.MoveOrders.cMoveorder;
import SSU_WHS.Move.MoveorderLines.cMoveorderLine;
import nl.icsvertex.scansuite.Activities.Move.MoveorderLinesActivity;
import nl.icsvertex.scansuite.Fragments.Dialogs.NothingHereFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.SendOrderFragment;
import nl.icsvertex.scansuite.R;

public class MoveorderLinesNotDoneFragment extends Fragment implements iICSDefaultFragment {

    //Region Public Properties

    //End Region Public Properties

    //Region Private Properties


    private static TextView textViewSelectedBin;


    private static RecyclerView recyclerViewMoveorderLinesNotDone;

    //End Region Private Properties

    //Region Constructor
    public MoveorderLinesNotDoneFragment() {

    }
    //End Region Constructor

    //Region Default Methods

    @Override
    public View onCreateView(LayoutInflater pvInflater, ViewGroup pvContainer,
                             Bundle pvSavedInstanceState) {

        // Inflate the layout for this fragment
        View rootview = pvInflater.inflate(R.layout.fragment_moveorder_lines_notdone, pvContainer, false);
        return rootview;

    }

    @Override
    public void onViewCreated(@NonNull View pvView, @Nullable Bundle pvSavedInstanceState) {
        this.mFragmentInitialize();


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


    //End Region Default Methods

    //Region iICSDefaultFragment defaults
    @Override
    public void mFragmentInitialize() {

        this.mFindViews();
        this.mFieldsInitialize();
        this.mSetListeners();
        this.mGetData();

    }

    @Override
    public void mFindViews() {
        this.recyclerViewMoveorderLinesNotDone = getView().findViewById(R.id.recyclerViewMoveorderLinesNotDone);
        this.textViewSelectedBin = getView().findViewById(R.id.textViewSelectedBin);
    }


    @Override
    public void mFieldsInitialize() {


    }

    @Override
    public void mSetListeners() {

        this.mSetQuickHelpListener();
        this.mSetLocationListener();
    }

    //End Region iICSDefaultFragment defaults

    //Region Public Methods

    public static void pSetChosenBinCode( ) {
        MoveorderLinesNotDoneFragment.textViewSelectedBin.setText(cMoveorderLine.currentMoveOrderLine.getBinCodeStr());
    }

    public static void pSetSelectedIndexInt(final int pvIndexInt) {

        new Handler().postDelayed(new Runnable() {
                @Override
               public void run() {
                    MoveorderLinesNotDoneFragment.recyclerViewMoveorderLinesNotDone.scrollToPosition(pvIndexInt);
                }
           },1);

    }

    //End Region Public Methods

    //Region Private Methods

    private void mSetQuickHelpListener() {

    }

    private void mSetLocationListener() {

    }

    private void mGetData() {

        List<cMoveorderLine> notHandledLinesObl = cMoveorder.currentMoveOrder.pGetLinesNotHandledFromDatabasObl();

        this.mFillRecycler(notHandledLinesObl);

}

    private void mFillRecycler(List<cMoveorderLine> pvDataObl) {

        if (pvDataObl.size() == 0) {
            this.mNoLinesAvailable(true);
            return;
        }

        this.mNoLinesAvailable(false);

        //Show the recycler view
        cMoveorderLine.getMoveorderLineNotDoneAdapter().pFillData(pvDataObl);
        this.recyclerViewMoveorderLinesNotDone.setHasFixedSize(false);
        this.recyclerViewMoveorderLinesNotDone.setAdapter(cMoveorderLine.getMoveorderLineNotDoneAdapter());
        this.recyclerViewMoveorderLinesNotDone.setLayoutManager(new LinearLayoutManager(cAppExtension.context));
        this.recyclerViewMoveorderLinesNotDone.setVisibility(View.VISIBLE);

        MoveorderLinesActivity.pChangeTabCounterText(cText.pDoubleToStringStr(cMoveorder.currentMoveOrder.pQuantityNotHandledDbl()) + "/" + cText.pDoubleToStringStr(cMoveorder.currentMoveOrder.pQuantityTotalDbl()));
    }

    private void mNoLinesAvailable(Boolean pvEnabledBln) {

        if (MoveorderLinesActivity.currentLineFragment != null && MoveorderLinesActivity.currentLineFragment == this) {
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
            this.recyclerViewMoveorderLinesNotDone.setVisibility(View.INVISIBLE);

            //Hide location button and clear text

            //MoveorderLinesNotDoneFragment.textViewSelectedBin.setText("");


            //Show nothing there fragment
            FragmentTransaction fragmentTransaction = cAppExtension.fragmentManager.beginTransaction();
            SendOrderFragment fragment = new SendOrderFragment();
            fragmentTransaction.replace(R.id.fragmentMoveorderLinesNotDone, fragment);
            fragmentTransaction.commit();

            //Change tabcounter text
            MoveorderLinesActivity.pChangeTabCounterText(cText.pDoubleToStringStr(cMoveorder.currentMoveOrder.pQuantityNotHandledDbl()) + "/" + cText.pDoubleToStringStr(cMoveorder.currentMoveOrder.pQuantityTotalDbl()));
        }
    }

    //End Region private Methods


}
