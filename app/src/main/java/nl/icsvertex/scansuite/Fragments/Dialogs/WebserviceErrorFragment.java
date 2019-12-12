package nl.icsvertex.scansuite.Fragments.Dialogs;


import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.cardview.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ICS.Interfaces.iICSDefaultFragment;
import SSU_WHS.General.cPublicDefinitions;
import ICS.Utils.cText;
import ICS.Utils.cUserInterface;
import nl.icsvertex.scansuite.R;


public class WebserviceErrorFragment extends DialogFragment implements iICSDefaultFragment {

    private  List<String> errorList;
    private ImageView imageNoEntry;
    private CardView errorContainer;
    private TextView textErrors;
    private Button buttonCancel;
    private ConstraintLayout webserviceErrorContainer;


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
        mFieldsInitialize();
        mSetListeners();
        cUserInterface.pEnableScanner();
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
                cUserInterface.pDoRotate(imageNoEntry, 0);
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
        isCurrentlyShown = errorContainer.getVisibility() == View.VISIBLE;
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
