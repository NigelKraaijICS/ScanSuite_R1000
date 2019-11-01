package nl.icsvertex.scansuite.Fragments.dialogs;


import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import ICS.Interfaces.iICSDefaultFragment;
import nl.icsvertex.scansuite.Activities.inventory.InventoryorderBinActivity;
import nl.icsvertex.scansuite.Activities.inventory.InventoryorderBinsActivity;
import nl.icsvertex.scansuite.Activities.pick.PickorderLinesActivity;
import nl.icsvertex.scansuite.R;
import nl.icsvertex.scansuite.Activities.pick.PickorderPickActivity;
import nl.icsvertex.scansuite.Activities.sort.SortorderSortActivity;
import ICS.cAppExtension;

public class AcceptRejectFragment extends DialogFragment implements iICSDefaultFragment {

    //Region Public Properties

    //End Region Public Properties

    //Region Private Properties

    private Button cancelButton;
    private ImageView acceptImageView;
    private ImageView rejectImageView;
    private TextView acceptRejectHeader;
    private TextView acceptRejectText;
    private TextView textReject;
    private TextView textAccept;

    private String  titleStr;
    private String  messageStr;

    //End Region Private Properties

    //Region Constructor
    public AcceptRejectFragment(String pvTitleStr, String pvMessageStr) {
        this.titleStr = pvTitleStr;
        this.messageStr = pvMessageStr;
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
        this.acceptRejectHeader = getView().findViewById(R.id.textViewAcceptRejectHeader);
        this.acceptRejectText = getView().findViewById(R.id.textViewAcceptRejectText);
    }


    @Override
    public void mFieldsInitialize() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                acceptRejectHeader.setText(titleStr);
                acceptRejectText.setText(messageStr);
            }
        });
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

        if (cAppExtension.activity instanceof PickorderLinesActivity) {
            PickorderLinesActivity.pLeaveActivity();
            this.dismiss();
        }

        if (cAppExtension.activity instanceof  PickorderPickActivity) {
            PickorderPickActivity.pAcceptPick();
            this.dismiss();
        }

        if (cAppExtension.activity instanceof SortorderSortActivity) {
            SortorderSortActivity.pAcceptPick();
            this.dismiss();
        }

        if (cAppExtension.activity instanceof InventoryorderBinActivity) {
            InventoryorderBinActivity.pCloseBin();
            this.dismiss();
        }

        if (cAppExtension.activity instanceof InventoryorderBinsActivity) {
            InventoryorderBinsActivity.pCloseOrder();
            this.dismiss();
        }

    }

    private void mReject() {

        if (cAppExtension.activity instanceof PickorderLinesActivity) {
            this.dismiss();
        }

        if (cAppExtension.activity instanceof  PickorderPickActivity) {
            PickorderPickActivity.pCancelPick();
            this.dismiss();
        }

        if (cAppExtension.activity instanceof SortorderSortActivity) {
            SortorderSortActivity.pCancelPick();
            this.dismiss();
        }

        if (cAppExtension.activity instanceof InventoryorderBinActivity) {
            this.dismiss();
        }

        if (cAppExtension.activity instanceof InventoryorderBinsActivity) {
            this.dismiss();
        }

    }

}
