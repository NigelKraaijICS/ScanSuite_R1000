package nl.icsvertex.scansuite;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import nl.icsvertex.scansuite.fragments.support.SupportApplicationFragment;
import nl.icsvertex.scansuite.fragments.support.SupportDeviceFragment;
import nl.icsvertex.scansuite.fragments.support.SupportNetworkFragment;

public class SupportPagerAdapter extends FragmentStatePagerAdapter {
    private int l_numberOfTabsInt;

    public SupportPagerAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.l_numberOfTabsInt = numOfTabs;
    }
    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0:
                SupportNetworkFragment tab1 = new SupportNetworkFragment();
                return tab1;
            case 1:
                SupportDeviceFragment tab2 = new SupportDeviceFragment();
                return tab2;
            case 2:
                SupportApplicationFragment tab3 = new SupportApplicationFragment();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return l_numberOfTabsInt;
    }
}
