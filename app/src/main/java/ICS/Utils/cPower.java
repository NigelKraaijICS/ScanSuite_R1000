package ICS.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import java.util.Objects;

import ICS.cAppExtension;
import nl.icsvertex.scansuite.Fragments.Support.SupportDeviceFragment;

public class cPower {

    private static IntentFilter PowerConnectIntentFilter;
    private static void getPowerConnectIntentFilter() {
        if (PowerConnectIntentFilter == null) {
            PowerConnectIntentFilter = new IntentFilter();
            PowerConnectIntentFilter.addAction(Intent.ACTION_POWER_CONNECTED);
            PowerConnectIntentFilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        }
    }

    private static BroadcastReceiver PowerConnectReceiver;
    private static void getPowerConnectReceiver() {
        if (PowerConnectReceiver == null) {
            PowerConnectReceiver = new BroadcastReceiver(){
                @Override
                public void onReceive(Context context, Intent intent) {
                    try {
                    SupportDeviceFragment supportDeviceFragment = (SupportDeviceFragment)cAppExtension.dialogFragment;
                    supportDeviceFragment.pPowerChanged();
                    } catch (Exception ignored) {

                    }
                }
            };
        }
    }

    public static void pRegisterPowerConnectReceiver(){

        //Turn off other receiver
        pUnregisterConnectPowerReceiver();

        //Initialise this receiver
        getPowerConnectReceiver();
        getPowerConnectIntentFilter();

        //Attach receiver to context
        cAppExtension.context.registerReceiver(PowerConnectReceiver, PowerConnectIntentFilter);
    }



    private static BroadcastReceiver PowerLevelChangedReceiver;
    private static void getPowerLevelChangedReceiver() {
        if (PowerLevelChangedReceiver == null) {
            PowerLevelChangedReceiver = new BroadcastReceiver(){
                @Override
                public void onReceive(Context context, Intent intent) {
                    String action = intent.getAction();

                    if(Objects.equals(action, Intent.ACTION_BATTERY_CHANGED)) {
                        try {
                            SupportDeviceFragment supportDeviceFragment = (SupportDeviceFragment)cAppExtension.dialogFragment;
                            supportDeviceFragment.pBatteryLevelChanged();
                        } catch (Exception ignored) {

                        }
                    }


                }
            };
        }
    }
    public static void pRegisterPowerLevelChangedReceiver(){

        //Turn off other receiver
        pUnregisterPowerLevelChangedReceiver();

        //Initialise this receiver
        getPowerLevelChangedReceiver();
        getPowerLevelChangedIntentFilter();

        //Attach receiver to context
        cAppExtension.context.registerReceiver(PowerLevelChangedReceiver, PowerLevelChangedIntentFilter);
    }



    public static void pUnregisterConnectPowerReceiver(){

        try {
            cAppExtension.context.unregisterReceiver(PowerConnectReceiver);
        } catch(IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public static void pUnregisterPowerLevelChangedReceiver(){

        try {
            cAppExtension.context.unregisterReceiver(PowerLevelChangedReceiver);
        } catch(IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    private static IntentFilter PowerLevelChangedIntentFilter;
    private static void getPowerLevelChangedIntentFilter() {
        if (PowerLevelChangedIntentFilter == null) {
            PowerLevelChangedIntentFilter = new IntentFilter();
            PowerLevelChangedIntentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        }
    }
}
