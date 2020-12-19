package SSU_WHS.PackAndShip.PackAndShipSetting;

import androidx.lifecycle.ViewModelProvider;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ICS.cAppExtension;

public class cPackAndShipSetting {

    public String settingCodeStr;
    public String getSettingCodeStr() { return settingCodeStr; }

    public String settingValueStr;
    public String getSettingValueStr() {
        return settingValueStr;
    }


    private cPackAndShipSettingEntity packAndShipSettingEntity;

    public static List<cPackAndShipSetting> allSettingsObl;
    public static cPackAndShipSetting currentPackAndShipSetting;

    private cPackAndShipSettingViewModel getPackAndShipSettingViewModel() {
        return new ViewModelProvider(cAppExtension.fragmentActivity).get(cPackAndShipSettingViewModel.class);
    }


    //Region Public Properties


    //End Region Public Properties

    //Region Constructor
    public cPackAndShipSetting(JSONObject pvJsonObject) {
        this.packAndShipSettingEntity = new cPackAndShipSettingEntity(pvJsonObject);

        this.settingCodeStr = this.packAndShipSettingEntity.getSettingCodeStr();
        this.settingValueStr = this.packAndShipSettingEntity.getSettingValueStr();
    }


    //End Region Constructor


    public boolean pInsertInDatabaseBln() {

        if (cPackAndShipSetting.allSettingsObl == null){
            cPackAndShipSetting.allSettingsObl = new ArrayList<>();
        }
        cPackAndShipSetting.allSettingsObl.add(this);
        return  true;
    }

    public static boolean pTruncateTableBln(){

        cPackAndShipSettingViewModel cPackAndShipSettingViewModel =  new ViewModelProvider(cAppExtension.fragmentActivity).get(cPackAndShipSettingViewModel.class);
        cPackAndShipSettingViewModel.deleteAll();
        return true;
    }

}
