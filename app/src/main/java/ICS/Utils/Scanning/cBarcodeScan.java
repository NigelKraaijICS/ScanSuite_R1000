package ICS.Utils.Scanning;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import java.util.Objects;

import ICS.Utils.cText;
import ICS.cAppExtension;
import nl.icsvertex.scansuite.Activities.General.LoginActivity;
import nl.icsvertex.scansuite.Activities.Intake.IntakeOrderIntakeActivity;
import nl.icsvertex.scansuite.Activities.Intake.IntakeorderLinesActivity;
import nl.icsvertex.scansuite.Activities.Move.CreateMoveActivity;
import nl.icsvertex.scansuite.Activities.Move.MoveLinePlaceActivity;
import nl.icsvertex.scansuite.Activities.Move.MoveLineTakeActivity;
import nl.icsvertex.scansuite.Activities.Move.MoveLineTakeMTActivity;
import nl.icsvertex.scansuite.Activities.Move.MoveLinesActivity;
import nl.icsvertex.scansuite.Activities.Move.MoveLinesTakeMTActivity;
import nl.icsvertex.scansuite.Activities.Move.MoveorderSelectActivity;
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
import nl.icsvertex.scansuite.Fragments.Dialogs.AddArticleFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.AddBinFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.AddEnvironmentFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.ArticleFullViewFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.BranchFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.CurrentLocationFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.EnvironmentFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.ScanArticleFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.ScanBinFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.StepDoneFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.PasswordFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.ReasonFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.WorkplaceFragment;
import nl.icsvertex.scansuite.Fragments.Inventory.CreateInventoryFragment;
import nl.icsvertex.scansuite.Fragments.Inventory.InventoryArticleDetailFragment;
import nl.icsvertex.scansuite.Activities.Returns.ReturnArticleDetailActivity;

public class cBarcodeScan {

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

    public cBarcodeScan(){

    }

    private static IntentFilter BarcodeIntentFilter;
    private static IntentFilter getBarcodeIntentFilter() {
        if (BarcodeIntentFilter == null) {
            BarcodeIntentFilter = new IntentFilter();
            for (String barcodeActionStr : cBarcodeScanDefinitions.getBarcodeActions()) {
                BarcodeIntentFilter.addAction(barcodeActionStr);
            }
            for (String barcodeCategorieStr : cBarcodeScanDefinitions.getBarcodeCategories()) {
                BarcodeIntentFilter.addCategory(barcodeCategorieStr);
            }
        }
        return BarcodeIntentFilter;
    }

    private static IntentFilter BarcodeFragmentIntentFilter;
    private static IntentFilter getBarcodeFragmentIntentFilter() {
        if (BarcodeFragmentIntentFilter == null) {
            BarcodeFragmentIntentFilter = new IntentFilter();
            for (String barcodeActionStr : cBarcodeScanDefinitions.getBarcodeActions()) {
                BarcodeFragmentIntentFilter.addAction(barcodeActionStr);
            }
            for (String barcodeCategorieStr : cBarcodeScanDefinitions.getBarcodeCategories()) {
                BarcodeFragmentIntentFilter.addCategory(barcodeCategorieStr);
            }
        }
        return BarcodeFragmentIntentFilter;
    }

    private static BroadcastReceiver BarcodeReceiver;
    private static BroadcastReceiver getBarcodeReceiver() {
        if (BarcodeReceiver == null) {
            BarcodeReceiver = new BroadcastReceiver(){
                @Override
                public void onReceive(Context pvContext, Intent pvIntent) {

                    if (pvContext != cAppExtension.context) {
                        return;
                    }

                    //Fill a barcodeStr scan object
                    cBarcodeScan barcodeScan = ICS.Utils.Scanning.cBarcodeScan.mGetBarcode(pvIntent);

                    //Login
                    if (cAppExtension.activity instanceof LoginActivity) {
                        LoginActivity loginActivity = (LoginActivity)cAppExtension.activity;
                        loginActivity.pHandleScan(barcodeScan);
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

                    if (cAppExtension.activity instanceof PickorderPickActivity) {
                        PickorderPickActivity pickorderPickActivity = (PickorderPickActivity)cAppExtension.activity;
                        pickorderPickActivity.pHandleScan(barcodeScan);
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

                    //Intake
                    if (cAppExtension.activity instanceof IntakeAndReceiveSelectActivity){
                        IntakeAndReceiveSelectActivity intakeAndReceiveSelectActivity = (IntakeAndReceiveSelectActivity)cAppExtension.activity;
                        intakeAndReceiveSelectActivity.pHandleScan(barcodeScan);
                    }

                    if (cAppExtension.activity instanceof CreateReceiveActivity){
                        CreateReceiveActivity createReceiveActivity = (CreateReceiveActivity)cAppExtension.activity;
                        createReceiveActivity.pHandleScan(barcodeScan,false,false,false);
                    }

                    if (cAppExtension.activity instanceof ReceiveLinesActivity){
                        ReceiveLinesActivity receiveLinesActivity = (ReceiveLinesActivity)cAppExtension.activity;
                        receiveLinesActivity.pHandleScan(barcodeScan,false);
                    }

                    if (cAppExtension.activity instanceof ReceiveOrderReceiveActivity){
                        ReceiveOrderReceiveActivity receiveOrderReceiveActivity = (ReceiveOrderReceiveActivity)cAppExtension.activity;
                        receiveOrderReceiveActivity.pHandleScan(barcodeScan);
                    }


                    if (cAppExtension.activity instanceof IntakeorderLinesActivity){
                        IntakeorderLinesActivity intakeorderLinesActivity = (IntakeorderLinesActivity)cAppExtension.activity;
                        intakeorderLinesActivity.pHandleScan(barcodeScan,false);
                    }

                    if (cAppExtension.activity instanceof IntakeOrderIntakeActivity){
                        IntakeOrderIntakeActivity intakeOrderIntakeActivity = (IntakeOrderIntakeActivity)cAppExtension.activity;
                        intakeOrderIntakeActivity.pHandleScan(barcodeScan);
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

                    if (cAppExtension.activity instanceof MoveLineTakeActivity){
                        MoveLineTakeActivity moveLineTakeActivity = (MoveLineTakeActivity)cAppExtension.activity;
                        moveLineTakeActivity.pHandleScan(barcodeScan);
                    }

                    if (cAppExtension.activity instanceof MoveLinePlaceActivity){
                        MoveLinePlaceActivity moveLinePlaceActivity = (MoveLinePlaceActivity)cAppExtension.activity;
                        moveLinePlaceActivity.pHandleScan(barcodeScan);
                    }

                }
            };
        }
        return BarcodeReceiver;
    }

    private static BroadcastReceiver BarcodeFragmentReceiver;
    private static BroadcastReceiver getBarcodeFragmentReceiver() {
        if (BarcodeFragmentReceiver == null) {
            BarcodeFragmentReceiver = new BroadcastReceiver(){

                @Override
                public void onReceive(Context context, Intent intent) {

                    if (context != cAppExtension.context) {
                        return;
                    }

                    //Fill a barcodeStr scan object
                    cBarcodeScan barcodeScan = ICS.Utils.Scanning.cBarcodeScan.mGetBarcode(intent);

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

                    if (cAppExtension.dialogFragment instanceof CreateInventoryFragment) {
                        CreateInventoryFragment createInventoryFragment = (CreateInventoryFragment)cAppExtension.dialogFragment;
                        createInventoryFragment.pHandleScan(barcodeScan);
                        return;
                    }

                    if (cAppExtension.dialogFragment instanceof AddBinFragment) {
                        AddBinFragment addBinFragment = (AddBinFragment)cAppExtension.dialogFragment;
                        addBinFragment.pHandleScan(barcodeScan);
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

                    if (cAppExtension.dialogFragment instanceof InventoryArticleDetailFragment) {
                        InventoryArticleDetailFragment inventoryArticleDetailFragment = (InventoryArticleDetailFragment)cAppExtension.dialogFragment;
                        inventoryArticleDetailFragment.pHandleScan(barcodeScan);
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
        return BarcodeFragmentReceiver;
    }

    public static void pRegisterBarcodeReceiver(){

        //Turn off other receiver
        cBarcodeScan.pUnregisterBarcodeFragmentReceiver();

        //Initialise this receiver
        cBarcodeScan.getBarcodeIntentFilter();
        cBarcodeScan.getBarcodeReceiver();

        //Attach receiver to context
        cAppExtension.context.registerReceiver(BarcodeReceiver,BarcodeIntentFilter);
    }

    public static void pRegisterBarcodeFragmentReceiver(){

        //Turn off other receiver
        cBarcodeScan.pUnregisterBarcodeReceiver();

        //Initialise this receiver
        cBarcodeScan.getBarcodeFragmentIntentFilter();
        cBarcodeScan.getBarcodeFragmentReceiver();

        //Attach receiver to context
        cAppExtension.context.registerReceiver(BarcodeFragmentReceiver,BarcodeFragmentIntentFilter);
    }

    public static void pUnregisterBarcodeReceiver(){

        try {
         cAppExtension.context.unregisterReceiver(BarcodeReceiver);
        } catch(IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public static void pUnregisterBarcodeFragmentReceiver(){

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

        return resultBarcodeScan;
    }

    private static String mCleanBarcodeStr(String pvDirtyBarcodeStr) {
        return  pvDirtyBarcodeStr.replaceAll("([\\r\\n\\t])","");
    }

}
