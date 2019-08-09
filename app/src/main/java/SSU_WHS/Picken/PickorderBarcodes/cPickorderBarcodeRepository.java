package SSU_WHS.Picken.PickorderBarcodes;

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

public class cPickorderBarcodeRepository {
    private iPickorderBarcodeDao pickorderBarcodeDao;
    private cWebresult webResult;

    private class itemnoVariantCodeParams {
        String itemno;
        String variantcode;

        itemnoVariantCodeParams(String pv_itemnoStr, String pv_variantcodeStr) {
            this.itemno = pv_itemnoStr;
            this.variantcode = pv_variantcodeStr;
        }
    }

    cPickorderBarcodeRepository(Application application) {
        acScanSuiteDatabase db = acScanSuiteDatabase.getDatabase(application);
        pickorderBarcodeDao = db.pickorderBarcodeDao();
    }

    public LiveData<List<cPickorderBarcodeEntity>> getPickorderBarcodes(final Boolean forcerefresh, final String branchStr, final String ordernumberStr) {

        final MutableLiveData<List<cPickorderBarcodeEntity>> data = new MutableLiveData<>();

        new Thread(new Runnable() {
            @Override
            public void run() {

                List<cPickorderBarcodeEntity> pickorderBarcodesObl = new ArrayList<>();
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

                        webResult = new cWebresult().mGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_GETPICKORDERBARCODES, l_PropertyInfoObl);
                        List<JSONObject> myList = webResult.getResultDtt();
                        for (int i = 0; i < myList.size(); i++) {
                            JSONObject jsonObject;
                            jsonObject = myList.get(i);

                            cPickorderBarcodeEntity pickorderBarcodeEntity = new cPickorderBarcodeEntity(jsonObject);
                            insert(pickorderBarcodeEntity);
                            pickorderBarcodesObl.add(pickorderBarcodeEntity);
                        }
                        data.postValue(pickorderBarcodesObl);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }//forcerefresh
                else {
                    pickorderBarcodesObl = getAll();
                    data.postValue(pickorderBarcodesObl);
                }
            } //run
        }).start(); //Thread
        return data;
    }

    public List<cPickorderBarcodeEntity> getAll() {
        List<cPickorderBarcodeEntity> pickorderBarcodeEntityObl = null;
        try {
            pickorderBarcodeEntityObl = new getAllAsyncTask(pickorderBarcodeDao).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return pickorderBarcodeEntityObl;
    }
    private static class getAllAsyncTask extends AsyncTask<Void, Void, List<cPickorderBarcodeEntity>> {
        private iPickorderBarcodeDao mAsyncTaskDao;

        getAllAsyncTask(iPickorderBarcodeDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected List<cPickorderBarcodeEntity> doInBackground(final Void... params) {
            return mAsyncTaskDao.getAll();
        }
    }
    public void deleteAll () {
        new cPickorderBarcodeRepository.deleteAllAsyncTask(pickorderBarcodeDao).execute();
    }

    private static class deleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private iPickorderBarcodeDao mAsyncTaskDao;

        deleteAllAsyncTask(iPickorderBarcodeDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final Void... params) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }
    public void insert(cPickorderBarcodeEntity pickorderBarcodeEntity) {
        new insertAsyncTask(pickorderBarcodeDao).execute(pickorderBarcodeEntity);
    }
    private static class insertAsyncTask extends AsyncTask<cPickorderBarcodeEntity, Void, Void> {
        private iPickorderBarcodeDao mAsyncTaskDao;

        insertAsyncTask(iPickorderBarcodeDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final cPickorderBarcodeEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
    public List<cPickorderBarcodeEntity> getPickorderBarcodesForItemVariantCode(String itemno, String variantcode) {
        List<cPickorderBarcodeEntity> pickorderBarcodeEntityObl = null;
        itemnoVariantCodeParams itemnoVariantCodeParams = new itemnoVariantCodeParams(itemno, variantcode);
        try {
            pickorderBarcodeEntityObl = new getPickorderBarcodesForItemVariantCodeAsyncTask(pickorderBarcodeDao).execute(itemnoVariantCodeParams).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return pickorderBarcodeEntityObl;
    }
    private static class getPickorderBarcodesForItemVariantCodeAsyncTask extends AsyncTask<itemnoVariantCodeParams, Void, List<cPickorderBarcodeEntity>> {
        private iPickorderBarcodeDao mAsyncTaskDao;

        getPickorderBarcodesForItemVariantCodeAsyncTask(iPickorderBarcodeDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected List<cPickorderBarcodeEntity> doInBackground(final itemnoVariantCodeParams... params) {
            return mAsyncTaskDao.getPickorderBarcodesForItemnoVariant(params[0].itemno, params[0].variantcode);
        }
    }

    public cPickorderBarcodeEntity getPickOrderBarcodeByBarcode(String barcode) {
        cPickorderBarcodeEntity pickorderBarcodeEntity = null;
        try {
            pickorderBarcodeEntity = new getPickOrderBarcodeByBarcodeAsyncTask(pickorderBarcodeDao).execute(barcode).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return pickorderBarcodeEntity;
    }
    private static class getPickOrderBarcodeByBarcodeAsyncTask extends AsyncTask<String, Void, cPickorderBarcodeEntity> {
        private iPickorderBarcodeDao mAsyncTaskDao;

        getPickOrderBarcodeByBarcodeAsyncTask(iPickorderBarcodeDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected cPickorderBarcodeEntity doInBackground(final String... params) {
            return mAsyncTaskDao.getPickorderBarcodesByBarcode(params[0]);
        }
    }


}
