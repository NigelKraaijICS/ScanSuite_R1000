package SSU_WHS.Picken.Shipment;

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

import ICS.Utils.cText;
import ICS.cAppExtension;
import SSU_WHS.Basics.Workplaces.cWorkplace;
import SSU_WHS.Picken.Pickorders.cPickorder;
import nl.icsvertex.scansuite.Activities.Ship.ShiporderLinesActivity;
import nl.icsvertex.scansuite.R;

public class cShipmentAdapter extends RecyclerView.Adapter<cShipmentAdapter.ShipmentViewHolder> {

    //Region Public Properties

    public static class ShipmentViewHolder extends RecyclerView.ViewHolder{

        private TextView textViewDocument;
        private TextView textViewQuantity;
        private ImageView imageSendStatus;
        public LinearLayout shipmentItemLinearLayout;


        public ShipmentViewHolder(View pvItemView) {
            super(pvItemView);
            this.textViewDocument = pvItemView.findViewById(R.id.textViewDocument);
            this.textViewDocument.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            this.textViewDocument.setSingleLine(true);
            this.textViewDocument.setMarqueeRepeatLimit(5);
            this.textViewDocument.setSelected(true);
            this.textViewQuantity = pvItemView.findViewById(R.id.textViewQuantity);
            this.textViewQuantity.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            this.textViewQuantity.setSingleLine(true);
            this.textViewQuantity.setMarqueeRepeatLimit(5);
            this.textViewQuantity.setSelected(true);
            this.imageSendStatus = pvItemView.findViewById(R.id.imageSendStatus);

            this.shipmentItemLinearLayout = pvItemView.findViewById(R.id.shipmentItemLinearLayout);
        }

    }
    //End Region Public Properties

    //Region Private Properties

    private List<LinearLayout> linearLayouts = new ArrayList<>();
    private RecyclerView thisRecyclerView;
    private final LayoutInflater LayoutInflaterObject;
    private List<cShipment> localShipmentsObl;

    //End Region private Properties

    //Region Constructor
    public cShipmentAdapter() {
        this.LayoutInflaterObject = LayoutInflater.from(cAppExtension.context);
    }
    //End Region Constructor

    //Region Default Methods

    @NonNull
    @Override
    public cShipmentAdapter.ShipmentViewHolder onCreateViewHolder(@NonNull ViewGroup pvViewGroup, int pvViewTypeInt) {
        View itemView = LayoutInflaterObject.inflate(R.layout.recycler_shipment, pvViewGroup, false);
        return new ShipmentViewHolder(itemView);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView pvRecyclerView) {
        super.onAttachedToRecyclerView(pvRecyclerView);
        this.thisRecyclerView = pvRecyclerView;
    }

    @Override
    public void onBindViewHolder(cShipmentAdapter.ShipmentViewHolder pvHolder, final int pvPositionInt) {

        this.linearLayouts.add(pvHolder.shipmentItemLinearLayout);

        if (this.localShipmentsObl == null || this.localShipmentsObl.size() == 0 ) {
            return;
        }

        final cShipment shipment = this.localShipmentsObl.get(pvPositionInt);

        if (!shipment.getProcessingSequenceStr().isEmpty()) {
            pvHolder.textViewDocument.setText(shipment.getProcessingSequenceStr());
        }
        else {
            pvHolder.textViewDocument.setText(shipment.getSourceNoStr());
        }

        pvHolder.textViewQuantity.setText(cText.pIntToStringStr(shipment.getQuantityDbl().intValue()));


        if (!cPickorder.currentPickOrder.PICK_SHIPPING_QC_CHECK_COUNT()) {
            pvHolder.imageSendStatus.setVisibility(View.GONE);
        }
        else {

            if (shipment.isCheckedBln()) {
                pvHolder.imageSendStatus.setVisibility(View.VISIBLE);
                pvHolder.imageSendStatus.setImageResource(R.drawable.ic_check_black_24dp);
            }

            if (shipment.isHandledBln()) {
                pvHolder.imageSendStatus.setVisibility(View.VISIBLE);
                pvHolder.imageSendStatus.setImageResource(R.drawable.ic_doublecheck_black_24dp);
            }
        }

        if (pvPositionInt ==0) {

            if (cAppExtension.activity instanceof ShiporderLinesActivity) {
                ShiporderLinesActivity shiporderLinesActivity = (ShiporderLinesActivity)cAppExtension.activity;
                shiporderLinesActivity.pShipmentSelected(shipment);

                if (localShipmentsObl.size() == 1 && cWorkplace.currentWorkplace != null && ShiporderLinesActivity.startedFromOrderSelectBln) {
                    shiporderLinesActivity.pHandleScan(null,true);
                }

            }

        }


        final int id = thisRecyclerView.getId();
        pvHolder.shipmentItemLinearLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //deselect all
                for (LinearLayout aLayout : linearLayouts) {
                    aLayout.setSelected(false);
                }
                //select current
                v.setSelected(true);
                //wich fragment?

                if (id == R.id.recyclerViewShiporderLinesToship) {
                    if (cAppExtension.activity instanceof ShiporderLinesActivity) {
                        cShipment.currentShipment = shipment;
                            ShiporderLinesActivity shiporderLinesActivity = (ShiporderLinesActivity) cAppExtension.activity;
                            shiporderLinesActivity.pHandleScan(null,true);

                    }
                }

            }
        });

    }


    @Override
    public int getItemCount () {
        if (this.localShipmentsObl != null)
            return this.localShipmentsObl .size();
        else return 0;
    }

    //End Region Default Methods

    //Region Public Methods
    public void pFillData(List<cShipment> pvDataObl) {
        localShipmentsObl = pvDataObl;
        notifyDataSetChanged();
    }
    //End Region Public Methods
}
