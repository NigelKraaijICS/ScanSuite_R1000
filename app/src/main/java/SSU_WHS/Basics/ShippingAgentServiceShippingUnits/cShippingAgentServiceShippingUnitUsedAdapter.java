package SSU_WHS.Basics.ShippingAgentServiceShippingUnits;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ICS.Utils.cText;
import ICS.cAppExtension;
import nl.icsvertex.scansuite.R;

public class cShippingAgentServiceShippingUnitUsedAdapter extends RecyclerView.Adapter<cShippingAgentServiceShippingUnitUsedAdapter.ShippingAgentServiceShippingUnitUsedViewHolder> {

    //Region Public Properties

    //End Region Public Properties

    static class ShippingAgentServiceShippingUnitUsedViewHolder extends RecyclerView.ViewHolder{

        private TextView textViewDescription;
        private TextView textViewShippingUnit;
        private TextView textViewQuantityUsed;
        private ImageView usedUnitsImage;

        ShippingAgentServiceShippingUnitUsedViewHolder(View pvView) {
            super(pvView);
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

    private LayoutInflater layoutInflater;
    private List<cShippingAgentServiceShippingUnit> localShippingUnits;


    //End Region Private Properties

    //Region Constructor

    public cShippingAgentServiceShippingUnitUsedAdapter() {
        this.layoutInflater = LayoutInflater.from(cAppExtension.context);
    }

    // End Region Constructor

    // Region Default Methods

    @NonNull
    @Override
    public cShippingAgentServiceShippingUnitUsedAdapter.ShippingAgentServiceShippingUnitUsedViewHolder onCreateViewHolder(@NonNull ViewGroup pvViewgroup, int pvViewTypeInt) {
        View itemView = layoutInflater.inflate(R.layout.recycler_shippingunit_used, pvViewgroup, false);
        return new ShippingAgentServiceShippingUnitUsedViewHolder(itemView);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView pvRecyclerView) {
        super.onAttachedToRecyclerView(pvRecyclerView);
    }

    @Override
    public void onBindViewHolder(@NonNull cShippingAgentServiceShippingUnitUsedAdapter.ShippingAgentServiceShippingUnitUsedViewHolder pvHolder, final int pvPositionInt) {

        if (this.localShippingUnits == null || this.localShippingUnits.size() == 0) {
            return;
        }

       cShippingAgentServiceShippingUnit shippingAgentServiceShippingUnit = this.localShippingUnits.get(pvPositionInt);

       pvHolder.textViewDescription.setText(shippingAgentServiceShippingUnit.getDescriptionStr());
       pvHolder.textViewShippingUnit.setText(shippingAgentServiceShippingUnit.getShippingUnitStr());
       pvHolder.textViewQuantityUsed.setText(cText.pIntToStringStr(shippingAgentServiceShippingUnit.getShippingUnitQuantityUsedInt()));

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
