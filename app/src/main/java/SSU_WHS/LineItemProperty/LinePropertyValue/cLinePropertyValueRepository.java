package SSU_WHS.LineItemProperty.LinePropertyValue;

import android.app.Application;
import android.os.AsyncTask;

import SSU_WHS.General.acScanSuiteDatabase;

public class cLinePropertyValueRepository {
    //Region Public Properties
    private final iLinePropertyValueDao linePropertyValueDao;
    //End Region Public Properties

    //End Region Private Properties

    //Region Constructor
    public cLinePropertyValueRepository(Application pvApplication) {
        //Region Private Properties
        acScanSuiteDatabase db = acScanSuiteDatabase.pGetDatabase(pvApplication);
        this.linePropertyValueDao = db.linePropertyValueDao();
    }
    //End Region Constructor

    //Region Public Methods

    public void pInsert(cLinePropertyValueEntity linePropertyValueEntity) {
        new mInsertInDatabaseAsyncTask(this.linePropertyValueDao).execute(linePropertyValueEntity);
    }

    public void pDelete(cLinePropertyValueEntity linePropertyValueEntity) {
        new mDeleteAsyncTask(this.linePropertyValueDao).execute(linePropertyValueEntity);
    }

    public void pTruncate() {
        new deleteAllAsyncTask(this.linePropertyValueDao).execute();
    }

    //End Region Public Methods

    private static class mInsertInDatabaseAsyncTask extends AsyncTask<cLinePropertyValueEntity, Void, Void> {
        private iLinePropertyValueDao mAsyncTaskDao;

        mInsertInDatabaseAsyncTask(iLinePropertyValueDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final cLinePropertyValueEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class mDeleteAsyncTask extends AsyncTask<cLinePropertyValueEntity, Void, Void> {
        private iLinePropertyValueDao mAsyncTaskDao;

        mDeleteAsyncTask(iLinePropertyValueDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected Void doInBackground(final cLinePropertyValueEntity... params) {
            mAsyncTaskDao.delete(params[0]);
            return null;
        }
    }

    private static class deleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private iLinePropertyValueDao mAsyncTaskDao;

        deleteAllAsyncTask(iLinePropertyValueDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final Void... params) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }
}
