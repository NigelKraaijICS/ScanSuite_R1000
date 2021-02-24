package SSU_WHS.Move.MoveorderBarcodes;

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
import SSU_WHS.Basics.ArticleBarcode.cArticleBarcode;
import SSU_WHS.Move.MoveOrders.cMoveorder;
import SSU_WHS.Move.MoveorderLines.cMoveorderLine;
import nl.icsvertex.scansuite.Activities.Move.MoveLinePlaceGeneratedActivity;
import nl.icsvertex.scansuite.Activities.Move.MoveLinePlaceMTActivity;
import nl.icsvertex.scansuite.Activities.Move.MoveLineTakeActivity;
import nl.icsvertex.scansuite.Activities.Move.MoveLineTakeMTActivity;
import nl.icsvertex.scansuite.R;

public class cMoveorderBarcodeAdapter extends RecyclerView.Adapter<cMoveorderBarcodeAdapter.moveorderBarcodeViewHolder>  {

    private cArticleBarcode articleBarcode = null;
    private cMoveorderBarcode moveorderBarcode = null;

    //Region Public Properties
    public static class moveorderBarcodeViewHolder extends RecyclerView.ViewHolder{
        private final TextView textViewBarcode;
        private final TextView textViewQuantity;
        public LinearLayout barcodeItemLinearLayout;

        public moveorderBarcodeViewHolder(View pvItemView) {
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
    public cMoveorderBarcodeAdapter() {
        this.layoutInflaterObject = LayoutInflater.from(cAppExtension.context);
    }
    //End Region Constructor

    //Region Public Methods

    @NonNull
    @Override
    public moveorderBarcodeViewHolder onCreateViewHolder(@NonNull ViewGroup pvParent, int pvViewTypeInt) {
        View itemView = this.layoutInflaterObject.inflate(R.layout.recycler_barcode, pvParent, false);
        return new moveorderBarcodeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull moveorderBarcodeViewHolder pvHolder, int pvPositionInt) {

        if (cAppExtension.activity instanceof MoveLineTakeActivity) {
            if (cMoveorder.currentMoveOrder.currentArticle == null || cMoveorder.currentMoveOrder.currentArticle.barcodesObl == null || cMoveorder.currentMoveOrder.currentArticle.barcodesObl.size() == 0) {
                return;
            }

            articleBarcode   = cMoveorder.currentMoveOrder.currentArticle.barcodesObl.get(pvPositionInt);
            Objects.requireNonNull(pvHolder).textViewBarcode.setText(articleBarcode.getBarcodeStr());
            pvHolder.textViewQuantity.setText(cText.pDoubleToStringStr(articleBarcode.getQuantityPerUnitOfMeasureDbl()));
        }

        if (cAppExtension.activity instanceof MoveLineTakeMTActivity) {
            if (cMoveorderLine.currentMoveOrderLine == null || cMoveorderLine.currentMoveOrderLine.orderBarcodesObl() == null || cMoveorderLine.currentMoveOrderLine.orderBarcodesObl() .size() == 0) {
                return;
            }

            moveorderBarcode =    cMoveorderLine.currentMoveOrderLine.orderBarcodesObl() .get(pvPositionInt);
            Objects.requireNonNull(pvHolder).textViewBarcode.setText(moveorderBarcode.getBarcodeStr());
            pvHolder.textViewQuantity.setText(cText.pDoubleToStringStr(moveorderBarcode.getQuantityPerUnitOfMeasureDbl()));
        }


        pvHolder.barcodeItemLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (cAppExtension.activity instanceof MoveLineTakeActivity) {
                    MoveLineTakeActivity moveLineTakeActivity = (MoveLineTakeActivity)cAppExtension.activity;
                    moveLineTakeActivity.pHandleScan(cBarcodeScan.pFakeScan(articleBarcode.getBarcodeStr()));

                }

                if (cAppExtension.activity instanceof MoveLineTakeMTActivity) {
                    MoveLineTakeMTActivity moveLineTakeMTActivity = (MoveLineTakeMTActivity)cAppExtension.activity;
                    moveLineTakeMTActivity.pHandleScan(cBarcodeScan.pFakeScan(moveorderBarcode.getBarcodeStr()));
                }
                if (cAppExtension.activity instanceof MoveLinePlaceMTActivity) {
                    MoveLinePlaceMTActivity moveLinePlaceMTActivity = (MoveLinePlaceMTActivity)cAppExtension.activity;
                    moveLinePlaceMTActivity.pHandleScan(cBarcodeScan.pFakeScan(moveorderBarcode.getBarcodeStr()));
                }


                if (cAppExtension.activity instanceof MoveLinePlaceGeneratedActivity) {
                    MoveLinePlaceGeneratedActivity moveLinePlaceGeneratedActivity = (MoveLinePlaceGeneratedActivity)cAppExtension.activity;
                    moveLinePlaceGeneratedActivity.pHandleScan(cBarcodeScan.pFakeScan(moveorderBarcode.getBarcodeStr()));
                }

            }
        });
    }

    @Override
    public int getItemCount () {

        if (cAppExtension.activity instanceof MoveLineTakeActivity) {
            if (cMoveorder.currentMoveOrder.currentArticle != null && cMoveorder.currentMoveOrder.currentArticle.barcodesObl != null)
                return cMoveorder.currentMoveOrder.currentArticle.barcodesObl.size();
            else return 0;
        }

        if (cAppExtension.activity instanceof MoveLineTakeMTActivity) {
            if (cMoveorderLine.currentMoveOrderLine != null && cMoveorderLine.currentMoveOrderLine.orderBarcodesObl()  != null)
                return cMoveorderLine.currentMoveOrderLine.orderBarcodesObl() .size();
            else return 0;
        }

        if (cAppExtension.activity instanceof MoveLinePlaceMTActivity) {
            if (cMoveorderLine.currentMoveOrderLine != null && cMoveorderLine.currentMoveOrderLine.orderBarcodesObl()  != null)
                return cMoveorderLine.currentMoveOrderLine.orderBarcodesObl() .size();
            else return 0;
        }

        if (cAppExtension.activity instanceof MoveLinePlaceGeneratedActivity) {
            if (cMoveorderLine.currentMoveOrderLine != null && cMoveorderLine.currentMoveOrderLine.orderBarcodesObl()  != null)
                return cMoveorderLine.currentMoveOrderLine.orderBarcodesObl() .size();
            else return 0;
        }

        return  0;
    }
}
