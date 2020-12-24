package SSU_WHS.PackAndShip.PackAndShipBarcode;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ICS.cAppExtension;
import nl.icsvertex.scansuite.R;

public class cPackAndShipOrderBarcodeAdapter extends RecyclerView.Adapter<cPackAndShipOrderBarcodeAdapter.packAndShipBarcodeViewHolder>  {

    //Region Public Properties
    public static class packAndShipBarcodeViewHolder extends RecyclerView.ViewHolder{
        private final TextView textViewBarcode;
        public LinearLayout packAndShipBarcodeLinearLayout;

        public packAndShipBarcodeViewHolder(View pvItemView) {
            super(pvItemView);
            this.textViewBarcode = pvItemView.findViewById(R.id.textViewBarcode);

            this.textViewBarcode.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            this.textViewBarcode.setSingleLine(true);
            this.textViewBarcode.setMarqueeRepeatLimit(5);
            this.textViewBarcode.setSelected(true);
            this.packAndShipBarcodeLinearLayout = pvItemView.findViewById(R.id.packAndShipBarcodeLinearLayout);
        }
    }
    //End Region Public Properties

    //Region Private Properties
    private final LayoutInflater layoutInflaterObject;
    //End Region Private Properties

    //Region Constructor
    public cPackAndShipOrderBarcodeAdapter() {
        this.layoutInflaterObject = LayoutInflater.from(cAppExtension.context);
    }
    //End Region Constructor

    //Region Public Methods

    @NonNull
    @Override
    public packAndShipBarcodeViewHolder onCreateViewHolder(@NonNull ViewGroup pvParent, int pvViewTypeInt) {
        View itemView = this.layoutInflaterObject.inflate(R.layout.recycler_packandship_barcode, pvParent, false);
        return new packAndShipBarcodeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull packAndShipBarcodeViewHolder pvHolder, final int pvPositionInt) {


        cPackAndShipBarcode selectedPackAndShipBarcode = cPackAndShipBarcode.allPackAndShipOrderBarcodesObl.get(pvPositionInt);

        pvHolder.textViewBarcode.setText(selectedPackAndShipBarcode.getBarcodeStr());


        pvHolder.packAndShipBarcodeLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
    }

    @Override
    public int getItemCount () {

        if ( cPackAndShipBarcode.allPackAndShipOrderBarcodesObl != null) {
            return  cPackAndShipBarcode.allPackAndShipOrderBarcodesObl.size();
        }

        return 0;
    }

}
