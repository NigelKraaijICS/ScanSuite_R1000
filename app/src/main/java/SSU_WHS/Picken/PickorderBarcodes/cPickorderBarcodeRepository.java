package SSU_WHS.Picken.PickorderBarcodes;

import android.app.Application;
import android.os.AsyncTask;

import SSU_WHS.General.acScanSuiteDatabase;

public class cPickorderBarcodeRepository {
   // Public properties
   private iPickorderBarcodeDao pickorderBarcodeDao;
    //End Public properties

    //End Private properties

    //Region Constructor
    cPickorderBarcodeRepository(Application pvApplication) {
        //Private properties
        acScanSuiteDatabase db = acScanSuiteDatabase.pGetDatabase(pvApplication);
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
