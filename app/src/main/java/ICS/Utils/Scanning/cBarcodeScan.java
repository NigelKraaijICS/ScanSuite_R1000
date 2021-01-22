package ICS.Utils.Scanning;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.crashlytics.FirebaseCrashlytics;

import java.util.Objects;

import ICS.Utils.cText;
import ICS.cAppExtension;
import nl.icsvertex.scansuite.Activities.FinishShip.FinishShipLinesActivity;
import nl.icsvertex.scansuite.Activities.FinishShip.FinishShiporderSelectActivity;
import nl.icsvertex.scansuite.Activities.General.BarcodeInfoActivity;
import nl.icsvertex.scansuite.Activities.General.LoginActivity;
import nl.icsvertex.scansuite.Activities.Intake.CreateIntakeActivity;
import nl.icsvertex.scansuite.Activities.Intake.IntakeOrderIntakeActivity;
import nl.icsvertex.scansuite.Activities.Intake.IntakeOrderIntakeGeneratedActivity;
import nl.icsvertex.scansuite.Activities.Intake.IntakeorderMASLinesActivity;
import nl.icsvertex.scansuite.Activities.Intake.IntakeorderMATLinesActivity;
import nl.icsvertex.scansuite.Activities.Move.CreateMoveActivity;
import nl.icsvertex.scansuite.Activities.Move.MoveLinePlaceActivity;
import nl.icsvertex.scansuite.Activities.Move.MoveLinePlaceGeneratedActivity;
import nl.icsvertex.scansuite.Activities.Move.MoveLinePlaceMTActivity;
import nl.icsvertex.scansuite.Activities.Move.MoveLineTakeActivity;
import nl.icsvertex.scansuite.Activities.Move.MoveLineTakeMTActivity;
import nl.icsvertex.scansuite.Activities.Move.MoveLinesActivity;
import nl.icsvertex.scansuite.Activities.Move.MoveLinesPlaceMTActivity;
import nl.icsvertex.scansuite.Activities.Move.MoveLinesTakeMTActivity;
import nl.icsvertex.scansuite.Activities.Move.MoveMISinglepieceActivity;
import nl.icsvertex.scansuite.Activities.Move.MoveorderLinesPlaceGeneratedActivity;
import nl.icsvertex.scansuite.Activities.Move.MoveorderSelectActivity;
import nl.icsvertex.scansuite.Activities.PackAndShip.PackAndShipMultiActivity;
import nl.icsvertex.scansuite.Activities.PackAndShip.PackAndShipSingleActivity;
import nl.icsvertex.scansuite.Activities.Pick.PickorderLinesGeneratedActivity;
import nl.icsvertex.scansuite.Activities.Pick.PickorderPickGeneratedActivity;
import nl.icsvertex.scansuite.Activities.QualityControl.PickorderQCActivity;
import nl.icsvertex.scansuite.Activities.QualityControl.QualityControlLinesActivity;
import nl.icsvertex.scansuite.Activities.Receive.CreateReceiveActivity;
import nl.icsvertex.scansuite.Activities.IntakeAndReceive.IntakeAndReceiveSelectActivity;
import nl.icsvertex.scansuite.Activities.Inventory.InventoryorderBinActivity;
import nl.icsvertex.scansuite.Activities.Inventory.InventoryorderBinsActivity;
import nl.icsvertex.scansuite.Activities.Inventory.InventoryorderSelectActivity;
import nl.icsvertex.scansuite.Activities.Pick.PickorderLinesActivity;
import nl.icsvertex.scansuite.Activities.Pick.PickorderPickActivity;
import nl.icsvertex.scansuite.Activities.Pick.PickorderSelectActivity;
import nl.icsvertex.scansuite.Activities.Receive.ReceiveLinesActivity;
import nl.icsvertex.scansuite.Activities.Receive.ReceiveOrderReceiveActivity;
import nl.icsvertex.scansuite.Activities.Returns.CreateReturnActivity;
import nl.icsvertex.scansuite.Activities.Returns.ReturnorderDocumentActivity;
import nl.icsvertex.scansuite.Activities.Returns.ReturnorderDocumentsActivity;
import nl.icsvertex.scansuite.Activities.Returns.ReturnorderSelectActivity;
import nl.icsvertex.scansuite.Activities.Ship.ShiporderLinesActivity;
import nl.icsvertex.scansuite.Activities.Ship.ShiporderSelectActivity;
import nl.icsvertex.scansuite.Activities.Ship.ShiporderShipActivity;
import nl.icsvertex.scansuite.Activities.Sort.SortorderLinesActivity;
import nl.icsvertex.scansuite.Activities.Sort.SortorderSelectActivity;
import nl.icsvertex.scansuite.Activities.Sort.SortorderSortActivity;
import nl.icsvertex.scansuite.Activities.Store.StoreorderLinesActivity;
import nl.icsvertex.scansuite.Activities.Store.StoreorderSelectActivity;
import nl.icsvertex.scansuite.Fragments.Dialogs.AddArticleFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.AddBinFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.AddEnvironmentFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.ArticleFullViewFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.BranchFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.CurrentLocationFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.EnvironmentFragment;
import nl.icsvertex.scansuite.Activities.Pick.PickorderLineItemPropertyInputActvity;
import nl.icsvertex.scansuite.Fragments.Dialogs.ScanArticleFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.ScanBinFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.SetBinFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.StepDoneFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.PasswordFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.ReasonFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.WorkplaceFragment;
import nl.icsvertex.scansuite.Activities.Inventory.CreateInventoryActivity;
import nl.icsvertex.scansuite.Activities.Inventory.InventoryArticleActivity;
import nl.icsvertex.scansuite.Activities.Returns.ReturnArticleDetailActivity;

public class cBarcodeScan {

    private static String currentActivityContextStr = "";

    public static class BarcodeType {

        public static final int Unknown = 0;
        public static final int EAN8 = 1;
        public static final int EAN13 = 2;
    }

    private String barcodeFormattedStr;
    public String getBarcodeFormattedStr() {
        return barcodeFormattedStr;
    }

    public String barcodeOriginalStr;
    public String getBarcodeOriginalStr() {
        return barcodeOriginalStr;
    }

    public String barcodeTypeStr;
    public String getBarcodeTypeStr() {
        return barcodeTypeStr;
    }

    private   Boolean containsCrlf = false;
    public Boolean getContainsCrlfBln() {
        return containsCrlf;
    }

    public cBarcodeScan(){

    }

    private static IntentFilter BarcodeIntentFilter;
    private static void getBarcodeIntentFilter() {
        if (BarcodeIntentFilter == null) {
            BarcodeIntentFilter = new IntentFilter();
            for (String barcodeActionStr : cBarcodeScanDefinitions.getBarcodeActions()) {
                BarcodeIntentFilter.addAction(barcodeActionStr);
            }
            for (String barcodeCategorieStr : cBarcodeScanDefinitions.getBarcodeCategories()) {
                BarcodeIntentFilter.addCategory(barcodeCategorieStr);
            }
        }
    }

    private static IntentFilter BarcodeFragmentIntentFilter;
    private static void getBarcodeFragmentIntentFilter() {
        if (BarcodeFragmentIntentFilter == null) {
            BarcodeFragmentIntentFilter = new IntentFilter();
            for (String barcodeActionStr : cBarcodeScanDefinitions.getBarcodeActions()) {
                BarcodeFragmentIntentFilter.addAction(barcodeActionStr);
            }
            for (String barcodeCategorieStr : cBarcodeScanDefinitions.getBarcodeCategories()) {
                BarcodeFragmentIntentFilter.addCategory(barcodeCategorieStr);
            }
        }
    }

    private static BroadcastReceiver BarcodeReceiver;
    private static void getBarcodeReceiver() {
        if (BarcodeReceiver == null) {
            BarcodeReceiver = new BroadcastReceiver(){
                @Override
                public void onReceive(Context pvContext, Intent pvIntent) {

                    if (pvContext != cAppExtension.context) {
                        return;
                    }

                    //Fill a barcodeStr scan object
                    cBarcodeScan barcodeScan = ICS.Utils.Scanning.cBarcodeScan.mGetBarcode(pvIntent);

                    FirebaseCrashlytics.getInstance().setCustomKey("LastScannedBarcode", barcodeScan.getBarcodeOriginalStr());

                    //Login
                    if (cAppExtension.activity instanceof LoginActivity) {
                        LoginActivity loginActivity = (LoginActivity)cAppExtension.activity;
                        loginActivity.pHandleScan(barcodeScan);
                       }

                    //BarcodeInfo
                    if (cAppExtension.activity instanceof BarcodeInfoActivity) {
                        BarcodeInfoActivity barcodeInfoActivity = (BarcodeInfoActivity)cAppExtension.activity;
                        barcodeInfoActivity.pHandleScan(barcodeScan);
                    }

                    //Pick
                    if (cAppExtension.activity instanceof PickorderSelectActivity){
                        PickorderSelectActivity pickorderSelectActivity = (PickorderSelectActivity)cAppExtension.activity;
                        pickorderSelectActivity.pHandleScan(barcodeScan);
                    }

                    if (cAppExtension.activity instanceof PickorderLinesActivity) {
                        PickorderLinesActivity pickorderLinesActivity = (PickorderLinesActivity)cAppExtension.activity;
                        pickorderLinesActivity.pHandleScan(barcodeScan, false);
                    }

                    if (cAppExtension.activity instanceof PickorderLinesGeneratedActivity) {
                        PickorderLinesGeneratedActivity pickorderLinesGeneratedActivity = (PickorderLinesGeneratedActivity)cAppExtension.activity;
                        pickorderLinesGeneratedActivity.pHandleScan(barcodeScan);
                    }

                    if (cAppExtension.activity instanceof PickorderPickActivity) {
                        PickorderPickActivity pickorderPickActivity = (PickorderPickActivity)cAppExtension.activity;
                        pickorderPickActivity.pHandleScan(barcodeScan);
                    }

                    if (cAppExtension.activity instanceof PickorderPickGeneratedActivity) {
                        PickorderPickGeneratedActivity pickorderPickGeneratedActivity = (PickorderPickGeneratedActivity)cAppExtension.activity;
                        pickorderPickGeneratedActivity.pHandleScan(barcodeScan);
                    }

                    //Sort
                    if (cAppExtension.activity instanceof SortorderSelectActivity){
                        SortorderSelectActivity sortorderSelectActivity = (SortorderSelectActivity)cAppExtension.activity;
                        sortorderSelectActivity.pHandleScan(barcodeScan);
                    }

                    if (cAppExtension.activity instanceof SortorderLinesActivity){
                        SortorderLinesActivity sortorderLinesActivity = (SortorderLinesActivity)cAppExtension.activity;
                        sortorderLinesActivity.pHandleScan(barcodeScan, false);
                    }

                    if (cAppExtension.activity instanceof SortorderSortActivity){
                        SortorderSortActivity sortorderSortActivity = (SortorderSortActivity)cAppExtension.activity;
                        sortorderSortActivity.pHandleScan(barcodeScan);
                    }

                    //Ship
                    if (cAppExtension.activity instanceof ShiporderSelectActivity){
                        ShiporderSelectActivity shiporderSelectActivity = (ShiporderSelectActivity)cAppExtension.activity;
                        shiporderSelectActivity.pHandleScan(barcodeScan);
                    }

                    if (cAppExtension.activity instanceof ShiporderLinesActivity){
                        ShiporderLinesActivity shiporderLinesActivity = (ShiporderLinesActivity)cAppExtension.activity;
                        shiporderLinesActivity.pHandleScan(barcodeScan, false);
                    }

                    if (cAppExtension.activity instanceof ShiporderShipActivity){
                        ShiporderShipActivity shiporderShipActivity = (ShiporderShipActivity)cAppExtension.activity;
                        shiporderShipActivity.pHandleScan(barcodeScan);
                    }

                    //Finish Single Pieces
                    if (cAppExtension.activity instanceof FinishShiporderSelectActivity){
                        FinishShiporderSelectActivity finishShiporderSelectActivity = (FinishShiporderSelectActivity)cAppExtension.activity;
                        finishShiporderSelectActivity.pHandleScan(barcodeScan);
                    }

                    if (cAppExtension.activity instanceof FinishShipLinesActivity){
                        FinishShipLinesActivity finishShipLinesActivity = (FinishShipLinesActivity)cAppExtension.activity;
                        finishShipLinesActivity.pHandleScan(barcodeScan);
                    }

                    //QC
                    if (cAppExtension.activity instanceof QualityControlLinesActivity){
                        QualityControlLinesActivity qualityControlLinesActivity = (QualityControlLinesActivity)cAppExtension.activity;
                        qualityControlLinesActivity.pHandleScan(barcodeScan);
                    }

                    if (cAppExtension.activity instanceof PickorderQCActivity){
                        PickorderQCActivity pickorderQCActivity = (PickorderQCActivity)cAppExtension.activity;
                        pickorderQCActivity.pHandleScan(barcodeScan);
                    }

                    //Inventory
                    if (cAppExtension.activity instanceof CreateInventoryActivity) {
                        CreateInventoryActivity createInventoryActivity = (CreateInventoryActivity)cAppExtension.activity;
                        createInventoryActivity.pHandleScan(barcodeScan);
                        return;
                    }

                    if (cAppExtension.activity instanceof InventoryorderSelectActivity){
                        InventoryorderSelectActivity inventoryorderSelectActivity = (InventoryorderSelectActivity)cAppExtension.activity;
                        inventoryorderSelectActivity.pHandleScan(barcodeScan);
                    }

                    if (cAppExtension.activity instanceof InventoryorderBinsActivity){
                        InventoryorderBinsActivity inventoryorderBinsActivity = (InventoryorderBinsActivity)cAppExtension.activity;
                        inventoryorderBinsActivity.pHandleScan(barcodeScan);
                    }

                    if (cAppExtension.activity instanceof InventoryorderBinActivity){
                        InventoryorderBinActivity inventoryorderBinActivity = (InventoryorderBinActivity)cAppExtension.activity;
                        inventoryorderBinActivity.pHandleScan(barcodeScan, false);
                    }
                    if (cAppExtension.activity instanceof InventoryArticleActivity) {
                        InventoryArticleActivity inventoryArticleDetailActivity = (InventoryArticleActivity)cAppExtension.activity;
                        inventoryArticleDetailActivity.pHandleScan(barcodeScan);
                        return;
                    }


                    //Intake
                    if (cAppExtension.activity instanceof IntakeAndReceiveSelectActivity){
                        IntakeAndReceiveSelectActivity intakeAndReceiveSelectActivity = (IntakeAndReceiveSelectActivity)cAppExtension.activity;
                        intakeAndReceiveSelectActivity.pHandleScan(barcodeScan);
                    }

                    if (cAppExtension.activity instanceof CreateReceiveActivity){
                        CreateReceiveActivity createReceiveActivity = (CreateReceiveActivity)cAppExtension.activity;
                        createReceiveActivity.pHandleScan(barcodeScan,false,false,false);
                    }

                    if (cAppExtension.activity instanceof CreateIntakeActivity){
                        CreateIntakeActivity createIntakeActivity = (CreateIntakeActivity)cAppExtension.activity;
                        createIntakeActivity.pHandleScan(barcodeScan);
                    }

                    if (cAppExtension.activity instanceof ReceiveLinesActivity){
                        ReceiveLinesActivity receiveLinesActivity = (ReceiveLinesActivity)cAppExtension.activity;
                        receiveLinesActivity.pHandleScan(barcodeScan,false);
                    }

                    if (cAppExtension.activity instanceof ReceiveOrderReceiveActivity){
                        ReceiveOrderReceiveActivity receiveOrderReceiveActivity = (ReceiveOrderReceiveActivity)cAppExtension.activity;
                        receiveOrderReceiveActivity.pHandleScan(barcodeScan);
                    }


                    if (cAppExtension.activity instanceof IntakeorderMATLinesActivity){
                        IntakeorderMATLinesActivity intakeorderMATLinesActivity = (IntakeorderMATLinesActivity)cAppExtension.activity;
                        intakeorderMATLinesActivity.pHandleScan(barcodeScan,false);
                    }

                    if (cAppExtension.activity instanceof IntakeorderMASLinesActivity){
                        IntakeorderMASLinesActivity intakeorderMASLinesActivity = (IntakeorderMASLinesActivity)cAppExtension.activity;
                        intakeorderMASLinesActivity.pHandleScan(barcodeScan, false);
                    }

                    if (cAppExtension.activity instanceof IntakeOrderIntakeActivity){
                        IntakeOrderIntakeActivity intakeOrderIntakeActivity = (IntakeOrderIntakeActivity)cAppExtension.activity;
                        intakeOrderIntakeActivity.pHandleScan(barcodeScan);
                    }

                    if (cAppExtension.activity instanceof IntakeOrderIntakeGeneratedActivity){
                        IntakeOrderIntakeGeneratedActivity intakeOrderIntakeGeneratedActivity = (IntakeOrderIntakeGeneratedActivity)cAppExtension.activity;
                        intakeOrderIntakeGeneratedActivity.pHandleScan(barcodeScan);
                    }

                    //Return
                    if (cAppExtension.activity instanceof ReturnorderSelectActivity){
                        ReturnorderSelectActivity returnorderSelectActivity = (ReturnorderSelectActivity)cAppExtension.activity;
                        returnorderSelectActivity.pHandleScan(barcodeScan);
                    }

                    if (cAppExtension.activity instanceof ReturnorderDocumentsActivity){
                        ReturnorderDocumentsActivity returnorderDocumentsActivity = (ReturnorderDocumentsActivity)cAppExtension.activity;
                        returnorderDocumentsActivity.pHandleScan(barcodeScan, false);
                    }

                    if (cAppExtension.activity instanceof ReturnorderDocumentActivity){
                        ReturnorderDocumentActivity returnorderDocumentActivity = (ReturnorderDocumentActivity)cAppExtension.activity;
                        returnorderDocumentActivity.pHandleScan(barcodeScan);
                    }

                    if (cAppExtension.activity instanceof CreateReturnActivity) {
                        CreateReturnActivity createReturnActivity = (CreateReturnActivity)cAppExtension.activity;
                        createReturnActivity.pHandleScan(barcodeScan,false,false);
                    }

                    if (cAppExtension.activity instanceof ReturnArticleDetailActivity) {
                        ReturnArticleDetailActivity returnArticleDetailActivity = (ReturnArticleDetailActivity)cAppExtension.activity;
                        returnArticleDetailActivity.pHandleScan(barcodeScan);
                    }

                    //Move
                    if (cAppExtension.activity instanceof MoveorderSelectActivity){
                        MoveorderSelectActivity moveorderSelectActivity = (MoveorderSelectActivity)cAppExtension.activity;
                        moveorderSelectActivity.pHandleScan(barcodeScan);
                    }

                    if (cAppExtension.activity instanceof CreateMoveActivity){
                        CreateMoveActivity createMoveActivity = (CreateMoveActivity)cAppExtension.activity;
                        createMoveActivity.pHandleScan(barcodeScan, false,false);
                    }

                    if (cAppExtension.activity instanceof MoveLinesActivity) {
                        MoveLinesActivity moveLinesActivity = (MoveLinesActivity)cAppExtension.activity;
                        moveLinesActivity.pHandleScan(barcodeScan);
                    }

                    if (cAppExtension.activity instanceof MoveLinesTakeMTActivity) {
                        MoveLinesTakeMTActivity moveLinesTakeMTActivity = (MoveLinesTakeMTActivity)cAppExtension.activity;
                        moveLinesTakeMTActivity.pHandleScan(barcodeScan);
                    }
                    if (cAppExtension.activity instanceof MoveLineTakeMTActivity) {
                        MoveLineTakeMTActivity moveLineTakeMTActivity = (MoveLineTakeMTActivity)cAppExtension.activity;
                        moveLineTakeMTActivity.pHandleScan(barcodeScan);
                    }

                    if (cAppExtension.activity instanceof MoveLinesPlaceMTActivity) {
                        MoveLinesPlaceMTActivity moveLinesPlaceMTActivity = (MoveLinesPlaceMTActivity)cAppExtension.activity;
                        moveLinesPlaceMTActivity.pHandleScan(barcodeScan);
                    }

                    if (cAppExtension.activity instanceof MoveLinePlaceMTActivity) {
                        MoveLinePlaceMTActivity moveLinePlaceMTActivity = (MoveLinePlaceMTActivity)cAppExtension.activity;
                        moveLinePlaceMTActivity.pHandleScan(barcodeScan);
                    }

                    if (cAppExtension.activity instanceof MoveLineTakeActivity){
                        MoveLineTakeActivity moveLineTakeActivity = (MoveLineTakeActivity)cAppExtension.activity;
                        moveLineTakeActivity.pHandleScan(barcodeScan);
                    }

                    if (cAppExtension.activity instanceof MoveLinePlaceActivity){
                        MoveLinePlaceActivity moveLinePlaceActivity = (MoveLinePlaceActivity)cAppExtension.activity;
                        moveLinePlaceActivity.pHandleScan(barcodeScan);
                    }

                    if (cAppExtension.activity instanceof MoveMISinglepieceActivity){
                        MoveMISinglepieceActivity moveMISinglepieceActivity = (MoveMISinglepieceActivity)cAppExtension.activity;
                        moveMISinglepieceActivity.pHandleScan(barcodeScan);
                    }

                    if (cAppExtension.activity instanceof MoveorderLinesPlaceGeneratedActivity){
                        MoveorderLinesPlaceGeneratedActivity moveorderLinesPlaceGeneratedActivity = (MoveorderLinesPlaceGeneratedActivity)cAppExtension.activity;
                        moveorderLinesPlaceGeneratedActivity.pHandleScan(barcodeScan);
                    }

                    if (cAppExtension.activity instanceof MoveLinePlaceGeneratedActivity){
                        MoveLinePlaceGeneratedActivity moveLinePlaceGeneratedActivity = (MoveLinePlaceGeneratedActivity)cAppExtension.activity;
                        moveLinePlaceGeneratedActivity.pHandleScan(barcodeScan);
                    }

                    if (cAppExtension.activity instanceof PackAndShipSingleActivity){
                        PackAndShipSingleActivity packAndShipSingleActivity = (PackAndShipSingleActivity)cAppExtension.activity;
                        packAndShipSingleActivity.pHandleScan(barcodeScan);
                    }

                    if (cAppExtension.activity instanceof PackAndShipMultiActivity){
                        PackAndShipMultiActivity packAndShipMultiActivity = (PackAndShipMultiActivity)cAppExtension.activity;
                        packAndShipMultiActivity.pHandleScan(barcodeScan);
                    }

                    if (cAppExtension.activity instanceof StoreorderSelectActivity){
                        StoreorderSelectActivity storeorderSelectActivity = (StoreorderSelectActivity)cAppExtension.activity;
                        storeorderSelectActivity.pHandleScan(barcodeScan);
                    }

                    if (cAppExtension.activity instanceof StoreorderLinesActivity){
                        StoreorderLinesActivity storeorderLinesActivity = (StoreorderLinesActivity)cAppExtension.activity;
                        storeorderLinesActivity.pHandleScan(barcodeScan, false);
                    }

                    if (cAppExtension.activity instanceof PickorderLineItemPropertyInputActvity) {
                        PickorderLineItemPropertyInputActvity pickorderLineItemPropertyInputActvity = (PickorderLineItemPropertyInputActvity)cAppExtension.activity;
                        pickorderLineItemPropertyInputActvity.pHandleScan(barcodeScan);
                    }


                }
            };
        }
    }

    private static BroadcastReceiver BarcodeFragmentReceiver;
    private static void getBarcodeFragmentReceiver() {
        if (BarcodeFragmentReceiver == null) {
            BarcodeFragmentReceiver = new BroadcastReceiver(){

                @Override
                public void onReceive(Context context, Intent intent) {

                    if (context != cAppExtension.context) {
                        return;
                    }

                    //Fill a barcodeStr scan object
                    cBarcodeScan barcodeScan = ICS.Utils.Scanning.cBarcodeScan.mGetBarcode(intent);
                    FirebaseCrashlytics.getInstance().setCustomKey("LastScannedBarcode", barcodeScan.getBarcodeOriginalStr());

                    if (cAppExtension.dialogFragment instanceof BranchFragment) {
                        BranchFragment branchFragment = (BranchFragment)cAppExtension.dialogFragment;
                        branchFragment.pHandleScan(barcodeScan);
                        return;
                    }

                    if(cAppExtension.dialogFragment instanceof StepDoneFragment) {
                        StepDoneFragment stepDoneFragment = (StepDoneFragment)cAppExtension.dialogFragment;
                        stepDoneFragment.pHandleScan(barcodeScan);
                        return;
                    }

                    if(cAppExtension.dialogFragment instanceof WorkplaceFragment) {
                        WorkplaceFragment workplaceFragment = (WorkplaceFragment)cAppExtension.dialogFragment;
                        workplaceFragment.pHandleScan(barcodeScan);
                        return;
                    }

                    if(cAppExtension.dialogFragment instanceof CurrentLocationFragment) {
                        CurrentLocationFragment currentLocationFragment = (CurrentLocationFragment)cAppExtension.dialogFragment;
                        currentLocationFragment.pHandleScan(barcodeScan);
                        return;
                    }

                    if(cAppExtension.dialogFragment instanceof ArticleFullViewFragment) {
                        ArticleFullViewFragment articleFullViewFragment = (ArticleFullViewFragment)cAppExtension.dialogFragment;
                        articleFullViewFragment.pHandleScan(barcodeScan);
                        return;
                    }

                    if (cAppExtension.dialogFragment instanceof EnvironmentFragment) {
                        EnvironmentFragment environmentFragment = (EnvironmentFragment)cAppExtension.dialogFragment;
                        environmentFragment.pHandleScan(barcodeScan);
                        return;
                    }

                    if (cAppExtension.dialogFragment instanceof AddEnvironmentFragment) {
                        AddEnvironmentFragment addEnvironmentFragment = (AddEnvironmentFragment)cAppExtension.dialogFragment;
                        addEnvironmentFragment.pHandleScan(barcodeScan);
                        return;
                    }

                    if (cAppExtension.dialogFragment instanceof AddBinFragment) {
                        AddBinFragment addBinFragment = (AddBinFragment)cAppExtension.dialogFragment;
                        addBinFragment.pHandleScan(barcodeScan);
                        return;
                    }

                    if (cAppExtension.dialogFragment instanceof SetBinFragment) {
                        SetBinFragment setBinFragment = (SetBinFragment)cAppExtension.dialogFragment;
                        setBinFragment.pHandleScan(barcodeScan);
                        return;
                    }

                    if (cAppExtension.dialogFragment instanceof AddArticleFragment) {
                        AddArticleFragment addArticleFragment = (AddArticleFragment)cAppExtension.dialogFragment;
                        addArticleFragment.pHandleScan(barcodeScan);
                        return;
                    }

                    if (cAppExtension.dialogFragment instanceof ScanArticleFragment) {
                        ScanArticleFragment scanArticleFragment = (ScanArticleFragment)cAppExtension.dialogFragment;
                        scanArticleFragment.pHandleScan(barcodeScan);
                        return;
                    }


                    if (cAppExtension.dialogFragment instanceof ScanBinFragment) {
                        ScanBinFragment scanBinFragment = (ScanBinFragment)cAppExtension.dialogFragment;
                        scanBinFragment.pHandleScan(barcodeScan);
                        return;
                    }

                    if (cAppExtension.dialogFragment instanceof PasswordFragment) {
                        PasswordFragment passwordFragment = (PasswordFragment)cAppExtension.dialogFragment;
                        passwordFragment.pHandleScan(barcodeScan);
                        return;
                    }

                    if (cAppExtension.dialogFragment instanceof ReasonFragment) {
                        ReasonFragment reasonFragment = (ReasonFragment)cAppExtension.dialogFragment;
                        reasonFragment.pHandleScan(barcodeScan);
                    }

                }
            };
        }
    }

    public static void pRegisterBarcodeReceiver(String pvClassNameStr){

        Log.i("ICS","pRegisterBarcodeReceiver: " +   pvClassNameStr);

        //Turn off other receiver
        cBarcodeScan.pUnregisterBarcodeFragmentReceiver(pvClassNameStr);

        //Initialise this receiver
        cBarcodeScan.getBarcodeIntentFilter();
        cBarcodeScan.getBarcodeReceiver();

        //Prevent multiple receivers on same context
        if (cBarcodeScan.currentActivityContextStr.equalsIgnoreCase(cAppExtension.context.getClass().getSimpleName())) {
             return;
        }

        //Attach receiver to context
        cAppExtension.context.registerReceiver(BarcodeReceiver,BarcodeIntentFilter);
        cBarcodeScan.currentActivityContextStr  = cAppExtension.context.getClass().getSimpleName();



    }

    public static void pRegisterBarcodeFragmentReceiver(String pvClassNameStr){

        Log.i("ICS","pRegisterBarcodeFragmentReceiver: " +   pvClassNameStr);

        //Turn off other receiver
        cBarcodeScan.pUnregisterBarcodeReceiver(pvClassNameStr);

        //Initialise this receiver
        cBarcodeScan.getBarcodeFragmentIntentFilter();
        cBarcodeScan.getBarcodeFragmentReceiver();

        //Attach receiver to context
        cAppExtension.context.registerReceiver(BarcodeFragmentReceiver,BarcodeFragmentIntentFilter);

    }

    public static void pUnregisterBarcodeReceiver(String pvClassNameStr){

        Log.i("ICS","pUnregisterBarcodeReceiver: " +   pvClassNameStr);

        try {
         cAppExtension.context.unregisterReceiver(BarcodeReceiver);
            cBarcodeScan.currentActivityContextStr = "";
        } catch(IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public static void pUnregisterBarcodeFragmentReceiver(String pvClassNameStr){

        Log.i("ICS","pUnregisterBarcodeFragmentReceiver: " +   pvClassNameStr);

        try {
            cAppExtension.context.unregisterReceiver(BarcodeFragmentReceiver);
        } catch(IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public static cBarcodeScan pFakeScan(String pvBarcodeStr) {
        cBarcodeScan result = new cBarcodeScan();
        result.barcodeOriginalStr = pvBarcodeStr;
        result.barcodeFormattedStr = pvBarcodeStr;
        result.barcodeTypeStr = cText.pIntToStringStr( BarcodeType.Unknown);
        return  result;
    }

    private static cBarcodeScan mGetBarcode(Intent pvIntent) {

        cBarcodeScan resultBarcodeScan;

        Bundle extras = pvIntent.getExtras();
        String returnBarcodeStr;
        String scannedBarcodeStr = "";
        String barcodeTypeStr = "";


        if (extras != null) {

            //so who is sending us this?
            if (Objects.requireNonNull(pvIntent.getAction()).equalsIgnoreCase(cBarcodeScanDefinitions.BARCODEINTENT_DATALOGIC_ACTION)) {
                scannedBarcodeStr = extras.getString(cBarcodeScanDefinitions.BARCODEINTENT_DATALOGIC_EXTRABARCODE);
                barcodeTypeStr = extras.getString(cBarcodeScanDefinitions.BARCODEINTENT_DATALOGIC_EXTRABARCODETYPE);
            }

            if (pvIntent.getAction().equalsIgnoreCase(cBarcodeScanDefinitions.BARCODEINTENT_ZEBRA_ACTION)) {
                scannedBarcodeStr = extras.getString(cBarcodeScanDefinitions.BARCODEINTENT_ZEBRA_EXTRABARCODE);
                barcodeTypeStr = extras.getString(cBarcodeScanDefinitions.BARCODEINTENT_ZEBRA_EXTRABARCODETYPE);
            }

            if (pvIntent.getAction().equalsIgnoreCase(cBarcodeScanDefinitions.BARCODEINTENT_TC55_ACTION)) {
                scannedBarcodeStr = extras.getString(cBarcodeScanDefinitions.BARCODEINTENT_TC55_EXTRABARCODE);
                barcodeTypeStr = extras.getString(cBarcodeScanDefinitions.BARCODEINTENT_TC55_EXTRABARCODETYPE);
            }

            if (pvIntent.getAction().equalsIgnoreCase(cBarcodeScanDefinitions.BARCODEINTENT_PROGLOVE_ACTION)) {
                scannedBarcodeStr = extras.getString(cBarcodeScanDefinitions.BARCODEINTENT_PROGLOVE_EXTRABARCODE);
                barcodeTypeStr = extras.getString(cBarcodeScanDefinitions.BARCODEINTENT_PROGLOVE_EXTRABARCODETYPE);
            }

            if (pvIntent.getAction().equalsIgnoreCase(cBarcodeScanDefinitions.BARCODEINTENT_HONEYWELL_ACTION)) {
                scannedBarcodeStr = extras.getString(cBarcodeScanDefinitions.BARCODEINTENT_HONEYWELL_EXTRABARCODE);
                String honeywellBarcodeType = extras.getString(cBarcodeScanDefinitions.BARCODEINTENT_HONEYWELL_EXTRABARCODETYPE);
                barcodeTypeStr = cBarcodeScanDefinitions.pGetHoneyWellBarcodeTypeStr(honeywellBarcodeType);
            }


        }

        assert scannedBarcodeStr != null;




        scannedBarcodeStr = mCleanBarcodeStr(scannedBarcodeStr);
        returnBarcodeStr =  mCleanBarcodeStr(scannedBarcodeStr) ;

        //If it's an EAN barcodeStr cut off checkdigit
        assert barcodeTypeStr != null;
        if (barcodeTypeStr.toUpperCase().contains("EAN") ) {

            if (returnBarcodeStr.length() == 13) {
                returnBarcodeStr = returnBarcodeStr.substring(0,12);
            }

            if (returnBarcodeStr.length() == 8) {
                returnBarcodeStr = returnBarcodeStr.substring(0,8);
            }
        }

        resultBarcodeScan = new cBarcodeScan();
        resultBarcodeScan.barcodeOriginalStr = scannedBarcodeStr;
        resultBarcodeScan.barcodeFormattedStr = returnBarcodeStr;
        resultBarcodeScan.barcodeTypeStr = barcodeTypeStr;
        resultBarcodeScan.containsCrlf = scannedBarcodeStr.contains("\\n");



        return resultBarcodeScan;
    }

    private static String mCleanBarcodeStr(String pvDirtyBarcodeStr) {
        return  pvDirtyBarcodeStr.replaceAll("([\\r\\n\\t])","");
    }




}
