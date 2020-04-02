package SSU_WHS.Intake.Intakeorders;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ICS.Utils.cProductFlavor;
import ICS.cAppExtension;
import SSU_WHS.Basics.Users.cUser;
import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import nl.icsvertex.scansuite.Activities.IntakeAndReceive.IntakeAndReceiveSelectActivity;
import nl.icsvertex.scansuite.BuildConfig;
import nl.icsvertex.scansuite.R;

public class cIntakeorderAdapter extends RecyclerView.Adapter<cIntakeorderAdapter.IntakeorderViewHolder>  {

    //Region Public Properties

    public static class IntakeorderViewHolder extends RecyclerView.ViewHolder{


        private TextView textViewOrdernumber;
        private TextView textViewOrderUser;
        private TextView textViewOrdertype;
        private TextView  textViewExternalreference;
        private ImageView imageViewIsProcessedOrWait;
        public LinearLayout intakeorderItemLinearLayout;

        public IntakeorderViewHolder(View pvItemView) {
            super(pvItemView);

            this.textViewOrderUser = pvItemView.findViewById(R.id.textViewOrderUser);
            this.textViewOrderUser.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            this.textViewOrderUser.setSingleLine(true);
            this.textViewOrderUser.setMarqueeRepeatLimit(5);
            this.textViewOrderUser.setSelected(true);
            this.textViewOrdernumber = pvItemView.findViewById(R.id.textViewOrdernumber);

            this.textViewOrdernumber.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            this.textViewOrdernumber.setSingleLine(true);
            this.textViewOrdernumber.setMarqueeRepeatLimit(5);
            this.textViewOrdernumber.setSelected(true);
            this.textViewExternalreference = pvItemView.findViewById(R.id.textViewExternalreference);
            this.textViewOrdertype = pvItemView.findViewById(R.id.textViewOrdertype);
            this.intakeorderItemLinearLayout = pvItemView.findViewById(R.id.intakeorderItemLinearLayout);
            this.imageViewIsProcessedOrWait = pvItemView.findViewById(R.id.imageViewIsProcessedOrWait);

        }
        //End Region Public Properties
    }

    //Region Private Properties
    private final LayoutInflater LayoutInflaterObject;
    private List<cIntakeorder> localIntakeorderObl;

    //End Region Private Properties

    //Region Constructor
    public cIntakeorderAdapter() {
        this.LayoutInflaterObject = LayoutInflater.from(cAppExtension.context);
    }
    //End Region Constructor


    //Region Default Methods
    @NonNull
    @Override
    public cIntakeorderAdapter.IntakeorderViewHolder onCreateViewHolder(@NonNull ViewGroup pvParent, int pbViewTypeInt) {
        View itemView = LayoutInflaterObject.inflate(R.layout.recycler_intakeorder, pvParent, false);
        return new IntakeorderViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull cIntakeorderAdapter.IntakeorderViewHolder pvHolder, int pvPositionInt) {

        if (localIntakeorderObl == null || localIntakeorderObl.size() == 0 ) {
            return;
        }

        final cIntakeorder  selectedIntakeorder = localIntakeorderObl.get(pvPositionInt);


        if (selectedIntakeorder.getStatusInt() == 10 ) {
            pvHolder.textViewOrderUser.setText(cUser.pUserToShowStr(selectedIntakeorder.getAssignedUserIdStr()));
        }
        else {
            pvHolder.textViewOrderUser.setText(cUser.pUserToShowStr(selectedIntakeorder.getCurrentUserIdStr()));
        }

        if (selectedIntakeorder.getProcessingOrParkedBln()) {
            pvHolder.imageViewIsProcessedOrWait.setVisibility(View.VISIBLE);
        }
        else {
            pvHolder.imageViewIsProcessedOrWait.setVisibility(View.INVISIBLE);
        }

        pvHolder.textViewOrdernumber.setText(selectedIntakeorder.getOrderNumberStr());
        pvHolder.textViewOrdernumber.setTag(selectedIntakeorder.getOrderNumberStr());

        if (BuildConfig.FLAVOR.equalsIgnoreCase(cProductFlavor.FlavorEnu.BMN.toString())) {
            pvHolder.textViewOrdernumber.setText(selectedIntakeorder.getDocumentStr());
            pvHolder.textViewOrdernumber.setTag(selectedIntakeorder.getDocumentStr());
        }

        pvHolder.textViewExternalreference.setText(selectedIntakeorder.getExternalReferenceStr());


        pvHolder.textViewOrdertype.setText(cWarehouseorder.getWorkflowDescription(selectedIntakeorder.getOrderTypeStr()));

        pvHolder.intakeorderItemLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (cAppExtension.context instanceof IntakeAndReceiveSelectActivity) {
                    IntakeAndReceiveSelectActivity intakeAndReceiveSelectActivity = (IntakeAndReceiveSelectActivity)cAppExtension.activity;
                    intakeAndReceiveSelectActivity.pIntakeorderSelected(selectedIntakeorder);
                }
                            }
        });
    }

    @Override
    public int getItemCount () {
        if (localIntakeorderObl != null)
            return localIntakeorderObl.size();
        else return 0;
    }

    //End Region Default Methods

    //Region Public Methods
    public void pFillData(List<cIntakeorder> pvDataObl) {
        localIntakeorderObl = pvDataObl;
        notifyDataSetChanged();
    }

    public void pSetFilter(String pvQueryTextStr) {
        localIntakeorderObl = this.mGetFilteredListObl(pvQueryTextStr);
        notifyDataSetChanged();
    }

    //End Region Public Methods


    //Region Private Methods
    private List<cIntakeorder> mGetFilteredListObl(String pvQueryTextStr) {

        pvQueryTextStr = pvQueryTextStr.toLowerCase();
        List<cIntakeorder> resultObl = new ArrayList<>();

        if (localIntakeorderObl == null || localIntakeorderObl.size() == 0) {
            return resultObl;
        }

        for (cIntakeorder intakeorder:localIntakeorderObl)
        {
            if (intakeorder.getOrderNumberStr().toLowerCase().contains(pvQueryTextStr) || intakeorder.getExternalReferenceStr().toLowerCase().contains(pvQueryTextStr))
            {
                resultObl.add(intakeorder);
            }
        }
        return resultObl;
    }

    //End Region Private Methods


}
