package SSU_WHS.Basics.Packaging;

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
import nl.icsvertex.scansuite.Activities.Packaging.PackagingActivity;
import nl.icsvertex.scansuite.BuildConfig;
import nl.icsvertex.scansuite.Fragments.Packaging.PackagingInFragment;
import nl.icsvertex.scansuite.Fragments.Packaging.PackagingOutFragment;
import nl.icsvertex.scansuite.R;

public class cPackagingInAdapter extends RecyclerView.Adapter<cPackagingInAdapter.PackagingViewHolder> {


    //Region Public Properties

    static class PackagingViewHolder extends RecyclerView.ViewHolder{

        private LinearLayout packagingItemLinearLayout;
        private ImageView imageViewPackagingUnit;
        private TextView textViewDescription;
        private TextView textViewPackagingUnit;
        private TextView textViewQuantityUsed;
        private ConstraintLayout primaryContent;
        private ConstraintLayout secondaryContent;
        private AppCompatImageButton imageButtonMinus;
        private AppCompatImageButton imageButtonPlus;
        private  AppCompatImageButton imageButtonZero;
        private AppCompatImageView imageChevronDown;

        PackagingViewHolder(View pvView) {
            super(pvView);
            this.packagingItemLinearLayout = pvView.findViewById(R.id.packagingItemLinearLayout);
            this.primaryContent = pvView.findViewById(R.id.primaryContent);
            this.secondaryContent = pvView.findViewById(R.id.secondaryContent);
            this.secondaryContent.setVisibility(View.GONE);
            this.imageViewPackagingUnit = pvView.findViewById(R.id.imageViewPackagingUnit);
            this.textViewDescription = pvView.findViewById(R.id.textViewDescription);
            this.textViewDescription.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            this.textViewDescription.setSingleLine(true);
            this.textViewDescription.setMarqueeRepeatLimit(5);
            this.textViewDescription.setSelected(true);
            this.textViewPackagingUnit = pvView.findViewById(R.id.textViewPackagingUnit);
            this.textViewPackagingUnit.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            this.textViewPackagingUnit.setSingleLine(true);
            this.textViewPackagingUnit.setMarqueeRepeatLimit(5);
            this.textViewPackagingUnit.setSelected(true);
            this.imageButtonPlus = pvView.findViewById(R.id.imageButtonPlus);
            this.imageButtonMinus = pvView.findViewById(R.id.imageButtonMinus);
            this.imageButtonZero = pvView.findViewById(R.id.imageButtonZero);
            this.textViewQuantityUsed = pvView.findViewById(R.id.textViewQuantityUsed);
            this.imageChevronDown = pvView.findViewById(R.id.imageChevronDown);
        }
    }

    //End Region Public Properties

    //Region Private Properties

    private List<LinearLayout> packagingUnitItemLinearLayouts = new ArrayList<>();
    private LayoutInflater layoutInflater;
    private List<cPackaging> localPackagingUnitsObl;

    //End Region Private Properties

    //Region Constructor
    public cPackagingInAdapter() {
        this.layoutInflater = LayoutInflater.from(cAppExtension.context);
    }
    //End Region Constructor

    //Region Default Methods

    @NonNull
    @Override
    public cPackagingInAdapter.PackagingViewHolder onCreateViewHolder(@NonNull ViewGroup pvViewGroup, int pvViewTypeInt) {
        View itemView = layoutInflater.inflate(R.layout.recycler_packagingunit, pvViewGroup, false);
        return new PackagingViewHolder(itemView);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView pvRecyclerView) {
        super.onAttachedToRecyclerView(pvRecyclerView);
    }


    @Override
    public void onBindViewHolder(final cPackagingInAdapter.PackagingViewHolder pvHolder, final int pvPositionInt) {

        this.packagingUnitItemLinearLayouts.add(pvHolder.packagingItemLinearLayout);

        if (this.localPackagingUnitsObl == null || this.localPackagingUnitsObl.size() == 0) {
            return;
        }

        final cPackaging packaging = this.localPackagingUnitsObl.get(pvPositionInt);

        pvHolder.textViewDescription.setText(packaging.getDescriptionStr());
        pvHolder.textViewPackagingUnit.setText(packaging.getCodeStr());

        pvHolder.textViewQuantityUsed.setText(cText.pIntToStringStr(packaging.getQuantityInUsedInt()));

        switch (pvPositionInt){
            case 0:
                pvHolder.imageViewPackagingUnit.setImageResource(R.drawable.ic_pallet);
                break;
            case 1:
                pvHolder.imageViewPackagingUnit.setImageResource(R.drawable.ic_pallet1);
                break;
            case 2:
                pvHolder.imageViewPackagingUnit.setImageResource(R.drawable.ic_pallet2);
                break;
            case 3:
                pvHolder.imageViewPackagingUnit.setImageResource(R.drawable.ic_pallet3);
                break;
            case 4:
                pvHolder.imageViewPackagingUnit.setImageResource(R.drawable.ic_pallet4);
                break;
            case 5:
                pvHolder.imageViewPackagingUnit.setImageResource(R.drawable.ic_pallet5);
                break;
            case 6:
                pvHolder.imageViewPackagingUnit.setImageResource(R.drawable.ic_pallet6);
                break;
            case 7:
                pvHolder.imageViewPackagingUnit.setImageResource(R.drawable.ic_pallet7);
                break;
            case 8:
                pvHolder.imageViewPackagingUnit.setImageResource(R.drawable.ic_pallet8);
                break;
            case 9:
                pvHolder.imageViewPackagingUnit.setImageResource(R.drawable.ic_pallet9);
                break;
            default:
                pvHolder.imageViewPackagingUnit.setImageResource(R.drawable.ic_padlock);
                break;
        }


        pvHolder.primaryContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                AnimationUtils.loadAnimation(cAppExtension.context.getApplicationContext(), R.anim.rotate_180);

                //Close all others
                for (LinearLayout aLayout : packagingUnitItemLinearLayouts) {
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
                    pvHolder.imageChevronDown.animate().rotation(180).start();
                    pvHolder.secondaryContent.animate().scaleY(1).start();
                    pvHolder.secondaryContent.setVisibility(View.VISIBLE);

                }


            }
        });

        pvHolder.imageButtonPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cPackaging.currentPackaging = packaging;

                int currentQuantity = cText.pStringToIntegerInt(pvHolder.textViewQuantityUsed.getText().toString());
                Integer newQuantity;
                newQuantity = currentQuantity + 1;
                pvHolder.textViewQuantityUsed.setText(cText.pIntToStringStr((newQuantity)));

                cPackaging.currentPackaging.quantityInUsedInt = newQuantity;



            }
        });

        pvHolder.imageButtonMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cPackaging.currentPackaging = packaging;


                int currentQuantity = cText.pStringToIntegerInt(pvHolder.textViewQuantityUsed.getText().toString());
                if (currentQuantity == 0) {
                    cUserInterface.pDoNope(pvHolder.textViewQuantityUsed, true, false);
                    return;
                }
                Integer newQuantity = currentQuantity - 1;
                pvHolder.textViewQuantityUsed.setText(cText.pIntToStringStr(newQuantity));

                cPackaging.currentPackaging.quantityInUsedInt = newQuantity;

            }
        });

        pvHolder.imageButtonZero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer newQuantity = 0;
                pvHolder.textViewQuantityUsed.setText(cText.pIntToStringStr(newQuantity));
                    cPackaging.currentPackaging.quantityInUsedInt = newQuantity;
            }
        });
    }


    @Override
    public int getItemCount () {
        if (this.localPackagingUnitsObl != null)
            return this.localPackagingUnitsObl.size();
        else return 0;
    }

    //End Region Default Methods

    //Region Public Methods

    public void pFillData(List<cPackaging> pvDataObl) {
        this.localPackagingUnitsObl = pvDataObl;
        notifyDataSetChanged();
    }

    //End Region Public Methods

    //Region Private Methods


    //End Region Private Methods



}


