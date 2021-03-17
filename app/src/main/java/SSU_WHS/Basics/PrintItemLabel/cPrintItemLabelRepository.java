package SSU_WHS.Basics.PrintItemLabel;

import android.os.AsyncTask;

import org.json.JSONException;
import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import SSU_WHS.Basics.Article.cArticle;
import SSU_WHS.Basics.LabelTemplate.cLabelTemplate;
import SSU_WHS.Basics.Users.cUser;
import SSU_WHS.Basics.Workplaces.cWorkplace;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebserviceDefinitions;

public class cPrintItemLabelRepository {
    //Region Public Methods

    public cWebresult pPrintLineItemLabelViaWebserviceWrs() {

        List<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();

        try {
            webResultWrs = new mPrintLineItemLabelViaWebserviceAsyncTask().execute().get();
        } catch (ExecutionException | InterruptedException e) {
            webResultWrs.setResultBln(false);
            webResultWrs.setSuccessBln(false);
            resultObl.add(e.getLocalizedMessage());
            webResultWrs.setResultObl(resultObl);
            e.printStackTrace();
        }
        return webResultWrs;
    }

    public cWebresult pPrintItemLabelViaWebserviceWrs() {

        List<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();

        try {
            webResultWrs = new mPrintItemLabelViaWebserviceAsyncTask().execute().get();
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
    private static class mPrintLineItemLabelViaWebserviceAsyncTask extends AsyncTask<Void, Void, cWebresult> {
        @Override
        protected cWebresult doInBackground(Void... params) {
            cWebresult webresult = new cWebresult();


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
                l_PropertyInfo3Pin.name = cWebserviceDefinitions.WEBPROPERTY_ITEMLABELTEMPLATE;
                l_PropertyInfo3Pin.setValue(cLabelTemplate.currentLabelTemplate.getTemplateStr());
                l_PropertyInfoObl.add(l_PropertyInfo3Pin);

                PropertyInfo l_PropertyInfo4Pin = new PropertyInfo();
                l_PropertyInfo4Pin.name = cWebserviceDefinitions.WEBPROPERTY_WORKSPACE;
                l_PropertyInfo4Pin.setValue(cWorkplace.currentWorkplace.getWorkplaceStr());
                l_PropertyInfoObl.add(l_PropertyInfo4Pin);

                PropertyInfo l_PropertyInfo5Pin = new PropertyInfo();
                l_PropertyInfo5Pin.name = cWebserviceDefinitions.WEBPROPERTY_PRINTQUANTITY;
                l_PropertyInfo5Pin.setValue(cPrintItemLabel.currentPrintItemLabel.getQuantityLng());
                l_PropertyInfoObl.add(l_PropertyInfo5Pin);

                PropertyInfo l_PropertyInfo6Pin = new PropertyInfo();
                l_PropertyInfo6Pin.name = cWebserviceDefinitions.WEBPROPERTY_LINENO;
                l_PropertyInfo6Pin.setValue(cPrintItemLabel.currentPrintItemLabel.getLineNoLng());
                l_PropertyInfoObl.add(l_PropertyInfo6Pin);

                PropertyInfo l_PropertyInfo7Pin = new PropertyInfo();
                l_PropertyInfo7Pin.name = cWebserviceDefinitions.WEBPROPERTY_BARCODE;
                l_PropertyInfo7Pin.setValue(cPrintItemLabel.currentPrintItemLabel.getBarcodeStr());
                l_PropertyInfoObl.add(l_PropertyInfo7Pin);

                PropertyInfo l_PropertyInfo8Pin = new PropertyInfo();
                l_PropertyInfo8Pin.name = cWebserviceDefinitions.WEBPROPERTY_ORDERTYPE;
                l_PropertyInfo8Pin.setValue(cPrintItemLabel.currentPrintItemLabel.getOrderTypeStr());
                l_PropertyInfoObl.add(l_PropertyInfo8Pin);

                PropertyInfo l_PropertyInfo9Pin = new PropertyInfo();
                l_PropertyInfo9Pin.name = cWebserviceDefinitions.WEBPROPERTY_ORDERNUMBER;
                l_PropertyInfo9Pin.setValue(cPrintItemLabel.currentPrintItemLabel.getOrderNumberStr());
                l_PropertyInfoObl.add(l_PropertyInfo9Pin);

                PropertyInfo l_PropertyInfo10Pin = new PropertyInfo();
                l_PropertyInfo10Pin.name = cWebserviceDefinitions.WEBPROPERTY_ITEMPROPERTYSEQUENCENO;
                l_PropertyInfo10Pin.setValue(0);
                l_PropertyInfoObl.add(l_PropertyInfo10Pin);
            try {
                webresult = cWebresult.pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_PRINTLINEITEMLABEL, l_PropertyInfoObl);

            } catch (JSONException e) {
                webresult.setSuccessBln(false);
                webresult.setResultBln(false);
            }
            return webresult;
        }
    }

    private static class mPrintItemLabelViaWebserviceAsyncTask extends AsyncTask<Void, Void, cWebresult> {
        @Override
        protected cWebresult doInBackground(Void... params) {
            cWebresult webresult = new cWebresult();


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
            l_PropertyInfo3Pin.name = cWebserviceDefinitions.WEBPROPERTY_ITEMLABELTEMPLATE;
            l_PropertyInfo3Pin.setValue(cLabelTemplate.currentLabelTemplate.getTemplateStr());
            l_PropertyInfoObl.add(l_PropertyInfo3Pin);

            PropertyInfo l_PropertyInfo4Pin = new PropertyInfo();
            l_PropertyInfo4Pin.name = cWebserviceDefinitions.WEBPROPERTY_WORKSPACE;
            l_PropertyInfo4Pin.setValue(cWorkplace.currentWorkplace.getWorkplaceStr());
            l_PropertyInfoObl.add(l_PropertyInfo4Pin);

            PropertyInfo l_PropertyInfo5Pin = new PropertyInfo();
            l_PropertyInfo5Pin.name = cWebserviceDefinitions.WEBPROPERTY_PRINTQUANTITY;
            l_PropertyInfo5Pin.setValue(cPrintItemLabel.currentPrintItemLabel.getQuantityLng());
            l_PropertyInfoObl.add(l_PropertyInfo5Pin);

            PropertyInfo l_PropertyInfo6Pin = new PropertyInfo();
            l_PropertyInfo6Pin.name = cWebserviceDefinitions.WEBPROPERTY_STOCKOWNER;
            l_PropertyInfo6Pin.setValue(cPrintItemLabel.currentPrintItemLabel.getStockOwnerStr());
            l_PropertyInfoObl.add(l_PropertyInfo6Pin);

            PropertyInfo l_PropertyInfo7Pin = new PropertyInfo();
            l_PropertyInfo7Pin.name = cWebserviceDefinitions.WEBPROPERTY_BARCODE;
            l_PropertyInfo7Pin.setValue(cPrintItemLabel.currentPrintItemLabel.getBarcodeStr());
            l_PropertyInfoObl.add(l_PropertyInfo7Pin);

            PropertyInfo l_PropertyInfo8Pin = new PropertyInfo();
            l_PropertyInfo8Pin.name = cWebserviceDefinitions.WEBPROPERTY_ITEMNO;
            l_PropertyInfo8Pin.setValue(cArticle.currentArticle.getItemNoStr());
            l_PropertyInfoObl.add(l_PropertyInfo8Pin);

            PropertyInfo l_PropertyInfo9Pin = new PropertyInfo();
            l_PropertyInfo9Pin.name = cWebserviceDefinitions.WEBPROPERTY_VARIANTCODE;
            l_PropertyInfo9Pin.setValue(cArticle.currentArticle.getVariantCodeStr());
            l_PropertyInfoObl.add(l_PropertyInfo9Pin);

            try {
                webresult = cWebresult.pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_PRINTITEMLABEL, l_PropertyInfoObl);

            } catch (JSONException e) {
                webresult.setSuccessBln(false);
                webresult.setResultBln(false);
            }
            return webresult;
        }
    }
    //End Region Private Methods
}
