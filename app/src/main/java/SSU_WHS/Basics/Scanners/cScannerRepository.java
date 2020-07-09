package SSU_WHS.Basics.Scanners;

import android.app.Application;
import android.os.AsyncTask;

import org.json.JSONException;
import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import SSU_WHS.General.acScanSuiteDatabase;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebserviceDefinitions;

public class cScannerRepository {
    //Region Public Properties
    private iScannerDao scannerDao;
    //End Region Public Properties

    //Region Private Properties

    //End Region Private Properties

    //Region Constructor
    cScannerRepository(Application pvApplication) {
       acScanSuiteDatabase db = acScanSuiteDatabase.pGetDatabase(pvApplication);
        this.scannerDao = db.scannerDao();
    }
    //End Region Constructor

    //Region Public Methods

    public cWebresult pGetScannersFromWebserviceWrs() {

        List<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();

        try {
            webResultWrs = new scannersFromWebserviceGetAsyncTask().execute().get();
        } catch (ExecutionException | InterruptedException e) {
            webResultWrs.setResultBln(false);
            webResultWrs.setSuccessBln(false);
            resultObl.add(e.getLocalizedMessage());
            webResultWrs.setResultObl(resultObl);
            e.printStackTrace();
        }
        return webResultWrs;
    }

    public void insert (cScannerEntity scannerEntity) {
        new insertAsyncTask(scannerDao).execute(scannerEntity);
    }

    public void deleteAll () {
        new cScannerRepository.deleteAllAsyncTask(scannerDao).execute();
    }

    //End Region Public Methods

    //Region Private Methods
    private static class deleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private iScannerDao mAsyncTaskDao;

        deleteAllAsyncTask(iScannerDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final Void... params) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }
    private static class insertAsyncTask extends AsyncTask<cScannerEntity, Void, Void> {
        private iScannerDao mAsyncTaskDao;

        insertAsyncTask(iScannerDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final cScannerEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

   private static class scannersFromWebserviceGetAsyncTask extends AsyncTask<Void, Void, cWebresult> {
        @Override
        protected cWebresult doInBackground(final Void... params) {
            cWebresult WebresultWrs = new cWebresult();

            List<PropertyInfo> l_PropertyInfoObl = new ArrayList<>();

            try {
                WebresultWrs = cWebresult.pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_GETSCANNERS, l_PropertyInfoObl);
            } catch (JSONException e) {
                WebresultWrs.setResultBln(false);
                WebresultWrs.setSuccessBln(false);
                e.printStackTrace();
            }

            return WebresultWrs;
        }
    }

    //End Region Private Methods

}
