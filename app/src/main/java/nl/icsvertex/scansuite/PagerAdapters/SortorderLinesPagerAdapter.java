package nl.icsvertex.scansuite.PagerAdapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;

import ICS.cAppExtension;
import nl.icsvertex.scansuite.Fragments.sort.SortorderLinesSortedFragment;
import nl.icsvertex.scansuite.Fragments.sort.SortorderLinesToSortFragment;
import nl.icsvertex.scansuite.Fragments.sort.SortorderLinesTotalFragment;

public class SortorderLinesPagerAdapter extends FragmentStatePagerAdapter {

    //Region Public Properties

    //End Region Public Properties

    //Region Private Properties
    private int numberOfTabsInt;
    //End Region Private Properties

//Region Constructor

    public SortorderLinesPagerAdapter(int pvNumberOfTabsInt) {
        super(cAppExtension.fragmentManager);
        this.numberOfTabsInt = pvNumberOfTabsInt;
    }

    //End Region Constructor


    @Override
    public Fragment getItem(int pvPositionInt) {
        switch(pvPositionInt) {
            case 0:
                return new SortorderLinesToSortFragment();
            case 1:
                return new SortorderLinesSortedFragment();
            case 2:
                return new SortorderLinesTotalFragment();
            default:
                return null;
        }
    }
    @Override
    public int getCount() {return this.numberOfTabsInt;}
}
