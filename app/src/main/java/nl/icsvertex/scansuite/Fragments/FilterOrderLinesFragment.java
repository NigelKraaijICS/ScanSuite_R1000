package nl.icsvertex.scansuite.Fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import ICS.cAppExtension;
import nl.icsvertex.scansuite.Activities.inventory.InventoryorderSelectActivity;
import nl.icsvertex.scansuite.Activities.pick.PickorderSelectActivity;
import nl.icsvertex.scansuite.Activities.ship.ShiporderSelectActivity;
import nl.icsvertex.scansuite.Activities.sort.SortorderSelectActivity;
import nl.icsvertex.scansuite.R;


public class FilterOrderLinesFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {


        if (cAppExtension.context instanceof PickorderSelectActivity) {
            setPreferencesFromResource(R.xml.filter_orders, rootKey);
        }

        if (cAppExtension.context instanceof SortorderSelectActivity) {
            setPreferencesFromResource(R.xml.filter_orders, rootKey);
        }

        if (cAppExtension.context instanceof ShiporderSelectActivity) {
            setPreferencesFromResource(R.xml.filter_orders, rootKey);
        }

        if (cAppExtension.context instanceof InventoryorderSelectActivity) {
            setPreferencesFromResource(R.xml.filter_inventoryorders, rootKey);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Preference applyButton = findPreference(getString(R.string.filter_orderlines_apply_key));
        applyButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
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
        PreferenceManager.getDefaultSharedPreferences(cAppExtension.activity.getApplicationContext()).registerOnSharedPreferenceChangeListener(spChanged);
    }
}
