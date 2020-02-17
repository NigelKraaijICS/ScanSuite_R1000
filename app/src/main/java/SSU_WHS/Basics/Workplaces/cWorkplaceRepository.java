package SSU_WHS.Basics.Workplaces;

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

public class cWorkplaceRepository {
    //Region Public Properties
    public iWorkplaceDao workplaceDao;
    //End Region Public Properties

    //Region Private Properties
    private cWebresult webResult;
    private acScanSuiteDatabase db;
    //End Region Private Properties

    //Region Constructor
    cWorkplaceRepository(Application pvApplication) {
        this.db = acScanSuiteDatabase.pGetDatabase(pvApplication);
        this.workplaceDao = db.workplaceDao();
    }
    //End Region Constructor

    //Region Public Methods

    public cWebresult pGetWorkplacesFromWebserviceWrs() {

        List<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();

        try {
            webResultWrs = new workplacesFromWebserviceGetAsyncTask().execute().get();
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

    public void insert (cWorkplaceEntity workplaceEntity) {
        new insertAsyncTask(workplaceDao).execute(workplaceEntity);
    }

    public void deleteAll () {
        new cWorkplaceRepository.deleteAllAsyncTask(workplaceDao).execute();
    }

    //End Region Public Methods

    //Region Private Methods
    private static class deleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private iWorkplaceDao mAsyncTaskDao;

        deleteAllAsyncTask(iWorkplaceDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final Void... params) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }
    private static class insertAsyncTask extends AsyncTask<cWorkplaceEntity, Void, Void> {
        private iWorkplaceDao mAsyncTaskDao;

        insertAsyncTask(iWorkplaceDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final cWorkplaceEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

   private static class workplacesFromWebserviceGetAsyncTask extends AsyncTask<Void, Void, cWebresult> {
        @Override
        protected cWebresult doInBackground(final Void... params) {
            cWebresult WebresultWrs = new cWebresult();

            List<PropertyInfo> l_PropertyInfoObl = new ArrayList<>();

            PropertyInfo l_PropertyInfo1Pin = new PropertyInfo();
            l_PropertyInfo1Pin.name = cWebserviceDefinitions.WEBPROPERTY_LOCATION_NL;
            l_PropertyInfo1Pin.setValue(cUser.currentUser.currentBranch.getBranchStr());
            l_PropertyInfoObl.add(l_PropertyInfo1Pin);

            try {
                WebresultWrs = new cWebresult().pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_GETWORKPLACES, l_PropertyInfoObl);
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
