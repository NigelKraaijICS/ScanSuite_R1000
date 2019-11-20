package SSU_WHS.Picken.Shipment;

import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ICS.Utils.cText;
import nl.icsvertex.scansuite.R;
import nl.icsvertex.scansuite.Activities.Ship.ShiporderLinesActivity;
import ICS.cAppExtension;

public class cShipmentAdapter extends RecyclerView.Adapter<cShipmentAdapter.PickorderLinePackAndShipViewHolder> {

    //Region Public Properties

    public class PickorderLinePackAndShipViewHolder extends RecyclerView.ViewHolder{

        private TextView textViewPickorderLinePackAndShipDocument;
        private TextView textViewPickorderLinePackAndShipQuantity;
        private TextView textViewPickorderLinePackAndShipPack;
        public LinearLayout pickorderLinePackAndShipItemLinearLayout;


        public PickorderLinePackAndShipViewHolder(View pvItemView) {
            super(pvItemView);
            this.textViewPickorderLinePackAndShipDocument = pvItemView.findViewById(R.id.textViewPickorderLinePackAndShipDocument);
            this.textViewPickorderLinePackAndShipDocument.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            this.textViewPickorderLinePackAndShipDocument.setSingleLine(true);
            this.textViewPickorderLinePackAndShipDocument.setMarqueeRepeatLimit(5);
            this.textViewPickorderLinePackAndShipDocument.setSelected(true);
            this.textViewPickorderLinePackAndShipQuantity = pvItemView.findViewById(R.id.textViewPickorderLinePackAndShipQuantity);
            this.textViewPickorderLinePackAndShipQuantity.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            this.textViewPickorderLinePackAndShipQuantity.setSingleLine(true);
            this.textViewPickorderLinePackAndShipQuantity.setMarqueeRepeatLimit(5);
            this.textViewPickorderLinePackAndShipQuantity.setSelected(true);
            this.textViewPickorderLinePackAndShipPack = pvItemView.findViewById(R.id.textViewPickorderLinePackAndShipPack);
            this.textViewPickorderLinePackAndShipPack.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            this.textViewPickorderLinePackAndShipPack.setSingleLine(true);
            this.textViewPickorderLinePackAndShipPack.setMarqueeRepeatLimit(5);
            this.textViewPickorderLinePackAndShipPack.setSelected(true);
            this.pickorderLinePackAndShipItemLinearLayout = pvItemView.findViewById(R.id.pickorderLinePackAndShipItemLinearLayout);
        }

    }
    //End Region Public Properties

    //Region Private Properties

    private List<LinearLayout> pickorderLinePackAndShipItemLinearLayouts = new ArrayList<>();
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

    @Override
    public cShipmentAdapter.PickorderLinePackAndShipViewHolder onCreateViewHolder(ViewGroup pvViewGroup, int pvViewTypeInt) {
        View itemView = LayoutInflaterObject.inflate(R.layout.recycler_pickorderlinepackandship, pvViewGroup, false);
        return new cShipmentAdapter.PickorderLinePackAndShipViewHolder(itemView);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView pvRecyclerView) {
        super.onAttachedToRecyclerView(pvRecyclerView);
        this.thisRecyclerView = pvRecyclerView;
    }

    @Override
    public void onBindViewHolder(cShipmentAdapter.PickorderLinePackAndShipViewHolder pvHolder, final int pvPositionInt) {

        this.pickorderLinePackAndShipItemLinearLayouts.add(pvHolder.pickorderLinePackAndShipItemLinearLayout);

        if (this.localShipmentsObl == null || this.localShipmentsObl.size() == 0 ) {
            return;
        }


        final cShipment shipment = this.localShipmentsObl.get(pvPositionInt);

        if (!shipment.getProcessingSequenceStr().isEmpty()) {
            pvHolder.textViewPickorderLinePackAndShipDocument.setText(shipment.getProcessingSequenceStr());
        }
        else {
            pvHolder.textViewPickorderLinePackAndShipDocument.setText(shipment.getSourceNoStr());
        }

        pvHolder.textViewPickorderLinePackAndShipQuantity.setText(cText.pIntToStringStr(shipment.getQuantityDbl().intValue()));
        pvHolder.textViewPickorderLinePackAndShipPack.setText("");

        if (pvPositionInt ==0) {
           ShiporderLinesActivity.pShipmentSelected(shipment);
        }


        final int id = thisRecyclerView.getId();
        pvHolder.pickorderLinePackAndShipItemLinearLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //deselect all
                for (LinearLayout aLayout : pickorderLinePackAndShipItemLinearLayouts) {
                    aLayout.setSelected(false);
                }
                //select current
                v.setSelected(true);
                //wich fragment?

                if (id == R.id.recyclerViewShiporderLinesToship) {
                    if (cAppExtension.activity instanceof ShiporderLinesActivity) {

                        cShipment.currentShipment = shipment;
                        ShiporderLinesActivity.pHandleScan("",true);
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
