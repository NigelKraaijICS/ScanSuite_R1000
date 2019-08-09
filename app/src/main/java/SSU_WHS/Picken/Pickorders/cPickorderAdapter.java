package SSU_WHS.Picken.Pickorders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ICS.Utils.cText;
import nl.icsvertex.scansuite.activities.pick.PickorderSelectActivity;
import nl.icsvertex.scansuite.activities.ship.ShiporderSelectActivity;
import nl.icsvertex.scansuite.R;
import nl.icsvertex.scansuite.activities.sort.SortorderSelectActivity;

public class cPickorderAdapter  extends RecyclerView.Adapter<cPickorderAdapter.PickorderViewHolder>  {
    private Context callerContext;
    public class PickorderViewHolder extends RecyclerView.ViewHolder{
        private View viewOrderStatus;
        private TextView textViewOrdernumber;
        private TextView textViewOrderUser;
        private TextView textViewOrdertype;
        private TextView  textViewExternalreference;
        private TextView textViewCurrentLocation;
        private TextView textViewQuantityTotal;
        private ImageView imageViewPickorder;
        private ImageView imageViewIsSingleArticle;
        private ImageView imageViewIsProcessedOrWait;
        public LinearLayout pickorderItemLinearLayout;

        public PickorderViewHolder(View itemView) {
            super(itemView);
            viewOrderStatus = itemView.findViewById(R.id.viewOrderStatus);
            textViewOrderUser = itemView.findViewById(R.id.textViewOrderUser);
            textViewOrdernumber = itemView.findViewById(R.id.textViewOrdernumber);
            textViewCurrentLocation = itemView.findViewById(R.id.textViewCurrentLocation);
            textViewOrdernumber.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            textViewOrdernumber.setSingleLine(true);
            textViewOrdernumber.setMarqueeRepeatLimit(5);
            textViewOrdernumber.setSelected(true);
            textViewExternalreference = itemView.findViewById(R.id.textViewExternalreference);
            textViewOrdertype = itemView.findViewById(R.id.textViewOrdertype);
            textViewQuantityTotal = itemView.findViewById(R.id.textViewQuantityTotal);
            imageViewPickorder = itemView.findViewById(R.id.imageViewPickorder);
            pickorderItemLinearLayout = itemView.findViewById(R.id.pickorderItemLinearLayout);
            imageViewIsSingleArticle = itemView.findViewById(R.id.imageViewIsSingleArticle);
            imageViewIsProcessedOrWait = itemView.findViewById(R.id.imageViewIsProcessedOrWait);

        }
    }

    private final LayoutInflater mInflater;
    List<cPickorderEntity> mPickorders; //cached copy of pickorders
    List<cPickorderEntity> mAllPickorders;

    public cPickorderAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        callerContext = context;
    }

    @Override
    public cPickorderAdapter.PickorderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recycler_pickorder, parent, false);
        return new cPickorderAdapter.PickorderViewHolder(itemView);
    }

    public void setPickorders(List<cPickorderEntity> pickorders) {
        mAllPickorders = pickorders;
        mPickorders = pickorders;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(cPickorderAdapter.PickorderViewHolder holder, int position) {
        if (mPickorders != null) {
            final cPickorderEntity l_PickorderEntity = mPickorders.get(position);
            if (!l_PickorderEntity.getAssignedUserId().trim().isEmpty()) {
                holder.viewOrderStatus.setBackgroundResource(R.color.colorOrderStatusAssignedUser);
            }
            if (l_PickorderEntity.getStatus().equals(10) ) {
                holder.textViewOrderUser.setText(l_PickorderEntity.getAssignedUserId());
            }
            else {
                holder.textViewOrderUser.setText(l_PickorderEntity.getCurrentUserId());
            }

            if (l_PickorderEntity.getSingleArticleOrders().equalsIgnoreCase("0")) {
                holder.imageViewIsSingleArticle.setVisibility(View.INVISIBLE);
            }
            else {
                holder.imageViewIsSingleArticle.setVisibility(View.VISIBLE);
            }
            if (l_PickorderEntity.getIsprocessingorparked()) {
                holder.imageViewIsProcessedOrWait.setVisibility(View.VISIBLE);
            }
            else {
                holder.imageViewIsProcessedOrWait.setVisibility(View.INVISIBLE);
            }
            String l_pickordernumberStr = l_PickorderEntity.getOrdernumber();
            String l_externalReferenceStr = l_PickorderEntity.getExternalReference();
            String l_orderTypeStr = l_PickorderEntity.getOrdertype();
            holder.textViewOrdernumber.setText(l_pickordernumberStr);
            holder.textViewOrdernumber.setTag(l_pickordernumberStr);
            holder.textViewExternalreference.setText(l_externalReferenceStr);

            String l_CurrentlocationStr = l_PickorderEntity.getCurrentlocation();
            holder.textViewCurrentLocation.setText(l_CurrentlocationStr);
            holder.textViewCurrentLocation.setVisibility(View.VISIBLE);

            Integer quantityTotal = cText.stringToInteger(l_PickorderEntity.getQuantitytotal());
            if(quantityTotal == 0) {
                holder.textViewQuantityTotal.setText("");
            }
            else {
                holder.textViewQuantityTotal.setText(Integer.toString(quantityTotal));
            }

            if(l_orderTypeStr.equalsIgnoreCase(cPickorder.eOrderTypes.BC.toString())) {
                holder.textViewOrdertype.setText(R.string.ordertype_bc);
            }
            if(l_orderTypeStr.equalsIgnoreCase(cPickorder.eOrderTypes.BM.toString())) {
                holder.textViewOrdertype.setText(R.string.ordertype_bm);
            }
            if(l_orderTypeStr.equalsIgnoreCase(cPickorder.eOrderTypes.BP.toString())) {
                holder.textViewOrdertype.setText(R.string.ordertype_bp);
            }
            if(l_orderTypeStr.equalsIgnoreCase(cPickorder.eOrderTypes.EOM.toString())) {
                holder.textViewOrdertype.setText(R.string.ordertype_eom);
            }
            if(l_orderTypeStr.equalsIgnoreCase(cPickorder.eOrderTypes.EOOM.toString())) {
                holder.textViewOrdertype.setText(R.string.ordertype_eoom);
            }
            if(l_orderTypeStr.equalsIgnoreCase(cPickorder.eOrderTypes.EOOS.toString())) {
                holder.textViewOrdertype.setText(R.string.ordertype_eoos);
            }
            if(l_orderTypeStr.equalsIgnoreCase(cPickorder.eOrderTypes.EOR.toString())) {
                holder.textViewOrdertype.setText(R.string.ordertype_eor);
            }
            if(l_orderTypeStr.equalsIgnoreCase(cPickorder.eOrderTypes.EOS.toString())) {
                holder.textViewOrdertype.setText(R.string.ordertype_eos);
            }
            if(l_orderTypeStr.equalsIgnoreCase(cPickorder.eOrderTypes.ER.toString())) {
                holder.textViewOrdertype.setText(R.string.ordertype_er);
            }
            if(l_orderTypeStr.equalsIgnoreCase(cPickorder.eOrderTypes.IVM.toString())) {
                holder.textViewOrdertype.setText(R.string.ordertype_ivm);
            }
            if(l_orderTypeStr.equalsIgnoreCase(cPickorder.eOrderTypes.IVS.toString())) {
                holder.textViewOrdertype.setText(R.string.ordertype_ivs);
            }
            if(l_orderTypeStr.equalsIgnoreCase(cPickorder.eOrderTypes.MAM.toString())) {
                holder.textViewOrdertype.setText(R.string.ordertype_mam);
            }
            if(l_orderTypeStr.equalsIgnoreCase(cPickorder.eOrderTypes.MAS.toString())) {
                holder.textViewOrdertype.setText(R.string.ordertype_mas);
            }
            if(l_orderTypeStr.equalsIgnoreCase(cPickorder.eOrderTypes.MAT.toString())) {
                holder.textViewOrdertype.setText(R.string.ordertype_mat);
            }
            if(l_orderTypeStr.equalsIgnoreCase(cPickorder.eOrderTypes.MI.toString())) {
                holder.textViewOrdertype.setText(R.string.ordertype_mi);
            }
            if(l_orderTypeStr.equalsIgnoreCase(cPickorder.eOrderTypes.MO.toString())) {
                holder.textViewOrdertype.setText(R.string.ordertype_mo);
            }
            if(l_orderTypeStr.equalsIgnoreCase(cPickorder.eOrderTypes.MT.toString())) {
                holder.textViewOrdertype.setText(R.string.ordertype_mt);
            }
            if(l_orderTypeStr.equalsIgnoreCase(cPickorder.eOrderTypes.MV.toString())) {
                holder.textViewOrdertype.setText(R.string.ordertype_mv);
            }
            if(l_orderTypeStr.equalsIgnoreCase(cPickorder.eOrderTypes.MVI.toString())) {
                holder.textViewOrdertype.setText(R.string.ordertype_mvi);
            }
            if(l_orderTypeStr.equalsIgnoreCase(cPickorder.eOrderTypes.OMM.toString())) {
                holder.textViewOrdertype.setText(R.string.ordertype_omm);
            }
            if(l_orderTypeStr.equalsIgnoreCase(cPickorder.eOrderTypes.OMOM.toString())) {
                holder.textViewOrdertype.setText(R.string.ordertype_omom);
            }
            if(l_orderTypeStr.equalsIgnoreCase(cPickorder.eOrderTypes.OMOS.toString())) {
                holder.textViewOrdertype.setText(R.string.ordertype_omos);
            }
            if(l_orderTypeStr.equalsIgnoreCase(cPickorder.eOrderTypes.OMR.toString())) {
                holder.textViewOrdertype.setText(R.string.ordertype_omr);
            }
            if(l_orderTypeStr.equalsIgnoreCase(cPickorder.eOrderTypes.OMS.toString())) {
                holder.textViewOrdertype.setText(R.string.ordertype_oms);
            }
            if(l_orderTypeStr.equalsIgnoreCase(cPickorder.eOrderTypes.PA.toString())) {
                holder.textViewOrdertype.setText(R.string.ordertype_pa);
            }
            if(l_orderTypeStr.equalsIgnoreCase(cPickorder.eOrderTypes.PF.toString())) {
                holder.textViewOrdertype.setText(R.string.ordertype_pf);
            }
            if(l_orderTypeStr.equalsIgnoreCase(cPickorder.eOrderTypes.PV.toString())) {
                holder.textViewOrdertype.setText(R.string.ordertype_pv);
            }
            if(l_orderTypeStr.equalsIgnoreCase(cPickorder.eOrderTypes.RVR.toString())) {
                holder.textViewOrdertype.setText(R.string.ordertype_rvr);
            }
            if(l_orderTypeStr.equalsIgnoreCase(cPickorder.eOrderTypes.RVS.toString())) {
                holder.textViewOrdertype.setText(R.string.ordertype_rvs);
            }
            if(l_orderTypeStr.equalsIgnoreCase(cPickorder.eOrderTypes.SPV.toString())) {
                holder.textViewOrdertype.setText(R.string.ordertype_spv);
            }
            if(l_orderTypeStr.equalsIgnoreCase(cPickorder.eOrderTypes.UNKNOWN.toString())) {
                holder.textViewOrdertype.setText(R.string.ordertype_unknown);
            }

            holder.pickorderItemLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (callerContext instanceof PickorderSelectActivity) {
                        ((PickorderSelectActivity)callerContext).setChosenPickorder(l_PickorderEntity);
                    }
                    if (callerContext instanceof SortorderSelectActivity) {
                        ((SortorderSelectActivity)callerContext).setChosenSortorder(l_PickorderEntity);
                    }
                    if (callerContext instanceof ShiporderSelectActivity) {
                        ((ShiporderSelectActivity)callerContext).setChosenShiporder(l_PickorderEntity);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount () {
        if (mPickorders != null)
            return mPickorders.size();
        else return 0;
    }
    public void setFilter(String queryText) {
        mPickorders = filteredList(queryText);
        notifyDataSetChanged();
    }
    private List<cPickorderEntity> filteredList(String queryText) {
        queryText = queryText.toLowerCase();
        List<cPickorderEntity> filterPickorderList = new ArrayList<>();
        if (mAllPickorders == null) {
            return filterPickorderList;
        }
        for (cPickorderEntity pickorder:mAllPickorders)
        {
            String ordernumber = pickorder.getOrdernumber().toLowerCase();
            String externalreference = pickorder.getExternalReference().toLowerCase();
            String currentlocation = pickorder.getCurrentlocation().toLowerCase();
            if (ordernumber.contains(queryText) || externalreference.contains(queryText) || currentlocation.contains(queryText))
            {
                filterPickorderList.add(pickorder);
            }
        }
        return filterPickorderList;
    }
}
