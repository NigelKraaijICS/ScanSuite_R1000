package nl.icsvertex.scansuite.Fragments.inventory;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ICS.Interfaces.iICSDefaultFragment;
import ICS.Utils.cText;
import ICS.cAppExtension;
import SSU_WHS.Basics.Settings.cSetting;
import SSU_WHS.General.cPublicDefinitions;
import SSU_WHS.Inventory.InventoryOrders.cInventoryorder;
import SSU_WHS.Inventory.InventoryorderBins.cInventoryorderBin;
import nl.icsvertex.scansuite.Activities.inventory.InventoryorderBinsActivity;
import nl.icsvertex.scansuite.Fragments.NothingHereFragment;
import nl.icsvertex.scansuite.Fragments.SendOrderFragment;
import nl.icsvertex.scansuite.Fragments.dialogs.AddBinFragment;
import nl.icsvertex.scansuite.R;

public class InventoryBinsToDoFragment extends Fragment implements iICSDefaultFragment {

    ImageView imageAddBin;
    static RecyclerView recyclerViewInventoryBinsToDo;
    @Override
    public View onCreateView(LayoutInflater pvInflater, ViewGroup pvContainer,
                             Bundle pvSavedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = pvInflater.inflate(R.layout.fragment_inventoryorder_bins_todo, pvContainer, false);
        return rootview;
    }

    @Override
    public void onViewCreated(@NonNull View pvView, @Nullable Bundle pvSavedInstanceState) {
        this.mFragmentInitialize();
    }

    @Override
    public void mFragmentInitialize() {

        this.mFindViews();
        this.mFieldsInitialize();
        this.mSetListeners();
        this.mGetData();
    }

    @Override
    public void mFindViews() {
        imageAddBin = getView().findViewById(R.id.imageAddBin);
        recyclerViewInventoryBinsToDo = getView().findViewById(R.id.recyclerViewInventoryBinsToDo);
    }



    @Override
    public void mFieldsInitialize() {
        if (cInventoryorder.currentInventoryOrder.isGeneratedBln() || cInventoryorder.currentInventoryOrder.isInvAddExtraBinBln()) {
            imageAddBin.setVisibility(View.VISIBLE);
        }
        else {
            imageAddBin.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void mSetListeners() {
        mSetAddBinListener();
    }

    @Override
    public void setUserVisibleHint(boolean pvIsVisibleToUserBln) {
        super.setUserVisibleHint(pvIsVisibleToUserBln);

        if (pvIsVisibleToUserBln) {

            InventoryorderBinsActivity.currentBinFragment = this;

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.detach(this).attach(this).commit();
        }
    }
    public static void mGetData() {
        List<cInventoryorderBin> HandledBinsObl = cInventoryorder.currentInventoryOrder.pGetBinsNotDoneFromDatabasObl();
        mFillRecycler(HandledBinsObl);
    }

    private void mSetAddBinListener() {
        imageAddBin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddBinFragment addBinFragment = new AddBinFragment();
                addBinFragment.setCancelable(true);
                addBinFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.ADDBIN_TAG);
            }
        });
    }

    private static void mFillRecycler(List<cInventoryorderBin> pvDataObl) {

        if (pvDataObl.size() == 0) {
            InventoryBinsToDoFragment.mNoLinesAvailable(true);
            return;
        }
        InventoryBinsToDoFragment.mNoLinesAvailable(false);

        cInventoryorderBin.getInventoryorderBinNotDoneAdapter().pFillData(pvDataObl);
        recyclerViewInventoryBinsToDo.setHasFixedSize(false);
        recyclerViewInventoryBinsToDo.setAdapter(cInventoryorderBin.getInventoryorderBinNotDoneAdapter());
        recyclerViewInventoryBinsToDo.setLayoutManager(new LinearLayoutManager(cAppExtension.context));
        recyclerViewInventoryBinsToDo.setVisibility(View.VISIBLE);

        InventoryorderBinsActivity.pChangeTabCounterText(cText.pIntToStringStr(cInventoryorder.currentInventoryOrder.pGetBinsNotDoneFromDatabasObl().size()) + "/" + cText.pIntToStringStr(cInventoryorder.currentInventoryOrder.pGetBinsTotalFromDatabasObl().size()));
    }
    private static void mNoLinesAvailable(Boolean pvEnabledBln) {

        if (InventoryorderBinsActivity.currentBinFragment != null && InventoryorderBinsActivity.currentBinFragment  instanceof InventoryBinsToDoFragment) {
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
            recyclerViewInventoryBinsToDo.setVisibility(View.INVISIBLE);

            //Hide location button and clear text

            //Show nothing there fragment
            FragmentTransaction fragmentTransaction = cAppExtension.fragmentManager.beginTransaction();
            NothingHereFragment fragment = new NothingHereFragment();
            fragmentTransaction.replace(R.id.fragmentInventoryorderBinsToDo, fragment);
            fragmentTransaction.commit();

            //Change tabcounter text
            InventoryorderBinsActivity.pChangeTabCounterText(cText.pIntToStringStr(cInventoryorder.currentInventoryOrder.pGetBinsNotDoneFromDatabasObl().size()) + "/" + cText.pIntToStringStr(cInventoryorder.currentInventoryOrder.pGetBinsTotalFromDatabasObl().size()));
        }
    }
}


