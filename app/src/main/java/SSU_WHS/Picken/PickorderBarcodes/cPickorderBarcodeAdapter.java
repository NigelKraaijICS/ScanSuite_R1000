package SSU_WHS.Picken.PickorderBarcodes;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;

import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cText;
import ICS.cAppExtension;
import SSU_WHS.Picken.PickorderLinePackAndShip.cPickorderLinePackAndShip;
import SSU_WHS.Picken.PickorderLines.cPickorderLine;
import nl.icsvertex.scansuite.Activities.Pick.PickorderPickActivity;
import nl.icsvertex.scansuite.Activities.QualityControl.PickorderQCActivity;
import nl.icsvertex.scansuite.R;

public class cPickorderBarcodeAdapter extends RecyclerView.Adapter<cPickorderBarcodeAdapter.pickorderBarcodeViewHolder>  {

    //Region Public Properties
    public static class pickorderBarcodeViewHolder extends RecyclerView.ViewHolder{
        private TextView textViewBarcode;
        private TextView textViewQuantity;
        public LinearLayout barcodeItemLinearLayout;

        public pickorderBarcodeViewHolder(View pvItemView) {
            super(pvItemView);
            this.textViewBarcode = pvItemView.findViewById(R.id.textViewBarcode);

            this.textViewBarcode.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            this.textViewBarcode.setSingleLine(true);
            this.textViewBarcode.setMarqueeRepeatLimit(5);
            this.textViewBarcode.setSelected(true);
            this.textViewQuantity = pvItemView.findViewById(R.id.textViewQuantity);
            this.barcodeItemLinearLayout = pvItemView.findViewById(R.id.barcodeLinearLayout);
        }
    }
    //End Region Public Properties

    //Region Private Properties
    private final LayoutInflater layoutInflaterObject;
    //End Region Private Properties

    //Region Constructor
    public cPickorderBarcodeAdapter() {
        this.layoutInflaterObject = LayoutInflater.from(cAppExtension.context);
    }
    //End Region Constructor

    //Region Public Methods

    @NonNull
    @Override
    public pickorderBarcodeViewHolder onCreateViewHolder(@NonNull ViewGroup pvParent, int pvViewTypeInt) {
        View itemView = this.layoutInflaterObject.inflate(R.layout.recycler_barcode, pvParent, false);
        return new pickorderBarcodeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull pickorderBarcodeViewHolder pvHolder, int pvPositionInt) {

        if (cAppExtension.activity instanceof  PickorderPickActivity) {
            if (cPickorderLine.currentPickOrderLine == null || cPickorderLine.currentPickOrderLine.barcodesObl == null || cPickorderLine.currentPickOrderLine.barcodesObl.size() == 0) {
                return;
            }

            final cPickorderBarcode pickorderBarcode = cPickorderLine.currentPickOrderLine.barcodesObl.get(pvPositionInt);
            Objects.requireNonNull(pvHolder).textViewBarcode.setText(pickorderBarcode.getBarcodeStr());
            pvHolder.textViewQuantity.setText(cText.pDoubleToStringStr(pickorderBarcode.getQuantityPerUnitOfMeasureDbl()));

            pvHolder.barcodeItemLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        PickorderPickActivity pickorderPickActivity = (PickorderPickActivity)cAppExtension.activity;
                        pickorderPickActivity.pHandleScan(cBarcodeScan.pFakeScan(pickorderBarcode.getBarcodeStr()));
                }
            });
        }

        if (cAppExtension.activity instanceof PickorderQCActivity) {
            if (cPickorderLinePackAndShip.currentPickorderLinePackAndShip == null || cPickorderLinePackAndShip.currentPickorderLinePackAndShip .barcodesObl == null || cPickorderLinePackAndShip.currentPickorderLinePackAndShip.barcodesObl.size() == 0) {
                return;
            }

            final cPickorderBarcode pickorderBarcode = cPickorderLinePackAndShip.currentPickorderLinePackAndShip .barcodesObl.get(pvPositionInt);
            Objects.requireNonNull(pvHolder).textViewBarcode.setText(pickorderBarcode.getBarcodeStr());
            pvHolder.textViewQuantity.setText(cText.pDoubleToStringStr(pickorderBarcode.getQuantityPerUnitOfMeasureDbl()));

            pvHolder.barcodeItemLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PickorderQCActivity pickorderQCActivity = (PickorderQCActivity)cAppExtension.activity;
                    pickorderQCActivity.pHandleScan(cBarcodeScan.pFakeScan(pickorderBarcode.getBarcodeStr()));
                }
            });
        }


    }

    @Override
    public int getItemCount () {


        if (cAppExtension.activity instanceof  PickorderPickActivity) {
            if (cPickorderLine.currentPickOrderLine != null && cPickorderLine.currentPickOrderLine.barcodesObl != null)
                return cPickorderLine.currentPickOrderLine.barcodesObl.size();
            else return 0;
        }
        if (cAppExtension.activity instanceof PickorderQCActivity) {
            if (cPickorderLinePackAndShip.currentPickorderLinePackAndShip != null && cPickorderLinePackAndShip.currentPickorderLinePackAndShip.barcodesObl != null)
                return cPickorderLinePackAndShip.currentPickorderLinePackAndShip.barcodesObl.size();
            else return 0;
        }


        return  0;

    }
}
