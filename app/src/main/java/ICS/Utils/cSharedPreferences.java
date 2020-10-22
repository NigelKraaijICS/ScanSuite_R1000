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

    public static String getSerialNumerStr(){
        return cSharedPreferences.mGetSharedPreferenceString(cAppExtension.context.getString(R.string.shared_preference_serial_key), "");
    }

    public static void  setSerialNumerStr(String pvSerialStr){
        mSetSharedPreferenceString(cAppExtension.context.getString(R.string.shared_preference_serial_key),pvSerialStr);
    }

    private static SharedPreferences gSharedPreferences;

    private static SharedPreferences getSharedPreferences() {
        if (gSharedPreferences == null) {
            gSharedPreferences = cAppExtension.context.getSharedPreferences(cPublicDefinitions.SHAREDPREFERENCE_FILE, MODE_PRIVATE);
        }
        return gSharedPreferences;
    }


    private static Boolean mGetDefaultSharedPreferenceBoolean(String pv_KeyStr, Boolean pvDefaultValueStr) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(cAppExtension.context);
        return sharedPreferences.getBoolean(pv_KeyStr, pvDefaultValueStr);
    }

    private static String mGetSharedPreferenceString(String pvKeyStr, String pvDefaultValueStr) {
        return cSharedPreferences.getSharedPreferences().getString(pvKeyStr, pvDefaultValueStr);
    }

    public static void mSetSharedPreferenceString(String pvKeyStr, String pvValueStr) {
        SharedPreferences.Editor editor =  cSharedPreferences.getSharedPreferences().edit();
        editor.putString(pvKeyStr, pvValueStr);
        editor.apply();
    }
}
