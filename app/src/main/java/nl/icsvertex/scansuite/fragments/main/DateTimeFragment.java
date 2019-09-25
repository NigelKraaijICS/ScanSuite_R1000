package nl.icsvertex.scansuite.fragments.main;


import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import ICS.Interfaces.iICSDefaultFragment;
import SSU_WHS.General.cPublicDefinitions;
import ICS.Utils.cDateAndTime;
import nl.icsvertex.scansuite.R;

public class DateTimeFragment extends Fragment implements iICSDefaultFragment {
    TextView textViewCurrentDate;
    TextView textViewCurrentTime;
    TextView textViewTimeZone;
    ImageButton buttonChangeDateTime;

    public static DateTimeFragment newInstance() {
        return new DateTimeFragment();
    }

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
        mSetViewModels();
        mFieldsInitialize();
        mSetListeners();
    }

    @Override
    public void mFindViews() {
        textViewCurrentDate = getView().findViewById(R.id.textViewCurrentDate);
        textViewCurrentTime = getView().findViewById(R.id.textViewCurrentTime);
        textViewTimeZone = getView().findViewById(R.id.textViewTimeZone);
        buttonChangeDateTime = getView().findViewById(R.id.buttonChangeDateTime);
    }

    @Override
    public void mSetViewModels() {

    }

    @Override
    public void mFieldsInitialize() {
        textViewCurrentTime.setText(cDateAndTime.pGetCurrentTimeStr());
        textViewCurrentDate.setText(cDateAndTime.pGetCurrentLongDateStr());
        textViewTimeZone.setText(cDateAndTime.pGetCurrentTimeZoneStr());
    }

    @Override
    public void mSetListeners() {
        mChangeDateTimeListener();
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
