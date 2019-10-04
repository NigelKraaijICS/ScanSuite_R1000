package nl.icsvertex.scansuite.fragments.pick;


import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ICS.Interfaces.iICSDefaultFragment;
import ICS.Utils.cText;
import SSU_WHS.Basics.Settings.cSetting;
import SSU_WHS.Picken.PickorderLines.cPickorderLine;

import ICS.Utils.cUserInterface;
import SSU_WHS.Picken.Pickorders.cPickorder;
import nl.icsvertex.scansuite.activities.pick.PickorderLinesActivity;
import nl.icsvertex.scansuite.R;
import nl.icsvertex.scansuite.cAppExtension;
import nl.icsvertex.scansuite.fragments.NothingHereFragment;
import nl.icsvertex.scansuite.fragments.SendOrderFragment;


public class PickorderLinesToPickFragment extends  Fragment  implements iICSDefaultFragment {

    //Region Public Properties

    //End Region Public Properties

    //Region Private Properties

    private   TextView quickhelpText;
    private  ImageView quickhelpIcon;
    private ConstraintLayout quickhelpContainer;
    private static TextView textViewSelectedBin;
    private ConstraintLayout currentLocationView;

    private RecyclerView recyclerViewPickorderLinesTopick;

    //End Region Private Properties

    //Region Constructor
    public PickorderLinesToPickFragment() {

    }
    //End Region Constructor

    //Region Default Methods

    @Override
    public View onCreateView(LayoutInflater pvInflater, ViewGroup pvContainer,
                             Bundle pvSavedInstanceState) {

        // Inflate the layout for this fragment
        View rootview = pvInflater.inflate(R.layout.fragment_pickorder_lines_to_pick, pvContainer, false);
        return rootview;

    }

    @Override
    public void onViewCreated(@NonNull View pvView, @Nullable Bundle pvSavedInstanceState) {
        this.mFragmentInitialize();


    }

    @Override
    public void setUserVisibleHint(boolean pvIsVisibleToUserBln) {
        super.setUserVisibleHint(pvIsVisibleToUserBln);

        if (pvIsVisibleToUserBln) {

            PickorderLinesActivity.currentLineFragment = this;

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.detach(this).attach(this).commit();
        }
    }


    //End Region Default Methods

    //Region iICSDefaultFragment defaults
    @Override
    public void mFragmentInitialize() {

        this.mFindViews();
        this.mSetViewModels();
        this.mFieldsInitialize();
        this.mSetListeners();
        this.mGetData();

    }

    @Override
    public void mFindViews() {
        this.recyclerViewPickorderLinesTopick = getView().findViewById(R.id.recyclerViewPickorderLinesTopick);
        this.textViewSelectedBin = getView().findViewById(R.id.textViewSelectedBin);
        this.quickhelpText = getView().findViewById(R.id.quickhelpText);
        this.quickhelpContainer = getView().findViewById(R.id.quickhelpContainer);
        this.quickhelpIcon = getView().findViewById(R.id.quickhelpIcon);
        this.currentLocationView = getView().findViewById(R.id.currentLocationView);
    }

    @Override
    public void mSetViewModels() {

    }

    @Override
    public void mFieldsInitialize() {

        if (cSetting.PICK_BIN_MANUAL()) {
            this.currentLocationView.setVisibility(View.VISIBLE);
            this.currentLocationView.setClickable(true);
        }
        else {
            this.currentLocationView.setVisibility(View.INVISIBLE);
            this.currentLocationView.setClickable(false);
        }

        if (cSetting.PICK_BIN_IS_ITEM()) {
            this.quickhelpText.setText(R.string.scan_article_or_bincode);
        }
        else {
            this.quickhelpText.setText(R.string.scan_bincode);
        }


    }

    @Override
    public void mSetListeners() {

        this.mSetQuickHelpListener();
        this.mSetLocationListener();
    }

    //End Region iICSDefaultFragment defaults

    //Region Public Methods

    public static void pSetChosenBinCode( ) {
        PickorderLinesToPickFragment.textViewSelectedBin.setText(cPickorderLine.currentPickOrderLine.getBinCodeStr());
    }

    //End Region Public Methods

    //Region Private Methods

    private void mSetQuickHelpListener() {
        this.quickhelpContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cUserInterface.pDoRotate(quickhelpIcon, 0);
                if (quickhelpText.getVisibility() == View.VISIBLE) {
                    quickhelpText.setVisibility(View.GONE);
                }
                else {
                    quickhelpText.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void mSetLocationListener() {
        this.currentLocationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PickorderLinesActivity.pHandleScan("",true);
            }
        });
    }

    private void mGetData() {

        List<cPickorderLine> notHandledLinesObl = cPickorder.currentPickOrder.pGetLinesNotHandledFromDatabasObl();

        if (notHandledLinesObl.size()>0) {
            this.currentLocationView.setVisibility(View.VISIBLE);
        }
        else {
            this.currentLocationView.setVisibility(View.INVISIBLE);
    }

        this.mFillRecycler(notHandledLinesObl);

}

    private void mFillRecycler(List<cPickorderLine> pvDataObl) {

        if (pvDataObl.size() == 0) {
            this.mNoLinesAvailable(true);
            return;
        }

        this.mNoLinesAvailable(false);

        //Show the recycler view
        cPickorderLine.getPickorderLineToPickAdapter().pFillData(pvDataObl);
        this.recyclerViewPickorderLinesTopick.setHasFixedSize(false);
        this.recyclerViewPickorderLinesTopick.setAdapter(cPickorderLine.getPickorderLineToPickAdapter());
        this.recyclerViewPickorderLinesTopick.setLayoutManager(new LinearLayoutManager(cAppExtension.context));
        this.recyclerViewPickorderLinesTopick.setVisibility(View.VISIBLE);

        PickorderLinesActivity.pChangeTabCounterText(cText.doubleToString(cPickorder.currentPickOrder.pQuantityNotHandledDbl()) + "/" + cText.doubleToString(cPickorder.currentPickOrder.pQuantityTotalDbl()));

    }

    private void mNoLinesAvailable(Boolean pvEnabledBln) {

        if (PickorderLinesActivity.currentLineFragment != null && PickorderLinesActivity.currentLineFragment == this) {
            //Close no orders fragment if needed
            if (!pvEnabledBln) {
                List<Fragment> fragments = cAppExtension.fragmentManager.getFragments();
                for (Fragment fragment : fragments) {
                    if (fragment instanceof NothingHereFragment || fragment instanceof SendOrderFragment) {
                        FragmentTransaction fragmentTransaction = cAppExtension.fragmentManager.beginTransaction();
                        fragmentTransaction.remove(fragment);
                        fragmentTransaction.commit();
                    }
                }

                this.quickhelpContainer.setVisibility(View.VISIBLE);
                return;

            }

            //Hide the recycler view
            this.recyclerViewPickorderLinesTopick.setVisibility(View.INVISIBLE);

            //Hide location button and clear text
            this.currentLocationView.setVisibility(View.INVISIBLE);
            this.currentLocationView.setClickable(false);
            PickorderLinesToPickFragment.textViewSelectedBin.setText("");
            this.quickhelpContainer.setVisibility(View.INVISIBLE);

            //Show nothing there fragment
            FragmentTransaction fragmentTransaction = cAppExtension.fragmentManager.beginTransaction();
            SendOrderFragment fragment = new SendOrderFragment();
            fragmentTransaction.replace(R.id.fragmentPickorderLinesToPick, fragment);
            fragmentTransaction.commit();

            //Change tabcounter text
            PickorderLinesActivity.pChangeTabCounterText(cText.doubleToString(cPickorder.currentPickOrder.pQuantityNotHandledDbl()) + "/" + cText.doubleToString(cPickorder.currentPickOrder.pQuantityTotalDbl()));
        }
    }

    //End Region private Methods


}
