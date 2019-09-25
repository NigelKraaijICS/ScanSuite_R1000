package SSU_WHS.Basics.BarcodeLayouts;

import android.app.Application;
import android.os.AsyncTask;
import org.json.JSONException;
import org.ksoap2.serialization.PropertyInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import SSU_WHS.Webservice.cWebserviceDefinitions;
import SSU_WHS.General.acScanSuiteDatabase;
import SSU_WHS.Webservice.cWebresult;

public class cBarcodeLayoutRepository {

    //Region Public Properties
    public iBarcodeLayoutDao barcodeLayoutDao;
    //End Region Public Properties

    //Region Private Properties
    private acScanSuiteDatabase db;
    //End Region Private Properties

    //Region Constructor
    cBarcodeLayoutRepository(Application pvApplication) {
        this.db = acScanSuiteDatabase.getDatabase(pvApplication);
        this.barcodeLayoutDao = db.barcodeLaoutDao();
    }
    //End Region Constructor

    //Region Public Methods
    public cWebresult pGetBarcodeLayoutsFromWebserviceWrs() {

        List<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();

        try {
            webResultWrs = new barcodeLayoutsFromWebserviceGetAsyncTask().execute().get();
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

    public void pInsert(cBarcodeLayoutEntity barcodeLayoutEntity) {
        new insertAsyncTask(barcodeLayoutDao).execute(barcodeLayoutEntity);
    }

    public void pDeleteAll() {
        new cBarcodeLayoutRepository.deleteAllAsyncTask(barcodeLayoutDao).execute();
    }
    //End Region Public Methods

    //Region Private Methods
    private static class deleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private iBarcodeLayoutDao mAsyncTaskDao;

        deleteAllAsyncTask(iBarcodeLayoutDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final Void... params) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }
    private static class insertAsyncTask extends AsyncTask<cBarcodeLayoutEntity, Void, Void> {
        private iBarcodeLayoutDao mAsyncTaskDao;

        insertAsyncTask(iBarcodeLayoutDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final cBarcodeLayoutEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }


    private static class barcodeLayoutsFromWebserviceGetAsyncTask extends AsyncTask<Void, Void, cWebresult> {
        @Override
        protected cWebresult doInBackground(final Void... params) {
            cWebresult l_WebresultWrs = new cWebresult();

            List<PropertyInfo> l_PropertyInfoObl = new ArrayList<>();

            try {
                l_WebresultWrs = new cWebresult().pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_GETBARCODELAYOUTS, l_PropertyInfoObl);
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
