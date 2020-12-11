package SSU_WHS.Intake.Intakeorders;

import androidx.lifecycle.ViewModelProvider;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cDeviceInfo;
import ICS.Utils.cResult;
import ICS.Utils.cSharedPreferences;
import ICS.Utils.cText;
import ICS.Weberror.cWeberror;
import ICS.cAppExtension;
import SSU_WHS.Basics.Article.cArticle;
import SSU_WHS.Basics.BranchBin.cBranchBin;
import SSU_WHS.Basics.Packaging.cPackaging;
import SSU_WHS.Basics.Scanners.cScanner;
import SSU_WHS.Basics.Users.cUser;
import SSU_WHS.General.Comments.cComment;
import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.General.Warehouseorder.cWarehouseorderViewModel;
import SSU_WHS.Intake.IntakeorderBarcodes.cIntakeorderBarcode;
import SSU_WHS.Intake.IntakeorderMATLineBarcodes.cIntakeorderMATLineBarcode;
import SSU_WHS.Intake.IntakeorderMATLineSummary.cIntakeorderMATSummaryLine;
import SSU_WHS.Intake.IntakeorderMATLines.cIntakeorderMATLine;
import SSU_WHS.Receive.ReceiveLines.cReceiveorderLine;
import SSU_WHS.Receive.ReceiveLines.cReceiveorderLineViewModel;
import SSU_WHS.Receive.ReceiveSummaryLine.cReceiveorderSummaryLine;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebserviceDefinitions;
import nl.icsvertex.scansuite.R;

public class cIntakeorder {

    private String ordernumberStr;
    public String getOrderNumberStr() {return this.ordernumberStr;}

    private String ordertypeStr;
    public String getOrderTypeStr() {return this.ordertypeStr;}

    private String assignedUserIdStr;
    public String getAssignedUserIdStr() {return this.assignedUserIdStr;}

    private String currentUserIdStr;
    public String getCurrentUserIdStr() {return this.currentUserIdStr;}

    private int statusInt;
    public int getStatusInt() {return this.statusInt;}

    private String binCodeStr;
    public String getBinCodeStr() {return this.binCodeStr;}

    private String externalReferenceStr;
    public String getExternalReferenceStr() {return this.externalReferenceStr;}

    private String documentStr;
    public  String getDocumentStr(){ return this.documentStr;
    }

    private int sourceDocumentInt;
    private int getSourceDocumentInt(){return  this.sourceDocumentInt;}

    private boolean receiveAmountManualEOBln;
    public boolean getReceiveAmountManualEOBln() {return this.receiveAmountManualEOBln;}

    private boolean receiveStoreAutoAcceptAtRequestedBln;
    public boolean getReceiveStoreAutoAcceptAtRequestedBln() {return this.receiveStoreAutoAcceptAtRequestedBln;}

    private boolean receiveNoExtraBinsBln;
    public boolean getReceiveNoExtraBinsBln() {return this.receiveNoExtraBinsBln;}

    private boolean receiveNoExtraItemsBln;
    public boolean getReceiveNoExtraItemsBln() {return this.receiveNoExtraItemsBln;}

    private boolean receiveNoExtraPiecesBln;
    public boolean getReceiveNoExtraPiecesBln() {return this.receiveNoExtraPiecesBln;}

    public   boolean isBINScanPossible(){
        return this.getReceiveNoExtraBinsBln() && this.getReceiveNoExtraItemsBln();

    }

    private String receivedDateTime;
    public String getReceivedDateTime() {return this.receivedDateTime;}

    private Boolean isProcessingOrParkedBln;
    public Boolean getProcessingOrParkedBln() {
        return isProcessingOrParkedBln;
    }

    private boolean receiveWithPictureBln;
    public boolean isReceiveWithPictureBln() {
        return receiveWithPictureBln;
    }

    private boolean receiveWithAutoOpenBln;
    public boolean isReceiveWithAutoOpenBln() {
        return receiveWithAutoOpenBln;
    }

    private boolean receiveIntakeEOPackagingIntakeBln;
    public boolean isReceiveIntakeEOPackagingIntakeBln() {
        return receiveIntakeEOPackagingIntakeBln;
    }

    private boolean receiveIntakeEOPackagingshippedBln;
    public boolean isReceiveIntakeEOPackagingShippedBln() {
        return receiveIntakeEOPackagingshippedBln;
    }

    public  boolean isGenerated(){
        return this.getSourceDocumentInt() == cWarehouseorder.SourceDocumentTypeEnu.Generated;
    }

    private cIntakeorderEntity intakeorderEntity;

    private cWarehouseorderViewModel getWarehouseorderViewModel() {
        return new ViewModelProvider(cAppExtension.fragmentActivity).get(cWarehouseorderViewModel.class);
    }

    private cIntakeorderViewModel getIntakeorderViewModel() {
        return new ViewModelProvider(cAppExtension.fragmentActivity).get(cIntakeorderViewModel.class);
    }

    private cReceiveorderLineViewModel getReceiveorderLineViewModel() {
        return new ViewModelProvider(cAppExtension.fragmentActivity).get(cReceiveorderLineViewModel.class);
    }

    public static List<cIntakeorder> allIntakeordersObl;
    public static cIntakeorder currentIntakeOrder;

    private int unknownVariantCounterInt = 0;
    public int getUnknownVariantCounterInt() {
        return unknownVariantCounterInt;
    }

    public List<cIntakeorderMATSummaryLine> summaryMATLinesObl(){
        return  cIntakeorderMATSummaryLine.sortedMATSummaryLinesObl();
    }

    public List<cIntakeorderMATLine> linesMATObl(){
        return  cIntakeorderMATLine.allIntakeorderMATLinesObl;
    }

    public List<cIntakeorderMATLine> linesHandledMATForBinObl(String pvBinCodeStr){

        List<cIntakeorderMATLine> resultObl = new ArrayList<>();

        if ( cIntakeorderMATLine.allIntakeorderMATLinesObl == null) {
            return  resultObl;
        }

        for (cIntakeorderMATLine intakeorderMATLine : cIntakeorderMATLine.allIntakeorderMATLinesObl) {

            if (intakeorderMATLine.getBinCodeStr().equalsIgnoreCase(pvBinCodeStr) && intakeorderMATLine.getQuantityHandledDbl() > 0) {
                resultObl.add(intakeorderMATLine);
            }

        }

        if (resultObl.size() > 1) {
            Collections.reverse(resultObl);
        }

        return  resultObl;
    }

    public List<cReceiveorderSummaryLine> summaryReceiveLinesObl(){
        return  cReceiveorderSummaryLine.allReceiveorderSummaryLinesObl;
    }

    private List<cComment> commentsObl(){
        return  cComment.allCommentsObl;
    }
    public List<cIntakeorderBarcode> barcodesObl () {return  cIntakeorderBarcode.allBarcodesObl;}
    public List<cPackaging> packagingInObl;
    public List<cPackaging> packagingOutObl;

    public cIntakeorderBarcode intakeorderBarcodeScanned;

    public List<String> sourceNoObl;
    public boolean showDeviationsBln = true;
    public cBranchBin currentBin;

    //Region Constructor

    public cIntakeorder(JSONObject pvJsonObject) {

        this.intakeorderEntity = new cIntakeorderEntity(pvJsonObject);
        this.ordernumberStr = this.intakeorderEntity.getOrdernumberStr();
        this.ordertypeStr = this.intakeorderEntity.getOrderTypeStr();

        this.assignedUserIdStr = this.intakeorderEntity.getAssignedUserIdStr();
        this.currentUserIdStr = this.intakeorderEntity.getCurrentUserIdStr();

        this.statusInt = this.intakeorderEntity.getStatusInt();
        this.binCodeStr = this.intakeorderEntity.getBinCodeStr();
        this.externalReferenceStr = this.intakeorderEntity.getExternalReferenceStr();
        this.documentStr = this.intakeorderEntity.getDocumentStr();

        this.receiveAmountManualEOBln = cText.pStringToBooleanBln(this.intakeorderEntity.getReceiveAmountManualEOStr(), false);

        this.receiveStoreAutoAcceptAtRequestedBln = cText.pStringToBooleanBln(this.intakeorderEntity.getReceiveStoreAutoAcceptAtRequestedStr(), false);


        this.receivedDateTime = this.intakeorderEntity.getReceivedDateTime();
        this.isProcessingOrParkedBln = this.intakeorderEntity.getIsProcessingOrParkedStr();

        this.sourceDocumentInt = this.intakeorderEntity.getSourceDocumentInt();

        this.receiveWithPictureBln = cText.pStringToBooleanBln(this.intakeorderEntity.getReceiveWithPictureStr(),false);
        this.receiveWithAutoOpenBln = cText.pStringToBooleanBln(this.intakeorderEntity.getReceiveWithPictureAutoOpenStr(),false);
        this.receiveNoExtraBinsBln = cText.pStringToBooleanBln(this.intakeorderEntity.getReceiveNoExtraBinsStr(), false);
        this.receiveNoExtraItemsBln = cText.pStringToBooleanBln(this.intakeorderEntity.getReceiveNoExtraItemsStr(), false);
        this.receiveNoExtraPiecesBln = cText.pStringToBooleanBln(this.intakeorderEntity.getReceiveNoExtraPiecesStr(), false);
        this.receiveIntakeEOPackagingIntakeBln = cText.pStringToBooleanBln(this.intakeorderEntity.getReceiveIntakeEOPackagingIntake(), false);
        this.receiveIntakeEOPackagingshippedBln = cText.pStringToBooleanBln(this.intakeorderEntity.getReceiveIntakeEOPackagingshipped(), false);
    }

    public cIntakeorder(cIntakeorderEntity pvIntakeorderEntity) {

        this.intakeorderEntity = pvIntakeorderEntity;
        this.ordernumberStr = this.intakeorderEntity.getOrdernumberStr();
        this.ordertypeStr = this.intakeorderEntity.getOrderTypeStr();

        this.assignedUserIdStr = this.intakeorderEntity.getAssignedUserIdStr();
        this.currentUserIdStr = this.intakeorderEntity.getCurrentUserIdStr();
        this.statusInt = this.intakeorderEntity.getStatusInt();
        this.binCodeStr = this.intakeorderEntity.getBinCodeStr();
        this.externalReferenceStr = this.intakeorderEntity.getExternalReferenceStr();
        this.documentStr = this.intakeorderEntity.getDocumentStr();

        this.receiveAmountManualEOBln = cText.pStringToBooleanBln(this.intakeorderEntity.getReceiveAmountManualEOStr(), false);

        this.receiveWithPictureBln = cText.pStringToBooleanBln(this.intakeorderEntity.getReceiveWithPictureStr(),false);
        this.receiveWithAutoOpenBln = cText.pStringToBooleanBln(this.intakeorderEntity.getReceiveWithPictureAutoOpenStr(),false);
        this.receiveStoreAutoAcceptAtRequestedBln = cText.pStringToBooleanBln(this.intakeorderEntity.getReceiveStoreAutoAcceptAtRequestedStr(), false);
        this.receiveNoExtraBinsBln = cText.pStringToBooleanBln(this.intakeorderEntity.getReceiveNoExtraBinsStr(), false);
        this.receiveNoExtraItemsBln = cText.pStringToBooleanBln(this.intakeorderEntity.getReceiveNoExtraItemsStr(), false);
        this.receiveNoExtraPiecesBln = cText.pStringToBooleanBln(this.intakeorderEntity.getReceiveNoExtraPiecesStr(), false);
        this.receiveIntakeEOPackagingIntakeBln = cText.pStringToBooleanBln(this.intakeorderEntity.getReceiveIntakeEOPackagingIntake(), false);
        this.receiveIntakeEOPackagingshippedBln = cText.pStringToBooleanBln(this.intakeorderEntity.getReceiveIntakeEOPackagingshipped(), false);

        this.receivedDateTime = this.intakeorderEntity.getReceivedDateTime();
        this.isProcessingOrParkedBln = this.intakeorderEntity.getIsProcessingOrParkedStr();


        this.sourceDocumentInt = this.intakeorderEntity.getSourceDocumentInt();

    }

    //End Region Constructor

    public boolean pInsertInDatabaseBln() {
        this.getIntakeorderViewModel().insert(this.intakeorderEntity);

        if (cIntakeorder.allIntakeordersObl == null) {
            cIntakeorder.allIntakeordersObl = new ArrayList<>();
        }
        cIntakeorder.allIntakeordersObl.add(this);
        return true;
    }

    public static boolean pGetIntakeOrdersViaWebserviceBln(Boolean pvRefreshBln, String pvSearchTextStr) {

        if (pvRefreshBln) {
            cIntakeorder.allIntakeordersObl = null;
            cIntakeorder.mTruncateTable();
        }

        cWebresult WebResult;

        cIntakeorderViewModel intakeorderViewModel =  new ViewModelProvider(cAppExtension.fragmentActivity).get(cIntakeorderViewModel.class);
        WebResult = intakeorderViewModel.pGetIntakeordersFromWebserviceWrs(pvSearchTextStr);
        if (WebResult.getResultBln() && WebResult.getSuccessBln()) {
            for (JSONObject jsonObject : WebResult.getResultDtt()) {

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

        cIntakeorderViewModel intakeorderViewModel =  new ViewModelProvider(cAppExtension.fragmentActivity).get(cIntakeorderViewModel.class);
        hulpResultObl =  intakeorderViewModel.pGetIntakeordersFromDatabaseWithFilterObl(cUser.currentUser.getUsernameStr(), cSharedPreferences.userFilterBln());
        if (hulpResultObl == null || hulpResultObl.size() == 0) {
            return  resultObl;
        }

        for (cIntakeorderEntity intakeorderEntity : hulpResultObl ) {
            cIntakeorder intakeorder = new cIntakeorder(intakeorderEntity);
            resultObl.add(intakeorder);
        }

        return  resultObl;
    }

    public cResult pInvalidateViaWebserviceRst() {

        cResult result;
        result = new cResult();
        result.resultBln = true;

        cWebresult webresult;


        webresult =  this.getIntakeorderViewModel().pInvalidateViaWebserviceWrs();
        //Everything was fine, so we are done
        if (webresult.getSuccessBln() && webresult.getResultBln()) {

            result.resultBln = true;
            return result;
        }

        //Something really went wrong
        if (!webresult.getSuccessBln()) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_could_not_invalidate_order));
            return result;
        }

        return  result;

    }

    public cResult pPackagingHandledViaWebserviceRst() {

        cResult result;
        result = new cResult();
        result.resultBln = true;

        cWebresult webresult;

        webresult =  this.getIntakeorderViewModel().pPackagingHandledViaWebserviceWrs();

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

            cIntakeorder.currentIntakeOrder.mGetCommentsViaWebError(webresult.getResultDtt());
            return result;
        }

        return  result;

    }

    public cResult pMATHandledViaWebserviceRst() {

        cResult result;
        result = new cResult();
        result.resultBln = true;

        cWebresult webresult;


        webresult =  this.getIntakeorderViewModel().pMATHandledViaWebserviceWrs();

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

            cIntakeorder.currentIntakeOrder.mGetCommentsViaWebError(webresult.getResultDtt());
            return result;
        }

        return  result;

    }

    public cResult pReceiveHandledViaWebserviceRst() {

        cResult result;
        result = new cResult();
        result.resultBln = true;

        cWebresult webresult;


        webresult =  this.getIntakeorderViewModel().pReceiveHandledViaWebserviceWrs();

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

            cIntakeorder.currentIntakeOrder.mGetCommentsViaWebError(webresult.getResultDtt());
            return result;
        }

        return  result;

    }

    public List<cComment> pCommentObl(){

        if (this.commentsObl() == null || this.commentsObl().size() == 0) {
            return  null;
        }

        return cComment.pGetCommentsForTypeObl(cWarehouseorder.CommentTypeEnu.RECEIVE);

    }

    public List<cComment> pFeedbackCommentObl(){

        if (this.commentsObl() == null || this.commentsObl().size() == 0) {
            return  null;
        }

        return cComment.pGetCommentsForTypeObl(cWarehouseorder.CommentTypeEnu.FEEDBACK);

    }

    public cResult pLockViaWebserviceRst()        {

        //Initialise result
        cResult result;
        result = new cResult();
        result.resultBln = true;

        cWebresult Webresult;
        boolean ignoreBusyBln = false;

        cWarehouseorder.StepCodeEnu stepCodeEnu = cWarehouseorder.StepCodeEnu.Unkown;
        int workflowStepInt = 0;


        switch (this.getOrderTypeStr()){
            case "MAT":
            case "MAS":
                stepCodeEnu = cWarehouseorder.StepCodeEnu.Receive_Store;
                workflowStepInt = cWarehouseorder.WorkflowReceiveStoreStepEnu.Receive_Store;
                break;


            case "EOS":
                stepCodeEnu = cWarehouseorder.StepCodeEnu.Receive_InTake;
                workflowStepInt = cWarehouseorder.WorkflowExternalReceiveStepEnu.Receive_External;
                break;
        }


        if (this.getStatusInt() > 10 && cUser.currentUser.getUsernameStr().equalsIgnoreCase(this.getCurrentUserIdStr())) {
            ignoreBusyBln = true;
        }


        Webresult = this.getWarehouseorderViewModel().pLockWarehouseopdrachtViaWebserviceWrs(cWarehouseorder.OrderTypeEnu.ONTVANGST.toString(), this.getOrderNumberStr(), cDeviceInfo.getSerialnumberStr(), stepCodeEnu.toString(), workflowStepInt, ignoreBusyBln);

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
        if (!Webresult.getResultBln()&& Webresult.getResultLng() <= 0 && Webresult.getResultObl() != null &&
            Webresult.getResultObl().size() > 0 && !Webresult.getResultObl().get(0).equalsIgnoreCase(cUser.currentUser.getNameStr())) {
            result.resultBln = false;
            result.activityActionEnu = cWarehouseorder.ActivityActionEnu.Unknown;
            result.pAddErrorMessage(cAppExtension.context.getString((R.string.message_another_user_already_started)) + " " + Webresult.getResultObl().get(0));
            return result;
        }

        // We got a succesfull response, but we need to do something with this activity
        if (!Webresult.getResultBln() && Webresult.getResultLng() > 0) {

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
            //cIntakeorder.currentIntakeOrder.mGetCommentsViaWebError(Webresult.getResultDtt());
            return result;
        }

        return result;

    }

    public boolean pLockReleaseViaWebserviceBln() {

        cWebresult Webresult = null;

        if (cIntakeorder.currentIntakeOrder.getOrderTypeStr().equalsIgnoreCase("MAT") || cIntakeorder.currentIntakeOrder.getOrderTypeStr().equalsIgnoreCase("MAS")) {
            Webresult = this.getWarehouseorderViewModel().pLockReleaseWarehouseorderViaWebserviceWrs(cWarehouseorder.OrderTypeEnu.ONTVANGST.toString(), this.getOrderNumberStr(), cDeviceInfo.getSerialnumberStr(), cWarehouseorder.StepCodeEnu.Receive_Store.toString(), cWarehouseorder.WorkflowReceiveStoreStepEnu.Receive_Store);
        }

        //We try to release an order thats not created
        if (cIntakeorder.currentIntakeOrder.getOrderTypeStr().equalsIgnoreCase("EOR")){
            return  true;
        }

        if (cIntakeorder.currentIntakeOrder.getOrderTypeStr().equalsIgnoreCase("EOS")){
            Webresult = this.getWarehouseorderViewModel().pLockReleaseWarehouseorderViaWebserviceWrs(cWarehouseorder.OrderTypeEnu.ONTVANGST.toString(), this.getOrderNumberStr(), cDeviceInfo.getSerialnumberStr(), cWarehouseorder.StepCodeEnu.Receive_InTake.toString(), cWarehouseorder.WorkflowExternalReceiveStepEnu.Receive_External);
        }


        return Objects.requireNonNull(Webresult).getSuccessBln() && Webresult.getResultBln();
    }

    public boolean pGetCommentsViaWebserviceBln(Boolean pvRefeshBln) {
        if (pvRefeshBln) {
            cComment.allCommentsObl = null;
            cComment.pTruncateTableBln();
            cComment.commentsShownBln = false;
        }

        cWebresult webresult;
        webresult = this.getIntakeorderViewModel().pGetCommentsFromWebserviceWrs();
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

    public boolean pMatchBarcodesAndLinesBln() {

      if (cIntakeorderBarcode.allBarcodesObl == null || cIntakeorderBarcode.allBarcodesObl.size() == 0 ||
         cIntakeorderMATLineBarcode.allMATLineBarcodesObl == null || cIntakeorderMATLineBarcode.allMATLineBarcodesObl.size() == 0 ||
         cIntakeorderMATLine.allIntakeorderMATLinesObl == null ||  cIntakeorderMATLine.allIntakeorderMATLinesObl.size() == 0 )  {
          return true;
      }

      for (cIntakeorderMATLineBarcode intakeorderMATLineBarcode : cIntakeorderMATLineBarcode.allMATLineBarcodesObl) {

        cIntakeorderBarcode intakeorderBarcode =   cIntakeorderBarcode.pGetIntakeOrderBarcodeByBarcode(intakeorderMATLineBarcode.getBarcodeStr());
          if (intakeorderBarcode == null) {
              continue;
          }

        cIntakeorderMATLine intakeorderMATLine =   cIntakeorderMATLine.pGetLineByLineNo(intakeorderMATLineBarcode.getLineNoLng().intValue());
          if (intakeorderMATLine == null) {
              continue;
          }

          if (intakeorderMATLine.barcodesObl == null) {
              intakeorderMATLine.barcodesObl = new ArrayList<>();
          }

          intakeorderMATLine.barcodesObl.add(intakeorderBarcode);
      }

      return  true;
    }

    public double pQuantityHandledDbl() {

        Double resultDbl =this.getIntakeorderViewModel().pQuantityHandledDbl();

        if (resultDbl == null) {
            resultDbl  = 0.0;
        }

        return  resultDbl;
    }

    public boolean pDeleteDetailsBln(){

        cIntakeorderBarcode.pTruncateTableBln();
        cIntakeorderMATLineBarcode.pTruncateTableBln();
        cIntakeorderMATLine.pTruncateTableBln();

        return  true;
    }

    public boolean pGetBarcodesViaWebserviceBln(Boolean pvRefreshBln) {

        if (pvRefreshBln) {
            cIntakeorderBarcode.allBarcodesObl = null;
            cIntakeorderBarcode.pTruncateTableBln();
        }

        cWebresult WebResult;
        WebResult = this.getIntakeorderViewModel().pGetIntakeorderBarcodesFromWebserviceWrs();

        if (WebResult.getResultBln() && WebResult.getSuccessBln()) {

            for (JSONObject jsonObject : WebResult.getResultDtt()) {
                cIntakeorderBarcode intakeorderBarcode = new cIntakeorderBarcode(jsonObject);
                intakeorderBarcode.pInsertInDatabaseBln();
            }
            return true;
        } else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETINTAKEORDERMATLINES);
            return false;
        }
    }

    public boolean pReceiveAddUnkownBarcodeAndItemVariantBln(cBarcodeScan pvBarcodeScan) {

        cIntakeorder.currentIntakeOrder.unknownVariantCounterInt += 1;

        cWebresult WebResult;

        //First create Item Varianr
        WebResult =  this.getReceiveorderLineViewModel().pReceiveAddUnknownItemViaWebserviceWrs(pvBarcodeScan);
        if (WebResult.getResultBln()&& WebResult.getSuccessBln() ){

            if (WebResult.getResultDtt().size() == 1) {
                cReceiveorderSummaryLine receiveorderSummaryLine = new cReceiveorderSummaryLine(WebResult.getResultDtt().get(0));
                cReceiveorderSummaryLine.pAddSummaryLine(receiveorderSummaryLine);
                cReceiveorderSummaryLine.currentReceiveorderSummaryLine = receiveorderSummaryLine;
            }
        }

        else {
            cIntakeorder.currentIntakeOrder.unknownVariantCounterInt -= 1;
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_RECEIVEITEMVARIANTCREATE);
            return  false;
        }

        //Then add barcode
        WebResult =  this.getReceiveorderLineViewModel().pReceiveAddUnknownBarcodeViaWebserviceWrs(pvBarcodeScan);
        if (WebResult.getResultBln()&& WebResult.getSuccessBln() ){

            if (WebResult.getResultDtt().size() == 1) {
                cIntakeorderBarcode intakeorderBarcode = new cIntakeorderBarcode(WebResult.getResultDtt().get(0));

                if (cIntakeorderBarcode.allBarcodesObl == null) {
                    cIntakeorderBarcode.allBarcodesObl = new ArrayList<>();
                }

                cIntakeorderBarcode.allBarcodesObl.add(intakeorderBarcode);
                cIntakeorderBarcode.currentIntakeOrderBarcode = intakeorderBarcode;
                cIntakeorder.currentIntakeOrder.intakeorderBarcodeScanned = intakeorderBarcode;
            }
        }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_RECEIVEBARCODECREATE);
            return  false;
        }

        return  true;
    }

    public boolean pIntakeAddUnkownBarcodeAndItemVariantBln(cBarcodeScan pvBarcodeScan) {

        cIntakeorder.currentIntakeOrder.unknownVariantCounterInt += 1;

        cWebresult WebResult;

        //First create Item Varianr
        WebResult =  this.getReceiveorderLineViewModel().pIntakeAddUnknownItemViaWebserviceWrs(pvBarcodeScan);
        if (WebResult.getResultBln()&& WebResult.getSuccessBln() ){

            if (WebResult.getResultDtt().size() == 1) {
                cIntakeorderMATSummaryLine intakeorderMATSummaryLine = new cIntakeorderMATSummaryLine(WebResult.getResultDtt().get(0));
                cIntakeorderMATSummaryLine.pAddSummaryLine(intakeorderMATSummaryLine);
                cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine = intakeorderMATSummaryLine;
            }
        }

        else {
            cIntakeorder.currentIntakeOrder.unknownVariantCounterInt -= 1;
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_RECEIVEITEMVARIANTCREATE);
            return  false;
        }

        //Then add barcode
        WebResult =  this.getReceiveorderLineViewModel().pIntakeAddUnknownBarcodeViaWebserviceWrs(pvBarcodeScan, false);
        if (WebResult.getResultBln()&& WebResult.getSuccessBln() ){

            if (WebResult.getResultDtt().size() == 1) {
                cIntakeorderBarcode intakeorderBarcode = new cIntakeorderBarcode(WebResult.getResultDtt().get(0));

                if (cIntakeorderBarcode.allBarcodesObl == null) {
                    cIntakeorderBarcode.allBarcodesObl = new ArrayList<>();
                }

                cIntakeorderBarcode.allBarcodesObl.add(intakeorderBarcode);
                cIntakeorderBarcode.currentIntakeOrderBarcode = intakeorderBarcode;
                cIntakeorder.currentIntakeOrder.intakeorderBarcodeScanned = intakeorderBarcode;
            }
        }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_RECEIVEBARCODECREATE);
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

        cWebresult WebResult;


        if (cIntakeorder.currentIntakeOrder.getOrderTypeStr().equalsIgnoreCase("EOS")) {
            WebResult =  this.getReceiveorderLineViewModel().pAddERPItemViaWebserviceWrs(pvBarcodeScan);

            if (WebResult.getResultBln()&& WebResult.getSuccessBln() ){

                if (WebResult.getResultDtt().size() == 1) {
                    cReceiveorderSummaryLine receiveorderSummaryLine = new cReceiveorderSummaryLine(WebResult.getResultDtt().get(0));
                    cReceiveorderSummaryLine.pAddSummaryLine(receiveorderSummaryLine);
                    cReceiveorderSummaryLine.currentReceiveorderSummaryLine = receiveorderSummaryLine;
                }
            }

            else {
                cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_RECEIVEITEMVARIANTCREATE);
                return  false;
            }

            //Then add barcode
            WebResult =  this.getReceiveorderLineViewModel().pReceiveAddERPBarcodeViaWebserviceWrs(pvBarcodeScan);
            if (WebResult.getResultBln()&& WebResult.getSuccessBln() ){

                if (WebResult.getResultDtt().size() == 1) {
                    cIntakeorderBarcode intakeorderBarcode = new cIntakeorderBarcode(WebResult.getResultDtt().get(0));

                    if (cIntakeorderBarcode.allBarcodesObl == null) {
                        cIntakeorderBarcode.allBarcodesObl = new ArrayList<>();
                    }

                    cIntakeorderBarcode.allBarcodesObl.add(intakeorderBarcode);
                    cIntakeorderBarcode.currentIntakeOrderBarcode = intakeorderBarcode;
                    cIntakeorder.currentIntakeOrder.intakeorderBarcodeScanned = intakeorderBarcode;
                }
            }
            else {
                cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_RECEIVEBARCODECREATE);
                return  false;
            }
        }

        if (cIntakeorder.currentIntakeOrder.getOrderTypeStr().equalsIgnoreCase("MAS")) {
            WebResult =  this.getReceiveorderLineViewModel().pIntakeAddUnknownItemViaWebserviceWrs(pvBarcodeScan);

            if (WebResult.getResultBln()&& WebResult.getSuccessBln() ){

                if (WebResult.getResultDtt().size() == 1) {
                    cIntakeorderMATSummaryLine intakeorderMATSummaryLine = new cIntakeorderMATSummaryLine(WebResult.getResultDtt().get(0));
                    cIntakeorderMATSummaryLine.pAddSummaryLine(intakeorderMATSummaryLine);
                    cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine = intakeorderMATSummaryLine;
                }
            }

            else {
                cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_RECEIVEITEMVARIANTCREATE);
                return  false;
            }

            Boolean isUniqueBarcodeBln = false;

            if (cArticle.currentArticle.barcodesObl != null && cArticle.currentArticle.barcodesObl.get(0).getUniqueBarcodeBln()) {
                isUniqueBarcodeBln = true;
            }

            //Then add barcode
            WebResult =  this.getReceiveorderLineViewModel().pIntakeAddUnknownBarcodeViaWebserviceWrs(pvBarcodeScan, isUniqueBarcodeBln);

            if (WebResult.getResultBln()&& WebResult.getSuccessBln() ){

                if (WebResult.getResultDtt().size() == 1) {
                    cIntakeorderBarcode intakeorderBarcode = new cIntakeorderBarcode(WebResult.getResultDtt().get(0));

                    if (cIntakeorderBarcode.allBarcodesObl == null) {
                        cIntakeorderBarcode.allBarcodesObl = new ArrayList<>();
                    }

                    cIntakeorderBarcode.allBarcodesObl.add(intakeorderBarcode);
                    cIntakeorderBarcode.currentIntakeOrderBarcode = intakeorderBarcode;
                    cIntakeorder.currentIntakeOrder.intakeorderBarcodeScanned = intakeorderBarcode;
                }
            }
            else {
                cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_RECEIVEBARCODECREATE);
                return  false;
            }
        }


        return  true;
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

    public static cResult pCreateReceiveOrderViaWebserviceRst(String pvDocumentStr, String pvPackingSlipStr, String pvBinCodeStr, boolean pvCheckBarcodesBln) {


        cResult result;
        result = new cResult();
        result.resultBln = true;

        cWebresult WebResult;

        cIntakeorderViewModel intakeorderViewModel = new ViewModelProvider(cAppExtension.fragmentActivity).get(cIntakeorderViewModel.class);
        WebResult = intakeorderViewModel.pCreateReceiverOderViaWebserviceWrs(pvDocumentStr, pvPackingSlipStr, pvBinCodeStr, pvCheckBarcodesBln);

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

            cIntakeorder intakeorder = new cIntakeorder(WebResult.getResultDtt().get(0));
            intakeorder.pInsertInDatabaseBln();
            cIntakeorder.currentIntakeOrder = intakeorder;



            return result;


        } else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_RECEIVECREATE);
            result.resultBln = false;
            return result;
        }

     }

    public static cResult pCreateIntakeOrderViaWebserviceRst(String pvDocumentStr,boolean pvCheckBarcodesBln) {


        cResult result;
        result = new cResult();
        result.resultBln = true;

        cWebresult WebResult;

        cIntakeorderViewModel intakeorderViewModel = new ViewModelProvider(cAppExtension.fragmentActivity).get(cIntakeorderViewModel.class);
        WebResult = intakeorderViewModel.pCreateIntakeOrderViaWebserviceWrs(pvDocumentStr,  pvCheckBarcodesBln);

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

            cIntakeorder intakeorder = new cIntakeorder(WebResult.getResultDtt().get(0));
            intakeorder.pInsertInDatabaseBln();
            cIntakeorder.currentIntakeOrder = intakeorder;



            return result;


        } else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_RECEIVECREATE);
            result.resultBln = false;
            return result;
        }

    }

    public  cResult pGetOrderDetailsRst(){


        if (this.getOrderTypeStr().equalsIgnoreCase("MAT") || this.getOrderTypeStr().equalsIgnoreCase("MAS")) {
            return  this.mGetMATOrderDetailsRst();
        }

        else {
            return  this.mGetReceiveOrderDetailsRst();
        }

     }

    private   cResult mGetReceiveOrderDetailsRst(){

        cResult result;

        result = new cResult();
        result.resultBln = true;

        //Get all Items for current order, if size = 0 or webservice error then stop
        if (!cIntakeorder.currentIntakeOrder.mGetReceiveItemsViaWebserviceBln(true)) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_intakelines_failed));
            return result;
        }

        int indexInt = 0;

        for (cScanner scanner : cScanner.allScannerObl ) {

            boolean refreshBln = false;

            if (indexInt == 0) {
                refreshBln = true;
            }

            //Get all linesInt for current order, if size = 0 or webservice error then stop
            if (!cIntakeorder.currentIntakeOrder.mGetReceiveLinesViaWebserviceBln(refreshBln, scanner.getScannerStr()) ) {
                result.resultBln = false;
                result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_intakelines_failed));
                return result;
            }

            indexInt += 1;

        }

        // Get all barcodes, if size =0 or webservice error then stop
        if (!cIntakeorder.currentIntakeOrder.pGetBarcodesViaWebserviceBln(true)) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_barcodes_failed));
            return result;
        }

        //Get packaging
        if (!cIntakeorder.currentIntakeOrder.mGetPackagingViaWebserviceBln()) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_packaging_failed));
            return result;
        }

        // Get all comments
        if (!cIntakeorder.currentIntakeOrder.pGetCommentsViaWebserviceBln(true)) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_comments_failed));
            return result;
        }

        return  result;
    }

    private boolean mGetReceiveLinesViaWebserviceBln(Boolean pvRefreshBln, String pvScannerStr) {

        if (pvRefreshBln) {
            cReceiveorderLine.allReceiveorderLines = null;
            cIntakeorderMATLine.pTruncateTableBln();
        }

        cWebresult WebResult;
        WebResult = this.getIntakeorderViewModel().pGetIntakeorderReceiveLinesFromWebserviceWrs(pvScannerStr);
        if (WebResult.getResultBln()&& WebResult.getSuccessBln()) {



            for (JSONObject jsonObject : WebResult.getResultDtt()) {
                cReceiveorderLine receiveorderLine = new cReceiveorderLine(jsonObject);

                if (receiveorderLine.getHandledTimeStampStr() == "null" && cIntakeorder.currentIntakeOrder.isGenerated()) {
                    continue;
                }

                receiveorderLine.pInsertInDatabaseBln();

                cReceiveorderSummaryLine receiveorderSummaryLine = cReceiveorderSummaryLine.pGetSummaryLine(receiveorderLine.getItemNoStr(),
                        receiveorderLine.getVariantCodeStr());

                if (receiveorderSummaryLine == null ) {
                    //Thats weird, but continue
                    continue;
                }

                receiveorderSummaryLine.pAddLine(receiveorderLine);
            }
            return true;

        } else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETINTAKEORDERMATLINES);
            return false;
        }
    }

    private boolean mGetReceiveItemsViaWebserviceBln(Boolean pvRefreshBln) {

        if (pvRefreshBln) {
            cReceiveorderSummaryLine.allReceiveorderSummaryLinesObl = null;
            cReceiveorderLine.pTruncateTableBln();
        }

        cReceiveorderSummaryLine.allReceiveorderSummaryLinesObl = new ArrayList<>();

        cWebresult WebResult;
        WebResult = this.getIntakeorderViewModel().pGetIntakeorderReceiveItemsFromWebserviceWrs();
        if (WebResult.getResultBln()&& WebResult.getSuccessBln()) {
            for (JSONObject jsonObject : WebResult.getResultDtt()) {
                cReceiveorderSummaryLine receiveorderSummaryLine  = new cReceiveorderSummaryLine(jsonObject);
                cReceiveorderSummaryLine.pAddSummaryLine(receiveorderSummaryLine);

                if (receiveorderSummaryLine.getItemNoStr().equalsIgnoreCase("UNKNOWN")) {
                    cIntakeorder.currentIntakeOrder.unknownVariantCounterInt += 1;
                }

            }
            return true;

        } else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETINTAKEORDERMATLINES);
            return false;
        }
    }

    private boolean mGetPackagingViaWebserviceBln() {


        this.packagingInObl = new ArrayList<>();
        this.packagingOutObl = new ArrayList<>();


        cWebresult WebResult;
        WebResult = this.getIntakeorderViewModel().pGetIntakeorderPackagingFromWebserviceWrs();
        if (WebResult.getResultBln()&& WebResult.getSuccessBln()) {
            for (JSONObject jsonObject : WebResult.getResultDtt()) {
                cPackaging packaging  = new cPackaging(jsonObject);
                this.packagingInObl.add(packaging);
                this.packagingOutObl.add(packaging);
            }



            //If there is no packaging defined, use the general list from the BASICS as a starting point
            if (cPackaging.allPackaging == null)  {
                return true;
            }

            if ( this.packagingInObl.size() == 0 || this.packagingOutObl.size() ==0 ) {

                for (cPackaging packaging : cPackaging.allPackaging ) {
                    packaging.quantityInUsedInt = 0;
                    packaging.quantityOutUsedInt = 0;
                }

                this.packagingInObl.addAll(cPackaging.allPackaging);
                this.packagingOutObl.addAll(cPackaging.allPackaging);
            }

            return true;

        } else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETINTAKEORDERMATLINES);
            return false;
        }
    }

    private cResult mGetMATOrderDetailsRst(){

        cResult result;

        result = new cResult();
        result.resultBln = true;

        //Get all linesInt for current order, if size = 0 or webservice error then stop
        if (!cIntakeorder.currentIntakeOrder.mGetMATLinesViaWebserviceBln(true)) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_intakelines_failed));
            return result;
        }

        //Get line barcodes for current order
        if (!cIntakeorder.currentIntakeOrder.mGetMATLineBarcodesViaWebserviceBln(true)) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_linebarcodes_failed));
            return result;
        }

        // Get all barcodes, if size =0 or webservice error then stop
        if (!cIntakeorder.currentIntakeOrder.pGetBarcodesViaWebserviceBln(true)) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_barcodes_failed));
            return result;
        }

        if (!cIntakeorder.currentIntakeOrder.pMatchBarcodesAndLinesBln()) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_matching_lines_and_barcodes_failed));
            return result;
        }

        // Get all comments
        if (!cIntakeorder.currentIntakeOrder.pGetCommentsViaWebserviceBln(true)) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_comments_failed));
            return result;
        }

        if (cIntakeorder.currentIntakeOrder.getOrderTypeStr().equalsIgnoreCase("MAS") && cIntakeorder.currentIntakeOrder.isGenerated()) {
            if (cIntakeorderMATSummaryLine.allIntakeorderMATSummaryLinesObl == null) {
                cIntakeorderMATSummaryLine.allIntakeorderMATSummaryLinesObl = new ArrayList<>();
            }
        }



        return  result;
    }

    private boolean mGetMATLinesViaWebserviceBln(Boolean pvRefreshBln) {

        if (pvRefreshBln) {
            cIntakeorderMATLine.allIntakeorderMATLinesObl = null;
            cIntakeorderMATSummaryLine.allIntakeorderMATSummaryLinesObl = null;
            cIntakeorderMATLine.pTruncateTableBln();
        }

        cWebresult WebResult;
        WebResult = this.getIntakeorderViewModel().pGetIntakeorderMATLinesFromWebserviceWrs();
        if (WebResult.getResultBln()&& WebResult.getSuccessBln()) {

            this.sourceNoObl = new ArrayList<>();

            for (JSONObject jsonObject : WebResult.getResultDtt()) {
                cIntakeorderMATLine intakeorderMATLine = new cIntakeorderMATLine(jsonObject);

                if (cIntakeorder.currentIntakeOrder.getOrderTypeStr().equalsIgnoreCase("MAS") && intakeorderMATLine.getQuantityHandledDbl() == 0) {
                    continue;
                }

                intakeorderMATLine.pInsertInDatabaseBln();

                if (!this.sourceNoObl.contains(intakeorderMATLine.getSourceNoStr())) {
                    this.sourceNoObl.add(intakeorderMATLine.getSourceNoStr());
                }

                cIntakeorderMATSummaryLine intakeorderMATSummaryLine = cIntakeorderMATSummaryLine.pGetSummaryLine(intakeorderMATLine.getItemNoStr(),
                        intakeorderMATLine.getVariantCodeStr(),
                        intakeorderMATLine.getSourceNoStr());

                if (intakeorderMATSummaryLine == null ) {
                    cIntakeorderMATSummaryLine summaryLineToAdd = new cIntakeorderMATSummaryLine(intakeorderMATLine.getItemNoStr(),
                            intakeorderMATLine.getVariantCodeStr(),
                            intakeorderMATLine.getSourceNoStr());

                    summaryLineToAdd.sortingSequenceInt = intakeorderMATLine.getSortingSequenceInt();
                    summaryLineToAdd.containerStr = intakeorderMATLine.getContainerStr();

                    cIntakeorderMATSummaryLine.pAddSummaryLine(summaryLineToAdd);

                    if (!intakeorderMATLine.getBinCodeStr().isEmpty()){
                        summaryLineToAdd.binCodeStr = intakeorderMATLine.getBinCodeStr();
                    }



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

    private boolean mGetMATLineBarcodesViaWebserviceBln(Boolean pvRefreshBln) {

        if (pvRefreshBln) {
            cIntakeorderMATLineBarcode.allMATLineBarcodesObl = null;
            cIntakeorderMATLineBarcode.pTruncateTableBln();
        }

        cWebresult WebResult;
        WebResult = this.getIntakeorderViewModel().pGetIntakeorderMATLineBarcodesFromWebserviceWrs();
        if (WebResult.getResultBln() && WebResult.getSuccessBln()) {

            for (JSONObject jsonObject : WebResult.getResultDtt()) {
                cIntakeorderMATLineBarcode cIntakeorderMATLineBarcode = new cIntakeorderMATLineBarcode(jsonObject);
                cIntakeorderMATLineBarcode.pInsertInDatabaseBln();
            }

            return true;
        } else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETINTAKEORDERMATLINES);
            return false;
        }
    }

    private static void mTruncateTable() {
        cIntakeorderViewModel intakeorderViewModel = new ViewModelProvider(cAppExtension.fragmentActivity).get(cIntakeorderViewModel.class);
        intakeorderViewModel.deleteAll();
    }

    private void mGetCommentsViaWebError(List<JSONObject> pvResultDtt) {

        cComment.allCommentsObl = new ArrayList<>();

        for (JSONObject jsonObject : pvResultDtt) {
            cComment comment = new cComment(jsonObject);
            comment.pInsertInDatabaseBln();
        }
    }

}
