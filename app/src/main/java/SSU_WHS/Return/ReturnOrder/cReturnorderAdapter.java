package SSU_WHS.Return.ReturnOrder;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ICS.cAppExtension;
import SSU_WHS.General.cPublicDefinitions;
import nl.icsvertex.scansuite.Activities.Returns.ReturnorderSelectActivity;
import nl.icsvertex.scansuite.R;

public class cReturnorderAdapter extends RecyclerView.Adapter<cReturnorderAdapter.ReturnorderViewHolder>  {

    //Region Public Properties

    public class ReturnorderViewHolder extends RecyclerView.ViewHolder{

        private View viewOrderStatus;
        private TextView textViewOrdernumber;
        private TextView textViewOrderUser;
        private TextView textViewOrdertype;
        private TextView  textViewDocument;
        public LinearLayout returnorderItemLinearLayout;

        public ReturnorderViewHolder(View pvItemView) {
            super(pvItemView);
            this.viewOrderStatus = pvItemView.findViewById(R.id.viewOrderStatus);
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
            this.textViewDocument = pvItemView.findViewById(R.id.textViewDocument);
            this.textViewDocument.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            this.textViewDocument.setSingleLine(true);
            this.textViewDocument.setMarqueeRepeatLimit(5);
            this.textViewDocument.setSelected(true);
            this.textViewOrdertype = pvItemView.findViewById(R.id.textViewOrdertype);
            this.returnorderItemLinearLayout = pvItemView.findViewById(R.id.returnorderItemLinearLayout);

        }
        //End Region Public Properties
    }

    //Region Private Properties
    private final LayoutInflater LayoutInflaterObject;
    private List<cReturnorder> localReturnorderObl;

    //End Region Private Properties

    //Region Constructor
    public cReturnorderAdapter() {
        this.LayoutInflaterObject = LayoutInflater.from(cAppExtension.context);
    }
    //End Region Constructor


    //Region Default Methods
    @NonNull
    @Override
    public ReturnorderViewHolder onCreateViewHolder(@NonNull ViewGroup pvParent, int pbViewTypeInt) {
        View itemView = LayoutInflaterObject.inflate(R.layout.recycler_returnorder, pvParent, false);
        return new ReturnorderViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReturnorderViewHolder pvHolder, int pvPositionInt) {

        if (localReturnorderObl == null || localReturnorderObl.size() == 0 ) {
            return;
        }

        final cReturnorder selectedReturnorder = localReturnorderObl.get(pvPositionInt);

        if (!selectedReturnorder.getAssignedUserIdStr().isEmpty()) {
            pvHolder.viewOrderStatus.setBackgroundResource(R.color.colorOrderStatusAssignedUser);
        }

        if (selectedReturnorder.getStatusInt() == 10 ) {
            pvHolder.textViewOrderUser.setText(selectedReturnorder.getAssignedUserIdStr());
        }
        else {
            pvHolder.textViewOrderUser.setText(selectedReturnorder.getCurrentUserIdStr());
        }

        pvHolder.textViewOrdernumber.setText(selectedReturnorder.getOrderNumberStr());
        pvHolder.textViewOrdernumber.setTag(selectedReturnorder.getOrderNumberStr());
        pvHolder.textViewDocument.setText(selectedReturnorder.getDocumentStr());

        if(selectedReturnorder.getOrderTypeStr().equalsIgnoreCase(cPublicDefinitions.Workflows.RVS.toString())) {
            pvHolder.textViewOrdertype.setText(R.string.ordertype_rvs);
        }


        pvHolder.returnorderItemLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (cAppExtension.context instanceof ReturnorderSelectActivity) {
                    ReturnorderSelectActivity.pReturnorderSelected(selectedReturnorder);
                }
           }
        });
    }

    @Override
    public int getItemCount () {
        if (localReturnorderObl != null)
            return localReturnorderObl.size();
        else return 0;
    }

    //End Region Default Methods

    //Region Public Methods
    public void pFillData(List<cReturnorder> pvDataObl) {
        localReturnorderObl = pvDataObl;
        notifyDataSetChanged();
    }

    public void pSetFilter(String pvQueryTextStr) {
        localReturnorderObl = this.mGetFilteredListObl(pvQueryTextStr);
        notifyDataSetChanged();
    }

    //End Region Public Methods


    //Region Private Methods
    private List<cReturnorder> mGetFilteredListObl(String pvQueryTextStr) {

        pvQueryTextStr = pvQueryTextStr.toLowerCase();
        List<cReturnorder> resultObl = new ArrayList<>();

        if (localReturnorderObl == null || localReturnorderObl.size() == 0) {
            return resultObl;
        }

        for (cReturnorder returnorder: localReturnorderObl)
        {
            if (returnorder.getOrderNumberStr().toLowerCase().contains(pvQueryTextStr) || returnorder.getExternalReferenceStr().toLowerCase().contains(pvQueryTextStr))
            {
                resultObl.add(returnorder);
            }
        }
        return resultObl;
    }

    //End Region Private Methods


}

