package SSU_WHS.Basics.ItemProperty;

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


public class cItemPropertyRepository {

    //Region Public properties
    private iItemPropertyDao itemPropertyDao;
    //End Region Public Properties

    //Region Private Properties
    public//End Region Private Properties

    //Region Constructor
    cItemPropertyRepository(Application pvApplication) {
        acScanSuiteDatabase db = acScanSuiteDatabase.pGetDatabase(pvApplication);
        this.itemPropertyDao = db.itemPropertyDao();
    }

    //Region Public Methods
    public cWebresult pGetItemPropertyFromWebserviceWrs() {


        List<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();
        try {
            webResultWrs = new cItemPropertyRepository.itemPropertyFromWebserviceGetAsyncTask().execute().get();
        } catch (ExecutionException | InterruptedException e) {
            webResultWrs.setResultBln(false);
            webResultWrs.setSuccessBln(false);
            resultObl.add(e.getLocalizedMessage());
            webResultWrs.setResultObl(resultObl);
            e.printStackTrace();
        }
        return webResultWrs;
    }

    public void pInsert(cItemPropertyEntity itemPropertyEntity){
    new insertAsyncTask(itemPropertyDao).execute(itemPropertyEntity);
    }

    public void pDeleteAll() {
        new deleteAllAsyncTask(itemPropertyDao).execute();
    }
    //End Region Public Methods

    //Region Private Methods
    private static class deleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private iItemPropertyDao mAsyncTaskDao;

        deleteAllAsyncTask(iItemPropertyDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final Void... params) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }

    private static class insertAsyncTask extends AsyncTask<cItemPropertyEntity, Void, Void>{
       private iItemPropertyDao mAsyncTaskDao;

        insertAsyncTask(iItemPropertyDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final cItemPropertyEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }


    private static class itemPropertyFromWebserviceGetAsyncTask extends AsyncTask<Void, Void, cWebresult> {
        @Override
        protected cWebresult doInBackground(final Void... params) {
            cWebresult l_WebresultWrs = new cWebresult();

            List<PropertyInfo> l_PropertyInfoObl = new ArrayList<>();

            try {
                l_WebresultWrs = cWebresult.pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_GETITEMPROPERTY, l_PropertyInfoObl);
            } catch (JSONException e) {
                l_WebresultWrs.setResultBln(false);
                l_WebresultWrs.setSuccessBln(false);
                e.printStackTrace();
            }

            return l_WebresultWrs;
        }
    }
}
