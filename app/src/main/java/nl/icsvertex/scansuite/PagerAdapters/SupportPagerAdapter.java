package nl.icsvertex.scansuite.PagerAdapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;

import ICS.cAppExtension;
import nl.icsvertex.scansuite.Fragments.Support.SupportApplicationFragment;
import nl.icsvertex.scansuite.Fragments.Support.SupportDeviceFragment;
import nl.icsvertex.scansuite.Fragments.Support.SupportNetworkFragment;

public class SupportPagerAdapter extends FragmentStatePagerAdapter {
    private int numberOfTabsInt;
    public SupportPagerAdapter(int pvNumberOfTabsInt) {
        super(cAppExtension.fragmentManager, FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.numberOfTabsInt = pvNumberOfTabsInt;
    }

    @NonNull
    @Override
    public Fragment getItem(int pvPositionInt) {
        switch(pvPositionInt) {
            case 0:
                return  new SupportNetworkFragment();

            case 1:
                return new SupportDeviceFragment();

            case 2:
                return new SupportApplicationFragment();

            default:
                return cAppExtension.dialogFragment;
        }
    }

    @Override
    public int getCount() {
        return numberOfTabsInt;
    }
}
