package nl.icsvertex.scansuite.PagerAdapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;

import ICS.cAppExtension;
import SSU_WHS.Intake.Intakeorders.cIntakeorder;
import nl.icsvertex.scansuite.Fragments.Packaging.PackagingInFragment;
import nl.icsvertex.scansuite.Fragments.Packaging.PackagingOutFragment;
import nl.icsvertex.scansuite.Fragments.Packaging.PackagingUsedFragment;
import nl.icsvertex.scansuite.Fragments.Pick.PickorderLinesPickedFragment;
import nl.icsvertex.scansuite.Fragments.Pick.PickorderLinesToPickFragment;
import nl.icsvertex.scansuite.Fragments.Pick.PickorderLinesTotalFragment;

public class PackagingPagerAdapter extends FragmentPagerAdapter {

    //Region Private Properties
    private int numberOfTabsInt;
    //End Region Private Properties

    //Region Constructor
    public PackagingPagerAdapter(int pvNumberOfTabsInt) {
        super(cAppExtension.fragmentManager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.numberOfTabsInt = pvNumberOfTabsInt;
    }
    //End Region Constructor

    //Region Default Methods

    @NonNull
    @Override
    public Fragment getItem(int pvPositionInt) {
        switch(pvPositionInt) {
            case 0:

                //If we show two tabs, in is always first
                if (cIntakeorder.currentIntakeOrder.isReceiveIntakeEOPackagingIntakeBln()) {
                    return  new PackagingInFragment();
                }

                // If we only have one tab and it was in we don't reach this statement, if we do reach we only have to ship packaging so this is the firt tab
                if (cIntakeorder.currentIntakeOrder.isReceiveIntakeEOPackagingShippedBln()) {
                    return  new PackagingOutFragment();
                }

            case 1:

                // If we only have one tab and it was in we don't reach this statement, if we do reach we only have to ship packaging so this is the firt tab
                if (cIntakeorder.currentIntakeOrder.isReceiveIntakeEOPackagingShippedBln()) {
                    return  new PackagingOutFragment();
                }

                return  new PackagingUsedFragment();

            case 2:

                return  new PackagingUsedFragment();

            default:
                return cAppExtension.dialogFragment;
        }
    }

    @Override
    public int getCount() {return numberOfTabsInt;}

    //End Region Default Methods



}
