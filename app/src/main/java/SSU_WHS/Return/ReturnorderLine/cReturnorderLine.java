package SSU_WHS.Return.ReturnorderLine;

import androidx.lifecycle.ViewModelProviders;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ICS.Utils.cResult;
import ICS.Utils.cText;
import ICS.Weberror.cWeberror;
import ICS.cAppExtension;
import SSU_WHS.Basics.Article.cArticle;
import SSU_WHS.Return.ReturnOrder.cReturnorder;
import SSU_WHS.Return.ReturnorderDocument.cReturnorderDocument;
import SSU_WHS.Return.ReturnorderLineBarcode.cReturnorderLineBarcode;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebserviceDefinitions;
import nl.icsvertex.scansuite.R;


public class cReturnorderLine {

    private cReturnorderLineEntity returnorderLineEntity;
    public boolean nieuweRegelBln;

    public static List<cReturnorderLine> allLinesObl;
    public static cReturnorderLine currentReturnOrderLine;
    public List<Long> lineNumberObl;

    public List<cReturnorderLineBarcode> lineBarcodesObl;

    private static cReturnorderLineViewModel gReturnorderLineViewModel;
    public static cReturnorderLineViewModel getReturnorderLineViewModel() {
        if (gReturnorderLineViewModel == null) {
            gReturnorderLineViewModel = ViewModelProviders.of(cAppExtension.fragmentActivity ).get(cReturnorderLineViewModel.class);
        }
        return gReturnorderLineViewModel;
    }

    private static cReturnorderLineAdapter gReturnorderLineAdapter;
    public static cReturnorderLineAdapter getReturnorderLineAdapter() {
        if (gReturnorderLineAdapter == null) {
            gReturnorderLineAdapter = new cReturnorderLineAdapter();
        }
        return gReturnorderLineAdapter;
    }


    //Region Public Properties

    private int lineNoInt;
    public  int getLineNoInt(){return  lineNoInt;}
    public void setLineNoInt(int lineNoInt) {
        this.lineNoInt = lineNoInt;
    }

    private String itemNoStr;
    public String getItemNoStr() {
        return itemNoStr;
    }

    private String variantCodeStr;
    public String getVariantCodeStr() {
        return variantCodeStr;
    }

    public  String getItemNoAndVariantCodeStr(){
        return this.getItemNoStr() + " " + this.getVariantCodeStr();
    }

    private String documentStr;
    public String getDocumentStr() {return this.documentStr;}

    public String retourredenStr;
    public String getRetourredenStr() {return this.retourredenStr;}

    private String descriptionStr;
    public String getDescriptionStr() {
        return descriptionStr;
    }

    private String description2Str;
    public String getDescription2Str() {
        return description2Str;
    }

    private String vendorItemNoStr;
    public String getVendorItemNoStr() { return vendorItemNoStr; }

    private String vendorItemDescriptionStr;
    public String getVendorItemDescriptionStr() { return vendorItemDescriptionStr; }

    public  String getVendorItemNoAndDescriptionStr(){
        return  this.getVendorItemNoStr() + " " + this.getVendorItemDescriptionStr();
    }

    private int sortingSequenceNoInt;
    private int getSortingSequenceNoInt() { return sortingSequenceNoInt; }

    public Double quantitytakeDbl;
    public Double getQuantitytakeDbl() {return  this.quantitytakeDbl;}

    public Double quantityHandledTakeDbl;
    public double getQuantityHandledTakeDbl() {return this.quantityHandledTakeDbl;}

    public  String getQuantityToShowInAdapterStr(){
        return  cText.pDoubleToStringStr(this.getQuantityHandledTakeDbl()) + "/" + cText.pDoubleToStringStr(this.getQuantitytakeDbl());
    }

    private String extraField1Str;
    public String getExtraField1Str() {
        return extraField1Str;
    }

    private String extraField2Str;
    public String getExtraField2Str() {
        return extraField2Str;
    }

    private String extraField3Str;
    public String getExtraField3Str() {
        return extraField3Str;
    }

    private String extraField4Str;
    public String getExtraField4Str() {
        return extraField4Str;
    }

    private String extraField5Str;
    public String getExtraField5Str() {
        return extraField5Str;
    }

    private String extraField6Str;
    public String getExtraField6Str() {
        return extraField6Str;
    }

    private String extraField7Str;
    public String getExtraField7Str() {
        return extraField7Str;
    }

    private String extraField8Str;
    public String getExtraField8Str() {
        return extraField8Str;
    }

    private boolean generatedBln;
    public boolean isGeneratedBln() {
        return generatedBln;
    }

    //End Region Public Properties

    //Region Constructor
    public cReturnorderLine(JSONObject pvJsonObject) {
        this.returnorderLineEntity = new cReturnorderLineEntity(pvJsonObject);
        this.itemNoStr = this.returnorderLineEntity.getItemNoStr();
        this.variantCodeStr = this.returnorderLineEntity.getVariantCodeStr();
        this.documentStr = this.returnorderLineEntity.getDocumentStr();
        this.retourredenStr = this.returnorderLineEntity.getRetourreden();
        this.descriptionStr = this.returnorderLineEntity.getDescriptionStr();
        this.description2Str = this.returnorderLineEntity.getDescription2Str();
        this.vendorItemNoStr = this.returnorderLineEntity.getVendorItemNoStr();
        this.vendorItemDescriptionStr = this.returnorderLineEntity.getVendorItemDescriptionStr();
        this.sortingSequenceNoInt = this.returnorderLineEntity.getSortingsequencenoInt();
        this.quantitytakeDbl = this.returnorderLineEntity.getQuantitytakeDbl();
        this.quantityHandledTakeDbl = this.returnorderLineEntity.getQuantityHandledtake();
        this.extraField1Str =  this.returnorderLineEntity.getExtraField1Str();
        this.extraField2Str = this.returnorderLineEntity.getExtraField2Str();
        this.extraField3Str =  this.returnorderLineEntity.getExtraField3Str();
        this.extraField4Str =  this.returnorderLineEntity.getExtraField4Str();
        this.extraField5Str =  this.returnorderLineEntity.getExtraField5Str();
        this.extraField6Str =  this.returnorderLineEntity.getExtraField6Str();
        this.extraField7Str =  this.returnorderLineEntity.getExtraField7Str();
        this.extraField8Str =  this.returnorderLineEntity.getExtraField8Str();
        this.generatedBln = this.returnorderLineEntity.getGeneratedBln();
    }

    public cReturnorderLine(cArticle pvArticle){
        this.returnorderLineEntity = new cReturnorderLineEntity(pvArticle);
        this.itemNoStr = this.returnorderLineEntity.getItemNoStr();
        this.variantCodeStr = this.returnorderLineEntity.getVariantCodeStr();
        this.documentStr = this.returnorderLineEntity.getDocumentStr();
        this.retourredenStr = this.returnorderLineEntity.getRetourreden();
        this.descriptionStr = this.returnorderLineEntity.getDescriptionStr();
        this.description2Str = this.returnorderLineEntity.getDescription2Str();
        this.vendorItemNoStr = this.returnorderLineEntity.getVendorItemNoStr();
        this.vendorItemDescriptionStr = this.returnorderLineEntity.getVendorItemDescriptionStr();
        this.sortingSequenceNoInt = this.returnorderLineEntity.getSortingsequencenoInt();
        this.quantitytakeDbl = this.returnorderLineEntity.getQuantitytakeDbl();
        this.quantityHandledTakeDbl = this.returnorderLineEntity.getQuantityHandledtake();
        this.extraField1Str =  this.returnorderLineEntity.getExtraField1Str();
        this.extraField2Str = this.returnorderLineEntity.getExtraField2Str();
        this.extraField3Str =  this.returnorderLineEntity.getExtraField3Str();
        this.extraField4Str =  this.returnorderLineEntity.getExtraField4Str();
        this.extraField5Str =  this.returnorderLineEntity.getExtraField5Str();
        this.extraField6Str =  this.returnorderLineEntity.getExtraField6Str();
        this.extraField7Str =  this.returnorderLineEntity.getExtraField7Str();
        this.extraField8Str =  this.returnorderLineEntity.getExtraField8Str();
        this.generatedBln = this.returnorderLineEntity.getGeneratedBln();
    }
    //End Region Constructor

    public boolean pInsertInDatabaseBln() {
        cReturnorderLine.getReturnorderLineViewModel().insert(this.returnorderLineEntity);

        if (cReturnorderLine.allLinesObl == null){
            cReturnorderLine.allLinesObl = new ArrayList<>();
        }
        cReturnorderLine.allLinesObl.add(this);
        return  true;
    }

    public static boolean pTruncateTableBln(){
        cReturnorderLine.getReturnorderLineViewModel().deleteAll();
        return true;
    }

    public void pAddLineBarcode(String pvBarcodeStr, Double pvQuantityHandled) {

        if (this.lineBarcodesObl == null) {
            this.lineBarcodesObl = new ArrayList<>();
        }

        cReturnorderLineBarcode returnorderLineBarcode = new cReturnorderLineBarcode((long) this.getSortingSequenceNoInt(),pvBarcodeStr, pvQuantityHandled);

        if (cReturnorderLineBarcode.allLineBarcodesObl == null){
            cReturnorderLineBarcode.allLineBarcodesObl = new ArrayList<>();
        }

        cReturnorderLineBarcode.allLineBarcodesObl.add(returnorderLineBarcode);
        this.lineBarcodesObl.add(returnorderLineBarcode);

        cReturnorderLineBarcode.currentreturnorderLineBarcode = returnorderLineBarcode;
    }

    public boolean pSaveLineViaWebserviceBln (){

        cWebresult WebResult;

        if (cReturnorderLine.currentReturnOrderLine.lineNumberObl == null){
            cReturnorderLine.currentReturnOrderLine.lineNumberObl = new ArrayList<>();
        }

        WebResult =  cReturnorderLine.getReturnorderLineViewModel().pSaveLineViaWebserviceWrs();
        if (WebResult.getResultBln() && WebResult.getSuccessBln()){
            cReturnorderLine.currentReturnOrderLine.lineNumberObl.add(WebResult.getResultLng());
            cReturnorderLine.currentReturnOrderLine.lineBarcodesObl.clear();
            return  true;
            }
        else {
            cReturnorder.currentReturnOrder.unknownVariantCounterInt -= 1;
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_RETURNLINESAVE);
            return  false;
        }
    }

    public void pUpdateQuantityInDatabase(){
       cReturnorderLine.getReturnorderLineViewModel().pUpdateQuantityBln();
    }

    public cResult pResetRst(){

        //nit the result
        cResult result = new cResult();
        result.resultBln = true;

        cWebresult WebResult = new cWebresult();

        for (Long loopLineNoLng : cReturnorderLine.currentReturnOrderLine.lineNumberObl) {
            WebResult =  cReturnorderLine.getReturnorderLineViewModel().pResetLineViaWebserviceWrs(loopLineNoLng);
        }

        if (WebResult.getResultBln() && WebResult.getSuccessBln()){


            //Remove line barcodes from the database

            //todo: check if this lineNo is filled or is bullshit
            cReturnorderLineBarcode.getReturnorderLineBarcodeViewModel().pDeleteForLineNo(this.getLineNoInt());

            //Reset this line
            cReturnorderLine.currentReturnOrderLine.pUpdateQuantityInDatabase();
            cReturnorderLine.currentReturnOrderLine.lineBarcodesObl.clear();

            cReturnorderDocument.currentReturnOrderDocument.returnorderLineObl.remove(cReturnorderLine.currentReturnOrderLine);

            return  result;
        }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_RETURNLINERESET);
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.activity.getString(R.string.message_reset_line_via_webservice_failed));
            return  result;
        }
    }

    public cResult pResetDocumentRst(){

        //nit the result
        cResult result = new cResult();
        result.resultBln = true;

        cWebresult WebResult = new cWebresult();
        for (Long loopLineNoLng : cReturnorderLine.currentReturnOrderLine.lineNumberObl) {
            WebResult =  cReturnorderLine.getReturnorderLineViewModel().pResetLineViaWebserviceWrs(loopLineNoLng);
        }

        if (WebResult.getResultBln() && WebResult.getSuccessBln()){


            //Remove line barcodes from the database
            cReturnorderLineBarcode.getReturnorderLineBarcodeViewModel().pDeleteForLineNo(this.getLineNoInt());

            //Reset this line
            cReturnorderLine.currentReturnOrderLine.pUpdateQuantityInDatabase();
            cReturnorderLine.currentReturnOrderLine.lineBarcodesObl.clear();

            return  result;
        }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_RETURNLINERESET);
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.activity.getString(R.string.message_reset_line_via_webservice_failed));
            return  result;
        }
    }

}
