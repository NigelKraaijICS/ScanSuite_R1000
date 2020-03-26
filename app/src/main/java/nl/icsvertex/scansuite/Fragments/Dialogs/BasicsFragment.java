package nl.icsvertex.scansuite.Fragments.Dialogs;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ICS.Interfaces.iICSDefaultFragment;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Basics.Settings.cSettingsAdapter;
import nl.icsvertex.scansuite.R;


public class BasicsFragment extends DialogFragment implements iICSDefaultFragment {

    private   RecyclerView recyclerViewSettings;

    private cSettingsAdapter settingsAdapter;
    private  cSettingsAdapter getSettingsAdapter(){
        if (this.settingsAdapter == null) {
            this.settingsAdapter = new cSettingsAdapter();
        }

        return  settingsAdapter;
    }

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
       this.mFragmentInitialize();
    }
    @Override
    public void mFragmentInitialize() {
        this.mFindViews();
        this.mFieldsInitialize();
        this.mSetListeners();
        cUserInterface.pEnableScanner();
    }

    @Override
    public void mFindViews() {

        if (getView() != null) {
            this.recyclerViewSettings = getView().findViewById(R.id.recyclerViewSettings);
        }

    }



    @Override
    public void mFieldsInitialize() {
        this.mFillRecycler();
    }

    @Override
    public void mSetListeners() {

    }

    private void mFillRecycler() {
        this.mSetSettingsRecycler();
    }

    private void mSetSettingsRecycler() {
        this.recyclerViewSettings.setHasFixedSize(false);
        this. recyclerViewSettings.setAdapter(this.getSettingsAdapter());
        this.recyclerViewSettings.setLayoutManager(new LinearLayoutManager(cAppExtension.context));
    }


}
