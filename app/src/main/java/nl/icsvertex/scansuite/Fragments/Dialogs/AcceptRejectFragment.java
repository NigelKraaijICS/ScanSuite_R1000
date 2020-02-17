package nl.icsvertex.scansuite.Fragments.Dialogs;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Objects;

import ICS.Interfaces.iICSDefaultFragment;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Return.ReturnOrder.cReturnorder;
import nl.icsvertex.scansuite.Activities.Intake.IntakeOrderIntakeActivity;
import nl.icsvertex.scansuite.Activities.Intake.IntakeorderLinesActivity;
import nl.icsvertex.scansuite.Activities.Inventory.InventoryorderBinActivity;
import nl.icsvertex.scansuite.Activities.Inventory.InventoryorderBinsActivity;
import nl.icsvertex.scansuite.Activities.Pick.PickorderLinesActivity;
import nl.icsvertex.scansuite.Activities.Pick.PickorderPickActivity;
import nl.icsvertex.scansuite.Activities.Receive.ReceiveLinesActivity;
import nl.icsvertex.scansuite.Activities.Receive.ReceiveOrderReceiveActivity;
import nl.icsvertex.scansuite.Activities.Returns.ReturnorderDocumentActivity;
import nl.icsvertex.scansuite.Activities.Returns.ReturnorderDocumentsActivity;
import nl.icsvertex.scansuite.Activities.Sort.SortorderSortActivity;
import nl.icsvertex.scansuite.R;

public class AcceptRejectFragment extends DialogFragment implements iICSDefaultFragment {

    //Region Public Properties

    //End Region Public Properties

    //Region Private Properties

    private static  Button cancelButton;
    private static ImageView acceptImageView;
    private static ImageView rejectImageView;
    private static TextView acceptRejectHeader;
    private static TextView acceptRejectText;
    private static TextView textReject;
    private static TextView textAccept;

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
            AcceptRejectFragment.acceptImageView = getView().findViewById(R.id.acceptImageView);
            AcceptRejectFragment.rejectImageView = getView().findViewById(R.id.rejectImageView);
            AcceptRejectFragment.cancelButton = getView().findViewById(R.id.cancelButton);
            AcceptRejectFragment.textReject = getView().findViewById(R.id.textReject);
            AcceptRejectFragment.textAccept = getView().findViewById(R.id.textAccept);
            AcceptRejectFragment.acceptRejectHeader = getView().findViewById(R.id.textViewAcceptRejectHeader);
            AcceptRejectFragment.acceptRejectText = getView().findViewById(R.id.textViewAcceptRejectText);
        }
    }


    @Override
    public void mFieldsInitialize() {
        Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AcceptRejectFragment.acceptRejectHeader.setText(titleStr);
                AcceptRejectFragment.acceptRejectText.setText(messageStr);
                AcceptRejectFragment.textReject.setText(rejectStr);
                AcceptRejectFragment.textAccept.setText(acceptStr);
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
        AcceptRejectFragment.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (cAppExtension.activity instanceof  PickorderPickActivity) {
                    PickorderPickActivity.pAcceptRejectDialogDismissed();
                }

                if (cAppExtension.activity instanceof ReturnorderDocumentActivity){
                    ReturnorderDocumentActivity.pHandleFragmentDismissed();
                }
                if (cAppExtension.activity instanceof ReturnorderDocumentsActivity){
                    ReturnorderDocumentsActivity.pHandleFragmentDismissed();
                }


                dismiss();
            }
        });
    }

    private void mSetAcceptListener() {

        AcceptRejectFragment.acceptImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAccept();
            }
        });

        AcceptRejectFragment.textAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAccept();
            }
        });
    }

    private void mSetRejectListener() {
        AcceptRejectFragment.rejectImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mReject();
            }
        });
        AcceptRejectFragment.textReject.setOnClickListener(new View.OnClickListener() {
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

        if (cAppExtension.activity instanceof ReturnorderDocumentActivity) {
            if (!cReturnorder.currentReturnOrder.isRetourMultiDocument()){
                ReturnorderDocumentsActivity.pCloseOrder();
            }
            else{
                ReturnorderDocumentActivity.pCloseDocument();
            }
            this.dismiss();
        }

        if (cAppExtension.activity instanceof ReceiveLinesActivity) {
            ReceiveLinesActivity.pDone();
            this.dismiss();
        }

        if (cAppExtension.activity instanceof ReceiveOrderReceiveActivity) {
            ReceiveOrderReceiveActivity.pAcceptReceive();
        }

        if (cAppExtension.activity instanceof ReturnorderDocumentsActivity) {
            ReturnorderDocumentsActivity.pCloseOrder();
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
            InventoryorderBinActivity.pAcceptRejectDialogDismissed();
        }

        if (cAppExtension.activity instanceof  InventoryorderBinsActivity) {
            InventoryorderBinsActivity.pAcceptRejectDialogDismissed();
        }

        if (cAppExtension.activity instanceof IntakeOrderIntakeActivity) {
            IntakeOrderIntakeActivity.pCancelStore();
            this.dismiss();
        }

        if (cAppExtension.activity instanceof IntakeorderLinesActivity) {
            IntakeorderLinesActivity.pLeaveActivity();
            this.dismiss();
        }

        if (cAppExtension.activity instanceof ReceiveLinesActivity) {
            ReceiveLinesActivity.pLeaveActivity();
        }

        if (cAppExtension.activity instanceof ReceiveOrderReceiveActivity) {
            ReceiveOrderReceiveActivity.pCancelReceive();
        }

        if (cAppExtension.activity instanceof ReturnorderDocumentActivity){
            ReturnorderDocumentActivity.pStartDocumentsActivity();
            this.dismiss();
        }
        if (cAppExtension.activity instanceof ReturnorderDocumentsActivity){
            ReturnorderDocumentsActivity.pHandleFragmentDismissed();
            this.dismiss();
        }

    }

}
