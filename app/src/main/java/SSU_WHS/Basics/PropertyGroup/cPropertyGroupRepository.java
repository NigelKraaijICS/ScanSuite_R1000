package SSU_WHS.Basics.PropertyGroup;

import android.app.Application;
import android.os.AsyncTask;

import org.json.JSONException;
import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import SSU_WHS.Basics.ItemProperty.cItemPropertyEntity;
import SSU_WHS.Basics.ItemProperty.iItemPropertyDao;
import SSU_WHS.Basics.Users.cUser;
import SSU_WHS.General.acScanSuiteDatabase;
import SSU_WHS.Picken.Pickorders.cPickorder;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebserviceDefinitions;


public class cPropertyGroupRepository {

    //Region Public properties
    private iPropertyGroupDao propertyGroupDao;

    private static class PropertyDataGetParams {
        String orderTypeStr;
        String orderStr;
        String propertyGroupStr;


        PropertyDataGetParams(String pvOrderTypeStr, String pvOrderStr, String pvPropertyGroupStr) {
            this.orderTypeStr = pvOrderTypeStr;
            this.orderStr = pvOrderStr;
            this.propertyGroupStr = pvPropertyGroupStr;
        }
    }
    //End Region Public Properties

    //Region Private Properties
    public//End Region Private Properties

    //Region Constructor
    cPropertyGroupRepository(Application pvApplication) {
        acScanSuiteDatabase db = acScanSuiteDatabase.pGetDatabase(pvApplication);
        this.propertyGroupDao = db.propertyGroupDao();
    }

    //Region Public Methods
    public cWebresult pGetPropertyGroupsFromWebserviceWrs() {


        List<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();
        try {
            webResultWrs = new cPropertyGroupRepository.mGetpropertyGroupsFromWebserviceGetAsyncTask().execute().get();
        } catch (ExecutionException | InterruptedException e) {
            webResultWrs.setResultBln(false);
            webResultWrs.setSuccessBln(false);
            resultObl.add(e.getLocalizedMessage());
            webResultWrs.setResultObl(resultObl);
            e.printStackTrace();
        }
        return webResultWrs;
    }

    public cWebresult pGetPropertyDataFromWebserviceWrs(String pvOrderTypeStr, String pvOrderStr, String pvPropertyGroupStr) {


        List<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();

        PropertyDataGetParams propertyDataGetParams = new PropertyDataGetParams(pvOrderTypeStr,pvOrderStr,pvPropertyGroupStr);

        try {
            webResultWrs = new cPropertyGroupRepository.mGetPropertyDataFromWebserviceGetAsyncTask().execute(propertyDataGetParams).get();
        } catch (ExecutionException | InterruptedException e) {
            webResultWrs.setResultBln(false);
            webResultWrs.setSuccessBln(false);
            resultObl.add(e.getLocalizedMessage());
            webResultWrs.setResultObl(resultObl);
            e.printStackTrace();
        }
        return webResultWrs;
    }


    public void pInsert(cPropertyGroupEntity pvPropertyGroupEntity){
    new insertAsyncTask(propertyGroupDao).execute(pvPropertyGroupEntity);
    }

    public void pDeleteAll() {
        new deleteAllAsyncTask(propertyGroupDao).execute();
    }
    //End Region Public Methods

    //Region Private Methods
    private static class deleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private iPropertyGroupDao mAsyncTaskDao;

        deleteAllAsyncTask(iPropertyGroupDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final Void... params) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }

    private static class insertAsyncTask extends AsyncTask<cPropertyGroupEntity, Void, Void>{
       private iPropertyGroupDao mAsyncTaskDao;

        insertAsyncTask(iPropertyGroupDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final cPropertyGroupEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }


    private static class mGetpropertyGroupsFromWebserviceGetAsyncTask extends AsyncTask<Void, Void, cWebresult> {
        @Override
        protected cWebresult doInBackground(final Void... params) {
            cWebresult l_WebresultWrs = new cWebresult();

            List<PropertyInfo> l_PropertyInfoObl = new ArrayList<>();

            try {
                l_WebresultWrs = cWebresult.pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_GETPROPERTYGROUPS, l_PropertyInfoObl);
            } catch (JSONException e) {
                l_WebresultWrs.setResultBln(false);
                l_WebresultWrs.setSuccessBln(false);
                e.printStackTrace();
            }

            return l_WebresultWrs;
        }
    }

    private static class mGetPropertyDataFromWebserviceGetAsyncTask extends AsyncTask<PropertyDataGetParams, Void, cWebresult> {
        @Override
        protected cWebresult doInBackground(final PropertyDataGetParams... params) {
            cWebresult l_WebresultWrs = new cWebresult();

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
            l_PropertyInfo3Pin.setValue(params[0].orderStr);
            l_PropertyInfoObl.add(l_PropertyInfo3Pin);

            String actionTypeStr;

            if (cPickorder.currentPickOrder.getStatusInt() < 20) {
                actionTypeStr = "TAKE";
            }
            else
            {
                actionTypeStr = "PLACE";
            }

            PropertyInfo l_PropertyInfo4Pin = new PropertyInfo();
            l_PropertyInfo4Pin.name = cWebserviceDefinitions.WEBPROPERTY_ACTIONTYPECODE;
            l_PropertyInfo4Pin.setValue(actionTypeStr);
            l_PropertyInfoObl.add(l_PropertyInfo4Pin);

            PropertyInfo l_PropertyInfo5Pin = new PropertyInfo();
            l_PropertyInfo5Pin.name = cWebserviceDefinitions.WEBPROPERTY_PROPERTIEGROUP;
            l_PropertyInfo5Pin.setValue(params[0].propertyGroupStr);
            l_PropertyInfoObl.add(l_PropertyInfo5Pin);

            PropertyInfo l_PropertyInfo6Pin = new PropertyInfo();
            l_PropertyInfo6Pin.name = cWebserviceDefinitions.WEBPROPERTY_SCANNER;
            l_PropertyInfo6Pin.setValue("");
            l_PropertyInfoObl.add(l_PropertyInfo5Pin);


            try {
                l_WebresultWrs = cWebresult.pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_GETPROPERTYLINEDATA, l_PropertyInfoObl);
            } catch (JSONException e) {
                l_WebresultWrs.setResultBln(false);
                l_WebresultWrs.setSuccessBln(false);
                e.printStackTrace();
            }

            return l_WebresultWrs;
        }
    }
}
