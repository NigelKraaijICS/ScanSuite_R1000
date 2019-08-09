package SSU_WHS.PickorderBarcodes;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import ICS.Utils.cText;
import nl.icsvertex.scansuite.activities.pick.PickorderPickActivity;
import nl.icsvertex.scansuite.R;

public class cPickorderBarcodeAdapter extends RecyclerView.Adapter<cPickorderBarcodeAdapter.PickorderBarcodeViewHolder>  {
    private Context callerContext;

    public class PickorderBarcodeViewHolder extends RecyclerView.ViewHolder{
        private TextView textViewPickorderLineBarcode;
        private TextView textViewPickorderLineBarcodeQuantity;
        public LinearLayout pickorderLineBarcodeItemLinearLayout;

        public PickorderBarcodeViewHolder(View itemView) {
            super(itemView);
            textViewPickorderLineBarcode = itemView.findViewById(R.id.textViewPickorderLineBarcode);
            textViewPickorderLineBarcode.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            textViewPickorderLineBarcode.setSingleLine(true);
            textViewPickorderLineBarcode.setMarqueeRepeatLimit(5);
            textViewPickorderLineBarcode.setSelected(true);
            textViewPickorderLineBarcodeQuantity = itemView.findViewById(R.id.textViewPickorderLineBarcodeQuantity);
            pickorderLineBarcodeItemLinearLayout = itemView.findViewById(R.id.pickorderLineBarcodeItemLinearLayout);
        }
    }

    private final LayoutInflater mInflater;
    public static List<cPickorderBarcodeEntity> mPickorderBarcodes; //cached copy of pickorders

    public cPickorderBarcodeAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        callerContext = context;
    }

    @Override
    public cPickorderBarcodeAdapter.PickorderBarcodeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recycler_pickorderlinebarcode, parent, false);
        return new cPickorderBarcodeAdapter.PickorderBarcodeViewHolder(itemView);
    }

    public void setPickorderBarcodes(List<cPickorderBarcodeEntity> pickorderbarcodess) {
        mPickorderBarcodes = pickorderbarcodess;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(cPickorderBarcodeAdapter.PickorderBarcodeViewHolder holder, int position) {
        if (mPickorderBarcodes != null) {
            final cPickorderBarcodeEntity pickorderBarcodeEntity = mPickorderBarcodes.get(position);
            String barcode = pickorderBarcodeEntity.getBarcode();
            String quantity = Integer.toString(cText.stringToInteger(pickorderBarcodeEntity.getQuantityperunitofmeasure())) ;

            holder.textViewPickorderLineBarcode.setText(barcode);
            holder.textViewPickorderLineBarcodeQuantity.setText(quantity);

            holder.pickorderLineBarcodeItemLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (callerContext instanceof PickorderPickActivity) {
                        ((PickorderPickActivity)callerContext).setChosenBarcode(pickorderBarcodeEntity);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount () {
        if (mPickorderBarcodes != null)
            return mPickorderBarcodes.size();
        else return 0;
    }
}
