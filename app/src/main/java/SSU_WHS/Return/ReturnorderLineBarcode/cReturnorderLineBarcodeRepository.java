package SSU_WHS.Return.ReturnorderLineBarcode;

import android.app.Application;
import android.os.AsyncTask;

import SSU_WHS.General.acScanSuiteDatabase;

public class cReturnorderLineBarcodeRepository {
    //Public Properties

    private iReturnorderLineBarcodeDao returnorderLineBarcodeDao;

    //End Public Properties

    //Private properties

    //End Private properties

    //Region Constructor
    cReturnorderLineBarcodeRepository(Application application) {
        this.returnorderLineBarcodeDao = acScanSuiteDatabase.pGetDatabase(application).returnorderLineBarcodeDao();
    }

    //End Region Constructor

    //Region public methods

    public void pInsert (cReturnorderLineBarcodeEntity returnorderLineBarcodeEntity) {
        new insertAsyncTask(returnorderLineBarcodeDao).execute(returnorderLineBarcodeEntity);
    }

    public void pDeleteAll () {
        new deleteAllAsyncTask(returnorderLineBarcodeDao).execute();
    }

    public void pDeleteForLineNo(Integer pvLineNoInt) {
        new mDeleteAllForLineNoAsyncTask(returnorderLineBarcodeDao).execute(pvLineNoInt);
    }

    public void updateBarcodeAmount(String pvBarcodeStr, double pvAmountDbl) {
        UpdateBarcodeAmountParams updateBarcodeAmountParams = new UpdateBarcodeAmountParams(pvBarcodeStr, pvAmountDbl);
        new updateBarcodeAmountAsyncTask(returnorderLineBarcodeDao).execute(updateBarcodeAmountParams);
    }


    //End Region Public Methods

    //Region Private methods


    private static class deleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private iReturnorderLineBarcodeDao mAsyncTaskDao;

        deleteAllAsyncTask(iReturnorderLineBarcodeDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final Void... params) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }

    private static class mDeleteAllForLineNoAsyncTask extends AsyncTask<Integer, Void, Void> {
        private iReturnorderLineBarcodeDao mAsyncTaskDao;

        mDeleteAllForLineNoAsyncTask(iReturnorderLineBarcodeDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final Integer... params) {
            mAsyncTaskDao.deleteLinesForLineNo(params[0]);
            return null;
        }
    }

    private static class insertAsyncTask extends AsyncTask<cReturnorderLineBarcodeEntity, Void, Void> {
        private iReturnorderLineBarcodeDao mAsyncTaskDao;

        insertAsyncTask(iReturnorderLineBarcodeDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final cReturnorderLineBarcodeEntity... params) {
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
        private iReturnorderLineBarcodeDao mAsyncTaskDao;
        updateBarcodeAmountAsyncTask(iReturnorderLineBarcodeDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected Void doInBackground(UpdateBarcodeAmountParams... params) {
            mAsyncTaskDao.updateBarcodeAmount(params[0].barcodeStr, params[0].amountDbl);
            return null;
        }
    }
}
