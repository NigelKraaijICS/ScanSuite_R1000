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
    private static IntentFilter getPowerConnectIntentFilter() {
        if (PowerConnectIntentFilter == null) {
            PowerConnectIntentFilter = new IntentFilter();
            PowerConnectIntentFilter.addAction(Intent.ACTION_POWER_CONNECTED);
            PowerConnectIntentFilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        }
        return PowerConnectIntentFilter;
    }

    private static BroadcastReceiver PowerConnectReceiver;
    private static BroadcastReceiver getPowerConnectReceiver() {
        if (PowerConnectReceiver == null) {
            PowerConnectReceiver = new BroadcastReceiver(){
                @Override
                public void onReceive(Context context, Intent intent) {
                    SupportDeviceFragment.pPowerChanged();
                }
            };
        }
        return PowerConnectReceiver;
    }

    public static void pRegisterPowerConnectReceiver(){

        //Turn off other receiver
        mUnregisterConnectPowerReceiver();

        //Initialise this receiver
        getPowerConnectReceiver();
        getPowerConnectIntentFilter();

        //Attach receiver to context
        cAppExtension.context.registerReceiver(PowerConnectReceiver, PowerConnectIntentFilter);
    }



    private static BroadcastReceiver PowerLevelChangedReceiver;
    private static BroadcastReceiver getPowerLevelChangedReceiver() {
        if (PowerLevelChangedReceiver == null) {
            PowerLevelChangedReceiver = new BroadcastReceiver(){
                @Override
                public void onReceive(Context context, Intent intent) {
                    String action = intent.getAction();

                    if(Objects.equals(action, Intent.ACTION_BATTERY_CHANGED)) {
                        try {
                            SupportDeviceFragment.pBatteryLevelChanged();
                        } catch (Exception ignored) {

                        }
                    }


                }
            };
        }
        return PowerLevelChangedReceiver;
    }
    public static void pRegisterPowerLevelChangedReceiver(){

        //Turn off other receiver
        mUnregisterPowerLevelChangedReceiver();

        //Initialise this receiver
        getPowerLevelChangedReceiver();
        getPowerLevelChangedIntentFilter();

        //Attach receiver to context
        cAppExtension.context.registerReceiver(PowerLevelChangedReceiver, PowerLevelChangedIntentFilter);
    }



    private static void mUnregisterConnectPowerReceiver(){

        try {
            cAppExtension.context.unregisterReceiver(PowerConnectReceiver);
        } catch(IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    private static void mUnregisterPowerLevelChangedReceiver(){

        try {
            cAppExtension.context.unregisterReceiver(PowerLevelChangedReceiver);
        } catch(IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    private static IntentFilter PowerLevelChangedIntentFilter;
    private static IntentFilter getPowerLevelChangedIntentFilter() {
        if (PowerLevelChangedIntentFilter == null) {
            PowerLevelChangedIntentFilter = new IntentFilter();
            PowerLevelChangedIntentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        }
        return PowerLevelChangedIntentFilter;
    }
}
