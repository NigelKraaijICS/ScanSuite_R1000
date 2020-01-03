package nl.icsvertex.scansuite.Fragments.Support;


import android.os.Bundle;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Objects;

import ICS.Interfaces.iICSDefaultFragment;
import ICS.Utils.cDeviceInfo;
import ICS.Utils.cText;
import ICS.Utils.cUpdate;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Webservice.cWebservice;
import nl.icsvertex.scansuite.Fragments.Dialogs.BasicsFragment;
import nl.icsvertex.scansuite.R;


public class SupportApplicationFragment extends DialogFragment implements iICSDefaultFragment {


    //Region Public Properties

    //End Region Public Properties

    //Region Private Properties

    private static final String SETTINGSFRAGMENT_TAG = "SETTINGSFRAGMENT_TAG";

    private static TextView textViewApplicationVersion;
    private static TextView textViewWebservice;
    private static TextView textViewApplicationInstalled;
    private static TextView textViewApplicationLastUpdate;
    private static ImageButton updateImageButton;
    private static ImageButton testWebserviceImageButton;
    private static Button buttonMySettings;


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

    public void onViewCreated(@NonNull View pvView, @Nullable Bundle pvBundle) {
        cAppExtension.dialogFragment = this;
        this.mFragmentInitialize();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() ==android.R.id.home ) {
            cAppExtension.dialogFragment.dismiss();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //End Region Default Methods

    //Region iICSDefaultFragment defaults

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
            SupportApplicationFragment.textViewApplicationVersion = getView().findViewById(R.id.textViewApplicationVersion);
            SupportApplicationFragment.textViewWebservice = getView().findViewById(R.id.textViewWebservice);
            SupportApplicationFragment.updateImageButton = getView().findViewById(R.id.updateImageButton);
            SupportApplicationFragment.testWebserviceImageButton = getView().findViewById(R.id.testWebserviceImageButton);
            SupportApplicationFragment.buttonMySettings = getView().findViewById(R.id.buttonMySettings);
            SupportApplicationFragment.textViewApplicationInstalled = getView().findViewById(R.id.textViewApplicationInstalled);
            SupportApplicationFragment.textViewApplicationLastUpdate = getView().findViewById(R.id.textViewApplicationLastUpdate);
        }
    }

    @Override
    public void mFieldsInitialize() {
        SupportApplicationFragment.textViewApplicationVersion.setText(cDeviceInfo.getAppVersion());
        SupportApplicationFragment.textViewWebservice.setText(cWebservice.WEBSERVICE_URL);
        SupportApplicationFragment.updateImageButton.setVisibility(View.INVISIBLE);
        SupportApplicationFragment.textViewApplicationInstalled.setText(cDeviceInfo.getInstalldate());
        SupportApplicationFragment.textViewApplicationLastUpdate.setText(cDeviceInfo.getLastUpdateDate());
    }

    @Override
    public void mSetListeners() {
        this.mSetUpdateListener();
        this.mTestWebserviceListener();
        this.mWebserviceURLLongClickListener();
    }

    //End Region iICSDefaultFragment defaults

    //Region Private Methods

    private void mSetUpdateListener() {
        SupportApplicationFragment.updateImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cUpdate.mUpdateBln(updateImageButton ,"");
            }
        });

        SupportApplicationFragment.buttonMySettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mShowSettings();
            }
        });
    }

    private void mTestWebserviceListener() {
        SupportApplicationFragment.testWebserviceImageButton.setOnClickListener(new View.OnClickListener() {
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
    private void mWebserviceURLLongClickListener() {
        textViewWebservice.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (cText.pCopyTextToClipboard(textViewWebservice, "webservice url")) {
                    cUserInterface.pShowToastMessage(getString(R.string.text_copied_to_clipboard), R.raw.headsupsound);
                    textViewWebservice.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
                }
                else {
                    cUserInterface.pShowToastMessage(getString(R.string.text_not_copied_to_clipboard), R.raw.headsupsound);
                }
                return false;
            }
        });
    }

    private void mShowSettings() {
        BasicsFragment basicsFragment = new BasicsFragment();
        basicsFragment.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), SETTINGSFRAGMENT_TAG);
    }

    //End Region private Methods


}
