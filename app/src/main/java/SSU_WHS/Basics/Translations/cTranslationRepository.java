package SSU_WHS.Basics.Translations;

import android.app.Application;
import android.os.AsyncTask;

import org.json.JSONException;
import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import SSU_WHS.Basics.Users.cUser;
import SSU_WHS.Basics.Workplaces.cWorkplaceEntity;
import SSU_WHS.Basics.Workplaces.iWorkplaceDao;
import SSU_WHS.General.acScanSuiteDatabase;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebserviceDefinitions;

public class cTranslationRepository {
    //Region Public Properties
    private iTranslationDao translationDao;
    //End Region Public Properties

    //Region Private Properties

    //End Region Private Properties

    //Region Constructor
    cTranslationRepository(Application pvApplication) {
       acScanSuiteDatabase db = acScanSuiteDatabase.pGetDatabase(pvApplication);
        this.translationDao = db.translationDao();
    }
    //End Region Constructor

    //Region Public Methods

    public cWebresult pGetTranslationsFromWebserviceWrs() {

        List<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();

        try {
            webResultWrs = new mTranslationsFromWebserviceGetAsyncTask().execute().get();
        } catch (ExecutionException | InterruptedException e) {
            webResultWrs.setResultBln(false);
            webResultWrs.setSuccessBln(false);
            resultObl.add(e.getLocalizedMessage());
            webResultWrs.setResultObl(resultObl);
            e.printStackTrace();
        }
        return webResultWrs;
    }

    public void insert (cTranslationEntity pvTranslationEntity) {
        new insertAsyncTask(translationDao).execute(pvTranslationEntity);
    }

    public void deleteAll () {
        new cTranslationRepository.deleteAllAsyncTask(translationDao).execute();
    }

    //End Region Public Methods

    //Region Private Methods
    private static class deleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private iTranslationDao mAsyncTaskDao;

        deleteAllAsyncTask(iTranslationDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final Void... params) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }
    private static class insertAsyncTask extends AsyncTask<cTranslationEntity, Void, Void> {
        private iTranslationDao mAsyncTaskDao;

        insertAsyncTask(iTranslationDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final cTranslationEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

   private static class mTranslationsFromWebserviceGetAsyncTask extends AsyncTask<Void, Void, cWebresult> {
        @Override
        protected cWebresult doInBackground(final Void... params) {
            cWebresult WebresultWrs = new cWebresult();

            List<PropertyInfo> l_PropertyInfoObl = new ArrayList<>();


            try {
                WebresultWrs = cWebresult.pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_GETTRANSLATIONS, l_PropertyInfoObl);
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
