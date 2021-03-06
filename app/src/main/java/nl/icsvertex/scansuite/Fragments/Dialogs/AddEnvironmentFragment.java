package nl.icsvertex.scansuite.Fragments.Dialogs;


import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Objects;

import ICS.Environments.cEnvironment;
import ICS.Environments.cEnvironmentEntity;
import ICS.Interfaces.iICSDefaultFragment;
import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import nl.icsvertex.scansuite.Activities.General.MainDefaultActivity;
import nl.icsvertex.scansuite.R;

public class AddEnvironmentFragment extends DialogFragment implements iICSDefaultFragment {


    //Region Public Properties

    //End Region Public Properties

    //Region Private Properties
    private Button buttonCancel;
    private Button buttonSave;
    private EditText editTextEnvironmentName;
    private EditText editTextEnvironmentDescription;
    private EditText editTextEnvironmentUrl;
        //End Region Private Properties


    //Region Constructor
    public AddEnvironmentFragment() {

    }
    //End Region Constructor

//Region Default Methods

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_add_environment, container);
        cAppExtension.dialogFragment = this;
        return rootview;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        this.mFragmentInitialize();
    }

    @Override
    public void onPause() {
        super.onPause();
        cBarcodeScan.pUnregisterBarcodeFragmentReceiver(this.getClass().getSimpleName());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        cBarcodeScan.pRegisterBarcodeFragmentReceiver(this.getClass().getSimpleName());
        cUserInterface.pEnableScanner();

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels - getResources().getDimensionPixelSize(R.dimen.default_double_margin);

        Objects.requireNonNull(Objects.requireNonNull(getDialog()).getWindow()).setLayout(width, height);
    }

    //End Region Default Methods

    //Region iICSDefaultFragment defaults

    @Override
    public void mFragmentInitialize() {
        this.mFindViews();
        this.mSetListeners();
    }

    @Override
    public void mFindViews() {

        if (getView() != null) {
            this.buttonCancel = getView().findViewById(R.id.buttonCancel);
            this.buttonSave = getView().findViewById(R.id.buttonSave);
            this.editTextEnvironmentName = getView().findViewById(R.id.editTextEnvironmentName);
            this.editTextEnvironmentDescription = getView().findViewById(R.id.editTextEnvironmentDescription);
            this.editTextEnvironmentUrl = getView().findViewById(R.id.editTextEnvironmentUrl);
        }
    }



    @Override
    public void mFieldsInitialize() {

    }

    @Override
    public void mSetListeners() {
        this.mSetCancelListener();
        this.mSetSaveListener();
        this.mSetEditorActionListener();
    }

    //End Region iICSDefaultFragment defaults

    //Region Public Methods

    public void pHandleScan(cBarcodeScan pvBarcodeScan) {

        String[] scanSplit = pvBarcodeScan.getBarcodeOriginalStr().split("=");
        if (scanSplit.length != 2) {
            cUserInterface.pDoNope(this.buttonSave, true, false);

            return;
        }
        switch (scanSplit[0].toUpperCase()) {
            case "NAME":
                this.editTextEnvironmentName.setText(scanSplit[1]);
                break;
            case "DESCRIPTION":
                this.editTextEnvironmentDescription.setText(scanSplit[1]);
                break;
            case "URL":
                this.editTextEnvironmentUrl.setText(scanSplit[1]);
                break;
            default:
                cUserInterface.pDoNope(buttonSave, true, false);
        }
    }

    //End Region Public Methods

    //Region Private Methods

    private void mSetCancelListener() {
        this.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    private void mSetSaveListener() {
        this.buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = editTextEnvironmentName.getText().toString().trim();
                if (name.isEmpty()) {
                    cUserInterface.pDoNope(editTextEnvironmentName, true, false);
                    return;
                }
                String description = editTextEnvironmentDescription.getText().toString().trim();
                if (description.isEmpty()) {
                    cUserInterface.pDoNope(editTextEnvironmentDescription, true, false);
                    return;
                }
                String url = editTextEnvironmentUrl.getText().toString().trim();
                if (url.isEmpty()) {
                    cUserInterface.pDoNope(editTextEnvironmentUrl, true, false);
                    return;
                }
                cEnvironmentEntity environmentEntity = new cEnvironmentEntity();
                environmentEntity.name = name;
                environmentEntity.description = description;
                environmentEntity.webserviceurl =url;
                environmentEntity.isdefault = false;

                cEnvironment environment = new cEnvironment(environmentEntity);
                environment.pInsertInDatabaseBln();

                cUserInterface.pShowToastMessage( getString(R.string.environment_parameter1_saved, description), null);
                if (cAppExtension.context instanceof MainDefaultActivity) {
                    MainDefaultActivity mainDefaultActivity =  (MainDefaultActivity)cAppExtension.activity;
                    mainDefaultActivity.pSetAddedEnvironment();
                }
            }
        });
    }
    private void mSetEditorActionListener() {
        this.editTextEnvironmentUrl.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE || i == EditorInfo.IME_ACTION_GO ) {
                    buttonSave.callOnClick();
                }
                return true;
            }
        });
    }
    //End Region Private Methods






}
