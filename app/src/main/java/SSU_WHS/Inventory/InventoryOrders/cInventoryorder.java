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
import SSU_WHS.Basics.BranchBin.cBranchBin;
import SSU_WHS.Basics.Settings.cSetting;
import SSU_WHS.Basics.Users.cUser;
import SSU_WHS.General.Comments.cComment;
import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.Inventory.InventoryorderBarcodes.cInventoryorderBarcode;
import SSU_WHS.Inventory.InventoryorderBins.cInventoryorderBin;
import SSU_WHS.Inventory.InventoryorderBins.cInventoryorderBinEntity;
import SSU_WHS.Inventory.InventoryorderLineBarcodes.cInventoryorderLineBarcode;
import SSU_WHS.Inventory.InventoryorderLines.cInventoryorderLine;
import SSU_WHS.Inventory.InventoryorderLines.cInventoryorderLineEntity;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebserviceDefinitions;
import nl.icsvertex.scansuite.R;


public class cInventoryorder {


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

    public boolean invAutoCloseBinBln;
    public boolean isInvAutoCloseBinBln() {
        return invAutoCloseBinBln;
    }

    public boolean invPrecountBln;
    public boolean isInvPrecountBln() {
        return invPrecountBln;
    }

    public boolean invAmountManualBln;
    public boolean isInvAmountManualBln() {
        return invAmountManualBln;
    }

    public boolean invBarcodeCheckBln;
    public boolean isInvBarcodeCheckBln() {
        return invBarcodeCheckBln;
    }

    public boolean invAddExtraBinBln;
    public boolean isInvAddExtraBinBln() {
        return invAddExtraBinBln;
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

    public int sourceDocumentInt;
    public int getSourceDocumentInt() { return sourceDocumentInt; }

    public String documentStr;
    public String getDocumentStr() {
        return documentStr;
    }

    public String document2Str;
    public String getDocument2Str() {
        return document2Str;
    }

    public int webserviceTimeOutERPInsInt;
    public int getWebserviceTimeOutERPInsInt() {
        return webserviceTimeOutERPInsInt;
    }

    public String interfaceResultMethodStr;
    public String getInterfaceResultMethodStr() {
        return interfaceResultMethodStr;
    }

    public List<String> errorMessagesObl;
    public List<String> getErrorMessageObl() {
        return errorMessagesObl;
    }

    public Boolean isIVSBln() {

        if (this.getOrderTypeStr().equalsIgnoreCase(cWarehouseorder.WorkflowEnu.IVS.toString())) {
            return true;
        }

        return false;

    }

    public Boolean isGeneratedBln() {

        if (this.getSourceDocumentInt() == cWarehouseorder.SoureDocumentTypeEnu.Generated) {
            return  true;
        }

        return false;

    }

    public int unknownVariantCounterInt = 0;
    public int getUnknownVariantCounterInt() {
        return unknownVariantCounterInt;
    }


    public cInventoryorderEntity inventoryorderEntity;
    public boolean indatabaseBln;

    public static cInventoryorderViewModel gInventoryorderViewModel;

    public static cInventoryorderViewModel getInventoryorderViewModel() {
        if (gInventoryorderViewModel == null) {
            gInventoryorderViewModel = ViewModelProviders.of(cAppExtension.fragmentActivity).get(cInventoryorderViewModel.class);
        }
        return gInventoryorderViewModel;
    }

    public static cInventoryorderAdapter gInventoryorderAdapter;

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

    public List<cInventoryorderLine> linesObl () {return  cInventoryorderLine.allLinesObl;}

    public List<cInventoryorderBin> binsObl () {return  cInventoryorderBin.allInventoryorderBinsObl;}

    public static List<cInventoryorder> allInventoryordersObl;
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

        this.invAutoCloseBinBln = cText.pStringToBooleanBln(this.inventoryorderEntity.getInvAutoCloseBinStr(), false);
        this.invPrecountBln = cText.pStringToBooleanBln(this.inventoryorderEntity.getInvPrecountStr(), false);
        this.invAmountManualBln = cText.pStringToBooleanBln(this.inventoryorderEntity.getInvAmountManualStr(), false);
        this.invBarcodeCheckBln = cText.pStringToBooleanBln(this.inventoryorderEntity.getInvBarcodeCheckStr(), false);
        this.invAddExtraBinBln = cText.pStringToBooleanBln(this.inventoryorderEntity.getInvAddExtraBinStr(), false);

        this.externalReferenceStr = this.inventoryorderEntity.getExternalReferenceStr();
        this.workPlaceStr = this.inventoryorderEntity.getWorkplaceStr();
        this.stockOwnerStr = this.inventoryorderEntity.getStockOwnerStr();
        this.sourceDocumentInt = cText.pStringToIntegerInt(this.inventoryorderEntity.getSourceDocumentStr()) ;
        this.documentStr = this.inventoryorderEntity.getDocumentStr();
        this.document2Str = this.inventoryorderEntity.getDocument2Str();
        this.webserviceTimeOutERPInsInt = cText.pStringToIntegerInt(this.inventoryorderEntity.getWebserviceTimeOutERPInsStr());
        this.interfaceResultMethodStr = this.inventoryorderEntity.getInterfaceResultMethodStr();
    }

    public cInventoryorder(cInventoryorderEntity pvInventoryorderEntity) {

        this.inventoryorderEntity = pvInventoryorderEntity;
        this.orderNumberStr = this.inventoryorderEntity.getOrdernumberStr();
        this.orderTypeStr = this.inventoryorderEntity.getOrderTypeStr();
        this.numberOfBinsInt = cText.pStringToIntegerInt(this.inventoryorderEntity.getNumberofBinsStr());
        this.assignedUserIdStr = this.inventoryorderEntity.getAssignedUserIdStr();
        this.currentUserIdStr = this.inventoryorderEntity.getCurrentUserIdStr();
        this.statusInt = cText.pStringToIntegerInt(this.inventoryorderEntity.getStatusStr());

        this.invAutoCloseBinBln = cText.pStringToBooleanBln(this.inventoryorderEntity.getInvAutoCloseBinStr(), false);
        this.invPrecountBln = cText.pStringToBooleanBln(this.inventoryorderEntity.getInvPrecountStr(), false);
        this.invAmountManualBln = cText.pStringToBooleanBln(this.inventoryorderEntity.getInvAmountManualStr(), false);
        this.invBarcodeCheckBln = cText.pStringToBooleanBln(this.inventoryorderEntity.getInvBarcodeCheckStr(), false);
        this.invAddExtraBinBln = cText.pStringToBooleanBln(this.inventoryorderEntity.getInvAddExtraBinStr(), false);

        this.externalReferenceStr = this.inventoryorderEntity.getExternalReferenceStr();
        this.workPlaceStr = this.inventoryorderEntity.getWorkplaceStr();
        this.stockOwnerStr = this.inventoryorderEntity.getStockOwnerStr();
        this.sourceDocumentInt =  cText.pStringToIntegerInt(this.inventoryorderEntity.getSourceDocumentStr());
        this.documentStr = this.inventoryorderEntity.getDocumentStr();
        this.document2Str = this.inventoryorderEntity.getDocument2Str();
        this.webserviceTimeOutERPInsInt = cText.pStringToIntegerInt(this.inventoryorderEntity.getWebserviceTimeOutERPInsStr());
        this.interfaceResultMethodStr = this.inventoryorderEntity.getInterfaceResultMethodStr();
    }

    //End Region Constructor

    //Region Public Methods

    public boolean pInsertInDatabaseBln() {
        cInventoryorder.getInventoryorderViewModel().insert(this.inventoryorderEntity);
        this.indatabaseBln = true;

        if (cInventoryorder.allInventoryordersObl == null) {
            cInventoryorder.allInventoryordersObl = new ArrayList<>();
        }
        cInventoryorder.allInventoryordersObl.add(this);
        return true;
    }

    public static Boolean pCreateInventoryOrderViaWebserviceBln(String pvDocumentStr) {

        cWebresult WebResult;
        WebResult = cInventoryorder.getInventoryorderViewModel().pCreateInventoryOrderViaWebserviceWrs(pvDocumentStr);
        if (WebResult.getResultBln() == true && WebResult.getSuccessBln() == true) {
            List<JSONObject> myList = WebResult.getResultDtt();
            for (int i = 0; i < myList.size(); i++) {
                JSONObject jsonObject;
                jsonObject = myList.get(i);

                cInventoryorder inventoryorder = new cInventoryorder(jsonObject);
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

        if (pvRefreshBln == true) {
            cInventoryorder.allInventoryordersObl = null;
            cInventoryorder.mTruncateTableBln();
        }

        cWebresult WebResult;
        WebResult = cInventoryorder.getInventoryorderViewModel().pGetInventoryordersFromWebserviceWrs(pvSearchTextStr);
        if (WebResult.getResultBln() == true && WebResult.getSuccessBln() == true) {

            List<JSONObject> myList = WebResult.getResultDtt();
            for (int i = 0; i < myList.size(); i++) {
                JSONObject jsonObject;
                jsonObject = myList.get(i);

                cInventoryorder inventoryorder = new cInventoryorder(jsonObject);
                inventoryorder.pInsertInDatabaseBln();
            }
            return true;
        } else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETINVENTORYORDERS);
            return false;
        }
    }

    public static List<cInventoryorder> pGetInventoriesFromDatabasObl() {

        List<cInventoryorder> resultObl = new ArrayList<>();
        List<cInventoryorderEntity> hulpResultObl;

        hulpResultObl = cInventoryorder.getInventoryorderViewModel().pGetInventoryordersFromDatabaseObl();
        if (hulpResultObl == null || hulpResultObl.size() == 0) {
            return resultObl;
        }

        for (cInventoryorderEntity inventoryorderEntity : hulpResultObl) {
            cInventoryorder inventoryorder = new cInventoryorder(inventoryorderEntity);
            resultObl.add(inventoryorder);
        }

        return resultObl;
    }

    public cResult pLockViaWebserviceRst(cWarehouseorder.StepCodeEnu pvStepCodeEnu, int pvWorkFlowStepInt) {

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

        Webresult = cWarehouseorder.getWarehouseorderViewModel().pLockWarehouseopdrachtViaWebserviceWrs(cWarehouseorder.OrderTypeEnu.INVENTARISATIE.toString(), this.getOrderNumberStr(), cDeviceInfo.getSerialnumberStr(), pvStepCodeEnu.toString(), pvWorkFlowStepInt, ignoreBusyBln);

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
        if (Webresult.getSuccessBln() == false) {
            result.resultBln = false;
            result.activityActionEnu = cWarehouseorder.ActivityActionEnu.Unknown;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_couldnt_lock_order));
            return result;
        }

        //Check if this activity is meant for a different user
        if (Webresult.getResultBln() == false && Webresult.getResultLng() <= 0 && Webresult.getResultObl() != null &&
                Webresult.getResultObl().size() >= 2 && Webresult.getResultObl().get(0).equalsIgnoreCase("invalid_user_not_assigned") == true) {
            result.resultBln = false;
            result.activityActionEnu = cWarehouseorder.ActivityActionEnu.Unknown;
            result.pAddErrorMessage(cAppExtension.context.getString((R.string.message_meant_for_other_user)) + " " + Webresult.getResultObl().get(1));
            return result;
        }

        //Check if this activity is meant for a different user
        if (Webresult.getResultBln() == false && Webresult.getResultLng() <= 0 && Webresult.getResultObl() != null &&
                Webresult.getResultObl().size() >= 0 && Webresult.getResultObl().get(0).equalsIgnoreCase(cUser.currentUser.getNameStr()) == false) {
            result.resultBln = false;
            result.activityActionEnu = cWarehouseorder.ActivityActionEnu.Unknown;
            result.pAddErrorMessage(cAppExtension.context.getString((R.string.message_another_user_already_started)) + " " + Webresult.getResultObl().get(0));
            return result;
        }

        // We got a succesfull response, but we need to do something with this activity
        if (Webresult.getResultBln() == false && Webresult.getResultLng() > 0) {

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

            cInventoryorder.currentInventoryOrder.pGetCommentsViaWebErrorBln(Webresult.getResultDtt());
            return result;
        }

        return result;

    }

    public boolean pLockReleaseViaWebserviceBln(cWarehouseorder.StepCodeEnu pvStepCodeEnu, int pvWorkFlowStepInt) {

        cWebresult Webresult;

        Webresult = cWarehouseorder.getWarehouseorderViewModel().pLockReleaseWarehouseorderViaWebserviceWrs(cWarehouseorder.OrderTypeEnu.INVENTARISATIE.toString(), this.getOrderNumberStr(), cDeviceInfo.getSerialnumberStr(), pvStepCodeEnu.toString(), pvWorkFlowStepInt);

        if (Webresult.getSuccessBln() == false || Webresult.getResultBln() == false) {
            return false;
        }
        return true;
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

    public boolean pDeleteDetailsBln() {

        cInventoryorderLine.pTruncateTableBln();
        cInventoryorderBin.pTruncateTableBln();
        cInventoryorderBarcode.pTruncateTableBln();
        cInventoryorderLineBarcode.pTruncateTableBln();
        return true;
    }

    public boolean pGetLinesViaWebserviceBln(Boolean pvRefreshBln) {

        if (pvRefreshBln == true) {
            cInventoryorderLine.allLinesObl = null;
            cInventoryorderLine.pTruncateTableBln();
        }

        cWebresult WebResult;

        WebResult = cInventoryorder.getInventoryorderViewModel().pGetLinesFromWebserviceWrs();
        if (WebResult.getResultBln() == true && WebResult.getSuccessBln() == true) {

            if (cInventoryorderLine.allLinesObl == null) {
                cInventoryorderLine.allLinesObl = new ArrayList<>();
            }

            List<JSONObject> myList = WebResult.getResultDtt();
            for (int i = 0; i < myList.size(); i++) {
                JSONObject jsonObject;
                jsonObject = myList.get(i);

                cInventoryorderLine inventoryorderLine = new cInventoryorderLine(jsonObject);
                inventoryorderLine.pInsertInDatabaseBln();

                if (! inventoryorderLine.getHandeledTimeStampStr().isEmpty()) {

                  cInventoryorderBin.currentInventoryOrderBin  =   cInventoryorder.currentInventoryOrder.pGetBin(inventoryorderLine.getBinCodeStr());
                    if ( cInventoryorderBin.currentInventoryOrderBin == null) {
                        continue;
                    }

                    cInventoryorderBin.currentInventoryOrderBin.statusInt = cWarehouseorder.InventoryBinStatusEnu.InventoryDone;
                    cInventoryorderBin.currentInventoryOrderBin.pUpdateStatusInDatabaseBln();
                    cInventoryorderBin.currentInventoryOrderBin = null;
                }


                continue;
            }

            return  true;

        } else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETINVENTORYORDERLINES);
            return false;
        }
    }

    public boolean pGetBinsViaWebserviceBln(Boolean pvRefreshBln) {

        if (pvRefreshBln == true) {
            cInventoryorderBin.allInventoryorderBinsObl = null;
            cInventoryorderBin.pTruncateTableBln();
        }

        cWebresult WebResult;

        WebResult = cInventoryorder.getInventoryorderViewModel().pGetBinsFromWebserviceWrs();
        if (WebResult.getResultBln() == true && WebResult.getSuccessBln() == true) {

            if (cInventoryorderBin.allInventoryorderBinsObl == null) {
                cInventoryorderBin.allInventoryorderBinsObl = new ArrayList<>();
            }

            List<JSONObject> myList = WebResult.getResultDtt();
            for (int i = 0; i < myList.size(); i++) {
                JSONObject jsonObject;
                jsonObject = myList.get(i);

                cInventoryorderBin inventoryorderBin = new cInventoryorderBin(jsonObject);
                inventoryorderBin.pInsertInDatabaseBln();
            }

            return  true;

        } else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETINVENTORYORDERBINS);
            return false;
        }
    }

    public boolean pGetCommentsViaWebserviceBln(Boolean pvRefeshBln) {

        if (pvRefeshBln == true) {
            cComment.allCommentsObl = null;
            cComment.pTruncateTableBln();
            cComment.commentsShownBln = false;
        }

        cWebresult webresult;
        webresult = cInventoryorder.getInventoryorderViewModel().pGetCommentsFromWebserviceWrs();
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
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETINVENTORYORDERCOMMENTS);
            return false;
        }
    }

    public boolean pGetBarcodesViaWebserviceBln(Boolean pvRefreshBln) {

        if (pvRefreshBln == true) {
            cInventoryorderBarcode.allInventoryorderBarcodesObl = null;
            cInventoryorderBarcode.pTruncateTableBln();
        }

        cWebresult WebResult;

        WebResult =  cInventoryorder.getInventoryorderViewModel().pGetBarcodesFromWebserviceWrs();
        if (WebResult.getResultBln() == true && WebResult.getSuccessBln() == true ){

            if (cInventoryorderBarcode.allInventoryorderBarcodesObl == null) {
                cInventoryorderBarcode.allInventoryorderBarcodesObl = new ArrayList<>();
            }

            List<JSONObject> myList = WebResult.getResultDtt();
            for (int i = 0; i < myList.size(); i++) {
                JSONObject jsonObject;
                jsonObject = myList.get(i);

                cInventoryorderBarcode inventoryorderBarcode = new cInventoryorderBarcode(jsonObject);

                inventoryorderBarcode.pInsertInDatabaseBln();
            }

            return  true;
        }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETINVENTORYORDERBARCODES);
            return  false;
        }
    }

    public boolean pGetLineBarcodesViaWebserviceBln(Boolean pvRefreshBln) {

        if (pvRefreshBln == true) {
            cInventoryorderLineBarcode.allLineBarcodesObl = null;
            cInventoryorderLineBarcode.pTruncateTableBln();
        }

        cWebresult WebResult;

        WebResult =  cInventoryorder.getInventoryorderViewModel().pGetLineBarcodesFromWebserviceWrs();
        if (WebResult.getResultBln() == true && WebResult.getSuccessBln() == true ){
            List<JSONObject> myList = WebResult.getResultDtt();
            for (int i = 0; i < myList.size(); i++) {
                JSONObject jsonObject;
                jsonObject = myList.get(i);


                cInventoryorderLineBarcode inventoryorderLineBarcode = new cInventoryorderLineBarcode(jsonObject);

                //Search for line that belongs to this barcode
                cInventoryorderLine inventoryorderLine = cInventoryorder.currentInventoryOrder.pGetLineWithLineNo(inventoryorderLineBarcode.getLineNoLng());

                //If we can't find it, skip this
                if (inventoryorderLine == null) {
                    continue;
                }

                //Add barcode to line
                inventoryorderLine.pAddLineBarcode(inventoryorderLineBarcode.getBarcodeStr(),inventoryorderLineBarcode.getQuantityhandledDbl());
            }
        }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETINVENTORYORDERLINEBARCODES);
            return  false;
        }

        return  true;
    }

    public boolean pAddUnkownBarcodeBln(cBarcodeScan pvBarcodeScan) {

        cInventoryorder.currentInventoryOrder.unknownVariantCounterInt += 1;

        cWebresult WebResult;

        WebResult =  cInventoryorder.getInventoryorderViewModel().pAddUnknownItemViaWebserviceWrs(pvBarcodeScan);
        if (WebResult.getResultBln() == true && WebResult.getSuccessBln() == true ){
            List<JSONObject> myList = WebResult.getResultDtt();
            for (int i = 0; i < myList.size(); i++) {
                JSONObject jsonObject;
                jsonObject = myList.get(i);

                cInventoryorderBarcode inventoryorderBarcode = new cInventoryorderBarcode(jsonObject);
                inventoryorderBarcode.pInsertInDatabaseBln();
                cInventoryorderBarcode.currentInventoryOrderBarcode = inventoryorderBarcode;
                return  true;
            }
        }
        else {
            cInventoryorder.currentInventoryOrder.unknownVariantCounterInt -= 1;
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_INVENTORYBARCODECREATE);
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

        //Search for the scanned barcode in the article barcodes
        cArticleBarcode matchedArticleBarcode = null;
        for (cArticleBarcode articleBarcode : cArticle.currentArticle.barcodesObl) {

            if (articleBarcode.getBarcodeStr().equalsIgnoreCase(pvBarcodeScan.getBarcodeStr()) ||
                articleBarcode.getBarcodeStr().equalsIgnoreCase(pvBarcodeScan.getBarcodeOriginalStr())) {
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
        if (WebResult.getResultBln() == true && WebResult.getSuccessBln() == true ){
                for (cArticleBarcode articleBarcode :  cArticle.currentArticle.barcodesObl) {
                    cInventoryorderBarcode inventoryorderBarcode = new cInventoryorderBarcode(articleBarcode);
                    inventoryorderBarcode.pInsertInDatabaseBln();

                    if (inventoryorderBarcode.getBarcodeStr().equalsIgnoreCase(pvBarcodeScan.getBarcodeStr())) {
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

    public boolean pAddLineBln() {

        cWebresult WebResult;

        WebResult =  cInventoryorder.getInventoryorderViewModel().pAddLineViaWebserviceWrs();
        if (WebResult.getResultBln() == true && WebResult.getSuccessBln() == true ){
            List<JSONObject> myList = WebResult.getResultDtt();
            for (int i = 0; i < myList.size(); i++) {
                JSONObject jsonObject;
                jsonObject = myList.get(i);

                cInventoryorderLine.currentInventoryOrderLine= new cInventoryorderLine(jsonObject);
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

    public  cInventoryorderLine pGetLineWithLineNo(Long pvLineNoLng) {

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

    public  List<cInventoryorderLine> pGetLinesForBinObl(String pvBincodeStr) {

        List<cInventoryorderLine> resultObl = new ArrayList<>();
        List<cInventoryorderLineEntity> hulpResultObl;

        hulpResultObl = cInventoryorderLine.getInventoryorderLineViewModel().pGetLinesFromDatabaseObl(pvBincodeStr);
        if (hulpResultObl == null || hulpResultObl.size() == 0) {
            return resultObl;
        }

        for (cInventoryorderLineEntity inventoryorderLineEntity : hulpResultObl) {
            cInventoryorderLine inventoryorderLine = new cInventoryorderLine(inventoryorderLineEntity);
            resultObl.add(inventoryorderLine);
        }

        return resultObl;
    }

    public Double  pGetTotalCountDbl() {

        Double resultDbl;

        resultDbl = cInventoryorderLine.getInventoryorderLineViewModel().pGetTotalCountDbl();
        if (resultDbl == null ) {
            return Double.valueOf(0);
        }

        return resultDbl;
    }

    public Double  pGetCountForBinDbl(String pvBincodeStr) {

      Double resultDbl;

        resultDbl = cInventoryorderLine.getInventoryorderLineViewModel().pGetCountForBinCodeDbl(pvBincodeStr);
        if (resultDbl == null ) {
            return Double.valueOf(0);
        }

        return resultDbl;
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

    public List<cComment> pFeedbackCommentObl(){

        if (cInventoryorder.currentInventoryOrder.commentsObl() == null || cInventoryorder.currentInventoryOrder.commentsObl().size() == 0) {
            return  null;
        }

        List<cComment> hulpObl = cComment.pGetCommentsForTypeObl(cWarehouseorder.CommentTypeEnu.FEEDBACK);
        return hulpObl;

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
        if (WebResult.getResultBln() == true && WebResult.getSuccessBln() == true ){
            List<JSONObject> myList = WebResult.getResultDtt();
            for (int i = 0; i < myList.size(); i++) {
                JSONObject jsonObject;
                jsonObject = myList.get(i);

                cInventoryorderBin InventoryorderBin = new cInventoryorderBin(jsonObject);
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

    public cInventoryorderBarcode pGetOrderBarcode(cBarcodeScan pvBarcodescan) {

        if (this.barcodesObl() == null || this.barcodesObl().size() == 0)  {
            return  null;
        }

        for (cInventoryorderBarcode inventoryorderBarcode : this.barcodesObl()) {

            if (inventoryorderBarcode.getBarcodeStr().equalsIgnoreCase(pvBarcodescan.getBarcodeStr()) || inventoryorderBarcode.getBarcodeStr().equalsIgnoreCase(pvBarcodescan.getBarcodeOriginalStr())) {
                return  inventoryorderBarcode;
            }
        }

        return  null;

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

            cInventoryorder.currentInventoryOrder.pGetCommentsViaWebErrorBln(webresult.getResultDtt());
            return result;
        }

        return  result;


    }

    //Region Public Methods

    //Region Private Methods

    private static boolean mTruncateTableBln() {
        cInventoryorder.getInventoryorderViewModel().deleteAll();
        return true;
    }

    //End Region Private Methods





    //End Region Private Methods



}
