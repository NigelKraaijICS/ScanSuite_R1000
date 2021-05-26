package SSU_WHS.Basics.ArticlePropertyValue;

import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import ICS.cAppExtension;
import nl.icsvertex.scansuite.R;

public class cArticlePropertyValueAdapter extends RecyclerView.Adapter<cArticlePropertyValueAdapter.commentViewHolder> {

    private List<cArticlePropertyValue> localItemPropertyValueObl;

    public static class commentViewHolder extends RecyclerView.ViewHolder{
        private final AppCompatImageView imagePropertyType;
        private final TextView textViewPropertyCode;
        private final EditText editTextPropertyValue;

        public LinearLayout itemPropertyValueInputItemLinearLayout;
        public ConstraintLayout viewForeground;

    public commentViewHolder(View pvView) {
        super(pvView);
        this.imagePropertyType =  pvView.findViewById(R.id.imagePropertyType);
        this.textViewPropertyCode = pvView.findViewById(R.id.textViewPropertyCode);
        this.editTextPropertyValue = pvView.findViewById(R.id.editTextPropertyValue);
        this.itemPropertyValueInputItemLinearLayout = pvView.findViewById(R.id.itemPropertyLinearLayout);
    }
}

    //Region Private Properties
    private final LayoutInflater layoutInflaterObject;
    private final List<LinearLayout> itemPropertyValueLinearLayoutObl = new ArrayList<>();

    //End Region Private Properties

    //Region Constructor
    public cArticlePropertyValueAdapter() {
        this.layoutInflaterObject = LayoutInflater.from(cAppExtension.context);
    }
    //End Region Constructor

    @NonNull
    @Override
    public cArticlePropertyValueAdapter.commentViewHolder onCreateViewHolder(@NonNull ViewGroup pvParentVieGroup, int pvViewTypeInt) {
        View itemView = this.layoutInflaterObject.inflate(R.layout.recycler_item_property, pvParentVieGroup, false);
        return new cArticlePropertyValueAdapter.commentViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final cArticlePropertyValueAdapter.commentViewHolder pvHolder, int pvPositionInt) {

        this.itemPropertyValueLinearLayoutObl.add(pvHolder.itemPropertyValueInputItemLinearLayout);

        final cArticlePropertyValue articlePropertyValue = this.localItemPropertyValueObl.get(pvPositionInt);

        if (this.localItemPropertyValueObl.size() > 0) {
            switch (articlePropertyValue.getItemProperty().getValueTypeStr().toUpperCase()) {

                case "BOOLEAN":
                    pvHolder.imagePropertyType.setImageResource(R.drawable.ic_check_black_24dp);
                    break;

                case "DECIMAL":
                    pvHolder.imagePropertyType.setImageResource(R.drawable.ic_counter_black_24dp);
                    break;

                case "TEXT" :
                case "CODE":
                    pvHolder.imagePropertyType.setImageResource(R.drawable.ic_text_black_24dp);
                    break;

                case "DATE":
                    pvHolder.imagePropertyType.setImageResource(R.drawable.ic_calendar_black_24dp);
                    break;
            }

            pvHolder.itemPropertyValueInputItemLinearLayout.setOnClickListener(pvView -> {
                for (LinearLayout linearLayout : itemPropertyValueLinearLayoutObl){
                    linearLayout.setSelected(false);
                }
                pvView.setSelected(true);
                cArticlePropertyValue.currentArticlePropertyValue = articlePropertyValue;
                pvHolder.editTextPropertyValue.requestFocus();
            });

            pvHolder.textViewPropertyCode.setText(articlePropertyValue.getItemProperty().getOmschrijvingStr());
            pvHolder.editTextPropertyValue.setText(articlePropertyValue.getValueStr());

            pvHolder.editTextPropertyValue.setOnClickListener(view -> {


                String propertyValueStr = articlePropertyValue.getItemProperty().getValueTypeStr().toUpperCase();


                if(propertyValueStr.equals("BOOLEAN")  || propertyValueStr.equals("TEXT") || propertyValueStr.equals("CODE")){
                    pvHolder.editTextPropertyValue.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
                }
                if(propertyValueStr.equals("DATE")){
                    pvHolder.editTextPropertyValue.setInputType(InputType.TYPE_CLASS_DATETIME | InputType.TYPE_DATETIME_VARIATION_DATE);
                }
                if(propertyValueStr.equals("DECIMAL")){
                    pvHolder.editTextPropertyValue.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED| InputType.TYPE_NUMBER_FLAG_DECIMAL);
                }
            });

            pvHolder.editTextPropertyValue.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    articlePropertyValue.valueStr = s.toString().trim();
                }
            });
        }
    }

    public void pFillData(List<cArticlePropertyValue> pvDataObl) {
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
