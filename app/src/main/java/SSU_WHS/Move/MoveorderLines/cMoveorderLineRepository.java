package SSU_WHS.Move.MoveorderLines;

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
import SSU_WHS.Basics.Users.cUser;
import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.General.acScanSuiteDatabase;
import SSU_WHS.Move.MoveOrders.cMoveorder;
import SSU_WHS.Move.MoveorderBarcodes.cMoveorderBarcode;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebservice;
import SSU_WHS.Webservice.cWebserviceDefinitions;

public class cMoveorderLineRepository {

    //Region Public Properties
    private iMoveorderLineDao moveorderLineDao;
    //End Region Public Properties

    //Region Constructor
    cMoveorderLineRepository(Application pvApplication) {
        //Region Private Properties
        acScanSuiteDatabase db = acScanSuiteDatabase.pGetDatabase(pvApplication);
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

    public cWebresult pMoveItemTakeHandledViaWebserviceWrs(List<cMoveorderBarcode> pvBarcodesObl) {

        List<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();

        try {
            webResultWrs = new mMoveTakeItemHandledViaWebserviceAsyncTask().execute(pvBarcodesObl).get();
        } catch (ExecutionException | InterruptedException e) {
            webResultWrs.setResultBln(false);
            webResultWrs.setSuccessBln(false);
            resultObl.add(e.getLocalizedMessage());
            webResultWrs.setResultObl(resultObl);
            e.printStackTrace();
        }
        return webResultWrs;
    }

    public cWebresult pMoveItemTakeMTHandledViaWebserviceWrs(List<cMoveorderBarcode> pvBarcodesObl) {

        List<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();

        try {
            webResultWrs = new mMoveTakeItemMTHandledViaWebserviceAsyncTask().execute(pvBarcodesObl).get();
        } catch (ExecutionException | InterruptedException e) {
            webResultWrs.setResultBln(false);
            webResultWrs.setSuccessBln(false);
            resultObl.add(e.getLocalizedMessage());
            webResultWrs.setResultObl(resultObl);
            e.printStackTrace();
        }
        return webResultWrs;
    }

    public cWebresult pMoveItemPlaceHandledViaWebserviceWrs(List<cMoveorderBarcode> pvBarcodesObl) {

        List<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();

        try {
            webResultWrs = new mMovePlaceItemHandledViaWebserviceAsyncTask().execute(pvBarcodesObl).get();
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

    //Region Private Methods

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

    public cWebresult pAddUnkownBarcodeViaWebserviceWrs(cBarcodeScan pvBarcodeScan) {
        List<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();

        try {
            webResultWrs = new mMoveorderUnknownBarcodeViaWebserviceAsyncTask().execute(pvBarcodeScan).get();
        } catch (ExecutionException | InterruptedException e) {
            webResultWrs.setResultBln(false);
            webResultWrs.setSuccessBln(false);
            resultObl.add(e.getLocalizedMessage());
            webResultWrs.setResultObl(resultObl);
            e.printStackTrace();
        }
        return webResultWrs;
    }

    public cWebresult pAddERPBarcodeViaWebserviceWrs(cBarcodeScan pvBarcodeScan) {
        List<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();

        try {
            webResultWrs = new mMoveorderERPBarcodeViaWebserviceAsyncTask().execute(pvBarcodeScan).get();
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
            webResultWrs = new mResetLineViaWebserviceAsyncTask().execute().get();
        } catch (ExecutionException | InterruptedException e) {
            webResultWrs.setResultBln(false);
            webResultWrs.setSuccessBln(false);
            resultObl.add(e.getLocalizedMessage());
            webResultWrs.setResultObl(resultObl);
            e.printStackTrace();
        }
        return webResultWrs;
    }


    private static class mMoveTakeItemHandledViaWebserviceAsyncTask extends AsyncTask<List<cMoveorderBarcode>, Void, cWebresult> {
        @SafeVarargs
        @Override
        protected final cWebresult doInBackground(List<cMoveorderBarcode>... params) {
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
                l_PropertyInfo4Pin.setValue(cMoveorder.currentMoveOrder.getOrderNumberStr());
                l_PropertyInfoObl.add(l_PropertyInfo4Pin);

                PropertyInfo l_PropertyInfo5Pin = new PropertyInfo();
                l_PropertyInfo5Pin.name = cWebserviceDefinitions.WEBPROPERTY_ITEMNO;
                l_PropertyInfo5Pin.setValue(cMoveorder.currentMoveOrder.currentMoveorderBarcode.getItemNoStr());
                l_PropertyInfoObl.add(l_PropertyInfo5Pin);

                PropertyInfo l_PropertyInfo6Pin = new PropertyInfo();
                l_PropertyInfo6Pin.name = cWebserviceDefinitions.WEBPROPERTY_VARIANTCODE;
                l_PropertyInfo6Pin.setValue(cMoveorder.currentMoveOrder.currentMoveorderBarcode.getVariantCodeStr());
                l_PropertyInfoObl.add(l_PropertyInfo6Pin);

                PropertyInfo l_PropertyInfo7Pin = new PropertyInfo();
                l_PropertyInfo7Pin.name = cWebserviceDefinitions.WEBPROPERTY_BINCODE;
                l_PropertyInfo7Pin.setValue(cMoveorder.currentMoveOrder.currentBranchBin.getBinCodeStr());
                l_PropertyInfoObl.add(l_PropertyInfo7Pin);

                PropertyInfo l_PropertyInfo8Pin = new PropertyInfo();
                l_PropertyInfo8Pin.name = cWebserviceDefinitions.WEBPROPERTY_ACTIONTYPECODE_CAMELCASE;
                l_PropertyInfo8Pin.setValue(cWarehouseorder.ActionTypeEnu.TAKE.toString().toUpperCase());
                l_PropertyInfoObl.add(l_PropertyInfo8Pin);

                PropertyInfo l_PropertyInfo9Pin = new PropertyInfo();
                l_PropertyInfo9Pin.name = cWebserviceDefinitions.WEBPROPERTY_HANDLEDTIMESTAMP;
                l_PropertyInfo9Pin.setValue(cDateAndTime.pGetCurrentDateTimeForWebserviceStr());
                l_PropertyInfoObl.add(l_PropertyInfo9Pin);

                SoapObject barcodesHandledList = new SoapObject(cWebservice.WEBSERVICE_NAMESPACE, cWebserviceDefinitions.WEBPROPERTY_BARCODESLIST);

                //Only loop through handled barcodes, if there are any
                if (params != null) {
                    for (cMoveorderBarcode moveorderBarcode: params[0]) {

                        SoapObject soapObject = new SoapObject(cWebservice.WEBSERVICE_NAMESPACE, cWebserviceDefinitions.WEBPROPERTY_BARCODEHANDLED_COMPLEX);
                        soapObject.addProperty(cWebserviceDefinitions.WEBPROPERTY_BARCODE_COMPLEX, moveorderBarcode.getBarcodeStr());
                        soapObject.addProperty(cWebserviceDefinitions.WEBPROPERTY_QUANTITYHANDLED_COMPLEX, moveorderBarcode.getQuantityHandled());
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

                webresult = cWebresult.pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_MOVEITEM_HANDLED, l_PropertyInfoObl);

            } catch (JSONException e) {
                webresult.setSuccessBln(false);
                webresult.setResultBln(false);
            }
            return webresult;
        }
    }

    private static class mMovePlaceItemHandledViaWebserviceAsyncTask extends AsyncTask<List<cMoveorderBarcode>, Void, cWebresult> {
        @SafeVarargs
        @Override
        protected final cWebresult doInBackground(List<cMoveorderBarcode>... params) {
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
                l_PropertyInfo4Pin.setValue(cMoveorder.currentMoveOrder.getOrderNumberStr());
                l_PropertyInfoObl.add(l_PropertyInfo4Pin);

                PropertyInfo l_PropertyInfo5Pin = new PropertyInfo();
                l_PropertyInfo5Pin.name = cWebserviceDefinitions.WEBPROPERTY_ITEMNO;
                l_PropertyInfo5Pin.setValue(cMoveorder.currentMoveOrder.currentMoveorderBarcode.getItemNoStr());
                l_PropertyInfoObl.add(l_PropertyInfo5Pin);

                PropertyInfo l_PropertyInfo6Pin = new PropertyInfo();
                l_PropertyInfo6Pin.name = cWebserviceDefinitions.WEBPROPERTY_VARIANTCODE;
                l_PropertyInfo6Pin.setValue(cMoveorder.currentMoveOrder.currentMoveorderBarcode.getVariantCodeStr());
                l_PropertyInfoObl.add(l_PropertyInfo6Pin);

                PropertyInfo l_PropertyInfo7Pin = new PropertyInfo();
                l_PropertyInfo7Pin.name = cWebserviceDefinitions.WEBPROPERTY_BINCODE;
                l_PropertyInfo7Pin.setValue(cMoveorder.currentMoveOrder.currentBranchBin.getBinCodeStr());
                l_PropertyInfoObl.add(l_PropertyInfo7Pin);

                PropertyInfo l_PropertyInfo8Pin = new PropertyInfo();
                l_PropertyInfo8Pin.name = cWebserviceDefinitions.WEBPROPERTY_ACTIONTYPECODE_CAMELCASE;
                l_PropertyInfo8Pin.setValue(cWarehouseorder.ActionTypeEnu.PLACE.toString().toUpperCase());
                l_PropertyInfoObl.add(l_PropertyInfo8Pin);

                PropertyInfo l_PropertyInfo9Pin = new PropertyInfo();
                l_PropertyInfo9Pin.name = cWebserviceDefinitions.WEBPROPERTY_HANDLEDTIMESTAMP;
                l_PropertyInfo9Pin.setValue(cDateAndTime.pGetCurrentDateTimeForWebserviceStr());
                l_PropertyInfoObl.add(l_PropertyInfo9Pin);

                SoapObject barcodesHandledList = new SoapObject(cWebservice.WEBSERVICE_NAMESPACE, cWebserviceDefinitions.WEBPROPERTY_BARCODESLIST);

                //Only loop through handled barcodes, if there are any
                if (params != null) {
                    for (cMoveorderBarcode moveorderBarcode: params[0]) {
                        SoapObject soapObject = new SoapObject(cWebservice.WEBSERVICE_NAMESPACE, cWebserviceDefinitions.WEBPROPERTY_BARCODEHANDLED_COMPLEX);
                        soapObject.addProperty(cWebserviceDefinitions.WEBPROPERTY_BARCODE_COMPLEX, moveorderBarcode.getBarcodeStr());
                        soapObject.addProperty(cWebserviceDefinitions.WEBPROPERTY_QUANTITYHANDLED_COMPLEX, moveorderBarcode.getQuantityHandled());
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

                webresult = cWebresult.pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_MOVEITEM_HANDLED, l_PropertyInfoObl);

            } catch (JSONException e) {
                webresult.setSuccessBln(false);
                webresult.setResultBln(false);
            }
            return webresult;
        }
    }

    private static class mMoveTakeItemMTHandledViaWebserviceAsyncTask extends AsyncTask<List<cMoveorderBarcode>, Void, cWebresult> {
        @SafeVarargs
        @Override
        protected final cWebresult doInBackground(List<cMoveorderBarcode>... params) {
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
                l_PropertyInfo4Pin.setValue(cMoveorder.currentMoveOrder.getOrderNumberStr());
                l_PropertyInfoObl.add(l_PropertyInfo4Pin);

                PropertyInfo l_PropertyInfo5Pin = new PropertyInfo();
                l_PropertyInfo5Pin.name = cWebserviceDefinitions.WEBPROPERTY_LINENO;
                l_PropertyInfo5Pin.setValue(cMoveorderLine.currentMoveOrderLine.getLineNoInt());
                l_PropertyInfoObl.add(l_PropertyInfo5Pin);

                PropertyInfo l_PropertyInfo6Pin = new PropertyInfo();
                l_PropertyInfo6Pin.name = cWebserviceDefinitions.WEBPROPERTY_HANDLEDTIMESTAMP;
                l_PropertyInfo6Pin.setValue(cDateAndTime.pGetCurrentDateTimeForWebserviceStr());
                l_PropertyInfoObl.add(l_PropertyInfo6Pin);

                PropertyInfo l_PropertyInfo7Pin = new PropertyInfo();
                l_PropertyInfo7Pin.name = cWebserviceDefinitions.WEBPROPERTY_CONTAINER;
                l_PropertyInfo7Pin.setValue("");
                l_PropertyInfoObl.add(l_PropertyInfo7Pin);


                SoapObject barcodesHandledList = new SoapObject(cWebservice.WEBSERVICE_NAMESPACE, cWebserviceDefinitions.WEBPROPERTY_BARCODESLIST);

                //Only loop through handled barcodes, if there are any
                if (params != null) {
                    for (cMoveorderBarcode moveorderBarcode: params[0]) {

                        SoapObject soapObject = new SoapObject(cWebservice.WEBSERVICE_NAMESPACE, cWebserviceDefinitions.WEBPROPERTY_BARCODEHANDLED_COMPLEX);
                        soapObject.addProperty(cWebserviceDefinitions.WEBPROPERTY_BARCODE_COMPLEX, moveorderBarcode.getBarcodeStr());
                        soapObject.addProperty(cWebserviceDefinitions.WEBPROPERTY_QUANTITYHANDLED_COMPLEX, moveorderBarcode.getQuantityHandled());
                        barcodesHandledList.addSoapObject(soapObject);
                    }
                }

                PropertyInfo l_PropertyInfo8Pin = new PropertyInfo();
                l_PropertyInfo8Pin.name = cWebserviceDefinitions.WEBPROPERTY_BARCODELIST;
                l_PropertyInfo8Pin.setValue(barcodesHandledList);
                l_PropertyInfoObl.add(l_PropertyInfo8Pin);

                SoapObject propertiesHandledList = new SoapObject(cWebservice.WEBSERVICE_NAMESPACE, cWebserviceDefinitions.WEBPROPERTY_PROPERTIESHANDLEDLIST);

                PropertyInfo l_PropertyInfo9Pin = new PropertyInfo();
                l_PropertyInfo9Pin.name = cWebserviceDefinitions.WEBPROPERTY_PROPERTIESHANDLEDLIST;
                l_PropertyInfo9Pin.setValue(propertiesHandledList);
                l_PropertyInfoObl.add(l_PropertyInfo9Pin);

                webresult = cWebresult.pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_MOVEORDERLINE_HANDLEDTAKEMT, l_PropertyInfoObl);

            } catch (JSONException e) {
                webresult.setSuccessBln(false);
                webresult.setResultBln(false);
            }
            return webresult;
        }
    }

    private static class mMoveorderUnknownBarcodeViaWebserviceAsyncTask extends AsyncTask<cBarcodeScan, Void, cWebresult> {
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
                l_PropertyInfo3Pin.setValue(cMoveorder.currentMoveOrder.getOrderNumberStr());
                l_PropertyInfoObl.add(l_PropertyInfo3Pin);

                PropertyInfo l_PropertyInfo4Pin = new PropertyInfo();
                l_PropertyInfo4Pin.name = cWebserviceDefinitions.WEBPROPERTY_ITEMNO;
                l_PropertyInfo4Pin.setValue("UNKNOWN");
                l_PropertyInfoObl.add(l_PropertyInfo4Pin);

                PropertyInfo l_PropertyInfo5Pin = new PropertyInfo();
                l_PropertyInfo5Pin.name = cWebserviceDefinitions.WEBPROPERTY_VARIANTCODETINY;
                l_PropertyInfo5Pin.setValue("");
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

                webresult =  cWebresult.pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_MOVEBARCODECREATE, l_PropertyInfoObl);

            } catch (JSONException e) {
                webresult.setSuccessBln(false);
                webresult.setResultBln(false);
            }
            return webresult;
        }
    }

    private static class mMoveorderERPBarcodeViaWebserviceAsyncTask extends AsyncTask<cBarcodeScan, Void, cWebresult> {
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
                l_PropertyInfo3Pin.setValue(cMoveorder.currentMoveOrder.getOrderNumberStr());
                l_PropertyInfoObl.add(l_PropertyInfo3Pin);

                PropertyInfo l_PropertyInfo4Pin = new PropertyInfo();
                l_PropertyInfo4Pin.name = cWebserviceDefinitions.WEBPROPERTY_ITEMNO;
                l_PropertyInfo4Pin.setValue(cMoveorder.currentMoveOrder.currentArticle.getItemNoStr());
                l_PropertyInfoObl.add(l_PropertyInfo4Pin);

                PropertyInfo l_PropertyInfo5Pin = new PropertyInfo();
                l_PropertyInfo5Pin.name = cWebserviceDefinitions.WEBPROPERTY_VARIANTCODETINY;
                l_PropertyInfo5Pin.setValue(cMoveorder.currentMoveOrder.currentArticle.getVariantCodeStr());
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

                webresult =  cWebresult.pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_MOVEBARCODECREATE, l_PropertyInfoObl);

            } catch (JSONException e) {
                webresult.setSuccessBln(false);
                webresult.setResultBln(false);
            }
            return webresult;
        }
    }

    private static class mResetLineViaWebserviceAsyncTask extends AsyncTask<cBarcodeScan, Void, cWebresult> {
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
                l_PropertyInfo3Pin.setValue(cMoveorder.currentMoveOrder.getOrderNumberStr());
                l_PropertyInfoObl.add(l_PropertyInfo3Pin);

                PropertyInfo l_PropertyInfo4Pin = new PropertyInfo();
                l_PropertyInfo4Pin.name = cWebserviceDefinitions.WEBPROPERTY_LINENO;
                l_PropertyInfo4Pin.setValue(cMoveorderLine.currentMoveOrderLine.getLineNoInt());
                l_PropertyInfoObl.add(l_PropertyInfo4Pin);

                webresult =  cWebresult.pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_MOVELINERESET, l_PropertyInfoObl);

            } catch (JSONException e) {
                webresult.setSuccessBln(false);
                webresult.setResultBln(false);
            }
            return webresult;
        }
    }

    //End Region Private Methods


}

