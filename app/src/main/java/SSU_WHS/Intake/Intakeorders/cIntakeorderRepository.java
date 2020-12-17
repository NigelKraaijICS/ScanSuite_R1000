package SSU_WHS.Intake.Intakeorders;

import android.app.Application;
import android.os.AsyncTask;

import androidx.sqlite.db.SimpleSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteQuery;

import org.json.JSONException;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import ICS.Utils.cDateAndTime;
import ICS.Utils.cDeviceInfo;
import ICS.Utils.cSharedPreferences;
import ICS.Utils.cText;
import SSU_WHS.Basics.Packaging.cPackaging;
import SSU_WHS.Basics.Settings.cSetting;
import SSU_WHS.Basics.Users.cUser;
import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.General.acScanSuiteDatabase;
import SSU_WHS.General.cDatabase;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebservice;
import SSU_WHS.Webservice.cWebserviceDefinitions;
import nl.icsvertex.scansuite.Activities.IntakeAndReceive.IntakeAndReceiveSelectActivity;

import static ICS.Utils.cText.pAddSingleQuotesStr;

public class cIntakeorderRepository {

    //Region Public Properties
    private iIntakeorderDao intakeorderDao;

    //End Region Public Properties

    //Region Private Properties

    private static class IntakeCreateParams {

        String documentStr;
        boolean checkBarcodesBln;

        IntakeCreateParams(String pvDocumentStr,boolean pvCheckBarcodesBln ) {
            this.documentStr = pvDocumentStr;
            this.checkBarcodesBln = pvCheckBarcodesBln;
        }
    }

    private static class ReceiveCreateParams {
        String documentStr;
        String packingSlipStr;
        String binCodeStr;
        boolean checkBarcodesBln;

        ReceiveCreateParams(String pvDocumentStr, String pvPackingSlipStr, String pvBinCodeStr, boolean pvCheckBarcodesBln ) {
            this.documentStr = pvDocumentStr;
            this.packingSlipStr = pvPackingSlipStr;
            this.binCodeStr = pvBinCodeStr;
            this.checkBarcodesBln = pvCheckBarcodesBln;
        }
    }

    //Region Constructor
    cIntakeorderRepository(Application pvApplication) {
         acScanSuiteDatabase db = acScanSuiteDatabase.pGetDatabase(pvApplication);
        this.intakeorderDao = db.intakeorderDao();
    }
    //End Region Constructor

    //Region Public Methods

    public cWebresult pGetIntakeordersFromWebserviceWrs(String pvSearchTextStr) {

        List<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();

        try {
            webResultWrs = new mGetIntakeordersFromWebserviceAsyncTask().execute(pvSearchTextStr).get();
        } catch (ExecutionException | InterruptedException e) {
            webResultWrs.setResultBln(false);
            webResultWrs.setSuccessBln(false);
            resultObl.add(e.getLocalizedMessage());
            webResultWrs.setResultObl(resultObl);
            e.printStackTrace();
        }
        return webResultWrs;
    }

    public List<cIntakeorderEntity> pGetIntakeordersFromDatabaseWithFilterObl(String pvCurrentUserStr, Boolean pvUseFiltersBln) {

        List<cIntakeorderEntity> ResultObl = null;
        StringBuilder SQLStatementStr;
        int i;

        SQLStatementStr = new StringBuilder("SELECT * FROM " + cDatabase.TABLENAME_INTAKEORDER);
        if (pvUseFiltersBln) {
//            TTT
            if (cSharedPreferences.showAssignedToMeBln() && cSharedPreferences.showAssignedToOthersBln() && cSharedPreferences.showNotAssignedBln()) {
                SQLStatementStr.append(" WHERE 1=1 ");
            }
//            TTF
            else if (cSharedPreferences.showAssignedToMeBln() && cSharedPreferences.showAssignedToOthersBln() && !cSharedPreferences.showNotAssignedBln()) {
                SQLStatementStr.append(" WHERE AssignedUserId != '' ");
            }
//            TFT
            else if (cSharedPreferences.showAssignedToMeBln() && !cSharedPreferences.showAssignedToOthersBln() && cSharedPreferences.showNotAssignedBln()) {
                SQLStatementStr.append(" WHERE AssignedUserId = ").append(pAddSingleQuotesStr(pvCurrentUserStr.toUpperCase())).append(" OR  AssignedUserId = '' ");
                SQLStatementStr.append(" OR CurrentUserId = ").append(pAddSingleQuotesStr(pvCurrentUserStr.toUpperCase())).append(" OR  CurrentUserId = '' ");
            }
//            FTT
            else if (!cSharedPreferences.showAssignedToMeBln() && cSharedPreferences.showAssignedToOthersBln() && cSharedPreferences.showNotAssignedBln()) {
                SQLStatementStr.append(" WHERE AssignedUserId != ").append(pAddSingleQuotesStr(pvCurrentUserStr.toUpperCase())).append(" ");
            }
//            TFF
            else if (cSharedPreferences.showAssignedToMeBln() && !cSharedPreferences.showAssignedToOthersBln() && !cSharedPreferences.showNotAssignedBln()) {
                SQLStatementStr.append(" WHERE AssignedUserId = ").append(pAddSingleQuotesStr(pvCurrentUserStr.toUpperCase())).append(" ");
                SQLStatementStr.append(" OR CurrentUserId = ").append(pAddSingleQuotesStr(pvCurrentUserStr.toUpperCase())).append(" ");
            }
//            FTF
            else if (!cSharedPreferences.showAssignedToMeBln() && cSharedPreferences.showAssignedToOthersBln() && !cSharedPreferences.showNotAssignedBln()) {
                SQLStatementStr.append(" WHERE AssignedUserId != ").append(pAddSingleQuotesStr(pvCurrentUserStr.toUpperCase())).append(" AND  AssignedUserId != '' ");
            }
//            FFT
            else if (!cSharedPreferences.showAssignedToMeBln() && !cSharedPreferences.showAssignedToOthersBln() && cSharedPreferences.showNotAssignedBln()) {
                SQLStatementStr.append(" WHERE AssignedUserId = '' AND CurrentUserId = ''");
            }
//            FFF
            else if (!cSharedPreferences.showAssignedToMeBln() && !cSharedPreferences.showAssignedToOthersBln() && !cSharedPreferences.showNotAssignedBln()) {
                SQLStatementStr.append(" WHERE AssignedUserId = 'HELEMAALNIEMAND' ");
            }
        }

        if (cUser.currentUser.currentAuthorisation.getCustomAuthorisation() != null) {

            if (!cUser.currentUser.currentAuthorisation.getCustomAuthorisation().getFilterfieldStr().isEmpty()) {
                String[] splitFields =    cUser.currentUser.currentAuthorisation.getCustomAuthorisation().getFilterfieldStr().split("\\|");
                String[] splitValues =    cUser.currentUser.currentAuthorisation.getCustomAuthorisation().getFiltervalueStr().split("\\|");

                if (splitFields.length == splitValues.length) {


                    for (i = 0; i < splitFields.length; i++) {
                        if (!SQLStatementStr.toString().toUpperCase().contains("WHERE")) {
                            SQLStatementStr.append(" WHERE ").append(splitFields[i]).append(" = ").append(cText.pAddSingleQuotesStr(splitValues[i]));
                        }
                        else {
                            SQLStatementStr.append(" AND ").append(splitFields[i]).append(" = ").append(cText.pAddSingleQuotesStr(splitValues[i]));
                        }
                    }
                }
            }
        }

        SQLStatementStr.append(" ORDER BY Priority, Opdrachtnummer ASC");


        try {
            SupportSQLiteQuery query = new SimpleSQLiteQuery(SQLStatementStr.toString());
            ResultObl = new cIntakeorderRepository.mGetIntakeordersFromDatabaseWithFilterAsyncTask(intakeorderDao).execute(query).get();

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return ResultObl;
    }

    public void pInsertInDatabase(cIntakeorderEntity intakeorderEntity) {
        new mInsertAsyncTask(intakeorderDao).execute(intakeorderEntity);
    }

    public void pTruncateTable() {
        new mDeleteAllAsyncTask(intakeorderDao).execute();
    }

    public cWebresult pGetMATLinesFromWebserviceWrs() {

        List<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();

        try {
            webResultWrs = new mGetMATLinesFromWebserviceAsyncTask().execute().get();
        } catch (ExecutionException | InterruptedException e) {
            webResultWrs.setResultBln(false);
            webResultWrs.setSuccessBln(false);
            resultObl.add(e.getLocalizedMessage());
            webResultWrs.setResultObl(resultObl);
            e.printStackTrace();
        }
        return webResultWrs;
    }

    public cWebresult pGetReceiveLinesFromWebserviceWrs(String pvScannerStr) {

        List<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();

        try {
            webResultWrs = new mGetReceiveLinesFromWebserviceAsyncTask().execute(pvScannerStr).get();
        } catch (ExecutionException | InterruptedException e) {
            webResultWrs.setResultBln(false);
            webResultWrs.setSuccessBln(false);
            resultObl.add(e.getLocalizedMessage());
            webResultWrs.setResultObl(resultObl);
            e.printStackTrace();
        }
        return webResultWrs;
    }

    public cWebresult pGetReceiveItemsFromWebserviceWrs() {

        List<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();

        try {
            webResultWrs = new mGetReceiveItemsFromWebserviceAsyncTask().execute().get();
        } catch (ExecutionException | InterruptedException e) {
            webResultWrs.setResultBln(false);
            webResultWrs.setSuccessBln(false);
            resultObl.add(e.getLocalizedMessage());
            webResultWrs.setResultObl(resultObl);
            e.printStackTrace();
        }
        return webResultWrs;
    }

    public cWebresult pGetBarcodesFromWebserviceWrs() {

        List<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();

        try {
            webResultWrs = new mGetBarcodesFromWebserviceAsyncTask().execute().get();
        } catch (ExecutionException | InterruptedException e) {
            webResultWrs.setResultBln(false);
            webResultWrs.setSuccessBln(false);
            resultObl.add(e.getLocalizedMessage());
            webResultWrs.setResultObl(resultObl);
            e.printStackTrace();
        }
        return webResultWrs;
    }



    public cWebresult pGetMATLineBarcodesFromWebserviceWrs() {

        List<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();

        try {
            webResultWrs = new mGetLineBarcodesFromWebserviceAsyncTask().execute().get();
        } catch (ExecutionException | InterruptedException e) {
            webResultWrs.setResultBln(false);
            webResultWrs.setSuccessBln(false);
            resultObl.add(e.getLocalizedMessage());
            webResultWrs.setResultObl(resultObl);
            e.printStackTrace();
        }
        return webResultWrs;
    }

    public cWebresult pGetCommentsFromWebservice(){
        ArrayList<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();

        try {
            webResultWrs = new mGetCommentsFromWebserviceAsyncTask().execute().get();
        } catch (ExecutionException | InterruptedException e) {
            webResultWrs.setResultBln(false);
            webResultWrs.setSuccessBln(false);
            resultObl.add(e.getLocalizedMessage());
            webResultWrs.setResultObl(resultObl);
            e.printStackTrace();
        }
        return webResultWrs;
    }


    public cWebresult pGetPackagingFromWebserviceWrs() {

        List<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();

        try {
            webResultWrs = new mGetPackagingWebserviceAsyncTask().execute().get();
        } catch (ExecutionException | InterruptedException e) {
            webResultWrs.setResultBln(false);
            webResultWrs.setSuccessBln(false);
            resultObl.add(e.getLocalizedMessage());
            webResultWrs.setResultObl(resultObl);
            e.printStackTrace();
        }
        return webResultWrs;
    }



    public cWebresult pMATHandledViaWebserviceBln() {

        cWebresult webResult;
        try {

            webResult = new mIntakeorderStepHandledAsyncTask() .execute().get();
            return  webResult;
        }

        catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return  null;

        }
    }


    public cWebresult pPackagingHandledViaWebserviceBln() {

        cWebresult webResult;
        try {

            webResult = new mPackagingHandledAsyncTask() .execute().get();
            return  webResult;
        }

        catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return  null;

        }
    }

    public cWebresult pReceiveHandledViaWebserviceBln() {

        cWebresult webResult;

        try {

            webResult = new mIntakeorderStepHandledAsyncTask() .execute().get();
            return  webResult;
        }

        catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return  null;

        }
    }

    public cWebresult pInvalidateViaWebserviceBln() {

        cWebresult webResult;


        try {

            webResult = new mIntakeorderInvalidateAsyncTask() .execute().get();
            return  webResult;
        }

        catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return  null;

        }
    }


    public cWebresult pCreateReceiveOrderViaWebserviceWrs(String pvDocumentStr, String pvPackingSlipStr, String pvBinCodeStr, boolean pvCheckBarcodesBln) {

        List<String> resultObl = new ArrayList<>();
        ReceiveCreateParams receiveCreateParams;
        receiveCreateParams = new ReceiveCreateParams(pvDocumentStr, pvPackingSlipStr, pvBinCodeStr, pvCheckBarcodesBln);

        cWebresult webResultWrs = new cWebresult();

        try {
            webResultWrs = new mCreateReceiveOrderViaWebserviceAsyncTask().execute(receiveCreateParams).get();
        } catch (ExecutionException | InterruptedException e) {
            webResultWrs.setResultBln(false);
            webResultWrs.setSuccessBln(false);
            resultObl.add(e.getLocalizedMessage());
            webResultWrs.setResultObl(resultObl);
            e.printStackTrace();
        }
        return webResultWrs;
    }


    public cWebresult pCreateIntakeOrderViaWebserviceWrs(String pvDocumentStr, boolean pvCheckBarcodesBln) {

        List<String> resultObl = new ArrayList<>();
        IntakeCreateParams intakeCreateParams;
        intakeCreateParams = new IntakeCreateParams(pvDocumentStr, pvCheckBarcodesBln);

        cWebresult webResultWrs = new cWebresult();

        try {
            webResultWrs = new mCreateIntakeOrderViaWebserviceAsyncTask().execute(intakeCreateParams).get();
        } catch (ExecutionException | InterruptedException e) {
            webResultWrs.setResultBln(false);
            webResultWrs.setSuccessBln(false);
            resultObl.add(e.getLocalizedMessage());
            webResultWrs.setResultObl(resultObl);
            e.printStackTrace();
        }
        return webResultWrs;
    }

    //Quantity's

    public Double pQuantityHandledDbl() {
        Double resultDbl = 0.0;
        try {
            resultDbl = new mGetQuantityHandledAsyncTask(intakeorderDao).execute().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return resultDbl;
    }

    //End Region Public Methods

    //Region Private Methods

    private static class mGetIntakeordersFromWebserviceAsyncTask extends AsyncTask<String, Void, cWebresult> {
        @Override
        protected cWebresult doInBackground(final String... params) {
            cWebresult WebresultWrs = new cWebresult();

            List<PropertyInfo> l_PropertyInfoObl = new ArrayList<>();
            String maintTypeStr = "";

            PropertyInfo l_PropertyInfo1Pin = new PropertyInfo();
            l_PropertyInfo1Pin.name = cWebserviceDefinitions.WEBPROPERTY_USERNAMEDUNGLISH;
            l_PropertyInfo1Pin.setValue(cUser.currentUser.getUsernameStr());
            l_PropertyInfoObl.add(l_PropertyInfo1Pin);

            PropertyInfo l_PropertyInfo2Pin = new PropertyInfo();
            l_PropertyInfo2Pin.name = cWebserviceDefinitions.WEBPROPERTY_LOCATION_NL;
            l_PropertyInfo2Pin.setValue(cUser.currentUser.currentBranch.getBranchStr());
            l_PropertyInfoObl.add(l_PropertyInfo2Pin);

            PropertyInfo l_PropertyInfo3Pin = new PropertyInfo();
            l_PropertyInfo3Pin.name = cWebserviceDefinitions.WEBPROPERTY_SEARCHTEXT;
            l_PropertyInfo3Pin.setValue(params[0]);
            l_PropertyInfoObl.add(l_PropertyInfo3Pin);

            switch (IntakeAndReceiveSelectActivity.currentMainTypeEnu) {
                case Unknown:
                    maintTypeStr = "";
                    break;

                case Store:
                    maintTypeStr = "MA";
                    break;

                case External:
                    maintTypeStr = "EO";
                    break;

                case Internal:
                    maintTypeStr = "OM";
                    break;
            }

            PropertyInfo l_PropertyInfo4Pin = new PropertyInfo();
            l_PropertyInfo4Pin.name = cWebserviceDefinitions.WEBPROPERTY_MAINTYPE;
            l_PropertyInfo4Pin.setValue(maintTypeStr);
            l_PropertyInfoObl.add(l_PropertyInfo4Pin);

            try {
                WebresultWrs = cWebresult.pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_GETINTAKEORDERS, l_PropertyInfoObl);
            } catch (JSONException e) {
                WebresultWrs.setResultBln(false);
                WebresultWrs.setSuccessBln(false);
                e.printStackTrace();
            }

            return WebresultWrs;
        }
    }

    private static class mGetIntakeordersFromDatabaseWithFilterAsyncTask extends AsyncTask<SupportSQLiteQuery, Void, List<cIntakeorderEntity>> {
        private iIntakeorderDao mAsyncTaskDao;

        mGetIntakeordersFromDatabaseWithFilterAsyncTask(iIntakeorderDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected List<cIntakeorderEntity> doInBackground(final SupportSQLiteQuery... params) {
            return mAsyncTaskDao.getFilteredIntakeOrders(params[0]);
        }
    }

    private static class mInsertAsyncTask extends AsyncTask<cIntakeorderEntity, Void, Void> {
        private iIntakeorderDao mAsyncTaskDao;

        mInsertAsyncTask(iIntakeorderDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final cIntakeorderEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class mDeleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private iIntakeorderDao mAsyncTaskDao;

        mDeleteAllAsyncTask(iIntakeorderDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final Void... params) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }

    private static class mGetMATLinesFromWebserviceAsyncTask extends AsyncTask<String, Void, cWebresult> {
        @Override
        protected cWebresult doInBackground(final String... params) {
            cWebresult WebresultWrs = new cWebresult();

            List<PropertyInfo> l_PropertyInfoObl = new ArrayList<>();

            PropertyInfo l_PropertyInfo1Pin = new PropertyInfo();
            l_PropertyInfo1Pin.name = cWebserviceDefinitions.WEBPROPERTY_LOCATION_NL;
            l_PropertyInfo1Pin.setValue(cUser.currentUser.currentBranch.getBranchStr());
            l_PropertyInfoObl.add(l_PropertyInfo1Pin);

            PropertyInfo l_PropertyInfo2Pin = new PropertyInfo();
            l_PropertyInfo2Pin.name = cWebserviceDefinitions.WEBPROPERTY_ORDERNUMBER;
            l_PropertyInfo2Pin.setValue(cIntakeorder.currentIntakeOrder.getOrderNumberStr());
            l_PropertyInfoObl.add(l_PropertyInfo2Pin);

            try {
                WebresultWrs = cWebresult.pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_GETINTAKEORDERMATLINES, l_PropertyInfoObl);
            } catch (JSONException e) {
                WebresultWrs.setResultBln(false);
                WebresultWrs.setSuccessBln(false);
                e.printStackTrace();
            }

            return WebresultWrs;
        }
    }

    private static class mGetReceiveLinesFromWebserviceAsyncTask extends AsyncTask<String, Void, cWebresult> {
        @Override
        protected cWebresult doInBackground(final String... params) {
            cWebresult WebresultWrs = new cWebresult();

            List<PropertyInfo> l_PropertyInfoObl = new ArrayList<>();

            PropertyInfo l_PropertyInfo1Pin = new PropertyInfo();
            l_PropertyInfo1Pin.name = cWebserviceDefinitions.WEBPROPERTY_LOCATION_NL;
            l_PropertyInfo1Pin.setValue(cUser.currentUser.currentBranch.getBranchStr());
            l_PropertyInfoObl.add(l_PropertyInfo1Pin);

            PropertyInfo l_PropertyInfo2Pin = new PropertyInfo();
            l_PropertyInfo2Pin.name = cWebserviceDefinitions.WEBPROPERTY_ORDERNUMBER;
            l_PropertyInfo2Pin.setValue(cIntakeorder.currentIntakeOrder.getOrderNumberStr());
            l_PropertyInfoObl.add(l_PropertyInfo2Pin);

            PropertyInfo l_PropertyInfo3Pin = new PropertyInfo();
            l_PropertyInfo3Pin.name = cWebserviceDefinitions.WEBPROPERTY_SCANNER;
            l_PropertyInfo3Pin.setValue(params[0]);
            l_PropertyInfoObl.add(l_PropertyInfo3Pin);

            try {
                WebresultWrs = cWebresult.pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_GETINTAKEORDERLINES, l_PropertyInfoObl);
            } catch (JSONException e) {
                WebresultWrs.setResultBln(false);
                WebresultWrs.setSuccessBln(false);
                e.printStackTrace();
            }

            return WebresultWrs;
        }
    }

    private static class mGetReceiveItemsFromWebserviceAsyncTask extends AsyncTask<String, Void, cWebresult> {
        @Override
        protected cWebresult doInBackground(final String... params) {
            cWebresult WebresultWrs = new cWebresult();

            List<PropertyInfo> l_PropertyInfoObl = new ArrayList<>();

            PropertyInfo l_PropertyInfo1Pin = new PropertyInfo();
            l_PropertyInfo1Pin.name = cWebserviceDefinitions.WEBPROPERTY_LOCATION_NL;
            l_PropertyInfo1Pin.setValue(cUser.currentUser.currentBranch.getBranchStr());
            l_PropertyInfoObl.add(l_PropertyInfo1Pin);

            PropertyInfo l_PropertyInfo2Pin = new PropertyInfo();
            l_PropertyInfo2Pin.name = cWebserviceDefinitions.WEBPROPERTY_ORDERNUMBER;
            l_PropertyInfo2Pin.setValue(cIntakeorder.currentIntakeOrder.getOrderNumberStr());
            l_PropertyInfoObl.add(l_PropertyInfo2Pin);

            try {
                new cWebresult();
                WebresultWrs = cWebresult.pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_GETINTAKEORDERITEMS, l_PropertyInfoObl);
            } catch (JSONException e) {
                WebresultWrs.setResultBln(false);
                WebresultWrs.setSuccessBln(false);
                e.printStackTrace();
            }

            return WebresultWrs;
        }
    }

    private static class mGetLineBarcodesFromWebserviceAsyncTask extends AsyncTask<String, Void, cWebresult> {
        @Override
        protected cWebresult doInBackground(final String... params) {
            cWebresult WebresultWrs = new cWebresult();

            List<PropertyInfo> l_PropertyInfoObl = new ArrayList<>();

            PropertyInfo l_PropertyInfo1Pin = new PropertyInfo();
            l_PropertyInfo1Pin.name = cWebserviceDefinitions.WEBPROPERTY_LOCATION_NL;
            l_PropertyInfo1Pin.setValue(cUser.currentUser.currentBranch.getBranchStr());
            l_PropertyInfoObl.add(l_PropertyInfo1Pin);

            PropertyInfo l_PropertyInfo2Pin = new PropertyInfo();
            l_PropertyInfo2Pin.name = cWebserviceDefinitions.WEBPROPERTY_ORDERNUMBER;
            l_PropertyInfo2Pin.setValue(cIntakeorder.currentIntakeOrder.getOrderNumberStr());
            l_PropertyInfoObl.add(l_PropertyInfo2Pin);

            try {
                WebresultWrs = cWebresult.pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_GETINTAKEORDERMATLINEBARCODES, l_PropertyInfoObl);
            } catch (JSONException e) {
                WebresultWrs.setResultBln(false);
                WebresultWrs.setSuccessBln(false);
                e.printStackTrace();
            }

            return WebresultWrs;
        }
    }

    private static class mGetBarcodesFromWebserviceAsyncTask extends AsyncTask<String, Void, cWebresult> {
        @Override
        protected cWebresult doInBackground(final String... params) {
            cWebresult WebresultWrs = new cWebresult();

            List<PropertyInfo> l_PropertyInfoObl = new ArrayList<>();

            PropertyInfo l_PropertyInfo1Pin = new PropertyInfo();
            l_PropertyInfo1Pin.name = cWebserviceDefinitions.WEBPROPERTY_LOCATION_NL;
            l_PropertyInfo1Pin.setValue(cUser.currentUser.currentBranch.getBranchStr());
            l_PropertyInfoObl.add(l_PropertyInfo1Pin);

            PropertyInfo l_PropertyInfo2Pin = new PropertyInfo();
            l_PropertyInfo2Pin.name = cWebserviceDefinitions.WEBPROPERTY_ORDERNUMBER;
            l_PropertyInfo2Pin.setValue(cIntakeorder.currentIntakeOrder.getOrderNumberStr());
            l_PropertyInfoObl.add(l_PropertyInfo2Pin);

            try {

                WebresultWrs = cWebresult.pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_GETINTAKEARCODES, l_PropertyInfoObl);
            } catch (JSONException e) {
                WebresultWrs.setResultBln(false);
                WebresultWrs.setSuccessBln(false);
                e.printStackTrace();
            }

            return WebresultWrs;
        }
    }

    private static class mGetCommentsFromWebserviceAsyncTask extends AsyncTask <Void, Void, cWebresult> {
        @Override
        protected cWebresult doInBackground(final Void... params){
            cWebresult WebresultWrs = new cWebresult();

            List<PropertyInfo> l_PropertyInfoObl = new ArrayList<>();

            PropertyInfo l_PropertyInfo1Pin = new PropertyInfo();
            l_PropertyInfo1Pin.name = cWebserviceDefinitions.WEBPROPERTY_LOCATION_NL;
            l_PropertyInfo1Pin.setValue(cUser.currentUser.currentBranch.getBranchStr());
            l_PropertyInfoObl.add(l_PropertyInfo1Pin);

            PropertyInfo l_PropertyInfo2Pin = new PropertyInfo();
            l_PropertyInfo2Pin.name = cWebserviceDefinitions.WEBPROPERTY_ORDERNUMBER;
            l_PropertyInfo2Pin.setValue(cIntakeorder.currentIntakeOrder.getOrderNumberStr());
            l_PropertyInfoObl.add(l_PropertyInfo2Pin);

            try{
                WebresultWrs = cWebresult.pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_GETPICKORDERCOMMENTS, l_PropertyInfoObl);
            } catch (JSONException e) {
                WebresultWrs.setResultBln(false);
                WebresultWrs.setSuccessBln(false);
                e.printStackTrace();
            }

            return WebresultWrs;
        }
    }

    private static class mGetPackagingWebserviceAsyncTask extends AsyncTask<String, Void, cWebresult> {
        @Override
        protected cWebresult doInBackground(final String... params) {
            cWebresult WebresultWrs = new cWebresult();

            List<PropertyInfo> l_PropertyInfoObl = new ArrayList<>();

            PropertyInfo l_PropertyInfo1Pin = new PropertyInfo();
            l_PropertyInfo1Pin.name = cWebserviceDefinitions.WEBPROPERTY_ORDERTYPE;
            l_PropertyInfo1Pin.setValue(cWarehouseorder.OrderTypeEnu.ONTVANGST.toString().toUpperCase());
            l_PropertyInfoObl.add(l_PropertyInfo1Pin);

            PropertyInfo l_PropertyInfo2Pin = new PropertyInfo();
            l_PropertyInfo2Pin.name = cWebserviceDefinitions.WEBPROPERTY_LOCATION_NL;
            l_PropertyInfo2Pin.setValue(cUser.currentUser.currentBranch.getBranchStr());
            l_PropertyInfoObl.add(l_PropertyInfo2Pin);

            PropertyInfo l_PropertyInfo3Pin = new PropertyInfo();
            l_PropertyInfo3Pin.name = cWebserviceDefinitions.WEBPROPERTY_ORDERNUMBER;
            l_PropertyInfo3Pin.setValue(cIntakeorder.currentIntakeOrder.getOrderNumberStr());
            l_PropertyInfoObl.add(l_PropertyInfo3Pin);

            try {
                new cWebresult();
                WebresultWrs = cWebresult.pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_PACKAGINGGET, l_PropertyInfoObl);
            } catch (JSONException e) {
                WebresultWrs.setResultBln(false);
                WebresultWrs.setSuccessBln(false);
                e.printStackTrace();
            }

            return WebresultWrs;
        }
    }

    private static class mGetQuantityHandledAsyncTask extends AsyncTask<Void, Void, Double> {
        private iIntakeorderDao mAsyncTaskDao;
        mGetQuantityHandledAsyncTask(iIntakeorderDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected Double doInBackground(Void... params) {
            return mAsyncTaskDao.getNumberHandledDbl();
        }
    }

    private static class mIntakeorderStepHandledAsyncTask extends AsyncTask<Void, Void, cWebresult> {
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
                l_PropertyInfo3Pin.setValue(cIntakeorder.currentIntakeOrder.getOrderNumberStr());
                l_PropertyInfoObl.add(l_PropertyInfo3Pin);

                PropertyInfo l_PropertyInfo4Pin = new PropertyInfo();
                l_PropertyInfo4Pin.name = cWebserviceDefinitions.WEBPROPERTY_SCANNER;
                l_PropertyInfo4Pin.setValue(cDeviceInfo.getSerialnumberStr());
                l_PropertyInfoObl.add(l_PropertyInfo4Pin);

                PropertyInfo l_PropertyInfo5Pin = new PropertyInfo();
                l_PropertyInfo5Pin.name = cWebserviceDefinitions.WEBPROPERTY_WORKFLOWSTEPCODESTR;

                String workflowStepcodeStr = "";

                if (cIntakeorder.currentIntakeOrder.getOrderTypeStr().equalsIgnoreCase("MAT")) {
                    workflowStepcodeStr = cWarehouseorder.StepCodeEnu.Receive_Store.toString().toUpperCase();
                }

                if (cIntakeorder.currentIntakeOrder.getOrderTypeStr().equalsIgnoreCase("EOS")) {
                    workflowStepcodeStr = cWarehouseorder.StepCodeEnu.Receive_InTake.toString().toUpperCase();
                }

                l_PropertyInfo5Pin.setValue(workflowStepcodeStr);
                l_PropertyInfoObl.add(l_PropertyInfo5Pin);

                PropertyInfo l_PropertyInfo6Pin = new PropertyInfo();
                l_PropertyInfo6Pin.name = cWebserviceDefinitions.WEBPROPERTY_CULTURE;
                l_PropertyInfo6Pin.setValue("");
                l_PropertyInfoObl.add(l_PropertyInfo6Pin);

                      webresult = cWebresult.pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_INTAKEHANLED, l_PropertyInfoObl);
            } catch (JSONException e) {
                webresult.setSuccessBln(false);
                webresult.setResultBln(false);
            }
            return webresult;
        }
    }

    private static class mPackagingHandledAsyncTask extends AsyncTask<Void, Void, cWebresult> {
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
                l_PropertyInfo2Pin.setValue(cWarehouseorder.OrderTypeEnu.ONTVANGST.toString().toUpperCase());
                l_PropertyInfoObl.add(l_PropertyInfo2Pin);

                PropertyInfo l_PropertyInfo3Pin = new PropertyInfo();
                l_PropertyInfo3Pin.name = cWebserviceDefinitions.WEBPROPERTY_LOCATION_NL;
                l_PropertyInfo3Pin.setValue(cUser.currentUser.currentBranch.getBranchStr());
                l_PropertyInfoObl.add(l_PropertyInfo3Pin);

                PropertyInfo l_PropertyInfo4Pin = new PropertyInfo();
                l_PropertyInfo4Pin.name = cWebserviceDefinitions.WEBPROPERTY_ORDERNUMBER;
                l_PropertyInfo4Pin.setValue(cIntakeorder.currentIntakeOrder.getOrderNumberStr());
                l_PropertyInfoObl.add(l_PropertyInfo4Pin);

                SoapObject packagingList = new SoapObject(cWebservice.WEBSERVICE_NAMESPACE, cWebserviceDefinitions.WEBPROPERTY_PACKAGINGLIST);

                //Only loop through handled barcodes, of there are any
                if (cIntakeorder.currentIntakeOrder.packagingInObl  != null) {
                    for (cPackaging packaging : cIntakeorder.currentIntakeOrder.packagingInObl ) {
                        SoapObject soapObject = new SoapObject(cWebservice.WEBSERVICE_NAMESPACE, cWebserviceDefinitions.WEBPROPERTY_PACKAGINGHANDLED_COMPLEX);
                        soapObject.addProperty(cWebserviceDefinitions.WEBPROPERTY_PACKAGING_COMPLEX, packaging.getCodeStr());
                        soapObject.addProperty(cWebserviceDefinitions.WEBPROPERTY_QUANTITY_IN_TAKE_COMPLEX,packaging.getQuantityInUsedInt());
                        soapObject.addProperty(cWebserviceDefinitions.WEBPROPERTY_QUANTITY_SHIPPED_COMPLEX, 0);
                        packagingList.addSoapObject(soapObject);
                    }
                }

                if (cIntakeorder.currentIntakeOrder.packagingOutObl  != null) {
                    for (cPackaging packaging : cIntakeorder.currentIntakeOrder.packagingOutObl ) {
                        SoapObject soapObject = new SoapObject(cWebservice.WEBSERVICE_NAMESPACE, cWebserviceDefinitions.WEBPROPERTY_PACKAGINGHANDLED_COMPLEX);
                        soapObject.addProperty(cWebserviceDefinitions.WEBPROPERTY_PACKAGING_COMPLEX, packaging.getCodeStr());
                        soapObject.addProperty(cWebserviceDefinitions.WEBPROPERTY_QUANTITY_IN_TAKE_COMPLEX,0);
                        soapObject.addProperty(cWebserviceDefinitions.WEBPROPERTY_QUANTITY_SHIPPED_COMPLEX, packaging.getQuantityOutUsedInt());
                        packagingList.addSoapObject(soapObject);
                    }
                }

                PropertyInfo l_PropertyInfo5Pin = new PropertyInfo();
                l_PropertyInfo5Pin.name = cWebserviceDefinitions.WEBPROPERTY_PACKAGINGLIST;
                l_PropertyInfo5Pin.setValue(packagingList);
                l_PropertyInfoObl.add(l_PropertyInfo5Pin);


                webresult = cWebresult.pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_PACKAGINGHANDLED, l_PropertyInfoObl);
            } catch (JSONException e) {
                webresult.setSuccessBln(false);
                webresult.setResultBln(false);
            }
            return webresult;
        }
    }

    private static class mIntakeorderInvalidateAsyncTask extends AsyncTask<Void, Void, cWebresult> {
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
                l_PropertyInfo3Pin.setValue(cIntakeorder.currentIntakeOrder.getOrderNumberStr());
                l_PropertyInfoObl.add(l_PropertyInfo3Pin);


                PropertyInfo l_PropertyInfo4Pin = new PropertyInfo();
                l_PropertyInfo4Pin.name = cWebserviceDefinitions.WEBPROPERTY_CULTURE;
                l_PropertyInfo4Pin.setValue("");
                l_PropertyInfoObl.add(l_PropertyInfo4Pin);

                webresult = cWebresult.pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_RECEIVEINVALIDATE, l_PropertyInfoObl);
            } catch (JSONException e) {
                webresult.setSuccessBln(false);
                webresult.setResultBln(false);
            }
            return webresult;
        }
    }

    private static class mCreateIntakeOrderViaWebserviceAsyncTask extends AsyncTask<IntakeCreateParams, Void, cWebresult> {
        @Override
        protected cWebresult doInBackground(IntakeCreateParams... params) {
            cWebresult l_WebresultWrs = new cWebresult();
            try {
                List<PropertyInfo> l_PropertyInfoObl = new ArrayList<>();

                PropertyInfo l_PropertyInfo1Pin = new PropertyInfo();
                l_PropertyInfo1Pin.name = cWebserviceDefinitions.WEBPROPERTY_USERNAMEDUNGLISH;
                l_PropertyInfo1Pin.setValue(cUser.currentUser.getUsernameStr());
                l_PropertyInfoObl.add(l_PropertyInfo1Pin);

                PropertyInfo l_PropertyInfo2Pin = new PropertyInfo();
                l_PropertyInfo2Pin.name = cWebserviceDefinitions.WEBPROPERTY_LANGUAGEASCULTURE;
                l_PropertyInfo2Pin.setValue("");
                l_PropertyInfoObl.add(l_PropertyInfo2Pin);

                PropertyInfo l_PropertyInfo3Pin = new PropertyInfo();
                l_PropertyInfo3Pin.name = cWebserviceDefinitions.WEBPROPERTY_LOCATION_NL;
                l_PropertyInfo3Pin.setValue(cUser.currentUser.currentBranch.getBranchStr());
                l_PropertyInfoObl.add(l_PropertyInfo3Pin);

                PropertyInfo l_PropertyInfo4Pin = new PropertyInfo();
                l_PropertyInfo4Pin.name = cWebserviceDefinitions.WEBPROPERTY_STOCKOWNER;
                l_PropertyInfo4Pin.setValue("");
                l_PropertyInfoObl.add(l_PropertyInfo4Pin);

                PropertyInfo l_PropertyInfo5Pin = new PropertyInfo();
                l_PropertyInfo5Pin.name = cWebserviceDefinitions.WEBPROPERTY_WORKFLOW;
                l_PropertyInfo5Pin.setValue("MAS");
                l_PropertyInfoObl.add(l_PropertyInfo5Pin);

                PropertyInfo l_PropertyInfo6Pin = new PropertyInfo();
                l_PropertyInfo6Pin.name = cWebserviceDefinitions.WEBPROPERTY_RECEIVEDDAT;
                l_PropertyInfo6Pin.setValue(cDateAndTime.pGetCurrentDateTimeForWebserviceStr());
                l_PropertyInfoObl.add(l_PropertyInfo6Pin);

                PropertyInfo l_PropertyInfo7Pin = new PropertyInfo();
                l_PropertyInfo7Pin.name = cWebserviceDefinitions.WEBPROPERTY_DOCUMENT;
                l_PropertyInfo7Pin.setValue(params[0].documentStr);
                l_PropertyInfoObl.add(l_PropertyInfo7Pin);

                PropertyInfo l_PropertyInfo8Pin = new PropertyInfo();
                l_PropertyInfo8Pin.name = cWebserviceDefinitions.WEBPROPERTY_DOCUMENT2;
                l_PropertyInfo8Pin.setValue("");
                l_PropertyInfoObl.add(l_PropertyInfo8Pin);

                PropertyInfo l_PropertyInfo9Pin = new PropertyInfo();
                l_PropertyInfo9Pin.name = cWebserviceDefinitions.WEBPROPERTY_ORIGINNO;
                l_PropertyInfo9Pin.setValue("");
                l_PropertyInfoObl.add(l_PropertyInfo9Pin);

                PropertyInfo l_PropertyInfo10Pin = new PropertyInfo();
                l_PropertyInfo10Pin.name = cWebserviceDefinitions.WEBPROPERTY_EXTERNALREFERENCE;
                l_PropertyInfo10Pin.setValue(params[0].documentStr + ";" + cUser.currentUser.currentBranch.getReceiveDefaultBinStr() + ";" + cDateAndTime.pGetCurrentDateTimeForIntakeStr() );
                l_PropertyInfoObl.add(l_PropertyInfo10Pin);

                PropertyInfo l_PropertyInfo11Pin = new PropertyInfo();
                l_PropertyInfo11Pin.name = cWebserviceDefinitions.WEBPROPERTY_RECEIVEBIN;
                l_PropertyInfo11Pin.setValue(cUser.currentUser.currentBranch.getReceiveDefaultBinStr());
                l_PropertyInfoObl.add(l_PropertyInfo11Pin);

                PropertyInfo l_PropertyInfo12Pin = new PropertyInfo();
                l_PropertyInfo12Pin.name = cWebserviceDefinitions.WEBPROPERTY_RECEIVEBARCODECHECK;
                l_PropertyInfo12Pin.setValue(params[0].checkBarcodesBln);
                l_PropertyInfoObl.add(l_PropertyInfo12Pin);

                PropertyInfo l_PropertyInfo13Pin = new PropertyInfo();
                l_PropertyInfo13Pin.name = cWebserviceDefinitions.WEBPROPERTY_ADMINISTRATION;
                l_PropertyInfo13Pin.setValue("");
                l_PropertyInfoObl.add(l_PropertyInfo13Pin);

                l_WebresultWrs = cWebresult.pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_RECEIVECREATE, l_PropertyInfoObl);
            } catch (JSONException e) {
                l_WebresultWrs.setSuccessBln(false);
                l_WebresultWrs.setResultBln(false);
            }
            return l_WebresultWrs;
        }
    }

    private static class mCreateReceiveOrderViaWebserviceAsyncTask extends AsyncTask<ReceiveCreateParams, Void, cWebresult> {
        @Override
        protected cWebresult doInBackground(ReceiveCreateParams... params) {
            cWebresult l_WebresultWrs = new cWebresult();
            try {
                List<PropertyInfo> l_PropertyInfoObl = new ArrayList<>();

                PropertyInfo l_PropertyInfo1Pin = new PropertyInfo();
                l_PropertyInfo1Pin.name = cWebserviceDefinitions.WEBPROPERTY_USERNAMEDUNGLISH;
                l_PropertyInfo1Pin.setValue(cUser.currentUser.getUsernameStr());
                l_PropertyInfoObl.add(l_PropertyInfo1Pin);

                PropertyInfo l_PropertyInfo2Pin = new PropertyInfo();
                l_PropertyInfo2Pin.name = cWebserviceDefinitions.WEBPROPERTY_LANGUAGEASCULTURE;
                l_PropertyInfo2Pin.setValue("");
                l_PropertyInfoObl.add(l_PropertyInfo2Pin);

                PropertyInfo l_PropertyInfo3Pin = new PropertyInfo();
                l_PropertyInfo3Pin.name = cWebserviceDefinitions.WEBPROPERTY_LOCATION_NL;
                l_PropertyInfo3Pin.setValue(cUser.currentUser.currentBranch.getBranchStr());
                l_PropertyInfoObl.add(l_PropertyInfo3Pin);

                PropertyInfo l_PropertyInfo4Pin = new PropertyInfo();
                l_PropertyInfo4Pin.name = cWebserviceDefinitions.WEBPROPERTY_STOCKOWNER;
                l_PropertyInfo4Pin.setValue("");
                l_PropertyInfoObl.add(l_PropertyInfo4Pin);


                String newWorkflowsStr = cSetting.RECEIVE_NEW_WORKFLOWS().get(0).toUpperCase();
                newWorkflowsStr = newWorkflowsStr.replace(";;",";");

                if (newWorkflowsStr.endsWith(";")) {
                    newWorkflowsStr = newWorkflowsStr.substring(0, newWorkflowsStr.length() - 1);
                }

                PropertyInfo l_PropertyInfo5Pin = new PropertyInfo();
                l_PropertyInfo5Pin.name = cWebserviceDefinitions.WEBPROPERTY_WORKFLOW;
                l_PropertyInfo5Pin.setValue(newWorkflowsStr);
                l_PropertyInfoObl.add(l_PropertyInfo5Pin);

                PropertyInfo l_PropertyInfo6Pin = new PropertyInfo();
                l_PropertyInfo6Pin.name = cWebserviceDefinitions.WEBPROPERTY_RECEIVEDDAT;
                l_PropertyInfo6Pin.setValue(cDateAndTime.pGetCurrentDateTimeForWebserviceStr());
                l_PropertyInfoObl.add(l_PropertyInfo6Pin);

                PropertyInfo l_PropertyInfo7Pin = new PropertyInfo();
                l_PropertyInfo7Pin.name = cWebserviceDefinitions.WEBPROPERTY_DOCUMENT;
                l_PropertyInfo7Pin.setValue(params[0].documentStr);
                l_PropertyInfoObl.add(l_PropertyInfo7Pin);

                PropertyInfo l_PropertyInfo8Pin = new PropertyInfo();
                l_PropertyInfo8Pin.name = cWebserviceDefinitions.WEBPROPERTY_DOCUMENT2;
                l_PropertyInfo8Pin.setValue(params[0].packingSlipStr);
                l_PropertyInfoObl.add(l_PropertyInfo8Pin);

                PropertyInfo l_PropertyInfo9Pin = new PropertyInfo();
                l_PropertyInfo9Pin.name = cWebserviceDefinitions.WEBPROPERTY_ORIGINNO;
                l_PropertyInfo9Pin.setValue("");
                l_PropertyInfoObl.add(l_PropertyInfo9Pin);

                PropertyInfo l_PropertyInfo10Pin = new PropertyInfo();
                l_PropertyInfo10Pin.name = cWebserviceDefinitions.WEBPROPERTY_EXTERNALREFERENCE;
                l_PropertyInfo10Pin.setValue(params[0].documentStr + ";" + params[0].packingSlipStr + ";" + cDateAndTime.pGetCurrentDateTimeForIntakeStr() );
                l_PropertyInfoObl.add(l_PropertyInfo10Pin);

                PropertyInfo l_PropertyInfo11Pin = new PropertyInfo();
                l_PropertyInfo11Pin.name = cWebserviceDefinitions.WEBPROPERTY_RECEIVEBIN;
                l_PropertyInfo11Pin.setValue(params[0].binCodeStr);
                l_PropertyInfoObl.add(l_PropertyInfo11Pin);

                PropertyInfo l_PropertyInfo12Pin = new PropertyInfo();
                l_PropertyInfo12Pin.name = cWebserviceDefinitions.WEBPROPERTY_RECEIVEBARCODECHECK;
                l_PropertyInfo12Pin.setValue(params[0].checkBarcodesBln);
                l_PropertyInfoObl.add(l_PropertyInfo12Pin);

                PropertyInfo l_PropertyInfo13Pin = new PropertyInfo();
                l_PropertyInfo13Pin.name = cWebserviceDefinitions.WEBPROPERTY_ADMINISTRATION;
                l_PropertyInfo13Pin.setValue("");
                l_PropertyInfoObl.add(l_PropertyInfo13Pin);

                l_WebresultWrs = cWebresult.pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_RECEIVECREATE, l_PropertyInfoObl);
            } catch (JSONException e) {
                l_WebresultWrs.setSuccessBln(false);
                l_WebresultWrs.setResultBln(false);
            }
            return l_WebresultWrs;
        }
    }

    //Region Items

    //End Region Private Methods








}
