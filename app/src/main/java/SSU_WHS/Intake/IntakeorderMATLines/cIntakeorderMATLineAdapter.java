package SSU_WHS.Intake.IntakeorderMATLines;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ICS.Utils.cText;
import ICS.cAppExtension;
import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.Intake.Intakeorders.cIntakeorder;
import nl.icsvertex.scansuite.Activities.intake.IntakeorderLinesActivity;
import nl.icsvertex.scansuite.Fragments.intake.IntakeorderLinesToDoFragment;
import nl.icsvertex.scansuite.R;

public class cIntakeorderMATLineAdapter extends RecyclerView.Adapter<cIntakeorderMATLineAdapter.IntakeorderMATLineViewHolder>  {

    //Region Public Properties
    public class IntakeorderMATLineViewHolder extends RecyclerView.ViewHolder{

        private TextView textViewBinCode;
        private TextView textViewLine;
        private TextView textViewQuantity;
        private TextView textViewSourceNo;
        private LinearLayout intakeOrderMATItemLinearLayout;

        public IntakeorderMATLineViewHolder(View pvItemView) {
            super(pvItemView);

            this.intakeOrderMATItemLinearLayout = pvItemView.findViewById(R.id.intakeorderLineItemLinearLayout);

            this.textViewLine = pvItemView.findViewById(R.id.textViewLine);
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

            this.textViewQuantity = pvItemView.findViewById(R.id.textViewQuantityLabel);

        }
    }
    //End Region Public Properties

    //Region Private Properties
    private final LayoutInflater LayoutInflaterObject;
    private List<cIntakeorderMATLine> localIntakeorderMATLinesObl;
    //End Region Private Propertiess

    private List<LinearLayout> intakeorderLineItemLinearLayout = new ArrayList<>();
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

        this.intakeorderLineItemLinearLayout.add(pvHolder.intakeOrderMATItemLinearLayout);

        if (this.localIntakeorderMATLinesObl == null || this.localIntakeorderMATLinesObl.size() == 0 ) {
            return;
        }

        final cIntakeorderMATLine currentIntakeorderMatLine = this.localIntakeorderMATLinesObl.get(pvPositionInt);

        String lineDescriptionStr = currentIntakeorderMatLine.getItemNoStr() + "~" + currentIntakeorderMatLine.getVariantCodeStr() + ": " + currentIntakeorderMatLine.getDescriptionStr();
        String quantityToShowStr = "";
        String binCodeStr = "";
        String sourceNoStr = currentIntakeorderMatLine.sourceNoStr;

        if (RecyclerView.getId() == R.id.recyclerViewIntakeLinesToDo) {
             quantityToShowStr  = currentIntakeorderMatLine.getQuantityHandledDbl().intValue() + "/" + currentIntakeorderMatLine.getQuantityDbl().intValue();
             binCodeStr = currentIntakeorderMatLine.getBinCodeStr();
        }

        if (RecyclerView.getId() == R.id.recyclerViewIntakeLinesDone) {
            quantityToShowStr  = currentIntakeorderMatLine.getQuantityHandledDbl().intValue() + "/" + currentIntakeorderMatLine.getQuantityDbl().intValue();
            binCodeStr = currentIntakeorderMatLine.getBinCodeHandledStr();

            if (currentIntakeorderMatLine.getLocalStatusInt() == cWarehouseorder.IntakeMATLineLocalStatusEnu.LOCALSTATUS_DONE_NOTSENT || (currentIntakeorderMatLine.getLocalStatusInt() == cWarehouseorder.IntakeMATLineLocalStatusEnu.LOCALSTATUS_DONE_ERROR_SENDING)) {
                pvHolder.textViewBinCode.setTextColor(cAppExtension.activity.getResources().getColor(R.color.colorAccent));
                pvHolder.textViewLine.setTextColor(cAppExtension.activity.getResources().getColor(R.color.colorAccent));
                pvHolder.textViewQuantity.setTextColor(cAppExtension.activity.getResources().getColor(R.color.colorAccent));
                pvHolder.textViewSourceNo.setTextColor(cAppExtension.activity.getResources().getColor(R.color.colorAccent));
            }
        }

        if (RecyclerView.getId() == R.id.recyclerViewIntakeorderLinesTotal) {
           quantityToShowStr  = cText.pIntToStringStr( currentIntakeorderMatLine.getQuantityDbl().intValue());


           if (!currentIntakeorderMatLine.getBinCodeHandledStr().isEmpty()) {
               binCodeStr = currentIntakeorderMatLine.getBinCodeStr() + "/" + currentIntakeorderMatLine.getBinCodeHandledStr();
           } else {
               binCodeStr = currentIntakeorderMatLine.getBinCodeStr();
           }


            if (currentIntakeorderMatLine.getLocalStatusInt() == cWarehouseorder.IntakeMATLineLocalStatusEnu.LOCALSTATUS_DONE_SENT) {
                pvHolder.textViewBinCode.setTextColor(cAppExtension.activity.getResources().getColor(R.color.colorGreen));
                pvHolder.textViewLine.setTextColor(cAppExtension.activity.getResources().getColor(R.color.colorGreen));
                pvHolder.textViewQuantity.setTextColor(cAppExtension.activity.getResources().getColor(R.color.colorGreen));
                pvHolder.textViewSourceNo.setTextColor(cAppExtension.activity.getResources().getColor(R.color.colorGreen));
            }

            if (currentIntakeorderMatLine.getLocalStatusInt() == cWarehouseorder.IntakeMATLineLocalStatusEnu.LOCALSTATUS_DONE_NOTSENT || (currentIntakeorderMatLine.getLocalStatusInt() == cWarehouseorder.IntakeMATLineLocalStatusEnu.LOCALSTATUS_DONE_ERROR_SENDING)) {
                pvHolder.textViewBinCode.setTextColor(cAppExtension.activity.getResources().getColor(R.color.colorAccent));
                pvHolder.textViewLine.setTextColor(cAppExtension.activity.getResources().getColor(R.color.colorAccent));
                pvHolder.textViewQuantity.setTextColor(cAppExtension.activity.getResources().getColor(R.color.colorAccent));
                pvHolder.textViewSourceNo.setTextColor(cAppExtension.activity.getResources().getColor(R.color.colorAccent));
            }

        }

        //Set description and quantity
        pvHolder.textViewLine.setText(lineDescriptionStr);
        pvHolder.textViewQuantity.setText(quantityToShowStr);
        pvHolder.textViewBinCode.setText(binCodeStr);
        pvHolder.textViewSourceNo.setText(sourceNoStr);

        //Start On Click Listener
        pvHolder.intakeOrderMATItemLinearLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View pvView) {

                //deselect all
                for (LinearLayout linearLayout : intakeorderLineItemLinearLayout) {
                    linearLayout.setSelected(false);
                }

                //select current
                pvView.setSelected(true);

                //Kick off correct event at correct activity
                if (cAppExtension.context instanceof IntakeorderLinesActivity) {

                    if (RecyclerView.getId() == R.id.recyclerViewIntakeLinesToDo) {
                        IntakeorderLinesActivity.pIntakelineSelected(currentIntakeorderMatLine);
                    }

                    if (RecyclerView.getId() == R.id.recyclerViewIntakeLinesDone) {
                        IntakeorderLinesActivity.pIntakelineToResetSelected(currentIntakeorderMatLine);
                    }
                }
            }
        });
        //End On Click Listener

        if (cIntakeorder.currentIntakeOrder.lastSelectedIndexInt > this.localIntakeorderMATLinesObl.size() -1 ) {
            cIntakeorder.currentIntakeOrder.lastSelectedIndexInt = 0;
        }

        //Select the first one, or selected index
        if (pvPositionInt == cIntakeorder.currentIntakeOrder.lastSelectedIndexInt && cAppExtension.activity instanceof  IntakeorderLinesActivity &&  RecyclerView.getId() == R.id.recyclerViewIntakeLinesToDo) {
            pvHolder.intakeOrderMATItemLinearLayout.performClick();
            IntakeorderLinesToDoFragment.pSetSelectedIndexInt(pvPositionInt);
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
    }

    public void pSetFilter(String pvQueryTextStr) {
        this.localIntakeorderMATLinesObl = mGetFilteredListObl(pvQueryTextStr);
        notifyDataSetChanged();
    }

    //End Region Public Methods

    //Region Private Methods

    private List<cIntakeorderMATLine> mGetFilteredListObl(String pvQueryTextStr) {
        pvQueryTextStr = pvQueryTextStr.toLowerCase();
        List<cIntakeorderMATLine> resultObl = new ArrayList<>();

        if (this.localIntakeorderMATLinesObl == null || this.localIntakeorderMATLinesObl.size() == 0) {
            return resultObl;
        }

        for (cIntakeorderMATLine intakeorderMATLine :this.localIntakeorderMATLinesObl)
        {
            if (intakeorderMATLine.getBinCodeStr().toLowerCase().contains(pvQueryTextStr) || intakeorderMATLine.getItemNoStr().toLowerCase().contains(pvQueryTextStr) || intakeorderMATLine.getDescriptionStr().toLowerCase().contains(pvQueryTextStr)|| intakeorderMATLine.getSourceNoStr().contains(pvQueryTextStr))
            {
                resultObl.add(intakeorderMATLine);
            }
        }
        return resultObl;
    }

    //End Region Private Methods






}
