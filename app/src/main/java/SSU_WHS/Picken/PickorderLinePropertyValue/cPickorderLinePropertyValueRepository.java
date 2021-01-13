package SSU_WHS.Picken.PickorderLinePropertyValue;

import android.app.Application;
import android.os.AsyncTask;

import SSU_WHS.General.acScanSuiteDatabase;
import SSU_WHS.Picken.PickorderLineProperty.cPickorderLinePropertyEntity;
import SSU_WHS.Picken.PickorderLineProperty.iPickorderLinePropertyDao;

public class cPickorderLinePropertyValueRepository {

    //Region Public Properties
    private iPickorderLinePropertyValueDao pickorderLinePropertyValueDao;
    //End Region Public Properties

    //End Region Private Properties

    //Region Constructor
    public cPickorderLinePropertyValueRepository(Application pvApplication) {
        //Region Private Properties
        acScanSuiteDatabase db = acScanSuiteDatabase.pGetDatabase(pvApplication);
        this.pickorderLinePropertyValueDao = db.pickorderLinePropertyValueDao();
    }
    //End Region Constructor

    //Region Public Methods

    public void pInsert(cPickorderLinePropertyValueEntity pvPickorderLinePropertyValueEntity) {
        new mInsertInDatabaseAsyncTask(this.pickorderLinePropertyValueDao).execute(pvPickorderLinePropertyValueEntity);
    }

    public void pDelete(cPickorderLinePropertyValueEntity pvPickorderLinePropertyValueEntity) {
        new mDeleteAsyncTask(this.pickorderLinePropertyValueDao).execute(pvPickorderLinePropertyValueEntity);
    }

    public void pTruncate() {
        new cPickorderLinePropertyValueRepository.deleteAllAsyncTask(this.pickorderLinePropertyValueDao).execute();
    }

    //End Region Public Methods

    private static class mInsertInDatabaseAsyncTask extends AsyncTask<cPickorderLinePropertyValueEntity, Void, Void> {
        private iPickorderLinePropertyValueDao mAsyncTaskDao;

        mInsertInDatabaseAsyncTask(iPickorderLinePropertyValueDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final cPickorderLinePropertyValueEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class mDeleteAsyncTask extends AsyncTask<cPickorderLinePropertyValueEntity, Void, Void> {
        private iPickorderLinePropertyValueDao mAsyncTaskDao;

        mDeleteAsyncTask(iPickorderLinePropertyValueDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected Void doInBackground(final cPickorderLinePropertyValueEntity... params) {
            mAsyncTaskDao.deletePickorder(params[0]);
            return null;
        }
    }

    private static class deleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private iPickorderLinePropertyValueDao mAsyncTaskDao;

        deleteAllAsyncTask(iPickorderLinePropertyValueDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final Void... params) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }



}
