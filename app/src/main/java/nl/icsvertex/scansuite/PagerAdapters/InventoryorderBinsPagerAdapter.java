package nl.icsvertex.scansuite.PagerAdapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;

import ICS.cAppExtension;
import nl.icsvertex.scansuite.Fragments.Inventory.InventoryBinsDoneFragment;
import nl.icsvertex.scansuite.Fragments.Inventory.InventoryBinsToDoFragment;
import nl.icsvertex.scansuite.Fragments.Inventory.InventoryBinsTotalFragment;


public class InventoryorderBinsPagerAdapter extends FragmentStatePagerAdapter {

    //Region Private Properties
    private int numberOfTabsInt;
    //End Region Private Properties

    public InventoryorderBinsPagerAdapter(int pvNumberOfTabsInt) {
        super(cAppExtension.fragmentManager);
        this.numberOfTabsInt = pvNumberOfTabsInt;
    }

    @Override
    public Fragment getItem(int pvPositionInt) {
        switch(pvPositionInt) {
            case 0:
                InventoryBinsToDoFragment tab1 = new InventoryBinsToDoFragment();
                return tab1;
            case 1:
                InventoryBinsDoneFragment tab2 = new InventoryBinsDoneFragment();
                return tab2;
            case 2:
                InventoryBinsTotalFragment tab3 = new InventoryBinsTotalFragment();
                return tab3;
            default:
                return null;
        }
    }
    @Override
    public int getCount() {return numberOfTabsInt;}
}
