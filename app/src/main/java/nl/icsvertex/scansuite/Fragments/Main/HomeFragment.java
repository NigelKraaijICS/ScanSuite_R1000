package nl.icsvertex.scansuite.Fragments.Main;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import ICS.Interfaces.iICSDefaultFragment;
import ICS.Utils.cConnection;
import ICS.Utils.cDeviceInfo;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import nl.icsvertex.scansuite.Activities.General.LoginActivity;
import nl.icsvertex.scansuite.Activities.General.MainDefaultActivity;
import nl.icsvertex.scansuite.Fragments.Dialogs.NoConnectionFragment;
import nl.icsvertex.scansuite.R;

public class HomeFragment extends Fragment implements iICSDefaultFragment {

    //region Private Properties
    private static  ImageView imageLogo;
    private static TextView textViewDevicedetails;
    private static CardView cardViewDeviceDetails;
    private static Button buttonLogin;
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
        cUserInterface.pEnableScanner();
    }

    @Override
    public void mFindViews() {

        if (getView() != null) {
            HomeFragment.imageLogo = getView().findViewById(R.id.imageLogo);
            HomeFragment.cardViewDeviceDetails = getView().findViewById(R.id.cardViewDeviceDetails);
            HomeFragment.textViewDevicedetails = getView().findViewById(R.id.textViewDeviceDetails);
            HomeFragment.buttonLogin = getView().findViewById(R.id.buttonLogin);
        }


    }

    @Override
    public void mFieldsInitialize() {
        this.mSetDeviceInfo();
        HomeFragment.buttonLogin.setVisibility(View.VISIBLE);
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
        HomeFragment.textViewDevicedetails.setText(cDeviceInfo.getDeviceInfoStr());
    }

    private static void mStartLoginActivity() {

        cUserInterface.pCheckAndCloseOpenDialogs();

        Intent intent = new Intent(cAppExtension.context, LoginActivity.class);
        cAppExtension.context.startActivity(intent);
    }

    //Region Listeners
    private void mSetLogoListener() {
        HomeFragment.imageLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               mBounceLogo();
                if (HomeFragment.cardViewDeviceDetails.getVisibility() == View.VISIBLE) {
                    HomeFragment.cardViewDeviceDetails.setVisibility(View.INVISIBLE);
                }
                else {
                    HomeFragment.cardViewDeviceDetails.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void mSetLoginButtonListener() {
        HomeFragment.buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (cAppExtension.context instanceof MainDefaultActivity) {

                    if (!cConnection.isInternetConnectedBln()) {
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

