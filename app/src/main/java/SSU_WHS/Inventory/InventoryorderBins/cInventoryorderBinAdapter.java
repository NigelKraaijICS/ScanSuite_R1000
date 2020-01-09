package SSU_WHS.Inventory.InventoryorderBins;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ICS.Utils.cText;
import ICS.cAppExtension;
import SSU_WHS.Inventory.InventoryOrders.cInventoryorder;
import nl.icsvertex.scansuite.Activities.Inventory.InventoryorderBinsActivity;
import nl.icsvertex.scansuite.Fragments.Inventory.InventoryBinsDoneFragment;
import nl.icsvertex.scansuite.Fragments.Inventory.InventoryBinsToDoFragment;
import nl.icsvertex.scansuite.Fragments.Inventory.InventoryBinsTotalFragment;
import nl.icsvertex.scansuite.R;

public class cInventoryorderBinAdapter extends RecyclerView.Adapter<cInventoryorderBinAdapter.InventoryorderBinViewHolder> {
    //Region Public Properties

    public class InventoryorderBinViewHolder extends RecyclerView.ViewHolder{

        private View viewOrderStatus;
        private TextView textViewBin;
        private TextView textViewCounted;
        private TextView textViewLines;
        private ImageView imageBin;
        private ImageView imageChevronDown;

        public RelativeLayout viewBackground;
        public ConstraintLayout viewForeground;

        public InventoryorderBinViewHolder(View pvItemView) {
            super(pvItemView);
            this.viewOrderStatus = pvItemView.findViewById(R.id.viewOrderStatus);
            this.textViewBin = pvItemView.findViewById(R.id.textViewBin);
            this.textViewBin.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            this.textViewBin.setSingleLine(true);
            this.textViewBin.setMarqueeRepeatLimit(5);
            this.textViewBin.setSelected(true);
            this.textViewCounted = pvItemView.findViewById(R.id.textViewCounted);
            this.textViewLines = pvItemView.findViewById(R.id.textViewLines);
            this.imageBin = pvItemView.findViewById(R.id.imageBin);
            this.imageChevronDown  = pvItemView.findViewById(R.id.imageChevronDown);
            this.viewBackground = pvItemView.findViewById(R.id.view_background);
            this.viewForeground = pvItemView.findViewById(R.id.view_foreground);
        }
        //End Region Public Properties
    }

    //Region Private Properties
    private final LayoutInflater LayoutInflaterObject;
    private List<cInventoryorderBin> localInventoryorderBinObl;
    private RecyclerView thisRecyclerView;

    //End Region Private Properties

    //Region Constructor
    public cInventoryorderBinAdapter() {
        this.LayoutInflaterObject = LayoutInflater.from(cAppExtension.context);
    }
    //End Region Constructor


    //Region Default Methods
    @Override
    public cInventoryorderBinAdapter.InventoryorderBinViewHolder onCreateViewHolder(ViewGroup pvParent, int pbViewTypeInt) {
        View itemView = LayoutInflaterObject.inflate(R.layout.recycler_inventoryorderbin, pvParent, false);
        return new cInventoryorderBinAdapter.InventoryorderBinViewHolder(itemView);
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView pvRecyclerView) {
        this.thisRecyclerView = pvRecyclerView;
        super.onAttachedToRecyclerView( this.thisRecyclerView);
    }

    @Override
    public void onBindViewHolder(cInventoryorderBinAdapter.InventoryorderBinViewHolder pvHolder, final int pvPositionInt) {

        if (localInventoryorderBinObl == null || localInventoryorderBinObl.size() == 0 ) {
            return;
        }

       final  cInventoryorderBin inventoryorderBin = localInventoryorderBinObl.get(pvPositionInt);


        pvHolder.textViewBin.setText(inventoryorderBin.getBinCodeStr());
        pvHolder.textViewBin.setTag(inventoryorderBin.getBinCodeStr());
        String imageBinUniqueTag =  inventoryorderBin.getBinCodeStr() + "_IMG";
        pvHolder.imageBin.setTag(imageBinUniqueTag);
        pvHolder.textViewCounted.setText(cAppExtension.activity.getString(R.string.lines) + ' ' + cInventoryorder.currentInventoryOrder.pGetLinesForBinObl(inventoryorderBin.getBinCodeStr()).size());
        pvHolder.textViewLines.setText( cAppExtension.activity.getString(R.string.items)  + ' ' + cText.pDoubleToStringStr(cInventoryorder.currentInventoryOrder.pGetItemCountForBinDbl(inventoryorderBin.getBinCodeStr())));


        if (InventoryorderBinsActivity.currentBinFragment instanceof InventoryBinsToDoFragment) {
            pvHolder.imageChevronDown.setVisibility(View.VISIBLE);
        }

        if  (InventoryorderBinsActivity.currentBinFragment instanceof InventoryBinsDoneFragment) {
            pvHolder.imageChevronDown.setVisibility(View.INVISIBLE);
        }

        if  (InventoryorderBinsActivity.currentBinFragment instanceof InventoryBinsTotalFragment) {
            pvHolder.imageChevronDown.setVisibility(View.INVISIBLE);
        }

        pvHolder.viewForeground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cAppExtension.context instanceof InventoryorderBinsActivity) {
                    cInventoryorderBin.currentInventoryOrderBin = inventoryorderBin;
                   InventoryorderBinsActivity.pInventoryorderBinSelected();
                }
            }
        });
    }

    @Override
    public int getItemCount () {
        if (localInventoryorderBinObl != null)
            return localInventoryorderBinObl.size();
        else return 0;
    }

    //End Region Default Methods

    //Region Public Methods
    public void pFillData(List<cInventoryorderBin> pvDataObl) {
        localInventoryorderBinObl = pvDataObl;
        notifyDataSetChanged();
    }

    public void pSetFilter(String pvQueryTextStr) {
        localInventoryorderBinObl = this.mGetFilteredListObl(pvQueryTextStr);
        notifyDataSetChanged();
    }

    //End Region Public Methods


    //Region Private Methods
    private List<cInventoryorderBin> mGetFilteredListObl(String pvQueryTextStr) {

        pvQueryTextStr = pvQueryTextStr.toLowerCase();
        List<cInventoryorderBin> resultObl = new ArrayList<>();

        if (localInventoryorderBinObl == null || localInventoryorderBinObl.size() == 0) {
            return resultObl;
        }

        for (cInventoryorderBin inventoryorderbin:localInventoryorderBinObl)
        {
            if (inventoryorderbin.getBinCodeStr().toLowerCase().contains(pvQueryTextStr))
            {
                resultObl.add(inventoryorderbin);
            }
        }
        return resultObl;
    }

    //End Region Private Methods
}
