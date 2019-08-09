package nl.icsvertex.scansuite.fragments.pick;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import SSU_WHS.Picken.PickorderLines.cPickorderLineAdapter;
import SSU_WHS.Picken.PickorderLines.cPickorderLineEntity;
import SSU_WHS.Picken.PickorderLines.cPickorderLineViewModel;
import SSU_WHS.General.cPublicDefinitions;
import ICS.Utils.cSharedPreferences;
import nl.icsvertex.scansuite.R;

public class PickorderLinesTotalFragment extends Fragment {
    Context thisContext;

    String currentUser;
    String currentBranch;
    String chosenOrder;

    RecyclerView recyclerViewPickorderLinesTotal;
    private cPickorderLineViewModel pickorderLineViewModel;
    private cPickorderLineAdapter pickorderLineAdapter;

    public PickorderLinesTotalFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pickorder_lines_total, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        thisContext = this.getContext();
        currentUser = cSharedPreferences.getSharedPreferenceString(cPublicDefinitions.PREFERENCE_CURRENT_USER, "");
        currentBranch = cSharedPreferences.getSharedPreferenceString(cPublicDefinitions.PREFERENCE_CURRENT_BRANCH, "");

        Intent intent = getActivity().getIntent();
        chosenOrder = intent.getStringExtra(cPublicDefinitions.PICKING_CHOSENORDER);

        m_findViews();
        m_getData();
    }

    private void m_findViews() {
        recyclerViewPickorderLinesTotal = getView().findViewById(R.id.recyclerViewPickorderLinesTotal);
    }
    private void m_getData() {
        pickorderLineViewModel = ViewModelProviders.of(this).get(cPickorderLineViewModel.class);
        pickorderLineViewModel.getTotalPickorderLineEntities().observe(this, new Observer<List<cPickorderLineEntity>>() {
            @Override
            public void onChanged(@Nullable List<cPickorderLineEntity> cPickorderLineEntities) {
                m_setPickorderLineRecycler(cPickorderLineEntities);
            }
        });
    }

    private void m_setPickorderLineRecycler(List<cPickorderLineEntity> pickorderLinesEntities) {
        pickorderLineAdapter = new cPickorderLineAdapter(thisContext);
        recyclerViewPickorderLinesTotal.setHasFixedSize(false);
        recyclerViewPickorderLinesTotal.setAdapter(pickorderLineAdapter);
        recyclerViewPickorderLinesTotal.setLayoutManager(new LinearLayoutManager(thisContext));

        pickorderLineAdapter.setPickorderLines(pickorderLinesEntities);
    }
}
