package nl.icsvertex.scansuite.Fragments.Returns;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
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
import SSU_WHS.General.cPublicDefinitions;
import SSU_WHS.Return.ReturnOrder.cReturnorder;
import SSU_WHS.Return.ReturnorderDocument.cReturnorderDocument;
import SSU_WHS.Return.ReturnorderDocument.cReturnorderDocumentAdapter;
import SSU_WHS.Return.ReturnorderDocument.cReturnorderDocumentRecyclerItemTouchHelper;
import nl.icsvertex.scansuite.Activities.Returns.ReturnorderDocumentsActivity;
import nl.icsvertex.scansuite.Fragments.Dialogs.AddDocumentFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.NothingHereFragment;
import nl.icsvertex.scansuite.R;

public class ReturnDocumentsToDoFragment extends Fragment implements iICSDefaultFragment, cReturnorderDocumentRecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    //Region Public Properties

    //End Region Public Properties

    //Region Private Properties

    private ImageView imageAddDocument;
    private RecyclerView recyclerViewReturnDocumentsToDo;

    private TextView quickhelpText;
    private ImageView quickhelpIcon;
    private ConstraintLayout quickhelpContainer;

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
        return pvInflater.inflate(R.layout.fragment_returnorder_documents_todo, pvContainer, false);
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
        ReturnorderDocumentsActivity.currentDocumentFragment = this;
        this.mGetData();
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

        if (getView() != null) {
            this.imageAddDocument = getView().findViewById(R.id.imageAddDocument);
            this.recyclerViewReturnDocumentsToDo = getView().findViewById(R.id.recyclerViewReturnDocumentsToDo);
            this.quickhelpText = getView().findViewById(R.id.quickhelpText);
            this.quickhelpContainer = getView().findViewById(R.id.quickhelpContainer);
            this.quickhelpIcon = getView().findViewById(R.id.quickhelpIcon);
        }

    }

    @Override
    public void mFieldsInitialize() {

        this.quickhelpText.setText(R.string.scan_or_select_document);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new cReturnorderDocumentRecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(this.recyclerViewReturnDocumentsToDo);

        if (!cReturnorder.currentReturnOrder.isGeneratedBln()) {
            this.imageAddDocument.setVisibility(View.INVISIBLE);
        }
        else {
            if (!cReturnorder.currentReturnOrder.getRetourMultiDocumentBln()) {
                this.imageAddDocument.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public void mSetListeners() {
        this.mSetQuickHelpListener();
        this.mSetAddDocumentListener();
    }

    //End Region iICSDefaultActivity defaults

    //Region Public Methods

    //End Region Public Methods

    //Region Private Methods

    private void mGetData() {
        this.mFillRecycler(cReturnorder.currentReturnOrder.pGetDocumentsNotDoneFromDatabasObl());
    }

    private void mSetAddDocumentListener() {
        this.imageAddDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mShowAddDocumentFragment();
            }
        });
    }

    private  void mFillRecycler(List<cReturnorderDocument> pvDataObl) {

        if (pvDataObl.size() == 0) {
            this.mNoLinesAvailable(true);
            return;
        }

        this.mNoLinesAvailable(false);

        this.getReturnorderDocumentAdapter().pFillData(pvDataObl);
        this.recyclerViewReturnDocumentsToDo.setHasFixedSize(false);
        this.recyclerViewReturnDocumentsToDo.setAdapter(this.getReturnorderDocumentAdapter());
        this.recyclerViewReturnDocumentsToDo.setLayoutManager(new LinearLayoutManager(cAppExtension.context));
        this.recyclerViewReturnDocumentsToDo.setVisibility(View.VISIBLE);

        if (cAppExtension.activity instanceof  ReturnorderDocumentsActivity) {
            ReturnorderDocumentsActivity returnorderDocumentsActivity = (ReturnorderDocumentsActivity)cAppExtension.activity;
            returnorderDocumentsActivity.pChangeTabCounterText(cText.pIntToStringStr(cReturnorder.currentReturnOrder.pGetDocumentsNotDoneFromDatabasObl().size()) + "/" + cText.pIntToStringStr(cReturnorder.currentReturnOrder.pGetDocumentsTotalFromDatabasObl().size()));
        }

    }

    private  void mNoLinesAvailable(Boolean pvEnabledBln) {

        if (ReturnorderDocumentsActivity.currentDocumentFragment instanceof ReturnDocumentsToDoFragment) {
            //Close no orders fragment if needed
            if (!pvEnabledBln) {
                List<Fragment> fragments = cAppExtension.fragmentManager.getFragments();
                for (Fragment fragment : fragments) {
                    if (fragment instanceof NothingHereFragment) {
                        FragmentTransaction fragmentTransaction = cAppExtension.fragmentManager.beginTransaction();
                        fragmentTransaction.remove(fragment);
                        fragmentTransaction.commit();
                    }
                }
                return;
            }

            //Hide the recycler view
            this.recyclerViewReturnDocumentsToDo.setVisibility(View.INVISIBLE);

            //Hide location button and clear text

            //Show nothing there fragment
            FragmentTransaction fragmentTransaction = cAppExtension.fragmentManager.beginTransaction();
            NothingHereFragment fragment = new NothingHereFragment();
            fragmentTransaction.replace(R.id.fragmentReturnorderDocumentsToDo, fragment);
            fragmentTransaction.commit();

            //Change tabcounter text
            if (cAppExtension.activity instanceof  ReturnorderDocumentsActivity) {
                ReturnorderDocumentsActivity returnorderDocumentsActivity = (ReturnorderDocumentsActivity) cAppExtension.activity;
                returnorderDocumentsActivity.pChangeTabCounterText(cText.pIntToStringStr(cReturnorder.currentReturnOrder.pGetDocumentsNotDoneFromDatabasObl().size()) + "/" + cText.pIntToStringStr(cReturnorder.currentReturnOrder.pGetDocumentsTotalFromDatabasObl().size()));
            }
        }
    }

    private void mShowAddDocumentFragment(){
        AddDocumentFragment addDocumentFragment = new AddDocumentFragment();
        addDocumentFragment.setCancelable(true);
        addDocumentFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.ADDDOCUMENT_TAG);

    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder pvViewHolder, int pvDirectionInt, int pvPositionInt) {
        if (!(pvViewHolder instanceof cReturnorderDocumentAdapter.ReturnorderDocumentViewHolder)) {
            return;
        }

        if (cReturnorderDocument.returnorderDocumentsTodoObl.size() <= 0) {
            return;
        }

        cReturnorderDocument.currentReturnOrderDocument = cReturnorderDocument.returnorderDocumentsTodoObl.get(pvPositionInt);

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

    private void mSetQuickHelpListener() {
        this.quickhelpContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cUserInterface.pDoRotate(quickhelpIcon, 0);
                if (quickhelpText.getVisibility() == View.VISIBLE) {
                    quickhelpText.setVisibility(View.GONE);
                }
                else {
                    quickhelpText.setVisibility(View.VISIBLE);
                }
            }
        });
    }
    //End Region Private Methods
}






