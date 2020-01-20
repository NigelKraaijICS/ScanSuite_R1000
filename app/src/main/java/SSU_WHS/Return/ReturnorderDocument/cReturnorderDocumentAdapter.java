package SSU_WHS.Return.ReturnorderDocument;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ICS.cAppExtension;
import nl.icsvertex.scansuite.Activities.Returns.ReturnorderDocumentsActivity;
import nl.icsvertex.scansuite.Fragments.Returns.ReturnDocumentsDoneFragment;
import nl.icsvertex.scansuite.Fragments.Returns.ReturnDocumentsToDoFragment;
import nl.icsvertex.scansuite.Fragments.Returns.ReturnDocumentsTotalFragment;
import nl.icsvertex.scansuite.R;

public class cReturnorderDocumentAdapter extends RecyclerView.Adapter<cReturnorderDocumentAdapter.ReturnorderDocumentViewHolder> {
    //Region Public Properties

    public class ReturnorderDocumentViewHolder extends RecyclerView.ViewHolder{

        private TextView textViewDocument;
        private TextView textViewCounted;
        private TextView textViewLines;
        private ImageView imageDocument;
        private ImageView imageChevronDown;

        public RelativeLayout viewBackground;
        public ConstraintLayout viewForeground;

        public ReturnorderDocumentViewHolder(View pvItemView) {
            super(pvItemView);
            this.textViewDocument = pvItemView.findViewById(R.id.textViewDocument);
            this.textViewDocument.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            this.textViewDocument.setSingleLine(true);
            this.textViewDocument.setMarqueeRepeatLimit(5);
            this.textViewDocument.setSelected(true);
            this.textViewCounted = pvItemView.findViewById(R.id.textViewCounted);
            this.textViewLines = pvItemView.findViewById(R.id.textViewLines);
            this.imageDocument = pvItemView.findViewById(R.id.imageDocument);
            this.imageChevronDown  = pvItemView.findViewById(R.id.imageChevronDown);
            this.viewBackground = pvItemView.findViewById(R.id.view_background);
            this.viewForeground = pvItemView.findViewById(R.id.view_foreground);
        }
        //End Region Public Properties
    }

    //Region Private Properties
    private final LayoutInflater LayoutInflaterObject;
    private List<cReturnorderDocument> localReturnorderDocumentObl;
    private static RecyclerView thisRecyclerView;

    //End Region Private Properties

    //Region Constructor
    public cReturnorderDocumentAdapter() {
        this.LayoutInflaterObject = LayoutInflater.from(cAppExtension.context);
    }
    //End Region Constructor


    //Region Default Methods
    @NonNull
    @Override
    public ReturnorderDocumentViewHolder onCreateViewHolder(@NonNull ViewGroup pvParent, int pbViewTypeInt) {
        View itemView = LayoutInflaterObject.inflate(R.layout.recycler_returnorderdocument, pvParent, false);
        return new ReturnorderDocumentViewHolder(itemView);
    }
    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView pvRecyclerView) {
        cReturnorderDocumentAdapter.thisRecyclerView = pvRecyclerView;
        super.onAttachedToRecyclerView( cReturnorderDocumentAdapter.thisRecyclerView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReturnorderDocumentViewHolder pvHolder, int pvPositionInt) {

        if (localReturnorderDocumentObl == null || localReturnorderDocumentObl.size() == 0 ) {
            return;
        }

        final cReturnorderDocument selectedReturnorderDocument = localReturnorderDocumentObl.get(pvPositionInt);


        pvHolder.textViewDocument.setText( selectedReturnorderDocument.getSourceDocumentStr());
        pvHolder.textViewDocument.setTag( selectedReturnorderDocument.getSourceDocumentStr());
        String imageBinUniqueTag =  selectedReturnorderDocument.getSourceDocumentStr() + "_IMG";
        pvHolder.imageDocument.setTag(imageBinUniqueTag);
        pvHolder.textViewCounted.setText(selectedReturnorderDocument.getLineCounterStr());
        pvHolder.textViewLines.setText(selectedReturnorderDocument.getItemCounterStr());


        if  (ReturnorderDocumentsActivity.currentDocumentFragment instanceof ReturnDocumentsToDoFragment) {
            pvHolder.imageChevronDown.setVisibility(View.VISIBLE);
        }

        if  (ReturnorderDocumentsActivity.currentDocumentFragment instanceof ReturnDocumentsDoneFragment) {
            pvHolder.imageChevronDown.setVisibility(View.INVISIBLE);
        }

        if  (ReturnorderDocumentsActivity.currentDocumentFragment instanceof ReturnDocumentsTotalFragment) {
            pvHolder.imageChevronDown.setVisibility(View.INVISIBLE);
        }


        pvHolder.viewForeground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cReturnorderDocument.currentReturnOrderDocument = selectedReturnorderDocument;
                ReturnorderDocumentsActivity.pReturnorderDocumentSelected();
            }
        });
    }

    @Override
    public int getItemCount () {
        if (localReturnorderDocumentObl != null)
            return localReturnorderDocumentObl.size();
        else return 0;
    }

    //End Region Default Methods

    //Region Public Methods
    public void pFillData(List<cReturnorderDocument> pvDataObl) {
        localReturnorderDocumentObl = pvDataObl;
        notifyDataSetChanged();
    }

    public void pSetFilter(String pvQueryTextStr) {
        localReturnorderDocumentObl = this.mGetFilteredListObl(pvQueryTextStr);
        notifyDataSetChanged();
    }

    //End Region Public Methods


    //Region Private Methods
    private List<cReturnorderDocument> mGetFilteredListObl(String pvQueryTextStr) {

        pvQueryTextStr = pvQueryTextStr.toLowerCase();
        List<cReturnorderDocument> resultObl = new ArrayList<>();

        if (localReturnorderDocumentObl == null || localReturnorderDocumentObl.size() == 0) {
            return resultObl;
        }

        for (cReturnorderDocument returnorderDocument: localReturnorderDocumentObl)
        {
            if (returnorderDocument.getSourceDocumentStr().toLowerCase().contains(pvQueryTextStr))
            {
                resultObl.add(returnorderDocument);
            }
        }
        return resultObl;
    }

    //End Region Private Methods
}
