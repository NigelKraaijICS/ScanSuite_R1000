package SSU_WHS.Move.MoveOrders;

import androidx.lifecycle.ViewModelProvider;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
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
import SSU_WHS.Basics.BranchBin.cBranchBin;
import SSU_WHS.Basics.Settings.cSetting;
import SSU_WHS.Basics.Users.cUser;
import SSU_WHS.General.Comments.cComment;
import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.General.Warehouseorder.cWarehouseorderViewModel;
import SSU_WHS.Move.MoveItemVariant.cMoveItemVariant;
import SSU_WHS.Move.MoveorderBarcodes.cMoveorderBarcode;
import SSU_WHS.Move.MoveorderLineBarcode.cMoveorderLineBarcode;
import SSU_WHS.Move.MoveorderLines.cMoveorderLine;
import SSU_WHS.Move.MoveorderLines.cMoveorderLineViewModel;
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

    private boolean moveAmountManualBln;
    public boolean getMoveAmountManualBln() {return this.moveAmountManualBln;}

    private boolean moveBarcodeCheckBln;
    public boolean isMoveBarcodeCheckBln() {return this.moveBarcodeCheckBln;}

    private boolean moveValidateStockBln;
    public boolean isMoveValidateStockBln() {return this.moveValidateStockBln;}

    public boolean getProcessingOrParkedBln() {

        if (this.getStatusInt() == cWarehouseorder.MoveStatusEnu.Move_Take_Wait) {
            return  true;
        }

        return this.getStatusInt() == cWarehouseorder.MoveStatusEnu.Move_Place_Wait;

    }

    public boolean isGeneratedBln() {
       return this.getSourceDocumentInt() == cWarehouseorder.SourceDocumentTypeEnu.Generated;
   }



    public  boolean showTodoBln = true;

    private cMoveorderEntity moveorderEntity;

    public List<cComment> commentsObl() {
        return cComment.allCommentsObl;
    }
    public List<cMoveorderBarcode> barcodesObl () {return  cMoveorderBarcode.allMoveorderBarcodesObl;}
    public List<cMoveorderLine> linesObl () {return  cMoveorderLine.allLinesObl;}

    public List<cMoveorderLine> takeLinesObl;
    public List<cMoveorderLine> sortedTakeLinesObl() {

        List<cMoveorderLine> sortedTakeLinesObl = new ArrayList<>();

        if (this.takeLinesObl == null || this.takeLinesObl.size() == 0) {
            return  sortedTakeLinesObl;
        }

        for (cMoveorderLine moveorderLine : this.takeLinesObl) {
            sortedTakeLinesObl.add(moveorderLine);
        }

        Collections.sort(sortedTakeLinesObl);

        return  sortedTakeLinesObl;

    }

    public List<cMoveorderLine> placeLinesObl;
    public List<cMoveorderLine> sortedPlaceLinesObl() {

        List<cMoveorderLine> sortedTakeLinesObl = new ArrayList<>();

        if (this.placeLinesObl == null || this.placeLinesObl.size() == 0) {
            return  sortedTakeLinesObl;
        }

        for (cMoveorderLine moveorderLine : this.placeLinesObl) {
            sortedTakeLinesObl.add(moveorderLine);
        }

        Collections.sort(sortedTakeLinesObl);

        return  sortedTakeLinesObl;

    }

    public  List<cMoveorderLine> placeLinesTodoObl (){

        List<cMoveorderLine> resultObl = new ArrayList<>();

        if (this.placeLinesObl == null || this.placeLinesObl.size() == 0) {
            return resultObl;
        }

        for (cMoveorderLine moveorderLine :this.placeLinesObl)
        {
            if (moveorderLine.getQuantityHandledDbl() < moveorderLine.getQuantityDbl())
            {
                resultObl.add(moveorderLine);
            }
        }
        return resultObl;

    }

    public  List<cMoveorderLine> takeLinesTodoObl (String pvBinCodeStr){

        List<cMoveorderLine> resultObl = new ArrayList<>();

        if (this.takeLinesObl == null || this.takeLinesObl.size() == 0) {
            return resultObl;
        }

        for (cMoveorderLine moveorderLine :this.takeLinesObl)
        {
            if (!moveorderLine.isHandledBln())
            {
                if (pvBinCodeStr.isEmpty()) {
                    resultObl.add(moveorderLine);
                }
                else
                {
                    if (moveorderLine.getBinCodeStr().equalsIgnoreCase(pvBinCodeStr)) {
                        resultObl.add((moveorderLine));
                    }
                }

            }
        }
        return resultObl;

    }


    public SortedMap<String, cArticle> articleObl;
    public List<cBranchBin> binsObl;

    public static List<cMoveorder> allMoveordersObl;
    public static cMoveorder currentMoveOrder;

    public cArticle currentArticle;
    public cMoveorderBarcode currentMoveorderBarcode;
    public cMoveorderBarcode moveorderBarcodeToHandle;
    public cBranchBin currentBranchBin;
    public  cMoveItemVariant currentMoveItemVariant;

    private cMoveorderViewModel getMoveorderViewModel () {
        return new ViewModelProvider(cAppExtension.fragmentActivity).get(cMoveorderViewModel.class);
    }

    private cWarehouseorderViewModel getWarehouseorderViewModel () {
        return new ViewModelProvider(cAppExtension.fragmentActivity).get(cWarehouseorderViewModel.class);
    }

    private cMoveorderLineViewModel getMoveorderLineViewModel() {
        return new ViewModelProvider(cAppExtension.fragmentActivity).get(cMoveorderLineViewModel.class);
    }

    public enum lineModusEnu {
        TAKE,
        PLACE
    }

    public lineModusEnu currenLineModusEnu = lineModusEnu.TAKE;

    //Region Public Properties

    //Region Constructor

    public cMoveorder(JSONObject pvJsonObject) {

        this.moveorderEntity = new cMoveorderEntity(pvJsonObject);
        this.orderNumberStr = this.moveorderEntity.getOrdernumberStr();
        this.orderTypeStr = this.moveorderEntity.getOrderTypeStr();
        this.assignedUserIdStr = this.moveorderEntity.getAssignedUserIdStr();
        this.currentUserIdStr = this.moveorderEntity.getCurrentUserIdStr();
        this.statusInt = cText.pStringToIntegerInt(this.moveorderEntity.getStatusStr());
        this.binCodeStr = this.moveorderEntity.getBincodeStr();
        this.externalReferenceStr = this.moveorderEntity.getExternalReferenceStr();
        this.documentStr = this.moveorderEntity.getDocumentStr();

        this.moveAmountManualBln = cText.pStringToBooleanBln(this.moveorderEntity.getMoveAmountManualStr(), false);
        this.moveBarcodeCheckBln = cText.pStringToBooleanBln(this.moveorderEntity.getMoveBarcodeCheckStr(), false);
        this.moveValidateStockBln = cText.pStringToBooleanBln(this.moveorderEntity.getMoveValidateStockStr(), false);
        this.sourceDocumentInt = cText.pStringToIntegerInt(this.moveorderEntity.getSourceDocumentStr()) ;
    }

    public cMoveorder(cMoveorderEntity pvMoveorderEntity) {

        this.moveorderEntity = pvMoveorderEntity;

        this.orderNumberStr = this.moveorderEntity.getOrdernumberStr();
        this.orderTypeStr = this.moveorderEntity.getOrderTypeStr();
        this.assignedUserIdStr = this.moveorderEntity.getAssignedUserIdStr();
        this.currentUserIdStr = this.moveorderEntity.getCurrentUserIdStr();
        this.statusInt = cText.pStringToIntegerInt(this.moveorderEntity.getStatusStr());
        this.binCodeStr = this.moveorderEntity.getBincodeStr();
        this.externalReferenceStr = this.moveorderEntity.getExternalReferenceStr();
        this.documentStr = this.moveorderEntity.getDocumentStr();

        this.moveAmountManualBln = cText.pStringToBooleanBln(this.moveorderEntity.getMoveAmountManualStr(), false);
        this.moveBarcodeCheckBln = cText.pStringToBooleanBln(this.moveorderEntity.getMoveBarcodeCheckStr(), false);
        this.moveValidateStockBln = cText.pStringToBooleanBln(this.moveorderEntity.getMoveValidateStockStr(), false);
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


        } else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_RECEIVECREATE);
            result.resultBln = false;
        }
        return result;
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
               for (JSONObject jsonObject : WebResult.getResultDtt()) {
                   cMoveorder moveorder = new cMoveorder(jsonObject);
                   moveorder.pInsertInDatabaseBln();
               }
           }

            return  true;


        } else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETMOVEORDERS);
            return false;
        }

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
                Webresult = this.getWarehouseorderViewModel().pLockReleaseWarehouseorderViaWebserviceWrs(cWarehouseorder.OrderTypeEnu.VERPLAATS.toString(), this.getOrderNumberStr(), cDeviceInfo.getSerialnumberStr(), cWarehouseorder.StepCodeEnu.Move_Take.toString(), cWarehouseorder.MoveStatusEnu.Move_Take);
                break;

            case cWarehouseorder.MoveStatusEnu.Move_Place_Busy:
                Webresult = this.getWarehouseorderViewModel().pLockReleaseWarehouseorderViaWebserviceWrs(cWarehouseorder.OrderTypeEnu.VERPLAATS.toString(), this.getOrderNumberStr(), cDeviceInfo.getSerialnumberStr(), cWarehouseorder.StepCodeEnu.Move_Place.toString(), cWarehouseorder.MoveStatusEnu.Move_Place);
                break;


        }
        return Objects.requireNonNull(Webresult).getSuccessBln() && Webresult.getResultBln();
    }

    public cResult pGetOrderDetailsRst(){

        cResult result;

        result = new cResult();
        result.resultBln = true;

        //Get all linesInt for current order, if webservice error then stop
        if (!cMoveorder.currentMoveOrder.pGetLinesViaWebserviceBln(true)) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_movelines_failed));
            return result;
        }

        // Get all barcodes, if webservice error then stop
        if (!cMoveorder.currentMoveOrder.pGetBarcodesViaWebserviceBln(true)) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_barcodes_failed));
            return result;
        }

        // Get all barcodes, if webservice error then stop
        if (!cMoveorder.currentMoveOrder.pGetLineBarcodesViaWebserviceBln(true)) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_line_barcodes_failed));
            return result;
        }

        // Get all BINS, if webservice error then stop
        if (!cMoveorder.currentMoveOrder.pGetBINSViaWebserviceBln()) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_bins_failed));
            return result;
        }


        // Get all comments
        if (!cMoveorder.currentMoveOrder.pGetCommentsViaWebserviceBln(true)) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_comments_failed));
            return result;
        }

        return  result;
    }

    public cResult pGetOrderDetailsMTRst(){

        cResult result;

        result = new cResult();
        result.resultBln = true;

        //Get all linesInt for current order, if webservice error then stop
        if (!cMoveorder.currentMoveOrder.pGetLinesMTViaWebserviceBln(true)) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_movelines_failed));
            return result;
        }

        // Get all barcodes, if webservice error then stop
        if (!cMoveorder.currentMoveOrder.pGetBarcodesViaWebserviceBln(true)) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_barcodes_failed));
            return result;
        }

        // Get all barcodes, if webservice error then stop
        if (!cMoveorder.currentMoveOrder.pGetLineBarcodesViaWebserviceBln(true)) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_line_barcodes_failed));
            return result;
        }

        // Get all BINS, if webservice error then stop
        if (!cMoveorder.currentMoveOrder.pGetBINSViaWebserviceBln()) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_bins_failed));
            return result;
        }

        // Get all comments
        if (!cMoveorder.currentMoveOrder.pGetCommentsViaWebserviceBln(true)) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_comments_failed));
            return result;
        }

        return  result;
    }

    public cResult pCloseTakeMTRst(){

        cResult result = new cResult();
        result.resultBln = true;

        cWebresult WebResult = this.getMoveorderViewModel().pCloseTakeMTViaWebserviceWrs();
        if (!WebResult.getResultBln() || !WebResult.getSuccessBln()) {
            result.resultBln = false;
            result.pAddErrorMessage(WebResult.getResultObl().toString());
            return result;
        }

        return  result;
    }

    public boolean pDeleteDetailsBln() {
        cMoveorderLine.pTruncateTableBln();
        cMoveorderBarcode.pTruncateTableBln();
        cComment.pTruncateTableBln();
        return true;
    }

    public boolean pGetLinesViaWebserviceBln(Boolean pvRefreshBln) {

        cMoveorderLine moveorderLineToAdd;

        if (pvRefreshBln) {
            cMoveorderLine.allLinesObl = null;
            this.takeLinesObl = null;
            this.placeLinesObl = null;
            cMoveorderLine.pTruncateTableBln();
            cMoveItemVariant.allMoveItemVariantObl = null;
        }
        cWebresult WebResult = this.getMoveorderViewModel().pGetLinesFromWebserviceWrs();
        if (WebResult.getResultBln() && WebResult.getSuccessBln()) {

            if (cMoveorderLine.allLinesObl == null) {
                cMoveorderLine.allLinesObl = new ArrayList<>();
                this.takeLinesObl = new ArrayList<>();
                this.placeLinesObl = new ArrayList<>();
                cMoveItemVariant.allMoveItemVariantObl = new LinkedHashMap<>();
            }

            for (JSONObject jsonObject : WebResult.getResultDtt()) {
                cMoveorderLine moveorderLine = new cMoveorderLine(jsonObject);
                moveorderLine.pInsertInDatabaseBln();

                // Try to get ItemVariant from SortedList
                cMoveItemVariant moveItemVariant = cMoveItemVariant.allMoveItemVariantObl.get(moveorderLine.getKeyStr());

                // Create ItemVariant if it doens't exist
                if (moveItemVariant == null) {
                    moveItemVariant = new cMoveItemVariant(moveorderLine.getItemNoStr(), moveorderLine.getVariantCodeStr());
                    moveItemVariant.quantityPlacedDbl = 0;
                    moveItemVariant.quantityTakenDbl = 0;

                    cMoveItemVariant.allMoveItemVariantObl.put(moveItemVariant.getKeyStr(), moveItemVariant);
                }

                if (moveItemVariant.linesObl == null) {
                    moveItemVariant.linesObl = new ArrayList<>();
                }

                // Add barcode to ItemVariant
                moveItemVariant.linesObl.add(moveorderLine);


                if (moveorderLine.getActionTypeCodeStr().equalsIgnoreCase(cWarehouseorder.ActionTypeEnu.TAKE.toString())) {
                    cMoveorder.currentMoveOrder.takeLinesObl.add(moveorderLine);
                    moveItemVariant.quantityTakenDbl += moveorderLine.getQuantityHandledDbl();

                    if (moveorderLine.getQuantityHandledDbl() > 0) {
                        if (moveItemVariant.getHandledTimeStampStr() == null || cText.pStringToDateStr(moveorderLine.getHandledTimeStampStr(), "YYYY-MM-dd").after(cText.pStringToDateStr(moveItemVariant.getHandledTimeStampStr(), "YYYY-MM-dd"))) {
                            moveItemVariant.handledTimeStampStr = moveorderLine.getHandledTimeStampStr();
                        }
                    }
                }

                if (moveorderLine.getActionTypeCodeStr().equalsIgnoreCase(cWarehouseorder.ActionTypeEnu.PLACE.toString())) {
                    cMoveorder.currentMoveOrder.placeLinesObl.add(moveorderLine);
                    moveItemVariant.quantityPlacedDbl += moveorderLine.getQuantityHandledDbl();

                }
            }



            //Loop through all item variants to check if we need to create extra place lines
            for (cMoveItemVariant loopMoveItemVariant :cMoveItemVariant.allMoveItemVariantObl.values()) {


                //If we took enough, skip this variant
                if (loopMoveItemVariant.getQuantityPlacedDbl() >= loopMoveItemVariant.getQuantityTakenDbl()) {
                    continue;
                }

                //We placed less then we took, so create place line for quantity still to place
                moveorderLineToAdd  = loopMoveItemVariant.linePlaceTodoLine();
                moveorderLineToAdd.quantityDbl = loopMoveItemVariant.getQuantityTodoPlaceDbl();
                moveorderLineToAdd.quantityHandledDbl = 0;
                moveorderLineToAdd.pInsertInDatabaseBln();
                this.placeLinesObl.add(moveorderLineToAdd);

            }

            return  true;

        } else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETMOVEORDERLINES);
            return false;
        }


    }

    public boolean pGetLinesMTViaWebserviceBln(Boolean pvRefreshBln) {

        if (pvRefreshBln) {
            cMoveorderLine.allLinesObl = null;
            this.takeLinesObl = null;
            this.placeLinesObl = null;
            cMoveorderLine.pTruncateTableBln();
            cMoveItemVariant.allMoveItemVariantObl = null;
        }
        cWebresult WebResult = this.getMoveorderViewModel().pGetLinesFromWebserviceWrs();
        if (WebResult.getResultBln() && WebResult.getSuccessBln()) {

            if (cMoveorderLine.allLinesObl == null) {
                cMoveorderLine.allLinesObl = new ArrayList<>();
                this.takeLinesObl = new ArrayList<>();
                this.placeLinesObl = new ArrayList<>();
                cMoveItemVariant.allMoveItemVariantObl = new LinkedHashMap<>();
            }

            for (JSONObject jsonObject : WebResult.getResultDtt()) {
                cMoveorderLine moveorderLine = new cMoveorderLine(jsonObject);
                moveorderLine.pInsertInDatabaseBln();

                // Try to get ItemVariant from SortedList
                cMoveItemVariant moveItemVariant = cMoveItemVariant.allMoveItemVariantObl.get(moveorderLine.getKeyStr());

                // Create ItemVariant if it doens't exist
                if (moveItemVariant == null) {
                    moveItemVariant = new cMoveItemVariant(moveorderLine.getItemNoStr(), moveorderLine.getVariantCodeStr());
                    moveItemVariant.quantityPlacedDbl = 0;
                    moveItemVariant.quantityTakenDbl = 0;

                    cMoveItemVariant.allMoveItemVariantObl.put(moveItemVariant.getKeyStr(), moveItemVariant);
                }

                if (moveItemVariant.linesObl == null) {
                    moveItemVariant.linesObl = new ArrayList<>();
                }

                // Add barcode to ItemVariant
                moveItemVariant.linesObl.add(moveorderLine);


                if (moveorderLine.getActionTypeCodeStr().equalsIgnoreCase(cWarehouseorder.ActionTypeEnu.TAKE.toString())) {
                    cMoveorder.currentMoveOrder.takeLinesObl.add(moveorderLine);
                    moveItemVariant.quantityTakenDbl += moveorderLine.getQuantityHandledDbl();

                    if (moveorderLine.getQuantityHandledDbl() > 0) {
                        if (moveItemVariant.getHandledTimeStampStr() == null || cText.pStringToDateStr(moveorderLine.getHandledTimeStampStr(), "YYYY-MM-dd").after(cText.pStringToDateStr(moveItemVariant.getHandledTimeStampStr(), "YYYY-MM-dd"))) {
                            moveItemVariant.handledTimeStampStr = moveorderLine.getHandledTimeStampStr();
                        }
                    }
                }

                if (moveorderLine.getActionTypeCodeStr().equalsIgnoreCase(cWarehouseorder.ActionTypeEnu.PLACE.toString())) {
                    cMoveorder.currentMoveOrder.placeLinesObl.add(moveorderLine);
                    moveItemVariant.quantityPlacedDbl += moveorderLine.getQuantityHandledDbl();

                }
            }

            return  true;

        } else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETMOVEORDERLINES);
            return false;
        }


    }

    public boolean pGetLineBarcodesViaWebserviceBln(Boolean pvRefreshBln) {


        if (pvRefreshBln) {
            cMoveorderLineBarcode.allMoveorderLineBarcodesObl = null;
        }

        cWebresult WebResult = this.getMoveorderViewModel().pGetLineBarcodesFromWebserviceWrs();
        if (WebResult.getResultBln() && WebResult.getSuccessBln()) {

            if (cMoveorderLineBarcode.allMoveorderLineBarcodesObl == null) {
                cMoveorderLineBarcode.allMoveorderLineBarcodesObl= new ArrayList<>();
            }

            for (JSONObject jsonObject : WebResult.getResultDtt()) {
                cMoveorderLineBarcode moveorderLineBarcode = new cMoveorderLineBarcode(jsonObject);
                moveorderLineBarcode.pInsertInDatabaseBln();
            }

            return  true;

        } else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETMOVEORDERLINEBARCODES);
            return false;
        }


    }

    public boolean pGetBINSViaWebserviceBln() {

        List<String> binsObl = new ArrayList<>();

        for (cMoveorderLine moveorderLine : this.linesObl()) {
            if (!binsObl.contains(moveorderLine.getBinCodeStr()) && !moveorderLine.getBinCodeStr().isEmpty()) {
                binsObl.add(moveorderLine.getBinCodeStr());
            }
        }

        cWebresult WebResult = this.getMoveorderViewModel().pGetBINSFromWebserviceWrs(binsObl);
        if (WebResult.getResultBln() && WebResult.getSuccessBln()) {

            if (WebResult.getResultDtt().size() != binsObl.size()) {
                return  false;
            }

            for (JSONObject jsonObject : WebResult.getResultDtt()) {
                cBranchBin branchBin = new cBranchBin(jsonObject);
                if (cUser.currentUser.currentBranch.binsObl == null) {
                    cUser.currentUser.currentBranch.binsObl = new ArrayList<>();
                }

                if (this.binsObl == null) {
                    this.binsObl = new ArrayList<>();
                }

                cUser.currentUser.currentBranch.binsObl.add(branchBin);
                this.binsObl.add(branchBin);
            }

            return  true;

        } else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETMOVEORDERLINEBARCODES);
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

            if (cMoveorder.currentMoveOrder.articleObl == null) {
                cMoveorder.currentMoveOrder.articleObl = new TreeMap<>();
            }

            for (  JSONObject jsonObject : WebResult.getResultDtt()) {
                cMoveorderBarcode moveorderBarcode = new cMoveorderBarcode(jsonObject);
                moveorderBarcode.pInsertInDatabaseBln();

                if (!cMoveorder.currentMoveOrder.articleObl.containsKey(moveorderBarcode.getItemNoAndVariantCodeStr())) {
                    cArticle article = new cArticle(jsonObject);
                    article.descriptionStr = moveorderBarcode.getBarcodeStr();
                    cMoveorder.currentMoveOrder.articleObl.put(moveorderBarcode.getItemNoAndVariantCodeStr(),article);
                }

            }

            return  true;
        }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETMOVEORDERBARCODES);
            return  false;
        }
    }

    public static List<cMoveorder> pGetMovesWithFilterFromDatabasObl() {

        List<cMoveorder> resultObl = new ArrayList<>();
        List<cMoveorderEntity> hulpResultObl;

        cMoveorderViewModel  moveorderViewModel = new ViewModelProvider(cAppExtension.fragmentActivity).get(cMoveorderViewModel.class);

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

    public cMoveorderLine pGetTakeLineForCurrentArticleAndBin(cMoveorderBarcode pvMoveorderBarcode ) {

        if (this.linesObl() == null || this.linesObl().size() == 0 || cMoveorder.currentMoveOrder.currentBranchBin  == null) {
            return  null;
        }

        for (cMoveorderLine moveorderLine : this.linesObl()) {

            if (!moveorderLine.getActionTypeCodeStr().equalsIgnoreCase(cWarehouseorder.ActionTypeEnu.TAKE.toString())) {
                continue;
            }


            //Check if item matches scanned item
            if (moveorderLine.getItemNoStr().equalsIgnoreCase(pvMoveorderBarcode.getItemNoStr()) &&
                 moveorderLine.getVariantCodeStr().equalsIgnoreCase(pvMoveorderBarcode.getVariantCodeStr())) {

                if (moveorderLine.getBinCodeStr().equalsIgnoreCase(cMoveorder.currentMoveOrder.currentBranchBin.getBinCodeStr())) {
                    return  moveorderLine;
                }
            }
        }

        return  null;
    }

    public List<cMoveorderLine> pGetTakeLinesForCurrentBin() {

        List<cMoveorderLine> resulObl = new ArrayList<>();

        if (this.takeLinesObl == null || this.takeLinesObl.size() == 0 || cMoveorder.currentMoveOrder.currentBranchBin  == null) {
            return  resulObl;
        }

        for (cMoveorderLine moveorderLine : this.takeLinesObl) {
            if (moveorderLine.getBinCodeStr().equalsIgnoreCase(cMoveorder.currentMoveOrder.currentBranchBin.getBinCodeStr())) {
                resulObl.add(moveorderLine);
            }
        }

        return  resulObl;
    }

    public cBranchBin pGetBin(String pvBincodeStr) {



        if (this.binsObl == null || this.binsObl.size() == 0) {
            return null;
        }

        for (cBranchBin branchBin : this.binsObl) {
            if (branchBin.getBinCodeStr().equalsIgnoreCase(pvBincodeStr)) {
                return  branchBin;
            }
        }

        return  null;
    }

    public boolean pAddUnkownBarcodeAndItemVariantBln(cBarcodeScan pvBarcodeScan) {

        cWebresult WebResult;

        //Then add barcode
        WebResult =  this.getMoveorderLineViewModel().pAddUnknownBarcodeViaWebserviceWrs(pvBarcodeScan);
        if (WebResult.getResultBln()&& WebResult.getSuccessBln() ){

            if (WebResult.getResultDtt().size() == 1) {
                cMoveorderBarcode moveorderBarcode = new cMoveorderBarcode(WebResult.getResultDtt().get(0));

                if (cMoveorderBarcode.allMoveorderBarcodesObl == null) {
                    cMoveorderBarcode.allMoveorderBarcodesObl= new ArrayList<>();
                }

                if (cMoveorder.currentMoveOrder.articleObl == null) {
                    cMoveorder.currentMoveOrder.articleObl = new TreeMap<>();
                }

                cMoveorderBarcode.allMoveorderBarcodesObl.add(moveorderBarcode);
                cMoveorder.currentMoveOrder.currentMoveorderBarcode = moveorderBarcode;

               cMoveorder.currentMoveOrder.currentArticle= new cArticle(cMoveorder.currentMoveOrder.currentMoveorderBarcode.getItemNoStr(),cMoveorder.currentMoveOrder.currentMoveorderBarcode.getVariantCodeStr());
               cMoveorder.currentMoveOrder.currentArticle.barcodesObl = new ArrayList<>();
               cMoveorder.currentMoveOrder.currentArticle.barcodesObl.add(new cArticleBarcode(cMoveorder.currentMoveOrder.currentMoveorderBarcode));
               cMoveorder.currentMoveOrder.articleObl.put(cMoveorder.currentMoveOrder.currentArticle.getItemNoAndVariantCodeStr(),cMoveorder.currentMoveOrder.currentArticle);

            }
        }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_MOVEBARCODECREATE);
            return  false;
        }

        return  true;
    }

    public boolean pAddERPBarcodeBln(cBarcodeScan pvBarcodeScan) {

        //Get article info via the web service
        cMoveorder.currentMoveOrder.currentArticle  = cArticle.pGetArticleByBarcodeViaWebservice(pvBarcodeScan);

        //We failed to get the article
        if ( cMoveorder.currentMoveOrder.currentArticle== null) {
            return false;
        }

        //Add article to article object list
        cMoveorder.currentMoveOrder.articleObl.put(cMoveorder.currentMoveOrder.currentArticle.getItemNoAndVariantCodeStr(), cMoveorder.currentMoveOrder.currentArticle);


        //Get barcodes for this article
        if (! cMoveorder.currentMoveOrder.currentArticle.pGetBarcodesViaWebserviceBln()) {
            return false;
        }

        if (cMoveorderBarcode.allMoveorderBarcodesObl == null) {
            cMoveorderBarcode.allMoveorderBarcodesObl= new ArrayList<>();
        }

        for (cArticleBarcode articleBarcode :  cMoveorder.currentMoveOrder.currentArticle.barcodesObl) {
            if (articleBarcode.getBarcodeStr().equalsIgnoreCase(pvBarcodeScan.getBarcodeOriginalStr()) ||
               articleBarcode.getBarcodeWithoutCheckDigitStr().equalsIgnoreCase(pvBarcodeScan.getBarcodeOriginalStr())) {

                //Then add barcode
                cWebresult WebResult =  this.getMoveorderLineViewModel().pAddERPBarcodeViaWebserviceWrs(pvBarcodeScan);
                if (WebResult.getResultBln()&& WebResult.getSuccessBln() ){
                    cMoveorderBarcode moveorderBarcode = new cMoveorderBarcode(WebResult.getResultDtt().get(0));
                    cMoveorderBarcode.allMoveorderBarcodesObl.add(moveorderBarcode);
                    cMoveorder.currentMoveOrder.currentMoveorderBarcode= moveorderBarcode;
                }

            }
        }


        return  true;
    }

    public boolean pMoveTakeItemHandledBln(List<cMoveorderBarcode> pvScannedBarcodesObl) {
        cWebresult WebResult;


        List<cMoveorderBarcode> sortedBarcodeObl = this.mSortedBarcodeListObl(pvScannedBarcodesObl);

        WebResult =  this.getMoveorderLineViewModel().pMoveItemTakeHandledViaWebserviceWrs(sortedBarcodeObl);

        if (WebResult.getResultBln() && WebResult.getSuccessBln()){

            for ( JSONObject jsonObject : WebResult.getResultDtt()) {
                cMoveorderLine moveorderLine = new cMoveorderLine(jsonObject);
                moveorderLine.pInsertInDatabaseBln();
                cMoveorder.currentMoveOrder.takeLinesObl.add(moveorderLine);

                cMoveItemVariant moveItemVariant = cMoveItemVariant.allMoveItemVariantObl.get(moveorderLine.getKeyStr());
                if (moveItemVariant == null) {
                    moveItemVariant  = new cMoveItemVariant(moveorderLine.getItemNoStr(),moveorderLine.getVariantCodeStr());
                    moveItemVariant.quantityPlacedDbl = 0;
                    moveItemVariant.quantityTakenDbl = 0;
                    moveItemVariant.linesObl = new ArrayList<>();
                    cMoveItemVariant.allMoveItemVariantObl.put(moveorderLine.getKeyStr(),moveItemVariant);
                }

                //Add line to variant
                moveItemVariant.linesObl.add(moveorderLine);

                for (cMoveorderLine loopMoveorderLine : cMoveorder.currentMoveOrder.placeLinesObl) {
                    if (loopMoveorderLine.getItemNoStr().equalsIgnoreCase(moveorderLine.getItemNoStr()) && loopMoveorderLine.getVariantCodeStr().equalsIgnoreCase(moveorderLine.getVariantCodeStr())) {
                        loopMoveorderLine.quantityTakenDbl += moveorderLine.quantityHandledDbl;
                        loopMoveorderLine.handledTimeStampStr = moveorderLine.handledTimeStampStr;
                    }
                }

                moveItemVariant.quantityTakenDbl += moveorderLine.quantityHandledDbl;

                cMoveorderLine moveorderLineToAdd = new cMoveorderLine(moveorderLine.getItemNoStr(),moveorderLine.getVariantCodeStr());
                moveorderLineToAdd.actionTypeCodeStr = cWarehouseorder.ActionTypeEnu.PLACE.toString();
                moveorderLineToAdd.descriptionStr = moveorderLine.getDescriptionStr();
                moveorderLineToAdd.description2Str = moveorderLine.getDescription2Str();
                moveorderLineToAdd.quantityDbl = moveorderLine.getQuantityHandledDbl();
                cMoveorder.currentMoveOrder.placeLinesObl.add(moveorderLineToAdd);

                //Refresh line barcodes
                cMoveorder.currentMoveOrder.pGetLineBarcodesViaWebserviceBln(true);
                return true;
            }



            return  true;
        }
        else {

            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_PICKORDERLINE_HANDLED);
            return  false;
        }
    }

    public boolean pMoveTakeMTItemHandledBln(List<cMoveorderBarcode> pvScannedBarcodesObl) {
        cWebresult WebResult;


        List<cMoveorderBarcode> sortedBarcodeObl = this.mSortedBarcodeListObl(pvScannedBarcodesObl);

        WebResult =  this.getMoveorderLineViewModel().pMoveItemTakeMTHandledViaWebserviceWrs(sortedBarcodeObl);

        if (WebResult.getResultBln() && WebResult.getSuccessBln()){

//            for ( JSONObject jsonObject : WebResult.getResultDtt()) {
//                cMoveorderLine moveorderLine = new cMoveorderLine(jsonObject);
//                moveorderLine.pInsertInDatabaseBln();
//                cMoveorder.currentMoveOrder.takeLinesObl.add(moveorderLine);
//
//                cMoveItemVariant moveItemVariant = cMoveItemVariant.allMoveItemVariantObl.get(moveorderLine.getKeyStr());
//                if (moveItemVariant == null) {
//                    moveItemVariant  = new cMoveItemVariant(moveorderLine.getItemNoStr(),moveorderLine.getVariantCodeStr());
//                    moveItemVariant.quantityPlacedDbl = 0;
//                    moveItemVariant.quantityTakenDbl = 0;
//                    moveItemVariant.linesObl = new ArrayList<>();
//                    cMoveItemVariant.allMoveItemVariantObl.put(moveorderLine.getKeyStr(),moveItemVariant);
//                }
//
//                //Add line to variant
//                moveItemVariant.linesObl.add(moveorderLine);
//
//                for (cMoveorderLine loopMoveorderLine : cMoveorder.currentMoveOrder.placeLinesObl) {
//                    if (loopMoveorderLine.getItemNoStr().equalsIgnoreCase(moveorderLine.getItemNoStr()) && loopMoveorderLine.getVariantCodeStr().equalsIgnoreCase(moveorderLine.getVariantCodeStr())) {
//                        loopMoveorderLine.quantityTakenDbl += moveorderLine.quantityHandledDbl;
//                        loopMoveorderLine.handledTimeStampStr = moveorderLine.handledTimeStampStr;
//                    }
//                }
//
//                moveItemVariant.quantityTakenDbl += moveorderLine.quantityHandledDbl;
//
//                cMoveorderLine moveorderLineToAdd = new cMoveorderLine(moveorderLine.getItemNoStr(),moveorderLine.getVariantCodeStr());
//                moveorderLineToAdd.actionTypeCodeStr = cWarehouseorder.ActionTypeEnu.PLACE.toString();
//                moveorderLineToAdd.descriptionStr = moveorderLine.getDescriptionStr();
//                moveorderLineToAdd.description2Str = moveorderLine.getDescription2Str();
//                moveorderLineToAdd.quantityDbl = moveorderLine.getQuantityHandledDbl();
//                cMoveorder.currentMoveOrder.placeLinesObl.add(moveorderLineToAdd);
//
//                //Refresh line barcodes
//                cMoveorder.currentMoveOrder.pGetLineBarcodesViaWebserviceBln(true);
//                return true;
//            }

            return  true;
        }
        else {

            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_PICKORDERLINE_HANDLED);
            return  false;
        }
    }


    public boolean pMovePlaceItemHandledBln(List<cMoveorderBarcode> pvScannedBarcodesObl) {
        cWebresult WebResult;

        List<cMoveorderBarcode> sortedBarcodeObl = this.mSortedBarcodeListObl(pvScannedBarcodesObl);

        WebResult =  this.getMoveorderLineViewModel().pMoveItemPlaceHandledViaWebserviceWrs(sortedBarcodeObl);

        if (WebResult.getResultBln() && WebResult.getSuccessBln()){

            for ( JSONObject jsonObject : WebResult.getResultDtt()) {
                cMoveorderLine moveorderLine = new cMoveorderLine(jsonObject);
                moveorderLine.pInsertInDatabaseBln();

                //Get ItemVariant
                cMoveItemVariant moveItemVariant = cMoveItemVariant.allMoveItemVariantObl.get(moveorderLine.getKeyStr());

                //Edit quantity for this ItemVariant
                Objects.requireNonNull(moveItemVariant).quantityPlacedDbl +=  moveorderLine.getQuantityHandledDbl();

                //Add line to variant
                moveItemVariant.linesObl.add(moveorderLine);

                //Init new list for lines that need to be removed
                List<cMoveorderLine> linesToBeRemovedObl = new ArrayList<>();

                //Edit all place lines for this variant
                for (cMoveorderLine loopMoveorderPlaceLine : this.placeLinesObl) {
                    if (loopMoveorderPlaceLine.getItemNoStr().equalsIgnoreCase(moveorderLine.getItemNoStr()) && loopMoveorderPlaceLine.getVariantCodeStr().equalsIgnoreCase(moveorderLine.getVariantCodeStr())) {

                        //This line should be replaced
                        if (loopMoveorderPlaceLine.getQuantityTakenDbl() == moveorderLine.getQuantityHandledDbl() && loopMoveorderPlaceLine.getQuantityPlacedDbl() == 0) {
                            linesToBeRemovedObl.add(loopMoveorderPlaceLine);
                            continue;
                        }

                        loopMoveorderPlaceLine.quantityPlacedDbl += moveorderLine.getQuantityHandledDbl();
                    }
                }

                //Remove lines from placelines Obl if needed
                for (cMoveorderLine moveorderLineToBeRemoved : linesToBeRemovedObl ) {
                    this.placeLinesObl.remove(moveorderLineToBeRemoved);
                    moveItemVariant.linesObl.remove(moveorderLineToBeRemoved);
                    cMoveorder.currentMoveOrder.linesObl().remove(moveorderLineToBeRemoved);
                }


                //Add the new line
                cMoveorder.currentMoveOrder.placeLinesObl.add(moveorderLine);
                return true;
            }
            return  true;
        }
        else {

            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_PICKORDERLINE_HANDLED);
            return  false;
        }
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

    private List<cMoveorderBarcode> mSortedBarcodeListObl(List<cMoveorderBarcode> pvUnsortedBarcodeObl) {

        List<cMoveorderBarcode> resultObl = new ArrayList<>();
        boolean barcodeFoundBln = false;

        for (cMoveorderBarcode moveorderBarcode : pvUnsortedBarcodeObl) {

            for (cMoveorderBarcode resultBarcode : resultObl) {
                if (resultBarcode.getBarcodeStr().equalsIgnoreCase(moveorderBarcode.getBarcodeStr())) {
                    resultBarcode.quantityHandled +=  moveorderBarcode.quantityPerUnitOfMeasure;
                    barcodeFoundBln = true;
                }
            }


            if (barcodeFoundBln) {
                barcodeFoundBln = false;
            }

            else {
                //new barcode, so add
                moveorderBarcode.quantityHandled = moveorderBarcode.quantityPerUnitOfMeasure;
                resultObl.add(moveorderBarcode);
            }

        }


        return resultObl;
    }

    //End Region Private Methods



}
