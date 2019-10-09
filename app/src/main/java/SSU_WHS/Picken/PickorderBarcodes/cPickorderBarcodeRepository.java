package SSU_WHS.Picken.PickorderBarcodes;

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

import SSU_WHS.Basics.Users.cUser;
import SSU_WHS.Picken.Pickorders.cPickorder;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebserviceDefinitions;
import SSU_WHS.General.acScanSuiteDatabase;

public class cPickorderBarcodeRepository {
   // Public properties
    public iPickorderBarcodeDao pickorderBarcodeDao;
    //End Public properties

    //Private properties
    private acScanSuiteDatabase db;
    //End Private properties

    //Region Constructor
    cPickorderBarcodeRepository(Application pvApplication) {
        this.db = acScanSuiteDatabase.getDatabase(pvApplication);
        this.pickorderBarcodeDao = db.pickorderBarcodeDao();
    }
    //End Region Constructor

    //Region public methods


    public void pInsert(cPickorderBarcodeEntity pickorderBarcodeEntity) {
        new insertAsyncTask(pickorderBarcodeDao).execute(pickorderBarcodeEntity);
    }

    public void pDelete(cPickorderBarcodeEntity pickorderBarcodeEntity) {
        new deleteAsyncTask(pickorderBarcodeDao).execute(pickorderBarcodeEntity);
    }

    public void pDeleteAll() {
        new cPickorderBarcodeRepository.deleteAllAsyncTask(pickorderBarcodeDao).execute();
    }

    //End region public methods

    //Region private methods
    private static class deleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private iPickorderBarcodeDao mAsyncTaskDao;

        deleteAllAsyncTask(iPickorderBarcodeDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final Void... params) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }

    private static class insertAsyncTask extends AsyncTask<cPickorderBarcodeEntity, Void, Void> {
        private iPickorderBarcodeDao mAsyncTaskDao;

        insertAsyncTask(iPickorderBarcodeDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final cPickorderBarcodeEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class deleteAsyncTask extends AsyncTask<cPickorderBarcodeEntity, Void, Void> {
        private iPickorderBarcodeDao mAsyncTaskDao;

        deleteAsyncTask(iPickorderBarcodeDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final cPickorderBarcodeEntity... params) {
            mAsyncTaskDao.delete(params[0].itemno,params[0].variantcode);
            return null;
        }
    }



}
