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

    @Override
    public pickorderBarcodeViewHolder onCreateViewHolder(ViewGroup pvParent, int pvViewTypeInt) {
        View itemView = this.layoutInflaterObject.inflate(R.layout.recycler_barcode, pvParent, false);
        return new pickorderBarcodeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(pickorderBarcodeViewHolder pvHolder, int pvPositionInt) {

        if (cPickorderLine.currentPickOrderLine == null || cPickorderLine.currentPickOrderLine.barcodesObl == null || cPickorderLine.currentPickOrderLine.barcodesObl.size() == 0) {
            return;
        }

       final cPickorderBarcode pickorderBarcode = cPickorderLine.currentPickOrderLine.barcodesObl.get(pvPositionInt);
        pvHolder.textViewBarcode.setText(pickorderBarcode.getBarcodeStr());
        pvHolder.textViewQuantity.setText(cText.pDoubleToStringStr(pickorderBarcode.getQuantityPerUnitOfMeasureDbl()));

        pvHolder.barcodeItemLinearLayout.setOnClickListener(new View.OnClickListener() {
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
