package nl.icsvertex.scansuite.fragments.dialogs;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import ICS.Interfaces.iICSDefaultFragment;
import nl.icsvertex.scansuite.R;
import nl.icsvertex.scansuite.activities.pick.PickorderPickActivity;
import nl.icsvertex.scansuite.activities.ship.ShipDetermineTransportActivity;
import nl.icsvertex.scansuite.activities.sort.SortorderSortActivity;


public class AcceptRejectFragment extends DialogFragment implements iICSDefaultFragment {
    View thisView;
    Activity callerActivity;
    Button cancelButton;
    ImageView acceptImageView;
    ImageView rejectImageView;
    TextView textReject;
    TextView textAccept;

    public AcceptRejectFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_accept_reject, container, false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
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
        acceptImageView = getView().findViewById(R.id.acceptImageView);
        rejectImageView = getView().findViewById(R.id.rejectImageView);
        cancelButton = getView().findViewById(R.id.cancelButton);
        textReject = getView().findViewById(R.id.textReject);
        textAccept = getView().findViewById(R.id.textAccept);
    }

    @Override
    public void mSetViewModels() {

    }
    @Override
    public void mFieldsInitialize() {

    }
    @Override
    public void mSetListeners() {
        mSetAcceptListener();
        mSetRejectListener();
        mSetCancelListener();
    }
    private void mSetCancelListener() {
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }
    private void mSetAcceptListener() {

        acceptImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAccept();
            }
        });
        textAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAccept();
            }
        });
    }
    private void mSetRejectListener() {
        rejectImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mReject();
            }
        });
        textReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mReject();
            }
        });
    }
    private void mAccept() {
        if (callerActivity instanceof PickorderPickActivity) {
            ((PickorderPickActivity)callerActivity).m_acceptPickorderLine();
        }
        if (callerActivity instanceof SortorderSortActivity) {
            ((SortorderSortActivity)callerActivity).m_acceptPickorderLine();
        }
        if (callerActivity instanceof ShipDetermineTransportActivity) {

        }
    }
    private void mReject() {
        if (callerActivity instanceof PickorderPickActivity) {
            ((PickorderPickActivity)callerActivity).m_rejectPickorderLine();
        }
        if (callerActivity instanceof SortorderSortActivity) {
            ((SortorderSortActivity)callerActivity).m_rejectPickorderLine();
        }
        if (callerActivity instanceof ShipDetermineTransportActivity) {

        }
    }

}
