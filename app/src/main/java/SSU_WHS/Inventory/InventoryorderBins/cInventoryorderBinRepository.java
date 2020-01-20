package SSU_WHS.Inventory.InventoryorderBins;

import android.app.Application;
import android.os.AsyncTask;

import org.json.JSONException;
import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import ICS.Utils.cDateAndTime;
import SSU_WHS.Basics.Users.cUser;
import SSU_WHS.General.acScanSuiteDatabase;
import SSU_WHS.Inventory.InventoryOrders.cInventoryorder;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebserviceDefinitions;

public class cInventoryorderBinRepository {
    //Region Public Properties
    private iInventoryorderBinDao inventoryorderBinDao;
    //End Region Public Properties

    //Region Private Properties
    private acScanSuiteDatabase db;

    private static class UpdateInventoryBinStatusParams {
        String binCodeStr;
        int statusInt;
        String timestampStr;

        UpdateInventoryBinStatusParams(String pvBinCodeStr, int pvStatusInt, String pvTimeStampStr) {
            this.binCodeStr = pvBinCodeStr;
            this.statusInt = pvStatusInt;
            this.timestampStr = pvTimeStampStr;
        }
    }

    //Region Constructor
    cInventoryorderBinRepository(Application pvApplication) {
        this.db = acScanSuiteDatabase.pGetDatabase(pvApplication);
        this.inventoryorderBinDao = db.inventoryorderBinDao();
    }
    //End Region Constructor

    //Region Public Methods


    public void insert(cInventoryorderBinEntity inventoryorderBinEntity) {
        new mInsertAsyncTask(inventoryorderBinDao).execute(inventoryorderBinEntity);
    }

    public void insertAll(List<cInventoryorderBinEntity> pvInventoryorderLineBarcodeEntities) {
        new mInsertAllAsyncTask(inventoryorderBinDao).execute(pvInventoryorderLineBarcodeEntities);
    }

    public boolean pUpdateStatusAndTimestampBln() {

        Integer integerValue;
        UpdateInventoryBinStatusParams updateInventorylineQuantityParams = new UpdateInventoryBinStatusParams(cInventoryorderBin.currentInventoryOrderBin.getBinCodeStr(),
                                                                                                              cInventoryorderBin.currentInventoryOrderBin.getStatusInt(),
                                                                                                              cInventoryorderBin.currentInventoryOrderBin.getHandledTimeStampStr());


        try {
            integerValue = new updateStatusAsyncTask(inventoryorderBinDao).execute(updateInventorylineQuantityParams).get();

            return integerValue != 0;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return  false;
        }
    }

    public void deleteAll() {
        new mDeleteAllAsyncTask(inventoryorderBinDao).execute();
    }

    public void allNew() {
        new mAllNewAsyncTask(inventoryorderBinDao).execute();
    }

    public cWebresult pResetBinViaWebserviceWrs() {
        List<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();

        try {
            webResultWrs = new mResetBinViaViaWebserviceAsyncTask().execute().get();
        } catch (ExecutionException | InterruptedException e) {
            webResultWrs.setResultBln(false);
            webResultWrs.setSuccessBln(false);
            resultObl.add(e.getLocalizedMessage());
            webResultWrs.setResultObl(resultObl);
            e.printStackTrace();
        }
        return webResultWrs;
    }

    public cWebresult pReopenBinViaWebserviceWrs() {
        List<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();

        try {
            webResultWrs = new mReopenBinViaViaWebserviceAsyncTask().execute().get();
        } catch (ExecutionException | InterruptedException e) {
            webResultWrs.setResultBln(false);
            webResultWrs.setSuccessBln(false);
            resultObl.add(e.getLocalizedMessage());
            webResultWrs.setResultObl(resultObl);
            e.printStackTrace();
        }
        return webResultWrs;
    }


    private static class mInsertAsyncTask extends AsyncTask<cInventoryorderBinEntity, Void, Void> {
        private iInventoryorderBinDao mAsyncTaskDao;

        mInsertAsyncTask(iInventoryorderBinDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final cInventoryorderBinEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class mInsertAllAsyncTask extends AsyncTask<List<cInventoryorderBinEntity>, Void, Void> {
        private iInventoryorderBinDao mAsyncTaskDao;

        mInsertAllAsyncTask(iInventoryorderBinDao dao) {
            mAsyncTaskDao = dao;
        }

        @SafeVarargs
        @Override
        protected final Void doInBackground(final List<cInventoryorderBinEntity>... params) {
            mAsyncTaskDao.insertAll(params[0]);
            return null;
        }
    }



    private static class mDeleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private iInventoryorderBinDao mAsyncTaskDao;

        mDeleteAllAsyncTask(iInventoryorderBinDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Void... params) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }

    private static class mAllNewAsyncTask extends AsyncTask<Void, Void, Void> {
        private iInventoryorderBinDao mAsyncTaskDao;

        mAllNewAsyncTask(iInventoryorderBinDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Void... params) {
            mAsyncTaskDao.allNew();
            return null;
        }
    }

    private static class updateStatusAsyncTask extends AsyncTask<UpdateInventoryBinStatusParams, Void, Integer> {
        private iInventoryorderBinDao mAsyncTaskDao;
        updateStatusAsyncTask(iInventoryorderBinDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected Integer doInBackground(UpdateInventoryBinStatusParams... params) {
            return mAsyncTaskDao.updateBinStatus(params[0].binCodeStr, params[0].statusInt, params[0].timestampStr);
        }
    }

    private static class mResetBinViaViaWebserviceAsyncTask extends AsyncTask<Void, Void, cWebresult> {
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
                l_PropertyInfo4Pin.name = cWebserviceDefinitions.WEBPROPERTY_BINCODE;
                l_PropertyInfo4Pin.setValue(cInventoryorderBin.currentInventoryOrderBin.getBinCodeStr());
                l_PropertyInfoObl.add(l_PropertyInfo4Pin);


                new cWebresult();
                webresult = cWebresult.pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_INVENTORYBINRESET, l_PropertyInfoObl);

            } catch (JSONException e) {
                webresult.setSuccessBln(false);
                webresult.setResultBln(false);
            }
            return webresult;
        }
    }

    private static class mReopenBinViaViaWebserviceAsyncTask extends AsyncTask<Void, Void, cWebresult> {
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
                l_PropertyInfo4Pin.name = cWebserviceDefinitions.WEBPROPERTY_HANDLEDTIMESTAMP;
                l_PropertyInfo4Pin.setValue(cDateAndTime.pGetCurrentDateTimeForWebserviceStr());
                l_PropertyInfoObl.add(l_PropertyInfo4Pin);

                PropertyInfo l_PropertyInfo5Pin = new PropertyInfo();
                l_PropertyInfo5Pin.name = cWebserviceDefinitions.WEBPROPERTY_BINCODETINY;
                l_PropertyInfo5Pin.setValue(cInventoryorderBin.currentInventoryOrderBin.getBinCodeStr());
                l_PropertyInfoObl.add(l_PropertyInfo5Pin);


                new cWebresult();
                webresult = cWebresult.pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_INVENTORYBINOPEN, l_PropertyInfoObl);

            } catch (JSONException e) {
                webresult.setSuccessBln(false);
                webresult.setResultBln(false);
            }
            return webresult;
        }
    }


}
