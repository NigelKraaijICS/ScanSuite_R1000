package nl.icsvertex.scansuite.Fragments.Sort;

import android.app.AlertDialog;

import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ICS.Interfaces.iICSDefaultFragment;
import ICS.Utils.cText;
import ICS.Utils.cUserInterface;
import SSU_WHS.Picken.PickorderLines.cPickorderLine;

import SSU_WHS.Picken.Pickorders.cPickorder;
import nl.icsvertex.scansuite.Activities.Sort.SortorderLinesActivity;
import ICS.cAppExtension;
import nl.icsvertex.scansuite.Fragments.Dialogs.NothingHereFragment;
import nl.icsvertex.scansuite.R;

public class SortorderLinesSortedFragment extends Fragment implements iICSDefaultFragment {

    //Region Public Properties

    //End Region Public Properties

    //Region Private Properties

    private RecyclerView recyclerViewSortorderLinesSorted;
    private SearchView recyclerSearchView;
    private ConstraintLayout resetPicklineView;

    //End Region Private Properties

    //Region Constructor
    public SortorderLinesSortedFragment() {

    }
    //End Region Constructor

    //Region Default Methods

    @Override
    public View onCreateView(LayoutInflater pvInflater, ViewGroup pvViewGroup,
                             Bundle pvSavedInstanceState) {
        // Inflate the layout for this fragment
        return pvInflater.inflate(R.layout.fragment_sortorder_lines_sorted, pvViewGroup, false);

    }
    @Override
    public void onViewCreated(@NonNull View pvView, @Nullable Bundle pvSavedInstanceState) {
        this.mFragmentInitialize();
    }

    @Override
    public void setUserVisibleHint(boolean pvIsVisibleToUserBln) {
        super.setUserVisibleHint(pvIsVisibleToUserBln);

        if (pvIsVisibleToUserBln) {

            SortorderLinesActivity.currentLineFragment = this;

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
        cUserInterface.pEnableScanner();
    }

    @Override
    public void mFindViews() {
            this.recyclerSearchView = getView().findViewById(R.id.recyclerSearchView);
            this.recyclerViewSortorderLinesSorted = getView().findViewById(R.id.recyclerViewSortorderLinesSorted);
            this.resetPicklineView = getView().findViewById(R.id.resetPicklineView);
    }


    @Override
    public void mFieldsInitialize() {

    }

    @Override
    public void mSetListeners() {
        this.mSetRecyclerOnScrollListener();
        this.mSetSearchListener();
        this.mSetResetListener();
    }

    //End Region iICSDefaultFragment defaults

    //Region Private Methods

    private void mGetData() {

        List<cPickorderLine> HandledLinesObl = cPickorder.currentPickOrder.pGetLinesHandledFromDatabasObl();
        this.mFillRecycler(HandledLinesObl);
    }

    private void mFillRecycler(List<cPickorderLine> pvDataObl) {

        if (pvDataObl.size() == 0) {
            this.mNoLinesAvailable(true);
            return;
        }

        cPickorderLine.getPickorderLinePickedAdapter().pFillData(pvDataObl);
        this.recyclerViewSortorderLinesSorted.setHasFixedSize(false);
        this.recyclerViewSortorderLinesSorted.setAdapter(cPickorderLine.getPickorderLinePickedAdapter());
        this.recyclerViewSortorderLinesSorted.setLayoutManager(new LinearLayoutManager(cAppExtension.context));

        SortorderLinesActivity.pChangeTabCounterText(cText.pDoubleToStringStr(cPickorder.currentPickOrder.pQuantityHandledDbl()) + "/" + cText.pDoubleToStringStr(cPickorder.currentPickOrder.pQuantityTotalDbl()));

    }

    private void mSetResetListener() {
        this.resetPicklineView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAskResetLine();
            }
        });
    }

    private void mAskResetLine() {

        AlertDialog.Builder builder = new AlertDialog.Builder(cAppExtension.context);
        builder.setTitle(R.string.message_reset_header);
        builder.setMessage(getString(R.string.message_reset_text));
        builder.setPositiveButton(R.string.button_reset, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                SortorderLinesActivity.pHandleLineReset();
            }
        });

        builder.setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //do nothing (close the dialog)
            }
        });

        builder.show();
    }

    private void mSetRecyclerOnScrollListener() {
        this.recyclerViewSortorderLinesSorted.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutmanager = (LinearLayoutManager)recyclerView.getLayoutManager();
                if (dy < 0) {
                    assert layoutmanager != null;
                    int itemPosition = layoutmanager.findFirstCompletelyVisibleItemPosition();

                    if(itemPosition==0){
                        //cUserInterface.showToastMessage(thisContext, "Show", null);
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
                    assert layoutmanager != null;
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

    private void mNoLinesAvailable(Boolean pvEnabledBln) {

        if (SortorderLinesActivity.currentLineFragment != null && SortorderLinesActivity.currentLineFragment == this) {
            //Close no linesInt fragment if needed
            if (!pvEnabledBln) {


                this.resetPicklineView.setVisibility(View.VISIBLE);

                List<Fragment> fragments = cAppExtension.fragmentManager.getFragments();
                for (Fragment fragment : fragments) {
                    if (fragment instanceof NothingHereFragment || fragment instanceof NothingHereFragment ) {
                        FragmentTransaction fragmentTransaction = cAppExtension.fragmentManager.beginTransaction();
                        fragmentTransaction.remove(fragment);
                        fragmentTransaction.commit();
                    }
                }

                return;

            }

            //Hide the recycler view
            this.recyclerViewSortorderLinesSorted.setVisibility(View.INVISIBLE);
            this.resetPicklineView.setVisibility(View.INVISIBLE);

            //Show nothing there fragment
            FragmentTransaction fragmentTransaction = cAppExtension.fragmentManager.beginTransaction();
            NothingHereFragment fragment = new NothingHereFragment();
            fragmentTransaction.replace(R.id.fragmentSortorderLinesSorted, fragment);
            fragmentTransaction.commit();

            //Change tabcounter text
            SortorderLinesActivity.pChangeTabCounterText(cText.pDoubleToStringStr(cPickorder.currentPickOrder.pQuantityHandledDbl()) + "/" + cText.pDoubleToStringStr(cPickorder.currentPickOrder.pQuantityTotalDbl()));
        }
    }

    //End Region Private Methods


}
