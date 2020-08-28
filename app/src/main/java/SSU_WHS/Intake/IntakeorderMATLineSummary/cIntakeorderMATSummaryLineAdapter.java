package SSU_WHS.Intake.IntakeorderMATLineSummary;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ICS.Utils.cText;
import ICS.cAppExtension;
import SSU_WHS.Intake.Intakeorders.cIntakeorder;
import nl.icsvertex.scansuite.Activities.Intake.IntakeorderLinesActivity;
import nl.icsvertex.scansuite.R;

public class cIntakeorderMATSummaryLineAdapter extends RecyclerView.Adapter<cIntakeorderMATSummaryLineAdapter.IntakeorderMATLineViewHolder>  {

    //Region Public Properties
    public static class IntakeorderMATLineViewHolder extends RecyclerView.ViewHolder{

        private TextView textViewItemNoAndVariantCode;
        private TextView textViewDescription;
        private TextView textViewQuantity;
        private TextView textViewSourceNo;
        private TextView textViewBinCode;
        private FrameLayout intakeOrderMATItemLinearLayout;
        private ImageView imageSendStatus;

        public IntakeorderMATLineViewHolder(View pvItemView) {
            super(pvItemView);

            this.intakeOrderMATItemLinearLayout = pvItemView.findViewById(R.id.receiveorderLineItemSummaryLinearLayout);

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

            this.textViewBinCode = pvItemView.findViewById(R.id.textViewBinCode);

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

    @NonNull
    @Override
    public cIntakeorderMATSummaryLineAdapter.IntakeorderMATLineViewHolder onCreateViewHolder(@NonNull ViewGroup pvParent, int pvViewTypeInt) {
        View itemView = this.LayoutInflaterObject.inflate(R.layout.recycler_intakeorderline_summary, pvParent, false);
        return new IntakeorderMATLineViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final cIntakeorderMATSummaryLineAdapter.IntakeorderMATLineViewHolder pvHolder, final int pvPositionInt) {

        this.intakeorderLineItemFrameLayout.add(pvHolder.intakeOrderMATItemLinearLayout);

        if (this.localIntakeorderMATSummaryLinesObl == null || this.localIntakeorderMATSummaryLinesObl.size() == 0 ) {
            return;
        }

        final cIntakeorderMATSummaryLine currentIntakeorderMATSummaryLine = this.localIntakeorderMATSummaryLinesObl.get(pvPositionInt);

        //Set fields
        String itemNoAndVariantCodeStr;

        if (currentIntakeorderMATSummaryLine.getVariantCodeStr().isEmpty()) {
            itemNoAndVariantCodeStr = currentIntakeorderMATSummaryLine.getItemNoStr();
        }
        else {
            itemNoAndVariantCodeStr = currentIntakeorderMATSummaryLine.getItemNoStr() + "~" + currentIntakeorderMATSummaryLine.getVariantCodeStr();
        }

        String lineDescriptionStr = currentIntakeorderMATSummaryLine.getDescriptionStr();

        String quantityToShowStr = currentIntakeorderMATSummaryLine.getQuantityHandledDbl().intValue() + "/" + currentIntakeorderMATSummaryLine.getQuantityDbl().intValue();
        String sourceNoStr = currentIntakeorderMATSummaryLine.getSourceNoStr();

        //Set description and quantity
        pvHolder.textViewItemNoAndVariantCode.setText(itemNoAndVariantCodeStr);
        pvHolder.textViewDescription.setText(lineDescriptionStr);
        pvHolder.textViewQuantity.setText(quantityToShowStr);

        if (cIntakeorder.currentIntakeOrder.sourceNoObl.size() == 1) {
            pvHolder.textViewSourceNo.setVisibility(View.GONE);
        }
        else
        {
            pvHolder.textViewSourceNo.setVisibility(View.VISIBLE);
            pvHolder.textViewSourceNo.setText(sourceNoStr);
        }

        if (currentIntakeorderMATSummaryLine.getBinCodeHandledStr().isEmpty()) {
            pvHolder.textViewBinCode.setText(currentIntakeorderMATSummaryLine.getBinCodeStr());
        }
        else {
            pvHolder.textViewBinCode.setText(currentIntakeorderMATSummaryLine.getBinCodeHandledStr());
        }

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
                if (cAppExtension.activity instanceof IntakeorderLinesActivity) {
                    IntakeorderLinesActivity intakeorderLinesActivity = (IntakeorderLinesActivity)cAppExtension.activity;
                    intakeorderLinesActivity.pIntakelineSelected(currentIntakeorderMATSummaryLine);
                }
            }
        });

        //End On Click Listener

        //Select the first one, or selected index
        if (pvPositionInt == 0 && cAppExtension.activity instanceof IntakeorderLinesActivity ) {
            pvHolder.intakeOrderMATItemLinearLayout.performClick();
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

        if (cAppExtension.activity instanceof  IntakeorderLinesActivity) {
            IntakeorderLinesActivity intakeorderLinesActivity = (IntakeorderLinesActivity)cAppExtension.activity;
            intakeorderLinesActivity.pSetToolBarTitleWithCounters( cText.pIntToStringStr(this.localIntakeorderMATSummaryLinesObl.size())  + "/" + cText.pIntToStringStr(cIntakeorder.currentIntakeOrder.summaryMATLinesObl().size()) + " "  + cAppExtension.activity.getString(R.string.lines) + " " + cAppExtension.activity.getString(R.string.shown) );
            intakeorderLinesActivity.pShowData(this.localIntakeorderMATSummaryLinesObl);
        }

    }

    public void pShowDeviations() {

        this.localIntakeorderMATSummaryLinesObl = mGetDeviationsListObl();

        if (cAppExtension.activity instanceof  IntakeorderLinesActivity) {
            IntakeorderLinesActivity intakeorderLinesActivity = (IntakeorderLinesActivity)cAppExtension.activity;
            intakeorderLinesActivity.pSetToolBarTitleWithCounters(cText.pIntToStringStr(this.localIntakeorderMATSummaryLinesObl.size())  + "/" + cText.pIntToStringStr(cIntakeorder.currentIntakeOrder.summaryMATLinesObl().size()) + " " + cAppExtension.activity.getString(R.string.lines) + " " + cAppExtension.activity.getString(R.string.shown) );
            intakeorderLinesActivity.pShowData(this.localIntakeorderMATSummaryLinesObl);
        }
    }

    public void pSetFilter(String pvQueryTextStr, Boolean pvScannedBln) {
        this.localIntakeorderMATSummaryLinesObl = mGetFilteredListObl(pvQueryTextStr);

        if (cAppExtension.activity instanceof  IntakeorderLinesActivity) {
            IntakeorderLinesActivity intakeorderLinesActivity = (IntakeorderLinesActivity)cAppExtension.activity;
            intakeorderLinesActivity.pSetToolBarTitleWithCounters("(" + cText.pIntToStringStr(this.localIntakeorderMATSummaryLinesObl.size())  + "/" + cText.pIntToStringStr(cIntakeorder.currentIntakeOrder.summaryMATLinesObl().size()) + ") " + cAppExtension.activity.getString(R.string.lines) + " " + cAppExtension.activity.getString(R.string.shown) );

        }

        if (cAppExtension.activity instanceof  IntakeorderLinesActivity) {

            IntakeorderLinesActivity intakeorderLinesActivity = (IntakeorderLinesActivity)cAppExtension.activity;


        if (this.localIntakeorderMATSummaryLinesObl.size() == 1 && pvScannedBln) {

                intakeorderLinesActivity.pIntakelineSelected(this.localIntakeorderMATSummaryLinesObl.get(0));
                intakeorderLinesActivity.pStartLine();
            }
        else{
            intakeorderLinesActivity.pShowData(this.localIntakeorderMATSummaryLinesObl);
        }
        }


    }

    public void pSetBINFilter(String pvQueryTextStr) {
        this.localIntakeorderMATSummaryLinesObl = mGetFilteredListForBINObl(pvQueryTextStr);

        if (cAppExtension.activity instanceof  IntakeorderLinesActivity) {
            IntakeorderLinesActivity intakeorderLinesActivity = (IntakeorderLinesActivity)cAppExtension.activity;
            intakeorderLinesActivity.pSetToolBarTitleWithCounters("(" + cText.pIntToStringStr(this.localIntakeorderMATSummaryLinesObl.size())  + "/" + cText.pIntToStringStr(cIntakeorder.currentIntakeOrder.summaryMATLinesObl().size()) + ") " + cAppExtension.activity.getString(R.string.lines) + " " + cAppExtension.activity.getString(R.string.shown) );

        }

        if (cAppExtension.activity instanceof  IntakeorderLinesActivity) {

            IntakeorderLinesActivity intakeorderLinesActivity = (IntakeorderLinesActivity)cAppExtension.activity;
                intakeorderLinesActivity.pShowData(this.localIntakeorderMATSummaryLinesObl);
        }

    }


    //End Region Public Methods

    //Region Private Methods

    private List<cIntakeorderMATSummaryLine> mGetFilteredListObl(String pvQueryTextStr) {
        List<cIntakeorderMATSummaryLine> resultObl = new ArrayList<>();

        StringBuilder variantCodeStr = new StringBuilder();
        int loopInt = 0;

        if (this.localIntakeorderMATSummaryLinesObl == null || this.localIntakeorderMATSummaryLinesObl.size() == 0) {
            return resultObl;
        }

        if (pvQueryTextStr.isEmpty()) {
            return cIntakeorder.currentIntakeOrder.summaryMATLinesObl();
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

            variantCodeStr.append(loopStr);
            loopInt += 1;
        }

        for (cIntakeorderMATSummaryLine intakeorderMATSummaryLine :this.localIntakeorderMATSummaryLinesObl){

            if (!intakeorderMATSummaryLine.getItemNoStr().equalsIgnoreCase(fieldsObl[0])) {
                continue;
            }

            if (variantCodeStr.length() > 0) {
                if (!intakeorderMATSummaryLine.getVariantCodeStr().equalsIgnoreCase(variantCodeStr.toString())) {
                    continue;
                }
            }

            resultObl.add(intakeorderMATSummaryLine);

        }

        return resultObl;
    }

    private List<cIntakeorderMATSummaryLine> mGetFilteredListForBINObl(String pvQueryTextStr) {
        List<cIntakeorderMATSummaryLine> resultObl = new ArrayList<>();


        if (this.localIntakeorderMATSummaryLinesObl == null || this.localIntakeorderMATSummaryLinesObl.size() == 0) {
            return resultObl;
        }



        for (cIntakeorderMATSummaryLine intakeorderMATSummaryLine :this.localIntakeorderMATSummaryLinesObl){

            if (!intakeorderMATSummaryLine.getBinCodeStr().equalsIgnoreCase(pvQueryTextStr)) {
                continue;
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
