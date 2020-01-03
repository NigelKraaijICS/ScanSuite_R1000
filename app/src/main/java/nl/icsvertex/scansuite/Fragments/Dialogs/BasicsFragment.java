package nl.icsvertex.scansuite.Fragments.Dialogs;


import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ICS.Interfaces.iICSDefaultFragment;
import ICS.Utils.cUserInterface;
import SSU_WHS.Basics.Settings.cSetting;
import ICS.cAppExtension;
import nl.icsvertex.scansuite.R;


public class BasicsFragment extends DialogFragment implements iICSDefaultFragment {

    private static  RecyclerView recyclerViewSettings;

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
            BasicsFragment.recyclerViewSettings = getView().findViewById(R.id.recyclerViewSettings);
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
       BasicsFragment.recyclerViewSettings.setHasFixedSize(false);
        BasicsFragment. recyclerViewSettings.setAdapter(cSetting.getSettingsAdapter());
        BasicsFragment.recyclerViewSettings.setLayoutManager(new LinearLayoutManager(cAppExtension.context));
    }


}
