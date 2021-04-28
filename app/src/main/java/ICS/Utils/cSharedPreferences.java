package ICS.Utils;

import android.content.SharedPreferences;

import ICS.cAppExtension;
import SSU_WHS.General.cPublicDefinitions;
import nl.icsvertex.scansuite.R;

import static android.content.Context.MODE_PRIVATE;

public class cSharedPreferences {

    public static Boolean userFilterBln(){
       return cSharedPreferences.mGetSharedPreferenceBoolean(cAppExtension.context.getString(R.string.filter_orderlines_enable_key), false);
    }

    public static Boolean showProcessedWaitBln(){
        return cSharedPreferences.mGetSharedPreferenceBoolean(cAppExtension.context.getString(R.string.filter_orderlines_processing_key), false);
    }

    public static Boolean showSingleArticlesBln(){
        return cSharedPreferences.mGetSharedPreferenceBoolean(cAppExtension.context.getString(R.string.filter_orderlines_singlearticles_key), false);
    }

    public static Boolean showAssignedToMeBln(){
        return cSharedPreferences.mGetSharedPreferenceBoolean(cAppExtension.context.getString(R.string.filter_orderlines_my_orders_key), false);
    }

    public static Boolean showAssignedToOthersBln(){
        return cSharedPreferences.mGetSharedPreferenceBoolean(cAppExtension.context.getString(R.string.filter_orderlines_their_orders_key), false);
    }

    public static Boolean showNotAssignedBln(){
        return cSharedPreferences.mGetSharedPreferenceBoolean(cAppExtension.context.getString(R.string.filter_orderlines_nobodys_orders_key), false);
    }

    public static String getSerialNumerStr(){
        return cSharedPreferences.mGetSharedPreferenceString(cAppExtension.context.getString(R.string.shared_preference_serial_key), "");
    }

    public static void  setSerialNumerStr(String pvSerialStr){
        pSetSharedPreferenceString(cAppExtension.context.getString(R.string.shared_preference_serial_key),pvSerialStr);
    }

    public static void  setDarkModusBln(Boolean pvDarkModus){
        mSetSharedPreferenceBoolean(cAppExtension.context.getString(R.string.shared_preference_dark_mode), pvDarkModus);
    }

    public static boolean getDarkModusBln(){
 return cSharedPreferences.mGetSharedPreferenceBoolean(cAppExtension.context.getString(R.string.shared_preference_dark_mode),false);
    }

    private static SharedPreferences gSharedPreferences;

    private static SharedPreferences getSharedPreferences() {
        if (gSharedPreferences == null) {
            gSharedPreferences = cAppExtension.context.getSharedPreferences(cPublicDefinitions.SHAREDPREFERENCE_FILE, MODE_PRIVATE);
        }
        return gSharedPreferences;
    }


    private static Boolean mGetSharedPreferenceBoolean(String pv_KeyStr, Boolean pvDefaultValueBln) {
        //SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(cAppExtension.context);
        return cSharedPreferences.getSharedPreferences().getBoolean(pv_KeyStr, pvDefaultValueBln);
    }

    public static void mSetSharedPreferenceBoolean(String pvKeyStr, boolean pvValueBln) {
        SharedPreferences.Editor editor =  cSharedPreferences.getSharedPreferences().edit();
        editor.putBoolean(pvKeyStr, pvValueBln);
        editor.apply();
    }

    private static String mGetSharedPreferenceString(String pvKeyStr, String pvDefaultValueStr) {
        return cSharedPreferences.getSharedPreferences().getString(pvKeyStr, pvDefaultValueStr);
    }

    public static void pSetSharedPreferenceString(String pvKeyStr, String pvValueStr) {
        SharedPreferences.Editor editor =  cSharedPreferences.getSharedPreferences().edit();
        editor.putString(pvKeyStr, pvValueStr);
        editor.apply();
    }


}
