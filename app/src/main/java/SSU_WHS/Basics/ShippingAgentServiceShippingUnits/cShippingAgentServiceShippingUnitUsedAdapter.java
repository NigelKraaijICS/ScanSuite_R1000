package SSU_WHS.Basics.ShippingAgentServiceShippingUnits;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ICS.cAppExtension;
import nl.icsvertex.scansuite.R;

public class cShippingAgentServiceShippingUnitUsedAdapter extends RecyclerView.Adapter<cShippingAgentServiceShippingUnitUsedAdapter.ShippingAgentServiceShippingUnitUsedViewHolder> {

    //Region Public Properties

    //End Region Public Properties

    class ShippingAgentServiceShippingUnitUsedViewHolder extends RecyclerView.ViewHolder{

        private LinearLayout shippingUnitUsedItemLinearLayout;
        private TextView textViewDescription;
        private TextView textViewShippingUnit;
        private TextView textViewQuantityUsed;
        private ImageView usedUnitsImage;

        ShippingAgentServiceShippingUnitUsedViewHolder(View pvView) {
            super(pvView);
            this.shippingUnitUsedItemLinearLayout = pvView.findViewById(R.id.shippingUnitUsedItemLinearLayout);
            this.usedUnitsImage = pvView.findViewById(R.id.usedUnitsImage);
            this.textViewDescription = pvView.findViewById(R.id.textViewDescription);
            this.textViewDescription.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            this.textViewDescription.setSingleLine(true);
            this.textViewDescription.setMarqueeRepeatLimit(5);
            this.textViewDescription.setSelected(true);
            this.textViewShippingUnit = pvView.findViewById(R.id.textViewShippingUnit);
            this.textViewShippingUnit.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            this.textViewShippingUnit.setSingleLine(true);
            this.textViewShippingUnit.setMarqueeRepeatLimit(5);
            this.textViewShippingUnit.setSelected(true);
            this.textViewQuantityUsed = pvView.findViewById(R.id.textViewQuantityUsed);
        }
    }

    //Region Private Properties

    private List<LinearLayout> shippingUnitItemLinearLayouts = new ArrayList<>();
    private RecyclerView thisRecyclerView;
    private LayoutInflater layoutInflater;
    private List<cShippingAgentServiceShippingUnit> localShippingUnits;


    //End Region Private Properties

    //Region Constructor

    public cShippingAgentServiceShippingUnitUsedAdapter() {
        this.layoutInflater = LayoutInflater.from(cAppExtension.context);
    }

    // End Region Constructor

    // Region Default Methods

    @Override
    public cShippingAgentServiceShippingUnitUsedAdapter.ShippingAgentServiceShippingUnitUsedViewHolder onCreateViewHolder(ViewGroup pvViewgroup, int pvViewTypeInt) {
        View itemView = layoutInflater.inflate(R.layout.recycler_shippingunit_used, pvViewgroup, false);
        return new cShippingAgentServiceShippingUnitUsedAdapter.ShippingAgentServiceShippingUnitUsedViewHolder(itemView);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView pvRecyclerView) {
        super.onAttachedToRecyclerView(pvRecyclerView);
        this.thisRecyclerView = pvRecyclerView;
    }

    @Override
    public void onBindViewHolder(cShippingAgentServiceShippingUnitUsedAdapter.ShippingAgentServiceShippingUnitUsedViewHolder pvHolder, final int pvPositionInt) {

        this.shippingUnitItemLinearLayouts.add(pvHolder.shippingUnitUsedItemLinearLayout);

        if (this.localShippingUnits == null || this.localShippingUnits.size() == 0) {
            return;
        }

       cShippingAgentServiceShippingUnit shippingAgentServiceShippingUnit = this.localShippingUnits.get(pvPositionInt);

       pvHolder.textViewDescription.setText(shippingAgentServiceShippingUnit.getDescriptionStr());
       pvHolder.textViewShippingUnit.setText(shippingAgentServiceShippingUnit.getShippingUnitStr());
       pvHolder.textViewQuantityUsed.setText(Integer.toString(shippingAgentServiceShippingUnit.getShippingUnitQuantityUsedInt()));

        if (shippingAgentServiceShippingUnit.getShippingUnitStr().equalsIgnoreCase(cShippingAgentServiceShippingUnit.SHIPPINGUNIT_BOX)) {
            pvHolder.usedUnitsImage.setImageResource(R.drawable.ic_box);
        }
        if (shippingAgentServiceShippingUnit.getShippingUnitStr().contains(cShippingAgentServiceShippingUnit.SHIPPINGUNIT_BOX.toLowerCase())) {
            pvHolder.usedUnitsImage.setImageResource(R.drawable.ic_box);
        }
        if (shippingAgentServiceShippingUnit.getShippingUnitStr().equalsIgnoreCase(cShippingAgentServiceShippingUnit.SHIPPINGUNIT_PALLET)) {
            pvHolder.usedUnitsImage.setImageResource(R.drawable.ic_pallet);
        }
        if (shippingAgentServiceShippingUnit.getShippingUnitStr().equalsIgnoreCase(cShippingAgentServiceShippingUnit.SHIPPINGUNIT_CONTAINER)) {
            pvHolder.usedUnitsImage.setImageResource(R.drawable.ic_container);
        }
        if (shippingAgentServiceShippingUnit.getShippingUnitStr().equalsIgnoreCase(cShippingAgentServiceShippingUnit.SHIPPINGUNIT_LETTERBOX)) {
            pvHolder.usedUnitsImage.setImageResource(R.drawable.ic_letterbox);
        }
        if (shippingAgentServiceShippingUnit.getShippingUnitStr().equalsIgnoreCase(cShippingAgentServiceShippingUnit.SHIPPINGUNIT_HANGING)) {
            pvHolder.usedUnitsImage.setImageResource(R.drawable.ic_hanging);
        }
    }


    @Override
    public int getItemCount () {
        if (this.localShippingUnits != null)
            return this.localShippingUnits.size();
        else return 0;
    }

    //End Region Default Methods

    //Region Public Methods

    public void pFillData(List<cShippingAgentServiceShippingUnit> pvDataObl) {
        this.localShippingUnits = pvDataObl;
    }

    //End Region Public Methods

}
