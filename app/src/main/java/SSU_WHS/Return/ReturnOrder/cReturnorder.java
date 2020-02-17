package SSU_WHS.Return.ReturnOrder;

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
import ICS.Utils.cUserInterface;
import ICS.Weberror.cWeberror;
import ICS.cAppExtension;
import SSU_WHS.Basics.Article.cArticle;
import SSU_WHS.Basics.ArticleBarcode.cArticleBarcode;
import SSU_WHS.Basics.Settings.cSetting;
import SSU_WHS.Basics.Users.cUser;
import SSU_WHS.General.Comments.cComment;
import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.Return.ReturnorderBarcode.cReturnorderBarcode;
import SSU_WHS.Return.ReturnorderDocument.cReturnorderDocument;
import SSU_WHS.Return.ReturnorderLine.cReturnorderLine;
import SSU_WHS.Return.ReturnorderLineBarcode.cReturnorderLineBarcode;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebserviceDefinitions;
import nl.icsvertex.scansuite.Activities.Returns.ReturnorderDocumentActivity;
import nl.icsvertex.scansuite.R;


public class cReturnorder {

    private String ordernumberStr;
    public String getOrderNumberStr() {return this.ordernumberStr;}

    private String ordertypeStr;
    public String getOrderTypeStr() {return this.ordertypeStr;}

    private String assignedUserIdStr;
    public String getAssignedUserIdStr() {return this.assignedUserIdStr;}

    private String currentUserIdStr;
    public String getCurrentUserIdStr() {return this.currentUserIdStr;}

    private Integer statusInt;
    public Integer getStatusInt() {return this.statusInt;}

    private String binCodeStr;
    public String getBinCodeStr() {return  this.binCodeStr;}

    private String externalReferenceStr;
    public String getExternalReferenceStr() {return this.externalReferenceStr;}

    private Boolean retourMultiDocumentBln;
    public Boolean getRetourMultiDocumentBln(){return this.retourMultiDocumentBln;}

    private int sourceDocumentInt;
    private int getSourceDocumentInt() { return sourceDocumentInt; }

    private String documentStr;
    public String getDocumentStr() {return this.documentStr;}

    private String reasonStr;
    public String getReasonStr() {return this.reasonStr;}

    public int unknownVariantCounterInt = 0;
    public int getUnknownVariantCounterInt() {
        return unknownVariantCounterInt;
    }

    private boolean retourMultiDocument;
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

    private cReturnorderEntity returnorderEntity;

    private static cReturnorderViewModel gReturnorderViewModel;
    private static cReturnorderViewModel getReturnorderViewModel() {
        if (gReturnorderViewModel == null) {
            gReturnorderViewModel = ViewModelProviders.of(cAppExtension.fragmentActivity).get(cReturnorderViewModel.class);
        }
        return gReturnorderViewModel;
    }

    private static cReturnorderAdapter gReturnorderAdapter;
    public static cReturnorderAdapter getReturnorderAdapter() {
        if (gReturnorderAdapter == null) {
            gReturnorderAdapter = new cReturnorderAdapter();
        }
        return gReturnorderAdapter;
    }

    public Boolean isGeneratedBln() {
        return this.getSourceDocumentInt() == cWarehouseorder.SoureDocumentTypeEnu.Generated;
    }

    private List<cComment> commentsObl() {
        return cComment.allCommentsObl;
    }

    public List<cReturnorderBarcode> barcodesObl () {return  cReturnorderBarcode.allReturnorderBarcodesObl;}

    public List<cReturnorderLine> linesObl () {return  cReturnorderLine.allLinesObl;}

    public static List<cReturnorder> allReturnordersObl;
    public static cReturnorder currentReturnOrder;

    //Region Public Properties

    //Region Constructor

    public cReturnorder(JSONObject pvJsonObject) {

        this.returnorderEntity = new cReturnorderEntity(pvJsonObject);
        this.ordernumberStr =this.returnorderEntity.getOrdernumberStr();
        this.ordertypeStr = this.returnorderEntity.getOrderTypeStr();
        this.assignedUserIdStr = this.returnorderEntity.getAssignedUserIdStr();
        this.currentUserIdStr = this.returnorderEntity.getCurrentUserIdStr();
        this.statusInt = cText.pStringToIntegerInt(this.returnorderEntity.getStatusStr());
        this.binCodeStr = this.returnorderEntity.getBincodeStr();
        this.externalReferenceStr = this.returnorderEntity.getExternalReferenceStr();
        this.retourMultiDocumentBln = this.returnorderEntity.getRetourMultiDocumentBln();
        this.sourceDocumentInt = cText.pStringToIntegerInt(this.returnorderEntity.getSourceDocumentStr());
        this.documentStr = this.returnorderEntity.getDocumentStr();
        this.reasonStr = this.returnorderEntity.getReasonStr();
        this.retourMultiDocument = this.returnorderEntity.getRetourMultiDocumentBln();

    }

    public cReturnorder(cReturnorderEntity pvReturnorderEntity) {

        this.returnorderEntity = pvReturnorderEntity;
        this.ordernumberStr =this.returnorderEntity.getOrdernumberStr();
        this.ordertypeStr = this.returnorderEntity.getOrderTypeStr();
        this.assignedUserIdStr = this.returnorderEntity.getAssignedUserIdStr();
        this.currentUserIdStr = this.returnorderEntity.getCurrentUserIdStr();
        this.statusInt = cText.pStringToIntegerInt(this.returnorderEntity.getStatusStr());
        this.binCodeStr = this.returnorderEntity.getBincodeStr();
        this.externalReferenceStr = this.returnorderEntity.getExternalReferenceStr();
        this.retourMultiDocumentBln = this.returnorderEntity.getRetourMultiDocumentBln();
        this.sourceDocumentInt = cText.pStringToIntegerInt(this.returnorderEntity.getSourceDocumentStr());
        this.documentStr = this.returnorderEntity.getDocumentStr();
        this.reasonStr = this.returnorderEntity.getReasonStr();
        this.retourMultiDocument = this.returnorderEntity.getRetourMultiDocumentBln();
    }

    //End Region Constructor

    //Region Public Methods

    public boolean pInsertInDatabaseBln() {

        SSU_WHS.Return.ReturnOrder.cReturnorder.getReturnorderViewModel().insert(this.returnorderEntity);

        if (cReturnorder.allReturnordersObl == null) {
            cReturnorder.allReturnordersObl = new ArrayList<>();
        }

        cReturnorder.allReturnordersObl.add(this);
        return true;
    }

    public static Boolean pCreateReturnOrderViaWebserviceBln(String pvDocumentStr, Boolean pvMultipleDocumentsBln, String pvBinCodeStr) {

        cWebresult WebResult;

        WebResult = cReturnorder.getReturnorderViewModel().pCreateReturnOrderViaWebserviceWrs(pvDocumentStr, pvMultipleDocumentsBln, pvBinCodeStr);
        if (WebResult.getResultBln() && WebResult.getSuccessBln()) {
            if (WebResult.getResultDtt() != null && WebResult.getResultDtt().size() == 1) {
                cReturnorder returnorder = new SSU_WHS.Return.ReturnOrder.cReturnorder(WebResult.getResultDtt().get(0));
                returnorder.pInsertInDatabaseBln();
                cReturnorder.currentReturnOrder = returnorder;
                return  true;
            }
        } else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_RETURNORDERCREATE);
            return false;
        }

        return false;
    }

    public static boolean pGetReturnOrdersViaWebserviceBln(Boolean pvRefreshBln, String pvSearchTextStr) {

        if (pvRefreshBln) {
            cReturnorder.allReturnordersObl = null;
            cReturnorder.mTruncateTableBln();
        }

        cWebresult WebResult= cReturnorder.getReturnorderViewModel().pGetReturnordersFromWebserviceWrs(pvSearchTextStr);

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

    public boolean pDocumentsViaWebserviceBln(Boolean pvRefreshBln) {

        if (pvRefreshBln) {
            cReturnorderLine.allLinesObl = null;
            cReturnorderDocument.allReturnorderDocumentObl = null;
        }

        cReturnorderBarcode returnorderBarcode;

        //Get all lines from webservice
        cWebresult WebResult=  cReturnorder.getReturnorderViewModel().pGetLinesFromWebserviceWrs();
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
            if (this.getStatusInt() > 10 && cUser.currentUser.getNameStr().equalsIgnoreCase(this.getCurrentUserIdStr())) {
                ignoreBusyBln = true;
            }
        }

        Webresult = cWarehouseorder.getWarehouseorderViewModel().pLockWarehouseopdrachtViaWebserviceWrs(cWarehouseorder.OrderTypeEnu.RETOUR.toString(), this.getOrderNumberStr(), cDeviceInfo.getSerialnumberStr(), pvStepCodeEnu.toString(), pvWorkFlowStepInt, ignoreBusyBln);

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

            cReturnorder.currentReturnOrder.mGetCommentsViaWebError(Webresult.getResultDtt());
            return result;
        }

        return result;

    }

    public boolean pReturnorderDisposedBln() {

        cWebresult Webresult;

        Webresult = cReturnorder.getReturnorderViewModel().pReturnDisposedViaWebserviceWrs();

        if (!Webresult.getSuccessBln() || !Webresult.getResultBln()) {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_RETURNORDERDISPOSED);
            return false;
        }
        return true;
    }

    public boolean pLockReleaseViaWebserviceBln(cWarehouseorder.StepCodeEnu pvStepCodeEnu, int pvWorkFlowStepInt) {

        cWebresult Webresult = cWarehouseorder.getWarehouseorderViewModel().pLockReleaseWarehouseorderViaWebserviceWrs(cWarehouseorder.OrderTypeEnu.RETOUR.toString(), this.getOrderNumberStr(), cDeviceInfo.getSerialnumberStr(), pvStepCodeEnu.toString(), pvWorkFlowStepInt);
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

        cWebresult WebResult = cReturnorder.getReturnorderViewModel().pGetLinesFromWebserviceWrs();

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
                ReturnorderDocumentActivity.pHandleFragmentDismissed();
                cAppExtension.dialogFragment.dismiss();
                return result;
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
            resultDbl +=  returnorderLine.getQuantitytakeDbl();

        }
        resultInt += resultDbl.intValue();
        return resultInt;
    }

    public int pGetHandledCountForSourceDocumentInt(cReturnorderDocument pvReturnorderDocument) {

        double resultDbl = (double) 0;
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

    public boolean pGetHandledLinesViaWebserviceBln(Boolean pvRefreshBln) {

        if (pvRefreshBln) {
            cReturnorderLine.allLinesObl = null;
            cReturnorderLine.pTruncateTableBln();
        }

        cWebresult WebResult;

        WebResult = SSU_WHS.Return.ReturnOrder.cReturnorder.getReturnorderViewModel().pGetHandledLinesFromWebserviceWrs();
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

    public boolean pGetCommentsViaWebserviceBln(Boolean pvRefeshBln) {

        if (pvRefeshBln) {
            cComment.allCommentsObl = null;
            cComment.pTruncateTableBln();
            cComment.commentsShownBln = false;
        }

        cWebresult webresult =  cReturnorder.getReturnorderViewModel().pGetCommentsFromWebserviceWrs();
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

        cWebresult WebResult = cReturnorder.getReturnorderViewModel().pGetBarcodesFromWebserviceWrs();

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

    public boolean pGetLineBarcodesViaWebserviceBln(Boolean pvRefreshBln) {

        if (pvRefreshBln) {
            cReturnorderLineBarcode.allLineBarcodesObl = null;
            cReturnorderLineBarcode.pTruncateTableBln();
        }

        cReturnorderLineBarcode.allLineBarcodesObl = new ArrayList<>();
        cWebresult WebResult = cReturnorder.getReturnorderViewModel().pGetLineBarcodesFromWebserviceWrs();

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
        cWebresult WebResult =  cReturnorder.getReturnorderViewModel().pAddUnknownItemViaWebserviceWrs(pvBarcodeScan);


        if (WebResult.getResultBln() && WebResult.getSuccessBln()) {

            for (JSONObject jsonObject : WebResult.getResultDtt()) {
                cReturnorderBarcode returnorderBarcode = new cReturnorderBarcode(jsonObject);
                returnorderBarcode.pInsertInDatabaseBln();
                cReturnorderBarcode.currentReturnOrderBarcode = returnorderBarcode;
            }

            WebResult = cReturnorderBarcode.getReturnorderBarcodeViewModel().pCreateBarcodeViaWebserviceWrs();
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
        if (!cArticle.currentArticle.pGetBarcodesViaWebserviceBln()) {
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

        cWebresult WebResult =  cReturnorder.getReturnorderViewModel().pAddERPItemViaWebserviceWrs(matchedArticleBarcode);
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

        hulpResultObl =  cReturnorder.getReturnorderViewModel().pGetReturnOrdersWithFilterFromDatabaseObl(cUser.currentUser.getNameStr(), cSharedPreferences.userFilterBln());
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
                if (returnorderLine.quantityHandledTakeDbl < returnorderLine.getQuantitytakeDbl()) {
                    return  returnorderLine;
                }
            }
        }

        return  null;
    }

    public cResult pOrderHandledViaWebserviceRst() {

        cResult result;
        result = new cResult();
        result.resultBln = true;


        cWebresult webresult;

        webresult = cReturnorder.getReturnorderViewModel().pHandledViaWebserviceWrs();

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
        cReturnorder.getReturnorderViewModel().deleteAll();
    }

    private void mGetCommentsViaWebError(List<JSONObject> pvResultDtt) {

        cComment.allCommentsObl = new ArrayList<>();

        for (JSONObject jsonObject : pvResultDtt){
            cComment comment = new cComment(jsonObject);
            comment.pInsertInDatabaseBln();
        }

    }

    private void mSetDocumentStatus(){

        Integer quantityInt;
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
