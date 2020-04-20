package SSU_WHS.Basics.Packaging;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import org.json.JSONException;
import org.json.JSONObject;

import SSU_WHS.General.cDatabase;

@Entity(tableName = cDatabase.TABLENAME_PACKAGING, primaryKeys = {cDatabase.EMBALLAGE_NAMESTR})
public class cPackagingEntity {

    //Region Public Properties
    @NonNull
    @ColumnInfo(name = cDatabase.EMBALLAGE_NAMESTR)
    public String codeStr = "";
    @NonNull
    public String getCodeStr() {return codeStr;}

    @NonNull
    @ColumnInfo(name = cDatabase.DESCRIPTION_NAMESTR)
    public String descriptionStr = "";
    @NonNull
    public String getDescriptionStr() {return descriptionStr;}



    //End Region Public Properies

    //Region Constructor
    public cPackagingEntity() {

    }

    public cPackagingEntity(JSONObject jsonObject) {
        try {
            this.codeStr = jsonObject.getString(cDatabase.EMBALLAGE_NAMESTR);
            this.descriptionStr = jsonObject.getString(cDatabase.DESCRIPTION_DUTCH_NAMESTR);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    //End Region Constructor
}


