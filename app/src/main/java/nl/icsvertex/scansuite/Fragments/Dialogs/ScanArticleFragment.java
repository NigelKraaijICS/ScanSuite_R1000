package nl.icsvertex.scansuite.Fragments.Dialogs;


import android.os.Bundle;
import android.text.InputFilter;
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
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Basics.BarcodeLayouts.cBarcodeLayout;
import nl.icsvertex.scansuite.Activities.General.MenuActivity;
import nl.icsvertex.scansuite.Activities.Inventory.InventoryorderBinActivity;
import nl.icsvertex.scansuite.Activities.Receive.ReceiveLinesActivity;
import nl.icsvertex.scansuite.Activities.Returns.ReturnorderDocumentActivity;
import nl.icsvertex.scansuite.R;

public class ScanArticleFragment extends DialogFragment implements iICSDefaultFragment {

    //Region Public Properties


    //End Region Public Properties

    //Region Private Properties

    private TextView textViewScanArticleHeader;
    private TextView textViewScanArticleText;
    private EditText editTextScanArticle;
    private Button scanArticleButton;
    private Button cancelButton;

    //End Region Private Properties


    //Region Constructor
    public ScanArticleFragment() {

    }
    //End Region Constructor

    //Region Default Methods

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_scan_article, container, false);
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
        cUserInterface.pEnableScanner();
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
            this.textViewScanArticleHeader = getView().findViewById(R.id.textViewScanArticleHeader );
            this.textViewScanArticleText = getView().findViewById(R.id.textViewScanArticleText);
            this.editTextScanArticle = getView().findViewById(R.id.editTextScanArticle);
            this.scanArticleButton = getView().findViewById(R.id.scanArticleButton);
            this.cancelButton = getView().findViewById(R.id.cancelButton);
        }
    }


    @Override
    public void mFieldsInitialize() {
        this.textViewScanArticleHeader.setText(R.string.scan_article_header_default);

        InputFilter[] filterArray = new InputFilter[1];
        filterArray[0] = new InputFilter.LengthFilter(20);
        this.editTextScanArticle.setFilters(filterArray);
        cUserInterface.pShowKeyboard(this.editTextScanArticle);
        this.textViewScanArticleText.setVisibility(View.GONE);
    }

    @Override
    public void mSetListeners() {
        this.mSetScanArticleListener();
        this.mSetCancelListener();
        this.mSetEditorActionListener();
    }

    private void mSetCancelListener() {
        this.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    private void mSetScanArticleListener() {
        this.scanArticleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (editTextScanArticle.getText().toString().trim().isEmpty()) {
                    cUserInterface.pDoNope(editTextScanArticle, true, true);
                    return;
                }

               dismiss();

                if (cAppExtension.activity instanceof MenuActivity) {
                    MenuActivity menuActivity = (MenuActivity)cAppExtension.activity;
                    menuActivity.pHandleHandleArticleScanned( cBarcodeScan.pFakeScan(editTextScanArticle.getText().toString()));

                }


            }
        });
    }

    public void pHandleScan(cBarcodeScan pvBarcodeScan) {

        //Has prefix, so check if this is an
        if (!cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(), cBarcodeLayout.barcodeLayoutEnu.ARTICLE)) {
            cUserInterface.pDoNope(this.editTextScanArticle, true, true);
            return;
        }

        //no prefix, fine
        this.editTextScanArticle.setText(pvBarcodeScan.getBarcodeOriginalStr());
        this.scanArticleButton.callOnClick();
    }
    private void mSetEditorActionListener() {
        this.editTextScanArticle.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE || i == EditorInfo.IME_ACTION_GO ) {
                    scanArticleButton.callOnClick();
                }
                return true;
            }
        });
    }
}
