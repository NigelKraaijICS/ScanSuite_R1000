package nl.icsvertex.scansuite.Fragments.Dialogs;


import android.os.Bundle;

import androidx.annotation.NonNull;
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

    private static  List<String> errorList;
    private static ImageView imageNoEntry;
    private static CardView errorContainer;
    private static TextView textErrors;
    private static Button buttonCancel;

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
            WebserviceErrorFragment.errorList = (List<String>)bundle.getSerializable(cPublicDefinitions.WEBSERVICEERROR_LIST_TAG);
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
            WebserviceErrorFragment.imageNoEntry = getView().findViewById(R.id.imageNoEntry);
            WebserviceErrorFragment.errorContainer = getView().findViewById(R.id.errorContainer);
            WebserviceErrorFragment.textErrors = getView().findViewById(R.id.textErrors);
            WebserviceErrorFragment.buttonCancel = getView().findViewById(R.id.buttonCancel);
        }

    }

    @Override
    public void mFieldsInitialize() {
       WebserviceErrorFragment.errorContainer.setVisibility(View.INVISIBLE);
       this.mFillErrors();
    }

    @Override
    public void mSetListeners() {
        this.mSetErrorImageListener();
        this.mSetCancelListener();
    }

    private void mFillErrors() {
        if (WebserviceErrorFragment.errorList != null) {
            StringBuilder listToShow = new StringBuilder();
            for (String errorMessage : WebserviceErrorFragment.errorList) {
                listToShow.append(errorMessage).append(cText.NEWLINE);
            }
            WebserviceErrorFragment.textErrors.setText(listToShow.toString());
        }
    }
    private void mSetErrorImageListener() {
        WebserviceErrorFragment.imageNoEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cUserInterface.pDoRotate(WebserviceErrorFragment.imageNoEntry, 0);
                mShowOrHideDetails();
            }
        });
    }
    private void mSetCancelListener() {
       WebserviceErrorFragment.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
    private void mShowOrHideDetails() {
        boolean isCurrentlyShown;
        isCurrentlyShown = WebserviceErrorFragment.errorContainer.getVisibility() == View.VISIBLE;
        if (isCurrentlyShown) {
            WebserviceErrorFragment.errorContainer.animate().scaleY(0).withEndAction(new Runnable() {
                @Override
                public void run() {
                    WebserviceErrorFragment.errorContainer.setVisibility(View.INVISIBLE);
                }
            }).start();
        }
        else {
            WebserviceErrorFragment.errorContainer.animate().scaleY(1).start();
            WebserviceErrorFragment.errorContainer.setVisibility(View.VISIBLE);
        }
    }
}
