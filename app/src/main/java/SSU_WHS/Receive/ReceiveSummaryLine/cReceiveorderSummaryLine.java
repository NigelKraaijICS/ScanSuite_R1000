package SSU_WHS.Receive.ReceiveSummaryLine;

import androidx.lifecycle.ViewModelProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cResult;
import ICS.Utils.cText;
import ICS.Weberror.cWeberror;
import ICS.cAppExtension;
import SSU_WHS.Basics.ArticleImages.cArticleImage;
import SSU_WHS.Basics.ArticleImages.cArticleImageViewModel;
import SSU_WHS.Basics.Settings.cSetting;
import SSU_WHS.General.cDatabase;
import SSU_WHS.Intake.IntakeorderBarcodes.cIntakeorderBarcode;
import SSU_WHS.Intake.Intakeorders.cIntakeorder;
import SSU_WHS.Receive.ReceiveLines.cReceiveorderLine;
import SSU_WHS.Receive.ReceiveLines.cReceiveorderLineViewModel;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebserviceDefinitions;
import nl.icsvertex.scansuite.R;


public class cReceiveorderSummaryLine {

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

    private Double quantityDbl;
    public Double getQuantityDbl()
    {
        return this.quantityDbl;
    }

    private Double quantityHandledDbl;
    public Double getQuantityHandledDbl()
    {
        Double resultDbl =  0.0;

        if (this.receiveLinesObl == null || this.receiveLinesObl.size() == 0) {
            return this.getQuantityExportedDbl();
        }

        for (cReceiveorderLine receiveorderLine : this.receiveLinesObl) {
            resultDbl += receiveorderLine.getQuantityHandledDbl();
        }

        return resultDbl;
    }

    public  Double getAllowedQuantityDbl(){
        return this.getQuantityDbl() +  this.getQuantityDbl() * (cSetting.RECEIVE_EXTRA_PIECES_PERCENTAGE() / 100);
    }

    private Double quantityExportedDbl;
    private Double getQuantityExportedDbl()
    {
        return this.quantityExportedDbl;
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

    private String binCodeStr;
    public String getBinCodeStr() {

        if (this.binCodeStr.isEmpty()) {
            this.binCodeStr = cAppExtension.activity.getString(R.string.message_unknown);
        }

        return binCodeStr;
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

    public static List<cReceiveorderSummaryLine> allReceiveorderSummaryLinesObl;
    public List<cReceiveorderLine> receiveLinesObl;

    public  List<cReceiveorderLine> receivedLinesReversedObl(){

        List<cReceiveorderLine> resultObl = new ArrayList<>();

        if (this.receiveLinesObl == null || this.receiveLinesObl.size() == 0) {
            return resultObl;
        }

        resultObl.addAll(this.receiveLinesObl);
        Collections.reverse(resultObl);
        return  resultObl;

    }

    public List<cIntakeorderBarcode> barcodesObl(){

        List<cIntakeorderBarcode> resultObl = new ArrayList<>();

        for (cIntakeorderBarcode intakeorderBarcode : cIntakeorder.currentIntakeOrder.barcodesObl()) {
            if (intakeorderBarcode.getItemNoStr().equalsIgnoreCase(this.getItemNoStr()) && intakeorderBarcode.getVariantCodeStr().equalsIgnoreCase(this.getVariantCodeStr())) {
                resultObl.add(intakeorderBarcode);
            }
        }

        return  resultObl;
    }

    public static Double  totalItems() {

        Double resultDbl = 0.0;

        for (cReceiveorderSummaryLine receiveorderSummaryLine : cReceiveorderSummaryLine.allReceiveorderSummaryLinesObl) {
            resultDbl += receiveorderSummaryLine.getQuantityDbl();
        }

        return  resultDbl;

    }

    public static Double  totalItemsHandled() {

        Double resultDbl = 0.0;

        for (cReceiveorderSummaryLine receiveorderSummaryLine : cReceiveorderSummaryLine.allReceiveorderSummaryLinesObl) {
            resultDbl += receiveorderSummaryLine.getQuantityHandledDbl();
        }

        return  resultDbl;

    }

    public static Double  totalItemsDifference() {

        Double resultDbl = 0.0;

        if (cReceiveorderSummaryLine.totalItems().equals(cReceiveorderSummaryLine.totalItemsHandled())) {
            resultDbl  = 0.0;
            return resultDbl;
        }

        if (cReceiveorderSummaryLine.totalItemsHandled() > cReceiveorderSummaryLine.totalItems()) {
            resultDbl  =   cReceiveorderSummaryLine.totalItemsHandled() - cReceiveorderSummaryLine.totalItems();
            return  resultDbl;
        }

        if (cReceiveorderSummaryLine.totalItems() > cReceiveorderSummaryLine.totalItemsHandled()) {
            resultDbl  =   cReceiveorderSummaryLine.totalItems() - cReceiveorderSummaryLine.totalItemsHandled();
            return  resultDbl;
        }

        return  resultDbl;

    }

    public cArticleImage articleImage;
    public static cReceiveorderSummaryLine currentReceiveorderSummaryLine;


    private cReceiveorderLineViewModel getReceiveorderLineViewModel() {
        return new ViewModelProvider(cAppExtension.fragmentActivity).get(cReceiveorderLineViewModel.class);
    }




    //End Region Public Properties

    //Region Constructor

    public cReceiveorderSummaryLine(JSONObject jsonObject) {
        try {
            this.itemNoStr = jsonObject.getString(cDatabase.ITEMNO_NAMESTR);

            this.variantCodeStr = jsonObject.getString(cDatabase.VARIANTCODE_NAMESTR);
            this.descriptionStr = jsonObject.getString(cDatabase.DESCRIPTION_NAMESTR);
            this.description2Str = jsonObject.getString(cDatabase.DESCRIPTION2_NAMESTR);
            this.vendorItemNo = jsonObject.getString(cDatabase.VENDORITEMNO_NAMESTR);
            this.vendorItemDescriptionStr = jsonObject.getString(cDatabase.VENDORITEMDESCRIPTION_NAMESTR);


            this.quantityDbl = cText.pStringToDoubleDbl(jsonObject.getString(cDatabase.QUANTITYTAKE_NAMESTR));
            this.quantityHandledDbl = 0.0;
            this.quantityExportedDbl = cText.pStringToDoubleDbl(jsonObject.getString(cDatabase.QUANTITYTAKEEXPORTED_NAMESTR));

            this.binCodeStr = jsonObject.getString(cDatabase.BINCODE_NAMESTR);
            this.extraField1Str = "";
            this.extraField2Str = "";
            this.extraField3Str = "";
            this.extraField4Str = "";
            this.extraField5Str = "";
            this.extraField6Str = "";
            this.extraField7Str = "";
            this.extraField8Str = "";

            this.receiveLinesObl = new ArrayList<>();

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    //End Region Constructor

    //Region Public Methods

    public static cReceiveorderSummaryLine pGetSummaryLineWithItemNoAndVariantCode(String pvItemNoStr, String pvVariantCodeStr) {


        if (cReceiveorderSummaryLine.allReceiveorderSummaryLinesObl == null || cReceiveorderSummaryLine.allReceiveorderSummaryLinesObl.size() ==0 ) {
            return null;
        }

        for (cReceiveorderSummaryLine receiveorderSummaryLine : cReceiveorderSummaryLine.allReceiveorderSummaryLinesObl) {
            if (receiveorderSummaryLine.getItemNoStr().equalsIgnoreCase(pvItemNoStr) && receiveorderSummaryLine.getVariantCodeStr().equalsIgnoreCase(pvVariantCodeStr)) {
                return  receiveorderSummaryLine;
            }
        }

        return  null;

    }

    public static void pAddSummaryLine(cReceiveorderSummaryLine pReceiveorderSummaryLine) {

        if (cReceiveorderSummaryLine.allReceiveorderSummaryLinesObl == null) {
            cReceiveorderSummaryLine.allReceiveorderSummaryLinesObl = new ArrayList<>();
        }

        cReceiveorderSummaryLine.allReceiveorderSummaryLinesObl.add(pReceiveorderSummaryLine);

    }

    public static cReceiveorderSummaryLine pGetSummaryLine (String pvItemNoStr, String pvVariantCodeStr){

        if (cReceiveorderSummaryLine.allReceiveorderSummaryLinesObl == null || cReceiveorderSummaryLine.allReceiveorderSummaryLinesObl.size() == 0) {
            return  null;
        }

        for (cReceiveorderSummaryLine intakeorderMATSummaryLine : cReceiveorderSummaryLine.allReceiveorderSummaryLinesObl) {
            if (intakeorderMATSummaryLine.getItemNoStr().equalsIgnoreCase(pvItemNoStr) &&
                intakeorderMATSummaryLine.getVariantCodeStr().equalsIgnoreCase(pvVariantCodeStr))  {
                return  intakeorderMATSummaryLine;
            }
        }

        return  null;

    }

    public void pAddLine(cReceiveorderLine pvReceiveorderLine){

        this.receiveLinesObl.add((pvReceiveorderLine));
        this.extraField1Str = "";
        this.extraField2Str = "";
        this.extraField3Str = "";
        this.extraField4Str = "";
        this.extraField5Str = "";
        this.extraField6Str = "";
        this.extraField7Str = "";
        this.extraField8Str = "";

        if (pvReceiveorderLine.getExtraField1Str() != null) {
            this.extraField1Str = pvReceiveorderLine.getExtraField1Str();
        }

        if (pvReceiveorderLine.getExtraField2Str() != null) {
            this.extraField2Str = pvReceiveorderLine.getExtraField2Str();
        }

        if (pvReceiveorderLine.getExtraField3Str() != null) {
            this.extraField3Str = pvReceiveorderLine.getExtraField3Str();
        }
        if (pvReceiveorderLine.getExtraField4Str() != null) {
            this.extraField4Str = pvReceiveorderLine.getExtraField4Str();
        }
        if (pvReceiveorderLine.getExtraField5Str() != null) {
            this.extraField5Str = pvReceiveorderLine.getExtraField5Str();
        }
        if (pvReceiveorderLine.getExtraField6Str() != null) {
            this.extraField6Str = pvReceiveorderLine.getExtraField6Str();
        }
        if (pvReceiveorderLine.getExtraField7Str() != null) {
            this.extraField7Str = pvReceiveorderLine.getExtraField7Str();
        }
        if (pvReceiveorderLine.getExtraField8Str() != null) {
            this.extraField8Str = pvReceiveorderLine.getExtraField8Str();
        }

    }

    public cResult pSummaryLineBusyRst(){

        cResult result = new cResult();
        result.resultBln = false;

        for (cReceiveorderLine receiveorderLine : cReceiveorderSummaryLine.currentReceiveorderSummaryLine.receiveLinesObl) {
            if ( receiveorderLine.pGetBarcodesObl() == null ||
                    receiveorderLine.pGetBarcodesObl().size() == 0) {
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

        if (this.receiveLinesObl == null || this.receiveLinesObl.size() == 0) {
            return resultDbl;
        }

        for (cReceiveorderLine receiveorderLine : this.receiveLinesObl) {
            resultDbl -= receiveorderLine.getQuantityHandledDbl();
        }

        if (resultDbl <0) {
            resultDbl = (double) 0;
        }

        return  resultDbl;

    }

    public boolean pItemVariantHandledBln(List<cIntakeorderBarcode> pvScannedBarcodesObl ) {

        Double quantityScannedDbl = 0.0;

        for (cIntakeorderBarcode intakeorderBarcode : pvScannedBarcodesObl) {
            quantityScannedDbl += intakeorderBarcode.quantityHandledDbl;
        }

        cWebresult WebResult;
        WebResult =  this.getReceiveorderLineViewModel().pLineHandledViaWebserviceWrs(pvScannedBarcodesObl);
        if (WebResult.getResultBln() && WebResult.getSuccessBln()){
            this.quantityHandledDbl += quantityScannedDbl;

            //Add new line so the quanity gets raised again
            cReceiveorderLine receiveorderLine = new cReceiveorderLine(WebResult.getResultDtt().get(0), true, WebResult.getResultLng().intValue());
            receiveorderLine.quantityHandledDbl = quantityScannedDbl;
            receiveorderLine.pInsertInDatabaseBln();
            this.pAddLine(receiveorderLine);
            return  true;
        }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_RECEIVEINTAKEITEM);
            return  false;
        }
    }

    public Boolean pQuantityReachedBln(List<cIntakeorderBarcode> pvScannedBarcodesObl){

        Double quantityTotalDbl = this.quantityHandledDbl;

        for (cIntakeorderBarcode intakeorderBarcode : pvScannedBarcodesObl) {
            quantityTotalDbl += intakeorderBarcode.getQuantityPerUnitOfMeasureDbl();
        }

        return quantityTotalDbl >= this.getQuantityDbl();

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

        cArticleImageViewModel articleImageViewModel = new ViewModelProvider(cAppExtension.fragmentActivity).get(cArticleImageViewModel.class);
        Webresult = articleImageViewModel.pGetArticleImageFromWebserviceWrs(this.getItemNoStr(),this.getVariantCodeStr());
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

    public cResult pResetRst(){

        //nit the result
        cResult result = new cResult();
        result.resultBln = true;

        List<cReceiveorderLine> linesToResetObl = new ArrayList<>(this.receiveLinesObl);

            // Reset all lines and details via webservice
            for (cReceiveorderLine receiveorderLine : linesToResetObl) {

                cReceiveorderLine.currentReceiveorderLine = receiveorderLine;
                cReceiveorderLine.currentReceiveorderLine.pResetBln();
                cReceiveorderLine.currentReceiveorderLine= null;
            }

            this.receiveLinesObl.clear();

            return  result;
        }
    }


    //End Region Public Methods

    //Region Private Methods


    //End Region Private Methods


    //End Region Constructor



