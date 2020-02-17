package ICS.Weberror;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;
import java.util.concurrent.ExecutionException;

import ICS.acICSDatabase;

public class cWeberrorRepository {

    //Region Public Properties
    public iWeberrorDao weberrorDao;
    //End Region Public Properties

    //Region Private Properties
    private acICSDatabase db;
    //End Region Private Properties

    //Region Constructor
    cWeberrorRepository(Application pvApplication) {
        this.db = acICSDatabase.getDatabase(pvApplication);
        this.weberrorDao = db.weberrorDao();
    }
    //End Region Constructor

    //Region Public Methods
    public List<cWeberrorEntity> getAllWebErrorsFromDatabase() {
        List<cWeberrorEntity> weberrorEntities = null;
        try {
            weberrorEntities = new getAllWebErrorsFromDatabaseAsyncTask(weberrorDao).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return weberrorEntities;
    }


    public List<cWeberrorEntity> getAllWebErrorsForActivityFromDatabase(String pvActivityStr) {
        GetAllForActivityParams getAllForActivityParams = new GetAllForActivityParams(pvActivityStr);
        List<cWeberrorEntity> weberrorEntities = null;
        try {
            weberrorEntities = new getAllForActivity (weberrorDao).execute(getAllForActivityParams).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return weberrorEntities;
    }

    public List<cWeberrorEntity> getAllForActivity(String pvStatusStr) {
        List<cWeberrorEntity> weberrorEntities = null;
        try {
            weberrorEntities = new getAllForActivityAsyncTask(weberrorDao).execute(pvStatusStr).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return weberrorEntities;
    }

    public List<cWeberrorEntity> getAllByStatus(String pvStatusStr) {
        List<cWeberrorEntity> weberrorEntities = null;
        try {
            weberrorEntities = new getAllByStatusAsyncTask(weberrorDao).execute(pvStatusStr).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return weberrorEntities;
    }

    public List<cWeberrorEntity> getAllForWebMethodStr(String pvMethodStr) {
        List<cWeberrorEntity> weberrorEntities = null;
        try {
            weberrorEntities = new getAllByMethodAsyncTask(weberrorDao).execute(pvMethodStr).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return weberrorEntities;
    }

    public List<cWeberrorEntity> getAllForActivityAndStatus(String activity, String status) {
        GetAllForActivityAndStatusParams getAllForActivityAndStatusLiveParams = new GetAllForActivityAndStatusParams(activity, status);
        List<cWeberrorEntity> weberrorEntities = null;
        try {
            weberrorEntities = new getAllForActivityAndStatus(weberrorDao).execute(getAllForActivityAndStatusLiveParams).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return weberrorEntities;
    }

    public void deleteAllForActivity(String activity) {
        new deleteAllForActivityAsyncTask(weberrorDao).execute(activity);
    }

    public void insert (cWeberrorEntity weberrorEntity) {
        new insertAsyncTask(weberrorDao).execute(weberrorEntity);
    }

    public void deleteAll () {

        new    cWeberrorRepository.deleteAllAsyncTask(weberrorDao).execute();
    }

    public void delete(cWeberrorEntity weberrorEntity) {
        new deleteAsyncTask(weberrorDao).execute(weberrorEntity);
    }

    //End Region Public Methods

    //Region Private Methods
    private static class deleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private iWeberrorDao mAsyncTaskDao;

        deleteAllAsyncTask(iWeberrorDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final Void... params) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }

    private static class insertAsyncTask extends AsyncTask<cWeberrorEntity, Void, Void> {
        private iWeberrorDao mAsyncTaskDao;

        insertAsyncTask(iWeberrorDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final cWeberrorEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class deleteAsyncTask extends AsyncTask<cWeberrorEntity, Void, Void> {
        private iWeberrorDao mAsyncTaskDao;

        deleteAsyncTask(iWeberrorDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected Void doInBackground(final cWeberrorEntity... params) {
            mAsyncTaskDao.delete(params[0]);
            return null;
        }
    }

    private static class getAllWebErrorsFromDatabaseAsyncTask extends AsyncTask<Void, Void, List<cWeberrorEntity>> {
        private iWeberrorDao mAsyncTaskDao;

        getAllWebErrorsFromDatabaseAsyncTask(iWeberrorDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected List<cWeberrorEntity> doInBackground(final Void... params) {
            return mAsyncTaskDao.getAll();
        }
    }

    private static class getAllForActivity extends AsyncTask<GetAllForActivityParams, Void, List<cWeberrorEntity>> {
        private iWeberrorDao mAsyncTaskDao;

        getAllForActivity(iWeberrorDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected List<cWeberrorEntity> doInBackground(final GetAllForActivityParams... params) {
            return mAsyncTaskDao.getAllForActivity(params[0].activityStr);
        }
    }


    private static class getAllForActivityAsyncTask extends AsyncTask<String, Void, List<cWeberrorEntity>> {
        private iWeberrorDao mAsyncTaskDao;

        getAllForActivityAsyncTask(iWeberrorDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected List<cWeberrorEntity> doInBackground(final String... params) {
            return mAsyncTaskDao.getAllForActivity(params[0]);
        }
    }

    private static class getAllByMethodAsyncTask extends AsyncTask<String, Void, List<cWeberrorEntity>> {
        private iWeberrorDao mAsyncTaskDao;

        getAllByMethodAsyncTask(iWeberrorDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected List<cWeberrorEntity> doInBackground(final String... params) {
            return mAsyncTaskDao.getAllForWebMethod(params[0]);
        }
    }

    private static class getAllByStatusAsyncTask extends AsyncTask<String, Void, List<cWeberrorEntity>> {
        private iWeberrorDao mAsyncTaskDao;

        getAllByStatusAsyncTask(iWeberrorDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected List<cWeberrorEntity> doInBackground(final String... params) {
            return mAsyncTaskDao.getAllByStatus(params[0]);
        }
    }

    private static class getAllForActivityAndStatus extends AsyncTask<GetAllForActivityAndStatusParams, Void, List<cWeberrorEntity>> {
        private iWeberrorDao mAsyncTaskDao;

        getAllForActivityAndStatus(iWeberrorDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected List<cWeberrorEntity> doInBackground(final GetAllForActivityAndStatusParams... params) {
            return mAsyncTaskDao.getAllForActivityAndStatus(params[0].activityStr, params[0].statusStr);
        }
    }
    private static class deleteAllForActivityAsyncTask extends AsyncTask<String, Void, Void> {
        private iWeberrorDao mAsyncTaskDao;

        deleteAllForActivityAsyncTask(iWeberrorDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final String... params) {
            mAsyncTaskDao.deleteAllForActivity(params[0]);
            return null;
        }
    }

    private static class GetAllForActivityAndStatusParams {
        String activityStr;
        String statusStr;

        GetAllForActivityAndStatusParams(String pvActivityStr, String pvStatusStr) {
            this.activityStr = pvActivityStr;
            this.statusStr = pvStatusStr;
        }
    }

    private static class GetAllForActivityParams {
        String activityStr;

        GetAllForActivityParams(String pvActivityStr) {
            this.activityStr = pvActivityStr;
        }
    }


    //End Region Private Methods

}
