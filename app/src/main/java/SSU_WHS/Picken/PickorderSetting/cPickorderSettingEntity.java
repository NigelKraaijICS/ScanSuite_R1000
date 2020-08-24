package SSU_WHS.Picken.PickorderSetting;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import org.json.JSONException;
import org.json.JSONObject;

import SSU_WHS.General.cDatabase;

@Entity(tableName = cDatabase.TABLENAME_PICKORDERSETTING, primaryKeys = {cDatabase.SETTINGCODE_NAMESTR})

public class cPickorderSettingEntity {

    //Region Public Properties

    @NonNull
    @ColumnInfo(name = cDatabase.SETTINGCODE_NAMESTR)
    public String settingCode = "";
    public String getSettingCodeStr() {
        return settingCode;
    }

    @ColumnInfo(name = cDatabase.SETTINGVALUE_NAMESTR)
    public String settingValue= "";
    public String getSettingValue() {
        return settingValue;
    }


    //End Region Public Properties

    //Region Private Properties

    //End Region Private Properties

    //Region Constructor

    public cPickorderSettingEntity() {
        //empty constructor
    }

    public cPickorderSettingEntity(JSONObject pvJsonObject) {
        try {
            this.settingCode = pvJsonObject.getString(cDatabase.SETTINGCODE_NAMESTR);
            this.settingValue = pvJsonObject.getString(cDatabase.SETTINGVALUE_NAMESTR);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //End Region Constructor















}


