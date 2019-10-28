package SSU_WHS.Inventory.InventoryorderBins;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ICS.cAppExtension;
import SSU_WHS.Inventory.InventoryOrders.cInventoryorder;
import nl.icsvertex.scansuite.Activities.inventory.InventoryorderBinsActivity;
import nl.icsvertex.scansuite.R;

public class cInventoryorderBinAdapter extends RecyclerView.Adapter<cInventoryorderBinAdapter.InventoryorderBinViewHolder> {
    //Region Public Properties

    public class InventoryorderBinViewHolder extends RecyclerView.ViewHolder{

        private View viewOrderStatus;
        private TextView textViewBin;
        private TextView textViewCounted;
        private TextView textViewLines;
        private ImageView imageBin;
        public LinearLayout inventoryorderBinItemLinearLayout;

        public InventoryorderBinViewHolder(View pvItemView) {
            super(pvItemView);
            this.inventoryorderBinItemLinearLayout = pvItemView.findViewById(R.id.inventoryorderBinItemLinearLayout);
            this.viewOrderStatus = pvItemView.findViewById(R.id.viewOrderStatus);
            this.textViewBin = pvItemView.findViewById(R.id.textViewBin);
            this.textViewBin.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            this.textViewBin.setSingleLine(true);
            this.textViewBin.setMarqueeRepeatLimit(5);
            this.textViewBin.setSelected(true);
            this.textViewCounted = pvItemView.findViewById(R.id.textViewCounted);
            this.textViewLines = pvItemView.findViewById(R.id.textViewLines);
            this.imageBin = pvItemView.findViewById(R.id.imageBin);
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
    public void onBindViewHolder(cInventoryorderBinAdapter.InventoryorderBinViewHolder pvHolder, int pvPositionInt) {

        if (localInventoryorderBinObl == null || localInventoryorderBinObl.size() == 0 ) {
            return;
        }

        cInventoryorderBin.currentInventoryOrderBin = localInventoryorderBinObl.get(pvPositionInt);

        pvHolder.textViewBin.setText( cInventoryorderBin.currentInventoryOrderBin.getBinCodeStr());
        pvHolder.textViewBin.setTag( cInventoryorderBin.currentInventoryOrderBin.getBinCodeStr());
        String imageBinUniqueTag =  cInventoryorderBin.currentInventoryOrderBin.getBinCodeStr() + "_IMG";
        pvHolder.imageBin.setTag(imageBinUniqueTag);
        pvHolder.textViewCounted.setText(cAppExtension.activity.getString(R.string.lines) + ' ' + cInventoryorderBin.currentInventoryOrderBin.getLinesInt());
        pvHolder.textViewLines.setText( cAppExtension.activity.getString(R.string.items)  + ' ' +  cInventoryorder.currentInventoryOrder.pGetCountForBinDbl(cInventoryorderBin.currentInventoryOrderBin.getBinCodeStr()));

        pvHolder.inventoryorderBinItemLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cAppExtension.context instanceof InventoryorderBinsActivity) {
                    if (thisRecyclerView.getId() == R.id.recyclerViewInventoryBinsToDo) {
                        InventoryorderBinsActivity.pInventoryorderBinSelected();
                    }
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
