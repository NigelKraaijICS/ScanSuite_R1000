package SSU_WHS.Branches;

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

public class cBranchRepository {
    private iBranchDao branchDao;
    private cWebresult webResult;

    cBranchRepository(Application application) {
        acScanSuiteDatabase db = acScanSuiteDatabase.getDatabase(application);
        branchDao = db.branchDao();
    }

    public LiveData<List<cBranchEntity>> getBranchesForUser(final Boolean forceRefresh, final String user) {

        final MutableLiveData<List<cBranchEntity>> data = new MutableLiveData<>();

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<cBranchEntity> branchessObl = new ArrayList<>();
                if (forceRefresh) {
                    try {
                        deleteAll();
                        List<PropertyInfo> l_PropertyInfoObl = new ArrayList<>();

                        PropertyInfo l_PropertyInfo1Pin = new PropertyInfo();
                        l_PropertyInfo1Pin.name = cWebserviceDefinitions.WEBPROPERTY_USER;
                        l_PropertyInfo1Pin.setValue(user);
                        l_PropertyInfoObl.add(l_PropertyInfo1Pin);

                        webResult = new cWebresult().mGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_GETBRANCHESFORUSER, l_PropertyInfoObl);
                        List<JSONObject> myList = webResult.getResultDtt();
                        for (int i = 0; i < myList.size(); i++) {
                            JSONObject jsonObject;
                            jsonObject = myList.get(i);

                            cBranchEntity branchEntity = new cBranchEntity(jsonObject);
                            insert(branchEntity);
                            branchessObl.add(branchEntity);
                        }
                        data.postValue(branchessObl);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else { //no refresh, get local database
                    branchessObl = getAll();
                    data.postValue(branchessObl);
                }
            } //run
        }).start(); //Thread
        return data;
    }

    public List<cBranchEntity> getAll() {
        List<cBranchEntity> l_branchEntityObl = null;
        try {
            l_branchEntityObl = new getAllAsyncTask(branchDao).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return l_branchEntityObl;
    }
    private static class getAllAsyncTask extends AsyncTask<Void, Void, List<cBranchEntity>> {
        private iBranchDao mAsyncTaskDao;

        getAllAsyncTask(iBranchDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected List<cBranchEntity> doInBackground(final Void... params) {
            return mAsyncTaskDao.getAll();
        }
    }

    public void deleteAll () {
        new cBranchRepository.deleteAllAsyncTask(branchDao).execute();
    }

    private static class deleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private iBranchDao mAsyncTaskDao;

        deleteAllAsyncTask(iBranchDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final Void... params) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }

    public void insert (cBranchEntity branchEntity) {
        new insertAsyncTask(branchDao).execute(branchEntity);
    }
    private static class insertAsyncTask extends AsyncTask<cBranchEntity, Void, Void> {
        private iBranchDao mAsyncTaskDao;

        insertAsyncTask(iBranchDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final cBranchEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
    public cBranchEntity getBranchByCode(String branch) {
        cBranchEntity branchEntity = null;
        try {
            branchEntity = new getBranchByCodeAsyncTask(branchDao).execute(branch).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return branchEntity;
    }
    private static class getBranchByCodeAsyncTask extends AsyncTask<String, Void, cBranchEntity> {
        private iBranchDao mAsyncTaskDao;

        getBranchByCodeAsyncTask(iBranchDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected cBranchEntity doInBackground(final String... params) {
            return mAsyncTaskDao.getBranchByCode(params[0]);
        }
    }

}
