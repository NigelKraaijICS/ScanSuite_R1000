package SSU_WHS.Move.MoveorderLineBarcode;

import android.app.Application;
import android.os.AsyncTask;

import SSU_WHS.General.acScanSuiteDatabase;

public class cMoveorderLineBarcodeRepository {

    //Region Public Properties
    private final iMoveorderLineBarcodeDao moveorderLineBarcodeDao;
    //End Region Public Properties

    //Region Constructor
    cMoveorderLineBarcodeRepository(Application pvApplication) {
        //Region Private Properties
        acScanSuiteDatabase db = acScanSuiteDatabase.pGetDatabase(pvApplication);
        this.moveorderLineBarcodeDao = db.moveorderLineBarcodeDao();
    }
    //End Region Constructor

    //Region Public Methods

    public void insert(cMoveorderLineBarcodeEntity pvMoveorderLineBarcodeEntity) {
        new mInsertAsyncTask(moveorderLineBarcodeDao).execute(pvMoveorderLineBarcodeEntity);
    }

    public void delete(cMoveorderLineBarcodeEntity pvMoveorderLineBarcodeEntity) {
        new mDeleteAsyncTask(moveorderLineBarcodeDao).execute(pvMoveorderLineBarcodeEntity);
    }

    public void deleteAll() {
        new mDeleteAllAsyncTask(moveorderLineBarcodeDao).execute();
    }


    //End Region Public Methods

    //Region Private Methods

    private static class mInsertAsyncTask extends AsyncTask<cMoveorderLineBarcodeEntity, Void, Void> {
        private final iMoveorderLineBarcodeDao mAsyncTaskDao;

        mInsertAsyncTask(iMoveorderLineBarcodeDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final cMoveorderLineBarcodeEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class mDeleteAsyncTask extends AsyncTask<cMoveorderLineBarcodeEntity, Void, Void> {
        private final iMoveorderLineBarcodeDao mAsyncTaskDao;

        mDeleteAsyncTask(iMoveorderLineBarcodeDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final cMoveorderLineBarcodeEntity... params) {
            mAsyncTaskDao.delete(params[0]);
            return null;
        }
    }

    private static class mDeleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private final iMoveorderLineBarcodeDao mAsyncTaskDao;

        mDeleteAllAsyncTask(iMoveorderLineBarcodeDao dao) {
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

