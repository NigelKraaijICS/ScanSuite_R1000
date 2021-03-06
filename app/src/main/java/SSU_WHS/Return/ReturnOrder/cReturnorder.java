package SSU_WHS.Return.ReturnOrder;

import androidx.lifecycle.ViewModelProvider;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cDeviceInfo;
import ICS.Utils.cResult;
import ICS.Utils.cSharedPreferences;
import ICS.Utils.cText;
import ICS.Utils.cUserInterface;
import ICS.Weberror.cWeberror;
import ICS.cAppExtension;
import SSU_WHS.Basics.Article.cArticle;
import SSU_WHS.Basics.ArticleBarcode.cArticleBarcode;
import SSU_WHS.Basics.Settings.cSetting;
import SSU_WHS.Basics.Users.cUser;
import SSU_WHS.General.Comments.cComment;
import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.General.Warehouseorder.cWarehouseorderViewModel;
import SSU_WHS.Return.ReturnorderBarcode.cReturnorderBarcode;
import SSU_WHS.Return.ReturnorderBarcode.cReturnorderBarcodeViewModel;
import SSU_WHS.Return.ReturnorderDocument.cReturnorderDocument;
import SSU_WHS.Return.ReturnorderLine.cReturnorderLine;
import SSU_WHS.Return.ReturnorderLineBarcode.cReturnorderLineBarcode;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebserviceDefinitions;
import nl.icsvertex.scansuite.Activities.Returns.ReturnorderDocumentActivity;
import nl.icsvertex.scansuite.R;

public class cReturnorder {

    private final String ordernumberStr;
    public String getOrderNumberStr() {return this.ordernumberStr;}

    private final String ordertypeStr;
    public String getOrderTypeStr() {return this.ordertypeStr;}

    private final String assignedUserIdStr;
    public String getAssignedUserIdStr() {return this.assignedUserIdStr;}

    private final String currentUserIdStr;
    public String getCurrentUserIdStr() {return this.currentUserIdStr;}

    private String stockownerStr;
    public String getStockownerStr() {
        return stockownerStr;
    }

    private final Integer statusInt;
    public Integer getStatusInt() {return this.statusInt;}

    private final String binCodeStr;
    public String getBinCodeStr() {return  this.binCodeStr;}

    private String currentLocationStr;
    public  String  getCurrentLocationStr(){return  currentLocationStr;}

    private final String externalReferenceStr;
    public String getExternalReferenceStr() {return this.externalReferenceStr;}

    private final Boolean retourMultiDocumentBln;
    public Boolean getRetourMultiDocumentBln(){return this.retourMultiDocumentBln;}

    private final int sourceDocumentInt;
    private int getSourceDocumentInt() { return sourceDocumentInt; }

    private final String documentStr;
    public String getDocumentStr() {return this.documentStr;}

    private final String reasonStr;
    public String getReasonStr() {return this.reasonStr;}

    private final boolean returnWithPictureBln;
    public boolean isReturnWithPictureBln() {
        return returnWithPictureBln;
    }


    private final boolean retourOrderBINNoCheckBln;
    public  boolean getRetourOrderBINNoCheckBln() { return  retourOrderBINNoCheckBln;}

    public int unknownVariantCounterInt = 0;
    public int getUnknownVariantCounterInt() {
        return unknownVariantCounterInt;
    }

    private final boolean retourMultiDocument;
    public boolean isRetourMultiDocument(){return this.retourMultiDocument;}

    public static String getNumberOfOrdersStr(){
        return  cAppExtension.context.getResources().getQuantityString(R.plurals.plural_parameter1_orders, cReturnorder.allReturnordersObl.size(),cReturnorder.allReturnordersObl.size());
    }

    public static String getNumberOfFilteredOrdersStr(){
     return    "(" + cText.pIntToStringStr(cReturnorder.pGetReturnOrdersWithFilterFromDatabasObl().size())  + "/" + cText.pIntToStringStr(cReturnorder.allReturnordersObl.size()) + ") " + cAppExtension.activity.getString(R.string.orders) + " " + cAppExtension.activity.getString(R.string.shown);
    }

    public String getNumberOfItemsStr(){
       return cAppExtension.activity.getString(R.string.items) + ' ' + this.pGetTotalCountInt();
    }

    public String getCountForCurrentSourceDocumentStr(){
        return cText.pIntToStringStr(this.pGetCountForSourceDocumentInt(cReturnorderDocument.currentReturnOrderDocument));
    }

    private final cReturnorderEntity returnorderEntity;

    public Boolean isGeneratedBln() {
        return this.getSourceDocumentInt() == cWarehouseorder.SourceDocumentTypeEnu.Generated;
    }

    private List<cComment> commentsObl() {
        return cComment.allCommentsObl;
    }

    public List<cReturnorderBarcode> barcodesObl () {return  cReturnorderBarcode.allReturnorderBarcodesObl;}

    public List<cReturnorderLine> linesObl () {return  cReturnorderLine.allLinesObl;}

    public static List<cReturnorder> allReturnordersObl;
    public static cReturnorder currentReturnOrder;
    public static String currentNewDocumentScan = "";

    private cReturnorderViewModel getReturnorderViewModel(){
        return new ViewModelProvider(cAppExtension.fragmentActivity).get(cReturnorderViewModel.class);
    }

    private cWarehouseorderViewModel getWarehouseorderViewModel(){
        return new ViewModelProvider(cAppExtension.fragmentActivity).get(cWarehouseorderViewModel.class);
    }

    private cReturnorderBarcodeViewModel getReturnorderBarcodeViewModel(){
        return new ViewModelProvider(cAppExtension.fragmentActivity).get(cReturnorderBarcodeViewModel.class);
    }


    //Region Public Properties

    //Region Constructor

    public cReturnorder(JSONObject pvJsonObject) {

        this.returnorderEntity = new cReturnorderEntity(pvJsonObject);
        this.ordernumberStr =this.returnorderEntity.getOrdernumberStr();
        this.ordertypeStr = this.returnorderEntity.getOrderTypeStr();
        this.assignedUserIdStr = this.returnorderEntity.getAssignedUserIdStr();
        this.stockownerStr= this.returnorderEntity.getStockownerStr();
        this.currentUserIdStr = this.returnorderEntity.getCurrentUserIdStr();
        this.statusInt = cText.pStringToIntegerInt(this.returnorderEntity.getStatusStr());
        this.binCodeStr = this.returnorderEntity.getBincodeStr();
        this.currentLocationStr = this.returnorderEntity.getCurrentLocationStr();
        this.externalReferenceStr = this.returnorderEntity.getExternalReferenceStr();
        this.retourMultiDocumentBln = this.returnorderEntity.getRetourMultiDocumentBln();
        this.sourceDocumentInt = cText.pStringToIntegerInt(this.returnorderEntity.getSourceDocumentStr());
        this.documentStr = this.returnorderEntity.getDocumentStr();
        this.reasonStr = this.returnorderEntity.getReasonStr();
        this.returnWithPictureBln = this.returnorderEntity.getReturnWithPictureBln();
        this.retourMultiDocument = this.returnorderEntity.getRetourMultiDocumentBln();
        this.retourOrderBINNoCheckBln = this.returnorderEntity.getRetourOrderBINNoCheckBln();

    }

    public cReturnorder(cReturnorderEntity pvReturnorderEntity) {

        this.returnorderEntity = pvReturnorderEntity;
        this.ordernumberStr =this.returnorderEntity.getOrdernumberStr();
        this.ordertypeStr = this.returnorderEntity.getOrderTypeStr();
        this.assignedUserIdStr = this.returnorderEntity.getAssignedUserIdStr();
        this.stockownerStr= this.returnorderEntity.getStockownerStr();
        this.currentUserIdStr = this.returnorderEntity.getCurrentUserIdStr();
        this.statusInt = cText.pStringToIntegerInt(this.returnorderEntity.getStatusStr());
        this.binCodeStr = this.returnorderEntity.getBincodeStr();
        this.currentLocationStr = this.returnorderEntity.getCurrentLocationStr();
        this.externalReferenceStr = this.returnorderEntity.getExternalReferenceStr();
        this.retourMultiDocumentBln = this.returnorderEntity.getRetourMultiDocumentBln();
        this.sourceDocumentInt = cText.pStringToIntegerInt(this.returnorderEntity.getSourceDocumentStr());
        this.documentStr = this.returnorderEntity.getDocumentStr();
        this.reasonStr = this.returnorderEntity.getReasonStr();
        this.returnWithPictureBln = this.returnorderEntity.getReturnWithPictureBln();
        this.retourMultiDocument = this.returnorderEntity.getRetourMultiDocumentBln();
        this.retourOrderBINNoCheckBln = this.returnorderEntity.getRetourOrderBINNoCheckBln();
    }

    //End Region Constructor

    //Region Public Methods

    public boolean pInsertInDatabaseBln() {

        this.getReturnorderViewModel().insert(this.returnorderEntity);

        if (cReturnorder.allReturnordersObl == null) {
            cReturnorder.allReturnordersObl = new ArrayList<>();
        }

        cReturnorder.allReturnordersObl.add(this);
        return true;
    }

    public static cResult pCreateReturnOrderViaWebserviceRst(String pvDocumentStr, Boolean pvMultipleDocumentsBln, String pvBinCodeStr) {

        cResult result;
        result = new cResult();
        result.resultBln = true;

        cWebresult WebResult;

        cReturnorderViewModel returnorderViewModel =  new ViewModelProvider(cAppExtension.fragmentActivity).get(cReturnorderViewModel.class);
        WebResult = returnorderViewModel.pCreateReturnOrderViaWebserviceWrs(pvDocumentStr, pvMultipleDocumentsBln, pvBinCodeStr);
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

            cReturnorder returnorder = new cReturnorder(WebResult.getResultDtt().get(0));
            returnorder.pInsertInDatabaseBln();
            cReturnorder.currentReturnOrder = returnorder;


        } else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_RETURNORDERCREATE);
            result.resultBln = false;
            result.pAddErrorMessage(WebResult.getResultStr());
        }
        return result;
    }

    public static boolean pGetReturnOrdersViaWebserviceBln(Boolean pvRefreshBln, String pvSearchTextStr) {

        if (pvRefreshBln) {
            cReturnorder.allReturnordersObl = null;
            cReturnorder.mTruncateTableBln();
        }

        cReturnorderViewModel returnorderViewModel =  new ViewModelProvider(cAppExtension.fragmentActivity).get(cReturnorderViewModel.class);
        cWebresult WebResult= returnorderViewModel.pGetReturnordersFromWebserviceWrs(pvSearchTextStr);

        if (WebResult.getResultBln() && WebResult.getSuccessBln()) {

            for (JSONObject jsonObject : WebResult.getResultDtt()) {
                cReturnorder returnorder = new cReturnorder(jsonObject);
                returnorder.pInsertInDatabaseBln();
            }
            return true;
        } else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_RETURNORDERSGET);
            return false;
        }
    }

    private boolean mDocumentsViaWebserviceBln() {

            cReturnorderLine.allLinesObl = null;
            cReturnorderDocument.allReturnorderDocumentObl = null;

        cReturnorderBarcode returnorderBarcode;

        //Get all lines from webservice
        cWebresult WebResult=  this.getReturnorderViewModel().pGetLinesFromWebserviceWrs();
        if (WebResult.getResultBln() && WebResult.getSuccessBln() ){


            for (JSONObject jsonObject : WebResult.getResultDtt()) {

                if (cReturnorderLine.allLinesObl == null) {
                    cReturnorderLine.allLinesObl  = new ArrayList<>();
                }


                //Convert JSON object to line object
                cReturnorderLine returnorderLine = new cReturnorderLine(jsonObject);

                //Get matching barcodeStr for this line
                returnorderBarcode = cReturnorder.currentReturnOrder.mGetBarcodeForLine(returnorderLine);

                //Init line numbers object list
                returnorderLine.lineNumberObl = new ArrayList<>();

                //Loop through all line barcodes, if barcodeStr matches then add LineNo to LineNumber object list
                for (cReturnorderLineBarcode returnorderLineBarcode : cReturnorderLineBarcode.allLineBarcodesObl){
                    if (returnorderLineBarcode.getBarcodeStr().equalsIgnoreCase( Objects.requireNonNull(returnorderBarcode).getBarcodeStr())){
                        returnorderLine.lineNumberObl.add(returnorderLineBarcode.getLineNoLng());
                    }
                }

                // Add object to all object list
                cReturnorderLine.allLinesObl.add(returnorderLine);

                //Get return document for this line
                cReturnorderDocument returnorderDocument = cReturnorderDocument.pGetReturnDocument(returnorderLine.getDocumentStr());
                if (returnorderDocument == null ) {
                    //This is a new document, create object and add this line
                    cReturnorderDocument returnDocumentToAdd = new cReturnorderDocument(returnorderLine.getDocumentStr());
                    returnDocumentToAdd.pInsertInDatabaseBln();
                    returnDocumentToAdd.pAddReturnorderLine(returnorderLine);
                }
                else {
                    //This is a known document, add this line
                    returnorderDocument.pAddReturnorderLine(returnorderLine);
                }
            }


            if (cReturnorderLine.allLinesObl == null){
                cReturnorderDocument returnorderDocument  = new cReturnorderDocument(currentReturnOrder.getDocumentStr());
                returnorderDocument.pInsertInDatabaseBln();
            }

            this.mSetDocumentStatus();
            return  true;


        }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_RETURNLINESGET);
            return  false;
        }
    }

    public cResult pLockViaWebserviceRst(cWarehouseorder.StepCodeEnu pvStepCodeEnu, int pvWorkFlowStepInt) {

        //Initialise result
        cResult result;
        result = new cResult();
        result.resultBln = true;

        cWebresult Webresult;
        boolean ignoreBusyBln = false;


        if (cSetting.GENERIC_UNLOCK_BUSY_ORDERS_ALLOWED()) {
            if (this.getStatusInt() > 10 && cUser.currentUser.getUsernameStr().equalsIgnoreCase(this.getCurrentUserIdStr())) {
                ignoreBusyBln = true;
            }
        }

        Webresult = this.getWarehouseorderViewModel().pLockWarehouseopdrachtViaWebserviceWrs(cWarehouseorder.OrderTypeEnu.RETOUR.toString(), this.getOrderNumberStr(), cDeviceInfo.getSerialnumberStr(), pvStepCodeEnu.toString(), pvWorkFlowStepInt, ignoreBusyBln);

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
                Webresult.getResultObl().size() > 0 && !Webresult.getResultObl().get(0).equalsIgnoreCase(cUser.currentUser.getUsernameStr())) {
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

            cReturnorder.currentReturnOrder.mGetCommentsViaWebError(Webresult.getResultDtt());
            return result;
        }

        return result;

    }

    public cResult pGetOrderDetailsRst(){

        cResult result;

        result = new cResult();
        result.resultBln = true;

        //Get all linesInt for current order, if size = 0 or webservice error then stop
        if (!cReturnorder.currentReturnOrder.pGetLinesViaWebserviceBln(true)) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_returnorderlines_failed));
            return result;
        }

        // Get all comments
        if (!cReturnorder.currentReturnOrder.mGetCommentsViaWebserviceBln()) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_comments_failed));
            return result;
        }
        //Get all barcodes
        if (!cReturnorder.currentReturnOrder.pGetBarcodesViaWebserviceBln(true)) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_barcodes_failed));
            return result;
        }
        //Get all ReturnHandledlines
        if (!cReturnorder.currentReturnOrder.mGetHandledLinesViaWebserviceBln()){
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_returnorderlines_failed));
            return result;
        }

        //Get all Returnlinebarcodes
        if (!cReturnorder.currentReturnOrder.pGetLineBarcodesViaWebserviceBln()) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_line_barcodes_failed));
            return result;
        }
        if(!cReturnorder.currentReturnOrder.mDocumentsViaWebserviceBln()){
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_returnordersdocuments_failed));
            return result;
        }

        return  result;
    }

    public boolean pReturnorderDisposedBln() {

        cWebresult Webresult;

        Webresult = this.getReturnorderViewModel().pReturnDisposedViaWebserviceWrs();

        if (!Webresult.getSuccessBln() || !Webresult.getResultBln()) {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_RETURNORDERDISPOSED);
            return false;
        }
        return true;
    }

    public boolean pUpdateCurrentLocationBln(String pvCurrentLocationStr) {


        cWebresult Webresult;

        Webresult = this.getReturnorderViewModel().pUpdateCurrentLocationViaWebserviceWrs(pvCurrentLocationStr);
        if (!Webresult.getSuccessBln() || !Webresult.getResultBln()) {
            return  false;
        }

        this.currentLocationStr = pvCurrentLocationStr;
        return  true;

    }

    public boolean pLockReleaseViaWebserviceBln(cWarehouseorder.StepCodeEnu pvStepCodeEnu, int pvWorkFlowStepInt) {

        cWebresult Webresult = this.getWarehouseorderViewModel().pLockReleaseWarehouseorderViaWebserviceWrs(cWarehouseorder.OrderTypeEnu.RETOUR.toString(), this.getOrderNumberStr(), cDeviceInfo.getSerialnumberStr(), pvStepCodeEnu.toString(), pvWorkFlowStepInt);
        return Webresult.getSuccessBln() && Webresult.getResultBln();
    }

    public boolean pDeleteDetailsBln() {
        cReturnorderLine.pTruncateTableBln();
        cReturnorderDocument.pTruncateTableBln();
        cReturnorderBarcode.pTruncateTableBln();
        cReturnorderLineBarcode.pTruncateTableBln();
        return true;
    }

    public boolean pGetLinesViaWebserviceBln(Boolean pvRefreshBln) {

        if (pvRefreshBln) {
            cReturnorderLine.allLinesObl = null;
            cReturnorderLine.pTruncateTableBln();
        }

        cWebresult WebResult = this.getReturnorderViewModel().pGetLinesFromWebserviceWrs();

        if (WebResult.getResultBln() && WebResult.getSuccessBln()) {

            if (cReturnorderLine.allLinesObl == null) {
                cReturnorderLine.allLinesObl = new ArrayList<>();
            }

            for (JSONObject jsonObject : WebResult.getResultDtt()) {
                cReturnorderLine returnorderLine = new cReturnorderLine(jsonObject);
                returnorderLine.pInsertInDatabaseBln();
            }

            return  true;

        } else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_RETURNLINESGET);
            return false;
        }
    }

    public cResult pDeleteDetailsFromDocument (){
        cResult result;
        result = new cResult();
        List<cReturnorderLine> returnorderLineObl;

        result.resultBln = true;

        returnorderLineObl = cReturnorderDocument.currentReturnOrderDocument.returnorderLineObl;

        for (cReturnorderLine returnorderLine : returnorderLineObl){
            cReturnorderLine.currentReturnOrderLine = returnorderLine;
            result = cReturnorderLine.currentReturnOrderLine.pResetDocumentRst();
        }

        cReturnorderDocument.currentReturnOrderDocument.returnorderLineObl.clear();
        cReturnorderDocument.currentReturnOrderDocument.statusInt = cWarehouseorder.ReturnDocumentStatusEnu.New;
        return result;

    }

    public cResult pResetLinesToZeroFromDocument (){
        cResult result;
        result = new cResult();

        result.resultBln = true;

        for (cReturnorderLine returnorderLine : cReturnorderDocument.currentReturnOrderDocument.returnorderLineObl){
            //Set the current barcodeStr
            cReturnorderBarcode returnorderBarcode = cReturnorder.currentReturnOrder.mGetBarcodeForLine(returnorderLine);

            cReturnorderLine.currentReturnOrderLine = returnorderLine;
            //Create new line barcodeStr
            cReturnorderLine.currentReturnOrderLine.pAddLineBarcode(Objects.requireNonNull(returnorderBarcode).getBarcodeStr(), -(returnorderLine.getQuantityHandledTakeDbl()));
            cReturnorderLine.currentReturnOrderLine.quantityHandledTakeDbl = 0.0;

            if (!cReturnorderLine.currentReturnOrderLine.pSaveLineViaWebserviceBln()) {
                cUserInterface.pDoExplodingScreen(cAppExtension.activity.getString(R.string.message_line_save_failed),"",true,true);

                if (cAppExtension.activity instanceof ReturnorderDocumentActivity ) {
                    ReturnorderDocumentActivity returnorderDocumentActivity = (ReturnorderDocumentActivity)cAppExtension.activity;
                    returnorderDocumentActivity.pHandleFragmentDismissed();
                    cAppExtension.dialogFragment.dismiss();
                    return result;
                }


            }
            //Change quantityDbl handled in database
            cReturnorderLine.currentReturnOrderLine.pUpdateQuantityInDatabase();
        }
        cReturnorderDocument.currentReturnOrderDocument.statusInt = cWarehouseorder.ReturnDocumentStatusEnu.New;
        return result;
    }

    private int pGetTotalCountInt() {

        Double resultDbl = (double) 0;
        int resultInt = 0;
        if (cReturnorderDocument.allReturnorderDocumentObl == null || cReturnorderDocument.allReturnorderDocumentObl.size() == 0) {
            return  resultInt;
        }

        for ( cReturnorderDocument returnorderDocument: cReturnorderDocument.allReturnorderDocumentObl){
            for( cReturnorderLine returnorderLine : returnorderDocument.returnorderLineObl){
                resultDbl += returnorderLine.getQuantitytakeDbl();
            }
        }
        resultInt += resultDbl.intValue();
        return resultInt;
    }

    public int pGetCountForSourceDocumentInt(cReturnorderDocument pvReturnorderDocument) {

        Double resultDbl = (double) 0;
        int resultInt = 0;

        for( cReturnorderLine returnorderLine : pvReturnorderDocument.returnorderLineObl){

            if (returnorderLine.isGeneratedBln()) {
                resultDbl +=  returnorderLine.getQuantityHandledTakeDbl();
            }
            else {
                resultDbl +=  returnorderLine.getQuantitytakeDbl();
            }
        }
        resultInt += resultDbl.intValue();
        return resultInt;
    }

    public int pGetHandledCountForSourceDocumentInt(cReturnorderDocument pvReturnorderDocument) {

        double resultDbl = 0;
        int resultInt = 0;

        for( cReturnorderLine returnorderLine : pvReturnorderDocument.returnorderLineObl){
            resultDbl += returnorderLine.getQuantityHandledTakeDbl();
        }
        resultInt += resultDbl;
        return resultInt;
    }

    public List<cReturnorderDocument> pGetDocumentsDoneFromDatabasObl() {

        List<cReturnorderDocument> resultObl = new ArrayList<>();
        Integer statusInt;
        if (cReturnorderDocument.returnorderDocumentsDoneObl == null) {
            cReturnorderDocument.returnorderDocumentsDoneObl = new ArrayList<>();
        }
        else {
            cReturnorderDocument.returnorderDocumentsDoneObl.clear();
        }

        for (cReturnorderDocument returnorderDocument : cReturnorderDocument.allReturnorderDocumentObl){

            statusInt = returnorderDocument.getStatusInt();
            if (statusInt.equals(cWarehouseorder.ReturnDocumentStatusEnu.ReturnDone)){
                resultObl.add(returnorderDocument);
                cReturnorderDocument.returnorderDocumentsDoneObl.add(returnorderDocument);
            }
        }

        return  resultObl;
    }

    public List<cReturnorderDocument> pGetDocumentsNotDoneFromDatabasObl() {

        List<cReturnorderDocument> resultObl = new ArrayList<>();


        if (cReturnorderDocument.returnorderDocumentsTodoObl == null) {
            cReturnorderDocument.returnorderDocumentsTodoObl = new ArrayList<>();
        }
        else {
            cReturnorderDocument.returnorderDocumentsTodoObl.clear();
        }

        for (cReturnorderDocument returnorderDocument : cReturnorderDocument.allReturnorderDocumentObl){

            if ( returnorderDocument.getStatusInt() < cWarehouseorder.ReturnDocumentStatusEnu.ReturnDone) {
                resultObl.add(returnorderDocument);
                cReturnorderDocument.returnorderDocumentsTodoObl.add(returnorderDocument);
            }
        }

        return  resultObl;
    }

    public List<cReturnorderDocument> pGetDocumentsTotalFromDatabasObl() {

        List<cReturnorderDocument> resultObl = new ArrayList<>();
        if (cReturnorderDocument.returnorderDocumentsTotalObl == null) {
            cReturnorderDocument.returnorderDocumentsTotalObl = new ArrayList<>();
        }
        else {
            cReturnorderDocument.returnorderDocumentsTotalObl.clear();
        }
        for (cReturnorderDocument returnorderDocument : cReturnorderDocument.allReturnorderDocumentObl){

            resultObl.add(returnorderDocument);
            cReturnorderDocument.returnorderDocumentsTotalObl.add(returnorderDocument);
        }

        return  resultObl;
    }

    public cReturnorderDocument pGetDocument(String pvSourcedocumentStr) {

        List<cReturnorderDocument> hulpObl;

        hulpObl = this.pGetDocumentsTotalFromDatabasObl();

        if (hulpObl == null || hulpObl.size() == 0) {
            return null;
        }

        for (cReturnorderDocument returnorderDocument : hulpObl) {
            if (returnorderDocument.getSourceDocumentStr().equalsIgnoreCase(pvSourcedocumentStr)) {
                return  returnorderDocument;
            }
        }

        return  null;
    }

    private boolean mGetHandledLinesViaWebserviceBln() {

            cReturnorderLine.allLinesObl = null;
            cReturnorderLine.pTruncateTableBln();

        cWebresult WebResult;

        WebResult = this.getReturnorderViewModel().pGetHandledLinesFromWebserviceWrs();
        if (WebResult.getResultBln() && WebResult.getSuccessBln()) {

            if (cReturnorderLine.allLinesObl == null) {
                cReturnorderLine.allLinesObl = new ArrayList<>();
            }

            for (JSONObject jsonObject : WebResult.getResultDtt()) {
                cReturnorderLine returnorderLine = new cReturnorderLine(jsonObject);
                returnorderLine.pInsertInDatabaseBln();
            }

            return  true;

        } else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_RETURNLINESCANNEDGET);
            return false;
        }
    }

    private boolean mGetCommentsViaWebserviceBln() {


            cComment.allCommentsObl = null;
            cComment.pTruncateTableBln();
            cComment.commentsShownBln = false;


        cWebresult webresult =  this.getReturnorderViewModel().pGetCommentsFromWebserviceWrs();
        if (webresult.getResultBln() && webresult.getSuccessBln()) {

            cComment.allCommentsObl = new ArrayList<>();

            for (JSONObject jsonObject : webresult.getResultDtt()) {
                cComment comment = new cComment(jsonObject);
                comment.pInsertInDatabaseBln();
            }

            return true;
        }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_RETURNORDERCOMMENTSGET);
            return false;
        }
    }

    public boolean pGetBarcodesViaWebserviceBln(Boolean pvRefreshBln) {

        if (pvRefreshBln) {
            cReturnorderBarcode.allReturnorderBarcodesObl = null;
            cReturnorderBarcode.pTruncateTableBln();
        }

        cWebresult WebResult = this.getReturnorderViewModel().pGetBarcodesFromWebserviceWrs();

        if (WebResult.getResultBln() && WebResult.getSuccessBln()){

            if (cReturnorderBarcode.allReturnorderBarcodesObl == null) {
                cReturnorderBarcode.allReturnorderBarcodesObl = new ArrayList<>();
            }

            for (JSONObject jsonObject : WebResult.getResultDtt()) {
                cReturnorderBarcode returnorderBarcode = new cReturnorderBarcode(jsonObject);

                returnorderBarcode.pInsertInDatabaseBln();
            }

            return  true;
        }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_RETURNBARCODEGET);
            return  false;
        }
    }

    private boolean pGetLineBarcodesViaWebserviceBln() {

            cReturnorderLineBarcode.allLineBarcodesObl = null;
            cReturnorderLineBarcode.pTruncateTableBln();


        cReturnorderLineBarcode.allLineBarcodesObl = new ArrayList<>();
        cWebresult WebResult = this.getReturnorderViewModel().pGetLineBarcodesFromWebserviceWrs();

        if (WebResult.getResultBln() && WebResult.getSuccessBln()){

            for (JSONObject jsonObject : WebResult.getResultDtt()) {
                cReturnorderLineBarcode returnorderLineBarcode = new cReturnorderLineBarcode(jsonObject);
                cReturnorderLineBarcode.allLineBarcodesObl.add(returnorderLineBarcode);

                //Search for line that belongs to this barcodeStr
                cReturnorderLine returnorderLine = cReturnorder.currentReturnOrder.mGetLineWithLineNo(returnorderLineBarcode.getLineNoLng());

                //If we can't find it, skip this
                if (returnorderLine == null) {
                    continue;
                }

                //Add barcodeStr to line
                returnorderLine.pAddLineBarcode(returnorderLineBarcode.getBarcodeStr(),returnorderLineBarcode.getQuantityhandledDbl());
            }
            }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_RETURNLINESCANNEDBARCODESGET);
            return  false;
        }

        return  true;
    }

    public boolean pAddUnkownBarcodeBln(cBarcodeScan pvBarcodeScan) {

        cReturnorder.currentReturnOrder.unknownVariantCounterInt += 1;
        cWebresult WebResult =  this.getReturnorderViewModel().pAddUnknownItemViaWebserviceWrs(pvBarcodeScan);


        if (WebResult.getResultBln() && WebResult.getSuccessBln()) {

            for (JSONObject jsonObject : WebResult.getResultDtt()) {
                cReturnorderBarcode returnorderBarcode = new cReturnorderBarcode(jsonObject);
                returnorderBarcode.pInsertInDatabaseBln();
                cReturnorderBarcode.currentReturnOrderBarcode = returnorderBarcode;
            }

            WebResult = this.getReturnorderBarcodeViewModel().pCreateBarcodeViaWebserviceWrs();
            if (WebResult.getResultBln() && WebResult.getSuccessBln()) {

                for (JSONObject jsonObject : WebResult.getResultDtt()) {
                    cReturnorderBarcode returnorderBarcode = new cReturnorderBarcode(jsonObject);
                    returnorderBarcode.pInsertInDatabaseBln();
                    cReturnorderBarcode.currentReturnOrderBarcode = returnorderBarcode;
                }
            }
            cReturnorderLine returnorderLine = new cReturnorderLine(cReturnorderBarcode.currentReturnOrderBarcode);
            returnorderLine.pInsertInDatabaseBln();
            returnorderLine.nieuweRegelBln = true;
            cReturnorderLine.currentReturnOrderLine = returnorderLine;

        } else {
            cReturnorder.currentReturnOrder.unknownVariantCounterInt -= 1;
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_RETURNBARCODECREATE);
            return false;
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
                articleBarcode.getBarcodeStr().equalsIgnoreCase(pvBarcodeScan.getBarcodeFormattedStr())) {
                matchedArticleBarcode = articleBarcode;
                break;
            }
        }

        //We didn't find a match, so no use in adding the line
        if (matchedArticleBarcode == null) {
            return  false;
        }

        cReturnorderLine returnorderLine = new cReturnorderLine(cArticle.currentArticle);
        returnorderLine.pInsertInDatabaseBln();
        returnorderLine.nieuweRegelBln = true;
        cReturnorderLine.currentReturnOrderLine = returnorderLine;

        cWebresult WebResult =  this.getReturnorderViewModel().pAddERPItemViaWebserviceWrs(matchedArticleBarcode);
        if (WebResult.getResultBln() && WebResult.getSuccessBln()){
            for (cArticleBarcode articleBarcode :  cArticle.currentArticle.barcodesObl) {
                cReturnorderBarcode returnorderBarcode = new cReturnorderBarcode(articleBarcode);
                returnorderBarcode.pInsertInDatabaseBln();


                if (returnorderBarcode.getBarcodeStr().equalsIgnoreCase(pvBarcodeScan.getBarcodeOriginalStr())) {
                    cReturnorderBarcode.currentReturnOrderBarcode = returnorderBarcode;
                }
            }
        }
        else {
            cReturnorder.currentReturnOrder.unknownVariantCounterInt -= 1;
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_RETURNBARCODECREATE);
            return  false;
        }

        return  true;
    }

    public cReturnorderBarcode pGetBarcodeForSelectedLine (){

        for (cReturnorderBarcode returnorderBarcode : cReturnorder.currentReturnOrder.barcodesObl()){
            if (returnorderBarcode.getItemNoStr().equalsIgnoreCase(cReturnorderLine.currentReturnOrderLine.getItemNoStr()) && returnorderBarcode.getVariantCodeStr().equalsIgnoreCase(cReturnorderLine.currentReturnOrderLine.getVariantCodeStr())){
                return returnorderBarcode;
            }
        }
        return null;
    }

    public static List<cReturnorder> pGetReturnOrdersWithFilterFromDatabasObl() {

        List<cReturnorder> resultObl = new ArrayList<>();
        List<cReturnorderEntity> hulpResultObl;

        cReturnorderViewModel returnorderViewModel=  new ViewModelProvider(cAppExtension.fragmentActivity).get(cReturnorderViewModel.class);
        hulpResultObl =  returnorderViewModel.pGetReturnOrdersWithFilterFromDatabaseObl(cUser.currentUser.getUsernameStr(), cSharedPreferences.userReturnFilterBln());
        if (hulpResultObl == null || hulpResultObl.size() == 0) {
            return  resultObl;
        }

        for (cReturnorderEntity returnorderEntity : hulpResultObl ) {
            cReturnorder returnorder = new cReturnorder(returnorderEntity);
            resultObl.add(returnorder);
        }

        return  resultObl;
    }

    public List<cComment> pFeedbackCommentObl(){

        if (cReturnorder.currentReturnOrder.commentsObl() == null || cReturnorder.currentReturnOrder.commentsObl().size() == 0) {
            return  null;
        }

        return cComment.pGetCommentsForTypeObl(cWarehouseorder.CommentTypeEnu.FEEDBACK);

    }

    public cReturnorderBarcode pGetOrderBarcode(cBarcodeScan pvBarcodescan) {

        if (this.barcodesObl() == null || this.barcodesObl().size() == 0)  {
            return  null;
        }

        for (cReturnorderBarcode returnorderBarcode : this.barcodesObl()) {

            if (returnorderBarcode.getBarcodeStr().equalsIgnoreCase(pvBarcodescan.getBarcodeOriginalStr()) ||
                    returnorderBarcode.getBarcodeStr().equalsIgnoreCase(pvBarcodescan.getBarcodeFormattedStr())) {
                return  returnorderBarcode;
            }
        }

        return  null;

    }

    public cReturnorderLine pGetLineForArticle() {

        if (cReturnorderDocument.currentReturnOrderDocument.returnorderLineObl == null || cReturnorderDocument.currentReturnOrderDocument.returnorderLineObl.size() == 0) {
            return  null;
        }

        for (cReturnorderLine returnorderLine : cReturnorderDocument.currentReturnOrderDocument.returnorderLineObl) {
            //Check if item matches scanned item
            if (returnorderLine.getItemNoStr().equalsIgnoreCase(cReturnorderBarcode.currentReturnOrderBarcode.getItemNoStr()) &&
                returnorderLine.getVariantCodeStr().equalsIgnoreCase(cReturnorderBarcode.currentReturnOrderBarcode.getVariantCodeStr())) {
                return returnorderLine;
            }
        }

        return  null;
    }

    public cResult pOrderHandledViaWebserviceRst() {

        cResult result;
        result = new cResult();
        result.resultBln = true;


        cWebresult webresult;

        webresult = this.getReturnorderViewModel().pHandledViaWebserviceWrs();

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

            cReturnorder.currentReturnOrder.mGetCommentsViaWebError(webresult.getResultDtt());
            return result;
        }

        return  result;


    }

    //Region Public Methods

    //Region Private Methods

    private static void mTruncateTableBln() {
        cReturnorderViewModel returnorderViewModel=  new ViewModelProvider(cAppExtension.fragmentActivity).get(cReturnorderViewModel.class);
        returnorderViewModel.deleteAll();
    }

    private void mGetCommentsViaWebError(List<JSONObject> pvResultDtt) {

        cComment.allCommentsObl = new ArrayList<>();

        for (JSONObject jsonObject : pvResultDtt){
            cComment comment = new cComment(jsonObject);
            comment.pInsertInDatabaseBln();
        }

    }

    private void mSetDocumentStatus(){

        int quantityInt;
        Integer quantityHandledInt;

        if (cReturnorderDocument.allReturnorderDocumentObl == null){
            return;
        }

        for (cReturnorderDocument returnorderDocument : cReturnorderDocument.allReturnorderDocumentObl){
            quantityInt = pGetCountForSourceDocumentInt(returnorderDocument);
            quantityHandledInt = pGetHandledCountForSourceDocumentInt(returnorderDocument);

            if (quantityHandledInt.equals(0)){
                returnorderDocument.statusInt = cWarehouseorder.ReturnDocumentStatusEnu.New;
            }
            else {
                if(quantityHandledInt.equals(quantityInt)){
                    returnorderDocument.statusInt = cWarehouseorder.ReturnDocumentStatusEnu.ReturnDone;
                }
                else {
                    returnorderDocument.statusInt = cWarehouseorder.ReturnDocumentStatusEnu.InventoryPause;
                }
            }
        }

    }

    private cReturnorderBarcode mGetBarcodeForLine(cReturnorderLine pvReturnorderLine){
        for (cReturnorderBarcode returnorderBarcode : cReturnorder.currentReturnOrder.barcodesObl()){
            if (returnorderBarcode.getItemNoStr().equalsIgnoreCase(pvReturnorderLine.getItemNoStr()) && returnorderBarcode.getVariantCodeStr().equalsIgnoreCase(pvReturnorderLine.getVariantCodeStr())){
                return returnorderBarcode;
            }
        }
        return null;
    }

    private  cReturnorderLine mGetLineWithLineNo(Long pvLineNoLng) {

        if (this.linesObl() == null || this.linesObl().size() == 0) {
            return  null;
        }

        for (cReturnorderLine returnorderLine : this.linesObl()) {

            if (cText.pIntToStringStr(returnorderLine.getLineNoInt()).equalsIgnoreCase(cText.pLongToStringStr(pvLineNoLng))) {
                return  returnorderLine;
            }

        }

        return  null;

    }

    //End Region Private Methods





    //End Region Private Methods

}
