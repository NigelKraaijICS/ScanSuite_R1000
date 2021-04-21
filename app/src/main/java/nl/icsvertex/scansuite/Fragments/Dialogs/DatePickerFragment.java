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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


import ICS.Interfaces.iICSDefaultFragment;
import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.LineItemProperty.LinePropertyValue.cLinePropertyValue;
import nl.icsvertex.scansuite.Activities.Pick.PickorderLineItemPropertyInputActvity;
import nl.icsvertex.scansuite.Activities.Receive.ReceiveorderLinePropertyInputActivity;
import nl.icsvertex.scansuite.R;

public  class DatePickerFragment extends DialogFragment implements iICSDefaultFragment {

    public DatePickerFragment(ArrayList<cLinePropertyValue> pvLinePropertyValues){
        if (pvLinePropertyValues != null){
            this.propertyObl = new ArrayList<>();
            for (cLinePropertyValue propertyValue : pvLinePropertyValues){
                this.propertyObl.add(propertyValue.getValueStr());
            }
        }


    }

    private ArrayList<String> propertyObl;
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

        mFindViews();
        mSetListeners();
        mFieldsInitialize();
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
        Calendar calendar;

        if (cLinePropertyValue.currentLinePropertyValue.getValueStr() != null) {
            calendar = Calendar(cLinePropertyValue.currentLinePropertyValue.getValueStr());
            if (calendar == null){
                calendar = Calendar.getInstance();
            }
            this.datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), null);
        }
        else{
            calendar = Calendar.getInstance();
            this.datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), null);
        }
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

    private Calendar Calendar (String strDate)
    {

        try
        {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");// this is your date format "12/24/2013" = "MM/dd/yyy"
            java.util.Date date = formatter.parse(strDate);//convert to date
            Calendar cal = Calendar.getInstance();// get calendar instance
            cal.setTime(date);//set the calendar date to your date
            return cal;
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        return null;
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
        if (!this.mCheckValueAllowedBln()) {return;}

        if (cAppExtension.activity instanceof PickorderLineItemPropertyInputActvity){
            PickorderLineItemPropertyInputActvity pickorderLineItemPropertyInputActvity = (PickorderLineItemPropertyInputActvity) cAppExtension.activity;
            pickorderLineItemPropertyInputActvity.pHandeManualAction(cBarcodeScan.pFakeScan(this.dateStr));
            dismiss();
        }
        if (cAppExtension.activity instanceof ReceiveorderLinePropertyInputActivity){
            ReceiveorderLinePropertyInputActivity receiveorderLinePropertyInputActivity = (ReceiveorderLinePropertyInputActivity) cAppExtension.activity;
            receiveorderLinePropertyInputActivity.pHandeManualAction(cBarcodeScan.pFakeScan(this.dateStr));
            dismiss();
        }
    }

    private boolean mCheckValueAllowedBln(){
        if (this.propertyObl == null || this.propertyObl.size() < 1){
            return true;
        }

        for (String string: this.propertyObl) {
            if (string.equalsIgnoreCase(this.dateStr)){
                return  true;
            }
        }

        cUserInterface.pDoNope(this.datePicker, true, true);
        cUserInterface.pShowSnackbarMessage(this.buttonOk, cAppExtension.activity.getString(R.string.message_property_not_allowed),R.raw.headsupsound,false);

        return false;
    }
}