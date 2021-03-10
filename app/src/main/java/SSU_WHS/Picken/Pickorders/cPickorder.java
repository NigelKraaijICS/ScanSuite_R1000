package SSU_WHS.Picken.Pickorders;

import androidx.lifecycle.ViewModelProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cDeviceInfo;
import ICS.Utils.cResult;
import ICS.Utils.cSharedPreferences;
import ICS.Utils.cText;
import ICS.Weberror.cWeberror;
import ICS.cAppExtension;
import SSU_WHS.Basics.Article.cArticle;
import SSU_WHS.Basics.ArticleBarcode.cArticleBarcode;
import SSU_WHS.Basics.ArticleImages.cArticleImage;
import SSU_WHS.Basics.ArticleImages.cArticleImageViewModel;
import SSU_WHS.Basics.ArticleStock.cArticleStock;
import SSU_WHS.Basics.BranchBin.cBranchBin;
import SSU_WHS.Basics.Branches.cBranch;
import SSU_WHS.Basics.PropertyGroup.cPropertyGroup;
import SSU_WHS.Basics.PropertyGroup.cPropertyGroupViewModel;
import SSU_WHS.Basics.Settings.cSetting;
import SSU_WHS.Basics.ShippingAgentServiceShippingUnits.cShippingAgentServiceShippingUnit;
import SSU_WHS.Basics.ShippingAgentServices.cShippingAgentService;
import SSU_WHS.Basics.ShippingAgents.cShippingAgent;
import SSU_WHS.Basics.Users.cUser;
import SSU_WHS.Basics.Workplaces.cWorkplace;
import SSU_WHS.General.Comments.cComment;
import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.General.Warehouseorder.cWarehouseorderViewModel;
import SSU_WHS.General.cDatabase;
import SSU_WHS.Picken.FinishSinglePieceLine.cPickorderLineFinishSinglePiece;
import SSU_WHS.Picken.PickorderAddresses.cPickorderAddress;
import SSU_WHS.Picken.PickorderBarcodes.cPickorderBarcode;
import SSU_WHS.Picken.PickorderCompositeBarcode.cPickorderCompositeBarcode;
import SSU_WHS.Picken.PickorderLineBarcodes.cPickorderLineBarcode;
import SSU_WHS.Picken.PickorderLinePackAndShip.cPickorderLinePackAndShip;
import SSU_WHS.Picken.PickorderLineProperty.cPickorderLineProperty;
import SSU_WHS.Picken.PickorderLinePropertyValue.cPickorderLinePropertyValue;
import SSU_WHS.Picken.PickorderLines.cPickorderLine;
import SSU_WHS.Picken.PickorderLines.cPickorderLineEntity;
import SSU_WHS.Picken.PickorderLines.cPickorderLineViewModel;
import SSU_WHS.Picken.PickorderSetting.cPickorderSetting;
import SSU_WHS.Picken.PickorderShipPackages.cPickorderShipPackage;
import SSU_WHS.Picken.SalesOrderPackingTable.cSalesOrderPackingTable;
import SSU_WHS.Picken.Shipment.cShipment;
import SSU_WHS.Picken.Storement.cStorement;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebserviceDefinitions;
import nl.icsvertex.scansuite.Activities.Pick.PickorderSelectActivity;
import nl.icsvertex.scansuite.R;

public class cPickorder{

    //Region Public Properties

    private final String orderNumberStr;
    public String getOrderNumberStr() {
        return orderNumberStr;
    }

    private final String orderTypeStr;
    public String getOrderTypeStr() {
        return orderTypeStr;
    }

    private final int sourceDocumentInt;
    private int getSourceDocumentInt() {
        return sourceDocumentInt;
    }

    private final String documentStr;
    public String getDocumentStr() {
        return documentStr;
    }

    private final int quantityTotalInt;
    public int getQuantityTotalInt() {
        return quantityTotalInt;
    }

    private final boolean singleArticleOrdersBln;
    public boolean isSingleArticleOrdersBln() {
        return singleArticleOrdersBln;
    }

    private final boolean pickSalesAskWorkplaceBln;
    public boolean isPickSalesAskWorkplaceBln() {
        return pickSalesAskWorkplaceBln;
    }

    private final boolean pickTransferAskWorkplaceBln;
    public boolean isPickTransferAskWorkplaceBln() {
        return pickTransferAskWorkplaceBln;
    }

    private final boolean pickPickPVVKOEachPieceBln;
    public boolean isPickPickPVVKOEachPieceBln() {
        return pickPickPVVKOEachPieceBln;
    }

    private final boolean pickWithPictureBln;
    public boolean isPickWithPictureBln() {
        return pickWithPictureBln;
    }

    private final boolean pickPickWithAutoOpenBln;
    public boolean isPickWithPictureAutoOpenBln() {
        return pickPickWithAutoOpenBln;
    }

    private final boolean pickPickWithPicturePrefetchBln;
    private boolean isPickPickWithPicturePrefetchBln() {
        return pickPickWithPicturePrefetchBln;
    }

    private final boolean pickActivityBinRequiredBln;
    public boolean isPickActivityBinRequiredBln() {
        return pickActivityBinRequiredBln;
    }

    private final boolean pickAutoNextBln;
    public boolean isPickAutoNextBln() {
        return pickAutoNextBln;
    }

    private final String assignedUserIdStr;
    public String getAssignedUserIdStr() {
        return assignedUserIdStr;
    }

    private String stockownerStr;
    public String getStockownerStr() {
        return stockownerStr;
    }

    private final String currentUserIdStr;
    public String getCurrentUserIdStr() {
        return currentUserIdStr;
    }

    private int statusInt;
    public int getStatusInt() {
        return statusInt;
    }

    private final String externalReferenceStr;
    public String getExternalReferenceStr() {
        return externalReferenceStr;
    }

    private String currentLocationStr;
    public String getCurrentLocationStr() {
        return currentLocationStr;
    }

    private final Boolean isProcessingOrParkedBln;
    public Boolean getProcessingOrParkedBln() {
        return isProcessingOrParkedBln;
    }

    private Boolean isSelectedBln;
    public Boolean getIsSelectedBln() {
        return isSelectedBln;
    }

    public  String getDestinationAndDescriptionStr(){

        String resultStr ;

        cBranch branch = cBranch.pGetBranchByCode(this.getExternalReferenceStr());


        if (branch == null) {
            resultStr = this.getExternalReferenceStr();
        }
        else {
            resultStr = branch.getBranchNameStr() + "  (" + this.getExternalReferenceStr() + ")";
        }

        return  resultStr;

    }

    public Boolean isPVBln() {
        return this.getOrderTypeStr().equalsIgnoreCase(cWarehouseorder.WorkflowEnu.PV.toString());
    }

    public Boolean isBCBln() {
        return this.getOrderTypeStr().equalsIgnoreCase(cWarehouseorder.WorkflowEnu.BC.toString());
    }

    public Boolean isBPBln() {
        return this.getOrderTypeStr().equalsIgnoreCase(cWarehouseorder.WorkflowEnu.BP.toString());
    }

    public Boolean isPABln() {
        return this.getOrderTypeStr().equalsIgnoreCase(cWarehouseorder.WorkflowEnu.PA.toString());
    }

    public Boolean isPFBln() {
        return this.getOrderTypeStr().equalsIgnoreCase(cWarehouseorder.WorkflowEnu.PF.toString());
    }

    public boolean isTransferBln(){

        return this.isPABln() || this.isPFBln();

    }

    private boolean isSingleDestinationBln(){

        if (!this.isTransferBln()) {
            return  false;
        }

        String destinationStr = "";

        for (cPickorderLine pickorderLine : this.linesObl()) {

            if (destinationStr.isEmpty()) {
                destinationStr =  pickorderLine.getDestinationNoStr();
            }

            if (!destinationStr.equalsIgnoreCase(pickorderLine.getDestinationNoStr())) {
                return  false;
            }
        }

        return  true;

    }

    public cBranch destionationBranch() {

        if (!this.isSingleDestinationBln()) {
            return  null;
        }

        return  cBranch.pGetBranchByCode(this.linesObl().get(0).getDestinationNoStr());

    }

    public boolean isSortableBln(){
        return this.isBCBln() || this.isBPBln();
    }

    public boolean isStorableBln(){
        return this.getStatusInt() == cWarehouseorder.WorkflowPickStepEnu.PickStorage;
    }

    private Boolean isPackAndShipNeededLocalBln = null;
    public boolean isPackAndShipNeededBln() {





        if (isPackAndShipNeededLocalBln != null) {
            return isPackAndShipNeededLocalBln;
        }

        if (this.pQuantityHandledDbl() == 0) {
            isPackAndShipNeededLocalBln = false;
            return  isPackAndShipNeededLocalBln;
        }

        if (this.linesObl() == null || this.linesObl().size() == 0 ) {
            isPackAndShipNeededLocalBln = false;
            return  isPackAndShipNeededLocalBln;
        }

        for (cPickorderLine pickorderLine : this.linesObl()) {
            if (pickorderLine.getStatusShippingInt() != cWarehouseorder.PackingAndShippingStatusEnu.NotNeeded ||
                    pickorderLine.getStatuPackingInt() != cWarehouseorder.PackingAndShippingStatusEnu.NotNeeded) {
                isPackAndShipNeededLocalBln = true;
                return  isPackAndShipNeededLocalBln;
            }
        }

        isPackAndShipNeededLocalBln = false;
        return  isPackAndShipNeededLocalBln;
    }

    public  boolean isCombinedOrderBln() {

        return cWarehouseorder.SourceDocumentTypeEnu.CombinedPick == this.getSourceDocumentInt();

    }

    public  boolean isGeneratedOrderBln() {

        return cWarehouseorder.SourceDocumentTypeEnu.Generated == this.getSourceDocumentInt();

    }

    private Boolean isSinglePickLocalBln = null;
    public boolean isSingleBinBln(){

        if (isSinglePickLocalBln != null) {
            return  isSinglePickLocalBln;
        }

        String binCodeStr = "";

        if (this.linesObl() == null || this.linesObl().size() ==0 ) {
            isSinglePickLocalBln = false;
            return isSinglePickLocalBln;
        }

        for (cPickorderLine pickorderLine : this.linesObl()) {

            if (binCodeStr.isEmpty()) {
                binCodeStr = pickorderLine.getBinCodeStr();
                continue;
            }

            if (!pickorderLine.getBinCodeStr().equalsIgnoreCase(binCodeStr)) {
                isSinglePickLocalBln = false;
                return isSinglePickLocalBln;
            }
        }

        isSinglePickLocalBln = true;
        return isSinglePickLocalBln;

    }

    public  boolean PICK_SHIPPING_QC_CHECK_COUNT () {

        if (this.settingsObl() == null || this.settingsObl().size() == 0) {
            return  false;
        }

        for (cPickorderSetting pickorderSetting : this.settingsObl()) {
            if (pickorderSetting.getSettingCodeStr().equalsIgnoreCase(cSetting.settingEnu.PICK_SHIPPING_QC_CHECK_COUNT.toString())) {
                return  cText.pStringToBooleanBln(pickorderSetting.getSettingValueStr(), false);
            }
        }

        return  false;

    }

    public cBranch scannedBranch;
    public cBranchBin currentBranchBin;

    public int lastSelectedIndexInt;
    public int getLastSelectedIndexInt() {
        return lastSelectedIndexInt;
    }

    private final cPickorderEntity pickorderEntity;

    private cWarehouseorderViewModel getWarehouseorderViewModel() {
        return new ViewModelProvider(cAppExtension.fragmentActivity).get(cWarehouseorderViewModel.class);
    }

    private cPickorderViewModel getPickorderViewModel() {
        return new ViewModelProvider(cAppExtension.fragmentActivity).get(cPickorderViewModel.class);
    }

    private cPickorderLineViewModel getPickorderLineViewModel() {
        return new ViewModelProvider(cAppExtension.fragmentActivity).get(cPickorderLineViewModel.class);
    }

    public static List<cPickorder> allPickordersObl;

    public  static  List<cPickorder> pickordersToSelectObl(){

        List<cPickorder> resultObl = new ArrayList<>();

        if (cPickorder.allPickordersObl == null || cPickorder.allPickordersObl.size() == 0) {
            return  resultObl;
        }

        for (cPickorder pickorder : cPickorder.allPickordersObl) {

            if (pickorder.getStatusInt() != cWarehouseorder.WorkflowPickStepEnu.PickPicking) {
                continue;
            }

            if (pickorder.getSourceDocumentInt() == cWarehouseorder.SourceDocumentTypeEnu.CombinedPick ) {
                continue;
            }

            if (cPickorder.currentCombinedPickOrder != null) {
                if (!pickorder.getOrderTypeStr().equalsIgnoreCase(cPickorder.currentCombinedPickOrder.getOrderTypeStr())) {
                    continue;
                }

            }

            resultObl.add(pickorder);

        }
        return  resultObl;
    }

    public  static  List<cPickorder> pickorderSelectedObl(){

        List<cPickorder> resultObl = new ArrayList<>();

        if (cPickorder.allPickordersObl == null || cPickorder.allPickordersObl.size() == 0) {
            return  resultObl;
        }

        for (cPickorder pickorder : cPickorder.allPickordersObl) {

            if (pickorder.isSelectedBln) {
                resultObl.add(pickorder);
            }

        }
        return  resultObl;
    }

    public static cPickorder currentPickOrder;
    public static cPickorder currentCombinedPickOrder;
    public cPickorderBarcode pickorderQCBarcodeScanned;
    public cPickorderBarcode pickorderBarcodeScanned;
    public static int totalPicksInt;

    public List<cPickorderLine> linesObl(){
        return  cPickorderLine.allLinesObl;
    }
    public List<cShipment> shipmentObl(){ return  cShipment.allShipmentsObl; }
    public List<cStorement> storementObl(){ return  cStorement.allStorementsObl; }
    public List<cPickorderAddress> adressesObl(){
        return  cPickorderAddress.allAdressesObl ;
    }

    private List<cPickorderSetting> settingsObl(){
        return  cPickorderSetting.allSettingObl  ;
    }
    private List<cArticleImage> imagesObl()  {
        return  cArticleImage.allImages;
    }
    public List<cPickorderBarcode> barcodesObl() {
        return  cPickorderBarcode.allBarcodesObl;
    }
    public List<cPickorderCompositeBarcode> compositeBarcodesObl() {
        return  cPickorderCompositeBarcode.allCompositeBarcodesObl;
    }
    public List<cPickorderLineProperty> linePropertysObl() { return  cPickorderLineProperty.allLinePropertysObl; }
    public List<cPickorderLinePropertyValue> linePropertyValueObl() { return  cPickorderLinePropertyValue.allLinePropertysValuesObl; }
    public List<cComment> commentsObl(){
        return  cComment.allCommentsObl;
    }
    public List<cSalesOrderPackingTable> salesOrderPackingTableObl() {
        return  cSalesOrderPackingTable.allSalesOrderPackingTabelsObl;
    }

    public cArticle currentArticle;
    public SortedMap<String, cArticle> articleObl;
    public  LinkedHashMap<Integer, List<JSONObject>>  itemProperyDataObl;

    //End region Public Properties

    //Region Constructor
    public cPickorder(JSONObject pvJsonObject){

        this.pickorderEntity = new cPickorderEntity(pvJsonObject);
        this.orderNumberStr = this.pickorderEntity.getOrdernumberStr();
        this.orderTypeStr = this.pickorderEntity.getOrderTypeStr();
        this.stockownerStr = this.pickorderEntity.getStockOwnerStr();
        this.quantityTotalInt =cText.pStringToIntegerInt(this.pickorderEntity.getQuantityTotalStr());
        this.singleArticleOrdersBln = cText.pStringToBooleanBln(this.pickorderEntity.getSingleArticleOrdersStr(),false) ;
        this.pickSalesAskWorkplaceBln = cText.pStringToBooleanBln(this.pickorderEntity.getPickSalesAskWorkplaceStr(),false) ;
        this.pickTransferAskWorkplaceBln = cText.pStringToBooleanBln(this.pickorderEntity.getPickTransferAskWorkplaceStr(),false) ;
        this.pickPickPVVKOEachPieceBln = cText.pStringToBooleanBln(this.pickorderEntity.getPickPickPVVKOEachPieceStr(),false) ;
        this.pickWithPictureBln = cText.pStringToBooleanBln(this.pickorderEntity.getpickWithPictureStr(),false) ;
        this.pickPickWithAutoOpenBln = cText.pStringToBooleanBln(this.pickorderEntity.getPickWithPictureAutoOpenStr(),false) ;
        this.pickPickWithPicturePrefetchBln = cText.pStringToBooleanBln(this.pickorderEntity.getPickWithPicturePrefetchStr(),false) ;
        this.pickActivityBinRequiredBln = cText.pStringToBooleanBln(this.pickorderEntity.getPickActivityBinRequired(),false);
        this.pickAutoNextBln = cText.pStringToBooleanBln(this.pickorderEntity.getPickAutoNext(),false);

        this.assignedUserIdStr = this.pickorderEntity.getAssignedUserIdStr();
        this.currentUserIdStr = this.pickorderEntity.getCurrentUserIdStr();
        this.statusInt = cText.pStringToIntegerInt(this.pickorderEntity.getStatusStr());
        this.sourceDocumentInt = cText.pStringToIntegerInt(this.pickorderEntity.getSourceDocumentStr());
        this.documentStr = this.pickorderEntity.getDocumentStr();
        this.externalReferenceStr = this.pickorderEntity.getExternalReferenceStr();
        this.currentLocationStr = this.pickorderEntity.getCurrentLocationStr();
        this.isSelectedBln = this.pickorderEntity.getIsSelected();
        this.isProcessingOrParkedBln = this.pickorderEntity.getIsProcessingOrParkedStr();
    }

    private cPickorder(cPickorderEntity pvPickorderEntity){

        this.pickorderEntity = pvPickorderEntity;
        this.orderNumberStr = this.pickorderEntity.getOrdernumberStr();
        this.orderTypeStr = this.pickorderEntity.getOrderTypeStr();
        this.stockownerStr = this.pickorderEntity.getStockOwnerStr();

        this.quantityTotalInt =cText.pStringToIntegerInt(this.pickorderEntity.getQuantityTotalStr());
        this.singleArticleOrdersBln = cText.pStringToBooleanBln(this.pickorderEntity.getSingleArticleOrdersStr(),false) ;
        this.pickSalesAskWorkplaceBln = cText.pStringToBooleanBln(this.pickorderEntity.getPickSalesAskWorkplaceStr(),false) ;
        this.pickTransferAskWorkplaceBln = cText.pStringToBooleanBln(this.pickorderEntity.getPickTransferAskWorkplaceStr(),false) ;

        this.pickPickPVVKOEachPieceBln = cText.pStringToBooleanBln(this.pickorderEntity.getPickPickPVVKOEachPieceStr(),false) ;
        this.pickWithPictureBln = cText.pStringToBooleanBln(this.pickorderEntity.getpickWithPictureStr(),false) ;
        this.pickPickWithAutoOpenBln = cText.pStringToBooleanBln(this.pickorderEntity.getPickWithPictureAutoOpenStr(),false) ;
        this.pickPickWithPicturePrefetchBln = cText.pStringToBooleanBln(this.pickorderEntity.getPickWithPicturePrefetchStr(),false) ;
        this.pickActivityBinRequiredBln = cText.pStringToBooleanBln(this.pickorderEntity.getPickActivityBinRequired(),false);
        this.pickAutoNextBln = cText.pStringToBooleanBln(this.pickorderEntity.getPickAutoNext(),false);
        this.assignedUserIdStr = this.pickorderEntity.getAssignedUserIdStr();
        this.currentUserIdStr = this.pickorderEntity.getCurrentUserIdStr();
        this.statusInt = cText.pStringToIntegerInt(this.pickorderEntity.getStatusStr());
        this.sourceDocumentInt = cText.pStringToIntegerInt(this.pickorderEntity.getSourceDocumentStr());
        this.documentStr = this.pickorderEntity.getDocumentStr();

        this.externalReferenceStr = this.pickorderEntity.getExternalReferenceStr();
        this.currentLocationStr = this.pickorderEntity.getCurrentLocationStr();
        this.isSelectedBln = this.pickorderEntity.getIsSelected();
        this.isProcessingOrParkedBln = this.pickorderEntity.getIsProcessingOrParkedStr();
    }
    //End Region Constructor

    //Region Public Methods

    public static  cPickorder pGetPickorder(String pvOrdernumberStr) {

        for (cPickorder pickorder : cPickorder.pickordersToSelectObl()) {

            if (pickorder.getOrderNumberStr().equalsIgnoreCase(pvOrdernumberStr)) {
                return  pickorder;
            }
        }

        return  null;

    }

    public static  cPickorder pGetPackAndShipOrderByCurrentLocationStr(String pvCurrentLocationStr) {

        if (cPickorder.allPickordersObl == null) {
            return null;
        }

        for (cPickorder pickorder : cPickorder.allPickordersObl) {

            if (pickorder.getCurrentLocationStr().equalsIgnoreCase(pvCurrentLocationStr)) {
                return  pickorder;
            }
        }

        return  null;

    }

    public static void pUnselectAllOrders() {

        if (cPickorder.allPickordersObl != null) {
            for (cPickorder pickorder : cPickorder.allPickordersObl) {
                cPickorder.currentPickOrder = pickorder;
                cPickorder.currentPickOrder.pUpdateSelectedRst(false);
            }
        }
    }

    public static boolean pGetPickOrdersViaWebserviceBln(Boolean pvRefreshBln, Boolean pvProcessingOrParkedBln, String pvSearchTextStr) {

        if (pvRefreshBln) {
            cPickorder.allPickordersObl = null;
            cPickorder.mTruncateTable();
        }

        cWebresult WebResult;

        cPickorderViewModel pickorderViewModel =  new ViewModelProvider(cAppExtension.fragmentActivity).get(cPickorderViewModel.class);
        WebResult =  pickorderViewModel.pGetPickordersFromWebserviceWrs(pvProcessingOrParkedBln,pvSearchTextStr);
        if (WebResult.getResultBln() && WebResult.getSuccessBln()){

            for (JSONObject jsonObject : WebResult.getResultDtt()) {
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

        if (pvRefreshBln) {
            cPickorder.allPickordersObl = null;
            cPickorder.mTruncateTable();
        }

        cWebresult WebResult;
        cPickorderViewModel pickorderViewModel =  new ViewModelProvider(cAppExtension.fragmentActivity).get(cPickorderViewModel.class);
        WebResult =  pickorderViewModel.pGetPickordersNextStepFromWebserviceWrs(cUser.currentUser.getUsernameStr(),cWarehouseorder.StepCodeEnu.Pick_Sorting,pvSearchTextStr);
        if (WebResult.getResultBln() && WebResult.getSuccessBln()){

            for (JSONObject jsonObject : WebResult.getResultDtt()) {
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

    public static boolean pGetStoreOrdersViaWebserviceBln(Boolean pvRefreshBln,String pvSearchTextStr) {

        if (pvRefreshBln) {
            cPickorder.allPickordersObl = null;
            cPickorder.mTruncateTable();
        }

        cWebresult WebResult;
        cPickorderViewModel pickorderViewModel =  new ViewModelProvider(cAppExtension.fragmentActivity).get(cPickorderViewModel.class);
        WebResult =  pickorderViewModel.pGetPickordersNextStepFromWebserviceWrs(cUser.currentUser.getUsernameStr(),cWarehouseorder.StepCodeEnu.Pick_Storage,pvSearchTextStr);
        if (WebResult.getResultBln() && WebResult.getSuccessBln()){

            for (JSONObject jsonObject : WebResult.getResultDtt()) {
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

        if (pvRefreshBln) {
            cPickorder.allPickordersObl = null;
            cPickorder.mTruncateTable();
        }

        cWebresult WebResult;
        cPickorderViewModel pickorderViewModel =  new ViewModelProvider(cAppExtension.fragmentActivity).get(cPickorderViewModel.class);
        WebResult =  pickorderViewModel.pGetPickordersNextStepFromWebserviceWrs(cUser.currentUser.getUsernameStr(),cWarehouseorder.StepCodeEnu.Pick_PackAndShip,pvSearchTextStr);
        if (WebResult.getResultBln() && WebResult.getSuccessBln()){

            for (JSONObject jsonObject :WebResult.getResultDtt()) {
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

    public static boolean pGetFinishShipOrdersViaWebserviceBln(Boolean pvRefreshBln, String pvSearchTextStr) {

        if (pvRefreshBln) {
            cPickorder.allPickordersObl = null;
            cPickorder.mTruncateTable();
        }

        cWebresult WebResult;
        cPickorderViewModel pickorderViewModel =  new ViewModelProvider(cAppExtension.fragmentActivity).get(cPickorderViewModel.class);
        WebResult =  pickorderViewModel.pGetPickordersNextStepFromWebserviceWrs(cUser.currentUser.getUsernameStr(),cWarehouseorder.StepCodeEnu.Finish_Packing,pvSearchTextStr);
        if (WebResult.getResultBln() && WebResult.getSuccessBln()){

            for (JSONObject jsonObject :WebResult.getResultDtt()) {
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

    public static List<cPickorder> pGetPicksWithFilterFromDatabasObl() {

        List<cPickorder> resultObl = new ArrayList<>();
        List<cPickorderEntity> hulpResultObl;

        cPickorderViewModel pickorderViewModel =  new ViewModelProvider(cAppExtension.fragmentActivity).get(cPickorderViewModel.class);
        hulpResultObl =  pickorderViewModel.pGetPickordersWithFilterFromDatabaseObl(cUser.currentUser.getUsernameStr(),cSharedPreferences.userFilterBln());
        if (hulpResultObl == null || hulpResultObl.size() == 0) {
            if (cAppExtension.activity instanceof PickorderSelectActivity) {
                PickorderSelectActivity pickorderSelectActivity = (PickorderSelectActivity)cAppExtension.activity;
                pickorderSelectActivity.pSetToolBarTitleWithCounters(0);
            }

            return  resultObl;
        }

        for (cPickorderEntity pickorderEntity : hulpResultObl ) {
            cPickorder pickorder = new cPickorder(pickorderEntity);
            resultObl.add(pickorder);
        }

        if (cAppExtension.activity instanceof PickorderSelectActivity) {
            PickorderSelectActivity pickorderSelectActivity = (PickorderSelectActivity)cAppExtension.activity;
            pickorderSelectActivity.pSetToolBarTitleWithCounters(resultObl.size());

        }
        return  resultObl;
    }

    public boolean pInsertInDatabaseBln() {
        this.getPickorderViewModel().insert(this.pickorderEntity);

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
        boolean ignoreBusyBln = false;

        if (this.getStatusInt() > 10 && cUser.currentUser.getUsernameStr().equalsIgnoreCase(this.getCurrentUserIdStr())) {
            ignoreBusyBln = true;
        }

        Webresult = this.getWarehouseorderViewModel().pLockWarehouseopdrachtViaWebserviceWrs(cWarehouseorder.OrderTypeEnu.PICKEN.toString(),
                this.getOrderNumberStr(),
                cDeviceInfo.getSerialnumberStr(),
                pvStepCodeEnu.toString(),
                pvWorkFlowStepInt,
                ignoreBusyBln);

        //No result, so something really went wrong
        if (Webresult == null) {
            result.resultBln = false;
            result.activityActionEnu = cWarehouseorder.ActivityActionEnu.Unknown;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_couldnt_lock_order));
            return result;
        }

        //Everything was fine, so we are done
        if (Webresult.getSuccessBln() && Webresult.getResultBln()) {
            result.resultBln = true;
            return result;
        }

        //Something really went wrong
        if (!Webresult.getSuccessBln()) {
            result.resultBln = false;
            result.activityActionEnu = cWarehouseorder.ActivityActionEnu.Unknown;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_couldnt_lock_order));
            return result;
        }

        //Check if this activity is meant for a different user
        if (!Webresult.getResultBln()&& Webresult.getResultLng() <= 0 && Webresult.getResultObl() != null &&
                Webresult.getResultObl().size() >= 2 && Webresult.getResultObl().get(0).equalsIgnoreCase("invalid_user_not_assigned")) {
            result.resultBln = false;
            result.activityActionEnu = cWarehouseorder.ActivityActionEnu.Unknown;
            result.pAddErrorMessage(cAppExtension.context.getString((R.string.message_meant_for_other_user)) + " " + Webresult.getResultObl().get(1) );
            return  result;
        }

        //Check if this activity is meant for a different user
        if (!Webresult.getResultBln() && Webresult.getResultLng() <= 0 && Webresult.getResultObl() != null &&
                Webresult.getResultObl().size() > 0 && !Webresult.getResultObl().get(0).equalsIgnoreCase(cUser.currentUser.getUsernameStr())) {
            result.resultBln = false;
            result.activityActionEnu = cWarehouseorder.ActivityActionEnu.Unknown;
            result.pAddErrorMessage(cAppExtension.context.getString((R.string.message_another_user_already_started)) + " " + Webresult.getResultObl().get(0) );
            return  result;
        }

        // We got a succesfull response, but we need to do something with this activity
        if (!Webresult.getResultBln() && Webresult.getResultLng() > 0 ) {

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

            cPickorder.currentPickOrder.mGetCommentsViaWebError(Webresult.getResultDtt());
            return result;
        }

        return  result;

    }

    public boolean pLockReleaseViaWebserviceBln(cWarehouseorder.StepCodeEnu pvStepCodeEnu, int pvWorkFlowStepInt) {

        cWebresult Webresult;

        Webresult = this.getWarehouseorderViewModel().pLockReleaseWarehouseorderViaWebserviceWrs(cWarehouseorder.OrderTypeEnu.PICKEN.toString(),this.getOrderNumberStr(),cDeviceInfo.getSerialnumberStr(),pvStepCodeEnu.toString(), pvWorkFlowStepInt);

        return Webresult.getSuccessBln() && Webresult.getResultBln();
    }

    public cResult pGetPickDetailsRst(){

        cResult result;

        result = new cResult();
        result.resultBln = true;

        //Get all TAKE linesInt for current order, if size = 0 or webservice error then stop
        if (!this.pGetLinesViaWebserviceBln(true, cWarehouseorder.PickOrderTypeEnu.PICK)) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_lines_failed));
            return result;
        }

        //Get TAKE line barcodes for current order
        if (!this.mGetLineBarcodesViaWebserviceBln( cWarehouseorder.ActionTypeEnu.TAKE)) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_linebarcodes_failed));
            return result;
        }

        //Get TAKE line barcodes for current order
        if (!this.mGetLineBarcodesViaWebserviceBln(cWarehouseorder.ActionTypeEnu.TAKE)) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_linebarcodes_failed));
            return result;
        }

        // Get all propertys, if webservice error then stop
        if (!this.mGetLinePropertysViaWebserviceBln( )) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_line_propertys_failed));
            return result;
        }

        // Get all property values, if webservice error then stop
        if (!this.mGetLinePropertyValuesViaWebserviceBln( )) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_line_property_values_failed));
            return result;
        }

        // Get all article images, only if neccesary
        if (!this.mGetArticleImagesViaWebserviceBln()) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_article_images_failed));
            return result;
        }

        // Get all barcodes, if size =0 or webservice error then stop
        if (!this.pGetBarcodesViaWebserviceBln(true)) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_barcodes_failed));
            return result;
        }

        // Get all barcodes, if size =0 or webservice error then stop
        if (!this.pGetCompositeBarcodesViaWebserviceBln(true)) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_composite_barcodes_failed));
            return result;
        }

        // Get all adresses, if system settings Pick Shipping Sales == false then don't ask web service
        if (!this.pGetAdressesViaWebserviceBln(true)) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_adresses_failed));
            return result;
        }

        // Get all comments
        if (!this.pGetCommentsViaWebserviceBln(true)) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_comments_failed));
            return result;
        }

        if (!this.mGetSortingDetailsBln()) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_sorting_detals_failed));
            return result;
        }

        if (!this.pGetPropertyLineDataViaWebserviceBln()) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_property_line_data_failed));
            return result;
        }

        return  result;
    }

    public cResult pGetSortDetailsRst(){

        cResult result;

        result = new cResult();
        result.resultBln = true;

        //Get all PLACE linesInt for current order, if size = 0 or webservice error then stop
        if (!this.pGetLinesViaWebserviceBln(true, cWarehouseorder.PickOrderTypeEnu.SORT)) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_lines_failed));
            return result;
        }

        //Get all PLACE linesInt for current order, if size = 0 or webservice error then stop
        if (!this.mGetLineBarcodesViaWebserviceBln(cWarehouseorder.ActionTypeEnu.PLACE)) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_linebarcodes_failed));
            return result;
        }

        // Get all propertys, if webservice error then stop
        if (!this.mGetLinePropertysViaWebserviceBln()) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_line_propertys_failed));
            return result;
        }

        // Get all property values, if webservice error then stop
        if (!this.mGetLinePropertyValuesViaWebserviceBln( )) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_line_property_values_failed));
            return result;
        }

        // Get all article images, only if neccesary
        if (!this.mGetArticleImagesViaWebserviceBln()) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_article_images_failed));
            return result;
        }

        // Get all barcodes, if size =0 or webservice error then stop
        if (!this.pGetBarcodesViaWebserviceBln(true)) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_barcodes_failed));
            return result;
        }

        // Get all barcodes, if size =0 or webservice error then stop
        if (!this.pGetCompositeBarcodesViaWebserviceBln(true)) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_composite_barcodes_failed));
            return result;
        }

        // Get all adresses, if system settings Pick Shipping Sales == false then don't ask web service
        if (!this.pGetAdressesViaWebserviceBln(true)) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_adresses_failed));
            return result;
        }

        // Get all comments
        if (!this.pGetCommentsViaWebserviceBln(true)) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_comments_failed));
            return result;
        }

        if (!this.mGetSortingDetailsBln()) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_sorting_detals_failed));
            return result;
        }

        if (!this.pGetPropertyLineDataViaWebserviceBln()) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_property_line_data_failed));
            return result;
        }

        return  result;
    }

    public cResult pGetSelfpickDetailsRst(){

        cResult result;

        result = new cResult();
        result.resultBln = true;

        //Get all linesInt for current order, if webservice error then stop
        if (!this.pGetLinesViaWebserviceBln(true, cWarehouseorder.PickOrderTypeEnu.PICK)) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_lines_failed));
            return result;
        }

        // Get all barcodes, if webservice error then stop
        if (!this.pGetBarcodesViaWebserviceBln(true)) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_barcodes_failed));
            return result;
        }

        // Get all barcodes, if size =0 or webservice error then stop
        if (!this.pGetCompositeBarcodesViaWebserviceBln(true)) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_composite_barcodes_failed));
            return result;
        }

        // Get all propertys, if webservice error then stop
        if (!this.mGetLineBarcodesViaWebserviceBln(cWarehouseorder.ActionTypeEnu.TAKE )) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_line_barcodes_failed));
            return result;
        }

        // Get all propertys, if webservice error then stop
        if (!this.mGetLinePropertysViaWebserviceBln( )) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_line_propertys_failed));
            return result;
        }

        // Get all property values, if webservice error then stop
        if (!this.mGetLinePropertyValuesViaWebserviceBln( )) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_line_property_values_failed));
            return result;
        }

        // Get all comments
        if (!this.pGetCommentsViaWebserviceBln(true)) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_comments_failed));
            return result;
        }

        return  result;
    }

    public cResult pGetShipmentDetailsRst() {

        cResult result;

        result = new cResult();
        result.resultBln = true;

        //Check all ShippingAgents
        if (cShippingAgent.allShippingAgentsObl == null || cShippingAgent.allShippingAgentsObl.size() == 0) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_no_shippingagents_available));
            return result;
        }

        //Check all ShippingAgents
        if (cShippingAgentService.allShippingAgentServicesObl == null || cShippingAgentService.allShippingAgentServicesObl.size() == 0) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_no_shippingagent_services_available));
            return result;
        }

        //Check all ShippingAgent Shipping Units
        if (cShippingAgentServiceShippingUnit.allShippingAgentServiceShippingUnitsObl == null || cShippingAgentServiceShippingUnit.allShippingAgentServiceShippingUnitsObl.size() == 0) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_no_shippingagent_services_units_available));
            return result;
        }

        // Get all settings
        if (!this.mGetSettingsViaWebserviceBln()) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_comments_failed));
            return result;
        }

        // Get all linesInt, if zero than there is something wrong
        if (!this.pGetPackAndShipLinesViaWebserviceBln(true)) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_getting_pack_and_ship_lines_failed));
            return result;
        }

        // Get Barcodes
        if (!this.pGetBarcodesViaWebserviceBln(true)) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_barcodes_failed));
            return result;
        }

        // Get all barcodes, if size =0 or webservice error then stop
        if (!this.pGetCompositeBarcodesViaWebserviceBln(true)) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_composite_barcodes_failed));
            return result;
        }

        // Get all packages
        if (!this.pGetShippingPackagedViaWebserviceBln(true)) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_getting_packages_failed));
            return result;
        }

        // Get all adresses, if system settings Pick Shipping Sales == false then don't ask web service
        if (!this.pGetAdressesViaWebserviceBln(true)) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_adresses_failed));
            return result;
        }

        // Get all comments
        if (!this.pGetCommentsViaWebserviceBln(true)) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_comments_failed));
            return result;
        }

        if (this.PICK_SHIPPING_QC_CHECK_COUNT()) {

            for (cShipment shipment : cShipment.allShipmentsObl) {
                shipment.pCheckIfShipmentIsChecked();
            }

        }


        return result;
    }

    public cResult pGetFinishShippingDetailsRst() {

        cResult result;

        result = new cResult();
        result.resultBln = true;

        // Get all lines, if zero than there is something wrong
        if (!this.mGetFinishPackSinglePieceLinesViaWebserviceBln()) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_getting_pack_and_ship_lines_failed));
            return result;
        }

        // Get Barcodes
        if (!this.pGetBarcodesViaWebserviceBln(true)) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_barcodes_failed));
            return result;
        }

        // Get all barcodes, if size =0 or webservice error then stop
        if (!this.pGetCompositeBarcodesViaWebserviceBln(true)) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_composite_barcodes_failed));
            return result;
        }

        // Get all comments
        if (!this.pGetCommentsViaWebserviceBln(true)) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_comments_failed));
            return result;
        }

        return result;
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

        for (cPickorderLine pickorderLine : this.pGetLinesNotHandledFromDatabaseObl()) {
            cPickorderLine.currentPickOrderLine = pickorderLine;
            cPickorderLine.currentPickOrderLine.pLineBusyRst();
            cPickorderLine.currentPickOrderLine.pHandledIndatabase();
            cPickorderLine.currentPickOrderLine.pHandledBln();
        }

        return true;
    }

    public boolean pUpdateCurrentLocationBln(String pvCurrentLocationStr) {


        cWebresult Webresult;

        Webresult = this.getPickorderViewModel().pUpdateCurrentLocationViaWebserviceWrs(pvCurrentLocationStr);
        if (!Webresult.getSuccessBln() || !Webresult.getResultBln()) {
            return  false;
        }

        if (!this.getPickorderViewModel().pUpdatePickorderCurrentLocationInDatabaseBln(pvCurrentLocationStr)) {
            return  false;
        }

        this.currentLocationStr = pvCurrentLocationStr;
        return  true;

    }

    public double pQuantityNotHandledDbl() {
        Double resultDbl =  this.getPickorderViewModel().pQuantityNotHandledDbl();
        if (resultDbl == null) {
            resultDbl  = 0.0;
        }

        return  resultDbl;

    }

    public double pQuantityHandledDbl() {

        Double resultDbl = this.getPickorderViewModel().pQuantityHandledDbl();

        if (resultDbl == null) {
            resultDbl  = 0.0;
        }

        return  resultDbl;
    }

    public double pQuantityTotalDbl() {
        return  this.getPickorderViewModel().pGetQuantityTotalDbl();
    }

    public int pGetIndexOfNotHandledLineInt(cPickorderLine pvPickorderLine) {

        int resultInt = -1;

        if (this.pGetLinesNotHandledFromDatabaseObl() == null || this.pGetLinesNotHandledFromDatabaseObl().size() == 0) {
            return  resultInt;
        }


        int indexInt = -1;

        for (cPickorderLine pickorderLine : this.pGetLinesNotHandledFromDatabaseObl()) {

            indexInt += 1;

            if (cText.pIntToStringStr(pickorderLine.getLineNoInt()).equalsIgnoreCase(cText.pIntToStringStr(pvPickorderLine.getLineNoInt()))  ) {

                resultInt =  indexInt;
                break;
            }
        }

        return  resultInt;

    }

    public List<cComment> pPickCommentObl(){

        if (this.commentsObl() == null || this.commentsObl().size() == 0) {
            return  null;
        }


        return cComment.pGetCommentsForTypeObl(cWarehouseorder.CommentTypeEnu.PICK);

    }

    public List<cComment> pSortCommentObl(){

        if (this.commentsObl() == null || this.commentsObl().size() == 0) {
            return  null;
        }

        return cComment.pGetCommentsForTypeObl(cWarehouseorder.CommentTypeEnu.SORT);

    }

    public List<cComment> pStoreCommentObl(){

        if (this.commentsObl() == null || this.commentsObl().size() == 0) {
            return  null;
        }

        return cComment.pGetCommentsForTypeObl(cWarehouseorder.CommentTypeEnu.STORE);

    }

    public List<cComment> pShipCommentObl(){

        if (this.commentsObl() == null || this.commentsObl().size() == 0) {
            return  null;
        }

        return cComment.pGetCommentsForTypeObl(cWarehouseorder.CommentTypeEnu.SHIP);

    }

    public List<cComment> pFeedbackCommentObl(){

        if (cPickorder.currentPickOrder.commentsObl() == null || cPickorder.currentPickOrder.commentsObl().size() == 0) {
            return  null;
        }

        return cComment.pGetCommentsForTypeObl(cWarehouseorder.CommentTypeEnu.FEEDBACK);

    }

    public List<cComment> pFeedbackAndPickCommentObl(){

        List<cComment> resultObl = new ArrayList<>();

        if (this.pFeedbackCommentObl() != null) {
            resultObl.addAll(this.pFeedbackCommentObl());
        }

        if (this.pPickCommentObl() != null) {
            resultObl.addAll(this.pPickCommentObl());
        }


        return resultObl;

    }

    public boolean pGetLinesViaWebserviceBln(Boolean pvRefreshBln,cWarehouseorder.PickOrderTypeEnu pvPickOrderTypeEnu) {

        if (pvRefreshBln) {
            cPickorderLine.allLinesObl = null;
            cPickorderLine.pTruncateTableBln();
        }

        cWebresult WebResult;
        cWarehouseorder.ActionTypeEnu actionTypeEnu = null;

        if (pvPickOrderTypeEnu == cWarehouseorder.PickOrderTypeEnu.PICK) {
            actionTypeEnu = cWarehouseorder.ActionTypeEnu.TAKE;
        }

        if (pvPickOrderTypeEnu == cWarehouseorder.PickOrderTypeEnu.SORT) {
            actionTypeEnu = cWarehouseorder.ActionTypeEnu.PLACE;
        }

        WebResult =  this.getPickorderViewModel().pGetLinesFromWebserviceWrs(actionTypeEnu);
        if (WebResult.getResultBln() && WebResult.getSuccessBln()){


            for (JSONObject jsonObject : WebResult.getResultDtt()) {
                if (cPickorderLine.allLinesObl == null) {
                    cPickorderLine.allLinesObl = new ArrayList<>();
                }

                cPickorderLine pickorderLine = new cPickorderLine(jsonObject,pvPickOrderTypeEnu);

                if (pvPickOrderTypeEnu == cWarehouseorder.PickOrderTypeEnu.PICK) {
                    pickorderLine.pInsertInDatabaseBln();
                    continue;
                }

                //Check the statusInt, so linesInt that are not picked are ignored
                if (pvPickOrderTypeEnu == cWarehouseorder.PickOrderTypeEnu.SORT && pickorderLine.getQuantityTakenDbl() >0 ) {
                    pickorderLine.pInsertInDatabaseBln();
                }

            }

            if (!this.isGeneratedOrderBln()) {
                return cPickorderLine.allLinesObl.size() != 0;
            }
            else
            {
                if (cPickorderLine.allLinesObl == null) {
                    cPickorderLine.allLinesObl = new ArrayList<>();
                }
                return  true;
            }


        }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETPICKORDERS);
            return  false;
        }
    }

    public boolean pGetStorageLinesViaWebserviceBln(Boolean pvRefreshBln) {

        if (pvRefreshBln) {
            cPickorderLine.allLinesObl = null;
            cPickorderLine.pTruncateTableBln();
        }

        cWebresult WebResult;

        WebResult =  this.getPickorderViewModel().pGetStorageLinesFromWebserviceWrs();
        if (WebResult.getResultBln() && WebResult.getSuccessBln()){


            for (JSONObject jsonObject : WebResult.getResultDtt()) {
                if (cPickorderLine.allLinesObl == null) {
                    cPickorderLine.allLinesObl = new ArrayList<>();
                }

                cPickorderLine pickorderLine = new cPickorderLine(jsonObject, cWarehouseorder.PickOrderTypeEnu.STORE);

                if (pickorderLine.getProcessingSequenceStr().isEmpty() && this.isPVBln()) {
                    continue;
                }

                pickorderLine.pInsertInDatabaseBln();
                cStorement storement = cStorement.pGetStorement(pickorderLine.getSourceNoStr());
                if (storement == null ) {
                    cStorement storementToAdd = new cStorement(pickorderLine.getSourceNoStr());
                    storementToAdd.binCodeStr = pickorderLine.getStorageBinCodeStr();
                    storementToAdd.handledBln = !storementToAdd.getBinCodeStr().isEmpty();

                    cStorement.pAddStorement(storementToAdd);
                    storementToAdd.pAddPickorderLine(pickorderLine);
                    continue;
                }

                storement.pAddPickorderLine(pickorderLine);

            }

            if (!this.isGeneratedOrderBln()) {
                return cPickorderLine.allLinesObl.size() != 0;
            }
            else
            {
                if (cPickorderLine.allLinesObl == null) {
                    cPickorderLine.allLinesObl = new ArrayList<>();
                }
                return  true;
            }
        }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETPICKORDERLINES);
            return  false;
        }
    }

    public List<cPickorderLine> pGetLinesFromDatabaseObl() {

        List<cPickorderLine> resultObl = new ArrayList<>();
        List<cPickorderLineEntity> hulpResultObl;

        hulpResultObl =  this.getPickorderViewModel().pGetAllLinesFromDatabaseObl();
        if (hulpResultObl == null || hulpResultObl.size() == 0) {
            return  resultObl;
        }

        for (cPickorderLineEntity pickorderLineEntity : hulpResultObl ) {
            cPickorderLine PickorderLine = new cPickorderLine(pickorderLineEntity);
            resultObl.add(PickorderLine);
        }
        return  resultObl;
    }

    public List<cPickorderLine> pGetLinesToSendFromDatabaseObl() {

        List<cPickorderLine> resultObl = new ArrayList<>();
        List<cPickorderLineEntity> hulpResultObl;

        hulpResultObl =  this.getPickorderViewModel().pGetPickorderLinesToSendFromDatabaseObl();
        if (hulpResultObl == null || hulpResultObl.size() == 0) {
            return  resultObl;
        }

        for (cPickorderLineEntity pickorderLineEntity : hulpResultObl ) {
            cPickorderLine PickorderLine = new cPickorderLine(pickorderLineEntity);
            resultObl.add(PickorderLine);
        }
        return  resultObl;
    }

    public List<cPickorderLine> pGetLinesNotHandledFromDatabaseObl() {

        List<cPickorderLine> resultObl = new ArrayList<>();
        List<cPickorderLineEntity> hulpResultObl;

        if (this.scannedBranch == null) {
            hulpResultObl =  this.getPickorderViewModel().pGetLinesNotHandledFromDatabaseObl();
        }
        else {
            hulpResultObl =  this.getPickorderViewModel().pGetLinesNotHandledForBranchFromDatabaseObl(this.scannedBranch.getBranchStr());
        }

        if (hulpResultObl == null || hulpResultObl.size() == 0) {
            return  resultObl;
        }

        for (cPickorderLineEntity pickorderLineEntity : hulpResultObl ) {
            cPickorderLine PickorderLine = new cPickorderLine(pickorderLineEntity);
            resultObl.add(PickorderLine);
        }
        return  resultObl;
    }

    public List<cPickorderLine> pGetLinesBusyFromDatabaseObl() {

        List<cPickorderLine> resultObl = new ArrayList<>();
        List<cPickorderLineEntity> hulpResultObl;

        hulpResultObl =  this.getPickorderViewModel().pGetLinesBusyFromDatabaseObl();
        if (hulpResultObl == null || hulpResultObl.size() == 0) {
            return  resultObl;
        }

        for (cPickorderLineEntity pickorderLineEntity : hulpResultObl ) {
            cPickorderLine PickorderLine = new cPickorderLine(pickorderLineEntity);
            resultObl.add(PickorderLine);
        }
        return  resultObl;
    }

    public List<cPickorderLine> pGetLinesHandledFromDatabaseObl() {

        List<cPickorderLine> resultObl = new ArrayList<>();
        List<cPickorderLineEntity> hulpResultObl;

        hulpResultObl =  this.getPickorderViewModel().pGetLinesHandledFromDatabaseObl();
        if (hulpResultObl == null || hulpResultObl.size() == 0) {
            return  resultObl;
        }

        for (cPickorderLineEntity pickorderLineEntity : hulpResultObl ) {
            cPickorderLine PickorderLine = new cPickorderLine(pickorderLineEntity);
            resultObl.add(PickorderLine);
        }
        return  resultObl;
    }

    public cBranch pGetBranchForOpenLines(String pvScannedBranchStr){

        cBranch resultBranch;
        List<cPickorderLine> hulpObl = this.pGetLinesNotHandledFromDatabaseObl();

        if (hulpObl == null || hulpObl.size() == 0) {
            return  null;
        }

        for (cPickorderLine pickorderLine : hulpObl) {

            if (pickorderLine.getDestinationNoStr().equalsIgnoreCase(pvScannedBranchStr)) {

                resultBranch =  cBranch.pGetBranchByCode(pickorderLine.getDestinationNoStr());
                return  resultBranch;
            }
        }

        return  null;

    }

    public cPickorderLine pGetNextLineToHandleForBin(String pvBinCodeStr) {

        List <cPickorderLine> hulpObl = this.pGetLinesNotHandledFromDatabaseObl();

        if (hulpObl == null || hulpObl.size() ==0 ) {
            return  null;
        }

        for (cPickorderLine pickorderLine : hulpObl) {

            if (pickorderLine.getBinCodeStr().equalsIgnoreCase(pvBinCodeStr)) {

                if (this.scannedBranch == null) {
                    return  pickorderLine;
                }

                if (pickorderLine.getDestinationNoStr().equalsIgnoreCase(this.scannedBranch.getBranchStr())) {
                    return  pickorderLine;
                }

            }
        }

        return  null;

    }

    public boolean pGetPackAndShipLinesViaWebserviceBln(Boolean pvRefreshBln) {

        if (pvRefreshBln) {
            cPickorderLinePackAndShip.allPackAndShipLinesObl = null;
        }

        cWebresult WebResult;
        WebResult =  this.getPickorderViewModel().pGetPackAndShipLinesFromWebserviceWrs();

        if (WebResult.getResultBln() && WebResult.getSuccessBln()){

            for (JSONObject jsonObject : WebResult.getResultDtt()) {
                if (cPickorderLinePackAndShip.allPackAndShipLinesObl == null) {
                    cPickorderLinePackAndShip.allPackAndShipLinesObl  = new ArrayList<>();
                }

                cPickorderLinePackAndShip pickorderLinePackAndShip = new cPickorderLinePackAndShip(jsonObject);

                //Nothing has been picked, so we can skip this line
                if (pickorderLinePackAndShip.quantityHandledDbl == 0) {
                    continue;
                }

                cPickorderLinePackAndShip.allPackAndShipLinesObl.add(pickorderLinePackAndShip);

                cShipment shipment = cShipment.pGetShipment(pickorderLinePackAndShip.getSourceNoStr());
                if (shipment == null ) {
                    cShipment shipmentToAdd = new cShipment(pickorderLinePackAndShip.getSourceNoStr());

                    shipmentToAdd.handledBln = pickorderLinePackAndShip.localStatusInt != cWarehouseorder.PackingAndShippingStatusEnu.Needed;

                    cShipment.pAddShipment(shipmentToAdd);
                    shipmentToAdd.pAddPackAndShipLine(pickorderLinePackAndShip);
                    continue;
                }

                shipment.pAddPackAndShipLine(pickorderLinePackAndShip);
            }


            return cPickorderLinePackAndShip.allPackAndShipLinesObl.size() != 0;

        }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETPICKORDERLINESPACKANDSHIP);
            return  false;
        }
    }

    public List<cShipment> pGetNotHandledShipmentsObl() {

        List<cShipment> resultObl = new ArrayList<>();

        if (this.shipmentObl() == null || this.shipmentObl().size() == 0) {
            return resultObl;
        }

        for (cShipment shipment : this.shipmentObl()) {

            if (!shipment.handledBln) {
                resultObl.add(shipment);
            }

        }
        return  resultObl;
    }

    public List<cShipment> pGetHandledShipmentsObl() {

        List<cShipment> resultObl = new ArrayList<>();

        if (this.shipmentObl() == null || this.shipmentObl().size() == 0) {
            return resultObl;
        }

        for (cShipment shipment : this.shipmentObl()) {

            if (shipment.handledBln) {
                resultObl.add(shipment);
            }

        }
        return  resultObl;
    }

    public List<cStorement> pGetNotHandledStorementsObl() {

        List<cStorement> resultObl = new ArrayList<>();

        if (this.storementObl() == null || this.storementObl().size() == 0) {
            return resultObl;
        }

        for (cStorement storement : this.storementObl()) {

            if (!storement.handledBln) {
                resultObl.add(storement);
            }

        }
        return  resultObl;
    }

    public List<cStorement> pGetHandledStorementsObl() {

        List<cStorement> resultObl = new ArrayList<>();

        if (this.storementObl() == null || this.storementObl().size() == 0) {
            return resultObl;
        }

        for (cStorement storement : this.storementObl()) {

            if (storement.handledBln) {
                resultObl.add(storement);
            }

        }
        return  resultObl;
    }

    public cResult pPickFaseHandledViaWebserviceRst() {

        cResult result;
        result = new cResult();
        result.resultBln = true;

        String workplaceStr = "";
        int actionInt;

        cWebresult webresult;

        if (cWorkplace.currentWorkplace != null) {
            workplaceStr = cWorkplace.currentWorkplace.getWorkplaceStr();
        }

        webresult =  this.getPickorderViewModel().pPickingHandledViaWebserviceWrs(workplaceStr);


        //No result, so something really went wrong
        if (webresult == null) {
            result.resultBln = false;
            result.activityActionEnu = cWarehouseorder.ActivityActionEnu.Unknown;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_couldnt_handle_step));
            return result;
        }

        //Something really went wrong
        if (!webresult.getSuccessBln()) {
            result.resultBln = false;
            result.activityActionEnu = cWarehouseorder.ActivityActionEnu.Unknown;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_couldnt_handle_step));
            return result;
        }

        //Get next action of webresult
        if (webresult.getResultLng() > 100) {
            actionInt  = (int) (webresult.getResultLng()/100);
        }
        else
        {
            if (webresult.getResultLng() < 10) {
                actionInt = webresult.getResultLng().intValue();
            }
            else{
                actionInt = -1;
            }
        }

        //Convert int to action
        result.activityActionEnu= cWarehouseorder.pGetActivityActionEnu(actionInt);

        //Everything was fine, so we are done
        if (webresult.getSuccessBln() && webresult.getResultBln() && result.activityActionEnu == cWarehouseorder.ActivityActionEnu.Unknown) {
            cPickorder.currentPickOrder.statusInt = webresult.getResultLng().intValue();
            result.resultBln = true;
            return result;
        }

        // We have an action to perform
        result.resultBln = false;

        if (result.activityActionEnu == cWarehouseorder.ActivityActionEnu.Hold) {
            result.pAddErrorMessage(cAppExtension.context.getString((R.string.hold_the_order)));
        }

        this.mGetCommentsViaWebError(webresult.getResultDtt());
        return  result;


    }

    public cResult pPickGeneratedFaseHandledViaWebserviceRst(String pvShipBinStr) {

        cResult result;
        result = new cResult();
        result.resultBln = true;

        int actionInt;

        cWebresult webresult;


        webresult =  this.getPickorderViewModel().pPickingGeneratedHandledViaWebserviceWrs(pvShipBinStr);


        //No result, so something really went wrong
        if (webresult == null) {
            result.resultBln = false;
            result.activityActionEnu = cWarehouseorder.ActivityActionEnu.Unknown;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_couldnt_handle_step));
            return result;
        }

        //Something really went wrong
        if (!webresult.getSuccessBln()) {
            result.resultBln = false;
            result.activityActionEnu = cWarehouseorder.ActivityActionEnu.Unknown;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_couldnt_handle_step));
            return result;
        }

        //Get next action of webresult
        if (webresult.getResultLng() > 100) {
            actionInt  = (int) (webresult.getResultLng()/100);
        }
        else
        {
            if (webresult.getResultLng() < 10) {
                actionInt = webresult.getResultLng().intValue();
            }
            else{
                actionInt = -1;
            }
        }

        //Convert int to action
        result.activityActionEnu= cWarehouseorder.pGetActivityActionEnu(actionInt);

        //Everything was fine, so we are done
        if (webresult.getSuccessBln() && webresult.getResultBln() && result.activityActionEnu == cWarehouseorder.ActivityActionEnu.Unknown) {
            cPickorder.currentPickOrder.statusInt = webresult.getResultLng().intValue();
            result.resultBln = true;
            return result;
        }

        // We have an action to perform
        result.resultBln = false;

        if (result.activityActionEnu == cWarehouseorder.ActivityActionEnu.Hold) {
            result.pAddErrorMessage(cAppExtension.context.getString((R.string.hold_the_order)));
        }

        this.mGetCommentsViaWebError(webresult.getResultDtt());
        return  result;


    }

    public cResult pStoreFaseHandledViaWebserviceRst() {

        cResult result;
        result = new cResult();
        result.resultBln = true;

        int actionInt;

        cWebresult webresult;

        webresult =  this.getPickorderViewModel().pStoreHandledViaWebserviceWrs();


        //No result, so something really went wrong
        if (webresult == null) {
            result.resultBln = false;
            result.activityActionEnu = cWarehouseorder.ActivityActionEnu.Unknown;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_couldnt_handle_step));
            return result;
        }

        //Something really went wrong
        if (!webresult.getSuccessBln()) {
            result.resultBln = false;
            result.activityActionEnu = cWarehouseorder.ActivityActionEnu.Unknown;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_couldnt_handle_step));
            return result;
        }

        //Get next action of webresult
        if (webresult.getResultLng() > 100) {
            actionInt  = (int) (webresult.getResultLng()/100);
        }
        else
        {
            if (webresult.getResultLng() < 10) {
                actionInt = webresult.getResultLng().intValue();
            }
            else{
                actionInt = -1;
            }
        }

        //Convert int to action
        result.activityActionEnu= cWarehouseorder.pGetActivityActionEnu(actionInt);

        //Everything was fine, so we are done
        if (webresult.getSuccessBln() && webresult.getResultBln() && result.activityActionEnu == cWarehouseorder.ActivityActionEnu.Unknown) {
            cPickorder.currentPickOrder.statusInt = webresult.getResultLng().intValue();
            result.resultBln = true;
            return result;
        }

        // We have an action to perform
        result.resultBln = false;

        if (result.activityActionEnu == cWarehouseorder.ActivityActionEnu.Hold) {
            result.pAddErrorMessage(cAppExtension.context.getString((R.string.hold_the_order)));
        }

        this.mGetCommentsViaWebError(webresult.getResultDtt());
        return  result;


    }

    public cResult pSortHandledViaWebserviceRst() {

        cResult result;
        result = new cResult();
        result.resultBln = true;

        String workplaceStr = "";

        cWebresult webresult;

        if (cWorkplace.currentWorkplace != null) {
            workplaceStr = cWorkplace.currentWorkplace.getWorkplaceStr();
        }

        webresult =  this.getPickorderViewModel().pSortHandledViaWebserviceWrs(workplaceStr);

        //No result, so something really went wrong
        if (webresult == null) {
            result.resultBln = false;
            result.activityActionEnu = cWarehouseorder.ActivityActionEnu.Unknown;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_couldnt_handle_step));
            return result;
        }

        //Everything was fine, so we are done
        if (webresult.getSuccessBln() && webresult.getResultBln()) {
            result.resultBln = true;
            return result;
        }

        //Something really went wrong
        if (!webresult.getSuccessBln()) {
            result.resultBln = false;
            result.activityActionEnu = cWarehouseorder.ActivityActionEnu.Unknown;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_couldnt_handle_step));
            return result;
        }

        // We got a succesfull response, but we need to do something with this activity
        if (!webresult.getResultBln() && webresult.getResultLng() > 0 ) {

            Long actionLng = 0L;

            if (webresult.getResultLng() < 10 ) {
                actionLng = webresult.getResultLng();
            }

            if (webresult.getResultLng() > 100) {
                actionLng  = webresult.getResultLng()/100;
            }

            //Try to convert action long to action enumerate
            cWarehouseorder.ActivityActionEnu activityActionEnu = cWarehouseorder.pGetActivityActionEnu(actionLng.intValue());

            result.resultBln = false;
            result.activityActionEnu = activityActionEnu;

            if (result.activityActionEnu == cWarehouseorder.ActivityActionEnu.Hold) {
                result.pAddErrorMessage(cAppExtension.context.getString((R.string.hold_the_order)));
            }

            this.mGetCommentsViaWebError(webresult.getResultDtt());
            return result;
        }

        return  result;


    }

    public cResult pShipHandledViaWebserviceRst() {

        cResult result;
        result = new cResult();
        result.resultBln = true;

        String workplaceStr = "";

        cWebresult webresult;

        if (cWorkplace.currentWorkplace != null) {
            workplaceStr = cWorkplace.currentWorkplace.getWorkplaceStr();
        }

        webresult =  this.getPickorderViewModel().pShipHandledViaWebserviceWrs(workplaceStr);

        //No result, so something really went wrong
        if (webresult == null) {
            result.resultBln = false;
            result.activityActionEnu = cWarehouseorder.ActivityActionEnu.Unknown;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_couldnt_handle_step));
            return result;
        }

        //Everything was fine, so we are done
        if (webresult.getSuccessBln() && webresult.getResultBln()) {
            result.resultBln = true;
            return result;
        }

        //Something really went wrong
        if (!webresult.getSuccessBln()) {
            result.resultBln = false;
            result.activityActionEnu = cWarehouseorder.ActivityActionEnu.Unknown;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_couldnt_handle_step));
            return result;
        }

        // We got a succesfull response, but we need to do something with this activity
        if (!webresult.getResultBln() && webresult.getResultLng() > 0 ) {

            Long actionLng = 0L;

            if (webresult.getResultLng() < 10 ) {
                actionLng = webresult.getResultLng();
            }

            if (webresult.getResultLng() > 100) actionLng = webresult.getResultLng() / 100;

            //Try to convert action long to action enumerate
            cWarehouseorder.ActivityActionEnu activityActionEnu = cWarehouseorder.pGetActivityActionEnu(actionLng.intValue());

            result.resultBln = false;
            result.activityActionEnu = activityActionEnu;

            if (result.activityActionEnu == cWarehouseorder.ActivityActionEnu.Hold) {
                result.pAddErrorMessage(cAppExtension.context.getString((R.string.hold_the_order)));
            }

            this.mGetCommentsViaWebError(webresult.getResultDtt());
            return result;
        }

        return  result;


    }

    public cResult pFinishSinglePiecesHandledViaWebserviceRst() {

        cResult result;
        result = new cResult();
        result.resultBln = true;

        String workplaceStr = "";

        cWebresult webresult;

        if (cWorkplace.currentWorkplace != null) {
            workplaceStr = cWorkplace.currentWorkplace.getWorkplaceStr();
        }

        webresult =  this.getPickorderViewModel().pFinishSinglePiecesHandledViaWebserviceWrs(workplaceStr);

        //No result, so something really went wrong
        if (webresult == null) {
            result.resultBln = false;
            result.activityActionEnu = cWarehouseorder.ActivityActionEnu.Unknown;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_couldnt_handle_step));
            return result;
        }

        //Everything was fine, so we are done
        if (webresult.getSuccessBln() && webresult.getResultBln()) {
            result.resultBln = true;
            return result;
        }

        //Something really went wrong
        if (!webresult.getSuccessBln()) {
            result.resultBln = false;
            result.activityActionEnu = cWarehouseorder.ActivityActionEnu.Unknown;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_couldnt_handle_step));
            return result;
        }

        // We got a succesfull response, but we need to do something with this activity
        if (!webresult.getResultBln() && webresult.getResultLng() > 0 ) {

            Long actionLng = 0L;

            if (webresult.getResultLng() < 10 ) {
                actionLng = webresult.getResultLng();
            }

            if (webresult.getResultLng() > 100) actionLng = webresult.getResultLng() / 100;

            //Try to convert action long to action enumerate
            cWarehouseorder.ActivityActionEnu activityActionEnu = cWarehouseorder.pGetActivityActionEnu(actionLng.intValue());

            result.resultBln = false;
            result.activityActionEnu = activityActionEnu;

            if (result.activityActionEnu == cWarehouseorder.ActivityActionEnu.Hold) {
                result.pAddErrorMessage(cAppExtension.context.getString((R.string.hold_the_order)));
            }

            this.mGetCommentsViaWebError(webresult.getResultDtt());
            return result;
        }

        return  result;


    }

    public boolean pGetAdressesViaWebserviceBln(Boolean pvRefreshBln) {

        if (pvRefreshBln) {
            cPickorderAddress.allAdressesObl = null;
            cPickorderAddress.pTruncateTableBln();
        }

        cWebresult WebResult;
        WebResult =  this.getPickorderViewModel().pGetAdressesFromWebserviceWrs();
        if (WebResult.getResultBln() && WebResult.getSuccessBln()){

            for (JSONObject jsonObject : WebResult.getResultDtt()) {
                cPickorderAddress pickorderAddress = new cPickorderAddress(jsonObject);
                pickorderAddress.pInsertInDatabaseBln();
            }

            return  true;
        }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETPICKORDERADDRESSES);
            return  false;
        }
    }

    public boolean pGetPropertyLineDataViaWebserviceBln() {

        //If there are no property groups we are done
        if (cPropertyGroup.allPropertyGroupsObl == null || cPropertyGroup.allPropertyGroupsObl.size() == 0) {
            return true;
        }

        if (this.itemProperyDataObl != null && this.itemProperyDataObl.size() > 0) {
            return true;
        }

        this.itemProperyDataObl = new LinkedHashMap<>();

        //We need a viewmodel to make a call to the web service
        cPropertyGroupViewModel propertyGroupViewModel = new ViewModelProvider(cAppExtension.fragmentActivity).get(cPropertyGroupViewModel.class);

        //Get data for each property group
        for (cPropertyGroup propertyGroup : cPropertyGroup.allPropertyGroupsObl) {


            //Get data for this property group
            cWebresult WebResult = propertyGroupViewModel.pGetPropertyDataFromWebserviceWrs("PICKEN",
                    this.getOrderNumberStr(),
                    propertyGroup.getPropertyGroupStr());

            if (WebResult.getResultBln() && WebResult.getSuccessBln()) {

                //Loop through received JSON objects
                for (JSONObject jsonObject : WebResult.getResultDtt()) {

                    //JSON object contains LineNo, search lines for this LineNo
                    try {

                        List<JSONObject> objectsForThisLineObl;

                        objectsForThisLineObl = this.itemProperyDataObl.get(jsonObject.getInt(cDatabase.LINENO_NAMESTR));

                        if (objectsForThisLineObl == null) {
                            objectsForThisLineObl = new ArrayList<>();
                            objectsForThisLineObl.add(jsonObject);
                            this.itemProperyDataObl.put(jsonObject.getInt(cDatabase.LINENO_NAMESTR), objectsForThisLineObl);
                        } else {
                            objectsForThisLineObl.add(jsonObject);
                        }


                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }
        return true;
    }

    public boolean pGetBarcodesViaWebserviceBln(Boolean pvRefreshBln) {

        if (pvRefreshBln) {
            cPickorderBarcode.allBarcodesObl = null;
            cPickorderBarcode.pTruncateTableBln();
        }

        if (this.articleObl == null) {
            this.articleObl = new TreeMap<>();
        }

        cWebresult WebResult;
        WebResult =  this.getPickorderViewModel().pGetBarcodesFromWebserviceWrs();



        if (WebResult.getResultBln() && WebResult.getSuccessBln()){

            for (JSONObject jsonObject : WebResult.getResultDtt()) {
                cPickorderBarcode pickorderBarcode = new cPickorderBarcode(jsonObject);
                pickorderBarcode.pInsertInDatabaseBln();

                if (!cPickorder.currentPickOrder.articleObl.containsKey(pickorderBarcode.getItemNoAndVariantCodeStr())) {
                    cArticle article = new cArticle(jsonObject);
                    article.descriptionStr = pickorderBarcode.getBarcodeStr();
                    this.articleObl.put(pickorderBarcode.getItemNoAndVariantCodeStr(), article);
                }
            }
            if (!this.isGeneratedOrderBln()) {
                return cPickorderBarcode.allBarcodesObl.size() != 0;
            }
            else
            {
                if (cPickorderBarcode.allBarcodesObl == null ){
                    cPickorderBarcode.allBarcodesObl = new ArrayList<>();

                }
                return  true;
            }
        }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETPICKORDERBARCODES);
            return  false;
        }
    }

    public boolean pGetCompositeBarcodesViaWebserviceBln(Boolean pvRefreshBln) {

        if (pvRefreshBln) {
            cPickorderCompositeBarcode.allCompositeBarcodesObl = null;
            cPickorderCompositeBarcode.pTruncateTableBln();
        }

        cWebresult WebResult;
        WebResult =  this.getPickorderViewModel().pGetCompositeBarcodesFromWebserviceWrs();

        if (WebResult.getResultBln() && WebResult.getSuccessBln()){

            for (JSONObject jsonObject : WebResult.getResultDtt()) {
                cPickorderCompositeBarcode pickorderCompositeBarcode = new cPickorderCompositeBarcode(jsonObject);
                pickorderCompositeBarcode.pInsertInDatabaseBln();

            }
        }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETWAREHOUSEORDERCOMPOSITEBARCODES);
            return  false;
        }

        return  true;
    }

    public boolean pGetCommentsViaWebserviceBln(Boolean pvRefeshBln) {

        if (pvRefeshBln) {
            cComment.allCommentsObl = null;
            cComment.pTruncateTableBln();
            cComment.commentsShownBln = false;
        }

        cWebresult webresult;
        webresult = this.getPickorderViewModel().pGetCommentsFromWebserviceWrs();
        if (webresult.getResultBln() && webresult.getSuccessBln()) {

            cComment.allCommentsObl = new ArrayList<>();

            for (JSONObject jsonObject : webresult.getResultDtt()) {
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

    public boolean pGetShippingPackagedViaWebserviceBln(Boolean pvRefeshBln) {
        if (pvRefeshBln) {
            cPickorderShipPackage.allPackagesObl = null;
            cPickorderShipPackage.pTruncateTableBln();
        }

        cWebresult webresult;
        webresult = this.getPickorderViewModel().pGetPackagesFromWebserviceWrs();
        if (webresult.getResultBln() && webresult.getSuccessBln()) {
            for (JSONObject jsonObject : webresult.getResultDtt()) {
                cPickorderShipPackage pickorderShipPackage = new cPickorderShipPackage(jsonObject);
                pickorderShipPackage.pInsertInDatabaseBln();
            }
            return true;
        }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETPICKORDERCOMMENTS);
            return false;
        }
    }

    public cPickorderLine pGetLineNotHandledByBarcode(cBarcodeScan pvBarcodeScan) {

        if (this.barcodesObl() == null || this.barcodesObl().size() == 0)  {
            return  null;
        }

        List<cPickorderLine> hulpObl = this.pGetLinesNotHandledFromDatabaseObl();
        if (hulpObl == null || hulpObl.size() == 0) {
            return  null;
        }

        String barcodeToMatchStr = pvBarcodeScan.getBarcodeOriginalStr();
        if (pvBarcodeScan.getBarcodeTypeStr().toUpperCase().contains("EAN") && pvBarcodeScan.getBarcodeOriginalStr().length() == 13 ) {
            barcodeToMatchStr = pvBarcodeScan.getBarcodeFormattedStr();
        }

        cPickorderBarcode pickorderBarcodeWithBarcode = null;

        for (cPickorderBarcode pickorderBarcode : this.barcodesObl()) {
            if (pickorderBarcode.getBarcodeStr().equalsIgnoreCase(barcodeToMatchStr) || pickorderBarcode.getBarcodeWithoutCheckDigitStr().equalsIgnoreCase(barcodeToMatchStr)){
                pickorderBarcodeWithBarcode = pickorderBarcode;
                break;
            }
        }

        if (pickorderBarcodeWithBarcode  == null) {
            return  null;
        }


        List<cPickorderLine> newLinesObl = new ArrayList<>();
        List<cPickorderLine> busyLinesObl = new ArrayList<>();

        for (cPickorderLine pickorderLine : hulpObl) {
            if (pickorderLine.getItemNoStr().equalsIgnoreCase(pickorderBarcodeWithBarcode.getItemNoStr()) &&
                    pickorderLine.getVariantCodeStr().equalsIgnoreCase((pickorderBarcodeWithBarcode.getVariantCodeStr()))) {

                if (pickorderLine.getQuantityHandledDbl() >0 ) {
                    busyLinesObl.add(pickorderLine);
                }

                if (pickorderLine.getQuantityHandledDbl() == 0 ) {
                    newLinesObl.add(pickorderLine);
                }
            }
        }

        if (busyLinesObl.size() > 0) {
            return  busyLinesObl.get(0);
        }

        if (newLinesObl .size() > 0) {
            return  newLinesObl.get(0);
        }


        return null;
    }

    public Boolean pUpdateWorkplaceViaWebserviceBln(String pvWorkplaceStr) {

        return  this.getPickorderViewModel().pPickorderUpdateWorkplaceViaWebserviceBln(pvWorkplaceStr);

    }

    public cResult pUpdateSelectedRst(boolean pvSelectedBln ) {

        cResult result = new cResult();
        result.resultBln = true;

        if  (!pvSelectedBln) {
            if(cPickorder.pickorderSelectedObl().size() > 0) {
                if (!this.mRemoveOrderFromCombinedOrderViaWebserviceBln()) {
                    result.resultBln = false;
                    result.pAddErrorMessage(cAppExtension.activity.getString(R.string.couldnt_remove_order_from_combined_order));
                    return result;
                }

                this.isSelectedBln = pvSelectedBln;

                if (!this.getPickorderViewModel().pUpdateIsSelectedBln()) {
                    result.resultBln = false;
                    result.pAddErrorMessage(cAppExtension.activity.getString(R.string.couldnt_update_order));
                    return result;
                }

                if (cPickorder.pickorderSelectedObl().size() == 0) {

                    if (!cPickorder.currentPickOrder.mRemoveCombinedOrderViaWebserviceBln()) {
                        result.resultBln = false;
                        result.pAddErrorMessage(cAppExtension.activity.getString(R.string.couldnt_remove_combined_order));
                        return result;
                    }

                }


                return  result;
            }
        }


        this.isSelectedBln = pvSelectedBln;

        if (!this.getPickorderViewModel().pUpdateIsSelectedBln()) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.activity.getString(R.string.couldnt_update_order));
            return result;
        }

        if (pvSelectedBln) {

            //This is the first pickorder that we selected, so create a combined pick from this
            if (cPickorder.pickorderSelectedObl().size() == 1) {

                cResult hulpRst = mCreateCombinedOrderViaWebserviceRst();
                if (!hulpRst.resultBln) {
                    result.resultBln = false;
                    result.pAddErrorMessage(cAppExtension.activity.getString(R.string.couldnt_create_combined_order) + " " + hulpRst.messagesStr());
                    return result;
                }
            }
            else
            {
                if (!this.mAddOrderToCombinedOrderViaWebserviceBln()) {
                    result.resultBln = false;
                    result.pAddErrorMessage(cAppExtension.activity.getString(R.string.couldnt_create_combined_order));
                    return result;
                }
            }
            return  result;
        }

        return result ;
    }

    public static cResult pCreatePickOrderViaWebserviceRst(String pvDocumentstr,  boolean pvCheckBarcodesBln) {

        cResult result;
        result = new cResult();
        result.resultBln = true;

        cWebresult WebResult;

        cPickorderViewModel pickorderViewModel =   new ViewModelProvider(cAppExtension.fragmentActivity).get(cPickorderViewModel.class);
        WebResult = pickorderViewModel.pCreatePickOrderViaWebserviceWrs(pvDocumentstr, pvCheckBarcodesBln);

        //No result, so something really went wrong
        if (WebResult == null) {
            result.resultBln = false;
            result.activityActionEnu = cWarehouseorder.ActivityActionEnu.Unknown;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_couldnt_handle_step));
            return result;
        }

        if (WebResult.getResultBln()&& WebResult.getSuccessBln()) {


            // We got a succesfull response, but we need to do something with this activity

            Long actionLng = WebResult.getResultLng();

            if (WebResult.getResultLng() < 10 ) {
                actionLng = WebResult.getResultLng();
            }

            if (WebResult.getResultLng() > 100) {
                actionLng  = WebResult.getResultLng()/100;
            }



            //Try to convert action long to action enumerate
            result.activityActionEnu= cWarehouseorder.pGetActivityActionEnu(actionLng.intValue());

            if (result.activityActionEnu == cWarehouseorder.ActivityActionEnu.NoStart) {
                result.resultBln = false;
                result.pAddErrorMessage(cAppExtension.context.getString((R.string.order_cant_be_started)));
                return  result;
            }

            if (result.activityActionEnu== cWarehouseorder.ActivityActionEnu.Delete) {
                result.resultBln = false;
                result.pAddErrorMessage(cAppExtension.context.getString((R.string.order_has_deleted_by_erp)));
                return  result;
            }
            if (result.activityActionEnu == cWarehouseorder.ActivityActionEnu.Next) {
                result.resultBln = true;
                result.pSetResultAction(WebResult.pGetNextActivityObl());
                return  result;
            }

            //No activty created
            if (WebResult.getResultDtt() == null || WebResult.getResultDtt().size() == 0) {
                result.resultBln = false;
                result.pAddErrorMessage(cAppExtension.context.getString((R.string.message_order_not_created)));
                return  result;
            }

            //Something went wrong, we received a comment
            if (actionLng == 80) {
                result.resultBln = false;
                cComment comment = new cComment(WebResult.getResultDtt().get(0));
                result.pAddErrorMessage(comment.commentTextStr);
                return  result;
            }

            cPickorder pickorder = new cPickorder(WebResult.getResultDtt().get(0));
            pickorder.pInsertInDatabaseBln();
            cPickorder.currentPickOrder = pickorder;


        } else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_RECEIVECREATE);
            result.resultBln = false;
        }
        return result;
    }

    public boolean pAddERPBarcodeBln(cBarcodeScan pvBarcodeScan) {

        //Get article info via the web service
        cPickorder.currentPickOrder.currentArticle  = cArticle.pGetArticleByBarcodeViaWebservice(pvBarcodeScan);

        //We failed to get the article
        if ( cPickorder.currentPickOrder.currentArticle == null) {
            return false;
        }

        cArticle.currentArticle = cPickorder.currentPickOrder.currentArticle;

        //Add article to article object list
        cPickorder.currentPickOrder.articleObl.put(cPickorder.currentPickOrder.currentArticle.getItemNoAndVariantCodeStr(), cPickorder.currentPickOrder.currentArticle);


        //Get barcodes for this article
        if (! cPickorder.currentPickOrder.currentArticle.pGetBarcodesViaWebserviceBln(pvBarcodeScan)) {
            return false;
        }

        boolean isUniqueBarcodeBln = false;

        if (cArticle.currentArticle.barcodesObl != null && cArticle.currentArticle.barcodesObl.get(0).getUniqueBarcodeBln()) {
            isUniqueBarcodeBln = true;
        }

        if (cPickorderBarcode.allBarcodesObl == null) {
            cPickorderBarcode.allBarcodesObl= new ArrayList<>();
        }

        for (cArticleBarcode articleBarcode :  cPickorder.currentPickOrder.currentArticle.barcodesObl) {
            if (articleBarcode.getBarcodeStr().equalsIgnoreCase(pvBarcodeScan.getBarcodeOriginalStr()) ||
                    articleBarcode.getBarcodeWithoutCheckDigitStr().equalsIgnoreCase(pvBarcodeScan.getBarcodeOriginalStr())) {

                //Then add barcode
                cWebresult WebResult =  this.getPickorderLineViewModel().pAddERPBarcodeViaWebserviceWrs(pvBarcodeScan, isUniqueBarcodeBln);
                if (WebResult.getResultBln()&& WebResult.getSuccessBln() ){
                    cPickorderBarcode pickorderBarcode = new cPickorderBarcode(WebResult.getResultDtt().get(0));
                    cPickorderBarcode.allBarcodesObl.add(pickorderBarcode);
                    cPickorderBarcode.currentPickorderBarcode= pickorderBarcode;
                }
            }
        }


        return  true;
    }

    public  double pGetQuantityToPick(cArticleStock pvArticleStock) {

        double resultDbl = pvArticleStock.getQuantityDbl();


        for (cPickorderLine pickorderLine : this.linesObl()) {

            if (!pickorderLine.getBinCodeStr().equalsIgnoreCase(pvArticleStock.getBincodeStr())) {
                continue;
            }

            if (!pickorderLine.getItemNoStr().equalsIgnoreCase(pvArticleStock.getItemNoStr())) {
                continue;
            }

            if (!pickorderLine.getVariantCodeStr().equalsIgnoreCase(pvArticleStock.getVariantCodeStr())) {
                continue;
            }

            resultDbl -= pickorderLine.getQuantityHandledDbl();

        }

        if (resultDbl < 0) {
            resultDbl = 0;
        }

        return  resultDbl;
    }


    public  boolean pCreateItemVariantGeneratedNeededBln() {

        if (this.linesObl() == null | this.linesObl().size() == 0) {
            return  true;
        }


        for (cPickorderLine pickorderLine : this.linesObl()) {

            if (!pickorderLine.getItemNoStr().equalsIgnoreCase(cPickorderLine.currentPickOrderLine.getItemNoStr()) ||
                    !pickorderLine.getVariantCodeStr().equalsIgnoreCase(cPickorderLine.currentPickOrderLine.getVariantCodeStr())) {
                continue;
            }

            if (!pickorderLine.getBinCodeStr().equalsIgnoreCase(cPickorderLine.currentPickOrderLine.getBinCodeStr())) {
                continue;
            }

            if (!pickorderLine.getDestinationNoStr().equalsIgnoreCase(cPickorderLine.currentPickOrderLine.getDestinationNoStr())) {
                continue;
            }

            return  false;

        }


        return  true;
    }

    //End Region Public Methods

    //Region Private Methods

    private boolean mGetLineBarcodesViaWebserviceBln(cWarehouseorder.ActionTypeEnu pvActionTypeEnu) {

        cPickorderLineBarcode.allLineBarcodesObl = null;
        cPickorderLineBarcode.pTruncateTableBln();

        cWebresult WebResult;
        WebResult =  this.getPickorderViewModel().pGetLineBarcodesFromWebserviceWrs(pvActionTypeEnu);
        if (WebResult.getResultBln() && WebResult.getSuccessBln()){

            for (JSONObject jsonObject : WebResult.getResultDtt()) {

                cPickorderLineBarcode pickorderLineBarcode = new cPickorderLineBarcode(jsonObject);
                pickorderLineBarcode.pInsertInDatabaseBln();
            }

            if (this.isGeneratedOrderBln()) {
                if (cPickorderLineBarcode.allLineBarcodesObl == null) {
                    cPickorderLineBarcode.allLineBarcodesObl = new ArrayList<>();
                }
            }

            return  true;

        }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETPICKORDERBARCODES);
            return  false;
        }
    }

    private boolean mGetLinePropertysViaWebserviceBln() {

        cPickorderLineProperty.allLinePropertysObl = null;
        cPickorderLineProperty.pTruncateTableBln();

        cWebresult WebResult;
        WebResult =  this.getPickorderViewModel().pGetLinePropertysViaWebserviceWrs();
        if (WebResult.getResultBln() && WebResult.getSuccessBln()){

            for (JSONObject jsonObject : WebResult.getResultDtt()) {
                cPickorderLineProperty pickorderLineProperty = new cPickorderLineProperty(jsonObject);
                pickorderLineProperty.pInsertInDatabaseBln();
            }

            return  true;
        }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_WAREHOUSEOPDRACHTLINEITEMPROPERTIESGET);
            return  false;
        }
    }

    private boolean mGetLinePropertyValuesViaWebserviceBln() {


        cPickorderLinePropertyValue.allLinePropertysValuesObl = null;
        cPickorderLinePropertyValue.pTruncateTableBln();

        cWebresult WebResult;
        WebResult =  this.getPickorderViewModel().pGetLinePropertyValuesViaWebserviceWrs();
        if (WebResult.getResultBln() && WebResult.getSuccessBln()){

            for (JSONObject jsonObject : WebResult.getResultDtt()) {
                cPickorderLinePropertyValue pickorderLinePropertyValue = new cPickorderLinePropertyValue(jsonObject);
                pickorderLinePropertyValue.pInsertInDatabaseBln();
            }

            return  true;
        }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_WAREHOUSEOPDRACHTLINEITEMPROPERTIEVALUESGET);
            return  false;
        }
    }

    private boolean mGetSortingDetailsBln(){

        if (!this.isPVBln() && !this.isBPBln()){
            return  true;
        }

        for (cPickorderLine pickorderLine : this.linesObl()) {


            if (pickorderLine.processingSequenceStr.isEmpty()) {
                continue;
            }

            if (pickorderLine.processingSequenceStr.equalsIgnoreCase(pickorderLine.getSourceNoStr())) {
                continue;
            }

            cSalesOrderPackingTable salesOrderPackingTable = new cSalesOrderPackingTable(pickorderLine.getSourceNoStr(),pickorderLine.processingSequenceStr);
            salesOrderPackingTable.pInsertInDatabaseBln();

        }


        return  true;

    }

    private boolean mGetArticleImagesViaWebserviceBln() {

        if (!this.isPickWithPictureBln() || !this.isPickPickWithPicturePrefetchBln()) {
            return  true;
        }

        cArticleImage.allImages = null;
        cArticleImage.pTruncateTableBln();


        if (this.imagesObl()  != null) {
            return  true;
        }

        if (this.linesObl() == null || this.linesObl().size() == 0) {
            return this.isGeneratedOrderBln();
        }

        List<String> itemNoAndVariantCodeObl;
        itemNoAndVariantCodeObl = new ArrayList<>();

        for (cPickorderLine pickorderLine : this.linesObl()) {
            String itemNoAndVariantCodeStr = pickorderLine.getItemNoStr() + ";" + pickorderLine.getVariantCodeStr();

            if (!itemNoAndVariantCodeObl.contains(itemNoAndVariantCodeStr)) {
                itemNoAndVariantCodeObl.add(itemNoAndVariantCodeStr);
            }
        }

        if (itemNoAndVariantCodeObl.size() > 0) {
            cWebresult WebResult;
            cArticleImageViewModel articleImageViewModel = new ViewModelProvider(cAppExtension.fragmentActivity).get(cArticleImageViewModel.class);
            WebResult = articleImageViewModel.pGetArticleImagesFromWebserviceWrs(itemNoAndVariantCodeObl);
            if (WebResult.getResultBln() && WebResult.getSuccessBln()){

                cArticleImage.allImages = new ArrayList<>();

                for (JSONObject jsonObject : WebResult.getResultDtt()) {
                    cArticleImage articleImage = new cArticleImage(jsonObject);

                    if (!cArticleImage.allImages.contains(articleImage)) {
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

            return  true;
    }

    private void mGetCommentsViaWebError(List<JSONObject> pvResultDtt) {

        cComment.allCommentsObl = new ArrayList<>();

        for (JSONObject jsonObject : pvResultDtt) {
            cComment comment = new cComment(jsonObject);
            comment.pInsertInDatabaseBln();
        }

    }

    private static void mTruncateTable(){
        cPickorderViewModel pickorderViewModel =  new ViewModelProvider(cAppExtension.fragmentActivity).get(cPickorderViewModel.class);
        pickorderViewModel.deleteAll();
    }

    private cResult mCreateCombinedOrderViaWebserviceRst() {

        cResult result = new cResult();
        result.resultBln = true;

        cWebresult WebResult;
        WebResult =  this.getPickorderViewModel().pCreateCombinedPickViaWebserviceWrs();

        if (WebResult.getResultBln() && WebResult.getSuccessBln()){

            for (JSONObject jsonObject : WebResult.getResultDtt()) {
                cPickorder.currentCombinedPickOrder  = new cPickorder(jsonObject);
                break;
            }

            return result;

        }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETPICKORDERLINESPACKANDSHIP);
            result.resultBln = false;
            result.pAddErrorMessage(WebResult.getResultStr());
            return  result;
        }
    }

    private boolean mAddOrderToCombinedOrderViaWebserviceBln() {

        cWebresult WebResult;
        WebResult =  this.getPickorderViewModel().pAddOrderToCombinedPickViaWebserviceWrs();

        if (WebResult.getResultBln() && WebResult.getSuccessBln()){
            return true;

        }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETPICKORDERLINESPACKANDSHIP);
            return  false;
        }
    }

    private boolean mRemoveOrderFromCombinedOrderViaWebserviceBln() {

        cWebresult WebResult;
        WebResult =  this.getPickorderViewModel().pRemoveOrderFromCombinedPickViaWebserviceWrs();

        if (WebResult.getResultBln() && WebResult.getSuccessBln()){
            return true;

        }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETPICKORDERLINESPACKANDSHIP);
            return  false;
        }
    }

    private boolean mRemoveCombinedOrderViaWebserviceBln() {

        cWebresult WebResult;
        WebResult =  this.getPickorderViewModel().pRemoveCombinedPickViaWebserviceWrs();

        if (WebResult.getResultBln() && WebResult.getSuccessBln()){
            cPickorder.currentCombinedPickOrder = null;
            return true;

        }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETPICKORDERLINESPACKANDSHIP);
            return  false;
        }
    }

    private boolean mGetSettingsViaWebserviceBln() {

        cPickorderSetting.allSettingObl = null;
        cPickorderSetting.pTruncateTableBln();

        cWebresult WebResult;
        WebResult =  this.getPickorderViewModel().pGetSettingsFromWebserviceWrs();
        if (WebResult.getResultBln() && WebResult.getSuccessBln()){

            for (JSONObject jsonObject : WebResult.getResultDtt()) {
                cPickorderSetting pickorderSetting = new cPickorderSetting(jsonObject);
                pickorderSetting.pInsertInDatabaseBln();
            }
            return  true;
        }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_WAREHOUSEOPDRACHTSETTINGSGET);
            return  false;
        }
    }

    private boolean mGetFinishPackSinglePieceLinesViaWebserviceBln() {


        cPickorderLinePackAndShip.allPackAndShipLinesObl = null;


        cWebresult WebResult;
        WebResult =  this.getPickorderViewModel().pGetFinishPackSinglePieceLinesViaWebserviceWrs();

        if (WebResult.getResultBln() && WebResult.getSuccessBln()){

            for (JSONObject jsonObject : WebResult.getResultDtt()) {
                if (cPickorderLineFinishSinglePiece.allFinishSinglePieceLinesObl == null) {
                    cPickorderLineFinishSinglePiece.allFinishSinglePieceLinesObl  = new ArrayList<>();
                }

                cPickorderLineFinishSinglePiece pickorderLineFinishSinglePiece = new cPickorderLineFinishSinglePiece(jsonObject);

                //Nothing has been picked, so we can skip this line
                if (pickorderLineFinishSinglePiece.quantityHandledDbl == 0) {
                    continue;
                }

                cPickorderLineFinishSinglePiece.allFinishSinglePieceLinesObl.add(pickorderLineFinishSinglePiece);
            }


            return cPickorderLineFinishSinglePiece.allFinishSinglePieceLinesObl.size() != 0;

        }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETPICKORDERLINESPACKANDSHIP);
            return  false;
        }
    }


    //End Region Private Methods

}
