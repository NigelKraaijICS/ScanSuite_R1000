package SSU_WHS.Basics.ShippingAgentServiceShippingUnits;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ICS.Utils.cText;
import ICS.Utils.cUserInterface;
import nl.icsvertex.scansuite.activities.ship.ShipDetermineTransportActivity;
import nl.icsvertex.scansuite.R;

public class cShippingAgentServiceShippingUnitAdapter extends RecyclerView.Adapter<cShippingAgentServiceShippingUnitAdapter.ShippingAgentServiceShippingUnitViewHolder> {
    private Context callerContext;
    private List<LinearLayout> shippingUnitItemLinearLayouts = new ArrayList<>();
    private RecyclerView thisRecyclerView;


    class ShippingAgentServiceShippingUnitViewHolder extends RecyclerView.ViewHolder{
        LinearLayout shippingUnitItemLinearLayout;
        ImageView imageViewShippingUnit;
        TextView textViewDescription;
        TextView textViewShippingUnit;
        TextView textViewQuantityUsed;
        ConstraintLayout primaryContent;
        ConstraintLayout secondaryContent;
        AppCompatImageButton imageButtonMinus;
        AppCompatImageButton imageButtonPlus;
        AppCompatImageButton imageButtonZero;
        AppCompatImageView imageChevronDown;

        ShippingAgentServiceShippingUnitViewHolder(View itemView) {
        super(itemView);
        shippingUnitItemLinearLayout = itemView.findViewById(R.id.shippingUnitItemLinearLayout);
        primaryContent = itemView.findViewById(R.id.primaryContent);
        secondaryContent = itemView.findViewById(R.id.secondaryContent);
        secondaryContent.setVisibility(View.GONE);
        imageViewShippingUnit = itemView.findViewById(R.id.imageViewShippingUnit);
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
        imageButtonPlus = itemView.findViewById(R.id.imageButtonPlus);
        imageButtonMinus = itemView.findViewById(R.id.imageButtonMinus);
        imageButtonZero = itemView.findViewById(R.id.imageButtonZero);
        textViewQuantityUsed = itemView.findViewById(R.id.textViewQuantityUsed);
        imageChevronDown = itemView.findViewById(R.id.imageChevronDown);
    }
}

    private final LayoutInflater mInflater;
    public List<cShippingAgentServiceShippingUnitEntity> mShippingAgentServiceShippingUnits; //cached copy of pickorders

    public cShippingAgentServiceShippingUnitAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        callerContext = context;
    }

    @Override
    public cShippingAgentServiceShippingUnitAdapter.ShippingAgentServiceShippingUnitViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recycler_shippingunit, parent, false);
        return new cShippingAgentServiceShippingUnitAdapter.ShippingAgentServiceShippingUnitViewHolder(itemView);
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
    public void onBindViewHolder(final cShippingAgentServiceShippingUnitAdapter.ShippingAgentServiceShippingUnitViewHolder holder, final int position) {
        shippingUnitItemLinearLayouts.add(holder.shippingUnitItemLinearLayout);

        if (mShippingAgentServiceShippingUnits != null) {

            final cShippingAgentServiceShippingUnitEntity shippingUnitEntity = mShippingAgentServiceShippingUnits.get(position);
            holder.textViewDescription.setText(shippingUnitEntity.getDescription());
            holder.textViewShippingUnit.setText(shippingUnitEntity.getShippingunit());
            holder.textViewQuantityUsed.setText(Integer.toString(shippingUnitEntity.getShippingunitquantityused()));
            if (shippingUnitEntity.getShippingunit().equalsIgnoreCase(cShippingAgentServiceShippingUnit.SHIPPINGUNIT_BOX)) {
                holder.imageViewShippingUnit.setImageResource(R.drawable.ic_box);
            }
            if (shippingUnitEntity.getShippingunit().toLowerCase().contains(cShippingAgentServiceShippingUnit.SHIPPINGUNIT_BOX.toLowerCase())) {
                holder.imageViewShippingUnit.setImageResource(R.drawable.ic_box);
            }
            if (shippingUnitEntity.getShippingunit().equalsIgnoreCase(cShippingAgentServiceShippingUnit.SHIPPINGUNIT_PALLET)) {
                holder.imageViewShippingUnit.setImageResource(R.drawable.ic_pallet);
            }
            if (shippingUnitEntity.getShippingunit().equalsIgnoreCase(cShippingAgentServiceShippingUnit.SHIPPINGUNIT_CONTAINER)) {
                holder.imageViewShippingUnit.setImageResource(R.drawable.ic_container);
            }
            if (shippingUnitEntity.getShippingunit().equalsIgnoreCase(cShippingAgentServiceShippingUnit.SHIPPINGUNIT_LETTERBOX)) {
                holder.imageViewShippingUnit.setImageResource(R.drawable.ic_letterbox);
            }
            if (shippingUnitEntity.getShippingunit().equalsIgnoreCase(cShippingAgentServiceShippingUnit.SHIPPINGUNIT_HANGING)) {
                holder.imageViewShippingUnit.setImageResource(R.drawable.ic_hanging);
            }
            holder.primaryContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    Animation animation = AnimationUtils.loadAnimation(callerContext.getApplicationContext(), R.anim.rotate_180);
                    //Close all others
                    for (LinearLayout aLayout : shippingUnitItemLinearLayouts) {
                        ConstraintLayout secondaryLayout = aLayout.findViewById(R.id.secondaryContent);
                        ConstraintLayout primaryLayout = aLayout.findViewById(R.id.primaryContent);
                        if (secondaryLayout != null) {
                            if (primaryLayout != view) {
                                if (secondaryLayout.getVisibility() == View.VISIBLE) {
                                    ImageView chevronImage = primaryLayout.findViewById(R.id.imageChevronDown);
                                    if (chevronImage != null) {
                                        chevronImage.animate().rotation(0).start();
                                    }
                                }
                                secondaryLayout.animate().scaleY(0).start();
                                secondaryLayout.setVisibility(View.GONE);
                            }
                        }
                    }
                    Boolean isExpanded;
                    if (holder.secondaryContent.getVisibility() == View.VISIBLE) {
                        isExpanded = true;
                    }
                    else {
                        isExpanded = false;
                    }
                    if (isExpanded) {
                        holder.imageChevronDown.animate().rotation(0).start();
                        holder.secondaryContent.animate().scaleY(0).start();
                        holder.secondaryContent.setVisibility(View.GONE);
                    }
                    else {
                        //package + 1
                        holder.imageButtonPlus.callOnClick();
                        holder.imageChevronDown.animate().rotation(180).start();
                        holder.secondaryContent.animate().scaleY(1).start();
                        holder.secondaryContent.setVisibility(View.VISIBLE);
                    }
                    //we want this to animate, but it does nasty things to the quantity textview
                    //notifyItemChanged(position);
                }
            });
            holder.imageButtonPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Integer currentQuantity = cText.stringToInteger(holder.textViewQuantityUsed.getText().toString());
                    Integer newQuantity = currentQuantity + 1;
                    holder.textViewQuantityUsed.setText(Integer.toString(newQuantity));
                    mUpdateNumber(newQuantity, shippingUnitEntity);
                }
            });
            holder.imageButtonMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Integer currentQuantity = cText.stringToInteger(holder.textViewQuantityUsed.getText().toString());
                    if (currentQuantity == 0) {
                        cUserInterface.doNope(holder.textViewQuantityUsed, true, false);
                        return;
                    }
                    Integer newQuantity = currentQuantity - 1;
                    holder.textViewQuantityUsed.setText(Integer.toString(newQuantity));
                    mUpdateNumber(newQuantity, shippingUnitEntity);
                }
            });
            holder.imageButtonZero.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Integer newQuantity = 0;
                    holder.textViewQuantityUsed.setText(Integer.toString(newQuantity));
                    mUpdateNumber(newQuantity, shippingUnitEntity);
                }
            });
        }
    }
    private void mUpdateNumber(Integer newQuantity, cShippingAgentServiceShippingUnitEntity shippingAgentServiceShippingUnit) {
        if (callerContext instanceof ShipDetermineTransportActivity) {
            ((ShipDetermineTransportActivity) callerContext).updateShippingUnitUsed(newQuantity, shippingAgentServiceShippingUnit);
        }
    }
    @Override
    public int getItemCount () {
        if (mShippingAgentServiceShippingUnits != null)
            return mShippingAgentServiceShippingUnits.size();
        else return 0;
    }
}
