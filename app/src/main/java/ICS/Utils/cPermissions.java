package ICS.Utils;


import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;

import ICS.cAppExtension;

public class cPermissions {

    public static void checkPermissions(Context pvContext, Activity pvActivity) {
        PackageInfo info;
        try {
            info = pvContext.getPackageManager().getPackageInfo(pvContext.getPackageName(), PackageManager.GET_PERMISSIONS);
            int PERMISSION_ALL = 1;
            ActivityCompat.requestPermissions(pvActivity, info.requestedPermissions, PERMISSION_ALL);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}
