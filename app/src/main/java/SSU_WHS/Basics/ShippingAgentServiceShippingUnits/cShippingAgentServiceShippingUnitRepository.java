package SSU_WHS.Basics.ShippingAgentServiceShippingUnits;

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

public class cShippingAgentServiceShippingUnitRepository {

    //Region Public Properties
    public iShippingAgentServiceShippingUnitDao shippingAgentServiceShippingUnitDao;
    //End Region Public Properties

    //Region Private Properties
    private acScanSuiteDatabase db;
    //End Region Private Properties

    //Region Constructor
    cShippingAgentServiceShippingUnitRepository(Application pvApplication) {
        this.db = acScanSuiteDatabase.getDatabase(pvApplication);
        this.shippingAgentServiceShippingUnitDao = db.shippingAgentServiceShippingUnitDao();
    }
    //End Region Constructor

    //Region Public Methods


    public cWebresult pGetShippingAgentServiceShippingUnitsFromWebserviceWrs() {

        List<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();

        try {
            webResultWrs = new cShippingAgentServiceShippingUnitRepository.shippingAgentServiceShippingUnitsFromWebserviceGetAsyncTask().execute().get();
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
    public void pInsert(cShippingAgentServiceShippingUnitEntity shippingAgentServiceShippingUnitEntity) {
        new  cShippingAgentServiceShippingUnitRepository.insertAsyncTask(this.shippingAgentServiceShippingUnitDao).execute(shippingAgentServiceShippingUnitEntity);
    }

    public void pDeleteAll() {
        new cShippingAgentServiceShippingUnitRepository.deleteAllAsyncTask (this.shippingAgentServiceShippingUnitDao).execute();
    }
    //End Region Public Methods

    //Region Private Methods
    private static class deleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private iShippingAgentServiceShippingUnitDao mAsyncTaskDao;

        deleteAllAsyncTask(iShippingAgentServiceShippingUnitDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final Void... params) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }
    private static class insertAsyncTask extends AsyncTask<cShippingAgentServiceShippingUnitEntity, Void, Void> {
        private iShippingAgentServiceShippingUnitDao mAsyncTaskDao;

        insertAsyncTask(iShippingAgentServiceShippingUnitDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final cShippingAgentServiceShippingUnitEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class shippingAgentServiceShippingUnitsFromWebserviceGetAsyncTask extends AsyncTask<Void, Void, cWebresult> {
        @Override
        protected cWebresult doInBackground(final Void... params) {
            cWebresult l_WebresultWrs = new cWebresult();

            List<PropertyInfo> l_PropertyInfoObl = new ArrayList<>();
            try {
                l_WebresultWrs = new cWebresult().pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_GETSHIPPINGAGENTSERVICESHPPINGUNITS, l_PropertyInfoObl);
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
