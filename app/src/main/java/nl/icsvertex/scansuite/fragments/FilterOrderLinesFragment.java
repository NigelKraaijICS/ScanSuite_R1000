package nl.icsvertex.scansuite.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import nl.icsvertex.scansuite.activities.pick.PickorderSelectActivity;
import nl.icsvertex.scansuite.R;


public class FilterOrderLinesFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.filter_orders, rootKey);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Preference applyButton = findPreference(getString(R.string.filter_orderlines_apply_key));
        applyButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Context callerContext= getActivity();

                if (callerContext instanceof PickorderSelectActivity) {

                    //todo:put this back
//                    ((PickorderSelectActivity)callerContext).mApplyFilter();
                }
                return true;
            }
        });
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SharedPreferences.OnSharedPreferenceChangeListener spChanged = new
                SharedPreferences.OnSharedPreferenceChangeListener() {
                    @Override
                    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                                          String key) {
                        if (key.equalsIgnoreCase(getString(R.string.filter_orderlines_enable_key))) {

                        }
                    }
                };
        PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext()).registerOnSharedPreferenceChangeListener(spChanged);
    }
}
