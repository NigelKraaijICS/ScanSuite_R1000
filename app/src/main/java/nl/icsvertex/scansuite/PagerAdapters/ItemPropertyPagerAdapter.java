package nl.icsvertex.scansuite.PagerAdapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.List;

import ICS.cAppExtension;
import nl.icsvertex.scansuite.Fragments.Pick.PickorderLinesPickedFragment;
import nl.icsvertex.scansuite.Fragments.Pick.PickorderLinesToPickFragment;
import nl.icsvertex.scansuite.Fragments.Pick.PickorderLinesTotalFragment;

public class ItemPropertyPagerAdapter extends FragmentStatePagerAdapter {

    //Region Private Properties
    private final List<Fragment> fragmentsObl;
    private final List<String> titleObl;
    //End Region Private Properties

    //Region Constructor
    public ItemPropertyPagerAdapter(List<Fragment> pvFragmentObl, List<String> pvTitleObl) {
        super(cAppExtension.fragmentManager, FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.fragmentsObl = pvFragmentObl;
        this.titleObl = pvTitleObl;
    }
    //End Region Constructor

    //Region Default Methods

    @NonNull
    @Override
    public Fragment getItem(int pvPositionInt) {
        return this.fragmentsObl.get(pvPositionInt);
    }

    @Override
    public int getCount() {
        return this.fragmentsObl .size();
    }

    @Override
    public CharSequence getPageTitle(int position) {

        if (titleObl != null &&
                position >= 0 &&
                position < titleObl.size()) {
            return titleObl.get(position);
        }

        return null;
    }

    //End Region Default Methods
}

