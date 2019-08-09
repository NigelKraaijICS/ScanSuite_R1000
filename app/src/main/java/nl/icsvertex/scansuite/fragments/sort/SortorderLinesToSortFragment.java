package nl.icsvertex.scansuite.fragments.sort;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import SSU_WHS.PickorderLines.cPickorderLineAdapter;
import SSU_WHS.PickorderLines.cPickorderLineEntity;
import SSU_WHS.PickorderLines.cPickorderLineViewModel;
import SSU_WHS.Settings.cSettingsViewModel;
import SSU_WHS.cPublicDefinitions;
import ICS.Utils.cSharedPreferences;
import ICS.Utils.cUserInterface;
import nl.icsvertex.scansuite.R;
import nl.icsvertex.scansuite.activities.sort.SortorderLinesActivity;
import nl.icsvertex.scansuite.activities.sort.SortorderSortActivity;


public class SortorderLinesToSortFragment extends Fragment {

    Context thisContext;

    cSettingsViewModel settingsViewModel;

    String currentUser;
    String currentBranch;
    String chosenOrder;

    //region Settings

    //endregion Settings

    ConstraintLayout packingTableView;
    RecyclerView recyclerViewSortorderLinesTosort;
    private cPickorderLineViewModel sortorderLineViewModel;
    private cPickorderLineAdapter sortorderLineAdapter;

    public SortorderLinesToSortFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sortorder_lines_to_sort, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        thisContext = this.getContext();
        currentUser = cSharedPreferences.getSharedPreferenceString(cPublicDefinitions.PREFERENCE_CURRENT_USER, "");
        currentBranch = cSharedPreferences.getSharedPreferenceString(cPublicDefinitions.PREFERENCE_CURRENT_BRANCH, "");

        Intent intent = getActivity().getIntent();
        chosenOrder = intent.getStringExtra(cPublicDefinitions.PICKING_CHOSENORDER);
        settingsViewModel = ViewModelProviders.of(this).get(cSettingsViewModel.class);

        m_findViews();
        m_getSettings();
        m_fieldsInitialize();
        m_setListeners();
        m_getData();
    }

    private void m_findViews() {
        recyclerViewSortorderLinesTosort = getView().findViewById(R.id.recyclerViewSortorderLinesTosort);
        packingTableView = getView().findViewById(R.id.packingTableView);
    }
    private void m_getSettings() {

    }
    private void m_fieldsInitialize() {

    }
    private void m_setListeners() {
        mSetPackTableListener();
    }
    private void mSetPackTableListener() {
        packingTableView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(thisContext, SortorderSortActivity.class);
                cPickorderLineEntity chosenSortorderLine;
                chosenSortorderLine = ((SortorderLinesActivity)getActivity()).getChosenSortorderLine();
                if (chosenSortorderLine == null) {
                    cUserInterface.showToastMessage(getString(R.string.sortorder_nothing_selected), R.raw.headsupsound);
                    return;
                }
                intent.putExtra(cPublicDefinitions.SORTING_CHOSENITEMNO, chosenSortorderLine.getRecordid());
                startActivity(intent);
            }
        });
    }
    public void setChosenSortorderLine(cPickorderLineEntity sortorderLineEntity) {

    }
    private void m_getData() {
        sortorderLineViewModel = ViewModelProviders.of(this).get(cPickorderLineViewModel.class);
        sortorderLineViewModel.getNotHandledPickorderLineEntities().observe(this, new Observer<List<cPickorderLineEntity>>() {
            @Override
            public void onChanged(@Nullable List<cPickorderLineEntity> cPickorderLineEntities) {
                if (cPickorderLineEntities != null) {
                    m_setSortorderLineRecycler(cPickorderLineEntities);
                }

            }
        });
    }
    private void  m_setSortorderLineRecycler(List<cPickorderLineEntity> sortorderLinesNotHandledEntities) {
        sortorderLineAdapter = new cPickorderLineAdapter(thisContext);
        recyclerViewSortorderLinesTosort.setHasFixedSize(false);
        recyclerViewSortorderLinesTosort.setAdapter(sortorderLineAdapter);
        recyclerViewSortorderLinesTosort.setLayoutManager(new LinearLayoutManager(thisContext));
        sortorderLineAdapter.setPickorderLines(sortorderLinesNotHandledEntities);
    }

}
