package SSU_WHS.PackAndShip.PackAndShipSetting;

import android.app.Application;
import android.os.AsyncTask;

import SSU_WHS.General.acScanSuiteDatabase;

public class cPackAndShipSettingRepository {



    //Region Public Properties
    private iPackAndShipSettingDao packAndShipSettingDao;
    //End Region Public Properties

    //Region Constructor
    cPackAndShipSettingRepository(Application pvApplication) {
        //Region Private Properties
        acScanSuiteDatabase db = acScanSuiteDatabase.pGetDatabase(pvApplication);
        this.packAndShipSettingDao = db.packAndShipSettingDao();
    }
    //End Region Constructor

    //Region Public Methods

    public void insert(cPackAndShipSettingEntity pvPackAndShipSettingEntity) {
        new mInsertAsyncTask(packAndShipSettingDao).execute(pvPackAndShipSettingEntity);
    }

    public void delete(cPackAndShipSettingEntity pvPackAndShipSettingEntity) {
        new mDeleteAsyncTask(packAndShipSettingDao).execute(pvPackAndShipSettingEntity);
    }

    public void deleteAll() {
        new mDeleteAllAsyncTask(packAndShipSettingDao).execute();
    }

    //End Region Public Methods

    //Region Private Methods

    private static class mInsertAsyncTask extends AsyncTask<cPackAndShipSettingEntity, Void, Void> {
        private iPackAndShipSettingDao mAsyncTaskDao;

        mInsertAsyncTask(iPackAndShipSettingDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final cPackAndShipSettingEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class mDeleteAsyncTask extends AsyncTask<cPackAndShipSettingEntity, Void, Void> {
        private iPackAndShipSettingDao mAsyncTaskDao;

        mDeleteAsyncTask(iPackAndShipSettingDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final cPackAndShipSettingEntity... params) {
            mAsyncTaskDao.delete(params[0]);
            return null;
        }
    }

    private static class mDeleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private iPackAndShipSettingDao mAsyncTaskDao;

        mDeleteAllAsyncTask(iPackAndShipSettingDao dao) {
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

