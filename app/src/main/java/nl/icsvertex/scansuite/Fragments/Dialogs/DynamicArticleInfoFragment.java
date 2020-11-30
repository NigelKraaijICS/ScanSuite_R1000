package nl.icsvertex.scansuite.Fragments.Dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedHashMap;

import ICS.Interfaces.iICSDefaultFragment;
import ICS.cAppExtension;
import SSU_WHS.Basics.PropertyGroupProperty.cPropertyGroupPropertyAdapter;
import nl.icsvertex.scansuite.R;

public class DynamicArticleInfoFragment extends Fragment implements iICSDefaultFragment {

    private RecyclerView recyclerArticleInfo;
    private final String titleStr;
    private final LinkedHashMap<String, String> keyAndValueHashMap;

    private cPropertyGroupPropertyAdapter propertyGroupPropertyAdapter;
    private cPropertyGroupPropertyAdapter getPropertyGroupPropertyAdapter(){
        if (this.propertyGroupPropertyAdapter == null) {
            this.propertyGroupPropertyAdapter = new cPropertyGroupPropertyAdapter();
        }
        return  this.propertyGroupPropertyAdapter;
    }


    public DynamicArticleInfoFragment(String pvTitleStr, LinkedHashMap<String, String> pvKeyAndValueHashmap) {
        this.titleStr = pvTitleStr;
        this.keyAndValueHashMap = pvKeyAndValueHashmap;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dynamic_article_info, container, false);
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
        this.recyclerArticleInfo =  getView().findViewById(R.id.recyclerArticleInfo);

    }

    @Override
    public void mFieldsInitialize() {
        this.mFillRecycler();
    }

    @Override
    public void mSetListeners() {

    }

    private void mFillRecycler() {

        if (this.keyAndValueHashMap == null || this.keyAndValueHashMap.size() == 0) {
            return;
        }


        //Show the recycler view
        this.getPropertyGroupPropertyAdapter().pFillData(this.keyAndValueHashMap);
        this.recyclerArticleInfo.setHasFixedSize(false);
        this.recyclerArticleInfo.setAdapter(this.getPropertyGroupPropertyAdapter());
        this.recyclerArticleInfo.setLayoutManager(new LinearLayoutManager(cAppExtension.context));
        this.recyclerArticleInfo.setVisibility(View.VISIBLE);


    }
}