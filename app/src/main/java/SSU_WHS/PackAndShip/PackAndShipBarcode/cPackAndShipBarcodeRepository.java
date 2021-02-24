package SSU_WHS.PackAndShip.PackAndShipBarcode;

import android.app.Application;
import android.os.AsyncTask;

import SSU_WHS.General.acScanSuiteDatabase;

public class cPackAndShipBarcodeRepository {
    //Region Public Properties
    private final iPackAndShipBarcodeDao packAndShipBarcodeDao;
    //End Region Public Properties

    //Region Constructor
    cPackAndShipBarcodeRepository(Application pvApplication) {
        //Region Private Properties
        acScanSuiteDatabase db = acScanSuiteDatabase.pGetDatabase(pvApplication);
        this.packAndShipBarcodeDao = db.packAndShipBarcodeDao();
    }
    //End Region Constructor

    //Region Public Methods

    public void insert(cPackAndShipBarcodeEntity pvPackAndShipBarcodeEntity) {
        new mInsertAsyncTask(packAndShipBarcodeDao).execute(pvPackAndShipBarcodeEntity);
    }

    public void deleteAll() {
        new mDeleteAllAsyncTask(packAndShipBarcodeDao).execute();
    }

    private static class mInsertAsyncTask extends AsyncTask<cPackAndShipBarcodeEntity, Void, Void> {
        private final iPackAndShipBarcodeDao mAsyncTaskDao;

        mInsertAsyncTask(iPackAndShipBarcodeDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final cPackAndShipBarcodeEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class mDeleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private final iPackAndShipBarcodeDao mAsyncTaskDao;

        mDeleteAllAsyncTask(iPackAndShipBarcodeDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Void... params) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }
}
