package nl.icsvertex.scansuite.fragments.dialogs;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ICS.Interfaces.iICSDefaultFragment;
import SSU_WHS.Settings.cSettingsAdapter;
import SSU_WHS.Settings.cSettingsEntity;
import SSU_WHS.Settings.cSettingsViewModel;
import SSU_WHS.cAppExtension;
import nl.icsvertex.scansuite.R;


public class BasicsFragment extends DialogFragment implements iICSDefaultFragment {

    RecyclerView recyclerViewSettings;
    private cSettingsViewModel settingsViewModel;
    private cSettingsAdapter settingsAdapter;

    public BasicsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_basics, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mFragmentInitialize();
    }
    @Override
    public void mFragmentInitialize() {
        mFindViews();
        mSetViewModels();
        mFieldsInitialize();
        mSetListeners();
    }

    @Override
    public void mFindViews() {
        recyclerViewSettings = getView().findViewById(R.id.recyclerViewSettings);
    }

    @Override
    public void mSetViewModels() {
        settingsViewModel = ViewModelProviders.of(this).get(cSettingsViewModel.class);
    }

    @Override
    public void mFieldsInitialize() {
        mFillRecycler();
    }

    @Override
    public void mSetListeners() {

    }

    private void mFillRecycler() {
        settingsAdapter = new cSettingsAdapter(cAppExtension.context);
        List<cSettingsEntity> settingEntities = settingsViewModel.getLocalSettings();
        m_setSettingsRecycler(settingEntities);
    }
    private void m_setSettingsRecycler(List<cSettingsEntity> settingsEntities) {
        recyclerViewSettings.setHasFixedSize(false);
        recyclerViewSettings.setAdapter(settingsAdapter);
        recyclerViewSettings.setLayoutManager(new LinearLayoutManager(cAppExtension.context));
        settingsAdapter.setSettings(settingsEntities);
    }


}
