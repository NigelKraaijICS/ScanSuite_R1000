package ICS.Weberror;

import android.content.Context;
import android.support.v4.app.FragmentManager;

import java.util.List;

import ICS.Utils.cUserInterface;

public class cWeberror {
    public enum WeberrorStatusEnu {
        NEW,
        SHOWNNOTSEND,
        SENDING,
        SENT
    }

    public static final String FIREBASE_ITEMNAME = "ics scansuite webservice event";
    public static final String FIREBASE_ISSUCCESS = "issuccess";
    public static final String FIREBASE_ISRESULT = "isresult";
    public static final String FIREBASE_ACTIVITY = "activity";
    public static final String FIREBASE_MESSAGE = "message";
    public static final String FIREBASE_DEVICE = "device";
    public static final String FIREBASE_PARAMETERS = "parameters";
    public static final String FIREBASE_METHOD = "method";
    public static final String FIREBASE_TIMESTAMP = "timestamp";
    public static final String FIREBASE_URL = "url";

    public static final String FIREBASE_KEY_LANGUAGE = "language";
    public static final String FIREBASE_KEY_SSID = "ssid";
    public static final String FIREBASE_KEY_IP = "ipaddress";

    public static void mHandleWeberrors(List<cWeberrorEntity> weberrorEntities) {
        if (weberrorEntities != null && weberrorEntities.size() > 0) {
            boolean isSuccess = true;
            for (cWeberrorEntity weberrorEntity : weberrorEntities) {
                if (!weberrorEntity.getIssucess()) {
                    isSuccess = false;
                }
            }
            if (!isSuccess) {
                cUserInterface.checkAndCloseOpenDialogs();
                cUserInterface.doWebserviceError(weberrorEntities, true, true);
            }

        }
    }

}
