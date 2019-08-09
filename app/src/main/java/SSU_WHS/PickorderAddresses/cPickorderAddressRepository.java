package SSU_WHS.PickorderAddresses;

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

public class cPickorderAddressRepository {
    private iPickorderAddressDao pickorderAddressDao;
    private cWebresult webResult;

    cPickorderAddressRepository(Application application) {
        acScanSuiteDatabase db = acScanSuiteDatabase.getDatabase(application);
        pickorderAddressDao = db.pickorderAddressDao();
    }

    public LiveData<List<cPickorderAddressEntity>> getPickorderAddresses(final Boolean forcerefresh, final String branchStr, final String ordernumberStr) {

        final MutableLiveData<List<cPickorderAddressEntity>> data = new MutableLiveData<>();

        new Thread(new Runnable() {
            @Override
            public void run() {

                List<cPickorderAddressEntity> pickorderAddressesObl = new ArrayList<>();
                if (forcerefresh) {
                    try {
                        deleteAll();
                        List<PropertyInfo> l_PropertyInfoObl = new ArrayList<>();

                        PropertyInfo l_PropertyInfo1Pin = new PropertyInfo();
                        l_PropertyInfo1Pin.name = cWebserviceDefinitions.WEBPROPERTY_BRANCH;
                        l_PropertyInfo1Pin.setValue(branchStr);
                        l_PropertyInfoObl.add(l_PropertyInfo1Pin);

                        PropertyInfo l_PropertyInfo2Pin = new PropertyInfo();
                        l_PropertyInfo2Pin.name = cWebserviceDefinitions.WEBPROPERTY_ORDERNUMBER;
                        l_PropertyInfo2Pin.setValue(ordernumberStr);
                        l_PropertyInfoObl.add(l_PropertyInfo2Pin);

                        webResult = new cWebresult().mGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_GETPICKORDERADDRESSES, l_PropertyInfoObl);
                        List<JSONObject> myList = webResult.getResultDtt();
                        for (int i = 0; i < myList.size(); i++) {
                            JSONObject jsonObject;
                            jsonObject = myList.get(i);

                            cPickorderAddressEntity pickorderAddressEntity = new cPickorderAddressEntity(jsonObject);
                            insert(pickorderAddressEntity);
                            pickorderAddressesObl.add(pickorderAddressEntity);
                        }
                        data.postValue(pickorderAddressesObl);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }//forcerefresh
                else {
                    pickorderAddressesObl = getAll();
                    data.postValue(pickorderAddressesObl);
                }
            } //run
        }).start(); //Thread
        return data;
    }
    public List<cPickorderAddressEntity> getAll() {
        List<cPickorderAddressEntity> pickorderAddressEntityObl = null;
        try {
            pickorderAddressEntityObl = new getAllAsyncTask(pickorderAddressDao).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return pickorderAddressEntityObl;
    }
    private static class getAllAsyncTask extends AsyncTask<Void, Void, List<cPickorderAddressEntity>> {
        private iPickorderAddressDao mAsyncTaskDao;

        getAllAsyncTask(iPickorderAddressDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected List<cPickorderAddressEntity> doInBackground(final Void... params) {
            return mAsyncTaskDao.getAll();
        }
    }
    public void deleteAll () {
        new deleteAllAsyncTask(pickorderAddressDao).execute();
    }

    private static class deleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private iPickorderAddressDao mAsyncTaskDao;

        deleteAllAsyncTask(iPickorderAddressDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final Void... params) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }
    public void insert(cPickorderAddressEntity pickorderAddressEntity) {
        new insertAsyncTask(pickorderAddressDao).execute(pickorderAddressEntity);
    }
    private static class insertAsyncTask extends AsyncTask<cPickorderAddressEntity, Void, Void> {
        private iPickorderAddressDao mAsyncTaskDao;

        insertAsyncTask(iPickorderAddressDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final cPickorderAddressEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
    public cPickorderAddressEntity getAddressByAddresCode(String addresscode) {
        cPickorderAddressEntity pickorderAddressEntity = null;
        try {
            pickorderAddressEntity = new getAddressByAddresCodeAsyncTask(pickorderAddressDao).execute(addresscode).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return pickorderAddressEntity;
    }

    private static class getAddressByAddresCodeAsyncTask extends AsyncTask<String, Void, cPickorderAddressEntity> {
        private iPickorderAddressDao mAsyncTaskDao;
        getAddressByAddresCodeAsyncTask (iPickorderAddressDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected cPickorderAddressEntity doInBackground(String... params) {
            return mAsyncTaskDao.getAddressByAddresCode(params[0]);
        }
    }
}
