package ICS.Utils;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.text.format.Formatter;

import java.lang.reflect.Field;
import java.util.Locale;

import SSU_WHS.cAppExtension;
import nl.icsvertex.scansuite.BuildConfig;
import nl.icsvertex.scansuite.R;

import static android.content.Context.WIFI_SERVICE;
import static android.os.Build.SERIAL;

public class cDeviceInfo {
    public static String getDeviceManufacturer ( ) {
        return Build.MANUFACTURER;
    }

    public static String getDeviceBrand ( ) {
        //some terminals say they're from "MotorolaSolutionsInc", weirdo's.
        if (Build.BRAND.toUpperCase().contains("MOTOROLA")) {
            return "MOTOROLA";
        }
        return Build.BRAND;
    }

    public static String getDeviceModel ( )
    {
        return Build.MODEL;
    }

    public static String getSerialnumber ( )
    {
        return SERIAL;
    }

    public static String getAppVersion ( ) { return BuildConfig.VERSION_NAME; }

    public static String getAndroidBuildVersion ( ) {
        return Build.VERSION.RELEASE;
    }

    public static String getConfiguration ( ) { return ""; }

    public static String getIpAddress () {
        WifiManager wm = (WifiManager) cAppExtension.context.getApplicationContext().getSystemService(WIFI_SERVICE);
        String ip;
        if (wm != null) {
            ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
        }
        else {
            ip = cAppExtension.context.getString(R.string.error_couldnt_get_ip);
        }
        return ip;
    }

    public static String getSSID () {
        WifiManager wm = (WifiManager) cAppExtension.context.getApplicationContext().getSystemService(WIFI_SERVICE);
        WifiInfo wi;
        String ssid;
        if (wm != null) {
            wi = wm.getConnectionInfo();
            ssid = wi.getSSID();
        }
        else {
            ssid = cAppExtension.context.getString(R.string.error_couldnt_get_ssid);
        }
        return ssid;
    }

    public static String getFriendlyLanguage() {
        return Locale.getDefault().getDisplayLanguage();
    }

    public static String getSDKCodeName() {
        String codeName = "";
        Field[] fields = Build.VERSION_CODES.class.getFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            int fieldValue = -1;

            try {
                fieldValue = field.getInt(new Object());
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

            if (fieldValue == Build.VERSION.SDK_INT) {
                codeName = fieldName;
            }
        }
        return codeName;
    }
}