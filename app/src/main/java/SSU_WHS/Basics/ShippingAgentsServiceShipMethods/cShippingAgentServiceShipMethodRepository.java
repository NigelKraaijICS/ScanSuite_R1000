package SSU_WHS.Basics.ShippingAgentsServiceShipMethods;

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

public class cShippingAgentServiceShipMethodRepository {
    private iShippingAgentServiceShipMethodDao shippingAgentServiceShipMethodDao;
    private cWebresult webResult;

    cShippingAgentServiceShipMethodRepository(Application application) {
        acScanSuiteDatabase db = acScanSuiteDatabase.getDatabase(application);
        shippingAgentServiceShipMethodDao = db.shippingAgentServiceShipMethodDao();
    }

    public LiveData<List<cShippingAgentServiceShipMethodEntity>> getShippingAgentServiceShipMethods(final Boolean forcerefresh) {

        final MutableLiveData<List<cShippingAgentServiceShipMethodEntity>> data = new MutableLiveData<>();

        new Thread(new Runnable() {
            @Override
            public void run() {

                List<cShippingAgentServiceShipMethodEntity> shippingAgentServiceShipMethodEntities = new ArrayList<>();
                if (forcerefresh) {
                    try {
                        deleteAll();
                        List<PropertyInfo> l_PropertyInfoObl = new ArrayList<>();

                        webResult = new cWebresult().mGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_GETSHIPPINGAGENTSERVICESHIPMETHODS, l_PropertyInfoObl);
                        List<JSONObject> myList = webResult.getResultDtt();
                        for (int i = 0; i < myList.size(); i++) {
                            JSONObject jsonObject;
                            jsonObject = myList.get(i);

                            cShippingAgentServiceShipMethodEntity shippingAgentServiceShipMethodEntity = new cShippingAgentServiceShipMethodEntity(jsonObject);
                            insert(shippingAgentServiceShipMethodEntity);
                            shippingAgentServiceShipMethodEntities.add(shippingAgentServiceShipMethodEntity);
                        }
                        data.postValue(shippingAgentServiceShipMethodEntities);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }//forcerefresh
                else {
                    shippingAgentServiceShipMethodEntities = getAll();
                    data.postValue(shippingAgentServiceShipMethodEntities);
                }
            } //run
        }).start(); //Thread
        return data;
    }
    public List<cShippingAgentServiceShipMethodEntity> getAll() {
        List<cShippingAgentServiceShipMethodEntity> shippingAgentServiceShipMethodEntities = null;
        try {
            shippingAgentServiceShipMethodEntities = new getAllAsyncTask(shippingAgentServiceShipMethodDao).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return shippingAgentServiceShipMethodEntities;
    }
    private static class getAllAsyncTask extends AsyncTask<Void, Void, List<cShippingAgentServiceShipMethodEntity>> {
        private iShippingAgentServiceShipMethodDao mAsyncTaskDao;

        getAllAsyncTask(iShippingAgentServiceShipMethodDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected List<cShippingAgentServiceShipMethodEntity> doInBackground(final Void... params) {
            return mAsyncTaskDao.getAll();
        }
    }
    public void deleteAll () {
        new deleteAllAsyncTask(shippingAgentServiceShipMethodDao).execute();
    }

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
    public void insert(cShippingAgentServiceShipMethodEntity shippingAgentServiceShipMethodEntity) {
        new insertAsyncTask(shippingAgentServiceShipMethodDao).execute(shippingAgentServiceShipMethodEntity);
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

}
