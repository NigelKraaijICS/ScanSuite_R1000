package nl.icsvertex.scansuite.Fragments.Dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import ICS.Interfaces.iICSDefaultFragment;
import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Picken.PickorderLinePropertyValue.cPickorderLinePropertyValue;
import nl.icsvertex.scansuite.Activities.Pick.PickorderLineItemPropertyInputActvity;
import nl.icsvertex.scansuite.R;


public class ItemPropertyTextInputFragment extends DialogFragment implements iICSDefaultFragment {

    private  TextView textHeader;
    private  EditText inputText;
    private  Button buttonCancel;
    private  Button buttonOk;


    public ItemPropertyTextInputFragment() {
        // Empty constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_itemproperty_text_input, container, false);

    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        cAppExtension.dialogFragment = this;
        this.mFragmentInitialize();
    }

    @Override
    public void onResume() {
        super.onResume();
        cUserInterface.pShowKeyboard(this.inputText);
        cBarcodeScan.pRegisterBarcodeFragmentReceiver(this.getClass().getSimpleName());
        cUserInterface.pEnableScanner();
    }

    @Override
    public void onDestroy() {
        try {
            cBarcodeScan.pUnregisterBarcodeFragmentReceiver(this.getClass().getSimpleName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    @Override
    public void onPause() {
        try {
            cBarcodeScan.pUnregisterBarcodeFragmentReceiver(this.getClass().getSimpleName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onPause();
    }


    @Override
    public void mFragmentInitialize() {
        this.mFindViews();
        this.mFieldsInitialize();
        this.mSetListeners();
    }

    @Override
    public void mFindViews() {
        if (getView() != null) {
            this.textHeader = getView().findViewById(R.id.textHeader);
            this.inputText = getView().findViewById(R.id.inputText);
            this.buttonCancel = getView().findViewById(R.id.buttonCancel);
            this.buttonOk = getView().findViewById(R.id.buttonOk);
        }
    }

    @Override
    public void mFieldsInitialize() {
        this.inputText.setSelectAllOnFocus(true);
        this.inputText.requestFocus();
        this.inputText.setHint(cAppExtension.activity.getString(R.string.message_please_enter, cPickorderLinePropertyValue.currentPickorderLinePropertyValue.getItemProperty().getOmschrijvingStr().toLowerCase()));
        this.textHeader.setText(cPickorderLinePropertyValue.currentPickorderLinePropertyValue.getItemProperty().getOmschrijvingStr());
    }

    @Override
    public void mSetListeners() {
        this.mSetCancelListener();
        this.mSetOKListener();
    }


    private void mSetOKListener() {

        this.buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mHandleOk();
            }
        });
    }

    private void mSetCancelListener() {
        this.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              dismiss();
                }
        });
    }


    private void mHandleOk() {

        if (cAppExtension.activity instanceof PickorderLineItemPropertyInputActvity) {
            PickorderLineItemPropertyInputActvity pickorderLineItemPropertyInputActvity = (PickorderLineItemPropertyInputActvity)cAppExtension.activity;
            pickorderLineItemPropertyInputActvity.pHandleScan(cBarcodeScan.pFakeScan(this.inputText.getText().toString()));
            dismiss();
        }

    }

}
