package SSU_WHS.Basics.IdentifierWithDestination;

import android.app.Application;
import android.os.AsyncTask;

import org.json.JSONException;
import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import SSU_WHS.Basics.Users.cUser;
import SSU_WHS.Basics.Workplaces.cWorkplaceEntity;
import SSU_WHS.Basics.Workplaces.iWorkplaceDao;
import SSU_WHS.General.acScanSuiteDatabase;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebserviceDefinitions;

public class cIdentifierWithDestinationRepository {
    //Region Public Properties

    //End Region Public Properties

    //Region Private Properties

    //End Region Private Properties

    //Region Constructor
    cIdentifierWithDestinationRepository() {

    }
    //End Region Constructor

    //Region Public Methods

    public cWebresult pGetIdentifierFromWebserviceWrs(String pvIdentifierStr) {

        List<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();

        try {
            webResultWrs = new identifierFromWebserviceGetAsyncTask().execute(pvIdentifierStr).get();
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


   private static class identifierFromWebserviceGetAsyncTask extends AsyncTask<String, Void, cWebresult> {
        @Override
        protected cWebresult doInBackground(final String... params) {
            cWebresult WebresultWrs = new cWebresult();

            List<PropertyInfo> l_PropertyInfoObl = new ArrayList<>();

            PropertyInfo l_PropertyInfo1Pin = new PropertyInfo();
            l_PropertyInfo1Pin.name = cWebserviceDefinitions.WEBPROPERTY_USERNAMEDUTCH;
            l_PropertyInfo1Pin.setValue(cUser.currentUser.getNameStr());
            l_PropertyInfoObl.add(l_PropertyInfo1Pin);

            PropertyInfo l_PropertyInfo2Pin = new PropertyInfo();
            l_PropertyInfo2Pin.name = cWebserviceDefinitions.WEBPROPERTY_IDENTIFIER;
            l_PropertyInfo2Pin.setValue(params[0]);
            l_PropertyInfoObl.add(l_PropertyInfo2Pin);

            try {
                WebresultWrs = cWebresult.pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_GETIDENTIFIERWITHDESTINATION, l_PropertyInfoObl);
            } catch (JSONException e) {
                WebresultWrs.setResultBln(false);
                WebresultWrs.setSuccessBln(false);
                e.printStackTrace();
            }

            return WebresultWrs;
        }
    }

    //End Region Private Methods

}
