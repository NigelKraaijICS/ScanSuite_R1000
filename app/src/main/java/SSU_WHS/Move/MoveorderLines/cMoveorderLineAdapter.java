package SSU_WHS.Move.MoveorderLines;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ICS.Utils.cText;
import ICS.cAppExtension;
import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.Move.MoveOrders.cMoveorder;
import nl.icsvertex.scansuite.Activities.Move.MoveLinesActivity;
import nl.icsvertex.scansuite.Activities.Move.MoveLinesPlaceMTActivity;
import nl.icsvertex.scansuite.Activities.Move.MoveLinesTakeMTActivity;
import nl.icsvertex.scansuite.Fragments.Move.MoveLinesPlaceFragment;
import nl.icsvertex.scansuite.R;

public class cMoveorderLineAdapter extends RecyclerView.Adapter<cMoveorderLineAdapter.MoveorderLineViewHolder> {

    //Region Public Properties

    public static class MoveorderLineViewHolder extends RecyclerView.ViewHolder{

        private TextView textViewArticle;
        private TextView textViewDescription;
        private TextView textViewCounted;
        private TextView textViewBIN;
        private ImageView imageViewBIN;
        public RelativeLayout viewBackground;
        public ConstraintLayout viewForeground;

        public MoveorderLineViewHolder(View pvItemView) {
            super(pvItemView);

            this.textViewArticle = pvItemView.findViewById(R.id.textViewArticle);
            this.textViewArticle.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            this.textViewArticle.setSingleLine(true);
            this.textViewArticle.setMarqueeRepeatLimit(5);
            this.textViewArticle.postDelayed(new Runnable() {
                @Override
                public void run() {
                    textViewArticle.setSelected(true);
                }
            },1500);

            this.textViewDescription = pvItemView.findViewById(R.id.textViewDescription);
            this.textViewDescription.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            this.textViewDescription.setSingleLine(true);
            this.textViewDescription.setMarqueeRepeatLimit(5);
            this.textViewDescription.postDelayed(new Runnable() {
                @Override
                public void run() {
                    textViewDescription.setSelected(true);
                }
            },1500);

            this.textViewCounted = pvItemView.findViewById(R.id.textViewCounted);
            this.textViewBIN = pvItemView.findViewById(R.id.textViewBIN);
            this.imageViewBIN  = pvItemView.findViewById(R.id.imageViewBIN);
            this.viewBackground = pvItemView.findViewById(R.id.view_background);
            this.viewForeground = pvItemView.findViewById(R.id.view_foreground);
        }
        //End Region Public Properties
    }

    //Region Private Properties

    private final LayoutInflater LayoutInflaterObject;

    private List<cMoveorderLine> localMoveorderLinesObl;

    //End Region Private Properties

    //Region Constructor

    public cMoveorderLineAdapter() {
        this.LayoutInflaterObject = LayoutInflater.from(cAppExtension.context);
    }

    //End Region Constructor


    //Region Default Methods

    @NonNull
    @Override
    public cMoveorderLineAdapter.MoveorderLineViewHolder onCreateViewHolder(@NonNull ViewGroup pvParent, int pbViewTypeInt) {
        View itemView = LayoutInflaterObject.inflate(R.layout.recycler_moveorderline, pvParent, false);
        return new MoveorderLineViewHolder(itemView);
    }
    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView pvRecyclerView) {
        super.onAttachedToRecyclerView(pvRecyclerView);
    }

    @Override
    public void onBindViewHolder(@NonNull cMoveorderLineAdapter.MoveorderLineViewHolder pvHolder, int pvPositionInt) {

        if (localMoveorderLinesObl == null || localMoveorderLinesObl.size() == 0 ) {
            return;
        }

        final cMoveorderLine moveorderLine = localMoveorderLinesObl.get(pvPositionInt);

        pvHolder.textViewArticle.setText(moveorderLine.getItemNoAndVariantCodeStr());

        pvHolder.textViewDescription.setText(moveorderLine.getDescriptionStr() + " " + moveorderLine.getDescription2Str());
        pvHolder.textViewDescription.setVisibility(View.VISIBLE);

        String quantityToShowStr = "";

        if (moveorderLine.getActionTypeCodeStr().equalsIgnoreCase(cWarehouseorder.ActionTypeEnu.TAKE.toString())) {


            switch (cMoveorder.currentMoveOrder.getOrderTypeStr()) {
                case "MV":
                    quantityToShowStr = cText.pDoubleToStringStr(moveorderLine.getQuantityHandledDbl());
                    break;


                case "MT":
                    quantityToShowStr  =  cText.pDoubleToStringStr(moveorderLine.getQuantityHandledDbl()) + "/" + cText.pDoubleToStringStr(moveorderLine.getQuantityDbl());
                    break;
            }

            pvHolder.textViewCounted.setText(quantityToShowStr);
            pvHolder.textViewBIN.setText(moveorderLine.getBinCodeStr());
        }

        if (moveorderLine.getActionTypeCodeStr().equalsIgnoreCase(cWarehouseorder.ActionTypeEnu.PLACE.toString())) {

            if (cMoveorder.currentMoveOrder.getOrderTypeStr().equalsIgnoreCase("MV")) {
                if (moveorderLine.getQuantityHandledDbl() == 0) {
                    pvHolder.textViewCounted.setText(cText.pDoubleToStringStr(moveorderLine.getQuantityHandledDbl()) + "/" + cText.pDoubleToStringStr(moveorderLine.getQuantityDbl()));
                }
                else
                {
                    pvHolder.textViewCounted.setText(cText.pDoubleToStringStr(moveorderLine.getQuantityHandledDbl()));
                }

                if (moveorderLine.getBinCodeStr().isEmpty()) {
                    pvHolder.textViewBIN.setVisibility(View.GONE);
                    pvHolder.imageViewBIN.setVisibility(View.GONE);
                }
                else
                {
                    pvHolder.textViewBIN.setVisibility(View.VISIBLE);
                    pvHolder.imageViewBIN.setVisibility(View.VISIBLE);
                    pvHolder.textViewBIN.setText(moveorderLine.getBinCodeStr());
                }
            }

            if (cMoveorder.currentMoveOrder.getOrderTypeStr().equalsIgnoreCase("MT")) {
                if (moveorderLine.getQuantityHandledDbl() == 0) {
                    pvHolder.textViewCounted.setText(cText.pDoubleToStringStr(moveorderLine.getQuantityHandledDbl()) + "/" + cText.pDoubleToStringStr(moveorderLine.getQuantityTakenDbl()));
                }
                else
                {
                    pvHolder.textViewCounted.setText(cText.pDoubleToStringStr(moveorderLine.getQuantityTakenDbl()));
                }

                if (moveorderLine.getBinCodeStr().isEmpty()) {
                    pvHolder.textViewBIN.setVisibility(View.GONE);
                    pvHolder.imageViewBIN.setVisibility(View.GONE);
                }
                else
                {
                    pvHolder.textViewBIN.setVisibility(View.VISIBLE);
                    pvHolder.imageViewBIN.setVisibility(View.VISIBLE);
                    pvHolder.textViewBIN.setText(moveorderLine.getBinCodeStr());
                }
            }

        }

    }

    @Override
    public int getItemCount () {
        if (localMoveorderLinesObl != null)
            return localMoveorderLinesObl.size();
        else return 0;
    }

    //End Region Default Methods

    //Region Public Methods


    public void pFillData(List<cMoveorderLine> pvDataObl) {
        this.localMoveorderLinesObl = pvDataObl;

        if (cAppExtension.activity instanceof MoveLinesActivity) {
            MoveLinesActivity moveLinesActivity = (MoveLinesActivity)cAppExtension.activity ;
            moveLinesActivity.pChangeToolBarSubText(cAppExtension.activity.getString(R.string.lines) + " " + cText.pIntToStringStr(pvDataObl.size()) );
        }

        if (cAppExtension.activity instanceof MoveLinesTakeMTActivity) {
            MoveLinesTakeMTActivity moveLinesTakeMTActivity = (MoveLinesTakeMTActivity)cAppExtension.activity ;
            moveLinesTakeMTActivity.pChangeToolBarSubText(cAppExtension.activity.getString(R.string.lines) + " " + cText.pIntToStringStr(pvDataObl.size()) );
            moveLinesTakeMTActivity.pShowData( this.localMoveorderLinesObl);
        }

        if (cAppExtension.activity instanceof MoveLinesPlaceMTActivity) {
            MoveLinesPlaceMTActivity moveLinesPlaceMTActivity = (MoveLinesPlaceMTActivity)cAppExtension.activity ;
            moveLinesPlaceMTActivity.pChangeToolBarSubText(cAppExtension.activity.getString(R.string.lines) + " " + cText.pIntToStringStr(pvDataObl.size()) );
            moveLinesPlaceMTActivity.pShowData( this.localMoveorderLinesObl);
        }
    }

    public void pShowTodo() {
        this.localMoveorderLinesObl = cMoveorder.currentMoveOrder.placeLinesTodoObl();

        if (cAppExtension.activity instanceof MoveLinesActivity) {
            MoveLinesActivity moveLinesActivity = (MoveLinesActivity) cAppExtension.activity;

            //if PickorderLinesPickedFragment is the current fragment
            if (moveLinesActivity.currentLineFragment instanceof MoveLinesPlaceFragment ) {

                MoveLinesPlaceFragment moveLinesPlaceFragment = (MoveLinesPlaceFragment)moveLinesActivity.currentLineFragment;

                //if there are no defects, then show no linesInt fragment
                if (this.localMoveorderLinesObl.size() == 0 ) {
                    moveLinesPlaceFragment.pNoLinesAvailable(true);
                    moveLinesActivity.pChangeToolBarSubText(cAppExtension.activity.getString(R.string.lines) + " 0 / " + cText.pIntToStringStr(cMoveorder.currentMoveOrder.placeLinesObl.size()));
                }
                // There are linesInt to show, so refresh the fragent then all linesInt are shown again
                else {
                    moveLinesPlaceFragment.pGetData( this.localMoveorderLinesObl);
                    moveLinesActivity.pChangeToolBarSubText(cAppExtension.activity.getString(R.string.lines) + " " +  cText.pIntToStringStr(this.localMoveorderLinesObl.size()) + " / " + cText.pIntToStringStr(cMoveorder.currentMoveOrder.placeLinesObl.size()));
                }

                notifyDataSetChanged();
            }


        }
        }

    public void pShowTAKETodo(String pvBinCodeStr) {

        this.localMoveorderLinesObl = cMoveorder.currentMoveOrder.takeLinesTodoObl(pvBinCodeStr);

        if (cAppExtension.activity instanceof MoveLinesTakeMTActivity) {
            MoveLinesTakeMTActivity moveLinesTakeMTActivity = (MoveLinesTakeMTActivity) cAppExtension.activity;

                //if there are no defects, then show no linesInt fragment
                if (this.localMoveorderLinesObl.size() == 0 ) {
                    moveLinesTakeMTActivity.pShowData(this.localMoveorderLinesObl);
                    moveLinesTakeMTActivity.pChangeToolBarSubText(cAppExtension.activity.getString(R.string.lines) + " 0 / " + cText.pIntToStringStr(cMoveorder.currentMoveOrder.takeLinesObl.size()));
                }
                // There are linesInt to show, so refresh the fragent then all linesInt are shown again
                else {
                    moveLinesTakeMTActivity.pShowData( this.localMoveorderLinesObl);
                    moveLinesTakeMTActivity.pChangeToolBarSubText(cAppExtension.activity.getString(R.string.lines) + " " +  cText.pIntToStringStr(this.localMoveorderLinesObl.size()) + " / " + cText.pIntToStringStr(cMoveorder.currentMoveOrder.takeLinesObl.size()));
                }

            cAppExtension.activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    notifyDataSetChanged();
                }
            });
        }
    }

    public void pShowPLACETodo(String pvBinCodeStr) {

        this.localMoveorderLinesObl = cMoveorder.currentMoveOrder.placeLinesTodoObl(pvBinCodeStr);

        if (cAppExtension.activity instanceof MoveLinesPlaceMTActivity) {
            MoveLinesPlaceMTActivity moveLinesPlaceMTActivity = (MoveLinesPlaceMTActivity) cAppExtension.activity;

            //if there are no defects, then show no linesInt fragment
            if (this.localMoveorderLinesObl.size() == 0 ) {
                moveLinesPlaceMTActivity.pShowData(this.localMoveorderLinesObl);
                moveLinesPlaceMTActivity.pChangeToolBarSubText(cAppExtension.activity.getString(R.string.lines) + " 0 / " + cText.pIntToStringStr(cMoveorder.currentMoveOrder.placeLinesObl.size()));
            }
            // There are linesInt to show, so refresh the fragent then all linesInt are shown again
            else {
                moveLinesPlaceMTActivity.pShowData( this.localMoveorderLinesObl);
                moveLinesPlaceMTActivity.pChangeToolBarSubText(cAppExtension.activity.getString(R.string.lines) + " " +  cText.pIntToStringStr(this.localMoveorderLinesObl.size()) + " / " + cText.pIntToStringStr(cMoveorder.currentMoveOrder.placeLinesObl.size()));
            }

            cAppExtension.activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    notifyDataSetChanged();
                }
            });
        }
    }

    //End Region Public Methods


    //Region Private Methods

    //End Region Private Methods
}
