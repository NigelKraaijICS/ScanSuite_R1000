package SSU_WHS.PackAndShip.PackAndShipLines;

import android.app.Application;
import android.os.AsyncTask;
import SSU_WHS.General.acScanSuiteDatabase;


public class cPackAndShipOrderLineRepository {



    //Region Public Properties
    private final iPackAndShipOrderLineDao packAndShipOrderLineDao;
    //End Region Public Properties

    //Region Constructor
    cPackAndShipOrderLineRepository(Application pvApplication) {
        //Region Private Properties
        acScanSuiteDatabase db = acScanSuiteDatabase.pGetDatabase(pvApplication);
        this.packAndShipOrderLineDao = db.packAndShipOrderLineDao();
    }
    //End Region Constructor

    //Region Public Methods

    public void insert(cPackAndShipOrderLineEntity pvPackAndShipOrderLineEntity) {
        new mInsertAsyncTask(packAndShipOrderLineDao).execute(pvPackAndShipOrderLineEntity);
    }

    public void delete(cPackAndShipOrderLineEntity pvPackAndShipOrderLineEntity) {
        new mDeleteAsyncTask(packAndShipOrderLineDao).execute(pvPackAndShipOrderLineEntity);
    }

    public void deleteAll() {
        new mDeleteAllAsyncTask(packAndShipOrderLineDao).execute();
    }

    //End Region Public Methods

    //Region Private Methods

    private static class mInsertAsyncTask extends AsyncTask<cPackAndShipOrderLineEntity, Void, Void> {
        private final iPackAndShipOrderLineDao mAsyncTaskDao;

        mInsertAsyncTask(iPackAndShipOrderLineDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final cPackAndShipOrderLineEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class mDeleteAsyncTask extends AsyncTask<cPackAndShipOrderLineEntity, Void, Void> {
        private final iPackAndShipOrderLineDao mAsyncTaskDao;

        mDeleteAsyncTask(iPackAndShipOrderLineDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final cPackAndShipOrderLineEntity... params) {
            mAsyncTaskDao.delete(params[0]);
            return null;
        }
    }

    private static class mDeleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private final iPackAndShipOrderLineDao mAsyncTaskDao;

        mDeleteAllAsyncTask(iPackAndShipOrderLineDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Void... params) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }


    //End Region Private Methods


}

