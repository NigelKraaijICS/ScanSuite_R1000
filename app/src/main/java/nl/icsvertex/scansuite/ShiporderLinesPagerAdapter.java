package nl.icsvertex.scansuite;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import nl.icsvertex.scansuite.fragments.ship.ShiporderLinesShippedFragment;
import nl.icsvertex.scansuite.fragments.ship.ShiporderLinesToShipFragment;
import nl.icsvertex.scansuite.fragments.ship.ShiporderLinesTotalFragment;

public class ShiporderLinesPagerAdapter extends FragmentStatePagerAdapter {
    int l_numberOfTabsInt;

    public ShiporderLinesPagerAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.l_numberOfTabsInt = numOfTabs;
    }
    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0:
                ShiporderLinesToShipFragment tab1 = new ShiporderLinesToShipFragment();
                return tab1;
            case 1:
                ShiporderLinesShippedFragment tab2 = new ShiporderLinesShippedFragment();
                return tab2;
            case 2:
                ShiporderLinesTotalFragment tab3 = new ShiporderLinesTotalFragment();
                return tab3;
            default:
                return null;
        }
    }
    @Override
    public int getCount() {return l_numberOfTabsInt;}
}
