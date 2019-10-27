package SSU_WHS.Picken.PickorderBarcodes;

import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import ICS.Utils.cText;
import SSU_WHS.Picken.PickorderLines.cPickorderLine;
import nl.icsvertex.scansuite.Activities.pick.PickorderPickActivity;
import ICS.cAppExtension;
import nl.icsvertex.scansuite.R;

public class cPickorderBarcodeAdapter extends RecyclerView.Adapter<cPickorderBarcodeAdapter.pickorderBarcodeViewHolder>  {

    //Region Public Properties
    public class pickorderBarcodeViewHolder extends RecyclerView.ViewHolder{
        private TextView textViewPickorderLineBarcode;
        private TextView textViewPickorderLineBarcodeQuantity;
        public LinearLayout pickorderLineBarcodeItemLinearLayout;

        public pickorderBarcodeViewHolder(View pvItemView) {
            super(pvItemView);
            this.textViewPickorderLineBarcode = pvItemView.findViewById(R.id.textViewPickorderLineBarcode);

            this.textViewPickorderLineBarcode.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            this.textViewPickorderLineBarcode.setSingleLine(true);
            this.textViewPickorderLineBarcode.setMarqueeRepeatLimit(5);
            this.textViewPickorderLineBarcode.setSelected(true);
            this.textViewPickorderLineBarcodeQuantity = pvItemView.findViewById(R.id.textViewPickorderLineBarcodeQuantity);
            this.pickorderLineBarcodeItemLinearLayout = pvItemView.findViewById(R.id.pickorderLineBarcodeItemLinearLayout);
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

    @Override
    public pickorderBarcodeViewHolder onCreateViewHolder(ViewGroup pvParent, int pvViewTypeInt) {
        View itemView = this.layoutInflaterObject.inflate(R.layout.recycler_pickorderlinebarcode, pvParent, false);
        return new pickorderBarcodeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(pickorderBarcodeViewHolder pvHolder, int pvPositionInt) {

        if (cPickorderLine.currentPickOrderLine == null || cPickorderLine.currentPickOrderLine.barcodesObl == null || cPickorderLine.currentPickOrderLine.barcodesObl.size() == 0) {
            return;
        }

       final cPickorderBarcode pickorderBarcode = cPickorderLine.currentPickOrderLine.barcodesObl.get(pvPositionInt);
        pvHolder.textViewPickorderLineBarcode.setText(pickorderBarcode.getBarcodeStr());
        pvHolder.textViewPickorderLineBarcodeQuantity.setText(cText.pDoubleToStringStr(pickorderBarcode.getQuantityPerUnitOfMeasureDbl()));

        pvHolder.pickorderLineBarcodeItemLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
        PickorderPickActivity.pHandleScan(pickorderBarcode.getBarcodeStr());
            }
        });
    }

    @Override
    public int getItemCount () {
        if (cPickorderLine.currentPickOrderLine != null && cPickorderLine.currentPickOrderLine.barcodesObl != null)
            return cPickorderLine.currentPickOrderLine.barcodesObl.size();
        else return 0;
    }
}
