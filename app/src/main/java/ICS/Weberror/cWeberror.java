package ICS.Weberror;

import android.os.Bundle;

import androidx.lifecycle.ViewModelProviders;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.List;

import ICS.cAppExtension;
import SSU_WHS.Webservice.cWebservice;

public class cWeberror {

    //Region Public Properties

    public Integer recordidInt;
    public Integer getRecordidInt() {
        return recordidInt;
    }

    public Boolean isSuccessBln;
    public Boolean getSuccessBln() {
        return isSuccessBln;
    }

    public Boolean isResultBln;
    public Boolean getResultBln() {
        return isResultBln;
    }

    public String activityStr;
    public String getActivityStr() {
        return activityStr;
    }

    public String webmethodStr;
    public String getWebmethodStr() {
        return webmethodStr;
    }

    public String parametersStr;
    public String getParametersStr() {
        return parametersStr;
    }

    public String resultStr;
    public String getResultStr() {
        return resultStr;
    }

    public String deviceStr;
    public String getDeviceStr() {
        return deviceStr;
    }

    public String datetimeStr;
    public String getDatetimeStr() {
        return datetimeStr;
    }

    public String localstatusStr;
    public String getLocalstatusStr() {
        return localstatusStr;
    }

    public cWeberrorEntity weberrorEntity;
    public boolean inDatabaseBln;

    public static cWeberrorViewModel gWebErrorViewModel;

    public static cWeberrorViewModel getWebErrorViewModel() {
        if (gWebErrorViewModel == null) {
            gWebErrorViewModel = ViewModelProviders.of(cAppExtension.fragmentActivity ).get(cWeberrorViewModel.class);
        }
        return gWebErrorViewModel;
    }

    public static List<cWeberror> allWebErrors;

    public enum WeberrorStatusEnu {
        NEW,
        SHOWNNOTSEND,
        SENDING,
        SENT
    }

    private static FirebaseAnalytics mFirebaseAnalytics;

    public static final String FIREBASE_ITEMNAME = "ics scansuite webservice event";
    public static final String FIREBASE_ISSUCCESS = "issuccess";
    public static final String FIREBASE_ISRESULT = "isResultBln";
    public static final String FIREBASE_ACTIVITY = "activityStr";
    public static final String FIREBASE_MESSAGE = "message";
    public static final String FIREBASE_DEVICE = "deviceStr";
    public static final String FIREBASE_PARAMETERS = "parametersStr";
    public static final String FIREBASE_METHOD = "method";
    public static final String FIREBASE_TIMESTAMP = "timestamp";
    public static final String FIREBASE_URL = "url";

    public static final String FIREBASE_KEY_LANGUAGE = "language";
    public static final String FIREBASE_KEY_SSID = "ssid";
    public static final String FIREBASE_KEY_IP = "ipaddress";

    //End Region public Properties

    //Region Constructor
    public cWeberror(cWeberrorEntity pvWeberrorEntity) {
        this.weberrorEntity = pvWeberrorEntity;
        this.recordidInt = this.weberrorEntity.getRecordidInt();
        this.isSuccessBln = this.weberrorEntity.getIsSuccessBln();
        this.isResultBln = this.weberrorEntity.getIsresultBln();
        this.activityStr =  this.weberrorEntity.getActivityStr();
        this.webmethodStr = this.weberrorEntity.getWebmethodStr();
        this.parametersStr = this.weberrorEntity.getParametersStr();
        this.resultStr = this.weberrorEntity.getResultStr();
        this.deviceStr = this.weberrorEntity.getDeviceStr();
        this.datetimeStr = this.weberrorEntity.getDatetimeStr();
        this.localstatusStr = this.weberrorEntity.getLocalstatusStr();
        }
    //End Region Constructor

    //Region Public Methods

    public static void pGetAllWebErrors() {

        cWeberror.allWebErrors = new ArrayList<>();

        List<cWeberrorEntity> WebErrorsFromDatabaseObl =   cWeberror.getWebErrorViewModel().getAllWebErrors();

        for (cWeberrorEntity weberrorEntity : WebErrorsFromDatabaseObl) {
            cWeberror weberror = new cWeberror(weberrorEntity);
            weberror.inDatabaseBln = true;
            cWeberror.allWebErrors.add(weberror);
        }
    }

    public static  ArrayList<cWeberror> pGetAllWebErrorsByStatus(String pvStatusStr) {

        ArrayList<cWeberror> resulObl = new ArrayList<>();
        List<cWeberrorEntity> WebErrorsFromDatabaseObl =   cWeberror.getWebErrorViewModel().getAllByStatus(pvStatusStr);

        for (cWeberrorEntity weberrorEntity : WebErrorsFromDatabaseObl) {
            cWeberror weberror = new cWeberror(weberrorEntity);
            resulObl.add(weberror);
        }

        return resulObl;
    }



    public static ArrayList<cWeberror> pGetWebErrorsForActivity(String pvActivityStr) {

        ArrayList<cWeberror> resulObl = new ArrayList<>();
        List<cWeberrorEntity> WebErrorsFromDatabaseObl =   cWeberror.getWebErrorViewModel().getAllForActivity(pvActivityStr);

        for (cWeberrorEntity weberrorEntity : WebErrorsFromDatabaseObl) {
            cWeberror weberror = new cWeberror(weberrorEntity);
            resulObl.add(weberror);
        }

        return resulObl;
    }

    public static ArrayList<cWeberror> pGetWebErrorsForActivityAndStatus(String pvActivityStr, String pvStatusStr) {

        ArrayList<cWeberror> resulObl = new ArrayList<>();
        List<cWeberrorEntity> WebErrorsFromDatabaseObl =   cWeberror.getWebErrorViewModel().getAllForActivityAndStatus(pvActivityStr,pvStatusStr);

        for (cWeberrorEntity weberrorEntity : WebErrorsFromDatabaseObl) {
            cWeberror weberror = new cWeberror(weberrorEntity);
            resulObl.add(weberror);
        }

        return resulObl;
    }

    public boolean pInsertInDatabaseBln() {
        cWeberror.getWebErrorViewModel().insert(this.weberrorEntity);
        this.inDatabaseBln = true;

        if (cWeberror.allWebErrors == null){
            cWeberror.allWebErrors = new ArrayList<>();
        }

        cWeberror.allWebErrors .add(this);
        return true;
    }

    public boolean pDeleteFromDatabaseBln() {
        cWeberror.getWebErrorViewModel().delete(this.weberrorEntity);
        this.inDatabaseBln = false;
        cWeberror.allWebErrors.remove(this);
        return true;
    }

    public static void pReportErrorsToFirebaseBln(String pvWebmethodStr) {

        ArrayList<cWeberror> webErrorObl = cWeberror.mGetWebErrorsForWebmethod(pvWebmethodStr);
        if (webErrorObl.size() == 0 ) {
            return;
        }

        for (cWeberror weberror : webErrorObl ) {
            weberror.mReportErrorToFirebaseBln();
        }
    }
    //End Region Public Methods

    //Region Private Methods

    private static ArrayList<cWeberror> mGetWebErrorsForWebmethod(String pvMethodStr) {

        ArrayList<cWeberror> resulObl = new ArrayList<>();
        List<cWeberrorEntity> WebErrorsFromDatabaseObl =   cWeberror.getWebErrorViewModel().getAllForWebMethod(pvMethodStr);

        for (cWeberrorEntity weberrorEntity : WebErrorsFromDatabaseObl) {
            cWeberror weberror = new cWeberror(weberrorEntity);
            resulObl.add(weberror);
        }

        return resulObl;
    }

    private void mReportErrorToFirebaseBln() {

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, FIREBASE_ITEMNAME);
        bundle.putString(FIREBASE_ISSUCCESS, this.isSuccessBln.toString());
        bundle.putString(FIREBASE_ISRESULT, this.isResultBln.toString());
        bundle.putString(FIREBASE_ACTIVITY, this.getActivityStr());
        bundle.putString(FIREBASE_DEVICE, this.getDeviceStr() );
        bundle.putString(FIREBASE_PARAMETERS, this.getParametersStr());
        bundle.putString(FIREBASE_METHOD, this.webmethodStr);
        bundle.putString(FIREBASE_TIMESTAMP, this.getDatetimeStr());
        bundle.putString(FIREBASE_URL, cWebservice.WEBSERVICE_URL);

        try {
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        }
        catch(Exception e) {
            return;
        }

        this.pDeleteFromDatabaseBln();
    }




    //End Region Private Methods


}
