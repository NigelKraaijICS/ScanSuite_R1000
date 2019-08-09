package nl.icsvertex.scansuite.fragments.dialogs;


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
import android.widget.Button;

import java.util.List;

import ICS.Interfaces.iICSDefaultFragment;
import SSU_WHS.Picken.PickorderBarcodes.cPickorderBarcodeAdapter;
import SSU_WHS.Picken.PickorderBarcodes.cPickorderBarcodeEntity;
import SSU_WHS.General.cPublicDefinitions;
import nl.icsvertex.scansuite.R;


public class BarcodePickerFragment extends DialogFragment implements iICSDefaultFragment {
    RecyclerView barcodeRecyclerview;
    Button buttonClose;
    DialogFragment thisFragment;
    cPickorderBarcodeAdapter pickorderBarcodeAdapter;
    List<cPickorderBarcodeEntity> barcodes;
    Context thisContext;

    public BarcodePickerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_barcode_picker, container);
        return rootview;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        barcodes = (List<cPickorderBarcodeEntity>)bundle.getSerializable(cPublicDefinitions.BARCODEFRAGMENT_LIST_TAG);

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
        barcodeRecyclerview = getView().findViewById(R.id.barcodeRecyclerview);
        buttonClose = getView().findViewById(R.id.buttonClose);
    }

    @Override
    public void mSetViewModels() {

    }

    @Override
    public void mFieldsInitialize() {
        mFillRecyclerView();
    }

    @Override
    public void mSetListeners() {
        mSetCloseListener();
    }
    private void mSetCloseListener() {
        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                thisFragment.dismiss();
            }
        });
    }
    private void mFillRecyclerView() {
        pickorderBarcodeAdapter = new cPickorderBarcodeAdapter(thisContext);
        barcodeRecyclerview.setHasFixedSize(false);
        barcodeRecyclerview.setAdapter(pickorderBarcodeAdapter);
        barcodeRecyclerview.setLayoutManager(new LinearLayoutManager(thisContext));

        pickorderBarcodeAdapter.setPickorderBarcodes(barcodes);
    }


}
