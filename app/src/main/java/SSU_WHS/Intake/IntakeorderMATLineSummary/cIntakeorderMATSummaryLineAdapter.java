package SSU_WHS.Intake.IntakeorderMATLineSummary;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ICS.Utils.cText;
import ICS.cAppExtension;
import SSU_WHS.Intake.IntakeorderMATLines.cIntakeorderMATLine;
import SSU_WHS.Intake.Intakeorders.cIntakeorder;
import nl.icsvertex.scansuite.Activities.Intake.IntakeorderLinesActivity;
import nl.icsvertex.scansuite.R;

public class cIntakeorderMATSummaryLineAdapter extends RecyclerView.Adapter<cIntakeorderMATSummaryLineAdapter.IntakeorderMATLineViewHolder>  {

    //Region Public Properties
    public class IntakeorderMATLineViewHolder extends RecyclerView.ViewHolder{

        private TextView textViewItemNoAndVariantCode;
        private TextView textViewDescription;
        private TextView textViewQuantity;
        private TextView textViewSourceNo;
        private FrameLayout intakeOrderMATItemLinearLayout;
        private ImageView imageSendStatus;
        private  TextView textViewBins;


        public IntakeorderMATLineViewHolder(View pvItemView) {
            super(pvItemView);

            this.intakeOrderMATItemLinearLayout = pvItemView.findViewById(R.id.intakeorderLineItemSummaryLinearLayout);

            this.textViewItemNoAndVariantCode = pvItemView.findViewById(R.id.textViewItemNoAndVariant);
            this.textViewItemNoAndVariantCode.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            this.textViewItemNoAndVariantCode.setSingleLine(true);
            this.textViewItemNoAndVariantCode.setMarqueeRepeatLimit(5);
            this.textViewItemNoAndVariantCode.setSelected(true);

            this.textViewDescription = pvItemView.findViewById(R.id.textViewDescription);
            this.textViewDescription.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            this.textViewDescription.setSingleLine(true);
            this.textViewDescription.setMarqueeRepeatLimit(5);
            this.textViewDescription.setSelected(true);

            this.textViewSourceNo = pvItemView.findViewById(R.id.textViewSourceNo);
            this.textViewSourceNo.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            this.textViewSourceNo.setSingleLine(true);
            this.textViewSourceNo.setMarqueeRepeatLimit(5);
            this.textViewSourceNo.setSelected(true);

            this.textViewQuantity = pvItemView.findViewById(R.id.textViewQuantity);
            this.imageSendStatus = pvItemView.findViewById(R.id.imageSendStatus);

        }
    }
    //End Region Public Properties

    //Region Private Properties
    private final LayoutInflater LayoutInflaterObject;
    private List<cIntakeorderMATSummaryLine> localIntakeorderMATSummaryLinesObl;
    //End Region Private Propertiess

    private List<FrameLayout> intakeorderLineItemFrameLayout = new ArrayList<>();


    //Region Constructor
    public cIntakeorderMATSummaryLineAdapter() {
        this.LayoutInflaterObject = LayoutInflater.from(cAppExtension.context);

    }
    // End Region Constructor

    //Region Default Methods

    @Override
    public cIntakeorderMATSummaryLineAdapter.IntakeorderMATLineViewHolder onCreateViewHolder(ViewGroup pvParent, int pvViewTypeInt) {
        View itemView = this.LayoutInflaterObject.inflate(R.layout.recycler_intakeorderline_summary, pvParent, false);
        return new cIntakeorderMATSummaryLineAdapter.IntakeorderMATLineViewHolder(itemView);
    }

//    @Override
//    public void onAttachedToRecyclerView(RecyclerView pvRecyclerView) {
//        this.recyclerlineBins = pvRecyclerView;
//        super.onAttachedToRecyclerView( this.recyclerlineBins);
//    }

    @Override
    public void onBindViewHolder(final cIntakeorderMATSummaryLineAdapter.IntakeorderMATLineViewHolder pvHolder, final int pvPositionInt) {

        this.intakeorderLineItemFrameLayout.add(pvHolder.intakeOrderMATItemLinearLayout);

        if (this.localIntakeorderMATSummaryLinesObl == null || this.localIntakeorderMATSummaryLinesObl.size() == 0 ) {
            return;
        }

        final cIntakeorderMATSummaryLine currentIntakeorderMATSummaryLine = this.localIntakeorderMATSummaryLinesObl.get(pvPositionInt);

        //Set fields
        String itemNoAndVariantCodeStr = currentIntakeorderMATSummaryLine.getItemNoStr() + "~" + currentIntakeorderMATSummaryLine.getVariantCodeStr();
        String lineDescriptionStr = currentIntakeorderMATSummaryLine.getDescriptionStr();
        String quantityToShowStr = currentIntakeorderMATSummaryLine.getQuantityHandledDbl().intValue() + "/" + currentIntakeorderMATSummaryLine.getQuantityDbl().intValue();
        String sourceNoStr = currentIntakeorderMATSummaryLine.sourceNoStr;

        //Set description and quantity
        pvHolder.textViewItemNoAndVariantCode.setText(itemNoAndVariantCodeStr);
        pvHolder.textViewDescription.setText(lineDescriptionStr);
        pvHolder.textViewQuantity.setText(quantityToShowStr);
        pvHolder.textViewSourceNo.setText(sourceNoStr);
        pvHolder.imageSendStatus.setVisibility(View.INVISIBLE);

        //Start On Click Listener
        pvHolder.intakeOrderMATItemLinearLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View pvView) {

                //deselect all
                for (FrameLayout frameLayout : intakeorderLineItemFrameLayout) {
                    frameLayout.setSelected(false);
                }

                //select current
                pvView.setSelected(true);

                //Kick off correct event at correct activity
                if (cAppExtension.context instanceof IntakeorderLinesActivity) {
                        IntakeorderLinesActivity.pIntakelineSelected(currentIntakeorderMATSummaryLine);
                }
            }
        });


        pvHolder.intakeOrderMATItemLinearLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                pvHolder.textViewBins.setVisibility(View.VISIBLE);
                return true;
            }
        });

        //End On Click Listener


        //Select the first one, or selected index
        if (pvPositionInt == 0 && cAppExtension.activity instanceof IntakeorderLinesActivity ) {
            pvHolder.intakeOrderMATItemLinearLayout.performClick();
            return;
        }
    }

    @Override
    public int getItemCount () {
        if (this.localIntakeorderMATSummaryLinesObl != null)
            return this.localIntakeorderMATSummaryLinesObl.size();
        else return 0;
    }

    //End Region Default Methods

    //Region Public Methods
    public void pFillData(List<cIntakeorderMATSummaryLine> pvDataObl) {
        this.localIntakeorderMATSummaryLinesObl = pvDataObl;
        IntakeorderLinesActivity.pSetToolBarTitleWithCounters("(" + cText.pIntToStringStr(this.localIntakeorderMATSummaryLinesObl.size())  + "/" + cText.pIntToStringStr(cIntakeorder.currentIntakeOrder.summaryLinesObl().size()) + ") " + cAppExtension.activity.getString(R.string.lines) + " " + cAppExtension.activity.getString(R.string.shown) );
        notifyDataSetChanged();
    }

    public void pShowDeviations() {

        this.localIntakeorderMATSummaryLinesObl = mGetDeviationsListObl();
        IntakeorderLinesActivity.pSetToolBarTitleWithCounters("(" + cText.pIntToStringStr(this.localIntakeorderMATSummaryLinesObl.size())  + "/" + cText.pIntToStringStr(cIntakeorder.currentIntakeOrder.summaryLinesObl().size()) + ") " + cAppExtension.activity.getString(R.string.lines) + " " + cAppExtension.activity.getString(R.string.shown) );

        if (cAppExtension.activity instanceof IntakeorderLinesActivity ) {
            IntakeorderLinesActivity.pShowData(this.localIntakeorderMATSummaryLinesObl);
        }

        notifyDataSetChanged();
    }

    public void pSetFilter(String pvQueryTextStr, Boolean pvScannedBln) {
        this.localIntakeorderMATSummaryLinesObl = mGetFilteredListObl(pvQueryTextStr);
        IntakeorderLinesActivity.pSetToolBarTitleWithCounters("(" + cText.pIntToStringStr(this.localIntakeorderMATSummaryLinesObl.size())  + "/" + cText.pIntToStringStr(cIntakeorder.currentIntakeOrder.summaryLinesObl().size()) + ") " + cAppExtension.activity.getString(R.string.lines) + " " + cAppExtension.activity.getString(R.string.shown) );
        notifyDataSetChanged();

        if (this.localIntakeorderMATSummaryLinesObl.size() == 1 && pvScannedBln) {

            if (cAppExtension.activity instanceof  IntakeorderLinesActivity) {
                IntakeorderLinesActivity.pIntakelineSelected(this.localIntakeorderMATSummaryLinesObl.get(0));
                IntakeorderLinesActivity.pStartLine();
                return;
            }

        }

    }

    //End Region Public Methods

    //Region Private Methods

    private List<cIntakeorderMATSummaryLine> mGetFilteredListObl(String pvQueryTextStr) {
        List<cIntakeorderMATSummaryLine> resultObl = new ArrayList<>();

        String variantCodeStr = "";
        int loopInt = 0;

        if (this.localIntakeorderMATSummaryLinesObl == null || this.localIntakeorderMATSummaryLinesObl.size() == 0) {
            return resultObl;
        }

        if (pvQueryTextStr.isEmpty()) {
            return cIntakeorder.currentIntakeOrder.summaryLinesObl();
        }

        String[] fieldsObl = pvQueryTextStr.split(" ");
        if (fieldsObl.length <= 0) {
            return resultObl;
        }

        for (String loopStr : fieldsObl) {

            if (loopInt == 0) {
                loopInt += 1;
                continue;
            }

            variantCodeStr += loopStr;
            loopInt += 1;
        }

        for (cIntakeorderMATSummaryLine intakeorderMATSummaryLine :this.localIntakeorderMATSummaryLinesObl){

            if (!intakeorderMATSummaryLine.getItemNoStr().equalsIgnoreCase(fieldsObl[0])) {
                continue;
            }

            if (!variantCodeStr.isEmpty()) {
                if (!intakeorderMATSummaryLine.getVariantCodeStr().equalsIgnoreCase(variantCodeStr)) {
                    continue;
                }
            }

            resultObl.add(intakeorderMATSummaryLine);

        }

        return resultObl;
    }

    private List<cIntakeorderMATSummaryLine> mGetDeviationsListObl() {

        List<cIntakeorderMATSummaryLine> resultObl = new ArrayList<>();
        for (cIntakeorderMATSummaryLine intakeorderMATSummaryLine : cIntakeorderMATSummaryLine.allIntakeorderMATSummaryLinesObl)
        {
            if (intakeorderMATSummaryLine.getQuantityHandledDbl() > intakeorderMATSummaryLine.getQuantityDbl() ||
                intakeorderMATSummaryLine.getQuantityHandledDbl() < intakeorderMATSummaryLine.getQuantityDbl())
            {
                resultObl.add(intakeorderMATSummaryLine);
            }
        }
        return resultObl;
    }

    //End Region Private Methods

}
