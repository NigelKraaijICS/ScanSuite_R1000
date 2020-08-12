package SSU_WHS.Basics.CustomAuthorisations;

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

public class cCustomAuthorisationRepository {

    //Region Public Properties
    private iCustomAuthorisationDao customAuthorisationDao;
    //End Region Public Properties

    //Region Private Properties

    //End Region Private Properties

    //Region Constructor
    cCustomAuthorisationRepository(Application pvApplication) {
        acScanSuiteDatabase db = acScanSuiteDatabase.pGetDatabase(pvApplication);
        this.customAuthorisationDao = db.customAuthorisationDao();
    }
    //End Region Constructor

    //Region Public Methods
    public cWebresult pGetCustomAutorisationsFromWebserviceWrs() {

        ArrayList<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();

        try {
            webResultWrs = new mGetcustomAutorisationsFromWebserviceGetAsyncTask().execute().get();
        } catch (ExecutionException | InterruptedException e) {
            webResultWrs.setResultBln(false);
            webResultWrs.setSuccessBln(false);
            resultObl.add(e.getLocalizedMessage());
            webResultWrs.setResultObl(resultObl);
            e.printStackTrace();
        }
        return webResultWrs;
    }

    public cCustomAuthorisationEntity pGetCustomAuthorisationByAutorisation(String authorisation) {
        cCustomAuthorisationEntity customAuthorisationEntity = null;
        try {
            customAuthorisationEntity = new pGetCustomAuthorisationByAutorisationAsync(customAuthorisationDao).execute(authorisation).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return customAuthorisationEntity;
    }

    private static class pGetCustomAuthorisationByAutorisationAsync extends AsyncTask<String, Void, cCustomAuthorisationEntity> {
        private iCustomAuthorisationDao mAsyncTaskDao;

        pGetCustomAuthorisationByAutorisationAsync(iCustomAuthorisationDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected cCustomAuthorisationEntity doInBackground(final String... params) {
            return mAsyncTaskDao.getByAutorisation(params[0]);
        }
    }

    public void pInsert(cCustomAuthorisationEntity customAuthorisationEntity) {
        new insertAsyncTask(customAuthorisationDao).execute(customAuthorisationEntity);
    }

    public void pDeleteAll() {

        new   cCustomAuthorisationRepository.deleteAllAsyncTask(customAuthorisationDao).execute();
    }

    //End Region Public Methods

    //Region Private Methods
    private static class deleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private iCustomAuthorisationDao mAsyncTaskDao;

        deleteAllAsyncTask(iCustomAuthorisationDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final Void... params) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }
    private static class insertAsyncTask extends AsyncTask<cCustomAuthorisationEntity, Void, Void> {
        private iCustomAuthorisationDao mAsyncTaskDao;

        insertAsyncTask(iCustomAuthorisationDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final cCustomAuthorisationEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class mGetcustomAutorisationsFromWebserviceGetAsyncTask extends AsyncTask<Void, Void, cWebresult> {
        @Override
        protected cWebresult doInBackground(final Void... params) {

            cWebresult WebresultWrs = new cWebresult();

            List<PropertyInfo> l_PropertyInfoObl = new ArrayList<>();

            PropertyInfo l_PropertyInfo1Pin = new PropertyInfo();
            l_PropertyInfo1Pin.name = cWebserviceDefinitions.WEBPROPERTY_INCLUDEIMAGE;
            l_PropertyInfo1Pin.setValue(true);
            l_PropertyInfoObl.add(l_PropertyInfo1Pin);

            try {
                WebresultWrs = cWebresult.pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_CUSTOMAUTHORISATION, l_PropertyInfoObl);

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
