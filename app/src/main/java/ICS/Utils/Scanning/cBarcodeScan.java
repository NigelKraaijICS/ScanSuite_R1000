package ICS.Utils.Scanning;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import nl.icsvertex.scansuite.activities.pick.PickorderLinesActivity;
import nl.icsvertex.scansuite.activities.pick.PickorderPickActivity;
import nl.icsvertex.scansuite.cAppExtension;
import nl.icsvertex.scansuite.activities.general.LoginActivity;
import nl.icsvertex.scansuite.activities.pick.PickorderSelectActivity;
import nl.icsvertex.scansuite.fragments.dialogs.ArticleFullViewFragment;
import nl.icsvertex.scansuite.fragments.dialogs.BranchFragment;
import nl.icsvertex.scansuite.fragments.dialogs.CurrentLocationFragment;
import nl.icsvertex.scansuite.fragments.dialogs.EnvironmentFragment;
import nl.icsvertex.scansuite.fragments.dialogs.OrderDoneFragment;
import nl.icsvertex.scansuite.fragments.dialogs.WorkplaceFragment;

public class cBarcodeScan {

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
                    String barcodeStr = ICS.Utils.Scanning.cBarcodeScan.pGetBarcode(intent, true);
                    if (barcodeStr == null) {
                        barcodeStr = "";
                    }

                    // Check current Activity
                    if (cAppExtension.activity instanceof LoginActivity) {
                        LoginActivity.pHandleScan(barcodeStr);

                    }

                    if (cAppExtension.activity instanceof PickorderSelectActivity){
                        PickorderSelectActivity.pHandleScan(barcodeStr);
                    }

                    if (cAppExtension.activity instanceof PickorderLinesActivity) {
                        PickorderLinesActivity.pHandleScan(barcodeStr, false);
                    }

                    if (cAppExtension.activity instanceof PickorderPickActivity) {
                        PickorderPickActivity.pHandleScan(barcodeStr);
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
                    String barcodeStr = ICS.Utils.Scanning.cBarcodeScan.pGetBarcode(intent, true);
                    if (barcodeStr == null) {
                        barcodeStr = "";
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

    public static String pGetBarcode(Intent pvIntent, Boolean pvCleanBln) {
        Bundle extras = pvIntent.getExtras();
        String l_returnBarcode = "";
        String l_barcodeStr = "";
        String l_barcodeTypeStr = "";
        if (extras != null) {
            //so who is sending us this?
            if (pvIntent.getAction().equalsIgnoreCase(cBarcodeScanDefinitions.BARCODEINTENT_DATALOGIC_ACTION)) {
                l_barcodeStr = extras.getString(cBarcodeScanDefinitions.BARCODEINTENT_DATALOGIC_EXTRABARCODE);
                l_barcodeTypeStr = extras.getString(cBarcodeScanDefinitions.BARCODEINTENT_DATALOGIC_EXTRABARCODETYPE);
            }
            if (pvIntent.getAction().equalsIgnoreCase(cBarcodeScanDefinitions.BARCODEINTENT_ZEBRA_ACTION)) {
                l_barcodeStr = extras.getString(cBarcodeScanDefinitions.BARCODEINTENT_ZEBRA_EXTRABARCODE);
                l_barcodeTypeStr = extras.getString(cBarcodeScanDefinitions.BARCODEINTENT_ZEBRA_EXTRABARCODETYPE);
            }
            if (pvIntent.getAction().equalsIgnoreCase(cBarcodeScanDefinitions.BARCODEINTENT_TC55_ACTION)) {
                l_barcodeStr = extras.getString(cBarcodeScanDefinitions.BARCODEINTENT_TC55_EXTRABARCODE);
                l_barcodeTypeStr = extras.getString(cBarcodeScanDefinitions.BARCODEINTENT_TC55_EXTRABARCODETYPE);
            }
        }
        if (pvCleanBln) {
            l_returnBarcode =  mCleanBarcodeStr(l_barcodeStr) ;
        }
        if (!pvCleanBln) {
            l_returnBarcode = l_barcodeStr;
        }
        return l_returnBarcode;
    }

    private static String mCleanBarcodeStr(String pvDirtyBarcodeStr) {
        String l_cleanBarcodeStr = pvDirtyBarcodeStr.replaceAll("(\\r|\\n|\\t)","");
        return l_cleanBarcodeStr;
    }

}
