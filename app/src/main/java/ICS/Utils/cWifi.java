package ICS.Utils;

import android.content.Context;
import android.net.wifi.WifiManager;

import ICS.cAppExtension;

public class cWifi {
    public static void toggleWiFi() {
        WifiManager wifiManager = (WifiManager) cAppExtension.context.getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(false);
        wifiManager.setWifiEnabled(true);
    }
}
