package nl.icsvertex.scansuite.Fragments.Packaging;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ICS.Interfaces.iICSDefaultFragment;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Basics.Packaging.cPackaging;
import SSU_WHS.Basics.Packaging.cPackagingUnitUsedAdapter;
import SSU_WHS.Intake.Intakeorders.cIntakeorder;
import nl.icsvertex.scansuite.Activities.Packaging.PackagingActivity;
import nl.icsvertex.scansuite.R;

public class AcceptPackagingFragment extends DialogFragment implements iICSDefaultFragment {

    //Region Public Properties

    //End Region Public Properties

    //Region Private Properties

    private  ImageView acceptImageView;
    private  ImageView rejectImageView;
    private  TextView textViewAcceptPackagingHeader;
    private  TextView textReject;
    private  TextView textAccept;
    private RecyclerView recyclerPackaging;

    private String  titleStr;
    private String  acceptStr;
    private String rejectStr;

    private cPackagingUnitUsedAdapter packagingUnitUsedAdapter;
    private cPackagingUnitUsedAdapter getPackagingUnitUsedAdapter() {
        if (this.packagingUnitUsedAdapter == null) {
            this.packagingUnitUsedAdapter = new cPackagingUnitUsedAdapter();
        }

        return  this.packagingUnitUsedAdapter;
    }




    //End Region Private Properties

    //Region Constructor
    public AcceptPackagingFragment(String pvTitleStr,String pvRejectStr, String pvAcceptStr) {
        this.titleStr = pvTitleStr;

        this.acceptStr = pvAcceptStr;
        this.rejectStr = pvRejectStr;

    }
    //End Region Constructor


    //Region Default Methods
    @Override
    public View onCreateView(LayoutInflater pvInflater, ViewGroup pvViewGroup,
                             Bundle pvSavedInstanceState) {

        // Inflate the layout for this fragment
        return pvInflater.inflate(R.layout.fragment_accept_packaging, pvViewGroup, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        cAppExtension.dialogFragment = this;
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
            this.acceptImageView = getView().findViewById(R.id.acceptImageView);
            this.rejectImageView = getView().findViewById(R.id.rejectImageView);
            this.textReject = getView().findViewById(R.id.textReject);
            this.textAccept = getView().findViewById(R.id.textAccept);
            this.textViewAcceptPackagingHeader = getView().findViewById(R.id.textViewAcceptPackagingHeader);
            this.recyclerPackaging = getView().findViewById(R.id.recyclerPackaging);
        }
    }


    @Override
    public void mFieldsInitialize() {
        Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textViewAcceptPackagingHeader.setText(titleStr);
                textReject.setText(rejectStr);
                textAccept.setText(acceptStr);
                mFillRecycler();
            }
        });
    }

    @Override
    public void mSetListeners() {
        this.mSetAcceptListener();
        this.mSetRejectListener();

    }
    //End Region Default Methods

    private void mSetAcceptListener() {
        this.acceptImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAccept();
            }
        });
        this.textAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAccept();
            }
        });
    }

    private void mSetRejectListener() {
        this.rejectImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mReject();
            }
        });
        this.textReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mReject();
            }
        });
    }

    private void mAccept() {

        if (cAppExtension.activity instanceof PackagingActivity) {
            PackagingActivity packagingActivity = (PackagingActivity)cAppExtension.activity;
            packagingActivity.pHandlePackagingDone();
            this.dismiss();
        }

    }

    private void mReject() {

        if (cAppExtension.activity instanceof PackagingActivity){
            this.dismiss();
        }
    }

    private void mFillRecycler() {

        List<cPackaging> packagingUsedObl = new ArrayList<>();

        for (cPackaging packaging : cIntakeorder.currentIntakeOrder.packagingObl) {
            if (packaging.getQuantityUsedInt() > 0) {
                packagingUsedObl.add(packaging);
            }
        }

        //Show the recycler view
        this.getPackagingUnitUsedAdapter().pFillData(packagingUsedObl);
        this.recyclerPackaging.setHasFixedSize(false);
        this.recyclerPackaging.setAdapter(this.getPackagingUnitUsedAdapter());
        this.recyclerPackaging.setLayoutManager(new LinearLayoutManager(cAppExtension.context));
        this.recyclerPackaging.setVisibility(View.VISIBLE);

    }

}
