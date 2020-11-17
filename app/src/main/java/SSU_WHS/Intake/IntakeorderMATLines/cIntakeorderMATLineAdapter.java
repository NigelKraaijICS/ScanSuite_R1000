package SSU_WHS.Intake.IntakeorderMATLines;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
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
import nl.icsvertex.scansuite.Activities.Intake.IntakeorderMATLinesActivity;
import nl.icsvertex.scansuite.R;

public class cIntakeorderMATLineAdapter extends RecyclerView.Adapter<cIntakeorderMATLineAdapter.IntakeorderMATLineViewHolder>  {

    //Region Public Properties
    public static class IntakeorderMATLineViewHolder extends RecyclerView.ViewHolder{

        private TextView textViewBinCode;
        private TextView textViewQuantity;
        private FrameLayout intakeOrderMATItemFrameLayout;

        public RelativeLayout viewBackground;
        public ConstraintLayout viewForeground;


        public IntakeorderMATLineViewHolder(View pvItemView) {
            super(pvItemView);

            this.intakeOrderMATItemFrameLayout = pvItemView.findViewById(R.id.receiveorderLineItemLinearLayout);

            this.textViewBinCode = pvItemView.findViewById(R.id.timeText);
            this.textViewBinCode.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            this.textViewBinCode.setSingleLine(true);
            this.textViewBinCode.setMarqueeRepeatLimit(5);
            this.textViewBinCode.setSelected(true);
            this.viewBackground = pvItemView.findViewById(R.id.view_background);
            this.viewForeground = pvItemView.findViewById(R.id.view_foreground);
            this.textViewQuantity = pvItemView.findViewById(R.id.storedQuantityText);

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

    @NonNull
    @Override
    public cIntakeorderMATLineAdapter.IntakeorderMATLineViewHolder onCreateViewHolder(@NonNull ViewGroup pvParent, int pvViewTypeInt) {
        View itemView = this.LayoutInflaterObject.inflate(R.layout.recycler_intakeorderline, pvParent, false);
        return new IntakeorderMATLineViewHolder(itemView);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView pvRecyclerView) {
        this.RecyclerView = pvRecyclerView;
        super.onAttachedToRecyclerView( this.RecyclerView);
    }

    @Override
    public void onBindViewHolder(final cIntakeorderMATLineAdapter.IntakeorderMATLineViewHolder pvHolder, final int pvPositionInt) {

        this.intakeorderLineItemFrameLayout.add(pvHolder.intakeOrderMATItemFrameLayout);

        if (this.localIntakeorderMATLinesObl == null || this.localIntakeorderMATLinesObl.size() == 0 ) {
            return;
        }

        final cIntakeorderMATLine currentIntakeorderMatLine = this.localIntakeorderMATLinesObl.get(pvPositionInt);

        //Set fields
        String quantityToShowStr = cText.pDoubleToStringStr(currentIntakeorderMatLine.getQuantityHandledDbl());
        String binCodeStr;

        if (currentIntakeorderMatLine.getBinCodeHandledStr().isEmpty()) {
            binCodeStr = currentIntakeorderMatLine.getBinCodeStr();
        } else {
            binCodeStr = currentIntakeorderMatLine.getBinCodeHandledStr();
        }

        //Set description and quantity
        pvHolder.textViewQuantity.setText(quantityToShowStr);
        pvHolder.textViewBinCode.setText(binCodeStr);

        //Start On Click Listener
        pvHolder.intakeOrderMATItemFrameLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View pvView) {

                //deselect all
                for (FrameLayout frameLayout : intakeorderLineItemFrameLayout) {
                    frameLayout.setSelected(false);
                }

                //select current
                //pvView.setSelected(true);

            }
        });

        //End On Click Listener


        //Select the first one, or selected index
        if (pvPositionInt == 0 && cAppExtension.activity instanceof IntakeorderMATLinesActivity &&  RecyclerView.getId() == R.id.recyclerViewLines) {
            pvHolder.intakeOrderMATItemFrameLayout.performClick();
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
    public void pFillData(List<cIntakeorderMATLine> pvDataObl ) {
        this.localIntakeorderMATLinesObl = pvDataObl;

        if (cAppExtension.activity instanceof IntakeorderMATLinesActivity) {
            IntakeorderMATLinesActivity intakeorderMATLinesActivity = (IntakeorderMATLinesActivity)cAppExtension.activity;
            intakeorderMATLinesActivity.pSetToolBarTitleWithCounters("(" + cText.pIntToStringStr(this.localIntakeorderMATLinesObl.size())  + "/" + cText.pIntToStringStr(cIntakeorder.currentIntakeOrder.linesMATObl().size()) + ") " + cAppExtension.activity.getString(R.string.lines) + " " + cAppExtension.activity.getString(R.string.shown) );
        }

        notifyDataSetChanged();
    }

    //End Region Public Methods

    //Region Private Methods

    //End Region Private Methods

}
