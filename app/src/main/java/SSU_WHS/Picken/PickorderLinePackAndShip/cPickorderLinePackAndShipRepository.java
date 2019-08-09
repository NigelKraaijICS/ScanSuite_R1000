package SSU_WHS.Picken.PickorderLinePackAndShip;

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

public class cPickorderLinePackAndShipRepository {
    private iPickorderLinePackAndShipDao pickorderLinePackAndShipDao;
    private cWebresult webResult;

    cPickorderLinePackAndShipRepository(Application application) {
        acScanSuiteDatabase db = acScanSuiteDatabase.getDatabase(application);
        pickorderLinePackAndShipDao = db.pickorderLinePackAndShipDao();
    }

    public LiveData<List<cPickorderLinePackAndShipEntity>> getPickorderLinePackAndShips(final Boolean forcerefresh, final String branchStr, final String ordernumberStr) {

        final MutableLiveData<List<cPickorderLinePackAndShipEntity>> data = new MutableLiveData<>();

        new Thread(new Runnable() {
            @Override
            public void run() {

                List<cPickorderLinePackAndShipEntity> pickorderLinePackAndShipEntities = new ArrayList<>();
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

                        webResult = new cWebresult().mGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_GETPICKORDERLINESPACKANDSHIP, l_PropertyInfoObl);
                        List<JSONObject> myList = webResult.getResultDtt();
                        for (int i = 0; i < myList.size(); i++) {
                            JSONObject jsonObject;
                            jsonObject = myList.get(i);

                            cPickorderLinePackAndShipEntity pickorderLineBarcodeEntity = new cPickorderLinePackAndShipEntity(jsonObject);
                            insert(pickorderLineBarcodeEntity);
                            pickorderLinePackAndShipEntities.add(pickorderLineBarcodeEntity);
                        }
                        data.postValue(pickorderLinePackAndShipEntities);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }//forcerefresh
                else {
                    pickorderLinePackAndShipEntities = getAll();
                    data.postValue(pickorderLinePackAndShipEntities);
                }
            } //run
        }).start(); //Thread
        return data;
    }

    public List<cPickorderLinePackAndShipEntity> getAll() {
        List<cPickorderLinePackAndShipEntity> pickorderLinePackAndShipEntities = null;
        try {
            pickorderLinePackAndShipEntities = new getAllAsyncTask(pickorderLinePackAndShipDao).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return pickorderLinePackAndShipEntities;
    }
    private static class getAllAsyncTask extends AsyncTask<Void, Void, List<cPickorderLinePackAndShipEntity>> {
        private iPickorderLinePackAndShipDao mAsyncTaskDao;

        getAllAsyncTask(iPickorderLinePackAndShipDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected List<cPickorderLinePackAndShipEntity> doInBackground(final Void... params) {
            return mAsyncTaskDao.getAll();
        }
    }
    public void deleteAll () {
        new deleteAllAsyncTask(pickorderLinePackAndShipDao).execute();
    }

    private static class deleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private iPickorderLinePackAndShipDao mAsyncTaskDao;

        deleteAllAsyncTask(iPickorderLinePackAndShipDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final Void... params) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }
    public void insert(cPickorderLinePackAndShipEntity pickorderLinePackAndShipEntity) {
        new insertAsyncTask(pickorderLinePackAndShipDao).execute(pickorderLinePackAndShipEntity);
    }
    private static class insertAsyncTask extends AsyncTask<cPickorderLinePackAndShipEntity, Void, Void> {
        private iPickorderLinePackAndShipDao mAsyncTaskDao;

        insertAsyncTask(iPickorderLinePackAndShipDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final cPickorderLinePackAndShipEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
    public void delete(cPickorderLinePackAndShipEntity pickorderLinePackAndShipEntity) {
        new deleteAsyncTask(pickorderLinePackAndShipDao).execute(pickorderLinePackAndShipEntity);
    }
    private static class deleteAsyncTask extends AsyncTask<cPickorderLinePackAndShipEntity, Void, Void> {
        private iPickorderLinePackAndShipDao mAsyncTaskDao;

        deleteAsyncTask(iPickorderLinePackAndShipDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected Void doInBackground(final cPickorderLinePackAndShipEntity... params) {
            mAsyncTaskDao.deletePickorder(params[0]);
            return null;
        }
    }
    public Double getTotalArticles() {
        Double doubleValue = Double.valueOf(0);
        try {
            doubleValue = new getTotalArticlesAsyncTask(pickorderLinePackAndShipDao).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return doubleValue;
    }

    private static class getTotalArticlesAsyncTask extends AsyncTask<Void, Void, Double> {
        private iPickorderLinePackAndShipDao mAsyncTaskDao;

        getTotalArticlesAsyncTask(iPickorderLinePackAndShipDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected Double doInBackground(Void... params) {
            return mAsyncTaskDao.getTotalArticles();
        }
    }

    public Double getHandledArticles() {
        Double doubleValue = Double.valueOf(0);
        try {
            doubleValue = new getHandledArticlesAsyncTask(pickorderLinePackAndShipDao).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();

        }
        return doubleValue;
    }

    private static class getHandledArticlesAsyncTask extends AsyncTask<Void, Void, Double> {
        private iPickorderLinePackAndShipDao mAsyncTaskDao;

        getHandledArticlesAsyncTask(iPickorderLinePackAndShipDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected Double doInBackground(Void... params) {
            return mAsyncTaskDao.getHandledArticles();
        }
    }
    public LiveData<List<cPickorderLinePackAndShipEntity>> getTotalPickorderLinePackAndShipEntities() {
        LiveData<List<cPickorderLinePackAndShipEntity>> l_pickorderLinePackAndShipEntityObl = null;
        try {
            l_pickorderLinePackAndShipEntityObl = new getTotalPickorderLinePackAndShipEntitiesAsyncTask(pickorderLinePackAndShipDao).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return l_pickorderLinePackAndShipEntityObl;
    }

    private static class getTotalPickorderLinePackAndShipEntitiesAsyncTask extends AsyncTask<Void, Void, LiveData<List<cPickorderLinePackAndShipEntity>>> {
        private iPickorderLinePackAndShipDao mAsyncTaskDao;

        getTotalPickorderLinePackAndShipEntitiesAsyncTask(iPickorderLinePackAndShipDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected LiveData<List<cPickorderLinePackAndShipEntity>> doInBackground(final Void... params) {
            return mAsyncTaskDao.getTotalPickorderLinePackAndShipEntities();
        }
    }

    public LiveData<List<cPickorderLinePackAndShipEntity>> getHandledPickorderLinePackAndShipEntities() {
        LiveData<List<cPickorderLinePackAndShipEntity>> l_pickorderLinePackAndShipEntityObl = null;
        try {
            l_pickorderLinePackAndShipEntityObl = new getHandledPickorderLinePackAndShipEntitiesAsyncTask(pickorderLinePackAndShipDao).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return l_pickorderLinePackAndShipEntityObl;
    }

    private static class getHandledPickorderLinePackAndShipEntitiesAsyncTask extends AsyncTask<Void, Void, LiveData<List<cPickorderLinePackAndShipEntity>>> {
        private iPickorderLinePackAndShipDao mAsyncTaskDao;

        getHandledPickorderLinePackAndShipEntitiesAsyncTask(iPickorderLinePackAndShipDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected LiveData<List<cPickorderLinePackAndShipEntity>> doInBackground(final Void... params) {
            return mAsyncTaskDao.getHandledPickorderLinePackAndShipEntities();
        }
    }
    public LiveData<List<cPickorderLinePackAndShipEntity>> getNotHandledPickorderLinePackAndShipEntities() {
        LiveData<List<cPickorderLinePackAndShipEntity>> l_pickorderLinePackAndShipEntityObl = null;
        try {
            l_pickorderLinePackAndShipEntityObl = new getNotHandledPickorderLinePackAndShipEntitiesAsyncTask(pickorderLinePackAndShipDao).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return l_pickorderLinePackAndShipEntityObl;
    }

    private static class getNotHandledPickorderLinePackAndShipEntitiesAsyncTask extends AsyncTask<Void, Void, LiveData<List<cPickorderLinePackAndShipEntity>>> {
        private iPickorderLinePackAndShipDao mAsyncTaskDao;

        getNotHandledPickorderLinePackAndShipEntitiesAsyncTask(iPickorderLinePackAndShipDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected LiveData<List<cPickorderLinePackAndShipEntity>> doInBackground(final Void... params) {
            return mAsyncTaskDao.getNotHandledPickorderLinePackAndShipEntities();
        }
    }

    public LiveData<List<cPickorderLinePackAndShipGroupBySourceNo>> getNotHandledPickorderinePackAndShipEntitiesDistinctSourceno() {
        LiveData<List<cPickorderLinePackAndShipGroupBySourceNo>> l_pickorderLinePackAndShipEntityObl = null;
        try {
            l_pickorderLinePackAndShipEntityObl = new getNotHandledPickorderinePackAndShipEntitiesDistinctSourcenoAsyncTask(pickorderLinePackAndShipDao).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return l_pickorderLinePackAndShipEntityObl;
    }

    private static class getNotHandledPickorderinePackAndShipEntitiesDistinctSourcenoAsyncTask extends AsyncTask<Void, Void, LiveData<List<cPickorderLinePackAndShipGroupBySourceNo>>> {
        private iPickorderLinePackAndShipDao mAsyncTaskDao;

        getNotHandledPickorderinePackAndShipEntitiesDistinctSourcenoAsyncTask(iPickorderLinePackAndShipDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected LiveData<List<cPickorderLinePackAndShipGroupBySourceNo>> doInBackground(final Void... params) {
            return mAsyncTaskDao.getNotHandledPickorderinePackAndShipEntitiesDistinctSourceno();
        }
    }


    public LiveData<List<cPickorderLinePackAndShipGroupBySourceNo>> getHandledPickorderinePackAndShipEntitiesDistinctSourceno() {
        LiveData<List<cPickorderLinePackAndShipGroupBySourceNo>> l_pickorderLinePackAndShipEntityObl = null;
        try {
            l_pickorderLinePackAndShipEntityObl = new getHandledPickorderinePackAndShipEntitiesDistinctSourcenoAsyncTask(pickorderLinePackAndShipDao).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return l_pickorderLinePackAndShipEntityObl;
    }

    private static class getHandledPickorderinePackAndShipEntitiesDistinctSourcenoAsyncTask extends AsyncTask<Void, Void, LiveData<List<cPickorderLinePackAndShipGroupBySourceNo>>> {
        private iPickorderLinePackAndShipDao mAsyncTaskDao;

        getHandledPickorderinePackAndShipEntitiesDistinctSourcenoAsyncTask(iPickorderLinePackAndShipDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected LiveData<List<cPickorderLinePackAndShipGroupBySourceNo>> doInBackground(final Void... params) {
            return mAsyncTaskDao.getHandledPickorderinePackAndShipEntitiesDistinctSourceno();
        }
    }

    public LiveData<List<cPickorderLinePackAndShipGroupBySourceNo>> getAllPickorderinePackAndShipEntitiesDistinctSourceno() {
        LiveData<List<cPickorderLinePackAndShipGroupBySourceNo>> l_pickorderLinePackAndShipEntityObl = null;
        try {
            l_pickorderLinePackAndShipEntityObl = new getAllPickorderinePackAndShipEntitiesDistinctSourcenoAsyncTask(pickorderLinePackAndShipDao).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return l_pickorderLinePackAndShipEntityObl;
    }

    private static class getAllPickorderinePackAndShipEntitiesDistinctSourcenoAsyncTask extends AsyncTask<Void, Void, LiveData<List<cPickorderLinePackAndShipGroupBySourceNo>>> {
        private iPickorderLinePackAndShipDao mAsyncTaskDao;

        getAllPickorderinePackAndShipEntitiesDistinctSourcenoAsyncTask(iPickorderLinePackAndShipDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected LiveData<List<cPickorderLinePackAndShipGroupBySourceNo>> doInBackground(final Void... params) {
            return mAsyncTaskDao.getAllPickorderinePackAndShipEntitiesDistinctSourceno();
        }
    }

    public List<cPickorderLinePackAndShipEntity> getNotHandledPickorderLinePackAndShipEntitiesBySourceNo(String sourceno) {
        List<cPickorderLinePackAndShipEntity> l_pickorderLinePackAndShipEntityObl = null;
        try {
            l_pickorderLinePackAndShipEntityObl = new getNotHandledPickorderLinePackAndShipEntitiesBySourceNoAsyncTask(pickorderLinePackAndShipDao).execute(sourceno).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return l_pickorderLinePackAndShipEntityObl;
    }

    private static class getNotHandledPickorderLinePackAndShipEntitiesBySourceNoAsyncTask extends AsyncTask<String, Void, List<cPickorderLinePackAndShipEntity>> {
        private iPickorderLinePackAndShipDao mAsyncTaskDao;

        getNotHandledPickorderLinePackAndShipEntitiesBySourceNoAsyncTask(iPickorderLinePackAndShipDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected List<cPickorderLinePackAndShipEntity> doInBackground(final String... params) {
            return mAsyncTaskDao.getNotHandledPickorderLinePackAndShipEntitiesBySourceNo(params[0]);
        }
    }

    public List<cPickorderLinePackAndShipEntity> getNotHandledPickorderLinePackAndShipEntitiesByProcessingSequence(String processingsequence) {
        List<cPickorderLinePackAndShipEntity> l_pickorderLinePackAndShipEntityObl = null;
        try {
            l_pickorderLinePackAndShipEntityObl = new getNotHandledPickorderLinePackAndShipEntitiesByProcessingSequenceAsyncTask(pickorderLinePackAndShipDao).execute(processingsequence).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return l_pickorderLinePackAndShipEntityObl;
    }
    private static class getNotHandledPickorderLinePackAndShipEntitiesByProcessingSequenceAsyncTask extends AsyncTask<String, Void, List<cPickorderLinePackAndShipEntity>> {
        private iPickorderLinePackAndShipDao mAsyncTaskDao;

        getNotHandledPickorderLinePackAndShipEntitiesByProcessingSequenceAsyncTask(iPickorderLinePackAndShipDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected List<cPickorderLinePackAndShipEntity> doInBackground(final String... params) {
            return mAsyncTaskDao.getNotHandledPickorderLinePackAndShipEntitiesByProcessingSequence(params[0]);
        }
    }




    private static class UpdateOrderlinePackAndShipQuantityParams {
        Integer recordid;
        Double quantity;

        UpdateOrderlinePackAndShipQuantityParams(Integer pv_recordid, Double pv_quantity) {
            this.recordid = pv_recordid;
            this.quantity = pv_quantity;
        }
    }


    public int updateOrderLinePackAndShipQuantity(Integer pv_recordid, Double pv_quantity) {
        Integer integerValue = 0;
        UpdateOrderlinePackAndShipQuantityParams updateOrderlinePackAndShipQuantityParams = new UpdateOrderlinePackAndShipQuantityParams(pv_recordid, pv_quantity);
        try {
            integerValue = new updateOrderLinePackAndShipQuantityAsyncTask(pickorderLinePackAndShipDao).execute(updateOrderlinePackAndShipQuantityParams).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return integerValue;
    }
    private static class updateOrderLinePackAndShipQuantityAsyncTask extends AsyncTask<UpdateOrderlinePackAndShipQuantityParams, Void, Integer> {
        private iPickorderLinePackAndShipDao mAsyncTaskDao;
        updateOrderLinePackAndShipQuantityAsyncTask(iPickorderLinePackAndShipDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected Integer doInBackground(UpdateOrderlinePackAndShipQuantityParams... params) {
            return mAsyncTaskDao.updateOrderLinePackAndShipQuantity(params[0].recordid, params[0].quantity);
        }
    }
    private static class UpdateOrderlinePackAndShipLocalStatusParams {
        Integer recordid;
        Integer newstatus;

        UpdateOrderlinePackAndShipLocalStatusParams(Integer pv_recordid, Integer pv_newstatus) {
            this.recordid = pv_recordid;
            this.newstatus = pv_newstatus;
        }

    }

    public int updateOrderLinePackAndShipLocalStatus(Integer pv_recordid, Integer pv_newstatus) {
        Integer integerValue = 0;
        UpdateOrderlinePackAndShipLocalStatusParams updateOrderlineLocaStatusParams = new UpdateOrderlinePackAndShipLocalStatusParams(pv_recordid, pv_newstatus);
        try {
            integerValue = new updateOrderLinePackAndShipLocalStatusAsyncTask(pickorderLinePackAndShipDao).execute(updateOrderlineLocaStatusParams).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return integerValue;
    }
    private static class updateOrderLinePackAndShipLocalStatusAsyncTask extends AsyncTask<UpdateOrderlinePackAndShipLocalStatusParams, Void, Integer> {
        private iPickorderLinePackAndShipDao mAsyncTaskDao;
        updateOrderLinePackAndShipLocalStatusAsyncTask(iPickorderLinePackAndShipDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected Integer doInBackground(UpdateOrderlinePackAndShipLocalStatusParams... params) {
            return mAsyncTaskDao.updateOrderLinePackAndShipLocalStatus(params[0].recordid, params[0].newstatus);
        }
    }


    private static class UpdateOrderlinePackAndShipLocalStatusBySourceNoParams {
        String sourceno;
        Integer newstatus;

        UpdateOrderlinePackAndShipLocalStatusBySourceNoParams(String pv_sourceno, Integer pv_newstatus) {
            this.sourceno = pv_sourceno;
            this.newstatus = pv_newstatus;
        }

    }

    public int updateOrderLinePackAndShipLocalStatusBySourceno(String pv_sourceno, Integer pv_newstatus) {
        Integer integerValue = 0;
        UpdateOrderlinePackAndShipLocalStatusBySourceNoParams updateOrderlineLocaStatusParams = new UpdateOrderlinePackAndShipLocalStatusBySourceNoParams(pv_sourceno, pv_newstatus);
        try {
            integerValue = new updateOrderLinePackAndShipLocalStatusBySourceNoAsyncTask(pickorderLinePackAndShipDao).execute(updateOrderlineLocaStatusParams).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return integerValue;
    }
    private static class updateOrderLinePackAndShipLocalStatusBySourceNoAsyncTask extends AsyncTask<UpdateOrderlinePackAndShipLocalStatusBySourceNoParams, Void, Integer> {
        private iPickorderLinePackAndShipDao mAsyncTaskDao;
        updateOrderLinePackAndShipLocalStatusBySourceNoAsyncTask(iPickorderLinePackAndShipDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected Integer doInBackground(UpdateOrderlinePackAndShipLocalStatusBySourceNoParams... params) {
            return mAsyncTaskDao.updateOrderLinePackAndShipLocalStatusBySourceNo(params[0].sourceno, params[0].newstatus);
        }
    }

    public int getNumberToShipForCounter() {
        Integer integerValue = 0;
        try {
            integerValue = new getNumberToShipForCounterAsyncTask(pickorderLinePackAndShipDao).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return integerValue;
    }
    private static class getNumberToShipForCounterAsyncTask extends AsyncTask<Void, Void, Integer> {
        private iPickorderLinePackAndShipDao mAsyncTaskDao;
        getNumberToShipForCounterAsyncTask(iPickorderLinePackAndShipDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected Integer doInBackground(Void... params) {
            return mAsyncTaskDao.getNumberToShipForCounter();
        }
    }
    public int getNumberShippedForCounter() {
        Integer integerValue = 0;
        try {
            integerValue = new getNumberShippedForCounterAsyncTask(pickorderLinePackAndShipDao).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return integerValue;
    }
    private static class getNumberShippedForCounterAsyncTask extends AsyncTask<Void, Void, Integer> {
        private iPickorderLinePackAndShipDao mAsyncTaskDao;
        getNumberShippedForCounterAsyncTask(iPickorderLinePackAndShipDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected Integer doInBackground(Void... params) {
            return mAsyncTaskDao.getNumberShippedForCounter();
        }
    }
    public int getNumberTotalForCounter() {
        Integer integerValue = 0;
        try {
            integerValue = new getNumberTotalForCounterAsyncTask(pickorderLinePackAndShipDao).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return integerValue;
    }
    private static class getNumberTotalForCounterAsyncTask extends AsyncTask<Void, Void, Integer> {
        private iPickorderLinePackAndShipDao mAsyncTaskDao;
        getNumberTotalForCounterAsyncTask(iPickorderLinePackAndShipDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected Integer doInBackground(Void... params) {
            return mAsyncTaskDao.getNumberTotalForCounter();
        }
    }


}
