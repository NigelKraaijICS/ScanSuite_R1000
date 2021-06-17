package nl.icsvertex.scansuite.PagerAdapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import ICS.cAppExtension;

public class ArticleInfoPagerAdapter extends FragmentStatePagerAdapter {

    //Region Private Properties
    private List<Fragment> fragmentsObl;
    //End Region Private Properties

    //Region Constructor
    public ArticleInfoPagerAdapter(List<Fragment> pvFragmentObl) {
        super(cAppExtension.fragmentManager, FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.fragmentsObl = pvFragmentObl;

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

    //End Region Default Methods



}
