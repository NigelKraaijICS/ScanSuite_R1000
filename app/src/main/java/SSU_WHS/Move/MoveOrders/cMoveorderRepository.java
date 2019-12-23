package SSU_WHS.Move.MoveOrders;

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
import ICS.Utils.cDeviceInfo;
import ICS.Utils.cSharedPreferences;
import ICS.Utils.cText;
import SSU_WHS.Basics.ArticleBarcode.cArticleBarcode;
import SSU_WHS.Basics.Settings.cSetting;
import SSU_WHS.Basics.Users.cUser;
import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.General.acScanSuiteDatabase;
import SSU_WHS.General.cDatabase;
import SSU_WHS.Move.MoveorderLines.cMoveorderLineEntity;
import SSU_WHS.Move.MoveorderLines.iMoveorderLineDao;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebserviceDefinitions;

import static ICS.Utils.cText.pAddSingleQuotesStr;

public class cMoveorderRepository {
    //Region Public Properties
    public iMoveorderDao moveorderDao;
    public iMoveorderLineDao moveorderLineDao;

    //End Region Public Properties

    //Region Private Properties
    private acScanSuiteDatabase db;

    //Region Constructor
    cMoveorderRepository(Application pvApplication) {
        this.db = acScanSuiteDatabase.getDatabase(pvApplication);
        this.moveorderDao = db.moveorderDao();
        this.moveorderLineDao = db.moveorderLineDao();
    }
    //End Region Constructor

    //Region Public Methods
    public cWebresult pCreateMoveOrderViaWebserviceWrs(String pvDocumentStr) {

        List<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();

        try {
            webResultWrs = new mCreateMoveOrderViaWebserviceAsyncTask().execute(pvDocumentStr).get();
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
    private class MoveorderParams {
        String searchTextStr;
        String mainTypeStr;


        MoveorderParams(String pvSearchTextStr, String pvMainTypeStr) {
            this.searchTextStr = pvSearchTextStr;
            this.mainTypeStr = pvMainTypeStr;
        }
    }

    private class UpdateMoveorderCurrentLocationLocalParams {
        String orderNumberStr;
        String currentLocationStr;

        UpdateMoveorderCurrentLocationLocalParams(String pvOrderNumberStr, String pvCurrentLocationStr) {
            this.orderNumberStr = pvOrderNumberStr;
            this.currentLocationStr = pvCurrentLocationStr;
        }
    }

    public cWebresult pGetMoveordersFromWebserviceWrs(String pvSearchTextStr, String pvMainTypeStr) {

        List<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();

        MoveorderParams moveorderParams = new MoveorderParams(pvSearchTextStr, pvMainTypeStr);

        try {
            webResultWrs = new mGetMoveordersFromWebserviceAsyncTask().execute(moveorderParams).get();
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


    public cWebresult pAddUnkownItemViaWebserviceWrs(cBarcodeScan pvBarcodeScan) {
        List<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();

        try {
            webResultWrs = new mMoveorderUnknownItemAddViaWebserviceAsyncTask().execute(pvBarcodeScan).get();
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

    public cWebresult pAddERPItemViaWebserviceWrs(cArticleBarcode pvArticleBarcode) {
        List<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();

        try {
            webResultWrs = new mMoveorderERPItemAddViaWebserviceAsyncTask().execute(pvArticleBarcode).get();
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


    public cWebresult pHandledViaWebserviceWrs() {
        List<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();

        try {
            webResultWrs = new mMoveorderHandledViaWebserviceAsyncTask().execute().get();
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
    private static class mMoveorderUnknownItemAddViaWebserviceAsyncTask extends AsyncTask<cBarcodeScan, Void, cWebresult> {
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
                l_PropertyInfo3Pin.setValue(cMoveorder.currentMoveOrder.getOrderNumberStr());
                l_PropertyInfoObl.add(l_PropertyInfo3Pin);

                PropertyInfo l_PropertyInfo4Pin = new PropertyInfo();
                l_PropertyInfo4Pin.name = cWebserviceDefinitions.WEBPROPERTY_ITEMNO;
                l_PropertyInfo4Pin.setValue("UNKNOWN");
                l_PropertyInfoObl.add(l_PropertyInfo4Pin);

                PropertyInfo l_PropertyInfo5Pin = new PropertyInfo();
                l_PropertyInfo5Pin.name = cWebserviceDefinitions.WEBPROPERTY_VARIANTCODETINY;
                l_PropertyInfo5Pin.setValue(cMoveorder.currentMoveOrder.getUnknownVariantCounterInt());
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

                webresult = new cWebresult().pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_MOVEBARCODECREATE, l_PropertyInfoObl);

            } catch (JSONException e) {
                webresult.setSuccessBln(false);
                webresult.setResultBln(false);
            }
            return webresult;
        }
    }


    private static class mMoveorderERPItemAddViaWebserviceAsyncTask extends AsyncTask<cArticleBarcode, Void, cWebresult> {
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
                l_PropertyInfo3Pin.setValue(cMoveorder.currentMoveOrder.getOrderNumberStr());
                l_PropertyInfoObl.add(l_PropertyInfo3Pin);

                PropertyInfo l_PropertyInfo4Pin = new PropertyInfo();
                l_PropertyInfo4Pin.name = cWebserviceDefinitions.WEBPROPERTY_ITEMNO;
                l_PropertyInfo4Pin.setValue("UNKNOWN");
                l_PropertyInfoObl.add(l_PropertyInfo4Pin);

                PropertyInfo l_PropertyInfo5Pin = new PropertyInfo();
                l_PropertyInfo5Pin.name = cWebserviceDefinitions.WEBPROPERTY_VARIANTCODE;
                l_PropertyInfo5Pin.setValue(cMoveorder.currentMoveOrder.getUnknownVariantCounterInt());
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

                webresult = new cWebresult().pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_MOVEBARCODECREATE, l_PropertyInfoObl);

            } catch (JSONException e) {
                webresult.setSuccessBln(false);
                webresult.setResultBln(false);
            }
            return webresult;
        }
    }



    private static class mMoveorderHandledViaWebserviceAsyncTask extends AsyncTask<Void, Void, cWebresult> {
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
                l_PropertyInfo3Pin.setValue(cMoveorder.currentMoveOrder.getOrderNumberStr());
                l_PropertyInfoObl.add(l_PropertyInfo3Pin);

                PropertyInfo l_PropertyInfo4Pin = new PropertyInfo();
                l_PropertyInfo4Pin.name = cWebserviceDefinitions.WEBPROPERTY_SCANNER;
                l_PropertyInfo4Pin.setValue(cDeviceInfo.getSerialnumberStr());
                l_PropertyInfoObl.add(l_PropertyInfo4Pin);

                PropertyInfo l_PropertyInfo5Pin = new PropertyInfo();
                l_PropertyInfo5Pin.name = cWebserviceDefinitions.WEBPROPERTY_WORKFLOWSTEPCODESTR;
                l_PropertyInfo5Pin.setValue(cWarehouseorder.WorkflowMoveStepEnu.MoveHandled);
                l_PropertyInfoObl.add(l_PropertyInfo5Pin);

                PropertyInfo l_PropertyInfo6Pin = new PropertyInfo();
                l_PropertyInfo6Pin.name = cWebserviceDefinitions.WEBPROPERTY_CULTURE;
                l_PropertyInfo6Pin.setValue("");
                l_PropertyInfoObl.add(l_PropertyInfo6Pin);



                webresult = new cWebresult().pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_MOVEHANDLED, l_PropertyInfoObl);

            } catch (JSONException e) {
                webresult.setSuccessBln(false);
                webresult.setResultBln(false);
            }
            return webresult;
        }
    }

    public List<cMoveorderEntity> pGetMovesFromDatabaseWithFilterObl(String pvCurrentUserStr, Boolean pvUseFiltersBln) {

        List<cMoveorderEntity> ResultObl = null;
        String SQLStatementStr;

        SQLStatementStr = "SELECT * FROM " + cDatabase.TABLENAME_MOVEORDER ;
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
                SQLStatementStr += " WHERE AssignedUserId = " + pAddSingleQuotesStr(pvCurrentUserStr.toUpperCase()) + " OR  AssignedUserId = '' " ;
                SQLStatementStr += " OR CurrentUserId = " + pAddSingleQuotesStr(pvCurrentUserStr.toUpperCase()) + " OR  CurrentUserId = '' " ;
            }
//            FTT
            else if (!cSharedPreferences.showAssignedToMeBln() && cSharedPreferences.showAssignedToOthersBln() && cSharedPreferences.showNotAssignedBln()) {
                SQLStatementStr += " WHERE AssignedUserId != " + pAddSingleQuotesStr(pvCurrentUserStr.toUpperCase()) + " ";
            }
//            TFF
            else if (cSharedPreferences.showAssignedToMeBln() && !cSharedPreferences.showAssignedToOthersBln() && !cSharedPreferences.showNotAssignedBln()) {
                SQLStatementStr += " WHERE AssignedUserId = " + pAddSingleQuotesStr(pvCurrentUserStr.toUpperCase()) + " ";
                SQLStatementStr += " OR CurrentUserId = " + pAddSingleQuotesStr(pvCurrentUserStr.toUpperCase()) + " " ;
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
            ResultObl = new cMoveorderRepository.mGetMovesFromDatabaseWithFilterAsyncTask(moveorderDao).execute(query).get();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return ResultObl;
    }

    //Endregion Public Methods

    public void insert (cMoveorderEntity moveorderEntity) {
        new mInsertAsyncTask(moveorderDao).execute(moveorderEntity);
    }

    public List<cMoveorderEntity> pGetMoveordersFromDatabaseObl() {
        List<cMoveorderEntity> ResultObl = null;
        try {
            ResultObl = new mGetMoveordersFromDatabaseAsyncTask(moveorderDao).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return ResultObl;
    }

    public void deleteAll () {
        new mDeleteAllAsyncTask(moveorderDao).execute();
    }

    private static class mInsertAsyncTask extends AsyncTask<cMoveorderEntity, Void, Void> {
        private iMoveorderDao mAsyncTaskDao;

        mInsertAsyncTask(iMoveorderDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final cMoveorderEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class mDeleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private iMoveorderDao mAsyncTaskDao;

        mDeleteAllAsyncTask(iMoveorderDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final Void... params) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }

    private static class mCreateMoveOrderViaWebserviceAsyncTask extends AsyncTask<String, Void, cWebresult> {
        @Override
        protected cWebresult doInBackground(String... params) {
            cWebresult l_WebresultWrs = new cWebresult();
            try {
                List<PropertyInfo> l_PropertyInfoObl = new ArrayList<>();

                PropertyInfo l_PropertyInfo1Pin = new PropertyInfo();
                l_PropertyInfo1Pin.name = cWebserviceDefinitions.WEBPROPERTY_USERNAMEDUNGLISH;
                l_PropertyInfo1Pin.setValue(cUser.currentUser.getNameStr());
                l_PropertyInfoObl.add(l_PropertyInfo1Pin);

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
                l_PropertyInfo4Pin.setValue(cWarehouseorder.WorkflowEnu.MV.toString());
                l_PropertyInfoObl.add(l_PropertyInfo4Pin);

                PropertyInfo l_PropertyInfo5Pin = new PropertyInfo();
                l_PropertyInfo5Pin.name = cWebserviceDefinitions.WEBPROPERTY_DOCUMENT;
                l_PropertyInfo5Pin.setValue(params[0]);
                l_PropertyInfoObl.add(l_PropertyInfo5Pin);

                PropertyInfo l_PropertyInfo6Pin = new PropertyInfo();
                l_PropertyInfo6Pin.name = cWebserviceDefinitions.WEBPROPERTY_CONTAINER;
                l_PropertyInfo6Pin.setValue("");
                l_PropertyInfoObl.add(l_PropertyInfo6Pin);

                PropertyInfo l_PropertyInfo7Pin = new PropertyInfo();
                l_PropertyInfo7Pin.name = cWebserviceDefinitions.WEBPROPERTY_CURRENTLOCATION_SHORT;
                l_PropertyInfo7Pin.setValue("");
                l_PropertyInfoObl.add(l_PropertyInfo7Pin);

                PropertyInfo l_PropertyInfo8Pin = new PropertyInfo();
                l_PropertyInfo8Pin.name = cWebserviceDefinitions.WEBPROPERTY_EXTERNALREFERENCE;
                l_PropertyInfo8Pin.setValue(params[0]);
                l_PropertyInfoObl.add(l_PropertyInfo8Pin);

                PropertyInfo l_PropertyInfo9Pin = new PropertyInfo();
                l_PropertyInfo9Pin.name = cWebserviceDefinitions.WEBPROPERTY_MOVEBARCODECHECK;
                l_PropertyInfo9Pin.setValue(cSetting.MOVE_BARCODE_CHECK());
                l_PropertyInfoObl.add(l_PropertyInfo9Pin);

                PropertyInfo l_PropertyInfo10Pin = new PropertyInfo();
                l_PropertyInfo10Pin.name = cWebserviceDefinitions.WEBPROPERTY_MIBATCHTRAKEBIN;
                l_PropertyInfo10Pin.setValue("");
                l_PropertyInfoObl.add(l_PropertyInfo10Pin);

                PropertyInfo l_PropertyInfo11Pin = new PropertyInfo();
                l_PropertyInfo11Pin.name = cWebserviceDefinitions.WEBPROPERTY_ADMINISTRATION;
                l_PropertyInfo11Pin.setValue("");
                l_PropertyInfoObl.add(l_PropertyInfo11Pin);

                l_WebresultWrs = new cWebresult().pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_MOVEORDERCREATE, l_PropertyInfoObl);
            } catch (JSONException e) {
                l_WebresultWrs.setSuccessBln(false);
                l_WebresultWrs.setResultBln(false);
            }
            return l_WebresultWrs;
        }
    }

    private static class mGetMoveordersFromWebserviceAsyncTask extends AsyncTask<MoveorderParams, Void, cWebresult> {
        @Override
        protected cWebresult doInBackground(final MoveorderParams... params) {
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

            PropertyInfo l_PropertyInfo3Pin = new PropertyInfo();
            l_PropertyInfo3Pin.name = cWebserviceDefinitions.WEBPROPERTY_SEARCHTEXT;
            l_PropertyInfo3Pin.setValue(params[0].searchTextStr);
            l_PropertyInfoObl.add(l_PropertyInfo3Pin);

            PropertyInfo l_PropertyInfo4Pin = new PropertyInfo();
            l_PropertyInfo4Pin.name = cWebserviceDefinitions.WEBPROPERTY_MAINTYPE;
            l_PropertyInfo4Pin.setValue(params[0].mainTypeStr);
            l_PropertyInfoObl.add(l_PropertyInfo4Pin);


            try {
                WebresultWrs = new cWebresult().pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_GETMOVEORDERS, l_PropertyInfoObl);
            } catch (JSONException e) {
                WebresultWrs.setResultBln(false);
                WebresultWrs.setSuccessBln(false);
                e.printStackTrace();
            }

            return WebresultWrs;
        }
    }

    private static class mGetMoveordersFromDatabaseAsyncTask extends AsyncTask<Void, Void, List<cMoveorderEntity>> {
        private iMoveorderDao mAsyncTaskDao;

        mGetMoveordersFromDatabaseAsyncTask(iMoveorderDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected List<cMoveorderEntity> doInBackground(final Void... params) {
            return mAsyncTaskDao.getMoveordersFromDatabase();
        }
    }

    private static class mGetMovesFromDatabaseWithFilterAsyncTask extends AsyncTask<SupportSQLiteQuery, Void, List<cMoveorderEntity>> {
        private iMoveorderDao mAsyncTaskDao;

        mGetMovesFromDatabaseWithFilterAsyncTask(iMoveorderDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected List<cMoveorderEntity> doInBackground(final SupportSQLiteQuery... params) {
            return mAsyncTaskDao.getFilteredMoveOrders(params[0]);
        }
    }

    public cWebresult pGetLinesFromWebserviceWrs() {

        List<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();

        try {
            webResultWrs = new mGetLinesViaWebserviceAsyncTask().execute().get();
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

    private static class mGetLinesViaWebserviceAsyncTask extends AsyncTask<String, Void, cWebresult> {
        @Override
        protected cWebresult doInBackground(String... params) {
            cWebresult webresult = new cWebresult();
            try {
                List<PropertyInfo> l_PropertyInfoObl = new ArrayList<>();

                PropertyInfo l_PropertyInfo1Pin = new PropertyInfo();
                l_PropertyInfo1Pin.name = cWebserviceDefinitions.WEBPROPERTY_LOCATION_NL;
                l_PropertyInfo1Pin.setValue(cUser.currentUser.currentBranch.getBranchStr());
                l_PropertyInfoObl.add(l_PropertyInfo1Pin);

                PropertyInfo l_PropertyInfo2Pin = new PropertyInfo();
                l_PropertyInfo2Pin.name = cWebserviceDefinitions.WEBPROPERTY_ORDERNUMBER;
                l_PropertyInfo2Pin.setValue(cMoveorder.currentMoveOrder.getOrderNumberStr());
                l_PropertyInfoObl.add(l_PropertyInfo2Pin);

                webresult = new cWebresult().pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_GETMOVEORDERLINES, l_PropertyInfoObl);

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
                l_PropertyInfo2Pin.setValue(cMoveorder.currentMoveOrder.getOrderNumberStr());
                l_PropertyInfoObl.add(l_PropertyInfo2Pin);

                webresult = new cWebresult().pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_GETMOVEORDERBARCODES, l_PropertyInfoObl);

            } catch (JSONException e) {
                webresult.setSuccessBln(false);
                webresult.setResultBln(false);
            }
            return webresult;
        }
    }
    public List<cMoveorderLineEntity> pGetLinesHandledFromDatabaseObl() {
        List<cMoveorderLineEntity> resultObl = null;
        try {
            resultObl = new mGetHandledLinesOblAsyncTask(moveorderLineDao).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return resultObl;
    }

    public List<cMoveorderLineEntity> pGetLinesNotHandledFromDatabaseObl() {
        List<cMoveorderLineEntity> resultObl = null;
        try {
            resultObl = new mGetNotHandledLinesAsyncTask(moveorderLineDao).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return resultObl;
    }

    public List<cMoveorderLineEntity> pGetLinesForBinFromDatabaseObl(String pvBinStr) {
        List<cMoveorderLineEntity> resultObl = null;
        try {
            resultObl = new mGetLinesForBinFromDatabaseAsyncTask(moveorderLineDao).execute(pvBinStr).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return resultObl;
    }



    public cWebresult pGetLineBarcodesFromWebserviceWrs() {

        List<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();

        try {
            webResultWrs = new mGetLineBarcodesViaWebserviceAsyncTask().execute().get();
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
                l_PropertyInfo2Pin.setValue(cMoveorder.currentMoveOrder.getOrderNumberStr());
                l_PropertyInfoObl.add(l_PropertyInfo2Pin);

                webresult = new cWebresult().pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_GETMOVEORDERLINEBARCODES, l_PropertyInfoObl);

            } catch (JSONException e) {
                webresult.setSuccessBln(false);
                webresult.setResultBln(false);
            }
            return webresult;
        }
    }

    public cWebresult pGetCommentsFromWebservice(){
        ArrayList<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();

        try {
            webResultWrs = new mGetCommentsFromWebserviceAsyncTask().execute().get();
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
            l_PropertyInfo2Pin.setValue(cMoveorder.currentMoveOrder.getOrderNumberStr());
            l_PropertyInfoObl.add(l_PropertyInfo2Pin);

            try{
                WebresultWrs = cWebresult.pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_GETMOVEORDERCOMMENTS, l_PropertyInfoObl);
            } catch (JSONException e) {
                WebresultWrs.setResultBln(false);
                WebresultWrs.setSuccessBln(false);
                e.printStackTrace();
            }

            return WebresultWrs;
        }
    }

    public cWebresult pUpdateCurrentLocationViaWebserviceWrs(String pvCurrentLocationStr) {

        List<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();

        try {
            webResultWrs = new mUpdateCurrentLocationViaWebserviceAsyncTask().execute(pvCurrentLocationStr).get();
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

    public Boolean pPickorderUpdatCurrentLocationInDatabaseBln(String pvCurrentLocationStr) {

        Integer integerValue;
        UpdateMoveorderCurrentLocationLocalParams updateMoveorderCurrentLocationLocalParams = new UpdateMoveorderCurrentLocationLocalParams(cMoveorder.currentMoveOrder.getOrderNumberStr(), pvCurrentLocationStr);
        try {
            integerValue = new mUpdateMoveorderCurrentLocationInDatabaseAsyncTask(moveorderDao).execute(updateMoveorderCurrentLocationLocalParams).get();
            if (integerValue == 0 ) {
                return  false;
            }
            return  true;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return  false;
        } catch (ExecutionException e) {
            e.printStackTrace();
            return  false;
        }
    }

    private static class mUpdateCurrentLocationViaWebserviceAsyncTask extends AsyncTask<String, Void, cWebresult> {
        @Override
        protected cWebresult doInBackground(String... params) {
            cWebresult webresult = new cWebresult();
            try {
                List<PropertyInfo> l_PropertyInfoObl = new ArrayList<>();

                PropertyInfo l_PropertyInfo1Pin = new PropertyInfo();
                l_PropertyInfo1Pin.name = cWebserviceDefinitions.WEBPROPERTY_USERNAMEDUNGLISH;
                l_PropertyInfo1Pin.setValue(cUser.currentUser.getNameStr());
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
                l_PropertyInfo4Pin.name = cWebserviceDefinitions.WEBPROPERTY_CURRENTLOCATION;
                l_PropertyInfo4Pin.setValue(params[0]);
                l_PropertyInfoObl.add(l_PropertyInfo4Pin);

                webresult = new cWebresult().pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_MOVEORDERUPDATECURRENTLOCATIION, l_PropertyInfoObl);

            } catch (JSONException e) {
                webresult.setSuccessBln(false);
                webresult.setResultBln(false);
            }
            return webresult;
        }
    }

    private static class mUpdateMoveorderCurrentLocationInDatabaseAsyncTask extends AsyncTask<UpdateMoveorderCurrentLocationLocalParams, Void, Integer> {
        private iMoveorderDao mAsyncTaskDao;
        mUpdateMoveorderCurrentLocationInDatabaseAsyncTask(iMoveorderDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected Integer doInBackground(UpdateMoveorderCurrentLocationLocalParams... params) {
            return mAsyncTaskDao.updateMoveorderCurrentLocation(params[0].orderNumberStr, params[0].currentLocationStr);
        }
    }

    public Double pGetTotalQuantityDbl() {
        Double resultDbl = 0.0;
        try {
            resultDbl = new mGetTotalQuanitityAsyncTask(moveorderLineDao).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            resultDbl = 0d;
        } catch (ExecutionException e) {
            e.printStackTrace();
            resultDbl = 0d;
        }
        if (resultDbl == null) {
            resultDbl = 0d;
        }
        return resultDbl;
    }

    private static class mGetTotalQuanitityAsyncTask extends AsyncTask<Void, Void, Double> {
        private iMoveorderLineDao mAsyncTaskDao;

        mGetTotalQuanitityAsyncTask(iMoveorderLineDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected Double doInBackground(Void... params) {
            return mAsyncTaskDao.getTotalCountDbl();
        }
    }

    public Double pQuantityNotHandledDbl() {
        Double resultDbl = 0.0;
        try {
            resultDbl = new mGetQuantityNotHandledAsyncTask(moveorderLineDao).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            resultDbl = 0d;
        } catch (ExecutionException e) {
            e.printStackTrace();
            resultDbl = 0d;
        }
        if (resultDbl == null) {
            resultDbl = 0d;
        }
        return resultDbl;
    }

    private static class mGetQuantityNotHandledAsyncTask extends AsyncTask<Void, Void, Double> {
        private iMoveorderLineDao mAsyncTaskDao;
        mGetQuantityNotHandledAsyncTask(iMoveorderLineDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected Double doInBackground(Void... params) {
            return mAsyncTaskDao.getQuantityNotHandledDbl();
        }
    }

    public Double pQuantityHandledDbl() {
        Double resultDbl = 0.0;
        try {
            resultDbl = new mGetQuantityHandledAsyncTask(moveorderLineDao).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            resultDbl = 0d;
        } catch (ExecutionException e) {
            e.printStackTrace();
            resultDbl = 0d;
        }
        if (resultDbl == null) {
            resultDbl = 0d;
        }
        return resultDbl;
    }

    private static class mGetHandledLinesOblAsyncTask extends AsyncTask<Void, Void, List<cMoveorderLineEntity>> {
        private iMoveorderLineDao mAsyncTaskDao;

        mGetHandledLinesOblAsyncTask(iMoveorderLineDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected List<cMoveorderLineEntity> doInBackground(final Void... params) {
            return mAsyncTaskDao.getHandledMoveorderLineEntities();
        }
    }
    private static class mGetNotHandledLinesAsyncTask extends AsyncTask<Void, Void, List<cMoveorderLineEntity>> {
        private iMoveorderLineDao mAsyncTaskDao;

        mGetNotHandledLinesAsyncTask(iMoveorderLineDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected List<cMoveorderLineEntity> doInBackground(final Void... params) {
            return mAsyncTaskDao.getNotHandledOrderLineEntities();
        }
    }
    private static class mGetLinesForBinFromDatabaseAsyncTask extends AsyncTask<String, Void, List<cMoveorderLineEntity>> {
        private iMoveorderLineDao mAsyncTaskDao;

        mGetLinesForBinFromDatabaseAsyncTask(iMoveorderLineDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected List<cMoveorderLineEntity> doInBackground(final String... params) {
            return mAsyncTaskDao.getLinesForBinFromDatabase(params[0]);
        }
    }



    private static class mGetQuantityHandledAsyncTask extends AsyncTask<Void, Void, Double> {
        private iMoveorderLineDao mAsyncTaskDao;
        mGetQuantityHandledAsyncTask(iMoveorderLineDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected Double doInBackground(Void... params) {
            return mAsyncTaskDao.getNumberHandledDbl();
        }
    }


}
