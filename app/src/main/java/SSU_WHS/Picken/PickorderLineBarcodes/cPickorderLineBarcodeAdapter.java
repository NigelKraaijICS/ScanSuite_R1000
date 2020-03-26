package SSU_WHS.Picken.PickorderLineBarcodes;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ICS.Utils.cText;
import ICS.cAppExtension;
import nl.icsvertex.scansuite.R;


public class cPickorderLineBarcodeAdapter extends RecyclerView.Adapter<cPickorderLineBarcodeAdapter.pickorderLineBarcodeViewHolder>  {

    //Public Properties
    public class pickorderLineBarcodeViewHolder extends RecyclerView.ViewHolder{
        private TextView textViewPickorderLineBarcode;
        private TextView textViewPickorderLineBarcodeQuantity;
        public LinearLayout pickorderLineBarcodeItemLinearLayout;

        public pickorderLineBarcodeViewHolder(View itemView) {
            super(itemView);
            this.textViewPickorderLineBarcode = itemView.findViewById(R.id.textViewBarcode);
            this.textViewPickorderLineBarcode.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            this.textViewPickorderLineBarcode.setSingleLine(true);
            this.textViewPickorderLineBarcode.setMarqueeRepeatLimit(5);
            this.textViewPickorderLineBarcode.setSelected(true);
            this.textViewPickorderLineBarcodeQuantity = itemView.findViewById(R.id.textViewQuantity);
            this.pickorderLineBarcodeItemLinearLayout = itemView.findViewById(R.id.barcodeLinearLayout);
        }
    }
    //End Public Properties

    //Private Properties
    private LayoutInflater layoutInflaterObject;
    //End Private Properties

    public cPickorderLineBarcodeAdapter() {
        this.layoutInflaterObject = LayoutInflater.from(cAppExtension.context);
    }

    @NonNull
    @Override
    public pickorderLineBarcodeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = this.layoutInflaterObject.inflate(R.layout.recycler_barcode, parent, false);
        return new pickorderLineBarcodeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull pickorderLineBarcodeViewHolder holder, int position) {
        if (cPickorderLineBarcode.allLineBarcodesObl != null) {
            final cPickorderLineBarcode pickorderLineBarcode = cPickorderLineBarcode.allLineBarcodesObl.get(position);
            final String barcodeStr = pickorderLineBarcode.getBarcodeStr();
            String quantity = cText.pDoubleToStringStr(pickorderLineBarcode.getQuantityhandledDbl());

            holder.textViewPickorderLineBarcode.setText(barcodeStr);
            holder.textViewPickorderLineBarcodeQuantity.setText(quantity);

            //todo: this is not used
//            holder.pickorderLineBarcodeItemLinearLayout.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mBarcodeSelected(barcodeStr);
//                }
//            });
        }
    }

    @Override
    public int getItemCount () {
        if (cPickorderLineBarcode.allLineBarcodesObl != null)
            return cPickorderLineBarcode.allLineBarcodesObl.size();
        else return 0;
    }


}
