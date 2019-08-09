package nl.icsvertex.scansuite;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import nl.icsvertex.scansuite.fragments.pick.PickorderLinesPickedFragment;
import nl.icsvertex.scansuite.fragments.pick.PickorderLinesToPickFragment;
import nl.icsvertex.scansuite.fragments.pick.PickorderLinesTotalFragment;

public class PickorderLinesPagerAdapter extends FragmentStatePagerAdapter {
    int l_numberOfTabsInt;

    public PickorderLinesPagerAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.l_numberOfTabsInt = numOfTabs;
    }
    @Override
    public Fragment getItem(int position) {
        switch(position) {
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
    public int getCount() {return l_numberOfTabsInt;}
}
