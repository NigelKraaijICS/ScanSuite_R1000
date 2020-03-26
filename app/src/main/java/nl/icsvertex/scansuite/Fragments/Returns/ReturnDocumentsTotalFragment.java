package nl.icsvertex.scansuite.Fragments.Returns;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Objects;

import ICS.Interfaces.iICSDefaultFragment;
import ICS.Utils.cResult;
import ICS.Utils.cText;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Return.ReturnOrder.cReturnorder;
import SSU_WHS.Return.ReturnorderDocument.cReturnorderDocument;
import SSU_WHS.Return.ReturnorderDocument.cReturnorderDocumentAdapter;
import SSU_WHS.Return.ReturnorderDocument.cReturnorderDocumentRecyclerItemTouchHelper;
import nl.icsvertex.scansuite.Activities.Returns.ReturnorderDocumentsActivity;
import nl.icsvertex.scansuite.R;

public class ReturnDocumentsTotalFragment extends Fragment implements iICSDefaultFragment,   cReturnorderDocumentRecyclerItemTouchHelper.RecyclerItemTouchHelperListener  {

    //Region Public Properties

    //End Region Public Properties

    //Region Private Properties

    private  RecyclerView recyclerViewReturnDocumentsTotal;
    private  ImageView imageCloseOrder;

    private  cReturnorderDocumentAdapter returnorderDocumentAdapter;
    private  cReturnorderDocumentAdapter getReturnorderDocumentAdapter(){
        if (this.returnorderDocumentAdapter == null) {
            this.returnorderDocumentAdapter = new cReturnorderDocumentAdapter();
        }

        return  this.returnorderDocumentAdapter;
    }

    //End Region Private Properties

    //Region Default Methods

    @Override
    public View onCreateView(LayoutInflater pvInflater, ViewGroup pvContainer,
                             Bundle pvSavedInstanceState) {
        // Inflate the layout for this fragment
        return pvInflater.inflate(R.layout.fragment_returnorder_documents_total, pvContainer, false);
    }

    @Override
    public void onViewCreated(@NonNull View pvView, @Nullable Bundle pvSavedInstanceState) {
        this.mFragmentInitialize();
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
        ReturnorderDocumentsActivity.currentDocumentFragment = this;
        this.mFragmentInitialize();
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder pvViewHolder, int pvDirectionInt, int pvPositionInt) {
        if (!(pvViewHolder instanceof cReturnorderDocumentAdapter.ReturnorderDocumentViewHolder)) {
            return;
        }

        if (cReturnorderDocument.returnorderDocumentsTotalObl.size() <= 0 ){
            return;
        }

        cReturnorderDocument.currentReturnOrderDocument = cReturnorderDocument.returnorderDocumentsTotalObl.get(pvPositionInt);

        //Remove the enviroment
        this.mRemoveAdapterFromFragment();
        this.mGetData();
    }

    private void mRemoveAdapterFromFragment(){

        //remove the item from recyclerview
        if (!cReturnorder.currentReturnOrder.isGeneratedBln()){
            cResult hulpRst = cReturnorder.currentReturnOrder.pResetLinesToZeroFromDocument();
            if (! hulpRst.resultBln) {
                cUserInterface.pDoExplodingScreen(hulpRst.messagesStr(),"",true,true);
            }
        }

        else {
            cResult hulpRst = cReturnorder.currentReturnOrder.pDeleteDetailsFromDocument();
            if (! hulpRst.resultBln) {
                cUserInterface.pDoExplodingScreen(hulpRst.messagesStr(),"",true,true);
            }
        }
    }
    //End Region Default Methods

    //Region iICSDefaultActivity defaults

    @Override
    public void mFragmentInitialize() {

        this.mFindViews();
        this.mFieldsInitialize();
        this.mSetListeners();
        this.mGetData();
    }

    @Override
    public void mFindViews() {
        this.recyclerViewReturnDocumentsTotal = Objects.requireNonNull(getView()).findViewById(R.id.recyclerViewReturnDocumentsTotal);
        this.imageCloseOrder = getView().findViewById(R.id.imageCloseOrder);
    }

    @Override
    public void mFieldsInitialize() {

        this.imageCloseOrder.setVisibility(View.INVISIBLE);

        if (cReturnorder.currentReturnOrder.pGetDocumentsDoneFromDatabasObl().size() >0 && cReturnorder.currentReturnOrder.pGetDocumentsNotDoneFromDatabasObl().size() == 0 ) {
            this.imageCloseOrder.setVisibility(View.VISIBLE);
        }

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new cReturnorderDocumentRecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(this.recyclerViewReturnDocumentsTotal);


    }

    @Override
    public void mSetListeners() {
        this.mSetDoneListener();
    }

    //End Region iICSDefaultActivity defaults

    //Region Public Methods

    //End Region Public Methods

    //Region Private Methods

    private  void mGetData() {
       this.mFillRecycler(cReturnorder.currentReturnOrder.pGetDocumentsTotalFromDatabasObl());
    }

    private  void mFillRecycler(List<cReturnorderDocument> pvDataObl) {
        this.getReturnorderDocumentAdapter().pFillData(pvDataObl);
        this.recyclerViewReturnDocumentsTotal.setHasFixedSize(false);
        this.recyclerViewReturnDocumentsTotal.setAdapter(this.getReturnorderDocumentAdapter());
        this.recyclerViewReturnDocumentsTotal.setLayoutManager(new LinearLayoutManager(cAppExtension.context));
        this.recyclerViewReturnDocumentsTotal.setVisibility(View.VISIBLE);

        if (cAppExtension.activity instanceof ReturnorderDocumentsActivity ) {
            ReturnorderDocumentsActivity returnorderDocumentsActivity = (ReturnorderDocumentsActivity)cAppExtension.activity;
            returnorderDocumentsActivity.pChangeTabCounterText(cText.pIntToStringStr(cReturnorder.currentReturnOrder.pGetDocumentsTotalFromDatabasObl().size()));
        }
    }

    private void mSetDoneListener() {
        this.imageCloseOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cAppExtension.activity instanceof ReturnorderDocumentsActivity ) {
                    ReturnorderDocumentsActivity returnorderDocumentsActivity = (ReturnorderDocumentsActivity) cAppExtension.activity;
                    returnorderDocumentsActivity.pHandleOrderCloseClick();
                }
            }
        });
    }

    //End Region Private Methods

}
