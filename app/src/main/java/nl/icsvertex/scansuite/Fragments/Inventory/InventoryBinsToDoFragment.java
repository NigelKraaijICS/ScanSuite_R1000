package nl.icsvertex.scansuite.Fragments.Inventory;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ICS.Interfaces.iICSDefaultFragment;
import ICS.Utils.cText;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.General.cPublicDefinitions;
import SSU_WHS.Inventory.InventoryOrders.cInventoryorder;
import SSU_WHS.Inventory.InventoryorderBins.cInventoryorderBin;
import SSU_WHS.Inventory.InventoryorderBins.cInventoryorderBinAdapter;
import nl.icsvertex.scansuite.Activities.Inventory.InventoryorderBinsActivity;
import nl.icsvertex.scansuite.Fragments.Dialogs.AddBinFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.NothingHereFragment;
import nl.icsvertex.scansuite.Fragments.Dialogs.SendOrderFragment;
import nl.icsvertex.scansuite.R;

public class InventoryBinsToDoFragment extends Fragment implements iICSDefaultFragment {

    //Region Public Properties

    //End Region Public Properties

    //Region Private Properties

    private  ImageView imageAddBin;
    private  RecyclerView recyclerViewInventoryBinsToDo;

    private  TextView quickhelpText;
    private  ImageView quickhelpIcon;
    private  ConstraintLayout quickhelpContainer;

    private cInventoryorderBinAdapter inventoryorderBinAdapter;
    private cInventoryorderBinAdapter getInventoryorderBinAdapter() {
        if (inventoryorderBinAdapter != null) {
            return  inventoryorderBinAdapter;
        }

        inventoryorderBinAdapter = new cInventoryorderBinAdapter();
        return  inventoryorderBinAdapter;
    }

    //End Region Private Properties


    //Region Default Methods

    @Override
    public View onCreateView(LayoutInflater pvInflater, ViewGroup pvContainer,
                             Bundle pvSavedInstanceState) {
        // Inflate the layout for this fragment
        return pvInflater.inflate(R.layout.fragment_inventoryorder_bins_todo, pvContainer, false);
    }

    @Override
    public void onViewCreated(@NonNull View pvView, @Nullable Bundle pvSavedInstanceState) {

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
        InventoryorderBinsActivity.currentBinFragment = this;
        this.mFragmentInitialize();
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
            this.imageAddBin = getView().findViewById(R.id.imageAddBin);
            this.recyclerViewInventoryBinsToDo = getView().findViewById(R.id.recyclerViewMoveLinesTake);
            this.quickhelpText = getView().findViewById(R.id.quickhelpText);
            this.quickhelpContainer = getView().findViewById(R.id.quickHelpContainer);
            this.quickhelpIcon = getView().findViewById(R.id.quickhelpIcon);
        }

    }

    @Override
    public void mFieldsInitialize() {

        this.quickhelpText.setText(R.string.scan_bincode);

        if (cInventoryorder.currentInventoryOrder.isGeneratedBln() || cInventoryorder.currentInventoryOrder.isInvAddExtraBinBln()) {
            this.imageAddBin.setVisibility(View.VISIBLE);
        }
        else {
            this.imageAddBin.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void mSetListeners() {
        this.mSetAddBinListener();
        this.mSetQuickHelpListener();
    }

    //End Region iICSDefaultActivity defaults

    //Region Public Methods

    //End Region Public Methods

    //Region Private Methods

    private  void mGetData() {
        this.mFillRecycler(cInventoryorder.currentInventoryOrder.pGetBinsNotDoneFromDatabasObl());
    }

    private void mSetAddBinListener() {
        this.imageAddBin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               mShowAddBinFragment();
            }
        });
    }

    private  void mFillRecycler(List<cInventoryorderBin> pvDataObl) {

        if (pvDataObl.size() == 0) {
            this.mNoLinesAvailable(true);
            return;
        }
        this.mNoLinesAvailable(false);

        this.getInventoryorderBinAdapter().pFillData(pvDataObl);
        this.recyclerViewInventoryBinsToDo.setHasFixedSize(false);
        this.recyclerViewInventoryBinsToDo.setAdapter(this.getInventoryorderBinAdapter());
        this.recyclerViewInventoryBinsToDo.setLayoutManager(new LinearLayoutManager(cAppExtension.context));
        this.recyclerViewInventoryBinsToDo.setVisibility(View.VISIBLE);

        if (cAppExtension.activity instanceof  InventoryorderBinsActivity) {
            InventoryorderBinsActivity inventoryorderBinsActivity = (InventoryorderBinsActivity)cAppExtension.activity;
            inventoryorderBinsActivity.pChangeTabCounterText(cText.pIntToStringStr(cInventoryorder.currentInventoryOrder.pGetBinsNotDoneFromDatabasObl().size()) + "/" + cText.pIntToStringStr(cInventoryorder.currentInventoryOrder.pGetBinsTotalFromDatabasObl().size()));
        }
    }

    private  void mNoLinesAvailable(Boolean pvEnabledBln) {

        if (InventoryorderBinsActivity.currentBinFragment  instanceof InventoryBinsToDoFragment) {
            //Close no orders fragment if needed
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

            //Hide the recycler view
            this.recyclerViewInventoryBinsToDo.setVisibility(View.INVISIBLE);

            //Hide location button and clear text

            //Show nothing there fragment
            FragmentTransaction fragmentTransaction = cAppExtension.fragmentManager.beginTransaction();
            NothingHereFragment fragment = new NothingHereFragment();
            fragmentTransaction.replace(R.id.fragmentInventoryorderBinsToDo, fragment);
            fragmentTransaction.commit();

            //Change tabcounter text

            if (cAppExtension.activity instanceof  InventoryorderBinsActivity) {
                InventoryorderBinsActivity inventoryorderBinsActivity = (InventoryorderBinsActivity)cAppExtension.activity;
                inventoryorderBinsActivity.pChangeTabCounterText(cText.pIntToStringStr(cInventoryorder.currentInventoryOrder.pGetBinsNotDoneFromDatabasObl().size()) + "/" + cText.pIntToStringStr(cInventoryorder.currentInventoryOrder.pGetBinsTotalFromDatabasObl().size()));
            }
        }
    }

    private void mShowAddBinFragment(){

        AddBinFragment addBinFragment = new AddBinFragment();
        addBinFragment.setCancelable(true);
        addBinFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.ADDBIN_TAG);

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






