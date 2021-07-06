package SSU_WHS.LineItemProperty.LinePropertyValue;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import ICS.Utils.cText;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Basics.ContentlabelContainer.cContentlabelContainer;
import SSU_WHS.Basics.Settings.cSetting;
import SSU_WHS.Intake.IntakeorderMATLineSummary.cIntakeorderMATSummaryLine;
import SSU_WHS.Intake.Intakeorders.cIntakeorder;
import SSU_WHS.Inventory.InventoryorderLines.cInventoryorderLine;
import SSU_WHS.LineItemProperty.LineProperty.cLineProperty;
import SSU_WHS.Move.MoveorderLines.cMoveorderLine;
import SSU_WHS.Picken.PickorderLines.cPickorderLine;
import SSU_WHS.Receive.ReceiveLines.cReceiveorderLine;
import SSU_WHS.Receive.ReceiveSummaryLine.cReceiveorderSummaryLine;
import nl.icsvertex.scansuite.Activities.Intake.IntakeOrderLinePropertyInputActivity;
import nl.icsvertex.scansuite.Activities.Inventory.InventoryLinePropertyInputActivity;
import nl.icsvertex.scansuite.Activities.Move.MoveLineItemPropertyActivity;
import nl.icsvertex.scansuite.Activities.Pick.PickorderLineItemPropertyInputActvity;
import nl.icsvertex.scansuite.Activities.Receive.ReceiveorderLinePropertyInputActivity;
import nl.icsvertex.scansuite.R;

public class cLinePropertyValueInputAdapter extends RecyclerView.Adapter<cLinePropertyValueInputAdapter.commentViewHolder> {
    public static class commentViewHolder extends RecyclerView.ViewHolder{

        private final AppCompatImageView imageViewPropertyType;
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
        private final Button imageButtonMax;

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
            this.imageButtonMax = pvView.findViewById(R.id.imageButtonMax);
        }
    }

    //Region Private Properties
    private final LayoutInflater layoutInflaterObject;
    private final List<LinearLayout> itemPropertyValueLinearLayoutObl = new ArrayList<>();
    private List<cLinePropertyValue> localItemPropertyValueObl;
    //End Region Private Properties

    //Region Constructor
    public cLinePropertyValueInputAdapter() {
        this.layoutInflaterObject = LayoutInflater.from(cAppExtension.context);
    }
    //End Region Constructor

    @NonNull
    @Override
    public cLinePropertyValueInputAdapter.commentViewHolder onCreateViewHolder(@NonNull ViewGroup pvParentVieGroup, int pvViewTypeInt) {
        View itemView = this.layoutInflaterObject.inflate(R.layout.recycler_itempropertyvalue_input, pvParentVieGroup, false);
        return new cLinePropertyValueInputAdapter.commentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final cLinePropertyValueInputAdapter.commentViewHolder pvHolder, int pvPositionInt) {

        this.itemPropertyValueLinearLayoutObl.add(pvHolder.itemPropertyValueInputItemLinearLayout);

        if (this.localItemPropertyValueObl != null && this.localItemPropertyValueObl.size() > 0) {

            final cLinePropertyValue linePropertyValue = this.localItemPropertyValueObl.get(pvPositionInt);


            switch (linePropertyValue.getItemProperty().getValueTypeStr().toUpperCase()) {

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

            if (linePropertyValue.getQuantityAvailableDbl() > 0) {
                pvHolder.textViewDescription.setText(linePropertyValue.getItemProperty().getOmschrijvingStr() + "  (" + (int) Math.round(linePropertyValue.getQuantityAvailableDbl()) + ")");
            } else{
                pvHolder.textViewDescription.setText(linePropertyValue.getItemProperty().getOmschrijvingStr());
            }


            pvHolder.textViewValue.setText(linePropertyValue.getValueStr());
            pvHolder.textViewQuantityUsed.setText(cText.pDoubleToStringStr(linePropertyValue.getQuantityDbl()));

            if (linePropertyValue.getQuantityDbl() == 0 ) {
                pvHolder.imageChevronDown.setVisibility(View.GONE);
            }

            if (linePropertyValue.getItemProperty().getUniqueBln() && linePropertyValue.getQuantityDbl() > 0 ) {
                pvHolder.imageButtonPlus.setVisibility(View.INVISIBLE);
                pvHolder.imageButtonManual.setVisibility(View.INVISIBLE);
                pvHolder.imageButtonMax.setVisibility(View.INVISIBLE);
            }

            if (linePropertyValue == cLinePropertyValue.currentLinePropertyValue) {
                pvHolder.secondaryContent.setVisibility(View.VISIBLE);
            }

            if (cAppExtension.activity instanceof MoveLineItemPropertyActivity) {
               // pvHolder.imageButtonManual.setVisibility(View.INVISIBLE);
                if (linePropertyValue.getLineProperty().getIsRequiredBln()){
                    pvHolder.imageButtonZero.setVisibility(View.INVISIBLE);
                }
            }

            if (cAppExtension.activity instanceof PickorderLineItemPropertyInputActvity) {

                if ( cPickorderLine.currentPickOrderLine != null && cPickorderLine.currentPickOrderLine.presetValueObl != null) {
                    for (cLinePropertyValue loopValue : cPickorderLine.currentPickOrderLine.presetValueObl){
                        if (loopValue.getPropertyCodeStr().equalsIgnoreCase(linePropertyValue.getPropertyCodeStr()) && loopValue.getValueStr().equalsIgnoreCase(linePropertyValue.getValueStr())){
                            pvHolder.imageButtonZero.setVisibility(View.INVISIBLE);
                            break;
                        }
                    }
                }
            }

            pvHolder.primaryContent.setOnClickListener(view -> {

                if (linePropertyValue.getValueStr() == null) {
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


            });

            pvHolder.imageButtonMax.setOnClickListener(view -> {

                cLinePropertyValue.currentLinePropertyValue = linePropertyValue;
                double availableDbl = 0.0;
                boolean moveLineBln = false;
                if(cAppExtension.activity instanceof ReceiveorderLinePropertyInputActivity){
                    availableDbl = cReceiveorderSummaryLine.currentReceiveorderSummaryLine.getQuantityDbl() - cReceiveorderSummaryLine.currentReceiveorderSummaryLine.getQuantityHandledDbl();
                }
                if(cAppExtension.activity instanceof IntakeOrderLinePropertyInputActivity){
                    availableDbl = cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getQuantityDbl() - cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getQuantityHandledDbl();
                }
                if (cAppExtension.activity instanceof PickorderLineItemPropertyInputActvity){
                    availableDbl = cPickorderLine.currentPickOrderLine.getQuantityDbl();
                }
                if (cAppExtension.activity instanceof InventoryLinePropertyInputActivity){
                    availableDbl = cInventoryorderLine.currentInventoryOrderLine.getQuantityDbl();
                }
                if (cAppExtension.activity instanceof MoveLineItemPropertyActivity){
                    if (cLinePropertyValue.currentLinePropertyValue.getQuantityAvailableDbl() > 0){
                        availableDbl = cLinePropertyValue.currentLinePropertyValue.getQuantityAvailableDbl() - cLinePropertyValue.currentLinePropertyValue.getQuantityDbl();
                        moveLineBln = true;
                    }else{
                        availableDbl = cMoveorderLine.currentMoveOrderLine.getQuantityDbl();
                    }
                }

                if (!moveLineBln){
                ArrayList<cLinePropertyValue> loopList = localItemPropertySortObl().get(linePropertyValue.getPropertyCodeStr());

                for (cLinePropertyValue linePropertyValue1 : loopList ) {
                    availableDbl -= linePropertyValue1.getQuantityDbl();
                 }
                }

                cLinePropertyValue.currentLinePropertyValue.quantityDbl += availableDbl;

                if (cAppExtension.activity instanceof ReceiveorderLinePropertyInputActivity) {
                    ReceiveorderLinePropertyInputActivity receiveorderLinePropertyInputActivity = (ReceiveorderLinePropertyInputActivity) cAppExtension.activity;

                    receiveorderLinePropertyInputActivity.pTryToChangeQuantity();
                    receiveorderLinePropertyInputActivity.pRefreshActivity();
                }

                if (cAppExtension.activity instanceof IntakeOrderLinePropertyInputActivity) {
                    IntakeOrderLinePropertyInputActivity intakeOrderLinePropertyInputActivity = (IntakeOrderLinePropertyInputActivity) cAppExtension.activity;

                    intakeOrderLinePropertyInputActivity.pTryToChangeQuantity();
                    intakeOrderLinePropertyInputActivity.pRefreshActivity();
                }

                if (cAppExtension.activity instanceof InventoryLinePropertyInputActivity) {
                    InventoryLinePropertyInputActivity inventoryLinePropertyInputActivity = (InventoryLinePropertyInputActivity) cAppExtension.activity;

                    inventoryLinePropertyInputActivity.pTryToChangeQuantity();
                    inventoryLinePropertyInputActivity.pRefreshActivity();
                }

                if (cAppExtension.activity instanceof MoveLineItemPropertyActivity) {
                    MoveLineItemPropertyActivity moveLineItemPropertyActivity = (MoveLineItemPropertyActivity) cAppExtension.activity;

                    moveLineItemPropertyActivity.pTryToChangeQuantity(true, false, availableDbl);
                    moveLineItemPropertyActivity.pRefreshActivity();
                }

                if (cAppExtension.activity instanceof PickorderLineItemPropertyInputActvity){
                    PickorderLineItemPropertyInputActvity pickorderLineItemPropertyInputActvity = (PickorderLineItemPropertyInputActvity) cAppExtension.activity;

                    if(cPickorderLine.currentPickOrderLine.currentContainer != null){
                        cPickorderLine.currentPickOrderLine.currentContainer.quantityHandledDbl  += availableDbl;
                    }
                    pickorderLineItemPropertyInputActvity.pTryToChangePickedQuantity( );
                    pickorderLineItemPropertyInputActvity.pRefreshActivity();
                }

            });

            pvHolder.imageButtonPlus.setOnClickListener(view -> {

                cLinePropertyValue.currentLinePropertyValue = linePropertyValue;
                if (cAppExtension.activity instanceof ReceiveorderLinePropertyInputActivity){
                    if (linePropertyValue.getQuantityDbl()>= (cReceiveorderSummaryLine.currentReceiveorderSummaryLine.getQuantityDbl() - cReceiveorderSummaryLine.currentReceiveorderSummaryLine.quantityHandledDbl)){
                        if (cIntakeorder.currentIntakeOrder.getReceiveNoExtraPiecesBln() && !cIntakeorder.currentIntakeOrder.isGenerated() && cReceiveorderSummaryLine.currentReceiveorderSummaryLine.getQuantityDbl() > 0 ) {
                            cUserInterface.pDoNope(pvHolder.imageButtonPlus, true, true);
                            cUserInterface.pShowSnackbarMessage(pvHolder.imageButtonPlus, cAppExtension.context.getString(R.string.number_cannot_be_higher),R.raw.headsupsound,false);
                            return ;
                        }
                        if (cSetting.RECEIVE_EXTRA_PIECES_PERCENTAGE() > 0 && cReceiveorderSummaryLine.currentReceiveorderSummaryLine.getQuantityDbl() > 0  && (cSetting.RECEIVE_EXTRA_PIECES_PERCENTAGE_MANDATORY())) {
                            //Check if the new quantity would exceed the allowed quantity
                            if (linePropertyValue.getQuantityDbl() > cReceiveorderSummaryLine.currentReceiveorderSummaryLine.getAllowedQuantityDbl()) {
                                //We would exceed the allowed quantity so show that this is not allowed
                                cUserInterface.pDoNope(pvHolder.imageButtonPlus, true, true);
                                cUserInterface.pShowSnackbarMessage(pvHolder.imageButtonPlus,  cAppExtension.context.getString(R.string.number_received_total_cant_be_higher_then, cText.pDoubleToStringStr(cReceiveorderSummaryLine.currentReceiveorderSummaryLine.getAllowedQuantityDbl())),R.raw.headsupsound,false);
                                return ;
                            }
                        }
                    }
                }
                if (cAppExtension.activity instanceof PickorderLineItemPropertyInputActvity){
                    if (linePropertyValue.getQuantityDbl()== cPickorderLine.currentPickOrderLine.getQuantityDbl()){
                        cUserInterface.pDoNope(pvHolder.imageButtonPlus, true, true);
                        cUserInterface.pShowSnackbarMessage(pvHolder.imageButtonPlus, cAppExtension.activity.getString(R.string.message_overpick_not_allowed),R.raw.headsupsound,false);
                        return;
                    }
                }
                if (cAppExtension.activity instanceof MoveLineItemPropertyActivity){

                    if (linePropertyValue.getQuantityAvailableDbl() > 0){
                        if (linePropertyValue.getQuantityDbl()== linePropertyValue.getQuantityAvailableDbl()){
                            cUserInterface.pDoNope(pvHolder.imageButtonPlus, true, true);
                            cUserInterface.pShowSnackbarMessage(pvHolder.imageButtonPlus, cAppExtension.activity.getString(R.string.message_overtake_not_allowed),R.raw.headsupsound,false);
                            return;
                        }
                    } else{
                        if (linePropertyValue.getQuantityDbl()== cMoveorderLine.currentMoveOrderLine.getQuantityDbl()){
                            cUserInterface.pDoNope(pvHolder.imageButtonPlus, true, true);
                            cUserInterface.pShowSnackbarMessage(pvHolder.imageButtonPlus, cAppExtension.activity.getString(R.string.message_overtake_not_allowed),R.raw.headsupsound,false);
                            return;
                        }
                    }
                }

                if (cAppExtension.activity instanceof IntakeOrderLinePropertyInputActivity){
                    if (linePropertyValue.getQuantityDbl()== cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getQuantityDbl()&& cSetting.RECEIVE_NO_EXTRA_ITEMS() && cSetting.RECEIVE_NO_EXTRA_PIECES()){
                        cUserInterface.pDoNope(pvHolder.imageButtonPlus, true, true);
                        cUserInterface.pShowSnackbarMessage(pvHolder.imageButtonPlus,  cAppExtension.context.getString(R.string.number_cannot_be_higher),R.raw.headsupsound,false);
                        return;
                    }
                }

                int currentQuantity = cText.pStringToIntegerInt(pvHolder.textViewQuantityUsed.getText().toString());
                cLinePropertyValue.currentLinePropertyValue.quantityDbl = currentQuantity + cLinePropertyValue.quantityPerUnitOfMeasureDbl;

                if (cAppExtension.activity instanceof ReceiveorderLinePropertyInputActivity) {
                    ReceiveorderLinePropertyInputActivity receiveorderLinePropertyInputActivity = (ReceiveorderLinePropertyInputActivity) cAppExtension.activity;

                    receiveorderLinePropertyInputActivity.pTryToChangeQuantity();
                    receiveorderLinePropertyInputActivity.pRefreshActivity();
                }
                if (cAppExtension.activity instanceof IntakeOrderLinePropertyInputActivity) {
                    IntakeOrderLinePropertyInputActivity intakeOrderLinePropertyInputActivity = (IntakeOrderLinePropertyInputActivity) cAppExtension.activity;

                    intakeOrderLinePropertyInputActivity.pTryToChangeQuantity();
                    intakeOrderLinePropertyInputActivity.pRefreshActivity();
                }
                if (cAppExtension.activity instanceof InventoryLinePropertyInputActivity) {
                    InventoryLinePropertyInputActivity inventoryLinePropertyInputActivity = (InventoryLinePropertyInputActivity) cAppExtension.activity;

                    inventoryLinePropertyInputActivity.pTryToChangeQuantity();
                    inventoryLinePropertyInputActivity.pRefreshActivity();
                }
                if (cAppExtension.activity instanceof MoveLineItemPropertyActivity) {
                    MoveLineItemPropertyActivity moveLineItemPropertyActivity = (MoveLineItemPropertyActivity) cAppExtension.activity;

                    moveLineItemPropertyActivity.pTryToChangeQuantity(true, false, cLinePropertyValue.quantityPerUnitOfMeasureDbl);
                    moveLineItemPropertyActivity.pRefreshActivity();
                }
                if (cAppExtension.activity instanceof PickorderLineItemPropertyInputActvity){
                    PickorderLineItemPropertyInputActvity pickorderLineItemPropertyInputActvity = (PickorderLineItemPropertyInputActvity) cAppExtension.activity;

                    if(cPickorderLine.currentPickOrderLine.currentContainer != null){
                        cPickorderLine.currentPickOrderLine.currentContainer.quantityHandledDbl  += cLinePropertyValue.quantityPerUnitOfMeasureDbl;
                    }

                    pickorderLineItemPropertyInputActvity.pTryToChangePickedQuantity();
                    pickorderLineItemPropertyInputActvity.pRefreshActivity();
                }

            });

            pvHolder.imageButtonMinus.setOnClickListener(view -> {

                cLinePropertyValue.currentLinePropertyValue = linePropertyValue;

                int currentQuantity = cText.pStringToIntegerInt(pvHolder.textViewQuantityUsed.getText().toString());
                double newQuantity = currentQuantity - cLinePropertyValue.quantityPerUnitOfMeasureDbl;
                cLinePropertyValue.currentLinePropertyValue.quantityDbl = newQuantity;

                if (cAppExtension.activity instanceof ReceiveorderLinePropertyInputActivity) {
                    ReceiveorderLinePropertyInputActivity  receiveorderLinePropertyInputActivity = (ReceiveorderLinePropertyInputActivity) cAppExtension.activity;

                    if (newQuantity == 0) {
                        receiveorderLinePropertyInputActivity.pDeleteValueFromRecyler();
                    }

                    receiveorderLinePropertyInputActivity.pTryToChangeQuantity();
                    receiveorderLinePropertyInputActivity.pRefreshActivity();
                }

                if (cAppExtension.activity instanceof IntakeOrderLinePropertyInputActivity) {
                    IntakeOrderLinePropertyInputActivity  intakeOrderLinePropertyInputActivity = (IntakeOrderLinePropertyInputActivity) cAppExtension.activity;

                    if (newQuantity == 0) {
                        intakeOrderLinePropertyInputActivity.pDeleteValueFromRecyler();
                    }

                    intakeOrderLinePropertyInputActivity.pTryToChangeQuantity();
                    intakeOrderLinePropertyInputActivity.pRefreshActivity();
                }
                if (cAppExtension.activity instanceof InventoryLinePropertyInputActivity) {
                    InventoryLinePropertyInputActivity  inventoryLinePropertyInputActivity = (InventoryLinePropertyInputActivity) cAppExtension.activity;

                    if (newQuantity == 0) {
                        inventoryLinePropertyInputActivity.pDeleteValueFromRecyler();
                    }

                    inventoryLinePropertyInputActivity.pTryToChangeQuantity();
                    inventoryLinePropertyInputActivity.pRefreshActivity();
                }
                if (cAppExtension.activity instanceof MoveLineItemPropertyActivity) {
                    MoveLineItemPropertyActivity  moveLineItemPropertyActivity = (MoveLineItemPropertyActivity) cAppExtension.activity;

                    if (newQuantity == 0) {
                        moveLineItemPropertyActivity.pDeleteValueFromRecyler();
                    }

                    moveLineItemPropertyActivity.pTryToChangeQuantity(false,false, cLinePropertyValue.quantityPerUnitOfMeasureDbl);
                    moveLineItemPropertyActivity.pRefreshActivity();
                }
                if (cAppExtension.activity instanceof PickorderLineItemPropertyInputActvity){
                    PickorderLineItemPropertyInputActvity pickorderLineItemPropertyInputActvity = (PickorderLineItemPropertyInputActvity) cAppExtension.activity;
                    if (newQuantity == 0) {
                        pickorderLineItemPropertyInputActvity.pDeleteValueFromRecyler();
                    }
                    if(cPickorderLine.currentPickOrderLine.currentContainer != null){
                        cPickorderLine.currentPickOrderLine.currentContainer.quantityHandledDbl  -= cLinePropertyValue.quantityPerUnitOfMeasureDbl;
                    }

                    pickorderLineItemPropertyInputActvity.pTryToChangePickedQuantity();
                    pickorderLineItemPropertyInputActvity.pRefreshActivity();
                }
            });

            pvHolder.imageButtonZero.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    cLinePropertyValue.currentLinePropertyValue = linePropertyValue;
                    double countDbl =  cLinePropertyValue.currentLinePropertyValue.quantityDbl;

                    if (cAppExtension.activity instanceof ReceiveorderLinePropertyInputActivity) {
                        ReceiveorderLinePropertyInputActivity  receiveorderLinePropertyInputActivity = (ReceiveorderLinePropertyInputActivity) cAppExtension.activity;


                        cLinePropertyValue.currentLinePropertyValue.quantityDbl --;
                        receiveorderLinePropertyInputActivity.pTryToChangeQuantity();
                        receiveorderLinePropertyInputActivity.pDeleteValueFromRecyler();
                        receiveorderLinePropertyInputActivity.pRefreshActivity();
                    }
                    if (cAppExtension.activity instanceof IntakeOrderLinePropertyInputActivity) {
                        IntakeOrderLinePropertyInputActivity  intakeOrderLinePropertyInputActivity = (IntakeOrderLinePropertyInputActivity) cAppExtension.activity;

                        cLinePropertyValue.currentLinePropertyValue.quantityDbl --;
                        intakeOrderLinePropertyInputActivity.pTryToChangeQuantity();
                        intakeOrderLinePropertyInputActivity.pDeleteValueFromRecyler();
                        intakeOrderLinePropertyInputActivity.pRefreshActivity();
                    }
                    if (cAppExtension.activity instanceof InventoryLinePropertyInputActivity) {
                        InventoryLinePropertyInputActivity  inventoryLinePropertyInputActivity = (InventoryLinePropertyInputActivity) cAppExtension.activity;

                        cLinePropertyValue.currentLinePropertyValue.quantityDbl --;
                        inventoryLinePropertyInputActivity.pTryToChangeQuantity();
                        inventoryLinePropertyInputActivity.pDeleteValueFromRecyler();
                        inventoryLinePropertyInputActivity.pRefreshActivity();
                    }
                    if (cAppExtension.activity instanceof MoveLineItemPropertyActivity) {
                        MoveLineItemPropertyActivity  moveLineItemPropertyActivity = (MoveLineItemPropertyActivity) cAppExtension.activity;

                        cLinePropertyValue.currentLinePropertyValue.quantityDbl --;
                        moveLineItemPropertyActivity.pTryToChangeQuantity(true, true, 0);
                        moveLineItemPropertyActivity.pDeleteValueFromRecyler();
                        moveLineItemPropertyActivity.pRefreshActivity();
                    }
                    if (cAppExtension.activity instanceof PickorderLineItemPropertyInputActvity){
                        PickorderLineItemPropertyInputActvity pickorderLineItemPropertyInputActvity = (PickorderLineItemPropertyInputActvity) cAppExtension.activity;
                        while (countDbl > 0) {

                            countDbl--;
                        }
                        if(cPickorderLine.currentPickOrderLine.currentContainer != null){
                            cPickorderLine.currentPickOrderLine.currentContainer.quantityHandledDbl  = 0.0;
                        }

                        cLinePropertyValue.currentLinePropertyValue.quantityDbl --;
                        pickorderLineItemPropertyInputActvity.pTryToChangePickedQuantity();
                        pickorderLineItemPropertyInputActvity.pDeleteValueFromRecyler();
                        pickorderLineItemPropertyInputActvity.pRefreshActivity();

                    }
                }
            });

            pvHolder.imageButtonManual.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    cLinePropertyValue.currentLinePropertyValue = linePropertyValue;
                    cLinePropertyValue.currentLinePropertyValue.quantityDbl = 0;

                    if (cAppExtension.activity instanceof ReceiveorderLinePropertyInputActivity) {
                        ReceiveorderLinePropertyInputActivity receiveorderLinePropertyInputActivity = (ReceiveorderLinePropertyInputActivity) cAppExtension.activity;
                        receiveorderLinePropertyInputActivity.pShowNumericInputFragment();
                    }
                    if (cAppExtension.activity instanceof IntakeOrderLinePropertyInputActivity) {
                        IntakeOrderLinePropertyInputActivity intakeOrderLinePropertyInputActivity = (IntakeOrderLinePropertyInputActivity) cAppExtension.activity;
                        intakeOrderLinePropertyInputActivity.pShowNumericInputFragment();
                    }
                    if (cAppExtension.activity instanceof InventoryLinePropertyInputActivity) {
                        InventoryLinePropertyInputActivity inventoryLinePropertyInputActivity = (InventoryLinePropertyInputActivity) cAppExtension.activity;
                        inventoryLinePropertyInputActivity.pShowNumericInputFragment();
                    }
                    if (cAppExtension.activity instanceof MoveLineItemPropertyActivity) {
                        MoveLineItemPropertyActivity moveLineItemPropertyActivity = (MoveLineItemPropertyActivity) cAppExtension.activity;
                        moveLineItemPropertyActivity.pShowNumericInputFragment();
                    }
                    if (cAppExtension.activity instanceof PickorderLineItemPropertyInputActvity){
                        PickorderLineItemPropertyInputActvity pickorderLineItemPropertyInputActvity = (PickorderLineItemPropertyInputActvity) cAppExtension.activity;
                        pickorderLineItemPropertyInputActvity.pShowNumericInputFragment();
                    }
                }
            });

            pvHolder.imageViewPropertyType.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    cLinePropertyValue.currentLinePropertyValue = linePropertyValue;
                    cLineProperty.currentLineProperty = cLinePropertyValue.currentLinePropertyValue.getLineProperty();

                    String propertyValueStr = linePropertyValue.getItemProperty().getValueTypeStr().toUpperCase();

                    if(propertyValueStr.equals("BOOLEAN") || propertyValueStr.equals("DECIMAL") || propertyValueStr.equals("TEXT") || propertyValueStr.equals("CODE")){
                        if (cAppExtension.activity instanceof ReceiveorderLinePropertyInputActivity) {
                            ReceiveorderLinePropertyInputActivity receiveorderLinePropertyInputActivity = (ReceiveorderLinePropertyInputActivity) cAppExtension.activity;
                            receiveorderLinePropertyInputActivity.pShowTextInputFragment();
                        }
                        if (cAppExtension.activity instanceof IntakeOrderLinePropertyInputActivity) {
                            IntakeOrderLinePropertyInputActivity intakeOrderLinePropertyInputActivity = (IntakeOrderLinePropertyInputActivity) cAppExtension.activity;
                            intakeOrderLinePropertyInputActivity.pShowTextInputFragment();
                        }
                        if (cAppExtension.activity instanceof PickorderLineItemPropertyInputActvity){
                            PickorderLineItemPropertyInputActvity pickorderLineItemPropertyInputActvity = (PickorderLineItemPropertyInputActvity) cAppExtension.activity;
                            pickorderLineItemPropertyInputActvity.pShowTextInputFragment();
                        }
                        if (cAppExtension.activity instanceof InventoryLinePropertyInputActivity){
                            InventoryLinePropertyInputActivity inventoryLinePropertyInputActivity = (InventoryLinePropertyInputActivity) cAppExtension.activity;
                            inventoryLinePropertyInputActivity.pShowTextInputFragment();
                        }
                        if (cAppExtension.activity instanceof MoveLineItemPropertyActivity){
                            MoveLineItemPropertyActivity moveLineItemPropertyActivity = (MoveLineItemPropertyActivity) cAppExtension.activity;
                            moveLineItemPropertyActivity.pShowTextInputFragment();
                        }
                    }
                    if(propertyValueStr.equals("DATE")){
                        if (cAppExtension.activity instanceof ReceiveorderLinePropertyInputActivity) {
                            ReceiveorderLinePropertyInputActivity receiveorderLinePropertyInputActivity = (ReceiveorderLinePropertyInputActivity) cAppExtension.activity;
                            receiveorderLinePropertyInputActivity.pShowDatePickerDialog();
                        }
                        if (cAppExtension.activity instanceof IntakeOrderLinePropertyInputActivity) {
                            IntakeOrderLinePropertyInputActivity intakeOrderLinePropertyInputActivity = (IntakeOrderLinePropertyInputActivity) cAppExtension.activity;
                            intakeOrderLinePropertyInputActivity.pShowDatePickerDialog();
                        }
                        if (cAppExtension.activity instanceof PickorderLineItemPropertyInputActvity){
                            PickorderLineItemPropertyInputActvity pickorderLineItemPropertyInputActvity = (PickorderLineItemPropertyInputActvity) cAppExtension.activity;
                            pickorderLineItemPropertyInputActvity.pShowDatePickerDialog();
                        }
                        if (cAppExtension.activity instanceof InventoryLinePropertyInputActivity){
                            InventoryLinePropertyInputActivity inventoryLinePropertyInputActivity = (InventoryLinePropertyInputActivity) cAppExtension.activity;
                            inventoryLinePropertyInputActivity.pShowDatePickerDialog();
                        }
                        if (cAppExtension.activity instanceof MoveLineItemPropertyActivity){
                            MoveLineItemPropertyActivity moveLineItemPropertyActivity = (MoveLineItemPropertyActivity) cAppExtension.activity;
                            moveLineItemPropertyActivity.pShowDatePickerDialog();
                        }
                    }
                }
            });
        }
    }

    public void pFillData(List<cLinePropertyValue> pvDataObl) {
        this.localItemPropertyValueObl = pvDataObl;
        notifyDataSetChanged();
    }

    private LinkedHashMap<String, ArrayList<cLinePropertyValue>> localItemPropertySortObl(){
        LinkedHashMap<String, ArrayList<cLinePropertyValue>> linkedHashMap = new LinkedHashMap<>();;

        for (cLinePropertyValue linePropertyValue : localItemPropertyValueObl) {
            //Create the hashmap dynammically and fill it
            ArrayList<cLinePropertyValue> loopList = linkedHashMap.get(linePropertyValue.getPropertyCodeStr());
            if (loopList == null) {
                ArrayList<cLinePropertyValue> propertyValues = new ArrayList<>();
                propertyValues.add(linePropertyValue);
                linkedHashMap.put(linePropertyValue.getPropertyCodeStr(), propertyValues);
            }
            else{
                loopList.add(linePropertyValue);
            }

        }
        return linkedHashMap;
    }

    @Override
    public int getItemCount () {
        if (this.localItemPropertyValueObl != null)
            return this.localItemPropertyValueObl.size();
        else return 0;
    }
}
