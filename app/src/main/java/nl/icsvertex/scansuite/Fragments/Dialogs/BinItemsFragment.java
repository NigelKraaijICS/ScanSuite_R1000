package nl.icsvertex.scansuite.Fragments.Dialogs;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;

import ICS.Interfaces.iICSDefaultFragment;
import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Basics.BinItem.cBinItem;
import SSU_WHS.Basics.BinItem.cBinItemAdapter;
import nl.icsvertex.scansuite.R;

public class BinItemsFragment extends DialogFragment implements iICSDefaultFragment {

    //Region Public Properties



    //End Region Public Properties

    //Region Private Properties
    private RecyclerView binRecyclerview;

    private cBinItemAdapter binItemAdapter;
    private cBinItemAdapter getBinItemAdapter(){
        if (this.binItemAdapter == null) {
            this.binItemAdapter = new cBinItemAdapter();
        }

        return  binItemAdapter;
    }

    private String binCodeStr;
    TextView textViewBIN;

    //End Region Private Properties


    //Region Constructor
    public BinItemsFragment(String pvBinCodeStr) {
        binCodeStr = pvBinCodeStr;
    }
    //End Region Constructor


    //Region Default Methods
    @Nullable
    @Override
    public View onCreateView(LayoutInflater pvInflater, ViewGroup pvContainer, Bundle pvSavedInstanceState) {
        return pvInflater.inflate(R.layout.fragment_binitems, pvContainer);
    }

    @Override
    public void onViewCreated(@NonNull View pvView, @Nullable Bundle pvSavedInstanceState) {
        this.mFragmentInitialize();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        cUserInterface.pEnableScanner();
        cAppExtension.dialogFragment = this;

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels - getResources().getDimensionPixelSize(R.dimen.default_double_margin);

        Objects.requireNonNull(Objects.requireNonNull(getDialog()).getWindow()).setLayout(width, height);
    }

    //End Region Default Methods

    //Region iICSDefaultFragment methods
    @Override
    public void mFragmentInitialize() {
        this.mFindViews();
        this.mFieldsInitialize();
        this.mSetListeners();
    }

    @Override
    public void mFindViews() {

        if (getView() != null) {
            this.textViewBIN = getView().findViewById(R.id.textViewBIN);
            this.binRecyclerview = getView().findViewById(R.id.binRecyclerview);

        }

    }


    @Override
    public void mFieldsInitialize() {
        this.textViewBIN.setText(this.binCodeStr);
        this.mGetData();
    }

    @Override
    public void mSetListeners() {

    }

    //End Region iICSDefaultFragment methods

    //Region Private Methods

    private void mGetData() {

        boolean webserviceResult;
        webserviceResult = cBinItem.pGetBinItemsViaWebserviceBln(this.binCodeStr);

        if (webserviceResult) {
            this.mSetBinItemRecycler();
        }
    }

    private void mSetBinItemRecycler() {
        this.binRecyclerview.setHasFixedSize(false);
        this.binRecyclerview.setAdapter(this.getBinItemAdapter());
        this.binRecyclerview.setLayoutManager(new LinearLayoutManager(cAppExtension.context));
    }

    //End Region Private Methods





}
