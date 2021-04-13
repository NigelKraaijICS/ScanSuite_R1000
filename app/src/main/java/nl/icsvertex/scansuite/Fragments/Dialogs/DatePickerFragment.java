package nl.icsvertex.scansuite.Fragments.Dialogs;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;


import ICS.Interfaces.iICSDefaultFragment;
import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import nl.icsvertex.scansuite.Activities.Pick.PickorderLineItemPropertyInputActvity;
import nl.icsvertex.scansuite.R;

public  class DatePickerFragment extends DialogFragment implements iICSDefaultFragment {

    public DatePickerFragment(){

    }
    private Button buttonCancel;
    private  Button buttonOk;
    private DatePicker datePicker;
    private String dateStr;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_select_date, container, false);

    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        cAppExtension.dialogFragment = this;
        this.mFragmentInitialize();
    }

    @Override
    public void onResume() {
        super.onResume();
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

        mFieldsInitialize();
        mFindViews();
        mSetListeners();
    }

    @Override
    public void mFindViews() {
        if (getView() != null) {
            this.buttonCancel = getView().findViewById(R.id.buttonCancel);
            this.buttonOk = getView().findViewById(R.id.buttonOk);
            this.datePicker = getView().findViewById(R.id.datePicker);
        }
    }

    @Override
    public void mFieldsInitialize() {

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
                int year = datePicker.getYear();
                int month = datePicker.getMonth();
                int day = datePicker.getDayOfMonth();

                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, day);

                @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                dateStr = format.format(calendar.getTime());
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

    public  void pHandleScan(cBarcodeScan pvBarcodeScan) {
        this.dateStr= pvBarcodeScan.getBarcodeOriginalStr();
        mHandleOk();
    }

    private void mHandleOk() {
        if (cAppExtension.activity instanceof PickorderLineItemPropertyInputActvity){
            PickorderLineItemPropertyInputActvity pickorderLineItemPropertyInputActvity = (PickorderLineItemPropertyInputActvity) cAppExtension.activity;
            pickorderLineItemPropertyInputActvity.pHandeManualAction(cBarcodeScan.pFakeScan(this.dateStr));
            dismiss();
        }
    }
}