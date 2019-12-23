package nl.icsvertex.scansuite.PagerAdapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;

import ICS.cAppExtension;
import nl.icsvertex.scansuite.Fragments.Move.MoveorderLinesDoneFragment;
import nl.icsvertex.scansuite.Fragments.Move.MoveorderLinesNotDoneFragment;
import nl.icsvertex.scansuite.Fragments.Move.MoveorderLinesTotalFragment;

public class MoveorderLinesPagerAdapter extends FragmentStatePagerAdapter {

    //Region Private Properties
    private int numberOfTabsInt;
    //End Region Private Properties

    //Region Constructor
    public MoveorderLinesPagerAdapter(int pvNumberOfTabsInt) {
        super(cAppExtension.fragmentManager);
        this.numberOfTabsInt = pvNumberOfTabsInt;
    }
    //End Region Constructor

    //Region Default Methods

    @Override
    public Fragment getItem(int pvPositionInt) {
        switch(pvPositionInt) {
            case 0:
                MoveorderLinesNotDoneFragment tab1 = new MoveorderLinesNotDoneFragment();
                return tab1;
            case 1:
                MoveorderLinesDoneFragment tab2 = new MoveorderLinesDoneFragment();
                return tab2;
            case 2:
                MoveorderLinesTotalFragment tab3 = new MoveorderLinesTotalFragment();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {return numberOfTabsInt;}

    //End Region Default Methods



}
