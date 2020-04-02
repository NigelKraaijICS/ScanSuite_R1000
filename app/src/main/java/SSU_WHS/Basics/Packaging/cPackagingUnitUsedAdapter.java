package SSU_WHS.Basics.Packaging;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ICS.Utils.cText;
import ICS.cAppExtension;
import nl.icsvertex.scansuite.R;

public class cPackagingUnitUsedAdapter extends RecyclerView.Adapter<cPackagingUnitUsedAdapter.PackagingUnitUsedViewHolder> {

    //Region Public Properties

    //End Region Public Properties

    static class PackagingUnitUsedViewHolder extends RecyclerView.ViewHolder{

        private TextView textViewDescription;
        private TextView textViewPackagingUnit;
        private TextView textViewQuantityUsed;

        PackagingUnitUsedViewHolder(View pvView) {
            super(pvView);
            this.textViewDescription = pvView.findViewById(R.id.textViewDescription);
            this.textViewDescription.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            this.textViewDescription.setSingleLine(true);
            this.textViewDescription.setMarqueeRepeatLimit(5);
            this.textViewDescription.setSelected(true);
            this.textViewPackagingUnit = pvView.findViewById(R.id.textViewPackagingUnit);
            this.textViewPackagingUnit.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            this.textViewPackagingUnit.setSingleLine(true);
            this.textViewPackagingUnit.setMarqueeRepeatLimit(5);
            this.textViewPackagingUnit.setSelected(true);
            this.textViewQuantityUsed = pvView.findViewById(R.id.textViewQuantityUsed);
        }
    }

    //Region Private Properties

    private LayoutInflater layoutInflater;
    private List<cPackaging> localPackagingUnits;


    //End Region Private Properties

    //Region Constructor

    public cPackagingUnitUsedAdapter() {
        this.layoutInflater = LayoutInflater.from(cAppExtension.context);
    }

    // End Region Constructor

    // Region Default Methods

    @NonNull
    @Override
    public cPackagingUnitUsedAdapter.PackagingUnitUsedViewHolder onCreateViewHolder(@NonNull ViewGroup pvViewgroup, int pvViewTypeInt) {
        View itemView = layoutInflater.inflate(R.layout.recycler_packagingunit_used, pvViewgroup, false);
        return new PackagingUnitUsedViewHolder(itemView);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView pvRecyclerView) {
        super.onAttachedToRecyclerView(pvRecyclerView);
    }

    @Override
    public void onBindViewHolder(@NonNull cPackagingUnitUsedAdapter.PackagingUnitUsedViewHolder pvHolder, final int pvPositionInt) {

        if (this.localPackagingUnits == null || this.localPackagingUnits.size() == 0) {
            return;
        }

       cPackaging packaging = this.localPackagingUnits.get(pvPositionInt);

       pvHolder.textViewDescription.setText(packaging.getDescriptionStr());
       pvHolder.textViewPackagingUnit.setText(packaging.getCodeStr());
       pvHolder.textViewQuantityUsed.setText(cText.pIntToStringStr(packaging.getQuantityUsedInt()));
    }


    @Override
    public int getItemCount () {
        if (this.localPackagingUnits != null)
            return this.localPackagingUnits.size();
        else return 0;
    }

    //End Region Default Methods

    //Region Public Methods

    public void pFillData(List<cPackaging> pvDataObl) {
        this.localPackagingUnits = pvDataObl;
    }

    //End Region Public Methods

}
