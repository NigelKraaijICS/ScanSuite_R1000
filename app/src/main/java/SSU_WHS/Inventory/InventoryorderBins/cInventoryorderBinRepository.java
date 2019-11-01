package SSU_WHS.Inventory.InventoryorderBins;

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

public class cInventoryorderBinRepository {
    //Region Public Properties
    public iInventoryorderBinDao inventoryorderBinDao;
    //End Region Public Properties

    //Region Private Properties
    private acScanSuiteDatabase db;

    private static class UpdateInventoryBinStatusParams {
        String binCodeStr;
        int statusInt;

        UpdateInventoryBinStatusParams(String pvBinCodeStr, int statusInt) {
            this.binCodeStr = pvBinCodeStr;
            this.statusInt = statusInt;
        }
    }

    //Region Constructor
    cInventoryorderBinRepository(Application pvApplication) {
        this.db = acScanSuiteDatabase.getDatabase(pvApplication);
        this.inventoryorderBinDao = db.inventoryorderBinDao();
    }
    //End Region Constructor

    //Region Public Methods


    public void insert(cInventoryorderBinEntity inventoryorderBinEntity) {
        new mInsertAsyncTask(inventoryorderBinDao).execute(inventoryorderBinEntity);
    }

    public boolean pUpdateStatusBln() {

        Integer integerValue;
        UpdateInventoryBinStatusParams updateInventorylineQuantityParams = new UpdateInventoryBinStatusParams(cInventoryorderBin.currentInventoryOrderBin.getBinCodeStr(),
                                                                                                              cInventoryorderBin.currentInventoryOrderBin.getStatusInt());


        try {
            integerValue = new updateStatusAsyncTask(inventoryorderBinDao).execute(updateInventorylineQuantityParams).get();

            if (integerValue != 0) {
                return  true;}
            else{
                return false;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            return  false;
        } catch (ExecutionException e) {
            e.printStackTrace();
            return  false;
        }
    }

    public void deleteAll() {
        new mDeleteAllAsyncTask(inventoryorderBinDao).execute();
    }

    public cWebresult pResetBinViaWebserviceWrs() {
        List<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();

        try {
            webResultWrs = new mResetBinViaViaWebserviceAsyncTask().execute().get();
        } catch (ExecutionException e) {
            webResultWrs.setResultBln(false);
            webResultWrs.setSuccessBln(false);
            resultObl.add(e.getLocalizedMessage());
            webResultWrs.setResultObl(resultObl);
            e.printStackTrace();
        } catch (InterruptedException e) {
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

    private static class updateStatusAsyncTask extends AsyncTask<UpdateInventoryBinStatusParams, Void, Integer> {
        private iInventoryorderBinDao mAsyncTaskDao;
        updateStatusAsyncTask(iInventoryorderBinDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected Integer doInBackground(UpdateInventoryBinStatusParams... params) {
            return mAsyncTaskDao.updateBinStatus(params[0].binCodeStr, params[0].statusInt);
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
                l_PropertyInfo1Pin.setValue(cUser.currentUser.getNameStr());
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


                webresult = new cWebresult().pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_INVENTORYBINRESET, l_PropertyInfoObl);

            } catch (JSONException e) {
                webresult.setSuccessBln(false);
                webresult.setResultBln(false);
            }
            return webresult;
        }
    }


}
