package nl.icsvertex.scansuite.fragments.pick;

import android.app.AlertDialog;

import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;

import ICS.Interfaces.iICSDefaultFragment;
import ICS.Utils.cText;
import ICS.Utils.cUserInterface;
import SSU_WHS.Picken.PickorderLines.cPickorderLine;
import SSU_WHS.Picken.Pickorders.cPickorder;
import nl.icsvertex.scansuite.activities.pick.PickorderLinesActivity;
import nl.icsvertex.scansuite.cAppExtension;
import nl.icsvertex.scansuite.fragments.NothingHereFragment;
import nl.icsvertex.scansuite.R;

public class PickorderLinesPickedFragment extends Fragment implements iICSDefaultFragment {

    //Region Public Properties
    static public TextView textViewSelectedLine;
    //End Region Public Properties

    //Region Private Properties

    RecyclerView recyclerViewPickorderLinesPicked;
    androidx.appcompat.widget.SearchView recyclerSearchView;
    Switch switchDefects;

    ConstraintLayout fragmentPickorderLinesToPick;
    ConstraintLayout resetPicklineView;
    ConstraintLayout abortOrderView;

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
        View rootview = pvInflater.inflate(R.layout.fragment_pickorder_lines_picked, pvContainer, false);
        return rootview;


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        this.mFragmentInitialize();
    }

    @Override
    public void setUserVisibleHint(boolean pvIsVisibleToUserBln) {
        super.setUserVisibleHint(pvIsVisibleToUserBln);

        if (pvIsVisibleToUserBln) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.detach(this).attach(this).commit();
        }
    }


    // End Region Default Methods

    //Region iICSDefaultFragment defaults

    @Override
    public void mFragmentInitialize() {
        this.mFindViews();
        this.mSetViewModels();
        this.mFieldsInitialize();
        this.mSetListeners();
        this.mGetData();
    }

    @Override
    public void mFindViews() {
        this.fragmentPickorderLinesToPick = getView().findViewById(R.id.fragmentPickorderLinesToPick);
        this.recyclerSearchView = getView().findViewById(R.id.recyclerSearchView);
        this.recyclerViewPickorderLinesPicked = getView().findViewById(R.id.recyclerViewPickorderLinesPicked);
        this.switchDefects = getView().findViewById(R.id.switchDefects);
        this.textViewSelectedLine = getView().findViewById(R.id.textViewSelectedLine);
        this.resetPicklineView = getView().findViewById(R.id.resetPicklineView);
        this.abortOrderView = getView().findViewById(R.id.abortOrderView);
    }

    @Override
    public void mSetViewModels() {

    }

    @Override
    public void mFieldsInitialize() {

    }

    @Override
    public void mSetListeners() {
        this.mSetRecyclerOnScrollListener();
        this.mSetSearchListener();
        this.mSetDefectsListener();
        this.mSetResetListener();
        this.mSetAbortListener();
    }


    //End Region iICSDefaultFragment defaults

    //Region Public Methods

    public static void pSetChosenItemNo() {
        PickorderLinesPickedFragment.textViewSelectedLine.setText(cPickorderLine.currentPickOrderLine.getItemNoStr());
    }

    //End Region Public Methods

    //Region Private Methods

    private void mGetData() {

        List<cPickorderLine> HandledLinesObl = cPickorder.currentPickOrder.pGetLinesHandledFromDatabasObl();
        this.mFillRecycler(HandledLinesObl);

    }

    private void mFillRecycler(List<cPickorderLine> pvDataObl) {

        if (pvDataObl.size() == 0) {
            this.mNoLinesAvailable(true);
            return;
        }

        this.mNoLinesAvailable(false);

        this.switchDefects.setVisibility(View.VISIBLE);
        this.resetPicklineView.setVisibility(View.VISIBLE);
        this.abortOrderView.setVisibility(View.VISIBLE);

        cPickorderLine.getPickorderLinePickedAdapter().pFillData(pvDataObl);
        this.recyclerViewPickorderLinesPicked.setHasFixedSize(false);
        this.recyclerViewPickorderLinesPicked.setAdapter(cPickorderLine.getPickorderLinePickedAdapter());
        this.recyclerViewPickorderLinesPicked.setLayoutManager(new LinearLayoutManager(cAppExtension.context));
        this.recyclerViewPickorderLinesPicked.setVisibility(View.VISIBLE);

        PickorderLinesActivity.pChangeTabCounterText(cText.doubleToString(cPickorder.currentPickOrder.pQuantityHandledDbl()) + "/" + cText.doubleToString(cPickorder.currentPickOrder.pQuantityTotalDbl()));

    }

    //End Region Private Methods

    private void mSetDefectsListener() {
        this.switchDefects.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean show) {
                   cPickorderLine.getPickorderLinePickedAdapter().pvShowDefects(show);
                }
        });
    }

    private void mSetResetListener() {
        this.resetPicklineView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAskResetLine();
            }
        });
    }

    private void mSetAbortListener() {
        this.abortOrderView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAskAbort();
            }
        });
    }

    private void mAskResetLine() {

        AlertDialog.Builder builder = new AlertDialog.Builder(cAppExtension.context);
        builder.setTitle(R.string.message_reset_header);
        builder.setMessage(getString(R.string.message_reset_text));
        builder.setPositiveButton(R.string.button_reset, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                PickorderLinesActivity.pHandleLineReset();
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

        if (cPickorder.currentPickOrder.pAbortBln() == false) {
            cUserInterface.pDoExplodingScreen(cAppExtension.context.getString(R.string.error_couldnt_abort_order), cPickorderLine.currentPickOrderLine.getLineNoInt().toString(), true, true );
            return;
        }

        this.mGetData();
        return;

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
                    int itemPosition = layoutmanager.findFirstCompletelyVisibleItemPosition();

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
                    int itemPosition = layoutmanager.findFirstCompletelyVisibleItemPosition();

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
            public boolean onQueryTextChange(String queryText) {
                cPickorderLine.getPickorderLinePickedAdapter().pSetFilter(queryText);
                return true;
            }
        });
    }

    private void mNoLinesAvailable(Boolean pvEnabledBln) {

        if (pvEnabledBln == false) {
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

            this.switchDefects.setVisibility(View.INVISIBLE);
            this.resetPicklineView.setVisibility(View.INVISIBLE);
            this.abortOrderView.setVisibility(View.INVISIBLE);
            this.recyclerViewPickorderLinesPicked.setVisibility(View.INVISIBLE);

            FragmentTransaction fragmentTransaction = cAppExtension.fragmentManager.beginTransaction();
            NothingHereFragment fragment = new NothingHereFragment();
            fragmentTransaction.replace(R.id.fragmentPickorderLinesPicked, fragment);
            fragmentTransaction.commit();

            PickorderLinesActivity.pChangeTabCounterText(cText.doubleToString(cPickorder.currentPickOrder.pQuantityHandledDbl()) + "/" + cText.doubleToString(cPickorder.currentPickOrder.pQuantityTotalDbl()));

            return;


    }


}
