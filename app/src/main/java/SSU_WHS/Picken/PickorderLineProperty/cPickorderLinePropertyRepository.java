package SSU_WHS.Picken.PickorderLineProperty;

import android.app.Application;
import android.os.AsyncTask;

import org.json.JSONException;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import ICS.Utils.cDateAndTime;
import ICS.Utils.cDeviceInfo;
import SSU_WHS.Basics.Users.cUser;
import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.General.acScanSuiteDatabase;
import SSU_WHS.PackAndShip.PackAndShipOrders.cPackAndShipOrder;
import SSU_WHS.PackAndShip.PackAndShipShipment.cPackAndShipShipment;
import SSU_WHS.PackAndShip.PackAndShipShipment.cPackAndShipShipmentRepository;
import SSU_WHS.Picken.PickorderLineBarcodes.cPickorderLineBarcode;
import SSU_WHS.Picken.PickorderLines.cPickorderLine;
import SSU_WHS.Picken.PickorderLines.cPickorderLineEntity;
import SSU_WHS.Picken.PickorderLines.iPickorderLineDao;
import SSU_WHS.Picken.Pickorders.cPickorder;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebservice;
import SSU_WHS.Webservice.cWebserviceDefinitions;

public class cPickorderLinePropertyRepository {

    //Region Public Properties
    private iPickorderLinePropertyDao pickorderLinePropertyDao;
    //End Region Public Properties

    //End Region Private Properties

    //Region Constructor
    public cPickorderLinePropertyRepository(Application pvApplication) {
        //Region Private Properties
        acScanSuiteDatabase db = acScanSuiteDatabase.pGetDatabase(pvApplication);
        this.pickorderLinePropertyDao = db.pickorderLinePropertyDao();
    }
    //End Region Constructor

    //Region Public Methods

    public void pInsert(cPickorderLinePropertyEntity pvPickorderLinePropertyEntity) {
        new mInsertInDatabaseAsyncTask(this.pickorderLinePropertyDao).execute(pvPickorderLinePropertyEntity);
    }

    public void pDelete(cPickorderLinePropertyEntity pvPickorderLinePropertyEntity) {
        new mDeleteAsyncTask(this.pickorderLinePropertyDao).execute(pvPickorderLinePropertyEntity);
    }

    public void pTruncate() {
        new cPickorderLinePropertyRepository.deleteAllAsyncTask(this.pickorderLinePropertyDao).execute();
    }

    //End Region Public Methods

    private static class mInsertInDatabaseAsyncTask extends AsyncTask<cPickorderLinePropertyEntity, Void, Void> {
        private iPickorderLinePropertyDao mAsyncTaskDao;

        mInsertInDatabaseAsyncTask(iPickorderLinePropertyDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final cPickorderLinePropertyEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class mDeleteAsyncTask extends AsyncTask<cPickorderLinePropertyEntity, Void, Void> {
        private iPickorderLinePropertyDao mAsyncTaskDao;

        mDeleteAsyncTask(iPickorderLinePropertyDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected Void doInBackground(final cPickorderLinePropertyEntity... params) {
            mAsyncTaskDao.deletePickorder(params[0]);
            return null;
        }
    }

    private static class deleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private iPickorderLinePropertyDao mAsyncTaskDao;

        deleteAllAsyncTask(iPickorderLinePropertyDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final Void... params) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }



}
