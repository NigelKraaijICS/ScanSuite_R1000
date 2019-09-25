package SSU_WHS.Basics.Branches;

import android.app.Application;
import android.os.AsyncTask;

import org.json.JSONException;
import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import SSU_WHS.Basics.Users.cUser;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebserviceDefinitions;
import SSU_WHS.General.acScanSuiteDatabase;

public class cBranchRepository {

    //Region Public Properties
    public iBranchDao branchDao;
    //End Region Public Properties

    //Region Private Properties
    private acScanSuiteDatabase db;
    //End Region Private Properties

    //Region Constructor
    cBranchRepository(Application pvApplication) {
        this.db = acScanSuiteDatabase.getDatabase(pvApplication);
        this.branchDao = db.branchDao();
    }
    //End Region Constructor

    //Region Public Methods
    public cWebresult pGetBranchesFromWebserviceWrs() {

        List<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();

        try {
            webResultWrs = new branchesFromWebserviceGetAsyncTask().execute().get();
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


    public void pInsert(cBranchEntity branchEntity) {
        new insertAsyncTask(branchDao).execute(branchEntity);
    }

    public void pDeleteAll() {

        new   cBranchRepository.deleteAllAsyncTask(branchDao).execute();
    }

    //End Region Public Methods

    //Region Private Methods
    private static class deleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private iBranchDao mAsyncTaskDao;

        deleteAllAsyncTask(iBranchDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final Void... params) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }
    private static class insertAsyncTask extends AsyncTask<cBranchEntity, Void, Void> {
        private iBranchDao mAsyncTaskDao;

        insertAsyncTask(iBranchDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final cBranchEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class branchesFromWebserviceGetAsyncTask extends AsyncTask<Void, Void, cWebresult> {
        @Override
        protected cWebresult doInBackground(final Void... params) {
            cWebresult WebresultWrs = new cWebresult();

            List<PropertyInfo> l_PropertyInfoObl = new ArrayList<>();

            PropertyInfo l_PropertyInfo1Pin = new PropertyInfo();
            l_PropertyInfo1Pin.name = cWebserviceDefinitions.WEBPROPERTY_USER;
            l_PropertyInfo1Pin.setValue(cUser.currentUser.getUsernameStr());
            l_PropertyInfoObl.add(l_PropertyInfo1Pin);

            try {
                WebresultWrs = new cWebresult().pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_GETBRANCHESFORUSER, l_PropertyInfoObl);
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
