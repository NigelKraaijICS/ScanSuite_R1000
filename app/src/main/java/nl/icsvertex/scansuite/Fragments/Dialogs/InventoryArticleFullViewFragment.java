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
import ICS.Utils.cRegex;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Basics.BarcodeLayouts.cBarcodeLayout;
import SSU_WHS.Picken.PickorderLines.cPickorderLine;
import nl.icsvertex.scansuite.Activities.Pick.PickorderPickActivity;
import nl.icsvertex.scansuite.R;


public class InventoryArticleFullViewFragment extends DialogFragment implements iICSDefaultFragment {

    //Region Public Properties

    //End Region Public Properties

    //Region Private Properties
    private static ImageView toolbarImage;
    private static  TextView toolbarTitle;
    private static TextView toolbarSubtext;

    private static ConstraintLayout articleFullViewContainer;
    private static TextView articleFullItemNoTextView;
    private static TextView articleFullVariantTextView;
    private static ImageView articleFullImageView;
    //End Region Private Properties


    //Region Constructor
    public InventoryArticleFullViewFragment() {
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

            if (cAppExtension.activity instanceof PickorderPickActivity) {
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

        Objects.requireNonNull(Objects.requireNonNull(getDialog()).getWindow()).setLayout(width, height);
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

        if (getView() != null) {
            InventoryArticleFullViewFragment.toolbarImage = getView().findViewById(R.id.toolbarImage);
            InventoryArticleFullViewFragment.toolbarTitle = getView().findViewById(R.id.toolbarTitle);
            InventoryArticleFullViewFragment.toolbarSubtext = getView().findViewById(R.id.toolbarSubtext);

            InventoryArticleFullViewFragment.articleFullViewContainer = getView().findViewById(R.id.articleFuillViewContainer);
            InventoryArticleFullViewFragment.articleFullItemNoTextView = getView().findViewById(R.id.articleFullItemNoTextView);
            InventoryArticleFullViewFragment.articleFullVariantTextView = getView().findViewById(R.id.articleFullVariantTextView);
            InventoryArticleFullViewFragment.articleFullImageView = getView().findViewById(R.id.articleFullImageView);
        }

    }



    @Override
    public void mFieldsInitialize() {

        if (!cPickorderLine.currentPickOrderLine.getDescription2Str().isEmpty()) {
            InventoryArticleFullViewFragment.toolbarSubtext.setText(cPickorderLine.currentPickOrderLine.getDescription2Str());
            InventoryArticleFullViewFragment.toolbarSubtext.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            InventoryArticleFullViewFragment.toolbarSubtext.setSingleLine(true);
            InventoryArticleFullViewFragment.toolbarSubtext.setMarqueeRepeatLimit(5);
            InventoryArticleFullViewFragment.toolbarSubtext.postDelayed(new Runnable() {
                @Override
                public void run() {
                    InventoryArticleFullViewFragment.toolbarSubtext.setSelected(true);
                }
            },1500);
        }

        InventoryArticleFullViewFragment.articleFullItemNoTextView.setText(cPickorderLine.currentPickOrderLine.getItemNoStr());
        InventoryArticleFullViewFragment.articleFullVariantTextView.setText(cPickorderLine.currentPickOrderLine.getVariantCodeStr());
        InventoryArticleFullViewFragment.articleFullVariantTextView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        InventoryArticleFullViewFragment.articleFullVariantTextView.setSingleLine(true);
        InventoryArticleFullViewFragment.articleFullVariantTextView.setMarqueeRepeatLimit(5);
        InventoryArticleFullViewFragment.articleFullVariantTextView.setSelected(true);

        if (cPickorderLine.currentPickOrderLine.articleImage != null) {
            byte[] decodedString = Base64.decode(cPickorderLine.currentPickOrderLine.articleImage.getImageStr(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            InventoryArticleFullViewFragment.articleFullImageView.setImageBitmap(decodedByte);
        }
        else {
            InventoryArticleFullViewFragment.articleFullImageView.setImageDrawable(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.ic_no_image_lightgrey_24dp));
        }
    }

    @Override
    public void mSetListeners() {
        mDismissListener();
    }

    //End Region Default Methods

    //Region Public Methods

    public static void pHandleScan(String pvBarcodeStr){

        String barcodeWithoutPrefixStr;

        //No prefix
        if (!cRegex.hasPrefix(pvBarcodeStr)) {
            InventoryArticleFullViewFragment.mArticleScanned(pvBarcodeStr);
            return;
        }

        boolean foundBln = false;

        if (cBarcodeLayout.pCheckBarcodeWithLayoutBln(pvBarcodeStr, cBarcodeLayout.barcodeLayoutEnu.ARTICLE)) {
            foundBln = true;
        }

        //has prefix, is article
        if (foundBln) {
            barcodeWithoutPrefixStr = cRegex.pStripRegexPrefixStr(pvBarcodeStr);
            InventoryArticleFullViewFragment.mArticleScanned(barcodeWithoutPrefixStr);
        }
        else {
            //has prefix, isn't branch
            cUserInterface.pDoNope(InventoryArticleFullViewFragment.articleFullViewContainer, true, true);
        }
    }
    //End Region Public Methods

    //Region Private Methods

    private void mSetToolbar() {

        InventoryArticleFullViewFragment.toolbarTitle.setText(cPickorderLine.currentPickOrderLine.getDescriptionStr());
        InventoryArticleFullViewFragment.toolbarTitle.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        InventoryArticleFullViewFragment.toolbarTitle.setSingleLine(true);
        InventoryArticleFullViewFragment.toolbarTitle.setMarqueeRepeatLimit(5);
        InventoryArticleFullViewFragment.toolbarTitle.postDelayed(new Runnable() {
            @Override
            public void run() {
                InventoryArticleFullViewFragment.toolbarTitle.setSelected(true);
            }
        },1500);

        InventoryArticleFullViewFragment.toolbarImage.setImageResource(R.drawable.ic_info);

    }

    private void mDismissListener() {
        Objects.requireNonNull(getView()).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }


    //todo: check why we don't use this
    private static void mArticleScanned(String pvBarcodeStr) {


    }

    //End Region Private Mrethods




}
