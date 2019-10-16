package SSU_WHS.Basics.Workplaces;

import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import nl.icsvertex.scansuite.Activities.pick.PickorderLinesActivity;
import nl.icsvertex.scansuite.Activities.ship.ShiporderLinesActivity;
import nl.icsvertex.scansuite.Activities.ship.ShiporderShipActivity;
import nl.icsvertex.scansuite.Activities.sort.SortorderLinesActivity;
import ICS.cAppExtension;
import nl.icsvertex.scansuite.R;

public class cWorkplaceAdapter extends RecyclerView.Adapter<cWorkplaceAdapter.WorkplaceViewHolder>{

    //Region Public Properties
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

    //End Region Public Properties

    //Region Constructor

    public cWorkplaceAdapter() {
        this.LayoutInflaterObject = LayoutInflater.from(cAppExtension.context);
    }

    //Region Private Properties
    private final LayoutInflater LayoutInflaterObject;
    //End Region Private Propertoes

    @Override
    public cWorkplaceAdapter.WorkplaceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = this.LayoutInflaterObject.inflate(R.layout.recycler_workplace, parent, false);
        return new cWorkplaceAdapter.WorkplaceViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(cWorkplaceAdapter.WorkplaceViewHolder holder, int position) {
        if (cWorkplace.allWorkplacesObl != null) {
            final cWorkplace workplace = cWorkplace.allWorkplacesObl.get(position);

            holder.textViewDescription.setText(workplace.getDescriptionStr());
            holder.textViewWorkplace.setText(workplace.getWorkplaceStr());

            //todo: put this back
            //TODO: do this in a different shared way
            holder.workplaceItemLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //Set the current workplace
                    cWorkplace.currentWorkplace = workplace;

                    if (cAppExtension.context instanceof PickorderLinesActivity) {
                        PickorderLinesActivity.pClosePickAndDecideNextStep();
                        return;
                    }

                    if (cAppExtension.context instanceof SortorderLinesActivity) {
                        SortorderLinesActivity.pCloseSortAndDecideNextStep();
                    }

                    if (cAppExtension.context instanceof ShiporderLinesActivity) {
                        ShiporderLinesActivity.pShowOrderDoneFragment();
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount () {
        if (cWorkplace.allWorkplacesObl != null)
            return cWorkplace.allWorkplacesObl.size();
        else return 0;
    }
}
