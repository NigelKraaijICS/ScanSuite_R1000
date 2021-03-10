package SSU_WHS.Move.MoveOrders;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ICS.cAppExtension;
import SSU_WHS.Basics.Users.cUser;
import SSU_WHS.General.cPublicDefinitions;
import nl.icsvertex.scansuite.Activities.Move.MoveorderSelectActivity;
import nl.icsvertex.scansuite.R;

public class cMoveorderAdapter extends RecyclerView.Adapter<cMoveorderAdapter.MoveorderViewHolder>  {

    //Region Public Properties

    public static class MoveorderViewHolder extends RecyclerView.ViewHolder{

        private final View viewOrderStatus;
        private final TextView textViewOrdernumber;
        private final TextView textViewOrderUser;
        private final TextView textViewOrdertype;
        private final TextView textViewDocument;
        private final ImageView imageViewIsProcessedOrWait;
        private final ImageView imageDocument;
        public LinearLayout moveorderItemLinearLayout;

        public MoveorderViewHolder(View pvItemView) {
            super(pvItemView);
            this.viewOrderStatus = pvItemView.findViewById(R.id.viewOrderStatus);
            this.textViewOrderUser = pvItemView.findViewById(R.id.textViewOrderUser);
            this.textViewOrderUser.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            this.textViewOrderUser.setSingleLine(true);
            this.textViewOrderUser.setMarqueeRepeatLimit(5);
            this.textViewOrderUser.setSelected(true);
            this.textViewOrdernumber = pvItemView.findViewById(R.id.textViewOrdernumber);
            this.textViewOrdernumber.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            this.textViewOrdernumber.setSingleLine(true);
            this.textViewOrdernumber.setMarqueeRepeatLimit(5);
            this.textViewOrdernumber.setSelected(true);
            this.textViewDocument = pvItemView.findViewById(R.id.textViewDocument);
            this.textViewDocument.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            this.textViewDocument.setSingleLine(true);
            this.textViewDocument.setMarqueeRepeatLimit(5);
            this.textViewDocument.setSelected(true);
            this.textViewOrdertype = pvItemView.findViewById(R.id.textViewOrdertype);
            this.imageDocument = pvItemView.findViewById(R.id.imageDocument);
            this.imageViewIsProcessedOrWait = pvItemView.findViewById(R.id.imageViewIsProcessedOrWait);
            this.moveorderItemLinearLayout = pvItemView.findViewById(R.id.moveorderItemLinearLayout);

        }
        //End Region Public Properties
    }

    //Region Private Properties
    private final LayoutInflater LayoutInflaterObject;
    private List<cMoveorder> localMoveorderObl;

    //End Region Private Properties

    //Region Constructor
    public cMoveorderAdapter() {
        this.LayoutInflaterObject = LayoutInflater.from(cAppExtension.context);
    }
    //End Region Constructor


    //Region Default Methods
    @NonNull
    @Override
    public cMoveorderAdapter.MoveorderViewHolder onCreateViewHolder(@NonNull ViewGroup pvParent, int pbViewTypeInt) {
        View itemView = LayoutInflaterObject.inflate(R.layout.recycler_moveorder, pvParent, false);
        return new MoveorderViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull cMoveorderAdapter.MoveorderViewHolder pvHolder, int pvPositionInt) {

        if (localMoveorderObl == null || localMoveorderObl.size() == 0 ) {
            return;
        }

        final cMoveorder selectedMoveorder = localMoveorderObl.get(pvPositionInt);

        if (!selectedMoveorder.getAssignedUserIdStr().isEmpty()) {
            pvHolder.viewOrderStatus.setBackgroundResource(R.color.colorOrderStatusAssignedUser);
        }

        if (selectedMoveorder.getStatusInt() == 10 ) {
            pvHolder.textViewOrderUser.setText(cUser.pUserToShowStr(selectedMoveorder.getAssignedUserIdStr()));
        }
        else {
            pvHolder.textViewOrderUser.setText(cUser.pUserToShowStr(selectedMoveorder.getCurrentUserIdStr()));
        }

        pvHolder.textViewOrdernumber.setText(selectedMoveorder.getOrderNumberStr());
        pvHolder.textViewOrdernumber.setTag(selectedMoveorder.getOrderNumberStr());


        if(selectedMoveorder.getOrderTypeStr().equalsIgnoreCase(cPublicDefinitions.Workflows.MV.toString())) {
            pvHolder.textViewOrdertype.setText(R.string.ordertype_mv);
            pvHolder.textViewDocument.setText(selectedMoveorder.getDocumentStr());
        }

        if(selectedMoveorder.getOrderTypeStr().equalsIgnoreCase(cPublicDefinitions.Workflows.MT.toString())) {
            pvHolder.textViewOrdertype.setText(R.string.ordertype_mt);
            pvHolder.textViewDocument.setText(selectedMoveorder.getExternalReferenceStr());
        }
        if(selectedMoveorder.getOrderTypeStr().equalsIgnoreCase(cPublicDefinitions.Workflows.MI.toString())) {
            pvHolder.textViewOrdertype.setText(R.string.ordertype_mi);
            pvHolder.textViewDocument.setText(selectedMoveorder.getDocumentStr());
        }

        if (selectedMoveorder.getProcessingOrParkedBln()) {
            pvHolder.imageViewIsProcessedOrWait.setVisibility(View.VISIBLE);
        }
        else {
            pvHolder.imageViewIsProcessedOrWait.setVisibility(View.INVISIBLE);
        }

        if (selectedMoveorder.getDocumentStr().isEmpty()) {
            pvHolder.textViewDocument.setVisibility(View.GONE);
            pvHolder.imageDocument.setVisibility(View.GONE);
            pvHolder.moveorderItemLinearLayout.requestLayout();
        }

        pvHolder.moveorderItemLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (cAppExtension.context instanceof MoveorderSelectActivity) {
                    MoveorderSelectActivity moveorderSelectActivity = (MoveorderSelectActivity)cAppExtension.activity;
                    moveorderSelectActivity.pMoveorderSelected(selectedMoveorder);
                }
           }
        });
    }

    @Override
    public int getItemCount () {
        if (localMoveorderObl != null)
            return localMoveorderObl.size();
        else return 0;
    }

    //End Region Default Methods

    //Region Public Methods
    public void pFillData(List<cMoveorder> pvDataObl) {
        localMoveorderObl = pvDataObl;
        notifyDataSetChanged();
    }

    public void pSetFilter(String pvQueryTextStr) {
        localMoveorderObl = this.mGetFilteredListObl(pvQueryTextStr);
        notifyDataSetChanged();
    }

    //End Region Public Methods


    //Region Private Methods
    private List<cMoveorder> mGetFilteredListObl(String pvQueryTextStr) {

        pvQueryTextStr = pvQueryTextStr.toLowerCase();
        List<cMoveorder> resultObl = new ArrayList<>();

        if (localMoveorderObl == null || localMoveorderObl.size() == 0) {
            return resultObl;
        }

        for (cMoveorder moveorder:localMoveorderObl)
        {
            if (moveorder.getOrderNumberStr().toLowerCase().contains(pvQueryTextStr) || moveorder.getExternalReferenceStr().toLowerCase().contains(pvQueryTextStr))
            {
                resultObl.add(moveorder);
            }
        }
        return resultObl;
    }

    //End Region Private Methods


}

