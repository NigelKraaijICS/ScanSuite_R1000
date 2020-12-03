package SSU_WHS.Basics.CompositeBarcodeProperty;

import android.app.Application;
import android.os.AsyncTask;

import org.json.JSONException;
import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import SSU_WHS.Basics.CompositeBarcode.cCompositeBarcodeEntity;
import SSU_WHS.Basics.CompositeBarcode.iCompositeBarcodeDao;
import SSU_WHS.General.acScanSuiteDatabase;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebserviceDefinitions;


public class cCompositeBarcodePropertyRepository {

    //Region Public properties
    private final iCompositeBarcodePropertyDao compositeBarcodePropertyDao;

    //End Region Public Properties

    //Region Private Properties
    public//End Region Private Properties

    //Region Constructor
    cCompositeBarcodePropertyRepository(Application pvApplication) {
        acScanSuiteDatabase db = acScanSuiteDatabase.pGetDatabase(pvApplication);
        this.compositeBarcodePropertyDao = db.compositeBarcodePropertyDao();
    }

    //Region Public Methods



    public void pInsert(cCompositeBarcodePropertyEntity pvCompositeBarcodePropertyEntity){
    new insertAsyncTask(compositeBarcodePropertyDao).execute(pvCompositeBarcodePropertyEntity);
    }

    public void pDeleteAll() {
        new deleteAllAsyncTask(compositeBarcodePropertyDao).execute();
    }
    //End Region Public Methods

    //Region Private Methods
    private static class deleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private final iCompositeBarcodePropertyDao mAsyncTaskDao;

        deleteAllAsyncTask(iCompositeBarcodePropertyDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final Void... params) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }

    private static class insertAsyncTask extends AsyncTask<cCompositeBarcodePropertyEntity, Void, Void>{
       private final iCompositeBarcodePropertyDao mAsyncTaskDao;

        insertAsyncTask(iCompositeBarcodePropertyDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final cCompositeBarcodePropertyEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

}
