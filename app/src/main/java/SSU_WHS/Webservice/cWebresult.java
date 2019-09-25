package SSU_WHS.Webservice;

import android.util.Log;
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

    public static cWebresult pGetwebresultWrs(String pv_methodNameStr, List<PropertyInfo> pv_PropertyInfoObl) throws JSONException {

        cWebresult l_WebresultWrs = new cWebresult();
        l_WebresultWrs.vResultDtt = new ArrayList<>();
        l_WebresultWrs.vResultObl = new ArrayList<>();

        SoapObject request = new SoapObject(cWebservice.WEBSERVICE_NAMESPACE, pv_methodNameStr);

        URL url;
        boolean isHTTPSBln;
        String hostStr;
        String pathStr;

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
        }
        else {
            l_WebresultWrs.setSuccessBln(false);
            l_WebresultWrs.setResultBln(false);
            l_WebresultWrs.vResultObl.add("not a valid url");
            return l_WebresultWrs;
        }

        if (pv_PropertyInfoObl != null) {
            for (int i = 0; i < pv_PropertyInfoObl.size(); i++) {
                PropertyInfo propertyInfo = pv_PropertyInfoObl.get(i);
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
                HttpsTransportSE httpsTransport = new HttpsTransportSE(hostStr, cWebservice.WEBSERVICE_HTTPS_PORT, pathStr, cWebservice.WEBSERVICE_HTTPS_TIMEOUT);
                httpsTransport.call(cWebservice.WEBSERVICE_NAMESPACE + cWebservice.WEBSERVICE_WEBSERVICENAME + pv_methodNameStr, envelope);
            }
            else {
                HttpTransportSE httpTransport = new HttpTransportSE(url.toString());
                try {
                    httpTransport.call(cWebservice.WEBSERVICE_NAMESPACE + cWebservice.WEBSERVICE_WEBSERVICENAME + pv_methodNameStr, envelope);
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
            mHandleWeberror(l_WebresultWrs,pv_methodNameStr, pv_PropertyInfoObl);
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




}
