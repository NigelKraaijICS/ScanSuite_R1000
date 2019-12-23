package nl.icsvertex.scansuite.Fragments.Move;


import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;

import ICS.Interfaces.iICSDefaultFragment;
import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cRegex;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Basics.BarcodeLayouts.cBarcodeLayout;
import nl.icsvertex.scansuite.Activities.Move.MoveorderSelectActivity;
import nl.icsvertex.scansuite.R;


public class CreateMoveFragment extends DialogFragment implements iICSDefaultFragment {

    //Region Private Properties

    static private ConstraintLayout createMoveContainer;
    private TextView textViewCreateMoveHeader;
    private TextView textViewCreateMoveText;
    static private EditText editTextDocument;
    static private Button createMoveButton;
    static private Button cancelButton;
    ImageView createMoveImageView;
    private Boolean showDocumentBln;

    //End Region private Properties

    //Region Constructor
    public CreateMoveFragment(Boolean pvShowDocumentBln) {
        this.showDocumentBln = pvShowDocumentBln;
    }
    //End Region Constructor

    @Override
    public View onCreateView(LayoutInflater pvInflater, ViewGroup pvContainer,
                             Bundle pvSavedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = pvInflater.inflate(R.layout.fragment_create_move, pvContainer, false);
        cAppExtension.dialogFragment = this;
        return rootview;
    }

    @Override
    public void onViewCreated(View pvView, @Nullable Bundle pvSavedInstanceState) {
        this.mFragmentInitialize();
    }

    @Override
    public void onDestroy() {
        try {
            cBarcodeScan.pUnregisterBarcodeFragmentReceiver();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }
    @Override
    public void onPause() {
        try {
            cBarcodeScan.pUnregisterBarcodeFragmentReceiver();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onPause();
    }
    @Override
    public void onResume() {
        cBarcodeScan.pRegisterBarcodeFragmentReceiver();
        super.onResume();
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
        this.textViewCreateMoveHeader = getView().findViewById(R.id.textViewCreateMoveHeader);
        this.textViewCreateMoveText = getView().findViewById(R.id.textViewCreateMoveText);
        this.editTextDocument = getView().findViewById(R.id.editTextDocument);
        this.createMoveContainer = getView().findViewById(R.id.createMoveContainer);
        this.createMoveButton = getView().findViewById(R.id.closeOrderButton);
        this.cancelButton = getView().findViewById(R.id.cancelButton);
        this.createMoveImageView = getView().findViewById(R.id.createMoveImageView);
    }


    @Override
    public void mFieldsInitialize() {

        this.textViewCreateMoveHeader.setText(cAppExtension.context.getString(R.string.createmove_header_default));
        this.textViewCreateMoveText.setText(cAppExtension.context.getString(R.string.createmove_text_default));

        if (!this.showDocumentBln) {
            editTextDocument.setVisibility(View.INVISIBLE);
            return;
        }

        InputFilter[] filterArray = new InputFilter[1];
        filterArray[0] = new InputFilter.LengthFilter(50);
        this.editTextDocument.setFilters(filterArray);
        cUserInterface.pShowKeyboard(editTextDocument);
    }

    @Override
    public void mSetListeners() {
        this.mSetImageListener();
        this.mSetCreateListener();
        this.mSetCancelListener();
    }
    private void mSetImageListener() {
        this.createMoveImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View pvView) {
                cUserInterface.pDoRotate(pvView, 0);
            }
        });
    }

    private void mSetCancelListener() {
        this.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View pvView) {
                cAppExtension.dialogFragment.dismiss();
            }
        });
    }

    private void mSetCreateListener() {
        this.createMoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View pvView) {
                if (cAppExtension.activity  instanceof MoveorderSelectActivity) {
                    MoveorderSelectActivity.pCreateOrder(editTextDocument.getText().toString().trim());
                    cAppExtension.dialogFragment.dismiss();
                }
            }
        });
    }

    public static void pHandleScan(String pvScannedBarcodeStr) {

        String barcodeWithoutPrefixStr;

        //No prefix
        if (!cRegex.hasPrefix(pvScannedBarcodeStr)) {
            editTextDocument.setText(pvScannedBarcodeStr);
            return;
        }

        Boolean foundBln = false;

        if (cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvScannedBarcodeStr, cBarcodeLayout.barcodeLayoutEnu.DOCUMENT) == true) {
            foundBln = true;        }

        //has prefix, is DOCUMENT
        if (foundBln) {
            barcodeWithoutPrefixStr = cRegex.pStripRegexPrefixStr(pvScannedBarcodeStr);
            editTextDocument.setText(barcodeWithoutPrefixStr);
            return;
        }
        else {
            //has prefix, isn't DOCUMENT
            cUserInterface.pDoNope(createMoveContainer, true, true);
            return;
        }
    }

}
