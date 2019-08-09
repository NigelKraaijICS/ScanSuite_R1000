package SSU_WHS.Pickorders;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.db.SimpleSQLiteQuery;
import android.arch.persistence.db.SupportSQLiteQuery;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import SSU_WHS.Complex_types.c_InterfaceShippingPackageIesp;
import SSU_WHS.Webservice.cWebserviceDefinitions;
import SSU_WHS.acScanSuiteDatabase;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebservice;

import static ICS.Utils.cText.addSingleQuotes;

public class cPickorderRepository {
    private iPickorderDao pickorderDao;
    private cWebresult webResult;
    private LiveData<List<cPickorderEntity>> mLocalPickorders;

    private class SortorderGetParams {
        String user;
        String branch;
        Integer pickstep;
        String searchtext;
        String maintype;

        SortorderGetParams(String pv_user, String pv_branch, Integer pv_pickstep, String pv_searchtext, String pv_maintype) {
            this.user = pv_user;
            this.branch = pv_branch;
            this.pickstep = pv_pickstep;
            this.searchtext = pv_searchtext;
            this.maintype = pv_maintype;
        }
    }

    private class PickorderStepHandledParams {
        String user;
        String language;
        String branch;
        String ordernumber;
        String device;
        String workplace;
        String workflowstepcode;
        Integer workflowstep;
        String culture;

        PickorderStepHandledParams(String pv_user, String pv_language, String pv_Branch, String pv_orderNumber, String pv_device, String pv_workplace, String pv_workflowStepcode, Integer pv_workflowStep, String pv_culture) {
            this.user = pv_user;
            this.language = pv_language;
            this.branch = pv_Branch;
            this.ordernumber = pv_orderNumber;
            this.device = pv_device;
            this.workplace = pv_workplace;
            this.workflowstepcode = pv_workflowStepcode;
            this.workflowstep = pv_workflowStep;
            this.culture = pv_culture;
        }
    }

    private class filterOrderLinesParams {
        Boolean useFilters;
        Boolean showProcessedWait;
        Boolean showSingleArticles;
        Boolean showAssignedToMe;
        Boolean showAssignedToOthers;
        Boolean showNotAssigned;

        filterOrderLinesParams(Boolean pv_useFilters, Boolean pv_showProcessedWait, Boolean pv_showSingleArticles, Boolean pv_showAssignedToMe, Boolean pv_showAssignedToOthers, Boolean pv_showNotAssigned) {
            this.useFilters = pv_useFilters;
            this.showProcessedWait = pv_showProcessedWait;
            this.showSingleArticles = pv_showSingleArticles;
            this.showAssignedToMe = pv_showAssignedToMe;
            this.showAssignedToOthers = pv_showAssignedToOthers;
            this.showNotAssigned = pv_showNotAssigned;
        }
    }

    cPickorderRepository(Application application) {
        acScanSuiteDatabase db = acScanSuiteDatabase.getDatabase(application);
        pickorderDao = db.pickorderDao();
    }
    public LiveData<List<cPickorderEntity>> getFilteredPickorders(String currentUser, Boolean useFilters, Boolean showProcessedWait, Boolean showSingleArticles, Boolean showAssignedToMe, Boolean showAssignedToOthers, Boolean showNotAssigned) {
        LiveData<List<cPickorderEntity>> l_PickorderEntityObl = null;
        String sqlquery;

        sqlquery = "SELECT * FROM Pickorders ";
        if (useFilters) {
//            TTT
            if (showAssignedToMe && showAssignedToOthers && showNotAssigned) {
                sqlquery += "WHERE 1=1 ";
            }
//            TTF
            else if (showAssignedToMe && showAssignedToOthers && !showNotAssigned) {
                sqlquery += "WHERE AssignedUserId != '' ";
            }
//            TFT
            else if (showAssignedToMe && !showAssignedToOthers && showNotAssigned) {
                sqlquery += "WHERE AssignedUserId = " + addSingleQuotes(currentUser) + " OR  AssignedUserId = '' " ;
            }
//            FTT
            else if (!showAssignedToMe && showAssignedToOthers && showNotAssigned) {
                sqlquery += "WHERE AssignedUserId != " + addSingleQuotes(currentUser) + " ";
            }
//            TFF
            else if (showAssignedToMe && !showAssignedToOthers && !showNotAssigned) {
                sqlquery += "WHERE AssignedUserId = " + addSingleQuotes(currentUser) + " ";
            }
//            FTF
            else if (!showAssignedToMe && showAssignedToOthers && !showNotAssigned) {
                sqlquery += "WHERE AssignedUserId != " + addSingleQuotes(currentUser) + " AND  AssignedUserId != '' ";
            }
//            FFT
            else if (!showAssignedToMe && !showAssignedToOthers && showNotAssigned) {
                sqlquery += "WHERE AssignedUserId = '' ";
            }
//            FFF
            else if (!showAssignedToMe && !showAssignedToOthers && !showNotAssigned) {
                sqlquery += "WHERE AssignedUserId = 'AJAX_IS_KAMPIOEN' ";
            }
            if (showSingleArticles) {
                sqlquery += " AND SingleArticleOrders != 0 ";
            }
            if (!showProcessedWait) {
                sqlquery += " AND NOT(IsProcessingOrParked) ";
            }
        }

        try {


            SupportSQLiteQuery query = new SimpleSQLiteQuery(sqlquery);
            l_PickorderEntityObl = new getFilteredPickordersAsyncTask(pickorderDao).execute(query).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return l_PickorderEntityObl;
    }
    private static class getFilteredPickordersAsyncTask extends AsyncTask<SupportSQLiteQuery, Void, LiveData<List<cPickorderEntity>>> {
        private iPickorderDao mAsyncTaskDao;

        getFilteredPickordersAsyncTask(iPickorderDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected LiveData<List<cPickorderEntity>> doInBackground(final SupportSQLiteQuery... params) {
//            return mAsyncTaskDao.getFilteredPickorders(params[0].useFilters,params[0].showProcessedWait,params[0].showSingleArticles,params[0].showAssignedToMe,params[0].showAssignedToOthers,params[0].showNotAssigned );
            return mAsyncTaskDao.getFilteredPickorders(params[0]);
        }
    }

    public List<cPickorderEntity> getLocalPickorders() {
        List<cPickorderEntity> l_PickorderEntityObl = null;
        try {
            l_PickorderEntityObl = new getLocalPickordersAsyncTask(pickorderDao).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return l_PickorderEntityObl;
    }

    private static class getLocalPickordersAsyncTask extends AsyncTask<Void, Void, List<cPickorderEntity>> {
        private iPickorderDao mAsyncTaskDao;

        getLocalPickordersAsyncTask(iPickorderDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected List<cPickorderEntity> doInBackground(final Void... params) {
            return mAsyncTaskDao.getLocalPickorders();
        }
    }

    public LiveData<List<cPickorderEntity>> getPickorders(final Boolean forcerefresh, final String usernameStr, final String branchStr, final Boolean inprogressBln, final String searchTextStr, final String mainTypeStr) {
        final MutableLiveData<List<cPickorderEntity>> data = new MutableLiveData<>();

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<cPickorderEntity> pickOrderEntities = new ArrayList<>();
                if (forcerefresh) {
                    try {

                        List<PropertyInfo> l_PropertyInfoObl = new ArrayList<>();

                        PropertyInfo l_PropertyInfo1Pin = new PropertyInfo();
                        l_PropertyInfo1Pin.name = cWebserviceDefinitions.WEBPROPERTY_USERNAMEDUNGLISH;
                        l_PropertyInfo1Pin.setValue(usernameStr);
                        l_PropertyInfoObl.add(l_PropertyInfo1Pin);

                        PropertyInfo l_PropertyInfo2Pin = new PropertyInfo();
                        l_PropertyInfo2Pin.name = cWebserviceDefinitions.WEBPROPERTY_BRANCH;
                        l_PropertyInfo2Pin.setValue(branchStr);
                        l_PropertyInfoObl.add(l_PropertyInfo2Pin);

                        PropertyInfo l_PropertyInfo3Pin = new PropertyInfo();
                        l_PropertyInfo3Pin.name = cWebserviceDefinitions.WEBPROPERTY_INPROGRESS;
                        l_PropertyInfo3Pin.setValue(inprogressBln);
                        l_PropertyInfoObl.add(l_PropertyInfo3Pin);

                        PropertyInfo l_PropertyInfo4Pin = new PropertyInfo();
                        l_PropertyInfo4Pin.name = cWebserviceDefinitions.WEBPROPERTY_SEARCHTEXT;
                        l_PropertyInfo4Pin.setValue(searchTextStr);
                        l_PropertyInfoObl.add(l_PropertyInfo4Pin);

                        PropertyInfo l_PropertyInfo5Pin = new PropertyInfo();
                        l_PropertyInfo5Pin.name = cWebserviceDefinitions.WEBPROPERTY_MAINTYPE;
                        l_PropertyInfo5Pin.setValue(mainTypeStr);
                        l_PropertyInfoObl.add(l_PropertyInfo5Pin);

                        webResult = new cWebresult().mGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_GETPICKORDERS, l_PropertyInfoObl);
                        List<JSONObject> myList = webResult.getResultDtt();
                        for (int i = 0; i < myList.size(); i++) {
                            JSONObject jsonObject;
                            jsonObject = myList.get(i);

                            cPickorderEntity pickorderEntity = new cPickorderEntity(jsonObject, inprogressBln);
                            insert(pickorderEntity);
                            pickOrderEntities.add(pickorderEntity);
                        }
                        data.postValue(pickOrderEntities);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    pickOrderEntities = getLocalPickorders();
                    data.postValue(pickOrderEntities);
                }
            } //run
        }).start(); //Thread
        return data;
    }

    public LiveData<List<cPickorderEntity>> getSortOrShiporders(final Boolean forcerefresh, final String usernameStr, final String branchStr, final Integer pickstepInt, final String searchTextStr, final String mainTypeStr) {
        final MutableLiveData<List<cPickorderEntity>> data = new MutableLiveData<>();

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<cPickorderEntity> pickOrderEntities = new ArrayList<>();
                if (forcerefresh) {
                    try {

                        List<PropertyInfo> l_PropertyInfoObl = new ArrayList<>();

                        PropertyInfo l_PropertyInfo1Pin = new PropertyInfo();
                        l_PropertyInfo1Pin.name = cWebserviceDefinitions.WEBPROPERTY_USERNAMEDUNGLISH;
                        l_PropertyInfo1Pin.setValue(usernameStr);
                        l_PropertyInfoObl.add(l_PropertyInfo1Pin);

                        PropertyInfo l_PropertyInfo2Pin = new PropertyInfo();
                        l_PropertyInfo2Pin.name = cWebserviceDefinitions.WEBPROPERTY_BRANCH;
                        l_PropertyInfo2Pin.setValue(branchStr);
                        l_PropertyInfoObl.add(l_PropertyInfo2Pin);

                        PropertyInfo l_PropertyInfo3Pin = new PropertyInfo();
                        l_PropertyInfo3Pin.name = cWebserviceDefinitions.WEBPROPERTY_PICKSTEP;
                        l_PropertyInfo3Pin.setValue(pickstepInt);
                        l_PropertyInfoObl.add(l_PropertyInfo3Pin);

                        PropertyInfo l_PropertyInfo4Pin = new PropertyInfo();
                        l_PropertyInfo4Pin.name = cWebserviceDefinitions.WEBPROPERTY_SEARCHTEXT;
                        l_PropertyInfo4Pin.setValue(searchTextStr);
                        l_PropertyInfoObl.add(l_PropertyInfo4Pin);

                        PropertyInfo l_PropertyInfo5Pin = new PropertyInfo();
                        l_PropertyInfo5Pin.name = cWebserviceDefinitions.WEBPROPERTY_MAINTYPE;
                        l_PropertyInfo5Pin.setValue(mainTypeStr);
                        l_PropertyInfoObl.add(l_PropertyInfo5Pin);

                        webResult = new cWebresult().mGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_GETPICKORDERSSEQUELSTEP, l_PropertyInfoObl);
                        List<JSONObject> myList = webResult.getResultDtt();
                        for (int i = 0; i < myList.size(); i++) {
                            JSONObject jsonObject;
                            jsonObject = myList.get(i);

                            cPickorderEntity pickorderEntity = new cPickorderEntity(jsonObject, false);
                            insert(pickorderEntity);
                            pickOrderEntities.add(pickorderEntity);
                        }
                        data.postValue(pickOrderEntities);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    pickOrderEntities = getLocalPickorders();
                    data.postValue(pickOrderEntities);
                }
            } //run
        }).start(); //Thread
        return data;
    }


    public List<cPickorderEntity> getAll() {
        List<cPickorderEntity> l_pickorderEntityObl = null;
        try {
            l_pickorderEntityObl = new getAllAsyncTask(pickorderDao).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return l_pickorderEntityObl;
    }
    private static class getAllAsyncTask extends AsyncTask<Void, Void, List<cPickorderEntity>> {
        private iPickorderDao mAsyncTaskDao;

        getAllAsyncTask(iPickorderDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected List<cPickorderEntity> doInBackground(final Void... params) {
            return mAsyncTaskDao.getAll();
        }
    }

    public void deleteAll () {
        new cPickorderRepository.deleteAllAsyncTask(pickorderDao).execute();
    }

    private static class deleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private iPickorderDao mAsyncTaskDao;

        deleteAllAsyncTask(iPickorderDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final Void... params) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }

    public void insert (cPickorderEntity pickorderEntity) {
        new insertAsyncTask(pickorderDao).execute(pickorderEntity);
    }
    private static class insertAsyncTask extends AsyncTask<cPickorderEntity, Void, Void> {
        private iPickorderDao mAsyncTaskDao;

        insertAsyncTask(iPickorderDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final cPickorderEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
    public cPickorderEntity getPickorderByOrderNumber(String ordernumber) {
        cPickorderEntity pickorderEntity = null;
        try {
            pickorderEntity = new getPickorderByOrderNumberAsyncTask(pickorderDao).execute(ordernumber).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return pickorderEntity;
    }
    private static class getPickorderByOrderNumberAsyncTask extends AsyncTask<String, Void, cPickorderEntity> {
        private iPickorderDao mAsyncTaskDao;
        getPickorderByOrderNumberAsyncTask (iPickorderDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected cPickorderEntity doInBackground(String... params) {
            return mAsyncTaskDao.getPickorderByOrderNumber(params[0]);
        }
    }


    private static class ProcessingOrParkedOrdersParams {
        String user;
        String branch;
        String maintype;

        ProcessingOrParkedOrdersParams(String pv_user, String pv_branch, String pv_maintype) {
            this.user = pv_user;
            this.branch = pv_branch;
            this.maintype = pv_maintype;
        }
    }

    public Boolean getProcessingOrParkedOrdersBln(String user, String branch, String maintype) {
        cWebresult l_webResult;
        Boolean l_resultBln;
        ProcessingOrParkedOrdersParams processingOrParkedOrdersParams = new ProcessingOrParkedOrdersParams(user, branch, maintype);
        try {
            l_webResult = new getGetProcessingOrParkedOrdersAsyncTask().execute(processingOrParkedOrdersParams).get();
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

    private static class getGetProcessingOrParkedOrdersAsyncTask extends AsyncTask<ProcessingOrParkedOrdersParams, Void, cWebresult> {
        @Override
        protected cWebresult doInBackground(ProcessingOrParkedOrdersParams... params) {
            cWebresult l_WebresultWrs = new cWebresult();
            try {
                List<PropertyInfo> l_PropertyInfoObl = new ArrayList<>();

                PropertyInfo l_PropertyInfo1Pin = new PropertyInfo();
                l_PropertyInfo1Pin.name = cWebserviceDefinitions.WEBPROPERTY_USERNAMEDUNGLISH;
                l_PropertyInfo1Pin.setValue(params[0].user);
                l_PropertyInfoObl.add(l_PropertyInfo1Pin);

                PropertyInfo l_PropertyInfo2Pin = new PropertyInfo();
                l_PropertyInfo2Pin.name = cWebserviceDefinitions.WEBPROPERTY_BRANCH;
                l_PropertyInfo2Pin.setValue(params[0].branch);
                l_PropertyInfoObl.add(l_PropertyInfo2Pin);

                PropertyInfo l_PropertyInfo3Pin = new PropertyInfo();
                l_PropertyInfo3Pin.name = cWebserviceDefinitions.WEBPROPERTY_INPROGRESS;
                l_PropertyInfo3Pin.setValue(params[0].maintype);
                l_PropertyInfoObl.add(l_PropertyInfo3Pin);

                l_WebresultWrs = new cWebresult().mGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_GETPROCESSINGORPARKEDORDERS, l_PropertyInfoObl);
            } catch (JSONException e) {
                l_WebresultWrs.setSuccessBln(false);
                l_WebresultWrs.setResultBln(false);
            }
            return l_WebresultWrs;
        }
    }
    public Boolean pickorderStepHandled(String user, String language, String branch, String orderNumber, String device, String workplace, String workflowStepcode, Integer workflowStep, String culture) {
        PickorderStepHandledParams pickorderStepHandledParams = new PickorderStepHandledParams(user, language, branch, orderNumber, device,workplace,workflowStepcode, workflowStep, culture);
        cWebresult l_webResult;
        Boolean l_resultBln;
        try {
            l_webResult = new getPickorderStepHandledAsyncTask().execute(pickorderStepHandledParams).get();
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

    private static class getPickorderStepHandledAsyncTask extends AsyncTask<PickorderStepHandledParams, Void, cWebresult> {
        @Override
        protected cWebresult doInBackground(PickorderStepHandledParams... params) {
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
                l_PropertyInfo3Pin.name = cWebserviceDefinitions.WEBPROPERTY_BRANCH;
                l_PropertyInfo3Pin.setValue(params[0].branch);
                l_PropertyInfoObl.add(l_PropertyInfo3Pin);

                PropertyInfo l_PropertyInfo4Pin = new PropertyInfo();
                l_PropertyInfo4Pin.name = cWebserviceDefinitions.WEBPROPERTY_ORDERNUMBER;
                l_PropertyInfo4Pin.setValue(params[0].ordernumber);
                l_PropertyInfoObl.add(l_PropertyInfo4Pin);

                PropertyInfo l_PropertyInfo5Pin = new PropertyInfo();
                l_PropertyInfo5Pin.name = cWebserviceDefinitions.WEBPROPERTY_SCANNER;
                l_PropertyInfo5Pin.setValue(params[0].device);
                l_PropertyInfoObl.add(l_PropertyInfo5Pin);

                PropertyInfo l_PropertyInfo6Pin = new PropertyInfo();
                l_PropertyInfo6Pin.name = cWebserviceDefinitions.WEBPROPERTY_WORKPLACE;
                l_PropertyInfo6Pin.setValue(params[0].workplace);
                l_PropertyInfoObl.add(l_PropertyInfo6Pin);

                PropertyInfo l_PropertyInfo7Pin = new PropertyInfo();
                l_PropertyInfo7Pin.name = cWebserviceDefinitions.WEBPROPERTY_WORKFLOWSTEPCODESTR;
                l_PropertyInfo7Pin.setValue(params[0].workflowstepcode);
                l_PropertyInfoObl.add(l_PropertyInfo7Pin);

                PropertyInfo l_PropertyInfo8Pin = new PropertyInfo();
                l_PropertyInfo8Pin.name = cWebserviceDefinitions.WEBPROPERTY_WORKFLOWSTEPINT;
                l_PropertyInfo8Pin.setValue(params[0].workflowstep);
                l_PropertyInfoObl.add(l_PropertyInfo8Pin);

                PropertyInfo l_PropertyInfo9Pin = new PropertyInfo();
                l_PropertyInfo9Pin.name = cWebserviceDefinitions.WEBPROPERTY_CULTURE;
                l_PropertyInfo9Pin.setValue(params[0].culture);
                l_PropertyInfoObl.add(l_PropertyInfo9Pin);

                l_WebresultWrs = new cWebresult().mGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_PICKORDERSTEPHANDLED, l_PropertyInfoObl);
            } catch (JSONException e) {
                l_WebresultWrs.setSuccessBln(false);
                l_WebresultWrs.setResultBln(false);
            }
            return l_WebresultWrs;
        }
    }

    private class PickorderSourceDocumentShippedParams {
        String user;
        String branch;
        String ordernumber;
        String sourceno;
        String culture;
        String shippingagent;
        String shippingservice;
        List<c_InterfaceShippingPackageIesp> shippingpackages;

        PickorderSourceDocumentShippedParams(String pv_user, String pv_branch, String pv_ordernumber, String pv_sourceno, String pv_culture, String pv_shippingagent, String pv_shippingservice, List<c_InterfaceShippingPackageIesp> pv_shippingpackages) {
            this.user = pv_user;
            this.branch = pv_branch;
            this.ordernumber = pv_ordernumber;
            this.sourceno = pv_sourceno;
            this.culture = pv_culture;
            this.shippingagent = pv_shippingagent;
            this.shippingservice = pv_shippingservice;
            this.shippingpackages = pv_shippingpackages;
        }
    }


    public Boolean pickorderSourceDocumentShipped(String user, String branch, String ordernumber, String sourceno, String culture, String shippingagent, String shippingservice, List<c_InterfaceShippingPackageIesp> packages) {
        PickorderSourceDocumentShippedParams pickorderSourceDocumentShippedParams = new PickorderSourceDocumentShippedParams(user, branch, ordernumber, sourceno, culture, shippingagent, shippingservice, packages);
        cWebresult l_webResult;
        Boolean l_resultBln;
        try {
            l_webResult = new pickorderSourceDocumentShippedAsyncTask().execute(pickorderSourceDocumentShippedParams).get();
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

    private static class pickorderSourceDocumentShippedAsyncTask extends AsyncTask<PickorderSourceDocumentShippedParams, Void, cWebresult> {
        @Override
        protected cWebresult doInBackground(PickorderSourceDocumentShippedParams... params) {
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
                l_PropertyInfo4Pin.name = cWebserviceDefinitions.WEBPROPERTY_SOURCENO;
                l_PropertyInfo4Pin.setValue(params[0].sourceno);
                l_PropertyInfoObl.add(l_PropertyInfo4Pin);

                PropertyInfo l_PropertyInfo5Pin = new PropertyInfo();
                l_PropertyInfo5Pin.name = cWebserviceDefinitions.WEBPROPERTY_CULTURE;
                l_PropertyInfo5Pin.setValue(params[0].culture);
                l_PropertyInfoObl.add(l_PropertyInfo5Pin);

                PropertyInfo l_PropertyInfo6Pin = new PropertyInfo();
                l_PropertyInfo6Pin.name = cWebserviceDefinitions.WEBPROPERTY_SHIPPINGAGENT;
                l_PropertyInfo6Pin.setValue(params[0].shippingagent);
                l_PropertyInfoObl.add(l_PropertyInfo6Pin);

                PropertyInfo l_PropertyInfo7Pin = new PropertyInfo();
                l_PropertyInfo7Pin.name = cWebserviceDefinitions.WEBPROPERTY_SHIPPINGSERVICE;
                l_PropertyInfo7Pin.setValue(params[0].shippingservice);
                l_PropertyInfoObl.add(l_PropertyInfo7Pin);

                PropertyInfo l_PropertyInfo8Pin = new PropertyInfo();
                l_PropertyInfo8Pin.name = cWebserviceDefinitions.WEBPROPERTY_SHIPPINGOPTIONS;
                l_PropertyInfo8Pin.setValue("");
                l_PropertyInfoObl.add(l_PropertyInfo8Pin);

                SoapObject shippingpackages = new SoapObject(cWebservice.WEBSERVICE_NAMESPACE, cWebserviceDefinitions.WEBPROPERTY_SHIPPINGPACKAGES);
                for (c_InterfaceShippingPackageIesp interfaceShippingPackageIesp: params[0].shippingpackages) {
                    SoapObject soapObject = new SoapObject(cWebservice.WEBSERVICE_NAMESPACE, cWebserviceDefinitions.WEBPROPERTY_INTERFACESHIPPINGPACKAGE);
                    soapObject.addProperty(cWebserviceDefinitions.WEBPROPERTY_INTERFACESHIPPINGPACKAGE_PACKAGE, interfaceShippingPackageIesp.getG_PackagetypeStr());
                    soapObject.addProperty(cWebserviceDefinitions.WEBPROPERTY_INTERFACESHIPPINGPACKAGE_SEQUENCENUMBER, interfaceShippingPackageIesp.getG_SequenceNumberInt());
                    soapObject.addProperty(cWebserviceDefinitions.WEBPROPERTY_INTERFACESHIPPINGPACKAGE_WEIGHTING, interfaceShippingPackageIesp.getG_WeightinGLng());
                    soapObject.addProperty(cWebserviceDefinitions.WEBPROPERTY_INTERFACESHIPPINGPACKAGE_ITEMCOUNT, interfaceShippingPackageIesp.getG_ItemcountDbl());
                    soapObject.addProperty(cWebserviceDefinitions.WEBPROPERTY_INTERFACESHIPPINGPACKAGE_CONTAINERTYPE, interfaceShippingPackageIesp.getG_ContainersoortStr());
                    soapObject.addProperty(cWebserviceDefinitions.WEBPROPERTY_INTERFACESHIPPINGPACKAGE_CONTAINER, interfaceShippingPackageIesp.getG_ContainerStr());
                    shippingpackages.addSoapObject(soapObject);
                }

                PropertyInfo l_PropertyInfo9Pin = new PropertyInfo();
                l_PropertyInfo9Pin.name = cWebserviceDefinitions.WEBPROPERTY_SHIPPINGPACKAGES;
                l_PropertyInfo9Pin.setValue(shippingpackages);
//                l_PropertyInfo9Pin.setType("ArrayOfC_InterfaceShippingPackageIesp");
//                l_PropertyInfo9Pin.setNamespace(cWebservice.WEBSERVICE_NAMESPACE);
                l_PropertyInfoObl.add(l_PropertyInfo9Pin);

                l_WebresultWrs = new cWebresult().mGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_PICKORDERSOURCEDOCUMENTSHIPPED, l_PropertyInfoObl);
            } catch (JSONException e) {
                l_WebresultWrs.setSuccessBln(false);
                l_WebresultWrs.setResultBln(false);
            }
            return l_WebresultWrs;
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


    public Boolean pickorderUpdateWorkplace(String user, String branch, String ordernumber, String workplace) {
        PickorderUpdateWorkplaceParams pickorderUpdateWorkplaceParams = new PickorderUpdateWorkplaceParams(user, branch, ordernumber, workplace);
        cWebresult l_webResult;
        Boolean l_resultBln;
        try {
            l_webResult = new pickorderUpdateWorkplaceAsyncTask().execute(pickorderUpdateWorkplaceParams).get();
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

    private static class pickorderUpdateWorkplaceAsyncTask extends AsyncTask<PickorderUpdateWorkplaceParams, Void, cWebresult> {
        @Override
        protected cWebresult doInBackground(PickorderUpdateWorkplaceParams... params) {
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
                l_PropertyInfo4Pin.name = cWebserviceDefinitions.WEBPROPERTY_WORKPLACE;
                l_PropertyInfo4Pin.setValue(params[0].workplace);
                l_PropertyInfoObl.add(l_PropertyInfo4Pin);

                l_WebresultWrs = new cWebresult().mGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_PICKORDERUPDATEWORKPLACE, l_PropertyInfoObl);

            } catch (JSONException e) {
                l_WebresultWrs.setSuccessBln(false);
                l_WebresultWrs.setResultBln(false);
            }
            return l_WebresultWrs;
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



    public int updatePickorderWorkplaceLocal(String pv_ordernumber, String pv_workplace) {
        Integer integerValue = 0;
        UpdatePickorderWorkplaceLocalParams updatePickorderWorkplaceLocalParams = new UpdatePickorderWorkplaceLocalParams(pv_ordernumber, pv_workplace);
        try {
            integerValue = new updatePickorderWorkplaceLocalAsyncTask(pickorderDao).execute(updatePickorderWorkplaceLocalParams).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return integerValue;
    }
    private static class updatePickorderWorkplaceLocalAsyncTask extends AsyncTask<UpdatePickorderWorkplaceLocalParams, Void, Integer> {
        private iPickorderDao mAsyncTaskDao;
        updatePickorderWorkplaceLocalAsyncTask(iPickorderDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected Integer doInBackground(UpdatePickorderWorkplaceLocalParams... params) {
            return mAsyncTaskDao.updatePickorderWorkplace(params[0].ordernumber, params[0].workplace);
        }
    }
}
