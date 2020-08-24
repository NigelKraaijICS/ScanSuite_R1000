package SSU_WHS.Picken.PickorderSetting;

import android.app.Application;
import android.os.AsyncTask;

import SSU_WHS.General.acScanSuiteDatabase;
import SSU_WHS.Picken.PickorderAddresses.cPickorderAddressEntity;
import SSU_WHS.Picken.PickorderAddresses.iPickorderAddressDao;

public class cPickorderSettingRepository {

    private iPickorderSettingDao pickorderSettingDao;

    //Region Private Properties

    //End Region Private Properties

    cPickorderSettingRepository(Application pvApplication) {
        acScanSuiteDatabase db= acScanSuiteDatabase.pGetDatabase(pvApplication);
        this.pickorderSettingDao = db.pickorderSettingDao();
    }

    //Region Public Methods

    public void pDeleteAll() {
        new deleteAllAsyncTask(pickorderSettingDao).execute();
    }

    public void pInsert(cPickorderSettingEntity pickorderSettingEntity){
        new insertAsyncTask(pickorderSettingDao).execute(pickorderSettingEntity);
    }
    //End Region Public Methods

    //Region Private Methods

    private static class insertAsyncTask extends AsyncTask<cPickorderSettingEntity, Void, Void> {
        private iPickorderSettingDao mAsyncTaskDao;

        insertAsyncTask(iPickorderSettingDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final cPickorderSettingEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class deleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private iPickorderSettingDao mAsyncTaskDao;

        deleteAllAsyncTask(iPickorderSettingDao dao) {
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
