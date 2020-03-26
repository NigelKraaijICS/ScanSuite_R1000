package nl.icsvertex.scansuite.Fragments.Dialogs;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;

import java.util.List;

import ICS.Interfaces.iICSDefaultFragment;
import ICS.Utils.cText;
import ICS.Utils.cUserInterface;
import SSU_WHS.General.cPublicDefinitions;
import nl.icsvertex.scansuite.R;


public class WebserviceErrorFragment extends DialogFragment implements iICSDefaultFragment {

    private List<String> errorList;
    private ImageView imageNoEntry;
    private  CardView errorContainer;
    private  TextView textErrors;
    private  Button buttonCancel;

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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            this.errorList = (List<String>) bundle.getSerializable(cPublicDefinitions.WEBSERVICEERROR_LIST_TAG);
        }
        this.mFragmentInitialize();
    }

    @Override
    public void mFragmentInitialize() {
        this.mFindViews();
        this.mFieldsInitialize();
        this. mSetListeners();
        cUserInterface.pEnableScanner();
    }
    @Override
    public void mFindViews() {
        if (getView() != null) {
            this.imageNoEntry = getView().findViewById(R.id.imageNoEntry);
            this.errorContainer = getView().findViewById(R.id.errorContainer);
            this.textErrors = getView().findViewById(R.id.textErrors);
            this.buttonCancel = getView().findViewById(R.id.buttonCancel);
        }

    }

    @Override
    public void mFieldsInitialize() {
        this.errorContainer.setVisibility(View.INVISIBLE);
       this.mFillErrors();
    }

    @Override
    public void mSetListeners() {
        this.mSetErrorImageListener();
        this.mSetCancelListener();
    }

    private void mFillErrors() {
        if (this.errorList != null) {
            StringBuilder listToShow = new StringBuilder();
            for (String errorMessage : this.errorList) {
                listToShow.append(errorMessage).append(cText.NEWLINE);
            }
            this.textErrors.setText(listToShow.toString());
        }
    }
    private void mSetErrorImageListener() {
        this.imageNoEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cUserInterface.pDoRotate(imageNoEntry, 0);
                mShowOrHideDetails();
            }
        });
    }
    private void mSetCancelListener() {
        this.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
    private void mShowOrHideDetails() {
        boolean isCurrentlyShown;
        isCurrentlyShown = this.errorContainer.getVisibility() == View.VISIBLE;
        if (isCurrentlyShown) {
            this.errorContainer.animate().scaleY(0).withEndAction(new Runnable() {
                @Override
                public void run() {
                    errorContainer.setVisibility(View.INVISIBLE);
                }
            }).start();
        }
        else {
            this.errorContainer.animate().scaleY(1).start();
            this.errorContainer.setVisibility(View.VISIBLE);
        }
    }
}
