package nl.icsvertex.scansuite.fragments.dialogs;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import ICS.Environments.cEnvironment;
import ICS.Environments.cEnvironmentEntity;
import ICS.Interfaces.iICSDefaultFragment;
import ICS.Utils.Scanning.cBarcodeScanDefinitions;
import ICS.Utils.cUserInterface;
import nl.icsvertex.scansuite.cAppExtension;
import nl.icsvertex.scansuite.R;
import nl.icsvertex.scansuite.activities.general.MainDefaultActivity;

public class AddEnvironmentFragment extends DialogFragment implements iICSDefaultFragment {

    private  Button buttonCancel;
    private Button buttonSave;
    private EditText editTextEnvironmentName;
    private EditText editTextEnvironmentDescription;
    private EditText editTextEnvironmentUrl;
    private DialogFragment thisFragment;
    private  IntentFilter barcodeFragmentIntentFilter;

    private BroadcastReceiver barcodeFragmentReceiver;

    public AddEnvironmentFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_add_environment, container);
        thisFragment = this;
        return rootview;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mFragmentInitialize();
    }

    @Override
    public void onPause() {
        try {
            getActivity().unregisterReceiver(barcodeFragmentReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        try {
            getActivity().unregisterReceiver(barcodeFragmentReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    @Override
    public void onResume() {
        getActivity().registerReceiver(barcodeFragmentReceiver, barcodeFragmentIntentFilter);
        super.onResume();
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels - getResources().getDimensionPixelSize(R.dimen.default_double_margin);

        getDialog().getWindow().setLayout(width, height);
    }

    @Override
    public void mFragmentInitialize() {
        mFindViews();
        mSetViewModels();
        mBarcodeReceiver();
        mSetListeners();
    }

    @Override
    public void mFindViews() {
        buttonCancel = getView().findViewById(R.id.buttonCancel);
        buttonSave = getView().findViewById(R.id.buttonSave);
        editTextEnvironmentName = getView().findViewById(R.id.editTextEnvironmentName);
        editTextEnvironmentDescription = getView().findViewById(R.id.editTextEnvironmentDescription);
        editTextEnvironmentUrl = getView().findViewById(R.id.editTextEnvironmentUrl);
    }

    @Override
    public void mSetViewModels() {

    }

    @Override
    public void mFieldsInitialize() {

    }

    @Override
    public void mSetListeners() {
        mSetCancelListener();
        mSetSaveListener();
    }

    private void mBarcodeReceiver() {
        barcodeFragmentIntentFilter = new IntentFilter();
        for (String str : cBarcodeScanDefinitions.getBarcodeActions()) {
            barcodeFragmentIntentFilter.addAction(str);
        }
        for (String str : cBarcodeScanDefinitions.getBarcodeCategories()) {
            barcodeFragmentIntentFilter.addCategory(str);
        }

        barcodeFragmentReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String barcodeStr = ICS.Utils.Scanning.cBarcodeScan.pGetBarcode(intent, false);
                if (barcodeStr == null) {
                    barcodeStr = "";
                }
                mHandleScan(barcodeStr);
            }
        };
        //don't forget to unregister on destroy.
        getActivity().registerReceiver(barcodeFragmentReceiver,barcodeFragmentIntentFilter);
    }

    private void mSetCancelListener() {
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                thisFragment.dismiss();
            }
        });
    }
    private void mSetSaveListener() {
        buttonSave.setOnClickListener(new View.OnClickListener() {
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
                    ((MainDefaultActivity)cAppExtension.context).pSetAddedEnvironment();
                }
            }
        });
    }

    private void mHandleScan(String barcode) {
        String[] scanSplit = barcode.split("=");
        if (scanSplit.length != 2) {
            mDoWrongBarcode();
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
                mDoWrongBarcode();
        }
    }
    private void mDoWrongBarcode() {
        cUserInterface.pDoNope(buttonSave, true, false);
    }

}
