package SSU_WHS.Picken.PickorderLines;

import androidx.lifecycle.ViewModelProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import ICS.Utils.cDateAndTime;
import ICS.Utils.cResult;
import ICS.Weberror.cWeberror;
import ICS.cAppExtension;
import SSU_WHS.Basics.ArticleImages.cArticleImage;
import SSU_WHS.Basics.ArticleImages.cArticleImageViewModel;
import SSU_WHS.Basics.Branches.cBranch;
import SSU_WHS.Basics.PropertyGroup.cPropertyGroup;
import SSU_WHS.Basics.PropertyGroupProperty.cPropertyGroupProperty;
import SSU_WHS.Basics.Settings.cSetting;
import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.Move.MoveItemVariant.cMoveItemVariant;
import SSU_WHS.Move.MoveOrders.cMoveorder;
import SSU_WHS.Picken.PickorderBarcodes.cPickorderBarcode;
import SSU_WHS.Picken.PickorderLineBarcodes.cPickorderLineBarcode;
import SSU_WHS.Picken.Pickorders.cPickorder;
import SSU_WHS.Picken.SalesOrderPackingTable.cSalesOrderPackingTable;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebserviceDefinitions;
import nl.icsvertex.scansuite.R;

public class cPickorderLine {

    //Region Public Properties

    private final Integer recordIDInt;
    public Integer getRecordIDInt() {
        return recordIDInt;
    }

    private final Integer lineNoInt;
    public Integer getLineNoInt() {
        return lineNoInt;
    }

    private final String itemNoStr;
    public String getItemNoStr() {
        return itemNoStr;
    }

    private final String variantCodeStr;
    public String getVariantCodeStr() {
        return variantCodeStr;
    }

    public  String getItemNoAndVariantStr(){
        return  this.getItemNoStr() + " " + this.getVariantCodeStr();
    }

    private final String descriptionStr;
    public String getDescriptionStr() {
        return descriptionStr;
    }

    private final String description2Str;
    public String getDescription2Str() {
        return description2Str;
    }

    private final String binCodeStr;
    public String getBinCodeStr() {
        return binCodeStr;
    }

    private Double quantityDbl;
    public Double getQuantityDbl() {
        return quantityDbl;
    }

    public Double quantityHandledDbl;
    public Double getQuantityHandledDbl() {
        return quantityHandledDbl;
    }

    private final Double quantityTakenDbl;
    public Double getQuantityTakenDbl() {
        return quantityTakenDbl;
    }

    private final Double quantityRejectedDbl;
    public Double getQuantityRejectedDbl() {
        return quantityRejectedDbl;
    }

    private final String sourceNoStr;
    public String getSourceNoStr() {
        return sourceNoStr;
    }

    private final String destinationNoStr;
    public String getDestinationNoStr() {
        return destinationNoStr;
    }

    public String processingSequenceStr;
    public String getProcessingSequenceStr() {
        return processingSequenceStr;
    }

    private final String vendorItemNo;
    public String getVendorItemNoStr() {
        return vendorItemNo;
    }

    private final String vendorItemDescriptionStr;
    public String getVendorItemDescriptionStr() {
        return vendorItemDescriptionStr;
    }

    public  String getVendorItemNoAndDescriptionStr(){
        return  this.getVendorItemNoStr() + " " + this.getVendorItemDescriptionStr();
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

    private String takenTimeStampStr;
    public String getTakenTimeStampStr() {
        return takenTimeStampStr;
    }

    private Integer localStatusInt;
    public Integer getLocalStatusInt() {
        return localStatusInt;
    }

    private final String extraField1Str;
    public String getExtraField1Str() {
        return extraField1Str;
    }

    private final String extraField2Str;
    public String getExtraField2Str() {
        return extraField2Str;
    }

    private final String extraField3Str;
    public String getExtraField3Str() {
        return extraField3Str;
    }

    private final String extraField4Str;
    public String getExtraField4Str() {
        return extraField4Str;
    }

    private final String extraField5Str;
    public String getExtraField5Str() {
        return extraField5Str;
    }

    private final String extraField6Str;
    public String getExtraField6Str() {
        return extraField6Str;
    }

    private final String extraField7Str;
    public String getExtraField7Str() {
        return extraField7Str;
    }

    private final String extraField8Str;
    public String getExtraField8Str() {
        return extraField8Str;
    }

    public cArticleImage articleImage;

    public  String getDestinationAndDescriptionStr(){

       String resultStr ;

       cBranch branch = cBranch.pGetBranchByCode(this.getDestinationNoStr());


        if (branch == null) {
            resultStr = this.getDestinationNoStr();
        }
        else {
            resultStr = branch.getBranchNameStr() + "  (" + this.getDestinationNoStr() + ")";
        }

        return  resultStr;

    }

    private final cPickorderLineEntity PickorderLineEntity;
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

    public static List<cPickorderLine> allLinesObl;
    public static cPickorderLine currentPickOrderLine;

   private LinkedHashMap<String,  LinkedHashMap<String,String>> itemProperyDataObl;
    public  LinkedHashMap<String,  LinkedHashMap<String,String> > itemProperyDataObl(){



        if (this.itemProperyDataObl != null) {
            return  this.itemProperyDataObl;
        }

        if (cPickorder.currentPickOrder.itemProperyDataObl == null) {
            return  null;
        }

        this.itemProperyDataObl = new LinkedHashMap<>();
        List<JSONObject> jsonONjectObl = cPickorder.currentPickOrder.itemProperyDataObl.get(this.lineNoInt);

        if (jsonONjectObl == null) {
            return  null;
        }

        for (cPropertyGroup propertyGroup : cPropertyGroup.allPropertyGroupsObl) {
            // Init new hashmap, to store info per key for this line
            //Ddo this for all propertys in the current group
            LinkedHashMap<String,  String> fieldAndValueHasmap = new LinkedHashMap<>();

            for (JSONObject jsonObject : jsonONjectObl) {
                for (cPropertyGroupProperty propertyGroupProperty : propertyGroup.sortedPropertyObl()) {
                    try {
                        //Create the hasmap dynammically and fill it
                        fieldAndValueHasmap.put(propertyGroupProperty.getPropertyStr(), jsonObject.getString(propertyGroupProperty.getPropertyStr()));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            //Add key and value for this propertygroup, so groupname and hashmaplist of propertys and values
            this.itemProperyDataObl.put(propertyGroup.getPropertyGroupStr(),fieldAndValueHasmap);
        }

        return  this.itemProperyDataObl;

    }

    private cPickorderLineViewModel getPickorderLineViewModel() {
        return new ViewModelProvider(cAppExtension.fragmentActivity).get(cPickorderLineViewModel.class);
    }



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


        if (pvPickOrderTypeEnu == cWarehouseorder.PickOrderTypeEnu.PICK  ) {
            this.quantityDbl = this.PickorderLineEntity.getQuantityDbl();
        }

        if (pvPickOrderTypeEnu == cWarehouseorder.PickOrderTypeEnu.SORT ) {
            this.quantityDbl = this.PickorderLineEntity.getQuantityTakenDbl();
        }

        this.quantityHandledDbl = this.PickorderLineEntity.getQuantityHandledDbl();
        this.quantityTakenDbl =  this.PickorderLineEntity.getQuantityTakenDbl();
        this.quantityRejectedDbl = this.PickorderLineEntity.getQuantityRejected();

        this.sourceNoStr = this.PickorderLineEntity.getSourceNoStr();
        this.destinationNoStr = this.PickorderLineEntity.getDestinationNoStr();

        this.processingSequenceStr = this.PickorderLineEntity.getProcessingSequenceStr();

        this.vendorItemNo =  this.PickorderLineEntity.getVendorItemNoStr();
        this.vendorItemDescriptionStr =  this.PickorderLineEntity.getVendorItemDescriptionStr();

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

        this.takenTimeStampStr =  this.PickorderLineEntity.getTakenTimeStampStr();
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

        this.quantityDbl = this.PickorderLineEntity.getQuantityDbl();
        this.quantityHandledDbl = this.PickorderLineEntity.getQuantityHandledDbl();
        this.quantityRejectedDbl = this.PickorderLineEntity.getQuantityRejected();

        this.sourceNoStr = this.PickorderLineEntity.getSourceNoStr();
        this.destinationNoStr = this.PickorderLineEntity.getDestinationNoStr();

        this.processingSequenceStr = this.PickorderLineEntity.getProcessingSequenceStr();

        this.vendorItemNo =  this.PickorderLineEntity.getVendorItemNoStr();
        this.vendorItemDescriptionStr =  this.PickorderLineEntity.getVendorItemDescriptionStr();

        this.statusInt =  this.PickorderLineEntity.getStatusInt();
        this.localStatusInt = this.PickorderLineEntity.getLocalstatusInt();

        if (this.statusInt > cWarehouseorder.PicklineStatusEnu.Needed) {
            this.localStatusInt = cWarehouseorder.PicklineLocalStatusEnu.LOCALSTATUS_DONE_SENT;
        }

        this.quantityTakenDbl =  this.PickorderLineEntity.getQuantityTakenDbl();
        this.takenTimeStampStr =  this.PickorderLineEntity.getTakenTimeStampStr();
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

        cPickorderLineViewModel pickorderLineViewModel =   new ViewModelProvider(cAppExtension.fragmentActivity).get(cPickorderLineViewModel.class);
        pickorderLineViewModel.deleteAll();
        return true;
    }

    public boolean pInsertInDatabaseBln() {
        this.getPickorderLineViewModel().insert(this.PickorderLineEntity);

        if (cPickorderLine.allLinesObl == null){
            cPickorderLine.allLinesObl = new ArrayList<>();
        }
        cPickorderLine.allLinesObl.add(this);
        return  true;
    }

    public boolean pUpdateProcessingSequenceBln(String pvProcessingSequenceStr) {

        boolean resultBln;
        resultBln =   this.getPickorderLineViewModel().pUpdateProcessingSequenceBln(pvProcessingSequenceStr);

        if (!resultBln) {
            return  false;
        }

        this.processingSequenceStr = pvProcessingSequenceStr;
        return true;

    }

    public cResult pLineBusyRst(){


        cResult result = new cResult();
        result.resultBln = false;

        if (cPickorderLine.currentPickOrderLine.mGetBarcodesObl() == null || Objects.requireNonNull(cPickorderLine.currentPickOrderLine.mGetBarcodesObl()).size() == 0) {
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

        if (this.mGetBarcodesObl() == null || Objects.requireNonNull(cPickorderLine.currentPickOrderLine.mGetBarcodesObl()).size() == 0) {
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

        if (this.getLocalStatusInt() == cWarehouseorder.PicklineLocalStatusEnu.LOCALSTATUS_DONE_SENT) {
            WebResult =  this.getPickorderLineViewModel().pResetViaWebserviceWrs();
            if (!WebResult.getResultBln() || !WebResult.getSuccessBln()) {
                cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_PICKORDERLINERESET);
                return  false;
            }
        }

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

    public void pHandledIndatabase(){

        if (!this.mUpdateQuanitityHandled(this.quantityHandledDbl)) {
            return;
        }

        if (!this.mUpdateLocalStatusBln(cWarehouseorder.PicklineLocalStatusEnu.LOCALSTATUS_DONE_NOTSENT)) {
            return;
        }

        this.takenTimeStampStr = cDateAndTime.pGetCurrentDateTimeForWebserviceStr();

        this.mUpdateHandledTimeStampBln(this.takenTimeStampStr);

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

    public void pCancelIndatabase(){


        cPickorderLine.currentPickOrderLine.quantityHandledDbl = 0.0;

        if (!this.mUpdateQuanitityHandled(this.quantityHandledDbl)) {
            return;
        }

        //Remove or update line barcodeStr
        cPickorderLine.currentPickOrderLine.pRemoveOrUpdateLineBarcode();
        if (!this.mUpdateLocalStatusBln(cWarehouseorder.PicklineLocalStatusEnu.LOCALSTATUS_NEW)) {
            return;
        }

        for (cPickorderLineBarcode pickorderLineBarcode : this.handledBarcodesObl()) {
            pickorderLineBarcode.pDeleteFromDatabaseBln();
        }

        this.takenTimeStampStr = "";

        this.mUpdateHandledTimeStampBln(this.takenTimeStampStr);


    }

    public void pAddOrUpdateLineBarcode(Double pvAmountDbl){

        boolean addBarcodeBln = false;

        //If there are no line barcodes, then simply add this one
        if (this.handledBarcodesObl() == null || this.handledBarcodesObl().size() == 0) {
            addBarcodeBln = true;
        }

        if (!addBarcodeBln) {
            for (cPickorderLineBarcode pickorderLineBarcode : this.handledBarcodesObl()  ) {

                if (pickorderLineBarcode.getBarcodeStr().equalsIgnoreCase(cPickorderBarcode.currentPickorderBarcode.getBarcodeStr())) {
                    pickorderLineBarcode.quantityHandledDbl += pvAmountDbl;
                    pickorderLineBarcode.pUpdateAmountInDatabase();
                    return;
                }
            }
        }

        cPickorderLineBarcode pickorderLineBarcode = new cPickorderLineBarcode(cPickorderLine.currentPickOrderLine.getLineNoInt().longValue(), cPickorderBarcode.currentPickorderBarcode.getBarcodeStr());
        pickorderLineBarcode.quantityHandledDbl = pvAmountDbl;
        pickorderLineBarcode.pInsertInDatabaseBln();

    }

    public void pRemoveOrUpdateLineBarcode(){

        //If there are no line barcodes, this should not be possible
        if (this.handledBarcodesObl() == null || this.handledBarcodesObl().size() == 0) {
            return;
        }

        for (cPickorderLineBarcode pickorderLineBarcode : this.handledBarcodesObl()  ) {

            if (pickorderLineBarcode.getBarcodeStr().equalsIgnoreCase(cPickorderBarcode.currentPickorderBarcode.getBarcodeStr())) {

                pickorderLineBarcode.quantityHandledDbl -= cPickorderBarcode.currentPickorderBarcode.getQuantityPerUnitOfMeasureDbl();

                if (pickorderLineBarcode.getQuantityhandledDbl() > 0) {
                    pickorderLineBarcode.pUpdateAmountInDatabase();
                    return;
                }

                pickorderLineBarcode.pDeleteFromDatabaseBln();
                return;
            }
        }
    }

    public boolean pHandledBln() {

        cWebresult WebResult;
        WebResult =  this.getPickorderLineViewModel().pPickLineHandledViaWebserviceWrs();
        if (WebResult.getResultBln() && WebResult.getSuccessBln()){

            return this.mUpdateLocalStatusBln(cWarehouseorder.PicklineLocalStatusEnu.LOCALSTATUS_DONE_SENT);
        }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_PICKORDERLINE_HANDLED);
            return  false;
        }
    }

    public boolean pSortedBln() {

        cWebresult WebResult;
        WebResult =  this.getPickorderLineViewModel().pPickLineSortedViaWebserviceWrs();
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

    public String pGetPackingTableForSourceNoStr() {

        if (cPickorder.currentPickOrder.salesOrderPackingTableObl() == null || cPickorder.currentPickOrder.salesOrderPackingTableObl().size() == 0) {
            return "";
        }

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
        WebResult =  this.getPickorderLineViewModel().pGetSortLocationAdviceViaWebserviceWrs(this.getSourceNoStr());
        if (WebResult.getResultBln() && WebResult.getSuccessBln()){

            resultObl.addAll(WebResult.getResultObl());

            return  resultObl;
        }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETSORTLOCATIONADVICE);
            return  null;
        }
    }

    //End Region Public Methods

    //Region Private Methods

    private boolean mUpdateLocalStatusBln(Integer pvNewStatusInt) {

        boolean resultBln;
        resultBln =   this.getPickorderLineViewModel().pUpdateLocalStatusBln(pvNewStatusInt);

        if (!resultBln) {
            return  false;
        }

        this.localStatusInt = pvNewStatusInt;
        return true;

    }

    private boolean mUpdateQuanitityHandled(double pvQuantityHandledBln) {

        boolean resultBln;
        resultBln =   this.getPickorderLineViewModel().pUpdateQuantityHandledBln(pvQuantityHandledBln);

        if (!resultBln) {
            return  false;
        }

        this.quantityHandledDbl = pvQuantityHandledBln;
        return true;

    }

    private boolean mUpdateHandledTimeStampBln(String pvHandledTimeStampStr) {

        boolean resultBln;
        resultBln =   this.getPickorderLineViewModel().pUpdateHandledTimeStampBln(pvHandledTimeStampStr);

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


