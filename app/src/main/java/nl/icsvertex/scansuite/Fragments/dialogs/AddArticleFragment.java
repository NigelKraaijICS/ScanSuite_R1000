package nl.icsvertex.scansuite.Fragments.dialogs;


import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;

import ICS.Interfaces.iICSDefaultFragment;
import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cRegex;
import ICS.Utils.cText;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Basics.BarcodeLayouts.cBarcodeLayout;
import nl.icsvertex.scansuite.Activities.inventory.InventoryorderBinActivity;
import nl.icsvertex.scansuite.Activities.inventory.InventoryorderBinsActivity;
import nl.icsvertex.scansuite.R;

public class AddArticleFragment extends DialogFragment implements iICSDefaultFragment {

    //Region Public Properties


    //End Region Public Properties

    //Region Private Properties
    private ConstraintLayout addArticleContainer;
    private TextView textViewAddArticleHeader;
    private TextView textViewAddArticleText;
    private  static EditText editTextAddArticle;
    private static Button addArticleButton;
    private Button cancelButton;
    //End Region Private Properties


    //Region Constructor
    public AddArticleFragment() {

    }
    //End Region Constructor

    //Region Default Methods

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_article, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
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
        this.textViewAddArticleHeader = getView().findViewById(R.id.textViewAddArticleHeader );
        this.textViewAddArticleText = getView().findViewById(R.id.textViewAddArticleText);
        this.editTextAddArticle = getView().findViewById(R.id.editTextAddArticle);
        this.addArticleContainer = getView().findViewById(R.id.addArticleContainer);
        this.addArticleButton = getView().findViewById(R.id.addArticleButton);
        this.cancelButton = getView().findViewById(R.id.cancelButton);
    }


    @Override
    public void mFieldsInitialize() {
        this.textViewAddArticleHeader.setText(R.string.add_article_header_default);

        InputFilter[] filterArray = new InputFilter[1];
        filterArray[0] = new InputFilter.LengthFilter(20);
        this.editTextAddArticle.setFilters(filterArray);
        cUserInterface.pShowKeyboard(this.editTextAddArticle);
        this.textViewAddArticleText.setVisibility(View.GONE);
    }

    @Override
    public void mSetListeners() {
        this.mSetAddArticleListener();
        this.mSetCancelListener();
    }

    private void mSetCancelListener() {
       this.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cAppExtension.dialogFragment.dismiss();
                InventoryorderBinActivity.pHandleAddArticleFragmentDismissed();
            }
        });
    }

    private void mSetAddArticleListener() {
        this.addArticleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (cAppExtension.activity instanceof InventoryorderBinActivity) {

                    if (AddArticleFragment.editTextAddArticle.getText().toString().trim().isEmpty()) {
                        cUserInterface.pDoNope(AddArticleFragment.editTextAddArticle, true, true);
                        return;
                    }

                    cAppExtension.dialogFragment.dismiss();

                    //Simulate a scan
                    cBarcodeScan barcodeScan = new cBarcodeScan();
                    barcodeScan.barcodeStr = AddArticleFragment.editTextAddArticle.getText().toString().trim();
                    barcodeScan.barcodeOriginalStr = AddArticleFragment.editTextAddArticle.getText().toString().trim();
                    barcodeScan.barcodeTypeStr= cText.pIntToStringStr(cBarcodeScan.BarcodeType.Unknown);

                    //Pass simulated scan
                    InventoryorderBinActivity.pHandleScan(barcodeScan);
                }
            }
        });
    }

    public static void pHandleScan(String pvScannedBarcodeStr) {

        //Has prefix, so check if this is an
        if (!cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvScannedBarcodeStr, cBarcodeLayout.barcodeLayoutEnu.ARTICLE)) {
            cUserInterface.pDoNope(AddArticleFragment.editTextAddArticle, true, true);
            return;
        }

        //no prefix, fine
        AddArticleFragment.editTextAddArticle.setText(pvScannedBarcodeStr);
        AddArticleFragment.addArticleButton.callOnClick();
    }
}
