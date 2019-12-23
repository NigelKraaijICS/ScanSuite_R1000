package SSU_WHS.Intake.Intakeorders;

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
import SSU_WHS.Basics.Settings.cSetting;
import SSU_WHS.Basics.Users.cUser;
import SSU_WHS.General.Comments.cComment;
import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.Intake.IntakeorderBarcodes.cIntakeorderBarcode;
import SSU_WHS.Intake.IntakeorderMATLineBarcodes.cIntakeorderMATLineBarcode;
import SSU_WHS.Intake.IntakeorderMATLineSummary.cIntakeorderMATSummaryLine;
import SSU_WHS.Intake.IntakeorderMATLines.cIntakeorderMATLine;
import SSU_WHS.Intake.IntakeorderMATLines.cIntakeorderMATLineEntity;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebserviceDefinitions;
import nl.icsvertex.scansuite.R;

public class cIntakeorder {

     public String ordernumberStr;
    public String getOrderNumberStr() {return this.ordernumberStr;}

    public String ordertypeStr;
    public String getOrderTypeStr() {return this.ordertypeStr;}

    public int numberofBinsInt;
    public int getNumberofBinsInt() {return this.numberofBinsInt;}

    public String assignedUserIdStr;
    public String getAssignedUserIdStr() {return this.assignedUserIdStr;}

    public String currentUserIdStr;
    public String getCurrentUserIdStr() {return this.currentUserIdStr;}

    public String requestedStartDateTimeStr;
    public String getRequestedStartDateTimeStr() {return this.requestedStartDateTimeStr;}

    public String requestedEndDateTimeStr;
    public String getRequestedEndDateTimeStr() {return this.requestedEndDateTimeStr;}

    public int statusInt;
    public int getStatusInt() {return this.statusInt;}

    public String binCodeStr;
    public String getBinCodeStr() {return this.binCodeStr;}

    public String externalReferenceStr;
    public String getExternalReferenceStr() {return this.externalReferenceStr;}

    public String workplaceStr;
    public String getWorkplaceStr() {return this.workplaceStr;}

    public boolean receiveExportPartBln;
    public boolean getReceiveExportPartBln() {return this.receiveExportPartBln;}

    public boolean receiveAmountManualEOBln;
    public boolean getReceiveAmountManualEOBln() {return this.receiveAmountManualEOBln;}

    public boolean receiveAmountManualOMBln;
    public boolean getReceiveAmountManualOMBln() {return this.receiveAmountManualOMBln;}

    public boolean receiveAmountManualMABln;
    public boolean getReceiveAmountManualMABln() {return this.receiveAmountManualMABln;}

    public boolean receiveBarcodeCheckBln;
    public boolean getReceiveBarcodeCheckBln() {return this.receiveBarcodeCheckBln;}

    public boolean receiveStoreAutoAcceptAtRequestedBln;
    public boolean getReceiveStoreAutoAcceptAtRequestedBln() {return this.receiveStoreAutoAcceptAtRequestedBln;}

    public boolean receiveStoreAutoAcceptValidationMessageBln;
    public boolean getReceiveStoreAutoAcceptValidationMessageBln() {return this.receiveStoreAutoAcceptValidationMessageBln;}

    public boolean receiveNoExtraBinsBln;
    public boolean getReceiveNoExtraBinsBln() {return this.receiveNoExtraBinsBln;}

    public boolean receiveNoExtraItemsBln;
    public boolean getReceiveNoExtraItemsBln() {return this.receiveNoExtraItemsBln;}

    public boolean receiveNoExtraPiecesBln;
    public boolean getReceiveNoExtraPiecesBln() {return this.receiveNoExtraPiecesBln;}

    public boolean receiveMatAutoSplitIncompleteLineBln;
    public boolean getReceiveMatAutoSplitIncompleteLineBln() {return this.receiveMatAutoSplitIncompleteLineBln;}

    public boolean receiveMATEmptyBinsBln;
    public boolean getReceiveMATEmptyBinsBln() {return this.receiveMATEmptyBinsBln;}

    public String receivedDateTime;
    public String getReceivedDateTime() {return this.receivedDateTime;}

    public Boolean isProcessingOrParkedBln;
    public Boolean getProcessingOrParkedBln() {
        return isProcessingOrParkedBln;
    }


    public cIntakeorderEntity intakeorderEntity;
    public boolean indatabaseBln;

    public static cIntakeorderViewModel gIntakeorderViewModel;

    public static cIntakeorderViewModel getIntakeorderViewModel() {
        if (gIntakeorderViewModel == null) {
            gIntakeorderViewModel = ViewModelProviders.of(cAppExtension.fragmentActivity).get(cIntakeorderViewModel.class);
        }
        return gIntakeorderViewModel;
    }

    public static cIntakeorderAdapter gIntakeorderAdapter;
    public static cIntakeorderAdapter getIntakeorderAdapter() {
        if (gIntakeorderAdapter == null) {
            gIntakeorderAdapter = new cIntakeorderAdapter();
        }
        return gIntakeorderAdapter;
    }

    public static List<cIntakeorder> allIntakeordersObl;
    public static cIntakeorder currentIntakeOrder;

    public List<cIntakeorderMATSummaryLine> summaryLinesObl(){
        return  cIntakeorderMATSummaryLine.allIntakeorderMATSummaryLinesObl;
    }
    public List<cIntakeorderMATLine> linesObl(){
        return  cIntakeorderMATLine.allIntakeorderMATLinesObl;
    }
    public List<cComment> commentsObl(){
        return  cComment.allCommentsObl;
    }
    public List<cIntakeorderBarcode> barcodesObl () {return  cIntakeorderBarcode.allBarcodesObl;}

    public cIntakeorderBarcode intakeorderBarcodeScanned;

    //Region Constructor

    public cIntakeorder(JSONObject pvJsonObject) {

        this.intakeorderEntity = new cIntakeorderEntity(pvJsonObject);
        this.ordernumberStr = this.intakeorderEntity.getOrdernumberStr();
        this.ordertypeStr = this.intakeorderEntity.getOrderTypeStr();
        this.numberofBinsInt = this.intakeorderEntity.getNumberofBinsInt();
        this.assignedUserIdStr = this.intakeorderEntity.getAssignedUserIdStr();
        this.currentUserIdStr = this.intakeorderEntity.getCurrentUserIdStr();
        this.requestedStartDateTimeStr = this.intakeorderEntity.getRequestedStartDateTimeStr();
        this.requestedEndDateTimeStr = this.intakeorderEntity.getRequestedEndDateTimeStr();
        this.statusInt = this.intakeorderEntity.getStatusInt();
        this.binCodeStr = this.intakeorderEntity.getBinCodeStr();
        this.externalReferenceStr = this.intakeorderEntity.getExternalReferenceStr();
        this.workplaceStr = this.intakeorderEntity.getWorkplaceStr();


        this.receiveExportPartBln = cText.pStringToBooleanBln(this.intakeorderEntity.getReceiveExportPartStr(), false);
        this.receiveAmountManualEOBln = cText.pStringToBooleanBln(this.intakeorderEntity.getReceiveAmountManualEOStr(), false);
        this.receiveAmountManualOMBln = cText.pStringToBooleanBln(this.intakeorderEntity.getReceiveAmountManualOMStr(), false);
        this.receiveAmountManualMABln = cText.pStringToBooleanBln(this.intakeorderEntity.getReceiveAmountManualMAStr(), false);
        this.receiveBarcodeCheckBln = cText.pStringToBooleanBln(this.intakeorderEntity.getReceiveBarcodeCheckStr(), false);
        this.receiveStoreAutoAcceptAtRequestedBln = cText.pStringToBooleanBln(this.intakeorderEntity.getReceiveStoreAutoAcceptAtRequestedStr(), false);
        this.receiveStoreAutoAcceptValidationMessageBln = cText.pStringToBooleanBln(this.intakeorderEntity.getReceiveStoreAutoAcceptValidationMessageStr(), false);
        this.receiveNoExtraBinsBln = cText.pStringToBooleanBln(this.intakeorderEntity.getReceiveNoExtraBinsStr(), false);
        this.receiveNoExtraItemsBln = cText.pStringToBooleanBln(this.intakeorderEntity.getReceiveNoExtraItemsStr(), false);
        this.receiveNoExtraPiecesBln = cText.pStringToBooleanBln(this.intakeorderEntity.getReceiveNoExtraPiecesStr(), false);
        this.receiveMatAutoSplitIncompleteLineBln = cText.pStringToBooleanBln(this.intakeorderEntity.getReceiveMatAutoSplitIncompleteLineStr(), false);

        this.receivedDateTime = this.intakeorderEntity.getReceivedDateTime();
        this.isProcessingOrParkedBln = this.intakeorderEntity.getIsProcessingOrParkedStr();

    }

    public cIntakeorder(cIntakeorderEntity pvIntakeorderEntity) {

        this.intakeorderEntity = pvIntakeorderEntity;
        this.ordernumberStr = this.intakeorderEntity.getOrdernumberStr();
        this.ordertypeStr = this.intakeorderEntity.getOrderTypeStr();
        this.numberofBinsInt = this.intakeorderEntity.getNumberofBinsInt();
        this.assignedUserIdStr = this.intakeorderEntity.getAssignedUserIdStr();
        this.currentUserIdStr = this.intakeorderEntity.getCurrentUserIdStr();
        this.requestedStartDateTimeStr = this.intakeorderEntity.getRequestedStartDateTimeStr();
        this.requestedEndDateTimeStr = this.intakeorderEntity.getRequestedEndDateTimeStr();
        this.statusInt = this.intakeorderEntity.getStatusInt();
        this.binCodeStr = this.intakeorderEntity.getBinCodeStr();
        this.externalReferenceStr = this.intakeorderEntity.getExternalReferenceStr();
        this.workplaceStr = this.intakeorderEntity.getWorkplaceStr();

        this.receiveExportPartBln = cText.pStringToBooleanBln(this.intakeorderEntity.getReceiveExportPartStr(), false);
        this.receiveAmountManualEOBln = cText.pStringToBooleanBln(this.intakeorderEntity.getReceiveAmountManualEOStr(), false);
        this.receiveAmountManualOMBln = cText.pStringToBooleanBln(this.intakeorderEntity.getReceiveAmountManualOMStr(), false);
        this.receiveAmountManualMABln = cText.pStringToBooleanBln(this.intakeorderEntity.getReceiveAmountManualMAStr(), false);
        this.receiveBarcodeCheckBln = cText.pStringToBooleanBln(this.intakeorderEntity.getReceiveBarcodeCheckStr(), false);
        this.receiveStoreAutoAcceptAtRequestedBln = cText.pStringToBooleanBln(this.intakeorderEntity.getReceiveStoreAutoAcceptAtRequestedStr(), false);
        this.receiveStoreAutoAcceptValidationMessageBln = cText.pStringToBooleanBln(this.intakeorderEntity.getReceiveStoreAutoAcceptValidationMessageStr(), false);
        this.receiveNoExtraBinsBln = cText.pStringToBooleanBln(this.intakeorderEntity.getReceiveNoExtraBinsStr(), false);
        this.receiveNoExtraItemsBln = cText.pStringToBooleanBln(this.intakeorderEntity.getReceiveNoExtraItemsStr(), false);
        this.receiveNoExtraPiecesBln = cText.pStringToBooleanBln(this.intakeorderEntity.getReceiveNoExtraPiecesStr(), false);
        this.receiveMatAutoSplitIncompleteLineBln = cText.pStringToBooleanBln(this.intakeorderEntity.getReceiveMatAutoSplitIncompleteLineStr(), false);

        this.receivedDateTime = this.intakeorderEntity.getReceivedDateTime();
        this.isProcessingOrParkedBln = this.intakeorderEntity.getIsProcessingOrParkedStr();
    }

    //End Region Constructor

    public boolean pInsertInDatabaseBln() {
        cIntakeorder.getIntakeorderViewModel().insert(this.intakeorderEntity);
        this.indatabaseBln = true;

        if (cIntakeorder.allIntakeordersObl == null) {
            cIntakeorder.allIntakeordersObl = new ArrayList<>();
        }
        cIntakeorder.allIntakeordersObl.add(this);
        return true;
    }

    public static boolean pGetIntakeOrdersViaWebserviceBln(Boolean pvRefreshBln, String pvSearchTextStr) {

        if (pvRefreshBln == true) {
            cIntakeorder.allIntakeordersObl = null;
            cIntakeorder.mTruncateTableBln();
        }

        cWebresult WebResult;
        WebResult = cIntakeorder.getIntakeorderViewModel().pGetIntakeordersFromWebserviceWrs(pvSearchTextStr);
        if (WebResult.getResultBln() == true && WebResult.getSuccessBln() == true) {

            List<JSONObject> myList = WebResult.getResultDtt();
            for (int i = 0; i < myList.size(); i++) {
                JSONObject jsonObject;
                jsonObject = myList.get(i);

                cIntakeorder intakeorder = new cIntakeorder(jsonObject);
                intakeorder.pInsertInDatabaseBln();
            }
            return true;
        } else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETINTAKEORDERS);
            return false;
        }
    }

    public static List<cIntakeorder> pGetIntakesWithFilterFromDatabasObl() {

        List<cIntakeorder> resultObl = new ArrayList<>();
        List<cIntakeorderEntity> hulpResultObl;

        hulpResultObl =  cIntakeorder.getIntakeorderViewModel().pGetIntakeordersFromDatabaseWithFilterObl(cUser.currentUser.getNameStr(), cSharedPreferences.userFilterBln());
        if (hulpResultObl == null || hulpResultObl.size() == 0) {
            return  resultObl;
        }

        for (cIntakeorderEntity intakeorderEntity : hulpResultObl ) {
            cIntakeorder intakeorder = new cIntakeorder(intakeorderEntity);
            resultObl.add(intakeorder);
        }

        return  resultObl;
    }

    public List<cIntakeorderMATLine> pGetLinesFromDatabasObl() {

        List<cIntakeorderMATLine> resultObl = new ArrayList<>();
        List<cIntakeorderMATLineEntity> hulpResultObl;

        hulpResultObl =  cIntakeorder.getIntakeorderViewModel().pGetAllLinesFromDatabaseObl();
        if (hulpResultObl == null || hulpResultObl.size() == 0) {
            return  resultObl;
        }

        for (cIntakeorderMATLineEntity intakeorderMATLineEntity : hulpResultObl ) {
            cIntakeorderMATLine IntakeorderMATLine = new cIntakeorderMATLine(intakeorderMATLineEntity);
            resultObl.add(IntakeorderMATLine);
        }
        return  resultObl;
    }

    public List<cIntakeorderMATLine> pGetIntakeorderMATLinesToSendFromDatabaseObl() {

        List<cIntakeorderMATLine> resultObl = new ArrayList<>();
        List<cIntakeorderMATLineEntity> hulpResultObl;

        hulpResultObl =  cIntakeorder.getIntakeorderViewModel().pGetIntakeorderMATLinesToSendFromDatabaseObl();
        if (hulpResultObl == null || hulpResultObl.size() == 0) {
            return  resultObl;
        }

        for (cIntakeorderMATLineEntity intakeorderMATLineEntity : hulpResultObl ) {
            cIntakeorderMATLine intakeorderMATLine = new cIntakeorderMATLine(intakeorderMATLineEntity);
            resultObl.add(intakeorderMATLine);
        }
        return  resultObl;
    }

    public List<cIntakeorderMATLine> pGetLinesNotHandledFromDatabasObl() {

        List<cIntakeorderMATLine> resultObl = new ArrayList<>();
        List<cIntakeorderMATLineEntity> hulpResultObl;

        hulpResultObl =  cIntakeorder.getIntakeorderViewModel().pGetIntakeorderMATLinesNotHandledFromDatabaseObl();
        if (hulpResultObl == null || hulpResultObl.size() == 0) {
            return  resultObl;
        }

        for (cIntakeorderMATLineEntity intakeorderMATLineEntity : hulpResultObl ) {
            cIntakeorderMATLine intakeorderMATLine = new cIntakeorderMATLine(intakeorderMATLineEntity);
            resultObl.add(intakeorderMATLine);
        }
        return  resultObl;
    }

    public List<cIntakeorderMATLine> pGetLinesBusyFromDatabasObl() {

        List<cIntakeorderMATLine> resultObl = new ArrayList<>();
        List<cIntakeorderMATLineEntity> hulpResultObl;

        hulpResultObl =  cIntakeorder.getIntakeorderViewModel().pGetIntakeorderMATLinesBusyFromDatabaseObl();
        if (hulpResultObl == null || hulpResultObl.size() == 0) {
            return  resultObl;
        }

        for (cIntakeorderMATLineEntity intakeorderMATLineEntity : hulpResultObl ) {
            cIntakeorderMATLine intakeorderMATLine = new cIntakeorderMATLine(intakeorderMATLineEntity);
            resultObl.add(intakeorderMATLine);
        }
        return  resultObl;
    }

    public List<cIntakeorderMATLine> pGetLinesHandledFromDatabasObl() {

        List<cIntakeorderMATLine> resultObl = new ArrayList<>();
        List<cIntakeorderMATLineEntity> hulpResultObl;

        hulpResultObl =  cIntakeorder.getIntakeorderViewModel().pGetIntakeorderMATLinesHandledFromDatabaseObl();
        if (hulpResultObl == null || hulpResultObl.size() == 0) {
            return  resultObl;
        }

        for (cIntakeorderMATLineEntity intakeorderMATLineEntity : hulpResultObl ) {
            cIntakeorderMATLine intakeorderMATLine = new cIntakeorderMATLine(intakeorderMATLineEntity);
            resultObl.add(intakeorderMATLine);
        }
        return  resultObl;
    }

    public List<cIntakeorderMATLine> pGetLinesToSendFromDatabasObl() {

        List<cIntakeorderMATLine> resultObl = new ArrayList<>();
        List<cIntakeorderMATLineEntity> hulpResultObl;

        hulpResultObl =  cIntakeorder.getIntakeorderViewModel().pGetIntakeorderMATLinesToSendFromDatabaseObl();
        if (hulpResultObl == null || hulpResultObl.size() == 0) {
            return  resultObl;
        }

        for (cIntakeorderMATLineEntity intakeorderMATLineEntity : hulpResultObl ) {
            cIntakeorderMATLine intakeorderMATLine = new cIntakeorderMATLine(intakeorderMATLineEntity);
            resultObl.add(intakeorderMATLine);
        }
        return  resultObl;
    }

    public List<cIntakeorderMATLine> pGetLinesForBinObl(String pvBinCodeStr) {

        List <cIntakeorderMATLine> hulpObl = this.pGetLinesFromDatabasObl();
        List <cIntakeorderMATLine> resultObl = new ArrayList<>();

        if (hulpObl == null || hulpObl.size() ==0 ) {
            return  null;
        }

        for (cIntakeorderMATLine intakeorderMATLine : hulpObl) {

            if (intakeorderMATLine.getBinCodeStr().equalsIgnoreCase(pvBinCodeStr) || intakeorderMATLine.getBinCodeHandledStr().equalsIgnoreCase(pvBinCodeStr)) {
                resultObl.add(intakeorderMATLine);
            }
        }

        return  null;

    }

    public cResult pHandledViaWebserviceRst() {

        cResult result;
        result = new cResult();
        result.resultBln = true;

        cWebresult webresult;


        webresult =  cIntakeorder.getIntakeorderViewModel().pHandledViaWebserviceWrs();

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

            cIntakeorder.currentIntakeOrder.pGetCommentsViaWebErrorBln(webresult.getResultDtt());
            return result;
        }

        return  result;

    }

    public List<cComment> pCommentObl(){

        if (this.commentsObl() == null || this.commentsObl().size() == 0) {
            return  null;
        }

        List<cComment> hulpObl = cComment.pGetCommentsForTypeObl(cWarehouseorder.CommentTypeEnu.RECEIVE);
        return hulpObl;

    }

    public List<cComment> pFeedbackCommentObl(){

        if (this.commentsObl() == null || this.commentsObl().size() == 0) {
            return  null;
        }

        List<cComment> hulpObl = cComment.pGetCommentsForTypeObl(cWarehouseorder.CommentTypeEnu.FEEDBACK);
        return hulpObl;

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

        Webresult = cWarehouseorder.getWarehouseorderViewModel().pLockWarehouseopdrachtViaWebserviceWrs(cWarehouseorder.OrderTypeEnu.ONTVANGST.toString(), this.getOrderNumberStr(), cDeviceInfo.getSerialnumberStr(), pvStepCodeEnu.toString(), pvWorkFlowStepInt, ignoreBusyBln);

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

            //TODO WTF is this?
            //cIntakeorder.currentIntakeOrder.pGetCommentsViaWebErrorBln(Webresult.getResultDtt());
            return result;
        }

        return result;

    }

    public boolean pLockReleaseViaWebserviceBln() {

        cWebresult Webresult;

        Webresult = cWarehouseorder.getWarehouseorderViewModel().pLockReleaseWarehouseorderViaWebserviceWrs(cWarehouseorder.OrderTypeEnu.ONTVANGST.toString(), this.getOrderNumberStr(), cDeviceInfo.getSerialnumberStr(), cWarehouseorder.StepCodeEnu.Receive_Store.toString(), cWarehouseorder.WorkflowReceiveStoreStepEnu.Receive_StoreBezig);

        if (Webresult.getSuccessBln() == false || Webresult.getResultBln() == false) {
            return false;
        }
        return true;
    }

    public boolean pGetCommentsViaWebserviceBln(Boolean pvRefeshBln) {
        if (pvRefeshBln == true) {
            cComment.allCommentsObl = null;
            cComment.pTruncateTableBln();
            cComment.commentsShownBln = false;
        }

        cWebresult webresult;
        webresult = cIntakeorder.getIntakeorderViewModel().pGetCommentsFromWebserviceWrs();
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

    public double pQuantityNotHandledDbl() {
        Double resultDbl =  cIntakeorder.getIntakeorderViewModel().pQuantityNotHandledDbl();
        if (resultDbl == null) {
            resultDbl  = 0.0;
        }

        return  resultDbl;

    }

    public double pQuantityHandledDbl() {

        Double resultDbl =cIntakeorder.getIntakeorderViewModel().pQuantityHandledDbl();

        if (resultDbl == null) {
            resultDbl  = 0.0;
        }

        return  resultDbl;
    }

    public double pQuantityTotalDbl() {
        return  cIntakeorder.getIntakeorderViewModel().pGetQuantityTotalDbl();
    }

    public int pGetIndexOfNotHandledLineInt(cIntakeorderMATLine pvIntakeorderMATLine) {

        int resultInt = -1;

        if (this.pGetLinesNotHandledFromDatabasObl() == null || this.pGetLinesNotHandledFromDatabasObl().size() == 0) {
            return  resultInt;
        }


        int indexInt = -1;

        for (cIntakeorderMATLine intakeorderMATLine : this.pGetLinesNotHandledFromDatabasObl()) {

            indexInt += 1;

            if (cText.pIntToStringStr(intakeorderMATLine.getLineNoInt()).equalsIgnoreCase(cText.pIntToStringStr(pvIntakeorderMATLine.getLineNoInt()))  ) {

                resultInt =  indexInt;
                break;
            }
        }

        return  resultInt;

    }

    public boolean pDeleteDetailsBln(){

        cIntakeorderBarcode.pTruncateTableBln();
        cIntakeorderMATLineBarcode.pTruncateTableBln();
        cIntakeorderMATLine.pTruncateTableBln();

        return  true;
    }

    public boolean pGetMATLineBarcodesViaWebserviceBln(Boolean pvRefreshBln) {

        if (pvRefreshBln == true) {
            cIntakeorderMATLineBarcode.allMATLineBarcodesObl = null;
            cIntakeorderMATLineBarcode.pTruncateTableBln();
        }

        cWebresult WebResult;
        WebResult = cIntakeorder.getIntakeorderViewModel().pGetIntakeorderMATLineBarcodesFromWebserviceWrs();
        if (WebResult.getResultBln() == true && WebResult.getSuccessBln() == true) {

            List<JSONObject> myList = WebResult.getResultDtt();
            for (int i = 0; i < myList.size(); i++) {
                JSONObject jsonObject;
                jsonObject = myList.get(i);

                cIntakeorderMATLineBarcode cIntakeorderMATLineBarcode = new cIntakeorderMATLineBarcode(jsonObject);
                cIntakeorderMATLineBarcode.pInsertInDatabaseBln();
            }
            return true;
        } else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETINTAKEORDERMATLINES);
            return false;
        }
    }

    public boolean pGetMATLinesViaWebserviceBln(Boolean pvRefreshBln) {

        if (pvRefreshBln == true) {
            cIntakeorderMATLine.allIntakeorderMATLinesObl = null;
            cIntakeorderMATSummaryLine.allIntakeorderMATSummaryLinesObl = null;
            cIntakeorderMATLine.pTruncateTableBln();
        }

        cWebresult WebResult;
        WebResult = cIntakeorder.getIntakeorderViewModel().pGetIntakeorderMATLinesFromWebserviceWrs();
        if (WebResult.getResultBln() == true && WebResult.getSuccessBln() == true) {

            List<JSONObject> myList = WebResult.getResultDtt();
            for (int i = 0; i < myList.size(); i++) {
                JSONObject jsonObject;
                jsonObject = myList.get(i);

                cIntakeorderMATLine intakeorderMATLine = new cIntakeorderMATLine(jsonObject);
                intakeorderMATLine.pInsertInDatabaseBln();

                if (intakeorderMATLine.getBinCodeStr().isEmpty() && intakeorderMATLine.getQuantityDbl() > 0) {
                    this.receiveMATEmptyBinsBln = true;
                }

                cIntakeorderMATSummaryLine intakeorderMATSummaryLine = cIntakeorderMATSummaryLine.pGetSummaryLine(intakeorderMATLine.getItemNoStr(),
                                                                                                                  intakeorderMATLine.getVariantCodeStr(),
                                                                                                                  intakeorderMATLine.getSourceNoStr());

                if (intakeorderMATSummaryLine == null ) {
                    cIntakeorderMATSummaryLine summaryLineToAdd = new cIntakeorderMATSummaryLine(intakeorderMATLine.getItemNoStr(),
                                                                                                 intakeorderMATLine.getVariantCodeStr(),
                                                                                                 intakeorderMATLine.getSourceNoStr());


                    cIntakeorderMATSummaryLine.pAddSummaryLine(summaryLineToAdd);
                    summaryLineToAdd.pAddMATLine(intakeorderMATLine);
                    continue;
                }

                intakeorderMATSummaryLine.pAddMATLine(intakeorderMATLine);
            }

            return true;

        } else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETINTAKEORDERMATLINES);
            return false;
        }
    }

    public boolean pGetBarcodesViaWebserviceBln(Boolean pvRefreshBln) {

        if (pvRefreshBln == true) {
            cIntakeorderBarcode.allBarcodesObl = null;
            cIntakeorderBarcode.pTruncateTableBln();
        }

        cWebresult WebResult;
        WebResult = cIntakeorder.getIntakeorderViewModel().pGetIntakeorderMATBarcodesFromWebserviceWrs();
        if (WebResult.getResultBln() == true && WebResult.getSuccessBln() == true) {

            List<JSONObject> myList = WebResult.getResultDtt();
            for (int i = 0; i < myList.size(); i++) {
                JSONObject jsonObject;
                jsonObject = myList.get(i);

                cIntakeorderBarcode intakeorderBarcode = new cIntakeorderBarcode(jsonObject);
                intakeorderBarcode.pInsertInDatabaseBln();
            }
            return true;
        } else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETINTAKEORDERMATLINES);
            return false;
        }
    }

    public cIntakeorderBarcode pGetOrderBarcode(cBarcodeScan pvBarcodescan) {

        if (this.barcodesObl() == null || this.barcodesObl().size() == 0)  {
            return  null;
        }

        for (cIntakeorderBarcode intakeorderBarcode : this.barcodesObl()) {

            if (intakeorderBarcode.getBarcodeStr().equalsIgnoreCase(pvBarcodescan.getBarcodeOriginalStr()) ||
                intakeorderBarcode.getBarcodeWithoutCheckDigitStr().equalsIgnoreCase(pvBarcodescan.getBarcodeFormattedStr())) {
                return  intakeorderBarcode;
            }
        }

        return  null;

    }



    public boolean pCheckItemVariantBln(String pvItemNoStr, String pvVariantCodeStr) {

        if (this.barcodesObl() == null || this.barcodesObl().size() == 0)  {
            return  false;
        }

        for (cIntakeorderBarcode intakeorderBarcode : this.barcodesObl()) {

            if (intakeorderBarcode.getItemNoStr().equalsIgnoreCase(pvItemNoStr) && intakeorderBarcode.getVariantCodeStr().equalsIgnoreCase(pvVariantCodeStr)) {
                return  true;
            }
        }

        return  false;
    }

    public boolean pCheckLinesForBinAndItemVariantBln(String pvBinCodeStr, String pvItemNoStr, String pvVariantCodeStr) {


        List<cIntakeorderMATLine> hulpObl = this.pGetLinesFromDatabasObl();

        if (hulpObl== null || hulpObl.size() == 0)  {
            return  false;
        }

        for (cIntakeorderMATLine intakeorderMATLine : hulpObl) {

            if (intakeorderMATLine.getItemNoStr().equalsIgnoreCase(pvItemNoStr) && intakeorderMATLine.getVariantCodeStr().equalsIgnoreCase(pvVariantCodeStr) && intakeorderMATLine.getBinCodeStr().equalsIgnoreCase(pvBinCodeStr)) {
                return  true;
            }
        }

        return  false;

    }

    private static boolean mTruncateTableBln() {
        cIntakeorder.getIntakeorderViewModel().deleteAll();
        return true;
    }


}
