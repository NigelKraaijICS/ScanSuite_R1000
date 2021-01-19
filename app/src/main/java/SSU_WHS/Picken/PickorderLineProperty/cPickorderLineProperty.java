package SSU_WHS.Picken.PickorderLineProperty;

import androidx.lifecycle.ViewModelProvider;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ICS.cAppExtension;
import SSU_WHS.Picken.PickorderLineBarcodes.cPickorderLineBarcodeViewModel;
import SSU_WHS.Picken.PickorderLinePropertyValue.cPickorderLinePropertyValue;

public class cPickorderLineProperty {

    //Public Properties
    private Integer lineNoInt;
    public Integer getLineNoInt() {return lineNoInt;}

    private String propertyCodeStr;
    public String getPropertyCodeStr() {return propertyCodeStr;}

    private Integer sequenceNoHandledInt;
    public Integer getSequenceNoHandledInt() {return sequenceNoHandledInt;}

    private Integer sortingSequenceNoInt;
    public Integer getSortingSequenceNoInt() {return sortingSequenceNoInt;}

    private String layoutStr;
    public String getLayoutStr() {return layoutStr;}

    private boolean isInputBln;
    public boolean getIsInputBln() {return isInputBln;}

    private boolean isRequiredBln;
    public boolean getIsRequiredBln() {return isRequiredBln;}

    private String valueHandledStr;
    public String getValueHandledStr() {return valueHandledStr;}

    private cPickorderLinePropertyEntity pickorderLinePropertyEntity;

    public static ArrayList<cPickorderLineProperty> allLinePropertysObl;

    public List<cPickorderLinePropertyValue> propertyValueObl() {

        List<cPickorderLinePropertyValue> resultObl = new ArrayList<>();

        if (cPickorderLinePropertyValue.allLinePropertysValuesObl == null || cPickorderLinePropertyValue.allLinePropertysValuesObl.size() == 0)  {
            return  resultObl;
        }

        for (cPickorderLinePropertyValue pickorderLinePropertyValue : cPickorderLinePropertyValue.allLinePropertysValuesObl) {
            if (pickorderLinePropertyValue.getLineNoInt() == this.getLineNoInt()) {
                resultObl.add(pickorderLinePropertyValue);
            }
        }

        return  resultObl;

    }

    private cPickorderLinePropertyViewModel getPickorderLinePropertyViewModel() {
        return new ViewModelProvider(cAppExtension.fragmentActivity).get(cPickorderLinePropertyViewModel.class);
    }

    public cPickorderLineProperty(JSONObject pvJsonObject) {
        this.pickorderLinePropertyEntity = new cPickorderLinePropertyEntity(pvJsonObject);
        this.lineNoInt = this.pickorderLinePropertyEntity.getLineNoInt();
        this.propertyCodeStr = this.pickorderLinePropertyEntity.getPropertyCodeStr();
        this.sequenceNoHandledInt = this.pickorderLinePropertyEntity.getSequenceNoHandledInt();
        this.sortingSequenceNoInt = this.pickorderLinePropertyEntity.getSortingSequenceNoInt();
        this.layoutStr = this.pickorderLinePropertyEntity.getLayoutStr();
        this.isInputBln = this.pickorderLinePropertyEntity.getIsInputBln();
        this.isRequiredBln = this.pickorderLinePropertyEntity.getIsRequiredBln();
        this.valueHandledStr = this.pickorderLinePropertyEntity.getValueHandledStr();
    }



    public boolean pInsertInDatabaseBln() {
        this.getPickorderLinePropertyViewModel().insert(this.pickorderLinePropertyEntity);

        if (cPickorderLineProperty.allLinePropertysObl == null) {
            cPickorderLineProperty.allLinePropertysObl = new ArrayList<>();
        }
        cPickorderLineProperty.allLinePropertysObl.add(this);
        return true;
    }

    public boolean pDeleteFromDatabaseBln() {
        if (cPickorderLineProperty.allLinePropertysObl != null) {
            cPickorderLineProperty.allLinePropertysObl.remove(this);
        }
        return true;
    }

    public static boolean pTruncateTableBln() {

        cPickorderLinePropertyViewModel pickorderLinePropertyViewModel =  new ViewModelProvider(cAppExtension.fragmentActivity).get(cPickorderLinePropertyViewModel.class);
        pickorderLinePropertyViewModel.deleteAll();
        cPickorderLineProperty.allLinePropertysObl = null;
        return true;
    }

}
