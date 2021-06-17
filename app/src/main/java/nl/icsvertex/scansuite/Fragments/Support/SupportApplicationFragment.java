package nl.icsvertex.scansuite.Fragments.Support;


import android.os.Bundle;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.DialogFragment;

import ICS.Interfaces.iICSDefaultFragment;
import ICS.Utils.cDeviceInfo;
import ICS.Utils.cSharedPreferences;
import ICS.Utils.cText;
import ICS.Utils.cUpdate;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Webservice.cWebservice;
import nl.icsvertex.scansuite.Activities.General.MainDefaultActivity;
import nl.icsvertex.scansuite.R;


public class SupportApplicationFragment extends DialogFragment implements iICSDefaultFragment {


    //Region Public Properties

    //End Region Public Properties

    //Region Private Properties

    private  TextView textViewApplicationVersion;
    private  TextView textViewWebservice;
    private  TextView textViewApplicationInstalled;
    private  TextView textViewApplicationLastUpdate;
    private  ImageButton updateImageButton;
    private  ImageButton testWebserviceImageButton;
    private SwitchCompat checkDarkModus;

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
    @Override
    public void onResume() {
        super.onResume();
        this.mFragmentInitialize();
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
            this.textViewApplicationVersion = getView().findViewById(R.id.textViewApplicationVersion);
            this.textViewWebservice = getView().findViewById(R.id.textViewWebservice);
            this.updateImageButton = getView().findViewById(R.id.updateImageButton);
            this.testWebserviceImageButton = getView().findViewById(R.id.testWebserviceImageButton);
            this.textViewApplicationInstalled = getView().findViewById(R.id.textViewApplicationInstalled);
            this.textViewApplicationLastUpdate = getView().findViewById(R.id.textViewApplicationLastUpdate);
            this.checkDarkModus = getView().findViewById(R.id.checkDarkModus);
        }
    }

    @Override
    public void mFieldsInitialize() {
        this.textViewApplicationVersion.setText(cDeviceInfo.getAppVersion());
        this.textViewWebservice.setText(cWebservice.WEBSERVICE_URL);
        this.updateImageButton.setVisibility(View.INVISIBLE);
        this.textViewApplicationInstalled.setText(cDeviceInfo.getInstalldate());
        this.textViewApplicationLastUpdate.setText(cDeviceInfo.getLastUpdateDate());
        this.checkDarkModus.setChecked(cSharedPreferences.getDarkModusBln());
    }

    @Override
    public void mSetListeners() {
        this.mSetUpdateListener();
        this.mTestWebserviceListener();
        this.mWebserviceURLLongClickListener();
        this.mSetDarkModusListener();
    }

    //End Region iICSDefaultFragment defaults

    //Region Private Methods

    private void mSetUpdateListener() {
        this.updateImageButton.setOnClickListener(view -> cUpdate.mUpdateBln(updateImageButton ,""));
    }

    private void mSetDarkModusListener() {
        this.checkDarkModus.setOnCheckedChangeListener((compoundButton, show) -> {

            cSharedPreferences.setDarkModusBln(checkDarkModus.isChecked());

            if (cAppExtension.activity instanceof  MainDefaultActivity) {
                MainDefaultActivity mainDefaultActivity = (MainDefaultActivity)cAppExtension.activity;
                mainDefaultActivity.pChangeDarkModus();
            }
        });
    }

    private void mTestWebserviceListener() {
        this.testWebserviceImageButton.setOnClickListener(view -> {
            if (cWebservice.pGetWebserviceAvailableBln()) {
                cUserInterface.pShowToastMessage(getString(R.string.message_webservice_live) , null);
                cUserInterface.pDoYep(textViewWebservice, true, false);
            }
            else {
                cUserInterface.pShowToastMessage( getString(R.string.message_webservice_not_live) , null);
                cUserInterface.pDoNope(textViewWebservice, true,false);
            }
        });
    }
    private void mWebserviceURLLongClickListener() {
        textViewWebservice.setOnLongClickListener(view -> {
            if (cText.pCopyTextToClipboard(textViewWebservice, "webservice url")) {
                cUserInterface.pShowToastMessage(getString(R.string.text_copied_to_clipboard), R.raw.headsupsound);
                textViewWebservice.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
            }
            else {
                cUserInterface.pShowToastMessage(getString(R.string.text_not_copied_to_clipboard), R.raw.headsupsound);
            }
            return false;
        });
    }


    //End Region private Methods


}
