package nl.icsvertex.scansuite.Fragments.dialogs;


import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ICS.Interfaces.iICSDefaultFragment;
import SSU_WHS.General.cPublicDefinitions;
import nl.icsvertex.scansuite.R;


public class HugeErrorFragment extends DialogFragment implements iICSDefaultFragment {

    private String errorMessage;
    private  String extraMessage;

    private ConstraintLayout containerHugeError;
    private TextView textViewErrorMessage;
    private TextView textViewExtraError;


    public HugeErrorFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_huge_error, container, false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        errorMessage = args.getString(cPublicDefinitions.HUGEERROR_ERRORMESSAGE, getString(R.string.error_unspecified));
        extraMessage = args.getString(cPublicDefinitions.HUGEERROR_EXTRASTRING, "");

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
        textViewErrorMessage = getView().findViewById(R.id.textViewErrorMessage);
        textViewExtraError = getView().findViewById(R.id.textViewExtraError);
        containerHugeError = getView().findViewById(R.id.containerHugeError);
    }



    @Override
    public void mFieldsInitialize() {
        textViewErrorMessage.setText(errorMessage);
        if (extraMessage.trim().isEmpty()) {
            textViewExtraError.setVisibility(View.GONE);
        }
        textViewExtraError.setText(extraMessage);
    }

    @Override
    public void mSetListeners() {
        mSetClickListener();
    }

    private void mSetClickListener() {
        containerHugeError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }


}
