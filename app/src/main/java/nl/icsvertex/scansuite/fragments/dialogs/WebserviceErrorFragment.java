package nl.icsvertex.scansuite.fragments.dialogs;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ICS.Interfaces.iICSDefaultFragment;
import SSU_WHS.cPublicDefinitions;
import ICS.Utils.cText;
import ICS.Utils.cUserInterface;
import nl.icsvertex.scansuite.R;


public class WebserviceErrorFragment extends DialogFragment implements iICSDefaultFragment {
    List<String> errorList;
    ImageView imageNoEntry;
    CardView errorContainer;
    TextView textErrors;
    Button buttonCancel;
    ConstraintLayout webserviceErrorContainer;


    public WebserviceErrorFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_webservice_error, container, false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        errorList = (List<String>)bundle.getSerializable(cPublicDefinitions.WEBSERVICEERROR_LIST_TAG);

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
        webserviceErrorContainer = getView().findViewById(R.id.webserviceErrorContainer);
        imageNoEntry = getView().findViewById(R.id.imageNoEntry);
        errorContainer = getView().findViewById(R.id.errorContainer);
        textErrors = getView().findViewById(R.id.textErrors);
        buttonCancel = getView().findViewById(R.id.buttonCancel);
    }

    @Override
    public void mSetViewModels() {

    }
    @Override
    public void mFieldsInitialize() {
        errorContainer.setVisibility(View.INVISIBLE);
        mFillErrors();
    }
    @Override
    public void mSetListeners() {
        mSetErrorImageListener();
        mSetCancelListener();
    }
    private void mFillErrors() {
        if (errorList != null) {
            String listToShow = "";
            for (String errorMessage : errorList) {
                listToShow += errorMessage + cText.NEWLINE;
            }
            textErrors.setText(listToShow);
        }
    }
    private void mSetErrorImageListener() {
        imageNoEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cUserInterface.doRotate(imageNoEntry, 0);
                mShowOrHideDetails();
            }
        });
    }
    private void mSetCancelListener() {
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
    private void mShowOrHideDetails() {
        Boolean isCurrentlyShown;
        if (errorContainer.getVisibility() == View.VISIBLE) {
            isCurrentlyShown = true;
        }
        else {
            isCurrentlyShown = false;
        }
        if (isCurrentlyShown) {
            errorContainer.animate().scaleY(0).withEndAction(new Runnable() {
                @Override
                public void run() {
                    errorContainer.setVisibility(View.INVISIBLE);
                }
            }).start();
        }
        else {
            errorContainer.animate().scaleY(1).start();
            errorContainer.setVisibility(View.VISIBLE);
        }
    }
}
