package SSU_WHS.Move.MoveorderLines;

import android.media.Image;
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
import SSU_WHS.Move.MoveItemVariant.cMoveItemVariant;
import SSU_WHS.Move.Moveorders.cMoveorder;
import nl.icsvertex.scansuite.Activities.Move.MoveLinePlaceGeneratedActivity;
import nl.icsvertex.scansuite.Activities.Move.MoveLinesActivity;
import nl.icsvertex.scansuite.Activities.Move.MoveLinesPlaceMTActivity;
import nl.icsvertex.scansuite.Activities.Move.MoveLinesTakeMTActivity;
import nl.icsvertex.scansuite.Fragments.Move.MoveLinesPlaceFragment;
import nl.icsvertex.scansuite.R;

public class cMoveorderLineAdapter extends RecyclerView.Adapter<cMoveorderLineAdapter.MoveorderLineViewHolder> {

    //Region Public Properties

    public static class MoveorderLineViewHolder extends RecyclerView.ViewHolder{

        private final TextView textViewArticle;
        private final TextView textViewDescription;
        private final TextView textViewCounted;
        private final TextView textViewBIN;
        private final ImageView imageViewDescription;
        private final ImageView imageViewBIN;
        private final ImageView imageViewCounted;
        private final View menuSeparator;
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
            this.imageViewDescription = pvItemView.findViewById(R.id.imageViewDescription);
            this.imageViewBIN  = pvItemView.findViewById(R.id.imageViewBIN);
            this.imageViewCounted = pvItemView.findViewById(R.id.imageViewCounted);
            this.viewBackground = pvItemView.findViewById(R.id.view_background);
            this.viewForeground = pvItemView.findViewById(R.id.view_foreground);
            this.menuSeparator = pvItemView.findViewById(R.id.menuSeparator);
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

        pvHolder.textViewDescription.setText(moveorderLine.getDescriptionExtendedStr());
        pvHolder.textViewDescription.setVisibility(View.VISIBLE);

        if (moveorderLine.getItemNoAndVariantCodeStr().equalsIgnoreCase(moveorderLine.getDescriptionExtendedStr())) {
            pvHolder.textViewDescription.setVisibility(View.GONE);
            pvHolder.imageViewDescription.setVisibility(View.GONE);
        }

        String quantityToShowStr;

        if (moveorderLine.getActionTypeCodeStr().equalsIgnoreCase(cWarehouseorder.ActionTypeEnu.TAKE.toString())) {


            switch (cMoveorder.currentMoveOrder.getOrderTypeStr()) {
                case "MV":
                    quantityToShowStr = cText.pDoubleToStringStr(moveorderLine.getQuantityHandledDbl());
                    pvHolder.textViewCounted.setText(quantityToShowStr);
                    break;


                case "MT":
                    quantityToShowStr  =  cText.pDoubleToStringStr(moveorderLine.getQuantityHandledDbl()) + "/" + cText.pDoubleToStringStr(moveorderLine.getQuantityDbl());
                    pvHolder.textViewCounted.setText(quantityToShowStr);
                    break;
            }


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

                quantityToShowStr = cText.pDoubleToStringStr(moveorderLine.getQuantityHandledDbl()) + "/" + cText.pDoubleToStringStr(moveorderLine.getQuantityPlaceable()) ;
                pvHolder.textViewCounted.setText(quantityToShowStr);

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

            if (cMoveorder.currentMoveOrder.getOrderTypeStr().equalsIgnoreCase("MI")) {
                quantityToShowStr = cText.pDoubleToStringStr(moveorderLine.getQuantityHandledDbl());

                if (moveorderLine.isUniqueBln()) {
                    pvHolder.textViewCounted.setVisibility(View.GONE);
                    pvHolder.imageViewCounted.setVisibility(View.GONE);
                } else {
                    pvHolder.textViewCounted.setText(quantityToShowStr);
                }

                if (cAppExtension.activity instanceof MoveLinePlaceGeneratedActivity) {
                    pvHolder.textViewBIN.setVisibility(View.GONE);
                    pvHolder.imageViewBIN.setVisibility(View.GONE);
                    pvHolder.menuSeparator.setVisibility(View.GONE);
                }
                else
                {
                    pvHolder.textViewBIN.setVisibility(View.VISIBLE);
                    pvHolder.imageViewBIN.setVisibility(View.VISIBLE);
                    pvHolder.textViewBIN.setText(moveorderLine.getBinCodeStr());
                    pvHolder.menuSeparator.setVisibility(View.VISIBLE);
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
