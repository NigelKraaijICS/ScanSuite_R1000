package nl.icsvertex.scansuite.PagerAdapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;

import ICS.cAppExtension;
import nl.icsvertex.scansuite.Fragments.Sort.SortorderLinesSortedFragment;
import nl.icsvertex.scansuite.Fragments.Sort.SortorderLinesToSortFragment;
import nl.icsvertex.scansuite.Fragments.Sort.SortorderLinesTotalFragment;
import nl.icsvertex.scansuite.Fragments.Store.StoreorderLinesStoredFragment;
import nl.icsvertex.scansuite.Fragments.Store.StoreorderLinesToStoreFragment;
import nl.icsvertex.scansuite.Fragments.Store.StoreorderLinesTotalFragment;

public class StoreorderLinesPagerAdapter extends FragmentPagerAdapter {

    //Region Public Properties

    //End Region Public Properties

    //Region Private Properties
    private int numberOfTabsInt;
    //End Region Private Properties

//Region Constructor

    public StoreorderLinesPagerAdapter(int pvNumberOfTabsInt) {
        super(cAppExtension.fragmentManager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.numberOfTabsInt = pvNumberOfTabsInt;

    }

    //End Region Constructor


    @NonNull
    @Override
    public Fragment getItem(int pvPositionInt) {
        switch(pvPositionInt) {
            case 0:
                return new StoreorderLinesToStoreFragment();
            case 1:
                return new StoreorderLinesStoredFragment();
            case 2:
                return new StoreorderLinesTotalFragment();
            default:
                return cAppExtension.dialogFragment;
        }
    }
    @Override
    public int getCount() {return this.numberOfTabsInt;}
}
