package SSU_WHS.Basics.Branches;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import nl.icsvertex.scansuite.activities.general.LoginActivity;
import nl.icsvertex.scansuite.R;

public class cBranchAdapter  extends RecyclerView.Adapter<cBranchAdapter.BranchViewHolder>{
    private Context callerContext;
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

    private final LayoutInflater mInflater;
    public static List<cBranchEntity> mBranches; //cached copy of pickorders

    public cBranchAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        callerContext = context;
    }

    @Override
    public cBranchAdapter.BranchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recycler_branch, parent, false);
        return new cBranchAdapter.BranchViewHolder(itemView);
    }

    public void setBranches(List<cBranchEntity> branches) {
        mBranches = branches;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(cBranchAdapter.BranchViewHolder holder, int position) {
        if (mBranches != null) {
            final cBranchEntity l_BranchEntity = mBranches.get(position);
            String l_descriptionStr = l_BranchEntity.getBranchname();
            String l_BranchStr = l_BranchEntity.getBranch();
            String l_branchTypeStr = l_BranchEntity.getBranchtype();

            if (l_branchTypeStr.equalsIgnoreCase(cBranch.brachTypeEnum.UNKNOWN.toString())) {
                holder.imageViewMenuItem.setImageResource(R.drawable.ic_unknown);
            }
            if (l_branchTypeStr.equalsIgnoreCase(cBranch.brachTypeEnum.STORE.toString())) {
                holder.imageViewMenuItem.setImageResource(R.drawable.ic_store);
            }
            if (l_branchTypeStr.equalsIgnoreCase(cBranch.brachTypeEnum.WAREHOUSE.toString())) {
                holder.imageViewMenuItem.setImageResource(R.drawable.ic_warehouse);
            }
            if (l_branchTypeStr.equalsIgnoreCase(cBranch.brachTypeEnum.INTRANSIT.toString())) {
                holder.imageViewMenuItem.setImageResource(R.drawable.ic_transit);
            }

            holder.textViewDescription.setText(l_descriptionStr);
            holder.textViewBranch.setText(l_BranchStr);

            holder.branchItemLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (callerContext instanceof LoginActivity) {
                        ((LoginActivity)callerContext).setChosenBranch(l_BranchEntity);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount () {
        if (mBranches != null)
            return mBranches.size();
        else return 0;
    }
}
