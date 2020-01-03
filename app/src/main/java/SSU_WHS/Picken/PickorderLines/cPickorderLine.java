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
import ICS.cAppExtension;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebserviceDefinitions;

public class cPickorderLine {

    //Region Public Properties

    private Integer recordIDInt;
    public Integer getRecordIDInt() {
        return recordIDInt;
    }

    private Integer lineNoInt;
    public Integer getLineNoInt() {
        return lineNoInt;
    }

    private String itemNoStr;
    public String getItemNoStr() {
        return itemNoStr;
    }

    private String variantCodeStr;
    public String getVariantCodeStr() {
        return variantCodeStr;
    }

    public  String getItemNoAndVariantStr(){
        return  this.getItemNoStr() + " " + this.getVariantCodeStr();
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

    private String containerStr;
    public String getContainerStr() {
        return containerStr;
    }

    private String containerTypeStr;
    public String getContainerTypeStr() {
        return containerTypeStr;
    }

    private String containerInputStr;
    public String getContainerInputStr() {
        return containerInputStr;
    }

    private String containerHandled;
    public String getContainerHandledStr() {
        return containerHandled;
    }

    private Double quantityDbl;
    public Double getQuantityDbl() {
        return quantityDbl;
    }

    public Double quantityHandledDbl;
    public Double getQuantityHandledDbl() {
        return quantityHandledDbl;
    }

    private double quantityRejectedDbl;
    public double getQuantityRejectedDbl() {
        return quantityRejectedDbl;
    }

    private double quantityTakenDbl;
    public double getQuantityTakenDbl() {
        return quantityTakenDbl;
    }

    private String sourceNoStr;
    public String getSourceNoStr() {
        return sourceNoStr;
    }

    private String destinationNoStr;
    public String getDestinationNoStr() {
        return destinationNoStr;
    }

    private Boolean isPartOfMultiLineOrderBln;
    public Boolean getPartOfMultiLineOrderBln() {
        return isPartOfMultiLineOrderBln;
    }

    private String shippingAdviceStr;
    public String getShippingAdviceStr() {
        return shippingAdviceStr;
    }

    public String processingSequenceStr;
    public String getProcessingSequenceStr() {
        return processingSequenceStr;
    }

    private String storeSourceOpdrachtStr;
    public String getStoreSourceOpdrachtStr() {
        return storeSourceOpdrachtStr;
    }

    private String storageBinCodeStr;
    public String getStorageBinCodeStr() {
        return storageBinCodeStr;
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
        return  this.getVendorItemNoStr() + " " + this.getVendorItemDescriptionStr();
    }

    private String component10Str;
    public String getComponent10Str() {
        return component10Str;
    }

    private String brandStr;
    public String getBrandStr() {
        return brandStr;
    }

    private boolean printDocumentsBln;
    public boolean isPrintDocumentsBln() {
        return printDocumentsBln;
    }

    private int statusInt;
    public int getStatusInt() {
        return statusInt;
    }

    private int statusShippingInt;
    public int getStatusShippingInt() {
        return statusShippingInt;
    }

    private int statusPackingInt;
    public int getStatuPackingInt() {
        return statusPackingInt;
    }

    private int lineNoTakeInt;
    public int getLineNoTakeInt() {
        return lineNoTakeInt;
    }

    private String takenTimeStampStr;
    public String getTakenTimeStampStr() {
        return takenTimeStampStr;
    }

    private Integer localStatusInt;
    public Integer getLocalStatusInt() {
        return localStatusInt;
    }

    private String localSortLocationStr;
    public String getLocalSortLocationStr() {
        return localSortLocationStr;
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

    private cPickorderLineEntity PickorderLineEntity;
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

    private static cPickorderLineViewModel gPickorderLineViewModel;
    private static cPickorderLineViewModel getPickorderLineViewModel() {
        if (gPickorderLineViewModel == null) {
            gPickorderLineViewModel = ViewModelProviders.of(cAppExtension.fragmentActivity ).get(cPickorderLineViewModel.class);
        }
        return gPickorderLineViewModel;
    }

    private static cPickorderLineAdapter gPickorderLineToPickAdapter;
    public static cPickorderLineAdapter getPickorderLineToPickAdapter() {
        if (gPickorderLineToPickAdapter == null) {
            gPickorderLineToPickAdapter = new cPickorderLineAdapter();
        }
        return gPickorderLineToPickAdapter;
    }

    private static cPickorderLineAdapter gPickorderLinePickedAdapter;
    public static cPickorderLineAdapter getPickorderLinePickedAdapter() {
        if (gPickorderLinePickedAdapter == null) {
            gPickorderLinePickedAdapter = new cPickorderLineAdapter();
        }
        return gPickorderLinePickedAdapter;
    }

    private static cPickorderLineAdapter gPickorderLineTotalAdapter;
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
        this.isPartOfMultiLineOrderBln = cText.pStringToBooleanBln(this.PickorderLineEntity.getIspartOfMultilLneOrderStr(), false) ;
        this.shippingAdviceStr= this.PickorderLineEntity.getShippingAdviceStr();
        this.processingSequenceStr = this.PickorderLineEntity.getProcessingSequenceStr();
        this.storeSourceOpdrachtStr = this.PickorderLineEntity.getStoreSourceOpdrachtStr();
        this.storageBinCodeStr =   this.PickorderLineEntity.getStorageBinCodeStr();
        this.vendorItemNo =  this.PickorderLineEntity.getVendorItemNoStr();
        this.vendorItemDescriptionStr =  this.PickorderLineEntity.getVendorItemDescriptionStr();
        this.component10Str =   this.PickorderLineEntity.getComponent10Str();
        this.brandStr = this.PickorderLineEntity.getBrandStr();
        this.printDocumentsBln= cText.pStringToBooleanBln(this.PickorderLineEntity.getPrintdocumentsStr(), false) ;
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
        this.isPartOfMultiLineOrderBln = cText.pStringToBooleanBln(this.PickorderLineEntity.getIspartOfMultilLneOrderStr(), false) ;
        this.shippingAdviceStr= this.PickorderLineEntity.getShippingAdviceStr();
        this.processingSequenceStr = this.PickorderLineEntity.getProcessingSequenceStr();
        this.storeSourceOpdrachtStr = this.PickorderLineEntity.getStoreSourceOpdrachtStr();
        this.storageBinCodeStr =   this.PickorderLineEntity.getStorageBinCodeStr();
        this.vendorItemNo =  this.PickorderLineEntity.getVendorItemNoStr();
        this.vendorItemDescriptionStr =  this.PickorderLineEntity.getVendorItemDescriptionStr();
        this.component10Str =   this.PickorderLineEntity.getComponent10Str();
        this.brandStr = this.PickorderLineEntity.getBrandStr();
        this.printDocumentsBln= cText.pStringToBooleanBln(this.PickorderLineEntity.getPrintdocumentsStr(), false) ;
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

        if (!resultBln) {
            return  false;
        }

        this.processingSequenceStr = pvProcessingSequenceStr;
        return true;

    }

    public cResult pLineBusyRst(){


        cResult result = new cResult();
        result.resultBln = false;

        if (cPickorderLine.currentPickOrderLine.mGetBarcodesObl() == null || cPickorderLine.currentPickOrderLine.mGetBarcodesObl().size() == 0) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.no_barcodes_availabe_for_this_line));
            return result;
        }

        if (!this.mBusyBln()) {
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


        if (!this.mBusyBln()) {

            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_couldnt_set_line_on_busy));
            return result;
        }

        result.resultBln = true;

        return  result;

    }

    public void pErrorSending() {
      this.mUpdateLocalStatusBln(cWarehouseorder.PicklineLocalStatusEnu.LOCALSTATUS_DONE_ERROR_SENDING);
    }

    public boolean pResetBln() {

        cWebresult WebResult;
        WebResult =  cPickorderLine.getPickorderLineViewModel().pResetViaWebserviceWrs();
        if (WebResult.getResultBln() && WebResult.getSuccessBln()){

            this.mUpdateLocalStatusBln(cWarehouseorder.PicklineLocalStatusEnu.LOCALSTATUS_NEW);
            this.mUpdateQuanitityHandled(0);

            //delete all line barcodes
            for (cPickorderLineBarcode pickorderLineBarcode : this.handledBarcodesObl()  ) {
                    pickorderLineBarcode.pDeleteFromDatabaseBln();
            }

            //If we already have a processingSequence, then empty it
            if (!this.processingSequenceStr.isEmpty()) {

                //Check if we need to remove the SalesorderPackingTableLines
               if (this.pGetLinesForProcessingSequenceObl().size() <= 1)  {
                   cSalesOrderPackingTable.pDeleteFromDatabaseBln(this.processingSequenceStr);
                }

                this.pUpdateProcessingSequenceBln("");
            }

            return  true;
        }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_PICKORDERLINERESET);
            return  false;
        }

    }

    public boolean pHandledIndatabaseBln(){


        if (!this.mUpdateQuanitityHandled(this.quantityHandledDbl)) {
            return  false;
        }

        if (!this.mUpdateLocalStatusBln(cWarehouseorder.PicklineLocalStatusEnu.LOCALSTATUS_DONE_NOTSENT)) {
            return  false;
        }

        this.takenTimeStampStr = cDateAndTime.pGetCurrentDateTimeForWebserviceStr();

        return this.mUpdateHandledTimeStampBln(this.takenTimeStampStr);

    }

    public boolean pUpdateSortLineIndatabaseBln(){


        if (!this.mUpdateQuanitityHandled(this.quantityHandledDbl)) {
            return  false;
        }

        if (!this.mUpdateLocalStatusBln(cWarehouseorder.PicklineLocalStatusEnu.LOCALSTATUS_BUSY)) {
            return  false;
        }

        this.takenTimeStampStr = cDateAndTime.pGetCurrentDateTimeForWebserviceStr();

        return this.mUpdateHandledTimeStampBln(this.takenTimeStampStr);

    }

    public boolean pCancelIndatabaseBln(){


        if (!this.mUpdateQuanitityHandled(this.quantityHandledDbl)) {
            return  false;
        }

        if (!this.mUpdateLocalStatusBln(cWarehouseorder.PicklineLocalStatusEnu.LOCALSTATUS_NEW)) {
            return  false;
        }

        this.takenTimeStampStr = "";

        return this.mUpdateHandledTimeStampBln(this.takenTimeStampStr);

    }

    public boolean pAddOrUpdateLineBarcodeBln(Double pvAmountDbl){

        //If there are no line barcodes, then simply add this one
        if (this.handledBarcodesObl() == null || this.handledBarcodesObl().size() == 0) {
            cPickorderLineBarcode pickorderLineBarcode = new cPickorderLineBarcode(cPickorderLine.currentPickOrderLine.getLineNoInt().longValue(), cPickorderBarcode.currentPickorderBarcode.getBarcodeStr());
            pickorderLineBarcode.quantityHandledDbl = pvAmountDbl;
            pickorderLineBarcode.pInsertInDatabaseBln();
            return true;
        }

        for (cPickorderLineBarcode pickorderLineBarcode : this.handledBarcodesObl()  ) {

            if (pickorderLineBarcode.getBarcodeStr().equalsIgnoreCase(cPickorderBarcode.currentPickorderBarcode.getBarcodeStr())) {
                pickorderLineBarcode.quantityHandledDbl += pvAmountDbl;
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

                pickorderLineBarcode.quantityHandledDbl -= cPickorderBarcode.currentPickorderBarcode.getQuantityPerUnitOfMeasureDbl();

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
        if (WebResult.getResultBln() && WebResult.getSuccessBln()){

            return this.mUpdateLocalStatusBln(cWarehouseorder.PicklineLocalStatusEnu.LOCALSTATUS_DONE_SENT);
        }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_PICKORDERLINE_HANDLED);
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

        Webresult = cArticleImage.getArticleImageViewModel().pGetArticleImageFromWebserviceWrs(this.getItemNoStr(),this.getVariantCodeStr());
        if (!Webresult.getSuccessBln() || !Webresult.getResultBln()) {
            return  false;
        }

        List<JSONObject> myList = Webresult.getResultDtt();

        if (Webresult.getResultDtt().size() == 1) {
            cArticleImage articleImage = new cArticleImage(Webresult.getResultDtt().get(0));
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

    public List<cPickorderLine> pGetLinesForProcessingSequenceObl() {

        List<cPickorderLine> resultObl = new ArrayList<>();

        if (cPickorder.currentPickOrder.linesObl() == null || cPickorder.currentPickOrder.linesObl().size() == 0) {
            return resultObl;
        }

        for (cPickorderLine pickorderLine :cPickorder.currentPickOrder.linesObl() ) {

            if (pickorderLine.processingSequenceStr.equalsIgnoreCase(this.processingSequenceStr)) {
                resultObl.add(pickorderLine);
            }

        }


        return resultObl;
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
        if (WebResult.getResultBln() && WebResult.getSuccessBln()){

            resultObl.addAll(WebResult.getResultObl());

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

        if (!resultBln) {
            return  false;
        }

        this.localStatusInt = pvNewStatusInt;
        return true;

    }

    private boolean mUpdateQuanitityHandled(double pvQuantityHandledBln) {

        boolean resultBln;
        resultBln =   cPickorderLine.getPickorderLineViewModel().pUpdateQuantityHandledBln(pvQuantityHandledBln);

        if (!resultBln) {
            return  false;
        }

        this.quantityHandledDbl = pvQuantityHandledBln;
        return true;

    }

    private boolean mUpdateHandledTimeStampBln(String pvHandledTimeStampStr) {

        boolean resultBln;
        resultBln =   cPickorderLine.getPickorderLineViewModel().pUpdateHandledTimeStampBln(pvHandledTimeStampStr);

        return resultBln;

    }

    private  List<cPickorderBarcode> mGetBarcodesObl(){

        //If barcodes already filled, then we are done
        if (this.barcodesObl != null) {
            return this.barcodesObl;
        }

        // Get barcodes via PickorderBarcode class
        this.barcodesObl = cPickorderBarcode.pGetPickbarcodesViaVariantAndItemNoObl(this.getItemNoStr(),this.getVariantCodeStr());
        if (this.barcodesObl == null || this.barcodesObl.size() == 0) {
            return  null;
        }

        return  this.barcodesObl;
    }

    private boolean mBusyBln() {
        return this.mUpdateLocalStatusBln(cWarehouseorder.PicklineLocalStatusEnu.LOCALSTATUS_BUSY);

    }

     //End Region Private Methods

}


