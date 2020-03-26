package SSU_WHS.Basics.Workplaces;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ICS.cAppExtension;
import nl.icsvertex.scansuite.Activities.Pick.PickorderLinesActivity;
import nl.icsvertex.scansuite.Activities.Ship.ShiporderLinesActivity;
import nl.icsvertex.scansuite.Activities.Sort.SortorderLinesActivity;
import nl.icsvertex.scansuite.Fragments.Dialogs.WorkplaceFragment;
import nl.icsvertex.scansuite.R;

public class cWorkplaceAdapter extends RecyclerView.Adapter<cWorkplaceAdapter.WorkplaceViewHolder>{

    //Region Public Properties
    public static class WorkplaceViewHolder extends RecyclerView.ViewHolder{
        private TextView textViewDescription;
        private TextView textViewWorkplace;
        public LinearLayout workplaceItemLinearLayout;

        public WorkplaceViewHolder(View itemView) {
            super(itemView);
            this.textViewDescription = itemView.findViewById(R.id.textViewDescription);
            this.textViewDescription.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            this.textViewDescription.setSingleLine(true);
            this.textViewDescription.setMarqueeRepeatLimit(5);
            this.textViewDescription.setSelected(true);
            this.textViewWorkplace = itemView.findViewById(R.id.textViewWorkplace);
            this.workplaceItemLinearLayout = itemView.findViewById(R.id.workplaceItemLinearLayout);
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

    @NonNull
    @Override
    public cWorkplaceAdapter.WorkplaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = this.LayoutInflaterObject.inflate(R.layout.recycler_workplace, parent, false);
        return new WorkplaceViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull cWorkplaceAdapter.WorkplaceViewHolder holder, int position) {
        if (cWorkplace.allWorkplacesObl != null) {

            final cWorkplace workplace = cWorkplace.allWorkplacesObl.get(position);

            holder.textViewDescription.setText(workplace.getDescriptionStr());
            holder.textViewWorkplace.setText(workplace.getWorkplaceStr());

            holder.workplaceItemLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //Set the current workplaceStr
                    cWorkplace.currentWorkplace = workplace;

                    if (cAppExtension.context instanceof PickorderLinesActivity) {
                        PickorderLinesActivity pickorderLinesActivity = (PickorderLinesActivity)cAppExtension.activity;
                        pickorderLinesActivity.pClosePickAndDecideNextStep();
                        return;
                    }

                    if (cAppExtension.context instanceof SortorderLinesActivity) {
                        SortorderLinesActivity sortorderLinesActivity = (SortorderLinesActivity)cAppExtension.activity;
                        sortorderLinesActivity.pCloseSortAndDecideNextStep();
                    }

                    if (cAppExtension.context instanceof ShiporderLinesActivity) {

                        ShiporderLinesActivity shiporderLinesActivity = (ShiporderLinesActivity)cAppExtension.activity;

                        if (cAppExtension.dialogFragment instanceof WorkplaceFragment) {
                            cAppExtension.dialogFragment.dismiss();
                        }

                        shiporderLinesActivity.pWorkplaceSelected();

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
