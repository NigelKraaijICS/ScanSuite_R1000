package SSU_WHS.Picken.Pickorders;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
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
import nl.icsvertex.scansuite.Activities.Ship.ShiporderSelectActivity;
import nl.icsvertex.scansuite.Activities.Sort.SortorderSelectActivity;
import ICS.cAppExtension;
import SSU_WHS.General.cPublicDefinitions;
import nl.icsvertex.scansuite.Activities.Pick.PickorderSelectActivity;
import nl.icsvertex.scansuite.R;

public class cPickorderAdapter  extends RecyclerView.Adapter<cPickorderAdapter.PickorderViewHolder>  {

    //Region Public Properties

    public class PickorderViewHolder extends RecyclerView.ViewHolder{

        private TextView textViewOrdernumber;
        private TextView textViewOrderUser;
        private TextView textViewOrdertype;
        private TextView  textViewExternalreference;
        private TextView textViewCurrentLocation;
        private TextView textViewQuantityTotal;
        private ImageView imageViewIsSingleArticle;
        private ImageView imageViewIsProcessedOrWait;
        public LinearLayout pickorderItemLinearLayout;

        public PickorderViewHolder(View pvItemView) {
            super(pvItemView);

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

            this.textViewExternalreference = pvItemView.findViewById(R.id.textViewExternalreference);
            this.textViewExternalreference.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            this.textViewExternalreference.setSingleLine(true);
            this.textViewExternalreference.setMarqueeRepeatLimit(5);
            this.textViewExternalreference.setSelected(true);

            this.textViewCurrentLocation = pvItemView.findViewById(R.id.textViewCurrentLocation);

            this.textViewOrdertype = pvItemView.findViewById(R.id.textViewOrdertype);
            this.textViewQuantityTotal = pvItemView.findViewById(R.id.textViewQuantityTotal);
            this.pickorderItemLinearLayout = pvItemView.findViewById(R.id.pickorderItemLinearLayout);
            this.imageViewIsSingleArticle = pvItemView.findViewById(R.id.imageViewIsSingleArticle);
            this.imageViewIsProcessedOrWait = pvItemView.findViewById(R.id.imageViewIsProcessedOrWait);

        }
        //End Region Public Properties
    }

    //Region Private Properties
    private final LayoutInflater LayoutInflaterObject;
    private List<cPickorder> localPickorderObl;

    //End Region Private Properties

    //Region Constructor
    public cPickorderAdapter() {
        this.LayoutInflaterObject = LayoutInflater.from(cAppExtension.context);
    }
    //End Region Constructor


    //Region Default Methods
    @NonNull
    @Override
    public cPickorderAdapter.PickorderViewHolder onCreateViewHolder(@NonNull ViewGroup pvParent, int pbViewTypeInt) {
        View itemView = LayoutInflaterObject.inflate(R.layout.recycler_pickorder, pvParent, false);
        return new cPickorderAdapter.PickorderViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull cPickorderAdapter.PickorderViewHolder pvHolder, int pvPositionInt) {

        if (localPickorderObl == null || localPickorderObl.size() == 0 ) {
            return;
        }

        final cPickorder  selectedPickorder = localPickorderObl.get(pvPositionInt);


        if (selectedPickorder.getStatusInt() == 10 ) {
            pvHolder.textViewOrderUser.setText(selectedPickorder.getAssignedUserIdStr());
        }
        else {
            pvHolder.textViewOrderUser.setText(selectedPickorder.getCurrentUserIdStr());
        }

        if (!selectedPickorder.isSingleArticleOrdersBln()) {
            pvHolder.imageViewIsSingleArticle.setVisibility(View.INVISIBLE);
        }
        else {
            pvHolder.imageViewIsSingleArticle.setVisibility(View.VISIBLE);
        }

        if (selectedPickorder.getProcessingOrParkedBln()) {
            pvHolder.imageViewIsProcessedOrWait.setVisibility(View.VISIBLE);
        }
        else {
            pvHolder.imageViewIsProcessedOrWait.setVisibility(View.INVISIBLE);
        }


        pvHolder.textViewOrdernumber.setText(selectedPickorder.getOrderNumberStr());
        pvHolder.textViewOrdernumber.setTag(selectedPickorder.getOrderNumberStr());


        if (selectedPickorder.isTransferBln()) {
            pvHolder.textViewExternalreference.setText(selectedPickorder.getDestinationAndDescriptionStr());
        } else {
            pvHolder.textViewExternalreference.setText(selectedPickorder.getExternalReferenceStr());
        }

        pvHolder.textViewCurrentLocation.setText(selectedPickorder.getCurrentLocationStr());
        pvHolder.textViewCurrentLocation.setVisibility(View.VISIBLE);

        if(selectedPickorder.getQuantityTotalInt() == 0) {
            pvHolder.textViewQuantityTotal.setText("");
        }
        else {
            pvHolder.textViewQuantityTotal.setText(cText.pIntToStringStr(selectedPickorder.getQuantityTotalInt()));
        }

        if(selectedPickorder.getOrderTypeStr().equalsIgnoreCase(cPublicDefinitions.Workflows.BC.toString())) {
            pvHolder.textViewOrdertype.setText(R.string.ordertype_bc);
        }
        if(selectedPickorder.getOrderTypeStr().equalsIgnoreCase(cPublicDefinitions.Workflows.BM.toString())) {
            pvHolder.textViewOrdertype.setText(R.string.ordertype_bm);
        }
        if(selectedPickorder.getOrderTypeStr().equalsIgnoreCase(cPublicDefinitions.Workflows.BP.toString())) {
            pvHolder.textViewOrdertype.setText(R.string.ordertype_bp);
        }
        if(selectedPickorder.getOrderTypeStr().equalsIgnoreCase(cPublicDefinitions.Workflows.EOM.toString())) {
            pvHolder.textViewOrdertype.setText(R.string.ordertype_eom);
        }
        if(selectedPickorder.getOrderTypeStr().equalsIgnoreCase(cPublicDefinitions.Workflows.EOOM.toString())) {
            pvHolder.textViewOrdertype.setText(R.string.ordertype_eoom);
        }
        if(selectedPickorder.getOrderTypeStr().equalsIgnoreCase(cPublicDefinitions.Workflows.EOOS.toString())) {
            pvHolder.textViewOrdertype.setText(R.string.ordertype_eoos);
        }
        if(selectedPickorder.getOrderTypeStr().equalsIgnoreCase(cPublicDefinitions.Workflows.EOR.toString())) {
            pvHolder.textViewOrdertype.setText(R.string.ordertype_eor);
        }
        if(selectedPickorder.getOrderTypeStr().equalsIgnoreCase(cPublicDefinitions.Workflows.EOS.toString())) {
            pvHolder.textViewOrdertype.setText(R.string.ordertype_eos);
        }
        if(selectedPickorder.getOrderTypeStr().equalsIgnoreCase(cPublicDefinitions.Workflows.ER.toString())) {
            pvHolder.textViewOrdertype.setText(R.string.ordertype_er);
        }
        if(selectedPickorder.getOrderTypeStr().equalsIgnoreCase(cPublicDefinitions.Workflows.IVM.toString())) {
            pvHolder.textViewOrdertype.setText(R.string.ordertype_ivm);
        }
        if(selectedPickorder.getOrderTypeStr().equalsIgnoreCase(cPublicDefinitions.Workflows.IVS.toString())) {
            pvHolder.textViewOrdertype.setText(R.string.ordertype_ivs);
        }
        if(selectedPickorder.getOrderTypeStr().equalsIgnoreCase(cPublicDefinitions.Workflows.MAM.toString())) {
            pvHolder.textViewOrdertype.setText(R.string.ordertype_mam);
        }
        if(selectedPickorder.getOrderTypeStr().equalsIgnoreCase(cPublicDefinitions.Workflows.MAS.toString())) {
            pvHolder.textViewOrdertype.setText(R.string.ordertype_mas);
        }
        if(selectedPickorder.getOrderTypeStr().equalsIgnoreCase(cPublicDefinitions.Workflows.MAT.toString())) {
            pvHolder.textViewOrdertype.setText(R.string.ordertype_mat);
        }
        if(selectedPickorder.getOrderTypeStr().equalsIgnoreCase(cPublicDefinitions.Workflows.MI.toString())) {
            pvHolder.textViewOrdertype.setText(R.string.ordertype_mi);
        }
        if(selectedPickorder.getOrderTypeStr().equalsIgnoreCase(cPublicDefinitions.Workflows.MO.toString())) {
            pvHolder.textViewOrdertype.setText(R.string.ordertype_mo);
        }
        if(selectedPickorder.getOrderTypeStr().equalsIgnoreCase(cPublicDefinitions.Workflows.MT.toString())) {
            pvHolder.textViewOrdertype.setText(R.string.ordertype_mt);
        }
        if(selectedPickorder.getOrderTypeStr().equalsIgnoreCase(cPublicDefinitions.Workflows.MV.toString())) {
            pvHolder.textViewOrdertype.setText(R.string.ordertype_mv);
        }
        if(selectedPickorder.getOrderTypeStr().equalsIgnoreCase(cPublicDefinitions.Workflows.MVI.toString())) {
            pvHolder.textViewOrdertype.setText(R.string.ordertype_mvi);
        }
        if(selectedPickorder.getOrderTypeStr().equalsIgnoreCase(cPublicDefinitions.Workflows.OMM.toString())) {
            pvHolder.textViewOrdertype.setText(R.string.ordertype_omm);
        }
        if(selectedPickorder.getOrderTypeStr().equalsIgnoreCase(cPublicDefinitions.Workflows.OMOM.toString())) {
            pvHolder.textViewOrdertype.setText(R.string.ordertype_omom);
        }
        if(selectedPickorder.getOrderTypeStr().equalsIgnoreCase(cPublicDefinitions.Workflows.OMOS.toString())) {
            pvHolder.textViewOrdertype.setText(R.string.ordertype_omos);
        }
        if(selectedPickorder.getOrderTypeStr().equalsIgnoreCase(cPublicDefinitions.Workflows.OMR.toString())) {
            pvHolder.textViewOrdertype.setText(R.string.ordertype_omr);
        }
        if(selectedPickorder.getOrderTypeStr().equalsIgnoreCase(cPublicDefinitions.Workflows.OMS.toString())) {
            pvHolder.textViewOrdertype.setText(R.string.ordertype_oms);
        }
        if(selectedPickorder.getOrderTypeStr().equalsIgnoreCase(cPublicDefinitions.Workflows.PA.toString())) {
            pvHolder.textViewOrdertype.setText(R.string.ordertype_pa);
        }
        if(selectedPickorder.getOrderTypeStr().equalsIgnoreCase(cPublicDefinitions.Workflows.PF.toString())) {
            pvHolder.textViewOrdertype.setText(R.string.ordertype_pf);
        }
        if(selectedPickorder.getOrderTypeStr().equalsIgnoreCase(cPublicDefinitions.Workflows.PV.toString())) {
            pvHolder.textViewOrdertype.setText(R.string.ordertype_pv);
        }
        if(selectedPickorder.getOrderTypeStr().equalsIgnoreCase(cPublicDefinitions.Workflows.RVR.toString())) {
            pvHolder.textViewOrdertype.setText(R.string.ordertype_rvr);
        }
        if(selectedPickorder.getOrderTypeStr().equalsIgnoreCase(cPublicDefinitions.Workflows.RVS.toString())) {
            pvHolder.textViewOrdertype.setText(R.string.ordertype_rvs);
        }
        if(selectedPickorder.getOrderTypeStr().equalsIgnoreCase(cPublicDefinitions.Workflows.SPV.toString())) {
            pvHolder.textViewOrdertype.setText(R.string.ordertype_spv);
        }
        if(selectedPickorder.getOrderTypeStr().equalsIgnoreCase(cPublicDefinitions.Workflows.UNKNOWN.toString())) {
            pvHolder.textViewOrdertype.setText(R.string.ordertype_unknown);
        }

        pvHolder.pickorderItemLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (cAppExtension.context instanceof PickorderSelectActivity) {
                    PickorderSelectActivity.pPickorderSelected(selectedPickorder);
                }
                if (cAppExtension.context instanceof SortorderSelectActivity) {
                    SortorderSelectActivity.pSortorderSelected(selectedPickorder);
                }

                if (cAppExtension.context instanceof ShiporderSelectActivity) {
                    ShiporderSelectActivity.pShiporderSelected(selectedPickorder);
                }

            }
        });
    }

    @Override
    public int getItemCount () {
        if (localPickorderObl != null)
            return localPickorderObl.size();
        else return 0;
    }

    //End Region Default Methods

    //Region Public Methods
    public void pFillData(List<cPickorder> pvDataObl) {
        localPickorderObl = pvDataObl;
        notifyDataSetChanged();
    }

    public void pSetFilter(String pvQueryTextStr) {
        localPickorderObl = this.mGetFilteredListObl(pvQueryTextStr);
        notifyDataSetChanged();
    }

    //End Region Public Methods


    //Region Private Methods
    private List<cPickorder> mGetFilteredListObl(String pvQueryTextStr) {

        pvQueryTextStr = pvQueryTextStr.toLowerCase();
        List<cPickorder> resultObl = new ArrayList<>();

        if (localPickorderObl == null || localPickorderObl.size() == 0) {
            return resultObl;
        }

        for (cPickorder pickorder:localPickorderObl)
        {
            if (pickorder.getOrderNumberStr().toLowerCase().contains(pvQueryTextStr) || pickorder.getExternalReferenceStr().toLowerCase().contains(pvQueryTextStr) || pickorder.getCurrentLocationStr().toLowerCase().contains(pvQueryTextStr))
            {
                resultObl.add(pickorder);
            }
        }
        return resultObl;
    }

    //End Region Private Methods


}
