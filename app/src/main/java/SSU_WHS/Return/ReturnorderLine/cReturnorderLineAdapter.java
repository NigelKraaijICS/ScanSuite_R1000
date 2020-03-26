package SSU_WHS.Return.ReturnorderLine;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ICS.Utils.cText;
import ICS.cAppExtension;
import SSU_WHS.Return.ReturnOrder.cReturnorder;
import nl.icsvertex.scansuite.Activities.Returns.ReturnorderDocumentActivity;
import nl.icsvertex.scansuite.R;

public class cReturnorderLineAdapter extends RecyclerView.Adapter<SSU_WHS.Return.ReturnorderLine.cReturnorderLineAdapter.ReturnorderLineViewHolder> {
    //Region Public Properties

    public static class ReturnorderLineViewHolder extends RecyclerView.ViewHolder{

        private TextView textViewArticle;
        private TextView textViewDescription;
        private TextView textViewCounted;
        private TextView textViewReason;

        public RelativeLayout viewBackground;
        public ConstraintLayout viewForeground;

        public ReturnorderLineViewHolder(View pvItemView) {
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
            this.textViewReason = pvItemView.findViewById(R.id.textViewReason);
            this.viewBackground = pvItemView.findViewById(R.id.view_background);
            this.viewForeground = pvItemView.findViewById(R.id.view_foreground);
        }
        //End Region Public Properties
    }

    //Region Private Properties
    private final LayoutInflater LayoutInflaterObject;
    private static List<cReturnorderLine> localReturnorderLineObl;
    private static RecyclerView thisRecyclerView;

    //End Region Private Properties

    //Region Constructor
    public cReturnorderLineAdapter() {
        this.LayoutInflaterObject = LayoutInflater.from(cAppExtension.context);
    }
    //End Region Constructor


    //Region Default Methods
    @NonNull
    @Override
    public SSU_WHS.Return.ReturnorderLine.cReturnorderLineAdapter.ReturnorderLineViewHolder onCreateViewHolder(@NonNull ViewGroup pvParent, int pbViewTypeInt) {
        View itemView = LayoutInflaterObject.inflate(R.layout.recycler_returnorderline, pvParent, false);
        return new ReturnorderLineViewHolder(itemView);
    }


    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView pvRecyclerView) {
        cReturnorderLineAdapter.thisRecyclerView = pvRecyclerView;
        super.onAttachedToRecyclerView( cReturnorderLineAdapter.thisRecyclerView);
    }

    @Override
    public void onBindViewHolder(@NonNull SSU_WHS.Return.ReturnorderLine.cReturnorderLineAdapter.ReturnorderLineViewHolder pvHolder, int pvPositionInt) {

        if (localReturnorderLineObl == null || localReturnorderLineObl.size() == 0 ) {
            return;
        }

        final cReturnorderLine returnorderLine = localReturnorderLineObl.get(pvPositionInt);

        pvHolder.textViewArticle.setText(returnorderLine.getItemNoAndVariantCodeStr());


        String  descriptionStr = returnorderLine.getDescriptionStr();

        if (returnorderLine.getItemNoStr().equalsIgnoreCase("UNKNOWN")) {
            descriptionStr = returnorderLine.getDescription2Str();
        }

        pvHolder.textViewDescription.setText(descriptionStr);
        pvHolder.textViewDescription.setVisibility(View.VISIBLE);
        if (cReturnorder.currentReturnOrder.isGeneratedBln()){
            pvHolder.textViewCounted.setText(cText.pDoubleToStringStr(returnorderLine.getQuantityHandledTakeDbl()));
        }
        else {
        pvHolder.textViewCounted.setText(returnorderLine.getQuantityToShowInAdapterStr());
        }
        pvHolder.textViewReason.setText(returnorderLine.getReturnReasonDescriptoinStr());

        pvHolder.viewForeground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cReturnorderLine.currentReturnOrderLine = returnorderLine;

                if (cAppExtension.activity instanceof  ReturnorderDocumentActivity) {
                    ReturnorderDocumentActivity returnorderDocumentActivity = (ReturnorderDocumentActivity)cAppExtension.activity;
                    returnorderDocumentActivity.pHandleSelectedLine();
                }

            }
        });
    }

    @Override
    public int getItemCount () {
        if (localReturnorderLineObl != null)
            return localReturnorderLineObl.size();
        else return 0;
    }

    //End Region Default Methods

    //Region Public Methods
    public void pFillData(List<cReturnorderLine> pvDataObl) {
        cReturnorderLineAdapter.localReturnorderLineObl = pvDataObl;
        notifyDataSetChanged();
    }

    public void pSetFilter(String pvQueryTextStr) {
        cReturnorderLineAdapter.localReturnorderLineObl = this.mGetFilteredListObl(pvQueryTextStr);
        notifyDataSetChanged();
    }


    //End Region Public Methods


    //Region Private Methods

    private List<cReturnorderLine> mGetFilteredListObl(String pvQueryTextStr) {

        pvQueryTextStr = pvQueryTextStr.toLowerCase();
        List<cReturnorderLine> resultObl = new ArrayList<>();

        if (cReturnorderLineAdapter.localReturnorderLineObl == null || cReturnorderLineAdapter.localReturnorderLineObl.size() == 0) {
            return resultObl;
        }

        for (cReturnorderLine returnorderLine: localReturnorderLineObl)
        {
            if (returnorderLine.getVariantCodeStr().toLowerCase().contains(pvQueryTextStr) ||
                returnorderLine.getDescriptionStr().toLowerCase().contains(pvQueryTextStr))
            {
                resultObl.add(returnorderLine);
            }
        }
        return resultObl;
    }



    //End Region Private Methods
}
