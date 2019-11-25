package SSU_WHS.Intake.IntakeorderMATLines;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ICS.Utils.cText;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;

import SSU_WHS.Intake.Intakeorders.cIntakeorder;
import nl.icsvertex.scansuite.Activities.Intake.IntakeorderLinesActivity;
import nl.icsvertex.scansuite.R;

public class cIntakeorderMATLineAdapter extends RecyclerView.Adapter<cIntakeorderMATLineAdapter.IntakeorderMATLineViewHolder>  {

    //Region Public Properties
    public class IntakeorderMATLineViewHolder extends RecyclerView.ViewHolder{

        private TextView textViewBinCode;
        private TextView textViewLine;
        private TextView textViewQuantity;
        private TextView textViewSourceNo;
        private FrameLayout intakeOrderMATItemLinearLayout;
        private ImageView imageSendStatus;

        public RelativeLayout viewBackground;
        public ConstraintLayout viewForeground;


        public IntakeorderMATLineViewHolder(View pvItemView) {
            super(pvItemView);

            this.intakeOrderMATItemLinearLayout = pvItemView.findViewById(R.id.intakeorderLineItemLinearLayout);

            this.textViewLine = pvItemView.findViewById(R.id.textViewDescription);
            this.textViewLine.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            this.textViewLine.setSingleLine(true);
            this.textViewLine.setMarqueeRepeatLimit(5);
            this.textViewLine.setSelected(true);

            this.textViewBinCode = pvItemView.findViewById(R.id.textViewBIN);
            this.textViewBinCode.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            this.textViewBinCode.setSingleLine(true);
            this.textViewBinCode.setMarqueeRepeatLimit(5);
            this.textViewBinCode.setSelected(true);

            this.textViewSourceNo = pvItemView.findViewById(R.id.textViewSourceNo);
            this.textViewSourceNo.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            this.textViewSourceNo.setSingleLine(true);
            this.textViewSourceNo.setMarqueeRepeatLimit(5);
            this.textViewSourceNo.setSelected(true);

            this.textViewQuantity = pvItemView.findViewById(R.id.textViewQuantity);
            this.viewBackground = pvItemView.findViewById(R.id.view_background);
            this.viewForeground = pvItemView.findViewById(R.id.view_foreground);
            this.imageSendStatus = pvItemView.findViewById(R.id.imageSendStatus);

        }
    }
    //End Region Public Properties

    //Region Private Properties
    private final LayoutInflater LayoutInflaterObject;
    private List<cIntakeorderMATLine> localIntakeorderMATLinesObl;
    //End Region Private Propertiess

    private List<FrameLayout> intakeorderLineItemFrameLayout = new ArrayList<>();
    private RecyclerView RecyclerView;

    //Region Constructor
    public cIntakeorderMATLineAdapter() {
        this.LayoutInflaterObject = LayoutInflater.from(cAppExtension.context);
    }
    // End Region Constructor

    //Region Default Methods

    @Override
    public cIntakeorderMATLineAdapter.IntakeorderMATLineViewHolder onCreateViewHolder(ViewGroup pvParent, int pvViewTypeInt) {
        View itemView = this.LayoutInflaterObject.inflate(R.layout.recycler_intakeorderline, pvParent, false);
        return new cIntakeorderMATLineAdapter.IntakeorderMATLineViewHolder(itemView);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView pvRecyclerView) {
        this.RecyclerView = pvRecyclerView;
        super.onAttachedToRecyclerView( this.RecyclerView);
    }

    @Override
    public void onBindViewHolder(final cIntakeorderMATLineAdapter.IntakeorderMATLineViewHolder pvHolder, final int pvPositionInt) {

        this.intakeorderLineItemFrameLayout.add(pvHolder.intakeOrderMATItemLinearLayout);

        if (this.localIntakeorderMATLinesObl == null || this.localIntakeorderMATLinesObl.size() == 0 ) {
            return;
        }

        final cIntakeorderMATLine currentIntakeorderMatLine = this.localIntakeorderMATLinesObl.get(pvPositionInt);

        //Set fields
        String lineDescriptionStr = currentIntakeorderMatLine.getItemNoStr() + "~" + currentIntakeorderMatLine.getVariantCodeStr() + ": " + currentIntakeorderMatLine.getDescriptionStr();
        String quantityToShowStr = currentIntakeorderMatLine.getQuantityHandledDbl().intValue() + "/" + currentIntakeorderMatLine.getQuantityDbl().intValue();
        String binCodeStr = currentIntakeorderMatLine.binCodeStr;
        String sourceNoStr = currentIntakeorderMatLine.sourceNoStr;

        //Set description and quantity
        pvHolder.textViewLine.setText(lineDescriptionStr);
        pvHolder.textViewQuantity.setText(quantityToShowStr);
        pvHolder.textViewBinCode.setText(binCodeStr);
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
                    if (RecyclerView.getId() == R.id.recyclerViewLines) {
                        IntakeorderLinesActivity.pIntakelineSelected(currentIntakeorderMatLine);
                    }
                }
            }
        });

        //End On Click Listener


        //Select the first one, or selected index
        if (pvPositionInt == 0 && cAppExtension.activity instanceof IntakeorderLinesActivity &&  RecyclerView.getId() == R.id.recyclerViewLines) {
            pvHolder.intakeOrderMATItemLinearLayout.performClick();
            return;
        }
    }

    @Override
    public int getItemCount () {
        if (this.localIntakeorderMATLinesObl != null)
            return this.localIntakeorderMATLinesObl.size();
        else return 0;
    }

    //End Region Default Methods

    //Region Public Methods
    public void pFillData(List<cIntakeorderMATLine> pvDataObl) {
        this.localIntakeorderMATLinesObl = pvDataObl;
        IntakeorderLinesActivity.pSetToolBarTitleWithCounters("(" + cText.pIntToStringStr(this.localIntakeorderMATLinesObl.size())  + "/" + cText.pIntToStringStr(cIntakeorder.currentIntakeOrder.linesObl().size()) + ") " + cAppExtension.activity.getString(R.string.lines) + " " + cAppExtension.activity.getString(R.string.shown) );
        notifyDataSetChanged();
    }

    public void pSetFilter(String pvQueryTextStr) {
        this.localIntakeorderMATLinesObl = mGetFilteredListObl(pvQueryTextStr);
        IntakeorderLinesActivity.pSetToolBarTitleWithCounters("(" + cText.pIntToStringStr(this.localIntakeorderMATLinesObl.size())  + "/" + cText.pIntToStringStr(cIntakeorder.currentIntakeOrder.linesObl().size()) + ") " + cAppExtension.activity.getString(R.string.lines) + " " + cAppExtension.activity.getString(R.string.shown) );
        notifyDataSetChanged();
    }

    //End Region Public Methods

    //Region Private Methods

    private List<cIntakeorderMATLine> mGetFilteredListObl(String pvQueryTextStr) {
        List<cIntakeorderMATLine> resultObl = new ArrayList<>();

        String variantCodeStr = "";
        int loopInt = 0;

        if (this.localIntakeorderMATLinesObl == null || this.localIntakeorderMATLinesObl.size() == 0) {
            return resultObl;
        }

        if (pvQueryTextStr.isEmpty()) {
            return cIntakeorder.currentIntakeOrder.linesObl();
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

        for (cIntakeorderMATLine intakeorderMATLine :this.localIntakeorderMATLinesObl){

            if (!intakeorderMATLine.getItemNoStr().equalsIgnoreCase(fieldsObl[0])) {
                continue;
            }

            if (!variantCodeStr.isEmpty()) {
                if (!intakeorderMATLine.getVariantCodeStr().equalsIgnoreCase(variantCodeStr)) {
                    continue;
                }
            }

            resultObl.add(intakeorderMATLine);

        }

        return resultObl;
    }

    //End Region Private Methods

}
