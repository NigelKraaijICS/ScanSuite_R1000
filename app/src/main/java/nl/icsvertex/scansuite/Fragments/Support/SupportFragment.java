package nl.icsvertex.scansuite.Fragments.Support;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import ICS.Interfaces.iICSDefaultFragment;
import ICS.Utils.cUserInterface;
import nl.icsvertex.scansuite.PagerAdapters.SupportPagerAdapter;
import nl.icsvertex.scansuite.R;

public class SupportFragment extends Fragment implements iICSDefaultFragment {

    //Region Public Properties

    //End Region Public Properties

    //Region Private Properties
    private static TabLayout supportTabLayout;
    private static ViewPager supportViewPager;
    private static SupportPagerAdapter supportPagerAdapter;
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
        cUserInterface.pEnableScanner();
    }

    @Override
    public void mFindViews() {

        if (getView() != null) {
            SupportFragment.supportViewPager = getView().findViewById(R.id.supportViewpager);
            SupportFragment.supportTabLayout = getView().findViewById(R.id.supportTabLayout);
        }
    }


    @Override
    public void mFieldsInitialize() {
        SupportFragment.supportTabLayout.addTab(SupportFragment.supportTabLayout.newTab().setText(R.string.tab_support_network));
        SupportFragment.supportTabLayout.addTab(SupportFragment.supportTabLayout.newTab().setText(R.string.tab_support_device));
        SupportFragment.supportTabLayout.addTab(SupportFragment.supportTabLayout.newTab().setText(R.string.tab_support_application));
        SupportFragment.supportPagerAdapter = new SupportPagerAdapter(SupportFragment.supportTabLayout.getTabCount());
        SupportFragment.supportViewPager.setAdapter(SupportFragment.supportPagerAdapter);
    }

    @Override
    public void mSetListeners() {
        this.mSetTabListener();
    }

    private void mSetTabListener() {
        SupportFragment.supportViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(SupportFragment.supportTabLayout));
        SupportFragment.supportTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                SupportFragment.supportViewPager.setCurrentItem(tab.getPosition());
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
