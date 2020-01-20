package nl.icsvertex.scansuite.PagerAdapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;

import ICS.cAppExtension;
import nl.icsvertex.scansuite.Fragments.Returns.ReturnDocumentsDoneFragment;
import nl.icsvertex.scansuite.Fragments.Returns.ReturnDocumentsToDoFragment;
import nl.icsvertex.scansuite.Fragments.Returns.ReturnDocumentsTotalFragment;

public class ReturnorderDocumentsPagerAdapter extends FragmentPagerAdapter {

        //Region Private Properties
        private int numberOfTabsInt;
        //End Region Private Properties

        public ReturnorderDocumentsPagerAdapter(int pvNumberOfTabsInt) {
            super(cAppExtension.fragmentManager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
            this.numberOfTabsInt = pvNumberOfTabsInt;
        }

        @NonNull
        @Override
        public Fragment getItem(int pvPositionInt) {
            switch(pvPositionInt) {
                case 0:

                    return new ReturnDocumentsToDoFragment();
                case 1:
                    return  new ReturnDocumentsDoneFragment();
                case 2:
                    return new ReturnDocumentsTotalFragment();
                    default:
                        return cAppExtension.dialogFragment;
            }
        }
        @Override
        public int getCount() {return numberOfTabsInt;}
}
