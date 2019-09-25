package SSU_WHS.Basics.Users;

import android.app.Application;
import android.os.AsyncTask;

import org.json.JSONException;
import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import ICS.Utils.cSharedPreferences;
import SSU_WHS.General.cPublicDefinitions;
import SSU_WHS.Webservice.cWebserviceDefinitions;
import SSU_WHS.General.acScanSuiteDatabase;
import SSU_WHS.Webservice.cWebresult;

public class cUserRepository {

    //Region Public Properties
    public iUserDao userDao;
    //End Region Public Properties

    //Region Private Properties
    private cWebresult webResult;
    private acScanSuiteDatabase db;
    //End Region Private Properties

    //Region Constructor
     cUserRepository(Application pvApplication) {
        this.db = acScanSuiteDatabase.getDatabase(pvApplication);
        this.userDao = db.userDao();
    }
    //End Region Constructor

    //Region Public Methods
    public cWebresult pGetUsersFromWebserviceWrs() {

        List<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();

        try {
            webResultWrs = new usersFromWebserviceGetAsyncTask().execute().get();
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


    public void pInsert(cUserEntity userEntity) {
        new insertAsyncTask(userDao).execute(userEntity);
    }

    public void pDeleteAll() {
        new cUserRepository.deleteAllAsyncTask(userDao).execute();
    }

    public cWebresult userLogonWrs (String pvPassword) {

        cUserLoginParams userLoginParams = new cUserLoginParams(cUser.currentUser.getUsernameStr(), pvPassword, "");
        List<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();

        try {
            webResultWrs = new userLogonAsyncTask().execute(userLoginParams).get();
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

    //End Region Public Methods

    //Region Private Methods
    private static class deleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private iUserDao mAsyncTaskDao;

        deleteAllAsyncTask(iUserDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final Void... params) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }
    private static class insertAsyncTask extends AsyncTask<cUserEntity, Void, Void> {
        private iUserDao mAsyncTaskDao;

        insertAsyncTask(iUserDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final cUserEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class userLogonAsyncTask extends AsyncTask<cUserLoginParams, Void, cWebresult> {
        @Override
        protected cWebresult doInBackground(cUserLoginParams... params) {
            cWebresult WebresultWrs = new cWebresult();

            List<PropertyInfo> l_PropertyInfoObl = new ArrayList<>();

            PropertyInfo l_PropertyInfo1Pin = new PropertyInfo();
            l_PropertyInfo1Pin.name = cWebserviceDefinitions.WEBPROPERTY_USERNAME;
            l_PropertyInfo1Pin.setValue(params[0].user);
            l_PropertyInfoObl.add(l_PropertyInfo1Pin);

            PropertyInfo l_PropertyInfo2Pin = new PropertyInfo();
            l_PropertyInfo2Pin.name = cWebserviceDefinitions.WEBPROPERTY_PASSWORD;
            l_PropertyInfo2Pin.setValue(params[0].password);
            l_PropertyInfoObl.add(l_PropertyInfo2Pin);

            PropertyInfo l_PropertyInfo3Pin = new PropertyInfo();
            l_PropertyInfo3Pin.name = cWebserviceDefinitions.WEBPROPERTY_LANGUAGEASCULTURE;
            l_PropertyInfo3Pin.setValue(params[0].culture);
            l_PropertyInfoObl.add(l_PropertyInfo3Pin);

            try {
                WebresultWrs = new cWebresult().pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_USERLOGIN, l_PropertyInfoObl);
            } catch (JSONException e) {
                WebresultWrs.setResultBln(false);
                WebresultWrs.setSuccessBln(false);
                e.printStackTrace();
            }

            return WebresultWrs;
        }
    }

    private static class usersFromWebserviceGetAsyncTask extends AsyncTask<Void, Void, cWebresult> {
        @Override
        protected cWebresult doInBackground(final Void... params) {
            cWebresult l_WebresultWrs = new cWebresult();

            List<PropertyInfo> l_PropertyInfoObl = new ArrayList<>();

            PropertyInfo l_PropertyInfo1Pin = new PropertyInfo();
            l_PropertyInfo1Pin.name = cWebserviceDefinitions.WEBPROPERTY_LOCATION_NL;
            l_PropertyInfo1Pin.setValue("");
            l_PropertyInfoObl.add(l_PropertyInfo1Pin);


            try {
                l_WebresultWrs = new cWebresult().pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_GETUSERS, l_PropertyInfoObl);
            } catch (JSONException e) {
                l_WebresultWrs.setResultBln(false);
                l_WebresultWrs.setSuccessBln(false);
                e.printStackTrace();
            }

            return l_WebresultWrs;
        }
    }

    private class cUserLoginParams{

        //Region Public Properties
        String user;
        String password;
        String culture;
        //End Region Public Properties

        //Region Constructor
        cUserLoginParams(String pv_user, String pv_password, String pv_culture) {
            this.user = pv_user;
            this.password = pv_password;
            this.culture = pv_culture;
        }
        //End Region Constructor
    }


    //End Region Private Methods




}
