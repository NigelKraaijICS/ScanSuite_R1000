package ICS.Weberror;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;
import java.util.concurrent.ExecutionException;

import ICS.acICSDatabase;

public class cWeberrorRepository {
    private iWeberrorDao weberrorDao;

    public cWeberrorRepository(Application application) {
        acICSDatabase db = acICSDatabase.getDatabase(application);
        weberrorDao = db.weberrorDao();
    }

    public void insert (cWeberrorEntity weberrorEntity) {
        new insertAsyncTask(weberrorDao).execute(weberrorEntity);
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
    public void delete(cWeberrorEntity weberrorEntity) {
        new deleteAsyncTask(weberrorDao).execute(weberrorEntity);
    }
    private static class deleteAsyncTask extends AsyncTask<cWeberrorEntity, Void, Void> {
        private iWeberrorDao mAsyncTaskDao;

        deleteAsyncTask(iWeberrorDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected Void doInBackground(final cWeberrorEntity... params) {
            mAsyncTaskDao.deleteWeberror(params[0]);
            return null;
        }
    }


    public void deleteAll () {
        new deleteAllAsyncTask(weberrorDao).execute();
    }

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
    public List<cWeberrorEntity> getAll() {
        List<cWeberrorEntity> weberrorEntities = null;
        try {
            weberrorEntities = new getAllAsyncTask(weberrorDao).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return weberrorEntities;
    }
    private static class getAllAsyncTask extends AsyncTask<Void, Void, List<cWeberrorEntity>> {
        private iWeberrorDao mAsyncTaskDao;

        getAllAsyncTask(iWeberrorDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected List<cWeberrorEntity> doInBackground(final Void... params) {
            return mAsyncTaskDao.getAll();
        }
    }

    public LiveData<List<cWeberrorEntity>> getAllLive() {
        LiveData<List<cWeberrorEntity>> weberrorEntities = null;
        try {
            weberrorEntities = new getAllLiveAsyncTask(weberrorDao).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return weberrorEntities;
    }
    private static class getAllLiveAsyncTask extends AsyncTask<Void, Void, LiveData<List<cWeberrorEntity>>> {
        private iWeberrorDao mAsyncTaskDao;

        getAllLiveAsyncTask(iWeberrorDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected LiveData<List<cWeberrorEntity>> doInBackground(final Void... params) {
            return mAsyncTaskDao.getAllLive();
        }
    }

    public LiveData<List<cWeberrorEntity>> getAllByStatusLive(String status) {
        LiveData<List<cWeberrorEntity>> weberrorEntities = null;
        try {
            weberrorEntities = new getAllByStatusLiveAsyncTask(weberrorDao).execute(status).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return weberrorEntities;
    }
    private static class getAllByStatusLiveAsyncTask extends AsyncTask<String, Void, LiveData<List<cWeberrorEntity>>> {
        private iWeberrorDao mAsyncTaskDao;

        getAllByStatusLiveAsyncTask(iWeberrorDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected LiveData<List<cWeberrorEntity>> doInBackground(final String... params) {
            return mAsyncTaskDao.getAllByStatusLive(params[0]);
        }
    }

    private static class GetAllForActivityAndStatusLiveParams {
        String activity;
        String status;

        GetAllForActivityAndStatusLiveParams(String activity, String status) {
            this.activity = activity;
            this.status = status;
        }
    }

    public LiveData<List<cWeberrorEntity>> getAllForActivityAndStatusLive(String activity, String status) {
        GetAllForActivityAndStatusLiveParams getAllForActivityAndStatusLiveParams = new GetAllForActivityAndStatusLiveParams(activity, status);
        LiveData<List<cWeberrorEntity>> weberrorEntities = null;
        try {
            weberrorEntities = new getAllForActivityAndStatusLive(weberrorDao).execute(getAllForActivityAndStatusLiveParams).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return weberrorEntities;
    }

    private static class getAllForActivityAndStatusLive extends AsyncTask<GetAllForActivityAndStatusLiveParams, Void, LiveData<List<cWeberrorEntity>>> {
        private iWeberrorDao mAsyncTaskDao;

        getAllForActivityAndStatusLive(iWeberrorDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected LiveData<List<cWeberrorEntity>> doInBackground(final GetAllForActivityAndStatusLiveParams... params) {
            return mAsyncTaskDao.getAllForActivityAndStatusLive(params[0].activity, params[0].status);
        }
    }

    public void deleteAllForActivity(String activity) {
        new deleteAllForActivityAsyncTask(weberrorDao).execute(activity);
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

    public List<cWeberrorEntity> getAllForActivity(String activity) {
        List<cWeberrorEntity> weberrorsForActivityEntities = null;
        try {
            weberrorsForActivityEntities = new getAllForActivityAsyncTask(weberrorDao).execute(activity).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return weberrorsForActivityEntities;
    }

    private static class getAllForActivityAsyncTask extends AsyncTask<String, Void, List<cWeberrorEntity>> {
        private iWeberrorDao mAsyncTaskDao;

        getAllForActivityAsyncTask(iWeberrorDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected List<cWeberrorEntity> doInBackground(final String... params) {
            return mAsyncTaskDao.getAllForActivity(params[0]);
        }
    }

    public LiveData<List<cWeberrorEntity>> getAllForActivityLive(String activity) {
        LiveData<List<cWeberrorEntity>> weberrorsForActivityEntities = null;
        try {
            weberrorsForActivityEntities = new getAllForActivityLiveAsyncTask(weberrorDao).execute(activity).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return weberrorsForActivityEntities;
    }

    private static class getAllForActivityLiveAsyncTask extends AsyncTask<String, Void, LiveData<List<cWeberrorEntity>>> {
        private iWeberrorDao mAsyncTaskDao;

        getAllForActivityLiveAsyncTask(iWeberrorDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected LiveData<List<cWeberrorEntity>> doInBackground(final String... params) {
            return mAsyncTaskDao.getAllForActivityLive(params[0]);
        }
    }
}
