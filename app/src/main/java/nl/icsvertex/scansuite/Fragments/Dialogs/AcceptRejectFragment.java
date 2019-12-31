package nl.icsvertex.scansuite.Fragments.Dialogs;


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
import ICS.Utils.cUserInterface;
import nl.icsvertex.scansuite.Activities.Intake.IntakeOrderIntakeActivity;
import nl.icsvertex.scansuite.Activities.Intake.IntakeorderLinesActivity;
import nl.icsvertex.scansuite.Activities.Inventory.InventoryorderBinActivity;
import nl.icsvertex.scansuite.Activities.Inventory.InventoryorderBinsActivity;
import nl.icsvertex.scansuite.Activities.Pick.PickorderLinesActivity;
import nl.icsvertex.scansuite.R;
import nl.icsvertex.scansuite.Activities.Pick.PickorderPickActivity;
import nl.icsvertex.scansuite.Activities.Sort.SortorderSortActivity;
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
    private String  acceptStr;
    private String rejectStr;
    private boolean ignoreAcceptBln;

    //End Region Private Properties

    //Region Constructor
    public AcceptRejectFragment(String pvTitleStr, String pvMessageStr, String pvRejectStr, String pvAcceptStr, boolean pvIgnoreAccept) {
        this.titleStr = pvTitleStr;
        this.messageStr = pvMessageStr;

        this.acceptStr = pvAcceptStr;
        this.rejectStr = pvRejectStr;

        this.ignoreAcceptBln = pvIgnoreAccept;
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
        cUserInterface.pEnableScanner();
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
                textReject.setText(rejectStr);
                textAccept.setText(acceptStr);
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


                if (cAppExtension.activity instanceof  PickorderPickActivity) {
                    PickorderPickActivity.pAcceptRejectDialogDismissed();
                }

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
            PickorderPickActivity.pAcceptPick(ignoreAcceptBln);
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

        if (cAppExtension.activity instanceof IntakeorderLinesActivity) {
            IntakeorderLinesActivity.pDone();
            this.dismiss();
        }

        if (cAppExtension.activity instanceof IntakeOrderIntakeActivity) {
            IntakeOrderIntakeActivity.pAcceptStore();
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

        if (cAppExtension.activity instanceof  InventoryorderBinsActivity) {
            InventoryorderBinsActivity.pAcceptRejectDialogDismissed();
        }

        if (cAppExtension.activity instanceof IntakeOrderIntakeActivity) {
            this.dismiss();
        }

        if (cAppExtension.activity instanceof IntakeorderLinesActivity) {
            IntakeorderLinesActivity.pLeaveActivity();
            this.dismiss();
        }

    }

}
