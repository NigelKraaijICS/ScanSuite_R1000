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

    private static RecyclerView recyclerViewReturnDocumentsTotal;
    private static ImageView imageCloseOrder;

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
        ReturnDocumentsTotalFragment.mGetData();
        ReturnDocumentsToDoFragment.pGetData();
        ReturnDocumentsDoneFragment.pGetData();

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
        ReturnDocumentsTotalFragment.mGetData();
    }

    @Override
    public void mFindViews() {
        ReturnDocumentsTotalFragment.recyclerViewReturnDocumentsTotal = Objects.requireNonNull(getView()).findViewById(R.id.recyclerViewReturnDocumentsTotal);
        ReturnDocumentsTotalFragment.imageCloseOrder = getView().findViewById(R.id.imageCloseOrder);
    }

    @Override
    public void mFieldsInitialize() {

        ReturnDocumentsTotalFragment.imageCloseOrder.setVisibility(View.INVISIBLE);

        if (cReturnorder.currentReturnOrder.pGetDocumentsDoneFromDatabasObl().size() >0 && cReturnorder.currentReturnOrder.pGetDocumentsNotDoneFromDatabasObl().size() == 0 ) {
            ReturnDocumentsTotalFragment.imageCloseOrder.setVisibility(View.VISIBLE);
        }

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new cReturnorderDocumentRecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(ReturnDocumentsTotalFragment.recyclerViewReturnDocumentsTotal);


    }

    @Override
    public void mSetListeners() {
        this.mSetDoneListener();
    }

    //End Region iICSDefaultActivity defaults

    //Region Public Methods

    public static void pGetData(){
        mGetData();
    }

    //End Region Public Methods

    //Region Private Methods

    private static void mGetData() {
        mFillRecycler(cReturnorder.currentReturnOrder.pGetDocumentsTotalFromDatabasObl());
    }

    private static void mFillRecycler(List<cReturnorderDocument> pvDataObl) {

        cReturnorderDocument.getReturnorderDocumentTotalAdapter().pFillData(pvDataObl);
        recyclerViewReturnDocumentsTotal.setHasFixedSize(false);
        recyclerViewReturnDocumentsTotal.setAdapter(cReturnorderDocument.getReturnorderDocumentTotalAdapter());
        recyclerViewReturnDocumentsTotal.setLayoutManager(new LinearLayoutManager(cAppExtension.context));
        recyclerViewReturnDocumentsTotal.setVisibility(View.VISIBLE);
        ReturnorderDocumentsActivity.pChangeTabCounterText(cText.pIntToStringStr(cReturnorder.currentReturnOrder.pGetDocumentsTotalFromDatabasObl().size()));

    }

    private void mSetDoneListener() {
        ReturnDocumentsTotalFragment.imageCloseOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReturnorderDocumentsActivity.pHandleOrderCloseClick();
            }
        });
    }

    //End Region Private Methods

}
