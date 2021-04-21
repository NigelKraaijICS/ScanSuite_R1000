package SSU_WHS.LineItemProperty.LinePropertyValue;

import androidx.lifecycle.ViewModelProvider;

import org.json.JSONObject;

import java.util.ArrayList;

import ICS.Utils.cText;
import ICS.cAppExtension;
import SSU_WHS.Basics.ItemProperty.cItemProperty;
import SSU_WHS.LineItemProperty.LineProperty.cLineProperty;
import nl.icsvertex.scansuite.R;

public class cLinePropertyValue implements Comparable{

    //Public Properties
    public int lineNoInt;

    public int getLineNoInt() {return lineNoInt;}

    private String propertyCodeStr;
    public String getPropertyCodeStr() {return propertyCodeStr;}

    public cLineProperty getLineProperty(){

        if (this.getPropertyCodeStr().isEmpty() || cLineProperty.allLinePropertysObl == null || cLineProperty.allLinePropertysObl.size() == 0) {
            return  null;
        }

        for (cLineProperty lineProperty :cLineProperty.allLinePropertysObl ) {
            if (lineProperty.getLineNoInt().equals(this.getLineNoInt()) && lineProperty.getPropertyCodeStr().equalsIgnoreCase(this.getPropertyCodeStr())) {
                return lineProperty;
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

    private cLinePropertyValueEntity linePropertyValueEntity;

    public static cLinePropertyValue currentLinePropertyValue;
    public static ArrayList<cLinePropertyValue> allLinePropertysValuesObl;

    public static ArrayList<cLinePropertyValue> allowedPropertyValuesObl;

    private cLinePropertyValueViewModel getLinePropertyValueViewModel() {
        return new ViewModelProvider(cAppExtension.fragmentActivity).get(cLinePropertyValueViewModel.class);
    }

    public cLinePropertyValue(JSONObject pvJsonObject) {
        this.linePropertyValueEntity = new cLinePropertyValueEntity(pvJsonObject);
        this.lineNoInt = this.linePropertyValueEntity.getLineNoInt();
        this.propertyCodeStr = this.linePropertyValueEntity.getPropertyCodeStr();
        this.valueStr = this.linePropertyValueEntity.getValueStr();
        this.sortingSequenceNoInt = this.linePropertyValueEntity.getSortingSequenceNoInt();
        this.quantityDbl = 0;
    }

    public cLinePropertyValue(cLineProperty lineProperty) {
        this.linePropertyValueEntity = null;

        this.lineNoInt = lineProperty.getLineNoInt();
        this.propertyCodeStr = lineProperty.getPropertyCodeStr();
        this.valueStr = cAppExtension.activity.getString(R.string.novalueyet);
        this.sortingSequenceNoInt = 0;
        this.quantityDbl = 0;
    }

    public cLinePropertyValue(int pvLineNoInt, String pvPropertyCodeStr, String pvValueStr) {
        this.linePropertyValueEntity = null;
        this.lineNoInt = pvLineNoInt;
        this.propertyCodeStr = pvPropertyCodeStr;
        this.valueStr = pvValueStr;
        this.sortingSequenceNoInt = 0;
        this.quantityDbl = 1;
    }

    public boolean pInsertInDatabaseBln() {
        this.getLinePropertyValueViewModel().insert(this.linePropertyValueEntity);

        if (cLinePropertyValue.allLinePropertysValuesObl == null) {
            cLinePropertyValue.allLinePropertysValuesObl = new ArrayList<>();
        }
        if (cLinePropertyValue.allowedPropertyValuesObl == null) {
            cLinePropertyValue.allowedPropertyValuesObl = new ArrayList<>();
        }

        for (cLinePropertyValue linePropertyValue : cLinePropertyValue.allLinePropertysValuesObl){
            if (linePropertyValue.getLineNoInt() == this.getLineNoInt() && linePropertyValue.getPropertyCodeStr().equalsIgnoreCase(this.getPropertyCodeStr())){
                return true;
            }
        }

        cLinePropertyValue.allLinePropertysValuesObl.add(this);
        return true;
    }

    public boolean pDeleteFromDatabaseBln() {
        if (cLinePropertyValue.allLinePropertysValuesObl != null) {
            cLinePropertyValue.allLinePropertysValuesObl.remove(this);
        }
        return true;
    }

    public static boolean pTruncateTableBln() {

        cLinePropertyValueViewModel linePropertyValueViewModel =  new ViewModelProvider(cAppExtension.fragmentActivity).get(cLinePropertyValueViewModel.class);
        linePropertyValueViewModel.deleteAll();
        cLinePropertyValue.allowedPropertyValuesObl = null;
        return true;
    }

    @Override
    public int compareTo(Object o) {
        int compareint = cText.pDoubleToInt(((cLinePropertyValue)o).getQuantityDbl());
        return compareint- cText.pDoubleToInt(this.getQuantityDbl());

    }

}
