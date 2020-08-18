package SSU_WHS.Return.ReturnorderLine;

import androidx.lifecycle.ViewModelProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ICS.Utils.cResult;
import ICS.Utils.cText;
import ICS.Weberror.cWeberror;
import ICS.cAppExtension;
import SSU_WHS.Basics.Article.cArticle;
import SSU_WHS.Basics.BranchReason.cBranchReason;
import SSU_WHS.Basics.Users.cUser;
import SSU_WHS.General.cDatabase;
import SSU_WHS.Return.ReturnOrder.cReturnorder;
import SSU_WHS.Return.ReturnorderBarcode.cReturnorderBarcode;
import SSU_WHS.Return.ReturnorderDocument.cReturnorderDocument;
import SSU_WHS.Return.ReturnorderLineBarcode.cReturnorderLineBarcode;
import SSU_WHS.Return.ReturnorderLineBarcode.cReturnorderLineBarcodeViewModel;
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

    public List<cReturnorderBarcode> barcodeObl() {
        List<cReturnorderBarcode> resultObl = new ArrayList<>();

        if (cReturnorder.currentReturnOrder.barcodesObl() == null || cReturnorder.currentReturnOrder.barcodesObl().size() == 0) {
            return  resultObl;
        }

        //Loop through all barcodes, and if item matches add it to the list
        for (cReturnorderBarcode returnorderBarcode : cReturnorder.currentReturnOrder.barcodesObl()) {
            if (returnorderBarcode.getItemNoStr().equalsIgnoreCase(this.getItemNoStr()) && returnorderBarcode.getVariantCodeStr().equalsIgnoreCase(this.getVariantCodeStr())) {
                resultObl.add(returnorderBarcode);
            }
        }

        return  resultObl;
    }



    //Region Public Properties

    private int lineNoInt;
    public  int getLineNoInt(){return  lineNoInt;}

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

    public String retourRedenStr;
    public String getRetourRedenStr() {return this.retourRedenStr;}

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

    public String getReturnReasonDescriptoinStr(){
        cBranchReason  branchReason = cUser.currentUser.currentBranch.pGetReasonByName(this.getRetourRedenStr());
        if (branchReason != null){
            return branchReason.getDescriptionStr();
        } else {
            return "";
        }
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

    private cReturnorderLineViewModel getReturnorderLineViewModel(){
        return new ViewModelProvider(cAppExtension.fragmentActivity).get(cReturnorderLineViewModel.class);
    }

    private cReturnorderLineBarcodeViewModel getReturnorderLineBarcodeViewModel(){
        return new ViewModelProvider(cAppExtension.fragmentActivity).get(cReturnorderLineBarcodeViewModel.class);
    }

    //End Region Public Properties

    //Region Constructor
    public cReturnorderLine(JSONObject pvJsonObject) {
        this.returnorderLineEntity = new cReturnorderLineEntity(pvJsonObject);
        this.itemNoStr = this.returnorderLineEntity.getItemNoStr();
        this.variantCodeStr = this.returnorderLineEntity.getVariantCodeStr();
        this.documentStr = this.returnorderLineEntity.getDocumentStr();
        this.retourRedenStr = this.returnorderLineEntity.getRetourreden();
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

        if (this.getRetourRedenStr().isEmpty()) {
            this.nieuweRegelBln = true;
        }

    }

      public cReturnorderLine(cArticle pvArticle){
        this.returnorderLineEntity = new cReturnorderLineEntity(pvArticle);
        this.itemNoStr = this.returnorderLineEntity.getItemNoStr();
        this.variantCodeStr = this.returnorderLineEntity.getVariantCodeStr();
        this.documentStr = this.returnorderLineEntity.getDocumentStr();
        this.retourRedenStr = this.returnorderLineEntity.getRetourreden();
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

          if (this.getRetourRedenStr().isEmpty()) {
              this.nieuweRegelBln = true;
          }
    }

    public cReturnorderLine(cReturnorderBarcode pvReturnorderBarcode){
        this.returnorderLineEntity = new cReturnorderLineEntity(pvReturnorderBarcode);
        this.itemNoStr = this.returnorderLineEntity.getItemNoStr();
        this.variantCodeStr = this.returnorderLineEntity.getVariantCodeStr();
        this.documentStr = this.returnorderLineEntity.getDocumentStr();
        this.retourRedenStr = this.returnorderLineEntity.getRetourreden();
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

        if (this.getRetourRedenStr().isEmpty()) {
            this.nieuweRegelBln = true;
        }
    }


    //End Region Constructor

    public boolean pInsertInDatabaseBln() {
        this.getReturnorderLineViewModel().insert(this.returnorderLineEntity);

        if (cReturnorderLine.allLinesObl == null){
            cReturnorderLine.allLinesObl = new ArrayList<>();
        }
        cReturnorderLine.allLinesObl.add(this);
        return  true;
    }

    public static boolean pTruncateTableBln(){
        cReturnorderLineViewModel returnorderLineViewModel =  new ViewModelProvider(cAppExtension.fragmentActivity).get(cReturnorderLineViewModel.class);
        returnorderLineViewModel.deleteAll();
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

        WebResult = this.getReturnorderLineViewModel().pSaveLineViaWebserviceWrs();
        if (WebResult.getResultBln() && WebResult.getSuccessBln()){
            cReturnorderLine.currentReturnOrderLine.lineNumberObl.add(WebResult.getResultLng());
            cReturnorderLine.currentReturnOrderLine.lineBarcodesObl.clear();

            if (cReturnorderLine.currentReturnOrderLine.getItemNoStr().equalsIgnoreCase("UNKNOWN")) {
                JSONObject jsonObject = WebResult.getResultDtt().get(0);
                try {
                    cReturnorderLine.currentReturnOrderLine.variantCodeStr =    jsonObject.getString(cDatabase.VARIANTCODE_NAMESTR);
                    cReturnorderLine.currentReturnOrderLine.description2Str =  jsonObject.getString(cDatabase.DESCRIPTION2_NAMESTR);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return  true;
            }
        else {
            cReturnorder.currentReturnOrder.unknownVariantCounterInt -= 1;
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_RETURNLINESAVE);
            return  false;
        }
    }

    public void pUpdateQuantityInDatabase(){
       this.getReturnorderLineViewModel().pUpdateQuantity();
    }

    public cResult pResetRst(){

        //nit the result
        cResult result = new cResult();
        result.resultBln = true;

        cWebresult WebResult = new cWebresult();

        for (Long loopLineNoLng : cReturnorderLine.currentReturnOrderLine.lineNumberObl) {
            WebResult =  this.getReturnorderLineViewModel().pResetLineViaWebserviceWrs(loopLineNoLng);
        }

        if (WebResult.getResultBln() && WebResult.getSuccessBln()){

            //Remove line barcodes from the database

            //todo: check if this lineNo is filled or is bullshit
            this.getReturnorderLineBarcodeViewModel().pDeleteForLineNo(this.getLineNoInt());

            //Reset this line
            cReturnorderLine.currentReturnOrderLine.pUpdateQuantityInDatabase();
            cReturnorderLine.currentReturnOrderLine.lineBarcodesObl.clear();

            cReturnorderDocument.currentReturnOrderDocument.returnorderLineObl.remove(cReturnorderLine.currentReturnOrderLine);
            cBranchReason.currentBranchReason = null;
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
            WebResult =  this.getReturnorderLineViewModel().pResetLineViaWebserviceWrs(loopLineNoLng);
        }

        if (WebResult.getResultBln() && WebResult.getSuccessBln()){


            //Remove line barcodes from the database
            this.getReturnorderLineBarcodeViewModel().pDeleteForLineNo(this.getLineNoInt());

            //Reset this line
            cReturnorderLine.currentReturnOrderLine.pUpdateQuantityInDatabase();
            if (cReturnorderLine.currentReturnOrderLine.lineBarcodesObl != null){
                cReturnorderLine.currentReturnOrderLine.lineBarcodesObl.clear();
            }

            return  result;
        }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_RETURNLINERESET);
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.activity.getString(R.string.message_reset_line_via_webservice_failed));
            return  result;
        }
    }

    public static void pCheckIfLineIsAlreadyInUse () {

        ArrayList<cReturnorderLineBarcode> lineBarcodeObl;
        lineBarcodeObl = new ArrayList<>();

        for (cReturnorderLine returnorderLine : cReturnorderDocument.currentReturnOrderDocument.returnorderLineObl){
            if (returnorderLine.getRetourRedenStr().equals(cReturnorderLine.currentReturnOrderLine.getRetourRedenStr())
                    && returnorderLine.getItemNoStr().equals(cReturnorderLine.currentReturnOrderLine.getItemNoStr())
                    && returnorderLine.getVariantCodeStr().equals(cReturnorderLine.currentReturnOrderLine.getVariantCodeStr())){

                if (returnorderLine.getSortingSequenceNoInt() != cReturnorderLine.currentReturnOrderLine.getSortingSequenceNoInt()) {
                    lineBarcodeObl.addAll(cReturnorderLine.currentReturnOrderLine.lineBarcodesObl);
                    returnorderLine.quantityHandledTakeDbl += cReturnorderLine.currentReturnOrderLine.getQuantityHandledTakeDbl();
                    cReturnorderLine.currentReturnOrderLine = returnorderLine;
                    cReturnorderLine.currentReturnOrderLine.lineBarcodesObl = lineBarcodeObl;
                }
            }
        }
    }


}
