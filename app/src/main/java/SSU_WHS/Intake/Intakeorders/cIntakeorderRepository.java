package SSU_WHS.Intake.Intakeorders;

import android.app.Application;
import android.os.AsyncTask;

import androidx.sqlite.db.SimpleSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteQuery;

import org.json.JSONException;
import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import ICS.Utils.cDeviceInfo;
import ICS.Utils.cSharedPreferences;
import SSU_WHS.Basics.Users.cUser;
import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.General.acScanSuiteDatabase;
import SSU_WHS.General.cDatabase;
import SSU_WHS.Intake.IntakeorderMATLines.cIntakeorderMATLineEntity;
import SSU_WHS.Intake.IntakeorderMATLines.iIntakeorderMATLineDao;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebserviceDefinitions;

import static ICS.Utils.cText.pAddSingleQuotesStr;

public class cIntakeorderRepository {

    //Region Public Properties
    public iIntakeorderDao intakeorderDao;
    public iIntakeorderMATLineDao intakeorderMATLineDao;

    //End Region Public Properties

    //Region Private Properties
    private acScanSuiteDatabase db;

    private class IntakeStepHandledParams {
        String userStr;
        String languageStr;
        String branchStr;
        String orderNumberStr;
        String deviceStr;
        String workflowStepcodeStr;

        String cultureStr;

        IntakeStepHandledParams(String pvUserStr, String pvLanguageStr, String pvBranchStr, String pvOrderNumberStr, String pvDeviceStr,String pvWorkflowStepCodeStr, String pvCultureStr) {
            this.userStr = pvUserStr;
            this.languageStr = pvLanguageStr;
            this.branchStr = pvBranchStr;
            this.orderNumberStr = pvOrderNumberStr;
            this.deviceStr = pvDeviceStr;
            this.workflowStepcodeStr = pvWorkflowStepCodeStr;
            this.cultureStr = pvCultureStr;
        }
    }


    //Region Constructor
    cIntakeorderRepository(Application pvApplication) {
        this.db = acScanSuiteDatabase.pGetDatabase(pvApplication);
        this.intakeorderDao = db.intakeorderDao();
        this.intakeorderMATLineDao = db.intakeorderMATLineDao();
    }
    //End Region Constructor

    //Region Public Methods

    public cWebresult pGetIntakeordersFromWebserviceWrs(String pvSearchTextStr) {

        List<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();

        try {
            webResultWrs = new mGetIntakeordersFromWebserviceAsyncTask().execute(pvSearchTextStr).get();
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

    public List<cIntakeorderEntity> pGetIntakeordersFromDatabaseObl() {
        List<cIntakeorderEntity> ResultObl = null;
        try {
            ResultObl = new mGetIntakeordersFromDatabaseAsyncTask(intakeorderDao).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return ResultObl;
    }

    public List<cIntakeorderEntity> pGetIntakeordersFromDatabaseWithFilterObl(String pvCurrentUserStr, Boolean pvUseFiltersBln) {

        List<cIntakeorderEntity> ResultObl = null;
        String SQLStatementStr;

        SQLStatementStr = "SELECT * FROM " + cDatabase.TABLENAME_INTAKEORDER;
        if (pvUseFiltersBln) {
//            TTT
            if (cSharedPreferences.showAssignedToMeBln() && cSharedPreferences.showAssignedToOthersBln() && cSharedPreferences.showNotAssignedBln()) {
                SQLStatementStr += " WHERE 1=1 ";
            }
//            TTF
            else if (cSharedPreferences.showAssignedToMeBln() && cSharedPreferences.showAssignedToOthersBln() && !cSharedPreferences.showNotAssignedBln()) {
                SQLStatementStr += " WHERE AssignedUserId != '' ";
            }
//            TFT
            else if (cSharedPreferences.showAssignedToMeBln() && !cSharedPreferences.showAssignedToOthersBln() && cSharedPreferences.showNotAssignedBln()) {
                SQLStatementStr += " WHERE AssignedUserId = " + pAddSingleQuotesStr(pvCurrentUserStr.toUpperCase()) + " OR  AssignedUserId = '' " ;
                SQLStatementStr += " OR CurrentUserId = " + pAddSingleQuotesStr(pvCurrentUserStr.toUpperCase()) + " OR  CurrentUserId = '' " ;
            }
//            FTT
            else if (!cSharedPreferences.showAssignedToMeBln() && cSharedPreferences.showAssignedToOthersBln() && cSharedPreferences.showNotAssignedBln()) {
                SQLStatementStr += " WHERE AssignedUserId != " + pAddSingleQuotesStr(pvCurrentUserStr.toUpperCase()) + " ";
            }
//            TFF
            else if (cSharedPreferences.showAssignedToMeBln() && !cSharedPreferences.showAssignedToOthersBln() && !cSharedPreferences.showNotAssignedBln()) {
                SQLStatementStr += " WHERE AssignedUserId = " + pAddSingleQuotesStr(pvCurrentUserStr.toUpperCase()) + " ";
                SQLStatementStr += " OR CurrentUserId = " + pAddSingleQuotesStr(pvCurrentUserStr.toUpperCase()) + " " ;
            }
//            FTF
            else if (!cSharedPreferences.showAssignedToMeBln() && cSharedPreferences.showAssignedToOthersBln() && !cSharedPreferences.showNotAssignedBln()) {
                SQLStatementStr += " WHERE AssignedUserId != " + pAddSingleQuotesStr(pvCurrentUserStr.toUpperCase()) + " AND  AssignedUserId != '' ";
            }
//            FFT
            else if (!cSharedPreferences.showAssignedToMeBln() && !cSharedPreferences.showAssignedToOthersBln() && cSharedPreferences.showNotAssignedBln()) {
                SQLStatementStr += " WHERE AssignedUserId = '' AND CurrentUserId = ''";
            }
//            FFF
            else if (!cSharedPreferences.showAssignedToMeBln() && !cSharedPreferences.showAssignedToOthersBln() && !cSharedPreferences.showNotAssignedBln()) {
                SQLStatementStr += " WHERE AssignedUserId = 'HELEMAALNIEMAND' ";
            }
        }

        try {
            SupportSQLiteQuery query = new SimpleSQLiteQuery(SQLStatementStr);
            ResultObl = new cIntakeorderRepository.mGetIntakeordersFromDatabaseWithFilterAsyncTask(intakeorderDao).execute(query).get();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return ResultObl;
    }

    public void pInsertInDatabase(cIntakeorderEntity intakeorderEntity) {
        new mInsertAsyncTask(intakeorderDao).execute(intakeorderEntity);
    }

    public void pTruncateTable() {
        new mDeleteAllAsyncTask(intakeorderDao).execute();
    }

    public cWebresult pGetMATLinesFromWebserviceWrs() {

        List<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();

        try {
            webResultWrs = new mGetMATLinesFromWebserviceAsyncTask().execute().get();
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

    public List<cIntakeorderMATLineEntity> pGetAllMATLinesFromDatabaseObl() {

        List<cIntakeorderMATLineEntity> resultObl = null;
        try {
            resultObl = new mGetAllLinesFromDatabaseAsyncTask(intakeorderMATLineDao).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return resultObl;
    }

    public List<cIntakeorderMATLineEntity> pGetMATLinesToSendFromDatabaseObl() {
        List<cIntakeorderMATLineEntity> resultObl = null;
        try {
            resultObl = new mGetMATLinesToSendFromDatabaseAsyncTask(intakeorderMATLineDao).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return resultObl;
    }

    public List<cIntakeorderMATLineEntity> pGetMATLinesNotHandledFromDatabaseObl() {
        List<cIntakeorderMATLineEntity> resultObl = null;
        try {
            resultObl = new mGetMATLinesNotHandledFromDatabaseAsyncTask(intakeorderMATLineDao).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return resultObl;
    }

    public List<cIntakeorderMATLineEntity> pGetMATLinesBusyFromDatabaseObl() {
        List<cIntakeorderMATLineEntity> resultObl = null;
        try {
            resultObl = new mGetBusyMATLinesAsyncTask(intakeorderMATLineDao).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return resultObl;
    }

    public List<cIntakeorderMATLineEntity> pGetMATLinesHandledFromDatabaseObl() {
        List<cIntakeorderMATLineEntity> resultObl = null;
        try {
            resultObl = new mGetMATLinesHandledFromDatabaseAsyncTask(intakeorderMATLineDao).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return resultObl;
    }

    public cWebresult pGetBarcodesFromWebserviceWrs() {

        List<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();

        try {
            webResultWrs = new mGetMATBarcodesFromWebserviceAsyncTask().execute().get();
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

    public cWebresult pGetMATLineBarcodesFromWebserviceWrs() {

        List<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();

        try {
            webResultWrs = new mGetMATLineBarcodesFromWebserviceAsyncTask().execute().get();
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

    public cWebresult pHandledViaWebserviceBln() {

        cWebresult webResult;

        IntakeStepHandledParams intakeStepHandledParams;
        intakeStepHandledParams = new IntakeStepHandledParams(cUser.currentUser.getNameStr(), "", cUser.currentUser.currentBranch.getBranchStr(), cIntakeorder.currentIntakeOrder.getOrderNumberStr(), cDeviceInfo.getSerialnumberStr() , cWarehouseorder.StepCodeEnu.Receive_Store.toString(), "");

        try {

            webResult = new mIntakeorderStepHandledAsyncTask() .execute(intakeStepHandledParams).get();
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

    //Pick quantityDbl's
    public Double pQuantityNotHandledDbl() {
        Double resultDbl = 0.0;
        try {
            resultDbl = new mGetQuantityNotHandledAsyncTask(intakeorderDao).execute().get();
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
            resultDbl = new mGetQuantityHandledAsyncTask(intakeorderDao).execute().get();
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
            resultDbl = new mGetTotalQuanitityAsyncTask(intakeorderDao).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return resultDbl;
    }


    //End Region Public Methods

    //Region Private Methods

    private static class mGetIntakeordersFromWebserviceAsyncTask extends AsyncTask<String, Void, cWebresult> {
        @Override
        protected cWebresult doInBackground(final String... params) {
            cWebresult WebresultWrs = new cWebresult();

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
            l_PropertyInfo3Pin.name = cWebserviceDefinitions.WEBPROPERTY_SEARCHTEXT;
            l_PropertyInfo3Pin.setValue(params[0]);
            l_PropertyInfoObl.add(l_PropertyInfo3Pin);

            PropertyInfo l_PropertyInfo4Pin = new PropertyInfo();
            l_PropertyInfo4Pin.name = cWebserviceDefinitions.WEBPROPERTY_MAINTYPE;
            l_PropertyInfo4Pin.setValue("");
            l_PropertyInfoObl.add(l_PropertyInfo4Pin);

            try {
                WebresultWrs = new cWebresult().pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_GETINTAKEORDERS, l_PropertyInfoObl);
            } catch (JSONException e) {
                WebresultWrs.setResultBln(false);
                WebresultWrs.setSuccessBln(false);
                e.printStackTrace();
            }

            return WebresultWrs;
        }
    }

    private static class mGetIntakeordersFromDatabaseAsyncTask extends AsyncTask<Void, Void, List<cIntakeorderEntity>> {
        private iIntakeorderDao mAsyncTaskDao;

        mGetIntakeordersFromDatabaseAsyncTask(iIntakeorderDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected List<cIntakeorderEntity> doInBackground(final Void... params) {
            return mAsyncTaskDao.getIntakeordersFromDatabase();
        }
    }

    private static class mGetIntakeordersFromDatabaseWithFilterAsyncTask extends AsyncTask<SupportSQLiteQuery, Void, List<cIntakeorderEntity>> {
        private iIntakeorderDao mAsyncTaskDao;

        mGetIntakeordersFromDatabaseWithFilterAsyncTask(iIntakeorderDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected List<cIntakeorderEntity> doInBackground(final SupportSQLiteQuery... params) {
            return mAsyncTaskDao.getFilteredIntakeOrders(params[0]);
        }
    }

    private static class mInsertAsyncTask extends AsyncTask<cIntakeorderEntity, Void, Void> {
        private iIntakeorderDao mAsyncTaskDao;

        mInsertAsyncTask(iIntakeorderDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final cIntakeorderEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class mDeleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private iIntakeorderDao mAsyncTaskDao;

        mDeleteAllAsyncTask(iIntakeorderDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final Void... params) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }


    private static class mGetMATLinesFromWebserviceAsyncTask extends AsyncTask<String, Void, cWebresult> {
        @Override
        protected cWebresult doInBackground(final String... params) {
            cWebresult WebresultWrs = new cWebresult();

            List<PropertyInfo> l_PropertyInfoObl = new ArrayList<>();

            PropertyInfo l_PropertyInfo1Pin = new PropertyInfo();
            l_PropertyInfo1Pin.name = cWebserviceDefinitions.WEBPROPERTY_LOCATION_NL;
            l_PropertyInfo1Pin.setValue(cUser.currentUser.currentBranch.getBranchStr());
            l_PropertyInfoObl.add(l_PropertyInfo1Pin);

            PropertyInfo l_PropertyInfo2Pin = new PropertyInfo();
            l_PropertyInfo2Pin.name = cWebserviceDefinitions.WEBPROPERTY_ORDERNUMBER;
            l_PropertyInfo2Pin.setValue(cIntakeorder.currentIntakeOrder.getOrderNumberStr());
            l_PropertyInfoObl.add(l_PropertyInfo2Pin);

            try {
                WebresultWrs = new cWebresult().pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_GETINTAKEORDERMATLINES, l_PropertyInfoObl);
            } catch (JSONException e) {
                WebresultWrs.setResultBln(false);
                WebresultWrs.setSuccessBln(false);
                e.printStackTrace();
            }

            return WebresultWrs;
        }
    }

    private static class mGetAllLinesFromDatabaseAsyncTask extends AsyncTask<Void, Void, List<cIntakeorderMATLineEntity>> {
        private iIntakeorderMATLineDao mAsyncTaskDao;

        mGetAllLinesFromDatabaseAsyncTask(iIntakeorderMATLineDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected List<cIntakeorderMATLineEntity> doInBackground(final Void... params) {
            return mAsyncTaskDao.getAll();
        }
    }

    private static class mGetMATLinesNotHandledFromDatabaseAsyncTask extends AsyncTask<Void, Void, List<cIntakeorderMATLineEntity>> {
        private iIntakeorderMATLineDao mAsyncTaskDao;

        mGetMATLinesNotHandledFromDatabaseAsyncTask(iIntakeorderMATLineDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected List<cIntakeorderMATLineEntity> doInBackground(final Void... params) {
            return mAsyncTaskDao.getNotHandledIntakeorderMATLineEntities();
        }
    }

    private static class mGetBusyMATLinesAsyncTask extends AsyncTask<Void, Void, List<cIntakeorderMATLineEntity>> {
        private iIntakeorderMATLineDao mAsyncTaskDao;

        mGetBusyMATLinesAsyncTask(iIntakeorderMATLineDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected List<cIntakeorderMATLineEntity> doInBackground(final Void... params) {
            return mAsyncTaskDao.getBusyIntakeorderMATLineEntities();
        }
    }

    private static class mGetMATLinesHandledFromDatabaseAsyncTask extends AsyncTask<Void, Void, List<cIntakeorderMATLineEntity>> {
        private iIntakeorderMATLineDao mAsyncTaskDao;

        mGetMATLinesHandledFromDatabaseAsyncTask(iIntakeorderMATLineDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected List<cIntakeorderMATLineEntity> doInBackground(final Void... params) {
            return mAsyncTaskDao.getHandledIntakeorderMATLineEntities();
        }
    }

    private static class mGetMATLinesToSendFromDatabaseAsyncTask extends AsyncTask<Void, Void, List<cIntakeorderMATLineEntity>> {
        private iIntakeorderMATLineDao mAsyncTaskDao;

        mGetMATLinesToSendFromDatabaseAsyncTask(iIntakeorderMATLineDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected List<cIntakeorderMATLineEntity> doInBackground(final Void... params) {
            return mAsyncTaskDao.getIntakeorderMATLineEntitiesToSend();
        }
    }


    private static class mGetMATLineBarcodesFromWebserviceAsyncTask extends AsyncTask<String, Void, cWebresult> {
        @Override
        protected cWebresult doInBackground(final String... params) {
            cWebresult WebresultWrs = new cWebresult();

            List<PropertyInfo> l_PropertyInfoObl = new ArrayList<>();

            PropertyInfo l_PropertyInfo1Pin = new PropertyInfo();
            l_PropertyInfo1Pin.name = cWebserviceDefinitions.WEBPROPERTY_LOCATION_NL;
            l_PropertyInfo1Pin.setValue(cUser.currentUser.currentBranch.getBranchStr());
            l_PropertyInfoObl.add(l_PropertyInfo1Pin);

            PropertyInfo l_PropertyInfo2Pin = new PropertyInfo();
            l_PropertyInfo2Pin.name = cWebserviceDefinitions.WEBPROPERTY_ORDERNUMBER;
            l_PropertyInfo2Pin.setValue(cIntakeorder.currentIntakeOrder.getOrderNumberStr());
            l_PropertyInfoObl.add(l_PropertyInfo2Pin);

            try {
                WebresultWrs = new cWebresult().pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_GETINTAKEORDERMATLINEBARCODES, l_PropertyInfoObl);
            } catch (JSONException e) {
                WebresultWrs.setResultBln(false);
                WebresultWrs.setSuccessBln(false);
                e.printStackTrace();
            }

            return WebresultWrs;
        }
    }

    private static class mGetMATBarcodesFromWebserviceAsyncTask extends AsyncTask<String, Void, cWebresult> {
        @Override
        protected cWebresult doInBackground(final String... params) {
            cWebresult WebresultWrs = new cWebresult();

            List<PropertyInfo> l_PropertyInfoObl = new ArrayList<>();

            PropertyInfo l_PropertyInfo1Pin = new PropertyInfo();
            l_PropertyInfo1Pin.name = cWebserviceDefinitions.WEBPROPERTY_LOCATION_NL;
            l_PropertyInfo1Pin.setValue(cUser.currentUser.currentBranch.getBranchStr());
            l_PropertyInfoObl.add(l_PropertyInfo1Pin);

            PropertyInfo l_PropertyInfo2Pin = new PropertyInfo();
            l_PropertyInfo2Pin.name = cWebserviceDefinitions.WEBPROPERTY_ORDERNUMBER;
            l_PropertyInfo2Pin.setValue(cIntakeorder.currentIntakeOrder.getOrderNumberStr());
            l_PropertyInfoObl.add(l_PropertyInfo2Pin);

            try {
                WebresultWrs = new cWebresult().pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_GETINTAKEARCODES, l_PropertyInfoObl);
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
            l_PropertyInfo2Pin.setValue(cIntakeorder.currentIntakeOrder.getOrderNumberStr());
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

    private static class mGetQuantityNotHandledAsyncTask extends AsyncTask<Void, Void, Double> {
        private iIntakeorderDao mAsyncTaskDao;
        mGetQuantityNotHandledAsyncTask(iIntakeorderDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected Double doInBackground(Void... params) {
            return mAsyncTaskDao.getQuantityNotHandledDbl();
        }
    }

    private static class mGetQuantityHandledAsyncTask extends AsyncTask<Void, Void, Double> {
        private iIntakeorderDao mAsyncTaskDao;
        mGetQuantityHandledAsyncTask(iIntakeorderDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected Double doInBackground(Void... params) {
            return mAsyncTaskDao.getNumberHandledDbl();
        }
    }

    private static class mGetTotalQuanitityAsyncTask extends AsyncTask<Void, Void, Double> {
        private iIntakeorderDao mAsyncTaskDao;

        mGetTotalQuanitityAsyncTask(iIntakeorderDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected Double doInBackground(Void... params) {
            return mAsyncTaskDao.getTotalQuantityDbl();
        }
    }


    private static class mIntakeorderStepHandledAsyncTask extends AsyncTask<IntakeStepHandledParams, Void, cWebresult> {
        @Override
        protected cWebresult doInBackground(IntakeStepHandledParams... params) {
            cWebresult webresult = new cWebresult();
            try {
                List<PropertyInfo> l_PropertyInfoObl = new ArrayList<>();

                PropertyInfo l_PropertyInfo1Pin = new PropertyInfo();
                l_PropertyInfo1Pin.name = cWebserviceDefinitions.WEBPROPERTY_USERNAMEDUTCH;
                l_PropertyInfo1Pin.setValue(params[0].userStr);
                l_PropertyInfoObl.add(l_PropertyInfo1Pin);

                PropertyInfo l_PropertyInfo2Pin = new PropertyInfo();
                l_PropertyInfo2Pin.name = cWebserviceDefinitions.WEBPROPERTY_LOCATION_NL;
                l_PropertyInfo2Pin.setValue(params[0].branchStr);
                l_PropertyInfoObl.add(l_PropertyInfo2Pin);

                PropertyInfo l_PropertyInfo3Pin = new PropertyInfo();
                l_PropertyInfo3Pin.name = cWebserviceDefinitions.WEBPROPERTY_ORDERNUMBER;
                l_PropertyInfo3Pin.setValue(params[0].orderNumberStr);
                l_PropertyInfoObl.add(l_PropertyInfo3Pin);

                PropertyInfo l_PropertyInfo4Pin = new PropertyInfo();
                l_PropertyInfo4Pin.name = cWebserviceDefinitions.WEBPROPERTY_SCANNER;
                l_PropertyInfo4Pin.setValue(params[0].deviceStr);
                l_PropertyInfoObl.add(l_PropertyInfo4Pin);

                PropertyInfo l_PropertyInfo5Pin = new PropertyInfo();
                l_PropertyInfo5Pin.name = cWebserviceDefinitions.WEBPROPERTY_WORKFLOWSTEPCODESTR;
                l_PropertyInfo5Pin.setValue(params[0].workflowStepcodeStr);
                l_PropertyInfoObl.add(l_PropertyInfo5Pin);

                PropertyInfo l_PropertyInfo6Pin = new PropertyInfo();
                l_PropertyInfo6Pin.name = cWebserviceDefinitions.WEBPROPERTY_CULTURE;
                l_PropertyInfo6Pin.setValue(params[0].cultureStr);
                l_PropertyInfoObl.add(l_PropertyInfo6Pin);

                webresult = new cWebresult().pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_INTAKEHANLED, l_PropertyInfoObl);
            } catch (JSONException e) {
                webresult.setSuccessBln(false);
                webresult.setResultBln(false);
            }
            return webresult;
        }
    }


    //End Region Private Methods








}
