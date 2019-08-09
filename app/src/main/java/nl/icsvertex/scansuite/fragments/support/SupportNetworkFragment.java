package nl.icsvertex.scansuite.fragments.support;


import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import ICS.Interfaces.iICSDefaultFragment;
import SSU_WHS.General.cPublicDefinitions;
import ICS.Utils.cConnection;
import ICS.Utils.cDeviceInfo;
import nl.icsvertex.scansuite.R;

public class SupportNetworkFragment extends Fragment implements iICSDefaultFragment {
    TextView textViewConnectionType;
    TextView textViewSSID;
    TextView textViewMyIp;
    TextView textViewInternetConnection;
    ImageButton buttonWifiSettings;

    public SupportNetworkFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_support_network, container, false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        mFragmentInitialize();

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == cPublicDefinitions.CHANGEWIFI_REQUESTCODE) {
            mFieldsInitialize();
        }
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
        textViewConnectionType = getView().findViewById(R.id.textViewConnectionType);
        textViewSSID = getView().findViewById(R.id.textViewSSID);
        textViewMyIp = getView().findViewById(R.id.textViewMyIp);
        textViewInternetConnection = getView().findViewById(R.id.textViewInternetConnection);
        buttonWifiSettings = getView().findViewById(R.id.buttonWifiSettings);
    }

    @Override
    public void mSetViewModels() {

    }

    @Override
    public void mFieldsInitialize() {
        //connection?
        cConnection.connectionType connectionType = cConnection.getCurrentConnectionType();
        textViewConnectionType.setText(R.string.connectiontype_unknown);
        if (connectionType == cConnection.connectionType.NONE) {
            textViewConnectionType.setText(R.string.connectiontype_none);
        }
        if (connectionType == cConnection.connectionType.WIFI) {
            textViewConnectionType.setText(R.string.connectiontype_wifi);
            textViewSSID.setText(cDeviceInfo.getSSID());
        }
        if (connectionType == cConnection.connectionType.MOBILEDATA) {
            textViewConnectionType.setText(R.string.connectiontype_mobiledata);
        }
        if (connectionType == cConnection.connectionType.ETHERNET) {
            textViewConnectionType.setText(R.string.connectiontype_ethernet);
        }
        if (connectionType == cConnection.connectionType.UNKNOWN) {
            textViewConnectionType.setText(R.string.connectiontype_unknown);
        }
        textViewMyIp.setText(cDeviceInfo.getIpAddress());
        //internet?
        if (cConnection.isGooglePingableBln()) {
            textViewInternetConnection.setText(R.string.internet_connected);
        }
        else {
            textViewInternetConnection.setText(R.string.internet_not_connected);
        }
    }

    @Override
    public void mSetListeners() {
        mSetWifiListener();
    }

    private void mSetWifiListener() {
        buttonWifiSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(Settings.ACTION_WIFI_SETTINGS);
                if (intent.resolveActivity(getView().getContext().getPackageManager()) != null) {
                    startActivityForResult(intent, cPublicDefinitions.CHANGEWIFI_REQUESTCODE);
                }
            }
        });
    }
}
