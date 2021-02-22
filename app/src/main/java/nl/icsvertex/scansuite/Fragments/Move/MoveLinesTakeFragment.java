package nl.icsvertex.scansuite.Fragments.Move;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ICS.Interfaces.iICSDefaultFragment;
import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cRegex;
import ICS.Utils.cResult;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Basics.BarcodeLayouts.cBarcodeLayout;
import SSU_WHS.Basics.Users.cUser;
import SSU_WHS.Move.MoveItemVariant.cMoveItemVariant;
import SSU_WHS.Move.Moveorders.cMoveorder;
import SSU_WHS.Move.MoveorderLines.cMoveorderLine;
import SSU_WHS.Move.MoveorderLines.cMoveorderLineAdapter;
import SSU_WHS.Move.MoveorderLines.cMoveorderLineRecyclerItemTouchHelper;
import nl.icsvertex.scansuite.Activities.Move.MoveLineTakeActivity;
import nl.icsvertex.scansuite.Activities.Move.MoveLinesActivity;
import nl.icsvertex.scansuite.Fragments.Dialogs.NothingHereFragment;
import nl.icsvertex.scansuite.R;

public class MoveLinesTakeFragment extends Fragment implements iICSDefaultFragment, cMoveorderLineRecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    //Region Public Properties

    private boolean busyBln = false;
    private List<cMoveorderLine> localLinesObl;

    //End Region Public Properties

    //Region Private Properties

    private RecyclerView recyclerViewMoveLinesTake;

    private cMoveorderLineAdapter moveorderLineAdapter;
    private cMoveorderLineAdapter getMoveorderLineAdapter() {
        if (this.moveorderLineAdapter == null) {
            this.moveorderLineAdapter = new cMoveorderLineAdapter();
        }

        return this.moveorderLineAdapter;
    }

    //End Region Private Properties


    //Region Default Methods

    @Override
    public View onCreateView(LayoutInflater pvInflater, ViewGroup pvContainer,
                             Bundle pvSavedInstanceState) {
        // Inflate the layout for this fragment
        return pvInflater.inflate(R.layout.fragment_movelines_take, pvContainer, false);
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

        if (cAppExtension.activity instanceof  MoveLinesActivity) {
            MoveLinesActivity moveLinesActivity = (MoveLinesActivity)cAppExtension.activity;
            moveLinesActivity.currentLineFragment = this;
        }

        this.mFragmentInitialize();
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder pvViewHolder, int pvDirectionInt, int pvPositionInt) {

        if (!(pvViewHolder instanceof  cMoveorderLineAdapter.MoveorderLineViewHolder)) {
            return;
        }

        cMoveorderLine.currentMoveOrderLine = this.localLinesObl.get(pvPositionInt);


        cMoveItemVariant moveItemVariant = cMoveItemVariant.allMoveItemVariantObl.get( cMoveorderLine.currentMoveOrderLine.getKeyStr());
        if (moveItemVariant == null ) {
            this.mStepFailed(cAppExtension.activity.getString(R.string.message_itemvariant_unknown),"");
            this.mGetData(cMoveorder.currentMoveOrder.sortedTakeLinesObl());
            return;
        }

        if (moveItemVariant.getQuantityPlacedDbl() >0) {
            this.mStepFailed(cAppExtension.activity.getString(R.string.message_already_placed_so_not_possible),"");
            this.mGetData(cMoveorder.currentMoveOrder.sortedTakeLinesObl());
            return;
        }


        //Reset the line
        this.mRemoveAdapterFromFragment();
    }

    //End Region Default Methods

    //Region iICSDefaultActivity defaults

    @Override
    public void mFragmentInitialize() {

        this.mFindViews();
        this.mFieldsInitialize();
        this.mSetListeners();

        this.mGetData(cMoveorder.currentMoveOrder.sortedTakeLinesObl());

    }

    @Override
    public void mFindViews() {

        if (getView() != null) {
            this.recyclerViewMoveLinesTake = getView().findViewById(R.id.recyclerViewMoveLinesTake);
        }

    }

    @Override
    public void mFieldsInitialize() {

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new cMoveorderLineRecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(this.recyclerViewMoveLinesTake);

    }

    @Override
    public void mSetListeners() {

    }

    //End Region iICSDefaultActivity defaults

    //Region Public Methods

    public void pHandleScan(final cBarcodeScan pvBarcodeScan) {

        if (this.busyBln) {
            return;
        }

        //Close open Dialogs
        cUserInterface.pCheckAndCloseOpenDialogs();

        // Show that we are getting data
        cUserInterface.pShowGettingData();

        new Thread(new Runnable() {
            public void run() {
                mHandleScan(pvBarcodeScan);
            }
        }).start();

    }



    //End Region Public Methods

    //Region Private Methods

    private void mGetData(List<cMoveorderLine> pvDataObl) {
        this.localLinesObl = pvDataObl;
        this.mFillRecycler(this.localLinesObl);
    }

    private void mFillRecycler(List<cMoveorderLine> pvDataObl) {

        if (pvDataObl.size() == 0) {
            this.mNoLinesAvailable(true);
            return;
        }
        this.mNoLinesAvailable(false);

        this.getMoveorderLineAdapter().pFillData(pvDataObl);
        this.recyclerViewMoveLinesTake.setHasFixedSize(false);
        this.recyclerViewMoveLinesTake.setAdapter(this.getMoveorderLineAdapter());
        this.recyclerViewMoveLinesTake.setLayoutManager(new LinearLayoutManager(cAppExtension.context));
        this.recyclerViewMoveLinesTake.setVisibility(View.VISIBLE);
    }

    private void mHandleScan(cBarcodeScan pvBarcodeScan) {

        // Show that we are getting data and set busy boolean
        this.busyBln = true;

        cResult hulpResult;

        //Check if we have scanned a BIN
        if (!cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(), cBarcodeLayout.barcodeLayoutEnu.BIN)) {
            //unknown scan
            this.mStepFailed(cAppExtension.context.getString(R.string.message_bin_required), pvBarcodeScan.getBarcodeOriginalStr());
            this.busyBln = false;
            return;
        }

        //Handle the BIN scan
        hulpResult = this.mHandleBINScan(pvBarcodeScan);
        if (!hulpResult.resultBln) {
            this.mFillRecycler(this.localLinesObl);
            this.busyBln = false;
            return;
        }

        this.busyBln = false;

    }

    private cResult mHandleBINScan(cBarcodeScan pvBarcodeScan) {

        cResult result = new cResult();
        result.resultBln = true;

        //Only BIN scans are allowed
        if (!cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(),cBarcodeLayout.barcodeLayoutEnu.BIN)) {
            this.mStepFailed(cAppExtension.context.getString(R.string.error_unknown_location), pvBarcodeScan.getBarcodeOriginalStr());
            result.resultBln = false;
            return result;
        }

        //Strip the prefix if thats neccesary
        String barcodewithoutPrefix = cRegex.pStripRegexPrefixStr(pvBarcodeScan.getBarcodeOriginalStr());

        cResult hulpRst;
        hulpRst = this.mCheckAndGetBinRst(barcodewithoutPrefix);
        //Something went wrong, so show message and return
        if (! hulpRst.resultBln) {
            this.mStepFailed(hulpRst.messagesStr(), pvBarcodeScan.getBarcodeOriginalStr());
            result.resultBln = false;
            return result;
        }

        //Set the scanned barcodeStr, so we can raise quantity in next activity
        this.mStartMoveLineTakeActivity();
        return result;
    }

    private cResult mCheckAndGetBinRst(String pvBinCodeStr){

        cResult result = new cResult();
        result.resultBln = true;

        //Search for the BIN in current BINS
        cMoveorder.currentMoveOrder.currentBranchBin =  cMoveorder.currentMoveOrder.pGetBin(pvBinCodeStr);

        //We found a BIN so we are done
        if (cMoveorder.currentMoveOrder.currentBranchBin  != null) {
            return result;
        }

        //Search for BIN in Branch cache/via webservice
        cMoveorder.currentMoveOrder.currentBranchBin  = cUser.currentUser.currentBranch.pGetBinByCode(pvBinCodeStr);
        if (cMoveorder.currentMoveOrder.currentBranchBin == null) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.activity.getString(R.string.message_bin_unknown,pvBinCodeStr));
            return result;
        }

        if (cMoveorder.currentMoveOrder.binsObl == null) {
            cMoveorder.currentMoveOrder.binsObl = new ArrayList<>();
        }

        if (!cMoveorder.currentMoveOrder.binsObl.contains(cMoveorder.currentMoveOrder.currentBranchBin)) {
            cMoveorder.currentMoveOrder.binsObl.add(cMoveorder.currentMoveOrder.currentBranchBin);
        }

        return  result;


    }

    private void mStepFailed(String pvErrorMessageStr, String pvScannedBarcodeStr) {
        cUserInterface.pDoExplodingScreen(pvErrorMessageStr, pvScannedBarcodeStr, true, true);

        if (cAppExtension.activity instanceof  MoveLinesActivity) {
            MoveLinesActivity moveLinesActivity = (MoveLinesActivity)cAppExtension.activity;
            moveLinesActivity.currentLineFragment = this;
        }

    }

    private void mNoLinesAvailable(Boolean pvEnabledBln) {

        if (cAppExtension.activity instanceof MoveLinesActivity) {
            MoveLinesActivity moveLinesActivity = (MoveLinesActivity) cAppExtension.activity;

            if (moveLinesActivity.currentLineFragment instanceof MoveLinesTakeFragment) {
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
                    return;
                }

                //Hide the recycler view
                this.recyclerViewMoveLinesTake.setVisibility(View.INVISIBLE);

                //Hide location button and clear text

                //Show nothing there fragment
                FragmentTransaction fragmentTransaction = cAppExtension.fragmentManager.beginTransaction();
                NothingHereFragment fragment = new NothingHereFragment();
                fragmentTransaction.replace(R.id.fragmentMoveLinesTake, fragment);
                fragmentTransaction.commit();
            }
        }
    }

    private  void mStartMoveLineTakeActivity(){

        this.busyBln = false;

        //we have an article or bin to handle, so start move activity
        Intent intent = new Intent(cAppExtension.context, MoveLineTakeActivity.class);
        cAppExtension.activity.startActivity(intent);

    }

    private void mRemoveAdapterFromFragment(){

        cUserInterface.pShowGettingData();

        //remove the item from recyclerview
        cResult resultRst = cMoveorderLine.currentMoveOrderLine.pResetRst();
        if (!resultRst.resultBln) {
            cUserInterface.pHideGettingData();
            this.mStepFailed(resultRst.messagesStr(),"");
            return;
        }

        //Renew data, so only current lines are shown
        this.mGetData(cMoveorder.currentMoveOrder.sortedTakeLinesObl());
        cUserInterface.pHideGettingData();
    }


    //End Region Private Methods
}






