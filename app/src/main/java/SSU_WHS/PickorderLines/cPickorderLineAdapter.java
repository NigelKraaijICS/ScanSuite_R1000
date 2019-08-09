package SSU_WHS.PickorderLines;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

//import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;
import nl.icsvertex.scansuite.activities.pick.PickorderLinesActivity;
import nl.icsvertex.scansuite.R;
import nl.icsvertex.scansuite.activities.ship.ShiporderLinesActivity;
import nl.icsvertex.scansuite.activities.sort.SortorderLinesActivity;

public class cPickorderLineAdapter extends RecyclerView.Adapter<cPickorderLineAdapter.PickorderLineViewHolder>  {
    private Context callerContext;
    private List<LinearLayout> pickorderLineItemLinearLayouts = new ArrayList<>();
    private RecyclerView thisRecyclerView;
    public class PickorderLineViewHolder extends RecyclerView.ViewHolder{
        private TextView textViewPickorderLineLocation;
        private TextView textViewPickorderLine;
        private TextView textViewPickorderLineQuantity;
        private TextView textViewPickorderLineSourceNo;
        private ImageView imageViewPickorderLine;
        public LinearLayout pickorderLineItemLinearLayout;


        public PickorderLineViewHolder(View itemView) {
            super(itemView);
            textViewPickorderLineLocation = itemView.findViewById(R.id.textViewPickorderLineLocation);
            textViewPickorderLineLocation.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            textViewPickorderLineLocation.setSingleLine(true);
            textViewPickorderLineLocation.setMarqueeRepeatLimit(5);
            textViewPickorderLineLocation.setSelected(true);
            textViewPickorderLine = itemView.findViewById(R.id.textViewPickorderLine);
            textViewPickorderLine.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            textViewPickorderLine.setSingleLine(true);
            textViewPickorderLine.setMarqueeRepeatLimit(5);
            textViewPickorderLine.setSelected(true);
            textViewPickorderLineSourceNo = itemView.findViewById(R.id.textViewPickorderLineSourceNo);
            textViewPickorderLineSourceNo.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            textViewPickorderLineSourceNo.setSingleLine(true);
            textViewPickorderLineSourceNo.setMarqueeRepeatLimit(5);
            textViewPickorderLineSourceNo.setSelected(true);
            textViewPickorderLineQuantity = itemView.findViewById(R.id.textViewPickorderLineQuantity);
            imageViewPickorderLine = itemView.findViewById(R.id.imageViewPickorderLine);
            pickorderLineItemLinearLayout = itemView.findViewById(R.id.pickorderLineItemLinearLayout);
        }
    }

    private final LayoutInflater mInflater;
    public List<cPickorderLineEntity> mPickorderLines; //cached copy of pickorders
    public List<cPickorderLineEntity> mAllPickorderLines; //cached copy of pickorders


    public cPickorderLineAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        callerContext = context;
    }

    @Override
    public cPickorderLineAdapter.PickorderLineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recycler_pickorderline, parent, false);
        return new cPickorderLineAdapter.PickorderLineViewHolder(itemView);
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        thisRecyclerView = recyclerView;
    }
    public void setPickorderLines(List<cPickorderLineEntity> pickorderlines) {
        mAllPickorderLines = pickorderlines;
        mPickorderLines = pickorderlines;
        //notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(cPickorderLineAdapter.PickorderLineViewHolder holder, final int position) {
        //if (!pickorderLineItemLinearLayouts.contains(holder.pickorderLineItemLinearLayout)) {
            pickorderLineItemLinearLayouts.add(holder.pickorderLineItemLinearLayout);
        //}

        if (mPickorderLines != null) {
            final cPickorderLineEntity l_PickorderLineEntity = mPickorderLines.get(position);
            String l_lineDescriptionStr = l_PickorderLineEntity.getItemno() + "~" + l_PickorderLineEntity.getVariantcode() + ": " + l_PickorderLineEntity.getDescription();
            String quantityFieldToShow;
            Double l_quantityDbl = l_PickorderLineEntity.getQuantity();
            Double l_quantityPickedDbl = l_PickorderLineEntity.getQuantityhandled();

            quantityFieldToShow = l_quantityPickedDbl.intValue() + "/" + l_quantityDbl.intValue();

            String l_locationStr = l_PickorderLineEntity.getBincode().trim();
            String l_sourcenoStr = l_PickorderLineEntity.getSourceno().trim();

            holder.textViewPickorderLineLocation.setText(l_locationStr);
            holder.textViewPickorderLine.setText(l_lineDescriptionStr);
            holder.textViewPickorderLineQuantity.setText(quantityFieldToShow);
            holder.textViewPickorderLineSourceNo.setText(l_sourcenoStr);

            final int id = thisRecyclerView.getId();
            holder.pickorderLineItemLinearLayout.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    //deselect all
                    for (LinearLayout aLayout : pickorderLineItemLinearLayouts) {
                        aLayout.setSelected(false);
                    }
                    //select current
                    v.setSelected(true);
                    //wich fragment?
                    if (id == R.id.recyclerViewPickorderLinesTopick) {
                        if (callerContext instanceof PickorderLinesActivity) {
                            ((PickorderLinesActivity) callerContext).setChosenPickorderLine(l_PickorderLineEntity);
                        }
                    }
                    if (id == R.id.recyclerViewPickorderLinesPicked) {
                        if (callerContext instanceof PickorderLinesActivity) {
                            ((PickorderLinesActivity) callerContext).setChosenPickorderLineToReset(l_PickorderLineEntity);
                        }
                    }
                    if (id == R.id.recyclerViewSortorderLinesTosort) {
                        if (callerContext instanceof SortorderLinesActivity) {
                            ((SortorderLinesActivity) callerContext).setChosenSortorderLine(l_PickorderLineEntity);
                        }
                    }
                    if (id == R.id.recyclerViewSortorderLinesSorted) {
                        if (callerContext instanceof SortorderLinesActivity) {
                            ((SortorderLinesActivity) callerContext).setChosenSortorderLineToReset(l_PickorderLineEntity);
                        }
                    }

                }

            });


            if (id == R.id.recyclerViewPickorderLinesTopick) {
                //select first one
                if (callerContext instanceof PickorderLinesActivity) {
                    if (mPickorderLines.size() > 0) {
                        if (position ==0) {
                            holder.pickorderLineItemLinearLayout.setSelected(true);
                        }
                        final cPickorderLineEntity l_firstPickorderLineEntity = mPickorderLines.get(0);
                        ((PickorderLinesActivity)callerContext).setChosenPickorderLine(l_firstPickorderLineEntity);
                    }
                }
            }
            if (id == R.id.recyclerViewPickorderLinesPicked) {
                //select first one
                if (callerContext instanceof PickorderLinesActivity) {
                    if (mPickorderLines.size() > 0) {
                        if (position ==0) {
                            holder.pickorderLineItemLinearLayout.setSelected(true);
                        }
                        final cPickorderLineEntity l_firstPickorderLineEntity = mPickorderLines.get(0);
                        ((PickorderLinesActivity)callerContext).setChosenPickorderLineToReset(l_firstPickorderLineEntity);
                    }
                }
            }
            if (id == R.id.recyclerViewSortorderLinesTosort) {
                //select first one
                holder.textViewPickorderLineLocation.setVisibility(View.GONE);
                if (callerContext instanceof SortorderLinesActivity) {
                    if (mPickorderLines.size() > 0) {
                        if (position ==0) {
                            holder.pickorderLineItemLinearLayout.setSelected(true);
                        }
                        final cPickorderLineEntity l_firstPickorderLineEntity = mPickorderLines.get(0);
                        ((SortorderLinesActivity)callerContext).setChosenSortorderLine(l_firstPickorderLineEntity);
                    }
                }
            }
            if (id == R.id.recyclerViewSortorderLinesSorted) {
                holder.textViewPickorderLineLocation.setVisibility(View.GONE);
                //select first one
                if (callerContext instanceof SortorderLinesActivity) {
                    if (mPickorderLines.size() > 0) {
                        if (position ==0) {
                            holder.pickorderLineItemLinearLayout.setSelected(true);
                        }
                        final cPickorderLineEntity l_firstPickorderLineEntity = mPickorderLines.get(0);
                        ((SortorderLinesActivity)callerContext).setChosenSortorderLineToReset(l_firstPickorderLineEntity);
                    }
                }
            }
            if (id == R.id.recyclerViewSortorderLinesTotal) {
                holder.textViewPickorderLineLocation.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount () {
        if (mPickorderLines != null)
            return mPickorderLines.size();
        else return 0;
    }
    public void setFilter(String queryText) {
        mPickorderLines = filteredList(queryText);
        notifyDataSetChanged();
    }
    private List<cPickorderLineEntity> filteredList(String queryText) {
        queryText = queryText.toLowerCase();
        List<cPickorderLineEntity> filterPickorderLineList = new ArrayList<>();
        for (cPickorderLineEntity pickorderline:mAllPickorderLines)
        {
            String bincode = pickorderline.getBincode().toLowerCase();
            String itemno = pickorderline.getItemno().toLowerCase();
            String variantcode = pickorderline.getVariantcode().toLowerCase();
            String description = pickorderline.getDescription().toLowerCase();
            String sourceno = pickorderline.getSourceno().toLowerCase();

            if (bincode.contains(queryText) || itemno.contains(queryText) || variantcode.contains(queryText) || description.contains(queryText) || sourceno.contains(queryText))
            {
                filterPickorderLineList.add(pickorderline);
            }
        }
        return filterPickorderLineList;
    }
    public void showDefects(Boolean show) {
        mPickorderLines = defectsList(show);
        notifyDataSetChanged();
    }

    private List<cPickorderLineEntity> defectsList(Boolean show) {
        if (show) {
            List<cPickorderLineEntity> defectsPickorderLineList = new ArrayList<>();
            for (cPickorderLineEntity pickorderline:mAllPickorderLines)
            {
                Double pickedquantity = pickorderline.getQuantityhandled();
                Double requiredQuantity = pickorderline.getQuantity();
                if (pickedquantity < requiredQuantity)
                {
                    defectsPickorderLineList.add(pickorderline);
                }
            }
            return defectsPickorderLineList;
        }
        else {
            return mAllPickorderLines;
        }
    }

}
