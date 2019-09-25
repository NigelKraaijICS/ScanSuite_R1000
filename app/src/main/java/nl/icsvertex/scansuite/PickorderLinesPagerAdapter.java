package nl.icsvertex.scansuite;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;

import nl.icsvertex.scansuite.fragments.pick.PickorderLinesPickedFragment;
import nl.icsvertex.scansuite.fragments.pick.PickorderLinesToPickFragment;
import nl.icsvertex.scansuite.fragments.pick.PickorderLinesTotalFragment;

import static nl.icsvertex.scansuite.cAppExtension.fragmentManager;

public class PickorderLinesPagerAdapter extends FragmentStatePagerAdapter {

    //Region Private Properties
    private int numberOfTabsInt;
    //End Region Private Properties

    public PickorderLinesPagerAdapter(int pvNumberOfTabsInt) {
        super(fragmentManager);
        this.numberOfTabsInt = pvNumberOfTabsInt;
    }

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
}
