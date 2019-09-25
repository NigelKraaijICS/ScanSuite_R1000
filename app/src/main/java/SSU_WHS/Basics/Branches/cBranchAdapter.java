package SSU_WHS.Basics.Branches;

import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import SSU_WHS.Basics.Users.cUser;
import nl.icsvertex.scansuite.cAppExtension;
import nl.icsvertex.scansuite.activities.general.LoginActivity;
import nl.icsvertex.scansuite.R;

public class cBranchAdapter  extends RecyclerView.Adapter<cBranchAdapter.BranchViewHolder>{

    //Region Public Properties
    public class BranchViewHolder extends RecyclerView.ViewHolder{
        private TextView textViewDescription;
        private TextView textViewBranch;
        private ImageView imageViewMenuItem;
        public LinearLayout branchItemLinearLayout;

        public BranchViewHolder(View itemView) {
            super(itemView);
            imageViewMenuItem = itemView.findViewById(R.id.imageViewMenuItem);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            textViewDescription.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            textViewDescription.setSingleLine(true);
            textViewDescription.setMarqueeRepeatLimit(5);
            textViewDescription.setSelected(true);
            textViewBranch = itemView.findViewById(R.id.textViewBranch);
            branchItemLinearLayout = itemView.findViewById(R.id.branchItemLinearLayout);
        }
    }
    //End Region Public Properties

    //Region Private Properties
    private final LayoutInflater LayoutInflaterObject;
    //End Region Private Propertoes

    //Region Constructor
    public cBranchAdapter() {
        this.LayoutInflaterObject = LayoutInflater.from(cAppExtension.context);
    }
    //End Region Constructor

    @Override
    public cBranchAdapter.BranchViewHolder onCreateViewHolder(ViewGroup pvParent, int pvViewType) {
        View itemView = this.LayoutInflaterObject.inflate(R.layout.recycler_branch, pvParent, false);
        return new cBranchAdapter.BranchViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(cBranchAdapter.BranchViewHolder holder, int position) {
        if (cUser.currentUser != null && cUser.currentUser.branchesObl != null) {

            final cBranch branch = cUser.currentUser.branchesObl.get(position);


            if (branch.getBranchTypeStr().equalsIgnoreCase(cBranch.brachTypeEnum.UNKNOWN.toString())) {
                holder.imageViewMenuItem.setImageResource(R.drawable.ic_unknown);
            }
            if (branch.getBranchTypeStr().equalsIgnoreCase(cBranch.brachTypeEnum.STORE.toString())) {
                holder.imageViewMenuItem.setImageResource(R.drawable.ic_store);
            }
            if (branch.getBranchTypeStr().equalsIgnoreCase(cBranch.brachTypeEnum.WAREHOUSE.toString())) {
                holder.imageViewMenuItem.setImageResource(R.drawable.ic_warehouse);
            }
            if (branch.getBranchTypeStr().equalsIgnoreCase(cBranch.brachTypeEnum.INTRANSIT.toString())) {
                holder.imageViewMenuItem.setImageResource(R.drawable.ic_transit);
            }

            holder.textViewDescription.setText(branch.getBranchNameStr());
            holder.textViewBranch.setText(branch.getBranchStr());

            holder.branchItemLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (cAppExtension.context instanceof LoginActivity) {
                        cUser.currentUser.currentBranch = branch;
                         LoginActivity.pBranchSelected(branch);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount () {
        if (cUser.currentUser != null && cUser.currentUser.branchesObl != null)
            return cUser.currentUser.branchesObl.size();
        else return 0;
    }
}
