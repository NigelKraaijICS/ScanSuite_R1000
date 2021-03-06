package SSU_WHS.Inventory.InventoryorderLines;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ICS.Utils.cText;
import ICS.cAppExtension;
import nl.icsvertex.scansuite.R;

public class cInventoryorderLineAdapter extends RecyclerView.Adapter<cInventoryorderLineAdapter.InventoryorderLineViewHolder> {

    //Region Public Properties

    public static class InventoryorderLineViewHolder extends RecyclerView.ViewHolder{

        private TextView textViewArticle;
        private TextView textViewDescription;
        private TextView textViewCounted;
        private ImageView imageBarcode;
        private TextView textViewBarcode;
        private FrameLayout inventoryorderLineItemLinearLayout;

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
            this.imageBarcode = pvItemView.findViewById(R.id.imageViewBarcode);
            this.textViewBarcode = pvItemView.findViewById(R.id.textViewBarcode);
            this.viewBackground = pvItemView.findViewById(R.id.view_background);
            this.viewForeground = pvItemView.findViewById(R.id.view_foreground);
            this.inventoryorderLineItemLinearLayout = pvItemView.findViewById(R.id.moveOrderLineItemLinearLayout);
        }
        //End Region Public Properties
    }

    //Region Private Properties
    private final LayoutInflater LayoutInflaterObject;
    private List<cInventoryorderLine> localInventoryorderLineObl;

    private final List<FrameLayout> inventoryOrderLineItemLinearLayouts = new ArrayList<>();

    //End Region Private Properties

    //Region Constructor
    public cInventoryorderLineAdapter() {
        this.LayoutInflaterObject = LayoutInflater.from(cAppExtension.context);
    }
    //End Region Constructor


    //Region Default Methods
    @NonNull
    @Override
    public cInventoryorderLineAdapter.InventoryorderLineViewHolder onCreateViewHolder(@NonNull ViewGroup pvParent, int pbViewTypeInt) {
        View itemView = LayoutInflaterObject.inflate(R.layout.recycler_inventoryorderline, pvParent, false);
        return new InventoryorderLineViewHolder(itemView);
    }
    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView pvRecyclerView) {
        super.onAttachedToRecyclerView(pvRecyclerView);
    }

    @Override
    public void onBindViewHolder(@NonNull cInventoryorderLineAdapter.InventoryorderLineViewHolder pvHolder, final int pvPositionInt) {

        this.inventoryOrderLineItemLinearLayouts.add(pvHolder.inventoryorderLineItemLinearLayout);

        if (localInventoryorderLineObl == null || localInventoryorderLineObl.size() == 0 ) {
            return;
        }

        final cInventoryorderLine inventoryorderLine = localInventoryorderLineObl.get(pvPositionInt);


        pvHolder.textViewArticle.setText(inventoryorderLine.getItemNoAndVariantCodeStr());
        pvHolder.textViewDescription.setText(inventoryorderLine.getDescriptionStr());
        pvHolder.textViewDescription.setVisibility(View.VISIBLE);
        pvHolder.textViewCounted.setText(cText.pDoubleToStringStr(inventoryorderLine.getQuantityHandledDbl()));

        if (inventoryorderLine.lineBarcodesObl() == null || inventoryorderLine.lineBarcodesObl().size() == 0) {
            pvHolder.imageBarcode.setVisibility(View.GONE);
            pvHolder.textViewBarcode.setVisibility(View.GONE);
        } else {

            if (inventoryorderLine.lineBarcodesObl().size() == 1) {
                pvHolder.imageBarcode.setVisibility(View.VISIBLE);
                pvHolder.textViewBarcode.setVisibility(View.VISIBLE);
                pvHolder.textViewBarcode.setText(inventoryorderLine.lineBarcodesObl().get(0).getBarcodeStr());
            }
            else {
                pvHolder.imageBarcode.setVisibility(View.VISIBLE);
                pvHolder.textViewBarcode.setVisibility(View.VISIBLE);
                pvHolder.textViewBarcode.setText(cAppExtension.activity.getString(R.string.multiple_barcodes));
            }

        }

        pvHolder.inventoryorderLineItemLinearLayout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View pvView){
                for (FrameLayout frameLayout : inventoryOrderLineItemLinearLayouts){
                    frameLayout.setSelected(false);
                }
                pvView.setSelected(true);
                cInventoryorderLine.currentInventoryOrderLine = inventoryorderLine;
            }});
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
        this.localInventoryorderLineObl = pvDataObl;
        notifyDataSetChanged();
    }

    public void pSetFilter(String pvQueryTextStr) {
        this. localInventoryorderLineObl = this.mGetFilteredListObl(pvQueryTextStr);
        notifyDataSetChanged();
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
