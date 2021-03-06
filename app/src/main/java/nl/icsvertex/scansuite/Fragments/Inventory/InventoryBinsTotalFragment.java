package nl.icsvertex.scansuite.Fragments.Inventory;

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
import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cResult;
import ICS.Utils.cText;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Basics.Settings.cSetting;
import SSU_WHS.Inventory.InventoryOrders.cInventoryorder;
import SSU_WHS.Inventory.InventoryorderBins.cInventoryorderBin;
import SSU_WHS.Inventory.InventoryorderBins.cInventoryorderBinAdapter;
import SSU_WHS.Inventory.InventoryorderBins.cInventoryorderBinRecyclerItemTouchHelper;
import nl.icsvertex.scansuite.Activities.Inventory.InventoryorderBinsActivity;
import nl.icsvertex.scansuite.Fragments.Dialogs.NothingHereFragment;
import nl.icsvertex.scansuite.R;

public class InventoryBinsTotalFragment extends Fragment implements iICSDefaultFragment,   cInventoryorderBinRecyclerItemTouchHelper.RecyclerItemTouchHelperListener  {

    //Region Public Properties

    //End Region Public Properties

    //Region Private Properties

    private RecyclerView recyclerViewInventoryBinsTotal;
    private ImageView imageCloseOrder;
    private int positionSwiped;

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
        return pvInflater.inflate(R.layout.fragment_inventoryorder_bins_total, pvContainer, false);
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

    @Override
    public void onSwiped(RecyclerView.ViewHolder pvViewHolder, int pvDirectionInt, int pvPositionInt) {
        if (!(pvViewHolder instanceof cInventoryorderBinAdapter.InventoryorderBinViewHolder)) {
            return;
        }

        this.positionSwiped = pvPositionInt;


        cInventoryorderBin.currentInventoryOrderBin = cInventoryorder.currentInventoryOrder.pGetBinsTotalFromDatabasObl().get(pvPositionInt);
        if ( cInventoryorder.currentInventoryOrder.pGetItemCountForBinDbl(cInventoryorderBin.currentInventoryOrderBin.getBinCodeStr()) <= 0 && cInventoryorder.currentInventoryOrder.isGeneratedBln()) {
            cUserInterface.pShowSnackbarMessage(pvViewHolder.itemView,cAppExtension.activity.getString(R.string.message_zero_lines_cant_be_reset),null,true);
            this.getInventoryorderBinAdapter().notifyItemChanged(pvPositionInt);
            return;
        }

        //do we need an adult for this?
        if (!cSetting.INV_RESET_PASSWORD().isEmpty()) {
            cUserInterface.pShowpasswordDialog(getString(R.string.supervisor_password_header), getString(R.string.supervisor_password_text), false);
            return;
        }

        //Remove the enviroment
        this.mRemoveAdapterFromFragment();

    }

    //End Region Default Methods

    //Region iICSDefaultActivity defaults

    public  void pPasswordSuccess() {
        cBarcodeScan.pRegisterBarcodeReceiver(this.getClass().getSimpleName());
        this.mRemoveAdapterFromFragment();
    }

    public  void pPasswordCancelled() {
        cBarcodeScan.pRegisterBarcodeReceiver(this.getClass().getSimpleName());
        this.getInventoryorderBinAdapter().notifyItemChanged(this.positionSwiped);
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

        if (getView() != null) {
            this.recyclerViewInventoryBinsTotal = getView().findViewById(R.id.recyclerViewInventoryBinsTotal);
            this.imageCloseOrder = getView().findViewById(R.id.imageCloseOrder);
        }


    }

    @Override
    public void mFieldsInitialize() {

        this.imageCloseOrder.setVisibility(View.INVISIBLE);


        if (cInventoryorder.currentInventoryOrder.pGetBinsDoneFromDatabasObl().size() >0 && cInventoryorder.currentInventoryOrder.pGetBinsNotDoneFromDatabasObl().size() == 0 ) {
            this.imageCloseOrder.setVisibility(View.VISIBLE);
        }

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new cInventoryorderBinRecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(this.recyclerViewInventoryBinsTotal);


    }

    @Override
    public void mSetListeners() {
        this.mSetDoneListener();
    }

    //End Region iICSDefaultActivity defaults

    //Region Public Methods

    //End Region Public Methods

    //Region Private Methods

    private  void mGetData() {
        this.mFillRecycler(cInventoryorder.currentInventoryOrder.pGetBinsTotalFromDatabasObl());
    }

    private void mFillRecycler(List<cInventoryorderBin> pvDataObl) {

        if (pvDataObl.size() == 0) {
            this.mNoLinesAvailable(true);
            return;
        }

        this.mNoLinesAvailable(false);

        this.getInventoryorderBinAdapter().pFillData(pvDataObl);
        this.recyclerViewInventoryBinsTotal.setHasFixedSize(false);
        this.recyclerViewInventoryBinsTotal.setAdapter(this.getInventoryorderBinAdapter());
        this.recyclerViewInventoryBinsTotal.setLayoutManager(new LinearLayoutManager(cAppExtension.context));
        this. recyclerViewInventoryBinsTotal.setVisibility(View.VISIBLE);

        if (cAppExtension.activity instanceof  InventoryorderBinsActivity) {
            InventoryorderBinsActivity inventoryorderBinsActivity = (InventoryorderBinsActivity)cAppExtension.activity;
            inventoryorderBinsActivity.pChangeTabCounterText(cText.pIntToStringStr(cInventoryorder.currentInventoryOrder.pGetBinsTotalFromDatabasObl().size()));

        }
    }

    private void mSetDoneListener() {
        this.imageCloseOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (cAppExtension.activity instanceof  InventoryorderBinsActivity) {
                    InventoryorderBinsActivity inventoryorderBinsActivity = (InventoryorderBinsActivity)cAppExtension.activity;
                    inventoryorderBinsActivity.pHandleOrderCloseClick();

                }
            }
        });
    }

    private  void mRemoveAdapterFromFragment(){

        //remove the item from recyclerview
        cResult hulpRst = cInventoryorderBin.currentInventoryOrderBin.pResetRst();
        if (! hulpRst.resultBln) {
            cUserInterface.pDoExplodingScreen(hulpRst.messagesStr(),"",true,true);
            return;
        }

        //Renew data, so only current lines are shown
        this.mGetData();

        //Change counters
        if (cAppExtension.activity instanceof  InventoryorderBinsActivity) {
            InventoryorderBinsActivity inventoryorderBinsActivity = (InventoryorderBinsActivity)cAppExtension.activity;
            inventoryorderBinsActivity.pChangeTabCounterText(cText.pIntToStringStr(cInventoryorder.currentInventoryOrder.pGetBinsTotalFromDatabasObl().size()));
            inventoryorderBinsActivity.pChangeToolBarSubText(cAppExtension.activity.getString(R.string.items) + ' ' + cText.pDoubleToStringStr(cInventoryorder.currentInventoryOrder.pGetTotalItemCountDbl()));
        }

    }

    private  void mNoLinesAvailable(Boolean pvEnabledBln) {

        if (InventoryorderBinsActivity.currentBinFragment  instanceof InventoryBinsTotalFragment) {
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
            this.recyclerViewInventoryBinsTotal.setVisibility(View.INVISIBLE);

            //Show nothing there fragment
            FragmentTransaction fragmentTransaction = cAppExtension.fragmentManager.beginTransaction();
            NothingHereFragment fragment = new NothingHereFragment();
            fragmentTransaction.replace(R.id.fragmentInventoryorderBinsTotal, fragment);
            fragmentTransaction.commit();

            //Change tabcounter text
            if (cAppExtension.activity instanceof  InventoryorderBinsActivity) {
                InventoryorderBinsActivity inventoryorderBinsActivity = (InventoryorderBinsActivity) cAppExtension.activity;
                inventoryorderBinsActivity.pChangeTabCounterText( cText.pIntToStringStr(cInventoryorder.currentInventoryOrder.pGetBinsTotalFromDatabasObl().size()));
            }
        }
    }

    //End Region Private Methods

}
