package SSU_WHS.Basics.PropertyGroupProperty;

import android.app.Application;
import android.os.AsyncTask;

import org.json.JSONException;
import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import SSU_WHS.Basics.PropertyGroup.cPropertyGroupEntity;
import SSU_WHS.Basics.PropertyGroup.iPropertyGroupDao;
import SSU_WHS.General.acScanSuiteDatabase;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebserviceDefinitions;


public class cPropertyGroupPropertyRepository {

    //Region Public properties
    private iPropertyGroupPropertyDao propertyGroupPropertyDao;
    //End Region Public Properties

    //Region Private Properties
    public//End Region Private Properties

    //Region Constructor
    cPropertyGroupPropertyRepository(Application pvApplication) {
        acScanSuiteDatabase db = acScanSuiteDatabase.pGetDatabase(pvApplication);
        this.propertyGroupPropertyDao = db.propertyGroupPropertyDao();
    }

    //Region Public Methods

    public void pInsert(cPropertyGroupPropertyEntity pvPropertyGroupPropertyEntity){
    new insertAsyncTask(propertyGroupPropertyDao).execute(pvPropertyGroupPropertyEntity);
    }

    public void pDeleteAll() {
        new deleteAllAsyncTask(propertyGroupPropertyDao).execute();
    }
    //End Region Public Methods

    //Region Private Methods
    private static class deleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private iPropertyGroupPropertyDao mAsyncTaskDao;

        deleteAllAsyncTask(iPropertyGroupPropertyDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final Void... params) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }

    private static class insertAsyncTask extends AsyncTask<cPropertyGroupPropertyEntity, Void, Void>{
       private iPropertyGroupPropertyDao mAsyncTaskDao;

        insertAsyncTask(iPropertyGroupPropertyDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final cPropertyGroupPropertyEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

}
