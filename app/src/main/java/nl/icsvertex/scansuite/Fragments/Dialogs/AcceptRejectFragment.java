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
import nl.icsvertex.scansuite.Activities.Intake.IntakeOrderIntakeGeneratedActivity;
import nl.icsvertex.scansuite.Activities.Intake.IntakeOrderLinePropertyInputActivity;
import nl.icsvertex.scansuite.Activities.Intake.IntakeorderMASLinesActivity;
import nl.icsvertex.scansuite.Activities.Intake.IntakeorderMATLinesActivity;
import nl.icsvertex.scansuite.Activities.Inventory.InventoryLinePropertyInputActivity;
import nl.icsvertex.scansuite.Activities.Inventory.InventoryorderBinActivity;
import nl.icsvertex.scansuite.Activities.Inventory.InventoryorderBinsActivity;
import nl.icsvertex.scansuite.Activities.Move.MoveLineItemPropertyActivity;
import nl.icsvertex.scansuite.Activities.Move.MoveLinePlaceGeneratedActivity;
import nl.icsvertex.scansuite.Activities.Move.MoveLinePlaceMTActivity;
import nl.icsvertex.scansuite.Activities.Move.MoveLineTakeActivity;
import nl.icsvertex.scansuite.Activities.Move.MoveLineTakeMTActivity;
import nl.icsvertex.scansuite.Activities.Move.MoveLinesActivity;
import nl.icsvertex.scansuite.Activities.Move.MoveLinesPlaceMTActivity;
import nl.icsvertex.scansuite.Activities.Move.MoveLinesTakeMTActivity;
import nl.icsvertex.scansuite.Activities.Move.MoveorderLinesPlaceGeneratedActivity;
import nl.icsvertex.scansuite.Activities.Packaging.PackagingActivity;
import nl.icsvertex.scansuite.Activities.Pick.PickorderLineItemPropertyInputActvity;
import nl.icsvertex.scansuite.Activities.Pick.PickorderLinesActivity;
import nl.icsvertex.scansuite.Activities.Pick.PickorderLinesGeneratedActivity;
import nl.icsvertex.scansuite.Activities.Pick.PickorderPickActivity;
import nl.icsvertex.scansuite.Activities.Pick.PickorderPickGeneratedActivity;
import nl.icsvertex.scansuite.Activities.QualityControl.PickorderQCActivity;
import nl.icsvertex.scansuite.Activities.Receive.ReceiveLinesActivity;
import nl.icsvertex.scansuite.Activities.Receive.ReceiveOrderReceiveActivity;
import nl.icsvertex.scansuite.Activities.Receive.ReceiveorderLinePropertyInputActivity;
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

    private final String  titleStr;
    private final String  messageStr;
    private final String  acceptStr;
    private final String rejectStr;
    private final boolean ignoreAcceptBln;

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

        if (cAppExtension.activity instanceof PickorderLinesGeneratedActivity) {
            PickorderLinesGeneratedActivity pickorderLinesGeneratedActivity = (PickorderLinesGeneratedActivity)cAppExtension.activity;
            pickorderLinesGeneratedActivity.pLeaveActivity();
            this.dismiss();
            return;
        }

        if (cAppExtension.activity instanceof  PickorderPickActivity) {
            PickorderPickActivity pickorderPickActivity = (PickorderPickActivity)cAppExtension.activity;
            pickorderPickActivity.pAcceptPick(ignoreAcceptBln);
            this.dismiss();
            return;
        }

        if (cAppExtension.activity instanceof PickorderLineItemPropertyInputActvity) {
            PickorderLineItemPropertyInputActvity pickorderLineItemPropertyInputActvity = (PickorderLineItemPropertyInputActvity)cAppExtension.activity;
            pickorderLineItemPropertyInputActvity.amountHandledBln = true;
            pickorderLineItemPropertyInputActvity.pHandled();
            this.dismiss();
            return;
        }

        if (cAppExtension.activity instanceof SortorderSortActivity) {
            SortorderSortActivity sortorderSortActivity = (SortorderSortActivity)cAppExtension.activity;
            sortorderSortActivity.pAcceptPick();
            this.dismiss();
            return;
        }

        if (cAppExtension.activity instanceof PickorderQCActivity) {
            PickorderQCActivity pickorderQCActivity = (PickorderQCActivity)cAppExtension.activity;
            pickorderQCActivity.pAcceptQC(ignoreAcceptBln);
            this.dismiss();
            return;
        }

        if (cAppExtension.activity instanceof PickorderPickGeneratedActivity) {
            PickorderPickGeneratedActivity pickorderPickGeneratedActivity = (PickorderPickGeneratedActivity)cAppExtension.activity;
            pickorderPickGeneratedActivity.pAcceptPick(true);
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

        if (cAppExtension.activity instanceof InventoryLinePropertyInputActivity) {
            InventoryLinePropertyInputActivity inventoryLinePropertyInputActivity = (InventoryLinePropertyInputActivity)cAppExtension.activity;
            inventoryLinePropertyInputActivity.amountHandledBln = true;
            inventoryLinePropertyInputActivity.pHandled();
            this.dismiss();
            return;
        }

        if (cAppExtension.activity instanceof IntakeOrderLinePropertyInputActivity) {
            IntakeOrderLinePropertyInputActivity intakeOrderLinePropertyInputActivity = (IntakeOrderLinePropertyInputActivity)cAppExtension.activity;
            intakeOrderLinePropertyInputActivity.pSendScansBln();
            this.dismiss();
            return;
        }

        if (cAppExtension.activity instanceof IntakeorderMATLinesActivity) {
            IntakeorderMATLinesActivity intakeorderMATLinesActivity = (IntakeorderMATLinesActivity)cAppExtension.activity;
            intakeorderMATLinesActivity.pDone();
            this.dismiss();
            return;
        }

        if (cAppExtension.activity instanceof IntakeorderMASLinesActivity) {
            IntakeorderMASLinesActivity intakeorderMASLinesActivity = (IntakeorderMASLinesActivity)cAppExtension.activity;
            intakeorderMASLinesActivity.pDone();
            this.dismiss();
            return;
        }

        if (cAppExtension.activity instanceof IntakeOrderIntakeGeneratedActivity) {
            IntakeOrderIntakeGeneratedActivity intakeOrderIntakeGeneratedActivity = (IntakeOrderIntakeGeneratedActivity)cAppExtension.activity;
            intakeOrderIntakeGeneratedActivity.pAcceptStore();
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
            returnorderDocumentActivity.pCloseDocument("");
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
            }

            else {
                receiveLinesActivity.pAddUnknownScan(ReceiveLinesActivity.barcodeScanToHandle);
            }
            this.dismiss();
            return;

        }

        if (cAppExtension.activity instanceof ReceiveOrderReceiveActivity) {
            ReceiveOrderReceiveActivity receiveOrderReceiveActivity = (ReceiveOrderReceiveActivity)cAppExtension.activity;
            receiveOrderReceiveActivity.pAcceptReceive();
        }

        if (cAppExtension.activity instanceof ReceiveorderLinePropertyInputActivity) {
            ReceiveorderLinePropertyInputActivity receiveorderLinePropertyInputActivity = (ReceiveorderLinePropertyInputActivity)cAppExtension.activity;
            receiveorderLinePropertyInputActivity.amountHandledBln = true;
            receiveorderLinePropertyInputActivity.pHandled();
            this.dismiss();
            return;
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

        if (cAppExtension.activity instanceof MoveLinesTakeMTActivity) {
            MoveLinesTakeMTActivity moveLinesTakeMTActivity = (MoveLinesTakeMTActivity)cAppExtension.activity;
            moveLinesTakeMTActivity.pLeaveActivity();
        }

        if (cAppExtension.activity instanceof MoveLinesPlaceMTActivity) {
            MoveLinesPlaceMTActivity moveLinesPlaceMTActivity = (MoveLinesPlaceMTActivity)cAppExtension.activity;
            if (!moveLinesPlaceMTActivity.closeOrderClickedBln) {
                moveLinesPlaceMTActivity.pLeaveActivity();
                return;
            }
            moveLinesPlaceMTActivity.pDone();
        }

        if (cAppExtension.activity instanceof MoveLineTakeMTActivity) {
            MoveLineTakeMTActivity moveLineTakeMTActivity = (MoveLineTakeMTActivity)cAppExtension.activity;
            moveLineTakeMTActivity.pAcceptMove();
        }

        if (cAppExtension.activity instanceof MoveLinePlaceMTActivity) {
            MoveLinePlaceMTActivity moveLinePlaceMTActivity = (MoveLinePlaceMTActivity)cAppExtension.activity;
            moveLinePlaceMTActivity.pAcceptMove();
        }

        if (cAppExtension.activity instanceof MoveLinePlaceGeneratedActivity) {
            MoveLinePlaceGeneratedActivity moveLinePlaceGeneratedActivity = (MoveLinePlaceGeneratedActivity)cAppExtension.activity;
            moveLinePlaceGeneratedActivity.pAcceptMove();
        }

        if (cAppExtension.activity instanceof MoveorderLinesPlaceGeneratedActivity) {
            MoveorderLinesPlaceGeneratedActivity moveorderLinesPlaceGeneratedActivity = (MoveorderLinesPlaceGeneratedActivity)cAppExtension.activity;
            moveorderLinesPlaceGeneratedActivity.pDone();
        }
        if (cAppExtension.activity instanceof MoveLineItemPropertyActivity) {
            MoveLineItemPropertyActivity moveLineItemPropertyActivity = (MoveLineItemPropertyActivity)cAppExtension.activity;
            moveLineItemPropertyActivity.takeConfirmedBln = true;
            moveLineItemPropertyActivity.pHandled();
            this.dismiss();
            return;
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

        if (cAppExtension.activity instanceof  PickorderLineItemPropertyInputActvity) {
            PickorderLineItemPropertyInputActvity pickorderLineItemPropertyInputActvity = (PickorderLineItemPropertyInputActvity)cAppExtension.activity;
            pickorderLineItemPropertyInputActvity.pCancelPick();
            this.dismiss();
            return;
        }

        if (cAppExtension.activity instanceof SortorderSortActivity) {
            SortorderSortActivity sortorderSortActivity = (SortorderSortActivity)cAppExtension.activity;
            sortorderSortActivity.pCancelPick();
            this.dismiss();
            return;
        }

        if (cAppExtension.activity instanceof PickorderQCActivity) {
            PickorderQCActivity pickorderQCActivity = (PickorderQCActivity)cAppExtension.activity;
            pickorderQCActivity.pCancelQC();
            this.dismiss();
            return;
        }

        if (cAppExtension.activity instanceof PickorderPickGeneratedActivity) {
            PickorderPickGeneratedActivity pickorderPickGeneratedActivity = (PickorderPickGeneratedActivity)cAppExtension.activity;
            pickorderPickGeneratedActivity.pCancelPick();
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

        if (cAppExtension.activity instanceof  InventoryLinePropertyInputActivity) {
            InventoryLinePropertyInputActivity inventoryLinePropertyInputActivity = (InventoryLinePropertyInputActivity)cAppExtension.activity;
            inventoryLinePropertyInputActivity.pCancel();
            this.dismiss();
            return;
        }

        if (cAppExtension.activity instanceof IntakeOrderIntakeActivity) {
            IntakeOrderIntakeActivity intakeOrderIntakeActivity = (IntakeOrderIntakeActivity)cAppExtension.activity;
            intakeOrderIntakeActivity.pCancelStore();
            this.dismiss();
            return;
        }

        if (cAppExtension.activity instanceof IntakeOrderLinePropertyInputActivity) {
            IntakeOrderLinePropertyInputActivity intakeOrderLinePropertyInputActivity = (IntakeOrderLinePropertyInputActivity)cAppExtension.activity;
            intakeOrderLinePropertyInputActivity.pCancelReceive();
            this.dismiss();
            return;
        }

        if (cAppExtension.activity instanceof IntakeorderMATLinesActivity) {
            IntakeorderMATLinesActivity intakeorderMATLinesActivity = (IntakeorderMATLinesActivity)cAppExtension.activity;
            intakeorderMATLinesActivity.pLeaveActivity();
            this.dismiss();
            return;
        }

        if (cAppExtension.activity instanceof IntakeorderMASLinesActivity) {
            IntakeorderMASLinesActivity intakeorderMASLinesActivity = (IntakeorderMASLinesActivity)cAppExtension.activity;
            intakeorderMASLinesActivity.pLeaveActivity();
            this.dismiss();
            return;
        }

        if (cAppExtension.activity instanceof IntakeOrderIntakeGeneratedActivity) {
            IntakeOrderIntakeGeneratedActivity intakeOrderIntakeGeneratedActivity = (IntakeOrderIntakeGeneratedActivity)cAppExtension.activity;
            intakeOrderIntakeGeneratedActivity.pCancelStore();
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

        if (cAppExtension.activity instanceof  ReceiveorderLinePropertyInputActivity) {
            ReceiveorderLinePropertyInputActivity receiveorderLinePropertyInputActivity = (ReceiveorderLinePropertyInputActivity)cAppExtension.activity;
            receiveorderLinePropertyInputActivity.pCancelReceive();
            this.dismiss();
            return;
        }

        if (cAppExtension.activity instanceof ReturnorderDocumentActivity){
            ReturnorderDocumentActivity returnorderDocumentActivity = (ReturnorderDocumentActivity)cAppExtension.activity;
            returnorderDocumentActivity.pHandleFragmentDismissed();
            this.dismiss();
            return;
        }

        if (cAppExtension.activity instanceof  ReturnArticleDetailActivity) {
            ReturnArticleDetailActivity returnArticleDetailActivity = (ReturnArticleDetailActivity)cAppExtension.activity;
            returnArticleDetailActivity.pHandleFragmentDismissed();
            this.dismiss();
            return;
        }

        if (cAppExtension.activity instanceof PackagingActivity){
            this.dismiss();
        }

        if (cAppExtension.activity instanceof MoveLinesActivity){
            this.dismiss();
        }

        if (cAppExtension.activity instanceof  MoveLineItemPropertyActivity) {
            MoveLineItemPropertyActivity moveLineItemPropertyActivity = (MoveLineItemPropertyActivity)cAppExtension.activity;
            moveLineItemPropertyActivity.pCancel();
            this.dismiss();
            return;
        }

        if (cAppExtension.activity instanceof MoveLineTakeActivity) {
            MoveLineTakeActivity moveLineTakeActivity = (MoveLineTakeActivity)cAppExtension.activity;
            moveLineTakeActivity.pCancelMove();
            return;
        }
        if (cAppExtension.activity instanceof MoveLinesPlaceMTActivity) {
            MoveLinesPlaceMTActivity moveLinesPlaceMTActivity = (MoveLinesPlaceMTActivity)cAppExtension.activity;
            moveLinesPlaceMTActivity.closeOrderClickedBln = false;
            return;
        }


        if (cAppExtension.activity instanceof MoveLineTakeMTActivity) {
            MoveLineTakeMTActivity moveLineTakeMTActivity = (MoveLineTakeMTActivity)cAppExtension.activity;
            moveLineTakeMTActivity.pCancelMove();
            return;
        }

        if (cAppExtension.activity instanceof MoveLinePlaceMTActivity) {
            MoveLinePlaceMTActivity moveLinePlaceMTActivity = (MoveLinePlaceMTActivity)cAppExtension.activity;
            moveLinePlaceMTActivity.pCancelMove();
            return;
        }

        if (cAppExtension.activity instanceof  MoveLinePlaceGeneratedActivity) {
            MoveLinePlaceGeneratedActivity moveLinePlaceGeneratedActivity = (MoveLinePlaceGeneratedActivity)cAppExtension.activity;
            moveLinePlaceGeneratedActivity.pCancelMove();
            return;
        }

        if (cAppExtension.activity instanceof MoveorderLinesPlaceGeneratedActivity) {
            MoveorderLinesPlaceGeneratedActivity moveorderLinesPlaceGeneratedActivity = (MoveorderLinesPlaceGeneratedActivity)cAppExtension.activity;
            moveorderLinesPlaceGeneratedActivity.pLeaveActivity();
        }

    }

}
