package SSU_WHS.Workplaces;

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

import SSU_WHS.Webservice.cWebserviceDefinitions;
import SSU_WHS.acScanSuiteDatabase;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebservice;

public class cWorkplaceRepository {
    private iWorkplaceDao workplaceDao;
    private cWebresult webResult;

    cWorkplaceRepository(Application application) {
        acScanSuiteDatabase db = acScanSuiteDatabase.getDatabase(application);
        workplaceDao = db.workplaceDao();
    }

    public LiveData<List<cWorkplaceEntity>> getWorkplaces(final Boolean forceRefresh, final String branch) {

        final MutableLiveData<List<cWorkplaceEntity>> data = new MutableLiveData<>();

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<cWorkplaceEntity> workplacesObl = new ArrayList<>();
                if (forceRefresh) {
                    try {
                        deleteAll();
                        List<PropertyInfo> l_PropertyInfoObl = new ArrayList<>();

                        PropertyInfo l_PropertyInfo1Pin = new PropertyInfo();
                        l_PropertyInfo1Pin.name = cWebserviceDefinitions.WEBPROPERTY_BRANCH;
                        l_PropertyInfo1Pin.setValue(branch);
                        l_PropertyInfoObl.add(l_PropertyInfo1Pin);

                        webResult = new cWebresult().mGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_GETWORKPLACES, l_PropertyInfoObl);
                        List<JSONObject> myList = webResult.getResultDtt();
                        for (int i = 0; i < myList.size(); i++) {
                            JSONObject jsonObject;
                            jsonObject = myList.get(i);

                            cWorkplaceEntity workplaceEntity = new cWorkplaceEntity(jsonObject);
                            insert(workplaceEntity);
                            workplacesObl.add(workplaceEntity);
                        }
                        data.postValue(workplacesObl);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else { //no refresh, get local database
                    workplacesObl = getAll();
                    data.postValue(workplacesObl);
                }
            } //run
        }).start(); //Thread
        return data;
    }

    public List<cWorkplaceEntity> getAll() {
        List<cWorkplaceEntity> l_workplaceEntityObl = null;
        try {
            l_workplaceEntityObl = new getAllAsyncTask(workplaceDao).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return l_workplaceEntityObl;
    }
    private static class getAllAsyncTask extends AsyncTask<Void, Void, List<cWorkplaceEntity>> {
        private iWorkplaceDao mAsyncTaskDao;

        getAllAsyncTask(iWorkplaceDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected List<cWorkplaceEntity> doInBackground(final Void... params) {
            return mAsyncTaskDao.getAll();
        }
    }

    public void deleteAll () {
        new cWorkplaceRepository.deleteAllAsyncTask(workplaceDao).execute();
    }

    private static class deleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private iWorkplaceDao mAsyncTaskDao;

        deleteAllAsyncTask(iWorkplaceDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final Void... params) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }

    public void insert (cWorkplaceEntity workplaceEntity) {
        new insertAsyncTask(workplaceDao).execute(workplaceEntity);
    }
    private static class insertAsyncTask extends AsyncTask<cWorkplaceEntity, Void, Void> {
        private iWorkplaceDao mAsyncTaskDao;

        insertAsyncTask(iWorkplaceDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final cWorkplaceEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
    public cWorkplaceEntity getWorkplaceByCode(String workplace) {
        cWorkplaceEntity workplaceEntity = null;
        try {
            workplaceEntity = new getWorkplaceByCodeAsyncTask(workplaceDao).execute(workplace).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return workplaceEntity;
    }
    private static class getWorkplaceByCodeAsyncTask extends AsyncTask<String, Void, cWorkplaceEntity> {
        private iWorkplaceDao mAsyncTaskDao;

        getWorkplaceByCodeAsyncTask(iWorkplaceDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected cWorkplaceEntity doInBackground(final String... params) {
            return mAsyncTaskDao.getWorkplaceByCode(params[0]);
        }
    }

}
