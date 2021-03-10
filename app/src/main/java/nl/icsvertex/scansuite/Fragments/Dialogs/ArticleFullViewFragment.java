package nl.icsvertex.scansuite.Fragments.Dialogs;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import java.util.Objects;

import ICS.Interfaces.iICSDefaultFragment;
import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cICSImageZoomView;
import ICS.Utils.cRegex;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Basics.BarcodeLayouts.cBarcodeLayout;
import SSU_WHS.Intake.IntakeorderMATLineSummary.cIntakeorderMATSummaryLine;
import SSU_WHS.Picken.PickorderLines.cPickorderLine;
import SSU_WHS.Picken.Pickorders.cPickorder;
import SSU_WHS.Return.ReturnorderLine.cReturnorderLine;
import nl.icsvertex.scansuite.Activities.Intake.IntakeOrderIntakeActivity;
import nl.icsvertex.scansuite.Activities.Pick.PickorderPickActivity;
import nl.icsvertex.scansuite.Activities.Pick.PickorderPickGeneratedActivity;
import nl.icsvertex.scansuite.Activities.Returns.ReturnArticleDetailActivity;
import nl.icsvertex.scansuite.R;

public class ArticleFullViewFragment extends DialogFragment implements iICSDefaultFragment {

    //Region Public Properties

    //End Region Public Properties

    //Region Private Properties
    private ImageView toolbarImage;
    private TextView toolbarTitle;
    private TextView toolbarSubtext;

    private ConstraintLayout articleFullViewContainer;
    private TextView articleFullItemNoTextView;
    private TextView articleFullVariantTextView;
    private cICSImageZoomView articleFullImageView;
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
            cBarcodeScan.pUnregisterBarcodeFragmentReceiver(this.getClass().getSimpleName());

            if (cAppExtension.activity instanceof  PickorderPickActivity) {
                PickorderPickActivity pickorderPickActivity = (PickorderPickActivity)cAppExtension.activity;
                pickorderPickActivity.pRegisterBarcodeReceiver();
            }

            if (cAppExtension.activity instanceof PickorderPickGeneratedActivity) {
                PickorderPickGeneratedActivity pickorderPickGeneratedActivity = (PickorderPickGeneratedActivity)cAppExtension.activity;
                pickorderPickGeneratedActivity.pRegisterBarcodeReceiver();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        super.onPause();
    }
    @Override
    public void onDestroy() {
        try {
            cBarcodeScan.pUnregisterBarcodeFragmentReceiver(this.getClass().getSimpleName());

            if (cAppExtension.activity instanceof  PickorderPickActivity) {
                PickorderPickActivity pickorderPickActivity = (PickorderPickActivity)cAppExtension.activity;
                pickorderPickActivity.pRegisterBarcodeReceiver();
            }

            if (cAppExtension.activity instanceof  PickorderPickGeneratedActivity) {
                PickorderPickGeneratedActivity pickorderPickGeneratedActivity = (PickorderPickGeneratedActivity)cAppExtension.activity;
                pickorderPickGeneratedActivity.pRegisterBarcodeReceiver();
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

        Objects.requireNonNull(Objects.requireNonNull(getDialog()).getWindow()).setLayout(width, height);
        cBarcodeScan.pRegisterBarcodeFragmentReceiver(this.getClass().getSimpleName());
        cUserInterface.pEnableScanner();
    }

    @Override
    public void mFragmentInitialize() {
        this.mFindViews();
        this.mFieldsInitialize();
        this.mSetListeners();
        this.mSetToolbar();

        cBarcodeScan.pRegisterBarcodeFragmentReceiver(this.getClass().getSimpleName());

    }

    @Override
    public void mFindViews() {

        if (getView() != null) {
            this.toolbarImage = getView().findViewById(R.id.toolbarImage);
            this.toolbarTitle = getView().findViewById(R.id.toolbarTitle);
            this.toolbarSubtext = getView().findViewById(R.id.toolbarSubtext);

            this.articleFullViewContainer = getView().findViewById(R.id.articleFuillViewContainer);
            this.articleFullItemNoTextView = getView().findViewById(R.id.articleFullItemNoTextView);
            this.articleFullVariantTextView = getView().findViewById(R.id.articleFullVariantTextView);
            this.articleFullImageView = getView().findViewById(R.id.articleFullImageView);
        }

    }

     @Override
    public void mFieldsInitialize() {


        if (cAppExtension.activity instanceof PickorderPickActivity) {
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

            this.articleFullItemNoTextView.setText(cPickorderLine.currentPickOrderLine.getItemNoStr());
            this.articleFullVariantTextView.setText(cPickorderLine.currentPickOrderLine.getVariantCodeStr());
            this.articleFullVariantTextView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            this.articleFullVariantTextView.setSingleLine(true);
            this.articleFullVariantTextView.setMarqueeRepeatLimit(5);
            this.articleFullVariantTextView.setSelected(true);

            if (cPickorderLine.currentPickOrderLine.articleImage != null) {
                byte[] decodedString = Base64.decode(cPickorderLine.currentPickOrderLine.articleImage.getImageStr(), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                this.articleFullImageView.setImageBitmap(decodedByte);
            }
            else {
                this.articleFullImageView.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_no_image_lightgrey_24dp));
            }
        }

         if (cAppExtension.activity instanceof PickorderPickGeneratedActivity) {
             if (!cPickorder.currentPickOrder.currentArticle.getDescription2Str().isEmpty()) {
                 this.toolbarSubtext.setText(cPickorder.currentPickOrder.currentArticle.getDescription2Str());
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

             this.articleFullItemNoTextView.setText(cPickorder.currentPickOrder.currentArticle.getItemNoStr());
             this.articleFullVariantTextView.setText(cPickorder.currentPickOrder.currentArticle.getVariantCodeStr());
             this.articleFullVariantTextView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
             this.articleFullVariantTextView.setSingleLine(true);
             this.articleFullVariantTextView.setMarqueeRepeatLimit(5);
             this.articleFullVariantTextView.setSelected(true);

             if (cPickorder.currentPickOrder.currentArticle.articleImage != null) {
                 byte[] decodedString = Base64.decode(cPickorder.currentPickOrder.currentArticle.articleImage.getImageStr(), Base64.DEFAULT);
                 Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                 this.articleFullImageView.setImageBitmap(decodedByte);
             }
             else {
                 this.articleFullImageView.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_no_image_lightgrey_24dp));
             }
         }

         if (cAppExtension.activity instanceof IntakeOrderIntakeActivity) {
             if (!cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getDescription2Str().isEmpty()) {
                 this.toolbarSubtext.setText(cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getDescription2Str());
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

             this.articleFullItemNoTextView.setText(cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getItemNoStr());
             this.articleFullVariantTextView.setText(cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getVariantCodeStr());
             this.articleFullVariantTextView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
             this.articleFullVariantTextView.setSingleLine(true);
             this.articleFullVariantTextView.setMarqueeRepeatLimit(5);
             this.articleFullVariantTextView.setSelected(true);

             if (cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.articleImage != null) {
                 byte[] decodedString = Base64.decode(cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.articleImage.getImageStr(), Base64.DEFAULT);
                 Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                 this.articleFullImageView.setImageBitmap(decodedByte);
             }
             else {
                 this.articleFullImageView.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_no_image_lightgrey_24dp));
             }
         }

         if (cAppExtension.activity instanceof ReturnArticleDetailActivity) {
             if (!cReturnorderLine.currentReturnOrderLine.getDescription2Str().isEmpty()) {
                 this.toolbarSubtext.setText(cReturnorderLine.currentReturnOrderLine.getDescription2Str());
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

             this.articleFullItemNoTextView.setText(cReturnorderLine.currentReturnOrderLine.getItemNoStr());
             this.articleFullVariantTextView.setText(cReturnorderLine.currentReturnOrderLine.getVariantCodeStr());
             this.articleFullVariantTextView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
             this.articleFullVariantTextView.setSingleLine(true);
             this.articleFullVariantTextView.setMarqueeRepeatLimit(5);
             this.articleFullVariantTextView.setSelected(true);

             if (cReturnorderLine.currentReturnOrderLine.articleImage != null) {
                 byte[] decodedString = Base64.decode(cReturnorderLine.currentReturnOrderLine.articleImage.getImageStr(), Base64.DEFAULT);
                 Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                 this.articleFullImageView.setImageBitmap(decodedByte);
             }
             else {
                 this.articleFullImageView.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_no_image_lightgrey_24dp));
             }
         }

    }

    @Override
    public void mSetListeners() {
        this.mDismissListener();
    }

    //End Region Default Methods

    //Region Public Methods


    public  void pHandleScan(cBarcodeScan pvBarcodeScan){


        //No prefix
        if (!cRegex.pHasPrefix(pvBarcodeScan.getBarcodeOriginalStr())) {
            this.mArticleScanned(pvBarcodeScan);
            return;
        }

        boolean foundBln = false;

        if (cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeScan.getBarcodeOriginalStr(),cBarcodeLayout.barcodeLayoutEnu.ARTICLE)) {
            foundBln = true;
        }

        //has prefix, is article
        if (foundBln) {
            this.mArticleScanned(pvBarcodeScan);
        }
        else {
            //has prefix, isn't branch
            cUserInterface.pDoNope(this.articleFullViewContainer, true, true);
        }
    }
    //End Region Public Methods

    //Region Private Methods

    private void mSetToolbar() {

        if (cAppExtension.activity instanceof PickorderPickActivity) {
            this.toolbarTitle.setText(cPickorderLine.currentPickOrderLine.getDescriptionStr());
        }

        if (cAppExtension.activity instanceof IntakeOrderIntakeActivity) {
            this.toolbarTitle.setText(cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getDescriptionStr());
        }

        if (cAppExtension.activity instanceof  ReturnArticleDetailActivity) {
            this.toolbarTitle.setText(cReturnorderLine.currentReturnOrderLine.getDescriptionStr());
        }

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
         requireView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }


    private void mArticleScanned(cBarcodeScan pvBarcodeScan) {

        if (cAppExtension.activity instanceof PickorderPickActivity) {
            PickorderPickActivity pickorderPickActivity = (PickorderPickActivity)cAppExtension.activity;
            pickorderPickActivity.pHandleScan(pvBarcodeScan);
            cAppExtension.dialogFragment.dismiss();
        }

        if (cAppExtension.activity instanceof IntakeOrderIntakeActivity) {
            IntakeOrderIntakeActivity intakeOrderIntakeActivity = (IntakeOrderIntakeActivity)cAppExtension.activity;
            intakeOrderIntakeActivity.pHandleScan(pvBarcodeScan);
            cAppExtension.dialogFragment.dismiss();
        }

        if (cAppExtension.activity instanceof ReturnArticleDetailActivity) {
            ReturnArticleDetailActivity returnArticleDetailActivity = (ReturnArticleDetailActivity)cAppExtension.activity;
            returnArticleDetailActivity.pHandleScan(pvBarcodeScan);
            cAppExtension.dialogFragment.dismiss();
        }

    }

    //End Region Private Mrethods




}
