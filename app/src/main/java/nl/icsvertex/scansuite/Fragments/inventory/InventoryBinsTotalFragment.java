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
import nl.icsvertex.scansuite.R;

public class InventoryBinsTotalFragment extends Fragment implements iICSDefaultFragment {

    static RecyclerView recyclerViewInventoryBinsTotal;

    @Override
    public View onCreateView(LayoutInflater pvInflater, ViewGroup pvContainer,
                             Bundle pvSavedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = pvInflater.inflate(R.layout.fragment_inventoryorder_bins_total, pvContainer, false);
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
        recyclerViewInventoryBinsTotal = getView().findViewById(R.id.recyclerViewInventoryBinsTotal);
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
        List<cInventoryorderBin> HandledBinsObl = cInventoryorder.currentInventoryOrder.pGetBinsTotalFromDatabasObl();
        mFillRecycler(HandledBinsObl);
    }

    private static void mFillRecycler(List<cInventoryorderBin> pvDataObl) {

        cInventoryorderBin.getInventoryorderBinTotalAdapter().pFillData(pvDataObl);
        recyclerViewInventoryBinsTotal.setHasFixedSize(false);
        recyclerViewInventoryBinsTotal.setAdapter(cInventoryorderBin.getInventoryorderBinTotalAdapter());
        recyclerViewInventoryBinsTotal.setLayoutManager(new LinearLayoutManager(cAppExtension.context));
        recyclerViewInventoryBinsTotal.setVisibility(View.VISIBLE);
        InventoryorderBinsActivity.pChangeTabCounterText(cText.pIntToStringStr(cInventoryorder.currentInventoryOrder.pGetBinsTotalFromDatabasObl().size()));

    }
}
