package SSU_WHS.Inventory.InventoryOrders;

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
import nl.icsvertex.scansuite.Activities.Inventory.InventoryorderSelectActivity;
import nl.icsvertex.scansuite.R;

public class cInventoryorderAdapter  extends RecyclerView.Adapter<cInventoryorderAdapter.InventoryorderViewHolder>  {

    //Region Public Properties

    public class InventoryorderViewHolder extends RecyclerView.ViewHolder{

        private View viewOrderStatus;
        private TextView textViewOrdernumber;
        private TextView textViewOrderUser;
        private TextView textViewOrdertype;
        private TextView  textViewDocument;
        private TextView textViewQuantityTotal;
        private ImageView imageViewIsProcessedOrWait;
        public LinearLayout inventoryorderItemLinearLayout;

        public InventoryorderViewHolder(View pvItemView) {
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
    private List<cInventoryorder> localInventoryorderObl;

    //End Region Private Properties

    //Region Constructor
    public cInventoryorderAdapter() {
        this.LayoutInflaterObject = LayoutInflater.from(cAppExtension.context);
    }
    //End Region Constructor


    //Region Default Methods
    @Override
    public cInventoryorderAdapter.InventoryorderViewHolder onCreateViewHolder(ViewGroup pvParent, int pbViewTypeInt) {
        View itemView = LayoutInflaterObject.inflate(R.layout.recycler_inventoryorder, pvParent, false);
        return new cInventoryorderAdapter.InventoryorderViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(cInventoryorderAdapter.InventoryorderViewHolder pvHolder, int pvPositionInt) {

        if (localInventoryorderObl == null || localInventoryorderObl.size() == 0 ) {
            return;
        }

        final cInventoryorder selectedInventoryorder = localInventoryorderObl.get(pvPositionInt);

        if (!selectedInventoryorder.getAssignedUserIdStr().isEmpty()) {
            pvHolder.viewOrderStatus.setBackgroundResource(R.color.colorOrderStatusAssignedUser);
        }

        if (selectedInventoryorder.getStatusInt() == 10 ) {
            pvHolder.textViewOrderUser.setText(selectedInventoryorder.getAssignedUserIdStr());
        }
        else {
            pvHolder.textViewOrderUser.setText(selectedInventoryorder.getCurrentUserIdStr());
        }

        pvHolder.textViewOrdernumber.setText(selectedInventoryorder.getOrderNumberStr());
        pvHolder.textViewOrdernumber.setTag(selectedInventoryorder.getOrderNumberStr());
        pvHolder.textViewDocument.setText(selectedInventoryorder.getDocumentStr());
        pvHolder.textViewQuantityTotal.setText(Integer.toString(selectedInventoryorder.getNumberOfBinsInt()));

        if(selectedInventoryorder.getOrderTypeStr().equalsIgnoreCase(cPublicDefinitions.Workflows.IVM.toString())) {
            pvHolder.textViewOrdertype.setText(R.string.ordertype_ivm);
        }
        if(selectedInventoryorder.getOrderTypeStr().equalsIgnoreCase(cPublicDefinitions.Workflows.IVS.toString())) {
            pvHolder.textViewOrdertype.setText(R.string.ordertype_ivs);
        }


        pvHolder.inventoryorderItemLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (cAppExtension.context instanceof InventoryorderSelectActivity) {
                    InventoryorderSelectActivity.pInventoryorderSelected(selectedInventoryorder);
                    return;
                }
           }
        });
    }

    @Override
    public int getItemCount () {
        if (localInventoryorderObl != null)
            return localInventoryorderObl.size();
        else return 0;
    }

    //End Region Default Methods

    //Region Public Methods
    public void pFillData(List<cInventoryorder> pvDataObl) {
        localInventoryorderObl = pvDataObl;
        notifyDataSetChanged();
    }

    public void pSetFilter(String pvQueryTextStr) {
        localInventoryorderObl = this.mGetFilteredListObl(pvQueryTextStr);
        notifyDataSetChanged();
    }

    //End Region Public Methods


    //Region Private Methods
    private List<cInventoryorder> mGetFilteredListObl(String pvQueryTextStr) {

        pvQueryTextStr = pvQueryTextStr.toLowerCase();
        List<cInventoryorder> resultObl = new ArrayList<>();

        if (localInventoryorderObl == null || localInventoryorderObl.size() == 0) {
            return resultObl;
        }

        for (cInventoryorder inventoryorder:localInventoryorderObl)
        {
            if (inventoryorder.getOrderNumberStr().toLowerCase().contains(pvQueryTextStr) || inventoryorder.getExternalReferenceStr().toLowerCase().contains(pvQueryTextStr))
            {
                resultObl.add(inventoryorder);
            }
        }
        return resultObl;
    }

    //End Region Private Methods


}

