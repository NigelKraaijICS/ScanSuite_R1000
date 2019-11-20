package ICS.Utils.Scanning;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import nl.icsvertex.scansuite.Activities.General.MenuActivity;
import nl.icsvertex.scansuite.Activities.Intake.IntakeOrderIntakeActivity;
import nl.icsvertex.scansuite.Activities.Intake.IntakeorderLinesActivity;
import nl.icsvertex.scansuite.Activities.Intake.IntakeorderSelectActivity;
import nl.icsvertex.scansuite.Activities.Inventory.InventoryorderBinActivity;
import nl.icsvertex.scansuite.Activities.Inventory.InventoryorderBinsActivity;
import nl.icsvertex.scansuite.Activities.Inventory.InventoryorderSelectActivity;
import nl.icsvertex.scansuite.Activities.Pick.PickorderLinesActivity;
import nl.icsvertex.scansuite.Activities.Pick.PickorderPickActivity;
import nl.icsvertex.scansuite.Activities.Ship.ShiporderLinesActivity;
import nl.icsvertex.scansuite.Activities.Ship.ShiporderSelectActivity;
import nl.icsvertex.scansuite.Activities.Ship.ShiporderShipActivity;
import nl.icsvertex.scansuite.Activities.Sort.SortorderLinesActivity;
import nl.icsvertex.scansuite.Activities.Sort.SortorderSelectActivity;
import nl.icsvertex.scansuite.Activities.Sort.SortorderSortActivity;
import ICS.cAppExtension;
import nl.icsvertex.scansuite.Activities.General.LoginActivity;
import nl.icsvertex.scansuite.Activities.Pick.PickorderSelectActivity;
import nl.icsvertex.scansuite.Fragments.Dialogs.AddArticleFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.AddBinFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.AddEnvironmentFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.ArticleFullViewFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.BranchFragment;
import nl.icsvertex.scansuite.Fragments.Inventory.CreateInventoryFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.CurrentLocationFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.EnvironmentFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.OrderDoneFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.WorkplaceFragment;
import nl.icsvertex.scansuite.Fragments.Inventory.InventoryArticleDetailFragment;

public class cBarcodeScan {

    public class BarcodeType {

        public static final int Unknown = 0;
        public static final int EAN8 = 1;
        public static final int EAN13 = 2;

        public static final int UPCA = 3;
        public static final int UPCE = 4;

        public static final int INTERLEAVED2OF5 = 5;

        public static final int CODE128 = 10;
        public static final int CODE39 = 11;


    }

    public String barcodeStr;
    public String getBarcodeStr() {
        return barcodeStr;
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
                public void onReceive(Context context, Intent intent) {

                    cBarcodeScan barcodeScan = ICS.Utils.Scanning.cBarcodeScan.pGetBarcode(intent, true);
                    String barcodeStr;


                    if (barcodeScan == null) {
                        barcodeStr = "";
                    }  {
                        barcodeStr = barcodeScan.getBarcodeStr();
                    }

                    //Login
                    if (cAppExtension.activity instanceof LoginActivity) {
                        LoginActivity.pHandleScan(barcodeStr);

                    }

                    //Login
                    if (cAppExtension.activity instanceof MenuActivity) {
                        MenuActivity.pHandleScan(barcodeScan);

                    }

                    //Pick
                    if (cAppExtension.activity instanceof PickorderSelectActivity){
                        PickorderSelectActivity.pHandleScan(barcodeStr);
                    }

                    if (cAppExtension.activity instanceof PickorderLinesActivity) {
                        PickorderLinesActivity.pHandleScan(barcodeStr, false);
                    }

                    if (cAppExtension.activity instanceof PickorderPickActivity) {
                        PickorderPickActivity.pHandleScan(barcodeStr);
                    }

                    //Sort
                    if (cAppExtension.activity instanceof SortorderSelectActivity){
                        SortorderSelectActivity.pHandleScan(barcodeStr);
                    }

                    if (cAppExtension.activity instanceof SortorderLinesActivity){
                        SortorderLinesActivity.pHandleScan(barcodeStr, false);
                    }

                    if (cAppExtension.activity instanceof SortorderSortActivity){
                        SortorderSortActivity.pHandleScan(barcodeStr);
                    }

                    //Ship
                    if (cAppExtension.activity instanceof ShiporderSelectActivity){
                        ShiporderSelectActivity.pHandleScan(barcodeStr);
                    }

                    if (cAppExtension.activity instanceof ShiporderLinesActivity){
                        ShiporderLinesActivity.pHandleScan(barcodeStr, false);
                    }

                    if (cAppExtension.activity instanceof ShiporderShipActivity){
                        ShiporderShipActivity.pHandleScan(barcodeStr);
                    }

                    //Inventory
                    if (cAppExtension.activity instanceof InventoryorderSelectActivity){
                        InventoryorderSelectActivity.pHandleScan(barcodeStr);
                    }

                    if (cAppExtension.activity instanceof InventoryorderBinsActivity){
                        InventoryorderBinsActivity.pHandleScan(barcodeStr);
                    }

                    if (cAppExtension.activity instanceof InventoryorderBinActivity){
                        InventoryorderBinActivity.pHandleScan(barcodeScan);
                    }

                    //Intake
                    if (cAppExtension.activity instanceof IntakeorderSelectActivity){
                        IntakeorderSelectActivity.pHandleScan(barcodeStr);
                    }

                    if (cAppExtension.activity instanceof IntakeorderLinesActivity){
                        IntakeorderLinesActivity.pHandleScan(barcodeScan,false);
                    }

                    if (cAppExtension.activity instanceof IntakeOrderIntakeActivity){
                        IntakeOrderIntakeActivity.pHandleScan(barcodeScan);
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

                    cBarcodeScan barcodeScan = ICS.Utils.Scanning.cBarcodeScan.pGetBarcode(intent, true);
                    String barcodeStr = "";

                    if (barcodeScan != null) {
                        barcodeStr = barcodeScan.getBarcodeStr();
                    }

                    if (cAppExtension.dialogFragment instanceof BranchFragment) {
                        BranchFragment.pHandleScan(barcodeStr);
                        return;
                    }

                    if(cAppExtension.dialogFragment instanceof OrderDoneFragment) {
                        OrderDoneFragment.pHandleScan(barcodeStr);
                        return;
                    }

                    if(cAppExtension.dialogFragment instanceof WorkplaceFragment) {
                        WorkplaceFragment.pHandleScan(barcodeStr);
                        return;
                    }

                    if(cAppExtension.dialogFragment instanceof CurrentLocationFragment) {
                        CurrentLocationFragment.pHandleScan(barcodeStr);
                        return;
                    }

                    if(cAppExtension.dialogFragment instanceof ArticleFullViewFragment) {
                        ArticleFullViewFragment.pHandleScan(barcodeStr);
                        return;
                    }

                    if (cAppExtension.dialogFragment instanceof EnvironmentFragment) {
                        EnvironmentFragment.pHandleScan(barcodeStr);
                    }

                    if (cAppExtension.dialogFragment instanceof AddEnvironmentFragment) {
                        AddEnvironmentFragment.pHandleScan(barcodeStr);
                    }

                    if (cAppExtension.dialogFragment instanceof CreateInventoryFragment) {
                        CreateInventoryFragment.pHandleScan(barcodeStr);
                    }

                    if (cAppExtension.dialogFragment instanceof AddBinFragment) {
                        AddBinFragment.pHandleScan(barcodeStr);
                    }

                    if (cAppExtension.dialogFragment instanceof AddArticleFragment) {
                        AddArticleFragment.pHandleScan(barcodeStr);
                    }

                    if (cAppExtension.dialogFragment instanceof InventoryArticleDetailFragment) {
                        InventoryArticleDetailFragment.pHandleScan(barcodeScan);
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

    public static cBarcodeScan pGetBarcode(Intent pvIntent, Boolean pvCleanBln) {

        cBarcodeScan resultBarcodeScan;

        Bundle extras = pvIntent.getExtras();
        String returnBarcodeStr = "";
        String scannedBarcodeStr = "";
        String barcodeTypeStr = "";


        if (extras != null) {
            //so who is sending us this?
            if (pvIntent.getAction().equalsIgnoreCase(cBarcodeScanDefinitions.BARCODEINTENT_DATALOGIC_ACTION)) {
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
        }

        if (pvCleanBln) {
            scannedBarcodeStr = mCleanBarcodeStr(scannedBarcodeStr);
            returnBarcodeStr =  mCleanBarcodeStr(scannedBarcodeStr) ;
        }
        if (!pvCleanBln) {
            returnBarcodeStr = scannedBarcodeStr;
        }

        //If it's an EAN barcode cut off checkdigit
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
        resultBarcodeScan.barcodeStr = returnBarcodeStr;
        resultBarcodeScan.barcodeTypeStr = barcodeTypeStr;

        return resultBarcodeScan;
    }

    private static String mCleanBarcodeStr(String pvDirtyBarcodeStr) {
        return  pvDirtyBarcodeStr.replaceAll("(\\r|\\n|\\t)","");
    }

}
