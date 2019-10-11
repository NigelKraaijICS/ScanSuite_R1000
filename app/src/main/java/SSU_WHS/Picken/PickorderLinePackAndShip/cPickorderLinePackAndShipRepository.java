package SSU_WHS.Picken.PickorderLinePackAndShip;

import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebserviceDefinitions;
import SSU_WHS.General.acScanSuiteDatabase;

public class cPickorderLinePackAndShipRepository {

    //Region Public Properties

    //Region Public Properties

    //Region Private Properties
    private iPickorderLinePackAndShipDao pickorderLinePackAndShipDao;
    private acScanSuiteDatabase db;
    //End Region Private Properties


    //Region Constructor
    cPickorderLinePackAndShipRepository(Application pvApplication) {
        this.db = acScanSuiteDatabase.getDatabase(pvApplication);
        this.pickorderLinePackAndShipDao = db.pickorderLinePackAndShipDao();
    }
    //End Region Constructor

    //Region Public Methods

    public void pDeleteAll() {
        new deleteAllAsyncTask(this.pickorderLinePackAndShipDao).execute();
    }

    public void pInsert(cPickorderLinePackAndShipEntity pickorderLinePackAndShipEntity){
        new insertAsyncTask(this.pickorderLinePackAndShipDao).execute(pickorderLinePackAndShipEntity);
    }
    //End Region Public Methods


    public void deleteAll () {
        new deleteAllAsyncTask(pickorderLinePackAndShipDao).execute();
    }

    private static class deleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private iPickorderLinePackAndShipDao mAsyncTaskDao;

        deleteAllAsyncTask(iPickorderLinePackAndShipDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final Void... params) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }
    public void insert(cPickorderLinePackAndShipEntity pickorderLinePackAndShipEntity) {
        new insertAsyncTask(pickorderLinePackAndShipDao).execute(pickorderLinePackAndShipEntity);
    }
    private static class insertAsyncTask extends AsyncTask<cPickorderLinePackAndShipEntity, Void, Void> {
        private iPickorderLinePackAndShipDao mAsyncTaskDao;

        insertAsyncTask(iPickorderLinePackAndShipDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final cPickorderLinePackAndShipEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }



}
