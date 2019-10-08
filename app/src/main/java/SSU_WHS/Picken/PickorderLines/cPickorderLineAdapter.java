package SSU_WHS.Picken.PickorderLines;

import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ICS.Utils.cText;
import ICS.Utils.cUserInterface;
import SSU_WHS.Picken.Pickorders.cPickorder;
import nl.icsvertex.scansuite.activities.sort.SortorderLinesActivity;
import nl.icsvertex.scansuite.cAppExtension;
import nl.icsvertex.scansuite.activities.pick.PickorderLinesActivity;
import nl.icsvertex.scansuite.R;
import nl.icsvertex.scansuite.fragments.pick.PickorderLinesPickedFragment;

public class cPickorderLineAdapter extends RecyclerView.Adapter<cPickorderLineAdapter.PickorderLineViewHolder>  {

    //Region Public Properties
    public class PickorderLineViewHolder extends RecyclerView.ViewHolder{

        private TextView textViewPickorderLineLocation;
        private TextView textViewPickorderLine;
        private TextView textViewPickorderLineQuantity;
        private TextView textViewPickorderLineSourceNo;
        private LinearLayout pickorderLineItemLinearLayout;

        public PickorderLineViewHolder(View pvItemView) {
            super(pvItemView);
            this.textViewPickorderLineLocation = pvItemView.findViewById(R.id.textViewPickorderLineLocation);
            this.textViewPickorderLineLocation.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            this.textViewPickorderLineLocation.setSingleLine(true);
            this.textViewPickorderLineLocation.setMarqueeRepeatLimit(5);
            this.textViewPickorderLineLocation.setSelected(true);
            this.textViewPickorderLine = pvItemView.findViewById(R.id.textViewPickorderLine);
            this.textViewPickorderLine.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            this.textViewPickorderLine.setSingleLine(true);
            this.textViewPickorderLine.setMarqueeRepeatLimit(5);
            this.textViewPickorderLine.setSelected(true);
            this.textViewPickorderLineSourceNo = pvItemView.findViewById(R.id.textViewPickorderLineSourceNo);
            this.textViewPickorderLineSourceNo.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            this.textViewPickorderLineSourceNo.setSingleLine(true);
            this.textViewPickorderLineSourceNo.setMarqueeRepeatLimit(5);
            this.textViewPickorderLineSourceNo.setSelected(true);
            this.textViewPickorderLineQuantity = pvItemView.findViewById(R.id.textViewPickorderLineQuantity);
            this. pickorderLineItemLinearLayout = pvItemView.findViewById(R.id.pickorderLineItemLinearLayout);
        }
    }
    //End Region Public Properties

    //Region Private Properties
    private final LayoutInflater LayoutInflaterObject;
    private List<cPickorderLine> localPickorderLinesObl;
    //End Region Private Propertiess

    private List<LinearLayout> pickorderLineItemLinearLayouts = new ArrayList<>();
    private RecyclerView thisRecyclerView;

    //Region Constructor
    public cPickorderLineAdapter() {
        this.LayoutInflaterObject = LayoutInflater.from(cAppExtension.context);
    }
    // End Region Constructor

    //Region Default Methods

    @Override
    public cPickorderLineAdapter.PickorderLineViewHolder onCreateViewHolder(ViewGroup pvParent, int pvViewTypeInt) {
        View itemView = this.LayoutInflaterObject.inflate(R.layout.recycler_pickorderline, pvParent, false);
        return new cPickorderLineAdapter.PickorderLineViewHolder(itemView);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView pvRecyclerView) {
        this.thisRecyclerView = pvRecyclerView;
        super.onAttachedToRecyclerView( this.thisRecyclerView);
    }

    @Override
    public void onBindViewHolder(final cPickorderLineAdapter.PickorderLineViewHolder pvHolder, final int pvPositionInt) {

        this.pickorderLineItemLinearLayouts.add(pvHolder.pickorderLineItemLinearLayout);

        if (this.localPickorderLinesObl == null || this.localPickorderLinesObl.size() == 0 ) {
            return;
        }

        final cPickorderLine currentPickorderLine = this.localPickorderLinesObl.get(pvPositionInt);


        String lineDescriptionStr = currentPickorderLine.getItemNoStr() + "~" + currentPickorderLine.getVariantCodeStr() + ": " + currentPickorderLine.getDescriptionStr();
        String quantityToShowStr = "";

        //Pick recyclers
        if (thisRecyclerView.getId() == R.id.recyclerViewPickorderLinesTopick) {
           quantityToShowStr  = currentPickorderLine.getQuantityHandledDbl().intValue() + "/" + currentPickorderLine.getQuantityDbl().intValue();
            pvHolder.textViewPickorderLineLocation.setText(currentPickorderLine.getBinCodeStr());
            pvHolder.textViewPickorderLineLocation.setVisibility(View.VISIBLE);
        }

        if (thisRecyclerView.getId() == R.id.recyclerViewPickorderLinesPicked) {
           quantityToShowStr  = currentPickorderLine.getQuantityHandledDbl().intValue() + "/" + currentPickorderLine.getQuantityDbl().intValue();
            pvHolder.textViewPickorderLineLocation.setText(currentPickorderLine.getBinCodeStr());
            pvHolder.textViewPickorderLineLocation.setVisibility(View.VISIBLE);
        }

        if (thisRecyclerView.getId() == R.id.recyclerViewPickorderLinesTotal) {
           quantityToShowStr  = cText.intToString( currentPickorderLine.getQuantityDbl().intValue());
            pvHolder.textViewPickorderLineLocation.setText(currentPickorderLine.getBinCodeStr());
            pvHolder.textViewPickorderLineLocation.setVisibility(View.VISIBLE);
        }

        //Sort Recyclers
        if (thisRecyclerView.getId() == R.id.recyclerViewSortorderLinesTosort) {
            quantityToShowStr  = currentPickorderLine.getQuantityHandledDbl().intValue() + "/" + currentPickorderLine.getQuantityDbl().intValue();
            pvHolder.textViewPickorderLineLocation.setText("");
            pvHolder.textViewPickorderLineLocation.setVisibility(View.GONE);
        }

        if (thisRecyclerView.getId() == R.id.recyclerViewSortorderLinesSorted) {
            quantityToShowStr  = currentPickorderLine.getQuantityHandledDbl().intValue() + "/" + currentPickorderLine.getQuantityDbl().intValue();
            pvHolder.textViewPickorderLineLocation.setText(currentPickorderLine.getProcessingSequenceStr());
            pvHolder.textViewPickorderLineLocation.setVisibility(View.VISIBLE);
        }

        if (thisRecyclerView.getId() == R.id.recyclerViewSortorderLinesTotal) {
            quantityToShowStr  = cText.intToString( currentPickorderLine.getQuantityDbl().intValue());
            pvHolder.textViewPickorderLineLocation.setText("");
            pvHolder.textViewPickorderLineLocation.setVisibility(View.GONE);
        }

        pvHolder.textViewPickorderLine.setText(lineDescriptionStr);
        pvHolder.textViewPickorderLineQuantity.setText(quantityToShowStr);
        pvHolder.textViewPickorderLineSourceNo.setText(currentPickorderLine.getSourceNoStr());

        //Start On Click Listener
        pvHolder.pickorderLineItemLinearLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View pvView) {

                //deselect all
                for (LinearLayout linearLayout : pickorderLineItemLinearLayouts) {
                    linearLayout.setSelected(false);
                }

                //select current
                pvView.setSelected(true);

                //Kick off correct event at correct activity
                if (cAppExtension.context instanceof PickorderLinesActivity) {

                    if (thisRecyclerView.getId() == R.id.recyclerViewPickorderLinesTopick) {
                        PickorderLinesActivity.pPicklineSelected(currentPickorderLine);
                    }

                    if (thisRecyclerView.getId() == R.id.recyclerViewPickorderLinesPicked) {
                        PickorderLinesActivity.pPicklineToResetSelected(currentPickorderLine);
                    }
                }


                if (cAppExtension.context  instanceof SortorderLinesActivity) {

                    if (thisRecyclerView.getId() == R.id.recyclerViewSortorderLinesTosort) {
                        SortorderLinesActivity.pPicklineSelected(currentPickorderLine);
                    }
                    if (thisRecyclerView.getId() == R.id.recyclerViewSortorderLinesSorted) {
                        SortorderLinesActivity.pPicklineToResetSelected(currentPickorderLine);
                        return;
                    }

                    if (thisRecyclerView.getId() == R.id.recyclerViewSortorderLinesTotal) {
                        pvHolder.textViewPickorderLineLocation.setVisibility(View.GONE);
                    }

                }

            }
        });
        //End On Click Listener

        // If this is the first line, then click it
        if (pvPositionInt == 0 ) {
            pvHolder.pickorderLineItemLinearLayout.performClick();
        }
    }

    @Override
    public int getItemCount () {
        if (this.localPickorderLinesObl != null)
            return this.localPickorderLinesObl.size();
        else return 0;
    }

    //End Region Default Methods

    //Region Public Methods
    public void pFillData(List<cPickorderLine> pvDataObl) {
        this.localPickorderLinesObl = pvDataObl;
       }

    public void pSetFilter(String pvQueryTextStr) {
        this.localPickorderLinesObl = mGetFilteredListObl(pvQueryTextStr);
        notifyDataSetChanged();
    }

    public void pvShowDefects(Boolean pvShowBlm) {
        this.localPickorderLinesObl = mGetDefectsListObl(pvShowBlm);

        //if PickorderLinesPickedFragment is the current fragment
        if (PickorderLinesActivity.currentLineFragment instanceof PickorderLinesPickedFragment ) {

            //if there are no defects, then show no lines fragment
            if (this.localPickorderLinesObl.size() == 0 ) {
                PickorderLinesPickedFragment.pNoLinesAvailable(true);
            }
            // There are lines to show, so refresh the fragent then all lines are shown again
            else {
               PickorderLinesPickedFragment.pGetData();
            }

        }

        notifyDataSetChanged();
    }

    //End Region Public Methods

    //Region Private Methods

    private List<cPickorderLine> mGetFilteredListObl(String pvQueryTextStr) {
        pvQueryTextStr = pvQueryTextStr.toLowerCase();
        List<cPickorderLine> resultObl = new ArrayList<>();

        if (this.localPickorderLinesObl == null || this.localPickorderLinesObl.size() == 0) {
            return resultObl;
        }

        for (cPickorderLine pickorderLine :this.localPickorderLinesObl)
        {
            if (pickorderLine.getBinCodeStr().toLowerCase().contains(pvQueryTextStr) || pickorderLine.getItemNoStr().toLowerCase().contains(pvQueryTextStr) || pickorderLine.getDescriptionStr().toLowerCase().contains(pvQueryTextStr)|| pickorderLine.getSourceNoStr().contains(pvQueryTextStr))
            {
                resultObl.add(pickorderLine);
            }
        }
        return resultObl;
    }

    private List<cPickorderLine> mGetDefectsListObl(Boolean pvShowBln) {

        if (!pvShowBln) {
            this.localPickorderLinesObl = cPickorder.currentPickOrder.pGetLinesHandledFromDatabasObl();
            return  this.localPickorderLinesObl;
        }

        List<cPickorderLine> resultObl = new ArrayList<>();
        for (cPickorderLine pickorderLine :this.localPickorderLinesObl)
        {
            if (pickorderLine.getQuantityHandledDbl() < pickorderLine.quantityDbl)
            {
                resultObl.add(pickorderLine);
            }
        }
        return resultObl;
    }

    //End Region Private Methods






}
