package SSU_WHS.Return.ReturnOrder;

import android.app.Application;
import android.os.AsyncTask;

import androidx.sqlite.db.SimpleSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteQuery;

import org.json.JSONException;
import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cDateAndTime;
import ICS.Utils.cDeviceInfo;
import ICS.Utils.cSharedPreferences;
import ICS.Utils.cText;
import SSU_WHS.Basics.Article.cArticle;
import SSU_WHS.Basics.ArticleBarcode.cArticleBarcode;
import SSU_WHS.Basics.Settings.cSetting;
import SSU_WHS.Basics.Users.cUser;
import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.General.acScanSuiteDatabase;
import SSU_WHS.General.cDatabase;
import SSU_WHS.Return.ReturnorderBarcode.cReturnorderBarcodeEntity;
import SSU_WHS.Return.ReturnorderBarcode.iReturnorderBarcodeDao;
import SSU_WHS.Return.ReturnorderDocument.cReturnorderDocumentEntity;
import SSU_WHS.Return.ReturnorderDocument.iReturnorderDocumentDao;
import SSU_WHS.Return.ReturnorderLine.cReturnorderLine;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebserviceDefinitions;

import static ICS.Utils.cText.pAddSingleQuotesStr;

public class cReturnorderRepository {
    //Region Public Properties

    private iReturnorderDao returnorderDao;
    private iReturnorderDocumentDao returnorderDocumentDao;
    private iReturnorderBarcodeDao returnorderBarcodeDao;
    //End Region Public Properties

    private static class CreateReturnorderParams {
        String documentStr;
        Boolean multipleDocumentsBln;
        String bincodeStr;

        CreateReturnorderParams(String pvDocumentStr, Boolean pvMultipleDocumentsBln, String pvBincodeStr) {
            this.documentStr = pvDocumentStr;
            this.multipleDocumentsBln = pvMultipleDocumentsBln;
            this.bincodeStr = pvBincodeStr;
        }
    }

    //Region Constructor
    cReturnorderRepository(Application pvApplication) {
        //Region Private Properties
        acScanSuiteDatabase db = acScanSuiteDatabase.pGetDatabase(pvApplication);
        this.returnorderDao = db.returnorderDao();
    }
    //End Region Constructor

    //Region Public Methods
    public cWebresult pCreateReturnOrderViaWebserviceWrs(String pvDocumentStr, Boolean pvMultipleDocumentsBln, String pvBincodeStr) {

        List<String> resultObl = new ArrayList<>();
        CreateReturnorderParams createReturnorderParams = new CreateReturnorderParams(pvDocumentStr, pvMultipleDocumentsBln, pvBincodeStr);
        cWebresult webResultWrs = new cWebresult();

        try {
            webResultWrs = new mCreateReturnOrderViaWebserviceAsyncTask().execute(createReturnorderParams).get();
        } catch (ExecutionException | InterruptedException e) {
            webResultWrs.setResultBln(false);
            webResultWrs.setSuccessBln(false);
            resultObl.add(e.getLocalizedMessage());
            webResultWrs.setResultObl(resultObl);
            e.printStackTrace();
        }
        return webResultWrs;
    }

    public cWebresult pGetReturnordersFromWebserviceWrs(String pvSearchTextStr) {

        List<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();

        try {
            webResultWrs = new mGetReturnordersFromWebserviceAsyncTask().execute(pvSearchTextStr).get();
        } catch (ExecutionException | InterruptedException e) {
            webResultWrs.setResultBln(false);
            webResultWrs.setSuccessBln(false);
            resultObl.add(e.getLocalizedMessage());
            webResultWrs.setResultObl(resultObl);
            e.printStackTrace();
        }
        return webResultWrs;
    }


    public cWebresult pHandledViaWebserviceWrs() {
        List<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();

        try {
            webResultWrs = new mReturnorderHandledViaWebserviceAsyncTask().execute().get();
        } catch (ExecutionException | InterruptedException e) {
            webResultWrs.setResultBln(false);
            webResultWrs.setSuccessBln(false);
            resultObl.add(e.getLocalizedMessage());
            webResultWrs.setResultObl(resultObl);
            e.printStackTrace();
        }
        return webResultWrs;
    }

    private static class mReturnorderHandledViaWebserviceAsyncTask extends AsyncTask<Void, Void, cWebresult> {
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
                l_PropertyInfo3Pin.setValue(cReturnorder.currentReturnOrder.getOrderNumberStr());
                l_PropertyInfoObl.add(l_PropertyInfo3Pin);

                PropertyInfo l_PropertyInfo4Pin = new PropertyInfo();
                l_PropertyInfo4Pin.name = cWebserviceDefinitions.WEBPROPERTY_SCANNER;
                l_PropertyInfo4Pin.setValue(cDeviceInfo.getSerialnumberStr());
                l_PropertyInfoObl.add(l_PropertyInfo4Pin);

                PropertyInfo l_PropertyInfo5Pin = new PropertyInfo();
                l_PropertyInfo5Pin.name = cWebserviceDefinitions.WEBPROPERTY_WORKFLOWSTEPCODESTR;
                l_PropertyInfo5Pin.setValue(cWarehouseorder.StepCodeEnu.Retour.toString());
                l_PropertyInfoObl.add(l_PropertyInfo5Pin);

                PropertyInfo l_PropertyInfo6Pin = new PropertyInfo();
                l_PropertyInfo6Pin.name = cWebserviceDefinitions.WEBPROPERTY_CULTURE;
                l_PropertyInfo6Pin.setValue("");
                l_PropertyInfoObl.add(l_PropertyInfo6Pin);


                new cWebresult();
                webresult = cWebresult.pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_RETURNORDERHANDLED, l_PropertyInfoObl);

            } catch (JSONException e) {
                webresult.setSuccessBln(false);
                webresult.setResultBln(false);
            }
            return webresult;
        }
    }

    public List<cReturnorderEntity> pGetReturnOrdersFromDatabaseWithFilterObl(String pvCurrentUserStr, Boolean pvUseFiltersBln) {

        List<cReturnorderEntity> ResultObl = null;
        String SQLStatementStr;

        SQLStatementStr = "SELECT * FROM " + cDatabase.TABLENAME_RETURNORDER;
        if (pvUseFiltersBln) {
//            TTT
            if (cSharedPreferences.showAssignedToMeBln() && cSharedPreferences.showAssignedToOthersBln() && cSharedPreferences.showNotAssignedBln()) {
                SQLStatementStr += " WHERE 1=1 ";
            }
//            TTF
            else if (cSharedPreferences.showAssignedToMeBln() && cSharedPreferences.showAssignedToOthersBln() && !cSharedPreferences.showNotAssignedBln()) {
                SQLStatementStr += " WHERE AssignedUserId != '' ";
            }
//            TFT
            else if (cSharedPreferences.showAssignedToMeBln() && !cSharedPreferences.showAssignedToOthersBln() && cSharedPreferences.showNotAssignedBln()) {
                SQLStatementStr += " WHERE AssignedUserId = " + pAddSingleQuotesStr(pvCurrentUserStr.toUpperCase()) + " OR  AssignedUserId = '' ";
                SQLStatementStr += " OR CurrentUserId = " + pAddSingleQuotesStr(pvCurrentUserStr.toUpperCase()) + " OR  CurrentUserId = '' ";
            }
//            FTT
            else if (!cSharedPreferences.showAssignedToMeBln() && cSharedPreferences.showAssignedToOthersBln() && cSharedPreferences.showNotAssignedBln()) {
                SQLStatementStr += " WHERE AssignedUserId != " + pAddSingleQuotesStr(pvCurrentUserStr.toUpperCase()) + " ";
            }
//            TFF
            else if (cSharedPreferences.showAssignedToMeBln() && !cSharedPreferences.showAssignedToOthersBln() && !cSharedPreferences.showNotAssignedBln()) {
                SQLStatementStr += " WHERE AssignedUserId = " + pAddSingleQuotesStr(pvCurrentUserStr.toUpperCase()) + " ";
                SQLStatementStr += " OR CurrentUserId = " + pAddSingleQuotesStr(pvCurrentUserStr.toUpperCase()) + " ";
            }
//            FTF
            else if (!cSharedPreferences.showAssignedToMeBln() && cSharedPreferences.showAssignedToOthersBln() && !cSharedPreferences.showNotAssignedBln()) {
                SQLStatementStr += " WHERE AssignedUserId != " + pAddSingleQuotesStr(pvCurrentUserStr.toUpperCase()) + " AND  AssignedUserId != '' ";
            }
//            FFT
            else if (!cSharedPreferences.showAssignedToMeBln() && !cSharedPreferences.showAssignedToOthersBln() && cSharedPreferences.showNotAssignedBln()) {
                SQLStatementStr += " WHERE AssignedUserId = '' AND CurrentUserId = ''";
            }
//            FFF
            else if (!cSharedPreferences.showAssignedToMeBln() && !cSharedPreferences.showAssignedToOthersBln() && !cSharedPreferences.showNotAssignedBln()) {
                SQLStatementStr += " WHERE AssignedUserId = 'HELEMAALNIEMAND' ";
            }

        }

        try {
            SupportSQLiteQuery query = new SimpleSQLiteQuery(SQLStatementStr);
            ResultObl = new mGetReturnOrdersFromDatabaseWithFilterAsyncTask(returnorderDao).execute(query).get();

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return ResultObl;
    }


    //Endregion Public Methods

    //Region Items

    public cWebresult pAddUnkownItemViaWebserviceWrs(cBarcodeScan pvBarcodeScan) {
        List<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();

        try {
            webResultWrs = new mReturnorderUnknownItemAddViaWebserviceAsyncTask().execute(pvBarcodeScan).get();
        } catch (ExecutionException | InterruptedException e) {
            webResultWrs.setResultBln(false);
            webResultWrs.setSuccessBln(false);
            resultObl.add(e.getLocalizedMessage());
            webResultWrs.setResultObl(resultObl);
            e.printStackTrace();
        }
        return webResultWrs;
    }

    public cWebresult pAddERPItemViaWebserviceWrs(cArticleBarcode pvArticleBarcode) {
        List<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();

        try {
            webResultWrs = new mReturnorderERPItemAddViaWebserviceAsyncTask().execute(pvArticleBarcode).get();
        } catch (ExecutionException | InterruptedException e) {
            webResultWrs.setResultBln(false);
            webResultWrs.setSuccessBln(false);
            resultObl.add(e.getLocalizedMessage());
            webResultWrs.setResultObl(resultObl);
            e.printStackTrace();
        }
        return webResultWrs;
    }


    public void insert(cReturnorderEntity inventoryorderEntity) {
        new mInsertAsyncTask(returnorderDao).execute(inventoryorderEntity);
    }

    public List<cReturnorderEntity> pGetReturnordersFromDatabaseObl() {
        List<cReturnorderEntity> ResultObl = null;
        try {
            ResultObl = new mGetReturnordersFromDatabaseAsyncTask(returnorderDao).execute().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return ResultObl;
    }

    public void deleteAll() {
        new mDeleteAllAsyncTask(returnorderDao).execute();
    }

    private static class mInsertAsyncTask extends AsyncTask<cReturnorderEntity, Void, Void> {
        private iReturnorderDao mAsyncTaskDao;

        mInsertAsyncTask(iReturnorderDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final cReturnorderEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class mDeleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private iReturnorderDao mAsyncTaskDao;

        mDeleteAllAsyncTask(iReturnorderDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Void... params) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }

    private static class mCreateReturnOrderViaWebserviceAsyncTask extends AsyncTask<CreateReturnorderParams, Void, cWebresult> {
        @Override
        protected cWebresult doInBackground(CreateReturnorderParams... params) {
            cWebresult l_WebresultWrs = new cWebresult();
            try {
                List<PropertyInfo> l_PropertyInfoObl = new ArrayList<>();

                PropertyInfo l_PropertyInfo1Pin = new PropertyInfo();
                l_PropertyInfo1Pin.name = cWebserviceDefinitions.WEBPROPERTY_USERNAMEDUNGLISH;
                l_PropertyInfo1Pin.setValue(cUser.currentUser.getNameStr());
                l_PropertyInfoObl.add(l_PropertyInfo1Pin);

                PropertyInfo l_PropertyInfo10Pin = new PropertyInfo();
                l_PropertyInfo10Pin.name = cWebserviceDefinitions.WEBPROPERTY_LANGUAGEASCULTURE;
                l_PropertyInfo10Pin.setValue("NL");
                l_PropertyInfoObl.add(l_PropertyInfo10Pin);

                PropertyInfo l_PropertyInfo2Pin = new PropertyInfo();
                l_PropertyInfo2Pin.name = cWebserviceDefinitions.WEBPROPERTY_LOCATION_NL;
                l_PropertyInfo2Pin.setValue(cUser.currentUser.currentBranch.getBranchStr());
                l_PropertyInfoObl.add(l_PropertyInfo2Pin);

                PropertyInfo l_PropertyInfo3Pin = new PropertyInfo();
                l_PropertyInfo3Pin.name = cWebserviceDefinitions.WEBPROPERTY_STOCKOWNER;
                l_PropertyInfo3Pin.setValue("");
                l_PropertyInfoObl.add(l_PropertyInfo3Pin);

                PropertyInfo l_PropertyInfo4Pin = new PropertyInfo();
                l_PropertyInfo4Pin.name = cWebserviceDefinitions.WEBPROPERTY_WORKFLOW;
                l_PropertyInfo4Pin.setValue(cWarehouseorder.WorkflowEnu.RVS.toString());
                l_PropertyInfoObl.add(l_PropertyInfo4Pin);

                PropertyInfo l_PropertyInfo5Pin = new PropertyInfo();
                l_PropertyInfo5Pin.name = cWebserviceDefinitions.WEBPROPERTY_RECEIVEDDAT;
                l_PropertyInfo5Pin.setValue(cDateAndTime.pGetCurrentDateTimeForWebserviceStr());
                l_PropertyInfoObl.add(l_PropertyInfo5Pin);

                PropertyInfo l_PropertyInfo6Pin = new PropertyInfo();
                l_PropertyInfo6Pin.name = cWebserviceDefinitions.WEBPROPERTY_EXTERNALREFERENCE;
                l_PropertyInfo6Pin.setValue("");
                l_PropertyInfoObl.add(l_PropertyInfo6Pin);

                PropertyInfo l_PropertyInfo7Pin = new PropertyInfo();
                l_PropertyInfo7Pin.name = cWebserviceDefinitions.WEBPROPERTY_DOCUMENT;
                l_PropertyInfo7Pin.setValue(params[0].documentStr);
                l_PropertyInfoObl.add(l_PropertyInfo7Pin);

                PropertyInfo l_PropertyInfo8Pin = new PropertyInfo();
                l_PropertyInfo8Pin.name = cWebserviceDefinitions.WEBPROPERTY_REASON;
                l_PropertyInfo8Pin.setValue("");
                l_PropertyInfoObl.add(l_PropertyInfo8Pin);

                PropertyInfo l_PropertyInfo9Pin = new PropertyInfo();
                l_PropertyInfo9Pin.name = cWebserviceDefinitions.WEBPROPERTY_RETURNBIN;
                l_PropertyInfo9Pin.setValue(params[0].bincodeStr);
                l_PropertyInfoObl.add(l_PropertyInfo9Pin);

                PropertyInfo l_PropertyInfo11Pin = new PropertyInfo();
                l_PropertyInfo11Pin.name = cWebserviceDefinitions.WEBPROPERTY_RETURNBARCODECHECK;
                l_PropertyInfo11Pin.setValue(cSetting.RETOUR_BARCODE_CHECK());
                l_PropertyInfoObl.add(l_PropertyInfo11Pin);

                PropertyInfo l_PropertyInfo13Pin = new PropertyInfo();
                l_PropertyInfo13Pin.name = cWebserviceDefinitions.WEBPROPERTY_RETURNMULTIPLEDOCUMENTS;
                l_PropertyInfo13Pin.setValue(params[0].multipleDocumentsBln);
                l_PropertyInfoObl.add(l_PropertyInfo13Pin);

                PropertyInfo l_PropertyInfo12Pin = new PropertyInfo();
                l_PropertyInfo12Pin.name = cWebserviceDefinitions.WEBPROPERTY_ADMINISTRATION;
                l_PropertyInfo12Pin.setValue("");
                l_PropertyInfoObl.add(l_PropertyInfo12Pin);

                l_WebresultWrs = cWebresult.pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_RETURNORDERCREATE, l_PropertyInfoObl);
            } catch (JSONException e) {
                l_WebresultWrs.setSuccessBln(false);
                l_WebresultWrs.setResultBln(false);
            }
            return l_WebresultWrs;
        }
    }

    private static class mGetReturnordersFromWebserviceAsyncTask extends AsyncTask<String, Void, cWebresult> {
        @Override
        protected cWebresult doInBackground(final String... params) {
            cWebresult WebresultWrs = new cWebresult();

            List<PropertyInfo> l_PropertyInfoObl = new ArrayList<>();

            PropertyInfo l_PropertyInfo1Pin = new PropertyInfo();
            l_PropertyInfo1Pin.name = cWebserviceDefinitions.WEBPROPERTY_USERNAMEDUNGLISH;
            l_PropertyInfo1Pin.setValue(cUser.currentUser.getNameStr());
            l_PropertyInfoObl.add(l_PropertyInfo1Pin);

            PropertyInfo l_PropertyInfo2Pin = new PropertyInfo();
            l_PropertyInfo2Pin.name = cWebserviceDefinitions.WEBPROPERTY_LOCATION_NL;
            l_PropertyInfo2Pin.setValue(cUser.currentUser.currentBranch.getBranchStr());
            l_PropertyInfoObl.add(l_PropertyInfo2Pin);

            PropertyInfo l_PropertyInfo4Pin = new PropertyInfo();
            l_PropertyInfo4Pin.name = cWebserviceDefinitions.WEBPROPERTY_SEARCHTEXT;
            l_PropertyInfo4Pin.setValue(params[0]);
            l_PropertyInfoObl.add(l_PropertyInfo4Pin);

            try {
                WebresultWrs = cWebresult.pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_RETURNORDERSGET, l_PropertyInfoObl);
            } catch (JSONException e) {
                WebresultWrs.setResultBln(false);
                WebresultWrs.setSuccessBln(false);
                e.printStackTrace();
            }

            return WebresultWrs;
        }
    }

    private static class mGetReturnordersFromDatabaseAsyncTask extends AsyncTask<Void, Void, List<cReturnorderEntity>> {
        private iReturnorderDao mAsyncTaskDao;

        mGetReturnordersFromDatabaseAsyncTask(iReturnorderDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected List<cReturnorderEntity> doInBackground(final Void... params) {
            return mAsyncTaskDao.getReturnordersFromDatabase();
        }
    }

    private static class mGetReturnOrdersFromDatabaseWithFilterAsyncTask extends AsyncTask<SupportSQLiteQuery, Void, List<cReturnorderEntity>> {
        private iReturnorderDao mAsyncTaskDao;

        mGetReturnOrdersFromDatabaseWithFilterAsyncTask(iReturnorderDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected List<cReturnorderEntity> doInBackground(final SupportSQLiteQuery... params) {
            return mAsyncTaskDao.getFilteredReturnOrders(params[0]);
        }
    }

    public cWebresult pGetLinesFromWebserviceWrs() {

        List<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();

        try {
            webResultWrs = new mGetLinesViaWebserviceAsyncTask().execute().get();
        } catch (ExecutionException | InterruptedException e) {
            webResultWrs.setResultBln(false);
            webResultWrs.setSuccessBln(false);
            resultObl.add(e.getLocalizedMessage());
            webResultWrs.setResultObl(resultObl);
            e.printStackTrace();
        }
        return webResultWrs;
    }

    private static class mGetLinesViaWebserviceAsyncTask extends AsyncTask<Void, Void, cWebresult> {
        @Override
        protected cWebresult doInBackground(Void... params) {
            cWebresult webresult = new cWebresult();
            try {

                List<PropertyInfo> l_PropertyInfoObl = new ArrayList<>();

                PropertyInfo l_PropertyInfo3Pin = new PropertyInfo();
                l_PropertyInfo3Pin.name = cWebserviceDefinitions.WEBPROPERTY_LOCATION_NL;
                l_PropertyInfo3Pin.setValue(cUser.currentUser.currentBranch.getBranchStr());
                l_PropertyInfoObl.add(l_PropertyInfo3Pin);

                PropertyInfo l_PropertyInfo4Pin = new PropertyInfo();
                l_PropertyInfo4Pin.name = cWebserviceDefinitions.WEBPROPERTY_ORDERNUMBER;
                l_PropertyInfo4Pin.setValue(cReturnorder.currentReturnOrder.getOrderNumberStr());
                l_PropertyInfoObl.add(l_PropertyInfo4Pin);


                webresult = cWebresult.pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_RETURNLINESGET, l_PropertyInfoObl);

            } catch (JSONException e) {
                webresult.setSuccessBln(false);
                webresult.setResultBln(false);
            }
            return webresult;
        }
    }

    public cWebresult pGetHandledLinesFromWebserviceWrs() {

        List<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();

        try {
            webResultWrs = new mGetHandledLinesViaWebserviceAsyncTask().execute().get();
        } catch (ExecutionException | InterruptedException e) {
            webResultWrs.setResultBln(false);
            webResultWrs.setSuccessBln(false);
            resultObl.add(e.getLocalizedMessage());
            webResultWrs.setResultObl(resultObl);
            e.printStackTrace();
        }
        return webResultWrs;
    }

    private static class mGetHandledLinesViaWebserviceAsyncTask extends AsyncTask<Void, Void, cWebresult> {
        @Override
        protected cWebresult doInBackground(Void... params) {
            cWebresult webresult = new cWebresult();
            try {

                List<PropertyInfo> l_PropertyInfoObl = new ArrayList<>();

                PropertyInfo l_PropertyInfo3Pin = new PropertyInfo();
                l_PropertyInfo3Pin.name = cWebserviceDefinitions.WEBPROPERTY_LOCATION_NL;
                l_PropertyInfo3Pin.setValue(cUser.currentUser.currentBranch.getBranchStr());
                l_PropertyInfoObl.add(l_PropertyInfo3Pin);

                PropertyInfo l_PropertyInfo4Pin = new PropertyInfo();
                l_PropertyInfo4Pin.name = cWebserviceDefinitions.WEBPROPERTY_ORDERNUMBER;
                l_PropertyInfo4Pin.setValue(cReturnorder.currentReturnOrder.getOrderNumberStr());
                l_PropertyInfoObl.add(l_PropertyInfo4Pin);

                webresult = cWebresult.pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_RETURNLINESCANNEDGET, l_PropertyInfoObl);

            } catch (JSONException e) {
                webresult.setSuccessBln(false);
                webresult.setResultBln(false);
            }
            return webresult;
        }
    }

    public cWebresult pReturnDisposedViaWebserviceWrs() {

        List<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();

        try {
            webResultWrs = new mReturnDisposedViaWebserviceAsyncTask().execute().get();
        } catch (ExecutionException | InterruptedException e) {
            webResultWrs.setResultBln(false);
            webResultWrs.setSuccessBln(false);
            resultObl.add(e.getLocalizedMessage());
            webResultWrs.setResultObl(resultObl);
            e.printStackTrace();
        }
        return webResultWrs;
    }

    private static class mReturnDisposedViaWebserviceAsyncTask extends AsyncTask<Void, Void, cWebresult> {
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
                l_PropertyInfo4Pin.setValue(cReturnorder.currentReturnOrder.getOrderNumberStr());
                l_PropertyInfoObl.add(l_PropertyInfo4Pin);

                PropertyInfo l_PropertyInfo5Pin = new PropertyInfo();
                l_PropertyInfo5Pin.name = cWebserviceDefinitions.WEBPROPERTY_CULTURE;
                l_PropertyInfo5Pin.setValue("");
                l_PropertyInfoObl.add(l_PropertyInfo5Pin);

                webresult = cWebresult.pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_RETURNORDERDISPOSED, l_PropertyInfoObl);

            } catch (JSONException e) {
                webresult.setSuccessBln(false);
                webresult.setResultBln(false);
            }
            return webresult;
        }
    }

    //Region Items
    private static class mReturnorderUnknownItemAddViaWebserviceAsyncTask extends AsyncTask<cBarcodeScan, Void, cWebresult> {
        @Override
        protected cWebresult doInBackground(cBarcodeScan... params) {
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
                l_PropertyInfo3Pin.setValue(cReturnorder.currentReturnOrder.getOrderNumberStr());
                l_PropertyInfoObl.add(l_PropertyInfo3Pin);

                PropertyInfo l_PropertyInfo4Pin = new PropertyInfo();
                l_PropertyInfo4Pin.name = cWebserviceDefinitions.WEBPROPERTY_ITEMNO;
                l_PropertyInfo4Pin.setValue("UNKNOWN");
                l_PropertyInfoObl.add(l_PropertyInfo4Pin);

                PropertyInfo l_PropertyInfo5Pin = new PropertyInfo();
                l_PropertyInfo5Pin.name = cWebserviceDefinitions.WEBPROPERTY_VARIANTCODETINY;
                l_PropertyInfo5Pin.setValue(cReturnorder.currentReturnOrder.getUnknownVariantCounterInt());
                l_PropertyInfoObl.add(l_PropertyInfo5Pin);

                PropertyInfo l_PropertyInfo6Pin = new PropertyInfo();
                l_PropertyInfo6Pin.name = cWebserviceDefinitions.WEBPROPERTY_BARCODE;
                l_PropertyInfo6Pin.setValue(params[0].getBarcodeOriginalStr());
                l_PropertyInfoObl.add(l_PropertyInfo6Pin);

                PropertyInfo l_PropertyInfo7Pin = new PropertyInfo();
                l_PropertyInfo7Pin.name = cWebserviceDefinitions.WEBPROPERTY_BARCODETYPE;
                l_PropertyInfo7Pin.setValue(cText.pStringToIntegerInt(params[0].getBarcodeTypeStr()));
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
                l_PropertyInfo10Pin.setValue("???");
                l_PropertyInfoObl.add(l_PropertyInfo10Pin);

                PropertyInfo l_PropertyInfo11Pin = new PropertyInfo();
                l_PropertyInfo11Pin.name = cWebserviceDefinitions.WEBPROPERTY_ITEMTYPE;
                l_PropertyInfo11Pin.setValue(cText.pIntToStringStr(cWarehouseorder.ItemTypeEnu.Item));
                l_PropertyInfoObl.add(l_PropertyInfo11Pin);

                webresult = cWebresult.pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_RETURNBARCODECREATE, l_PropertyInfoObl);

            } catch (JSONException e) {
                webresult.setSuccessBln(false);
                webresult.setResultBln(false);
            }
            return webresult;
        }
    }

    private static class mReturnorderERPItemAddViaWebserviceAsyncTask extends AsyncTask<cArticleBarcode, Void, cWebresult> {
        @Override
        protected cWebresult doInBackground(cArticleBarcode... params) {
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
                l_PropertyInfo3Pin.setValue(cReturnorder.currentReturnOrder.getOrderNumberStr());
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
                l_PropertyInfo6Pin.setValue(params[0].getBarcodeStr());
                l_PropertyInfoObl.add(l_PropertyInfo6Pin);

                PropertyInfo l_PropertyInfo7Pin = new PropertyInfo();
                l_PropertyInfo7Pin.name = cWebserviceDefinitions.WEBPROPERTY_BARCODETYPE;
                l_PropertyInfo7Pin.setValue(params[0].getBarcodeTypeInt());
                l_PropertyInfoObl.add(l_PropertyInfo7Pin);

                PropertyInfo l_PropertyInfo8Pin = new PropertyInfo();
                l_PropertyInfo8Pin.name = cWebserviceDefinitions.WEBPROPERTY_ISUNIQUEBARCODE;
                l_PropertyInfo8Pin.setValue(params[0].isUniqueBarcodeBln);
                l_PropertyInfoObl.add(l_PropertyInfo8Pin);

                PropertyInfo l_PropertyInfo9Pin = new PropertyInfo();
                l_PropertyInfo9Pin.name = cWebserviceDefinitions.WEBPROPERTY_QUANTITYPERUNITOFMEASURE;
                l_PropertyInfo9Pin.setValue(params[0].getQuantityPerUnitOfMeasureDbl());
                l_PropertyInfoObl.add(l_PropertyInfo9Pin);

                PropertyInfo l_PropertyInfo10Pin = new PropertyInfo();
                l_PropertyInfo10Pin.name = cWebserviceDefinitions.WEBPROPERTY_UNITOFMEASURE;
                l_PropertyInfo10Pin.setValue(params[0].getUnitOfMeasureStr());
                l_PropertyInfoObl.add(l_PropertyInfo10Pin);

                PropertyInfo l_PropertyInfo11Pin = new PropertyInfo();
                l_PropertyInfo11Pin.name = cWebserviceDefinitions.WEBPROPERTY_ITEMTYPE;
                l_PropertyInfo11Pin.setValue(cText.pIntToStringStr(cWarehouseorder.ItemTypeEnu.Item));
                l_PropertyInfoObl.add(l_PropertyInfo11Pin);

                webresult =  cWebresult.pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_RETURNBARCODECREATE, l_PropertyInfoObl);

            } catch (JSONException e) {
                webresult.setSuccessBln(false);
                webresult.setResultBln(false);
            }
            return webresult;
        }
    }

    public cWebresult pGetBarcodesFromWebserviceWrs() {

        List<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();

        try {
            webResultWrs = new mGetBarcodesViaWebserviceAsyncTask().execute().get();
        } catch (ExecutionException | InterruptedException e) {
            webResultWrs.setResultBln(false);
            webResultWrs.setSuccessBln(false);
            resultObl.add(e.getLocalizedMessage());
            webResultWrs.setResultObl(resultObl);
            e.printStackTrace();
        }
        return webResultWrs;
    }

    private static class mGetBarcodesViaWebserviceAsyncTask extends AsyncTask<Void, Void, cWebresult> {
        @Override
        protected cWebresult doInBackground(Void... params) {
            cWebresult webresult = new cWebresult();
            try {
                List<PropertyInfo> l_PropertyInfoObl = new ArrayList<>();

                PropertyInfo l_PropertyInfo1Pin = new PropertyInfo();
                l_PropertyInfo1Pin.name = cWebserviceDefinitions.WEBPROPERTY_LOCATION_NL;
                l_PropertyInfo1Pin.setValue(cUser.currentUser.currentBranch.getBranchStr());
                l_PropertyInfoObl.add(l_PropertyInfo1Pin);

                PropertyInfo l_PropertyInfo2Pin = new PropertyInfo();
                l_PropertyInfo2Pin.name = cWebserviceDefinitions.WEBPROPERTY_ORDERNUMBER;
                l_PropertyInfo2Pin.setValue(cReturnorder.currentReturnOrder.getOrderNumberStr());
                l_PropertyInfoObl.add(l_PropertyInfo2Pin);

                webresult = cWebresult.pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_RETURNBARCODEGET, l_PropertyInfoObl);

            } catch (JSONException e) {
                webresult.setSuccessBln(false);
                webresult.setResultBln(false);
            }
            return webresult;
        }
    }

    public cWebresult pGetLineBarcodesFromWebserviceWrs() {

        List<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();

        try {
            webResultWrs = new mGetLineBarcodesViaWebserviceAsyncTask().execute().get();
        } catch (ExecutionException | InterruptedException e) {
            webResultWrs.setResultBln(false);
            webResultWrs.setSuccessBln(false);
            resultObl.add(e.getLocalizedMessage());
            webResultWrs.setResultObl(resultObl);
            e.printStackTrace();
        }
        return webResultWrs;
    }

    private static class mGetLineBarcodesViaWebserviceAsyncTask extends AsyncTask<Void, Void, cWebresult> {
        @Override
        protected cWebresult doInBackground(Void... params) {
            cWebresult webresult = new cWebresult();
            try {
                List<PropertyInfo> l_PropertyInfoObl = new ArrayList<>();

                PropertyInfo l_PropertyInfo1Pin = new PropertyInfo();
                l_PropertyInfo1Pin.name = cWebserviceDefinitions.WEBPROPERTY_LOCATION_NL;
                l_PropertyInfo1Pin.setValue(cUser.currentUser.currentBranch.getBranchStr());
                l_PropertyInfoObl.add(l_PropertyInfo1Pin);

                PropertyInfo l_PropertyInfo2Pin = new PropertyInfo();
                l_PropertyInfo2Pin.name = cWebserviceDefinitions.WEBPROPERTY_ORDERNUMBER;
                l_PropertyInfo2Pin.setValue(cReturnorder.currentReturnOrder.getOrderNumberStr());
                l_PropertyInfoObl.add(l_PropertyInfo2Pin);

                webresult = cWebresult.pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_RETURNLINESCANNEDBARCODESGET, l_PropertyInfoObl);

            } catch (JSONException e) {
                webresult.setSuccessBln(false);
                webresult.setResultBln(false);
            }
            return webresult;
        }
    }

    public cWebresult pGetCommentsFromWebservice() {
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

    private static class mGetCommentsFromWebserviceAsyncTask extends AsyncTask<Void, Void, cWebresult> {
        @Override
        protected cWebresult doInBackground(final Void... params) {
            cWebresult WebresultWrs = new cWebresult();

            List<PropertyInfo> l_PropertyInfoObl = new ArrayList<>();

            PropertyInfo l_PropertyInfo1Pin = new PropertyInfo();
            l_PropertyInfo1Pin.name = cWebserviceDefinitions.WEBPROPERTY_LOCATION_NL;
            l_PropertyInfo1Pin.setValue(cUser.currentUser.currentBranch.getBranchStr());
            l_PropertyInfoObl.add(l_PropertyInfo1Pin);

            PropertyInfo l_PropertyInfo2Pin = new PropertyInfo();
            l_PropertyInfo2Pin.name = cWebserviceDefinitions.WEBPROPERTY_ORDERNUMBER;
            l_PropertyInfo2Pin.setValue(cReturnorder.currentReturnOrder.getOrderNumberStr());
            l_PropertyInfoObl.add(l_PropertyInfo2Pin);

            try {
                WebresultWrs = cWebresult.pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_RETURNORDERCOMMENTSGET, l_PropertyInfoObl);
            } catch (JSONException e) {
                WebresultWrs.setResultBln(false);
                WebresultWrs.setSuccessBln(false);
                e.printStackTrace();
            }

            return WebresultWrs;
        }
    }
    private static class mGetReturnorderDocumentNotDoneFromDatabaseAsyncTask extends AsyncTask<Void, Void, List<cReturnorderDocumentEntity>> {
        private iReturnorderDocumentDao mAsyncTaskDao;
        mGetReturnorderDocumentNotDoneFromDatabaseAsyncTask(iReturnorderDocumentDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected List<cReturnorderDocumentEntity> doInBackground(final Void... params) {
            return mAsyncTaskDao.getReturnorderDocumentNotDone();
        }
    }

    private static class mGetReturnorderDocumentDoneFromDatabaseAsyncTask extends AsyncTask<Void, Void, List<cReturnorderDocumentEntity>> {
        private iReturnorderDocumentDao mAsyncTaskDao;
        mGetReturnorderDocumentDoneFromDatabaseAsyncTask(iReturnorderDocumentDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected List<cReturnorderDocumentEntity> doInBackground(final Void... params) {
            return mAsyncTaskDao.getReturnorderDocumentDone();
        }
    }

    private static class mGetBarcodeForSelectedLineFromDatabaseAsyncTask extends AsyncTask<String, Void, cReturnorderBarcodeEntity> {
        private iReturnorderBarcodeDao mAsyncTaskDao;
        mGetBarcodeForSelectedLineFromDatabaseAsyncTask(iReturnorderBarcodeDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected cReturnorderBarcodeEntity doInBackground(final String... params) {
            return mAsyncTaskDao.getBarcodeForLine(cReturnorderLine.currentReturnOrderLine.getItemNoStr(), cReturnorderLine.currentReturnOrderLine.getVariantCodeStr());
        }
    }

    private static class mCheckDocumentInDatabaseAsyncTask extends AsyncTask<String, Void, cReturnorderDocumentEntity> {
        private iReturnorderDocumentDao mAsyncTaskDao;
        mCheckDocumentInDatabaseAsyncTask(iReturnorderDocumentDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected cReturnorderDocumentEntity doInBackground(final String... params) {
            return mAsyncTaskDao.checkBin(params[0]);
        }
    }

    private static class pGetInventoryorderBinTotalFromDatabaseAsyncTask extends AsyncTask<Void, Void, List<cReturnorderDocumentEntity>> {
        private iReturnorderDocumentDao mAsyncTaskDao;
        pGetInventoryorderBinTotalFromDatabaseAsyncTask(iReturnorderDocumentDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected List<cReturnorderDocumentEntity> doInBackground(final Void... params) {
            return mAsyncTaskDao.getReturnorderDocumentTotal();
        }
    }
}

