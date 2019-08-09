package nl.icsvertex.scansuite.fragments.main;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import ICS.Interfaces.iICSDefaultFragment;
import ICS.Utils.cConnection;
import ICS.Utils.cDeviceInfo;
import ICS.Utils.cText;
import ICS.Utils.cUserInterface;
import SSU_WHS.General.cAppExtension;
import nl.icsvertex.scansuite.activities.general.LoginActivity;
import nl.icsvertex.scansuite.R;
import nl.icsvertex.scansuite.activities.general.MainDefaultActivity;
import nl.icsvertex.scansuite.fragments.dialogs.NoConnectionFragment;

public class HomeFragment extends Fragment implements iICSDefaultFragment {

    ImageView imageLogo;
    TextView textViewDevicedetails;
    CardView cardViewDeviceDetails;
    Button buttonLogin;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
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
        imageLogo = getView().findViewById(R.id.imageLogo);
        cardViewDeviceDetails = getView().findViewById(R.id.cardViewDeviceDetails);
        textViewDevicedetails = getView().findViewById(R.id.textViewDeviceDetails);
        buttonLogin = getView().findViewById(R.id.buttonLogin);
    }

    @Override
    public void mSetViewModels() {

    }

    @Override
    public void mFieldsInitialize() {
        mSetDeviceInfo();
        buttonLogin.setVisibility(View.VISIBLE);
        mBounceLogo();
    }

    @Override
    public void mSetListeners() {
        mSetLogoListener();
        mSetLoginButtonListener();
    }

    private void mBounceLogo() {
        cUserInterface.doBoing(imageLogo);
    }

    private void mSetDeviceInfo() {
        String l_deviceInfoStr = "";
        l_deviceInfoStr += getString(R.string.device_manufacturer) + cText.LABEL_VALUE_SEPARATOR + cDeviceInfo.getDeviceManufacturer() + cText.NEWLINE;
        l_deviceInfoStr += getString(R.string.device_brand)  + cText.LABEL_VALUE_SEPARATOR + cDeviceInfo.getDeviceBrand() + cText.NEWLINE;
        l_deviceInfoStr += getString(R.string.device_model)  + cText.LABEL_VALUE_SEPARATOR + cDeviceInfo.getDeviceModel() + cText.NEWLINE;
        l_deviceInfoStr += getString(R.string.device_serialnumber)  + cText.LABEL_VALUE_SEPARATOR + cDeviceInfo.getSerialnumber() + cText.NEWLINE;
        l_deviceInfoStr += getString(R.string.application_version)  + cText.LABEL_VALUE_SEPARATOR + cDeviceInfo.getAppVersion();

        textViewDevicedetails.setText(l_deviceInfoStr);
    }

    private void mSetLogoListener() {
        imageLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBounceLogo();
                if (cardViewDeviceDetails.getVisibility() == View.VISIBLE) {
                    cardViewDeviceDetails.setVisibility(View.INVISIBLE);
                }
                else {
                    cardViewDeviceDetails.setVisibility(View.VISIBLE);
                }
            }
        });
    }
    private void mSetLoginButtonListener() {
       buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cUserInterface.mShowGettingData();


                if (cAppExtension.context instanceof MainDefaultActivity) {

                    if (cConnection.isInternetConnectedBln()) {
                        new Thread(new Runnable() {
                            public void run() {
                                ((MainDefaultActivity)cAppExtension.context).mLetsGetThisPartyStartedOrNot();
                            }
                        }).start();
                    }
                    else {
                        final NoConnectionFragment noConnectionFragment = new NoConnectionFragment();
                        noConnectionFragment.setCancelable(true);
                        noConnectionFragment.show(((MainDefaultActivity) cAppExtension.context).getSupportFragmentManager(), "NOCONNECTION");
                    }
                }
            }
        });
    }
    private void goLogin() {
        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivity(intent);
    }

    public void doAllDone() {
        goLogin();
    }

}

