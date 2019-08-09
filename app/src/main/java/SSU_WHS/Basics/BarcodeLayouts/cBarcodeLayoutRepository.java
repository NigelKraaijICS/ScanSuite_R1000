package SSU_WHS.Basics.BarcodeLayouts;

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

public class cBarcodeLayoutRepository {
    private iBarcodeLayoutDao barcodeLayoutDao;
    private cWebresult webResult;

    cBarcodeLayoutRepository(Application application) {
        acScanSuiteDatabase db = acScanSuiteDatabase.getDatabase(application);
        barcodeLayoutDao = db.barcodeLaoutDao();
    }
    public List<cBarcodeLayoutEntity> getLocalBarcodeLayouts() {
        List<cBarcodeLayoutEntity> l_BarcodeEntityObl = null;
        try {
            l_BarcodeEntityObl = new getLocalBarcodeLayoutsAsyncTask(barcodeLayoutDao).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return l_BarcodeEntityObl;
    }
    private static class getLocalBarcodeLayoutsAsyncTask extends AsyncTask<Void, Void, List<cBarcodeLayoutEntity>> {
        private iBarcodeLayoutDao mAsyncTaskDao;

        getLocalBarcodeLayoutsAsyncTask(iBarcodeLayoutDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected List<cBarcodeLayoutEntity> doInBackground(final Void... params) {
            return mAsyncTaskDao.getLocalBarcodeLayouts();
        }
    }
    public LiveData<List<cBarcodeLayoutEntity>> getBarcodeLayouts() {

        final MutableLiveData<List<cBarcodeLayoutEntity>> data = new MutableLiveData<>();

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    List<cBarcodeLayoutEntity> barcodeLayoutsObl = new ArrayList<>();
                    List<PropertyInfo> l_PropertyInfoObl = new ArrayList<>();

                    webResult = new cWebresult().mGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_GETBARCODELAYOUTS, l_PropertyInfoObl);
                    List<JSONObject> myList = webResult.getResultDtt();
                    for(int i = 0;i <myList.size();i++) {
                        JSONObject jsonObject;
                        jsonObject = myList.get(i);

                        cBarcodeLayoutEntity barcodeLayoutEntity = new cBarcodeLayoutEntity(jsonObject);
                        insert(barcodeLayoutEntity);
                        barcodeLayoutsObl.add(barcodeLayoutEntity);
                    }
                    data.postValue(barcodeLayoutsObl);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } //run
        }).start(); //Thread
        return data;
    }
    public void deleteAll () {
        new cBarcodeLayoutRepository.deleteAllAsyncTask(barcodeLayoutDao).execute();
    }

    private static class deleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private iBarcodeLayoutDao mAsyncTaskDao;

        deleteAllAsyncTask(iBarcodeLayoutDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final Void... params) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }
    public void insert(cBarcodeLayoutEntity barcodeLayoutEntity) {
        new insertAsyncTask(barcodeLayoutDao).execute(barcodeLayoutEntity);
    }
    private static class insertAsyncTask extends AsyncTask<cBarcodeLayoutEntity, Void, Void> {
        private iBarcodeLayoutDao mAsyncTaskDao;

        insertAsyncTask(iBarcodeLayoutDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final cBarcodeLayoutEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
    public List<cBarcodeLayoutEntity> getBarcodeLayoutsOfType(final String barcodeType) {
        List<cBarcodeLayoutEntity> l_barcodeLayoutEntityObl = null;
        try {
            l_barcodeLayoutEntityObl = new getBarcodeLayoutsOfTypeAsyncTask(barcodeLayoutDao).execute(barcodeType).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return l_barcodeLayoutEntityObl;
    }
    private static class getBarcodeLayoutsOfTypeAsyncTask extends AsyncTask<String, Void, List<cBarcodeLayoutEntity>> {
        private iBarcodeLayoutDao mAsyncTaskDao;

        getBarcodeLayoutsOfTypeAsyncTask(iBarcodeLayoutDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected List<cBarcodeLayoutEntity> doInBackground(final String... params) {
            return mAsyncTaskDao.getBarcodeLayoutsOfType(params[0]);
        }
    }
}
