package nl.icsvertex.scansuite.fragments.dialogs;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
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

import ICS.Interfaces.iICSDefaultFragment;
import SSU_WHS.General.cPublicDefinitions;
import ICS.Utils.cImages;
import nl.icsvertex.scansuite.R;
import nl.icsvertex.scansuite.activities.general.MainDefaultActivity;

public class NoConnectionFragment extends DialogFragment implements iICSDefaultFragment {
    View thisView;
    Activity callerActivity;
    Fragment thisFragment;
    Context thisContext;

    ImageView imageViewSatellite;
    ImageView imageViewEarth;
    Button openSettingsButton;
    Button tryAgainButton;
    Boolean blnToggleGrey;

    public static NoConnectionFragment newInstance() {
        NoConnectionFragment fragment = new NoConnectionFragment();
        return fragment;
    }

    public NoConnectionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        thisContext = this.getContext();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_no_connection, container, false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        thisFragment = this;
        blnToggleGrey = true;

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
        imageViewSatellite = thisView.findViewById(R.id.imageViewSatellite);
        imageViewEarth = thisView.findViewById(R.id.imageViewEarth);
        openSettingsButton = thisView.findViewById(R.id.openSettingsButton);
        tryAgainButton = thisView.findViewById(R.id.tryAgainButton);
    }

    @Override
    public void mSetViewModels() {

    }

    @Override
    public void mFieldsInitialize() {

    }
    @Override
    public void mSetListeners() {
       mEarthListener();
       mTryAgainListener();
       mOpenSettingsListener();
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

        imageViewSatellite.startAnimation(animationSet);
    }
    private void mTryAgainListener() {
        tryAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callerActivity instanceof MainDefaultActivity) {
                    ((MainDefaultActivity)callerActivity).mLetsGetThisPartyStartedOrNot();
                    dismiss();
                }
            }
        });
    }
    private void mOpenSettingsListener() {
        openSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(Settings.ACTION_WIFI_SETTINGS);
                if (intent.resolveActivity(getView().getContext().getPackageManager()) != null) {
                    startActivityForResult(intent, cPublicDefinitions.CHANGEWIFI_REQUESTCODE);
                }
                dismiss();
            }
        });
    }

    private void mEarthListener() {
        imageViewEarth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!blnToggleGrey) {
                    imageViewSatellite.setImageDrawable(getResources().getDrawable(R.drawable.ic_satellite));
                    imageViewEarth.setImageDrawable(getResources().getDrawable(R.drawable.ic_earth));
                    blnToggleGrey = true;
                }
                else {
                    imageViewSatellite.setImageDrawable(cImages.convertToGrayscale(imageViewSatellite.getDrawable()));
                    imageViewEarth.setImageDrawable(cImages.convertToGrayscale(imageViewEarth.getDrawable()));
                    blnToggleGrey = false;
                }
            }
        });
    }
}
