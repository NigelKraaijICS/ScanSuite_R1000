package SSU_WHS.Picken.PickorderSetting;

import androidx.lifecycle.ViewModelProvider;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ICS.cAppExtension;
import SSU_WHS.Picken.PickorderAddresses.cPickorderAddressEntity;
import SSU_WHS.Picken.PickorderAddresses.cPickorderAddressViewModel;

public class cPickorderSetting {

    private String settingCodeStr;
    public String getSettingCodeStr() { return settingCodeStr; }

    private String settingValueStr;
    public String getSettingValueStr() { return settingValueStr; }



    private cPickorderSettingViewModel getPickorderSettingViewModel() {
        return new ViewModelProvider(cAppExtension.fragmentActivity).get(cPickorderSettingViewModel.class);
    }

    private cPickorderSettingEntity pickorderSettingEntity;
    public static List<cPickorderSetting> allSettingObl;

    //Region Constructor

    public cPickorderSetting(JSONObject pvJsonObject) {
        this.pickorderSettingEntity = new cPickorderSettingEntity(pvJsonObject);
        this.settingCodeStr = this.pickorderSettingEntity.getSettingCodeStr();
        this.settingValueStr = this.pickorderSettingEntity.getSettingValue();
    }
    //End Region Constructor

    public boolean pInsertInDatabaseBln() {

        this.getPickorderSettingViewModel().insert(this.pickorderSettingEntity);

        if (cPickorderSetting.allSettingObl == null) {
            cPickorderSetting.allSettingObl = new ArrayList<>();
        }

        cPickorderSetting.allSettingObl.add(this);
        return true;
    }

    public static boolean pTruncateTableBln(){
        cPickorderSettingViewModel pickorderSettingViewModel =   new ViewModelProvider(cAppExtension.fragmentActivity).get(cPickorderSettingViewModel.class);
        pickorderSettingViewModel.deleteAll();
        return true;
    }


}
