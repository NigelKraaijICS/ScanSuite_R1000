package ICS.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import SSU_WHS.General.cAppExtension;

public class cConnection {
    public enum connectionType {
        NONE,
        WIFI,
        ETHERNET,
        MOBILEDATA,
        UNKNOWN
    }

    public static Boolean isGooglePingableBln() {
        try {
            Process p1 = java.lang.Runtime.getRuntime().exec("ping -c 1 8.8.8.8");
            int returnVal = p1.waitFor();
            boolean reachable = (returnVal==0);
            return reachable;
        } catch (Exception e) {
            return false;
        }
    }
    public static Boolean isInternetConnectedBln() {
        Boolean resultBln = false;
        ConnectivityManager cm = (ConnectivityManager) cAppExtension.context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            resultBln = activeNetwork != null && activeNetwork.isConnected();
        }
        return resultBln;
    }

    public static connectionType getCurrentConnectionType() {
        ConnectivityManager cm = (ConnectivityManager) cAppExtension.context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
                Boolean isConnectedBln;
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                //are we connected at all?
                isConnectedBln = activeNetwork != null && activeNetwork.isConnected();
                if (!isConnectedBln) {
                    return connectionType.NONE;
                }
                if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                    return connectionType.WIFI;
                }
                if (activeNetwork.getType() == ConnectivityManager.TYPE_ETHERNET) {
                    return connectionType.ETHERNET;
                }
                if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE || activeNetwork.getType() == ConnectivityManager.TYPE_WIMAX) {
                    return connectionType.MOBILEDATA;
                }
                //none of the above
                return connectionType.UNKNOWN;
            }
        return connectionType.UNKNOWN;
    }

}
