package SSU_WHS.Intake.IntakeorderMATLineSummary;

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

import ICS.Utils.cText;
import ICS.cAppExtension;
import SSU_WHS.Intake.Intakeorders.cIntakeorder;
import nl.icsvertex.scansuite.Activities.Intake.IntakeorderMASLinesActivity;
import nl.icsvertex.scansuite.Activities.Intake.IntakeorderMATLinesActivity;
import nl.icsvertex.scansuite.R;

public class cIntakeorderMATSummaryLineAdapter extends RecyclerView.Adapter<cIntakeorderMATSummaryLineAdapter.IntakeorderMATSummaryLineViewHolder>  {

    //Region Public Properties
    public static class IntakeorderMATSummaryLineViewHolder extends RecyclerView.ViewHolder{

        private final TextView textViewItemNoAndVariantCode;
        private final TextView textViewDescription;
        private final TextView textViewQuantity;
        private final TextView textViewSourceNo;
        private final ImageView imageBin;
        private final TextView textViewBinCode;
        private final FrameLayout receiveorderLineItemSummaryFrameLayout;
        private final ImageView imageSendStatus;

        public RelativeLayout viewBackground;
        public ConstraintLayout viewForeground;

        public IntakeorderMATSummaryLineViewHolder(View pvItemView) {
            super(pvItemView);

            this.receiveorderLineItemSummaryFrameLayout = pvItemView.findViewById(R.id.receiveorderLineItemSummaryFrameLayout);

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

            this.imageBin = pvItemView.findViewById(R.id.imageBin);
            this.textViewBinCode = pvItemView.findViewById(R.id.textViewBinCode);

            this.textViewQuantity = pvItemView.findViewById(R.id.textViewQuantity);
            this.imageSendStatus = pvItemView.findViewById(R.id.imageSendStatus);

            this.viewBackground = pvItemView.findViewById(R.id.view_background);
            this.viewForeground = pvItemView.findViewById(R.id.view_foreground);

        }
    }
    //End Region Public Properties

    //Region Private Properties
    private final LayoutInflater LayoutInflaterObject;
    private List<cIntakeorderMATSummaryLine> localIntakeorderMATSummaryLinesObl;
    //End Region Private Propertiess

    private final List<FrameLayout> intakeorderLineItemFrameLayout = new ArrayList<>();


    //Region Constructor
    public cIntakeorderMATSummaryLineAdapter() {
        this.LayoutInflaterObject = LayoutInflater.from(cAppExtension.context);

    }
    // End Region Constructor

    //Region Default Methods

    @NonNull
    @Override
    public cIntakeorderMATSummaryLineAdapter.IntakeorderMATSummaryLineViewHolder onCreateViewHolder(@NonNull ViewGroup pvParent, int pvViewTypeInt) {
        View itemView = this.LayoutInflaterObject.inflate(R.layout.recycler_intakeorderline_summary, pvParent, false);
        return new IntakeorderMATSummaryLineViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final cIntakeorderMATSummaryLineAdapter.IntakeorderMATSummaryLineViewHolder pvHolder, final int pvPositionInt) {

        this.intakeorderLineItemFrameLayout.add(pvHolder.receiveorderLineItemSummaryFrameLayout);

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

        String quantityToShowStr;

        if (currentIntakeorderMATSummaryLine.getQuantityHandledDbl().intValue() >= currentIntakeorderMATSummaryLine.getQuantityDbl().intValue() && cIntakeorder.currentIntakeOrder.getOrderTypeStr().equalsIgnoreCase("MAS")) {
            quantityToShowStr =  cText.pDoubleToStringStr(currentIntakeorderMATSummaryLine.getQuantityHandledDbl());



        }
        else
        {
            quantityToShowStr = currentIntakeorderMATSummaryLine.getQuantityHandledDbl().intValue() + "/" + currentIntakeorderMATSummaryLine.getQuantityDbl().intValue();
        }

        if (currentIntakeorderMATSummaryLine.isUniqueBln()) {
            quantityToShowStr = "";
        }

        String sourceNoStr = currentIntakeorderMATSummaryLine.getSourceNoStr();

        //Set description and quantity
        pvHolder.textViewItemNoAndVariantCode.setText(itemNoAndVariantCodeStr);
        pvHolder.textViewDescription.setText(lineDescriptionStr);

        if (lineDescriptionStr.equalsIgnoreCase(itemNoAndVariantCodeStr)) {
            pvHolder.textViewDescription.setVisibility(View.GONE);
        }

        pvHolder.textViewQuantity.setText(quantityToShowStr);

        if (cIntakeorder.currentIntakeOrder.sourceNoObl.size() == 1) {
            pvHolder.textViewSourceNo.setVisibility(View.GONE);
        }
        else
        {
            pvHolder.textViewSourceNo.setVisibility(View.VISIBLE);
            pvHolder.textViewSourceNo.setText(sourceNoStr);
        }

        String binCodeStr;

        if (currentIntakeorderMATSummaryLine.getBinCodeHandledStr().isEmpty()) {
            binCodeStr = currentIntakeorderMATSummaryLine.getBinCodeStr();

        }
        else {
            binCodeStr = currentIntakeorderMATSummaryLine.getBinCodeHandledStr();
        }

        if (currentIntakeorderMATSummaryLine.getContainerStr() != null &&  !currentIntakeorderMATSummaryLine.getContainerStr().isEmpty()) {
            binCodeStr += " (" + currentIntakeorderMATSummaryLine.getContainerStr() + ")";
        }

        if (binCodeStr.isEmpty()) {
            pvHolder.textViewBinCode.setVisibility(View.GONE);
            pvHolder.imageBin.setVisibility(View.GONE);
        }

        pvHolder.textViewBinCode.setText(binCodeStr);

        pvHolder.imageSendStatus.setVisibility(View.GONE);

        //Start On Click Listener
        pvHolder.receiveorderLineItemSummaryFrameLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View pvView) {

                //deselect all
                for (FrameLayout frameLayout : intakeorderLineItemFrameLayout) {
                    frameLayout.setSelected(false);
                }

                //select current
                pvView.setSelected(true);

                //Kick off correct event at correct activity
                if (cAppExtension.activity instanceof IntakeorderMATLinesActivity) {
                    IntakeorderMATLinesActivity intakeorderMATLinesActivity = (IntakeorderMATLinesActivity)cAppExtension.activity;
                    intakeorderMATLinesActivity.pIntakelineSelected(currentIntakeorderMATSummaryLine);
                }

                if (cAppExtension.activity instanceof IntakeorderMASLinesActivity) {
                    IntakeorderMASLinesActivity intakeorderMASLinesActivity = (IntakeorderMASLinesActivity)cAppExtension.activity;
                    intakeorderMASLinesActivity.pIntakelineSelected(currentIntakeorderMATSummaryLine);
                }
            }
        });

        //End On Click Listener

        //Select the first one, or selected index
        if (pvPositionInt == 0 && cAppExtension.activity instanceof IntakeorderMATLinesActivity) {
            pvHolder.receiveorderLineItemSummaryFrameLayout.performClick();
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

        if (cAppExtension.activity instanceof IntakeorderMATLinesActivity) {
            IntakeorderMATLinesActivity intakeorderMATLinesActivity = (IntakeorderMATLinesActivity)cAppExtension.activity;
            intakeorderMATLinesActivity.pSetToolBarTitleWithCounters( cText.pIntToStringStr(this.localIntakeorderMATSummaryLinesObl.size())  + "/" + cText.pIntToStringStr(cIntakeorder.currentIntakeOrder.summaryMATLinesObl().size()) + " "  + cAppExtension.activity.getString(R.string.lines) + " " + cAppExtension.activity.getString(R.string.shown) );
            intakeorderMATLinesActivity.pShowData(this.localIntakeorderMATSummaryLinesObl);
        }

        if (cAppExtension.activity instanceof IntakeorderMASLinesActivity) {
            IntakeorderMASLinesActivity intakeorderMASLinesActivity = (IntakeorderMASLinesActivity)cAppExtension.activity;
            intakeorderMASLinesActivity.pSetToolBarTitleWithCounters(  cText.pIntToStringStr(cIntakeorderMATSummaryLine.sortedMATSummaryLinesGeneratedObl().size()) + " "  + cAppExtension.activity.getString(R.string.lines));
            intakeorderMASLinesActivity.pShowData(this.localIntakeorderMATSummaryLinesObl);
        }

    }

    public void pShowDeviations() {

        this.localIntakeorderMATSummaryLinesObl = mGetDeviationsListObl();

        if (cAppExtension.activity instanceof IntakeorderMATLinesActivity) {
            IntakeorderMATLinesActivity intakeorderMATLinesActivity = (IntakeorderMATLinesActivity)cAppExtension.activity;
            intakeorderMATLinesActivity.pSetToolBarTitleWithCounters(cText.pIntToStringStr(this.localIntakeorderMATSummaryLinesObl.size())  + "/" + cText.pIntToStringStr(cIntakeorder.currentIntakeOrder.summaryMATLinesObl().size()) + " " + cAppExtension.activity.getString(R.string.lines) + " " + cAppExtension.activity.getString(R.string.shown) );
            intakeorderMATLinesActivity.pShowData(this.localIntakeorderMATSummaryLinesObl);
        }
    }

    public void pSetFilter(String pvQueryTextStr, Boolean pvScannedBln) {
        this.localIntakeorderMATSummaryLinesObl = mGetFilteredListObl(pvQueryTextStr);

        if (cAppExtension.activity instanceof IntakeorderMATLinesActivity) {
            IntakeorderMATLinesActivity intakeorderMATLinesActivity = (IntakeorderMATLinesActivity)cAppExtension.activity;
            intakeorderMATLinesActivity.pSetToolBarTitleWithCounters("(" + cText.pIntToStringStr(this.localIntakeorderMATSummaryLinesObl.size())  + "/" + cText.pIntToStringStr(cIntakeorder.currentIntakeOrder.summaryMATLinesObl().size()) + ") " + cAppExtension.activity.getString(R.string.lines) + " " + cAppExtension.activity.getString(R.string.shown) );
            if (this.localIntakeorderMATSummaryLinesObl.size() == 1 && pvScannedBln) {

                intakeorderMATLinesActivity.pIntakelineSelected(this.localIntakeorderMATSummaryLinesObl.get(0));
                intakeorderMATLinesActivity.pStartLine();
            }
            else{
                intakeorderMATLinesActivity.pShowData(this.localIntakeorderMATSummaryLinesObl);
            }
        }


        if (cAppExtension.activity instanceof IntakeorderMASLinesActivity) {
            IntakeorderMASLinesActivity intakeorderMASLinesActivity = (IntakeorderMASLinesActivity)cAppExtension.activity;
            intakeorderMASLinesActivity.pSetToolBarTitleWithCounters("(" + cText.pIntToStringStr(cIntakeorderMATSummaryLine.sortedMATSummaryLinesGeneratedObl().size()) + cAppExtension.activity.getString(R.string.lines));
            if (this.localIntakeorderMATSummaryLinesObl.size() == 1 && pvScannedBln) {
                intakeorderMASLinesActivity.pIntakelineSelected(this.localIntakeorderMATSummaryLinesObl.get(0));
            }
            else{
                intakeorderMASLinesActivity.pShowData(this.localIntakeorderMATSummaryLinesObl);
            }
        }


    }

    public void pSetBINFilter(String pvQueryTextStr) {
        this.localIntakeorderMATSummaryLinesObl = mGetFilteredListForBINObl(pvQueryTextStr);

        if (cAppExtension.activity instanceof IntakeorderMATLinesActivity) {
            IntakeorderMATLinesActivity intakeorderMATLinesActivity = (IntakeorderMATLinesActivity)cAppExtension.activity;
            intakeorderMATLinesActivity.pSetToolBarTitleWithCounters("(" + cText.pIntToStringStr(this.localIntakeorderMATSummaryLinesObl.size())  + "/" + cText.pIntToStringStr(cIntakeorder.currentIntakeOrder.summaryMATLinesObl().size()) + ") " + cAppExtension.activity.getString(R.string.lines) + " " + cAppExtension.activity.getString(R.string.shown) );

        }

        if (cAppExtension.activity instanceof IntakeorderMATLinesActivity) {

            IntakeorderMATLinesActivity intakeorderMATLinesActivity = (IntakeorderMATLinesActivity)cAppExtension.activity;
                intakeorderMATLinesActivity.pShowData(this.localIntakeorderMATSummaryLinesObl);
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
        for (cIntakeorderMATSummaryLine intakeorderMATSummaryLine : cIntakeorderMATSummaryLine.sortedMATSummaryLinesObl())
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
