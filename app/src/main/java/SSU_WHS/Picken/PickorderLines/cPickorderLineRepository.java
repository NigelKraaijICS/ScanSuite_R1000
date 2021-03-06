package SSU_WHS.Picken.PickorderLines;

import android.app.Application;
import android.os.AsyncTask;

import org.json.JSONException;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cDeviceInfo;
import SSU_WHS.Basics.ContentlabelContainer.cContentlabelContainer;
import SSU_WHS.Basics.Users.cUser;
import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.General.acScanSuiteDatabase;
import SSU_WHS.LineItemProperty.LinePropertyValue.cLinePropertyValue;
import SSU_WHS.Picken.PickorderBarcodes.cPickorderBarcode;
import SSU_WHS.Picken.PickorderLineBarcodes.cPickorderLineBarcode;
import SSU_WHS.Picken.Pickorders.cPickorder;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebservice;
import SSU_WHS.Webservice.cWebserviceDefinitions;

public class cPickorderLineRepository {

    //Region Public Properties
    private final iPickorderLineDao pickorderLineDao;
    //End Region Public Properties

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

    private static class SortLocationAdviceParams {
        String orderTypeStr;
        String sourceNoStr;

        SortLocationAdviceParams(String orderTypeStr, String sourceNoStr) {
            this.orderTypeStr = orderTypeStr;
            this.sourceNoStr = sourceNoStr;
        }
    }

    private static class AddBarcodeParams {
        cBarcodeScan barcodeScan;
        Boolean isUniqueBarcodeBln;

        AddBarcodeParams(cBarcodeScan pvBarcodeScan, Boolean pvIsUniqueBarcodeBln) {
            this.barcodeScan = pvBarcodeScan;
            this.isUniqueBarcodeBln = pvIsUniqueBarcodeBln;
        }
    }

    //End Region Private Properties

    //Region Constructor
    public cPickorderLineRepository(Application pvApplication) {
        //Region Private Properties
        acScanSuiteDatabase db = acScanSuiteDatabase.pGetDatabase(pvApplication);
        this.pickorderLineDao = db.pickorderLineDao();
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
        } catch (ExecutionException | InterruptedException e) {
            webResultWrs.setResultBln(false);
            webResultWrs.setSuccessBln(false);
            resultObl.add(e.getLocalizedMessage());
            webResultWrs.setResultObl(resultObl);
            e.printStackTrace();
        }
        return webResultWrs;
    }

    public cWebresult pPickLineGeneratedHandledViaWebserviceWrs(List<cPickorderBarcode> pvScannedBarcodesObl) {
        List<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();


        if (cPickorder.currentPickOrder.pCreateItemVariantGeneratedNeededBln()) {
            try {
                webResultWrs = new mPickorderLineGeneratedCreateVariantViaWebserviceAsyncTask().execute().get();
            } catch (ExecutionException | InterruptedException e) {
                webResultWrs.setResultBln(false);
                webResultWrs.setSuccessBln(false);
                resultObl.add(e.getLocalizedMessage());
                webResultWrs.setResultObl(resultObl);
                e.printStackTrace();
                return webResultWrs;
            }
        }

        try {
            webResultWrs = new mPickorderLineGeneratedHandledViaWebserviceAsyncTask().execute(pvScannedBarcodesObl).get();
        } catch (ExecutionException | InterruptedException e) {
            webResultWrs.setResultBln(false);
            webResultWrs.setSuccessBln(false);
            resultObl.add(e.getLocalizedMessage());
            webResultWrs.setResultObl(resultObl);
            e.printStackTrace();
        }

        return webResultWrs;
    }

    public cWebresult pPickLinePropertysHandledViaWebserviceWrs() {
        List<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();

        try {
            webResultWrs = new mPickorderLinePropertiesHandledViaWebserviceAsyncTask().execute().get();
        } catch (ExecutionException | InterruptedException e) {
            webResultWrs.setResultBln(false);
            webResultWrs.setSuccessBln(false);
            resultObl.add(e.getLocalizedMessage());
            webResultWrs.setResultObl(resultObl);
            e.printStackTrace();
        }
        return webResultWrs;
    }

    public cWebresult pPickLineSortedViaWebserviceWrs() {
        List<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();

        try {
            webResultWrs = new mPickorderLineSortedViaWebserviceAsyncTask().execute().get();
        } catch (ExecutionException | InterruptedException e) {
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
        } catch (ExecutionException | InterruptedException e) {
            webResultWrs.setResultBln(false);
            webResultWrs.setSuccessBln(false);
            resultObl.add(e.getLocalizedMessage());
            webResultWrs.setResultObl(resultObl);
            e.printStackTrace();
        }
        return webResultWrs;
    }

    public cWebresult pResetGeneratedViaWebserviceWrs() {

        List<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();

        try {
            webResultWrs = new mResetGeneratedViaWebserviceAsyncTask().execute().get();
        } catch (ExecutionException | InterruptedException e) {
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
        } catch (ExecutionException | InterruptedException e) {
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
            integerValue = new updateOrderLineQuantityHandledAsyncTask(pickorderLineDao).execute(updateOrderlineQuantityParams).get();

            return integerValue != 0;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return  false;
        }
    }

    public boolean pUpdateProcessingSequenceBln(String pvProcessingSequenceStr) {
        Integer integerValue;
        UpdateOrderlineProcessingSequenceParams updateOrderlineProcessingSequenceParams = new UpdateOrderlineProcessingSequenceParams(cPickorderLine.currentPickOrderLine.getRecordIDInt(),pvProcessingSequenceStr);

        try {
            integerValue = new updateOrderLineProcessingSequenceAsyncTask(pickorderLineDao).execute(updateOrderlineProcessingSequenceParams).get();

            return integerValue != 0;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return  false;
        }
    }

    public boolean pUpdateLocalStatusBln(Integer pvNewStatusInt) {

        Integer integerValue;
        UpdateOrderlineLocaStatusParams updateOrderlineLocaStatusParams = new UpdateOrderlineLocaStatusParams(cPickorderLine.currentPickOrderLine.getRecordIDInt(), pvNewStatusInt);
        try {
            integerValue = new updateOrderLineLocalStatusAsyncTask(pickorderLineDao).execute(updateOrderlineLocaStatusParams).get();
            return integerValue != 0;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return  false;
        }
    }

    public boolean pUpdateLocalHandledTimeStampBln(String pvHandledTimeStampStr) {

        Integer integerValue;
        UpdateOrderlineHandledTimeStampParams updateOrderlineHandledTimeStampParams = new UpdateOrderlineHandledTimeStampParams(cPickorderLine.currentPickOrderLine.getRecordIDInt(), pvHandledTimeStampStr);
        try {
            integerValue = new updateOrderLineLocalHandledTimeStampAsyncTask(pickorderLineDao).execute(updateOrderlineHandledTimeStampParams).get();
            return integerValue != 0;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return  false;
        }
    }

    public cWebresult pAddERPBarcodeViaWebserviceWrs(cBarcodeScan pvBarcodeScan , boolean pvIsUniquBarcodeBln) {
        List<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();

        cPickorderLineRepository.AddBarcodeParams addBarcodeParams = new cPickorderLineRepository.AddBarcodeParams(pvBarcodeScan, pvIsUniquBarcodeBln);

        try {
            webResultWrs = new cPickorderLineRepository.mPickorderERPBarcodeViaWebserviceAsyncTask().execute(addBarcodeParams).get();
        } catch (ExecutionException | InterruptedException e) {
            webResultWrs.setResultBln(false);
            webResultWrs.setSuccessBln(false);
            resultObl.add(e.getLocalizedMessage());
            webResultWrs.setResultObl(resultObl);
            e.printStackTrace();
        }
        return webResultWrs;
    }


    //End Region Public Methods

    private static class mInsertInDatabaseAsyncTask extends AsyncTask<cPickorderLineEntity, Void, Void> {
        private final iPickorderLineDao mAsyncTaskDao;

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
        private final iPickorderLineDao mAsyncTaskDao;

        mDeleteAsyncTask(iPickorderLineDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected Void doInBackground(final cPickorderLineEntity... params) {
            mAsyncTaskDao.deletePickorder(params[0]);
            return null;
        }
    }

    private static class deleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private final iPickorderLineDao mAsyncTaskDao;

        deleteAllAsyncTask(iPickorderLineDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final Void... params) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }

    private static class mPickorderLineGeneratedCreateVariantViaWebserviceAsyncTask extends AsyncTask<Void, Void, cWebresult> {
        @Override
        protected cWebresult doInBackground(Void ... params) {
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
                l_PropertyInfo4Pin.name = cWebserviceDefinitions.WEBPROPERTY_ITEMNO;
                l_PropertyInfo4Pin.setValue(cPickorderLine.currentPickOrderLine.getItemNoStr());
                l_PropertyInfoObl.add(l_PropertyInfo4Pin);

                PropertyInfo l_PropertyInfo5Pin = new PropertyInfo();
                l_PropertyInfo5Pin.name = cWebserviceDefinitions.WEBPROPERTY_VARIANTCODETINY;
                l_PropertyInfo5Pin.setValue(cPickorderLine.currentPickOrderLine.getVariantCodeStr());
                l_PropertyInfoObl.add(l_PropertyInfo5Pin);

                PropertyInfo l_PropertyInfo6Pin = new PropertyInfo();
                l_PropertyInfo6Pin.name = cWebserviceDefinitions.WEBPROPERTY_BARCODE;
                l_PropertyInfo6Pin.setValue(cPickorderBarcode.currentPickorderBarcode.getBarcodeStr());
                l_PropertyInfoObl.add(l_PropertyInfo6Pin);

                PropertyInfo l_PropertyInfo7Pin = new PropertyInfo();
                l_PropertyInfo7Pin.name = cWebserviceDefinitions.WEBPROPERTY_DESTINATIONNO;
                l_PropertyInfo7Pin.setValue(cPickorderLine.currentPickOrderLine.getDestinationNoStr());
                l_PropertyInfoObl.add(l_PropertyInfo7Pin);

                PropertyInfo l_PropertyInfo8Pin = new PropertyInfo();
                l_PropertyInfo8Pin.name = cWebserviceDefinitions.WEBPROPERTY_BINCODE;
                l_PropertyInfo8Pin.setValue(cPickorderLine.currentPickOrderLine.getBinCodeStr());
                l_PropertyInfoObl.add(l_PropertyInfo8Pin);

                webresult = cWebresult.pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_PICKORDERITEMVARIANTCREATE, l_PropertyInfoObl);

            } catch (JSONException e) {
                webresult.setSuccessBln(false);
                webresult.setResultBln(false);
            }
            return webresult;
        }
    }

    private static class mPickorderLineGeneratedHandledViaWebserviceAsyncTask extends AsyncTask<List<cPickorderBarcode> , Void, cWebresult> {
        @Override
        protected cWebresult doInBackground(List<cPickorderBarcode> ... params) {
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
                l_PropertyInfo4Pin.setValue(cPickorder.currentPickOrder.getOrderNumberStr());
                l_PropertyInfoObl.add(l_PropertyInfo4Pin);

                PropertyInfo l_PropertyInfo5Pin = new PropertyInfo();
                l_PropertyInfo5Pin.name = cWebserviceDefinitions.WEBPROPERTY_HANDLEDTIMESTAMP;
                l_PropertyInfo5Pin.setValue(cPickorderLine.currentPickOrderLine.getTakenTimeStampStr());
                l_PropertyInfoObl.add(l_PropertyInfo5Pin);

                PropertyInfo l_PropertyInfo6Pin = new PropertyInfo();
                l_PropertyInfo6Pin.name = cWebserviceDefinitions.WEBPROPERTY_ITEMNO;
                l_PropertyInfo6Pin.setValue(cPickorderLine.currentPickOrderLine.getItemNoStr());
                l_PropertyInfoObl.add(l_PropertyInfo6Pin);

                PropertyInfo l_PropertyInfo7Pin = new PropertyInfo();
                l_PropertyInfo7Pin.name = cWebserviceDefinitions.WEBPROPERTY_VARIANTCODE;
                l_PropertyInfo7Pin.setValue(cPickorderLine.currentPickOrderLine.getVariantCodeStr());
                l_PropertyInfoObl.add(l_PropertyInfo7Pin);

                PropertyInfo l_PropertyInfo8Pin = new PropertyInfo();
                l_PropertyInfo8Pin.name = cWebserviceDefinitions.WEBPROPERTY_DESTINATIONNO;
                l_PropertyInfo8Pin.setValue(cPickorderLine.currentPickOrderLine.getDestinationNoStr());
                l_PropertyInfoObl.add(l_PropertyInfo8Pin);

                PropertyInfo l_PropertyInfo9Pin = new PropertyInfo();
                l_PropertyInfo9Pin.name = cWebserviceDefinitions.WEBPROPERTY_BINCODE;
                l_PropertyInfo9Pin.setValue(cPickorderLine.currentPickOrderLine.getBinCodeStr());
                l_PropertyInfoObl.add(l_PropertyInfo9Pin);

                SoapObject barcodesHandledList = new SoapObject(cWebservice.WEBSERVICE_NAMESPACE, cWebserviceDefinitions.WEBPROPERTY_BARCODESLIST);

                //Only loop through handled barcodes, of there are any
                if (params != null) {
                    for (cPickorderBarcode pickorderBarcode  :params[0]) {
                        SoapObject soapObject = new SoapObject(cWebservice.WEBSERVICE_NAMESPACE, cWebserviceDefinitions.WEBPROPERTY_BARCODEHANDLED_COMPLEX);
                        soapObject.addProperty(cWebserviceDefinitions.WEBPROPERTY_BARCODE_COMPLEX, pickorderBarcode.getBarcodeStr());
                        soapObject.addProperty(cWebserviceDefinitions.WEBPROPERTY_QUANTITYHANDLED_COMPLEX, pickorderBarcode.getQuantityHandledDbl());
                        barcodesHandledList.addSoapObject(soapObject);
                    }
                }

                PropertyInfo l_PropertyInfo10Pin = new PropertyInfo();
                l_PropertyInfo10Pin.name = cWebserviceDefinitions.WEBPROPERTY_BARCODELIST;
                l_PropertyInfo10Pin.setValue(barcodesHandledList);
                l_PropertyInfoObl.add(l_PropertyInfo10Pin);

                SoapObject propertiesHandledObl = new SoapObject(cWebservice.WEBSERVICE_NAMESPACE, cWebserviceDefinitions.WEBPROPERTY_PROPERTIESHANDLED);
                PropertyInfo l_PropertyInfo11Pin = new PropertyInfo();
                l_PropertyInfo11Pin.name = cWebserviceDefinitions.WEBPROPERTY_PROPERTIESHANDLED;
                l_PropertyInfo11Pin.setValue(propertiesHandledObl);
                l_PropertyInfoObl.add(l_PropertyInfo11Pin);

                webresult = cWebresult.pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_PICKORDERLINEGENERATEDTAKEHANDLED, l_PropertyInfoObl);

            } catch (JSONException e) {
                webresult.setSuccessBln(false);
                webresult.setResultBln(false);
            }
            return webresult;
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
                l_PropertyInfo6Pin.setValue("");
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

                SoapObject containerList = new SoapObject(cWebservice.WEBSERVICE_NAMESPACE, cWebserviceDefinitions.WEBPROPERTY_CONTAINERSLIST);
                //Only loop through list if there are any
                if(cPickorderLine.currentPickOrderLine.containerObl != null) {
                    for (cContentlabelContainer contentlabelContainer : cPickorderLine.currentPickOrderLine.containerObl){
                        SoapObject soapObject = new SoapObject(cWebservice.WEBSERVICE_NAMESPACE, cWebserviceDefinitions.WEBPROPERTY_CONTAINERHANDLED_COMPLEX);
                        soapObject.addProperty(cWebserviceDefinitions.WEBPROPERTY_CONTAINERSEQUENCENO_COMPLEX, contentlabelContainer.getContainerSequencoNoLng());
                        soapObject.addProperty(cWebserviceDefinitions.WEBPROPERTY_QUANTITYHANDLED_COMPLEX, contentlabelContainer.getQuantityHandledDbl());
                        containerList.addSoapObject(soapObject);
                    }
                }

                PropertyInfo l_PropertyInfo8Pin = new PropertyInfo();
                l_PropertyInfo8Pin.name = cWebserviceDefinitions.WEBPROPERTY_CONTAINERSLIST;
                l_PropertyInfo8Pin.setValue(containerList);
                l_PropertyInfoObl.add(l_PropertyInfo8Pin);

                PropertyInfo l_PropertyInfo9Pin = new PropertyInfo();
                l_PropertyInfo9Pin.name = cWebserviceDefinitions.WEBPROPERTY_PROCESSINGSEQUENCE;
                l_PropertyInfo9Pin.setValue(cPickorderLine.currentPickOrderLine.getProcessingSequenceStr());
                l_PropertyInfoObl.add(l_PropertyInfo9Pin);

                webresult = cWebresult.pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_PICKORDERLINE_HANDLED, l_PropertyInfoObl);

            } catch (JSONException e) {
                webresult.setSuccessBln(false);
                webresult.setResultBln(false);
            }
            return webresult;
        }
    }

    private static class mPickorderLinePropertiesHandledViaWebserviceAsyncTask extends AsyncTask<Void, Void, cWebresult> {
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
                l_PropertyInfo2Pin.name = cWebserviceDefinitions.WEBPROPERTY_ORDERTYPE;
                l_PropertyInfo2Pin.setValue((cWarehouseorder.OrderTypeEnu.PICKEN.toString().toUpperCase()));
                l_PropertyInfoObl.add(l_PropertyInfo2Pin);

                PropertyInfo l_PropertyInfo3Pin = new PropertyInfo();
                l_PropertyInfo3Pin.name = cWebserviceDefinitions.WEBPROPERTY_LOCATION_NL;
                l_PropertyInfo3Pin.setValue(cUser.currentUser.currentBranch.getBranchStr());
                l_PropertyInfoObl.add(l_PropertyInfo3Pin);

                PropertyInfo l_PropertyInfo4Pin = new PropertyInfo();
                l_PropertyInfo4Pin.name = cWebserviceDefinitions.WEBPROPERTY_ORDERNUMBER;
                l_PropertyInfo4Pin.setValue(cPickorder.currentPickOrder.getOrderNumberStr());
                l_PropertyInfoObl.add(l_PropertyInfo4Pin);

                PropertyInfo l_PropertyInfo5Pin = new PropertyInfo();
                l_PropertyInfo5Pin.name = cWebserviceDefinitions.WEBPROPERTY_LINENO;
                l_PropertyInfo5Pin.setValue(cPickorderLine.currentPickOrderLine.getLineNoInt());
                l_PropertyInfoObl.add(l_PropertyInfo5Pin);

                SoapObject propertysHandledList = new SoapObject(cWebservice.WEBSERVICE_NAMESPACE, cWebserviceDefinitions.WEBPROPERTY_PROPERTIESHANDLED);

                //Only loop through handled properties, of there are any
                if (cPickorderLine.currentPickOrderLine.linePropertyValuesObl() != null) {

                    int countForPropertyInt = 0;
                    int countForValueInt;
                    String propertyCodeStr = "";

                    for (cLinePropertyValue linePropertyValue: cPickorderLine.currentPickOrderLine.linePropertyValuesObl()) {

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
                            propertysHandledList.addSoapObject(soapObject);
                            countForValueInt++;
                            countForPropertyInt++;
                        }
                    }
                }

                PropertyInfo l_PropertyInfo6Pin = new PropertyInfo();
                l_PropertyInfo6Pin.name = cWebserviceDefinitions.WEBPROPERTY_PROPERTIESHANDLED;
                l_PropertyInfo6Pin.setValue(propertysHandledList);
                l_PropertyInfoObl.add(l_PropertyInfo6Pin);

                webresult = cWebresult.pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_LINE_PROPERTIES_HANDLED, l_PropertyInfoObl);

            } catch (JSONException e) {
                webresult.setSuccessBln(false);
                webresult.setResultBln(false);
            }
            return webresult;
        }
    }

    private static class mPickorderLineSortedViaWebserviceAsyncTask extends AsyncTask<Void, Void, cWebresult> {
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
                l_PropertyInfo7Pin.setValue(cPickorderLine.currentPickOrderLine.handledBarcodesObl().get(0).getBarcodeStr());
                l_PropertyInfoObl.add(l_PropertyInfo7Pin);

                SoapObject propertieHandledList = new SoapObject(cWebservice.WEBSERVICE_NAMESPACE, cWebserviceDefinitions.WEBPROPERTY_PROPERTIESHANDLED);

                PropertyInfo l_PropertyInfo8Pin = new PropertyInfo();
                l_PropertyInfo8Pin.name = cWebserviceDefinitions.WEBPROPERTY_PROPERTIESHANDLED;
                l_PropertyInfo8Pin.setValue(propertieHandledList);
                l_PropertyInfoObl.add(l_PropertyInfo8Pin);

                PropertyInfo l_PropertyInfo9Pin = new PropertyInfo();
                l_PropertyInfo9Pin.name = cWebserviceDefinitions.WEBPROPERTY_CONTAINERSLIST;
                l_PropertyInfo9Pin.setValue(null);
                l_PropertyInfoObl.add(l_PropertyInfo9Pin);

                PropertyInfo l_PropertyInfo10Pin = new PropertyInfo();
                l_PropertyInfo10Pin.name = cWebserviceDefinitions.WEBPROPERTY_LOCATION;
                l_PropertyInfo10Pin.setValue(cPickorderLine.currentPickOrderLine.getProcessingSequenceStr());
                l_PropertyInfoObl.add(l_PropertyInfo10Pin);

                webresult = cWebresult.pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_SORTORDERLINE_HANDLED, l_PropertyInfoObl);

            } catch (JSONException e) {
                webresult.setSuccessBln(false);
                webresult.setResultBln(false);
            }
            return webresult;
        }
    }

    private static class updateOrderLineQuantityHandledAsyncTask extends AsyncTask<UpdateOrderlineQuantityParams, Void, Integer> {
        private final iPickorderLineDao mAsyncTaskDao;
        updateOrderLineQuantityHandledAsyncTask(iPickorderLineDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected Integer doInBackground(UpdateOrderlineQuantityParams... params) {
            return mAsyncTaskDao.updateOrderLineQuantityHandled(params[0].recordIdint, params[0].quantityDbl);
        }
    }

    private static class updateOrderLineProcessingSequenceAsyncTask extends AsyncTask<UpdateOrderlineProcessingSequenceParams, Void, Integer> {
        private final iPickorderLineDao mAsyncTaskDao;
        updateOrderLineProcessingSequenceAsyncTask(iPickorderLineDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected Integer doInBackground(UpdateOrderlineProcessingSequenceParams... params) {
            return mAsyncTaskDao.updateOrderLineProcessingSequence(params[0].recordIDInt, params[0].processingSequenceStr);
        }
    }

    private static class updateOrderLineLocalStatusAsyncTask extends AsyncTask<UpdateOrderlineLocaStatusParams, Void, Integer> {
        private final iPickorderLineDao mAsyncTaskDao;
        updateOrderLineLocalStatusAsyncTask(iPickorderLineDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected Integer doInBackground(UpdateOrderlineLocaStatusParams... params) {
            return mAsyncTaskDao.updateOrderLineLocalStatus(params[0].recordIDInt, params[0].newStatusInt);
        }
    }

    private static class updateOrderLineLocalHandledTimeStampAsyncTask extends AsyncTask<UpdateOrderlineHandledTimeStampParams, Void, Integer> {
        private final iPickorderLineDao mAsyncTaskDao;
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

                l_WebresultWrs = cWebresult.pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_PICKORDERLINERESET, l_PropertyInfoObl);
            } catch (JSONException e) {
                l_WebresultWrs.setSuccessBln(false);
                l_WebresultWrs.setResultBln(false);
            }
            return l_WebresultWrs;
        }
    }

    private static class mResetGeneratedViaWebserviceAsyncTask extends AsyncTask<Void, Void, cWebresult> {
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
                l_PropertyInfo4Pin.name = cWebserviceDefinitions.WEBPROPERTY_LINENO;
                l_PropertyInfo4Pin.setValue(cPickorderLine.currentPickOrderLine.getLineNoInt());
                l_PropertyInfoObl.add(l_PropertyInfo4Pin);

                l_WebresultWrs = cWebresult.pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_PICKORDERLINERESETGENERATED, l_PropertyInfoObl);
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

                l_WebresultWrs = cWebresult.pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_GETSORTLOCATIONADVICE, l_PropertyInfoObl);
            } catch (JSONException e) {
                l_WebresultWrs.setSuccessBln(false);
                l_WebresultWrs.setResultBln(false);
            }
            return l_WebresultWrs;
        }
    }

    private static class mPickorderERPBarcodeViaWebserviceAsyncTask extends AsyncTask<cPickorderLineRepository.AddBarcodeParams, Void, cWebresult> {
        @Override
        protected cWebresult doInBackground(cPickorderLineRepository.AddBarcodeParams... params) {
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
                l_PropertyInfo4Pin.name = cWebserviceDefinitions.WEBPROPERTY_ITEMNO;
                l_PropertyInfo4Pin.setValue(cPickorder.currentPickOrder.currentArticle.getItemNoStr());
                l_PropertyInfoObl.add(l_PropertyInfo4Pin);

                PropertyInfo l_PropertyInfo5Pin = new PropertyInfo();
                l_PropertyInfo5Pin.name = cWebserviceDefinitions.WEBPROPERTY_VARIANTCODETINY;
                l_PropertyInfo5Pin.setValue(cPickorder.currentPickOrder.currentArticle.getVariantCodeStr());
                l_PropertyInfoObl.add(l_PropertyInfo5Pin);

                PropertyInfo l_PropertyInfo6Pin = new PropertyInfo();
                l_PropertyInfo6Pin.name = cWebserviceDefinitions.WEBPROPERTY_BARCODE;
                l_PropertyInfo6Pin.setValue(params[0].barcodeScan.getBarcodeOriginalStr());
                l_PropertyInfoObl.add(l_PropertyInfo6Pin);

                PropertyInfo l_PropertyInfo7Pin = new PropertyInfo();
                l_PropertyInfo7Pin.name = cWebserviceDefinitions.WEBPROPERTY_BARCODETYPE;
                l_PropertyInfo7Pin.setValue(cBarcodeScan.BarcodeType.EAN13);
                l_PropertyInfoObl.add(l_PropertyInfo7Pin);

                PropertyInfo l_PropertyInfo8Pin = new PropertyInfo();
                l_PropertyInfo8Pin.name = cWebserviceDefinitions.WEBPROPERTY_ISUNIQUEBARCODE;
                l_PropertyInfo8Pin.setValue(params[0].isUniqueBarcodeBln);
                l_PropertyInfoObl.add(l_PropertyInfo8Pin);

                PropertyInfo l_PropertyInfo9Pin = new PropertyInfo();
                l_PropertyInfo9Pin.name = cWebserviceDefinitions.WEBPROPERTY_QUANTITYPERUNITOFMEASURE;
                l_PropertyInfo9Pin.setValue(1);
                l_PropertyInfoObl.add(l_PropertyInfo9Pin);

                PropertyInfo l_PropertyInfo10Pin = new PropertyInfo();
                l_PropertyInfo10Pin.name = cWebserviceDefinitions.WEBPROPERTY_UNITOFMEASURE;
                l_PropertyInfo10Pin.setValue("STUK");
                l_PropertyInfoObl.add(l_PropertyInfo10Pin);

                PropertyInfo l_PropertyInfo11Pin = new PropertyInfo();
                l_PropertyInfo11Pin.name = cWebserviceDefinitions.WEBPROPERTY_ITEMTYPE;
                l_PropertyInfo11Pin.setValue("");
                l_PropertyInfoObl.add(l_PropertyInfo11Pin);

                webresult =  cWebresult.pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_PICKORDERBARCODECREATE, l_PropertyInfoObl);

            } catch (JSONException e) {
                webresult.setSuccessBln(false);
                webresult.setResultBln(false);
            }
            return webresult;
        }
    }


}
