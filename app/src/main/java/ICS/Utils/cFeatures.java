package ICS.Utils;

import android.content.Context;
import android.content.pm.PackageManager;

public class cFeatures {
    public static boolean hasFeatures(Context context, String... features) {
        if (context != null && features != null) {
            PackageManager pm = context.getPackageManager();
            for (String feature : features) {
                if(!pm.hasSystemFeature(feature)) {
                    return false;
                }
            }
        }
        return true;
    }
}
