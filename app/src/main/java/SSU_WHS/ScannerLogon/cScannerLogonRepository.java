package SSU_WHS.ScannerLogon;

import android.app.Application;
import android.os.AsyncTask;

import org.json.JSONException;
import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebserviceDefinitions;
import SSU_WHS.General.acScanSuiteDatabase;
import ICS.Utils.cDeviceInfo;

public class cScannerLogonRepository {

    //Region Public Properties
    public iScannerLogonDao scannerLogonDao;
    //End Region Public Properties

    //Region Private Properties
    private acScanSuiteDatabase db;
    //End Region Private Properties

    //Region Constructor
    cScannerLogonRepository(Application pvApplication) {
        this.db = acScanSuiteDatabase.getDatabase(pvApplication);
        this.scannerLogonDao = db.scannerLogonDao();
    }
    //End Region Constructor

    //Region Public Methods
        public cWebresult pScannerLogonWrs() {

        List<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();

        try {
            webResultWrs = new scannerLogonAsyncTask().execute().get();
        } catch (ExecutionException e) {
            webResultWrs.setResultBln(false);
            webResultWrs.setSuccessBln(false);
            resultObl.add(e.getLocalizedMessage());
            webResultWrs.setResultObl(resultObl);
            e.printStackTrace();
        } catch (InterruptedException e) {
            webResultWrs.setResultBln(false);
            webResultWrs.setSuccessBln(false);
            resultObl.add(e.getLocalizedMessage());
            webResultWrs.setResultObl(resultObl);
            e.printStackTrace();
        }
        return webResultWrs;
    }


    public void insert (cScannerLogonEntity scannerLogonEntity) {
        new insertAsyncTask(scannerLogonDao).execute(scannerLogonEntity);
    }

    public void deleteAll () {
        new cScannerLogonRepository.deleteAllAsyncTask(scannerLogonDao).execute();
    }

    //End Region Public Methods

    //Region Private Methods
    private static class deleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private iScannerLogonDao mAsyncTaskDao;

        deleteAllAsyncTask(iScannerLogonDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final Void... params) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }
    private static class insertAsyncTask extends AsyncTask<cScannerLogonEntity, Void, Void> {
        private iScannerLogonDao mAsyncTaskDao;

        insertAsyncTask(iScannerLogonDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final cScannerLogonEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class scannerLogonAsyncTask extends AsyncTask<Void, Void, cWebresult> {
        @Override
        protected cWebresult doInBackground(final Void... params) {
            cWebresult l_WebresultWrs = new cWebresult();

            List<PropertyInfo> l_PropertyInfoObl = new ArrayList<>();

            PropertyInfo l_PropertyInfo1Pin = new PropertyInfo();
            l_PropertyInfo1Pin.name = cWebserviceDefinitions.WEBPROPERTY_SCANNER;
            l_PropertyInfo1Pin.setValue(cDeviceInfo.getSerialnumberStr());
            l_PropertyInfoObl.add(l_PropertyInfo1Pin);

            PropertyInfo l_PropertyInfo2Pin = new PropertyInfo();
            l_PropertyInfo2Pin.name = cWebserviceDefinitions.WEBPROPERTY_DEVICEBRAND;
            l_PropertyInfo2Pin.setValue(cDeviceInfo.getDeviceBrand());
            l_PropertyInfoObl.add(l_PropertyInfo2Pin);

            PropertyInfo l_PropertyInfo3Pin = new PropertyInfo();
            l_PropertyInfo3Pin.name = cWebserviceDefinitions.WEBPROPERTY_DEVICETYPE;
            l_PropertyInfo3Pin.setValue(cDeviceInfo.getDeviceModel());
            l_PropertyInfoObl.add(l_PropertyInfo3Pin);

            PropertyInfo l_PropertyInfo4Pin = new PropertyInfo();
            l_PropertyInfo4Pin.name = cWebserviceDefinitions.WEBPROPERTY_SCANNERVERSION;
            l_PropertyInfo4Pin.setValue(cDeviceInfo.getAppVersion());
            l_PropertyInfoObl.add(l_PropertyInfo4Pin);

            //We'll leave this emtpy for now
            PropertyInfo l_PropertyInfo5Pin = new PropertyInfo();
            l_PropertyInfo5Pin.name = cWebserviceDefinitions.WEBPROPERTY_SCANNERCONFIGURATION;
            l_PropertyInfo5Pin.setValue("");
            l_PropertyInfoObl.add(l_PropertyInfo5Pin);

            try {
                l_WebresultWrs = new cWebresult().pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_SCANNERLOGON, l_PropertyInfoObl);
            } catch (JSONException e) {
                l_WebresultWrs.setResultBln(false);
                l_WebresultWrs.setSuccessBln(false);
                e.printStackTrace();
            }

            return l_WebresultWrs;
        }
    }

    //End Region Private Methods
}
