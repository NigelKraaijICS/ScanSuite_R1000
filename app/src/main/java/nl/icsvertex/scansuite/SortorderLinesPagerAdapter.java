package nl.icsvertex.scansuite;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import nl.icsvertex.scansuite.fragments.sort.SortorderLinesSortedFragment;
import nl.icsvertex.scansuite.fragments.sort.SortorderLinesToSortFragment;
import nl.icsvertex.scansuite.fragments.sort.SortorderLinesTotalFragment;

public class SortorderLinesPagerAdapter extends FragmentStatePagerAdapter {
    int l_numberOfTabsInt;

    public SortorderLinesPagerAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.l_numberOfTabsInt = numOfTabs;
    }
    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0:
                SortorderLinesToSortFragment tab1 = new SortorderLinesToSortFragment();
                return tab1;
            case 1:
                SortorderLinesSortedFragment tab2 = new SortorderLinesSortedFragment();
                return tab2;
            case 2:
                SortorderLinesTotalFragment tab3 = new SortorderLinesTotalFragment();
                return tab3;
            default:
                return null;
        }
    }
    @Override
    public int getCount() {return l_numberOfTabsInt;}
}
