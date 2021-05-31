package nl.icsvertex.scansuite.Fragments.Support;


import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Objects;

import ICS.Interfaces.iICSDefaultFragment;
import ICS.Utils.cConnection;
import ICS.Utils.cDeviceInfo;
import ICS.Utils.cUserInterface;
import SSU_WHS.General.cPublicDefinitions;
import nl.icsvertex.scansuite.R;

public class SupportNetworkFragment extends Fragment implements iICSDefaultFragment {

    private  TextView textViewConnectionType;
    private  TextView textViewSSID;
    private  TextView textViewMyIp;
    private  TextView textViewInternetConnection;
    private  ImageButton buttonWifiSettings;

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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        this.mFragmentInitialize();
    }
    @Override
    public void onResume() {
        super.onResume();
        this.mFragmentInitialize();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == cPublicDefinitions.CHANGEWIFI_REQUESTCODE) {
            this.mFieldsInitialize();
        }
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
            this.textViewConnectionType = getView().findViewById(R.id.textViewConnectionType);
            this.textViewSSID = getView().findViewById(R.id.textViewSSID);
            this.textViewMyIp = getView().findViewById(R.id.textViewMyIp);
            this.textViewInternetConnection = getView().findViewById(R.id.textViewInternetConnection);
            this.buttonWifiSettings = getView().findViewById(R.id.buttonWifiSettings);
        }
    }


    @Override
    public void mFieldsInitialize() {
        //connection?
        cConnection.connectionType connectionType = cConnection.getCurrentConnectionType();
        this.textViewConnectionType.setText(R.string.connectiontype_unknown);

        if (connectionType == cConnection.connectionType.NONE) {
            this.textViewConnectionType.setText(R.string.connectiontype_none);
        }

        if (connectionType == cConnection.connectionType.WIFI) {
            this.textViewConnectionType.setText(R.string.connectiontype_wifi);
            this.textViewSSID.setText(cDeviceInfo.getSSID());
        }

        if (connectionType == cConnection.connectionType.MOBILEDATA) {
            this.textViewConnectionType.setText(R.string.connectiontype_mobiledata);
        }

        if (connectionType == cConnection.connectionType.ETHERNET) {
            this.textViewConnectionType.setText(R.string.connectiontype_ethernet);
        }

        if (connectionType == cConnection.connectionType.UNKNOWN) {
            this.textViewConnectionType.setText(R.string.connectiontype_unknown);
        }

        this.textViewMyIp.setText(cDeviceInfo.getIpAddress());

        //internet?
        if (cConnection.isGooglePingableBln()) {
            this.textViewInternetConnection.setText(R.string.internet_connected);
        }
        else {
            this.textViewInternetConnection.setText(R.string.internet_not_connected);
        }
    }

    @Override
    public void mSetListeners() {
        this.mSetWifiListener();
    }

    private void mSetWifiListener() {
        this.buttonWifiSettings.setOnClickListener(v -> {
            Intent intent  = new Intent(Settings.ACTION_WIFI_SETTINGS);
            if (intent.resolveActivity(requireView().getContext().getPackageManager()) != null) {
                startActivityForResult(intent, cPublicDefinitions.CHANGEWIFI_REQUESTCODE);
            }
        });
    }
}
