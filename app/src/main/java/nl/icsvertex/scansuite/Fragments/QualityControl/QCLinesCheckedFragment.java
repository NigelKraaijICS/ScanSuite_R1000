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
import SSU_WHS.Picken.PickorderLinePackAndShip.cPickorderLinePackAndShip;
import SSU_WHS.Picken.PickorderLinePackAndShip.cPickorderLinePackAndShipAdapter;
import SSU_WHS.Picken.Shipment.cShipment;
import nl.icsvertex.scansuite.Activities.QualityControl.QualityControlLinesActivity;
import nl.icsvertex.scansuite.Fragments.Dialogs.NothingHereFragment;
import nl.icsvertex.scansuite.R;


public class QCLinesCheckedFragment extends Fragment implements iICSDefaultFragment {

    //Region Public Properties

    //End Region Public Properties

    //Region Private Properties
    private RecyclerView recyclerViewQCLinesChecked;

    private cPickorderLinePackAndShipAdapter pickorderLinePackAndShipAdapter;
    private  cPickorderLinePackAndShipAdapter getPickorderLinePackAndShipAdapter(){
        if (this.pickorderLinePackAndShipAdapter == null) {
            this.pickorderLinePackAndShipAdapter = new cPickorderLinePackAndShipAdapter();
        }

        return  this.pickorderLinePackAndShipAdapter;
    }

    //End Region Private Properties


    //Region Constructor
    public QCLinesCheckedFragment() {
        // Required empty public constructor
    }
    //End Region Constructor



    //Region Default Methods

    @Override
    public View onCreateView(LayoutInflater pvLayoutInflater, ViewGroup pvViewGroup,
                             Bundle pvSavedInstanceState) {
        // Inflate the layout for this fragment
        return pvLayoutInflater.inflate(R.layout.fragment_qc_lines_checked, pvViewGroup, false);
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
            this.recyclerViewQCLinesChecked = getView().findViewById(R.id.recyclerViewQCLinesChecked);
        }


    }

    @Override
    public void mFieldsInitialize() {
        this.recyclerViewQCLinesChecked.setVisibility(View.VISIBLE);
    }

    @Override
    public void mSetListeners() {

    }

    //End Region iICSDefaultFragment defaults

    //Region Public Methods

    //End Region Public Methods


    //Region Private Methods



    private void mGetData() {
        this.mFillRecycler(cShipment.currentShipment.linesCheckedObl());
    }

    private void mFillRecycler(List<cPickorderLinePackAndShip> pvDataObl) {

        if (pvDataObl.size() == 0) {
            this.mNoLinesAvailable();
            return;
        }

        this.getPickorderLinePackAndShipAdapter().pFillData(pvDataObl);
        this.recyclerViewQCLinesChecked.setHasFixedSize(false);
        this.recyclerViewQCLinesChecked.setAdapter(this.getPickorderLinePackAndShipAdapter());
        this.recyclerViewQCLinesChecked.setLayoutManager(new LinearLayoutManager(cAppExtension.context));


        if (cAppExtension.activity instanceof QualityControlLinesActivity) {
            QualityControlLinesActivity qualityControlLinesActivity = (QualityControlLinesActivity)cAppExtension.activity;
            qualityControlLinesActivity.pChangeTabCounterText(cText.pIntToStringStr(cShipment.currentShipment.linesCheckedObl().size()) + "/" + cText.pIntToStringStr(cShipment.currentShipment.packAndShipLineObl.size()));
        }

    }

    private void mNoLinesAvailable() {


        if (QualityControlLinesActivity.currentLineFragment == this) {
            //Close no linesInt fragment if needed


            //Hide the recycler view
            this.recyclerViewQCLinesChecked.setVisibility(View.INVISIBLE);

            //Show nothing there fragment
            FragmentTransaction fragmentTransaction = cAppExtension.fragmentManager.beginTransaction();
            NothingHereFragment fragment = new NothingHereFragment();
            fragmentTransaction.replace(R.id.fragmentQCLinesChecked, fragment);
            fragmentTransaction.commit();

            //Change tabcounter text
            if (cAppExtension.activity instanceof QualityControlLinesActivity) {
                QualityControlLinesActivity qualityControlLinesActivity = (QualityControlLinesActivity)cAppExtension.activity;
                qualityControlLinesActivity.pChangeTabCounterText(cText.pIntToStringStr(cShipment.currentShipment.linesCheckedObl().size()) + "/" + cText.pIntToStringStr(cShipment.currentShipment.packAndShipLineObl.size()));
            }

        }
    }

    //End Region Private Methods

}
