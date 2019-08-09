package SSU_WHS.Basics.Workplaces;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import nl.icsvertex.scansuite.activities.pick.PickorderPickActivity;
import nl.icsvertex.scansuite.activities.ship.ShiporderLinesActivity;
import nl.icsvertex.scansuite.activities.sort.SortorderLinesActivity;
import nl.icsvertex.scansuite.R;

public class cWorkplaceAdapter extends RecyclerView.Adapter<cWorkplaceAdapter.WorkplaceViewHolder>{
    private Context callerContext;
    public class WorkplaceViewHolder extends RecyclerView.ViewHolder{
        private TextView textViewDescription;
        private TextView textViewWorkplace;
        public LinearLayout workplaceItemLinearLayout;

        public WorkplaceViewHolder(View itemView) {
            super(itemView);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            textViewDescription.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            textViewDescription.setSingleLine(true);
            textViewDescription.setMarqueeRepeatLimit(5);
            textViewDescription.setSelected(true);
            textViewWorkplace = itemView.findViewById(R.id.textViewWorkplace);
            workplaceItemLinearLayout = itemView.findViewById(R.id.workplaceItemLinearLayout);
        }
    }

    private final LayoutInflater mInflater;
    public static List<cWorkplaceEntity> mWorkplaces; //cached copy of pickorders

    public cWorkplaceAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        callerContext = context;
    }

    @Override
    public cWorkplaceAdapter.WorkplaceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recycler_workplace, parent, false);
        return new cWorkplaceAdapter.WorkplaceViewHolder(itemView);
    }

    public void setWorkplaces(List<cWorkplaceEntity> workplaces) {
        mWorkplaces = workplaces;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(cWorkplaceAdapter.WorkplaceViewHolder holder, int position) {
        if (mWorkplaces != null) {
            final cWorkplaceEntity l_WorkplaceEntity = mWorkplaces.get(position);
            String l_descriptionStr = l_WorkplaceEntity.getDescription();
            String l_workplaceStr = l_WorkplaceEntity.getWorkplace();

            holder.textViewDescription.setText(l_descriptionStr);
            holder.textViewWorkplace.setText(l_workplaceStr);

            holder.workplaceItemLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (callerContext instanceof PickorderPickActivity) {
                        ((PickorderPickActivity)callerContext).setChosenWorkplace(l_WorkplaceEntity);
                    }
                    if (callerContext instanceof SortorderLinesActivity) {
                        ((SortorderLinesActivity)callerContext).setChosenWorkplace(l_WorkplaceEntity);
                    }
                    if (callerContext instanceof ShiporderLinesActivity) {
                        ((ShiporderLinesActivity)callerContext).setChosenWorkplace(l_WorkplaceEntity);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount () {
        if (mWorkplaces != null)
            return mWorkplaces.size();
        else return 0;
    }
}
