package SSU_WHS.Picken.PickorderLinePackAndShip;

import android.app.Application;
import android.os.AsyncTask;

import SSU_WHS.General.acScanSuiteDatabase;

public class cPickorderLinePackAndShipRepository {

    //Region Public Properties

    //Region Public Properties

    //Region Private Properties
    private iPickorderLinePackAndShipDao pickorderLinePackAndShipDao;
    private acScanSuiteDatabase db;

    private static class UpdateOrderlineQuantityParams {
        Integer recordIdint;
        Double quantityDbl;

        UpdateOrderlineQuantityParams(Integer pvRecordIDInt, Double pvQuantityDbl) {
            this.recordIdint = pvRecordIDInt;
            this.quantityDbl = pvQuantityDbl;
        }
    }


    private static class UpdateOrderlineLocaStatusParams {
        Integer recordIDInt;
        Integer newStatusInt;

        UpdateOrderlineLocaStatusParams(Integer pvRecordIDInt, Integer pvNewsStatusInt) {
            this.recordIDInt = pvRecordIDInt;
            this.newStatusInt = pvNewsStatusInt;
        }

    }

    //End Region Private Properties


    //Region Constructor
    cPickorderLinePackAndShipRepository(Application pvApplication) {
        this.db = acScanSuiteDatabase.pGetDatabase(pvApplication);
        this.pickorderLinePackAndShipDao = db.pickorderLinePackAndShipDao();
    }
    //End Region Constructor

    //Region Public Methods

    public void pDeleteAll() {
        new deleteAllAsyncTask(this.pickorderLinePackAndShipDao).execute();
    }

    public void pInsert(cPickorderLinePackAndShipEntity pickorderLinePackAndShipEntity){
        new insertAsyncTask(this.pickorderLinePackAndShipDao).execute(pickorderLinePackAndShipEntity);
    }


    public void deleteAll () {
        new deleteAllAsyncTask(pickorderLinePackAndShipDao).execute();
    }

    public void insert(cPickorderLinePackAndShipEntity pickorderLinePackAndShipEntity) {
        new insertAsyncTask(pickorderLinePackAndShipDao).execute(pickorderLinePackAndShipEntity);
    }

    //End Region Public Methods

    //Region Private Methods

    private static class deleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private iPickorderLinePackAndShipDao mAsyncTaskDao;

        deleteAllAsyncTask(iPickorderLinePackAndShipDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final Void... params) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }

    private static class insertAsyncTask extends AsyncTask<cPickorderLinePackAndShipEntity, Void, Void> {
        private iPickorderLinePackAndShipDao mAsyncTaskDao;

        insertAsyncTask(iPickorderLinePackAndShipDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final cPickorderLinePackAndShipEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }


    //End Region Private Methods






}
