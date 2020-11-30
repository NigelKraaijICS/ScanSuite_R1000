package SSU_WHS.Basics.PropertyGroupProperty;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import ICS.cAppExtension;
import SSU_WHS.Basics.Translations.cTranslation;
import nl.icsvertex.scansuite.R;

public class cPropertyGroupPropertyAdapter extends RecyclerView.Adapter<cPropertyGroupPropertyAdapter.PropertyGroupPropertyViewHolder>  {

    //Region Public Properties
    public static class PropertyGroupPropertyViewHolder extends RecyclerView.ViewHolder{

        private final TextView textViewPropertyKey;
        private final TextView textViewPropertyValue;
        private final LinearLayout propertyGroupPropertyLinearLayout;

        public PropertyGroupPropertyViewHolder(View pvItemView) {
            super(pvItemView);

            this.propertyGroupPropertyLinearLayout = pvItemView.findViewById(R.id.propertyGroupPropertyLinearLayout);

            this.textViewPropertyKey = pvItemView.findViewById(R.id.textViewPropertyKey);
            this.textViewPropertyKey.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            this.textViewPropertyKey.setSingleLine(true);
            this.textViewPropertyKey.setMarqueeRepeatLimit(5);
            this.textViewPropertyKey.setSelected(true);

            this.textViewPropertyValue = pvItemView.findViewById(R.id.textViewPropertyValue);
            this.textViewPropertyValue.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            this.textViewPropertyValue.setSingleLine(true);
            this.textViewPropertyValue.setMarqueeRepeatLimit(5);
            this.textViewPropertyValue.setSelected(true);

        }
    }
    //End Region Public Properties

    //Region Private Properties
    private final LayoutInflater LayoutInflaterObject;
    //End Region Private Propertiess

    private final List<LinearLayout> IndentifierInfoLineViewHolder = new ArrayList<>();
    private List<Map.Entry<String, String>> localPropertyObl;
    //Region Constructor
    public cPropertyGroupPropertyAdapter() {
        this.LayoutInflaterObject = LayoutInflater.from(cAppExtension.context);
    }
    // End Region Constructor

    //Region Default Methods

    @NonNull
    @Override
    public cPropertyGroupPropertyAdapter.PropertyGroupPropertyViewHolder onCreateViewHolder(@NonNull ViewGroup pvParent, int pvViewTypeInt) {
        View itemView = this.LayoutInflaterObject.inflate(R.layout.recycler_propertygroupproperty, pvParent, false);
        return new PropertyGroupPropertyViewHolder(itemView);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView pvRecyclerView) {
        super.onAttachedToRecyclerView(pvRecyclerView);
    }

    @Override
    public void onBindViewHolder(final cPropertyGroupPropertyAdapter.PropertyGroupPropertyViewHolder pvHolder, final int pvPositionInt) {

        this.IndentifierInfoLineViewHolder.add(pvHolder.propertyGroupPropertyLinearLayout);
        Map.Entry<String, String> keyValueEntry = this.localPropertyObl.get(pvPositionInt);

        pvHolder.textViewPropertyKey.setText(cTranslation.pGetTranslastionStr(keyValueEntry.getKey(), Locale.getDefault().getLanguage().toUpperCase()) );
        pvHolder.textViewPropertyValue.setText(keyValueEntry.getValue());

    }

    @Override
    public int getItemCount () {
        if (this.localPropertyObl != null)
            return this.localPropertyObl.size();
        else return 0;
    }
    //End Region Default Methods

    //Region Public Methods
    public void pFillData(LinkedHashMap<String, String> pvDataObl) {

        this.localPropertyObl = new ArrayList<>();

        for (Map.Entry<String, String> entry : pvDataObl.entrySet()) {
            this.localPropertyObl.add(entry);
        }

    }


    //End Region Public Methods

    //Region Private Methods

    //End Region Private Methods

}
