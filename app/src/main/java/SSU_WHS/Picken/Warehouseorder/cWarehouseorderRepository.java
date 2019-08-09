package SSU_WHS.Picken.Warehouseorder;

import android.app.Application;
import android.os.AsyncTask;

import org.json.JSONException;
import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebserviceDefinitions;
import SSU_WHS.General.acScanSuiteDatabase;

public class cWarehouseorderRepository {
    private cWebresult webResult;

    cWarehouseorderRepository(Application application) {
        acScanSuiteDatabase db = acScanSuiteDatabase.getDatabase(application);
    }
    private static class WarehouseorderUnLockParams {
        String user;
        String ordertype;
        String branch;
        String ordernumber;

        WarehouseorderUnLockParams(String pv_user, String pv_ordertype, String pv_branch, String pv_ordernumber) {
            this.user = pv_user;
            this.ordertype = pv_ordertype;
            this.branch = pv_branch;
            this.ordernumber = pv_ordernumber;
        }
    }

    private static class WarehouseorderLockParams {
        String user;
        String language;
        String ordertype;
        String branch;
        String ordernumber;
        String device;
        String workflowstepstr;
        Integer workflowstepint;
        Boolean ignorebusy;

        WarehouseorderLockParams(String pv_user, String pv_language, String pv_ordertype, String pv_branch, String pv_ordernumber, String  pv_device, String pv_workflowstepstr, Integer pv_workflowstepint, Boolean pv_ignorebusy) {
            this.user = pv_user;
            this.language = pv_language;
            this.ordertype = pv_ordertype;
            this.branch = pv_branch;
            this.ordernumber = pv_ordernumber;
            this.device = pv_device;
            this.workflowstepstr = pv_workflowstepstr;
            this.workflowstepint = pv_workflowstepint;
            this.ignorebusy = pv_ignorebusy;
        }
    }
    private static class WarehouseorderLockReleaseParams {
        String user;
        String language;
        String ordertype;
        String branch;
        String ordernumber;
        String device;
        String workflowstepstr;
        Integer workflowstepint;

        WarehouseorderLockReleaseParams(String pv_user, String pv_language, String pv_ordertype, String pv_branch, String pv_ordernumber, String  pv_device, String pv_workflowstepstr, Integer pv_workflowstepint) {
            this.user = pv_user;
            this.language = pv_language;
            this.ordertype = pv_ordertype;
            this.branch = pv_branch;
            this.ordernumber = pv_ordernumber;
            this.device = pv_device;
            this.workflowstepstr = pv_workflowstepstr;
            this.workflowstepint = pv_workflowstepint;
        }
    }
    private static class UpdateCurrentOrderLocationParams {
        String user;
        String branch;
        String ordernumber;
        String currentLocation;

        UpdateCurrentOrderLocationParams(String pv_user, String pv_branch, String pv_ordernumber, String pv_currentLocation) {
            this.user = pv_user;
            this.branch = pv_branch;
            this.ordernumber = pv_ordernumber;
            this.currentLocation = pv_currentLocation;
        }
    }

    public Boolean updateCurrentOrderLocation(String user, String branch, String ordernumber, String currentLocation) {
        cWebresult l_webResult;
        Boolean l_resultBln = false;

        UpdateCurrentOrderLocationParams updateCurrentOrderLocationParams = new UpdateCurrentOrderLocationParams(user, branch, ordernumber, currentLocation);

        try {
            l_webResult = new updateCurrentOrderLocationAsyncTask().execute(updateCurrentOrderLocationParams).get();
            if (l_webResult != null) {
                if (!l_webResult.getSuccessBln()) {
                    l_resultBln = false;
                    return l_resultBln;
                }
                if (!l_webResult.getResultBln()) {
                    l_resultBln = false;
                    return l_resultBln;
                }
                l_resultBln = true;
                return l_resultBln;
            }
            else {
                l_resultBln = false;
            }
        } catch (InterruptedException e) {
            l_resultBln = false;
            e.printStackTrace();
        } catch (ExecutionException e) {
            l_resultBln = false;
            e.printStackTrace();
        }
        return l_resultBln;

    }

    private static class updateCurrentOrderLocationAsyncTask extends AsyncTask<UpdateCurrentOrderLocationParams, Void, cWebresult> {
        @Override
        protected cWebresult doInBackground(UpdateCurrentOrderLocationParams... params) {
            cWebresult l_WebresultWrs = new cWebresult();
            try {
                List<PropertyInfo> l_PropertyInfoObl = new ArrayList<>();

                PropertyInfo l_PropertyInfo1Pin = new PropertyInfo();
                l_PropertyInfo1Pin.name = cWebserviceDefinitions.WEBPROPERTY_USERNAMEDUTCH;
                l_PropertyInfo1Pin.setValue(params[0].user);
                l_PropertyInfoObl.add(l_PropertyInfo1Pin);

                PropertyInfo l_PropertyInfo2Pin = new PropertyInfo();
                l_PropertyInfo2Pin.name = cWebserviceDefinitions.WEBPROPERTY_BRANCH;
                l_PropertyInfo2Pin.setValue(params[0].branch);
                l_PropertyInfoObl.add(l_PropertyInfo2Pin);

                PropertyInfo l_PropertyInfo3Pin = new PropertyInfo();
                l_PropertyInfo3Pin.name = cWebserviceDefinitions.WEBPROPERTY_ORDERNUMBER;
                l_PropertyInfo3Pin.setValue(params[0].ordernumber);
                l_PropertyInfoObl.add(l_PropertyInfo3Pin);

                PropertyInfo l_PropertyInfo4Pin = new PropertyInfo();
                l_PropertyInfo4Pin.name = cWebserviceDefinitions.WEBPROPERTY_CURRENTLOCATION;
                l_PropertyInfo4Pin.setValue(params[0].currentLocation);
                l_PropertyInfoObl.add(l_PropertyInfo4Pin);

                l_WebresultWrs = new cWebresult().mGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_UPDATECURRENTORDERLOCATION, l_PropertyInfoObl);
            } catch (JSONException e) {
                l_WebresultWrs.setSuccessBln(false);
                l_WebresultWrs.setResultBln(false);
            }
            return l_WebresultWrs;
        }
    }



    public Boolean getWarehouseUnlockBln(String user, String ordertype, String branch, String ordernumber) {
        cWebresult l_webResult;
        Boolean l_resultBln = false;

        WarehouseorderUnLockParams warehouseorderUnLockParams = new WarehouseorderUnLockParams(user, ordertype, branch, ordernumber);
        try {
            l_webResult = new getWarehouseorderUnLockAsyncTask().execute(warehouseorderUnLockParams).get();
            if (l_webResult != null) {
                if (!l_webResult.getSuccessBln()) {
                    l_resultBln = false;
                    return l_resultBln;
                }
                if (!l_webResult.getResultBln()) {
                    l_resultBln = false;
                    return l_resultBln;
                }
                l_resultBln = true;
                return l_resultBln;
            }
            else {
                l_resultBln = false;
            }
        } catch (InterruptedException e) {
            l_resultBln = false;
            e.printStackTrace();
        } catch (ExecutionException e) {
            l_resultBln = false;
            e.printStackTrace();
        }
        return l_resultBln;
    }

    private static class getWarehouseorderUnLockAsyncTask extends AsyncTask<WarehouseorderUnLockParams, Void, cWebresult> {
        @Override
        protected cWebresult doInBackground(WarehouseorderUnLockParams... params) {
            cWebresult l_WebresultWrs = new cWebresult();
            try {
                List<PropertyInfo> l_PropertyInfoObl = new ArrayList<>();

                PropertyInfo l_PropertyInfo1Pin = new PropertyInfo();
                l_PropertyInfo1Pin.name = cWebserviceDefinitions.WEBPROPERTY_USERNAMEDUTCH;
                l_PropertyInfo1Pin.setValue(params[0].user);
                l_PropertyInfoObl.add(l_PropertyInfo1Pin);

                PropertyInfo l_PropertyInfo2Pin = new PropertyInfo();
                l_PropertyInfo2Pin.name = cWebserviceDefinitions.WEBPROPERTY_ORDERTYPE;
                l_PropertyInfo2Pin.setValue(params[0].ordertype);
                l_PropertyInfoObl.add(l_PropertyInfo2Pin);

                PropertyInfo l_PropertyInfo3Pin = new PropertyInfo();
                l_PropertyInfo3Pin.name = cWebserviceDefinitions.WEBPROPERTY_BRANCH;
                l_PropertyInfo3Pin.setValue(params[0].branch);
                l_PropertyInfoObl.add(l_PropertyInfo3Pin);

                PropertyInfo l_PropertyInfo4Pin = new PropertyInfo();
                l_PropertyInfo4Pin.name = cWebserviceDefinitions.WEBPROPERTY_ORDERNUMBER;
                l_PropertyInfo4Pin.setValue(params[0].ordernumber);
                l_PropertyInfoObl.add(l_PropertyInfo4Pin);

                l_WebresultWrs = new cWebresult().mGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_WAREHOUSEORDERUNLOCK, l_PropertyInfoObl);
            } catch (JSONException e) {
                l_WebresultWrs.setSuccessBln(false);
                l_WebresultWrs.setResultBln(false);
            }
            return l_WebresultWrs;
        }
    }


    public Boolean getWarehouseorderLockBln(String user, String language, String ordertype, String branch, String ordernumber, String device, String workflowstepstr, Integer workflowstepint, Boolean ignorebusy) {
        cWebresult l_webResult;
        Boolean l_resultBln;
        WarehouseorderLockParams warehouseorderLockParams = new WarehouseorderLockParams(user, language, ordertype, branch, ordernumber, device, workflowstepstr, workflowstepint, ignorebusy);
        try {
            l_webResult = new getWarehouseorderLockAsyncTask().execute(warehouseorderLockParams).get();
            if (l_webResult != null) {
                if (!l_webResult.getSuccessBln()) {
                    l_resultBln = false;
                    return l_resultBln;
                }
                if (!l_webResult.getResultBln()) {
                    l_resultBln = false;
                    return l_resultBln;
                }
                l_resultBln = true;
                return l_resultBln;
            }
            else {
                l_resultBln = false;
            }
        } catch (InterruptedException e) {
            l_resultBln = false;
            e.printStackTrace();
        } catch (ExecutionException e) {
            l_resultBln = false;
            e.printStackTrace();
        }
        return l_resultBln;
    }
    private static class getWarehouseorderLockAsyncTask extends AsyncTask<WarehouseorderLockParams, Void, cWebresult> {
        @Override
        protected cWebresult doInBackground(WarehouseorderLockParams... params) {
            cWebresult l_WebresultWrs = new cWebresult();
            try {
                List<PropertyInfo> l_PropertyInfoObl = new ArrayList<>();

                PropertyInfo l_PropertyInfo1Pin = new PropertyInfo();
                l_PropertyInfo1Pin.name = cWebserviceDefinitions.WEBPROPERTY_USERNAMEDUTCH;
                l_PropertyInfo1Pin.setValue(params[0].user);
                l_PropertyInfoObl.add(l_PropertyInfo1Pin);

                PropertyInfo l_PropertyInfo2Pin = new PropertyInfo();
                l_PropertyInfo2Pin.name = cWebserviceDefinitions.WEBPROPERTY_LANGUAGEASCULTURE;
                l_PropertyInfo2Pin.setValue(params[0].language);
                l_PropertyInfoObl.add(l_PropertyInfo2Pin);

                PropertyInfo l_PropertyInfo3Pin = new PropertyInfo();
                l_PropertyInfo3Pin.name = cWebserviceDefinitions.WEBPROPERTY_ORDERTYPE;
                l_PropertyInfo3Pin.setValue(params[0].ordertype);
                l_PropertyInfoObl.add(l_PropertyInfo3Pin);

                PropertyInfo l_PropertyInfo4Pin = new PropertyInfo();
                l_PropertyInfo4Pin.name = cWebserviceDefinitions.WEBPROPERTY_BRANCH;
                l_PropertyInfo4Pin.setValue(params[0].branch);
                l_PropertyInfoObl.add(l_PropertyInfo4Pin);

                PropertyInfo l_PropertyInfo5Pin = new PropertyInfo();
                l_PropertyInfo5Pin.name = cWebserviceDefinitions.WEBPROPERTY_ORDERNUMBER;
                l_PropertyInfo5Pin.setValue(params[0].ordernumber);
                l_PropertyInfoObl.add(l_PropertyInfo5Pin);

                PropertyInfo l_PropertyInfo6Pin = new PropertyInfo();
                l_PropertyInfo6Pin.name = cWebserviceDefinitions.WEBPROPERTY_SCANNER;
                l_PropertyInfo6Pin.setValue(params[0].device);
                l_PropertyInfoObl.add(l_PropertyInfo6Pin);

                PropertyInfo l_PropertyInfo7Pin = new PropertyInfo();
                l_PropertyInfo7Pin.name = cWebserviceDefinitions.WEBPROPERTY_WORKFLOWSTEPSTR;
                l_PropertyInfo7Pin.setValue(params[0].workflowstepstr);
                l_PropertyInfoObl.add(l_PropertyInfo7Pin);

                PropertyInfo l_PropertyInfo8Pin = new PropertyInfo();
                l_PropertyInfo8Pin.name = cWebserviceDefinitions.WEBPROPERTY_WORKFLOWSTEPINT;
                l_PropertyInfo8Pin.setValue(params[0].workflowstepint);
                l_PropertyInfoObl.add(l_PropertyInfo8Pin);

                PropertyInfo l_PropertyInfo9Pin = new PropertyInfo();
                l_PropertyInfo9Pin.name = cWebserviceDefinitions.WEBPROPERTY_IGNOREBUSY;
                l_PropertyInfo9Pin.setValue(params[0].ignorebusy);
                l_PropertyInfoObl.add(l_PropertyInfo9Pin);

                l_WebresultWrs = new cWebresult().mGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_WAREHOUSEORDERLOCK, l_PropertyInfoObl);
            } catch (JSONException e) {
                l_WebresultWrs.setSuccessBln(false);
                l_WebresultWrs.setResultBln(false);
            }
            return l_WebresultWrs;
        }
    }


    public Boolean getWarehouseorderLockReleaseBln(String user, String language, String ordertype, String branch, String ordernumber, String device, String workflowstepstr, Integer workflowstepint) {
        cWebresult l_webResult;
        Boolean l_resultBln;
        WarehouseorderLockReleaseParams warehouseorderLockReleaseParams = new WarehouseorderLockReleaseParams(user, language, ordertype, branch, ordernumber, device, workflowstepstr, workflowstepint);
        try {
            l_webResult = new getWarehouseorderLockReleaseAsyncTask().execute(warehouseorderLockReleaseParams).get();
            if (l_webResult != null) {
                if (!l_webResult.getSuccessBln()) {
                    l_resultBln = false;
                    return l_resultBln;
                }
                if (!l_webResult.getResultBln()) {
                    l_resultBln = false;
                    return l_resultBln;
                }
                l_resultBln = true;
                return l_resultBln;
            }
            else {
                l_resultBln = false;
            }
        } catch (InterruptedException e) {
            l_resultBln = false;
            e.printStackTrace();
        } catch (ExecutionException e) {
            l_resultBln = false;
            e.printStackTrace();
        }
        return l_resultBln;
    }
    private static class getWarehouseorderLockReleaseAsyncTask extends AsyncTask<WarehouseorderLockReleaseParams, Void, cWebresult> {
        @Override
        protected cWebresult doInBackground(WarehouseorderLockReleaseParams... params) {
            cWebresult l_WebresultWrs = new cWebresult();
            try {
                List<PropertyInfo> l_PropertyInfoObl = new ArrayList<>();

                PropertyInfo l_PropertyInfo1Pin = new PropertyInfo();
                l_PropertyInfo1Pin.name = cWebserviceDefinitions.WEBPROPERTY_USERNAMEDUTCH;
                l_PropertyInfo1Pin.setValue(params[0].user);
                l_PropertyInfoObl.add(l_PropertyInfo1Pin);

                PropertyInfo l_PropertyInfo2Pin = new PropertyInfo();
                l_PropertyInfo2Pin.name = cWebserviceDefinitions.WEBPROPERTY_LANGUAGEASCULTURE;
                l_PropertyInfo2Pin.setValue(params[0].language);
                l_PropertyInfoObl.add(l_PropertyInfo2Pin);

                PropertyInfo l_PropertyInfo3Pin = new PropertyInfo();
                l_PropertyInfo3Pin.name = cWebserviceDefinitions.WEBPROPERTY_ORDERTYPE;
                l_PropertyInfo3Pin.setValue(params[0].ordertype);
                l_PropertyInfoObl.add(l_PropertyInfo3Pin);

                PropertyInfo l_PropertyInfo4Pin = new PropertyInfo();
                l_PropertyInfo4Pin.name = cWebserviceDefinitions.WEBPROPERTY_BRANCH;
                l_PropertyInfo4Pin.setValue(params[0].branch);
                l_PropertyInfoObl.add(l_PropertyInfo4Pin);

                PropertyInfo l_PropertyInfo5Pin = new PropertyInfo();
                l_PropertyInfo5Pin.name = cWebserviceDefinitions.WEBPROPERTY_ORDERNUMBER;
                l_PropertyInfo5Pin.setValue(params[0].ordernumber);
                l_PropertyInfoObl.add(l_PropertyInfo5Pin);

                PropertyInfo l_PropertyInfo6Pin = new PropertyInfo();
                l_PropertyInfo6Pin.name = cWebserviceDefinitions.WEBPROPERTY_SCANNER;
                l_PropertyInfo6Pin.setValue(params[0].device);
                l_PropertyInfoObl.add(l_PropertyInfo6Pin);

                PropertyInfo l_PropertyInfo7Pin = new PropertyInfo();
                l_PropertyInfo7Pin.name = cWebserviceDefinitions.WEBPROPERTY_WORKFLOWSTEPCODESTR;
                l_PropertyInfo7Pin.setValue(params[0].workflowstepstr);
                l_PropertyInfoObl.add(l_PropertyInfo7Pin);

                PropertyInfo l_PropertyInfo8Pin = new PropertyInfo();
                l_PropertyInfo8Pin.name = cWebserviceDefinitions.WEBPROPERTY_WORKFLOWSTEPINT;
                l_PropertyInfo8Pin.setValue(params[0].workflowstepint);
                l_PropertyInfoObl.add(l_PropertyInfo8Pin);

                l_WebresultWrs = new cWebresult().mGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_WAREHOUSEORDERLOCKRELEASE, l_PropertyInfoObl);
            } catch (JSONException e) {
                l_WebresultWrs.setSuccessBln(false);
                l_WebresultWrs.setResultBln(false);
            }
            return l_WebresultWrs;
        }
    }
}
