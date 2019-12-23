package ICS.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Build;
import android.text.format.Formatter;

import java.io.File;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ICS.cAppExtension;
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

    public static String getSerialnumberStr( )
    {
        return SERIAL;
    }

    public static String getAppVersion ( ) { return BuildConfig.VERSION_NAME; }

    public static String getInstalldate() {

        long installDate;
        try {
            installDate = cAppExtension.context.getPackageManager().getPackageInfo(cAppExtension.context.getPackageName(), 0).firstInstallTime;
        } catch (PackageManager.NameNotFoundException e) {
            return cAppExtension.context.getString(R.string.application_unknown_install_date);
        }
        String dateString = new SimpleDateFormat("dd-MM-yyyy HH:mm").format(new Date(installDate));
        return dateString;
    }
    public static String getLastUpdateDate() {
        long updateDate;
        PackageManager packageManager = cAppExtension.context.getPackageManager();
        if (packageManager != null) {
            try {
                ApplicationInfo appInfo = packageManager.getApplicationInfo(cAppExtension.context.getPackageName(), 0);
                String appFile = appInfo.sourceDir;
                updateDate = new File(appFile).lastModified();
                String dateString = new SimpleDateFormat("dd-MM-yyyy HH:mm").format(new Date(updateDate));
                return dateString;
            } catch (PackageManager.NameNotFoundException e) {
                return cAppExtension.context.getString(R.string.application_unknown_update_date);
            }
        }
        else {
            return cAppExtension.context.getString(R.string.application_unknown_update_date);
        }
    }


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

    public static String getFriendlyVersionName (){
        int code = Build.VERSION.SDK_INT;

        switch (code) {
            case 0: {
                return cAppExtension.context.getString(R.string.android_no_codename);
            }
            case 1: {
                return cAppExtension.context.getString(R.string.android_no_codename);
            }
            case 2: {
                return cAppExtension.context.getString(R.string.android_no_codename);
            }
            case 3: {
                return "Cupcake";
            }
            case 4: {
                return "Donut";
            }
            case 5: {
                return "Eclair";
            }
            case 6: {
                return "Eclair";
            }
            case 7: {
                return "Eclair";
            }
            case 8: {
                return "Froyo";
            }
            case 9: {
                return "Gingerbread";
            }
            case 10: {
                return "Gingerbread";
            }
            case 11: {
                return "Honeycomb";
            }
            case 12: {
                return "Honeycomb";
            }
            case 13: {
                return "Honeycomb";
            }
            case 14: {
                return "Ice Cream Sandwich";
            }
            case 15: {
                return "Ice Cream Sandwich";
            }
            case 16: {
                return "Jelly Bean";
            }
            case 17: {
                return "Jelly Bean";
            }
            case 18: {
                return "Jelly Bean";
            }
            case 19: {
                return "KitKat";
            }
            case 20: {
                return "KitKat";
            }
            case 21: {
                return "Lollipop";
            }
            case 22: {
                return "Lollipop";
            }
            case 23: {
                return "Marshmallow";
            }
            case 24: {
                return "Nougat";
            }
            case 25: {
                return "Nougat";
            }
            case 26: {
                return "Oreo";
            }
            case 27: {
                return "Oreo";
            }
            case 28: {
                return "Pie";
            }
            case 29: {
                return "Android10";
            }
            default: {
                return "Android";
            }
        }
    }


    public static int getBatteryChargePct() {
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = cAppExtension.context.registerReceiver(null, ifilter);
        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        float batteryPct = (level / (float)scale) *100;

        return Math.round(batteryPct);
    }

    public static boolean isCharging() {
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = cAppExtension.context.registerReceiver(null, ifilter);
        // Are we charging / charged?
        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL;

        // How are we charging?
        int chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
        boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;
        return isCharging;
    }
    public static boolean isChargingUSB() {
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = cAppExtension.context.registerReceiver(null, ifilter);
        // How are we charging?
        int chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
        return usbCharge;
    }
    public static boolean isChargingAC() {
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = cAppExtension.context.registerReceiver(null, ifilter);
        // How are we charging?
        int chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;
        return acCharge;
    }
    public static String getChargingStatusString() {
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = cAppExtension.context.registerReceiver(null, ifilter);
        // Are we charging / charged?
        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL;

        // How are we charging?
        int chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
        boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;
        boolean wirelessCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_WIRELESS;


        if (!isCharging) {
            return cAppExtension.context.getString(R.string.battery_isnotcharging);
        }
        if (usbCharge) {
            return cAppExtension.context.getString(R.string.battery_ischarging_over_parameter1, cAppExtension.context.getString(R.string.battery_usb));
        }
        if (acCharge) {
            return cAppExtension.context.getString(R.string.battery_ischarging_over_parameter1, cAppExtension.context.getString(R.string.battery_ac));
        }
        if (wirelessCharge) {
            return cAppExtension.context.getString(R.string.battery_ischarging_over_parameter1, cAppExtension.context.getString(R.string.battery_wireless));
        }

        return cAppExtension.context.getString(R.string.battery_unknownnotcharging);

    }
}