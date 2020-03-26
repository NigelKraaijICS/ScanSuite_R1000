package SSU_WHS.Receive.ReceiveSummaryLine;

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
import nl.icsvertex.scansuite.Activities.Receive.ReceiveLinesActivity;
import nl.icsvertex.scansuite.R;

public class cReceiverorderSummaryLineAdapter extends RecyclerView.Adapter<cReceiverorderSummaryLineAdapter.ReceiverorderLineViewHolder>  {

    //Region Public Properties
    public static class ReceiverorderLineViewHolder extends RecyclerView.ViewHolder{

        private TextView textViewItemNoAndVariantCode;
        private TextView textViewDescription;
        private TextView textViewQuantity;
        private FrameLayout receiveorderItemLinearLayout;
        private ImageView imageSendStatus;

        public RelativeLayout viewBackground;
        public ConstraintLayout viewForeground;

        public ReceiverorderLineViewHolder(View pvItemView) {
            super(pvItemView);

            this.receiveorderItemLinearLayout = pvItemView.findViewById(R.id.receiveorderLineItemSummaryLinearLayout);

            this.textViewItemNoAndVariantCode = pvItemView.findViewById(R.id.textViewItemNoAndVariant);
            this.textViewItemNoAndVariantCode.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            this.textViewItemNoAndVariantCode.setSingleLine(true);
            this.textViewItemNoAndVariantCode.setMarqueeRepeatLimit(5);
            this.textViewItemNoAndVariantCode.setSelected(true);

            this.textViewDescription = pvItemView.findViewById(R.id.textViewDescription);
            this.textViewDescription.setSingleLine(false);

            this.viewBackground = pvItemView.findViewById(R.id.view_background);
            this.viewForeground = pvItemView.findViewById(R.id.view_foreground);

            this.textViewQuantity = pvItemView.findViewById(R.id.textViewQuantity);
            this.imageSendStatus = pvItemView.findViewById(R.id.imageSendStatus);

        }
    }
    //End Region Public Properties

    //Region Private Properties
    private final LayoutInflater LayoutInflaterObject;
    private List<cReceiveorderSummaryLine> localReceiveorderSummaryLinesObl;
    //End Region Private Propertiess

    private List<FrameLayout> rceiveorderLineItemFrameLayout = new ArrayList<>();


    //Region Constructor
    public cReceiverorderSummaryLineAdapter() {
        this.LayoutInflaterObject = LayoutInflater.from(cAppExtension.context);

    }
    // End Region Constructor

    //Region Default Methods

    @NonNull
    @Override
    public cReceiverorderSummaryLineAdapter.ReceiverorderLineViewHolder onCreateViewHolder(@NonNull ViewGroup pvParent, int pvViewTypeInt) {
        View itemView = this.LayoutInflaterObject.inflate(R.layout.recycler_receiveorderline_summary, pvParent, false);
        return new ReceiverorderLineViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final cReceiverorderSummaryLineAdapter.ReceiverorderLineViewHolder pvHolder, final int pvPositionInt) {

        this.rceiveorderLineItemFrameLayout.add(pvHolder.receiveorderItemLinearLayout);

        if (this.localReceiveorderSummaryLinesObl == null || this.localReceiveorderSummaryLinesObl.size() == 0 ) {
            return;
        }

        final cReceiveorderSummaryLine currentReceiveorderSummaryLine = this.localReceiveorderSummaryLinesObl.get(pvPositionInt);

        //Set fields
        String itemNoAndVariantCodeStr = currentReceiveorderSummaryLine.getItemNoStr();
        if (!currentReceiveorderSummaryLine.getVariantCodeStr().isEmpty()) {
            itemNoAndVariantCodeStr += "~" + currentReceiveorderSummaryLine.getVariantCodeStr() ;
        }

        String lineDescriptionStr = currentReceiveorderSummaryLine.getDescriptionStr();
        if (!currentReceiveorderSummaryLine.getDescription2Str().isEmpty()) {
            lineDescriptionStr += "~" + currentReceiveorderSummaryLine.getDescription2Str() ;
        }

        if (lineDescriptionStr.equalsIgnoreCase("UNKNOWN")) {
            lineDescriptionStr = currentReceiveorderSummaryLine.getDescription2Str();
        }

        String quantityToShowStr;

        if (!cIntakeorder.currentIntakeOrder.isGenerated()) {
            quantityToShowStr  = cText.pDoubleToStringStr(currentReceiveorderSummaryLine.getQuantityHandledDbl())  + "/" + cText.pDoubleToStringStr(currentReceiveorderSummaryLine.getQuantityDbl());
        }
        else
            {
                quantityToShowStr  = cText.pDoubleToStringStr(currentReceiveorderSummaryLine.getQuantityHandledDbl());
            }

        //Set description and quantity
        pvHolder.textViewItemNoAndVariantCode.setText(itemNoAndVariantCodeStr);
        pvHolder.textViewDescription.setText(lineDescriptionStr);
        pvHolder.textViewQuantity.setText(quantityToShowStr);

        pvHolder.imageSendStatus.setVisibility(View.INVISIBLE);

        //Start On Click Listener
        pvHolder.receiveorderItemLinearLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View pvView) {

                //deselect all
                for (FrameLayout frameLayout : rceiveorderLineItemFrameLayout) {
                    frameLayout.setSelected(false);
                }

                //select current
                pvView.setSelected(true);

                //Kick off correct event at correct activity
                if (cAppExtension.context instanceof ReceiveLinesActivity) {
                    ReceiveLinesActivity receiveLinesActivity = (ReceiveLinesActivity)cAppExtension.activity;
                    receiveLinesActivity.pReceivelineSelected(currentReceiveorderSummaryLine);
                }
            }
        });

        //End On Click Listener

        //Select the first one, or selected index
        if (pvPositionInt == 0 && cAppExtension.activity instanceof ReceiveLinesActivity ) {
            pvHolder.receiveorderItemLinearLayout.performClick();
        }
    }

    @Override
    public int getItemCount () {
        if (this.localReceiveorderSummaryLinesObl != null)
            return this.localReceiveorderSummaryLinesObl.size();
        else return 0;
    }

    //End Region Default Methods

    //Region Public Methods
    public void pFillData(List<cReceiveorderSummaryLine> pvDataObl) {
        this.localReceiveorderSummaryLinesObl = pvDataObl;

        if (cAppExtension.activity instanceof  ReceiveLinesActivity) {
            ReceiveLinesActivity receiveLinesActivity = (ReceiveLinesActivity)cAppExtension.activity;
            receiveLinesActivity.pSetToolBarTitleWithCounters("(" + cText.pIntToStringStr(this.localReceiveorderSummaryLinesObl.size())  + "/" + cText.pIntToStringStr(cIntakeorder.currentIntakeOrder.summaryReceiveLinesObl().size()) + ") " + cAppExtension.activity.getString(R.string.lines) + " " + cAppExtension.activity.getString(R.string.shown) );
        }

    }

    public void pShowDeviations() {

        this.localReceiveorderSummaryLinesObl = mGetDeviationsListObl();

        if (cAppExtension.activity instanceof  ReceiveLinesActivity) {
            ReceiveLinesActivity receiveLinesActivity = (ReceiveLinesActivity) cAppExtension.activity;
            receiveLinesActivity.pSetToolBarTitleWithCounters("(" + cText.pIntToStringStr(this.localReceiveorderSummaryLinesObl.size())  + "/" + cText.pIntToStringStr(cIntakeorder.currentIntakeOrder.summaryReceiveLinesObl().size()) + ") " + cAppExtension.activity.getString(R.string.lines) + " " + cAppExtension.activity.getString(R.string.shown) );
            receiveLinesActivity.pShowData(this.localReceiveorderSummaryLinesObl);
        }

    }

    public void pSetFilter(String pvQueryTextStr, Boolean pvScannedBln) {
        this.localReceiveorderSummaryLinesObl = mGetFilteredListObl(pvQueryTextStr);

        if (cAppExtension.activity instanceof ReceiveLinesActivity ) {
            ReceiveLinesActivity receiveLinesActivity = (ReceiveLinesActivity) cAppExtension.activity;
            receiveLinesActivity.pSetToolBarTitleWithCounters("(" + cText.pIntToStringStr(this.localReceiveorderSummaryLinesObl.size())  + "/" + cText.pIntToStringStr(cIntakeorder.currentIntakeOrder.summaryReceiveLinesObl().size()) + ") " + cAppExtension.activity.getString(R.string.lines) + " " + cAppExtension.activity.getString(R.string.shown) );
        }


        if (this.localReceiveorderSummaryLinesObl.size() == 1 && pvScannedBln) {

            if (cAppExtension.activity instanceof  ReceiveLinesActivity) {
                ReceiveLinesActivity receiveLinesActivity = (ReceiveLinesActivity) cAppExtension.activity;
                receiveLinesActivity.pReceivelineSelected(this.localReceiveorderSummaryLinesObl.get(0));
                receiveLinesActivity.pStartLine();
            }

        }

    }

    //End Region Public Methods

    //Region Private Methods

    private List<cReceiveorderSummaryLine> mGetFilteredListObl(String pvQueryTextStr) {
        List<cReceiveorderSummaryLine> resultObl = new ArrayList<>();

        StringBuilder variantCodeStr = new StringBuilder();
        int loopInt = 0;

        if (this.localReceiveorderSummaryLinesObl == null || this.localReceiveorderSummaryLinesObl.size() == 0) {
            return resultObl;
        }

        if (pvQueryTextStr.isEmpty()) {
            return cIntakeorder.currentIntakeOrder.summaryReceiveLinesObl();
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

        for (cReceiveorderSummaryLine receiveorderSummaryLine :this.localReceiveorderSummaryLinesObl){

            if (!receiveorderSummaryLine.getItemNoStr().equalsIgnoreCase(fieldsObl[0])) {
                continue;
            }

            if (variantCodeStr.length() > 0) {
                if (!receiveorderSummaryLine.getVariantCodeStr().equalsIgnoreCase(variantCodeStr.toString())) {
                    continue;
                }
            }

            resultObl.add(receiveorderSummaryLine);

        }

        return resultObl;
    }

    private List<cReceiveorderSummaryLine> mGetDeviationsListObl() {

        List<cReceiveorderSummaryLine> resultObl = new ArrayList<>();
        for (cReceiveorderSummaryLine receiveorderSummaryLine : cReceiveorderSummaryLine.allReceiveorderSummaryLinesObl)
        {
            if (receiveorderSummaryLine.getQuantityHandledDbl() > receiveorderSummaryLine.getQuantityDbl() ||
                    receiveorderSummaryLine.getQuantityHandledDbl() < receiveorderSummaryLine.getQuantityDbl())
            {
                resultObl.add(receiveorderSummaryLine);
            }
        }
        return resultObl;
    }

    //End Region Private Methods

}
