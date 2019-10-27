package nl.icsvertex.scansuite.Fragments.inventory;

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

import java.util.List;

import ICS.Interfaces.iICSDefaultFragment;
import ICS.Utils.cText;
import ICS.cAppExtension;
import SSU_WHS.Inventory.InventoryOrders.cInventoryorder;
import SSU_WHS.Inventory.InventoryorderBins.cInventoryorderBin;
import nl.icsvertex.scansuite.Activities.inventory.InventoryorderBinsActivity;
import nl.icsvertex.scansuite.Fragments.NothingHereFragment;
import nl.icsvertex.scansuite.Fragments.SendOrderFragment;
import nl.icsvertex.scansuite.R;

public class InventoryBinsDoneFragment extends Fragment implements iICSDefaultFragment {
    static RecyclerView recyclerViewInventoryBinsDone;

    @Override
    public View onCreateView(LayoutInflater pvInflater, ViewGroup pvContainer,
                             Bundle pvSavedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = pvInflater.inflate(R.layout.fragment_inventoryorder_bins_done, pvContainer, false);
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
        recyclerViewInventoryBinsDone = getView().findViewById(R.id.recyclerViewInventoryBinsDone);
    }


    @Override
    public void mFieldsInitialize() {

    }

    @Override
    public void mSetListeners() {

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
    private void mGetData() {
        List<cInventoryorderBin> HandledBinsObl = cInventoryorder.currentInventoryOrder.pGetBinsDoneFromDatabasObl();
        InventoryBinsDoneFragment.mFillRecycler(HandledBinsObl);
    }
    private static void mFillRecycler(List<cInventoryorderBin> pvDataObl) {

        if (pvDataObl.size() == 0) {
            InventoryBinsDoneFragment.mNoLinesAvailable(true);
            return;
        }

        InventoryBinsDoneFragment.mNoLinesAvailable(false);

        cInventoryorderBin.getInventoryorderBinDoneAdapter().pFillData(pvDataObl);
        recyclerViewInventoryBinsDone.setHasFixedSize(false);
        recyclerViewInventoryBinsDone.setAdapter(cInventoryorderBin.getInventoryorderBinDoneAdapter());
        recyclerViewInventoryBinsDone.setLayoutManager(new LinearLayoutManager(cAppExtension.context));
        recyclerViewInventoryBinsDone.setVisibility(View.VISIBLE);

        InventoryorderBinsActivity.pChangeTabCounterText(cText.pIntToStringStr(cInventoryorder.currentInventoryOrder.pGetBinsDoneFromDatabasObl().size()) + "/" + cText.pIntToStringStr(cInventoryorder.currentInventoryOrder.pGetBinsTotalFromDatabasObl().size()));

    }
    public static void mNoLinesAvailable(Boolean pvEnabledBln) {

        if (InventoryorderBinsActivity.currentBinFragment != null && InventoryorderBinsActivity.currentBinFragment instanceof InventoryBinsDoneFragment) {

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

            FragmentTransaction fragmentTransaction = cAppExtension.fragmentManager.beginTransaction();
            NothingHereFragment fragment = new NothingHereFragment();
            fragmentTransaction.replace(R.id.fragmentInventoryorderBinsDone, fragment);
            fragmentTransaction.commit();

            InventoryorderBinsActivity.pChangeTabCounterText(cText.pIntToStringStr(cInventoryorder.currentInventoryOrder.pGetBinsDoneFromDatabasObl().size()) + "/" + cText.pIntToStringStr(cInventoryorder.currentInventoryOrder.pGetBinsTotalFromDatabasObl().size()));

        }
    }
}
