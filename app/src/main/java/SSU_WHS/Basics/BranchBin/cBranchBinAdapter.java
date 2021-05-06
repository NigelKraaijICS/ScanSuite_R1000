package SSU_WHS.Basics.BranchBin;

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
import SSU_WHS.Basics.Users.cUser;
import nl.icsvertex.scansuite.Fragments.Dialogs.BinFragment;
import nl.icsvertex.scansuite.R;

public class cBranchBinAdapter extends RecyclerView.Adapter<cBranchBinAdapter.BranchBinViewHolder>{

    //Region Public Properties
    public static class BranchBinViewHolder extends RecyclerView.ViewHolder{
        private final TextView textViewBin;
        public LinearLayout branchBinItemLinearLayout;

        public BranchBinViewHolder(View itemView) {
            super(itemView);
            this.textViewBin = itemView.findViewById(R.id.textViewBin);
            this.branchBinItemLinearLayout = itemView.findViewById(R.id.branchBinItemLinearLayout);
        }
    }
    //End Region Public Properties

    //Region Private Properties
    private final LayoutInflater LayoutInflaterObject;
    //End Region Private Propertoes

    //Region Constructor
    public cBranchBinAdapter() {
        this.LayoutInflaterObject = LayoutInflater.from(cAppExtension.context);
    }
    //End Region Constructor

    // Region Default Methods

    @NonNull
    @Override
    public cBranchBinAdapter.BranchBinViewHolder onCreateViewHolder(@NonNull ViewGroup pvParent, int pvViewType) {
        View itemView = this.LayoutInflaterObject.inflate(R.layout.recycler_branchbin, pvParent, false);
        return new BranchBinViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull cBranchBinAdapter.BranchBinViewHolder holder, int position) {


        if (cUser.currentUser != null &&  cUser.currentUser.currentBranch!= null &&  cUser.currentUser.currentBranch.shipBinsObl != null) {

            final cBranchBin branchBin = cUser.currentUser.currentBranch.shipBinsObl.get(position);
            holder.textViewBin.setText(branchBin.getBinCodeStr());

            holder.branchBinItemLinearLayout.setOnClickListener(v -> {

                if (cAppExtension.dialogFragment instanceof BinFragment) {
                    BinFragment binFragment = (BinFragment)cAppExtension.dialogFragment;
                    binFragment.pHandleScan(cBarcodeScan.pFakeScan(branchBin.getBinCodeStr()));
                }
            });
        }
    }

    @Override
    public int getItemCount () {
        if (cUser.currentUser != null &&  cUser.currentUser.currentBranch!= null  &&  cUser.currentUser.currentBranch.shipBinsObl != null)
            return cUser.currentUser.currentBranch.shipBinsObl.size();
        else return 0;
    }

    // End Region Default Methods


}
