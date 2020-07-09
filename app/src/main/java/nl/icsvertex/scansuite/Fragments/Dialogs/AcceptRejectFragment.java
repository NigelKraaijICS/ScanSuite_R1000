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

import ICS.Interfaces.iICSDefaultFragment;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import nl.icsvertex.scansuite.Activities.Intake.IntakeOrderIntakeActivity;
import nl.icsvertex.scansuite.Activities.Intake.IntakeorderLinesActivity;
import nl.icsvertex.scansuite.Activities.Inventory.InventoryorderBinActivity;
import nl.icsvertex.scansuite.Activities.Inventory.InventoryorderBinsActivity;
import nl.icsvertex.scansuite.Activities.Move.MoveLineTakeActivity;
import nl.icsvertex.scansuite.Activities.Move.MoveLinesActivity;
import nl.icsvertex.scansuite.Activities.Packaging.PackagingActivity;
import nl.icsvertex.scansuite.Activities.Pick.PickorderLinesActivity;
import nl.icsvertex.scansuite.Activities.Pick.PickorderPickActivity;
import nl.icsvertex.scansuite.Activities.Receive.ReceiveLinesActivity;
import nl.icsvertex.scansuite.Activities.Receive.ReceiveOrderReceiveActivity;
import nl.icsvertex.scansuite.Activities.Returns.ReturnArticleDetailActivity;
import nl.icsvertex.scansuite.Activities.Returns.ReturnorderDocumentActivity;
import nl.icsvertex.scansuite.Activities.Returns.ReturnorderDocumentsActivity;
import nl.icsvertex.scansuite.Activities.Sort.SortorderSortActivity;
import nl.icsvertex.scansuite.R;

public class AcceptRejectFragment extends DialogFragment implements iICSDefaultFragment {

    //Region Public Properties

    //End Region Public Properties

    //Region Private Properties

    private  Button cancelButton;
    private  ImageView acceptImageView;
    private  ImageView rejectImageView;
    private  TextView acceptRejectHeader;
    private  TextView acceptRejectText;
    private  TextView textReject;
    private  TextView textAccept;

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
            this.acceptImageView = getView().findViewById(R.id.acceptImageView);
            this.rejectImageView = getView().findViewById(R.id.rejectImageView);
            this.cancelButton = getView().findViewById(R.id.cancelButton);
            this.textReject = getView().findViewById(R.id.textReject);
            this.textAccept = getView().findViewById(R.id.textAccept);
            this.acceptRejectHeader = getView().findViewById(R.id.textViewAcceptRejectHeader);
            this.acceptRejectText = getView().findViewById(R.id.textViewAcceptRejectText);
        }
    }


    @Override
    public void mFieldsInitialize() {
        requireActivity().runOnUiThread(new Runnable() {
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
                    PickorderPickActivity pickorderPickActivity = (PickorderPickActivity)cAppExtension.activity;
                    pickorderPickActivity.pAcceptRejectDialogDismissed();
                }

                if (cAppExtension.activity instanceof ReturnorderDocumentActivity){
                    ReturnorderDocumentActivity returnorderDocumentActivity = (ReturnorderDocumentActivity)cAppExtension.activity;
                    returnorderDocumentActivity.pHandleFragmentDismissed();
                }
                if (cAppExtension.activity instanceof ReturnorderDocumentsActivity){
                    ReturnorderDocumentsActivity returnorderDocumentsActivity = (ReturnorderDocumentsActivity)cAppExtension.activity;
                    returnorderDocumentsActivity.pHandleFragmentDismissed();
                }

                if (cAppExtension.activity instanceof ReturnArticleDetailActivity) {
                    ReturnArticleDetailActivity pickorderPickActivity = (ReturnArticleDetailActivity)cAppExtension.activity;
                    pickorderPickActivity.pHandleFragmentDismissed();
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
            PickorderLinesActivity pickorderLinesActivity = (PickorderLinesActivity)cAppExtension.activity;
            pickorderLinesActivity.pLeaveActivity();
            this.dismiss();
            return;
        }

        if (cAppExtension.activity instanceof  PickorderPickActivity) {
            PickorderPickActivity pickorderPickActivity = (PickorderPickActivity)cAppExtension.activity;
            pickorderPickActivity.pAcceptPick(ignoreAcceptBln);
            this.dismiss();
            return;
        }

        if (cAppExtension.activity instanceof SortorderSortActivity) {
            SortorderSortActivity sortorderSortActivity = (SortorderSortActivity)cAppExtension.activity;
            sortorderSortActivity.pAcceptPick();
            this.dismiss();
            return;
        }

        if (cAppExtension.activity instanceof InventoryorderBinActivity) {
            InventoryorderBinActivity inventoryorderBinActivity = (InventoryorderBinActivity)cAppExtension.activity;
            inventoryorderBinActivity.pCloseBin();
            this.dismiss();
            return;
        }

        if (cAppExtension.activity instanceof InventoryorderBinsActivity) {
            InventoryorderBinsActivity inventoryorderBinsActivity = new InventoryorderBinsActivity();
            inventoryorderBinsActivity.pCloseOrder();
            this.dismiss();
            return;
        }

        if (cAppExtension.activity instanceof IntakeorderLinesActivity) {
            IntakeorderLinesActivity intakeorderLinesActivity = (IntakeorderLinesActivity)cAppExtension.activity;
            intakeorderLinesActivity.pDone();
            this.dismiss();
            return;
        }

        if (cAppExtension.activity instanceof IntakeOrderIntakeActivity) {
            IntakeOrderIntakeActivity intakeOrderIntakeActivity = (IntakeOrderIntakeActivity)cAppExtension.activity;
            intakeOrderIntakeActivity.pAcceptStore();
            this.dismiss();
            return;
        }

        if (cAppExtension.activity instanceof ReturnorderDocumentActivity) {
            ReturnorderDocumentActivity returnorderDocumentActivity = (ReturnorderDocumentActivity)cAppExtension.activity;
            returnorderDocumentActivity.pCloseDocument();
            this.dismiss();
            return;
        }

        if (cAppExtension.activity instanceof ReturnorderDocumentsActivity) {
            ReturnorderDocumentsActivity returnorderDocumentsActivity = (ReturnorderDocumentsActivity)cAppExtension.activity;
            returnorderDocumentsActivity.pCloseOrder();
            this.dismiss();
            return;
        }

        if (cAppExtension.activity instanceof  ReturnArticleDetailActivity) {
            ReturnArticleDetailActivity returnArticleDetailActivity = (ReturnArticleDetailActivity)cAppExtension.activity;
            returnArticleDetailActivity.pDone();
            this.dismiss();
            return;
        }

        if (cAppExtension.activity instanceof ReceiveLinesActivity) {

            ReceiveLinesActivity receiveLinesActivity = (ReceiveLinesActivity)cAppExtension.activity;

            if (ReceiveLinesActivity.closeOrderClickedBln) {
                receiveLinesActivity.pDone();
                this.dismiss();
                return;
            }

            if (ReceiveLinesActivity.packagingClickedBln) {
                receiveLinesActivity.pPackaging();
                this.dismiss();
                return;
            }

            else {
                receiveLinesActivity.pAddUnknownScan(ReceiveLinesActivity.barcodeScanToHandle);
                this.dismiss();
                return;
            }

        }

        if (cAppExtension.activity instanceof ReceiveOrderReceiveActivity) {
            ReceiveOrderReceiveActivity receiveOrderReceiveActivity = (ReceiveOrderReceiveActivity)cAppExtension.activity;
            receiveOrderReceiveActivity.pAcceptReceive();
        }

        if (cAppExtension.activity instanceof PackagingActivity) {
            PackagingActivity packagingActivity = (PackagingActivity)cAppExtension.activity;
            packagingActivity.pHandlePackagingDone();
        }

        if (cAppExtension.activity instanceof MoveLinesActivity) {
            MoveLinesActivity moveLinesActivity = (MoveLinesActivity)cAppExtension.activity;

            if (!moveLinesActivity.closeOrderClickedBln) {
                moveLinesActivity.pLeaveActivity();
                return;
            }

            moveLinesActivity.pDone();
        }

        if (cAppExtension.activity instanceof MoveLineTakeActivity) {
            MoveLineTakeActivity moveLineTakeActivity = (MoveLineTakeActivity)cAppExtension.activity;
            moveLineTakeActivity.pAcceptMove(true);
        }

    }

    private void mReject() {

        if (cAppExtension.activity instanceof PickorderLinesActivity) {
            this.dismiss();
            return;
        }

        if (cAppExtension.activity instanceof  PickorderPickActivity) {
            PickorderPickActivity pickorderPickActivity = (PickorderPickActivity)cAppExtension.activity;
            pickorderPickActivity.pCancelPick();
            this.dismiss();
            return;
        }

        if (cAppExtension.activity instanceof SortorderSortActivity) {
            SortorderSortActivity sortorderSortActivity = (SortorderSortActivity)cAppExtension.activity;
            sortorderSortActivity.pCancelPick();
            this.dismiss();
            return;
        }

        if (cAppExtension.activity instanceof InventoryorderBinActivity) {
            InventoryorderBinActivity inventoryorderBinActivity = (InventoryorderBinActivity)cAppExtension.activity;
            inventoryorderBinActivity.pAcceptRejectDialogDismissed();
            return;
        }

        if (cAppExtension.activity instanceof  InventoryorderBinsActivity) {
            InventoryorderBinsActivity inventoryorderBinsActivity = new InventoryorderBinsActivity();
            inventoryorderBinsActivity.pAcceptRejectDialogDismissed();
            return;
        }

        if (cAppExtension.activity instanceof IntakeOrderIntakeActivity) {
            IntakeOrderIntakeActivity intakeOrderIntakeActivity = (IntakeOrderIntakeActivity)cAppExtension.activity;
            intakeOrderIntakeActivity.pCancelStore();
            this.dismiss();
            return;
        }

        if (cAppExtension.activity instanceof IntakeorderLinesActivity) {
            IntakeorderLinesActivity intakeorderLinesActivity = (IntakeorderLinesActivity)cAppExtension.activity;
            intakeorderLinesActivity.pLeaveActivity();
            this.dismiss();
            return;
        }

        if (cAppExtension.activity instanceof ReceiveLinesActivity) {

            if (ReceiveLinesActivity.closeOrderClickedBln) {
                ReceiveLinesActivity receiveLinesActivity = (ReceiveLinesActivity)cAppExtension.activity;
                receiveLinesActivity.pLeaveActivity();
                return;
            }

            if (ReceiveLinesActivity.packagingClickedBln) {
                ReceiveLinesActivity receiveLinesActivity = (ReceiveLinesActivity)cAppExtension.activity;
                ReceiveLinesActivity.packagingHandledBln = true;
                receiveLinesActivity.pDone();
                return;
            }

            ReceiveLinesActivity.barcodeScanToHandle = null;
            this.dismiss();
            return;
        }

        if (cAppExtension.activity instanceof ReceiveOrderReceiveActivity) {
            ReceiveOrderReceiveActivity receiveOrderReceiveActivity = (ReceiveOrderReceiveActivity)cAppExtension.activity;
            receiveOrderReceiveActivity.pCancelReceive();
            return;
        }

        if (cAppExtension.activity instanceof ReturnorderDocumentActivity){
            ReturnorderDocumentActivity returnorderDocumentActivity = (ReturnorderDocumentActivity)cAppExtension.activity;
            returnorderDocumentActivity.pHandleFragmentDismissed();
            this.dismiss();
            return;
        }

        if (cAppExtension.activity instanceof ReturnorderDocumentsActivity){
            ReturnorderDocumentsActivity returnorderDocumentsActivity = (ReturnorderDocumentsActivity)cAppExtension.activity;
            returnorderDocumentsActivity.pHandleFragmentDismissed();
            this.dismiss();
        }

        if (cAppExtension.activity instanceof PackagingActivity){
            this.dismiss();
        }

        if (cAppExtension.activity instanceof MoveLinesActivity){
            this.dismiss();
        }

        if (cAppExtension.activity instanceof MoveLineTakeActivity) {
            MoveLineTakeActivity moveLineTakeActivity = (MoveLineTakeActivity)cAppExtension.activity;
            moveLineTakeActivity.pCancelMove();
            return;
        }

    }

}
