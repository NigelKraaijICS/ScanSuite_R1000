package SSU_WHS.Inventory.InventoryorderBins;

import android.app.Application;
import android.os.AsyncTask;

import org.json.JSONException;
import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import SSU_WHS.Basics.Users.cUser;
import SSU_WHS.General.acScanSuiteDatabase;
import SSU_WHS.Inventory.InventoryOrders.cInventoryorder;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebserviceDefinitions;

public class cInventoryorderBinRepository {
    //Region Public Properties
    public iInventoryorderBinDao inventoryorderBinDao;
    //End Region Public Properties

    //Region Private Properties
    private acScanSuiteDatabase db;

    //Region Constructor
    cInventoryorderBinRepository(Application pvApplication) {
        this.db = acScanSuiteDatabase.getDatabase(pvApplication);
        this.inventoryorderBinDao = db.inventoryorderBinDao();
    }
    //End Region Constructor

    //Region Public Methods


    public void insert(cInventoryorderBinEntity inventoryorderBinEntity) {
        new mInsertAsyncTask(inventoryorderBinDao).execute(inventoryorderBinEntity);
    }

    public void deleteAll() {
        new mDeleteAllAsyncTask(inventoryorderBinDao).execute();
    }

    private static class mInsertAsyncTask extends AsyncTask<cInventoryorderBinEntity, Void, Void> {
        private iInventoryorderBinDao mAsyncTaskDao;

        mInsertAsyncTask(iInventoryorderBinDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final cInventoryorderBinEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class mDeleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private iInventoryorderBinDao mAsyncTaskDao;

        mDeleteAllAsyncTask(iInventoryorderBinDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Void... params) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }



}
