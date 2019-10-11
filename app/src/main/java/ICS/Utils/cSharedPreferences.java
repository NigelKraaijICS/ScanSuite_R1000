package ICS.Utils;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import ICS.cAppExtension;
import SSU_WHS.General.cPublicDefinitions;
import nl.icsvertex.scansuite.R;

import static android.content.Context.MODE_PRIVATE;

public class cSharedPreferences {

    public static Boolean userFilterBln(){
       return cSharedPreferences.mGetDefaultSharedPreferenceBoolean(cAppExtension.context.getString(R.string.filter_orderlines_enable_key), false);
    }

    public static Boolean showProcessedWaitBln(){
        return cSharedPreferences.mGetDefaultSharedPreferenceBoolean(cAppExtension.context.getString(R.string.filter_orderlines_processing_key), false);
    }

    public static Boolean showSingleArticlesBln(){
        return cSharedPreferences.mGetDefaultSharedPreferenceBoolean(cAppExtension.context.getString(R.string.filter_orderlines_singlearticles_key), false);
    }

    public static Boolean showAssignedToMeBln(){
        return cSharedPreferences.mGetDefaultSharedPreferenceBoolean(cAppExtension.context.getString(R.string.filter_orderlines_my_orders_key), false);
    }

    public static Boolean showAssignedToOthersBln(){
        return cSharedPreferences.mGetDefaultSharedPreferenceBoolean(cAppExtension.context.getString(R.string.filter_orderlines_their_orders_key), false);
    }

    public static Boolean showNotAssignedBln(){
        return cSharedPreferences.mGetDefaultSharedPreferenceBoolean(cAppExtension.context.getString(R.string.filter_orderlines_nobodys_orders_key), false);
    }

    private static SharedPreferences gSharedPreferences;

    private static SharedPreferences getSharedPreferences() {
        if (gSharedPreferences == null) {
            gSharedPreferences = cAppExtension.context.getSharedPreferences(cPublicDefinitions.SHAREDPREFERENCE_FILE, MODE_PRIVATE);
        }
        return gSharedPreferences;
    }

    public static void pSetSharedPreferenceString(String pvKeyStr, String pvValueStr) {
        SharedPreferences.Editor editor =  cSharedPreferences.getSharedPreferences().edit();
        editor.putString(pvKeyStr, pvValueStr);
        editor.apply();
    }
    private static String mGetSharedPreferenceString(String pvKeyStr, String pvDefaultValueStr) {

        return cSharedPreferences.getSharedPreferences().getString(pvKeyStr, pvDefaultValueStr);
    }
    private static Boolean mGetDefaultSharedPreferenceBoolean(String pv_KeyStr, Boolean pv_DefaultValueBln) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(cAppExtension.context);
        return sharedPreferences.getBoolean(pv_KeyStr, pv_DefaultValueBln);
    }
}
