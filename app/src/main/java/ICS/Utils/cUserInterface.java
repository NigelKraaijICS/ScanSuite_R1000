package ICS.Utils;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.os.Vibrator;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ICS.Weberror.cWeberrorEntity;
import SSU_WHS.General.cAppExtension;
import SSU_WHS.General.cPublicDefinitions;
import nl.icsvertex.scansuite.fragments.dialogs.GettingDataFragment;
import nl.icsvertex.scansuite.fragments.dialogs.HugeErrorFragment;
import nl.icsvertex.scansuite.R;
import nl.icsvertex.scansuite.fragments.dialogs.WebserviceErrorFragment;

public class cUserInterface {
    public static GettingDataFragment gettingDataFragment;

    public static void showToastMessage(String pv_messageStr, Integer pv_soundInt) {
        if (pv_soundInt != null && pv_soundInt != 0) {
            playSound(pv_soundInt, null);
        }
        Toast.makeText(cAppExtension.context, pv_messageStr, Toast.LENGTH_SHORT).show();
    }
    public static void showSnackbarMessage(View pv_viewVew, String pv_messageStr, Integer pv_soundInt, Boolean pv_VibrateBln) {
        if (pv_soundInt != null && pv_soundInt != 0) {
            playSound(pv_soundInt, null);
        }
        if (pv_VibrateBln) {
            vibrate();
        }
        Snackbar.make(pv_viewVew, pv_messageStr, Snackbar.LENGTH_LONG).show();
    }
    public static void showActionSnackbar(View pv_viewVew, String pv_messageStr, Integer pv_soundInt, Boolean pv_VibrateBln) {
        if (pv_soundInt != null && pv_soundInt != 0) {
            playSound(pv_soundInt, null);
        }
        if (pv_VibrateBln) {
            vibrate();
        }
        Snackbar snackbar =  Snackbar.make(pv_viewVew, pv_messageStr, Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        sbView.setBackgroundColor(ContextCompat.getColor(cAppExtension.context, R.color.colorError));
        snackbar.show();
    }

    public static void playSound(final Integer pv_soundInt, final Integer pv_startAtMsInt) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (pv_soundInt != null && pv_soundInt != 0) {
                    final MediaPlayer mp = MediaPlayer.create(cAppExtension.context, pv_soundInt);
                    if (mp != null) {
                        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                mp.reset();
                                mp.release();
                            }
                        });
                        if (pv_startAtMsInt != null && pv_startAtMsInt != 0) {
                            mp.seekTo(pv_startAtMsInt);
                        }
                        mp.start();
                    }
                }
            }//run
            }).start(); //thread
    }

    public static void vibrate() {
        // Get instance of Vibrator from current Context
        Vibrator v = (Vibrator) cAppExtension.context.getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 300 milliseconds
        if (v!= null) {
            if (v.hasVibrator()) {
                v.vibrate(300);
                //star wars
                //v.vibrate(new long[]{0, 500, 110, 500, 110, 450, 110, 200, 110, 170, 40, 450, 110, 200, 110, 170, 40, 500}, -1);
            }
        }
    }

    public static void doNope(View view, Boolean playsound, Boolean vibrate) {
        Animation shake = AnimationUtils.loadAnimation(cAppExtension.context, R.anim.shake);
        view.startAnimation(shake);
        if (playsound) {
            playSound( R.raw.badsound, null);
        }
        if (vibrate) {
            vibrate();
        }
    }

    public static void doYep(View view, Boolean playsound, Boolean vibrate) {
        Animation nod = AnimationUtils.loadAnimation(cAppExtension.context, R.anim.nod);
        view.startAnimation(nod);
        if (playsound) {
            playSound(R.raw.goodsound, null);
        }
        if (vibrate) {
            vibrate();
        }
    }
    public static void doWobble(View view) {
        Animation wobble = AnimationUtils.loadAnimation(cAppExtension.context, R.anim.wobble);
        view.startAnimation(wobble);
    }
    public static void doRotate(View view, int repeatcount) {
        Animation rotate = AnimationUtils.loadAnimation(cAppExtension.context, R.anim.rotate);
        if (repeatcount == -1) {
            rotate.setRepeatCount(Animation.INFINITE);
        }
        else {
            rotate.setRepeatCount(repeatcount);
        }
        view.startAnimation(rotate);
    }
    public static void doBoing(View view) {
        final Animation animation = AnimationUtils.loadAnimation(cAppExtension.context, R.anim.bounce);
        // Use bounce interpolator with amplitude 0.2 and frequency 20
        ICS.Utils.cBounceInterpolator interpolator = new ICS.Utils.cBounceInterpolator(0.2,20);
        animation.setInterpolator(interpolator);
        view.startAnimation(animation);
    }

    public static void doExplodingScreen(String errormesssage, String message2, Boolean playsound, Boolean vibrate) {
        if (playsound) {
            playSound(R.raw.badsound, null);
        }
        if (vibrate) {
            vibrate();
        }
        final HugeErrorFragment hugeErrorFragment = new HugeErrorFragment();
        Bundle bundle = new Bundle();
        bundle.putString(cPublicDefinitions.HUGEERROR_ERRORMESSAGE, errormesssage);
        bundle.putString(cPublicDefinitions.HUGEERROR_EXTRASTRING, message2);
        hugeErrorFragment.setArguments(bundle);
        hugeErrorFragment.setCancelable(true);
        hugeErrorFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.HUGEERROR_TAG);
        checkAndCloseOpenDialogs();
    }
    public static void doWebserviceError(List<cWeberrorEntity> weberrorEntities, Boolean playsound, Boolean vibrate) {
        if (playsound) {
            playSound(R.raw.badsound, null);
        }
        if (vibrate) {
            vibrate();
        }
        ArrayList messages = new ArrayList();
        for (cWeberrorEntity weberrorEntity: weberrorEntities) {
            messages.add(weberrorEntity.getWebmethod());
            //messages.add(weberrorEntity.getResult());
        }
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(cPublicDefinitions.WEBSERVICEERROR_LIST_TAG, messages);

        WebserviceErrorFragment webserviceErrorFragment = new WebserviceErrorFragment();
        webserviceErrorFragment.setArguments(bundle);
        webserviceErrorFragment.setCancelable(true);
        webserviceErrorFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.WEBSERVICEERROR_TAG);
    }
    public static void checkAndCloseOpenDialogs() {
        //todo: this is dumb
        mHideGettingData();
        //todo: should be in here
        List<Fragment> fragments = cAppExtension.fragmentManager.getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                if (fragment instanceof DialogFragment) {
                    ((DialogFragment) fragment).dismiss();
                }
            }
        }
    }
    public static void mShowKeyboard(View pvView) {
        InputMethodManager imm2 = (InputMethodManager) cAppExtension.context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm2 != null) {
            imm2.showSoftInput(pvView, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    public static void mShowGettingData() {
        gettingDataFragment = new GettingDataFragment();
        gettingDataFragment.setCancelable(true);
            cAppExtension.activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    gettingDataFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.GETTING_DATA_TAG);
                }
            });

        }

        public static void mHideGettingData() {
            if (gettingDataFragment != null) {
                try {
                    gettingDataFragment.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
}
