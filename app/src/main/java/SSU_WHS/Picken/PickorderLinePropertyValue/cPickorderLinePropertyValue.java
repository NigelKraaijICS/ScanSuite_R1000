package SSU_WHS.Picken.PickorderLinePropertyValue;

import androidx.lifecycle.ViewModelProvider;

import org.json.JSONObject;

import java.util.ArrayList;

import ICS.cAppExtension;
import SSU_WHS.Basics.ItemProperty.cItemProperty;
import SSU_WHS.Picken.PickorderLineProperty.cPickorderLineProperty;
import nl.icsvertex.scansuite.R;

public class cPickorderLinePropertyValue{

    //Public Properties
    private int lineNoInt;

    public int getLineNoInt() {return lineNoInt;}

    private String propertyCodeStr;
    public String getPropertyCodeStr() {return propertyCodeStr;}

    public  cPickorderLineProperty getPickorderLineProperty(){

        if (this.getPropertyCodeStr().isEmpty() || cPickorderLineProperty.allLinePropertysObl == null || cPickorderLineProperty.allLinePropertysObl.size() == 0) {
            return  null;
        }

        for (cPickorderLineProperty pickorderLineProperty :cPickorderLineProperty.allLinePropertysObl ) {
            if (pickorderLineProperty.getLineNoInt().equals(this.getLineNoInt()) && pickorderLineProperty.getPropertyCodeStr().equalsIgnoreCase(this.getPropertyCodeStr())) {
                return pickorderLineProperty;
            }
        }

        return  null;

    }

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

    public double quantityDbl;
    public double getQuantityDbl() {return quantityDbl;}

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
        this.quantityDbl = 0;
    }

    public cPickorderLinePropertyValue(cPickorderLineProperty pvPickorderLineProperty) {
        this.pickorderLinePropertyValueEntity = null;

        this.lineNoInt = 0;
        this.propertyCodeStr = pvPickorderLineProperty.getPropertyCodeStr();
        this.valueStr = cAppExtension.activity.getString(R.string.novalueyet);
        this.sortingSequenceNoInt = 0;
        this.quantityDbl = 0;
    }

    public cPickorderLinePropertyValue(int pvLineNoInt, String pvPropertyCodeStr, String pvValueStr) {
        this.pickorderLinePropertyValueEntity = null;
        this.lineNoInt = pvLineNoInt;
        this.propertyCodeStr = pvPropertyCodeStr;
        this.valueStr = pvValueStr;
        this.sortingSequenceNoInt = 0;
        this.quantityDbl = 1;
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

}
