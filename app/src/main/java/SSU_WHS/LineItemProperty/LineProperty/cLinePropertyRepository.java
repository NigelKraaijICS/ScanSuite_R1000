package SSU_WHS.LineItemProperty.LineProperty;

import android.app.Application;
import android.os.AsyncTask;

import SSU_WHS.General.acScanSuiteDatabase;

public class cLinePropertyRepository {
    //Region Public Properties
    private final iLinePropertyDao linePropertyDao;
    //End Region Public Properties

    //End Region Private Properties

    //Region Constructor
    public cLinePropertyRepository(Application pvApplication) {
        //Region Private Properties
        acScanSuiteDatabase db = acScanSuiteDatabase.pGetDatabase(pvApplication);
        this.linePropertyDao = db.linePropertyDao();
    }
    //End Region Constructor

    //Region Public Methods

    public void pInsert(cLinePropertyEntity linePropertyEntity) {
        new mInsertInDatabaseAsyncTask(this.linePropertyDao).execute(linePropertyEntity);
    }

    public void pDelete(cLinePropertyEntity linePropertyEntity) {
        new mDeleteAsyncTask(this.linePropertyDao).execute(linePropertyEntity);
    }

    public void pTruncate() {
        new deleteAllAsyncTask(this.linePropertyDao).execute();
    }

    //End Region Public Methods

    private static class mInsertInDatabaseAsyncTask extends AsyncTask<cLinePropertyEntity, Void, Void> {
        private iLinePropertyDao mAsyncTaskDao;

        mInsertInDatabaseAsyncTask(iLinePropertyDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final cLinePropertyEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class mDeleteAsyncTask extends AsyncTask<cLinePropertyEntity, Void, Void> {
        private iLinePropertyDao mAsyncTaskDao;

        mDeleteAsyncTask(iLinePropertyDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected Void doInBackground(final cLinePropertyEntity... params) {
            mAsyncTaskDao.delete(params[0]);
            return null;
        }
    }

    private static class deleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private iLinePropertyDao mAsyncTaskDao;

        deleteAllAsyncTask(iLinePropertyDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final Void... params) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }
}
