package SSU_WHS.Inventory.InventoryorderBarcodes;

import android.app.Application;
import android.os.AsyncTask;

import org.json.JSONException;
import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import SSU_WHS.Basics.Users.cUser;
import SSU_WHS.General.acScanSuiteDatabase;
import SSU_WHS.Inventory.InventoryOrders.cInventoryorder;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebserviceDefinitions;

public class cInventoryorderBarcodeRepository {

    //Region Public Properties

    //End Region Public Properties

    //Region Private Properties
    private iInventoryorderBarcodeDao inventoryorderBarcodeDao;

    //Region Constructor
    cInventoryorderBarcodeRepository(Application pvApplication) {
        acScanSuiteDatabase db=  acScanSuiteDatabase.pGetDatabase(pvApplication);
        this.inventoryorderBarcodeDao = db.inventoryorderBarcodeDao();
    }
    //End Region Constructor

    //Region Public Methods

    public void insert(cInventoryorderBarcodeEntity inventoryorderBarcodeEntity) {
        new mInsertAsyncTask(inventoryorderBarcodeDao).execute(inventoryorderBarcodeEntity);
    }

    public void insertAll(List<cInventoryorderBarcodeEntity>  inventoryorderBarcodeEntities) {
        new mInsertAllAsyncTask(inventoryorderBarcodeDao).execute(inventoryorderBarcodeEntities);
    }

    public void deleteAll() {
        new mDeleteAllAsyncTask(inventoryorderBarcodeDao).execute();
    }

    private static class mInsertAsyncTask extends AsyncTask<cInventoryorderBarcodeEntity, Void, Void> {
        private iInventoryorderBarcodeDao mAsyncTaskDao;

        mInsertAsyncTask(iInventoryorderBarcodeDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final cInventoryorderBarcodeEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class mInsertAllAsyncTask extends AsyncTask<List<cInventoryorderBarcodeEntity>, Void, Void> {
        private iInventoryorderBarcodeDao mAsyncTaskDao;

        mInsertAllAsyncTask(iInventoryorderBarcodeDao dao) {
            mAsyncTaskDao = dao;
        }

        @SafeVarargs
        @Override
        protected final Void doInBackground(final List<cInventoryorderBarcodeEntity>... params) {
            mAsyncTaskDao.insertAll(params[0]);
            return null;
        }
    }

    private static class mDeleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private iInventoryorderBarcodeDao mAsyncTaskDao;

        mDeleteAllAsyncTask(iInventoryorderBarcodeDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Void... params) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }

    public cWebresult pCreateBarcodeViaWebserviceWrs() {

        List<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();

        try {
            webResultWrs = new mCreateBarcodeViaWebserviceAsyncTask().execute().get();
        } catch (ExecutionException | InterruptedException e) {
            webResultWrs.setResultBln(false);
            webResultWrs.setSuccessBln(false);
            resultObl.add(e.getLocalizedMessage());
            webResultWrs.setResultObl(resultObl);
            e.printStackTrace();
        }
        return webResultWrs;
    }

    private static class mCreateBarcodeViaWebserviceAsyncTask extends AsyncTask<Void, Void, cWebresult> {
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
                l_PropertyInfo2Pin.name = cWebserviceDefinitions.WEBPROPERTY_LOCATION_NL;
                l_PropertyInfo2Pin.setValue(cUser.currentUser.currentBranch.getBranchStr());
                l_PropertyInfoObl.add(l_PropertyInfo2Pin);

                PropertyInfo l_PropertyInfo3Pin = new PropertyInfo();
                l_PropertyInfo3Pin.name = cWebserviceDefinitions.WEBPROPERTY_ORDERNUMBER;
                l_PropertyInfo3Pin.setValue(cInventoryorder.currentInventoryOrder.getOrderNumberStr());
                l_PropertyInfoObl.add(l_PropertyInfo3Pin);

                PropertyInfo l_PropertyInfo4Pin = new PropertyInfo();
                l_PropertyInfo4Pin.name = cWebserviceDefinitions.WEBPROPERTY_ITEMNO;
                l_PropertyInfo4Pin.setValue(cInventoryorderBarcode.currentInventoryOrderBarcode.getItemNoStr());
                l_PropertyInfoObl.add(l_PropertyInfo4Pin);

                PropertyInfo l_PropertyInfo5Pin = new PropertyInfo();
                l_PropertyInfo5Pin.name = cWebserviceDefinitions.WEBPROPERTY_VARIANTCODE;
                l_PropertyInfo5Pin.setValue(cInventoryorderBarcode.currentInventoryOrderBarcode.getVariantCodeStr());
                l_PropertyInfoObl.add(l_PropertyInfo5Pin);

                PropertyInfo l_PropertyInfo6Pin = new PropertyInfo();
                l_PropertyInfo6Pin.name = cWebserviceDefinitions.WEBPROPERTY_BARCODE;
                l_PropertyInfo6Pin.setValue(cInventoryorderBarcode.currentInventoryOrderBarcode.getBarcodeStr());
                l_PropertyInfoObl.add(l_PropertyInfo6Pin);

                PropertyInfo l_PropertyInfo7Pin = new PropertyInfo();
                l_PropertyInfo7Pin.name = cWebserviceDefinitions.WEBPROPERTY_BARCODETYPE;
                l_PropertyInfo7Pin.setValue(cInventoryorderBarcode.currentInventoryOrderBarcode.getBarcodeTypeStr());
                l_PropertyInfoObl.add(l_PropertyInfo7Pin);

                PropertyInfo l_PropertyInfo8Pin = new PropertyInfo();
                l_PropertyInfo8Pin.name = cWebserviceDefinitions.WEBPROPERTY_ISUNIQUEBARCODE;
                l_PropertyInfo8Pin.setValue(cInventoryorderBarcode.currentInventoryOrderBarcode.getIsUniqueBarcodeBln());
                l_PropertyInfoObl.add(l_PropertyInfo8Pin);

                PropertyInfo l_PropertyInfo9Pin = new PropertyInfo();
                l_PropertyInfo9Pin.name = cWebserviceDefinitions.WEBPROPERTY_QUANTITYPERUNITOFMEASURE;
                l_PropertyInfo9Pin.setValue(cInventoryorderBarcode.currentInventoryOrderBarcode.getQuantityPerUnitOfMeasureDbl());
                l_PropertyInfoObl.add(l_PropertyInfo9Pin);

                PropertyInfo l_PropertyInfo10Pin = new PropertyInfo();
                l_PropertyInfo10Pin.name = cWebserviceDefinitions.WEBPROPERTY_UNITOFMEASURE;
                l_PropertyInfo10Pin.setValue(cInventoryorderBarcode.currentInventoryOrderBarcode.getUnitOfMeasureStr());
                l_PropertyInfoObl.add(l_PropertyInfo10Pin);

                PropertyInfo l_PropertyInfo11Pin = new PropertyInfo();
                l_PropertyInfo11Pin.name = cWebserviceDefinitions.WEBPROPERTY_ITEMTYPE;
                l_PropertyInfo11Pin.setValue(cInventoryorderBarcode.currentInventoryOrderBarcode.getItemTypeStr());
                l_PropertyInfoObl.add(l_PropertyInfo11Pin);

                webresult =  cWebresult.pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_GETINVENTORYORDERBARCODES, l_PropertyInfoObl);

            } catch (JSONException e) {
                webresult.setSuccessBln(false);
                webresult.setResultBln(false);
            }
            return webresult;
        }
    }

}
