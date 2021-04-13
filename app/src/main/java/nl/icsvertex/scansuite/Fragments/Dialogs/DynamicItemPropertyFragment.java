package nl.icsvertex.scansuite.Fragments.Dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import ICS.Interfaces.iICSDefaultFragment;
import ICS.cAppExtension;
import SSU_WHS.Basics.PropertyGroupProperty.cPropertyGroupPropertyAdapter;
import SSU_WHS.Picken.PickorderLinePropertyValue.cPickorderLinePropertyValue;
import SSU_WHS.Picken.PickorderLinePropertyValue.cPickorderLinePropertyValueInputAdapter;
import SSU_WHS.Picken.PickorderLinePropertyValue.cPickorderLinePropertyValueNoInputAdapter;
import nl.icsvertex.scansuite.R;

public class DynamicItemPropertyFragment extends Fragment implements iICSDefaultFragment {

    private RecyclerView itemPropertyRecyclerview;

    private final ArrayList<cPickorderLinePropertyValue> dataObl;
    private cPickorderLinePropertyValueInputAdapter pickorderLinePropertyAdapter;
    private cPickorderLinePropertyValueInputAdapter getPickorderLinePropertyAdapter(){
        if (this.pickorderLinePropertyAdapter == null) {
            this.pickorderLinePropertyAdapter = new cPickorderLinePropertyValueInputAdapter();
        }

        return  pickorderLinePropertyAdapter;
    }

    public DynamicItemPropertyFragment(ArrayList<cPickorderLinePropertyValue> pvArrayList) {
        this.dataObl =  pvArrayList;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dynamic_item_property, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onResume() {
        super.onResume();
        this.mFragmentInitialize();
    }


    @Override
    public void mFragmentInitialize() {
        this.mFindViews();
        this.mFieldsInitialize();
        this.mSetListeners();
    }

    @Override
    public void mFindViews() {
        this.itemPropertyRecyclerview =  getView().findViewById(R.id.itemPropertyRecyclerview);

    }

    @Override
    public void mFieldsInitialize() {
        this.mFillRecycler();
    }

    @Override
    public void mSetListeners() {

    }


    private void mFillRecycler() {

        if (this.dataObl == null || this.dataObl.size() == 0) {
            return;
        }

        //Show the recycler view
        this.getPickorderLinePropertyAdapter().pFillData(this.dataObl);
        this.itemPropertyRecyclerview.setHasFixedSize(false);
        this.itemPropertyRecyclerview.setVisibility(View.VISIBLE);
        this.itemPropertyRecyclerview.setHasFixedSize(false);
        this.itemPropertyRecyclerview.setAdapter(this.getPickorderLinePropertyAdapter());
        this.itemPropertyRecyclerview.setLayoutManager(new LinearLayoutManager(cAppExtension.context));
    }
}
