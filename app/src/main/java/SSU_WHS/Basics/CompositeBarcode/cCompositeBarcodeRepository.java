package SSU_WHS.Basics.CompositeBarcode;

import android.app.Application;
import android.os.AsyncTask;

import org.json.JSONException;
import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import SSU_WHS.Basics.PropertyGroup.cPropertyGroupEntity;
import SSU_WHS.Basics.PropertyGroup.iPropertyGroupDao;
import SSU_WHS.Basics.Users.cUser;
import SSU_WHS.General.acScanSuiteDatabase;
import SSU_WHS.Picken.Pickorders.cPickorder;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebserviceDefinitions;


public class cCompositeBarcodeRepository {

    //Region Public properties
    private final iCompositeBarcodeDao compositeBarcodeDao;

    //End Region Public Properties

    //Region Private Properties
    public//End Region Private Properties

    //Region Constructor
    cCompositeBarcodeRepository(Application pvApplication) {
        acScanSuiteDatabase db = acScanSuiteDatabase.pGetDatabase(pvApplication);
        this.compositeBarcodeDao = db.compositeBarcodeDao();
    }

    //Region Public Methods
    public cWebresult pGetCompositeBarcodesFromWebserviceWrs() {

        List<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();
        try {
            webResultWrs = new cCompositeBarcodeRepository.mGetCompositeBarcodesFromWebserviceGetAsyncTask().execute().get();
        } catch (ExecutionException | InterruptedException e) {
            webResultWrs.setResultBln(false);
            webResultWrs.setSuccessBln(false);
            resultObl.add(e.getLocalizedMessage());
            webResultWrs.setResultObl(resultObl);
            e.printStackTrace();
        }
        return webResultWrs;
    }


    public void pInsert(cCompositeBarcodeEntity pvCompositeBarcodeEntity){
    new insertAsyncTask(compositeBarcodeDao).execute(pvCompositeBarcodeEntity);
    }

    public void pDeleteAll() {
        new deleteAllAsyncTask(compositeBarcodeDao).execute();
    }
    //End Region Public Methods

    //Region Private Methods
    private static class deleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private final iCompositeBarcodeDao mAsyncTaskDao;

        deleteAllAsyncTask(iCompositeBarcodeDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final Void... params) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }

    private static class insertAsyncTask extends AsyncTask<cCompositeBarcodeEntity, Void, Void>{
       private final iCompositeBarcodeDao mAsyncTaskDao;

        insertAsyncTask(iCompositeBarcodeDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final cCompositeBarcodeEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }


    private static class mGetCompositeBarcodesFromWebserviceGetAsyncTask extends AsyncTask<Void, Void, cWebresult> {
        @Override
        protected cWebresult doInBackground(final Void... params) {
            cWebresult l_WebresultWrs = new cWebresult();

            List<PropertyInfo> l_PropertyInfoObl = new ArrayList<>();

            try {
                l_WebresultWrs = cWebresult.pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_GETCOMPOSITEBARCODES, l_PropertyInfoObl);
            } catch (JSONException e) {
                l_WebresultWrs.setResultBln(false);
                l_WebresultWrs.setSuccessBln(false);
                e.printStackTrace();
            }

            return l_WebresultWrs;
        }
    }
}
