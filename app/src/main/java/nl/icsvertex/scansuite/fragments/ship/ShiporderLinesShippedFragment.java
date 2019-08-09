package nl.icsvertex.scansuite.fragments.ship;

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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import SSU_WHS.Picken.PickorderLinePackAndShip.cPickorderLinePackAndShipEntity;
import SSU_WHS.Picken.PickorderLinePackAndShip.cPickorderLinePackAndShipGroupBySourceNo;
import SSU_WHS.Picken.PickorderLinePackAndShip.cPickorderLinePackAndShipGroupBySourceNoAdapter;
import SSU_WHS.Picken.PickorderLinePackAndShip.cPickorderLinePackAndShipViewModel;
import SSU_WHS.Picken.PickorderLines.cPickorderLine;
import SSU_WHS.General.cPublicDefinitions;
import ICS.Utils.cSharedPreferences;
import nl.icsvertex.scansuite.R;

public class ShiporderLinesShippedFragment extends Fragment {
    Context thisContext;
    RecyclerView recyclerViewShiporderLinesShipped;
    SearchView recyclerSearchView;
    cPickorderLinePackAndShipEntity chosenShiporderLineEntity;
    ConstraintLayout resetPicklineView;
    String currentUser;
    String currentBranch;
    String currentOrder;

    private cPickorderLinePackAndShipViewModel pickorderLinePackAndShipViewModel;
    private cPickorderLinePackAndShipGroupBySourceNoAdapter pickorderLinePackAndShipGroupBySourceNoAdapter;

    public ShiporderLinesShippedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shiporder_lines_shipped, container, false);

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
        recyclerViewShiporderLinesShipped = getView().findViewById(R.id.recyclerViewShiporderLinesShipped);
        resetPicklineView = getView().findViewById(R.id.resetPicklineView);
    }
    private void m_getData() {
        pickorderLinePackAndShipViewModel = ViewModelProviders.of(this).get(cPickorderLinePackAndShipViewModel.class);
        pickorderLinePackAndShipViewModel.getHandledPickorderinePackAndShipEntitiesDistinctSourceno().observe(this, new Observer<List<cPickorderLinePackAndShipGroupBySourceNo>>() {
            @Override
            public void onChanged(@Nullable List<cPickorderLinePackAndShipGroupBySourceNo> pickorderLinePackAndShipGroupBySourceNos) {
                m_setShiporderLineRecycler(pickorderLinePackAndShipGroupBySourceNos);
            }
        });
    }
    private void m_setShiporderLineRecycler(List<cPickorderLinePackAndShipGroupBySourceNo> pickorderLinePackAndShipGroupBySourceNos) {
        pickorderLinePackAndShipGroupBySourceNoAdapter = new cPickorderLinePackAndShipGroupBySourceNoAdapter(thisContext);
        //pickorderLinePackAndShipAdapter = new cPickorderLinePackAndShipAdapter(thisContext);
        recyclerViewShiporderLinesShipped.setHasFixedSize(false);
        recyclerViewShiporderLinesShipped.setAdapter(pickorderLinePackAndShipGroupBySourceNoAdapter);
        recyclerViewShiporderLinesShipped.setLayoutManager(new LinearLayoutManager(thisContext));
        pickorderLinePackAndShipGroupBySourceNoAdapter.setPickorderLinePacikAndShips(pickorderLinePackAndShipGroupBySourceNos);
    }
    private void m_setListeners() {
        m_setRecyclerOnScrollListener();
        m_setSearchListener();
        //m_setResetListener();
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
        pickorderLinePackAndShipViewModel.updateOrderLinePackAndShipLocalStatus(chosenShiporderLineEntity.getRecordid(), cPickorderLine.LOCALSTATUS_NEW);
        pickorderLinePackAndShipViewModel.updateOrderLinePackAndShipQuantity(chosenShiporderLineEntity.getRecordid(), 0d);

//        Boolean linereset = pickorderLinePackAndShipViewModel.pickorderlinePackAndShipReset(currentUser, currentBranch, currentOrder, chosenShiporderLineEntity.getLineNo().longValue());
//        if (linereset) {
//
//        }
    }

    public void setChosenShiporderLinePackAndShipEntity(cPickorderLinePackAndShipGroupBySourceNo shiporderLineEntity) {
        List<cPickorderLinePackAndShipEntity> pickorderLinePackAndShipEntities = pickorderLinePackAndShipViewModel.getNotHandledPickorderLinePackAndShipEntitiesBySourceNo(shiporderLineEntity.getSourceno());
        chosenShiporderLineEntity = pickorderLinePackAndShipEntities.get(0);
    }
    private void m_setRecyclerOnScrollListener() {
        recyclerViewShiporderLinesShipped.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
//                pickorderLinePackAndShipAdapter.setFilter(queryText);
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
}
