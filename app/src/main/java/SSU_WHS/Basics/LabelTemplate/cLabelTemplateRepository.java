package SSU_WHS.Basics.LabelTemplate;

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

public class cLabelTemplateRepository {
    //Region Public Properties
    private  iLabelTemplateDao labelTemplateDao;
    //End Region Public Properties

    //Region Constructor
    cLabelTemplateRepository(Application pvApplication){
        acScanSuiteDatabase db = acScanSuiteDatabase.pGetDatabase(pvApplication);
        this.labelTemplateDao = db.labelTemplateDao();
    }

    //Region Public Methods

    public cWebresult pGetLabelTemplateFromWebserviceWrs() {

        List<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();

        try {
            webResultWrs = new cLabelTemplateRepository.labelTemplateFromWebserviceGetAsyncTask().execute().get();
        } catch (ExecutionException | InterruptedException e) {
            webResultWrs.setResultBln(false);
            webResultWrs.setSuccessBln(false);
            resultObl.add(e.getLocalizedMessage());
            webResultWrs.setResultObl(resultObl);
            e.printStackTrace();
        }
        return webResultWrs;
    }

    public void insert (cLabelTemplateEntity labelTemplateEntity) {
        new cLabelTemplateRepository.insertAsyncTask(labelTemplateDao).execute(labelTemplateEntity);
    }

    public void deleteAll () {
        new cLabelTemplateRepository.deleteAllAsyncTask(labelTemplateDao).execute();
    }

    //End Region Public Methods

    //Region Private Methods
    private static class deleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private iLabelTemplateDao mAsyncTaskDao;

        deleteAllAsyncTask(iLabelTemplateDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final Void... params) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }
    private static class insertAsyncTask extends AsyncTask<cLabelTemplateEntity, Void, Void> {
        private iLabelTemplateDao mAsyncTaskDao;

        insertAsyncTask(iLabelTemplateDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final cLabelTemplateEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class labelTemplateFromWebserviceGetAsyncTask extends AsyncTask<Void, Void, cWebresult> {
        @Override
        protected cWebresult doInBackground(final Void... params) {
            cWebresult WebresultWrs = new cWebresult();

            try {
                WebresultWrs = cWebresult.pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_GETLABELTEMPLATES,  null);
            } catch (JSONException e) {
                WebresultWrs.setResultBln(false);
                WebresultWrs.setSuccessBln(false);
                e.printStackTrace();
            }

            return WebresultWrs;
        }
    }

    //End Region Private Methods

    //End Region Constructor
}
