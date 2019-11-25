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
import SSU_WHS.Intake.IntakeorderMATLineBarcodes.cIntakeorderMATLineBarcode;
import SSU_WHS.Intake.Intakeorders.cIntakeorder;
import SSU_WHS.Picken.PickorderLines.cPickorderLine;
import SSU_WHS.Picken.Pickorders.cPickorder;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebservice;
import SSU_WHS.Webservice.cWebserviceDefinitions;

public class cIntakeorderMATLineRepository {
    //Region Public Properties
    public iIntakeorderMATLineDao intakeorderMATLineDao;

    //End Region Public Properties

    //Region Private Properties
    private acScanSuiteDatabase db;

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

    private static class UpdateOrderlineBinCodeHandledParams {
        Integer lineNoInt;
        String binCodeStr;

        UpdateOrderlineBinCodeHandledParams(Integer pvLineNoInt, String pvBinCodeStr) {
            this.lineNoInt = pvLineNoInt;
            this.binCodeStr = pvBinCodeStr;
        }
    }


    //Region Constructor
    cIntakeorderMATLineRepository(Application pvApplication) {
        this.db = acScanSuiteDatabase.getDatabase(pvApplication);
        this.intakeorderMATLineDao = db.intakeorderMATLineDao();
    }
    //End Region Constructor

    //Region Public Methods



    public void insert (cIntakeorderMATLineEntity intakeorderMATLineEntity) {
        new mInsertAsyncTask(intakeorderMATLineDao).execute(intakeorderMATLineEntity);
    }

    public List<cIntakeorderMATLineEntity> pGetIntakeorderMATLinesFromDatabaseObl() {
        List<cIntakeorderMATLineEntity> ResultObl = null;
        try {
            ResultObl = new mGetIntakeorderMATLinesFromDatabaseAsyncTask(intakeorderMATLineDao).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return ResultObl;
    }

    public void deleteAll () {
        new mDeleteAllAsyncTask(intakeorderMATLineDao).execute();
    }

    public boolean pUpdateLocalStatusBln(Integer pvNewStatusInt) {

        Integer integerValue;
        UpdateOrderlineLocaStatusParams updateOrderlineLocaStatusParams = new UpdateOrderlineLocaStatusParams(cIntakeorderMATLine.currentIntakeorderMATLine.getLineNoInt(), pvNewStatusInt);
        try {
            integerValue = new updateOrderLineLocalStatusAsyncTask(intakeorderMATLineDao).execute(updateOrderlineLocaStatusParams).get();
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

    public boolean pUpdateQuantityHandledBln(Double pvQuantityDbl) {

        Integer integerValue;
        UpdateOrderlineQuantityParams updateOrderlineQuantityParams = new UpdateOrderlineQuantityParams(cIntakeorderMATLine.currentIntakeorderMATLine.getLineNoInt(), pvQuantityDbl);

        try {
            integerValue = new updateOrderLineQuantityAsyncTask(intakeorderMATLineDao).execute(updateOrderlineQuantityParams).get();

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

    public boolean pUpdateBinCodeHandledBln(String pvBinCodeStr) {

        Integer integerValue;
        UpdateOrderlineBinCodeHandledParams updateOrderlineBinCodeHandledParams = new UpdateOrderlineBinCodeHandledParams(cIntakeorderMATLine.currentIntakeorderMATLine.getLineNoInt(), pvBinCodeStr);

        try {
            integerValue = new updateOrderLineBinCodeHandledyAsyncTask(intakeorderMATLineDao).execute(updateOrderlineBinCodeHandledParams).get();

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

    public cWebresult pLineHandledViaWebserviceWrs() {
        List<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();

        try {
            webResultWrs = new mLineHandledViaWebserviceAsyncTask().execute().get();
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

    public cWebresult pLineSplitViaWebserviceWrs() {
        List<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();

        try {
            webResultWrs = new mLineSplitViaWebserviceAsyncTask().execute().get();
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

    private static class mGetIntakeorderMATLinesFromDatabaseAsyncTask extends AsyncTask<Void, Void, List<cIntakeorderMATLineEntity>> {
        private iIntakeorderMATLineDao mAsyncTaskDao;

        mGetIntakeorderMATLinesFromDatabaseAsyncTask(iIntakeorderMATLineDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected List<cIntakeorderMATLineEntity> doInBackground(final Void... params) {
            return mAsyncTaskDao.getIntakeorderMATLinesFromDatabase();
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

    private static class updateOrderLineQuantityAsyncTask extends AsyncTask<UpdateOrderlineQuantityParams, Void, Integer> {
        private iIntakeorderMATLineDao mAsyncTaskDao;
        updateOrderLineQuantityAsyncTask(iIntakeorderMATLineDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected Integer doInBackground(UpdateOrderlineQuantityParams... params) {
            return mAsyncTaskDao.updateOrderLineQuantity(params[0].lineNoInt, params[0].quantityDbl);
        }
    }

    private static class updateOrderLineBinCodeHandledyAsyncTask extends AsyncTask<UpdateOrderlineBinCodeHandledParams, Void, Integer> {
        private iIntakeorderMATLineDao mAsyncTaskDao;
        updateOrderLineBinCodeHandledyAsyncTask(iIntakeorderMATLineDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected Integer doInBackground(UpdateOrderlineBinCodeHandledParams... params) {
            return mAsyncTaskDao.updateOrderLineBinCodeHandled(params[0].lineNoInt, params[0].binCodeStr);
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
                l_PropertyInfo1Pin.setValue(cUser.currentUser.getNameStr());
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
                l_PropertyInfo4Pin.name = cWebserviceDefinitions.WEBPROPERTY_LINENOTAKE;
                l_PropertyInfo4Pin.setValue(cIntakeorderMATLine.currentIntakeorderMATLine.getLineNoInt());
                l_PropertyInfoObl.add(l_PropertyInfo4Pin);

                l_WebresultWrs = new cWebresult().pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_INTAKELINEMATRESET, l_PropertyInfoObl);
            } catch (JSONException e) {
                l_WebresultWrs.setSuccessBln(false);
                l_WebresultWrs.setResultBln(false);
            }
            return l_WebresultWrs;
        }
    }

    private static class mLineHandledViaWebserviceAsyncTask extends AsyncTask<Void, Void, cWebresult> {
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
                PropertyInfo l_PropertyInfo9Pin = new PropertyInfo();
                l_PropertyInfo9Pin.name = cWebserviceDefinitions.WEBPROPERTY_PROPERTIESHANDLED;
                l_PropertyInfo9Pin.setValue(propertiesHandledObl);
                l_PropertyInfoObl.add(l_PropertyInfo9Pin);


                webresult = new cWebresult().pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_INTAKELINEMATHANDLED, l_PropertyInfoObl);

            } catch (JSONException e) {
                webresult.setSuccessBln(false);
                webresult.setResultBln(false);
            }
            return webresult;
        }
    }

    private static class mLineSplitViaWebserviceAsyncTask extends AsyncTask<Void, Void, cWebresult> {
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
                PropertyInfo l_PropertyInfo9Pin = new PropertyInfo();
                l_PropertyInfo9Pin.name = cWebserviceDefinitions.WEBPROPERTY_PROPERTIESHANDLED;
                l_PropertyInfo9Pin.setValue(propertiesHandledObl);
                l_PropertyInfoObl.add(l_PropertyInfo9Pin);


                webresult = new cWebresult().pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_INTAKELINEMATHANDLEDPART, l_PropertyInfoObl);

            } catch (JSONException e) {
                webresult.setSuccessBln(false);
                webresult.setResultBln(false);
            }
            return webresult;
        }
    }

}
