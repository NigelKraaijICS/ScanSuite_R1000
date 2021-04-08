package nl.icsvertex.scansuite.Fragments.Dialogs;

import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
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
import ICS.Utils.cRegex;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Basics.BarcodeLayouts.cBarcodeLayout;
import SSU_WHS.Picken.PickorderLinePropertyValue.cPickorderLinePropertyValue;
import nl.icsvertex.scansuite.Activities.Pick.PickorderLineItemPropertyInputActvity;
import nl.icsvertex.scansuite.R;


public class ItemPropertyTextInputFragment extends DialogFragment implements iICSDefaultFragment {

    private  TextView textHeader;
    private  EditText inputText;
    private  Button buttonCancel;
    private  Button buttonOk;
    private String typeStr;


    public ItemPropertyTextInputFragment(String pvTypeStr) {
        this.typeStr = pvTypeStr;
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

        if (this.typeStr.equals("DECIMAL")){
            this.inputText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED| InputType.TYPE_NUMBER_FLAG_DECIMAL);
        }
    }

    @Override
    public void mSetListeners() {
        this.mSetCancelListener();
        this.mSetOKListener();
    }

    public  void pHandleScan(cBarcodeScan pvBarcodeScan) {
        this.inputText.setText(pvBarcodeScan.getBarcodeOriginalStr());
        mHandleOk();
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
            PickorderLineItemPropertyInputActvity pickorderLineItemPropertyInputActvity = (PickorderLineItemPropertyInputActvity)cAppExtension.activity;
            pickorderLineItemPropertyInputActvity.pHandeManualAction(cBarcodeScan.pFakeScan(this.inputText.getText().toString()));
            dismiss();
    }

}
