package SSU_WHS.Inventory.InventoryorderLines;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
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
        private TextView textViewDescription;
        private TextView textViewCounted;

        public RelativeLayout viewBackground;
        public ConstraintLayout viewForeground;

        public InventoryorderLineViewHolder(View pvItemView) {
            super(pvItemView);
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

            this.textViewDescription = pvItemView.findViewById(R.id.textViewDescription);
            this.textViewDescription.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            this.textViewDescription.setSingleLine(true);
            this.textViewDescription.setMarqueeRepeatLimit(5);
            this.textViewDescription.postDelayed(new Runnable() {
                @Override
                public void run() {
                    textViewDescription.setSelected(true);
                }
            },1500);

            this.textViewCounted = pvItemView.findViewById(R.id.textViewCounted);
            this.viewBackground = pvItemView.findViewById(R.id.view_background);
            this.viewForeground = pvItemView.findViewById(R.id.view_foreground);
        }
        //End Region Public Properties
    }

    //Region Private Properties
    private final LayoutInflater LayoutInflaterObject;
    private static List<cInventoryorderLine> localInventoryorderLineObl;
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
        pvHolder.textViewDescription.setText(inventoryorderLine.getDescriptionStr());
        pvHolder.textViewDescription.setVisibility(View.VISIBLE);
        pvHolder.textViewCounted.setText(cText.pDoubleToStringStr(inventoryorderLine.getQuantityHandledDbl()));
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
        cInventoryorderLineAdapter.localInventoryorderLineObl = pvDataObl;
        notifyDataSetChanged();
    }

    public void pSetFilter(String pvQueryTextStr) {
        this. localInventoryorderLineObl = this.mGetFilteredListObl(pvQueryTextStr);
        notifyDataSetChanged();
    }

    public  void pRemoveItem(int pvPositionInt) {
        cInventoryorderLineAdapter.localInventoryorderLineObl.remove(cInventoryorderLine.currentInventoryOrderLine);
        notifyItemRemoved(pvPositionInt);
    }
    public  void pRestoreItem(int pvPositionInt) {
        cInventoryorderLineAdapter.localInventoryorderLineObl.add(pvPositionInt, cInventoryorderLine.currentInventoryOrderLine);

        notifyItemInserted(pvPositionInt);
    }


    //End Region Public Methods


    //Region Private Methods

    private List<cInventoryorderLine> mGetFilteredListObl(String pvQueryTextStr) {

        pvQueryTextStr = pvQueryTextStr.toLowerCase();
        List<cInventoryorderLine> resultObl = new ArrayList<>();

        if (this.localInventoryorderLineObl == null || this.localInventoryorderLineObl.size() == 0) {
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
