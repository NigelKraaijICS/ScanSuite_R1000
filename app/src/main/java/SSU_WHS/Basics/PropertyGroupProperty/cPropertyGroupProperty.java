package SSU_WHS.Basics.PropertyGroupProperty;

import androidx.lifecycle.ViewModelProvider;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ICS.Weberror.cWeberror;
import ICS.cAppExtension;
import SSU_WHS.Basics.PropertyGroup.cPropertyGroupViewModel;
import SSU_WHS.Move.MoveorderLines.cMoveorderLine;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebserviceDefinitions;

public class cPropertyGroupProperty implements Comparable {

    private String originStr;
    public String getOriginStr() { return originStr; }

    private String originKeyStr;
    public String getOriginKeyStr() { return originKeyStr; }

    private String propertyStr;
    public String getPropertyStr() { return propertyStr; }

    private int orderInt;
    public int getOrderInt() { return orderInt; }

    private cPropertyGroupPropertyViewModel getPropertyGroupPropertyViewModel() {
        return new ViewModelProvider(cAppExtension.fragmentActivity).get(cPropertyGroupPropertyViewModel.class);
    }

    private cPropertyGroupPropertyEntity propertyGroupPropertyEntity;

    //Region Constructor
    public cPropertyGroupProperty(JSONObject pvJsonObject) {
        this.propertyGroupPropertyEntity = new cPropertyGroupPropertyEntity(pvJsonObject);
        this.originStr = this.propertyGroupPropertyEntity.getOriginStr();
        this.originKeyStr = this.propertyGroupPropertyEntity.getOriginKeyStr();
        this.propertyStr = this.propertyGroupPropertyEntity.getPropertyStr();
        this.orderInt = this.propertyGroupPropertyEntity.getOrderInt();
    }

    //End Region Constructor

    //Region Public Methods
    public boolean pInsertInDatabaseBln() {
        this.getPropertyGroupPropertyViewModel().insert(this.propertyGroupPropertyEntity);
        return true;
    }

    public static boolean pTruncateTableBln(){
        cPropertyGroupPropertyViewModel propertyGroupPropertyViewModel =  new ViewModelProvider(cAppExtension.fragmentActivity).get(cPropertyGroupPropertyViewModel.class);
        propertyGroupPropertyViewModel.deleteAll();
        return true;
    }

    //End Region Public Methods

    //Region Private Methods

    @Override
    public int compareTo(Object o) {
        int compareint =((cPropertyGroupProperty)o).getOrderInt();
        return compareint-this.getOrderInt();

    }

    //End Region Private Methods
}
