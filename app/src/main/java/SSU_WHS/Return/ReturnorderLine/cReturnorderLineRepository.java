package SSU_WHS.Return.ReturnorderLine;

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
import SSU_WHS.Basics.BranchReason.cBranchReason;
import SSU_WHS.Basics.Users.cUser;
import SSU_WHS.General.acScanSuiteDatabase;
import SSU_WHS.Return.ReturnOrder.cReturnorder;
import SSU_WHS.Return.ReturnorderBarcode.cReturnorderBarcode;
import SSU_WHS.Return.ReturnorderDocument.cReturnorderDocument;
import SSU_WHS.Return.ReturnorderLineBarcode.cReturnorderLineBarcode;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebservice;
import SSU_WHS.Webservice.cWebserviceDefinitions;

public class cReturnorderLineRepository {
    //Region Public Properties
    private iReturnorderLineDao returnorderLineDao;
    //End Region Public Properties

    //Region Private Properties


    private static class UpdateReturnlineQuantityParams {
        Long lineNoLng;
        Double quantityDbl;

        UpdateReturnlineQuantityParams(Long pvLineNoLng, Double pvQuantityDbl) {
            this.lineNoLng = pvLineNoLng;
            this.quantityDbl = pvQuantityDbl;
        }
    }

    //Region Constructor
    cReturnorderLineRepository(Application pvApplication) {
        this.returnorderLineDao = acScanSuiteDatabase.pGetDatabase(pvApplication).returnorderLineDao();
    }
    //End Region Constructor

    //Region Public Methods

    public void insert(cReturnorderLineEntity returnorderLineEntity) {
        new mInsertAsyncTask(returnorderLineDao).execute(returnorderLineEntity);
    }

    public void delete(cReturnorderLineEntity returnorderLineEntity) {
        new mDeleteAsyncTask(returnorderLineDao).execute(returnorderLineEntity);
    }

    public void deleteAll() {
        new mDeleteAllAsyncTask(returnorderLineDao).execute();
    }

    private static class mInsertAsyncTask extends AsyncTask<cReturnorderLineEntity, Void, Void> {
        private iReturnorderLineDao mAsyncTaskDao;

        mInsertAsyncTask(iReturnorderLineDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final cReturnorderLineEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class mDeleteAsyncTask extends AsyncTask<cReturnorderLineEntity, Void, Void> {
        private iReturnorderLineDao mAsyncTaskDao;

        mDeleteAsyncTask(iReturnorderLineDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final cReturnorderLineEntity... params) {
            mAsyncTaskDao.delete(params[0]);
            return null;
        }
    }

    private static class mDeleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private iReturnorderLineDao mAsyncTaskDao;

        mDeleteAllAsyncTask(iReturnorderLineDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Void... params) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }

    public void pCreateItemVariantViaWebservice() {
        List<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();

        try {
            webResultWrs = new mCreateItemVariantViaWebserviceAsyncTask().execute().get();
        } catch (ExecutionException | InterruptedException e) {
            webResultWrs.setResultBln(false);
            webResultWrs.setSuccessBln(false);
            resultObl.add(e.getLocalizedMessage());
            webResultWrs.setResultObl(resultObl);
            e.printStackTrace();
        }
    }


    public cWebresult pSaveLineViaWebserviceWrs() {
        List<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();

        try {
            webResultWrs = new mSaveLineViaWebserviceAsyncTask().execute().get();
        } catch (ExecutionException | InterruptedException e) {
            webResultWrs.setResultBln(false);
            webResultWrs.setSuccessBln(false);
            resultObl.add(e.getLocalizedMessage());
            webResultWrs.setResultObl(resultObl);
            e.printStackTrace();
        }
        return webResultWrs;
    }

    public cWebresult pResetLineViaWebserviceWrs(Long pvLineNoLng) {
        List<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();

        try {
            webResultWrs = new mResetLineViaViaWebserviceAsyncTask().execute(pvLineNoLng).get();
        } catch (ExecutionException | InterruptedException e) {
            webResultWrs.setResultBln(false);
            webResultWrs.setSuccessBln(false);
            resultObl.add(e.getLocalizedMessage());
            webResultWrs.setResultObl(resultObl);
            e.printStackTrace();
        }
        return webResultWrs;
    }

    public void pUpdateQuantity() {


        UpdateReturnlineQuantityParams updateInventorylineQuantityParams = new UpdateReturnlineQuantityParams((long) cReturnorderLine.currentReturnOrderLine.getLineNoInt(),
                                                                                                                     cReturnorderLine.currentReturnOrderLine.getQuantityHandledTakeDbl());


        try {
           new mUpdateQuantityHandledAsyncTask(returnorderLineDao).execute(updateInventorylineQuantityParams).get();

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private static class mSaveLineViaWebserviceAsyncTask extends AsyncTask<Void, Void, cWebresult> {
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
                l_PropertyInfo4Pin.setValue(cReturnorder.currentReturnOrder.getOrderNumberStr());
                l_PropertyInfoObl.add(l_PropertyInfo4Pin);

                PropertyInfo l_PropertyInfo5Pin = new PropertyInfo();
                l_PropertyInfo5Pin.name = cWebserviceDefinitions.WEBPROPERTY_HANDLEDTIMESTAMP;
                l_PropertyInfo5Pin.setValue(cDateAndTime.pGetCurrentDateTimeForWebserviceStr());
                l_PropertyInfoObl.add(l_PropertyInfo5Pin);

                PropertyInfo l_PropertyInfo6Pin = new PropertyInfo();
                l_PropertyInfo6Pin.name = cWebserviceDefinitions.WEBPROPERTY_ITEMNO;
                l_PropertyInfo6Pin.setValue(cReturnorderLine.currentReturnOrderLine.getItemNoStr());
                l_PropertyInfoObl.add(l_PropertyInfo6Pin);

                PropertyInfo l_PropertyInfo7Pin = new PropertyInfo();
                l_PropertyInfo7Pin.name = cWebserviceDefinitions.WEBPROPERTY_VARIANTCODE;
                l_PropertyInfo7Pin.setValue(cReturnorderLine.currentReturnOrderLine.getVariantCodeStr());
                l_PropertyInfoObl.add(l_PropertyInfo7Pin);

                PropertyInfo l_PropertyInfo8Pin = new PropertyInfo();
                l_PropertyInfo8Pin.name = cWebserviceDefinitions.WEBPROPERTY_DOCUMENT;
                l_PropertyInfo8Pin.setValue(cReturnorderDocument.currentReturnOrderDocument.getSourceDocumentStr());
                l_PropertyInfoObl.add(l_PropertyInfo8Pin);

                PropertyInfo l_PropertyInfo9Pin = new PropertyInfo();
                l_PropertyInfo9Pin.name = cWebserviceDefinitions.WEBPROPERTY_RETURNREASONNL;
                l_PropertyInfo9Pin.setValue(cReturnorderLine.currentReturnOrderLine.getRetourRedenStr());
                l_PropertyInfoObl.add(l_PropertyInfo9Pin);

                SoapObject barcodesHandledList = new SoapObject(cWebservice.WEBSERVICE_NAMESPACE, cWebserviceDefinitions.WEBPROPERTY_BARCODESLIST);
                if (cReturnorderLine.currentReturnOrderLine.lineBarcodesObl != null){
                    for (cReturnorderLineBarcode returnorderLineBarcode : cReturnorderLine.currentReturnOrderLine.lineBarcodesObl){
                        SoapObject soapObject = new SoapObject(cWebservice.WEBSERVICE_NAMESPACE, cWebserviceDefinitions.WEBPROPERTY_BARCODEHANDLED_COMPLEX);
                        soapObject.addProperty(cWebserviceDefinitions.WEBPROPERTY_BARCODE_COMPLEX, returnorderLineBarcode.getBarcodeStr());
                        soapObject.addProperty(cWebserviceDefinitions.WEBPROPERTY_QUANTITYHANDLED_COMPLEX, returnorderLineBarcode.getQuantityhandledDbl());
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

                webresult = cWebresult.pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_RETURNLINESAVE, l_PropertyInfoObl);

            } catch (JSONException e) {
                webresult.setSuccessBln(false);
                webresult.setResultBln(false);
            }
            return webresult;
        }
    }

    private static class mCreateItemVariantViaWebserviceAsyncTask extends AsyncTask<Void, Void, cWebresult> {
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
                l_PropertyInfo3Pin.setValue(cReturnorder.currentReturnOrder.getOrderNumberStr());
                l_PropertyInfoObl.add(l_PropertyInfo3Pin);

                PropertyInfo l_PropertyInfo4Pin = new PropertyInfo();
                l_PropertyInfo4Pin.name = cWebserviceDefinitions.WEBPROPERTY_ITEMNO;
                l_PropertyInfo4Pin.setValue(cReturnorderBarcode.currentReturnOrderBarcode.getItemNoStr());
                l_PropertyInfoObl.add(l_PropertyInfo4Pin);

                PropertyInfo l_PropertyInfo5Pin = new PropertyInfo();
                l_PropertyInfo5Pin.name = cWebserviceDefinitions.WEBPROPERTY_VARIANTCODETINY;
                l_PropertyInfo5Pin.setValue(cReturnorderBarcode.currentReturnOrderBarcode.getVariantCodeStr());
                l_PropertyInfoObl.add(l_PropertyInfo5Pin);

                PropertyInfo l_PropertyInfo6Pin = new PropertyInfo();
                l_PropertyInfo6Pin.name = cWebserviceDefinitions.WEBPROPERTY_BARCODE;
                l_PropertyInfo6Pin.setValue(cReturnorderBarcode.currentReturnOrderBarcode.getBarcodeStr());
                l_PropertyInfoObl.add(l_PropertyInfo6Pin);

                PropertyInfo l_PropertyInfo7Pin = new PropertyInfo();
                l_PropertyInfo7Pin.name = cWebserviceDefinitions.WEBPROPERTY_DOCUMENT;
                l_PropertyInfo7Pin.setValue(cReturnorderDocument.currentReturnOrderDocument.getSourceDocumentStr());
                l_PropertyInfoObl.add(l_PropertyInfo7Pin);

                PropertyInfo l_PropertyInfo8Pin = new PropertyInfo();
                l_PropertyInfo8Pin.name = cWebserviceDefinitions.WEBPROPERTY_RETURNREASONBIGNL;
                l_PropertyInfo8Pin.setValue(cBranchReason.currentBranchReason.getReasonStr());
                l_PropertyInfoObl.add(l_PropertyInfo8Pin);

                webresult = cWebresult.pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_RETURNCREATEITEMVARIANT, l_PropertyInfoObl);

            } catch (JSONException e) {
                webresult.setSuccessBln(false);
                webresult.setResultBln(false);
            }
            return webresult;
        }
    }

    private static class mResetLineViaViaWebserviceAsyncTask extends AsyncTask<Long, Void, cWebresult> {
        @Override
        protected cWebresult doInBackground(Long... params) {
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
                l_PropertyInfo3Pin.setValue(cReturnorder.currentReturnOrder.getOrderNumberStr());
                l_PropertyInfoObl.add(l_PropertyInfo3Pin);

                PropertyInfo l_PropertyInfo4Pin = new PropertyInfo();
                l_PropertyInfo4Pin.name = cWebserviceDefinitions.WEBPROPERTY_LINENO;
                l_PropertyInfo4Pin.setValue(params[0]);
                l_PropertyInfoObl.add(l_PropertyInfo4Pin);

                webresult = cWebresult.pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_RETURNLINERESET, l_PropertyInfoObl);

            } catch (JSONException e) {
                webresult.setSuccessBln(false);
                webresult.setResultBln(false);
            }
            return webresult;
        }
    }

    private static class mUpdateQuantityHandledAsyncTask extends AsyncTask<UpdateReturnlineQuantityParams, Void, Integer> {
        private iReturnorderLineDao mAsyncTaskDao;
        mUpdateQuantityHandledAsyncTask(iReturnorderLineDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected Integer doInBackground(UpdateReturnlineQuantityParams... params) {
            return mAsyncTaskDao.updateOrderLineQuantity(params[0].lineNoLng, params[0].quantityDbl);
        }
    }
}

