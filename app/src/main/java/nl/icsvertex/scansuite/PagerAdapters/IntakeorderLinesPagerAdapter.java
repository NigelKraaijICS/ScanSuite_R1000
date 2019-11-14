package nl.icsvertex.scansuite.PagerAdapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;

import ICS.cAppExtension;
import nl.icsvertex.scansuite.Fragments.intake.IntakeorderLinesDoneFragment;
import nl.icsvertex.scansuite.Fragments.intake.IntakeorderLinesToDoFragment;
import nl.icsvertex.scansuite.Fragments.intake.IntakeorderLinesTotalFragment;
import nl.icsvertex.scansuite.Fragments.pick.PickorderLinesPickedFragment;
import nl.icsvertex.scansuite.Fragments.pick.PickorderLinesToPickFragment;
import nl.icsvertex.scansuite.Fragments.pick.PickorderLinesTotalFragment;

public class IntakeorderLinesPagerAdapter extends FragmentStatePagerAdapter {

    //Region Private Properties
    private int numberOfTabsInt;
    //End Region Private Properties

    //Region Constructor
    public IntakeorderLinesPagerAdapter(int pvNumberOfTabsInt) {
        super(cAppExtension.fragmentManager);
        this.numberOfTabsInt = pvNumberOfTabsInt;
    }
    //End Region Constructor

    //Region Default Methods

    @Override
    public Fragment getItem(int pvPositionInt) {
        switch(pvPositionInt) {
            case 0:
                IntakeorderLinesToDoFragment tab1 = new IntakeorderLinesToDoFragment();
                return tab1;
            case 1:
                IntakeorderLinesDoneFragment tab2 = new IntakeorderLinesDoneFragment();
                return tab2;
            case 2:
                IntakeorderLinesTotalFragment tab3 = new IntakeorderLinesTotalFragment();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {return numberOfTabsInt;}

    //End Region Default Methods



}
