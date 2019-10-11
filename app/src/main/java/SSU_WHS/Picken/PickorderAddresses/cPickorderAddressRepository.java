package SSU_WHS.Picken.PickorderAddresses;

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

public class cPickorderAddressRepository {
    public iPickorderAddressDao pickorderAddressDao;

    //Region Private Properties
    private acScanSuiteDatabase db;
    //End Region Private Properties

    cPickorderAddressRepository(Application pvApplication) {
        this.db = acScanSuiteDatabase.getDatabase(pvApplication);
        this.pickorderAddressDao = db.pickorderAddressDao();
    }

    //Region Public Methods

    public void pDeleteAll() {
        new deleteAllAsyncTask(pickorderAddressDao).execute();
    }

    public void pInsert(cPickorderAddressEntity pickorderAddressEntity){
        new insertAsyncTask(pickorderAddressDao).execute(pickorderAddressEntity);
    }
    //End Region Public Methods

    //Region Private Methods

    private static class insertAsyncTask extends AsyncTask<cPickorderAddressEntity, Void, Void> {
        private iPickorderAddressDao mAsyncTaskDao;

        insertAsyncTask(iPickorderAddressDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final cPickorderAddressEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class deleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private iPickorderAddressDao mAsyncTaskDao;

        deleteAllAsyncTask(iPickorderAddressDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final Void... params) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }

    //End Region Private Methods



}
