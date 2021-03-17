package nl.icsvertex.scansuite.Fragments.Dialogs;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ICS.Interfaces.iICSDefaultFragment;
import ICS.Utils.cRegex;
import ICS.Utils.cText;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Basics.BarcodeLayouts.cBarcodeLayout;
import SSU_WHS.Basics.LabelTemplate.cLabelTemplate;
import SSU_WHS.Basics.PrintBinLabel.cPrintBinLabel;
import SSU_WHS.Basics.Users.cUser;
import SSU_WHS.Basics.Workplaces.cWorkplace;
import SSU_WHS.General.cPublicDefinitions;
import SSU_WHS.Intake.IntakeorderMATLineSummary.cIntakeorderMATSummaryLine;
import SSU_WHS.Intake.Intakeorders.cIntakeorder;
import SSU_WHS.Inventory.InventoryorderBins.cInventoryorderBin;
import SSU_WHS.Inventory.InventoryorderLines.cInventoryorderLine;
import SSU_WHS.Move.Moveorders.cMoveorder;
import SSU_WHS.Picken.PickorderLines.cPickorderLine;
import SSU_WHS.Receive.ReceiveLines.cReceiveorderLine;
import SSU_WHS.Receive.ReceiveSummaryLine.cReceiveorderSummaryLine;
import nl.icsvertex.scansuite.Activities.Intake.IntakeOrderIntakeActivity;
import nl.icsvertex.scansuite.Activities.Intake.IntakeOrderIntakeGeneratedActivity;
import nl.icsvertex.scansuite.Activities.Inventory.InventoryArticleActivity;
import nl.icsvertex.scansuite.Activities.Inventory.InventoryorderBinActivity;
import nl.icsvertex.scansuite.Activities.Move.MoveLinePlaceActivity;
import nl.icsvertex.scansuite.Activities.Move.MoveLinePlaceGeneratedActivity;
import nl.icsvertex.scansuite.Activities.Move.MoveLinePlaceMTActivity;
import nl.icsvertex.scansuite.Activities.Move.MoveLineTakeActivity;
import nl.icsvertex.scansuite.Activities.Move.MoveLineTakeMTActivity;
import nl.icsvertex.scansuite.Activities.Pick.PickorderPickActivity;
import nl.icsvertex.scansuite.Activities.Pick.PickorderPickGeneratedActivity;
import nl.icsvertex.scansuite.Activities.Receive.ReceiveOrderReceiveActivity;
import nl.icsvertex.scansuite.R;

public class PrintBinLabelFragment extends DialogFragment implements iICSDefaultFragment {

    //Region Private Properties

    private ConstraintLayout lineBinPrint;
    private TextView textViewTitle;
    private ImageView barcodeImage;
    private Spinner workplaceSpinner;
    private Spinner layoutSpinner;

    private TextView quantityText;
    private TextView quantityRequiredText;
    private AppCompatImageButton imageButtonMinus;
    private AppCompatImageButton imageButtonPlus;

    private  int counterMinusHelperInt;
    private  int counterPlusHelperInt;
    private Handler minusHandler;
    private  Handler plusHandler;

    private Button printButton;
    private  Button cancelButton;
    private Long amountLng;
    private String binCodeStr;

    //End Region private Properties

    //Region Constructor
    public PrintBinLabelFragment() {
    }
    //End Region Constructor

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_line_bin_print, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        cAppExtension.dialogFragment = this;
        this.mFragmentInitialize();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = WindowManager.LayoutParams.WRAP_CONTENT;
        Objects.requireNonNull(Objects.requireNonNull(getDialog()).getWindow()).setLayout(width, height);
    }

    public void onBackPressed(){
        cAppExtension.dialogFragment.dismiss();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem pvMenuItem) {
            return super.onOptionsItemSelected(pvMenuItem);
    }

    @Override
    public void mFragmentInitialize() {

        this.mFindViews();

        this.mFieldsInitialize();

        this.mSetToolbar();

        this.mSetListeners();

    }


    @Override
    public void mFindViews() {

        this.textViewTitle = getView().findViewById(R.id.textViewTitle);
        this.lineBinPrint = getView().findViewById(R.id.lineBinPrint);
        this.workplaceSpinner = getView().findViewById(R.id.workspaceSpinner);
        this.layoutSpinner = getView().findViewById(R.id.layoutSpinner);
        this.printButton = getView().findViewById(R.id.printButton);
        this.cancelButton = getView().findViewById(R.id.cancelButton);
        this.quantityText = getView().findViewById(R.id.quantityText);
        this.quantityRequiredText = getView().findViewById(R.id.quantityRequiredText);
        this.barcodeImage = getView().findViewById(R.id.barcodeImageView);
        this.imageButtonMinus = getView().findViewById(R.id.imageButtonMinus);
        this.imageButtonPlus = getView().findViewById(R.id.imageButtonPlus);
    }


    public void mSetToolbar() {
        this.textViewTitle.setText(this.binCodeStr);
        this.textViewTitle.setSelected(true);
    }

    @Override
    public void mFieldsInitialize() {
        if (cAppExtension.activity instanceof PickorderPickActivity) {
            this.binCodeStr = cPickorderLine.currentPickOrderLine.getBinCodeStr();
        }
        if (cAppExtension.activity instanceof PickorderPickGeneratedActivity) {
            this.binCodeStr = cPickorderLine.currentPickOrderLine.getBinCodeStr();
        }
        if (cAppExtension.activity instanceof InventoryArticleActivity) {
            this.binCodeStr = cInventoryorderLine.currentInventoryOrderLine.getBinCodeStr();
        }
        if (cAppExtension.activity instanceof InventoryorderBinActivity) {
            this.binCodeStr = cInventoryorderBin.currentInventoryOrderBin.getBinCodeStr();
        }
        if (cAppExtension.activity instanceof MoveLinePlaceActivity) {
            this.binCodeStr = cMoveorder.currentMoveOrder.currentBranchBin.getBinCodeStr();
        }
        if (cAppExtension.activity instanceof MoveLinePlaceGeneratedActivity) {
            this.binCodeStr = cMoveorder.currentMoveOrder.currentBranchBin.getBinCodeStr();
        }
        if (cAppExtension.activity instanceof MoveLineTakeActivity) {
            this.binCodeStr = cMoveorder.currentMoveOrder.currentBranchBin.getBinCodeStr();
        }
        if (cAppExtension.activity instanceof MoveLinePlaceMTActivity) {
            this.binCodeStr = cMoveorder.currentMoveOrder.currentBranchBin.getBinCodeStr();
        }
        if (cAppExtension.activity instanceof MoveLineTakeMTActivity) {
            this.binCodeStr = cMoveorder.currentMoveOrder.currentBranchBin.getBinCodeStr();
        }
        if (cAppExtension.activity instanceof IntakeOrderIntakeActivity){
            this.binCodeStr = cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getBinCodeStr();
        }
        if (cAppExtension.activity instanceof IntakeOrderIntakeGeneratedActivity){
            this.binCodeStr = cIntakeorderMATSummaryLine.currentIntakeorderMATSummaryLine.getBinCodeStr();
        }
        if (cAppExtension.activity instanceof ReceiveOrderReceiveActivity){
            this.binCodeStr = cReceiveorderSummaryLine.currentReceiveorderSummaryLine.getBinCodeStr();
        }
        this.mShowWorkPlaceSpinner();
        this.mShowLabelTemplateSpinner();
        this.amountLng = 1L;
        this.mShowQuantityInfo();

        cBarcodeLayout barcodeLayout;
        barcodeLayout = cBarcodeLayout.pGetBarcodeLayoutByEnumerate(cBarcodeLayout.barcodeLayoutEnu.BIN);

        String binCodeStr;
        binCodeStr = cRegex.pGetWholePrefixFromLayout(barcodeLayout.getLayoutValueStr()) + this.binCodeStr;

        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(binCodeStr , BarcodeFormat.CODE_128, 250, 72);
            Bitmap bitmap = Bitmap.createBitmap(250, 72, Bitmap.Config.RGB_565);
            for(int i = 0; i< 250; i++){
                for(int j =  0; j< 72; j++){
                    bitmap.setPixel(i,j,bitMatrix.get(i,j)? Color.BLACK:Color.WHITE);
                }
            }
            this.barcodeImage.setImageBitmap(bitmap);

        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void mSetListeners() {
        this.mSetPrintListener();
        this.mSetCancelListener();
        this.mSetWorkplaceSpinnerListener();
        this.mSetLayoutSpinnerListener();
        this.mSetMinusListener();
        this.mSetPlusListener();
        this.mSetNumberListener();
    }

    private void mSetCancelListener() {
        this.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View pvView) {
         cAppExtension.dialogFragment.dismiss();
            }
        });
    }

    private void mSetPrintListener() {
        this.printButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View pvView) {
                if (cWorkplace.currentWorkplace == null | cLabelTemplate.currentLabelTemplate == null){
                    return;
                }
                mPrintBinLabel();
            }
        });
    }
    private void mPrintBinLabel(){
        cPrintBinLabel printBinLabel;
        printBinLabel = new cPrintBinLabel(this.binCodeStr,  this.amountLng);
       if(printBinLabel.pPrintBinLabelViaWebserviceBln()){
           cAppExtension.dialogFragment.dismiss();
       }
    }

    private void mShowLabelTemplateSpinner() {

        if (cLabelTemplate.binTemplateObl == null ||  cLabelTemplate.binTemplateObl.size() <= 0) {
            this.layoutSpinner.setVisibility(View.GONE);
            return;
        }

        this.layoutSpinner.setVisibility(View.VISIBLE);
        this.mFillTemplateSpinner();
    }


    private void mFillTemplateSpinner() {

        if (cLabelTemplate.binTemplateObl == null ||  cLabelTemplate.binTemplateObl.size() <= 0 ) {
            return;
        }

        List<String> labelTemplateObl = new ArrayList<>();
        for (cLabelTemplate labelTemplate :cLabelTemplate.binTemplateObl ) {
            labelTemplateObl.add(labelTemplate.getTemplateStr());
        }


        ArrayAdapter<String> adapter = new ArrayAdapter<>(cAppExtension.context,
                android.R.layout.simple_spinner_dropdown_item,
                labelTemplateObl);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        this.layoutSpinner.setAdapter(adapter);
    }

    private void mSetLayoutSpinnerListener() {
        this.layoutSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                cLabelTemplate.currentLabelTemplate = cLabelTemplate.pGetLabelTemplateByName(cLabelTemplate.binTemplateObl ,layoutSpinner.getSelectedItem().toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });
    }

    private void mShowWorkPlaceSpinner() {

        if (cWorkplace.allWorkplacesObl  == null || cWorkplace.allWorkplacesObl.size() == 0) {
            this.workplaceSpinner.setVisibility(View.GONE);
            return;
        }

        this.workplaceSpinner.setVisibility(View.VISIBLE);
        this.mFillWorkplaceSpinner();
    }


    private void mFillWorkplaceSpinner() {

        if (cWorkplace.allWorkplacesObl == null ||  cWorkplace.allWorkplacesObl.size() <= 0 ) {
            return;
        }

        List<String> workPlaceObl = new ArrayList<>();

        if (cUser.currentUser.currentBranch.workplacesObl().size() >= 1) {
            for (cWorkplace workplace :cUser.currentUser.currentBranch.workplacesObl() ) {
                workPlaceObl.add(workplace.getWorkplaceStr());
            }
        }
        else
        {
            for (cWorkplace workplace :cWorkplace.allWorkplacesObl ) {
                workPlaceObl.add(workplace.getWorkplaceStr());
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(cAppExtension.context,
                android.R.layout.simple_spinner_dropdown_item,
                workPlaceObl);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        this.workplaceSpinner.setAdapter(adapter);
        if (cWorkplace.currentWorkplace != null)
        { this.workplaceSpinner.setSelection(adapter.getPosition(cWorkplace.currentWorkplace.getWorkplaceStr()));}
    }

    private void mSetWorkplaceSpinnerListener() {
        this.workplaceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                cWorkplace.currentWorkplace = cWorkplace.pGetWorkplaceByName(workplaceSpinner.getSelectedItem().toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });
    }

    //Region Private Methods

    private void mTryToChangeQuantity(Boolean pvIsPositiveBln, Boolean pvAmountFixedBln, Integer pvAmountInt) {

        if (pvIsPositiveBln) {

            //Determine the new amount
            this.amountLng += pvAmountInt;
            this.quantityText.setText(cText.pLongToStringStr(this.amountLng));
            return;
        }

        //negative
        //Check if value already is zero
        if (this.amountLng <= 1 ) {
            cUserInterface.pDoNope(this.quantityText, true, true);
            return;
        }
        //Determine the new amount
        this.amountLng -= pvAmountInt;
               //Change quantityDbl in activity
        this.quantityText.setText(cText.pLongToStringStr(this.amountLng));

    }

    private void mShowQuantityInfo() {

        this.quantityText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        this.quantityText.setSelectAllOnFocus(true);
        this.quantityText.requestFocus();
        this.quantityText.setSingleLine();
        this.quantityText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_TEXT_VARIATION_NORMAL);
        this.quantityText.setCursorVisible(false);

        this.quantityText.setText(cText.pLongToStringStr(this.amountLng));
        this.quantityRequiredText.setVisibility(View.INVISIBLE);
    }

    private void mNumberClicked() {
        this.mShowNumberPickerFragment();
    }
    //Region Number Broadcaster

    private void mShowNumberPickerFragment() {

        cUserInterface.pCheckAndCloseOpenDialogs();

        Bundle bundle = new Bundle();
        bundle.putLong(cPublicDefinitions.NUMBERINTENT_CURRENTQUANTITY, this.amountLng);
        bundle.putDouble(cPublicDefinitions.NUMBERINTENT_MAXQUANTITY,9999);

        NumberpickerFragment numberpickerFragment = new NumberpickerFragment();
        numberpickerFragment.setArguments(bundle);

        numberpickerFragment.show(cAppExtension.fragmentManager, cPublicDefinitions.NUMBERPICKERFRAGMENT_TAG);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void mSetPlusListener() {

        this.imageButtonPlus.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (plusHandler != null) return true;
                    plusHandler = new Handler();
                    plusHandler.postDelayed(mPlusAction, 750);
                }

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (plusHandler == null) return true;
                    plusHandler.removeCallbacks(mPlusAction);
                    plusHandler = null;
                    counterPlusHelperInt = 0;
                }

                return false;
            }
        });

        this.imageButtonPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTryToChangeQuantity(true, false, 1);
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void mSetMinusListener() {

        this.imageButtonMinus.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (minusHandler != null) return true;
                    minusHandler = new Handler();
                    minusHandler.postDelayed(mMinusAction, 750);
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (minusHandler == null) return true;
                    minusHandler.removeCallbacks(mMinusAction);
                    minusHandler = null;
                    counterMinusHelperInt = 0;
                }
                return false;
            }

        });

        this.imageButtonMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTryToChangeQuantity(false, false, 1);
            }
        });
    }

    private void mSetNumberListener() {
        this.quantityText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNumberClicked();
            }
        });

    }

    private final Runnable mMinusAction = new Runnable() {
        @Override
        public void run() {
            imageButtonMinus.performClick();
            long milliSecsLng;
            if (counterMinusHelperInt < 10) {
                milliSecsLng = 200;
            } else if (counterMinusHelperInt < 20) {
                milliSecsLng = 150;
            } else if (counterMinusHelperInt < 30) {
                milliSecsLng = 100;
            } else if (counterMinusHelperInt < 40) {
                milliSecsLng = 50;
            } else {
                milliSecsLng = 50;
            }
            mDoDelayedMinus(this, milliSecsLng);
        }
    };

    private final Runnable mPlusAction = new Runnable() {
        @Override
        public void run() {
            imageButtonPlus.performClick();
            long milliSecsLng;
            if (counterPlusHelperInt < 10) {
                milliSecsLng = 200;
            } else if (counterPlusHelperInt < 20) {
                milliSecsLng = 150;
            } else if (counterPlusHelperInt < 30) {
                milliSecsLng = 100;
            } else if (counterPlusHelperInt < 40) {
                milliSecsLng = 50;
            } else {
                milliSecsLng = 50;
            }
            mDoDelayedPlus(this, milliSecsLng);
        }
    };

    private final BroadcastReceiver mNumberReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int numberChosenInt = 0;
            Bundle extras = intent.getExtras();

            if (extras != null) {
                numberChosenInt = extras.getInt(cPublicDefinitions.NUMBERINTENT_EXTRANUMBER);
            }
            mHandleQuantityChosen(numberChosenInt);
        }
    };

    private void mDoDelayedMinus(Runnable pvRunnable, long pvMilliSecsLng) {
        this.minusHandler.postDelayed(pvRunnable, pvMilliSecsLng);
        this.counterMinusHelperInt += 1;
    }

    private void mDoDelayedPlus(Runnable pvRunnable, long pvMilliSecsLng) {
        this.plusHandler.postDelayed(pvRunnable, pvMilliSecsLng);
        this.counterPlusHelperInt += 1;
    }

    private void mHandleQuantityChosen(Integer pvQuantityInt) {
        this.mTryToChangeQuantity(pvQuantityInt != 0, true,pvQuantityInt);
    }

}
