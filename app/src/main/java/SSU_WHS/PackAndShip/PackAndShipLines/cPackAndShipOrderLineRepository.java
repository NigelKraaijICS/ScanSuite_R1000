package SSU_WHS.PackAndShip.PackAndShipLines;

import android.app.Application;
import android.os.AsyncTask;

import org.json.JSONException;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cDateAndTime;
import ICS.Utils.cDeviceInfo;
import SSU_WHS.Basics.IdentifierWithDestination.cIdentifierWithDestination;
import SSU_WHS.Basics.Users.cUser;
import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.General.acScanSuiteDatabase;
import SSU_WHS.Move.MoveOrders.cMoveorder;
import SSU_WHS.Move.MoveorderBarcodes.cMoveorderBarcode;
import SSU_WHS.Move.MoveorderLines.cMoveorderLine;
import SSU_WHS.Move.MoveorderLines.cMoveorderLineEntity;
import SSU_WHS.Move.MoveorderLines.iMoveorderLineDao;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebservice;
import SSU_WHS.Webservice.cWebserviceDefinitions;

public class cPackAndShipOrderLineRepository {



    //Region Public Properties
    private iPackAndShipOrderLineDao packAndShipOrderLineDao;
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
        private iPackAndShipOrderLineDao mAsyncTaskDao;

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
        private iPackAndShipOrderLineDao mAsyncTaskDao;

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
        private iPackAndShipOrderLineDao mAsyncTaskDao;

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

