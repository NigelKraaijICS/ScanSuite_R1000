package SSU_WHS.Inventory.InventoryOrders;

import androidx.lifecycle.ViewModelProviders;

import org.json.JSONObject;

import java.util.ArrayList;
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
import SSU_WHS.Basics.BranchBin.cBranchBin;
import SSU_WHS.Basics.Users.cUser;
import SSU_WHS.General.Comments.cComment;
import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.Inventory.InventoryorderBarcodes.cInventoryorderBarcode;
import SSU_WHS.Inventory.InventoryorderBarcodes.cInventoryorderBarcodeEntity;
import SSU_WHS.Inventory.InventoryorderBins.cInventoryorderBin;
import SSU_WHS.Inventory.InventoryorderBins.cInventoryorderBinEntity;
import SSU_WHS.Inventory.InventoryorderLineBarcodes.cInventoryorderLineBarcode;
import SSU_WHS.Inventory.InventoryorderLineBarcodes.cInventoryorderLineBarcodeEntity;
import SSU_WHS.Inventory.InventoryorderLines.cInventoryorderLine;
import SSU_WHS.Inventory.InventoryorderLines.cInventoryorderLineEntity;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebserviceDefinitions;
import nl.icsvertex.scansuite.R;


public class cInventoryorder {

    private String orderNumberStr;
    public String getOrderNumberStr() {
        return orderNumberStr;
    }

    private String orderTypeStr;
    public String getOrderTypeStr() {
        return orderTypeStr;
    }

    private int numberOfBinsInt;
    public int getNumberOfBinsInt() {
        return numberOfBinsInt;
    }

    private String assignedUserIdStr;
    public String getAssignedUserIdStr() {
        return assignedUserIdStr;
    }

    private String currentUserIdStr;
    public String getCurrentUserIdStr() {
        return currentUserIdStr;
    }

    private int statusInt;
    public int getStatusInt() {
        return statusInt;
    }

    private boolean invAmountManualBln;
    public boolean isInvAmountManualBln() {
        return invAmountManualBln;
    }

    private boolean invBarcodeCheckBln;
    public boolean isInvBarcodeCheckBln() {
        return invBarcodeCheckBln;
    }

    private boolean invAddExtraBinBln;
    public boolean isInvAddExtraBinBln() {
        return invAddExtraBinBln;
    }

    private String externalReferenceStr;
    public String getExternalReferenceStr() {
        return externalReferenceStr;
    }

    private int sourceDocumentInt;
    private int getSourceDocumentInt() { return sourceDocumentInt; }

    private String documentStr;
    public String getDocumentStr() {
        return documentStr;
    }

    private boolean inventoryWithPictureBln;
    public boolean isInventoryWithPictureBln() {
        return inventoryWithPictureBln;
    }

    private boolean inventoryWithPictureAutoOpenBln;
    public boolean isInventoryWithPictureAutoOpenBln() {
        return inventoryWithPictureAutoOpenBln;
    }

    private boolean inventoryWithPicturePrefetchBln;
    public boolean isInventoryWithPicturePrefetchBln() {
        return inventoryWithPicturePrefetchBln;
    }

    public Boolean isGeneratedBln() {
        return this.getSourceDocumentInt() == cWarehouseorder.SoureDocumentTypeEnu.Generated;
    }

    public int unknownVariantCounterInt = 0;
    public int getUnknownVariantCounterInt() {
        return unknownVariantCounterInt;
    }

    private cInventoryorderEntity inventoryorderEntity;
    public boolean indatabaseBln;

    private static cInventoryorderViewModel gInventoryorderViewModel;
    public static cInventoryorderViewModel getInventoryorderViewModel() {
        if (gInventoryorderViewModel == null) {
            gInventoryorderViewModel = ViewModelProviders.of(cAppExtension.fragmentActivity).get(cInventoryorderViewModel.class);
        }
        return gInventoryorderViewModel;
    }

    private static cInventoryorderAdapter gInventoryorderAdapter;
    public static cInventoryorderAdapter getInventoryorderAdapter() {
        if (gInventoryorderAdapter == null) {
            gInventoryorderAdapter = new cInventoryorderAdapter();
        }
        return gInventoryorderAdapter;
    }

    public List<cComment> commentsObl() {
        return cComment.allCommentsObl;
    }
    public List<cInventoryorderBarcode> barcodesObl () {return  cInventoryorderBarcode.allInventoryorderBarcodesObl;}
    private List<cInventoryorderLine> linesObl() {return  cInventoryorderLine.allLinesObl;}
    private List<cArticleImage> imagesObl()  {
        return  cArticleImage.allImages;
    }

    private static List<cInventoryorder> allCachedOrdersObl;
    public static List<cInventoryorder> allInventoryOrdersObl(Boolean pvRefreshBln ){

        if (pvRefreshBln) {
             cInventoryorder.allCachedOrdersObl = null;
        }

        if (cInventoryorder.allCachedOrdersObl != null) {
            return  cInventoryorder.allCachedOrdersObl;
        }

        cInventoryorder.allCachedOrdersObl  = new ArrayList<>();
        List<cInventoryorderEntity> hulpObl  =  cInventoryorder.getInventoryorderViewModel().pGetInventoryordersFromDatabaseObl();


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
        this.statusInt = cText.pStringToIntegerInt(this.inventoryorderEntity.getStatusStr());

        this.invAmountManualBln = cText.pStringToBooleanBln(this.inventoryorderEntity.getInvAmountManualStr(), false);
        this.invBarcodeCheckBln = cText.pStringToBooleanBln(this.inventoryorderEntity.getInvBarcodeCheckStr(), false);
        this.invAddExtraBinBln = cText.pStringToBooleanBln(this.inventoryorderEntity.getInvAddExtraBinStr(), false);

        this.externalReferenceStr = this.inventoryorderEntity.getExternalReferenceStr();

        this.sourceDocumentInt = cText.pStringToIntegerInt(this.inventoryorderEntity.getSourceDocumentStr()) ;
        this.documentStr = this.inventoryorderEntity.getDocumentStr();

        this.inventoryWithPictureBln = cText.pStringToBooleanBln(this.inventoryorderEntity.getInventoryWithPictureStr(),false) ;
        this.inventoryWithPictureAutoOpenBln = cText.pStringToBooleanBln(this.inventoryorderEntity.getInventoryWithPictureAutoOpenStr(),false) ;
        this.inventoryWithPicturePrefetchBln = cText.pStringToBooleanBln(this.inventoryorderEntity.getInventoryWithPicturePrefetchStr(),false) ;
    }

    public cInventoryorder(cInventoryorderEntity pvInventoryorderEntity) {

        this.inventoryorderEntity = pvInventoryorderEntity;
        this.orderNumberStr = this.inventoryorderEntity.getOrdernumberStr();
        this.orderTypeStr = this.inventoryorderEntity.getOrderTypeStr();
        this.numberOfBinsInt = cText.pStringToIntegerInt(this.inventoryorderEntity.getNumberofBinsStr());
        this.assignedUserIdStr = this.inventoryorderEntity.getAssignedUserIdStr();
        this.currentUserIdStr = this.inventoryorderEntity.getCurrentUserIdStr();
        this.statusInt = cText.pStringToIntegerInt(this.inventoryorderEntity.getStatusStr());

        this.invAmountManualBln = cText.pStringToBooleanBln(this.inventoryorderEntity.getInvAmountManualStr(), false);
        this.invBarcodeCheckBln = cText.pStringToBooleanBln(this.inventoryorderEntity.getInvBarcodeCheckStr(), false);
        this.invAddExtraBinBln = cText.pStringToBooleanBln(this.inventoryorderEntity.getInvAddExtraBinStr(), false);

        this.externalReferenceStr = this.inventoryorderEntity.getExternalReferenceStr();

        this.sourceDocumentInt =  cText.pStringToIntegerInt(this.inventoryorderEntity.getSourceDocumentStr());
        this.documentStr = this.inventoryorderEntity.getDocumentStr();

        this.inventoryWithPictureBln = cText.pStringToBooleanBln(this.inventoryorderEntity.getInventoryWithPictureStr(),false) ;
        this.inventoryWithPictureAutoOpenBln = cText.pStringToBooleanBln(this.inventoryorderEntity.getInventoryWithPictureAutoOpenStr(),false) ;
        this.inventoryWithPicturePrefetchBln = cText.pStringToBooleanBln(this.inventoryorderEntity.getInventoryWithPicturePrefetchStr(),false) ;

    }

    //End Region Constructor

    //Region Public Methods

    //Region Orders

    public boolean pInsertInDatabaseBln() {
        cInventoryorder.getInventoryorderViewModel().insert(this.inventoryorderEntity);
        this.indatabaseBln = true;

        return true;
    }

    public static Boolean pCreateInventoryOrderViaWebserviceBln(String pvDocumentStr) {

        cWebresult WebResult;
        WebResult = cInventoryorder.getInventoryorderViewModel().pCreateInventoryOrderViaWebserviceWrs(pvDocumentStr);
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
        WebResult = cInventoryorder.getInventoryorderViewModel().pGetInventoryordersFromWebserviceWrs(pvSearchTextStr);
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

        if (this.getStatusInt() > 10 && cUser.currentUser.getNameStr().equalsIgnoreCase(this.getCurrentUserIdStr())) {
            ignoreBusyBln = true;
        }

        Webresult = cWarehouseorder.getWarehouseorderViewModel().pLockWarehouseopdrachtViaWebserviceWrs(cWarehouseorder.OrderTypeEnu.INVENTARISATIE.toString(),
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
                Webresult.getResultObl().size() > 0 && ! Webresult.getResultObl().get(0).equalsIgnoreCase(cUser.currentUser.getNameStr())) {
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

        Webresult = cWarehouseorder.getWarehouseorderViewModel().pLockReleaseWarehouseorderViaWebserviceWrs(cWarehouseorder.OrderTypeEnu.INVENTARISATIE.toString(), this.getOrderNumberStr(), cDeviceInfo.getSerialnumberStr(), pvStepCodeEnu.toString(), pvWorkFlowStepInt);

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

        webresult =  cInventoryorder.getInventoryorderViewModel().pHandledViaWebserviceWrs();

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

        hulpResultObl =  cInventoryorder.getInventoryorderViewModel().pGetInventoriesWithFilterFromDatabaseObl(cUser.currentUser.getNameStr(), cSharedPreferences.userFilterBln());
        if (hulpResultObl == null || hulpResultObl.size() == 0) {
            return  resultObl;
        }

        for (cInventoryorderEntity inventoryorderEntity : hulpResultObl ) {
            cInventoryorder inventoryorder = new cInventoryorder(inventoryorderEntity);
            resultObl.add(inventoryorder);
        }

        return  resultObl;
    }

    private static void mTruncateTable() {
        cInventoryorder.getInventoryorderViewModel().deleteAll();
    }

    private static  void mInsertAllInDatabase(List<cInventoryorderEntity> pvInventoryOrderEntityObl ) {
        cInventoryorder.getInventoryorderViewModel().insertAll (pvInventoryOrderEntityObl);
    }

    //End Region Orders

    //Region Lines

    public boolean pGetLinesViaWebserviceBln(Boolean pvRefreshBln) {

        if (pvRefreshBln) {
            cInventoryorderLine.allLinesObl = null;
            cInventoryorderLine.pTruncateTableBln();
        }

        cWebresult WebResult;

        WebResult = cInventoryorder.getInventoryorderViewModel().pGetLinesFromWebserviceWrs();
        if (WebResult.getResultBln() && WebResult.getSuccessBln()) {

            if (cInventoryorderLine.allLinesObl == null) {
                cInventoryorderLine.allLinesObl = new ArrayList<>();
            }


            List<cInventoryorderLineEntity> importObl = new ArrayList<>();
            List<cInventoryorderLine> objectObl = new ArrayList<>();

            for (JSONObject jsonObject : WebResult.getResultDtt()) {

                cInventoryorderLine inventoryorderLine = new cInventoryorderLine(jsonObject);
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

                    cInventoryorderBin.currentInventoryOrderBin.pUpdateStatusAndTimeStampInDatabaseBln();
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

        cWebresult WebResult =  cInventoryorder.getInventoryorderViewModel().pAddLineViaWebserviceWrs();
        if (WebResult.getResultBln()&& WebResult.getSuccessBln()){

            if (WebResult.getResultDtt().size() == 1) {
                cInventoryorderLine.currentInventoryOrderLine= new cInventoryorderLine(WebResult.getResultDtt().get(0));
                cInventoryorderLine.currentInventoryOrderLine.pInsertInDatabaseBln();
                cInventoryorderLine.currentInventoryOrderLine.pAddLineBarcode(cInventoryorderBarcode.currentInventoryOrderBarcode.getBarcodeStr(),cInventoryorderBarcode.currentInventoryOrderBarcode.getQuantityPerUnitOfMeasureDbl());
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

        return resultObl;
    }

    public Double pGetTotalItemCountDbl() {

        Double resultDbl;

        resultDbl = cInventoryorderLine.getInventoryorderLineViewModel().pGetTotalCountDbl();
        if (resultDbl == null ) {
            return (double) 0;
        }

        return resultDbl;
    }

    public Double pGetItemCountForBinDbl(String pvBincodeStr) {

        Double resultDbl;

        resultDbl = cInventoryorderLine.getInventoryorderLineViewModel().pGetCountForBinCodeDbl(pvBincodeStr);
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

    public boolean pGetBinsViaWebserviceBln(Boolean pvRefreshBln) {

        if (pvRefreshBln) {
            cInventoryorderBin.allInventoryorderBinsObl = null;
            cInventoryorderBin.pTruncateTableBln();
            cInventoryorder.binsObl = new ArrayList<>();
        }

        cWebresult WebResult;

        WebResult = cInventoryorder.getInventoryorderViewModel().pGetBinsFromWebserviceWrs();
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

    public boolean pGetPossibleBinsViaWebserviceBln() {


        cWebresult WebResult;

        WebResult = cInventoryorder.getInventoryorderViewModel().pGetPossibleBinsFromWebserviceWrs();
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
                inventoryorderBin.pUpdateStatusAndTimeStampInDatabaseBln();

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

        hulpResultObl =  cInventoryorder.getInventoryorderViewModel().pGetBinsDoneFromDatabaseObl();
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

        hulpResultObl =  cInventoryorder.getInventoryorderViewModel().pGetBinsNotDoneFromDatabaseObl();
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

        hulpResultObl =  cInventoryorder.getInventoryorderViewModel().pGetBinsTotalFromDatabaseObl();
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
        WebResult =  cInventoryorder.getInventoryorderViewModel().pAddBinViaWebserviceWrs(pvBranchBin.getBinCodeStr());
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

        WebResult =  cInventoryorder.getInventoryorderViewModel().pGetBarcodesFromWebserviceWrs();
        if (WebResult.getResultBln() && WebResult.getSuccessBln() ){

            if (cInventoryorderBarcode.allInventoryorderBarcodesObl == null) {
                cInventoryorderBarcode.allInventoryorderBarcodesObl = new ArrayList<>();
            }


            List<cInventoryorderBarcodeEntity> inventoryorderEntities = new ArrayList<>();

            for (JSONObject jsonObject :WebResult.getResultDtt() ) {
                cInventoryorderBarcode inventoryorderBarcode = new cInventoryorderBarcode(jsonObject);
                inventoryorderEntities.add(inventoryorderBarcode.inventoryorderBarcodeEntity);
            }

            cInventoryorderBarcode.pInsertAllInDatabaseBln(inventoryorderEntities);
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

        WebResult =  cInventoryorder.getInventoryorderViewModel().pAddUnknownItemViaWebserviceWrs(pvBarcodeScan);
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
        if (!cArticle.currentArticle.pGetBarcodesViaWebserviceBln()) {
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
        WebResult =  cInventoryorder.getInventoryorderViewModel().pAddERPItemViaWebserviceWrs(matchedArticleBarcode);
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

    public boolean pGetLineBarcodesViaWebserviceBln(Boolean pvRefreshBln) {

        if (pvRefreshBln) {
            cInventoryorderLineBarcode.allLineBarcodesObl = null;
            cInventoryorderLineBarcode.pTruncateTableBln();
        }

        cWebresult WebResult =  cInventoryorder.getInventoryorderViewModel().pGetLineBarcodesFromWebserviceWrs();
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
                inventoryorderLine.pAddLineBarcode(inventoryorderLineBarcode.getBarcodeStr(),inventoryorderLineBarcode.getQuantityhandledDbl());

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

    public boolean pGetArticleImagesViaWebserviceBln(Boolean pvRefreshBln) {

        if (!cInventoryorder.currentInventoryOrder.isInventoryWithPictureBln()  || !cInventoryorder.currentInventoryOrder.inventoryWithPicturePrefetchBln) {
            return  true;
        }

        if (pvRefreshBln) {
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

        for (cInventoryorderLine inventoryorderLine : this.linesObl()) {
            String itemNoAndVariantCodeStr = inventoryorderLine.getItemNoStr() + ";" + inventoryorderLine.getVariantCodeStr();

            if (!itemNoAndVariantCodeObl.contains(itemNoAndVariantCodeStr)) {
                itemNoAndVariantCodeObl.add(itemNoAndVariantCodeStr);
            }
        }

        cWebresult WebResult;
        WebResult =  cArticleImage.getArticleImageViewModel().pGetArticleImagesFromWebserviceWrs(itemNoAndVariantCodeObl);
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
        webresult = cInventoryorder.getInventoryorderViewModel().pGetCommentsFromWebserviceWrs();
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

    private void mGetCommentsViaWebError(List<JSONObject> pvResultDtt) {

        cComment.allCommentsObl = new ArrayList<>();
        for (JSONObject jsonObject : pvResultDtt) {
            cComment comment = new cComment(jsonObject);
            comment.pInsertInDatabaseBln();
        }


    }

    //End Region Comments

}
