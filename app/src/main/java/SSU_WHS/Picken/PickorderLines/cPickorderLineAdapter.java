package SSU_WHS.Picken.PickorderLines;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ICS.ICSControls.cICSMarqueeView;
import ICS.Utils.cText;
import ICS.cAppExtension;
import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.Picken.Pickorders.cPickorder;
import nl.icsvertex.scansuite.Activities.Pick.PickorderLinesActivity;
import nl.icsvertex.scansuite.Activities.Pick.PickorderLinesGeneratedActivity;
import nl.icsvertex.scansuite.Activities.Sort.SortorderLinesActivity;
import nl.icsvertex.scansuite.Fragments.Pick.PickorderLinesPickedFragment;
import nl.icsvertex.scansuite.Fragments.Pick.PickorderLinesToPickFragment;
import nl.icsvertex.scansuite.Fragments.Sort.SortorderLinesToSortFragment;
import nl.icsvertex.scansuite.R;

import static ICS.ICSControls.cICSMarqueeView.pSetMarqueeview;

public class cPickorderLineAdapter extends RecyclerView.Adapter<cPickorderLineAdapter.PickorderLineViewHolder>  {

    //Region Public Properties
    public static class PickorderLineViewHolder extends RecyclerView.ViewHolder{

        private final TextView textViewBIN;
        private final TextView textViewDescription;
        private final TextView textViewQuantity;
        private final TextView textViewSourceNo;
        private final TextView textViewDestination;
        private final FrameLayout pickorderLineItemFrameLayout;
        private final ImageView imageSendStatus;

        public RelativeLayout viewBackground;
        public ConstraintLayout viewForeground;

        public PickorderLineViewHolder(View pvItemView) {
            super(pvItemView);
            this.textViewBIN = pvItemView.findViewById(R.id.textViewBIN);
            pSetMarqueeview(textViewBIN);

            this.textViewDescription = pvItemView.findViewById(R.id.textViewDescription);
            pSetMarqueeview(textViewDescription);

            this.textViewSourceNo = pvItemView.findViewById(R.id.textViewSourceNo);
            pSetMarqueeview(textViewSourceNo);

            this.textViewDestination = pvItemView.findViewById(R.id.textViewDestination);
            pSetMarqueeview(textViewDestination);

            this.textViewQuantity = pvItemView.findViewById(R.id.textViewQuantity);
            this.pickorderLineItemFrameLayout = pvItemView.findViewById(R.id.pickorderLineItemLinearLayout);
            this.viewBackground = pvItemView.findViewById(R.id.view_background);
            this.viewForeground = pvItemView.findViewById(R.id.view_foreground);
            this.imageSendStatus = pvItemView.findViewById(R.id.imageSendStatus);
        }
    }
    //End Region Public Properties

    //Region Private Properties
    private final LayoutInflater LayoutInflaterObject;
    private List<cPickorderLine> localPickorderLinesObl;
    //End Region Private Propertiess

    private final List<FrameLayout> pickorderLineItemLinearLayouts = new ArrayList<>();
    private RecyclerView thisRecyclerView;

    //Region Constructor
    public cPickorderLineAdapter() {
        this.LayoutInflaterObject = LayoutInflater.from(cAppExtension.context);
    }
    // End Region Constructor

    //Region Default Methods

    @NonNull
    @Override
    public cPickorderLineAdapter.PickorderLineViewHolder onCreateViewHolder(@NonNull ViewGroup pvParent, int pvViewTypeInt) {
        View itemView = this.LayoutInflaterObject.inflate(R.layout.recycler_pickorderline, pvParent, false);
        return new PickorderLineViewHolder(itemView);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView pvRecyclerView) {
        this.thisRecyclerView = pvRecyclerView;
        super.onAttachedToRecyclerView( this.thisRecyclerView);

        if (this.localPickorderLinesObl == null) {
            return;
        }

        if (cPickorder.currentPickOrder.getLastSelectedIndexInt() > this.localPickorderLinesObl.size() -1 ) {
            cPickorder.currentPickOrder.lastSelectedIndexInt = this.localPickorderLinesObl.size() -1 ;
        }

        //Select the first one, or selected index
            if (PickorderLinesActivity.currentLineFragment instanceof  PickorderLinesToPickFragment) {
                PickorderLinesToPickFragment pickorderLinesToPickFragment = (PickorderLinesToPickFragment)PickorderLinesActivity.currentLineFragment;
                pickorderLinesToPickFragment.pSetSelectedIndexInt();
            }

    }

    @Override
    public void onBindViewHolder(final cPickorderLineAdapter.PickorderLineViewHolder pvHolder, final int pvPositionInt) {

        this.pickorderLineItemLinearLayouts.add(pvHolder.pickorderLineItemFrameLayout);

        if (this.localPickorderLinesObl == null || this.localPickorderLinesObl.size() == 0 ) {
            return;
        }

        final cPickorderLine currentPickorderLine = this.localPickorderLinesObl.get(pvPositionInt);

        String lineDescriptionStr = currentPickorderLine.getItemNoStr() + "~" + currentPickorderLine.getVariantCodeStr() + ": " + currentPickorderLine.getDescriptionStr();
        String quantityToShowStr = "";

        if (!cPickorder.currentPickOrder.isTransferBln()) {
            pvHolder.textViewDestination.setVisibility(View.GONE);
        }
        else {
            pvHolder.textViewDestination.setVisibility(View.VISIBLE);
            pvHolder.textViewDestination.setText(currentPickorderLine.getDestinationAndDescriptionStr());
        }


        //Pick recyclers
        if (thisRecyclerView.getId() == R.id.recyclerViewPickorderLinesTopick) {
            quantityToShowStr  = cText.pIntToStringStr(currentPickorderLine.getQuantityDbl().intValue());
            pvHolder.textViewBIN.setText(currentPickorderLine.getBinCodeStr());
            pvHolder.textViewBIN.setVisibility(View.VISIBLE);
            pvHolder.imageSendStatus.setVisibility(View.GONE);
        }

        if (thisRecyclerView.getId() == R.id.recyclerViewPickorderLinesPicked) {
            quantityToShowStr  = currentPickorderLine.getQuantityHandledDbl().intValue() + "/" + currentPickorderLine.getQuantityDbl().intValue();
            pvHolder.textViewBIN.setText(currentPickorderLine.getBinCodeStr());
            pvHolder.textViewBIN.setVisibility(View.VISIBLE);

            if (currentPickorderLine.getLocalStatusInt() == cWarehouseorder.PicklineLocalStatusEnu.LOCALSTATUS_DONE_NOTSENT || (currentPickorderLine.getLocalStatusInt() == cWarehouseorder.PicklineLocalStatusEnu.LOCALSTATUS_DONE_ERROR_SENDING)) {
                pvHolder.imageSendStatus.setVisibility(View.VISIBLE);
                pvHolder.imageSendStatus.setImageResource(R.drawable.ic_check_black_24dp);
            }

            if (currentPickorderLine.getLocalStatusInt() == cWarehouseorder.PicklineLocalStatusEnu.LOCALSTATUS_DONE_SENT) {
                pvHolder.imageSendStatus.setVisibility(View.VISIBLE);
                pvHolder.imageSendStatus.setImageResource(R.drawable.ic_doublecheck_black_24dp);
            }

        }

        if (thisRecyclerView.getId() == R.id.recyclerViewPickorderLinesTotal) {
            quantityToShowStr  = cText.pIntToStringStr( currentPickorderLine.getQuantityDbl().intValue());
            pvHolder.textViewBIN.setText(currentPickorderLine.getBinCodeStr());
            pvHolder.textViewBIN.setVisibility(View.VISIBLE);
            pvHolder.imageSendStatus.setVisibility(View.GONE);

            if (currentPickorderLine.getLocalStatusInt() == cWarehouseorder.PicklineLocalStatusEnu.LOCALSTATUS_DONE_NOTSENT || (currentPickorderLine.getLocalStatusInt() == cWarehouseorder.PicklineLocalStatusEnu.LOCALSTATUS_DONE_ERROR_SENDING)) {
                pvHolder.imageSendStatus.setVisibility(View.VISIBLE);
                pvHolder.imageSendStatus.setImageResource(R.drawable.ic_check_black_24dp);

            }

            if (currentPickorderLine.getLocalStatusInt() == cWarehouseorder.PicklineLocalStatusEnu.LOCALSTATUS_DONE_SENT) {
                pvHolder.imageSendStatus.setVisibility(View.VISIBLE);
                pvHolder.imageSendStatus.setImageResource(R.drawable.ic_doublecheck_black_24dp);
            }

        }

        //Pick Generated Recycler
        if (thisRecyclerView.getId() == R.id.recyclerViewPickorderLinesGenerated) {
            quantityToShowStr  = cText.pDoubleToStringStr(currentPickorderLine.getQuantityHandledDbl());
            pvHolder.textViewBIN.setText(currentPickorderLine.getBinCodeStr());
            pvHolder.textViewBIN.setVisibility(View.VISIBLE);
            pvHolder.imageSendStatus.setVisibility(View.GONE);
            pvHolder.textViewSourceNo.setVisibility(View.GONE);
        }

        //Sort Recyclers
        if (thisRecyclerView.getId() == R.id.recyclerViewSortorderLinesTosort) {
            quantityToShowStr  = currentPickorderLine.getQuantityHandledDbl().intValue() + "/" + currentPickorderLine.getQuantityDbl().intValue();
            pvHolder.textViewBIN.setText(currentPickorderLine.getProcessingSequenceStr());
            pvHolder.textViewBIN.setVisibility(View.VISIBLE);
            pvHolder.imageSendStatus.setVisibility(View.INVISIBLE);
        }

        if (thisRecyclerView.getId() == R.id.recyclerViewSortorderLinesSorted) {
            quantityToShowStr  = currentPickorderLine.getQuantityHandledDbl().intValue() + "/" + currentPickorderLine.getQuantityDbl().intValue();
            pvHolder.textViewBIN.setText(currentPickorderLine.getProcessingSequenceStr());
            pvHolder.textViewBIN.setVisibility(View.VISIBLE);
            pvHolder.imageSendStatus.setVisibility(View.INVISIBLE);
        }

        if (thisRecyclerView.getId() == R.id.recyclerViewSortorderLinesTotal) {
            quantityToShowStr  = cText.pIntToStringStr( currentPickorderLine.getQuantityDbl().intValue());
            pvHolder.textViewBIN.setText("");
            pvHolder.textViewBIN.setVisibility(View.GONE);
            pvHolder.imageSendStatus.setVisibility(View.INVISIBLE);
        }

        if (currentPickorderLine.getBinCodeStr().isEmpty()) {
            pvHolder.textViewBIN.setVisibility(View.GONE);
        }

        pvHolder.textViewDescription.setText(lineDescriptionStr);
        pvHolder.textViewQuantity.setText(quantityToShowStr);
        pvHolder.textViewSourceNo.setText(currentPickorderLine.getSourceNoStr());

        //Start On Click Listener
        pvHolder.pickorderLineItemFrameLayout.setOnClickListener(pvView -> {

            //deselect all
            for (FrameLayout frameLayout : pickorderLineItemLinearLayouts) {
                frameLayout.setSelected(false);
            }

            //select current
            pvView.setSelected(true);

            //Kick off correct event at correct activity
            if (cAppExtension.context instanceof PickorderLinesActivity) {

                PickorderLinesActivity pickorderLinesActivity = (PickorderLinesActivity)cAppExtension.activity;

                if (thisRecyclerView.getId() == R.id.recyclerViewPickorderLinesTopick) {
                    pickorderLinesActivity.pPicklineSelected(currentPickorderLine);
                }

                if (thisRecyclerView.getId() == R.id.recyclerViewPickorderLinesPicked) {
                    pickorderLinesActivity.pPicklineToResetSelected(currentPickorderLine);
                }
            }

            if (cAppExtension.context instanceof PickorderLinesGeneratedActivity) {

                PickorderLinesGeneratedActivity pickorderLinesGeneratedActivity = (PickorderLinesGeneratedActivity)cAppExtension.activity;

                pickorderLinesGeneratedActivity.pPicklineToResetSelected(currentPickorderLine);

            }


            if (cAppExtension.context  instanceof SortorderLinesActivity) {

                SortorderLinesActivity sortorderLinesActivity = (SortorderLinesActivity)cAppExtension.activity;

                if (thisRecyclerView.getId() == R.id.recyclerViewSortorderLinesTosort) {
                    sortorderLinesActivity.pPicklineSelected(currentPickorderLine);

                    if (SortorderLinesActivity.currentLineFragment instanceof SortorderLinesToSortFragment) {
                        SortorderLinesToSortFragment sortorderLinesToSortFragment = (SortorderLinesToSortFragment) SortorderLinesActivity.currentLineFragment;
                        sortorderLinesToSortFragment.pShowHideDetailButton();
                    }

                }
                if (thisRecyclerView.getId() == R.id.recyclerViewSortorderLinesSorted) {
                    sortorderLinesActivity.pPicklineToResetSelected(currentPickorderLine);
                    return;
                }

                if (thisRecyclerView.getId() == R.id.recyclerViewSortorderLinesTotal) {
                    pvHolder.textViewBIN.setVisibility(View.GONE);
                }

            }

        });
        //End On Click Listener

        //Select the first one, or selected index
        if (pvPositionInt == cPickorder.currentPickOrder.getLastSelectedIndexInt() && cAppExtension.activity instanceof  PickorderLinesActivity &&  thisRecyclerView.getId() == R.id.recyclerViewPickorderLinesTopick) {
            pvHolder.pickorderLineItemFrameLayout.performClick();
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

    public void pShowDefects(Boolean pvShowBln) {
        this.localPickorderLinesObl = mGetDefectsListObl(pvShowBln);

        //if PickorderLinesPickedFragment is the current fragment
        if (PickorderLinesActivity.currentLineFragment instanceof PickorderLinesPickedFragment ) {

            PickorderLinesPickedFragment pickorderLinesPickedFragment = (PickorderLinesPickedFragment)PickorderLinesActivity.currentLineFragment;

            //if there are no defects, then show no linesInt fragment
            if (this.localPickorderLinesObl.size() == 0 ) {
                pickorderLinesPickedFragment.pNoLinesAvailable(true);
            }
            // There are linesInt to show, so refresh the fragent then all linesInt are shown again
            else {
                pickorderLinesPickedFragment.pGetData( this.localPickorderLinesObl );
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
            this.localPickorderLinesObl = cPickorder.currentPickOrder.pGetLinesHandledFromDatabaseObl();
            return  this.localPickorderLinesObl;
        }

        List<cPickorderLine> resultObl = new ArrayList<>();

        if (this.localPickorderLinesObl != null) {
            for (cPickorderLine pickorderLine :this.localPickorderLinesObl)
            {
                if (pickorderLine.getQuantityHandledDbl() < pickorderLine.getQuantityDbl())
                {
                    resultObl.add(pickorderLine);
                }
            }
        }


        return resultObl;
    }

    //End Region Private Methods






}
