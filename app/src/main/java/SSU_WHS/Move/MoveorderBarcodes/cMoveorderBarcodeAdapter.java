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
import nl.icsvertex.scansuite.Activities.Move.MoveLineTakeActivity;
import nl.icsvertex.scansuite.R;

public class cMoveorderBarcodeAdapter extends RecyclerView.Adapter<cMoveorderBarcodeAdapter.moveorderBarcodeViewHolder>  {

    //Region Public Properties
    public static class moveorderBarcodeViewHolder extends RecyclerView.ViewHolder{
        private TextView textViewBarcode;
        private TextView textViewQuantity;
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

        if (cMoveorder.currentMoveOrder.currentArticle == null || cMoveorder.currentMoveOrder.currentArticle.barcodesObl == null || cMoveorder.currentMoveOrder.currentArticle.barcodesObl.size() == 0) {
            return;
        }

       final cArticleBarcode articleBarcode = cMoveorder.currentMoveOrder.currentArticle.barcodesObl.get(pvPositionInt);
        Objects.requireNonNull(pvHolder).textViewBarcode.setText(articleBarcode.getBarcodeStr());
        pvHolder.textViewQuantity.setText(cText.pDoubleToStringStr(articleBarcode.getQuantityPerUnitOfMeasureDbl()));

        pvHolder.barcodeItemLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (cAppExtension.activity instanceof MoveLineTakeActivity) {
                    MoveLineTakeActivity moveLineTakeActivity = (MoveLineTakeActivity)cAppExtension.activity;
                    moveLineTakeActivity.pHandleScan(cBarcodeScan.pFakeScan(articleBarcode.getBarcodeStr()));

                }
            }
        });
    }

    @Override
    public int getItemCount () {
        if (cMoveorder.currentMoveOrder.currentArticle != null && cMoveorder.currentMoveOrder.currentArticle.barcodesObl != null)
            return cMoveorder.currentMoveOrder.currentArticle.barcodesObl.size();
        else return 0;
    }
}
