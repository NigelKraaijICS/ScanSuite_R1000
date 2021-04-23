package SSU_WHS.Intake.IntakeorderMATLines;

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
import SSU_WHS.Intake.IntakeorderBarcodes.cIntakeorderBarcode;
import SSU_WHS.Intake.IntakeorderMATLineBarcodes.cIntakeorderMATLineBarcode;
import SSU_WHS.Intake.IntakeorderMATLineSummary.cIntakeorderMATSummaryLine;
import SSU_WHS.Intake.Intakeorders.cIntakeorder;
import SSU_WHS.LineItemProperty.LinePropertyValue.cLinePropertyValue;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebservice;
import SSU_WHS.Webservice.cWebserviceDefinitions;

public class cIntakeorderMATLineRepository {
    //Region Public Properties

    //End Region Public Properties

    //Region Private Properties
    private iIntakeorderMATLineDao intakeorderMATLineDao;

    private static class UpdateOrderlineLocaStatusParams {
        Integer lineNoInt;
        Integer newStatusInt;

        UpdateOrderlineLocaStatusParams(Integer pvLineNoInt, Integer pvNewsStatusInt) {
            this.lineNoInt = pvLineNoInt;
            this.newStatusInt = pvNewsStatusInt;
        }
    }

    private static class UpdateOrderlineQuantityParams {
        Integer lineNoInt;
        Double quantityDbl;

        UpdateOrderlineQuantityParams(Integer pvLineNoInt, Double pvQuantityDbl) {
            this.lineNoInt = pvLineNoInt;
            this.quantityDbl = pvQuantityDbl;
        }
    }


    private static class CreateLineParams {
        String binCodeStr;
        List<cIntakeorderBarcode> barcodeObl;

        CreateLineParams(String pvBinCodeStr,List<cIntakeorderBarcode> pvBarcodeObl ) {
            this.binCodeStr = pvBinCodeStr;
            this.barcodeObl = pvBarcodeObl;
        }
    }


    //Region Constructor
    cIntakeorderMATLineRepository(Application pvApplication) {
        acScanSuiteDatabase db = acScanSuiteDatabase.pGetDatabase(pvApplication);
        this.intakeorderMATLineDao = db.intakeorderMATLineDao();
    }
    //End Region Constructor

    //Region Public Methods



    public void insert (cIntakeorderMATLineEntity intakeorderMATLineEntity) {
        new mInsertAsyncTask(intakeorderMATLineDao).execute(intakeorderMATLineEntity);
    }

    public void deleteAll () {
        new mDeleteAllAsyncTask(intakeorderMATLineDao).execute();
    }

    public boolean pUpdateLocalStatusBln(Integer pvNewStatusInt) {

        Integer integerValue;

        UpdateOrderlineLocaStatusParams updateOrderlineLocaStatusParams = new UpdateOrderlineLocaStatusParams(cIntakeorderMATLine.currentIntakeorderMATLine.getLineNoInt(), pvNewStatusInt);
        try {
            integerValue = new updateOrderLineLocalStatusAsyncTask(intakeorderMATLineDao).execute(updateOrderlineLocaStatusParams).get();
            return integerValue != 0;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return  false;
        }
    }

    public boolean pUpdateQuantityHandledBln(Double pvQuantityDbl) {

        Integer integerValue;
        UpdateOrderlineQuantityParams updateOrderlineQuantityParams = new UpdateOrderlineQuantityParams(cIntakeorderMATLine.currentIntakeorderMATLine.getLineNoInt(), pvQuantityDbl);

        try {
            integerValue = new updateOrderLineQuantityHandledAsyncTask(intakeorderMATLineDao).execute(updateOrderlineQuantityParams).get();

            return integerValue != 0;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return  false;
        }

    }

    public cWebresult pResetMATLineViaWebserviceWrs() {

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

    public cWebresult pMATLineHandledViaWebserviceWrs() {
        List<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();

        try {
            webResultWrs = new mMATLineHandledViaWebserviceAsyncTask().execute().get();
        } catch (ExecutionException | InterruptedException e) {
            webResultWrs.setResultBln(false);
            webResultWrs.setSuccessBln(false);
            resultObl.add(e.getLocalizedMessage());
            webResultWrs.setResultObl(resultObl);
            e.printStackTrace();
        }
        return webResultWrs;
    }

    public cWebresult pMATLineSplitViaWebserviceWrs() {
        List<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();

        try {
            webResultWrs = new mMATLineSplitViaWebserviceAsyncTask().execute().get();
        } catch (ExecutionException | InterruptedException e) {
            webResultWrs.setResultBln(false);
            webResultWrs.setSuccessBln(false);
            resultObl.add(e.getLocalizedMessage());
            webResultWrs.setResultObl(resultObl);
            e.printStackTrace();
        }
        return webResultWrs;
    }

    public cWebresult pCreateMATLineViaWebserviceWrs(String pvBinCodeStr, List<cIntakeorderBarcode> pvBarcodeObl) {
        List<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();

        CreateLineParams createLineParams = new CreateLineParams(pvBinCodeStr, pvBarcodeObl);
        try {
            webResultWrs = new mMATLineCreateViaWebserviceAsyncTask().execute(createLineParams).get();
        } catch (ExecutionException | InterruptedException e) {
            webResultWrs.setResultBln(false);
            webResultWrs.setSuccessBln(false);
            resultObl.add(e.getLocalizedMessage());
            webResultWrs.setResultObl(resultObl);
            e.printStackTrace();
        }
        return webResultWrs;
    }

    public cWebresult pCreateStoreLineViaWebserviceWrs(String pvBinCodeStr, List<cIntakeorderBarcode> pvBarcodeObl) {
        List<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();

        CreateLineParams createLineParams = new CreateLineParams(pvBinCodeStr, pvBarcodeObl);
        try {
            webResultWrs = new mStoreLineCreateViaWebserviceAsyncTask().execute(createLineParams).get();
        } catch (ExecutionException | InterruptedException e) {
            webResultWrs.setResultBln(false);
            webResultWrs.setSuccessBln(false);
            resultObl.add(e.getLocalizedMessage());
            webResultWrs.setResultObl(resultObl);
            e.printStackTrace();
        }
        return webResultWrs;
    }

    private static class mInsertAsyncTask extends AsyncTask<cIntakeorderMATLineEntity, Void, Void> {
        private iIntakeorderMATLineDao mAsyncTaskDao;

        mInsertAsyncTask(iIntakeorderMATLineDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final cIntakeorderMATLineEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class mDeleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private iIntakeorderMATLineDao mAsyncTaskDao;

        mDeleteAllAsyncTask(iIntakeorderMATLineDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final Void... params) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }

    private static class updateOrderLineLocalStatusAsyncTask extends AsyncTask<UpdateOrderlineLocaStatusParams, Void, Integer> {
        private iIntakeorderMATLineDao mAsyncTaskDao;
        updateOrderLineLocalStatusAsyncTask(iIntakeorderMATLineDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected Integer doInBackground(UpdateOrderlineLocaStatusParams... params) {
            return mAsyncTaskDao.updateOrderLineLocalStatus(params[0].lineNoInt, params[0].newStatusInt);
        }
    }

    private static class updateOrderLineQuantityHandledAsyncTask extends AsyncTask<UpdateOrderlineQuantityParams, Void, Integer> {
        private iIntakeorderMATLineDao mAsyncTaskDao;
        updateOrderLineQuantityHandledAsyncTask(iIntakeorderMATLineDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected Integer doInBackground(UpdateOrderlineQuantityParams... params) {
            return mAsyncTaskDao.updateOrderLineQuantityHandled(params[0].lineNoInt, params[0].quantityDbl);
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
                l_PropertyInfo3Pin.setValue(cIntakeorder.currentIntakeOrder.getOrderNumberStr());
                l_PropertyInfoObl.add(l_PropertyInfo3Pin);

                PropertyInfo l_PropertyInfo4Pin = new PropertyInfo();
                l_PropertyInfo4Pin.name = cWebserviceDefinitions.WEBPROPERTY_LINENO;
                l_PropertyInfo4Pin.setValue(cIntakeorderMATLine.currentIntakeorderMATLine.getLineNoInt());
                l_PropertyInfoObl.add(l_PropertyInfo4Pin);

                l_WebresultWrs =  cWebresult.pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_INTAKELINEMATRESET, l_PropertyInfoObl);
            } catch (JSONException e) {
                l_WebresultWrs.setSuccessBln(false);
                l_WebresultWrs.setResultBln(false);
            }
            return l_WebresultWrs;
        }
    }

    private static class mMATLineHandledViaWebserviceAsyncTask extends AsyncTask<Void, Void, cWebresult> {
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
                l_PropertyInfo4Pin.setValue(cIntakeorder.currentIntakeOrder.getOrderNumberStr());
                l_PropertyInfoObl.add(l_PropertyInfo4Pin);

                PropertyInfo l_PropertyInfo5Pin = new PropertyInfo();
                l_PropertyInfo5Pin.name = cWebserviceDefinitions.WEBPROPERTY_LINENO;
                l_PropertyInfo5Pin.setValue(cIntakeorderMATLine.currentIntakeorderMATLine.getLineNoInt());
                l_PropertyInfoObl.add(l_PropertyInfo5Pin);

                PropertyInfo l_PropertyInfo6Pin = new PropertyInfo();
                l_PropertyInfo6Pin.name = cWebserviceDefinitions.WEBPROPERTY_BINCODEHANDLED;
                l_PropertyInfo6Pin.setValue(cIntakeorderMATLine.currentIntakeorderMATLine.getBinCodeHandledStr());
                l_PropertyInfoObl.add(l_PropertyInfo6Pin);

                PropertyInfo l_PropertyInfo7Pin = new PropertyInfo();
                l_PropertyInfo7Pin.name = cWebserviceDefinitions.WEBPROPERTY_HANDLEDTIMESTAMP;
                l_PropertyInfo7Pin.setValue((cDateAndTime.pGetCurrentDateTimeForWebserviceStr()));
                l_PropertyInfoObl.add(l_PropertyInfo7Pin);


                SoapObject barcodesHandledList = new SoapObject(cWebservice.WEBSERVICE_NAMESPACE, cWebserviceDefinitions.WEBPROPERTY_BARCODESLIST);

                //Only loop through handled barcodes, of there are any
                if (cIntakeorderMATLine.currentIntakeorderMATLine.handledBarcodesObl() != null) {
                    for (cIntakeorderMATLineBarcode intakeorderMATLineBarcode: cIntakeorderMATLine.currentIntakeorderMATLine.handledBarcodesObl()) {
                        SoapObject soapObject = new SoapObject(cWebservice.WEBSERVICE_NAMESPACE, cWebserviceDefinitions.WEBPROPERTY_BARCODEHANDLED_COMPLEX);
                        soapObject.addProperty(cWebserviceDefinitions.WEBPROPERTY_BARCODE_COMPLEX, intakeorderMATLineBarcode.getBarcodeStr());
                        soapObject.addProperty(cWebserviceDefinitions.WEBPROPERTY_QUANTITYHANDLED_COMPLEX, intakeorderMATLineBarcode.getQuantityhandledDbl());
                        barcodesHandledList.addSoapObject(soapObject);
                    }
                }

                PropertyInfo l_PropertyInfo8Pin = new PropertyInfo();
                l_PropertyInfo8Pin.name = cWebserviceDefinitions.WEBPROPERTY_BARCODELIST;
                l_PropertyInfo8Pin.setValue(barcodesHandledList);
                l_PropertyInfoObl.add(l_PropertyInfo8Pin);

                SoapObject propertiesHandledObl = new SoapObject(cWebservice.WEBSERVICE_NAMESPACE, cWebserviceDefinitions.WEBPROPERTY_PROPERTIESHANDLED);

                //Only loop through handled properties, of there are any
                if (cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.handledPropertyValueObl != null) {

                    int countForPropertyInt = 0;
                    int countForValueInt;
                    String propertyCodeStr = "";

                    for (cLinePropertyValue linePropertyValue: cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.handledPropertyValueObl) {

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
                PropertyInfo l_PropertyInfo9Pin = new PropertyInfo();
                l_PropertyInfo9Pin.name = cWebserviceDefinitions.WEBPROPERTY_PROPERTIESHANDLED;
                l_PropertyInfo9Pin.setValue(propertiesHandledObl);
                l_PropertyInfoObl.add(l_PropertyInfo9Pin);


                webresult = cWebresult.pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_INTAKELINEMATHANDLED, l_PropertyInfoObl);

            } catch (JSONException e) {
                webresult.setSuccessBln(false);
                webresult.setResultBln(false);
            }
            return webresult;
        }
    }

    private static class mMATLineSplitViaWebserviceAsyncTask extends AsyncTask<Void, Void, cWebresult> {
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
                l_PropertyInfo4Pin.setValue(cIntakeorder.currentIntakeOrder.getOrderNumberStr());
                l_PropertyInfoObl.add(l_PropertyInfo4Pin);

                PropertyInfo l_PropertyInfo5Pin = new PropertyInfo();
                l_PropertyInfo5Pin.name = cWebserviceDefinitions.WEBPROPERTY_LINENO;
                l_PropertyInfo5Pin.setValue(cIntakeorderMATLine.currentIntakeorderMATLine.getLineNoInt());
                l_PropertyInfoObl.add(l_PropertyInfo5Pin);


                PropertyInfo l_PropertyInfo6Pin = new PropertyInfo();
                l_PropertyInfo6Pin.name = cWebserviceDefinitions.WEBPROPERTY_HANDLEDTIMESTAMP;
                l_PropertyInfo6Pin.setValue((cDateAndTime.pGetCurrentDateTimeForWebserviceStr()));
                l_PropertyInfoObl.add(l_PropertyInfo6Pin);

                PropertyInfo l_PropertyInfo7Pin = new PropertyInfo();
                l_PropertyInfo7Pin.name = cWebserviceDefinitions.WEBPROPERTY_BINCODEHANDLED;
                l_PropertyInfo7Pin.setValue(cIntakeorderMATLine.currentIntakeorderMATLine.getBinCodeHandledStr());
                l_PropertyInfoObl.add(l_PropertyInfo7Pin);

                SoapObject barcodesHandledList = new SoapObject(cWebservice.WEBSERVICE_NAMESPACE, cWebserviceDefinitions.WEBPROPERTY_BARCODESLIST);

                //Only loop through handled barcodes, of there are any
                if (cIntakeorderMATLine.currentIntakeorderMATLine.handledBarcodesObl() != null) {
                    for (cIntakeorderMATLineBarcode intakeorderMATLineBarcode: cIntakeorderMATLine.currentIntakeorderMATLine.handledBarcodesObl()) {
                        SoapObject soapObject = new SoapObject(cWebservice.WEBSERVICE_NAMESPACE, cWebserviceDefinitions.WEBPROPERTY_BARCODEHANDLED_COMPLEX);
                        soapObject.addProperty(cWebserviceDefinitions.WEBPROPERTY_BARCODE_COMPLEX, intakeorderMATLineBarcode.getBarcodeStr());
                        soapObject.addProperty(cWebserviceDefinitions.WEBPROPERTY_QUANTITYHANDLED_COMPLEX, intakeorderMATLineBarcode.getQuantityhandledDbl());
                        barcodesHandledList.addSoapObject(soapObject);
                    }
                }

                PropertyInfo l_PropertyInfo8Pin = new PropertyInfo();
                l_PropertyInfo8Pin.name = cWebserviceDefinitions.WEBPROPERTY_BARCODELIST;
                l_PropertyInfo8Pin.setValue(barcodesHandledList);
                l_PropertyInfoObl.add(l_PropertyInfo8Pin);

                SoapObject propertiesHandledObl = new SoapObject(cWebservice.WEBSERVICE_NAMESPACE, cWebserviceDefinitions.WEBPROPERTY_PROPERTIESHANDLED);

                //Only loop through handled properties, of there are any
                if (cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.handledPropertyValueObl != null) {

                    int countForPropertyInt = 0;
                    int countForValueInt;
                    String propertyCodeStr = "";

                    for (cLinePropertyValue linePropertyValue: cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.handledPropertyValueObl) {

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

                PropertyInfo l_PropertyInfo9Pin = new PropertyInfo();
                l_PropertyInfo9Pin.name = cWebserviceDefinitions.WEBPROPERTY_PROPERTIESHANDLED;
                l_PropertyInfo9Pin.setValue(propertiesHandledObl);
                l_PropertyInfoObl.add(l_PropertyInfo9Pin);


                webresult = cWebresult.pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_INTAKELINEMATHANDLEDPART, l_PropertyInfoObl);

            } catch (JSONException e) {
                webresult.setSuccessBln(false);
                webresult.setResultBln(false);
            }
            return webresult;
        }
    }

    private static class mMATLineCreateViaWebserviceAsyncTask extends AsyncTask<CreateLineParams, Void, cWebresult> {
        @Override
        protected cWebresult doInBackground(CreateLineParams... params) {
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
                l_PropertyInfo4Pin.setValue(cIntakeorder.currentIntakeOrder.getOrderNumberStr());
                l_PropertyInfoObl.add(l_PropertyInfo4Pin);

                PropertyInfo l_PropertyInfo5Pin = new PropertyInfo();
                l_PropertyInfo5Pin.name = cWebserviceDefinitions.WEBPROPERTY_ITEMNO;
                l_PropertyInfo5Pin.setValue(cIntakeorderBarcode.currentIntakeOrderBarcode.getItemNoStr());
                l_PropertyInfoObl.add(l_PropertyInfo5Pin);

                PropertyInfo l_PropertyInfo6Pin = new PropertyInfo();
                l_PropertyInfo6Pin.name = cWebserviceDefinitions.WEBPROPERTY_VARIANTCODE;
                l_PropertyInfo6Pin.setValue(cIntakeorderBarcode.currentIntakeOrderBarcode.getVariantCodeStr());
                l_PropertyInfoObl.add(l_PropertyInfo6Pin);

                PropertyInfo l_PropertyInfo7Pin = new PropertyInfo();
                l_PropertyInfo7Pin.name = cWebserviceDefinitions.WEBPROPERTY_BINCODE;
                l_PropertyInfo7Pin.setValue(params[0].binCodeStr);
                l_PropertyInfoObl.add(l_PropertyInfo7Pin);

                PropertyInfo l_PropertyInfo8Pin = new PropertyInfo();
                l_PropertyInfo8Pin.name = cWebserviceDefinitions.WEBPROPERTY_HANDLEDTIMESTAMP;
                l_PropertyInfo8Pin.setValue(cDateAndTime.pGetCurrentDateTimeForWebserviceStr());
                l_PropertyInfoObl.add(l_PropertyInfo8Pin);

                SoapObject barcodesHandledList = new SoapObject(cWebservice.WEBSERVICE_NAMESPACE, cWebserviceDefinitions.WEBPROPERTY_BARCODESLIST);

                //Only loop through handled barcodes, of there are any
                if (params[0].barcodeObl != null) {
                    for (cIntakeorderBarcode intakeorderBarcode: params[0].barcodeObl ) {
                        SoapObject soapObject = new SoapObject(cWebservice.WEBSERVICE_NAMESPACE, cWebserviceDefinitions.WEBPROPERTY_BARCODEHANDLED_COMPLEX);
                        soapObject.addProperty(cWebserviceDefinitions.WEBPROPERTY_BARCODE_COMPLEX, intakeorderBarcode.getBarcodeStr());
                        soapObject.addProperty(cWebserviceDefinitions.WEBPROPERTY_QUANTITYHANDLED_COMPLEX, intakeorderBarcode.getQuantityPerUnitOfMeasureDbl());
                        barcodesHandledList.addSoapObject(soapObject);
                    }
                }

                PropertyInfo l_PropertyInfo9Pin = new PropertyInfo();
                l_PropertyInfo9Pin.name = cWebserviceDefinitions.WEBPROPERTY_BARCODELIST;
                l_PropertyInfo9Pin.setValue(barcodesHandledList);
                l_PropertyInfoObl.add(l_PropertyInfo9Pin);

                SoapObject propertiesHandledObl = new SoapObject(cWebservice.WEBSERVICE_NAMESPACE, cWebserviceDefinitions.WEBPROPERTY_PROPERTIESHANDLED);

                //Only loop through handled properties, of there are any
                if (cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.handledPropertyValueObl != null) {

                    int countForPropertyInt = 0;
                    int countForValueInt;
                    String propertyCodeStr = "";

                    for (cLinePropertyValue linePropertyValue: cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.handledPropertyValueObl) {

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
                PropertyInfo l_PropertyInfo10Pin = new PropertyInfo();
                l_PropertyInfo10Pin.name = cWebserviceDefinitions.WEBPROPERTY_PROPERTIESHANDLED;
                l_PropertyInfo10Pin.setValue(propertiesHandledObl);
                l_PropertyInfoObl.add(l_PropertyInfo10Pin);


                webresult = cWebresult.pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_INTAKEITEMHANLED, l_PropertyInfoObl);

            } catch (JSONException e) {
                webresult.setSuccessBln(false);
                webresult.setResultBln(false);
            }
            return webresult;
        }
    }

    private static class mStoreLineCreateViaWebserviceAsyncTask extends AsyncTask<CreateLineParams, Void, cWebresult> {
        @Override
        protected cWebresult doInBackground(CreateLineParams... params) {
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


                PropertyInfo l_PropertyInfo4PinPn = new PropertyInfo();
                l_PropertyInfo4PinPn.name = cWebserviceDefinitions.WEBPROPERTY_ORDERNUMBER;
                l_PropertyInfo4PinPn.setValue(cIntakeorder.currentIntakeOrder.getOrderNumberStr());
                l_PropertyInfoObl.add(l_PropertyInfo4PinPn);

                PropertyInfo l_PropertyInfo5Pin = new PropertyInfo();
                l_PropertyInfo5Pin.name = cWebserviceDefinitions.WEBPROPERTY_HANDLEDTIMESTAMP;
                l_PropertyInfo5Pin.setValue((cDateAndTime.pGetCurrentDateTimeForWebserviceStr()));
                l_PropertyInfoObl.add(l_PropertyInfo5Pin);

                PropertyInfo l_PropertyInfo6Pin = new PropertyInfo();
                l_PropertyInfo6Pin.name = cWebserviceDefinitions.WEBPROPERTY_ITEMNO;
                l_PropertyInfo6Pin.setValue(cIntakeorderBarcode.currentIntakeOrderBarcode.getItemNoStr());
                l_PropertyInfoObl.add(l_PropertyInfo6Pin);

                PropertyInfo l_PropertyInfo7Pin = new PropertyInfo();
                l_PropertyInfo7Pin.name = cWebserviceDefinitions.WEBPROPERTY_VARIANTCODE;
                l_PropertyInfo7Pin.setValue(cIntakeorderBarcode.currentIntakeOrderBarcode.getVariantCodeStr());
                l_PropertyInfoObl.add(l_PropertyInfo7Pin);

                PropertyInfo l_PropertyInfo8Pin = new PropertyInfo();
                l_PropertyInfo8Pin.name = cWebserviceDefinitions.WEBPROPERTY_BINCODE;
                l_PropertyInfo8Pin.setValue(params[0].binCodeStr);
                l_PropertyInfoObl.add(l_PropertyInfo8Pin);

                SoapObject barcodesHandledList = new SoapObject(cWebservice.WEBSERVICE_NAMESPACE, cWebserviceDefinitions.WEBPROPERTY_BARCODESLIST);

                if (cIntakeorder.currentIntakeOrder.getOrderTypeStr().equalsIgnoreCase("MAT")) {
                    //Only loop through handled barcodes, of there are any
                    if (params[0].barcodeObl != null) {
                        for (cIntakeorderBarcode intakeorderBarcode: params[0].barcodeObl ) {
                            SoapObject soapObject = new SoapObject(cWebservice.WEBSERVICE_NAMESPACE, cWebserviceDefinitions.WEBPROPERTY_BARCODEHANDLED_COMPLEX);
                            soapObject.addProperty(cWebserviceDefinitions.WEBPROPERTY_BARCODE_COMPLEX, intakeorderBarcode.getBarcodeStr());
                            soapObject.addProperty(cWebserviceDefinitions.WEBPROPERTY_QUANTITYHANDLED_COMPLEX, intakeorderBarcode.getQuantityHandledDbl());
                            barcodesHandledList.addSoapObject(soapObject);
                        }
                    }
                }

                if (cIntakeorder.currentIntakeOrder.getOrderTypeStr().equalsIgnoreCase("MAS")) {
                    //Only loop through handled barcodes, of there are any
                    if (params[0].barcodeObl != null) {
                        for (cIntakeorderBarcode intakeorderBarcode: params[0].barcodeObl ) {
                            SoapObject soapObject = new SoapObject(cWebservice.WEBSERVICE_NAMESPACE, cWebserviceDefinitions.WEBPROPERTY_BARCODEHANDLED_COMPLEX);
                            soapObject.addProperty(cWebserviceDefinitions.WEBPROPERTY_BARCODE_COMPLEX, intakeorderBarcode.getBarcodeStr());
                            soapObject.addProperty(cWebserviceDefinitions.WEBPROPERTY_QUANTITYHANDLED_COMPLEX, intakeorderBarcode.getQuantityHandledDbl());
                            barcodesHandledList.addSoapObject(soapObject);
                        }
                    }
                }

                PropertyInfo l_PropertyInfo9Pin = new PropertyInfo();
                l_PropertyInfo9Pin.name = cWebserviceDefinitions.WEBPROPERTY_BARCODELIST;
                l_PropertyInfo9Pin.setValue(barcodesHandledList);
                l_PropertyInfoObl.add(l_PropertyInfo9Pin);

                SoapObject propertiesHandledObl = new SoapObject(cWebservice.WEBSERVICE_NAMESPACE, cWebserviceDefinitions.WEBPROPERTY_PROPERTIESHANDLED);
                PropertyInfo l_PropertyInfo10Pin = new PropertyInfo();
                l_PropertyInfo10Pin.name = cWebserviceDefinitions.WEBPROPERTY_PROPERTIESHANDLED;
                l_PropertyInfo10Pin.setValue(propertiesHandledObl);
                l_PropertyInfoObl.add(l_PropertyInfo10Pin);


                webresult = cWebresult.pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_INTAKESTOREITEMHANLED, l_PropertyInfoObl);

            } catch (JSONException e) {
                webresult.setSuccessBln(false);
                webresult.setResultBln(false);
            }
            return webresult;
        }
    }

}
