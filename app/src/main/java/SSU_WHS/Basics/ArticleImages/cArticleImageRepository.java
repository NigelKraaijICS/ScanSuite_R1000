package SSU_WHS.Basics.ArticleImages;

import android.app.Application;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import SSU_WHS.General.acScanSuiteDatabase;
import SSU_WHS.Webservice.cWebresult;

public class cArticleImageRepository {
    //Region Public Properties

    public iArticleImageDao articleImageDao;
    //End Region Public Properties

    //Region Private Properties
    private acScanSuiteDatabase db;

    //End Region Private Properties

    //Region Constructor
    cArticleImageRepository(Application pvApplication) {
        this.db = acScanSuiteDatabase.getDatabase(pvApplication);
        this.articleImageDao = db.articleImageDao();
    }
    //End Region Constructor

    //Region Public Methods

    public void insert (cArticleImageEntity articleImageEntity) {
        new insertAsyncTask(articleImageDao).execute(articleImageEntity);
    }

    public void deleteAll () {
        new cArticleImageRepository.deleteAllAsyncTask(articleImageDao).execute();
    }

    //End Region Public Methods

    //Region Private Methods

    //End Region Private Methods

    private static class deleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private iArticleImageDao mAsyncTaskDao;

        deleteAllAsyncTask(iArticleImageDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final Void... params) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }

    private static class insertAsyncTask extends AsyncTask<cArticleImageEntity, Void, Void> {
        private iArticleImageDao mAsyncTaskDao;

        insertAsyncTask(iArticleImageDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final cArticleImageEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

}
