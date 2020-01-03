package nl.icsvertex.scansuite.Fragments.Dialogs;


import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;

import java.util.Objects;

import ICS.Interfaces.iICSDefaultFragment;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.General.cPublicDefinitions;
import ICS.Utils.cImages;
import nl.icsvertex.scansuite.R;
import nl.icsvertex.scansuite.Activities.General.MainDefaultActivity;

public class NoConnectionFragment extends DialogFragment implements iICSDefaultFragment {

    private static ImageView imageViewSatellite;
    private static ImageView imageViewEarth;
    private static Button openSettingsButton;
    private static Button tryAgainButton;
    private static Boolean toggleGreyBln;

   public NoConnectionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_no_connection, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        NoConnectionFragment.toggleGreyBln = true;

        this.mFragmentInitialize();
        this.mSetAnimations();
    }

    @Override
    public void mFragmentInitialize() {
        this.mFindViews();
        this.mFieldsInitialize();
        this.mSetListeners();
        cUserInterface.pEnableScanner();
    }

    @Override
    public void mFindViews() {

       if (getView() != null) {
           NoConnectionFragment.imageViewSatellite = getView().findViewById(R.id.imageViewSatellite);
           NoConnectionFragment.imageViewEarth = getView().findViewById(R.id.imageViewEarth);
           NoConnectionFragment.openSettingsButton = getView().findViewById(R.id.openSettingsButton);
           NoConnectionFragment.tryAgainButton = getView().findViewById(R.id.tryAgainButton);
       }

    }


    @Override
    public void mFieldsInitialize() {

    }

    @Override
    public void mSetListeners() {
        this.mEarthListener();
        this.mTryAgainListener();
        this. mOpenSettingsListener();
    }

    private void mSetAnimations() {
        RotateAnimation anim = new RotateAnimation(0f, 1080f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setInterpolator(new LinearInterpolator());
        anim.setRepeatCount(Animation.INFINITE);
        anim.setDuration(5000);

        TranslateAnimation anim2 = new TranslateAnimation(-180f,1400f,0f,0f);
        anim2.setInterpolator(new LinearInterpolator());
        anim2.setRepeatCount(Animation.INFINITE);
        anim2.setDuration(5000);

        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(anim);
        animationSet.addAnimation(anim2);

        NoConnectionFragment.imageViewSatellite.startAnimation(animationSet);
    }
    private void mTryAgainListener() {
        NoConnectionFragment.tryAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cAppExtension.activity instanceof MainDefaultActivity) {
                    ((MainDefaultActivity)cAppExtension.activity).pLetsGetThisPartyStartedOrNot();
                    dismiss();
                }
            }
        });
    }
    private void mOpenSettingsListener() {
        NoConnectionFragment.openSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(Settings.ACTION_WIFI_SETTINGS);
                if (intent.resolveActivity(Objects.requireNonNull(getView()).getContext().getPackageManager()) != null) {
                    startActivityForResult(intent, cPublicDefinitions.CHANGEWIFI_REQUESTCODE);
                }
                dismiss();
            }
        });
    }

    private void mEarthListener() {
        NoConnectionFragment.imageViewEarth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!NoConnectionFragment.toggleGreyBln) {
                    NoConnectionFragment.imageViewSatellite.setImageDrawable(getResources().getDrawable(R.drawable.ic_satellite));
                    NoConnectionFragment.imageViewEarth.setImageDrawable(getResources().getDrawable(R.drawable.ic_earth));
                    NoConnectionFragment.toggleGreyBln = true;
                }
                else {
                    NoConnectionFragment.imageViewSatellite.setImageDrawable(cImages.convertToGrayscale(imageViewSatellite.getDrawable()));
                    NoConnectionFragment.imageViewEarth.setImageDrawable(cImages.convertToGrayscale(imageViewEarth.getDrawable()));
                    NoConnectionFragment.toggleGreyBln = false;
                }
            }
        });
    }
}
