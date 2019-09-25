package SSU_WHS.Basics.ShippingAgentServiceShippingUnits;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import nl.icsvertex.scansuite.R;

public class cShippingAgentServiceShippingUnitUsedAdapter extends RecyclerView.Adapter<cShippingAgentServiceShippingUnitUsedAdapter.ShippingAgentServiceShippingUnitUsedViewHolder> {
    private Context callerContext;
    private List<LinearLayout> shippingUnitItemLinearLayouts = new ArrayList<>();
    private RecyclerView thisRecyclerView;


    class ShippingAgentServiceShippingUnitUsedViewHolder extends RecyclerView.ViewHolder{
        LinearLayout shippingUnitUsedItemLinearLayout;
        TextView textViewDescription;
        TextView textViewShippingUnit;
        TextView textViewQuantityUsed;
        ImageView usedUnitsImage;

        ShippingAgentServiceShippingUnitUsedViewHolder(View itemView) {
        super(itemView);
        shippingUnitUsedItemLinearLayout = itemView.findViewById(R.id.shippingUnitUsedItemLinearLayout);
        usedUnitsImage = itemView.findViewById(R.id.usedUnitsImage);
        textViewDescription = itemView.findViewById(R.id.textViewDescription);
        textViewDescription.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        textViewDescription.setSingleLine(true);
        textViewDescription.setMarqueeRepeatLimit(5);
        textViewDescription.setSelected(true);
        textViewShippingUnit = itemView.findViewById(R.id.textViewShippingUnit);
        textViewShippingUnit.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        textViewShippingUnit.setSingleLine(true);
        textViewShippingUnit.setMarqueeRepeatLimit(5);
        textViewShippingUnit.setSelected(true);
        textViewQuantityUsed = itemView.findViewById(R.id.textViewQuantityUsed);
    }
}

    private final LayoutInflater mInflater;
    public List<cShippingAgentServiceShippingUnitEntity> mShippingAgentServiceShippingUnits; //cached copy of pickorders

    public cShippingAgentServiceShippingUnitUsedAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        callerContext = context;
    }

    @Override
    public cShippingAgentServiceShippingUnitUsedAdapter.ShippingAgentServiceShippingUnitUsedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recycler_shippingunit_used, parent, false);
        return new cShippingAgentServiceShippingUnitUsedAdapter.ShippingAgentServiceShippingUnitUsedViewHolder(itemView);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        thisRecyclerView = recyclerView;
    }
    public void setShippingUnits(List<cShippingAgentServiceShippingUnitEntity> shippingUnits) {
        mShippingAgentServiceShippingUnits = shippingUnits;
        //notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(final cShippingAgentServiceShippingUnitUsedAdapter.ShippingAgentServiceShippingUnitUsedViewHolder holder, final int position) {
        shippingUnitItemLinearLayouts.add(holder.shippingUnitUsedItemLinearLayout);

        if (mShippingAgentServiceShippingUnits != null) {

            final cShippingAgentServiceShippingUnitEntity shippingUnitEntity = mShippingAgentServiceShippingUnits.get(position);
            holder.textViewDescription.setText(shippingUnitEntity.getDescriptionStr());
            holder.textViewShippingUnit.setText(shippingUnitEntity.getShippingunitStr());
            holder.textViewQuantityUsed.setText(Integer.toString(shippingUnitEntity.getShippingUnitQuantityusedInt()));
            if (shippingUnitEntity.getShippingunitStr().equalsIgnoreCase(cShippingAgentServiceShippingUnit.SHIPPINGUNIT_BOX)) {
                holder.usedUnitsImage.setImageResource(R.drawable.ic_box);
            }
            if (shippingUnitEntity.getShippingunitStr().toLowerCase().contains(cShippingAgentServiceShippingUnit.SHIPPINGUNIT_BOX.toLowerCase())) {
                holder.usedUnitsImage.setImageResource(R.drawable.ic_box);
            }
            if (shippingUnitEntity.getShippingunitStr().equalsIgnoreCase(cShippingAgentServiceShippingUnit.SHIPPINGUNIT_PALLET)) {
                holder.usedUnitsImage.setImageResource(R.drawable.ic_pallet);
            }
            if (shippingUnitEntity.getShippingunitStr().equalsIgnoreCase(cShippingAgentServiceShippingUnit.SHIPPINGUNIT_CONTAINER)) {
                holder.usedUnitsImage.setImageResource(R.drawable.ic_container);
            }
            if (shippingUnitEntity.getShippingunitStr().equalsIgnoreCase(cShippingAgentServiceShippingUnit.SHIPPINGUNIT_LETTERBOX)) {
                holder.usedUnitsImage.setImageResource(R.drawable.ic_letterbox);
            }
            if (shippingUnitEntity.getShippingunitStr().equalsIgnoreCase(cShippingAgentServiceShippingUnit.SHIPPINGUNIT_HANGING)) {
                holder.usedUnitsImage.setImageResource(R.drawable.ic_hanging);
            }
        }
    }
    @Override
    public int getItemCount () {
        if (mShippingAgentServiceShippingUnits != null)
            return mShippingAgentServiceShippingUnits.size();
        else return 0;
    }
}
