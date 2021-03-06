package nl.icsvertex.scansuite.Fragments.Main;


import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextClock;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ICS.Interfaces.iICSDefaultFragment;
import ICS.Utils.cDateAndTime;
import SSU_WHS.General.cPublicDefinitions;
import nl.icsvertex.scansuite.R;

public class DateTimeFragment extends Fragment implements iICSDefaultFragment {

    //Region Pubic Properties

    //Region Public Properties

    //Region Private Properties

    private  TextView textViewCurrentDate;
    private  TextClock textClockCurrentTime;
    private  TextView textViewTimeZone;
    private  ImageButton buttonChangeDateTime;

    //Region Private Properties




    public DateTimeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_datetime, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
       this.mFragmentInitialize();
    }

    @Override
    public void mFragmentInitialize() {
        this. mFindViews();
        this.mFieldsInitialize();
        this.mSetListeners();
    }

    @Override
    public void mFindViews() {

        if (getView() != null) {
            this.textViewCurrentDate = getView().findViewById(R.id.textViewCurrentDate);
            this.textClockCurrentTime = getView().findViewById(R.id.textClockCurrentTime);
            this.textViewTimeZone = getView().findViewById(R.id.textViewTimeZone);
            this.buttonChangeDateTime = getView().findViewById(R.id.buttonChangeDateTime);
        }

    }


    @Override
    public void mFieldsInitialize() {
        this.mSetDateAndTimeZone();
    }

    @Override
    public void mSetListeners() {
        this.mChangeDateTimeListener();
        this.mClockChangeListener();
    }

    private void mClockChangeListener() {
        this.textClockCurrentTime.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mSetDateAndTimeZone();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    private void mSetDateAndTimeZone() {
        this.textViewCurrentDate.setText(cDateAndTime.pGetCurrentLongDateStr());
        this.textViewTimeZone.setText(cDateAndTime.pGetCurrentTimeZoneStr());
    }

    private void mChangeDateTimeListener() {
        this.buttonChangeDateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(Settings.ACTION_DATE_SETTINGS), cPublicDefinitions.CHANGEDATETIME_REQUESTCODE);
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == cPublicDefinitions.CHANGEDATETIME_REQUESTCODE) {
            mFieldsInitialize();
        }
    }
}
