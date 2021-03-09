package SSU_WHS.Picken.PickorderCompositeBarcode;

import android.app.Application;
import android.os.AsyncTask;

import SSU_WHS.General.acScanSuiteDatabase;

public class cPickorderCompositeBarcodeRepository {
   // Public properties
   private final iPickorderCompositeBarcodeDao pickorderCompositeBarcodeDao;
    //End Public properties

    //End Private properties

    //Region Constructor
    cPickorderCompositeBarcodeRepository(Application pvApplication) {
        //Private properties
        acScanSuiteDatabase db = acScanSuiteDatabase.pGetDatabase(pvApplication);
        this.pickorderCompositeBarcodeDao = db.pickorderCompositeBarcodeDao();
    }
    //End Region Constructor

    //Region public methods


    public void pInsert(cPickorderCompositeBarcodeEntity pickorderBarcodeEntity) {
        new insertAsyncTask(pickorderCompositeBarcodeDao).execute(pickorderBarcodeEntity);
    }

    public void pDeleteAll() {
        new cPickorderCompositeBarcodeRepository.deleteAllAsyncTask(pickorderCompositeBarcodeDao).execute();
    }

    //End region public methods

    //Region private methods
    private static class deleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private final iPickorderCompositeBarcodeDao mAsyncTaskDao;

        deleteAllAsyncTask(iPickorderCompositeBarcodeDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final Void... params) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }

    private static class insertAsyncTask extends AsyncTask<cPickorderCompositeBarcodeEntity, Void, Void> {
        private final iPickorderCompositeBarcodeDao mAsyncTaskDao;

        insertAsyncTask(iPickorderCompositeBarcodeDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final cPickorderCompositeBarcodeEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

}
