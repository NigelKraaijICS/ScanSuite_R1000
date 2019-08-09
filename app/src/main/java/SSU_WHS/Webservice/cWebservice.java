package SSU_WHS.Webservice;

import android.os.AsyncTask;

import org.json.JSONException;

import java.util.concurrent.ExecutionException;

import ICS.Utils.cUserInterface;
import SSU_WHS.General.cAppExtension;
import SSU_WHS.General.cPublicDefinitions;
import nl.icsvertex.scansuite.R;

public class cWebservice {
    public static String WEBSERVICE_NAMESPACE = "http://www.icsvertex.nl/"; //Include '/' at end

    public static String WEBSERVICE_URL = "";

    public static String WEBSERVICE_WEBSERVICENAME = ""; //Include '/' at end (except when empty))

    public static Integer WEBSERVICE_HTTPS_PORT = 443;
    public static Integer WEBSERVICE_HTTPS_TIMEOUT = 60*1000;


    public static Boolean mGetWebserviceAvailableBln() {
        cWebresult l_webResult;
        Boolean l_resultBln;
        try {
            l_webResult = new getWebserviceAvailableAsyncTask().execute().get();
            if (l_webResult != null) {
                l_resultBln = l_webResult.getResultBln();
            }
            else {
                l_resultBln = false;
            }
        } catch (InterruptedException e) {
            l_resultBln = false;
            e.printStackTrace();
        } catch (ExecutionException e) {
            l_resultBln = false;
            e.printStackTrace();
        }
        return l_resultBln;
    }

    public static cWebresult mGetWebserviceAvailableWrs() {
        cWebresult l_webResult = new cWebresult();
        try {
            l_webResult = new getWebserviceAvailableAsyncTask().execute().get();
        } catch (ExecutionException e) {
            l_webResult.setResultBln(false);
            l_webResult.setSuccessBln(false);
            l_webResult.addResult(e.getLocalizedMessage());
            e.printStackTrace();
        } catch (InterruptedException e) {
            l_webResult.setResultBln(false);
            l_webResult.setSuccessBln(false);
            l_webResult.addResult(e.getLocalizedMessage());
            e.printStackTrace();
        }
        return l_webResult;
    }

    private static class getWebserviceAvailableAsyncTask extends AsyncTask<Void, Void, cWebresult> {
        @Override
        protected cWebresult doInBackground(Void... params) {
            cWebresult l_WebresultWrs = new cWebresult();
            try {
                l_WebresultWrs = new cWebresult().mGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_SERVICEAVAILABLE, null);
            } catch (JSONException e) {
                l_WebresultWrs.setSuccessBln(false);
                l_WebresultWrs.setResultBln(false);
            }
            return l_WebresultWrs;
        }
    }
    public static boolean mWebserviceIsAvailableAndRightVersion() {
        cWebresult webresult = mGetWebserviceAvailableWrs();

        if (webresult == null ||!webresult.getSuccessBln() || !webresult.getResultBln()) {
            cUserInterface.doExplodingScreen( cAppExtension.context.getResources().getString(R.string.message_webservice_not_live), "", true,true);
            return false;
        }
        if (!Long.toString(webresult.getResultLng()).equalsIgnoreCase(cPublicDefinitions.INTERFACE_VERSION)) {
            cUserInterface.doExplodingScreen(cAppExtension.context.getResources().getString(R.string.error_interface_version_should_be_parameter1_is_parameter2, Long.toString(webresult.getResultLng()),cPublicDefinitions.INTERFACE_VERSION ),"",  true, true);
            return false;
        }
        return true;
    }


}
