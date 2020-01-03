package nl.icsvertex.scansuite.Fragments.Dialogs;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import java.util.Objects;

import ICS.Interfaces.iICSDefaultFragment;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import nl.icsvertex.scansuite.Activities.Ship.ShiporderShipActivity;
import nl.icsvertex.scansuite.R;

public class SendingFragment extends DialogFragment implements iICSDefaultFragment {

    //Region Public Properties

    //End Region Public Properties

    //Region Private Properties

    private static   ImageView imageRocket;
    private static ImageView imageCloud;
    private static TextView textSending;
    private static TextView textError;
    private static TextView textDots;
    private static  TextView textTryAgain;
    private static  ImageView imageViewTryAgain;

    //End Region Private Properties

    //Region Contsructor
    public SendingFragment() {
        // Required empty public constructor
    }
    //Emd Region Constructor

    //Region Default Methods
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sending, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        this.mFragmentInitialize();
    }

    //End Region Default Methods

    //Region iICSDefaultActivity defaults

    @Override
    public void mFragmentInitialize() {
        this.mFindViews();
        this.mFieldsInitialize();
        this.mSetListeners();
        this.mSetAnimations();
        cUserInterface.pEnableScanner();
    }

    @Override
    public void mFindViews() {

        if (getView() != null) {
            SendingFragment.imageRocket = getView().findViewById(R.id.imageRocket);
            SendingFragment.imageCloud = getView().findViewById(R.id.imageCloud);
            SendingFragment.textSending = getView().findViewById(R.id.textSending);
            SendingFragment.textError = getView().findViewById(R.id.textErrorDetail);
            SendingFragment.textDots = getView().findViewById(R.id.textDots);
            SendingFragment.textTryAgain = getView().findViewById(R.id.textTryAgain);
            SendingFragment.imageViewTryAgain = getView().findViewById(R.id.imageViewTryAgain);
        }

    }


    @Override
    public void mFieldsInitialize() {
        SendingFragment.textSending.setText(R.string.dialog_sending);
        SendingFragment.textDots.setVisibility(View.VISIBLE);
        SendingFragment.textError.setVisibility(View.INVISIBLE);
        this.mShowHideTryAgain(false);
    }

    @Override
    public void mSetListeners() {
        this.mSetTryAgainListener();
    }

    //End Region iICSDefaultActivity defaults

    //Region Public Methods

    public void pShowFlyAwayAnimation() {

        //new thread, so run in UI
        Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                SendingFragment.textSending.setText(R.string.dialog_sent);
                SendingFragment.textDots.setVisibility(View.INVISIBLE);
                SendingFragment.textError.setVisibility(View.INVISIBLE);
            }
        });

        TranslateAnimation anim1 = new TranslateAnimation(0f,1400f,0f,-1400f);
        anim1.setInterpolator(new LinearInterpolator());
        anim1.setRepeatCount(0);
        anim1.setDuration(1000);

        AnimationSet animationSet = new AnimationSet(true);
            animationSet.addAnimation(anim1);
            SendingFragment.imageRocket.startAnimation(animationSet);
            animationSet.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {


                    if (cAppExtension.activity instanceof ShiporderShipActivity) {
                        ShiporderShipActivity.pHandleBackToLines();
                    }
                    dismiss();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }




    public void pShowCrashAnimation(final String pvErrorMessageStr) {

        this.mShowHideTryAgain(true);

        //new thread, so run in UI
        Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                SendingFragment.textSending.setText(R.string.dialog_notsent);
                SendingFragment.textDots.setVisibility(View.INVISIBLE);
                SendingFragment.textError.setVisibility(View.VISIBLE);
                SendingFragment.textError.setText(pvErrorMessageStr);
            }
        });

        cUserInterface.pPlaySound( R.raw.badsound, null);

        TranslateAnimation anim1 = new TranslateAnimation(0f,1400f,0f,1400f);
        anim1.setInterpolator(new LinearInterpolator());
        anim1.setRepeatCount(0);
        anim1.setDuration(2000);

        AnimationSet animationSet = new AnimationSet(true);
            animationSet.addAnimation(anim1);
            SendingFragment.imageRocket.startAnimation(animationSet);
            animationSet.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    SendingFragment.imageRocket.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }


    //End Region Public Methods

    //Region Private Methods

    private void mSetTryAgainListener() {
        SendingFragment.imageViewTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();

                if (cAppExtension.activity instanceof ShiporderShipActivity) {
                        ShiporderShipActivity.pHandleSourceDocumentDone();
                }
            }
        });
    }

    private void mSetAnimations() {

        TranslateAnimation anim1 = new TranslateAnimation(300f,-1400f,-300f,1400f);
        anim1.setInterpolator(new LinearInterpolator());
        anim1.setRepeatCount(Animation.INFINITE);
        anim1.setDuration(2000);

        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(anim1);
        SendingFragment.imageCloud.startAnimation(animationSet);

        cUserInterface.pDoWobble(SendingFragment.imageRocket);

        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {

            int count = 0;

            @Override
            public void run() {
                count++;
                if (count == 1)
                {
                    SendingFragment.textDots.setText(".");
                }
                else if (count == 2)
                {
                    SendingFragment.textDots.setText("..");
                }
                else if (count == 3)
                {
                    SendingFragment.textDots.setText("...");

                }
                if (count == 3) {
                    count = 0;
                }
                handler.postDelayed(this, 2 * 600);
            }
        };
        handler.postDelayed(runnable, 600);
    }

    private void mShowHideTryAgain(Boolean pvShowBln) {

        if (pvShowBln) {
            Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    SendingFragment.textTryAgain.setVisibility(View.VISIBLE);
                    SendingFragment.imageViewTryAgain.setVisibility(View.VISIBLE);
                }
            });
        }

        Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                SendingFragment.textTryAgain.setVisibility(View.INVISIBLE);
                SendingFragment.imageViewTryAgain.setVisibility(View.INVISIBLE);
            }
        });

    }

    //End Region Private Methods
}




