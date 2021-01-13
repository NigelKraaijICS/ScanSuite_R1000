package SSU_WHS.Inventory.InventoryorderBarcodes;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;

import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cText;
import ICS.cAppExtension;
import SSU_WHS.Inventory.InventoryorderLines.cInventoryorderLine;
import nl.icsvertex.scansuite.Activities.Inventory.InventoryArticleActivity;
import nl.icsvertex.scansuite.R;

public class cInventoryOrderBarcodeAdapter extends RecyclerView.Adapter<cInventoryOrderBarcodeAdapter.inventoryorderBarcodeViewHolder>  {


    //Region Public Properties
    public static class inventoryorderBarcodeViewHolder extends RecyclerView.ViewHolder{
        private final TextView textViewBarcode;
        private final TextView textViewQuantity;
        public LinearLayout barcodeItemLinearLayout;

        public inventoryorderBarcodeViewHolder(View pvItemView) {
            super(pvItemView);
            this.textViewBarcode = pvItemView.findViewById(R.id.textViewBarcode);

            this.textViewBarcode.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            this.textViewBarcode.setSingleLine(true);
            this.textViewBarcode.setMarqueeRepeatLimit(5);
            this.textViewBarcode.setSelected(true);
            this.textViewQuantity = pvItemView.findViewById(R.id.textViewQuantity);
            this.barcodeItemLinearLayout = pvItemView.findViewById(R.id.barcodeLinearLayout);
        }
    }
    //End Region Public Properties

    //Region Private Properties
    private final LayoutInflater layoutInflaterObject;
    //End Region Private Properties

    //Region Constructor
    public cInventoryOrderBarcodeAdapter() {
        this.layoutInflaterObject = LayoutInflater.from(cAppExtension.context);
    }
    //End Region Constructor

    //Region Public Methods

    @NonNull
    @Override
    public inventoryorderBarcodeViewHolder onCreateViewHolder(@NonNull ViewGroup pvParent, int pvViewTypeInt) {
        View itemView = this.layoutInflaterObject.inflate(R.layout.recycler_barcode, pvParent, false);
        return new inventoryorderBarcodeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull inventoryorderBarcodeViewHolder pvHolder, int pvPositionInt) {

       final cInventoryorderBarcode inventoryorderBarcode = cInventoryorderLine.currentInventoryOrderLine.barcodesObl().get(pvPositionInt);
        Objects.requireNonNull(pvHolder).textViewBarcode.setText(inventoryorderBarcode.getBarcodeStr());
        pvHolder.textViewQuantity.setText(cText.pDoubleToStringStr(inventoryorderBarcode.getQuantityPerUnitOfMeasureDbl()));

        pvHolder.barcodeItemLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cAppExtension.activity instanceof InventoryArticleActivity) {
                    InventoryArticleActivity inventoryArticleDetailActivity = (InventoryArticleActivity) cAppExtension.activity;
                    inventoryArticleDetailActivity.pHandleScan(cBarcodeScan.pFakeScan(inventoryorderBarcode.getBarcodeStr()));
                }
            }
        });
    }

    @Override
    public int getItemCount () {
            if (cInventoryorderLine.currentInventoryOrderLine != null &&cInventoryorderLine.currentInventoryOrderLine.barcodesObl() != null)
                return cInventoryorderLine.currentInventoryOrderLine.barcodesObl().size();
            else return 0;
    }
}
