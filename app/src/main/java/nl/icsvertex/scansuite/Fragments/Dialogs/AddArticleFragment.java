package nl.icsvertex.scansuite.Fragments.Dialogs;


import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Objects;

import ICS.Interfaces.iICSDefaultFragment;
import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cProductFlavor;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Basics.BarcodeLayouts.cBarcodeLayout;
import nl.icsvertex.scansuite.Activities.Inventory.InventoryorderBinActivity;
import nl.icsvertex.scansuite.Activities.Receive.ReceiveLinesActivity;
import nl.icsvertex.scansuite.Activities.Returns.ReturnorderDocumentActivity;
import nl.icsvertex.scansuite.BuildConfig;
import nl.icsvertex.scansuite.R;

public class AddArticleFragment extends DialogFragment implements iICSDefaultFragment {

    //Region Public Properties


    //End Region Public Properties

    //Region Private Properties

    private static TextView textViewAddArticleHeader;
    private static TextView textViewAddArticleText;
    private static EditText editTextAddArticle;
    private static Button addArticleButton;
    private static Button cancelButton;

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
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = WindowManager.LayoutParams.WRAP_CONTENT;
        Objects.requireNonNull(Objects.requireNonNull(getDialog()).getWindow()).setLayout(width, height);
        cBarcodeScan.pRegisterBarcodeFragmentReceiver();
        cUserInterface.pEnableScanner();
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
            AddArticleFragment.textViewAddArticleHeader = getView().findViewById(R.id.textViewAddArticleHeader );
            AddArticleFragment.textViewAddArticleText = getView().findViewById(R.id.textViewAddArticleText);
            AddArticleFragment.editTextAddArticle = getView().findViewById(R.id.editTextAddArticle);
            AddArticleFragment.addArticleButton = getView().findViewById(R.id.addArticleButton);
            AddArticleFragment.cancelButton = getView().findViewById(R.id.cancelButton);
        }
    }


    @Override
    public void mFieldsInitialize() {
        AddArticleFragment.textViewAddArticleHeader.setText(R.string.add_article_header_default);

        InputFilter[] filterArray = new InputFilter[1];
        filterArray[0] = new InputFilter.LengthFilter(20);
        AddArticleFragment.editTextAddArticle.setFilters(filterArray);
        if (BuildConfig.FLAVOR.equalsIgnoreCase(cProductFlavor.FlavorEnu.BMN.toString())) {
            AddArticleFragment.editTextAddArticle.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
        cUserInterface.pShowKeyboard(AddArticleFragment.editTextAddArticle);
        AddArticleFragment.textViewAddArticleText.setVisibility(View.GONE);
    }

    @Override
    public void mSetListeners() {
        this.mSetAddArticleListener();
        this.mSetCancelListener();
        this.mSetEditorActionListener();
    }

    private void mSetCancelListener() {
        AddArticleFragment.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (cAppExtension.activity instanceof  InventoryorderBinActivity) {
                    cAppExtension.dialogFragment.dismiss();
                    InventoryorderBinActivity.pHandleAddArticleFragmentDismissed();
                    return;
                }

                dismiss();


            }
        });
    }

    private void mSetAddArticleListener() {
        AddArticleFragment.addArticleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (AddArticleFragment.editTextAddArticle.getText().toString().trim().isEmpty()) {
                    cUserInterface.pDoNope(AddArticleFragment.editTextAddArticle, true, true);
                    return;
                }

                cAppExtension.dialogFragment.dismiss();

                if (cAppExtension.activity instanceof InventoryorderBinActivity) {

                    InventoryorderBinActivity.pHandleAddArticleFragmentDismissed();
                    //Pass fake scan
                    InventoryorderBinActivity.pHandleScan(cBarcodeScan.pFakeScan( AddArticleFragment.editTextAddArticle.getText().toString().trim()), true);
                }

                if (cAppExtension.activity instanceof ReturnorderDocumentActivity) {

                    ReturnorderDocumentActivity.pHandleFragmentDismissed();
                    //Pass fake scan
                    ReturnorderDocumentActivity.pHandleScan(cBarcodeScan.pFakeScan( AddArticleFragment.editTextAddArticle.getText().toString().trim()));
                }

                if (cAppExtension.activity instanceof ReceiveLinesActivity) {

                    //Pass fake scan
                    ReceiveLinesActivity.pHandleScan(cBarcodeScan.pFakeScan( AddArticleFragment.editTextAddArticle.getText().toString().trim()), false);
                }

            }
        });
    }

    public static void pHandleScan(cBarcodeScan pvBarcodeScan) {

        //Has prefix, so check if this is an
        if (!cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(), cBarcodeLayout.barcodeLayoutEnu.ARTICLE)) {
            cUserInterface.pDoNope(AddArticleFragment.editTextAddArticle, true, true);
            return;
        }

        //no prefix, fine
        AddArticleFragment.editTextAddArticle.setText(pvBarcodeScan.getBarcodeOriginalStr());
        AddArticleFragment.addArticleButton.callOnClick();
    }
    private void mSetEditorActionListener() {
        AddArticleFragment.editTextAddArticle.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE || i == EditorInfo.IME_ACTION_GO ) {
                    AddArticleFragment.addArticleButton.callOnClick();
                }
                return true;
            }
        });
    }
}
