package ICS.Utils;

import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;

import SSU_WHS.General.cAppExtension;
import SSU_WHS.General.cPublicDefinitions;

import static android.content.Context.MODE_PRIVATE;

public class cSharedPreferences {
    public static void setSharedPreferenceString(String pv_KeyStr, String pv_ValueStr) {
        SharedPreferences l_sharedPref = cAppExtension.context.getSharedPreferences(cPublicDefinitions.SHAREDPREFERENCE_FILE, MODE_PRIVATE);
        SharedPreferences.Editor l_editorEdt = l_sharedPref.edit();
        l_editorEdt.putString(pv_KeyStr, pv_ValueStr);
        //commit is direct
        //l_editorEdt.commit();
        //apply is asynch
        l_editorEdt.apply();
    }
    public static String getSharedPreferenceString(String pv_KeyStr, String pv_DefaultValueStr) {
        SharedPreferences l_sharedPref = cAppExtension.context.getSharedPreferences(cPublicDefinitions.SHAREDPREFERENCE_FILE, MODE_PRIVATE);
        String l_ResultStr = l_sharedPref.getString(pv_KeyStr, pv_DefaultValueStr);
        return l_ResultStr;
    }
    public static void setSharedPreferenceBoolean(String pv_KeyStr, Boolean pv_ValueBln) {
        SharedPreferences l_sharedPref = cAppExtension.context.getSharedPreferences(cPublicDefinitions.SHAREDPREFERENCE_FILE, MODE_PRIVATE);
        SharedPreferences.Editor l_editorEdt = l_sharedPref.edit();
        l_editorEdt.putBoolean(pv_KeyStr, pv_ValueBln);
        //commit is direct
        //l_editorEdt.commit();
        //apply is asynch
        l_editorEdt.apply();
    }
    public static Boolean getDefaultSharedPreferenceBoolean(String pv_KeyStr, Boolean pv_DefaultValueBln) {
        SharedPreferences l_sharedPref = PreferenceManager.getDefaultSharedPreferences(cAppExtension.context);
        Boolean l_ResultBln = l_sharedPref.getBoolean(pv_KeyStr, pv_DefaultValueBln);
        return l_ResultBln;
    }
}
