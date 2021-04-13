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
import nl.icsvertex.scansuite.Activities.Intake.IntakeorderMASLinesActivity;
import nl.icsvertex.scansuite.Activities.Intake.IntakeorderMATLinesActivity;
import nl.icsvertex.scansuite.Activities.Inventory.InventoryorderBinActivity;
import nl.icsvertex.scansuite.Activities.Inventory.InventoryorderBinsActivity;
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
import nl.icsvertex.scansuite.Activities.Returns.ReturnArticleDetailActivity;
import nl.icsvertex.scansuite.Activities.Returns.ReturnorderDocumentActivity;
import nl.icsvertex.scansuite.Activities.Returns.ReturnorderDocumentsActivity;
import nl.icsvertex.scansuite.Activities.Sort.SortorderSortActivity;
import nl.icsvertex.scansuite.R;

public class AcceptRejectPropertyFragment  extends DialogFragment implements iICSDefaultFragment {

    //Region Public Properties

    //End Region Public Properties

    //Region Private Properties

    private Button cancelButton;
    private ImageView acceptImageView;
    private  ImageView rejectImageView;
    private TextView acceptRejectHeader;
    private  TextView acceptRejectText;
    private  TextView textReject;
    private  TextView textAccept;

    private final String  titleStr;
    private final String  messageStr;
    private final String  acceptStr;
    private final String rejectStr;

    //End Region Private Properties

    //Region Constructor
    public AcceptRejectPropertyFragment(String pvTitleStr, String pvMessageStr, String pvRejectStr, String pvAcceptStr) {
        this.titleStr = pvTitleStr;
        this.messageStr = pvMessageStr;

        this.acceptStr = pvAcceptStr;
        this.rejectStr = pvRejectStr;
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

        if (cAppExtension.activity instanceof  PickorderLineItemPropertyInputActvity) {
            PickorderLineItemPropertyInputActvity pickorderLineItemPropertyInputActvity = (PickorderLineItemPropertyInputActvity)cAppExtension.activity;
            pickorderLineItemPropertyInputActvity.amountHandledBln = true;
            pickorderLineItemPropertyInputActvity.pResetTab(false);
            this.dismiss();
            return;
        }
    }

    private void mReject() {

        if (cAppExtension.activity instanceof  PickorderLineItemPropertyInputActvity) {
            PickorderLineItemPropertyInputActvity pickorderLineItemPropertyInputActvity = (PickorderLineItemPropertyInputActvity)cAppExtension.activity;
            pickorderLineItemPropertyInputActvity.amountHandledBln = false;
            pickorderLineItemPropertyInputActvity.pResetTab(true);
            this.dismiss();
            return;
        }
    }
}
