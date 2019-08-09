package nl.icsvertex.scansuite.fragments.sort;

import android.app.AlertDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import SSU_WHS.PickorderLines.cPickorderLine;
import SSU_WHS.PickorderLines.cPickorderLineAdapter;
import SSU_WHS.PickorderLines.cPickorderLineEntity;
import SSU_WHS.PickorderLines.cPickorderLineViewModel;
import SSU_WHS.cPublicDefinitions;
import ICS.Utils.cSharedPreferences;
import nl.icsvertex.scansuite.fragments.NothingHereFragment;
import nl.icsvertex.scansuite.R;

public class SortorderLinesSortedFragment extends Fragment {
    Context thisContext;
    RecyclerView recyclerViewSortorderLinesSorted;
    SearchView recyclerSearchView;
    cPickorderLineEntity chosenSortorderLineEntity;
    ConstraintLayout resetPicklineView;
    String currentUser;
    String currentBranch;
    String currentOrder;

    private cPickorderLineViewModel pickorderLineViewModel;
    private cPickorderLineAdapter pickorderLinePickedAdapter;
    public SortorderLinesSortedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sortorder_lines_sorted, container, false);

    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        thisContext = this.getContext();

        currentOrder = cSharedPreferences.getSharedPreferenceString(cPublicDefinitions.PREFERENCE_CURRENT_ORDER, "");
        currentUser = cSharedPreferences.getSharedPreferenceString(cPublicDefinitions.PREFERENCE_CURRENT_USER, "");
        currentBranch = cSharedPreferences.getSharedPreferenceString(cPublicDefinitions.PREFERENCE_CURRENT_BRANCH, "");

        m_findViews();
        m_getData();
        m_setListeners();
    }
    private void m_findViews() {
        recyclerSearchView = getView().findViewById(R.id.recyclerSearchView);
        recyclerViewSortorderLinesSorted = getView().findViewById(R.id.recyclerViewSortorderLinesSorted);
        resetPicklineView = getView().findViewById(R.id.resetPicklineView);
    }
    private void m_getData() {
        pickorderLineViewModel = ViewModelProviders.of(this).get(cPickorderLineViewModel.class);
        pickorderLineViewModel.getHandledPickorderLineEntities().observe(this, new Observer<List<cPickorderLineEntity>>() {
            @Override
            public void onChanged(@Nullable List<cPickorderLineEntity> cPickorderLineEntities) {
                m_setSortorderLineRecycler(cPickorderLineEntities);
            }
        });
    }
    private void m_setSortorderLineRecycler(List<cPickorderLineEntity> pickorderLineEntities) {
        pickorderLinePickedAdapter = new cPickorderLineAdapter(thisContext);
        pickorderLinePickedAdapter.setPickorderLines(pickorderLineEntities);
        recyclerViewSortorderLinesSorted.setHasFixedSize(false);
        recyclerViewSortorderLinesSorted.setAdapter(pickorderLinePickedAdapter);
        recyclerViewSortorderLinesSorted.setLayoutManager(new LinearLayoutManager(thisContext));
        if (pickorderLineEntities.size() == 0) {
            mSetNoOrders();
        }
    }
    private void m_setListeners() {
        m_setRecyclerOnScrollListener();
        m_setSearchListener();
        m_setResetListener();
    }
    private void m_setResetListener() {
        resetPicklineView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_askResetLine();
            }
        });
    }
    private void m_askResetLine() {
        m_checkAndCloseOpenDialogs();
        AlertDialog.Builder builder = new AlertDialog.Builder(thisContext);
        builder.setTitle(R.string.message_reset_header);
        builder.setMessage(getString(R.string.message_reset_text));
        builder.setPositiveButton(R.string.button_reset, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                m_resetLine();
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
    private void m_resetLine() {
        pickorderLineViewModel.updateOrderLineLocalStatus(chosenSortorderLineEntity.getRecordid(), cPickorderLine.LOCALSTATUS_NEW);
        pickorderLineViewModel.updateOrderLineQuantity(chosenSortorderLineEntity.getRecordid(), 0d);
        Boolean linereset = pickorderLineViewModel.pickorderlineReset(currentUser, currentBranch, currentOrder, chosenSortorderLineEntity.getLineNo().longValue());
        if (linereset) {

        }
    }
    public void setChosenSortorderLine(cPickorderLineEntity sortorderLineEntity) {
        chosenSortorderLineEntity = sortorderLineEntity;
    }
    private void m_setRecyclerOnScrollListener() {
        recyclerViewSortorderLinesSorted.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutmanager = (LinearLayoutManager)recyclerView.getLayoutManager();
                int totalNumber = recyclerView.getAdapter().getItemCount();
                if (dy < 0) {
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
                    int itemPosition = layoutmanager.findFirstCompletelyVisibleItemPosition();

                    if(itemPosition>1){// your *second item your recyclerview
                        // Start the animation
                        recyclerSearchView.setVisibility(View.GONE);
//                        recyclerSearchView.animate()
//                                .translationY(0)
//                                //.translationY(0)
//                                .alpha(0.0f)
//                                .setListener(new AnimatorListenerAdapter() {
//                                    @Override
//                                    public void onAnimationEnd(Animator animation) {
//                                        super.onAnimationEnd(animation);
//                                        recyclerSearchView.setVisibility(View.GONE);
//                                    }
//                                });

                    }


                }
            }
        });
    }
    private void m_setSearchListener() {
        //make whole view clickable
        recyclerSearchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerSearchView.setIconified(false);
            }
        });
        //query entered
        recyclerSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String queryText) {
                pickorderLinePickedAdapter.setFilter(queryText);
                return true;
            }
        });
    }

    private void m_checkAndCloseOpenDialogs() {
        List<Fragment> fragments = getActivity().getSupportFragmentManager().getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                if (fragment instanceof DialogFragment) {
                    ((DialogFragment) fragment).dismiss();
                }
            }
        }
    }
    private void mSetNoOrders() {
        resetPicklineView.setVisibility(View.INVISIBLE);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        NothingHereFragment fragment = new NothingHereFragment();
        fragmentTransaction.replace(R.id.fragmentSortorderLinesSorted, fragment);
        fragmentTransaction.commit();
    }
}
