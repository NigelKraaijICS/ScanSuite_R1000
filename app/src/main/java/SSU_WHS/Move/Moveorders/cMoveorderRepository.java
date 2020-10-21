package SSU_WHS.Move.MoveOrders;

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

import ICS.Utils.cDeviceInfo;
import ICS.Utils.cSharedPreferences;
import ICS.Utils.cText;
import SSU_WHS.Basics.Article.cArticle;
import SSU_WHS.Basics.ArticleBarcode.cArticleBarcode;
import SSU_WHS.Basics.Users.cUser;
import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.General.acScanSuiteDatabase;
import SSU_WHS.General.cDatabase;
import SSU_WHS.Move.MoveorderLines.cMoveorderLineEntity;
import SSU_WHS.Move.MoveorderLines.iMoveorderLineDao;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebservice;
import SSU_WHS.Webservice.cWebserviceDefinitions;

import static ICS.Utils.cText.pAddSingleQuotesStr;

public class cMoveorderRepository {
    //Region Public Properties
    private iMoveorderDao moveorderDao;
    private iMoveorderLineDao moveorderLineDao;

    private static class MoveCreateParams {
        String documentStr;
        String binCodeStr;
        boolean checkBarcodesBln;

        MoveCreateParams(String pvDocumentStr,  String pvBinCodeStr, boolean pvCheckBarcodesBln ) {
            this.documentStr = pvDocumentStr;
            this.binCodeStr = pvBinCodeStr;
            this.checkBarcodesBln = pvCheckBarcodesBln;
        }
    }

    private static class MoveorderParams {
        String searchTextStr;
        String mainTypeStr;

        MoveorderParams(String pvSearchTextStr, String pvMainTypeStr) {
            this.searchTextStr = pvSearchTextStr;
            this.mainTypeStr = pvMainTypeStr;
        }
    }

    //End Region Public Properties

    //Region Constructor
    cMoveorderRepository(Application pvApplication) {
        //Region Private Properties
        acScanSuiteDatabase db = acScanSuiteDatabase.pGetDatabase(pvApplication);
        this.moveorderDao = db.moveorderDao();
        this.moveorderLineDao = db.moveorderLineDao();
    }
    //End Region Constructor

    //Region Public Methods

    public void insert (cMoveorderEntity moveorderEntity) {
        new mInsertAsyncTask(moveorderDao).execute(moveorderEntity);
    }

    public void deleteAll () {
        new mDeleteAllAsyncTask(moveorderDao).execute();
    }

    public cWebresult pCreateMoveOrderMVViaWebserviceWrs(String pvDocumentStr, String pvBinCodeStr, boolean pvCheckBarcodesBln) {

        List<String> resultObl = new ArrayList<>();

        MoveCreateParams moveCreateParams;
        moveCreateParams = new MoveCreateParams(pvDocumentStr, pvBinCodeStr, pvCheckBarcodesBln);


        cWebresult webResultWrs = new cWebresult();

        try {
            webResultWrs = new mCreateMoveOrderMVViaWebserviceAsyncTask().execute(moveCreateParams).get();
        } catch (ExecutionException | InterruptedException e) {
            webResultWrs.setResultBln(false);
            webResultWrs.setSuccessBln(false);
            resultObl.add(e.getLocalizedMessage());
            webResultWrs.setResultObl(resultObl);
            e.printStackTrace();
        }
        return webResultWrs;
    }

    public cWebresult pCreateMoveOrderMIViaWebserviceWrs() {

        List<String> resultObl = new ArrayList<>();


        cWebresult webResultWrs = new cWebresult();

        try {
            webResultWrs = new mCreateMoveOrderMIViaWebserviceAsyncTask().execute().get();
        } catch (ExecutionException | InterruptedException e) {
            webResultWrs.setResultBln(false);
            webResultWrs.setSuccessBln(false);
            resultObl.add(e.getLocalizedMessage());
            webResultWrs.setResultObl(resultObl);
            e.printStackTrace();
        }
        return webResultWrs;
    }

    public cWebresult pGetMoveordersFromWebserviceWrs(String pvSearchTextStr, String pvMainTypeStr) {

        List<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();

        MoveorderParams moveorderParams = new MoveorderParams(pvSearchTextStr, pvMainTypeStr);

        try {
            webResultWrs = new mGetMoveordersFromWebserviceAsyncTask().execute(moveorderParams).get();
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
            webResultWrs = new mMoveorderERPItemAddViaWebserviceAsyncTask().execute(pvArticleBarcode).get();
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
            webResultWrs = new mMoveorderHandledViaWebserviceAsyncTask().execute().get();
        } catch (ExecutionException | InterruptedException e) {
            webResultWrs.setResultBln(false);
            webResultWrs.setSuccessBln(false);
            resultObl.add(e.getLocalizedMessage());
            webResultWrs.setResultObl(resultObl);
            e.printStackTrace();
        }

        return webResultWrs;
    }

    public List<cMoveorderEntity> pGetMovesFromDatabaseWithFilterObl(String pvCurrentUserStr, Boolean pvUseFiltersBln) {

        List<cMoveorderEntity> ResultObl = null;
        StringBuilder SQLStatementStr;
        int i;

        SQLStatementStr = new StringBuilder("SELECT * FROM " + cDatabase.TABLENAME_MOVEORDER);
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

             SQLStatementStr.append(" ORDER BY Priority, Opdrachtnummer ASC");

        try {
            SupportSQLiteQuery query = new SimpleSQLiteQuery(SQLStatementStr.toString());
            ResultObl = new cMoveorderRepository.mGetMovesFromDatabaseWithFilterAsyncTask(moveorderDao).execute(query).get();

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return ResultObl;
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

    public List<cMoveorderLineEntity> pGetLinesForBinFromDatabaseObl(String pvBinStr) {
        List<cMoveorderLineEntity> resultObl = null;
        try {
            resultObl = new mGetLinesForBinFromDatabaseAsyncTask(moveorderLineDao).execute(pvBinStr).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return resultObl;
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

    public cWebresult pGetBINSFromWebserviceWrs(List<String> pvBINSObl) {

        List<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();

        try {
            webResultWrs = new mGetBINSViaWebserviceAsyncTask().execute(pvBINSObl).get();
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

    public cWebresult pCloseTakeMTViamWebserviceWrs() {

        List<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();

        try {
            webResultWrs = new mCloseTakeMTViaWebserviceAsyncTask().execute().get();
        } catch (ExecutionException | InterruptedException e) {
            webResultWrs.setResultBln(false);
            webResultWrs.setSuccessBln(false);
            resultObl.add(e.getLocalizedMessage());
            webResultWrs.setResultObl(resultObl);
            e.printStackTrace();
        }
        return webResultWrs;
    }

    //Endregion Public Methods

    private static class mMoveorderERPItemAddViaWebserviceAsyncTask extends AsyncTask<cArticleBarcode, Void, cWebresult> {
        @Override
        protected cWebresult doInBackground(cArticleBarcode... params) {
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
                l_PropertyInfo4Pin.setValue(cArticle.currentArticle.getItemNoStr());
                l_PropertyInfoObl.add(l_PropertyInfo4Pin);

                PropertyInfo l_PropertyInfo5Pin = new PropertyInfo();
                l_PropertyInfo5Pin.name = cWebserviceDefinitions.WEBPROPERTY_VARIANTCODE;
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
                l_PropertyInfo8Pin.setValue(params[0].getUniqueBarcodeBln());
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


                webresult = cWebresult.pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_MOVEBARCODECREATE, l_PropertyInfoObl);

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
                l_PropertyInfo4Pin.name = cWebserviceDefinitions.WEBPROPERTY_SCANNER;
                l_PropertyInfo4Pin.setValue(cDeviceInfo.getSerialnumberStr());
                l_PropertyInfoObl.add(l_PropertyInfo4Pin);

                PropertyInfo l_PropertyInfo5Pin = new PropertyInfo();
                l_PropertyInfo5Pin.name = cWebserviceDefinitions.WEBPROPERTY_BATCHBINCODE;
                l_PropertyInfo5Pin.setValue("");
                l_PropertyInfoObl.add(l_PropertyInfo5Pin);

                PropertyInfo l_PropertyInfo6Pin = new PropertyInfo();
                l_PropertyInfo6Pin.name = cWebserviceDefinitions.WEBPROPERTY_WORKFLOWSTEPCODESTR;
                l_PropertyInfo6Pin.setValue(cWarehouseorder.StepCodeEnu.Move_Place.toString().toUpperCase());
                l_PropertyInfoObl.add(l_PropertyInfo6Pin);

                PropertyInfo l_PropertyInfo7Pin = new PropertyInfo();
                l_PropertyInfo7Pin.name = cWebserviceDefinitions.WEBPROPERTY_CULTURE;
                l_PropertyInfo7Pin.setValue("");
                l_PropertyInfoObl.add(l_PropertyInfo7Pin);

                webresult = cWebresult.pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_MOVEHANDLED, l_PropertyInfoObl);

            } catch (JSONException e) {
                webresult.setSuccessBln(false);
                webresult.setResultBln(false);
            }
            return webresult;
        }
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

    private static class mCreateMoveOrderMVViaWebserviceAsyncTask extends AsyncTask<MoveCreateParams, Void, cWebresult> {
        @Override
        protected cWebresult doInBackground(MoveCreateParams... params) {
            cWebresult webresult = new cWebresult();
            try {
                List<PropertyInfo> l_PropertyInfoObl = new ArrayList<>();

                PropertyInfo l_PropertyInfo1Pin = new PropertyInfo();
                l_PropertyInfo1Pin.name = cWebserviceDefinitions.WEBPROPERTY_USERNAMEDUNGLISH;
                l_PropertyInfo1Pin.setValue(cUser.currentUser.getUsernameStr());
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
                l_PropertyInfo5Pin.setValue(params[0].documentStr);
                l_PropertyInfoObl.add(l_PropertyInfo5Pin);

                PropertyInfo l_PropertyInfo6Pin = new PropertyInfo();
                l_PropertyInfo6Pin.name = cWebserviceDefinitions.WEBPROPERTY_CONTAINER;
                l_PropertyInfo6Pin.setValue("");
                l_PropertyInfoObl.add(l_PropertyInfo6Pin);

                PropertyInfo l_PropertyInfo7Pin = new PropertyInfo();
                l_PropertyInfo7Pin.name = cWebserviceDefinitions.WEBPROPERTY_CURRENTLOCATION_SHORT;
                l_PropertyInfo7Pin.setValue(params[0].binCodeStr);
                l_PropertyInfoObl.add(l_PropertyInfo7Pin);

                PropertyInfo l_PropertyInfo8Pin = new PropertyInfo();
                l_PropertyInfo8Pin.name = cWebserviceDefinitions.WEBPROPERTY_EXTERNALREFERENCE;
                l_PropertyInfo8Pin.setValue("");
                l_PropertyInfoObl.add(l_PropertyInfo8Pin);

                PropertyInfo l_PropertyInfo9Pin = new PropertyInfo();
                l_PropertyInfo9Pin.name = cWebserviceDefinitions.WEBPROPERTY_MOVEBARCODECHECK;
                l_PropertyInfo9Pin.setValue(params[0].checkBarcodesBln);
                l_PropertyInfoObl.add(l_PropertyInfo9Pin);

                PropertyInfo l_PropertyInfo10Pin = new PropertyInfo();
                l_PropertyInfo10Pin.name = cWebserviceDefinitions.WEBPROPERTY_MIBATCHTRAKEBIN;
                l_PropertyInfo10Pin.setValue("");
                l_PropertyInfoObl.add(l_PropertyInfo10Pin);

                PropertyInfo l_PropertyInfo11Pin = new PropertyInfo();
                l_PropertyInfo11Pin.name = cWebserviceDefinitions.WEBPROPERTY_ADMINISTRATION;
                l_PropertyInfo11Pin.setValue("");
                l_PropertyInfoObl.add(l_PropertyInfo11Pin);

                webresult = cWebresult.pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_MOVEORDERCREATE, l_PropertyInfoObl);
            } catch (JSONException e) {
                webresult.setSuccessBln(false);
                webresult.setResultBln(false);
            }
            return webresult;
        }
    }

    private static class mCreateMoveOrderMIViaWebserviceAsyncTask extends AsyncTask<Void, Void, cWebresult> {
        @Override
        protected cWebresult doInBackground(Void... params) {
            cWebresult webresult = new cWebresult();
            try {
                List<PropertyInfo> l_PropertyInfoObl = new ArrayList<>();

                PropertyInfo l_PropertyInfo1Pin = new PropertyInfo();
                l_PropertyInfo1Pin.name = cWebserviceDefinitions.WEBPROPERTY_USERNAMEDUNGLISH;
                l_PropertyInfo1Pin.setValue(cUser.currentUser.getUsernameStr());
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
                l_PropertyInfo4Pin.setValue(cWarehouseorder.WorkflowEnu.MI.toString());
                l_PropertyInfoObl.add(l_PropertyInfo4Pin);

                PropertyInfo l_PropertyInfo5Pin = new PropertyInfo();
                l_PropertyInfo5Pin.name = cWebserviceDefinitions.WEBPROPERTY_DOCUMENT;
                l_PropertyInfo5Pin.setValue("");
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
                l_PropertyInfo8Pin.setValue("");
                l_PropertyInfoObl.add(l_PropertyInfo8Pin);

                PropertyInfo l_PropertyInfo9Pin = new PropertyInfo();
                l_PropertyInfo9Pin.name = cWebserviceDefinitions.WEBPROPERTY_MOVEBARCODECHECK;
                l_PropertyInfo9Pin.setValue(false);
                l_PropertyInfoObl.add(l_PropertyInfo9Pin);

                PropertyInfo l_PropertyInfo10Pin = new PropertyInfo();
                l_PropertyInfo10Pin.name = cWebserviceDefinitions.WEBPROPERTY_MIBATCHTRAKEBIN;
                l_PropertyInfo10Pin.setValue("");
                l_PropertyInfoObl.add(l_PropertyInfo10Pin);

                PropertyInfo l_PropertyInfo11Pin = new PropertyInfo();
                l_PropertyInfo11Pin.name = cWebserviceDefinitions.WEBPROPERTY_ADMINISTRATION;
                l_PropertyInfo11Pin.setValue("");
                l_PropertyInfoObl.add(l_PropertyInfo11Pin);

                webresult = cWebresult.pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_MOVEORDERCREATE, l_PropertyInfoObl);
            } catch (JSONException e) {
                webresult.setSuccessBln(false);
                webresult.setResultBln(false);
            }
            return webresult;
        }
    }

    private static class mGetMoveordersFromWebserviceAsyncTask extends AsyncTask<MoveorderParams, Void, cWebresult> {
        @Override
        protected cWebresult doInBackground(final MoveorderParams... params) {
            cWebresult WebresultWrs = new cWebresult();

            List<PropertyInfo> l_PropertyInfoObl = new ArrayList<>();

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
            l_PropertyInfo3Pin.setValue(params[0].searchTextStr);
            l_PropertyInfoObl.add(l_PropertyInfo3Pin);

            PropertyInfo l_PropertyInfo4Pin = new PropertyInfo();
            l_PropertyInfo4Pin.name = cWebserviceDefinitions.WEBPROPERTY_MAINTYPE;
            l_PropertyInfo4Pin.setValue(params[0].mainTypeStr);
            l_PropertyInfoObl.add(l_PropertyInfo4Pin);


            try {
                WebresultWrs = cWebresult.pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_GETMOVEORDERS, l_PropertyInfoObl);
            } catch (JSONException e) {
                WebresultWrs.setResultBln(false);
                WebresultWrs.setSuccessBln(false);
                e.printStackTrace();
            }

            return WebresultWrs;
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

                webresult = cWebresult.pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_GETMOVEORDERLINES, l_PropertyInfoObl);

            } catch (JSONException e) {
                webresult.setSuccessBln(false);
                webresult.setResultBln(false);
            }
            return webresult;
        }
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

                webresult = cWebresult.pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_GETMOVEORDERBARCODES, l_PropertyInfoObl);

            } catch (JSONException e) {
                webresult.setSuccessBln(false);
                webresult.setResultBln(false);
            }
            return webresult;
        }
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

                webresult = cWebresult.pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_GETMOVEORDERLINEBARCODES, l_PropertyInfoObl);

            } catch (JSONException e) {
                webresult.setSuccessBln(false);
                webresult.setResultBln(false);
            }
            return webresult;
        }
    }

    private static class mGetBINSViaWebserviceAsyncTask extends AsyncTask<List<String>, Void, cWebresult> {
        @Override
        protected cWebresult doInBackground(List<String>... params) {
            cWebresult webresult = new cWebresult();
            try {
                List<PropertyInfo> l_PropertyInfoObl = new ArrayList<>();



                PropertyInfo l_PropertyInfo1Pin = new PropertyInfo();
                l_PropertyInfo1Pin.name = cWebserviceDefinitions.WEBPROPERTY_LOCATION_NL;
                l_PropertyInfo1Pin.setValue(cUser.currentUser.currentBranch.getBranchStr());
                l_PropertyInfoObl.add(l_PropertyInfo1Pin);

                SoapObject binSubSet = new SoapObject(cWebservice.WEBSERVICE_NAMESPACE, cWebserviceDefinitions.WEBPROPERTY_BINSUBSET);

                for (String loopStr : params[0]) {
                    binSubSet.addProperty("string", loopStr);
                }

                PropertyInfo l_PropertyInfo2Pin = new PropertyInfo();
                l_PropertyInfo2Pin.name = cWebserviceDefinitions.WEBPROPERTY_BINSUBSET;
                l_PropertyInfo2Pin.setValue(binSubSet);
                l_PropertyInfoObl.add(l_PropertyInfo2Pin);

                webresult = cWebresult.pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_GETWAREHOUSELOCATIONS, l_PropertyInfoObl);

            } catch (JSONException e) {
                webresult.setSuccessBln(false);
                webresult.setResultBln(false);
            }
            return webresult;
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

    private static class mGetLinesForBinFromDatabaseAsyncTask extends AsyncTask<String, Void, List<cMoveorderLineEntity>> {
        private iMoveorderLineDao mAsyncTaskDao;

        mGetLinesForBinFromDatabaseAsyncTask(iMoveorderLineDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected List<cMoveorderLineEntity> doInBackground(final String... params) {
            return mAsyncTaskDao.getLinesForBinFromDatabase(params[0]);
        }
    }

    private static class mCloseTakeMTViaWebserviceAsyncTask extends AsyncTask<String, Void, cWebresult> {
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
                l_PropertyInfo3Pin.setValue(cMoveorder.currentMoveOrder.getOrderNumberStr());
                l_PropertyInfoObl.add(l_PropertyInfo3Pin);

                PropertyInfo l_PropertyInfo4Pin = new PropertyInfo();
                l_PropertyInfo4Pin.name = cWebserviceDefinitions.WEBPROPERTY_SCANNER;
                l_PropertyInfo4Pin.setValue(cDeviceInfo.getSerialnumberStr());
                l_PropertyInfoObl.add(l_PropertyInfo4Pin);

                PropertyInfo l_PropertyInfo5Pin = new PropertyInfo();
                l_PropertyInfo5Pin.name = cWebserviceDefinitions.WEBPROPERTY_WORKFLOWSTEPCODESTR;
                l_PropertyInfo5Pin.setValue(cWarehouseorder.MoveStatusEnu.Move_Take_Busy);
                l_PropertyInfoObl.add(l_PropertyInfo5Pin);

                PropertyInfo l_PropertyInfo6Pin = new PropertyInfo();
                l_PropertyInfo6Pin.name = cWebserviceDefinitions.WEBPROPERTY_CULTURE;
                l_PropertyInfo6Pin.setValue("");
                l_PropertyInfoObl.add(l_PropertyInfo6Pin);


                webresult = cWebresult.pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_MOVEPICKHANDLED, l_PropertyInfoObl);

            } catch (JSONException e) {
                webresult.setSuccessBln(false);
                webresult.setResultBln(false);
            }
            return webresult;
        }
    }


}
