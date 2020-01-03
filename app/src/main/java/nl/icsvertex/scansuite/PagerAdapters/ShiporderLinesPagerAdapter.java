package nl.icsvertex.scansuite.PagerAdapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;

import ICS.cAppExtension;
import nl.icsvertex.scansuite.Fragments.Ship.ShiporderLinesShippedFragment;
import nl.icsvertex.scansuite.Fragments.Ship.ShiporderLinesToShipFragment;
import nl.icsvertex.scansuite.Fragments.Ship.ShiporderLinesTotalFragment;

public class ShiporderLinesPagerAdapter extends FragmentPagerAdapter {

    //Region Private Properties
    private int numberOfTabsInt;
    //End Region Private Properties

    //Region Constructor
    public ShiporderLinesPagerAdapter(int pvNumberOfTabsInt) {
        super(cAppExtension.fragmentManager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.numberOfTabsInt = pvNumberOfTabsInt;

    }
    //End Region Constructor

    //Region Default Methods

    @NonNull
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
                return cAppExtension.dialogFragment;
        }
    }
    @Override
    public int getCount() {return numberOfTabsInt;}

    //End Region Default Methods

}
