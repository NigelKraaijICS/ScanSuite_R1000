package nl.icsvertex.scansuite;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;

import nl.icsvertex.scansuite.fragments.support.SupportApplicationFragment;
import nl.icsvertex.scansuite.fragments.support.SupportDeviceFragment;
import nl.icsvertex.scansuite.fragments.support.SupportNetworkFragment;

public class SupportPagerAdapter extends FragmentStatePagerAdapter {

    private int numberOfTabsInt;

    public SupportPagerAdapter(int pvNumberOfTabsInt) {
        super(cAppExtension.fragmentManager);
        this.numberOfTabsInt = pvNumberOfTabsInt;
    }
    @Override
    public Fragment getItem(int pvPositionInt) {
        switch(pvPositionInt) {
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
        return numberOfTabsInt;
    }
}
