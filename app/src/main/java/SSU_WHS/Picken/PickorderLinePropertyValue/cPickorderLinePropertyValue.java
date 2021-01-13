package SSU_WHS.Picken.PickorderLinePropertyValue;

import androidx.lifecycle.ViewModelProvider;

import org.json.JSONObject;

import java.util.ArrayList;

import ICS.cAppExtension;
import SSU_WHS.Picken.PickorderLineProperty.cPickorderLinePropertyEntity;
import SSU_WHS.Picken.PickorderLineProperty.cPickorderLinePropertyViewModel;

public class cPickorderLinePropertyValue {

    //Public Properties
    private int lineNoInt;
    public int getLineNoInt() {return lineNoInt;}

    private String propertyCodeStr;
    public String getPropertyCodeStr() {return propertyCodeStr;}

    private String valueStr;
    public String getValueStr() {return valueStr;}

    private int sortingSequenceNoInt;
    public  int getSortingSequenceNoInt(){return sortingSequenceNoInt;}

    private cPickorderLinePropertyValueEntity pickorderLinePropertyValueEntity;

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
