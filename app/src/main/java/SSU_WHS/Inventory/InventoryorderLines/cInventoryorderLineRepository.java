package SSU_WHS.Inventory.InventoryorderLines;

import android.app.Application;
import android.os.AsyncTask;

import org.json.JSONException;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import ICS.Utils.cDateAndTime;
import ICS.Utils.cDeviceInfo;
import SSU_WHS.Basics.Users.cUser;
import SSU_WHS.General.acScanSuiteDatabase;
import SSU_WHS.Inventory.InventoryOrders.cInventoryorder;
import SSU_WHS.Inventory.InventoryorderLineBarcodes.cInventoryorderLineBarcode;
import SSU_WHS.LineItemProperty.LinePropertyValue.cLinePropertyValue;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebservice;
import SSU_WHS.Webservice.cWebserviceDefinitions;

public class cInventoryorderLineRepository {
    //Region Public Properties
    private iInventoryorderLineDao inventoryorderLineDao;
    //End Region Public Properties

    //Region Private Properties


    private static class UpdateInventorylineQuantityParams {
        Long lineNoLng;
        Double quantityDbl;

        UpdateInventorylineQuantityParams(Long pvLineNoLng, Double pvQuantityDbl) {
            this.lineNoLng = pvLineNoLng;
            this.quantityDbl = pvQuantityDbl;
        }
    }

    //Region Constructor
    cInventoryorderLineRepository(Application pvApplication) {
        acScanSuiteDatabase db= acScanSuiteDatabase.pGetDatabase(pvApplication);
        this.inventoryorderLineDao = db.inventoryorderLineDao();
    }
    //End Region Constructor

    //Region Public Methods

    public void insert(cInventoryorderLineEntity inventoryorderLineEntity) {
        new mInsertAsyncTask(inventoryorderLineDao).execute(inventoryorderLineEntity);
    }

    public void insertAll(List<cInventoryorderLineEntity>  pvInventoryorderLineEntities) {
        new mInsertAllAsyncTask(inventoryorderLineDao).execute(pvInventoryorderLineEntities);
    }

    public void delete(cInventoryorderLineEntity inventoryorderLineEntity) {
        new mDeleteAsyncTask(inventoryorderLineDao).execute(inventoryorderLineEntity);
    }

    public void deleteAll() {
        new mDeleteAllAsyncTask(inventoryorderLineDao).execute();
    }

    private static class mInsertAsyncTask extends AsyncTask<cInventoryorderLineEntity, Void, Void> {
        private iInventoryorderLineDao mAsyncTaskDao;

        mInsertAsyncTask(iInventoryorderLineDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final cInventoryorderLineEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class mInsertAllAsyncTask extends AsyncTask<List<cInventoryorderLineEntity>, Void, Void> {
        private iInventoryorderLineDao mAsyncTaskDao;

        mInsertAllAsyncTask(iInventoryorderLineDao dao) {
            mAsyncTaskDao = dao;
        }

        @SafeVarargs
        @Override
        protected final Void doInBackground(final List<cInventoryorderLineEntity>... params) {
            mAsyncTaskDao.insertAll(params[0]);
            return null;
        }
    }

    private static class mDeleteAsyncTask extends AsyncTask<cInventoryorderLineEntity, Void, Void> {
        private iInventoryorderLineDao mAsyncTaskDao;

        mDeleteAsyncTask(iInventoryorderLineDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final cInventoryorderLineEntity... params) {
            mAsyncTaskDao.delete(params[0]);
            return null;
        }
    }

    private static class mDeleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private iInventoryorderLineDao mAsyncTaskDao;

        mDeleteAllAsyncTask(iInventoryorderLineDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Void... params) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }

    public Double pGetTotalCountDbl() {
        Double resultDbl = (double) 0;
        try {
            resultDbl = new pGetTotalCountFromDatabaseAsyncTask(inventoryorderLineDao).execute().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return resultDbl;
    }

    public Double pGetCountForBinCodeDbl(String pvBincode) {
        Double resultDbl = (double) 0;
        try {
            resultDbl = new pGetCountForBincodeFromDatabaseAsyncTask(inventoryorderLineDao).execute(pvBincode).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return resultDbl;
    }

    public cWebresult pSaveLineViaWebserviceWrs() {
        List<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();

        try {
            webResultWrs = new mSaveLineViaViaWebserviceAsyncTask().execute().get();
        } catch (ExecutionException | InterruptedException e) {
            webResultWrs.setResultBln(false);
            webResultWrs.setSuccessBln(false);
            resultObl.add(e.getLocalizedMessage());
            webResultWrs.setResultObl(resultObl);
            e.printStackTrace();
        }
        return webResultWrs;
    }

    public cWebresult pResetLineViaWebserviceWrs() {
        List<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();

        try {
            webResultWrs = new mResetLineViaViaWebserviceAsyncTask().execute().get();
        } catch (ExecutionException | InterruptedException e) {
            webResultWrs.setResultBln(false);
            webResultWrs.setSuccessBln(false);
            resultObl.add(e.getLocalizedMessage());
            webResultWrs.setResultObl(resultObl);
            e.printStackTrace();
        }
        return webResultWrs;
    }

    public boolean pUpdateQuantity() {

        Integer integerValue;
        UpdateInventorylineQuantityParams updateInventorylineQuantityParams = new UpdateInventorylineQuantityParams((long) cInventoryorderLine.currentInventoryOrderLine.getLineNoInt(),
                                                                                                                     cInventoryorderLine.currentInventoryOrderLine.getQuantityHandledDbl());


        try {
            integerValue = new mUpdateQuantityHandledAsyncTask(inventoryorderLineDao).execute(updateInventorylineQuantityParams).get();

            return integerValue != 0;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return  false;
        }
    }



    private static class pGetCountForBincodeFromDatabaseAsyncTask extends AsyncTask<String, Void, Double> {
        private iInventoryorderLineDao mAsyncTaskDao;
        pGetCountForBincodeFromDatabaseAsyncTask(iInventoryorderLineDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Double doInBackground(final String... params) {
            return mAsyncTaskDao.getCountForBincodeDbl(params[0]);
        }
    }

    private static class pGetTotalCountFromDatabaseAsyncTask extends AsyncTask<Void, Void, Double> {
        private iInventoryorderLineDao mAsyncTaskDao;
        pGetTotalCountFromDatabaseAsyncTask(iInventoryorderLineDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Double doInBackground(final Void... params) {
            return mAsyncTaskDao.getTotalCountDbl();
        }
    }

    private static class mSaveLineViaViaWebserviceAsyncTask extends AsyncTask<Void, Void, cWebresult> {
        @Override
        protected cWebresult doInBackground(Void... params) {
            cWebresult webresult = new cWebresult();
            try {

                List<PropertyInfo> l_PropertyInfoObl = new ArrayList<>();

                PropertyInfo l_PropertyInfo1Pin = new PropertyInfo();
                l_PropertyInfo1Pin.name = cWebserviceDefinitions.WEBPROPERTY_USERNAMEDUTCH;
                l_PropertyInfo1Pin.setValue(cUser.currentUser.getUsernameStr());
                l_PropertyInfoObl.add(l_PropertyInfo1Pin);

                PropertyInfo l_PropertyInfo2Pin = new PropertyInfo();
                l_PropertyInfo2Pin.name = cWebserviceDefinitions.WEBPROPERTY_SCANNERID;
                l_PropertyInfo2Pin.setValue(cDeviceInfo.getSerialnumberStr());
                l_PropertyInfoObl.add(l_PropertyInfo2Pin);

                PropertyInfo l_PropertyInfo3Pin = new PropertyInfo();
                l_PropertyInfo3Pin.name = cWebserviceDefinitions.WEBPROPERTY_LOCATION_NL;
                l_PropertyInfo3Pin.setValue(cUser.currentUser.currentBranch.getBranchStr());
                l_PropertyInfoObl.add(l_PropertyInfo3Pin);

                PropertyInfo l_PropertyInfo4Pin = new PropertyInfo();
                l_PropertyInfo4Pin.name = cWebserviceDefinitions.WEBPROPERTY_ORDERNUMBER;
                l_PropertyInfo4Pin.setValue(cInventoryorder.currentInventoryOrder.getOrderNumberStr());
                l_PropertyInfoObl.add(l_PropertyInfo4Pin);

                PropertyInfo l_PropertyInfo5Pin = new PropertyInfo();
                l_PropertyInfo5Pin.name = cWebserviceDefinitions.WEBPROPERTY_LINENO;
                l_PropertyInfo5Pin.setValue(cInventoryorderLine.currentInventoryOrderLine.getLineNoInt());
                l_PropertyInfoObl.add(l_PropertyInfo5Pin);

                PropertyInfo l_PropertyInfo6Pin = new PropertyInfo();
                l_PropertyInfo6Pin.name = cWebserviceDefinitions.WEBPROPERTY_HANDLEDTIMESTAMP;
                l_PropertyInfo6Pin.setValue(cDateAndTime.pGetCurrentDateTimeForWebserviceStr());
                l_PropertyInfoObl.add(l_PropertyInfo6Pin);

                SoapObject barcodesHandledList = new SoapObject(cWebservice.WEBSERVICE_NAMESPACE, cWebserviceDefinitions.WEBPROPERTY_BARCODESLIST);

                //Only loop through handled barcodes, of there are any
                if (cInventoryorderLine.currentInventoryOrderLine.lineBarcodesObl() != null) {
                    for (cInventoryorderLineBarcode inventoryorderLineBarcode : cInventoryorderLine.currentInventoryOrderLine.lineBarcodesObl()) {
                        SoapObject soapObject = new SoapObject(cWebservice.WEBSERVICE_NAMESPACE, cWebserviceDefinitions.WEBPROPERTY_BARCODEHANDLED_COMPLEX);
                        soapObject.addProperty(cWebserviceDefinitions.WEBPROPERTY_BARCODE_COMPLEX, inventoryorderLineBarcode.getBarcodeStr());
                        soapObject.addProperty(cWebserviceDefinitions.WEBPROPERTY_QUANTITYHANDLED_COMPLEX, inventoryorderLineBarcode.getQuantityhandledDbl());
                        barcodesHandledList.addSoapObject(soapObject);
                    }
                }

                PropertyInfo l_PropertyInfo7Pin = new PropertyInfo();
                l_PropertyInfo7Pin.name = cWebserviceDefinitions.WEBPROPERTY_BARCODELIST;
                l_PropertyInfo7Pin.setValue(barcodesHandledList);
                l_PropertyInfoObl.add(l_PropertyInfo7Pin);

                SoapObject propertiesHandledObl = new SoapObject(cWebservice.WEBSERVICE_NAMESPACE, cWebserviceDefinitions.WEBPROPERTY_PROPERTIESHANDLED);

                //Only loop through handled properties, of there are any
                if (cInventoryorderLine.currentInventoryOrderLine.linePropertyValuesObl() != null) {

                    int countForPropertyInt = 0;
                    int countForValueInt;
                    String propertyCodeStr = "";

                    for (cLinePropertyValue linePropertyValue: cInventoryorderLine.currentInventoryOrderLine.linePropertyValuesObl()) {

                        countForValueInt = 1;

                        if (!propertyCodeStr.equalsIgnoreCase(linePropertyValue.getPropertyCodeStr())) {
                            propertyCodeStr = linePropertyValue.getPropertyCodeStr();
                            countForPropertyInt = 1;
                        }

                        while (countForValueInt <= linePropertyValue.getQuantityDbl()) {
                            SoapObject soapObject = new SoapObject(cWebservice.WEBSERVICE_NAMESPACE, cWebserviceDefinitions.WEBPROPERTY_PROPERTYHANDLED_COMPLEX);
                            soapObject.addProperty(cWebserviceDefinitions.WEBPROPERTY_INTERFACESPROPERTY_PROPERTYCODE, linePropertyValue.getPropertyCodeStr());
                            soapObject.addProperty(cWebserviceDefinitions.WEBPROPERTY_INTERFACESPROPERTY_SEQUENCENOHANDLED, countForPropertyInt);
                            soapObject.addProperty(cWebserviceDefinitions.WEBPROPERTY_INTERFACESPROPERTY_VALUEHANDLED, linePropertyValue.getValueStr());
                            propertiesHandledObl.addSoapObject(soapObject);
                            countForValueInt++;
                            countForPropertyInt++;
                        }
                    }
                }
                PropertyInfo l_PropertyInfo8Pin = new PropertyInfo();
                l_PropertyInfo8Pin.name = cWebserviceDefinitions.WEBPROPERTY_PROPERTIESHANDLED;
                l_PropertyInfo8Pin.setValue(propertiesHandledObl);
                l_PropertyInfoObl.add(l_PropertyInfo8Pin);

                webresult = cWebresult.pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_INVENTORYLINESAVE, l_PropertyInfoObl);

            } catch (JSONException e) {
                webresult.setSuccessBln(false);
                webresult.setResultBln(false);
            }
            return webresult;
        }
    }

    private static class mResetLineViaViaWebserviceAsyncTask extends AsyncTask<Void, Void, cWebresult> {
        @Override
        protected cWebresult doInBackground(Void... params) {
            cWebresult webresult = new cWebresult();
            try {

                List<PropertyInfo> l_PropertyInfoObl = new ArrayList<>();

                PropertyInfo l_PropertyInfo1Pin = new PropertyInfo();
                l_PropertyInfo1Pin.name = cWebserviceDefinitions.WEBPROPERTY_USERNAMEDUTCH;
                l_PropertyInfo1Pin.setValue(cUser.currentUser.getUsernameStr());
                l_PropertyInfoObl.add(l_PropertyInfo1Pin);

                PropertyInfo l_PropertyInfo2Pin = new PropertyInfo();
                l_PropertyInfo2Pin.name = cWebserviceDefinitions.WEBPROPERTY_SCANNERID;
                l_PropertyInfo2Pin.setValue(cDeviceInfo.getSerialnumberStr());
                l_PropertyInfoObl.add(l_PropertyInfo2Pin);

                PropertyInfo l_PropertyInfo3Pin = new PropertyInfo();
                l_PropertyInfo3Pin.name = cWebserviceDefinitions.WEBPROPERTY_LOCATION_NL;
                l_PropertyInfo3Pin.setValue(cUser.currentUser.currentBranch.getBranchStr());
                l_PropertyInfoObl.add(l_PropertyInfo3Pin);

                PropertyInfo l_PropertyInfo4Pin = new PropertyInfo();
                l_PropertyInfo4Pin.name = cWebserviceDefinitions.WEBPROPERTY_ORDERNUMBER;
                l_PropertyInfo4Pin.setValue(cInventoryorder.currentInventoryOrder.getOrderNumberStr());
                l_PropertyInfoObl.add(l_PropertyInfo4Pin);

                PropertyInfo l_PropertyInfo5Pin = new PropertyInfo();
                l_PropertyInfo5Pin.name = cWebserviceDefinitions.WEBPROPERTY_LINENO;
                l_PropertyInfo5Pin.setValue(cInventoryorderLine.currentInventoryOrderLine.getLineNoInt());
                l_PropertyInfoObl.add(l_PropertyInfo5Pin);

                webresult = cWebresult.pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_INVENTORYLINERESET, l_PropertyInfoObl);

            } catch (JSONException e) {
                webresult.setSuccessBln(false);
                webresult.setResultBln(false);
            }
            return webresult;
        }
    }

    private static class mUpdateQuantityHandledAsyncTask extends AsyncTask<UpdateInventorylineQuantityParams, Void, Integer> {
        private iInventoryorderLineDao mAsyncTaskDao;
        mUpdateQuantityHandledAsyncTask(iInventoryorderLineDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected Integer doInBackground(UpdateInventorylineQuantityParams... params) {
            return mAsyncTaskDao.updateOrderLineQuantity(params[0].lineNoLng, params[0].quantityDbl);
        }
    }
}

