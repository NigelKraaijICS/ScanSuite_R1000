package nl.icsvertex.scansuite.Fragments.Dialogs;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Objects;

import ICS.Interfaces.iICSDefaultFragment;
import ICS.Utils.Scanning.cBarcodeScan;
import ICS.Utils.cRegex;
import ICS.Utils.cText;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Picken.PickorderBarcodes.cPickorderBarcode;
import SSU_WHS.Picken.PickorderLineBarcodes.cPickorderLineBarcode;
import SSU_WHS.Picken.PickorderLineProperty.cPickorderLineProperty;
import SSU_WHS.Picken.PickorderLinePropertyValue.cPickorderLinePropertyValue;
import SSU_WHS.Picken.PickorderLinePropertyValue.cPickorderLinePropertyValueInputAdapter;
import SSU_WHS.Picken.PickorderLines.cPickorderLine;
import nl.icsvertex.scansuite.R;

public class ItemPropertyInputFragment extends DialogFragment implements iICSDefaultFragment {

   //Region Private Properties
   private  ImageView toolbarImage;
   private  TextView toolbarTitle;

   private TextView articleDescriptionText;
   private TextView articleDescription2Text;
   private TextView articleItemText;
   private TextView articleBarcodeText;
   private TextView textViewQuantity;

    private TextView textViewInstruction;
    private  RecyclerView itemPropertyRecyclerview;
    private  Button buttonCancel;
    private  Button buttonOK;
    private  List<cPickorderLinePropertyValue> localItemPropertyValueObl;

    private cPickorderLinePropertyValueInputAdapter pickorderLinePropertyValueInputAdapter;
    private cPickorderLinePropertyValueInputAdapter getPickorderLinePropertyValueInputAdapter(){
        if (this.pickorderLinePropertyValueInputAdapter == null) {
            this.pickorderLinePropertyValueInputAdapter = new cPickorderLinePropertyValueInputAdapter();
        }

        return  pickorderLinePropertyValueInputAdapter;
    }

    //End Region Private Properties


    //Region Constructor
    public ItemPropertyInputFragment() {
        // Required empty public constructor
    }

    public ItemPropertyInputFragment(List<cPickorderLinePropertyValue> pvDataObl) {
        this.localItemPropertyValueObl = pvDataObl;
    }
    //End Region Constructor

    //Region Default Methods

    @Nullable
    @Override
    public View onCreateView(LayoutInflater pvInflater, ViewGroup pvContainer, Bundle pvSavedInstanceState) {
        cBarcodeScan.pRegisterBarcodeFragmentReceiver(this.getClass().getSimpleName());
        return pvInflater.inflate(R.layout.fragment_itemproperty_input, pvContainer);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        cAppExtension.dialogFragment = this;
        this.mFragmentInitialize();
    }

    @Override
    public void onPause() {
        try {
            cBarcodeScan.pUnregisterBarcodeFragmentReceiver(this.getClass().getSimpleName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        super.onPause();
    }

    @Override
    public void onDestroy() {
        try {
            cBarcodeScan.pUnregisterBarcodeFragmentReceiver(this.getClass().getSimpleName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        cBarcodeScan.pRegisterBarcodeFragmentReceiver(this.getClass().getSimpleName());

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels - getResources().getDimensionPixelSize(R.dimen.default_double_margin);

        Objects.requireNonNull(Objects.requireNonNull(getDialog()).getWindow()).setLayout(width, height);
        cUserInterface.pEnableScanner();
    }

    //End Region Default Methods

    //Region iICSDefaultFragment methods

    @Override
    public void mFragmentInitialize() {
        this.mFindViews();
        this.mFieldsInitialize();
        this.mSetListeners();
        this.mSetToolbar();
        this.mSetItemPropertyValueRecycler();
    }

    @Override
    public void mFindViews() {
        if (getView() != null) {
            this.toolbarImage = getView().findViewById(R.id.toolbarImage);
            this.toolbarTitle = getView().findViewById(R.id.toolbarTitle);


            this.articleDescriptionText = getView().findViewById(R.id.articleDescriptionText);
            this.articleDescription2Text = getView().findViewById(R.id.articleDescription2Text);
            this.articleItemText = getView().findViewById(R.id.articleItemText);
            this.articleBarcodeText = getView().findViewById(R.id.articleBarcodeText);
            this.textViewQuantity = getView().findViewById(R.id.textViewQuantity);

            this.textViewInstruction  = getView().findViewById(R.id.textViewInstruction);
            this.itemPropertyRecyclerview = getView().findViewById(R.id.itemPropertyRecyclerview);
            this.buttonCancel = getView().findViewById(R.id.buttonCancel);
            this.buttonOK = getView().findViewById(R.id.buttonOK);
        }
    }

    @Override
    public void mFieldsInitialize() {

        StringBuilder InstructionStr;

            int indexInt = 0;
            InstructionStr = new StringBuilder(cAppExtension.activity.getString(R.string.message_please_enter_a) + " ");

            for (cPickorderLinePropertyValue pickorderLinePropertyValue : this.localItemPropertyValueObl){

                if (indexInt == 0) {
                    InstructionStr.append(pickorderLinePropertyValue.getItemProperty().getOmschrijvingStr());
                }
                else
                {
                    InstructionStr.append(" ").append(cAppExtension.activity.getString(R.string.message_and_a)).append(" ").append(pickorderLinePropertyValue.getItemProperty().getOmschrijvingStr());
                }
                indexInt += 1;
            }

        this.articleDescriptionText.setText(cPickorderLine.currentPickOrderLine.getDescriptionStr());
        this.articleDescription2Text.setText(cPickorderLine.currentPickOrderLine.getDescription2Str());
        this.articleItemText.setText(cPickorderLine.currentPickOrderLine.getItemNoAndVariantStr());
        this.articleBarcodeText.setText(cPickorderBarcode.currentPickorderBarcode.getBarcodeAndQuantityStr());
        this.textViewQuantity.setText(cText.pDoubleToStringStr(cPickorderLine.currentPickOrderLine.getQuantityDbl()));
        this.textViewInstruction.setText(InstructionStr.toString());
    }

    @Override
    public void mSetListeners() {
        this.mSetHeaderListener();
        this.mSetCloseListener();
    }

    //End Regioni ICSDefaultFragment methods

    //Region Public Methods
    public  void pHandleScan(cBarcodeScan pvBarcodeScan) {

        if (cPickorderLine.currentPickOrderLine.pickorderLinePropertyInputObl().size() == 1) {
            cPickorderLineProperty.currentPickorderLineProperty = cPickorderLine.currentPickOrderLine.pickorderLinePropertyInputObl().get(0);

            if (!cRegex.pCheckRegexBln( cPickorderLineProperty.currentPickorderLineProperty.getItemProperty().getLayoutStr(),pvBarcodeScan.getBarcodeOriginalStr())) {
                cUserInterface.pShowSnackbarMessage(this.itemPropertyRecyclerview,cAppExtension.activity.getString(R.string.message_unknown_barcode_for_this_line),R.raw.badsound, true);
                return;
            }

            cPickorderLineProperty.currentPickorderLineProperty.pValueAdded(pvBarcodeScan.getBarcodeOriginalStr());
            this.localItemPropertyValueObl = cPickorderLineProperty.currentPickorderLineProperty.propertyValueObl();
            this.mSetItemPropertyValueRecycler();
        }

    }

    //End Region Public Methods

    //Region Private Methods
    private void mSetToolbar() {

        this.toolbarTitle.setText(cAppExtension.activity.getString(R.string.screentitle_itemproperty_input));
        this.toolbarTitle.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        this.toolbarTitle.setSingleLine(true);
        this.toolbarTitle.setMarqueeRepeatLimit(5);
        this.toolbarTitle.postDelayed(new Runnable() {
            @Override
            public void run() {
                toolbarTitle.setSelected(true);
            }
        },1500);

        this.toolbarImage.setImageResource(R.drawable.ic_info);

    }
    private void mSetCloseListener() {

        this.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cAppExtension.dialogFragment.dismiss();
            }
        });

        this.buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cAppExtension.dialogFragment.dismiss();
            }
        });
    }

    private void mSetHeaderListener() {

        this.toolbarTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mScrollToBottom();
            }
        });

        this.toolbarTitle.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mScrollToTop();
                return true;
            }
        });
    }

    private void mScrollToTop() {
        this.itemPropertyRecyclerview.smoothScrollToPosition(0);
    }

    private void mScrollToBottom() {
        if (this.getPickorderLinePropertyValueInputAdapter()!= null) {
            if (this.getPickorderLinePropertyValueInputAdapter().getItemCount() > 0) {
                this.itemPropertyRecyclerview.smoothScrollToPosition(this.getPickorderLinePropertyValueInputAdapter().getItemCount() -1 );
            }
        }
    }


    private void mSetItemPropertyValueRecycler() {
        this.itemPropertyRecyclerview.setHasFixedSize(false);
        this.itemPropertyRecyclerview.setAdapter(this.getPickorderLinePropertyValueInputAdapter());
        this.itemPropertyRecyclerview.setLayoutManager(new LinearLayoutManager(cAppExtension.context));
        this.getPickorderLinePropertyValueInputAdapter().pFillData(this.localItemPropertyValueObl);
    }
    //End Region Private Methods





}
