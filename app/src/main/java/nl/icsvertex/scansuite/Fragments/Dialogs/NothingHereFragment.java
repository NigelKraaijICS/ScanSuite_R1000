package nl.icsvertex.scansuite.Fragments.Dialogs;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ICS.Utils.cImages;
import ICS.Utils.cUserInterface;
import nl.icsvertex.scansuite.R;

public class NothingHereFragment extends Fragment {

    //Region Public Properties

    //End Region Public Properties

    //Region private Properties
    private static ImageView imageTumbleweed;
    private static ImageView imageViewWind;
    private static ImageView imageViewCactus;
    private static Boolean blnToggleGrey;
    private static View viewDesert;
    //End Region Private Properties

    //Region Constructor
    public NothingHereFragment() {
        // Required empty public constructor
    }

    //End Region Constructor

    //Region Default Methods
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_nothing_here, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        NothingHereFragment.imageTumbleweed = view.findViewById(R.id.imageViewTumbleweed);
        NothingHereFragment.imageTumbleweed.setImageDrawable(cImages.convertToGrayscale(imageTumbleweed.getDrawable()));
        NothingHereFragment.imageViewWind = view.findViewById(R.id.imageViewWind);
        NothingHereFragment.imageViewWind.setImageDrawable(cImages.convertToGrayscale(imageViewWind.getDrawable()));
        NothingHereFragment.imageViewCactus = view.findViewById(R.id.imageViewCactus);
        NothingHereFragment.imageViewCactus.setImageDrawable(cImages.convertToGrayscale(imageViewCactus.getDrawable()));
        NothingHereFragment.viewDesert = view.findViewById(R.id.viewDesert);
        NothingHereFragment.blnToggleGrey = false;

        NothingHereFragment.imageViewCactus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!NothingHereFragment.blnToggleGrey) {
                    NothingHereFragment.imageTumbleweed.setImageDrawable(getResources().getDrawable(R.drawable.ic_tumbleweed));
                    NothingHereFragment.imageViewCactus.setImageDrawable(getResources().getDrawable(R.drawable.ic_cactus));
                    NothingHereFragment.imageViewWind.setImageDrawable(getResources().getDrawable(R.drawable.ic_wind));
                    NothingHereFragment.viewDesert.setBackgroundResource(R.color.colorDesert);
                    NothingHereFragment.blnToggleGrey = true;
                }
                else {
                    NothingHereFragment.imageTumbleweed.setImageDrawable(cImages.convertToGrayscale(imageTumbleweed.getDrawable()));
                    NothingHereFragment.imageViewCactus.setImageDrawable(cImages.convertToGrayscale(imageViewCactus.getDrawable()));
                    NothingHereFragment.imageViewWind.setImageDrawable(cImages.convertToGrayscale(imageViewWind.getDrawable()));
                    NothingHereFragment.viewDesert.setBackgroundColor(getResources().getColor(R.color.colorDesertGrey));
                    NothingHereFragment.blnToggleGrey = false;
                }
            }
        });

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
        NothingHereFragment.imageTumbleweed.startAnimation(animationSet);
        NothingHereFragment.imageViewWind.startAnimation(anim2);

        cUserInterface.pEnableScanner();
    }

    //End Region Default Methods




}
