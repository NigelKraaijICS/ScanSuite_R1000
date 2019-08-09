package SSU_WHS.PickorderShipMethods;

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

public class cPickorderShipMethodRepository {
    private iPickorderShipMethodDao pickorderShipMethodDao;
    private cWebresult webResult;

    cPickorderShipMethodRepository(Application application) {
        acScanSuiteDatabase db = acScanSuiteDatabase.getDatabase(application);
        pickorderShipMethodDao = db.pickorderShipMethodDao();
    }

    public LiveData<List<cPickorderShipMethodEntity>> getPickorderShipMethods(final Boolean forcerefresh, final String branch, final String ordernumber) {

        final MutableLiveData<List<cPickorderShipMethodEntity>> data = new MutableLiveData<>();

        new Thread(new Runnable() {
            @Override
            public void run() {

                List<cPickorderShipMethodEntity> pickorderShipMethodEntities = new ArrayList<>();
                if (forcerefresh) {
                    try {
                        deleteAll();
                        List<PropertyInfo> l_PropertyInfoObl = new ArrayList<>();

                        PropertyInfo l_PropertyInfo1Pin = new PropertyInfo();
                        l_PropertyInfo1Pin.name = cWebserviceDefinitions.WEBPROPERTY_BRANCH;
                        l_PropertyInfo1Pin.setValue(branch);
                        l_PropertyInfoObl.add(l_PropertyInfo1Pin);

                        PropertyInfo l_PropertyInfo2Pin = new PropertyInfo();
                        l_PropertyInfo2Pin.name = cWebserviceDefinitions.WEBPROPERTY_ORDERNUMBER;
                        l_PropertyInfo2Pin.setValue(ordernumber);
                        l_PropertyInfoObl.add(l_PropertyInfo2Pin);

                        webResult = new cWebresult().mGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_GETPICKORDERSHIPMETHODS, l_PropertyInfoObl);
                        List<JSONObject> myList = webResult.getResultDtt();
                        for (int i = 0; i < myList.size(); i++) {
                            JSONObject jsonObject;
                            jsonObject = myList.get(i);

                            cPickorderShipMethodEntity shippingAgentServiceShipMethodEntity = new cPickorderShipMethodEntity(jsonObject);
                            insert(shippingAgentServiceShipMethodEntity);
                            pickorderShipMethodEntities.add(shippingAgentServiceShipMethodEntity);
                        }
                        data.postValue(pickorderShipMethodEntities);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }//forcerefresh
                else {
                    pickorderShipMethodEntities = getAll();
                    data.postValue(pickorderShipMethodEntities);
                }
            } //run
        }).start(); //Thread
        return data;
    }
    public List<cPickorderShipMethodEntity> getAll() {
        List<cPickorderShipMethodEntity> pickorderShipMethodEntities = null;
        try {
            pickorderShipMethodEntities = new getAllAsyncTask(pickorderShipMethodDao).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return pickorderShipMethodEntities;
    }
    private static class getAllAsyncTask extends AsyncTask<Void, Void, List<cPickorderShipMethodEntity>> {
        private iPickorderShipMethodDao mAsyncTaskDao;

        getAllAsyncTask(iPickorderShipMethodDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected List<cPickorderShipMethodEntity> doInBackground(final Void... params) {
            return mAsyncTaskDao.getAll();
        }
    }
    public void deleteAll () {
        new deleteAllAsyncTask(pickorderShipMethodDao).execute();
    }

    private static class deleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private iPickorderShipMethodDao mAsyncTaskDao;

        deleteAllAsyncTask(iPickorderShipMethodDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final Void... params) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }
    public void insert(cPickorderShipMethodEntity pickorderShipMethodEntity) {
        new insertAsyncTask(pickorderShipMethodDao).execute(pickorderShipMethodEntity);
    }
    private static class insertAsyncTask extends AsyncTask<cPickorderShipMethodEntity, Void, Void> {
        private iPickorderShipMethodDao mAsyncTaskDao;

        insertAsyncTask(iPickorderShipMethodDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final cPickorderShipMethodEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
    public cPickorderShipMethodEntity getPickorderShipMethodBySourceNo(String sourceno) {
        cPickorderShipMethodEntity pickorderShipMethodEntity = null;
        try {
            pickorderShipMethodEntity = new getPickorderShipMethodBySourceNoAsyncTask(pickorderShipMethodDao).execute(sourceno).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return pickorderShipMethodEntity;
    }
    private static class getPickorderShipMethodBySourceNoAsyncTask extends AsyncTask<String, Void, cPickorderShipMethodEntity> {
        private iPickorderShipMethodDao mAsyncTaskDao;

        getPickorderShipMethodBySourceNoAsyncTask(iPickorderShipMethodDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected cPickorderShipMethodEntity doInBackground(final String... params) {
            return mAsyncTaskDao.getPickorderShipMethodBySourceNo(params[0]);
        }
    }

}
