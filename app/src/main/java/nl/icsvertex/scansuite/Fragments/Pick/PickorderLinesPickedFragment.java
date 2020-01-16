package nl.icsvertex.scansuite.Fragments.Pick;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

    private static RecyclerView recyclerViewPickorderLinesPicked;
    private static androidx.appcompat.widget.SearchView recyclerSearchView;
    private static Switch switchDefects;
    private static ConstraintLayout abortOrderView;

    private static List<cPickorderLine> localLinesObl;

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

        cPickorderLine.currentPickOrderLine = PickorderLinesPickedFragment.localLinesObl.get(pvPositionInt);

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
        PickorderLinesPickedFragment.pGetData(cPickorder.currentPickOrder.pGetLinesHandledFromDatabasObl());
        cUserInterface.pEnableScanner();
    }

    @Override
    public void mFindViews() {

        if (getView() != null) {
            PickorderLinesPickedFragment.recyclerSearchView = getView().findViewById(R.id.recyclerSearchView);
            PickorderLinesPickedFragment.recyclerViewPickorderLinesPicked = getView().findViewById(R.id.recyclerViewPickorderLinesPicked);
            PickorderLinesPickedFragment.switchDefects = getView().findViewById(R.id.switchDefects);
            PickorderLinesPickedFragment.abortOrderView = getView().findViewById(R.id.actionsConstraintView);
        }

    }


    @Override
    public void mFieldsInitialize() {

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new cPickorderLineRecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(PickorderLinesPickedFragment.recyclerViewPickorderLinesPicked);

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


    public static void pNoLinesAvailable(Boolean pvEnabledBln) {

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

            PickorderLinesPickedFragment.abortOrderView.setVisibility(View.INVISIBLE);
            PickorderLinesPickedFragment.recyclerViewPickorderLinesPicked.setVisibility(View.INVISIBLE);

            FragmentTransaction fragmentTransaction = cAppExtension.fragmentManager.beginTransaction();
            NothingHereFragment fragment = new NothingHereFragment();
            fragmentTransaction.replace(R.id.fragmentPickorderLinesPicked, fragment);
            fragmentTransaction.commit();

            PickorderLinesActivity.pChangeTabCounterText(cText.pDoubleToStringStr(cPickorder.currentPickOrder.pQuantityHandledDbl()) + "/" + cText.pDoubleToStringStr(cPickorder.currentPickOrder.pQuantityTotalDbl()));

        }





    }

    public static void pGetData(List<cPickorderLine> pvDataObl) {

       PickorderLinesPickedFragment.localLinesObl = pvDataObl;
       PickorderLinesPickedFragment.mFillRecycler(PickorderLinesPickedFragment.localLinesObl);
    }

    //End Region Public Methods

    //Region Private Methods

    private static void mFillRecycler(List<cPickorderLine> pvDataObl) {

        if (pvDataObl.size() == 0) {
            PickorderLinesPickedFragment.pNoLinesAvailable(true);
            return;
        }

        PickorderLinesPickedFragment.pNoLinesAvailable(false);

        PickorderLinesPickedFragment.switchDefects.setVisibility(View.VISIBLE);
        PickorderLinesPickedFragment.abortOrderView.setVisibility(View.VISIBLE);

        cPickorderLine.getPickorderLinePickedAdapter().pFillData(pvDataObl);
        PickorderLinesPickedFragment.recyclerViewPickorderLinesPicked.setHasFixedSize(false);
        PickorderLinesPickedFragment.recyclerViewPickorderLinesPicked.setAdapter(cPickorderLine.getPickorderLinePickedAdapter());
        PickorderLinesPickedFragment.recyclerViewPickorderLinesPicked.setLayoutManager(new LinearLayoutManager(cAppExtension.context));
        PickorderLinesPickedFragment.recyclerViewPickorderLinesPicked.setVisibility(View.VISIBLE);

        PickorderLinesActivity.pChangeTabCounterText(cText.pDoubleToStringStr(cPickorder.currentPickOrder.pQuantityHandledDbl()) + "/" + cText.pDoubleToStringStr(cPickorder.currentPickOrder.pQuantityTotalDbl()));

    }

    //End Region Private Methods

    private void mSetDefectsListener() {
        PickorderLinesPickedFragment.switchDefects.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean show) {
                   cPickorderLine.getPickorderLinePickedAdapter().pShowDefects(show);
                }
        });
    }

    private void mSetAbortListener() {
        PickorderLinesPickedFragment.abortOrderView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAskAbort();
            }
        });
    }

    private void mAskAbort() {

        cUserInterface.pCheckAndCloseOpenDialogs();

        AlertDialog.Builder builder = new AlertDialog.Builder(cAppExtension.context);
        builder.setTitle(R.string.message_abort_header);
        builder.setMessage(getString(R.string.message_abort_text));
        builder.setPositiveButton(R.string.button_abort, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mAbortOrder();
            }
        });

        builder.setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //do nothing (close the dialog)
            }
        });

        builder.show();
    }

    private void mAbortOrder() {

        if (!cPickorder.currentPickOrder.pAbortBln()) {
            cUserInterface.pDoExplodingScreen(cAppExtension.context.getString(R.string.error_couldnt_abort_order), cPickorderLine.currentPickOrderLine.getLineNoInt().toString(), true, true );
            return;
        }

        PickorderLinesPickedFragment.pGetData(cPickorder.currentPickOrder.pGetLinesHandledFromDatabasObl());

    }

    private void mSetRecyclerOnScrollListener() {
        recyclerViewPickorderLinesPicked.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
        PickorderLinesPickedFragment.recyclerSearchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PickorderLinesPickedFragment.recyclerSearchView.setIconified(false);
            }
        });

        //query entered
        PickorderLinesPickedFragment.recyclerSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String pvQueryTextStr) {
                cPickorderLine.getPickorderLinePickedAdapter().pSetFilter(pvQueryTextStr);
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
        PickorderLinesPickedFragment.pGetData(cPickorder.currentPickOrder.pGetLinesHandledFromDatabasObl());

    }

}
