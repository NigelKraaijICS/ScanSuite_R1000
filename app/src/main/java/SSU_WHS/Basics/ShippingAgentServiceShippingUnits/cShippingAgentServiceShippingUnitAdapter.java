package SSU_WHS.Basics.ShippingAgentServiceShippingUnits;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ICS.Utils.cText;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import nl.icsvertex.scansuite.R;

public class cShippingAgentServiceShippingUnitAdapter extends RecyclerView.Adapter<cShippingAgentServiceShippingUnitAdapter.ShippingAgentServiceShippingUnitViewHolder> {


    //Region Public Properties

    class ShippingAgentServiceShippingUnitViewHolder extends RecyclerView.ViewHolder{

        private LinearLayout shippingUnitItemLinearLayout;
        private ImageView imageViewShippingUnit;
        private TextView textViewDescription;
        private TextView textViewShippingUnit;
        private TextView textViewQuantityUsed;
        private ConstraintLayout primaryContent;
        private ConstraintLayout secondaryContent;
        private AppCompatImageButton imageButtonMinus;
        private AppCompatImageButton imageButtonPlus;
        private  AppCompatImageButton imageButtonZero;
        private AppCompatImageView imageChevronDown;

        ShippingAgentServiceShippingUnitViewHolder(View pvView) {
            super(pvView);
            this.shippingUnitItemLinearLayout = pvView.findViewById(R.id.shippingUnitItemLinearLayout);
            this.primaryContent = pvView.findViewById(R.id.primaryContent);
            this.secondaryContent = pvView.findViewById(R.id.secondaryContent);
            this.secondaryContent.setVisibility(View.GONE);
            this.imageViewShippingUnit = pvView.findViewById(R.id.imageViewShippingUnit);
            this.textViewDescription = pvView.findViewById(R.id.textViewDescription);
            this.textViewDescription.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            this.textViewDescription.setSingleLine(true);
            this.textViewDescription.setMarqueeRepeatLimit(5);
            this.textViewDescription.setSelected(true);
            this.textViewShippingUnit = pvView.findViewById(R.id.textViewShippingUnit);
            this.textViewShippingUnit.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            this.textViewShippingUnit.setSingleLine(true);
            this.textViewShippingUnit.setMarqueeRepeatLimit(5);
            this.textViewShippingUnit.setSelected(true);
            this.imageButtonPlus = pvView.findViewById(R.id.imageButtonPlus);
            this.imageButtonMinus = pvView.findViewById(R.id.imageButtonMinus);
            this.imageButtonZero = pvView.findViewById(R.id.imageButtonZero);
            this.textViewQuantityUsed = pvView.findViewById(R.id.textViewQuantityUsed);
            this.imageChevronDown = pvView.findViewById(R.id.imageChevronDown);
        }
    }

    //End Region Public Properties

    //Region Private Properties

    private List<LinearLayout> shippingUnitItemLinearLayouts = new ArrayList<>();
    private RecyclerView thisRecyclerView;
    private LayoutInflater layoutInflater;
    private List<cShippingAgentServiceShippingUnit> localShippingUnitsObl;

    //End Region Private Properties

    //Region Constructor
    public cShippingAgentServiceShippingUnitAdapter() {
        this.layoutInflater = LayoutInflater.from(cAppExtension.context);
    }
    //End Region Constructor

    //Region Default Methods

    @Override
    public cShippingAgentServiceShippingUnitAdapter.ShippingAgentServiceShippingUnitViewHolder onCreateViewHolder(ViewGroup pvViewGroup, int pvViewTypeInt) {
        View itemView = layoutInflater.inflate(R.layout.recycler_shippingunit, pvViewGroup, false);
        return new cShippingAgentServiceShippingUnitAdapter.ShippingAgentServiceShippingUnitViewHolder(itemView);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView pvRecyclerView) {
        super.onAttachedToRecyclerView(pvRecyclerView);
        this.thisRecyclerView = pvRecyclerView;
    }


    @Override
    public void onBindViewHolder(final cShippingAgentServiceShippingUnitAdapter.ShippingAgentServiceShippingUnitViewHolder pvHolder, final int pvPositionInt) {

        this.shippingUnitItemLinearLayouts.add(pvHolder.shippingUnitItemLinearLayout);

        if (this.localShippingUnitsObl == null || this.localShippingUnitsObl.size() == 0) {
            return;
        }

        final cShippingAgentServiceShippingUnit shippingAgentServiceShippingUnit = this.localShippingUnitsObl.get(pvPositionInt);

        pvHolder.textViewDescription.setText(shippingAgentServiceShippingUnit.getDescriptionStr());
        pvHolder.textViewShippingUnit.setText(shippingAgentServiceShippingUnit.getShippingUnitStr());
        pvHolder.textViewQuantityUsed.setText(cText.intToString(shippingAgentServiceShippingUnit.getShippingUnitQuantityUsedInt()));


        if (shippingAgentServiceShippingUnit.getShippingUnitStr().equalsIgnoreCase(cShippingAgentServiceShippingUnit.SHIPPINGUNIT_BOX)) {
            pvHolder.imageViewShippingUnit.setImageResource(R.drawable.ic_box);
        }
        if (shippingAgentServiceShippingUnit.getShippingUnitStr().toLowerCase().contains(cShippingAgentServiceShippingUnit.SHIPPINGUNIT_BOX.toLowerCase())) {
            pvHolder.imageViewShippingUnit.setImageResource(R.drawable.ic_box);
        }
        if (shippingAgentServiceShippingUnit.getShippingUnitStr().equalsIgnoreCase(cShippingAgentServiceShippingUnit.SHIPPINGUNIT_PALLET)) {
            pvHolder.imageViewShippingUnit.setImageResource(R.drawable.ic_pallet);
        }
        if (shippingAgentServiceShippingUnit.getShippingUnitStr().equalsIgnoreCase(cShippingAgentServiceShippingUnit.SHIPPINGUNIT_CONTAINER)) {
            pvHolder.imageViewShippingUnit.setImageResource(R.drawable.ic_container);
        }
        if (shippingAgentServiceShippingUnit.getShippingUnitStr().equalsIgnoreCase(cShippingAgentServiceShippingUnit.SHIPPINGUNIT_LETTERBOX)) {
            pvHolder.imageViewShippingUnit.setImageResource(R.drawable.ic_letterbox);
        }
        if (shippingAgentServiceShippingUnit.getShippingUnitStr().equalsIgnoreCase(cShippingAgentServiceShippingUnit.SHIPPINGUNIT_HANGING)) {
            pvHolder.imageViewShippingUnit.setImageResource(R.drawable.ic_hanging);
        }

        pvHolder.primaryContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                AnimationUtils.loadAnimation(cAppExtension.context.getApplicationContext(), R.anim.rotate_180);

                //Close all others
                for (LinearLayout aLayout : shippingUnitItemLinearLayouts) {
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

                Boolean isExpanded;

                if (pvHolder.secondaryContent.getVisibility() == View.VISIBLE) {
                    isExpanded = true;
                }
                else {
                    isExpanded = false;
                }

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

                cShippingAgentServiceShippingUnit.currentShippingAgentServiceShippingUnit = shippingAgentServiceShippingUnit;

                Integer currentQuantity = cText.stringToInteger(pvHolder.textViewQuantityUsed.getText().toString());
                Integer newQuantity = currentQuantity + 1;
                pvHolder.textViewQuantityUsed.setText(Integer.toString(newQuantity));
                cShippingAgentServiceShippingUnit.currentShippingAgentServiceShippingUnit.ShippingUnitQuantityUsedInt = newQuantity;
            }
        });

        pvHolder.imageButtonMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cShippingAgentServiceShippingUnit.currentShippingAgentServiceShippingUnit = shippingAgentServiceShippingUnit;


                Integer currentQuantity = cText.stringToInteger(pvHolder.textViewQuantityUsed.getText().toString());
                if (currentQuantity == 0) {
                    cUserInterface.pDoNope(pvHolder.textViewQuantityUsed, true, false);
                    return;
                }
                Integer newQuantity = currentQuantity - 1;
                pvHolder.textViewQuantityUsed.setText(Integer.toString(newQuantity));
                cShippingAgentServiceShippingUnit.currentShippingAgentServiceShippingUnit.ShippingUnitQuantityUsedInt = newQuantity;
            }
        });

        pvHolder.imageButtonZero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer newQuantity = 0;
                pvHolder.textViewQuantityUsed.setText(Integer.toString(newQuantity));
                cShippingAgentServiceShippingUnit.currentShippingAgentServiceShippingUnit.ShippingUnitQuantityUsedInt = newQuantity;
            }
        });
    }


    @Override
    public int getItemCount () {
        if (this.localShippingUnitsObl != null)
            return this.localShippingUnitsObl.size();
        else return 0;
    }

    //End Region Default Methods

    //Region Public Methods

    public void pFillData(List<cShippingAgentServiceShippingUnit> pvDataObl) {
        this.localShippingUnitsObl = pvDataObl;
        notifyDataSetChanged();
    }

    //End Region Public Methods

    //Region Private Methods


    //End Region Private Methods



}


