package SSU_WHS.Picken.FinishSinglePieceLine;

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
import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.Picken.Pickorders.cPickorder;
import SSU_WHS.Picken.Shipment.cShipment;
import nl.icsvertex.scansuite.Activities.FinishShip.FinishShipLinesActivity;
import nl.icsvertex.scansuite.Activities.Ship.ShiporderLinesActivity;
import nl.icsvertex.scansuite.R;

public class cPickorderLineFinishSinglePieceAdapter extends RecyclerView.Adapter<cPickorderLineFinishSinglePieceAdapter.PickorderLinePackAndShipViewHolder> {

    //Region Public Properties

    public static class PickorderLinePackAndShipViewHolder extends RecyclerView.ViewHolder{

        private TextView textViewDocument;
        private TextView textViewItemNoAndVariant;
        private  TextView textViewDescription;
        private ImageView imageSendStatus;
        public LinearLayout pickorderLinePackAndShipItemLinearLayout;


        public PickorderLinePackAndShipViewHolder(View pvItemView) {
            super(pvItemView);
            this.textViewDocument = pvItemView.findViewById(R.id.textViewDocument);
            this.textViewDocument.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            this.textViewDocument.setSingleLine(true);
            this.textViewDocument.setMarqueeRepeatLimit(5);
            this.textViewDocument.setSelected(true);

            this.textViewItemNoAndVariant = pvItemView.findViewById(R.id.textViewItemNoAndVariant);
            this.textViewItemNoAndVariant.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            this.textViewItemNoAndVariant.setSingleLine(true);
            this.textViewItemNoAndVariant.setMarqueeRepeatLimit(5);
            this.textViewItemNoAndVariant.setSelected(true);

            this.textViewDescription = pvItemView.findViewById(R.id.textViewDescription);
            this.textViewDescription.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            this.textViewDescription.setSingleLine(true);
            this.textViewDescription.setMarqueeRepeatLimit(5);
            this.textViewDescription.setSelected(true);

            this.imageSendStatus = pvItemView.findViewById(R.id.imageSendStatus);

            this.pickorderLinePackAndShipItemLinearLayout = pvItemView.findViewById(R.id.pickorderLinePackAndShipItemLinearLayout);
        }

    }
    //End Region Public Properties

    //Region Private Properties

    private List<LinearLayout> pickorderLinePackAndShipItemLinearLayouts = new ArrayList<>();
    private RecyclerView thisRecyclerView;
    private final LayoutInflater LayoutInflaterObject;
    private List<cPickorderLineFinishSinglePiece> localLinesObl;

    //End Region private Properties

    //Region Constructor
    public cPickorderLineFinishSinglePieceAdapter() {
        this.LayoutInflaterObject = LayoutInflater.from(cAppExtension.context);
    }
    //End Region Constructor

    //Region Default Methods

    @NonNull
    @Override
    public cPickorderLineFinishSinglePieceAdapter.PickorderLinePackAndShipViewHolder onCreateViewHolder(@NonNull ViewGroup pvViewGroup, int pvViewTypeInt) {
        View itemView = LayoutInflaterObject.inflate(R.layout.recycler_finish_singlepieceline, pvViewGroup, false);
        return new PickorderLinePackAndShipViewHolder(itemView);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView pvRecyclerView) {
        super.onAttachedToRecyclerView(pvRecyclerView);
        this.thisRecyclerView = pvRecyclerView;
    }

    @Override
    public void onBindViewHolder(cPickorderLineFinishSinglePieceAdapter.PickorderLinePackAndShipViewHolder pvHolder, final int pvPositionInt) {

        this.pickorderLinePackAndShipItemLinearLayouts.add(pvHolder.pickorderLinePackAndShipItemLinearLayout);

        if (this.localLinesObl == null || this.localLinesObl.size() == 0 ) {
            return;
        }

        final cPickorderLineFinishSinglePiece pickorderLineFinishSinglePiece = this.localLinesObl.get(pvPositionInt);

        pvHolder.textViewDocument.setText(pickorderLineFinishSinglePiece.getSourceNoStr());
        pvHolder.textViewItemNoAndVariant.setText(pickorderLineFinishSinglePiece.getItemNoStr() + "~" + pickorderLineFinishSinglePiece.getVariantCodeStr() );
        pvHolder.textViewDescription.setText(pickorderLineFinishSinglePiece.getDescriptionStr());

        if (pickorderLineFinishSinglePiece.getLocalStatusInt() == cWarehouseorder.PicklineLocalStatusEnu.LOCALSTATUS_NEW) {
            pvHolder.imageSendStatus.setVisibility(View.GONE);
        }
    }


    @Override
    public int getItemCount () {
        if (this.localLinesObl != null)
            return this.localLinesObl .size();
        else return 0;
    }

    //End Region Default Methods

    //Region Public Methods
    public void pFillData(List<cPickorderLineFinishSinglePiece> pvDataObl) {
        localLinesObl = pvDataObl;
        notifyDataSetChanged();
    }
    //End Region Public Methods
}
