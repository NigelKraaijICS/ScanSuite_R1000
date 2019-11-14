package SSU_WHS.Intake.IntakeorderBarcodes;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cText;
import ICS.cAppExtension;
import SSU_WHS.Intake.IntakeorderMATLines.cIntakeorderMATLine;
import nl.icsvertex.scansuite.Activities.intake.IntakeOrderIntakeActivity;
import nl.icsvertex.scansuite.R;

public class cIntakeorderBarcodeAdapter extends RecyclerView.Adapter<cIntakeorderBarcodeAdapter.intakeorderBarcodeViewHolder>  {

    //Region Public Properties
    public class intakeorderBarcodeViewHolder extends RecyclerView.ViewHolder{
        private TextView textViewBarcode;
        private TextView textViewQuantity;
        public LinearLayout barcodeItemLinearLayout;

        public intakeorderBarcodeViewHolder(View pvItemView) {
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
    public cIntakeorderBarcodeAdapter() {
        this.layoutInflaterObject = LayoutInflater.from(cAppExtension.context);
    }
    //End Region Constructor

    //Region Public Methods

    @Override
    public intakeorderBarcodeViewHolder onCreateViewHolder(ViewGroup pvParent, int pvViewTypeInt) {
        View itemView = this.layoutInflaterObject.inflate(R.layout.recycler_barcode, pvParent, false);
        return new intakeorderBarcodeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(intakeorderBarcodeViewHolder pvHolder, int pvPositionInt) {

        if (cIntakeorderMATLine.currentIntakeorderMATLine == null || cIntakeorderMATLine.currentIntakeorderMATLine.barcodesObl == null || cIntakeorderMATLine.currentIntakeorderMATLine.barcodesObl.size() == 0) {
            return;
        }

       final cIntakeorderBarcode intakeorderBarcode = cIntakeorderMATLine.currentIntakeorderMATLine.barcodesObl.get(pvPositionInt);
        pvHolder.textViewBarcode.setText(intakeorderBarcode.getBarcodeStr());
        pvHolder.textViewQuantity.setText(cText.pDoubleToStringStr(intakeorderBarcode.getQuantityPerUnitOfMeasureDbl()));

        pvHolder.barcodeItemLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                cBarcodeScan barcodeScan = new cBarcodeScan();
                barcodeScan.barcodeOriginalStr = intakeorderBarcode.getBarcodeStr();
                barcodeScan.barcodeStr = intakeorderBarcode.getBarcodeStr();
                barcodeScan.barcodeTypeStr = cText.pIntToStringStr(cBarcodeScan.BarcodeType.EAN13);
                IntakeOrderIntakeActivity.pHandleScan(barcodeScan);

            }
        });
    }

    @Override
    public int getItemCount () {
        if (cIntakeorderMATLine.currentIntakeorderMATLine != null && cIntakeorderMATLine.currentIntakeorderMATLine.barcodesObl != null)
            return cIntakeorderMATLine.currentIntakeorderMATLine.barcodesObl.size();
        else return 0;
    }
}
