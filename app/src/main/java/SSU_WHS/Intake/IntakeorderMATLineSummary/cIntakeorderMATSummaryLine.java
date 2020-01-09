package SSU_WHS.Intake.IntakeorderMATLineSummary;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cResult;
import ICS.Weberror.cWeberror;
import ICS.cAppExtension;
import SSU_WHS.Basics.ArticleImages.cArticleImage;
import SSU_WHS.Basics.BranchBin.cBranchBin;
import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.Intake.IntakeorderBarcodes.cIntakeorderBarcode;
import SSU_WHS.Intake.IntakeorderMATLines.cIntakeorderMATLine;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebserviceDefinitions;
import nl.icsvertex.scansuite.R;
import java.util.HashMap;


public class cIntakeorderMATSummaryLine {

    //Region Public Properties

    private String itemNoStr;
    public String getItemNoStr() {
        return itemNoStr;
    }

    private String variantCodeStr;
    public String getVariantCodeStr() {
        return variantCodeStr;
    }

    public  String getItemNoAndVariantCodeStr(){
        return   this.getItemNoStr() + " " + this.getVariantCodeStr();
    }

    private String descriptionStr;
    public String getDescriptionStr() {
        return descriptionStr;
    }

    private String description2Str;
    public String getDescription2Str() {
        return description2Str;
    }

    private String binCodeStr;
    public String getBinCodeStr() {
        return binCodeStr;
    }

    private Double quantityDbl;
    public Double getQuantityDbl()
    {
        Double resultDbl =  0.0;

        if (this.MATLinesObl == null || this.MATLinesObl.size() == 0) {
            return resultDbl ;
        }

        for (cIntakeorderMATLine intakeorderMATLine : this.MATLinesObl) {
            resultDbl += intakeorderMATLine.getQuantityDbl();
        }

        return resultDbl;
    }

    private Double quantityHandledDbl;
    public Double getQuantityHandledDbl()
    {
        Double resultDbl =  0.0;

        if (this.MATLinesObl == null || this.MATLinesObl.size() == 0) {
            return resultDbl ;
        }

        for (cIntakeorderMATLine intakeorderMATLine : this.MATLinesObl) {
            resultDbl += intakeorderMATLine.getQuantityHandledDbl();
        }

        return resultDbl;
    }

    private String sourceNoStr;
    public String getSourceNoStr() {
        return sourceNoStr;
    }

    private String vendorItemNo;
    public String getVendorItemNoStr() {
        return vendorItemNo;
    }

    private String vendorItemDescriptionStr;
    public String getVendorItemDescriptionStr() {
        return vendorItemDescriptionStr;
    }

    public  String getVendorItemNoAndDescriptionStr(){
        return   this.getVendorItemNoStr() + " " + this.getVendorItemDescriptionStr();
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

    public cArticleImage articleImage;

    public static List<cIntakeorderMATSummaryLine> allIntakeorderMATSummaryLinesObl;
    private List<cIntakeorderMATLine> MATLinesObl;

    public List<cIntakeorderMATLine> MATLinestoShowObl() {

        List<cIntakeorderMATLine> resultObl = new ArrayList<>();
        List<String> scannedBINSObl = new ArrayList<>();

        if (this.MATLinesObl == null || this.MATLinesObl.size() == 0) {
            return  resultObl;
        }


        if (this.MATLinesObl.size() == 1) {
            return  this.MATLinesObl;
        }

        //Loop through all lines, so we can make a list of scanned locations
        for (cIntakeorderMATLine intakeorderMATLine : this.MATLinesObl) {

            if (!intakeorderMATLine.getBinCodeHandledStr().isEmpty()) {
                scannedBINSObl.add((intakeorderMATLine.getBinCodeHandledStr()));
            }
        }

        //Loop a second time, so we can build the list
        for (cIntakeorderMATLine intakeorderMATLine : this.MATLinesObl) {


            //If the quantity is zero and the binlist containt this bin, we only show the scanned line
            if (intakeorderMATLine.getQuantityHandledDbl() ==0 && scannedBINSObl.contains(intakeorderMATLine.getBinCodeStr())) {
                continue;
            }

            //Add the line
            resultObl.add((intakeorderMATLine));

        }

        return  resultObl;


    }

    public List<cIntakeorderBarcode> barcodesObl(){

        List<cIntakeorderBarcode> resultObl = new ArrayList<>();

        for (cIntakeorderMATLine intakeorderMATLine : MATLinesObl) {

           for (cIntakeorderBarcode intakeorderBarcode : intakeorderMATLine.barcodesObl) {

               if (! resultObl.contains(intakeorderBarcode)) {
                   resultObl.add(intakeorderBarcode);
               }
           }
        }
        return  resultObl;
    }

    public static Double  totalItems() {

        Double resultDbl = 0.0;

        for (cIntakeorderMATSummaryLine intakeorderMATSummaryLine : cIntakeorderMATSummaryLine.allIntakeorderMATSummaryLinesObl) {
            resultDbl += intakeorderMATSummaryLine.getQuantityDbl();
        }

        return  resultDbl;

    }

    public static Double  totalItemsHandled() {

        Double resultDbl = 0.0;

        for (cIntakeorderMATSummaryLine intakeorderMATSummaryLine : cIntakeorderMATSummaryLine.allIntakeorderMATSummaryLinesObl) {
            resultDbl += intakeorderMATSummaryLine.getQuantityHandledDbl();
        }

        return  resultDbl;

    }

    public static Double  totalItemsDifference() {

        Double resultDbl = 0.0;

        if (cIntakeorderMATSummaryLine.totalItems().equals(cIntakeorderMATSummaryLine.totalItemsHandled())) {
            resultDbl  = 0.0;
            return resultDbl;
        }

        if (cIntakeorderMATSummaryLine.totalItemsHandled() > cIntakeorderMATSummaryLine.totalItems()) {
            resultDbl  =   cIntakeorderMATSummaryLine.totalItemsHandled() - cIntakeorderMATSummaryLine.totalItems();
            return  resultDbl;
        }

        if (cIntakeorderMATSummaryLine.totalItems() > cIntakeorderMATSummaryLine.totalItemsHandled()) {
            resultDbl  =   cIntakeorderMATSummaryLine.totalItems() - cIntakeorderMATSummaryLine.totalItemsHandled();
            return  resultDbl;
        }

        return  resultDbl;

    }

    public static cIntakeorderMATSummaryLine currentIntakeorderMATSummaryLine;

    private static cIntakeorderMATSummaryLineAdapter gSummaryLinesAdapter;
    public static cIntakeorderMATSummaryLineAdapter getSummaryLinesAdapter() {
        if (gSummaryLinesAdapter == null) {
            gSummaryLinesAdapter = new cIntakeorderMATSummaryLineAdapter();
        }
        return gSummaryLinesAdapter;
    }

    //End Region Public Properties

    //Region Constructor

    public cIntakeorderMATSummaryLine(String pvItemNoStr, String pvVariantCodeStr, String pvSourceNoStr) {
        this.itemNoStr = pvItemNoStr;
        this.variantCodeStr = pvVariantCodeStr;
        this.sourceNoStr = pvSourceNoStr;

        this.quantityDbl = 0.0;
        this.quantityHandledDbl = 0.0;

        this.MATLinesObl = new ArrayList<>();

    }

    //End Region Constructor

    //Region Public Methods

    public static void pAddSummaryLine(cIntakeorderMATSummaryLine pvIntakeorderMATSummaryLine) {

        if (cIntakeorderMATSummaryLine.allIntakeorderMATSummaryLinesObl == null) {
            cIntakeorderMATSummaryLine.allIntakeorderMATSummaryLinesObl = new ArrayList<>();
        }

        cIntakeorderMATSummaryLine.allIntakeorderMATSummaryLinesObl.add(pvIntakeorderMATSummaryLine);

    }

    public static cIntakeorderMATSummaryLine pGetSummaryLine (String pvItemNoStr, String pvVariantCodeStr, String pvSourceNoStr){

        if (cIntakeorderMATSummaryLine.allIntakeorderMATSummaryLinesObl  == null || cIntakeorderMATSummaryLine.allIntakeorderMATSummaryLinesObl .size() == 0) {
            return  null;
        }

        for (cIntakeorderMATSummaryLine intakeorderMATSummaryLine : cIntakeorderMATSummaryLine.allIntakeorderMATSummaryLinesObl ) {
            if (intakeorderMATSummaryLine.getItemNoStr().equalsIgnoreCase(pvItemNoStr) &&
                intakeorderMATSummaryLine.getVariantCodeStr().equalsIgnoreCase(pvVariantCodeStr) &&
                intakeorderMATSummaryLine.getSourceNoStr().equalsIgnoreCase(pvSourceNoStr)) {
                return  intakeorderMATSummaryLine;
            }
        }

        return  null;

    }

    public void pAddMATLine(cIntakeorderMATLine pvMATLine){

        this.MATLinesObl.add((pvMATLine));

        this.descriptionStr = pvMATLine.getDescriptionStr();
        this.description2Str = pvMATLine.getDescription2Str();
        this.binCodeStr = pvMATLine.getBinCodeStr();

        this.vendorItemNo = pvMATLine.getVendorItemNoStr();
        this.vendorItemDescriptionStr = pvMATLine.getVendorItemDescriptionStr();

        this.extraField1Str = pvMATLine.getExtraField1Str();
        this.extraField2Str = pvMATLine.getExtraField2Str();
        this.extraField3Str = pvMATLine.getExtraField3Str();
        this.extraField4Str = pvMATLine.getExtraField4Str();
        this.extraField5Str = pvMATLine.getExtraField5Str();
        this.extraField6Str = pvMATLine.getExtraField6Str();
        this.extraField7Str = pvMATLine.getExtraField7Str();
        this.extraField8Str = pvMATLine.getExtraField8Str();

    }

    public cResult pSummaryLineBusyRst(){

        cResult result = new cResult();
        result.resultBln = false;

        for (cIntakeorderMATLine intakeorderMATLine : cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.MATLinesObl) {
            if ( intakeorderMATLine.pGetBarcodesObl() == null ||
                intakeorderMATLine.pGetBarcodesObl().size() == 0) {
                result.resultBln = false;
                result.pAddErrorMessage(cAppExtension.context.getString(R.string.no_barcodes_availabe_for_this_line));
                return result;
            }

        }

        result.resultBln = true;

        return  result;

    }

    public cIntakeorderBarcode pGetBarcode(cBarcodeScan pvBarcodeScan){

        if (pvBarcodeScan.getBarcodeOriginalStr().isEmpty()) {
            return  null;
        }


        if (this.barcodesObl() == null || this.barcodesObl().size() == 0) {
            return  null;
        }

        for (cIntakeorderBarcode intakeorderBarcode : this.barcodesObl()) {
            if (intakeorderBarcode.getBarcodeStr().equalsIgnoreCase(pvBarcodeScan.getBarcodeOriginalStr()) ||
                    intakeorderBarcode.getBarcodeWithoutCheckDigitStr().equalsIgnoreCase(pvBarcodeScan.getBarcodeFormattedStr())) {
                return  intakeorderBarcode;
            }
        }

        return  null;

    }

    public Double pGetQuantityToHandleDbl(){

        Double resultDbl = this.getQuantityDbl();

        if (this.MATLinesObl == null || this.MATLinesObl.size() == 0) {
            return resultDbl;
        }

        for (cIntakeorderMATLine intakeorderMATLine : this.MATLinesObl) {
            resultDbl -= intakeorderMATLine.getQuantityHandledDbl();
        }

        return  resultDbl;

    }


    public cIntakeorderMATLine pGetLineForBinCode(cBranchBin pvBranchBin){

        if (this.MATLinesObl == null || this.MATLinesObl.size() == 0) {
            return null;
        }

        for (cIntakeorderMATLine intakeorderMATLine : this.MATLinesObl) {

            if (intakeorderMATLine.getBinCodeStr().isEmpty() || intakeorderMATLine.getBinCodeStr().equalsIgnoreCase(pvBranchBin.getBinCodeStr())) {
                return  intakeorderMATLine;
            }

        }

        return  null;

    }

    public boolean pItemVariantHandledBln(String pvBinCodeStr, List<cIntakeorderBarcode> pvScannedBarcodesObl ) {

        List <cIntakeorderMATLine> matchedLinesObl = new ArrayList<>();
        cIntakeorderMATLine matchedLine = null;
        Double quantityScannedDbl = 0.0;

        cWebresult WebResult;

        if (this.MATLinesObl == null || this.MATLinesObl.size() == 0 )  {
            return  false;
        }

        if (this.MATLinesObl.size() == 1) {
            matchedLinesObl.add(this.MATLinesObl.get(0));
        }
        else  {
            //Loop through all lines and look for a match
            for (cIntakeorderMATLine intakeorderMATLine : this.MATLinesObl) {

                //We have a match on BIN/the BIN is empty
                if (intakeorderMATLine.getBinCodeHandledStr().equalsIgnoreCase(pvBinCodeStr) || intakeorderMATLine.getBinCodeHandledStr().isEmpty()) {
                    matchedLinesObl.add(intakeorderMATLine);
                }
            }
        }

        //Handle matched lines
        switch(matchedLinesObl.size()) {

            //We have no match, so this will result in a new line
            case 0:

                WebResult =  cIntakeorderMATLine.getIntakeorderMATLineViewModel().pCreateLineViaWebserviceWrs(pvBinCodeStr,pvScannedBarcodesObl);
                if (WebResult.getResultBln() && WebResult.getSuccessBln()){

                    if (this.getBinCodeStr().isEmpty()) {
                        this.binCodeStr = pvBinCodeStr;
                    }

                    this.quantityHandledDbl += quantityScannedDbl;

                    //Add new line so the quanity gets raised again
                    cIntakeorderMATLine intakeorderMATLine = new cIntakeorderMATLine(WebResult.getResultDtt().get(0));
                    intakeorderMATLine.pInsertInDatabaseBln();
                    this.pAddMATLine(intakeorderMATLine);
                    return  true;
                }
                else {
                    cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_INTAKEITEMHANLED);
                    return  false;
                }

                //We have a match, so we can handle one specific line
            case 1:
                matchedLine = matchedLinesObl.get(0);
                break;

            //We have multiple possible matches, so we still have to match the line
            default:

                //Loop through the matched lines, untill we have a line with a bincode
                for (cIntakeorderMATLine intakeorderMATLine : matchedLinesObl) {

                    matchedLine = intakeorderMATLine;
                    if (intakeorderMATLine.getBinCodeHandledStr().isEmpty()) {
                        break;
                    }
                }
        }
        //We have a match, so we can handle this line
        if (matchedLine != null) {

            cIntakeorderMATLine.currentIntakeorderMATLine = matchedLine;

            //loop through barcodes to calculate scanned quantity
            for (cIntakeorderBarcode intakeorderBarcode : pvScannedBarcodesObl) {
                cIntakeorderMATLine.currentIntakeorderMATLine.pAddOrUpdateLineBarcodeBln(intakeorderBarcode);
                quantityScannedDbl += intakeorderBarcode.getQuantityPerUnitOfMeasureDbl();
            }


            //Update the MAT line
            cIntakeorderMATLine.currentIntakeorderMATLine.binCodeHandledStr = pvBinCodeStr;
            cIntakeorderMATLine.currentIntakeorderMATLine.quantityHandledDbl += quantityScannedDbl;
            cIntakeorderMATLine.currentIntakeorderMATLine.pHandledIndatabaseBln();

            //If quantity is equal or bigger then asked, the line is finished
            if (this.pQuantityReachedBln(pvScannedBarcodesObl)) {

                WebResult =  cIntakeorderMATLine.getIntakeorderMATLineViewModel().pLineHandledViaWebserviceWrs();
                if (WebResult.getResultBln() && WebResult.getSuccessBln()){

                    if (this.getBinCodeStr().isEmpty()) {
                        this.binCodeStr = pvBinCodeStr;
                    }

                    this.quantityHandledDbl += quantityScannedDbl;

                    return  true;
                }
                else {

                    //Undo updated MAT line + own quantity
                    cIntakeorderMATLine.currentIntakeorderMATLine.binCodeHandledStr = "";
                    cIntakeorderMATLine.currentIntakeorderMATLine.quantityHandledDbl -= quantityScannedDbl;
                    cIntakeorderMATLine.currentIntakeorderMATLine.pHandledIndatabaseBln();
                    cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_INTAKELINEMATHANDLED);
                    return  false;
                }

            }

            else {
                //if quantity is lower, we have to split the line
                WebResult =  cIntakeorderMATLine.getIntakeorderMATLineViewModel().pLineSplitViaWebserviceWrs();
                if (WebResult.getResultBln() && WebResult.getSuccessBln() ){


                    //Set the quantity to the quantity scanned
                    cIntakeorderMATLine.currentIntakeorderMATLine.binCodeHandledStr = pvBinCodeStr;
                    cIntakeorderMATLine.currentIntakeorderMATLine.quantityDbl = quantityScannedDbl;
                    cIntakeorderMATLine.currentIntakeorderMATLine.quantityHandledDbl = quantityScannedDbl;
                    cIntakeorderMATLine.currentIntakeorderMATLine.pHandledIndatabaseBln();

                    //Add new line so the quanity gets raised again
                    cIntakeorderMATLine intakeorderMATLine = new cIntakeorderMATLine(WebResult.getResultDtt().get(0));
                    intakeorderMATLine.pInsertInDatabaseBln();
                    this.pAddMATLine(intakeorderMATLine);
                    return  true;
                }
                else {
                    cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_INTAKELINEMATHANDLEDPART);
                    return  false;
                }
            }
        }


        //We have no match, so we have to add a line
        return  true;
    }


    public boolean pGetArticleImageBln(){

        if (this.articleImage != null) {
            return  true;
        }

        this.articleImage = cArticleImage.pGetArticleImageByItemNoAndVariantCode(this.getItemNoStr(),this.getVariantCodeStr());
        if (this.articleImage != null){
            return  true;
        }

        cWebresult Webresult;

        Webresult = cArticleImage.getArticleImageViewModel().pGetArticleImageFromWebserviceWrs(this.getItemNoStr(),this.getVariantCodeStr());
        if (!Webresult.getSuccessBln() || !Webresult.getResultBln()) {
            return  false;
        }

        if (Webresult.getResultDtt().size() == 1) {
            cArticleImage articleImage = new cArticleImage(Webresult.getResultDtt().get(0));
            articleImage.pInsertInDatabaseBln();
            this.articleImage = articleImage;
            return true;
        }

        return  false;

    }

    public Boolean pQuantityReachedBln(List<cIntakeorderBarcode> pvScannedBarcodesObl){

        Double quantityTotalDbl = this.quantityHandledDbl;

        for (cIntakeorderBarcode intakeorderBarcode : pvScannedBarcodesObl) {
            quantityTotalDbl += intakeorderBarcode.getQuantityPerUnitOfMeasureDbl();
        }

        return quantityTotalDbl >= this.getQuantityDbl();

    }


    //End Region Public Methods

    //Region Private Methods


    //End Region Private Methods


    //End Region Constructor



}
