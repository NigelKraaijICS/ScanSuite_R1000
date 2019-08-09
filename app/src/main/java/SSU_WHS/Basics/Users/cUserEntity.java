package SSU_WHS.Basics.Users;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import SSU_WHS.General.cDatabase;

@Entity(tableName=cDatabase.TABLENAME_USERS)
public class cUserEntity {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name="Username")
    public String username;
    @ColumnInfo(name="Name")
    public String name;
    @ColumnInfo(name="Importfile")
    public String importfile;

    //empty constructor
    public cUserEntity() {

    }
    public cUserEntity(JSONObject jsonObject) {
        try {
            username = jsonObject.getString(cDatabase.USERNAME_NAMESTR);
            name = jsonObject.getString(cDatabase.NAME_NAMESTR);
            importfile = jsonObject.getString(cDatabase.IMPORTFILE_NAMESTR);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getUsernameStr() {return this.username;}
    public String getNameStr() {return this.name;}
    public String getImportfileStr() {return this.importfile;}

}
