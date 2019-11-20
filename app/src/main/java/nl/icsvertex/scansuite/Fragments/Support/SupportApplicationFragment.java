package nl.icsvertex.scansuite.Fragments.Support;


import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import ICS.Interfaces.iICSDefaultFragment;
import ICS.cAppExtension;
import SSU_WHS.Webservice.cWebservice;
import ICS.Utils.cDeviceInfo;
import ICS.Utils.cUpdate;
import ICS.Utils.cUserInterface;
import nl.icsvertex.scansuite.Fragments.Dialogs.BasicsFragment;
import nl.icsvertex.scansuite.R;


public class SupportApplicationFragment extends DialogFragment implements iICSDefaultFragment {


    //Region Public Properties

    //End Region Public Properties

    //Region Private Properties

    private static final String SETTINGSFRAGMENT_TAG = "SETTINGSFRAGMENT_TAG";

    private TextView textViewApplicationVersion;
    private TextView textViewWebservice;
    private TextView textViewApplicationInstalled;
    private String updateUrl;
    private ImageButton updateImageButton;
    private ImageButton testWebserviceImageButton;
    private Button buttonMySettings;


    //End Region Private Properties

    //Region Constructor

    public SupportApplicationFragment() {
        // Required empty public constructor
    }

    //End Region Constructor


    //Region Default Methods

    @Override
    public View onCreateView(LayoutInflater pvLayoutInflater,
                             ViewGroup pvViewGroup,
                             Bundle pvBundle) {
        // Inflate the layout for this fragment
        return pvLayoutInflater.inflate(R.layout.fragment_support_application, pvViewGroup, false);
    }

    @Override

    public void onViewCreated(View pvView, @Nullable Bundle pvBundle) {
        cAppExtension.dialogFragment = this;
        this.mFragmentInitialize();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                cAppExtension.dialogFragment.dismiss();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //End Region Default Methods

    //Region iICSDefaultFragment defaults

    @Override
    public void mFragmentInitialize() {
        this.mFindViews();
        this.mFieldsInitialize();
        this.mSetListeners();
    }

    @Override
    public void mFindViews() {

        this.textViewApplicationVersion = getView().findViewById(R.id.textViewApplicationVersion);
        this.textViewWebservice = getView().findViewById(R.id.textViewWebservice);
        this.updateImageButton = getView().findViewById(R.id.updateImageButton);
        this.testWebserviceImageButton = getView().findViewById(R.id.testWebserviceImageButton);
        this.buttonMySettings = getView().findViewById(R.id.buttonMySettings);
        this.textViewApplicationInstalled = getView().findViewById(R.id.textViewApplicationInstalled);

    }

    @Override
    public void mFieldsInitialize() {
        this.textViewApplicationVersion.setText(cDeviceInfo.getAppVersion());
        this.textViewWebservice.setText(cWebservice.WEBSERVICE_URL);
        this.updateImageButton.setVisibility(View.INVISIBLE);
        this.textViewApplicationInstalled.setText(cDeviceInfo.getInstalldate());
    }

    @Override
    public void mSetListeners() {
        this.mSetUpdateListener();
        this.mTestWebserviceListener();
    }

    //End Region iICSDefaultFragment defaults

    //Region Private Methods

    private void mSetUpdateListener() {
        this.updateImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cUpdate.mUpdateBln(updateImageButton ,updateUrl);
            }
        });

        this. buttonMySettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mShowSettings();
            }
        });
    }

    private void mTestWebserviceListener() {
        this.testWebserviceImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cWebservice.pGetWebserviceAvailableBln()) {
                    cUserInterface.pShowToastMessage(getString(R.string.message_webservice_live) , null);
                    cUserInterface.pDoYep(textViewWebservice, true, false);
                }
                else {
                    cUserInterface.pShowToastMessage( getString(R.string.message_webservice_not_live) , null);
                    cUserInterface.pDoNope(textViewWebservice, true,false);
                }
            }
        });
    }

    private void mShowSettings() {
        BasicsFragment basicsFragment = new BasicsFragment();
        basicsFragment.show(getActivity().getSupportFragmentManager(), SETTINGSFRAGMENT_TAG);
    }

    //End Region private Methods


}
