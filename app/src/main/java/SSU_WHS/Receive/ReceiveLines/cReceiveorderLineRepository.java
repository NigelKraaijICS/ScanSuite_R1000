package SSU_WHS.Receive.ReceiveLines;

import android.app.Application;
import android.os.AsyncTask;

import org.json.JSONException;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cDateAndTime;
import ICS.Utils.cDeviceInfo;
import ICS.Utils.cText;
import SSU_WHS.Basics.Article.cArticle;
import SSU_WHS.Basics.Users.cUser;
import SSU_WHS.General.acScanSuiteDatabase;
import SSU_WHS.Intake.IntakeorderBarcodes.cIntakeorderBarcode;
import SSU_WHS.Intake.Intakeorders.cIntakeorder;
import SSU_WHS.Receive.ReceiveSummaryLine.cReceiveorderSummaryLine;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebservice;
import SSU_WHS.Webservice.cWebserviceDefinitions;

public class cReceiveorderLineRepository {
    //Region Public Properties

    //End Region Public Properties

    //Region Private Properties
    private iReceiveorderLineDao iReceiveorderLineDao;

    private static class UpdateOrderlineLocalStatusParams {
        Integer lineNoInt;
        Integer newStatusInt;

        UpdateOrderlineLocalStatusParams(Integer pvLineNoInt, Integer pvNewsStatusInt) {
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

    //Region Constructor
    cReceiveorderLineRepository(Application pvApplication) {
        acScanSuiteDatabase db = acScanSuiteDatabase.pGetDatabase(pvApplication);
        this.iReceiveorderLineDao = db.receiveorderLineDao();
    }
    //End Region Constructor

    //Region Public Methods


    public void insert (cReceiveorderLineEntity intakeorderMATLineEntity) {
        new mInsertAsyncTask(iReceiveorderLineDao).execute(intakeorderMATLineEntity);
    }

    public void deleteAll () {
        new mDeleteAllAsyncTask(iReceiveorderLineDao).execute();
    }

    public boolean pUpdateLocalStatusBln(Integer pvNewStatusInt) {

        Integer integerValue;

        UpdateOrderlineLocalStatusParams updateOrderlineLocaStatusParams = new UpdateOrderlineLocalStatusParams(cReceiveorderLine.currentReceiveorderLine.getLineNoInt(), pvNewStatusInt);
        try {
            integerValue = new updateOrderLineLocalStatusAsyncTask(iReceiveorderLineDao).execute(updateOrderlineLocaStatusParams).get();
            return integerValue != 0;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return  false;
        }
    }

    public boolean pUpdateQuantityHandledBln(Double pvQuantityDbl) {

        Integer integerValue;
        UpdateOrderlineQuantityParams updateOrderlineQuantityParams = new UpdateOrderlineQuantityParams(cReceiveorderLine.currentReceiveorderLine.getLineNoInt(), pvQuantityDbl);

        try {
            integerValue = new updateOrderLineQuantityHandledAsyncTask(iReceiveorderLineDao).execute(updateOrderlineQuantityParams).get();

            return integerValue != 0;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return  false;
        }

    }

    public cWebresult pResetReceiveLineViaWebserviceWrs() {

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

    public cWebresult pReceiveLineHandledViaWebserviceWrs(List<cIntakeorderBarcode> pvBarcodeObl) {
        List<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();

        try {
            webResultWrs = new mReceiveLineHandledViaWebserviceAsyncTask().execute(pvBarcodeObl).get();
        } catch (ExecutionException | InterruptedException e) {
            webResultWrs.setResultBln(false);
            webResultWrs.setSuccessBln(false);
            resultObl.add(e.getLocalizedMessage());
            webResultWrs.setResultObl(resultObl);
            e.printStackTrace();
        }
        return webResultWrs;
    }

    public cWebresult pAddUnkownItemViaWebserviceWrs(cBarcodeScan pvBarcodeScan) {
        List<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();

        try {
            webResultWrs = new mIntakeorderUnknownItemAddViaWebserviceAsyncTask().execute(pvBarcodeScan).get();
        } catch (ExecutionException | InterruptedException e) {
            webResultWrs.setResultBln(false);
            webResultWrs.setSuccessBln(false);
            resultObl.add(e.getLocalizedMessage());
            webResultWrs.setResultObl(resultObl);
            e.printStackTrace();
        }
        return webResultWrs;
    }

    public cWebresult pAddERPItemViaWebserviceWrs(cBarcodeScan pvBarcodeScan) {
        List<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();

        try {
            webResultWrs = new mIntakeorderERPItemAddViaWebserviceAsyncTask().execute(pvBarcodeScan).get();
        } catch (ExecutionException | InterruptedException e) {
            webResultWrs.setResultBln(false);
            webResultWrs.setSuccessBln(false);
            resultObl.add(e.getLocalizedMessage());
            webResultWrs.setResultObl(resultObl);
            e.printStackTrace();
        }
        return webResultWrs;
    }



    public cWebresult pAddUnkownBarcodeViaWebserviceWrs(cBarcodeScan pvBarcodeScan) {
        List<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();

        try {
            webResultWrs = new mIntakeorderUnknownBarcodeViaWebserviceAsyncTask().execute(pvBarcodeScan).get();
        } catch (ExecutionException | InterruptedException e) {
            webResultWrs.setResultBln(false);
            webResultWrs.setSuccessBln(false);
            resultObl.add(e.getLocalizedMessage());
            webResultWrs.setResultObl(resultObl);
            e.printStackTrace();
        }
        return webResultWrs;
    }

    private static class mInsertAsyncTask extends AsyncTask<cReceiveorderLineEntity, Void, Void> {
        private iReceiveorderLineDao mAsyncTaskDao;

        mInsertAsyncTask(iReceiveorderLineDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final cReceiveorderLineEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class mDeleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private iReceiveorderLineDao mAsyncTaskDao;

        mDeleteAllAsyncTask(iReceiveorderLineDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final Void... params) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }

    private static class updateOrderLineLocalStatusAsyncTask extends AsyncTask<UpdateOrderlineLocalStatusParams, Void, Integer> {
        private iReceiveorderLineDao mAsyncTaskDao;
        updateOrderLineLocalStatusAsyncTask(iReceiveorderLineDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected Integer doInBackground(UpdateOrderlineLocalStatusParams... params) {
            return mAsyncTaskDao.updateOrderLineLocalStatus(params[0].lineNoInt, params[0].newStatusInt);
        }
    }

    private static class updateOrderLineQuantityHandledAsyncTask extends AsyncTask<UpdateOrderlineQuantityParams, Void, Integer> {
        private iReceiveorderLineDao mAsyncTaskDao;
        updateOrderLineQuantityHandledAsyncTask(iReceiveorderLineDao dao) { mAsyncTaskDao = dao; }
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
                l_PropertyInfo4Pin.setValue(cReceiveorderLine.currentReceiveorderLine.getLineNoInt());
                l_PropertyInfoObl.add(l_PropertyInfo4Pin);

                l_WebresultWrs =  cWebresult.pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_RECEIVELINRESET, l_PropertyInfoObl);
            } catch (JSONException e) {
                l_WebresultWrs.setSuccessBln(false);
                l_WebresultWrs.setResultBln(false);
            }
            return l_WebresultWrs;
        }
    }


    private static class mIntakeorderUnknownItemAddViaWebserviceAsyncTask extends AsyncTask<cBarcodeScan, Void, cWebresult> {
        @Override
        protected cWebresult doInBackground(cBarcodeScan... params) {
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
                l_PropertyInfo3Pin.setValue(cIntakeorder.currentIntakeOrder.getOrderNumberStr());
                l_PropertyInfoObl.add(l_PropertyInfo3Pin);

                PropertyInfo l_PropertyInfo4Pin = new PropertyInfo();
                l_PropertyInfo4Pin.name = cWebserviceDefinitions.WEBPROPERTY_ITEMNO;
                l_PropertyInfo4Pin.setValue("UNKNOWN");
                l_PropertyInfoObl.add(l_PropertyInfo4Pin);

                PropertyInfo l_PropertyInfo5Pin = new PropertyInfo();
                l_PropertyInfo5Pin.name = cWebserviceDefinitions.WEBPROPERTY_VARIANTCODETINY;

                String testStr = cText.rPadStr(cText.pIntToStringStr(cIntakeorder.currentIntakeOrder.getUnknownVariantCounterInt()),"0",4, cText.ePadLocation.Left );

                l_PropertyInfo5Pin.setValue(testStr);
                l_PropertyInfoObl.add(l_PropertyInfo5Pin);

                PropertyInfo l_PropertyInfo6Pin = new PropertyInfo();
                l_PropertyInfo6Pin.name = cWebserviceDefinitions.WEBPROPERTY_BARCODE;
                l_PropertyInfo6Pin.setValue(params[0].getBarcodeOriginalStr());
                l_PropertyInfoObl.add(l_PropertyInfo6Pin);


                new cWebresult();
                webresult = cWebresult.pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_RECEIVEITEMVARIANTCREATE, l_PropertyInfoObl);

            } catch (JSONException e) {
                webresult.setSuccessBln(false);
                webresult.setResultBln(false);
            }
            return webresult;
        }
    }


    private static class mIntakeorderERPItemAddViaWebserviceAsyncTask extends AsyncTask<cBarcodeScan, Void, cWebresult> {
        @Override
        protected cWebresult doInBackground(cBarcodeScan... params) {
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
                l_PropertyInfo3Pin.setValue(cIntakeorder.currentIntakeOrder.getOrderNumberStr());
                l_PropertyInfoObl.add(l_PropertyInfo3Pin);

                PropertyInfo l_PropertyInfo4Pin = new PropertyInfo();
                l_PropertyInfo4Pin.name = cWebserviceDefinitions.WEBPROPERTY_ITEMNO;
                l_PropertyInfo4Pin.setValue(cArticle.currentArticle.getItemNoStr());
                l_PropertyInfoObl.add(l_PropertyInfo4Pin);

                PropertyInfo l_PropertyInfo5Pin = new PropertyInfo();
                l_PropertyInfo5Pin.name = cWebserviceDefinitions.WEBPROPERTY_VARIANTCODETINY;
                l_PropertyInfo5Pin.setValue(cArticle.currentArticle.getVariantCodeStr());
                l_PropertyInfoObl.add(l_PropertyInfo5Pin);

                PropertyInfo l_PropertyInfo6Pin = new PropertyInfo();
                l_PropertyInfo6Pin.name = cWebserviceDefinitions.WEBPROPERTY_BARCODE;
                l_PropertyInfo6Pin.setValue(params[0].getBarcodeOriginalStr());
                l_PropertyInfoObl.add(l_PropertyInfo6Pin);


                new cWebresult();
                webresult = cWebresult.pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_RECEIVEITEMVARIANTCREATE, l_PropertyInfoObl);

            } catch (JSONException e) {
                webresult.setSuccessBln(false);
                webresult.setResultBln(false);
            }
            return webresult;
        }
    }

    private static class mIntakeorderUnknownBarcodeViaWebserviceAsyncTask extends AsyncTask<cBarcodeScan, Void, cWebresult> {
        @Override
        protected cWebresult doInBackground(cBarcodeScan... params) {
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
                l_PropertyInfo3Pin.setValue(cIntakeorder.currentIntakeOrder.getOrderNumberStr());
                l_PropertyInfoObl.add(l_PropertyInfo3Pin);

                PropertyInfo l_PropertyInfo4Pin = new PropertyInfo();
                l_PropertyInfo4Pin.name = cWebserviceDefinitions.WEBPROPERTY_ITEMNO;
                l_PropertyInfo4Pin.setValue(cReceiveorderSummaryLine.currentReceiveorderSummaryLine.getItemNoStr());
                l_PropertyInfoObl.add(l_PropertyInfo4Pin);

                PropertyInfo l_PropertyInfo5Pin = new PropertyInfo();
                l_PropertyInfo5Pin.name = cWebserviceDefinitions.WEBPROPERTY_VARIANTCODETINY;

                if (!cReceiveorderSummaryLine.currentReceiveorderSummaryLine.getItemNoStr().equalsIgnoreCase("UNKNOWN")) {
                    l_PropertyInfo5Pin.setValue(cReceiveorderSummaryLine.currentReceiveorderSummaryLine.getVariantCodeStr());
                }
                else {
                    l_PropertyInfo5Pin.setValue("");
                }

                l_PropertyInfoObl.add(l_PropertyInfo5Pin);

                PropertyInfo l_PropertyInfo6Pin = new PropertyInfo();
                l_PropertyInfo6Pin.name = cWebserviceDefinitions.WEBPROPERTY_BARCODE;
                l_PropertyInfo6Pin.setValue(params[0].getBarcodeOriginalStr());
                l_PropertyInfoObl.add(l_PropertyInfo6Pin);

                PropertyInfo l_PropertyInfo7Pin = new PropertyInfo();
                l_PropertyInfo7Pin.name = cWebserviceDefinitions.WEBPROPERTY_BARCODETYPE;
                l_PropertyInfo7Pin.setValue(cBarcodeScan.BarcodeType.EAN13);
                l_PropertyInfoObl.add(l_PropertyInfo7Pin);

                PropertyInfo l_PropertyInfo8Pin = new PropertyInfo();
                l_PropertyInfo8Pin.name = cWebserviceDefinitions.WEBPROPERTY_ISUNIQUEBARCODE;
                l_PropertyInfo8Pin.setValue(false);
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

                webresult =  cWebresult.pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_RECEIVEBARCODECREATE, l_PropertyInfoObl);

            } catch (JSONException e) {
                webresult.setSuccessBln(false);
                webresult.setResultBln(false);
            }
            return webresult;
        }
    }

    private static class mReceiveLineHandledViaWebserviceAsyncTask extends AsyncTask<List<cIntakeorderBarcode>, Void, cWebresult> {
        @SafeVarargs
        @Override
        protected final cWebresult doInBackground(List<cIntakeorderBarcode>... params) {
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
                l_PropertyInfo5Pin.name = cWebserviceDefinitions.WEBPROPERTY_HANDLEDTIMESTAMP;
                l_PropertyInfo5Pin.setValue((cDateAndTime.pGetCurrentDateTimeForWebserviceStr()));
                l_PropertyInfoObl.add(l_PropertyInfo5Pin);

                PropertyInfo l_PropertyInfo6Pin = new PropertyInfo();
                l_PropertyInfo6Pin.name = cWebserviceDefinitions.WEBPROPERTY_ITEMNO;
                l_PropertyInfo6Pin.setValue(cReceiveorderSummaryLine.currentReceiveorderSummaryLine.getItemNoStr());
                l_PropertyInfoObl.add(l_PropertyInfo6Pin);

                PropertyInfo l_PropertyInfo7Pin = new PropertyInfo();
                l_PropertyInfo7Pin.name = cWebserviceDefinitions.WEBPROPERTY_VARIANTCODE;
                l_PropertyInfo7Pin.setValue(cReceiveorderSummaryLine.currentReceiveorderSummaryLine.getVariantCodeStr());
                l_PropertyInfoObl.add(l_PropertyInfo7Pin);

                SoapObject barcodesHandledList = new SoapObject(cWebservice.WEBSERVICE_NAMESPACE, cWebserviceDefinitions.WEBPROPERTY_BARCODESLIST);

                //Only loop through handled barcodes, of there are any
                if (params != null) {
                    for (cIntakeorderBarcode intakeorderBarcode: params[0]) {
                        SoapObject soapBarcodeObject = new SoapObject(cWebservice.WEBSERVICE_NAMESPACE, cWebserviceDefinitions.WEBPROPERTY_BARCODEHANDLED_COMPLEX);
                        soapBarcodeObject.addProperty(cWebserviceDefinitions.WEBPROPERTY_BARCODE_COMPLEX, intakeorderBarcode.getBarcodeStr());
                        soapBarcodeObject.addProperty(cWebserviceDefinitions.WEBPROPERTY_QUANTITYHANDLED_COMPLEX, intakeorderBarcode.getQuantityHandledDbl());
                        barcodesHandledList.addSoapObject(soapBarcodeObject);
                    }
                }

                PropertyInfo l_PropertyInfo8Pin = new PropertyInfo();
                l_PropertyInfo8Pin.name = cWebserviceDefinitions.WEBPROPERTY_BARCODELIST;
                l_PropertyInfo8Pin.setValue(barcodesHandledList);
                l_PropertyInfoObl.add(l_PropertyInfo8Pin);

                SoapObject propertiesHandledObl = new SoapObject(cWebservice.WEBSERVICE_NAMESPACE, cWebserviceDefinitions.WEBPROPERTY_PROPERTIESHANDLED);
                PropertyInfo l_PropertyInfo9Pin = new PropertyInfo();
                l_PropertyInfo9Pin.name = cWebserviceDefinitions.WEBPROPERTY_PROPERTIESHANDLED;
                l_PropertyInfo9Pin.setValue(propertiesHandledObl);
                l_PropertyInfoObl.add(l_PropertyInfo9Pin);


                webresult = cWebresult.pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_RECEIVEINTAKEITEM, l_PropertyInfoObl);

            } catch (JSONException e) {
                webresult.setSuccessBln(false);
                webresult.setResultBln(false);
            }
            return webresult;
        }
    }


}
