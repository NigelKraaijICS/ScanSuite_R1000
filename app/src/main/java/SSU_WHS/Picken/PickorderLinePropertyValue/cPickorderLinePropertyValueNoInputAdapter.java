package SSU_WHS.Picken.PickorderLinePropertyValue;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import nl.icsvertex.scansuite.R;

public class cPickorderLinePropertyValueNoInputAdapter extends RecyclerView.Adapter<cPickorderLinePropertyValueNoInputAdapter.ItempropertyValueNoInputViewHolder>{

    public class ItempropertyValueNoInputViewHolder extends RecyclerView.ViewHolder{

        private TextView itemPropertyTextView;
        private TextView itemPropertyValueTextView;
        public LinearLayout itemPropertyValueNoInputItemLinearLayout;

        public ItempropertyValueNoInputViewHolder(View itemView) {
            super(itemView);
            this.itemPropertyTextView = itemView.findViewById(R.id.itemPropertyTextView);
            this.itemPropertyValueTextView = itemView.findViewById(R.id.itemPropertyValueTextView);
            this.itemPropertyValueNoInputItemLinearLayout = itemView.findViewById(R.id.itemPropertyValueNoInputItemLinearLayout);
        }
    }

    //Region Private Properties
    private final LayoutInflater layoutInflaterObject;
    private List<cPickorderLinePropertyValue> localItemPropertyValueObl;
    //End Region Private Properties

    //Region Constructor
    public cPickorderLinePropertyValueNoInputAdapter() {
        this.layoutInflaterObject = LayoutInflater.from(cAppExtension.context);
    }
    //End Region Constructor

    @NonNull
    @Override
    public ItempropertyValueNoInputViewHolder onCreateViewHolder(@NonNull ViewGroup pvParentViewGroup, int pvViewTypeInt) {
        View itemView = this.layoutInflaterObject.inflate(R.layout.recycler_itempropertyvalue_noinput, pvParentViewGroup, false);
        return new ItempropertyValueNoInputViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItempropertyValueNoInputViewHolder pvHolderObj, int pvPositionInt) {

        if (this.localItemPropertyValueObl != null || this.localItemPropertyValueObl.size() > 0) {
            final cPickorderLinePropertyValue pickorderLinePropertyValue = this.localItemPropertyValueObl.get(pvPositionInt);

            pvHolderObj.itemPropertyTextView.setText(pickorderLinePropertyValue.getItemProperty().getOmschrijvingStr());
            pvHolderObj.itemPropertyValueTextView.setText(pickorderLinePropertyValue.getValueStr());

            pvHolderObj.itemPropertyValueTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Animation animation = AnimationUtils.loadAnimation(cAppExtension.context.getApplicationContext(), R.anim.bounce);
                    ICS.Utils.cBounceInterpolator interpolator = new ICS.Utils.cBounceInterpolator(0.1,15);
                    animation.setInterpolator(interpolator);
                    v.startAnimation(animation);
                    cUserInterface.pPlaySound(R.raw.message, 0);
                    }
            });
        }
    }

    public void pFillData(List<cPickorderLinePropertyValue> pvDataObl) {
        this.localItemPropertyValueObl = pvDataObl;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount () {
        if (this.localItemPropertyValueObl != null)
            return this.localItemPropertyValueObl.size();
        else return 0;
    }
}
