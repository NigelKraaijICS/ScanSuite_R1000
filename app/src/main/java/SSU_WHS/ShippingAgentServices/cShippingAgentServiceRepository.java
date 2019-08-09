package SSU_WHS.ShippingAgentServices;

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
import SSU_WHS.Webservice.cWebservice;
import SSU_WHS.Webservice.cWebserviceDefinitions;
import SSU_WHS.acScanSuiteDatabase;

public class cShippingAgentServiceRepository {
    private iShippingAgentServiceDao shippingAgentServiceDao;
    private cWebresult webResult;

    cShippingAgentServiceRepository(Application application) {
        acScanSuiteDatabase db = acScanSuiteDatabase.getDatabase(application);
        shippingAgentServiceDao = db.shippingAgentServiceDao();
    }

    public LiveData<List<cShippingAgentServiceEntity>> getShippingAgentServices(final Boolean forcerefresh) {

        final MutableLiveData<List<cShippingAgentServiceEntity>> data = new MutableLiveData<>();

        new Thread(new Runnable() {
            @Override
            public void run() {

                List<cShippingAgentServiceEntity> shippingAgentServicesObl = new ArrayList<>();
                if (forcerefresh) {
                    try {
                        deleteAll();
                        List<PropertyInfo> l_PropertyInfoObl = new ArrayList<>();

                        webResult = new cWebresult().mGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_GETSHIPPINGAGENTSERVICES, l_PropertyInfoObl);
                        List<JSONObject> myList = webResult.getResultDtt();
                        for (int i = 0; i < myList.size(); i++) {
                            JSONObject jsonObject;
                            jsonObject = myList.get(i);

                            cShippingAgentServiceEntity shippingAgentServiceEntity = new cShippingAgentServiceEntity(jsonObject);
                            insert(shippingAgentServiceEntity);
                            shippingAgentServicesObl.add(shippingAgentServiceEntity);
                        }
                        data.postValue(shippingAgentServicesObl);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }//forcerefresh
                else {
                    shippingAgentServicesObl = getAll();
                    data.postValue(shippingAgentServicesObl);
                }
            } //run
        }).start(); //Thread
        return data;
    }
    public List<cShippingAgentServiceEntity> getAll() {
        List<cShippingAgentServiceEntity> shippingAgentServiceEntitiesObl = null;
        try {
            shippingAgentServiceEntitiesObl = new getAllAsyncTask(shippingAgentServiceDao).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return shippingAgentServiceEntitiesObl;
    }
    private static class getAllAsyncTask extends AsyncTask<Void, Void, List<cShippingAgentServiceEntity>> {
        private iShippingAgentServiceDao mAsyncTaskDao;

        getAllAsyncTask(iShippingAgentServiceDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected List<cShippingAgentServiceEntity> doInBackground(final Void... params) {
            return mAsyncTaskDao.getAll();
        }
    }
    public void deleteAll () {
        new deleteAllAsyncTask(shippingAgentServiceDao).execute();
    }

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
    public void insert(cShippingAgentServiceEntity shippingAgentServiceEntity) {
        new insertAsyncTask(shippingAgentServiceDao).execute(shippingAgentServiceEntity);
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
    public cShippingAgentServiceEntity getShippingAgentServiceByServiceCode(String servicecode) {
        cShippingAgentServiceEntity shippingAgentServiceEntity = null;
        try {
            shippingAgentServiceEntity = new getShippingAgentServiceByServiceCodeAsyncTask(shippingAgentServiceDao).execute(servicecode).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return shippingAgentServiceEntity;
    }
    private static class getShippingAgentServiceByServiceCodeAsyncTask extends AsyncTask<String, Void, cShippingAgentServiceEntity> {
        private iShippingAgentServiceDao mAsyncTaskDao;

        getShippingAgentServiceByServiceCodeAsyncTask(iShippingAgentServiceDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected cShippingAgentServiceEntity doInBackground(final String... params) {
            return mAsyncTaskDao.getShippingAgentServiceEntityByServiceCode(params[0]);
        }
    }
}
