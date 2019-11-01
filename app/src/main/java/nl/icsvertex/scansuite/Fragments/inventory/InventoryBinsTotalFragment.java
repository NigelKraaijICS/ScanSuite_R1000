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
import SSU_WHS.Inventory.InventoryOrders.cInventoryorder;
import SSU_WHS.Inventory.InventoryorderBins.cInventoryorderBin;
import nl.icsvertex.scansuite.Activities.inventory.InventoryorderBinsActivity;
import nl.icsvertex.scansuite.R;

public class InventoryBinsTotalFragment extends Fragment implements iICSDefaultFragment {

    //Region Public Properties

    //End Region Public Properties

    //Region Private Properties

    private static RecyclerView recyclerViewInventoryBinsTotal;
    private ImageView imageCloseOrder;

    //End Region Private Properties

    //Region Default Methods

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
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
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
        InventoryBinsTotalFragment.recyclerViewInventoryBinsTotal = getView().findViewById(R.id.recyclerViewInventoryBinsTotal);
        this.imageCloseOrder = getView().findViewById(R.id.imageCloseOrder);
    }

    @Override
    public void mFieldsInitialize() {

        this.imageCloseOrder.setVisibility(View.INVISIBLE);


        if (cInventoryorder.currentInventoryOrder.pGetBinsDoneFromDatabasObl().size() >0 && cInventoryorder.currentInventoryOrder.pGetBinsNotDoneFromDatabasObl().size() == 0 ) {
            this.imageCloseOrder.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void mSetListeners() {
        this.mSetDoneListener();
    }

    //End Region iICSDefaultActivity defaults

    //Region Public Methods

    //End Region Public Methods

    //Region Private Methods

    private void mGetData() {
        mFillRecycler(cInventoryorder.currentInventoryOrder.pGetBinsTotalFromDatabasObl());
    }

    private static void mFillRecycler(List<cInventoryorderBin> pvDataObl) {

        cInventoryorderBin.getInventoryorderBinTotalAdapter().pFillData(pvDataObl);
        recyclerViewInventoryBinsTotal.setHasFixedSize(false);
        recyclerViewInventoryBinsTotal.setAdapter(cInventoryorderBin.getInventoryorderBinTotalAdapter());
        recyclerViewInventoryBinsTotal.setLayoutManager(new LinearLayoutManager(cAppExtension.context));
        recyclerViewInventoryBinsTotal.setVisibility(View.VISIBLE);
        InventoryorderBinsActivity.pChangeTabCounterText(cText.pIntToStringStr(cInventoryorder.currentInventoryOrder.pGetBinsTotalFromDatabasObl().size()));

    }

    private void mSetDoneListener() {
        this.imageCloseOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InventoryorderBinsActivity.pHandleOrderCloseClick();
            }
        });
    }

    //End Region Private Methods

}
