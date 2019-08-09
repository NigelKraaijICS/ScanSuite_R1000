package SSU_WHS.ShippingAgentServiceShippingUnits;

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

public class cShippingAgentServiceShippingUnitRepository {
    private iShippingAgentServiceShippingUnitDao shippingAgentServiceShippingUnitDao;
    private cWebresult webResult;

    cShippingAgentServiceShippingUnitRepository(Application application) {
        acScanSuiteDatabase db = acScanSuiteDatabase.getDatabase(application);
        shippingAgentServiceShippingUnitDao = db.shippingAgentServiceShippingUnitDao();
    }

    public LiveData<List<cShippingAgentServiceShippingUnitEntity>> getShippingAgentServiceShippingUnits(final Boolean forcerefresh) {

        final MutableLiveData<List<cShippingAgentServiceShippingUnitEntity>> data = new MutableLiveData<>();

        new Thread(new Runnable() {
            @Override
            public void run() {

                List<cShippingAgentServiceShippingUnitEntity> shippingAgentServiceShippingUnitEntities = new ArrayList<>();
                if (forcerefresh) {
                    try {
                        deleteAll();
                        List<PropertyInfo> l_PropertyInfoObl = new ArrayList<>();

                        webResult = new cWebresult().mGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_GETSHIPPINGAGENTSERVICESHPPINGUNITS, l_PropertyInfoObl);
                        List<JSONObject> myList = webResult.getResultDtt();
                        for (int i = 0; i < myList.size(); i++) {
                            JSONObject jsonObject;
                            jsonObject = myList.get(i);

                            cShippingAgentServiceShippingUnitEntity shippingAgentServiceShippingUnitEntity = new cShippingAgentServiceShippingUnitEntity(jsonObject);
                            insert(shippingAgentServiceShippingUnitEntity);
                            shippingAgentServiceShippingUnitEntities.add(shippingAgentServiceShippingUnitEntity);
                        }
                        data.postValue(shippingAgentServiceShippingUnitEntities);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }//forcerefresh
                else {
                    shippingAgentServiceShippingUnitEntities = getAll();
                    data.postValue(shippingAgentServiceShippingUnitEntities);
                }
            } //run
        }).start(); //Thread
        return data;
    }
    public List<cShippingAgentServiceShippingUnitEntity> getAll() {
        List<cShippingAgentServiceShippingUnitEntity> shippingAgentServiceShippingUnitEntities = null;
        try {
            shippingAgentServiceShippingUnitEntities = new getAllAsyncTask(shippingAgentServiceShippingUnitDao).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return shippingAgentServiceShippingUnitEntities;
    }
    private static class getAllAsyncTask extends AsyncTask<Void, Void, List<cShippingAgentServiceShippingUnitEntity>> {
        private iShippingAgentServiceShippingUnitDao mAsyncTaskDao;

        getAllAsyncTask(iShippingAgentServiceShippingUnitDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected List<cShippingAgentServiceShippingUnitEntity> doInBackground(final Void... params) {
            return mAsyncTaskDao.getAll();
        }
    }
    public void deleteAll () {
        new deleteAllAsyncTask(shippingAgentServiceShippingUnitDao).execute();
    }

    private static class deleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private iShippingAgentServiceShippingUnitDao mAsyncTaskDao;

        deleteAllAsyncTask(iShippingAgentServiceShippingUnitDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final Void... params) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }
    public void insert(cShippingAgentServiceShippingUnitEntity shippingAgentServiceShippingUnitEntity) {
        new insertAsyncTask(shippingAgentServiceShippingUnitDao).execute(shippingAgentServiceShippingUnitEntity);
    }
    private static class insertAsyncTask extends AsyncTask<cShippingAgentServiceShippingUnitEntity, Void, Void> {
        private iShippingAgentServiceShippingUnitDao mAsyncTaskDao;

        insertAsyncTask(iShippingAgentServiceShippingUnitDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final cShippingAgentServiceShippingUnitEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
    private static class GetShippingUnitsByAgentAndServiceParams {
        String shippingagent;
        String shippingservice;

        GetShippingUnitsByAgentAndServiceParams(String shippingAgent, String shippingService) {
            this.shippingagent = shippingAgent;
            this.shippingservice = shippingService;
        }
    }

    public List<cShippingAgentServiceShippingUnitEntity> getShippingUnitsByAgentAndService(String shippingAgent, String shippingService) {
        GetShippingUnitsByAgentAndServiceParams shippingUnitsByAgentAndServiceParams = new GetShippingUnitsByAgentAndServiceParams(shippingAgent, shippingService);
        List<cShippingAgentServiceShippingUnitEntity> shippingUnitsByAgentAndService = null;
        try {
            shippingUnitsByAgentAndService = new getShippingUnitsByAgentAndServiceAsyncTask(shippingAgentServiceShippingUnitDao).execute(shippingUnitsByAgentAndServiceParams).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return shippingUnitsByAgentAndService;
    }

    private static class getShippingUnitsByAgentAndServiceAsyncTask extends AsyncTask<GetShippingUnitsByAgentAndServiceParams, Void, List<cShippingAgentServiceShippingUnitEntity>> {
        private iShippingAgentServiceShippingUnitDao mAsyncTaskDao;

        getShippingUnitsByAgentAndServiceAsyncTask(iShippingAgentServiceShippingUnitDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected List<cShippingAgentServiceShippingUnitEntity> doInBackground(final GetShippingUnitsByAgentAndServiceParams... params) {
            return mAsyncTaskDao.getShippingUnitsByAgentAndService(params[0].shippingagent, params[0].shippingservice);
        }
    }
    public LiveData<List<cShippingAgentServiceShippingUnitEntity>> getShippingUnitsByAgentAndServiceLive(String shippingAgent, String shippingService) {
        GetShippingUnitsByAgentAndServiceParams shippingUnitsByAgentAndServiceParams = new GetShippingUnitsByAgentAndServiceParams(shippingAgent, shippingService);
        LiveData<List<cShippingAgentServiceShippingUnitEntity>> shippingUnitsByAgentAndService = null;
        try {
            shippingUnitsByAgentAndService = new getShippingUnitsByAgentAndServiceLiveAsyncTask(shippingAgentServiceShippingUnitDao).execute(shippingUnitsByAgentAndServiceParams).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return shippingUnitsByAgentAndService;
    }

    private static class getShippingUnitsByAgentAndServiceLiveAsyncTask extends AsyncTask<GetShippingUnitsByAgentAndServiceParams, Void, LiveData<List<cShippingAgentServiceShippingUnitEntity>>> {
        private iShippingAgentServiceShippingUnitDao mAsyncTaskDao;

        getShippingUnitsByAgentAndServiceLiveAsyncTask(iShippingAgentServiceShippingUnitDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected LiveData<List<cShippingAgentServiceShippingUnitEntity>> doInBackground(final GetShippingUnitsByAgentAndServiceParams... params) {
            return mAsyncTaskDao.getShippingUnitsByAgentAndServiceLive(params[0].shippingagent, params[0].shippingservice);
        }
    }

    private static class UpdateShippingUnitQuantityUsedParams {
        Integer newquantity;
        String shippingagent;
        String shippingservice;
        String shippingunit;

        UpdateShippingUnitQuantityUsedParams(Integer newQuantity, cShippingAgentServiceShippingUnitEntity shippingAgentServiceShippingUnitEntity) {
            this.newquantity = newQuantity;
            this.shippingagent = shippingAgentServiceShippingUnitEntity.getShippingagent();
            this.shippingservice = shippingAgentServiceShippingUnitEntity.getService();
            this.shippingunit = shippingAgentServiceShippingUnitEntity.getShippingunit();
        }
    }
    public void updateShippingUnitQuantityUsed(Integer newQuantity, cShippingAgentServiceShippingUnitEntity shippingAgentServiceShippingUnitEntity) {
        UpdateShippingUnitQuantityUsedParams updateShippingUnitQuantityUsedParams = new UpdateShippingUnitQuantityUsedParams(newQuantity, shippingAgentServiceShippingUnitEntity);
        new updateShippingUnitQuantityUsedAsyncTask(shippingAgentServiceShippingUnitDao).execute(updateShippingUnitQuantityUsedParams);
    }
    private static class updateShippingUnitQuantityUsedAsyncTask extends AsyncTask<UpdateShippingUnitQuantityUsedParams, Void, Void> {
        private iShippingAgentServiceShippingUnitDao mAsyncTaskDao;

        updateShippingUnitQuantityUsedAsyncTask(iShippingAgentServiceShippingUnitDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected Void doInBackground(final UpdateShippingUnitQuantityUsedParams... params) {
            mAsyncTaskDao.updateShippingUnitQuantityUsed(params[0].newquantity , params[0].shippingagent, params[0].shippingservice, params[0].shippingunit);
            return null;
        }
    }
    public void resetShippingUnitQuantityUsed() {
        new resetShippingUnitQuantityUsedAsyncTask(shippingAgentServiceShippingUnitDao).execute();
    }
    private static class resetShippingUnitQuantityUsedAsyncTask extends AsyncTask<Void, Void, Void> {
        private iShippingAgentServiceShippingUnitDao mAsyncTaskDao;

        resetShippingUnitQuantityUsedAsyncTask(iShippingAgentServiceShippingUnitDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected Void doInBackground(final Void... params) {
            mAsyncTaskDao.resetShippingUnitQuantityUsed();
            return null;
        }
    }

    private static class GetShippingUnitsByAgentAndServiceAndShippingUnitParams {
        String shippingagent;
        String shippingservice;
        String shippingunit;

        GetShippingUnitsByAgentAndServiceAndShippingUnitParams(String shippingAgent, String shippingService, String shippingUnit) {
            this.shippingagent = shippingAgent;
            this.shippingservice = shippingService;
            this.shippingunit = shippingUnit;
        }
    }

    public cShippingAgentServiceShippingUnitEntity getShippingUnitsByAgentAndServiceAndShippingUnit(String shippingAgent, String shippingService, String shippingUnit) {
        GetShippingUnitsByAgentAndServiceAndShippingUnitParams shippingUnitsByAgentAndServiceAndShippingUnitParams = new GetShippingUnitsByAgentAndServiceAndShippingUnitParams(shippingAgent, shippingService, shippingUnit);
        cShippingAgentServiceShippingUnitEntity shippingUnitsByAgentAndServiceAndShippingUnit = null;
        try {
            shippingUnitsByAgentAndServiceAndShippingUnit = new getShippingUnitsByAgentAndServiceAndShippingUnitAsyncTask(shippingAgentServiceShippingUnitDao).execute(shippingUnitsByAgentAndServiceAndShippingUnitParams).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return shippingUnitsByAgentAndServiceAndShippingUnit;
    }

    private static class getShippingUnitsByAgentAndServiceAndShippingUnitAsyncTask extends AsyncTask<GetShippingUnitsByAgentAndServiceAndShippingUnitParams, Void, cShippingAgentServiceShippingUnitEntity> {
        private iShippingAgentServiceShippingUnitDao mAsyncTaskDao;

        getShippingUnitsByAgentAndServiceAndShippingUnitAsyncTask(iShippingAgentServiceShippingUnitDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected cShippingAgentServiceShippingUnitEntity doInBackground(final GetShippingUnitsByAgentAndServiceAndShippingUnitParams... params) {
            return mAsyncTaskDao.getShippingUnitsByAgentAndServiceAndShippingUnit(params[0].shippingagent, params[0].shippingservice, params[0].shippingunit);
        }
    }
}
