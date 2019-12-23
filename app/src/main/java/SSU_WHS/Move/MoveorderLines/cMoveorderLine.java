package SSU_WHS.Move.MoveorderLines;

import androidx.lifecycle.ViewModelProviders;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ICS.Utils.cDateAndTime;
import ICS.Utils.cResult;
import ICS.Weberror.cWeberror;
import ICS.cAppExtension;
import SSU_WHS.Basics.ArticleImages.cArticleImage;
import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.Move.MoveorderBarcodes.cMoveorderBarcode;
import SSU_WHS.Move.MoveorderLineBarcodes.cMoveorderLineBarcode;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebserviceDefinitions;
import nl.icsvertex.scansuite.R;


public class cMoveorderLine {
    public cMoveorderLineEntity moveorderLineEntity;
    public boolean indatabaseBln;

    public static List<cMoveorderLine> allLinesObl;
    public static cMoveorderLine currentMoveOrderLine;

    public List<cMoveorderLineBarcode> lineBarcodesObl;

    public static cMoveorderLineViewModel gMoveorderLineViewModel;
    public static cMoveorderLineViewModel getMoveorderLineViewModel() {
        if (gMoveorderLineViewModel == null) {
            gMoveorderLineViewModel = ViewModelProviders.of(cAppExtension.fragmentActivity ).get(cMoveorderLineViewModel.class);
        }
        return gMoveorderLineViewModel;
    }

    public static cMoveorderLineAdapter gMoveorderLineAdapter;
    public static cMoveorderLineAdapter getMoveorderLineAdapter() {
        if (gMoveorderLineAdapter == null) {
            gMoveorderLineAdapter = new cMoveorderLineAdapter();
        }
        return gMoveorderLineAdapter;
    }


    //Region Public Properties
    public Integer recordIDInt;
    public Integer getRecordIDInt() {
        return recordIDInt;
    }

    public int lineNoInt;
    public int getLineNoInt() {
        return lineNoInt;
    }

    public String itemNoStr;
    public String getItemNoStr() {
        return itemNoStr;
    }

    public String variantCodeStr;
    public String getVariantCodeStr() {
        return variantCodeStr;
    }

    public String descriptionStr;
    public String getDescriptionStr() {
        return descriptionStr;
    }

    public String description2Str;
    public String getDescription2Str() {
        return description2Str;
    }

    public String binCodeStr;
    public String getBinCodeStr() {
        return binCodeStr;
    }

    public String containerStr;
    public String getContainerStr() {
        return containerStr;
    }

    public String containerTypeStr;
    public String getContainerTypeStr() {
        return containerTypeStr;
    }

    public String containerInputStr;
    public String getContainerInputStr() {
        return containerInputStr;
    }

    public String containerHandledStr;
    public String getContainerHandledStr() {
        return containerHandledStr;
    }

    public Double quantityDbl;
    public Double getQuantityDbl() {
        return quantityDbl;
    }

    public Double quantityHandledDbl;
    public Double getQuantityHandledDbl() {
        return quantityHandledDbl;
    }

    public Double quantityRejectedDbl;
    public Double getQuantityRejectedDbl() {
        return quantityRejectedDbl;
    }

    public String handledTimeStampStr;
    public String getHandledTimeStampStr() { return handledTimeStampStr; }

    public String sourceNoStr;
    public String getSourceNoStr() { return sourceNoStr; }

    public String destinationNoStr;
    public String getDestinationNoStr() { return destinationNoStr; }

    public String isPartOfMultiLineorderNoStr;
    public String getIsPartOfMultiLineorderNoStr() { return isPartOfMultiLineorderNoStr; }

    public int shippingAdviceInt;
    public int getShippingAdviceInt() { return shippingAdviceInt; }

    public int sortingSequenceNoInt;
    public int getSortingSequenceNoInt() { return sortingSequenceNoInt; }

    public String processingSequenceStr;
    public String getProcessingSequenceStr() { return processingSequenceStr; }

    public String storeSourceOrderStr;
    public String getStoreSourceOrderStr() { return storeSourceOrderStr; }

    public String storageBinCodeStr;
    public String getStorageBinCodeStr() { return storageBinCodeStr; }

    public String vendorItemNoStr;
    public String getVendorItemNoStr() { return vendorItemNoStr; }

    public String vendorItemDescriptionStr;
    public String getVendorItemDescriptionStr() { return vendorItemDescriptionStr; }

    public String printDocumentsStr;
    public String getPrintDocumentsStr() { return printDocumentsStr; }

    public int statusInt;
    public int getStatusInt() { return statusInt; }

    public String actionTypeCodeStr;
    public String getActionTypeCodeStr() { return actionTypeCodeStr; }

    public int localStatusInt;
    public int getLocalStatusInt() { return localStatusInt; }

    public String extraField1Str;
    public String getExtraField1Str() {
        return extraField1Str;
    }

    public String extraField2Str;
    public String getExtraField2Str() {
        return extraField2Str;
    }

    public String extraField3Str;
    public String getExtraField3Str() {
        return extraField3Str;
    }

    public String extraField4Str;
    public String getExtraField4Str() {
        return extraField4Str;
    }

    public String extraField5Str;
    public String getExtraField5Str() {
        return extraField5Str;
    }

    public String extraField6Str;
    public String getExtraField6Str() {
        return extraField6Str;
    }

    public String extraField7Str;
    public String getExtraField7Str() {
        return extraField7Str;
    }

    public String extraField8Str;
    public String getExtraField8Str() {
        return extraField8Str;
    }

    public cArticleImage articleImage;

    public  List<cMoveorderBarcode> barcodesObl;
    //End Region Public Properties

    //Region Constructor
    public cMoveorderLine(JSONObject pvJsonObject) {
        this.moveorderLineEntity = new cMoveorderLineEntity(pvJsonObject);
        this.lineNoInt = this.moveorderLineEntity.getLineNoInt();
        this.itemNoStr = this.moveorderLineEntity.getItemNoStr();
        this.variantCodeStr = this.moveorderLineEntity.getVariantCodeStr();
        this.descriptionStr = this.moveorderLineEntity.getDescriptionStr();
        this.description2Str = this.moveorderLineEntity.getDescription2Str();
        this.binCodeStr = this.moveorderLineEntity.getBincodeStr();
        this.containerStr = this.moveorderLineEntity.getContainerStr();
        this.containerTypeStr = this.moveorderLineEntity.getContainerTypeStr();
        this.containerInputStr = this.moveorderLineEntity.getContainerInput();
        this.containerHandledStr = this.moveorderLineEntity.getContainerHandled();
        this.quantityDbl = this.moveorderLineEntity.getQuantityDbl();
        this.quantityHandledDbl = this.moveorderLineEntity.getQuantityhandledDbl();
        this.quantityRejectedDbl = this.moveorderLineEntity.getQuantityrejectedDbl();
        this.handledTimeStampStr = this.moveorderLineEntity.getHandledtimestampStr();
        this.sourceNoStr = this.moveorderLineEntity.getSourcenoStr();
        this.destinationNoStr = this.moveorderLineEntity.getDestinationNoStr();
        this.isPartOfMultiLineorderNoStr = this.moveorderLineEntity.getIspartofMultilineorderStr();
        this.shippingAdviceInt = this.moveorderLineEntity.getShippingadviceInt();
        this.sortingSequenceNoInt = this.moveorderLineEntity.getSortingsequencenoInt();
        this.processingSequenceStr = this.moveorderLineEntity.getProcessingSequenceNoStr();
        this.storeSourceOrderStr = this.moveorderLineEntity.getStoresourceOrderStr();
        this.storageBinCodeStr = this.moveorderLineEntity.getStoragebincodeStr();
        this.vendorItemNoStr = this.moveorderLineEntity.getVendorItemNoStr();
        this.vendorItemDescriptionStr = this.moveorderLineEntity.getVendorItemDescriptionStr();
        this.printDocumentsStr = this.moveorderLineEntity.getPrintdocumentsStr();
        this.statusInt = this.moveorderLineEntity.getStatusInt();
        this.actionTypeCodeStr = this.moveorderLineEntity.getActiontypecodeStr();
        this.extraField1Str =  this.moveorderLineEntity.getExtraField1Str();
        this.extraField2Str = this.moveorderLineEntity.getExtraField2Str();
        this.extraField3Str =  this.moveorderLineEntity.getExtraField3Str();
        this.extraField4Str =  this.moveorderLineEntity.getExtraField4Str();
        this.extraField5Str =  this.moveorderLineEntity.getExtraField5Str();
        this.extraField6Str =  this.moveorderLineEntity.getExtraField6Str();
        this.extraField7Str =  this.moveorderLineEntity.getExtraField7Str();
        this.extraField8Str =  this.moveorderLineEntity.getExtraField8Str();
    }

    public cMoveorderLine(cMoveorderLineEntity pvMoveorderLineEntity){
        this.moveorderLineEntity = pvMoveorderLineEntity;
        this.lineNoInt = this.moveorderLineEntity.getLineNoInt();
        this.itemNoStr = this.moveorderLineEntity.getItemNoStr();
        this.variantCodeStr = this.moveorderLineEntity.getVariantCodeStr();
        this.descriptionStr = this.moveorderLineEntity.getDescriptionStr();
        this.description2Str = this.moveorderLineEntity.getDescription2Str();
        this.binCodeStr = this.moveorderLineEntity.getBincodeStr();
        this.containerStr = this.moveorderLineEntity.getContainerStr();
        this.containerTypeStr = this.moveorderLineEntity.getContainerTypeStr();
        this.containerInputStr = this.moveorderLineEntity.getContainerInput();
        this.containerHandledStr = this.moveorderLineEntity.getContainerHandled();
        this.quantityDbl = this.moveorderLineEntity.getQuantityDbl();
        this.quantityHandledDbl = this.moveorderLineEntity.getQuantityhandledDbl();
        this.quantityRejectedDbl = this.moveorderLineEntity.getQuantityrejectedDbl();
        this.handledTimeStampStr = this.moveorderLineEntity.getHandledtimestampStr();
        this.sourceNoStr = this.moveorderLineEntity.getSourcenoStr();
        this.destinationNoStr = this.moveorderLineEntity.getDestinationNoStr();
        this.isPartOfMultiLineorderNoStr = this.moveorderLineEntity.getIspartofMultilineorderStr();
        this.shippingAdviceInt = this.moveorderLineEntity.getShippingadviceInt();
        this.sortingSequenceNoInt = this.moveorderLineEntity.getSortingsequencenoInt();
        this.processingSequenceStr = this.moveorderLineEntity.getProcessingSequenceNoStr();
        this.storeSourceOrderStr = this.moveorderLineEntity.getStoresourceOrderStr();
        this.storageBinCodeStr = this.moveorderLineEntity.getStoragebincodeStr();
        this.vendorItemNoStr = this.moveorderLineEntity.getVendorItemNoStr();
        this.vendorItemDescriptionStr = this.moveorderLineEntity.getVendorItemDescriptionStr();
        this.printDocumentsStr = this.moveorderLineEntity.getPrintdocumentsStr();
        this.statusInt = this.moveorderLineEntity.getStatusInt();
        this.actionTypeCodeStr = this.moveorderLineEntity.getActiontypecodeStr();
        this.extraField1Str =  this.moveorderLineEntity.getExtraField1Str();
        this.extraField2Str = this.moveorderLineEntity.getExtraField2Str();
        this.extraField3Str =  this.moveorderLineEntity.getExtraField3Str();
        this.extraField4Str =  this.moveorderLineEntity.getExtraField4Str();
        this.extraField5Str =  this.moveorderLineEntity.getExtraField5Str();
        this.extraField6Str =  this.moveorderLineEntity.getExtraField6Str();
        this.extraField7Str =  this.moveorderLineEntity.getExtraField7Str();
        this.extraField8Str =  this.moveorderLineEntity.getExtraField8Str();
    }
    //End Region Constructor

    public boolean pInsertInDatabaseBln() {
        cMoveorderLine.getMoveorderLineViewModel().insert(this.moveorderLineEntity);
        this.indatabaseBln = true;

        if (cMoveorderLine.allLinesObl == null){
            cMoveorderLine.allLinesObl = new ArrayList<>();
        }
        cMoveorderLine.allLinesObl.add(this);
        return  true;
    }

    public static boolean pTruncateTableBln(){
        cMoveorderLine.getMoveorderLineViewModel().deleteAll();
        return true;
    }

    public boolean pHandledIndatabaseBln(){


        if (this.mUpdateQuanitityHandled(this.quantityHandledDbl)  == false) {
            return  false;
        }

        if (this.mUpdateLocalStatusBln(cWarehouseorder.PicklineLocalStatusEnu.LOCALSTATUS_DONE_NOTSENT)  == false) {
            return  false;
        }

        this.handledTimeStampStr = cDateAndTime.pGetCurrentDateTimeForWebserviceStr();

        if (this.mUpdateHandledTimeStampBln(this.handledTimeStampStr)  == false) {
            return  false;
        }

        return  true;

    }
    public boolean pErrorSendingBln() {

        return this.mUpdateLocalStatusBln(cWarehouseorder.MovelineLocalStatusEnu.LOCALSTATUS_DONE_ERROR_SENDING);

    }
    public boolean pHandledTakeBln() {

        cWebresult WebResult;
        WebResult =  cMoveorderLine.getMoveorderLineViewModel().pMoveLineHandledTakeMTViaWebserviceWrs();
        if (WebResult.getResultBln() == true && WebResult.getSuccessBln() == true ){

            if(this.mUpdateLocalStatusBln( cWarehouseorder.PicklineLocalStatusEnu.LOCALSTATUS_DONE_SENT) == false) {
                return  false;
            }
            return  true;
        }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_PICKORDERLINE_HANDLED);
            return  false;
        }
    }

    public boolean pCancelIndatabaseBln(){


        if (this.mUpdateQuanitityHandled(this.quantityHandledDbl)  == false) {
            return  false;
        }

        if (this.mUpdateLocalStatusBln(cWarehouseorder.MovelineLocalStatusEnu.LOCALSTATUS_NEW)  == false) {
            return  false;
        }

        this.handledTimeStampStr = "";

        if (this.mUpdateHandledTimeStampBln(this.handledTimeStampStr)  == false) {
            return  false;
        }

        return  true;

    }
    public boolean pMoveItemHandledBln() {
        cWebresult WebResult;
        WebResult =  cMoveorderLine.getMoveorderLineViewModel().pMoveItemHandledViaWebserviceWrs();
        if (WebResult.getResultBln() == true && WebResult.getSuccessBln() == true ){
            if(this.mUpdateLocalStatusBln(cWarehouseorder.MovelineLocalStatusEnu.LOCALSTATUS_DONE_SENT) == false) {
                return  false;
            }
            return  true;
        }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_PICKORDERLINE_HANDLED);
            return  false;
        }
    }

    public boolean pHandledPlaceBln() {

        cWebresult WebResult;
        WebResult =  cMoveorderLine.getMoveorderLineViewModel().pMoveLineHandledPlaceMTViaWebserviceWrs();
        if (WebResult.getResultBln() == true && WebResult.getSuccessBln() == true ){

            if(this.mUpdateLocalStatusBln( cWarehouseorder.PicklineLocalStatusEnu.LOCALSTATUS_DONE_SENT) == false) {
                return  false;
            }
            return  true;
        }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_PICKORDERLINE_HANDLED);
            return  false;
        }
    }

    public boolean pRemoveOrUpdateLineBarcodeBln(){

        //If there are no line barcodes, this should not be possible
        if (this.handledBarcodesObl() == null || this.handledBarcodesObl().size() == 0) {
            return false;
        }

        for (cMoveorderLineBarcode moveorderLineBarcode : this.handledBarcodesObl()  ) {

            if (moveorderLineBarcode.getBarcodeStr().equalsIgnoreCase(cMoveorderBarcode.currentMoveOrderBarcode.getBarcodeStr())) {

                moveorderLineBarcode.quantityHandledDbl -= cMoveorderBarcode.currentMoveOrderBarcode.quantityPerUnitOfMeasure;

                if (moveorderLineBarcode.getQuantityhandledDbl() > 0) {
                    moveorderLineBarcode.pUpdateAmountInDatabaseBln();
                    return  true;
                }

                moveorderLineBarcode.pDeleteFromDatabaseBln();
                return  true;
            }
        }
        return  false;
    }

    private boolean mUpdateQuanitityHandled(double pvQuantityHandledBln) {

        boolean resultBln;
        resultBln =   cMoveorderLine.getMoveorderLineViewModel().pUpdateQuantityHandledBln(pvQuantityHandledBln);

        if (resultBln == false) {
            return  false;
        }

        this.quantityHandledDbl = pvQuantityHandledBln;
        return true;

    }
    private boolean mUpdateLocalStatusBln(Integer pvNewStatusInt) {

        boolean resultBln;
        resultBln =   cMoveorderLine.getMoveorderLineViewModel().pUpdateLocalStatusBln(pvNewStatusInt);

        if (resultBln == false) {
            return  false;
        }

        this.localStatusInt = pvNewStatusInt;
        return true;

    }

    private boolean mUpdateHandledTimeStampBln(String pvHandledTimeStampStr) {

        boolean resultBln;
        resultBln =   cMoveorderLine.getMoveorderLineViewModel().pUpdateHandledTimeStampBln(pvHandledTimeStampStr);

        if (resultBln == false) {
            return  false;
        }

        return true;

    }

    public List<cMoveorderLineBarcode> handledBarcodesObl(){


        List<cMoveorderLineBarcode> resultObl;
        resultObl = new ArrayList<>();

        if (cMoveorderLineBarcode.allLineBarcodesObl == null) {
            return  resultObl;
        }

        for (cMoveorderLineBarcode moveorderLineBarcode :cMoveorderLineBarcode.allLineBarcodesObl ) {

            int result = Long.compare(moveorderLineBarcode.getLineNoLng(), this.getLineNoInt());
            if (result == 0) {
                resultObl.add(moveorderLineBarcode);
            }
        }


        return  resultObl;
    }

    public boolean pAddLineBarcodeBln(String pvBarcodeStr, Double pvQuantityDbl) {

        if (this.lineBarcodesObl == null) {
            this.lineBarcodesObl = new ArrayList<>();
        }

        cMoveorderLineBarcode moveorderLineBarcode = new cMoveorderLineBarcode((long) this.getLineNoInt(),pvBarcodeStr,pvQuantityDbl);

        if (cMoveorderLineBarcode.allLineBarcodesObl == null){
            cMoveorderLineBarcode.allLineBarcodesObl = new ArrayList<>();
        }

        cMoveorderLineBarcode.allLineBarcodesObl.add(moveorderLineBarcode);
        this.lineBarcodesObl.add(moveorderLineBarcode);

        return  true;
    }
    public boolean pAddOrUpdateLineBarcodeBln(Double pvAmountDbl){

        //If there are no line barcodes, then simply add this one
        if (this.handledBarcodesObl() == null || this.handledBarcodesObl().size() == 0) {
            cMoveorderLineBarcode moveorderLineBarcode = new cMoveorderLineBarcode((long) cMoveorderLine.currentMoveOrderLine.getLineNoInt(), cMoveorderBarcode.currentMoveOrderBarcode.getBarcodeStr());
            moveorderLineBarcode.quantityHandledDbl = pvAmountDbl;
            moveorderLineBarcode.pInsertInDatabaseBln();
            return true;
        }

        for (cMoveorderLineBarcode moveorderLineBarcode : this.handledBarcodesObl()  ) {

            if (moveorderLineBarcode.getBarcodeStr().equalsIgnoreCase(cMoveorderBarcode.currentMoveOrderBarcode.getBarcodeStr())) {
                moveorderLineBarcode.quantityHandledDbl += pvAmountDbl;
                moveorderLineBarcode.pUpdateAmountInDatabaseBln();
                return  true;
            }
        }
        return  false;
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
        if (Webresult.getSuccessBln() == false || Webresult.getResultBln() == false) {
            return  false;
        }

        List<JSONObject> myList = Webresult.getResultDtt();
        for (int i = 0; i < myList.size(); i++) {
            JSONObject jsonObject;
            jsonObject = myList.get(i);

            cArticleImage articleImage = new cArticleImage(jsonObject);
            articleImage.pInsertInDatabaseBln();
            this.articleImage = articleImage;
            return true;

        }
        return  false;

    }


    public boolean pUpdateQuantityInDatabaseBln(){

        boolean resultBln;
        resultBln =   cMoveorderLine.getMoveorderLineViewModel().pUpdateQuantityBln();

        if (resultBln == false) {
            return  false;
        }

        return true;

    }

    public cMoveorderLineBarcode pGetLineBarcodeByScannedBarcode(String pvBarcodeStr) {

        //We scanned a barcode that belongs to the current article, so check if we already have a line barcode
        for (cMoveorderLineBarcode moveorderLineBarcode : this.lineBarcodesObl) {

            //We have a match, so set the current line
            if (moveorderLineBarcode.getBarcodeStr().equalsIgnoreCase(pvBarcodeStr)) {
                return moveorderLineBarcode;
            }
        }

        return  null;
    }

    public cResult pResetRst(){

        //nit the result
        cResult result = new cResult();
        result.resultBln = true;

        cWebresult WebResult;

        WebResult =  cMoveorderLine.getMoveorderLineViewModel().pResetLineViaWebserviceWrs();
        if (WebResult.getResultBln() == true && WebResult.getSuccessBln() == true ){


            //Remove line barcodes from the database
            cMoveorderLineBarcode.getMoveorderLineBarcodeViewModel().pDeleteForLineNo(this.getLineNoInt());

            //Reset this line
            this.quantityHandledDbl = 0.0;
            this.pUpdateQuantityInDatabaseBln();
            this.lineBarcodesObl.clear();

            return  result;
        }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_MOVELINERESET);
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.activity.getString(R.string.message_reset_line_via_webservice_failed));
            return  result;
        }
    }

    public static cMoveorderLineAdapter gMoveorderLineTotalAdapter;
    public static cMoveorderLineAdapter getMoveorderLineTotalAdapter() {
        if (gMoveorderLineTotalAdapter == null) {
            gMoveorderLineTotalAdapter = new cMoveorderLineAdapter();
        }
        return gMoveorderLineTotalAdapter;
    }
    public static cMoveorderLineAdapter gMoveorderLineDoneAdapter;
    public static cMoveorderLineAdapter getMoveorderLineDoneAdapter() {
        if (gMoveorderLineDoneAdapter == null) {
            gMoveorderLineDoneAdapter = new cMoveorderLineAdapter();
        }
        return gMoveorderLineDoneAdapter;
    }
    public static cMoveorderLineAdapter gMoveorderLineNotDoneAdapter;
    public static cMoveorderLineAdapter getMoveorderLineNotDoneAdapter() {
        if (gMoveorderLineNotDoneAdapter == null) {
            gMoveorderLineNotDoneAdapter = new cMoveorderLineAdapter();
        }
        return gMoveorderLineNotDoneAdapter;
    }

    private  List<cMoveorderBarcode> mGetBarcodesObl(){

        //If barcodes already filled, then we are done
        if (this.barcodesObl != null) {
            return this.barcodesObl;
        }

        // Get barcodes via PickorderBarcode class
        barcodesObl = cMoveorderBarcode.pGetMovebarcodeViaVariantAndItemNoObl(this.getItemNoStr(),this.getVariantCodeStr());
        if (this.barcodesObl == null || this.barcodesObl.size() == 0) {
            return  null;
        }

        return  this.barcodesObl;
    }

}
