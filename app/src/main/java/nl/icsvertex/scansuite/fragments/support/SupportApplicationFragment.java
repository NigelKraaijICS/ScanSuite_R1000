package nl.icsvertex.scansuite.fragments.support;


import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import ICS.Interfaces.iICSDefaultFragment;
import SSU_WHS.Basics.Settings.cSettingsViewModel;
import SSU_WHS.Webservice.cWebservice;
import ICS.Utils.cDeviceInfo;
import ICS.Utils.cUpdate;
import ICS.Utils.cUserInterface;
import nl.icsvertex.scansuite.fragments.dialogs.BasicsFragment;
import nl.icsvertex.scansuite.R;


public class SupportApplicationFragment extends Fragment implements iICSDefaultFragment {

    private static final String SETTINGSFRAGMENT_TAG = "SETTINGSFRAGMENT_TAG";

    private TextView textViewApplicationVersion;
    private  TextView textViewWebservice;
    private  String updateUrl;
    private ImageButton updateImageButton;
    private  ImageButton testWebserviceImageButton;
    private  Button buttonMySettings;

    public SupportApplicationFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_support_application, container, false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
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
        textViewApplicationVersion = getView().findViewById(R.id.textViewApplicationVersion);
        textViewWebservice = getView().findViewById(R.id.textViewWebservice);
        updateImageButton = getView().findViewById(R.id.updateImageButton);
        testWebserviceImageButton = getView().findViewById(R.id.testWebserviceImageButton);
        buttonMySettings = getView().findViewById(R.id.buttonMySettings);
    }

    @Override
    public void mSetViewModels() {

    }

    @Override
    public void mFieldsInitialize() {
        textViewApplicationVersion.setText(cDeviceInfo.getAppVersion());
        textViewWebservice.setText(cWebservice.WEBSERVICE_URL);
        updateImageButton.setVisibility(View.INVISIBLE);

    }

    @Override
    public void mSetListeners() {
        mSetUpdateListener();
        mTestWebserviceListener();
    }

    private void mSetUpdateListener() {
        updateImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cUpdate.mUpdateBln(updateImageButton ,updateUrl);
            }
        });

        buttonMySettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_showSettings();
            }
        });
    }
    private void mTestWebserviceListener() {
        testWebserviceImageButton.setOnClickListener(new View.OnClickListener() {
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

    private void m_showSettings() {
        final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        final BasicsFragment basicsFragment = new BasicsFragment();
        basicsFragment.show(fragmentManager, SETTINGSFRAGMENT_TAG);
    }

}
