package ICS.Utils;

import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

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

    public static String getSerialNumerStr(){
        return cSharedPreferences.pGetSharedPreferenceString(cAppExtension.context.getString(R.string.shared_preference_serial_key), "");
    }

    public static void  setSerialNumerStr(String pvSerialStr){
        pSetSharedPreferenceString(cAppExtension.context.getString(R.string.shared_preference_serial_key),pvSerialStr);
    }

    public static void  setDarkModusBln(Boolean pvDarkModus){
        pSetSharedPreferenceBoolean(cAppExtension.context.getString(R.string.shared_preference_dark_mode), pvDarkModus);
    }

    public static boolean getDarkModusBln(){
 return cSharedPreferences.pGetSharedPreferenceBoolean(cAppExtension.context.getString(R.string.shared_preference_dark_mode),false);
    }

    private static SharedPreferences gSharedPreferences;

    private static SharedPreferences getSharedPreferences() {
        if (gSharedPreferences == null) {
            gSharedPreferences = cAppExtension.context.getSharedPreferences(cPublicDefinitions.SHAREDPREFERENCE_FILE, MODE_PRIVATE);
        }
        return gSharedPreferences;
    }


    public static Boolean pGetSharedPreferenceBoolean(String pv_KeyStr, Boolean pvDefaultValueBln) {
        return cSharedPreferences.getSharedPreferences().getBoolean(pv_KeyStr, pvDefaultValueBln);
    }

    public static void pSetSharedPreferenceBoolean(String pvKeyStr, boolean pvValueBln) {
        SharedPreferences.Editor editor =  cSharedPreferences.getSharedPreferences().edit();
        editor.putBoolean(pvKeyStr, pvValueBln);
        editor.apply();
    }

    public static String pGetSharedPreferenceString(String pvKeyStr, String pvDefaultValueStr) {
        return cSharedPreferences.getSharedPreferences().getString(pvKeyStr, pvDefaultValueStr);
    }

    public static void pSetSharedPreferenceString(String pvKeyStr, String pvValueStr) {
        SharedPreferences.Editor editor =  cSharedPreferences.getSharedPreferences().edit();
        editor.putString(pvKeyStr, pvValueStr);
        editor.apply();
    }

    private static Boolean mGetDefaultSharedPreferenceBoolean(String pvKeyStr, Boolean pvDefaultValueBln) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(cAppExtension.context);
        return sharedPreferences.getBoolean(pvKeyStr, pvDefaultValueBln);
    }


}
