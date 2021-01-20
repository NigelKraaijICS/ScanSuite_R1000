package SSU_WHS.Picken.PickorderLinePropertyValue;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ICS.Utils.cText;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Basics.ShippingAgentServiceShippingUnits.cShippingAgentServiceShippingUnit;
import nl.icsvertex.scansuite.R;

public class cPickorderLinePropertyValueInputAdapter extends RecyclerView.Adapter<cPickorderLinePropertyValueInputAdapter.commentViewHolder>{

    public class commentViewHolder extends RecyclerView.ViewHolder{

        private TextView textViewDescription;
        private TextView textViewValue;
        private TextView textViewQuantityUsed;
        private ConstraintLayout primaryContent;
        private ConstraintLayout secondaryContent;
        private AppCompatImageButton imageButtonMinus;
        private AppCompatImageButton imageButtonPlus;
        private  AppCompatImageButton imageButtonZero;
        private AppCompatImageView imageChevronDown;

        public LinearLayout itemPropertyValueInputItemLinearLayout;

        public commentViewHolder(View pvView) {
            super(pvView);

            this.textViewDescription = pvView.findViewById(R.id.textViewDescription);
            this.textViewDescription.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            this.textViewDescription.setSingleLine(true);
            this.textViewDescription.setMarqueeRepeatLimit(5);
            this.textViewDescription.setSelected(true);

            this.textViewValue = pvView.findViewById(R.id.textViewValue);

            this.textViewQuantityUsed = pvView.findViewById(R.id.textViewQuantityUsed);

            this.itemPropertyValueInputItemLinearLayout = pvView.findViewById(R.id.itemPropertyValueInputItemLinearLayout);
            this.primaryContent = pvView.findViewById(R.id.primaryContent);
            this.secondaryContent = pvView.findViewById(R.id.secondaryContent);
            this.secondaryContent.setVisibility(View.GONE);

            this.imageButtonPlus = pvView.findViewById(R.id.imageButtonPlus);
            this.imageButtonMinus = pvView.findViewById(R.id.imageButtonMinus);
            this.imageButtonZero = pvView.findViewById(R.id.imageButtonZero);
            this.textViewQuantityUsed = pvView.findViewById(R.id.textViewQuantityUsed);
            this.imageChevronDown = pvView.findViewById(R.id.imageChevronDown);
        }
    }

    //Region Private Properties
    private final LayoutInflater layoutInflaterObject;
    private List<LinearLayout> itemPropertyValueLinearLayoutObl = new ArrayList<>();
    private List<cPickorderLinePropertyValue> localItemPropertyValueObl;
    //End Region Private Properties

    //Region Constructor
    public cPickorderLinePropertyValueInputAdapter() {
        this.layoutInflaterObject = LayoutInflater.from(cAppExtension.context);
    }
    //End Region Constructor

    @NonNull
    @Override
    public commentViewHolder onCreateViewHolder(@NonNull ViewGroup pvParentVieGroup, int pvViewTypeInt) {
        View itemView = this.layoutInflaterObject.inflate(R.layout.recycler_itempropertyvalue_input, pvParentVieGroup, false);
        return new commentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final commentViewHolder pvHolder, int pvPositionInt) {

        this.itemPropertyValueLinearLayoutObl.add(pvHolder.itemPropertyValueInputItemLinearLayout);

        if (this.localItemPropertyValueObl != null || this.localItemPropertyValueObl.size() > 0) {
            final cPickorderLinePropertyValue pickorderLinePropertyValue = this.localItemPropertyValueObl.get(pvPositionInt);

            pvHolder.textViewDescription.setText(pickorderLinePropertyValue.getItemProperty().getOmschrijvingStr());
            pvHolder.textViewValue.setText(pickorderLinePropertyValue.getValueStr());
            pvHolder.textViewQuantityUsed.setText(cText.pDoubleToStringStr(pickorderLinePropertyValue.getQuanitityDbl()));

            pvHolder.primaryContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    AnimationUtils.loadAnimation(cAppExtension.context.getApplicationContext(), R.anim.rotate_180);

                    //Close all others
                    for (LinearLayout aLayout : itemPropertyValueLinearLayoutObl) {
                        ConstraintLayout secondaryLayout = aLayout.findViewById(R.id.secondaryContent);
                        ConstraintLayout primaryLayout = aLayout.findViewById(R.id.primaryContent);
                        if (secondaryLayout != null) {
                            if (primaryLayout != view) {
                                if (secondaryLayout.getVisibility() == View.VISIBLE) {
                                    ImageView chevronImage = primaryLayout.findViewById(R.id.imageChevronDown);
                                    if (chevronImage != null) {
                                        chevronImage.animate().rotation(0).start();
                                    }
                                }
                                secondaryLayout.animate().scaleY(0).start();
                                secondaryLayout.setVisibility(View.GONE);
                            }
                        }
                    }

                    boolean isExpanded;

                    isExpanded = pvHolder.secondaryContent.getVisibility() == View.VISIBLE;

                    if (isExpanded) {
                        pvHolder.imageChevronDown.animate().rotation(0).start();
                        pvHolder.secondaryContent.animate().scaleY(0).start();
                        pvHolder.secondaryContent.setVisibility(View.GONE);
                    }
                    else {
                        //package + 1
                        pvHolder.imageButtonPlus.callOnClick();
                        pvHolder.imageChevronDown.animate().rotation(180).start();
                        pvHolder.secondaryContent.animate().scaleY(1).start();
                        pvHolder.secondaryContent.setVisibility(View.VISIBLE);
                    }


                }
            });

            pvHolder.imageButtonPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

//                    cShippingAgentServiceShippingUnit.currentShippingAgentServiceShippingUnit = shippingAgentServiceShippingUnit;
//
//                    int currentQuantity = cText.pStringToIntegerInt(pvHolder.textViewQuantityUsed.getText().toString());
//                    Integer newQuantity;
//                    newQuantity = currentQuantity + 1;
//                    pvHolder.textViewQuantityUsed.setText(cText.pIntToStringStr((newQuantity)));
//                    cShippingAgentServiceShippingUnit.currentShippingAgentServiceShippingUnit.ShippingUnitQuantityUsedInt = newQuantity;
                }
            });

            pvHolder.imageButtonMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

//                    cShippingAgentServiceShippingUnit.currentShippingAgentServiceShippingUnit = shippingAgentServiceShippingUnit;
//
//
//                    int currentQuantity = cText.pStringToIntegerInt(pvHolder.textViewQuantityUsed.getText().toString());
//                    if (currentQuantity == 0) {
//                        cUserInterface.pDoNope(pvHolder.textViewQuantityUsed, true, false);
//                        return;
//                    }
//                    Integer newQuantity = currentQuantity - 1;
//                    pvHolder.textViewQuantityUsed.setText(cText.pIntToStringStr(newQuantity));
//                    cShippingAgentServiceShippingUnit.currentShippingAgentServiceShippingUnit.ShippingUnitQuantityUsedInt = newQuantity;
                }
            });

            pvHolder.imageButtonZero.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Integer newQuantity = 0;
//                    pvHolder.textViewQuantityUsed.setText(cText.pIntToStringStr(newQuantity));
//                    cShippingAgentServiceShippingUnit.currentShippingAgentServiceShippingUnit.ShippingUnitQuantityUsedInt = newQuantity;
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
