package SSU_WHS.PackAndShip.PackAndShipShippingMethod;

import android.app.Application;
import android.os.AsyncTask;

import SSU_WHS.General.acScanSuiteDatabase;
import SSU_WHS.PackAndShip.PackAndShipSetting.cPackAndShipSettingEntity;
import SSU_WHS.PackAndShip.PackAndShipSetting.iPackAndShipSettingDao;

public class cPackAndShipShippingMethodRepository {



    //Region Public Properties
    private iPackAndShipShippingMethodDao packAndShipShippingMethodDao;
    //End Region Public Properties

    //Region Constructor
    cPackAndShipShippingMethodRepository(Application pvApplication) {
        //Region Private Properties
        acScanSuiteDatabase db = acScanSuiteDatabase.pGetDatabase(pvApplication);
        this.packAndShipShippingMethodDao = db.packAndShipShippingMethodDao();
    }
    //End Region Constructor

    //Region Public Methods

    public void insert(cPackAndShipShippingMethodEntity pvPackAndShipShippingMethodEntity) {
        new mInsertAsyncTask(packAndShipShippingMethodDao).execute(pvPackAndShipShippingMethodEntity);
    }

    public void delete(cPackAndShipShippingMethodEntity pvPackAndShipShippingMethodEntity) {
        new mDeleteAsyncTask(packAndShipShippingMethodDao).execute(pvPackAndShipShippingMethodEntity);
    }

    public void deleteAll() {
        new mDeleteAllAsyncTask(packAndShipShippingMethodDao).execute();
    }

    //End Region Public Methods

    //Region Private Methods

    private static class mInsertAsyncTask extends AsyncTask<cPackAndShipShippingMethodEntity, Void, Void> {
        private iPackAndShipShippingMethodDao mAsyncTaskDao;

        mInsertAsyncTask(iPackAndShipShippingMethodDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final cPackAndShipShippingMethodEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class mDeleteAsyncTask extends AsyncTask<cPackAndShipShippingMethodEntity, Void, Void> {
        private iPackAndShipShippingMethodDao mAsyncTaskDao;

        mDeleteAsyncTask(iPackAndShipShippingMethodDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final cPackAndShipShippingMethodEntity... params) {
            mAsyncTaskDao.delete(params[0]);
            return null;
        }
    }

    private static class mDeleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private iPackAndShipShippingMethodDao mAsyncTaskDao;

        mDeleteAllAsyncTask(iPackAndShipShippingMethodDao dao) {
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

