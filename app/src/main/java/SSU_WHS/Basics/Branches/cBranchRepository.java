package SSU_WHS.Basics.Branches;

import android.app.Application;
import android.os.AsyncTask;

import org.json.JSONException;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import SSU_WHS.Basics.Users.cUser;
import SSU_WHS.General.acScanSuiteDatabase;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebservice;
import SSU_WHS.Webservice.cWebserviceDefinitions;

public class cBranchRepository {

    //Region Public Properties
    private iBranchDao branchDao;
    //End Region Public Properties

    //Region Private Properties

    //End Region Private Properties

    //Region Constructor
    cBranchRepository(Application pvApplication) {
         acScanSuiteDatabase db =  acScanSuiteDatabase.pGetDatabase(pvApplication);
        this.branchDao = db.branchDao();
    }
    //End Region Constructor

    //Region Public Methods
    public cWebresult pGetUserBranchesFromWebserviceWrs() {

        List<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();

        try {
            webResultWrs = new mUserBranchesFromWebserviceGetAsyncTask().execute().get();
        } catch (ExecutionException | InterruptedException e) {
            webResultWrs.setResultBln(false);
            webResultWrs.setSuccessBln(false);
            resultObl.add(e.getLocalizedMessage());
            webResultWrs.setResultObl(resultObl);
            e.printStackTrace();
        }
        return webResultWrs;
    }

    public cWebresult pGetBranchesFromWebserviceWrs() {

        List<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();

        try {
            webResultWrs = new mBranchesFromWebserviceGetAsyncTask().execute().get();
        } catch (ExecutionException | InterruptedException e) {
            webResultWrs.setResultBln(false);
            webResultWrs.setSuccessBln(false);
            resultObl.add(e.getLocalizedMessage());
            webResultWrs.setResultObl(resultObl);
            e.printStackTrace();
        }
        return webResultWrs;
    }

    public cWebresult pGetReceiveBinsFromWebserviceWrs() {

        List<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();

        try {
            webResultWrs = new mReceiveBinsFromWebserviceGetAsyncTask().execute().get();
        } catch (ExecutionException | InterruptedException e) {
            webResultWrs.setResultBln(false);
            webResultWrs.setSuccessBln(false);
            resultObl.add(e.getLocalizedMessage());
            webResultWrs.setResultObl(resultObl);
            e.printStackTrace();
        }
        return webResultWrs;
    }

    public cWebresult pGetShipBinsFromWebserviceWrs() {

        List<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();

        try {
            webResultWrs = new mShipBinsFromWebserviceGetAsyncTask().execute().get();
        } catch (ExecutionException | InterruptedException e) {
            webResultWrs.setResultBln(false);
            webResultWrs.setSuccessBln(false);
            resultObl.add(e.getLocalizedMessage());
            webResultWrs.setResultObl(resultObl);
            e.printStackTrace();
        }
        return webResultWrs;
    }

    public cWebresult pGetBinFromWebserviceWrs(String pvBinCodeStr) {

        List<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();

        try {
            webResultWrs = new mBinFromWebserviceGetAsyncTask().execute(pvBinCodeStr).get();
        } catch (ExecutionException | InterruptedException e) {
            webResultWrs.setResultBln(false);
            webResultWrs.setSuccessBln(false);
            resultObl.add(e.getLocalizedMessage());
            webResultWrs.setResultObl(resultObl);
            e.printStackTrace();
        }
        return webResultWrs;
    }

    public cWebresult pGetReasonFromWebserviceWrs() {

        List<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();

        try {
            webResultWrs = new mReasonsFromWebserviceGetAsyncTask().execute().get();
        } catch (ExecutionException | InterruptedException e) {
            webResultWrs.setResultBln(false);
            webResultWrs.setSuccessBln(false);
            resultObl.add(e.getLocalizedMessage());
            webResultWrs.setResultObl(resultObl);
            e.printStackTrace();
        }
        return webResultWrs;
    }
    public cWebresult pGetAuthorizedStockOwnerFromWebserviceWrs() {

        List<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();

        try {
            webResultWrs = new mAuthorizedStockOwnerFromWebserviceGetAsyncTask().execute().get();
        } catch (ExecutionException | InterruptedException e) {
            webResultWrs.setResultBln(false);
            webResultWrs.setSuccessBln(false);
            resultObl.add(e.getLocalizedMessage());
            webResultWrs.setResultObl(resultObl);
            e.printStackTrace();
        }
        return webResultWrs;
    }


    public void pInsert(cBranchEntity branchEntity) {
        new mInsertAsyncTask(branchDao).execute(branchEntity);
    }

    public void pDeleteAll() {

        new mDeleteAllAsyncTask(branchDao).execute();
    }

    //End Region Public Methods

    //Region Private Methods

    private static class mDeleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private iBranchDao mAsyncTaskDao;

        mDeleteAllAsyncTask(iBranchDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final Void... params) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }

    private static class mInsertAsyncTask extends AsyncTask<cBranchEntity, Void, Void> {
        private iBranchDao mAsyncTaskDao;

        mInsertAsyncTask(iBranchDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final cBranchEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

     private static class mBranchesFromWebserviceGetAsyncTask extends AsyncTask<Void, Void, cWebresult> {
        @Override
        protected cWebresult doInBackground(final Void... params) {
            cWebresult WebresultWrs = new cWebresult();

            try {
                new cWebresult();
                WebresultWrs = cWebresult.pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_GETBRANCHES, null);
            } catch (JSONException e) {
                WebresultWrs.setResultBln(false);
                WebresultWrs.setSuccessBln(false);
                e.printStackTrace();
            }

            return WebresultWrs;
        }
    }

    private static class mReceiveBinsFromWebserviceGetAsyncTask extends AsyncTask<Void, Void, cWebresult> {
        @Override
        protected cWebresult doInBackground(final Void... params) {
            cWebresult WebresultWrs = new cWebresult();

            List<PropertyInfo> l_PropertyInfoObl = new ArrayList<>();

            PropertyInfo l_PropertyInfo1Pin = new PropertyInfo();
            l_PropertyInfo1Pin.name = cWebserviceDefinitions.WEBPROPERTY_LOCATION_NL;
            l_PropertyInfo1Pin.setValue(cUser.currentUser.currentBranch.getBranchStr());
            l_PropertyInfoObl.add(l_PropertyInfo1Pin);

            try {
                new cWebresult();
                WebresultWrs = cWebresult.pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_GETRECEIVEBINS, l_PropertyInfoObl);
            } catch (JSONException e) {
                WebresultWrs.setResultBln(false);
                WebresultWrs.setSuccessBln(false);
                e.printStackTrace();
            }

            return WebresultWrs;
        }
    }

    private static class mShipBinsFromWebserviceGetAsyncTask extends AsyncTask<Void, Void, cWebresult> {
        @Override
        protected cWebresult doInBackground(final Void... params) {
            cWebresult WebresultWrs = new cWebresult();

            List<PropertyInfo> l_PropertyInfoObl = new ArrayList<>();

            PropertyInfo l_PropertyInfo1Pin = new PropertyInfo();
            l_PropertyInfo1Pin.name = cWebserviceDefinitions.WEBPROPERTY_LOCATION_NL;
            l_PropertyInfo1Pin.setValue(cUser.currentUser.currentBranch.getBranchStr());
            l_PropertyInfoObl.add(l_PropertyInfo1Pin);

            try {
                new cWebresult();
                WebresultWrs = cWebresult.pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_GETSHIPBINS, l_PropertyInfoObl);
            } catch (JSONException e) {
                WebresultWrs.setResultBln(false);
                WebresultWrs.setSuccessBln(false);
                e.printStackTrace();
            }

            return WebresultWrs;
        }
    }


    private static class mUserBranchesFromWebserviceGetAsyncTask extends AsyncTask<Void, Void, cWebresult> {
        @Override
        protected cWebresult doInBackground(final Void... params) {
            cWebresult WebresultWrs = new cWebresult();

            List<PropertyInfo> l_PropertyInfoObl = new ArrayList<>();

            PropertyInfo l_PropertyInfo1Pin = new PropertyInfo();
            l_PropertyInfo1Pin.name = cWebserviceDefinitions.WEBPROPERTY_USER;
            l_PropertyInfo1Pin.setValue(cUser.currentUser.getUsernameStr());
            l_PropertyInfoObl.add(l_PropertyInfo1Pin);

            try {
                new cWebresult();
                WebresultWrs = cWebresult.pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_GETBRANCHESFORUSER, l_PropertyInfoObl);
            } catch (JSONException e) {
                WebresultWrs.setResultBln(false);
                WebresultWrs.setSuccessBln(false);
                e.printStackTrace();
            }

            return WebresultWrs;
        }
    }

    private static class mBinFromWebserviceGetAsyncTask extends AsyncTask<String, Void, cWebresult> {
        @Override
        protected cWebresult doInBackground(final String... params) {
            cWebresult WebresultWrs = new cWebresult();

            List<PropertyInfo> l_PropertyInfoObl = new ArrayList<>();

            PropertyInfo l_PropertyInfo1Pin = new PropertyInfo();
            l_PropertyInfo1Pin.name = cWebserviceDefinitions.WEBPROPERTY_LOCATION_NL;
            l_PropertyInfo1Pin.setValue(cUser.currentUser.currentBranch.getBranchStr());
            l_PropertyInfoObl.add(l_PropertyInfo1Pin);

            SoapObject binSubSet = new SoapObject(cWebservice.WEBSERVICE_NAMESPACE, cWebserviceDefinitions.WEBPROPERTY_BINSUBSET);
            binSubSet.addProperty("string", params[0]);

            PropertyInfo l_PropertyInfo2Pin = new PropertyInfo();
            l_PropertyInfo2Pin.name = cWebserviceDefinitions.WEBPROPERTY_BINSUBSET;
            l_PropertyInfo2Pin.setValue(binSubSet);
            l_PropertyInfoObl.add(l_PropertyInfo2Pin);

            try {
                new cWebresult();
                WebresultWrs = cWebresult.pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_GETWAREHOUSELOCATIONS, l_PropertyInfoObl);
            } catch (JSONException e) {
                WebresultWrs.setResultBln(false);
                WebresultWrs.setSuccessBln(false);
                e.printStackTrace();
            }

            return WebresultWrs;
        }
    }

    private static class mReasonsFromWebserviceGetAsyncTask extends AsyncTask<Void, Void, cWebresult> {
        @Override
        protected cWebresult doInBackground(final Void... params) {
            cWebresult WebresultWrs = new cWebresult();

            List<PropertyInfo> l_PropertyInfoObl = new ArrayList<>();

            PropertyInfo l_PropertyInfo1Pin = new PropertyInfo();
            l_PropertyInfo1Pin.name = cWebserviceDefinitions.WEBPROPERTY_LOCATION_NL;
            l_PropertyInfo1Pin.setValue(cUser.currentUser.currentBranch.getBranchStr());
            l_PropertyInfoObl.add(l_PropertyInfo1Pin);

            try {
                WebresultWrs = cWebresult.pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_GETWAREHOUSEREASONS, l_PropertyInfoObl);
            } catch (JSONException e) {
                WebresultWrs.setResultBln(false);
                WebresultWrs.setSuccessBln(false);
                e.printStackTrace();
            }

            return WebresultWrs;
        }
    }

    private static class mAuthorizedStockOwnerFromWebserviceGetAsyncTask extends AsyncTask<Void, Void, cWebresult> {
        @Override
        protected cWebresult doInBackground(final Void... params) {
            cWebresult WebresultWrs = new cWebresult();

            List<PropertyInfo> l_PropertyInfoObl = new ArrayList<>();

            PropertyInfo l_PropertyInfo1Pin = new PropertyInfo();
            l_PropertyInfo1Pin.name = cWebserviceDefinitions.WEBPROPERTY_LOCATION_NL;
            l_PropertyInfo1Pin.setValue(cUser.currentUser.currentBranch.getBranchStr());
            l_PropertyInfoObl.add(l_PropertyInfo1Pin);

            PropertyInfo l_PropertyInfo2Pin = new PropertyInfo();
            l_PropertyInfo2Pin.name = cWebserviceDefinitions.WEBPROPERTY_USER;
            l_PropertyInfo2Pin.setValue(cUser.currentUser.getUsernameStr());
            l_PropertyInfoObl.add(l_PropertyInfo2Pin);

            try {
                WebresultWrs = cWebresult.pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_GETSTOCKOWNERFORUSER, l_PropertyInfoObl);
            } catch (JSONException e) {
                WebresultWrs.setResultBln(false);
                WebresultWrs.setSuccessBln(false);
                e.printStackTrace();
            }

            return WebresultWrs;
        }
    }

    //End Region Private Methods

}
