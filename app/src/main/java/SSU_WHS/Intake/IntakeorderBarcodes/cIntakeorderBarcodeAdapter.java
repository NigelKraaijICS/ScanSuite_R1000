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
import SSU_WHS.Intake.IntakeorderMATLineSummary.cIntakeorderMATSummaryLine;
import SSU_WHS.Intake.Intakeorders.cIntakeorder;
import SSU_WHS.Receive.ReceiveSummaryLine.cReceiveorderSummaryLine;
import nl.icsvertex.scansuite.Activities.Intake.IntakeOrderIntakeActivity;
import nl.icsvertex.scansuite.Activities.Receive.ReceiveOrderReceiveActivity;
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
    private static cIntakeorderBarcode  intakeorderBarcode;
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


        if (cAppExtension.activity instanceof  IntakeOrderIntakeActivity) {
            if ( cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine == null || cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.barcodesObl() == null || cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.barcodesObl().size() == 0) {
                return;
            }

           cIntakeorderBarcodeAdapter.intakeorderBarcode = cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.barcodesObl().get(pvPositionInt);
        }

        if (cAppExtension.activity instanceof  ReceiveOrderReceiveActivity) {
            if ( cReceiveorderSummaryLine.currentReceiveorderSummaryLine == null || cReceiveorderSummaryLine.currentReceiveorderSummaryLine.barcodesObl() == null || cReceiveorderSummaryLine.currentReceiveorderSummaryLine.barcodesObl().size() == 0) {
                return;
            }
            cIntakeorderBarcodeAdapter.intakeorderBarcode=  cReceiveorderSummaryLine.currentReceiveorderSummaryLine.barcodesObl().get(pvPositionInt);
        }

        pvHolder.textViewBarcode.setText(cIntakeorderBarcodeAdapter.intakeorderBarcode.getBarcodeStr());
        pvHolder.textViewQuantity.setText(cText.pDoubleToStringStr(cIntakeorderBarcodeAdapter.intakeorderBarcode.getQuantityPerUnitOfMeasureDbl()));

        pvHolder.barcodeItemLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (cAppExtension.activity instanceof  IntakeOrderIntakeActivity) {
                    IntakeOrderIntakeActivity.pHandleScan(cBarcodeScan.pFakeScan(cIntakeorderBarcodeAdapter.intakeorderBarcode.getBarcodeStr()));
                }

                if (cAppExtension.activity instanceof ReceiveOrderReceiveActivity) {
                    cIntakeorder.currentIntakeOrder.intakeorderBarcodeScanned = cIntakeorderBarcodeAdapter.intakeorderBarcode;
                    ReceiveOrderReceiveActivity.pHandleScan(cBarcodeScan.pFakeScan(cIntakeorderBarcodeAdapter.intakeorderBarcode.getBarcodeStr()));
                }

            }
        });
    }

    @Override
    public int getItemCount () {

        if (cAppExtension.activity instanceof  IntakeOrderIntakeActivity) {
            if (cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine != null && cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.barcodesObl() != null) {
                return cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.barcodesObl().size();
            }
        }

        if (cAppExtension.activity instanceof  ReceiveOrderReceiveActivity) {
            if (cReceiveorderSummaryLine.currentReceiveorderSummaryLine != null && cReceiveorderSummaryLine.currentReceiveorderSummaryLine.barcodesObl() != null) {
                return cReceiveorderSummaryLine.currentReceiveorderSummaryLine.barcodesObl().size();
            }
        }

        return 0;
    }

}
