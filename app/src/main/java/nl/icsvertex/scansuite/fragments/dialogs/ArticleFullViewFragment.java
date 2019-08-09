package nl.icsvertex.scansuite.fragments.dialogs;


import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import ICS.Interfaces.iICSDefaultFragment;
import SSU_WHS.Basics.ArticleImages.cArticleImageEntity;
import SSU_WHS.Basics.ArticleImages.cArticleImageViewModel;
import SSU_WHS.Picken.PickorderLines.cPickorderLineEntity;
import SSU_WHS.Picken.PickorderLines.cPickorderLineViewModel;
import SSU_WHS.General.cPublicDefinitions;
import nl.icsvertex.scansuite.R;

public class ArticleFullViewFragment extends DialogFragment implements iICSDefaultFragment {
    ImageView toolbarImage;
    TextView toolbarTitle;
    TextView toolbarSubtext;
    ImageView toolbarImageHelp;

    ConstraintLayout articleFuillViewContainer;
    TextView articleFullItemNoTextView;
    TextView articleFullVariantTextView;
    ImageView articleFullImageView;

    cPickorderLineViewModel pickorderLineViewModel;
    cPickorderLineEntity pickorderLineEntity2;
    cArticleImageViewModel articleImageViewModel;

    Integer currentRecordid;

    public ArticleFullViewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_article_full_view, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        currentRecordid = args.getInt(cPublicDefinitions.ARTICLEFULL_RECORDID, 0);

        mFragmentInitialize();

        mSetToolbar();

    }
    @Override
    public void onResume() {
        super.onResume();
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels - getResources().getDimensionPixelSize(R.dimen.default_double_margin);

        getDialog().getWindow().setLayout(width, height);
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
        toolbarImage = getView().findViewById(R.id.toolbarImage);
        toolbarTitle = getView().findViewById(R.id.toolbarTitle);
        toolbarSubtext = getView().findViewById(R.id.toolbarSubtext);
        toolbarImageHelp = getView().findViewById(R.id.toolbarImageHelp);

        articleFuillViewContainer = getView().findViewById(R.id.articleFuillViewContainer);
        articleFullItemNoTextView = getView().findViewById(R.id.articleFullItemNoTextView);
        articleFullVariantTextView = getView().findViewById(R.id.articleFullVariantTextView);
        articleFullImageView = getView().findViewById(R.id.articleFullImageView);
    }

    @Override
    public void mSetViewModels() {
        pickorderLineViewModel = ViewModelProviders.of(this).get(cPickorderLineViewModel.class);
        articleImageViewModel = ViewModelProviders.of(this).get(cArticleImageViewModel.class);
    }

    @Override
    public void mFieldsInitialize() {
        pickorderLineEntity2 = pickorderLineViewModel.getPickLineByRecordid(currentRecordid);

        toolbarTitle.setText(pickorderLineEntity2.getDescription());
        toolbarTitle.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        toolbarTitle.setSingleLine(true);
        toolbarTitle.setMarqueeRepeatLimit(5);
        toolbarTitle.postDelayed(new Runnable() {
            @Override
            public void run() {
                toolbarTitle.setSelected(true);
            }
        },1500);

        if (!pickorderLineEntity2.getDescription2().isEmpty()) {
            toolbarSubtext.setText(pickorderLineEntity2.getDescription2());
            toolbarSubtext.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            toolbarSubtext.setSingleLine(true);
            toolbarSubtext.setMarqueeRepeatLimit(5);
            toolbarSubtext.postDelayed(new Runnable() {
                @Override
                public void run() {
                    toolbarSubtext.setSelected(true);
                }
            },1500);
        }

        //articleFullBrandTextView.setText(pickorderLineEntity2.getBrand());
        articleFullItemNoTextView.setText(pickorderLineEntity2.getItemno());
        articleFullVariantTextView.setText(pickorderLineEntity2.getVariantcode());
        articleFullVariantTextView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        articleFullVariantTextView.setSingleLine(true);
        articleFullVariantTextView.setMarqueeRepeatLimit(5);
        articleFullVariantTextView.setSelected(true);
        cArticleImageEntity articleImageEntity = articleImageViewModel.getArticleImageByItemnoAndVariantCode(pickorderLineEntity2.getItemno(), pickorderLineEntity2.getVariantcode());
        if (articleImageEntity != null) {
            byte[] decodedString = Base64.decode(articleImageEntity.getImage(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            articleFullImageView.setImageBitmap(decodedByte);
        }
        else {
            articleFullImageView.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_no_image_lightgrey_24dp));
        }
    }

    @Override
    public void mSetListeners() {
        mDismissListener();
    }

    private void mSetToolbar() {
        toolbarImage.setImageResource(R.drawable.ic_info);
        toolbarImageHelp.setVisibility(View.GONE);
    }

    private void mDismissListener() {
        getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }


}
