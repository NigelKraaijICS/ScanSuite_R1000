package SSU_WHS.Basics.BranchReason;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ICS.cAppExtension;
import SSU_WHS.Basics.Users.cUser;
import nl.icsvertex.scansuite.Activities.Returns.CreateReturnActivity;
import nl.icsvertex.scansuite.Fragments.Returns.ReturnArticleDetailFragment;
import nl.icsvertex.scansuite.R;

public class cBranchReasonAdapter extends RecyclerView.Adapter<cBranchReasonAdapter.ReasonViewHolder> {

    //Region Public Properties
    public static class ReasonViewHolder extends RecyclerView.ViewHolder{
        private TextView textViewDescription;
        private TextView textViewReason;
        public LinearLayout reasonItemLinearLayout;

        public ReasonViewHolder(View itemView) {
            super(itemView);
            this.textViewDescription = itemView.findViewById(R.id.textViewDescription);
            this.textViewDescription.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            this.textViewDescription.setSingleLine(true);
            this.textViewDescription.setMarqueeRepeatLimit(5);
            this.textViewDescription.setSelected(true);

            this.textViewReason = itemView.findViewById(R.id.textViewReason);
            this.reasonItemLinearLayout = itemView.findViewById(R.id.reasonItemLinearLayout);
        }
    }

    //End Region Public Properties

    //Region Constructor

    public cBranchReasonAdapter() {
        this.LayoutInflaterObject = LayoutInflater.from(cAppExtension.context);
    }

    //Region Private Properties
    private final LayoutInflater LayoutInflaterObject;
    //End Region Private Propertoes

    @NonNull
    @Override
    public cBranchReasonAdapter.ReasonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = this.LayoutInflaterObject.inflate(R.layout.recycler_reason, parent, false);
        return new ReasonViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull cBranchReasonAdapter.ReasonViewHolder holder, int position) {
        if (cUser.currentUser.currentBranch.returnReasonObl != null) {

            final cBranchReason branchReason = cUser.currentUser.currentBranch.returnReasonObl.get(position);

            holder.textViewDescription.setText(branchReason.getDescriptionStr());
            holder.textViewReason.setText(branchReason.getReasonStr());

            holder.reasonItemLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //Set the current reasonStr
                    cBranchReason.currentBranchReason = branchReason;

                        if (cAppExtension.activity instanceof CreateReturnActivity) {
                            CreateReturnActivity createReturnActivity = (CreateReturnActivity)cAppExtension.activity;
                            createReturnActivity.pSetReason();
                            createReturnActivity.pHandleFragmentDismissed();
                            cAppExtension.dialogFragment.dismiss();
                        }
                        else {

                            if (cAppExtension.dialogFragment instanceof  ReturnArticleDetailFragment) {
                               final  ReturnArticleDetailFragment returnArticleDetailFragment = (ReturnArticleDetailFragment)cAppExtension.dialogFragment;
                                returnArticleDetailFragment.pSetReason();
                                cAppExtension.dialogFragment.dismiss();

                                Handler handler = new Handler(Looper.getMainLooper());
                                handler.postDelayed(new Runnable() {
                                    public void run() {
                                        returnArticleDetailFragment.pHandleFragmentDismissed();
                                        // Actions to do after 0.3 seconds
                                    }
                                }, 200);
                            }


                        }

                    }

            });
        }
    }

    @Override
    public int getItemCount () {
        if (cUser.currentUser.currentBranch.returnReasonObl != null)
            return cUser.currentUser.currentBranch.returnReasonObl.size();
        else return 0;
    }
}
