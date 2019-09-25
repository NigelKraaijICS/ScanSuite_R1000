package nl.icsvertex.scansuite.fragments;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import ICS.Utils.cImages;
import nl.icsvertex.scansuite.R;

public class NothingHereFragment extends Fragment {
    Fragment thisFragment;
    Context thisContext;
    ImageView imageTumbleweed;
    ImageView imageViewWind;
    ImageView imageViewCactus;
    Boolean blnToggleGrey;
    View viewDesert;

    public static NothingHereFragment newInstance() {
        return new NothingHereFragment();
    }

    public NothingHereFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_nothing_here, container, false);

    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        thisFragment = this;
        imageTumbleweed = view.findViewById(R.id.imageViewTumbleweed);
        imageTumbleweed.setImageDrawable(cImages.convertToGrayscale(imageTumbleweed.getDrawable()));
        imageViewWind = view.findViewById(R.id.imageViewWind);
        imageViewWind.setImageDrawable(cImages.convertToGrayscale(imageViewWind.getDrawable()));
        imageViewCactus = view.findViewById(R.id.imageViewCactus);
        imageViewCactus.setImageDrawable(cImages.convertToGrayscale(imageViewCactus.getDrawable()));
        viewDesert = view.findViewById(R.id.viewDesert);
        blnToggleGrey = false;

        imageViewCactus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!blnToggleGrey) {
                    imageTumbleweed.setImageDrawable(getResources().getDrawable(R.drawable.ic_tumbleweed));
                    imageViewCactus.setImageDrawable(getResources().getDrawable(R.drawable.ic_cactus));
                    imageViewWind.setImageDrawable(getResources().getDrawable(R.drawable.ic_wind));
                    viewDesert.setBackgroundResource(R.color.colorDesert);
                    blnToggleGrey = true;
                }
                else {
                    imageTumbleweed.setImageDrawable(cImages.convertToGrayscale(imageTumbleweed.getDrawable()));
                    imageViewCactus.setImageDrawable(cImages.convertToGrayscale(imageViewCactus.getDrawable()));
                    imageViewWind.setImageDrawable(cImages.convertToGrayscale(imageViewWind.getDrawable()));
                    viewDesert.setBackgroundColor(getResources().getColor(R.color.colorDesertGrey));
                    blnToggleGrey = false;
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
        imageTumbleweed.startAnimation(animationSet);
        imageViewWind.startAnimation(anim2);
    }

}
