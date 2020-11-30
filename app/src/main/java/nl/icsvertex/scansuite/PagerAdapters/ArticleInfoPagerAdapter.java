package nl.icsvertex.scansuite.PagerAdapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ArticleInfoPagerAdapter extends FragmentPagerAdapter {

    //Region Private Properties
    private List<Fragment> fragmentsObl;
    //End Region Private Properties

    //Region Constructor
    public ArticleInfoPagerAdapter(@NonNull FragmentManager pvFragmentManager, List<Fragment> pvFragmentObl) {
        super(pvFragmentManager);
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
