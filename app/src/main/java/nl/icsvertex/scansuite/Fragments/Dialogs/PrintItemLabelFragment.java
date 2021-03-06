package nl.icsvertex.scansuite.Fragments.Dialogs;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ICS.Interfaces.iICSDefaultFragment;
import ICS.Utils.cRegex;
import ICS.Utils.cText;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Basics.Article.cArticle;
import SSU_WHS.Basics.ArticleBarcode.cArticleBarcode;
import SSU_WHS.Basics.BarcodeLayouts.cBarcodeLayout;
import SSU_WHS.Basics.LabelTemplate.cLabelTemplate;
import SSU_WHS.Basics.PrintItemLabel.cPrintItemLabel;
import SSU_WHS.Basics.Users.cUser;
import SSU_WHS.Basics.Workplaces.cWorkplace;
import SSU_WHS.General.cPublicDefinitions;
import SSU_WHS.Intake.IntakeorderBarcodes.cIntakeorderBarcode;
import SSU_WHS.Intake.IntakeorderMATLineSummary.cIntakeorderMATSummaryLine;
import SSU_WHS.Intake.IntakeorderMATLines.cIntakeorderMATLine;
import SSU_WHS.Intake.Intakeorders.cIntakeorder;
import SSU_WHS.Inventory.InventoryOrders.cInventoryorder;
import SSU_WHS.Inventory.InventoryorderBarcodes.cInventoryorderBarcode;
import SSU_WHS.Inventory.InventoryorderLines.cInventoryorderLine;
import SSU_WHS.Move.MoveorderBarcodes.cMoveorderBarcode;
import SSU_WHS.Move.MoveorderLines.cMoveorderLine;
import SSU_WHS.Move.Moveorders.cMoveorder;
import SSU_WHS.Picken.PickorderBarcodes.cPickorderBarcode;
import SSU_WHS.Picken.PickorderLines.cPickorderLine;
import SSU_WHS.Picken.Pickorders.cPickorder;
import SSU_WHS.Receive.ReceiveLines.cReceiveorderLine;
import SSU_WHS.Receive.ReceiveSummaryLine.cReceiveorderSummaryLine;
import SSU_WHS.Return.ReturnOrder.cReturnorder;
import SSU_WHS.Return.ReturnorderBarcode.cReturnorderBarcode;
import SSU_WHS.Return.ReturnorderLine.cReturnorderLine;
import nl.icsvertex.scansuite.Activities.General.MenuActivity;
import nl.icsvertex.scansuite.Activities.Intake.IntakeOrderIntakeActivity;
import nl.icsvertex.scansuite.Activities.Intake.IntakeOrderIntakeGeneratedActivity;
import nl.icsvertex.scansuite.Activities.Intake.IntakeorderMASLinesActivity;
import nl.icsvertex.scansuite.Activities.Intake.IntakeorderMATLinesActivity;
import nl.icsvertex.scansuite.Activities.Inventory.InventoryArticleActivity;
import nl.icsvertex.scansuite.Activities.Inventory.InventoryorderBinActivity;
import nl.icsvertex.scansuite.Activities.Move.MoveLinePlaceActivity;
import nl.icsvertex.scansuite.Activities.Move.MoveLinePlaceGeneratedActivity;
import nl.icsvertex.scansuite.Activities.Move.MoveLinePlaceMTActivity;
import nl.icsvertex.scansuite.Activities.Move.MoveLineTakeActivity;
import nl.icsvertex.scansuite.Activities.Move.MoveLineTakeMTActivity;
import nl.icsvertex.scansuite.Activities.Move.MoveLinesActivity;
import nl.icsvertex.scansuite.Activities.Move.MoveLinesPlaceMTActivity;
import nl.icsvertex.scansuite.Activities.Move.MoveLinesTakeMTActivity;
import nl.icsvertex.scansuite.Activities.Move.MoveorderLinesPlaceGeneratedActivity;
import nl.icsvertex.scansuite.Activities.Pick.PickorderLinesActivity;
import nl.icsvertex.scansuite.Activities.Pick.PickorderLinesGeneratedActivity;
import nl.icsvertex.scansuite.Activities.Pick.PickorderPickActivity;
import nl.icsvertex.scansuite.Activities.Pick.PickorderPickGeneratedActivity;
import nl.icsvertex.scansuite.Activities.Receive.ReceiveOrderReceiveActivity;
import nl.icsvertex.scansuite.Activities.Returns.ReturnArticleDetailActivity;
import nl.icsvertex.scansuite.R;

public class PrintItemLabelFragment extends DialogFragment implements iICSDefaultFragment {

    //Region Private Properties

    private ConstraintLayout lineBinPrint;
    private TextView textViewTitle;
//    private ImageView barcodeImage;
    private Spinner barcodeSpinner;
    private Spinner workplaceSpinner;
    private Spinner layoutSpinner;

    private AppCompatImageButton imageButtonNoInputPropertys;
    private TextView articleDescriptionCompactText;
    private TextView articleDescription2CompactText;
    private TextView articleItemCompactText;
    private TextView articleBarcodeCompactText;
    private TextView textViewWorkspaceFail;
    private TextView textViewLayoutFail;
    private ProgressBar progressBar;

    private TextView quantityText;
    private TextView quantityRequiredText;
    private AppCompatImageButton imageButtonMinus;
    private AppCompatImageButton imageButtonPlus;

    private  int counterMinusHelperInt;
    private  int counterPlusHelperInt;
    private Handler minusHandler;
    private  Handler plusHandler;

    private Button printButton;
    private Button cancelButton;
    private Long amountLng;
    private String orderTypeStr;
    private String orderNumberStr;
    private Long lineNoLng;
    private String barcodeStr;
    private Boolean unknownLineBln;
    private Boolean unknownArticleBln;
    private String stockOwnerStr;
    private ArrayList<String> barcodeObl;

    //End Region private Properties

    //Region Constructor
    public PrintItemLabelFragment() {
    }
    //End Region Constructor

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_print_item_label, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        cAppExtension.dialogFragment = this;
        this.mFragmentInitialize();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = WindowManager.LayoutParams.WRAP_CONTENT;
        Objects.requireNonNull(Objects.requireNonNull(getDialog()).getWindow()).setLayout(width, height);
    }

    public void onBackPressed(){
        cAppExtension.dialogFragment.dismiss();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem pvMenuItem) {
        return super.onOptionsItemSelected(pvMenuItem);
    }

    @Override
    public void mFragmentInitialize() {

        this.mFindViews();

        this.mFieldsInitialize();

        this.mSetToolbar();

        this.mSetListeners();

    }


    @Override
    public void mFindViews() {

        this.textViewTitle = getView().findViewById(R.id.textViewTitle);

        this.imageButtonNoInputPropertys = getView().findViewById(R.id.imageButtonNoInputPropertys);
        this.articleDescriptionCompactText = getView().findViewById(R.id.articleDescriptionCompactText);
        this.articleDescription2CompactText = getView().findViewById(R.id.articleDescription2CompactText);
        this.articleItemCompactText = getView().findViewById(R.id.articleItemCompactText);
        this.articleBarcodeCompactText = getView().findViewById(R.id.articleBarcodeCompactText);
        this.lineBinPrint = getView().findViewById(R.id.lineBinPrint);
        this.workplaceSpinner = getView().findViewById(R.id.workspaceSpinner);
        this.barcodeSpinner = getView().findViewById(R.id.barcodeSpinner);
        this.layoutSpinner = getView().findViewById(R.id.layoutSpinner);
        this.printButton = getView().findViewById(R.id.printButton);
        this.cancelButton = getView().findViewById(R.id.cancelButton);
        this.quantityText = getView().findViewById(R.id.quantityText);
        this.quantityRequiredText = getView().findViewById(R.id.quantityRequiredText);
//        this.barcodeImage = getView().findViewById(R.id.barcodeImageView);
        this.imageButtonMinus = getView().findViewById(R.id.imageButtonMinus);
        this.imageButtonPlus = getView().findViewById(R.id.imageButtonPlus);
        this.progressBar = getView().findViewById(R.id.progressBar);
        this.textViewWorkspaceFail = getView().findViewById(R.id.textViewWorkspaceFail);
        this.textViewLayoutFail = getView().findViewById(R.id.textViewLayoutFail);
    }


    public void mSetToolbar() {
        this.textViewTitle.setText(this.barcodeStr);
        this.textViewTitle.setSelected(true);
    }

    @Override
    public void mFieldsInitialize() {
        this.unknownLineBln = false;
        this.unknownArticleBln = false;

        if (cAppExtension.activity instanceof PickorderPickActivity) {
            if (cPickorderBarcode.currentPickorderBarcode !=null){
                this.barcodeStr = cPickorderBarcode.currentPickorderBarcode.getBarcodeStr();
            }else{
                if(cArticle.currentArticle != null && cArticle.currentArticle.barcodesObl.size() > 0){
                    this.barcodeStr = cArticle.currentArticle.barcodesObl.get(0).getBarcodeStr();
                    if (cArticle.currentArticle.barcodesObl.size() > 1){
                        this.barcodeObl = new ArrayList<>();
                        for (cArticleBarcode barcode :  cArticle.currentArticle.barcodesObl){
                            this.barcodeObl.add(barcode.getBarcodeStr() + "( " + barcode.quantityPerUnitOfMeasureDbl + " )");
                        }
                    }
                }
            }
            this.orderNumberStr = cPickorder.currentPickOrder.getOrderNumberStr();
            this.orderTypeStr = "PICKEN";
            this.stockOwnerStr = cPickorder.currentPickOrder.getStockownerStr();
            if (cPickorderLine.currentPickOrderLine == null){
                this.unknownLineBln = true;
            }
            else {this.lineNoLng = cText.pIntegerToLongLng(cPickorderLine.currentPickOrderLine.getLineNoInt());}
        }
        if (cAppExtension.activity instanceof PickorderPickGeneratedActivity) {
            if (cPickorderBarcode.currentPickorderBarcode !=null){
                this.barcodeStr = cPickorderBarcode.currentPickorderBarcode.getBarcodeStr();
            }else{
                if(cArticle.currentArticle != null && cArticle.currentArticle.barcodesObl.size() > 0){
                    this.barcodeStr = cArticle.currentArticle.barcodesObl.get(0).getBarcodeStr();
                    if (cArticle.currentArticle.barcodesObl.size() > 1){
                        this.barcodeObl = new ArrayList<>();
                        for (cArticleBarcode barcode :  cArticle.currentArticle.barcodesObl){
                            this.barcodeObl.add(barcode.getBarcodeStr() + "( " + barcode.quantityPerUnitOfMeasureDbl + " )");
                        }
                    }
                }
            }
            this.orderNumberStr = cPickorder.currentPickOrder.getOrderNumberStr();
            this.orderTypeStr = "PICKEN";
            this.stockOwnerStr = cPickorder.currentPickOrder.getStockownerStr();
            if (cPickorderLine.currentPickOrderLine == null){
                this.unknownLineBln = true;
            }
            else {this.lineNoLng = cText.pIntegerToLongLng(cPickorderLine.currentPickOrderLine.getLineNoInt());}
        }
        if (cAppExtension.activity instanceof PickorderLinesActivity && cPickorderLine.currentPickOrderLine != null) {
            if (cPickorderLine.currentPickOrderLine.barcodesObl != null && cPickorderLine.currentPickOrderLine.barcodesObl.size() > 0){
                this.barcodeStr = cPickorderLine.currentPickOrderLine.barcodesObl.get(0).getBarcodeStr();
                if (cPickorderLine.currentPickOrderLine.barcodesObl.size() > 1){
                    this.barcodeObl = new ArrayList<>();
                    for (cPickorderBarcode barcode :  cPickorderLine.currentPickOrderLine.barcodesObl){
                        this.barcodeObl.add(barcode.getBarcodeAndQuantityStr());
                    }
                }

            }else{
                if(cArticle.currentArticle != null && cArticle.currentArticle.barcodesObl.size() > 0){
                    this.barcodeStr = cArticle.currentArticle.barcodesObl.get(0).getBarcodeStr();
                    if (cArticle.currentArticle.barcodesObl.size() > 1){
                        this.barcodeObl = new ArrayList<>();
                        for (cArticleBarcode barcode :  cArticle.currentArticle.barcodesObl){
                            this.barcodeObl.add(barcode.getBarcodeStr() + "( " + barcode.quantityPerUnitOfMeasureDbl + " )");
                        }
                    }
                }
            }
            this.orderNumberStr = cPickorder.currentPickOrder.getOrderNumberStr();
            this.orderTypeStr = "PICKEN";
            this.stockOwnerStr = cPickorder.currentPickOrder.getStockownerStr();
            this.lineNoLng = cText.pIntegerToLongLng(cPickorderLine.currentPickOrderLine.getLineNoInt());
        }
        if (cAppExtension.activity instanceof PickorderLinesGeneratedActivity && cPickorderLine.currentPickOrderLine != null) {
            if (cPickorderLine.currentPickOrderLine.barcodesObl != null && cPickorderLine.currentPickOrderLine.barcodesObl.size() > 0){
                this.barcodeStr = cPickorderLine.currentPickOrderLine.barcodesObl.get(0).getBarcodeStr();
                if (cPickorderLine.currentPickOrderLine.barcodesObl.size() > 1){
                    this.barcodeObl = new ArrayList<>();
                    for (cPickorderBarcode barcode :  cPickorderLine.currentPickOrderLine.barcodesObl){
                        this.barcodeObl.add(barcode.getBarcodeAndQuantityStr());
                    }
                }
            }else{
                if(cArticle.currentArticle != null && cArticle.currentArticle.barcodesObl.size() > 0){
                    this.barcodeStr = cArticle.currentArticle.barcodesObl.get(0).getBarcodeStr();
                    if (cArticle.currentArticle.barcodesObl.size() > 1){
                        this.barcodeObl = new ArrayList<>();
                        for (cArticleBarcode barcode :  cArticle.currentArticle.barcodesObl){
                            this.barcodeObl.add(barcode.getBarcodeStr() + "( " + barcode.quantityPerUnitOfMeasureDbl + " )");
                        }
                    }
                }
            }
            this.orderNumberStr = cPickorder.currentPickOrder.getOrderNumberStr();
            this.orderTypeStr = "PICKEN";
            this.stockOwnerStr = cPickorder.currentPickOrder.getStockownerStr();
            this.lineNoLng = cText.pIntegerToLongLng(cPickorderLine.currentPickOrderLine.getLineNoInt());
        }
        if (cAppExtension.activity instanceof InventoryArticleActivity) {
            if (cInventoryorderBarcode.currentInventoryOrderBarcode != null) {
                this.barcodeStr = cInventoryorderBarcode.currentInventoryOrderBarcode.getBarcodeStr();
            }
            else {
                if(cArticle.currentArticle != null && cArticle.currentArticle.barcodesObl.size() > 0){
                    this.barcodeStr = cArticle.currentArticle.barcodesObl.get(0).getBarcodeStr();
                    if (cArticle.currentArticle.barcodesObl.size() > 1){
                        this.barcodeObl = new ArrayList<>();
                        for (cArticleBarcode barcode :  cArticle.currentArticle.barcodesObl){
                            this.barcodeObl.add(barcode.getBarcodeStr() + "( " + barcode.quantityPerUnitOfMeasureDbl + " )");
                        }
                    }
                }
            }
            this.orderNumberStr = cInventoryorder.currentInventoryOrder.getOrderNumberStr();
            this.orderTypeStr = "INVENTARISATIE";
            this.stockOwnerStr = cInventoryorder.currentInventoryOrder.getStockownerStr();
            if (cInventoryorderLine.currentInventoryOrderLine == null){
                this.unknownLineBln = true;
            }
            else {this.lineNoLng = cText.pIntegerToLongLng(cInventoryorderLine.currentInventoryOrderLine.getLineNoInt());
                    this.unknownArticleBln = true;
                    cArticle.currentArticle = new cArticle(cInventoryorderLine.currentInventoryOrderLine.getItemNoStr(), cInventoryorderLine.currentInventoryOrderLine.getVariantCodeStr());
                    this.articleDescriptionCompactText.setText(cInventoryorderLine.currentInventoryOrderLine.getDescriptionStr());
                    this.articleDescription2CompactText.setText(cInventoryorderLine.currentInventoryOrderLine.getDescription2Str());}
        }
        if (cAppExtension.activity instanceof InventoryorderBinActivity && cInventoryorderLine.currentInventoryOrderLine !=  null) {
            if (cInventoryorderLine.currentInventoryOrderLine.barcodesObl().size() > 0 ) {
                this.barcodeStr = cInventoryorderLine.currentInventoryOrderLine.barcodesObl().get(0).getBarcodeStr();
                if (cInventoryorderLine.currentInventoryOrderLine.barcodesObl().size() > 1){
                    this.barcodeObl = new ArrayList<>();
                    for (cInventoryorderBarcode barcode :  cInventoryorderLine.currentInventoryOrderLine.barcodesObl()){
                        this.barcodeObl.add(barcode.getBarcodeAndQuantityStr());
                    }
                }
            }
            this.orderNumberStr = cInventoryorder.currentInventoryOrder.getOrderNumberStr();
            this.orderTypeStr = "INVENTARISATIE";
            this.stockOwnerStr = cInventoryorder.currentInventoryOrder.getStockownerStr();
            this.lineNoLng = cText.pIntegerToLongLng(cInventoryorderLine.currentInventoryOrderLine.getLineNoInt());
            this.unknownArticleBln = true;
            cArticle.currentArticle = new cArticle(cInventoryorderLine.currentInventoryOrderLine.getItemNoStr(), cInventoryorderLine.currentInventoryOrderLine.getVariantCodeStr());
            this.articleDescriptionCompactText.setText(cInventoryorderLine.currentInventoryOrderLine.getDescriptionStr());
            this.articleDescription2CompactText.setText(cInventoryorderLine.currentInventoryOrderLine.getDescription2Str());
        }
        if (cAppExtension.activity instanceof MoveLinePlaceActivity) {
            if ( cMoveorder.currentMoveOrder.currentMoveorderBarcode != null) {
                this.barcodeStr =  cMoveorder.currentMoveOrder.currentMoveorderBarcode.getBarcodeStr();
            }
            else{
                if(cArticle.currentArticle != null && cArticle.currentArticle.barcodesObl.size() > 0){
                    this.barcodeStr = cArticle.currentArticle.barcodesObl.get(0).getBarcodeStr();
                    if (cArticle.currentArticle.barcodesObl.size() > 1){
                        this.barcodeObl = new ArrayList<>();
                        for (cArticleBarcode barcode :  cArticle.currentArticle.barcodesObl){
                            this.barcodeObl.add(barcode.getBarcodeStr() + "( " + barcode.quantityPerUnitOfMeasureDbl + " )");
                        }
                    }
                }
            }
            this.orderNumberStr = cMoveorder.currentMoveOrder.getOrderNumberStr();
            this.orderTypeStr = "VERPLAATS";
            this.stockOwnerStr = cMoveorder.currentMoveOrder.getStockownerStr();
            if (cMoveorderLine.currentMoveOrderLine == null){
                this.unknownLineBln = true;
            }
            else {this.lineNoLng = cText.pIntegerToLongLng(cMoveorderLine.currentMoveOrderLine.getLineNoInt());}
        }
        if (cAppExtension.activity instanceof MoveLinePlaceGeneratedActivity) {
            if ( cMoveorder.currentMoveOrder.currentMoveorderBarcode != null) {
                this.barcodeStr =  cMoveorder.currentMoveOrder.currentMoveorderBarcode.getBarcodeStr();
            }
            else{
                if(cArticle.currentArticle != null && cArticle.currentArticle.barcodesObl.size() > 0){
                    this.barcodeStr = cArticle.currentArticle.barcodesObl.get(0).getBarcodeStr();
                    if (cArticle.currentArticle.barcodesObl.size() > 1){
                        this.barcodeObl = new ArrayList<>();
                        for (cArticleBarcode barcode :  cArticle.currentArticle.barcodesObl){
                            this.barcodeObl.add(barcode.getBarcodeStr() + "( " + barcode.quantityPerUnitOfMeasureDbl + " )");
                        }
                    }
                }
            }
            this.orderNumberStr = cMoveorder.currentMoveOrder.getOrderNumberStr();
            this.orderTypeStr = "VERPLAATS";
            this.stockOwnerStr = cMoveorder.currentMoveOrder.getStockownerStr();
            if (cMoveorderLine.currentMoveOrderLine == null){
                this.unknownLineBln = true;
            }
            else {this.lineNoLng = cText.pIntegerToLongLng(cMoveorderLine.currentMoveOrderLine.getLineNoInt());}
        }
        if (cAppExtension.activity instanceof MoveLineTakeActivity) {
            if ( cMoveorder.currentMoveOrder.currentMoveorderBarcode != null) {
                this.barcodeStr =  cMoveorder.currentMoveOrder.currentMoveorderBarcode.getBarcodeStr();
            }
            else{
                if(cArticle.currentArticle != null && cArticle.currentArticle.barcodesObl.size() > 0){
                    this.barcodeStr = cArticle.currentArticle.barcodesObl.get(0).getBarcodeStr();
                    if (cArticle.currentArticle.barcodesObl.size() > 1){
                        this.barcodeObl = new ArrayList<>();
                        for (cArticleBarcode barcode :  cArticle.currentArticle.barcodesObl){
                            this.barcodeObl.add(barcode.getBarcodeStr() + "( " + barcode.quantityPerUnitOfMeasureDbl + " )");
                        }
                    }
                }
            }
            this.orderNumberStr = cMoveorder.currentMoveOrder.getOrderNumberStr();
            this.orderTypeStr = "VERPLAATS";
            this.stockOwnerStr = cMoveorder.currentMoveOrder.getStockownerStr();
            if (cMoveorderLine.currentMoveOrderLine == null){
                this.unknownLineBln = true;
            }
            else {this.lineNoLng = cText.pIntegerToLongLng(cMoveorderLine.currentMoveOrderLine.getLineNoInt());}
        }
        if (cAppExtension.activity instanceof MoveLinePlaceMTActivity) {
            if ( cMoveorder.currentMoveOrder.currentMoveorderBarcode != null) {
                this.barcodeStr =  cMoveorder.currentMoveOrder.currentMoveorderBarcode.getBarcodeStr();
            }
            else{
                if(cArticle.currentArticle != null && cArticle.currentArticle.barcodesObl.size() > 0){
                    this.barcodeStr = cArticle.currentArticle.barcodesObl.get(0).getBarcodeStr();
                    if (cArticle.currentArticle.barcodesObl.size() > 1){
                        this.barcodeObl = new ArrayList<>();
                        for (cArticleBarcode barcode :  cArticle.currentArticle.barcodesObl){
                            this.barcodeObl.add(barcode.getBarcodeStr() + "( " + barcode.quantityPerUnitOfMeasureDbl + " )");
                        }
                    }
                }
            }
            this.orderNumberStr = cMoveorder.currentMoveOrder.getOrderNumberStr();
            this.orderTypeStr = "VERPLAATS";
            this.stockOwnerStr = cMoveorder.currentMoveOrder.getStockownerStr();
            if (cMoveorderLine.currentMoveOrderLine == null){
                this.unknownLineBln = true;
            }
            else {this.lineNoLng = cText.pIntegerToLongLng(cMoveorderLine.currentMoveOrderLine.getLineNoInt());}
        }
        if (cAppExtension.activity instanceof MoveLineTakeMTActivity) {
            if ( cMoveorder.currentMoveOrder.currentMoveorderBarcode != null) {
                this.barcodeStr =  cMoveorder.currentMoveOrder.currentMoveorderBarcode.getBarcodeStr();
            }
            else{
                if(cArticle.currentArticle != null && cArticle.currentArticle.barcodesObl.size() > 0){
                    this.barcodeStr = cArticle.currentArticle.barcodesObl.get(0).getBarcodeStr();
                    if (cArticle.currentArticle.barcodesObl.size() > 1){
                        this.barcodeObl = new ArrayList<>();
                        for (cArticleBarcode barcode :  cArticle.currentArticle.barcodesObl){
                            this.barcodeObl.add(barcode.getBarcodeStr() + "( " + barcode.quantityPerUnitOfMeasureDbl + " )");
                        }
                    }
                }
            }
            this.orderNumberStr = cMoveorder.currentMoveOrder.getOrderNumberStr();
            this.orderTypeStr = "VERPLAATS";
            this.stockOwnerStr = cMoveorder.currentMoveOrder.getStockownerStr();
            if (cMoveorderLine.currentMoveOrderLine == null){
                this.unknownLineBln = true;
            }
            else {this.lineNoLng = cText.pIntegerToLongLng(cMoveorderLine.currentMoveOrderLine.getLineNoInt());}
        }
        if (cAppExtension.activity instanceof MoveLinesActivity && cMoveorderLine.currentMoveOrderLine != null) {
            if ( cMoveorderLine.currentMoveOrderLine.orderBarcodesObl().size()> 0) {
                this.barcodeStr =  cMoveorderLine.currentMoveOrderLine.orderBarcodesObl().get(0).getBarcodeStr();
                if (cMoveorderLine.currentMoveOrderLine.orderBarcodesObl().size() > 1){
                    this.barcodeObl = new ArrayList<>();
                    for (cMoveorderBarcode barcode :  cMoveorderLine.currentMoveOrderLine.orderBarcodesObl()){
                        this.barcodeObl.add(barcode.getBarcodeAndQuantityStr());
                    }
                }
            }
            this.orderNumberStr = cMoveorder.currentMoveOrder.getOrderNumberStr();
            this.orderTypeStr = "VERPLAATS";
            this.stockOwnerStr = cMoveorder.currentMoveOrder.getStockownerStr();
            this.lineNoLng = cText.pIntegerToLongLng(cMoveorderLine.currentMoveOrderLine.getLineNoInt());
            this.unknownArticleBln = true;
            cArticle.currentArticle = new cArticle(cMoveorderLine.currentMoveOrderLine.getItemNoStr(),cMoveorderLine.currentMoveOrderLine.getVariantCodeStr());
            this.articleDescriptionCompactText.setText(cMoveorderLine.currentMoveOrderLine.getDescriptionStr());
            this.articleDescription2CompactText.setText(cMoveorderLine.currentMoveOrderLine.getDescription2Str());
        }
        if (cAppExtension.activity instanceof MoveLinesPlaceMTActivity && cMoveorderLine.currentMoveOrderLine != null) {
            if ( cMoveorderLine.currentMoveOrderLine.orderBarcodesObl().size()> 0) {
                this.barcodeStr =  cMoveorderLine.currentMoveOrderLine.orderBarcodesObl().get(0).getBarcodeStr();
                if (cMoveorderLine.currentMoveOrderLine.orderBarcodesObl().size() > 1){
                    this.barcodeObl = new ArrayList<>();
                    for (cMoveorderBarcode barcode :  cMoveorderLine.currentMoveOrderLine.orderBarcodesObl()){
                        this.barcodeObl.add(barcode.getBarcodeAndQuantityStr());
                    }
                }
            }
            this.orderNumberStr = cMoveorder.currentMoveOrder.getOrderNumberStr();
            this.orderTypeStr = "VERPLAATS";
            this.stockOwnerStr = cMoveorder.currentMoveOrder.getStockownerStr();
            this.lineNoLng = cText.pIntegerToLongLng(cMoveorderLine.currentMoveOrderLine.getLineNoInt());
            this.unknownArticleBln = true;
            cArticle.currentArticle = new cArticle(cMoveorderLine.currentMoveOrderLine.getItemNoStr(),cMoveorderLine.currentMoveOrderLine.getVariantCodeStr());
            this.articleDescriptionCompactText.setText(cMoveorderLine.currentMoveOrderLine.getDescriptionStr());
            this.articleDescription2CompactText.setText(cMoveorderLine.currentMoveOrderLine.getDescription2Str());
        }
        if (cAppExtension.activity instanceof MoveLinesTakeMTActivity && cMoveorderLine.currentMoveOrderLine != null) {
            if ( cMoveorderLine.currentMoveOrderLine.orderBarcodesObl().size()> 0) {
                this.barcodeStr =  cMoveorderLine.currentMoveOrderLine.orderBarcodesObl().get(0).getBarcodeStr();
                if (cMoveorderLine.currentMoveOrderLine.orderBarcodesObl().size() > 1){
                    this.barcodeObl = new ArrayList<>();
                    for (cMoveorderBarcode barcode :  cMoveorderLine.currentMoveOrderLine.orderBarcodesObl()){
                        this.barcodeObl.add(barcode.getBarcodeAndQuantityStr());
                    }
                }
            }
            this.orderNumberStr = cMoveorder.currentMoveOrder.getOrderNumberStr();
            this.orderTypeStr = "VERPLAATS";
            this.stockOwnerStr = cMoveorder.currentMoveOrder.getStockownerStr();
            this.lineNoLng = cText.pIntegerToLongLng(cMoveorderLine.currentMoveOrderLine.getLineNoInt());
            this.unknownArticleBln = true;
            cArticle.currentArticle = new cArticle(cMoveorderLine.currentMoveOrderLine.getItemNoStr(),cMoveorderLine.currentMoveOrderLine.getVariantCodeStr());
            this.articleDescriptionCompactText.setText(cMoveorderLine.currentMoveOrderLine.getDescriptionStr());
            this.articleDescription2CompactText.setText(cMoveorderLine.currentMoveOrderLine.getDescription2Str());
        }
        if (cAppExtension.activity instanceof MoveorderLinesPlaceGeneratedActivity && cMoveorderLine.currentMoveOrderLine != null) {
            if ( cMoveorderLine.currentMoveOrderLine.orderBarcodesObl().size()> 0) {
                this.barcodeStr =  cMoveorderLine.currentMoveOrderLine.orderBarcodesObl().get(0).getBarcodeStr();
                if (cMoveorderLine.currentMoveOrderLine.orderBarcodesObl().size() > 1){
                    this.barcodeObl = new ArrayList<>();
                    for (cMoveorderBarcode barcode :  cMoveorderLine.currentMoveOrderLine.orderBarcodesObl()){
                        this.barcodeObl.add(barcode.getBarcodeAndQuantityStr());
                    }
                }
            }
            this.orderNumberStr = cMoveorder.currentMoveOrder.getOrderNumberStr();
            this.orderTypeStr = "VERPLAATS";
            this.stockOwnerStr = cMoveorder.currentMoveOrder.getStockownerStr();
            this.lineNoLng = cText.pIntegerToLongLng(cMoveorderLine.currentMoveOrderLine.getLineNoInt());
            this.unknownArticleBln = true;
            cArticle.currentArticle = new cArticle(cMoveorderLine.currentMoveOrderLine.getItemNoStr(),cMoveorderLine.currentMoveOrderLine.getVariantCodeStr());
            this.articleDescriptionCompactText.setText(cMoveorderLine.currentMoveOrderLine.getDescriptionStr());
            this.articleDescription2CompactText.setText(cMoveorderLine.currentMoveOrderLine.getDescription2Str());
        }
        if (cAppExtension.activity instanceof IntakeOrderIntakeActivity){
            if (cIntakeorderBarcode.currentIntakeOrderBarcode != null) {
                this.barcodeStr = cIntakeorderBarcode.currentIntakeOrderBarcode.getBarcodeStr();
                this.unknownArticleBln = true;
                cArticle.currentArticle = new cArticle(cIntakeorderBarcode.currentIntakeOrderBarcode.getItemNoStr(), cIntakeorderBarcode.currentIntakeOrderBarcode.getVariantCodeStr());
                this.articleDescriptionCompactText.setText(cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getDescriptionStr());
                this.articleDescription2CompactText.setText(cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getDescription2Str());
            }
            else {
                if(cArticle.currentArticle != null && cArticle.currentArticle.barcodesObl.size() > 0){
                    this.barcodeStr = cArticle.currentArticle.barcodesObl.get(0).getBarcodeStr();
                    if (cArticle.currentArticle.barcodesObl.size() > 1){
                        this.barcodeObl = new ArrayList<>();
                        for (cArticleBarcode barcode :  cArticle.currentArticle.barcodesObl){
                            this.barcodeObl.add(barcode.getBarcodeStr() + "( " + barcode.quantityPerUnitOfMeasureDbl + " )");
                        }
                    }
                }
            }
            this.orderNumberStr = cIntakeorder.currentIntakeOrder.getOrderNumberStr();
            this.orderTypeStr = "ONTVANGST";
            this.stockOwnerStr = cIntakeorder.currentIntakeOrder.getStockownerStr();
            if (cIntakeorderMATLine.currentIntakeorderMATLine == null){
                this.unknownLineBln = true;
            }
            else {this.lineNoLng = cText.pIntegerToLongLng(cIntakeorderMATLine.currentIntakeorderMATLine.getLineNoInt());}
        }
        if (cAppExtension.activity instanceof IntakeOrderIntakeGeneratedActivity){
            if (cIntakeorderBarcode.currentIntakeOrderBarcode != null) {
                this.barcodeStr = cIntakeorderBarcode.currentIntakeOrderBarcode.getBarcodeStr();
                this.unknownArticleBln = true;
                cArticle.currentArticle = new cArticle(cIntakeorderBarcode.currentIntakeOrderBarcode.getItemNoStr(), cIntakeorderBarcode.currentIntakeOrderBarcode.getVariantCodeStr());
                this.articleDescriptionCompactText.setText(cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getDescriptionStr());
                this.articleDescription2CompactText.setText(cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getDescription2Str());
            }
            else {
                if(cArticle.currentArticle != null && cArticle.currentArticle.barcodesObl.size() > 0){
                    this.barcodeStr = cArticle.currentArticle.barcodesObl.get(0).getBarcodeStr();
                    if (cArticle.currentArticle.barcodesObl.size() > 1){
                        this.barcodeObl = new ArrayList<>();
                        for (cArticleBarcode barcode :  cArticle.currentArticle.barcodesObl){
                            this.barcodeObl.add(barcode.getBarcodeStr() + "( " + barcode.quantityPerUnitOfMeasureDbl + " )");
                        }
                    }
                }
            }
            this.orderNumberStr = cIntakeorder.currentIntakeOrder.getOrderNumberStr();
            this.orderTypeStr = "ONTVANGST";
            this.stockOwnerStr = cIntakeorder.currentIntakeOrder.getStockownerStr();
            if (cIntakeorderMATLine.currentIntakeorderMATLine == null){
                this.unknownLineBln = true;
            }
            else {this.lineNoLng = cText.pIntegerToLongLng(cIntakeorderMATLine.currentIntakeorderMATLine.getLineNoInt());}
        }
        if (cAppExtension.activity instanceof IntakeorderMASLinesActivity){
            if (cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.barcodesObl().size()> 0) {
                this.barcodeStr = cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.barcodesObl().get(0).getBarcodeStr();
                this.unknownArticleBln = true;
                cArticle.currentArticle = new cArticle(cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getItemNoStr(),cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getVariantCodeStr());
                this.articleDescriptionCompactText.setText(cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getDescriptionStr());
                this.articleDescription2CompactText.setText(cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getDescription2Str());

                if (cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.barcodesObl().size() > 1){
                    this.barcodeObl = new ArrayList<>();
                    for (cIntakeorderBarcode barcode :  cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.barcodesObl()){
                        this.barcodeObl.add(barcode.getBarcodeAndQuantityStr());
                    }
                }
            }
            this.orderNumberStr = cIntakeorder.currentIntakeOrder.getOrderNumberStr();
            this.orderTypeStr = "ONTVANGST";
            this.stockOwnerStr = cIntakeorder.currentIntakeOrder.getStockownerStr();
            if (cIntakeorderMATLine.currentIntakeorderMATLine == null){
                this.unknownLineBln = true;
            }
            else {this.lineNoLng = cText.pIntegerToLongLng(cIntakeorderMATLine.currentIntakeorderMATLine.getLineNoInt());}
        }
        if (cAppExtension.activity instanceof IntakeorderMATLinesActivity){
            if (cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.barcodesObl().size()> 0) {
                this.barcodeStr = cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.barcodesObl().get(0).getBarcodeStr();
                this.unknownArticleBln = true;
                cArticle.currentArticle = new cArticle(cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getItemNoStr(),cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getVariantCodeStr());
                this.articleDescriptionCompactText.setText(cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getDescriptionStr());
                this.articleDescription2CompactText.setText(cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getDescription2Str());
                if (cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.barcodesObl().size() > 1){
                    this.barcodeObl = new ArrayList<>();
                    for (cIntakeorderBarcode barcode :  cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.barcodesObl()){
                        this.barcodeObl.add(barcode.getBarcodeAndQuantityStr());
                    }
                }
            }
            this.orderNumberStr = cIntakeorder.currentIntakeOrder.getOrderNumberStr();
            this.orderTypeStr = "ONTVANGST";
            this.stockOwnerStr = cIntakeorder.currentIntakeOrder.getStockownerStr();
            if (cIntakeorderMATLine.currentIntakeorderMATLine == null){
                this.unknownLineBln = true;
            }
            else {this.lineNoLng = cText.pIntegerToLongLng(cIntakeorderMATLine.currentIntakeorderMATLine.getLineNoInt());}
        }
        if (cAppExtension.activity instanceof ReceiveOrderReceiveActivity){
            if (cIntakeorderBarcode.currentIntakeOrderBarcode != null) {
                this.barcodeStr = cIntakeorderBarcode.currentIntakeOrderBarcode.getBarcodeStr();
                this.unknownArticleBln = true;
                cArticle.currentArticle = new cArticle(cIntakeorderBarcode.currentIntakeOrderBarcode.getItemNoStr(), cIntakeorderBarcode.currentIntakeOrderBarcode.getVariantCodeStr());
                this.articleDescriptionCompactText.setText(cReceiveorderSummaryLine.currentReceiveorderSummaryLine.getDescriptionStr());
                this.articleDescription2CompactText.setText(cReceiveorderSummaryLine.currentReceiveorderSummaryLine.getDescription2Str());
            }
            else {
                if(cArticle.currentArticle != null && cArticle.currentArticle.barcodesObl.size() > 0){
                    this.barcodeStr = cArticle.currentArticle.barcodesObl.get(0).getBarcodeStr();
                    if (cArticle.currentArticle.barcodesObl.size() > 1){
                        this.barcodeObl = new ArrayList<>();
                        for (cArticleBarcode barcode :  cArticle.currentArticle.barcodesObl){
                            this.barcodeObl.add(barcode.getBarcodeStr() + "( " + barcode.quantityPerUnitOfMeasureDbl + " )");
                        }
                    }
                }
            }
            this.orderNumberStr = cIntakeorder.currentIntakeOrder.getOrderNumberStr();
            this.orderTypeStr = "ONTVANGST";
            this.stockOwnerStr = cIntakeorder.currentIntakeOrder.getStockownerStr();
            if (cReceiveorderLine.currentReceiveorderLine == null){
                this.unknownLineBln = true;
            }
            else {this.lineNoLng = cText.pIntegerToLongLng(cReceiveorderLine.currentReceiveorderLine.getLineNoInt());}
        }
        if (cAppExtension.activity instanceof ReturnArticleDetailActivity){
            if (cReturnorderBarcode.currentReturnOrderBarcode != null) {
                this.barcodeStr = cReturnorderBarcode.currentReturnOrderBarcode.getBarcodeStr();
            } else {
                if(cArticle.currentArticle != null && cArticle.currentArticle.barcodesObl.size() > 0){
                    this.barcodeStr = cArticle.currentArticle.barcodesObl.get(0).getBarcodeStr();
                    if (cArticle.currentArticle.barcodesObl.size() > 1){
                        this.barcodeObl = new ArrayList<>();
                        for (cArticleBarcode barcode :  cArticle.currentArticle.barcodesObl){
                            this.barcodeObl.add(barcode.getBarcodeStr() + "( " + barcode.quantityPerUnitOfMeasureDbl + " )");
                        }
                    }
                }
            }
            this.orderNumberStr = cReturnorder.currentReturnOrder.getOrderNumberStr();
            this.orderTypeStr = "RETOUR";
            this.stockOwnerStr = cReturnorder.currentReturnOrder.getStockownerStr();
            if (cReturnorderLine.currentReturnOrderLine == null){
                this.unknownLineBln = true;
            }
            else {this.lineNoLng = cText.pIntegerToLongLng(cReturnorderLine.currentReturnOrderLine.getLineNoInt());
                if (this.lineNoLng == 0L) {this.unknownLineBln = true;}
            }
        }
        if (cAppExtension.activity instanceof MenuActivity){
            if (cArticle.currentArticle.barcodesObl.size() > 0){
                this.barcodeStr = cArticle.currentArticle.barcodesObl.get(0).getBarcodeStr();
                if (cArticle.currentArticle.barcodesObl.size() > 1){
                    this.barcodeObl = new ArrayList<>();
                    for (cArticleBarcode barcode :  cArticle.currentArticle.barcodesObl){
                        this.barcodeObl.add(barcode.getBarcodeStr() + "( " + barcode.quantityPerUnitOfMeasureDbl + " )");
                    }
                }
            }
            this.unknownLineBln = true;
            this.stockOwnerStr = "";
        }

        if (this.barcodeStr == null){
            cAppExtension.dialogFragment.dismiss();
            return;
        }
        if (this.unknownLineBln && cArticle.currentArticle == null) {
            cAppExtension.dialogFragment.dismiss();
            return;
        }
        this.mSetBarcode(this.barcodeStr);
        this.progressBar.setVisibility(View.INVISIBLE);
        this.mShowBarcodeSpinner();
        this.mShowWorkPlaceSpinner();
        this.mShowLabelTemplateSpinner();
        this.amountLng = 1L;
        this.mShowQuantityInfo();
        this.mSetArticleInfo();

    }

    @Override
    public void mSetListeners() {
        this.mSetPrintListener();
        this.mSetCancelListener();
        if (this.barcodeObl != null){this.mSetBarcodeSpinnerListener();}
        this.mSetWorkplaceSpinnerListener();
        this.mSetLayoutSpinnerListener();
        this.mSetMinusListener();
        this.mSetPlusListener();
        this.mSetNumberListener();
    }

    private void mSetCancelListener() {
        this.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View pvView) {
                cAppExtension.dialogFragment.dismiss();
            }
        });
    }

    private void mSetPrintListener() {
        this.printButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View pvView) {
                if (cWorkplace.currentWorkplace == null | cLabelTemplate.currentLabelTemplate == null){
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        mPrintItemLabel();
                    }
                };
                Thread printLabel = new Thread(runnable);
                printLabel.start();
                //
            }
        });
    }

    private void  mSetArticleInfo(){
        if (!this.unknownArticleBln) {
            this.articleDescriptionCompactText.setText(cArticle.currentArticle.getDescriptionStr());
            this.articleDescription2CompactText.setText(cArticle.currentArticle.getDescription2Str());
        }
        this.articleItemCompactText.setText(cArticle.currentArticle.getItemNoAndVariantCodeStr());
        this.articleBarcodeCompactText.setText(this.barcodeStr);
        this.imageButtonNoInputPropertys.setVisibility(View.GONE);
    }
    private void mPrintItemLabel(){


        cPrintItemLabel printItemLabel;


        if (this.unknownLineBln) {
            printItemLabel = new cPrintItemLabel(this.stockOwnerStr, this.barcodeStr, this.amountLng);
            if(  printItemLabel.pPrintItemLabelViaWebserviceBln()){

                cAppExtension.dialogFragment.dismiss();
                return;
            }
        }
        else {
            printItemLabel = new cPrintItemLabel(this.orderTypeStr, this.orderNumberStr, this.lineNoLng, this.barcodeStr, this.amountLng);
            if(  printItemLabel.pPrintLineItemLabelViaWebserviceBln()){

                cAppExtension.dialogFragment.dismiss();
                return;
            }
        }

        this.progressBar.setVisibility(View.INVISIBLE);
        cUserInterface.pShowToastMessage(cAppExtension.context.getString(R.string.error_print_failed),null);
    }

    private void mShowLabelTemplateSpinner() {

        if (cLabelTemplate.itemTemplateObl == null ||  cLabelTemplate.itemTemplateObl.size() <= 0) {
            this.printButton.setVisibility(View.GONE);
            this.textViewLayoutFail.setText(cAppExtension.activity.getString(R.string.message_templates_not_available));
            return;
        }

        this.layoutSpinner.setVisibility(View.VISIBLE);
        this.mFillTemplateSpinner();
    }


    private void mFillTemplateSpinner() {

        if (cLabelTemplate.itemTemplateObl == null ||  cLabelTemplate.itemTemplateObl.size() <= 0 ) {
            return;
        }

        List<String> labelTemplateObl = new ArrayList<>();
        for (cLabelTemplate labelTemplate :cLabelTemplate.itemTemplateObl ) {
            labelTemplateObl.add(labelTemplate.getTemplateStr());
        }


        ArrayAdapter<String> adapter = new ArrayAdapter<>(cAppExtension.context,
                android.R.layout.simple_spinner_dropdown_item,
                labelTemplateObl);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        this.layoutSpinner.setAdapter(adapter);
    }

    private void mSetLayoutSpinnerListener() {
        this.layoutSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                cLabelTemplate.currentLabelTemplate = cLabelTemplate.pGetLabelTemplateByName(cLabelTemplate.itemTemplateObl ,layoutSpinner.getSelectedItem().toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });
    }

    private void mShowWorkPlaceSpinner() {

        if (cWorkplace.allWorkplacesObl  == null || cWorkplace.allWorkplacesObl.size() == 0) {
            this.printButton.setVisibility(View.GONE);
            this.textViewWorkspaceFail.setText(cAppExtension.activity.getString(R.string.message_workplaces_not_available));
            return;
        }

        this.workplaceSpinner.setVisibility(View.VISIBLE);
        this.mFillWorkplaceSpinner();
    }


    private void mFillWorkplaceSpinner() {

        if (cWorkplace.allWorkplacesObl == null ||  cWorkplace.allWorkplacesObl.size() <= 0 ) {
            return;
        }

        List<String> workPlaceObl = new ArrayList<>();

        if (cUser.currentUser.currentBranch.workplacesObl().size() >= 1) {
            for (cWorkplace workplace :cUser.currentUser.currentBranch.workplacesObl() ) {
                workPlaceObl.add(workplace.getWorkplaceStr());
            }
        }
        else
        {
            for (cWorkplace workplace :cWorkplace.allWorkplacesObl ) {
                workPlaceObl.add(workplace.getWorkplaceStr());
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(cAppExtension.context,
                android.R.layout.simple_spinner_dropdown_item,
                workPlaceObl);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        this.workplaceSpinner.setAdapter(adapter);
        if (cWorkplace.currentWorkplace != null)
        { this.workplaceSpinner.setSelection(adapter.getPosition(cWorkplace.currentWorkplace.getWorkplaceStr()));}
    }

    private void mSetWorkplaceSpinnerListener() {
        this.workplaceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                cWorkplace.currentWorkplace = cWorkplace.pGetWorkplaceByName(workplaceSpinner.getSelectedItem().toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });
    }

    private void mShowBarcodeSpinner() {

        if (this.barcodeObl  == null) {
            this.barcodeSpinner.setVisibility(View.GONE);
            return;
        }

        this.barcodeSpinner.setVisibility(View.VISIBLE);
        this.mFillBarcodeSpinner();
    }

    private void mFillBarcodeSpinner() {

        if (this.barcodeObl  == null) {
            return;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(cAppExtension.context,
                android.R.layout.simple_spinner_dropdown_item,
                this.barcodeObl);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        this.barcodeSpinner.setAdapter(adapter);
        if (this.barcodeStr != null)
        { this.barcodeSpinner.setSelection(adapter.getPosition(this.barcodeStr));}
    }

    private void mSetBarcodeSpinnerListener() {
        this.barcodeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                mSetBarcode(barcodeSpinner.getSelectedItem().toString().substring(0, barcodeSpinner.getSelectedItem().toString().indexOf("(")));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });
    }

    //Region Private Methods

    private void mTryToChangeQuantity(Boolean pvIsPositiveBln, Boolean pvAmountFixedBln, Integer pvAmountInt) {

        if (pvIsPositiveBln) {

            //Determine the new amount
            this.amountLng += pvAmountInt;
            this.quantityText.setText(cText.pLongToStringStr(this.amountLng));
            return;
        }

        //negative
        //Check if value already is zero
        if (this.amountLng <= 1 ) {
            cUserInterface.pDoNope(this.quantityText, true, true);
            return;
        }
        //Determine the new amount
        this.amountLng -= pvAmountInt;
        //Change quantityDbl in activity
        this.quantityText.setText(cText.pLongToStringStr(this.amountLng));

    }

    private void mShowQuantityInfo() {

        this.quantityText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        this.quantityText.setSelectAllOnFocus(true);
        this.quantityText.requestFocus();
        this.quantityText.setSingleLine();
        this.quantityText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_TEXT_VARIATION_NORMAL);
        this.quantityText.setCursorVisible(false);

        this.quantityText.setText(cText.pLongToStringStr(this.amountLng));
        this.quantityRequiredText.setVisibility(View.INVISIBLE);
    }

    private void mNumberClicked() {
        this.mShowNumberPickerFragment();
    }
    //Region Number Broadcaster

    private void mShowNumberPickerFragment() {

        cUserInterface.pCheckAndCloseOpenDialogs();

        Bundle bundle = new Bundle();
        bundle.putLong(cPublicDefinitions.NUMBERINTENT_CURRENTQUANTITY, this.amountLng);
        bundle.putDouble(cPublicDefinitions.NUMBERINTENT_MAXQUANTITY,9999);

        NumberpickerFragment numberpickerFragment = new NumberpickerFragment();
        numberpickerFragment.setArguments(bundle);

        numberpickerFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.NUMBERPICKERFRAGMENT_TAG);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void mSetPlusListener() {

        this.imageButtonPlus.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (plusHandler != null) return true;
                    plusHandler = new Handler();
                    plusHandler.postDelayed(mPlusAction, 750);
                }

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (plusHandler == null) return true;
                    plusHandler.removeCallbacks(mPlusAction);
                    plusHandler = null;
                    counterPlusHelperInt = 0;
                }

                return false;
            }
        });

        this.imageButtonPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTryToChangeQuantity(true, false, 1);
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void mSetMinusListener() {

        this.imageButtonMinus.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (minusHandler != null) return true;
                    minusHandler = new Handler();
                    minusHandler.postDelayed(mMinusAction, 750);
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (minusHandler == null) return true;
                    minusHandler.removeCallbacks(mMinusAction);
                    minusHandler = null;
                    counterMinusHelperInt = 0;
                }
                return false;
            }

        });

        this.imageButtonMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTryToChangeQuantity(false, false, 1);
            }
        });
    }

    private void mSetNumberListener() {
        this.quantityText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNumberClicked();
            }
        });

    }

    private final Runnable mMinusAction = new Runnable() {
        @Override
        public void run() {
            imageButtonMinus.performClick();
            long milliSecsLng;
            if (counterMinusHelperInt < 10) {
                milliSecsLng = 200;
            } else if (counterMinusHelperInt < 20) {
                milliSecsLng = 150;
            } else if (counterMinusHelperInt < 30) {
                milliSecsLng = 100;
            } else if (counterMinusHelperInt < 40) {
                milliSecsLng = 50;
            } else {
                milliSecsLng = 50;
            }
            mDoDelayedMinus(this, milliSecsLng);
        }
    };

    private final Runnable mPlusAction = new Runnable() {
        @Override
        public void run() {
            imageButtonPlus.performClick();
            long milliSecsLng;
            if (counterPlusHelperInt < 10) {
                milliSecsLng = 200;
            } else if (counterPlusHelperInt < 20) {
                milliSecsLng = 150;
            } else if (counterPlusHelperInt < 30) {
                milliSecsLng = 100;
            } else if (counterPlusHelperInt < 40) {
                milliSecsLng = 50;
            } else {
                milliSecsLng = 50;
            }
            mDoDelayedPlus(this, milliSecsLng);
        }
    };

    private final BroadcastReceiver mNumberReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int numberChosenInt = 0;
            Bundle extras = intent.getExtras();

            if (extras != null) {
                numberChosenInt = extras.getInt(cPublicDefinitions.NUMBERINTENT_EXTRANUMBER);
            }
            mHandleQuantityChosen(numberChosenInt);
        }
    };

    private void mDoDelayedMinus(Runnable pvRunnable, long pvMilliSecsLng) {
        this.minusHandler.postDelayed(pvRunnable, pvMilliSecsLng);
        this.counterMinusHelperInt += 1;
    }

    private void mDoDelayedPlus(Runnable pvRunnable, long pvMilliSecsLng) {
        this.plusHandler.postDelayed(pvRunnable, pvMilliSecsLng);
        this.counterPlusHelperInt += 1;
    }

    private void mHandleQuantityChosen(Integer pvQuantityInt) {
        this.mTryToChangeQuantity(pvQuantityInt != 0, true,pvQuantityInt);
    }

    private void mSetBarcode(String pvBarcodeStr){
        this.barcodeStr = pvBarcodeStr;
        this.textViewTitle.setText(this.barcodeStr);

//        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
//        try {
//            BitMatrix bitMatrix = multiFormatWriter.encode( this.barcodeStr , BarcodeFormat.CODE_128, 250, 72);
//            Bitmap bitmap = Bitmap.createBitmap(250, 72, Bitmap.Config.RGB_565);
//            for(int i = 0; i< 250; i++){
//                for(int j =  0; j< 72; j++){
//                    bitmap.setPixel(i,j,bitMatrix.get(i,j)? Color.BLACK:Color.WHITE);
//                }
//            }
//            this.barcodeImage.setImageBitmap(bitmap);
//
//        } catch (WriterException e) {
//            e.printStackTrace();
//        }

    }

}