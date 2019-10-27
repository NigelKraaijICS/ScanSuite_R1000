package nl.icsvertex.scansuite.Fragments.main;


import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.cardview.widget.CardView;
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
import ICS.cAppExtension;
import nl.icsvertex.scansuite.Activities.general.LoginActivity;
import nl.icsvertex.scansuite.R;
import nl.icsvertex.scansuite.Activities.general.MainDefaultActivity;
import nl.icsvertex.scansuite.Fragments.dialogs.NoConnectionFragment;

public class HomeFragment extends Fragment implements iICSDefaultFragment {

    //region Private Properties
    private ImageView imageLogo;
    private TextView textViewDevicedetails;
    private CardView cardViewDeviceDetails;
    private Button buttonLogin;
    //end region Private Properties

    //region Constructor
    public HomeFragment() {
        // Required empty public constructor
    }
    // end region Constructor

    //Region Default Methods
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        this.mFragmentInitialize();
    }

    //End Region Default Methods

   //Region iICSDefaultFragment Methods
    @Override
    public void mFragmentInitialize() {
        this.mFindViews();
        this.mFieldsInitialize();
        this.mSetListeners();
    }

    @Override
    public void mFindViews() {
        this.imageLogo = getView().findViewById(R.id.imageLogo);
        this.cardViewDeviceDetails = getView().findViewById(R.id.cardViewDeviceDetails);
        this.textViewDevicedetails = getView().findViewById(R.id.textViewDeviceDetails);
        this.buttonLogin = getView().findViewById(R.id.buttonLogin);
    }

    @Override
    public void mFieldsInitialize() {
        this.mSetDeviceInfo();
        this.buttonLogin.setVisibility(View.VISIBLE);
        this.mBounceLogo();
    }

    @Override
    public void mSetListeners() {
        this.mSetLogoListener();
        this.mSetLoginButtonListener();
    }

    //End Region iICSDefaultFragment Methods

    //Region Public Methods

    public static void pFragmentDone() {
        HomeFragment.mStartLoginActivity();
    }

    //End Region Public Methods

    //Region Private Methods

    private void mBounceLogo() {
        cUserInterface.pDoBoing(imageLogo);
    }

    private void mSetDeviceInfo() {
        String DeviceInfoStr = "";
        DeviceInfoStr += getString(R.string.device_manufacturer) + cText.LABEL_VALUE_SEPARATOR + cDeviceInfo.getDeviceManufacturer() + cText.NEWLINE;
        DeviceInfoStr += getString(R.string.device_brand)  + cText.LABEL_VALUE_SEPARATOR + cDeviceInfo.getDeviceBrand() + cText.NEWLINE;
        DeviceInfoStr += getString(R.string.device_model)  + cText.LABEL_VALUE_SEPARATOR + cDeviceInfo.getDeviceModel() + cText.NEWLINE;
        DeviceInfoStr += getString(R.string.device_serialnumber)  + cText.LABEL_VALUE_SEPARATOR + cDeviceInfo.getSerialnumberStr() + cText.NEWLINE;
        DeviceInfoStr += getString(R.string.application_version)  + cText.LABEL_VALUE_SEPARATOR + cDeviceInfo.getAppVersion();
        this.textViewDevicedetails.setText(DeviceInfoStr);
    }

    private static void mStartLoginActivity() {
        Intent intent = new Intent(cAppExtension.context, LoginActivity.class);
        cAppExtension.context.startActivity(intent);
    }

    //Region Listeners
    private void mSetLogoListener() {
        this.imageLogo.setOnClickListener(new View.OnClickListener() {
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
        this.buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (cAppExtension.context instanceof MainDefaultActivity) {

                    if (cConnection.isInternetConnectedBln() == false) {
                        final NoConnectionFragment noConnectionFragment = new NoConnectionFragment();
                        noConnectionFragment.setCancelable(true);
                        noConnectionFragment.show(((MainDefaultActivity) cAppExtension.context).getSupportFragmentManager(), "NOCONNECTION");
                        return;
                    }

                    cUserInterface.pShowGettingData();

                    new Thread(new Runnable() {
                        public void run() {
                            ((MainDefaultActivity)cAppExtension.context).pLetsGetThisPartyStartedOrNot();
                        }
                    }).start();
                }
            }
        });
    }

    //endregion Listeners



}

