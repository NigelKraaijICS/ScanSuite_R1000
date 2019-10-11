package nl.icsvertex.scansuite.PagerAdapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;

import ICS.cAppExtension;
import nl.icsvertex.scansuite.Fragments.pick.PickorderLinesPickedFragment;
import nl.icsvertex.scansuite.Fragments.pick.PickorderLinesToPickFragment;
import nl.icsvertex.scansuite.Fragments.pick.PickorderLinesTotalFragment;

public class PickorderLinesPagerAdapter extends FragmentStatePagerAdapter {

    //Region Private Properties
    private int numberOfTabsInt;
    //End Region Private Properties

    //Region Constructor
    public PickorderLinesPagerAdapter(int pvNumberOfTabsInt) {
        super(cAppExtension.fragmentManager);
        this.numberOfTabsInt = pvNumberOfTabsInt;
    }
    //End Region Constructor

    //Region Default Methods

    @Override
    public Fragment getItem(int pvPositionInt) {
        switch(pvPositionInt) {
            case 0:
                PickorderLinesToPickFragment tab1 = new PickorderLinesToPickFragment();
                return tab1;
            case 1:
                PickorderLinesPickedFragment tab2 = new PickorderLinesPickedFragment();
                return tab2;
            case 2:
                PickorderLinesTotalFragment tab3 = new PickorderLinesTotalFragment();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {return numberOfTabsInt;}

    //End Region Default Methods



}
