package SSU_WHS.Picken.WarehouseLocations;

import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebservice;
import SSU_WHS.Webservice.cWebserviceDefinitions;
import SSU_WHS.General.acScanSuiteDatabase;

public class cWarehouseLocationRepository {
    private iWarehouseLocationDao warehouseLocationDao;
    private cWebresult webResult;

    cWarehouseLocationRepository(Application application) {
        acScanSuiteDatabase db = acScanSuiteDatabase.pGetDatabase(application);
        warehouseLocationDao = db.warehouseLocationDao();
    }
    public LiveData<List<cWarehouseLocationEntity>> getWarehouseLocations(final Boolean forceRefresh, final String branch, List<String> bins) {

        final MutableLiveData<List<cWarehouseLocationEntity>> data = new MutableLiveData<>();
        final SoapObject soapBins = new SoapObject(cWebservice.WEBSERVICE_NAMESPACE, cWebserviceDefinitions.WEBPROPERTY_BINSUBSET);
        for (String bin: bins) {
            soapBins.addProperty("string", bin);
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<cWarehouseLocationEntity> warehouseLocationsObl = new ArrayList<>();
                if (forceRefresh) {
                    try {
                        deleteAll();
                        List<PropertyInfo> l_PropertyInfoObl = new ArrayList<>();

                        PropertyInfo l_PropertyInfo1Pin = new PropertyInfo();
                        l_PropertyInfo1Pin.name = cWebserviceDefinitions.WEBPROPERTY_LOCATION_NL;
                        l_PropertyInfo1Pin.setValue(branch);
                        l_PropertyInfoObl.add(l_PropertyInfo1Pin);

                        PropertyInfo l_PropertyInfo2Pin = new PropertyInfo();
                        l_PropertyInfo2Pin.name = cWebserviceDefinitions.WEBPROPERTY_BINSUBSET;
                        l_PropertyInfo2Pin.setValue(soapBins);
                        l_PropertyInfoObl.add(l_PropertyInfo2Pin);

                        webResult = new cWebresult().pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_GETWAREHOUSELOCATIONS, l_PropertyInfoObl);
                        List<JSONObject> myList = webResult.getResultDtt();
                        for (int i = 0; i < myList.size(); i++) {
                            JSONObject jsonObject;
                            jsonObject = myList.get(i);

                            cWarehouseLocationEntity warehouseLocationEntity = new cWarehouseLocationEntity(jsonObject);
                            insert(warehouseLocationEntity);
                            warehouseLocationsObl.add(warehouseLocationEntity);
                        }
                        data.postValue(warehouseLocationsObl);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else { //no refresh, get local database
                    warehouseLocationsObl = getAll();
                    data.postValue(warehouseLocationsObl);
                }
            } //run
        }).start(); //Thread
        return data;
    }

    public List<cWarehouseLocationEntity> getAll() {
        List<cWarehouseLocationEntity> l_warehouseLocationEntityObl = null;
        try {
            l_warehouseLocationEntityObl = new getAllAsyncTask(warehouseLocationDao).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return l_warehouseLocationEntityObl;
    }
    private static class getAllAsyncTask extends AsyncTask<Void, Void, List<cWarehouseLocationEntity>> {
        private iWarehouseLocationDao mAsyncTaskDao;

        getAllAsyncTask(iWarehouseLocationDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected List<cWarehouseLocationEntity> doInBackground(final Void... params) {
            return mAsyncTaskDao.getAll();
        }
    }

    public void deleteAll () {
        new cWarehouseLocationRepository.deleteAllAsyncTask(warehouseLocationDao).execute();
    }

    private static class deleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private iWarehouseLocationDao mAsyncTaskDao;

        deleteAllAsyncTask(iWarehouseLocationDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final Void... params) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }

    public void insert (cWarehouseLocationEntity warehouseLocationEntity) {
        new insertAsyncTask(warehouseLocationDao).execute(warehouseLocationEntity);
    }
    private static class insertAsyncTask extends AsyncTask<cWarehouseLocationEntity, Void, Void> {
        private iWarehouseLocationDao mAsyncTaskDao;

        insertAsyncTask(iWarehouseLocationDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final cWarehouseLocationEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
}
