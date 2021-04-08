package nl.icsvertex.scansuite.Fragments.Dialogs;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import ICS.Interfaces.iICSDefaultFragment;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Basics.Article.cArticle;
import SSU_WHS.Basics.PropertyGroup.cPropertyGroup;
import SSU_WHS.Basics.Settings.cSetting;
import SSU_WHS.Picken.PickorderLineProperty.cPickorderLineProperty;
import SSU_WHS.Picken.PickorderLinePropertyValue.cPickorderLinePropertyValue;
import SSU_WHS.Picken.PickorderLinePropertyValue.cPickorderLinePropertyValueNoInputAdapter;
import SSU_WHS.Picken.PickorderLines.cPickorderLine;
import nl.icsvertex.scansuite.PagerAdapters.ArticleInfoPagerAdapter;
import nl.icsvertex.scansuite.R;

public class ItemPropertyNoInputFragment extends DialogFragment implements iICSDefaultFragment {

   //Region Private Properties
   private  ImageView toolbarImage;
   private  TextView toolbarTitle;

    private AppCompatImageButton imageButtonNoInputPropertys;
    private TextView articleDescriptionCompactText;
    private TextView articleDescription2CompactText;
    private TextView articleItemCompactText;
    private TextView articleBarcodeCompactText;

   // private  RecyclerView itemPropertyRecyclerview;
    private  Button buttonOK;

    private TabLayout itemPropertyTabLayout;
    private ViewPager itemPropertyViewpager;
    public  int numberOfTabsInt;

    private  List<cPickorderLinePropertyValue> localItemPropertyValueObl;


    private cPickorderLinePropertyValueNoInputAdapter pickorderLinePropertyAdapter;
    private cPickorderLinePropertyValueNoInputAdapter getPickorderLinePropertyAdapter(){
        if (this.pickorderLinePropertyAdapter == null) {
            this.pickorderLinePropertyAdapter = new cPickorderLinePropertyValueNoInputAdapter();
        }

        return  pickorderLinePropertyAdapter;
    }

    //End Region Private Properties


    //Region Constructor
    public ItemPropertyNoInputFragment() {
        // Required empty public constructor
    }

//    public ItemPropertyNoInputFragment(List<cPickorderLinePropertyValue> pvDataObl) {
//        this.localItemPropertyValueObl = pvDataObl;
//    }
    //End Region Constructor

    //Region Default Methods

    @Nullable
    @Override
    public View onCreateView(LayoutInflater pvInflater, ViewGroup pvContainer, Bundle pvSavedInstanceState) {
        return pvInflater.inflate(R.layout.fragment_itemproperty_no_input, pvContainer);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        cAppExtension.dialogFragment = this;
        this.mFragmentInitialize();
    }

    @Override
    public void onResume() {
        super.onResume();
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels - getResources().getDimensionPixelSize(R.dimen.default_double_margin);

        Objects.requireNonNull(Objects.requireNonNull(getDialog()).getWindow()).setLayout(width, height);
        cUserInterface.pEnableScanner();
    }

    //End Region Default Methods

    //Region iICSDefaultFragment methods

    @Override
    public void mFragmentInitialize() {
        this.mFindViews();
        this.mFieldsInitialize();
        this.mSetListeners();
        this.mSetToolbar();
     //   this.mSetItemPropertyValueRecycler();
    }

    @Override
    public void mFindViews() {
        if (getView() != null) {
            this.toolbarImage = getView().findViewById(R.id.toolbarImage);
            this.toolbarTitle = getView().findViewById(R.id.toolbarTitle);

            this.imageButtonNoInputPropertys = getView().findViewById(R.id.imageButtonNoInputPropertys);
            this.articleDescriptionCompactText = getView().findViewById(R.id.articleDescriptionCompactText);
            this.articleDescription2CompactText = getView().findViewById(R.id.articleDescription2CompactText);
            this.articleItemCompactText = getView().findViewById(R.id.articleItemCompactText);
            this.articleBarcodeCompactText = getView().findViewById(R.id.articleBarcodeCompactText);

            //this.itemPropertyRecyclerview = getView().findViewById(R.id.itemPropertyRecyclerview);
            this.itemPropertyTabLayout = getView().findViewById(R.id.itemPropertyTabLayout);
            this.itemPropertyViewpager = getView().findViewById(R.id.itemPropertyViewpager);
            this.buttonOK = getView().findViewById(R.id.buttonOK);
        }
    }


    @Override
    public void mFieldsInitialize() {
        this.mSetArticleInfo();
        this.mBuildAndFillTabs();
    }

    @Override
    public void mSetListeners() {
     //   this.mSetHeaderListener();
        this.mSetCloseListener();
    }

    //End Regioni ICSDefaultFragment methods

    //Region Public Methods

    //Region Public Methods


    //End Region Public Methods
    //End Region Public Methods

    //Region Private Methods
    private void mSetToolbar() {

        this.toolbarTitle.setText(cAppExtension.activity.getString(R.string.screentitle_itemproperty_no_input));
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
    private void mSetCloseListener() {
        this.buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cAppExtension.dialogFragment.dismiss();
            }
        });
    }

//    private void mSetHeaderListener() {
//        this.toolbarTitle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mScrollToBottom();
//            }
//        });
//
//        this.toolbarTitle.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                mScrollToTop();
//                return true;
//            }
//        });
//    }

//    private void mScrollToTop() {
//        this.itemPropertyRecyclerview.smoothScrollToPosition(0);
//    }
//
//    private void mScrollToBottom() {
//        if (this.getPickorderLinePropertyAdapter()!= null) {
//            if (this.getPickorderLinePropertyAdapter().getItemCount() > 0) {
//                this.itemPropertyRecyclerview.smoothScrollToPosition(this.getPickorderLinePropertyAdapter().getItemCount() -1 );
//            }
//        }
//    }
//
//    private void mSetItemPropertyValueRecycler() {
//        this.itemPropertyRecyclerview.setHasFixedSize(false);
//        this.itemPropertyRecyclerview.setAdapter(this.getPickorderLinePropertyAdapter());
//        this.itemPropertyRecyclerview.setLayoutManager(new LinearLayoutManager(cAppExtension.context));
//        this.getPickorderLinePropertyAdapter().pFillData(this.localItemPropertyValueObl);
//    }
    private void  mSetArticleInfo(){

        this.articleDescriptionCompactText.setText(cPickorderLine.currentPickOrderLine.getDescriptionStr());
        this.articleDescription2CompactText.setText(cPickorderLine.currentPickOrderLine.getDescription2Str());
        this.articleItemCompactText.setText(cPickorderLine.currentPickOrderLine.getItemNoAndVariantStr());
        this.articleBarcodeCompactText.setVisibility(View.GONE);
        this.imageButtonNoInputPropertys.setVisibility(View.GONE);
    }

    private void mBuildAndFillTabs() {
        ArrayList<Fragment> fragments = new ArrayList<>();

//        this.itemPropertyTabLayout = getView().findViewById(R.id.itemPropertyTabLayout);
//        for (cPickorderLineProperty pickorderLineProperty : cPickorderLine.currentPickOrderLine.pickorderLinePropertyNoInputObl()) {
//
//            DynamicItemPropertyFragment dynamicItemPropertyFragment = new DynamicItemPropertyFragment(pickorderLineProperty.getPropertyCodeStr(), pickorderLineProperty.propertyValueObl());
//            fragments.add(dynamicItemPropertyFragment);
//
//        }

        ArticleInfoPagerAdapter articleInfoPagerAdapter = new ArticleInfoPagerAdapter(this.getChildFragmentManager(),  fragments);
        this.itemPropertyViewpager.setAdapter(articleInfoPagerAdapter);
        this.itemPropertyTabLayout.setupWithViewPager(itemPropertyViewpager);
    }
    //End Region Private Methods
}
