package SSU_WHS.Picken.SalesOrderPackingTable;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;
import java.util.concurrent.ExecutionException;

import SSU_WHS.General.acScanSuiteDatabase;

public class cSalesOrderPackingTableRepository {
    private iSalesOrderPackingTableDao salesOrderPackingTableDao;

    cSalesOrderPackingTableRepository(Application application) {
        acScanSuiteDatabase db = acScanSuiteDatabase.getDatabase(application);
        salesOrderPackingTableDao = db.salesOrderPackingTableDao();
    }

    public List<cSalesOrderPackingTableEntity> getAll() {
        List<cSalesOrderPackingTableEntity> salesOrderPackingTableEntities = null;
        try {
            salesOrderPackingTableEntities = new getAllAsyncTask(salesOrderPackingTableDao).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return salesOrderPackingTableEntities;
    }
    private static class getAllAsyncTask extends AsyncTask<Void, Void, List<cSalesOrderPackingTableEntity>> {
        private iSalesOrderPackingTableDao mAsyncTaskDao;

        getAllAsyncTask(iSalesOrderPackingTableDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected List<cSalesOrderPackingTableEntity> doInBackground(final Void... params) {
            return mAsyncTaskDao.getAll();
        }
    }
    public void deleteAll () {
        new deleteAllAsyncTask(salesOrderPackingTableDao).execute();
    }

    private static class deleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private iSalesOrderPackingTableDao mAsyncTaskDao;

        deleteAllAsyncTask(iSalesOrderPackingTableDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final Void... params) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }
    public void insert (cSalesOrderPackingTableEntity salesOrderPackingTableEntity) {
        new insertAsyncTask(salesOrderPackingTableDao).execute(salesOrderPackingTableEntity);
    }
    private static class insertAsyncTask extends AsyncTask<cSalesOrderPackingTableEntity, Void, Void> {
        private iSalesOrderPackingTableDao mAsyncTaskDao;

        insertAsyncTask(iSalesOrderPackingTableDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final cSalesOrderPackingTableEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
    public cSalesOrderPackingTableEntity getPackingTableForSalesorder(String pv_salesorder) {
        cSalesOrderPackingTableEntity salesOrderPackingTableEntity = null;
        try {
            salesOrderPackingTableEntity = new getPackingTableForSalesorderAsyncTask(salesOrderPackingTableDao).execute(pv_salesorder).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return salesOrderPackingTableEntity;
    }
    private static class getPackingTableForSalesorderAsyncTask extends AsyncTask<String, Void, cSalesOrderPackingTableEntity> {
        private iSalesOrderPackingTableDao mAsyncTaskDao;

        getPackingTableForSalesorderAsyncTask(iSalesOrderPackingTableDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected cSalesOrderPackingTableEntity doInBackground(final String... params) {
            return mAsyncTaskDao.getPackingTableForSalesOrder(params[0]);
        }
    }
    public cSalesOrderPackingTableEntity getSalesorderForPackingTable(String pv_packingtable) {
        cSalesOrderPackingTableEntity salesOrderPackingTableEntity = null;
        try {
            salesOrderPackingTableEntity = new getSalesorderForPackingTableAsyncTask(salesOrderPackingTableDao).execute(pv_packingtable).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return salesOrderPackingTableEntity;
    }
    private static class getSalesorderForPackingTableAsyncTask extends AsyncTask<String, Void, cSalesOrderPackingTableEntity> {
        private iSalesOrderPackingTableDao mAsyncTaskDao;

        getSalesorderForPackingTableAsyncTask(iSalesOrderPackingTableDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected cSalesOrderPackingTableEntity doInBackground(final String... params) {
            return mAsyncTaskDao.getSalesorderForPackingTable(params[0]);
        }
    }
}
