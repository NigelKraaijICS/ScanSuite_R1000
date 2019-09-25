package nl.icsvertex.scansuite.fragments.support;


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
import nl.icsvertex.scansuite.SupportPagerAdapter;


public class SupportFragment extends Fragment implements iICSDefaultFragment {
    TabLayout supportTabLayout;
    ViewPager supportViewPager;
    SupportPagerAdapter supportPagerAdapter;

    public static SupportFragment newInstance() { return new SupportFragment(); }

    public SupportFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_support, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mFragmentInitialize();
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
        supportViewPager = getView().findViewById(R.id.supportViewpager);
        supportPagerAdapter = new SupportPagerAdapter(getActivity().getSupportFragmentManager(), supportTabLayout.getTabCount());
        supportTabLayout = getView().findViewById(R.id.supportTabLayout);
    }

    @Override
    public void mSetViewModels() {

    }

    @Override
    public void mFieldsInitialize() {
        supportViewPager.setAdapter(supportPagerAdapter);

        supportTabLayout.addTab(supportTabLayout.newTab().setText(R.string.tab_support_network));
        supportTabLayout.addTab(supportTabLayout.newTab().setText(R.string.tab_support_device));
        supportTabLayout.addTab(supportTabLayout.newTab().setText(R.string.tab_support_application));

    }

    @Override
    public void mSetListeners() {
        mSetTabListener();
    }

    private void mSetTabListener() {
        supportViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(supportTabLayout));
        supportTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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
