package SSU_WHS.Inventory.InventoryorderLineBarcodes;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

import SSU_WHS.General.acScanSuiteDatabase;

public class cInventoryorderLineBarcodeRepository {
    //Public Properties

    private iInventoryorderLineBarcodeDao inventoryorderLineBarcodeDao;

    //End Public Properties

    //Private properties
    //End Private properties

    //Region Constructor
    cInventoryorderLineBarcodeRepository(Application pvApplication) {
        acScanSuiteDatabase db = acScanSuiteDatabase.pGetDatabase(pvApplication);
        this.inventoryorderLineBarcodeDao = db.inventoryorderLineBarcodeDao();
    }

    //End Region Constructor

    //Region public methods

    public void pInsert (cInventoryorderLineBarcodeEntity inventoryorderLineBarcodeEntity) {
        new insertAsyncTask(inventoryorderLineBarcodeDao).execute(inventoryorderLineBarcodeEntity);
    }

    public void insertAll(List<cInventoryorderLineBarcodeEntity> pvInventoryorderLineBarcodeEntities) {
        new mInsertAllAsyncTask(inventoryorderLineBarcodeDao).execute(pvInventoryorderLineBarcodeEntities);
    }

    public void pDelete (cInventoryorderLineBarcodeEntity inventoryorderLineBarcodeEntity) {
        new deleteAsyncTask(inventoryorderLineBarcodeDao).execute(inventoryorderLineBarcodeEntity);
    }


    public void pDeleteAll () {
        new deleteAllAsyncTask(inventoryorderLineBarcodeDao).execute();
    }


    public void updateBarcodeAmount(String pvBarcodeStr, double pvAmountDbl) {
        UpdateBarcodeAmountParams updateBarcodeAmountParams = new UpdateBarcodeAmountParams(pvBarcodeStr, pvAmountDbl);
        new updateBarcodeAmountAsyncTask(inventoryorderLineBarcodeDao).execute(updateBarcodeAmountParams);
    }


    //End Region Public Methods

    //Region Private methods


    private static class deleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private iInventoryorderLineBarcodeDao mAsyncTaskDao;

        deleteAllAsyncTask(iInventoryorderLineBarcodeDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final Void... params) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }

    private static class insertAsyncTask extends AsyncTask<cInventoryorderLineBarcodeEntity, Void, Void> {
        private iInventoryorderLineBarcodeDao mAsyncTaskDao;

        insertAsyncTask(iInventoryorderLineBarcodeDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final cInventoryorderLineBarcodeEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class mInsertAllAsyncTask extends AsyncTask<List<cInventoryorderLineBarcodeEntity>, Void, Void> {
        private iInventoryorderLineBarcodeDao mAsyncTaskDao;

        mInsertAllAsyncTask(iInventoryorderLineBarcodeDao dao) {
            mAsyncTaskDao = dao;
        }

        @SafeVarargs
        @Override
        protected final Void doInBackground(final List<cInventoryorderLineBarcodeEntity>... params) {
            mAsyncTaskDao.insertAll(params[0]);
            return null;
        }
    }


    private static class deleteAsyncTask extends AsyncTask<cInventoryorderLineBarcodeEntity, Void, Void> {
        private iInventoryorderLineBarcodeDao mAsyncTaskDao;

        deleteAsyncTask(iInventoryorderLineBarcodeDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final cInventoryorderLineBarcodeEntity... params) {
            mAsyncTaskDao.delete(params[0]);
            return null;
        }
    }


    private static class UpdateBarcodeAmountParams {
        String barcodeStr;
        double amountDbl;

        UpdateBarcodeAmountParams(String pvBarcodeStr, double pvAmountDbl) {
            this.barcodeStr = pvBarcodeStr;
            this.amountDbl = pvAmountDbl;
        }
    }

    private static class updateBarcodeAmountAsyncTask extends AsyncTask<UpdateBarcodeAmountParams, Void, Void> {
        private iInventoryorderLineBarcodeDao mAsyncTaskDao;
        updateBarcodeAmountAsyncTask(iInventoryorderLineBarcodeDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected Void doInBackground(UpdateBarcodeAmountParams... params) {
            mAsyncTaskDao.updateBarcodeAmount(params[0].barcodeStr, params[0].amountDbl);
            return null;
        }
    }
}
