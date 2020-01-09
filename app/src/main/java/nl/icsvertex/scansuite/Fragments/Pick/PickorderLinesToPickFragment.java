package nl.icsvertex.scansuite.Fragments.Pick;


import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
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
import nl.icsvertex.scansuite.Activities.Pick.PickorderLinesActivity;
import nl.icsvertex.scansuite.R;
import ICS.cAppExtension;
import nl.icsvertex.scansuite.Fragments.Dialogs.NothingHereFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.SendOrderFragment;


public class PickorderLinesToPickFragment extends  Fragment  implements iICSDefaultFragment {

    //Region Public Properties

    //End Region Public Properties

    //Region Private Properties

    private static TextView quickhelpText;
    private static ImageView quickhelpIcon;
    private static ConstraintLayout quickhelpContainer;
    private static TextView textViewSelectedBin;
    private static ConstraintLayout currentLocationView;

    private static RecyclerView recyclerViewPickorderLinesTopick;

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
        return pvInflater.inflate(R.layout.fragment_pickorder_lines_to_pick, pvContainer, false);

    }

    @Override
    public void onViewCreated(@NonNull View pvView, @Nullable Bundle pvSavedInstanceState) {



    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        cUserInterface.pEnableScanner();
        PickorderLinesActivity.currentLineFragment = this;
        this.mFragmentInitialize();
    }


    //End Region Default Methods

    //Region iICSDefaultFragment defaults
    @Override
    public void mFragmentInitialize() {
        this.mFindViews();
        this.mFieldsInitialize();
        this.mSetListeners();
        this.mGetData();
        cUserInterface.pEnableScanner();

    }

    @Override
    public void mFindViews() {

        if (getView() != null) {
            PickorderLinesToPickFragment.recyclerViewPickorderLinesTopick = getView().findViewById(R.id.recyclerViewPickorderLinesTopick);
            PickorderLinesToPickFragment.textViewSelectedBin = getView().findViewById(R.id.textViewSelectedBin);
            PickorderLinesToPickFragment.quickhelpText = getView().findViewById(R.id.quickhelpText);
            PickorderLinesToPickFragment.quickhelpContainer = getView().findViewById(R.id.actionsContainer);
            PickorderLinesToPickFragment.quickhelpIcon = getView().findViewById(R.id.quickhelpIcon);
            PickorderLinesToPickFragment.currentLocationView = getView().findViewById(R.id.currentBINView);
        }

    }


    @Override
    public void mFieldsInitialize() {

        if (cSetting.PICK_BIN_MANUAL()) {
            PickorderLinesToPickFragment.currentLocationView.setVisibility(View.VISIBLE);
            PickorderLinesToPickFragment.currentLocationView.setClickable(true);
        }
        else {
            PickorderLinesToPickFragment.currentLocationView.setVisibility(View.INVISIBLE);
            PickorderLinesToPickFragment.currentLocationView.setClickable(false);
        }

        if (cSetting.PICK_BIN_IS_ITEM()) {
            PickorderLinesToPickFragment.quickhelpText.setText(R.string.scan_article_or_bincode);
        }
        else {
            PickorderLinesToPickFragment.quickhelpText.setText(R.string.scan_bincode);
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

    public static void pSetSelectedIndexInt(final int pvIndexInt) {

        new Handler().postDelayed(new Runnable() {
                @Override
               public void run() {
                    PickorderLinesToPickFragment.recyclerViewPickorderLinesTopick.scrollToPosition(pvIndexInt);
                }
           },1);

    }

    //End Region Public Methods

    //Region Private Methods

    private void mSetQuickHelpListener() {
        PickorderLinesToPickFragment.quickhelpContainer.setOnClickListener(new View.OnClickListener() {
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
        PickorderLinesToPickFragment.currentLocationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PickorderLinesActivity.pHandleScan(null,true);
            }
        });
    }

    private void mGetData() {

        List<cPickorderLine> notHandledLinesObl = cPickorder.currentPickOrder.pGetLinesNotHandledFromDatabasObl();

        if (notHandledLinesObl.size()>0) {
            PickorderLinesToPickFragment.currentLocationView.setVisibility(View.VISIBLE);
        }
        else {
            PickorderLinesToPickFragment.currentLocationView.setVisibility(View.INVISIBLE);
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
        PickorderLinesToPickFragment.recyclerViewPickorderLinesTopick.setHasFixedSize(false);
        PickorderLinesToPickFragment.recyclerViewPickorderLinesTopick.setAdapter(cPickorderLine.getPickorderLineToPickAdapter());
        PickorderLinesToPickFragment.recyclerViewPickorderLinesTopick.setLayoutManager(new LinearLayoutManager(cAppExtension.context));
        PickorderLinesToPickFragment.recyclerViewPickorderLinesTopick.setVisibility(View.VISIBLE);

        PickorderLinesActivity.pChangeTabCounterText(cText.pDoubleToStringStr(cPickorder.currentPickOrder.pQuantityNotHandledDbl()) + "/" + cText.pDoubleToStringStr(cPickorder.currentPickOrder.pQuantityTotalDbl()));
    }

    private void mNoLinesAvailable(Boolean pvEnabledBln) {

        if (PickorderLinesActivity.currentLineFragment == this) {
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

                PickorderLinesToPickFragment.quickhelpContainer.setVisibility(View.VISIBLE);
                return;

            }

            //Hide the recycler view
            PickorderLinesToPickFragment.recyclerViewPickorderLinesTopick.setVisibility(View.INVISIBLE);

            //Hide location button and clear text
            PickorderLinesToPickFragment.currentLocationView.setVisibility(View.INVISIBLE);
            PickorderLinesToPickFragment.currentLocationView.setClickable(false);
            PickorderLinesToPickFragment.textViewSelectedBin.setText("");
            PickorderLinesToPickFragment.quickhelpContainer.setVisibility(View.INVISIBLE);

            //Show nothing there fragment
            FragmentTransaction fragmentTransaction = cAppExtension.fragmentManager.beginTransaction();
            SendOrderFragment fragment = new SendOrderFragment();
            fragmentTransaction.replace(R.id.fragmentPickorderLinesToPick, fragment);
            fragmentTransaction.commit();

            //Change tabcounter text
            PickorderLinesActivity.pChangeTabCounterText(cText.pDoubleToStringStr(cPickorder.currentPickOrder.pQuantityNotHandledDbl()) + "/" + cText.pDoubleToStringStr(cPickorder.currentPickOrder.pQuantityTotalDbl()));
        }
    }

    //End Region private Methods


}
