package nl.icsvertex.scansuite.fragments.dialogs;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.NumberPicker;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import ICS.Interfaces.iICSDefaultFragment;
import SSU_WHS.General.cAppExtension;
import SSU_WHS.General.cPublicDefinitions;
import ICS.Utils.cUserInterface;
import nl.icsvertex.scansuite.R;

public class NumberpickerFragment extends DialogFragment implements iICSDefaultFragment {

    NumberPicker quantityNumberPicker;
    int currentQuantity;
    Double maxQuantityDbl;
    int maxQuantityInt;

    ImageButton imageButtonPlus;
    ImageButton imageButtonMinus;

    Button buttonPlus5;
    Button buttonPlus10;
    Button buttonMax;
    Button buttonMinus5;
    Button buttonMinus10;
    Button buttonMin;

    Button doneButton;
    Button cancelButton;

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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        currentQuantity = args.getInt(cPublicDefinitions.NUMBERINTENT_CURRENTQUANTITY, 0);
        maxQuantityDbl = args.getDouble(cPublicDefinitions.NUMBERINTENT_MAXQUANTITY, 0);
        maxQuantityInt = (int) Math.round(maxQuantityDbl);
        if (maxQuantityInt > maxQuantityDbl) {
            maxQuantityInt = maxQuantityInt-1;
        }
        mFragmentInitialize();
    }
    @Override
    public void mFragmentInitialize() {
        mFindViews();
        mSetViewModels();
        mFieldsInitialize();
        mSetListeners();
    }

    @Override
    public void mFindViews() {
        quantityNumberPicker = getView().findViewById(R.id.quantityNumberPicker);
        imageButtonPlus = getView().findViewById(R.id.imageButtonPlus);
        imageButtonMinus = getView().findViewById(R.id.imageButtonMinus);

        buttonPlus5 = getView().findViewById(R.id.buttonPlus5);
        buttonPlus10 = getView().findViewById(R.id.buttonPlus10);
        buttonMax = getView().findViewById(R.id.buttonMax);
        buttonMinus5 = getView().findViewById(R.id.buttonMinus5);
        buttonMinus10 = getView().findViewById(R.id.buttonMinus10);
        buttonMin = getView().findViewById(R.id.buttonMin);

        doneButton = getView().findViewById(R.id.doneButton);
        cancelButton = getView().findViewById(R.id.cancelButton);
    }

    @Override
    public void mSetViewModels() {

    }

    @Override
    public void mFieldsInitialize() {
//this order is important
        quantityNumberPicker.setMaxValue(maxQuantityInt);
        quantityNumberPicker.setValue(currentQuantity);
    }

    @Override
    public void mSetListeners() {
        mSetNumberListeners();
        mSetNumpadListeners();
        mSetDoneListener();
        mSetCancelListener();
    }

    private void mSetDoneListener() {
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int myValue = quantityNumberPicker.getValue();
                Intent intent = new Intent(cPublicDefinitions.NUMBERINTENT_NUMBER);
                intent.putExtra(cPublicDefinitions.NUMBERINTENT_EXTRANUMBER, myValue);
                LocalBroadcastManager.getInstance(cAppExtension.context).sendBroadcast(intent);
                dismiss();
            }
        });

    }
    private void mSetCancelListener() {
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    private void mSetNumberListeners() {
        imageButtonPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (quantityNumberPicker.getValue() == quantityNumberPicker.getMaxValue()) {
                    cUserInterface.doNope(quantityNumberPicker, true, true);
                }
                else {
                    changeValueByOne(quantityNumberPicker, true);
                }
            }
        });
        imageButtonMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (quantityNumberPicker.getValue() < 1) {
                    cUserInterface.doNope(quantityNumberPicker, true, true);
                }
                else {
                    changeValueByOne(quantityNumberPicker, false);
                }
            }
        });
    }

    private void mSetNumpadListeners() {
        buttonPlus5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_numberPickerChange(5,true);
            }
        });
        buttonPlus10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_numberPickerChange(10,true);
            }
        });
        buttonMax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quantityNumberPicker.setValue(quantityNumberPicker.getMaxValue());
            }
        });
        buttonMinus5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_numberPickerChange(5,false);
            }
        });
        buttonMinus10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_numberPickerChange(10,false);
            }
        });
        buttonMin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quantityNumberPicker.setValue(quantityNumberPicker.getMinValue());
            }
        });
    }

    private void m_numberPickerChange(int changeValue, boolean isMore) {
        int currentValue = quantityNumberPicker.getValue();
        int maxValue = quantityNumberPicker.getMaxValue();
        int minValue = quantityNumberPicker.getMinValue();
        int newValue;

        if (isMore) {
            //plus
            newValue = currentValue + changeValue;
            if (newValue > maxValue) {
                quantityNumberPicker.setValue(maxValue);
            }
            else {
                quantityNumberPicker.setValue(newValue);
            }
        }
        else {
            //minus
            if (changeValue > currentValue) {
                quantityNumberPicker.setValue(minValue);
            }
            else {
                newValue = currentValue - changeValue;
                quantityNumberPicker.setValue(newValue);
            }
        }
    }

    private void changeValueByOne(final NumberPicker higherPicker, final boolean increment) {

        Method method;
        try {
            // reflection call for
            // higherPicker.changeValueByOne(true);
            method = higherPicker.getClass().getDeclaredMethod("changeValueByOne", boolean.class);
            method.setAccessible(true);
            method.invoke(higherPicker, increment);

        } catch (final NoSuchMethodException e) {
            e.printStackTrace();
        } catch (final IllegalArgumentException e) {
            e.printStackTrace();
        } catch (final IllegalAccessException e) {
            e.printStackTrace();
        } catch (final InvocationTargetException e) {
            e.printStackTrace();
        }
    }


}
