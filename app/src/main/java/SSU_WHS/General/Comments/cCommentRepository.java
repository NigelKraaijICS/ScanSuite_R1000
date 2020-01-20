package SSU_WHS.General.Comments;

import android.app.Application;
import android.os.AsyncTask;

import SSU_WHS.General.acScanSuiteDatabase;

public class cCommentRepository {
    //Public properties
    public iCommentDao commentDao;
    //End public properties

    //Private Properties
    private acScanSuiteDatabase db;
    //End Private Properties

    //Constructor
    cCommentRepository(Application pvApplication) {
        this.db = acScanSuiteDatabase.pGetDatabase(pvApplication);
        this.commentDao = db.commentDao();
    }
    //End Constructor

    //Public Methods
    public void pInsert(cCommentEntity pvCommentEntity) {
        new insertAsyncTask(commentDao).execute(pvCommentEntity);
    }

    public void pDeleteAll() {
        new deleteAllAsyncTask(commentDao).execute();
    }


    //End Public Methods

    //Private Methods
    private static class insertAsyncTask extends AsyncTask<cCommentEntity, Void, Void> {
        private iCommentDao mAsyncTaskDao;

        insertAsyncTask(iCommentDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final cCommentEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class deleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private iCommentDao mAsyncTaskDao;

        deleteAllAsyncTask(iCommentDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final Void... params) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }


}
