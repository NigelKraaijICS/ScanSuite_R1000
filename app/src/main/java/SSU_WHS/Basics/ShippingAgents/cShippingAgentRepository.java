package SSU_WHS.Basics.ShippingAgents;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebserviceDefinitions;
import SSU_WHS.General.acScanSuiteDatabase;

public class cShippingAgentRepository {
    private iShippingAgentDao shippingAgentDao;
    private cWebresult webResult;

    cShippingAgentRepository(Application application) {
        acScanSuiteDatabase db = acScanSuiteDatabase.getDatabase(application);
        shippingAgentDao = db.shippingAgentDao();
    }

    public LiveData<List<cShippingAgentEntity>> getShippingAgents(final Boolean forcerefresh) {

        final MutableLiveData<List<cShippingAgentEntity>> data = new MutableLiveData<>();

        new Thread(new Runnable() {
            @Override
            public void run() {

                List<cShippingAgentEntity> shippingAgentsObl = new ArrayList<>();
                if (forcerefresh) {
                    try {
                        deleteAll();
                        List<PropertyInfo> l_PropertyInfoObl = new ArrayList<>();

                        webResult = new cWebresult().mGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_GETSHIPPINGAGENTS, l_PropertyInfoObl);
                        List<JSONObject> myList = webResult.getResultDtt();
                        for (int i = 0; i < myList.size(); i++) {
                            JSONObject jsonObject;
                            jsonObject = myList.get(i);

                            cShippingAgentEntity shippingAgentEntity = new cShippingAgentEntity(jsonObject);
                            insert(shippingAgentEntity);
                            shippingAgentsObl.add(shippingAgentEntity);
                        }
                        data.postValue(shippingAgentsObl);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }//forcerefresh
                else {
                    shippingAgentsObl = getAll();
                    data.postValue(shippingAgentsObl);
                }
            } //run
        }).start(); //Thread
        return data;
    }
    public List<cShippingAgentEntity> getAll() {
        List<cShippingAgentEntity> shippingAgentEntitiesObl = null;
        try {
            shippingAgentEntitiesObl = new getAllAsyncTask(shippingAgentDao).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return shippingAgentEntitiesObl;
    }
    private static class getAllAsyncTask extends AsyncTask<Void, Void, List<cShippingAgentEntity>> {
        private iShippingAgentDao mAsyncTaskDao;

        getAllAsyncTask(iShippingAgentDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected List<cShippingAgentEntity> doInBackground(final Void... params) {
            return mAsyncTaskDao.getAll();
        }
    }
    public void deleteAll () {
        new deleteAllAsyncTask(shippingAgentDao).execute();
    }

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
    public void insert(cShippingAgentEntity shippingAgentEntity) {
        new insertAsyncTask(shippingAgentDao).execute(shippingAgentEntity);
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
    public cShippingAgentEntity getShippingAgentByAgentName(String agentname) {
        cShippingAgentEntity shippingAgentEntity = null;
        try {
            shippingAgentEntity = new getShippingAgentByAgentNameAsyncTask(shippingAgentDao).execute(agentname).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return shippingAgentEntity;
    }
    private static class getShippingAgentByAgentNameAsyncTask extends AsyncTask<String, Void, cShippingAgentEntity> {
        private iShippingAgentDao mAsyncTaskDao;

        getShippingAgentByAgentNameAsyncTask(iShippingAgentDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected cShippingAgentEntity doInBackground(final String... params) {
            return mAsyncTaskDao.getShippingAgentEntityByAgentName(params[0]);
        }
    }

}
