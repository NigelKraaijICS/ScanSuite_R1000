package SSU_WHS.Picken.PickorderLinePackAndShip;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import nl.icsvertex.scansuite.R;
import nl.icsvertex.scansuite.activities.ship.ShiporderLinesActivity;

public class cPickorderLinePackAndShipGroupBySourceNoAdapter extends RecyclerView.Adapter<cPickorderLinePackAndShipGroupBySourceNoAdapter.PickorderLinePackAndShipViewHolder> {
    private Context callerContext;
    private List<LinearLayout> pickorderLinePackAndShipItemLinearLayouts = new ArrayList<>();
    private RecyclerView thisRecyclerView;
    public class PickorderLinePackAndShipViewHolder extends RecyclerView.ViewHolder{
        private TextView textViewPickorderLinePackAndShipDocument;
        private TextView textViewPickorderLinePackAndShipQuantity;
        private TextView textViewPickorderLinePackAndShipPack;
        public LinearLayout pickorderLinePackAndShipItemLinearLayout;


        public PickorderLinePackAndShipViewHolder(View itemView) {
            super(itemView);
            textViewPickorderLinePackAndShipDocument = itemView.findViewById(R.id.textViewPickorderLinePackAndShipDocument);
            textViewPickorderLinePackAndShipDocument.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            textViewPickorderLinePackAndShipDocument.setSingleLine(true);
            textViewPickorderLinePackAndShipDocument.setMarqueeRepeatLimit(5);
            textViewPickorderLinePackAndShipDocument.setSelected(true);
            textViewPickorderLinePackAndShipQuantity = itemView.findViewById(R.id.textViewPickorderLinePackAndShipQuantity);
            textViewPickorderLinePackAndShipQuantity.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            textViewPickorderLinePackAndShipQuantity.setSingleLine(true);
            textViewPickorderLinePackAndShipQuantity.setMarqueeRepeatLimit(5);
            textViewPickorderLinePackAndShipQuantity.setSelected(true);
            textViewPickorderLinePackAndShipPack = itemView.findViewById(R.id.textViewPickorderLinePackAndShipPack);
            textViewPickorderLinePackAndShipPack.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            textViewPickorderLinePackAndShipPack.setSingleLine(true);
            textViewPickorderLinePackAndShipPack.setMarqueeRepeatLimit(5);
            textViewPickorderLinePackAndShipPack.setSelected(true);
            pickorderLinePackAndShipItemLinearLayout = itemView.findViewById(R.id.pickorderLinePackAndShipItemLinearLayout);
        }
    }

    private final LayoutInflater mInflater;
    public List<cPickorderLinePackAndShipGroupBySourceNo> mPickorderLinePackAndShips; //cached copy of pickorders
    public List<cPickorderLinePackAndShipGroupBySourceNo> mAllPickorderLinePackAndShips; //cached copy of pickorders


    public cPickorderLinePackAndShipGroupBySourceNoAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        callerContext = context;
    }

    @Override
    public cPickorderLinePackAndShipGroupBySourceNoAdapter.PickorderLinePackAndShipViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recycler_pickorderlinepackandship, parent, false);
        return new cPickorderLinePackAndShipGroupBySourceNoAdapter.PickorderLinePackAndShipViewHolder(itemView);
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        thisRecyclerView = recyclerView;
    }
    public void setPickorderLinePacikAndShips(List<cPickorderLinePackAndShipGroupBySourceNo> pickorderLinePackAndShipEntities) {
        mAllPickorderLinePackAndShips = pickorderLinePackAndShipEntities;
        mPickorderLinePackAndShips = pickorderLinePackAndShipEntities;
        //notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(cPickorderLinePackAndShipGroupBySourceNoAdapter.PickorderLinePackAndShipViewHolder holder, final int position) {
        //if (!pickorderLineItemLinearLayouts.contains(holder.pickorderLineItemLinearLayout)) {
        pickorderLinePackAndShipItemLinearLayouts.add(holder.pickorderLinePackAndShipItemLinearLayout);
        //}

        if (mPickorderLinePackAndShips != null) {
            final cPickorderLinePackAndShipGroupBySourceNo pickorderLinePackAndShipGroupBySourceNo = mPickorderLinePackAndShips.get(position);

            String quantityFieldToShow;
            Double quantityDbl = pickorderLinePackAndShipGroupBySourceNo.getQuantityhandled();

            quantityFieldToShow = Integer.toString(quantityDbl.intValue()) ;

            String l_sourcenoStr = pickorderLinePackAndShipGroupBySourceNo.getSourceno().trim();
            String l_processingSequence = pickorderLinePackAndShipGroupBySourceNo.getProcessingsequence().trim();

            if (!l_processingSequence.isEmpty()) {
                holder.textViewPickorderLinePackAndShipDocument.setText(l_processingSequence);
            }
            else {
                holder.textViewPickorderLinePackAndShipDocument.setText(l_sourcenoStr);
            }
            holder.textViewPickorderLinePackAndShipQuantity.setText(quantityFieldToShow);
            holder.textViewPickorderLinePackAndShipPack.setText("");

            final int id = thisRecyclerView.getId();
            holder.pickorderLinePackAndShipItemLinearLayout.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    //deselect all
                    for (LinearLayout aLayout : pickorderLinePackAndShipItemLinearLayouts) {
                        aLayout.setSelected(false);
                    }
                    //select current
                    v.setSelected(true);
                    //wich fragment?
                    if (id == R.id.recyclerViewShiporderLinesToship) {
                        if (callerContext instanceof ShiporderLinesActivity) {
                            ((ShiporderLinesActivity) callerContext).setChosenShiporderLine(pickorderLinePackAndShipGroupBySourceNo);
                        }
                    }
                    if (id == R.id.recyclerViewShiporderLinesShipped) {
                        if (callerContext instanceof ShiporderLinesActivity) {
                            ((ShiporderLinesActivity) callerContext).setChosenShiporderLineToReset(pickorderLinePackAndShipGroupBySourceNo);
                        }
                    }


                }

            });

            if (id == R.id.recyclerViewShiporderLinesToship) {
                //select first one
                if (callerContext instanceof ShiporderLinesActivity) {
                    if (mPickorderLinePackAndShips.size() > 0) {
                        if (position ==0) {
                            holder.pickorderLinePackAndShipItemLinearLayout.setSelected(true);
                        }
                        final cPickorderLinePackAndShipGroupBySourceNo l_firstPickorderLineEntity = mPickorderLinePackAndShips.get(0);
                        ((ShiporderLinesActivity)callerContext).setChosenShiporderLine(l_firstPickorderLineEntity);
                    }
                }
            }
            if (id == R.id.recyclerViewShiporderLinesShipped) {
                //select first one
//                if (callerContext instanceof ShiporderLinesActivity) {
//                    if (mPickorderLinePackAndShips.size() > 0) {
//                        if (position ==0) {
//                            holder.pickorderLinePackAndShipItemLinearLayout.setSelected(true);
//                        }
//                        final cPickorderLinePackAndShipGroupBySourceNo l_firstPickorderLineEntity = mPickorderLinePackAndShips.get(0);
//                        ((ShiporderLinesActivity)callerContext).setChosenShiporderLineToReset(l_firstPickorderLineEntity);
//                    }
//                }
            }
        }
    }

    @Override
    public int getItemCount () {
        if (mPickorderLinePackAndShips != null)
            return mPickorderLinePackAndShips.size();
        else return 0;
    }
    public void setFilter(String queryText) {
        mPickorderLinePackAndShips = filteredList(queryText);
        notifyDataSetChanged();
    }
    private List<cPickorderLinePackAndShipGroupBySourceNo> filteredList(String queryText) {
        queryText = queryText.toLowerCase();
        List<cPickorderLinePackAndShipGroupBySourceNo> filterPickorderLinePackAndShipList = new ArrayList<>();
        for (cPickorderLinePackAndShipGroupBySourceNo pickorderlinepackandship:mAllPickorderLinePackAndShips)
        {
            String sourceno = pickorderlinepackandship.getSourceno().toLowerCase();

            if (sourceno.contains(queryText))
            {
                filterPickorderLinePackAndShipList.add(pickorderlinepackandship);
            }
        }
        return filterPickorderLinePackAndShipList;
    }
}
