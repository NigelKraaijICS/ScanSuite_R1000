package SSU_WHS.PackAndShip.PackAndShipShippingPackage;

import android.app.Application;
import android.os.AsyncTask;

import SSU_WHS.General.acScanSuiteDatabase;
import SSU_WHS.PackAndShip.PackAndShipAddress.cPackAndShipAddressEntity;
import SSU_WHS.PackAndShip.PackAndShipAddress.iPackAndShipAddressDao;

public class cPackAndShipShippingPackageRepository {
    //Region Public Properties
    private iPackAndShipShippingPackageDao packAndShipShippingPackageDao;
    //End Region Public Properties

    //Region Constructor
    cPackAndShipShippingPackageRepository(Application pvApplication) {
        //Region Private Properties
        acScanSuiteDatabase db = acScanSuiteDatabase.pGetDatabase(pvApplication);
        this.packAndShipShippingPackageDao = db.packAndShipShippingPackageDao();
    }
    //End Region Constructor

    //Region Public Methods

    public void insert(cPackAndShipShippingPackageEntity pvPackAndShipShippingPackageEntity) {
        new mInsertAsyncTask(packAndShipShippingPackageDao).execute(pvPackAndShipShippingPackageEntity);
    }

    public void deleteAll() {
        new mDeleteAllAsyncTask(packAndShipShippingPackageDao).execute();
    }

    private static class mInsertAsyncTask extends AsyncTask<cPackAndShipShippingPackageEntity, Void, Void> {
        private iPackAndShipShippingPackageDao mAsyncTaskDao;

        mInsertAsyncTask(iPackAndShipShippingPackageDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final cPackAndShipShippingPackageEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class mDeleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private iPackAndShipShippingPackageDao mAsyncTaskDao;

        mDeleteAllAsyncTask(iPackAndShipShippingPackageDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Void... params) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }


}
