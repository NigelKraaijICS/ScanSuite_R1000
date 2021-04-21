package nl.icsvertex.scansuite.Fragments.Dialogs;

import android.os.Bundle;
import android.text.InputFilter;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ICS.Interfaces.iICSDefaultFragment;
import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Basics.BarcodeLayouts.cBarcodeLayout;
import SSU_WHS.Basics.StockOwner.cStockOwner;
import SSU_WHS.Basics.Users.cUser;
import nl.icsvertex.scansuite.Activities.General.MenuActivity;
import nl.icsvertex.scansuite.R;

public class SearchArticleFragment extends DialogFragment implements iICSDefaultFragment {

    //Region Public Properties


    //End Region Public Properties

    //Region Private Properties

    private TextView textViewScanArticleHeader;
    private TextView textViewScanArticleText;
    private EditText editTextScanArticle;
    private EditText editTextItemNo;
    private EditText editTextVariantCode;
    private Button scanArticleButton;
    private Button cancelButton;
    private Spinner stockownerSpinner;

    //End Region Private Properties


    //Region Constructor
    public SearchArticleFragment() {

    }
    //End Region Constructor

    //Region Default Methods

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_article, container, false);
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
            this.editTextItemNo = getView().findViewById(R.id.editTextItemNo);
            this.editTextVariantCode = getView().findViewById(R.id.editTextVariantCode);
            this.editTextScanArticle = getView().findViewById(R.id.editTextScanArticle);
            this.scanArticleButton = getView().findViewById(R.id.scanArticleButton);
            this.cancelButton = getView().findViewById(R.id.cancelButton);
            this.stockownerSpinner = getView().findViewById(R.id.stockownerSpinner);
        }
    }


    @Override
    public void mFieldsInitialize() {
        this.textViewScanArticleHeader.setText(R.string.search_article_header_default);
        this.mShowStockOwnerSpinner();

        InputFilter[] filterArray = new InputFilter[1];
        filterArray[0] = new InputFilter.LengthFilter(20);

        InputFilter[] variantArray = new InputFilter[1];
        variantArray[0] = new InputFilter.LengthFilter(10);

        this.editTextScanArticle.setFilters(filterArray);
        this.editTextItemNo.setFilters(filterArray);
        this.editTextVariantCode.setFilters(variantArray);
        this.textViewScanArticleText.setVisibility(View.GONE);
    }

    @Override
    public void mSetListeners() {
        this.mSetFindArticleListener();
        this.mSetCancelListener();
        this.mSetEditorActionListener();
        this.mSetItemNoEditorActionListener();
        this.mSetVariantcodeEditorActionListener();
        this.mSetStockOwnerSpinnerListener();
    }

    private void mSetCancelListener() {
        this.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cAppExtension.dialogFragment.dismiss();
            }
        });
    }

    private void mSetFindArticleListener() {
        this.scanArticleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (editTextItemNo.getText().toString().trim().isEmpty() && !editTextVariantCode.getText().toString().trim().isEmpty() && editTextScanArticle.getText().toString().trim().isEmpty()) {
                    cUserInterface.pDoNope(editTextItemNo, true, true);
                    return;
                }
                if (editTextItemNo.getText().toString().trim().isEmpty() && editTextVariantCode.getText().toString().trim().isEmpty() && editTextScanArticle.getText().toString().trim().isEmpty()) {
                    cUserInterface.pDoNope(editTextScanArticle, true, true);
                    return;
                }

                cAppExtension.dialogFragment.dismiss();

                if (cAppExtension.activity instanceof MenuActivity) {
                    MenuActivity menuActivity = (MenuActivity)cAppExtension.activity;
                    if (!editTextScanArticle.getText().toString().trim().isEmpty()){
                        menuActivity.pHandleHandleArticleScanned( cBarcodeScan.pFakeScan(editTextScanArticle.getText().toString()));
                    }else {
                        menuActivity.pHandleArticleSearch(editTextItemNo.getText().toString(), editTextVariantCode.getText().toString());
                    }
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
    private void mSetItemNoEditorActionListener() {
        this.editTextItemNo.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE || i == EditorInfo.IME_ACTION_GO ) {
                    editTextVariantCode.callOnClick();
                }
                return true;
            }
        });
    }
    private void mSetVariantcodeEditorActionListener() {
        this.editTextVariantCode.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE || i == EditorInfo.IME_ACTION_GO ) {
                    scanArticleButton.callOnClick();
                }
                return true;
            }
        });
    }
    private void mShowStockOwnerSpinner() {

        if (cStockOwner.allStockOwnerObl  == null || cStockOwner.allStockOwnerObl.size() == 0) {
            this.stockownerSpinner.setVisibility(View.GONE);
            return;
        }
        this.stockownerSpinner.setVisibility(View.VISIBLE);
        this.mFillStockOwnerSpinner();
    }
    private void mFillStockOwnerSpinner() {

        if (cStockOwner.allStockOwnerObl == null ||  cStockOwner.allStockOwnerObl.size() <= 0 ) {
            return;
        }

        List<String> stockOwnerObl = new ArrayList<>();

        if (cUser.currentUser.currentBranch.stockOwnerObl().size() >= 1) {
            for (cStockOwner stockOwner :cUser.currentUser.currentBranch.stockOwnerObl() ) {
                stockOwnerObl.add(stockOwner.getDescriptionStr());
            }
        }
        else
        {
            for (cStockOwner stockOwner :cStockOwner.allStockOwnerObl ) {
                stockOwnerObl.add(stockOwner.getDescriptionStr());
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(cAppExtension.context,
                android.R.layout.simple_spinner_dropdown_item,
                stockOwnerObl);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        this.stockownerSpinner.setAdapter(adapter);
        if (cUser.currentUser.currentStockOwner != null)
        { this.stockownerSpinner.setSelection(adapter.getPosition(cUser.currentUser.currentStockOwner.getDescriptionStr()));}
    }
    private void mSetStockOwnerSpinnerListener() {

        this.stockownerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                cUser.currentUser.currentStockOwner = cStockOwner.pGetStockOwnerByDescriptionStr(stockownerSpinner.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
    }
}

