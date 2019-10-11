package nl.icsvertex.scansuite.PagerAdapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;

import ICS.cAppExtension;
import nl.icsvertex.scansuite.Fragments.ship.ShiporderLinesShippedFragment;
import nl.icsvertex.scansuite.Fragments.ship.ShiporderLinesToShipFragment;
import nl.icsvertex.scansuite.Fragments.ship.ShiporderLinesTotalFragment;

public class ShiporderLinesPagerAdapter extends FragmentStatePagerAdapter {

    //Region Private Properties
    private int numberOfTabsInt;
    //End Region Private Properties

    //Region Constructor
    public ShiporderLinesPagerAdapter(int pvNumberOfTabsInt) {
        super(cAppExtension.fragmentManager);
        this.numberOfTabsInt = pvNumberOfTabsInt;
    }
    //End Region Constructor

    //Region Default Methods

    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0:
                return new ShiporderLinesToShipFragment();
            case 1:
                return new ShiporderLinesShippedFragment();
            case 2:
                return new ShiporderLinesTotalFragment();
            default:
                return null;
        }
    }
    @Override
    public int getCount() {return numberOfTabsInt;}

    //End Region Default Methods

}
