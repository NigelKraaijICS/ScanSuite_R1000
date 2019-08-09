package SSU_WHS.PickorderLineBarcodes;

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
import nl.icsvertex.scansuite.R;


public class cPickorderLineBarcodeAdapter extends RecyclerView.Adapter<cPickorderLineBarcodeAdapter.PickorderLineBarcodeViewHolder>  {
    private Context callerContext;

    public class PickorderLineBarcodeViewHolder extends RecyclerView.ViewHolder{
        private TextView textViewPickorderLineBarcode;
        private TextView textViewPickorderLineBarcodeQuantity;
        public LinearLayout pickorderLineBarcodeItemLinearLayout;

        public PickorderLineBarcodeViewHolder(View itemView) {
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
    public static List<cPickorderLineBarcodeEntity> mPickorderLineBarcodes; //cached copy of pickorders

    public cPickorderLineBarcodeAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        callerContext = context;
    }

    @Override
    public cPickorderLineBarcodeAdapter.PickorderLineBarcodeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recycler_pickorderlinebarcode, parent, false);
        return new cPickorderLineBarcodeAdapter.PickorderLineBarcodeViewHolder(itemView);
    }

    public void setPickorderLineBarcodes(List<cPickorderLineBarcodeEntity> pickorderlinebarcodess) {
        mPickorderLineBarcodes = pickorderlinebarcodess;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(cPickorderLineBarcodeAdapter.PickorderLineBarcodeViewHolder holder, int position) {
        if (mPickorderLineBarcodes != null) {
            final cPickorderLineBarcodeEntity pickorderLineBarcodeEntity = mPickorderLineBarcodes.get(position);
            String barcode = pickorderLineBarcodeEntity.getBarcode();
            String quantity = Integer.toString(cText.stringToInteger(pickorderLineBarcodeEntity.getQuantityhandled())) ;

            holder.textViewPickorderLineBarcode.setText(barcode);
            holder.textViewPickorderLineBarcodeQuantity.setText(quantity);

//            holder.pickorderLineBarcodeItemLinearLayout.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (callerContext instanceof PickorderPickActivity) {
//                        ((PickorderPickActivity)callerContext).setChosenBarcode(pickorderLineBarcodeEntity);
//
//                    }
//                }
//            });
        }
    }

    @Override
    public int getItemCount () {
        if (mPickorderLineBarcodes != null)
            return mPickorderLineBarcodes.size();
        else return 0;
    }
}
