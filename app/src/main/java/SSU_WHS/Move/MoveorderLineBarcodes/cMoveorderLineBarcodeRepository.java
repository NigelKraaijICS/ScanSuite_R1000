package SSU_WHS.Move.MoveorderLineBarcodes;

import android.app.Application;
import android.os.AsyncTask;

import SSU_WHS.General.acScanSuiteDatabase;

public class cMoveorderLineBarcodeRepository {
    //Public Properties

    public iMoveorderLineBarcodeDao moveorderLineBarcodeDao;

    //End Public Properties

    //Private properties
    private acScanSuiteDatabase db;
    //End Private properties

    //Region Constructor
    cMoveorderLineBarcodeRepository(Application application) {
        this.db = acScanSuiteDatabase.getDatabase(application);
        this.moveorderLineBarcodeDao = db.moveorderLineBarcodeDao();
    }

    //End Region Constructor

    //Region public methods

    public void pInsert (cMoveorderLineBarcodeEntity moveorderLineBarcodeEntity) {
        new insertAsyncTask(moveorderLineBarcodeDao).execute(moveorderLineBarcodeEntity);
    }

    public void pDeleteAll () {
        new deleteAllAsyncTask(moveorderLineBarcodeDao).execute();
    }

    public void pDeleteForLineNo(Integer pvLineNoInt) {
        new mDeleteAllForLineNoAsyncTask(moveorderLineBarcodeDao).execute(pvLineNoInt);
    }

    public void updateBarcodeAmount(String pvBarcodeStr, double pvAmountDbl) {
        UpdateBarcodeAmountParams updateBarcodeAmountParams = new UpdateBarcodeAmountParams(pvBarcodeStr, pvAmountDbl);
        new updateBarcodeAmountAsyncTask(moveorderLineBarcodeDao).execute(updateBarcodeAmountParams);
    }

    //End Region Public Methods

    //Region Private methods


    private static class deleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private iMoveorderLineBarcodeDao mAsyncTaskDao;

        deleteAllAsyncTask(iMoveorderLineBarcodeDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final Void... params) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }

    private static class mDeleteAllForLineNoAsyncTask extends AsyncTask<Integer, Void, Void> {
        private iMoveorderLineBarcodeDao mAsyncTaskDao;

        mDeleteAllForLineNoAsyncTask(iMoveorderLineBarcodeDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final Integer... params) {
            mAsyncTaskDao.deleteLinesForLineNo(params[0]);
            return null;
        }
    }

    private static class insertAsyncTask extends AsyncTask<cMoveorderLineBarcodeEntity, Void, Void> {
        private iMoveorderLineBarcodeDao mAsyncTaskDao;

        insertAsyncTask(iMoveorderLineBarcodeDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final cMoveorderLineBarcodeEntity... params) {
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
        private iMoveorderLineBarcodeDao mAsyncTaskDao;
        updateBarcodeAmountAsyncTask(iMoveorderLineBarcodeDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected Void doInBackground(UpdateBarcodeAmountParams... params) {
            mAsyncTaskDao.updateBarcodeAmount(params[0].barcodeStr, params[0].amountDbl);
            return null;
        }
    }

    private static class getMoveorderLineBarcodesForLineNoAsyncTask extends AsyncTask<Integer, Void, Void> {
        private iMoveorderLineBarcodeDao mAsyncTaskDao;
        getMoveorderLineBarcodesForLineNoAsyncTask(iMoveorderLineBarcodeDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected Void doInBackground(Integer... params) {
            mAsyncTaskDao.getMoveorderLineBarcodesForLineNo(params[0]);
            return null;
        }
    }


}
