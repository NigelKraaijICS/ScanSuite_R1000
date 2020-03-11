package SSU_WHS.Picken.PickorderLines;

import android.app.Application;
import android.os.AsyncTask;

import org.json.JSONException;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import SSU_WHS.Basics.Users.cUser;
import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.General.acScanSuiteDatabase;
import SSU_WHS.Picken.PickorderLineBarcodes.cPickorderLineBarcode;
import SSU_WHS.Picken.Pickorders.cPickorder;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebservice;
import SSU_WHS.Webservice.cWebserviceDefinitions;

public class cPickorderLineRepository {

    //Region Public Properties
    public iPickorderLineDao pickorderLineDao;
    //End Region Public Properties

    //Region Private Properties
    private acScanSuiteDatabase db;

    private static class UpdateOrderlineQuantityParams {
        Integer recordIdint;
        Double quantityDbl;

        UpdateOrderlineQuantityParams(Integer pvRecordIDInt, Double pvQuantityDbl) {
            this.recordIdint = pvRecordIDInt;
            this.quantityDbl = pvQuantityDbl;
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

    private static class UpdateOrderlineProcessingSequenceParams {
        Integer recordIDInt;
        String processingSequenceStr;

        UpdateOrderlineProcessingSequenceParams(Integer pvRecordIdInt, String pvProcessingSequenceStr) {
            this.recordIDInt = pvRecordIdInt;
            this.processingSequenceStr = pvProcessingSequenceStr;
        }
    }

    private static class UpdateOrderlineLocaStatusParams {
        Integer recordIDInt;
        Integer newStatusInt;

        UpdateOrderlineLocaStatusParams(Integer pvRecordIDInt, Integer pvNewsStatusInt) {
            this.recordIDInt = pvRecordIDInt;
            this.newStatusInt = pvNewsStatusInt;
        }

    }

    private static class UpdateSortOrderLineParams{
        Integer recordIdInt;
        Integer quantityHandledInt;
        String binStr;

        UpdateSortOrderLineParams(Integer pvRecordIdInt, Integer pvNumberInt, String pvLocationStr) {
            this.recordIdInt = pvRecordIdInt;
            this.quantityHandledInt = pvNumberInt;
            this.binStr = pvLocationStr;
        }
    }

    private static class GetSortorderLineNotHandledByItemNoAndVariantParams {
        String itemNoStr;
        String variantCodeStr;

        GetSortorderLineNotHandledByItemNoAndVariantParams(String pvItemNoStr, String pvVariantCodeStr) {
            this.itemNoStr = pvItemNoStr;
            this.variantCodeStr = pvVariantCodeStr;
        }
    }

    private static class SortLocationAdviceParams {
        String orderTypeStr;
        String sourceNoStr;

        SortLocationAdviceParams(String orderTypeStr, String sourceNoStr) {
            this.orderTypeStr = orderTypeStr;
            this.sourceNoStr = sourceNoStr;
        }
    }



    //End Region Private Properties

    //Region Constructor
    public cPickorderLineRepository(Application pvApplication) {
        this.db = acScanSuiteDatabase.pGetDatabase(pvApplication);
        this.pickorderLineDao = this.db.pickorderLineDao();
    }
    //End Region Constructor

    //Region Public Methods

    public void pInsert(cPickorderLineEntity pvPickorderLineEntity) {
        new mInsertInDatabaseAsyncTask(this.pickorderLineDao).execute(pvPickorderLineEntity);
    }

    public void pDelete(cPickorderLineEntity pickorderLineEntity) {
        new mDeleteAsyncTask(pickorderLineDao).execute(pickorderLineEntity);
    }

    public void pTruncate() {
        new cPickorderLineRepository.deleteAllAsyncTask(pickorderLineDao).execute();
    }

    public cWebresult pPickLineHandledViaWebserviceWrs() {
        List<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();

        try {
            webResultWrs = new mPickorderLineHandledViaWebserviceAsyncTask().execute().get();
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

    public cWebresult pResetViaWebserviceWrs() {

        List<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();

        try {
            webResultWrs = new mResetViaWebserviceAsyncTask().execute().get();
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

    public cWebresult pGetSortLocationAdviceViaWebserviceWrs(String pvSourceNoStr) {

        List<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();

        SortLocationAdviceParams sortLocationAdviceParams = new SortLocationAdviceParams(cWarehouseorder.OrderTypeEnu.PICKEN.toString(), pvSourceNoStr);

        try {
            webResultWrs = new mGetSortLocationAdviceViaWebserviceAsyncTask().execute(sortLocationAdviceParams).get();
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

    public boolean pUpdateQuantityHandledBln(Double pvQuantityDbl) {

        Integer integerValue;
        UpdateOrderlineQuantityParams updateOrderlineQuantityParams = new UpdateOrderlineQuantityParams(cPickorderLine.currentPickOrderLine.getRecordIDInt(), pvQuantityDbl);

        try {
            integerValue = new updateOrderLineQuantityAsyncTask(pickorderLineDao).execute(updateOrderlineQuantityParams).get();

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

    public boolean pUpdateProcessingSequenceBln(String pvProcessingSequenceStr) {
        Integer integerValue;
        UpdateOrderlineProcessingSequenceParams updateOrderlineProcessingSequenceParams = new UpdateOrderlineProcessingSequenceParams(cPickorderLine.currentPickOrderLine.getRecordIDInt(),pvProcessingSequenceStr);

        try {
            integerValue = new updateOrderLineProcessingSequenceAsyncTask(pickorderLineDao).execute(updateOrderlineProcessingSequenceParams).get();

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
        UpdateOrderlineLocaStatusParams updateOrderlineLocaStatusParams = new UpdateOrderlineLocaStatusParams(cPickorderLine.currentPickOrderLine.getRecordIDInt(), pvNewStatusInt);
        try {
            integerValue = new updateOrderLineLocalStatusAsyncTask(pickorderLineDao).execute(updateOrderlineLocaStatusParams).get();
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
        UpdateOrderlineHandledTimeStampParams updateOrderlineHandledTimeStampParams = new UpdateOrderlineHandledTimeStampParams(cPickorderLine.currentPickOrderLine.getRecordIDInt(), pvHandledTimeStampStr);
        try {
            integerValue = new updateOrderLineLocalHandledTimeStampAsyncTask(pickorderLineDao).execute(updateOrderlineHandledTimeStampParams).get();
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

    public List<cPickorderLineEntity> pGetSortLineForItemNoAndVariantCodeObl(String pvItemNoStr, String pvVariantCodeStr) {

        List<cPickorderLineEntity>  resultObl;
        GetSortorderLineNotHandledByItemNoAndVariantParams getSortorderLineNotHandledByItemNoAndVariantParams = new GetSortorderLineNotHandledByItemNoAndVariantParams(pvItemNoStr, pvVariantCodeStr);

        try {
            resultObl = new mGetSortorderLineNotHandledByItemNoAndVariantOblAsyncTask(this.pickorderLineDao).execute(getSortorderLineNotHandledByItemNoAndVariantParams).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        } catch (InterruptedException e) {

            return null;
        }
        return resultObl;

    }

    public List<cPickorderLineEntity> pGetLinesForSourceNoObl(String pvSourceNoStr) {

        List<cPickorderLineEntity> resultObl;

        try {
            resultObl = new mGetLinesForSourceNoAsyncTask(this.pickorderLineDao).execute(pvSourceNoStr).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        } catch (InterruptedException e) {

            return null;
        }
        return resultObl;

    }


    //End Region Public Methods

    private static class mInsertInDatabaseAsyncTask extends AsyncTask<cPickorderLineEntity, Void, Void> {
        private iPickorderLineDao mAsyncTaskDao;

        mInsertInDatabaseAsyncTask(iPickorderLineDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final cPickorderLineEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class mDeleteAsyncTask extends AsyncTask<cPickorderLineEntity, Void, Void> {
        private iPickorderLineDao mAsyncTaskDao;

        mDeleteAsyncTask(iPickorderLineDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected Void doInBackground(final cPickorderLineEntity... params) {
            mAsyncTaskDao.deletePickorder(params[0]);
            return null;
        }
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

    private static class mPickorderLineHandledViaWebserviceAsyncTask extends AsyncTask<Void, Void, cWebresult> {
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
                l_PropertyInfo3Pin.setValue(cPickorder.currentPickOrder.getOrderNumberStr());
                l_PropertyInfoObl.add(l_PropertyInfo3Pin);

                PropertyInfo l_PropertyInfo4Pin = new PropertyInfo();
                l_PropertyInfo4Pin.name = cWebserviceDefinitions.WEBPROPERTY_LINENOTAKE;
                l_PropertyInfo4Pin.setValue(cPickorderLine.currentPickOrderLine.getLineNoInt());
                l_PropertyInfoObl.add(l_PropertyInfo4Pin);

                PropertyInfo l_PropertyInfo5Pin = new PropertyInfo();
                l_PropertyInfo5Pin.name = cWebserviceDefinitions.WEBPROPERTY_HANDLEDTIMESTAMP;
                l_PropertyInfo5Pin.setValue(cPickorderLine.currentPickOrderLine.getTakenTimeStampStr());
                l_PropertyInfoObl.add(l_PropertyInfo5Pin);

                PropertyInfo l_PropertyInfo6Pin = new PropertyInfo();
                l_PropertyInfo6Pin.name = cWebserviceDefinitions.WEBPROPERTY_PICKFROMCONTAINER;
                l_PropertyInfo6Pin.setValue(cPickorderLine.currentPickOrderLine.getContainerHandledStr());
                l_PropertyInfoObl.add(l_PropertyInfo6Pin);


                SoapObject barcodesHandledList = new SoapObject(cWebservice.WEBSERVICE_NAMESPACE, cWebserviceDefinitions.WEBPROPERTY_BARCODESLIST);

                //Only loop through handled barcodes, of there are any
                if (cPickorderLine.currentPickOrderLine.handledBarcodesObl() != null) {
                    for (cPickorderLineBarcode pickorderLineBarcode: cPickorderLine.currentPickOrderLine.handledBarcodesObl()) {
                        SoapObject soapObject = new SoapObject(cWebservice.WEBSERVICE_NAMESPACE, cWebserviceDefinitions.WEBPROPERTY_BARCODEHANDLED_COMPLEX);
                        soapObject.addProperty(cWebserviceDefinitions.WEBPROPERTY_BARCODE_COMPLEX, pickorderLineBarcode.getBarcodeStr());
                        soapObject.addProperty(cWebserviceDefinitions.WEBPROPERTY_QUANTITYHANDLED_COMPLEX, pickorderLineBarcode.getQuantityhandledDbl());
                        barcodesHandledList.addSoapObject(soapObject);
                    }
                }

                PropertyInfo l_PropertyInfo7Pin = new PropertyInfo();
                l_PropertyInfo7Pin.name = cWebserviceDefinitions.WEBPROPERTY_BARCODELIST;
                l_PropertyInfo7Pin.setValue(barcodesHandledList);
                l_PropertyInfoObl.add(l_PropertyInfo7Pin);

                PropertyInfo l_PropertyInfo8Pin = new PropertyInfo();
                l_PropertyInfo8Pin.name = cWebserviceDefinitions.WEBPROPERTY_CONTAINERSLIST;
                l_PropertyInfo8Pin.setValue(null);
                l_PropertyInfoObl.add(l_PropertyInfo8Pin);

                PropertyInfo l_PropertyInfo9Pin = new PropertyInfo();
                l_PropertyInfo9Pin.name = cWebserviceDefinitions.WEBPROPERTY_PROCESSINGSEQUENCE;
                l_PropertyInfo9Pin.setValue(cPickorderLine.currentPickOrderLine.getProcessingSequenceStr());
                l_PropertyInfoObl.add(l_PropertyInfo9Pin);

                webresult = new cWebresult().pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_PICKORDERLINE_HANDLED, l_PropertyInfoObl);

            } catch (JSONException e) {
                webresult.setSuccessBln(false);
                webresult.setResultBln(false);
            }
            return webresult;
        }
    }

    private static class mPickorderSortLineHandledViaWebserviceAsyncTask extends AsyncTask<String, Void, cWebresult> {
        @Override
        protected cWebresult doInBackground(String... params) {
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
                l_PropertyInfo3Pin.setValue(cPickorder.currentPickOrder.getOrderNumberStr());
                l_PropertyInfoObl.add(l_PropertyInfo3Pin);

                PropertyInfo l_PropertyInfo4Pin = new PropertyInfo();
                l_PropertyInfo4Pin.name = cWebserviceDefinitions.WEBPROPERTY_LINENOPLACE;
                l_PropertyInfo4Pin.setValue(cPickorderLine.currentPickOrderLine.getLineNoInt());
                l_PropertyInfoObl.add(l_PropertyInfo4Pin);

                PropertyInfo l_PropertyInfo5Pin = new PropertyInfo();
                l_PropertyInfo5Pin.name = cWebserviceDefinitions.WEBPROPERTY_HANDLEDTIMESTAMP;
                l_PropertyInfo5Pin.setValue(cPickorderLine.currentPickOrderLine.getTakenTimeStampStr());
                l_PropertyInfoObl.add(l_PropertyInfo5Pin);

                PropertyInfo l_PropertyInfo6Pin = new PropertyInfo();
                l_PropertyInfo6Pin.name = cWebserviceDefinitions.WEBPROPERTY_NUMBER;
                l_PropertyInfo6Pin.setValue(cPickorderLine.currentPickOrderLine.getQuantityHandledDbl());
                l_PropertyInfoObl.add(l_PropertyInfo6Pin);

                PropertyInfo l_PropertyInfo7Pin = new PropertyInfo();
                l_PropertyInfo7Pin.name = cWebserviceDefinitions.WEBPROPERTY_BARCODE;
                l_PropertyInfo7Pin.setValue(params[0]);
                l_PropertyInfoObl.add(l_PropertyInfo7Pin);

                PropertyInfo l_PropertyInfo8Pin = new PropertyInfo();
                l_PropertyInfo8Pin.name = cWebserviceDefinitions.WEBPROPERTY_PROPERTIESHANDLED;
                l_PropertyInfo8Pin.setValue(null);
                l_PropertyInfoObl.add(l_PropertyInfo8Pin);

                PropertyInfo l_PropertyInfo9Pin = new PropertyInfo();
                l_PropertyInfo9Pin.name = cWebserviceDefinitions.WEBPROPERTY_CONTAINERSLIST;
                l_PropertyInfo9Pin.setValue(null);
                l_PropertyInfoObl.add(l_PropertyInfo9Pin);

                PropertyInfo l_PropertyInfo10Pin = new PropertyInfo();
                l_PropertyInfo10Pin.name = cWebserviceDefinitions.WEBPROPERTY_LOCATION;
                l_PropertyInfo10Pin.setValue(cPickorderLine.currentPickOrderLine.getLocalSortLocationStr());
                l_PropertyInfoObl.add(l_PropertyInfo10Pin);

                webresult = new cWebresult().pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_SORTORDERLINE_HANDLED, l_PropertyInfoObl);

            } catch (JSONException e) {
                webresult.setSuccessBln(false);
                webresult.setResultBln(false);
            }
            return webresult;
        }
    }

    private static class updateOrderLineQuantityAsyncTask extends AsyncTask<UpdateOrderlineQuantityParams, Void, Integer> {
        private iPickorderLineDao mAsyncTaskDao;
        updateOrderLineQuantityAsyncTask(iPickorderLineDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected Integer doInBackground(UpdateOrderlineQuantityParams... params) {
            return mAsyncTaskDao.updateOrderLineQuantity(params[0].recordIdint, params[0].quantityDbl);
        }
    }

    private static class updateOrderLineProcessingSequenceAsyncTask extends AsyncTask<UpdateOrderlineProcessingSequenceParams, Void, Integer> {
        private iPickorderLineDao mAsyncTaskDao;
        updateOrderLineProcessingSequenceAsyncTask(iPickorderLineDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected Integer doInBackground(UpdateOrderlineProcessingSequenceParams... params) {
            return mAsyncTaskDao.updateOrderLineProcessingSequence(params[0].recordIDInt, params[0].processingSequenceStr);
        }
    }

    private static class updateOrderLineLocalStatusAsyncTask extends AsyncTask<UpdateOrderlineLocaStatusParams, Void, Integer> {
        private iPickorderLineDao mAsyncTaskDao;
        updateOrderLineLocalStatusAsyncTask(iPickorderLineDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected Integer doInBackground(UpdateOrderlineLocaStatusParams... params) {
            return mAsyncTaskDao.updateOrderLineLocalStatus(params[0].recordIDInt, params[0].newStatusInt);
        }
    }

    private static class updateOrderLineLocalHandledTimeStampAsyncTask extends AsyncTask<UpdateOrderlineHandledTimeStampParams, Void, Integer> {
        private iPickorderLineDao mAsyncTaskDao;
        updateOrderLineLocalHandledTimeStampAsyncTask(iPickorderLineDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected Integer doInBackground(UpdateOrderlineHandledTimeStampParams... params) {
            return mAsyncTaskDao.updateOrderLineLocalHandledTimeStamp(params[0].recordIdint, params[0].handledTimeStampStr);
        }
    }

    private static class mResetViaWebserviceAsyncTask extends AsyncTask<Void, Void, cWebresult> {
        @Override
        protected cWebresult doInBackground(Void... params) {
            cWebresult l_WebresultWrs = new cWebresult();
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
                l_PropertyInfo3Pin.setValue(cPickorder.currentPickOrder.getOrderNumberStr());
                l_PropertyInfoObl.add(l_PropertyInfo3Pin);

                PropertyInfo l_PropertyInfo4Pin = new PropertyInfo();
                l_PropertyInfo4Pin.name = cWebserviceDefinitions.WEBPROPERTY_LINENOTAKE;
                l_PropertyInfo4Pin.setValue(cPickorderLine.currentPickOrderLine.getLineNoInt());
                l_PropertyInfoObl.add(l_PropertyInfo4Pin);

                l_WebresultWrs = new cWebresult().pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_PICKORDERLINERESET, l_PropertyInfoObl);
            } catch (JSONException e) {
                l_WebresultWrs.setSuccessBln(false);
                l_WebresultWrs.setResultBln(false);
            }
            return l_WebresultWrs;
        }
    }

    private static class mGetSortLocationAdviceViaWebserviceAsyncTask extends AsyncTask<SortLocationAdviceParams, Void, cWebresult> {
        @Override
        protected cWebresult doInBackground(SortLocationAdviceParams... params) {
            cWebresult l_WebresultWrs = new cWebresult();
            try {
                List<PropertyInfo> l_PropertyInfoObl = new ArrayList<>();

                PropertyInfo l_PropertyInfo1Pin = new PropertyInfo();
                l_PropertyInfo1Pin.name = cWebserviceDefinitions.WEBPROPERTY_ORDERTYPE;
                l_PropertyInfo1Pin.setValue(params[0].orderTypeStr);
                l_PropertyInfoObl.add(l_PropertyInfo1Pin);

                PropertyInfo l_PropertyInfo2Pin = new PropertyInfo();
                l_PropertyInfo2Pin.name = cWebserviceDefinitions.WEBPROPERTY_LOCATION_NL;
                l_PropertyInfo2Pin.setValue(cUser.currentUser.currentBranch.getBranchStr());
                l_PropertyInfoObl.add(l_PropertyInfo2Pin);

                PropertyInfo l_PropertyInfo3Pin = new PropertyInfo();
                l_PropertyInfo3Pin.name = cWebserviceDefinitions.WEBPROPERTY_ORDERNUMBER;
                l_PropertyInfo3Pin.setValue(cPickorder.currentPickOrder.getOrderNumberStr());
                l_PropertyInfoObl.add(l_PropertyInfo3Pin);

                PropertyInfo l_PropertyInfo4Pin = new PropertyInfo();
                l_PropertyInfo4Pin.name = cWebserviceDefinitions.WEBPROPERTY_SOURCENO;
                l_PropertyInfo4Pin.setValue(params[0].sourceNoStr);
                l_PropertyInfoObl.add(l_PropertyInfo4Pin);

                l_WebresultWrs = new cWebresult().pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_GETSORTLOCATIONADVICE, l_PropertyInfoObl);
            } catch (JSONException e) {
                l_WebresultWrs.setSuccessBln(false);
                l_WebresultWrs.setResultBln(false);
            }
            return l_WebresultWrs;
        }
    }


    private static class mGetLinesForSourceNoAsyncTask extends AsyncTask<String, Void,  List<cPickorderLineEntity>> {
        private iPickorderLineDao mAsyncTaskDao;
        mGetLinesForSourceNoAsyncTask(iPickorderLineDao dao) { mAsyncTaskDao = dao;}
        @Override
        protected List<cPickorderLineEntity> doInBackground(String... params) {
            mAsyncTaskDao.getPickLinesNotHandledForSourceNo(params[0]);
            return null;
        }
    }

    private static class mGetSortorderLineNotHandledByItemNoAndVariantOblAsyncTask extends AsyncTask<GetSortorderLineNotHandledByItemNoAndVariantParams, Void, List<cPickorderLineEntity>> {
        private iPickorderLineDao mAsyncTaskDao;
        mGetSortorderLineNotHandledByItemNoAndVariantOblAsyncTask(iPickorderLineDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected List<cPickorderLineEntity> doInBackground(GetSortorderLineNotHandledByItemNoAndVariantParams... params) {
            return mAsyncTaskDao.getSortorderLinesNotHandledByItemNoAndVariant(params[0].itemNoStr, params[0].variantCodeStr);
        }
    }



}
