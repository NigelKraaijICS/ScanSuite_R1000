package SSU_WHS.Receive.ReceiveLines;

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
import nl.icsvertex.scansuite.Activities.Intake.IntakeorderLinesActivity;
import nl.icsvertex.scansuite.R;

public class cReceiveorderLineAdapter extends RecyclerView.Adapter<cReceiveorderLineAdapter.ReceiveorderLineViewHolder>  {

    //Region Public Properties
    public class ReceiveorderLineViewHolder extends RecyclerView.ViewHolder{

        private TextView textViewTime;
        private TextView textViewQuantity;
        private FrameLayout receiverorderItemFrameLayout;

        public RelativeLayout viewBackground;
        public ConstraintLayout viewForeground;

        public ReceiveorderLineViewHolder(View pvItemView) {
            super(pvItemView);

            this.receiverorderItemFrameLayout = pvItemView.findViewById(R.id.receiveorderLineItemLinearLayout);

            this.textViewTime = pvItemView.findViewById(R.id.timeText);
            this.textViewTime.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            this.textViewTime.setSingleLine(true);
            this.textViewTime.setMarqueeRepeatLimit(5);
            this.textViewTime.setSelected(true);
            this.viewBackground = pvItemView.findViewById(R.id.view_background);
            this.viewForeground = pvItemView.findViewById(R.id.view_foreground);
            this.textViewQuantity = pvItemView.findViewById(R.id.storedQuantityText);

        }
    }
    //End Region Public Properties

    //Region Private Properties
    private final LayoutInflater LayoutInflaterObject;
    private List<cReceiveorderLine> localReceiveorderline;
    //End Region Private Propertiess

    private List<FrameLayout> receiverOrderLineItemFrameLayout = new ArrayList<>();
    private RecyclerView RecyclerView;

    //Region Constructor
    public cReceiveorderLineAdapter() {
        this.LayoutInflaterObject = LayoutInflater.from(cAppExtension.context);
    }
    // End Region Constructor

    //Region Default Methods

    @NonNull
    @Override
    public  cReceiveorderLineAdapter.ReceiveorderLineViewHolder onCreateViewHolder(@NonNull ViewGroup pvParent, int pvViewTypeInt) {
        View itemView = this.LayoutInflaterObject.inflate(R.layout.recycler_receiveline, pvParent, false);
        return new cReceiveorderLineAdapter.ReceiveorderLineViewHolder(itemView);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView pvRecyclerView) {
        this.RecyclerView = pvRecyclerView;
        super.onAttachedToRecyclerView( this.RecyclerView);
    }

    @Override
    public void onBindViewHolder(final cReceiveorderLineAdapter.ReceiveorderLineViewHolder pvHolder, final int pvPositionInt) {

        this.receiverOrderLineItemFrameLayout.add(pvHolder.receiverorderItemFrameLayout);

        if (this.localReceiveorderline == null || this.localReceiveorderline.size() == 0 ) {
            return;
        }

        final cReceiveorderLine currentReceiveorderLine = this.localReceiveorderline.get(pvPositionInt);

        //Set fields
        String quantityToShowStr = cText.pDoubleToStringStr(currentReceiveorderLine.getQuantityHandledDbl());

        String[] splited = currentReceiveorderLine.getHandledTimeStampStr().split("T");
        String timeStr = splited[1].substring(0,5);

        //Set description and quantity
        pvHolder.textViewQuantity.setText(quantityToShowStr);
        pvHolder.textViewTime.setText(timeStr);

        //Start On Click Listener
        pvHolder.receiverorderItemFrameLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View pvView) {

                //deselect all
                for (FrameLayout frameLayout : receiverOrderLineItemFrameLayout) {
                    frameLayout.setSelected(false);
                }

                //select current
                //pvView.setSelected(true);

            }
        });

        //End On Click Listener


        //Select the first one, or selected index
        if (pvPositionInt == 0 && cAppExtension.activity instanceof IntakeorderLinesActivity &&  RecyclerView.getId() == R.id.recyclerViewLines) {
            pvHolder.receiverorderItemFrameLayout.performClick();
        }
    }

    @Override
    public int getItemCount () {
        if (this.localReceiveorderline != null)
            return this.localReceiveorderline.size();
        else return 0;
    }

    //End Region Default Methods

    //Region Public Methods
    public void pFillData(List<cReceiveorderLine> pvDataObl ) {
        this.localReceiveorderline = pvDataObl;
        notifyDataSetChanged();
    }

    //End Region Public Methods

    //Region Private Methods

    //End Region Private Methods

}
