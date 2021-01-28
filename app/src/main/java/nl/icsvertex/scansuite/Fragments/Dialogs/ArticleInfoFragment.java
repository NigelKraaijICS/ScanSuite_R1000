package nl.icsvertex.scansuite.Fragments.Dialogs;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import ICS.Interfaces.iICSDefaultFragment;
import ICS.cAppExtension;
import SSU_WHS.Basics.Article.cArticle;
import SSU_WHS.Basics.PropertyGroup.cPropertyGroup;
import SSU_WHS.Basics.Settings.cSetting;
import nl.icsvertex.scansuite.PagerAdapters.ArticleInfoPagerAdapter;
import nl.icsvertex.scansuite.R;

public class ArticleInfoFragment extends Fragment implements iICSDefaultFragment {

    //Region Public Properties
    private TabLayout articleInfoTabLayout;
    private ViewPager articleInfoViewpager;
    public  int numberOfTabsInt;


    //End Region Public Properties

    //Region Private Properties
    private final LinkedHashMap<String,  LinkedHashMap<String,String>> articleInfoLinkedHashmap;
    //End Region Private Properties

    //Region Constructor
    public ArticleInfoFragment(LinkedHashMap<String, LinkedHashMap<String,String>> pvArticleInfoLinkedHashmap) {
        this.articleInfoLinkedHashmap = pvArticleInfoLinkedHashmap;
        // Required empty public constructor
    }
    //End Region Constructor

    //Region Default Methods

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_article_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        this.mFragmentInitialize();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void mFragmentInitialize() {
        this.mFindViews();
        this.mFieldsInitialize();
        this.mSetListeners();
        this.mSetToolbar();
    }

    @Override
    public void mFindViews() {
        if (getView() != null) {
            this.articleInfoTabLayout = getView().findViewById(R.id.articleInfoTabLayout);
            this.articleInfoViewpager = getView().findViewById(R.id.articleInfoViewpager);
        }
    }

     @Override
    public void mFieldsInitialize() {
         this.mBuildAndFillTabs();
    }

    @Override
    public void mSetListeners() {

    }

    //End Region Default Methods

    //Region Public Methods
    //End Region Public Methods

    //Region Private Methods

    private void mSetToolbar() {

    }

    private void mBuildAndFillTabs() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        List<Drawable> drawableObl = new ArrayList<>();

        this.articleInfoTabLayout = getView().findViewById(R.id.articleInfoTabLayout);

        for (Map.Entry<String, LinkedHashMap<String, String>> articleInfoEntry :  this.articleInfoLinkedHashmap.entrySet()) {

            cPropertyGroup  propertyGroup = cPropertyGroup.pGetPropertyGroupByNameStr((articleInfoEntry.getKey()));
            if (propertyGroup == null) {
                continue;
            }

            this.articleInfoTabLayout.addTab(this.articleInfoTabLayout.newTab());
            drawableObl.add(propertyGroup.imageDrawable());

            DynamicArticleInfoFragment dynamicArticleInfoFragment = new DynamicArticleInfoFragment(articleInfoEntry.getKey(),articleInfoEntry.getValue());
            fragments.add(dynamicArticleInfoFragment);
        }

        if (cSetting.REALTIME_BARCODE_CHECK() && cArticle.currentArticle != null ) {

            this.articleInfoTabLayout.addTab(this.articleInfoTabLayout.newTab());
            DynamicStockFragment dynamicStockFragment = new DynamicStockFragment();
            fragments.add(dynamicStockFragment);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                Drawable drawable   = cAppExtension.activity.getDrawable(R.drawable.ic_stock);
                drawableObl.add(drawable);
            }

        }

        ArticleInfoPagerAdapter articleInfoPagerAdapter = new ArticleInfoPagerAdapter(this.getChildFragmentManager(),  fragments);
        this.articleInfoViewpager.setAdapter(articleInfoPagerAdapter);
        this.articleInfoTabLayout.setupWithViewPager(articleInfoViewpager);

        for (Drawable drawable : drawableObl) {
            this.articleInfoTabLayout.getTabAt(drawableObl.indexOf(drawable)).setIcon(drawable);
        }


    }
}
