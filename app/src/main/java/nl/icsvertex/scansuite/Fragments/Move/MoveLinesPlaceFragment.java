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
import java.util.Objects;

import ICS.Interfaces.iICSDefaultFragment;
import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cRegex;
import ICS.Utils.cResult;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Basics.BarcodeLayouts.cBarcodeLayout;
import SSU_WHS.Basics.BranchBin.cBranchBin;
import SSU_WHS.Basics.Users.cUser;
import SSU_WHS.Intake.IntakeorderBarcodes.cIntakeorderBarcode;
import SSU_WHS.Intake.IntakeorderMATLineSummary.cIntakeorderMATSummaryLine;
import SSU_WHS.Move.MoveItemVariant.cMoveItemVariant;
import SSU_WHS.Move.MoveOrders.cMoveorder;
import SSU_WHS.Move.MoveorderLines.cMoveorderLine;
import SSU_WHS.Move.MoveorderLines.cMoveorderLineAdapter;
import SSU_WHS.Move.MoveorderLines.cMoveorderLineRecyclerItemTouchHelper;
import nl.icsvertex.scansuite.Activities.Move.MoveLinePlaceActivity;
import nl.icsvertex.scansuite.Activities.Move.MoveLinesActivity;
import nl.icsvertex.scansuite.Fragments.Dialogs.NothingHereFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.SendOrderFragment;
import nl.icsvertex.scansuite.R;

public class MoveLinesPlaceFragment extends Fragment implements iICSDefaultFragment,cMoveorderLineRecyclerItemTouchHelper.RecyclerItemTouchHelperListener  {

    //Region Public Properties

    //End Region Public Properties

    //Region Private Properties

    private boolean busyBln = false;
    private List<cMoveorderLine> localLinesObl;

    private  RecyclerView recyclerViewMoveLinesPlace;

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
        return pvInflater.inflate(R.layout.fragment_movelines_place, pvContainer, false);
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

           if (!cMoveorder.currentMoveOrder.showTodoBln) {
               this.pGetData(cMoveorder.currentMoveOrder.sortedPlaceLinesObl());
           }
           else{
               this.pShowTodo();
           }
            return;
        }

        if (cMoveorderLine.currentMoveOrderLine.getQuantityHandledDbl() == 0) {
            this.mStepFailed(cAppExtension.activity.getString(R.string.message_zero_lines_cant_be_reset),"");
            if (!cMoveorder.currentMoveOrder.showTodoBln) {
                this.pGetData(cMoveorder.currentMoveOrder.sortedPlaceLinesObl());
            }
            else{
                this.pShowTodo();
            }
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

        if (cMoveorder.currentMoveOrder.showTodoBln) {
                this.pShowTodo();
            }
        else {
            this.pGetData(cMoveorder.currentMoveOrder.sortedPlaceLinesObl());
        }

    }

    @Override
    public void mFindViews() {

        if (getView() != null) {
            this.recyclerViewMoveLinesPlace = getView().findViewById(R.id.recyclerViewMoveLinesPlace);
        }

    }

    @Override
    public void mFieldsInitialize() {

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new cMoveorderLineRecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(this.recyclerViewMoveLinesPlace);

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

    public void pNoLinesAvailable(Boolean pvEnabledBln) {

        if (cAppExtension.activity instanceof MoveLinesActivity) {

            MoveLinesActivity moveLinesActivity = (MoveLinesActivity)cAppExtension.activity;

            if (moveLinesActivity.currentLineFragment instanceof MoveLinesPlaceFragment) {

                if (!pvEnabledBln) {
                    List<Fragment> fragments = cAppExtension.fragmentManager.getFragments();
                    for (Fragment fragment : fragments) {
                        if (fragment instanceof NothingHereFragment || fragment instanceof SendOrderFragment) {
                            FragmentTransaction fragmentTransaction = cAppExtension.fragmentManager.beginTransaction();
                            fragmentTransaction.remove(fragment);
                            fragmentTransaction.commit();
                        }
                    }
                    return;
                }

                this.recyclerViewMoveLinesPlace.setVisibility(View.INVISIBLE);

                FragmentTransaction fragmentTransaction = cAppExtension.fragmentManager.beginTransaction();
                NothingHereFragment fragment = new NothingHereFragment();
                fragmentTransaction.replace(R.id.fragmentMoveLinesPlace, fragment);
                fragmentTransaction.commit();

            }
        }
    }

    public  void pGetData(List<cMoveorderLine> pvDataObl) {
        this.localLinesObl = pvDataObl;
        this.mFillRecycler(this.localLinesObl);
    }

    public  void pShowTodo() {
        this.getMoveorderLineAdapter().pShowTodo();
    }

    //End Region Public Methods

    //Region Private Methods

    private void mFillRecycler(List<cMoveorderLine> pvDataObl) {

        if (pvDataObl.size() == 0) {
            this.pNoLinesAvailable(true);
            return;
        }

        this.pNoLinesAvailable(false);

        this.getMoveorderLineAdapter().pFillData(pvDataObl);
        this.recyclerViewMoveLinesPlace.setHasFixedSize(false);
        this.recyclerViewMoveLinesPlace.setAdapter(this.getMoveorderLineAdapter());
        this.recyclerViewMoveLinesPlace.setLayoutManager(new LinearLayoutManager(cAppExtension.context));
        this.recyclerViewMoveLinesPlace.setVisibility(View.VISIBLE);
    }

    private void mHandleScan(cBarcodeScan pvBarcodeScan) {

        // Show that we are getting data and set busy boolean
        this.busyBln = true;

        cResult hulpResult;

        boolean binCheckedBln = false;

        //This barcode matches multiple lay-outs so this can be a BIN or an article
        if (Objects.requireNonNull(cBarcodeLayout.pGetBarcodeLayoutByBarcodeObl(pvBarcodeScan.getBarcodeOriginalStr())).size() > 1) {

            //First check if this is a BIN
            cBranchBin branchBin =  cUser.currentUser.currentBranch.pGetBinByCode(cRegex.pStripRegexPrefixStr(pvBarcodeScan.getBarcodeOriginalStr()));

            if (branchBin != null) {

                hulpResult = this.mHandleBINScanRst(pvBarcodeScan);
                if (! hulpResult.resultBln) {
                    this.mStepFailed(hulpResult.messagesStr(),pvBarcodeScan.getBarcodeOriginalStr());
                    return;
                }

                return;
            }

            binCheckedBln = true;
        }

        //Check if we have scanned a BIN and check if there are not handled linesInt for this BIN
        if (!binCheckedBln && cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(), cBarcodeLayout.barcodeLayoutEnu.BIN)) {

            hulpResult = this.mHandleBINScanRst(pvBarcodeScan);
            if (! hulpResult.resultBln) {
                this.mStepFailed(hulpResult.messagesStr(),pvBarcodeScan.getBarcodeOriginalStr());
                return;
            }

            return;

        }

        //Check if we have scanned an ARTICLE and check if there are not handled linesInt for this BIN
        if (cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(), cBarcodeLayout.barcodeLayoutEnu.ARTICLE)) {

            hulpResult = this.mHandleArticleScanRst(pvBarcodeScan);
            if (! hulpResult.resultBln) {
                this.mStepFailed(hulpResult.messagesStr(),pvBarcodeScan.getBarcodeOriginalStr());
                return;
            }
            return;
    }

        this.busyBln = false;

    }

    private cResult mHandleBINScanRst(cBarcodeScan pvBarcodeScan) {

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
        this.mStartMoveLinePlaceActivity();
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

    private cResult mHandleArticleScanRst(cBarcodeScan pvBarcodeScan){

        cResult result = new cResult();
        result.resultBln = true;

        //Check if we already know the barcode
        cMoveorder.currentMoveOrder.moveorderBarcodeToHandle = cMoveorder.currentMoveOrder.pGetOrderBarcode(pvBarcodeScan);

        //This is a new barcode, this should not be possible during placing
        if (cMoveorder.currentMoveOrder.moveorderBarcodeToHandle == null) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.activity.getString(R.string.message_unknown_barcode));
            return  result;
        }

        //Get ItemVariant with the key of the barcode
        cMoveorder.currentMoveOrder.currentMoveItemVariant = cMoveItemVariant.allMoveItemVariantObl.get( cMoveorder.currentMoveOrder.moveorderBarcodeToHandle.getKeyStr());
        if (cMoveorder.currentMoveOrder.currentMoveItemVariant == null) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.activity.getString(R.string.message_unknown_itemvariant));
            return  result;
        }

        //Check if we can place something for this variant
        if ( Objects.requireNonNull(cMoveorder.currentMoveOrder.currentMoveItemVariant).getQuantityTakenDbl() <= 0 ) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.activity.getString(R.string.message_itemvariant_not_taken_so_cant_be_placed));
            return  result;
        }

        //Check if we don't have a take line for this article and BIN otherwise you would place the item on the same BIN as where you took it from
        result = this.mCheckForTakeLineRst();
        if (!result.resultBln) {
            return  result;
        }

        //Get article from cache
        cMoveorder.currentMoveOrder.currentArticle = cMoveorder.currentMoveOrder.articleObl.get(cMoveorder.currentMoveOrder.moveorderBarcodeToHandle.getItemNoAndVariantCodeStr());
        if (cMoveorder.currentMoveOrder.currentArticle == null) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.activity.getString(R.string.message_unknown_article));
            return  result;
        }

        //Set the scanned barcodeStr, so we can raise quantity in next activity
        this.mStartMoveLinePlaceActivity();
        return result;

    }

    private cResult mCheckForTakeLineRst(){

        cResult result = new cResult();
        result.resultBln = true;

        cMoveorderLine moveorderLine = cMoveorder.currentMoveOrder.pGetTakeLineForCurrentArticleAndBin();
        if (moveorderLine == null) {
            return  result;
        }

        result.resultBln = false;
        result.pAddErrorMessage(cAppExtension.activity.getString(R.string.message_already_exists_reset_first));
        return  result;



    }

    private  void mStartMoveLinePlaceActivity(){

        this.busyBln = false;

        //we have an article or bin to handle, so start move activity
        Intent intent = new Intent(cAppExtension.context, MoveLinePlaceActivity.class);
        cAppExtension.activity.startActivity(intent);

    }

    private void mStepFailed(String pvErrorMessageStr, String pvScannedBarcodeStr) {
        cUserInterface.pDoExplodingScreen(pvErrorMessageStr, pvScannedBarcodeStr, true, true);
    }

    private void mRemoveAdapterFromFragment() {

        cUserInterface.pShowGettingData();

        //remove the item from recyclerview
        cResult resultRst = cMoveorderLine.currentMoveOrderLine.pResetRst();
        if (!resultRst.resultBln) {
            cUserInterface.pHideGettingData();
            this.mStepFailed(resultRst.messagesStr(), "");
            return;
        }

        //Renew data, so only current lines are shown
        if (!cMoveorder.currentMoveOrder.showTodoBln) {
            this.pGetData(cMoveorder.currentMoveOrder.sortedPlaceLinesObl());
        } else {
            this.pShowTodo();
        }

        cUserInterface.pHideGettingData();

    }
    //End Region Private Methods
}






