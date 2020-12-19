package SSU_WHS.PackAndShip.PackAndShipOrders;

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
import java.util.Locale;

import ICS.cAppExtension;
import SSU_WHS.Basics.Translations.cTranslation;
import SSU_WHS.Basics.Users.cUser;
import SSU_WHS.General.cPublicDefinitions;
import nl.icsvertex.scansuite.Activities.PackAndShip.PackAndShipSelectActivity;
import nl.icsvertex.scansuite.R;

public class cPackAndShipOrderAdapter extends RecyclerView.Adapter<cPackAndShipOrderAdapter.PackAndShipOrderViewHolder>  {

    //Region Public Properties

    public static class PackAndShipOrderViewHolder extends RecyclerView.ViewHolder{

        private final View viewOrderStatus;
        private final TextView textViewOrdernumber;
        private final TextView textViewOrderUser;
        private final TextView textViewOrdertype;
        private final TextView textViewDocument;
        private final ImageView imageViewIsProcessedOrWait;
        public LinearLayout packAndShiporderItemLinearLayout;

        public PackAndShipOrderViewHolder(View pvItemView) {
            super(pvItemView);
            this.packAndShiporderItemLinearLayout = pvItemView.findViewById(R.id.packAndShiporderItemLinearLayout);
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
            this.imageViewIsProcessedOrWait = pvItemView.findViewById(R.id.imageViewIsProcessedOrWait);


        }
        //End Region Public Properties
    }

    //Region Private Properties
    private final LayoutInflater LayoutInflaterObject;
    private List<cPackAndShipOrder> localPackAndShipOrderObl;

    //End Region Private Properties

    //Region Constructor
    public cPackAndShipOrderAdapter() {
        this.LayoutInflaterObject = LayoutInflater.from(cAppExtension.context);
    }
    //End Region Constructor


    //Region Default Methods
    @NonNull
    @Override
    public cPackAndShipOrderAdapter.PackAndShipOrderViewHolder onCreateViewHolder(@NonNull ViewGroup pvParent, int pbViewTypeInt) {
        View itemView = LayoutInflaterObject.inflate(R.layout.recycler_packandshiporder, pvParent, false);
        return new PackAndShipOrderViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull cPackAndShipOrderAdapter.PackAndShipOrderViewHolder pvHolder, int pvPositionInt) {

        if (localPackAndShipOrderObl == null || localPackAndShipOrderObl.size() == 0 ) {
            return;
        }

        final cPackAndShipOrder selectedPackAndShipOrder = localPackAndShipOrderObl.get(pvPositionInt);

        if (!selectedPackAndShipOrder.getAssignedUserIdStr().isEmpty()) {
            pvHolder.viewOrderStatus.setBackgroundResource(R.color.colorOrderStatusAssignedUser);
        }

        if (selectedPackAndShipOrder.getStatusInt() == 10 ) {
            pvHolder.textViewOrderUser.setText(cUser.pUserToShowStr(selectedPackAndShipOrder.getAssignedUserIdStr()));
        }
        else {
            pvHolder.textViewOrderUser.setText(cUser.pUserToShowStr(selectedPackAndShipOrder.getCurrentUserIdStr()));
        }

        pvHolder.textViewOrdernumber.setText(selectedPackAndShipOrder.getOrderNumberStr());
        pvHolder.textViewOrdernumber.setTag(selectedPackAndShipOrder.getOrderNumberStr());


        String orderTypeText = "";

        pvHolder.textViewOrdertype.setText(R.string.ordertype_mv);
        pvHolder.textViewDocument.setText(selectedPackAndShipOrder.getExternalReferenceStr());

        if(selectedPackAndShipOrder.getOrderTypeStr().equalsIgnoreCase(cPublicDefinitions.Workflows.PS1.toString())) {
            orderTypeText = cAppExtension.activity.getString(R.string.ordertype_ps1);
        }

        if(selectedPackAndShipOrder.getOrderTypeStr().equalsIgnoreCase(cPublicDefinitions.Workflows.PSM.toString())) {
            orderTypeText = cAppExtension.activity.getString(R.string.ordertype_psm);
        }

        pvHolder.textViewOrdertype.setText(cTranslation.pGetTranslastionStr(orderTypeText, Locale.getDefault().getLanguage().toUpperCase()));

        if (selectedPackAndShipOrder.getProcessingOrParkedBln()) {
            pvHolder.imageViewIsProcessedOrWait.setVisibility(View.VISIBLE);
        }
        else {
            pvHolder.imageViewIsProcessedOrWait.setVisibility(View.INVISIBLE);
        }

        pvHolder.packAndShiporderItemLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (cAppExtension.context instanceof PackAndShipSelectActivity) {
                    PackAndShipSelectActivity packAndShipSelectActivity = (PackAndShipSelectActivity)cAppExtension.activity;
                    packAndShipSelectActivity.pPackAndShipOrderSelected(selectedPackAndShipOrder);
                }
           }
        });
    }

    @Override
    public int getItemCount () {
        if (localPackAndShipOrderObl != null)
            return localPackAndShipOrderObl.size();
        else return 0;
    }

    //End Region Default Methods

    //Region Public Methods
    public void pFillData(List<cPackAndShipOrder> pvDataObl) {
        localPackAndShipOrderObl = pvDataObl;
        notifyDataSetChanged();
    }

    public void pSetFilter(String pvQueryTextStr) {
        localPackAndShipOrderObl = this.mGetFilteredListObl(pvQueryTextStr);
        notifyDataSetChanged();
    }

    //End Region Public Methods


    //Region Private Methods
    private List<cPackAndShipOrder> mGetFilteredListObl(String pvQueryTextStr) {

        pvQueryTextStr = pvQueryTextStr.toLowerCase();
        List<cPackAndShipOrder> resultObl = new ArrayList<>();

        if (localPackAndShipOrderObl == null || localPackAndShipOrderObl.size() == 0) {
            return resultObl;
        }

        for (cPackAndShipOrder packAndShipOrder: localPackAndShipOrderObl)
        {
            if (packAndShipOrder.getOrderNumberStr().toLowerCase().contains(pvQueryTextStr) || packAndShipOrder.getExternalReferenceStr().toLowerCase().contains(pvQueryTextStr))
            {
                resultObl.add(packAndShipOrder);
            }
        }
        return resultObl;
    }

    //End Region Private Methods


}

