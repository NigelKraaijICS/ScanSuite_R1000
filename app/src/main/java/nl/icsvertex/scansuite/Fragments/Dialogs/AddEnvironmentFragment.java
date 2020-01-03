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
    private static Button buttonCancel;
    private static Button buttonSave;
    private static EditText editTextEnvironmentName;
    private static EditText editTextEnvironmentDescription;
    private static EditText editTextEnvironmentUrl;
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
        cBarcodeScan.pUnregisterBarcodeFragmentReceiver();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        cBarcodeScan.pRegisterBarcodeFragmentReceiver();
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
            AddEnvironmentFragment.buttonCancel = getView().findViewById(R.id.buttonCancel);
            AddEnvironmentFragment.buttonSave = getView().findViewById(R.id.buttonSave);
            AddEnvironmentFragment.editTextEnvironmentName = getView().findViewById(R.id.editTextEnvironmentName);
            AddEnvironmentFragment.editTextEnvironmentDescription = getView().findViewById(R.id.editTextEnvironmentDescription);
            AddEnvironmentFragment.editTextEnvironmentUrl = getView().findViewById(R.id.editTextEnvironmentUrl);
        }
    }



    @Override
    public void mFieldsInitialize() {

    }

    @Override
    public void mSetListeners() {
        mSetCancelListener();
        mSetSaveListener();
        mSetEditorActionListener();
    }

    //End Region iICSDefaultFragment defaults

    //Region Public Methods

    public static void pHandleScan(cBarcodeScan pvBarcodeScan) {

        String[] scanSplit = pvBarcodeScan.getBarcodeOriginalStr().split("=");
        if (scanSplit.length != 2) {
            cUserInterface.pDoNope(buttonSave, true, false);

            return;
        }
        switch (scanSplit[0].toUpperCase()) {
            case "NAME":
                editTextEnvironmentName.setText(scanSplit[1]);
                break;
            case "DESCRIPTION":
                editTextEnvironmentDescription.setText(scanSplit[1]);
                break;
            case "URL":
                editTextEnvironmentUrl.setText(scanSplit[1]);
                break;
            default:
                cUserInterface.pDoNope(buttonSave, true, false);
        }
    }

    //End Region Public Methods

    //Region Private Methods

    private void mSetCancelListener() {
        AddEnvironmentFragment.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cAppExtension.dialogFragment.dismiss();
            }
        });
    }

    private void mSetSaveListener() {
        AddEnvironmentFragment.buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = AddEnvironmentFragment.editTextEnvironmentName.getText().toString().trim();
                if (name.isEmpty()) {
                    cUserInterface.pDoNope(AddEnvironmentFragment.editTextEnvironmentName, true, false);
                    return;
                }
                String description = AddEnvironmentFragment.editTextEnvironmentDescription.getText().toString().trim();
                if (description.isEmpty()) {
                    cUserInterface.pDoNope(AddEnvironmentFragment.editTextEnvironmentDescription, true, false);
                    return;
                }
                String url = AddEnvironmentFragment.editTextEnvironmentUrl.getText().toString().trim();
                if (url.isEmpty()) {
                    cUserInterface.pDoNope(AddEnvironmentFragment.editTextEnvironmentUrl, true, false);
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
                    MainDefaultActivity.pSetAddedEnvironment();
                }
            }
        });
    }
    private void mSetEditorActionListener() {
        AddEnvironmentFragment.editTextEnvironmentUrl.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE || i == EditorInfo.IME_ACTION_GO ) {
                    AddEnvironmentFragment.buttonSave.callOnClick();
                }
                return true;
            }
        });
    }
    //End Region Private Methods






}
