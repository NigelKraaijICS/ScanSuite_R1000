package nl.icsvertex.scansuite.Fragments.Main;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextClock;
import android.widget.TextView;

import ICS.Interfaces.iICSDefaultFragment;
import ICS.Utils.cUserInterface;
import SSU_WHS.General.cPublicDefinitions;
import ICS.Utils.cDateAndTime;
import nl.icsvertex.scansuite.R;

public class DateTimeFragment extends Fragment implements iICSDefaultFragment {

    //Region Pubic Properties

    //Region Public Properties

    //Region Private Properties

    private TextView textViewCurrentDate;
    private TextClock textClockCurrentTime;
    private TextView textViewTimeZone;
    private ImageButton buttonChangeDateTime;

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
        mFragmentInitialize();
    }

    @Override
    public void mFragmentInitialize() {
        mFindViews();
        mFieldsInitialize();
        mSetListeners();
    }

    @Override
    public void mFindViews() {
        textViewCurrentDate = getView().findViewById(R.id.textViewCurrentDate);
        textClockCurrentTime = getView().findViewById(R.id.textClockCurrentTime);
        textViewTimeZone = getView().findViewById(R.id.textViewTimeZone);
        buttonChangeDateTime = getView().findViewById(R.id.buttonChangeDateTime);
    }


    @Override
    public void mFieldsInitialize() {
        mSetDateAndTimeZone();
    }

    @Override
    public void mSetListeners() {
        mChangeDateTimeListener();
        mClockChangeListener();
    }

    private void mClockChangeListener() {
        textClockCurrentTime.addTextChangedListener(new TextWatcher() {

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
        textViewCurrentDate.setText(cDateAndTime.pGetCurrentLongDateStr());
        textViewTimeZone.setText(cDateAndTime.pGetCurrentTimeZoneStr());
    }

    private void mChangeDateTimeListener() {
        buttonChangeDateTime.setOnClickListener(new View.OnClickListener() {
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
