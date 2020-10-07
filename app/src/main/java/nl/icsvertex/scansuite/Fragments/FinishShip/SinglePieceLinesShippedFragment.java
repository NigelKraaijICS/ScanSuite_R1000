package nl.icsvertex.scansuite.Fragments.FinishShip;

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

import ICS.Interfaces.iICSDefaultFragment;
import ICS.Utils.cText;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Picken.FinishSinglePieceLine.cPickorderLineFinishSinglePiece;
import SSU_WHS.Picken.FinishSinglePieceLine.cPickorderLineFinishSinglePieceAdapter;
import nl.icsvertex.scansuite.Activities.FinishShip.FinishShipLinesActivity;
import nl.icsvertex.scansuite.Fragments.Dialogs.NothingHereFragment;
import nl.icsvertex.scansuite.R;


public class SinglePieceLinesShippedFragment extends Fragment implements iICSDefaultFragment {

    //Region Public Properties

    //End Region Public Properties

    //Region Private Properties
   private RecyclerView recyclerViewSinglePieceLinesShipped;

   private cPickorderLineFinishSinglePieceAdapter pickorderLineFinishSinglePieceAdapter;
   private  cPickorderLineFinishSinglePieceAdapter getPickorderLineFinishSinglePieceAdapter(){
        if (this.pickorderLineFinishSinglePieceAdapter == null) {
            this.pickorderLineFinishSinglePieceAdapter = new cPickorderLineFinishSinglePieceAdapter();
        }

        return  this.pickorderLineFinishSinglePieceAdapter;
    }

    //End Region Private Properties


    //Region Constructor
    public SinglePieceLinesShippedFragment() {
        // Required empty public constructor
    }
    //End Region Constructor



    //Region Default Methods

    @Override
    public View onCreateView(LayoutInflater pvLayoutInflater, ViewGroup pvViewGroup,
                             Bundle pvSavedInstanceState) {
        // Inflate the layout for this fragment
        return pvLayoutInflater.inflate(R.layout.fragment_singlepiece_lines_shipped, pvViewGroup, false);
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
        FinishShipLinesActivity.currentLineFragment = this;
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
            this.recyclerViewSinglePieceLinesShipped = getView().findViewById(R.id.recyclerViewSinglePieceLinesShipped);
        }


    }


    @Override
    public void mFieldsInitialize() {
        this.recyclerViewSinglePieceLinesShipped.setVisibility(View.VISIBLE);
    }

    @Override
    public void mSetListeners() {

    }

    //End Region iICSDefaultFragment defaults

    //Region Public Methods

    //End Region Public Methods


    //Region Private Methods


    private void mGetData() {
        this.mFillRecycler();
    }

    private void mFillRecycler() {

        if (cPickorderLineFinishSinglePiece.linesShippedObl().size() == 0) {
            this.mNoLinesAvailable();
            return;
        }

        this.getPickorderLineFinishSinglePieceAdapter().pFillData(cPickorderLineFinishSinglePiece.linesShippedObl());
        this.recyclerViewSinglePieceLinesShipped.setHasFixedSize(false);
        this.recyclerViewSinglePieceLinesShipped.setAdapter(this.getPickorderLineFinishSinglePieceAdapter());
        this.recyclerViewSinglePieceLinesShipped.setLayoutManager(new LinearLayoutManager(cAppExtension.context));

        if (cAppExtension.activity instanceof  FinishShipLinesActivity) {
            FinishShipLinesActivity finishShipLinesActivity = (FinishShipLinesActivity)cAppExtension.activity;
            finishShipLinesActivity.pChangeTabCounterText(cText.pIntToStringStr(cPickorderLineFinishSinglePiece.linesShippedObl().size()) + "/" + cText.pIntToStringStr(cPickorderLineFinishSinglePiece.allFinishSinglePieceLinesObl.size()));
        }

    }

    private void mNoLinesAvailable() {

        if (FinishShipLinesActivity.currentLineFragment == this) {
            //Close no linesInt fragment if needed


            //Hide the recycler view
            this.recyclerViewSinglePieceLinesShipped.setVisibility(View.INVISIBLE);

            //Show nothing there fragment
            FragmentTransaction fragmentTransaction = cAppExtension.fragmentManager.beginTransaction();
            NothingHereFragment fragment = new NothingHereFragment();
            fragmentTransaction.replace(R.id.fragmentSinglePieceLinesShipped, fragment);
            fragmentTransaction.commit();

            //Change tabcounter text
            if (cAppExtension.activity instanceof  FinishShipLinesActivity) {
                FinishShipLinesActivity finishShipLinesActivity = (FinishShipLinesActivity) cAppExtension.activity;
                finishShipLinesActivity.pChangeTabCounterText(cText.pIntToStringStr(cPickorderLineFinishSinglePiece.linesShippedObl().size()) + "/" + cText.pIntToStringStr(cPickorderLineFinishSinglePiece.allFinishSinglePieceLinesObl.size()));
            }
        }
    }

    //End Region Private Methods

}
