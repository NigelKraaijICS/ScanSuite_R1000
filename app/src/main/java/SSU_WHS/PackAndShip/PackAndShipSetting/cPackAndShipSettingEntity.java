package SSU_WHS.PackAndShip.PackAndShipSetting;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

import SSU_WHS.General.cDatabase;

@Entity(tableName= cDatabase.TABLENAME_PACKANDSHIPSETTING)
public class cPackAndShipSettingEntity {

    @PrimaryKey(autoGenerate = true)
    public Integer recordid;

    @ColumnInfo(name = cDatabase.SETTINGCODE_NAMESTR)
    public String settingCodeStr;
    public String getSettingCodeStr() {return this.settingCodeStr;}

    @ColumnInfo(name = cDatabase.SETTINGVALUE_NAMESTR)
    public String settingValueStr;
    public String getSettingValueStr() {return this.settingValueStr;}

    //empty constructor
    public cPackAndShipSettingEntity() {

    }

    public cPackAndShipSettingEntity(JSONObject pvJsonObject) {
        try {

            this.settingCodeStr = pvJsonObject.getString(cDatabase.SETTINGCODE_NAMESTR);
            this.settingValueStr = pvJsonObject.getString(cDatabase.SETTINGVALUE_NAMESTR);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
