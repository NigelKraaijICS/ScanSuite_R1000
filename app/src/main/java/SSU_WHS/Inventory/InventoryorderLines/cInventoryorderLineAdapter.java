package SSU_WHS.Inventory.InventoryorderLines;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ICS.Utils.cText;
import ICS.cAppExtension;
import nl.icsvertex.scansuite.Activities.inventory.InventoryorderBinActivity;
import nl.icsvertex.scansuite.R;

public class cInventoryorderLineAdapter extends RecyclerView.Adapter<cInventoryorderLineAdapter.InventoryorderLineViewHolder> {
    //Region Public Properties

    public class InventoryorderLineViewHolder extends RecyclerView.ViewHolder{

        private TextView textViewArticle;
        private TextView textViewVendorItemNo;
        private TextView textViewCounted;
        public LinearLayout inventoryorderBinItemLinearLayout;

        public InventoryorderLineViewHolder(View pvItemView) {
            super(pvItemView);
            this.inventoryorderBinItemLinearLayout = pvItemView.findViewById(R.id.inventoryorderLineItemLinearLayout);

            this.textViewArticle = pvItemView.findViewById(R.id.textViewArticle);
            this.textViewArticle.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            this.textViewArticle.setSingleLine(true);
            this.textViewArticle.setMarqueeRepeatLimit(5);
            this.textViewArticle.postDelayed(new Runnable() {
                @Override
                public void run() {
                    textViewArticle.setSelected(true);
                }
            },1500);

            this.textViewVendorItemNo = pvItemView.findViewById(R.id.textViewVendorItemNo);
            this.textViewVendorItemNo.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            this.textViewVendorItemNo.setSingleLine(true);
            this.textViewVendorItemNo.setMarqueeRepeatLimit(5);
            this.textViewVendorItemNo.postDelayed(new Runnable() {
                @Override
                public void run() {
                    textViewVendorItemNo.setSelected(true);
                }
            },1500);

            this.textViewCounted = pvItemView.findViewById(R.id.textViewCounted);
        }
        //End Region Public Properties
    }

    //Region Private Properties
    private final LayoutInflater LayoutInflaterObject;
    private List<cInventoryorderLine> localInventoryorderLineObl;
    private RecyclerView thisRecyclerView;

    //End Region Private Properties

    //Region Constructor
    public cInventoryorderLineAdapter() {
        this.LayoutInflaterObject = LayoutInflater.from(cAppExtension.context);
    }
    //End Region Constructor


    //Region Default Methods
    @Override
    public cInventoryorderLineAdapter.InventoryorderLineViewHolder onCreateViewHolder(ViewGroup pvParent, int pbViewTypeInt) {
        View itemView = LayoutInflaterObject.inflate(R.layout.recycler_inventoryorderline, pvParent, false);
        return new cInventoryorderLineAdapter.InventoryorderLineViewHolder(itemView);
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView pvRecyclerView) {
        this.thisRecyclerView = pvRecyclerView;
        super.onAttachedToRecyclerView( this.thisRecyclerView);
    }

    @Override
    public void onBindViewHolder(cInventoryorderLineAdapter.InventoryorderLineViewHolder pvHolder, int pvPositionInt) {

        if (localInventoryorderLineObl == null || localInventoryorderLineObl.size() == 0 ) {
            return;
        }

        final cInventoryorderLine inventoryorderLine = localInventoryorderLineObl.get(pvPositionInt);

        pvHolder.textViewArticle.setText(inventoryorderLine.getItemNoStr() + " " + inventoryorderLine.getVariantCodeStr());
        pvHolder.textViewVendorItemNo.setText(inventoryorderLine.getDescriptionStr());
        pvHolder.textViewVendorItemNo.setVisibility(View.VISIBLE);
        pvHolder.textViewCounted.setText(cText.pDoubleToStringStr(inventoryorderLine.getQuantityHandledDbl()));

        pvHolder.inventoryorderBinItemLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cAppExtension.context instanceof InventoryorderBinActivity) {
                        cInventoryorderLine.currentInventoryOrderLine = inventoryorderLine;
                        InventoryorderBinActivity.pLineSelected();
                }
            }
        });

    }

    @Override
    public int getItemCount () {
        if (localInventoryorderLineObl != null)
            return localInventoryorderLineObl.size();
        else return 0;
    }

    //End Region Default Methods

    //Region Public Methods
    public void pFillData(List<cInventoryorderLine> pvDataObl) {
        localInventoryorderLineObl = pvDataObl;
        notifyDataSetChanged();
    }

    public void pSetFilter(String pvQueryTextStr) {
        localInventoryorderLineObl = this.mGetFilteredListObl(pvQueryTextStr);
        notifyDataSetChanged();
    }

    //End Region Public Methods


    //Region Private Methods
    private List<cInventoryorderLine> mGetFilteredListObl(String pvQueryTextStr) {

        pvQueryTextStr = pvQueryTextStr.toLowerCase();
        List<cInventoryorderLine> resultObl = new ArrayList<>();

        if (localInventoryorderLineObl == null || localInventoryorderLineObl.size() == 0) {
            return resultObl;
        }

        for (cInventoryorderLine inventoryorderline:localInventoryorderLineObl)
        {
            if (inventoryorderline.getVariantCodeStr().toLowerCase().contains(pvQueryTextStr) ||
                inventoryorderline.getDescriptionStr().toLowerCase().contains(pvQueryTextStr))
            {
                resultObl.add(inventoryorderline);
            }
        }
        return resultObl;
    }
    //End Region Private Methods
}
