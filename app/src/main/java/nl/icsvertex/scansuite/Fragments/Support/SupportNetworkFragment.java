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

    private static TextView textViewConnectionType;
    private static TextView textViewSSID;
    private static TextView textViewMyIp;
    private static TextView textViewInternetConnection;
    private static ImageButton buttonWifiSettings;

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
            SupportNetworkFragment.textViewConnectionType = getView().findViewById(R.id.textViewConnectionType);
            SupportNetworkFragment.textViewSSID = getView().findViewById(R.id.textViewSSID);
            SupportNetworkFragment.textViewMyIp = getView().findViewById(R.id.textViewMyIp);
            SupportNetworkFragment.textViewInternetConnection = getView().findViewById(R.id.textViewInternetConnection);
            SupportNetworkFragment.buttonWifiSettings = getView().findViewById(R.id.buttonWifiSettings);
        }
    }


    @Override
    public void mFieldsInitialize() {
        //connection?
        cConnection.connectionType connectionType = cConnection.getCurrentConnectionType();
        SupportNetworkFragment.textViewConnectionType.setText(R.string.connectiontype_unknown);

        if (connectionType == cConnection.connectionType.NONE) {
            SupportNetworkFragment.textViewConnectionType.setText(R.string.connectiontype_none);
        }

        if (connectionType == cConnection.connectionType.WIFI) {
            SupportNetworkFragment.textViewConnectionType.setText(R.string.connectiontype_wifi);
            SupportNetworkFragment.textViewSSID.setText(cDeviceInfo.getSSID());
        }

        if (connectionType == cConnection.connectionType.MOBILEDATA) {
            SupportNetworkFragment.textViewConnectionType.setText(R.string.connectiontype_mobiledata);
        }

        if (connectionType == cConnection.connectionType.ETHERNET) {
            SupportNetworkFragment.textViewConnectionType.setText(R.string.connectiontype_ethernet);
        }

        if (connectionType == cConnection.connectionType.UNKNOWN) {
            SupportNetworkFragment.textViewConnectionType.setText(R.string.connectiontype_unknown);
        }

        SupportNetworkFragment.textViewMyIp.setText(cDeviceInfo.getIpAddress());

        //internet?
        if (cConnection.isGooglePingableBln()) {
            SupportNetworkFragment.textViewInternetConnection.setText(R.string.internet_connected);
        }
        else {
            SupportNetworkFragment.textViewInternetConnection.setText(R.string.internet_not_connected);
        }
    }

    @Override
    public void mSetListeners() {
        this.mSetWifiListener();
    }

    private void mSetWifiListener() {
        SupportNetworkFragment.buttonWifiSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(Settings.ACTION_WIFI_SETTINGS);
                if (intent.resolveActivity(Objects.requireNonNull(getView()).getContext().getPackageManager()) != null) {
                    startActivityForResult(intent, cPublicDefinitions.CHANGEWIFI_REQUESTCODE);
                }
            }
        });
    }
}
