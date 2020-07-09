package SSU_WHS.Basics.BinItem;

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

public class cBinItemAdapter extends RecyclerView.Adapter<cBinItemAdapter.BinItemViewHolder>{

    //Region Public Properties
    public static class BinItemViewHolder extends RecyclerView.ViewHolder{
        private TextView textViewItemNo;
        private TextView textViewVariantCode;
        private TextView textViewQuantity;
        public LinearLayout binItemItemLinearLayout;

        public BinItemViewHolder(View itemView) {
            super(itemView);

            this.binItemItemLinearLayout = itemView.findViewById(R.id.binItemItemLinearLayout);


            this.textViewItemNo = itemView.findViewById(R.id.textViewItemNo);
            this.textViewItemNo.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            this.textViewItemNo.setSingleLine(true);
            this.textViewItemNo.setMarqueeRepeatLimit(5);
            this.textViewItemNo.setSelected(true);

            this.textViewVariantCode = itemView.findViewById(R.id.textViewVariantCode);

            this.textViewQuantity = itemView.findViewById(R.id.textViewQuanitity);
        }
    }

    //End Region Public Properties

    //Region Constructor

    public cBinItemAdapter() {
        this.LayoutInflaterObject = LayoutInflater.from(cAppExtension.context);
    }

    //Region Private Properties
    private final LayoutInflater LayoutInflaterObject;
    //End Region Private Propertoes

    @NonNull
    @Override
    public cBinItemAdapter.BinItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = this.LayoutInflaterObject.inflate(R.layout.recycler_binitem, parent, false);
        return new BinItemViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull cBinItemAdapter.BinItemViewHolder holder, int position) {
        if (cBinItem.allBinItemsObl != null) {

            final cBinItem binItem = cBinItem.allBinItemsObl.get(position);

            holder.textViewItemNo.setText(binItem.getItemNoStr());
            holder.textViewVariantCode.setText(binItem.getVariantCodeStr());
            holder.textViewQuantity.setText(cText.pDoubleToStringStr(binItem.getStockDbl()));

            holder.binItemItemLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //Set the current binItem
                    cBinItem.currentBinIteme = binItem;

                }
            });
        }
    }

    @Override
    public int getItemCount () {
        if (cBinItem.allBinItemsObl != null)
            return cBinItem.allBinItemsObl.size();
        else return 0;
    }
}
