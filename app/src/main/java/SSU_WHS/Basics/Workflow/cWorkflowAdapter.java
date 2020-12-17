package SSU_WHS.Basics.Workflow;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ICS.cAppExtension;
import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import nl.icsvertex.scansuite.Activities.Move.MoveorderSelectActivity;
import nl.icsvertex.scansuite.R;

public class cWorkflowAdapter extends RecyclerView.Adapter<cWorkflowAdapter.WorkflowViewHolder>{

    private List<String> localWorkflowObl;

    //Region Public Properties
    public static class WorkflowViewHolder extends RecyclerView.ViewHolder{

        private final ImageView imageViewWorkflow;
        private final TextView textViewDescription;
        public LinearLayout workflowItemLinearLayout;

        public WorkflowViewHolder(View itemView) {
            super(itemView);
            this.textViewDescription = itemView.findViewById(R.id.textViewDescription);
            this.textViewDescription.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            this.textViewDescription.setSingleLine(true);
            this.textViewDescription.setMarqueeRepeatLimit(5);
            this.textViewDescription.setSelected(true);
            this.imageViewWorkflow = itemView.findViewById(R.id.imageViewWorkflow);
            this.workflowItemLinearLayout = itemView.findViewById(R.id.workflowItemLinearLayout);
        }
    }

    //End Region Public Properties

    //Region Constructor

    public cWorkflowAdapter() {
        this.LayoutInflaterObject = LayoutInflater.from(cAppExtension.context);
    }

    //Region Private Properties
    private final LayoutInflater LayoutInflaterObject;
    //End Region Private Propertoes

    @NonNull
    @Override
    public cWorkflowAdapter.WorkflowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = this.LayoutInflaterObject.inflate(R.layout.recycler_workflow, parent, false);
        return new WorkflowViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull cWorkflowAdapter.WorkflowViewHolder pvHolder, int pvPositionInt) {


        if (this.localWorkflowObl != null) {

            final String workflowStr = this.localWorkflowObl.get(pvPositionInt);

            pvHolder.textViewDescription.setText(cWarehouseorder.pGetWorkflowDescriptionStr(workflowStr));
            this.mSetImage(pvHolder, workflowStr);


            pvHolder.workflowItemLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //Set the current workflow

                    if (cAppExtension.activity instanceof MoveorderSelectActivity) {
                        MoveorderSelectActivity moveorderSelectActivity = (MoveorderSelectActivity)cAppExtension.activity;
                        moveorderSelectActivity.pNewWorkflowSelected(workflowStr);
                    }

                }
            });
        }
    }

    @Override
    public int getItemCount () {
        if (this.localWorkflowObl != null)
            return this.localWorkflowObl.size();
        else return 0;
    }

    public void pFillData(List<String> pvWorkflowObl) {
        this.localWorkflowObl = pvWorkflowObl;
    }


    private void mSetImage(cWorkflowAdapter.WorkflowViewHolder pvHolder,String pvWorkflowStr) {

        if (pvWorkflowStr.equalsIgnoreCase(cWarehouseorder.WorkflowEnu.EOR.toString())) {
            pvHolder.imageViewWorkflow.setImageResource(R.drawable.ic_menu_intake_eo);
            return;
        }

        if (pvWorkflowStr.equalsIgnoreCase(cWarehouseorder.WorkflowEnu.EOS.toString())) {
            pvHolder.imageViewWorkflow.setImageResource(R.drawable.ic_menu_intake_eo);
            return;
        }

        if (pvWorkflowStr.equalsIgnoreCase(cWarehouseorder.WorkflowEnu.MAS.toString())) {
            pvHolder.imageViewWorkflow.setImageResource(R.drawable.ic_menu_intake_ma);
            return;
        }

        if (pvWorkflowStr.equalsIgnoreCase(cWarehouseorder.WorkflowEnu.IVS.toString())) {
            pvHolder.imageViewWorkflow.setImageResource(R.drawable.ic_menu_inventory);
            return;
        }


        if (pvWorkflowStr.equalsIgnoreCase(cWarehouseorder.WorkflowEnu.MI.toString())) {
            pvHolder.imageViewWorkflow.setImageResource(R.drawable.ic_menu_move_mi);
            return;
        }

        if (pvWorkflowStr.equalsIgnoreCase(cWarehouseorder.WorkflowEnu.MO.toString())) {
            pvHolder.imageViewWorkflow.setImageResource(R.drawable.ic_menu_move_mo);
            return;
        }

        if (pvWorkflowStr.equalsIgnoreCase(cWarehouseorder.WorkflowEnu.MV.toString())) {
            pvHolder.imageViewWorkflow.setImageResource(R.drawable.ic_menu_move_mv);
            return;
        }

        if (pvWorkflowStr.equalsIgnoreCase(cWarehouseorder.WorkflowEnu.MVI.toString())) {
            pvHolder.imageViewWorkflow.setImageResource(R.drawable.ic_menu_moveitem);
            return;
        }


        if (pvWorkflowStr.equalsIgnoreCase(cWarehouseorder.WorkflowEnu.PF.toString())) {
            pvHolder.imageViewWorkflow.setImageResource(R.drawable.ic_menu_pick_pf);
            return;
        }

        if (pvWorkflowStr.equalsIgnoreCase(cWarehouseorder.WorkflowEnu.PV.toString())) {
            pvHolder.imageViewWorkflow.setImageResource(R.drawable.ic_menu_pick_pv);
            return;
        }

        if (pvWorkflowStr.equalsIgnoreCase(cWarehouseorder.WorkflowEnu.RVS.toString())) {
            pvHolder.imageViewWorkflow.setImageResource(R.drawable.ic_menu_return);
            return;
        }

        if (pvWorkflowStr.equalsIgnoreCase(cWarehouseorder.WorkflowEnu.RVR.toString())) {
            pvHolder.imageViewWorkflow.setImageResource(R.drawable.ic_menu_external_return);
            return;
        }

        if (pvWorkflowStr.equalsIgnoreCase(cWarehouseorder.WorkflowEnu.SPV.toString())) {
            pvHolder.imageViewWorkflow.setImageResource(R.drawable.ic_menu_selfpick);
            return;
        }
    }
}
