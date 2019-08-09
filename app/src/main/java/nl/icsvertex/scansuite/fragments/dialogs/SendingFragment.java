package nl.icsvertex.scansuite.fragments.dialogs;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import ICS.Interfaces.iICSDefaultFragment;
import ICS.Utils.cUserInterface;
import SSU_WHS.cAppExtension;
import nl.icsvertex.scansuite.R;
import nl.icsvertex.scansuite.activities.ship.ShipDetermineTransportActivity;

public class SendingFragment extends android.support.v4.app.DialogFragment implements iICSDefaultFragment {

    ImageView imageRocket;
    ImageView imageCloud;
    TextView textSending;
    TextView textDots;
    TextView textTryAgain;
    ImageView imageViewTryAgain;
    DialogInterface dialogInterface;

    public static SendingFragment newInstance() {
        return new SendingFragment();
    }

    public SendingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dialogInterface = getDialog();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sending, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mFragmentInitialize();
        mSetAnimations();
    }

    @Override
    public void mFragmentInitialize() {
        mFindViews();
        mSetViewModels();
        mFieldsInitialize();
        mSetListeners();
    }
    @Override
    public void mFindViews() {
        imageRocket = getView().findViewById(R.id.imageRocket);
        imageCloud = getView().findViewById(R.id.imageCloud);
        textSending = getView().findViewById(R.id.textSending);
        textDots = getView().findViewById(R.id.textDots);
        textTryAgain = getView().findViewById(R.id.textTryAgain);
        imageViewTryAgain = getView().findViewById(R.id.imageViewTryAgain);
    }

    @Override
    public void mSetViewModels() {

    }
    @Override
    public void mFieldsInitialize() {
        textSending.setText(R.string.dialog_sending);
        textDots.setVisibility(View.VISIBLE);
        mHideTryAgain();
    }
    @Override
    public void mSetListeners() {
        mSetTryAgainListener();
    }

    private void mSetTryAgainListener() {
        imageViewTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (cAppExtension.activity instanceof ShipDetermineTransportActivity) {
                    try {
                        ((ShipDetermineTransportActivity) getActivity()).trySendAgain();
                    } catch (NullPointerException e) {

                    }
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
        imageCloud.startAnimation(animationSet);

        cUserInterface.doWobble(imageRocket);

        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {

            int count = 0;

            @Override
            public void run() {
                count++;
                if (count == 1)
                {
                    textDots.setText(".");
                }
                else if (count == 2)
                {
                    textDots.setText("..");
                }
                else if (count == 3)
                {
                    textDots.setText("...");

                }
                if (count == 3) {
                    count = 0;
                }
                handler.postDelayed(this, 2 * 600);
            }
        };
        handler.postDelayed(runnable, 1 * 600);
    }
    public void flyAway() {
        //new thread, so run in UI
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textSending.setText(R.string.dialog_sent);
                textDots.setVisibility(View.INVISIBLE);
            }
        });

        TranslateAnimation anim1 = new TranslateAnimation(0f,1400f,0f,-1400f);
        anim1.setInterpolator(new LinearInterpolator());
        anim1.setRepeatCount(0);
        anim1.setDuration(1000);

        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(anim1);
        imageRocket.startAnimation(animationSet);
        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                if (cAppExtension.activity instanceof ShipDetermineTransportActivity) {
                    ((ShipDetermineTransportActivity)getActivity()).goLines();
                }
                dismiss();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
    public void notSendCrash() {
        mShowTryAgain();
        //new thread, so run in UI
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textSending.setText(R.string.dialog_notsent);
                textDots.setVisibility(View.INVISIBLE);
            }
        });
        cUserInterface.playSound( R.raw.badsound, null);

        TranslateAnimation anim1 = new TranslateAnimation(0f,1400f,0f,1400f);
        anim1.setInterpolator(new LinearInterpolator());
        anim1.setRepeatCount(0);
        anim1.setDuration(2000);

        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(anim1);
        imageRocket.startAnimation(animationSet);
        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                imageRocket.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
    private void mHideTryAgain() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textTryAgain.setVisibility(View.INVISIBLE);
                imageViewTryAgain.setVisibility(View.INVISIBLE);
            }
        });
    }
    private void mShowTryAgain() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textTryAgain.setVisibility(View.VISIBLE);
                imageViewTryAgain.setVisibility(View.VISIBLE);
            }
        });
    }
}
