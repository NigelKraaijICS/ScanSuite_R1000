package nl.icsvertex.scansuite.PagerAdapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;

import ICS.cAppExtension;
import nl.icsvertex.scansuite.Fragments.Inventory.InventoryBinsDoneFragment;
import nl.icsvertex.scansuite.Fragments.Inventory.InventoryBinsToDoFragment;
import nl.icsvertex.scansuite.Fragments.Inventory.InventoryBinsTotalFragment;


public class InventoryorderBinsPagerAdapter extends FragmentPagerAdapter {

    //Region Private Properties
    private int numberOfTabsInt;
    //End Region Private Properties

    public InventoryorderBinsPagerAdapter(int pvNumberOfTabsInt) {
        super(cAppExtension.fragmentManager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.numberOfTabsInt = pvNumberOfTabsInt;
    }

    @NonNull
    @Override
    public Fragment getItem(int pvPositionInt) {
        switch(pvPositionInt) {
            case 0:
                return new InventoryBinsToDoFragment();
            case 1:
                return new InventoryBinsDoneFragment();
            case 2:
                return  new InventoryBinsTotalFragment();
            default:
                return cAppExtension.dialogFragment;
        }
    }
    @Override
    public int getCount() {return numberOfTabsInt;}
}
