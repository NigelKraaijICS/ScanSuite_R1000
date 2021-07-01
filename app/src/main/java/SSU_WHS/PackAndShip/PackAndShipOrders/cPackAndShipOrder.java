package SSU_WHS.PackAndShip.PackAndShipOrders;

import androidx.lifecycle.ViewModelProvider;

import org.json.JSONArray;
import org.json.JSONException;
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
import SSU_WHS.Basics.Settings.cSetting;
import SSU_WHS.Basics.ShippingAgentServiceShippingUnits.cShippingAgentServiceShippingUnit;
import SSU_WHS.Basics.ShippingAgentServices.cShippingAgentService;
import SSU_WHS.Basics.ShippingAgents.cShippingAgent;
import SSU_WHS.Basics.Users.cUser;
import SSU_WHS.General.Comments.cComment;
import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.General.Warehouseorder.cWarehouseorderViewModel;
import SSU_WHS.General.cDatabase;
import SSU_WHS.PackAndShip.PackAndShipAddress.cPackAndShipAddress;
import SSU_WHS.PackAndShip.PackAndShipBarcode.cPackAndShipBarcode;
import SSU_WHS.PackAndShip.PackAndShipLines.cPackAndShipLine;
import SSU_WHS.PackAndShip.PackAndShipSetting.cPackAndShipSetting;
import SSU_WHS.PackAndShip.PackAndShipShipment.cPackAndShipShipment;
import SSU_WHS.PackAndShip.PackAndShipShippingMethod.cPackAndShipShippingMethod;
import SSU_WHS.PackAndShip.PackAndShipShippingPackage.cPackAndShipShippingPackage;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebserviceDefinitions;
import nl.icsvertex.scansuite.R;


public class cPackAndShipOrder {

    private final int numberOfBinsInt;
    public int getNumberOfBinsInt() {
        return numberOfBinsInt;
    }

    private final String assignedUserIdStr;
    public String getAssignedUserIdStr() {
        return assignedUserIdStr;
    }

    public String currentLocationStr;
    public String getCurrentLocationStr() {
        return currentLocationStr;
    }

    public String currentUserIdStr;
    public String getCurrentUserIdStr() {
        return currentUserIdStr;
    }

    public String destinationNoStr;
    public String getDestinationNoStr() {
        return destinationNoStr;
    }

    private final String documentStr;
    public String getDocumentStr() {
        return documentStr;
    }

    private final String document2Str;
    public String getDocument2Str() {
        return document2Str;
    }

    private final String documentTypeStr;
    public String getDocumentTypeStr() {
        return documentTypeStr;
    }

    private final String documentType2Str;
    public String getDocumentType2Str() {
        return documentType2Str;
    }

    private final String externalReferenceStr;
    public String getExternalReferenceStr() {
        return externalReferenceStr;
    }

    private final String orderNumberStr;
    public String getOrderNumberStr() {
        return orderNumberStr;
    }

    private final String orderTypeStr;
    public String getOrderTypeStr() {
        return orderTypeStr;
    }

    private final int sourceDocumentInt;
    private int getSourceDocumentInt() {
        return sourceDocumentInt;
    }

    public int statusInt;
    public int getStatusInt() {
        return statusInt;
    }

    private final String workplaceStr;
    public String getWorkplaceStr() {
        return workplaceStr;
    }

    public boolean getProcessingOrParkedBln() {

        if (this.getStatusInt() == cWarehouseorder.PackAndShipStatusEnu.Pack_And_Ship_Wait) {
            return  true;
        }

        return this.getStatusInt() == cWarehouseorder.PackAndShipStatusEnu.Pack_And_Ship_Wait;

    }

    public boolean isGeneratedBln() {
       return this.getSourceDocumentInt() == cWarehouseorder.SourceDocumentTypeEnu.Generated;
   }

    private final cPackAndShipOrderEntity packAndShipOrderEntity;

    public List<cComment> commentsObl() {
        return cComment.allCommentsObl;
    }

    public List<cPackAndShipAddress> adressesObl () {return  cPackAndShipAddress.allAddressesObl;}
    public List<cPackAndShipBarcode> barcodesObl () {return  cPackAndShipBarcode.allPackAndShipOrderBarcodesObl;}
    public List<cPackAndShipLine> linesObl () {return  cPackAndShipLine.allLinesObl;}
    public List<cPackAndShipSetting> settingObl () {return  cPackAndShipSetting.allSettingsObl;}
    public List<cPackAndShipShipment> shipmentObl () {return  cPackAndShipShipment.allShipmentsObl;}
    public List<cPackAndShipShippingMethod> shippingMethodObl () {return  cPackAndShipShippingMethod.allShippingMethodsObl;}
    public List<cPackAndShipShippingPackage> shippingPackageObl () {return  cPackAndShipShippingPackage.allPackagesObl;}

    public static List<cPackAndShipOrder> allPackAndShipOrdersObl;
    public static cPackAndShipOrder currentPackAndShipOrder;

    private cPackAndShipOrderViewModel getPackAndShipOrderViewModel () {
        return new ViewModelProvider(cAppExtension.fragmentActivity).get(cPackAndShipOrderViewModel.class);
    }

    private cWarehouseorderViewModel getWarehouseorderViewModel () {
        return new ViewModelProvider(cAppExtension.fragmentActivity).get(cWarehouseorderViewModel.class);
    }


    //Region Public Properties

    //Region Constructor

    public cPackAndShipOrder(JSONObject pvJsonObject) {

        this.packAndShipOrderEntity = new cPackAndShipOrderEntity(pvJsonObject);

        this.numberOfBinsInt = cText.pStringToIntegerInt(this.packAndShipOrderEntity.getNumberofBinsStr());
        this.assignedUserIdStr = this.packAndShipOrderEntity.getAssignedUserIdStr();
        this.currentLocationStr = this.packAndShipOrderEntity.getCurrentLocationStr();
        this.currentUserIdStr = this.packAndShipOrderEntity.getCurrentUserIdStr();
        this.destinationNoStr = this.packAndShipOrderEntity.getDestinationStr();
        this.documentStr = this.packAndShipOrderEntity.getDocumentStr();
        this.document2Str = this.packAndShipOrderEntity.getDocument2Str();
        this.documentTypeStr = this.packAndShipOrderEntity.getDocumentTypeStr();
        this.documentType2Str= this.packAndShipOrderEntity.getDocumentType2Str();
        this.externalReferenceStr = this.packAndShipOrderEntity.getExternalReferenceStr();
        this.orderNumberStr = this.packAndShipOrderEntity.getOrdernumberStr();
        this.orderTypeStr = this.packAndShipOrderEntity.getOrderTypeStr();
        this.sourceDocumentInt = cText.pStringToIntegerInt(this.packAndShipOrderEntity.getSourceDocumentStr()) ;
        this.statusInt = cText.pStringToIntegerInt(this.packAndShipOrderEntity.getStatusStr());
        this.workplaceStr = this.packAndShipOrderEntity.getWorkplaceStr();
    }


    public cPackAndShipOrder(cPackAndShipOrderEntity pvPackAndShipOrderEntity) {

        this.packAndShipOrderEntity = pvPackAndShipOrderEntity;

        this.numberOfBinsInt = cText.pStringToIntegerInt(this.packAndShipOrderEntity.getNumberofBinsStr());
        this.assignedUserIdStr = this.packAndShipOrderEntity.getAssignedUserIdStr();
        this.currentLocationStr = this.packAndShipOrderEntity.getCurrentLocationStr();
        this.currentUserIdStr = this.packAndShipOrderEntity.getCurrentUserIdStr();
        this.destinationNoStr = this.packAndShipOrderEntity.getDestinationStr();
        this.documentStr = this.packAndShipOrderEntity.getDocumentStr();
        this.document2Str = this.packAndShipOrderEntity.getDocument2Str();
        this.documentTypeStr = this.packAndShipOrderEntity.getDocumentTypeStr();
        this.documentType2Str= this.packAndShipOrderEntity.getDocumentType2Str();
        this.externalReferenceStr = this.packAndShipOrderEntity.getExternalReferenceStr();
        this.orderNumberStr = this.packAndShipOrderEntity.getOrdernumberStr();
        this.orderTypeStr = this.packAndShipOrderEntity.getOrderTypeStr();
        this.sourceDocumentInt = cText.pStringToIntegerInt(this.packAndShipOrderEntity.getSourceDocumentStr()) ;
        this.statusInt = cText.pStringToIntegerInt(this.packAndShipOrderEntity.getStatusStr());
        this.workplaceStr = this.packAndShipOrderEntity.getWorkplaceStr();
    }

    //End Region Constructor

    //Region Public Methods

    public boolean pInsertInDatabaseBln() {
        this.getPackAndShipOrderViewModel().insert(this.packAndShipOrderEntity);
        if (cPackAndShipOrder.allPackAndShipOrdersObl == null) {
            cPackAndShipOrder.allPackAndShipOrdersObl = new ArrayList<>();
        }
        cPackAndShipOrder.allPackAndShipOrdersObl.add(this);
        return true;
    }

    public static boolean pGetPackAndShipOrdersViaWebserviceBln(Boolean pvRefreshBln, String pvSearchTextStr) {

        cWebresult WebResult;

        if (pvRefreshBln) {
            cPackAndShipOrder.allPackAndShipOrdersObl = null;
            cPackAndShipOrder.mTruncateTable();
        }
        cPackAndShipOrderViewModel packAndShipOrderViewModel =   new ViewModelProvider(cAppExtension.fragmentActivity).get(cPackAndShipOrderViewModel.class);
        WebResult = packAndShipOrderViewModel.pGetPackAndShipOrdersFromWebserviceWrs(pvSearchTextStr);
        if (WebResult.getResultBln() && WebResult.getSuccessBln()) {

           if(WebResult.getResultDtt() != null && WebResult.getResultDtt().size() > 0) {
               for (JSONObject jsonObject : WebResult.getResultDtt()) {
                   cPackAndShipOrder packAndShipOrder = new cPackAndShipOrder(jsonObject);
                   packAndShipOrder.pInsertInDatabaseBln();
               }
           }

            return  true;


        } else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETPACKANDSHIPORDERS);
            return false;
        }

    }

    public static cResult pCreatePackAndShipOrderPS1ViaWebserviceRst(String pvDocumentStr) {

        cResult result;
        result = new cResult();
        result.resultBln = true;

        cWebresult WebResult;

        cPackAndShipOrderViewModel packAndShipOrderViewModel =   new ViewModelProvider(cAppExtension.fragmentActivity).get(cPackAndShipOrderViewModel.class);
        WebResult = packAndShipOrderViewModel.pCreatePackAndShipOrderPS1ViaWebserviceWrs(pvDocumentStr);

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

            cPackAndShipOrder packAndShipOrder = new cPackAndShipOrder(WebResult.getResultDtt().get(0));
            packAndShipOrder.pInsertInDatabaseBln();
            cPackAndShipOrder.currentPackAndShipOrder = packAndShipOrder;


        } else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_RECEIVECREATE);
            result.resultBln = false;
        }
        return result;
    }

    public static cResult pCreatePackAndShipOrderPSMViaWebserviceRst(String pvDocumentStr) {

        cResult result;
        result = new cResult();
        result.resultBln = true;

        cWebresult WebResult;

        cPackAndShipOrderViewModel packAndShipOrderViewModel =   new ViewModelProvider(cAppExtension.fragmentActivity).get(cPackAndShipOrderViewModel.class);
        WebResult = packAndShipOrderViewModel.pCreatePackAndShipOrderPSMViaWebserviceWrs(pvDocumentStr);

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

            cPackAndShipOrder packAndShipOrder = new cPackAndShipOrder(WebResult.getResultDtt().get(0));
            packAndShipOrder.pInsertInDatabaseBln();
            cPackAndShipOrder.currentPackAndShipOrder = packAndShipOrder;


        } else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_RECEIVECREATE);
            result.resultBln = false;
        }
        return result;
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
            case cWarehouseorder.PackAndShipStatusEnu.Pack_And_Ship:
            case cWarehouseorder.PackAndShipStatusEnu.Pack_And_Ship_Busy:
            case cWarehouseorder.PackAndShipStatusEnu.Pack_And_Ship_Wait:
                Webresult = this.getWarehouseorderViewModel().pLockWarehouseopdrachtViaWebserviceWrs(cWarehouseorder.OrderTypeEnu.INPAKKEN_VERZENDEN.toString(), this.getOrderNumberStr(), cDeviceInfo.getSerialnumberStr(), cWarehouseorder.StepCodeEnu.PackAndShipShipping.toString(), cWarehouseorder.PackAndShipStatusEnu.Pack_And_Ship, ignoreBusyBln);
                this.statusInt = cWarehouseorder.PackAndShipStatusEnu.Pack_And_Ship_Busy;
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

            cPackAndShipOrder.currentPackAndShipOrder.mGetCommentsViaWebError(Webresult.getResultDtt());
            return result;
        }

        return result;

    }

    public boolean pLockReleaseViaWebserviceBln() {

        cWebresult Webresult;

        Webresult = this.getWarehouseorderViewModel().pLockReleaseWarehouseorderViaWebserviceWrs(cWarehouseorder.OrderTypeEnu.INPAKKEN_VERZENDEN.toString(), this.getOrderNumberStr(), cDeviceInfo.getSerialnumberStr(), cWarehouseorder.StepCodeEnu.PackAndShipShipping.toString(), cWarehouseorder.PackAndShipStatusEnu.Pack_And_Ship);

        return Objects.requireNonNull(Webresult).getSuccessBln() && Webresult.getResultBln();
    }

    public boolean pDeleteViaWebserviceBln() {

        cWebresult Webresult;

        Webresult =  this.getPackAndShipOrderViewModel().pDeleteViaWebserviceWrs();
        return Webresult.getResultBln() && Webresult.getSuccessBln();
    }

    public cResult pHandledViaWebserviceRst() {

        cResult result;
        result = new cResult();
        result.resultBln = true;

        cWebresult webresult;

        webresult =  this.getPackAndShipOrderViewModel().pHandledViaWebserviceWrs();

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

            cPackAndShipOrder.currentPackAndShipOrder.mGetCommentsViaWebError(webresult.getResultDtt());
            return result;
        }

        return  result;


    }

    public cResult pGetOrderDetailsRst(){

        cResult result;

        result = new cResult();
        result.resultBln = true;

        //Check all ShippingAgents
        if (cShippingAgent.allShippingAgentsObl == null || cShippingAgent.allShippingAgentsObl.size() == 0) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_no_shippingagents_available));
            return result;
        }

        //Check all ShippingAgents
        if (cShippingAgentService.allShippingAgentServicesObl == null || cShippingAgentService.allShippingAgentServicesObl.size() == 0) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_no_shippingagent_services_available));
            return result;
        }

        //Check all ShippingAgent Shipping Units
        if (cShippingAgentServiceShippingUnit.allShippingAgentServiceShippingUnitsObl == null || cShippingAgentServiceShippingUnit.allShippingAgentServiceShippingUnitsObl.size() == 0) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_no_shippingagent_services_units_available));
            return result;
        }

        //Get all linesInt for current order, if webservice error then stop
        if (!cPackAndShipOrder.currentPackAndShipOrder.pGetLinesViaWebserviceBln(true)) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_lines_failed));
            return result;
        }

        // Get all barcodes, if webservice error then stop
        if (!cPackAndShipOrder.currentPackAndShipOrder.pGetBarcodesViaWebserviceBln(true)) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_barcodes_failed));
            return result;
        }

        // Get all barcodes, if webservice error then stop
        if (!cPackAndShipOrder.currentPackAndShipOrder.pGetShipmentsViaWebserviceBln(true)) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_line_barcodes_failed));
            return result;
        }

        // Get all barcodes, if webservice error then stop
        if (!cPackAndShipOrder.currentPackAndShipOrder.pGetAddressesViaWebserviceBln(true)) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_adresses_failed));
            return result;
        }

        // Get all comments
        if (!cPackAndShipOrder.currentPackAndShipOrder.pGetCommentsViaWebserviceBln(true)) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.error_get_comments_failed));
            return result;
        }

        return  result;
    }

    public cResult pGetFirstDocumentAndDetailsRst(String pvDocumentStr){

        cResult result = new cResult();
        result.resultBln = true;

        cWebresult WebResult = this.getPackAndShipOrderViewModel().pGetDocumentAndDetailsViaWebserviceWrs(pvDocumentStr);
        if (WebResult.getResultBln() && WebResult.getSuccessBln()) {

            for (JSONObject jsonObject : WebResult.getResultDtt()) {
                cPackAndShipShipment packAndShipShipment = new cPackAndShipShipment(jsonObject, true);
                packAndShipShipment.pInsertInDatabaseBln();
                cPackAndShipShipment.currentShipment = packAndShipShipment;

                try {
                    JSONArray adressesObl    = jsonObject.getJSONArray(cDatabase.ADDRESSES_NAMESTR);

                    for (int i = 0, size = adressesObl.length(); i < size; i++)
                    {
                        JSONObject object =adressesObl.getJSONObject(i);
                        cPackAndShipAddress packAndShipAddress = new cPackAndShipAddress(object, true);
                        packAndShipAddress.pInsertInDatabaseBln();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    JSONArray shippingMethodsObl    = jsonObject.getJSONArray(cDatabase.SHIPPINGMETHODS_NAMESTR);

                    for (int i = 0, size = shippingMethodsObl.length(); i < size; i++)
                    {
                        JSONObject object =shippingMethodsObl.getJSONObject(i);
                        cPackAndShipShippingMethod packAndShipShippingMethod = new cPackAndShipShippingMethod(object, "PS1");
                        packAndShipShippingMethod.pInsertInDatabaseBln();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        } else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETSOURCEDOCUMENTSHIPPINGDATA);
            result.resultBln = false;
            result.pAddErrorMessage(WebResult.getResultStr());
        }
        return  result;
    }

    public cResult pGetNextDocumentAndDetailsRst(String pvDocumentStr) {

        cResult result = new cResult();
        result.resultBln = true;

        cWebresult WebResult = this.getPackAndShipOrderViewModel().pGetDocumentAndDetailsViaWebserviceWrs(pvDocumentStr);
        if (WebResult.getResultBln() && WebResult.getSuccessBln()) {

            for (JSONObject jsonObject : WebResult.getResultDtt()) {
                try {
                    JSONArray adressesObl    = jsonObject.getJSONArray(cDatabase.ADDRESSES_NAMESTR);

                    for (int i = 0, size = adressesObl.length(); i < size; i++)
                    {
                        JSONObject object =adressesObl.getJSONObject(i);
                        cPackAndShipAddress packAndShipAddress = new cPackAndShipAddress(object, true);

                        cResult hulpRst = this.mValidateShipmentRst(packAndShipAddress);
                        if (!hulpRst.resultBln) {
                            result.resultBln = false;
                            result.pAddErrorMessage(hulpRst.messagesStr());
                            return result;
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return result;
            }

        } else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETSOURCEDOCUMENTSHIPPINGDATA);
            result.resultBln = false;
            result.pAddErrorMessage(WebResult.getResultStr());
            return result;
        }

        return  result;
    }

    public cResult pAddShipmentViaWebserviceRst() {

        cResult result = new cResult();
        result.resultBln = true;

        cWebresult WebResult = this.getPackAndShipOrderViewModel().pAddShipmentViaWebserviceWrs();
        if (WebResult.getResultBln() && WebResult.getSuccessBln()) {

            for (JSONObject jsonObject : WebResult.getResultDtt()) {

                try {
                    JSONArray linesObl   = jsonObject.getJSONArray(cDatabase.LINE_NAMESTR);

                    for (int i = 0, size = linesObl.length(); i < size; i++)
                    {
                        JSONObject object =linesObl.getJSONObject(i);
                        cPackAndShipShipment.currentShipment.sourceNoStr = object.getString(cDatabase.SOURCENO_NAMESTR);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    JSONArray barcodeObl   = jsonObject.getJSONArray(cDatabase.BARCODE_NAMESTR);

                    for (int i = 0, size = barcodeObl.length(); i < size; i++)
                    {
                        JSONObject object =barcodeObl.getJSONObject(i);
                        cPackAndShipBarcode packAndShipBarcode = new cPackAndShipBarcode(object);
                        packAndShipBarcode.pInsertInDatabaseBln();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        } else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_WAREHOUSEOPDRACHTSETTINGSGET);
            result.pAddErrorMessage(WebResult.getResultObl().get(0));
            result.resultBln = false;
        }
        return  result;


    }

    public cResult pAddDocumentToCurrentShipmentViaWebserviceRst(String pvDocumentStr) {

        cResult result = new cResult();
        result.resultBln = true;

        cWebresult WebResult = this.getPackAndShipOrderViewModel().pAddDocumentToCurrentShipmentViaWebserviceWrs(pvDocumentStr);
        if (WebResult.getResultBln() && WebResult.getSuccessBln()) {

            for (JSONObject jsonObject : WebResult.getResultDtt()) {

                try {
                    JSONArray linesObl   = jsonObject.getJSONArray(cDatabase.LINE_NAMESTR);

                    for (int i = 0, size = linesObl.length(); i < size; i++)
                    {
                        JSONObject object =linesObl.getJSONObject(i);
                        cPackAndShipShipment.currentShipment.sourceNoStr = object.getString(cDatabase.SOURCENO_NAMESTR);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    JSONArray barcodeObl   = jsonObject.getJSONArray(cDatabase.BARCODE_NAMESTR);

                    for (int i = 0, size = barcodeObl.length(); i < size; i++)
                    {
                        JSONObject object =barcodeObl.getJSONObject(i);
                        cPackAndShipBarcode packAndShipBarcode = new cPackAndShipBarcode(object);
                        packAndShipBarcode.pInsertInDatabaseBln();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        } else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_WAREHOUSEOPDRACHTSETTINGSGET);
            result.resultBln = false;
        }
        return  result;


    }

    public Boolean pUpdateWorkplaceViaWebserviceBln() {

        return  this.getPackAndShipOrderViewModel().pUpdateWorkplaceViaWebserviceBln();

    }

    public boolean pDeleteDetailsBln() {
        cPackAndShipLine.pTruncateTableBln();
        cPackAndShipBarcode.pTruncateTableBln();
        cPackAndShipSetting.pTruncateTableBln();
        cComment.pTruncateTableBln();
        return true;
    }

    public boolean pGetSettingsViaWebserviceBln(Boolean pvRefreshBln) {


        if (pvRefreshBln) {
            cPackAndShipSetting.allSettingsObl = null;
            cPackAndShipSetting.pTruncateTableBln();
        }
        cWebresult WebResult = this.getPackAndShipOrderViewModel().pGetSettingsFromWebserviceWrs();
        if (WebResult.getResultBln() && WebResult.getSuccessBln()) {

            if (cPackAndShipLine.allLinesObl == null) {
                cPackAndShipLine.allLinesObl= new ArrayList<>();
            }

            for (JSONObject jsonObject : WebResult.getResultDtt()) {
                cPackAndShipSetting packAndShipSetting = new cPackAndShipSetting(jsonObject);
                packAndShipSetting.pInsertInDatabaseBln();
            }

            return  true;

        } else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_WAREHOUSEOPDRACHTSETTINGSGET);
            return false;
        }


    }

    public boolean pGetLinesViaWebserviceBln(Boolean pvRefreshBln) {



        if (pvRefreshBln) {
            cPackAndShipLine.allLinesObl = null;
            cPackAndShipLine.pTruncateTableBln();
        }
        cWebresult WebResult = this.getPackAndShipOrderViewModel().pGetLinesFromWebserviceWrs();
        if (WebResult.getResultBln() && WebResult.getSuccessBln()) {

            if (cPackAndShipLine.allLinesObl == null) {
                cPackAndShipLine.allLinesObl= new ArrayList<>();
            }

            for (JSONObject jsonObject : WebResult.getResultDtt()) {
                cPackAndShipLine packAndShipOrderLine = new cPackAndShipLine(jsonObject);
                packAndShipOrderLine.pInsertInDatabaseBln();
            }

            return  true;

        } else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETPACKANDSHIPORDERLINES);
            return false;
        }


    }

    public boolean pGetShipmentsViaWebserviceBln(Boolean pvRefreshBln) {
        if (pvRefreshBln) {
            cPackAndShipShipment.allShipmentsObl = null;
            cPackAndShipShipment.pTruncateTableBln();
        }
        cWebresult WebResult = this.getPackAndShipOrderViewModel().pGetShipmentsFromWebserviceWrs();
        if (WebResult.getResultBln() && WebResult.getSuccessBln()) {

            if (cPackAndShipShipment.allShipmentsObl == null) {
                cPackAndShipShipment.allShipmentsObl= new ArrayList<>();
            }

            for (JSONObject jsonObject : WebResult.getResultDtt()) {
                cPackAndShipShipment packAndShipShipment = new cPackAndShipShipment(jsonObject, false);
                packAndShipShipment.pInsertInDatabaseBln();
            }

            if (cPackAndShipShipment.allShipmentsObl.size()  > 0) {
                cPackAndShipShipment.currentShipment = cPackAndShipShipment.allShipmentsObl.get(0);
            }

            return  true;

        } else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETPACKANDSHIPSHIPMENTS);
            return false;
        }
    }

    public boolean pGetAddressesViaWebserviceBln(Boolean pvRefreshBln) {
        if (pvRefreshBln) {
            cPackAndShipAddress.allAddressesObl = null;
            cPackAndShipAddress.pTruncateTableBln();
        }
        cWebresult WebResult = this.getPackAndShipOrderViewModel().pGetAddressesFromWebserviceWrs();
        if (WebResult.getResultBln() && WebResult.getSuccessBln()) {

            if (cPackAndShipAddress.allAddressesObl== null) {
                cPackAndShipAddress.allAddressesObl =  new ArrayList<>();
            }

            for (JSONObject jsonObject : WebResult.getResultDtt()) {
                cPackAndShipAddress packAndShipAddress = new cPackAndShipAddress(jsonObject, false);
                packAndShipAddress.pInsertInDatabaseBln();
            }

            return  true;

        } else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETPACKANDSHIPADDRESSES);
            return false;
        }
    }

    public boolean pGetShippingMethodsViaWebserviceBln(Boolean pvRefreshBln) {
        if (pvRefreshBln) {
            cPackAndShipShippingMethod.allShippingMethodsObl = null;
            cPackAndShipShippingMethod.pTruncateTableBln();
        }
        cWebresult WebResult = this.getPackAndShipOrderViewModel().pGetShippingMethodsFromWebserviceWrs();
        if (WebResult.getResultBln() && WebResult.getSuccessBln()) {

            if (cPackAndShipShippingMethod.allShippingMethodsObl == null) {
                cPackAndShipShippingMethod.allShippingMethodsObl= new ArrayList<>();
            }

            for (JSONObject jsonObject : WebResult.getResultDtt()) {
                cPackAndShipShippingMethod packAndShipShipment = new cPackAndShipShippingMethod(jsonObject,"PS1");
                packAndShipShipment.pInsertInDatabaseBln();
            }

            return  true;

        } else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETPACKANDSHIPSHIPPINGMETHODS);
            return false;
        }
    }

    public boolean pGetCommentsViaWebserviceBln(Boolean pvRefeshBln) {

        if (pvRefeshBln) {
            cComment.allCommentsObl = null;
            cComment.pTruncateTableBln();
            cComment.commentsShownBln = false;
        }

         cWebresult webresult = this.getPackAndShipOrderViewModel().pGetCommentsFromWebserviceWrs();
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
            cPackAndShipBarcode.allPackAndShipOrderBarcodesObl = null;
            cPackAndShipBarcode.pTruncateTableBln();
        }

        cWebresult WebResult =  this.getPackAndShipOrderViewModel().pGetBarcodesFromWebserviceWrs();
        if (WebResult.getResultBln() && WebResult.getSuccessBln()){

            if ( cPackAndShipBarcode.allPackAndShipOrderBarcodesObl == null) {
                cPackAndShipBarcode.allPackAndShipOrderBarcodesObl = new ArrayList<>();
            }

            for (  JSONObject jsonObject : WebResult.getResultDtt()) {
                cPackAndShipBarcode packAndShipBarcode = new cPackAndShipBarcode(jsonObject);
                packAndShipBarcode.pInsertInDatabaseBln();

            }

            return  true;
        }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETPACKANDSHIPORDERS);
            return  false;
        }
    }

    public boolean pGetShippingPackagesViaWebserviceBln(Boolean pvRefreshBln) {

        if (pvRefreshBln) {
            cPackAndShipShippingPackage.allPackagesObl = null;
            cPackAndShipShippingPackage.pTruncateTableBln();
        }

        cWebresult WebResult =  this.getPackAndShipOrderViewModel().pGetShippingPackagesFromWebserviceWrs();
        if (WebResult.getResultBln() && WebResult.getSuccessBln()){

            if ( cPackAndShipShippingPackage.allPackagesObl == null) {
                cPackAndShipShippingPackage.allPackagesObl = new ArrayList<>();
            }

            for (  JSONObject jsonObject : WebResult.getResultDtt()) {
                cPackAndShipShippingPackage packAndShipShippingPackage = new cPackAndShipShippingPackage(jsonObject);
                packAndShipShippingPackage.pInsertInDatabaseBln();

            }

            return  true;
        }
        else {
            cWeberror.pReportErrorsToFirebaseBln(cWebserviceDefinitions.WEBMETHOD_GETPACKANDSHIPSHIPPINPACKAGES);
            return  false;
        }
    }

    public static List<cPackAndShipOrder> pGetPackAndShipOrdersWithFilterFromDatabasObl() {

        List<cPackAndShipOrder> resultObl = new ArrayList<>();
        List<cPackAndShipOrderEntity> hulpResultObl;

        cPackAndShipOrderViewModel  packAndShipOrderViewModel = new ViewModelProvider(cAppExtension.fragmentActivity).get(cPackAndShipOrderViewModel.class);

        hulpResultObl = packAndShipOrderViewModel.pGetPackAndShipOrdersWithFilterFromDatabaseObl(cUser.currentUser.getUsernameStr(), cSharedPreferences.userPackAndShipFilterBln());
        if (hulpResultObl == null || hulpResultObl.size() == 0) {
            return  resultObl;
        }

        for (cPackAndShipOrderEntity packAndShipOrderEntity : hulpResultObl ) {
            cPackAndShipOrder packAndShipOrder = new cPackAndShipOrder(packAndShipOrderEntity);
            resultObl.add(packAndShipOrder);
        }

        return resultObl;
    }

    public List<cComment> pFeedbackCommentObl(){

        if (cPackAndShipOrder.currentPackAndShipOrder.commentsObl() == null || cPackAndShipOrder.currentPackAndShipOrder.commentsObl().size() == 0) {
            return  null;
        }

        return  cComment.pGetCommentsForTypeObl(cWarehouseorder.CommentTypeEnu.FEEDBACK);

    }

    public cPackAndShipBarcode pGetOrderBarcode(cBarcodeScan pvBarcodescan) {

        if (this.barcodesObl() == null || this.barcodesObl().size() == 0)  {
            return  null;
        }

        for (cPackAndShipBarcode packAndShipBarcode : this.barcodesObl()) {

            if (packAndShipBarcode.getBarcodeStr().equalsIgnoreCase(pvBarcodescan.getBarcodeFormattedStr()) || packAndShipBarcode.getBarcodeStr().equalsIgnoreCase(pvBarcodescan.getBarcodeOriginalStr())) {
                return  packAndShipBarcode;
            }
        }

        return  null;

    }

    private cResult mValidateShipmentRst(cPackAndShipAddress pvPackAndShipAddress) {

        cResult result = new cResult();
        result.resultBln = true;

        if (cPackAndShipShipment.currentShipment.deliveryAddress() == null ) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.activity.getString(R.string.message_no_addresses_to_check));
            return  result;
        }

       if (!pvPackAndShipAddress.getAddressNameStr().equalsIgnoreCase(cPackAndShipShipment.currentShipment.deliveryAddress().getAddressNameStr())) {
           result.resultBln = false;
           result.pAddErrorMessage(cAppExtension.activity.getString(R.string.message_addressname_mismatch));
           return  result;
       }

        if (!pvPackAndShipAddress.getAddressStr().equalsIgnoreCase(cPackAndShipShipment.currentShipment.deliveryAddress().getAddressStr())) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.activity.getString(R.string.message_address_mismatch));
            return  result;
        }

        if (!pvPackAndShipAddress.getZipcodeStr().equalsIgnoreCase(cPackAndShipShipment.currentShipment.deliveryAddress().getZipcodeStr())) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.activity.getString(R.string.message_zipcode_mismatch));
            return  result;
        }

        if (!pvPackAndShipAddress.getCityStr().equalsIgnoreCase(cPackAndShipShipment.currentShipment.deliveryAddress().getCityStr())) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.activity.getString(R.string.message_city_mismatch));
            return  result;
        }

        if (!pvPackAndShipAddress.getCountryStr().equalsIgnoreCase(cPackAndShipShipment.currentShipment.deliveryAddress().getCountryStr())) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.activity.getString(R.string.message_country_mismatch));
            return  result;
        }


        return  result;

    }

    //Region Public Methods

    //Region Private Methods

    private static void mTruncateTable() {
        cPackAndShipOrderViewModel    packAndShipOrderViewModel =   new ViewModelProvider(cAppExtension.fragmentActivity).get(cPackAndShipOrderViewModel.class);
        packAndShipOrderViewModel.deleteAll();
    }

    private void mGetCommentsViaWebError(List<JSONObject> pvResultDtt) {

        cComment.allCommentsObl = new ArrayList<>();

        if (pvResultDtt != null && pvResultDtt.size() > 0 ) {
            JSONObject jsonObject = pvResultDtt.get(0);
            cComment comment = new cComment(jsonObject);
            comment.pInsertInDatabaseBln();
        }

    }

    //End Region Private Methods

}
