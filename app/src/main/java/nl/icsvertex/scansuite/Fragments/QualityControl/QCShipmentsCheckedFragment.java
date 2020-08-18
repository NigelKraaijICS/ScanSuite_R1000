package nl.icsvertex.scansuite.Fragments.QualityControl;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ICS.Interfaces.iICSDefaultFragment;
import ICS.Utils.cText;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Picken.PickorderLines.cPickorderLine;
import SSU_WHS.Picken.PickorderLines.cPickorderLineAdapter;
import SSU_WHS.Picken.Shipment.cShipment;
import nl.icsvertex.scansuite.Activities.QualityControl.QualityControlLinesActivity;
import nl.icsvertex.scansuite.Activities.QualityControl.QualityControlShipmentsActivity;
import nl.icsvertex.scansuite.Fragments.Dialogs.NothingHereFragment;
import nl.icsvertex.scansuite.R;


public class QCShipmentsCheckedFragment extends Fragment implements iICSDefaultFragment {

    //Region Public Properties

    //End Region Public Properties

    //Region Private Properties
    private RecyclerView recyclerViewQCShipmentsChecked;

    private cPickorderLineAdapter pickorderLineAdapter;
    private  cPickorderLineAdapter getPickorderLineAdapter(){
        if (this.pickorderLineAdapter == null) {
            this.pickorderLineAdapter = new cPickorderLineAdapter();
        }

        return  this.pickorderLineAdapter;
    }
    //End Region Private Properties


    //Region Constructor
    public QCShipmentsCheckedFragment() {
        // Required empty public constructor
    }
    //End Region Constructor



    //Region Default Methods

    @Override
    public View onCreateView(LayoutInflater pvLayoutInflater, ViewGroup pvViewGroup,
                             Bundle pvSavedInstanceState) {
        // Inflate the layout for this fragment
        return pvLayoutInflater.inflate(R.layout.fragment_qc_shipments_checked, pvViewGroup, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        cUserInterface.pEnableScanner();
        QualityControlLinesActivity.currentLineFragment = this;
        this.mFragmentInitialize();

    }

    //End Region Default Methods

    //Region iICSDefaultFragment defaults


    @Override
    public void mFragmentInitialize() {
        this.mFindViews();
        this.mFieldsInitialize();
        this.mSetListeners();
        this.mGetData();
        cUserInterface.pEnableScanner();
    }

    @Override
    public void mFindViews() {

        if (getView() != null) {
            this.recyclerViewQCShipmentsChecked = getView().findViewById(R.id.recyclerViewQCShipmentsChecked);
        }


    }

    @Override
    public void mFieldsInitialize() {
        this.recyclerViewQCShipmentsChecked.setVisibility(View.VISIBLE);
    }

    @Override
    public void mSetListeners() {

    }

    //End Region iICSDefaultFragment defaults

    //Region Public Methods

    //End Region Public Methods

    //Region Private Methods

    private void mGetData() {
        this.mFillRecycler(cShipment.currentShipment.QCLinesCheckedObl());
    }

    private void mFillRecycler(List<cPickorderLine> pvDataObl) {

        if (pvDataObl.size() == 0) {
            this.mNoLinesAvailable();
            return;
        }

        this.getPickorderLineAdapter().pFillData(pvDataObl);
        this.recyclerViewQCShipmentsChecked.setHasFixedSize(false);
        this.recyclerViewQCShipmentsChecked.setAdapter(this.getPickorderLineAdapter());
        this.recyclerViewQCShipmentsChecked.setLayoutManager(new LinearLayoutManager(cAppExtension.context));

        if (cAppExtension.activity instanceof QualityControlShipmentsActivity) {
            QualityControlShipmentsActivity qualityControlShipmentsActivity = (QualityControlShipmentsActivity)cAppExtension.activity;
            qualityControlShipmentsActivity.pChangeTabCounterText(cText.pIntToStringStr(cShipment.currentShipment.QCLinesCheckedObl().size()) + "/" + cText.pIntToStringStr(cShipment.currentShipment.QCLinesObl.size()));
        }

    }

    private void mNoLinesAvailable() {

        if (QualityControlShipmentsActivity.currentShipmentFragment == this) {

            //Hide the recycler view
            this.recyclerViewQCShipmentsChecked.setVisibility(View.INVISIBLE);

            //Show nothing there fragment
            FragmentTransaction fragmentTransaction = cAppExtension.fragmentManager.beginTransaction();
            NothingHereFragment fragment = new NothingHereFragment();
            fragmentTransaction.replace(R.id.fragmentQCShipmentsChecked, fragment);
            fragmentTransaction.commit();

            //Change tabcounter text
            if (cAppExtension.activity instanceof QualityControlShipmentsActivity) {
                QualityControlShipmentsActivity qualityControlShipmentsActivity = (QualityControlShipmentsActivity)cAppExtension.activity;
                qualityControlShipmentsActivity.pChangeTabCounterText(cText.pIntToStringStr(cShipment.currentShipment.QCLinesCheckedObl().size()) + "/" + cText.pIntToStringStr(cShipment.currentShipment.QCLinesObl.size()));
            }

        }
    }

    //End Region Private Methods

}
