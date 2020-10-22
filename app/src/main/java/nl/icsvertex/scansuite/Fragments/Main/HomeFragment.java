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
import androidx.fragment.app.DialogFragment;

import java.util.concurrent.ExecutionException;

import ICS.Interfaces.iICSDefaultFragment;
import ICS.Utils.cConnection;
import ICS.Utils.cDeviceInfo;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import nl.icsvertex.scansuite.Activities.General.LoginActivity;
import nl.icsvertex.scansuite.Activities.General.MainDefaultActivity;
import nl.icsvertex.scansuite.Fragments.Dialogs.NoConnectionFragment;
import nl.icsvertex.scansuite.R;

public class HomeFragment extends DialogFragment implements iICSDefaultFragment {

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
        cAppExtension.dialogFragment  = this;
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
            this.imageLogo = getView().findViewById(R.id.imageLogo);
            this.cardViewDeviceDetails = getView().findViewById(R.id.cardViewDeviceDetails);
            this.textViewDevicedetails = getView().findViewById(R.id.textViewDeviceDetails);
            this.buttonLogin = getView().findViewById(R.id.buttonLogin);
        }


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

    //End Region Public Methods

    //Region Private Methods

    private void mBounceLogo() {
        cUserInterface.pDoBoing(imageLogo);
    }

    private void mSetDeviceInfo() {
        this.textViewDevicedetails.setText(cDeviceInfo.getDeviceInfoStr());
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


                if (cDeviceInfo.getSerialnumberStr().isEmpty()) {
                    cUserInterface.pDoExplodingScreen(cAppExtension.activity.getString(R.string.message_set_serial_first),"", true,true);
                    return;
                }

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
                            try {
                                ((MainDefaultActivity)cAppExtension.context).pLetsGetThisPartyStartedOrNot();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
            }
        });
    }

    //endregion Listeners



}

