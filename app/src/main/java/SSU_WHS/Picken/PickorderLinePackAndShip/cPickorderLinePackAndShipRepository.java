package SSU_WHS.Picken.PickorderLinePackAndShip;

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
import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.Picken.PickorderLineBarcodes.cPickorderLineBarcode;
import SSU_WHS.Picken.Pickorders.cPickorder;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebservice;
import SSU_WHS.Webservice.cWebserviceDefinitions;

public class cPickorderLinePackAndShipRepository {
   // Public properties

    //End Public properties

    //End Private properties

    //Region Constructor

    cPickorderLinePackAndShipRepository() {
        //Private properties

    }
    //End Region Constructor

    //Region public methods


    public cWebresult pLineCheckedViaWebserviceWrs() {
        List<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();

        try {
            webResultWrs = new cPickorderLinePackAndShipRepository.mLineCheckedViaWebserviceAsyncTask().execute().get();
        } catch (ExecutionException | InterruptedException e) {
            webResultWrs.setResultBln(false);
            webResultWrs.setSuccessBln(false);
            resultObl.add(e.getLocalizedMessage());
            webResultWrs.setResultObl(resultObl);
            e.printStackTrace();
        }

        if (cPickorderLinePackAndShip.currentPickorderLinePackAndShip.getQuantityCheckedDbl() < cPickorderLinePackAndShip.currentPickorderLinePackAndShip.getQuantityDbl()) {
            try {
                webResultWrs = new cPickorderLinePackAndShipRepository.mLineCorrectionViaWebserviceAsyncTask().execute().get();
            } catch (ExecutionException | InterruptedException e) {
                webResultWrs.setResultBln(false);
                webResultWrs.setSuccessBln(false);
                resultObl.add(e.getLocalizedMessage());
                webResultWrs.setResultObl(resultObl);
                e.printStackTrace();
            }
        }

        return webResultWrs;
    }

    //End region public methods

    private static class mLineCorrectionViaWebserviceAsyncTask extends AsyncTask<Void, Void, cWebresult> {
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
                l_PropertyInfo3Pin.setValue(cPickorder.currentPickOrder.getOrderNumberStr());
                l_PropertyInfoObl.add(l_PropertyInfo3Pin);

                PropertyInfo l_PropertyInfo4Pin = new PropertyInfo();
                l_PropertyInfo4Pin.name = cWebserviceDefinitions.WEBPROPERTY_LINENOTAKE;
                l_PropertyInfo4Pin.setValue(cPickorderLinePackAndShip.currentPickorderLinePackAndShip.getLineNoTakeInt());
                l_PropertyInfoObl.add(l_PropertyInfo4Pin);


                PropertyInfo l_PropertyInfo5Pin = new PropertyInfo();
                l_PropertyInfo5Pin.name = cWebserviceDefinitions.WEBPROPERTY_HANDLEDTIMESTAMP;
                l_PropertyInfo5Pin.setValue(cDateAndTime.pGetCurrentDateTimeForWebserviceStr());
                l_PropertyInfoObl.add(l_PropertyInfo5Pin);

                PropertyInfo l_PropertyInfo6Pin = new PropertyInfo();
                l_PropertyInfo6Pin.name = cWebserviceDefinitions.WEBPROPERTY_SCANNER;
                l_PropertyInfo6Pin.setValue(cDeviceInfo.getSerialnumberStr());
                l_PropertyInfoObl.add(l_PropertyInfo6Pin);

                SoapObject barcodesHandledList = new SoapObject(cWebservice.WEBSERVICE_NAMESPACE, cWebserviceDefinitions.WEBPROPERTY_BARCODESLIST);

                //Only loop through handled barcodes, of there are any
                if (cPickorderLinePackAndShip.currentPickorderLinePackAndShip.handledBarcodesObl() != null) {
                    for (cPickorderLineBarcode pickorderLineBarcode: cPickorderLinePackAndShip.currentPickorderLinePackAndShip.handledBarcodesObl()) {
                        SoapObject soapObject = new SoapObject(cWebservice.WEBSERVICE_NAMESPACE, cWebserviceDefinitions.WEBPROPERTY_BARCODEHANDLED_COMPLEX);
                        soapObject.addProperty(cWebserviceDefinitions.WEBPROPERTY_BARCODE_COMPLEX, pickorderLineBarcode.getBarcodeStr());
                        soapObject.addProperty(cWebserviceDefinitions.WEBPROPERTY_QUANTITYHANDLED_COMPLEX, pickorderLineBarcode.getQuantityhandledDbl());
                        barcodesHandledList.addSoapObject(soapObject);
                    }
                }

                PropertyInfo l_PropertyInfo7Pin = new PropertyInfo();
                l_PropertyInfo7Pin.name = cWebserviceDefinitions.WEBPROPERTY_BARCODELIST;
                l_PropertyInfo7Pin.setValue(barcodesHandledList);
                l_PropertyInfoObl.add(l_PropertyInfo7Pin);

                PropertyInfo l_PropertyInfo8Pin = new PropertyInfo();
                l_PropertyInfo8Pin.name = cWebserviceDefinitions.WEBPROPERTY_CONTAINERSLIST;
                l_PropertyInfo8Pin.setValue(null);
                l_PropertyInfoObl.add(l_PropertyInfo8Pin);

                webresult = cWebresult.pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_PICKORDERLINECORRECT, l_PropertyInfoObl);

            } catch (JSONException e) {
                webresult.setSuccessBln(false);
                webresult.setResultBln(false);
            }
            return webresult;
        }
    }

    private static class mLineCheckedViaWebserviceAsyncTask extends AsyncTask<Void, Void, cWebresult> {
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
                l_PropertyInfo2Pin.setValue(cWarehouseorder.OrderTypeEnu.PICKEN.toString());
                l_PropertyInfoObl.add(l_PropertyInfo2Pin);

                PropertyInfo l_PropertyInfo3Pin = new PropertyInfo();
                l_PropertyInfo3Pin.name = cWebserviceDefinitions.WEBPROPERTY_LOCATION_NL;
                l_PropertyInfo3Pin.setValue(cUser.currentUser.currentBranch.getBranchStr());
                l_PropertyInfoObl.add(l_PropertyInfo3Pin);

                PropertyInfo l_PropertyInfo4Pin = new PropertyInfo();
                l_PropertyInfo4Pin.name = cWebserviceDefinitions.WEBPROPERTY_ORDERNUMBER;
                l_PropertyInfo4Pin.setValue(cPickorder.currentPickOrder.getOrderNumberStr());
                l_PropertyInfoObl.add(l_PropertyInfo4Pin);

                PropertyInfo l_PropertyInfo5Pin = new PropertyInfo();
                l_PropertyInfo5Pin.name = cWebserviceDefinitions.WEBPROPERTY_LINENO;
                l_PropertyInfo5Pin.setValue(cPickorderLinePackAndShip.currentPickorderLinePackAndShip.getLineNoInt());
                l_PropertyInfoObl.add(l_PropertyInfo5Pin);

                PropertyInfo l_PropertyInfo6Pin = new PropertyInfo();
                l_PropertyInfo6Pin.name = cWebserviceDefinitions.WEBPROPERTY_QUANTITYCHECKED;
                l_PropertyInfo6Pin.setValue(cPickorderLinePackAndShip.currentPickorderLinePackAndShip.getQuantityCheckedDbl());
                l_PropertyInfoObl.add(l_PropertyInfo6Pin);



                webresult = cWebresult.pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_PICKORDERLINECHECKED, l_PropertyInfoObl);

            } catch (JSONException e) {
                webresult.setSuccessBln(false);
                webresult.setResultBln(false);
            }
            return webresult;
        }
    }

    //Region private methods




}
