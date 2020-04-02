package nl.icsvertex.scansuite.Fragments.Dialogs;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Objects;

import ICS.Interfaces.iICSDefaultFragment;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import nl.icsvertex.scansuite.Activities.Packaging.PackagingActivity;
import nl.icsvertex.scansuite.Activities.Ship.ShiporderShipActivity;
import nl.icsvertex.scansuite.R;

public class SendingFragment extends DialogFragment implements iICSDefaultFragment {

    //Region Public Properties

    //End Region Public Properties

    //Region Private Properties

    private ImageView imageRocket;
    private ImageView imageCloud;
    private TextView textSending;
    private TextView textError;
    private TextView textDots;
    private TextView textTryAgain;
    private ImageView imageViewTryAgain;

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
            this.imageRocket = getView().findViewById(R.id.imageRocket);
            this.imageCloud = getView().findViewById(R.id.imageCloud);
            this.textSending = getView().findViewById(R.id.textSending);
            this.textError = getView().findViewById(R.id.textErrorDetail);
            this.textDots = getView().findViewById(R.id.textDots);
            this.textTryAgain = getView().findViewById(R.id.textTryAgain);
            this.imageViewTryAgain = getView().findViewById(R.id.imageViewTryAgain);
        }

    }


    @Override
    public void mFieldsInitialize() {
        this.textSending.setText(R.string.dialog_sending);
        this.textDots.setVisibility(View.VISIBLE);
        this.textError.setVisibility(View.INVISIBLE);
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
                textSending.setText(R.string.dialog_sent);
                textDots.setVisibility(View.INVISIBLE);
                textError.setVisibility(View.INVISIBLE);
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

                    if (cAppExtension.activity instanceof ShiporderShipActivity) {
                        ShiporderShipActivity shiporderShipActivity = (ShiporderShipActivity)cAppExtension.activity;
                        shiporderShipActivity.pHandleBackToLines();
                    }

                    if (cAppExtension.activity instanceof PackagingActivity) {
                        PackagingActivity packagingActivity = (PackagingActivity)cAppExtension.activity;
                        packagingActivity.pDone();
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
               textSending.setText(R.string.dialog_notsent);
               textDots.setVisibility(View.INVISIBLE);
               textError.setVisibility(View.VISIBLE);
              textError.setText(pvErrorMessageStr);
            }
        });

        cUserInterface.pPlaySound( R.raw.badsound, null);

        TranslateAnimation anim1 = new TranslateAnimation(0f,1400f,0f,1400f);
        anim1.setInterpolator(new LinearInterpolator());
        anim1.setRepeatCount(0);
        anim1.setDuration(2000);

        AnimationSet animationSet = new AnimationSet(true);
            animationSet.addAnimation(anim1);
        this.imageRocket.startAnimation(animationSet);
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


    //End Region Public Methods

    //Region Private Methods

    private void mSetTryAgainListener() {
        this.imageViewTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();

                if (cAppExtension.activity instanceof ShiporderShipActivity) {
                    ShiporderShipActivity shiporderShipActivity = (ShiporderShipActivity)cAppExtension.activity;
                    shiporderShipActivity.pHandleSourceDocumentDone();
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
        this.imageCloud.startAnimation(animationSet);

        cUserInterface.pDoWobble(this.imageRocket);

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
        handler.postDelayed(runnable, 600);
    }

    private void mShowHideTryAgain(Boolean pvShowBln) {

        if (pvShowBln) {
            Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    textTryAgain.setVisibility(View.VISIBLE);
                    imageViewTryAgain.setVisibility(View.VISIBLE);
                }
            });
        }

        Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textTryAgain.setVisibility(View.INVISIBLE);
               imageViewTryAgain.setVisibility(View.INVISIBLE);
            }
        });

    }

    //End Region Private Methods
}




