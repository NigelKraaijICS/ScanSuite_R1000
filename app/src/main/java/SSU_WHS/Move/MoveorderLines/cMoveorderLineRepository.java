package SSU_WHS.Move.MoveorderLines;

import android.app.Application;
import android.os.AsyncTask;

import org.json.JSONException;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import ICS.Utils.cDeviceInfo;
import SSU_WHS.Basics.Article.cArticle;
import SSU_WHS.Basics.BranchBin.cBranchBin;
import SSU_WHS.Basics.Users.cUser;
import SSU_WHS.General.acScanSuiteDatabase;
import SSU_WHS.Move.MoveOrders.cMoveorder;
import SSU_WHS.Move.MoveorderLineBarcodes.cMoveorderLineBarcode;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebservice;
import SSU_WHS.Webservice.cWebserviceDefinitions;

public class cMoveorderLineRepository {
    //Region Public Properties
    public iMoveorderLineDao moveorderLineDao;
    //End Region Public Properties

    //Region Private Properties
    private acScanSuiteDatabase db;

    private class GetLinesForBinItemNoVariantCodeFromDatabaseParams {
        String bin;
        String itemno;
        String variantcode;


        GetLinesForBinItemNoVariantCodeFromDatabaseParams(String pvBinStr, String pvItemNoStr, String pvVariantCodeStr) {
            this.bin = pvBinStr;
            this.itemno = pvItemNoStr;
            this.variantcode = pvVariantCodeStr;
        }
    }

    private static class UpdateMovelineQuantityParams {
        Long lineNoLng;
        Double quantityDbl;

        UpdateMovelineQuantityParams(Long pvLineNoLng, Double pvQuantityDbl) {
            this.lineNoLng = pvLineNoLng;
            this.quantityDbl = pvQuantityDbl;
        }
    }

    private static class UpdateOrderlineLocalStatusParams {
        Integer recordIDInt;
        Integer newStatusInt;

        UpdateOrderlineLocalStatusParams(Integer pvRecordIDInt, Integer pvNewsStatusInt) {
            this.recordIDInt = pvRecordIDInt;
            this.newStatusInt = pvNewsStatusInt;
        }

    }
    private static class UpdateOrderlineHandledTimeStampParams {
        Integer recordIdint;
        String handledTimeStampStr;

        UpdateOrderlineHandledTimeStampParams(Integer pvRecordIDInt, String pvHandledTimeStampStr) {
            this.recordIdint = pvRecordIDInt;
            this.handledTimeStampStr = pvHandledTimeStampStr;
        }
    }
    //Region Constructor
    cMoveorderLineRepository(Application pvApplication) {
        this.db = acScanSuiteDatabase.getDatabase(pvApplication);
        this.moveorderLineDao = db.moveorderLineDao();
    }
    //End Region Constructor

    //Region Public Methods

    public void insert(cMoveorderLineEntity moveorderLineEntity) {
        new mInsertAsyncTask(moveorderLineDao).execute(moveorderLineEntity);
    }

    public void delete(cMoveorderLineEntity moveorderLineEntity) {
        new mDeleteAsyncTask(moveorderLineDao).execute(moveorderLineEntity);
    }

    public void deleteAll() {
        new mDeleteAllAsyncTask(moveorderLineDao).execute();
    }

    private static class mInsertAsyncTask extends AsyncTask<cMoveorderLineEntity, Void, Void> {
        private iMoveorderLineDao mAsyncTaskDao;

        mInsertAsyncTask(iMoveorderLineDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final cMoveorderLineEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class mDeleteAsyncTask extends AsyncTask<cMoveorderLineEntity, Void, Void> {
        private iMoveorderLineDao mAsyncTaskDao;

        mDeleteAsyncTask(iMoveorderLineDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final cMoveorderLineEntity... params) {
            mAsyncTaskDao.delete(params[0]);
            return null;
        }
    }

    private static class mDeleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private iMoveorderLineDao mAsyncTaskDao;

        mDeleteAllAsyncTask(iMoveorderLineDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Void... params) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }

    public List<cMoveorderLineEntity> pGetLinesForBinItemNoVariantCodeFromDatabaseObl() {

        //todo: something with the current bin
        List<cMoveorderLineEntity>  resultObl;
        GetLinesForBinItemNoVariantCodeFromDatabaseParams getLinesForBinItemNoVariantCodeFromDatabaseParams = new GetLinesForBinItemNoVariantCodeFromDatabaseParams("", cArticle.currentArticle.getItemNoStr(), cArticle.currentArticle.getVariantCodeStr());

        try {
            resultObl = new mGetLinesForBinItemNoVariantCodeFromDatabaseOblAsyncTask(this.moveorderLineDao).execute(getLinesForBinItemNoVariantCodeFromDatabaseParams).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        } catch (InterruptedException e) {

            return null;
        }
        return resultObl;

    }

    public List<cMoveorderLineEntity> pGetMoveorderLinesForBincodeFromDatabaseObl(String pvBincode) {
        List<cMoveorderLineEntity> ResultObl = null;
        try {
            ResultObl = new pGetMoveorderLinesForBincodeFromDatabaseAsyncTask(moveorderLineDao).execute(pvBincode).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return ResultObl;
    }

    public boolean pUpdateQuantityHandledBln(Double pvQuantityDbl) {

        Integer integerValue;
        UpdateMovelineQuantityParams updateMovelineQuantityParams = new UpdateMovelineQuantityParams((long)cMoveorderLine.currentMoveOrderLine.getLineNoInt(), pvQuantityDbl);

        try {
            integerValue = new updateOrderLineQuantityAsyncTask(moveorderLineDao).execute(updateMovelineQuantityParams).get();

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

    public boolean pUpdateLocalStatusBln(Integer pvNewStatusInt) {

        Integer integerValue;
        UpdateOrderlineLocalStatusParams updateOrderlineLocaStatusParams = new UpdateOrderlineLocalStatusParams(cMoveorderLine.currentMoveOrderLine.getRecordIDInt(), pvNewStatusInt);
        try {
            integerValue = new updateOrderLineLocalStatusAsyncTask(moveorderLineDao).execute(updateOrderlineLocaStatusParams).get();
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
    public boolean pUpdateLocalHandledTimeStampBln(String pvHandledTimeStampStr) {

        Integer integerValue;
        UpdateOrderlineHandledTimeStampParams updateOrderlineHandledTimeStampParams = new UpdateOrderlineHandledTimeStampParams(cMoveorderLine.currentMoveOrderLine.getRecordIDInt(), pvHandledTimeStampStr);
        try {
            integerValue = new updateOrderLineLocalHandledTimeStampAsyncTask(moveorderLineDao).execute(updateOrderlineHandledTimeStampParams).get();
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

    public cWebresult pMoveLineHandledTakeMTViaWebserviceWrs() {
        List<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();

        try {
            webResultWrs = new mMoveorderLineHandledTakeMTViaWebserviceAsyncTask().execute().get();
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

    public cWebresult pMoveItemHandledViaWebserviceWrs() {
        List<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();

        try {
            webResultWrs = new mMoveItemHandledViaWebserviceAsyncTask().execute().get();
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

    public cWebresult pMoveLineHandledPlaceMTViaWebserviceWrs() {
        List<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();

        try {
            webResultWrs = new mMoveorderLineHandledPlaceMTViaWebserviceAsyncTask().execute().get();
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


    public Double pGetTotalCountDbl() {
        Double resultDbl = Double.valueOf(0);
        try {
            resultDbl = new pGetTotalCountFromDatabaseAsyncTask(moveorderLineDao).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return resultDbl;
    }

    public Double pGetCountForBinCodeDbl(String pvBincode) {
        Double resultDbl = Double.valueOf(0);
        try {
            resultDbl = new pGetCountForBincodeFromDatabaseAsyncTask(moveorderLineDao).execute(pvBincode).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return resultDbl;
    }


    public cWebresult pResetLineViaWebserviceWrs() {
        List<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();

        try {
            webResultWrs = new mResetLineViaViaWebserviceAsyncTask().execute().get();
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

    public boolean pUpdateQuantityBln() {

        Integer integerValue;
        UpdateMovelineQuantityParams updateMovelineQuantityParams = new UpdateMovelineQuantityParams((long) cMoveorderLine.currentMoveOrderLine.getLineNoInt(),
                                                                                                                     cMoveorderLine.currentMoveOrderLine.getQuantityHandledDbl());


        try {
            integerValue = new mUpdateQuantityHandledAsyncTask(moveorderLineDao).execute(updateMovelineQuantityParams).get();

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

    private static class pGetMoveorderLinesForBincodeFromDatabaseAsyncTask extends AsyncTask<String, Void, List<cMoveorderLineEntity>> {
        private iMoveorderLineDao mAsyncTaskDao;
        pGetMoveorderLinesForBincodeFromDatabaseAsyncTask(iMoveorderLineDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected List<cMoveorderLineEntity> doInBackground(final String... params) {
            return mAsyncTaskDao.getMoveorderLineForBincode(params[0]);
        }
    }

    private static class pGetCountForBincodeFromDatabaseAsyncTask extends AsyncTask<String, Void, Double> {
        private iMoveorderLineDao mAsyncTaskDao;
        pGetCountForBincodeFromDatabaseAsyncTask(iMoveorderLineDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Double doInBackground(final String... params) {
            return mAsyncTaskDao.getCountForBincodeDbl(params[0]);
        }
    }

    private static class pGetTotalCountFromDatabaseAsyncTask extends AsyncTask<Void, Void, Double> {
        private iMoveorderLineDao mAsyncTaskDao;
        pGetTotalCountFromDatabaseAsyncTask(iMoveorderLineDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Double doInBackground(final Void... params) {
            return mAsyncTaskDao.getTotalCountDbl();
        }
    }

    private static class updateOrderLineQuantityAsyncTask extends AsyncTask<UpdateMovelineQuantityParams, Void, Integer> {
        private iMoveorderLineDao mAsyncTaskDao;
        updateOrderLineQuantityAsyncTask(iMoveorderLineDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected Integer doInBackground(UpdateMovelineQuantityParams... params) {
            return mAsyncTaskDao.updateOrderLineQuantity(params[0].lineNoLng, params[0].quantityDbl);
        }
    }

    private static class updateOrderLineLocalStatusAsyncTask extends AsyncTask<UpdateOrderlineLocalStatusParams, Void, Integer> {
        private iMoveorderLineDao mAsyncTaskDao;
        updateOrderLineLocalStatusAsyncTask(iMoveorderLineDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected Integer doInBackground(UpdateOrderlineLocalStatusParams... params) {
            return mAsyncTaskDao.updateOrderLineLocalStatus(params[0].recordIDInt, params[0].newStatusInt);
        }
    }
    private static class updateOrderLineLocalHandledTimeStampAsyncTask extends AsyncTask<UpdateOrderlineHandledTimeStampParams, Void, Integer> {
        private iMoveorderLineDao mAsyncTaskDao;
        updateOrderLineLocalHandledTimeStampAsyncTask(iMoveorderLineDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected Integer doInBackground(UpdateOrderlineHandledTimeStampParams... params) {
            return mAsyncTaskDao.updateOrderLineLocalHandledTimeStamp(params[0].recordIdint, params[0].handledTimeStampStr);
        }
    }

    private static class mMoveorderLineHandledTakeMTViaWebserviceAsyncTask extends AsyncTask<Void, Void, cWebresult> {
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
                l_PropertyInfo2Pin.name = cWebserviceDefinitions.WEBPROPERTY_SCANNERID;
                l_PropertyInfo2Pin.setValue(cDeviceInfo.getSerialnumberStr());
                l_PropertyInfoObl.add(l_PropertyInfo2Pin);

                PropertyInfo l_PropertyInfo3Pin = new PropertyInfo();
                l_PropertyInfo3Pin.name = cWebserviceDefinitions.WEBPROPERTY_LOCATION_NL;
                l_PropertyInfo3Pin.setValue(cUser.currentUser.currentBranch.getBranchStr());
                l_PropertyInfoObl.add(l_PropertyInfo3Pin);

                PropertyInfo l_PropertyInfo4Pin = new PropertyInfo();
                l_PropertyInfo4Pin.name = cWebserviceDefinitions.WEBPROPERTY_ORDERNUMBER;
                l_PropertyInfo4Pin.setValue(cMoveorder.currentMoveOrder.getOrderNumberStr());
                l_PropertyInfoObl.add(l_PropertyInfo4Pin);

                PropertyInfo l_PropertyInfo5Pin = new PropertyInfo();
                l_PropertyInfo5Pin.name = cWebserviceDefinitions.WEBPROPERTY_LINENOTAKE;
                l_PropertyInfo5Pin.setValue(cMoveorderLine.currentMoveOrderLine.getLineNoInt());
                l_PropertyInfoObl.add(l_PropertyInfo5Pin);

                PropertyInfo l_PropertyInfo6Pin = new PropertyInfo();
                l_PropertyInfo6Pin.name = cWebserviceDefinitions.WEBPROPERTY_HANDLEDTIMESTAMP;
                l_PropertyInfo6Pin.setValue(cMoveorderLine.currentMoveOrderLine.getHandledTimeStampStr());
                l_PropertyInfoObl.add(l_PropertyInfo6Pin);

                PropertyInfo l_PropertyInfo7Pin = new PropertyInfo();
                l_PropertyInfo7Pin.name = cWebserviceDefinitions.WEBPROPERTY_CONTAINER;
                l_PropertyInfo7Pin.setValue(cMoveorderLine.currentMoveOrderLine.getContainerHandledStr());
                l_PropertyInfoObl.add(l_PropertyInfo7Pin);

                SoapObject barcodesHandledList = new SoapObject(cWebservice.WEBSERVICE_NAMESPACE, cWebserviceDefinitions.WEBPROPERTY_BARCODESLIST);

                //Only loop through handled barcodes, of there are any
                if (cMoveorderLine.currentMoveOrderLine.handledBarcodesObl() != null) {
                    for (cMoveorderLineBarcode moveorderLineBarcode: cMoveorderLine.currentMoveOrderLine.handledBarcodesObl()) {
                        SoapObject soapObject = new SoapObject(cWebservice.WEBSERVICE_NAMESPACE, cWebserviceDefinitions.WEBPROPERTY_BARCODEHANDLED_COMPLEX);
                        soapObject.addProperty(cWebserviceDefinitions.WEBPROPERTY_BARCODE_COMPLEX, moveorderLineBarcode.getBarcodeStr());
                        soapObject.addProperty(cWebserviceDefinitions.WEBPROPERTY_QUANTITYHANDLED_COMPLEX, moveorderLineBarcode.getQuantityhandledDbl());
                        barcodesHandledList.addSoapObject(soapObject);
                    }
                }

                PropertyInfo l_PropertyInfo8Pin = new PropertyInfo();
                l_PropertyInfo8Pin.name = cWebserviceDefinitions.WEBPROPERTY_BARCODELIST;
                l_PropertyInfo8Pin.setValue(barcodesHandledList);
                l_PropertyInfoObl.add(l_PropertyInfo8Pin);

                PropertyInfo l_PropertyInfo9Pin = new PropertyInfo();
                l_PropertyInfo9Pin.name = cWebserviceDefinitions.WEBPROPERTY_PROPERTIESHANDLEDLIST;
                l_PropertyInfo9Pin.setValue(null);
                l_PropertyInfoObl.add(l_PropertyInfo9Pin);

                webresult = new cWebresult().pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_MOVEORDERLINE_HANDLEDTAKEMT, l_PropertyInfoObl);

            } catch (JSONException e) {
                webresult.setSuccessBln(false);
                webresult.setResultBln(false);
            }
            return webresult;
        }
    }

    private static class mMoveItemHandledViaWebserviceAsyncTask extends AsyncTask<Void, Void, cWebresult> {
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
                l_PropertyInfo2Pin.name = cWebserviceDefinitions.WEBPROPERTY_SCANNERID;
                l_PropertyInfo2Pin.setValue(cDeviceInfo.getSerialnumberStr());
                l_PropertyInfoObl.add(l_PropertyInfo2Pin);

                PropertyInfo l_PropertyInfo3Pin = new PropertyInfo();
                l_PropertyInfo3Pin.name = cWebserviceDefinitions.WEBPROPERTY_LOCATION_NL;
                l_PropertyInfo3Pin.setValue(cUser.currentUser.currentBranch.getBranchStr());
                l_PropertyInfoObl.add(l_PropertyInfo3Pin);

                PropertyInfo l_PropertyInfo4Pin = new PropertyInfo();
                l_PropertyInfo4Pin.name = cWebserviceDefinitions.WEBPROPERTY_ORDERNUMBER;
                l_PropertyInfo4Pin.setValue(cMoveorder.currentMoveOrder.getOrderNumberStr());
                l_PropertyInfoObl.add(l_PropertyInfo4Pin);

                PropertyInfo l_PropertyInfo5Pin = new PropertyInfo();
                l_PropertyInfo5Pin.name = cWebserviceDefinitions.WEBPROPERTY_ITEMNO;
                l_PropertyInfo5Pin.setValue(cMoveorderLine.currentMoveOrderLine.getItemNoStr());
                l_PropertyInfoObl.add(l_PropertyInfo5Pin);

                PropertyInfo l_PropertyInfo6Pin = new PropertyInfo();
                l_PropertyInfo6Pin.name = cWebserviceDefinitions.WEBPROPERTY_VARIANTCODE;
                l_PropertyInfo6Pin.setValue(cMoveorderLine.currentMoveOrderLine.getVariantCodeStr());
                l_PropertyInfoObl.add(l_PropertyInfo6Pin);

                PropertyInfo l_PropertyInfo7Pin = new PropertyInfo();
                l_PropertyInfo7Pin.name = cWebserviceDefinitions.WEBPROPERTY_BINCODE;
                l_PropertyInfo7Pin.setValue(cMoveorderLine.currentMoveOrderLine.getBinCodeStr());
                l_PropertyInfoObl.add(l_PropertyInfo7Pin);

                PropertyInfo l_PropertyInfo8Pin = new PropertyInfo();
                l_PropertyInfo8Pin.name = cWebserviceDefinitions.WEBPROPERTY_ACTIONTYPECODE_CAMELCASE;
                l_PropertyInfo8Pin.setValue(cMoveorderLine.currentMoveOrderLine.getActionTypeCodeStr());
                l_PropertyInfoObl.add(l_PropertyInfo8Pin);

                PropertyInfo l_PropertyInfo9Pin = new PropertyInfo();
                l_PropertyInfo9Pin.name = cWebserviceDefinitions.WEBPROPERTY_HANDLEDTIMESTAMP;
                l_PropertyInfo9Pin.setValue(cMoveorderLine.currentMoveOrderLine.getHandledTimeStampStr());
                l_PropertyInfoObl.add(l_PropertyInfo9Pin);

                SoapObject barcodesHandledList = new SoapObject(cWebservice.WEBSERVICE_NAMESPACE, cWebserviceDefinitions.WEBPROPERTY_BARCODESLIST);

                //Only loop through handled barcodes, of there are any
                if (cMoveorderLine.currentMoveOrderLine.handledBarcodesObl() != null) {
                    for (cMoveorderLineBarcode moveorderLineBarcode: cMoveorderLine.currentMoveOrderLine.handledBarcodesObl()) {
                        SoapObject soapObject = new SoapObject(cWebservice.WEBSERVICE_NAMESPACE, cWebserviceDefinitions.WEBPROPERTY_BARCODEHANDLED_COMPLEX);
                        soapObject.addProperty(cWebserviceDefinitions.WEBPROPERTY_BARCODE_COMPLEX, moveorderLineBarcode.getBarcodeStr());
                        soapObject.addProperty(cWebserviceDefinitions.WEBPROPERTY_QUANTITYHANDLED_COMPLEX, moveorderLineBarcode.getQuantityhandledDbl());
                        barcodesHandledList.addSoapObject(soapObject);
                    }
                }

                PropertyInfo l_PropertyInfo10Pin = new PropertyInfo();
                l_PropertyInfo10Pin.name = cWebserviceDefinitions.WEBPROPERTY_BARCODELIST;
                l_PropertyInfo10Pin.setValue(barcodesHandledList);
                l_PropertyInfoObl.add(l_PropertyInfo10Pin);

                SoapObject propertiesHandledList = new SoapObject(cWebservice.WEBSERVICE_NAMESPACE, cWebserviceDefinitions.WEBPROPERTY_PROPERTIESHANDLEDLIST);

                PropertyInfo l_PropertyInfo11Pin = new PropertyInfo();
                l_PropertyInfo11Pin.name = cWebserviceDefinitions.WEBPROPERTY_PROPERTIESHANDLEDLIST;
                l_PropertyInfo11Pin.setValue(propertiesHandledList);
                l_PropertyInfoObl.add(l_PropertyInfo11Pin);

                webresult = new cWebresult().pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_MOVEITEM_HANDLED, l_PropertyInfoObl);

            } catch (JSONException e) {
                webresult.setSuccessBln(false);
                webresult.setResultBln(false);
            }
            return webresult;
        }
    }



    private static class mMoveorderLineHandledPlaceMTViaWebserviceAsyncTask extends AsyncTask<Void, Void, cWebresult> {
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
                l_PropertyInfo2Pin.name = cWebserviceDefinitions.WEBPROPERTY_SCANNERID;
                l_PropertyInfo2Pin.setValue(cDeviceInfo.getSerialnumberStr());
                l_PropertyInfoObl.add(l_PropertyInfo2Pin);

                PropertyInfo l_PropertyInfo3Pin = new PropertyInfo();
                l_PropertyInfo3Pin.name = cWebserviceDefinitions.WEBPROPERTY_LOCATION_NL;
                l_PropertyInfo3Pin.setValue(cUser.currentUser.currentBranch.getBranchStr());
                l_PropertyInfoObl.add(l_PropertyInfo3Pin);

                PropertyInfo l_PropertyInfo4Pin = new PropertyInfo();
                l_PropertyInfo4Pin.name = cWebserviceDefinitions.WEBPROPERTY_ORDERNUMBER;
                l_PropertyInfo4Pin.setValue(cMoveorder.currentMoveOrder.getOrderNumberStr());
                l_PropertyInfoObl.add(l_PropertyInfo4Pin);

                PropertyInfo l_PropertyInfo5Pin = new PropertyInfo();
                l_PropertyInfo5Pin.name = cWebserviceDefinitions.WEBPROPERTY_LINENOTAKE;
                l_PropertyInfo5Pin.setValue(cMoveorderLine.currentMoveOrderLine.getLineNoInt());
                l_PropertyInfoObl.add(l_PropertyInfo5Pin);

                PropertyInfo l_PropertyInfo6Pin = new PropertyInfo();
                l_PropertyInfo6Pin.name = cWebserviceDefinitions.WEBPROPERTY_HANDLEDTIMESTAMP;
                l_PropertyInfo6Pin.setValue(cMoveorderLine.currentMoveOrderLine.getHandledTimeStampStr());
                l_PropertyInfoObl.add(l_PropertyInfo6Pin);

                PropertyInfo l_PropertyInfo7Pin = new PropertyInfo();
                l_PropertyInfo7Pin.name = cWebserviceDefinitions.WEBPROPERTY_BINCODEHANDLED;
                l_PropertyInfo7Pin.setValue(cMoveorderLine.currentMoveOrderLine.getBinCodeStr());
                l_PropertyInfoObl.add(l_PropertyInfo7Pin);

                SoapObject barcodesHandledList = new SoapObject(cWebservice.WEBSERVICE_NAMESPACE, cWebserviceDefinitions.WEBPROPERTY_BARCODESLIST);

                //Only loop through handled barcodes, if there are any
                if (cMoveorderLine.currentMoveOrderLine.handledBarcodesObl() != null) {
                    for (cMoveorderLineBarcode moveorderLineBarcode: cMoveorderLine.currentMoveOrderLine.handledBarcodesObl()) {
                        SoapObject soapObject = new SoapObject(cWebservice.WEBSERVICE_NAMESPACE, cWebserviceDefinitions.WEBPROPERTY_BARCODEHANDLED_COMPLEX);
                        soapObject.addProperty(cWebserviceDefinitions.WEBPROPERTY_BARCODE_COMPLEX, moveorderLineBarcode.getBarcodeStr());
                        soapObject.addProperty(cWebserviceDefinitions.WEBPROPERTY_QUANTITYHANDLED_COMPLEX, moveorderLineBarcode.getQuantityhandledDbl());
                        barcodesHandledList.addSoapObject(soapObject);
                    }
                }

                PropertyInfo l_PropertyInfo8Pin = new PropertyInfo();
                l_PropertyInfo8Pin.name = cWebserviceDefinitions.WEBPROPERTY_BARCODELIST;
                l_PropertyInfo8Pin.setValue(barcodesHandledList);
                l_PropertyInfoObl.add(l_PropertyInfo8Pin);

                PropertyInfo l_PropertyInfo9Pin = new PropertyInfo();
                l_PropertyInfo9Pin.name = cWebserviceDefinitions.WEBPROPERTY_PROPERTIESHANDLEDLIST;
                l_PropertyInfo9Pin.setValue(null);
                l_PropertyInfoObl.add(l_PropertyInfo9Pin);

                webresult = new cWebresult().pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_MOVEORDERLINE_HANDLEDPLACEMT, l_PropertyInfoObl);

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
                l_PropertyInfo1Pin.setValue(cUser.currentUser.getNameStr());
                l_PropertyInfoObl.add(l_PropertyInfo1Pin);

                PropertyInfo l_PropertyInfo3Pin = new PropertyInfo();
                l_PropertyInfo3Pin.name = cWebserviceDefinitions.WEBPROPERTY_LOCATION_NL;
                l_PropertyInfo3Pin.setValue(cUser.currentUser.currentBranch.getBranchStr());
                l_PropertyInfoObl.add(l_PropertyInfo3Pin);

                PropertyInfo l_PropertyInfo4Pin = new PropertyInfo();
                l_PropertyInfo4Pin.name = cWebserviceDefinitions.WEBPROPERTY_ORDERNUMBER;
                l_PropertyInfo4Pin.setValue(cMoveorder.currentMoveOrder.getOrderNumberStr());
                l_PropertyInfoObl.add(l_PropertyInfo4Pin);

                PropertyInfo l_PropertyInfo5Pin = new PropertyInfo();
                l_PropertyInfo5Pin.name = cWebserviceDefinitions.WEBPROPERTY_LINENO;
                l_PropertyInfo5Pin.setValue(cMoveorderLine.currentMoveOrderLine.getLineNoInt());
                l_PropertyInfoObl.add(l_PropertyInfo5Pin);


                webresult = new cWebresult().pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_MOVELINERESET, l_PropertyInfoObl);

            } catch (JSONException e) {
                webresult.setSuccessBln(false);
                webresult.setResultBln(false);
            }
            return webresult;
        }
    }

    private static class mUpdateQuantityHandledAsyncTask extends AsyncTask<UpdateMovelineQuantityParams, Void, Integer> {
        private iMoveorderLineDao mAsyncTaskDao;
        mUpdateQuantityHandledAsyncTask(iMoveorderLineDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected Integer doInBackground(UpdateMovelineQuantityParams... params) {
            return mAsyncTaskDao.updateOrderLineQuantity(params[0].lineNoLng, params[0].quantityDbl);
        }
    }

    private static class mGetLinesForBinItemNoVariantCodeFromDatabaseOblAsyncTask extends AsyncTask<GetLinesForBinItemNoVariantCodeFromDatabaseParams, Void, List<cMoveorderLineEntity>> {
        private iMoveorderLineDao mAsyncTaskDao;
        mGetLinesForBinItemNoVariantCodeFromDatabaseOblAsyncTask(iMoveorderLineDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected List<cMoveorderLineEntity> doInBackground(GetLinesForBinItemNoVariantCodeFromDatabaseParams... params) {
            return mAsyncTaskDao.getLinesForBinItemNoVariantCodeFromDatabase(params[0].bin,  params[0].itemno, params[0].variantcode);
        }
    }
}

