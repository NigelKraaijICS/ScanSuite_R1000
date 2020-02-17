package SSU_WHS.General.Warehouseorder;

import android.app.Application;
import android.os.AsyncTask;

import org.json.JSONException;
import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import SSU_WHS.Basics.Users.cUser;
import SSU_WHS.General.acScanSuiteDatabase;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebserviceDefinitions;

public class cWarehouseorderRepository {

    //Region Private Properties
    private acScanSuiteDatabase db;

    private static class WarehouseorderUnLockParams {

        String userStr;
        String orderTypeStr;
        String branchStr;
        String orderNumberStr;

        WarehouseorderUnLockParams(String pvUserStr, String pvOrderTypeStr, String pvBranchStr, String pvOrderNumberStr) {
            this.userStr = pvUserStr;
            this.orderTypeStr = pvOrderTypeStr;
            this.branchStr = pvBranchStr;
            this.orderNumberStr = pvOrderNumberStr;
        }
    }

    private static class WarehouseorderLockParams {

        String userStr;
        String languageStr;
        String orderTypeStr;
        String branchStr;
        String orderNumberStr;
        String deviceStr;
        String workflowStepstr;
        Integer workFlowStepInt;
        Boolean ignoreBusyBln;

        WarehouseorderLockParams(String pvUserStr, String pvLanguageStr, String pvOrderTypeStr, String pvBranchStr, String pvOrderNumberStr, String  pvDeviceStr, String pvWorkFlowStepStr, Integer pv_workflowstepint, Boolean pvIgnoreBusyBln) {
            this.userStr = pvUserStr;
            this.languageStr = pvLanguageStr;
            this.orderTypeStr = pvOrderTypeStr;
            this.branchStr = pvBranchStr;
            this.orderNumberStr = pvOrderNumberStr;
            this.deviceStr = pvDeviceStr;
            this.workflowStepstr = pvWorkFlowStepStr;
            this.workFlowStepInt = pv_workflowstepint;
            this.ignoreBusyBln = pvIgnoreBusyBln;
        }
    }

    private static class WarehouseorderLockReleaseParams {

        String userStr;
        String languageStr;
        String orderTypeStr;
        String branchStr;
        String orderNumberStr;
        String deviceStr;
        String workFlowStepStr;
        Integer workFlowStepInt;

        WarehouseorderLockReleaseParams(String pvUserStr, String pvLanguageStr, String pvOrderTypeStr, String pvBranchStr, String pvOrdernumberStr, String  pvDeviceStr, String pvWorkFlowStepStr, Integer pvWorkflowStepInt) {
            this.userStr = pvUserStr;
            this.languageStr = pvLanguageStr;
            this.orderTypeStr = pvOrderTypeStr;
            this.branchStr = pvBranchStr;
            this.orderNumberStr = pvOrdernumberStr;
            this.deviceStr = pvDeviceStr;
            this.workFlowStepStr = pvWorkFlowStepStr;
            this.workFlowStepInt = pvWorkflowStepInt;
        }
    }

    //End Region Private Properties

    //Region Constructor
    cWarehouseorderRepository(Application pvApplication) {
        this.db = acScanSuiteDatabase.pGetDatabase(pvApplication);
    }
    //End Region Constructor

    //Region Public Methods

    public static cWebresult pWarehouseopdrachtUnlockViaWebserviceWrs(String pvOrdertypeStr,String pvOrderNumberStr) {

        cWebresult webResultWrs = new cWebresult();

        List<String> resultObl = new ArrayList<>();
        WarehouseorderUnLockParams warehouseorderUnLockParams = new WarehouseorderUnLockParams(cUser.currentUser.getNameStr(), pvOrdertypeStr, cUser.currentUser.currentBranch.getBranchStr(), pvOrderNumberStr);

        try {
            webResultWrs = new mWarehouseorderUnLockAsyncTask().execute(warehouseorderUnLockParams).get();
        } catch (ExecutionException | InterruptedException e) {
            webResultWrs.setResultBln(false);
            webResultWrs.setSuccessBln(false);
            resultObl.add(e.getLocalizedMessage());
            webResultWrs.setResultObl(resultObl);
            e.printStackTrace();
        }
        return webResultWrs;
    }

    public static cWebresult pWarehouseopdrachtLockViaWebserviceWrs(String pvOrderTypeStr, String pvOrderNumberStr, String pvDeviceStr, String pvWorkflowStepStr, Integer pv_WorkflowStepInt, Boolean pvIgnoreBusyBln) {
        cWebresult webResultWrs = new cWebresult();

        List<String> resultObl = new ArrayList<>();
        WarehouseorderLockParams warehouseorderLockParams = new WarehouseorderLockParams(cUser.currentUser.getNameStr().toUpperCase(), "", pvOrderTypeStr, cUser.currentUser.currentBranch.getBranchStr(), pvOrderNumberStr, pvDeviceStr, pvWorkflowStepStr, pv_WorkflowStepInt, pvIgnoreBusyBln);
        try {
            webResultWrs = new mWarehouseorderLockAsyncTask().execute(warehouseorderLockParams).get();
        } catch (ExecutionException | InterruptedException e) {
            webResultWrs.setResultBln(false);
            webResultWrs.setSuccessBln(false);
            resultObl.add(e.getLocalizedMessage());
            webResultWrs.setResultObl(resultObl);
            e.printStackTrace();
        }
        return webResultWrs;
    }

    public static cWebresult pWarehouseorderLockReleaseViaWebserviceWrs(String pvOrderTypeStr,String pvOrderNumberStr, String pvDeviceStr, String pvWorkFlowStepStr, Integer pvWorkFlowStepInt) {
        cWebresult webResultWrs = new cWebresult();

        List<String> resultObl = new ArrayList<>();
        WarehouseorderLockReleaseParams warehouseorderLockReleaseParams = new WarehouseorderLockReleaseParams(cUser.currentUser.getNameStr().toUpperCase(), "", pvOrderTypeStr, cUser.currentUser.currentBranch.getBranchStr(), pvOrderNumberStr, pvDeviceStr,pvWorkFlowStepStr, pvWorkFlowStepInt);
        try {
            webResultWrs = new mWarehouseorderLockReleaseAsyncTask().execute(warehouseorderLockReleaseParams).get();
        } catch (ExecutionException | InterruptedException e) {
            webResultWrs.setResultBln(false);
            webResultWrs.setSuccessBln(false);
            resultObl.add(e.getLocalizedMessage());
            webResultWrs.setResultObl(resultObl);
            e.printStackTrace();
        }
        return webResultWrs;
    }

    //End Region Public Methods

    //Region Private Methods

    private static class mWarehouseorderUnLockAsyncTask extends AsyncTask<WarehouseorderUnLockParams, Void, cWebresult> {
        @Override
        protected cWebresult doInBackground(WarehouseorderUnLockParams... params) {
            cWebresult webResultWrs = new cWebresult();
            try {
                List<PropertyInfo> l_PropertyInfoObl = new ArrayList<>();

                PropertyInfo l_PropertyInfo1Pin = new PropertyInfo();
                l_PropertyInfo1Pin.name = cWebserviceDefinitions.WEBPROPERTY_USERNAMEDUTCH;
                l_PropertyInfo1Pin.setValue(params[0].userStr);
                l_PropertyInfoObl.add(l_PropertyInfo1Pin);

                PropertyInfo l_PropertyInfo2Pin = new PropertyInfo();
                l_PropertyInfo2Pin.name = cWebserviceDefinitions.WEBPROPERTY_ORDERTYPE;
                l_PropertyInfo2Pin.setValue(params[0].orderTypeStr);
                l_PropertyInfoObl.add(l_PropertyInfo2Pin);

                PropertyInfo l_PropertyInfo3Pin = new PropertyInfo();
                l_PropertyInfo3Pin.name = cWebserviceDefinitions.WEBPROPERTY_LOCATION_NL;
                l_PropertyInfo3Pin.setValue(params[0].branchStr);
                l_PropertyInfoObl.add(l_PropertyInfo3Pin);

                PropertyInfo l_PropertyInfo4Pin = new PropertyInfo();
                l_PropertyInfo4Pin.name = cWebserviceDefinitions.WEBPROPERTY_ORDERNUMBER;
                l_PropertyInfo4Pin.setValue(params[0].orderNumberStr);
                l_PropertyInfoObl.add(l_PropertyInfo4Pin);

                new cWebresult();
                webResultWrs = cWebresult.pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_WAREHOUSEORDERUNLOCK, l_PropertyInfoObl);
            } catch (JSONException e) {
                webResultWrs.setSuccessBln(false);
                webResultWrs.setResultBln(false);
            }
            return webResultWrs;
        }
    }

    private static class mWarehouseorderLockAsyncTask extends AsyncTask<WarehouseorderLockParams, Void, cWebresult> {
        @Override
        protected cWebresult doInBackground(WarehouseorderLockParams... params) {
            cWebresult l_WebresultWrs = new cWebresult();
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
                l_PropertyInfo3Pin.name = cWebserviceDefinitions.WEBPROPERTY_ORDERTYPE;
                l_PropertyInfo3Pin.setValue(params[0].orderTypeStr);
                l_PropertyInfoObl.add(l_PropertyInfo3Pin);

                PropertyInfo l_PropertyInfo4Pin = new PropertyInfo();
                l_PropertyInfo4Pin.name = cWebserviceDefinitions.WEBPROPERTY_LOCATION_NL;
                l_PropertyInfo4Pin.setValue(params[0].branchStr);
                l_PropertyInfoObl.add(l_PropertyInfo4Pin);

                PropertyInfo l_PropertyInfo5Pin = new PropertyInfo();
                l_PropertyInfo5Pin.name = cWebserviceDefinitions.WEBPROPERTY_ORDERNUMBER;
                l_PropertyInfo5Pin.setValue(params[0].orderNumberStr);
                l_PropertyInfoObl.add(l_PropertyInfo5Pin);

                PropertyInfo l_PropertyInfo6Pin = new PropertyInfo();
                l_PropertyInfo6Pin.name = cWebserviceDefinitions.WEBPROPERTY_SCANNER;
                l_PropertyInfo6Pin.setValue(params[0].deviceStr);
                l_PropertyInfoObl.add(l_PropertyInfo6Pin);

                PropertyInfo l_PropertyInfo7Pin = new PropertyInfo();
                l_PropertyInfo7Pin.name = cWebserviceDefinitions.WEBPROPERTY_WORKFLOWSTEPSTR;
                l_PropertyInfo7Pin.setValue(params[0].workflowStepstr);
                l_PropertyInfoObl.add(l_PropertyInfo7Pin);

                PropertyInfo l_PropertyInfo8Pin = new PropertyInfo();
                l_PropertyInfo8Pin.name = cWebserviceDefinitions.WEBPROPERTY_WORKFLOWSTEPINT;
                l_PropertyInfo8Pin.setValue(params[0].workFlowStepInt);
                l_PropertyInfoObl.add(l_PropertyInfo8Pin);

                PropertyInfo l_PropertyInfo9Pin = new PropertyInfo();
                l_PropertyInfo9Pin.name = cWebserviceDefinitions.WEBPROPERTY_IGNOREBUSY;
                l_PropertyInfo9Pin.setValue(params[0].ignoreBusyBln);
                l_PropertyInfoObl.add(l_PropertyInfo9Pin);

                new cWebresult();
                l_WebresultWrs = cWebresult.pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_WAREHOUSEORDERLOCK, l_PropertyInfoObl);
            } catch (JSONException e) {
                l_WebresultWrs.setSuccessBln(false);
                l_WebresultWrs.setResultBln(false);
            }
            return l_WebresultWrs;
        }
    }

    private static class mWarehouseorderLockReleaseAsyncTask extends AsyncTask<WarehouseorderLockReleaseParams, Void, cWebresult> {
        @Override
        protected cWebresult doInBackground(WarehouseorderLockReleaseParams... params) {
            cWebresult webResult = new cWebresult();
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
                l_PropertyInfo3Pin.name = cWebserviceDefinitions.WEBPROPERTY_ORDERTYPE;
                l_PropertyInfo3Pin.setValue(params[0].orderTypeStr);
                l_PropertyInfoObl.add(l_PropertyInfo3Pin);

                PropertyInfo l_PropertyInfo4Pin = new PropertyInfo();
                l_PropertyInfo4Pin.name = cWebserviceDefinitions.WEBPROPERTY_LOCATION_NL;
                l_PropertyInfo4Pin.setValue(params[0].branchStr);
                l_PropertyInfoObl.add(l_PropertyInfo4Pin);

                PropertyInfo l_PropertyInfo5Pin = new PropertyInfo();
                l_PropertyInfo5Pin.name = cWebserviceDefinitions.WEBPROPERTY_ORDERNUMBER;
                l_PropertyInfo5Pin.setValue(params[0].orderNumberStr);
                l_PropertyInfoObl.add(l_PropertyInfo5Pin);

                PropertyInfo l_PropertyInfo6Pin = new PropertyInfo();
                l_PropertyInfo6Pin.name = cWebserviceDefinitions.WEBPROPERTY_SCANNER;
                l_PropertyInfo6Pin.setValue(params[0].deviceStr);
                l_PropertyInfoObl.add(l_PropertyInfo6Pin);

                PropertyInfo l_PropertyInfo7Pin = new PropertyInfo();
                l_PropertyInfo7Pin.name = cWebserviceDefinitions.WEBPROPERTY_WORKFLOWSTEPCODESTR;
                l_PropertyInfo7Pin.setValue(params[0].workFlowStepStr);
                l_PropertyInfoObl.add(l_PropertyInfo7Pin);

                PropertyInfo l_PropertyInfo8Pin = new PropertyInfo();
                l_PropertyInfo8Pin.name = cWebserviceDefinitions.WEBPROPERTY_WORKFLOWSTEPINT;
                l_PropertyInfo8Pin.setValue(params[0].workFlowStepInt);
                l_PropertyInfoObl.add(l_PropertyInfo8Pin);

                new cWebresult();
                webResult = cWebresult.pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_WAREHOUSEORDERLOCKRELEASE, l_PropertyInfoObl);
            } catch (JSONException e) {
                webResult.setSuccessBln(false);
                webResult.setResultBln(false);
            }
            return webResult;
        }
    }
}
