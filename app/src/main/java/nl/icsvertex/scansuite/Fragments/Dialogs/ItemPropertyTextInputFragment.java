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

import java.util.ArrayList;

import ICS.Interfaces.iICSDefaultFragment;
import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cRegex;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.LineItemProperty.LinePropertyValue.cLinePropertyValue;
import nl.icsvertex.scansuite.Activities.Intake.IntakeOrderLinePropertyInputActivity;
import nl.icsvertex.scansuite.Activities.Inventory.InventoryLinePropertyInputActivity;
import nl.icsvertex.scansuite.Activities.Move.MoveLineItemPropertyActivity;
import nl.icsvertex.scansuite.Activities.Pick.PickorderLineItemPropertyInputActvity;
import nl.icsvertex.scansuite.Activities.Receive.ReceiveorderLinePropertyInputActivity;
import nl.icsvertex.scansuite.R;


public class ItemPropertyTextInputFragment extends DialogFragment implements iICSDefaultFragment {

    private  TextView textHeader;
    private  EditText inputText;
    private  Button buttonCancel;
    private  Button buttonOk;
    private ArrayList<String> propertyObl;
    private final String typeStr;


    public ItemPropertyTextInputFragment(String pvTypeStr, ArrayList<cLinePropertyValue> pvLinePropertyValues) {
        this.typeStr = pvTypeStr;
        if (pvLinePropertyValues != null){
            this.propertyObl = new ArrayList<>();
            for (cLinePropertyValue propertyValue : pvLinePropertyValues){
                if(propertyValue.getPropertyCodeStr().equalsIgnoreCase(cLinePropertyValue.currentLinePropertyValue.getPropertyCodeStr())){
                    this.propertyObl.add(propertyValue.getValueStr());}
            }
        }
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
        this.inputText.setHint(cAppExtension.activity.getString(R.string.message_please_enter, cLinePropertyValue.currentLinePropertyValue.getItemProperty().getOmschrijvingStr().toLowerCase()));
        this.textHeader.setText(cLinePropertyValue.currentLinePropertyValue.getItemProperty().getOmschrijvingStr());

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
        if (!mCheckValueAllowedBln()){
            return;
        }

        if (cAppExtension.activity instanceof PickorderLineItemPropertyInputActvity) {
            PickorderLineItemPropertyInputActvity pickorderLineItemPropertyInputActvity = (PickorderLineItemPropertyInputActvity) cAppExtension.activity;
            pickorderLineItemPropertyInputActvity.pHandeManualAction(cBarcodeScan.pFakeScan(this.inputText.getText().toString()));
            dismiss();
        }
        if (cAppExtension.activity instanceof ReceiveorderLinePropertyInputActivity) {
            ReceiveorderLinePropertyInputActivity receiveorderLinePropertyInputActivity = (ReceiveorderLinePropertyInputActivity) cAppExtension.activity;
            receiveorderLinePropertyInputActivity.pHandeManualAction(cBarcodeScan.pFakeScan(this.inputText.getText().toString()));
            dismiss();
        }
        if (cAppExtension.activity instanceof IntakeOrderLinePropertyInputActivity) {
            IntakeOrderLinePropertyInputActivity intakeOrderLinePropertyInputActivity = (IntakeOrderLinePropertyInputActivity) cAppExtension.activity;
            intakeOrderLinePropertyInputActivity.pHandeManualAction(cBarcodeScan.pFakeScan(this.inputText.getText().toString()));
            dismiss();
        }
        if (cAppExtension.activity instanceof InventoryLinePropertyInputActivity) {
            InventoryLinePropertyInputActivity inventoryLinePropertyInputActivity = (InventoryLinePropertyInputActivity) cAppExtension.activity;
            inventoryLinePropertyInputActivity.pHandeManualAction(cBarcodeScan.pFakeScan(this.inputText.getText().toString()));
            dismiss();
        }
        if (cAppExtension.activity instanceof MoveLineItemPropertyActivity) {
            MoveLineItemPropertyActivity moveLineItemPropertyActivity = (MoveLineItemPropertyActivity) cAppExtension.activity;
            moveLineItemPropertyActivity.pHandeManualAction(cBarcodeScan.pFakeScan(this.inputText.getText().toString()));
            dismiss();
        }
    }

    private boolean mCheckValueAllowedBln(){
        if (this.propertyObl == null || this.propertyObl.size() < 1){
            return true;
        }
        if (cAppExtension.activity instanceof InventoryLinePropertyInputActivity){
            return true;
        }
        //Check if allowed in activity
        if (cAppExtension.activity instanceof MoveLineItemPropertyActivity){
            return true;
        }
        for (String string: this.propertyObl) {
            if (string.equalsIgnoreCase(this.inputText.getText().toString())){
                return  true;
            }
        }

        cUserInterface.pDoNope(this.inputText, true, true);
        cUserInterface.pShowSnackbarMessage(this.buttonOk, cAppExtension.activity.getString(R.string.message_property_not_allowed),R.raw.headsupsound,false);

        return false;
    }
}
