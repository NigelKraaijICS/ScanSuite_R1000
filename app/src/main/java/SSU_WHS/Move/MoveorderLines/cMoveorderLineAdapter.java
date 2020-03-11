package SSU_WHS.Move.MoveorderLines;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ICS.Utils.cText;
import ICS.cAppExtension;
import nl.icsvertex.scansuite.R;

public class cMoveorderLineAdapter extends RecyclerView.Adapter<cMoveorderLineAdapter.MoveorderLineViewHolder> {
    //Region Public Properties

    public class MoveorderLineViewHolder extends RecyclerView.ViewHolder{

        private TextView textViewArticle;
        private TextView textViewDescription;
        private TextView textViewCounted;

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
            this.viewBackground = pvItemView.findViewById(R.id.view_background);
            this.viewForeground = pvItemView.findViewById(R.id.view_foreground);
        }
        //End Region Public Properties
    }

    //Region Private Properties
    private final LayoutInflater LayoutInflaterObject;
    private static List<cMoveorderLine> localMoveorderLineObl;
    private RecyclerView thisRecyclerView;

    //End Region Private Properties

    //Region Constructor
    public cMoveorderLineAdapter() {
        this.LayoutInflaterObject = LayoutInflater.from(cAppExtension.context);
    }
    //End Region Constructor


    //Region Default Methods
    @Override
    public cMoveorderLineAdapter.MoveorderLineViewHolder onCreateViewHolder(ViewGroup pvParent, int pbViewTypeInt) {
        View itemView = LayoutInflaterObject.inflate(R.layout.recycler_moveorderline, pvParent, false);
        return new cMoveorderLineAdapter.MoveorderLineViewHolder(itemView);
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView pvRecyclerView) {
        this.thisRecyclerView = pvRecyclerView;
        super.onAttachedToRecyclerView( this.thisRecyclerView);
    }

    @Override
    public void onBindViewHolder(cMoveorderLineAdapter.MoveorderLineViewHolder pvHolder, int pvPositionInt) {

        if (localMoveorderLineObl == null || localMoveorderLineObl.size() == 0 ) {
            return;
        }

        final cMoveorderLine moveorderLine = localMoveorderLineObl.get(pvPositionInt);

        pvHolder.textViewArticle.setText(moveorderLine.getItemNoStr() + " " + moveorderLine.getVariantCodeStr());
        pvHolder.textViewDescription.setText(moveorderLine.getDescriptionStr());
        pvHolder.textViewDescription.setVisibility(View.VISIBLE);
        pvHolder.textViewCounted.setText(cText.pDoubleToStringStr(moveorderLine.getQuantityHandledDbl()));
    }

    @Override
    public int getItemCount () {
        if (localMoveorderLineObl != null)
            return localMoveorderLineObl.size();
        else return 0;
    }

    //End Region Default Methods

    //Region Public Methods
    public void pFillData(List<cMoveorderLine> pvDataObl) {
        cMoveorderLineAdapter.localMoveorderLineObl = pvDataObl;
        notifyDataSetChanged();
    }

    public void pSetFilter(String pvQueryTextStr) {
        this.localMoveorderLineObl = this.mGetFilteredListObl(pvQueryTextStr);
        notifyDataSetChanged();
    }

    public  void pRemoveItem(int pvPositionInt) {
        cMoveorderLineAdapter.localMoveorderLineObl.remove(cMoveorderLine.currentMoveOrderLine);
        notifyItemRemoved(pvPositionInt);
    }
    public  void pRestoreItem(int pvPositionInt) {
        cMoveorderLineAdapter.localMoveorderLineObl.add(pvPositionInt, cMoveorderLine.currentMoveOrderLine);

        notifyItemInserted(pvPositionInt);
    }


    //End Region Public Methods


    //Region Private Methods

    private List<cMoveorderLine> mGetFilteredListObl(String pvQueryTextStr) {

        pvQueryTextStr = pvQueryTextStr.toLowerCase();
        List<cMoveorderLine> resultObl = new ArrayList<>();

        if (this.localMoveorderLineObl == null || this.localMoveorderLineObl.size() == 0) {
            return resultObl;
        }

        for (cMoveorderLine moveorderline:localMoveorderLineObl)
        {
            if (moveorderline.getVariantCodeStr().toLowerCase().contains(pvQueryTextStr) ||
                    moveorderline.getDescriptionStr().toLowerCase().contains(pvQueryTextStr))
            {
                resultObl.add(moveorderline);
            }
        }
        return resultObl;
    }



    //End Region Private Methods
}
