package ICS.Utils;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;

import ICS.cAppExtension;
import nl.icsvertex.scansuite.Activities.Pick.PickorderLinesActivity;
import nl.icsvertex.scansuite.Activities.Pick.PickorderLinesGeneratedActivity;
import nl.icsvertex.scansuite.Fragments.Dialogs.CurrentLocationFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.StepDoneFragment;
import nl.icsvertex.scansuite.R;

public class cConnection {

    public enum connectionType {
        NONE,
        WIFI,
        ETHERNET,
        MOBILEDATA,
        UNKNOWN
    }

    public static Boolean isGooglePingableBln() {
        try {
            Process p1 = java.lang.Runtime.getRuntime().exec("ping -c 1 8.8.8.8");
            int returnVal = p1.waitFor();
            return (returnVal==0);
        } catch (Exception e) {
            return false;
        }
    }

    public static Boolean isInternetConnectedBln() {
        boolean resultBln = false;
        ConnectivityManager cm = (ConnectivityManager) cAppExtension.context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            resultBln = activeNetwork != null && activeNetwork.isConnected();
        }
        return resultBln;
    }

    public static connectionType getCurrentConnectionType() {
        ConnectivityManager cm = (ConnectivityManager) cAppExtension.context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            boolean isConnectedBln;
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            //are we connected at all?
            isConnectedBln = activeNetwork != null && activeNetwork.isConnected();
            if (!isConnectedBln) {
                return connectionType.NONE;
            }
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                return connectionType.WIFI;
            }
            if (activeNetwork.getType() == ConnectivityManager.TYPE_ETHERNET) {
                return connectionType.ETHERNET;
            }
            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE || activeNetwork.getType() == ConnectivityManager.TYPE_WIMAX) {
                return connectionType.MOBILEDATA;
            }
            //none of the above
            return connectionType.UNKNOWN;
        }
        return connectionType.UNKNOWN;
    }

    private static AlertDialog tryAgainDialog;

    public static void pReconnectWifi() {
        WifiManager wifiManager = (WifiManager)cAppExtension.context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifiManager != null) {
            wifiManager.setWifiEnabled(true);
            wifiManager.reconnect();
        }
    }

    private static IntentFilter wifiChangedIntentFilter;
    private static void getWifiChangedIntentFilter() {

        if (cConnection.wifiChangedIntentFilter == null) {
            cConnection.wifiChangedIntentFilter = new IntentFilter();

            cConnection.wifiChangedIntentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);

        }
    }

    private static IntentFilter wifiChangedFragmentIntentFilter;
    private static void getWifiChangedFragmentIntentFilter() {

        if (cConnection.wifiChangedFragmentIntentFilter == null) {
            cConnection.wifiChangedFragmentIntentFilter = new IntentFilter();

            cConnection.wifiChangedFragmentIntentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);

        }
    }

    private static BroadcastReceiver wifiChangedReceiver;
    private static void getWifiChangedReceiver() {
        if (cConnection.wifiChangedReceiver == null) {
            cConnection.wifiChangedReceiver = new BroadcastReceiver(){
                @Override
                public void onReceive(Context context, Intent intent) {

                    Bundle extras = intent.getExtras();
                    if (extras != null) {
                        cConnection.isInternetConnectedBln();
                    }


                }
            };
        }
    }

    private static BroadcastReceiver wifiChangedFragmentReceiver;
    private static void getWifiChangedFragmentReceiver() {
        if (cConnection.wifiChangedFragmentReceiver == null) {
            cConnection.wifiChangedFragmentReceiver = new BroadcastReceiver(){
                @Override
                public void onReceive(Context context, Intent intent) {

                    Bundle extras = intent.getExtras();
                    if (extras != null) {
                        cConnection.isInternetConnectedBln();
                    }


                }
            };
        }
    }

    public static void pRegisterWifiChangedReceiver(){

        //Turn off other receiver
        cConnection.pUnregisterWifiChangedFragmentReceiver();

        //Initialise this receiver
        cConnection.getWifiChangedIntentFilter();
        cConnection.getWifiChangedReceiver();

        //Attach receiver to context
        cAppExtension.context.registerReceiver(cConnection.wifiChangedReceiver,cConnection.wifiChangedIntentFilter);
    }

    public static void pRegisterWifiChangedFragmentReceiver(){

        //Turn off other receiver
        cConnection.pUnregisterWifiChangedReceiver();

        //Initialise this receiver
        cConnection.getWifiChangedFragmentIntentFilter();
        cConnection.getWifiChangedFragmentReceiver();

        //Attach receiver to context
        cAppExtension.context.registerReceiver(cConnection.wifiChangedFragmentReceiver,cConnection.wifiChangedFragmentIntentFilter);
    }

    public static void pUnregisterWifiChangedReceiver(){

        try {
            cAppExtension.context.unregisterReceiver(cConnection.wifiChangedReceiver);
        } catch(IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public static void pUnregisterWifiChangedFragmentReceiver(){

        try {
            cAppExtension.context.unregisterReceiver(cConnection.wifiChangedFragmentReceiver);
        } catch(IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    private static void mShowConnectionWaiter() {
        if (cConnection.tryAgainDialog != null) {
            cConnection.tryAgainDialog.dismiss();
        }

        final AlertDialog.Builder dialog = new AlertDialog.Builder(cAppExtension.context).setTitle(cAppExtension.activity.getString(R.string.message_checking_connection)).setMessage(cAppExtension.activity.getString(R.string.message_please_wait));
        final AlertDialog alert = dialog.create();
        alert.show();

        // Hide after some seconds
        final Handler handler  = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (alert.isShowing()) {
                    alert.dismiss();
                }
            }
        };

        alert.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                handler.removeCallbacks(runnable);

                if (cAppExtension.activity instanceof PickorderLinesActivity) {

                    if (cAppExtension.dialogFragment instanceof StepDoneFragment) {
                        StepDoneFragment stepDoneFragment = (StepDoneFragment)cAppExtension.dialogFragment;
                        stepDoneFragment.pSetConnectionState();
                        return;
                    }

                    if (cAppExtension.dialogFragment instanceof CurrentLocationFragment) {
                        CurrentLocationFragment currentLocationFragment = (CurrentLocationFragment)cAppExtension.dialogFragment;
                        currentLocationFragment.pSetConnectionState();
                        return;
                    }

                    if (cAppExtension.activity instanceof PickorderLinesActivity) {
                        PickorderLinesActivity pickorderLinesActivity = (PickorderLinesActivity)cAppExtension.activity;
                        PickorderLinesActivity.startedViaOrderSelectBln = false;
                        pickorderLinesActivity.pCheckAllDone();
                    }

                    if (cAppExtension.activity instanceof PickorderLinesGeneratedActivity) {
                        PickorderLinesGeneratedActivity pickorderLinesGeneratedActivity = (PickorderLinesGeneratedActivity)cAppExtension.activity;
                        pickorderLinesGeneratedActivity.pCloseOrder();
                    }


                }

            }
        });

        handler.postDelayed(runnable, 8000);
    }

    public static void pShowTryAgainDialog() {

        cConnection.pReconnectWifi();
        AlertDialog.Builder tryAgainBuilder = new AlertDialog.Builder(cAppExtension.context);

        tryAgainBuilder.setMessage(cAppExtension.activity.getString((R.string.message_no_connection_try_again)));
        tryAgainBuilder.setTitle(cAppExtension.activity.getString(R.string.message_no_connection_header));
        tryAgainBuilder.setPositiveButton(R.string.button_try_again, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                cConnection.mShowConnectionWaiter();
            }
        });
        cConnection.tryAgainDialog = tryAgainBuilder.create();
        cConnection.tryAgainDialog.show();
    }


}
