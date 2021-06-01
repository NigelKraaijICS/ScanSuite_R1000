package SSU_WHS.Basics.Workplaces;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ICS.Utils.Scanning.cBarcodeScan;
import ICS.cAppExtension;
import nl.icsvertex.scansuite.Activities.FinishShip.FinishShipLinesActivity;
import nl.icsvertex.scansuite.Activities.Pick.PickorderLinesActivity;
import nl.icsvertex.scansuite.Activities.Pick.PickorderLinesGeneratedActivity;
import nl.icsvertex.scansuite.Activities.Ship.ShiporderLinesActivity;
import nl.icsvertex.scansuite.Activities.Sort.SortorderLinesActivity;
import nl.icsvertex.scansuite.Fragments.Dialogs.WorkplaceFragment;
import nl.icsvertex.scansuite.R;

public class cWorkplaceAdapter extends RecyclerView.Adapter<cWorkplaceAdapter.WorkplaceViewHolder>{

    //Region Public Properties
    public static class WorkplaceViewHolder extends RecyclerView.ViewHolder{
        private final TextView textViewDescription;
        private final TextView textViewWorkplace;
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

            holder.workplaceItemLinearLayout.setOnClickListener(v -> {

                //Set the current workplaceStr
                cWorkplace.currentWorkplace = workplace;

                if (cAppExtension.dialogFragment instanceof WorkplaceFragment) {
                    WorkplaceFragment workplaceFragment = (WorkplaceFragment)cAppExtension.dialogFragment;
                    workplaceFragment.pHandleScan(cBarcodeScan.pFakeScan(cWorkplace.currentWorkplace.getWorkplaceStr()));
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
