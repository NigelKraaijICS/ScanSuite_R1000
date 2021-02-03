package nl.icsvertex.scansuite.Fragments.Pick;


import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.List;

import ICS.Interfaces.iICSDefaultFragment;
import ICS.Utils.cText;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Basics.Settings.cSetting;
import SSU_WHS.Picken.PickorderLines.cPickorderLine;
import SSU_WHS.Picken.PickorderLines.cPickorderLineAdapter;
import SSU_WHS.Picken.Pickorders.cPickorder;
import nl.icsvertex.scansuite.Activities.Pick.PickorderLinesActivity;
import nl.icsvertex.scansuite.Fragments.Dialogs.NothingHereFragment;
import nl.icsvertex.scansuite.R;


public class PickorderLinesToPickFragment extends  Fragment  implements iICSDefaultFragment, SwipeRefreshLayout.OnRefreshListener {

    //Region Public Properties

    //End Region Public Properties

    //Region Private Properties

    private TextView quickhelpText;
    private ImageView quickhelpIcon;
    private ConstraintLayout quickhelpContainer;
    private TextView textViewSelectedBin;
    private ConstraintLayout currentLocationView;

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerViewPickorderLinesTopick;

    private cPickorderLineAdapter pickorderLineAdapter;
    private cPickorderLineAdapter getPickorderLineAdapter(){
        if (this.pickorderLineAdapter == null) {
            this.pickorderLineAdapter = new cPickorderLineAdapter();
        }
        return  this.pickorderLineAdapter;
    }

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

    @Override
    public void onRefresh() {

        //Clear the scanned branch

        if (cPickorder.currentPickOrder.destionationBranch() == null) {
            cPickorder.currentPickOrder.scannedBranch = null;
        }

        //Refresh the recycler
        this.pBranchScanned();
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
            this.swipeRefreshLayout = getView().findViewById(R.id.swipeRefreshLayout);
            this.recyclerViewPickorderLinesTopick = getView().findViewById(R.id.recyclerViewPickorderLinesTopick);
            this.textViewSelectedBin = getView().findViewById(R.id.textViewSelectedBin);
            this.quickhelpText = getView().findViewById(R.id.quickhelpText);
            this.quickhelpContainer = getView().findViewById(R.id.quickHelpContainer);
            this.quickhelpIcon = getView().findViewById(R.id.quickhelpIcon);
            this.currentLocationView = getView().findViewById(R.id.currentBINContainer);
        }

    }

    @Override
    public void mFieldsInitialize() {

        if (cPickorder.currentPickOrder.isPFBln() ) {
            if (cPickorder.currentPickOrder.scannedBranch == null) {
                this.currentLocationView.setVisibility(View.INVISIBLE);
                this.currentLocationView.setClickable(false);
                this.quickhelpText.setText(R.string.message_scan_destination);
                this.quickhelpText.performClick();
                return;
            }
        }

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
            if (cPickorder.currentPickOrder.isSingleBinBln()) {
                this.quickhelpText.setText(R.string.message_scan_article);
            }
        }
    }

    @Override
    public void mSetListeners() {
        this.mSetQuickHelpListener();
        this.mSetLocationListener();
        this.mSetSwipeRefreshListener();
    }

    //End Region iICSDefaultFragment defaults

    //Region Public Methods

    public void pBranchScanned(){
        this.mFieldsInitialize();
        this.mGetData();

    }

    public  void pSetChosenBinCode( ) {
        this.textViewSelectedBin.setText(cPickorderLine.currentPickOrderLine.getBinCodeStr(),TextView.BufferType.SPANNABLE);
        this.currentLocationView.requestLayout();
    }

    public  void pSetSelectedIndexInt() {

        new Handler().postDelayed(new Runnable() {
                @Override
               public void run() {
                    recyclerViewPickorderLinesTopick.scrollToPosition(cPickorder.currentPickOrder.lastSelectedIndexInt);
                }
           },1);

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

                if (cAppExtension.activity instanceof  PickorderLinesActivity) {
                    PickorderLinesActivity pickorderLinesActivity = (PickorderLinesActivity)cAppExtension.activity;
                    pickorderLinesActivity.pHandleScan(null,true);

                }
            }
        });
    }

    private void mSetSwipeRefreshListener() {
        this.swipeRefreshLayout.setOnRefreshListener(this);
        this.swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent), getResources().getColor(R.color.colorActive), getResources().getColor(R.color.colorPrimary));
    }

    private void mGetData() {

        List<cPickorderLine> notHandledLinesObl = cPickorder.currentPickOrder.pGetLinesNotHandledFromDatabaseObl();

        if (notHandledLinesObl.size()>0) {
            this.currentLocationView.setVisibility(View.VISIBLE);
            this.mFieldsInitialize();
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
        this.getPickorderLineAdapter().pFillData(pvDataObl);
        this.recyclerViewPickorderLinesTopick.setHasFixedSize(false);
        this.recyclerViewPickorderLinesTopick.setAdapter(this.getPickorderLineAdapter());
        this.recyclerViewPickorderLinesTopick.setLayoutManager(new LinearLayoutManager(cAppExtension.context));
        this.recyclerViewPickorderLinesTopick.setVisibility(View.VISIBLE);

        if (cAppExtension.activity instanceof  PickorderLinesActivity) {
            PickorderLinesActivity pickorderLinesActivity = (PickorderLinesActivity)cAppExtension.activity;
            pickorderLinesActivity.pChangeTabCounterText(cText.pDoubleToStringStr(cPickorder.currentPickOrder.pQuantityNotHandledDbl()) + "/" + cText.pDoubleToStringStr(cPickorder.currentPickOrder.pQuantityTotalDbl()));
        }

    }

    private void mNoLinesAvailable(Boolean pvEnabledBln) {

        if (PickorderLinesActivity.currentLineFragment instanceof  PickorderLinesToPickFragment ) {
            //Close no orders fragment if needed
            if (!pvEnabledBln) {
                List<Fragment> fragments = cAppExtension.fragmentManager.getFragments();
                for (Fragment fragment : fragments) {
                    if (fragment instanceof NothingHereFragment) {
                        FragmentTransaction fragmentTransaction = cAppExtension.fragmentManager.beginTransaction();
                        fragmentTransaction.remove(fragment);
                        fragmentTransaction.commit();
                    }
                }

                this.swipeRefreshLayout.setRefreshing(false);
                this.quickhelpContainer.setVisibility(View.VISIBLE);
                return;

            }

            //Hide the recycler view
            this.recyclerViewPickorderLinesTopick.setVisibility(View.INVISIBLE);
            this.swipeRefreshLayout.setRefreshing(false);

            //Hide location button and clear text
            this.currentLocationView.setVisibility(View.INVISIBLE);
            this.currentLocationView.setClickable(false);
            this.textViewSelectedBin.setText("");
            this.quickhelpContainer.setVisibility(View.INVISIBLE);

            //Show nothing there fragment
            FragmentTransaction fragmentTransaction = cAppExtension.fragmentManager.beginTransaction();
            NothingHereFragment fragment = new NothingHereFragment();
            fragmentTransaction.replace(R.id.fragmentPickorderLinesToPick, fragment);
            fragmentTransaction.commit();

            if (cAppExtension.activity instanceof  PickorderLinesActivity) {
                if (cPickorder.currentPickOrder != null) {
                    PickorderLinesActivity pickorderLinesActivity = (PickorderLinesActivity) cAppExtension.activity;
                    pickorderLinesActivity.pChangeTabCounterText(cText.pDoubleToStringStr(cPickorder.currentPickOrder.pQuantityNotHandledDbl()) + "/" + cText.pDoubleToStringStr(cPickorder.currentPickOrder.pQuantityTotalDbl()));
                }
            }
            //Change tabcounter text
        }
    }

    //End Region private Methods


}
