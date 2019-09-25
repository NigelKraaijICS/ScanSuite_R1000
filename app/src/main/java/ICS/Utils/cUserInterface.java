package ICS.Utils;

import android.content.Context;
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

import java.util.ArrayList;
import java.util.List;

import ICS.Weberror.cWeberror;
import nl.icsvertex.scansuite.cAppExtension;
import SSU_WHS.General.cPublicDefinitions;
import nl.icsvertex.scansuite.R;
import nl.icsvertex.scansuite.fragments.dialogs.GettingDataFragment;
import nl.icsvertex.scansuite.fragments.dialogs.HugeErrorFragment;
import nl.icsvertex.scansuite.fragments.dialogs.PasswordFragment;
import nl.icsvertex.scansuite.fragments.dialogs.WebserviceErrorFragment;

import static SSU_WHS.General.cPublicDefinitions.PASSWORDFRAGMENT_HEADER;
import static SSU_WHS.General.cPublicDefinitions.PASSWORDFRAGMENT_HINT;
import static SSU_WHS.General.cPublicDefinitions.PASSWORDFRAGMENT_ISNUMERIC;
import static SSU_WHS.General.cPublicDefinitions.PASSWORDFRAGMENT_TAG;
import static SSU_WHS.General.cPublicDefinitions.PASSWORDFRAGMENT_TEXT;

public class cUserInterface {
    private static GettingDataFragment gettingDataFragment;

    public static void pShowToastMessage(String pvMessageStr, Integer pvSoundInt) {
        if (pvSoundInt != null && pvSoundInt != 0) {
            pPlaySound(pvSoundInt, null);
        }
        Toast.makeText(cAppExtension.context, pvMessageStr, Toast.LENGTH_SHORT).show();
    }
    public static void pShowSnackbarMessage(View pvView, String pvMessageStr, Integer pvSoundInt, Boolean pvVibrateBln) {
        if (pvSoundInt != null && pvSoundInt != 0) {
            pPlaySound(pvSoundInt, null);
        }
        if (pvVibrateBln) {
            pDoVibrate();
        }
        Snackbar.make(pvView, pvMessageStr, Snackbar.LENGTH_LONG).show();
    }
    public static void pShowActionSnackbar(View pvView, String pvMessageStr, Integer pvSoundInt, Boolean pvVibrateBln) {
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

    public static void pDoNope(View pvView, Boolean pvPlaySoundBln, Boolean pvVibrateBln) {
        Animation shake = AnimationUtils.loadAnimation(cAppExtension.context, R.anim.shake);
        pvView.startAnimation(shake);
        if (pvPlaySoundBln) {
            pPlaySound( R.raw.badsound, null);
        }
        if (pvVibrateBln) {
            pDoVibrate();
        }
    }

    public static void pDoYep(View pvView, Boolean pvPlaySoundBln, Boolean pvVibrateBln) {
        Animation nod = AnimationUtils.loadAnimation(cAppExtension.context, R.anim.nod);
        pvView.startAnimation(nod);
        if (pvPlaySoundBln) {
            pPlaySound(R.raw.goodsound, null);
        }
        if (pvVibrateBln) {
            pDoVibrate();
        }
    }
    public static void pDoWobble(View pvView) {
        Animation wobble = AnimationUtils.loadAnimation(cAppExtension.context, R.anim.wobble);
        pvView.startAnimation(wobble);
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
    public static void pDoBoing(View pvView) {
        final Animation animation = AnimationUtils.loadAnimation(cAppExtension.context, R.anim.bounce);
        // Use bounce interpolator with amplitude 0.2 and frequency 20
        ICS.Utils.cBounceInterpolator interpolator = new ICS.Utils.cBounceInterpolator(0.2,20);
        animation.setInterpolator(interpolator);
        pvView.startAnimation(animation);
    }

    public static void pDoExplodingScreen(String pvErrorMesssageStr, String pvMessage2Str, Boolean pvPlaysoundBln, Boolean pvVibrateBln) {
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
        pCheckAndCloseOpenDialogs();
    }
    public static void pDoWebserviceError(List<cWeberror> pvWeberrorsObl, Boolean pvPlaySoundBln, Boolean pvVibrateBln) {
        if (pvPlaySoundBln) {
            pPlaySound(R.raw.badsound, null);
        }
        if (pvVibrateBln) {
            pDoVibrate();
        }
        ArrayList messages = new ArrayList();

        for (cWeberror weberror: pvWeberrorsObl) {
            messages.add(weberror.getWebmethodStr());
        }
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(cPublicDefinitions.WEBSERVICEERROR_LIST_TAG, messages);

        WebserviceErrorFragment webserviceErrorFragment = new WebserviceErrorFragment();
        webserviceErrorFragment.setArguments(bundle);
        webserviceErrorFragment.setCancelable(true);
        webserviceErrorFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.WEBSERVICEERROR_TAG);
    }
    public static void pCheckAndCloseOpenDialogs() {
        //todo: this is dumb
        pHideGettingData();
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



    public static void pShowGettingData() {
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
        if (gettingDataFragment != null) {
            try {
                gettingDataFragment.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public static void pShowpasswordDialog(String pvHeaderStr, String pvTextStr, Boolean pvIsNumericBln) {

        PasswordFragment passwordFragment = new PasswordFragment();
        Bundle bundle = new Bundle();

        bundle.putString(PASSWORDFRAGMENT_HEADER, pvHeaderStr);
        bundle.putString(PASSWORDFRAGMENT_TEXT, pvTextStr);
        bundle.putString(PASSWORDFRAGMENT_HINT, cAppExtension.context.getString(R.string.hint_password));
        bundle.putBoolean(PASSWORDFRAGMENT_ISNUMERIC, pvIsNumericBln);

        passwordFragment.setArguments(bundle);
        passwordFragment.show(cAppExtension.fragmentManager, PASSWORDFRAGMENT_TAG);
    }


}
