package SSU_WHS.Basics.Settings;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

import SSU_WHS.General.cDatabase;

@Entity(tableName=cDatabase.TABLENAME_SETTINGS)
public class cSettingsEntity {

    //Region Public Properties
    @PrimaryKey @NonNull
    @ColumnInfo(name= cDatabase.SETTING_NAMESTR)
    public String name = "";
    public String getNameStr() {return this.name;}

    @ColumnInfo(name= cDatabase.VALUE_NAMESTR)
    public String value;
    public String getValueStr() {return this.value;}

    //End Region Public Properties

    public  cSettingsEntity(){

    }

    public cSettingsEntity(JSONObject jsonObject) {
        try {
            this.name = jsonObject.getString(cDatabase.SETTING_NAMESTR);
            this.value = jsonObject.getString(cDatabase.VALUE_NAMESTR);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    //End Region Constructor
}
