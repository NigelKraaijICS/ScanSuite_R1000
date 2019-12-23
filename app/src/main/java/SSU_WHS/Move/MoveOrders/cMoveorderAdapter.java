package SSU_WHS.Move.MoveOrders;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ICS.cAppExtension;
import SSU_WHS.General.cPublicDefinitions;
import nl.icsvertex.scansuite.Activities.Move.MoveorderSelectActivity;
import nl.icsvertex.scansuite.R;

public class cMoveorderAdapter extends RecyclerView.Adapter<cMoveorderAdapter.MoveorderViewHolder>  {

    //Region Public Properties

    public class MoveorderViewHolder extends RecyclerView.ViewHolder{

        private View viewOrderStatus;
        private TextView textViewOrdernumber;
        private TextView textViewOrderUser;
        private TextView textViewOrdertype;
        private TextView  textViewDocument;
        private TextView textViewQuantityTotal;
        private ImageView imageViewIsProcessedOrWait;
        public LinearLayout inventoryorderItemLinearLayout;

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
            this.inventoryorderItemLinearLayout = pvItemView.findViewById(R.id.inventoryorderItemLinearLayout);
            this.imageViewIsProcessedOrWait = pvItemView.findViewById(R.id.imageViewIsProcessedOrWait);
            this.textViewQuantityTotal = pvItemView.findViewById(R.id.textViewQuantityTotal);

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
    @Override
    public cMoveorderAdapter.MoveorderViewHolder onCreateViewHolder(ViewGroup pvParent, int pbViewTypeInt) {
        View itemView = LayoutInflaterObject.inflate(R.layout.recycler_inventoryorder, pvParent, false);
        return new cMoveorderAdapter.MoveorderViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(cMoveorderAdapter.MoveorderViewHolder pvHolder, int pvPositionInt) {

        if (localMoveorderObl == null || localMoveorderObl.size() == 0 ) {
            return;
        }

        final cMoveorder selectedMoveorder = localMoveorderObl.get(pvPositionInt);

        if (!selectedMoveorder.getAssignedUserIdStr().isEmpty()) {
            pvHolder.viewOrderStatus.setBackgroundResource(R.color.colorOrderStatusAssignedUser);
        }

        if (selectedMoveorder.getStatusInt() == 10 ) {
            pvHolder.textViewOrderUser.setText(selectedMoveorder.getAssignedUserIdStr());
        }
        else {
            pvHolder.textViewOrderUser.setText(selectedMoveorder.getCurrentUserIdStr());
        }

        pvHolder.textViewOrdernumber.setText(selectedMoveorder.getOrderNumberStr());
        pvHolder.textViewOrdernumber.setTag(selectedMoveorder.getOrderNumberStr());
        pvHolder.textViewDocument.setText(selectedMoveorder.getDocumentStr());
        pvHolder.textViewQuantityTotal.setText(Integer.toString(selectedMoveorder.getNumberOfBinsInt()));

        if(selectedMoveorder.getOrderTypeStr().equalsIgnoreCase(cPublicDefinitions.Workflows.MV.toString())) {
            pvHolder.textViewOrdertype.setText(R.string.ordertype_mv);
        }

        pvHolder.inventoryorderItemLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //TODO Do this:
                if (cAppExtension.context instanceof MoveorderSelectActivity) {
                    MoveorderSelectActivity.pMoveorderSelected(selectedMoveorder);
                    return;
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

