package nl.icsvertex.scansuite.PagerAdapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;

import ICS.cAppExtension;
import nl.icsvertex.scansuite.Fragments.Move.MoveLinesPlaceFragment;
import nl.icsvertex.scansuite.Fragments.Move.MoveLinesTakeFragment;


public class MoveLinesPagerAdapter extends FragmentPagerAdapter {

    //Region Private Properties
    private int numberOfTabsInt;
    //End Region Private Properties

    public MoveLinesPagerAdapter(int pvNumberOfTabsInt) {
        super(cAppExtension.fragmentManager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.numberOfTabsInt = pvNumberOfTabsInt;
    }

    @NonNull
    @Override
    public Fragment getItem(int pvPositionInt) {
        switch(pvPositionInt) {
            case 0:
                return new MoveLinesTakeFragment();
            case 1:
                return new MoveLinesPlaceFragment();
            default:
                return cAppExtension.dialogFragment;
        }
    }
    @Override
    public int getCount() {return numberOfTabsInt;}
}
