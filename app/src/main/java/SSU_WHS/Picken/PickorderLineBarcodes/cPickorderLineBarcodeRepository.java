package SSU_WHS.Picken.PickorderLineBarcodes;

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

public class cPickorderLineBarcodeRepository {
    private iPickorderLineBarcodeDao pickorderLineBarcodeDao;
    private cWebresult webResult;

    cPickorderLineBarcodeRepository(Application application) {
        acScanSuiteDatabase db = acScanSuiteDatabase.getDatabase(application);
        pickorderLineBarcodeDao = db.pickorderLineBarcodeDao();
    }

    public void delete(cPickorderLineBarcodeEntity pickorderLineBarcodeEntity) {
        new deleteAsyncTask(pickorderLineBarcodeDao).execute(pickorderLineBarcodeEntity);
    }
    private static class deleteAsyncTask extends AsyncTask<cPickorderLineBarcodeEntity, Void, Void> {
        private iPickorderLineBarcodeDao mAsyncTaskDao;

        deleteAsyncTask(iPickorderLineBarcodeDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected Void doInBackground(final cPickorderLineBarcodeEntity... params) {
            mAsyncTaskDao.delete(params[0]);
            return null;
        }
    }


    public LiveData<List<cPickorderLineBarcodeEntity>> getPickorderLineBarcodes(final Boolean forcerefresh, final String branchStr, final String ordernumberStr, final String actionTypeCodeStr) {

        final MutableLiveData<List<cPickorderLineBarcodeEntity>> data = new MutableLiveData<>();

        new Thread(new Runnable() {
            @Override
            public void run() {

                List<cPickorderLineBarcodeEntity> pickorderLineBarcodesObl = new ArrayList<>();
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

                        PropertyInfo l_PropertyInfo3Pin = new PropertyInfo();
                        l_PropertyInfo3Pin.name = cWebserviceDefinitions.WEBPROPERTY_ACTIONTYPECODE;
                        l_PropertyInfo3Pin.setValue(actionTypeCodeStr);
                        l_PropertyInfoObl.add(l_PropertyInfo3Pin);

                        webResult = new cWebresult().mGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_GETPICKORDERLINEBARCODES, l_PropertyInfoObl);
                        List<JSONObject> myList = webResult.getResultDtt();
                        for (int i = 0; i < myList.size(); i++) {
                            JSONObject jsonObject;
                            jsonObject = myList.get(i);

                            cPickorderLineBarcodeEntity pickorderLineBarcodeEntity = new cPickorderLineBarcodeEntity(jsonObject);
                            insert(pickorderLineBarcodeEntity);
                            pickorderLineBarcodesObl.add(pickorderLineBarcodeEntity);
                        }
                        data.postValue(pickorderLineBarcodesObl);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }//forcerefresh
                else {
                    pickorderLineBarcodesObl = getAll();
                    data.postValue(pickorderLineBarcodesObl);
                }
            } //run
        }).start(); //Thread
        return data;
    }
    public List<cPickorderLineBarcodeEntity> getPickorderLineBarcodesForLineNo(Integer lineno) {
        List<cPickorderLineBarcodeEntity> pickorderLineBarcodeEntityObl = null;
        try {
            pickorderLineBarcodeEntityObl = new getPickorderLineBarcodesForLineNoAsyncTask(pickorderLineBarcodeDao).execute(lineno).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return pickorderLineBarcodeEntityObl;
    }
    private static class getPickorderLineBarcodesForLineNoAsyncTask extends AsyncTask<Integer, Void, List<cPickorderLineBarcodeEntity>> {
        private iPickorderLineBarcodeDao mAsyncTaskDao;

        getPickorderLineBarcodesForLineNoAsyncTask(iPickorderLineBarcodeDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected List<cPickorderLineBarcodeEntity> doInBackground(final Integer... params) {
            return mAsyncTaskDao.getPickorderLineBarcodesForLineNo(params[0]);
        }
    }
    public List<cPickorderLineBarcodeEntity> getAll() {
        List<cPickorderLineBarcodeEntity> pickorderLineBarcodeEntityObl = null;
        try {
            pickorderLineBarcodeEntityObl = new getAllAsyncTask(pickorderLineBarcodeDao).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return pickorderLineBarcodeEntityObl;
    }
    private static class getAllAsyncTask extends AsyncTask<Void, Void, List<cPickorderLineBarcodeEntity>> {
        private iPickorderLineBarcodeDao mAsyncTaskDao;

        getAllAsyncTask(iPickorderLineBarcodeDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected List<cPickorderLineBarcodeEntity> doInBackground(final Void... params) {
            return mAsyncTaskDao.getAll();
        }
    }
    public void deleteAll () {
        new cPickorderLineBarcodeRepository.deleteAllAsyncTask(pickorderLineBarcodeDao).execute();
    }

    private static class deleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private iPickorderLineBarcodeDao mAsyncTaskDao;

        deleteAllAsyncTask(iPickorderLineBarcodeDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final Void... params) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }
    public void insert(cPickorderLineBarcodeEntity pickorderLineBarcodeEntity) {
        new insertAsyncTask(pickorderLineBarcodeDao).execute(pickorderLineBarcodeEntity);
    }
    private static class insertAsyncTask extends AsyncTask<cPickorderLineBarcodeEntity, Void, Void> {
        private iPickorderLineBarcodeDao mAsyncTaskDao;

        insertAsyncTask(iPickorderLineBarcodeDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final cPickorderLineBarcodeEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
    public cPickorderLineBarcodeEntity getPickorderLineBarcodeByBarcode(String barcode) {
        cPickorderLineBarcodeEntity pickorderLineBarcodeEntity = null;
        try {
            pickorderLineBarcodeEntity = new getPickorderLineBarcodeByBarcodeAsyncTask(pickorderLineBarcodeDao).execute(barcode).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return pickorderLineBarcodeEntity;
    }
    private static class getPickorderLineBarcodeByBarcodeAsyncTask extends AsyncTask<String, Void, cPickorderLineBarcodeEntity> {
        private iPickorderLineBarcodeDao mAsyncTaskDao;
        getPickorderLineBarcodeByBarcodeAsyncTask(iPickorderLineBarcodeDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected cPickorderLineBarcodeEntity doInBackground(String... params) {
            return mAsyncTaskDao.getPickorderLineBarcodeByBarcode(params[0]);
        }
    }

    private static class UpdateBarcodeAmountParams {
        String barcode;
        int amount;

        UpdateBarcodeAmountParams(String barcode, int amount) {
            this.barcode = barcode;
            this.amount = amount;
        }
    }

    public void updateBarcodeAmount(String barcode, int amount) {
        UpdateBarcodeAmountParams updateBarcodeAmountParams = new UpdateBarcodeAmountParams(barcode, amount);
        new updateBarcodeAmountAsyncTask(pickorderLineBarcodeDao).execute(updateBarcodeAmountParams);
    }

    private static class updateBarcodeAmountAsyncTask extends AsyncTask<UpdateBarcodeAmountParams, Void, Void> {
        private iPickorderLineBarcodeDao mAsyncTaskDao;
        updateBarcodeAmountAsyncTask(iPickorderLineBarcodeDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected Void doInBackground(UpdateBarcodeAmountParams... params) {
            mAsyncTaskDao.updateBarcodeAmount(params[0].barcode, params[0].amount);
            return null;
        }
    }
}
