package SSU_WHS.Picken.PickorderLinePackAndShip;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ICS.cAppExtension;
import SSU_WHS.Picken.Shipment.cShipment;
import nl.icsvertex.scansuite.Activities.QualityControl.QualityControlLinesActivity;
import nl.icsvertex.scansuite.Activities.Ship.ShiporderLinesActivity;
import nl.icsvertex.scansuite.R;

public class cPickorderLinePackAndShipAdapter extends RecyclerView.Adapter<cPickorderLinePackAndShipAdapter.PickorderLineViewHolder>  {

    //Region Public Properties
    public static class PickorderLineViewHolder extends RecyclerView.ViewHolder{


        private TextView textViewItemNoAndVariant;
        private TextView textViewDescription;
        private TextView textViewQuantity;
        private FrameLayout pickorderLinePackAndShipItemLinearLayout;
        public ConstraintLayout container;

        public PickorderLineViewHolder(View pvItemView) {
            super(pvItemView);
            this.textViewItemNoAndVariant = pvItemView.findViewById(R.id.textViewItemNoAndVariant);
            this.textViewItemNoAndVariant.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            this.textViewItemNoAndVariant.setSingleLine(true);
            this.textViewItemNoAndVariant.setMarqueeRepeatLimit(5);
            this.textViewItemNoAndVariant.setSelected(true);

            this.textViewDescription = pvItemView.findViewById(R.id.textViewDescription);
            this.textViewDescription.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            this.textViewDescription.setSingleLine(true);
            this.textViewDescription.setMarqueeRepeatLimit(5);
            this.textViewDescription.setSelected(true);

            this.textViewQuantity = pvItemView.findViewById(R.id.textViewQuantity);
            this.pickorderLinePackAndShipItemLinearLayout = pvItemView.findViewById(R.id.pickorderLinePackAndShipItemLinearLayout);
            this.container = pvItemView.findViewById(R.id.container);
        }
    }
    //End Region Public Properties

    //Region Private Properties
    private final LayoutInflater LayoutInflaterObject;
    private List<cPickorderLinePackAndShip> localPackAndShipLinesObl;
    //End Region Private Propertiess

    private List<FrameLayout> pickorderLineItemLinearLayouts = new ArrayList<>();
    private RecyclerView thisRecyclerView;

    //Region Constructor
    public cPickorderLinePackAndShipAdapter() {
        this.LayoutInflaterObject = LayoutInflater.from(cAppExtension.context);
    }
    // End Region Constructor

    //Region Default Methods

    @NonNull
    @Override
    public cPickorderLinePackAndShipAdapter.PickorderLineViewHolder onCreateViewHolder(@NonNull ViewGroup pvParent, int pvViewTypeInt) {
        View itemView = this.LayoutInflaterObject.inflate(R.layout.recycler_pickorderlinepackandship, pvParent, false);
        return new PickorderLineViewHolder(itemView);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView pvRecyclerView) {
        this.thisRecyclerView = pvRecyclerView;
        super.onAttachedToRecyclerView( this.thisRecyclerView);
    }

    @Override
    public void onBindViewHolder(final cPickorderLinePackAndShipAdapter.PickorderLineViewHolder pvHolder, final int pvPositionInt) {

        this.pickorderLineItemLinearLayouts.add(pvHolder.pickorderLinePackAndShipItemLinearLayout);

        if (this.localPackAndShipLinesObl == null || this.localPackAndShipLinesObl.size() == 0 ) {
            return;
        }

        final cPickorderLinePackAndShip pickorderLinePackAndShip = this.localPackAndShipLinesObl.get(pvPositionInt);

        String itemNoAndVariantCode = pickorderLinePackAndShip.getItemNoStr() + "~" + pickorderLinePackAndShip.getVariantCodeStr();
        String lineDescriptionStr =  pickorderLinePackAndShip.getDescriptionStr() + " " + pickorderLinePackAndShip.getDescription2Str();
        String quantityToShowStr = "";


        //Pick recyclers
        if (thisRecyclerView.getId() == R.id.recyclerViewQCLinesToCheck) {
           quantityToShowStr  = pickorderLinePackAndShip.getQuantityCheckedDbl().intValue() + "/" + pickorderLinePackAndShip.getQuantityDbl().intValue();
        }

        if (thisRecyclerView.getId() == R.id.recyclerViewQCLinesChecked) {
           quantityToShowStr  = pickorderLinePackAndShip.getQuantityCheckedDbl().intValue() + "/" + pickorderLinePackAndShip.getQuantityDbl().intValue();


        }
        pvHolder.textViewItemNoAndVariant.setText(itemNoAndVariantCode);
        pvHolder.textViewDescription.setText(lineDescriptionStr);
        pvHolder.textViewQuantity.setText(quantityToShowStr);


        final int id = thisRecyclerView.getId();
        //Start On Click Listener
        pvHolder.pickorderLinePackAndShipItemLinearLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View pvView) {

                //deselect all
                for (FrameLayout frameLayout : pickorderLineItemLinearLayouts) {
                    frameLayout.setSelected(false);
                }

                //select current
                pvView.setSelected(true);

                if (id == R.id.recyclerViewQCLinesToCheck) {
                    if (cAppExtension.activity instanceof QualityControlLinesActivity) {
                        cPickorderLinePackAndShip.currentPickorderLinePackAndShip = pickorderLinePackAndShip;
                        cPickorderLinePackAndShip.currentPickorderLinePackAndShip.pLineBusyRst();
                        QualityControlLinesActivity qualityControlLinesActivity = (QualityControlLinesActivity) cAppExtension.activity;
                        qualityControlLinesActivity.pStartQCActivity();

                    }
                }


            }
        });
        //End On Click Listener

    }

    @Override
    public int getItemCount () {
        if (this.localPackAndShipLinesObl != null)
            return this.localPackAndShipLinesObl.size();
        else return 0;
    }

    //End Region Default Methods

    //Region Public Methods
    public void pFillData(List<cPickorderLinePackAndShip> pvDataObl) {
        this.localPackAndShipLinesObl = pvDataObl;
    }


    //End Region Public Methods

    //Region Private Methods

    //End Region Private Methods






}
