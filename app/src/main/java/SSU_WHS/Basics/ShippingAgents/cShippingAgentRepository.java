package SSU_WHS.Basics.ShippingAgents;

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

public class cShippingAgentRepository {

    //Region Public Properties
    private iShippingAgentDao shippingAgentDao;
    //End Region Public Properties

    //Region Private Properties

    //End Region Private Properties

    //Region Constructor
    cShippingAgentRepository(Application pvApplication) {
        acScanSuiteDatabase db= acScanSuiteDatabase.pGetDatabase(pvApplication);
        this.shippingAgentDao = db.shippingAgentDao();
    }
    //End Region Constructor

    //Region Public Methods


    public cWebresult pGetShippingAgentsFromWebserviceWrs() {

        List<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();

        try {
            webResultWrs = new cShippingAgentRepository.shippingAgentsFromWebserviceGetAsyncTask().execute().get();
        } catch (ExecutionException | InterruptedException e) {
            webResultWrs.setResultBln(false);
            webResultWrs.setSuccessBln(false);
            resultObl.add(e.getLocalizedMessage());
            webResultWrs.setResultObl(resultObl);
            e.printStackTrace();
        }
        return webResultWrs;
    }
    public void pInsert(cShippingAgentEntity shippingAgentEntity) {
        new  cShippingAgentRepository.insertAsyncTask(shippingAgentDao).execute(shippingAgentEntity);
    }

    public void pDeleteAll() {
        new cShippingAgentRepository.deleteAllAsyncTask(shippingAgentDao).execute();
    }
    //End Region Public Methods

    //Region Private Methods
    private static class deleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private iShippingAgentDao mAsyncTaskDao;

        deleteAllAsyncTask(iShippingAgentDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final Void... params) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }
    private static class insertAsyncTask extends AsyncTask<cShippingAgentEntity, Void, Void> {
        private iShippingAgentDao mAsyncTaskDao;

        insertAsyncTask(iShippingAgentDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final cShippingAgentEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class shippingAgentsFromWebserviceGetAsyncTask extends AsyncTask<Void, Void, cWebresult> {
        @Override
        protected cWebresult doInBackground(final Void... params) {
            cWebresult l_WebresultWrs = new cWebresult();

            List<PropertyInfo> l_PropertyInfoObl = new ArrayList<>();
            try {
                l_WebresultWrs = cWebresult.pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_GETSHIPPINGAGENTS, l_PropertyInfoObl);
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
