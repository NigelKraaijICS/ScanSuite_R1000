package nl.icsvertex.scansuite.Fragments.Pick;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ICS.Interfaces.iICSDefaultFragment;
import ICS.Utils.cText;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Picken.PickorderLines.cPickorderLine;
import SSU_WHS.Picken.PickorderLines.cPickorderLineAdapter;
import SSU_WHS.Picken.PickorderLines.cPickorderLineRecyclerItemTouchHelper;
import SSU_WHS.Picken.Pickorders.cPickorder;
import nl.icsvertex.scansuite.Activities.Pick.PickorderLinesActivity;
import nl.icsvertex.scansuite.Fragments.Dialogs.NothingHereFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.SendOrderFragment;
import nl.icsvertex.scansuite.R;

public class PickorderLinesPickedFragment extends Fragment implements iICSDefaultFragment, cPickorderLineRecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    //Region Public Properties
    //End Region Public Properties

    //Region Private Properties

    private RecyclerView recyclerViewPickorderLinesPicked;
    private SearchView recyclerSearchView;
    private Switch switchDefects;
    private ConstraintLayout abortOrderView;

    private List<cPickorderLine> localLinesObl;

    private cPickorderLineAdapter pickorderLineAdapter;
    private cPickorderLineAdapter getPickorderLineAdapter(){
        if (this.pickorderLineAdapter == null) {
            this.pickorderLineAdapter = new cPickorderLineAdapter();
        }

        return this.pickorderLineAdapter;
    }

    //End Region Private Properties

    //Region Constructor
    public PickorderLinesPickedFragment() {

    }
    //End Region Constructor

    //Region Default Methods
    @Override
    public View onCreateView(LayoutInflater pvInflater, ViewGroup pvContainer,
                             Bundle pvSavedInstanceState) {
        // Inflate the layout for this fragment
        return  pvInflater.inflate(R.layout.fragment_pickorder_lines_picked, pvContainer, false);
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
        PickorderLinesActivity.currentLineFragment = this;
        this.mFragmentInitialize();
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder pvViewHolder, int pvDirectionInt, int pvPositionInt) {

        if (!(pvViewHolder instanceof  cPickorderLineAdapter.PickorderLineViewHolder)) {
            return;
        }

        cPickorderLine.currentPickOrderLine = this.localLinesObl.get(pvPositionInt);

        //Reset the line
        this.mRemoveAdapterFromFragment();
    }

    // End Region Default Methods

    //Region iICSDefaultFragment defaults

    @Override
    public void mFragmentInitialize() {
        this.mFindViews();
        this.mFieldsInitialize();
        this.mSetListeners();
        this.pGetData(cPickorder.currentPickOrder.pGetLinesHandledFromDatabasObl());
        cUserInterface.pEnableScanner();
    }

    @Override
    public void mFindViews() {
        if (getView() != null) {
            this.recyclerSearchView = getView().findViewById(R.id.recyclerSearchView);
            this.recyclerViewPickorderLinesPicked = getView().findViewById(R.id.recyclerViewPickorderLinesPicked);
            this.switchDefects = getView().findViewById(R.id.switchDefects);
            this.abortOrderView = getView().findViewById(R.id.abortConstraintView);
        }
    }


    @Override
    public void mFieldsInitialize() {
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new cPickorderLineRecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(this.recyclerViewPickorderLinesPicked);
    }

    @Override
    public void mSetListeners() {
        this.mSetRecyclerOnScrollListener();
        this.mSetSearchListener();
        this.mSetDefectsListener();
        this.mSetAbortListener();
    }


    //End Region iICSDefaultFragment defaults

    //Region Public Methods

    public void pNoLinesAvailable(Boolean pvEnabledBln) {

        if (cAppExtension.activity instanceof  PickorderLinesActivity) {
            if (PickorderLinesActivity.currentLineFragment instanceof PickorderLinesPickedFragment) {

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

                this.abortOrderView.setVisibility(View.INVISIBLE);
                this.recyclerViewPickorderLinesPicked.setVisibility(View.INVISIBLE);

                FragmentTransaction fragmentTransaction = cAppExtension.fragmentManager.beginTransaction();
                NothingHereFragment fragment = new NothingHereFragment();
                fragmentTransaction.replace(R.id.fragmentPickorderLinesPicked, fragment);
                fragmentTransaction.commit();

                if (cAppExtension.activity instanceof  PickorderLinesActivity) {
                    PickorderLinesActivity pickorderLinesActivity = (PickorderLinesActivity)cAppExtension.activity;
                    pickorderLinesActivity.pChangeTabCounterText(cText.pDoubleToStringStr(cPickorder.currentPickOrder.pQuantityHandledDbl()) + "/" + cText.pDoubleToStringStr(cPickorder.currentPickOrder.pQuantityTotalDbl()));
                }


            }
        }
    }

    public  void pGetData(List<cPickorderLine> pvDataObl) {
       this.localLinesObl = pvDataObl;
       this.mFillRecycler(this.localLinesObl);
    }

    //End Region Public Methods

    //Region Private Methods

    private void mFillRecycler(List<cPickorderLine> pvDataObl) {

        if (pvDataObl.size() == 0) {
            this.pNoLinesAvailable(true);
            return;
        }

        this.pNoLinesAvailable(false);

        this.switchDefects.setVisibility(View.VISIBLE);
        this.abortOrderView.setVisibility(View.VISIBLE);

        this.getPickorderLineAdapter().pFillData(pvDataObl);
        this.recyclerViewPickorderLinesPicked.setHasFixedSize(false);
        this.recyclerViewPickorderLinesPicked.setAdapter(this.getPickorderLineAdapter());
        this.recyclerViewPickorderLinesPicked.setLayoutManager(new LinearLayoutManager(cAppExtension.context));
        this.recyclerViewPickorderLinesPicked.setVisibility(View.VISIBLE);

        if (cAppExtension.activity instanceof PickorderLinesActivity) {
            PickorderLinesActivity pickorderLinesActivity = (PickorderLinesActivity)cAppExtension.activity;
            pickorderLinesActivity.pChangeTabCounterText(cText.pDoubleToStringStr(cPickorder.currentPickOrder.pQuantityHandledDbl()) + "/" + cText.pDoubleToStringStr(cPickorder.currentPickOrder.pQuantityTotalDbl()));

        }


    }

    //End Region Private Methods

    private void mSetDefectsListener() {
        this.switchDefects.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean show) {
                getPickorderLineAdapter().pShowDefects(show);
                }
        });
    }

    private void mSetAbortListener() {
        this.abortOrderView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (cAppExtension.activity instanceof  PickorderLinesActivity) {
                    PickorderLinesActivity pickorderLinesActivity = (PickorderLinesActivity)cAppExtension.activity;
                    pickorderLinesActivity.pAskAbort();
                }

            }
        });
    }



    private void mSetRecyclerOnScrollListener() {
        this.recyclerViewPickorderLinesPicked.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutmanager = (LinearLayoutManager)recyclerView.getLayoutManager();
                if (dy < 0) {
                    int itemPosition = 0;
                    if (layoutmanager != null) {
                        itemPosition = layoutmanager.findFirstCompletelyVisibleItemPosition();
                    }

                    if(itemPosition==0){
                        //cUserInterface.pShowToastMessage(thisContext, "Show", null);
                        // Prepare the View for the animation
                        recyclerSearchView.setVisibility(View.VISIBLE);
                        recyclerSearchView.setAlpha(0.0f);

                        // Start the animation
                        recyclerSearchView.animate()
                                //.translationY(recyclerSearchView.getHeight())
                                .translationY(0)
                                .alpha(1.0f)
                                .setListener(null);

                    }

                } else {
                    int itemPosition = 0;
                    if (layoutmanager != null) {
                        itemPosition = layoutmanager.findFirstCompletelyVisibleItemPosition();
                    }

                    if(itemPosition>1){// your *second item your recyclerview
                        // Start the animation
                        recyclerSearchView.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    private void mSetSearchListener() {
        //make whole view clickable
        this.recyclerSearchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerSearchView.setIconified(false);
            }
        });

        //query entered
        this.recyclerSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String pvQueryTextStr) {
                getPickorderLineAdapter().pSetFilter(pvQueryTextStr);
                return true;
            }
        });
    }

    private void mRemoveAdapterFromFragment(){

        //remove the item from recyclerview
        boolean resultBln = cPickorderLine.currentPickOrderLine.pResetBln();
        if (! resultBln) {
            cUserInterface.pDoExplodingScreen(cAppExtension.activity.getString(R.string.message_reset_line_via_webservice_failed),"",true,true);
            return;
        }

        //Renew data, so only current lines are shown
        this.pGetData(cPickorder.currentPickOrder.pGetLinesHandledFromDatabasObl());

    }

}
