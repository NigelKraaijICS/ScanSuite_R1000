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
import ICS.Utils.cSharedPreferences;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.General.cPublicDefinitions;
import nl.icsvertex.scansuite.Activities.General.LoginActivity;
import nl.icsvertex.scansuite.Activities.General.MainDefaultActivity;
import nl.icsvertex.scansuite.Fragments.Dialogs.NoConnectionFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.SetSerialFragment;
import nl.icsvertex.scansuite.R;

import static SSU_WHS.General.cPublicDefinitions.SHAREDPREFERENCE_USEPROGLOVE;

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

    private static void onClick(View view) {


        if (cDeviceInfo.getSerialnumberStr().isEmpty()) {
            cUserInterface.pDoExplodingScreen(cAppExtension.activity.getString(R.string.message_set_serial_first), "", true, true);
            mShowSetSerialFragment();
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

            new Thread(() -> {
                try {
                    ((MainDefaultActivity) cAppExtension.context).pLetsGetThisPartyStartedOrNot();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }).start();
        }
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
        this.metSetProGlove();
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

    private void metSetProGlove() {
        Boolean useProGlove = cSharedPreferences.pGetSharedPreferenceBoolean(SHAREDPREFERENCE_USEPROGLOVE, false);
        if (!useProGlove) {
            View proGloveView =  getActivity().findViewById(R.id.action_proglove);
            if (proGloveView != null) {
                proGloveView.setVisibility(View.GONE);
            }
        }

    }

    private void mBounceLogo() {
        cUserInterface.pDoBoing(imageLogo);
    }

    private void mSetDeviceInfo() {
        this.textViewDevicedetails.setText(cDeviceInfo.getDeviceInfoStr());
    }

    //Region Listeners
    private void mSetLogoListener() {
        this.imageLogo.setOnClickListener(view -> {
           mBounceLogo();
            if (cardViewDeviceDetails.getVisibility() == View.VISIBLE) {
                cardViewDeviceDetails.setVisibility(View.INVISIBLE);
            }
            else {
                cardViewDeviceDetails.setVisibility(View.VISIBLE);
            }
        });
    }

    private void mSetLoginButtonListener() {
        this.buttonLogin.setOnClickListener(HomeFragment::onClick);
    }

    //endregion Listeners

    private static void mShowSetSerialFragment(){

        cUserInterface.pCheckAndCloseOpenDialogs();

        SetSerialFragment serialFragment = new SetSerialFragment();
        serialFragment.setCancelable(true);
        serialFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.SETSERIAL_TAG);

    }

}

