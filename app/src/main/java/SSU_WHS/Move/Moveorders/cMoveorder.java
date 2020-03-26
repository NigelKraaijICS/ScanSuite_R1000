package SSU_WHS.Move.MoveOrders;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import org.json.JSONObject;

import java.util.ArrayList;
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
import SSU_WHS.Basics.ArticleBarcode.cArticleBarcode;
import SSU_WHS.Basics.BranchBin.cBranchBin;
import SSU_WHS.Basics.Settings.cSetting;
import SSU_WHS.Basics.Users.cUser;
import SSU_WHS.General.Comments.cComment;
import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.General.Warehouseorder.cWarehouseorderViewModel;
import SSU_WHS.Move.MoveorderBarcodes.cMoveorderBarcode;
import SSU_WHS.Move.MoveorderLineBarcodes.cMoveorderLineBarcode;
import SSU_WHS.Move.MoveorderLines.cMoveorderLine;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebserviceDefinitions;
import nl.icsvertex.scansuite.R;


public class cMoveorder {

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

    private String documentStr;
    public String getDocumentStr() {
        return documentStr;
    }

    private String externalReferenceStr;
    public String getExternalReferenceStr() {
        return externalReferenceStr;
    }

    private int sourceDocumentInt;
    private int getSourceDocumentInt() {
        return sourceDocumentInt;
    }

    private Boolean moveAmountManualBln;
    public Boolean getMoveAmountManualBln() {return this.moveAmountManualBln;}

    private Boolean moveBarcodeCheckBln;
    public Boolean getMoveBarcodeCheckBln() {return this.moveBarcodeCheckBln;}

    private Boolean moveValidateStockBln;
    public Boolean getMoveValidateStockBln() {return this.moveValidateStockBln;}

    private Boolean moveValidateStockEnforceBln;
    public Boolean getMoveValidateStockEnforceBln() {return this.moveValidateStockEnforceBln;}

    private Boolean moveAutoAcceptAtRequestedBln;
    public Boolean getMoveAutoAcceptAtRequestedBln() {return this.moveAutoAcceptAtRequestedBln;}

   public Boolean isGeneratedBln() {
       return this.getSourceDocumentInt() == cWarehouseorder.SoureDocumentTypeEnu.Generated;
   }

    private int unknownVariantCounterInt = 0;
    public int getUnknownVariantCounterInt() {
        return unknownVariantCounterInt;
    }

    private cMoveorderEntity moveorderEntity;

    public List<cComment> commentsObl() {
        return cComment.allCommentsObl;
    }
    public List<cMoveorderBarcode> barcodesObl () {return  cMoveorderBarcode.allMoveorderBarcodesObl;}
    public List<cMoveorderLine> linesObl () {return  cMoveorderLine.allLinesObl;}
    public List<cMoveorderLine> takeLinesObl () {

        List<cMoveorderLine> resultObl = new ArrayList<>();

        if (this.linesObl() == null || this.linesObl().size() == 0)  {
            return  resultObl;
        }

        for (cMoveorderLine moveorderLine : this.linesObl()) {
            if (moveorderLine.getActionTypeCodeStr().equalsIgnoreCase(cWarehouseorder.ActionTypeEnu.TAKE.toString())) {
                resultObl.add(moveorderLine);
            }
        }

        return  resultObl;
 }
    public List<cMoveorderLine> placeLinesObl () {

        List<cMoveorderLine> resultObl = new ArrayList<>();

        if (this.linesObl() == null || this.linesObl().size() == 0)  {
            return  resultObl;
        }

        for (cMoveorderLine moveorderLine : this.linesObl()) {
            if (moveorderLine.getActionTypeCodeStr().equalsIgnoreCase(cWarehouseorder.ActionTypeEnu.PLACE.toString())) {
                resultObl.add(moveorderLine);
            }
        }

        return  resultObl;
    }

    public static List<cMoveorder> allMoveordersObl;
    public static cMoveorder currentMoveOrder;

    public cArticle currentArticle;
    public cMoveorderLineBarcode currentMoveorderLineBarcode;
    public cBranchBin currentBranchBin;

    private cMoveorderViewModel getMoveorderViewModel () {
        return new ViewModelProvider(cAppExtension.fragmentActivity).get(cMoveorderViewModel.class);
    }

    private cWarehouseorderViewModel getWarehouseorderViewModel () {
        return new ViewModelProvider(cAppExtension.fragmentActivity).get(cWarehouseorderViewModel.class);
    }




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
        this.documentStr = this.moveorderEntity.getDocumentStr();

        this.moveAmountManualBln = cText.pStringToBooleanBln(this.moveorderEntity.getMoveAmountManualStr(), false);
        this.moveBarcodeCheckBln = cText.pStringToBooleanBln(this.moveorderEntity.getMoveBarcodeCheckStr(), false);
        this.moveValidateStockBln = cText.pStringToBooleanBln(this.moveorderEntity.getMoveValidateStockStr(), false);
        this.moveValidateStockEnforceBln = cText.pStringToBooleanBln(this.moveorderEntity.getMoveValidateStockEnforceStr(), false);
        this.moveAutoAcceptAtRequestedBln = cText.pStringToBooleanBln(this.moveorderEntity.getMoveAutoAcceptAtRequestedStr(), false);

        this.sourceDocumentInt = cText.pStringToIntegerInt(this.moveorderEntity.getSourceDocumentStr()) ;
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
        this.documentStr = this.moveorderEntity.getDocumentStr();

        this.moveAmountManualBln = cText.pStringToBooleanBln(this.moveorderEntity.getMoveAmountManualStr(), false);
        this.moveBarcodeCheckBln = cText.pStringToBooleanBln(this.moveorderEntity.getMoveBarcodeCheckStr(), false);
        this.moveValidateStockBln = cText.pStringToBooleanBln(this.moveorderEntity.getMoveValidateStockStr(), false);
        this.moveValidateStockEnforceBln = cText.pStringToBooleanBln(this.moveorderEntity.getMoveValidateStockEnforceStr(), false);
        this.moveAutoAcceptAtRequestedBln = cText.pStringToBooleanBln(this.moveorderEntity.getMoveAutoAcceptAtRequestedStr(), false);
        this.sourceDocumentInt = cText.pStringToIntegerInt(this.moveorderEntity.getSourceDocumentStr()) ;
    }

    //End Region Constructor

    //Region Public Methods

    public boolean pInsertInDatabaseBln() {
        this.getMoveorderViewModel().insert(this.moveorderEntity);
        if (cMoveorder.allMoveordersObl == null) {
            cMoveorder.allMoveordersObl = new ArrayList<>();
        }
        cMoveorder.allMoveordersObl.add(this);
        return true;
    }

    public static cResult pCreateMoveOrderViaWebserviceRst(String pvDocumentStr, String pvBinCodeStr, boolean pvCheckBarcodesBln) {

        cResult result;
        result = new cResult();
        result.resultBln = true;

        cWebresult WebResult;

        cMoveorderViewModel moveorderViewModel =   new ViewModelProvider(cAppExtension.fragmentActivity).get(cMoveorderViewModel.class);
        WebResult = moveorderViewModel.pCreateMoveOrderViaWebserviceWrs(pvDocumentStr,pvBinCodeStr,pvCheckBarcodesBln);

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

            cMoveorder moveorder = new cMoveorder(WebResult.getResultDtt().get(0));
            moveorder.pInsertInDatabaseBln();
            cMoveorder.currentMoveOrder = moveorder;
            return result;


        } else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_RECEIVECREATE);
            result.resultBln = false;
            return result;
        }
    }

    public static boolean pGetMoveOrdersViaWebserviceBln(Boolean pvRefreshBln, String pvSearchTextStr, String pvMainTypeStr) {

        cWebresult WebResult;

        if (pvRefreshBln) {
            cMoveorder.allMoveordersObl = null;
            cMoveorder.mTruncateTable();
        }
        cMoveorderViewModel moveorderViewModel =   new ViewModelProvider(cAppExtension.fragmentActivity).get(cMoveorderViewModel.class);
        WebResult = moveorderViewModel.pGetMoveordersFromWebserviceWrs(pvSearchTextStr, pvMainTypeStr);
        if (WebResult.getResultBln() && WebResult.getSuccessBln()) {

           if(WebResult.getResultDtt() != null && WebResult.getResultDtt().size() > 0) {
               JSONObject jsonObject = WebResult.getResultDtt().get(0);
               cMoveorder moveorder = new cMoveorder(jsonObject);
               moveorder.pInsertInDatabaseBln();
               return  true;
           }

        } else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETMOVEORDERS);
            return false;
        }

        return  false;
    }

    public cResult pLockViaWebserviceRst() {

        //Initialise result
        cResult result;
        result = new cResult();
        result.resultBln = true;

        cWebresult Webresult = null;
        boolean ignoreBusyBln = false;

        if (cSetting.GENERIC_UNLOCK_BUSY_ORDERS_ALLOWED()) {
            if (this.getStatusInt() > 10 && cUser.currentUser.getUsernameStr().equalsIgnoreCase(this.getCurrentUserIdStr())) {
                ignoreBusyBln = true;
            }
        }

        switch (this.getStatusInt()) {
            case cWarehouseorder.MoveStatusEnu.Move_Take:
            case cWarehouseorder.MoveStatusEnu.Move_Take_Busy:
            case cWarehouseorder.MoveStatusEnu.Move_Take_Wait:
                Webresult = this.getWarehouseorderViewModel().pLockWarehouseopdrachtViaWebserviceWrs(cWarehouseorder.OrderTypeEnu.VERPLAATS.toString(), this.getOrderNumberStr(), cDeviceInfo.getSerialnumberStr(), cWarehouseorder.StepCodeEnu.Move_Take.toString(), cWarehouseorder.MoveStatusEnu.Move_Take, ignoreBusyBln);
                this.statusInt = cWarehouseorder.MoveStatusEnu.Move_Take_Busy;
                break;

            case cWarehouseorder.MoveStatusEnu.Move_Place:
            case cWarehouseorder.MoveStatusEnu.Move_Place_Busy:
            case cWarehouseorder.MoveStatusEnu.Move_Place_Wait:
                Webresult = this.getWarehouseorderViewModel().pLockWarehouseopdrachtViaWebserviceWrs(cWarehouseorder.OrderTypeEnu.VERPLAATS.toString(), this.getOrderNumberStr(), cDeviceInfo.getSerialnumberStr(), cWarehouseorder.StepCodeEnu.Move_Place.toString(), cWarehouseorder.MoveStatusEnu.Move_Place, ignoreBusyBln);
                this.statusInt = cWarehouseorder.MoveStatusEnu.Move_Place_Busy;
                break;
        }


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
        if (!Webresult.getResultBln() && Webresult.getResultLng() <= 0 && Webresult.getResultObl() != null) {
            Webresult.getResultObl().size();
            if (!Webresult.getResultObl().get(0).equalsIgnoreCase(cUser.currentUser.getUsernameStr())) {
                result.resultBln = false;
                result.activityActionEnu = cWarehouseorder.ActivityActionEnu.Unknown;
                result.pAddErrorMessage(cAppExtension.context.getString((R.string.message_another_user_already_started)) + " " + Webresult.getResultObl().get(0));
                return result;
            }
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

            cMoveorder.currentMoveOrder.mGetCommentsViaWebError(Webresult.getResultDtt());
            return result;
        }

        return result;

    }

    public boolean pLockReleaseViaWebserviceBln() {

        cWebresult Webresult = null;

        switch (this.getStatusInt()) {
            case cWarehouseorder.MoveStatusEnu.Move_Take_Busy:
                Webresult = this.getWarehouseorderViewModel().pLockReleaseWarehouseorderViaWebserviceWrs(cWarehouseorder.OrderTypeEnu.VERPLAATS.toString(), this.getOrderNumberStr(), cDeviceInfo.getSerialnumberStr(), cWarehouseorder.StepCodeEnu.Move_Take.toString(), cWarehouseorder.MoveStatusEnu.Move_Take_Busy);
                break;

            case cWarehouseorder.MoveStatusEnu.Move_Place_Busy:
                Webresult = this.getWarehouseorderViewModel().pLockReleaseWarehouseorderViaWebserviceWrs(cWarehouseorder.OrderTypeEnu.VERPLAATS.toString(), this.getOrderNumberStr(), cDeviceInfo.getSerialnumberStr(), cWarehouseorder.StepCodeEnu.Move_Place.toString(), cWarehouseorder.MoveStatusEnu.Move_Place_Busy);
                break;


        }
        return Objects.requireNonNull(Webresult).getSuccessBln() && Webresult.getResultBln();
    }

    public boolean pDeleteDetailsBln() {
        cMoveorderLine.pTruncateTableBln();
        cMoveorderBarcode.pTruncateTableBln();
        cMoveorderLineBarcode.pTruncateTableBln();
        cComment.pTruncateTableBln();
        return true;
    }

    public boolean pGetLinesViaWebserviceBln(Boolean pvRefreshBln) {

        if (pvRefreshBln) {
            cMoveorderLine.allLinesObl = null;
            cMoveorderLine.pTruncateTableBln();
        }
        cWebresult WebResult = this.getMoveorderViewModel().pGetLinesFromWebserviceWrs();
        if (WebResult.getResultBln() && WebResult.getSuccessBln()) {

            if (cMoveorderLine.allLinesObl == null) {
                cMoveorderLine.allLinesObl = new ArrayList<>();
            }

            for ( JSONObject jsonObject :WebResult.getResultDtt()) {
                cMoveorderLine moveorderLine = new cMoveorderLine(jsonObject);
                moveorderLine.pInsertInDatabaseBln();
            }

            return  true;

        } else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETMOVEORDERLINES);
            return false;
        }
    }

    public boolean pGetCommentsViaWebserviceBln(Boolean pvRefeshBln) {

        if (pvRefeshBln) {
            cComment.allCommentsObl = null;
            cComment.pTruncateTableBln();
            cComment.commentsShownBln = false;
        }

         cWebresult webresult = this.getMoveorderViewModel().pGetCommentsFromWebserviceWrs();
        if (webresult.getResultBln() && webresult.getSuccessBln()) {

            cComment.allCommentsObl = new ArrayList<>();

            for ( JSONObject jsonObject : webresult.getResultDtt()) {
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

        if (pvRefreshBln) {
            cMoveorderBarcode.allMoveorderBarcodesObl = null;
            cMoveorderBarcode.pTruncateTableBln();
        }

        cWebresult WebResult =  this.getMoveorderViewModel().pGetBarcodesFromWebserviceWrs();
        if (WebResult.getResultBln() && WebResult.getSuccessBln()){

            if (cMoveorderBarcode.allMoveorderBarcodesObl == null) {
                cMoveorderBarcode.allMoveorderBarcodesObl = new ArrayList<>();
            }

            for (  JSONObject jsonObject : WebResult.getResultDtt()) {
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

        if (pvRefreshBln) {
            cMoveorderLineBarcode.allLineBarcodesObl = null;
            cMoveorderLineBarcode.pTruncateTableBln();
        }

        cWebresult WebResult =  this.getMoveorderViewModel().pGetLineBarcodesFromWebserviceWrs();
        if (WebResult.getResultBln() && WebResult.getSuccessBln()){

            for ( JSONObject jsonObject : WebResult.getResultDtt()) {
                cMoveorderLineBarcode moveorderLineBarcode = new cMoveorderLineBarcode(jsonObject);
                //Search for line that belongs to this barcode
                cMoveorderLine moveorderLine = cMoveorder.currentMoveOrder.mGetLineWithLineNo(moveorderLineBarcode.getLineNoLng());

                //If we can't find it, skip this
                if (moveorderLine == null) {
                    continue;
                }

                //Add barcode to line
                //todo make this work
             //   moveorderLine.pAddLineBarcodeBln(moveorderLineBarcode.getBarcodeStr(),moveorderLineBarcode.getQuantityhandledDbl());
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

        WebResult =  this.getMoveorderViewModel().pAddUnknownItemViaWebserviceWrs(pvBarcodeScan);
        if (WebResult.getResultBln() && WebResult.getSuccessBln()){
            for (  JSONObject jsonObject : WebResult.getResultDtt()) {
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


        cWebresult WebResult =  this.getMoveorderViewModel().pAddERPItemViaWebserviceWrs(matchedArticleBarcode);
        if (WebResult.getResultBln() && WebResult.getSuccessBln()){

            for ( JSONObject jsonObject : WebResult.getResultDtt()) {
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

    public static List<cMoveorder> pGetMovesWithFilterFromDatabasObl() {

        List<cMoveorder> resultObl = new ArrayList<>();
        List<cMoveorderEntity> hulpResultObl;

        cMoveorderViewModel    moveorderViewModel =   new ViewModelProvider(cAppExtension.fragmentActivity).get(cMoveorderViewModel.class);

        hulpResultObl = moveorderViewModel.pGetMovesWithFilterFromDatabaseObl(cUser.currentUser.getUsernameStr(), cSharedPreferences.userFilterBln());
        if (hulpResultObl == null || hulpResultObl.size() == 0) {
            return  resultObl;
        }

        for (cMoveorderEntity moveorderEntity : hulpResultObl ) {
            cMoveorder moveorder = new cMoveorder(moveorderEntity);
            resultObl.add(moveorder);
        }

        return resultObl;
    }

    public List<cComment> pFeedbackCommentObl(){

        if (cMoveorder.currentMoveOrder.commentsObl() == null || cMoveorder.currentMoveOrder.commentsObl().size() == 0) {
            return  null;
        }

        return  cComment.pGetCommentsForTypeObl(cWarehouseorder.CommentTypeEnu.FEEDBACK);

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

    public cMoveorderLine pGetLineForArticleAndBin(String pvBinStr) {


        if (this.linesObl() == null || this.linesObl().size() == 0) {
            return  null;
        }

        for (cMoveorderLine moveorderLine : this.linesObl()) {

            //Check if BIN matches current BIN
            if (! moveorderLine.getBinCodeStr().equalsIgnoreCase(   pvBinStr)) {
                continue;
            }

            //Check if item matches scanned item
            if (moveorderLine.getItemNoStr().equalsIgnoreCase(cMoveorderBarcode.currentMoveOrderBarcode.getItemNoStr()) &&
                    moveorderLine.getVariantCodeStr().equalsIgnoreCase(cMoveorderBarcode.currentMoveOrderBarcode.getVariantCodeStr())) {
                return  moveorderLine;
            }
        }

        return  null;
    }

    public cMoveorderLine pGetLineForArticle() {

        if (this.linesObl() == null || this.linesObl().size() == 0) {
            return  null;
        }

        for (cMoveorderLine moveorderLine : this.linesObl()) {

            //Check if item matches scanned item
            if (moveorderLine.getItemNoStr().equalsIgnoreCase(cMoveorderBarcode.currentMoveOrderBarcode.getItemNoStr()) &&
                    moveorderLine.getVariantCodeStr().equalsIgnoreCase(cMoveorderBarcode.currentMoveOrderBarcode.getVariantCodeStr())) {
                return  moveorderLine;
            }
        }

        return  null;
    }

    public cMoveorderLine pGetLineForBin(String pvBinCodeStr) {

        if (this.linesObl() == null || this.linesObl().size() == 0) {
            return  null;
        }

        for (cMoveorderLine moveorderLine : this.linesObl()) {

            //Check if item matches scanned item
            if (moveorderLine.getBinCodeStr().equalsIgnoreCase(pvBinCodeStr)) {
                return  moveorderLine;
            }
        }
        return  null;
    }

    public cResult pOrderHandledViaWebserviceRst() {

        cResult result;
        result = new cResult();
        result.resultBln = true;

        cWebresult webresult;

        webresult =  this.getMoveorderViewModel().pHandledViaWebserviceWrs();

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

            cMoveorder.currentMoveOrder.mGetCommentsViaWebError(webresult.getResultDtt());
            return result;
        }

        return  result;


    }
    //Region Public Methods

    //Region Private Methods

    private static void mTruncateTable() {
        cMoveorderViewModel    moveorderViewModel =   new ViewModelProvider(cAppExtension.fragmentActivity).get(cMoveorderViewModel.class);
        moveorderViewModel.deleteAll();
    }

    private void mGetCommentsViaWebError(List<JSONObject> pvResultDtt) {

        cComment.allCommentsObl = new ArrayList<>();

        if (pvResultDtt != null && pvResultDtt.size() > 0 ) {
            JSONObject jsonObject = pvResultDtt.get(0);
            cComment comment = new cComment(jsonObject);
            comment.pInsertInDatabaseBln();
        }

    }

    private cMoveorderLine mGetLineWithLineNo(Long pvLineNoLng) {

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

    //End Region Private Methods



}
