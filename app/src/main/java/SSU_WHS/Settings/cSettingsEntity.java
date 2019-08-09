package SSU_WHS.Settings;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import SSU_WHS.cDatabase;

@Entity(tableName=cDatabase.TABLENAME_SETTINGS)
public class cSettingsEntity {
    @PrimaryKey @NonNull
    @ColumnInfo(name="Name")
    public String name;
    @ColumnInfo(name="Value")
    public String value;

    //empty constructor
    public cSettingsEntity() {

    }

    public cSettingsEntity(JSONObject jsonObject) {
        try {
            name = jsonObject.getString(cDatabase.SETTING_NAMESTR);
            value = jsonObject.getString(cDatabase.VALUE_NAMESTR);
            //description = jsonObject.getString(cDatabase.DESCRIPTIONDUTCH_NAMESTR);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public String getNameStr() {return this.name;}
    public String getValueStr() {return this.value;}
    //public String getDescriptionStr() {return this.description;}

    @Ignore
    public cSettingsEntity(String name, String value) {
        this.name = name;
        this.value = value;
        //this.description = description;
    }
}
