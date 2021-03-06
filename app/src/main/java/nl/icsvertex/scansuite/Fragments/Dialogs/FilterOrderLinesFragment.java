package nl.icsvertex.scansuite.Fragments.Dialogs;


import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import nl.icsvertex.scansuite.Activities.IntakeAndReceive.IntakeAndReceiveSelectActivity;
import nl.icsvertex.scansuite.Activities.Inventory.InventoryorderSelectActivity;
import nl.icsvertex.scansuite.Activities.Move.MoveorderSelectActivity;
import nl.icsvertex.scansuite.Activities.PackAndShip.PackAndShipSelectActivity;
import nl.icsvertex.scansuite.Activities.Pick.PickorderSelectActivity;
import nl.icsvertex.scansuite.Activities.Returns.ReturnorderSelectActivity;
import nl.icsvertex.scansuite.Activities.Ship.ShiporderSelectActivity;
import nl.icsvertex.scansuite.Activities.Sort.SortorderSelectActivity;
import nl.icsvertex.scansuite.Activities.Store.StoreorderSelectActivity;
import nl.icsvertex.scansuite.R;


public class FilterOrderLinesFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {


        if (cAppExtension.context instanceof PickorderSelectActivity) {

            if (PickorderSelectActivity.currentModusEnu == PickorderSelectActivity.ModusEnu.NORMAL) {
                setPreferencesFromResource(R.xml.pick_filter, rootKey);
            }
            else
            {
                setPreferencesFromResource(R.xml.combine_pick_filter, rootKey);
            }

        }

        if (cAppExtension.context instanceof SortorderSelectActivity) {
            setPreferencesFromResource(R.xml.sort_filter, rootKey);
        }

        if (cAppExtension.context instanceof ShiporderSelectActivity) {
            setPreferencesFromResource(R.xml.ship_filter, rootKey);
        }
        if (cAppExtension.context instanceof StoreorderSelectActivity) {
            setPreferencesFromResource(R.xml.store_filter, rootKey);
        }

        if (cAppExtension.context instanceof InventoryorderSelectActivity) {
            setPreferencesFromResource(R.xml.inventory_filter, rootKey);
        }

        if (cAppExtension.context instanceof IntakeAndReceiveSelectActivity) {
            setPreferencesFromResource(R.xml.intake_filter, rootKey);
        }

        if (cAppExtension.context instanceof ReturnorderSelectActivity) {
            setPreferencesFromResource(R.xml.return_filter, rootKey);
        }

        if (cAppExtension.context instanceof MoveorderSelectActivity) {
            setPreferencesFromResource(R.xml.move_filter, rootKey);
        }

        if (cAppExtension.context instanceof PackAndShipSelectActivity) {
            setPreferencesFromResource(R.xml.pack_and_ship_filter, rootKey);
        }


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Preference applyButton = findPreference(getString(R.string.filter_orderlines_apply_key));
        if (applyButton != null) {
            applyButton.setOnPreferenceClickListener(preference -> true);
        }
        cUserInterface.pEnableScanner();
    }

// todo: kijken of dit niet alles en zijn moeder kapot maakt

//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        SharedPreferences.OnSharedPreferenceChangeListener spChanged = (sharedPreferences, key) -> {
//
//        };
//        PreferenceManager.getDefaultSharedPreferences(cAppExtension.activity.getApplicationContext()).registerOnSharedPreferenceChangeListener(spChanged);
//    }
}
