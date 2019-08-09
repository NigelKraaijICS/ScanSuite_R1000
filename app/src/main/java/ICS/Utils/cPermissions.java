package ICS.Utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

import SSU_WHS.General.cAppExtension;

public class cPermissions {
    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

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
