package SSU_WHS.Move.MoveOrders;

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
import SSU_WHS.Basics.Settings.cSetting;
import SSU_WHS.Basics.Users.cUser;
import SSU_WHS.General.Comments.cComment;
import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.Move.MoveorderBarcodes.cMoveorderBarcode;
import SSU_WHS.Move.MoveorderLineBarcodes.cMoveorderLineBarcode;
import SSU_WHS.Move.MoveorderLines.cMoveorderLine;
import SSU_WHS.Move.MoveorderLines.cMoveorderLineEntity;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebserviceDefinitions;
import nl.icsvertex.scansuite.R;


public class cMoveorder {

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

    public String binCodeStr;
    public String getBinCodeStr() {
        return binCodeStr;
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

    public Boolean moveAmountManualBln;
    public Boolean getMoveAmountManualBln() {return this.moveAmountManualBln;}

    public Boolean moveBarcodeCheckBln;
    public Boolean getMoveBarcodeCheckBln() {return this.moveBarcodeCheckBln;}

    public Boolean moveValidateStockBln;
    public Boolean getMoveValidateStockBln() {return this.moveValidateStockBln;}

    public Boolean moveValidateStockEnforceBln;
    public Boolean getMoveValidateStockEnforceBln() {return this.moveValidateStockEnforceBln;}

    public Boolean moveMiBatchTakeRequiredBln;
    public Boolean getMoveMiBatchTakeRequiredBln() {return this.moveMiBatchTakeRequiredBln;}

    public Boolean moveMoBatchPlaceRequiredBln;
    public Boolean getMoveMoBatchPlaceRequiredBln() {return this.moveMoBatchPlaceRequiredBln;}

    public Boolean moveMtTakeAmountManualBln;
    public Boolean getMoveMtTakeAmountManualBln() {return this.moveMtTakeAmountManualBln;}

    public Boolean moveMtTakeAutoItemBln;
    public Boolean getMoveMtTakeAutoItemBln() {return this.moveMtTakeAutoItemBln;}

    public Boolean moveMtTakeAutoItemContainerOnceBln;
    public Boolean getMoveMtTakeAutoItemContainerOnceStr() {return this.moveMtTakeAutoItemContainerOnceBln;}

    public Boolean moveMtTakeAllowEndBln;
    public Boolean getMoveMtTakeAllowEndBln() {return this.moveMtTakeAllowEndBln;}

    public Boolean moveMtTakeIgnoreUndertakeBln;
    public Boolean getMoveMtTakeIgnoreUndertakeBln() {return this.moveMtTakeIgnoreUndertakeBln;}

    public Boolean moveMtTakeAutoAcceptSinglePieceBln;
    public Boolean getMoveMtTakeAutoAcceptSinglePieceBln() {return this.moveMtTakeAutoAcceptSinglePieceBln;}

    public Boolean moveMtPlaceAmountManualBln;
    public Boolean getMoveMtPlaceAmountManualBln() {return this.moveMtPlaceAmountManualBln;}

    public Boolean moveMtPlaceAutoItemBln;
    public Boolean getMoveMtPlaceAutoItemBln() {return this.moveMtPlaceAutoItemBln;}

    public Boolean moveAutoAcceptAtRequestedBln;
    public Boolean getMoveAutoAcceptAtRequestedBln() {return this.moveAutoAcceptAtRequestedBln;}

    public Boolean moveNoExtraBinsBln;
    public Boolean getMoveNoExtraBinsBln() {return this.moveNoExtraBinsBln;}

    public Boolean moveNoExtraItemsBln;
    public Boolean getMoveNoExtraItemsBln() {return this.moveNoExtraItemsBln;}

    public Boolean moveNoExtraPiecesBln;
    public Boolean getMoveNoExtraPiecesBln() {return this.moveNoExtraPiecesBln;}

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

    public String containerStr;
    public String getContainerStr() {
        return containerStr;
    }

    public String currentLocationStr;
    public String getCurrentLocationStr() {
        return currentLocationStr;
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


    public cMoveorderEntity moveorderEntity;
    public boolean indatabaseBln;

    public static cMoveorderViewModel gMoveorderViewModel;

    public static cMoveorderViewModel getMoveorderViewModel() {
        if (gMoveorderViewModel == null) {
            gMoveorderViewModel = ViewModelProviders.of(cAppExtension.fragmentActivity).get(cMoveorderViewModel.class);
        }
        return gMoveorderViewModel;
    }



    public static cMoveorderAdapter gMoveorderAdapter;

    public static cMoveorderAdapter getMoveorderAdapter() {
        if (gMoveorderAdapter == null) {
            gMoveorderAdapter = new cMoveorderAdapter();
        }
        return gMoveorderAdapter;
    }

    public List<cComment> commentsObl() {
        return cComment.allCommentsObl;
    }

    public List<cMoveorderBarcode> barcodesObl () {return  cMoveorderBarcode.allMoveorderBarcodesObl;}

    public List<cMoveorderLine> linesObl () {return  cMoveorderLine.allLinesObl;}

    public static List<cMoveorder> allMoveordersObl;
    public static cMoveorder currentMoveOrder;

    //Region Public Properties

    //Region Constructor

    public cMoveorder(JSONObject pvJsonObject) {

        this.moveorderEntity = new cMoveorderEntity(pvJsonObject);
        this.orderNumberStr = this.moveorderEntity.getOrdernumberStr();
        this.orderTypeStr = this.moveorderEntity.getOrderTypeStr();
        this.numberOfBinsInt = cText.pStringToIntegerInt(this.moveorderEntity.getNumberofBinsStr());
        this.assignedUserIdStr = this.moveorderEntity.getAssignedUserIdStr();
        this.currentUserIdStr = this.moveorderEntity.getCurrentUserIdStr();
        this.statusInt = cText.pStringToIntegerInt(this.moveorderEntity.getStatusStr());
        this.binCodeStr = this.moveorderEntity.getBincodeStr();
        this.externalReferenceStr = this.moveorderEntity.getExternalReferenceStr();
        this.workPlaceStr = this.moveorderEntity.getWorkplaceStr();
        this.stockOwnerStr = this.moveorderEntity.getStockOwnerStr();

        this.moveAmountManualBln = cText.pStringToBooleanBln(this.moveorderEntity.getMoveAmountManualStr(), false);
        this.moveBarcodeCheckBln = cText.pStringToBooleanBln(this.moveorderEntity.getMoveBarcodeCheckStr(), false);
        this.moveValidateStockBln = cText.pStringToBooleanBln(this.moveorderEntity.getMoveValidateStockStr(), false);
        this.moveValidateStockEnforceBln = cText.pStringToBooleanBln(this.moveorderEntity.getMoveValidateStockEnforceStr(), false);
        this.moveMiBatchTakeRequiredBln = cText.pStringToBooleanBln(this.moveorderEntity.getMoveMiBatchTakeRequiredStr(), false);
        this.moveMoBatchPlaceRequiredBln = cText.pStringToBooleanBln(this.moveorderEntity.getMoveMoBatchPlaceRequiredStr(), false);
        this.moveMtTakeAmountManualBln = cText.pStringToBooleanBln(this.moveorderEntity.getMoveMtTakeAmountManualStr(), false);
        this.moveMtTakeAutoItemBln = cText.pStringToBooleanBln(this.moveorderEntity.getMoveMtTakeAutoItemStr(), false);
        this.moveMtTakeAutoItemContainerOnceBln = cText.pStringToBooleanBln(this.moveorderEntity.getMoveMtTakeAutoItemContainerOnceStr(), false);
        this.moveMtTakeAllowEndBln = cText.pStringToBooleanBln(this.moveorderEntity.getMoveMtTakeAllowEndStr(), false);
        this.moveMtTakeIgnoreUndertakeBln = cText.pStringToBooleanBln(this.moveorderEntity.getMoveMtTakeIgnoreUndertakeStr(), false);
        this.moveMtTakeAutoAcceptSinglePieceBln = cText.pStringToBooleanBln(this.moveorderEntity.getMoveMtTakeAutoAcceptSinglePieceStr(), false);
        this.moveMtPlaceAmountManualBln = cText.pStringToBooleanBln(this.moveorderEntity.getMoveMtPlaceAmountManualStr(), false);
        this.moveMtPlaceAutoItemBln = cText.pStringToBooleanBln(this.moveorderEntity.getMoveMtPlaceAutoItemStr(), false);
        this.moveAutoAcceptAtRequestedBln = cText.pStringToBooleanBln(this.moveorderEntity.getMoveAutoAcceptAtRequestedStr(), false);
        this.moveNoExtraBinsBln = cText.pStringToBooleanBln(this.moveorderEntity.getMoveNoExtraBinsStr(), false);
        this.moveNoExtraItemsBln = cText.pStringToBooleanBln(this.moveorderEntity.getMoveNoExtraItemsStr(), false);
        this.moveNoExtraPiecesBln = cText.pStringToBooleanBln(this.moveorderEntity.getMoveNoExtraPiecesStr(), false);

        this.sourceDocumentInt = cText.pStringToIntegerInt(this.moveorderEntity.getSourceDocumentStr()) ;
        this.documentStr = this.moveorderEntity.getDocumentStr();
        this.document2Str = this.moveorderEntity.getDocument2Str();
        this.containerStr = this.moveorderEntity.getContainerStr();
        this.currentLocationStr = this.getCurrentLocationStr();
        this.webserviceTimeOutERPInsInt = cText.pStringToIntegerInt(this.moveorderEntity.getWebserviceTimeOutERPInsStr());
        this.interfaceResultMethodStr = this.moveorderEntity.getInterfaceResultMethodStr();
    }

    public cMoveorder(cMoveorderEntity pvMoveorderEntity) {

        this.moveorderEntity = pvMoveorderEntity;
        this.orderNumberStr = this.moveorderEntity.getOrdernumberStr();
        this.orderTypeStr = this.moveorderEntity.getOrderTypeStr();
        this.numberOfBinsInt = cText.pStringToIntegerInt(this.moveorderEntity.getNumberofBinsStr());
        this.assignedUserIdStr = this.moveorderEntity.getAssignedUserIdStr();
        this.currentUserIdStr = this.moveorderEntity.getCurrentUserIdStr();
        this.statusInt = cText.pStringToIntegerInt(this.moveorderEntity.getStatusStr());
        this.binCodeStr = this.moveorderEntity.getBincodeStr();
        this.externalReferenceStr = this.moveorderEntity.getExternalReferenceStr();
        this.workPlaceStr = this.moveorderEntity.getWorkplaceStr();
        this.stockOwnerStr = this.moveorderEntity.getStockOwnerStr();

        this.moveAmountManualBln = cText.pStringToBooleanBln(this.moveorderEntity.getMoveAmountManualStr(), false);
        this.moveBarcodeCheckBln = cText.pStringToBooleanBln(this.moveorderEntity.getMoveBarcodeCheckStr(), false);
        this.moveValidateStockBln = cText.pStringToBooleanBln(this.moveorderEntity.getMoveValidateStockStr(), false);
        this.moveValidateStockEnforceBln = cText.pStringToBooleanBln(this.moveorderEntity.getMoveValidateStockEnforceStr(), false);
        this.moveMiBatchTakeRequiredBln = cText.pStringToBooleanBln(this.moveorderEntity.getMoveMiBatchTakeRequiredStr(), false);
        this.moveMoBatchPlaceRequiredBln = cText.pStringToBooleanBln(this.moveorderEntity.getMoveMoBatchPlaceRequiredStr(), false);
        this.moveMtTakeAmountManualBln = cText.pStringToBooleanBln(this.moveorderEntity.getMoveMtTakeAmountManualStr(), false);
        this.moveMtTakeAutoItemBln = cText.pStringToBooleanBln(this.moveorderEntity.getMoveMtTakeAutoItemStr(), false);
        this.moveMtTakeAutoItemContainerOnceBln = cText.pStringToBooleanBln(this.moveorderEntity.getMoveMtTakeAutoItemContainerOnceStr(), false);
        this.moveMtTakeAllowEndBln = cText.pStringToBooleanBln(this.moveorderEntity.getMoveMtTakeAllowEndStr(), false);
        this.moveMtTakeIgnoreUndertakeBln = cText.pStringToBooleanBln(this.moveorderEntity.getMoveMtTakeIgnoreUndertakeStr(), false);
        this.moveMtTakeAutoAcceptSinglePieceBln = cText.pStringToBooleanBln(this.moveorderEntity.getMoveMtTakeAutoAcceptSinglePieceStr(), false);
        this.moveMtPlaceAmountManualBln = cText.pStringToBooleanBln(this.moveorderEntity.getMoveMtPlaceAmountManualStr(), false);
        this.moveMtPlaceAutoItemBln = cText.pStringToBooleanBln(this.moveorderEntity.getMoveMtPlaceAutoItemStr(), false);
        this.moveAutoAcceptAtRequestedBln = cText.pStringToBooleanBln(this.moveorderEntity.getMoveAutoAcceptAtRequestedStr(), false);
        this.moveNoExtraBinsBln = cText.pStringToBooleanBln(this.moveorderEntity.getMoveNoExtraBinsStr(), false);
        this.moveNoExtraItemsBln = cText.pStringToBooleanBln(this.moveorderEntity.getMoveNoExtraItemsStr(), false);
        this.moveNoExtraPiecesBln = cText.pStringToBooleanBln(this.moveorderEntity.getMoveNoExtraPiecesStr(), false);

        this.sourceDocumentInt = cText.pStringToIntegerInt(this.moveorderEntity.getSourceDocumentStr()) ;
        this.documentStr = this.moveorderEntity.getDocumentStr();
        this.document2Str = this.moveorderEntity.getDocument2Str();
        this.containerStr = this.moveorderEntity.getContainerStr();
        this.currentLocationStr = this.getCurrentLocationStr();
        this.webserviceTimeOutERPInsInt = cText.pStringToIntegerInt(this.moveorderEntity.getWebserviceTimeOutERPInsStr());
        this.interfaceResultMethodStr = this.moveorderEntity.getInterfaceResultMethodStr();
    }

    //End Region Constructor

    //Region Public Methods

    public boolean pInsertInDatabaseBln() {
        cMoveorder.getMoveorderViewModel().insert(this.moveorderEntity);
        this.indatabaseBln = true;

        if (cMoveorder.allMoveordersObl == null) {
            cMoveorder.allMoveordersObl = new ArrayList<>();
        }
        cMoveorder.allMoveordersObl.add(this);
        return true;
    }

    public static Boolean pCreateMoveOrderViaWebserviceBln(String pvDocumentStr) {

        cWebresult WebResult;
        WebResult = cMoveorder.getMoveorderViewModel().pCreateMoveOrderViaWebserviceWrs(pvDocumentStr);
        if (WebResult.getResultBln() == true && WebResult.getSuccessBln() == true) {
            List<JSONObject> myList = WebResult.getResultDtt();
            for (int i = 0; i < myList.size(); i++) {
                JSONObject jsonObject;
                jsonObject = myList.get(i);

                cMoveorder moveorder = new cMoveorder(jsonObject);
                moveorder.pInsertInDatabaseBln();

                cMoveorder.currentMoveOrder = moveorder;
                return true;
            }

        } else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_MOVEORDERCREATE);
            return false;
        }

        return false;
    }

    public static boolean pGetMoveOrdersViaWebserviceBln(Boolean pvRefreshBln, String pvSearchTextStr, String pvMainTypeStr) {

        if (pvRefreshBln == true) {
            cMoveorder.allMoveordersObl = null;
            cMoveorder.mTruncateTableBln();
        }

        cWebresult WebResult;
        WebResult = cMoveorder.getMoveorderViewModel().pGetMoveordersFromWebserviceWrs(pvSearchTextStr, pvMainTypeStr);
        if (WebResult.getResultBln() == true && WebResult.getSuccessBln() == true) {

            List<JSONObject> myList = WebResult.getResultDtt();
            for (int i = 0; i < myList.size(); i++) {
                JSONObject jsonObject;
                jsonObject = myList.get(i);

                cMoveorder moveorder = new cMoveorder(jsonObject);
                moveorder.pInsertInDatabaseBln();
            }
            return true;
        } else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETMOVEORDERS);
            return false;
        }
    }

    public static List<cMoveorder> pGetMovesFromDatabasObl() {

        List<cMoveorder> resultObl = new ArrayList<>();
        List<cMoveorderEntity> hulpResultObl;

        hulpResultObl = cMoveorder.getMoveorderViewModel().pGetMoveordersFromDatabaseObl();
        if (hulpResultObl == null || hulpResultObl.size() == 0) {
            return resultObl;
        }

        for (cMoveorderEntity moveorderEntity : hulpResultObl) {
            cMoveorder moveorder = new cMoveorder(moveorderEntity);
            resultObl.add(moveorder);
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

        Webresult = cWarehouseorder.getWarehouseorderViewModel().pLockWarehouseopdrachtViaWebserviceWrs(cWarehouseorder.OrderTypeEnu.VERPLAATS.toString(), this.getOrderNumberStr(), cDeviceInfo.getSerialnumberStr(), pvStepCodeEnu.toString(), pvWorkFlowStepInt, ignoreBusyBln);

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

            cMoveorder.currentMoveOrder.pGetCommentsViaWebErrorBln(Webresult.getResultDtt());
            return result;
        }

        return result;

    }

    public boolean pLockReleaseViaWebserviceBln(cWarehouseorder.StepCodeEnu pvStepCodeEnu, int pvWorkFlowStepInt) {

        cWebresult Webresult;

        Webresult = cWarehouseorder.getWarehouseorderViewModel().pLockReleaseWarehouseorderViaWebserviceWrs(cWarehouseorder.OrderTypeEnu.VERPLAATS.toString(), this.getOrderNumberStr(), cDeviceInfo.getSerialnumberStr(), pvStepCodeEnu.toString(), pvWorkFlowStepInt);

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

        cMoveorderLine.pTruncateTableBln();
        cMoveorderBarcode.pTruncateTableBln();
        cMoveorderLineBarcode.pTruncateTableBln();
        return true;
    }

    public boolean pGetLinesViaWebserviceBln(Boolean pvRefreshBln) {

        if (pvRefreshBln == true) {
            cMoveorderLine.allLinesObl = null;
            cMoveorderLine.pTruncateTableBln();
        }

        cWebresult WebResult;

        WebResult = cMoveorder.getMoveorderViewModel().pGetLinesFromWebserviceWrs();
        if (WebResult.getResultBln() == true && WebResult.getSuccessBln() == true) {

            if (cMoveorderLine.allLinesObl == null) {
                cMoveorderLine.allLinesObl = new ArrayList<>();
            }

            List<JSONObject> myList = WebResult.getResultDtt();
            for (int i = 0; i < myList.size(); i++) {
                JSONObject jsonObject;
                jsonObject = myList.get(i);

                cMoveorderLine moveorderLine = new cMoveorderLine(jsonObject);
                moveorderLine.pInsertInDatabaseBln();

                continue;
            }

            return  true;

        } else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETMOVEORDERLINES);
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
        webresult = cMoveorder.getMoveorderViewModel().pGetCommentsFromWebserviceWrs();
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
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETMOVEORDERCOMMENTS);
            return false;
        }
    }

    public boolean pGetBarcodesViaWebserviceBln(Boolean pvRefreshBln) {

        if (pvRefreshBln == true) {
            cMoveorderBarcode.allMoveorderBarcodesObl = null;
            cMoveorderBarcode.pTruncateTableBln();
        }

        cWebresult WebResult;

        WebResult =  cMoveorder.getMoveorderViewModel().pGetBarcodesFromWebserviceWrs();
        if (WebResult.getResultBln() == true && WebResult.getSuccessBln() == true ){

            if (cMoveorderBarcode.allMoveorderBarcodesObl == null) {
                cMoveorderBarcode.allMoveorderBarcodesObl = new ArrayList<>();
            }

            List<JSONObject> myList = WebResult.getResultDtt();
            for (int i = 0; i < myList.size(); i++) {
                JSONObject jsonObject;
                jsonObject = myList.get(i);

                cMoveorderBarcode moveorderBarcode = new cMoveorderBarcode(jsonObject);

                moveorderBarcode.pInsertInDatabaseBln();
            }

            return  true;
        }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETMOVEORDERBARCODES);
            return  false;
        }
    }

    public boolean pGetLineBarcodesViaWebserviceBln(Boolean pvRefreshBln) {

        if (pvRefreshBln == true) {
            cMoveorderLineBarcode.allLineBarcodesObl = null;
            cMoveorderLineBarcode.pTruncateTableBln();
        }

        cWebresult WebResult;

        WebResult =  cMoveorder.getMoveorderViewModel().pGetLineBarcodesFromWebserviceWrs();
        if (WebResult.getResultBln() == true && WebResult.getSuccessBln() == true ){
            List<JSONObject> myList = WebResult.getResultDtt();
            for (int i = 0; i < myList.size(); i++) {
                JSONObject jsonObject;
                jsonObject = myList.get(i);

                cMoveorderLineBarcode moveorderLineBarcode = new cMoveorderLineBarcode(jsonObject);

                //Search for line that belongs to this barcode
                cMoveorderLine moveorderLine = cMoveorder.currentMoveOrder.pGetLineWithLineNo(moveorderLineBarcode.getLineNoLng());

                //If we can't find it, skip this
                if (moveorderLine == null) {
                    continue;
                }

                //Add barcode to line
                moveorderLine.pAddLineBarcodeBln(moveorderLineBarcode.getBarcodeStr(),moveorderLineBarcode.getQuantityhandledDbl());
            }
        }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETMOVEORDERLINEBARCODES);
            return  false;
        }

        return  true;
    }

    public boolean pAddUnkownBarcodeBln(cBarcodeScan pvBarcodeScan) {

        cMoveorder.currentMoveOrder.unknownVariantCounterInt += 1;

        cWebresult WebResult;

        WebResult =  cMoveorder.getMoveorderViewModel().pAddUnknownItemViaWebserviceWrs(pvBarcodeScan);
        if (WebResult.getResultBln() == true && WebResult.getSuccessBln() == true ){
            List<JSONObject> myList = WebResult.getResultDtt();
            for (int i = 0; i < myList.size(); i++) {
                JSONObject jsonObject;
                jsonObject = myList.get(i);

                cMoveorderBarcode moveorderBarcode = new cMoveorderBarcode(jsonObject);
                moveorderBarcode.pInsertInDatabaseBln();
                cMoveorderBarcode.currentMoveOrderBarcode = moveorderBarcode;
                return  true;
            }
        }
        else {
            cMoveorder.currentMoveOrder.unknownVariantCounterInt -= 1;
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_MOVEBARCODECREATE);
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

            if (articleBarcode.getBarcodeStr().equalsIgnoreCase(pvBarcodeScan.getBarcodeFormattedStr()) ||
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
        WebResult =  cMoveorder.getMoveorderViewModel().pAddERPItemViaWebserviceWrs(matchedArticleBarcode);
        if (WebResult.getResultBln() == true && WebResult.getSuccessBln() == true ){
            List<JSONObject> myList = WebResult.getResultDtt();
            for (int i = 0; i < myList.size(); i++) {
                JSONObject jsonObject;
                jsonObject = myList.get(i);

                cMoveorderBarcode moveorderBarcode = new cMoveorderBarcode(jsonObject);
                moveorderBarcode.pInsertInDatabaseBln();
                cMoveorderBarcode.currentMoveOrderBarcode = moveorderBarcode;
                return  true;
            }
        }
        else {
            cMoveorder.currentMoveOrder.unknownVariantCounterInt -= 1;
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_MOVEBARCODECREATE);
            return  false;
        }

        return  true;
    }


    public  cMoveorderLine pGetLineWithLineNo(Long pvLineNoLng) {

       if (this.linesObl() == null || this.linesObl().size() == 0) {
           return  null;
       }

       for (cMoveorderLine moveorderLine : this.linesObl()) {

           if (cText.pIntToStringStr(moveorderLine.getLineNoInt()).equalsIgnoreCase(cText.pLongToStringStr(pvLineNoLng))) {
               return  moveorderLine;
           }

       }

       return  null;

    }

    public Double  pGetTotalCountDbl() {

        Double resultDbl;

        resultDbl = cMoveorderLine.getMoveorderLineViewModel().pGetTotalCountDbl();
        if (resultDbl == null ) {
            return Double.valueOf(0);
        }

        return resultDbl;
    }

    public Double  pGetCountForBinDbl(String pvBincodeStr) {

      Double resultDbl;

        resultDbl = cMoveorderLine.getMoveorderLineViewModel().pGetCountForBinCodeDbl(pvBincodeStr);
        if (resultDbl == null ) {
            return Double.valueOf(0);
        }

        return resultDbl;
    }

    public static List<cMoveorder> pGetMovesWithFilterFromDatabasObl() {

        List<cMoveorder> resultObl = new ArrayList<>();
        List<cMoveorderEntity> hulpResultObl;

        hulpResultObl =  cMoveorder.getMoveorderViewModel().pGetMovesWithFilterFromDatabaseObl(cUser.currentUser.getNameStr(), cSharedPreferences.userFilterBln());
        if (hulpResultObl == null || hulpResultObl.size() == 0) {
            return  resultObl;
        }

        for (cMoveorderEntity moveorderEntity : hulpResultObl ) {
            cMoveorder moveorder = new cMoveorder(moveorderEntity);
            resultObl.add(moveorder);
        }

        return  resultObl;
    }

    public List<cComment> pFeedbackCommentObl(){

        if (cMoveorder.currentMoveOrder.commentsObl() == null || cMoveorder.currentMoveOrder.commentsObl().size() == 0) {
            return  null;
        }

        List<cComment> hulpObl = cComment.pGetCommentsForTypeObl(cWarehouseorder.CommentTypeEnu.FEEDBACK);
        return hulpObl;

    }

    public cMoveorderBarcode pGetOrderBarcode(cBarcodeScan pvBarcodescan) {

        if (this.barcodesObl() == null || this.barcodesObl().size() == 0)  {
            return  null;
        }

        for (cMoveorderBarcode moveorderBarcode : this.barcodesObl()) {

            if (moveorderBarcode.getBarcodeStr().equalsIgnoreCase(pvBarcodescan.getBarcodeFormattedStr()) || moveorderBarcode.getBarcodeStr().equalsIgnoreCase(pvBarcodescan.getBarcodeOriginalStr())) {
                return  moveorderBarcode;
            }
        }

        return  null;

    }

    public cResult pOrderHandledViaWebserviceRst() {

        cResult result;
        result = new cResult();
        result.resultBln = true;


        cWebresult webresult;

        webresult =  cMoveorder.getMoveorderViewModel().pHandledViaWebserviceWrs();

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

            cMoveorder.currentMoveOrder.pGetCommentsViaWebErrorBln(webresult.getResultDtt());
            return result;
        }

        return  result;


    }
    public boolean pUpdateCurrentLocationBln(String pvCurrentLocationStr) {


        cWebresult Webresult;

        Webresult = cMoveorder.getMoveorderViewModel().pUpdateCurrentLocationViaWebserviceWrs(pvCurrentLocationStr);
        if (Webresult.getSuccessBln() == false || Webresult.getResultBln() == false) {
            return  false;
        }

        if (cMoveorder.getMoveorderViewModel().pUpdateMoveorderCurrentLocationInDatabaseBln(pvCurrentLocationStr) == false) {
            return  false;
        }

        this.currentLocationStr = pvCurrentLocationStr;
        return  true;

    }

    public double pQuantityTotalDbl() {
        return  cMoveorder.getMoveorderViewModel().pGetQuantityTotalDbl();
    }

    public double pQuantityNotHandledDbl() {
        return  cMoveorder.getMoveorderViewModel().pQuantityNotHandledDbl();
    }
    public double pQuantityHandledDbl() {
        return  cMoveorder.getMoveorderViewModel().pQuantityHandledDbl();
    }

    public List<cMoveorderLine> pGetLinesHandledFromDatabasObl() {

        List<cMoveorderLine> resultObl = new ArrayList<>();
        List<cMoveorderLineEntity> hulpResultObl;

        hulpResultObl =  cMoveorder.getMoveorderViewModel().pGetLinesHandledFromDatabaseObl();
        if (hulpResultObl == null || hulpResultObl.size() == 0) {
            return  resultObl;
        }

        for (cMoveorderLineEntity moveorderLineEntity : hulpResultObl ) {
            cMoveorderLine moveorderLine = new cMoveorderLine(moveorderLineEntity);
            resultObl.add(moveorderLine);
        }
        return  resultObl;
    }
    public List<cMoveorderLine> pGetLinesNotHandledFromDatabasObl() {

        List<cMoveorderLine> resultObl = new ArrayList<>();
        List<cMoveorderLineEntity> hulpResultObl;

        hulpResultObl =  cMoveorder.getMoveorderViewModel().pGetLinesNotHandledFromDatabaseObl();
        if (hulpResultObl == null || hulpResultObl.size() == 0) {
            return  resultObl;
        }

        for (cMoveorderLineEntity moveorderLineEntity : hulpResultObl ) {
            cMoveorderLine moveorderLine = new cMoveorderLine(moveorderLineEntity);
            resultObl.add(moveorderLine);
        }
        return  resultObl;
    }

    public List<cMoveorderLine> pGetLinesForBinFromDatabaseObl(String pvBinStr) {
        List<cMoveorderLine> resultObl = new ArrayList<>();
        List<cMoveorderLineEntity> hulpResultObl;

        hulpResultObl =  cMoveorder.getMoveorderViewModel().pGetLinesForBinFromDatabaseObl(pvBinStr);

        if (hulpResultObl == null || hulpResultObl.size() == 0) {
            return  resultObl;
        }

        for (cMoveorderLineEntity moveorderLineEntity : hulpResultObl ) {
            cMoveorderLine moveorderLine = new cMoveorderLine(moveorderLineEntity);
            resultObl.add(moveorderLine);
        }
        return  resultObl;

    }
    //Region Public Methods

    //Region Private Methods

    private static boolean mTruncateTableBln() {
        cMoveorder.getMoveorderViewModel().deleteAll();
        return true;
    }

    //End Region Private Methods





    //End Region Private Methods



}
