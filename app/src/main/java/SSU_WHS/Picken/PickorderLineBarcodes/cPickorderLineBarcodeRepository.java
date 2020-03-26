package SSU_WHS.Picken.PickorderLineBarcodes;

import android.app.Application;
import android.os.AsyncTask;

import SSU_WHS.General.acScanSuiteDatabase;

public class cPickorderLineBarcodeRepository {

    //Public Properties

    private iPickorderLineBarcodeDao pickorderLineBarcodeDao;

    //End Public Properties

    //End Private properties

    //Region Constructor
    cPickorderLineBarcodeRepository(Application application) {
        //Private properties
        acScanSuiteDatabase db = acScanSuiteDatabase.pGetDatabase(application);
        this.pickorderLineBarcodeDao = db.pickorderLineBarcodeDao();
    }

    //End Region Constructor

    //Region public methods

    public void pInsert (cPickorderLineBarcodeEntity pickorderLineBarcodeEntity) {
        new insertAsyncTask(pickorderLineBarcodeDao).execute(pickorderLineBarcodeEntity);
    }

    public void pDeleteAll () {
        new cPickorderLineBarcodeRepository.deleteAllAsyncTask(pickorderLineBarcodeDao).execute();
    }

    public void pDeleteForLineNo(Integer pvLineNoInt) {
        new cPickorderLineBarcodeRepository.mDeleteAllForLineNoAsyncTask(pickorderLineBarcodeDao).execute(pvLineNoInt);
    }

    public void updateBarcodeAmount(String pvBarcodeStr, double pvAmountDbl) {
        UpdateBarcodeAmountParams updateBarcodeAmountParams = new UpdateBarcodeAmountParams(pvBarcodeStr, pvAmountDbl);
        new updateBarcodeAmountAsyncTask(pickorderLineBarcodeDao).execute(updateBarcodeAmountParams);
    }


    //End Region Public Methods

    //Region Private methods


    private static class deleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private iPickorderLineBarcodeDao mAsyncTaskDao;

        deleteAllAsyncTask(iPickorderLineBarcodeDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final Void... params) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }

    private static class mDeleteAllForLineNoAsyncTask extends AsyncTask<Integer, Void, Void> {
        private iPickorderLineBarcodeDao mAsyncTaskDao;

        mDeleteAllForLineNoAsyncTask(iPickorderLineBarcodeDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final Integer... params) {
            mAsyncTaskDao.deleteLinesForLineNo(params[0]);
            return null;
        }
    }

    private static class insertAsyncTask extends AsyncTask<cPickorderLineBarcodeEntity, Void, Void> {
        private iPickorderLineBarcodeDao mAsyncTaskDao;

        insertAsyncTask(iPickorderLineBarcodeDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final cPickorderLineBarcodeEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class UpdateBarcodeAmountParams {
        String barcodeStr;
        double amountDbl;

        UpdateBarcodeAmountParams(String pvBarcodeStr, double pvAmountDbl) {
            this.barcodeStr = pvBarcodeStr;
            this.amountDbl = pvAmountDbl;
        }
    }

    private static class updateBarcodeAmountAsyncTask extends AsyncTask<UpdateBarcodeAmountParams, Void, Void> {
        private iPickorderLineBarcodeDao mAsyncTaskDao;
        updateBarcodeAmountAsyncTask(iPickorderLineBarcodeDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected Void doInBackground(UpdateBarcodeAmountParams... params) {
            mAsyncTaskDao.updateBarcodeAmount(params[0].barcodeStr, params[0].amountDbl);
            return null;
        }
    }
}
