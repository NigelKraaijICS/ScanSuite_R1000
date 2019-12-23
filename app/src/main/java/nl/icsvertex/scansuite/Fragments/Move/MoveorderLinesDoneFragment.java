package nl.icsvertex.scansuite.Fragments.Move;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
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
import SSU_WHS.Picken.PickorderLines.cPickorderLine;
import nl.icsvertex.scansuite.Activities.Move.MoveorderLinesActivity;
import nl.icsvertex.scansuite.Fragments.Dialogs.NothingHereFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.SendOrderFragment;
import nl.icsvertex.scansuite.R;

public class MoveorderLinesDoneFragment extends Fragment implements iICSDefaultFragment {

    //Region Public Properties
    //End Region Public Properties

    //Region Private Properties

    private static RecyclerView recyclerViewMoveorderLinesDone;
    private SearchView recyclerSearchView;

    private static  TextView textViewSelectedLine;

    //End Region Private Properties

    //Region Constructor
    public MoveorderLinesDoneFragment() {

    }
    //End Region Constructor

    //Region Default Methods
    @Override
    public View onCreateView(LayoutInflater pvInflater, ViewGroup pvContainer,
                             Bundle pvSavedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = pvInflater.inflate(R.layout.fragment_moveorder_lines_done, pvContainer, false);
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

            MoveorderLinesActivity.currentLineFragment = this;

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
        this.pGetData(cMoveorder.currentMoveOrder.pGetLinesHandledFromDatabasObl());
    }

    @Override
    public void mFindViews() {
        this.recyclerSearchView = getView().findViewById(R.id.recyclerSearchView);
        this.recyclerViewMoveorderLinesDone = getView().findViewById(R.id.recyclerViewMoveorderLinesDone);
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

    public static void pGetData(List<cMoveorderLine> pvDataObl) {

        MoveorderLinesDoneFragment.mFillRecycler(pvDataObl);
    }

    private static void mFillRecycler(List<cMoveorderLine> pvDataObl) {

        if (pvDataObl.size() == 0) {
            MoveorderLinesDoneFragment.pNoLinesAvailable(true);
            return;
        }

        MoveorderLinesDoneFragment.pNoLinesAvailable(false);

        cMoveorderLine.getMoveorderLineDoneAdapter().pFillData(pvDataObl);
        MoveorderLinesDoneFragment.recyclerViewMoveorderLinesDone.setHasFixedSize(false);
        MoveorderLinesDoneFragment.recyclerViewMoveorderLinesDone.setAdapter(cMoveorderLine.getMoveorderLineDoneAdapter());
        MoveorderLinesDoneFragment.recyclerViewMoveorderLinesDone.setLayoutManager(new LinearLayoutManager(cAppExtension.context));
        MoveorderLinesDoneFragment.recyclerViewMoveorderLinesDone.setVisibility(View.VISIBLE);

        MoveorderLinesActivity.pChangeTabCounterText(cText.pDoubleToStringStr(cMoveorder.currentMoveOrder.pQuantityHandledDbl()) + "/" + cText.pDoubleToStringStr(cMoveorder.currentMoveOrder.pQuantityTotalDbl()));

    }

    //End Region Private Methods





    private void mSetRecyclerOnScrollListener() {
        recyclerViewMoveorderLinesDone.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                cPickorderLine.getPickorderLinePickedAdapter().pSetFilter(pvQueryTextStr);
                return true;
            }
        });
    }

    public static void pNoLinesAvailable(Boolean pvEnabledBln) {

        if (MoveorderLinesActivity.currentLineFragment != null && MoveorderLinesActivity.currentLineFragment instanceof MoveorderLinesDoneFragment) {

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

            MoveorderLinesDoneFragment.recyclerViewMoveorderLinesDone.setVisibility(View.INVISIBLE);

            FragmentTransaction fragmentTransaction = cAppExtension.fragmentManager.beginTransaction();
            NothingHereFragment fragment = new NothingHereFragment();
            fragmentTransaction.replace(R.id.fragmentMoveorderLinesDone, fragment);
            fragmentTransaction.commit();

            MoveorderLinesActivity.pChangeTabCounterText(cText.pDoubleToStringStr(cMoveorder.currentMoveOrder.pQuantityHandledDbl()) + "/" + cText.pDoubleToStringStr(cMoveorder.currentMoveOrder.pQuantityTotalDbl()));

        }

    }


}
