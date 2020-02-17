package ICS.Utils;


import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;

import ICS.cAppExtension;

public class cPermissions {

    public static void checkPermissions() {
        PackageInfo info;
        try {
            info = cAppExtension.context.getPackageManager().getPackageInfo(cAppExtension.context.getPackageName(), PackageManager.GET_PERMISSIONS);
            int PERMISSION_ALL = 1;
            ActivityCompat.requestPermissions(cAppExtension.activity, info.requestedPermissions, PERMISSION_ALL);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}
