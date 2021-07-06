package nl.icsvertex.scansuite.Fragments.Dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.Objects;

import ICS.Interfaces.iICSDefaultFragment;
import ICS.Utils.Scanning.cBarcodeScan;
import ICS.cAppExtension;
import SSU_WHS.Basics.ContentlabelContainer.cContentlabelContainerAdapter;
import SSU_WHS.Picken.Pickorders.cPickorder;
import nl.icsvertex.scansuite.R;

public class ContentlabelContainerFragment extends DialogFragment implements iICSDefaultFragment {
//Region Public Properties


    //End Region Public Properties

    //Region Private Properties
    private RecyclerView containerRecyclerview;
    private Button buttonCloseChooseContainer;
    private ShimmerFrameLayout shimmerViewContainer;
    private cContentlabelContainerAdapter contentlabelContainerAdapter;
    private cContentlabelContainerAdapter getContentlabelContainerAdapter(){
        if (this.contentlabelContainerAdapter == null){
            this.contentlabelContainerAdapter = new cContentlabelContainerAdapter();
        }
        return contentlabelContainerAdapter;
    }
    //End Region Private Properties


    //Region Constructor
    public ContentlabelContainerFragment() {

    }
    //End Region Constructor


    //Region Default Methods
    @Nullable
    @Override
    public View onCreateView(LayoutInflater pvInflater, ViewGroup pvContainer, Bundle pvSavedInstanceState) {
        View rootview = pvInflater.inflate(R.layout.fragment_container, pvContainer);
        cBarcodeScan.pRegisterBarcodeFragmentReceiver(this.getClass().getSimpleName());

        return rootview;
    }

    @Override
    public void onViewCreated(@NonNull View pvView, @Nullable Bundle pvSavedInstanceState) {
        this.mFragmentInitialize();
        cAppExtension.dialogFragment = this;
    }

    @Override
    public void onPause() {
        try {
            this.shimmerViewContainer.stopShimmerAnimation();
            cBarcodeScan.pUnregisterBarcodeFragmentReceiver(this.getClass().getSimpleName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        super.onPause();
    }

    @Override
    public void onDestroy() {
        try {
            cBarcodeScan.pUnregisterBarcodeFragmentReceiver(this.getClass().getSimpleName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        cBarcodeScan.pRegisterBarcodeFragmentReceiver(this.getClass().getSimpleName());

        this.shimmerViewContainer.startShimmerAnimation();
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels - getResources().getDimensionPixelSize(R.dimen.default_double_margin);

        Objects.requireNonNull(Objects.requireNonNull(getDialog()).getWindow()).setLayout(width, height);
    }

    //End Region Default Methods

    //Region iICSDefaultFragment methods
    @Override
    public void mFragmentInitialize() {
        this.mFindViews();
        this.mFieldsInitialize();
        this.mSetListeners();
    }

    @Override
    public void mFindViews() {

        if (getView() != null) {
            this.containerRecyclerview = getView().findViewById(R.id.containerRecyclerview);
            this.buttonCloseChooseContainer = getView().findViewById(R.id.buttonCloseChooseContainer);
            this.shimmerViewContainer = getView().findViewById(R.id.shimmerViewContainer);
        }

    }

    @Override
    public void mFieldsInitialize() {
        this.mGetData();
    }

    @Override
    public void mSetListeners() {
        this.mSetCloseListener();
    }

    //End Region iICSDefaultFragment methods

    //Region Private Methods

    private void mGetData() {

        this.mSetContainerRecycler();
      }

    private void mSetContainerRecycler() {

        this.getContentlabelContainerAdapter().pFillData(cPickorder.currentPickOrder.contentlabelContainerObl);
        this.containerRecyclerview.setHasFixedSize(false);
        this.containerRecyclerview.setAdapter(this.getContentlabelContainerAdapter());
        this.containerRecyclerview.setLayoutManager(new LinearLayoutManager(cAppExtension.context));
        //Stopping Shimmer Effect's animation after data is loaded
        this.shimmerViewContainer.stopShimmerAnimation();
        this.shimmerViewContainer.setVisibility(View.GONE);
    }

    private void mSetCloseListener() {
        this.buttonCloseChooseContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cAppExtension.dialogFragment != null) {
                    cAppExtension.dialogFragment.dismiss();
                }
            }
        });
    }

    //End Region Private Methods

}
