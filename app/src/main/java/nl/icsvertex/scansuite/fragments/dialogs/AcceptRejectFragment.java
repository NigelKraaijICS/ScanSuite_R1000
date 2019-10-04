package nl.icsvertex.scansuite.fragments.dialogs;


import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import ICS.Interfaces.iICSDefaultFragment;
import nl.icsvertex.scansuite.R;
import nl.icsvertex.scansuite.activities.pick.PickorderPickActivity;
import nl.icsvertex.scansuite.activities.sort.SortorderSortActivity;
import nl.icsvertex.scansuite.cAppExtension;

public class AcceptRejectFragment extends DialogFragment implements iICSDefaultFragment {

    //Region Public Properties

    //End Region Public Properties

    //Region Private Properties

    private Button cancelButton;
    private ImageView acceptImageView;
    private ImageView rejectImageView;
    private TextView textReject;
    private TextView textAccept;

    //End Region Private Properties

    //Region Constructor
    public AcceptRejectFragment() {
        // Required empty public constructor
    }
    //End Region Constructor


    //Region Default Methods
    @Override
    public View onCreateView(LayoutInflater pvInflater, ViewGroup pvViewGroup,
                             Bundle pvSavedInstanceState) {

        // Inflate the layout for this fragment
        return pvInflater.inflate(R.layout.fragment_accept_reject, pvViewGroup, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        cAppExtension.dialogFragment = this;
        this.mFragmentInitialize();
    }

    @Override
    public void mFragmentInitialize() {
        this.mFindViews();
        this.mSetViewModels();
        this.mFieldsInitialize();
        this.mSetListeners();
    }

    @Override
    public void mFindViews() {
        this.acceptImageView = getView().findViewById(R.id.acceptImageView);
        this.rejectImageView = getView().findViewById(R.id.rejectImageView);
        this.cancelButton = getView().findViewById(R.id.cancelButton);
        this.textReject = getView().findViewById(R.id.textReject);
        this.textAccept = getView().findViewById(R.id.textAccept);
    }

    @Override
    public void mSetViewModels() {

    }
    @Override
    public void mFieldsInitialize() {

    }
    @Override
    public void mSetListeners() {
        this.mSetAcceptListener();
        this.mSetRejectListener();
        this.mSetCancelListener();
    }
    //End Region Default Methods


    private void mSetCancelListener() {
        this.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }
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

        if (cAppExtension.activity instanceof  PickorderPickActivity) {
            PickorderPickActivity.pAcceptPick();
        }


        if (cAppExtension.activity instanceof SortorderSortActivity) {
            SortorderSortActivity.pAcceptPick();
        }

        //todo: put this back
//        if (callerActivity instanceof ShipDetermineTransportActivity) {
//
//        }
    }

    private void mReject() {

        if (cAppExtension.activity instanceof  PickorderPickActivity) {
            PickorderPickActivity.pCancelPick();
        }

        if (cAppExtension.activity instanceof SortorderSortActivity) {
            SortorderSortActivity.pCancelPick();
        }


        //todo: put this back
//        if (callerActivity instanceof SortorderSortActivity) {
//            ((SortorderSortActivity)callerActivity).m_rejectPickorderLine();
//        }
//        if (callerActivity instanceof ShipDetermineTransportActivity) {
//
//        }
    }

}
