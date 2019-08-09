package SSU_WHS.PickorderLines;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import SSU_WHS.Complex_types.c_BarcodeHandledUwbh;
import SSU_WHS.Webservice.cBarcode;
import SSU_WHS.Webservice.cContainer;
import SSU_WHS.Webservice.cWebserviceDefinitions;
import SSU_WHS.acScanSuiteDatabase;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebservice;

public class cPickorderLineRepository {
    private iPickorderLineDao pickorderLineDao;
    private cWebresult webResult;
    private LiveData<List<cPickorderLineEntity>> mLocalPickorderLiness;

    cPickorderLineRepository(Application application) {
        acScanSuiteDatabase db = acScanSuiteDatabase.getDatabase(application);
        pickorderLineDao = db.pickorderLineDao();
    }

    private static class UpdateOrderlineQuantityParams {
        Integer recordid;
        Double quantity;

        UpdateOrderlineQuantityParams(Integer pv_recordid, Double pv_quantity) {
            this.recordid = pv_recordid;
            this.quantity = pv_quantity;
        }
    }

    private static class UpdateOrderlineProcessingSequenceParams {
        Integer recordid;
        String processingsequence;

        UpdateOrderlineProcessingSequenceParams(Integer pv_recordid, String pv_processingsequence) {
            this.recordid = pv_recordid;
            this.processingsequence = pv_processingsequence;
        }
    }


    private static class PickorderLineResetParams {
        String user;
        String branch;
        String ordernumber;
        Long lineno;

        PickorderLineResetParams(String pv_user, String pv_branch, String pv_ordernumber, Long pv_lineno) {
            this.user = pv_user;
            this.branch = pv_branch;
            this.ordernumber = pv_ordernumber;
            this.lineno = pv_lineno;
        }
    }

    private static class UpdateOrderlineLocaStatusParams {
        Integer recordid;
        Integer newstatus;

        UpdateOrderlineLocaStatusParams(Integer pv_recordid, Integer pv_newstatus) {
            this.recordid = pv_recordid;
            this.newstatus = pv_newstatus;
        }

    }
    private static class ItemNoandVariantCodeParams{
        String itemno;
        String variantcode;

        ItemNoandVariantCodeParams(String pv_itemNo, String pv_VariantCode) {
            this.itemno = pv_itemNo;
            this.variantcode = pv_VariantCode;
        }
    }
    private static class UpdateSortOrderLineParams{
        Integer recordid;
        Integer number;
        String location;

        UpdateSortOrderLineParams(Integer pv_recordid, Integer pv_number, String pv_location) {
            this.recordid = pv_recordid;
            this.number = pv_number;
            this.location = pv_location;
        }
    }


    public List<cPickorderLineEntity> getLocalPickorderLines() {
        List<cPickorderLineEntity> l_PickorderLineEntityObl = null;
        try {
            l_PickorderLineEntityObl = new getLocalPickorderLinesAsyncTask(pickorderLineDao).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return l_PickorderLineEntityObl;
    }

    public cPickorderLineEntity getPickorderLineByItemNo(String itemno) {
        cPickorderLineEntity pickorderLineEntity = null;
        try {
            pickorderLineEntity = new getPickorderLineByItemnoAsyncTask(pickorderLineDao).execute(itemno).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return pickorderLineEntity;
    }

    public Double getTotalArticles() {
        Double doubleValue = Double.valueOf(0);
        try {
            doubleValue = new getTotalArticlesAsyncTask(pickorderLineDao).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return doubleValue;
    }

    private static class getTotalArticlesAsyncTask extends AsyncTask<Void, Void, Double> {
        private iPickorderLineDao mAsyncTaskDao;

        getTotalArticlesAsyncTask(iPickorderLineDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected Double doInBackground(Void... params) {
            return mAsyncTaskDao.getTotalArticles();
        }
    }

    public Double getHandledArticles() {
        Double doubleValue = Double.valueOf(0);
        try {
            doubleValue = new getHandledArticlesAsyncTask(pickorderLineDao).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();

        }
        return doubleValue;
    }

    private static class getHandledArticlesAsyncTask extends AsyncTask<Void, Void, Double> {
        private iPickorderLineDao mAsyncTaskDao;

        getHandledArticlesAsyncTask(iPickorderLineDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected Double doInBackground(Void... params) {
            return mAsyncTaskDao.getHandledArticles();
        }
    }


    private static class getLocalPickorderLinesAsyncTask extends AsyncTask<Void, Void, List<cPickorderLineEntity>> {
        private iPickorderLineDao mAsyncTaskDao;

        getLocalPickorderLinesAsyncTask(iPickorderLineDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected List<cPickorderLineEntity> doInBackground(final Void... params) {
            return mAsyncTaskDao.getLocalPickorderLines();
        }
    }
    private static class getPickorderLineByItemnoAsyncTask extends AsyncTask<String, Void, cPickorderLineEntity> {
        private iPickorderLineDao mAsyncTaskDao;
        getPickorderLineByItemnoAsyncTask(iPickorderLineDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected cPickorderLineEntity doInBackground(String... params) {
            return mAsyncTaskDao.getPickorderLineByItemno(params[0]);
        }
    }

    LiveData<List<cPickorderLineEntity>> getPickorderLines(final Boolean forcerefresh,
                                                           final String pickorderType,
                                                           final String branchStr,
                                                           final String ordernumberStr,
                                                           final String actionTypeCodeStr,
                                                           final String ExtraField1,
                                                           final String ExtraField2,
                                                           final String ExtraField3,
                                                           final String ExtraField4,
                                                           final String ExtraField5,
                                                           final String ExtraField6,
                                                           final String ExtraField7,
                                                           final String ExtraField8) {

        final MutableLiveData<List<cPickorderLineEntity>> data = new MutableLiveData<>();

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<cPickorderLineEntity> l_pickorderLineObl = new ArrayList<>();
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

                        webResult = new cWebresult().mGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_GETPICKORDERLINES, l_PropertyInfoObl);
                        List<JSONObject> myList = webResult.getResultDtt();
                        for (int i = 0; i < myList.size(); i++) {
                            JSONObject jsonObject;
                            jsonObject = myList.get(i);

                            cPickorderLineEntity pickorderLineEntity = new cPickorderLineEntity(jsonObject, pickorderType, ExtraField1, ExtraField2, ExtraField3, ExtraField4,ExtraField5,ExtraField6,ExtraField7,ExtraField8);
                            insert(pickorderLineEntity);
                            l_pickorderLineObl.add(pickorderLineEntity);
                        }
                        data.postValue(l_pickorderLineObl);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }//forcerefresh
                else {
                    l_pickorderLineObl = getAll();
                    data.postValue(l_pickorderLineObl);
                }
            } //run

        }).start(); //Thread
        return data;
    }

    public void pickOrderLineHandled(final String userName, final String branch, final String orderNumber, final Long lineNumber, final String handledTimeStamp, final String containerHandled, final List<c_BarcodeHandledUwbh> barcodes, final String containers, final String processingSequence ) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    List<PropertyInfo> l_PropertyInfoObl = new ArrayList<>();

                    PropertyInfo l_PropertyInfo1Pin = new PropertyInfo();
                    l_PropertyInfo1Pin.name = cWebserviceDefinitions.WEBPROPERTY_USERNAMEDUTCH;
                    l_PropertyInfo1Pin.setValue(userName);
                    l_PropertyInfoObl.add(l_PropertyInfo1Pin);

                    PropertyInfo l_PropertyInfo2Pin = new PropertyInfo();
                    l_PropertyInfo2Pin.name = cWebserviceDefinitions.WEBPROPERTY_BRANCH;
                    l_PropertyInfo2Pin.setValue(branch);
                    l_PropertyInfoObl.add(l_PropertyInfo2Pin);

                    PropertyInfo l_PropertyInfo3Pin = new PropertyInfo();
                    l_PropertyInfo3Pin.name = cWebserviceDefinitions.WEBPROPERTY_ORDERNUMBER;
                    l_PropertyInfo3Pin.setValue(orderNumber);
                    l_PropertyInfoObl.add(l_PropertyInfo3Pin);

                    PropertyInfo l_PropertyInfo4Pin = new PropertyInfo();
                    l_PropertyInfo4Pin.name = cWebserviceDefinitions.WEBPROPERTY_LINENOTAKE;
                    l_PropertyInfo4Pin.setValue(lineNumber);
                    l_PropertyInfoObl.add(l_PropertyInfo4Pin);

                    PropertyInfo l_PropertyInfo5Pin = new PropertyInfo();
                    l_PropertyInfo5Pin.name = cWebserviceDefinitions.WEBPROPERTY_HANDLEDTIMESTAMP;
                    l_PropertyInfo5Pin.setValue(handledTimeStamp);
                    l_PropertyInfoObl.add(l_PropertyInfo5Pin);

                    PropertyInfo l_PropertyInfo6Pin = new PropertyInfo();
                    l_PropertyInfo6Pin.name = cWebserviceDefinitions.WEBPROPERTY_PICKFROMCONTAINER;
                    l_PropertyInfo6Pin.setValue(containerHandled);
                    l_PropertyInfoObl.add(l_PropertyInfo6Pin);

                    SoapObject barcodesHandledList = new SoapObject(cWebservice.WEBSERVICE_NAMESPACE, cWebserviceDefinitions.WEBPROPERTY_BARCODESLIST);
                    for (c_BarcodeHandledUwbh barcodeHandledUwbh: barcodes) {
                        SoapObject soapObject = new SoapObject(cWebservice.WEBSERVICE_NAMESPACE, cWebserviceDefinitions.WEBPROPERTY_BARCODEHANDLED_COMPLEX);
                        soapObject.addProperty(cWebserviceDefinitions.WEBPROPERTY_BARCODE_COMPLEX, barcodeHandledUwbh.getG_BarcodeStr());
                        soapObject.addProperty(cWebserviceDefinitions.WEBPROPERTY_QUANTITYHANDLED_COMPLEX, barcodeHandledUwbh.getG_QuantityHandledDbl());
                        barcodesHandledList.addSoapObject(soapObject);
                    }

                    PropertyInfo l_PropertyInfo7Pin = new PropertyInfo();
                    l_PropertyInfo7Pin.name = cWebserviceDefinitions.WEBPROPERTY_BARCODELIST;
                    l_PropertyInfo7Pin.setValue(barcodesHandledList);
                    l_PropertyInfoObl.add(l_PropertyInfo7Pin);

                    PropertyInfo l_PropertyInfo8Pin = new PropertyInfo();
                    l_PropertyInfo8Pin.name = cWebserviceDefinitions.WEBPROPERTY_CONTAINERSLIST;
                    l_PropertyInfo8Pin.setValue(containers);
                    l_PropertyInfoObl.add(l_PropertyInfo8Pin);

                    PropertyInfo l_PropertyInfo9Pin = new PropertyInfo();
                    l_PropertyInfo9Pin.name = cWebserviceDefinitions.WEBPROPERTY_PROCESSINGSEQUENCE;
                    l_PropertyInfo9Pin.setValue(processingSequence);
                    l_PropertyInfoObl.add(l_PropertyInfo9Pin);

                    webResult = new cWebresult().mGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_PICKORDERLINE_HANDLED, l_PropertyInfoObl);
                    List<JSONObject> myList = webResult.getResultDtt();
                    for (int i = 0; i < myList.size(); i++) {
                        JSONObject jsonObject;
                        jsonObject = myList.get(i);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } //run
        }).start(); //Thread

    }

//    public cWebresult pickOrderLineHandled(final String userName, final String branch, final String orderNumber, final Long lineNumber, final String handledTimeStamp, final String containerHandled, final List<c_BarcodeHandledUwbh> barcodes, final String containers, final String processingSequence ) throws InterruptedException, ExecutionException {
//
//        ExecutorService executorService = Executors.newSingleThreadExecutor();
//
//        Callable<cWebresult> callable = new Callable<cWebresult>() {
//            @Override
//            public cWebresult call() throws Exception {
//                try {
//
//                    List<PropertyInfo> l_PropertyInfoObl = new ArrayList<>();
//
//                    PropertyInfo l_PropertyInfo1Pin = new PropertyInfo();
//                    l_PropertyInfo1Pin.name = cWebservice.WEBPROPERTY_USERNAMEDUTCH;
//                    l_PropertyInfo1Pin.setValue(userName);
//                    l_PropertyInfoObl.add(l_PropertyInfo1Pin);
//
//                    PropertyInfo l_PropertyInfo2Pin = new PropertyInfo();
//                    l_PropertyInfo2Pin.name = cWebservice.WEBPROPERTY_BRANCH;
//                    l_PropertyInfo2Pin.setValue(branch);
//                    l_PropertyInfoObl.add(l_PropertyInfo2Pin);
//
//                    PropertyInfo l_PropertyInfo3Pin = new PropertyInfo();
//                    l_PropertyInfo3Pin.name = cWebservice.WEBPROPERTY_ORDERNUMBER;
//                    l_PropertyInfo3Pin.setValue(orderNumber);
//                    l_PropertyInfoObl.add(l_PropertyInfo3Pin);
//
//                    PropertyInfo l_PropertyInfo4Pin = new PropertyInfo();
//                    l_PropertyInfo4Pin.name = cWebservice.WEBPROPERTY_LINENOTAKE;
//                    l_PropertyInfo4Pin.setValue(lineNumber);
//                    l_PropertyInfoObl.add(l_PropertyInfo4Pin);
//
//                    PropertyInfo l_PropertyInfo5Pin = new PropertyInfo();
//                    l_PropertyInfo5Pin.name = cWebservice.WEBPROPERTY_HANDLEDTIMESTAMP;
//                    l_PropertyInfo5Pin.setValue(handledTimeStamp);
//                    l_PropertyInfoObl.add(l_PropertyInfo5Pin);
//
//                    PropertyInfo l_PropertyInfo6Pin = new PropertyInfo();
//                    l_PropertyInfo6Pin.name = cWebservice.WEBPROPERTY_PICKFROMCONTAINER;
//                    l_PropertyInfo6Pin.setValue(containerHandled);
//                    l_PropertyInfoObl.add(l_PropertyInfo6Pin);
//
//                    SoapObject barcodesHandledList = new SoapObject(cWebservice.WEBSERVICE_NAMESPACE, cWebservice.WEBPROPERTY_BARCODESLIST);
//                    for (c_BarcodeHandledUwbh barcodeHandledUwbh: barcodes) {
//                        SoapObject soapObject = new SoapObject(cWebservice.WEBSERVICE_NAMESPACE, cWebservice.WEBPROPERTY_BARCODEHANDLED_COMPLEX);
//                        soapObject.addProperty(cWebservice.WEBPROPERTY_BARCODE_COMPLEX, barcodeHandledUwbh.getG_BarcodeStr());
//                        soapObject.addProperty(cWebservice.WEBPROPERTY_QUANTITYHANDLED_COMPLEX, barcodeHandledUwbh.getG_QuantityHandledDbl());
//                        barcodesHandledList.addSoapObject(soapObject);
//                    }
//
//                    PropertyInfo l_PropertyInfo7Pin = new PropertyInfo();
//                    l_PropertyInfo7Pin.name = cWebservice.WEBPROPERTY_BARCODELIST;
//                    l_PropertyInfo7Pin.setValue(barcodesHandledList);
//                    l_PropertyInfoObl.add(l_PropertyInfo7Pin);
//
//                    PropertyInfo l_PropertyInfo8Pin = new PropertyInfo();
//                    l_PropertyInfo8Pin.name = cWebservice.WEBPROPERTY_CONTAINERSLIST;
//                    l_PropertyInfo8Pin.setValue(containers);
//                    l_PropertyInfoObl.add(l_PropertyInfo8Pin);
//
//                    PropertyInfo l_PropertyInfo9Pin = new PropertyInfo();
//                    l_PropertyInfo9Pin.name = cWebservice.WEBPROPERTY_PROCESSINGSEQUENCE;
//                    l_PropertyInfo9Pin.setValue(processingSequence);
//                    l_PropertyInfoObl.add(l_PropertyInfo9Pin);
//
//                    webResult = new cWebresult().mGetwebresultWrs(cWebservice.WEBMETHOD_PICKORDERLINE_HANDLED, l_PropertyInfoObl);
//
//                    List<JSONObject> myList = webResult.getResultDtt();
//                    for (int i = 0; i < myList.size(); i++) {
//                        JSONObject jsonObject;
//                        jsonObject = myList.get(i);
//
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                return webResult;
//            }
//        };
//        Future<cWebresult> future = executorService.submit(callable);
//        cWebresult webresult = future.get();
//        executorService.shutdown();
//        return  webresult;
//    }


    public void sortOrderLineHandled(final String userName, final String branch, final String orderNumber, final Long lineNumber, final String handledTimeStamp, final Double number, final String barcode, final String propertiesHandled, final String containers, final String  location) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    List<PropertyInfo> l_PropertyInfoObl = new ArrayList<>();

                    PropertyInfo l_PropertyInfo1Pin = new PropertyInfo();
                    l_PropertyInfo1Pin.name = cWebserviceDefinitions.WEBPROPERTY_USERNAMEDUTCH;
                    l_PropertyInfo1Pin.setValue(userName);
                    l_PropertyInfoObl.add(l_PropertyInfo1Pin);

                    PropertyInfo l_PropertyInfo2Pin = new PropertyInfo();
                    l_PropertyInfo2Pin.name = cWebserviceDefinitions.WEBPROPERTY_BRANCH;
                    l_PropertyInfo2Pin.setValue(branch);
                    l_PropertyInfoObl.add(l_PropertyInfo2Pin);

                    PropertyInfo l_PropertyInfo3Pin = new PropertyInfo();
                    l_PropertyInfo3Pin.name = cWebserviceDefinitions.WEBPROPERTY_ORDERNUMBER;
                    l_PropertyInfo3Pin.setValue(orderNumber);
                    l_PropertyInfoObl.add(l_PropertyInfo3Pin);

                    PropertyInfo l_PropertyInfo4Pin = new PropertyInfo();
                    l_PropertyInfo4Pin.name = cWebserviceDefinitions.WEBPROPERTY_LINENOPLACE;
                    l_PropertyInfo4Pin.setValue(lineNumber);
                    l_PropertyInfoObl.add(l_PropertyInfo4Pin);

                    PropertyInfo l_PropertyInfo5Pin = new PropertyInfo();
                    l_PropertyInfo5Pin.name = cWebserviceDefinitions.WEBPROPERTY_HANDLEDTIMESTAMP;
                    l_PropertyInfo5Pin.setValue(handledTimeStamp);
                    l_PropertyInfoObl.add(l_PropertyInfo5Pin);

                    PropertyInfo l_PropertyInfo6Pin = new PropertyInfo();
                    l_PropertyInfo6Pin.name = cWebserviceDefinitions.WEBPROPERTY_NUMBER;
                    l_PropertyInfo6Pin.setValue(number);
                    l_PropertyInfoObl.add(l_PropertyInfo6Pin);

                    PropertyInfo l_PropertyInfo7Pin = new PropertyInfo();
                    l_PropertyInfo7Pin.name = cWebserviceDefinitions.WEBPROPERTY_BARCODE;
                    l_PropertyInfo7Pin.setValue(barcode);
                    l_PropertyInfoObl.add(l_PropertyInfo7Pin);

                    PropertyInfo l_PropertyInfo8Pin = new PropertyInfo();
                    l_PropertyInfo8Pin.name = cWebserviceDefinitions.WEBPROPERTY_PROPERTIESHANDLED;
                    l_PropertyInfo8Pin.setValue(propertiesHandled);
                    l_PropertyInfoObl.add(l_PropertyInfo8Pin);

                    PropertyInfo l_PropertyInfo9Pin = new PropertyInfo();
                    l_PropertyInfo9Pin.name = cWebserviceDefinitions.WEBPROPERTY_CONTAINERSLIST;
                    l_PropertyInfo9Pin.setValue(containers);
                    l_PropertyInfoObl.add(l_PropertyInfo9Pin);

                    PropertyInfo l_PropertyInfo10Pin = new PropertyInfo();
                    l_PropertyInfo10Pin.name = cWebserviceDefinitions.WEBPROPERTY_LOCATION;
                    l_PropertyInfo10Pin.setValue(location);
                    l_PropertyInfoObl.add(l_PropertyInfo10Pin);

                    webResult = new cWebresult().mGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_SORTORDERLINE_HANDLED, l_PropertyInfoObl);
                    List<JSONObject> myList = webResult.getResultDtt();
                    for (int i = 0; i < myList.size(); i++) {
                        JSONObject jsonObject;
                        jsonObject = myList.get(i);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } //run
        }).start(); //Thread
    }

    public List<cPickorderLineEntity> getAll() {
        List<cPickorderLineEntity> l_pickorderLineEntityObl = null;
        try {
            l_pickorderLineEntityObl = new getAllAsyncTask(pickorderLineDao).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return l_pickorderLineEntityObl;
    }
    private static class getAllAsyncTask extends AsyncTask<Void, Void, List<cPickorderLineEntity>> {
        private iPickorderLineDao mAsyncTaskDao;

        getAllAsyncTask(iPickorderLineDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected List<cPickorderLineEntity> doInBackground(final Void... params) {
            return mAsyncTaskDao.getAll();
        }
    }
    public void delete(cPickorderLineEntity pickorderLineEntity) {
        new deleteAsyncTask(pickorderLineDao).execute(pickorderLineEntity);
    }
    private static class deleteAsyncTask extends AsyncTask<cPickorderLineEntity, Void, Void> {
        private iPickorderLineDao mAsyncTaskDao;

        deleteAsyncTask(iPickorderLineDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected Void doInBackground(final cPickorderLineEntity... params) {
            mAsyncTaskDao.deletePickorder(params[0]);
            return null;
        }
    }

    public void deleteAll () {
        new cPickorderLineRepository.deleteAllAsyncTask(pickorderLineDao).execute();
    }

    private static class deleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private iPickorderLineDao mAsyncTaskDao;

        deleteAllAsyncTask(iPickorderLineDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final Void... params) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }

    public void insert (cPickorderLineEntity pickorderLineEntity) {
        new insertAsyncTask(pickorderLineDao).execute(pickorderLineEntity);
    }
    private static class insertAsyncTask extends AsyncTask<cPickorderLineEntity, Void, Void> {
        private iPickorderLineDao mAsyncTaskDao;

        insertAsyncTask(iPickorderLineDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final cPickorderLineEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
    public LiveData<List<cPickorderLineEntity>> getPickorderLineEntitiesToSend() {
        LiveData<List<cPickorderLineEntity>> pickOrdersToSend = null;
        try {
            pickOrdersToSend = new getPickorderLineEntitiesToSendAsyncTask(pickorderLineDao).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return pickOrdersToSend;
    }
    private static class getPickorderLineEntitiesToSendAsyncTask extends AsyncTask<Void, Void, LiveData<List<cPickorderLineEntity>>> {
        private iPickorderLineDao mAsyncTaskDao;

        getPickorderLineEntitiesToSendAsyncTask(iPickorderLineDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected LiveData<List<cPickorderLineEntity>> doInBackground(final Void... params) {
            return mAsyncTaskDao.getPickorderLineEntitiesToSend();
        }
    }

    public LiveData<List<cPickorderLineEntity>> getTotalPickorderLineEntities() {
        LiveData<List<cPickorderLineEntity>> l_pickorderLineEntityObl = null;
        try {
            l_pickorderLineEntityObl = new getTotalPickorderLineEntitiesAsyncTask(pickorderLineDao).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return l_pickorderLineEntityObl;
    }

    private static class getTotalPickorderLineEntitiesAsyncTask extends AsyncTask<Void, Void, LiveData<List<cPickorderLineEntity>>> {
        private iPickorderLineDao mAsyncTaskDao;

        getTotalPickorderLineEntitiesAsyncTask(iPickorderLineDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected LiveData<List<cPickorderLineEntity>> doInBackground(final Void... params) {
            return mAsyncTaskDao.getTotalPickorderLineEntities();
        }
    }

    public LiveData<List<cPickorderLineEntity>> getHandledPickorderLineEntities() {
        LiveData<List<cPickorderLineEntity>> l_pickorderLineEntityObl = null;
        try {
            l_pickorderLineEntityObl = new getHandledPickorderLineEntitiesAsyncTask(pickorderLineDao).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return l_pickorderLineEntityObl;
    }

    private static class getHandledPickorderLineEntitiesAsyncTask extends AsyncTask<Void, Void, LiveData<List<cPickorderLineEntity>>> {
        private iPickorderLineDao mAsyncTaskDao;

        getHandledPickorderLineEntitiesAsyncTask(iPickorderLineDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected LiveData<List<cPickorderLineEntity>> doInBackground(final Void... params) {
            return mAsyncTaskDao.getHandledPickorderLineEntities();
        }
    }
    public List<cPickorderLineEntity> getNotHandledPickorderLinesByItemNoandVariantCode(String pv_itemNo, String pv_variantCode) {
        ItemNoandVariantCodeParams itemNoandVariantCodeParams = new ItemNoandVariantCodeParams(pv_itemNo, pv_variantCode);
        List<cPickorderLineEntity> l_pickorderLineEntityObl = null;
        try {
            l_pickorderLineEntityObl = new getNotHandledPickorderLinesByItemNoandVariantCodeAsyncTask(pickorderLineDao).execute(itemNoandVariantCodeParams).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return l_pickorderLineEntityObl;
    }
    private static class getNotHandledPickorderLinesByItemNoandVariantCodeAsyncTask extends AsyncTask<ItemNoandVariantCodeParams, Void, List<cPickorderLineEntity>> {
        private iPickorderLineDao mAsyncTaskDao;

        getNotHandledPickorderLinesByItemNoandVariantCodeAsyncTask(iPickorderLineDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected List<cPickorderLineEntity> doInBackground(final ItemNoandVariantCodeParams... params) {
            return mAsyncTaskDao.getNotHandledPickorderLinesByItemNoandVariantCode(params[0].itemno, params[0].variantcode);
        }
    }

    public List<cPickorderLineEntity> getNotHandledPickorderLineEntitiesLin() {
        List<cPickorderLineEntity> l_pickorderLineEntityObl = null;
        try {
            l_pickorderLineEntityObl = new getNotHandledPickorderLineEntitiesLinAsyncTask(pickorderLineDao).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return l_pickorderLineEntityObl;
    }
    private static class getNotHandledPickorderLineEntitiesLinAsyncTask extends AsyncTask<Void, Void, List<cPickorderLineEntity>> {
        private iPickorderLineDao mAsyncTaskDao;

        getNotHandledPickorderLineEntitiesLinAsyncTask(iPickorderLineDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected List<cPickorderLineEntity> doInBackground(final Void... params) {
            return mAsyncTaskDao.getNotHandledPickorderLineEntitiesLin();
        }
    }

    public LiveData<List<cPickorderLineEntity>> getNotHandledPickorderLineEntities() {
        LiveData<List<cPickorderLineEntity>> l_pickorderLineEntityObl = null;
        try {
            l_pickorderLineEntityObl = new getNotHandledPickorderLineEntitiesAsyncTask(pickorderLineDao).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return l_pickorderLineEntityObl;
    }

    private static class getNotHandledPickorderLineEntitiesAsyncTask extends AsyncTask<Void, Void, LiveData<List<cPickorderLineEntity>>> {
        private iPickorderLineDao mAsyncTaskDao;

        getNotHandledPickorderLineEntitiesAsyncTask(iPickorderLineDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected LiveData<List<cPickorderLineEntity>> doInBackground(final Void... params) {
            return mAsyncTaskDao.getNotHandledPickorderLineEntities();
        }
    }
    public cPickorderLineEntity getPickorderLineNotHandledByBin(String pv_bin) {
        cPickorderLineEntity pickorderLineEntity = null;
        try {
            pickorderLineEntity = new getPickorderLineNotHandledByBinAsyncTask(pickorderLineDao).execute(pv_bin).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return pickorderLineEntity;
    }
    private static class getPickorderLineNotHandledByBinAsyncTask extends AsyncTask<String, Void, cPickorderLineEntity> {
        private iPickorderLineDao mAsyncTaskDao;
        getPickorderLineNotHandledByBinAsyncTask(iPickorderLineDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected cPickorderLineEntity doInBackground(String... params) {
            return mAsyncTaskDao.getPickorderLineNotHandledByBin(params[0]);
        }
    }
    public int updateOrderLineQuantity(Integer pv_recordid, Double pv_quantity) {
        Integer integerValue = 0;
        UpdateOrderlineQuantityParams updateOrderlineQuantityParams = new UpdateOrderlineQuantityParams(pv_recordid, pv_quantity);
        try {
            integerValue = new updateOrderLineQuantityAsyncTask(pickorderLineDao).execute(updateOrderlineQuantityParams).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return integerValue;
    }
    private static class updateOrderLineQuantityAsyncTask extends AsyncTask<UpdateOrderlineQuantityParams, Void, Integer> {
        private iPickorderLineDao mAsyncTaskDao;
        updateOrderLineQuantityAsyncTask(iPickorderLineDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected Integer doInBackground(UpdateOrderlineQuantityParams... params) {
            return mAsyncTaskDao.updateOrderLineQuantity(params[0].recordid, params[0].quantity);
        }
    }

    public int updateOrderLineProcessingSequence(Integer pv_recordid, String pv_processingSequence) {
        Integer integerValue = 0;
        UpdateOrderlineProcessingSequenceParams updateOrderlineProcessingSequenceParams = new UpdateOrderlineProcessingSequenceParams(pv_recordid, pv_processingSequence);
        try {
            integerValue = new updateOrderLineProcessingSequenceAsyncTask(pickorderLineDao).execute(updateOrderlineProcessingSequenceParams).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return integerValue;
    }
    private static class updateOrderLineProcessingSequenceAsyncTask extends AsyncTask<UpdateOrderlineProcessingSequenceParams, Void, Integer> {
        private iPickorderLineDao mAsyncTaskDao;
        updateOrderLineProcessingSequenceAsyncTask(iPickorderLineDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected Integer doInBackground(UpdateOrderlineProcessingSequenceParams... params) {
            return mAsyncTaskDao.updateOrderLineProcessingSequence(params[0].recordid, params[0].processingsequence);
        }
    }



    public void abortOrder() {
        new abortOrderAsyncTask(pickorderLineDao).execute();
    }
    private static class abortOrderAsyncTask extends AsyncTask<Void, Void, Void> {
        private iPickorderLineDao mAsyncTaskDao;
        abortOrderAsyncTask(iPickorderLineDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected Void doInBackground(Void... params) {
            mAsyncTaskDao.abortOrder();
            return null;
        }
    }

    public int updateOrderLineLocalStatus(Integer pv_recordid, Integer pv_newstatus) {
        Integer integerValue = 0;
        UpdateOrderlineLocaStatusParams updateOrderlineLocaStatusParams = new UpdateOrderlineLocaStatusParams(pv_recordid, pv_newstatus);
        try {
            integerValue = new updateOrderLineLocalStatusAsyncTask(pickorderLineDao).execute(updateOrderlineLocaStatusParams).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return integerValue;
    }
    private static class updateOrderLineLocalStatusAsyncTask extends AsyncTask<UpdateOrderlineLocaStatusParams, Void, Integer> {
        private iPickorderLineDao mAsyncTaskDao;
        updateOrderLineLocalStatusAsyncTask(iPickorderLineDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected Integer doInBackground(UpdateOrderlineLocaStatusParams... params) {
            return mAsyncTaskDao.updateOrderLineLocalStatus(params[0].recordid, params[0].newstatus);
        }
    }
    public void updateSortOrderLine(Integer pv_recordid, Integer pv_number, String pv_location) {
        UpdateSortOrderLineParams updateSortOrderParams = new UpdateSortOrderLineParams(pv_recordid, pv_number, pv_location);
        new updateSortOrderAsyncTask(pickorderLineDao).execute(updateSortOrderParams);
    }
    private static class updateSortOrderAsyncTask extends AsyncTask<UpdateSortOrderLineParams, Void, Void> {
        private iPickorderLineDao mAsyncTaskDao;
        updateSortOrderAsyncTask(iPickorderLineDao dao) { mAsyncTaskDao = dao;}
        @Override
        protected Void doInBackground(UpdateSortOrderLineParams... params) {
            mAsyncTaskDao.updateSortOrderLine(params[0].recordid, params[0].number, params[0].location);
            return null;
        }
    }


    public cPickorderLineEntity getNextPickLineFromLocation(String pv_location) {
        cPickorderLineEntity pickorderLineEntity = null;
        try {
            pickorderLineEntity = new getNextPickLineFromLocationAsyncTask(pickorderLineDao).execute(pv_location).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return pickorderLineEntity;
    }
    private static class getNextPickLineFromLocationAsyncTask extends AsyncTask<String, Void, cPickorderLineEntity> {
        private iPickorderLineDao mAsyncTaskDao;
        getNextPickLineFromLocationAsyncTask(iPickorderLineDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected cPickorderLineEntity doInBackground(String... params) {
            return mAsyncTaskDao.getNextPickLineFromLocation(params[0]);
        }
    }

    public cPickorderLineEntity getNextPickLineFromSourceNo(String pv_sourceno) {
        cPickorderLineEntity pickorderLineEntity = null;
        try {
            pickorderLineEntity = new getNextPickLineFromSourceNoAsyncTask(pickorderLineDao).execute(pv_sourceno).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return pickorderLineEntity;
    }
    private static class getNextPickLineFromSourceNoAsyncTask extends AsyncTask<String, Void, cPickorderLineEntity> {
        private iPickorderLineDao mAsyncTaskDao;
        getNextPickLineFromSourceNoAsyncTask(iPickorderLineDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected cPickorderLineEntity doInBackground(String... params) {
            return mAsyncTaskDao.getNextPickLineFromSourceNo(params[0]);
        }
    }

    public cPickorderLineEntity getPickLineByRecordid(Integer pv_recordid) {
        cPickorderLineEntity pickorderLineEntity = null;
        try {
            pickorderLineEntity = new getNextPickLineByRecordidAsyncTask(pickorderLineDao).execute(pv_recordid).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return pickorderLineEntity;
    }
    private static class getNextPickLineByRecordidAsyncTask extends AsyncTask<Integer, Void, cPickorderLineEntity> {
        private iPickorderLineDao mAsyncTaskDao;
        getNextPickLineByRecordidAsyncTask(iPickorderLineDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected cPickorderLineEntity doInBackground(Integer... params) {
            return mAsyncTaskDao.getPickLineByRecordid(params[0]);
        }
    }


    public Boolean pickorderlineReset(String user, String branch, String orderNumber, Long lineno) {
        PickorderLineResetParams pickorderLineResetParams = new PickorderLineResetParams(user, branch, orderNumber, lineno);
        cWebresult l_webResult;
        Boolean l_resultBln;
        try {
            l_webResult = new pickorderineResetAsyncTask().execute(pickorderLineResetParams).get();
            if (l_webResult != null) {
                if (!l_webResult.getSuccessBln()) {
                    l_resultBln = false;
                    return l_resultBln;
                }
                if (!l_webResult.getResultBln()) {
                    l_resultBln = false;
                    return l_resultBln;
                }
                l_resultBln = true;
                return l_resultBln;
            }
            else {
                l_resultBln = false;
            }
        } catch (InterruptedException e) {
            l_resultBln = false;
            e.printStackTrace();
        } catch (ExecutionException e) {
            l_resultBln = false;
            e.printStackTrace();
        }
        return l_resultBln;
    }

    private static class pickorderineResetAsyncTask extends AsyncTask<PickorderLineResetParams, Void, cWebresult> {
        @Override
        protected cWebresult doInBackground(PickorderLineResetParams... params) {
            cWebresult l_WebresultWrs = new cWebresult();
            try {
                List<PropertyInfo> l_PropertyInfoObl = new ArrayList<>();

                PropertyInfo l_PropertyInfo1Pin = new PropertyInfo();
                l_PropertyInfo1Pin.name = cWebserviceDefinitions.WEBPROPERTY_USERNAMEDUTCH;
                l_PropertyInfo1Pin.setValue(params[0].user);
                l_PropertyInfoObl.add(l_PropertyInfo1Pin);

                PropertyInfo l_PropertyInfo2Pin = new PropertyInfo();
                l_PropertyInfo2Pin.name = cWebserviceDefinitions.WEBPROPERTY_BRANCH;
                l_PropertyInfo2Pin.setValue(params[0].branch);
                l_PropertyInfoObl.add(l_PropertyInfo2Pin);

                PropertyInfo l_PropertyInfo3Pin = new PropertyInfo();
                l_PropertyInfo3Pin.name = cWebserviceDefinitions.WEBPROPERTY_ORDERNUMBER;
                l_PropertyInfo3Pin.setValue(params[0].ordernumber);
                l_PropertyInfoObl.add(l_PropertyInfo3Pin);

                PropertyInfo l_PropertyInfo4Pin = new PropertyInfo();
                l_PropertyInfo4Pin.name = cWebserviceDefinitions.WEBPROPERTY_LINENOTAKE;
                l_PropertyInfo4Pin.setValue(params[0].lineno);
                l_PropertyInfoObl.add(l_PropertyInfo4Pin);

                l_WebresultWrs = new cWebresult().mGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_PICKORDERLINERESET, l_PropertyInfoObl);
            } catch (JSONException e) {
                l_WebresultWrs.setSuccessBln(false);
                l_WebresultWrs.setResultBln(false);
            }
            return l_WebresultWrs;
        }
    }

    private static class GetSortorderLineNotHandledByItemNoAndVariantParams {
        String itemno;
        String variant;

        GetSortorderLineNotHandledByItemNoAndVariantParams(String pv_itemno, String pv_variant) {
            this.itemno = pv_itemno;
            this.variant = pv_variant;
        }

    }

    public cPickorderLineEntity getSortorderLineNotHandledByItemNoAndVariant(String itemno, String variant) {
        cPickorderLineEntity pickorderLineEntity = null;
        GetSortorderLineNotHandledByItemNoAndVariantParams getSortorderLineNotHandledByItemNoAndVariantParams = new GetSortorderLineNotHandledByItemNoAndVariantParams(itemno, variant);
        try {
            pickorderLineEntity = new getSortorderLineNotHandledByItemNoAndVariantAsyncTask(pickorderLineDao).execute(getSortorderLineNotHandledByItemNoAndVariantParams).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return pickorderLineEntity;
    }
    private static class getSortorderLineNotHandledByItemNoAndVariantAsyncTask extends AsyncTask<GetSortorderLineNotHandledByItemNoAndVariantParams, Void, cPickorderLineEntity> {
        private iPickorderLineDao mAsyncTaskDao;
        getSortorderLineNotHandledByItemNoAndVariantAsyncTask(iPickorderLineDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected cPickorderLineEntity doInBackground(GetSortorderLineNotHandledByItemNoAndVariantParams... params) {
            return mAsyncTaskDao.getSortorderLineNotHandledByItemNoAndVariant(params[0].itemno, params[0].variant);
        }
    }


    LiveData<List<String>> getSortLocationAdvice(final String ordertype, final String branch, final String ordernumber, final String sourceno) {

        final MutableLiveData<List<String>> data = new MutableLiveData<>();

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<String> advicelist = new ArrayList<>();

                    try {
                        List<PropertyInfo> l_PropertyInfoObl = new ArrayList<>();

                        PropertyInfo l_PropertyInfo1Pin = new PropertyInfo();
                        l_PropertyInfo1Pin.name = cWebserviceDefinitions.WEBPROPERTY_ORDERTYPE;
                        l_PropertyInfo1Pin.setValue(ordertype);
                        l_PropertyInfoObl.add(l_PropertyInfo1Pin);

                        PropertyInfo l_PropertyInfo2Pin = new PropertyInfo();
                        l_PropertyInfo2Pin.name = cWebserviceDefinitions.WEBPROPERTY_BRANCH;
                        l_PropertyInfo2Pin.setValue(branch);
                        l_PropertyInfoObl.add(l_PropertyInfo2Pin);

                        PropertyInfo l_PropertyInfo3Pin = new PropertyInfo();
                        l_PropertyInfo3Pin.name = cWebserviceDefinitions.WEBPROPERTY_ORDERNUMBER;
                        l_PropertyInfo3Pin.setValue(ordernumber);
                        l_PropertyInfoObl.add(l_PropertyInfo3Pin);

                        PropertyInfo l_PropertyInfo4Pin = new PropertyInfo();
                        l_PropertyInfo4Pin.name = cWebserviceDefinitions.WEBPROPERTY_SOURCENO;
                        l_PropertyInfo4Pin.setValue(sourceno);
                        l_PropertyInfoObl.add(l_PropertyInfo4Pin);


                        webResult = new cWebresult().mGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_GETSORTLOCATIONADVICE, l_PropertyInfoObl);
                        advicelist = webResult.getResultObl();

                        data.postValue(advicelist);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
            } //run

        }).start(); //Thread
        return data;
    }

    public int getNumberTotalForCounter() {
        Integer integerValue = 0;
        try {
            integerValue = new getNumberTotalForCounterAsyncTask(pickorderLineDao).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return integerValue;
    }
    private static class getNumberTotalForCounterAsyncTask extends AsyncTask<Void, Void, Integer> {
        private iPickorderLineDao mAsyncTaskDao;
        getNumberTotalForCounterAsyncTask(iPickorderLineDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected Integer doInBackground(Void... params) {
            return mAsyncTaskDao.getNumberTotalForCounter();
        }
    }
    public int getNumberTotalTotalForCounter() {
        Integer integerValue = 0;
        try {
            integerValue = new getNumberTotalTotalForCounterAsyncTask(pickorderLineDao).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return integerValue;
    }
    private static class getNumberTotalTotalForCounterAsyncTask extends AsyncTask<Void, Void, Integer> {
        private iPickorderLineDao mAsyncTaskDao;
        getNumberTotalTotalForCounterAsyncTask(iPickorderLineDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected Integer doInBackground(Void... params) {
            return mAsyncTaskDao.getNumberTotalTotalForCounter();
        }
    }


    public int getNumberNotHandledForCounter() {
        Integer integerValue = 0;
        try {
            integerValue = new getNumberNotHandledForCounterAsyncTask(pickorderLineDao).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return integerValue;
    }
    private static class getNumberNotHandledForCounterAsyncTask extends AsyncTask<Void, Void, Integer> {
        private iPickorderLineDao mAsyncTaskDao;
        getNumberNotHandledForCounterAsyncTask(iPickorderLineDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected Integer doInBackground(Void... params) {
            return mAsyncTaskDao.getNumberNotHandledForCounter();
        }
    }
    public int getNumberTotalNotHandledForCounter() {
        Integer integerValue = 0;
        try {
            integerValue = new getNumberTotalNotHandledForCounterAsyncTask(pickorderLineDao).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return integerValue;
    }
    private static class getNumberTotalNotHandledForCounterAsyncTask extends AsyncTask<Void, Void, Integer> {
        private iPickorderLineDao mAsyncTaskDao;
        getNumberTotalNotHandledForCounterAsyncTask(iPickorderLineDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected Integer doInBackground(Void... params) {
            return mAsyncTaskDao.getNumberTotalNotHandledForCounter();
        }
    }

    public int getNumberHandledForCounter() {
        Integer integerValue = 0;
        try {
            integerValue = new getNumberHandledForCounterAsyncTask(pickorderLineDao).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return integerValue;
    }
    private static class getNumberHandledForCounterAsyncTask extends AsyncTask<Void, Void, Integer> {
        private iPickorderLineDao mAsyncTaskDao;
        getNumberHandledForCounterAsyncTask(iPickorderLineDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected Integer doInBackground(Void... params) {
            return mAsyncTaskDao.getNumberHandledForCounter();
        }
    }

    public int getNumberTotalHandledForCounter() {
        Integer integerValue = 0;
        try {
            integerValue = new getNumberTotalHandledForCounterAsyncTask(pickorderLineDao).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return integerValue;
    }
    private static class getNumberTotalHandledForCounterAsyncTask extends AsyncTask<Void, Void, Integer> {
        private iPickorderLineDao mAsyncTaskDao;
        getNumberTotalHandledForCounterAsyncTask(iPickorderLineDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected Integer doInBackground(Void... params) {
            return mAsyncTaskDao.getNumberTotalHandledForCounter();
        }
    }
}
