package nl.icsvertex.scansuite.Fragments.Dialogs;

import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

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
    private static TextView textViewAddDocumentHeader;
    private static TextView textViewAddDocumentText;
    private static EditText editTextAddDocument;
    private static Button addDocumentButton;
    private static Button cancelButton;
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
        cBarcodeScan.pUnregisterBarcodeFragmentReceiver();
    }

    @Override
    public void onResume() {
        super.onResume();
        cBarcodeScan.pRegisterBarcodeFragmentReceiver();
    }

    @Override
    public void mFragmentInitialize() {
        this.mFindViews();
        this.mFieldsInitialize();
        this.mSetListeners();

        cBarcodeScan.pRegisterBarcodeFragmentReceiver();
    }

    @Override
    public void mFindViews() {

        if (getView() != null) {
            AddDocumentFragment.textViewAddDocumentHeader = getView().findViewById(R.id.textViewAddDocumentHeader );
            AddDocumentFragment.textViewAddDocumentText = getView().findViewById(R.id.textViewAddDocumentText);
            AddDocumentFragment.editTextAddDocument = getView().findViewById(R.id.editTextAddDocument);
            AddDocumentFragment.addDocumentButton = getView().findViewById(R.id.addDocumentButton);
            AddDocumentFragment.cancelButton = getView().findViewById(R.id.cancelButton);
        }


    }


    @Override
    public void mFieldsInitialize() {
        AddDocumentFragment.textViewAddDocumentHeader.setText(R.string.add_document_header_default);

        InputFilter[] filterArray = new InputFilter[1];
        filterArray[0] = new InputFilter.LengthFilter(20);
        AddDocumentFragment.editTextAddDocument.setFilters(filterArray);
        cUserInterface.pShowKeyboard(editTextAddDocument);
        AddDocumentFragment.textViewAddDocumentText.setVisibility(View.GONE);
    }

    @Override
    public void mSetListeners() {
        this.mSetAddDocumentListener();
        this.mSetCancelListener();
    }

    private void mSetCancelListener() {
        AddDocumentFragment.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cAppExtension.dialogFragment.dismiss();
                ReturnorderDocumentsActivity.pHandleAddBinFragmentDismissed();
            }
        });
    }
    private void mSetAddDocumentListener() {
        AddDocumentFragment.addDocumentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cAppExtension.activity instanceof ReturnorderDocumentsActivity) {

                    if (AddDocumentFragment.editTextAddDocument.getText().toString().trim().isEmpty()) {
                        cUserInterface.pDoNope(AddDocumentFragment.editTextAddDocument, true, true);
                        return;
                    }

                    cAppExtension.dialogFragment.dismiss();
                    ReturnorderDocumentsActivity.pHandleScan(cBarcodeScan.pFakeScan(AddDocumentFragment.editTextAddDocument.getText().toString()));

                }
            }
        });
    }


    public static void pHandleScan(String pvScannedBarcodeStr) {

        //Has prefix, so check if this is a BIN
        if (cRegex.hasPrefix(pvScannedBarcodeStr)) {

            boolean foundBin = false;

            if (cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvScannedBarcodeStr, cBarcodeLayout.barcodeLayoutEnu.DOCUMENT)) {
                foundBin = true;
            }

            if (foundBin) {
                //has prefix, is bin
                AddDocumentFragment.editTextAddDocument.setText(cRegex.pStripRegexPrefixStr(pvScannedBarcodeStr));
                AddDocumentFragment.addDocumentButton.callOnClick();
                return;
            }
            else {
                //has prefix, isn't bin
                cUserInterface.pDoNope(AddDocumentFragment.editTextAddDocument, true, true);
                return;
            }
        }

        //no prefix, fine
        AddDocumentFragment.editTextAddDocument.setText(pvScannedBarcodeStr);
        AddDocumentFragment.addDocumentButton.callOnClick();
    }

}
