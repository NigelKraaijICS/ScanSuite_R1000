package SSU_WHS.Picken.PickorderLines;

import androidx.lifecycle.ViewModelProviders;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


import ICS.Utils.cDateAndTime;
import ICS.Utils.cResult;
import ICS.Utils.cText;
import ICS.Weberror.cWeberror;
import SSU_WHS.Basics.ArticleImages.cArticleImage;
import SSU_WHS.Basics.Settings.cSetting;
import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.Picken.PickorderBarcodes.cPickorderBarcode;
import SSU_WHS.Picken.PickorderLineBarcodes.cPickorderLineBarcode;
import SSU_WHS.Picken.Pickorders.cPickorder;
import SSU_WHS.Picken.SalesOrderPackingTable.cSalesOrderPackingTable;
import nl.icsvertex.scansuite.R;
import nl.icsvertex.scansuite.cAppExtension;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebserviceDefinitions;

public class cPickorderLine {

    //Region Public Properties

    public Integer recordIDInt;
    public Integer getRecordIDInt() {
        return recordIDInt;
    }

    public Integer lineNoInt;
    public Integer getLineNoInt() {
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

    public String containerHandled;
    public String getContainerHandledStr() {
        return containerHandled;
    }

    public Double quantityDbl;
    public Double getQuantityDbl() {
        return quantityDbl;
    }

    public Double quantityHandledDbl;
    public Double getQuantityHandledDbl() {
        return quantityHandledDbl;
    }

    public double quantityRejectedDbl;
    public double getQuantityRejectedDbl() {
        return quantityRejectedDbl;
    }

    public double quantityTakenDbl;
    public double getQuantityTakenDbl() {
        return quantityTakenDbl;
    }

    public String sourceNoStr;
    public String getSourceNoStr() {
        return sourceNoStr;
    }

    public String destinationNoStr;
    public String getDestinationNoStr() {
        return destinationNoStr;
    }

    public Boolean isPartOfMultiLineOrderBln;
    public Boolean getPartOfMultiLineOrderBln() {
        return isPartOfMultiLineOrderBln;
    }

    public String shippingAdviceStr;
    public String getShippingAdviceStr() {
        return shippingAdviceStr;
    }

    public String processingSequenceStr;
    public String getProcessingSequenceStr() {
        return processingSequenceStr;
    }

    public String storeSourceOpdrachtStr;
    public String getStoreSourceOpdrachtStr() {
        return storeSourceOpdrachtStr;
    }

    public String storageBinCodeStr;
    public String getStorageBinCodeStr() {
        return storageBinCodeStr;
    }

    public String vendorItemNo;
    public String getVendorItemNoStr() {
        return vendorItemNo;
    }

    public String vendorItemDescriptionStr;
    public String getVendorItemDescriptionStr() {
        return vendorItemDescriptionStr;
    }

    public String component10Str;
    public String getComponent10Str() {
        return component10Str;
    }

    public String brandStr;
    public String getBrandStr() {
        return brandStr;
    }

    public boolean printDocumentsBln;
    public boolean isPrintDocumentsBln() {
        return printDocumentsBln;
    }

    public int statusInt;
    public int getStatusInt() {
        return statusInt;
    }

    public int statusShippingInt;
    public int getStatusShippingInt() {
        return statusShippingInt;
    }

    public int statusPackingInt;
    public int getStatuPackingInt() {
        return statusPackingInt;
    }

    public int lineNoTakeInt;
    public int getLineNoTakeInt() {
        return lineNoTakeInt;
    }

    public String takenTimeStampStr;
    public String getTakenTimeStampStr() {
        return takenTimeStampStr;
    }

    public Integer localStatusInt;
    public Integer getLocalStatusInt() {
        return localStatusInt;
    }

    public String localSortLocationStr;
    public String getLocalSortLocationStr() {
        return localSortLocationStr;
    }

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

    public cPickorderLineEntity PickorderLineEntity;
    public boolean indatabaseBln;

    public  List<cPickorderBarcode> barcodesObl;

    public List<cPickorderLineBarcode> handledBarcodesObl(){


        List<cPickorderLineBarcode> resultObl;
        resultObl = new ArrayList<>();

        if (cPickorderLineBarcode.allLineBarcodesObl == null) {
            return  resultObl;
        }

        for (cPickorderLineBarcode pickorderLineBarcode :cPickorderLineBarcode.allLineBarcodesObl ) {

            int result = Long.compare(pickorderLineBarcode.getLineNoLng(), this.getLineNoInt().longValue());
            if (result == 0) {
                resultObl.add(pickorderLineBarcode);
            }
        }


                 return  resultObl;
        }

    public static cPickorderLineViewModel gPickorderLineViewModel;
    public static cPickorderLineViewModel getPickorderLineViewModel() {
        if (gPickorderLineViewModel == null) {
            gPickorderLineViewModel = ViewModelProviders.of(cAppExtension.fragmentActivity ).get(cPickorderLineViewModel.class);
        }
        return gPickorderLineViewModel;
    }

    public static cPickorderLineAdapter gPickorderLineToPickAdapter;
    public static cPickorderLineAdapter getPickorderLineToPickAdapter() {
        if (gPickorderLineToPickAdapter == null) {
            gPickorderLineToPickAdapter = new cPickorderLineAdapter();
        }
        return gPickorderLineToPickAdapter;
    }

    public static cPickorderLineAdapter gPickorderLinePickedAdapter;
    public static cPickorderLineAdapter getPickorderLinePickedAdapter() {
        if (gPickorderLinePickedAdapter == null) {
            gPickorderLinePickedAdapter = new cPickorderLineAdapter();
        }
        return gPickorderLinePickedAdapter;
    }

    public static cPickorderLineAdapter gPickorderLineTotalAdapter;
    public static cPickorderLineAdapter getPickorderLineTotalAdapter() {
        if (gPickorderLineTotalAdapter == null) {
            gPickorderLineTotalAdapter = new cPickorderLineAdapter();
        }
        return gPickorderLineTotalAdapter;
    }

    public static List<cPickorderLine> allLinesObl;
    public static cPickorderLine currentPickOrderLine;

    //End Region Public Properties

    //Region Constructor
    public cPickorderLine(JSONObject pvJsonObject, cWarehouseorder.PickOrderTypeEnu pvPickOrderTypeEnu ){
        this.PickorderLineEntity = new cPickorderLineEntity(pvJsonObject, pvPickOrderTypeEnu.toString());
        this.recordIDInt = this.PickorderLineEntity.getRecordidInt();
        this.lineNoInt = this.PickorderLineEntity.getLineNoInt();
        this.itemNoStr = this.PickorderLineEntity.getItemNoStr();
        this.variantCodeStr = this.PickorderLineEntity.getVariantCodeStr();
        this.descriptionStr = this.PickorderLineEntity.getDescriptionStr();
        this.description2Str = this.PickorderLineEntity.getDescription2Str();
        this.binCodeStr= this.PickorderLineEntity.getBincodeStr();
        this.containerStr = this.PickorderLineEntity.getContainerStr();
        this.containerTypeStr= this.PickorderLineEntity.getContainerTypeStr();
        this.containerInputStr =this.PickorderLineEntity.getContainerinputsStr();
        this.containerHandled = this.PickorderLineEntity.getContainerHandledStr();

        if (pvPickOrderTypeEnu == cWarehouseorder.PickOrderTypeEnu.PICK) {
            this.quantityDbl = this.PickorderLineEntity.getQuantityDbl();
        }

        if (pvPickOrderTypeEnu == cWarehouseorder.PickOrderTypeEnu.SORT) {
            this.quantityDbl = this.PickorderLineEntity.getQuantityTakenDbl();
        }

        this.quantityHandledDbl = this.PickorderLineEntity.getQuantityHandledDbl();
        this.quantityRejectedDbl = this.PickorderLineEntity.getQuantityHandledDbl();
        this.quantityTakenDbl =  this.PickorderLineEntity.getQuantityTakenDbl();
        this.sourceNoStr = this.PickorderLineEntity.getSourceNoStr();
        this.destinationNoStr = this.PickorderLineEntity.getDestinationNoStr();
        this.isPartOfMultiLineOrderBln = cText.stringToBoolean(this.PickorderLineEntity.getIspartOfMultilLneOrderStr(), false) ;
        this.shippingAdviceStr= this.PickorderLineEntity.getShippingAdviceStr();
        this.processingSequenceStr = this.PickorderLineEntity.getProcessingSequenceStr();
        this.storeSourceOpdrachtStr = this.PickorderLineEntity.getStoreSourceOpdrachtStr();
        this.storageBinCodeStr =   this.PickorderLineEntity.getStorageBinCodeStr();
        this.vendorItemNo =  this.PickorderLineEntity.getVendorItemNoStr();
        this.vendorItemDescriptionStr =  this.PickorderLineEntity.getVendorItemDescriptionStr();
        this.component10Str =   this.PickorderLineEntity.getComponent10Str();
        this.brandStr = this.PickorderLineEntity.getBrandStr();
        this.printDocumentsBln= cText.stringToBoolean(this.PickorderLineEntity.getPrintdocumentsStr(), false) ;
        this.statusInt =  this.PickorderLineEntity.getStatusInt();
        this.statusShippingInt =  this.PickorderLineEntity.getStatusShippingInt();
        this.statusPackingInt =  this.PickorderLineEntity.getStatusPackingInt();
        this.statusInt =  this.PickorderLineEntity.getStatusInt();
        this.localStatusInt =  cWarehouseorder.PicklineLocalStatusEnu.LOCALSTATUS_NEW;

        if (pvPickOrderTypeEnu == cWarehouseorder.PickOrderTypeEnu.PICK) {
            if (this.statusInt > cWarehouseorder.PicklineStatusEnu.Needed) {
                this.localStatusInt = cWarehouseorder.PicklineLocalStatusEnu.LOCALSTATUS_DONE_SENT;
            }
        }

        if (pvPickOrderTypeEnu == cWarehouseorder.PickOrderTypeEnu.SORT) {
            if (this.statusInt == cWarehouseorder.PicklineStatusEnu.DONE) {
                if (Double.compare(this.getQuantityHandledDbl(),this.getQuantityDbl()) == 0) {
                    this.localStatusInt = cWarehouseorder.PicklineLocalStatusEnu.LOCALSTATUS_DONE_SENT;
                }
            }
        }

        this.lineNoTakeInt =  this.PickorderLineEntity.getLinenoTakeInt();
        this.takenTimeStampStr =  this.PickorderLineEntity.takentimestamp;
        this.localSortLocationStr = this.PickorderLineEntity.getLocalSortLocationStr();
        this.extraField1Str =  this.PickorderLineEntity.getExtraField1Str();
        this.extraField2Str = this.PickorderLineEntity.getExtraField2Str();
        this.extraField3Str =  this.PickorderLineEntity.getExtraField3Str();
        this.extraField4Str =  this.PickorderLineEntity.getExtraField4Str();
        this.extraField5Str =  this.PickorderLineEntity.getExtraField5Str();
        this.extraField6Str =  this.PickorderLineEntity.getExtraField6Str();
        this.extraField7Str =  this.PickorderLineEntity.getExtraField7Str();
        this.extraField8Str =  this.PickorderLineEntity.getExtraField8Str();
    }

    public cPickorderLine(cPickorderLineEntity pvPickorderLineEntity){
        this.PickorderLineEntity = pvPickorderLineEntity;
        this.recordIDInt = this.PickorderLineEntity.getRecordidInt();
        this.lineNoInt = this.PickorderLineEntity.getLineNoInt();
        this.itemNoStr = this.PickorderLineEntity.getItemNoStr();
        this.variantCodeStr = this.PickorderLineEntity.getVariantCodeStr();
        this.descriptionStr = this.PickorderLineEntity.getDescriptionStr();
        this.description2Str = this.PickorderLineEntity.getDescription2Str();
        this.binCodeStr= this.PickorderLineEntity.getBincodeStr();
        this.containerStr = this.PickorderLineEntity.getContainerStr();
        this.containerTypeStr= this.PickorderLineEntity.getContainerTypeStr();
        this.containerInputStr =this.PickorderLineEntity.getContainerinputsStr();
        this.containerHandled = this.PickorderLineEntity.getContainerHandledStr();
        this.quantityDbl = this.PickorderLineEntity.getQuantityDbl();
        this.quantityHandledDbl = this.PickorderLineEntity.getQuantityHandledDbl();
        this.quantityRejectedDbl = this.PickorderLineEntity.getQuantityHandledDbl();
        this.sourceNoStr = this.PickorderLineEntity.getSourceNoStr();
        this.destinationNoStr = this.PickorderLineEntity.getDestinationNoStr();
        this.isPartOfMultiLineOrderBln = cText.stringToBoolean(this.PickorderLineEntity.getIspartOfMultilLneOrderStr(), false) ;
        this.shippingAdviceStr= this.PickorderLineEntity.getShippingAdviceStr();
        this.processingSequenceStr = this.PickorderLineEntity.getProcessingSequenceStr();
        this.storeSourceOpdrachtStr = this.PickorderLineEntity.getStoreSourceOpdrachtStr();
        this.storageBinCodeStr =   this.PickorderLineEntity.getStorageBinCodeStr();
        this.vendorItemNo =  this.PickorderLineEntity.getVendorItemNoStr();
        this.vendorItemDescriptionStr =  this.PickorderLineEntity.getVendorItemDescriptionStr();
        this.component10Str =   this.PickorderLineEntity.getComponent10Str();
        this.brandStr = this.PickorderLineEntity.getBrandStr();
        this.printDocumentsBln= cText.stringToBoolean(this.PickorderLineEntity.getPrintdocumentsStr(), false) ;
        this.statusInt =  this.PickorderLineEntity.getStatusInt();
        this.localStatusInt = this.PickorderLineEntity.getLocalstatusInt();

        if (this.statusInt > cWarehouseorder.PicklineStatusEnu.Needed) {
            this.localStatusInt = cWarehouseorder.PicklineLocalStatusEnu.LOCALSTATUS_DONE_SENT;
        }

        this.lineNoTakeInt =  this.PickorderLineEntity.getLinenoTakeInt();
        this.quantityTakenDbl =  this.PickorderLineEntity.getQuantityTakenDbl();
        this.takenTimeStampStr =  this.PickorderLineEntity.takentimestamp;
         this.localSortLocationStr = this.PickorderLineEntity.getLocalSortLocationStr();
        this.extraField1Str =  this.PickorderLineEntity.getExtraField1Str();
        this.extraField2Str = this.PickorderLineEntity.getExtraField2Str();
        this. extraField3Str =  this.PickorderLineEntity.getExtraField3Str();
        this.extraField4Str =  this.PickorderLineEntity.getExtraField4Str();
        this.extraField5Str =  this.PickorderLineEntity.getExtraField5Str();
        this.extraField6Str =  this.PickorderLineEntity.getExtraField6Str();
        this.extraField7Str =  this.PickorderLineEntity.getExtraField7Str();
        this.extraField8Str =  this.PickorderLineEntity.getExtraField8Str();
    }

    //Region Public Methods

    public static boolean pTruncateTableBln(){
        cPickorderLine.getPickorderLineViewModel().deleteAll();
        return true;
    }

    public boolean pInsertInDatabaseBln() {
        cPickorderLine.getPickorderLineViewModel().insert(this.PickorderLineEntity);
        this.indatabaseBln = true;

        if (cPickorderLine.allLinesObl == null){
            cPickorderLine.allLinesObl = new ArrayList<>();
        }
        cPickorderLine.allLinesObl.add(this);
        return  true;
    }

    public boolean pUpdateProcessingSequenceBln(String pvProcessingSequenceStr) {

        boolean resultBln;
        resultBln =   cPickorderLine.getPickorderLineViewModel().pUpdateProcessingSequenceBln(pvProcessingSequenceStr);

        if (resultBln == false) {
            return  false;
        }

        this.processingSequenceStr = pvProcessingSequenceStr;
        return true;

    }

    public boolean pUpdateSortOrderLineBln(double pvQuantityHandledDbl, String pvBinCodeStr) {

        boolean resultBln;
        resultBln =   cPickorderLine.getPickorderLineViewModel().pUpdateSortOrderLineBln((int)pvQuantityHandledDbl,pvBinCodeStr);

        if (resultBln == false) {
            return  false;
        }

        this.quantityHandledDbl = pvQuantityHandledDbl;
        this.binCodeStr = pvBinCodeStr;
        return  true;

    }

    public cResult pLineBusyRst(){


        cResult result = new cResult();
        result.resultBln = false;

        if (this.mGetBarcodesObl() == null || cPickorderLine.currentPickOrderLine.mGetBarcodesObl().size() == 0) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.no_barcodes_availabe_for_this_line));
            return result;
        }

        if (this.mBusyBln() == false) {

            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_couldnt_set_line_on_busy));
            return result;
        }

        result.resultBln = true;

        return  result;

    }

    public cResult pSortLineBusyRst(){


        cResult result = new cResult();
        result.resultBln = false;

        if (this.mGetBarcodesObl() == null || cPickorderLine.currentPickOrderLine.mGetBarcodesObl().size() == 0) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.no_barcodes_availabe_for_this_line));
            return result;
        }


        if (this.mBusyBln() == false) {

            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_couldnt_set_line_on_busy));
            return result;
        }

        result.resultBln = true;

        return  result;

    }

    public boolean pErrorSendingBln() {

        return this.mUpdateLocalStatusBln(cWarehouseorder.PicklineLocalStatusEnu.LOCALSTATUS_DONE_ERROR_SENDING);

    }

    public boolean pResetBln() {

        cWebresult WebResult;
        WebResult =  cPickorderLine.getPickorderLineViewModel().pResetViaWebserviceWrs();
        if (WebResult.getResultBln() == true && WebResult.getSuccessBln() == true ){

            this.mUpdateLocalStatusBln(cWarehouseorder.PicklineLocalStatusEnu.LOCALSTATUS_NEW);
            this.mUpdateQuanitityHandled(0);



            return  true;
        }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_PICKORDERLINERESET);
            return  false;
        }

    }

    public boolean pHandledIndatabaseBln(){


        if (this.mUpdateQuanitityHandled(this.quantityHandledDbl)  == false) {
            return  false;
        }

        if (this.mUpdateLocalStatusBln(cWarehouseorder.PicklineLocalStatusEnu.LOCALSTATUS_DONE_NOTSENT)  == false) {
            return  false;
        }

        this.takenTimeStampStr = cDateAndTime.pGetCurrentDateTimeForWebserviceStr();

        if (this.mUpdateHandledTimeStampBln(this.takenTimeStampStr)  == false) {
            return  false;
        }

        return  true;

    }

    public boolean pUpdateSortLineIndatabaseBln(){


        if (this.mUpdateQuanitityHandled(this.quantityHandledDbl)  == false) {
            return  false;
        }

        if (this.mUpdateLocalStatusBln(cWarehouseorder.PicklineLocalStatusEnu.LOCALSTATUS_BUSY)  == false) {
            return  false;
        }

        this.takenTimeStampStr = cDateAndTime.pGetCurrentDateTimeForWebserviceStr();

        if (this.mUpdateHandledTimeStampBln(this.takenTimeStampStr)  == false) {
            return  false;
        }

        return  true;

    }

    public boolean pCancelIndatabaseBln(){


        if (this.mUpdateQuanitityHandled(this.quantityHandledDbl)  == false) {
            return  false;
        }

        if (this.mUpdateLocalStatusBln(cWarehouseorder.PicklineLocalStatusEnu.LOCALSTATUS_NEW)  == false) {
            return  false;
        }

        this.takenTimeStampStr = "";

        if (this.mUpdateHandledTimeStampBln(this.takenTimeStampStr)  == false) {
            return  false;
        }

        return  true;

    }

    public boolean pAddOrUpdateLineBarcodeBln(){

        //If there are no line barcodes, then simply add this one
        if (this.handledBarcodesObl() == null || this.handledBarcodesObl().size() == 0) {
            cPickorderLineBarcode pickorderLineBarcode = new cPickorderLineBarcode(cPickorderLine.currentPickOrderLine.getLineNoInt().longValue(), cPickorderBarcode.currentPickorderBarcode.getBarcodeStr());
            pickorderLineBarcode.quantityHandledDbl = cPickorderBarcode.currentPickorderBarcode.getQuantityPerUnitOfMeasureDbl();
            pickorderLineBarcode.pInsertInDatabaseBln();
            return true;
        }

        for (cPickorderLineBarcode pickorderLineBarcode : this.handledBarcodesObl()  ) {

            if (pickorderLineBarcode.getBarcodeStr().equalsIgnoreCase(cPickorderBarcode.currentPickorderBarcode.getBarcodeStr())) {
                pickorderLineBarcode.quantityHandledDbl += cPickorderBarcode.currentPickorderBarcode.quantityPerUnitOfMeasureDbl;
                pickorderLineBarcode.pUpdateAmountInDatabaseBln();
                return  true;
            }
        }
        return  false;
    }

    public boolean pRemoveOrUpdateLineBarcodeBln(){

        //If there are no line barcodes, this should not be possible
        if (this.handledBarcodesObl() == null || this.handledBarcodesObl().size() == 0) {
            return false;
        }

        for (cPickorderLineBarcode pickorderLineBarcode : this.handledBarcodesObl()  ) {

            if (pickorderLineBarcode.getBarcodeStr().equalsIgnoreCase(cPickorderBarcode.currentPickorderBarcode.getBarcodeStr())) {

                pickorderLineBarcode.quantityHandledDbl -= cPickorderBarcode.currentPickorderBarcode.quantityPerUnitOfMeasureDbl;

                if (pickorderLineBarcode.getQuantityhandledDbl() > 0) {
                    pickorderLineBarcode.pUpdateAmountInDatabaseBln();
                    return  true;
                }

                pickorderLineBarcode.pDeleteFromDatabaseBln();
                return  true;
            }
        }
        return  false;
    }

    public boolean pHandledBln() {

        cWebresult WebResult;
        WebResult =  cPickorderLine.getPickorderLineViewModel().pPickLineHandledViaWebserviceWrs();
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

    public boolean pSortHandledBln(String pvScannedBarcodeStr) {

        cWebresult WebResult;
        WebResult =  cPickorderLine.getPickorderLineViewModel().pSortLineHandledViaWebserviceWrs(pvScannedBarcodeStr);
        if (WebResult.getResultBln() == true && WebResult.getSuccessBln() == true ){
            return  true;
        }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_SORTORDERLINE_HANDLED);
            return  false;
        }
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

        Webresult = cPickorderLine.getPickorderLineViewModel().pGetArticleImageFromWebserviceWrs(this.getItemNoStr(),this.getVariantCodeStr());
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

    public String pGetPackingTableForSourceNoStr() {

        if (cPickorder.currentPickOrder.salesOrderPackingTableObl() == null || cPickorder.currentPickOrder.salesOrderPackingTableObl().size() == 0) {
            return "";
        }

        //Record for Current Sales order
        cSalesOrderPackingTable recordForSalesOrder = null;

        for (cSalesOrderPackingTable loopRecord : cPickorder.currentPickOrder.salesOrderPackingTableObl()) {

            if (loopRecord.getSalesorderStr().equalsIgnoreCase(cPickorderLine.currentPickOrderLine.getSourceNoStr()))
                return loopRecord.getPackingtableStr();
        }

        return "";
    }

    //todo: move this function to a different class
    public List<String> pGetAdvicedSortLocationsFromWebserviceObl(){

        List<String > resultObl;
        resultObl = new ArrayList<>();

        //If we don't need a result from SSU/ERP then we are done
        if (cSetting.PICK_SORT_LOCATION_ADVICE().isEmpty()) {
            return resultObl;
        }

        cWebresult WebResult;
        WebResult =  cPickorderLine.getPickorderLineViewModel().pGetSortLocationAdviceViaWebserviceWrs(this.getSourceNoStr());
        if (WebResult.getResultBln() == true && WebResult.getSuccessBln() == true ){

            for (String loopStr : WebResult.getResultObl() ) {
                resultObl.add(loopStr);
            }

            return  resultObl;
        }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETSORTLOCATIONADVICE);
            return  null;
        }
    }

      //todo: move this function to a different class
    public static List<cPickorderLine> pGetLinesForSourceNoObl(String pvSourceNoStr) {

        List<cPickorderLine > resultObl;
        resultObl = new ArrayList<>();

        List<cPickorderLineEntity> hulpObl;

        hulpObl = cPickorderLine.getPickorderLineViewModel().pGetLinesForSourceNoObl(pvSourceNoStr);
        if (hulpObl == null || hulpObl.size() == 0) {
            return  resultObl;
        }

        for (cPickorderLineEntity pickorderLineEntity :hulpObl ) {
            cPickorderLine pickorderLine = new cPickorderLine(pickorderLineEntity);
            resultObl.add(pickorderLine);
        }

        return  resultObl;
    }

    //todo: move this function to a different class?
    public static List<cPickorderLine> pGetSortLinesForItemNoAndVariantCodeObl(String pvItemNoStr, String pvVariantCodeStr) {

        List<cPickorderLine > resultObl;
        resultObl = new ArrayList<>();

        List<cPickorderLineEntity> hulpObl;

        hulpObl = cPickorderLine.getPickorderLineViewModel().pGetSortLineForItemNoAndVariantCodeObl(pvItemNoStr, pvVariantCodeStr);
        if (hulpObl == null || hulpObl.size() == 0) {
            return  resultObl;
        }

        for (cPickorderLineEntity pickorderLineEntity :hulpObl ) {
            cPickorderLine pickorderLine = new cPickorderLine(pickorderLineEntity);
            resultObl.add(pickorderLine);
        }

        return  resultObl;
    }

    //End Region Public Methods

    //Region Private Methods

    private boolean mUpdateLocalStatusBln(Integer pvNewStatusInt) {

        boolean resultBln;
        resultBln =   cPickorderLine.getPickorderLineViewModel().pUpdateLocalStatusBln(pvNewStatusInt);

        if (resultBln == false) {
            return  false;
        }

        this.localStatusInt = pvNewStatusInt;
        return true;

    }

    private boolean mUpdateQuanitityHandled(double pvQuantityHandledBln) {

        boolean resultBln;
        resultBln =   cPickorderLine.getPickorderLineViewModel().pUpdateQuantityHandledBln(pvQuantityHandledBln);

        if (resultBln == false) {
            return  false;
        }

        this.quantityHandledDbl = pvQuantityHandledBln;
        return true;

    }

    private boolean mUpdateHandledTimeStampBln(String pvHandledTimeStampStr) {

        boolean resultBln;
        resultBln =   cPickorderLine.getPickorderLineViewModel().pUpdateHandledTimeStampBln(pvHandledTimeStampStr);

        if (resultBln == false) {
            return  false;
        }

        return true;

    }

    private  List<cPickorderBarcode> mGetBarcodesObl(){

        //If barcodes already filled, then we are done
        if (this.barcodesObl != null) {
            return this.barcodesObl;
        }

        // Get barcodes via PickorderBarcode class
        this.barcodesObl = cPickorderBarcode.pGetPickbarcodeViaVariantAndItemNoObl(this.getItemNoStr(),this.getVariantCodeStr());
        if (this.barcodesObl == null || this.barcodesObl.size() == 0) {
            return  null;
        }

        return  this.barcodesObl;
    }

    private boolean mBusyBln() {
        return this.mUpdateLocalStatusBln(cWarehouseorder.PicklineLocalStatusEnu.LOCALSTATUS_BUSY);

    }

    private  boolean mDeleteLineBarcodes(){
        cPickorderLineBarcode.pTruncateTableBln();
        return true;

    }



    //End Region Private Methods

}


