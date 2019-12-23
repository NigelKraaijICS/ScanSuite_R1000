package ICS.Utils;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.security.PublicKey;
import java.util.List;

import ICS.cAppExtension;
import SSU_WHS.General.cPublicDefinitions;
import nl.icsvertex.scansuite.R;
import nl.icsvertex.scansuite.Fragments.Dialogs.GettingDataFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.HugeErrorFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.PasswordFragment;

import static SSU_WHS.General.cPublicDefinitions.PASSWORDFRAGMENT_HEADER;
import static SSU_WHS.General.cPublicDefinitions.PASSWORDFRAGMENT_HINT;
import static SSU_WHS.General.cPublicDefinitions.PASSWORDFRAGMENT_ISNUMERIC;
import static SSU_WHS.General.cPublicDefinitions.PASSWORDFRAGMENT_TAG;
import static SSU_WHS.General.cPublicDefinitions.PASSWORDFRAGMENT_TEXT;

import com.datalogic.device.input.KeyboardManager;
import com.datalogic.device.input.Trigger;

public class cUserInterface {
    private static GettingDataFragment gettingDataFragment;

    public static void pShowToastMessage(final String pvMessageStr, final Integer pvSoundInt) {
        cAppExtension.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (pvSoundInt != null && pvSoundInt != 0) {
                    pPlaySound(pvSoundInt, null);
                }
                Toast.makeText(cAppExtension.context, pvMessageStr, Toast.LENGTH_SHORT).show();
            }
        });

    }

    public static void pShowSnackbarMessage(final View pvView, final String pvMessageStr, final Integer pvSoundInt, final Boolean pvVibrateBln) {
        cAppExtension.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (pvSoundInt != null && pvSoundInt != 0) {
                    pPlaySound(pvSoundInt, null);
                }
                if (pvVibrateBln) {
                    pDoVibrate();
                }
                Snackbar.make(pvView, pvMessageStr, Snackbar.LENGTH_LONG).show();
            }
        });


    }

    public static void pShowActionSnackbar(final View pvView, final String pvMessageStr, final Integer pvSoundInt, final Boolean pvVibrateBln) {
        cAppExtension.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (pvSoundInt != null && pvSoundInt != 0) {
                    pPlaySound(pvSoundInt, null);
                }
                if (pvVibrateBln) {
                    pDoVibrate();
                }
                Snackbar snackbar =  Snackbar.make(pvView, pvMessageStr, Snackbar.LENGTH_LONG);
                View sbView = snackbar.getView();
                sbView.setBackgroundColor(ContextCompat.getColor(cAppExtension.context, R.color.colorError));
                snackbar.show();
            }
        });


    }

    public static void pPlaySound(final Integer pvSoundInt, final Integer pvStartAtMsInt) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (pvSoundInt != null && pvSoundInt != 0) {
                    final MediaPlayer mp = MediaPlayer.create(cAppExtension.context, pvSoundInt);
                    if (mp != null) {
                        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                mp.reset();
                                mp.release();
                            }
                        });
                        if (pvStartAtMsInt != null && pvStartAtMsInt != 0) {
                            mp.seekTo(pvStartAtMsInt);
                        }
                        mp.start();
                    }
                }
            }//run
            }).start(); //thread
    }

    public static void pDoVibrate() {
        // Get instance of Vibrator from current Context
        Vibrator v = (Vibrator) cAppExtension.context.getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 300 milliseconds
        if (v!= null) {
            if (v.hasVibrator()) {
                v.vibrate(300);
            }
        }
    }

    public static void pDoNope(final View pvView, final Boolean pvPlaySoundBln, final Boolean pvVibrateBln) {
        cAppExtension.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Animation shake = AnimationUtils.loadAnimation(cAppExtension.context, R.anim.shake);
                pvView.startAnimation(shake);
                if (pvPlaySoundBln) {
                    pPlaySound( R.raw.badsound, null);
                }
                if (pvVibrateBln) {
                    pDoVibrate();
                }
            }
        });

    }

    public static void pDoYep(final View pvView, final Boolean pvPlaySoundBln, final Boolean pvVibrateBln) {
        cAppExtension.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Animation nod = AnimationUtils.loadAnimation(cAppExtension.context, R.anim.nod);
                pvView.startAnimation(nod);
                if (pvPlaySoundBln) {
                    pPlaySound(R.raw.goodsound, null);
                }
                if (pvVibrateBln) {
                    pDoVibrate();
                }
            }
        });

    }

    public static void pDoWobble(final View pvView) {
        cAppExtension.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Animation wobble = AnimationUtils.loadAnimation(cAppExtension.context, R.anim.wobble);
                pvView.startAnimation(wobble);
            }
        });

    }

    public static void pDoRotate(final View pvView, final int pvRepeatCountInt) {
        cAppExtension.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Animation rotate = AnimationUtils.loadAnimation(cAppExtension.context, R.anim.rotate);
                if (pvRepeatCountInt == -1) {
                    rotate.setRepeatCount(Animation.INFINITE);
                }
                else {
                    rotate.setRepeatCount(pvRepeatCountInt);
                }
                pvView.startAnimation(rotate);
            }
        });
    }

    public static void pDoBoing(final View pvView) {
        cAppExtension.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final Animation animation = AnimationUtils.loadAnimation(cAppExtension.context, R.anim.bounce);
                // Use bounce interpolator with amplitude 0.2 and frequency 20
                ICS.Utils.cBounceInterpolator interpolator = new ICS.Utils.cBounceInterpolator(0.2,20);
                animation.setInterpolator(interpolator);
                pvView.startAnimation(animation);
            }
        });

    }

    public static void pDoExplodingScreen(final String pvErrorMesssageStr, final String pvMessage2Str, final Boolean pvPlaysoundBln, final Boolean pvVibrateBln) {
        cAppExtension.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                pCheckAndCloseOpenDialogs();
                if (pvPlaysoundBln) {
                    pPlaySound(R.raw.badsound, null);
                }
                if (pvVibrateBln) {
                    pDoVibrate();
                }
                final HugeErrorFragment hugeErrorFragment = new HugeErrorFragment();
                Bundle bundle = new Bundle();
                bundle.putString(cPublicDefinitions.HUGEERROR_ERRORMESSAGE, pvErrorMesssageStr);
                bundle.putString(cPublicDefinitions.HUGEERROR_EXTRASTRING, pvMessage2Str);
                hugeErrorFragment.setArguments(bundle);
                hugeErrorFragment.setCancelable(true);
                hugeErrorFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.HUGEERROR_TAG);
            }
        });
    }

    public static void pCheckAndCloseOpenDialogs() {

        pHideGettingData();

        List<Fragment> fragments = cAppExtension.fragmentManager.getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                if (fragment instanceof DialogFragment) {
                    ((DialogFragment) fragment).dismiss();
                }
            }
        }
    }

    public static void pShowKeyboard(final View pvView) {
        pvView.post(new Runnable() {
            @Override
            public void run() {
                pvView.requestFocus();
                InputMethodManager imgr = (InputMethodManager) cAppExtension.activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imgr != null) {
                    imgr.showSoftInput(pvView, InputMethodManager.SHOW_IMPLICIT);
                }
            }
        });
    }

    public static void pHideKeyboard() {
        InputMethodManager imm = (InputMethodManager) cAppExtension.activity.getSystemService(cAppExtension.activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = cAppExtension.activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(cAppExtension.activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void pShowGettingData() {

        //Disable the scanner
        cUserInterface.pDisableScanner();

        gettingDataFragment = new GettingDataFragment();
        gettingDataFragment.setCancelable(true);
            cAppExtension.activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    gettingDataFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.GETTING_DATA_TAG);
                }
            });
        }

    public static void pHideGettingData() {

        //(Re) Enable the scanner
        cUserInterface.pEnableScanner();

        cAppExtension.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (gettingDataFragment != null) {
                    try {
                        gettingDataFragment.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    public static void pShowpasswordDialog(final String pvHeaderStr, final String pvTextStr, final Boolean pvIsNumericBln) {
        cAppExtension.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                PasswordFragment passwordFragment = new PasswordFragment();
                Bundle bundle = new Bundle();

                bundle.putString(PASSWORDFRAGMENT_HEADER, pvHeaderStr);
                bundle.putString(PASSWORDFRAGMENT_TEXT, pvTextStr);
                bundle.putString(PASSWORDFRAGMENT_HINT, cAppExtension.context.getString(R.string.hint_password));
                bundle.putBoolean(PASSWORDFRAGMENT_ISNUMERIC, pvIsNumericBln);

                passwordFragment.setArguments(bundle);
                passwordFragment.show(cAppExtension.fragmentManager, PASSWORDFRAGMENT_TAG);
            }
        });

    }

    public static void pEnableScanner(){

        switch(cDeviceInfo.getDeviceManufacturer().toUpperCase()) {

            case "DATALOGIC":

                KeyboardManager keyManager = new KeyboardManager();
                for (Trigger trigger : keyManager.getAvailableTriggers()) {
                    boolean result;
                    if (trigger.getId() == KeyboardManager.TRIGGER_ID_MOTION || trigger.getId() == KeyboardManager.TRIGGER_ID_AUTOSCAN) {
                        result =  trigger.setEnabled(false);
                    }
                    else {
                        result =  trigger.setEnabled(true);
                    }

                    if(result){
                        //operation done
                    }else{
                        //do something in case of failure
                    }
                }

            case "ZEBRA":

                Intent i = new Intent();
                i.setAction("com.symbol.datawedge.api.ACTION_SCANNERINPUTPLUGIN");
                i.putExtra("com.symbol.datawedge.api.EXTRA_PARAMETER", "ENABLE_PLUGIN");
                cAppExtension.context.sendBroadcast(i);

            default:
        }



    }

    public static void pDisableScanner(){

        switch(cDeviceInfo.getDeviceManufacturer().toUpperCase()) {
            case "DATALOGIC":

                KeyboardManager keyManager = new KeyboardManager();
                for (Trigger trigger : keyManager.getAvailableTriggers()) {
                    boolean result =  trigger.setEnabled(false);

                    if(result){
                        //operation done
                    }else{
                        //do something in case of failure
                    }
                }

                break;

            case "ZEBRA":

                Intent i = new Intent();
                i.setAction("com.symbol.datawedge.api.ACTION_SCANNERINPUTPLUGIN");
                i.putExtra("com.symbol.datawedge.api.EXTRA_PARAMETER", "DISABLE_PLUGIN");
                cAppExtension.context.sendBroadcast(i);

                // code block
                break;

            default:
                // code block
        }

    }

}
