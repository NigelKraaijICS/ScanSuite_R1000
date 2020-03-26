package nl.icsvertex.scansuite.Fragments.Dialogs;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;

import ICS.Interfaces.iICSDefaultFragment;
import ICS.Utils.cUserInterface;
import SSU_WHS.General.cPublicDefinitions;
import nl.icsvertex.scansuite.R;


public class HugeErrorFragment extends DialogFragment implements iICSDefaultFragment {

    private  String errorMessage;
    private  String extraMessage;

    private  ConstraintLayout containerHugeError;
    private  TextView textViewErrorMessage;
    private  TextView textViewExtraError;


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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        if (args != null) {
            this.errorMessage = args.getString(cPublicDefinitions.HUGEERROR_ERRORMESSAGE, getString(R.string.error_unspecified));
            this.extraMessage = args.getString(cPublicDefinitions.HUGEERROR_EXTRASTRING, "");
        }

        this.mFragmentInitialize();
    }

    @Override
    public void mFragmentInitialize() {
        this.mFindViews();
        this.mFieldsInitialize();
        this.mSetListeners();
        cUserInterface.pEnableScanner();
    }

    @Override
    public void mFindViews() {

        if (getView() != null) {
            this.textViewErrorMessage = getView().findViewById(R.id.textViewErrorMessage);
            this.textViewExtraError = getView().findViewById(R.id.textViewExtraError);
            this.containerHugeError = getView().findViewById(R.id.containerHugeError);
        }
    }



    @Override
    public void mFieldsInitialize() {
        this.textViewErrorMessage.setText(errorMessage);
        if (this.extraMessage.trim().isEmpty()) {
            this.textViewExtraError.setVisibility(View.GONE);
        }
        this.textViewExtraError.setText(this.extraMessage);
    }

    @Override
    public void mSetListeners() {
        this.mSetClickListener();
    }

    private void mSetClickListener() {
        this.containerHugeError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }


}
