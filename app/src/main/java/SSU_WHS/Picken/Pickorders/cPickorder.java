package SSU_WHS.Picken.Pickorders;

import androidx.lifecycle.ViewModelProviders;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ICS.Utils.cDeviceInfo;
import ICS.Utils.cResult;
import ICS.Utils.cSharedPreferences;
import ICS.Utils.cText;
import ICS.Weberror.cWeberror;
import SSU_WHS.Basics.ArticleImages.cArticleImage;
import SSU_WHS.Basics.Settings.cSetting;
import SSU_WHS.Basics.Users.cUser;
import SSU_WHS.Basics.Workplaces.cWorkplace;
import SSU_WHS.General.Comments.cComment;
import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.Picken.PickorderLineBarcodes.cPickorderLineBarcode;
import nl.icsvertex.scansuite.R;
import nl.icsvertex.scansuite.cAppExtension;
import SSU_WHS.Picken.PickorderAddresses.cPickorderAddress;
import SSU_WHS.Picken.PickorderBarcodes.cPickorderBarcode;
import SSU_WHS.Picken.PickorderLines.cPickorderLine;
import SSU_WHS.Picken.PickorderLines.cPickorderLineEntity;
import SSU_WHS.Picken.SalesOrderPackingTable.cSalesOrderPackingTable;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebserviceDefinitions;

public class cPickorder {

    //Region Public Properties

    public String orderNumberStr;
    public String getOrderNumberStr() {
        return orderNumberStr;
    }

    public String orderTypeStr;
    public String getOrderTypeStr() {
        return orderTypeStr;
    }

    public int numberOfBinsInt;
    public int getNumberOfBinsInt() {
        return numberOfBinsInt;
    }

    public int quantityTotalInt;
    public int getQuantityTotalInt() {
        return quantityTotalInt;
    }

    public boolean singleArticleOrdersBln;
    public boolean isSingleArticleOrdersBln() {
        return singleArticleOrdersBln;
    }

    public boolean pickRejectDuringPickBln;
    public boolean isPickRejectDuringPickBln() {
        return pickRejectDuringPickBln;
    }

    public boolean pickRejectDuringSortBln;
    public boolean isPickRejectDuringSortBln() {
        return pickRejectDuringSortBln;
    }

    public boolean pickSalesAskWorkplaceBln;
    public boolean isPickSalesAskWorkplaceBln() {
        return pickSalesAskWorkplaceBln;
    }

    public boolean pickTransferAskWorkplaceBln;
    public boolean isPickTransferAskWorkplaceBln() {
        return pickTransferAskWorkplaceBln;
    }

    public boolean pickBarcodeCheckBln;
    public boolean isPickBarcodeCheckBln() {
        return pickBarcodeCheckBln;
    }

    public boolean pickPickPVVKOEachPieceBln;
    public boolean isPickPickPVVKOEachPieceBln() {
        return pickPickPVVKOEachPieceBln;
    }

    public boolean pickPickToContainerBln;
    public boolean isPickPickToContainerBln() {
        return pickPickToContainerBln;
    }

    public String pickPickToContainerTypeStr;
    public String getPickPickToContainerTypeStr() {
        return pickPickToContainerTypeStr;
    }

    public boolean pickPickWithPictureBln;
    public boolean isPickWithPictureBln() {
        return pickPickWithPictureBln;
    }

    public boolean pickPickWithAutoOpenBln;
    public boolean isPickWithPictureAutoOpenBln() {
        return pickPickWithAutoOpenBln;
    }

    public boolean pickPickWithPicturePrefetchBln;
    public boolean isPickPickWithPicturePrefetchBln() {
        return pickPickWithPicturePrefetchBln;
    }

    public boolean pickPrintAddressLabelBln;
    public boolean isPickPrintAddressLabelBln() {
        return pickPrintAddressLabelBln;
    }

    public boolean pickPrintContentLabelBln;
    public boolean isPickPrintContentLabelBln() {
        return pickPrintContentLabelBln;
    }

    public boolean pickActivityBinRequiredBln;
    public boolean ispickActivityBinRequiredBln() {
        return pickActivityBinRequiredBln;
    }

    public String assignedUserIdStr;
    public String getAssignedUserIdStr() {
        return assignedUserIdStr;
    }

    public String currentUserIdStr;
    public String getCurrentUserIdStr() {
        return currentUserIdStr;
    }

    public int statusInt;
    public int getStatusInt() {
        return statusInt;
    }

    public int statusPrintAtStartInt;
    public int getStatusPrintAtStartInt() {
        return statusPrintAtStartInt;
    }

    public String externalReferenceStr;
    public String getExternalReferenceStr() {
        return externalReferenceStr;
    }

    public String workPlaceStr;
    public String getWorkPlaceStr() {
        return workPlaceStr;
    }

    public String stockOwnerStr;
    public String getStockOwnerStr() {
        return stockOwnerStr;
    }

    public String requestedEndDateTimeStr;
    public String getRequestedEndDateTimeStr() {
        return requestedEndDateTimeStr;
    }

    public String sourceDocumentStr;
    public String getSourceDocumentStr() {
        return sourceDocumentStr;
    }

    public String documentStr;
    public String getDocumentStr() {
        return documentStr;
    }

    public String documentTypeStr;
    public String getDocumentTypeStr() {
        return documentTypeStr;
    }

    public String document2Str;
    public String getDocument2Str() {
        return document2Str;
    }

    public String documentType2Str;
    public String getDocumentType2Str() {
        return documentType2Str;
    }

    public int webserviceTimeOutERPInsInt;
    public int getWebserviceTimeOutERPInsInt() {
        return webserviceTimeOutERPInsInt;
    }

    public String currentLocationStr;
    public String getCurrentLocationStr() {
        return currentLocationStr;
    }

    public String interfaceResultMethodStr;
    public String getInterfaceResultMethodStr() {
        return interfaceResultMethodStr;
    }

    public String sortingStr;
    public String getSortingStr() {
        return sortingStr;
    }

    public Boolean isProcessingOrParkedBln;
    public Boolean getProcessingOrParkedBln() {
        return isProcessingOrParkedBln;
    }

    public List<String> errorMessagesObl;
    public List<String> getErrorMessageObl() {
        return errorMessagesObl;
    }

    public Boolean isBMBln() {

        if (this.getOrderTypeStr().equalsIgnoreCase(cWarehouseorder.WorkflowEnu.BM.toString())) {
            return  true;
        }

        return  false;

    }

    public Boolean isPVBln() {

        if (this.getOrderTypeStr().equalsIgnoreCase(cWarehouseorder.WorkflowEnu.PV.toString())) {
            return  true;
        }

        return  false;

    }

    public Boolean isBCBln() {

        if (this.getOrderTypeStr().equalsIgnoreCase(cWarehouseorder.WorkflowEnu.BC.toString())) {
            return  true;
        }

        return  false;

    }

    public Boolean isBPBln() {

        if (this.getOrderTypeStr().equalsIgnoreCase(cWarehouseorder.WorkflowEnu.BP.toString())) {
            return  true;
        }

        return  false;

    }

    public boolean PackAndShipNeededBln(){

        if (this.linesObl() == null || this.linesObl().size() == 0 ) {
            return  false;
        }

        for (cPickorderLine pickorderLine : this.linesObl()) {

            if (pickorderLine.statusShippingInt != cWarehouseorder.PackingAndShippingStatusEnu.NotNeeded || pickorderLine.statusPackingInt != cWarehouseorder.PackingAndShippingStatusEnu.NotNeeded)
                if (pickorderLine.quantityHandledDbl > 0) {
                    return  true;
                }
        }

        return  false;

    }

    public boolean SortNeededBln(){

        if (this.isBPBln() || this.isBCBln()) {
            return  true;
        }

        return  false;

    }

    public cPickorderEntity pickorderEntity;
    public boolean indatabaseBln;

    public static cPickorderViewModel gPickorderViewModel;

    public static cPickorderViewModel getPickorderViewModel() {
        if (gPickorderViewModel == null) {
            gPickorderViewModel = ViewModelProviders.of(cAppExtension.fragmentActivity ).get(cPickorderViewModel.class);
        }
        return gPickorderViewModel;
    }

    public static cPickorderAdapter gPickorderAdapter;
    public static cPickorderAdapter getPickorderAdapter() {
        if (gPickorderAdapter == null) {
            gPickorderAdapter = new cPickorderAdapter();
        }
        return gPickorderAdapter;
    }

    public static List<cPickorder> allPickordersObl;
    public static cPickorder currentPickOrder;

    public List<cPickorderLine> linesObl(){
        return  cPickorderLine.allLinesObl;
    }

    public List<cPickorderAddress> adressesObl(){
     return  cPickorderAddress.allAdressesObl ;
    }

    public List<cArticleImage> imagesObl()  {
        return  cArticleImage.allImages;
    }

    public List<cPickorderBarcode> barcodesObl() {
        return  cPickorderBarcode.allBarcodesObl;
    }

    public List<cComment> commentsObl(){
        return  cComment.allCommentsObl;
    }

    public List<cSalesOrderPackingTable> salesOrderPackingTableObl() {
        return  cSalesOrderPackingTable.allSalesOrderPackingTabelsObl;
    }

    //End region Public Properties

    //Region Constructor
    cPickorder(JSONObject pvJsonObject){

        this.pickorderEntity = new cPickorderEntity(pvJsonObject, false);
        this.orderNumberStr = this.pickorderEntity.getOrdernumberStr();
        this.orderTypeStr = this.pickorderEntity.getOrderTypeStr();
        this.numberOfBinsInt = cText.stringToInteger(this.pickorderEntity.getNumberofBinsStr()) ;
        this.quantityTotalInt =cText.stringToInteger(this.pickorderEntity.getQuantityTotalStr());
        this.singleArticleOrdersBln = cText.stringToBoolean(this.pickorderEntity.getSingleArticleOrdersStr(),false) ;
        this.pickRejectDuringPickBln = cText.stringToBoolean(this.pickorderEntity.getPickRejectDuringpickStr(),false) ;
        this.pickRejectDuringSortBln = cText.stringToBoolean(this.pickorderEntity.getPickRejectDuringSortStr(),false) ;
        this.pickSalesAskWorkplaceBln = cText.stringToBoolean(this.pickorderEntity.getPickSalesAskWorkplaceStr(),false) ;
        this.pickTransferAskWorkplaceBln = cText.stringToBoolean(this.pickorderEntity.getPickTransferAskWorkplaceStr(),false) ;
        this.pickBarcodeCheckBln  = cText.stringToBoolean(this.pickorderEntity.getPickBarcodeCheckStr(),false) ;
        this.pickPickPVVKOEachPieceBln = cText.stringToBoolean(this.pickorderEntity.getPickPickPVVKOEachPieceStr(),false) ;
        this.pickPickToContainerBln = cText.stringToBoolean(this.pickorderEntity.getPickPickToContainerStr(),false) ;
        this.pickPickToContainerTypeStr = this.pickorderEntity.getPickPickToContainerTypeStr();
        this.pickPrintAddressLabelBln = cText.stringToBoolean(this.pickorderEntity.getPickPrintAddresslabelStr(),false) ;
        this.pickPrintContentLabelBln = cText.stringToBoolean(this.pickorderEntity.getPickPrintContentLabelStr(),false) ;
        this.pickPickWithPictureBln = cText.stringToBoolean(this.pickorderEntity.getpickWithPictureStr(),false) ;
        this.pickPickWithAutoOpenBln = cText.stringToBoolean(this.pickorderEntity.getPickWithPictureAutoOpenStr(),false) ;
        this.pickPickWithPicturePrefetchBln = cText.stringToBoolean(this.pickorderEntity.getPickWithPicturePrefetchStr(),false) ;
        this.pickActivityBinRequiredBln = cText.stringToBoolean(this.pickorderEntity.getPickActivityBinRequired(),false);

        this.assignedUserIdStr = this.pickorderEntity.getAssignedUserIdStr();
        this.currentUserIdStr = this.pickorderEntity.getCurrentUserIdStr();
        this.statusInt = cText.stringToInteger(this.pickorderEntity.getStatusStr());
        this.statusPrintAtStartInt = cText.stringToInteger(this.pickorderEntity.getStatusPrintAtStartStr());
        this.externalReferenceStr = this.pickorderEntity.getExternalReferenceStr();
        this.workPlaceStr = this.pickorderEntity.getWorkplaceStr();
        this.stockOwnerStr = this.pickorderEntity.getStockOwnerStr();
        this.requestedEndDateTimeStr = this.pickorderEntity.getRequestedEndDateTimeStr();
        this.sourceDocumentStr = this.pickorderEntity.getSourceDocumentStr();
        this.documentStr = this.pickorderEntity.getDocumentStr();
        this.documentTypeStr = this.pickorderEntity.getDocumentTypeStr();
        this.document2Str = this.pickorderEntity.getDocument2Str();
        this.documentType2Str = this.pickorderEntity.getDocumentType2Str();
        this.webserviceTimeOutERPInsInt = cText.stringToInteger(this.pickorderEntity.getWebserviceTimeOutERPInsStr());
        this.currentLocationStr = this.pickorderEntity.getCurrentLocationStr();
        this.interfaceResultMethodStr = this.pickorderEntity.getInterfaceResultMethodStr();
        this.sortingStr = this.pickorderEntity.getSortingStr();
        this.isProcessingOrParkedBln = this.pickorderEntity.getIsProcessingOrParkedStr();
    }

    cPickorder(cPickorderEntity pvPickorderEntity){

        this.pickorderEntity = pvPickorderEntity;
        this.orderNumberStr = this.pickorderEntity.getOrdernumberStr();
        this.orderTypeStr = this.pickorderEntity.getOrderTypeStr();
        this.numberOfBinsInt = cText.stringToInteger(this.pickorderEntity.getNumberofBinsStr()) ;
        this.quantityTotalInt =cText.stringToInteger(this.pickorderEntity.getQuantityTotalStr());
        this.singleArticleOrdersBln = cText.stringToBoolean(this.pickorderEntity.getSingleArticleOrdersStr(),false) ;
        this.pickRejectDuringPickBln = cText.stringToBoolean(this.pickorderEntity.getPickRejectDuringpickStr(),false) ;
        this.pickRejectDuringSortBln = cText.stringToBoolean(this.pickorderEntity.getPickRejectDuringSortStr(),false) ;
        this.pickSalesAskWorkplaceBln = cText.stringToBoolean(this.pickorderEntity.getPickSalesAskWorkplaceStr(),false) ;
        this.pickTransferAskWorkplaceBln = cText.stringToBoolean(this.pickorderEntity.getPickTransferAskWorkplaceStr(),false) ;
        this.pickBarcodeCheckBln  = cText.stringToBoolean(this.pickorderEntity.getPickBarcodeCheckStr(),false) ;
        this.pickPickPVVKOEachPieceBln = cText.stringToBoolean(this.pickorderEntity.getPickPickPVVKOEachPieceStr(),false) ;
        this.pickPickToContainerBln = cText.stringToBoolean(this.pickorderEntity.getPickPickToContainerStr(),false) ;
        this.pickPickToContainerTypeStr = this.pickorderEntity.getPickPickToContainerTypeStr();
        this.pickPrintAddressLabelBln = cText.stringToBoolean(this.pickorderEntity.getPickPrintAddresslabelStr(),false) ;
        this.pickPrintContentLabelBln = cText.stringToBoolean(this.pickorderEntity.getPickPrintContentLabelStr(),false) ;
        this.pickPickWithPictureBln = cText.stringToBoolean(this.pickorderEntity.getpickWithPictureStr(),false) ;
        this.pickPickWithAutoOpenBln = cText.stringToBoolean(this.pickorderEntity.getPickWithPictureAutoOpenStr(),false) ;
        this.pickPickWithPicturePrefetchBln = cText.stringToBoolean(this.pickorderEntity.getPickWithPicturePrefetchStr(),false) ;
        this.pickActivityBinRequiredBln = cText.stringToBoolean(this.pickorderEntity.getPickActivityBinRequired(),false);
        this.assignedUserIdStr = this.pickorderEntity.getAssignedUserIdStr();
        this.currentUserIdStr = this.pickorderEntity.getCurrentUserIdStr();
        this.statusInt = cText.stringToInteger(this.pickorderEntity.getStatusStr());
        this.statusPrintAtStartInt = cText.stringToInteger(this.pickorderEntity.getStatusPrintAtStartStr());
        this.externalReferenceStr = this.pickorderEntity.getExternalReferenceStr();
        this.workPlaceStr = this.pickorderEntity.getWorkplaceStr();
        this.stockOwnerStr = this.pickorderEntity.getStockOwnerStr();
        this.requestedEndDateTimeStr = this.pickorderEntity.getRequestedEndDateTimeStr();
        this.sourceDocumentStr = this.pickorderEntity.getSourceDocumentStr();
        this.documentStr = this.pickorderEntity.getDocumentStr();
        this.documentTypeStr = this.pickorderEntity.getDocumentTypeStr();
        this.document2Str = this.pickorderEntity.getDocument2Str();
        this.documentType2Str = this.pickorderEntity.getDocumentType2Str();
        this.webserviceTimeOutERPInsInt = cText.stringToInteger(this.pickorderEntity.getWebserviceTimeOutERPInsStr());
        this.currentLocationStr = this.pickorderEntity.getCurrentLocationStr();
        this.interfaceResultMethodStr = this.pickorderEntity.getInterfaceResultMethodStr();
        this.sortingStr = this.pickorderEntity.getSortingStr();
        this.isProcessingOrParkedBln = this.pickorderEntity.getIsProcessingOrParkedStr();
    }
    //End Region Constructor

    //Region Public Methods

    public boolean pInsertInDatabaseBln() {
        cPickorder.getPickorderViewModel().insert(this.pickorderEntity);
        this.indatabaseBln = true;

        if (cPickorder.allPickordersObl == null){
            cPickorder.allPickordersObl = new ArrayList<>();
        }
        cPickorder.allPickordersObl.add(this);
        return  true;
    }

    public cResult pLockViaWebserviceRst(cWarehouseorder.StepCodeEnu pvStepCodeEnu, int pvWorkFlowStepInt ) {

        //Initialise result
        cResult result;
        result = new cResult();
        result.resultBln = true;

        cWebresult Webresult;
        Boolean ignoreBusyBln = false;

        if (cSetting.GENERIC_UNLOCK_BUSY_ORDERS_ALLOWED() == false) {
            ignoreBusyBln = false;
        } else {

            if (this.getStatusInt() > 10 && cUser.currentUser.getNameStr().equalsIgnoreCase(this.getCurrentUserIdStr())) {
                ignoreBusyBln = true;
            }
        }

        Webresult = cWarehouseorder.getWarehouseorderViewModel().pLockWarehouseopdrachtViaWebserviceWrs(cWarehouseorder.OrderTypeEnu.PICKEN.toString(),this.getOrderNumberStr(), cDeviceInfo.getSerialnumber(),pvStepCodeEnu.toString(),pvWorkFlowStepInt, ignoreBusyBln);

        //No result, so something really went wrong
        if (Webresult == null) {
            result.resultBln = false;
            result.activityActionEnu = cWarehouseorder.ActivityActionEnu.Unknown;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_couldnt_lock_order));
            return result;
        }

        //Everything was fine, so we are done
        if (Webresult.getSuccessBln() == true && Webresult.getResultBln() == true) {
            result.resultBln = true;
            return result;
        }

        //Something really went wrong
        if (Webresult.getSuccessBln() == false ) {
            result.resultBln = false;
            result.activityActionEnu = cWarehouseorder.ActivityActionEnu.Unknown;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_couldnt_lock_order));
            return result;
        }

        //Check if this activity is meant for a different user
        if (Webresult.getResultBln() == false && Webresult.getResultLng() <= 0 && Webresult.getResultObl() != null &&
            Webresult.getResultObl().size() >= 2 && Webresult.getResultObl().get(0).equalsIgnoreCase("invalid_user_not_assigned") == true ) {
            result.resultBln = false;
            result.activityActionEnu = cWarehouseorder.ActivityActionEnu.Unknown;
            result.pAddErrorMessage(cAppExtension.context.getString((R.string.message_meant_for_other_user)) + " " + Webresult.getResultObl().get(1) );
            return  result;
        }

        //Check if this activity is meant for a different user
        if (Webresult.getResultBln() == false && Webresult.getResultLng() <= 0 && Webresult.getResultObl() != null &&
            Webresult.getResultObl().size() >= 0 && Webresult.getResultObl().get(0).equalsIgnoreCase(cUser.currentUser.getNameStr()) == false ) {
            result.resultBln = false;
            result.activityActionEnu = cWarehouseorder.ActivityActionEnu.Unknown;
            result.pAddErrorMessage(cAppExtension.context.getString((R.string.message_another_user_already_started)) + " " + Webresult.getResultObl().get(0) );
            return  result;
        }

        // We got a succesfull response, but we need to do something with this activity
        if (Webresult.getResultBln() == false && Webresult.getResultLng() > 0 ) {

            //Try to convert result long to action enumerate
            cWarehouseorder.ActivityActionEnu activityActionEnu = cWarehouseorder.pGetActivityActionEnu(Webresult.getResultLng().intValue());

            result.resultBln = false;
            result.activityActionEnu = activityActionEnu;

            if (result.activityActionEnu == cWarehouseorder.ActivityActionEnu.Delete) {
                result.pAddErrorMessage(cAppExtension.context.getString((R.string.order_will_be_deleted)));
            }

            if (result.activityActionEnu == cWarehouseorder.ActivityActionEnu.NoStart) {
                result.pAddErrorMessage(cAppExtension.context.getString((R.string.order_cant_be_started)));
            }

            cPickorder.currentPickOrder.pGetCommentsViaWebErrorBln(Webresult.getResultDtt());
            return result;
        }

        return  result;

    }

    public boolean pLockReleaseViaWebserviceBln(cWarehouseorder.StepCodeEnu pvStepCodeEnu, int pvWorkFlowStepInt) {

        cWebresult Webresult;

        Webresult = cWarehouseorder.getWarehouseorderViewModel().pLockReleaseWarehouseorderViaWebserviceWrs(cWarehouseorder.OrderTypeEnu.PICKEN.toString(),this.getOrderNumberStr(),cDeviceInfo.getSerialnumber(),pvStepCodeEnu.toString(), pvWorkFlowStepInt);

        if (Webresult.getSuccessBln() == false || Webresult.getResultBln() == false) {
            return  false;
        }

        return  true;
    }

    public boolean pDeleteDetailsBln(){

        cPickorderLine.pTruncateTableBln();
        cPickorderBarcode.pTruncateTableBln();
        cPickorderLineBarcode.pTruncateTableBln();
        cPickorderAddress.pTruncateTableBln();
        cSalesOrderPackingTable.pTruncateTable();

        return  true;
    }

    public boolean pAbortBln() {
        cPickorder.getPickorderViewModel().pAbortOrder();
        return true;
    }

    public boolean pUpdateCurrentLocationBln(String pvCurrentLocationStr) {


        cWebresult Webresult;

        Webresult = cPickorder.getPickorderViewModel().pUpdateCurrentLocationViaWebserviceWrs(pvCurrentLocationStr);
        if (Webresult.getSuccessBln() == false || Webresult.getResultBln() == false) {
            return  false;
        }

        if (cPickorder.getPickorderViewModel().pUpdatePickorderCurrentLocationInDatabaseBln(pvCurrentLocationStr) == false) {
            return  false;
        }

        this.currentLocationStr = pvCurrentLocationStr;
        return  true;

    }

    public double pQuantityNotHandledDbl() {
        Double resultDbl =  cPickorder.getPickorderViewModel().pQuantityNotHandledDbl();
        if (resultDbl == null) {
            resultDbl  = 0.0;
        }

        return  resultDbl;

    }

    public double pQuantityHandledDbl() {

        Double resultDbl = cPickorder.getPickorderViewModel().pQuantityHandledCounterDbl();

        if (resultDbl == null) {
            resultDbl  = 0.0;
        }

        return  resultDbl;
    }

    public double pQuantityTotalDbl() {
        return  cPickorder.getPickorderViewModel().pGetNumberTotalForCounterDbl();
    }

    public List<cComment> pPickCommentObl(){

        if (cPickorder.currentPickOrder.commentsObl() == null || cPickorder.currentPickOrder.commentsObl().size() == 0) {
            return  null;
        }

        List<cComment> hulpObl = cComment.pGetCommentsForTypeObl(cWarehouseorder.CommentTypeEnu.PICK);
        return hulpObl;

    }

    public List<cComment> pSortCommentObl(){

        if (cPickorder.currentPickOrder.commentsObl() == null || cPickorder.currentPickOrder.commentsObl().size() == 0) {
            return  null;
        }

        List<cComment> hulpObl = cComment.pGetCommentsForTypeObl(cWarehouseorder.CommentTypeEnu.SORT);
        return hulpObl;

    }

    public List<cComment> pFeedbackCommentObl(){

        if (cPickorder.currentPickOrder.commentsObl() == null || cPickorder.currentPickOrder.commentsObl().size() == 0) {
            return  null;
        }

        List<cComment> hulpObl = cComment.pGetCommentsForTypeObl(cWarehouseorder.CommentTypeEnu.FEEDBACK);
        return hulpObl;

    }

    public boolean pGetLinesViaWebserviceBln(Boolean pvRefreshBln,cWarehouseorder.PickOrderTypeEnu pvPickOrderTypeEnu) {

        if (pvRefreshBln == true) {
            cPickorderLine.allLinesObl = null;
            cPickorderLine.pTruncateTableBln();
        }

        cWebresult WebResult;
        WebResult =  cPickorder.getPickorderViewModel().pGetLinesFromWebserviceWrs(cWarehouseorder.ActionTypeEnu.TAKE);
        if (WebResult.getResultBln() == true && WebResult.getSuccessBln() == true ){


            List<JSONObject> myList = WebResult.getResultDtt();
            for (int i = 0; i < myList.size(); i++) {
                JSONObject jsonObject;
                jsonObject = myList.get(i);

                if (cPickorderLine.allLinesObl == null) {
                    cPickorderLine.allLinesObl = new ArrayList<>();
                }

                cPickorderLine pickorderLine = new cPickorderLine(jsonObject,pvPickOrderTypeEnu);

                if (pvPickOrderTypeEnu == cWarehouseorder.PickOrderTypeEnu.PICK) {
                    pickorderLine.pInsertInDatabaseBln();
                    continue;
                }


                //Check the status, so lines that are not picked are ignored
                if (pvPickOrderTypeEnu == cWarehouseorder.PickOrderTypeEnu.SORT && pickorderLine.statusPackingInt == cWarehouseorder.PackingAndShippingStatusEnu.Needed) {
                    pickorderLine.pInsertInDatabaseBln();
                    continue;
                }
            }

            if (cPickorderLine.allLinesObl.size() == 0) {
                return  false;
            }else
            {
                return  true;
            }

        }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETPICKORDERS);
            return  false;
        }
    }

    public List<cPickorderLine> pGetLinesFromDatabasObl() {

        List<cPickorderLine> resultObl = new ArrayList<>();
        List<cPickorderLineEntity> hulpResultObl;

        hulpResultObl =  cPickorder.getPickorderViewModel().pGetAllLinesFromDatabaseObl();
        if (hulpResultObl == null || hulpResultObl.size() == 0) {
            return  resultObl;
        }

        for (cPickorderLineEntity pickorderLineEntity : hulpResultObl ) {
            cPickorderLine PickorderLine = new cPickorderLine(pickorderLineEntity);
            resultObl.add(PickorderLine);
        }
        return  resultObl;
    }

    public List<cPickorderLine> pGetLinesToSendFromDatabasObl() {

        List<cPickorderLine> resultObl = new ArrayList<>();
        List<cPickorderLineEntity> hulpResultObl;

        hulpResultObl =  cPickorder.getPickorderViewModel().pGetPickorderLinesToSendFromDatabaseObl();
        if (hulpResultObl == null || hulpResultObl.size() == 0) {
            return  resultObl;
        }

        for (cPickorderLineEntity pickorderLineEntity : hulpResultObl ) {
            cPickorderLine PickorderLine = new cPickorderLine(pickorderLineEntity);
            resultObl.add(PickorderLine);
        }
        return  resultObl;
    }

    public List<cPickorderLine> pGetLinesNotHandledFromDatabasObl() {

        List<cPickorderLine> resultObl = new ArrayList<>();
        List<cPickorderLineEntity> hulpResultObl;

        hulpResultObl =  cPickorder.getPickorderViewModel().pGetLinesNotHandledFromDatabaseObl();
        if (hulpResultObl == null || hulpResultObl.size() == 0) {
            return  resultObl;
        }

        for (cPickorderLineEntity pickorderLineEntity : hulpResultObl ) {
            cPickorderLine PickorderLine = new cPickorderLine(pickorderLineEntity);
            resultObl.add(PickorderLine);
        }
        return  resultObl;
    }

    public List<cPickorderLine> pGetLinesHandledFromDatabasObl() {

        List<cPickorderLine> resultObl = new ArrayList<>();
        List<cPickorderLineEntity> hulpResultObl;

        hulpResultObl =  cPickorder.getPickorderViewModel().pGetLinesHandledFromDatabaseObl();
        if (hulpResultObl == null || hulpResultObl.size() == 0) {
            return  resultObl;
        }

        for (cPickorderLineEntity pickorderLineEntity : hulpResultObl ) {
            cPickorderLine PickorderLine = new cPickorderLine(pickorderLineEntity);
            resultObl.add(PickorderLine);
        }
        return  resultObl;
    }

    public cPickorderLine pGetNetxLineToHandleForBin(String pvBinCodeStr) {

        List <cPickorderLine> hulpObl = this.pGetLinesNotHandledFromDatabasObl();

        if (hulpObl == null || hulpObl.size() ==0 ) {
            return  null;
        }

        for (cPickorderLine pickorderLine : hulpObl) {

            if (pickorderLine.getBinCodeStr().equalsIgnoreCase(pvBinCodeStr)) {
                return  pickorderLine;
            }
        }

        return  null;

    }

    public static boolean pGetPickOrdersViaWebserviceBln(Boolean pvRefreshBln, Boolean pvProcessingOrParkedBln, String pvSearchTextStr) {

        if (pvRefreshBln == true) {
            cPickorder.allPickordersObl = null;
            cPickorder.mTruncateTableBln();
        }

        cWebresult WebResult;
        WebResult =  cPickorder.getPickorderViewModel().pGetPickordersFromWebserviceWrs(pvProcessingOrParkedBln,pvSearchTextStr);
        if (WebResult.getResultBln() == true && WebResult.getSuccessBln() == true ){


            List<JSONObject> myList = WebResult.getResultDtt();
            for (int i = 0; i < myList.size(); i++) {
                JSONObject jsonObject;
                jsonObject = myList.get(i);

                cPickorder pickorder = new cPickorder(jsonObject);
                pickorder.pInsertInDatabaseBln();
            }
            return  true;
        }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETPICKORDERS);
            return  false;
        }
    }

    public static boolean pGetSortOrdersViaWebserviceBln(Boolean pvRefreshBln,String pvSearchTextStr) {

        if (pvRefreshBln == true) {
            cPickorder.allPickordersObl = null;
            cPickorder.mTruncateTableBln();
        }

        cWebresult WebResult;
        WebResult =  cPickorder.getPickorderViewModel().pGetSortOrShipordersFromWebserviceWrs("",cWarehouseorder.StepCodeEnu.Pick_Sorting,pvSearchTextStr);
        if (WebResult.getResultBln() == true && WebResult.getSuccessBln() == true ){


            List<JSONObject> myList = WebResult.getResultDtt();
            for (int i = 0; i < myList.size(); i++) {
                JSONObject jsonObject;
                jsonObject = myList.get(i);

                cPickorder pickorder = new cPickorder(jsonObject);
                pickorder.pInsertInDatabaseBln();
            }
            return  true;
        }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETPICKORDERS);
            return  false;
        }
    }

    public static boolean pGetPackAndShipOrdersViaWebserviceBln(Boolean pvRefreshBln, String pvSearchTextStr) {

        if (pvRefreshBln == true) {
            cPickorder.allPickordersObl = null;
            cPickorder.mTruncateTableBln();
        }

        cWebresult WebResult;
        WebResult =  cPickorder.getPickorderViewModel().pGetSortOrShipordersFromWebserviceWrs("",cWarehouseorder.StepCodeEnu.Pick_PackAndShip,pvSearchTextStr);
        if (WebResult.getResultBln() == true && WebResult.getSuccessBln() == true ){


            List<JSONObject> myList = WebResult.getResultDtt();
            for (int i = 0; i < myList.size(); i++) {
                JSONObject jsonObject;
                jsonObject = myList.get(i);

                cPickorder pickorder = new cPickorder(jsonObject);
                pickorder.pInsertInDatabaseBln();
            }
            return  true;
        }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETPICKORDERS);
            return  false;
        }
    }

    public cResult pPickHandledViaWebserviceRst() {

        cResult result;
        result = new cResult();
        result.resultBln = true;

        String workplaceStr = "";

        cWebresult webresult;

        if (cWorkplace.currentWorkplace != null) {
            workplaceStr = cWorkplace.currentWorkplace.getWorkplaceStr();
        }

        webresult =  cPickorder.getPickorderViewModel().pPickenHandledViaWebserviceWrs(workplaceStr);

        //No result, so something really went wrong
        if (webresult == null) {
            result.resultBln = false;
            result.activityActionEnu = cWarehouseorder.ActivityActionEnu.Unknown;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_couldnt_handle_step));
            return result;
        }

        //Everything was fine, so we are done
        if (webresult.getSuccessBln() == true && webresult.getResultBln() == true) {
            result.resultBln = true;
            return result;
        }

        //Something really went wrong
        if (webresult.getSuccessBln() == false ) {
            result.resultBln = false;
            result.activityActionEnu = cWarehouseorder.ActivityActionEnu.Unknown;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_couldnt_handle_step));
            return result;
        }

        // We got a succesfull response, but we need to do something with this activity
        if (webresult.getResultBln() == false && webresult.getResultLng() > 0 ) {

            Long actionLng = Long.valueOf(0);

            if (webresult.getResultLng() < 10 ) {
                actionLng = webresult.getResultLng();
            }

            if (webresult.getResultLng() > 100) {
                actionLng  = (long)(webresult.getResultLng()/100);
            }

            //Try to convert action long to action enumerate
            cWarehouseorder.ActivityActionEnu activityActionEnu = cWarehouseorder.pGetActivityActionEnu(actionLng.intValue());

            result.resultBln = false;
            result.activityActionEnu = activityActionEnu;

            if (result.activityActionEnu == cWarehouseorder.ActivityActionEnu.Hold) {
                result.pAddErrorMessage(cAppExtension.context.getString((R.string.hold_the_order)));
            }

            cPickorder.currentPickOrder.pGetCommentsViaWebErrorBln(webresult.getResultDtt());
            return result;
        }

        return  result;


    }

    public static List<cPickorder> pGetPicksFromDatabasObl() {

        List<cPickorder> resultObl = new ArrayList<>();
        List<cPickorderEntity> hulpResultObl;

        hulpResultObl =  cPickorder.getPickorderViewModel().pGetPickordersFromDatabaseObl();
        if (hulpResultObl == null || hulpResultObl.size() == 0) {
            return  resultObl;
        }

        for (cPickorderEntity pickorderEntity : hulpResultObl ) {
            cPickorder pickorder = new cPickorder(pickorderEntity);
            resultObl.add(pickorder);
        }

        return  resultObl;
    }

    public static List<cPickorder> pGetPicksWithFilterFromDatabasObl() {

        List<cPickorder> resultObl = new ArrayList<>();
        List<cPickorderEntity> hulpResultObl;

        hulpResultObl =  cPickorder.getPickorderViewModel().pGetPickordersWithFilterFromDatabaseObl(cUser.currentUser.getNameStr(),cSharedPreferences.userFilterBln(),cSharedPreferences.showProcessedWaitBln(),cSharedPreferences.showSingleArticlesBln(),cSharedPreferences.showAssignedToMeBln(),cSharedPreferences.showAssignedToOthersBln(),cSharedPreferences.showNotAssignedBln());
        if (hulpResultObl == null || hulpResultObl.size() == 0) {
            return  resultObl;
        }

        for (cPickorderEntity pickorderEntity : hulpResultObl ) {
            cPickorder pickorder = new cPickorder(pickorderEntity);
            resultObl.add(pickorder);
        }

        return  resultObl;
    }

    public boolean pGetAdressesViaWebserviceBln(Boolean pvRefreshBln) {

        if (cSetting.PICK_SHIPPING_SALES() == false ) {
            return  true;
        }

        if (pvRefreshBln == true) {
            cPickorderAddress.allAdressesObl = null;
            cPickorderAddress.pTruncateTableBln();
        }

        cWebresult WebResult;
        WebResult =  cPickorder.getPickorderViewModel().pGetAdressesFromWebserviceWrs();
        if (WebResult.getResultBln() == true && WebResult.getSuccessBln() == true ){

            List<JSONObject> myList = WebResult.getResultDtt();
            for (int i = 0; i < myList.size(); i++) {
                JSONObject jsonObject;
                jsonObject = myList.get(i);

                cPickorderAddress pickorderAddress = new cPickorderAddress(jsonObject);
                pickorderAddress.pInsertInDatabaseBln();

                if ( cPickorderAddress.allAdressesObl  == null) {
                    cPickorderAddress.allAdressesObl  = new ArrayList<>();
                }

                cPickorderAddress.allAdressesObl .add((pickorderAddress));

            }
            return  true;
        }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETPICKORDERADDRESSES);
            return  false;
        }
    }

    public boolean pGetArticleImagesViaWebserviceBln(Boolean pvRefreshBln) {

        if (cPickorder.currentPickOrder.isPickWithPictureBln() == false || cPickorder.currentPickOrder.pickPickWithPicturePrefetchBln == false) {
            return  true;
        }

        if (pvRefreshBln == true) {
            cArticleImage.allImages = null;
            cArticleImage.pTruncateTableBln();
        }

        if (this.imagesObl()  != null) {
            return  true;
        }

        if (this.linesObl() == null || this.linesObl().size() == 0) {
            return  false;
        }

        List<String> itemNoAndVariantCodeObl;
        itemNoAndVariantCodeObl = new ArrayList<>();

        for (cPickorderLine pickorderLine : this.linesObl()) {
            String itemNoAndVariantCodeStr = pickorderLine.getItemNoStr() + ";" + pickorderLine.getVariantCodeStr();

            if (itemNoAndVariantCodeObl.contains(itemNoAndVariantCodeStr) == false) {
                itemNoAndVariantCodeObl.add(itemNoAndVariantCodeStr);
            }
        }

        cWebresult WebResult;
        WebResult =  cPickorder.getPickorderViewModel().pGetArticleImagesFromWebserviceWrs(itemNoAndVariantCodeObl);
        if (WebResult.getResultBln() == true && WebResult.getSuccessBln() == true ){

            cArticleImage.allImages = new ArrayList<>();
            List<JSONObject> myList = WebResult.getResultDtt();

            for (int i = 0; i < myList.size(); i++) {
                JSONObject jsonObject;
                jsonObject = myList.get(i);

                cArticleImage articleImage = new cArticleImage(jsonObject);

                if ( cArticleImage.allImages.contains(articleImage) == false) {
                    articleImage.pInsertInDatabaseBln();
                    cArticleImage.allImages.add((articleImage));
                }
            }
            return  true;
        }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETARTICLEIMAGESMULTIPLE);
            return  false;
        }
    }

    public boolean pGetBarcodesViaWebserviceBln(Boolean pvRefreshBln) {

        if (pvRefreshBln == true) {
            cPickorderBarcode.allBarcodesObl = null;
            cPickorderBarcode.pTruncateTableBln();
        }

        cWebresult WebResult;
        WebResult =  cPickorder.getPickorderViewModel().pGetBarcodesFromWebserviceWrs();
        if (WebResult.getResultBln() == true && WebResult.getSuccessBln() == true ){

            List<JSONObject> myList = WebResult.getResultDtt();
            for (int i = 0; i < myList.size(); i++) {
                JSONObject jsonObject;
                jsonObject = myList.get(i);

                cPickorderBarcode pickorderBarcode = new cPickorderBarcode(jsonObject);
                pickorderBarcode.pInsertInDatabaseBln();
            }

            if (cPickorderBarcode.allBarcodesObl.size() == 0) {
                return  false;
            }

            return  true;

        }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETPICKORDERBARCODES);
            return  false;
        }
    }

    public boolean pGetCommentsViaWebserviceBln(Boolean pvRefeshBln) {
        if (pvRefeshBln == true) {
            cComment.allCommentsObl = null;
            cComment.pTruncateTable();
            cComment.commentsShownBln = false;
        }

        cWebresult webresult;
        webresult = cPickorder.getPickorderViewModel().pGetCommentsFromWebserviceWrs();
        if (webresult.getResultBln() == true && webresult.getSuccessBln() == true ) {

            cComment.allCommentsObl = new ArrayList<>();

            List<JSONObject> myList = webresult.getResultDtt();
            for (int i = 0; i < myList.size(); i++) {
                JSONObject jsonObject;
                jsonObject = myList.get(i);
                cComment comment = new cComment(jsonObject);
                comment.pInsertInDatabaseBln();
            }
            return true;
        }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETPICKORDERCOMMENTS);
            return false;
        }
    }

    public boolean pGetCommentsViaWebErrorBln(List<JSONObject> pvResultDtt) {

            cComment.allCommentsObl = new ArrayList<>();

            List<JSONObject> myList = pvResultDtt;
            for (int i = 0; i < myList.size(); i++) {
                JSONObject jsonObject;
                jsonObject = myList.get(i);
                cComment comment = new cComment(jsonObject);
                comment.pInsertInDatabaseBln();
            }
            return true;

    }

    public boolean pGetSortingDetailsBln(){

        if (!cPickorder.currentPickOrder.isPVBln()){
            return  true;
        }

        for (cPickorderLine pickorderLine : cPickorder.currentPickOrder.linesObl()) {


            if (pickorderLine.processingSequenceStr.isEmpty()) {
                continue;
            }

            if (pickorderLine.processingSequenceStr.equalsIgnoreCase(pickorderLine.sourceNoStr)) {
                continue;
            }

            cSalesOrderPackingTable salesOrderPackingTable = new cSalesOrderPackingTable(pickorderLine.sourceNoStr,pickorderLine.processingSequenceStr);
            salesOrderPackingTable.pInsertInDatabaseBln();

        }


        return  true;

    }

    public cPickorderLine pGetLineNotHandledByBarcode(String pvScannedBarcodeStr) {

        if (this.barcodesObl() == null || this.barcodesObl().size() == 0)  {
            return  null;
        }

        List<cPickorderLine> hulpObl = cPickorder.currentPickOrder.pGetLinesNotHandledFromDatabasObl();
        if (hulpObl == null || hulpObl.size() == 0) {
            return  null;
        }

        cPickorderBarcode pickorderBarcodeWithBarcode = null;

        for (cPickorderBarcode pickorderBarcode : this.barcodesObl()) {
            if (pickorderBarcode.barcodeStr.toLowerCase().equalsIgnoreCase(pvScannedBarcodeStr.toLowerCase())){
                pickorderBarcodeWithBarcode = pickorderBarcode;
                break;
            }
        }

        if (pickorderBarcodeWithBarcode  == null) {
            return  null;
        }

        for (cPickorderLine pickorderLine : hulpObl) {
            if (pickorderLine.localStatusInt == cWarehouseorder.PicklineLocalStatusEnu.LOCALSTATUS_NEW && pickorderLine.itemNoStr.equalsIgnoreCase(pickorderBarcodeWithBarcode.itemNoStr) && pickorderLine.variantCodeStr.equalsIgnoreCase((pickorderBarcodeWithBarcode.variantcodeStr))) {
                return  pickorderLine;
            }
        }
        return null;
    }

    //End Region Public Methods

    //Region Private Methods

    private static boolean mTruncateTableBln(){
        cPickorder.getPickorderViewModel().deleteAll();
        return true;
    }


    //End Region Private Methods

}
