package nl.icsvertex.scansuite.Fragments.Dialogs;


import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import ICS.Interfaces.iICSDefaultFragment;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.General.cPublicDefinitions;
import nl.icsvertex.scansuite.R;

public class NumberpickerFragment extends DialogFragment implements iICSDefaultFragment {

    private  NumberPicker quantityNumberPicker;
    private  int currentQuantity;
    private  Double maxQuantityDbl;
    private  int maxQuantityInt;

    private  ImageButton imageButtonPlus;
    private  ImageButton imageButtonMinus;

    private  Button buttonPlus5;
    private  Button buttonPlus10;
    private  Button buttonMax;
    private  Button buttonMinus5;
    private  Button buttonMinus10;
    private  Button buttonMin;

    private  Button doneButton;
    private  Button cancelButton;

    public NumberpickerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_numberpicker, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();

        if (args != null) {
            this.currentQuantity = args.getInt(cPublicDefinitions.NUMBERINTENT_CURRENTQUANTITY, 0);
            this.maxQuantityDbl = args.getDouble(cPublicDefinitions.NUMBERINTENT_MAXQUANTITY, 0);
        }

        this.maxQuantityInt = (int) Math.round(this.maxQuantityDbl);
        if (this.maxQuantityInt > this.maxQuantityDbl) {
            this.maxQuantityInt -= -1;
        }

        this.mFragmentInitialize();
    }

    @Override
    public void mFragmentInitialize() {
        this.mFindViews();
        this.mFieldsInitialize();
        this.mSetListeners();
        cUserInterface.pEnableScanner();
    }

    @Override
    public void mFindViews() {

        if (getView() != null) {
            this.quantityNumberPicker = getView().findViewById(R.id.quantityNumberPicker);
            this.imageButtonPlus = getView().findViewById(R.id.imageButtonPlus);
            this.imageButtonMinus = getView().findViewById(R.id.imageButtonMinus);

            this.buttonPlus5 = getView().findViewById(R.id.buttonPlus5);
            this.buttonPlus10 = getView().findViewById(R.id.buttonPlus10);
            this.buttonMax = getView().findViewById(R.id.buttonMax);
            this.buttonMinus5 = getView().findViewById(R.id.buttonMinus5);
            this.buttonMinus10 = getView().findViewById(R.id.buttonMinus10);
            this.buttonMin = getView().findViewById(R.id.buttonMin);

            this.doneButton = getView().findViewById(R.id.doneButton);
            this.cancelButton = getView().findViewById(R.id.cancelButton);
        }


    }

     @Override
    public void mFieldsInitialize() {
         this.quantityNumberPicker.setMaxValue(this.maxQuantityInt);
         this.quantityNumberPicker.setValue(this.currentQuantity);
    }

    @Override
    public void mSetListeners() {
        this.mSetNumberListeners();
        this.mSetNumpadListeners();
        this.mSetDoneListener();
        this.mSetCancelListener();
    }

    private void mSetDoneListener() {
        this.doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(cPublicDefinitions.NUMBERINTENT_NUMBER);
                intent.putExtra(cPublicDefinitions.NUMBERINTENT_EXTRANUMBER, quantityNumberPicker.getValue());
                LocalBroadcastManager.getInstance(cAppExtension.context).sendBroadcast(intent);
                dismiss();
            }
        });

    }
    private void mSetCancelListener() {
        this.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    private void mSetNumberListeners() {
        this.imageButtonPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (quantityNumberPicker.getValue() == quantityNumberPicker.getMaxValue()) {
                    cUserInterface.pDoNope(quantityNumberPicker, true, true);
                }
                else {
                    changeValueByOne(quantityNumberPicker, true);
                }
            }
        });
        this.imageButtonMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (quantityNumberPicker.getValue() < 1) {
                    cUserInterface.pDoNope(quantityNumberPicker, true, true);
                }
                else {
                    changeValueByOne(quantityNumberPicker, false);
                }
            }
        });
    }

    private void mSetNumpadListeners() {
        this.buttonPlus5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNumberPickerChange(5,true);
            }
        });

        this.buttonPlus10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNumberPickerChange(10,true);
            }
        });

        this.buttonMax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quantityNumberPicker.setValue(quantityNumberPicker.getMaxValue());
            }
        });

        this.buttonMinus5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNumberPickerChange(5,false);
            }
        });

        this.buttonMinus10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNumberPickerChange(10,false);
            }
        });

        this.buttonMin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quantityNumberPicker.setValue(quantityNumberPicker.getMinValue());
            }
        });

    }

    private void mNumberPickerChange(int changeValue, boolean isMore) {
        int currentValue = this.quantityNumberPicker.getValue();
        int maxValue = this.quantityNumberPicker.getMaxValue();
        int minValue = this.quantityNumberPicker.getMinValue();
        int newValue;

        if (isMore) {
            //plus
            newValue = currentValue + changeValue;
            this.quantityNumberPicker.setValue(Math.min(newValue, maxValue));
        }
        else {
            //minus
            if (changeValue > currentValue) {
                this.quantityNumberPicker.setValue(minValue);
            }
            else {
                newValue = currentValue - changeValue;
                this.quantityNumberPicker.setValue(newValue);
            }
        }
    }

    private void changeValueByOne(final NumberPicker higherPicker, final boolean increment) {

        Method method;
        try {
            // reflection call for
            method = higherPicker.getClass().getDeclaredMethod("changeValueByOne", boolean.class);
            method.setAccessible(true);
            method.invoke(higherPicker, increment);

        } catch (final NoSuchMethodException | IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
