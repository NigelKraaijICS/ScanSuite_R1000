package nl.icsvertex.scansuite.Fragments.Dialogs;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.NumberPicker;

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

    private static NumberPicker quantityNumberPicker;
    private static int currentQuantity;
    private static Double maxQuantityDbl;
    private static int maxQuantityInt;

    private static ImageButton imageButtonPlus;
    private static ImageButton imageButtonMinus;

    private static Button buttonPlus5;
    private static Button buttonPlus10;
    private static Button buttonMax;
    private static Button buttonMinus5;
    private static Button buttonMinus10;
    private static Button buttonMin;

    private static Button doneButton;
    private static Button cancelButton;

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
            NumberpickerFragment.currentQuantity = args.getInt(cPublicDefinitions.NUMBERINTENT_CURRENTQUANTITY, 0);
            NumberpickerFragment.maxQuantityDbl = args.getDouble(cPublicDefinitions.NUMBERINTENT_MAXQUANTITY, 0);
        }

        NumberpickerFragment.maxQuantityInt = (int) Math.round(NumberpickerFragment.maxQuantityDbl);
        if (NumberpickerFragment.maxQuantityInt > NumberpickerFragment.maxQuantityDbl) {
            NumberpickerFragment.maxQuantityInt -= -1;
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
            NumberpickerFragment.quantityNumberPicker = getView().findViewById(R.id.quantityNumberPicker);
            NumberpickerFragment.imageButtonPlus = getView().findViewById(R.id.imageButtonPlus);
            NumberpickerFragment.imageButtonMinus = getView().findViewById(R.id.imageButtonMinus);

            NumberpickerFragment.buttonPlus5 = getView().findViewById(R.id.buttonPlus5);
            NumberpickerFragment.buttonPlus10 = getView().findViewById(R.id.buttonPlus10);
            NumberpickerFragment.buttonMax = getView().findViewById(R.id.buttonMax);
            NumberpickerFragment.buttonMinus5 = getView().findViewById(R.id.buttonMinus5);
            NumberpickerFragment.buttonMinus10 = getView().findViewById(R.id.buttonMinus10);
            NumberpickerFragment.buttonMin = getView().findViewById(R.id.buttonMin);

            NumberpickerFragment.doneButton = getView().findViewById(R.id.doneButton);
            NumberpickerFragment.cancelButton = getView().findViewById(R.id.cancelButton);
        }


    }

     @Override
    public void mFieldsInitialize() {
         NumberpickerFragment.quantityNumberPicker.setMaxValue(maxQuantityInt);
         NumberpickerFragment.quantityNumberPicker.setValue(currentQuantity);
    }

    @Override
    public void mSetListeners() {
        this.mSetNumberListeners();
        this.mSetNumpadListeners();
        this.mSetDoneListener();
        this. mSetCancelListener();
    }

    private void mSetDoneListener() {
        NumberpickerFragment.doneButton.setOnClickListener(new View.OnClickListener() {
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
        NumberpickerFragment.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    private void mSetNumberListeners() {
        NumberpickerFragment.imageButtonPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NumberpickerFragment.quantityNumberPicker.getValue() == quantityNumberPicker.getMaxValue()) {
                    cUserInterface.pDoNope(NumberpickerFragment.quantityNumberPicker, true, true);
                }
                else {
                    changeValueByOne(NumberpickerFragment.quantityNumberPicker, true);
                }
            }
        });
        NumberpickerFragment.imageButtonMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NumberpickerFragment.quantityNumberPicker.getValue() < 1) {
                    cUserInterface.pDoNope(NumberpickerFragment.quantityNumberPicker, true, true);
                }
                else {
                    changeValueByOne(NumberpickerFragment.quantityNumberPicker, false);
                }
            }
        });
    }

    private void mSetNumpadListeners() {
        NumberpickerFragment.buttonPlus5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNumberPickerChange(5,true);
            }
        });

        NumberpickerFragment.buttonPlus10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNumberPickerChange(10,true);
            }
        });

        NumberpickerFragment.buttonMax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NumberpickerFragment.quantityNumberPicker.setValue(quantityNumberPicker.getMaxValue());
            }
        });

        NumberpickerFragment.buttonMinus5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNumberPickerChange(5,false);
            }
        });

        NumberpickerFragment.buttonMinus10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNumberPickerChange(10,false);
            }
        });

        NumberpickerFragment.buttonMin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NumberpickerFragment.quantityNumberPicker.setValue(quantityNumberPicker.getMinValue());
            }
        });

    }

    private void mNumberPickerChange(int changeValue, boolean isMore) {
        int currentValue = NumberpickerFragment.quantityNumberPicker.getValue();
        int maxValue = NumberpickerFragment.quantityNumberPicker.getMaxValue();
        int minValue = NumberpickerFragment.quantityNumberPicker.getMinValue();
        int newValue;

        if (isMore) {
            //plus
            newValue = currentValue + changeValue;
            if (newValue > maxValue) {
                NumberpickerFragment.quantityNumberPicker.setValue(maxValue);
            }
            else {
                NumberpickerFragment.quantityNumberPicker.setValue(newValue);
            }
        }
        else {
            //minus
            if (changeValue > currentValue) {
                NumberpickerFragment.quantityNumberPicker.setValue(minValue);
            }
            else {
                newValue = currentValue - changeValue;
                NumberpickerFragment.quantityNumberPicker.setValue(newValue);
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
