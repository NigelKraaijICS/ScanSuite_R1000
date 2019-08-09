package nl.icsvertex.scansuite.fragments.support;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ICS.Interfaces.iICSDefaultFragment;
import ICS.Utils.cDeviceInfo;
import nl.icsvertex.scansuite.R;

public class SupportDeviceFragment extends Fragment implements iICSDefaultFragment {
    TextView textViewDeviceManufacturer;
    TextView textViewDeviceBrand;
    TextView textViewDeviceModel;
    TextView textViewSerialnumber;
    TextView textViewAndroidversion;

    public SupportDeviceFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_support_device, container, false);
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
        textViewDeviceManufacturer = getView().findViewById(R.id.textViewDeviceManufacturer);
        textViewDeviceBrand = getView().findViewById(R.id.textViewDeviceBrand);
        textViewDeviceModel = getView().findViewById(R.id.textViewDeviceModel);
        textViewSerialnumber = getView().findViewById(R.id.textViewSerialnumber);
        textViewAndroidversion = getView().findViewById(R.id.textViewAndroidversion);
    }

    @Override
    public void mSetViewModels() {

    }

    @Override
    public void mFieldsInitialize() {
        textViewDeviceManufacturer.setText(cDeviceInfo.getDeviceManufacturer());
        textViewDeviceBrand.setText(cDeviceInfo.getDeviceBrand());
        textViewDeviceModel.setText(cDeviceInfo.getDeviceModel());
        textViewSerialnumber.setText(cDeviceInfo.getSerialnumber());
        textViewAndroidversion.setText(cDeviceInfo.getAndroidBuildVersion());
    }

    @Override
    public void mSetListeners() {

    }
}
