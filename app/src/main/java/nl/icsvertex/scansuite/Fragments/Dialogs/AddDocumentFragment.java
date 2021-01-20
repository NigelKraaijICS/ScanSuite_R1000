package nl.icsvertex.scansuite.Fragments.Dialogs;

import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Objects;

import ICS.Interfaces.iICSDefaultFragment;
import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cRegex;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Basics.BarcodeLayouts.cBarcodeLayout;
import nl.icsvertex.scansuite.Activities.Returns.ReturnorderDocumentsActivity;
import nl.icsvertex.scansuite.R;

public class AddDocumentFragment extends DialogFragment implements iICSDefaultFragment {

    //Region Public Properties


    //End Region Public Properties

    //Region Private Properties
    private TextView textViewAddDocumentHeader;
    private TextView textViewAddDocumentText;
    private EditText editTextAddDocument;
    private Button addDocumentButton;
    private Button cancelButton;
    //End Region Private Properties


    //Region Constructor

    //End Region Constructor

    //Region Default Methods

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_document, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        cAppExtension.dialogFragment  = this;
        this.mFragmentInitialize();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        cBarcodeScan.pUnregisterBarcodeFragmentReceiver(this.getClass().getSimpleName());
    }

    @Override
    public void onResume() {
        super.onResume();
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = WindowManager.LayoutParams.WRAP_CONTENT;
        Objects.requireNonNull(Objects.requireNonNull(getDialog()).getWindow()).setLayout(width, height);
        cBarcodeScan.pRegisterBarcodeFragmentReceiver(this.getClass().getSimpleName());
    }

    @Override
    public void mFragmentInitialize() {
        this.mFindViews();
        this.mFieldsInitialize();
        this.mSetListeners();

        cBarcodeScan.pRegisterBarcodeFragmentReceiver(this.getClass().getSimpleName());
    }

    @Override
    public void mFindViews() {

        if (getView() != null) {
            this.textViewAddDocumentHeader = getView().findViewById(R.id.textViewAddDocumentHeader );
            this.textViewAddDocumentText = getView().findViewById(R.id.textViewAddDocumentText);
            this.editTextAddDocument = getView().findViewById(R.id.editTextAddDocument);
            this.addDocumentButton = getView().findViewById(R.id.addDocumentButton);
            this.cancelButton = getView().findViewById(R.id.cancelButton);
        }


    }


    @Override
    public void mFieldsInitialize() {
        this.textViewAddDocumentHeader.setText(R.string.add_document_header_default);

        InputFilter[] filterArray = new InputFilter[1];
        filterArray[0] = new InputFilter.LengthFilter(20);
        this.editTextAddDocument.setFilters(filterArray);
        cUserInterface.pShowKeyboard(editTextAddDocument);
        this.textViewAddDocumentText.setVisibility(View.GONE);
    }

    @Override
    public void mSetListeners() {
        this.mSetAddDocumentListener();
        this.mSetCancelListener();
    }

    private void mSetCancelListener() {
        this.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cAppExtension.dialogFragment.dismiss();

                if (cAppExtension.activity instanceof  ReturnorderDocumentsActivity) {
                    ReturnorderDocumentsActivity returnorderDocumentsActivity = (ReturnorderDocumentsActivity)cAppExtension.activity;
                    returnorderDocumentsActivity.pHandleFragmentDismissed();
                }

            }
        });
    }
    private void mSetAddDocumentListener() {
        this.addDocumentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cAppExtension.activity instanceof ReturnorderDocumentsActivity) {

                    ReturnorderDocumentsActivity returnorderDocumentsActivity = (ReturnorderDocumentsActivity)cAppExtension.activity;

                    if (editTextAddDocument.getText().toString().trim().isEmpty()) {
                        cUserInterface.pDoNope(editTextAddDocument, true, true);
                        return;
                    }

                    cAppExtension.dialogFragment.dismiss();
                    returnorderDocumentsActivity.pHandleAddDocument(editTextAddDocument.getText().toString());

                }
            }
        });
    }


    public void pHandleScan(String pvScannedBarcodeStr) {

        //Has prefix, so check if this is a BIN
        if (cRegex.pHasPrefix(pvScannedBarcodeStr)) {

            boolean foundBin = false;

            if (cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvScannedBarcodeStr, cBarcodeLayout.barcodeLayoutEnu.DOCUMENT)) {
                foundBin = true;
            }

            if (foundBin) {
                //has prefix, is bin
                this.editTextAddDocument.setText(cRegex.pStripRegexPrefixStr(pvScannedBarcodeStr));
                this.addDocumentButton.callOnClick();
            }
            else {
                //has prefix, isn't bin
                cUserInterface.pDoNope(this.editTextAddDocument, true, true);
            }
            return;
        }

        //no prefix, fine
        this.editTextAddDocument.setText(pvScannedBarcodeStr);
        this.addDocumentButton.callOnClick();
    }

}
