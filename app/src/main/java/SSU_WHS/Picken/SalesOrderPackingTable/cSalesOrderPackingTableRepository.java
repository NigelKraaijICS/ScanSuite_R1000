package SSU_WHS.Picken.SalesOrderPackingTable;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;
import java.util.concurrent.ExecutionException;

import SSU_WHS.General.acScanSuiteDatabase;

public class cSalesOrderPackingTableRepository {

    //Region Public Properties
    private iSalesOrderPackingTableDao salesOrderPackingTableDao;
    //End Region Public Properties

    //End Region Private Properties

    //Region Constructor
    cSalesOrderPackingTableRepository(Application pvApplication) {
        //Region Private Properties
        acScanSuiteDatabase db = acScanSuiteDatabase.pGetDatabase(pvApplication);
        this.salesOrderPackingTableDao = db.salesOrderPackingTableDao();
    }
    //End Region Constructor

    //Region Public Methods

    public void insert (cSalesOrderPackingTableEntity salesOrderPackingTableEntity) {
        new insertAsyncTask(salesOrderPackingTableDao).execute(salesOrderPackingTableEntity);
    }

    public void delete (String pvPackingTableStr) {
        new deleteAsyncTask(salesOrderPackingTableDao).execute(pvPackingTableStr);
    }

    public List<cSalesOrderPackingTableEntity> pGetAllSalesOrderPackingTablesFromDatabaseObl() {

        List<cSalesOrderPackingTableEntity> resultObl = null;
        try {
            resultObl = new mGetAllSalesOrderPackingTablesFromDatabaseAsyncTask(salesOrderPackingTableDao).execute().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return resultObl;
    }

    public void pTruncateTable() {
        new mTruncateTableAsyncTask(salesOrderPackingTableDao).execute();
    }

    //End Region Public Methods

    // Region Private Methods

    private static class mTruncateTableAsyncTask extends AsyncTask<Void, Void, Void> {
        private iSalesOrderPackingTableDao mAsyncTaskDao;

        mTruncateTableAsyncTask(iSalesOrderPackingTableDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final Void... params) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }

    private static class mGetAllSalesOrderPackingTablesFromDatabaseAsyncTask extends AsyncTask<Void, Void, List<cSalesOrderPackingTableEntity>> {
        private iSalesOrderPackingTableDao mAsyncTaskDao;

        mGetAllSalesOrderPackingTablesFromDatabaseAsyncTask(iSalesOrderPackingTableDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected List<cSalesOrderPackingTableEntity> doInBackground(final Void... params) {
            return mAsyncTaskDao.getAll();
        }
    }


    // End Region Private Methods


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


    private static class deleteAsyncTask extends AsyncTask<String, Void, Void> {
        private iSalesOrderPackingTableDao mAsyncTaskDao;

        deleteAsyncTask(iSalesOrderPackingTableDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final String... params) {
            mAsyncTaskDao.delete(params[0]);
            return null;
        }
    }


}
