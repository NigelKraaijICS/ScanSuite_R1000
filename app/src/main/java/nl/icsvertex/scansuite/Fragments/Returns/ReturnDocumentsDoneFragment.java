package nl.icsvertex.scansuite.Fragments.Returns;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

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
import nl.icsvertex.scansuite.Fragments.Dialogs.NothingHereFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.SendOrderFragment;
import nl.icsvertex.scansuite.R;

public class ReturnDocumentsDoneFragment extends Fragment implements iICSDefaultFragment,  cReturnorderDocumentRecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    //Region Public Properties

    //End Region Public Properties

    //Region Private Properties

    private static RecyclerView recyclerViewReturnorderDocumentsDone;
    private static ImageView imageCloseOrder;

    //End Region Private Properties

    //Region Default Methods

    @Override
    public View onCreateView(LayoutInflater pvInflater, ViewGroup pvContainer,
                             Bundle pvSavedInstanceState) {
        // Inflate the layout for this fragment
        return pvInflater.inflate(R.layout.fragment_returnorder_documents_done, pvContainer, false);


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

        if (cReturnorderDocument.returnorderDocumentsDoneObl.size() <= 0) {
            return;
        }

        cReturnorderDocument.currentReturnOrderDocument = cReturnorderDocument.returnorderDocumentsDoneObl.get(pvPositionInt);

        //Remove the enviroment
        this.mRemoveAdapterFromFragment();
        ReturnDocumentsDoneFragment.mGetData();
        ReturnDocumentsToDoFragment.pGetData();
        ReturnDocumentsTotalFragment.pGetData();
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

        //Renew data, so only current lines are shown
    }

    //End Region Default Methods

    //Region iICSDefaultActivity defaults

    @Override
    public void mFragmentInitialize() {
        this.mFindViews();
        this.mFieldsInitialize();
        this.mSetListeners();
        ReturnDocumentsDoneFragment.mGetData();
    }

    @Override
    public void mFindViews() {

        if (getView() != null) {
            ReturnDocumentsDoneFragment.recyclerViewReturnorderDocumentsDone = getView().findViewById(R.id.recyclerViewReturnDocumentsDone);
            ReturnDocumentsDoneFragment.imageCloseOrder = getView().findViewById(R.id.imageCloseOrder);
        }


    }

    @Override
    public void mFieldsInitialize() {

        ReturnDocumentsDoneFragment.imageCloseOrder.setVisibility(View.INVISIBLE);

        if (cReturnorder.currentReturnOrder.pGetDocumentsDoneFromDatabasObl().size() >0 && cReturnorder.currentReturnOrder.pGetDocumentsNotDoneFromDatabasObl().size() == 0 ) {
            ReturnDocumentsDoneFragment.imageCloseOrder.setVisibility(View.VISIBLE);
        }

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new cReturnorderDocumentRecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(ReturnDocumentsDoneFragment.recyclerViewReturnorderDocumentsDone);


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
        ReturnDocumentsDoneFragment.mFillRecycler(cReturnorder.currentReturnOrder.pGetDocumentsDoneFromDatabasObl());
    }

    private static void mFillRecycler(List<cReturnorderDocument> pvDataObl) {

        if (pvDataObl.size() == 0) {
            ReturnDocumentsDoneFragment.mNoLinesAvailable(true);
            return;
        }

        ReturnDocumentsDoneFragment.mNoLinesAvailable(false);

        cReturnorderDocument.getReturnorderDocumentDoneAdapter().pFillData(pvDataObl);
        recyclerViewReturnorderDocumentsDone.setHasFixedSize(false);
        recyclerViewReturnorderDocumentsDone.setAdapter(cReturnorderDocument.getReturnorderDocumentDoneAdapter());
        recyclerViewReturnorderDocumentsDone.setLayoutManager(new LinearLayoutManager(cAppExtension.context));
        recyclerViewReturnorderDocumentsDone.setVisibility(View.VISIBLE);

        ReturnorderDocumentsActivity.pChangeTabCounterText(cText.pIntToStringStr(cReturnorder.currentReturnOrder.pGetDocumentsDoneFromDatabasObl().size()) + "/" + cText.pIntToStringStr(cReturnorder.currentReturnOrder.pGetDocumentsTotalFromDatabasObl().size()));

    }

    private static void mNoLinesAvailable(Boolean pvEnabledBln) {

        if (ReturnorderDocumentsActivity.currentDocumentFragment instanceof ReturnDocumentsDoneFragment) {

            if (!pvEnabledBln) {

                List<Fragment> fragments = cAppExtension.fragmentManager.getFragments();
                for (Fragment fragment : fragments) {
                    if (fragment instanceof NothingHereFragment || fragment instanceof SendOrderFragment) {
                        FragmentTransaction fragmentTransaction = cAppExtension.fragmentManager.beginTransaction();
                        fragmentTransaction.remove(fragment);
                        fragmentTransaction.commit();
                    }
                }
                return;
            }

            ReturnDocumentsDoneFragment.recyclerViewReturnorderDocumentsDone.setVisibility(View.INVISIBLE);
            FragmentTransaction fragmentTransaction = cAppExtension.fragmentManager.beginTransaction();
            NothingHereFragment fragment = new NothingHereFragment();
            fragmentTransaction.replace(R.id.fragmentReturnorderDocumentsDone, fragment);
            fragmentTransaction.commit();

            ReturnorderDocumentsActivity.pChangeTabCounterText(cText.pIntToStringStr(cReturnorder.currentReturnOrder.pGetDocumentsDoneFromDatabasObl().size()) + "/" + cText.pIntToStringStr(cReturnorder.currentReturnOrder.pGetDocumentsTotalFromDatabasObl().size()));

        }
    }

    private void mSetDoneListener() {
        ReturnDocumentsDoneFragment.imageCloseOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReturnorderDocumentsActivity.pHandleOrderCloseClick();
            }
        });
    }

       //End Region Private Methods

}
