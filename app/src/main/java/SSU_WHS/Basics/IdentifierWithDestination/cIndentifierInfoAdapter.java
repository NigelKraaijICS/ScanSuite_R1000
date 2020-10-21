package SSU_WHS.Basics.IdentifierWithDestination;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ICS.cAppExtension;
import nl.icsvertex.scansuite.R;

public class cIndentifierInfoAdapter extends RecyclerView.Adapter<cIndentifierInfoAdapter.IndentifierInfoLineViewHolder>  {

    //Region Public Properties
    public static class IndentifierInfoLineViewHolder extends RecyclerView.ViewHolder{

        private TextView textViewInfoKey;
        private TextView textViewInfoValue;
        private LinearLayout identifierInfoItemLinearLayout;

        public RelativeLayout viewBackground;
        public ConstraintLayout viewForeground;


        public IndentifierInfoLineViewHolder(View pvItemView) {
            super(pvItemView);

            this.identifierInfoItemLinearLayout = pvItemView.findViewById(R.id.identifierInfoItemLinearLayout);

            this.textViewInfoKey = pvItemView.findViewById(R.id.textViewInfoKey);
            this.textViewInfoKey.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            this.textViewInfoKey.setSingleLine(true);
            this.textViewInfoKey.setMarqueeRepeatLimit(5);
            this.textViewInfoKey.setSelected(true);

            this.textViewInfoValue = pvItemView.findViewById(R.id.textViewInfoValue);
            this.textViewInfoValue.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            this.textViewInfoValue.setSingleLine(true);
            this.textViewInfoValue.setMarqueeRepeatLimit(5);
            this.textViewInfoValue.setSelected(true);

        }
    }
    //End Region Public Properties

    //Region Private Properties
    private final LayoutInflater LayoutInflaterObject;
    //End Region Private Propertiess

    private List<LinearLayout> IndentifierInfoLineViewHolder = new ArrayList<>();
    private RecyclerView RecyclerView;

    //Region Constructor
    public cIndentifierInfoAdapter() {
        this.LayoutInflaterObject = LayoutInflater.from(cAppExtension.context);
    }
    // End Region Constructor

    //Region Default Methods

    @NonNull
    @Override
    public cIndentifierInfoAdapter.IndentifierInfoLineViewHolder onCreateViewHolder(@NonNull ViewGroup pvParent, int pvViewTypeInt) {
        View itemView = this.LayoutInflaterObject.inflate(R.layout.recycler_identifierinfo, pvParent, false);
        return new IndentifierInfoLineViewHolder(itemView);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView pvRecyclerView) {
        this.RecyclerView = pvRecyclerView;
        super.onAttachedToRecyclerView( this.RecyclerView);
    }

    @Override
    public void onBindViewHolder(final cIndentifierInfoAdapter.IndentifierInfoLineViewHolder pvHolder, final int pvPositionInt) {

        this.IndentifierInfoLineViewHolder.add(pvHolder.identifierInfoItemLinearLayout);

        if (cIdentifierWithDestination.currentIdentifier == null || cIdentifierWithDestination.currentIdentifier.infoObl.size() == 0 ) {
            return;
        }

        final cIdentifierInfo identifierInfo = cIdentifierWithDestination.currentIdentifier.infoObl.get(pvPositionInt);
        //Set description and quantity
        pvHolder.textViewInfoKey.setText(identifierInfo.getInfoKeyStr());
        pvHolder.textViewInfoValue.setText(identifierInfo.getInfoValueStr());


    }

    @Override
    public int getItemCount () {
        if (cIdentifierWithDestination.currentIdentifier != null && cIdentifierWithDestination.currentIdentifier.infoObl != null)
            return cIdentifierWithDestination.currentIdentifier.infoObl.size();
        else return 0;
    }

    //End Region Default Methods

    //Region Public Methods
    public void pFillData( ) {
        notifyDataSetChanged();
    }

    //End Region Public Methods

    //Region Private Methods

    //End Region Private Methods

}
