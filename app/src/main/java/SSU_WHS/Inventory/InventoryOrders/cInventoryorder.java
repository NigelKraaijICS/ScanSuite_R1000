package SSU_WHS.Inventory.InventoryOrders;

import androidx.lifecycle.ViewModelProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

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
import SSU_WHS.Basics.BranchBin.cBranchBin;
import SSU_WHS.Basics.Users.cUser;
import SSU_WHS.General.Comments.cComment;
import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.General.Warehouseorder.cWarehouseorderViewModel;
import SSU_WHS.Inventory.InventoryorderBarcodes.cInventoryorderBarcode;
import SSU_WHS.Inventory.InventoryorderBarcodes.cInventoryorderBarcodeEntity;
import SSU_WHS.Inventory.InventoryorderBins.cInventoryorderBin;
import SSU_WHS.Inventory.InventoryorderBins.cInventoryorderBinEntity;
import SSU_WHS.Inventory.InventoryorderLineBarcodes.cInventoryorderLineBarcode;
import SSU_WHS.Inventory.InventoryorderLineBarcodes.cInventoryorderLineBarcodeEntity;
import SSU_WHS.Inventory.InventoryorderLines.cInventoryorderLine;
import SSU_WHS.Inventory.InventoryorderLines.cInventoryorderLineEntity;
import SSU_WHS.Inventory.InventoryorderLines.cInventoryorderLineViewModel;
import SSU_WHS.LineItemProperty.LineProperty.cLineProperty;
import SSU_WHS.LineItemProperty.LinePropertyValue.cLinePropertyValue;
import SSU_WHS.Picken.PickorderLines.cPickorderLine;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebserviceDefinitions;
import nl.icsvertex.scansuite.R;


public class cInventoryorder {

    private final String orderNumberStr;
    public String getOrderNumberStr() {
        return orderNumberStr;
    }

    private final String orderTypeStr;
    public String getOrderTypeStr() {
        return orderTypeStr;
    }

    private final int numberOfBinsInt;
    public int getNumberOfBinsInt() {
        return numberOfBinsInt;
    }

    private final String assignedUserIdStr;
    public String getAssignedUserIdStr() {
        return assignedUserIdStr;
    }

    private final String currentUserIdStr;
    public String getCurrentUserIdStr() {
        return currentUserIdStr;
    }

    private String stockownerStr;
    public String getStockownerStr() {
        return stockownerStr;
    }

    private final int statusInt;
    public int getStatusInt() {
        return statusInt;
    }

    private final boolean invAmountManualBln;
    public boolean isInvAmountManualBln() {
        return invAmountManualBln;
    }

    private final boolean invBarcodeCheckBln;
    public boolean isInvBarcodeCheckBln() {
        return invBarcodeCheckBln;
    }

    private final boolean invAddExtraBinBln;
    public boolean isInvAddExtraBinBln() {
        return invAddExtraBinBln;
    }

    private final String externalReferenceStr;
    public String getExternalReferenceStr() {
        return externalReferenceStr;
    }

    private final int sourceDocumentInt;
    private int getSourceDocumentInt() { return sourceDocumentInt; }

    private final String documentStr;
    public String getDocumentStr() {
        return documentStr;
    }

    private final boolean inventoryWithPictureBln;
    public boolean isInventoryWithPictureBln() {
        return inventoryWithPictureBln;
    }

    private final boolean inventoryWithPicturePrefetchBln;
    private boolean isInventoryWithPicturePrefetchBln() {
        return inventoryWithPicturePrefetchBln;
    }

    public Boolean isGeneratedBln() {
        return this.getSourceDocumentInt() == cWarehouseorder.SourceDocumentTypeEnu.Generated;
    }

    public int unknownVariantCounterInt = 0;
    public int getUnknownVariantCounterInt() {
        return unknownVariantCounterInt;
    }

    public  LinkedHashMap<Integer, List<JSONObject>>  itemProperyDataObl;
    private final cInventoryorderEntity inventoryorderEntity;

    private cInventoryorderViewModel getInventoryorderViewModel() {
        return new ViewModelProvider(cAppExtension.fragmentActivity).get(cInventoryorderViewModel.class);
    }

    private cWarehouseorderViewModel getWarehouseorderViewModel() {
        return new ViewModelProvider(cAppExtension.fragmentActivity).get(cWarehouseorderViewModel.class);
    }

    public List<cComment> commentsObl() {
        return cComment.allCommentsObl;
    }
    public List<cInventoryorderBarcode> barcodesObl () {return  cInventoryorderBarcode.allInventoryorderBarcodesObl;}
    private List<cInventoryorderLine> linesObl() {return  cInventoryorderLine.allLinesObl;}
    private List<cArticleImage> imagesObl()  {
        return  cArticleImage.allImages;
    }

    public List<cLineProperty> linePropertysObl() { return  cLineProperty.allLinePropertysObl; }
    public List<cLinePropertyValue> linePropertyValueObl() { return  cLinePropertyValue.allLinePropertysValuesObl; }

    private static List<cInventoryorder> allCachedOrdersObl;
    public static List<cInventoryorder> allInventoryOrdersObl(Boolean pvRefreshBln ){

        if (pvRefreshBln) {
             cInventoryorder.allCachedOrdersObl = null;
        }

        if (cInventoryorder.allCachedOrdersObl != null) {
            return  cInventoryorder.allCachedOrdersObl;
        }

        cInventoryorder.allCachedOrdersObl  = new ArrayList<>();


        cInventoryorderViewModel inventoryorderViewModel = new ViewModelProvider(cAppExtension.fragmentActivity).get(cInventoryorderViewModel.class);
        List<cInventoryorderEntity> hulpObl  =  inventoryorderViewModel.pGetInventoriesWithFilterFromDatabaseObl(cUser.currentUser.getUsernameStr(), false);


        for (cInventoryorderEntity inventoryorderEntity : hulpObl) {
            cInventoryorder inventoryorder = new cInventoryorder(inventoryorderEntity);
            cInventoryorder.allCachedOrdersObl.add(inventoryorder);
        }

        return  cInventoryorder.allCachedOrdersObl;


    }

    private static List<String> binsObl;

    public static cInventoryorder currentInventoryOrder;

    //Region Public Properties

    //Region Constructor

    public cInventoryorder(JSONObject pvJsonObject) {

        this.inventoryorderEntity = new cInventoryorderEntity(pvJsonObject);
        this.orderNumberStr = this.inventoryorderEntity.getOrdernumberStr();
        this.orderTypeStr = this.inventoryorderEntity.getOrderTypeStr();
        this.numberOfBinsInt = cText.pStringToIntegerInt(this.inventoryorderEntity.getNumberofBinsStr());
        this.assignedUserIdStr = this.inventoryorderEntity.getAssignedUserIdStr();
        this.currentUserIdStr = this.inventoryorderEntity.getCurrentUserIdStr();
        this.stockownerStr = this.inventoryorderEntity.getStockOwnerStr();
        this.statusInt = cText.pStringToIntegerInt(this.inventoryorderEntity.getStatusStr());


        this.invAmountManualBln = cText.pStringToBooleanBln(this.inventoryorderEntity.getInvAmountManualStr(), false);
        this.invBarcodeCheckBln = cText.pStringToBooleanBln(this.inventoryorderEntity.getInvBarcodeCheckStr(), false);
        this.invAddExtraBinBln = cText.pStringToBooleanBln(this.inventoryorderEntity.getInvAddExtraBinStr(), false);

        this.externalReferenceStr = this.inventoryorderEntity.getExternalReferenceStr();

        this.sourceDocumentInt = cText.pStringToIntegerInt(this.inventoryorderEntity.getSourceDocumentStr()) ;
        this.documentStr = this.inventoryorderEntity.getDocumentStr();

        this.inventoryWithPictureBln = cText.pStringToBooleanBln(this.inventoryorderEntity.getInventoryWithPictureStr(),false) ;
        this.inventoryWithPicturePrefetchBln = cText.pStringToBooleanBln(this.inventoryorderEntity.getInventoryWithPicturePrefetchStr(),false) ;
    }

    public cInventoryorder(cInventoryorderEntity pvInventoryorderEntity) {

        this.inventoryorderEntity = pvInventoryorderEntity;
        this.orderNumberStr = this.inventoryorderEntity.getOrdernumberStr();
        this.orderTypeStr = this.inventoryorderEntity.getOrderTypeStr();
        this.numberOfBinsInt = cText.pStringToIntegerInt(this.inventoryorderEntity.getNumberofBinsStr());
        this.assignedUserIdStr = this.inventoryorderEntity.getAssignedUserIdStr();
        this.currentUserIdStr = this.inventoryorderEntity.getCurrentUserIdStr();
        this.stockownerStr = this.inventoryorderEntity.getStockOwnerStr();
        this.statusInt = cText.pStringToIntegerInt(this.inventoryorderEntity.getStatusStr());

        this.invAmountManualBln = cText.pStringToBooleanBln(this.inventoryorderEntity.getInvAmountManualStr(), false);
        this.invBarcodeCheckBln = cText.pStringToBooleanBln(this.inventoryorderEntity.getInvBarcodeCheckStr(), false);
        this.invAddExtraBinBln = cText.pStringToBooleanBln(this.inventoryorderEntity.getInvAddExtraBinStr(), false);

        this.externalReferenceStr = this.inventoryorderEntity.getExternalReferenceStr();

        this.sourceDocumentInt =  cText.pStringToIntegerInt(this.inventoryorderEntity.getSourceDocumentStr());
        this.documentStr = this.inventoryorderEntity.getDocumentStr();

        this.inventoryWithPictureBln = cText.pStringToBooleanBln(this.inventoryorderEntity.getInventoryWithPictureStr(),false) ;
        this.inventoryWithPicturePrefetchBln = cText.pStringToBooleanBln(this.inventoryorderEntity.getInventoryWithPicturePrefetchStr(),false) ;

    }

    //End Region Constructor

    //Region Public Methods

    //Region Orders

    public boolean pInsertInDatabaseBln() {
        getInventoryorderViewModel().insert(this.inventoryorderEntity);
        return true;
    }

    public static Boolean pCreateInventoryOrderViaWebserviceBln(String pvDocumentStr, boolean pvCheckBarcodesBln) {

        cWebresult WebResult;

        cInventoryorderViewModel inventoryorderViewModel = new ViewModelProvider(cAppExtension.fragmentActivity).get(cInventoryorderViewModel.class);
        WebResult = inventoryorderViewModel.pCreateInventoryOrderViaWebserviceWrs(pvDocumentStr, pvCheckBarcodesBln);

        if (WebResult.getResultBln()&& WebResult.getSuccessBln()) {

            if (WebResult.getResultDtt().size() == 1) {
                cInventoryorder inventoryorder = new cInventoryorder(WebResult.getResultDtt().get(0));
                inventoryorder.pInsertInDatabaseBln();
                cInventoryorder.currentInventoryOrder = inventoryorder;
                return true;
            }

        } else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETPICKORDERS);
            return false;
        }

        return false;
    }

    public static boolean pGetInventoryOrdersViaWebserviceBln(Boolean pvRefreshBln, String pvSearchTextStr) {

        if (pvRefreshBln) {
            cInventoryorder.mTruncateTable();
        }

        cWebresult WebResult;
        cInventoryorderViewModel inventoryorderViewModel = new ViewModelProvider(cAppExtension.fragmentActivity).get(cInventoryorderViewModel.class);
        WebResult = inventoryorderViewModel.pGetInventoryordersFromWebserviceWrs(pvSearchTextStr);
        if (WebResult.getResultBln() && WebResult.getSuccessBln()) {

            List<cInventoryorderEntity> insertObl = new ArrayList<>();

            for (JSONObject jsonObject : WebResult.getResultDtt()) {
                cInventoryorder inventoryorder = new cInventoryorder(jsonObject);
                insertObl.add(inventoryorder.inventoryorderEntity);
            }

            //Batch insert in database
            cInventoryorder.mInsertAllInDatabase(insertObl);

            //Make sure memory is filled
            cInventoryorder.allInventoryOrdersObl(true);
            return true;

        } else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETINVENTORYORDERS);
            return false;
        }



    }

    public cResult pLockViaWebserviceRst(cWarehouseorder.StepCodeEnu pvStepCodeEnu, int pvWorkFlowStepInt) {

        //Initialise result
        cResult result;
        result = new cResult();
        result.resultBln = true;

        cWebresult Webresult;
        boolean ignoreBusyBln = false;

        if (this.getStatusInt() > 10 && cUser.currentUser.getUsernameStr().equalsIgnoreCase(this.getCurrentUserIdStr())) {
            ignoreBusyBln = true;
        }

        Webresult = this.getWarehouseorderViewModel().pLockWarehouseopdrachtViaWebserviceWrs(cWarehouseorder.OrderTypeEnu.INVENTARISATIE.toString(),
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
        if (!Webresult.getResultBln() && Webresult.getResultLng() <= 0 && Webresult.getResultObl() != null &&
                Webresult.getResultObl().size() >= 2 && Webresult.getResultObl().get(0).equalsIgnoreCase("invalid_user_not_assigned")) {
            result.resultBln = false;
            result.activityActionEnu = cWarehouseorder.ActivityActionEnu.Unknown;
            result.pAddErrorMessage(cAppExtension.context.getString((R.string.message_meant_for_other_user)) + " " + Webresult.getResultObl().get(1));
            return result;
        }

        //Check if this activity is meant for a different user
        if (!Webresult.getResultBln() && Webresult.getResultLng() <= 0 && Webresult.getResultObl() != null &&
                Webresult.getResultObl().size() > 0 && ! Webresult.getResultObl().get(0).equalsIgnoreCase(cUser.currentUser.getUsernameStr())) {
            result.resultBln = false;
            result.activityActionEnu = cWarehouseorder.ActivityActionEnu.Unknown;
            result.pAddErrorMessage(cAppExtension.context.getString((R.string.message_another_user_already_started)) + " " + Webresult.getResultObl().get(0));
            return result;
        }

        // We got a succesfull response, but we need to do something with this activity
        if (!Webresult.getResultBln()&& Webresult.getResultLng() > 0) {

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

            cInventoryorder.currentInventoryOrder.mGetCommentsViaWebError(Webresult.getResultDtt());
            return result;
        }

        return result;

    }

    public boolean pLockReleaseViaWebserviceBln(cWarehouseorder.StepCodeEnu pvStepCodeEnu, int pvWorkFlowStepInt) {

        cWebresult Webresult;

        Webresult = this.getWarehouseorderViewModel().pLockReleaseWarehouseorderViaWebserviceWrs(cWarehouseorder.OrderTypeEnu.INVENTARISATIE.toString(), this.getOrderNumberStr(), cDeviceInfo.getSerialnumberStr(), pvStepCodeEnu.toString(), pvWorkFlowStepInt);

        return Webresult.getSuccessBln() && Webresult.getResultBln();
    }

    public void pDeleteDetails() {
        cInventoryorderLine.pTruncateTableBln();
        cInventoryorderBin.pTruncateTableBln();
        cInventoryorderBarcode.pTruncateTableBln();
        cInventoryorderLineBarcode.pTruncateTableBln();
    }

    public cResult pOrderHandledViaWebserviceRst() {

        cResult result;
        result = new cResult();
        result.resultBln = true;


        cWebresult webresult;
        cInventoryorderViewModel inventoryorderViewModel = new ViewModelProvider(cAppExtension.fragmentActivity).get(cInventoryorderViewModel.class);
        webresult =  inventoryorderViewModel.pHandledViaWebserviceWrs();

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

            cInventoryorder.currentInventoryOrder.mGetCommentsViaWebError(webresult.getResultDtt());
            return result;
        }

        return  result;


    }

    public static List<cInventoryorder> pGetInventoriesWithFilterFromDatabasObl() {

        List<cInventoryorder> resultObl = new ArrayList<>();
        List<cInventoryorderEntity> hulpResultObl;

        cInventoryorderViewModel inventoryorderViewModel = new ViewModelProvider(cAppExtension.fragmentActivity).get(cInventoryorderViewModel.class);
        hulpResultObl =  inventoryorderViewModel.pGetInventoriesWithFilterFromDatabaseObl(cUser.currentUser.getUsernameStr(), cSharedPreferences.userInventoryFilterBln());
        if (hulpResultObl == null || hulpResultObl.size() == 0) {
            return  resultObl;
        }

        for (cInventoryorderEntity inventoryorderEntity : hulpResultObl ) {
            cInventoryorder inventoryorder = new cInventoryorder(inventoryorderEntity);
            resultObl.add(inventoryorder);
        }

        return  resultObl;
    }

    public  boolean pCheckBarcodeWithLineBarcodesBln(cBarcodeScan pvBarcodeScan){

        //If scanned value matches the current barcodeStr, then we have a match
        if (pvBarcodeScan.getBarcodeOriginalStr().equalsIgnoreCase(cInventoryorderLineBarcode.currentInventoryorderLineBarcode.getBarcodeStr()) ||
            pvBarcodeScan.getBarcodeFormattedStr().equalsIgnoreCase(cInventoryorderBarcode.currentInventoryOrderBarcode.getBarcodeWithoutCheckDigitStr()) ) {
            //We have a match, so leave
            return  true;
        }

        //Check if this is a barcodeStr we already know
        cInventoryorderBarcode inventoryorderBarcode = cInventoryorder.currentInventoryOrder.pGetOrderBarcode(pvBarcodeScan);

        //We scanned a barcodeStr unknown to the order
        if (inventoryorderBarcode == null) {
            return false;
        }

        //We scanned a barcodeStr for a different article
        if (!inventoryorderBarcode.getItemNoStr().equalsIgnoreCase(cInventoryorderBarcode.currentInventoryOrderBarcode.getItemNoStr()) ||
            ! inventoryorderBarcode.getVariantCodeStr().equalsIgnoreCase(cInventoryorderBarcode.currentInventoryOrderBarcode.getVariantCodeStr())) {
            return false;
        }

        //We scanned a barcodeStr that belongs to the current article, so check if we already have a line barcodeStr
        for (cInventoryorderLineBarcode inventoryorderLineBarcode : cInventoryorderLine.currentInventoryOrderLine.lineBarcodesObl()) {

            //We have a match, so set
            if (inventoryorderLineBarcode.getBarcodeStr().equalsIgnoreCase(pvBarcodeScan.getBarcodeOriginalStr()) ||
                    inventoryorderLineBarcode.getBarcodeStr().equalsIgnoreCase(pvBarcodeScan.getBarcodeFormattedStr())) {
                cInventoryorderLineBarcode.currentInventoryorderLineBarcode = inventoryorderLineBarcode;
                cInventoryorderBarcode.currentInventoryOrderBarcode = inventoryorderBarcode;
                return  true;
            }
        }

        //Scanned barcodeStr is correct, but we need to create a line barcodeStr
        cInventoryorderLineBarcode.currentInventoryorderLineBarcode =  cInventoryorderLine.currentInventoryOrderLine.pAddLineBarcode(inventoryorderBarcode.getBarcodeStr(),inventoryorderBarcode.getQuantityPerUnitOfMeasureDbl(), true);
        cInventoryorderBarcode.currentInventoryOrderBarcode = inventoryorderBarcode;
        return  true;

    }

    private static  void mTruncateTable() {
        cInventoryorderViewModel inventoryorderViewModel = new ViewModelProvider(cAppExtension.fragmentActivity).get(cInventoryorderViewModel.class);
        inventoryorderViewModel.deleteAll();
    }

    //End Region Orders

    //Region Lines

    public boolean pGetLinesViaWebserviceBln(Boolean pvRefreshBln) {

        if (pvRefreshBln) {
            cInventoryorderLine.allLinesObl = null;
            cInventoryorderLine.pTruncateTableBln();
        }

        cWebresult WebResult;


        WebResult = getInventoryorderViewModel().pGetLinesFromWebserviceWrs();
        if (WebResult.getResultBln() && WebResult.getSuccessBln()) {

            if (cInventoryorderLine.allLinesObl == null) {
                cInventoryorderLine.allLinesObl = new ArrayList<>();
            }


            List<cInventoryorderLineEntity> importObl = new ArrayList<>();
            List<cInventoryorderLine> objectObl = new ArrayList<>();

            for (JSONObject jsonObject : WebResult.getResultDtt()) {

                cInventoryorderLine inventoryorderLine = new cInventoryorderLine(jsonObject);

                if (inventoryorderLine.getQuantityHandledAllScannersDbl() > inventoryorderLine.getQuantityDbl()) {
                    inventoryorderLine.quantityHandledDbl = inventoryorderLine.getQuantityHandledAllScannersDbl();
                }


                importObl.add(inventoryorderLine.inventoryorderLineEntity);
                objectObl.add((inventoryorderLine));

                if (! inventoryorderLine.getHandeledTimeStampStr().isEmpty()) {

                    cInventoryorderBin.currentInventoryOrderBin  =   cInventoryorder.currentInventoryOrder.pGetBin(inventoryorderLine.getBinCodeStr());
                    if ( cInventoryorderBin.currentInventoryOrderBin == null) {
                        continue;
                    }

                    if (cInventoryorder.currentInventoryOrder.isGeneratedBln()) {
                        cInventoryorderBin.currentInventoryOrderBin.statusInt = cWarehouseorder.InventoryBinStatusEnu.InventoryDone;
                    }

                    cInventoryorderBin.currentInventoryOrderBin.pUpdateStatusAndTimeStampInDatabase();
                    cInventoryorderBin.currentInventoryOrderBin = null;
                }

            }

            cInventoryorderLine.pInsertAllInDatabase(objectObl,importObl);


            return  true;

        } else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETINVENTORYORDERLINES);
            return false;
        }
    }

    public boolean pAddLineBln() {

        cWebresult WebResult =  getInventoryorderViewModel().pAddLineViaWebserviceWrs();
        if (WebResult.getResultBln()&& WebResult.getSuccessBln()){

            if (WebResult.getResultDtt().size() == 1) {
                cInventoryorderLine.currentInventoryOrderLine= new cInventoryorderLine(WebResult.getResultDtt().get(0));
                cInventoryorderLine.currentInventoryOrderLine.pInsertInDatabaseBln();
                cInventoryorderLine.currentInventoryOrderLine.pAddLineBarcode(cInventoryorderBarcode.currentInventoryOrderBarcode.getBarcodeStr(),cInventoryorderBarcode.currentInventoryOrderBarcode.getQuantityPerUnitOfMeasureDbl(), false);

                this.mGetLinePropertysViaWebserviceBln();
                return  true;
            }
        }
        else {
            cInventoryorder.currentInventoryOrder.unknownVariantCounterInt -= 1;
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_INVENTORYLINECREATE);
            return  false;
        }

        return  true;
    }

    public List<cInventoryorderLine> pGetLinesForBinObl(String pvBincodeStr) {

        List<cInventoryorderLine> resultObl = new ArrayList<>();
        for (cInventoryorderLine inventoryorderLine : cInventoryorder.currentInventoryOrder.linesObl()) {
            if (inventoryorderLine.getBinCodeStr().equalsIgnoreCase(pvBincodeStr)) {
                resultObl.add((inventoryorderLine));
            }
        }

        if (resultObl.size() > 1) {
            Collections.reverse(resultObl);
        }

        return resultObl;
    }

    public Double pGetTotalItemCountDbl() {

        Double resultDbl;

        cInventoryorderLineViewModel inventoryorderLineViewModel = new ViewModelProvider(cAppExtension.fragmentActivity).get(cInventoryorderLineViewModel.class);
        resultDbl = inventoryorderLineViewModel.pGetTotalCountDbl();
        if (resultDbl == null ) {
            return (double) 0;
        }

        return resultDbl;
    }

    public Double pGetItemCountForBinDbl(String pvBincodeStr) {

        Double resultDbl;

        cInventoryorderLineViewModel inventoryorderLineViewModel = new ViewModelProvider(cAppExtension.fragmentActivity).get(cInventoryorderLineViewModel.class);
        resultDbl = inventoryorderLineViewModel.pGetCountForBinCodeDbl(pvBincodeStr);
        if (resultDbl == null ) {
            return (double) 0;
        }

        return resultDbl;
    }

    public cInventoryorderLine pGetLineForArticleAndBin() {


        if (this.linesObl() == null || this.linesObl().size() == 0) {
            return  null;
        }

        for (cInventoryorderLine inventoryorderLine : this.linesObl()) {

            //Check if BIN matches current BIN
            if (! inventoryorderLine.getBinCodeStr().equalsIgnoreCase(   cInventoryorderBin.currentInventoryOrderBin.getBinCodeStr())) {
                continue;
            }

            //Check if item matches scanned item
            if (inventoryorderLine.getItemNoStr().equalsIgnoreCase(cInventoryorderBarcode.currentInventoryOrderBarcode.getItemNoStr()) &&
                    inventoryorderLine.getVariantCodeStr().equalsIgnoreCase(cInventoryorderBarcode.currentInventoryOrderBarcode.getVariantCodeStr())) {
                return  inventoryorderLine;
            }
        }

        return  null;
    }

    public cResult pGetOrderDetailsRst(){

        cResult result;

        result = new cResult();
        result.resultBln = true;

        //Get all bins for current order, if webservice error then stop
        if (!this.mGetBinsViaWebserviceBln()) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_bins_failed));
            return result;
        }

        //Get all linesInt for current order, if size = 0 or webservice error then stop
        if (!this.pGetLinesViaWebserviceBln(true)) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_lines_failed));
            return result;
        }

        //Get all linesInt for current order, if size = 0 or webservice error then stop
        if (!this.mGetPossibleBinsViaWebserviceBln()) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_possible_bins_failed));
            return result;
        }

        // Get all comments
        if (!this.pGetCommentsViaWebserviceBln(true)) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_comments_failed));
            return result;
        }

        //Get all barcodes
        if (!this.pGetBarcodesViaWebserviceBln(true)) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_barcodes_failed));
            return result;
        }

        //Get all inventorylinebarcodes
        if (!this.mGetLineBarcodesViaWebserviceBln()) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_line_barcodes_failed));
            return result;
        }

        // Get all article images, only if neccesary
        if (!this.mGetArticleImagesViaWebserviceBln()) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_article_images_failed));
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

        return  result;
    }

    private cInventoryorderLine mGetLineWithLineNo(Long pvLineNoLng) {

        if (this.linesObl() == null || this.linesObl().size() == 0) {
            return  null;
        }

        for (cInventoryorderLine inventoryorderLine : this.linesObl()) {

            if (cText.pIntToStringStr(inventoryorderLine.getLineNoInt()).equalsIgnoreCase(cText.pLongToStringStr(pvLineNoLng))) {
                return  inventoryorderLine;
            }

        }

        return  null;

    }

    //End Region Lines

    //Region BINS

    private boolean mGetBinsViaWebserviceBln() {

        cInventoryorderBin.allInventoryorderBinsObl = null;
        cInventoryorderBin.pTruncateTableBln();
        cInventoryorder.binsObl = new ArrayList<>();

        cWebresult WebResult;

        WebResult = getInventoryorderViewModel().pGetBinsFromWebserviceWrs();
        if (WebResult.getResultBln() && WebResult.getSuccessBln()) {

            if (cInventoryorderBin.allInventoryorderBinsObl == null) {
                cInventoryorderBin.allInventoryorderBinsObl = new ArrayList<>();
            }

            List<cInventoryorderBinEntity> insertObl = new ArrayList<>();
            List<cInventoryorderBin> closedBinsObl = new ArrayList<>();

            for ( JSONObject jsonObject : WebResult.getResultDtt()) {


                cInventoryorderBin inventoryorderBin = new cInventoryorderBin(jsonObject);
                cInventoryorder.binsObl.add(inventoryorderBin.getBinCodeStr());

                insertObl.add(inventoryorderBin.inventoryorderBinEntity);

                if (inventoryorderBin.getStatusInt() ==  cWarehouseorder.InventoryBinStatusEnu.InventoryDoneOnServer && !cInventoryorder.currentInventoryOrder.isGeneratedBln()) {
                    closedBinsObl.add(inventoryorderBin);
                }

            }

            cInventoryorderBin.pInsertAllInDatabase(insertObl);

            //Close all BINS in database after they got inserted
            for (cInventoryorderBin inventoryorderBin : closedBinsObl) {
                inventoryorderBin.pCloseBln(false);
            }

            return  true;

        } else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETINVENTORYORDERBINS);
            return false;
        }
    }

    private boolean mGetPossibleBinsViaWebserviceBln() {


        cWebresult WebResult;

        WebResult = getInventoryorderViewModel().pGetPossibleBinsFromWebserviceWrs();
        if (WebResult.getResultBln() && WebResult.getSuccessBln()) {

            if (cInventoryorderBin.allInventoryorderBinsObl == null) {
                cInventoryorderBin.allInventoryorderBinsObl = new ArrayList<>();
            }

            for ( JSONObject jsonObject : WebResult.getResultDtt()) {


                cInventoryorderBin inventoryorderBin = new cInventoryorderBin(jsonObject);

                if (cInventoryorder.binsObl.contains(inventoryorderBin.getBinCodeStr())) {
                    continue;
                }

                cInventoryorder.binsObl.add(inventoryorderBin.getBinCodeStr());
                inventoryorderBin.pInsertInDatabaseBln();
                inventoryorderBin.statusInt = cWarehouseorder.InventoryBinStatusEnu.New;
                inventoryorderBin.pUpdateStatusAndTimeStampInDatabase();

            }

            return  true;

        } else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETINVENTORYORDERBINS);
            return false;
        }
    }

    public List<cInventoryorderBin> pGetBinsDoneFromDatabasObl() {

        List<cInventoryorderBin> resultObl = new ArrayList<>();
        List<cInventoryorderBinEntity> hulpResultObl;

        hulpResultObl =  getInventoryorderViewModel().pGetBinsDoneFromDatabaseObl();
        if (hulpResultObl == null || hulpResultObl.size() == 0) {
            return  resultObl;
        }

        for (cInventoryorderBinEntity inventoryorderBinEntity : hulpResultObl ) {
            cInventoryorderBin inventoryorderBin = new cInventoryorderBin(inventoryorderBinEntity);
            resultObl.add(inventoryorderBin);
        }
        return  resultObl;
    }

    public List<cInventoryorderBin> pGetBinsNotDoneFromDatabasObl() {

        List<cInventoryorderBin> resultObl = new ArrayList<>();
        List<cInventoryorderBinEntity> hulpResultObl;

        hulpResultObl =  getInventoryorderViewModel().pGetBinsNotDoneFromDatabaseObl();
        if (hulpResultObl == null || hulpResultObl.size() == 0) {
            return  resultObl;
        }

        for (cInventoryorderBinEntity inventoryorderBinEntity : hulpResultObl ) {
            cInventoryorderBin inventoryorderBin = new cInventoryorderBin(inventoryorderBinEntity);
            resultObl.add(inventoryorderBin);
        }
        return  resultObl;
    }

    public List<cInventoryorderBin> pGetBinsTotalFromDatabasObl() {

        List<cInventoryorderBin> resultObl = new ArrayList<>();
        List<cInventoryorderBinEntity> hulpResultObl;

        hulpResultObl =  getInventoryorderViewModel().pGetBinsTotalFromDatabaseObl();
        if (hulpResultObl == null || hulpResultObl.size() == 0) {
            return  resultObl;
        }

        for (cInventoryorderBinEntity inventoryorderBinEntity : hulpResultObl ) {
            cInventoryorderBin inventoryorderBin = new cInventoryorderBin(inventoryorderBinEntity);
            resultObl.add(inventoryorderBin);
        }
        return  resultObl;
    }

    public cInventoryorderBin pGetBin(String pvBincodeStr) {

        List<cInventoryorderBin> hulpObl;

        hulpObl = this.pGetBinsTotalFromDatabasObl();

        if (hulpObl == null || hulpObl.size() == 0) {
            return null;
        }

        for (cInventoryorderBin inventoryorderBin : hulpObl) {
            if (inventoryorderBin.getBinCodeStr().equalsIgnoreCase(pvBincodeStr)) {
                return  inventoryorderBin;
            }
        }

        return  null;
    }

    public cInventoryorderBin pAddInventoryBin(cBranchBin pvBranchBin) {

        cWebresult WebResult;
        WebResult =  getInventoryorderViewModel().pAddBinViaWebserviceWrs(pvBranchBin.getBinCodeStr());
        if (WebResult.getResultBln() && WebResult.getSuccessBln()){

            if (WebResult.getResultDtt().size() == 1) {
                cInventoryorderBin InventoryorderBin = new cInventoryorderBin(WebResult.getResultDtt().get(0));
                InventoryorderBin.pInsertInDatabaseBln();
                return  InventoryorderBin;
            }
        }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETINVENTORYORDERLINEBARCODES);
            return  null;
        }

        return  null;
    }

    //End Region BINS

    //Region Barcodes

    public boolean pGetBarcodesViaWebserviceBln(Boolean pvRefreshBln) {

        if (pvRefreshBln) {
            cInventoryorderBarcode.allInventoryorderBarcodesObl = null;
            cInventoryorderBarcode.pTruncateTableBln();
        }

        cWebresult WebResult;

        WebResult =  getInventoryorderViewModel().pGetBarcodesFromWebserviceWrs();
        if (WebResult.getResultBln() && WebResult.getSuccessBln() ){

            if (cInventoryorderBarcode.allInventoryorderBarcodesObl == null) {
                cInventoryorderBarcode.allInventoryorderBarcodesObl = new ArrayList<>();
            }


            List<cInventoryorderBarcodeEntity> inventoryorderEntities = new ArrayList<>();

            for (JSONObject jsonObject :WebResult.getResultDtt() ) {
                cInventoryorderBarcode inventoryorderBarcode = new cInventoryorderBarcode(jsonObject);
                inventoryorderEntities.add(inventoryorderBarcode.inventoryorderBarcodeEntity);
                cInventoryorderBarcode.allInventoryorderBarcodesObl.add(inventoryorderBarcode);
            }

            cInventoryorderBarcode.pInsertAllInDatabase(inventoryorderEntities);
            return true;
        }

        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETINVENTORYORDERBARCODES);
            return  false;
        }
    }

    public boolean pAddUnkownBarcodeBln(cBarcodeScan pvBarcodeScan) {

        cInventoryorder.currentInventoryOrder.unknownVariantCounterInt += 1;

        cWebresult WebResult;

        WebResult =  getInventoryorderViewModel().pAddUnknownItemViaWebserviceWrs(pvBarcodeScan);
        if (WebResult.getResultBln()&& WebResult.getSuccessBln() ){

            if (WebResult.getResultDtt().size() == 1) {
                cInventoryorderBarcode inventoryorderBarcode = new cInventoryorderBarcode(WebResult.getResultDtt().get(0));
                inventoryorderBarcode.pInsertInDatabaseBln();
                cInventoryorderBarcode.currentInventoryOrderBarcode = inventoryorderBarcode;
                return  true;
            }
        }
        else {
            cInventoryorder.currentInventoryOrder.unknownVariantCounterInt -= 1;
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_RECEIVEITEMVARIANTCREATE);
            return  false;
        }

        return  true;
    }

    public boolean pAddERPBarcodeBln(cBarcodeScan pvBarcodeScan) {


        //Get article info via the web service
        cArticle.currentArticle  = cArticle.pGetArticleByBarcodeViaWebservice(pvBarcodeScan);

        //We failed to get the article
        if (cArticle.currentArticle == null) {
            return false;
        }

        //Get barcodes for this article
        if (!cArticle.currentArticle.pGetBarcodesViaWebserviceBln(pvBarcodeScan)) {
            return false;
        }

        //Search for the scanned barcodeStr in the article barcodes
        cArticleBarcode matchedArticleBarcode = null;
        for (cArticleBarcode articleBarcode : cArticle.currentArticle.barcodesObl) {
            if (articleBarcode.getBarcodeStr().equalsIgnoreCase(pvBarcodeScan.getBarcodeOriginalStr()) ||
                    articleBarcode.getBarcodeWithoutCheckDigitStr().equalsIgnoreCase(pvBarcodeScan.getBarcodeFormattedStr())) {
                matchedArticleBarcode = articleBarcode;
                break;
            }
        }

        //We didn't find a match, so no use in adding the line
        if (matchedArticleBarcode == null) {
            return  false;
        }


        cWebresult WebResult;
        WebResult =  getInventoryorderViewModel().pAddERPItemViaWebserviceWrs(matchedArticleBarcode);
        if (WebResult.getResultBln() && WebResult.getSuccessBln()){
            for (cArticleBarcode articleBarcode :  cArticle.currentArticle.barcodesObl) {
                cInventoryorderBarcode inventoryorderBarcode = new cInventoryorderBarcode(articleBarcode);
                inventoryorderBarcode.pInsertInDatabaseBln();

                if (inventoryorderBarcode.getBarcodeStr().equalsIgnoreCase(pvBarcodeScan.getBarcodeOriginalStr()) ||
                        inventoryorderBarcode.getBarcodeStr().equalsIgnoreCase(pvBarcodeScan.getBarcodeFormattedStr())) {
                    cInventoryorderBarcode.currentInventoryOrderBarcode = inventoryorderBarcode;
                }
            }
        }
        else {
            cInventoryorder.currentInventoryOrder.unknownVariantCounterInt -= 1;
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_INVENTORYBARCODECREATE);
            return  false;
        }

        return  true;
    }

    //End Region Barcodes

    //Region Line Barcode

    private boolean mGetLineBarcodesViaWebserviceBln() {
            cInventoryorderLineBarcode.allLineBarcodesObl = null;
            cInventoryorderLineBarcode.pTruncateTableBln();

        cWebresult WebResult =  getInventoryorderViewModel().pGetLineBarcodesFromWebserviceWrs();
        if (WebResult.getResultBln() && WebResult.getSuccessBln()){

            List<cInventoryorderLineBarcodeEntity> insertObl = new ArrayList<>();


            for (JSONObject jsonObject : WebResult.getResultDtt()) {
                cInventoryorderLineBarcode inventoryorderLineBarcode = new cInventoryorderLineBarcode(jsonObject);
                insertObl.add((inventoryorderLineBarcode.inventoryorderLineBarcodeEntity));

                //Search for line that belongs to this barcodeStr
                cInventoryorderLine inventoryorderLine = cInventoryorder.currentInventoryOrder.mGetLineWithLineNo(inventoryorderLineBarcode.getLineNoLng());

                //If we can't find it, skip this
                if (inventoryorderLine == null) {
                    continue;
                }

                //Add barcodeStr to line
                inventoryorderLine.pAddLineBarcode(inventoryorderLineBarcode.getBarcodeStr(),inventoryorderLineBarcode.getQuantityhandledDbl(), false);

            }

            cInventoryorderLineBarcode.pInsertAllInDatabase(insertObl);

        }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETINVENTORYORDERLINEBARCODES);
            return  false;
        }

        return  true;
    }

    //End Region Line Barcode

    //Region Barcode

    public cInventoryorderBarcode pGetOrderBarcode(cBarcodeScan pvBarcodescan) {

        if (this.barcodesObl() == null || this.barcodesObl().size() == 0)  {
            return  null;
        }

        for (cInventoryorderBarcode inventoryorderBarcode : this.barcodesObl()) {

            if (inventoryorderBarcode.getBarcodeStr().equalsIgnoreCase(pvBarcodescan.getBarcodeOriginalStr()) ||
                    inventoryorderBarcode.getBarcodeWithoutCheckDigitStr().equalsIgnoreCase(pvBarcodescan.getBarcodeFormattedStr())) {
                return  inventoryorderBarcode;
            }
        }

        return  null;

    }

    //End Region Barcode

    //Region Images

    private boolean mGetArticleImagesViaWebserviceBln() {

        if (!cInventoryorder.currentInventoryOrder.isInventoryWithPictureBln()  || !cInventoryorder.currentInventoryOrder.isInventoryWithPicturePrefetchBln()) {
            return  true;
        }


            cArticleImage.allImages = null;
            cArticleImage.pTruncateTableBln();


        if (this.imagesObl()  != null) {
            return  true;
        }

        if (this.linesObl() == null || this.linesObl().size() == 0) {
            return  false;
        }

        List<String> itemNoAndVariantCodeObl;
        itemNoAndVariantCodeObl = new ArrayList<>();

        for (cInventoryorderLine inventoryorderLine : this.linesObl()) {
            String itemNoAndVariantCodeStr = inventoryorderLine.getItemNoStr() + ";" + inventoryorderLine.getVariantCodeStr();

            if (!itemNoAndVariantCodeObl.contains(itemNoAndVariantCodeStr)) {
                itemNoAndVariantCodeObl.add(itemNoAndVariantCodeStr);
            }
        }

        cWebresult WebResult;
        cArticleImageViewModel articleImageViewModel = new ViewModelProvider(cAppExtension.fragmentActivity).get(cArticleImageViewModel.class);
        WebResult =  articleImageViewModel.pGetArticleImagesFromWebserviceWrs(itemNoAndVariantCodeObl);
        if (WebResult.getResultBln()&& WebResult.getSuccessBln()){

            cArticleImage.allImages = new ArrayList<>();
            List<JSONObject> myList = WebResult.getResultDtt();

            for (int i = 0; i < myList.size(); i++) {
                JSONObject jsonObject;
                jsonObject = myList.get(i);

                cArticleImage articleImage = new cArticleImage(jsonObject);

                if ( !cArticleImage.allImages.contains(articleImage)) {
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

    //End Region Images

    //Region Comments

    public boolean pGetCommentsViaWebserviceBln(Boolean pvRefeshBln) {

        if (pvRefeshBln) {
            cComment.allCommentsObl = null;
            cComment.pTruncateTableBln();
            cComment.commentsShownBln = false;
        }

        cWebresult webresult;
        webresult = getInventoryorderViewModel().pGetCommentsFromWebserviceWrs();
        if (webresult.getResultBln()&& webresult.getSuccessBln()) {

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
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETINVENTORYORDERCOMMENTS);
            return false;
        }
    }

    public List<cComment> pFeedbackCommentObl(){

        if (cInventoryorder.currentInventoryOrder.commentsObl() == null || cInventoryorder.currentInventoryOrder.commentsObl().size() == 0) {
            return  null;
        }

        return cComment.pGetCommentsForTypeObl(cWarehouseorder.CommentTypeEnu.FEEDBACK);

    }

    private static  void mInsertAllInDatabase(List<cInventoryorderEntity> pvInventoryOrderEntityObl ) {
        cInventoryorderViewModel inventoryorderViewModel = new ViewModelProvider(cAppExtension.fragmentActivity).get(cInventoryorderViewModel.class);
        inventoryorderViewModel.insertAll (pvInventoryOrderEntityObl);
    }

    private void mGetCommentsViaWebError(List<JSONObject> pvResultDtt) {

        cComment.allCommentsObl = new ArrayList<>();
        for (JSONObject jsonObject : pvResultDtt) {
            cComment comment = new cComment(jsonObject);
            comment.pInsertInDatabaseBln();
        }


    }
    private boolean mGetLinePropertysViaWebserviceBln() {

        cLineProperty.allLinePropertysObl = null;
        cLineProperty.pTruncateTableBln();

        cLinePropertyValue.allLinePropertysValuesObl = null;
        cLinePropertyValue.pTruncateTableBln();

        cWebresult WebResult;
        WebResult =  this.getInventoryorderViewModel().pGetLinePropertysViaWebserviceWrs();
        if (WebResult.getResultBln() && WebResult.getSuccessBln()){

            for (JSONObject jsonObject : WebResult.getResultDtt()) {
                cLineProperty pickorderLineProperty = new cLineProperty(jsonObject);
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



        cWebresult WebResult;
        WebResult =  this.getInventoryorderViewModel().pGetLinePropertyValuesViaWebserviceWrs();
        if (WebResult.getResultBln() && WebResult.getSuccessBln()){

            for (JSONObject jsonObject : WebResult.getResultDtt()) {
                cLinePropertyValue linePropertyValue = new cLinePropertyValue(jsonObject);
                linePropertyValue.pInsertInDatabaseBln();
                if (linePropertyValue.getValueStr() !=null){
                    for (cInventoryorderLine inventoryorderLine : this.linesObl()){
                        if (inventoryorderLine.getLineNoInt() == linePropertyValue.getLineNoInt()){
                            if (inventoryorderLine.presetValueObl == null) {
                                inventoryorderLine.presetValueObl = new ArrayList<>();
                            }
                            inventoryorderLine.presetValueObl.add(linePropertyValue);
                        }
                    }
                }
            }
            return  true;
        }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_WAREHOUSEOPDRACHTLINEITEMPROPERTIEVALUESGET);
            return  false;
        }
    }

    //End Region Comments

}
