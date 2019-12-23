package nl.icsvertex.scansuite.Fragments.Dialogs;



import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.core.content.ContextCompat;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import ICS.Interfaces.iICSDefaultFragment;
import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cRegex;
import ICS.Utils.cUserInterface;
import SSU_WHS.Basics.BarcodeLayouts.cBarcodeLayout;
import SSU_WHS.Picken.PickorderLines.cPickorderLine;
import nl.icsvertex.scansuite.R;
import nl.icsvertex.scansuite.Activities.Pick.PickorderPickActivity;
import ICS.cAppExtension;

public class ArticleFullViewFragment extends DialogFragment implements iICSDefaultFragment {

    //Region Public Properties

    //End Region Public Properties

    //Region Private Properties
    private ImageView toolbarImage;
    private  TextView toolbarTitle;
    private TextView toolbarSubtext;

    private static ConstraintLayout articleFullViewContainer;
    private TextView articleFullItemNoTextView;
    private TextView articleFullVariantTextView;
    private ImageView articleFullImageView;
    //End Region Private Properties


    //Region Constructor
    public ArticleFullViewFragment() {
        // Required empty public constructor
    }
    //End Region Constructor

    //Region Default Methods

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_article_full_view, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        cAppExtension.dialogFragment = this;
        this.mFragmentInitialize();

    }

    @Override
    public void onPause() {
        try {
            cBarcodeScan.pUnregisterBarcodeFragmentReceiver();

            if (cAppExtension.activity instanceof  PickorderPickActivity) {
                PickorderPickActivity.pRegisterBarcodeReceiver();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        super.onPause();
    }
    @Override
    public void onDestroy() {
        try {
            cBarcodeScan.pUnregisterBarcodeFragmentReceiver();

            if (cAppExtension.activity instanceof  PickorderPickActivity) {
                PickorderPickActivity.pRegisterBarcodeReceiver();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels - getResources().getDimensionPixelSize(R.dimen.default_double_margin);

        getDialog().getWindow().setLayout(width, height);
        cBarcodeScan.pRegisterBarcodeFragmentReceiver();
        cUserInterface.pEnableScanner();
    }

    @Override
    public void mFragmentInitialize() {
        this.mFindViews();
        this.mFieldsInitialize();
        this.mSetListeners();
        this.mSetToolbar();

        cBarcodeScan.pRegisterBarcodeFragmentReceiver();

    }

    @Override
    public void mFindViews() {

        this.toolbarImage = getView().findViewById(R.id.toolbarImage);
        this.toolbarTitle = getView().findViewById(R.id.toolbarTitle);
        this.toolbarSubtext = getView().findViewById(R.id.toolbarSubtext);

        this.articleFullViewContainer = getView().findViewById(R.id.articleFuillViewContainer);
        this.articleFullItemNoTextView = getView().findViewById(R.id.articleFullItemNoTextView);
        this.articleFullVariantTextView = getView().findViewById(R.id.articleFullVariantTextView);
        this.articleFullImageView = getView().findViewById(R.id.articleFullImageView);
    }

     @Override
    public void mFieldsInitialize() {

        if (!cPickorderLine.currentPickOrderLine.getDescription2Str().isEmpty()) {
            this.toolbarSubtext.setText(cPickorderLine.currentPickOrderLine.getDescription2Str());
            this.toolbarSubtext.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            this.toolbarSubtext.setSingleLine(true);
            this.toolbarSubtext.setMarqueeRepeatLimit(5);
            this.toolbarSubtext.postDelayed(new Runnable() {
                @Override
                public void run() {
                    toolbarSubtext.setSelected(true);
                }
            },1500);
        }

        //articleFullBrandTextView.setText(pickorderLineEntity2.getBrand());
        this.articleFullItemNoTextView.setText(cPickorderLine.currentPickOrderLine.getItemNoStr());
        this.articleFullVariantTextView.setText(cPickorderLine.currentPickOrderLine.getVariantCodeStr());
        this.articleFullVariantTextView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        this.articleFullVariantTextView.setSingleLine(true);
        this.articleFullVariantTextView.setMarqueeRepeatLimit(5);
        this.articleFullVariantTextView.setSelected(true);

        if (cPickorderLine.currentPickOrderLine.articleImage != null) {
            byte[] decodedString = Base64.decode(cPickorderLine.currentPickOrderLine.articleImage.getImageStr(), Base64.DEFAULT);
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

    //End Region Default Methods

    //Region Public Methods

    public static void pHandleScan(cBarcodeScan pvBarcodeScan){

        String barcodeWithoutPrefixStr;

        //No prefix
        if (!cRegex.hasPrefix(pvBarcodeScan.getBarcodeOriginalStr())) {
            ArticleFullViewFragment.mArticleScanned(pvBarcodeScan);
            return;
        }

        Boolean foundBln = false;

        if (cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(),cBarcodeLayout.barcodeLayoutEnu.ARTICLE)) {
            foundBln = true;
        }

        //has prefix, is article
        if (foundBln) {
            barcodeWithoutPrefixStr = cRegex.pStripRegexPrefixStr(pvBarcodeScan.getBarcodeOriginalStr());
            ArticleFullViewFragment.mArticleScanned(pvBarcodeScan);
            return;
        }
        else {
            //has prefix, isn't branch
            cUserInterface.pDoNope(articleFullViewContainer, true, true);
            return;
        }
    }
    //End Region Public Methods

    //Region Private Methods

    private void mSetToolbar() {

        this.toolbarTitle.setText(cPickorderLine.currentPickOrderLine.getDescriptionStr());
        this.toolbarTitle.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        this.toolbarTitle.setSingleLine(true);
        this.toolbarTitle.setMarqueeRepeatLimit(5);
        this.toolbarTitle.postDelayed(new Runnable() {
            @Override
            public void run() {
                toolbarTitle.setSelected(true);
            }
        },1500);

        this.toolbarImage.setImageResource(R.drawable.ic_info);
    }

    private void mDismissListener() {
        getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }


    private static void mArticleScanned(cBarcodeScan pvBarcodeScan) {

        if (cAppExtension.activity instanceof PickorderPickActivity) {
            PickorderPickActivity.pHandleScan(pvBarcodeScan);
            cAppExtension.dialogFragment.dismiss();
        }
    }

    //End Region Private Mrethods




}
