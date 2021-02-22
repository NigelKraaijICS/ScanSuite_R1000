package SSU_WHS.Basics.StockOwner;

import android.app.Application;
import android.os.AsyncTask;

import org.json.JSONException;
import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import SSU_WHS.Basics.Users.cUser;
import SSU_WHS.General.acScanSuiteDatabase;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebserviceDefinitions;

public class cStockOwnerRepository {
    //Region Public Properties
    private iStockOwnerDao stockOwnerDao;
    //End Region Public Properties

    //Region Private Properties

    //End Region Private Properties

    //Region Constructor
    cStockOwnerRepository(Application pvApplication) {
        acScanSuiteDatabase db =  acScanSuiteDatabase.pGetDatabase(pvApplication);
        this.stockOwnerDao = db.stockOwnerDao();
    }
    //End Region Constructor

    //Region Public Methods
    public cWebresult pGetStockOwnerFromWebserviceWrs() {

        List<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();

        try {
            webResultWrs = new mStockOwnerGetFromWebserviceGetAsyncTask().execute().get();
        } catch (ExecutionException | InterruptedException e) {
            webResultWrs.setResultBln(false);
            webResultWrs.setSuccessBln(false);
            resultObl.add(e.getLocalizedMessage());
            webResultWrs.setResultObl(resultObl);
            e.printStackTrace();
        }
        return webResultWrs;
    }

    public void pInsert(cStockOwnerEntity stockOwnerEntity) {
        new mInsertAsyncTask(stockOwnerDao).execute(stockOwnerEntity);
    }

    public void pDeleteAll() {

        new mDeleteAllAsyncTask(stockOwnerDao).execute();
    }

    //End Region Public Methods

    //Region Private Methods

    private static class mDeleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private iStockOwnerDao mAsyncTaskDao;

        mDeleteAllAsyncTask(iStockOwnerDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final Void... params) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }

    private static class mInsertAsyncTask extends AsyncTask<cStockOwnerEntity, Void, Void> {
        private iStockOwnerDao mAsyncTaskDao;

        mInsertAsyncTask(iStockOwnerDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final cStockOwnerEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class mStockOwnerGetFromWebserviceGetAsyncTask extends AsyncTask<Void, Void, cWebresult> {
        @Override
        protected cWebresult doInBackground(final Void... params) {
            cWebresult WebresultWrs = new cWebresult();

           try {
                new cWebresult();
                WebresultWrs = cWebresult.pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_STOCKOWNERGET, null);
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
