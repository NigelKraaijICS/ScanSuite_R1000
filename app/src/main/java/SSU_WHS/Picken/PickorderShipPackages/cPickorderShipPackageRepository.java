package SSU_WHS.Picken.PickorderShipPackages;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;
import java.util.concurrent.ExecutionException;

import SSU_WHS.General.acScanSuiteDatabase;

public class cPickorderShipPackageRepository {
    private iPickorderShipPackageDao pickorderShipPackageDao;

    cPickorderShipPackageRepository(Application application) {
        acScanSuiteDatabase db = acScanSuiteDatabase.pGetDatabase(application);
        pickorderShipPackageDao = db.pickorderShipPackageDao();
    }

    public List<cPickorderShipPackageEntity> getAll() {
        List<cPickorderShipPackageEntity> pickorderShipPackageEntities = null;
        try {
            pickorderShipPackageEntities = new getAllAsyncTask(pickorderShipPackageDao).execute().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return pickorderShipPackageEntities;
    }
    private static class getAllAsyncTask extends AsyncTask<Void, Void, List<cPickorderShipPackageEntity>> {
        private iPickorderShipPackageDao mAsyncTaskDao;

        getAllAsyncTask(iPickorderShipPackageDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected List<cPickorderShipPackageEntity> doInBackground(final Void... params) {
            return mAsyncTaskDao.getAll();
        }
    }
    public void deleteAll () {
        new deleteAllAsyncTask(pickorderShipPackageDao).execute();
    }

    private static class deleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private iPickorderShipPackageDao mAsyncTaskDao;

        deleteAllAsyncTask(iPickorderShipPackageDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final Void... params) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }
    public void insert(cPickorderShipPackageEntity pickorderShipPackageEntity) {
        new insertAsyncTask(pickorderShipPackageDao).execute(pickorderShipPackageEntity);
    }
    private static class insertAsyncTask extends AsyncTask<cPickorderShipPackageEntity, Void, Void> {
        private iPickorderShipPackageDao mAsyncTaskDao;

        insertAsyncTask(iPickorderShipPackageDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final cPickorderShipPackageEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

}
