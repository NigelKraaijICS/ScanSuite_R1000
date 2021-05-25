package nl.icsvertex.scansuite.Fragments.Dialogs;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.Objects;

import ICS.Interfaces.iICSDefaultFragment;
import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cRegex;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Basics.Article.cArticle;
import SSU_WHS.Basics.ArticleProperty.cArticleProperty;
import SSU_WHS.Basics.ArticlePropertyValue.cArticlePropertyValue;
import SSU_WHS.Basics.ArticlePropertyValue.cArticlePropertyValueAdapter;
import SSU_WHS.Basics.ArticleStock.cArticleStock;
import SSU_WHS.LineItemProperty.LineProperty.cLineProperty;
import SSU_WHS.Move.MoveorderLines.cMoveorderLine;
import SSU_WHS.Move.Moveorders.cMoveorder;
import nl.icsvertex.scansuite.Activities.Move.MoveLineItemPropertyActivity;
import nl.icsvertex.scansuite.Activities.Move.MoveLineTakeActivity;
import nl.icsvertex.scansuite.R;

public class ItemPropertyStockFragment extends DialogFragment implements iICSDefaultFragment {

    //Region Public Properties


    //End Region Public Properties

    //Region Private Properties
    private RecyclerView fillItemPropertyRecyclerview;
    private Button buttonCloseProp;
    private Button buttonOkProp;
    private ShimmerFrameLayout shimmerViewPropertyContainer;
    private ProgressBar progressBarProperty;
    private final cArticle currentArticle;
    private ArrayList<cArticlePropertyValue> articlePropertyValueObl;
    private final String valueStr;
    private final String workFlowStr;

    private cArticlePropertyValueAdapter articlePropertyValueAdapter;
    private cArticlePropertyValueAdapter getArticlePropertyValueAdapter(){
        if(this.articlePropertyValueAdapter == null){
            this.articlePropertyValueAdapter = new cArticlePropertyValueAdapter();
        }
        return  articlePropertyValueAdapter;
    }

    //End Region Private Properties


    //Region Constructor
    public ItemPropertyStockFragment(cArticle pvArticle, String pvWorkflowStr) {
        this.currentArticle = pvArticle;
        this.valueStr = null;
        this.workFlowStr = pvWorkflowStr;
    }
    public ItemPropertyStockFragment(cArticle pvArticle, String pvWorkflowStr, String pvValueStr){
        this.currentArticle = pvArticle;
        this.valueStr = pvValueStr;
        this.workFlowStr = pvWorkflowStr;
    }
    //End Region Constructor


    //Region Default Methods
    @Nullable
    @Override
    public View onCreateView(LayoutInflater pvInflater, ViewGroup pvContainer, Bundle pvSavedInstanceState) {
        View rootview = pvInflater.inflate(R.layout.fragment_fill_item_property, pvContainer);
        cBarcodeScan.pRegisterBarcodeFragmentReceiver(this.getClass().getSimpleName());

        return rootview;
    }

    @Override
    public void onViewCreated(@NonNull View pvView, @Nullable Bundle pvSavedInstanceState) {
        this.mFragmentInitialize();
        cAppExtension.dialogFragment = this;
    }

    @Override
    public void onPause() {
        try {
            this.shimmerViewPropertyContainer.stopShimmerAnimation();
            cBarcodeScan.pUnregisterBarcodeFragmentReceiver(this.getClass().getSimpleName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        super.onPause();
    }

    @Override
    public void onDestroy() {
        try {
            cBarcodeScan.pUnregisterBarcodeFragmentReceiver(this.getClass().getSimpleName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        cBarcodeScan.pRegisterBarcodeFragmentReceiver(this.getClass().getSimpleName());

        this.shimmerViewPropertyContainer.startShimmerAnimation();
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels - getResources().getDimensionPixelSize(R.dimen.default_double_margin);

        Objects.requireNonNull(Objects.requireNonNull(getDialog()).getWindow()).setLayout(width, height);
    }

    //End Region Default Methods

    //Region iICSDefaultFragment methods
    @Override
    public void mFragmentInitialize() {
        this.mFindViews();
        this.mFieldsInitialize();
        this.mSetListeners();
        this.mGetSupplyInstantly();
    }

    @Override
    public void mFindViews() {

        if (getView() != null) {
            this.fillItemPropertyRecyclerview = getView().findViewById(R.id.fillItemPropertyRecyclerview);
            this.buttonCloseProp = getView().findViewById(R.id.buttonCloseProp);
            this.buttonOkProp = getView().findViewById(R.id.buttonOkSelectProp);
            this.shimmerViewPropertyContainer = getView().findViewById(R.id.shimmerViewPropertyContainer);
            this.progressBarProperty = getView().findViewById(R.id.progressBarProperty);
        }

    }

    @Override
    public void mFieldsInitialize() {
        this.progressBarProperty.setVisibility(View.INVISIBLE);
        this.mGetData();
    }

    @Override
    public void mSetListeners() {
        this.mSetCloseListener();
        this.mSetOkListener();
    }

    //End Region iICSDefaultFragment methods

    //Region Private Methods

    public  void pHandleScan(cBarcodeScan pvBarcodeScan) {
        String barcodeWithoutPrefixStr ;
        barcodeWithoutPrefixStr = cRegex.pStripRegexPrefixStr(pvBarcodeScan.getBarcodeOriginalStr());

        if (cArticlePropertyValue.currentArticlePropertyValue != null){
            //Check regex for the property
            if (!cRegex.pCheckRegexBln( cArticlePropertyValue.currentArticlePropertyValue.getItemProperty().getLayoutStr(),barcodeWithoutPrefixStr)) {
                cUserInterface.pShowSnackbarMessage(this.buttonOkProp,cAppExtension.activity.getString(R.string.message_unknown_barcode_for_this_line),R.raw.badsound, true);
                return;
            }
            for (cArticlePropertyValue articlePropertyValue: this.articlePropertyValueObl){
                if (articlePropertyValue.getPropertyCodeStr().equalsIgnoreCase(cArticlePropertyValue.currentArticlePropertyValue.getPropertyCodeStr())){
                    articlePropertyValue.valueStr = barcodeWithoutPrefixStr;
                    this.mSetPropertyRecycler();
                    return;
                }
            }

        }else {
            //Check all properties if regex checks out, first found is filled
            for (cArticlePropertyValue articlePropertyValue : this.articlePropertyValueObl) {
                if(cRegex.pCheckRegexBln(articlePropertyValue.getItemProperty().getLayoutStr(), barcodeWithoutPrefixStr)){
                    articlePropertyValue.valueStr = barcodeWithoutPrefixStr;
                    this.mSetPropertyRecycler();
                    return;
                }

            }
            cUserInterface.pShowSnackbarMessage(this.buttonOkProp,cAppExtension.activity.getString(R.string.message_unknown_value),R.raw.badsound, true);
        }
    }

    private void mGetData() {
       this.articlePropertyValueObl = new ArrayList<>();

        for (cArticleProperty articleProperty: this.currentArticle.propertyObl){
            if (articleProperty.InputWorkflowObl().contains(this.workFlowStr) ||
                    articleProperty.RequiredWorkflowObl().contains(this.workFlowStr)) {
                cArticlePropertyValue articlePropertyValue = new cArticlePropertyValue(articleProperty);
                if (this.valueStr != null) {
                    if (articleProperty.getPropertyCodeStr().equalsIgnoreCase(cLineProperty.currentLineProperty.getPropertyCodeStr())) {
                        articlePropertyValue.valueStr = this.valueStr;
                    }
                }

                this.articlePropertyValueObl.add(articlePropertyValue);
            }

        }
        mSetPropertyRecycler();
        cArticlePropertyValue.currentArticlePropertyValue = null;
    }

    private void mSetPropertyRecycler() {

        this.getArticlePropertyValueAdapter().pFillData(this.articlePropertyValueObl);
        this.fillItemPropertyRecyclerview.setHasFixedSize(false);
        this.fillItemPropertyRecyclerview.setAdapter(this.getArticlePropertyValueAdapter());
        this.fillItemPropertyRecyclerview.setLayoutManager(new LinearLayoutManager(cAppExtension.context));
        //Stopping Shimmer Effect's animation after data is loaded
        this.shimmerViewPropertyContainer.stopShimmerAnimation();
        this.shimmerViewPropertyContainer.setVisibility(View.GONE);
    }

    private void mGetSupplyInstantly(){
        if (this.valueStr == null || this.articlePropertyValueObl.size() > 1){
            return;
        }
        this.buttonOkProp.performClick();
    }

    private void mSetCloseListener() {
        this.buttonCloseProp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCloseAndReturn();
            }
        });
    }

    private void mSetOkListener() {
        this.buttonOkProp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBarProperty.setVisibility(View.VISIBLE);
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        mHandleOk();
                    }
                };
                Thread handleOk = new Thread(runnable);
                handleOk.start();
            }
        });
    }

    private void mHandleOk(){
        cArticleStock articleStock = currentArticle.pGetPropertyStockForBINViaWebservice(articlePropertyValueObl, cMoveorder.currentMoveOrder.currentBranchBin.getBinCodeStr());
        if (articleStock != null){
            // Handle in the properties screen
            if (!(cAppExtension.activity instanceof  MoveLineItemPropertyActivity)){
                cMoveorderLine.currentMoveOrderLine = new cMoveorderLine(articleStock);
                for (cArticlePropertyValue articlePropertyValue: articlePropertyValueObl) {
                    cLineProperty lineProperty = new cLineProperty(articlePropertyValue.getArticleProperty(),
                            cMoveorderLine.currentMoveOrderLine.getLineNoInt(),
                            articlePropertyValue.getArticleProperty().InputWorkflowObl().contains(cMoveorder.currentMoveOrder.getOrderTypeStr()),
                            articlePropertyValue.getArticleProperty().RequiredWorkflowObl().contains(cMoveorder.currentMoveOrder.getOrderTypeStr()));

                    if (cLineProperty.allLinePropertysObl == null) {
                        cLineProperty.allLinePropertysObl = new ArrayList<>();
                    }
                    boolean foundBln = false;
                    for (cLineProperty loopProperty : cLineProperty.allLinePropertysObl){
                        if(loopProperty.getPropertyCodeStr().equalsIgnoreCase(lineProperty.getPropertyCodeStr())&& loopProperty.getLineNoInt().equals(lineProperty.getLineNoInt())){
                            foundBln = true;
                           break;
                        }
                    }
                    if (!foundBln){ cLineProperty.allLinePropertysObl.add(lineProperty);}
                }
            } else {
                cMoveorderLine.currentMoveOrderLine.quantityDbl += articleStock.getQuantityDbl();
            }

            for (cArticlePropertyValue articlePropertyValue: articlePropertyValueObl){
                if(articlePropertyValue.getArticleProperty().InputWorkflowObl().contains(cMoveorder.currentMoveOrder.getOrderTypeStr())){
                    cMoveorderLine.currentMoveOrderLine.pAddPropertyValue(articlePropertyValue, articleStock.getQuantityDbl());
                }
            }

            if (cAppExtension.activity instanceof MoveLineTakeActivity){
                MoveLineItemPropertyActivity.currentModus = MoveLineItemPropertyActivity.modusEnu.GENERATEDTAKE;
            }

            if (!(cAppExtension.activity instanceof  MoveLineItemPropertyActivity)){
                Intent intent = new Intent(cAppExtension.context, MoveLineItemPropertyActivity.class);
                cAppExtension.activity.startActivity(intent);
                cAppExtension.activity.finish();
            } else {
                    MoveLineItemPropertyActivity moveLineItemPropertyActivity = (MoveLineItemPropertyActivity) cAppExtension.activity;
                    moveLineItemPropertyActivity.pTryToChangeQuantity(true, false, cMoveorder.currentMoveOrder.currentMoveorderBarcode.getQuantityPerUnitOfMeasureDbl());
                    moveLineItemPropertyActivity.pRefreshActivity();
            }
            cAppExtension.dialogFragment.dismiss();

        }
        else{
            cUserInterface.pDoNope(buttonOkProp, true, true);
            cUserInterface.pShowSnackbarMessage(buttonOkProp, cAppExtension.activity.getString(R.string.message_no_stock_available),R.raw.headsupsound,false);
            this.progressBarProperty.setVisibility(View.INVISIBLE);
        }
    }
    private void mCloseAndReturn(){
        if (cAppExtension.activity instanceof MoveLineTakeActivity){
            MoveLineTakeActivity moveLineTakeActivity = (MoveLineTakeActivity) cAppExtension.activity;
            moveLineTakeActivity.pCancelMove();
            dismiss();
        }
    }

    //End Region Private Methods




}
