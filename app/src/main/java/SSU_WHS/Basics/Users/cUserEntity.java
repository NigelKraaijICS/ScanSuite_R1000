package SSU_WHS.Basics.Users;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

import SSU_WHS.General.cDatabase;

@Entity(tableName= cDatabase.TABLENAME_USERS)
public class cUserEntity {

    //Region Public Properties
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = cDatabase.USERNAME_NAMESTR)
    public String username = "";
    public String getUsernameStr() {return this.username;}

    @ColumnInfo(name= cDatabase.NAMEDUTCH_NAMESTR)
    public String name = "";
    public String getNameStr() {return this.name;}

    //End Region Public Properties

    //Region Constructor
    public cUserEntity() {

    }

    public cUserEntity(JSONObject pvJsonObject) {
        try {
            this.username = pvJsonObject.getString(cDatabase.USERNAME_NAMESTR);
            this.name = pvJsonObject.getString(cDatabase.NAMEDUTCH_NAMESTR);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    //End Region Constructor





}
