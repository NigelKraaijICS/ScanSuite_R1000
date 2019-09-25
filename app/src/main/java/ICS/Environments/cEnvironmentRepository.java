package ICS.Environments;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;
import java.util.concurrent.ExecutionException;

import ICS.acICSDatabase;

public class cEnvironmentRepository {

    //Region Public Properties
    public iEnvironmentDao environmentDao;
    //End Region Public Properties

    //Region Private Properties
    private acICSDatabase db;
    //End Region Private Properties

    //Region Constructor
    cEnvironmentRepository(Application pvApplication) {
        this.db = acICSDatabase.getDatabase(pvApplication);
        this.environmentDao = db.environmentDao();
    }
    //End Region Constructor

    //Region Public Methods
    public List<cEnvironmentEntity> getAllEnviromentsFromDatabase() {
        List<cEnvironmentEntity> environmentEntities = null;
        try {
            environmentEntities = new getAllEnviromentsFromDatabaseAsyncTask(environmentDao).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return environmentEntities;
    }

    public void insert(cEnvironmentEntity environmentEntity) {
        new insertAsyncTask(environmentDao).execute(environmentEntity);
    }

    public void deleteAll () {
        new deleteAllAsyncTask(environmentDao).execute();
    }

    public void delete(cEnvironmentEntity environmentEntity) {
        new deleteAsyncTask(environmentDao).execute(environmentEntity);
    }

    public int updateDefault(Boolean pvDefaultBln, String pvNameStr) {
        Integer integerValue = 0;
        UpdateDefaultParams updateDefaultParams = new UpdateDefaultParams(pvDefaultBln, pvNameStr);
        try {
            integerValue = new updateDefaultBlnAsyncTask(environmentDao).execute(updateDefaultParams).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return integerValue;
    }

    //End Region Private Methods


    private static class getAllEnviromentsFromDatabaseAsyncTask extends AsyncTask<Void, Void, List<cEnvironmentEntity>> {
        private iEnvironmentDao mAsyncTaskDao;

        getAllEnviromentsFromDatabaseAsyncTask(iEnvironmentDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected List<cEnvironmentEntity> doInBackground(final Void... params) {
            return mAsyncTaskDao.getAll();
        }
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

    private static class deleteAsyncTask extends AsyncTask<cEnvironmentEntity, Void, Void> {
        private iEnvironmentDao mAsyncTaskDao;

        deleteAsyncTask(iEnvironmentDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected Void doInBackground(final cEnvironmentEntity... params) {
            mAsyncTaskDao.delete(params[0]);
            return null;
        }
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

    private static class UpdateDefaultParams {
        Boolean defaultBln;
        String namestr;

        UpdateDefaultParams(Boolean pvDefaultBln, String pvNameStr) {
            this.defaultBln = pvDefaultBln;
            this.namestr = pvNameStr;
        }

    }

    private static class updateDefaultBlnAsyncTask extends AsyncTask<UpdateDefaultParams, Void, Integer> {
        private iEnvironmentDao mAsyncTaskDao;
        updateDefaultBlnAsyncTask(iEnvironmentDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected Integer doInBackground(UpdateDefaultParams... params) {
            return mAsyncTaskDao.updateDefault(params[0].defaultBln, params[0].namestr);
        }
    }

}
