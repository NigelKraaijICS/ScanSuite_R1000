package nl.icsvertex.scansuite.Fragments.support;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ICS.Interfaces.iICSDefaultFragment;
import nl.icsvertex.scansuite.R;
import nl.icsvertex.scansuite.PagerAdapters.SupportPagerAdapter;

public class SupportFragment extends Fragment implements iICSDefaultFragment {

    //Region Public Properties

    //End Region Public Properties

    //Region Private Properties
    private TabLayout supportTabLayout;
    private ViewPager supportViewPager;
    private SupportPagerAdapter supportPagerAdapter;
    //End Region Private Properties


    //Region Constructor

    public SupportFragment() {
        // Required empty public constructor
    }

    //End Region Constructor


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_support, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        this.mFragmentInitialize();
    }

    @Override
    public void mFragmentInitialize() {
        this.mFindViews();
        this.mFieldsInitialize();
        this.mSetListeners();
    }

    @Override
    public void mFindViews() {
        this.supportViewPager = getView().findViewById(R.id.supportViewpager);
        this.supportTabLayout = getView().findViewById(R.id.supportTabLayout);



    }


    @Override
    public void mFieldsInitialize() {

        this.supportTabLayout.addTab(this.supportTabLayout.newTab().setText(R.string.tab_support_network));
        this.supportTabLayout.addTab(this.supportTabLayout.newTab().setText(R.string.tab_support_device));
        this.supportTabLayout.addTab(this.supportTabLayout.newTab().setText(R.string.tab_support_application));
        this.supportPagerAdapter = new SupportPagerAdapter(this.supportTabLayout.getTabCount());

        this.supportViewPager.setAdapter(this.supportPagerAdapter);

    }

    @Override
    public void mSetListeners() {
        this.mSetTabListener();
    }

    private void mSetTabListener() {
        this.supportViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(this.supportTabLayout));
        this.supportTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                supportViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

}