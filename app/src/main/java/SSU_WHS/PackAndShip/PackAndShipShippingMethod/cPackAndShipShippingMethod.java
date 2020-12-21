package SSU_WHS.PackAndShip.PackAndShipShippingMethod;

import androidx.lifecycle.ViewModelProvider;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ICS.cAppExtension;
import SSU_WHS.PackAndShip.PackAndShipSetting.cPackAndShipSettingEntity;
import SSU_WHS.PackAndShip.PackAndShipSetting.cPackAndShipSettingViewModel;

public class cPackAndShipShippingMethod {

    public String shippingMethodCodeStr;
    public String getShippingMethodCodeStr() { return shippingMethodCodeStr; }

    public String shippingMethodValueStr;
    public String getShippingMethodValueStr() {
        return shippingMethodValueStr;
    }


    private cPackAndShipShippingMethodEntity packAndShipShippingMethodEntity;

    public static List<cPackAndShipShippingMethod> allShippingMethodsObl;
    public static cPackAndShipShippingMethod cPackAndShipShippingMethod;

    private cPackAndShipShippingMethodViewModel getPackAndShipShippingMethodViewModel() {
        return new ViewModelProvider(cAppExtension.fragmentActivity).get(cPackAndShipShippingMethodViewModel.class);
    }


    //Region Public Properties


    //End Region Public Properties

    //Region Constructor
    public cPackAndShipShippingMethod(JSONObject pvJsonObject) {
        this.packAndShipShippingMethodEntity = new cPackAndShipShippingMethodEntity(pvJsonObject);

        this.shippingMethodCodeStr = this.packAndShipShippingMethodEntity.getShippingMethodCodeStr();
        this.shippingMethodValueStr = this.packAndShipShippingMethodEntity.getShippingMethodValueStr();
    }


    //End Region Constructor


    public boolean pInsertInDatabaseBln() {

        if (cPackAndShipShippingMethod.allShippingMethodsObl == null){
            cPackAndShipShippingMethod.allShippingMethodsObl = new ArrayList<>();
        }
        cPackAndShipShippingMethod.allShippingMethodsObl.add(this);
        return  true;
    }

    public static boolean pTruncateTableBln(){

        cPackAndShipShippingMethodViewModel packAndShipShippingMethodViewModel =  new ViewModelProvider(cAppExtension.fragmentActivity).get(cPackAndShipShippingMethodViewModel.class);
        packAndShipShippingMethodViewModel.deleteAll();
        return true;
    }

}
