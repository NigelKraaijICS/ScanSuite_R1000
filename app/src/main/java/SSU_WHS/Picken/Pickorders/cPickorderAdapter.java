package SSU_WHS.Picken.Pickorders;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import ICS.Utils.cText;
import ICS.cAppExtension;
import SSU_WHS.Basics.Translations.cTranslation;
import SSU_WHS.Basics.Users.cUser;
import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.General.cPublicDefinitions;
import nl.icsvertex.scansuite.Activities.FinishShip.FinishShiporderSelectActivity;
import nl.icsvertex.scansuite.Activities.Pick.PickorderSelectActivity;
import nl.icsvertex.scansuite.Activities.Ship.ShiporderSelectActivity;
import nl.icsvertex.scansuite.Activities.Sort.SortorderSelectActivity;
import nl.icsvertex.scansuite.R;

public class cPickorderAdapter  extends RecyclerView.Adapter<cPickorderAdapter.PickorderViewHolder>  {

    //Region Public Properties

    public static class PickorderViewHolder extends RecyclerView.ViewHolder{


        private RelativeLayout checkboxRelativelayout;
        private ImageView imageCircle;
        private ImageView imageCheck;

        private TextView textViewOrdernumber;
        private TextView textViewOrderUser;
        private TextView textViewOrdertype;
        private TextView  textViewExternalreference;
        private TextView textViewCurrentLocation;
        private TextView textViewQuantityTotal;
        private ImageView imageCurrentLocation;
        private ImageView imageViewIsSingleArticle;
        private ImageView imageViewIsProcessedOrWait;
        private ImageView imageViewUser;
        private ImageView imageChevronDown;

        public LinearLayout pickorderItemLinearLayout;

        public PickorderViewHolder(View pvItemView) {
            super(pvItemView);

            this.pickorderItemLinearLayout = pvItemView.findViewById(R.id.pickorderItemLinearLayout);

            this.checkboxRelativelayout = pvItemView.findViewById(R.id.checkboxRelativelayout);
            this.checkboxRelativelayout.setVisibility(View.GONE);

            this.imageCircle = pvItemView.findViewById(R.id.imageCircle);
            this.imageCircle.setVisibility(View.INVISIBLE);

            this.imageCheck = pvItemView.findViewById(R.id.imageCheck);
            this.imageCheck.setVisibility(View.INVISIBLE);

            this.textViewOrderUser = pvItemView.findViewById(R.id.textViewOrderUser);
            this.textViewOrderUser.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            this.textViewOrderUser.setSingleLine(true);
            this.textViewOrderUser.setMarqueeRepeatLimit(5);
            this.textViewOrderUser.setSelected(true);

            this.imageViewUser = pvItemView.findViewById(R.id.imageViewUser);

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

            this.imageCurrentLocation = pvItemView.findViewById(R.id.imageCurrentLocation);
            this.textViewCurrentLocation = pvItemView.findViewById(R.id.textViewCurrentLocation);

            this.textViewOrdertype = pvItemView.findViewById(R.id.textViewOrdertype);
            this.textViewQuantityTotal = pvItemView.findViewById(R.id.textViewQuantityTotal);
            this.imageViewIsSingleArticle = pvItemView.findViewById(R.id.imageViewIsSingleArticle);
            this.imageViewIsProcessedOrWait = pvItemView.findViewById(R.id.imageViewIsProcessedOrWait);

            this.imageChevronDown = pvItemView.findViewById(R.id.imageChevronDown);

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
        return new PickorderViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final cPickorderAdapter.PickorderViewHolder pvHolder, int pvPositionInt) {

        if (localPickorderObl == null || localPickorderObl.size() == 0 ) {
            return;
        }

        final cPickorder  selectedPickorder = localPickorderObl.get(pvPositionInt);


        if (PickorderSelectActivity.currentModusEnu == PickorderSelectActivity.ModusEnu.COMBINE) {
            pvHolder.checkboxRelativelayout.setVisibility(View.VISIBLE);
            pvHolder.imageCircle.setVisibility(View.VISIBLE);
            pvHolder.imageChevronDown.setVisibility(View.GONE);
        }
        else {
            pvHolder.checkboxRelativelayout.setVisibility(View.GONE);
            pvHolder.imageCircle.setVisibility(View.INVISIBLE);
            pvHolder.imageChevronDown.setVisibility(View.VISIBLE);
        }

        if (!selectedPickorder.getIsSelectedBln()) {
            pvHolder.imageCheck.setVisibility(View.INVISIBLE);
        }
        else
            {
                pvHolder.imageCheck.setVisibility(View.VISIBLE);
        }


        String userStr;

        if (selectedPickorder.getStatusInt() == cWarehouseorder.WorkflowPickStepEnu.PickPicking ) {
            userStr = cUser.pUserToShowStr(selectedPickorder.getAssignedUserIdStr());

        }
        else {
            userStr = cUser.pUserToShowStr(selectedPickorder.getCurrentUserIdStr());
        }

        if (Objects.requireNonNull(userStr).isEmpty()) {
            pvHolder.imageViewUser.setVisibility(View.GONE);
            pvHolder.textViewOrderUser.setVisibility(View.GONE);
        }
        else {
            pvHolder.textViewOrderUser.setVisibility(View.VISIBLE);
            pvHolder.textViewOrderUser.setText(userStr);
            pvHolder.imageViewUser.setVisibility(View.VISIBLE);
        }

        if (!selectedPickorder.isSingleArticleOrdersBln()) {
            pvHolder.imageViewIsSingleArticle.setVisibility(View.GONE);
        }
        else {
            pvHolder.imageViewIsSingleArticle.setVisibility(View.VISIBLE);
        }

        if (selectedPickorder.getProcessingOrParkedBln()) {
            pvHolder.imageViewIsProcessedOrWait.setVisibility(View.VISIBLE);
        }
        else {
            pvHolder.imageViewIsProcessedOrWait.setVisibility(View.GONE);
        }

        pvHolder.textViewOrdernumber.setText(selectedPickorder.getOrderNumberStr());
        pvHolder.textViewOrdernumber.setTag(selectedPickorder.getOrderNumberStr());


        if (selectedPickorder.isTransferBln()) {
            pvHolder.textViewExternalreference.setText(selectedPickorder.getDestinationAndDescriptionStr());
        } else {
            pvHolder.textViewExternalreference.setText(selectedPickorder.getExternalReferenceStr());
        }

        if (selectedPickorder.getCurrentLocationStr().isEmpty()) {
            pvHolder.textViewCurrentLocation.setVisibility(View.GONE);
            pvHolder.imageCurrentLocation.setVisibility(View.GONE);
        }
        else
        {
            pvHolder.textViewCurrentLocation.setText(selectedPickorder.getCurrentLocationStr());
            pvHolder.textViewCurrentLocation.setVisibility(View.VISIBLE);
        }

        if(selectedPickorder.getQuantityTotalInt() == 0) {
            pvHolder.textViewQuantityTotal.setText("");
        }
        else {
            pvHolder.textViewQuantityTotal.setText(cText.pIntToStringStr(selectedPickorder.getQuantityTotalInt()));
        }

        String orderTypeText = "";

        if(selectedPickorder.getOrderTypeStr().equalsIgnoreCase(cPublicDefinitions.Workflows.BM.toString())) {
            orderTypeText = cAppExtension.activity.getString(R.string.ordertype_bm);
        }

        if(selectedPickorder.getOrderTypeStr().equalsIgnoreCase(cPublicDefinitions.Workflows.BC.toString())) {
            orderTypeText = cAppExtension.activity.getString(R.string.ordertype_bc);
        }

        if(selectedPickorder.getOrderTypeStr().equalsIgnoreCase(cPublicDefinitions.Workflows.PV.toString())) {
            orderTypeText = cAppExtension.activity.getString(R.string.ordertype_pv);
        }

        if(selectedPickorder.getOrderTypeStr().equalsIgnoreCase(cPublicDefinitions.Workflows.BP.toString())) {
            orderTypeText = cAppExtension.activity.getString(R.string.ordertype_bp);
        }

        if(selectedPickorder.getOrderTypeStr().equalsIgnoreCase(cPublicDefinitions.Workflows.PA.toString())) {
            orderTypeText = cAppExtension.activity.getString(R.string.ordertype_pa);
        }

        if(selectedPickorder.getOrderTypeStr().equalsIgnoreCase(cPublicDefinitions.Workflows.PF.toString())) {
            orderTypeText = cAppExtension.activity.getString(R.string.ordertype_pf);
        }

        if(selectedPickorder.getOrderTypeStr().equalsIgnoreCase(cPublicDefinitions.Workflows.UNKNOWN.toString())) {
            orderTypeText = cAppExtension.activity.getString(R.string.ordertype_unknown);
        }

        if (selectedPickorder.isCombinedOrderBln()) {
            orderTypeText = cAppExtension.activity.getString(R.string.ordertype_combined);
        }

        pvHolder.textViewOrdertype.setText(cTranslation.pGetTranslastionStr(orderTypeText, Locale.getDefault().getLanguage().toUpperCase()));

        pvHolder.pickorderItemLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (cAppExtension.activity instanceof PickorderSelectActivity) {
                    PickorderSelectActivity pickorderSelectActivity = (PickorderSelectActivity)cAppExtension.activity;


                    switch (PickorderSelectActivity.currentModusEnu) {
                        case NORMAL:
                            pickorderSelectActivity.pPickorderSelected(selectedPickorder);
                            break;

                        case COMBINE:

                            if (!selectedPickorder.getIsSelectedBln()) {
                                pvHolder.imageCheck.setVisibility(View.VISIBLE);
                            }
                            else
                            {
                                pvHolder.imageCheck.setVisibility(View.INVISIBLE);
                            }

                            pickorderSelectActivity.pPickorderSelectedForCombi(selectedPickorder);
                            break;
                    }

                }

                if (cAppExtension.activity instanceof SortorderSelectActivity) {
                    SortorderSelectActivity sortorderSelectActivity = (SortorderSelectActivity)cAppExtension.activity;
                    sortorderSelectActivity.pSortorderSelected(selectedPickorder);
                }

                if (cAppExtension.activity instanceof ShiporderSelectActivity) {
                    ShiporderSelectActivity shiporderSelectActivity = (ShiporderSelectActivity)cAppExtension.activity;
                    shiporderSelectActivity.pShiporderSelected(selectedPickorder);
                }

                if (cAppExtension.activity instanceof FinishShiporderSelectActivity) {
                    FinishShiporderSelectActivity finishShiporderSelectActivity = (FinishShiporderSelectActivity)cAppExtension.activity;
                    finishShiporderSelectActivity.pFinishShiporderSelected(selectedPickorder);
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
