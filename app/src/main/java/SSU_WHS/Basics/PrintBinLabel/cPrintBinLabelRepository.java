package SSU_WHS.Basics.PrintBinLabel;

import android.os.AsyncTask;

import org.json.JSONException;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import SSU_WHS.Basics.BranchReason.cBranchReason;
import SSU_WHS.Basics.LabelTemplate.cLabelTemplate;
import SSU_WHS.Basics.Users.cUser;
import SSU_WHS.Basics.Workplaces.cWorkplace;
import SSU_WHS.Return.ReturnOrder.cReturnorder;
import SSU_WHS.Return.ReturnorderBarcode.cReturnorderBarcode;
import SSU_WHS.Return.ReturnorderDocument.cReturnorderDocument;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebservice;
import SSU_WHS.Webservice.cWebserviceDefinitions;

public class cPrintBinLabelRepository {

    //Region Public Methods

    public cWebresult pPrintBinLabelViaWebserviceWrs() {

        List<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();

        try {
            webResultWrs = new mPrintBinLabelViaWebserviceAsyncTask().execute().get();
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

    private static class mPrintBinLabelViaWebserviceAsyncTask extends AsyncTask<String, Void, cWebresult> {
        @Override
        protected cWebresult doInBackground(final String... params) {
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
            l_PropertyInfo3Pin.name = cWebserviceDefinitions.WEBPROPERTY_BINLABELTEMPLATE;
            l_PropertyInfo3Pin.setValue(cLabelTemplate.currentLabelTemplate.getTemplateStr());
            l_PropertyInfoObl.add(l_PropertyInfo3Pin);

            PropertyInfo l_PropertyInfo4Pin = new PropertyInfo();
            l_PropertyInfo4Pin.name = cWebserviceDefinitions.WEBPROPERTY_WORKSPACE;
            l_PropertyInfo4Pin.setValue(cWorkplace.currentWorkplace.getWorkplaceStr());
            l_PropertyInfoObl.add(l_PropertyInfo4Pin);

            PropertyInfo l_PropertyInfo5Pin = new PropertyInfo();
            l_PropertyInfo5Pin.name = cWebserviceDefinitions.WEBPROPERTY_PRINTQUANTITY;
            l_PropertyInfo5Pin.setValue(cPrintBinLabel.currentPrintBinLabel.getQuantityLng());
            l_PropertyInfoObl.add(l_PropertyInfo5Pin);

            PropertyInfo l_PropertyInfo6Pin = new PropertyInfo();
            l_PropertyInfo6Pin.name = cWebserviceDefinitions.WEBPROPERTY_PRINTBINCODENL;
            l_PropertyInfo6Pin.setValue(cPrintBinLabel.currentPrintBinLabel.getBincodeStr());
            l_PropertyInfoObl.add(l_PropertyInfo6Pin);
            try {
                webresult = cWebresult.pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_PRINTBINLABEL, l_PropertyInfoObl);
            } catch (JSONException e) {
                webresult.setResultBln(false);
                webresult.setSuccessBln(false);
                e.printStackTrace();
            }

            return webresult;
        }
    }

    //End Region Private Methods
}
