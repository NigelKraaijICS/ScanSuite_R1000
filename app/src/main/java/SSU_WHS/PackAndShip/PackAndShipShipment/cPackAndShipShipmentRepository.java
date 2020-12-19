package SSU_WHS.PackAndShip.PackAndShipShipment;

import android.app.Application;
import android.os.AsyncTask;

import SSU_WHS.General.acScanSuiteDatabase;
import SSU_WHS.PackAndShip.PackAndShipSetting.cPackAndShipSettingEntity;
import SSU_WHS.PackAndShip.PackAndShipSetting.iPackAndShipSettingDao;

public class cPackAndShipShipmentRepository {



    //Region Public Properties
    private iPackAndShipShipmentDao packAndShipShipmentDao;
    //End Region Public Properties

    //Region Constructor
    cPackAndShipShipmentRepository(Application pvApplication) {
        //Region Private Properties
        acScanSuiteDatabase db = acScanSuiteDatabase.pGetDatabase(pvApplication);
        this.packAndShipShipmentDao = db.packAndShipShipmentDao();
    }
    //End Region Constructor

    //Region Public Methods

    public void insert(cPackAndShipShipmentEntity pvPackAndShipShipmentEntity) {
        new mInsertAsyncTask(packAndShipShipmentDao).execute(pvPackAndShipShipmentEntity);
    }

    public void delete(cPackAndShipShipmentEntity pvPackAndShipShipmentEntity) {
        new mDeleteAsyncTask(packAndShipShipmentDao).execute(pvPackAndShipShipmentEntity);
    }

    public void deleteAll() {
        new mDeleteAllAsyncTask(packAndShipShipmentDao).execute();
    }

    //End Region Public Methods

    //Region Private Methods

    private static class mInsertAsyncTask extends AsyncTask<cPackAndShipShipmentEntity, Void, Void> {
        private iPackAndShipShipmentDao mAsyncTaskDao;

        mInsertAsyncTask(iPackAndShipShipmentDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final cPackAndShipShipmentEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class mDeleteAsyncTask extends AsyncTask<cPackAndShipShipmentEntity, Void, Void> {
        private iPackAndShipShipmentDao mAsyncTaskDao;

        mDeleteAsyncTask(iPackAndShipShipmentDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final cPackAndShipShipmentEntity... params) {
            mAsyncTaskDao.delete(params[0]);
            return null;
        }
    }

    private static class mDeleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private iPackAndShipShipmentDao mAsyncTaskDao;

        mDeleteAllAsyncTask(iPackAndShipShipmentDao dao) {
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

