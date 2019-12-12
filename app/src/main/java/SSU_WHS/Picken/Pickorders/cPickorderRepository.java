package SSU_WHS.Picken.Pickorders;

import android.app.Application;

import androidx.sqlite.db.SimpleSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteQuery;
import android.os.AsyncTask;

import org.json.JSONException;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import ICS.Utils.cDeviceInfo;
import ICS.Utils.cSharedPreferences;
import SSU_WHS.Basics.ShippingAgentServiceShippingUnits.cShippingAgentServiceShippingUnit;
import SSU_WHS.Basics.Users.cUser;
import SSU_WHS.Basics.Workplaces.cWorkplace;
import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.Picken.PickorderLinePackAndShip.cPickorderLinePackAndShipEntity;
import SSU_WHS.Picken.PickorderLinePackAndShip.iPickorderLinePackAndShipDao;
import SSU_WHS.Picken.PickorderLines.cPickorderLineEntity;
import SSU_WHS.Picken.PickorderLines.iPickorderLineDao;
import SSU_WHS.Picken.Shipment.cShipment;
import SSU_WHS.Webservice.cWebserviceDefinitions;
import SSU_WHS.General.acScanSuiteDatabase;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebservice;

import static ICS.Utils.cText.pAddSingleQuotesStr;

public class cPickorderRepository {

    //Region Public Properties
    public iPickorderDao pickorderDao;
    public iPickorderLineDao pickorderLineDao;
    public iPickorderLinePackAndShipDao pickorderLinePackAndShipDao;
    //End Region Public Properties

    //Region Private Properties
    private acScanSuiteDatabase db;

    private class PickorderStepHandledParams {
        String userStr;
        String languageStr;
        String branchStr;
        String orderNumberStr;
        String deviceStr;
        String workPlaceStr;
        String workflowStepcodeStr;
        Integer workflowStepInt;
        String cultureStr;

        PickorderStepHandledParams(String pvUserStr, String pvLanguageStr, String pvBranchStr, String pvOrderNumberStr, String pvDeviceStr, String pvWorkplaceStr, String pvWorkflowStepCodeStr, Integer pvWorkflowStepStr, String pvCultureStr) {
            this.userStr = pvUserStr;
            this.languageStr = pvLanguageStr;
            this.branchStr = pvBranchStr;
            this.orderNumberStr = pvOrderNumberStr;
            this.deviceStr = pvDeviceStr;
            this.workPlaceStr = pvWorkplaceStr;
            this.workflowStepcodeStr = pvWorkflowStepCodeStr;
            this.workflowStepInt = pvWorkflowStepStr;
            this.cultureStr = pvCultureStr;
        }
    }

    private class PickorderUpdateWorkplaceParams {
        String user;
        String branch;
        String ordernumber;
        String workplace;

        PickorderUpdateWorkplaceParams(String pv_user, String pv_branch, String pv_ordernumber, String pv_workplace) {
            this.user = pv_user;
            this.branch = pv_branch;
            this.ordernumber = pv_ordernumber;
            this.workplace = pv_workplace;
        }
    }

    private class UpdatePickorderCurrentLocationLocalParams {
        String orderNumberStr;
        String currentLocationStr;

        UpdatePickorderCurrentLocationLocalParams(String pvOrderNumberStr, String pvCurrentLocationStr) {
            this.orderNumberStr = pvOrderNumberStr;
            this.currentLocationStr = pvCurrentLocationStr;
        }
    }

    private class UpdatePickorderWorkplaceLocalParams {
        String ordernumber;
        String workplace;

        UpdatePickorderWorkplaceLocalParams(String pv_ordernumber, String pv_workplace) {
            this.ordernumber = pv_ordernumber;
            this.workplace = pv_workplace;
        }
    }


    private class PickorderLocalParams {
        String userNameStr;
        String branchStr;
        Boolean inProgressBln;
        int pickStep;
        String searchTextStr;
        String mainTypeStr;


        PickorderLocalParams(String pvUserNameStr, String pvBranchStr, Boolean pvInProgressBln, int pvPickStepInt, String pvSearchTextStr, String pvMainTypeStr) {
            this.userNameStr = pvUserNameStr;
            this.branchStr = pvBranchStr;
            this.inProgressBln = pvInProgressBln;
            this.pickStep = pvPickStepInt;
            this.searchTextStr = pvSearchTextStr;
            this.mainTypeStr = pvMainTypeStr;
        }
    }

    private static class GetSortorderLineNotHandledByItemNoAndVariantParams {
        String itemNoStr;
        String variantCodeStr;

        GetSortorderLineNotHandledByItemNoAndVariantParams(String pvItemNoStr, String pvVariantCodeStr) {
            this.itemNoStr = pvItemNoStr;
            this.variantCodeStr = pvVariantCodeStr;
        }
    }

    //End Region Private Properties

    //Region Constructor
    cPickorderRepository(Application pvApplication) {
        this.db = acScanSuiteDatabase.getDatabase(pvApplication);
        this.pickorderDao = db.pickorderDao();
        this.pickorderLineDao = db.pickorderLineDao();
        this.pickorderLinePackAndShipDao = db.pickorderLinePackAndShipDao();
    }
    //End Region Constructor

    //Region Public Methods

    public void insert (cPickorderEntity pickorderEntity) {
        new mInsertAsyncTask(pickorderDao).execute(pickorderEntity);
    }

    public void deleteAll () {
        new mDeleteAllAsyncTask(pickorderDao).execute();
    }

    public void pAbortOrder() {
        new mAbortOrderAsyncTask(pickorderLineDao).execute();
    }

    public cWebresult pGetPickordersFromWebserviceWrs(Boolean pvInprogressBln, String pvSearchTextStr) {

        List<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();

        PickorderLocalParams pickorderLocalParams;
        pickorderLocalParams = new PickorderLocalParams(cUser.currentUser.getNameStr(),cUser.currentUser.currentBranch.getBranchStr(),pvInprogressBln,0, pvSearchTextStr,"");

        try {
            webResultWrs = new mGetPickordersFromWebserviceAsyncTask().execute(pickorderLocalParams).get();
        } catch (ExecutionException e) {
            webResultWrs.setResultBln(false);
            webResultWrs.setSuccessBln(false);
            resultObl.add(e.getLocalizedMessage());
            webResultWrs.setResultObl(resultObl);
            e.printStackTrace();
        } catch (InterruptedException e) {
            webResultWrs.setResultBln(false);
            webResultWrs.setSuccessBln(false);
            resultObl.add(e.getLocalizedMessage());
            webResultWrs.setResultObl(resultObl);
            e.printStackTrace();
        }
        return webResultWrs;
    }

    public cWebresult pGetPickordersToShipFromWebserviceWrs(String pvUsernameStr, cWarehouseorder.StepCodeEnu pvStepCodeEnu, String pvSearchTextStr) {
        List<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();

         int stepCodeInt= 0 ;

        if (pvStepCodeEnu == cWarehouseorder.StepCodeEnu.Pick_Picking) {
            stepCodeInt = 10;
        }
        if (pvStepCodeEnu == cWarehouseorder.StepCodeEnu.Pick_Sorting) {
            stepCodeInt = 20;
        }
        if (pvStepCodeEnu == cWarehouseorder.StepCodeEnu.Pick_PackAndShip) {
            stepCodeInt = 40;
        }

        PickorderLocalParams pickorderLocalParams;
        pickorderLocalParams = new PickorderLocalParams(pvUsernameStr,cUser.currentUser.currentBranch.getBranchStr(), false, stepCodeInt, pvSearchTextStr,"");

        try {
            webResultWrs = new mPickordersToSortFromWebserviceGetAsyncTask().execute(pickorderLocalParams).get();
        } catch (ExecutionException e) {
            webResultWrs.setResultBln(false);
            webResultWrs.setSuccessBln(false);
            resultObl.add(e.getLocalizedMessage());
            webResultWrs.setResultObl(resultObl);
            e.printStackTrace();
        } catch (InterruptedException e) {
            webResultWrs.setResultBln(false);
            webResultWrs.setSuccessBln(false);
            resultObl.add(e.getLocalizedMessage());
            webResultWrs.setResultObl(resultObl);
            e.printStackTrace();
        }
        return webResultWrs;
    }

    public List<cPickorderEntity> pGetPickordersFromDatabaseObl() {
        List<cPickorderEntity> ResultObl = null;
        try {
            ResultObl = new mGetPickordersFromDatabaseAsyncTask(pickorderDao).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return ResultObl;
    }

    public List<cPickorderEntity> pGetPickordersFromDatabaseWithFilterObl(String pvCurrentUserStr, Boolean pvUseFiltersBln) {

        List<cPickorderEntity> ResultObl = null;
        String SQLStatementStr;

        SQLStatementStr = "SELECT * FROM Pickorders ";
        if (pvUseFiltersBln) {
//            TTT
            if (cSharedPreferences.showAssignedToMeBln() && cSharedPreferences.showAssignedToOthersBln() && cSharedPreferences.showNotAssignedBln()) {
                SQLStatementStr += "WHERE 1=1 ";
            }
//            TTF
            else if (cSharedPreferences.showAssignedToMeBln() && cSharedPreferences.showAssignedToOthersBln() && !cSharedPreferences.showNotAssignedBln()) {
                SQLStatementStr += "WHERE AssignedUserId != '' ";
            }
//            TFT
            else if (cSharedPreferences.showAssignedToMeBln() && !cSharedPreferences.showAssignedToOthersBln() && cSharedPreferences.showNotAssignedBln()) {
                SQLStatementStr += "WHERE AssignedUserId = " + pAddSingleQuotesStr(pvCurrentUserStr.toUpperCase()) + " OR  AssignedUserId = '' " ;
                SQLStatementStr += "OR CurrentUserId = " + pAddSingleQuotesStr(pvCurrentUserStr.toUpperCase()) + " OR  CurrentUserId = '' " ;
            }
//            FTT
            else if (!cSharedPreferences.showAssignedToMeBln() && cSharedPreferences.showAssignedToOthersBln() && cSharedPreferences.showNotAssignedBln()) {
                SQLStatementStr += "WHERE AssignedUserId != " + pAddSingleQuotesStr(pvCurrentUserStr.toUpperCase()) + " ";
            }
//            TFF
            else if (cSharedPreferences.showAssignedToMeBln() && !cSharedPreferences.showAssignedToOthersBln() && !cSharedPreferences.showNotAssignedBln()) {
                SQLStatementStr += "WHERE AssignedUserId = " + pAddSingleQuotesStr(pvCurrentUserStr.toUpperCase()) + " ";
                SQLStatementStr += "OR CurrentUserId = " + pAddSingleQuotesStr(pvCurrentUserStr.toUpperCase()) + " " ;
            }
//            FTF
            else if (!cSharedPreferences.showAssignedToMeBln() && cSharedPreferences.showAssignedToOthersBln() && !cSharedPreferences.showNotAssignedBln()) {
                SQLStatementStr += "WHERE AssignedUserId != " + pAddSingleQuotesStr(pvCurrentUserStr.toUpperCase()) + " AND  AssignedUserId != '' ";
            }
//            FFT
            else if (!cSharedPreferences.showAssignedToMeBln() && !cSharedPreferences.showAssignedToOthersBln() && cSharedPreferences.showNotAssignedBln()) {
                SQLStatementStr += "WHERE AssignedUserId = '' AND CurrentUserId = ''";
            }
//            FFF
            else if (!cSharedPreferences.showAssignedToMeBln() && !cSharedPreferences.showAssignedToOthersBln() && !cSharedPreferences.showNotAssignedBln()) {
                SQLStatementStr += "WHERE AssignedUserId = 'HELEMAALNIEMAND' ";
            }
            if (cSharedPreferences.showSingleArticlesBln()) {
                SQLStatementStr += " AND SingleArticleOrders = 1 ";
            }

            if (cSharedPreferences.showProcessedWaitBln()) {
                SQLStatementStr += " AND (IsProcessingOrParked) ";
            }
        }

        try {
            SupportSQLiteQuery query = new SimpleSQLiteQuery(SQLStatementStr);
            ResultObl = new mGetPickordersFromDatabaseWithFilterAsyncTask(pickorderDao).execute(query).get();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return ResultObl;
    }

    public cWebresult pPickHandledViaWebserviceBln(String pvWorkplaceStr) {

        cWebresult webResult;

        PickorderStepHandledParams pickorderStepHandledParams;
        pickorderStepHandledParams = new PickorderStepHandledParams(cUser.currentUser.getNameStr(), "", cUser.currentUser.currentBranch.getBranchStr(), cPickorder.currentPickOrder.orderNumberStr, cDeviceInfo.getSerialnumberStr() ,pvWorkplaceStr,cWarehouseorder.StepCodeEnu.Pick_Picking.toString(), cWarehouseorder.WorkflowPickStepEnu.PickPicking, "");

        try {

            webResult = new mPickorderStepHandledAsyncTask().execute(pickorderStepHandledParams).get();
            return  webResult;
        }

        catch (InterruptedException e) {
            e.printStackTrace();
            return  null;

        } catch (ExecutionException e) {
            e.printStackTrace();
            return  null;

        }
    }

    public cWebresult pSortHandledViaWebserviceBln(String pvWorkplaceStr) {

        cWebresult webResult;

        PickorderStepHandledParams pickorderStepHandledParams;
        pickorderStepHandledParams = new PickorderStepHandledParams(cUser.currentUser.getNameStr(), "", cUser.currentUser.currentBranch.getBranchStr(), cPickorder.currentPickOrder.orderNumberStr, cDeviceInfo.getSerialnumberStr() ,pvWorkplaceStr,cWarehouseorder.StepCodeEnu.Pick_Sorting.toString(), cWarehouseorder.WorkflowPickStepEnu.PickSorting, "");

        try {

            webResult = new mPickorderStepHandledAsyncTask().execute(pickorderStepHandledParams).get();
            return  webResult;
        }

        catch (InterruptedException e) {
            e.printStackTrace();
            return  null;

        } catch (ExecutionException e) {
            e.printStackTrace();
            return  null;

        }
    }

    public cWebresult pShipHandledViaWebserviceWrs(String pvWorkplaceStr) {

        cWebresult webResult;

        PickorderStepHandledParams pickorderStepHandledParams;
        pickorderStepHandledParams = new PickorderStepHandledParams(cUser.currentUser.getNameStr(),
                                                                    "",
                                                                    cUser.currentUser.currentBranch.getBranchStr(),
                                                                    cPickorder.currentPickOrder.orderNumberStr,
                                                                    cDeviceInfo.getSerialnumberStr() ,
                                                                    pvWorkplaceStr,
                                                                    cWarehouseorder.StepCodeEnu.Pick_PackAndShip.toString(),
                                                                    cWarehouseorder.WorkflowPickStepEnu.PickPackAndShip,
                                                                    "");

        try {

            webResult = new mPickorderStepHandledAsyncTask().execute(pickorderStepHandledParams).get();
            return  webResult;
        }

        catch (InterruptedException e) {
            e.printStackTrace();
            return  null;

        } catch (ExecutionException e) {
            e.printStackTrace();
            return  null;

        }
    }

    public cWebresult pPickorderSourceDocumentShippedViaWebserviceWrs() {

        cWebresult webResult;

        try {
            webResult = new mPickorderSourceDocumentShippedAsyncTask().execute().get();
            if (webResult.getSuccessBln() == false || webResult.getResultBln() == false) {
                return  webResult;
            }

        }

        catch (InterruptedException e) {
            e.printStackTrace();
            return  null;

        } catch (ExecutionException e) {
            e.printStackTrace();
            return  null;
        }
        return  webResult;
    }

    public Boolean pPickorderUpdateWorkplaceViaWebserviceBln(String pvWorkplaceStr) {

        cWebresult webResult;

        PickorderUpdateWorkplaceParams pickorderUpdateWorkplaceParams;
        pickorderUpdateWorkplaceParams   = new PickorderUpdateWorkplaceParams(cUser.currentUser.getNameStr(), cUser.currentUser.currentBranch.getBranchStr(), cPickorder.currentPickOrder.getOrderNumberStr(), pvWorkplaceStr);

        try {
            webResult = new mPickorderUpdateWorkplaceViaWebserviceAsyncTask().execute(pickorderUpdateWorkplaceParams).get();
            if (webResult.getSuccessBln() == false || webResult.getResultBln() == false) {
                return  false;
            }
            return  true;
        }

        catch (InterruptedException e) {
            e.printStackTrace();
            return  false;

        } catch (ExecutionException e) {
            e.printStackTrace();
            return  false;

        }
    }

    public Boolean pPickorderUpdateWorkplaceInDatabaseBln() {

        Integer integerValue;
        UpdatePickorderWorkplaceLocalParams updatePickorderWorkplaceLocalParams = new UpdatePickorderWorkplaceLocalParams(cPickorder.currentPickOrder.getOrderNumberStr(), cWorkplace.currentWorkplace.getWorkplaceStr());
        try {
            integerValue = new mUpdatePickorderWorkplaceInDatabaseAsyncTask(pickorderDao).execute(updatePickorderWorkplaceLocalParams).get();
            if (integerValue == 0 ) {
                return  false;
            }
            return  true;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return  false;
        } catch (ExecutionException e) {
            e.printStackTrace();
            return  false;
        }
    }

    public cWebresult pUpdateCurrentLocationViaWebserviceWrs(String pvCurrentLocationStr) {

        List<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();

        try {
            webResultWrs = new mUpdateCurrentLocationViaWebserviceAsyncTask().execute(pvCurrentLocationStr).get();
        } catch (ExecutionException e) {
            webResultWrs.setResultBln(false);
            webResultWrs.setSuccessBln(false);
            resultObl.add(e.getLocalizedMessage());
            webResultWrs.setResultObl(resultObl);
            e.printStackTrace();
        } catch (InterruptedException e) {
            webResultWrs.setResultBln(false);
            webResultWrs.setSuccessBln(false);
            resultObl.add(e.getLocalizedMessage());
            webResultWrs.setResultObl(resultObl);
            e.printStackTrace();
        }
        return webResultWrs;
    }

    public Boolean pPickorderUpdatCurrentLocationInDatabaseBln(String pvCurrentLocationStr) {

        Integer integerValue;
        UpdatePickorderCurrentLocationLocalParams updatePickorderCurrentLocationLocalParams = new UpdatePickorderCurrentLocationLocalParams(cPickorder.currentPickOrder.getOrderNumberStr(), pvCurrentLocationStr);
        try {
            integerValue = new mUpdatePickorderCurrentLocationInDatabaseAsyncTask(pickorderDao).execute(updatePickorderCurrentLocationLocalParams).get();
            if (integerValue == 0 ) {
                return  false;
            }
            return  true;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return  false;
        } catch (ExecutionException e) {
            e.printStackTrace();
            return  false;
        }
    }

    //Pick Lines
    public cWebresult pGetLinesFromWebserviceWrs(cWarehouseorder.ActionTypeEnu pvActionTypeEnu) {

        List<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();

        try {
            webResultWrs = new mGetLinesViaWebserviceAsyncTask().execute(pvActionTypeEnu.toString()).get();
        } catch (ExecutionException e) {
            webResultWrs.setResultBln(false);
            webResultWrs.setSuccessBln(false);
            resultObl.add(e.getLocalizedMessage());
            webResultWrs.setResultObl(resultObl);
            e.printStackTrace();
        } catch (InterruptedException e) {
            webResultWrs.setResultBln(false);
            webResultWrs.setSuccessBln(false);
            resultObl.add(e.getLocalizedMessage());
            webResultWrs.setResultObl(resultObl);
            e.printStackTrace();
        }
        return webResultWrs;
    }

    public List<cPickorderLineEntity> pGetAllLinesFromDatabaseObl() {

        List<cPickorderLineEntity> resultObl = null;
        try {
            resultObl = new mGetAllLinesFromDatabaseAsyncTask(pickorderLineDao).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return resultObl;
    }

    public List<cPickorderLineEntity> pGetPickorderLinesToSendFromDatabaseObl() {
        List<cPickorderLineEntity> resultObl = null;
        try {
            resultObl = new mGetPickorderLinesToSendFromDatabaseAsyncTask(pickorderLineDao).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return resultObl;
    }

    public List<cPickorderLineEntity> pGetLinesNotHandledFromDatabaseObl() {
        List<cPickorderLineEntity> resultObl = null;
        try {
            resultObl = new mGetNotHandledLinesAsyncTask(pickorderLineDao).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return resultObl;
    }

    public List<cPickorderLineEntity> pGetLinesBusyFromDatabaseObl() {
        List<cPickorderLineEntity> resultObl = null;
        try {
            resultObl = new mGetBusyLinesAsyncTask(pickorderLineDao).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return resultObl;
    }

    public List<cPickorderLineEntity> pGetLinesHandledFromDatabaseObl() {
        List<cPickorderLineEntity> resultObl = null;
        try {
            resultObl = new mGetHandledLinesOblAsyncTask(pickorderLineDao).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return resultObl;
    }

    //Pick quantityDbl's
    public Double pQuantityNotHandledDbl() {
        Double resultDbl = 0.0;
        try {
            resultDbl = new mGetQuantityNotHandledAsyncTask(pickorderLineDao).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return resultDbl;
    }

    public Double pQuantityHandledDbl() {
        Double resultDbl = 0.0;
        try {
            resultDbl = new mGetQuantityHandledAsyncTask(pickorderLineDao).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return resultDbl;
    }

    public Double pGetTotalQuantityDbl() {
        Double resultDbl = 0.0;
        try {
            resultDbl = new mGetTotalQuanitityAsyncTask(pickorderLineDao).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return resultDbl;
    }


    //Pick Order details
    public cWebresult pGetAddressesFromWebserviceWrs() {

        List<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();

        try {
            webResultWrs = new mGetPickorderAdressesFromWebserviceTask().execute().get();
        } catch (ExecutionException e) {
            webResultWrs.setResultBln(false);
            webResultWrs.setSuccessBln(false);
            resultObl.add(e.getLocalizedMessage());
            webResultWrs.setResultObl(resultObl);
            e.printStackTrace();
        } catch (InterruptedException e) {
            webResultWrs.setResultBln(false);
            webResultWrs.setSuccessBln(false);
            resultObl.add(e.getLocalizedMessage());
            webResultWrs.setResultObl(resultObl);
            e.printStackTrace();
        }
        return webResultWrs;
    }



    public cWebresult pGetBarcodesFromWebservice(){
        ArrayList<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();

        try {
            webResultWrs = new mPickorderBarcodesGetFromWebserviceAsyncTask().execute().get();
        } catch (ExecutionException e) {
            webResultWrs.setResultBln(false);
            webResultWrs.setSuccessBln(false);
            resultObl.add(e.getLocalizedMessage());
            webResultWrs.setResultObl(resultObl);
            e.printStackTrace();
        } catch (InterruptedException e) {
            webResultWrs.setResultBln(false);
            webResultWrs.setSuccessBln(false);
            resultObl.add(e.getLocalizedMessage());
            webResultWrs.setResultObl(resultObl);
            e.printStackTrace();
        }
        return webResultWrs;
    }

    public cWebresult pGetLineBarcodesFromWebservice(cWarehouseorder.ActionTypeEnu pvActionTypeEnu){
        ArrayList<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();

        try {
            webResultWrs = new mPickorderLineBarcodesGetFromWebserviceAsyncTask().execute(pvActionTypeEnu.toString()).get();
        } catch (ExecutionException e) {
            webResultWrs.setResultBln(false);
            webResultWrs.setSuccessBln(false);
            resultObl.add(e.getLocalizedMessage());
            webResultWrs.setResultObl(resultObl);
            e.printStackTrace();
        } catch (InterruptedException e) {
            webResultWrs.setResultBln(false);
            webResultWrs.setSuccessBln(false);
            resultObl.add(e.getLocalizedMessage());
            webResultWrs.setResultObl(resultObl);
            e.printStackTrace();
        }
        return webResultWrs;
    }

    public cWebresult pGetCommentsFromWebservice(){
        ArrayList<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();

        try {
            webResultWrs = new mGetCommentsFromWebserviceAsyncTask().execute().get();
        } catch (ExecutionException e) {
            webResultWrs.setResultBln(false);
            webResultWrs.setSuccessBln(false);
            resultObl.add(e.getLocalizedMessage());
            webResultWrs.setResultObl(resultObl);
            e.printStackTrace();
        } catch (InterruptedException e) {
            webResultWrs.setResultBln(false);
            webResultWrs.setSuccessBln(false);
            resultObl.add(e.getLocalizedMessage());
            webResultWrs.setResultObl(resultObl);
            e.printStackTrace();
        }
        return webResultWrs;
    }

    public cWebresult pGetPackagesFromWebserviceWrs() {

        List<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();

        try {
            webResultWrs = new mGetPickorderPackagesFromWebserviceTask().execute().get();
        } catch (ExecutionException e) {
            webResultWrs.setResultBln(false);
            webResultWrs.setSuccessBln(false);
            resultObl.add(e.getLocalizedMessage());
            webResultWrs.setResultObl(resultObl);
            e.printStackTrace();
        } catch (InterruptedException e) {
            webResultWrs.setResultBln(false);
            webResultWrs.setSuccessBln(false);
            resultObl.add(e.getLocalizedMessage());
            webResultWrs.setResultObl(resultObl);
            e.printStackTrace();
        }
        return webResultWrs;
    }

    //Pack and Ship

    public cWebresult pGetPackAndShipLinesFromWebserviceWrs() {

        List<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();

        try {
            webResultWrs = new mGetPackAndShipLinesViaWebserviceAsyncTask().execute().get();
        } catch (ExecutionException e) {
            webResultWrs.setResultBln(false);
            webResultWrs.setSuccessBln(false);
            resultObl.add(e.getLocalizedMessage());
            webResultWrs.setResultObl(resultObl);
            e.printStackTrace();
        } catch (InterruptedException e) {
            webResultWrs.setResultBln(false);
            webResultWrs.setSuccessBln(false);
            resultObl.add(e.getLocalizedMessage());
            webResultWrs.setResultObl(resultObl);
            e.printStackTrace();
        }
        return webResultWrs;
    }

    public List<cPickorderLinePackAndShipEntity> pGetPackAndShipLinesNotHandledFromDatabaseObl() {
        List<cPickorderLinePackAndShipEntity> resultObl = null;
        try {
            resultObl = new mGetNotHandledPackAndShipLinesAsyncTask(pickorderLinePackAndShipDao).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return resultObl;
    }

    //End Region Public Methods

    //Region Private Methods

    private static class mDeleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private iPickorderDao mAsyncTaskDao;

        mDeleteAllAsyncTask(iPickorderDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final Void... params) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }

    private static class mInsertAsyncTask extends AsyncTask<cPickorderEntity, Void, Void> {
        private iPickorderDao mAsyncTaskDao;

        mInsertAsyncTask(iPickorderDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final cPickorderEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class mGetPickordersFromWebserviceAsyncTask extends AsyncTask<PickorderLocalParams, Void, cWebresult> {
        @Override
        protected cWebresult doInBackground(final PickorderLocalParams... params) {
            cWebresult WebresultWrs = new cWebresult();

            List<PropertyInfo> l_PropertyInfoObl = new ArrayList<>();

            PropertyInfo l_PropertyInfo1Pin = new PropertyInfo();
            l_PropertyInfo1Pin.name = cWebserviceDefinitions.WEBPROPERTY_USERNAMEDUNGLISH;
            l_PropertyInfo1Pin.setValue(params[0].userNameStr);
            l_PropertyInfoObl.add(l_PropertyInfo1Pin);

            PropertyInfo l_PropertyInfo2Pin = new PropertyInfo();
            l_PropertyInfo2Pin.name = cWebserviceDefinitions.WEBPROPERTY_LOCATION_NL;
            l_PropertyInfo2Pin.setValue(params[0].branchStr);
            l_PropertyInfoObl.add(l_PropertyInfo2Pin);

            PropertyInfo l_PropertyInfo3Pin = new PropertyInfo();
            l_PropertyInfo3Pin.name = cWebserviceDefinitions.WEBPROPERTY_INPROGRESS;
            l_PropertyInfo3Pin.setValue(params[0].inProgressBln);
            l_PropertyInfoObl.add(l_PropertyInfo3Pin);

            PropertyInfo l_PropertyInfo4Pin = new PropertyInfo();
            l_PropertyInfo4Pin.name = cWebserviceDefinitions.WEBPROPERTY_SEARCHTEXT;
            l_PropertyInfo4Pin.setValue(params[0].searchTextStr);
            l_PropertyInfoObl.add(l_PropertyInfo4Pin);

            PropertyInfo l_PropertyInfo5Pin = new PropertyInfo();
            l_PropertyInfo5Pin.name = cWebserviceDefinitions.WEBPROPERTY_MAINTYPE;
            l_PropertyInfo5Pin.setValue(params[0].mainTypeStr);
            l_PropertyInfoObl.add(l_PropertyInfo5Pin);

            try {
                WebresultWrs = new cWebresult().pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_GETPICKORDERS, l_PropertyInfoObl);
            } catch (JSONException e) {
                WebresultWrs.setResultBln(false);
                WebresultWrs.setSuccessBln(false);
                e.printStackTrace();
            }

            return WebresultWrs;
        }
    }

    private static class mPickordersToSortFromWebserviceGetAsyncTask extends AsyncTask<PickorderLocalParams, Void, cWebresult> {
        @Override
        protected cWebresult doInBackground(final PickorderLocalParams... params) {
            cWebresult WebresultWrs = new cWebresult();

            List<PropertyInfo> l_PropertyInfoObl = new ArrayList<>();

            PropertyInfo l_PropertyInfo1Pin = new PropertyInfo();
            l_PropertyInfo1Pin.name = cWebserviceDefinitions.WEBPROPERTY_USERNAMEDUNGLISH;
            l_PropertyInfo1Pin.setValue(params[0].userNameStr);
            l_PropertyInfoObl.add(l_PropertyInfo1Pin);

            PropertyInfo l_PropertyInfo2Pin = new PropertyInfo();
            l_PropertyInfo2Pin.name = cWebserviceDefinitions.WEBPROPERTY_LOCATION_NL;
            l_PropertyInfo2Pin.setValue(params[0].branchStr);
            l_PropertyInfoObl.add(l_PropertyInfo2Pin);

            PropertyInfo l_PropertyInfo3Pin = new PropertyInfo();
            l_PropertyInfo3Pin.name = cWebserviceDefinitions.WEBPROPERTY_PICKSTEP;
            l_PropertyInfo3Pin.setValue(params[0].pickStep);
            l_PropertyInfoObl.add(l_PropertyInfo3Pin);

            PropertyInfo l_PropertyInfo4Pin = new PropertyInfo();
            l_PropertyInfo4Pin.name = cWebserviceDefinitions.WEBPROPERTY_SEARCHTEXT;
            l_PropertyInfo4Pin.setValue(params[0].searchTextStr);
            l_PropertyInfoObl.add(l_PropertyInfo4Pin);

            PropertyInfo l_PropertyInfo5Pin = new PropertyInfo();
            l_PropertyInfo5Pin.name = cWebserviceDefinitions.WEBPROPERTY_MAINTYPE;
            l_PropertyInfo5Pin.setValue(params[0].mainTypeStr);
            l_PropertyInfoObl.add(l_PropertyInfo5Pin);

            try {
                WebresultWrs = new cWebresult().pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_GETPICKORDERSSEQUELSTEP, l_PropertyInfoObl);
            } catch (JSONException e) {
                WebresultWrs.setResultBln(false);
                WebresultWrs.setSuccessBln(false);
                e.printStackTrace();
            }

            return WebresultWrs;
        }
    }

    private static class mGetPickordersFromDatabaseAsyncTask extends AsyncTask<Void, Void, List<cPickorderEntity>> {
        private iPickorderDao mAsyncTaskDao;

        mGetPickordersFromDatabaseAsyncTask(iPickorderDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected List<cPickorderEntity> doInBackground(final Void... params) {
            return mAsyncTaskDao.getPickordersFromDatabase();
        }
    }

    private static class mGetPickordersFromDatabaseWithFilterAsyncTask extends AsyncTask<SupportSQLiteQuery, Void, List<cPickorderEntity>> {
        private iPickorderDao mAsyncTaskDao;

        mGetPickordersFromDatabaseWithFilterAsyncTask(iPickorderDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected List<cPickorderEntity> doInBackground(final SupportSQLiteQuery... params) {
            return mAsyncTaskDao.getFilteredPickorders(params[0]);
        }
    }

    private static class mPickorderStepHandledAsyncTask extends AsyncTask<PickorderStepHandledParams, Void, cWebresult> {
        @Override
        protected cWebresult doInBackground(PickorderStepHandledParams... params) {
            cWebresult webresult = new cWebresult();
            try {
                List<PropertyInfo> l_PropertyInfoObl = new ArrayList<>();

                PropertyInfo l_PropertyInfo1Pin = new PropertyInfo();
                l_PropertyInfo1Pin.name = cWebserviceDefinitions.WEBPROPERTY_USERNAMEDUTCH;
                l_PropertyInfo1Pin.setValue(params[0].userStr);
                l_PropertyInfoObl.add(l_PropertyInfo1Pin);

                PropertyInfo l_PropertyInfo2Pin = new PropertyInfo();
                l_PropertyInfo2Pin.name = cWebserviceDefinitions.WEBPROPERTY_LANGUAGEASCULTURE;
                l_PropertyInfo2Pin.setValue(params[0].languageStr);
                l_PropertyInfoObl.add(l_PropertyInfo2Pin);

                PropertyInfo l_PropertyInfo3Pin = new PropertyInfo();
                l_PropertyInfo3Pin.name = cWebserviceDefinitions.WEBPROPERTY_LOCATION_NL;
                l_PropertyInfo3Pin.setValue(params[0].branchStr);
                l_PropertyInfoObl.add(l_PropertyInfo3Pin);

                PropertyInfo l_PropertyInfo4Pin = new PropertyInfo();
                l_PropertyInfo4Pin.name = cWebserviceDefinitions.WEBPROPERTY_ORDERNUMBER;
                l_PropertyInfo4Pin.setValue(params[0].orderNumberStr);
                l_PropertyInfoObl.add(l_PropertyInfo4Pin);

                PropertyInfo l_PropertyInfo5Pin = new PropertyInfo();
                l_PropertyInfo5Pin.name = cWebserviceDefinitions.WEBPROPERTY_SCANNER;
                l_PropertyInfo5Pin.setValue(params[0].deviceStr);
                l_PropertyInfoObl.add(l_PropertyInfo5Pin);

                PropertyInfo l_PropertyInfo6Pin = new PropertyInfo();
                l_PropertyInfo6Pin.name = cWebserviceDefinitions.WEBPROPERTY_WORKPLACE;
                l_PropertyInfo6Pin.setValue(params[0].workPlaceStr);
                l_PropertyInfoObl.add(l_PropertyInfo6Pin);

                PropertyInfo l_PropertyInfo7Pin = new PropertyInfo();
                l_PropertyInfo7Pin.name = cWebserviceDefinitions.WEBPROPERTY_WORKFLOWSTEPCODESTR;
                l_PropertyInfo7Pin.setValue(params[0].workflowStepcodeStr);
                l_PropertyInfoObl.add(l_PropertyInfo7Pin);

                PropertyInfo l_PropertyInfo8Pin = new PropertyInfo();
                l_PropertyInfo8Pin.name = cWebserviceDefinitions.WEBPROPERTY_WORKFLOWSTEPINT;
                l_PropertyInfo8Pin.setValue(params[0].workflowStepInt);
                l_PropertyInfoObl.add(l_PropertyInfo8Pin);

                PropertyInfo l_PropertyInfo9Pin = new PropertyInfo();
                l_PropertyInfo9Pin.name = cWebserviceDefinitions.WEBPROPERTY_CULTURE;
                l_PropertyInfo9Pin.setValue(params[0].cultureStr);
                l_PropertyInfoObl.add(l_PropertyInfo9Pin);

                webresult = new cWebresult().pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_PICKORDERSTEPHANDLED, l_PropertyInfoObl);
            } catch (JSONException e) {
                webresult.setSuccessBln(false);
                webresult.setResultBln(false);
            }
            return webresult;
        }
    }

    private static class mPickorderSourceDocumentShippedAsyncTask extends AsyncTask<Void, Void, cWebresult> {
        @Override
        protected cWebresult doInBackground(Void... params) {
            cWebresult l_WebresultWrs = new cWebresult();
            try {
                List<PropertyInfo> l_PropertyInfoObl = new ArrayList<>();

                PropertyInfo l_PropertyInfo1Pin = new PropertyInfo();
                l_PropertyInfo1Pin.name = cWebserviceDefinitions.WEBPROPERTY_USERNAMEDUTCH;
                l_PropertyInfo1Pin.setValue(cUser.currentUser.getNameStr());
                l_PropertyInfoObl.add(l_PropertyInfo1Pin);

                PropertyInfo l_PropertyInfo2Pin = new PropertyInfo();
                l_PropertyInfo2Pin.name = cWebserviceDefinitions.WEBPROPERTY_LOCATION_NL;
                l_PropertyInfo2Pin.setValue(cUser.currentUser.currentBranch.getBranchStr());
                l_PropertyInfoObl.add(l_PropertyInfo2Pin);

                PropertyInfo l_PropertyInfo3Pin = new PropertyInfo();
                l_PropertyInfo3Pin.name = cWebserviceDefinitions.WEBPROPERTY_ORDERNUMBER;
                l_PropertyInfo3Pin.setValue(cPickorder.currentPickOrder.getOrderNumberStr());
                l_PropertyInfoObl.add(l_PropertyInfo3Pin);

                PropertyInfo l_PropertyInfo4Pin = new PropertyInfo();
                l_PropertyInfo4Pin.name = cWebserviceDefinitions.WEBPROPERTY_SOURCENO;
                l_PropertyInfo4Pin.setValue(cShipment.currentShipment.getSourceNoStr());
                l_PropertyInfoObl.add(l_PropertyInfo4Pin);

                PropertyInfo l_PropertyInfo5Pin = new PropertyInfo();
                l_PropertyInfo5Pin.name = cWebserviceDefinitions.WEBPROPERTY_CULTURE;
                l_PropertyInfo5Pin.setValue("");
                l_PropertyInfoObl.add(l_PropertyInfo5Pin);

                PropertyInfo l_PropertyInfo6Pin = new PropertyInfo();
                l_PropertyInfo6Pin.name = cWebserviceDefinitions.WEBPROPERTY_SHIPPINGAGENT;
                l_PropertyInfo6Pin.setValue(cShipment.currentShipment.shippingAgent().getShippintAgentStr());
                l_PropertyInfoObl.add(l_PropertyInfo6Pin);

                PropertyInfo l_PropertyInfo7Pin = new PropertyInfo();
                l_PropertyInfo7Pin.name = cWebserviceDefinitions.WEBPROPERTY_SHIPPINGSERVICE;
                l_PropertyInfo7Pin.setValue(cShipment.currentShipment.shippingAgentService().getServiceStr());
                l_PropertyInfoObl.add(l_PropertyInfo7Pin);

                PropertyInfo l_PropertyInfo8Pin = new PropertyInfo();
                l_PropertyInfo8Pin.name = cWebserviceDefinitions.WEBPROPERTY_SHIPPINGOPTIONS;
                l_PropertyInfo8Pin.setValue("");
                l_PropertyInfoObl.add(l_PropertyInfo8Pin);

                SoapObject shippingpackages = new SoapObject(cWebservice.WEBSERVICE_NAMESPACE, cWebserviceDefinitions.WEBPROPERTY_SHIPPINGPACKAGES);

                Integer sequencenumberInt = 0;
                String packageTypeToRememberStr = "";
                Integer counterForTypeInt = 0;

                for (cShippingAgentServiceShippingUnit shippingAgentServiceShippingUnit : cShipment.currentShipment.shippingAgentService().shippingUnitsObl()) {

                    //If we didn't use this, continue
                    if (shippingAgentServiceShippingUnit.getShippingUnitQuantityUsedInt() <= 0) {
                        continue;
                    }

                    //New packageype, so reset sequenceNumber
                    if (!packageTypeToRememberStr.equalsIgnoreCase(shippingAgentServiceShippingUnit.getShippingUnitStr())) {
                        packageTypeToRememberStr = shippingAgentServiceShippingUnit.getShippingUnitStr();
                        sequencenumberInt = 0;
                        counterForTypeInt = 0;
                    }

                    while (counterForTypeInt < shippingAgentServiceShippingUnit.getShippingUnitQuantityUsedInt()) {
                        counterForTypeInt += 1;
                        sequencenumberInt += 10;

                        SoapObject soapObject = new SoapObject(cWebservice.WEBSERVICE_NAMESPACE, cWebserviceDefinitions.WEBPROPERTY_INTERFACESHIPPINGPACKAGE);
                        soapObject.addProperty(cWebserviceDefinitions.WEBPROPERTY_INTERFACESHIPPINGPACKAGE_PACKAGE, packageTypeToRememberStr);
                        soapObject.addProperty(cWebserviceDefinitions.WEBPROPERTY_INTERFACESHIPPINGPACKAGE_SEQUENCENUMBER, sequencenumberInt);
                        soapObject.addProperty(cWebserviceDefinitions.WEBPROPERTY_INTERFACESHIPPINGPACKAGE_WEIGHTING, 0);
                        soapObject.addProperty(cWebserviceDefinitions.WEBPROPERTY_INTERFACESHIPPINGPACKAGE_ITEMCOUNT, 0);
                        soapObject.addProperty(cWebserviceDefinitions.WEBPROPERTY_INTERFACESHIPPINGPACKAGE_CONTAINERTYPE, "");
                        soapObject.addProperty(cWebserviceDefinitions.WEBPROPERTY_INTERFACESHIPPINGPACKAGE_CONTAINER, "");
                        shippingpackages.addSoapObject(soapObject);
                    }
                }

                PropertyInfo l_PropertyInfo9Pin = new PropertyInfo();
                l_PropertyInfo9Pin.name = cWebserviceDefinitions.WEBPROPERTY_SHIPPINGPACKAGES;
                l_PropertyInfo9Pin.setValue(shippingpackages);
                l_PropertyInfoObl.add(l_PropertyInfo9Pin);

                l_WebresultWrs = new cWebresult().pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_PICKORDERSOURCEDOCUMENTSHIPPED, l_PropertyInfoObl);
            } catch (JSONException e) {
                l_WebresultWrs.setSuccessBln(false);
                l_WebresultWrs.setResultBln(false);
            }
            return l_WebresultWrs;
        }
    }

    private static class mPickorderUpdateWorkplaceViaWebserviceAsyncTask extends AsyncTask<PickorderUpdateWorkplaceParams, Void, cWebresult> {
        @Override
        protected cWebresult doInBackground(PickorderUpdateWorkplaceParams... params) {
            cWebresult webresult = new cWebresult();
            try {
                List<PropertyInfo> l_PropertyInfoObl = new ArrayList<>();

                PropertyInfo l_PropertyInfo1Pin = new PropertyInfo();
                l_PropertyInfo1Pin.name = cWebserviceDefinitions.WEBPROPERTY_USERNAMEDUTCH;
                l_PropertyInfo1Pin.setValue(params[0].user);
                l_PropertyInfoObl.add(l_PropertyInfo1Pin);

                PropertyInfo l_PropertyInfo2Pin = new PropertyInfo();
                l_PropertyInfo2Pin.name = cWebserviceDefinitions.WEBPROPERTY_LOCATION_NL;
                l_PropertyInfo2Pin.setValue(params[0].branch);
                l_PropertyInfoObl.add(l_PropertyInfo2Pin);

                PropertyInfo l_PropertyInfo3Pin = new PropertyInfo();
                l_PropertyInfo3Pin.name = cWebserviceDefinitions.WEBPROPERTY_ORDERNUMBER;
                l_PropertyInfo3Pin.setValue(params[0].ordernumber);
                l_PropertyInfoObl.add(l_PropertyInfo3Pin);

                PropertyInfo l_PropertyInfo4Pin = new PropertyInfo();
                l_PropertyInfo4Pin.name = cWebserviceDefinitions.WEBPROPERTY_WORKPLACE;
                l_PropertyInfo4Pin.setValue(params[0].workplace);
                l_PropertyInfoObl.add(l_PropertyInfo4Pin);

                webresult = new cWebresult().pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_PICKORDERUPDATEWORKPLACE, l_PropertyInfoObl);

            } catch (JSONException e) {
                webresult.setSuccessBln(false);
                webresult.setResultBln(false);
            }
            return webresult;
        }
    }

    private static class mUpdatePickorderWorkplaceInDatabaseAsyncTask extends AsyncTask<UpdatePickorderWorkplaceLocalParams, Void, Integer> {
        private iPickorderDao mAsyncTaskDao;
        mUpdatePickorderWorkplaceInDatabaseAsyncTask(iPickorderDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected Integer doInBackground(UpdatePickorderWorkplaceLocalParams... params) {
            return mAsyncTaskDao.updatePickorderWorkplace(params[0].ordernumber, params[0].workplace);
        }
    }

    private static class mUpdatePickorderCurrentLocationInDatabaseAsyncTask extends AsyncTask<UpdatePickorderCurrentLocationLocalParams, Void, Integer> {
        private iPickorderDao mAsyncTaskDao;
        mUpdatePickorderCurrentLocationInDatabaseAsyncTask(iPickorderDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected Integer doInBackground(UpdatePickorderCurrentLocationLocalParams... params) {
            return mAsyncTaskDao.updatePickorderCurrentLocation(params[0].orderNumberStr, params[0].currentLocationStr);
        }
    }

    private static class mAbortOrderAsyncTask extends AsyncTask<Void, Void, Void> {
        private iPickorderLineDao mAsyncTaskDao;
        mAbortOrderAsyncTask(iPickorderLineDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected Void doInBackground(Void... params) {
            mAsyncTaskDao.abortOrder();
            return null;
        }
    }

    private static class mGetLinesViaWebserviceAsyncTask extends AsyncTask<String, Void, cWebresult> {
        @Override
        protected cWebresult doInBackground(String... params) {
            cWebresult webresult = new cWebresult();
            try {
                List<PropertyInfo> l_PropertyInfoObl = new ArrayList<>();

                PropertyInfo l_PropertyInfo1Pin = new PropertyInfo();
                l_PropertyInfo1Pin.name = cWebserviceDefinitions.WEBPROPERTY_LOCATION_NL;
                l_PropertyInfo1Pin.setValue(cUser.currentUser.currentBranch.getBranchStr());
                l_PropertyInfoObl.add(l_PropertyInfo1Pin);

                PropertyInfo l_PropertyInfo2Pin = new PropertyInfo();
                l_PropertyInfo2Pin.name = cWebserviceDefinitions.WEBPROPERTY_ORDERNUMBER;
                l_PropertyInfo2Pin.setValue(cPickorder.currentPickOrder.getOrderNumberStr());
                l_PropertyInfoObl.add(l_PropertyInfo2Pin);

                PropertyInfo l_PropertyInfo3Pin = new PropertyInfo();
                l_PropertyInfo3Pin.name = cWebserviceDefinitions.WEBPROPERTY_ACTIONTYPECODE;
                l_PropertyInfo3Pin.setValue(params[0]);
                l_PropertyInfoObl.add(l_PropertyInfo3Pin);

                webresult = new cWebresult().pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_GETPICKORDERLINES, l_PropertyInfoObl);

            } catch (JSONException e) {
                webresult.setSuccessBln(false);
                webresult.setResultBln(false);
            }
            return webresult;
        }
    }

    private static class mGetPackAndShipLinesViaWebserviceAsyncTask extends AsyncTask<Void, Void, cWebresult> {
        @Override
        protected cWebresult doInBackground(Void... params) {
            cWebresult webresult = new cWebresult();
            try {
                List<PropertyInfo> l_PropertyInfoObl = new ArrayList<>();

                PropertyInfo l_PropertyInfo1Pin = new PropertyInfo();
                l_PropertyInfo1Pin.name = cWebserviceDefinitions.WEBPROPERTY_LOCATION_NL;
                l_PropertyInfo1Pin.setValue(cUser.currentUser.currentBranch.getBranchStr());
                l_PropertyInfoObl.add(l_PropertyInfo1Pin);

                PropertyInfo l_PropertyInfo2Pin = new PropertyInfo();
                l_PropertyInfo2Pin.name = cWebserviceDefinitions.WEBPROPERTY_ORDERNUMBER;
                l_PropertyInfo2Pin.setValue(cPickorder.currentPickOrder.getOrderNumberStr());
                l_PropertyInfoObl.add(l_PropertyInfo2Pin);

                webresult = new cWebresult().pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_GETPICKORDERLINESPACKANDSHIP, l_PropertyInfoObl);

            } catch (JSONException e) {
                webresult.setSuccessBln(false);
                webresult.setResultBln(false);
            }
            return webresult;
        }
    }

    private static class mGetAllLinesFromDatabaseAsyncTask extends AsyncTask<Void, Void, List<cPickorderLineEntity>> {
        private iPickorderLineDao mAsyncTaskDao;

        mGetAllLinesFromDatabaseAsyncTask(iPickorderLineDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected List<cPickorderLineEntity> doInBackground(final Void... params) {
            return mAsyncTaskDao.getAll();
        }
    }

    private static class mGetPickorderLinesToSendFromDatabaseAsyncTask extends AsyncTask<Void, Void, List<cPickorderLineEntity>> {
        private iPickorderLineDao mAsyncTaskDao;

        mGetPickorderLinesToSendFromDatabaseAsyncTask(iPickorderLineDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected List<cPickorderLineEntity> doInBackground(final Void... params) {
            return mAsyncTaskDao.getPickorderLineEntitiesToSend();
        }
    }

    private static class mGetNotHandledLinesAsyncTask extends AsyncTask<Void, Void, List<cPickorderLineEntity>> {
        private iPickorderLineDao mAsyncTaskDao;

        mGetNotHandledLinesAsyncTask(iPickorderLineDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected List<cPickorderLineEntity> doInBackground(final Void... params) {
            return mAsyncTaskDao.getNotHandledPickorderLineEntitiesLin();
        }
    }

    private static class mGetBusyLinesAsyncTask extends AsyncTask<Void, Void, List<cPickorderLineEntity>> {
        private iPickorderLineDao mAsyncTaskDao;

        mGetBusyLinesAsyncTask(iPickorderLineDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected List<cPickorderLineEntity> doInBackground(final Void... params) {
            return mAsyncTaskDao.getBusyPickorderLineEntitiesLin();
        }
    }

    private static class mGetHandledLinesOblAsyncTask extends AsyncTask<Void, Void, List<cPickorderLineEntity>> {
        private iPickorderLineDao mAsyncTaskDao;

        mGetHandledLinesOblAsyncTask(iPickorderLineDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected List<cPickorderLineEntity> doInBackground(final Void... params) {
            return mAsyncTaskDao.getHandledPickorderLineEntities();
        }
    }

    private static class mGetNotHandledPackAndShipLinesAsyncTask extends AsyncTask<Void, Void, List<cPickorderLinePackAndShipEntity>> {
        private iPickorderLinePackAndShipDao mAsyncTaskDao;

        mGetNotHandledPackAndShipLinesAsyncTask(iPickorderLinePackAndShipDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected List<cPickorderLinePackAndShipEntity> doInBackground(final Void... params) {
            return mAsyncTaskDao.getNotHandledPickorderLinePackAndShipEntities();
        }
    }

    private static class mGetQuantityNotHandledAsyncTask extends AsyncTask<Void, Void, Double> {
        private iPickorderLineDao mAsyncTaskDao;
        mGetQuantityNotHandledAsyncTask(iPickorderLineDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected Double doInBackground(Void... params) {
            return mAsyncTaskDao.getQuantityNotHandledDbl();
        }
    }

    private static class mGetQuantityHandledAsyncTask extends AsyncTask<Void, Void, Double> {
        private iPickorderLineDao mAsyncTaskDao;
        mGetQuantityHandledAsyncTask(iPickorderLineDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected Double doInBackground(Void... params) {
            return mAsyncTaskDao.getNumberHandledDbl();
        }
    }

    private static class mGetTotalQuanitityAsyncTask extends AsyncTask<Void, Void, Double> {
        private iPickorderLineDao mAsyncTaskDao;

        mGetTotalQuanitityAsyncTask(iPickorderLineDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected Double doInBackground(Void... params) {
            return mAsyncTaskDao.getTotalQuantityDbl();
        }
    }


    private static class mGetPickorderAdressesFromWebserviceTask extends AsyncTask<List<String>, Void, cWebresult> {
        @Override
        protected cWebresult doInBackground(final List<String>... params) {
            cWebresult webResult = new cWebresult();

            List<PropertyInfo> l_PropertyInfoObl = new ArrayList<>();

            PropertyInfo l_PropertyInfo1Pin = new PropertyInfo();
            l_PropertyInfo1Pin.name = cWebserviceDefinitions.WEBPROPERTY_LOCATION_NL;
            l_PropertyInfo1Pin.setValue(cUser.currentUser.currentBranch.getBranchStr());
            l_PropertyInfoObl.add(l_PropertyInfo1Pin);

            PropertyInfo l_PropertyInfo2Pin = new PropertyInfo();
            l_PropertyInfo2Pin.name = cWebserviceDefinitions.WEBPROPERTY_ORDERNUMBER;
            l_PropertyInfo2Pin.setValue(cPickorder.currentPickOrder.getOrderNumberStr());
            l_PropertyInfoObl.add(l_PropertyInfo2Pin);

            try {
                webResult = new cWebresult().pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_GETPICKORDERADDRESSES, l_PropertyInfoObl);

            } catch (JSONException e) {
                webResult.setResultBln(false);
                webResult.setSuccessBln(false);
                e.printStackTrace();
            }

            return webResult;
        }
    }

    private static class mGetPickorderPackagesFromWebserviceTask extends AsyncTask<Void, Void, cWebresult> {
        @Override
        protected cWebresult doInBackground(final Void... params) {
            cWebresult webResult = new cWebresult();

            List<PropertyInfo> l_PropertyInfoObl = new ArrayList<>();

            PropertyInfo l_PropertyInfo1Pin = new PropertyInfo();
            l_PropertyInfo1Pin.name = cWebserviceDefinitions.WEBPROPERTY_LOCATION_NL;
            l_PropertyInfo1Pin.setValue(cUser.currentUser.currentBranch.getBranchStr());
            l_PropertyInfoObl.add(l_PropertyInfo1Pin);

            PropertyInfo l_PropertyInfo2Pin = new PropertyInfo();
            l_PropertyInfo2Pin.name = cWebserviceDefinitions.WEBPROPERTY_ORDERNUMBER;
            l_PropertyInfo2Pin.setValue(cPickorder.currentPickOrder.getOrderNumberStr());
            l_PropertyInfoObl.add(l_PropertyInfo2Pin);

            try {
                webResult = new cWebresult().pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_GETPICKORDERSHIPPACKAGES, l_PropertyInfoObl);

            } catch (JSONException e) {
                webResult.setResultBln(false);
                webResult.setSuccessBln(false);
                e.printStackTrace();
            }

            return webResult;
        }
    }

    private static class mPickorderBarcodesGetFromWebserviceAsyncTask extends AsyncTask <Void, Void, cWebresult>{
        @Override
        protected cWebresult doInBackground(final Void... params){
            cWebresult WebresultWrs = new cWebresult();

            List<PropertyInfo> l_PropertyInfoObl = new ArrayList<>();

            PropertyInfo l_PropertyInfo1Pin = new PropertyInfo();
            l_PropertyInfo1Pin.name = cWebserviceDefinitions.WEBPROPERTY_LOCATION_NL;
            l_PropertyInfo1Pin.setValue(cUser.currentUser.currentBranch.getBranchStr());
            l_PropertyInfoObl.add(l_PropertyInfo1Pin);

            PropertyInfo l_PropertyInfo2Pin = new PropertyInfo();
            l_PropertyInfo2Pin.name = cWebserviceDefinitions.WEBPROPERTY_ORDERNUMBER;
            l_PropertyInfo2Pin.setValue(cPickorder.currentPickOrder.getOrderNumberStr());
            l_PropertyInfoObl.add(l_PropertyInfo2Pin);

            try{
                WebresultWrs = cWebresult.pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_GETPICKORDERBARCODES, l_PropertyInfoObl);
            } catch (JSONException e) {
                WebresultWrs.setResultBln(false);
                WebresultWrs.setSuccessBln(false);
                e.printStackTrace();
            }

            return WebresultWrs;
        }
    }

    private static class mPickorderLineBarcodesGetFromWebserviceAsyncTask extends AsyncTask <String, Void, cWebresult>{
        @Override
        protected cWebresult doInBackground(final String... params){
            cWebresult WebresultWrs = new cWebresult();

            List<PropertyInfo> l_PropertyInfoObl = new ArrayList<>();

            PropertyInfo l_PropertyInfo1Pin = new PropertyInfo();
            l_PropertyInfo1Pin.name = cWebserviceDefinitions.WEBPROPERTY_LOCATION_NL;
            l_PropertyInfo1Pin.setValue(cUser.currentUser.currentBranch.getBranchStr());
            l_PropertyInfoObl.add(l_PropertyInfo1Pin);

            PropertyInfo l_PropertyInfo2Pin = new PropertyInfo();
            l_PropertyInfo2Pin.name = cWebserviceDefinitions.WEBPROPERTY_ORDERNUMBER;
            l_PropertyInfo2Pin.setValue(cPickorder.currentPickOrder.getOrderNumberStr());
            l_PropertyInfoObl.add(l_PropertyInfo2Pin);

            PropertyInfo l_PropertyInfo3Pin = new PropertyInfo();
            l_PropertyInfo3Pin.name = cWebserviceDefinitions.WEBPROPERTY_ACTIONTYPECODE;
            l_PropertyInfo3Pin.setValue(params[0]);
            l_PropertyInfoObl.add(l_PropertyInfo3Pin);

            try{
                WebresultWrs = cWebresult.pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_GETPICKORDERLINEBARCODES, l_PropertyInfoObl);
            } catch (JSONException e) {
                WebresultWrs.setResultBln(false);
                WebresultWrs.setSuccessBln(false);
                e.printStackTrace();
            }

            return WebresultWrs;
        }
    }

    private static class mGetCommentsFromWebserviceAsyncTask extends AsyncTask <Void, Void, cWebresult> {
        @Override
        protected cWebresult doInBackground(final Void... params){
            cWebresult WebresultWrs = new cWebresult();

            List<PropertyInfo> l_PropertyInfoObl = new ArrayList<>();

            PropertyInfo l_PropertyInfo1Pin = new PropertyInfo();
            l_PropertyInfo1Pin.name = cWebserviceDefinitions.WEBPROPERTY_LOCATION_NL;
            l_PropertyInfo1Pin.setValue(cUser.currentUser.currentBranch.getBranchStr());
            l_PropertyInfoObl.add(l_PropertyInfo1Pin);

            PropertyInfo l_PropertyInfo2Pin = new PropertyInfo();
            l_PropertyInfo2Pin.name = cWebserviceDefinitions.WEBPROPERTY_ORDERNUMBER;
            l_PropertyInfo2Pin.setValue(cPickorder.currentPickOrder.getOrderNumberStr());
            l_PropertyInfoObl.add(l_PropertyInfo2Pin);

            try{
                WebresultWrs = cWebresult.pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_GETPICKORDERCOMMENTS, l_PropertyInfoObl);
            } catch (JSONException e) {
                WebresultWrs.setResultBln(false);
                WebresultWrs.setSuccessBln(false);
                e.printStackTrace();
            }

            return WebresultWrs;
        }
    }

    private static class mUpdateCurrentLocationViaWebserviceAsyncTask extends AsyncTask<String, Void, cWebresult> {
        @Override
        protected cWebresult doInBackground(String... params) {
            cWebresult webresult = new cWebresult();
            try {
                List<PropertyInfo> l_PropertyInfoObl = new ArrayList<>();

                PropertyInfo l_PropertyInfo1Pin = new PropertyInfo();
                l_PropertyInfo1Pin.name = cWebserviceDefinitions.WEBPROPERTY_USERNAMEDUNGLISH;
                l_PropertyInfo1Pin.setValue(cUser.currentUser.getNameStr());
                l_PropertyInfoObl.add(l_PropertyInfo1Pin);

                PropertyInfo l_PropertyInfo2Pin = new PropertyInfo();
                l_PropertyInfo2Pin.name = cWebserviceDefinitions.WEBPROPERTY_LOCATION_NL;
                l_PropertyInfo2Pin.setValue(cUser.currentUser.currentBranch.getBranchStr());
                l_PropertyInfoObl.add(l_PropertyInfo2Pin);

                PropertyInfo l_PropertyInfo3Pin = new PropertyInfo();
                l_PropertyInfo3Pin.name = cWebserviceDefinitions.WEBPROPERTY_ORDERNUMBER;
                l_PropertyInfo3Pin.setValue(cPickorder.currentPickOrder.getOrderNumberStr());
                l_PropertyInfoObl.add(l_PropertyInfo3Pin);

                PropertyInfo l_PropertyInfo4Pin = new PropertyInfo();
                l_PropertyInfo4Pin.name = cWebserviceDefinitions.WEBPROPERTY_CURRENTLOCATION;
                l_PropertyInfo4Pin.setValue(params[0]);
                l_PropertyInfoObl.add(l_PropertyInfo4Pin);

                webresult = new cWebresult().pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_UPDATECURRENTORDERLOCATION, l_PropertyInfoObl);

            } catch (JSONException e) {
                webresult.setSuccessBln(false);
                webresult.setResultBln(false);
            }
            return webresult;
        }
    }


    //End Region Private Methods

}
