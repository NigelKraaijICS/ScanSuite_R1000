package SSU_WHS.Move.MoveorderLineBarcode;

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
import SSU_WHS.Basics.Users.cUser;
import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.General.acScanSuiteDatabase;
import SSU_WHS.Move.Moveorders.cMoveorder;
import SSU_WHS.Move.MoveorderBarcodes.cMoveorderBarcode;
import SSU_WHS.Move.MoveorderLines.cMoveorderLine;
import SSU_WHS.Move.MoveorderLines.cMoveorderLineEntity;
import SSU_WHS.Move.MoveorderLines.iMoveorderLineDao;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebservice;
import SSU_WHS.Webservice.cWebserviceDefinitions;

public class cMoveorderLineBarcodeRepository {

    //Region Public Properties
    private iMoveorderLineBarcodeDao moveorderLineBarcodeDao;
    //End Region Public Properties

    //Region Constructor
    cMoveorderLineBarcodeRepository(Application pvApplication) {
        //Region Private Properties
        acScanSuiteDatabase db = acScanSuiteDatabase.pGetDatabase(pvApplication);
        this.moveorderLineBarcodeDao = db.moveorderLineBarcodeDao();
    }
    //End Region Constructor

    //Region Public Methods

    public void insert(cMoveorderLineBarcodeEntity pvMoveorderLineBarcodeEntity) {
        new mInsertAsyncTask(moveorderLineBarcodeDao).execute(pvMoveorderLineBarcodeEntity);
    }

    public void delete(cMoveorderLineBarcodeEntity pvMoveorderLineBarcodeEntity) {
        new mDeleteAsyncTask(moveorderLineBarcodeDao).execute(pvMoveorderLineBarcodeEntity);
    }

    public void deleteAll() {
        new mDeleteAllAsyncTask(moveorderLineBarcodeDao).execute();
    }


    //End Region Public Methods

    //Region Private Methods

    private static class mInsertAsyncTask extends AsyncTask<cMoveorderLineBarcodeEntity, Void, Void> {
        private iMoveorderLineBarcodeDao mAsyncTaskDao;

        mInsertAsyncTask(iMoveorderLineBarcodeDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final cMoveorderLineBarcodeEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class mDeleteAsyncTask extends AsyncTask<cMoveorderLineBarcodeEntity, Void, Void> {
        private iMoveorderLineBarcodeDao mAsyncTaskDao;

        mDeleteAsyncTask(iMoveorderLineBarcodeDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final cMoveorderLineBarcodeEntity... params) {
            mAsyncTaskDao.delete(params[0]);
            return null;
        }
    }

    private static class mDeleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private iMoveorderLineBarcodeDao mAsyncTaskDao;

        mDeleteAllAsyncTask(iMoveorderLineBarcodeDao dao) {
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

