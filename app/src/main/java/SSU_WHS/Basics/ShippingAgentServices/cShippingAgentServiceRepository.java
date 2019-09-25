package SSU_WHS.Basics.ShippingAgentServices;

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

public class cShippingAgentServiceRepository {

    //Region Public Properties
    public iShippingAgentServiceDao shippingAgentServiceDao;
    //End Region Public Properties

    //Region Private Properties
    private acScanSuiteDatabase db;
    //End Region Private Properties

    //Region Constructor
    cShippingAgentServiceRepository(Application pvApplication) {
        this.db = acScanSuiteDatabase.getDatabase(pvApplication);
        this.shippingAgentServiceDao = db.shippingAgentServiceDao();
    }
    //End Region Constructor

    //Region Public Methods


    public cWebresult pGetShippingAgentServicesFromWebserviceWrs() {

        List<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();

        try {
            webResultWrs = new cShippingAgentServiceRepository.shippingAgentServicesFromWebserviceGetAsyncTask().execute().get();
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
    public void pInsert(cShippingAgentServiceEntity shippingAgentServiceEntity) {
        new  cShippingAgentServiceRepository.insertAsyncTask(shippingAgentServiceDao).execute(shippingAgentServiceEntity);
    }

    public void pDeleteAll() {
        new cShippingAgentServiceRepository.deleteAllAsyncTask (shippingAgentServiceDao).execute();
    }
    //End Region Public Methods

    //Region Private Methods
    private static class deleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private iShippingAgentServiceDao mAsyncTaskDao;

        deleteAllAsyncTask(iShippingAgentServiceDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final Void... params) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }
    private static class insertAsyncTask extends AsyncTask<cShippingAgentServiceEntity, Void, Void> {
        private iShippingAgentServiceDao mAsyncTaskDao;

        insertAsyncTask(iShippingAgentServiceDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final cShippingAgentServiceEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class shippingAgentServicesFromWebserviceGetAsyncTask extends AsyncTask<Void, Void, cWebresult> {
        @Override
        protected cWebresult doInBackground(final Void... params) {
            cWebresult l_WebresultWrs = new cWebresult();

            List<PropertyInfo> l_PropertyInfoObl = new ArrayList<>();
            try {
                l_WebresultWrs = new cWebresult().pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_GETSHIPPINGAGENTSERVICES, l_PropertyInfoObl);
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
