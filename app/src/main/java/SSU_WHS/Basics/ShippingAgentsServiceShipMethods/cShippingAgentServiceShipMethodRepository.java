package SSU_WHS.Basics.ShippingAgentsServiceShipMethods;

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

public class cShippingAgentServiceShipMethodRepository {
    //Region Public Properties
    private iShippingAgentServiceShipMethodDao shippingAgentServiceShipMethodDao;
    //End Region Public Properties

    //Region Private Properties

    //End Region Private Properties

    //Region Constructor
    cShippingAgentServiceShipMethodRepository(Application pvApplication) {
        acScanSuiteDatabase db = acScanSuiteDatabase.pGetDatabase(pvApplication);
        this.shippingAgentServiceShipMethodDao = db.shippingAgentServiceShipMethodDao();
    }
    //End Region Constructor

    //Region Public Methods


    public cWebresult pGetShippingAgentServiceShipMethodsFromWebserviceWrs() {

        List<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();

        try {
            webResultWrs = new cShippingAgentServiceShipMethodRepository.shippingAgentServiceShipMethodsFromWebserviceGetAsyncTask().execute().get();
        } catch (ExecutionException | InterruptedException e) {
            webResultWrs.setResultBln(false);
            webResultWrs.setSuccessBln(false);
            resultObl.add(e.getLocalizedMessage());
            webResultWrs.setResultObl(resultObl);
            e.printStackTrace();
        }
        return webResultWrs;
    }
    public void pInsert(cShippingAgentServiceShipMethodEntity shippingAgentServiceShipMethodEntity) {
        new  cShippingAgentServiceShipMethodRepository.insertAsyncTask(this.shippingAgentServiceShipMethodDao).execute(shippingAgentServiceShipMethodEntity);
    }

    public void pDeleteAll() {
        new cShippingAgentServiceShipMethodRepository.deleteAllAsyncTask (this.shippingAgentServiceShipMethodDao).execute();
    }
    //End Region Public Methods

    //Region Private Methods
    private static class deleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private iShippingAgentServiceShipMethodDao mAsyncTaskDao;

        deleteAllAsyncTask(iShippingAgentServiceShipMethodDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final Void... params) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }
    private static class insertAsyncTask extends AsyncTask<cShippingAgentServiceShipMethodEntity, Void, Void> {
        private iShippingAgentServiceShipMethodDao mAsyncTaskDao;

        insertAsyncTask(iShippingAgentServiceShipMethodDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final cShippingAgentServiceShipMethodEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class shippingAgentServiceShipMethodsFromWebserviceGetAsyncTask extends AsyncTask<Void, Void, cWebresult> {
        @Override
        protected cWebresult doInBackground(final Void... params) {
            cWebresult l_WebresultWrs = new cWebresult();

            List<PropertyInfo> l_PropertyInfoObl = new ArrayList<>();
            try {
                l_WebresultWrs = cWebresult.pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_GETSHIPPINGAGENTSERVICESHIPMETHODS, l_PropertyInfoObl);
            } catch (JSONException e) {
                l_WebresultWrs.setResultBln(false);
                l_WebresultWrs.setSuccessBln(false);
                e.printStackTrace();
            }

            return l_WebresultWrs;
        }
    }


}
