package SSU_WHS.LineItemProperty.LinePropertyValue;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import nl.icsvertex.scansuite.R;

public class cLinePropertyValueNoInputAdapter extends RecyclerView.Adapter<cLinePropertyValueNoInputAdapter.ItempropertyValueNoInputViewHolder>{

    public static class ItempropertyValueNoInputViewHolder extends RecyclerView.ViewHolder{

        private final AppCompatImageView imageViewPropertyType;
        private final TextView itemPropertyTextView;
        private final TextView itemPropertyValueTextView;
        public LinearLayout itemPropertyValueNoInputItemLinearLayout;

        public ItempropertyValueNoInputViewHolder(View itemView) {
            super(itemView);
            this.imageViewPropertyType = itemView.findViewById(R.id.imageViewPropertyType);
            this.itemPropertyTextView = itemView.findViewById(R.id.itemPropertyTextView);
            this.itemPropertyValueTextView = itemView.findViewById(R.id.itemPropertyValueTextView);
            this.itemPropertyValueNoInputItemLinearLayout = itemView.findViewById(R.id.itemPropertyValueNoInputItemLinearLayout);
        }
    }

    //Region Private Properties
    private final LayoutInflater layoutInflaterObject;
    private List<cLinePropertyValue> localItemPropertyValueObl;
    //End Region Private Properties

    //Region Constructor
    public cLinePropertyValueNoInputAdapter() {
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
    public void onBindViewHolder(@NonNull cLinePropertyValueNoInputAdapter.ItempropertyValueNoInputViewHolder pvHolderObj, int pvPositionInt) {

        if (this.localItemPropertyValueObl != null || this.localItemPropertyValueObl.size() > 0) {
            final cLinePropertyValue pickorderLinePropertyValue = this.localItemPropertyValueObl.get(pvPositionInt);

            if (pickorderLinePropertyValue.getItemProperty() != null) {
                pvHolderObj.itemPropertyTextView.setText(pickorderLinePropertyValue.getItemProperty().getOmschrijvingStr());
                pvHolderObj.itemPropertyValueTextView.setText(pickorderLinePropertyValue.getValueStr());

                switch (pickorderLinePropertyValue.getItemProperty().getValueTypeStr().toUpperCase()) {

                    case "BOOLEAN":
                        pvHolderObj.imageViewPropertyType.setImageResource(R.drawable.ic_check_black_24dp);
                        break;

                    case "DECIMAL":
                        pvHolderObj.imageViewPropertyType.setImageResource(R.drawable.ic_counter_black_24dp);
                        break;

                    case "TEXT" :
                    case "CODE":
                        pvHolderObj.imageViewPropertyType.setImageResource(R.drawable.ic_text_black_24dp);
                        break;

                    case "DATE":
                        pvHolderObj.imageViewPropertyType.setImageResource(R.drawable.ic_calendar_black_24dp);
                        break;
                }
            }

            pvHolderObj.itemPropertyValueTextView.setOnClickListener(v -> {
                final Animation animation = AnimationUtils.loadAnimation(cAppExtension.context.getApplicationContext(), R.anim.bounce);
                ICS.Utils.cBounceInterpolator interpolator = new ICS.Utils.cBounceInterpolator(0.1,15);
                animation.setInterpolator(interpolator);
                v.startAnimation(animation);
                cUserInterface.pPlaySound(R.raw.message, 0);
            });
        }
    }

    public void pFillData(List<cLinePropertyValue> pvDataObl) {
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
