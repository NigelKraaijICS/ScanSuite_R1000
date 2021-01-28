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
import ICS.cAppExtension;
import nl.icsvertex.scansuite.Activities.Pick.PickorderLineItemPropertyInputActvity;
import nl.icsvertex.scansuite.R;

public class cPickorderLinePropertyValueInputAdapter extends RecyclerView.Adapter<cPickorderLinePropertyValueInputAdapter.commentViewHolder>{

    public static class commentViewHolder extends RecyclerView.ViewHolder{

        private AppCompatImageView imageViewPropertyType;
        private final TextView textViewDescription;
        private final TextView textViewValue;
        private TextView textViewQuantityUsed;
        private final ConstraintLayout primaryContent;
        private final ConstraintLayout secondaryContent;
        private final AppCompatImageButton imageButtonMinus;
        private final AppCompatImageButton imageButtonPlus;
        private final AppCompatImageButton imageButtonZero;
        private final  AppCompatImageButton imageButtonManual;
        private final AppCompatImageView imageChevronDown;

        public LinearLayout itemPropertyValueInputItemLinearLayout;

        public commentViewHolder(View pvView) {
            super(pvView);

            this.imageViewPropertyType =  pvView.findViewById(R.id.imageViewPropertyType);

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
            this.imageButtonManual = pvView.findViewById(R.id.imageButtonManual);
            this.textViewQuantityUsed = pvView.findViewById(R.id.textViewQuantityUsed);
            this.imageChevronDown = pvView.findViewById(R.id.imageChevronDown);
        }
    }

    //Region Private Properties
    private final LayoutInflater layoutInflaterObject;
    private final List<LinearLayout> itemPropertyValueLinearLayoutObl = new ArrayList<>();
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

        if (this.localItemPropertyValueObl != null && this.localItemPropertyValueObl.size() > 0) {

            final cPickorderLinePropertyValue pickorderLinePropertyValue = this.localItemPropertyValueObl.get(pvPositionInt);


            switch (pickorderLinePropertyValue.getItemProperty().getValueTypeStr().toUpperCase()) {

                case "BOOLEAN":
                    pvHolder.imageViewPropertyType.setImageResource(R.drawable.ic_check_black_24dp);
                    break;

                case "DECIMAL":
                    pvHolder.imageViewPropertyType.setImageResource(R.drawable.ic_counter_black_24dp);
                    break;

                case "TEXT" :
                case "CODE":
                    pvHolder.imageViewPropertyType.setImageResource(R.drawable.ic_text_black_24dp);
                    break;

                case "DATE":
                    pvHolder.imageViewPropertyType.setImageResource(R.drawable.ic_calendar_black_24dp);
                    break;
            }

            pvHolder.textViewDescription.setText(pickorderLinePropertyValue.getItemProperty().getOmschrijvingStr());
            pvHolder.textViewValue.setText(pickorderLinePropertyValue.getValueStr());
            pvHolder.textViewQuantityUsed.setText(cText.pDoubleToStringStr(pickorderLinePropertyValue.getQuantityDbl()));

            if (pickorderLinePropertyValue.getQuantityDbl() == 0 ) {
                pvHolder.imageChevronDown.setVisibility(View.GONE);
            }

            if (cAppExtension.activity instanceof PickorderLineItemPropertyInputActvity) {
                PickorderLineItemPropertyInputActvity pickorderLineItemPropertyInputActvity = (PickorderLineItemPropertyInputActvity)cAppExtension.activity;

                if (pickorderLineItemPropertyInputActvity.getQuantityAvailable() == 0) {
                    pvHolder.imageButtonPlus.setVisibility(View.INVISIBLE);
                }
            }

            if (pickorderLinePropertyValue == cPickorderLinePropertyValue.currentPickorderLinePropertyValue) {
                pvHolder.secondaryContent.setVisibility(View.VISIBLE);
            }


            pvHolder.primaryContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {

                    if (pickorderLinePropertyValue.getQuantityDbl() == 0) {
                        return;
                    }

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
                        pvHolder.imageChevronDown.animate().rotation(180).start();
                        pvHolder.secondaryContent.animate().scaleY(1).start();
                        pvHolder.secondaryContent.setVisibility(View.VISIBLE);
                    }


                }
            });

            pvHolder.imageButtonPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    cPickorderLinePropertyValue.currentPickorderLinePropertyValue = pickorderLinePropertyValue;

                    int currentQuantity = cText.pStringToIntegerInt(pvHolder.textViewQuantityUsed.getText().toString());
                    int newQuantity;
                    newQuantity = currentQuantity + 1;
                    cPickorderLinePropertyValue.currentPickorderLinePropertyValue.quantityDbl = newQuantity;

                    if (cAppExtension.activity instanceof PickorderLineItemPropertyInputActvity) {
                        PickorderLineItemPropertyInputActvity pickorderLineItemPropertyInputActvity = (PickorderLineItemPropertyInputActvity)cAppExtension.activity;
                        pickorderLineItemPropertyInputActvity.pRefreshActivity();
                        pickorderLineItemPropertyInputActvity.pTryToChangePickedQuantity(true, false,1);
                    }

                }
            });

            pvHolder.imageButtonMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    cPickorderLinePropertyValue.currentPickorderLinePropertyValue = pickorderLinePropertyValue;

                    int currentQuantity = cText.pStringToIntegerInt(pvHolder.textViewQuantityUsed.getText().toString());
                    int newQuantity = currentQuantity - 1;
                    cPickorderLinePropertyValue.currentPickorderLinePropertyValue.quantityDbl = newQuantity;

                    final PickorderLineItemPropertyInputActvity pickorderLineItemPropertyInputActvity;
                    if (cAppExtension.activity instanceof PickorderLineItemPropertyInputActvity) {
                        pickorderLineItemPropertyInputActvity = (PickorderLineItemPropertyInputActvity) cAppExtension.activity;

                        if (newQuantity == 0) {
                            pickorderLineItemPropertyInputActvity.pDeleteValueFromRecyler();
                        }
                        pickorderLineItemPropertyInputActvity.pRefreshActivity();
                        pickorderLineItemPropertyInputActvity.pTryToChangePickedQuantity(false, false,1);
                    }
                }
            });

            pvHolder.imageButtonZero.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    cPickorderLinePropertyValue.currentPickorderLinePropertyValue = pickorderLinePropertyValue;
                    cPickorderLinePropertyValue.currentPickorderLinePropertyValue.quantityDbl = 0;

                    final PickorderLineItemPropertyInputActvity pickorderLineItemPropertyInputActvity;
                    if (cAppExtension.activity instanceof PickorderLineItemPropertyInputActvity) {
                        pickorderLineItemPropertyInputActvity = (PickorderLineItemPropertyInputActvity) cAppExtension.activity;
                        pickorderLineItemPropertyInputActvity.pDeleteValueFromRecyler();
                        pickorderLineItemPropertyInputActvity.pRefreshActivity();
                    }
                }
            });

            pvHolder.imageButtonManual.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    cPickorderLinePropertyValue.currentPickorderLinePropertyValue = pickorderLinePropertyValue;

                    int currentQuantity = cText.pStringToIntegerInt(pvHolder.textViewQuantityUsed.getText().toString());
                    cPickorderLinePropertyValue.currentPickorderLinePropertyValue.quantityDbl = 0;

                    if (cAppExtension.activity instanceof PickorderLineItemPropertyInputActvity) {
                        PickorderLineItemPropertyInputActvity pickorderLineItemPropertyInputActvity = (PickorderLineItemPropertyInputActvity) cAppExtension.activity;
                        pickorderLineItemPropertyInputActvity.pShowNumericInputFragment();
                    }
                }
            });

            pvHolder.imageViewPropertyType.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    cPickorderLinePropertyValue.currentPickorderLinePropertyValue = pickorderLinePropertyValue;

                    if (cAppExtension.activity instanceof PickorderLineItemPropertyInputActvity) {
                        PickorderLineItemPropertyInputActvity pickorderLineItemPropertyInputActvity = (PickorderLineItemPropertyInputActvity) cAppExtension.activity;
                        pickorderLineItemPropertyInputActvity.pShowTextInputFragment();
                    }
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
