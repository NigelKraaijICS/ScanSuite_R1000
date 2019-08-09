package ICS.Environments;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;
import java.util.concurrent.ExecutionException;

import SSU_WHS.Webservice.cWebresult;
import ICS.acICSDatabase;

public class cEnvironmentRepository {
    private iEnvironmentDao environmentDao;
    private cWebresult webResult;

    cEnvironmentRepository(Application application) {
        acICSDatabase db = acICSDatabase.getDatabase(application);
        environmentDao = db.environmentDao();
    }

    public List<cEnvironmentEntity> getAll() {
        List<cEnvironmentEntity> environmentEntities = null;
        try {
            environmentEntities = new getAllAsyncTask(environmentDao).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return environmentEntities;
    }
    private static class getAllAsyncTask extends AsyncTask<Void, Void, List<cEnvironmentEntity>> {
        private iEnvironmentDao mAsyncTaskDao;

        getAllAsyncTask(iEnvironmentDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected List<cEnvironmentEntity> doInBackground(final Void... params) {
            return mAsyncTaskDao.getAll();
        }
    }

    public cEnvironmentEntity getFirst() {
        cEnvironmentEntity environmentEntity = null;
        try {
            environmentEntity = new getFirstAsyncTask(environmentDao).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return environmentEntity;
    }
    private static class getFirstAsyncTask extends AsyncTask<Void, Void, cEnvironmentEntity> {
        private iEnvironmentDao mAsyncTaskDao;

        getFirstAsyncTask(iEnvironmentDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected cEnvironmentEntity doInBackground(final Void... params) {
            return mAsyncTaskDao.getFirst();
        }
    }

    public void deleteAll () {
        new deleteAllAsyncTask(environmentDao).execute();
    }

    private static class deleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private iEnvironmentDao mAsyncTaskDao;

        deleteAllAsyncTask(iEnvironmentDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final Void... params) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }
    public void delete(cEnvironmentEntity environmentEntity) {
        new deleteAsyncTask(environmentDao).execute(environmentEntity);
    }
    private static class deleteAsyncTask extends AsyncTask<cEnvironmentEntity, Void, Void> {
        private iEnvironmentDao mAsyncTaskDao;

        deleteAsyncTask(iEnvironmentDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected Void doInBackground(final cEnvironmentEntity... params) {
            mAsyncTaskDao.delete(params[0]);
            return null;
        }
    }


    public void insert(cEnvironmentEntity environmentEntity) {
        new insertAsyncTask(environmentDao).execute(environmentEntity);
    }
    private static class insertAsyncTask extends AsyncTask<cEnvironmentEntity, Void, Void> {
        private iEnvironmentDao mAsyncTaskDao;

        insertAsyncTask(iEnvironmentDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final cEnvironmentEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    public cEnvironmentEntity getEnvironmentByName(String name) {
        cEnvironmentEntity environmentEntity = null;
        try {
            environmentEntity = new getEnvironmentsByNameAsyncTask(environmentDao).execute(name).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return environmentEntity;
    }
    private static class getEnvironmentsByNameAsyncTask extends AsyncTask<String, Void, cEnvironmentEntity> {
        private iEnvironmentDao mAsyncTaskDao;

        getEnvironmentsByNameAsyncTask(iEnvironmentDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected cEnvironmentEntity doInBackground(final String... params) {
            return mAsyncTaskDao.getEnvironmentByName(params[0]);
        }
    }
    public cEnvironmentEntity getDefaultEnvironment() {
        cEnvironmentEntity environmentEntity = null;
        try {
            environmentEntity = new getDefaultEnvironmentAsyncTask(environmentDao).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return environmentEntity;
    }
    private static class getDefaultEnvironmentAsyncTask extends AsyncTask<Void, Void, cEnvironmentEntity> {
        private iEnvironmentDao mAsyncTaskDao;

        getDefaultEnvironmentAsyncTask(iEnvironmentDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected cEnvironmentEntity doInBackground(final Void... params) {
            return mAsyncTaskDao.getDefaultEnvironment();
        }
    }

    public int getNumberOfEnvironments() {
        Integer integerValue = 0;
        try {
            integerValue = new getNumberOfEnvironmentsAsyncTask(environmentDao).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return integerValue;
    }
    private static class getNumberOfEnvironmentsAsyncTask extends AsyncTask<Void, Void, Integer> {
        private iEnvironmentDao mAsyncTaskDao;
        getNumberOfEnvironmentsAsyncTask(iEnvironmentDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected Integer doInBackground(Void... params) {
            return mAsyncTaskDao.getNumberOfEnvironments();
        }
    }


}
