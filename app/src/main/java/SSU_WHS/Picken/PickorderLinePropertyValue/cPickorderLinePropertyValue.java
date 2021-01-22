package SSU_WHS.Picken.PickorderLinePropertyValue;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.lifecycle.ViewModelProvider;

import org.json.JSONObject;

import java.util.ArrayList;

import ICS.cAppExtension;
import SSU_WHS.Basics.ItemProperty.cItemProperty;
import SSU_WHS.General.cDatabase;
import SSU_WHS.Picken.PickorderLineProperty.cPickorderLineProperty;
import SSU_WHS.Picken.PickorderLineProperty.cPickorderLinePropertyEntity;
import SSU_WHS.Picken.PickorderLineProperty.cPickorderLinePropertyViewModel;
import nl.icsvertex.scansuite.R;

public class cPickorderLinePropertyValue implements Parcelable {

    //Public Properties
    private int lineNoInt;

    protected cPickorderLinePropertyValue(Parcel in) {
        lineNoInt = in.readInt();
        propertyCodeStr = in.readString();
        valueStr = in.readString();
        sortingSequenceNoInt = in.readInt();
        quanitityDbl = in.readDouble();
    }

    public static final Creator<cPickorderLinePropertyValue> CREATOR = new Creator<cPickorderLinePropertyValue>() {
        @Override
        public cPickorderLinePropertyValue createFromParcel(Parcel in) {
            return new cPickorderLinePropertyValue(in);
        }

        @Override
        public cPickorderLinePropertyValue[] newArray(int size) {
            return new cPickorderLinePropertyValue[size];
        }
    };

    public int getLineNoInt() {return lineNoInt;}

    private String propertyCodeStr;
    public String getPropertyCodeStr() {return propertyCodeStr;}

    public cItemProperty getItemProperty() {

        if (this.getPropertyCodeStr().isEmpty() || cItemProperty.allItemPropertiesObl == null || cItemProperty.allItemPropertiesObl.size() == 0) {
            return  null;
        }

        for (cItemProperty itemProperty :  cItemProperty.allItemPropertiesObl) {
            if (itemProperty.getPropertyStr().equalsIgnoreCase(this.getPropertyCodeStr())) {
                return  itemProperty;
            }
        }

        return  null;

    }

    private String valueStr;
    public String getValueStr() {return valueStr;}

    private int sortingSequenceNoInt;
    public  int getSortingSequenceNoInt(){return sortingSequenceNoInt;}

    public double quanitityDbl;
    public double getQuanitityDbl() {return quanitityDbl;}

    private cPickorderLinePropertyValueEntity pickorderLinePropertyValueEntity;

    public static cPickorderLinePropertyValue currentPickorderLinePropertyValue;
    public static ArrayList<cPickorderLinePropertyValue> allLinePropertysValuesObl;

    private cPickorderLinePropertyValueViewModel getPickorderLinePropertyValueViewModel() {
        return new ViewModelProvider(cAppExtension.fragmentActivity).get(cPickorderLinePropertyValueViewModel.class);
    }

    public cPickorderLinePropertyValue(JSONObject pvJsonObject) {
        this.pickorderLinePropertyValueEntity = new cPickorderLinePropertyValueEntity(pvJsonObject);
        this.lineNoInt = this.pickorderLinePropertyValueEntity.getLineNoInt();
        this.propertyCodeStr = this.pickorderLinePropertyValueEntity.getPropertyCodeStr();
        this.valueStr = this.pickorderLinePropertyValueEntity.getValueStr();
        this.sortingSequenceNoInt = this.pickorderLinePropertyValueEntity.getSortingSequenceNoInt();
        this.quanitityDbl = 0;
    }

    public cPickorderLinePropertyValue(cPickorderLineProperty pvPickorderLineProperty) {
        this.pickorderLinePropertyValueEntity = null;

        this.lineNoInt = 0;
        this.propertyCodeStr = pvPickorderLineProperty.getPropertyCodeStr();
        this.valueStr = cAppExtension.activity.getString(R.string.novalueyet);
        this.sortingSequenceNoInt = 0;
        this.quanitityDbl = 0;
    }

    public cPickorderLinePropertyValue(int pvLineNoInt, String pvPropertyCodeStr, String pvValueStr) {
        this.pickorderLinePropertyValueEntity = null;
        this.lineNoInt = pvLineNoInt;
        this.propertyCodeStr = pvPropertyCodeStr;
        this.valueStr = pvValueStr;
        this.sortingSequenceNoInt = 10000;
        this.quanitityDbl = 1;
    }

    public boolean pInsertInDatabaseBln() {
        this.getPickorderLinePropertyValueViewModel().insert(this.pickorderLinePropertyValueEntity);

        if (cPickorderLinePropertyValue.allLinePropertysValuesObl == null) {
            cPickorderLinePropertyValue.allLinePropertysValuesObl = new ArrayList<>();
        }
        cPickorderLinePropertyValue.allLinePropertysValuesObl.add(this);
        return true;
    }

    public boolean pDeleteFromDatabaseBln() {
        if (cPickorderLinePropertyValue.allLinePropertysValuesObl != null) {
            cPickorderLinePropertyValue.allLinePropertysValuesObl.remove(this);
        }
        return true;
    }

    public static boolean pTruncateTableBln() {

        cPickorderLinePropertyValueViewModel pickorderLinePropertyValueViewModel =  new ViewModelProvider(cAppExtension.fragmentActivity).get(cPickorderLinePropertyValueViewModel.class);
        pickorderLinePropertyValueViewModel.deleteAll();
        cPickorderLinePropertyValue.allLinePropertysValuesObl = null;
        return true;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Bundle bundle = new Bundle();

        // insert the key value pairs to the bundle
        bundle.putInt(cDatabase.LINENO_NAMESTR, lineNoInt);
        bundle.putString(cDatabase.PROPERTYCODE_NAMESTR, propertyCodeStr);
        bundle.putString(cDatabase.VALUE_NAMESTR, valueStr);
        bundle.putInt(cDatabase.SORTINGSEQUENCENO_NAMESTR, sortingSequenceNoInt);
        bundle.putDouble(cDatabase.QUANTITY_NAMESTR, quanitityDbl);
        // write the key value pairs to the parcel
        dest.writeBundle(bundle);

    }
}
