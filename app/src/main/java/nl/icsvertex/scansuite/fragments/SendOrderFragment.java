package nl.icsvertex.scansuite.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ICS.Interfaces.iICSDefaultFragment;
import ICS.Utils.cUserInterface;
import nl.icsvertex.scansuite.R;
import nl.icsvertex.scansuite.activities.pick.PickorderLinesActivity;
import nl.icsvertex.scansuite.activities.pick.PickorderSelectActivity;
import nl.icsvertex.scansuite.cAppExtension;


public class SendOrderFragment extends Fragment implements iICSDefaultFragment {

    //Region Public Properties


    //End Region Public Properties

    //Region Private Properties
    ImageView imageSendOrder;
    ImageButton buttonSendOrder;
    //End Region Private Properties

    //Region Constructor
    public SendOrderFragment() {
        // Required empty public constructor
    }
    //End Region Constructor

    //Region Default Methods

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_send_order, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        this.mFragmentInitialize();
    }

    //End Region Default Methods

    @Override
    public void mFragmentInitialize() {

        this.mFindViews();
        this.mSetViewModels();
        this.mFieldsInitialize();
        this.mSetListeners();
    }

    @Override
    public void mFindViews() {
        this.imageSendOrder = getView().findViewById(R.id.imageSendOrder);
        this.buttonSendOrder = getView().findViewById(R.id.buttonSendOrder);
    }


    @Override
    public void mSetViewModels() {

    }

    @Override
    public void mFieldsInitialize() {


    }

    @Override
    public void mSetListeners() {

        this.buttonSendOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cUserInterface.pDoRotate(buttonSendOrder, 2);
                if (cAppExtension.activity instanceof PickorderLinesActivity) {
                    PickorderLinesActivity.pPickingDone("");
                    return;
                }
            }
        });

    }
}

