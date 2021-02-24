package SSU_WHS.PackAndShip.PackAndShipAddress;

import android.app.Application;
import android.os.AsyncTask;

import SSU_WHS.General.acScanSuiteDatabase;

public class cPackAndShipAddressRepository {
    //Region Public Properties
    private final iPackAndShipAddressDao packAndShipAddressDao;
    //End Region Public Properties

    //Region Constructor
    cPackAndShipAddressRepository(Application pvApplication) {
        //Region Private Properties
        acScanSuiteDatabase db = acScanSuiteDatabase.pGetDatabase(pvApplication);
        this.packAndShipAddressDao = db.packAndShipAddressDao();
    }
    //End Region Constructor

    //Region Public Methods

    public void insert(cPackAndShipAddressEntity pvPackAndShipAddressEntity) {
        new mInsertAsyncTask(packAndShipAddressDao).execute(pvPackAndShipAddressEntity);
    }

    public void deleteAll() {
        new mDeleteAllAsyncTask(packAndShipAddressDao).execute();
    }

    private static class mInsertAsyncTask extends AsyncTask<cPackAndShipAddressEntity, Void, Void> {
        private final iPackAndShipAddressDao mAsyncTaskDao;

        mInsertAsyncTask(iPackAndShipAddressDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final cPackAndShipAddressEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class mDeleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private final iPackAndShipAddressDao mAsyncTaskDao;

        mDeleteAllAsyncTask(iPackAndShipAddressDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Void... params) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }


}
