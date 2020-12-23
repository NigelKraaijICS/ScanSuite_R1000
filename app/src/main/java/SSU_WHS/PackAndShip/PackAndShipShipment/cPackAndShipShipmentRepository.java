package SSU_WHS.PackAndShip.PackAndShipShipment;

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
import SSU_WHS.Basics.ShippingAgentServiceShippingUnits.cShippingAgentServiceShippingUnit;
import SSU_WHS.Basics.Users.cUser;
import SSU_WHS.General.acScanSuiteDatabase;
import SSU_WHS.PackAndShip.PackAndShipOrders.cPackAndShipOrder;
import SSU_WHS.Picken.Shipment.cShipment;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebservice;
import SSU_WHS.Webservice.cWebserviceDefinitions;

public class cPackAndShipShipmentRepository {



    //Region Public Properties
    private iPackAndShipShipmentDao packAndShipShipmentDao;
    //End Region Public Properties

    //Region Constructor
    cPackAndShipShipmentRepository(Application pvApplication) {
        //Region Private Properties
        acScanSuiteDatabase db = acScanSuiteDatabase.pGetDatabase(pvApplication);
        this.packAndShipShipmentDao = db.packAndShipShipmentDao();
    }
    //End Region Constructor

    //Region Public Methods

    public void insert(cPackAndShipShipmentEntity pvPackAndShipShipmentEntity) {
        new mInsertAsyncTask(packAndShipShipmentDao).execute(pvPackAndShipShipmentEntity);
    }

    public void delete(cPackAndShipShipmentEntity pvPackAndShipShipmentEntity) {
        new mDeleteAsyncTask(packAndShipShipmentDao).execute(pvPackAndShipShipmentEntity);
    }

    public void deleteAll() {
        new mDeleteAllAsyncTask(packAndShipShipmentDao).execute();
    }

    public cWebresult pHandledViaWebserviceWrs() {

        List<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();

        try {
            webResultWrs = new cPackAndShipShipmentRepository.mHandledViaWebserviceAsyncTask().execute().get();
        } catch (ExecutionException | InterruptedException e) {
            webResultWrs.setResultBln(false);
            webResultWrs.setSuccessBln(false);
            resultObl.add(e.getLocalizedMessage());
            webResultWrs.setResultObl(resultObl);
            e.printStackTrace();
        }
        return webResultWrs;
    }

    public cWebresult pShipViaWebserviceWrs(List<cShippingAgentServiceShippingUnit> pvShippingUnitsObl) {

        List<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();

        try {
            webResultWrs = new cPackAndShipShipmentRepository.mShipViaWebserviceAsyncTask().execute(pvShippingUnitsObl).get();
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

    private static class mInsertAsyncTask extends AsyncTask<cPackAndShipShipmentEntity, Void, Void> {
        private iPackAndShipShipmentDao mAsyncTaskDao;

        mInsertAsyncTask(iPackAndShipShipmentDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final cPackAndShipShipmentEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class mDeleteAsyncTask extends AsyncTask<cPackAndShipShipmentEntity, Void, Void> {
        private iPackAndShipShipmentDao mAsyncTaskDao;

        mDeleteAsyncTask(iPackAndShipShipmentDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final cPackAndShipShipmentEntity... params) {
            mAsyncTaskDao.delete(params[0]);
            return null;
        }
    }

    private static class mDeleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private iPackAndShipShipmentDao mAsyncTaskDao;

        mDeleteAllAsyncTask(iPackAndShipShipmentDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Void... params) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }

    private static class mHandledViaWebserviceAsyncTask extends AsyncTask<String, Void, cWebresult> {
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
                l_PropertyInfo3Pin.setValue(cPackAndShipOrder.currentPackAndShipOrder.getOrderNumberStr());
                l_PropertyInfoObl.add(l_PropertyInfo3Pin);

                PropertyInfo l_PropertyInfo4Pin = new PropertyInfo();
                l_PropertyInfo4Pin.name = cWebserviceDefinitions.WEBPROPERTY_SHIPMENT;
                l_PropertyInfo4Pin.setValue(cPackAndShipShipment.currentShipment.getSourceNoStr());
                l_PropertyInfoObl.add(l_PropertyInfo4Pin);

                PropertyInfo l_PropertyInfo5Pin = new PropertyInfo();
                l_PropertyInfo5Pin.name = cWebserviceDefinitions.WEBPROPERTY_HANDLEDTIMESTAMP;
                l_PropertyInfo5Pin.setValue(cDateAndTime.pGetCurrentDateTimeForWebserviceStr());
                l_PropertyInfoObl.add(l_PropertyInfo5Pin);

                PropertyInfo l_PropertyInfo6Pin = new PropertyInfo();
                l_PropertyInfo6Pin.name = cWebserviceDefinitions.WEBPROPERTY_SCANNERID;
                l_PropertyInfo6Pin.setValue(cDeviceInfo.getSerialnumberStr());
                l_PropertyInfoObl.add(l_PropertyInfo6Pin);



                webresult = cWebresult.pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_PACKANDSHIPSHIPMENTHANDLED, l_PropertyInfoObl);
            } catch (JSONException e) {
                webresult.setSuccessBln(false);
                webresult.setResultBln(false);
            }
            return webresult;
        }
    }

    private static class mShipViaWebserviceAsyncTask extends AsyncTask<List<cShippingAgentServiceShippingUnit>, Void, cWebresult> {
        @Override
        protected cWebresult doInBackground(List<cShippingAgentServiceShippingUnit>... params) {
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
                l_PropertyInfo3Pin.setValue(cPackAndShipOrder.currentPackAndShipOrder.getOrderNumberStr());
                l_PropertyInfoObl.add(l_PropertyInfo3Pin);

                PropertyInfo l_PropertyInfo4Pin = new PropertyInfo();
                l_PropertyInfo4Pin.name = cWebserviceDefinitions.WEBPROPERTY_SHIPMENT;
                l_PropertyInfo4Pin.setValue(cPackAndShipShipment.currentShipment.getSourceNoStr());
                l_PropertyInfoObl.add(l_PropertyInfo4Pin);

                PropertyInfo l_PropertyInfo5Pin = new PropertyInfo();
                l_PropertyInfo5Pin.name = cWebserviceDefinitions.WEBPROPERTY_SHIPPINGAGENT;
                l_PropertyInfo5Pin.setValue(cPackAndShipShipment.currentShipment.getShippingAgentCodeStr());
                l_PropertyInfoObl.add(l_PropertyInfo5Pin);

                PropertyInfo l_PropertyInfo6Pin = new PropertyInfo();
                l_PropertyInfo6Pin.name = cWebserviceDefinitions.WEBPROPERTY_SHIPPINGSERVICE;
                l_PropertyInfo6Pin.setValue(cPackAndShipShipment.currentShipment.getShippingAgentServiceCodeStr());
                l_PropertyInfoObl.add(l_PropertyInfo6Pin);

                PropertyInfo l_PropertyInfo7Pin = new PropertyInfo();
                l_PropertyInfo7Pin.name = cWebserviceDefinitions.WEBPROPERTY_SHIPPINGOPTIONS;
                l_PropertyInfo7Pin.setValue("");
                l_PropertyInfoObl.add(l_PropertyInfo7Pin);

                SoapObject shippingpackages = new SoapObject(cWebservice.WEBSERVICE_NAMESPACE, cWebserviceDefinitions.WEBPROPERTY_SHIPPINGPACKAGES);

                int sequencenumberInt = 0;
                String packageTypeToRememberStr = "";
                int counterForTypeInt = 0;

                for (cShippingAgentServiceShippingUnit shippingAgentServiceShippingUnit : params[0]) {

                    //If we didn't use this, continue
                    if (shippingAgentServiceShippingUnit.getShippingUnitQuantityUsedInt() <= 0) {
                        continue;
                    }

                    //New packageype, so reset sequenceNumber
                    if (!packageTypeToRememberStr.equalsIgnoreCase(shippingAgentServiceShippingUnit.getShippingUnitStr())) {
                        packageTypeToRememberStr = shippingAgentServiceShippingUnit.getShippingUnitStr();
                        sequencenumberInt = 0;
                        counterForTypeInt = 0;
                    }

                    while (counterForTypeInt < shippingAgentServiceShippingUnit.getShippingUnitQuantityUsedInt()) {
                        counterForTypeInt += 1;
                        sequencenumberInt += 10;

                        SoapObject soapObject = new SoapObject(cWebservice.WEBSERVICE_NAMESPACE, cWebserviceDefinitions.WEBPROPERTY_INTERFACESHIPPINGPACKAGE);
                        soapObject.addProperty(cWebserviceDefinitions.WEBPROPERTY_INTERFACESHIPPINGPACKAGE_PACKAGE, packageTypeToRememberStr);
                        soapObject.addProperty(cWebserviceDefinitions.WEBPROPERTY_INTERFACESHIPPINGPACKAGE_SEQUENCENUMBER, sequencenumberInt);
                        soapObject.addProperty(cWebserviceDefinitions.WEBPROPERTY_INTERFACESHIPPINGPACKAGE_WEIGHTING, 0);
                        soapObject.addProperty(cWebserviceDefinitions.WEBPROPERTY_INTERFACESHIPPINGPACKAGE_ITEMCOUNT, 0);
                        soapObject.addProperty(cWebserviceDefinitions.WEBPROPERTY_INTERFACESHIPPINGPACKAGE_CONTAINERTYPE, "");
                        soapObject.addProperty(cWebserviceDefinitions.WEBPROPERTY_INTERFACESHIPPINGPACKAGE_CONTAINER, "");
                        shippingpackages.addSoapObject(soapObject);
                    }
                }

                PropertyInfo l_PropertyInfo8Pin = new PropertyInfo();
                l_PropertyInfo8Pin.name = cWebserviceDefinitions.WEBPROPERTY_SHIPPINGPACKAGES;
                l_PropertyInfo8Pin.setValue(shippingpackages);
                l_PropertyInfoObl.add(l_PropertyInfo8Pin);

                PropertyInfo l_PropertyInfo9Pin = new PropertyInfo();
                l_PropertyInfo9Pin.name = cWebserviceDefinitions.WEBPROPERTY_HANDLEDTIMESTAMP;
                l_PropertyInfo9Pin.setValue(cDateAndTime.pGetCurrentDateTimeForWebserviceStr());
                l_PropertyInfoObl.add(l_PropertyInfo9Pin);


                PropertyInfo l_PropertyInfo10Pin = new PropertyInfo();
                l_PropertyInfo10Pin.name = cWebserviceDefinitions.WEBPROPERTY_SCANNERID;
                l_PropertyInfo10Pin.setValue(cDeviceInfo.getSerialnumberStr());
                l_PropertyInfoObl.add(l_PropertyInfo10Pin);


                webresult = cWebresult.pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_PACKANDSHIPSHIPMENTSHIPPED, l_PropertyInfoObl);
            } catch (JSONException e) {
                webresult.setSuccessBln(false);
                webresult.setResultBln(false);
            }
            return webresult;
        }
    }

    //End Region Private Methods


}

