package SSU_WHS.Webservice;

import android.util.Log;

import com.google.firebase.crashlytics.FirebaseCrashlytics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.ksoap2.transport.HttpTransportSE;
import org.ksoap2.transport.HttpsTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import ICS.Utils.cText;
import ICS.Weberror.cWeberror;
import ICS.Weberror.cWeberrorEntity;

public class cWebresult {

    private Boolean vSuccessBln;
    private Boolean vResultBln;
    private List<String> vResultObl;
    private Long vResultLng;
    private List<JSONObject> vResultDtt;

    public Boolean getSuccessBln ( )
    {
        return vSuccessBln;
    }
    public void setSuccessBln (Boolean pv_SuccessBln)
    {
        vSuccessBln = pv_SuccessBln;
    }

    public Boolean getResultBln ( )
    {
        return vResultBln;
    }
    public void setResultBln (Boolean pv_ResultBln)
    {
        vResultBln = pv_ResultBln;
    }

    public List<String> getResultObl ( )
    {
        return vResultObl;
    }
    public  String getResultStr() {

        String resultStr = "";

        if (this.getResultObl() != null && this.getResultObl().size()  > 0 ) {
            for (String resultLoopStr : this.getResultObl()) {
                resultStr += resultLoopStr + " ";
            }
         }

        return  resultStr;

    }
    public void setResultObl (List<String> pv_ResultObl)
    {
        vResultObl = pv_ResultObl;
    }
    public void addResult (String pvResult)
    {
        vResultObl.add(pvResult);
    }

    public Long getResultLng ( )
    {
        return vResultLng;
    }

    public List<JSONObject> getResultDtt ( )
    {
        return vResultDtt;
    }

    public static cWebresult pGetwebresultWrs(String pvMethodNameStr, List<PropertyInfo> pvPropertyInfoObl) throws JSONException {

        try {
            if (pvPropertyInfoObl != null) {
                FirebaseCrashlytics.getInstance().log("Webmethod: " +  pvMethodNameStr + " PropertyObl: " + pvPropertyInfoObl.toString());
            }
            else
            {
                FirebaseCrashlytics.getInstance().log("Webmethod: " +  pvMethodNameStr + " NO PARAMETERS");
            }
        } catch (Exception e) {
            return null;
        }

        cWebresult l_WebresultWrs = new cWebresult();
        l_WebresultWrs.vResultDtt = new ArrayList<>();
        l_WebresultWrs.vResultObl = new ArrayList<>();

        SoapObject request = new SoapObject(cWebservice.WEBSERVICE_NAMESPACE, pvMethodNameStr);

        URL url;
        boolean isHTTPSBln;
        String hostStr;
        String pathStr;
        int portInt;

        try {
            url = new URL(cWebservice.WEBSERVICE_URL);
        } catch (MalformedURLException e) {
            url = null;
        }
        if (url != null) {
            if (url.getProtocol().equalsIgnoreCase("https")) {
                isHTTPSBln = true;
            }
            else {
                isHTTPSBln = false;
            }
            hostStr = url.getHost();
            pathStr = url.getPath();
            portInt = url.getPort();
        }
        else {
            l_WebresultWrs.setSuccessBln(false);
            l_WebresultWrs.setResultBln(false);
            l_WebresultWrs.vResultObl.add("not a valid url");
            return l_WebresultWrs;
        }

        if (pvPropertyInfoObl != null) {
            for (int i = 0; i < pvPropertyInfoObl.size(); i++) {
                PropertyInfo propertyInfo = pvPropertyInfoObl.get(i);
                request.addProperty(propertyInfo);
            }
        }
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        new cMarshalDouble().register(envelope);
        new MarshalBase64().register(envelope);
        envelope.dotNet = true;

        //region complex types

        //endregion complex types

        envelope.setOutputSoapObject(request);

        try {
            if (isHTTPSBln) {

                if (portInt == 0) {
                    portInt = 443;
                }
                HttpsTransportSE httpsTransport = new HttpsTransportSE(hostStr, portInt, pathStr, cWebservice.WEBSERVICE_HTTPS_TIMEOUT);
                javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String s, SSLSession sslSession) {
                        return true;
                    }
                });                httpsTransport.call(cWebservice.WEBSERVICE_NAMESPACE + cWebservice.WEBSERVICE_WEBSERVICENAME + pvMethodNameStr, envelope);
            }
            else {
                HttpTransportSE httpTransport = new HttpTransportSE(url.toString(),cWebservice.WEBSERVICE_HTTPS_TIMEOUT);
                try {
                    httpTransport.call(cWebservice.WEBSERVICE_NAMESPACE + cWebservice.WEBSERVICE_WEBSERVICENAME + pvMethodNameStr, envelope);
                }
                catch (Exception e) {
                    l_WebresultWrs.setSuccessBln(false);
                    l_WebresultWrs.setResultBln(false);
                    l_WebresultWrs.vResultObl.add(e.getMessage());
                    return l_WebresultWrs;
                }
            }

        } catch (HttpResponseException e) {
            Log.e("HTTPLOG", e.getMessage());
            e.printStackTrace();

            l_WebresultWrs.setSuccessBln(false);
            l_WebresultWrs.setResultBln(false);
            l_WebresultWrs.vResultObl.add(e.getMessage());
            return l_WebresultWrs;
        } catch (IOException e) {
            Log.e("IOLOG", e.getMessage());
            e.printStackTrace();

            l_WebresultWrs.setSuccessBln(false);
            l_WebresultWrs.setResultBln(false);
            l_WebresultWrs.vResultObl.add(e.getMessage());

         return l_WebresultWrs;
        } catch (XmlPullParserException e) {
            Log.e("XMLLOG", e.getMessage());
            e.printStackTrace();

            l_WebresultWrs.setSuccessBln(false);
            l_WebresultWrs.setResultBln(false);
            l_WebresultWrs.vResultObl.add(e.getMessage());
            return l_WebresultWrs;
        } //send request

        SoapPrimitive result;
        try {
            result = (SoapPrimitive) envelope.getResponse();
        } catch (SoapFault soapFault) {
            l_WebresultWrs.setSuccessBln(false);
            l_WebresultWrs.setResultBln(false);
            l_WebresultWrs.vResultObl.add(soapFault.getMessage());
            return l_WebresultWrs;
        }
        JSONObject webserviceobject = null;
        JSONArray l_ResultOblArray = new JSONArray();
        JSONArray l_WebresultDatatableArray = new JSONArray();

        try {
            assert result != null;
            webserviceobject = new JSONObject(result.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        assert webserviceobject != null;
        JSONObject webResultObj = webserviceobject;

        l_WebresultWrs.vSuccessBln = webResultObj.getBoolean(cWebserviceDefinitions.SUCCESBLN_NAMESTR);
        l_WebresultWrs.vResultLng = webResultObj.getLong(cWebserviceDefinitions.RESULTLNG_NAMESTR);
        l_WebresultWrs.vResultBln = webResultObj.getBoolean(cWebserviceDefinitions.RESULTBLN_NAMESTR);

        try {
            l_ResultOblArray = webResultObj.getJSONArray(cWebserviceDefinitions.RESULTOBL_NAMESTR);
        } catch (JSONException e) {

        }
        if (l_ResultOblArray != null) {
            for (int j = 0; j < l_ResultOblArray.length(); j++) {
                JSONObject obj;
                try {
                    String value = l_ResultOblArray.getString(j);
                    l_WebresultWrs.vResultObl.add(value);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        //data
        try {
            l_WebresultDatatableArray = webResultObj.getJSONArray(cWebserviceDefinitions.RESULTDTT_NAMESTR);
        } catch (JSONException e) {

        }

        if (l_WebresultDatatableArray != null) {
            for (int i = 0; i < l_WebresultDatatableArray.length(); i++) {
                JSONObject o;
                try {
                    o = l_WebresultDatatableArray.getJSONObject(i);
                    l_WebresultWrs.vResultDtt.add(o);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        if (!l_WebresultWrs.getSuccessBln() || !l_WebresultWrs.getResultBln()) {
            mHandleWeberror(l_WebresultWrs,pvMethodNameStr, pvPropertyInfoObl);
        }
        return l_WebresultWrs;
    }
    private static void mHandleWeberror(cWebresult webresult, String webmethod , List<PropertyInfo> propertyInfoList) {
        if (!webresult.getSuccessBln() || !webresult.getResultBln()) {
            for (String errormessage : webresult.getResultObl()) {

                String parameters = "";
                if (propertyInfoList != null) {
                    parameters = propertyInfoList.toString();
                }

                 cWeberrorEntity weberrorEntity = new cWeberrorEntity(webmethod,parameters,errormessage,webresult);
                 cWeberror weberror = new cWeberror(weberrorEntity);
                 weberror.pInsertInDatabaseBln();
            }
        }
    }

    public List<String> pGetNextActivityObl() {

        List<String> resultObl = new ArrayList<>();
        boolean nextActivityFoundBln = false;
        int indexInt = 0;
        String nextActivityStr;


        if (this.getResultObl() == null) {
            return  resultObl;
        }

        if (this.getResultObl().size() < 1) {
            return  resultObl;
        }

        for (String loopStr : this.getResultObl()) {

            if (loopStr.equalsIgnoreCase("NEXT_ACTIVITY")) {
                nextActivityFoundBln = true;
                break;
            }

            indexInt += 1;

        }

        if (!nextActivityFoundBln) {
            return  resultObl;
        }

        nextActivityStr = this.getResultObl().get(indexInt +1);

        String[] value_split = nextActivityStr.split("\\þ");

        for (String  loopStr : value_split) {
            resultObl.add(loopStr);
        }

        return  resultObl;


    }


}
