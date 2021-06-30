package ICS.Utils.Scanning;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import java.util.List;

import ICS.cAppExtension;
import SSU_WHS.General.cPublicDefinitions;

import static ICS.cAppExtension.context;

public class cProGlove {
    public boolean pIsProgloveInstalled() {
        PackageManager packageManager = context.getPackageManager();
        if (packageManager == null) {
            return false;
        }
        try {
            packageManager.getPackageInfo(PROGLOVE_PACKAGE_NAME, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
    public void pStartProglove() {
        if (pIsProgloveInstalled()) {
            Intent i = context.getPackageManager().getLaunchIntentForPackage(PROGLOVE_PACKAGE_NAME);
            context.startActivity(i);
        }
    }
    public boolean isProgloveRunning() {
        final ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        final List<ActivityManager.RunningAppProcessInfo> procInfos = activityManager.getRunningAppProcesses();
        if (procInfos != null)
        {
            for (final ActivityManager.RunningAppProcessInfo processInfo : procInfos) {
                if (processInfo.processName.equals(PROGLOVE_PACKAGE_NAME)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void pShowPairGlove() {
        Intent i = new Intent();
        i.setAction(PROGLOVE_CONNECT_ACTION);
        context.sendBroadcast(i);
    }
    public void pSendScreen(String pvTemplateStr, String pvDataStr, Boolean pvRefreshFullBln, int pvDurationInSecondsInt, int pvLightsInt) {

        Intent proglove = new Intent();
        proglove.setAction(PROGLOVE_DISPLAY_ACTION);
        proglove.putExtra(PROGLOVE_DISPLAY_EXTRA_TEMPLATE_ID, pvTemplateStr);
        proglove.putExtra(PROGLOVE_DISPLAY_EXTRA_DATA, pvDataStr);
        proglove.putExtra(PROGLOVE_DISPLAY_EXTRA_SEPARATOR, "|");
        if (pvRefreshFullBln) {
            proglove.putExtra(PROGLOVE_DISPLAY_EXTRA_REFRESH_TYPE, PROGLOVE_DISPLAY_REFRESH_FULL);
        }
        else {
            proglove.putExtra(PROGLOVE_DISPLAY_EXTRA_REFRESH_TYPE, PROGLOVE_DISPLAY_REFRESH_PARTIAL);
        }

        proglove.putExtra(PROGLOVE_DISPLAY_EXTRA_DURATION, pvDurationInSecondsInt * 1000); //0 for permanent screen

        context.sendBroadcast(proglove);

        if (pvLightsInt != 0) {
            this.pLightEmUp(pvLightsInt);
        }
    }

    public void pLightEmUp(int feedBackInt) {
        Intent proglove = new Intent();
        proglove.setAction(PROGLOVE_FEEDBACK_ACTION);
        proglove.putExtra(PROGLOVE_FEEDBACK_EXTRA_SEQUENCE_ID, feedBackInt);
        context.sendBroadcast(proglove);
    }
    public void pDisco() {
        Intent proglove = new Intent();
        proglove.setAction(PROGLOVE_FEEDBACK_ACTION);
        proglove.putExtra(PROGLOVE_FEEDBACK_EXTRA_SEQUENCES_IN_ORDER, new int[]{1, 2, 3, 4, 5});
        context.sendBroadcast(proglove);
    }

    public void mSetOrientation() {
        Intent intent = new Intent();
        ComponentName myComponent  = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            myComponent = ComponentName.createRelative("de.proglove.connect",
                    "de.proglove.coreui.activities.DisplayOrientationActivity");
        }
        intent.setComponent(myComponent);

        context.startActivity(intent);
    }

    public static String PROGLOVE_CONNECT_ACTION = "com.proglove.api.CONNECT";

    public static String PROGLOVE_DISPLAY_ACTION = "com.proglove.api.SET_DISPLAY_SCREEN";
    public static String PROGLOVE_DISPLAY_EXTRA_TEMPLATE_ID = "com.proglove.api.extra.TEMPLATE_ID";
    public static String PROGLOVE_DISPLAY_EXTRA_DATA = "com.proglove.api.extra.DATA";
    public static String PROGLOVE_DISPLAY_EXTRA_SEPARATOR = "com.proglove.api.extra.SEPARATOR";
    public static String PROGLOVE_DISPLAY_EXTRA_REFRESH_TYPE = "com.proglove.api.extra.REFRESH_TYPE";
    public static String PROGLOVE_DISPLAY_EXTRA_DURATION = "com.proglove.api.extra.DURATION";

    public static String PROGLOVE_DISPLAY_REFRESH_FULL = "FULL_REFRESH";
    public static String PROGLOVE_DISPLAY_REFRESH_PARTIAL = "PARTIAL_REFRESH";

    public static String PROGLOVE_DISPLAY_TEMPLATE_1FIELD_1HEADER = "PG1";
    public static String PROGLOVE_DISPLAY_TEMPLATE_1FIELD_0HEADER = "PG1A";
    public static String PROGLOVE_DISPLAY_TEMPLATE_2FIELD_2HEADER = "PG2";
    public static String PROGLOVE_DISPLAY_TEMPLATE_3FIELD_3HEADER = "PG3";
    public static String PROGLOVE_DISPLAY_TEMPLATE_1FIELD_CHECKBOX = "PG1C";
    public static String PROGLOVE_DISPLAY_TEMPLATE_1TITLE_1FIELD_CHECKBOX = "PG2C";
    public static String PROGLOVE_DISPLAY_TEMPLATE_1FIELD_ERROR = "PG1E";
    public static String PROGLOVE_DISPLAY_TEMPLATE_1TITLE_1FIELD_ERROR = "PG2E";
    public static String PROGLOVE_DISPLAY_TEMPLATE_1FIELD_ALERT = "PG1I";
    public static String PROGLOVE_DISPLAY_TEMPLATE_1TITLE_1FIELD_ALERT = "PG2I";

    public static String PROGLOVE_FEEDBACK_ACTION = "com.proglove.api.PLAY_FEEDBACK";
    public static String PROGLOVE_FEEDBACK_EXTRA_SEQUENCE_ID = "com.proglove.api.extra.FEEDBACK_SEQUENCE_ID";
    public static String PROGLOVE_FEEDBACK_EXTRA_SEQUENCES_IN_ORDER = "com.proglove.api.extra.FEEDBACK_SEQUENCES_IN_ORDER";
    public static Integer PROGLOVE_FEEDBACK_POSITIVE = 1;
    public static Integer PROGLOVE_FEEDBACK_NEGATIVE = 2;
    public static Integer PROGLOVE_FEEDBACK_YELLOW = 3;
    public static Integer PROGLOVE_FEEDBACK_PURPLE = 4;
    public static Integer PROGLOVE_FEEDBACK_CYAN = 5;

    public static String PROGLOVE_PACKAGE_NAME = "de.proglove.connect";
    public static String PROGLOVE_INSTALL_URL = "https://scandroid.icsvertex.nl/proglove_latest.apk";
}
