package nl.icsvertex.scansuite.PagerAdapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;

import ICS.cAppExtension;
import nl.icsvertex.scansuite.Fragments.QualityControl.QCLinesCheckedFragment;
import nl.icsvertex.scansuite.Fragments.QualityControl.QCLinesToCheckFragment;
import nl.icsvertex.scansuite.Fragments.Sort.SortorderLinesSortedFragment;
import nl.icsvertex.scansuite.Fragments.Sort.SortorderLinesToSortFragment;
import nl.icsvertex.scansuite.Fragments.Sort.SortorderLinesTotalFragment;

public class QCLinesPagerAdapter extends FragmentPagerAdapter {

    //Region Public Properties

    //End Region Public Properties

    //Region Private Properties
    private int numberOfTabsInt;
    //End Region Private Properties

//Region Constructor

    public QCLinesPagerAdapter(int pvNumberOfTabsInt) {
        super(cAppExtension.fragmentManager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.numberOfTabsInt = pvNumberOfTabsInt;

    }

    //End Region Constructor


    @NonNull
    @Override
    public Fragment getItem(int pvPositionInt) {
        switch(pvPositionInt) {
            case 0:
                return new QCLinesToCheckFragment();
            case 1:
                return new QCLinesCheckedFragment();
            default:
                return cAppExtension.dialogFragment;
        }
    }
    @Override
    public int getCount() {return this.numberOfTabsInt;}
}
