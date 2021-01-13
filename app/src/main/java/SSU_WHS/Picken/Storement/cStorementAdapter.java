package SSU_WHS.Picken.Storement;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ICS.Utils.cText;
import ICS.cAppExtension;
import SSU_WHS.Picken.Pickorders.cPickorder;
import SSU_WHS.Picken.Shipment.cShipment;
import nl.icsvertex.scansuite.Activities.Ship.ShiporderLinesActivity;
import nl.icsvertex.scansuite.Activities.Store.StoreorderLinesActivity;
import nl.icsvertex.scansuite.R;

public class cStorementAdapter extends RecyclerView.Adapter<cStorementAdapter.StorementViewHolder> {

    //Region Public Properties

    public static class StorementViewHolder extends RecyclerView.ViewHolder{

        private TextView textViewDocument;
        private TextView textViewQuantity;
        private TextView textViewBinCode;
        public LinearLayout storementItemLinearLayout;

        public StorementViewHolder(View pvItemView) {
            super(pvItemView);

            this.storementItemLinearLayout = pvItemView.findViewById(R.id.storementItemLinearLayout);

            this.textViewDocument = pvItemView.findViewById(R.id.textViewDocument);
            this.textViewDocument.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            this.textViewDocument.setSingleLine(true);
            this.textViewDocument.setMarqueeRepeatLimit(5);
            this.textViewDocument.setSelected(true);

            this.textViewQuantity = pvItemView.findViewById(R.id.textViewQuantity);
            this.textViewQuantity.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            this.textViewQuantity.setSingleLine(true);
            this.textViewQuantity.setMarqueeRepeatLimit(5);
            this.textViewQuantity.setSelected(true);

            this.textViewBinCode = pvItemView.findViewById(R.id.textViewBinCode);
            this.textViewBinCode.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            this.textViewBinCode.setSingleLine(true);
            this.textViewBinCode.setMarqueeRepeatLimit(5);
            this.textViewBinCode.setSelected(true);


        }

    }
    //End Region Public Properties

    //Region Private Properties

    private List<LinearLayout> linearLayouts = new ArrayList<>();
    private RecyclerView thisRecyclerView;
    private final LayoutInflater LayoutInflaterObject;
    private List<cStorement> localStorementObl;

    //End Region private Properties

    //Region Constructor
    public cStorementAdapter() {
        this.LayoutInflaterObject = LayoutInflater.from(cAppExtension.context);
    }
    //End Region Constructor

    //Region Default Methods

    @NonNull
    @Override
    public cStorementAdapter.StorementViewHolder onCreateViewHolder(@NonNull ViewGroup pvViewGroup, int pvViewTypeInt) {
        View itemView = LayoutInflaterObject.inflate(R.layout.recycler_storement, pvViewGroup, false);
        return new StorementViewHolder(itemView);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView pvRecyclerView) {
        super.onAttachedToRecyclerView(pvRecyclerView);
        this.thisRecyclerView = pvRecyclerView;
    }

    @Override
    public void onBindViewHolder(cStorementAdapter.StorementViewHolder pvHolder, final int pvPositionInt) {

        this.linearLayouts.add(pvHolder.storementItemLinearLayout);

        if (this.localStorementObl == null || this.localStorementObl.size() == 0 ) {
            return;
        }

        final cStorement storement = this.localStorementObl.get(pvPositionInt);

        if (!storement.getProcessingSequenceStr().isEmpty()) {
            pvHolder.textViewDocument.setText(storement.getProcessingSequenceStr());
        }
        else {
            pvHolder.textViewDocument.setText(storement.getSourceNoStr());
        }

        pvHolder.textViewQuantity.setText(cText.pIntToStringStr(storement.getQuantityDbl().intValue()));

        if (storement.getBinCodeStr().isEmpty()) {
            pvHolder.textViewBinCode.setText("???");
        }
        else
        {
            pvHolder.textViewBinCode.setText(storement.getBinCodeStr());
        }

        if (pvPositionInt ==0) {
            if (cAppExtension.activity instanceof StoreorderLinesActivity) {
                StoreorderLinesActivity storeorderLinesActivity = (StoreorderLinesActivity)cAppExtension.activity;
                storeorderLinesActivity.pStorementSelected(storement);
            }
        }


        final int id = thisRecyclerView.getId();
        pvHolder.storementItemLinearLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //deselect all
                for (LinearLayout aLayout : linearLayouts) {
                    aLayout.setSelected(false);
                }
                //select current
                v.setSelected(true);
                //wich fragment?

                if (id == R.id.recyclerViewStoreorderLinesToStore) {
                    if (cAppExtension.activity instanceof StoreorderLinesActivity) {
                        cStorement.currentStorement = storement;
                        StoreorderLinesActivity storeorderLinesActivity = (StoreorderLinesActivity) cAppExtension.activity;
                        storeorderLinesActivity.pHandleScan(null,true);

                    }
                }

            }
        });

    }


    @Override
    public int getItemCount () {
        if (this.localStorementObl != null)
            return this.localStorementObl .size();
        else return 0;
    }

    //End Region Default Methods

    //Region Public Methods
    public void pFillData(List<cStorement> pvDataObl) {
        localStorementObl = pvDataObl;
        notifyDataSetChanged();
    }
    //End Region Public Methods
}
