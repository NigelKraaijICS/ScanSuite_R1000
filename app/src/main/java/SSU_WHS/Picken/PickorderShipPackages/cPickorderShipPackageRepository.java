package SSU_WHS.Picken.PickorderShipPackages;

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

public class cPickorderShipPackageRepository {
    private iPickorderShipPackageDao pickorderShipPackageDao;
    private cWebresult webResult;

    cPickorderShipPackageRepository(Application application) {
        acScanSuiteDatabase db = acScanSuiteDatabase.getDatabase(application);
        pickorderShipPackageDao = db.pickorderShipPackageDao();
    }

    public LiveData<List<cPickorderShipPackageEntity>> getPickorderShipPackages(final Boolean forcerefresh, final String branch, final String ordernumber) {

        final MutableLiveData<List<cPickorderShipPackageEntity>> data = new MutableLiveData<>();

        new Thread(new Runnable() {
            @Override
            public void run() {

                List<cPickorderShipPackageEntity> pickorderShipPackageEntities = new ArrayList<>();
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

                        webResult = new cWebresult().mGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_GETPICKORDERSHIPPACKAGES, l_PropertyInfoObl);
                        List<JSONObject> myList = webResult.getResultDtt();
                        for (int i = 0; i < myList.size(); i++) {
                            JSONObject jsonObject;
                            jsonObject = myList.get(i);

                            cPickorderShipPackageEntity pickorderShipPackageEntity = new cPickorderShipPackageEntity(jsonObject);
                            insert(pickorderShipPackageEntity);
                            pickorderShipPackageEntities.add(pickorderShipPackageEntity);
                        }
                        data.postValue(pickorderShipPackageEntities);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }//forcerefresh
                else {
                    pickorderShipPackageEntities = getAll();
                    data.postValue(pickorderShipPackageEntities);
                }
            } //run
        }).start(); //Thread
        return data;
    }
    public List<cPickorderShipPackageEntity> getAll() {
        List<cPickorderShipPackageEntity> pickorderShipPackageEntities = null;
        try {
            pickorderShipPackageEntities = new getAllAsyncTask(pickorderShipPackageDao).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return pickorderShipPackageEntities;
    }
    private static class getAllAsyncTask extends AsyncTask<Void, Void, List<cPickorderShipPackageEntity>> {
        private iPickorderShipPackageDao mAsyncTaskDao;

        getAllAsyncTask(iPickorderShipPackageDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected List<cPickorderShipPackageEntity> doInBackground(final Void... params) {
            return mAsyncTaskDao.getAll();
        }
    }
    public void deleteAll () {
        new deleteAllAsyncTask(pickorderShipPackageDao).execute();
    }

    private static class deleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private iPickorderShipPackageDao mAsyncTaskDao;

        deleteAllAsyncTask(iPickorderShipPackageDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final Void... params) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }
    public void insert(cPickorderShipPackageEntity pickorderShipPackageEntity) {
        new insertAsyncTask(pickorderShipPackageDao).execute(pickorderShipPackageEntity);
    }
    private static class insertAsyncTask extends AsyncTask<cPickorderShipPackageEntity, Void, Void> {
        private iPickorderShipPackageDao mAsyncTaskDao;

        insertAsyncTask(iPickorderShipPackageDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final cPickorderShipPackageEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

}
