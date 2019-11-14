package nl.icsvertex.scansuite.Fragments.intake;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ICS.Interfaces.iICSDefaultFragment;
import ICS.Utils.cText;
import ICS.cAppExtension;
import SSU_WHS.Intake.IntakeorderMATLines.cIntakeorderMATLine;
import SSU_WHS.Intake.Intakeorders.cIntakeorder;
import nl.icsvertex.scansuite.Activities.intake.IntakeorderLinesActivity;
import nl.icsvertex.scansuite.Fragments.NothingHereFragment;
import nl.icsvertex.scansuite.Fragments.SendOrderFragment;
import nl.icsvertex.scansuite.R;

public class IntakeorderLinesDoneFragment extends Fragment implements iICSDefaultFragment {

    //Region Public Properties
    //End Region Public Properties

    //Region Private Properties

    private static RecyclerView recyclerViewIntakeorderLinesDone;
    private SearchView recyclerSearchView;

    private ConstraintLayout fragmentIntakeorderLinesDone;
    private static TextView textViewSelectedLine;

    //End Region Private Properties

    //Region Constructor
    public IntakeorderLinesDoneFragment() {

    }
    //End Region Constructor

    //Region Default Methods
    @Override
    public View onCreateView(LayoutInflater pvInflater, ViewGroup pvContainer,
                             Bundle pvSavedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = pvInflater.inflate(R.layout.fragment_intake_lines_done, pvContainer, false);
        return rootview;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        this.mFragmentInitialize();
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

    // End Region Default Methods

    //Region iICSDefaultFragment defaults

    @Override
    public void mFragmentInitialize() {
        this.mFindViews();
        this.mFieldsInitialize();
        this.mSetListeners();

             this.pGetData(cIntakeorder.currentIntakeOrder.pGetLinesHandledFromDatabasObl());
    }

    @Override
    public void mFindViews() {
        this.fragmentIntakeorderLinesDone = getView().findViewById(R.id.fragmentIntakeLinesDone);
        this.recyclerSearchView = getView().findViewById(R.id.recyclerSearchView);
        this.recyclerViewIntakeorderLinesDone = getView().findViewById(R.id.recyclerViewIntakeLinesDone);
        this.textViewSelectedLine = getView().findViewById(R.id.textViewSelectedLine);
    }


    @Override
    public void mFieldsInitialize() {

    }

    @Override
    public void mSetListeners() {
        this.mSetRecyclerOnScrollListener();
        this.mSetSearchListener();
    }


    //End Region iICSDefaultFragment defaults

    //Region Public Methods

     //End Region Public Methods

    //Region Private Methods

    public static void pGetData(List<cIntakeorderMATLine> pvDataObl) {
        IntakeorderLinesDoneFragment.mFillRecycler(pvDataObl);
    }

    private static void mFillRecycler(List<cIntakeorderMATLine> pvDataObl) {

        if (pvDataObl.size() == 0) {
            IntakeorderLinesDoneFragment.pNoLinesAvailable(true);
            return;
        }

        IntakeorderLinesDoneFragment.pNoLinesAvailable(false);
        cIntakeorderMATLine.getIntakeorderMATLineDoneAdapter().pFillData(pvDataObl);
        IntakeorderLinesDoneFragment.recyclerViewIntakeorderLinesDone.setHasFixedSize(false);
        IntakeorderLinesDoneFragment.recyclerViewIntakeorderLinesDone.setAdapter(cIntakeorderMATLine.getIntakeorderMATLineDoneAdapter());
        IntakeorderLinesDoneFragment.recyclerViewIntakeorderLinesDone.setLayoutManager(new LinearLayoutManager(cAppExtension.context));
        IntakeorderLinesDoneFragment.recyclerViewIntakeorderLinesDone.setVisibility(View.VISIBLE);

        IntakeorderLinesActivity.pChangeTabCounterText(cText.pDoubleToStringStr(cIntakeorder.currentIntakeOrder.pQuantityHandledDbl()) + "/" + cText.pDoubleToStringStr(cIntakeorder.currentIntakeOrder.pQuantityTotalDbl()));

    }

    //End Region Private Methods

      private void mSetRecyclerOnScrollListener() {
        recyclerViewIntakeorderLinesDone.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutmanager = (LinearLayoutManager)recyclerView.getLayoutManager();
                if (dy < 0) {
                    int itemPosition = layoutmanager.findFirstCompletelyVisibleItemPosition();

                    if(itemPosition==0){
                        //cUserInterface.pShowToastMessage(thisContext, "Show", null);
                        // Prepare the View for the animation
                        recyclerSearchView.setVisibility(View.VISIBLE);
                        recyclerSearchView.setAlpha(0.0f);

                        // Start the animation
                        recyclerSearchView.animate()
                                //.translationY(recyclerSearchView.getHeight())
                                .translationY(0)
                                .alpha(1.0f)
                                .setListener(null);

                    }

                } else {
                    int itemPosition = layoutmanager.findFirstCompletelyVisibleItemPosition();

                    if(itemPosition>1){// your *second item your recyclerview
                        // Start the animation
                        recyclerSearchView.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    private void mSetSearchListener() {
        //make whole view clickable
        this.recyclerSearchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerSearchView.setIconified(false);
            }
        });

        //query entered
        this.recyclerSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String pvQueryTextStr) {
                cIntakeorderMATLine.getIntakeorderMATLineDoneAdapter().pSetFilter(pvQueryTextStr);
                return true;
            }
        });
    }

    public static void pNoLinesAvailable(Boolean pvEnabledBln) {

        if (IntakeorderLinesActivity.currentLineFragment != null && IntakeorderLinesActivity.currentLineFragment instanceof IntakeorderLinesDoneFragment) {

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

            FragmentTransaction fragmentTransaction = cAppExtension.fragmentManager.beginTransaction();
            NothingHereFragment fragment = new NothingHereFragment();
            fragmentTransaction.replace(R.id.fragmentIntakeLinesDone, fragment);
            fragmentTransaction.commit();

            IntakeorderLinesActivity.pChangeTabCounterText(cText.pDoubleToStringStr(cIntakeorder.currentIntakeOrder.pQuantityHandledDbl()) + "/" + cText.pDoubleToStringStr(cIntakeorder.currentIntakeOrder.pQuantityTotalDbl()));

        }





    }


}
